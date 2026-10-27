package com.fishingrpg.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.fishingrpg.game.FishingRPG;
import com.fishingrpg.game.systems.AudioManager;
import com.fishingrpg.game.systems.updater.AutoUpdater;
import com.fishingrpg.game.systems.updater.GameVersion;

/**
 * Hộp thoại cập nhật game tự động (In-Game Auto-Updater Modal Dialog).
 * - Hiển thị trạng thái kiểm tra phiên bản mới từ GitHub.
 * - Cho phép tải và tự động thay thế & khởi động lại chỉ với 1 click.
 * - Hỗ trợ mở link GitHub trực tiếp nếu muốn tải thủ công.
 */
public class UpdateDialog {

    private final FishingRPG game;
    private final GlyphLayout layout = new GlyphLayout();

    private boolean visible = false;
    private float pulseTimer = 0f;

    // Kích thước Dialog
    private static final float W = 720f;
    private static final float H = 460f;
    private final float dlgX = (1280f - W) / 2f;
    private final float dlgY = (720f - H) / 2f;

    // Tọa độ các nút bấm cân bằng và giãn cách chuẩn
    private float closeBtnX, closeBtnY, closeBtnW, closeBtnH;
    private float actionBtnX, actionBtnY, actionBtnW, actionBtnH;
    private float secondBtnX, secondBtnY, secondBtnW, secondBtnH;
    private float githubBtnX, githubBtnY, githubBtnW, githubBtnH;

    // Hover states
    private boolean hoverClose = false;
    private boolean hoverAction = false;
    private boolean hoverSecond = false;
    private boolean hoverGithub = false;

    private int ignoreClickFrames = 0;

    public UpdateDialog(FishingRPG game) {
        this.game = game;
        updateButtonBounds();
    }

    private void updateButtonBounds() {
        closeBtnW = 32f;
        closeBtnH = 32f;
        closeBtnX = dlgX + W - closeBtnW - 16f;
        closeBtnY = dlgY + H - closeBtnH - 16f;

        // Bố cục 3 nút chuẩn xác: 40px lề trái, 190px mỗi nút, 35px khoảng cách, 40px lề phải
        secondBtnW = 190f;
        secondBtnH = 46f;
        secondBtnX = dlgX + 40f;
        secondBtnY = dlgY + 32f;

        actionBtnW = 190f;
        actionBtnH = 46f;
        actionBtnX = dlgX + 265f;
        actionBtnY = dlgY + 32f;

        githubBtnW = 190f;
        githubBtnH = 46f;
        githubBtnX = dlgX + 490f;
        githubBtnY = dlgY + 32f;
    }

    public void open() {
        this.visible = true;
        this.pulseTimer = 0f;
        this.ignoreClickFrames = 3;
        AudioManager.getInstance().playSFX("sfx_button");
    }

    public void close() {
        this.visible = false;
        AudioManager.getInstance().playSFX("sfx_button");
    }

    public boolean isOpen() {
        return visible;
    }

    public boolean handleInput(Vector3 mouse) {
        if (!visible) return false;

        // Bỏ qua các frame đầu ngay khi vừa mở để tránh nhận nhầm click từ màn hình chính
        if (ignoreClickFrames > 0) {
            ignoreClickFrames--;
            return true;
        }

        AutoUpdater updater = AutoUpdater.getInstance();
        AutoUpdater.Status st = updater.getStatus();

        // Hover tracking theo từng trạng thái bố cục nút
        hoverClose = isInside(mouse.x, mouse.y, closeBtnX, closeBtnY, closeBtnW, closeBtnH);
        if (st == AutoUpdater.Status.UP_TO_DATE) {
            float uSecondX = dlgX + 130f;
            float uActionX = dlgX + 380f;
            float uBtnW = 210f;
            hoverSecond = isInside(mouse.x, mouse.y, uSecondX, secondBtnY, uBtnW, secondBtnH);
            hoverAction = isInside(mouse.x, mouse.y, uActionX, actionBtnY, uBtnW, actionBtnH);
            hoverGithub = false;
        } else {
            hoverSecond = isInside(mouse.x, mouse.y, secondBtnX, secondBtnY, secondBtnW, secondBtnH);
            hoverAction = isInside(mouse.x, mouse.y, actionBtnX, actionBtnY, actionBtnW, actionBtnH);
            hoverGithub = isInside(mouse.x, mouse.y, githubBtnX, githubBtnY, githubBtnW, githubBtnH);
        }

        if (Gdx.input.justTouched()) {
            if (hoverClose) {
                close();
                return true;
            }

            // Click ngoài khung để đóng nếu không phải đang tải (chỉ đóng khi đã mở ổn định > 0.25s)
            if (!isInside(mouse.x, mouse.y, dlgX, dlgY, W, H)) {
                if (st != AutoUpdater.Status.DOWNLOADING && pulseTimer > 0.25f) {
                    close();
                    return true;
                }
            }

            if (st == AutoUpdater.Status.UPDATE_AVAILABLE) {
                // Nút để sau
                if (hoverSecond) {
                    close();
                    return true;
                }
                // Nút mở GitHub
                if (hoverAction) {
                    AudioManager.getInstance().playSFX("sfx_button");
                    Gdx.net.openURI("https://github.com/" + GameVersion.GITHUB_OWNER + "/" + GameVersion.GITHUB_REPO + "/releases");
                    return true;
                }
                // Nút tải cập nhật tự động
                if (hoverGithub) {
                    AudioManager.getInstance().playSFX("sfx_button");
                    updater.startDownloadAndApply(null);
                    return true;
                }
            } else if (st == AutoUpdater.Status.UP_TO_DATE) {
                // Nút Đóng
                if (hoverAction) {
                    close();
                    return true;
                }
                // Nút kiểm tra lại
                if (hoverSecond) {
                    AudioManager.getInstance().playSFX("sfx_button");
                    updater.checkForUpdates(null);
                    return true;
                }
            } else if (st == AutoUpdater.Status.ERROR) {
                // Nút thử lại
                if (hoverSecond) {
                    AudioManager.getInstance().playSFX("sfx_button");
                    updater.checkForUpdates(null);
                    return true;
                }
                // Nút đóng
                if (hoverAction) {
                    close();
                    return true;
                }
                // Nút mở GitHub tải thủ công
                if (hoverGithub) {
                    AudioManager.getInstance().playSFX("sfx_button");
                    Gdx.net.openURI("https://github.com/" + GameVersion.GITHUB_OWNER + "/" + GameVersion.GITHUB_REPO + "/releases");
                    return true;
                }
            }
            return true;
        }

        return true; // Chặn tương tác bên dưới khi dialog mở
    }

    private boolean isInside(float mx, float my, float x, float y, float w, float h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }

    public void render(float delta, Vector3 mouse) {
        if (!visible) return;

        pulseTimer += delta;

        ShapeRenderer sr = game.shapeRenderer;
        SpriteBatch batch = game.batch;
        BitmapFont font = game.font;
        BitmapFont titleFont = game.titleFont;
        BitmapFont smallFont = game.smallFont;

        AutoUpdater updater = AutoUpdater.getInstance();
        AutoUpdater.Status st = updater.getStatus();

        // 1. VẼ NỀN VÀ KHUNG SHAPES
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Màn che mờ toàn màn hình
        sr.setColor(0.02f, 0.04f, 0.08f, 0.78f);
        sr.rect(0, 0, 1280f, 720f);

        // Nền Dialog chính
        sr.setColor(0.08f, 0.12f, 0.20f, 0.96f);
        sr.rect(dlgX, dlgY, W, H);

        // Thanh Header
        sr.setColor(0.12f, 0.18f, 0.30f, 1f);
        sr.rect(dlgX, dlgY + H - 65f, W, 65f);

        // Viền Header
        sr.setColor(0.25f, 0.40f, 0.65f, 0.8f);
        sr.rect(dlgX, dlgY + H - 67f, W, 2f);

        // Viền ngoài Dialog
        float pulse = (MathUtils.sin(pulseTimer * 3f) + 1f) * 0.5f;
        sr.setColor(0.35f + 0.15f * pulse, 0.55f + 0.15f * pulse, 0.90f, 0.95f);
        drawRectOutline(sr, dlgX, dlgY, W, H, 2.5f);

        // Nút Đóng (X)
        if (st != AutoUpdater.Status.DOWNLOADING) {
            sr.setColor(hoverClose ? new Color(0.85f, 0.25f, 0.25f, 0.9f) : new Color(0.20f, 0.25f, 0.38f, 0.8f));
            sr.rect(closeBtnX, closeBtnY, closeBtnW, closeBtnH);
            sr.setColor(1f, 1f, 1f, 0.4f);
            drawRectOutline(sr, closeBtnX, closeBtnY, closeBtnW, closeBtnH, 1.2f);
        }

        // Khung nội dung giữa
        float contentBoxX = dlgX + 35f;
        float contentBoxY = dlgY + 100f;
        float contentBoxW = W - 70f;
        float contentBoxH = H - 185f;

        sr.setColor(0.05f, 0.08f, 0.14f, 0.85f);
        sr.rect(contentBoxX, contentBoxY, contentBoxW, contentBoxH);
        sr.setColor(0.18f, 0.26f, 0.42f, 0.7f);
        drawRectOutline(sr, contentBoxX, contentBoxY, contentBoxW, contentBoxH, 1.5f);

        // Vẽ thanh tiến trình nếu đang tải
        if (st == AutoUpdater.Status.DOWNLOADING) {
            float barX = contentBoxX + 40f;
            float barY = contentBoxY + 80f;
            float barW = contentBoxW - 80f;
            float barH = 26f;

            // Thanh nền
            sr.setColor(0.02f, 0.04f, 0.08f, 1f);
            sr.rect(barX, barY, barW, barH);

            // Thanh tiến trình đã tải
            float progress = MathUtils.clamp(updater.getDownloadProgress(), 0f, 1f);
            sr.setColor(0.20f, 0.75f, 0.95f, 1f);
            sr.rect(barX + 2f, barY + 2f, (barW - 4f) * progress, barH - 4f);

            // Viền ngoài thanh tải
            sr.setColor(0.40f, 0.85f, 1f, 0.8f);
            drawRectOutline(sr, barX, barY, barW, barH, 1.5f);
        }

        // Vẽ các nút bấm theo trạng thái
        if (st == AutoUpdater.Status.UPDATE_AVAILABLE) {
            // Nút Để Sau (Trái)
            sr.setColor(hoverSecond ? new Color(0.35f, 0.40f, 0.50f, 1f) : new Color(0.20f, 0.25f, 0.35f, 1f));
            sr.rect(secondBtnX, secondBtnY, secondBtnW, secondBtnH);
            sr.setColor(0.50f, 0.65f, 0.80f, 0.7f);
            drawRectOutline(sr, secondBtnX, secondBtnY, secondBtnW, secondBtnH, 1.5f);

            // Nút GitHub (Giữa)
            sr.setColor(hoverAction ? new Color(0.25f, 0.35f, 0.60f, 1f) : new Color(0.15f, 0.25f, 0.45f, 1f));
            sr.rect(actionBtnX, actionBtnY, actionBtnW, actionBtnH);
            sr.setColor(0.45f, 0.65f, 0.95f, 0.8f);
            drawRectOutline(sr, actionBtnX, actionBtnY, actionBtnW, actionBtnH, 1.5f);

            // Nút Tải Tự Động (Phải)
            sr.setColor(hoverGithub ? new Color(0.15f, 0.65f, 0.40f, 1f) : new Color(0.10f, 0.50f, 0.30f, 1f));
            sr.rect(githubBtnX, githubBtnY, githubBtnW, githubBtnH);
            sr.setColor(0.40f, 0.95f, 0.60f, 0.9f);
            drawRectOutline(sr, githubBtnX, githubBtnY, githubBtnW, githubBtnH, 1.8f);

        } else if (st == AutoUpdater.Status.UP_TO_DATE) {
            float uSecondX = dlgX + 130f;
            float uActionX = dlgX + 380f;
            float uBtnW = 210f;

            // Nút Kiểm Tra Lại
            sr.setColor(hoverSecond ? new Color(0.25f, 0.35f, 0.50f, 1f) : new Color(0.16f, 0.22f, 0.34f, 1f));
            sr.rect(uSecondX, secondBtnY, uBtnW, secondBtnH);
            sr.setColor(0.40f, 0.55f, 0.75f, 0.7f);
            drawRectOutline(sr, uSecondX, secondBtnY, uBtnW, secondBtnH, 1.5f);

            // Nút Đóng
            sr.setColor(hoverAction ? new Color(0.25f, 0.50f, 0.80f, 1f) : new Color(0.18f, 0.35f, 0.60f, 1f));
            sr.rect(uActionX, actionBtnY, uBtnW, actionBtnH);
            sr.setColor(0.45f, 0.70f, 1f, 0.9f);
            drawRectOutline(sr, uActionX, actionBtnY, uBtnW, actionBtnH, 1.8f);

        } else if (st == AutoUpdater.Status.ERROR) {
            // Nút Thử Lại (Trái)
            sr.setColor(hoverSecond ? new Color(0.35f, 0.45f, 0.65f, 1f) : new Color(0.20f, 0.30f, 0.45f, 1f));
            sr.rect(secondBtnX, secondBtnY, secondBtnW, secondBtnH);
            sr.setColor(0.50f, 0.70f, 0.95f, 0.8f);
            drawRectOutline(sr, secondBtnX, secondBtnY, secondBtnW, secondBtnH, 1.5f);

            // Nút Đóng (Giữa)
            sr.setColor(hoverAction ? new Color(0.25f, 0.50f, 0.80f, 1f) : new Color(0.18f, 0.35f, 0.60f, 1f));
            sr.rect(actionBtnX, actionBtnY, actionBtnW, actionBtnH);
            sr.setColor(0.45f, 0.70f, 1f, 0.9f);
            drawRectOutline(sr, actionBtnX, actionBtnY, actionBtnW, actionBtnH, 1.8f);

            // Nút Mở GitHub (Phải)
            sr.setColor(hoverGithub ? new Color(0.30f, 0.45f, 0.70f, 1f) : new Color(0.18f, 0.28f, 0.48f, 1f));
            sr.rect(githubBtnX, githubBtnY, githubBtnW, githubBtnH);
            sr.setColor(0.50f, 0.75f, 1f, 0.8f);
            drawRectOutline(sr, githubBtnX, githubBtnY, githubBtnW, githubBtnH, 1.5f);
        }

        sr.end();

        // 2. VẼ CHỮ VÀ BIỂU TƯỢNG (TEXT RENDERING)
        batch.begin();

        // Tiêu đề Header (đã bỏ AUTO-UPDATER để không đè nút X)
        titleFont.setColor(1f, 0.92f, 0.45f, 1f);
        String titleStr = "✦ TRÌNH CẬP NHẬT TỰ ĐỘNG ✦";
        layout.setText(titleFont, titleStr);
        titleFont.draw(batch, titleStr, dlgX + 35f, dlgY + H - 24f);

        // Nút Đóng X
        if (st != AutoUpdater.Status.DOWNLOADING) {
            font.setColor(1f, 1f, 1f, hoverClose ? 1f : 0.75f);
            font.draw(batch, "X", closeBtnX + 10f, closeBtnY + 23f);
        }

        // Nội dung theo trạng thái
        if (st == AutoUpdater.Status.CHECKING) {
            font.setColor(0.85f, 0.92f, 1f, 1f);
            font.draw(batch, "Đang kết nối tới GitHub Releases...", contentBoxX + 40f, contentBoxY + contentBoxH - 60f);
            smallFont.setColor(0.65f, 0.78f, 0.90f, 0.85f);
            smallFont.draw(batch, "Vui lòng đợi giây lát trong khi kiểm tra thông tin phiên bản mới nhất...", contentBoxX + 40f, contentBoxY + contentBoxH - 95f);

        } else if (st == AutoUpdater.Status.UP_TO_DATE) {
            titleFont.setColor(0.35f, 0.95f, 0.55f, 1f);
            titleFont.draw(batch, "✓ BẠN ĐANG CHƠI PHIÊN BẢN MỚI NHẤT!", contentBoxX + 40f, contentBoxY + contentBoxH - 50f);

            font.setColor(1f, 1f, 1f, 1f);
            font.draw(batch, "Phiên bản hiện tại: " + GameVersion.getDisplayVersion() + " (Phát hành: " + GameVersion.BUILD_DATE + ")", contentBoxX + 40f, contentBoxY + contentBoxH - 100f);

            smallFont.setColor(0.70f, 0.85f, 0.95f, 0.9f);
            smallFont.draw(batch, "Không có bản cập nhật mới nào. Game của bạn đã sở hữu tất cả các tính năng & tối ưu mới nhất.", contentBoxX + 40f, contentBoxY + contentBoxH - 135f);
            smallFont.draw(batch, "Thời gian thực máy chủ luôn được đồng bộ trực tuyến tự động.", contentBoxX + 40f, contentBoxY + contentBoxH - 160f);

            float uSecondX = dlgX + 130f;
            float uActionX = dlgX + 380f;
            float uBtnW = 210f;

            // Nút Kiểm tra lại & Đóng
            font.setColor(1f, 1f, 1f, 1f);
            layout.setText(font, "KIỂM TRA LẠI");
            font.draw(batch, "KIỂM TRA LẠI", uSecondX + (uBtnW - layout.width) / 2f, secondBtnY + 30f);

            layout.setText(font, "ĐÓNG");
            font.draw(batch, "ĐÓNG", uActionX + (uBtnW - layout.width) / 2f, actionBtnY + 30f);

        } else if (st == AutoUpdater.Status.UPDATE_AVAILABLE) {
            titleFont.setColor(1f, 0.88f, 0.35f, 1f);
            titleFont.draw(batch, "★ ĐÃ CÓ BẢN CẬP NHẬT MỚI: " + updater.getLatestVersion() + "!", contentBoxX + 30f, contentBoxY + contentBoxH - 35f);

            font.setColor(0.85f, 0.92f, 1f, 1f);
            font.draw(batch, "Phiên bản hiện tại: " + GameVersion.getDisplayVersion() + "  →  Bản mới: " + updater.getLatestVersion(), contentBoxX + 30f, contentBoxY + contentBoxH - 72f);

            smallFont.setColor(0.75f, 0.88f, 0.98f, 0.95f);
            smallFont.draw(batch, "Ghi chú cập nhật:", contentBoxX + 30f, contentBoxY + contentBoxH - 105f);

            // Hiển thị tóm tắt ghi chú phát hành
            String notes = updater.getReleaseNotes();
            if (notes != null && !notes.isEmpty()) {
                String[] lines = notes.split("\n");
                float lineY = contentBoxY + contentBoxH - 130f;
                int shown = 0;
                for (String line : lines) {
                    if (shown >= 4) {
                        smallFont.draw(batch, "... và nhiều tối ưu khác (Xem chi tiết trên GitHub)", contentBoxX + 30f, lineY);
                        break;
                    }
                    line = line.trim();
                    if (!line.isEmpty()) {
                        if (line.length() > 70) line = line.substring(0, 67) + "...";
                        smallFont.draw(batch, "• " + line, contentBoxX + 30f, lineY);
                        lineY -= 22f;
                        shown++;
                    }
                }
            } else {
                smallFont.draw(batch, "Bản cập nhật tối ưu hóa hiệu năng, sửa lỗi và thêm nội dung mới.", contentBoxX + 30f, contentBoxY + contentBoxH - 130f);
            }

            // Nút bấm
            font.setColor(1f, 1f, 1f, 1f);
            layout.setText(font, "ĐỂ SAU");
            font.draw(batch, "ĐỂ SAU", secondBtnX + (secondBtnW - layout.width) / 2f, secondBtnY + 30f);

            layout.setText(font, "MỞ GITHUB");
            font.draw(batch, "MỞ GITHUB", actionBtnX + (actionBtnW - layout.width) / 2f, actionBtnY + 30f);

            layout.setText(font, "TẢI TỰ ĐỘNG");
            font.draw(batch, "TẢI TỰ ĐỘNG", githubBtnX + (githubBtnW - layout.width) / 2f, githubBtnY + 30f);

        } else if (st == AutoUpdater.Status.DOWNLOADING) {
            titleFont.setColor(0.35f, 0.85f, 1f, 1f);
            titleFont.draw(batch, "ĐANG TẢI BẢN CẬP NHẬT...", contentBoxX + 40f, contentBoxY + contentBoxH - 45f);

            int percent = (int)(updater.getDownloadProgress() * 100);
            font.setColor(1f, 1f, 1f, 1f);
            font.draw(batch, "Tiến độ: " + percent + "%", contentBoxX + 40f, contentBoxY + 130f);

            smallFont.setColor(0.70f, 0.85f, 0.95f, 0.9f);
            smallFont.draw(batch, "Hệ thống đang tự động tải gói cài đặt từ GitHub CDN về máy.", contentBoxX + 40f, contentBoxY + 45f);
            smallFont.draw(batch, "Vui lòng không tắt trò chơi. Game sẽ tự khởi động lại khi hoàn thành.", contentBoxX + 40f, contentBoxY + 20f);

        } else if (st == AutoUpdater.Status.READY_TO_RESTART) {
            titleFont.setColor(0.35f, 0.95f, 0.55f, 1f);
            titleFont.draw(batch, "✓ TẢI XUỐNG HOÀN TẤT 100%!", contentBoxX + 40f, contentBoxY + contentBoxH - 50f);

            font.setColor(1f, 1f, 1f, 1f);
            font.draw(batch, "Game đang tự động áp dụng bản vá và chuẩn bị khởi động lại...", contentBoxX + 40f, contentBoxY + contentBoxH - 100f);

        } else if (st == AutoUpdater.Status.ERROR) {
            titleFont.setColor(0.95f, 0.35f, 0.35f, 1f);
            titleFont.draw(batch, "! KHÔNG THỂ CẬP NHẬT TỰ ĐỘNG", contentBoxX + 30f, contentBoxY + contentBoxH - 40f);

            font.setColor(0.95f, 0.80f, 0.80f, 1f);
            font.draw(batch, updater.getStatusMessage(), contentBoxX + 30f, contentBoxY + contentBoxH - 85f);

            smallFont.setColor(0.70f, 0.85f, 0.95f, 0.85f);
            smallFont.draw(batch, "Bạn có thể thử lại hoặc bấm 'MỞ TRANG GITHUB' để tải file JAR mới nhất thủ công.", contentBoxX + 30f, contentBoxY + contentBoxH - 120f);

            font.setColor(1f, 1f, 1f, 1f);
            layout.setText(font, "THỬ LẠI");
            font.draw(batch, "THỬ LẠI", secondBtnX + (secondBtnW - layout.width) / 2f, secondBtnY + 30f);

            layout.setText(font, "ĐÓNG");
            font.draw(batch, "ĐÓNG", actionBtnX + (actionBtnW - layout.width) / 2f, actionBtnY + 30f);

            layout.setText(font, "MỞ TRANG GITHUB");
            font.draw(batch, "MỞ TRANG GITHUB", githubBtnX + (githubBtnW - layout.width) / 2f, githubBtnY + 30f);
        }

        batch.end();
    }

    private void drawRectOutline(ShapeRenderer sr, float x, float y, float w, float h, float thickness) {
        sr.rect(x, y, w, thickness);
        sr.rect(x, y + h - thickness, w, thickness);
        sr.rect(x, y, thickness, h);
        sr.rect(x + w - thickness, y, thickness, h);
    }
}
