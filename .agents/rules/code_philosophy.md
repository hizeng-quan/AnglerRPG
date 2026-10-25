# AnglerRPG Code Philosophy & Minimalist Engineering (Ponytail Rules)

> **Nguyên tắc cốt lõi:** *"Dòng code tốt nhất là dòng code không cần phải viết."*  
> Hãy tư duy như một **Senior Game Developer thực dụng**: giải quyết triệt để vấn đề với lượng code mới tối thiểu, an toàn nhất và dễ hiểu nhất.

---

## 1. Thang Quyết Định Tối Giản (The Lazy Decision Ladder)

Trước khi viết hoặc sửa bất kỳ dòng code nào trong AnglerRPG, AI Agent **bắt buộc** phải đi qua thang quyết định này và dừng lại ở phương án đầu tiên giải quyết được bài toán:

1. **YAGNI (Có thật sự cần tồn tại không?):**
   - Loại bỏ mọi tính năng, tùy chọn, wrapper trừu tượng không ai yêu cầu.
   - Một yêu cầu chung chung ("làm X") phải được giải quyết bằng phiên bản gọn nhẹ nhất đáp ứng đúng nghiệp vụ cốt lõi.
2. **Đã có sẵn trong Codebase AnglerRPG chưa?:**
   - Save game? Sử dụng `SaveManager`.
   - Âm thanh & SFX? Sử dụng `SoundController` và procedural ZzFX synthesizer có sẵn.
   - Dữ liệu cá & trang bị? Sử dụng `FishDatabase`, `EquipmentGenerator`.
   - UI & Popups? Tái sử dụng modal / floating text pattern của `GameHUD` và `BaseScreen`.
3. **LibGDX & Java Standard Library đã hỗ trợ chưa?:**
   - Toán & Ngẫu nhiên: Dùng `com.badlogic.gdx.math.MathUtils` (clamp, lerp, sinDeg, random).
   - Chuyển động & Easing: Dùng `com.badlogic.gdx.math.Interpolation` (swing, bounce, pow2) thay vì tự code hàm toán nội suy.
   - Vector & Hình học: Dùng `Vector2`, `Rectangle`, `Circle`.
   - Tối ưu bộ nhớ & Chống lag GC: Dùng `com.badlogic.gdx.utils.Pools` thay vì `new` object liên tục trong hàm `render()`.
   - Parse JSON: Dùng `JsonReader` / `JsonValue` có sẵn của LibGDX.
4. **Dependency đã cài đặt có làm được không?:**
   - Tuyệt đối **không** thêm thư viện ngoài (dependencies trong `build.gradle`) chỉ để giải quyết vài chục dòng code.
5. **Có thể viết trong 1 dòng dễ hiểu không?:**
   - Nếu một lập trình viên có thể hiểu ngay trong 1 dòng, hãy viết 1 dòng.
6. **Phương án tối thiểu:**
   - Chỉ khi các bước trên không đáp ứng, mới viết lượng code mới tối thiểu cần thiết.

---

## 2. Giới Hạn Cứng (Những Thứ Tuyệt Đối Không Được Cắt Xén)

"Lười biếng trong giải pháp" hoàn toàn khác với "cẩu thả trong điều tra":
- **Khảo sát kỹ trước khi sửa:** Phải đọc kỹ code xung quanh, tìm tất cả caller của hàm chuẩn bị sửa (`grep_search`), hiểu rõ luồng gọi trước khi gõ code.
- **Không bao giờ cắt bỏ:**
  - Logic xác thực dữ liệu tại ranh giới (Trust boundaries).
  - Khối xử lý ngoại lệ ngăn chặn mất dữ liệu save game.
  - An toàn vòng đời LibGDX / OpenGL (Resource Lifecycle Safety).
  - Phím bấm và khả năng tương thích (chuột + phím, chống nuốt phím tiếng Việt Unikey).

---

## 3. Quy Chuẩn Bất Di Bất Dịch Cho LibGDX & AnglerRPG

1. **An toàn Dispose & Screen Lifecycle:**
   - Trong mọi `Screen.render(float delta)`: Sau khi gọi `update(delta);`, nếu có khả năng screen bị chuyển hoặc dispose, bắt buộc phải có guard check:
     ```text
     if (game.getScreen() != this) return;
     ```
   - Không bao giờ gọi hàm vẽ trên `ShapeRenderer` hoặc `SpriteBatch` đã bị `dispose()`.
2. **Không ép kiểu / Chuyển đổi trung gian rườm rà:**
   - Giữ nguyên định dạng dữ liệu tự nhiên của engine. Tránh tạo DTO, Adapter thừa thãi khi không có nhiều hơn 1 nguồn tiêu thụ.
3. **Sửa lỗi tận gốc (Root Cause):**
   - Khi gặp bug, tìm nguyên nhân gốc rễ và sửa 1 lần tại lớp dùng chung, không "vá víu" (monkey-patch) ở từng màn hình riêng lẻ.
4. **Diff ngắn nhất là diff chiến thắng:**
   - Ưu tiên phương án có số dòng sửa đổi ít nhất, ít chạm đến các file không liên quan nhất.

---

## 4. Định Dạng Phản Hồi Của Agent

Mỗi phản hồi giải quyết code nên kết thúc bằng một ghi chú ngắn gọn (1–2 dòng):
- **Đã bỏ qua (YAGNI):** Những thứ cố tình không làm thêm để giữ giải pháp tối giản.
- **Rủi ro / Edge-case:** Những điểm cần lưu ý hoặc giới hạn nếu có.
