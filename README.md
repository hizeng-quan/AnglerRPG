# AnglerRPG (Fishing RPG)

AnglerRPG là một tựa game nhập vai câu cá (Fishing RPG) 2D được xây dựng trên nền tảng [libGDX](https://libgdx.com/). Khác với các tựa game câu cá nhàn rỗi (idle fishing) thông thường, AnglerRPG mang đến hệ thống chiến đấu kịch tính với cá, kết hợp cùng các yếu tố nhập vai sâu sắc như cây kỹ năng, trang bị, gacha và hệ thống nhiệm vụ.

## 🌟 Tính Năng Nổi Bật

### 🎣 Cơ Chế Câu Cá Hành Động (Action Fishing)
- **Quăng cần chuẩn xác**: Căn góc và thời điểm quăng cần để đạt "Perfect Cast", giúp cá cắn câu nhanh hơn.
- **Quản lý Thể Lực & Lực Căng (Stamina & Tension)**: Người chơi phải khéo léo giữ dây và nhả dây để bào mòn thể lực của cá mà không làm đứt dây (Tension quá cao) hoặc kiệt sức.
- **Kỹ năng chủ động**: Sử dụng các kỹ năng như *Kéo Mạnh*, *Giật Kép*, *Phá Giáp*, *Lưới Đánh Cá*... để đối phó với những loài cá hung dữ.

### 🐟 Trí Tuệ Cá Đa Dạng (Fish AI & Skills)
Mỗi loài cá trong game không chỉ có kích thước và trọng lượng khác nhau mà còn sở hữu các kỹ năng đặc biệt khi sinh mệnh của chúng gặp nguy hiểm:
- **Cá Thường**: Kháng cự cơ bản.
- **Cá Rare/Epic**: Bứt tốc, lặn sâu làm tăng sốc lực căng dây, hoặc phun mực che khuất tầm nhìn.
- **Cá Boss (Leviathan, Bạch Tuộc Khổng Lồ,...)**: Sử dụng kỹ năng tàn phá như *Cuồng Nộ*, *Sóng Thần*, *Bão Tố*, *Giật Điện* đòi hỏi thao tác tay và chiến thuật hợp lý để khuất phục.

### ⚔️ Yếu Tố Nhập Vai (RPG Elements)
- **Cấp độ & Chỉ số**: Tích lũy kinh nghiệm (XP) sau mỗi lần câu thành công, lên cấp và nhận điểm kỹ năng (Skill Points).
- **Cây Kỹ Năng (Skill Tree)**: Nâng cấp đa dạng các kỹ năng bị động (tăng tỉ lệ câu cá hiếm, giảm độ trượt của cá) và chủ động.
- **Trang Bị (Equipment)**: Thu thập và trang bị Cần Câu, Dây Câu, Lưỡi Câu. Có hệ thống dòng chỉ số phụ (Substats) và Hiệu ứng Bộ (Set Bonus) độc quyền.
- **Gacha**: Sử dụng Vé Gacha hoặc Kim cương để quay ra các trang bị Huyền Thoại.
- **Bách Khoa Toàn Thư (Bestiary)**: Bộ sưu tập lưu giữ kỷ lục về kích thước lớn nhất của từng loại cá bắt được.

### 🎵 Trải Nghiệm Nghe Nhìn Chân Thực (Audio & VFX)
- Nhạc nền đa dạng theo khu vực (Ao Làng, Bãi Biển, Đảo Xa).
- Chữ nổi (Floating Text) báo sát thương và hiệu ứng.
- Rung lắc màn hình (Screen Shake) chân thực khi đụng độ Boss hoặc khi lực căng dây ở mức độ nguy hiểm.

## 💻 Cấu Trúc Dự Án (Platforms)

- `core`: Module chính chứa toàn bộ logic, dữ liệu và giao diện của trò chơi.
- `lwjgl3`: Nền tảng desktop chính (Windows/Mac/Linux) sử dụng thư viện LWJGL3.

## 🚀 Cách Cài Đặt & Chạy Game

Dự án sử dụng [Gradle](https://gradle.org/) để quản lý thư viện. Bạn có thể mở terminal ở thư mục gốc và chạy các lệnh sau (thay `gradlew` bằng `./gradlew` nếu dùng macOS/Linux):

- **Chạy game**: `gradlew lwjgl3:run`
- **Build file chạy (.jar)**: `gradlew lwjgl3:jar` (File jar sẽ xuất hiện ở `lwjgl3/build/libs/`)
- **Dọn dẹp thư mục build**: `gradlew clean`

*(Bỏ qua các cảnh báo unchecked do ép kiểu trong hệ thống dữ liệu, chúng không ảnh hưởng đến trải nghiệm game).*

---
*Phát triển bởi đội ngũ đam mê câu cá và lập trình game!*
