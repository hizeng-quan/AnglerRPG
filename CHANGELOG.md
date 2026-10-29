# 📜 NHẬT KÝ CẬP NHẬT (CHANGELOG) - ANGLER RPG







> File này được dùng để ghi nhận chi tiết toàn bộ các tính năng, nâng cấp giao diện, sửa lỗi và cân bằng game của **Angler RPG**.







---







## 📅 [2026-10-29] - Phiên bản v0.5.5.7 (Cân Bằng Phẩm Cấp Legendary & Limited Premium, Đồng Bộ Chỉ Số Trang Bị)

### 📖 Tinh Chỉnh Sách Kỹ Năng: Khóa Quy Đổi SP Khi Kỹ Năng Đạt Cấp Tối Đa (PlayScreen.java & GameHUD.java)
- **Cơ chế điều chỉnh:**
  - Trước đây, khi câu được hoặc sở hữu Sách Kỹ Năng mà kỹ năng tương ứng đã đạt Cấp Tối Đa (MAX), người chơi có thể bấm dùng trực tiếp từ Túi Đồ để quy đổi thành +1 Điểm Kỹ Năng (SP).
  - Điều này vô tình làm dư thừa điểm SP tự do trong cây kỹ năng và giảm giá trị giao thương của Sách.
- **Quy tắc mới:**
  - Nếu kỹ năng tương ứng đã đạt cấp MAX, sách **KHÔNG THỂ** sử dụng để cộng SP nữa.
  - Khi người chơi cố gắng nhấp hoặc bấm phím tắt dùng sách đã MAX, hệ thống sẽ phát âm thanh cảnh báo `sfx_fail` và hiển thị thông báo: *"Kỹ năng đã đạt Cấp Tối Đa! Hãy bán sách tại Cửa Hàng."*
  - Sách lúc này chỉ có mục đích sử dụng duy nhất là **đem bán tại Cửa Hàng** để thu về lượng Vàng lớn (từ 1,000 đến 2,500 Vàng tùy loại sách).
- **Cập nhật Giao diện (GameHUD & ItemDatabase):**
  - Huy hiệu góc trên sách đã Max chuyển từ `+SP` (xanh dương sáng) sang `MAX` (xám bạc tinh tế, không chớp nháy gây xao nhãng).
  - Tooltip mô tả hiển thị rõ cảnh báo đỏ: *"• ĐÃ MAX CẤP: Không thể dùng để cộng SP"* và hướng dẫn bán tại Cửa Hàng.

### 🐛 Sửa Lỗi Hitbox Nút Cửa Hàng (ShopScreen.java)
- **Hiện tượng & Báo cáo:** Người chơi phản ánh khi vào Cửa Hàng, các nút chuyển tab "Bí Kíp Kỹ Năng", "Đạo Cụ Đặc Biệt" và "Halloween" bị lệch hitbox nghiêm trọng; bấm vào một tab này lại nhảy sang tab khác hoặc không nhận click.
- **Nguyên nhân kỹ thuật (Root Cause):**
  - Trong `ShopScreen.java`, phần vẽ giao diện (Render) và xử lý hover tính toán tọa độ cho 5 tab với chiều rộng `tabW = 210f`, khoảng cách `15f`, `tabStartX = (1280 - (5*210 + 4*15)) / 2 = 85f`.
  - Tuy nhiên, phần xử lý click chuột `Gdx.input.isButtonJustPressed` lại bị kẹt công thức cũ thời điểm chỉ có 4 tab: `tabW = 260f`, `tabStartX = (1280 - (4*260 + 3*15)) / 2 = 97.5f`.
  - Sự sai lệch lớn giữa tọa độ hiển thị và tọa độ bắt click khiến tab 2 ("Bí Kíp Kỹ Năng"), tab 3 ("Đạo Cụ Đặc Biệt") và tab 4 ("Halloween") bị lệch hitbox từ 50px đến hơn 200px.
- **Giải pháp xử lý:**
  - Định nghĩa tập hằng số chuẩn hóa toàn cục ở cấp độ class: `TAB_W = 210f`, `TAB_H = 36f`, `TAB_GAP = 15f`, `TAB_START_X`, `TAB_Y`.
  - Dùng chung toàn bộ hằng số này cho cả 3 pha: Render hình khối, Render chữ và Bắt sự kiện click chuột, đảm bảo hitbox khớp pixel 100% với hiển thị thực tế.

### ⚖️ Cân Bằng Phẩm Cấp: Nerf Điểm Phẩm Cấp 5★ Legendary & 6★ Limited Premium (EquipmentGenerator.java)
- **Nguyên nhân điều chỉnh:**
  - Hệ số điểm phẩm cấp cũ của 5★ Legendary (15 điểm) và 6★ Limited Premium (18 điểm) khiến các chỉ số sát thương, thể lực và sức căng dây tăng vọt, gây lạm phát chỉ số và làm giảm sự gay cấn trong các trận đấu câu Boss.
- **Biện pháp cân bằng:**
  - **Nerf điểm phẩm cấp Legendary**: Giảm từ 15 điểm xuống **12 điểm** (-3 điểm).
  - **Nerf điểm phẩm cấp Limited Premium**: Giảm từ 18 điểm xuống **15 điểm** (-3 điểm).
- **Đồng bộ chỉ số công thức chuẩn (Standard Formula):**
  - Hệ số nhân phẩm cấp chuẩn $mult = \text{set.points} + \text{getRarityPoints(rarity)}$ giảm tương ứng 3 điểm trên toàn bộ trang bị Legendary và Limited Premium.
  - **Set 5★ Legendary Quang Minh ($mult = 7 + 12 = 19$, trước đây 22):**
    - Trượng Ánh Sáng: **48.0 DMG** (trước 54.0), **335.0 Thể lực** (trước 380.0).
    - Dây Chỉ Vàng: **575.0 Sức căng** (trước 650.0), **9.6 Kháng đứt** (trước 10.8).
    - Lưỡi Câu Thần: **24.0% Cá hiếm** (trước 27.0%), **48.0% Giữ mồi** (trước 54.0%).
  - **Set 5★ Legendary Thời Không / Hoàng Gia ($mult = 6 + 12 = 18$, trước đây 21):**
    - Cần: **46.0 DMG** (trước 52.0), **320.0 Thể lực** (trước 365.0).
    - Dây: **550.0 Sức căng** (trước 625.0), **9.2 Kháng đứt** (trước 10.4).
    - Móc: **23.0% Cá hiếm** (trước 26.0%), **46.0% Giữ mồi** (trước 52.0%).

### 🩸 Đại Tu Set Bonus Huyết Nguyệt Thần Vương: Bá Quyền Huyết Nguyệt (FishingManager.java, GameplayAbility.java, FishDatabase.java)
- **Loại bỏ thiên vị cá Halloween:**
  - Xóa bỏ hoàn toàn cơ chế tăng tỷ lệ gặp cá Halloween (+45% rare boost ẩn tại `PlayerEquipment` và +60% encounter tại `FishDatabase`) để set đồ trở thành thần khí thực chiến đỉnh cao ở mọi vùng biển.
- **Định danh & hoàn thiện 2 cơ chế độc quyền:**
  1. **Huyết Ma Thôn Phệ (Vampirism):**
     - Khi kéo dây (Space), hút 3% HP hiện tại của cá (tối đa 12.0 TL/s) chuyển hóa trực tiếp thành Thể lực cho cần thủ và gây sát thương tương ứng lên cá.
  2. **Huyết Nhãn Tinh Chuẩn (Độ chính xác + Máu - Chu kỳ 5s hiệu lực, 10s hồi chiêu tuần hoàn):**
     - Kích hoạt dựa theo kết quả quăng cần (Normal, Good, Perfect) và tự động hồi chiêu / tái kích hoạt tuần hoàn suốt trận câu:
       - **Normal (Chuẩn Xác):** Tăng khả năng hồi phục: Trần hút thể lực của Huyết Ma Thôn Phệ tăng lên 18.0 TL/s (+50%) và hồi phục thụ động thêm +6.0 Thể lực/giây.
       - **Good (Xuất Sắc):** Bao gồm toàn bộ hiệu ứng cấp Normal + **Giảm 30% Thể lực yêu cầu** khi thi triển tất cả các kỹ năng chủ động (GAS & Legacy).
       - **Perfect (Hoàn Hảo):** Bao gồm toàn bộ hiệu ứng cấp Good + Normal + **Gây thêm +25% Sát Thương Chuẩn (True Damage)** theo sát thương gốc trong mỗi đòn kéo dây và kỹ năng (xuyên thẳng máu thật, bỏ qua mọi lớp giáp và khiên).

### 👑 Đồng Bộ Chỉ Số 2 Set Thần Khí 6★ Limited Premium (EquipmentGenerator.java & SaveManager.java)
- Chuẩn hóa hệ số nhân điểm $mult = 8 + 15 = 23$ (giảm 3 điểm so với mốc $mult = 26$):
  - **Thần Khí Lạc Hồng (6★ Limited Premium - Thiên Hướng Phòng Thủ / Bền Bỉ):**
    - Trượng Lạc Hồng: **54.0 DMG**, **425.0 Thể lực** (vượt xa 335.0 Quang Minh).
    - Dây Rồng Thiêng: **705.0 Sức căng**, **11.8 Kháng đứt** (vượt xa 9.6 Quang Minh).
    - Móc Âu Cơ: **27.0% Cá hiếm**, **60.0% Giữ mồi tối thượng** (vượt xa 48.0% Quang Minh).
  - **Huyết Nguyệt Thần Vương (6★ Limited Premium - Thiên Hướng Tấn Công / Bộc Phá):**
    - Huyết Nguyệt Thần Trượng: **60.0 DMG** (sát thương bộc phá cao nhất game), **375.0 Thể lực** (vượt 335.0 Quang Minh).
    - Dây Huyết Ma Cơ: **645.0 Sức căng**, **10.6 Kháng đứt** (vượt 9.6 Quang Minh).
    - Lưỡi Huyết Nguyệt Ma Trảo: **31.0% Cá hiếm** (tỉ lệ cá hiếm cao nhất game), **52.0% Giữ mồi** (vượt 48.0% Quang Minh).
- **Nâng cấp `SAVE_VERSION = 7`:**
  - `SaveManager` tự động rà soát toàn bộ trang bị của người chơi (trong túi và đang mặc), đồng bộ hóa lại toàn bộ chỉ số sang bảng số liệu mới nhất.

---

## 📅 [2026-10-28] - Phiên bản v0.5.5.6 (Đại Tu Chỉ Số 2 Set 6★ Mult 26, Rework Long Thần Hộ Thể 12s & Sửa Lỗi Thiên Tượng Huyết Nguyệt)



### 👑 Đại Tu & Buff Toàn Diện 2 Bộ Trang Bị 6★ Thần Khí Limited Premium (EquipmentGenerator.java & SaveManager.java)

- **Nguyên nhân điều chỉnh:** Hai bộ trang bị 6★ `LIMITED_PREMIUM` (Thần Khí Lạc Hồng và Huyết Nguyệt Thần Vương) có hệ số điểm nhân phẩm cấp mult = 8 + 18 = 26, nhưng trước đây lại bị gắn cứng các thông số cũ (44 - 48 DMG, 450 - 520 Tension), thấp hơn cả mốc trang bị mult 17 và bộ 5★ Legendary Quang Minh (mult 22, 54 DMG / 650 Tension).

- **Quy chuẩn chỉ số mới theo mốc mult 26:**

  - **Thần Khí Lạc Hồng (6★ Limited Premium - Thiên Hướng Phòng Thủ / Sinh Tồn Đỉnh Cao):**

    - Trượng Lạc Hồng: **60.0 DMG** (vượt xa 5★ Quang Minh 54.0), **470.0 Thể lực** (thể lực dồi dào nhất game, vượt xa 380.0 Quang Minh).

    - Dây Rồng Thiêng: **780.0 Sức căng** (áp đảo hoàn toàn, 780 > 650 Quang Minh), **13.0 Kháng đứt** (siêu việt, 13.0 > 10.8 Quang Minh).

    - Móc Âu Cơ: **30.0% Cá hiếm**, **66.0% Giữ mồi tối thượng** (vượt xa 54.0% Quang Minh).

  - **Huyết Nguyệt Thần Vương (6★ Limited Premium - Thiên Hướng Tấn Công / Sát Thương Bộc Phá Đỉnh Cao):**

    - Huyết Nguyệt Thần Trượng: **66.0 DMG** (sát thương bộc phá cao nhất toàn game), **420.0 Thể lực** (dồi dào, vượt xa 380.0 Quang Minh).

    - Dây Huyết Ma Cơ: **720.0 Sức căng** (vượt xa 650.0 Quang Minh), **11.8 Kháng đứt** (vượt xa 10.8 Quang Minh).

    - Lưỡi Huyết Nguyệt Ma Trảo: **34.0% Cá hiếm** (tỉ lệ cá hiếm cao nhất game), **58.0% Giữ mồi** (vượt 54.0% Quang Minh).

  - **Nâng cấp `SAVE_VERSION = 6`:** Tự động di chuyển dữ liệu và cập nhật toàn bộ trang bị Lạc Hồng và Huyết Nguyệt trong túi đồ / trang bị đang mặc của người chơi lên bảng chỉ số mới ngay khi khởi động game.



### 🐉 Rework Nội Tại Set Thần Khí Lạc Hồng (FishingManager.java & EquipmentGenerator.java)

- **Xóa Âm Vang Trống Đồng:** Loại bỏ hoàn toàn hiệu ứng giảm 25% gia tăng Tension theo yêu cầu.

- **Long Thần Hộ Thể (12 Giây):** Khi rơi vào trạng thái nguy cấp (Lực căng dây $\ge 85\%$ hoặc Thể lực $\le 25\%$), hào quang Long Thần kích hoạt:

  - Khóa Tension không tăng trong 2.0s.

  - Giảm **50% tiêu hao Thể lực trong 12.0s** (thay vì 3.0s trước đây).

  - Hồi chiêu: 30s.

- **Giữ nguyên:** 40% tỉ lệ hóa giải kỹ năng thường của Boss và hồi phục 10% Thể lực tối đa.



### 🌕 Sửa Lỗi Hiển Thị Buff Bầu Trời Huyết Nguyệt & Đại Tu Thiên Tượng (PlayScreen.java & EventManager.java)

- **Nguyên nhân lỗi:**

  - `HALLOWEEN_START_TIMESTAMP` bị cấu hình sớm khiến game nhận nhầm sự kiện Halloween đã bắt đầu trước ngày 30/10.

  - Buff Bầu Trời Huyết Nguyệt tự động kích hoạt vào ban đêm dù sự kiện chưa đến ngày; đồng thời trong trạng thái IDLE (bến câu) không hề có sự chuyển biến thiên tượng bầu trời, gây mâu thuẫn giữa thông báo và hình ảnh.

- **Biện pháp khắc phục:**

  - **Chặn kích hoạt trước ngày:** Cập nhật `HALLOWEEN_START_TIMESTAMP` về đúng **00:00 ngày 30/10/2026**. Sự kiện hiện tại là Lạc Hồng (kéo dài đến 23:59:59 ngày 29/10). Bầu Trời Huyết Nguyệt tuyệt đối không kích hoạt trước ngày 30/10.

  - **Đại tu chuyển sắc thiên tượng toàn cảnh:**

    - Cập nhật hiệu ứng thiên tượng áp dụng đồng bộ cho cả bến câu IDLE lẫn trong trận đấu câu.

    - Khi Huyết Nguyệt giáng trần: Bầu trời ngả sang sắc đỏ rượu gothic u uất, Siêu Mặt Trăng Máu (Blood Moon Eclipse) xuất hiện với quầng nhật hoa rực cháy, tia sáng tà ác, bóng nguyệt thực đen và 20 đốm tàn tro huyết hỏa bay lượn từ mặt nước lên không trung.

    - Đồng bộ hiển thị buff trong `castBuffList`: `"THIÊN TƯỢNG HUYẾT NGUYỆT: +20% Vàng, +20% EXP, x2 Cá Halloween"`.



---



## 📅 [2026-10-28] - Phiên bản v0.5.5.5 (Cân Bằng Nội Tại Thần Khí Lạc Hồng, Cơ Chế Kiệt Sức Thoát Cá & Khắc Phục Triệt Để Auto-Updater)







### 🐉 Cân Bằng & Rework Nội Tại Set Thần Khí Lạc Hồng (FishingManager.java & EquipmentGenerator.java)



- **Nguyên nhân điều chỉnh:** Hiệu ứng cũ miễn nhiễm 100% tất cả kỹ năng Boss quá mạnh, làm mất đi tính thử thách của các trận đấu Boss (nhất là Thanatos Vực Thẳm và Cá Cờ Cuồng Phong).



- **Cơ chế cân bằng mới:**



  - **Khắc Chế Kỹ Năng Boss (40% Tỉ Lệ):** Bộ 3 món Thần Khí Lạc Hồng có 40% tỉ lệ vô hiệu hóa kỹ năng thường/cooldown của Boss (như Âm Hồn Quấy Phá, Vực Xoáy Âm Ty, Tiếng Hú Địa Ngục, Độc Tố Âm Ty, Mãng Xà Siết Cước, Tiếng Than U Ám, Bứt Tốc, Quẫy Bùn, v.v.).



  - **Miễn Nhiễm Với Tuyệt Kỹ (Ultimate):** Các tuyệt kỹ mang tính quyết định của Boss như `cuong_no` (Cuồng Nộ Bá Chủ), `lien_hoan_tram_doat_menh` (Thanatos 3 Nhịp Re-hook), và `cuong_phong_ngu_kiem` (Cá Cờ Thần) **KHÔNG THỂ** bị vô hiệu hóa, người chơi bắt buộc phải dựa vào kỹ năng điều cần để vượt qua.



  - **Hồi Phục Thể Lực (+10% TL):** Khi khắc chế thành công kỹ năng Boss, cần thủ lập tức hồi phục 10% Thể lực tối đa (thay thế mức hồi phục tức thời 25% trước đây).



  - **Âm Vang Trống Đồng (-25% Tension):** Toàn bộ tốc độ gia tăng lực căng dây (Tension) trong quá trình kéo cá bị giảm 25% liên tục.



  - **Long Thần Hộ Thể (Kỹ Năng Nguy Cấp):** Khi rơi vào tình huống nguy hiểm (Lực căng dây $\ge 85\%$ hoặc Thể lực $\le 25\%$), hào quang Long Thần tự động thức tỉnh: Khóa cứng Tension không tăng trong 2.0s và giảm 50% tiêu hao Thể lực trong 3.0s (Hồi chiêu nội tại: 30s).







### 🏃 Cơ Chế Kiệt Sức & Cá Thoát Khi Hết Thể Lực (PlayerCombat.java & FishingManager.java)



- **Nguyên nhân lỗi:** Phương thức `consumeStamina(amount)` trong `PlayerCombat.java` trước đây trả về `false` và không trừ thể lực nếu `currentStamina < amount`. Do đó, khi thể lực chỉ còn 0.2 - 0.5 mà tốc độ rút theo khung hình lớn hơn, thể lực bị kẹt lơ lửng và không bao giờ giảm về `<= 0`, khiến cá không thể bơi thoát khi người chơi kiệt sức.



- **Biện pháp khắc phục:**



  - Bổ sung phương thức `player.drainStamina(float amount)` kẹp cứng thể lực về `0f` cho tất cả nguồn tiêu hao liên tục và sát thương từ cá.



  - Khi `player.getCurrentStamina() <= 0f`, trận đấu câu lập tức kết thúc thất bại với thông báo: `"HẾT THỂ LỰC! BẠN ĐÃ KIỆT SỨC VÀ CÁ ĐÃ BƠI THOÁT!"`.



  - Hiển thị dòng chữ nổi màu đỏ cam: `"KIỆT SỨC! CÁ CHẠY MẤT!"` và phát hiệu ứng âm thanh thất bại `sfx_fail`.







### 🎃 Sửa Triệt Để Lỗi Không Reset Nhiệm Vụ Sự Kiện Halloween (EventManager.java & EventScreen.java)



- **Nguyên nhân lỗi:** Thời gian bắt đầu sự kiện `HALLOWEEN_START_TIMESTAMP` bị cấu hình chậm hơn bản build game dẫn đến rơi vào giai đoạn đệm không kích hoạt nhiệm vụ; đồng thời logic so sánh mốc ngày cũ gặp lỗi khi lệch múi giờ hoặc trôi NTP.



- **Biện pháp khắc phục:**



  - Đồng bộ mốc thời gian sự kiện Halloween và chuyển sang so sánh theo ngày trong năm (`Calendar.DAY_OF_YEAR`), bảo đảm nhiệm vụ hằng ngày tự động làm mới chính xác lúc 00:00.



  - Tự động gọi kiểm tra và kích hoạt làm mới nhiệm vụ mỗi khi người chơi mở giao diện Sự Kiện (`EventScreen.show()`).



  - Bổ sung bộ đếm ngược thời gian còn lại đến 00:00 trực quan ngay trên tiêu đề màn hình Sự Kiện: `LÀM MỚI LÚC 00:00 (CÒN ...)`







### 🛠️ Nâng Cấp Hệ Thống Auto-Updater & Migration Save V5 (AutoUpdater.java & SaveManager.java)



- **Nguyên nhân lỗi:**



  - Auto-updater trước đây tải tệp nén `AnglerRPG-v0.5.5.2.zip` từ GitHub Release rồi trực tiếp chạy lệnh `java -jar AnglerRPG-v0.5.5.2.zip`, dẫn đến lỗi của JVM: `"Error: Invalid or corrupt jarfile"`.



  - Batch script tự xóa `%~f0` khi cmd đang đọc lệnh gây lỗi `"The batch file cannot be found."`.



- **Biện pháp khắc phục:**



  - Auto-updater tự động nhận diện gói ZIP, tự trích xuất tệp `AnglerRPG.jar` bên trong ra tệp tạm `AnglerRPG-extracted.tmp`, sau đó ghi đè an toàn vào `AnglerRPG.jar`.



  - Tối ưu hóa kịch bản cập nhật với tiến trình nền tách biệt (`start /b "" cmd /c del update_apply.bat`), xóa sạch tệp rác và khởi động lại game không còn báo lỗi.



  - Nâng cấp `SAVE_VERSION = 5` trong `SaveManager.java`: Tự động kiểm tra và nâng cấp toàn bộ trang bị cũ của người chơi lên chuẩn phẩm cấp và hệ số chỉ số mới nhất (Set Quang Minh 5★ 54 DMG / 380 Stamina, Dạ Hội Bí Ngô Epic Tier 5, Huyết Nguyệt Thần Vương vs Lạc Hồng).







---







## 📅 [2026-10-28] - Phiên bản v0.5.5.4 (Cân Bằng Toàn Diện Trang Bị, Khắc Phục Powercreep & Sửa Lỗi Set Quang Minh)







### 🌟 Chuẩn Hóa Phẩm Cấp & Khôi Phục Sức Mạnh Set Quang Minh (EquipmentGenerator.java & SaveManager.java)



- **Nguyên nhân lỗi:**



  - Khi người chơi lệch rate 50/50 ở Banner Giới Hạn và trúng các set off-banner ("Quang Minh", "Thời Không", "Hoàng Gia"), hệ thống vô tình gán phẩm cấp của trang bị thành `LIMITED_PREMIUM` (6★) thay vì giữ đúng chuẩn 5★ `LEGENDARY`.



  - Đồng thời, công thức tính chỉ số trong `EquipmentGenerator.java` từng bị giảm hệ số nhân từ `2.0f` DMG / `15.0f` Stamina xuống `1.2f` / `8.0f` và điểm độ hiếm LEGENDARY bị giảm từ 15 về 10, trong khi các trang bị cũ trong kho người chơi (như `Trượng Thời Gian` RARE) vẫn giữ chỉ số gốc (+32 DMG, +215 Stamina). Hậu quả là `Trượng Ánh Sáng` LEGENDARY mới quay chỉ đạt +30 DMG / +186 Stamina, yếu hơn cả trang bị 3★ RARE!



- **Biện pháp khắc phục:**



  - **Chuẩn hóa phẩm cấp:** Khi roll Gacha hoặc lệch rate ở banner Limited, các trang bị tiêu chuẩn ("Quang Minh", "Thời Không", "Hoàng Gia") được bảo đảm nghiêm ngặt 100% ở phẩm cấp 5★ `LEGENDARY`, tuyệt đối không bị gán nhầm thành `LIMITED_PREMIUM`.



  - **Khôi phục hệ số nhân chuẩn:** Khôi phục `getRarityPoints(LEGENDARY) = 15` và công thức chỉ số:



    - Cần câu: `10f + mult * 2.0f` (Base Damage), `50f + mult * 15.0f` (Max Stamina).



    - Dây cước: `100f + mult * 25.0f` (Max Tension), `2f + mult * 0.4f` (Kháng đứt).



    - Lưỡi câu: `5f + mult * 1.0f` (% Cá hiếm), `10f + mult * 2.0f` (% Giữ mồi).



  - **Chỉ số Set Quang Minh (5★ Legendary, mult = 7 + 15 = 22):**



    - Trượng Ánh Sáng: **54.0 DMG**, **380.0 Stamina** (vượt trội hoàn toàn so với Rare Trượng Thời Gian 32 DMG).



    - Dây Chỉ Vàng: **650.0 Tension**, **10.8 Kháng đứt**.



    - Lưỡi Câu Thần: **27.0% Cá hiếm**, **54.0% Giữ mồi**.







### 🎃 Cân Bằng Set Dạ Hội Bí Ngô Ngang Hàng Epic Tier 5 (EquipmentGenerator.java)



- **Nguyên nhân điều chỉnh:** Set sự kiện Dạ Hội Bí Ngô (4★ Limited Event) cần được cân bằng hợp lý để giữ đúng vai trò trang bị cày cuốc sự kiện miễn phí, không lấn át các trang bị cao cấp khác.



- **Biện pháp khắc phục:**



  - Quy chuẩn toàn bộ chỉ số của Set Dạ Hội Bí Ngô về đúng mốc tương đương một Set Epic Tier 5 (Set Vực Thẳm Epic, mult = 5 + 9 = 14):



    - Cần Dạ Hội Bí Ngô: **38.0 DMG**, **260.0 Stamina**.



    - Dây Cước Tơ Nhện Quỷ: **450.0 Tension**, **7.6 Kháng đứt**.



    - Lưỡi Kẹo Ma Quái: **19.0% Cá hiếm**, **38.0% Giữ mồi**.



  - Giữ nguyên hiệu ứng nội tại độc quyền Halloween "Lễ Hội Kẹo Ngọt": +35% EXP, +25% Vàng và 35% tỉ lệ rơi thêm Kẹo Trick-or-Treat khi câu cá.







### 🩸 Cân Bằng Set Huyết Nguyệt Thần Vương vs Set Thần Khí Lạc Hồng (EquipmentGenerator.java)



- **Nguyên nhân điều chỉnh:** Set Huyết Nguyệt Thần Vương trước đây sở hữu Sức căng 520.0 và Kháng đứt 8.5 cùng Sát thương 48.0, gây mất cân bằng nghiêm trọng (powercreep) so với set biểu tượng 6★ Thần Khí Lạc Hồng.



- **Biện pháp khắc phục:**



  - Tái phân hóa vai trò đối trọng công - thủ giữa 2 bộ trang bị 6★ Thần Khí đỉnh cao:



    - **Thần Khí Lạc Hồng (Thiên hướng Sinh Tồn / Phòng Thủ):**



      - Sát thương: **44.0 DMG**



      - Thể lực cần: **320.0 Stamina** (cao hơn Huyết Nguyệt: 320 > 280)



      - Sức căng dây: **520.0 Tension** (cao hơn Huyết Nguyệt: 520 > 450)



      - Kháng đứt dây: **8.8 Kháng đứt** (cao hơn Huyết Nguyệt: 8.8 > 7.2)



      - Móc Âu Cơ: **20.0% Cá hiếm**, **45.0% Giữ mồi**



      - Nội tại: Miễn nhiễm mọi hiệu ứng kỹ năng của Boss, hồi phục 25% Thể lực.



    - **Huyết Nguyệt Thần Vương (Thiên hướng Bộc Phá / Tấn Công):**



      - Sát thương: **48.0 DMG** (cao hơn Lạc Hồng: 48 > 44)



      - Thể lực cần: **280.0 Stamina** (thấp hơn Lạc Hồng: 280 < 320)



      - Sức căng dây: **450.0 Tension** (nerf từ 520 xuống 450, thấp hơn Lạc Hồng: 450 < 520)



      - Kháng đứt dây: **7.2 Kháng đứt** (nerf từ 8.5 xuống 7.2, thấp hơn Lạc Hồng: 7.2 < 8.8)



      - Lưỡi Huyết Nguyệt Ma Trảo: **22.0% Cá hiếm**, **42.0% Giữ mồi**



      - Nội tại: Quăng cần Perfect x2 DMG trong 2s đầu; kéo cá hút 3% HP cá thành Thể lực (tối đa 12/s).







### 💾 Tự Động Nâng Cấp Save Version 4 (SaveManager.java)



- Tăng `SAVE_VERSION` lên **4**.



- Bổ sung logic tự động di chuyển dữ liệu (Data Migration) khi người chơi tải game từ các phiên bản cũ (`savedVersion < 4`):



  - Tự động sửa lại phẩm cấp các món Quang Minh, Thời Không, Hoàng Gia bị nhầm `LIMITED_PREMIUM` về đúng `LEGENDARY`.



  - Tự động tính toán lại toàn bộ chỉ số của tất cả trang bị trong túi đồ người chơi theo công thức chuẩn mới, đảm bảo tính nhất quán và công bằng tuyệt đối.







---







## 📅 [2026-10-28] - Phiên bản v0.5.5.3 (Đồng Bộ Phiên Bản, Sửa Lỗi Tự Động Cập Nhật & Chuẩn Hóa Mã Hóa Reset Save)







### 🏷️ Đồng Bộ Hóa Phiên Bản Hệ Thống (GameVersion.java & PatchNotesDialog.java)



- **Nguyên nhân lỗi:** Trước đây `GameVersion.CURRENT_VERSION` bị giữ nguyên ở giá trị cũ `0.5.2.4`, trong khi `PatchNotesDialog.java` và bản phát hành đã ở `v0.5.5.2`. Điều này khiến:



  - Menu chính và Hộp thoại cập nhật luôn hiển thị phiên bản cũ `v0.5.2.4`.



  - Trình kiểm tra cập nhật `AutoUpdater` so sánh nhầm và liên tục hiển thị huy hiệu thông báo "CÓ BẢN MỚI" dù người chơi đã ở bản mới nhất.



- **Biện pháp khắc phục:**



  - Cập nhật đồng bộ `GameVersion.CURRENT_VERSION` lên `0.5.5.3`, ngày phát hành `2026-10-28`.



  - Đồng bộ `PatchNotesDialog.CURRENT_VERSION` lên `v0.5.5.3`.



  - Trình AutoUpdater so khớp chính xác phiên bản cục bộ và từ xa, triệt tiêu thông báo cập nhật sai lệch.







### 🔄 Đại Tu Trình Tự Động Cập Nhật (AutoUpdater.java)



- **Khắc phục lỗi "Invalid or corrupt jarfile .zip":**



  - **Nguyên nhân:** GitHub Releases đính kèm gói cập nhật dưới dạng `.zip` (bên trong chứa `AnglerRPG.jar`), nhưng `AutoUpdater` cũ tải về và gọi trực tiếp `java -jar AnglerRPG-*.zip`, khiến JVM báo lỗi file corrupt.



  - **Giải pháp:** Bổ sung cơ chế tự động nhận diện gói nén ZIP (`isZipArchive()`) và trích xuất file `.jar` bên trong ra `AnglerRPG-extracted.tmp` bằng luồng `ZipInputStream` chuẩn thư viện Java trước khi thực thi script thay thế.



  - **Ưu tiên Asset:** Tự động ưu tiên tải file `.jar` trực tiếp nếu có trên Release, sau đó mới đến `.zip` hoặc `.exe`.



- **Khắc phục lỗi "The batch file cannot be found" & Xung đột khóa file (File Lock):**



  - **Nguyên nhân:** Lệnh tự xóa cũ `del "%~f0"` trên Windows batch khiến tiến trình `cmd.exe` bị mất file khi đọc đến lệnh kế tiếp (`exit`), gây ra thông báo lỗi `The batch file cannot be found.` Ngoài ra, nếu JVM cũ chưa kịp đóng xong trong 1 giây, lệnh `move` đơn lẻ sẽ thất bại do bị khóa file.



  - **Giải pháp:** 



    - Đổi cú pháp tự xóa batch sang `(goto) 2>nul & del "%~f0"`, đóng `cmd.exe` êm ái mà không để lại thông báo lỗi.



    - Bổ sung vòng lặp retry 10 lần (10 giây) cho lệnh `move` để chờ tiến trình game cũ giải phóng khóa file hoàn toàn trước khi hoán đổi tệp JAR mới.



    - Tự động nhận diện chính xác tên file JAR đang chạy của game (`targetJarName`) thay vì cố định tên tệp tải về.



    - Hỗ trợ đa tầng HTTP Redirect (301/302/307/308, tối đa 5 lần) từ GitHub Releases sang AWS S3 CDN.







### 🛡️ Chuẩn Hóa Mã Hóa Toàn Diện Khi Reset Game (SaveManager.java & SettingsDialog.java)



- **Nguyên nhân lỗi:** Khi người chơi thực hiện "Đặt Lại Dữ Liệu Trò Chơi (Reset Game)" trong Cài đặt, hệ thống cũ xuất file sao lưu mang tên `angler_save_before_reset.json` với phần mở rộng `.json`.



- **Biện pháp khắc phục:**



  - Chuẩn hóa việc xuất file sao lưu trước khi reset thành định dạng mã hóa an toàn: `angler_save_before_reset_<timestamp>.sav` và `angler_save_before_reset.sav`.



  - Cả 2 file đều được mã hóa bằng chuẩn AES-256/CBC/PKCS5Padding kết hợp chữ ký toàn vẹn SHA-256 (tiền tố `ANGLER_SECURE_SAVE_v1:`).



  - Khóa chặt phương thức `SaveManager.exportToFile()`: Tự động kiểm tra tính hợp lệ của chuỗi mã hóa trước khi ghi ra đĩa; tuyệt đối không bao giờ xuất file chưa mã hóa nếu có lỗi phát sinh.



  - Bổ sung `angler_save_before_reset.sav` vào danh sách fallback tự động khi nạp lại dữ liệu trong Settings.











## 📅 [2026-10-28] - Phiên bản v0.5.5.2 (Đại Tu Đồ Họa Procedural Bản Đồ Đảo Xa - Khí Quyển Biển Khơi Sống Động)







### 🌊 Đại Tu Toàn Diện Background Bản Đồ Đảo Xa (DaoXaBackground.java & PlayScreen.java)



- **Khắc Phục Nền Đen Placeholder Cũ:**



  - Thay thế khối hình tròn đen tạm bợ trước đây bằng bộ kết xuất đồ họa procedural chuyên biệt `DaoXaBackground.java` chạy trên nền LibGDX ShapeRenderer.



  - Tối ưu hóa 100% không cấp phát bộ nhớ (Zero Allocation) trong vòng lặp `render()`, duy trì 60 FPS ổn định tuyệt đối.



- **Bầu Trời Đêm Vũ Trụ & Trăng Khuyết Đại Dương:**



  - Chuyển sắc 16 tầng mượt mà từ sắc đen vũ trụ (#02050E) đến sắc chàm chân trời biển (#0E1A32).



  - Biển sao 35 tinh tú nhấp nháy theo hàm sin với đa sắc thái (băng thanh cyan, vàng nhạt, bạc kim).



  - Mặt Trăng Lưỡi Liềm sắc sảo với 3 tầng hào quang khuếch tán cùng cột ánh trăng lung linh rọi dọc mặt sóng biển.



- **Quần Thể Đảo Đá Basalt & Ngôi Nhà Người Gác Đèn Biển:**



  - Dãy đảo đá xa mờ ảo nhô lên khỏi làn sương đêm chân trời; cụm tháp đá basalt cận cảnh với các diện cắt đa giác bắt sáng ánh trăng và rừng tùng biển phong ba lộng gió.



  - Ngôi nhà đá của người gác đèn với ống khói nhả khói nhẹ và cửa sổ bừng sáng ánh lửa lò sưởi ấm cúng giữa biển đêm.



- **Ngọn Hải Đăng Cổ Điển & Chùm Sáng Quét Xoay 360°:**



  - Tháp hải đăng sọc đỏ/trắng kinh điển, phòng đèn kính rực rỡ và chùm sáng quét xoay chu kỳ 360° chiếu rọi mặt biển với hiệu ứng lóe sáng Lens Flare khi tia sáng chiếu thẳng về phía camera.



- **Sóng Ngầm Đại Dương, Bọt Sóng Xô Bờ & Sinh Vật Biển Khơi:**



  - 4 tầng sóng ngầm cuộn nhịp nhàng với viền bọt sóng lấp lánh; bọt sóng trắng xóa đập vào chân vách đá bắn bọt khí và bụi nước li ti.



  - Hệ sinh thái biển sống động: Bóng Cá Cờ Xanh (Blue Marlin) rẽ sóng với vây buồm và mũi kiếm dài, đàn cá biển khơi bơi nhanh, vây cá mập rạn tuần tra, 2 cá thể sứa phát quang sinh học (cyan & tím) cùng bụi dạ quang Noctiluca bềnh bồng dưới đáy vực.







---







## 📅 [2026-10-28] - Phiên bản v0.5.5.1 (Giai Đoạn 6: Hoàn Tất Kiểm Thử Cân Bằng CLI, An Toàn Dữ Liệu Save & Đóng Gói Phát Hành)







### 📊 Suite Kiểm Thử Cân Bằng Game CLI & Mô Phỏng Monte Carlo (balance_cli.py)



- **Đại Chiến Siêu Boss Kình Ngư Tử Thần Thanatos (6,666 HP) vs Set 6★ Huyết Nguyệt Thần Vương:**



  - Mô phỏng 500 lượt trận độc lập tick-by-tick (0.05s) tích hợp đầy đủ hệ thống kỹ năng GAS, nội tại Set Huyết Nguyệt (Vampirism 12 TL/s, Perfect Cast x2 damage) và cơ chế Re-Hook Liên Hoàn Trảm.



  - **Tỷ lệ thắng (Win Rate) đạt chuẩn:** **85.2%** (ngưỡng mục tiêu thiết kế 70% - 85%).



  - **Thời gian kéo trung bình (Mean TTK):** **73.16s** (±18.24s) (nằm trọn vẹn trong khoảng chuẩn 45s đến 75s cho trải nghiệm Boss thế giới đỉnh cao).



  - Tỷ lệ đứt dây (Line Snap) tối ưu: chỉ 0.6% nhờ tính năng bảo hiểm của Nến Hồn Ma Bảo Hộ.



- **Mô Phỏng Kinh Tế 28 Ngày Bằng Mesa Framework (500 Agents):**



  - Đảm bảo kinh tế ổn định vững chắc trên cả 3 nhóm người chơi (Casual, Grinder, Hardcore).



  - Lượng Vàng và Kim Cương tích lũy tăng trưởng bền vững, không gây lạm phát kinh tế sau 28 ngày diễn ra sự kiện.







### 🛡️ Sửa Lỗi Đồng Bộ Dữ Liệu Save & An Toàn Bộ Nhớ Native (SaveManager.java)



- **Khắc phục lỗi thiếu 10 trường Buff khi nạp game:** Trước đây `SaveManager.loadPlayer()` chỉ phục hồi 6 loại buff cơ bản, bỏ sót các thuộc tính buff nâng cao. Đã bổ sung khôi phục đầy đủ:



  - Thời gian hiệu lực Kẹo Trick-or-Treat (`halloweenCandyTimer`) và số lượng Kẹo.



  - Số lần bảo hộ của Đèn Lồng Bí Ngô (`pumpkinLanternCharges`).



  - Số lần miễn phí Thể lực của Độc Dược Ma Quái (`freeStaminaSkillCharges`).



  - Thời gian buff Sát Thương Chuẩn Bùa Huyết Nguyệt (`bloodMoonTalismanTimer`) và gọi Huyết Nguyệt nhân tạo (`manualBloodMoonTimer`).



  - Trạng thái kích hoạt của Nến Hồn Ma Bảo Hộ (`ghostCandleActive`).



  - Số lần sử dụng Đá Mài (`whetstoneCharges`), Nam Châm Kho Báu (`treasureMagnetCharges`) và Sonar Thấu Thị (`sonarCharges`).



- **Mã Hóa & Kiểm Tra Toàn Vẹn AES-256 + SHA-256:** Bảo vệ 100% dữ liệu xuất/nạp `.sav` chống can thiệp và gian lận.







### 📦 Đóng Gói Phát Hành & Đồng Bộ Tài Liệu



- Hoàn thành đầy đủ bộ 3 tài liệu bắt buộc: `CHANGELOG.md`, `TASKS.md`, `PatchNotesDialog.java`.



- Biên dịch độc lập `gradlew lwjgl3:jar` xuất bản bản build phát hành Halloween hoàn chỉnh.







---







## 📅 [2026-10-28] - Phiên bản v0.5.5.0 (Giai Đoạn 5: Đạo Cụ Tiêu Hao, Bầu Trời Huyết Nguyệt, 2 Cửa Hàng & Bộ 27 Âm Thanh ZzFX)







### 🎒 Hệ Thống 5 Đạo Cụ Tiêu Hao Halloween (ItemDatabase, PlayerBuffs, Player, GameplayAbility)



- **Chuẩn Hóa Giá Bán Gấp Đôi (Vàng) & Vector Art Chuyên Biệt:**



  - **Kẹo Trick-or-Treat (300 Vàng):** Ăn kẹo ma thuật tăng +50% tỷ lệ bắt gặp cá Halloween và cá hiếm trong 15 phút thời gian thực (900s). Vector art kẹo xoắn cam/tím bí ngô.



  - **Đèn Lồng Bí Ngô (700 Vàng):** Kích hoạt hiệu ứng *Quang Minh Trừ Tà*, tăng **+30% thời gian cửa sổ phản ứng Re-Hook** khi cá nhảy/lướt tiếp nước (3 lần). Vector art bí ngô phát sáng Jack-o' chạm khắc mắt mũi rực rỡ.



  - **Độc Dược Ma Quái (900 Vàng):** 3 lần tung kỹ năng chủ động tiếp theo tiêu hao **0 Thể lực** (tự động giải trừ sau khi kết thúc trận câu). Vector art bình độc dược thủy tinh sủi bọt khí xanh neon.



  - **Bùa Huyết Nguyệt (1000 Vàng):**



    - *Trong lúc câu:* Gây thêm **+10% sát thương từ mọi nguồn dưới dạng Sát Thương Chuẩn (True Damage)** xuyên thẳng vào HP thật của cá trong 15 giây.



    - *Trước trận câu:* Kích hoạt tức thì chu kỳ **Bầu Trời Huyết Nguyệt 15 phút** (900s) nhân tạo. Vector art lá bùa giấy da cổ khắc ấn chu sa đỏ.



  - **Nến Hồn Ma Bảo Hộ (1200 Vàng):** Bảo hiểm đứt cước 1 lần/trận; khi Tension chạm ngưỡng 100% hoặc trúng Lưỡi Hái Thanatos, nến sẽ bùng cháy cứu đứt dây, kéo Tension lập tức về 50%. Vector art cây nến sáp trắng với ngọn lửa xanh lam u linh.







### 🌌 Khí Quyển Bầu Trời Huyết Nguyệt Chu Kỳ 15 Phút & Chuyển Sắc Êm Đềm (PlayScreen.java)



- **Chu Kỳ Tự Động Đối Xứng:** Kích hoạt từ 18:00 đêm đến 06:00 sáng; mỗi 15 phút bật / 15 phút tắt luân phiên (phút 00-15: BẬT, 15-30: TẮT, 30-45: BẬT, 45-60: TẮT).



- **Chuyển Sắc Êm Ái 3.0 Giây (`Interpolation.fade`):** Nếu đang trong trận câu mà chạm mốc thời gian, toàn bộ bầu trời chuyển dần sang sắc đỏ rượu Gothic với trăng máu khổng lồ và tàn lửa bay lượn mà không làm gián đoạn hay giật cục trận câu; kèm notification banner thông báo nhẹ nhàng.



- **Buff Toàn Diện Trong Đêm Huyết Nguyệt:**



  - **x2.0 Tỷ lệ xuất hiện của toàn bộ 7 loài cá Halloween**.



  - **+20% Vàng & +20% EXP** cho tất cả cá câu được.



  - **Vầng Hào Quang Đỏ Rực:** 100% cá Halloween và 25% cá thường có vầng hào quang ma mị.



  - **Cân Bằng Tỷ Lệ Rơi Rương & Kẹo:** Thanatos rơi 100% Rương Ma Quái; 6 loài cá Halloween khác có 20% rơi Kẹo Trick-or-Treat và 8% rơi Rương Ma Quái; cá thường có 10% rơi Kẹo.







### 🏪 Nâng Cấp 2 Cửa Hàng (ShopScreen.java & EventScreen.java + EventManager.java)



- **Quầy Halloween Độc Quyền Trong Shop Thường (`ShopScreen.java`):**



  - Thêm danh mục `HALLOWEEN` vào thanh filter 5 tab cân đối.



  - Bán 5 món đạo cụ bằng Vàng với viền thẻ neon cam/tím bí ngô ma quái.



- **Cửa Hàng Đổi Thưởng Điểm Sự Kiện (`EventScreen.java` + `EventManager.java`):**



  - Hoàn thành 4/6 nhiệm vụ ngày nhận ngay **100 Điểm Sự Kiện (EP)** mỗi ngày (thay vì 1 điểm).



  - Tinh chỉnh bảng giá đổi đồ phù hợp với thu nhập 100 EP/ngày:



    - Set Cần Bí Ngô Limited (Cần 500 EP, Dây 350 EP, Móc 350 EP = 1200 EP).



    - Vé Gacha Huyết Nguyệt (Cao Cấp): 60 EP (**Không giới hạn số lượng mua**).



    - Vé Gacha Tiêu Chuẩn: 30 EP (**Không giới hạn số lượng mua**).



    - Túi 15,000 Vàng: 25 EP.







### 🎵 Bộ 27 Âm Thanh Thủ Tục ZzFX Nạp Trực Tiếp Trong RAM (ZzFX.java & AudioManager.java)



- **Zero-Footprint Procedural Audio:** Tự động tổng hợp dạng sóng PCM 16-bit trong RAM, không phụ thuộc vào tệp WAV bên ngoài.



- Bao gồm đầy đủ 27 âm thanh:



  - 5 âm thanh dùng đạo cụ Halloween (`sfx_halloween_candy`, `sfx_pumpkin_lantern`, `sfx_ghost_potion`, `sfx_blood_moon_talisman`, `sfx_ghost_candle`).



  - Âm thanh bắt cá Halloween (`sfx_halloween_catch`), âm thanh tiếng sấm Huyết Nguyệt (`sfx_blood_moon_roar`).



  - Âm thanh 7 loài cá Halloween (IDLE/PULLING: Skeleton, Jack-o', Đầu Lâu, Dạng Ma, Frankenstein, Hải Xà, Thanatos).



  - Âm thanh các kỹ năng ma quái (Sấm sét hồi sinh, Tiếng hú câm lặng, Vực xoáy âm ty, Triệu hồn tạo Soul Shield).







### 🛡️ Tích Hợp Hiển Thị Buffs Trên HUD (GameHUD.java)



- Tích hợp 100% vào hệ thống `getActiveBuffs` có sẵn của game, hiển thị đếm ngược thời gian thực `mm:ss` cho Kẹo (15m), Bùa Huyết Nguyệt (15s), số lượt cho Đèn Lồng (3 lần) và Độc Dược (3 lần), trạng thái bảo hộ của Nến Hồn Ma và trạng thái Bầu Trời Huyết Nguyệt.







---







## 📅 [2026-10-27] - Phiên bản v0.5.4.3 (Tinh Chỉnh Giao Diện Sổ Tay & Tạo Hình Lại Cá Bí Ẩn '???')







### 🛠️ Tinh Chỉnh Giao Diện Bách Khoa Toàn Thư (CatalogScreen.java)



- **Loại Bỏ Lỗi Ký Tự Vuông (Missing Font Glyphs):**



  - Xóa bỏ các ký tự emoji không nằm trong bảng mã BitmapFont gốc, chuẩn hóa toàn bộ chuỗi phần thưởng thành văn bản phẳng thanh lịch: `+50 KC  •  +5,000 Vàng  •  +2,500 XP` và nút nhận `+15 KC NHẬN`.



  - Loại bỏ widget hiển thị Vàng/Kim Cương ở góc trên bên phải Sổ tay theo phản hồi người dùng, giúp dải 4 Tab bản đồ nằm cân đối hoàn hảo.



- **Tối Ưu Bố Cục Khung Thờ Halloween:**



  - Hạ thấp tọa độ đỉnh khung xuống `cardY = 520f` (so với `H - 165f = 555f` trước đó), đảm bảo đường viền trên (Top Border) và toàn bộ 4 góc bo nhọn Gothic nằm trọn vẹn trong vùng hiển thị `clipBounds [20f, 530f]`.



  - Phóng to thanh tiến trình lên **`280px` x `16px`** với viền neon tím/cam nổi bật; rút gọn nhãn hiển thị thành `0/7 Loài` (bỏ chữ "Đã Bắt") đặt chính giữa phía trên thanh tiến trình, chấm dứt hoàn toàn tình trạng chữ đè lên nhau.







### 🐟 Thiết Kế Lại Toàn Diện Tạo Hình Cá Bí Ẩn "???" (`drawSilhouetteFish`)



- **Khắc phục tạo hình hoạt họa sơ sài cũ:** Trước đây cá chưa câu được hiển thị bằng 3 hình học đơn điệu trông như quả trứng cụt đuôi.



- **Tạo hình Bóng Đen Thủy Tộc Đẳng Cấp RPG (Áp dụng toàn bộ Sổ tay):**



  - Thiết kế đa tầng hình học: Bệ bóng mờ đáy biển, đuôi chẽ hình lưỡi liềm thể thao (`Forked Lunate Tail`), vây lưng cong vút lướt sóng, vây ngực vuốt nhọn khí động học.



  - Thân ngư lôi thon gọn khí chất kẻ săn mồi đáy biển, đường viền sáng nhẹ phản quang dọc lưng và hốc mắt bí ẩn có điểm sáng xanh huyền bí.







---







## 📅 [2026-10-27] - Phiên bản v0.5.4.2 (Background Riêng & Bố Cục Bệ Thờ Độc Quyền Sổ Tay Halloween - CatalogScreen)







### 🎨 Background Riêng Biệt Cho Tab HALLOWEEN (CatalogScreen.java)



- **Bầu Trời Đêm Tím Thẫm Gothic:** Chuyển màu nền từ xanh dương đại dương sang tông tím đen ma mị `Color(0.06f, 0.02f, 0.08f, 1f)`.



- **Mặt Trăng Huyết Nguyệt Đáy Biển (Blood Moon Eclipse):** Mặt trăng máu khổng lồ tỏa 3 tầng hào quang nguyệt thực đỏ rượu và tím u linh (`Color(0.95f, 0.15f, 0.22f, 0.28f * pulse)`), điểm xuyết các vết tích hố thiên thạch cổ xưa và bóng nguyệt thực huyền bí.



- **Sương Mù U Linh & Hạt Bụi Ma Quái:** 3 lớp sương mù tím bồng bềnh dập dềnh dưới chân màn hình; các hạt bọt biển được thay thế bằng tia lửa tàn bí ngô cam vàng (`Pumpkin Embers`) và linh hồn ma trơi tím neon (`Ghost Wisps`).







### 🏛️ Bố Cục Bệ Thờ 2 Tầng Độc Quyền (2-Tier Gothic Altar Layout)



- **Khắc phục triệt để hiện tượng co rúm & đè chữ:** Trước đây bộ sưu tập Halloween có 7 loài bị ép vào khung 2 cột 560px khiến tiêu đề và phần thưởng đè lên nhau, các thẻ cá bị co lại còn 67px.



- **Khung Thờ Ma Quái Đại Trung Tâm (1160px x 475px):**



  - Trải rộng toàn màn hình, viền neon cam/tím rực rỡ với họa tiết góc nhọn Gothic.



  - Header rộng rãi phân tách rõ: Tiêu đề bộ sưu tập lớn, thanh tiến độ 7 loài cá và thông tin phần thưởng (+50 💎 KC, +5,000 🪙 Vàng, +2,500 ⭐ XP) hiển thị trang trọng.



- **Phân Tầng Hiển Thị 7 Loài Cá Hoàn Hảo (Không Cần Cuộn Trang):**



  - **Tầng 1 (4 Loài Cơ Bản & Hiếm):** Cá Skeleton, Cá Đèn Lồng Jack-o', Cá Đầu Lâu Huyết Nguyệt, Cá Dạng Ma — mỗi thẻ rộng `268px` x `175px`, có bệ hào quang neon và thẻ tên rõ nét.



  - **Tầng 2 (3 Loài Tinh Anh & Siêu Boss):** Thủy Quái Frankenstein (Epic), Hải Xà Dạ Xoa (Legendary), Kình Ngư Tử Thần Thanatos (World Boss) — bệ thờ rộng tới `360px` x `195px`, trang bị viền đỏ plasma, nhãn huy chương đặc quyền (`☠ WORLD BOSS ☠`, `★ LEGENDARY ★`) và bệ hào quang linh hồn uy nghiêm.







---







## 📅 [2026-10-27] - Phiên bản v0.5.4.1 (Sửa Lỗi Crash Sổ Tay & Tạo Hình Vector 7 Loài Cá Halloween Độc Bản - HLW-04.3)







### 🐛 Sửa Lỗi Crash Khi Mở Sổ Tay Thủy Tộc (CatalogScreen.java)



- **Hiện tượng**: Người chơi nhấp vào nút "SỔ TAY" từ màn hình chính hoặc HUD thì game bị văng (crash) tức thì.



- **Nguyên nhân gốc rễ**: Khi mở rộng Tab thứ 4 (HALLOWEEN) trong `CatalogScreen.java`, hai mảng bộ nhớ đệm `cachedCompletedInBiome` và `cachedTotalColInBiome` vẫn giữ nguyên kích thước khởi tạo là `new int[3]`. Khi phương thức `refreshCollectionStats()` duyệt vòng lặp `for (int i = 0; i < 4; i++)`, việc gán phần tử tại `i = 3` kích hoạt ngoại lệ `java.lang.ArrayIndexOutOfBoundsException: Index 3 out of bounds for length 3`.



- **Giải pháp xử lý**:



  - Tăng kích thước 2 mảng bộ nhớ đệm lên `new int[4]`.



  - Khởi tạo đầy đủ và an toàn trạng thái cho cả 4 tab: Ao Làng, Bãi Biển, Đảo Xa và Halloween.







### 🎨 Tạo Hình Vector 7 Loài Cá Halloween Độc Bản (HLW-04.3 - FishVisualRenderer.java)



- **Chuẩn hóa hệ sinh thái Vector đa tầng (Không dùng bitmap):**



  1. **Cá Skeleton (Common):** Cột sống và hàng xương sườn trắng ngà lộ rõ; hộp sọ cá sắc cạnh với hốc mắt đen sâu hoắm phát ánh sáng lân tinh xanh cyan; vây đuôi tưa rách quẫy nhịp nhàng.



  2. **Cá Đèn Lồng Jack-o' (Uncommon):** Thân hình quả bí ngô cam mập tròn trĩnh; trên thân khắc hoa văn khuôn mặt Jack-o'-Lantern ranh mãnh rực lửa vàng chói lọi; cuống vây lưng lá bí xanh gai uốn lượn.



  3. **Cá Đầu Lâu Huyết Nguyệt (Rare):** Thân hình cá săn mồi đen tuyền vệt sọc đỏ huyết mạch; đầu cá bọc chiếc sọ sừng nhọn uy hiếp; mắt đỏ ruby rực cháy; vây đuôi trăng khuyết chém sóng nước.



  4. **Cá Dạng Ma (Epic):** Thiết kế bán trong suốt huyền ảo (`alpha = 0.55f`); thân cá uốn lượn bồng bềnh như dải lụa ma trơi tan biến thành vệt khói tím hư ảo; đôi mắt u buồn phát sáng.



  5. **Thủy Quái Frankenstein (Epic):** Thân mình chắp vá từ các mảng da xanh rêu và xám tro sần sùi; 2 bu-lông kim loại cắm xuyên qua mang; các đường chỉ khâu đen sắc nét; tia chớp điện màu vàng nhạt nổ lách tách quanh vây.



  6. **Hải Xà Dạ Xoa (Legendary - Thay thế Bạch Tuộc):** Thân rồng biển đen obsidian uốn lượn đa khúc bọc gai xương nhọn hoắt; mắt dạ xoa rực lửa tím tà ác, mang phát quang xanh lục độc địa; hàm răng nanh sắc lẹm phun khói độc âm ty; vây lưng dạng lưỡi cưa phát sáng u linh ma quái.



  7. **Kình Ngư Tử Thần Thanatos (Siêu Boss Thế Giới):** Kích thước vĩ đại choáng ngợp; toàn thân màu đen hắc ám bọc các phiến giáp xương gai nhọn chĩa ngược; vây lưng vươn cao sắc bén như lưỡi hái thần chết với lưỡi plasma huyết sắc; 3 tầng hào quang ma quái (vòng tối vực thẳm ngoài cùng, hào quang tím u linh ở giữa, tia lửa huyết nguyệt crackling bên trong) cùng các linh hồn bay lơ lửng.







---







## 📅 [2026-10-27] - Phiên bản v0.5.4.0 (Giai Đoạn 3: Bộ 7 Cá Halloween, Siêu Boss Thanatos & Hệ Thống Giáp Toàn Diện)







### 🎃 Triển Khai Toàn Diện Giai Đoạn 3 Sự Kiện Halloween (HLW-03)



- **Hệ Sinh Thái 7 Loài Cá Halloween Toàn Cầu (Độ Khó Vượt Map Đảo Xa):**



  - Tỷ lệ xuất hiện toàn cầu cơ bản **15%**, cộng hưởng buff Kẹo Trick-or-Treat (+15%), Set Dạ Hội Bí Ngô (+10%) và Set Huyết Nguyệt Thần Vương (+15%).



  - Cân bằng chỉ số vượt trội Đảo Xa:



    * *Cá Skeleton (Common):* 400 HP, 58 dmg/s.



    * *Cá Đèn Lồng Jack-o' (Uncommon):* 720 HP, 90 dmg/s.



    * *Cá Đầu Lâu Huyết Nguyệt (Rare):* 1,300 HP, 132 dmg/s.



    * *Cá Dạng Ma - Phantom (Epic):* 2,000 HP, 168 dmg/s.



    * *Thủy Quái Frankenstein (Epic):* 2,250 HP, 178 dmg/s.



    * *Hải Xà Dạ Xoa (Legendary - Thay thế Bạch Tuộc):* 4,000 HP, 210 dmg/s.



    * *Kình Ngư Tử Thần Thanatos (Siêu Boss):* 6,666 HP, 235 dmg/s.



  - Buff máu Siêu Boss Cá Cờ Xanh Khổng Lồ Đảo Xa lên **3,600 HP**.







### 🛡️ Hệ Thống Giáp (Armor / Soul Shield) & Phá Giáp Xuyên Máu Thật



- **Hiển thị trực quan dải Giáp trên GameHUD:** Khi cá sở hữu Giáp, thanh máu được phủ một dải Giáp màu Cyan neon kèm nhãn `[GIÁP: XX]`. Sát thương đòn đánh thường bị trừ vào Giáp trước.



- **Cơ chế Phá Giáp (`pha_giap`) chiến thuật:**



  - **Sát thương gốc (Base Damage):** XUYÊN THẲNG QUA LỚP GIÁP trừ vào máu thật của cá (`takeTrueDamage`), hiển thị `-[Dmg] XUYÊN GIÁP!`.



  - **Sát thương cộng thêm (Bonus Damage):** Đánh thẳng bóc mòn Lớp Giáp (`damageShield`), hiển thị `-[Dmg] VÀO GIÁP!`.







### ⚡ Bộ Kỹ Năng Độc Bản & Đại Chiến Trùm Thanatos 3 Pha



- *Cá Skeleton:* Nội tại Cốt Cách Bất Diệt giảm 20% sát thương kéo thường, chịu thêm +50% sát thương từ mọi kỹ năng người chơi.



- *Cá Đèn Lồng Jack-o':* Hỏa Ngục Lân Tinh (+25% sinh tension khi PULLING, thiêu đốt 3 TL/s nếu tension > 70%); Hỏa Đăng Jack-o' nổ giật +15% tension & -10 TL (ngắt được bằng Trói Buộc).



- *Cá Đầu Lâu:* Khát Máu Đêm Rằm (hồi 1% HP/s khi player < 50% TL); Xung Kích Đoạt Hồn Re-Hook 0.6s, nếu trượt phạt giật +25 tension và cá hồi 15% máu đã mất.



- *Cá Dạng Ma:* Thể Xác Hư Vô (chu kỳ 8s hóa hư vô 2.5s miễn nhiễm sát thương); Tiếng Than U Ám (hút 15 TL hồi 5% HP, giảm 50% hồi phục trong 5s).



- *Thủy Quái Frankenstein:* Quá Tải Điện Năng (kéo > 1.5s phóng sét -10 TL & ngắt kéo dây 0.6s); Sấm Sét Hồi Sinh (hồi sinh tức thì 20% HP khi cạn máu lần 1 kèm sốc +25% tension).



- *Hải Xà Dạ Xoa:* Uốn Lượn Ma Quái (giảm 30% xả chùng Slack, quất đuôi giật +10 tension); Mãng Xà Siết Cước (khóa cứng tension 3s); Độc Tố Âm Ty (rút 5 TL/s trong 5s).



- *Siêu Boss Thanatos 3 Pha & Tuyệt kỹ Liên Hoàn Trảm Đoạt Mệnh:*



  - Nội tại Uy Áp Tử Thần (+20% tiêu hao Thể lực kỹ năng người chơi).



  - Pha 1: Tiếng Hú Địa Ngục câm lặng chiêu tiếp theo của người chơi.



  - Pha 2: Vực Xoáy Âm Ty (+15 tension/s trong 3s & hồi 3% HP); Âm Hồn Quấy Phá tạo Soul Shield 500 HP.



  - Pha 3 (< 30% HP): Tử Thần Bộc Phát giảm 30% hồi chiêu kỹ năng thường; Tuyệt kỹ Liên Hoàn Trảm 2 nhịp Re-Hook cộng dồn thành công:



    * 2/2 Thành công: Choáng Thanatos 3.5s nhận x2.0 sát thương.



    * 1/2 Thành công: Phạt nhẹ tê liệt cánh tay -40% lực kéo trong 4s và -15 TL (không tăng tension).



    * 0/2 Thành công: Phạt nặng +30 tension, phong ấn skill & đóng băng hồi TL trong 5s; cá hồi 5% HP.



    * 🚨 **Cơ chế đứt dây tức thì:** Nếu tại thời điểm xử lý hình phạt, lực căng dây vượt mức **90% Max Tension Khả Dụng** $\implies$ Lưỡi hái chém đứt phăng dây câu ngay lập tức!



- **Hệ thống Hàng Đợi Kỹ Năng & Global Cooldown (GCD 5.5s):** Giãn cách an toàn 5.5s giữa các lần cá tung chiêu chủ động, chống chồng đè kỹ năng.







### 📚 Sổ Tay Thủy Tộc & Vật Phẩm Chiến Tích



- **Tab Sổ Tay HALLOWEEN:** CatalogScreen mở rộng tab thứ 4 HALLOWEEN phong cách Gothic tím ma mị; đăng ký bộ sưu tập mới `hlw_dem_ma_quai` ("Huyền Thoại Đêm Halloween", thưởng 5000V, 2500XP, 50 KC) cùng hệ thống lore chuyên sâu cho 7 loài cá.



- **Rương Ma Quái (100% rơi từ Thanatos):** Mở ra nhận 3-5 Vé Gacha Halloween, 30-80 Kim Cương và 20-30 Kẹo Trick-or-Treat.







---







## 📅 [2026-10-27] - Phiên bản v0.5.3.5 (Tối Ưu Hiệu Ứng Rung Lắc Camera Căng Dây & Kế Hoạch Giai Đoạn 3: Bộ 7 Cá Halloween & Boss Thanatos)







### 🌊 Nâng Cấp Trải Nghiệm Chiến Đấu & Hiệu Ứng Rung Lắc (VFX-SHK-01)



- **Hạ thấp ngưỡng bắt đầu rung chấn:** Camera bắt đầu rung lắc nhẹ nhàng ngay từ mốc **80% Tension** (giảm từ 88% trước đây) trong [`PlayScreen.java`](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/PlayScreen.java#L122-L130), giúp người chơi sớm cảm nhận độ căng và sức nặng của cuộc giằng co.



- **Tăng tiến cường độ rung chấn (+0.02f):** Tăng mức chấn động lên `+0.02f` trauma mỗi tick ở vùng nguy hiểm (`> 80% Tension`).



- **Cường độ rung chấn cực hạn sát ngưỡng đứt dây:**



  - Khi Tension vượt ngưỡng **94%** (nguy cơ đứt cước cận kề), trần trauma tối đa được nâng từ `0.16f` lên **`0.45f`** và lượng trauma gia tăng mỗi tick là `+0.035f`.



  - Hiệu ứng rung giật màn hình phi tuyến (`shake = trauma^2`) dữ dội, cảnh báo nghẹt thở cho người chơi lập tức buông phím Space hoặc xả dây trước khi quá tải đứt cước.







### 🎃 Kế Hoạch Chi Tiết Giai Đoạn 3 Sự Kiện Halloween (HLW-03 - Cân Bằng Nâng Cao)



- **Chuẩn độ khó vượt trội Map Đảo Xa:** Toàn bộ 7 loài cá Halloween được buff mạnh chỉ số HP và Lực kéo (Pull Strength) để tạo thử thách xứng tầm cho người chơi sở hữu Set Dạ Hội Bí Ngô và Set 6★ Huyết Nguyệt Thần Vương:



  - *Cá Skeleton (Common):* 400 HP, 58 dmg/s.



  - *Cá Đèn Lồng Jack-o' (Uncommon):* 720 HP, 90 dmg/s.



  - *Cá Đầu Lâu Huyết Nguyệt (Rare):* 1,300 HP, 132 dmg/s.



  - *Cá Dạng Ma - Phantom (Epic):* 2,000 HP, 168 dmg/s.



  - *Thủy Quái Frankenstein (Epic):* 2,250 HP, 178 dmg/s.



  - *Hải Xà Dạ Xoa (Legendary - Thay thế Bạch Tuộc):* 4,000 HP, 210 dmg/s.



  - *Siêu Boss Thanatos (Boss):* Giữ nguyên 6,666 HP, buff sát thương kéo lên 235 dmg/s (Pha cuối bộc phát 290+ dmg/s).



- **Đại tu toàn diện bộ kỹ năng độc nhất vô nhị:**



  - *Cá Skeleton:* Xóa bỏ active skill; nội tại Cốt Cách Bất Diệt giảm 20% đòn kéo thường nhưng chịu thêm **+50% sát thương từ MỌI KỸ NĂNG** người chơi.



  - *Cá Đèn Lồng Jack-o':* Nội tại mới Hỏa Ngục Lân Tinh (+25% tốc độ tăng Tension khi cá PULLING, thiêu đốt 3 Thể lực/s nếu Tension >70%).



  - *Cá Đầu Lâu Huyết Nguyệt:* Nội tại đổi thành hồi +1% Max HP/s khi cần thủ <50% Thể lực; trượt Re-hook cá lập tức hồi 15% máu đã mất.



  - *Cá Dạng Ma (Phantom):* Tăng thời gian debuff giảm 50% hồi phục thể lực lên 5.0 giây.



  - *Thủy Quái Frankenstein:* Quá tải điện năng trừ 10 Thể lực và ngắt nhịp kéo dây 0.6 giây.



  - *Hải Xà Dạ Xoa (Legendary):* Rồng Biển Xương Đen Đáy Vực sở hữu nội tại giảm 30% xả chùng Slack; quất đuôi giật +10% Tension; Mãng Xà Siết Cước khóa cứng Tension trong 3s; Độc Tố Âm Ty rút 5 Thể lực/s (5s) và phủ sương mù tím ma quái.



  - *Đại chiến Trùm Thanatos 3 Pha:*



    * Nội tại Uy Áp Tử Thần tăng +20% tiêu hao Thể lực của mọi kỹ năng người chơi.



    * Tiếng Hú Địa Ngục: Vô hiệu hóa (câm lặng) chiêu thức tiếp theo của người chơi.



    * Vực Xoáy Âm Ty: Tự động kéo căng dây +15 Tension/s kèm hồi 3% HP cá (+200 HP).



    * Âm Hồn Quấy Phá: Tạo lớp Giáp Ma Ám (Soul Shield) 500 HP hấp thụ 50% đòn đánh thường, bị phá nát ngay lập tức nếu trúng chiêu Phá Giáp.



    * Pha cuối: Bỏ giáp tử thần, thay bằng giảm 30% hồi chiêu tất cả kỹ năng thường; Tuyệt kỹ Liên Hoàn Trảm Đoạt Mệnh 2 nhịp Re-Hook liên tiếp (0.5s & 0.6s) với cơ chế phản đòn choáng 3s hoặc nguy cơ đứt dây cực hạn.



- **Hệ Thống Giáp Hiển Thị Thanh HP & Phá Giáp Xuyên Máu:**



  - Lớp Giáp (Soul Shield 500 HP) được render trực tiếp dưới dạng dải màu Cyan trên thanh máu của cá tại GameHUD.



  - Các đòn đánh thường bị Giáp hấp thụ trừ vào Giáp trước.



  - **Cơ chế Phá Giáp (`pha_giap`) chiến thuật:** Sát thương gốc của kỹ năng **XUYÊN THẲNG VÀO MÁU THẬT** của cá, còn lượng sát thương cộng thêm (bonus dmg) sẽ **TRỪ VÀO LỚP GIÁP**, giúp bóc mòn giáp Boss hiệu quả mà vẫn bảo lưu sát thương trực tiếp.



- **Tuyệt Kỹ Liên Hoàn Trảm 2 Nhịp - Cơ Chế Cộng Dồn Thành Công:**



  - Thanatos tung đòn 2 nhịp liên hoàn (0.5s & 0.6s) với cơ chế cộng dồn số lần Perfect Re-Hook:



    * *Thành công cả 2 nhịp (2/2):* Phản đòn cực đại, bẻ gãy lưỡi hái! Thanatos bị **choáng 3.5 giây** và nhận **x2.0 sát thương**!



    * *Thành công 1 nhịp (1/2) - Phạt nhẹ (ít liên quan tension):* Tê liệt cánh tay trong 4s (giảm 40% lực kéo SPACE) và trừ 15 Thể lực. Tuyệt đối **không tăng Tension**.



    * *Trượt cả 2 nhịp (0/2) - Phạt nặng:* Giật +30 Tension, **khóa toàn bộ kỹ năng & phong ấn hồi thể lực trong 5.0 giây**, Thanatos hồi 5% HP (+333 HP).



    * 🚨 **Cơ Chế Đứt Dây Tức Thì:** Nếu tại thời điểm xử lý hình phạt, lực căng dây vượt mức **90% Max Tension Khả Dụng** (đã tính cả phần max tension bị giảm) $\implies$ **DÂY CÂU BỊ CHÉM ĐỨT PHĂNG LẬP TỨC**!



- **Hệ Thống Hàng Đợi Kỹ Năng & Giãn Global Cooldown Lên 5.0s - 6.0s:**



  - Do thời gian hồi chiêu của cá khá dài (12s - 18s), bộ đếm Global Cooldown được giãn cách lên **5.0s - 6.0s** (`fishSkillGCD = 5.5f`) giữa các kỹ năng, giúp người chơi có nhịp thở và không gian giằng co chiến thuật.



- **Cân Bằng Boss Cá Cờ Xanh Khổng Lồ Đảo Xa:**



  - Buff máu tối đa của Siêu Boss Đảo Xa từ **3,000 HP lên 3,600 HP** (Lực kéo giữ nguyên 195.0 dmg/s) trong [`fishes.json`](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/data/fishes.json) và [`CatalogScreen.java`](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/CatalogScreen.java#L927).











---







## 📅 [2026-10-27] - Phiên bản v0.5.3.4 (Khắc Phục Lỗi Kỹ Năng Dưỡng Sức & Tinh Chỉnh Tiêu Hao Thể Lực Động Theo Trạng Thái Cá)







### 🐛 Sửa Lỗi Kỹ Năng & Cân Bằng Gameplay (BAL-STM-01)



- **Sửa Lỗi Kỹ Năng Dưỡng Sức (`duong_suc`) - Khắc Phục Lỗi Hồi Kép**:



  - **Nguyên nhân bug:** Hệ thống GAS (`DuongSucAbility` -> `effect_duong_suc`) đã kích hoạt hiệu ứng hồi phục Thể lực đều đặn theo thời gian (`staminaRegenPerSec = 10.0f/s`), nhưng tại phương thức kết thúc `duongSucChannelTimer <= 0` trong `FishingManager.java` lại tồn đọng logic cũ gọi `player.consumeStamina(-recovery)` với `recovery = 50f`. Điều này dẫn đến việc người chơi vừa được hồi đều đặn +10/s vừa bị cộng dồn thêm một lượng đột biến +50 Thể lực ở cuối kênh dẫn.



  - **Khắc phục:** Loại bỏ hoàn toàn dòng lệnh cộng dồn +50 Thể lực khi hết thời gian vận sức; đồng bộ chuẩn hóa để toàn bộ lượng hồi phục 50 Thể lực được phân bổ đều đặn và mượt mà qua hệ thống GAS trong suốt thời gian vận sức.



  - **Chuẩn hóa thời gian vận sức & Cây Kỹ Năng (`SkillTree.java`, `Skill.java`):**



    - **Cấp 1:** Vận sức 5.0 giây, hồi tổng cộng 50 Thể lực (`+10.0 Thể lực/giây`).



    - **Cấp 2:** Vận sức 4.0 giây, hồi tổng cộng 50 Thể lực (`+12.5 Thể lực/giây`).



    - **Cấp 3:** Vận sức 3.0 giây, hồi tổng cộng 50 Thể lực (`+16.7 Thể lực/giây`).



    - Cập nhật định dạng hiển thị thông số chiêu thức trong Cây Kỹ Năng trực quan: `Vận sức X.Xs hồi +50 Thể lực (+Y/s)`.







- **Cơ Chế Tiêu Hao Thể Lực Động Trong Giao Tranh (Dynamic Combat Stamina Drain)**:



  - **Tiêu hao nhanh hơn khi cá PULLING:** Tăng mức tiêu hao thể lực cơ bản từ `2.0/s` khi cá nghỉ ngơi (`IDLE`) lên `3.0/s` khi cá đang ra sức kéo dây (`PULLING`).



  - **Biến thiên theo sát thương/lực kéo của cá (`pullStrength`):** Bổ sung hệ số biến thiên `pullStrength * 0.015f` vào công thức tiêu hao thể lực theo giây. Cá càng lớn và hung hãn (như các loài cá Săn Mồi Đảo Xa, Boss Cá Cờ Xanh 195 dmg/s) sẽ khiến người chơi mất nhiều thể lực hơn để duy trì việc ghìm giữ cần câu.



  - **Công thức tính toán:** `totalDrainPerSec = (baseDrain + pullStrength * 0.015f) * drainMult`, trong đó `drainMult` tiếp tục chịu ảnh hưởng tương tác từ các kỹ năng bị động (Cơ Bắp Thép, Phản Xạ) và cơ chế cuồng nộ Boss.







---







## 📅 [2026-10-27] - Phiên bản v0.5.3.3 (Đại Tu Cân Bằng Kỹ Năng 15 Loài Cá Đảo Xa, Re-Hook Cải Tiến & Thanh Tension Realtime)







### ⚖️ Đại Tu Cân Bằng Kỹ Năng Cá Đảo Xa & Nâng Cấp Gameplay (BAL-FSH-02)



- **Hiển Thị Giới Hạn Max Tension Thời Gian Thực (Visual Realtime Gauge)**:



  - Giữ nguyên số hiển thị gốc (`Current / Max`) trên Header Card để người chơi nắm được thông số trang bị.



  - Vẽ dải sọc đỏ chéo cảnh báo (Hazard Red Cap) bao phủ phần Max Tension bị giảm ở đỉnh thanh đo.



  - Đặt một vạch mốc đỏ neon nhấp nháy kèm mũi tên tam giác và text chỉ báo `% bị giảm` (`-10%`, `-20%`, `-30%`) chính xác tại ngưỡng Max Tension khả dụng mới.



- **Thân Đao Xé Gió (Cá Thu Ngàng - 600 HP, 85 dmg/s)**:



  - Tái cấu trúc cơ chế: Rút ngắn 40% thời lượng pha PULLING (còn 0.9s - 2.1s) nhưng tăng +40% lực kéo và giật tức thì +12% Tension vào dây cước ngay khi bắt đầu bơi.



- **Vũ Điệu Cờ Buồm (Cá Cờ Gòn - 1,150 HP, 125 dmg/s)**:



  - Cơ chế phạt kép: Cá nhảy 2 nhịp liên tiếp (mỗi nhịp 0.8s). Mỗi nhịp nếu trượt Perfect Re-hook phạt thẳng +30 đơn vị Tension (tổng trượt 2 nhịp nhận trọn +60 Tension).



- **Vực Đá Nuốt Chửng (Cá Mú Nghệ Khơi - 1,750 HP, 150 dmg/s)**:



  - Cú giật Tension ban đầu được nerf từ +35% xuống +20% Tension.



  - Kích hoạt debuff vĩnh viễn trong trận: Giảm 30% lượng hồi Thể lực của người chơi từ mọi nguồn (Bình Thể Lực, Bánh Mì, Dưỡng Sức, Hút máu) cho đến hết trận đấu.



- **Cắn Xé Điên Cuồng (Cá Mập Rạn Đen - 1,900 HP, 160 dmg/s)**:



  - Nerf cơ bản: Giảm 20% Max Tension an toàn trong 5 giây.



  - Tương tác nội tại Thính Mùi Máu: Nếu Thể lực người chơi < 50%, chuyển thành Cắn Xé Cuồng Huyết, bóp nghẹt 30% Max Tension và trừ ngay 30 Thể lực.



- **Siêu Boss Cá Cờ Xanh Khổng Lồ (3,000 HP, 195 dmg/s)**:



  - Mũi kiếm xé biển: Giảm ngưỡng phạt ghìm kéo từ 1.5s xuống 1.2s; bỏ hoàn toàn điểm thưởng sát thương người chơi.



  - Vũ điệu lướt sóng: Tiếp nước sốc giảm từ +35% xuống +20% Tension, trừ -15 Thể lực và hồi 8% máu Boss trừ khi Perfect Re-hook.



  - Cuồng phong ngự kiếm: Dưới 30% HP, mỗi lần lướt sóng tăng +10% lực kéo Boss cộng dồn; Boss bất tử khi ở trên không trong thời gian nộ.



- **Cơ Chế Re-Hook Chống Spam & Chống Ghìm Phím**:



  - Phạt +20% Tension nếu đè giữ phím Space quá 50% thời gian bay của cá.



  - Ngăn chặn hoàn toàn việc spam phím Space: Chỉ chấp nhận đúng 1 lần bấm duy nhất trong cửa sổ tiếp nước (0 < timer <= 0.35s). Bấm sớm hoặc spam sẽ bị phạt sốc lực.



- **Hệ Thống Floating Text Toàn Diện Cho Mọi Kỹ Năng & Nội Tại**:



  - Bổ sung chữ nổi thông báo ngắn gọn chỉ gồm Tên Kỹ Năng (không ghi thông số) và đồng bộ toàn bộ chú thích kỹ năng trong Bách Khoa Cá (CatalogScreen).







---







## 📅 [2026-10-27] - Phiên bản v0.5.3.2 (Tối Ưu Hóa Giao Diện Tooltip & Báo Cáo Kỹ Năng Đảo Xa)







### 🎨 Tối Ưu Hóa Giao Diện Tooltip & Cân Bằng Gameplay (UI-FIX-01)



- **Mục tiêu**: Loại bỏ triệt để hiện tượng tràn chữ (overflow) ra ngoài khung viền trên màn hình Bách Khoa Cá và HUD Túi đồ nhanh; tối ưu bố cục hiển thị thanh trạng thái Radar Sonar của Kính Nhìn Xuyên Nước; cung cấp báo cáo chi tiết kỹ năng 15 loài cá Đảo Xa.



- **Tự động xuống dòng (Text Wrap) & Chiều cao động (CatalogScreen.java)**:



  - Tăng độ rộng tooltip lên `480f` và tính toán trước chiều cao thực tế của danh sách kỹ năng cùng dòng Lore mô tả (`GlyphLayout.setText` với `targetWidth = tw - 38f`, `wrap = true`).



  - Phân tách rõ ràng Tên kỹ năng (vàng ánh kim `1f, 0.85f, 0.35f`) và Mô tả cơ chế (trắng lam `0.88f, 0.94f, 1f`) thụt lề 10px tinh tế.



  - Sắp xếp thứ tự các thành phần theo dòng chảy tự nhiên (Flow layout), loại bỏ toàn bộ tọa độ tuyệt đối hardcoded gây đè chữ lên Lore và Thưởng gốc Footer.



- **Tự động xuống dòng & Co giãn hộp vật phẩm (GameHUD.java)**:



  - Thiết lập chiều rộng `300f` cho vật phẩm thường và `340f` cho sách kỹ năng.



  - Tự động đo chiều cao mô tả bằng `GlyphLayout` (`wrap = true`), co giãn chiều cao `tipH` ôm khít nội dung, không còn bị tràn chữ sang phải đè lên các ô phím tắt Quickslot `[1]-[9]`.



- **Hạ thấp Banner Radar Sonar Thấu Thị (GameHUD.java)**:



  - Chuyển tọa độ hiển thị của Radar Thấu Thị từ nửa trên màn hình (`Y=550f`) xuống nửa dưới màn hình (`Y=200f`), nằm ở vị trí thoáng đãng ngay dưới phao câu cá.



  - Chấm dứt hoàn toàn sự cố đè lấp giữa Banner tỷ lệ câu mồi (`castBuffBanner`) và Banner Radar Thấu Thị.



- **Báo Cáo Phân Tích Cơ Chế Kỹ Năng 15 Loài Cá Đảo Xa**:



  - Hoàn tất bản báo cáo chi tiết toàn bộ chỉ số HP, lực kéo, điều kiện kích hoạt và tác động giằng co của 15 loài cá Đảo Xa phục vụ cân bằng game.







---







## 📅 [2026-10-27] - Phiên bản v0.5.3.1 (Giai Đoạn 2: Siêu Phẩm Limited 6★ Huyết Nguyệt Thần Vương, Set Bí Ngô & Banner Gacha Chuyển Động)







### 🩸 Giai Đoạn 2: 2 Bộ Trang Bị Halloween Độc Quyền & Banner Gacha Tự Động Chuyển Giao (HLW-02)



- **Mục tiêu**: Hiện thực hóa trọn vẹn 2 bộ trang bị chủ đề Lễ Hội Halloween: Bộ Cày Cuốc F2P "Dạ Hội Bí Ngô" (4★) và Siêu Phẩm Giới Hạn Tối Thượng "Huyết Nguyệt Thần Vương" (6★ Limited Premium), kèm cơ chế chuyển giao banner gacha động mà không làm ảnh hưởng sự kiện Lạc Hồng đang chạy.



- **Set 1: "Dạ Hội Bí Ngô" (4★ Limited Event - Độc Đáo Mới)**:



  - *Bộ ba trang bị*: Cần Dạ Hội Bí Ngô (Damage 34.0, Stamina 210), Dây Cước Tơ Nhện Quỷ (Tension 400, Kháng đứt 6.4), Lưỡi Kẹo Ma Quái (Cá hiếm +17%, Giữ mồi +34%).



  - *Nội tại Bộ 3 món [Lễ Hội Kẹo Ngọt]*:



    * +35% Kinh nghiệm (EXP) nhận được từ mọi nguồn bắt cá.



    * +25% Vàng thưởng nhận được khi bắt cá thành công.



    * +35% Tỷ lệ xuất hiện Cá Hiếm & Cá Halloween độc quyền.



    * **Đặc quyền Lễ Hội**: Khi câu cá thành công, có **35% tỉ lệ rơi thêm 1 Kẹo Trick-or-Treat** trực tiếp vào túi đồ!



- **Set 2: "Huyết Nguyệt Thần Vương" (6★ Limited Premium - Siêu Phẩm Tối Thượng)**:



  - *Bộ ba trang bị*: Huyết Nguyệt Thần Trượng (Damage 48.0, Stamina 280), Dây Huyết Ma Cơ (Tension 520, Kháng đứt 8.5), Lưỡi Huyết Nguyệt Ma Trảo (Cá hiếm +28%, Giữ mồi +45%).



  - *Nội tại Bộ 3 món [Bá Quyền Trăng Máu]*:



    * **Hút Máu Thời Gian Thực Cân Bằng (Balanced Vampirism)**: Khi nhấn giữ kéo dây `SPACE`, hút 3% HP hiện tại của cá chuyển thành Thể lực người chơi mỗi giây (`drainPerSec * delta`). Thiết lập trần hồi phục an toàn **tối đa 12.0 Thể lực/giây** để không làm phá vỡ cơ chế thể lực khi giằng co với các Siêu Boss HP khủng (Thanatos 6,666 HP, Cá Cờ Xanh 3,000 HP).



    * **Bộc Phá Quăng Hoàn Hảo (Perfect Cast Burst)**: Khi quăng cần đạt mốc `PERFECT!`, người chơi nhận hiệu ứng nhân đôi sát thương kéo cá (**x2.0 Damage**) duy trì trong **2 giây đầu tiên** của trận đấu.



    * +45% Tỷ lệ xuất hiện Cá Hiếm & Boss.



- **6 Tuyệt Tác Đồ Họa Vector SVG & LibGDX ShapeRenderer Chuyên Nghiệp**:



  - `assets/vectors/equipment/rod_da_hoi_bi_ngo.svg` & `drawPumpkinRod`: Cần Bí Ngô hắc diện thạch, quấn dây leo cam, đèn Jack-o'-Lantern rực lửa.



  - `assets/vectors/equipment/line_da_hoi_bi_ngo.svg` & `drawSpiderSilkLine`: Cước Tơ Nhện Quỷ tím thẫm, mạng nhện 8 nan, đầu lâu ma quái.



  - `assets/vectors/equipment/hook_da_hoi_bi_ngo.svg` & `drawGhostCandyHook`: Lưỡi Kẹo Ma Quái sọc kẹo bí ngô, charm bóng ma bồng bềnh.



  - `assets/vectors/equipment/rod_huyet_nguyet.svg` & `drawHuyetNguyetRod`: Huyết Nguyệt Thần Trượng với lưỡi hái Trăng Máu uốn cong xé toạc màn đêm, đốt sống rồng gai nhọn, quả cầu Nguyệt Thực rực lửa và giọt huyết lệ rơi từ đầu trượng.



  - `assets/vectors/equipment/line_huyet_nguyet.svg` & `drawHuyetNguyetLine`: **Đại Tu Độc Bản 6★ Limited Premium**: Dây Huyết Ma Cơ thiết kế khung xương Ma Vương Gothic 4 cánh xòe nhọn hoắt 4 góc chéo, vành Bát Giác Cấm Thuật nạm 8 nanh rồng tà thần, mạng lưới tơ huyết đan chéo hình ngôi sao bát giác thiêng liêng (Sacred Octagram Blood Weave), Trái Tim Trăng Máu 4 mặt facet giác cạnh kim cương với Ma Nhãn Tà Thần có đồng tử rồng quỷ rực lửa hoàng kim và 4 mảnh huyết thạch lơ lửng.



  - `assets/vectors/equipment/hook_huyet_nguyet.svg` & `drawHuyetNguyetHook`: **Đại Tu Độc Bản 6★ Limited Premium**: Lưỡi Huyết Nguyệt Ma Trảo hình thái Bàn Tay Vuốt Quỷ Ma Vương Đoạt Hồn (Demon Monarch Soul-Reaping Talon), khoen câu tạc Vương Miện Cặp Sừng Rồng Ma Vương khổng lồ xòe hai bên bọc vàng cổ, xích ma quỷ quấn cổ khoen, thân móc là Cột Sống Cổ Long Ma Quái 4 đốt đai vàng nạm Huyết Lệ Tinh Thể với 3 ngạnh vây gai lưng ngược, vòm uốn đa lớp sắc lẹm cùng hệ thống đa ngạnh tàn bạo (mũi vuốt quỷ chính, ngạnh xé thịt bên trong, ngạnh phá giáp bên ngoài và rãnh thoát máu kép).



- **Hệ Thống Gacha Banner Chuyển Giao Tự Động (Dynamic Gacha Lifecycle)**:



  - Trước 30/10/2026: Banner Giới Hạn giữ nguyên 100% Thần Khí Lạc Hồng và Chiến Binh Lạc Hồng, Trống Đồng Đông Sơn và Chim Lạc.



  - Tuyệt đối khóa 2 Set Halloween khỏi Đài Gacha Thường (Standard Gacha) và Banner Thường vĩnh viễn.



  - Từ 30/10/2026 00:00:00 GMT+7: Tự động chuyển giao toàn diện sang Banner "BỘ HUYẾT NGUYỆT THẦN VƯƠNG" với nền Huyết Nguyệt Eclipse ma mị, Rate-up 50% 6★ Huyết Nguyệt Thần Vương, bảo lưu trọn vẹn tiến trình Pity 80 roll.



  - Modal Chi Tiết & Modal Tỉ Lệ (Loot Table) chuyển đổi nhịp nhàng sang hiển thị 2 set Halloween độc quyền.







---







## 📅 [2026-10-27] - Phiên bản v0.5.3.0 (Giai Đoạn 1: Hạ Tầng Sự Kiện Halloween, Auto-Switch Lifecycle & Dynamic UI/Shop/Attendance)







### 🎃 Giai Đoạn 1: Hạ Tầng Sự Kiện Halloween & Tự Động Chuyển Giao (HLW-01)



- **Mục tiêu**: Thiết lập cỗ máy trạng thái tự động (`EventManager.java`) quản lý vòng đời chuyển tiếp sự kiện thời gian thực giữa Sự kiện Lạc Hồng và Sự kiện Halloween 2026, bảo toàn 100% dữ liệu và giao diện Lạc Hồng cho đến đúng thời điểm kết thúc.



- **Vòng Đời Sự Kiện Thời Gian Thực (State Machine)**:



  - `LAC_HONG_ACTIVE` (Đến hết 23:59:59 ngày 28/10/2026 GMT+7): Giữ nguyên trọn vẹn chủ đề Hào Khí Lạc Hồng, Trống đồng Đông Sơn, Chim Lạc, Nhiệm vụ sự kiện, Tiệm đổi quà và Chuỗi điểm danh.



  - `PRE_HALLOWEEN_INTERMISSION` (00:00:00 đến 23:59:59 ngày 29/10/2026 GMT+7): Khóa nhiệm vụ Lạc Hồng, tự động quy đổi Điểm Sự Kiện tồn đọng sang Vàng theo tỷ lệ 1 Điểm = 1,000 Vàng, hiển thị đồng hồ đếm ngược mở Đêm Hội Halloween.



  - `HALLOWEEN_ACTIVE` (00:00:00 ngày 30/10/2026 đến 23:59:59 ngày 26/11/2026 GMT+7): Tự động chuyển đổi giao diện sang phong cách Dark Gothic Fantasy với hình mờ Jack-o'-Lantern rực lửa, đàn dơi lượn sóng và hạt ma trơi phát quang. Reset chuỗi điểm danh 25 ngày Halloween và điểm sự kiện đúng 1 lần duy nhất qua cờ lưu trữ `halloweenResetDone`.



  - `POST_EVENT_GRACE`: Đóng sự kiện và thu hồi tài nguyên an toàn.



- **Giao Diện Động & Tiệm Đổi Quà (Dynamic UI & Shop)**:



  - Header linh hoạt: "HÀO KHÍ LẠC HỒNG" vs "ĐÊM HỘI HALLOWEEN 2026".



  - Cửa Hàng Sự Kiện: Tự động đổi vật phẩm sang Bộ Trang Bị Giới Hạn "Dạ Hội Bí Ngô" (Cần Dạ Hội Bí Ngô 4★, Dây Cước Tơ Nhện Quỷ 4★, Lưỡi Kẹo Ma Quái 4★) kèm Túi Vàng Ma Quái và Vé Gacha.



  - Chuỗi Điểm Danh 25 Ngày: Phần thưởng theo từng ngày linh hoạt theo sự kiện đang diễn ra, với Đại mốc Ngày 25 trao tặng Cần Dạ Hội Bí Ngô (Limited 4★).



- **Đồ Họa Vector Mới (EquipmentIconRenderer.java)**:



  - `drawPumpkinRod`: Cần Dạ Hội Bí Ngô với thân gai hắc diện thạch quấn dây leo cam, đầu cần gắn đèn bí ngô Jack-o'-Lantern mini phát quang, khuyên vàng đính ngọc.



  - `drawSpiderSilkLine`: Cước Tơ Nhện Quỷ với ổ cước tím thẫm, mạng nhện hình học 8 nan bạc và phù hiệu đầu lâu ma quái.



  - `drawGhostCandyHook`: Lưỡi Kẹo Ma Quái sọc kẹo Halloween uốn cong sắc lẹm, charm bóng ma mini bay bổng.



  - `drawPumpkinLantern`: Đèn Lồng Bí Ngô Jack-o'-Lantern 3 lớp múi tròn, cuống xanh, mặt chạm khắc rực lửa vàng.



  - `drawTrickOrTreatCandy`: Kẹo Halloween gói hai đầu cánh bướm tím-vàng, thân xoắn ốc cam ma mị.



- **An Toàn Bộ Nhớ & Tối Ưu LibGDX**:



  - Không phân bổ đối tượng trên Heap trong vòng lặp `render()`. Tái sử dụng `Color`, `Vector3`, `ShapeRenderer` dùng chung.







---







## 📅 [2026-10-27] - Phiên bản v0.5.2.4 (Hoàn Thành Bước 2.3 - 15 Loài Cá Vector Đảo Xa & Tuyệt Tác Siêu Boss Cá Cờ Xanh Khổng Lồ)







### 🌊 Bước 2.3: Chuẩn Hóa 15 Loài Cá Vector Đảo Xa & Tuyệt Tác Siêu Boss Cá Cờ Xanh ([FishVisualRenderer.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/FishVisualRenderer.java))



- **Mục tiêu**: Nghiệm thu và chuẩn hóa toàn diện đồ họa vector LibGDX ShapeRenderer đa lớp, ánh sáng biển sâu và hoạt ảnh cho toàn bộ 15 loài cá Đảo Xa hoàn toàn mới (`FSH-02C`), đặc biệt đại tu Siêu Boss Cá Cờ Xanh Khổng Lồ thành tuyệt tác đồ họa:



  - **6 Loài Cá Thường (Common)**:



    - `Cá Mú Đỏ`: Thân bầu dục đậm đà màu đỏ san hô rực rỡ, phủ kín hàng chục đốm sao xanh ngọc bích lấp lánh, vây đuôi quạt tròn viền xanh neon, miệng rộng hàm bẹt dữ dằn.



    - `Cá Hồng Chuối`: Thân thon dài màu hồng phấn - hồng cam tươi tắn, dải sọc vàng chuối óng ả chạy dọc từ sau mắt tới cuống đuôi, vây đuôi xẻ thùy vàng chanh rực rỡ.



    - `Cá Dìa Biển Đảo`: Thân dẹt hình bầu dục màu nâu ô-liu lấm tấm hoa văn cẩm thạch hoa đá biển khơi, vây lưng và vây hậu môn có hàng gai nhọn tẩm độc bảo vệ (`gai_doc_san_ho`), mõm nhỏ như miệng thỏ gặm san hô.



    - `Cá Nhồng Biển`: Thân thuôn dài ngư lôi như mũi tên bạc xé nước, lưng xám xanh ánh thép, 12 vạch sọc vằn đứng sẫm màu, hàm dưới chìa dài lộ răng nanh tam giác nhọn hoắt như dao găm (`can_day`), vây đuôi chữ V cắt nước cực nhanh (`wagFine`).



    - `Cá Bè Trang`: Thân dẹt sâu hình thoi bằng chrome bạc sáng chói, hàng 6 đốm đen tròn to như đồng tiền cổ chạy trên đường bên uốn lượn, vây đuôi xẻ sâu nhọn hoắt tạo lực bơi xé sóng.



    - `Cá Nục Heo (Mahi-Mahi / Dorado)`: Trán gồ vuông góc dốc thẳng đứng kiêu hãnh của dòng cá đực, lưng xanh ngọc lục bảo rực rỡ, mạn sườn chuyển vàng chanh phát quang chấm xanh điện tử, vây lưng dải lụa xanh biếc chạy suốt sống lưng, bơi bứt tốc đổi hướng (`doi_huong`).



  - **4 Loài Cá Hiếm (Uncommon)**:



    - `Cá Bớp Khơi`: Thân ngư lôi cơ bắp cuồn cuộn tròn trịa màu nâu đen espresso sẫm tuyền (`tro_luc`), 2 dải sọc trắng kem song song hoàn hảo chạy từ mang tới cuống đuôi, hàng 8 gai ngắn rời rạc cứng như đinh thép.



    - `Cá Cam Biển`: Thân dày khỏe bắp thịt chắc nịch, lưng xanh ô-liu ánh đồng, mạn sườn vàng cam hổ phách rực rỡ, vệt sọc sẫm màu đặc trưng vắt chéo từ mõm qua mắt lên đỉnh gáy, bơi dũng mãnh nương theo hải lưu xiết (`nuong_hai_luu`).



    - `Cá Thu Ngàng (Wahoo)`: Thân dài thanh mảnh như thanh trường đao ("Thân Đao Xé Gió"), lưng xanh coban sapphire đậm, 24-28 sọc vằn lam sẫm hổ vằn biển sâu, hàng 8 vây phụ nhỏ vàng chói ở cuống đuôi, vây đuôi lưỡi liềm đối xứng xé gió bạt mạng (`wagFine`).



    - `Cá Đuối Hoa Cương`: Đĩa thân tròn rộng xòe như cánh diều ngầm lướt đáy biển, phủ kín hoa văn cẩm thạch xoáy đá hoa cương mỹ thuật, đuôi roi dài có gai ngạnh độc phóng điện (`phong_dien`), tia điện cyan phát quang crackling theo nhịp timer.



  - **2 Loài Cá Quý (Rare)**:



    - `Cá Ngừ Vây Vàng`: Thân ngư lôi khổng lồ cơ bắp tròn vo xanh thẫm kim loại, vây lưng thứ hai và vây hậu môn hình lưỡi liềm vàng chanh rực rỡ vươn dài cong vút, hàng vây phụ tam giác vàng tươi viền đen tạo xoáy nước áp lực lớn (`xoay_nuoc`), mắt vàng sáng quắc.



    - `Cá Cờ Gòn (Sailfish)`: Mũi kiếm nhọn dài thanh mảnh, cánh buồm vây lưng vĩ đại cao chót vót màu xanh coban phủ đầy hàng chục đốm tròn đen óng ánh ("Vũ Điệu Cờ Buồm"), vây bụng dài như hai dải lụa mỏng buông thõng, vây đuôi trăng khuyết mềm mại.



  - **2 Loài Sử Thi (Epic)**:



    - `Cá Mú Nghệ Khơi (Giant Grouper)`: Thân hình đồ sộ mập mạp bọc lớp giáp đá cổ thạch sừng sững (`than_ho_co_thach`), các đốm đen hoa mai lớn phủ kín thân và vây, chiếc miệng vực thẳm khổng lồ nuốt chửng mọi thứ (`vuc_da_nuot_chung`), hào quang bụi đá trầm tích xoay quanh thân.



    - `Cá Mập Rạn Đen (Blacktip Reef Shark)`: Kẻ săn mồi đỉnh cao với thân hình khí động học xám nâu ánh đồng, bụng trắng muốt tương phản sắc nét, đỉnh chóp của TẤT CẢ các vây đều có vệt đen tuyền viền trắng nổi bật đặc trưng, 5 khe mang sắc lẹm, đôi mắt vàng thính mùi máu (`thinh_mui_mau`), hàm răng tam giác lởm chởm cắn xé điên cuồng (`can_xe_dien_cuong`).



  - **1 Siêu Boss Thế Giới (World Boss - 3,000 HP, Lực kéo 195.0, 260 kg)**:



    - `Cá Cờ Xanh Khổng Lồ (Blue Marlin - Tuyệt Tác Siêu Boss)`:



      - **Hào quang thần thoại 3 tầng**: Vòng tối tím vực thẳm ngoài cùng, sóng xung kích cobalt đại dương ở giữa, vầng sáng cyan điện tử shimmer bên trong phát xung theo nhịp `pulse` & `fastPulse`.



      - **Mũi Kiếm Thép Xé Biển (`mui_kiem_xe_bien`)**: Lưỡi kiếm dài đâm xuyên đại dương với rãnh năng lượng plasma điện tử chạy dọc thân kiếm và tia lửa điện lóe sáng chớp nháy ở đầu mũi kiếm.



      - **Cánh Buồm Lam Ngọc Vĩ Đại (`vu_dieu_luot_song`)**: 10 tia vây buồm vươn cao ngạo nghễ với màng vây xanh ngọc bích phát quang rực rỡ, chóp vây đính hạt ngọc lấp lánh.



      - **Thân Hình Ngư Lôi Bạo Chúa**: Lưng xanh navy hoàng gia, mạn sườn chuyển bạc xà cừ, 7 dải sọc xanh neon điện tử rực sáng chớp nháy.



      - **Vây Đuôi Trăng Khuyết Khổng Lồ**: Hai thùy đuôi vút dài uy mãnh xé toạc đại dương với các tia trợ lực vàng ngọc.



      - **Mắt Thần Săn Mồi (`cuong_phong_ngu_kiem`)**: Đôi mắt vàng kim rực lửa uy dũng, đồng tử sắc lạnh nhìn thấu tâm can cần thủ, kèm đốm sáng kim cương và tia sáng ngọc lục bảo.



      - **Hiệu Ứng Bọt Khí Cavitation**: Bọt khí bão tố khí động học và tia sóng áp lực tỏa ra quanh thân khi bơi.







### 📦 Đóng Gói Bản Cài Đặt Phát Hành ([AnglerRPG-v0.5.2.4.zip](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/AnglerRPG-v0.5.2.4.zip))



- Build lại tệp nhị phân thực thi game độc lập `AnglerRPG.jar` mới nhất (43.4 MB) thông qua tác vụ `gradlew lwjgl3:jar`.



- Đóng gói trọn gói vào tệp zip: `AnglerRPG-v0.5.2.4.zip` (42.9 MB) gồm `AnglerRPG.jar` và kịch bản khởi chạy 1-click `Launcher.bat`.



- Dọn dẹp và xóa bỏ tệp zip bản phát hành cũ (`AnglerRPG-v0.5.1.1.zip`).







---







## 📅 [2026-10-27] - Phiên bản v0.5.2.3 (Hoàn Thành Bước 2.2 - 15 Loài Cá Vector Bãi Biển & Siêu Boss Bạch Tuộc Khổng Lồ)







### 🐙 Bước 2.2: Chuẩn Hóa 15 Loài Cá Vector Bãi Biển & Siêu Boss Bạch Tuộc Khổng Lồ ([FishVisualRenderer.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/FishVisualRenderer.java))



- **Mục tiêu**: Nghiệm thu và chuẩn hóa toàn diện đồ họa vector LibGDX ShapeRenderer đa lớp, specular bóng sáng đại dương, vảy cá phản quang và cử động quẫy đuôi/xúc tu uyển chuyển cho 15 loài cá Bãi Biển (`FSH-02B`):



  - **6 Loài Cá Thường (Common)**:



    - `Cá Chỉ Vàng`: Thân thoi bạc ánh xanh biển, dải sọc vàng kim óng ả kéo dài từ mang đến cuống đuôi, đốm đen nắp mang đặc trưng, gai vảy cứng đường bên, vây đuôi xòe vàng chanh.



    - `Cá Đục`: Thân thon dài vàng cát ngụy trang đáy biển nông, vảy óng ánh chấm nâu li ti, mõm dài nhọn sục cát tìm mồi.



    - `Cá Cơm`: Thân trong suốt như pha lê, dải chỉ bạc dọc sống lưng phản chiếu lấp lánh, vây đuôi quẫy nhanh (`wagFine`).



    - `Cá Mòi`: Lưng xanh thẫm ánh thép, hàng 5 chấm đen tròn dọc mạn sườn, vảy bạc mềm mại, vây lưng tam giác nhọn.



    - `Cá Lẹp`: Thân dẹt mỏng như tờ giấy lướt sóng, bụng sắc cạnh như lưỡi dao, vảy trắng bạc phản quang.



    - `Cá Bạc Má`: Mắt to tròn thao thức, lưng xanh lục rằn ri vằn sóng biển, thân thon chắc nịch của dòng họ cá thu ngừ bơi nhanh.



  - **4 Loài Cá Hiếm (Uncommon)**:



    - `Cá Nục`: Thân thuôn tròn chắc nịch, đường bên uốn lượn phủ giáp vảy cứng (scutes), vây đuôi cánh én vàng tươi.



    - `Cá Đối`: Đầu dẹt mõm tù môi dày, vảy tròn to xám bạc viền đậm, mắt to lấp lánh màng mỡ bảo vệ.



    - `Cá Trích`: Thân dẹp dài xanh thẫm lưng, bụng trắng bạc óng ánh, vảy tròn lấp lánh phản xạ ánh mặt trời.



    - `Cá Chim Biển`: Thân dẹt hình đĩa tròn màu bạc ngọc trai kiêu sa, vây lưng và vây hậu môn cong dài như lưỡi liềm.



  - **2 Loài Cá Quý (Rare)**:



    - `Cá Mú`: Thân đầm chắc hoa văn đốm hoa đá (honeycomb) ngụy trang rạn san hô, miệng rộng răng sắc nhọn, vây lưng gai tua tủa.



    - `Cá Bóp`: Thân ngư lôi màu nâu đen khỏe khoắn, hai dải sọc trắng bạc song song nổi bật chạy dọc thân, vây lưng trước cụt gai sắc.



  - **2 Loài Cá Sử Thi (Epic)**:



    - `Cá Thu Hoàng Kim`: Hào quang hoàng kim bạo chúa xé biển, thân ngư lôi xanh sapphire bóng bẩy, 8 đôi vây phụ rời vàng chói dọc cuống đuôi, vây đuôi trăng khuyết khổng lồ vàng rực 24K.



    - `Cá Ngừ Đại Dương`: Thân bắp thịt hình thoi khổng lồ xanh thẫm - bạc, các vây phụ nhọn vàng tươi phát quang, mắt vàng săn mồi sắc lẹm, lực bơi bạt mạng.



  - **1 Siêu Boss Thế Giới (Boss - 2,200 HP)**:



    - `Bạch Tuộc Khổng Lồ`: Xoáy mực vực thẳm và vòng năng lượng cyan-tím đa tầng (`fastPulse`), 8 xúc tu gai nhọn khổng lồ quằn quại bọc giác mút đỏ thẫm phát sáng, mắt quỷ tím với đồng tử chữ nhật ma quái, mắt phụ phát quang thái dương, mỏ quái vật sừng đen chitin tử thần và giọt mực độc ma quỷ.







---







## 📅 [2026-10-27] - Phiên bản v0.5.2.2 (Hoàn Thành Bước 2.1 - 15 Loài Cá Vector Ao Làng & Tái Cấu Trúc Lộ Trình Đồ Họa)







### 🐟 Bước 2.1: Chuẩn Hóa 15 Loài Cá Vector Ao Làng & Siêu Boss Thủy Quái Sông Trà ([FishVisualRenderer.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/FishVisualRenderer.java))



- **Mục tiêu**: Nghiệm thu và chuẩn hóa toàn diện đồ họa vector LibGDX ShapeRenderer đa lớp, tạo khối nổi khối mềm mại, vệt sáng specular glint và cử động quẫy đuôi hình sin (tail wagging) cho 15 loài cá Ao Làng (`FSH-02A`):



  - **4 Loài Cá Thường (Common)**:



    - `Cá Rô Đồng`: Thân ô-liu ánh đồng, vây lưng 9 gai nhọn sắc bén, đốm đen nắp mang, vệt vằn ngụy trang.



    - `Cá Diếc`: Lưng vồng cao, vảy trắng bạc ngọc trai lấp lánh, vây phớt hồng cam dịu dàng.



    - `Cá Mương`: Thân thon dài trong suốt như lưỡi liềm, vảy bạc phản chiếu ánh nắng mặt hồ.



    - `Cá Lóc Nhí`: Lưng hoa văn rằn ri ngụy trang cỏ nước, mắt lanh lợi sắc sảo tìm mồi.



  - **3 Loài Cá Hiếm (Uncommon)**:



    - `Cá Chép`: Lưng gồ uy dũng ánh cam hoàng kim, vảy xếp lớp giáp rồng, đôi râu mềm mại rước lộc, vây đuôi đỏ son.



    - `Cá Mè`: Đầu to béo tròn, miệng hướng lên đớp bọt nước, vảy xám bạc nhạt bơi đàn tầng mặt.



    - `Cá Trê`: Thân trơn bóng đen nâu nhẵn thín, 4 cặp râu dài linh hoạt sắc bén, gai ngạnh sắc.



  - **3 Loài Cá Quý (Rare)**:



    - `Cá Trôi`: Thân thuôn dài khỏe khoắn, vảy xanh lam đậm viền trắng sữa, vây ngực viền đỏ.



    - `Cá Trắm Cỏ`: Thân trụ tròn cơ bắp, vảy trám sẫm sắc nét viền ánh lục, vây bụng khỏe.



    - `Cá Chim Trắng`: Thân dẹt hình đĩa tròn độc đáo, vảy bạc óng ánh, vây bụng ửng đỏ cam rực rỡ.



  - **4 Loài Cá Sử Thi (Epic)**:



    - `Cá Lóc Bông`: Thân dài lực lưỡng, hoa văn đốm hoa mai nâu đen báo đốm săn mồi nước ngọt.



    - `Cá Trê Lai`: Thân to kềnh càng rằn ri hổ vằn, ngạnh độc phát quang sắc thép.



    - `Cá Chép Vàng`: Vảy hoàng kim phát quang rực rỡ, vây lụa thướt tha mang lại may mắn thần tài.



    - `Cá Lăng Khổng Lồ`: Thân xám tro bóng loáng, đầu bẹt sát thủ săn mồi đáy sâu, râu xúc giác dài.



  - **1 Siêu Boss Thế Giới (Boss - 850 HP)**:



    - `Thủy Quái Sông Trà`: Thân khổng lồ phủ lớp vảy rêu cổ phù sa, đôi mắt quỷ dung nham đỏ rực phát sáng, vây lưng gai sừng nhọn uy dũng cuộn sóng dữ.







### 🗺️ Tái Cấu Trúc Lộ Trình Phát Triển Đồ Họa 6 Giai Đoạn ([TASKS.md](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/TASKS.md), [PatchNotesDialog.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/PatchNotesDialog.java))



- **Tách Bước 2 thành 3 giai đoạn độc lập chuyên sâu về cá**:



  - `Bước 2.1` (FSH-02A): 15 loài cá Ao Làng & Siêu Boss Thủy Quái Sông Trà (✅ Hoàn thành v0.5.2.2).



  - `Bước 2.2` (FSH-02B): 15 loài cá Bãi Biển & Siêu Boss Bạch Tuộc Khổng Lồ (📋 Kế hoạch Bước 2.2).



  - `Bước 2.3` (FSH-02C): 15 loài cá Đảo Xa & Siêu Boss Cá Cờ Xanh Khổng Lồ (📋 Kế hoạch Bước 2.3).



- **Di chuyển Môi Trường Nước & Khúc Xạ Ánh Sáng (`ENV-02`)**: Đưa ra khỏi Bước 2, chuyển thành `Giai Đoạn 3 (v0.5.3)` riêng biệt để hoàn thiện mặt nước sóng sánh, rạn san hô và tia sáng God Rays trên cả 3 bản đồ.



- **Dịch chuyển các Giai đoạn tiếp theo**: Giai đoạn 4 (UI & Nút Bấm Xúc Giác v0.5.4), Giai đoạn 5 (VFX Chiêu Thức & Boss v0.5.5), Giai đoạn 6 (Hoạt Ảnh Vật Lý & Gacha CS:GO v0.5.6).







---







## 📅 [2026-10-27] - Phiên bản v0.5.2.1 (Phân Trang Quick Slots, Sửa Lỗi Kính Sonar & Hoàn Thiện Bộ Sưu Tập Đảo Xa)







### 🎒 Hệ Thống Phân Trang Quick Slots ([GameHUD.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/GameHUD.java), [PlayScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/PlayScreen.java))



- **Nguyên nhân gốc rễ**: Giao diện Quick Slots trước đây chỉ vẽ cố định tối đa 9 ô (3x3). Khi người chơi sở hữu từ 10 loại vật phẩm trở lên (do mua thêm ở tiệm hoặc câu trúng loot tiêu hao), các ô thứ 10+ bị vẽ tràn ra ngoài màn hình (`y < 0`), không thể bấm chuột hay gán phím nóng 1-9, khiến người chơi lầm tưởng không mua được hoặc không tính đồ.



- **Giải pháp xử lý**:



  - Tích hợp cơ chế phân trang `quickSlotPage` 9 ô/trang với bộ đệm danh sách không cấp phát bộ nhớ rác (`cachedValidInventory`, `cachedPageItems`).



  - Bổ sung nút chuyển trang `[◀]` và `[▶]` cùng tiêu đề động `"Ô NHANH (Trang X/Y)"` trên thanh tiêu đề khi sở hữu trên 9 món đồ.



  - Đồng bộ chuẩn xác phím tắt 1-9: Phím tắt số `1` đến `9` và click chuột sẽ kích hoạt đúng các vật phẩm đang hiển thị trên trang hiện tại.







### 🔍 Sửa Lỗi Kính Nhìn Xuyên Nước (Sonar) & Tinh Chỉnh Mồi Thơm ([FishingManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/FishingManager.java), [GameHUD.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/GameHUD.java))



- **Sửa Lỗi Radar Quét Khi Đã Hết Lượt (0 Lần)**:



  - Khắc phục lỗi trong `triggerBite()` tự động gán `predictedCatch` cho cá cắn câu khiến `GameHUD` hiển thị bảng radar thấu thị ngay cả khi số lượt của Kính đã về 0.



  - Khóa chặt điều kiện hiển thị bảng Radar thấu thị và vòng quét sóng trên HUD: Chỉ hiển thị khi `player.getSonarCharges() > 0`.



- **Bảo Toàn Lượt Thấu Thị Khi Để Cá Chạy Thoát**:



  - Số lượt của Kính Nhìn Xuyên Nước **không bị trừ** nếu người chơi không kéo cá mà để cá chạy đi (`"CHẬM TAY!"`). Lượt thấu thị chỉ bị tiêu hao khi người chơi thực sự giật cần vào giao tranh (`startEncounter()`) hoặc câu thành công.



- **Tiêu Hao Mồi Thơm Khi Cá Đớp Hụt**:



  - Khi cá cắn câu nhưng người chơi chậm tay không kéo cá (`"CHẬM TAY!"`), Mồi Thơm tự động bị hủy (`player.setBaitBuffActive(false);`) do cá đã ăn mất mồi rồi bỏ chạy.







### 📚 Làm Lại 5 Bộ Sưu Tập Sổ Tay Đảo Xa & Xóa Dấu Vết Cá Cũ ([CollectionDatabase.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/data/CollectionDatabase.java), [CatalogScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/CatalogScreen.java))



- **Tái Thiết Kế 5 Collection Đảo Xa**:



  - `dx_ran_san_ho` (Rạn San Hô Đảo Xa): Cá Mú Đỏ, Cá Hồng Chuối, Cá Dìa Biển Đảo (1,500 Vàng, 800 XP, 25 KC).



  - `dx_kinh_ngu_nong` (Kình Ngư Nước Nông): Cá Nhồng Biển, Cá Bè Trang, Cá Nục Heo (2,500 Vàng, 1,200 XP, 35 KC).



  - `dx_dong_chay_khoi` (Dòng Chảy Khơi Xa): Cá Bớp Khơi, Cá Cam Biển, Cá Thu Ngàng, Cá Đuối Hoa Cương (5,000 Vàng, 2,500 XP, 50 KC).



  - `dx_chien_ham_dai_duong` (Chiến Hạm Đại Dương): Cá Ngừ Vây Vàng, Cá Cờ Gòn, Cá Mú Nghệ Khơi, Cá Mập Rạn Đen (10,000 Vàng, 5,000 XP, 75 KC).



  - `dx_huyen_thoai_bien_sau` (Huyền Thoại Biển Sâu): Cá Cờ Xanh Khổng Lồ (25,000 Vàng, 12,000 XP, 120 KC).



- **Cập Nhật Lore & Xóa Sạch Dữ Liệu Cũ**:



  - Bổ sung 15 đoạn lore sinh thái học độc đáo cho 15 loài cá mới của Đảo Xa trong `CatalogScreen.java`.



  - Quét sạch toàn bộ tên gọi và vết tích của 15 loài cá cũ khỏi toàn bộ hệ thống code và tài liệu.







### 🗺️ Hoán Đổi Lộ Trình Đồ Họa ([TASKS.md](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/TASKS.md), [PatchNotesDialog.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/PatchNotesDialog.java))



- Hoán đổi Bước 3 (Ngoại hình 45 loài cá vector & Môi trường 3 bản đồ) lên trước Bước 2 (Giao diện UI & Game Juice) theo đúng yêu cầu phát triển.







---







## 📅 [2026-10-27] - Phiên bản v0.5.2.0 (Đại Tu Đảo Xa, Siêu Boss Cá Cờ Xanh 3,000 HP & Cơ Chế Perfect Re-Hook)







### 🌊 Đại Tu Hệ Sinh Thái Đảo Xa Thực Tế ([fishes.json](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/data/fishes.json), [FishDatabase.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/data/FishDatabase.java))



- **Loại Bỏ Hoàn Toàn Các Loài Viễn Tưởng**:



  - Khai trừ toàn bộ các loài thần thoại/quá đà (Cá Nhà Táng, Mực Khổng Lồ, Thần Biển Leviathan 5,500 HP).



  - Thay thế bằng **15 loài cá biển khơi thực tế** theo chuẩn Big Game Fishing ngoài đảo xa, máu và lực kéo được tái cân bằng hoàn hảo theo độ khó VỪA (2 sao):



    - **6 Cá Thường (COMMON - 260 đến 360 HP, Lực kéo 42-55)**: Cá Mú Đỏ, Cá Hồng Chuối, Cá Dìa Biển Đảo, Cá Nhồng Biển, Cá Bè Trang, Cá Nục Heo.



    - **4 Cá Hiếm (UNCOMMON - 520 đến 650 HP, Lực kéo 72-85)**: Cá Bớp Khơi, Cá Cam Biển, Cá Thu Ngàng, Cá Đuối Hoa Cương.



    - **2 Cá Quý (RARE - 1,050 đến 1,150 HP, Lực kéo 115-125)**: Cá Ngừ Vây Vàng, Cá Cờ Gòn (Sailfish).



    - **2 Sử Thi (EPIC - 1,750 đến 1,900 HP, Lực kéo 150-160)**: Cá Mú Nghệ Khơi (Giant Grouper), Cá Mập Rạn Đen (Blacktip Reef Shark).



    - **1 Siêu Boss Thế Giới (BOSS - 3,000 HP, Lực kéo 195.0, Cân nặng 260 kg)**: **Cá Cờ Xanh Khổng Lồ (Blue Marlin)**.







### ⚔️ Chuẩn Hóa Phân Tầng Kỹ Năng & Cơ Chế Perfect Re-Hook ([FishingManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/FishingManager.java), [Fish.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/entities/Fish.java))



- **Cơ Chế Phản Xạ Đỉnh Cao: Perfect Re-Hook**:



  - Khi cá tung mình nhảy lên không trung, lực căng dây tụt về 0. Khi cá sắp chạm mặt nước (<= 0.35s), xuất hiện chỉ báo nhấp nháy `"▼ TIẾP NƯỚC! [SPACE] ▼"`.



  - **Re-Hook Thành Công**: Người chơi buông phím trong lúc bay và nhấp lại phím `Space` đúng khoảnh khắc chạm nước ➔ Kích hoạt `"PERFECT RE-HOOK!"` (vàng kim), gây **x2 sát thương kéo** và triệt tiêu toàn bộ lực sốc.



  - **Sốc Lực Trừng Phạt**: Nếu người chơi vẫn đè giữ phím kéo khi cá tiếp nước ➔ Cú va đập làm giật tăng sốc **+35% Tension** và trừ thể lực (`"SỐC LỰC TIẾP NƯỚC!"`), trừng phạt thói quen đè giữ phím liên tục.



- **14 Kỹ Năng Độc Đáo Mới Mẻ Từ Hạng Epic Trở Xuống**:



  - `tro_luc` (Cá Bớp Khơi): Lớp da lì lợm có **25% tỷ lệ miễn nhiễm hoàn toàn với lực kéo từ phím Space** (hiển thị `"TRƠ LỰC!"`).



  - `can_day` (Cá Nhồng Biển): Răng nanh sắc lẹm, 25% tỷ lệ khi PULLING cắn mòn giảm 10% Max Tension an toàn trong 4s.



  - `gai_doc_san_ho` (Cá Dìa): Gai độc cọ xát trừ 1.5 Thể lực/s khi cần thủ kéo dây lúc cá PULLING.



  - `doi_huong` (Cá Nục Heo): Bơi lượn đổi góc làm kim đo Tension dao động chao đảo ±10% (1.5s).



  - `nuong_hai_luu` (Cá Cam Biển): Khi Tension dâng trên 55%, lực kéo của cá tự tăng thêm +18%.



  - `than_dao` (Cá Thu Ngàng): Thân mỏng xé nước tăng 35% tốc độ bơi khi PULLING nhưng chu kỳ ngắn hơn.



  - `phong_dien` (Cá Đuối Hoa Cương): Giữ phím kéo Space liên tục quá 2.5s sẽ bị giật điện trừ 12 Thể lực.



  - `xoay_nuoc` (Cá Ngừ Vây Vàng - 50% HP): Tạo xoáy nước đại dương làm Tension chao đảo theo sóng sin và giảm 40% tốc độ xả chùng dây (3.5s).



  - `vu_dieu_co_buom` (Cá Cờ Gòn - 50% HP): Phóng lướt 2 nhịp liên tiếp trên mặt sóng (0.8s mỗi nhịp), tiếp nước tăng +20% Tension.



  - `than_ho_co_thach` (Cá Mú Nghệ Khơi): Giảm trực tiếp **-15 sát thương phẳng** mỗi đòn kéo dây.



  - `vuc_da_nuot_chung` (Cá Mú Nghệ Khơi - 50% HP): Hút xoáy đáy ngầm giật +35% Tension và rút 8 Thể lực/s nếu cố tình kéo (3.5s).



  - `thinh_mui_mau` (Cá Mập Rạn Đen): Khi Thể lực cần thủ dưới 50%, cá mập tăng +20% lực kéo.



  - `can_xe_dien_cuong` (Cá Mập Rạn Đen - 50% HP): Cắn xé làm giảm 25% Max Tension an toàn trong 5s, phản chấn trừ 20 Thể lực nếu đang giữ dây.







### 👑 Bộ 3 Kỹ Năng Độc Bản Của Boss Cá Cờ Xanh Khổng Lồ ([FishingManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/FishingManager.java))



- **Passive Độc Bản: Mũi Kiếm Xé Biển (`mui_kiem_xe_bien`)**:



  - Miễn nhiễm hoàn toàn các hiệu ứng khống chế (Trói buộc, Đóng băng).



  - Khắc chế đè phím: Ghìm giữ phím Space > 1.5s làm Tension dâng nhanh thêm +20%/s mỗi giây ghìm tiếp theo.



  - Thưởng nhấp nhả nhịp điệu (Tap-reeling): Nhấp nhả phím kéo <= 1.2s được thưởng **+15% sát thương kéo**.



- **Active 1: Vũ Điệu Lướt Sóng (`vu_dieu_luot_song`)**:



  - Tần suất định kỳ **8.0 giây** một lần trong suốt trận đấu. Phóng vút lên không trung **0.9 giây** chớp nhoáng thử thách phản xạ Re-Hook của cần thủ.



- **Active 2: Cuồng Phóng Ngư Kiếm (`cuong_phong_ngu_kiem`) - Soft Enrage**:



  - Kích hoạt khi Boss dưới 30% HP (Giai đoạn Sinh Tử).



  - Lũy tiến lực kéo: Mỗi nhịp bứt tốc nhận thêm **+10% lực kéo**, cộng dồn tối đa **+50%** (lên tới 292.5 pull).



  - Bào mòn thể lực theo thời gian: Cứ mỗi 1 giây trôi qua sau khi kích hoạt chiêu, tốc độ tiêu hao thể lực của cần thủ **tăng thêm +1%** (chạy đua sinh tử).



  - Tự động **reset sạch sẽ** toàn bộ trạng thái và bộ đếm ngay khi kết thúc trận đấu.







### 🎨 Đồ Họa Vector Boss Khơi Xa ([FishVisualRenderer.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/FishVisualRenderer.java))



- Bổ sung hàm vẽ độc quyền `drawCaCoXanhKhongLo`: Thân ngư lôi màu xanh coban hoàng gia bọc vảy bạc phản quang, vây cờ buồm cao vút đón gió, dãy sọc Neon Cyan phát sáng và mũi kiếm đao thép dài sắc lẹm xé toạc đại dương.







---







## 📅 [2026-10-27] - Phiên bản v0.5.1.2 (Nerf Cá PULLING, Sửa Auto-Updater, Giới Hạn Sách Cửa Hàng & Chống Lãng Phí Buff)







### ⚖️ Cân Bằng Chiến Đấu & Lực Kéo Cá ([FishingManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/FishingManager.java), [PlayScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/PlayScreen.java), [combat_simulator.py](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/scripts/balance/combat_simulator.py))



- **Nerf Sức Kéo Của Cá Khi Buông Dây Trong Trạng Thái Vùng Vẫy (PULLING)**:



  - *Vấn đề*: Khi cá vùng vẫy (`PULLING`), nếu người chơi buông phím Space, cá vẫn gây tới 80% lực kéo (`pullMult = 0.8f`), khiến Tension tăng rất nhanh hoặc không kịp xả nếu đối đầu với cá khỏe.



  - *Giải pháp*: Giảm hệ số lực kéo khi buông cần (`!isSpaceDown`) xuống **`0.4x`** (giảm 50% lực kéo), tạo khoảng thở chiến thuật để cần thủ xả bớt Tension an toàn trước khi cá chuyển nhịp.



- **Giảm Rung Màn Hình (Screen Shake Trauma) Khi Tension Tăng Cao**:



  - Giảm tốc độ tích lũy chấn động camera từ `+0.04f/frame` xuống `+0.01f/frame`, chỉ bắt đầu kích hoạt khi Tension vượt ngưỡng 88% và khống chế rung tối đa ở mức nhẹ (`trauma <= 0.16f`), triệt tiêu hoàn toàn cảm giác giật lắc gây mỏi mắt.



- **Bổ Sung Cảnh Báo Trực Quan & Chuẩn Hóa Chữ Nổi Khi Đứt Dây Câu**:



  - Khi Tension chạm trần 100%, hệ thống hiển thị thông báo `"DÂY CƯỚC BỊ ĐỨT, CÁ ĐÃ CHẠY MẤT!"` (đã bỏ tiền tố `"CĂNG DÂY TỐI ĐA!"` tránh rườm rà) kèm dòng chữ đỏ bay nổi `"ĐỨT DÂY CÂU!"`.



- **Bổ Sung Chữ Nổi Khi Cá Vừa Cắn Câu (Dấu ! Xuất Hiện)**:



  - Khi cá đớp phao, hệ thống tự động kiểm tra loại cá: Nhảy chữ nổi màu đỏ `"BOSS!"` nếu là cá Boss, và nhảy chữ nổi vàng kim `"NEW!"` nếu là loài cá chưa từng câu được bao giờ trong Sổ Tay Thủy Tộc.



- **Sửa Lỗi Chữ Nổi Khi Hụt Cá (Bite Timeout)**:



  - Khắc phục lỗi nhảy nguyên cả câu dài `"CHẬM TAY RỒI! CÁ ĐÃ ĂN HẾT MỒI VÀ CHẠY MẤT!"` làm đè chữ nổi, tinh giản thành một chữ nổi duy nhất `"CHẬM TAY!"`.







### 🏪 Tạp Hóa & Kinh Tế Sách Kỹ Năng ([ShopScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/ShopScreen.java), [PlayerEconomy.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/entities/PlayerEconomy.java))



- **Giới Hạn Số Lượng Mua Sách Kỹ Năng**:



  - Đặt công thức chuẩn cho số sách tối đa được phép mua: `Số Lượng Cho Phép = Cấp Tối Đa Kỹ Năng - Cấp Hiện Tại - Số Sách Đang Có Trong Túi`.



  - Khi số lượng cho phép `<= 0`, vật phẩm tự động chuyển sang trạng thái `"ĐÃ BÁN HẾT"`.



- **Loại Bỏ Sách Đã Bán Hết Khỏi Sự Kiện Giảm Giá**:



  - Thuật toán làm mới giảm giá tiệm tạp hóa (`PlayerEconomy.refreshDiscounts`) tự động lọc bỏ các sách kỹ năng đã bán hết / đạt cấp tối đa, đảm bảo chỉ giảm giá các món đồ người chơi thực sự còn nhu cầu mua.







### 🧪 Vật Phẩm & Hiệu Ứng Trạng Thái ([PlayScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/PlayScreen.java), [GameHUD.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/GameHUD.java), [ItemDatabase.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/data/ItemDatabase.java))



- **Nút BUFF Tích Hợp Trên Thanh Trạng Thái & Tooltip Xem Chi Tiết**:



  - Dời vị trí nút Mồi Thơm cũ sang vị trí thoáng đãng hơn (`x=438px`), tinh gọn thành một nút duy nhất `"BUFF (N)"` trên thanh trạng thái phía trên.



  - Khi hover vào nút `"BUFF"`, xuất hiện thẻ Tooltip chi tiết hiển thị toàn bộ danh sách các hiệu ứng vật phẩm đang kích hoạt kèm số lượt còn lại (Mồi Thơm, Đá Mài, Bùa Vàng, Sách, Nam Châm, Sonar, Lưới...).



- **Chống Dùng Trùng Lặp Vật Phẩm Nhiều Lượt (Multi-turn Items)**:



  - Khóa hành động sử dụng thêm và cảnh báo thông báo nếu các vật phẩm có hiệu lực nhiều lượt vẫn đang hoạt động: Lưới Đánh Cá, Mồi Rủi Ro, Bùa Hoàng Kim, Sách Thông Thái, Nước Bứt Phá, Đá Mài Lưỡi Câu, Nam Châm Kho Báu, Kính Nhìn Xuyên Nước.



- **Chuẩn Hóa Tên Vật Phẩm**:



  - Tinh gọn tên gọi `"Trà Thảo Mộc Định Thần"` thành **`"Trà Thảo Mộc"`**, duy trì alias tương thích ngược 100% với dữ liệu và file save cũ.







### 🖥️ Sửa Lỗi Giao Diện & Tối Ưu Hóa Trải Nghiệm ([UpdateDialog.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/UpdateDialog.java), [GachaScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/GachaScreen.java))



- **Sửa Lỗi Auto-Updater Tắt Ngay Lập Tức**:



  - Khắc phục sự cố xung đột sự kiện click chuỗi từ nút mở ở màn hình chính làm đóng Dialog ở frame đầu tiên (`ignoreClickFrames = 3` và chặn đóng ngoài box trong `0.25s` đầu).



- **Cân Đối Bố Cục Dialog Cập Nhật**:



  - Bỏ nhãn `(AUTO-UPDATER)` trên thanh tiêu đề thành `✦ TRÌNH CẬP NHẬT TỰ ĐỘNG ✦`, loại bỏ hiện tượng tràn viền đè nút `[X]`.



  - Căn chỉnh lại 3 nút chức năng với độ rộng đồng đều `190px`, khoảng cách lọt lòng `35px` cân đối chuẩn thẩm mỹ.



- **Khắc Phục Đè Chữ Thẻ Kết Quả Gacha**:



  - Mở rộng kích thước thẻ bài kết quả đơn lẻ (`340x380px`), tách biệt hoàn toàn cụm văn bản Tên trang bị, Phân loại, Bộ trang bị và Thuộc tính ra khỏi vòng tròn icon trang bị.



- **Sửa Lỗi Lệch Thanh Tiến Trình Bản Đồ & Điều Chỉnh Độ Khó ([MapScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/MapScreen.java))**:



  - Khắc phục lỗi thanh tiến trình câu cá của Ao Làng nhảy sang Bãi Biển và Bãi Biển nhảy sang Đảo Xa: Căn chỉnh lại để mỗi thẻ bản đồ hiển thị chính xác thanh tiến độ cá của bản đồ đó (`Đã bắt ở [Tên Bản Đồ]: X/15 loài`).



  - Điều chỉnh chuẩn độ khó 3 bản đồ: Ao Làng (`☆☆☆☆☆ Độ khó: Rất Dễ`), Bãi Biển (`★☆☆☆☆ Độ khó: Dễ`), Đảo Xa (`★★☆☆☆ Độ khó: Vừa`).



  - Viết lại chú thích Đảo Xa thành mô tả thực tế vùng biển khơi: `"Hòn đảo ngoài khơi xa, sóng gió và cá lớn"`.







---







## 📅 [2026-10-27] - Phiên bản v0.5.1.1 (Hotfix Cân Bằng Tension IDLE, Đại Tu 54 Vector Icon & Phóng To Quickslot)







### ⚖️ Tái Cân Bằng & Giảm Lực Kéo Của Cá Trong Trạng Thái Mệt (IDLE State Tension Nerf - [FishingManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/FishingManager.java))



- **Nerf Mạnh Lực Cản Cá Trong Nhịp Mệt (IDLE)**:



  - *Vấn đề*: Trong phiên bản trước, khi cá kiệt sức (IDLE), hệ số lực kéo ngầm vẫn tồn tại ở mức `0.15x` khi giữ Space và `0.10x` khi buông. Đối với các loài cá cấp cao (Cá Mú, Boss), lực kéo này cộng dồn khiến kim Tension tăng quá nhanh khi kéo và tụt rất chậm khi buông dây trong nhịp nghỉ.



  - *Giải pháp*:



    - Giảm hệ số lực cản khi kéo dây (`isSpaceDown`) trong trạng thái IDLE từ `0.15x` xuống còn **`0.03x`** (giảm 80% lực cản).



    - Triệt tiêu hoàn toàn lực kéo khi buông dây (`!isSpaceDown`) trong trạng thái IDLE từ `0.10x` về **`0.0x`** (cá thả trôi, 0% lực kéo), cho phép tốc độ xả tự nhiên `(6f + 0.04f * MaxTension)` phát huy 100% hiệu quả.



    - Giảm tốc độ gia tăng Tension cơ sở khi quay máy câu trong nhịp IDLE từ `(14f + 0.025f * Max)` xuống `(10f + 0.020f * Max)`.







### 🖥️ Nâng Cấp Giao Diện Phím Tắt Nhanh ([GameHUD.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/GameHUD.java))



- **Phóng To Kích Thước Ô Quickslot & Tỷ Lệ Hiển Thị**:



  - Tăng kích thước mỗi ô từ `58f` lên `68f` (+17% kích thước), panel tổng thể mở rộng thành `236x264px` tạo không gian thoáng đãng, dễ bấm chuột và thao tác phản xạ.



  - Tăng hệ số scale vẽ icon đồ họa vector từ `0.60f` lên `0.70f` tương xứng với kích thước ô mới, giúp mọi vật phẩm nổi bật và sắc nét.



  - Tự động đồng bộ neo tọa độ góc cho nhãn phím tắt `[1]-[9]`, huy hiệu số lượng xN và huy hiệu trạng thái sách kỹ năng (HỌC / ★UP / +SP).







### 🎨 Chuẩn Hóa Vector 4 Lớp Bước 1.5: 12 Icon Mồi Câu, Sách Kỹ Năng & Chiến Lợi Phẩm Đáy Biển ([assets/vectors/loot/](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/loot/), [assets/vectors/items/](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/items/), [LootVisualRenderer.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/LootVisualRenderer.java))



- **Thiết Kế 12 Tác Phẩm Vector Phân Tầng 4 Lớp Đạt Điểm Tuyệt Đối 100/100 Audit**:



  - *4 Chiến Lợi Phẩm Rác (Trash):*



    1. **Ủng Cao Su Cũ** ([loot_ung_cao_su.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/loot/loot_ung_cao_su.svg)): Cổ ủng gập viền, đế cao su gai cleat xẻ rãnh bùn, miếng vá da chỉ thêu vàng và giọt nước nhỏ giọt ở mũi ủng.



    2. **Chai Nhựa** ([loot_chai_nhua.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/loot/loot_chai_nhua.svg)): Thân chai PET trong suốt, nắp ren xanh dương đậm, ngấn nước đục đáy chai cùng nhãn dán rách mép và bọt khí lơ lửng.



    3. **Lốp Xe** ([loot_lop_xe.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/loot/loot_lop_xe.svg)): Vành lốp cao su dày với 8 rãnh khía gai xuyên tâm, hốc bánh đọng nước rêu và các dải rong rêu bám quấn quanh viền.



    4. **Túi Rác** ([loot_tui_rac.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/loot/loot_tui_rac.svg)): Túi nilon phồng to thắt 2 tai thỏ, dây rút đỏ lộ diện, các đường gân nếp gấp kéo căng và ánh bóng nhựa phản quang.



  - *4 Cổ Vật Kho Báu (Treasure):*



    5. **Bình Cổ** ([loot_binh_co.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/loot/loot_binh_co.svg)): Bầu bình ngọc bích men lam viền chân đế đồng cổ, miệng bình loe bọc vàng, quai rồng 2 bên và đai vàng đính 5 viên hồng ngọc.



    6. **Nhẫn Vàng** ([loot_nhan_vang.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/loot/loot_nhan_vang.svg)): Nhẫn vàng ròng 24K khối dày, chấu vương miện 4 ngàm ôm viên hồng ngọc ruby giác cắt cushion lấp lánh ánh sao 4 cánh.



    7. **Rương Chìm** ([loot_ruong_chim.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/loot/loot_ruong_chim.svg)): Rương gỗ sồi cổ ngâm đáy biển nắp hé mở, đai sắt rỉ đính đinh tán, tràn ngập tiền vàng doubloon & sapphire xanh, khóa đầu lâu đồng thau và vỏ hàu bám góc.



    8. **Kiếm Cổ** ([loot_kiem_co.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/loot/loot_kiem_co.svg)): Thanh bảo kiếm thép 2 lưỡi vươn thẳng, rãnh xẻ khắc 4 ký tự cổ ngữ phát sáng, đốc kiếm đầu rồng ngọc bích, chuôi quấn lụa đỏ và dải ngọc bội phỉ thúy.



  - *2 Mồi Câu Chuyên Dụng:*



    9. **Mồi Thơm** ([item_moi_thom.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/items/item_moi_thom.svg)): Hũ thủy tinh chứa các viên bột mồi thảo mộc vàng xanh, nắp bần buộc dây gai thẻ gỗ ngôi sao thơm và làn khói hương ngọt ngào.



    10. **Mồi Rủi Ro** ([item_moi_rui_ro.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/items/item_moi_rui_ro.svg)): Lọ thuốc độc thủy tinh tím sẫm nắp kim loại vặn ren, lõi mồi đỏ thẫm phát sáng, nhãn cảnh báo đầu lâu xương chéo và làn sương tím ma quái.



  - *2 Sách Kỹ Năng / Bí Kíp:*



    11. **Sách Kỹ Năng Chủ Động** ([item_sach_chu_dong.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/items/item_sach_chu_dong.svg)): Bìa da rồng đỏ thẫm viền 4 góc vàng chạm trổ, gáy sách gân nổi, biểu tượng kiếm chiến trận hoàng kim và dải ruy băng lụa đỏ đánh dấu trang.



    12. **Sách Kỹ Năng Bị Động / Sách Thông Thái** ([item_sach_bi_dong.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/items/item_sach_bi_dong.svg)): Bìa nhung xanh thẳm huyền bí viền 4 góc bạc, mép giấy ánh bạc, phù hiệu mắt toàn tri tinh tú và dải ruy băng ngọc sapphire giọt nước.



- **Hoàn Thành Toàn Diện Giai Đoạn 1 Kế Hoạch Phát Triển Đồ Họa**:



  - Chuẩn hóa trọn vẹn 100% (54/54 icon) gồm: 30 icon trang bị (10 bộ x 3 món) + 24 icon vật phẩm tiệm tạp hóa và chiến lợi phẩm biển.



  - Tích hợp toàn bộ vào [LootVisualRenderer.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/LootVisualRenderer.java) với bộ điều hướng chính xác 100%.







---







## 📅 [2026-10-26] - Phiên bản v0.4.24 (Chuẩn Hóa Đồ Họa Vector 4 Lớp Bước 1.2 & Tái Cân Bằng Lực Căng Dây Combat)







### ⚖️ Tái Cân Bằng Lực Căng Dây (Tension) & Cơ Chế Giằng Co Chiến Đấu ([FishingManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/FishingManager.java), [Fish.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/entities/Fish.java), [combat_simulator.py](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/scripts/balance/combat_simulator.py))



- **Giảm Mạnh Tốc Độ Xả Tension Tự Nhiên Khi Buông Dây**:



  - *Vấn đề*: Tốc độ giảm Tension tự nhiên cũ là `20f + 0.10f * MaxTension` (~30-40 tension/s), quá lớn so với lực kéo của cá (thường từ 10-30). Khi người chơi buông phím Space, kim Tension lập tức tụt dốc không phanh về 0 bất kể cá đang giằng co thế nào, khiến cơ chế đứt dây trở nên vô hại.



  - *Giải pháp*: Điều chỉnh tốc độ giảm tự nhiên xuống `(6f + 0.04f * MaxTension) * delta` (chỉ còn ~10 tension/s ở mốc 100 MaxTension, giảm hơn 67%), loại bỏ hiện tượng tụt dốc bất hợp lý.



- **Buff Cho Cá Khi Vùng Vẫy (PULLING) & Khi Kiệt Sức (IDLE)**:



  - *Khi cá vùng vẫy (PULLING)*:



    - Nếu người chơi buông phím Space (buông dây): Cá vẫn giằng với **80% lực kéo** (`pullMult = 0.8f`, tăng gấp đôi so với mốc 40% trước đây).



    - Nếu người chơi kéo cần (giữ Space): Giữ nguyên **150% lực kéo** (`pullMult = 1.5f`) cộng dồn lực quay máy câu làm tăng vọt Tension.



  - *Khi cá kiệt sức (IDLE)*:



    - Nerf lực cản của cá khi kiệt sức để người chơi dễ thở và dồn sát thương: `pullMult = 0.15f` khi kéo và `0.10f` khi buông.



    - Nhịp nghỉ IDLE của cá được ấn định ở mức `1.5s - 3.5s` cân đối hoàn hảo giữa nhịp hồi Tension và nhịp kéo.



- **Đồng Bộ Hệ Thống Mô Phỏng Cân Bằng (Monte Carlo Combat Simulator)**:



  - Cập nhật [combat_simulator.py](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/scripts/balance/combat_simulator.py) chuẩn hóa 100% các công thức kéo cá, sát thương cơ bản (20 base + chỉ số cần), thể lực người chơi (100 base + chỉ số cần) và tiêu hao thể lực.



  - Chạy kiểm định toàn diện qua [balance_cli.py](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/scripts/balance/balance_cli.py): Các bậc cá từ Ao Làng đến Đại Dương đều thỏa mãn tiêu chuẩn TTK và Win Rate theo quy chuẩn [balance_guidelines.md](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/.agents/rules/balance_guidelines.md).



### 🎨 Chuẩn Hóa Vector 4 Lớp Bước 1.4: 12 Icon Vật Phẩm Tiêu Hao & Đạo Cụ Tạp Hóa ([assets/vectors/items/](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/items/), [LootVisualRenderer.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/LootVisualRenderer.java), [ItemDatabase.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/data/ItemDatabase.java))



- **Thiết Kế 12 Tác Phẩm Vector Độc Bản Đạt Chuẩn Tuyệt Đối 100/100 Điểm Audit**:



  - *5 Vật Phẩm Tiêu Hao & Hồi Phục:*



    1. **Bánh Mì** ([item_banh_mi.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/items/item_banh_mi.svg)): Ổ bánh mì nướng vàng giòn, 3 rãnh khía vát nở xốp rắc bột mì trắng tinh, bơ mật óng ánh và bóng đổ tiếp xúc êm dịu.



    2. **Thuốc Hồi Stamina** ([item_thuoc_stamina.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/items/item_thuoc_stamina.svg)): Bình cầu thủy tinh ngọc bích sủi bọt khí hồi sinh, đai da cổ buộc đá vàng hổ phách, nắp bần gỗ sồi và dải quang phổ phản quang hình cung.



    3. **Nước Tăng Lực** ([item_nuoc_tang_luc.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/items/item_nuoc_tang_luc.svg)): Lon nhôm thể thao phong cách cyber đỏ rực, nắp lon khoen giật kim loại, tia chớp vàng bùng nổ năng lượng, dải sọc tốc độ và các giọt nước ngưng tụ sương lạnh.



    4. **Nước Bứt Phá** ([item_nuoc_but_pha.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/items/item_nuoc_but_pha.svg)): Bình tam giác Erlenmeyer giả kim thuật cổ đại, đai đồng tán đinh vàng vương miện, chất lỏng plasma rực lửa với tâm lốc xoáy năng lượng Overdrive cực hạn.



    5. **Trà Thảo Mộc Định Thần** ([item_tra_thao_moc.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/items/item_tra_thao_moc.svg)): Tách trà gốm ngọc men xanh ngọc bích nằm trên đĩa sứ viền đôi, mặt trà tĩnh lặng với lá trà xanh thơm ngát trôi nổi và 3 làn khói thơm thư thái uốn lượn.



  - *7 Đạo Cụ Chiến Đấu & Khai Thác Tiệm Tạp Hóa:*



    6. **Dây Câu Tốt** ([item_day_cau_tot.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/items/item_day_cau_tot.svg)): Cuộn cước câu cá thể thao gia cường khung titan xám đậm, lõi cước bện sợi Kevlar xanh biển chịu tải cực cao, kẹp giữ dây đồng thau và bóng đổ tròn tiếp xúc.



    7. **Bình Xịt Đóng Băng** ([item_binh_xit_bang.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/items/item_binh_xit_bang.svg)): Bình xịt khí nén kim loại xanh băng giá, dải phản quang kim loại lạnh, biểu tượng hoa tuyết Cryo sắc nét, van xịt và chùm khói sương cực lạnh đang phụt tỏa.



    8. **Đá Mài Lưỡi Câu** ([item_da_mai_luoi.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/items/item_da_mai_luoi.svg)): Khối đá mài 2 tầng độ mịn (tầng dưới thô xám đen, tầng trên mịn màu đá dầu), vát góc phối cảnh đẳng cự 3D, rãnh mài lưỡi sáng bóng và tia lửa mài sắc bén văng lên.



    9. **Nam Châm Kho Báu** ([item_nam_cham.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/items/item_nam_cham.svg)): Nam châm móng ngựa chữ U phủ sơn đỏ truyền thống, 2 cực N và S bọc thép mạ chrome sáng loáng, các đường cung từ trường phát sáng hút chặt các đồng tiền vàng lấp lánh.



    10. **Kính Nhìn Xuyên Nước** ([item_kinh_nhin_xuyen_nuoc.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/items/item_kinh_nhin_xuyen_nuoc.svg)): Kính lặn thấu thị công nghệ sonar với 2 tròng kính viền đồng thau, màn hình rada xanh lục phát sáng, tâm ngắm chữ thập đo đạc và tín hiệu cá bơi phản xạ vàng kim.



    11. **Lưới Đánh Cá** ([item_luoi_danh_ca.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/items/item_luoi_danh_ca.svg)): Lưới nón quăng cá đan mắt cáo hình thoi đều đặn bằng sợi tơ nilon xanh lam, khuyên tròn cầm tay bằng đồng vàng đỉnh lưới và chuỗi 7 quả chì chìm đáy có ánh sáng phản chiếu.



    12. **Bùa Hoàng Kim** ([item_bua_hoang_kim.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/items/item_bua_hoang_kim.svg)): Thẻ bùa giấy tiên đạo phương Đông màu hoàng kim viền đỏ chu sa, thư pháp bùa chú phong thủy uốn lượn sắc sảo, đính dây treo chỉ đỏ và hạt ngọc bội phỉ thúy may mắn.



- **Khắc Phục Lỗi Biến Dạng Tọa Độ & Ban Hành Quy Chuẩn An Toàn Tọa Độ (Coordinate Integrity)**:



  - *Nguyên nhân sự cố:* Bộ script chuyển đổi trước đó bị lỗi gõ nhầm biến `y1 = x1` và `y3 = x3` (`poly_pts[1][0]` thay vì `poly_pts[1][1]`), khiến các hình đa giác bị kéo xiên 45 độ thành dạng răng cưa mã vạch; đồng thời vòng hào quang nền bị vẽ quá lớn với màu đục che khuất vật thể chính.



  - *Giải pháp:* Sửa hoàn toàn thuật toán transpile sang cấu trúc `(x1, y1, x2, y2, x3, y3, x4, y4)` hoàn toàn độc lập, tinh chỉnh hào quang mềm mại ($r \le 44$, opacity $0.12 - 0.18$), bổ sung hàm `fillEllipse` chuẩn xác.



  - *Cập nhật Agent Skills:* Bổ sung Mục 4 (Quy Chuẩn Kiểm Thử Hình Học & An Toàn Tọa Độ) vào [.agents/skills/vector-graphics-designer/SKILL.md](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/.agents/skills/vector-graphics-designer/SKILL.md).



- **Tích Hợp Toàn Bộ Vào [LootVisualRenderer.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/LootVisualRenderer.java)**:



  - Bổ sung bộ helper hình học chuẩn LibGDX (`sx`, `sy`, `fillTri`, `fillQuad`, `drawLine`, `fillCircle`, `fillEllipse`).



  - Điều hướng chính xác toàn bộ 12 vật phẩm tiêu hao trong `drawLoot()`, chấm dứt việc dùng chung lọ thuốc tạm bợ.



  - Dọn dẹp sạch sẽ toàn bộ 36+ ảnh preview trong `assets/vectors/previews/`.







### 🎨 Đột Phá Thiết Kế Độc Bản (Strict Non-Repetition Rule) & Bùng Nổ Hiệu Ứng Thần Khí Lạc Hồng ([SKILL.md](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/.agents/skills/vector-graphics-designer/SKILL.md), [EquipmentIconRenderer.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/EquipmentIconRenderer.java), [assets/vectors/equipment/](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/))



- **Ban Hành Nguyên Lý Độc Nhất Vô Nhị (Section 1.1 trong SKILL.md)**:



  - Cấm tuyệt đối hiện tượng tái sử dụng phom dáng (template reuse) hoặc vẽ các đường cong na ná nhau giữa các bộ trang bị.



  - Mỗi icon bắt buộc phải sở hữu ngôn ngữ tạo hình (silhouette), chủ đề văn hóa và cấu trúc cơ học hoàn toàn khác biệt.



  - Quy định chuẩn hiệu ứng bùng nổ (Explosive Impact) riêng cho dòng Thần Khí Premium Limited (Lạc Hồng): Đỉnh cao huyền thoại văn hóa Lạc Việt, hào quang nhiều tầng, chi tiết vương giả rực lửa.



- **Tái Sinh Ngoại Hình Cần Câu (Rod) Quang Minh vs Lạc Hồng**:



  - *Cần Quang Minh ([rod_quang_minh.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/rod_quang_minh.svg))*: Chuyển đổi thành quyền trượng Seraph dựng đứng hình chữ thập (Upright Cruciform Staff), hào quang Sol Halo bay lơ lửng, đôi cánh lông vũ thiên thần xòe rộng rực sáng.



  - *Cần Lạc Hồng ([rod_lac_hong.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/rod_lac_hong.svg))*: Thần trượng chiến trận Đông Sơn nghiêng chéo bùng nổ, đầu trượng Trống Đồng 14 tia sáng rực lửa, Chim Lạc sải cánh ngậm hồng ngọc ruby và thanh long ngọc lục bảo cuộn dọc thân trượng.



- **Tái Sinh Ngoại Hình Dây Cước (Line) Cả 3 Bộ Đỉnh Cao**:



  - *Dây Quang Minh ([line_quang_minh.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/line_quang_minh.svg))*: Khung dệt quả trám Seraphic Reliquary Loom 4 ngàm lông vũ thiên sứ ôm trọn khối cầu Thái Dương Sunstone rực lửa.



  - *Dây Vệ Quốc ([line_ve_quoc.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/line_ve_quoc.svg))*: Ổ quay bát giác đồng rèn chiến trận với 8 đầu mũi giáo nhọn nhô ra ngoài, tâm khiên sao vàng 5 cánh, tay quay cơ học cổ và cờ lệnh đỏ bay phần phật.



  - *Dây Lạc Hồng ([line_lac_hong.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/line_lac_hong.svg))*: Bùng nổ Song Long Ouroboros (đôi rồng ngọc lục bảo bờm vàng) quấn quanh viên Ngọc Rồng Huyết Ngọc Ruby phân tầng ánh lửa thần thoại.



- **Tái Sinh Ngoại Hình Lưỡi Câu (Hook) Cả 3 Bộ Độc Bản**:



  - *Lưỡi Quang Minh ([hook_quang_minh.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/hook_quang_minh.svg))*: Mỏ neo thập tự thánh thiện đối xứng hoàn hảo, vương miện 3 vòng hào quang, thân kiếm 2 lưỡi và 2 ngạnh móc đối xứng đầu mũi tên kim cương.



  - *Lưỡi Vệ Quốc ([hook_ve_quoc.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/hook_ve_quoc.svg))*: Móng vuốt rồng Trảo Long Khởi Nghĩa với sống gai nhọn răng cưa chiến trận, mắt khuyên huy hiệu sao vàng 5 cánh và dải lụa đỏ xung trận.



  - *Lưỡi Lạc Hồng ([hook_lac_hong.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/hook_lac_hong.svg))*: Bệ đỡ Đóa Sen Hồng Khảm Vàng (Đài Sen Mẫu Âu Cơ) tại đáy uốn, ngạnh câu tạc hình Chim Lạc sải cánh mỏ khoằm với mắt hồng ngọc ruby, mắt khuyên ngọc Tiên Châu hào quang mặt trời và dải lụa tiên phục.



- **Kiểm Định Vector Studio & Đồng Bộ 100% In-Game**:



  - Toàn bộ 8 file SVG tái thiết kế đều đạt điểm số tuyệt đối 100/100 tại Quality Gate.



  - Transpile hoàn toàn 8 hàm vẽ vào [EquipmentIconRenderer.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/EquipmentIconRenderer.java).







### 🎨 Chuẩn Hóa Vector 4 Lớp Bước 1.3: 9 Icon Trang Bị Thần Thoại & Giới Hạn 6★ - 7★ ([EquipmentIconRenderer.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/EquipmentIconRenderer.java), [assets/vectors/equipment/](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/))



- **Thiết Kế & Kiểm Định Chất Lượng Tuyệt Đối (100/100 Điểm Kiểm Định Vector Studio)**:



  - Hoàn thiện trọn vẹn 9 tác phẩm vector đỉnh cao phân tầng 4 lớp (Aura phân tầng -> Facet Body -> Core Accent -> Specular Glint `#ffffff`):



    - **Set Quang Minh (7★ Mythic)**:



      - *Cần Câu Quang Minh ([rod_quang_minh.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/rod_quang_minh.svg))*: Quyền trượng Seraph thiên sứ bạch kim mạ vàng 24K, 3 đai ngọc Thái Dương Sunstone rực lửa, đôi cánh lông vũ thiên thần xòe rộng và vương miện Sol Diamond 8 tia sáng.



      - *Dây Cước Quang Minh ([line_quang_minh.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/line_quang_minh.svg))*: Bánh xe mặt trời chiến xa Archangel 12 răng cưa thái dương, 8 nan hoa vàng ròng, cuộn chỉ tơ vàng đan chéo phản quang và tâm lõi bảo ngọc Sol Gem kim cương.



      - *Lưỡi Câu Quang Minh ([hook_quang_minh.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/hook_quang_minh.svg))*: Lưỡi câu vàng ròng thiên sứ uốn lượn uy nghi, 3 tầng cánh lông vũ thánh thần dọc sống lưng, mắt khuyên vương miện hào quang bay lơ lửng đính hồng ngọc và ngạnh mũi giáo pha lê ánh kim.



    - **Set Vệ Quốc Vang Danh (Limited Event cày mốc)**:



      - *Cần Câu Vệ Quốc ([rod_ve_quoc.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/rod_ve_quoc.svg))*: Thân trúc son ngọc bích đỏ sẫm quấn đai đồng cổ khắc sao vàng 5 cánh, ngọn giáo đồng Đông Sơn và lá cờ lệnh ngũ sắc bay phấp phới trong gió chiến trận.



      - *Dây Cước Vệ Quốc ([line_ve_quoc.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/line_ve_quoc.svg))*: Khung cuộn trống đồng Đông Sơn 8 tia răng cưa, cuộn dây cước bện tơ đỏ huyết chiến bọc viền vàng kim và tâm sao vàng 5 cánh oai hùng.



      - *Lưỡi Câu Vệ Quốc ([hook_ve_quoc.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/hook_ve_quoc.svg))*: Móng vuốt rồng thiêng đồng cổ uốn lượn sắc nhọn, dải hoa văn sóng nước Đông Sơn, mắt khuyên vương miện sao vàng và dải lụa đỏ thắm hộ quốc.



    - **Set Thần Khí Lạc Hồng (6★ Limited Premium Gacha)**:



      - *Cần Câu Lạc Hồng ([rod_lac_hong.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/rod_lac_hong.svg))*: Thần trượng Đông Sơn khảm ngọc lục bảo và vàng hoàng gia 24K, 4 đai đồng nạm hồng ngọc ruby, đỉnh trượng đầu rồng thiêng và chim Lạc sải cánh thần thoại.



      - *Dây Cước Lạc Hồng ([line_lac_hong.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/line_lac_hong.svg))*: Khung cuộn trống đồng 16 răng cưa, Song Long Chầu Nguyệt (rồng ngọc lục bảo Lạc Long Quân) ôm trọn cuộn gân rồng, tâm lõi viên ngọc rồng Long Châu rực lửa và thẻ ngọc bài hộ mệnh.



      - *Lưỡi Câu Lạc Hồng ([hook_lac_hong.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/hook_lac_hong.svg))*: Lưỡi câu thiên thạch thần kim viền vàng ngọc bích, đài sen tiên mẫu Âu Cơ tỏa hương, ngạnh câu mỏ chim Lạc đính hồng ngọc và chuỗi ngọc Tiên Châu Bách Noãn.



- **Transpile Hoàn Toàn 9 Hàm Vẽ Vào [EquipmentIconRenderer.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/EquipmentIconRenderer.java)**:



  - Tái cấu trúc toàn bộ 9 hàm: `drawQuangMinhRod`, `drawQuangMinhLine`, `drawQuangMinhHook`, `drawVeQuocRod`, `drawVeQuocLine`, `drawVeQuocHook`, `drawLacHongRod`, `drawLacHongLine`, `drawLacHongHook`.



  - Thay thế toàn bộ hình vẽ sơ khai bằng các khối hình học đa giác đặc `(fillTri, fillQuad, drawLine, fillCircle)` chuẩn xác tọa độ SVG.







### 🎨 Chuẩn Hóa Vector 4 Lớp Bước 1.2: 9 Icon Trang Bị Cao Cấp & Huyền Thoại 4★ - 6★ ([EquipmentIconRenderer.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/EquipmentIconRenderer.java), [assets/vectors/equipment/](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/))



- **Thiết Kế & Kiểm Định Chất Lượng Tuyệt Đối (100/100 Điểm Kiểm Định Vector Studio)**:



  - Hoàn thiện trọn vẹn 9 tác phẩm vector hình học 4 lớp (Aura hào quang phân tầng -> Thân hình học khối Facet Body -> Tâm lõi Core Gem / Accent -> Đốm sáng phản chiếu Specular Glint `#ffffff`):



    - **Set Hải Tặc (4★ Epic)**:



      - *Cần Câu Hải Tặc ([rod_hai_tac.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/rod_hai_tac.svg))*: Trụ cột buồm chiến hạm gỗ gụ lâu năm bọc 4 đai đồng đinh tán vàng, xà ngang treo đèn bão cướp biển ngọn lửa ma quái, ròng rọc mạn thuyền quấn dây thừng gai và cờ đuôi nheo Jolly Roger đầu lâu mắt đỏ hồng ngọc.



      - *Dây Cước Hải Tặc ([line_hai_tac.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/line_hai_tac.svg))*: Bánh lái tàu chiến mahogany nạm đồng 8 tay cầm xoay tròn, mỏ neo cổ sắt rèn gác chéo, 8 mắt xích sắt bao quanh và đồng tiền vàng Aztec trung tâm khắc đầu lâu ivory phát sáng mắt ngọc lục bảo.



      - *Lưỡi Câu Hải Tặc ([hook_hai_tac.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/hook_hai_tac.svg))*: Lưỡi hái hải tặc rèn thép Damascus nguyên khối uốn cong ngược tàn khốc, rãnh xẻ rỉ máu hồng ngọc, chuôi bảo kiếm bát kiếm đồng bảo vệ mắt khuyên và đồng bạc 8-Reales xỏ lỗ lúc lắc.



    - **Set Vực Thẳm (5★ Legendary)**:



      - *Cần Câu Vực Thẳm ([rod_vuc_tham.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/rod_vuc_tham.svg))*: Quyền trượng vực sâu hắc ám obsidian giác cạnh sắc lẹm, 3 cụm gai sừng quỷ nhô ngang rực sáng tím plasma, vương miện lưỡi liềm hư không và con mắt quỷ toàn tri All-Seeing Nether Eye đồng tử dọc ma mị.



      - *Dây Cước Vực Thẳm ([line_vuc_tham.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/line_vuc_tham.svg))*: Khung cuộn bát giác nether-titanium 8 gai sừng quỷ hướng tâm, cuộn tơ hắc mạch tím thẫm đan xen huyết sắc và tâm mắt quỷ phát quang rực rỡ.



      - *Lưỡi Câu Vực Thẳm ([hook_vuc_tham.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/hook_vuc_tham.svg))*: Móng vuốt rồng vực sâu hắc ám ngạnh ngược, 3 gai vảy rồng hư không trên lưng móng, rãnh rỉ huyết tương tím plasma và mắt quỷ Nether Eye All-Seeing ở mắt khuyên.



    - **Set Hoàng Gia (6★ Legendary)**:



      - *Cần Câu Hoàng Gia ([rod_hoang_gia.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/rod_hoang_gia.svg))*: Quyền trượng bạch kim tinh khiết mạ vàng hoàng gia 24k dập nổi hoa văn, tay cầm quấn chỉ vàng, 2 đai khớp vàng, khoen gốm viền vàng và đỉnh trượng hoa bách hợp Fleur-de-lis đính hồng ngọc Ruby giác cắt kim cương chói lọi.



      - *Dây Cước Hoàng Gia ([line_hoang_gia.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/line_hoang_gia.svg))*: Bánh xe tơ tằm hoàng gia 8 cánh hoa văn cung đình nạm vàng ròng, cuộn chỉ tơ tằm tím dệt kim tuyến vàng óng ả và trục ấn triều đình khảm viên lam ngọc Sapphire giác cắt giác đa diện rực rỡ.



      - *Lưỡi Câu Hoàng Gia ([hook_hoang_gia.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/hook_hoang_gia.svg))*: Lưỡi câu tạc từ khối cẩm thạch trắng thượng hạng (White Nephrite Jade) nạm viền sống lưng vàng 24k, mắt khuyên vương miện hoàng gia hoa bách hợp, chuỗi ngọc trai giọt nước rủ xuống mềm mại và ngạnh khóa mồi mạ vàng đính hồng ngọc.



- **Transpile & Đồng Nhất 100% Vào Đồ Họa In-Game ([EquipmentIconRenderer.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/EquipmentIconRenderer.java))**:



  - Tái cấu trúc toàn bộ 9 phương thức `drawHaiTacRod`, `drawHaiTacLine`, `drawHaiTacHook`, `drawVucThamRod`, `drawVucThamLine`, `drawVucThamHook`, `drawHoangGiaRod`, `drawHoangGiaLine`, `drawHoangGiaHook`.



  - Thay thế toàn bộ nét phác thảo đường thẳng que cũ bằng hệ thống hình học đa giác đặc `(sx, sy, fillTri, fillQuad, drawLine, fillCircle)` chuẩn xác theo tọa độ ViewBox 128x128 của file SVG.



  - Đảm bảo hiệu ứng hiển thị lộng lẫy, phân tầng màu sắc tương phản sắc nét và đồng nhất tuyệt đối giữa màn hình Gacha, Túi đồ (Inventory), Bảng Trang Bị (Equipment) và Tiệm buôn.







### 🎨 Chuyển Thể Đồ Họa Vector Hình Học Nguyên Bản Bước 1.1 ([EquipmentIconRenderer.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/EquipmentIconRenderer.java), [GachaScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/GachaScreen.java))



- **Khắc Phục Hiện Tượng "Nét Vẽ Gậy Que" Khác Biệt Giữa File SVG Và In-Game**:



  - *Nguyên nhân*: Các hàm vẽ trước đây chỉ dùng vài nét đơn sơ `sr.rectLine` để phác thảo đại khái, khiến Lưỡi Câu Băng Giá nhìn như một cây đinh ba thanh mảnh thay vì mỏ neo pha lê tuyết phủ dày dặn, có giác cạnh và cánh cong như trong file vector gốc `hook_bang_gia.svg`.



  - *Giải pháp*: Xây dựng hệ thống ánh xạ tọa độ chuẩn xác `(sx, sy, fillTri, fillQuad, drawLine, fillCircle)` biến toàn bộ cấu trúc SVG 128x128 sang hình học đa giác đặc (`ShapeRenderer.ShapeType.Filled`) 100% trung thực:



    - **Lưỡi Câu Băng Giá ([hook_bang_gia.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/hook_bang_gia.svg))**: Lỗ xỏ lục giác băng Cryo, trụ đá ngọc bích 2 lớp, đôi cánh mỏ neo băng giá cong dày với giác cắt pha lê và ngạnh nhọn, huy hiệu sao băng tuyết 5 cánh đặc tâm phát sáng rực rỡ, mũi chóp mỏ neo và hạt lấp lánh specular glint.



    - **Cần Câu Băng Giá ([rod_bang_gia.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/rod_bang_gia.svg))**: Thân cần ngọc bích uốn lượn mượt mà không gai nhọn, 3 đai ngọc lam nạm bạc, chuôi kim cương và vương miện sao tuyết 6 cánh với các đỉnh quả trám đối xứng.



    - **Dây Cước Băng Giá ([line_bang_gia.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/line_bang_gia.svg))**: Khung cuộn lục giác với 6 sừng băng vươn ra ngoài, các vòng cước siêu dẫn, tâm lục giác lõi băng và giọt nước tuyết nhỏ giọt.



    - **Lưỡi Câu Thợ Săn ([hook_tho_san.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/hook_tho_san.svg))**: Móng vuốt lưỡi liềm khủng long hóa thạch dày đặc ngà voi, 3 gai răng cưa săn mồi trên lưng cong, rãnh xẻ rỉ nọc độc ngọc lục bảo và bùa nanh hổ phách.



    - **Lưỡi Câu Đại Dương ([hook_dai_duong.svg](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/hook_dai_duong.svg))**: Mắt xỏ vây đuôi cá voi mạ vàng, sống răng hàm thủy quái Leviathan uốn lượn, 2 vây cá mập và mũi lao tam xoa sóng triều Hải Vương.



- **Sửa Lỗi Giao Diện & Canh Chỉnh Chữ Tại Bệ Đỡ Gacha ([GachaScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/GachaScreen.java))**:



  - Khắc phục chữ nhãn "Cần Câu", "Dây Cước", "Lưỡi Câu" bị lệch tọa độ đè ngang mép dưới khung ô vuông trang bị: Nâng chiều cao bệ đỡ lên `124px`, đặt ô vật phẩm ở `pedY + 28f` và đưa nhãn chữ xuống `pedY + 6f` ngay ngắn, thoáng đãng.



  - Loại bỏ hoàn toàn viền khung chữ nhật màu xanh đè lên trục quay hình tròn của Đài quay gacha.



  - Đồng bộ vùng cảm ứng chuột (hitbox) của Bệ đỡ trang bị chính xác theo kích thước mới.







### 🛡️ Bảo Mật File Save & Chống Gian Lận Dữ Liệu ([SaveManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/data/SaveManager.java), [SettingsDialog.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/SettingsDialog.java))



- **Mã Hóa Toàn Diện File Save Bằng Thuật Toán AES-256**:



  - Khắc phục nguy cơ người chơi mở file lưu trữ bằng Notepad hoặc Text Editor để tự ý sửa Vàng, Kim Cương, Cấp độ, Điểm kỹ năng hoặc Trang bị gacha.



  - Áp dụng thuật toán mã hóa khối chuẩn công nghiệp `AES/CBC/PKCS5Padding` với khóa bí mật 256-bit được sinh từ hàm băm SHA-256 kết hợp muối (Salt) dự án.



  - Mỗi lần xuất file đều tự động tạo một Vector khởi tạo (IV) ngẫu nhiên 16-byte (`SecureRandom`), đảm bảo cùng một dữ liệu xuất ở hai thời điểm khác nhau sẽ cho ra hai chuỗi mã hóa hoàn toàn khác biệt.



- **Hệ Thống Kiểm Tra Tính Toàn Vẹn & Báo Động Can Thiệp (Tamper Detection)**:



  - Tính toán mã băm SHA-256 (32 bytes) của dữ liệu JSON gốc và đóng gói trực tiếp vào phần đầu payload trước khi mã hóa Base64 (`ANGLER_SECURE_SAVE_v1:`).



  - Khi nạp dữ liệu: Sau khi giải mã AES, hệ thống tự động băm lại chuỗi văn bản và so sánh đối chiếu bằng `MessageDigest.isEqual` (chống tấn công Timing Attack).



  - Nếu phát hiện bất kỳ ký tự nào bị sửa đổi dù chỉ 1 bit, hệ thống sẽ lập tức từ chối nạp, ghi nhật ký cảnh báo và hiển thị thông báo lỗi trên UI: *"Không tìm thấy dữ liệu hợp lệ (Dữ liệu bị lỗi hoặc đã qua chỉnh sửa)!"*.



- **Chuẩn Hóa Đuôi File Mở Rộng `.sav` & Sao Chép Clipboard Tiện Lợi**:



  - Đổi tên tệp xuất mặc định sang định dạng `.sav` (`angler_save_yyyyMMdd_HHmmss.sav` và bản fallback `angler_save.sav`), hạn chế việc hệ điều hành tự động gán mở file bằng Notepad.



  - Chuỗi dữ liệu sao chép vào Clipboard cũng là chuỗi đã được mã hóa an toàn.



  - FileDialog trong Cài Đặt hỗ trợ bộ lọc mở rộng `*.sav;*.json;*.bak`.



- **Tương Thích Ngược & An Toàn Tuyệt Đối**:



  - Bộ giải mã thông minh nhận diện định dạng: Nếu người chơi nạp file `.json` hoặc file `.bak` cũ (từ các bản v0.4.23 trở về trước), game vẫn tự động tiếp nhận mượt mà và sẽ tự động nâng cấp sang định dạng mã hóa mới ở lần xuất tiếp theo.







### ⏳ Chốt Hạn Sự Kiện Lạc Hồng & Cơ Chế Khóa An Toàn ([EventManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/EventManager.java), [EventScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/EventScreen.java))



- **Ấn Định Thời Gian Bế Mạc Sự Kiện Lạc Hồng**:



  - Thiết lập thời điểm kết thúc chính thức của Sự kiện Lạc Hồng vào đúng **23:59:59 ngày 28/10/2026**.



  - Đồng hồ đếm ngược tại Header sự kiện tự động tính toán thời gian còn lại (ngày/giờ) theo thời gian thực đồng bộ chống tua giờ.



- **Cơ Chế Khóa Cửa Hàng & Chống Đổi Đồ Khi Hết Hạn**:



  - Sau thời khắc 23:59:59 ngày 28/10, phương thức `EventManager.isEventActive()` chuyển sang `false`.



  - Toàn bộ các nút đổi vật phẩm trong Cửa Hàng Sự Kiện (Set Vệ Quốc, Vé quay, Vàng) tự động chuyển sang màu xám mờ và hiển thị nhãn: `"ĐÃ ĐÓNG (HẾT HẠN)"`.



  - Lệnh mua `buyShopItem` kích hoạt kiểm tra an toàn, chặn mọi thao tác đổi và hiển thị thông báo: *"Sự kiện Lạc Hồng đã kết thúc! Cửa hàng đã đóng cửa."*.



- **Tự Động Quy Đổi Điểm Sự Kiện Tồn Đọng Sang Vàng (Grace Transition)**:



  - Nhằm bảo vệ công sức cày cuốc của cần thủ, khi sự kiện kết thúc, toàn bộ Điểm Sự Kiện còn dư sẽ được tự động quy đổi thành Vàng với tỉ lệ ưu đãi: **1 Điểm SK = 1,000 Vàng**, nạp thẳng vào túi đồ người chơi và reset điểm về 0 để chuẩn bị cho mùa sự kiện mới.



  - Trong giai đoạn chuyển giao từ 29/10 đến 30/10, Header sự kiện hiển thị thông báo: *"ĐÃ KẾT THÚC (Halloween sau: Xd Yh)"*.







### 🎰 Đính Chính Hoạt Ảnh Băng Chuyền Gacha Kiểu CS:GO ([TASKS.md](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/TASKS.md), [PatchNotesDialog.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/PatchNotesDialog.java))



- **Đồng Bộ Mô Tả Hoạt Ảnh Gacha Theo Phong Cách Con Lăn Mở Hòm CS:GO (Case Opening Reel)**:



  - Khắc phục các mô tả nhầm lẫn trước đây trong tài liệu và Roadmap về "vòng quay 12 cung" hay "lật thẻ" (game hoàn toàn không có cơ chế lật thẻ bài).



  - Hoạt ảnh quay Gacha thực tế trong `GachaScreen.java` là cơ chế Băng chuyền con lăn cuộn ngang (Horizontal Roller Reel) kinh điển của CS:GO: Băng chuyền 70 thẻ trang bị trượt lướt tốc độ cao từ phải sang trái rồi hãm phanh mượt mà bằng đường cong gia tốc phi tuyến `Interpolation.pow5Out`, dừng lại tại kim chỉ vạch vàng căn giữa.



  - Cập nhật ma trận ưu tiên (GAC-02, GAC-03), lộ trình Phase 4 và bản tin Patch Notes: Loại bỏ hoàn toàn khái niệm "lật thẻ" và chuẩn hóa thành Showcase trang bị nổ vàng.







### 🛠️ Đại Tu Bố Cục Thẻ Kỹ Năng & Đồng Nhất Phông Chữ ([SkillTreeScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/SkillTreeScreen.java))



- **Bỏ Dấu Chấm & Chuẩn Hóa Dòng Hồi Chiêu**:



  - Loại bỏ hoàn toàn ký tự chấm đầu dòng `•` gây vướng mắt ở thông tin hồi chiêu, đổi thành `"Thời gian hồi chiêu: Xs"` đặt ngay ngắn 28px bên dưới nhãn phân loại `[Chủ Động • Cấp X/Y]`.



- **Đồng Nhất Chuẩn Phông Chữ & Tỷ Lệ Sắc Nét**:



  - Áp dụng chuẩn tỷ lệ font đồng nhất (0.95f) cho toàn bộ nhãn phân loại, hồi chiêu, mô tả chi tiết, tiêu đề hiệu ứng và thông số cấp tiếp theo, triệt tiêu hoàn toàn hiện tượng lệch cỡ font hay mờ nhòe.



- **Tối Ưu Bố Cục Dọc & Neo Cố Định Nút Hành Động Ở Đáy Bảng**:



  - Mở rộng khoảng cách giữa các phần (Mô tả, Hiệu ứng hiện tại, Hiệu ứng cấp tiếp theo) từ 28-32px, bổ sung đường phân cách tinh tế dưới header.



  - Neo cố định các nút hành động (Mở Khóa / Nâng Cấp / Gán Kỹ Năng) ở sát đáy bảng thông tin; lấp đầy không gian hài hòa, giải quyết triệt để cả lỗi đè chữ dính nhau lẫn khoảng trống thừa thãi.







### 🎨 Bước 1.1: Hoàn Thành 12 Vector Icon 4 Set Trang Bị Cơ Bản 0★ - 3★ (EQP-02A) ([EquipmentIconRenderer.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/EquipmentIconRenderer.java), [assets/vectors/equipment/](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/vectors/equipment/))



- **Chuẩn Hóa Thư Viện SVG Nguồn & Quy Chuẩn Vector 4 Lớp Đạt 100/100 Điểm Quality Gate**:



  - Thiết lập đầy đủ 12 file SVG gốc trong `assets/vectors/equipment/` theo chuẩn tỉ lệ `viewBox="0 0 128 128"` và thẩm mỹ Cyber-Fantasy & Deep Ocean Abyssal:



    * *Set Tập Sự (0★ Common):* `rod_tap_su.svg`, `line_tap_su.svg`, `hook_tap_su.svg`.



    * *Set Thợ Săn (1★ Uncommon):* `rod_tho_san.svg`, `line_tho_san.svg`, `hook_tho_san.svg`.



    * *Set Băng Giá (2★ Rare):* `rod_bang_gia.svg`, `line_bang_gia.svg`, `hook_bang_gia.svg`.



    * *Set Đại Dương (3★ Rare/Epic):* `rod_dai_duong.svg`, `line_dai_duong.svg`, `hook_dai_duong.svg`.



  - Toàn bộ 12 icon vượt qua đợt kiểm duyệt `vector_pipeline.py audit` với điểm số tuyệt đối 100/100.







### 💎 Đồng Nhất & Phóng Đại Bản Hiển Thị Đầy Đủ Trang Bị Gacha & Equipment (Full Representation & Anti-Squish) ([GachaScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/GachaScreen.java), [EquipmentScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/EquipmentScreen.java), [EquipmentIconRenderer.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/EquipmentIconRenderer.java))



- **Thiết Kế Lại Triệt Để Ngoại Hình 3 Móc Câu (Khác Biệt Hoàn Toàn Về Dáng Silhouette)**:



  - *Hook Thợ Săn (`hook_tho_san`):* Móng Vuốt Dã Thú Tiền Sử (Raptor Talon Sickle) uốn cong hình lưỡi liềm, ngàm xương kẹp dây da thuộc, rãnh xẻ máu ngọc bích và bùa ngọc mắt hổ.



  - *Hook Băng Giá (`hook_bang_gia`):* Mỏ Neo Tinh Thể Băng Đối Xứng (Ice Crystal Anchor), khuyên móc lục giác pha lê, thân trụ băng thẳng đứng với 2 cánh ngạnh băng song đối tựa cánh chim băng và huy hiệu hoa tuyết 6 cánh ở tâm.



  - *Hook Đại Dương (`hook_dai_duong`):* Lao Nanh Thủy Quái Đuôi Cá Voi (Leviathan Harpoon Fang), khuyên móc tạo hình đuôi cá voi vút cong, thân nanh đính vây cá mập và đầu phi lao đinh ba hải vương kèm ngọc trai phát quang.



- **Thiết Kế Lại Rod Băng Giá (`rod_bang_gia`) - Xóa Sạch Hoàn Toàn Gai Nhọn**:



  - Gọt sạch hoàn toàn các gai nhọn/mấu gồ ghề (`sr.triangle` gai góc). Thân cần chuyển thành quyền trượng tinh thể băng vĩnh cửu trơn láng, phẳng mịn và thanh thoát, ôm sát bởi các vòng đai nẹp bạch kim khắc cổ ngữ chìm và vương miện hoa tuyết 6 cánh ở đỉnh ngọn cần.



- **Đại Tu Hiển Thị Đầy Đủ Chi Tiết - Chấm Dứt Hiện Tượng Bị Bóp/Thu Nhỏ ("Anti-Squish")**:



  - *Vòng quay cuộn thưởng Gacha (CS:GO Reel):* Phóng to thẻ cuộn từ 135x158 lên 160x200, nâng bán kính icon từ `26f` lên **`44f`** (đường kính 88px, diện tích điểm ảnh tăng gấp gần 3 lần), hiển thị rõ nét từng chi tiết đồ họa khi con lăn lướt qua.



  - *Bảng Tỷ lệ & Kho Trang bị (Loot Table Modal):* Chuyển đổi toàn bộ từ 3 thanh ngang dẹt 50px sang 3 ô thẻ Showcase đứng rộng rãi (216x142px), nâng bán kính icon từ `15f` lên **`34f`** (diện tích điểm ảnh tăng gấp 4.5 lần!), bao gồm vầng hào quang aura và thông tin món đồ đặt ngay ngắn bên dưới.



  - *Bệ Trưng Bày Banner (Showcase Pedestal):* Mở rộng ô chứa lên 92x92px, nâng bán kính icon từ `24f` lên **`38f`**.



  - *Thẻ Kết Quả Quay Trúng:* Tăng bán kính icon lên **`50f`** (Quay 1 lượt) và **`36f`** (Quay 10 lượt).



  - *Màn hình Equipment:* Nâng bán kính icon lên `23f` trong danh sách hòm đồ và tooltip, bảo đảm sự đồng nhất tuyệt đối giữa hai giao diện.



- **Dynamic Stroke Scaling Trong `EquipmentIconRenderer.java`**:



  - Loại bỏ hoàn toàn các mức sàn nét vẽ cố định quá lớn (như `Math.max(6.5f, ...)` hay `Math.max(7.5f, ...)` từng làm bết dính nét khi co giãn), thay bằng tỷ lệ động `Math.max(1.0f, r * k)` giúp mọi đường nét luôn thanh thoát, sắc sảo chuẩn mực SVG ở mọi kích thước.



- **Bổ Sung Quy Chuẩn Full Representation Vào Agent Skills**:



  - Cập nhật quy chuẩn kiểm duyệt đồ họa trong `vector-graphics-designer` và `game-graphics-pipeline`.







### 🎨 Tái Cấu Trúc Lộ Trình Đồ Họa 5 Bước Triển Khai Cuốn Chiếu ([TASKS.md](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/TASKS.md), [PatchNotesDialog.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/PatchNotesDialog.java))



- **Phân Bổ Kế Hoạch Đồ Họa Thành 5 Bước Triển Khai Dần**:



  - *Bước 1 (v0.5.1):* Ngoại hình 30 Vector Icon 10 Set Trang Bị (loại bỏ Set Thời Không do đã hoàn thiện ở SET-07) & 24 Icon Tiệm Tạp Hóa / Loot Đáy Biển. Được phân rã thành **5 bước nhỏ** để triển khai cuốn chiếu dễ dàng:



    * *Bước 1.1:* 12 Icon 4 Set Trang Bị Khởi Đầu & Cơ Bản 0★ - 3★ (Tập Sự, Thợ Săn, Băng Giá, Đại Dương) — **Đã Hoàn Thành (v0.5.1a)**.



    * *Bước 1.2:* 9 Icon 3 Set Trang Bị Cao Cấp & Huyền Thoại 4★ - 6★ (Hải Tặc, Vực Thẳm, Hoàng Gia; loại bỏ Set Thời Không đã hoàn thiện ở SET-07).



    * *Bước 1.3:* 9 Icon 3 Set Trang Bị Thần Thoại & Giới Hạn Sự Kiện 6★ - 7★ (Quang Minh, Vệ Quốc Vang Danh, Thần Khí Lạc Hồng).



    * *Bước 1.4:* 12 Icon Vật Phẩm Tiêu Hao & Đạo Cụ Chiến Đấu Tiệm Tạp Hóa (Bánh Mì, Thuốc Stamina, Nước Tăng Lực, Bình Xịt, Bùa...).



    * *Bước 1.5:* 12 Icon Mồi Câu, Sách Kỹ Năng & Loot Rác / Kho Báu Đáy Biển (Mồi thơm/rủi ro, 7 sách kỹ năng, rương & loot rác).



  - *Bước 2 (v0.5.2):* Hiện đại hóa khung viền UI bo góc 12px, nút bấm xúc giác ZzFX, thanh Ghost HP bar và dải Tension bar Gradient.



  - *Bước 3 (v0.5.3):* Đại tu ngoại hình 45 loài cá vector thuộc 3 bản đồ cùng 3 Siêu Boss Thế Giới (Thủy Quái Sông Trà, Bạch Tuộc Khổng Lồ, Thần Biển Leviathan).



  - *Bước 4 (v0.5.4):* Bộ hiệu ứng hình ảnh VFX chiêu thức cần thủ (Dưỡng Sức, Giật Kép, Trói Buộc) và kỹ năng Boss (Quẫy Bùn, Bứt Tốc, Sóng Thần...).



  - *Bước 5 (v0.5.5):* Hệ thống Custom Skin Động Cần - Dây - Phao trên PlayScreen theo trang bị đang mặc (ANM-06), hoạt ảnh chuyển động quẫy đuôi cá hình sin, cần câu uốn cong vật lý và băng chuyền Gacha cuộn ngang kiểu CS:GO.







### 📦 Đóng Gói Bản Cài Đặt Phát Hành ([AnglerRPG-v0.4.24.zip](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/AnglerRPG-v0.4.24.zip))



- Build lại tệp nhị phân thực thi game độc lập `AnglerRPG.jar` mới nhất (43.3 MB) thông qua tác vụ `gradlew lwjgl3:jar`.



- Đóng gói trọn gói vào tệp zip: `AnglerRPG-v0.4.24.zip` (42.8 MB) gồm `AnglerRPG.jar` và kịch bản khởi chạy 1-click `Launcher.bat`.



- Dọn dẹp và xóa bỏ tệp zip bản phát hành cũ (`AnglerRPG-v0.4.211.zip`).







---







## 📅 [2026-10-25] - Phiên bản v0.4.23 (Sửa Lỗi Giao Diện Tiền Tệ, Cooldown, Cơ Chế Kéo Dây, Hệ Thống Phím Bấm, Chi Tiết Skill Cá & Kho Nhiệm Vụ Mới)







### 📖 Sổ Tay Thủy Tộc - Cơ Chế Kỹ Năng Của Cá & Giao Diện Tiền Tệ ([CatalogScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/CatalogScreen.java), [Fish.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/entities/Fish.java), [EquipmentIconRenderer.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/EquipmentIconRenderer.java))



- **Hiển Thị Chi Tiết Cơ Chế Kỹ Năng Của Từng Loài Cá**:



  - Bổ sung hệ thống mô tả chi tiết hiệu ứng (`Fish.getSkillDescription`) cho toàn bộ 20 kỹ năng thủy tộc trong game (Quẫy bùn, Lặn sâu, Cuồng nộ, Bứt tốc, Sóng thần, Giật điện, Nhảy vọt, Phun mực mù, Sóng âm, Quét xúc tu, Hút máu, Bão tố, và các nội tại Vỏ cứng, Bền bỉ, Trơn trượt, Ẩn mình, Xúc tu siết).



  - Tự động co giãn chiều cao tooltip thẻ cá theo số lượng kỹ năng của cá, hiển thị bullet vàng kèm tên kỹ năng và mô tả tác động trực quan.



- **Giãn Cách Dòng Hiển Thị Tiền Tệ**:



  - Khắc phục lỗi icon Đồng Vàng và Kim Cương bị dính sát đè lên nhau và chữ số bị chèn dòng trong dock tiền tệ Sổ Tay Thủy Tộc.



  - Tăng chiều cao khung dock lên 54px, phân chia 2 hàng Vàng và Kim Cương với khoảng cách 24px thoáng đãng và bổ sung đường kẻ phân cách tinh tế.



- **Đồng Nhất Mỹ Thuật Icon Đồng Vàng**:



  - Cập nhật `EquipmentIconRenderer.drawGoldCoinIcon`: loại bỏ lỗ vuông ở tâm, đồng nhất mỹ thuật với icon đồng tiền vàng tròn tại Tiệm Tạp Hóa (Shop) và HUD với các lớp kim loại bóng, viền nổi và điểm phản quang lấp lánh.







### 📜 Đổi Mới Kho Nhiệm Vụ Hằng Ngày & Hằng Tuần (Quest Bank Rework) ([QuestManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/QuestManager.java), [FishingManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/FishingManager.java), [PlayScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/PlayScreen.java), [Player.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/entities/Player.java))



- **Kho Nhiệm Vụ Đa Dạng - Triệt Tiêu Trùng Lặp Yêu Cầu**:



  - Loại bỏ hoàn toàn tình trạng các nhiệm vụ trùng lặp mục tiêu (như câu 3, câu 5, câu 7 cá cùng lúc).



  - Xây dựng 12 danh mục nhiệm vụ Hằng Ngày phong phú và độc nhất:



    1. *Ngư Phủ Cần Mẫn*: Câu 5 con cá bất kỳ.



    2. *Canh Góc Chuẩn Xác*: Đạt PERFECT khi quăng cần 3 lần.



    3. *Săn Cá Quý Hiếm*: Câu được 1 cá bậc Rare trở lên.



    4. *Khiêu Chiến Thủy Quái*: Đánh bại 1 Thủy Quái Boss thế giới.



    5. *Vận Dụng Tuyệt Kỹ*: Sử dụng kỹ năng 4 lần khi câu cá.



    6. *Dưỡng Sức Tiếp Năng*: Dùng 2 vật phẩm tiêu hao hỗ trợ.



    7. *Làm Sạch Vùng Nước*: Trục vớt 1 rác thải hoặc rương báu trôi dạt.



    8. *Chiến Lợi Nặng Ký*: Bắt 1 cá có trọng lượng từ 10kg trở lên.



    9. *Kỹ Thuật Điêu Luyện*: Câu thành công 1 cá mà lực căng dây không vượt quá 85%.



    10. *Giao Thương Hội Quán*: Tiêu 150 Vàng tại Tiệm Tạp Hóa.



    11. *Vận Mệnh Biển Cả*: Quay Gacha 1 lần.



    12. *Hải Trình Viễn Du*: Đổi vùng nước câu sang bản đồ khác.



  - Cập nhật 7 danh mục nhiệm vụ Hằng Tuần thử thách và chuyên sâu (Câu 40 cá, 15 Perfect, 3 Boss, 4 Epic+, 2 Kình Ngư 20kg+, 20 Kỹ năng, Tiêu 1.500 Vàng).







### ⏱️ Cây Kỹ Năng - Xóa Ký Tự Ô Vuông & Bố Cục Cooldown ([SkillTreeScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/SkillTreeScreen.java))



- **Xóa Ký Tự Lỗi Font**:



  - Loại bỏ ký tự emoji đồng hồ `⏱` không có trong atlas font Bitmap, thay bằng dấu chấm bullet thanh lịch `•` (`• Thời gian hồi chiêu:`).



- **Tránh Chữ Đè Lên Nút Nâng Cấp**:



  - Điều chỉnh khoảng cách hiển thị hiệu ứng kỹ năng (`curY`) và cố định dòng thông tin Cooldown nằm an toàn ở phía trên nút Nâng Cấp/Mở Khóa, triệt tiêu hoàn toàn lỗi chữ bị nút xanh đè lên.



  - Căn chỉnh dòng nhắc số sách kỹ năng có trong túi nằm cách nút bấm 10px gọn gàng.







### ⌨️ Đại Tu Hệ Thống Phím Bấm & Cơ Chế Hoán Đổi Tự Động ([KeybindManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/KeybindManager.java), [SettingsDialog.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/SettingsDialog.java), [PlayScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/PlayScreen.java))



- **Tự Động Hoán Đổi Phím Khi Trùng (Auto-Swap)**:



  - Khắc phục triệt để lỗi không đổi được keybind trong Cài Đặt do cơ chế chặn trùng phím: Khi người chơi gán một phím đã được hành động khác sử dụng, hệ thống sẽ tự động hoán đổi phím giữa 2 hành động thay vì hủy thao tác.



  - Hiển thị thông báo xác nhận hoán đổi rõ ràng (ví dụ: *"Đã gán phím [W] cho Kỹ Năng 1 (hoán đổi [Q] sang Kỹ Năng 2)"*).



- **Quét Phím Dự Phòng Cho Bộ Gõ Tiếng Việt (IME/Unikey Compatibility)**:



  - Bổ sung vòng lặp quét phím phần cứng trực tiếp `Gdx.input.isKeyJustPressed` trong `SettingsDialog` khi đang chờ gán phím, bảo đảm bắt được phím 100% kể cả khi bộ gõ Unikey/EVKey nuốt sự kiện `InputProcessor.keyDown`.



- **Bảo Vệ Khôi Phục Phím & Đa Dạng Cách Dùng Skill**:



  - Trong `KeybindManager`: Tự động phục hồi về phím mặc định nếu mã phím trong file Preferences bị lỗi hoặc bằng 0.



  - Trong `PlayScreen`: Bổ sung kiểm tra dự phòng phím Q, W, E, R gốc và cho phép kích hoạt kỹ năng bằng cách click chuột trái trực tiếp vào ô kỹ năng trên thanh HUD.







### 🎣 Cân Bằng Lực Căng Dây & Cơ Chế Giằng Co ([FishingManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/FishingManager.java))



- **Lực Căng Dây Khi Kéo (Reeling Tension Feedback)**:



  - Giải quyết thắc mắc của người chơi về việc giữ SPACE kéo dây nhưng thanh Tension không tăng:



    - Trước đây: Việc kéo dây chỉ tính tăng Tension khi cá ở trạng thái `PULLING`. Khi cá ở trạng thái `IDLE`, kéo dây gây sát thương nhưng Tension không hề tăng.



    - Bây giờ: Khi giữ SPACE kéo cá, lực căng dây luôn tăng nhẹ theo nhịp quay cần (`14f + 0.025f * MaxTension`) ngay cả khi cá đang IDLE, và tăng nhanh khi cá đang giằng co PULLING (`26f + 0.055f * MaxTension`), mang lại phản hồi lực căng chân thực.



- **Giới Hạn Sàn Kháng Lực Kéo (Fish Pull Floor)**:



  - Đặt giới hạn sàn tối thiểu 25% cho lực kéo của cá sau khi áp dụng chỉ số giảm lực kéo và kháng đứt dây (`totalPullMult = Math.max(0.25f, ...)`).



  - Ngăn chặn tình trạng trang bị cấp cao triệt tiêu toàn bộ lực kéo của cá xuống 1% khiến thanh lực căng gần như đứng yên, giúp các trận câu luôn có tính tương tác giằng co thú vị.







---







## 📅 [2026-10-25] - Phiên bản v0.4.22 (Sửa Lỗi Kỹ Năng, Cân Bằng Thành Tựu & Tinh Chỉnh Giao Diện)







### ⚔️ Khắc Phục Lỗi Kỹ Năng Chiến Đấu ([FishingManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/FishingManager.java), [DuongSucAbility.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/gas/abilities/DuongSucAbility.java), [TroiBuocAbility.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/gas/abilities/TroiBuocAbility.java))



- **Dưỡng Sức (Channeling Space Fix)**:



  - Khắc phục triệt để lỗi người chơi vẫn có thể bấm/giữ SPACE gây sát thương làm giảm HP cá trong lúc đang vận sức hồi thể lực.



  - Đồng bộ trạng thái vận sức giữa `duongSucChannelTimer` và GAS Gameplay Effect (`effect_duong_suc`); khi đang vận sức, toàn bộ sát thương space bị triệt tiêu hoàn toàn và không kích hoạt điều kiện sẩy cá do chùng dây.



- **Trói Buộc (Reeling Tension Fix)**:



  - Khắc phục triệt để lỗi khi cá đang bị dính hiệu ứng Trói Buộc (đóng băng lực kéo) mà người chơi bấm giữ SPACE kéo dây thì lực căng (Tension) vẫn bị tăng lên.



  - Thiết lập lực kéo cá bằng 0 và tỷ lệ gia tăng lực căng bằng 0 khi cá đang chịu hiệu ứng `isTroiBuoc` / `abilitySystem.isFishFrozen()`.







### 🏆 Cân Bằng Lại Phần Thưởng 48 Mốc Thành Tựu ([QuestManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/QuestManager.java))



- **Điều Chỉnh Định Mức Thưởng Kim Cương Hợp Lý (2 - 30 KC)**:



  - Cân bằng lại toàn bộ 48 mốc thành tựu trong cả 8 nhóm (Câu cá, Perfect Cast, Cá Rare, Kiếm Vàng, Tiêu Vàng, Gacha, Kỹ năng, Cấp độ) theo quy chuẩn kinh tế của game (Tier 1: 2-3 KC, Tier 2: 4-5 KC, Tier 3: 6-8 KC, Tier 4: 10-15 KC, Tier 5: 18-25 KC, Max Tier: 25-30 KC), giải quyết tình trạng lạm phát phần thưởng 400 - 600 KC.



  - Sửa lỗi mốc thành tựu *Sát Thủ Biển Sâu* (`a_fish_5000`) bị sai mục tiêu từ 500 thành chuẩn 5.000 con cá.



  - Cập nhật hàm `initAchievements()` để tự động đồng bộ phần thưởng, tiêu đề và mô tả mới cho cả các tệp lưu game cũ (`SaveManager`).







### 🏮 Hợp Nhất Thưởng Nhiệm Vụ Sự Kiện Vào Rương Mốc ([EventManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/EventManager.java), [EventScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/EventScreen.java))



- **Bỏ Phần Thưởng Đơn Lẻ & Nút Nhận Riêng**:



  - Loại bỏ hoàn toàn phần thưởng đơn lẻ và nút nhận riêng lẻ trên 6 thẻ nhiệm vụ sự kiện hàng ngày.



  - Thẻ nhiệm vụ chuyển đổi trực quan thành huy hiệu trạng thái cố định (`✓ ĐÃ XONG` khi hoàn thành, `ĐANG LÀM` khi chưa xong).



  - Toàn bộ giá trị phần thưởng sự kiện hàng ngày được quy về Rương Mốc Sự Kiện (hoàn thành 4/6 nhiệm vụ để mở rương).







### 📖 Chú Thích Kỹ Năng Động Theo Cấp & Vị Trí Cooldown ([Skill.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/skills/Skill.java), [SkillTree.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/skills/SkillTree.java), [SkillTreeScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/SkillTreeScreen.java), [GameHUD.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/GameHUD.java))



- **Chỉ Số Kỹ Năng Tự Động Thay Đổi Theo Cấp**:



  - Viết lại toàn bộ phần mô tả kỹ năng trong `SkillTree.java`, loại bỏ các con số cố định bị gán tĩnh (như 2s, x2/2.5/3, 10s, 0.5s/cấp).



  - Tách biệt rõ ràng phần mô tả tính năng tổng quan và phần thông số hiệu ứng được tính toán động chính xác theo cấp độ hiện tại (`getCurrentLevelEffectString()`).



- **Chuyển Thông Tin Cooldown Xuống Dưới**:



  - Trên giao diện Cây Kỹ Năng (`SkillTreeScreen`): Gỡ bỏ chữ `• Hồi chiêu:` ở badge đầu trang; đưa thông tin thời gian hồi chiêu xuống phía dưới cùng của khối hiệu ứng.



  - Trên thanh HUD chiến đấu (`GameHUD`): Đưa chỉ số Hồi chiêu (`⏱ Hồi chiêu`) và Tiêu hao thể lực (`⚡ Tốn Thể lực`) xuống đáy khung tooltip hover.







### 🎨 Tinh Chỉnh Giao Diện & Văn Bản Trang Bị ([EquipmentScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/EquipmentScreen.java), [CatalogScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/CatalogScreen.java), [EquipmentIconRenderer.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/EquipmentIconRenderer.java))



- **Tinh Giản Văn Bản Trang Bị**:



  - Đổi *"Độ bền dây (Max Tension):"* thành *"Độ bền dây:"* gọn gàng trên cả danh sách trang bị và popover tổng hợp chỉ số.



  - Loại bỏ từ *"xịn"* khỏi cụm *"Tỉ lệ cá hiếm / xịn:"* thành *"Tỉ lệ cá hiếm:"*.



- **Icon Vector Tiền Tệ Trong Sổ Tay (Catalog)**:



  - Thêm phương thức vector `drawGoldCoinIcon` chuẩn mỹ thuật trong `EquipmentIconRenderer`.



  - Thay thế chữ *"Vàng:"* và *"KC:"* bằng icon vector Đồng Vàng và Kim Cương trực tiếp trên thanh dock số dư tài nguyên của Sổ Tay Thủy Tộc.







---







## 📅 [2026-10-23] - Phiên bản v0.4.21 (Thưởng Kim Cương Sổ Tay Thủy Tộc & Sửa Lỗi Âm Thanh SFX)







### 📖 Bổ Sung Thưởng Kim Cương Sổ Tay Thủy Tộc ([CollectionDatabase.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/data/CollectionDatabase.java), [CatalogScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/CatalogScreen.java), [Player.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/entities/Player.java), [SaveManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/data/SaveManager.java))



- **Thưởng Hoàn Thành 15 Bộ Sưu Tập**:



  - Bổ sung trường `rewardDiamonds` cho toàn bộ 15 bộ sưu tập cá trên cả 3 bản đồ:



    - **Ao Làng**: Dân Dã Ao Làng (10 KC), Gương Mặt Quen Thuộc (15 KC), Kình Ngư Nước Ngọt (20 KC), Sát Thủ Đầm Lầy (35 KC), Truyền Thuyết Sông Hồ (60 KC). Tổng: **140 KC**.



    - **Bãi Biển**: Ven Bờ (15 KC), Bập Bềnh (20 KC), Xa Bờ (25 KC), Thợ Săn Đáy Biển (40 KC), Vua Của Biển Cả (70 KC). Tổng: **170 KC**.



    - **Đảo Xa**: Rạn San Hô (20 KC), Sinh Vật Độc Lạ (25 KC), Cư Dân Đại Dương (35 KC), Hung Thần Răng Cưa (50 KC), Thần Thoại Biển Khơi (100 KC). Tổng: **230 KC**.



    - Tổng cộng thưởng hoàn thành tất cả các bộ sưu tập: **540 Kim Cương**.



- **Thưởng Khám Phá Lần Đầu Từng Loài Cá (Catalog Discovery Diamonds)**:



  - Khi câu được một loài cá mới, ô cá trong Sổ Tay sẽ mở khóa huy hiệu phát sáng `+X 💎 NHẬN`:



    - **Common**: +2 KC



    - **Uncommon**: +3 KC



    - **Rare**: +5 KC



    - **Epic**: +10 KC



    - **Boss**: +25 KC



  - Người chơi có thể nhấp chuột trực tiếp vào ô cá để nhận thưởng riêng lẻ hoặc sử dụng nút **★ NHẬN TẤT CẢ ★** ở góc phải để nhận toàn bộ thưởng cá và bộ sưu tập một chạm.



  - Sau khi nhận, ô cá hiển thị nhãn xanh lá `✓ Đã lưu sổ`. Trạng thái nhận thưởng cá `claimedFishCatalog` được lưu bền vững vào `SaveManager`.



- **Nâng Cấp Giao Diện Sổ Tay**:



  - Bổ sung thanh dock hiển thị số dư tài nguyên **Vàng** và **Kim Cương** trực tiếp trên màn hình Sổ Tay, giúp người chơi thấy số dư tăng lên tức thì khi bấm nhận thưởng.







### 🔊 Bộ Tổng Hợp Âm Thanh Thủ Tục ZzFX & Khắc Phục Lỗi Âm Thanh Triệt Để ([ZzFX.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/ZzFX.java), [AudioManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/AudioManager.java), [sfx_button.wav](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/audio/sfx/sfx_button.wav))



- **Tích Hợp ZzFX Procedural Sound Synthesizer (Java 17 / OpenAL)**:



  - Port trọn vẹn thuật toán sinh sóng âm thanh lượng tử toán học ZzFX (Sine, Triangle, Sawtooth, Tan, Bitnoise, White Noise) trực tiếp vào RAM client game.



  - Tự động sinh mảng sóng âm thanh, đóng gói chuẩn WAV 16-bit PCM Little-Endian và nạp vào OpenAL Sound Engine của LibGDX qua `MemoryFileHandle` ảo.



  - Nặng **0 KB** tài nguyên ngoài đĩa, không tốn I/O đọc ổ cứng, tốc độ sinh trong vài mili-giây.



- **Lưới An Toàn Tự Động (Procedural Fallback Safety Net)**:



  - Khi gọi bất kỳ âm thanh nào qua `AudioManager.playSFX(name)`: Nếu tệp WAV trên đĩa cứng bị thiếu hoặc bị xóa, hệ thống **tự động phân tích tên gọi và sinh âm thanh thủ tục tương ứng ngay lập tức** (Button Click, Catch Success, Fail/Escape, Cast, Bite, Pull, Splash, Coin/Diamond, Skill, Level Up, Reel Click, Alert, Explosion).



  - Triệt tiêu vĩnh viễn nguy cơ văng game `FileNotFoundException` hoặc spam log lỗi ở từng frame.



- **Hỗ Trợ Phát Âm Thanh Thủ Tục Trực Tiếp**:



  - Bổ sung `AudioManager.getInstance().playZzFX(preset)` và `playZzFX(preset, pitch)` cho phép tùy biến cao độ âm thanh động (pitch-shifting) theo trọng lượng cá và cấp độ kỹ năng.







### 🏮 Tối Ưu Hóa Giao Diện Thẻ Nhiệm Vụ Sự Kiện ([EventScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/EventScreen.java))



- **Lược Bỏ Text & Hộp Thưởng Đơn Lẻ**:



  - Xóa bỏ hộp viền vàng và text hiển thị phần thưởng đơn lẻ (+300 Vàng, +15 KC, +1 Vé Quay...) ở góc phải trên từng thẻ nhiệm vụ sự kiện, tránh gây rối mắt và chật chội.



  - Mở rộng không gian hiển thị tiêu đề nhiệm vụ tràn viền thẻ, giúp tên nhiệm vụ hiển thị rõ ràng, thoáng đãng và thẩm mỹ hơn.







### 🚀 Trình Cập Nhật Tự Động Trực Tuyến & Khởi Động Lại 1-Click ([AutoUpdater.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/updater/AutoUpdater.java), [UpdateDialog.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/UpdateDialog.java), [Launcher.bat](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/Launcher.bat))



- **Kiểm Tra Bản Phát Hành Bất Đồng Bộ (GitHub Releases API)**:



  - Tự động kết nối kiểm tra phiên bản mới từ GitHub Releases qua luồng nền (Daemon thread), không gây đơ/lag giao diện game khi khởi động.



  - So sánh phiên bản chuẩn Semantic Versioning (`isNewerVersion`).



  - Hỗ trợ tải trực tiếp qua HTTP redirect (AWS S3 CDN của GitHub Releases) với thanh tiến độ hiển thị phần trăm trực quan.



- **Tự Động Áp Dụng Bản Vá & Tái Khởi Chạy Game**:



  - Khi tải xong bản cập nhật, tự động sinh script thay thế (`update_apply.bat` trên Windows, `update_apply.sh` trên Unix/macOS) để ghi đè file JAR mới và khởi chạy lại game chỉ trong 1 click.



- **Hộp Thoại Quản Lý Bản Cập Nhật In-Game (UpdateDialog)**:



  - Giao diện thiết kế chuẩn Dark UI, hỗ trợ xem ghi chú phát hành, nút tải cập nhật tự động, nút tải thủ công qua trình duyệt và nút đóng/kiểm tra lại.



  - Phím tắt `[U]` hoặc click chuột vào góc phải dưới màn hình chính (MainMenuScreen) để mở bảng cập nhật bất cứ lúc nào.



- **Trình Khởi Chạy Tiện Lợi (Launcher.bat)**:



  - Bổ sung file `Launcher.bat` ở thư mục gốc giúp người chơi mở game 1 chạm, tự động kiểm tra môi trường Java (JRE 11+) và áp dụng bản vá đang chờ.







### ⏰ Đồng Bộ Thời Gian Thực Chuẩn Quốc Tế & Chống Hack Giờ ([TimeSyncService.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/TimeSyncService.java), [EventManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/EventManager.java), [QuestManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/QuestManager.java))



- **Đồng Bộ Thời Gian Chuẩn Quốc Tế (Network Time Protocol / HTTP Date)**:



  - Tự động truy vấn Header Date từ các cụm máy chủ toàn cầu (Google, Cloudflare, GitHub API) ngay khi mở game.



  - Sử dụng đồng hồ đơn điệu `System.nanoTime()` Monotonic Clock để tính thời gian trôi qua, triệt tiêu hoàn toàn khả năng người chơi tua ngược hoặc tua nhanh đồng hồ Windows để hack chu kỳ điểm danh, nhiệm vụ ngày/tuần hay sự kiện.



- **Tích Hợp Toàn Diện Hệ Thống**:



  - Thay thế toàn bộ các điểm truy vấn giờ địa phương trong `EventManager` và `QuestManager` sang `TimeSyncService.getCurrentTimeMillis()`.







### 💥 Bộ Điều Khiển Rung Chấn Màn Hình & "Game Juice" ([ScreenShakeController.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/vfx/ScreenShakeController.java), [JuiceAnimator.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/vfx/JuiceAnimator.java), [PlayScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/PlayScreen.java))



- **Hệ Thống Rung Chấn Màn Hình Trauma-Based (Squirrel Eiserloh GDC Standard)**:



  - Thay thế cơ chế rung ngẫu nhiên cũ (bị lỗi trôi tâm camera) bằng mô hình Trauma `[0.0, 1.0]`. Năng lượng rung phi tuyến `shake = trauma^2` mang lại cảm giác chân thực: rung nhẹ tinh tế khi máy câu chạy, rung dữ dội khi Boss tung skill hay đứt cước.



  - Kết hợp đồng thời rung tịnh tiến `(X, Y)` và rung góc nghiêng `Angle`, tạo cảm giác chấn động vật lý 3D rõ rệt.



  - Hồi quy vị trí tâm gốc tuyệt đối (Zero Camera Drift), không bao giờ bị lệch khung hình sau va chạm.



- **Hiệu Ứng Khựng Đòn Đanh Thép (Hit-Stop / Micro-freeze)**:



  - Khi cá đớp phao (`BITE`) hoặc quăng trúng nhịp: Game tự động khựng lại 45 mili-giây, tạo cảm giác giật cần chắc tay và nặng đòn như các tựa game đối kháng/hành động đỉnh cao.



- **Bộ Nội Suy Easing & Bouncy Animation (JuiceAnimator)**:



  - Cung cấp các đường cong nội suy nảy đàn hồi (SwingOut, ElasticOut, BounceOut, Punch Scale, Pulse) cho popup phần thưởng, thông báo và hiệu ứng hào quang.







### ⚡ Hệ Thống Kỹ Năng Gameplay Ability System (GAS) ([AbilitySystemComponent.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/gas/AbilitySystemComponent.java), [GameplayAbility.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/gas/GameplayAbility.java), [GameplayEffect.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/gas/GameplayEffect.java))



- **Chuẩn Hóa Kiến Trúc Kỹ Năng Theo Chuẩn Công Nghiệp (Unreal Engine GAS Pattern)**:



  - Tách rời toàn bộ logic kỹ năng khỏi vòng lặp câu cá thành các đối tượng `GameplayAbility` độc lập (`KeoManhAbility`, `PhaGiapAbility`, `LuotDayAbility`, `TroiBuocAbility`, `GiatKepAbility`, `DuongSucAbility`, `NemXaAbility`).



  - Mỗi kỹ năng tự đóng gói logic kiểm tra điều kiện, chi phí thể lực, công thức sát thương, hoạt ảnh VFX và rung chấn màn hình.



- **Hệ Thống Hiệu Ứng Trạng Thái (GameplayEffect - Buffs & Debuffs)**:



  - Quản lý tập trung mọi hiệu ứng tăng/giảm sát thương, giảm lực kéo cá, đóng băng cá và hồi phục thể lực theo chu kỳ thời gian (Periodic ticks).



  - Tự động tiêu biến và giải phóng khi hết thời gian hoặc kết thúc trận câu.



- **Quản Lý Hồi Chiêu Động & Tương Thích Tuyệt Đối**:



  - `AbilitySystemComponent` (ASC) tự động đếm lùi thời gian hồi chiêu từng khung hình, loại bỏ hàng chục biến timer/cooldown rời rạc trước đây.



  - Tương thích 100% với hệ thống gán phím QWER, thanh HUD hiển thị và bộ trang bị Thời Không (-20% Cooldown).



- **Làm Mới Hồi Chiêu Tức Thì Sau Mỗi Lần Câu (Instant Cooldown Reset)**:



  - Tất cả kỹ năng (Kéo Mạnh, Giật Kép, Phá Giáp, Lướt Dây, Trói Buộc, Dưỡng Sức) được tự động hồi lại 100% (cooldown về 0) ngay khi kết thúc trận câu hoặc bắt đầu lượt câu mới, không bao giờ bị dính thời gian hồi chiêu từ trận trước.



- **Cân Bằng Thời Gian Hồi Chiêu Dựa Trên Sức Mạnh Thực Chiến**:



  - **Kéo Mạnh**: Điều chỉnh về **`3.5s`** (đòn sát thương chính, căn nhịp kéo thường bằng Space).



  - **Giật Kép**: Tăng từ `5.0s` lên **`8.0s`** (sát thương bộc phá x3.0 cực lớn, cần tính toán thời điểm dùng).



  - **Phá Giáp**: Giảm từ `20.0s` xuống **`14.0s`** (hiệu ứng debuff 10s, downtime 4s, vừa đủ kích hoạt 2 lần trong trận đánh Boss lớn).



  - **Lướt Dây**: Giảm từ `15.0s` xuống **`12.0s`** (thời gian hiệu lực 5-7s, xen kẽ nhịp phòng ngự an toàn).



  - **Trói Buộc**: Giảm từ `25.0s` xuống **`16.0s`** (khống chế cứng phong ấn cá, dùng ngắt chiêu Boss hoặc cứu dây).



  - **Dưỡng Sức**: Cân bằng chuẩn **`9.0s`** (vận sức 1-2s hồi 50 Thể lực, hồi sức bền bỉ khi câu cá lớn).



- **Bổ Sung Thông Tin Cooldown & Tooltip Trực Quan**:



  - Hiển thị nhãn `⏱ Hồi chiêu: X.Xs` trực tiếp trong bảng chi tiết kỹ năng của `SkillTreeScreen`.



  - Bổ sung popover tooltip khi rê chuột vào các ô kỹ năng `[Q]`, `[W]`, `[E]`, `[R]` trong màn hình chiến đấu câu cá, hiển thị đầy đủ Tên, Phím tắt, Hồi chiêu, Chi phí Thể lực và Mô tả chi tiết.







### 🛡️ Rà Soát Bảo Mật & Ẩn Tệp Tin Riêng Tư Trên GitHub ([.gitignore](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/.gitignore))



- **Bảo Vệ Tệp Lưu Dữ Liệu Người Chơi**:



  - Ẩn toàn bộ `angler_save*.json`, `save_*.json`, `*.sav`, `*.save`, `*.save.json` để người chơi/lập trình viên không vô tình đẩy dữ liệu lưu cá nhân lên kho lưu trữ công khai GitHub.



- **Loại Bỏ Bộ Đệm & Môi Trường Ảo**:



  - Chặn triệt để `__pycache__/`, `*.py[cod]`, `venv/`, `.venv/`, `.pytest_cache/`.



- **Bảo Vệ Thông Tin Nhạy Cảm**:



  - Chặn các file cấu hình môi trường bí mật `.env*`, chứng chỉ `*.key`, `*.pem`, `*secret*`, `*token*`, `*credentials*`.



- **Cách Ly Tệp Tạm Của Auto-Updater**:



  - Bổ sung `update_apply.bat`, `update_apply.sh`, `AnglerRPG-update.tmp`, `AnglerRPG.jar` vào `.gitignore`.







---







## 📅 [2026-10-23] - Phiên bản v0.4.20 (Cố Định 6★ Lạc Hồng, Cân Bằng 42 Loài Cá Thường & Đại Tu Kinh Tế Kim Cương)







### 👑 Cố Định Phẩm Cấp Set Thần Khí Lạc Hồng & Khóa Roll Không Nổ ([EquipmentGenerator.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/entities/equipment/EquipmentGenerator.java), [SaveManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/data/SaveManager.java))



- **Cố Định Độ Hiếm 6★ (LIMITED_PREMIUM)**:



  - Khắc phục triệt để lỗi Set Thần Khí Lạc Hồng xuất hiện ở các phẩm cấp thấp (Common, Uncommon, Rare, Epic).



  - Thiết lập ràng buộc bất biến (Hard Invariant): Bất kỳ trang bị nào thuộc Set Thần Khí Lạc Hồng đều luôn cố định ở phẩm cấp cao nhất 6★ (`Rarity.LIMITED_PREMIUM`).



- **Khóa Chặt Khỏi Roll Không Nổ**:



  - Loại bỏ hoàn toàn Set Thần Khí Lạc Hồng và Set Vệ Quốc Vang Danh khỏi Vòng quay Thường (`rollGacha`).



  - Trong Vòng quay Sự Kiện (`rollGachaLimited`): Set Lạc Hồng chỉ có thể xuất hiện khi **NỔ VÀNG 6★** (`Rarity.LIMITED_PREMIUM`). Tuyệt đối không lọt vào các lượt roll thường / roll xịt (Common, Uncommon, Rare, Epic).



- **Tự Động Nâng Cấp Kho Đồ**: Bộ nạp dữ liệu (`SaveManager`) tự động kiểm tra và nâng cấp toàn bộ trang bị Lạc Hồng trong túi đồ người chơi về chuẩn 6★ `LIMITED_PREMIUM`.







### 🐟 Đại Tu Cân Bằng Toàn Diện 42 Loài Cá Thường ([fishes.json](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/data/fishes.json))



- **Mục Tiêu & Tiêu Chuẩn Trải Nghiệm Đối Kháng**:



  - Khắc phục hiện tượng cá thường bị người chơi có trang bị trung cấp giật chết chỉ trong 1–2 giây, khôi phục trải nghiệm câu cá chân thực và căng thẳng.



  - Chuẩn hóa thời gian giằng co (Target TTK):



    - **COMMON (Phổ thông)**: Đạt **8s – 14s** (vừa đủ cảm nhận nhịp kéo và thanh lực căng).



    - **UNCOMMON (Hiếm vừa)**: Đạt **15s – 22s** (đòi hỏi người chơi kiểm soát nhả/nhấp phím Space qua 1 chu kỳ cá vùng vẫy).



    - **RARE (Hiếm)**: Đạt **22s – 32s** (yêu cầu quản lý thể lực và sức bền dây cước chặt chẽ).



    - **EPIC (Sử thi)**: Đạt **35s – 50s** (trận đấu cam go, phát huy tối đa kỹ năng trang bị).



- **Chi Tiết Điều Chỉnh Chỉ Số Cả 3 Bản Đồ**:



  - **Bản đồ 1 - Ao Làng (ao_lang)**:



    - *Common*: Cá Mương (HP 25 -> 55, TTK ~7s), Cá Diếc (HP 30 -> 65, TTK ~9s), Cá Lóc Nhí (HP 35 -> 70, TTK ~10s), Cá Rô Đồng (HP 40 -> 75, TTK ~11s), Cá Mè (HP 50 -> 80, TTK ~15s), Cá Chép (HP 80 -> 95, TTK ~18s).



    - *Uncommon*: Cá Trê (HP 60 -> 130, TTK ~14s), Cá Chim Trắng (HP 65 -> 140, TTK ~15s), Cá Trôi (HP 70 -> 150, TTK ~20s), Cá Trắm Cỏ (HP 90 -> 165, TTK ~19s).



    - *Rare*: Cá Lóc Bông (HP 120 -> 240, TTK ~25s), Cá Trê Lai (HP 150 -> 250, TTK ~31s).



    - *Epic*: Cá Chép Vàng (HP 200 -> 360, TTK ~36s), Cá Lăng Khổng Lồ (HP 250 -> 400, TTK ~39s).



  - **Bản đồ 2 - Bãi Biển (bai_bien)**:



    - *Common*: Cá Cơm (HP 30 -> 85), Cá Mòi (HP 40 -> 95), Cá Chỉ Vàng (HP 45 -> 105), Cá Lẹp (HP 50 -> 110), Cá Đục (HP 55 -> 120), Cá Bạc Má (HP 60 -> 130).



    - *Uncommon*: Cá Trích (HP 75 -> 190), Cá Đối (HP 85 -> 200), Cá Nục (HP 90 -> 210), Cá Chim Biển (HP 110 -> 220).



    - *Rare*: Cá Mú (HP 200 -> 340), Cá Bóp (HP 250 -> 380).



    - *Epic*: Cá Thu Hoàng Kim (HP 400 -> 550), Cá Ngừ Đại Dương (HP 500 -> 640).



  - **Bản đồ 3 - Đảo Xa (dao_xa)**:



    - *Common*: Cá Hề (HP 120 -> 180), Cá Phèn (HP 140 -> 200), Cá Ngựa Vằn (HP 150 -> 210), Cá Nóc (HP 160 -> 220), Mực Ống (HP 180 -> 240), Cá Đuối Nhỏ (HP 200 -> 260).



    - *Uncommon*: Cá Chuồn (HP 250 -> 320), Cá Cờ (HP 280 -> 350), Cá Mặt Trăng (HP 300 -> 370), Rùa Biển (HP 350 -> 420).



    - *Rare*: Cá Kiếm (HP 550 -> 620), Cá Mập Cáo (HP 600 -> 680).



    - *Epic*: Mực Khổng Lồ (HP 1,200 -> 1,100, TTK ~55s), Cá Voi Lưng Gù (HP 1,500 -> 1,300, TTK ~77s).



- **Cân Bằng Kinh Tế Vàng & EXP Tương Xứng**:



  - Nâng phần thưởng Vàng và EXP cho toàn bộ 42 loài cá theo tỷ lệ độ khó tương ứng, bảo đảm công sức giằng co lâu hơn mang lại lợi ích tài nguyên vượt trội cho người chơi.







### 💎 Cân Bằng Toàn Diện Nguồn Kiếm Kim Cương (Chống Lạm Phát Currency)



- **Vấn Đề Trước Đây**:



  - Tỷ lệ rơi Kim Cương (KC) từ câu cá hiếm đạt 100%, mỗi 10 con cá câu được thưởng thêm 5 KC, Rương ngày thưởng 20 KC, Rương tuần 100 KC, Nhiệm vụ sự kiện cho tới 55 KC/ngày và bảng Thành tựu trao tới 10,000 KC.



  - Hậu quả: Người chơi F2P tích lũy hơn 5,000 – 7,000 KC/tháng (đủ quay 250 – 350 lượt, tương đương 3 – 4 chu kỳ bảo hiểm Pity 80), làm mất hoàn toàn độ quý hiếm của tài nguyên và vòng quay Gacha.



- **Giải Pháp & Thông Số Chuẩn Hóa**:



  - **Rơi Từ Câu Cá ([FishingManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/FishingManager.java))**:



    - Cá Rare: Giảm từ 100% xuống **20% tỷ lệ rơi 1 KC** (tạo cảm giác bất ngờ khi bắt được cá quý).



    - Cá Epic: 50% rơi 1 KC, 50% rơi 2 KC (trước đây 2–3 KC cố định).



    - Boss: Chuẩn hóa rơi **3 – 5 KC** (trần nhân thêm từ Lưới tối đa +2 KC).



  - **Nội Tại Cấp Độ & Cần Thủ ([PlayerProgression.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/entities/PlayerProgression.java))**:



    - Thăng cấp thường: Thưởng **+2 KC**; riêng mỗi mốc 5 cấp (Lv.5, 10, 15, 20...) thưởng lớn **+10 KC**.



    - Cần thủ kiên trì: Chuyển mốc nhận thưởng thành **mỗi 25 con cá câu thành công thưởng +3 KC** (giảm tỷ lệ lạm phát từ 0.5 KC/cá xuống 0.12 KC/cá).



  - **Rương Nhiệm Vụ Hằng Ngày & Tuần ([QuestScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/QuestScreen.java))**:



    - Rương Ngày (100 điểm): Chuẩn hóa **10 KC**, tăng bù lên 250 Vàng và 250 XP (trước đây 20 KC).



    - Rương Tuần (100 điểm): Chuẩn hóa **40 KC**, tăng bù lên 1,500 Vàng và 1,500 XP (trước đây 100 KC).



  - **Nhiệm Vụ Sự Kiện & Điểm Danh ([EventManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/EventManager.java))**:



    - Nhiệm vụ sự kiện riêng lẻ: Điều chỉnh từ 10–20 KC xuống **3–8 KC**.



    - Rương Mốc 4/6 Nhiệm vụ sự kiện: Chuẩn hóa **10 KC** (trước đây 20 KC).



    - Điểm danh 25 ngày trong tháng: Tổng thưởng 135 KC (trước đây 215 KC), kết hợp vé quay thường và vé cao cấp.



  - **Bảng Thành Tựu ([QuestManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/QuestManager.java))**:



    - Tinh chỉnh toàn diện hệ số thưởng từ 5 KC (mốc sơ cấp) đến 300 – 600 KC (cho các mốc tối thượng 5,000 cá, 1,000,000 vàng, 1,000 lượt gacha), thay thế mức vô lý 2,000 – 10,000 KC trước đây.



- **Hiệu Quả Thực Nghiệm**:



  - Thu nhập Kim Cương F2P trung bình đạt **1,700 – 2,300 KC/tháng (~85 – 115 lượt quay sự kiện)**, tương ứng đúng **1 đến 1.2 chu kỳ Pity 80 mỗi tháng**. Đảm bảo giá trị cao cấp của trang bị Thần Khí Lạc Hồng và kích thích động lực cày cuốc dài hạn.







---







## 📅 [2026-10-23] - Phiên bản v0.4.19 (Đại Tu Cân Bằng Tiến Trình EXP, Kinh Tế Cửa Hàng & Sức Mạnh Boss)







### 📈 Đại Tu Tiến Trình EXP & Mở Khóa Bản Đồ ([PlayerProgression.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/entities/PlayerProgression.java))



- **Khắc phục Bức Tường EXP (EXP Wall Bug)**:



  - Công thức cũ nhân 10 mỗi 3 cấp khiến lượng EXP lên cấp 20 bị đẩy lên tới 20,000,000 XP (đòi hỏi câu hơn 363,000 con cá, tương đương 18 năm cày cuốc).



  - Thay thế hoàn toàn bằng công thức đa thức tăng trưởng mượt mà: $XP(\text{Level}) = 35 \times \text{Level}^{1.65} + 40$.



- **Tiến Trình Mở Khóa Bản Đồ Khoa Học**:



  - **Mở Bãi Biển (Map 2 - Lv.10)**: Cần tích lũy 5,485 XP (~200 con cá tại Ao Làng, khoảng 3–5 ngày chơi).



  - **Mở Đảo Xa (Map 3 - Lv.20)**: Cần tích lũy ~35,345 XP (~575 con cá tại Bãi Biển, khoảng 7–14 ngày cày cuốc).







### 🐉 Tăng Máu & Độ Giằng Co Cho 3 Boss Thế Giới ([fishes.json](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/assets/data/fishes.json))



- **Thủy Quái Sông Trà (Boss Ao Làng - Map 1)**: Máu tăng từ 600 HP lên **850 HP** (Thời gian câu trung bình đạt 104s với đồ Thợ Săn).



- **Bạch Tuộc Khổng Lồ (Boss Bãi Biển - Map 2)**: Máu tăng từ 1,200 HP lên **2,200 HP**.



- **Thần Biển Leviathan (Boss Đảo Xa - Map 3)**: Máu tăng từ 3,500 HP lên **5,500 HP** (Thời gian câu với đồ Thần Khí Lạc Hồng đạt 164s kịch tính, không còn bị sốc sát thương chết trong vài giây).







### 💰 Cân Bằng Cửa Hàng & Kiểm Soát Lạm Phát Vàng ([FishingManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/FishingManager.java))



- **Đặt Trần Thưởng Cho Lưới Đánh Cá**:



  - Tiền thưởng Vàng nhân thêm khi bắt được cá tối đa không vượt quá **400 Vàng**.



  - Kim Cương thưởng thêm tối đa không vượt quá **5 KC**, xóa bỏ hoàn toàn lỗ hổng farm 16,000 Vàng và 40 KC mỗi lượt kéo Boss tại Đảo Xa.



- **Đặt Trần Thưởng Cho Bùa Hoàng Kim**:



  - Tiền thưởng Vàng +50% được khống chế trần tối đa **500 Vàng** mỗi lần câu.







### 🎰 Chuẩn Hóa Cơ Chế Rate-Up 50% Gacha Sự Kiện ([EquipmentGenerator.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/entities/equipment/EquipmentGenerator.java))



- Khi quay trúng phẩm cấp 6★ (Thần Khí): Tỷ lệ ra đúng Set Thần Khí Lạc Hồng chiếm **50%**. Ba món sự kiện (Trượng Lạc Hồng, Dây Rồng Thiêng, Móc Âu Cơ) có xác suất chia đều chính xác **33.33%** mỗi món.



- 50% còn lại là các Set 6★ tiêu chuẩn (Quang Minh, Thời Không, Hoàng Gia).



- Bảo hiểm Pity 80 lượt chắc chắn nhận 1 trang bị 6★.







---







## 📅 [2026-10-22] - Phiên bản v0.4.18 (Đại Tu Ngoại Hình Toàn Diện Set Thời Không & Khởi Động Vector Studio MCP Pipeline)







### ⏳ Đại Tu Ngoại Hình Toàn Diện Bộ Trang Bị Thời Không ([EquipmentIconRenderer.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/EquipmentIconRenderer.java))



- **Trượng Thời Gian (Chrono Staff)**:



  - **Khắc phục lỗi hiển thị**: Phiên bản cũ vòng hào quang bị vẽ quá khổ (`r * 1.30f`) và cụm đầu gậy bị lệch tâm (`cx + r * 0.55f`) khiến hình ảnh tràn và biến dạng ngoài khung icon.



  - **Thiết kế mới Chuẩn Thiên Văn Hoàng Đạo (Astrolabe Design)**:



    - Căn chỉnh tâm điểm tuyệt đối `(cx, cy)` với tỷ lệ mở rộng `scale = (r * 2f) / 128f`.



    - Trục cán gậy hợp kim thời không màu Titanium tím thẫm (`#1B122C`), bọc vòng bảo hộ và khớp nối mạ vàng hoàng gia (`#F5D061`).



    - Bánh răng thiên văn 12 nan hoa xoay quanh tâm, gợi nhắc 12 khắc giờ trong vũ trụ.



    - Kim la bàn đồng hồ thời gian vát góc nhọn phát quang.



    - Lõi thời không quả cầu pha lê ngọc lam (Cyan Opal `#45F3FF`) đính ở đầu trượng, bao bọc bởi 2 vòng hào quang tinh vân pulsing (`#8B5CF6`).



- **Sợi Dây Xuyên Thấu (Transdimensional Line)**:



  - **Khắc phục lỗi hiển thị**: Phiên bản cũ chỉ là các vòng tròn đồng tâm phẳng, không có hình khối cuộn dây cước hay điểm nhấn thị giác.



  - **Thiết kế mới Cuộn Dây Tachyon Lồng Đôi (Tachyon Spool & Quantum Conduit)**:



    - Khung lõi chỉ bạc Titanium dạng đĩa quay thiên hà với 4 nan trục chữ thập.



    - 8 vòng sóng năng lượng cộng hưởng gradient đa sắc từ tím đậm (`#6D28D9`) sang xanh neon (`#06B6D4`) và trắng tinh khôi (`#FFFFFF`).



    - Chùm vệt phân rã photon lượng tử xuyên không gian đan chéo góc 45°, biểu trưng cho sợi dây xuyên qua các chiều không-thời gian.



    - Vòng vành ngoài mạ vàng cơ khí bấm giờ, có các khấc chia tọa độ thời gian sắc nét.



- **Lưỡi Câu Hư Không (Void Singularity Hook)**:



  - **Khắc phục lỗi hiển thị**: Phiên bản cũ có vòng tròn aura đen che phủ gần hết chi tiết lưỡi câu, tỷ lệ ngạnh và thân lưỡi bị lệch.



  - **Thiết kế mới Đường Cong Không-Thời Gian Möbius (Möbius Topology & Singularity Core)**:



    - Thân lưỡi câu uốn lượn theo hình học vi phân Topology của dải Mobius với đường uốn kép mềm mại.



    - Ngạnh móc đôi sắc nhọn mạ Chrome sáng bóng, đầu mũi kim cương Titanium sắc bén.



    - Hố đen kỳ dị (Void Singularity) ngự trị ở mắt xỏ dây, phát ra hào quang cực tím (`#7C3AED` và `#C084FC`) hút ánh sáng xung quanh.



    - Các hạt bụi thời gian (Starlight Motes) lấp lánh li ti tỏa ra từ thân lưỡi câu.



- **Tương Thích Mọi Giao Diện**:



  - Tối ưu hóa cho cả 3 chế độ hiển thị: Icon 40x40 trong túi đồ, Icon phóng đại 2X (bán kính 38f) trong Modal Chi Tiết Gacha, và hiển thị trên thanh trang bị [EquipmentScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/EquipmentScreen.java).







### 🎨 Tích Hợp Antigravity Vector Studio MCP Server & Pipeline Đồ Họa



- **Custom MCP Server cho Vector Graphics ([.agents/plugins/vector-studio/](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/.agents/plugins/vector-studio))**:



  - `render_vector_preview`: Biên dịch trực tiếp SVG thành hình ảnh PNG xem trước tức thì độ phân giải cao.



  - `audit_vector_artwork`: Kiểm tra tự động 6 tiêu chuẩn nghiêm ngặt (tọa độ gốc viewBox 128x128, tỷ lệ co giãn, độ tương phản màu, phân tầng layer, tối ưu số lượng phần tử).



  - `svg_to_libgdx_code`: Tự động biên dịch mã XML SVG thành hàm Java LibGDX ShapeRenderer chuẩn xác từng pixel.



  - `cleanup_pipeline_files`: **Bước cuối cùng của pipeline** - Tự động dọn dẹp các file preview so sánh cũ (`old_*`), file nháp tạm (`temp_*`, `test_*`, `draft_*`) và tài nguyên trung gian sau khi đã áp dụng code chuẩn xác vào game.



- **Visual Quality Gate (100/100 Điểm)**:



  - Cả 3 asset `rod_thoi_khong`, `line_thoi_khong`, `hook_thoi_khong` đều đã vượt qua bộ kiểm duyệt với điểm số tuyệt đối 100/100 PASSED.



### 📊 Khởi Động Hạ Tầng Mô Phỏng & Cân Bằng Game (Game Data & Balance Pipeline)



- **Tích Hợp Thư Viện Toán Học & Mô Phỏng Mã Nguồn Mở**:



  - `SimPy` & `NumPy`: Mô phỏng sự kiện thời gian thực (Monte Carlo Combat Tension) đo đạc TTK (Time-To-Catch) và tỷ lệ đứt dây câu giữa mọi loại cá vs trang bị.



  - `Mesa` & `Pandas`: Mô hình hóa tiến trình kinh tế đa tác tử (Agent-Based Lifecycle Model) theo dõi dòng chảy Vàng, Kim Cương, lạm phát và số lượt gacha sau 30-90 ngày.



  - `SciPy` & `SymPy`: Giải bài toán tối ưu hóa phi tuyến tính, tự động fit đường cong máu cá (Fish HP Power-Law Curve) và sát thương cần câu thỏa mãn target TTK chuẩn.



- **Bộ Công Cụ CLI & MCP Server ([scripts/balance/](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/scripts/balance))**:



  - `data_extractor.py`: Tự động trích xuất dữ liệu gốc trực tiếp từ `fishes.json` và code Java (`EquipmentGenerator.java`, `QuestManager.java`).



  - `combat_simulator.py`: Mô phỏng 500+ trận kéo cá, tính toán Win Rate, Mean TTK, Median TTK, Line Snap Rate.



  - `gacha_simulator.py`: Giải bài toán Coupon Collector với bảo hiểm Pity 80 và 50% Rate-up.



  - `economy_model.py`: Mô phỏng tác tử người chơi (Casual, Grinder, Hardcore).



  - `curve_solver.py`: Tự động tìm bộ tham số HP và sát thương lý tưởng.



  - `balance_cli.py`: Giao diện dòng lệnh hợp nhất cho toàn bộ tác vụ cân bằng.



- **Antigravity Customization Plugin & Skill**:



  - Tạo mới MCP Server `balance-studio` ([.agents/plugins/balance-studio/](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/.agents/plugins/balance-studio)).



  - Bổ sung Skill [game-balance-pipeline](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/.agents/skills/game-balance-pipeline/SKILL.md) và bộ quy chuẩn [balance_guidelines.md](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/.agents/rules/balance_guidelines.md).







---







## 📅 [2026-10-22] - Phiên bản v0.4.17 (Hệ Thống Cài Đặt Settings, Tùy Biến Keybinds, Kéo Thả Âm Lượng Độc Lập, Phím F11 & Khắc Phục Lỗi UI)







### 🎛️ Nâng Cấp Hệ Thống Cài Đặt & Quản Lý Dữ Liệu ([SettingsDialog.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/ui/SettingsDialog.java))



- **Khắc phục triệt để lỗi chữ bị đè xuyên Modal Xóa Dữ Liệu**:



  - Khi mở Modal Cảnh Báo Xóa Dữ Liệu (`showResetModal == true`), tự động ẩn toàn bộ các dòng chữ và nút bấm của Tab Dữ Liệu bên dưới.



  - Phủ thêm lớp màn mờ làm tối toàn màn hình (`alpha = 0.88f`), loại bỏ 100% hiện tượng các nút "XUẤT DỮ LIỆU", "NẠP DỮ LIỆU" lọt thỏm vào giữa thông báo cảnh báo đỏ.



- **Đồng bộ chuẩn xác 100% Hitbox nút bấm Tab Dữ Liệu**:



  - Đồng bộ hóa các hằng số tọa độ Y giữa xử lý input (`handleInput`) và render (`drawDataTabShapes`, `drawDataTabTexts`), chấm dứt hoàn toàn hiện tượng lệch 35px khi người chơi nhấp chuột vào nút Xóa dữ liệu.



- **Âm thanh Drag Thả Mượt Mà & Tắt Âm Độc Lập**:



  - Tách biệt hoàn toàn trạng thái Tắt Âm (`isBgmMuted` và `isSfxMuted`) trong [AudioManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/AudioManager.java). Giờ đây người chơi có thể tắt nhạc nền mà vẫn nghe rõ hiệu ứng tiếng quăng cần/kéo cá, hoặc ngược lại.



  - Cho phép giữ và kéo chuột (Drag) trực tiếp trên thanh âm lượng để điều chỉnh tỉ lệ % âm lượng tức thì.



- **Tính Năng Thay Đổi Toàn Bộ 14 Phím Bấm & Cơ Chế Chống Trùng Phím (Anti-Collision)**:



  - Tạo mới [KeybindManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/systems/KeybindManager.java) quản lý và lưu cấu hình phím vào `Preferences` (`FishingRPG_Keybinds`).



  - Hỗ trợ đổi phím cho cả 14 thao tác: **Kéo Dây / Giằng Co**, **4 ô Kỹ Năng** và **toàn bộ 9 ô Phím Tắt Nhanh (Quickslot 1 đến 9)**.



  - **Cơ Chế Bảo Vệ Tránh Trùng Keybind**: Tự động phát hiện khi phím mới bị trùng với một thao tác khác, phát âm thanh cảnh báo lỗi `sfx_fail`, hiển thị thông báo đỏ và chặn không cho gán trùng.



  - **Bố Cục Danh Sách Cuộn Dọc Duy Nhất (Single-Column Scrollable List)**: Thay thế bố cục 2 cột bằng một danh sách cuộn dọc liền mạch duy nhất, tích hợp thanh cuộn mượt mà hỗ trợ kéo thả chuột (drag), con lăn bánh xe và phím mũi tên Lên/Xuống.



  - **Nâng Cấp Giao Diện Chuẩn Vibe Game Fantasy RPG**:



    - Khung viền kép hoàng gia mạ vàng, 4 góc đính họa tiết Corner Ornaments 2 tầng với đá quý.



    - Hàng thẻ phím bấm làm bằng kính Obsidian Glass cao cấp với dải phản quang specular, hiệu ứng viền neon cyan/amber khi hover.



    - Nút bấm phím hiển thị nhãn phím rõ ràng, khi bấm chờ gán phím mới sẽ phát sáng aura vàng pulsing nổi bật.



    - Tab Âm Thanh nâng cấp thành bàn trộn âm: Khung thẻ Obsidian độc lập cho BGM và SFX, thanh trượt âm lượng rộng 160px có rãnh khấc chia độ (25%, 50%, 75%), núm trượt kim loại mạ vàng phát sáng, nút bật/tắt rune màu Emerald/Crimson.



    - Tab Hiển Thị nâng cấp thành 2 thẻ lựa chọn Toàn Màn Hình & Cửa Sổ lớn có badge `[✓ ĐANG KÍCH HOẠT]`, kèm mẹo phím F11.



    - Tab Dữ Liệu nâng cấp thành 3 thẻ tùy chọn lớn với mã màu riêng biệt (Xuất = Sapphire, Nạp = Gold, Xóa = Ruby).



  - Tích hợp nút **"ĐẶT LẠI MẶC ĐỊNH"** để khôi phục cấu hình ban đầu chỉ bằng một cú nhấp.



  - Tự động đồng bộ tên phím tùy biến lên thanh slot kỹ năng trong COMBAT, badge góc trên bên trái của từng ô Quickslot và các thông báo cắn câu.



- **Phím Tắt F11 Toàn Cục**:



  - Tích hợp bộ lắng nghe phím F11 tại [FishingRPG.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/FishingRPG.java), cho phép chuyển đổi nhanh giữa Toàn Màn Hình và Cửa Sổ 1280x720 tại bất kỳ màn hình nào trong game.



- **Tối Giản Hóa Giao Diện & Làm Sạch Chú Thích Cài Đặt**:



  - Xóa bỏ toàn bộ các dòng chữ chú thích cách sử dụng rườm rà ở dưới đáy của cả 4 tab trong Cài đặt (Âm thanh, Hiển thị, Phím bấm, Dữ liệu), giúp giao diện thông thoáng, tinh gọn và sang trọng.



  - Rà soát và xóa bỏ các tên phím cứng để trong ngoặc đơn (như "phím Space", "(1-9)") trong HUD và tin tức, đổi thành nhãn chung "thanh Phím Tắt" và tên phím động.



  - Ngẫu nhiên hóa bản tin News Ticker xuất hiện ban đầu khi mở Game HUD, chấm dứt tình trạng lúc nào cũng bắt đầu bằng "Tin vực biển: Xuất hiện một luồng cá...".







### 🎛️ Nút Cài Đặt [⚙] Trên Game HUD



- **Vị trí chuẩn xác theo yêu cầu**: Đặt tại `X = 1220f, Y = 610f, Size = 40x40`, nằm ngay bên dưới nút Thông tin `ⓘ` (`X = 1220f, Y = 660f`).



- **Thiết kế Vector Icon Bánh Răng**: Tạo hình bánh răng 8 nan tỏa và trục rỗng sắc sảo, có hiệu ứng xoay nhẹ và đổi màu highlight khi hover.



- **Giới hạn hiển thị chuyên biệt**: Chỉ xuất hiện trong Game HUD trạng thái rảnh rỗi (`State.IDLE`), tự động ẩn đi khi người chơi bước vào màn hình câu cá để giữ giao diện thông thoáng.







### 🧹 Tối Giản Hóa Menu Chính ([MainMenuScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/screens/MainMenuScreen.java))



- **Loại bỏ toàn bộ các nút thừa**: Bỏ hoàn toàn các nút "XUẤT DỮ LIỆU", "NẠP DỮ LIỆU", "RESET DỮ LIỆU" cùng các modal cảnh báo trước đây ở Menu chính.



- **Menu chính chỉ còn 2 lựa chọn cốt lõi**:



  1. `"BẮT ĐẦU CÂU"`: Vào ngay bến cảng câu cá.



  2. `"THOÁT"`: Đóng ứng dụng an toàn.



- Toàn bộ tính năng quản lý và sao lưu dữ liệu nay đã được quy tụ tập trung trong cửa sổ Cài Đặt.







---







## 📅 [2026-10-22] - Phiên bản v0.4.16 (Khắc Phục Triệt Để Crash Bộ Nhớ, Sửa Phím QWER, Sửa Điều Hướng Bản Đồ & Thêm Bảng Tin Cập Nhật Trực Quan)







### 🛡️ Khắc Phục Triệt Để Lỗi Crash Bộ Nhớ Native (JNI DirectByteBuffer Heap Exception)



- **Tái cấu trúc Quản lý ShapeRenderer Tập trung (Singleton Resource Architecture)**:



  - Khởi tạo một đối tượng `ShapeRenderer` dùng chung duy nhất trong [FishingRPG.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/FishingRPG.java) xuyên suốt vòng đời trò chơi.



  - Loại bỏ hoàn toàn 10 vị trí cấp phát `new ShapeRenderer()` và giải phóng `sr.dispose()` riêng lẻ tại các màn hình (`EquipmentScreen`, `GachaScreen`, `ShopScreen`, `SkillTreeScreen`, `QuestScreen`, `EventScreen`, `CatalogScreen`, `MapScreen`, `MainMenuScreen`, `PlayScreen`).



  - Chấm dứt 100% hiện tượng vỡ cấu trúc bộ nhớ Windows Heap (`ntdll.dll!RtlAllocateHeap` và `msvcrt.dll!memcpy` gây mã lỗi `0xc0000005 ACCESS_VIOLATION`) khi người chơi đóng mở qua lại giữa Gacha, Trang Bị, Cửa Hàng và HUD.



- **Sửa Lỗi Use-After-Free trong `EquipmentScreen.java`**:



  - Bổ sung lệnh bảo vệ `if (game.getScreen() != this) return;` ngay sau hàm `update(delta)`.



  - Loại bỏ các lệnh gọi `dispose()` thủ công khi ấn nút `← QUAY LẠI` hoặc phím `ESC`, ngăn chặn việc OpenGL vẽ trên tài nguyên đã bị hủy.



- **Bổ sung Bộ đệm Chống Nuốt Click (Input Debounce Buffer)**:



  - Bổ sung kiểm tra `framesToSkip <= 0` cho các nút bấm mở Cửa hàng, Túi đồ, Kỹ năng, Gacha, Sự kiện, Trang bị trên HUD trong [PlayScreen.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/PlayScreen.java), ngăn click từ màn hình trước bị lọt xuyên vào HUD.







### 🎮 Sửa Lỗi Phím Kỹ Năng Q / W / E / R & Tự Động Gắn Kỹ Năng Cho Người Chơi Mới



- **Tự động mở khóa và trang bị kỹ năng khởi đầu**:



  - Trong [Player.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/entities/Player.java) và [SaveManager.java](file:///c:/Users/Quan/IdeaProjects/AnglerRPG/core/src/main/java/com/fishingrpg/game/data/SaveManager.java), hệ thống đảm bảo kỹ năng chủ động cơ bản [Kéo Mạnh] (`keo_manh`) luôn đạt ít nhất Cấp 1 và được tự động gán vào ô phím [Q] (Slot 0) cho tài khoản mới hoặc tài khoản chưa gắn kỹ năng.



- **Bổ sung Phản Hồi Chữ Nổi (Floating Text) Trực Quan**:



  - Khi người chơi bấm phím Q, W, E, R vào ô chưa gắn kỹ năng, hiển thị thông báo chữ nổi màu vàng ngay giữa màn hình: `"Ô [Q/W/E/R] chưa gắn kỹ năng!"`.



  - Khi kỹ năng đang trong thời gian hồi chiêu, hiển thị chữ nổi thông báo thời gian còn lại (VD: `"Kéo Mạnh đang hồi (2.5s)!"`).



  - Khi người chơi không đủ Thể lực để thi triển chiêu thức, hiển thị cảnh báo đỏ nổi bật: `"Không đủ Thể lực!"`.







### 🚪 Chuẩn Hóa Hệ Thống Điều Hướng 3 Cấp Độ (Zero-Loop Hierarchy)



- **Phân định rõ ràng chức năng nút quay lại theo trạng thái**:



  - **Khi ở Màn hình chơi (Đang câu / Giằng co / Chuẩn bị quăng)**: Nút góc trên bên trái hiển thị nhãn `← BẢN ĐỒ`. Nhấn nút này (hoặc phím `ESC`) sẽ lưu game và quay trở lại **Màn Hình Chọn Bản Đồ (`MapScreen`)** để đổi bãi câu.



  - **Khi ở Màn hình chọn bản đồ (`MapScreen`)**: Nhấn nút `← QUAY LẠI` (hoặc phím `ESC`) sẽ quay trở về **Game HUD / Trung tâm Hub (`PlayScreen` ở `State.IDLE`)**.



  - **Khi ở Game HUD (`PlayScreen` ở `State.IDLE`)**: Nút góc trên bên trái hiển thị nhãn `← MENU`. Nhấn nút này (hoặc phím `ESC`) sẽ lưu game và quay trở về **Menu Chính (`MainMenuScreen`)**.



  - Xóa bỏ triệt để 100% tình trạng văng nhầm màn hình hoặc bị kẹt trong vòng lặp điều hướng.







### 📢 Bản Tin Thông Báo Cập Nhật & Nút Thông Tin [ⓘ] Góc Phải Game HUD



- **Bỏ nút Bản Tin tại Menu chính**: Menu chính tối giản với 5 lựa chọn cốt lõi (Bắt đầu câu, Xuất, Nạp, Reset, Thoát).



- **Thiết kế nút Thông tin [ⓘ] hình vuông tại góc trên bên phải Game HUD**:



  - Chuyển sang góc trên bên phải (`X = 1220f, Y = 660f`), thiết kế hình vuông 40x40 pixel chuẩn icon với logo chữ `ⓘ` (Information) tinh tế, bỏ nhãn chữ "BẢN TIN" để giao diện thoáng đãng.



  - **Cơ chế chấm thông báo thông minh (Unread Dot Indicator)**: Chỉ hiện chấm đỏ phát sáng khi có bản cập nhật mới chưa đọc. Khi người chơi nhấn vào xem, chấm đỏ sẽ tự động biến mất hoàn toàn.



  - **Chỉ xuất hiện trong Game HUD (`State.IDLE`)**: Tự động ẩn đi khi người chơi bước vào màn hình câu cá thực sự.



- **Đồng bộ màu sắc các mục trong bản tin**:



  - Toàn bộ các dòng `[SỬA LỖI]` được quy chuẩn về cùng một mã màu xanh ngọc đồng nhất (`Color(0.25f, 0.85f, 0.50f, 1f)`), giúp bố cục bảng tin hài hòa và chuyên nghiệp.



- **Khắc phục triệt để lỗi khung nội dung bị trắng**: Tích hợp camera chuẩn và Vertical Bounds Culling hiển thị 100% nội dung chữ sắc nét.







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



