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

                        // Tìm file cập nhật trong danh sách assets đính kèm của release
                        downloadUrl = "";
                        downloadFileName = "";
                        JsonValue assets = root.get("assets");
                        if (assets != null) {
                            // Ưu tiên 1: File .jar chạy trực tiếp
                            for (JsonValue asset = assets.child; asset != null; asset = asset.next) {
                                String name = asset.getString("name", "");
                                if (name.toLowerCase().endsWith(".jar")) {
                                    downloadUrl = asset.getString("browser_download_url", "");
                                    downloadFileName = name;
                                    break;
                                }
                            }
                            // Ưu tiên 2: Gói nén .zip (chứa .jar) hoặc file chạy .exe
                            if (downloadUrl.isEmpty()) {
                                for (JsonValue asset = assets.child; asset != null; asset = asset.next) {
                                    String name = asset.getString("name", "");
                                    if (name.toLowerCase().endsWith(".zip") || name.toLowerCase().endsWith(".exe")) {
                                        downloadUrl = asset.getString("browser_download_url", "");
                                        downloadFileName = name;
                                        break;
                                    }
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

                // Hỗ trợ đa tầng HTTP 302/301/307/308 redirects tự động của GitHub Releases -> AWS S3 CDN
                int redirectCount = 0;
                while (redirectCount < 5) {
                    int resp = conn.getResponseCode();
                    if (resp == HttpURLConnection.HTTP_MOVED_TEMP || resp == HttpURLConnection.HTTP_MOVED_PERM || resp == 307 || resp == 308) {
                        String newUrl = conn.getHeaderField("Location");
                        conn.disconnect();
                        conn = (HttpURLConnection) new URL(newUrl).openConnection();
                        conn.setRequestProperty("User-Agent", "AnglerRPG-Updater/" + GameVersion.CURRENT_VERSION);
                        conn.setConnectTimeout(8000);
                        conn.setReadTimeout(15000);
                        conn.setInstanceFollowRedirects(true);
                        redirectCount++;
                    } else {
                        break;
                    }
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

                // Nếu file tải về là gói nén ZIP thì tự động giải nén file .jar bên trong
                File fileToApply = tempTarget;
                if ((downloadFileName != null && downloadFileName.toLowerCase().endsWith(".zip")) || isZipArchive(tempTarget)) {
                    statusMessage = "Đang giải nén gói cập nhật...";
                    File extractedJar = extractJarFromZip(tempTarget);
                    if (extractedJar != null && extractedJar.exists()) {
                        tempTarget.delete();
                        fileToApply = extractedJar;
                    } else {
                        throw new IOException("Không tìm thấy tệp game (.jar) bên trong gói cập nhật .zip!");
                    }
                }

                statusMessage = "Tải thành công! Đang chuẩn bị khởi động lại...";

                if (progressCallback != null) {
                    Gdx.app.postRunnable(progressCallback::onComplete);
                }

                // Thực hiện thay thế và khởi động lại
                executeRestartScript(fileToApply);
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
    private static boolean isZipArchive(File file) {
        if (file == null || !file.exists() || file.length() < 4) return false;
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] header = new byte[4];
            if (fis.read(header) == 4) {
                return header[0] == 0x50 && header[1] == 0x4B && header[2] == 0x03 && header[3] == 0x04;
            }
        } catch (Exception ignored) {}
        return false;
    }

    private static File extractJarFromZip(File zipFile) throws IOException {
        File extractedJar = new File("AnglerRPG-extracted.tmp");
        if (extractedJar.exists()) extractedJar.delete();
        boolean found = false;
        try (java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)))) {
            java.util.zip.ZipEntry entry;
            byte[] buf = new byte[8192];

            while ((entry = zis.getNextEntry()) != null) {
                String name = entry.getName();
                if (entry.isDirectory() || name.startsWith("__MACOSX")) {
                    zis.closeEntry();
                    continue;
                }

                if (name.toLowerCase().endsWith(".jar")) {
                    try (OutputStream out = new BufferedOutputStream(new FileOutputStream(extractedJar))) {
                        int len;
                        while ((len = zis.read(buf)) != -1) {
                            out.write(buf, 0, len);
                        }
                        out.flush();
                    }
                    found = true;
                    zis.closeEntry();
                    break;
                } else {
                    File outFile = new File(name);
                    String canonicalDest = new File(".").getCanonicalPath();
                    if (outFile.getCanonicalPath().startsWith(canonicalDest)) {
                        File parent = outFile.getParentFile();
                        if (parent != null) parent.mkdirs();
                        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(outFile))) {
                            int len;
                            while ((len = zis.read(buf)) != -1) {
                                out.write(buf, 0, len);
                            }
                            out.flush();
                        }
                    }
                    zis.closeEntry();
                }
            }
        }

        return found ? extractedJar : null;
    }

    /**
     * Tạo script thay thế file và khởi động lại game tự động.
     */
    private void executeRestartScript(File tempFile) {
        try {
            // Xác định chính xác tên file JAR đang chạy của ứng dụng (mặc định là AnglerRPG.jar)
            String targetJarName = "AnglerRPG.jar";
            try {
                File runningLocation = new File(AutoUpdater.class.getProtectionDomain().getCodeSource().getLocation().toURI());
                if (runningLocation.isFile() && runningLocation.getName().toLowerCase().endsWith(".jar")) {
                    targetJarName = runningLocation.getName();
                }
            } catch (Exception ignored) {}

            boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
            if (isWindows) {
                File bat = new File("update_apply.bat");
                try (OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(bat), java.nio.charset.StandardCharsets.UTF_8)) {
                    fw.write("@echo off\r\n");
                    fw.write("chcp 65001 > nul\r\n");
                    fw.write("echo [AnglerRPG Updater] Đang chờ tiến trình game cũ đóng hoàn toàn...\r\n");
                    fw.write("for /L %%i in (1,1,10) do (\r\n");
                    fw.write("    move /y \"" + tempFile.getName() + "\" \"" + targetJarName + "\" > nul 2>&1 && goto move_done\r\n");
                    fw.write("    timeout /t 1 /nobreak > nul\r\n");
                    fw.write(")\r\n");
                    fw.write(":move_done\r\n");
                    fw.write("echo [AnglerRPG Updater] Khởi chạy game phiên bản mới...\r\n");
                    fw.write("start \"\" javaw -jar \"" + targetJarName + "\" || start \"\" java -jar \"" + targetJarName + "\"\r\n");
                    fw.write("if exist \"AnglerRPG-update.tmp\" del /f /q \"AnglerRPG-update.tmp\" > nul 2>&1\r\n");
                    fw.write("start /b \"\" cmd /c \"del update_apply.bat\" > nul 2>&1\r\n");
                    fw.write("exit\r\n");
                }

                Runtime.getRuntime().exec("cmd /c start \"\" \"" + bat.getAbsolutePath() + "\"");
            } else {
                File sh = new File("update_apply.sh");
                try (OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(sh), java.nio.charset.StandardCharsets.UTF_8)) {
                    fw.write("#!/bin/sh\n");
                    fw.write("for i in $(seq 1 10); do\n");
                    fw.write("    mv -f \"" + tempFile.getName() + "\" \"" + targetJarName + "\" 2>/dev/null && break\n");
                    fw.write("    sleep 1\n");
                    fw.write("done\n");
                    fw.write("java -jar \"" + targetJarName + "\" &\n");
                    fw.write("rm -f \"$0\"\n");
                }
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
