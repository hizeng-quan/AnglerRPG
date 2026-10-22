# Quy Chuẩn Đồ Họa & Render Vector (AnglerRPG Visual Guidelines)

Khi xây dựng, chỉnh sửa hoặc tối ưu hóa bất kỳ thành phần đồ họa nào trong dự án AnglerRPG, Agent và Lập trình viên **BẮT BUỘC** tuân thủ các nguyên tắc sau:

---

## 1. Nguyên Tắc An Toàn LibGDX ShapeRenderer
1. **Chia sẻ tài nguyên Singleton (Shared Resource):**
   - Tuyệt đối KHÔNG `new ShapeRenderer()` hoặc `sr.dispose()` cục bộ trong các Screen. Sử dụng duy nhất `game.shapeRenderer`.
2. **Bảo vệ Scissor Clipping & OpenGL State:**
   - Bất kỳ khi nào sử dụng `ScissorStack.pushScissors(scissors)`:
     * Phải gọi `sr.flush()` **trước khi** `pushScissors`.
     * Phải gọi `sr.flush()` **ngay trước khi** `popScissors()`.
     * Sử dụng ma trận `sr.getTransformMatrix()` và kích thước `Viewport` thực tế để tính toạ độ scissor.
3. **Bộ lọc biên cứng (Hard Bounds Culling):**
   - Khi vẽ danh sách cuộn, luôn kiểm tra toạ độ Y của từng phần tử (`rY + rowH >= boxBottom && rY <= boxTop`). Bỏ qua vẽ nếu phần tử nằm ngoài vùng hiển thị để tiết kiệm xung GPU.

---

## 2. Phong Cách Nghệ Thuật (Cyber-Fantasy & Deep Ocean)
1. **Quy tắc 4 lớp đa giác:**
   - Luôn thiết kế icon với: (1) Aura phát sáng mờ nền $\rightarrow$ (2) Thân đa giác chia mảng sáng/tối $\rightarrow$ (3) Chi tiết kim loại / ngọc viền $\rightarrow$ (4) Điểm sáng chói cực hạn (Specular Glint `#ffffff`).
2. **Quy chuẩn tỷ lệ tương đối:**
   - Các hàm vẽ icon nhận tham số tâm `cx`, `cy` và bán kính `r`. Toàn bộ toạ độ con phải tính toán phụ thuộc vào `scale = (r * 2f) / baseSize` để icon co giãn mượt mà ở mọi kích cỡ mà không bị lệch tâm.
3. **Tránh mảng màu đơn điệu:**
   - Không sử dụng các màu đơn sắc thô (như đỏ thuần `1,0,0` hay xanh thuần `0,0,1`). Phải dùng các dải màu phong phú (Cyan `#38bdf8`, Sapphire `#1e40af`, Hổ Phách `#f59e0b`, Lục Bảo `#10b981`, Thạch Anh Tím `#a855f7`).

---

## 3. Tối Ưu Hiệu Năng 60 FPS
- Mỗi icon trang bị / cá tối đa từ 8 đến 40 lệnh vẽ hình học (`triangle`, `circle`, `rect`).
- Tránh gọi `sr.setColor()` liên tục với các giá trị màu giống nhau liên tiếp.
- Tận dụng đối xứng gương (Symmetry) để tái sử dụng toạ độ hình học.
