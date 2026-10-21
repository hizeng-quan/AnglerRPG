# 📜 NHẬT KÝ CẬP NHẬT (CHANGELOG) - ANGLER RPG

> File này được dùng để ghi nhận chi tiết toàn bộ các tính năng, nâng cấp giao diện, sửa lỗi và cân bằng game của **Angler RPG**.

---

## 📅 [2026-10-21] - Phiên bản v0.4.15 (Sửa Lỗi Nút Menu, Thêm Nút Xuất/Nạp Dữ Liệu & Đóng Gói .EXE Windows)

### 🚪 Sửa Lỗi Nút Thoát Ra Menu Ở Game Lại Ra Màn Hình Bản Đồ (`PlayScreen.java`)
- **Khắc phục triệt để lỗi điều hướng**:
  - Sửa nút `← MENU` góc trên cùng bên trái trong [PlayScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/PlayScreen.java#L266-L270) và phím bấm `ESC` để quay trở về đúng **Màn Hình Menu Chính (`MainMenuScreen`)** thay vì mở nhầm bảng Chọn Bản Đồ (`MapScreen`).
  - Tự động lưu tiến trình người chơi (`SaveManager.savePlayer(player)`) trước khi thoát ra Menu chính.

### 💾 Thêm Tính Năng Xuất & Nạp Dữ Liệu Tại Menu Chính (`MainMenuScreen.java` & `SaveManager.java`)
- **Nút "XUẤT DỮ LIỆU"**:
  - Lưu tiến trình hiện tại và xuất toàn bộ dữ liệu người chơi (Chỉ số, Túi đồ, Trang bị, Kỹ năng, Tiến độ nhiệm vụ) và Kỷ lục cá (`SizeRecord`) thành chuỗi JSON chuẩn hóa.
  - Tự động ghi ra tệp `angler_save.json` ngay trong thư mục game và đồng thời sao chép chuỗi dữ liệu vào Bộ nhớ tạm (Clipboard) của hệ thống.
- **Nút "NẠP DỮ LIỆU"**:
  - Mở hộp thoại chọn tệp hệ thống (`FileDialog`) cho phép người dùng chọn bất kỳ tệp sao lưu `.json` nào trên máy.
  - Sửa lỗi `ClassCastException` khi nạp dữ liệu: Tái cấu trúc cơ chế giải mã bằng `JsonReader` & `JsonValue`, đọc mượt mà cả cấu trúc JSON thuần túy lẫn đối tượng bọc giá trị (`value`).
  - Hỗ trợ cơ chế dự phòng thông minh (Fallback): Tự động nạp từ `angler_save.json` trong thư mục game hoặc từ nội dung Clipboard nếu người dùng sao chép dữ liệu từ nơi khác.
  - Sau khi nạp thành công, tự động cập nhật ngay toàn bộ thông tin người chơi trong phiên chơi.
- **Nút "RESET DỮ LIỆU" & Hộp Thoại Xác Nhận Khóa 10 Giây (`MainMenuScreen.java`)**:
  - Thêm nút quản lý tiến trình **"RESET DỮ LIỆU"** với phong cách viền đỏ thẫm obsidian cao cấp, hài hòa với giao diện Menu chính.
  - **Hộp thoại xác nhận an toàn với bộ đếm ngược 10 giây (10-Second Lock Modal)**:
    - Khi bấm vào "RESET DỮ LIỆU", giao diện hiển thị bảng cảnh báo an toàn nổi trên nền phủ tối làm mờ mượt mà.
    - Cảnh báo rõ ràng việc xóa toàn bộ dữ liệu cấp độ, kinh nghiệm, vàng, kim cương, trang bị, kỹ năng và kỷ lục câu cá để trở về Cấp 1.
    - **Nút "ĐỒNG Ý" bị khóa và hiển thị thời gian đếm ngược**: `ĐỒNG Ý (10s) ... ĐỒNG Ý (1s)`. Trong 10 giây này, người chơi không thể bấm xác nhận, ngăn chặn 100% tình trạng bấm nhầm.
    - Thanh đo tiến trình (Progress Bar) động thể hiện trực quan thời gian 10 giây đang trôi qua.
    - Sau khi hết 10 giây, nút xác nhận chuyển sang trạng thái mở khóa rực sáng **"✓ ĐỒNG Ý XÓA"**.
    - Nút **"✕ HỦY BỎ"** luôn sẵn sàng ở bất kỳ thời điểm nào (kể cả phím `ESC`) để người chơi hủy thao tác an toàn.
  - **Cơ chế sao lưu an toàn tự động**:
    - Trước khi xóa dữ liệu, hệ thống tự động xuất một bản sao lưu an toàn ra file `angler_save_before_reset.json`.
    - Sau đó làm sạch dữ liệu trong Preferences (`SaveManager.clearSave()`), khởi tạo đối tượng Người chơi mới Cấp 1 và lưu trạng thái khởi điểm sạch sẽ.
- **Biểu ngữ phản hồi đồ họa (Status Banner)**:
  - Thiết kế thanh thông báo kính mờ (Glassmorphism) phát quang hiển thị trực quan thông điệp thành công (viền xanh lục) hoặc cảnh báo lỗi (viền đỏ) kèm hiệu ứng âm thanh SFX.
- **Khắc phục triệt để lỗi game vẫn chạy ngầm sau khi đóng**:
  - Thiết lập luồng nạp dữ liệu thành daemon thread (`importThread.setDaemon(true)`), tự động giải phóng hộp thoại AWT (`fd.dispose()`).
  - Bổ sung `Lwjgl3WindowAdapter:closeRequested` và khối `finally { System.exit(0); }` trong `Lwjgl3Launcher.java` cùng nút thoát menu `System.exit(0)`, đảm bảo đóng hẳn tiến trình Java khi tắt cửa sổ.

### 📦 Đóng Gói File Chạy `.exe` Cho Nền Tảng Windows
- Tích hợp công cụ đóng gói native launcher tạo tệp thực thi `.exe` giúp người chơi khởi chạy game trực tiếp chỉ bằng một cú nhấp đúp chuột mà không cần cài đặt môi trường Java/JDK hay chạy dòng lệnh.

---

## 📅 [2026-10-21] - Phiên bản v0.4.14 (Gộp Kỹ Năng Boss, Cập Nhật Dây Câu Tốt, Hiển Thị Buff Quăng Cần, Sửa Chọn Bản Đồ, Nâng Cấp Kéo Mạnh & Hoạt Ảnh Cá Chuyển Động)

### 🐙 Gộp Kỹ Năng Boss Bạch Tuộc Khổng Lồ: Mực Mù Hút Máu (`muc_mu_hut_mau`)
- Hợp nhất hai kỹ năng riêng lẻ *Phun Mực Mù* và *Hút Máu* thành một tuyệt chiêu tối thượng duy nhất: **`Mực mù hút máu`** (`Fish.java`, `fishes.json`, `FishingManager.java`):
  - Khi thi triển, Boss vừa phóng luồng hắc ám vực sâu che khuất tầm nhìn GameHUD, vừa kích hoạt mỏ quạ cắn xé hút 20 thể lực cần thủ để hồi phục 4% HP/s cho bản thân.
  - Chuẩn hóa tên kỹ năng hiển thị có dấu đầy đủ trong Bách Khoa Toàn Thư (`CatalogScreen`) và Chữ nổi giao tranh (`FishingManager`).
  - Nâng cấp hiệu ứng hạt VFX (`CombatVFXManager`): Kết hợp quầng bào tử mực đen vực thẳm xoáy tròn cùng các dòng tinh thể hút máu đỏ thẫm rực cháy.

### 🧵 Sửa Lỗi Hiển Thị Dây Câu Tốt (`loot.json`)
- Đính chính mô tả vật phẩm tiêu hao *Dây Câu Tốt* trong `loot.json` từ `"Tăng max Tension +20."` thành **`"Giảm 20 Tension khi sử dụng."`**, khớp 100% với logic thực tế trong `ItemDatabase.java` và giao tranh câu cá.

### 🎯 Kích Hoạt & Hiển Thị Buff Tỷ Lệ Câu Khi Quăng Cần (Good / Perfect)
- Khi quăng cần đạt **PERFECT**: Tự động áp dụng và hiển thị buff **`QUĂNG HOÀN HẢO (PERFECT): +25% Tỷ lệ Hiếm, Cắn nhanh 50%`** ngay trên bảng báo `✦ HIỆU ỨNG TỶ LỆ CÂU KÍCH HOẠT ✦`.
- Khi quăng cần đạt **GOOD**: Tự động áp dụng và hiển thị buff **`QUĂNG CHUẨN XÁC (GOOD): +10% Tỷ lệ Hiếm, Cắn nhanh 20%`** trên bảng báo `✦ HIỆU ỨNG TỶ LỆ CÂU KÍCH HOẠT ✦`.
- Tích hợp trực tiếp chỉ số tỷ lệ hiếm tăng thêm này vào hàm chọn cá ngẫu nhiên `FishDatabase.java:weightedRandom`, giúp cần thủ thực sự nhận được phần thưởng xứng đáng với kỹ năng quăng dây.

### 🗺️ Sửa Lỗi Màn Hình Chọn Bản Đồ Luôn Hiển Thị "Đi Câu Ngay" Ở Ao Làng
- **Khắc phục triệt để lỗi ghi đè bản đồ**:
  - Loại bỏ hoàn toàn dòng code `player.setCurrentMap("ao_lang")` trong hàm `goBackToHub()` của `MapScreen.java`.
  - Tự động lưu tiến trình người chơi ngay khi chọn bản đồ mới (`SaveManager.savePlayer(player)`), đảm bảo bản đồ được chọn (như Bãi Biển) được bảo lưu vĩnh viễn.
  - Sửa nút `BẢN ĐỒ` trên thanh công cụ GameHUD ở màn hình chính (`PlayScreen.java`), luôn mở đúng màn hình Hải Trình Khám Phá mà không đưa về Menu chính.

### ⚡ Nâng Cấp Hiệu Ứng Kỹ Năng Kéo Mạnh (`keo_manh` - `CombatVFXManager`)
- Đại tu toàn diện diện mạo chiến đấu của chiêu thức *Kéo Mạnh*:
  - **Cặp mũi tên xung lực kéo bộc phá (Dual Kinetic Chevrons ▲ ▲)**: Hai mũi tên hoàng kim rực lửa phóng ngược lên không trung với tốc độ cao.
  - **Cột năng lượng cước căng đanh phát quang xé nước (Vertical Tension Surge Beam)**: Tia chớp năng lượng hoàng kim xé toạc làn nước.
  - **Vòng sóng xung kích mặt nước mở rộng**: Các đường elip sóng nước lan tỏa mạnh mẽ cùng 35 tia lửa năng lượng và bọt nước văng tóe.
  - Cập nhật thông điệp chiến đấu: `"BẠO KÍCH KÉO MẠNH! Xung lực bộc phá"` và chữ nổi vàng rực rỡ khi bạo kích.

### 🐟 Thêm Hoạt Ảnh Cá Chuyển Động (Kéo Ra Xa, Vùng Vẫy, Nhảy Vọt) Khi Đang Câu
- Tối ưu hóa toàn diện chuyển động vật lý của cá trong giao tranh (`GameHUD.java`):
  - **Khoảng cách động (Dynamic Distance & Scale)**:
    - Khi cá còn sung sức hoặc đang giằng giãy (`PULLING`), cá bơi lùi sâu về góc trên bên trái vào lòng đại dương, khoảng cách tăng lên và kích thước thu nhỏ về `0.95x`.
    - Khi cá kiệt sức (`TIRED`) hoặc cần thủ nhấn giữ [SPACE] thu cước, cá bị kéo tiến sát về phía thuyền/đầu cần câu ở góc dưới bên phải, kích thước phóng to lên `1.48x`.
  - **Hoạt ảnh vùng vẫy kịch tính**:
    - Khi cá giãy mạnh: Tần số quẫy vây đuôi tăng gấp 3.8 lần, cá lắc đầu quẫy mình sang hai bên liên tục, tạo bọt nước trắng xóa cuộn trào và vệt sóng rẽ nước dữ dội.
    - Khi cá mệt mỏi: Cá trôi nhẹ theo làn sóng, nhịp thu cước kéo giật từng nhịp uyển chuyển.
  - **Hoạt ảnh kỹ năng đặc biệt**:
    - *Nhảy vọt (`nhay_vot`)*: Cá uốn mình phóng vút khỏi mặt nước bay theo đường cung parabol 115px trên không trung, bóng in trên mặt nước và giọt nước rơi rớt sống động.
    - *Lặn sâu (`lan_sau`)*: Cá chúc đầu lặn sâu xuống đáy nước (-42px) thu nhỏ kích thước.
    - *Bứt tốc (`but_toc`)*: Cá phóng vút về bên trái xé nước tạo vệt sóng rẽ nước siêu tốc.
    - *Trói buộc (`troiBuocTimer`)*: Cá run rẩy vi mô bị đóng băng trong vòng tinh thể băng giá.
  - Dây câu và vòng rẽ nước tại miệng cá tự động bám dính chính xác 100% theo từng chuyển động của cá.

### 🖥️ Màn Hình Menu Chính (`MainMenuScreen`)
- Cập nhật số hiệu phiên bản ở góc dưới màn hình menu chính thành: **`v0.4.14 • Fishing RPG`**.

---

## 📅 [2026-10-21] - Phiên bản v0.4.13 (Chuẩn Hóa Kỹ Năng Bãi Biển, Sửa Lỗi Minimize Cửa Sổ, Cân Bằng Nhảy Vọt & Làm Mới Boss Bạch Tuộc)

### 🌊 Chuẩn Hóa Hiển Thị & Phân Bổ Kỹ Năng Cá Vùng Bãi Biển
- **Hiển thị kỹ năng có dấu tiếng Việt đầy đủ**:
  - Bổ sung hàm `Fish.getSkillDisplayName(skillId)` trong `Fish.java` chuẩn hóa 100% danh xưng tiếng Việt có dấu (`Nhảy vọt`, `Quét xúc tu`, `Ẩn mình`, `Vỏ cứng`, `Xúc tu siết`, `Phun mực mù`, `Hút máu`, `Vùng vẫy`, `Bứt tốc`, `Lặn sâu`, `Quẫy bùn`, `Sóng thần`, `Cuồng nộ`, `Bền bỉ`, `Trơn trượt`...).
  - Áp dụng đồng bộ trên **Bách Khoa Toàn Thư (`CatalogScreen`)** và toàn bộ thông điệp giao tranh, chữ nổi chiến đấu (`FishingManager`).
- **Phân bổ lại kỹ năng tự nhiên theo tập tính sinh học cho 15 loài cá Bãi Biển**:
  - Không còn hiện tượng các loài trong cùng bộ bị gán trùng lặp skill một cách máy móc.
  - Phân bổ tự nhiên:
    - *Bộ Ven Bờ*: Cá Chỉ Vàng (Common), Cá Đục (`Ẩn mình` dưới đáy cát), Cá Cơm (Common), Cá Mòi (`Trơn trượt` vảy bạc), Cá Lẹp (`Ẩn mình`).
    - *Bộ Bập Bềnh*: Cá Bạc Má (`Bứt tốc` săn mồi tầng mặt).
    - *Bộ Xa Bờ*: Cá Nục (`Vùng vẫy`), Cá Đối (`Nhảy vọt` trên sóng), Cá Trích (`Lặn sâu` lẩn tránh), Cá Chim Biển (`Vỏ cứng` mình dày).
    - *Bộ Thợ Săn Đáy Biển*: Cá Mú (`Vỏ cứng` vảy dày rạn đá & `Lặn sâu` ghìm đáy - loại bỏ Quẫy bùn không hợp với biển), Cá Bóp (`Vùng vẫy`, `Bứt tốc`).
    - *Bộ Vua Của Biển Cả*: Cá Thu Hoàng Kim (`Bứt tốc`, `Nhảy vọt`), Cá Ngừ Đại Dương (`Bền bỉ`, `Bứt tốc`, `Vùng vẫy`).

### 🐙 Làm Lại Bộ Kỹ Năng Boss Bạch Tuộc Khổng Lồ (Colossal Kraken)
- Bộ kỹ năng hoàn toàn mới khớp tuyệt đối với tạo hình 8 xúc tu khổng lồ uốn lượn, túi mực vực thẳm và mỏ quạ chitin:
  - **`Xúc tu siết` (`xuc_tu` - Passive)**: Siết chặt dây câu, ghìm chặt độ căng khiến dây hạ áp chậm hơn 30% khi chùng.
  - **`Quét xúc tu` (`quet_xuc_tu` - Active)**: Quét xúc tu tạo sóng xung kích cực mạnh gây +35% lực căng dây đột ngột và đánh tụt 25 thể lực cần thủ; tích hợp hiệu ứng hạt chấn động màu lam tím (`CombatVFXManager`).
  - **`Phun mực mù` (`phun_muc_mu` - Active)**: Phóng luồng hắc ám vực sâu che khuất giao diện và thanh đo lực kéo.
  - **`Mỏ quạ cắn xé` (`hut_mau` - Active)**: Boss dùng mỏ chim khổng lồ cắn xé dây câu hút 20 thể lực người chơi để hồi 4% HP/s cho bản thân.

### ⚖️ Cân Bằng & Kiểm Tra Kỹ Năng Cá (`FishingManager`)
- **Nerf kỹ năng Nhảy Vọt (`nhay_vot`)**:
  - Kéo dài thời gian cá lơ lửng trên không từ 1.0s lên **1.8s** giúp người chơi dễ dàng nhận biết và thả dây.
  - Thả dây khi cá đang trên không giúp hạ ngay 50% độ căng hiện tại; nếu cố tình giữ kéo dây thì lực căng chỉ tăng 15%/s thay vì làm đứt dây tức thì.
  - Cú tiếp nước (splashdown shock) được giảm mạnh từ 90% tức thì xuống còn **+35% lực căng**, hoàn toàn có thể kiểm soát và phản xạ xử lý.
  - Khi cá đang bay trên không, lực sát thương rút máu của người chơi giảm 50%.
- **Kiểm tra và kích hoạt hoàn thiện các kỹ năng nội tại**:
  - `Trơn trượt`: Giảm 20% sát thương kéo cá.
  - `Bền bỉ`: Giảm 15% sát thương kéo cá.
  - `Vỏ cứng`: Giảm 20% sát thương kéo cá.
  - `Ẩn mình`: Giảm 25% sát thương khi cá đang nghỉ ngơi (IDLE) và tăng thêm 0.6s thời gian nghỉ ngơi giữa các đợt giằng co.

### 📦 Sửa Lỗi Tràn Khung Thưởng Bộ Sưu Tập (`CatalogScreen`)
- Cân chỉnh tọa độ hiển thị văn bản phần thưởng bộ sưu tập (`Thưởng: +8000V • +4000XP`) và huy hiệu hoàn tất (`✓ ĐÃ HOÀN TẤT`) sang chế độ căn phải tự động `cx + cardW - 15f - layout.width`.
- Giữ khoảng đệm thẩm mỹ 15px cách mép phải thẻ, loại bỏ hoàn toàn hiện tượng tràn chữ ra ngoài hộp ở các bộ sưu tập có chỉ số thưởng lớn.

### 🛡️ Sửa Triệt Để Lỗi Crash Khi Nhấn Nút Thu Nhỏ Cửa Sổ (Minimize Crash)
- **Nguyên nhân gốc rễ**: Khi thu nhỏ cửa sổ trên hệ thống Windows/LWJGL3, kích thước màn hình bị đẩy về `0x0`. Thao tác `ScissorStack.pushScissors` bị từ chối (trả về `false`) nhưng `ScissorStack.popScissors()` lại bị gọi vô điều kiện trong `CatalogScreen` và `QuestScreen`, dẫn đến ngoại lệ `ArrayIndexOutOfBoundsException: -1` làm sập toàn bộ ứng dụng Java.
- **Giải pháp toàn diện**:
  - Bao bọc toàn bộ khối gọi `ScissorStack` bằng điều kiện kiểm tra boolean: `boolean hasScissors = ScissorStack.pushScissors(scissor); if (hasScissors) ScissorStack.popScissors();`.
  - Bổ sung chốt chặn bảo vệ kích thước cửa sổ `if (w <= 0 || h <= 0) return;` trong phương thức `resize(w, h)` và `if (Gdx.graphics.getWidth() <= 0 || Gdx.graphics.getHeight() <= 0) return;` trong phương thức `render(delta)` tại **toàn bộ 10 màn hình** (`PlayScreen`, `CatalogScreen`, `QuestScreen`, `ShopScreen`, `SkillTreeScreen`, `EquipmentScreen`, `GachaScreen`, `MapScreen`, `MainMenuScreen`, `EventScreen`).

### 🖥️ Màn Hình Menu Chính (`MainMenuScreen`)
- Cập nhật số hiệu phiên bản ở góc dưới màn hình menu chính thành: **`v0.4.13 • Fishing RPG`**.

---

## 📅 [2026-10-21] - Phiên bản v0.4.12 (Nâng Cấp Ngoại Hình Bộ Cá Vua Của Biển Cả & Toàn Diện Kỹ Năng Nhánh Nhanh Nhẹn)

### 👑 Nâng Cấp Ngoại Hình Bộ Cá Vua Của Biển Cả (`FishVisualRenderer`)
- **Đồng nhất 100% hình ảnh giữa Bách Khoa Toàn Thư (`CatalogScreen`) và Màn hình chơi (`GameHUD` - câu cá, giằng co & catch banner)**:
  - **Cá Ngừ Đại Dương (Pacific Bluefin Tuna - EPIC)**:
    - Thân hình ngư lôi khổng lồ lực lưỡng khí động học hoàn hảo; phối màu gradient đại dương từ xanh navy thẫm lưng, chuyển lam ngọc sapphire hông và bụng trắng bạc platinum; các dải sọc bạc dập nổi thẳng đứng.
    - **Hàng 9 vây phụ tam giác rời màu vàng chanh neon viền đen (electric yellow finlets)** dọc sống lưng và sống bụng dẫn về đuôi.
    - Đôi vây lưng và vây hậu môn lưỡi liềm cao vút xé nước; vây ngực vuốt cong hình thanh kiếm sắc bén.
    - Vây đuôi trăng khuyết khổng lồ (lunate sickle tail) với sống gờ chịu lực (caudal keel) và **gốc cuống đuôi cố định tuyệt đối trên thân**; mắt vàng kim thông minh với con ngươi đen sâu thẳm.
  - **Bạch Tuộc Khổng Lồ (Colossal Kraken - BEACH REGION MYTHIC BOSS)**:
    - Boss thần thoại tối thượng của vùng Biển Biển Cạn/Bãi Biển (1200 HP, 150 lực kéo, bộ kỹ năng: Xúc Tu Quấn, Phun Mực Mù, Hút Máu);
    - Hiệu ứng vực thẳm xoáy mực tím đen đa tầng (abyssal inky vortex) và quầng hào quang phát quang lam ngọc/tím dạ quang co bóp nhịp nhàng (`pulse`); 6 bào tử bóng đêm eldritch quay quanh.
    - **8 xúc tu cơ bắp khổng lồ uốn lượn uốn khúc theo sóng sin đa pha firmly anchored cố định gốc tại tâm cơ thể**, không bị văng tách thân; hai hàng giác hút giác mút phát quang neon cyan với lõi sáng trắng tinh khiết.
    - Vòm đầu bạch tuộc khổng lồ bọc giáp đá ngầm san hô và đốm hoa văn cổ ngữ dạ quang; mỏ quạ chitin đen bóng sắc nhọn ngay tâm xúc tu đang rỏ giọt nọc độc mực đen.
    - **Mắt cổ thần tà ác khổng lồ (Elder God eye)** với hốc mắt lõm sâu, tròng mắt vàng hổ phách rực lửa, quầng sáng cyan ma quái và đồng tử hình chữ nhật nằm ngang đặc trưng của loài bạch tuộc.

### 💨 Đại Tu & Tối Ưu Hóa Ngoại Hình Nhánh Nhanh Nhẹn & Kỹ Năng Kéo Mạnh (`SkillTreeScreen`, `GameHUD`)
- **Tối ưu hóa toàn bộ nhánh Nhanh Nhẹn theo phong cách thanh thoát, khí động học, thoáng đãng**:
  - **Nhịp Nhàng (`nhip_nhang`) - Chỉnh lại rõ ràng hình nốt nhạc ♫**:
    - Thiết kế nổi bật **cặp nốt móc đôi (Beamed Eighth Notes ♫)** chuẩn mực âm nhạc; hai đầu nốt nhạc hình oval nghiêng vàng kim óng ả có điểm sáng trắng lấp lánh; hai thân nốt vươn cao thanh thoát kết nối bởi **thanh xà kép (double beam)** trắng - lam ngọc.
    - Hậu cảnh điểm xuyết dải khuông nhạc mềm mại, các vòng sóng âm điều hòa đồng tâm lan tỏa và chùm sao giai điệu lơ lửng.
  - **Phản Xạ (`phan_xa`) - Làm lại diện mạo chim ưng phản xạ chớp nhoáng**:
    - Đôi cánh phản xạ lướt gió khí động học vuốt nhọn thanh thoát; viền mắt dáng hạnh nhân sắc sảo bao bọc tròng mắt vàng hổ phách rực lửa và con ngươi săn mồi thẳng đứng; **tia chớp thần kinh phản xạ nanosecond (lightning synapse)** cắt chéo dứt khoát qua tâm mắt.
  - **Lướt Dây (`luot_day`) - Làm lại lướt sóng hydrofoil thanh thoát**:
    - Ngọn sóng biếc uốn lượn mềm mại với vòm sóng cuộn trào thanh mảnh; đỉnh sóng điểm xuyết những giọt bọt tuyết trắng bạc; **lưỡi kiếm lướt sóng hydrofoil khí động học mạ vàng** lướt êm ái trên mặt sóng cùng hai dải đuôi nước (rooster-tail) rẽ sóng nhẹ nhàng và tia nước lấp lánh.
  - **Giữ Dây (`giu_day`) - Tối ưu thanh thoát**:
    - Ống cuộn aerolite giải đấu thanh mảnh vát mép, cuộn cước fluorocarbon vàng óng với các đường rãnh luồn dây tinh tế, cùm phanh carbon thanh gọn cùng đai từ trường trợ lực.
  - **Nhanh Tay (`nhanh_tay`) - Tối ưu thanh thoát**:
    - Bộ ba phi tiễn xé gió siêu thanh xếp bậc thon gọn, dải khí động học trắng - xanh ngọc thanh thoát cùng nón sóng xung kích Mach và tia lửa siêu thanh.
  - **Hồi Phục Nhanh (`hoi_phuc_nhanh`) - Tối ưu thanh thoát**:
    - Dải lụa gió thiên giới xoắn kép uốn lượn nhẹ nhàng bao bọc Giọt Ngọc Tinh Hoa Hồi Phục thanh khiết với ngọn lửa tinh tú vàng kim bên trong và hạt linh khí thăng hoa.
- **Kéo Mạnh (`keo_manh`) - Làm lại diện mạo Lưỡi Câu Trợ Lực & Xung Lực Kéo Bộc Phá**:
  - Thay thế khối cơ bắp cũ bằng **Lưỡi Câu Titan Chiến Đấu Khổng Lồ** uốn cong dũng mãnh, khuyên móc câu nối với sợi dây cước hoàng kim căng đứt sóng nước; mũi câu và ngạnh sắc bén chỉ thẳng hướng giật kéo; **hai mũi tên xung lực kéo bộc phá (double kinetic pull chevrons ▲▲)** hướng lên cùng chùm tia lửa lực kéo bộc phát, tạo cảm giác giật cần cực kỳ uy lực và rõ ràng.

### 🖥️ Màn Hình Menu Chính (`MainMenuScreen`)
- Cập nhật số hiệu phiên bản ở góc dưới màn hình menu chính thành: **`v0.4.12 • Fishing RPG`**.

---

## 📅 [2026-10-21] - Phiên bản v0.4.11 (Nâng Cấp Ngoại Hình Set Thần Khí Lạc Hồng, Bộ Cá Thợ Săn Đáy Biển & Toàn Diện Kỹ Năng Nhánh Sức Mạnh)

### 👑 Nâng Cấp Ngoại Hình Đỉnh Cao Set Thần Khí Lạc Hồng (`EquipmentIconRenderer`)
- **Đồng nhất 100% hình ảnh trên GachaScreen, EquipmentScreen, Inventory & Modal Chi Tiết**:
  - Hỗ trợ đồng bộ cả 2 mã định danh `"Thần Khí Lạc Hồng"` và `"Lạc Hồng"`.
  - **Trượng Lạc Hồng (Solar Bronze Drum Staff - Rod)**: Thân trượng đúc từ Đồng Thau Đông Sơn cổ đại khảm Vàng ròng 24K và lõi ngọc bích hoàng gia; 4 đai cổ phù đính ngọc Ruby đỏ thắm phát sáng; chuôi trượng bọc ngọc bích đan dây lụa đỏ Lạc Tướng kết hợp chuông đồng Đông Sơn và dải tua rua mềm mại; đỉnh trượng trang bị Trống Đồng Đông Sơn thu nhỏ với mặt trời 14 tia rực lửa và vòng chấm tròn tiếp tuyến; ngự trị trên đỉnh là chú **Chim Lạc thần thoại trong tư thế sải cánh bay uy nghi** với mào uốn lượn, mắt ngọc ruby và đuôi lông vũ dài duyên dáng.
  - **Dây Rồng Thiêng (Sacred Dragon Spool - Line)**: Khung ống cuộn cước tạo tác hình Trống Đồng Đông Sơn nguyên khối với 16 răng cưa hoa lửa mặt trời; **đôi Rồng Thiêng Lạc Long Quân uốn lượn ôm trọn lấy hai bên vành cuộn cước** với vảy ngọc lục bảo và sừng râu dát vàng; cuộn dây bện từ hàng trăm vòng gân rồng thiêng vàng kim & đỏ thắm; tâm trục là viên **Ngọc Rồng Lạc Hồng** đỏ rực tỏa quầng lửa thiêng 8 tia sáng; dải tơ vàng buông dài gắn ngọc bài Lạc Hồng lơ lửng.
  - **Móc Âu Cơ (Au Co Lotus Hook - Hook)**: Thân lưỡi câu uốn từ Thiên Thạch Thần Kim viền vàng 24K sắc bén vô song, chạm khắc cổ ngữ sóng nước ngọc bích; **đóa sen Tiên Mẫu Âu Cơ 3 tầng bung nở e ấp** nâng đỡ khuyên móc; mũi câu tạo hình mỏ Chim Lạc thần thoại sắc nhọn đính ngọc ruby; khuyên móc câu là viên **Tiên Châu Bách Noãn** (bọc trăm trứng thiêng) phát quang ngọc trai thuần khiết cùng dải lụa phụng hoàng đỏ thắm đung đưa nhẹ nhàng.

### 🦈 Nâng Cấp Ngoại Hình Toàn Diện Bộ Cá Thợ Săn Đáy Biển (`FishVisualRenderer`)
- **Đồng nhất 100% hình ảnh giữa Bách Khoa Toàn Thư (`CatalogScreen`) và Màn hình chơi (`GameHUD` - Combat & Catch Banner)**:
  - **Cá Mú (Reef Grouper - RARE)**: Thân hình dày cộp vạm vỡ, lưng gù cơ bắp chuyên phục kích rạn san hô; **hệ thống đốm hoa văn cẩm thạch ngụy trang san hô đa tầng (mottled coral leopard spots)** với quầng sáng vàng bao quanh; đầu to khổng lồ bọc giáp gai mang; **hàm dưới trề to khoác lác săn mồi (cavernous underhung jaw)** trang bị hàng răng nanh sắc lẹm; vây lưng dài 11 gai nhọn cứng cáp; vây đuôi quạt tròn dày dặn với gốc đuôi cố định chắc chắn trên thân.
  - **Cá Bóp (Cobia - RARE)**: Thân dài thuôn hình ngư lôi lực lưỡng màu nâu đen đồng thau (espresso bronze); **đầu dẹp bằng hình nêm đặc trưng như cá mập săn mồi (depressed shark head)**; **hai dải sọc trắng bạc / bạch kim phát quang chạy dài tăm tắp từ sau mắt dọc suốt chiều dài thân (distinct twin luminous stripes)**; hàng 8 gai lưng ngắn nhọn như dao găm; vây đuôi bán nguyệt uy dũng (lunate sickle tail) với gốc đuôi cố định tuyệt đối.
  - **Cá Thu Hoàng Kim (Golden Spanish Mackerel - EPIC)**: Thân dài thuôn hình mũi tên xé nước mạ vàng 24K rực rỡ; **hàng vằn cọp hoàng kim thẳng đứng (golden tiger bars)** và dải ánh kim phát sáng dọc thân; mõm nhọn hoắt với **hàm răng nanh tam giác sắc như dao cạo (razor-sharp interlocking teeth)**; đôi vây lưng lưỡi liềm cao vút xé gió; **8 đôi vây phụ rời màu vàng chanh (golden finlets)** dọc cuống đuôi dẫn đến chiếc đuôi cánh én khổng lồ vàng rực kiêu hãnh.

### ⚡ Đại Tu Ngoại Hình Toàn Bộ Kỹ Năng Nhánh Sức Mạnh (`SkillTreeScreen`, `GameHUD`, `CombatVFXManager`)
- **Đồng nhất 100% giữa Cây Kỹ Năng (`SkillTreeScreen`), Thanh Kỹ Năng Chiến Đấu (`GameHUD`) và Hiệu ứng xuất chiêu (`CombatVFXManager`)**:
  - **Kéo Mạnh (`keo_manh`)**: Bắp tay cơ bắp lực lưỡng titan nổi khối cuồn cuộn bốc lửa, nắm đấm giáp sắt siết chặt sợi dây câu hoàng kim căng đứt sóng nước, xung quanh bùng nổ tia sét vàng và quầng xung kích lửa cam.
  - **Dây Thép (`day_thep`)**: Lõi cáp thép bện xoắn 7 sợi đa tầng mạ titan ánh kim bao quanh lõi tungsten chịu lực, ốc khóa siết cáp lục giác hoàng kim cùng vòng đai từ trường trợ lực phát sáng.
  - **Cơ Bắp Thép (`co_bap_thep`)**: Bộ ngực và cơ bụng 6 múi giáp titan crôm bóng loáng chịu lực, mạch máu năng lượng dung nham đỏ cam rực lửa chảy dọc khe giáp cùng biểu tượng sức mạnh bất hoại ở tâm ngực.
  - **Bạo Kích (`bao_kich`)**: Vụ nổ siêu tân tinh bạo kích với 4 đại kiếm quang hướng tâm sắc lẹm, 8 tia gai xung kích thứ cấp và lõi kim cương trắng sáng rực lửa bung tỏa mảnh vỡ pha lê.
  - **Phá Giáp (`pha_giap`)**: Khiên giáp hiệp sĩ titan hạng nặng bị đập vỡ toác thành nhiều mảnh văng ra ngoài, từ vết nứt phun trào năng lượng dung nham đỏ cam cực độ cùng cơn mưa mảnh giáp kim loại.
  - **Thể Lực Dồi Dào (`the_luc_doi_dao`)**: Trái tim ngọc ruby giác cắt kim cương 3D rực lửa, đôi cánh phượng hoàng hoàng kim xòe rộng nâng đỡ, tâm điểm là xung nhịp điện tâm đồ sinh mệnh vàng kim bất tận.
  - **Bền Bỉ (`ben_bi`)**: Chiếc đe rèn thép núi lửa kiên cố đa tầng, búa rèn hoàng kim giáng xuống mặt đe tạo nên làn sóng nhiệt và cơn mưa tàn lửa rực sáng.
  - **No Bụng (`no_bung`)**: Đùi thịt nướng đại thú hoàng gia vàng ươm óng ả với rãnh nướng kim cương, nước sốt mật ong lấp lánh, đĩa bạc tráng lệ cùng 3 dải khói thơm uốn lượn tỏa hương thơm ngon.

### 🖥️ Màn Hình Menu Chính (`MainMenuScreen`)
- Cập nhật số hiệu phiên bản ở góc dưới màn hình menu chính thành: **`v0.4.11 • Fishing RPG`**.

---

## 📅 [2026-10-21] - Phiên bản v0.4.10 (Nâng Cấp Ngoại Hình Set Quang Minh, Bộ Cá Xa Bờ, Sửa Lỗi Đuôi Cá Bãi Biển Lắc Lư)

### 🌊 Khắc Phục Đuôi Cá Bãi Biển Lắc Lư Quá Mức (`FishVisualRenderer`)
- **Cố định tuyệt đối điểm gắn kết giữa cuống đuôi và thân mình**:
  - Khắc phục triệt để lỗi đuôi cá lắc lư quá mạnh khiến gốc đuôi bị trượt tách rời khỏi thân hoặc làm biến dạng cấu trúc cơ thể cá.
  - Tách bạch hoàn toàn tọa độ gốc đuôi (`baseX, baseY`) gắn chặt vào sống thân cá: điểm gắn kết này luôn cố định 100% trong khi các thùy vây đuôi (caudal fin lobes) và chóp vây phía sau vẫy sóng nhịp nhàng với biên độ tự nhiên mềm mại (`finWag`).
  - Áp dụng chuẩn hóa cho toàn bộ các loài cá Bãi Biển:
    - *Bộ Ven Bờ*: **Cá Chỉ Vàng**, **Cá Đục**, **Cá Cơm**.
    - *Bộ Bập Bềnh*: **Cá Mòi**, **Cá Lẹp**, **Cá Bạc Má** (ổn định cả hàng vây phụ detached finlets).
    - *Bộ Xa Bờ*: **Cá Nục**, **Cá Đối**, **Cá Trích**, **Cá Chim Biển**.
    - *Hệ thống cá dự phòng (`drawGenericFish`)*: Chuẩn hóa gốc đuôi cố định trên thân cho mọi loại cá chưa có vector riêng.

### ☀️ Đổi Tên & Đại Tu Ngoại Hình Set Thần Thoại Thành "Quang Minh" (`EquipmentIconRenderer`, `EquipmentGenerator`, `GachaScreen`, `EquipmentScreen`)
- **Chuẩn hóa danh xưng bộ trang bị**:
  - Đổi tên bộ trang bị từ **"Thần Thoại"** thành **"Quang Minh"** (Set Quang Minh / Celestial Radiance Set) nhằm triệt tiêu hoàn toàn sự nhầm lẫn giữa tên bộ trang bị với bậc phẩm chất / độ hiếm Thần Thoại (`PHẨM CHẤT: THẦN THOẠI`).
  - Cập nhật đồng bộ trong `EquipmentGenerator.java`, `PlayerEquipment.java`, `GachaScreen.java`, `EquipmentScreen.java` và `EquipmentIconRenderer.java`.
  - Đảm bảo **tương thích ngược 100% (backward compatibility)**: người chơi sở hữu trang bị có nhãn "Thần Thoại" trong file save cũ vẫn nhận đủ điểm kích hoạt set và hiệu ứng Set Bonus 3 món (+35% mọi chỉ số phụ) bình thường.
- **Nâng cấp ngoại hình đỉnh cao, đồng nhất 100% trên GachaScreen, EquipmentScreen, Inventory & Modal Chi Tiết**:
  - **Trượng Ánh Sáng (Solar Radiance Rod - Rod)**: Thân trượng đúc từ vàng ròng thái dương kết hợp lõi ngọc bạch kim tinh khiết; 4 vòng khấc đai cơ khí solar đính ngọc mặt trời sunstone; chuôi cầm quấn dải lụa thần thánh trắng ngà đan chéo vương giả; cổ trượng bung nở đôi cánh thiên thần bảo hộ; đỉnh trượng trang bị vương miện nhật hoa 8 tia sáng hoàng kim đa chiều hướng tâm với lõi kim cương thái dương tỏa ánh hào quang thiêng liêng rạng ngời.
  - **Dây Chỉ Vàng (Golden Sun-Thread Spool - Line)**: Ống cuộn cước đúc từ khối vàng 24K nguyên chất với vành 12 bánh răng hoa mặt trời và 8 nan hoa tỏa tia; hàng nghìn vòng chỉ vàng óng ánh dệt từ sợi ánh sáng thái dương thần thánh; tâm trục là ngôi sao bình minh 8 cánh đính ngọc; dải tơ vàng mềm mại uốn lượn đính giọt ngọc nhật quang lơ lửng.
  - **Lưỡi Câu Thần (Archangel Divine Hook - Hook)**: Thân lưỡi câu uốn cong từ hợp kim vàng thiên giới viền bạch kim sắc bén vô song; cánh thiên thần 3 tầng xếp lớp bung nở ở sống lưng lưỡi câu; ngạnh câu tạo hình mũi giáo thánh thiêng liêng đính ngọc; khuyên móc câu là vòng hào quang thánh thiên (holy halo eyelet) phát các tia sáng rực rỡ lơ lửng.

### 🐟 Nâng Cấp Ngoại Hình Toàn Diện Bộ Cá Xa Bờ (`FishVisualRenderer`)
- **Đồng nhất 100% hình ảnh trên Bách Khoa Toàn Thư (`CatalogScreen`) và Màn hình chơi (`GameHUD` - Combat & Catch Banner)**:
  - **Cá Nục (Round Scad - Uncommon)**: Thân hình thoi tròn lẳn cơ bắp dạng ngư lôi (cylindrical torpedo) màu xanh rêu biển chuyển dần sang sườn bạc; **hàng vảy gai cứng đường bên gồ ghề dày dặn (heavy armored lateral scutes)** sắc nét ở nửa sau thân; đôi vây phụ rời ở cuống đuôi đặc trưng cá nục; đốm đen nắp mang tròn thẫm rõ nét.
  - **Cá Đối (Mullet - Uncommon)**: Thân tròn mập phủ vảy bạc bóng láng; **6 dải sọc xám thẫm chạy dài song song tăm tắp dọc thân**; **đầu dẹp bằng, mõm tù tròn đặc trưng loài sục cát**; **hai vây lưng cách xa nhau rõ rệt (widely separated dual dorsals)** cùng đốm thẫm ở gốc vây ngực bản rộng.
  - **Cá Trích (Sea Herring - Uncommon)**: Thân dẹp bên thon mảnh; lưng xanh cobalt đại dương sâu thẳm chuyển dần sang sườn **vảy bạc sáng lấp lánh phản quang như kim cương (sparkling diamond scale reflections)**; sống bụng sắc lẹm hình lưỡi dao cùng bầy bọt khí và tàn sáng lơ lửng.
  - **Cá Chim Biển (Silver Pomfret - Rare)**: Thân hình thoi dẹp sâu dạng đĩa tròn (deep rhomboid disc) màu bạch kim ánh tím mộng mơ; **đôi cánh vây lưng và vây bụng vút cao kiêu hãnh như cánh chim (high falcate wing fins)**; vây đuôi chẻ sâu chữ V dài duyên dáng với thùy dưới dài hơn thùy trên đặc trưng.

### 🖥️ Màn Hình Menu Chính (`MainMenuScreen`)
- Cập nhật số hiệu phiên bản ở góc dưới màn hình menu chính thành: **`v0.4.10 • Fishing RPG`**.

---

## 📅 [2026-10-21] - Phiên bản v0.4.9 (Nâng Cấp Ngoại Hình Set Thời Không, Bộ Cá Bập Bềnh, Sửa Lỗi Đuôi Cá Ven Bờ & Khớp Loot Table Bãi Biển)

### ⏳ Nâng Cấp Ngoại Hình Đỉnh Cao Set Thời Không (`EquipmentIconRenderer`)
- **Đồng nhất 100% hình ảnh trên GachaScreen, EquipmentScreen, Modal Chi Tiết và Kho Trang Bị**:
  - **Trượng Thời Gian (Chrono Scepter - Rod)**: Thân trượng đúc từ hợp kim titan tinh vân xanh thẫm đan xoắn kép (dual-twist astral titanium shaft) bao bọc ống dẫn plasma năng lượng thì không phát sáng; 4 vòng khấc đốt cơ khí thiên văn bằng vàng kim đính tinh thể thạch anh tím vi sai; đỉnh trượng trang bị đĩa thiên văn Astrolabe 12 răng cưa bánh răng đồng thau quay chậm, tâm là viên tinh thể kỳ dị không-thời gian (singularity crystal) tỏa ánh tím huyền ảo cùng bộ kim đồng hồ la bàn cổ; trên cùng lơ lửng tinh thể đồng hồ cát nghịch đảo phản trọng lực (floating antigravity hourglass) với cát bụi sao lấp lánh; vầng hào quang tím - lục lam vũ trụ bao quanh.
  - **Sợi Dây Xuyên Thấu (Chrono Weave Line - Line)**: Ống cuộn cước chế tác từ đồng thau thiên văn nặng với vành bánh răng 12 nan hoa hướng tâm; hàng nghìn vòng cước dệt từ sợi hạt tachyon liên chiều không gian siêu dẫn (tachyon filament) đan xen sắc lục lam neon và tím thẫm rực rỡ; tâm trục là chân trời sự kiện lỗ đen mini (singularity event horizon) hút sáng; đặc biệt có **quỹ đạo hạt vô cực Möbius (infinity orbital loop)** phát quang lơ lửng bay vòng quanh cuộn cước.
  - **Lưỡi Câu Hư Không (Void Rift Hook - Hook)**: Thân lưỡi câu uốn cong từ đá hắc diện thạch vũ trụ (void obsidian) viền lưỡi lam ngọc sắc như dao cạo; khắc chạm các ký tự cổ ngữ chrono-runes phát sáng xanh lam dọc thân; mũi câu là vết rách không-thời gian hình lưỡi liềm tím thẫm (dimensional rift barb) rực sáng; khuyên móc câu gắn bánh răng thời gian hoàng kim kèm con lắc dây chuyền bụi sao (stardust pendulum charm) đung đưa nhẹ nhàng.

### 🐟 Nâng Cấp Ngoại Hình Bộ Cá Bập Bềnh (`FishVisualRenderer`)
- **Đồng nhất 100% hình ảnh trên Bách Khoa Toàn Thư (`CatalogScreen`) và Màn hình chơi (`GameHUD` - Combat & Catch Banner)**:
  - **Cá Mòi (Sardine / Pilchard - Common)**: Thân hình thoi thuôn dài, nén nhẹ đặc trưng; lưng màu xanh thép đại dương sâu thẳm chuyển dần sang sườn bạc óng ánh và bụng trắng ngọc trai có sống bụng sắc nét; **chuỗi 6 đốm đen ngọc thạch đặc trưng (signature dark spots)** có quầng sáng bạc bao quanh chạy đều tăm tắp dọc lưng trên; nắp mang ánh vàng chanh rực rỡ; vây lưng tam giác nhỏ và vây đuôi chẻ sâu chữ V chuyển động mềm mại.
  - **Cá Lẹp (Thryssa / Anchovy-Scad - Common)**: Thân dẹp bên siêu mỏng như chiếc lá tre hay lưỡi dao sắc lẹm (ultra-thin knife silhouette) màu ngọc bích nhạt ánh bạc gương; **vây hậu môn dải quạt dài đặc trưng (long anal fin)** viền hổ phách chạy dọc suốt nửa sau bụng; sống bụng sắc bén với hàng gai răng cưa (serrated keel scutes); **mõm nhọn hình nón cùng xương hàm trên kéo dài như chiếc gai nhọn (extended needle maxilla)** đặc trưng của họ cá lẹp; đôi mắt to tròn ánh vàng chanh.
  - **Cá Bạc Má (Indian Mackerel - Common)**: Thân hình thoi tròn lẳn lực lưỡng của dòng cá bơi nhanh đại dương; lưng xanh ngọc lục bảo biếc nổi bật **hoa văn rằn ri gợn sóng lượn sóng (wavy vermiculated zebra patterns)**; dải ánh kim vàng rực rỡ dọc đường bên; **5 đôi vây phụ nhỏ hình tam giác rời (detached finlets)** viền vàng ở cả lưng và bụng cuống đuôi dẫn đến vây đuôi hình trăng khuyết; đặc biệt nổi bật **đôi má nắp mang tròn to tráng gương bạc bạch kim sáng loáng ("Bạc Má" mirror cheek plates)** viền vàng phản quang rực rỡ.

### 🛠️ Sửa Lỗi Đuôi Cá Bộ Ven Bờ (`FishVisualRenderer`)
- **Cố định đường sọc thân khi đuôi cá chuyển động**:
  - Khắc phục triệt để lỗi đuôi cá vẫy làm kéo theo đầu mút của dải sọc vàng trên **Cá Chỉ Vàng**, đường bên trên **Cá Đục**, và dải sọc gương bạc / sống lưng trên **Cá Cơm**.
  - Tách biệt hoàn toàn tọa độ đuôi vẫy (`tailX, tailY`) ra khỏi các chi tiết trên thân: các dải sọc, gai vảy và xương sống giờ đây gắn cố định chắc chắn vào cấu trúc thân mình (`cx, cy`), chỉ có phần vây đuôi (caudal fin) ở phía sau chuyển động vẫy uyển chuyển, mang lại sự tự nhiên và chính xác tuyệt đối về mặt vật lý.

### 🔍 Kiểm Tra & Đồng Bộ Loot Table Với Catalog Bãi Biển
- **Xác thực khớp 100% danh mục cá Bãi Biển**:
  - Kiểm tra đối chiếu toàn diện giữa `assets/data/fishes.json` (15 loài cá Bãi Biển), `CollectionDatabase.java` (5 bộ sưu tập Bãi Biển) và `CatalogScreen.java`.
  - Toàn bộ 15 loài cá thuộc 5 bộ sưu tập Bãi Biển đều khớp chính xác từng loài:
    - *Ven Bờ*: Cá Chỉ Vàng, Cá Đục, Cá Cơm.
    - *Bập Bềnh*: Cá Mòi, Cá Lẹp, Cá Bạc Má.
    - *Xa Bờ*: Cá Nục, Cá Đối, Cá Trích, Cá Chim Biển.
    - *Thợ Săn Đáy Biển*: Cá Mú, Cá Bóp, Cá Thu Hoàng Kim.
    - *Vua Của Biển Cả*: Cá Ngừ Đại Dương, Bạch Tuộc Khổng Lồ.
  - Sửa đổi các mục mô tả (Lore) còn sót/lệch trong `CatalogScreen.java`: bổ sung đầy đủ tiểu sử cho `Cá Ngừ Đại Dương` và `Bạch Tuộc Khổng Lồ` (thay thế tên ảo Cá Bò Hòm Gai và Hải Vương), đồng thời bổ sung toàn bộ lore cho 15 loài cá Đảo Xa.

### 🖥️ Màn Hình Menu Chính (`MainMenuScreen`)
- Cập nhật số hiệu phiên bản ở góc dưới màn hình menu chính thành: **`v0.4.9 • Fishing RPG`**.

---

## 📅 [2026-10-21] - Phiên bản v0.4.8 (Nâng Cấp Ngoại Hình Set Tập Sự, Bộ Cá Ven Bờ & Chuẩn Hóa Danh Hiệu Bộ Sưu Tập)

### 🎣 Nâng Cấp Ngoại Hình Toàn Diện Set Tập Sự (`EquipmentIconRenderer`)
- **Đồng nhất 100% đồ họa trên cả GachaScreen, EquipmentScreen, Modal Chi Tiết và Kho Trang Bị**:
  - **Cần Câu Tre (Rod)**: Thân cần đúc từ tre ngà già óng ả uốn cong dẻo dai tự nhiên, nổi bật 4 lóng tre (culm nodes) với vòng khấc đốt đen mun và viền sáng hổ phách; tay cầm bọc lớp dây gai bện chéo mộc mạc (hemp twine grip wrapping) chống trơn trượt cùng chuôi gỗ tiện tròn cổ điển; 3 khoen dẫn cước bằng đồng thau được buộc chặt vào thân cần bằng sợi chỉ lụa đỏ son truyền thống; ngọn cần thon thả uốn lượn điểm xuyết chùm lá lộc tre xanh non mơn mởn tràn đầy sức sống; sợi cước mảnh bay lượn từ ngọn đính phao xốp bầu dục đỏ-trắng nổi bồng bềnh; bao bọc bởi vầng hào quang vàng nắng ấm áp.
  - **Dây Cước Thường (Line)**: Khung cuộn cước tạc từ gỗ hạt dẻ nguyên khối (walnut wood spool) với các đường vân gỗ nổi bật; hàng ngàn vòng cước monofilament bóng mờ trong suốt cuộn đều tăm tắp với ánh sáng phản quang lấp lánh; tâm trục khóa bằng khuy đồng thau đính nhãn giấy mộc cổ điển (vintage parchment label); đầu dây cước bung ra ngoài vành cuộn thắt nút số 8 đính hạt chặn phao cao su màu cam rực rỡ.
  - **Lưỡi Câu Chì (Hook)**: Thân lưỡi câu uốn từ thép carbon tôi đen bóng (tempered black steel) hình chữ J chuẩn mực, có khoen xỏ cước uốn tròn tinh xảo; mũi câu sắc bén cùng ngạnh giữ mồi ngược; kẹp phía trên thân lưỡi là viên chì kẹp xẻ rãnh kim loại (metallic split-shot lead sinker) xám bạc phản quang chân thực; đặc biệt đính kèm **chú giun đất đỏ tươi (succulent red earthworm)** ngoe nguẩy trên mũi lưỡi câu sống động, đậm chất câu cá đồng quê.

### 🐟 Nâng Cấp Ngoại Hình Bộ Cá Ven Bờ (`FishVisualRenderer`)
- **Đồng nhất 100% hình ảnh trên Bách Khoa Toàn Thư (`CatalogScreen`) và Màn hình chơi (`GameHUD` - Combat & Catch Banner)**:
  - **Cá Chỉ Vàng (Yellowstripe Scad - Common)**: Thân hình thoi dẹp hai bên đặc trưng; lưng ánh xanh ngọc bích nước sâu chuyển dần sang bụng trắng bạc xà cừ ngọc trai; **dải sọc vàng kim óng ả rực rỡ (signature golden stripe)** chạy suốt từ sau mắt đến tận cuống đuôi; đốm đen thẫm tròn trên nắp mang; vây đuôi chẻ sâu hình chữ V màu vàng chanh tươi sáng đung đưa nhịp nhàng; vây lưng gai nhọn và vây ngực hình lưỡi liềm vàng chanh; đôi mắt to tròn viền vàng sáng long lanh.
  - **Cá Đục (Sand Whiting - Common)**: Thân hình trụ tròn thuôn dài như ngư lôi mũi tên màu cát biển; lưng vàng cát pha nâu nhạt điểm xuyết các đốm cát li ti ngụy trang; bụng trắng sữa ánh bạc; **mõm dài nhọn hình nón (conical snout)** chuyên sục cát tìm mồi; vây lưng kép với vây trước dựng gai nhọn trong mờ và vây sau thuôn dài viền vàng cát; vây đuôi xẻ nông màu cát mềm mại; đôi mắt nhỏ màu hổ phách tinh ranh.
  - **Cá Cơm (Translucent Glass Anchovy - Common)**: Thân hình nhỏ bé thon dài trong suốt như pha lê ngọc bích (glassy translucent crystal body); **dải sọc gương bạc phản quang (iridescent silver-mirror stripe)** lấp lánh dọc thân; để lộ khung xương sống mờ ảo và túi nội tạng ánh bạc bên trong; miệng xẻ sâu đặc trưng của cá cơm biển; vây đuôi chẻ đôi thanh mảnh trong suốt; đi kèm **đàn cá cơm nhỏ mini bơi lướt xung quanh (schooling shoal companions)** tạo hiệu ứng đàn cá rẽ sóng sống động tột độ.

### 🏷️ Chuẩn Hóa & Làm Mới Tên Gọi Các Bộ Sưu Tập (`CollectionDatabase`)
- **"Sát Thủ Đáy Biển" $\rightarrow$ "Thợ Săn Đáy Biển"**: Loại bỏ từ ngữ tiêu cực ("sát thủ"), thay thế bằng danh hiệu phiêu lưu trang trọng, oai phong cho bộ ba cá săn mồi rạn biển (Cá Mú, Cá Bóp, Cá Thu Hoàng Kim).
- **"Vua Của Biển" $\rightarrow$ "Vua Của Biển Cả"**: Mở rộng tên gọi tròn trịa, văn phong hùng tráng và đậm chất ngôn ngữ biển cả cho bộ cá huyền thoại (Cá Ngừ Đại Dương, Bạch Tuộc Khổng Lồ).
- **"Truyền Thuyết Vực Thẳm" $\rightarrow$ "Truyền Thuyết Sông Hồ"**: Hạ bớt tính chất quá đà ("vực thẳm" vốn thuộc về biển sâu), điều chỉnh về "Sông Hồ" chuẩn mực và mộc mạc, hoàn toàn phù hợp với bối cảnh thủy tộc nước ngọt Ao Làng (Cá Lăng Khổng Lồ, Thủy Quái Sông Trà).

### 🖥️ Màn Hình Menu Chính (`MainMenuScreen`)
- Cập nhật số hiệu phiên bản ở góc dưới màn hình menu chính thành: **`v0.4.8 • Fishing RPG`**.

---

## 📅 [2026-10-21] - Phiên bản v0.4.7 (Đại Tu Giao Diện Màn Chơi Bãi Biển & Nâng Cấp Toàn Diện Combat HUD)

### 🏖️ Đại Tu Toàn Diện Bản Đồ Bãi Biển (`BaiBienBackground` & `PlayScreen`)
- **Background Bãi Biển Sống Động Đẳng Cấp (`BaiBienBackground`)**:
  - Thay thế hoàn toàn nền xanh phẳng bằng hệ thống đại dương nhiệt đới đa tầng sinh động:
    - **Dải quang phổ biển sâu**: Chuyển sắc mượt mà từ xanh ngọc bích nước nông (Cyan lagoon) sang xanh thẳm đại dương (Deep Azure).
    - **Vệt nắng xuyên sáng (Volumetric Sunbeams)**: 5 dải ánh sáng mặt trời vàng óng khúc xạ và đung đưa nhẹ nhàng qua làn nước biển trong vắt.
    - **Mạng lưới sóng phản chiếu (Caustics Animation)**: 8 dải gợn sóng khúc xạ ánh sáng lấp lánh phản chiếu xuống đáy cát.
    - **Đáy cát vàng nhiệt đới & Hệ sinh thái biển**: Đụn cát vàng uốn lượn, điểm xuyết sao biển đỏ rực, vỏ sò ngọc trai, rạn san hô sừng hươu tím, san hô khối hồng và hải quỳ phát quang sinh học.
    - **Rừng rong biển (Kelp Forest) & Bọt khí**: Dải rong biển đuôi nheo đung đưa theo nhịp sóng ngầm; bọt khí sủi tăm li ti nổi lên bề mặt.
    - **Hệ sinh vật biển bơi lội sống động**: Đàn cá bướm nhiệt đới bơi theo đàn, rùa biển bơi lướt qua làn nước, sứa biển trong suốt phát quang co bóp nhịp nhàng, tán dừa đung đưa ở góc màn hình.
- **Đồng bộ hóa 100% tính năng chiến đấu & quăng cần**:
  - Hợp nhất toàn bộ cơ chế quăng cần, thanh timing độ chuẩn xác, cắn câu, giằng co combat và hiệu ứng rung giật cần tương đương với Ao Làng.

### 🎮 Nâng Cấp Toàn Diện Giao Diện Chiến Đấu & Điều Khiển (`GameHUD`)
- **Sửa lỗi hiển thị vị trí câu**:
  - Khắc phục lỗi chữ bị gán cứng thành `"Ao Làng"`; vị trí câu giờ đây cập nhật động chính xác theo bản đồ hiện tại (`"Bãi Biển"`, `"Ao Làng"`, `"Đảo Xa"`).
  - Tăng độ rộng khung vị trí lên `115px` để hiển thị trọn vẹn, không bị tràn viền.
- **Huy hiệu Cấp độ người chơi**:
  - Loại bỏ hoàn toàn chữ `"LV."`; hiển thị số cấp độ sạch sẽ, sắc nét, căn chính giữa tâm huy hiệu vàng rực rỡ với bóng đổ tương phản cao.
- **Nâng cấp đồ họa Nút Quăng Cần (`drawCastButton`)**:
  - Bổ sung vầng hào quang phát sáng nhấp nháy (ambient outer glow pulse aura).
  - Khung bệ hợp kim đồng - obsidian đa tầng kết hợp dải gradient màu hổ phách rực lửa.
  - Dải ruy băng phản quang thủy tinh lấp lánh ở mép trên cùng 4 ngàm vàng viền góc.
  - Bổ sung vector minh họa **Phao Câu & Vòng Sóng Nước** tinh xảo bên trái nút.
  - Chữ `"QUĂNG CẦN"` sắc trắng ngà với bóng đổ đậm đà, rõ nét.
- **Nâng cấp Thanh Máu của Cá (Fish HP Bar)**:
  - **Khắc phục đè vị trí**: Dịch chuyển vị trí từ `H - 65f` xuống `H - 98f`, tạo khoảng cách 10px thoáng đãng ngay dưới thanh header, chấm dứt hoàn toàn hiện tượng che lấp thẻ vị trí và buff.
  - **Hoạt ảnh máu giảm (Trailing Damage Ghost Bar)**: Thanh máu ảo màu vàng hổ phách lưu lại và tụt dần khi cá nhận sát thương, tạo cảm giác đòn đánh lực và trực quan.
  - **Đổi màu động theo % máu**:
    - `> 50%`: Xanh lục bảo (Emerald Green) tươi sáng.
    - `25% - 50%`: Vàng rực rỡ (Radiant Gold).
    - `< 25%`: Đỏ Ruby rực lửa nhấp nháy báo động nguy cấp (Pulsing Ruby Red).
  - Bổ sung vạch chia độ tại 25%, 50%, 75% và dải sáng bóng gương trên đỉnh thanh máu.
- **Đồ họa Kỹ năng Vector Đồng Nhất & Bỏ nút Căng Dây**:
  - Xóa bỏ hoàn toàn nút `[CĂNG DÂY [SPACE]]` trên màn hình, giúp tầm nhìn phía dưới đáy thoáng đãng.
  - Căn giữa hoàn hảo 4 ô kỹ năng kích hoạt `[Q]`, `[W]`, `[E]`, `[R]` ở cạnh đáy màn hình.
  - **Sử dụng 100% hình minh họa vector chuẩn từ Skill Tree** (`SkillTreeScreen.drawSkillIcon`), đồng nhất tuyệt đối về nhận diện đồ họa.
  - Bổ sung lớp phủ đen mờ và bộ đếm giây hồi chiêu to rõ ngay giữa ô khi skill đang cooldown.
  - Huy hiệu phím tắt `[Q] - [R]` được đặt trang trọng trong khung nhỏ gọn ở cạnh dưới ô.
- **Nâng cấp Thanh Trạng Thái Cá & Cảnh Báo Kỹ Năng Boss**:
  - Cập nhật banner cảnh báo động ở giữa màn hình:
    - Cá đang mệt: Banner xanh ngọc với mũi tên chuyển động `►► CÁ ĐANG MỆT! GIỮ SPACE ĐỂ KÉO! ►►`.
    - Cá đang giãy mạnh: Banner đỏ son báo nguy `◄◄ CÁ ĐANG GIÃY MẠNH! THẢ DÂY! ◄◄`.
    - **Cảnh báo Kỹ năng Cá / Boss phát động**: Banner cảnh báo nguy hiểm riêng biệt nhấp nháy cho từng chiêu thức: `CUỒNG NỘ` (Đỏ lửa bất tử), `QUẪY BÙN` (Tím hư không mất độ căng), `SÓNG THẦN` (Lam sụt giảm thể lực), `BỨT TỐC` (Cam lực kéo x2), `PHUN MỰC` (Đen che khuất), `GIẬT ĐIỆN` (Vàng điện giật).
- **Hoạt Ảnh Kéo Cá / Rác / Kho Báu Lên Khỏi Mặt Nước (Reel-Up Leap Animation)**:
  - Bổ sung phân cảnh vọt lên khỏi mặt nước theo đường cung parabol sống động khi hoàn thành giằng co câu cá thành công:
    - Vòng sóng nước bung nở (concentric splash rings) và tia nước bắn tung tóe tại vị trí vọt lên.
    - Dây cước căng óng ả nối từ đầu cần câu tới chiến lợi phẩm.
    - Giọt nước li ti nhỏ giọt theo quán tính bay.
    - Vẽ chuẩn xác 100% vector minh họa loài cá câu được (`FishVisualRenderer`) hoặc rác / rương kho báu (`LootVisualRenderer`) kèm vầng hào quang rực rỡ.
    - Dòng chữ chúc mừng `ĐÃ KÉO LÊN!` cùng tên chiến lợi phẩm tỏa sáng trước khi mở bảng Sidebar.
- **Tối ưu Thanh Thể Lực (Stamina Bar)**:
  - Bỏ chữ `"THỂ LỰC:"`, chỉ giữ lại số liệu `315 / 315`.
  - Sửa lỗi bóng đổ nhòe mờ (do gọi đúp 2 hướng đối nghịch); chuyển sang bóng đổ đơn góc sắc nét chuẩn mực `(+1f, -1f)` màu đen obsidian, giúp số nổi khối rõ ràng trên nền xanh ngọc.
- **Cân lại Cụm Nút Quăng Cần (`drawCastButton`)**:
  - Khắc phục hiện tượng chữ bị lệch sang một bên do phao câu chiếm diện tích cố định bên trái.
  - Áp dụng thuật toán cân bằng động theo cụm: Căn chỉnh vị trí tâm của icon Phao câu + chữ `"QUĂNG CẦN"` đồng thời, giúp khoảng lề trái và lề phải nút luôn đối xứng chuẩn xác tuyệt đối (`54px` đều cả hai bên).
  - Tương tự đồng bộ căn giữa cho `"RA KHƠI CÂU CÁ"` tại Ao Làng.
- **Tinh Chỉnh Hiệu Ứng Quẫy Bùn (`fishQuayBunTimer`)**:
  - Xóa bỏ hoàn toàn dòng chữ thừa `"MẤT TÍN HIỆU"` gây rối mắt và tràn viền.
  - Căn giữa hoàn hảo ký tự `"???"` bên trong khung tiêu đề trên và căn giữa chính xác theo cả trục ngang lẫn trục dọc bên trong ống đo độ căng hình trụ màu tím hư không.
- **Nâng Cấp Đồ Họa Banner Cá Đang Mệt & Cá Đang Giãy**:
  - **Cá đang giãy mạnh**: Nâng cấp phong cách Danger Hazard với nền đỏ Ruby rực lửa, viền hổ phách vàng phát sáng, 2 dải sọc cảnh báo chớp nháy nhịp nhàng ở hai mép viền, cùng lời nhắc `◄◄ CÁ ĐANG GIÃY MẠNH! THẢ DÂY NGAY! ◄◄`.
  - **Cá đang mệt**: Nâng cấp phong cách Radiant Jade với nền xanh lục bảo tươi sáng, viền neon ngọc lam, cặp mũi tên tam giác emerald `►` ở hai đầu và thông báo `►► CÁ ĐANG MỆT! GIỮ [SPACE] KÉO CẦN! ►►`.
- **Rà Soát & Triệt Tiêu Toàn Bộ Ký Tự Ô Vuông (Font Glyph & Unicode Audit)**:
  - Khắc phục triệt để lỗi ô vuông do thiếu glyph trong LibGDX `FreeTypeFontGenerator`:
    - Bổ sung ký tự hình khối và biểu tượng typography chuẩn (`●`, `◆`, `■`, `◀`, `▶`, `❝`, `❞`) vào tập ký tự nạp font `symbolChars` trong `FishingRPG.java`.
    - Thay thế các ký tự emoji (⚠️, ⚡) và biến thể Unicode không tương thích font bitmap sang hệ biểu tượng typographic native (`[!]`, `◄◄`, `►►`, `✦`, `•`, `“”`).
    - Khắc phục lỗi ô vuông tại Banner Trạng Thái Cá & Boss (`GameHUD`), Thông báo Cắn Câu (`GameHUD`), Mô tả Loài Cá (`CatalogScreen`), Nút Mặc Nhanh & Chế độ Lọc Trang Bị (`EquipmentScreen`).
- **Hoạt Ảnh Quăng Cần & Thẻ Báo Cáo Hiệu Ứng / Tỷ Lệ Câu**:
  - **Quỹ đạo bay parabol (Casting Flight Animation)**: Khi quăng cần, phao câu vút bay từ đầu cần câu lên cao rồi lượn parabol xuống điểm câu giữa mặt nước, kèm vệt hạt chuyển động lấp lánh và sóng nước bung nở khi tiếp nước.
  - **Thẻ HUD Hiển thị Buff kích hoạt (`castBuffBannerTimer`)**: Ngay khi vào trạng thái câu, xuất hiện bảng Glassmorphism mờ sang trọng ở góc trên liệt kê toàn bộ các buff đang có hiệu lực (Điểm thưởng Canh chuẩn Perfect/Good, Mồi Thơm, Lưỡi câu, Set Hoàng Gia, Đá mài, Lưới, Kỹ năng) và tổng % cơ hội cá hiếm/kho báu.
- **Chuyển Đổi Nút Menu Thành Nút Quay Lại Chọn Bản Đồ**:
  - Nút `"MENU"` góc trên bên trái được đổi tên thành `"BẢN ĐỒ"` kèm vector mũi tên quay lại (`←`).
  - Khi nhấn nút `"BẢN ĐỒ"` hoặc phím `ESC`, trò chơi tự động lưu dữ liệu người chơi và chuyển mượt mà về thẳng màn hình chọn bản đồ (`MapScreen`).
- **Nâng Cấp Đồ Họa Cắn Câu (!) & Động Cơ Floating Text Chiến Đấu**:
  - **Đồ họa (!) cắn câu**: Bổ sung vòng sóng nước bung nổ dữ dội và tia nước bắn tung tóe quanh phao câu. Phía sau dấu chấm than xuất hiện huy hiệu kim cương Starburst truyện tranh đỏ viền vàng rực rỡ, kèm lời nhắc nhấp nháy `✦ CẮN CÂU! GIẬT [SPACE] NGAY! ✦`.
  - **Động cơ Floating Text**: Hoàn thiện hiệu ứng pop-in nảy bật (scale bounce), bóng đổ đa hướng sắc nét, phân biệt sát thương chí mạng lớn (>25 dmg = phóng to 1.35x màu vàng kim) và hồi máu thể lực (+X màu xanh ngọc).
- **Loại Bỏ Chữ Che Khuất Khi Kéo Lên**:
  - Xóa bỏ dòng chữ chúc mừng `"ĐÃ KÉO LÊN!"` và tên vật phẩm che chắn hoạt ảnh; giữ lại thuần túy hình ảnh cá/rác/rương kho báu vọt lên mặt nước ngoạn mục.
- **Pháo Hoa Ăn Mừng Kỷ Lục Mới (Fireworks Celebration)**:
  - Khi câu được cá phá kỷ lục chiều dài hoặc cân nặng (`SizeRecord`), tự động bắn nhiều chùm pháo hoa rực rỡ 48 hạt sao bay đa màu sắc (`triggerFireworks`) kèm âm thanh reo hò thắng lợi (Fanfare).
- **Cơ Chế & Thông Báo Cá Ăn Mồi Chạy Mất Khi Quá Giờ**:
  - Khôi phục cơ chế nếu cá cắn câu mà người chơi không kịp giật cần trong cửa sổ thời gian (`stateTimer <= 0`), cá sẽ ăn hết mồi và tẩu thoát.
  - Hiển thị banner cảnh báo nguy hiểm nổi bật ở giữa màn hình: `[!] CHẬM TAY RỒI! CÁ ĐÃ ĂN HẾT MỒI VÀ CHẠY MẤT! [!]` cùng âm thanh nhắc nhở.
- **Đổi "Độ Căng" thành Icon & Cân đối lại số**:
  - Bỏ dòng chữ `"ĐỘ CĂNG"`.
  - Bổ sung **Icon Đồng Hồ Đo Độ Căng (Dial Gauge Meter)** sắc sảo với kim quay động theo lực căng dây thực tế.
  - Cân chỉnh chỉ số độ căng (`0/310`) nằm ngay ngắn ở chính giữa ô thẻ.
- **Xóa bỏ dòng chữ gợi ý ở thanh Timing**:
  - Xóa bỏ hoàn toàn dòng chữ `"NHẤN [QUĂNG CẦN] HOẶC [SPACE] KHI KIM Ở VÙNG XANH!"` và khung viền nền bên dưới thanh canh lực quăng cần, giúp khu vực trung tâm tinh gọn, sạch sẽ.
- **Khắc phục triệt để lỗi vòng lặp điều hướng Nút Quay Lại (`PlayScreen`, `MapScreen`, `GameHUD`)**:
  - **Duy nhất một nút có sẵn, không sinh nút mới**: Chuẩn hóa hành vi của nút quay lại góc trên cùng bên trái trên toàn bộ các màn hình:
    - **Khi đang câu cá (Bãi Biển, Ao Làng, Đảo Xa - `state != IDLE`)**: Nút hiển thị `"← BẢN ĐỒ"`, nhấn vào hoặc bấm phím `ESC` sẽ lưu dữ liệu và mở bảng Chọn Bản Đồ (`MapScreen`).
    - **Tại màn hình Chọn Bản Đồ (`MapScreen`)**: Nút hiển thị `"← QUAY LẠI"`, nhấn vào hoặc bấm phím `ESC` sẽ thoát hẳn chế độ câu cá và trở về **Sảnh Làng Chài (`state = IDLE`)**.
    - **Tại Sảnh Làng Chài (`state == IDLE`)**: Nút hiển thị `"← MENU"`, nhấn vào hoặc bấm phím `ESC` sẽ thoát ra **Màn hình chính (`MainMenuScreen`)**; nút hành động trung tâm `"RA KHƠI CÂU CÁ"` mở màn hình chọn bản đồ.
  - Xóa bỏ hoàn toàn tình trạng bị kẹt trong vòng lặp vô tận giữa Bãi Biển và Bản Đồ.

---

## 📅 [2026-10-21] - Phiên bản v0.4.6 (Tối Ưu Bách Khoa Toàn Thư, Set Hoàng Gia & Truyền Thuyết Vực Thẳm)

### 📖 Tối Ưu Bách Khoa Toàn Thư Thủy Tộc (`CatalogScreen`)
- **Khắc phục lỗi hiển thị độ hiếm & Tooltip cá**:
  - Sửa lỗi thiếu kích hoạt `GL_BLEND` khiến dải tiêu đề bị đè màu đặc (opaque) làm chìm chữ; bổ sung cơ chế hòa trộn alpha mượt mà.
  - Tên cá được hiển thị nổi bật bằng sắc trắng tương phản cao (`Color.WHITE`).
  - Thiết kế huy hiệu phẩm chất (Rarity Badge/Pill) riêng biệt có khung viền và chữ mang đúng sắc màu phẩm cấp sống động (`UNCOMMON` xanh lục tươi, `RARE` xanh lam, `EPIC` tím huyền bí, `BOSS` đỏ lửa ma quái), không còn bị mờ nhạt hay chìm vào nền.
- **Ẩn thông tin kỷ lục trong danh sách thẻ cá**:
  - Loại bỏ hoàn toàn dòng hiển thị cân nặng & chiều dài kỷ lục (`X kg • Y cm`) trong các ô thẻ cá của danh mục, giúp giao diện danh sách thông thoáng, thanh thoát và gọn gàng hơn.
  - Tên cá được căn chỉnh lại ngay ngắn ở chính giữa đáy ô thẻ.
  - Kỷ lục kích thước giờ đây **chỉ xuất hiện độc quyền khi rà chuột (hover)** vào ô cá trong bảng thông tin chi tiết (`Tooltip`).
- **Bổ sung dòng mô tả sinh thái / cốt truyện (Fish Lore)**:
  - Tích hợp thêm trích dẫn mô tả sinh học & huyền thoại thủy sinh (`❝ ... ❞`) cho toàn bộ 45 loài cá thuộc 3 khu vực (Ao Làng, Bãi Biển, Đảo Xa) trong bảng thông tin chi tiết khi hover chuột, mang lại chiều sâu nội dung và trải nghiệm khám phá thế giới phong phú.

### 👑 Nâng Cấp Ngoại Hình Set Hoàng Gia (`EquipmentIconRenderer`)
- **Đại tu toàn diện Set Hoàng Gia (Imperial Royal Set)**:
  - Đồng nhất 100% hình ảnh trên cả giao diện Trang Bị (`EquipmentScreen`), Vòng quay Gacha (`GachaScreen`), Modal chi tiết và Bảng Kho Trang Bị (`Loot Table`).
  - **Cần Câu Bạch Kim (Rod)**: Thân cần đúc từ hợp kim Bạch Kim - Titan (Platinum-Titanium) vương giả với các vòng khắc vàng 24K; tay cầm bọc vải nhung hoàng gia đỏ thắm viền dây chỉ vàng bện chéo; chuôi cần tạo hình Vương Miện đính viên Hồng Ngọc (Imperial Ruby) phát sáng; 3 khoen dẫn cước bằng Vàng ròng đính đá Lam Ngọc (Sapphire) với ánh kim cương; ngọn cần gắn **Vương Miện Hoàng Kim Đế Vương** với viên Kim Cương Tinh Tú phát sáng rực rỡ trên đỉnh.
  - **Tơ Tằm Thượng Hạng (Line)**: Khung lồng tời tạc từ ngà voi hoàng gia nguyên khối chạm trổ 8 cánh hoa vương giả; cuộn tơ tằm thượng hạng đan sợi chỉ vàng ròng 24K và lụa hoàng cung đỏ son; tâm trục ngự Ấn Triện Hoàng Đế nạm viên Hồng Ngọc cắt giác tinh xảo; càng gạt tời đúc vàng với bạc đạn kim cương và sợi tơ tằm óng ả phóng ra ánh hào quang quý tộc.
  - **Móc Cẩm Thạch (Hook)**: Thân lưỡi câu tiện khắc từ khối Ngọc Bích Phỉ Thúy (Imperial Jadeite) nguyên khối bóng bẩy trong mờ, quấn dải lụa Rồng Vàng 24K dát nổi dọc sống lưng; mũi móc câu và ngạnh kép đúc bằng Bạch Kim tôi nguội sắc bén vô song; khoen đỉnh đúc hình Bông Sen Vàng Hoàng Gia treo tua rua Hồng Ngọc đế vương.

### 🐉 Nâng Cấp Ngoại Hình Bộ Cá Truyền Thuyết Vực Thẳm (`FishVisualRenderer`)
- **Đại tu toàn diện Bộ Cá Truyền Thuyết Vực Thẳm (Abyssal Legends)**:
  - Đồng nhất 100% hình ảnh hiển thị trên cả 3 phân hệ: **Bách Khoa Toàn Thư (`CatalogScreen`)**, **Mô hình bơi trong nước khi giằng co câu cá (`GameHUD`)**, và **Thông báo giỏ cá câu được bên thanh Sidebar (`GameHUD`)**.
  - **Cá Lăng Khổng Lồ (Titan Bagrid Catfish - Epic)**: Thân hình cá da trơn khổng lồ bọc giáp đá phiến thạch (slate-blue armor) trầm mặc uy dũng; sống lưng vạm vỡ gù cao chuyển dần sang bụng ánh bạc phát quang huyền ảo (moonlight-silver); **vây đuôi chẻ sâu cực lớn viền son đỏ ngọc** với các tia vây xanh băng rực rỡ; **ngạnh lưng xương hóa thạch khổng lồ sắc bén như lưỡi cưa** với 4 ngạnh ngược và lõi năng lượng xanh lam phát sáng; cặp ngạnh ngực tựa như đôi dao găm hộ vệ; **4 cặp râu cá lăng rồng uốn lượn uyển chuyển** với chóp râu phát quang; đường cảm giác bên phát sáng nhấp nháy và đôi mắt xanh băng săn mồi lạnh lùng.
  - **Thủy Quái Sông Trà (Apex Mythic Boss Behemoth - BOSS)**:
    - **Hào quang Huyết Ma Vực Thẳm đa tầng**: Vòng xoáy huyết tế đỏ thẫm và vòng năng lượng magma rực lửa tỏa ra xung quanh bạo chúa theo nhịp thở; 6 tàn lửa ma quỷ xoay tròn quanh thân thể.
    - **Đuôi lưỡi hái bạo chúa khổng lồ**: Cặp lưỡi hái đuôi obsidian đỏ thẫm sắc nhọn với 3 móng vuốt xương nhô ra ở mép sau và các đường gân magma phát sáng.
    - **7 Dãy gai bạo chúa tiền sử rực lửa**: Dãy sừng rồng tiền sử khổng lồ nhô cao tới 50px trên sống lưng với vết nứt dung nham nóng chảy chạy dọc trong từng gai.
    - **Thân bọc giáp rồng hắc ám & Hệ thống vết nứt magma**: Lớp vảy rồng obsidian ánh tím đen kèm hệ thống vết nứt dung nham rực lửa chạy dọc thân lườn; vây ngực vuốt quỷ với 3 móng vuốt xương sắc lẹm.
    - **Đầu bạo chúa tiền sử & Hàm răng nanh tử thần**: Hộp sọ cá sấu bọc giáp với cặp sừng quỷ vuốt ngược về sau; hàm há to khoe vòm họng đỏ rực tử thần cùng **6 chiếc răng nanh khổng lồ sắc nhọn lởm chởm nhuốm máu**.
    - **Đôi Mắt Huyết Ma Bạo Chúa**: Tròng mắt rực lửa ma mị, mống mắt vàng dung nham nóng chảy, con ngươi khe dọc đen ngòm cùng con mắt phụ phát quang trên thái dương.

---

## 📅 [2026-10-21] - Phiên bản v0.4.5 (Tối Ưu Modal Gacha, Set Thợ Săn & Sát Thủ Đầm Lầy)

### 🎰 Tối Ưu Bảng Xem Chi Tiết Set Trang Bị Gacha (`GachaScreen`)
- **Chuyển đổi phương thức mở từ Rà chuột (Hover) sang Click-to-Open**:
  - Không còn tự động bung popup che khuất màn hình khi vô tình lướt chuột qua.
  - Người chơi nhấp chuột trực tiếp vào bệ trang bị 3 ô (`Pedestal`) hoặc thẻ thông tin set bên phải để mở Modal chi tiết.
  - Bổ sung hiệu ứng viền phát sáng nhấp nháy (pulsing outline) khi rà chuột lên bệ để thông báo trạng thái có thể tương tác.
  - Hỗ trợ đóng Modal mượt mà bằng nút đóng [X] ở góc trên bên phải, nhấp chuột ra ngoài vùng Modal, hoặc nhấn phím `ESC`.
- **Cân đối lại giao diện & Phóng to x2 Vector Artwork**:
  - Kích thước Modal mở rộng thành `700 x 370 px`, bố trí cân đối ở chính giữa màn hình với lớp màn mờ (dark backdrop scrim) làm nổi bật tiêu điểm.
  - Khắc phục triệt để hiện tượng lệch trái: 3 ô trang bị (`CẦN CÂU`, `DÂY CƯỚC`, `LƯỠI CÂU`) có kích thước `200 x 205 px`, khoảng cách `22 px`, căn giữa hoàn hảo với lề trái và phải đều tăm tắp đúng `28 px`.
  - **Phóng to x2 ngoại hình trang bị (bán kính tăng từ 20px lên 38px)** kèm vầng hào quang aura tỏa sáng, giúp người chơi chiêm ngưỡng rõ nét từng chi tiết đồ họa vector cao cấp.
- **Tinh gọn nội dung hiển thị**:
  - Loại bỏ hoàn toàn các dòng chỉ số chung chung (`+Lực kéo cao`, `+Kháng đứt dây`, `+Giữ mồi tốt`).
  - Tập trung hiển thị rõ ràng thông tin cốt lõi: **Cấp Điểm Bộ & Hệ số Sức mạnh cơ bản (Multiplier)** cùng **Chi tiết Hiệu ứng kích hoạt Set 3 món**.
  - Xóa bỏ dòng chữ gợi ý `"Rà chuột vào trang bị để xem chi tiết"` phía dưới bệ quay.

### 🏹 Nâng Cấp Ngoại Hình Set Thợ Săn (`EquipmentIconRenderer`)
- **Đại tu toàn diện Set Thợ Săn (Hunter Apex Predator Set)**:
  - Đồng nhất 100% giữa giao diện Trang Bị (`EquipmentScreen`), Vòng quay Gacha (`GachaScreen`), Modal chi tiết và Bảng Kho Trang Bị (`Loot Table`).
  - **Gậy Phục Kích (Rod)**: Thân gậy chế tác từ gỗ mun lõi thép bọc da thú ngụy trang rêu phong; các vòng đai đồng cổ và dây da thuộc bện chéo hình chữ X giữ chắc tay cầm; chuôi cần bọc nanh thú gắn viên ngọc Mắt Hổ (Tiger-Eye Amber) với con ngươi khe dọc sắc sảo; 3 khoen dẫn dây tơ kẽm đính ngọc bích; đầu cần trang bị **Mũi Phóng Lao Thép Damascus Ngạnh Kép (Damascus Harpoon Spearhead)** có rãnh tẩm độc xanh ngọc (emerald venom groove) và ngạnh móc xé thịt kép dành riêng cho việc săn bắt thủy quái đầm lầy.
  - **Sợi Tơ Kẽm (Line)**: Khung lồng tời đúc từ sắt tôi nguội viền 6 ngàm vuốt bẫy thú bao quanh bánh răng đồng cổ; cuộn dây cáp tơ kẽm xoắn titan ánh kim xám thép đa tầng cực kỳ chắc chắn; tâm trục ngự khối ngọc mắt hổ bát giác; càng tời cơ khí với bạc đạn ngọc bích và đầu sợi tơ kẽm phóng ra kèm tia lửa căng lực.
  - **Móng Vuốt Biển (Hook)**: Thân lưỡi câu tạc từ xương móng vuốt hóa thạch của đại thủy quái biển sâu, bọc sống lưng thép cường lực chống biến dạng; dọc sống lưng trang bị 3 ngạnh răng cưa ngạnh ngược (reverse serrated spines); mũi móng vuốt cong vút sắc lẹm với rãnh độc xanh ngọc và ngạnh khóa mồi phụ bên trong; khoen đỉnh đúc bằng đồng cổ thắt nút dây da treo nanh quái thú hộ mệnh.

### 🐊 Nâng Cấp Ngoại Hình Bộ Cá Sát Thủ Đầm Lầy (`FishVisualRenderer`)
- **Đại tu toàn diện Bộ Cá Sát Thủ Đầm Lầy (Swamp Assassins)**:
  - Đồng nhất 100% hình ảnh hiển thị trên cả 3 phân hệ: **Bách Khoa Toàn Thư (`CatalogScreen`)**, **Mô hình bơi trong nước khi giằng co câu cá (`GameHUD`)**, và **Thông báo giỏ cá câu được bên thanh Sidebar (`GameHUD`)**.
  - **Cá Lóc Bông (Giant Chevron Snakehead - Rare)**: Thân hình ngư lôi săn mồi lực lưỡng dài chắc nịch; sống lưng đen rêu sẫm chuyển dần sang xanh ngọc lục bảo ở lườn và bụng trắng ngà; **hoa văn rằn ri sọc cọp chữ V (chevron tiger camouflage blotches)** màu đen tuyền zigzag sắc nhọn dọc thân; vây lưng dài chạy dọc toàn bộ sống lưng với các gai nhọn đầu ngọc bích; vây đuôi tròn xòe rộng với các dải rằn ri đồng tâm uyển chuyển; **đầu rắn dẹp bè với hàm dưới chìa dài hung hãn lởm chởm răng nanh nhọn hoắt**; đôi mắt đỏ hổ phách rực lửa săn mồi với con ngươi khe dọc nguy hiểm.
  - **Cá Trê Lai (Giant Marbled Hybrid Catfish - Rare)**: Thân hình đầm chắc cơ bắp phủ **hoa văn cẩm thạch da báo vàng nghệ đốm đen (yellow-black marbled leopard print)** phức tạp và chân thực; đầu bẹt bè bọc giáp xương dày cộp với các đường rãnh giáp đầu; miệng ngang rộng với bờ môi thịt; **4 chiếc râu dài linh hoạt (2 râu mép vuốt dài theo dòng nước, 2 râu cằm)** cử động sống động theo nhịp vẫy; vây ngực trang bị ngạnh xương sắc nhọn tự vệ; dải vây lưng và vây hậu môn uốn lượn chạy dài nối tiếp đuôi tròn dày dặn; đôi mắt vàng ti hí đặc trưng loài săn mồi đáy đầm lầy.
  - **Cá Chép Vàng (Imperial Golden Koi - Epic)**: Dáng cá chép gù cao bề thế, vạm vỡ dũng mãnh; vầng hào quang hoàng kim thần thoại (mythical radiant halo) tỏa sáng nhấp nháy linh thiêng; toàn thân phủ màu vàng ròng 24K ánh kim rực rỡ hòa cùng ánh xà cừ trắng vàng óng ả ở bụng; **hàng vảy rồng hoàng kim 3D (golden dragon scales)** xếp lớp đan chéo viền hổ phách sắc nét; **vây đuôi cánh bướm thiên giới (celestial butterfly fins) xòe rộng thướt tha mềm mại** với các dải voan vàng óng uốn lượn; vây lưng cong vút hoàng gia; đôi râu chép dài quý phái phong thủy và đôi mắt vàng ngọc bích có điểm sáng kim cương tôn nghiêm.

---

## 📅 [2026-10-21] - Phiên bản v0.4.4 (Vực Thẳm Huyền Bí & Kình Ngư Nước Ngọt)

### 🔮 Tạo Hình Trang Bị Vector (Artworks)
- **Nâng cấp Set Vực Thẳm (Abyssal Void Fiend Set)**:
  - Đồng nhất 100% giữa giao diện Trang Bị (`EquipmentScreen`), Vòng quay Gacha (`GachaScreen` khi quay & xem chi tiết kho đồ), và Loot Table popup.
  - **Trượng Hắc Ám (Rod)**: Thân trượng đúc từ Hắc Diệp Thạch (Obsidian Nether-Titanium) đen tím thẫm với vết nứt hư không dẫn plasma tím neon rực sáng dọc sống lưng; 3 đai gai xương quỷ dọc thân với chóp nhọn phát quang ma thuật; đầu trượng trang bị **Lưỡi Hái Hư Không (Void Crescent Scythe)** sắc lẹm xé toạc bóng tối và **Con Mắt Vực Thẳm Đa Tầng (All-Seeing Nether Eye)** với tròng mắt đỏ thẫm, mống mắt tím magenta và con ngươi thú săn mồi hình thoi đen sâu thẳm; chuôi trượng bọc da rồng biển sâu gắn tinh thể hắc ám phát sáng.
  - **Sợi Hắc Mạch (Line)**: Vầng hào quang xoáy hư không và tia chớp plasma tím ma quái; khung bánh xe tời đúc từ mai rùa quái thú vực sâu đen tím với 8 ngàm sừng quỷ nhọn hoắt phát quang quanh chu vi; cuộn sợi tơ huyết mạch đan xen giữa tím neon và đỏ tía ma quái; tâm trục ngự Hạt Nhân Vực Thẳm (Void Singularity Core) xoáy tròn với con ngươi mắt quỷ đỏ tím bí ẩn.
  - **Móng Vuốt Vực Sâu (Hook)**: Vầng hào quang độc khí vực sâu; thân móc câu đúc từ xương vuốt rồng bóng tối với rãnh huyết quang (void fuller) phát sáng rực rỡ; 4 lớp gai vảy rồng quỷ xòe ngược hiểm hóc dọc sống lưng; mũi móng vuốt tam đao nhọn hoắt tẩm độc hư không với ngạnh kép xé thịt lật ngược; khoen đỉnh đúc hình Đầu Quỷ Vực Thẳm sừng cong và chuỗi bùa huyết ngọc tử linh lơ lửng.

### 🐟 Tạo Hình Thủy Tộc (Fish Visuals)
- **Đại tu toàn diện Bộ Cá Kình Ngư Nước Ngọt**:
  - Đồng nhất 100% hình ảnh hiển thị trên 3 phân hệ: **Bách Khoa Toàn Thư (`CatalogScreen`)**, **Mô hình bơi trong nước khi giằng co câu cá (`GameHUD`)**, và **Thông báo giỏ cá câu được bên thanh Sidebar (`GameHUD`)**.
  - **Cá Trôi**: Thân hình ngư lôi thon dài vạm vỡ khí động học mang sắc xanh rêu ngọc bích (emerald sheen) óng ánh; lườn bụng trắng ngọc trai; các hàng vảy mịn ánh ngọc phớt hồng đào nhẹ; vây đuôi chẻ sâu vút nhọn viền **màu đỏ cam son tươi rực rỡ (rosy-crimson)** cùng các tia vây xanh bạc mềm mại; vây lưng cao vút hình lưỡi liềm viền son; nắp mang ánh đồng phớt hồng; miệng dưới hình móng ngựa với đôi râu mép nhỏ tinh tế; **đôi mắt có vành viền đỏ ngọc ruby rực lửa (ruby-red eye ring)** kiên cường.
  - **Cá Trắm Cỏ**: Thân hình trụ dài vạm vỡ như khúc gỗ, cơ bắp cuồn cuộn đặc trưng kình ngư sông hồ; sống lưng xanh ô-liu sẫm bóng chuyển dần sang vàng lục ánh kim ở hông và bụng trắng kem; **lớp vảy mắt lưới quả trám khổng lồ (diamond net scales)** xếp lớp đều tăm tắp với viền vảy nâu ô-liu rõ nét từng hàng; vây đuôi rộng bản chẻ đôi uy lực đẩy nước; **đầu to tù rộng dẹp bằng, miệng rộng không râu với bờ môi dẻo dai khỏe khoắn**; mắt vàng hổ phách ánh kim tĩnh tại, thông thái.
  - **Cá Chim Trắng**: Dáng đĩa tròn dẹt cao hình quả trám (diamond disc body) cực kỳ bề thế và cơ bắp; toàn thân phủ lớp vảy mịn màu trắng bạc bạch kim ánh thép sáng lóa với các chấm đốm đen nhỏ li ti phân tán trên sống lưng gù cao; **vùng ức, cuống họng và toàn bộ vây bụng, vây hậu môn có màu ĐỎ CỜ / CAM RỰC LỬA (vivid fiery-red throat & fins)** cực kỳ hung hãn và rực rỡ; vây đuôi xòe rộng với **dải viền đen tuyền ở mép sau (jet-black trailing margin)** tạo độ tương phản thị giác đỉnh cao; hàm răng ăn tạp cơ bắp và mắt bạc viền đen ánh thép sắc lẹm.

---

## 📅 [2026-10-21] - Phiên bản v0.4.3 (Băng Giá Vĩnh Cửu & Gương Mặt Thân Quen)

### 🐟 Tạo Hình Thủy Tộc (Fish Visuals)
- **Sửa lỗi đứt đuôi Cá Lóc Nhí**:
  - Khắc phục triệt để hiện tượng vây đuôi tròn của Cá Lóc Nhí bị tách rời khỏi thân (loại bỏ khoảng cách hổng 26px).
  - Bổ sung cuống đuôi thuôn cơ bắp kết nối liền mạch từ thân (`cx - 18f * s`) đến chốt gốc vây đuôi (`tailX`), kéo dài dải sọc cam hổ phách rực lửa (golden racing stripe) vuốt nhọn tiếp giáp tâm vây đuôi và giữ nguyên hiệu ứng vẫy đuôi nhịp nhàng khi bơi.
- **Đại tu toàn diện Bộ Cá Gương Mặt Thân Quen (Gương Mặt Quen Thuộc)**:
  - Đồng nhất 100% hình ảnh hiển thị trên 3 phân hệ: **Bách Khoa Toàn Thư (`CatalogScreen`)**, **Mô hình bơi trong nước khi giằng co câu cá (`GameHUD`)**, và **Thông báo giỏ cá câu được bên thanh Sidebar (`GameHUD`)**.
  - **Cá Chép**: Thân hình đầm chắc mang sắc vàng hổ phách ánh cam quý phái; vảy rồng hoàng kim xếp lớp đan chéo rực sáng; nắp mang vây ngực viền cam son; vây lưng có gai cưa uốn lượn; cặp râu chép dài uốn lượn phong thủy; vây đuôi chẻ đôi mềm mại rực màu son đỏ với các tia vây phân nhánh tinh xảo; mắt vàng viền ngọc.
  - **Cá Mè**: Dáng mình dẹp bên lưng gù cao đặc trưng màu trắng bạc bạch kim óng ánh; đầu to khí động học với sống lưng ánh xanh thép; điểm đặc trưng mắt to hạ thấp dưới trục ngang khóe miệng; lườn bụng mỏng vát nhọn hình sống dao; vây đuôi chẻ sâu vút nhọn và vây lưng cao vút.
  - **Cá Trê**: Thân trần không vảy màu nâu bùn đất ấm áp chuyển dần sang vàng nhạt ở bụng; đầu dẹp bè dạng xẻng bọc giáp xương chắc nịch; bộ 4 râu nhạy cảm (2 râu mép dài, 2 râu cằm ngắn) vươn dài sống động; vây ngực sắc nhọn như ngạnh ngạnh tự vệ; dải vây lưng và vây hậu môn chạy dài tít tắp sát gốc vây đuôi cánh quạt tròn dẻo dai.

### ❄️ Tạo Hình Trang Bị Vector (Artworks)
- **Nâng cấp Set Băng Giá (Glacial Permafrost Set)**:
  - Đồng nhất 100% giữa giao diện Trang Bị (`EquipmentScreen`), Gacha (`GachaScreen` khi quay & xem chi tiết kho đồ), và Loot Table popup.
  - **Cần Câu Băng (Rod)**: Cần câu chế tác từ trụ băng vĩnh cửu trong suốt sắc lạnh với lõi hàn khí plasma thiên thanh; đai kim loại Bắc Cực khắc băng phù văn; chuôi cần bọc da sói trắng; cuộn ròng rọc máy băng viền bông tuyết; đỉnh cần đính **Ngôi Sao Băng Tuyết 6 Cánh (6-pointed Arctic Snowflake Star)** xoay nhẹ tỏa hào quang buốt giá.
  - **Dây Lạnh Cóng (Line)**: Vầng hào quang tinh thể băng giá lục giác; bánh xe tời đóng băng dày từ sông băng cổ đại viền 6 gai nhọn pha lê; cuộn dây làm từ sợi siêu dẫn nhiệt độ không tuyệt đối (absolute-zero filament) xanh lam buốt nhung; tâm trục đính tinh thể băng vĩnh cửu phát sáng lấp lánh.
  - **Móc Câu Tuyết (Hook)**: Vầng hào quang bão tuyết cuộn xoáy; thân móc câu đúc từ khối băng tinh linh thạch không bao giờ tan, chạm khắc cổ ngữ băng tuyết phát sáng; mũi câu nhọn hoắt dạng cột băng nhũ sắc lẹm với 2 ngạnh móc băng sắc nhọn; khoen đỉnh đính tinh thể bông tuyết pha lê và chuỗi hạt tuyết lơ lửng.

### 🎰 Giao Diện Gacha & Bảng Tỷ Lệ / Kho Trang Bị (Loot Table)
- **Khắc phục triệt để lỗi hiển thị & Chống tràn khung bảng Tỷ Lệ (Rates & Pity Rules)**:
  - Khắc phục triệt để lỗi OpenGL Scissor clip rò rỉ khiến toàn bộ khung nền và cột Tỷ lệ bên trái bị ẩn; đảm bảo bảng Tỷ lệ và Quy tắc Bảo hiểm luôn hiển thị 100% rõ ràng, sắc nét.
  - Phân chia bố cục Modal `1080 x 580 px`: Cột Tỷ lệ bên trái rộng `310px`, có đường kẻ ngăn cách giữa Xác suất phẩm chất và Quy tắc Bảo hiểm (Pity).
  - Căn chỉnh thụt lề chuẩn, cỡ chữ `fontMedium` và `fontSmall` phân cấp rõ ràng, nội dung nằm trọn vẹn trong viền box, chấm dứt hoàn toàn hiện tượng tràn mép viền.
- **Đại tu Bảng Kho Trang Bị (Loot Table) theo đúng cấu trúc tiêu chuẩn**:
  - **Mỗi hàng 3 ô cho mỗi set**: Mỗi set hiển thị 3 ô card riêng biệt (`216 x 50 px`) cạnh nhau cho Cần Câu, Dây Cước, Lưỡi Câu. Trong từng ô hiển thị trực quan Vector Artwork nguyên bản (`EquipmentIconRenderer.drawSetPiece`), nhãn phân loại trang bị (`CẦN CÂU`, `DÂY CƯỚC`, `LƯỠI CÂU`) và tên món cụ thể.
  - **Dưới hàng 3 ô là Cấp Điểm & Set Bonus**:
    - Cấp điểm & Multiplier: `[+X Cấp Điểm • Hệ số: xY.YY]` (vàng hoàng kim).
    - Set bonus: `★ Set 3 món: ...` (xanh ngọc lục bảo) hiển thị chi tiết hiệu ứng kích hoạt bộ đồ.
  - **Hỗ trợ cuộn đa năng**: Hỗ trợ lướt xem mượt mà qua toàn bộ danh sách set bằng con lăn chuột (`scrolled`), nút bấm `▲`/`▼` và phím mũi tên Lên/Xuống.

---

## 📅 [2026-10-20] - Phiên bản v0.4.2 (Hải Tặc Viễn Dương & Dân Dã Ao Làng)

### 🏴‍☠️ Tạo Hình Trang Bị Vector (Artworks)
- **Nâng cấp Set Trang Bị Hải Tặc (Pirate Galleon Set)**:
  - Đồng nhất 100% giữa giao diện Trang Bị (`EquipmentScreen`) và Gacha (`GachaScreen` khi quay & xem chi tiết).
  - **Trụ Cột Buồm (Rod)**: Thân cột buồm gỗ sồi mun đen dày dặn với vân gỗ nổi, 4 đai kẹp đồng thau tán đinh vàng, cụm ròng rọc puli đồng thau nguyên khối quấn dây thừng bện gai, thanh giằng ngang treo lồng đèn bão bập bùng lửa ấm, đỉnh cột buồm gắn cờ Hải Tặc đuôi én đen phấp phới cùng phù hiệu đầu lâu xương chéo ngọc mắt đỏ.
  - **Xích Neo Nhỏ (Line)**: Bánh lái thuyền buồm cổ điển gỗ gụ chạm khắc 8 chấu tay cầm vươn dài bọc đồng; mỏ neo hạm đội bằng sắt rèn bắt chéo; 10 mắt xích sắt dày đặc đan lồng vòng quanh bánh lái với ánh kim thép sáng bóng; tâm trục gắn huy hiệu đồng vàng Tây Ban Nha khắc nổi đầu lâu mắt ngọc lục bảo.
  - **Lưỡi Hái Thép (Hook)**: Thân thép Damascus rèn nguội đen nhánh với ngạnh móc lật ngược hiểm hóc; rãnh huyết quang (fuller) chạy dọc sống lưng lưỡi câu rực sáng năng lượng lời nguyền hải tặc; quai bảo vệ tay kiểu chuôi kiếm hải tặc (cutlass basket hilt) bằng đồng thau chạm khắc; khoen treo dây chuyền gắn đồng tiền bạc Tây Ban Nha 8-reales đục lỗ và hạt bùa xương.

### 🐟 Tạo Hình Thủy Tộc (Fish Visuals)
- **Đại tu toàn diện Bộ Cá Dân Dã Ao Làng**:
  - Đồng nhất 100% hình ảnh hiển thị trên 3 phân hệ: **Bách Khoa Toàn Thư (`CatalogScreen`)**, **Mô hình bơi trong nước khi giằng co câu cá (`GameHUD`)**, và **Thông báo giỏ cá câu được bên thanh Sidebar (`GameHUD`)**.
  - **Cá Rô Đồng**: Dáng mình vạm vỡ màu đồng ánh rêu ô-liu với bụng vàng hoàng yến; vây lưng gai sắc nhọn 9 tia dựng đứng hiếu chiến; 5 vệt rằn ri cọp ngang thân; nắp mang có gai nhọn và vết đốm đen nhung viền vàng phát quang huyền thoại; mắt cam rực lửa kiên cường.
  - **Cá Diếc**: Dáng dẹt tròn lưng gù cao đặc trưng ao làng; vảy ánh bạc bạch kim óng ánh với viền ngọc trai; vây đuôi chẻ đôi mềm mại phớt hồng cam đào cùng vây lưng uốn lượn; mắt ngọc hổ phách sáng ngời.
  - **Cá Mương**: Thân hình ngư lôi thon dài khí động học; vệt sọc dọc phát quang màu ngọc bích / lục lam neon (emerald-turquoise) rực sáng từ sau mang đến cuống đuôi; lườn bụng trắng ngọc sắc lẹm như lưỡi dao; miệng hếch nhọn đón mồi tầng mặt.
  - **Cá Lóc Nhí**: Thân thon dài đầu dẹp săn mồi; vệt kẻ chỉ vàng cam rực lửa (golden racing stripe) kéo dài từ mắt suốt dọc thân; vệt rằn hổ đen rải rác trên sống lưng; vây lưng và vây hậu môn trải dài; đuôi quạt tròn có đốm mắt đen và vành viền cam lửa nổi bật.

### 📖 Sổ Tay Thủy Tộc (CatalogScreen)
- **Tối ưu hóa Giao Diện & Bố Cục Thẻ Bộ Sưu Tập**:
  - **Xóa bỏ dòng chữ `★ Rê chuột xem`**: Loại bỏ dòng gợi ý thừa ở đáy từng ô cá đã bắt, giúp bố cục thông tin cân đối, thanh thoát và không còn bị rối mắt.
  - **Dịch chuyển vị trí dòng đếm loài (`X/X Loài`)**: Đẩy dòng hiển thị tiến độ (ví dụ `4/4 Loài`, `3/3 Loài`) dịch lên trên (`cy - 25f`), nằm ngay ngắn trong dải tiêu đề thẻ bộ sưu tập thay vì nằm sát mép đáy thanh tiến trình.
  - **Chuẩn hóa thông số lực cá**: Đổi nhãn `❖ Kéo giằng: X Lực` thành `❖ Lực kéo: X (dmg/s)` khi rê chuột xem chi tiết cá trong popup tooltip, giúp chỉ số lực kéo rõ ràng, chuẩn cơ chế sát thương theo giây.

---

## 📅 [2026-10-20] - Phiên bản v0.4.1 (Giao diện & Mỹ thuật Nâng cao)

### 💎 Giao Diện & Biểu Tượng (UI / UX)
- **Đồng nhất Icon Kim Cương toàn hệ thống**:
  - Chuyển đổi toàn bộ hiển thị kim cương sang dạng **viên ngọc hình thoi thẳng đứng cổ điển** (Cyan Diamond Rhombus) với giác cắt đa cạnh, vầng sáng phát quang và điểm sáng phản chiếu (specular glint).
  - Áp dụng đồng bộ 100% tại `GameHUD` (Profile người chơi), `ShopScreen` (Dock góc trên), và `GachaScreen` (Dock tài nguyên & nút Đổi Vé).
- **Đồng nhất Icon Vé Quay Gacha**:
  - **Vé Quay Thường**: Dạng vé chữ nhật hoàng kim viền sáng, 2 ngàm khuyết hông và lỗ bấm đục tròn sẫm màu ở tâm.
  - **Vé Quay Cao Cấp**: Dạng vé chữ nhật hoàng gia tím viền sáng, 2 ngàm khuyết hông và tâm mặt trời hoàng kim phát sáng rực rỡ (khớp 100% với hiển thị tại Tiệm Đổi Quà của Event).
  - Tích hợp đồng nhất tại cả `GachaScreen` và `EventScreen`.
- **Căn giữa chuẩn xác Icon & Text trong các Box**:
  - **Dock tài nguyên GachaScreen**: Chia 3 ô đều nhau (115px/ô) với vạch ngăn mờ tinh tế; thuật toán tự động đo độ rộng chuỗi số và icon để đặt chính giữa từng ô.
  - **3 Nút hành động GachaScreen**: Khắc phục hiện tượng icon và text bị dạt lệch sang trái ở các nút `ĐỔI VÉ QUAY`, `QUAY 1 LƯỢT`, `QUAY 10 LƯỢT`; tự căn giữa hoàn hảo theo chiều ngang và dọc.
- **Tối ưu hóa Giao diện Sự Kiện (EventScreen)**:
  - Xóa bỏ dòng chữ `Hoàn thành 4/6 nhiệm vụ nhận: +1 Điểm SK, +500 Vàng, +20 KC` tại tab Nhiệm Vụ Sự Kiện.
  - Căn chỉnh thanh tiến trình và banner mốc thưởng rộng rãi, thoáng mắt.

### 🎨 Tạo Hình Trang Bị Vector (Artworks)
- **Nâng cấp Set Thời Không (Chrono / Space-Time Set)**:
  - **Trượng Thời Gian**: Thân trượng titan kép xoắn với ống dẫn năng lượng plasma lam, các đai bánh răng thời gian hoàng kim, đỉnh trượng chế tác hình **Thước đo sao & Bánh răng Thời Không (Astrolabe Chrono Gear)** gồm 8 răng cưa, mặt số vũ trụ sâu thẳm, quỹ đạo xoay, lõi kỳ dị tím/lam, kim đồng hồ bằng vàng và quả cầu đồng hồ cát pha lê trôi nổi trên đỉnh.
  - **Sợi Dây Xuyên Thấu**: Vầng hào quang bẻ cong không thời gian (Wormhole aura), guồng chỉ đồng thau nặng với 12 ngàm răng cưa cơ khí, cuộn chỉ tachyon đan xen sợi tơ lam - tím phát quang và lõi kỳ dị vành chân trời sự kiện.
  - **Lưỡi Câu Hư Không**: Thân lưỡi hắc diệp thạch vật chất tối với vết nứt không gian, chạm khắc cổ tự thời gian phát sáng dọc thân, ngạnh câu trăng khuyết mở rách chiều không gian, khoen đỉnh vương miện đính ngọc thời gian và dây lắc bụi sao.
- **Nâng cấp Set Đại Dương (Oceanus / Leviathan Set)**:
  - **Trượng Biển Khơi**: Hào quang xoáy nước biển sâu phát quang sinh học; thân trượng titan cobalt sâu thẳm với rãnh dẫn năng lượng plasma lam ngọc, quấn các đai san hô vàng; đầu trượng là **Đinh Ba Hải Thần (Neptune Wave Trident)** 3 mũi sóng uốn lượn sắc bén, bảo ngọc Trái Tim Đại Dương và ngọc trai biển khơi khổng lồ đặt trên đài sen vàng.
  - **Sợi Thủy Tinh**: Hào quang vực thẳm xoáy tròn; guồng chỉ chế tác từ vỏ ốc anh vũ viền đồng biển với 8 ngàm răng cưa sóng; sợi chỉ cáp quang thủy tinh xanh ngọc phát sáng nhấp nháy; trục tâm mang la bàn hoa tiêu vàng ôm trọn viên ngọc trai phát sáng.
  - **Nanh Thủy Quái**: Hào quang hàm quái thú đại dương; thân lưỡi câu tạc từ xương nanh thủy quái Leviathan bọc sống lưng titan cobalt, chạm khắc hải phù văn phát sáng dọc thân; mũi câu vuốt nhọn với ngạnh kép xé nước; khoen đỉnh đúc hình mỏ neo hoàng gia cùng dây lắc ngọc phát quang mồi nhử cá lồng đèn.
- **Nâng cấp Hiệu Ứng Quay Gacha**:
  - Khi thanh trượt quay thưởng chạy (`SPINNING`), hiển thị trực tiếp thẻ trang bị kính mờ bo góc, viền phát quang theo phẩm chất và **vẽ trực tiếp toàn bộ artwork vector chi tiết của từng món đồ** lướt qua dưới hồng tâm thay vì chỉ hiển thị text.

### 🌅 Màn Hình Chính (MainMenuScreen)
- **Đại tu toàn diện Background Chập Tối Bờ Biển (Twilight Seascape Vista)**:
  - Loại bỏ hoàn toàn hình vẽ hoạt họa cũ và texture `logo.png`.
  - Bầu trời gradient chuyển sắc từ xanh đêm viễn dương -> tím hoàng hôn -> vàng cam chân trời cùng các chòm sao lấp lánh.
  - Mặt trời hoàng hôn tỏa nhiều tầng hào quang dịu mắt, dãy núi đảo mờ sương xa xăm.
  - Ngọn hải đăng trên vách đá quét chùm đèn xoay 360° rọi sáng mặt biển theo thời gian thực.
  - Cột ánh sáng phản chiếu gợn sóng lấp lánh dọc theo đường nối từ mặt trời xuống bờ cát.
  - Bờ cát với sóng biển dập dềnh thở nhịp nhàng, cầu tàu gỗ cổ điển với dây thừng, bóng cần câu tựa nghiêng hướng lên bầu trời sao, đàn cá bơi lượn dưới làn nước và đốm sáng sinh học/tàn lửa biển bay lơ lửng.
- **Logo Typographic Đẳng Cấp**:
  - Dòng chữ **"ANGLER RPG"** phong cách typography hoàng kim với đổ bóng 3D nhiều lớp (multi-pass shadow), đế chữ hoàng kim rực rỡ và highlight sáng bóng.
  - Cánh hoa văn cần câu hoàng gia uốn lượn hai bên, gia huy mỏ neo - lưỡi câu vàng kép đối xứng gắn ngôi sao hoa tiêu dẫn đường phía trên.
  - Phụ đề trang nhã: `✦ HUYỀN THOẠI CẦN THỦ • THE WILD ODYSSEY ✦`.
  - Nút bấm `BẮT ĐẦU CÂU` & `THOÁT` thiết kế phiến đá sapphire viền vàng phát quang theo nhịp thở khi rê chuột.

---

## 📅 [2026-10-20] - Phiên bản v0.4.0 (Cân Bằng & Hệ Thống Cửa Hàng)

### ⚖️ Cân Bằng Vật Phẩm (Balance)
- **Nước tăng lực**: Hồi 50 thể lực cố định -> Hồi 25% thể lực tối đa.
- **Bình xịt đóng băng**: Thời gian đóng băng từ 3.5s -> 2.0s; giá điều chỉnh từ 300 Vàng -> 500 Vàng.
- **Trà thảo mộc**: Xóa bỏ trạng thái bất lợi (loại bỏ giảm tension và hồi thể lực); giá từ 250 Vàng -> 750 Vàng.
- **Đá mài lưỡi câu**: Tăng giá từ 200 Vàng -> 750 Vàng.
- **Nam châm kho báu**: Tăng giá từ 350 Vàng -> 800 Vàng.

### 🏪 Cửa Hàng & Tính Năng Mới (Shop & Inventory)
- **Giảm giá ngẫu nhiên (Flash Sale)**: Kích hoạt ngẫu nhiên sau một số lần câu nhất định, chọn 2 vật phẩm ngẫu nhiên giảm giá từ 10% - 50%.
- **Sách kỹ năng trùng lặp**: Cho phép bán lấy vàng hoặc quy đổi thành 1 Skill Point khi nâng cấp chiêu thức tương ứng.
- **Thông báo vật phẩm câu được (Notification Toast)**: Chuyển sang thanh thông báo side bar bên phải (phía trên quick slot), không làm gián đoạn quá trình câu cá.
