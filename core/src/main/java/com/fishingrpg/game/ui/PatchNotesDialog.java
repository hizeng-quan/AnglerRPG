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

    public static final String CURRENT_VERSION = "v0.4.24";
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

        // ===== PHIÊN BẢN v0.4.24 (MỚI NHẤT) =====
        patchNotesItems.add(UpdateItem.versionHeader("PHIÊN BẢN v0.4.24 (MỚI NHẤT - MÃ HÓA SAVE FILE & HẠN SỰ KIỆN 28/10)"));

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

        roadmapItems.add(new UpdateItem("GIAI ĐOẠN 1", "Ngoại Hình 11 Set Trang Bị & Icon Vật Phẩm (v0.5.1)", new Color(0.35f, 0.80f, 1f, 1f))
            .addPoint("Nâng cấp Vector Icon toàn bộ 33 món (11 Cần, 11 Dây, 11 Lưỡi) trong 11 Set của EquipmentIconRenderer.")
            .addPoint("Chuẩn hóa Vector Icon cho toàn bộ vật phẩm trong ItemDatabase: Bánh mì, Thuốc stamina, Nước tăng lực, Bình xịt băng, Trà thảo mộc, Đá mài, Nam châm...")
            .addPoint("Hoàn thiện icon các Sách Kỹ Năng và vật phẩm Loot câu được từ đáy biển (LootVisualRenderer).")
        );

        roadmapItems.add(new UpdateItem("GIAI ĐOẠN 2", "Hiện Đại Hóa UI, Nút Bấm Xúc Giác & Combat Gauges (v0.5.2)", new Color(1f, 0.75f, 0.25f, 1f))
            .addPoint("Khung viền Dialog hiện đại bo góc, nút bấm 3 trạng thái rõ ràng phản hồi âm click cơ khí ZzFX.")
            .addPoint("Thanh máu cá Ghost HP bar tụt dần theo sát thương, Thanh lực căng Gradient rung lắc chớp nháy khi căng cao.")
            .addPoint("Thẻ bài trang bị hiển thị khung viền chuẩn hệ Rarity và so sánh chỉ số tức thì (Stat Diff) khi hover.")
        );

        roadmapItems.add(new UpdateItem("GIAI ĐOẠN 3", "Ngoại Hình 45 Loài Cá Vector & Hiệu Ứng Chiêu Thức (v0.5.3)", new Color(0.25f, 0.85f, 0.50f, 1f))
            .addPoint("Đại tu 45 loài cá trong FishVisualRenderer với tạo khối mềm mại, bóng sáng và vây ngực chuyển động tự nhiên.")
            .addPoint("3 Siêu Boss: Thủy Quái Sông Trà (vảy rêu phù sa, quẫy đục đáy), Bạch Tuộc Khổng Lồ (xúc tu gai nhọn, túi mực đen), Thần Biển Leviathan (thân rồng viễn cổ, sừng san hô phát quang).")
            .addPoint("Hoàn thiện bộ VFX chiêu thức cần thủ (Dưỡng Sức, Giật Kép, Trói Buộc) và kỹ năng cá (Quẫy Bùn, Mực Mù, Sóng Thần, Bão Tố).")
        );

        roadmapItems.add(new UpdateItem("GIAI ĐOẠN 4", "Hoạt Ảnh Vòng Quay Gacha Wheel & Thao Tác Kéo Cần (v0.5.4)", new Color(0.95f, 0.45f, 0.75f, 1f))
            .addPoint("Cải tiến hoạt ảnh cá quẫy đuôi mềm mại, uốn mình đổi hướng khi giằng co kéo dây với cần thủ.")
            .addPoint("Cần câu uốn cong vật lý co giãn theo lực căng Tension, hiệu ứng rung chấn màn hình giật cần cắn câu.")
            .addPoint("Nâng cấp hoạt ảnh Vòng Quay Gacha Wheel 12 cung xoay tít gia tốc, đèn led viền chớp sáng & nảy kim.")
            .addPoint("Hiệu ứng hào quang ánh vàng bùng nổ và lật mở thẻ trang bị khi quay trúng bậc Legendary & Limited Premium.")
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
