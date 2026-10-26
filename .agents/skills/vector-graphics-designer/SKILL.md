---
name: vector-graphics-designer
description: "Use this skill when designing, drawing, editing, or auditing vector artwork, equipment icons, fish sprites, and UI elements for AnglerRPG using SVG and LibGDX ShapeRenderer."
---

# Vector Graphics Designer Skill (AnglerRPG)

Chuyên môn thiết kế đồ họa vector phong cách **Cyber-Fantasy & Deep Ocean Abyssal** cho trò chơi AnglerRPG.

---

## 🎨 1. Quy chuẩn Thẩm mỹ & Bảng màu (Aesthetic Guidelines)

Mỗi tài sản đồ họa (Equipment, Fish, UI Ornament) trong game bắt buộc phải tuân theo nguyên lý 4 lớp hình học:
1. **Lớp 1 - Aura phát sáng nền (Atmospheric Glow):**
   - Hình tròn hoặc đa giác mờ (`alpha = 0.15f - 0.25f`) tạo vầng hào quang đại dương bí ẩn phía sau.
2. **Lớp 2 - Thân chính góc cạnh (Facet Body):**
   - Sử dụng các tam giác (`sr.triangle`) hoặc đa giác đối xứng chia nửa:
     * Cánh bên trái: Màu cơ sở rực rỡ (Base Tone).
     * Cánh bên phải: Màu bóng đổ sâu hơn (Shadow Tone, giảm 30-40% brightness).
3. **Lớp 3 - Lõi pha lê & Viền tương phản (Core Gem & Accents):**
   - Lõi năng lượng hoặc hoa văn kim loại hoàng kim / ngọc bích phát sáng (`alpha = 0.85f - 1.0f`).
4. **Lớp 4 - Điểm sáng chói cực hạn (Specular Glint):**
   - Điểm tròn trắng tinh khiết (`#ffffff`, `alpha = 0.95f`) đặt tại đỉnh chóp hoặc góc phản xạ ánh sáng trên-trái. Đây là yếu tố sống còn tạo độ bóng bẩy chuẩn game RPG.

---

## ⚡ 1.1 Nguyên Lý Độc Nhất Vô Nhị (Strict Non-Repetition & Unique Silhouette Rule)

> [!CRITICAL]
> **TUYỆT ĐỐI KHÔNG ĐƯỢC DÙNG CHUNG KHUÔN MẪU (TEMPLATE) HOẶC HÌNH DÁNG TRÙNG LẶP GIỮA CÁC SET TRANG BỊ!**
> Mỗi trang bị trong AnglerRPG, đặc biệt là các Set Bậc Cao (Legendary, Mythic, Limited Premium Gacha), phải có **phom dáng (Silhouette), cấu trúc khung xương (Skeleton) và hình thái mỹ thuật HOÀN TOÀN BIỆT LẬP**:

1. **Về Cần Câu (Rod Silhouette):**
   - Không được chỉ đơn điệu vẽ một đường chéo từ góc trái sang góc phải với vài đai đốt giống nhau.
   - Mỗi cần phải mang một phong thái vũ khí riêng biệt:
     * Cần dạng **Quyền Trượng Thánh Quang (Scepter)**: Đứng thẳng uy nghi hoặc nghiêng nhẹ, đỉnh trượng xòe cánh thiên thần khổng lồ hoặc vòng hào quang đa tầng.
     * Cần dạng **Thần Trượng Trống Đồng (War-Staff)**: Thân trượng dày dặn, khắc họa tiết rồng quấn quanh thân, đầu trượng là Chim Lạc sải cánh thần thoại hoặc đài sen nở rộ.
     * Cần dạng **Thương Khởi Nghĩa (Spear-Rod)**: Mũi giáo chiến trận bằng đồng, thân trúc son, cờ ngũ sắc bay phấp phới.
     * Cần dạng **Kiếm Sĩ / Cột Buồm / Hắc Ám**: Chuôi kiếm, xương sống quái thú, gai obsidian, hình thái bất đối xứng độc bản.

2. **Về Dây Cước (Line Spool Structure):**
   - **CẤM** vẽ tất cả các dây cước thành các vòng tròn đồng tâm với nan hoa bánh xe!
   - Đa dạng hóa cấu trúc cuộn cước:
     * Dạng **Song Long Chầu Nguyệt (Dragon Ouroboros)**: Thân rồng thiêng cuộn tròn ngậm viên ngọc rồng tỏa hào quang rực lửa.
     * Dạng **Khung Bát Giác Quân Cổ (Octagonal War Spool)**: Khung đồng 8 cạnh nạm đinh tán sao vàng, có tay quay chiến trận.
     * Dạng **Khung Tinh Thể / Chiến Xa Thánh Linh (Reliquary Loom)**: Khung kim cương/đồng hồ cát với 4 cánh thiên sứ bao bọc tơ vàng lơ lửng.
     * Dạng **Bánh Lái Hải Tặc / Bát Giác Hư Không / Bánh Xe Cung Đình**: Tùy biến hình học vỏ ngoài hoàn toàn khác biệt.

3. **Về Lưỡi Câu (Hook Anatomy & Form):**
   - **CẤM** dùng chung một đường cong Bezier chữ J rồi chỉ đổi màu!
   - Khắc họa phom dáng lưỡi câu hoàn toàn khác biệt theo từng chủ đề:
     * Dạng **Đài Sen Tiên Mẫu & Mỏ Chim Lạc**: Lưỡi câu cong vút mọc ra từ đài sen hồng ngọc, ngạnh câu tạc hình mỏ chim Lạc vươn dài đính ngọc.
     * Dạng **Thánh Kiếm Mỏ Neo (Cruciform Anchor)**: Lưỡi câu kết hợp mỏ neo thánh kiếm với vương miện hào quang thiên thần.
     * Dạng **Trảo Long Chiến Binh (Dragon War Claw)**: Móng vuốt rồng gãy khúc dữ dội nạm sao vàng, ngạnh câu xẻ rãnh máu chiến trận.
     * Dạng **Móng Vuốt Dã Thú / Mỏ Neo Pha Lê / Lưỡi Hái Damascus / Móng Vuốt Vực Sâu**: Từng đường uốn lượn, góc mở của họng câu (gap) và ngạnh (barb) phải độc nhất.

4. **Hiệu Ứng Bùng Nổ Cho Hàng Limited Premium Gacha (Thần Khí Lạc Hồng):**
   - Các trang bị Limited Premium phải mang khí chất hoàng gia cổ sử thần thoại hào hùng, hiệu ứng aura tầng tầng lớp lớp (lửa ruby, ngọc bích emerald, ánh vàng kim 24K), họa tiết văn hóa Đông Sơn & truyền thuyết Bách Việt (Lạc Long Quân - Âu Cơ - Chim Lạc) phải sắc sảo, choáng ngợp và ấn tượng ngay từ cái nhìn đầu tiên.

---

## 🛠️ 2. Quy trình làm việc với Vector Studio MCP

Khi cần vẽ một biểu tượng hoặc sinh asset mới:

### Bước 1: Thiết kế cấu trúc SVG / Shape
- Khởi tạo hình ảnh trong hệ toạ độ chuẩn `viewBox="0 0 100 100"` hoặc `viewBox="0 0 256 256"`.
- Giữ tâm hình ở giữa (`cx = width / 2`, `cy = height / 2`).

### Bước 2: Gọi Tool kết xuất ảnh Preview để kiểm duyệt mắt
- Sử dụng tool `render_vector_preview` từ MCP Server `vector-studio`.
- File ảnh PNG sẽ được lưu tự động tại `assets/vectors/previews/<asset_name>.png`.
- Agent xem ảnh preview và đánh giá độ nhận diện, màu sắc, độ bóng.

### Bước 3: Chạy Quality Audit
- Sử dụng tool `audit_vector_artwork`.
- Kiểm tra các tiêu chí:
  * Điểm số chất lượng >= 85/100.
  * Đã có Specular Glint (`#ffffff`).
  * Số lượng layer hình học từ 6 đến 40 shapes (tối ưu cho 60 FPS).

### Bước 4: Chuyển đổi sang mã Java LibGDX
- Sử dụng tool `svg_to_libgdx_code` để sinh ra hàm Java hoàn chỉnh:
  ```text
  public static void drawCustomIcon(ShapeRenderer sr, float cx, float cy, float r, Color color) {
      // Logic vẽ vector bằng ShapeRenderer
  }
  ```
- Hoặc nếu viết Java trực tiếp, sử dụng tool `preview_libgdx_code` để xem ảnh giả lập kết quả OpenGL trước khi ghi vào `EquipmentIconRenderer.java`.

### Bước 5: Dọn dẹp tài nguyên trung gian (Cleanup & Pruning)
- Sau khi code đã được áp dụng vào game và kiểm thử thành công:
  * Gọi tool `cleanup_pipeline_files` hoặc chạy `python scripts/vector_pipeline.py clean` để xóa các ảnh preview so sánh cũ, file nháp thử nghiệm hoặc file rác phát sinh.
  * Giữ gìn workspace luôn sạch sẽ, chỉ lưu trữ các file SVG nguồn chính thức và mã nguồn Java.

---

## 📐 3. Quy chuẩn Hiển thị Đầy đủ & Đồng nhất (Full Representation & Anti-Squish Gate)

Bắt buộc tuân thủ khi tích hợp đồ họa vector vào giao diện trò chơi:

1. **Đồng nhất mọi màn hình (Cross-Screen Consistency):**
   - Mọi asset sau khi thiết kế xong bắt buộc phải hiển thị đồng nhất ở:
     * **Màn hình Gacha:** Đài quay Banner Showcase, Vòng quay cuộn thưởng (Reel/Carousel), Thẻ kết quả trúng thưởng (1x & 10x), Bảng Tỷ lệ & Kho trang bị (Loot Table Modal).
     * **Màn hình Equipment:** Hòm đồ trang bị (Inventory Grid), Ô trang bị đang mặc (Equipped Slots), Khung xem trước chi tiết (Tooltip Card).

2. **Hiển thị Bản Đầy Đủ Chi Tiết (Full Representation - Tuyệt đối KHÔNG bóp/thu nhỏ):**
   - File SVG thiết kế rất đẹp và chi tiết; khi vào game **bản hiển thị phải là bản đầy đủ** tương xứng với tầm vóc thiết kế, tuyệt đối không được thu nhỏ vụn vặt làm bóp méo hình tượng:
     * **Trong Vòng quay Gacha (CS:GO Reel):** Bán kính icon tối thiểu `r >= 40f - 45f` (đường kính 80-90px), đặt ở vị trí trung tâm nổi bật trên thẻ cuộn.
     * **Trong Bảng Tỷ lệ & Kho (Loot Table):** Mỗi trang bị phải hiển thị dạng ô Showcase đứng độc lập với `boxH >= 115f`, bán kính icon `r >= 32f - 35f` với vầng hào quang aura, nhìn rõ từng ngạnh, khoen, hoa tuyết và rãnh hoa văn, tuyệt đối không nhét vào các thanh dẹt lép 50px với bán kính tí hon `r=15f`.
     * **Trong Thẻ kết quả quay trúng:** Bán kính icon `r >= 36f` (10x grid) và `r >= 50f` (1x single pull).
     * **Trong Bệ trưng bày Banner (Pedestal):** Bán kính icon `r >= 38f`.

3. **Tỉ lệ nét vẽ co giãn tự do (Dynamic Proportional Stroke Scaling):**
   - Trong mã LibGDX `ShapeRenderer`, các nét vẽ `rectLine` và bán kính `circle` phải co giãn tỉ lệ theo bán kính `r` (dùng `Math.max(1.0f, r * k)` thay vì hardcode mức sàn quá lớn như `Math.max(6.5f, ...)`). Điều này đảm bảo nét vẽ thanh thoát chuẩn SVG ở mọi độ phân giải, không bị biến dạng hay phình to thành cục màu thô ráp.

---

## 🛡️ 4. Quy Chuẩn Kiểm Thử Hình Học & An Toàn Tọa Độ (Coordinate Integrity & Visual Simulation Gate)

Để ngăn chặn triệt để lỗi vỡ hình, biến dạng răng cưa (sawtooth barcode) hay bệt màu hào quang:

1. **Tuyệt đối chính xác tọa độ đỉnh đa giác ($x, y$):**
   - Khi chuyển đổi hoặc viết hàm `fillTri`, `fillQuad`: Mỗi đỉnh bắt buộc phải nhận đúng cặp tọa độ $(x, y)$ phân biệt:
     $$P_0(x_0, y_0), \quad P_1(x_1, y_1), \quad P_2(x_2, y_2), \quad P_3(x_3, y_3)$$
   - Cấm kỵ tuyệt đối việc copy nhầm $y_1 = x_1$ hoặc $y_3 = x_3$. Lỗi này sẽ kéo giật mọi đa giác về đường chéo $y=x$, biến đường cong thành một rừng gai nham nhở.

2. **Quy chuẩn hào quang nền mờ dịu (`background_glow`):**
   - Lớp hào quang phía sau icon trang bị chỉ đóng vai trò tôn sáng, bán kính tối đa $r \le 42 - 44$ (trên viewBox 128x128) với độ trong suốt nhẹ nhàng ($0.12 \le \alpha \le 0.18$).
   - Tuyệt đối không vẽ hình tròn hào quang quá lớn ($r \ge 55$) hoặc màu đục làm thành đĩa tròn bệt màu che khuất ô trang bị.

3. **Kiểm tra bắt buộc qua Visual Simulation Tool trước khi bàn giao:**
   - Trước khi tích hợp hoặc chuyển đổi mã vào `EquipmentIconRenderer.java`, bắt buộc phải chạy mô phỏng hình ảnh qua `scripts/test_render_libgdx.py` (sử dụng Alpha Compositing chuẩn OpenGL) để mắt thấy tận mắt sản phẩm kết xuất cuối cùng.

4. **Tự động dọn dẹp thư mục Preview (`assets/vectors/previews/`):**
   - Sau khi hoàn thành tích hợp và kiểm định thành công, phải xóa toàn bộ các file ảnh `.png` trung gian trong `assets/vectors/previews/`, giữ cho thư mục luôn tinh gọn.


