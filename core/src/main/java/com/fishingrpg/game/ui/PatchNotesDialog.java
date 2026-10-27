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

    public static final String CURRENT_VERSION = "v0.5.3.0";
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

        // ===== PHIÊN BẢN v0.5.3.0 (MỚI NHẤT) =====
        patchNotesItems.add(UpdateItem.versionHeader("PHIÊN BẢN v0.5.3.0 (MỚI NHẤT - HẠ TẦNG SỰ KIỆN HALLOWEEN 2026 & DYNAMIC UI AUTO-SWITCH)"));

        patchNotesItems.add(new UpdateItem("SỰ KIỆN", "Hạ Tầng Sự Kiện Kép Lạc Hồng & Halloween 2026 Tự Động Chuyển Giao (HLW-01)", new Color(1.0f, 0.50f, 0.10f, 1f))
            .addPoint("Bảo toàn 100% giao diện, nhiệm vụ, chuỗi điểm danh và cửa hàng Hào Khí Lạc Hồng cho đến hết 23:59:59 ngày 28/10/2026.")
            .addPoint("Thời gian chuyển giao (Intermission - 29/10): Tự động hiển thị đồng hồ đếm ngược mở Đêm Hội Halloween và thu hồi, quy đổi Điểm SK tồn dư sang Vàng an toàn (1 Điểm SK = 1,000 Vàng).")
            .addPoint("Tự động kích hoạt Halloween lúc 00:00:00 ngày 30/10: Giao diện chuyển mượt mà sang phong cách Dark Gothic Fantasy với hình mờ Jack-o'-Lantern rực lửa, đàn dơi lượn sóng và hạt ma trơi phát quang.")
            .addPoint("Hệ thống Cửa Hàng & Điểm Danh Halloween: Tự động chuyển vật phẩm sang Set Dạ Hội Bí Ngô 4★ (Cần Dạ Hội Bí Ngô, Dây Cước Tơ Nhện Quỷ, Lưỡi Kẹo Ma Quái) cùng 25 ngày quà điểm danh mới (tự động reset an toàn 1 lần duy nhất qua cờ lưu trữ).")
            .addPoint("12 Nhiệm vụ Sự Kiện Mùa Lễ Hội ma quái hằng ngày làm mới lúc 00:00.")
        );

        // ===== PHIÊN BẢN v0.5.2.4 =====
        patchNotesItems.add(UpdateItem.versionHeader("PHIÊN BẢN v0.5.2.4 (HOÀN THÀNH BƯỚC 2.3: 15 LOÀI CÁ ĐẢO XA & TUYỆT TÁC SIÊU BOSS CÁ CỜ XANH)"));

        patchNotesItems.add(new UpdateItem("ĐỒ HỌA", "Bước 2.3: 15 Loài Cá Vector Đảo Xa & Siêu Boss Cá Cờ Xanh Khổng Lồ (FSH-02C)", new Color(0.25f, 0.85f, 0.50f, 1f))
            .addPoint("Đại tu toàn diện đồ họa vector LibGDX ShapeRenderer đa lớp, ánh sáng biển sâu và hoạt ảnh cho toàn bộ 15 loài cá Đảo Xa hoàn toàn mới.")
            .addPoint("6 Loài Cá Thường: Cá Mú Đỏ (thân đỏ san hô, đốm sao xanh ngọc bích lấp lánh), Cá Hồng Chuối (hồng cam tươi tắn, dải sọc vàng chuối óng ả), Cá Dìa Biển Đảo (hoa văn cẩm thạch hoa đá, gai nhọn tẩm độc bảo vệ), Cá Nhồng Biển (mũi tên bạc xé nước, hàm răng nanh tam giác nhọn hoắt), Cá Bè Trang (chrome bạc sáng chói, hàng chấm đen đồng tiền), Cá Nục Heo (trán gồ vuông góc dốc đứng, lưng xanh ngọc bích mạn sườn vàng chanh).")
            .addPoint("4 Loài Cá Hiếm: Cá Bớp Khơi (ngư lôi nâu đen cuồn cuộn, 2 sọc trắng kem song song, 8 gai cứng), Cá Cam Biển (bắp thịt dày chắc, vệt sọc sẫm vắt chéo từ mõm qua mắt lên gáy), Cá Thu Ngàng (thân đao thanh mảnh, 26 sọc vằn lam sẫm hổ vằn, 8 vây phụ vàng chói), Cá Đuối Hoa Cương (đĩa cẩm thạch hoa cương mỹ thuật, đuôi roi phóng tia điện cyan crackling).")
            .addPoint("2 Loài Cá Quý: Cá Ngừ Vây Vàng (ngư lôi khổng lồ, vây lưỡi liềm vàng chanh vươn dài cong vút, xoáy nước áp lực cao), Cá Cờ Gòn (mũi kiếm rapier thanh mảnh, cánh buồm xanh coban phủ đốm đen lộng gió).")
            .addPoint("2 Loài Sử Thi: Cá Mú Nghệ Khơi (gã khổng lồ rạn đá ngầm bọc giáp cổ thạch sừng sững, đốm đen hoa mai, miệng vực thẳm nuốt chửng, bụi đá trầm tích), Cá Mập Rạn Đen (sát thủ khí động học, đỉnh chóp TẤT CẢ các vây đen tuyền viền trắng nổi bật, 5 khe mang sắc lẹm, mắt thính mùi máu).")
            .addPoint("1 Tuyệt Tác Siêu Boss Thế Giới: Cá Cờ Xanh Khổng Lồ (3,000 HP, 260 kg) với 3 tầng hào quang thần thoại (tím vực thẳm, xanh coban, cyan điện tử), Mũi kiếm thép xé biển có rãnh plasma điện tử và tia lửa điện lóe sáng ở chóp kiếm, Cánh buồm lam ngọc 10 tia phát quang, 7 dải sọc neon điện tử chớp nháy, mắt thần săn mồi rực lửa và bọt khí cavitation bão tố.")
        );

        // ===== PHIÊN BẢN v0.5.2.3 =====
        patchNotesItems.add(UpdateItem.versionHeader("PHIÊN BẢN v0.5.2.3 (HOÀN THÀNH BƯỚC 2.2: 15 LOÀI CÁ BÃI BIỂN & SIÊU BOSS BẠCH TUỘC KHỔNG LỒ)"));

        patchNotesItems.add(new UpdateItem("ĐỒ HỌA", "Bước 2.2: 15 Loài Cá Vector Bãi Biển & Siêu Boss Bạch Tuộc Khổng Lồ (FSH-02B)", new Color(0.25f, 0.85f, 0.50f, 1f))
            .addPoint("Chuẩn hóa đồ họa LibGDX ShapeRenderer đa lớp, specular bóng sáng đại dương và hoạt ảnh quẫy đuôi/xúc tu cho toàn bộ 15 loài cá Bãi Biển.")
            .addPoint("6 Loài Cá Thường: Cá Chỉ Vàng (thân thoi bạc, dải sọc vàng kim óng ả, đốm nắp mang), Cá Đục (thân vàng cát ngụy trang đáy biển nông), Cá Cơm (trong suốt pha lê chỉ bạc sống lưng), Cá Mòi (lưng xanh thép, 5 chấm đen mạn sườn), Cá Lẹp (mỏng dẹt lưỡi dao lướt sóng), Cá Bạc Má (mắt to thao thức, lưng xanh rằn ri sóng biển).")
            .addPoint("4 Loài Cá Hiếm: Cá Nục (thân tròn chắc giáp vảy đường bên, vây đuôi vàng), Cá Đối (đầu dẹt mõm tù, vảy xám bạc mắt to màng mỡ), Cá Trích (thân dẹp dài xanh biển, vảy bạc lấp lánh phản quang), Cá Chim Biển (dẹt đĩa tròn bạc ngọc trai kiêu sa).")
            .addPoint("2 Loài Cá Quý: Cá Mú (thân hoa đá honeycomb rạn san hô, miệng rộng gai vây nhọn), Cá Bóp (ngư lôi nâu đen, 2 sọc trắng bạc song song khỏe khoắn).")
            .addPoint("2 Loài Sử Thi: Cá Thu Hoàng Kim (hào quang bạo chúa xé biển, 8 đôi vây phụ rời, vây đuôi trăng khuyết vàng 24K), Cá Ngừ Đại Dương (bắp thịt khổng lồ xanh thẫm, vây phụ nhọn vàng phát quang).")
            .addPoint("1 Siêu Boss Thế Giới: Bạch Tuộc Khổng Lồ (2,200 HP) với xoáy mực vực thẳm và năng lượng cyan-tím đa tầng, 8 xúc tu gai nhọn quằn quại bọc giác mút đỏ thẫm, mắt quỷ tím đồng tử chữ nhật, mỏ quái vật sừng đen chitin tử thần.")
        );

        // ===== PHIÊN BẢN v0.5.2.2 =====
        patchNotesItems.add(UpdateItem.versionHeader("PHIÊN BẢN v0.5.2.2 (HOÀN THÀNH BƯỚC 2.1: 15 LOÀI CÁ AO LÀNG & TÁI CẤU TRÚC LỘ TRÌNH ĐỒ HỌA)"));

        patchNotesItems.add(new UpdateItem("ĐỒ HỌA", "Bước 2.1: 15 Loài Cá Vector Ao Làng & Siêu Boss Thủy Quái Sông Trà (FSH-02A)", new Color(0.25f, 0.85f, 0.50f, 1f))
            .addPoint("Chuẩn hóa đồ họa LibGDX ShapeRenderer đa lớp, tạo khối nổi khối mềm mại, specular bóng sáng và hoạt ảnh quẫy vây mềm mại (tail wagging) cho 15 loài cá Ao Làng.")
            .addPoint("4 Loài Cá Thường: Cá Rô Đồng (thân ô-liu ánh đồng, vây lưng 9 gai nhọn), Cá Diếc (lưng vồng, vảy bạc ngọc trai), Cá Mương (thân trong suốt lưỡi liềm), Cá Lóc Nhí (hoa văn rằn ri ngụy trang cỏ nước).")
            .addPoint("3 Loài Cá Hiếm: Cá Chép (vảy rồng hoàng kim, râu mềm rước lộc, đuôi đỏ son), Cá Mè (đầu to miệng hướng bọt nước, vảy xám bạc), Cá Trê (thân trơn nhẵn 4 cặp râu dài sắc bén).")
            .addPoint("3 Loài Cá Quý: Cá Trôi (vảy xanh lam viền trắng sữa), Cá Trắm Cỏ (vảy trám sẫm viền ánh lục), Cá Chim Trắng (thân dẹt đĩa tròn, vây bụng ửng cam).")
            .addPoint("4 Loài Sử Thi: Cá Lóc Bông (báo đốm săn mồi nước ngọt), Cá Trê Lai (vằn rằn ri hổ vằn, ngạnh độc thép), Cá Chép Vàng (phát quang lấp lánh thần tài), Cá Lăng Khổng Lồ (sát thủ đáy sâu râu xúc giác).")
            .addPoint("1 Siêu Boss Thế Giới: Thủy Quái Sông Trà (850 HP) với vảy rêu phù sa, mắt quỷ dung nham đỏ rực, vây lưng sừng nhọn uy dũng cuộn sóng.")
        );

        patchNotesItems.add(new UpdateItem("LỘ TRÌNH", "Tái Cấu Trúc Lộ Trình Đồ Họa 6 Giai Đoạn", new Color(0.35f, 0.80f, 1f, 1f))
            .addPoint("Tách Bước 2 thành chuỗi chuyên biệt 3 bản đồ cá: Bước 2.1 (Ao Làng - Hoàn thành v0.5.2.2), Bước 2.2 (Bãi Biển), Bước 2.3 (Đảo Xa).")
            .addPoint("Chuyển Môi Trường Nước & Khúc Xạ Ánh Sáng (ENV-02) xuống thành Giai đoạn 3 riêng biệt (v0.5.3).")
            .addPoint("Điều chỉnh các bước tiếp theo: Giai đoạn 4 (UI & Nút Bấm Xúc Giác v0.5.4), Giai đoạn 5 (VFX Chiêu Thức & Boss v0.5.5), Giai đoạn 6 (Hoạt Ảnh Vật Lý & Gacha CS:GO v0.5.6).")
        );

        // ===== PHIÊN BẢN v0.5.2.1 =====
        patchNotesItems.add(UpdateItem.versionHeader("PHIÊN BẢN v0.5.2.1 (PHÂN TRANG QUICK SLOTS, SỬA SONAR & BỘ SƯU TẬP ĐẢO XA)"));

        patchNotesItems.add(new UpdateItem("TÚI ĐỒ", "Hệ Thống Phân Trang Quick Slots (Ô Phím Tắt Nhanh)", new Color(0.2f, 0.85f, 1f, 1f))
            .addPoint("Khắc phục triệt để lỗi khi có trên 9 loại vật phẩm trong túi đồ không thể hiển thị, không dùng được hoặc tưởng bị mất.")
            .addPoint("Tích hợp phân trang mượt mà [◀] Trang X/Y [▶] trên tiêu đề khung Ô Nhanh; tự động co giãn theo số lượng vật phẩm sở hữu.")
            .addPoint("Đồng bộ hoàn hảo phím tắt 1-9: Khi chuyển trang, các phím 1-9 và click chuột sẽ tương tác trực tiếp với đúng các vật phẩm hiển thị trên trang hiện tại.")
        );

        patchNotesItems.add(new UpdateItem("VẬT PHẨM", "Sửa Lỗi Kính Nhìn Xuyên Nước (Sonar) & Tinh Chỉnh Mồi Thơm", new Color(1f, 0.85f, 0.2f, 1f))
            .addPoint("Sửa triệt để lỗi radar thấu thị vẫn quét và hiển thị thông tin cá khi số lượt thấu thị của Kính Nhìn Xuyên Nước đã về 0.")
            .addPoint("Bảo toàn lượt thấu thị Sonar: Số lượt thấu thị sẽ KHÔNG bị trừ nếu người chơi không kéo cá mà để cá chạy đi (CHẬM TAY!). Lượt chỉ bị trừ khi thực sự giằng co kéo cá hoặc bắt được cá.")
            .addPoint("Mồi Thơm tự động mất đi khi cá cắn câu mà người chơi không kéo kịp để cá cắn trộm mồi và chạy mất.")
        );

        patchNotesItems.add(new UpdateItem("SỔ TAY", "Làm Lại 5 Bộ Sưu Tập (Collections) Khớp Chuẩn 15 Loài Cá Đảo Xa", new Color(0.35f, 0.95f, 0.6f, 1f))
            .addPoint("Tái cấu trúc 100% 5 Bộ Sưu Tập Đảo Xa trong Sổ Tay Cần Thủ: Rạn San Hô Đảo Xa, Kình Ngư Nước Nông, Dòng Chảy Khơi Xa, Chiến Hạm Đại Dương, và Huyền Thoại Biển Sâu (Boss Cá Cờ Xanh Khổng Lồ).")
            .addPoint("Cập nhật đầy đủ chú giải, tập tính và truyền thuyết sinh thái học cho toàn bộ 15 loài cá mới tại Đảo Xa.")
            .addPoint("Xóa sạch toàn bộ dữ liệu, tên gọi và dấu vết của 15 loài cá cũ ở Đảo Xa khỏi toàn bộ hệ thống.")
        );

        patchNotesItems.add(new UpdateItem("LỘ TRÌNH", "Điều Chỉnh Thứ Tự Ưu Tiên Kế Hoạch Phát Triển Đồ Họa", new Color(1f, 0.45f, 0.75f, 1f))
            .addPoint("Đẩy Bước 3 (Ngoại hình 45 loài cá vector & Môi trường 3 bản đồ) lên vị trí Giai đoạn 2 (v0.5.2) để ưu tiên hoàn thiện thế giới thủy tộc sống động trước.")
            .addPoint("Chuyển Giai đoạn UI & Nút Bấm Xúc Giác sang Bước 3 (v0.5.3).")
        );

        // ===== PHIÊN BẢN v0.5.2.0 =====
        patchNotesItems.add(UpdateItem.versionHeader("PHIÊN BẢN v0.5.2.0 (ĐẠI TU ĐẢO XA, SIÊU BOSS CÁ CỜ XANH 3,000 HP & PERFECT RE-HOOK)"));

        patchNotesItems.add(new UpdateItem("ĐẢO XA", "Đại Tu Toàn Diện 15 Loài Cá Đảo Xa Thực Tế & Siêu Boss 3,000 HP", new Color(0.2f, 0.85f, 1f, 1f))
            .addPoint("Loại bỏ hoàn toàn các loài viễn tưởng (Cá Nhà Táng, Mực Khổng Lồ, Leviathan); thay bằng 15 loài cá biển khơi thực tế theo chuẩn Big Game Fishing.")
            .addPoint("6 Cá Thường (260 - 360 HP): Cá Mú Đỏ, Cá Hồng Chuối, Cá Dìa Biển Đảo, Cá Nhồng Biển, Cá Bè Trang, Cá Nục Heo.")
            .addPoint("4 Cá Hiếm (520 - 650 HP): Cá Bớp Khơi, Cá Cam Biển, Cá Thu Ngàng, Cá Đuối Hoa Cương.")
            .addPoint("2 Cá Quý (1,050 - 1,150 HP): Cá Ngừ Vây Vàng, Cá Cờ Gòn (Sailfish).")
            .addPoint("2 Cá Sử Thi (1,750 - 1,900 HP): Cá Mú Nghệ Khơi (Giant Grouper), Cá Mập Rạn Đen (Blacktip Reef Shark).")
            .addPoint("1 Siêu Trùm Đại Dương: Cá Cờ Xanh Khổng Lồ (Blue Marlin) sở hữu 3,000 HP, lực kéo 195.0, cân nặng 260 kg.")
        );

        patchNotesItems.add(new UpdateItem("CHIẾN ĐẤU", "Chuẩn Hóa Phân Tầng Kỹ Năng & Cơ Chế Phản Xạ Perfect Re-Hook", new Color(1f, 0.85f, 0.2f, 1f))
            .addPoint("Tuân thủ nghiêm ngặt công thức phân tầng kỹ năng: Common (0-1 passive nhẹ), Uncommon (1 passive), Rare (1 active ở 50% HP), Epic (1 passive + 1 active), Boss (1 passive + 2 active độc bản).")
            .addPoint("Cơ chế Phản Xạ 'Perfect Re-Hook': Khi cá nhảy lên không trung, hệ thống hiện chỉ báo '▼ TIẾP NƯỚC! [SPACE] ▼'. Nhấp Space đúng khoảnh khắc chạm nước sẽ kích hoạt 'PERFECT RE-HOOK!', gây x2 sát thương kéo và triệt tiêu toàn bộ lực giật. Nếu vẫn đè giữ Space khi cá chạm nước sẽ bị sốc lực +35% Tension và trừ thể lực!")
            .addPoint("14 Kỹ năng mới từ Epic trở xuống: Trơ lực kéo (25% tỷ lệ miễn nhiễm kéo Space), Răng nanh cắt dây (-10% Max Tension), Gai độc san hô (-1.5 Stamina/s), Đổi hướng đột ngột (dao động Tension ±10%), Nương hải lưu (+18% pull khi Tension > 55%), Thân đao xé gió (+35% pull speed), Gai đuôi phóng điện (-12 Stamina nếu giữ Space > 2.5s), Xoáy nước đại dương (chao đảo sin & giảm 40% xả dây), Vũ điệu cờ buồm (2 nhịp phóng lướt), Thân hộ cổ thạch (-15 sát thương phẳng), Vực đá nuốt chửng (+35% Tension & -8 Stamina/s), Thính mùi máu (+20% pull khi thể lực < 50%), Cắn xé điên cuồng (-25% Max Tension).")
        );

        patchNotesItems.add(new UpdateItem("BOSS TRÙM", "Bộ 3 Kỹ Năng Độc Bản Của Boss Cá Cờ Xanh Khổng Lồ (Blue Marlin)", new Color(1f, 0.35f, 0.35f, 1f))
            .addPoint("Passive Độc Bản 'Mũi kiếm xé biển': Miễn nhiễm trói/băng; đè giữ phím kéo > 1.5s làm Tension dâng nhanh thêm +20%/s; nhấp nhả <= 1.2s được thưởng +15% sát thương kéo.")
            .addPoint("Active 1 'Vũ điệu lướt sóng': Tần suất định kỳ 8.0s một lần, phóng vút lên không trung 0.9s chớp nhoáng thử thách phản xạ Re-Hook của cần thủ.")
            .addPoint("Active 2 'Cuồng phóng ngư kiếm' (Dưới 30% HP): Kích hoạt chế độ Soft Enrage; mỗi đợt bứt tốc tăng +10% lực kéo (tối đa +50%); mỗi 1 giây trôi qua tăng +1% tốc độ tụt thể lực của cần thủ (tự động reset sạch sẽ ngay khi kết thúc trận đấu).")
        );

        patchNotesItems.add(new UpdateItem("ĐỒ HỌA", "Vector Artwork Độc Quyền Cho Boss Cá Cờ Xanh Khổng Lồ", new Color(0.35f, 0.95f, 0.6f, 1f))
            .addPoint("Thiết kế vector nguyên bản: Thân hình ngư lôi bắp thịt cuồn cuộn màu xanh coban hoàng gia, vây lưng cánh buồm cao ngạo nghễ, hoa văn sọc phản quang Neon Cyan rực rỡ và mũi kiếm đao thép dài sắc lẹm xé toạc sóng gió đại dương.")
        );

        // ===== PHIÊN BẢN v0.5.1.2 =====
        patchNotesItems.add(UpdateItem.versionHeader("PHIÊN BẢN v0.5.1.2 (NERF CÁ PULLING, SỬA AUTO-UPDATER, GIỚI HẠN SÁCH CỬA HÀNG & CHỐNG LÃNG PHÍ BUFF)"));

        patchNotesItems.add(new UpdateItem("CÂN BẰNG", "Nerf Sức Kéo Của Cá Khi Buông Cần & Giảm Rung Màn Hình", new Color(1f, 0.55f, 0.25f, 1f))
            .addPoint("Khi cá đang vùng vẫy (PULLING): Nếu người chơi buông phím kéo (Space), sát thương / lực kéo nhận vào giảm từ 0.8x xuống 0.4x (giảm 50%), tạo cơ hội xả bớt lực căng dây an toàn.")
            .addPoint("Giảm độ rung màn hình (Screen Shake Trauma) khi Tension cao: Giảm tốc độ tích tụ rung từ 0.04/frame xuống 0.01/frame (chỉ kích hoạt khi Tension > 88% và khống chế tối đa 0.16), chống mỏi mắt và rung giật quá mức.")
            .addPoint("Chuẩn hóa thông báo đứt dây: Tinh giản bỏ cụm 'CĂNG DÂY TỐI ĐA!', chỉ hiển thị rõ ràng 'DÂY CƯỚC BỊ ĐỨT, CÁ ĐÃ CHẠY MẤT!' và chữ nổi 'ĐỨT DÂY CÂU!'.")
            .addPoint("Bổ sung chữ nổi khi cắn câu (!): Tự động hiển thị nổi 'BOSS!' nếu đớp mồi là cá Boss, và 'NEW!' nếu là loài cá lần đầu tiên câu được.")
            .addPoint("Sửa lỗi chữ nổi khi hụt cá: Khắc phục lỗi nhảy cả câu dài khi hết thời gian chờ giật cần, tinh giản gọn gàng thành chữ nổi 'CHẬM TAY!'.")
        );

        patchNotesItems.add(new UpdateItem("CỬA HÀNG", "Giới Hạn Mua Sách Kỹ Năng & Loại Bỏ Sách Đã Đạt Cấp Khỏi Sự Kiện Giảm Giá", new Color(0.25f, 0.85f, 0.50f, 1f))
            .addPoint("Số lượng sách kỹ năng tối đa có thể mua = Cấp tối đa - Cấp hiện tại - Số sách đang có trong túi đồ.")
            .addPoint("Những quyển sách đã mua đủ hoặc kỹ năng đã max cấp sẽ hiển thị 'ĐÃ BÁN HẾT' và tự động loại khỏi sự kiện giảm giá ngẫu nhiên của tiệm tạp hóa.")
        );

        patchNotesItems.add(new UpdateItem("VẬT PHẨM", "Nút BUFF Tích Hợp Trên Thanh Trạng Thái & Khóa Dùng Trùng Lặp", new Color(0.35f, 0.80f, 1f, 1f))
            .addPoint("Dời vị trí và chuyển đổi hiển thị: Thay thế nút Mồi Thơm cũ thành nút 'BUFF (N)' đặt thoáng đãng tại x=438px trên thanh trạng thái.")
            .addPoint("Tích hợp Tooltip Hover: Rê chuột vào nút BUFF sẽ hiển thị bảng danh sách đầy đủ tất cả các buff vật phẩm đang có hiệu lực (Mồi Thơm, Đá Mài, Bùa Vàng, Sách, Nam Châm, Sonar, Lưới...).")
            .addPoint("Khóa tính năng sử dụng lặp lại các vật phẩm đang còn hiệu lực nhiều lượt, tránh lãng phí vật phẩm của cần thủ.")
            .addPoint("Đổi tên vật phẩm: 'Trà Thảo Mộc Định Thần' được tinh gọn thành 'Trà Thảo Mộc' (vẫn duy trì tương thích toàn diện với dữ liệu cũ).")
        );

        patchNotesItems.add(new UpdateItem("GIAO DIỆN", "Sửa Lỗi Auto-Updater & Cân Đối Bố Cục Thẻ Quay Gacha", fixBadgeColor)
            .addPoint("Sửa lỗi cửa sổ Trình Cập Nhật Tự Động (Auto-Updater) bật lên bị tắt ngay lập tức do nuốt sự kiện click từ giao diện chính.")
            .addPoint("Thiết kế lại bố cục cửa sổ cập nhật: Bỏ chữ '(AUTO-UPDATER)' trên tiêu đề tránh tràn viền và cân đối khoảng cách đều đặn cho 3 nút chức năng.")
            .addPoint("Mở rộng thẻ hiển thị kết quả quay Gacha đơn lẻ: Giãn cách hợp lý tên trang bị và các dòng chỉ số khỏi vòng tròn icon, loại bỏ hoàn toàn tình trạng đè chữ.")
            .addPoint("Sửa lỗi thanh tiến trình bản đồ (MapScreen): Căn chỉnh lại tiến độ câu cá hiển thị đúng trên từng thẻ bản đồ riêng biệt.")
            .addPoint("Cân chỉnh độ khó & mô tả bản đồ: Ao Làng (0 sao - Rất Dễ), Bãi Biển (1 sao - Dễ), Đảo Xa (2 sao - Vừa, vùng biển khơi thực tế).")
        );

        // ===== PHIÊN BẢN v0.5.1.1 =====
        patchNotesItems.add(UpdateItem.versionHeader("PHIÊN BẢN v0.5.1.1 (CÂN BẰNG TENSION IDLE, ĐẠI TU 54 ICON & QUICKSLOT)"));

        patchNotesItems.add(new UpdateItem("GIAO DIỆN", "Phóng To Kích Thước Ô Phím Tắt Nhanh (Quickslot Action Bar)", new Color(0.35f, 0.85f, 1f, 1f))
            .addPoint("Tăng kích thước ô Quickslot từ 58px lên 68px (+17% kích thước), mở rộng khung viền 236x264px tạo cảm giác bấm chuột rộng rãi và thoải mái.")
            .addPoint("Tăng tỷ lệ vẽ icon vector trong ô từ scale 0.60x lên 0.70x, giúp toàn bộ vật phẩm tiêu hao và đạo cụ hiển thị to rõ, sắc nét vượt trội.")
            .addPoint("Tự động co giãn đồng bộ nhãn phím tắt [1]-[9], huy hiệu số lượng xN và thẻ nâng cấp sách kỹ năng HỌC/UP/+SP ở 4 góc ô.")
        );

        patchNotesItems.add(new UpdateItem("ĐỒ HỌA", "Bước 1.5: 12 Vector Icon Mồi Câu, Sách Kỹ Năng & Loot Đáy Biển", new Color(1f, 0.85f, 0.25f, 1f))
            .addPoint("Chuẩn hóa toàn diện 12 file vector hình học phân tầng 4 lớp (100/100 điểm audit Quality Gate):")
            .addPoint("4 Chiến Lợi Phẩm Rác: Ủng Cao Su Cũ (miếng vá khâu chỉ & giọt nước nhỏ), Chai Nhựa (nhãn dán bóc dở & nước đục bọt khí), Lốp Xe (rãnh khía cao su gai & rong rêu bám), Túi Rác (túi nilon thắt tai & dây đỏ rút).")
            .addPoint("4 Cổ Vật Kho Báu: Bình Cổ (gốm men ngọc quai rồng & đai vàng đính ruby), Nhẫn Vàng (nhẫn vàng khối đính hồng ngọc cushion-cut lấp lánh sao), Rương Chìm (rương gỗ sồi đai sắt tràn vàng ròng & ngọc sapphire), Kiếm Cổ (lưỡi thép khắc phù chú & chuôi rồng ngọc bích tua lụa đỏ).")
            .addPoint("2 Mồi Câu Chuyên Dụng: Mồi Thơm (hũ thủy tinh mồi xanh lá mật ngọt & thẻ gỗ sao), Mồi Rủi Ro (hũ pha lê tím đậm mồi độc đỏ neon & nhãn đầu lâu tử thần).")
            .addPoint("2 Sách Kỹ Năng: Sách Kỹ Năng Chủ Động (bìa da rồng đỏ viền góc vàng & phù hiệu kiếm chiến trận), Sách Kỹ Năng Bị Động / Sách Thông Thái (bìa nhung xanh thẳm viền bạc & mắt toàn tri sao trời).")
            .addPoint("Chính thức hoàn thành 100% Giai đoạn 1 Kế hoạch Phát Triển Đồ Họa: Toàn bộ 54 icon trang bị và vật phẩm trong game đã được lột xác thành vector nguyên bản rực rỡ.")
        );

        patchNotesItems.add(new UpdateItem("CÂN BẰNG", "Nerf Lực Cản Cá Trong Trạng Thái Mệt (IDLE State Tension Nerf)", new Color(1f, 0.55f, 0.25f, 1f))
            .addPoint("Giảm mạnh hệ số lực cản của cá khi mệt (IDLE) lúc giữ phím kéo dây từ 0.15x xuống 0.03x (giảm 80% lực cản), giúp cần thủ dồn sát thương hiệu quả.")
            .addPoint("Triệt tiêu hoàn toàn lực kéo của cá khi mệt lúc buông dây (từ 0.10x về 0.0x), cho phép dây xả tự nhiên với tốc độ tối đa không bị cá ghì giữ.")
            .addPoint("Hạ nhẹ tốc độ gia tăng Tension cơ sở khi quay máy câu kéo cá mệt (từ 14 + 0.025*Max xuống 10 + 0.020*Max), mang lại nhịp chiến đấu cân bằng và mượt mà hơn.")
        );

        // ===== PHIÊN BẢN v0.4.24 =====
        patchNotesItems.add(UpdateItem.versionHeader("PHIÊN BẢN v0.4.24 (BƯỚC 1.4 TIÊU HAO, MÃ HÓA SAVE & TÁI CÂN BẰNG TENSION)"));

        patchNotesItems.add(new UpdateItem("CÂN BẰNG", "Tái Cân Bằng Lực Căng Dây (Tension) & Tăng Cường Trải Nghiệm Giằng Co", new Color(1f, 0.45f, 0.2f, 1f))
            .addPoint("Giảm mạnh tốc độ giảm Tension tự nhiên khi buông dây (từ 20f + 0.10f*Max xuống còn 6f + 0.04f*Max), loại bỏ hoàn toàn hiện tượng kim Tension tụt dốc không phanh.")
            .addPoint("Buff cá khi vùng vẫy (PULLING): Nếu người chơi buông dây, cá vẫn duy trì kéo với 80% lực (thay vì 40% như trước); khi kéo dây vẫn giữ nguyên 150% lực.")
            .addPoint("Nerf cá khi kiệt sức (IDLE): Điều chỉnh sức cản bơi ngầm xuống 15% khi kéo và 10% khi buông, nhịp nghỉ mở rộng lên 1.5s - 3.5s giúp cần thủ có thêm khoảng thở dồn sát thương.")
            .addPoint("Đồng bộ toàn bộ công thức và kiểm chứng qua công cụ mô phỏng Monte Carlo (combat simulator) đạt chuẩn chỉ số TTK và tỷ lệ giằng co theo quy chuẩn dự án.")
        );

        patchNotesItems.add(new UpdateItem("ĐỒ HỌA", "Bước 1.4: 12 Vector Icon Vật Phẩm Tiêu Hao & Đạo Cụ Tạp Hóa", new Color(0.2f, 0.95f, 0.55f, 1f))
            .addPoint("Chuẩn hóa toàn diện 12 file vector phân tầng 4 lớp (100/100 điểm kiểm định Quality Gate) cho toàn bộ đạo cụ & tiêu hao tiệm tạp hóa:")
            .addPoint("Nhóm Tiêu Hao & Hồi Phục: Bánh Mì (khía nướng bơ rắc bột), Thuốc Hồi Stamina (bình cầu ngọc bích sủi bọt khí nắp bần), Nước Tăng Lực (lon nhôm cyber tia sét & giọt sương lạnh), Nước Bứt Phá (bình tam giác giả kim tâm xoáy plasma), Trà Thảo Mộc (tách gốm ngọc đĩa sứ lá trà nổi & khói thơm).")
            .addPoint("Nhóm Đạo Cụ Chiến Đấu: Dây Câu Tốt (cuộn cước Kevlar gia cường lõi titan), Bình Xịt Đóng Băng (bình khí nén hoa tuyết Cryo khói lạnh), Đá Mài Lưỡi Câu (khối đá mài 2 tầng vát góc phát tia lửa), Nam Châm Kho Báu (nam châm chữ U hút tiền vàng & sóng từ), Kính Nhìn Xuyên Nước (kính lặn radar sonar lưới quét & đốm cá), Lưới Đánh Cá (lưới nón mắt cáo chì chìm & khuyên đồng), Bùa Hoàng Kim (thẻ bùa giấy cổ chu sa đỏ & hạt ngọc bích).")
            .addPoint("Khắc phục triệt để lỗi biến dạng hình học khi transpile (sửa lỗi hoán vị tọa độ đỉnh y1/y3 và ràng buộc hào quang nền).")
            .addPoint("Tích hợp hoàn toàn 12 hàm vẽ độc lập vào LootVisualRenderer và hiển thị sắc nét tuyệt đối trên ô túi đồ & cửa hàng.")
        );

        patchNotesItems.add(new UpdateItem("BẢO MẬT", "Mã Hóa File Save Quân Sự (AES-256) & Chống Can Thiệp Băm SHA-256", new Color(0.95f, 0.35f, 0.35f, 1f))
            .addPoint("Mã hóa toàn diện chuỗi dữ liệu xuất file save bằng thuật toán AES-256 kết hợp mã kiểm tra băm SHA-256.")
            .addPoint("Khóa triệt để việc mở file bằng Notepad/Text Editor để sửa Vàng, Kim Cương, Cấp độ hay Trang bị; phát hiện can thiệp sẽ lập tức từ chối nạp.")
            .addPoint("Đổi định dạng file xuất mặc định sang chuẩn .sav (angler_save_*.sav), hỗ trợ sao chép chuỗi mã hóa vào Clipboard an toàn.")
            .addPoint("Duy trì khả năng tương thích ngược hoàn hảo với các file .json hoặc bản sao lưu trước đây của người chơi.")
        );

        patchNotesItems.add(new UpdateItem("SỰ KIỆN", "Điều Chỉnh Thời Hạn Sự Kiện Lạc Hồng Đến Hết 28/10", new Color(1f, 0.75f, 0.25f, 1f))
            .addPoint("Ấn định thời điểm bế mạc Sự kiện Lạc Hồng vào đúng 23:59:59 ngày 28/10/2026 theo giờ chuẩn hệ thống.")
            .addPoint("Đồng hồ đếm ngược hiển thị chính xác thời gian còn lại trước khi sự kiện chính thức khép lại.")
        );

        patchNotesItems.add(new UpdateItem("CHUYỂN GIAO", "Cơ Chế Khóa An Toàn Cửa Hàng & Quy Đổi Điểm SK Tồn Đọng", new Color(0.35f, 0.85f, 0.5f, 1f))
            .addPoint("Khi sự kiện kết thúc: Cửa Hàng Sự Kiện tự động khóa nút đổi vật phẩm ('ĐÃ ĐÓNG (HẾT HẠN)') để bảo vệ tính công bằng.")
            .addPoint("Tự động bảo lưu giá trị nỗ lực của cần thủ: Toàn bộ Điểm Sự Kiện tồn đọng được quy đổi thành Vàng (1 Điểm SK = 1,000 Vàng).")
            .addPoint("Sẵn sàng hạ tầng dữ liệu và chuẩn bị khởi tranh Sự Kiện Halloween Đêm Hội Ma Quái vào ngày 30/10.")
        );

        patchNotesItems.add(new UpdateItem("GACHA", "Đính Chính Hoạt Ảnh Băng Chuyền Gacha Kiểu CS:GO", new Color(0.95f, 0.45f, 0.75f, 1f))
            .addPoint("Đính chính tài liệu & Roadmap: Hoạt ảnh quay Gacha là cơ chế băng chuyền con lăn ngang mở hòm CS:GO (Horizontal Roller Reel).")
            .addPoint("Loại bỏ hoàn toàn các mô tả sai lệch trước đây về 'bánh xe 12 cung' hay 'lật thẻ' (game dùng băng chuyền mở hòm, không có cơ chế lật thẻ).")
        );

        patchNotesItems.add(new UpdateItem("KỸ NĂNG", "Đại Tu Bố Cục Thẻ Kỹ Năng & Đồng Nhất Phông Chữ", fixBadgeColor)
            .addPoint("Bỏ dấu chấm ở đầu dòng hồi chiêu ('Thời gian hồi chiêu: Xs'), đặt thẳng hàng dưới nhãn phân loại.")
            .addPoint("Đồng nhất chuẩn phông chữ sắc nét cho toàn bộ mô tả, nhãn cấp độ và chỉ số hiệu ứng kỹ năng.")
            .addPoint("Phân bổ khoảng cách các phần thoáng đãng, neo nút Mở Khóa / Nâng Cấp ở đáy bảng; triệt tiêu dính chữ và khoảng trống thừa.")
        );

        patchNotesItems.add(new UpdateItem("ĐỒ HỌA", "Bước 1.1: 12 Vector Icon 4 Set Trang Bị Cơ Bản 0★-3★", newBadgeColor)
            .addPoint("Chuẩn hóa toàn diện 12 file SVG gốc và mã nguồn LibGDX cho 4 Set trang bị: Tập Sự, Thợ Săn, Băng Giá, Đại Dương.")
            .addPoint("Tuân thủ nghiêm ngặt quy chuẩn mỹ thuật Cyber-Fantasy & Deep Ocean 4 lớp, 100% đạt điểm kiểm duyệt tối đa 100/100 Quality Gate.")
            .addPoint("Tích hợp hiệu ứng phản quang Specular Glint sắc nét, tối ưu hiệu năng đồ họa mượt mà 60 FPS.")
        );

        patchNotesItems.add(new UpdateItem("ĐỒ HỌA", "Đại Tu Hiển Thị Đầy Đủ (Anti-Squish) & Thiết Kế Lại 3 Móc + Cần Băng", new Color(0.35f, 0.85f, 1f, 1f))
            .addPoint("Thiết kế lại 3 Móc câu biệt lập phom dáng: Móc Thợ Săn (Móng vuốt dã thú), Móc Băng Giá (Mỏ neo pha lê đối xứng), Móc Đại Dương (Phi lao nanh thủy quái đuôi cá voi).")
            .addPoint("Thiết kế lại Cần Băng Giá: Gọt sạch toàn bộ gai nhọn thành trượng băng tuyết thanh thoát phẳng mịn.")
            .addPoint("Đồng nhất bản hiển thị ĐẦY ĐỦ (Full Representation) tại Gacha (Vòng quay cuộn reel r=44f, Loot Table r=34f ô đứng 216x142, Bệ trưng bày r=38f) và Equipment.")
            .addPoint("Áp dụng Dynamic Stroke Scaling tỉ lệ theo r, chấm dứt hoàn toàn hiện tượng icon bị bóp nghẹt hay bết dính nét.")
        );

        patchNotesItems.add(new UpdateItem("ĐỒ HỌA", "Bước 1.2: 9 Vector Icon 3 Set Cao Cấp & Huyền Thoại 4★-6★", new Color(0.85f, 0.45f, 1f, 1f))
            .addPoint("Chuẩn hóa toàn diện 9 file vector hình học 4 lớp (100/100 điểm kiểm định) cho 3 Set: Hải Tặc (4★ Epic), Vực Thẳm (5★ Legendary), Hoàng Gia (6★ Legendary).")
            .addPoint("Set Hải Tặc: Cột buồm bọc đồng & đèn bão ma quái, Bánh lái thuyền & neo cổ đồng Aztec mắt lục bảo, Lưỡi hái Damascus rãnh máu & đồng 8-Reales.")
            .addPoint("Set Vực Thẳm: Trượng Obsidian gai góc & mắt quỷ toàn tri Nether Eye, Khung cuộn bát giác 8 sừng quỷ tơ hắc mạch, Móng vuốt rồng vực sâu ngạnh ngược.")
            .addPoint("Set Hoàng Gia: Quyền trượng bạch kim mạ vàng 24k hoa bách hợp đính hồng ngọc Ruby, Bánh xe tơ tằm 8 cánh cung đình khảm Sapphire, Lưỡi câu cẩm thạch trắng viền vàng ngọc trai giọt nước.")
            .addPoint("Transpile hoàn toàn 9 hàm vẽ sang hệ thống hình học đa giác đặc (sx, sy, fillTri, fillQuad, drawLine, fillCircle) đồng nhất 100% giữa SVG và in-game.")
        );

        patchNotesItems.add(new UpdateItem("ĐỒ HỌA", "Đột Phá Thiết Kế Độc Bản & Bùng Nổ Hiệu Ứng Thần Khí Lạc Hồng (Premium)", new Color(1f, 0.45f, 0.75f, 1f))
            .addPoint("Nguyên Lý Độc Bản (Strict Non-Repetition Rule): Cấm tuyệt đối tái sử dụng phom dáng hoặc khung mẫu cũ; mỗi trang bị sở hữu hình bóng (silhouette) và ngôn ngữ tạo hình hoàn toàn riêng biệt.")
            .addPoint("Cần Quang Minh vs Cần Lạc Hồng: Cần Quang Minh chuyển sang trục đứng chữ thập (Upright Cruciform) với vương miện Sol Halo lơ lửng và cánh Seraph xòe rộng; Cần Lạc Hồng nghiêng chéo bùng nổ với đầu trượng Trống Đồng Đông Sơn, Chim Lạc ngậm hồng ngọc và thanh long ngọc bích quấn thân.")
            .addPoint("Dây Cước 3 Bộ Khác Biệt Triệt Để: Dây Quang Minh dáng quả trám khung dệt thiên sứ ôm khối cầu Sunstone; Dây Vệ Quốc ổ quay bát giác 8 mũi giáo chiến trận, khiên sao vàng và cờ lệnh bay; Dây Lạc Hồng bùng nổ Song Long Ouroboros ngọc lục bảo ôm viên Ngọc Rồng Huyết Ngọc Ruby.")
            .addPoint("Lưỡi Câu 3 Bộ Độc Bản: Lưỡi Quang Minh mỏ neo chữ thập đối xứng thánh thiện 2 ngạnh mũi tên kim cương; Lưỡi Vệ Quốc móng vuốt rồng Trảo Long Khởi Nghĩa sống lưng gai giáp, mắt khuyên sao vàng và dải lụa chiến trận; Lưỡi Lạc Hồng đế Đóa Sen Hồng Âu Cơ tỏa sáng, ngạnh mỏ Chim Lạc mắt hồng ngọc và ngọc Tiên Châu tiên cảnh.")
            .addPoint("Cập nhật quy chuẩn vector-graphics-designer SKILL.md với điều khoản cấm trùng lặp phom dáng và tiêu chuẩn bùng nổ cho dòng đồ Premium Limited.")
        );

        patchNotesItems.add(new UpdateItem("ĐỒ HỌA", "Bước 1.3: 9 Vector Icon Set Thần Thoại & Giới Hạn 6★-7★", new Color(1f, 0.85f, 0.25f, 1f))
            .addPoint("Chuẩn hóa trọn vẹn 9 file vector 4 lớp (100/100 điểm audit) cho 3 Set đỉnh cao: Set Quang Minh (7★ Mythic), Set Vệ Quốc Vang Danh (Limited Event), Set Thần Khí Lạc Hồng (6★ Limited Gacha).")
            .addPoint("Set Quang Minh: Trượng Thiên Sứ Seraph bạch kim mạ vàng 24K nạm 3 đai ngọc Sunstone, cuộn tơ kim tuyến bánh xe mặt trời 12 răng cưa, Lưỡi câu thánh quang hào quang rực rỡ và 3 lớp cánh thiên thần.")
            .addPoint("Set Vệ Quốc Vang Danh: Cần khởi nghĩa trúc son ngọc bích nạm 3 đai đồng khắc sao vàng, cờ lệnh ngũ sắc bay trong gió, cước bện sợi tơ đỏ huyết chiến và lưỡi câu móng rồng chiến tích oai hùng.")
            .addPoint("Set Thần Khí Lạc Hồng: Thần trượng Đông Sơn khảm ngọc lục bảo & trống đồng 14 tia sáng rực lửa với cánh Chim Lạc sải cánh, cước Song Long Chầu Nguyệt ôm viên ngọc rồng Lạc Long Quân, Lưỡi câu tiên mẫu đóa sen Âu Cơ nạm ngọc Tiên Châu.")
            .addPoint("Transpile hoàn toàn 9 hàm vẽ sang hình học đa giác đặc (fillTri, fillQuad, drawLine, fillCircle) chuẩn xác 100% SVG, hiển thị lộng lẫy và sắc nét tuyệt đối trên mọi bệ hiển thị.")
        );

        patchNotesItems.add(new UpdateItem("HÌNH HỌC", "Chuyển Thể Đồ Họa Vector Hình Học Nguyên Bản SVG Sang LibGDX", fixBadgeColor)
            .addPoint("Khắc phục hiện tượng 'vẽ nét gậy que' trong game: Sử dụng thuật toán tam giác hóa đa giác đặc (ShapeRenderer.ShapeType.Filled) bám sát 100% tọa độ file SVG gốc.")
            .addPoint("Móc Băng Giá: Tái hiện đầy đủ lỗ xỏ lục giác, thân trụ 2 lớp ngọc bích, đôi cánh mỏ neo cong dày pha lê, sao tuyết 5 cánh đặc tâm và chóp mỏ neo đáy sắc sảo.")
            .addPoint("Sửa triệt để lỗi chữ nhãn 'Cần Câu', 'Dây Cước', 'Lưỡi Câu' đè ngang mép bệ đỡ gacha và xóa bỏ viền vuông xanh trên trục xoay tròn.")
        );

        // ===== PHIÊN BẢN v0.4.23 =====
        patchNotesItems.add(UpdateItem.versionHeader("PHIÊN BẢN v0.4.23 (TIỀN TỆ SỔ TAY, HOÁN ĐỔI PHÍM & CƠ CHẾ KÉO CÁ)"));

        patchNotesItems.add(new UpdateItem("SỔ TAY", "Mô Tả Chi Tiết Cơ Chế 20 Kỹ Năng Của Cá Trong Sổ Tay", new Color(0.4f, 0.85f, 1f, 1f))
            .addPoint("Bổ sung chú thích đầy đủ hiệu ứng và cơ chế tác động cho toàn bộ 20 kỹ năng chủ động & nội tại của cá.")
            .addPoint("Khung mô tả thẻ cá trong Sổ Tay tự động co giãn chiều cao linh hoạt, hiển thị rõ ràng từng kỹ năng.")
        );

        patchNotesItems.add(new UpdateItem("NHIỆM VỤ", "Đại Tu Kho Nhiệm Vụ Hằng Ngày & Hằng Tuần Đa Dạng", new Color(0.85f, 0.5f, 1f, 1f))
            .addPoint("Triệt tiêu hoàn toàn trùng lặp yêu cầu; mở rộng 12 danh mục nhiệm vụ Ngày và 7 nhiệm vụ Tuần phong phú.")
            .addPoint("Bổ sung các mục tiêu mới: Khiêu chiến Boss, bắt Kình ngư nặng ký, kỹ thuật câu giữ dây an toàn, dùng vật phẩm, dọn rác, viễn du.")
        );

        patchNotesItems.add(new UpdateItem("GIAO DIỆN", "Giãn Cách Dock Tiền Tệ Sổ Tay & Chuẩn Hóa Icon Vàng", fixBadgeColor)
            .addPoint("Tăng chiều cao khung dock tiền tệ trong Sổ Tay lên 54px, phân cách 2 hàng Vàng và Kim Cương 24px thoáng đãng.")
            .addPoint("Đồng nhất mỹ thuật Icon Đồng Vàng sang dạng đĩa tròn vàng bóng, viền khắc nổi và phản quang lấp lánh (bỏ lỗ vuông).")
        );

        patchNotesItems.add(new UpdateItem("KỸ NĂNG", "Khắc Phục Lỗi Font Ô Vuông & Căn Chỉnh Vị Trí Cooldown", new Color(0.4f, 0.85f, 1f, 1f))
            .addPoint("Loại bỏ emoji đồng hồ gây lỗi ô vuông [] trên máy không có glyph, thay bằng ký tự chấm đầu dòng • thanh lịch.")
            .addPoint("Cố định dòng Thời gian hồi chiêu và nhắc nhở Sách kỹ năng an toàn bên trên nút Nâng Cấp, không bị che khuất.")
        );

        patchNotesItems.add(new UpdateItem("HỆ THỐNG", "Tự Động Hoán Đổi Phím (Auto-Swap) & Hỗ Trợ Bộ Gõ Unikey", dataBadgeColor)
            .addPoint("Khi đổi phím trùng với hành động khác: Hệ thống tự động hoán đổi phím giữa 2 hành động thay vì chặn thao tác.")
            .addPoint("Bổ sung quét phím phần cứng dự phòng trong Cài Đặt, bảo đảm nhận diện phím 100% khi dùng bộ gõ Unikey/EVKey.")
            .addPoint("Tự động khôi phục phím mặc định nếu tệp lưu trữ phím bị lỗi hoặc bằng 0; hỗ trợ kích hoạt chiêu bằng click HUD.")
        );

        patchNotesItems.add(new UpdateItem("CÂN BẰNG", "Lực Căng Dây Khi Kéo Cá & Giới Hạn Sàn Kháng Lực Kéo", newBadgeColor)
            .addPoint("Kéo dây (giữ SPACE) luôn sinh lực căng cơ bản theo nhịp quay cần kể cả khi cá IDLE (14f + 0.025f*MaxTension).")
            .addPoint("Khi cá giằng co (PULLING), kéo dây làm tăng vọt lực căng (26f + 0.055f*MaxTension), tạo phản hồi giằng co chân thực.")
            .addPoint("Khóa sàn kháng lực kéo ở mức tối thiểu 25%, ngăn chặn trang bị cấp cao triệt tiêu toàn bộ lực kéo của cá.")
        );

        // ===== PHIÊN BẢN v0.4.22 =====
        patchNotesItems.add(UpdateItem.versionHeader("PHIÊN BẢN v0.4.22 (SỬA LỖI KỸ NĂNG & CÂN BẰNG THÀNH TỰU)"));

        patchNotesItems.add(new UpdateItem("SỬA LỖI", "Khắc Phục Lỗi Kỹ Năng Dưỡng Sức & Trói Buộc", fixBadgeColor)
            .addPoint("Dưỡng Sức: Khóa triệt để việc bấm/giữ SPACE gây sát thương lên cá trong lúc đang vận sức; không làm sẩy cá do chùng dây.")
            .addPoint("Trói Buộc: Khi cá chịu hiệu ứng Trói Buộc (loi_nguyen_troi_buoc), kéo dây hoàn toàn không làm tăng lực căng Tension.")
        );

        patchNotesItems.add(new UpdateItem("CÂN BẰNG", "Điều Chỉnh Phần Thưởng Kim Cương 48 Mốc Thành Tựu", dataBadgeColor)
            .addPoint("Hạ định mức thưởng Kim Cương về chuẩn cân bằng kinh tế (2 - 30 KC thay vì 400 - 600 KC) trên toàn bộ 8 nhóm thành tựu.")
            .addPoint("Sửa mốc chỉ tiêu 'Sát Thủ Biển Sâu' từ 500 thành chuẩn 5.000 con cá.")
            .addPoint("Hệ thống SaveManager tự động đồng bộ mô tả và phần thưởng mới cho cả các bản lưu game cũ.")
        );

        patchNotesItems.add(new UpdateItem("SỰ KIỆN", "Hợp Nhất Thưởng Nhiệm Vụ Sự Kiện Vào Rương Mốc", newBadgeColor)
            .addPoint("Loại bỏ phần thưởng đơn lẻ và nút nhận riêng của 6 nhiệm vụ sự kiện hàng ngày.")
            .addPoint("Thẻ nhiệm vụ chuyển thành dạng huy hiệu trạng thái ('✓ ĐÃ XONG' / 'ĐANG LÀM'), thưởng quy về Rương Mốc 4/6 nhiệm vụ.")
        );

        patchNotesItems.add(new UpdateItem("KỸ NĂNG", "Chú Thích Động Theo Cấp & Đưa Cooldown Xuống Dưới", new Color(0.4f, 0.85f, 1f, 1f))
            .addPoint("Viết lại toàn bộ chú thích kỹ năng: Chỉ số hiển thị động chính xác theo cấp độ hiện tại thay vì ghi chú số cố định.")
            .addPoint("Thông tin Thời gian hồi chiêu (Cooldown) và Tiêu hao thể lực được chuyển xuống phía dưới cùng của hộp mô tả.")
        );

        patchNotesItems.add(new UpdateItem("GIAO DIỆN", "Icon Vector Sổ Tay & Tinh Chỉnh Văn Bản Trang Bị", new Color(1f, 0.85f, 0.35f, 1f))
            .addPoint("Sổ Tay (Catalog): Thay thế chữ 'Vàng:' và 'KC:' trên thanh số dư bằng Icon Vector Đồng Vàng và Kim Cương sắc nét.")
            .addPoint("Trang bị: Tinh giản 'Độ bền dây (Max Tension)' thành 'Độ bền dây' và 'Tỉ lệ cá hiếm / xịn' thành 'Tỉ lệ cá hiếm'.")
        );

        // ===== PHIÊN BẢN v0.4.21 =====
        patchNotesItems.add(UpdateItem.versionHeader("PHIÊN BẢN v0.4.21 (THƯỞNG KIM CƯƠNG SỔ TAY & SỬA LỖI ÂM THANH)"));

        patchNotesItems.add(new UpdateItem("SỔ TAY", "Bổ Sung Thưởng Kim Cương 15 Bộ Sưu Tập Cá", newBadgeColor)
            .addPoint("Bổ sung thưởng Kim Cương milestone cho cả 15 bộ sưu tập cá: Ao Làng (140 KC), Bãi Biển (170 KC), Đảo Xa (230 KC).")
            .addPoint("Tổng thưởng trọn đời khi sưu tập đủ các bộ cá trong game: 540 Kim Cương.")
            .addPoint("Bổ sung thanh dock hiển thị số dư Vàng và Kim Cương trực tiếp trên thanh công cụ Sổ Tay.")
        );

        patchNotesItems.add(new UpdateItem("KHÁM PHÁ CÁ", "Thưởng Kim Cương Khám Phá Lần Đầu & Nhận Tất Cả", dataBadgeColor)
            .addPoint("Khi bắt được loài cá mới: Ô cá trong Sổ Tay mở tag phát sáng nhận KC: Common (+2), Uncommon (+3), Rare (+5), Epic (+10), Boss (+25).")
            .addPoint("Bấm chuột trực tiếp vào từng ô cá để nhận hoặc dùng nút '★ NHẬN TẤT CẢ ★' gom trọn toàn bộ thưởng cá và bộ sưu tập một chạm.")
            .addPoint("Lưu trữ bền vững trạng thái đã nhận thưởng cá vào SaveManager, hoàn toàn tương thích ngược.")
        );

        patchNotesItems.add(new UpdateItem("ÂM THANH", "Bộ Tổng Hợp Âm Thanh Thủ Tục ZzFX & Fallback RAM", fixBadgeColor)
            .addPoint("Tích hợp thuật toán ZzFX trực tiếp vào Java 17, tự tổng hợp sóng âm thanh (Sine, Triangle, Saw, Tan, Noise) trong RAM.")
            .addPoint("Nặng 0 KB tài nguyên đĩa, đóng gói chuẩn WAV 16-bit PCM nạp thẳng vào OpenAL Sound Engine của LibGDX.")
            .addPoint("Lưới an toàn tự động: Khi file âm thanh trên đĩa bị thiếu hoặc lỗi, game tự động phân tích và sinh âm thanh tức thì (Click, Catch, Bite, Cast, Coin, Skill, Level Up, Splash...).")
            .addPoint("Triệt tiêu vĩnh viễn nguy cơ văng game FileNotFoundException hay spam log lỗi.")
        );

        patchNotesItems.add(new UpdateItem("GIAO DIỆN", "Tối Ưu Thẻ Nhiệm Vụ Sự Kiện Gọn Gàng & Thoáng Đãng", newBadgeColor)
            .addPoint("Lược bỏ các hộp phần thưởng đơn lẻ gây chật chội trên 6 thẻ nhiệm vụ sự kiện.")
            .addPoint("Tiêu đề nhiệm vụ được mở rộng toàn chiều ngang thẻ, hiển thị rõ ràng và thẩm mỹ hơn.")
        );

        patchNotesItems.add(new UpdateItem("AUTO-UPDATER", "Module Tự Động Cập Nhật Trực Tuyến & Tự Động Khởi Động Lại", new Color(0.35f, 0.85f, 1f, 1f))
            .addPoint("Tự động kết nối GitHub Releases API kiểm tra phiên bản mới ngầm không gây gián đoạn.")
            .addPoint("Tải và thay thế file JAR, tự động chạy script khởi động lại game chỉ với 1 click [TẢI & CẬP NHẬT NGAY].")
            .addPoint("Hỗ trợ mở trực tiếp GitHub Releases để tải thủ công nếu người chơi mong muốn.")
        );

        patchNotesItems.add(new UpdateItem("THỜI GIAN THỰC", "Đồng Bộ Giờ Chuẩn Máy Chủ & Chống Gian Lận Đổi Giờ", new Color(1f, 0.65f, 0.25f, 1f))
            .addPoint("Đồng bộ thời gian thực chuẩn quốc tế (Google/Cloudflare/NTP) qua TimeSyncService ngầm.")
            .addPoint("Sử dụng đồng hồ đơn điệu Monotonic nanoTime: Triệt tiêu vĩnh viễn việc đổi giờ máy tính Windows để hack sự kiện/điểm danh.")
        );

        // ===== PHIÊN BẢN v0.4.20 =====
        patchNotesItems.add(UpdateItem.versionHeader("PHIÊN BẢN v0.4.20 (CỐ ĐỊNH 6★ LẠC HỒNG & ĐẠI TU CÁ THƯỜNG)"));

        patchNotesItems.add(new UpdateItem("GACHA", "Cố Định Phẩm Cấp Set Lạc Hồng & Khóa Roll Xịt", fixBadgeColor)
            .addPoint("Set Thần Khí Lạc Hồng luôn có phẩm cấp cố định 6★ (LIMITED_PREMIUM). Tuyệt đối không xuất hiện ở các phẩm cấp thấp hơn.")
            .addPoint("Khóa hoàn toàn Set Lạc Hồng khỏi vòng quay Thường; trong vòng quay Sự Kiện, Lạc Hồng chỉ có thể xuất hiện khi NỔ VÀNG 6★.")
            .addPoint("Hệ thống SaveManager tự động đồng bộ và nâng cấp mọi trang bị Lạc Hồng hiện có trong túi đồ người chơi về chuẩn 6★.")
        );

        patchNotesItems.add(new UpdateItem("CÂN BẰNG CÁ", "Đại Tu Toàn Diện 42 Loài Cá Thường Cả 3 Bản Đồ", dataBadgeColor)
            .addPoint("Tăng Máu và Lực kéo cho cá Common, Uncommon, Rare, Epic trên cả 3 Map (Ao Làng, Bãi Biển, Đảo Xa).")
            .addPoint("Chuẩn hóa TTK giằng co: Common (8-14s), Uncommon (15-22s), Rare (22-32s), Epic (35-50s); không còn bị kéo chết trong 1-2 giây.")
            .addPoint("Tăng mạnh phần thưởng Vàng và EXP tương xứng với độ khó và công sức kéo cá của cần thủ.")
        );

        patchNotesItems.add(new UpdateItem("KINH TẾ KC", "Cân Bằng Toàn Diện Nguồn Kim Cương (Chống Lạm Phát)", newBadgeColor)
            .addPoint("Cá Rare: Tỷ lệ rơi KC giảm về 20% (1 KC); cá Epic rơi 1-2 KC; Boss rơi 3-5 KC.")
            .addPoint("Nội tại cấp & cần thủ: Cấp thường +2 KC (mốc 5 cấp thưởng +10 KC); mỗi 25 cá câu thành công thưởng +3 KC.")
            .addPoint("Rương nhiệm vụ: Rương Ngày chuẩn hóa 10 KC (+250V, +250XP); Rương Tuần chuẩn hóa 40 KC (+1500V, +1500XP).")
            .addPoint("Sự kiện & Thành tựu: Điều chỉnh thưởng KC hợp lý, đảm bảo thu nhập F2P đạt ~1-1.2 chu kỳ Pity 80 mỗi tháng.")
        );

        // ===== PHIÊN BẢN v0.4.19 =====
        patchNotesItems.add(UpdateItem.versionHeader("PHIÊN BẢN v0.4.19 (CÂN BẰNG EXP, KINH TẾ & COMBAT)"));

        patchNotesItems.add(new UpdateItem("TIẾN TRÌNH", "Đại Tu Bức Tường EXP & Mở Khóa Bản Đồ", newBadgeColor)
            .addPoint("Khắc phục triệt để lỗi cấp số nhân x10 của công thức EXP cũ; chuyển sang công thức đa thức mượt mà XP(Level) = 35 * Level^1.65 + 40.")
            .addPoint("Tiến trình mở khóa bản đồ hợp lý: Đạt Lv.10 mở Bãi Biển sau ~200 con cá (~3-5 ngày) và đạt Lv.20 mở Đảo Xa sau ~575 con cá (~1-2 tuần cày cuốc).")
        );

        patchNotesItems.add(new UpdateItem("CÂN BẰNG BOSS", "Tăng Máu & Độ Giằng Co Cho 3 Boss Thế Giới", fixBadgeColor)
            .addPoint("Thủy Quái Sông Trà (Ao Làng): Máu tăng từ 600 HP lên 850 HP.")
            .addPoint("Bạch Tuộc Khổng Lồ (Bãi Biển): Máu tăng từ 1.200 HP lên 2.200 HP.")
            .addPoint("Thần Biển Leviathan (Đảo Xa): Máu tăng từ 3.500 HP lên 5.500 HP.")
            .addPoint("Kéo dài thời gian đối kháng câu Boss lên 45s - 90s, loại bỏ hoàn toàn hiện tượng Boss bị sốc sát thương chết trong vài giây.")
        );

        patchNotesItems.add(new UpdateItem("KINH TẾ & SHOP", "Đặt Trần Thưởng Cho Lưới & Bùa Hoàng Kim", dataBadgeColor)
            .addPoint("Lưới Đánh Cá: Tiền thưởng Vàng nhân thêm tối đa 400 Vàng, Kim Cương thưởng thêm tối đa 5 KC, ngăn chặn lạm phát tiền ở Map Đảo Xa.")
            .addPoint("Bùa Hoàng Kim: Đặt trần thưởng tối đa 500 Vàng trong 5 lượt buff.")
        );

        patchNotesItems.add(new UpdateItem("GACHA", "Chuẩn Hóa Tỷ Lệ Rate-Up 50% Banner Sự Kiện", newBadgeColor)
            .addPoint("Khi nổ trang bị 6★: 50% tỷ lệ ra đúng Set Thần Khí Lạc Hồng (chia đều 33.3% cho Cần, Dây, Lưỡi); 50% còn lại thuộc các Set 6★ khác.")
            .addPoint("Minh bạch mốc bảo hiểm Pity 80 lượt và bảo đảm tỷ lệ nhận trọn bộ 3 món công bằng cho người chơi.")
        );

        // ===== PHIÊN BẢN v0.4.18 =====
        patchNotesItems.add(UpdateItem.versionHeader("PHIÊN BẢN v0.4.18 (ĐẠI TU NGOẠI HÌNH SET THỜI KHÔNG & VECTOR PIPELINE)"));

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
        roadmapItems.add(UpdateItem.versionHeader("SỰ KIỆN ĐẶC BIỆT: HALLOWEEN 2026 (30/10 - 27/11, 28 NGÀY)"));

        roadmapItems.add(new UpdateItem("SỰ KIỆN", "Đêm Hội Ma Quái Halloween & Tự Động Chuyển Giao 28 Ngày", new Color(1f, 0.55f, 0.15f, 1f))
            .addPoint("Kéo dài trọn vẹn 28 ngày; tự động chuyển đổi giao diện Event, Shop đồ Halloween và chuỗi Điểm danh 25 Ngày.")
            .addPoint("2 Set Cần Câu Mới: Set Cày Điểm 'Dạ Hội Bí Ngô' (Shop SK & Điểm danh) và Set 6★ 'Huyết Nguyệt Thần Vương' (Đài Gacha Giới Hạn).")
            .addPoint("Bộ Cá Halloween: Cá Skeleton (Common), Cá Ma (Epic), Thủy Quái Frankenstein, và Siêu Boss Kình Ngư Thanatos 6666 HP.")
            .addPoint("Tính năng kẹo ma thuật Trick-or-Treat, Đèn Lồng Bí Ngô chiếu sáng đáy nước và thời tiết Bầu Trời Huyết Nguyệt.")
        );

        roadmapItems.add(new UpdateItem("GAMEPLAY", "Đại Tu Hệ Thống Kỹ Năng Cá & Hạng Cá Legendary Mới", new Color(0.95f, 0.8f, 0.25f, 1f))
            .addPoint("Bổ sung bậc phẩm cấp LEGENDARY (Mini-Boss khung viền Vàng Kim) lấp đầy khoảng trống giữa Epic và Boss thế giới.")
            .addPoint("Chuẩn hóa phân tầng kỹ năng: Common (0-1 Passive), Uncommon (1 Passive), Rare (1 skill), Epic (2 skill), Legendary (3 skill), Boss (3 skill độc nhất).")
            .addPoint("Đổi mới toàn bộ kỹ năng cũ: Vỏ cứng (giảm sát thương phẳng), Trơn trượt (né đòn miss), Bền bỉ (kéo dài PULLING), Vùng vẫy (dao động kim Tension).")
            .addPoint("Làm rõ 100% cơ chế Nhảy Vọt: Nhả dây trên không & nhấp kéo ngay khi chạm nước để gây x2 sát thương; giữ dây khi tiếp nước bị phạt sốc đứt cước.")
            .addPoint("Thiết kế bộ kỹ năng độc bản không trùng lặp cho cả 3 Boss thế giới (Thủy Quái Sông Trà, Bạch Tuộc Khổng Lồ, Thần Biển Leviathan).")
        );

        roadmapItems.add(new UpdateItem("ĐÃ RA MẮT", "Mã Hóa File Save & Chốt Hạn Sự Kiện 28/10 (v0.4.24)", new Color(0.35f, 0.85f, 0.50f, 1f))
            .addPoint("Đã hoàn thành mã hóa file save AES-256 kết hợp SHA-256 Checksum chống chỉnh sửa.")
            .addPoint("Đã chốt hạn Sự kiện Lạc Hồng vào 23:59:59 ngày 28/10; bảo lưu quy đổi Điểm SK sang Vàng và khóa an toàn Shop.")
        );

        roadmapItems.add(new UpdateItem("ĐÃ RA MẮT", "Game Juice, Screen Shake & GAS System (v0.4.211)", new Color(0.35f, 0.85f, 0.50f, 1f))
            .addPoint("Đã hoàn thành bộ điều khiển rung chấn Trauma Screen Shake & Easing Game Juice.")
            .addPoint("Đã hoàn thành Gameplay Ability System (GAS) chuẩn hóa bộ 4 kỹ năng QWER, Cooldown & Buff/Debuff.")
        );

        roadmapItems.add(new UpdateItem("ĐÃ RA MẮT", "Bộ Tổng Hợp Âm Thanh ZzFX & Auto-Updater (v0.4.21)", new Color(0.35f, 0.85f, 0.50f, 1f))
            .addPoint("Đã hoàn thành module tự động cập nhật game online 1-click từ GitHub Releases.")
            .addPoint("Đã hoàn thành đồng bộ thời gian thực chuẩn quốc tế (TimeSyncService) chống hack tua giờ.")
            .addPoint("Đã hoàn thành bộ tổng hợp âm thanh thủ tục ZzFX trong RAM, chống thiếu file âm thanh 100%.")
        );

        roadmapItems.add(new UpdateItem("ĐÃ RA MẮT", "Cố Định 6★ Lạc Hồng & Đại Tu Cá Thường (v0.4.20)", new Color(0.35f, 0.85f, 0.50f, 1f))
            .addPoint("Đã hoàn thành cố định phẩm cấp 6★ Set Lạc Hồng, cân bằng 42 loài cá và kinh tế Kim Cương.")
        );

        roadmapItems.add(UpdateItem.versionHeader("ĐẠI TU ĐỒ HỌA, GIAO DIỆN & HOẠT ẢNH (SERIES v0.5.x)"));

        roadmapItems.add(new UpdateItem("BƯỚC 1.1", "12 Icon 4 Set Khởi Đầu & Cơ Bản 0★-3★ (ĐÃ HOÀN THÀNH)", new Color(0.25f, 0.85f, 0.50f, 1f))
            .addPoint("Đã hoàn thành 12 Vector Icon cho 4 Set trang bị khởi đầu (Cần câu, Dây cước, Lưỡi câu).")
            .addPoint("Bao gồm: Set Tập Sự (0★ Common), Set Thợ Săn (1★ Uncommon), Set Băng Giá (2★ Rare) và Set Đại Dương (3★ Rare/Epic).")
            .addPoint("100% đạt chuẩn 100/100 Quality Gate và đồng bộ hoàn hảo thư viện SVG gốc.")
        );

        roadmapItems.add(new UpdateItem("BƯỚC 1.2", "9 Icon 3 Set Cao Cấp & Huyền Thoại 4★-6★ (v0.5.1b)", new Color(0.55f, 0.70f, 1f, 1f))
            .addPoint("Nâng cấp 9 Vector Icon cho 3 Set trang bị cao cấp rèn đúc tinh xảo (Cần, Dây, Lưỡi).")
            .addPoint("Bao gồm: Set Hải Tặc (4★ Epic), Set Vực Thẳm (5★ Legendary), Set Hoàng Gia (6★) (loại bỏ Set Thời Không đã hoàn thành ở SET-07).")
        );

        roadmapItems.add(new UpdateItem("BƯỚC 1.3", "9 Icon 3 Set Thần Thoại & Giới Hạn 6★-7★ (v0.5.1c)", new Color(1f, 0.85f, 0.35f, 1f))
            .addPoint("Nâng cấp 9 Vector Icon đỉnh cao cho 3 Set Thần Thoại & Giới Hạn độc bản (Cần, Dây, Lưỡi).")
            .addPoint("Bao gồm: Set Quang Minh (7★ Mythic), Set Vệ Quốc Vang Danh (Limited Event) và Set Thần Khí Lạc Hồng (6★ Limited Premium Gacha).")
        );

        roadmapItems.add(new UpdateItem("BƯỚC 1.4", "12 Icon Tiêu Hao & Đạo Cụ Tiệm Tạp Hóa (v0.5.1d)", new Color(0.40f, 0.85f, 0.50f, 1f))
            .addPoint("Chuẩn hóa nét vẽ vector sắc nét cho 5 vật phẩm tiêu hao: Bánh mì, Thuốc hồi stamina, Nước tăng lực, Nước bứt phá, Trà thảo mộc.")
            .addPoint("Chuẩn hóa 7 đạo cụ câu cá: Dây câu tốt, Bình xịt đóng băng, Đá mài lưỡi, Nam châm kho báu, Kính lặn, Lưới cá, Bùa hoàng kim.")
        );

        roadmapItems.add(new UpdateItem("BƯỚC 1.5", "12 Icon Mồi, Sách Kỹ Năng & Loot Đáy Biển (v0.5.1e)", new Color(0.85f, 0.50f, 0.95f, 1f))
            .addPoint("Chuẩn hóa icon Mồi Câu: Mồi Thơm (hũ xanh lá), Mồi Rủi Ro (hũ tím đầu lâu).")
            .addPoint("Chuẩn hóa biểu tượng 7 Sách Kỹ Năng bọc da kim loại & 8 món chiến lợi phẩm/rác đáy biển (Ủng rách, Bình cổ, Nhẫn vàng, Rương chìm...).")
        );

        roadmapItems.add(new UpdateItem("GIAI ĐOẠN 2", "Ngoại Hình 45 Loài Cá Vector 3 Bản Đồ (v0.5.2)", new Color(0.25f, 0.85f, 0.50f, 1f))
            .addPoint("Bước 2.1: Hoàn thành chuẩn hóa 15 loài cá Ao Làng & Siêu Boss Thủy Quái Sông Trà (vảy rêu phù sa, mắt quỷ dung nham).")
            .addPoint("Bước 2.2: Hoàn thành chuẩn hóa 15 loài cá Bãi Biển & Siêu Boss Bạch Tuộc Khổng Lồ (8 xúc tu xoáy mực, mắt tím ma mị, mỏ sừng chitin).")
            .addPoint("Bước 2.3: Hoàn thành chuẩn hóa 15 loài cá Đảo Xa & Siêu Boss Cá Cờ Xanh Khổng Lồ 3K HP (mũi kiếm plasma, cánh buồm lam ngọc, hào quang thần thoại).")
        );

        roadmapItems.add(new UpdateItem("GIAI ĐOẠN 3", "Môi Trường Nước 3 Bản Đồ & Khúc Xạ Ánh Sáng Biển (v0.5.3)", new Color(0.2f, 0.85f, 1f, 1f))
            .addPoint("Hoàn thiện chuyển động sóng nước tự nhiên theo thời gian thực tại Ao Làng, Bãi Biển và Đảo Xa (ENV-02).")
            .addPoint("Hiệu ứng rạn san hô, bóng nước khúc xạ ánh sáng (God Rays), bọt sóng ven bờ và đàn cá bơi lội bối cảnh sinh động.")
        );

        roadmapItems.add(new UpdateItem("GIAI ĐOẠN 4", "Hiện Đại Hóa UI, Nút Bấm Xúc Giác & Combat Gauges (v0.5.4)", new Color(1f, 0.75f, 0.25f, 1f))
            .addPoint("Khung viền Dialog hiện đại bo góc, nút bấm 3 trạng thái rõ ràng phản hồi âm click cơ khí ZzFX.")
            .addPoint("Thanh máu cá Ghost HP bar tụt dần theo sát thương, Thanh lực căng Gradient rung lắc chớp nháy khi căng cao.")
            .addPoint("Thẻ bài trang bị hiển thị khung viền chuẩn hệ Rarity và so sánh chỉ số tức thì (Stat Diff) khi hover.")
        );

        roadmapItems.add(new UpdateItem("GIAI ĐOẠN 5", "Bộ Hiệu Ứng Kỹ Năng VFX Cần Thủ & Boss Thủy Quái (v0.5.5)", new Color(0.95f, 0.55f, 0.25f, 1f))
            .addPoint("VFX chiêu thức cần thủ: Dưỡng Sức (vòng hồi ngọc), Giật Kép (sóng chém đôi), Trói Buộc (xích nước), Thuần Hóa, Mắt Đại Bàng.")
            .addPoint("VFX kỹ năng cá & Boss: Quẫy Bùn (che phủ HUD), Bứt Tốc (bọt xoáy), Lặn Sâu (tăng tension), Cuồng Nộ (hào quang đỏ), Giật Điện, Mực Mù, Sóng Thần, Bão Tố.")
        );

        roadmapItems.add(new UpdateItem("GIAI ĐOẠN 6", "Hoạt Ảnh Vật Lý, Băng Chuyền CS:GO & Custom Skin Động (v0.5.6)", new Color(0.95f, 0.45f, 0.75f, 1f))
            .addPoint("Hệ thống Custom Skin Động Cần - Dây - Phao trên PlayScreen: Tùy theo trang bị đang mặc mà ngoại hình cần câu trên màn chơi thay đổi tương ứng (Cần câu cho Cần, Dây câu cho Dây, Phao câu cho Móc).")
            .addPoint("Cải tiến hoạt ảnh cá quẫy đuôi mềm mại hình sin, uốn mình đổi hướng khi giằng co kéo dây với cần thủ.")
            .addPoint("Cần câu uốn cong vật lý co giãn theo lực căng Tension, hiệu ứng rung chấn màn hình giật cần cắn câu.")
            .addPoint("Hoàn thiện hoạt ảnh Băng Chuyền Gacha cuộn ngang kiểu CS:GO: Trượt lướt gia tốc pow5Out, kim chỉ vạch giữa & âm thanh tick qua từng món đồ.")
            .addPoint("Hiệu ứng Nổ Vàng cực đại cho trang bị Huyền Thoại / 6★ Thần Khí: Rung chấn màn hình, hào quang bùng nổ & showcase trang bị (không lật thẻ).")
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
