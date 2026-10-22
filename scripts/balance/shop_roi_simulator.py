"""
AnglerRPG Shop ROI & Item Power Balance Simulator:
Calculates mathematical Return on Investment (ROI), cost-effectiveness,
and profit margins of every item in ItemDatabase across different Maps.
"""

import sys
import os
import pandas as pd
import numpy as np

if sys.stdout.encoding != 'utf-8':
    try:
        sys.stdout.reconfigure(encoding='utf-8')
    except Exception:
        pass

# Map statistics based on exact fishes.json and FishDatabase.java weights
MAPS = {
    "Ao Làng (Map 1)": {
        "avg_fish_gold": 30.7,
        "common_fish_gold": 3.8,
        "boss_gold": 500.0,
        "avg_xp": 27.4,
        "required_level": 1
    },
    "Bãi Biển (Map 2)": {
        "avg_fish_gold": 67.4,
        "common_fish_gold": 6.0,
        "boss_gold": 1500.0,
        "avg_xp": 51.9,
        "required_level": 10
    },
    "Đảo Xa (Map 3)": {
        "avg_fish_gold": 189.6,
        "common_fish_gold": 21.7,
        "boss_gold": 4000.0,
        "avg_xp": 127.6,
        "required_level": 20
    }
}

ITEMS = [
    {
        "name": "Bánh Mì",
        "cost": 50,
        "category": "Hồi Phục",
        "effect": "Hồi 25 Thể lực",
        "power_rating": 3.0
    },
    {
        "name": "Mồi Thơm",
        "cost": 100,
        "category": "Mồi Câu",
        "effect": "Tăng 50% tỷ lệ cá hiếm",
        "power_rating": 4.0
    },
    {
        "name": "Lưới Đánh Cá",
        "cost": 300,
        "category": "Kinh Tế",
        "effect": "Nhân 2 đến 4 lượng cá bắt được (trung bình x2.5)",
        "power_rating": 8.5
    },
    {
        "name": "Bùa Hoàng Kim",
        "cost": 500,
        "category": "Kinh Tế",
        "effect": "+50% Vàng nhận được trong 5 lượt",
        "power_rating": 8.0
    },
    {
        "name": "Đá Mài Lưỡi Câu",
        "cost": 750,
        "category": "Chiến Đấu",
        "effect": "+25% sát thương kéo cá & cắn câu nhanh 40% (5 lượt)",
        "power_rating": 7.5
    },
    {
        "name": "Bình Xịt Đóng Băng",
        "cost": 500,
        "category": "Chiến Đấu",
        "effect": "Đóng băng cá 2s ngừng giằng",
        "power_rating": 6.5
    },
    {
        "name": "Sách Kỹ Năng",
        "cost": 1500,
        "category": "Bí Kíp",
        "effect": "+1 Cấp Kỹ Năng vĩnh viễn",
        "power_rating": 9.5
    }
]


def analyze_shop_roi():
    results = []
    for map_name, m in MAPS.items():
        avg_gold = m["avg_fish_gold"]
        boss_gold = m["boss_gold"]

        # 1. Lưới Đánh Cá (cost: 300, extra catch: x2.5 -> +1.5 extra fish)
        # Normal fish:
        extra_gold_net_normal = 1.5 * avg_gold
        net_roi_normal = round(((extra_gold_net_normal - 300) / 300) * 100, 1)
        # Boss fish:
        extra_gold_net_boss = 1.5 * boss_gold
        net_roi_boss = round(((extra_gold_net_boss - 300) / 300) * 100, 1)

        # 2. Bùa Hoàng Kim (cost: 500, +50% Gold for 5 catches)
        # 5 normal catches:
        gold_bua_normal = 5 * avg_gold * 0.50
        bua_roi_normal = round(((gold_bua_normal - 500) / 500) * 100, 1)
        # 4 normal + 1 boss catch:
        gold_bua_boss = (4 * avg_gold + 1 * boss_gold) * 0.50
        bua_roi_boss = round(((gold_bua_boss - 500) / 500) * 100, 1)

        # 3. Sách Kỹ Năng (cost: 1500)
        # How many normal catches to buy 1 book?
        catches_for_book = int(1500 / avg_gold)
        # How many boss catches to buy 1 book?
        bosses_for_book = round(1500 / boss_gold, 2)

        results.append({
            "map": map_name,
            "avg_fish_gold": avg_gold,
            "net_cost": 300,
            "net_roi_normal_pct": net_roi_normal,
            "net_roi_boss_pct": net_roi_boss,
            "bua_cost": 500,
            "bua_roi_normal_pct": bua_roi_normal,
            "bua_roi_boss_pct": bua_roi_boss,
            "book_cost": 1500,
            "catches_for_skill_book": catches_for_book,
            "bosses_for_skill_book": bosses_for_book
        })

    return results


def run_roi_cli():
    res = analyze_shop_roi()
    boss_names = {
        "Ao Làng (Map 1)": "Thủy Quái Sông Trà (500V)",
        "Bãi Biển (Map 2)": "Bạch Tuộc Khổng Lồ (1,500V)",
        "Đảo Xa (Map 3)": "Thần Biển Leviathan (4,000V)"
    }
    print("\n🏪 === PHÂN TÍCH HIỆU QUẢ HOÀN VỐN (ROI) VẬT PHẨM SHOP QUA 3 MAP ===")
    for r in res:
        boss_label = boss_names.get(r['map'], 'Boss')
        print(f"\n📍 BẢN ĐỒ: {r['map']} (Giá cá TB: {r['avg_fish_gold']} Vàng | Boss: {boss_label})")
        print(f"  • Lưới Đánh Cá (300V):")
        print(f"    - Dùng cho cá thường: Thu lời {r['net_roi_normal_pct']}% (Lỗ {abs(r['net_roi_normal_pct'])}% nếu dùng ở đây!)")
        print(f"    - Bắt trúng Boss {boss_label}: Thu lời +{r['net_roi_boss_pct']}% (Siêu lời gấp {round((r['net_roi_boss_pct']+100)/100, 1)} lần!)")
        print(f"  • Bùa Hoàng Kim (500V, 5 lượt):")
        print(f"    - 5 cá thường:   ROI: {r['bua_roi_normal_pct']}%")
        print(f"    - 1 Boss + 4 cá: ROI: +{r['bua_roi_boss_pct']}%")
        print(f"  • Sách Kỹ Năng (1,500V):")
        print(f"    - Cần câu: {r['catches_for_skill_book']} con cá thường HOẶC chỉ {r['bosses_for_skill_book']} con Boss {boss_label}!")


if __name__ == "__main__":
    run_roi_cli()
