---
name: game-graphics-pipeline
description: "Use this skill when running the AnglerRPG visual graphics pipeline, batch compiling vector assets, performing automated visual reviews, or integrating new graphic rendering systems."
---

# AnglerRPG Game Graphics Pipeline

Pipeline tự động hóa sản xuất, kiểm duyệt và chuyển đổi đồ họa cho dự án AnglerRPG.

---

## 🏗️ 1. Cấu trúc Quản lý Tài nguyên Đồ họa

```text
AnglerRPG/
├── .agents/
│   ├── plugins/vector-studio/    # Custom MCP Server (Node/Python)
│   ├── skills/                   # Antigravity Skills
│   └── rules/                    # Quy chuẩn đồ họa
├── assets/
│   ├── vectors/                  # Thư viện SVG gốc (Source of Truth)
│   │   ├── previews/             # Ảnh PNG render xem trước (Kiểm duyệt)
│   │   ├── equipment/            # Cần câu, Lưỡi câu, Dây câu, Phụ kiện
│   │   ├── fishes/               # Các loài cá thần thoại & vực thẳm
│   │   └── ui/                   # Badges, khung viền, icons
│   └── audio/ & data/            # Âm thanh & thông số game
├── core/src/.../ui/              # Java Renderers (EquipmentIconRenderer, etc.)
└── scripts/
    └── vector_pipeline.py        # CLI Pipeline Runner
```

---

## 🚀 2. Các lệnh thực thi Pipeline (CLI Commands)

Agent hoặc Nhà phát triển có thể kích hoạt pipeline bất kỳ lúc nào:

1. **Xem trước một file SVG sang PNG:**
   ```bash
   python scripts/vector_pipeline.py render assets/vectors/equipment/rod_abyss.svg
   ```

2. **Kiểm duyệt chất lượng toàn bộ tài nguyên (Audit all):**
   ```bash
   python scripts/vector_pipeline.py audit assets/vectors/
   ```

3. **Chuyển đổi SVG sang mã Java ShapeRenderer:**
   ```bash
   python scripts/vector_pipeline.py transpile assets/vectors/fishes/abyss_shark.svg --method drawAbyssShark
   ```

4. **Giả lập xem trước trực tiếp đoạn code Java ShapeRenderer:**
   ```bash
   python scripts/vector_pipeline.py preview-java "sr.setColor(1,0,0,1); sr.circle(128,128,50);" --name test_circle
   ```

5. **Bước cuối cùng: Dọn dẹp tài nguyên trung gian sau khi đã áp dụng code vào game:**
   ```bash
   # Dọn dẹp các file nháp, file so sánh cũ (old_*, temp_*, test_*, preview_*):
   python scripts/vector_pipeline.py clean --target intermediate

   # Hoặc dọn dẹp theo tên asset vừa làm xong:
   python scripts/vector_pipeline.py clean --target asset --asset thoi_khong

   # Dọn dẹp toàn bộ ảnh preview kết xuất tạm:
   python scripts/vector_pipeline.py clean --target previews
   ```

---

## 🛡️ 3. Cổng Kiểm Duyệt Hình Ảnh (Visual Quality Gate)

Trước khi thêm bất kỳ asset đồ họa mới nào vào game:
- **Nguyên tắc Bản Đầy Đủ (Full Representation - Tuyệt đối không bóp méo/thu nhỏ):**
  * Bản hiển thị trong game phải là bản đầy đủ chi tiết tương xứng với file SVG thiết kế gốc.
  * Đồng nhất tuyệt đối giữa Gacha (Vòng quay cuộn reel `r >= 42f`, Bảng Loot Table `r >= 34f`, Thẻ kết quả `r >= 36f-50f`, Bệ trưng bày `r >= 38f`) và Equipment (Hòm đồ, Ô trang bị, Tooltip).
  * Không nhồi nhét icon vào các thanh dẹt lép < 50px hoặc radius tí hon < 30px làm dính bết hình khối.
- **Tỉ lệ nét vẽ co giãn tự do (Dynamic Proportional Stroke Scaling):** Dùng `Math.max(1.0f, r * k)` cho độ dày nét để đường nét luôn sắc cạnh, thanh thoát, không bị biến dạng khi co giãn.
- **Kiểm tra tương phản nền tối:** Tất cả các nét vẽ phải nổi bật trên nền Abyss Navy `#030712`.
- **Độ ổn định khung hình:** Giới hạn không vượt quá 60 vertices cho mỗi icon để bảo toàn 60 FPS khi render lưới 9 quickslots và hàng trăm items trong hòm đồ.

---

## 🧹 4. Bước Cuối Cùng: Dọn Dẹp Sau Khi Tích Hợp (Post-Apply Cleanup)

Sau khi code Java LibGDX đã được tích hợp thành công vào mã nguồn game (ví dụ `EquipmentIconRenderer.java`) và kiểm tra biên dịch thành công:
1. **Mục đích:** Loại bỏ các file PNG render tạm thời, các file so sánh cũ (`old_*`), các file nháp thử nghiệm (`test_*`, `temp_*`) và snapshot dữ liệu test để giữ repository luôn sạch đẹp, tinh gọn.
2. **Quy tắc bảo vệ:** Giữ nguyên các file SVG thiết kế gốc trong `assets/vectors/equipment/` hoặc `assets/vectors/fishes/` làm tài sản gốc (Source of Truth), chỉ xóa các bản render xem trước hoặc file nháp tạm bợ.
3. **Thao tác nhanh:** Kích hoạt tool MCP `cleanup_pipeline_files` hoặc lệnh CLI `python scripts/vector_pipeline.py clean`.

