---
name: game-balance-pipeline
description: "Use this skill when analyzing, simulating, calculating stats, or balancing game economy, fishing combat, drop rates, gacha probabilities, or progression curves for AnglerRPG."
---

# Game Data & Balance Pipeline (AnglerRPG)

Pipeline toán học và mô phỏng số liệu chuyên dụng cho AnglerRPG, tích hợp các công cụ mã nguồn mở:
- **`SimPy` & `NumPy`:** Mô phỏng sự kiện thời gian thực (Monte Carlo Combat Tension) đo đạc Time-To-Catch (TTK) và tỷ lệ đứt dây câu.
- **`Mesa` & `Pandas`:** Mô hình kinh tế dựa trên tác tử (Agent-Based Modeling) mô phỏng dòng tiền, độ lạm phát và tiến trình 30-90 ngày của người chơi.
- **`SciPy` & `SymPy`:** Tối ưu hóa đường cong kinh nghiệm, máu cá và sát thương kéo thông qua hồi quy phi tuyến tính (Curve Fitting & Numerical Optimization).

---

## 🚀 1. Các Lệnh Thực Thi Pipeline (CLI Commands)

### 1. Mô phỏng Kéo Cá & Đối Kháng (Combat & Tension Simulator):
```bash
# Mô phỏng Cá Chép vs Đồ Tập Sự (500 trận):
python scripts/balance/balance_cli.py combat --fish "Cá Chép" --set "Tập Sự" --rarity "COMMON"

# Mô phỏng Boss Bạch Tuộc vs Đồ Lạc Hồng Thần Thoại:
python scripts/balance/balance_cli.py combat --fish "Bạch Tuộc Khổng Lồ" --set "Thần Khí Lạc Hồng" --rarity "LEGENDARY"
```

### 2. Mô phỏng Vòng Quay Gacha & Bảo Hiểm Pity:
```bash
# Mô phỏng Banner Sự Kiện (20 KC/lượt, 10,000 lượt):
python scripts/balance/balance_cli.py gacha --banner event --sims 10000 --target 3

# Mô phỏng Banner Tiêu Chuẩn (10 KC/lượt):
python scripts/balance/balance_cli.py gacha --banner standard --sims 10000 --target 3
```

### 3. Mô phỏng Kinh Tế Ảo (Mesa Multi-Agent Lifecycle):
```bash
# Mô phỏng 50 người chơi trong 30 ngày (Casual, Grinder, Hardcore):
python scripts/balance/balance_cli.py economy --days 30 --agents 50
```

### 4. Giải Toán & Tối Ưu Hóa Đường Cong (SciPy & SymPy):
```bash
# Tự động tính toán máu cá chuẩn cho mọi bậc để đạt TTK Boss = 65s:
python scripts/balance/balance_cli.py solve --target-boss 65
```

---

## 🎯 2. Tiêu Chuẩn Nghiệm Thu Cân Bằng (Balance Targets)

- **Vùng Trải Nghiệm Vàng (Combat TTK Sweet Spot):**
  * Cá Thường (Common): 8 - 14 giây (Win rate >= 98%)
  * Cá Hiếm (Uncommon/Rare): 18 - 28 giây (Win rate >= 85%)
  * Cá Sử Thi (Epic): 30 - 45 giây (Win rate >= 70%)
  * Cá Thủy Quái / Boss: 50 - 80 giây (Win rate >= 55% với đồ cùng bậc)
- **Tỷ Lệ Đứt Dây:** Không vượt quá 15% ở cấp độ phù hợp.
- **Dòng Tiền:** Tỷ lệ lạm phát Kim Cương qua nhiệm vụ ngày không vượt quá 25% chi phí 1 lượt quay gacha (tối đa 40 KC/ngày cho người chơi cày cuốc).
