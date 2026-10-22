package com.fishingrpg.game.systems.updater;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Module Tự Động Cập Nhật Trực Tuyến (Auto-Updater).
 * - Tự động kiểm tra bản phát hành mới nhất từ GitHub Releases qua API.
 * - Cho phép tải trực tiếp file game mới và kích hoạt script tự động thay thế & khởi động lại.
 * - Hoạt động hoàn toàn trên luồng nền (asynchronous background thread), không làm đơ giao diện game.
 */
public class AutoUpdater {

    private static final String TAG = "AutoUpdater";
    private static AutoUpdater instance;

    public enum Status {
        IDLE,
        CHECKING,
        UPDATE_AVAILABLE,
        UP_TO_DATE,
        DOWNLOADING,
        READY_TO_RESTART,
        ERROR
    }

    private Status status = Status.IDLE;
    private String latestVersion = "";
    private String releaseNotes = "";
    private String downloadUrl = "";
    private String downloadFileName = "";
    private float downloadProgress = 0f;
    private String statusMessage = "";

    public interface UpdateCheckCallback {
        void onResult(boolean updateAvailable, String latestVer, String notes);
    }

    public interface DownloadProgressCallback {
        void onProgress(float progress);
        void onComplete();
        void onError(String errorMsg);
    }

    public static synchronized AutoUpdater getInstance() {
        if (instance == null) {
            instance = new AutoUpdater();
        }
        return instance;
    }

    private AutoUpdater() {
    }

    /**
     * Kiểm tra cập nhật bất đồng bộ từ GitHub Releases API.
     */
    public void checkForUpdates(UpdateCheckCallback callback) {
        if (status == Status.CHECKING || status == Status.DOWNLOADING) return;

        status = Status.CHECKING;
        statusMessage = "Đang kiểm tra bản cập nhật mới...";

        Thread checkThread = new Thread(() -> {
            try {
                URL url = new URL(GameVersion.RELEASES_API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "AnglerRPG-Updater/" + GameVersion.CURRENT_VERSION);
                conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
                conn.setConnectTimeout(4000);
                conn.setReadTimeout(4000);

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();
                    conn.disconnect();

                    JsonValue root = new JsonReader().parse(response.toString());
                    String tag = root.getString("tag_name", "").trim();
                    String body = root.getString("body", "Không có thông tin ghi chú phiên bản.");

                    String cleanRemote = tag.replace("v", "").replace("V", "").trim();
                    String cleanCurrent = GameVersion.CURRENT_VERSION.trim();

                    if (isNewerVersion(cleanRemote, cleanCurrent)) {
                        latestVersion = tag;
                        releaseNotes = body;

                        // Tìm file .jar trong danh sách assets đính kèm của release
                        downloadUrl = "";
                        JsonValue assets = root.get("assets");
                        if (assets != null) {
                            for (JsonValue asset = assets.child; asset != null; asset = asset.next) {
                                String name = asset.getString("name", "");
                                if (name.endsWith(".jar") || name.endsWith(".exe") || name.endsWith(".zip")) {
                                    downloadUrl = asset.getString("browser_download_url", "");
                                    downloadFileName = name;
                                    break;
                                }
                            }
                        }

                        status = Status.UPDATE_AVAILABLE;
                        statusMessage = "Phát hiện bản mới: " + tag;
                        Gdx.app.log(TAG, "Bản cập nhật mới sẵn sàng: " + tag + " (Hiện tại: " + cleanCurrent + ")");
                        if (callback != null) {
                            Gdx.app.postRunnable(() -> callback.onResult(true, latestVersion, releaseNotes));
                        }
                    } else {
                        status = Status.UP_TO_DATE;
                        statusMessage = "Bạn đang chơi phiên bản mới nhất (" + GameVersion.getDisplayVersion() + ").";
                        if (callback != null) {
                            Gdx.app.postRunnable(() -> callback.onResult(false, cleanCurrent, ""));
                        }
                    }
                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    status = Status.UP_TO_DATE;
                    statusMessage = "Chưa có bản phát hành trên GitHub Releases.";
                    if (callback != null) Gdx.app.postRunnable(() -> callback.onResult(false, "", ""));
                } else {
                    status = Status.ERROR;
                    statusMessage = "Không thể kiểm tra bản mới (Mã lỗi: " + responseCode + ").";
                    if (callback != null) Gdx.app.postRunnable(() -> callback.onResult(false, "", ""));
                }
            } catch (Exception e) {
                status = Status.ERROR;
                statusMessage = "Lỗi kết nối kiểm tra cập nhật.";
                Gdx.app.log(TAG, "Kiểm tra cập nhật thất bại: " + e.getMessage());
                if (callback != null) Gdx.app.postRunnable(() -> callback.onResult(false, "", ""));
            }
        }, "AnglerRPG-UpdateCheckThread");

        checkThread.setDaemon(true);
        checkThread.start();
    }

    /**
     * Tải tệp cập nhật và tự động áp dụng (thay thế file JAR và khởi động lại game).
     */
    public void startDownloadAndApply(DownloadProgressCallback progressCallback) {
        if (downloadUrl == null || downloadUrl.isEmpty()) {
            if (progressCallback != null) {
                progressCallback.onError("Không tìm thấy đường link tải tệp cập nhật trên GitHub.");
            }
            return;
        }

        status = Status.DOWNLOADING;
        downloadProgress = 0f;
        statusMessage = "Đang tải bản cập nhật...";

        Thread downloadThread = new Thread(() -> {
            File tempTarget = new File("AnglerRPG-update.tmp");
            try {
                URL url = new URL(downloadUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "AnglerRPG-Updater/" + GameVersion.CURRENT_VERSION);
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(15000);
                conn.setInstanceFollowRedirects(true);

                // Hỗ trợ HTTP 302/301 redirects tự động của GitHub Releases -> AWS S3 CDN
                int status = conn.getResponseCode();
                if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == 307 || status == 308) {
                    String newUrl = conn.getHeaderField("Location");
                    conn.disconnect();
                    conn = (HttpURLConnection) new URL(newUrl).openConnection();
                    conn.setRequestProperty("User-Agent", "AnglerRPG-Updater/" + GameVersion.CURRENT_VERSION);
                }

                long totalBytes = conn.getContentLengthLong();
                InputStream in = new BufferedInputStream(conn.getInputStream());
                OutputStream out = new FileOutputStream(tempTarget);

                byte[] buffer = new byte[8192];
                long downloadedBytes = 0;
                int bytesRead;

                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                    downloadedBytes += bytesRead;
                    if (totalBytes > 0) {
                        float prog = (float) downloadedBytes / (float) totalBytes;
                        downloadProgress = prog;
                        if (progressCallback != null) {
                            Gdx.app.postRunnable(() -> progressCallback.onProgress(prog));
                        }
                    }
                }

                out.flush();
                out.close();
                in.close();
                conn.disconnect();

                this.status = Status.READY_TO_RESTART;
                downloadProgress = 1f;
                statusMessage = "Tải thành công! Đang chuẩn bị khởi động lại...";

                if (progressCallback != null) {
                    Gdx.app.postRunnable(progressCallback::onComplete);
                }

                // Thực hiện thay thế và khởi động lại
                executeRestartScript(tempTarget);

            } catch (Exception e) {
                this.status = Status.ERROR;
                statusMessage = "Lỗi tải cập nhật: " + e.getMessage();
                Gdx.app.error(TAG, "Tải cập nhật thất bại", e);
                if (tempTarget.exists()) tempTarget.delete();
                if (progressCallback != null) {
                    Gdx.app.postRunnable(() -> progressCallback.onError(e.getMessage()));
                }
            }
        }, "AnglerRPG-DownloadThread");

        downloadThread.setDaemon(true);
        downloadThread.start();
    }

    /**
     * Tạo script thay thế file và khởi động lại game tự động.
     */
    private void executeRestartScript(File tempFile) {
        try {
            boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
            if (isWindows) {
                File bat = new File("update_apply.bat");
                String targetJarName = (downloadFileName != null && !downloadFileName.isEmpty()) ? downloadFileName : "AnglerRPG.jar";
                
                FileWriter fw = new FileWriter(bat);
                fw.write("@echo off\r\n");
                fw.write("chcp 65001 > nul\r\n");
                fw.write("timeout /t 1 /nobreak > nul\r\n");
                fw.write("echo [AnglerRPG Updater] Đang cập nhật phiên bản mới...\r\n");
                fw.write("move /y \"" + tempFile.getName() + "\" \"" + targetJarName + "\" > nul\r\n");
                fw.write("echo [AnglerRPG Updater] Khởi chạy game...\r\n");
                fw.write("start \"\" javaw -jar \"" + targetJarName + "\" || start \"\" java -jar \"" + targetJarName + "\"\r\n");
                fw.write("del \"%~f0\" > nul 2>&1\r\n");
                fw.write("exit\r\n");
                fw.close();

                Runtime.getRuntime().exec("cmd /c start \"\" \"" + bat.getAbsolutePath() + "\"");
            } else {
                File sh = new File("update_apply.sh");
                FileWriter fw = new FileWriter(sh);
                fw.write("#!/bin/sh\n");
                fw.write("sleep 1\n");
                fw.write("mv -f \"" + tempFile.getName() + "\" \"AnglerRPG.jar\"\n");
                fw.write("java -jar \"AnglerRPG.jar\" &\n");
                fw.write("rm -f \"$0\"\n");
                fw.close();
                sh.setExecutable(true);
                Runtime.getRuntime().exec(new String[]{"/bin/sh", sh.getAbsolutePath()});
            }

            Gdx.app.postRunnable(() -> {
                Gdx.app.exit();
                System.exit(0);
            });
        } catch (Exception e) {
            Gdx.app.error(TAG, "Lỗi tạo script tự động khởi động lại", e);
        }
    }

    /**
     * So sánh 2 chuỗi phiên bản chuẩn Semantic Versioning (vd: "0.4.22" > "0.4.21").
     */
    public static boolean isNewerVersion(String remoteVer, String localVer) {
        if (remoteVer == null || remoteVer.isEmpty()) return false;
        if (localVer == null || localVer.isEmpty()) return true;

        String[] rParts = remoteVer.split("\\.");
        String[] lParts = localVer.split("\\.");
        int maxLen = Math.max(rParts.length, lParts.length);

        for (int i = 0; i < maxLen; i++) {
            int rVal = 0;
            int lVal = 0;
            if (i < rParts.length) {
                try { rVal = Integer.parseInt(rParts[i].replaceAll("\\D+", "")); } catch (Exception ignored) {}
            }
            if (i < lParts.length) {
                try { lVal = Integer.parseInt(lParts[i].replaceAll("\\D+", "")); } catch (Exception ignored) {}
            }
            if (rVal > lVal) return true;
            if (rVal < lVal) return false;
        }
        return false;
    }

    public boolean isUpdateAvailable() {
        return status == Status.UPDATE_AVAILABLE;
    }

    public Status getStatus() {
        return status;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public float getDownloadProgress() {
        return downloadProgress;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
