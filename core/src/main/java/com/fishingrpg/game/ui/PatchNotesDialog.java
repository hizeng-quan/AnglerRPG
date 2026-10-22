package com.fishingrpg.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.fishingrpg.game.FishingRPG;
import com.fishingrpg.game.systems.AudioManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Hop thoai thong bao chi tiet cac ban cap nhat moi va lo trinh phat trien (In-Game Patch Notes & Roadmap).
 * Co the mo tu MainMenuScreen hoac truc tiep tu GameHUD.
 */
public class PatchNotesDialog {

    public static final String CURRENT_VERSION = "v0.4.18";
    private static final String PREF_LAST_SEEN_VERSION = "last_seen_patch_notes_version";

    private final FishingRPG game;
    private final GlyphLayout layout = new GlyphLayout();

    private boolean visible = false;
    private int currentTab = 0; // 0: Bản cập nhật, 1: Kế hoạch & Lộ trình
    private float scrollY = 0f;
    private float maxScrollY = 0f;

    // Kích thước Dialog
    private static final float W = 960f;
    private static final float H = 600f;
    private float dlgX = (1280f - W) / 2f;
    private float dlgY = (720f - H) / 2f;

    // Hover states
    private boolean hoverTab0 = false;
    private boolean hoverTab1 = false;
    private boolean hoverClose = false;

    // Cấu trúc một mục cập nhật
    public static class UpdateItem {
        public final boolean isVersionHeader;
        public final String badge;
        public final String title;
        public final Color badgeColor;
        public final List<String> bulletPoints = new ArrayList<>();

        public UpdateItem(String badge, String title, Color badgeColor) {
            this(false, badge, title, badgeColor);
        }

        public UpdateItem(boolean isVersionHeader, String badge, String title, Color badgeColor) {
            this.isVersionHeader = isVersionHeader;
            this.badge = badge;
            this.title = title;
            this.badgeColor = badgeColor;
        }

        public UpdateItem addPoint(String point) {
            bulletPoints.add(point);
            return this;
        }

        public static UpdateItem versionHeader(String title) {
            return new UpdateItem(true, "PHIÊN BẢN", title, new Color(1f, 0.88f, 0.35f, 1f));
        }
    }

    private final List<UpdateItem> patchNotesItems = new ArrayList<>();
    private final List<UpdateItem> roadmapItems = new ArrayList<>();

    public PatchNotesDialog(FishingRPG game) {
        this.game = game;
        initContent();
    }

    private void initContent() {
        Color fixBadgeColor = new Color(0.25f, 0.85f, 0.50f, 1f);
        Color newBadgeColor = new Color(0.35f, 0.80f, 1f, 1f);
        Color dataBadgeColor = new Color(1f, 0.75f, 0.25f, 1f);

        // =====================================================================
        // --- TAB 0: BẢN TIN CẬP NHẬT THEO TỪNG PHIÊN BẢN ---
        // =====================================================================

        // ===== PHIÊN BẢN v0.4.18 (MỚI NHẤT) =====
        patchNotesItems.add(UpdateItem.versionHeader("PHIÊN BẢN v0.4.18 (MỚI NHẤT - ĐẠI TU NGOẠI HÌNH SET THỜI KHÔNG & VECTOR PIPELINE)"));

        patchNotesItems.add(new UpdateItem("ĐỒ HỌA", "Đại Tu Ngoại Hình Toàn Diện Bộ Trang Bị Thời Không", newBadgeColor)
            .addPoint("Trượng Thời Gian (Chrono Staff): Thiết kế thiên văn hoàng đạo Astrolabe với bánh răng 12 khắc giờ, kim la bàn xoay, lõi tinh thể ngọc lam và hào quang tinh vân vũ trụ.")
            .addPoint("Sợi Dây Xuyên Thấu (Transdimensional Line): Cấu trúc cuộn dây Tachyon vi mô lồng đôi, 8 vòng tròn năng lượng cộng hưởng gradient và chùm vệt photon lượng tử xuyên không gian.")
            .addPoint("Lưỡi Câu Hư Không (Void Singularity Hook): Đường cong không-thời gian dạng vòng vô cực Möbius Topology, lưỡi thép Titanium sắc bén và hố đen Singularity tím thẫm phát quang.")
            .addPoint("Căn chỉnh hoàn hảo tâm điểm (cx, cy) và kích thước chuẩn xác, loại bỏ hoàn toàn lỗi hình ảnh lệch khung hoặc tràn viền icon.")
        );

        patchNotesItems.add(new UpdateItem("CÔNG CỤ", "Tích Hợp Vector Studio MCP & Graphics Pipeline", newBadgeColor)
            .addPoint("Tích hợp Vector Studio MCP Server hỗ trợ thiết kế, kiểm duyệt và chuyển đổi SVG sang mã lệnh LibGDX ShapeRenderer.")
            .addPoint("Thiết lập bộ quy chuẩn đồ họa (Graphics Guidelines) và Visual Quality Gate tự động hóa 100/100 điểm.")
            .addPoint("Bổ sung bước Dọn dẹp sau khi tích hợp (Post-apply Cleanup): Tự động xóa bỏ các file ảnh nháp so sánh cũ, file thử nghiệm và tài nguyên trung gian sau khi đã áp dụng code vào game.")
        );

        // ===== PHIÊN BẢN v0.4.17 =====
        patchNotesItems.add(UpdateItem.versionHeader("PHIÊN BẢN v0.4.17 (CÀI ĐẶT, KEYBINDS & SỬA LỖI UI)"));

        patchNotesItems.add(new UpdateItem("TÍNH NĂNG", "Tùy Chỉnh Toàn Bộ Keybinds & Chống Trùng Phím", newBadgeColor)
            .addPoint("Hỗ trợ đổi phím cho cả 14 thao tác: Kéo dây, 4 ô Kỹ năng và toàn bộ 9 ô Phím Tắt Nhanh (Quickslot).")
            .addPoint("Tích hợp cơ chế bảo vệ Chống Trùng Phím (Anti-Collision): Tự động kiểm tra, cảnh báo đỏ và ngăn chặn việc gán trùng phím giữa các thao tác.")
            .addPoint("Bố cục Tab Phím Bấm dạng Danh Sách Cuộn Dọc duy nhất (Single-column scroll list) có thanh cuộn mượt mà hỗ trợ kéo chuột, con lăn và phím mũi tên.")
            .addPoint("Nâng cấp toàn bộ UI Cài Đặt theo phong cách Fantasy RPG: Corner Ornaments mạ vàng, thẻ Obsidian Glass, dải phản quang và nút bấm phát sáng.")
            .addPoint("Hỗ trợ phím F11 chuyển đổi nhanh giữa Toàn màn hình và Cửa sổ (1280x720) ở mọi nơi.")
            .addPoint("Tự động đồng bộ nhãn phím trên thanh Kỹ năng, ô Quickslot và thông báo cắn câu.")
        );

        patchNotesItems.add(new UpdateItem("ÂM THANH", "Kéo Thả Âm Lượng & Tắt Âm Độc Lập", newBadgeColor)
            .addPoint("Tắt âm Nhạc nền (BGM) và Hiệu ứng (SFX) hoạt động hoàn toàn độc lập với nhau.")
            .addPoint("Hỗ trợ kéo chuột (Drag) trực tiếp trên thanh trượt âm lượng BGM và SFX cực kỳ mượt mà.")
        );

        patchNotesItems.add(new UpdateItem("GIAO DIỆN", "Tối Ưu Giao Diện & Làm Sạch Chú Thích Cài Đặt", fixBadgeColor)
            .addPoint("Xóa bỏ toàn bộ các dòng chú thích rườm rà ở tất cả các tab Cài đặt, tối ưu không gian hiển thị rộng rãi, chuyên nghiệp.")
            .addPoint("Khắc phục triệt để lỗi chữ của Tab Dữ Liệu bị đè xuyên qua Modal Cảnh Báo Xóa Dữ Liệu.")
            .addPoint("Khắc phục lỗi lệch Hitbox nhấn chuột của các nút Xuất, Nạp và Xóa Dữ Liệu.")
            .addPoint("Ngẫu nhiên hóa bản tin News Ticker ở Game HUD thay vì luôn xuất hiện tin đầu tiên.")
            .addPoint("Rà soát và xóa các chỉ dẫn phím cứng trong ngoặc đơn để phù hợp với tùy biến Keybind.")
        );

        patchNotesItems.add(new UpdateItem("DỮ LIỆU", "Quản Lý Dữ Liệu Tập Trung Trong Cài Đặt", dataBadgeColor)
            .addPoint("Bỏ toàn bộ các nút xuất, nạp, reset dữ liệu bên Menu chính và quy về một mối trong mục Cài đặt.")
            .addPoint("Xuất dữ liệu tự động gắn Giờ + Ngày Tháng Năm Timestamp vào tên file để chống trùng lặp.")
            .addPoint("Nạp dữ liệu từ file hệ thống hoặc Clipboard; Xóa dữ liệu an toàn với đếm ngược 10 giây và tự động sao lưu dự phòng.")
        );

        // ===== PHIÊN BẢN v0.4.16 =====
        patchNotesItems.add(UpdateItem.versionHeader("PHIÊN BẢN v0.4.16 (KHẮC PHỤC LỖI & ĐIỀU HƯỚNG)"));

        patchNotesItems.add(new UpdateItem("SỬA LỖI", "Sửa Lỗi Crash Game Khi Thoát Gacha", fixBadgeColor)
            .addPoint("Sửa lỗi thoát khỏi màn hình Gacha ra game HUD bấm vào một mục bất kì có khả năng crash game.")
        );

        patchNotesItems.add(new UpdateItem("SỬA LỖI", "Sửa Lỗi Crash Khi Quay Lại Từ Trang Bị", fixBadgeColor)
            .addPoint("Sửa lỗi bấm vào phần Trang Bị sau đó ấn nút Quay lại thì bị crash game.")
        );

        patchNotesItems.add(new UpdateItem("SỬA LỖI", "Sửa Lỗi Phím Kỹ Năng Q / W / E / R", fixBadgeColor)
            .addPoint("Sửa lỗi phím kỹ năng Q, W, E, R không hoạt động hoặc không bấm được trên tài khoản mới.")
        );

        patchNotesItems.add(new UpdateItem("SỬA LỖI", "Sửa Lỗi Nút Menu & Điều Hướng", fixBadgeColor)
            .addPoint("Sửa lại hệ thống các nút quay lại, khắc phục lỗi vòng lặp điều hướng giữa màn chơi, bản đồ và menu chính.")
        );

        patchNotesItems.add(new UpdateItem("CẬP NHẬT", "Hỗ Trợ Kỹ Năng & Hướng Dẫn Trực Quan", newBadgeColor)
            .addPoint("Tự động mở khóa và trang bị kỹ năng cơ bản Kéo Mạnh vào ô phím [Q] cho người chơi mới.")
            .addPoint("Thêm thông báo chữ nổi hướng dẫn khi bấm ô phím chưa gắn kỹ năng, kỹ năng đang hồi chiêu hoặc không đủ Thể lực.")
            .addPoint("Thêm nút thông tin bản tin ngay tại góc Game HUD để tiện theo dõi các cập nhật mới.")
        );

        // =====================================================================
        // --- TAB 1: KẾ HOẠCH & LỘ TRÌNH (SẮP TỚI) ---
        // =====================================================================
        roadmapItems.add(UpdateItem.versionHeader("CÁC TÍNH NĂNG ĐÃ HOÀN THÀNH"));

        roadmapItems.add(new UpdateItem("ĐÃ RA MẮT", "Đại Tu Ngoại Hình Toàn Diện Set Thời Không (v0.4.18)", new Color(0.35f, 0.85f, 0.50f, 1f))
            .addPoint("Đã hoàn thành làm mới toàn diện đồ họa 3 món set Thời Không và tích hợp công cụ kiểm duyệt Vector Studio.")
        );

        roadmapItems.add(new UpdateItem("ĐÃ RA MẮT", "Hệ Thống Cài Đặt & Quản Lý Dữ Liệu (v0.4.17)", new Color(0.35f, 0.85f, 0.50f, 1f))
            .addPoint("Đã hoàn thành đưa nút Setting vào HUD, điều chỉnh âm thanh, chế độ hiển thị và quản lý file lưu.")
        );

        roadmapItems.add(UpdateItem.versionHeader("MỤC TIÊU PHÁT TRIỂN TIẾP THEO"));

        roadmapItems.add(new UpdateItem("CÂN BẰNG", "Cân Bằng Thử Thách & Phần Thưởng", new Color(1f, 0.55f, 0.35f, 1f))
            .addPoint("Cân bằng phần thưởng kiếm được từ nhiệm vụ (hạn chế việc đang quá dễ kiếm kim cương để quay).")
            .addPoint("Cân bằng sức mạnh cá và trang bị (điều chỉnh lại việc trang bị đang quá mạnh so với cá).")
            .addPoint("Cân bằng tỷ lệ quay của phần sự kiện: Các vật phẩm thuộc sự kiện có tỷ lệ quay ra ngang bằng với vật phẩm thường ở vòng quay sự kiện.")
        );
    }

    public void open() {
        this.visible = true;
        this.scrollY = 0f;
        AudioManager.getInstance().playSFX("sfx_button");
        markAsSeen();
    }

    public void close() {
        this.visible = false;
        AudioManager.getInstance().playSFX("sfx_button");
    }

    public boolean isOpen() {
        return visible;
    }

    public boolean isUnread() {
        com.badlogic.gdx.Preferences prefs = Gdx.app.getPreferences("FishingRPG_Config");
        String lastSeen = prefs.getString(PREF_LAST_SEEN_VERSION, "");
        return !CURRENT_VERSION.equals(lastSeen);
    }

    public boolean shouldAutoOpenOnStartup() {
        return isUnread();
    }

    public void markAsSeen() {
        com.badlogic.gdx.Preferences prefs = Gdx.app.getPreferences("FishingRPG_Config");
        prefs.putString(PREF_LAST_SEEN_VERSION, CURRENT_VERSION);
        prefs.flush();
    }

    public boolean handleInput(Vector3 mouse) {
        if (!visible) return false;

        // Tab coordinates
        float tabW = (W - 50f) / 2f;
        float tabH = 42f;
        float tabY = dlgY + H - 110f;
        float tab0X = dlgX + 25f;
        float tab1X = tab0X + tabW;

        hoverTab0 = (mouse.x >= tab0X && mouse.x <= tab0X + tabW && mouse.y >= tabY && mouse.y <= tabY + tabH);
        hoverTab1 = (mouse.x >= tab1X && mouse.x <= tab1X + tabW && mouse.y >= tabY && mouse.y <= tabY + tabH);

        // Close button coordinates
        float btnW = 200f;
        float btnH = 44f;
        float btnX = dlgX + (W - btnW) / 2f;
        float btnY = dlgY + 22f;

        hoverClose = (mouse.x >= btnX && mouse.x <= btnX + btnW && mouse.y >= btnY && mouse.y <= btnY + btnH);

        // Scroll with keys
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            scrollY = Math.max(0f, scrollY - 50f);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            scrollY = Math.min(maxScrollY, scrollY + 50f);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            close();
            return true;
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (hoverClose) {
                close();
                return true;
            }
            if (hoverTab0 && currentTab != 0) {
                currentTab = 0;
                scrollY = 0f;
                AudioManager.getInstance().playSFX("sfx_button");
                return true;
            }
            if (hoverTab1 && currentTab != 1) {
                currentTab = 1;
                scrollY = 0f;
                AudioManager.getInstance().playSFX("sfx_button");
                return true;
            }

            // Click outside dialog to close
            if (mouse.x < dlgX || mouse.x > dlgX + W || mouse.y < dlgY || mouse.y > dlgY + H) {
                close();
                return true;
            }

            return true; // Consume click within modal
        }

        return true; // Modal is blocking input to underlying elements
    }

    public void handleScroll(float amountY) {
        if (!visible) return;
        scrollY += amountY * 40f;
        if (scrollY < 0f) scrollY = 0f;
        if (scrollY > maxScrollY) scrollY = maxScrollY;
    }

    public void render(ShapeRenderer sr, SpriteBatch batch, BitmapFont font, BitmapFont titleFont, BitmapFont smallFont, float animTimer) {
        render(sr, batch, font, titleFont, smallFont, animTimer, null);
    }

    public void render(ShapeRenderer sr, SpriteBatch batch, BitmapFont font, BitmapFont titleFont, BitmapFont smallFont, float animTimer, com.badlogic.gdx.graphics.Camera camera) {
        if (!visible) return;

        // 1. DIM BACKDROP
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(0.01f, 0.02f, 0.05f, 0.88f);
        sr.rect(0, 0, 1280f, 720f);

        // 2. DIALOG WINDOW SHADOW & BODY
        // Soft drop shadow
        sr.setColor(0f, 0f, 0f, 0.55f);
        sr.rect(dlgX + 8f, dlgY - 8f, W, H);

        // Main Chassis
        sr.setColor(0.06f, 0.09f, 0.15f, 0.98f);
        sr.rect(dlgX, dlgY, W, H);

        // Header Background
        sr.setColor(0.09f, 0.14f, 0.23f, 1f);
        sr.rect(dlgX, dlgY + H - 65f, W, 65f);

        // Radiant Top Border Accent Line
        sr.setColor(1f, 0.82f, 0.25f, 0.95f);
        sr.rect(dlgX, dlgY + H - 3f, W, 3f);

        // Outer Neon Border
        sr.setColor(0.20f, 0.60f, 0.90f, 0.85f);
        drawBoxBorder(sr, dlgX, dlgY, W, H, 2f);

        // Divider under header
        sr.setColor(0.20f, 0.35f, 0.55f, 0.6f);
        sr.rect(dlgX, dlgY + H - 65f, W, 1.5f);

        // 3. TABS
        float tabW = (W - 50f) / 2f;
        float tabH = 42f;
        float tabY = dlgY + H - 110f;
        float tab0X = dlgX + 25f;
        float tab1X = tab0X + tabW;

        // Tab 0 Background
        if (currentTab == 0) {
            sr.setColor(0.14f, 0.25f, 0.40f, 1f);
        } else {
            sr.setColor(hoverTab0 ? new Color(0.10f, 0.16f, 0.26f, 1f) : new Color(0.07f, 0.11f, 0.18f, 1f));
        }
        sr.rect(tab0X, tabY, tabW, tabH);
        sr.setColor(currentTab == 0 ? new Color(0.35f, 0.85f, 1f, 1f) : new Color(0.20f, 0.30f, 0.45f, 0.7f));
        drawBoxBorder(sr, tab0X, tabY, tabW, tabH, currentTab == 0 ? 2f : 1f);

        // Tab 1 Background
        if (currentTab == 1) {
            sr.setColor(0.14f, 0.25f, 0.40f, 1f);
        } else {
            sr.setColor(hoverTab1 ? new Color(0.10f, 0.16f, 0.26f, 1f) : new Color(0.07f, 0.11f, 0.18f, 1f));
        }
        sr.rect(tab1X, tabY, tabW, tabH);
        sr.setColor(currentTab == 1 ? new Color(0.35f, 0.85f, 1f, 1f) : new Color(0.20f, 0.30f, 0.45f, 0.7f));
        drawBoxBorder(sr, tab1X, tabY, tabW, tabH, currentTab == 1 ? 2f : 1f);

        // 4. CONTENT CONTAINER
        float contX = dlgX + 25f;
        float contY = dlgY + 80f;
        float contW = W - 50f;
        float contH = H - 200f;

        sr.setColor(0.04f, 0.06f, 0.11f, 0.95f);
        sr.rect(contX, contY, contW, contH);
        sr.setColor(0.16f, 0.25f, 0.38f, 0.7f);
        drawBoxBorder(sr, contX, contY, contW, contH, 1.2f);

        // 5. CLOSE BUTTON
        float btnW = 200f;
        float btnH = 44f;
        float btnX = dlgX + (W - btnW) / 2f;
        float btnY = dlgY + 22f;

        sr.setColor(hoverClose ? new Color(0.22f, 0.65f, 0.40f, 1f) : new Color(0.12f, 0.45f, 0.28f, 0.95f));
        sr.rect(btnX, btnY, btnW, btnH);
        sr.setColor(hoverClose ? new Color(0.7f, 1f, 0.8f, 1f) : new Color(0.35f, 0.85f, 0.55f, 0.85f));
        drawBoxBorder(sr, btnX, btnY, btnW, btnH, 1.8f);

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // 6. RENDER TEXT & CONTENT
        batch.begin();

        // Header Title
        titleFont.getData().setScale(0.72f);
        titleFont.setColor(1f, 0.90f, 0.35f, 1f);
        layout.setText(titleFont, "✦ BẢN TIN CẬP NHẬT TRỰC TUYẾN ✦");
        titleFont.draw(batch, layout, dlgX + (W - layout.width) / 2f, dlgY + H - 18f);

        // Subtitle / Version tag
        font.getData().setScale(0.75f);
        font.setColor(0.70f, 0.85f, 1f, 0.9f);
        layout.setText(font, "Phiên bản " + CURRENT_VERSION + " - Hệ Thống Cài Đặt & Quản Lý Dữ Liệu");
        font.draw(batch, layout, dlgX + (W - layout.width) / 2f, dlgY + H - 43f);

        // Tab Labels
        font.getData().setScale(0.85f);
        // Tab 0 Label
        font.setColor(currentTab == 0 ? new Color(1f, 0.95f, 0.4f, 1f) : new Color(0.75f, 0.85f, 0.95f, 0.85f));
        layout.setText(font, "BẢN TIN CẬP NHẬT (" + CURRENT_VERSION + ")");
        font.draw(batch, layout, tab0X + (tabW - layout.width) / 2f, tabY + 28f);

        // Tab 1 Label
        font.setColor(currentTab == 1 ? new Color(1f, 0.95f, 0.4f, 1f) : new Color(0.75f, 0.85f, 0.95f, 0.85f));
        layout.setText(font, "KẾ HOẠCH & LỘ TRÌNH");
        font.draw(batch, layout, tab1X + (tabW - layout.width) / 2f, tabY + 28f);

        // Close Button Text
        font.getData().setScale(0.90f);
        font.setColor(hoverClose ? Color.WHITE : new Color(0.90f, 0.98f, 0.92f, 1f));
        layout.setText(font, "ĐÃ HIỂU / ĐÓNG");
        font.draw(batch, layout, btnX + (btnW - layout.width) / 2f, btnY + 29f);

        batch.end();

        // 7. RENDER SCROLLABLE CONTENT WITH BOUNDS CLIPPING
        renderScrollableContent(sr, batch, font, smallFont, contX, contY, contW, contH, camera);
    }

    private void renderScrollableContent(ShapeRenderer sr, SpriteBatch batch, BitmapFont font, BitmapFont smallFont, float cx, float cy, float cw, float ch, com.badlogic.gdx.graphics.Camera camera) {
        List<UpdateItem> items = (currentTab == 0) ? patchNotesItems : roadmapItems;

        com.badlogic.gdx.graphics.Camera cam = camera;
        if (cam == null) {
            if (game.getScreen() instanceof com.fishingrpg.game.PlayScreen) {
                cam = ((com.fishingrpg.game.PlayScreen) game.getScreen()).getCamera();
            } else if (game.getScreen() instanceof com.fishingrpg.game.screens.MainMenuScreen) {
                cam = ((com.fishingrpg.game.screens.MainMenuScreen) game.getScreen()).getCamera();
            }
        }

        boolean pushed = false;
        if (cam != null) {
            try {
                com.badlogic.gdx.math.Rectangle clipBounds = new com.badlogic.gdx.math.Rectangle(cx + 10f, cy + 10f, cw - 20f, ch - 20f);
                com.badlogic.gdx.math.Rectangle scissors = new com.badlogic.gdx.math.Rectangle();
                com.badlogic.gdx.scenes.scene2d.utils.ScissorStack.calculateScissors(cam, batch.getTransformMatrix(), clipBounds, scissors);
                pushed = com.badlogic.gdx.scenes.scene2d.utils.ScissorStack.pushScissors(scissors);
            } catch (Throwable ignored) {
                pushed = false;
            }
        }

        float paddingX = 30f;
        float startX = cx + paddingX;
        float currentY = cy + ch - 25f + scrollY;
        float totalHeight = 30f;

        float topLimit = cy + ch - 5f;
        float botLimit = cy + 10f;

        batch.begin();

        for (UpdateItem item : items) {
            if (item.isVersionHeader) {
                // Version Header Separator
                if (currentY <= topLimit + 20f && currentY >= botLimit - 15f) {
                    font.getData().setScale(0.92f);
                    font.setColor(1f, 0.88f, 0.35f, 1f);
                    layout.setText(font, "=== " + item.title + " ===");
                    font.draw(batch, layout, startX, currentY);

                    // Decorative underline
                    smallFont.getData().setScale(0.75f);
                    smallFont.setColor(0.35f, 0.55f, 0.75f, 0.7f);
                    smallFont.draw(batch, "-----------------------------------------------------------------------------------------------------------------", startX, currentY - 14f);
                }
                currentY -= 36f;
                totalHeight += 36f;
                continue;
            }

            // Badge & Title
            if (currentY <= topLimit + 20f && currentY >= botLimit - 5f) {
                smallFont.getData().setScale(0.85f);
                smallFont.setColor(item.badgeColor);
                smallFont.draw(batch, "[" + item.badge + "]", startX, currentY);

                font.getData().setScale(0.88f);
                font.setColor(1f, 0.95f, 0.75f, 1f);
                font.draw(batch, item.title, startX + 145f, currentY);
            }

            currentY -= 26f;
            totalHeight += 26f;

            // Bullet points
            font.getData().setScale(0.78f);
            font.setColor(0.85f, 0.92f, 0.98f, 0.92f);

            for (String pt : item.bulletPoints) {
                layout.setText(font, "- " + pt, Color.WHITE, cw - paddingX * 2f - 20f, Align.left, true);
                if (currentY <= topLimit && currentY - layout.height >= botLimit - 25f) {
                    font.draw(batch, layout, startX + 15f, currentY);
                }
                float itemH = layout.height + 12f;
                currentY -= itemH;
                totalHeight += itemH;
            }

            currentY -= 14f;
            totalHeight += 14f;
        }

        font.getData().setScale(1f);
        smallFont.getData().setScale(1f);
        batch.end();

        if (pushed) {
            com.badlogic.gdx.scenes.scene2d.utils.ScissorStack.popScissors();
        }

        maxScrollY = Math.max(0f, totalHeight - ch + 40f);

        // Render scrollbar on right
        if (maxScrollY > 0) {
            float trackH = ch - 20f;
            float trackY = cy + 10f;
            float trackX = cx + cw - 12f;
            float barH = Math.max(30f, trackH * (ch / totalHeight));
            float pct = maxScrollY > 0f ? (scrollY / maxScrollY) : 0f;
            float barY = trackY + (trackH - barH) * (1f - pct);

            Gdx.gl.glEnable(GL20.GL_BLEND);
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(0.12f, 0.16f, 0.24f, 0.6f);
            sr.rect(trackX, trackY, 6f, trackH);
            sr.setColor(0.35f, 0.65f, 0.95f, 0.8f);
            sr.rect(trackX, barY, 6f, barH);
            sr.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    private void drawBoxBorder(ShapeRenderer sr, float x, float y, float w, float h, float thickness) {
        sr.rect(x, y, w, thickness);
        sr.rect(x, y + h - thickness, w, thickness);
        sr.rect(x, y, thickness, h);
        sr.rect(x + w - thickness, y, thickness, h);
    }
}
