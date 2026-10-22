# AnglerRPG Balance & Data Modeling Guidelines

Bộ quy chuẩn toán học và nguyên tắc cân bằng dành cho dự án AnglerRPG.

---

## 1. Triết lý Thiết kế Cân bằng (Design Philosophy)

1. **Combat Tension là Trọng Tâm:** Trò chơi là câu cá RPG đối kháng, không phải clicker tự động. Mọi trận câu cá từ bậc RARE trở lên phải có sự giằng co, buộc người chơi phải quản lý nhịp thở (nhấp nhả phím Space) và thời điểm kích hoạt kỹ năng.
2. **Ngăn Chặn Lạm Phát Siêu Cấp:** Kinh tế game cần có sự cân xứng giữa Nguồn Thu (Sources/Inflow) và Bể Tiêu Thụ (Sinks/Outflow). Không để Kim Cương bị tích tụ vô tận từ nhiệm vụ ngày.
3. **Minh Bạch Xác Suất Gacha:** Mọi vòng quay phải có cơ chế bảo hiểm (Pity) và cận trên chi phí (Worst-case Cost Cap).

---

## 2. Tiêu chuẩn Chỉ số & TTK (Combat Standards)

$$\text{TTK} = \frac{\text{Máu Cá}}{\text{Sát Thương Kéo Hiệu Dụng}} \times \frac{1}{\text{Tỷ Lệ Nhấp Kéo}}$$

| Phẩm Cấp Cá | Target TTK (Giây) | Tỷ Lệ Đứt Dây Mục Tiêu | Đồ Đề Xuất Cùng Cấp |
| :--- | :---: | :---: | :--- |
| **Common (Thường)** | 8s - 14s | < 2% | Set Tập Sự (COMMON) |
| **Uncommon (Hiếm)** | 15s - 22s | 3% - 6% | Set Thợ Săn / Băng Giá (UNCOMMON) |
| **Rare (Đặc Biệt)** | 22s - 30s | 5% - 10% | Set Băng Giá / Đại Dương (RARE) |
| **Epic (Sử Thi)** | 35s - 48s | 8% - 15% | Set Hải Tặc / Vực Thẳm (EPIC) |
| **Boss (Thủy Quái)** | 55s - 80s | 12% - 20% | Set Hoàng Gia / Thời Không / Lạc Hồng |

---

## 3. Cân bằng Dòng Tiền (Economy Sinks & Sources)

- **Kim Cương (Diamonds / KC):**
  - *Nguồn (Inflow):* Nhiệm vụ ngày (15-25 KC/ngày), Nhiệm vụ tuần (30-40 KC/tuần), Thành tựu 1 lần (5-30 KC), Cá hiếm/Boss (1-5 KC).
  - *Tiêu thụ (Sink):* Quay Gacha Tiêu Chuẩn (10 KC/vé), Quay Gacha Sự Kiện (20 KC/vé), Mua mồi đặc biệt, Mở rộng ô chứa đồ.
- **Vàng (Gold):**
  - *Nguồn:* Bán cá, Nhiệm vụ ngày, Rương kho báu.
  - *Tiêu thụ:* Nâng cấp cần câu, Mua vật phẩm tiêu hao (Đá mài, Thuốc lắc, Nam châm), Nâng cấp cây kỹ năng.

---

## 4. Quy trình Nghiệm thu Số liệu trước khi Merge Code

Trước khi điều chỉnh bất kỳ file thông số nào (`fishes.json`, `EquipmentGenerator.java`, `QuestManager.java`):
1. Chạy `python scripts/balance/balance_cli.py combat` kiểm tra Win Rate và TTK.
2. Chạy `python scripts/balance/balance_cli.py gacha` xác nhận số vé trung bình và P99.
3. Chạy `python scripts/balance/balance_cli.py economy` xác nhận độ lạm phát sau 30 ngày.
4. Chỉ khi toàn bộ các chỉ số nằm trong ngưỡng quy định mới được đưa vào bản build chính thức.
