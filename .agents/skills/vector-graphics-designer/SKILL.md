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

### Bảng màu chuẩn theo độ hiếm (Rarity Palettes):
- **Phổ thông (Common - Tập Sự):** Xám bạc kim loại (`#94a3b8`), Xanh thẫm (`#1e293b`), Điểm trắng (`#f8fafc`).
- **Hiếm (Rare - Băng Giá, Thợ Săn):** Lục bảo (`#10b981`), Xanh băng tuyết (`#38bdf8`), Lam hoàng gia (`#1d4ed8`).
- **Sử thi (Epic - Hải Tặc, Đại Dương):** Tím thạch anh (`#a855f7`), Lam ngọc sâu (`#0284c7`), Viền vàng (`#f59e0b`).
- **Huyền thoại (Legendary - Vực Thẳm, Long Vương):** Vàng hoàng gia (`#fbbf24`), Đỏ dung nham (`#ef4444`), Lam vực thẳm (`#0f172a`).

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
  ```java
  public static void drawCustomIcon(ShapeRenderer sr, float cx, float cy, float r, Color color) {
      // Logic vẽ vector bằng ShapeRenderer
  }
  ```
- Hoặc nếu viết Java trực tiếp, sử dụng tool `preview_libgdx_code` để xem ảnh giả lập kết quả OpenGL trước khi ghi vào `EquipmentIconRenderer.java`.

### Bước 5: Dọn dẹp tài nguyên trung gian (Cleanup & Pruning)
- Sau khi code đã được áp dụng vào game và kiểm thử thành công:
  * Gọi tool `cleanup_pipeline_files` hoặc chạy `python scripts/vector_pipeline.py clean` để xóa các ảnh preview so sánh cũ, file nháp thử nghiệm hoặc file rác phát sinh.
  * Giữ gìn workspace luôn sạch sẽ, chỉ lưu trữ các file SVG nguồn chính thức và mã nguồn Java.

