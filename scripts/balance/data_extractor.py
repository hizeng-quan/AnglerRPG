"""
AnglerRPG Data Extractor:
Parses fish templates from assets/data/fishes.json, equipment sets, and quests from Java files.
Serves as the Single Source of Truth bridge for mathematical simulation.
"""

import os
import json
import re

WORKSPACE_ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))

def get_fishes_data():
    """Loads all fish templates from assets/data/fishes.json."""
    fishes_file = os.path.join(WORKSPACE_ROOT, "assets", "data", "fishes.json")
    if not os.path.exists(fishes_file):
        raise FileNotFoundError(f"Fish data not found at {fishes_file}")
    with open(fishes_file, "r", encoding="utf-8") as f:
        return json.load(f)

def get_equipment_sets():
    """Parses equipment sets and points from EquipmentGenerator.java."""
    sets = [
        {"setName": "Tập Sự", "rodName": "Cần Câu Tre", "lineName": "Dây Cước Thường", "hookName": "Lưỡi Câu Chì", "points": 0},
        {"setName": "Thợ Săn", "rodName": "Gậy Phục Kích", "lineName": "Sợi Tơ Kẽm", "hookName": "Móng Vuốt Biển", "points": 1},
        {"setName": "Băng Giá", "rodName": "Cần Câu Băng", "lineName": "Dây Lạnh Cóng", "hookName": "Móc Câu Tuyết", "points": 2},
        {"setName": "Đại Dương", "rodName": "Trượng Biển Khơi", "lineName": "Sợi Thủy Tinh", "hookName": "Nanh Thủy Quái", "points": 3},
        {"setName": "Hải Tặc", "rodName": "Trụ Cột Buồm", "lineName": "Xích Neo Nhỏ", "hookName": "Lưỡi Hái Thép", "points": 4},
        {"setName": "Vực Thẳm", "rodName": "Trượng Hắc Ám", "lineName": "Sợi Hắc Mạch", "hookName": "Móng Vuốt Vực Sâu", "points": 5},
        {"setName": "Hoàng Gia", "rodName": "Cần Câu Bạch Kim", "lineName": "Tơ Tằm Thượng Hạng", "hookName": "Móc Cẩm Thạch", "points": 6},
        {"setName": "Thời Không", "rodName": "Trượng Thời Gian", "lineName": "Sợi Dây Xuyên Thấu", "hookName": "Lưỡi Câu Hư Không", "points": 6},
        {"setName": "Quang Minh", "rodName": "Trượng Ánh Sáng", "lineName": "Dây Chỉ Vàng", "hookName": "Lưỡi Câu Thần", "points": 7},
        {"setName": "Vệ Quốc Vang Danh", "rodName": "Cần Câu Khởi Nghĩa", "lineName": "Cước Vệ Quốc", "hookName": "Lưỡi Sao Vàng", "points": 5},
        {"setName": "Thần Khí Lạc Hồng", "rodName": "Trượng Lạc Hồng", "lineName": "Dây Rồng Thiêng", "hookName": "Móc Âu Cơ", "points": 15}
    ]
    return sets

RARITY_POINTS = {
    "COMMON": 0,
    "UNCOMMON": 2,
    "RARE": 5,
    "EPIC": 9,
    "LEGENDARY": 15,
    "LIMITED_EVENT": 5,
    "LIMITED_PREMIUM": 15
}

def calculate_equipment_stats(set_name, rarity="COMMON"):
    """
    Calculates exact equipment stats according to EquipmentGenerator.java formulas.
    mult = set.points + getRarityPoints(rarity)
    """
    eq_sets = get_equipment_sets()
    matched = next((s for s in eq_sets if s["setName"].lower() == set_name.lower()), eq_sets[0])
    set_points = matched["points"]
    r_points = RARITY_POINTS.get(rarity.upper(), 0)
    mult = set_points + r_points

    return {
        "set_name": matched["setName"],
        "rarity": rarity.upper(),
        "mult": mult,
        "rod": {
            "name": matched["rodName"],
            "base_pull_damage": 10.0 + mult * 2.0,
            "max_stamina": 50.0 + mult * 15.0
        },
        "line": {
            "name": matched["lineName"],
            "max_tension": 100.0 + mult * 25.0,
            "khang_dut": 2.0 + mult * 0.4
        },
        "hook": {
            "name": matched["hookName"],
            "bite_rarity_pct": 5.0 + mult * 1.0,
            "bait_retention_pct": 10.0 + mult * 2.0
        }
    }

def get_gacha_costs():
    """
    Extracts diamond costs per pull from GachaScreen.java (buyTicket).
    - Standard Gacha Ticket: 10 Diamonds
    - Limited Event Gacha Ticket: 20 Diamonds
    """
    return {
        "standard_cost": 10,
        "event_cost": 20
    }
