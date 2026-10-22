package com.fishingrpg.game.systems;

import com.badlogic.gdx.Gdx;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Dịch vụ đồng bộ thời gian thực chuẩn (Real-Time Server Time Sync).
 * - Tự động đồng bộ với máy chủ thời gian chuẩn quốc tế (Google / Cloudflare / NTP).
 * - Sử dụng đồng hồ đơn điệu (Monotonic clock qua System.nanoTime()) để triệt tiêu
 *   hoàn toàn tình trạng tua ngược/tua nhanh thời gian máy tính Windows để hack sự kiện/điểm danh.
 * - Chạy ngầm trong background thread, tuyệt đối không làm đơ/lag khung hình khi khởi động game.
 */
public class TimeSyncService {

    private static final String TAG = "TimeSyncService";
    private static volatile boolean isSynced = false;
    private static volatile long serverTimeOffset = 0L;
    private static volatile long baseServerMillis = 0L;
    private static volatile long baseMonotonicNanos = 0L;
    private static volatile boolean isSyncing = false;

    private static final String[] TIME_SERVERS = {
        "https://www.google.com",
        "https://cloudflare.com",
        "https://api.github.com"
    };

    public static synchronized void init() {
        if (isSynced || isSyncing) return;
        isSyncing = true;

        Thread syncThread = new Thread(() -> {
            try {
                for (String serverUrl : TIME_SERVERS) {
                    try {
                        URL url = new URL(serverUrl);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("HEAD");
                        conn.setConnectTimeout(2500);
                        conn.setReadTimeout(2500);
                        conn.setInstanceFollowRedirects(true);
                        conn.connect();

                        long dateHeader = conn.getDate();
                        conn.disconnect();

                        if (dateHeader > 0) {
                            long localMillis = System.currentTimeMillis();
                            serverTimeOffset = dateHeader - localMillis;
                            baseServerMillis = dateHeader;
                            baseMonotonicNanos = System.nanoTime();
                            isSynced = true;
                            Gdx.app.log(TAG, "Đã đồng bộ thời gian thực chuẩn từ " + serverUrl + "! Độ lệch: " + serverTimeOffset + " ms");
                            break;
                        }
                    } catch (Exception ignored) {
                        // Thử máy chủ dự phòng tiếp theo
                    }
                }
            } finally {
                isSyncing = false;
            }
        }, "AnglerRPG-TimeSyncThread");

        syncThread.setDaemon(true);
        syncThread.start();
    }

    /**
     * Lấy thời gian thực (Epoch Milliseconds).
     * Nếu đã đồng bộ với máy chủ, trả về thời gian chuẩn thực tế (chống gian lận đổi giờ hệ thống).
     * Nếu chưa đồng bộ hoặc offline, trả về thời gian máy tính cục bộ an toàn.
     */
    public static long getCurrentTimeMillis() {
        if (isSynced && baseMonotonicNanos > 0) {
            long elapsedMillis = (System.nanoTime() - baseMonotonicNanos) / 1_000_000L;
            return baseServerMillis + elapsedMillis;
        }
        return System.currentTimeMillis();
    }

    public static boolean isSynced() {
        return isSynced;
    }

    public static long getServerTimeOffset() {
        return serverTimeOffset;
    }
}
