import sys
import random
import mesa
import pandas as pd
import numpy as np

if sys.stdout.encoding != 'utf-8':
    try:
        sys.stdout.reconfigure(encoding='utf-8')
    except Exception:
        pass


class PlayerAgent(mesa.Agent):
    def __init__(self, model, archetype="casual", mode="baseline"):
        super().__init__(model)
        self.archetype = archetype
        self.mode = mode # "baseline" or "balanced"
        self.gold = 50
        self.diamonds = 0
        self.level = 1
        self.xp = 0
        self.total_catches = 0
        self.gear_points = 0
        self.gacha_pulls_done = 0
        self.skill_books_bought = 0
        self.inventory_upgrades = 0
        self.day_count = 0

    def step(self):
        self.day_count += 1

        # 1. Daily Catches based on archetype
        if self.archetype == "casual":
            catches = random.randint(8, 14)
            daily_quests = random.randint(2, 3)
            rare_catches = random.randint(0, 1)
        elif self.archetype == "grinder":
            catches = random.randint(30, 50)
            daily_quests = 4
            rare_catches = random.randint(2, 4)
        else: # hardcore
            catches = random.randint(70, 110)
            daily_quests = 5
            rare_catches = random.randint(5, 9)

        self.total_catches += catches

        # 2. Gold from selling catches
        # Map tier increases as level progresses (Lv 1-5: Map 1, Lv 6-15: Map 2, Lv 16+: Map 3)
        if self.level <= 5:
            base_fish_val = 5.0
            boss_chance = 0.05
            boss_val = 500.0
        elif self.level <= 15:
            base_fish_val = 22.0
            boss_chance = 0.10
            boss_val = 1500.0
        else:
            base_fish_val = 75.0
            boss_chance = 0.20
            boss_val = 4000.0

        earned_gold = catches * base_fish_val
        # Boss encounters
        if random.random() < boss_chance:
            earned_gold += boss_val
            # If using Net / Lưới Đánh Cá
            if self.mode == "baseline":
                # Uncapped: extra 1.5x boss value
                earned_gold += boss_val * 1.5
            else:
                # Balanced: capped extra bonus to max 400V
                earned_gold += min(boss_val * 1.5, 400.0)

        self.gold += earned_gold
        self.gold += daily_quests * 50

        # 3. Rich Diamond Inflow (Honeymoon vs Recurring)
        # A. Honeymoon Inflow (Day 1 - 7): Catalog discoveries + Early Achievements + Event Login
        if self.day_count <= 7:
            honeymoon_kc = 0
            # Catalog new fish bonus
            honeymoon_kc += random.randint(15, 30)
            # Early achievements
            honeymoon_kc += random.randint(20, 45)
            # Event login gift
            honeymoon_kc += 15
            self.diamonds += honeymoon_kc

        # B. Recurring Inflow:
        if self.mode == "baseline":
            # Rare fish drop (100% 1-3 KC)
            self.diamonds += rare_catches * random.randint(1, 3)
            # Daily quests (20 KC chest + event)
            self.diamonds += daily_quests * 6 + 10
            # Weekly quest prorated (avg 14 KC / day)
            self.diamonds += 14
        else:
            # Balanced: 20% rare chance, capped chests
            self.diamonds += int(rare_catches * 0.25)
            # Daily quests (10 KC chest + event)
            self.diamonds += min(daily_quests * 3, 10) + 5
            # Weekly quest prorated (avg 6 KC / day)
            self.diamonds += 6

        # 4. Shop Spending (Gold Sinks)
        # Sách Kỹ Năng:
        if self.mode == "baseline":
            # Fixed price 1500V per book
            book_cost = 1500
            while self.gold >= book_cost and self.skill_books_bought < 10:
                self.gold -= book_cost
                self.skill_books_bought += 1
        else:
            # Balanced: Progressive pricing (500 -> 1200 -> 2500 -> 5000)
            cost_curve = [500, 1200, 2500, 5000, 8000, 12000, 16000, 20000, 25000, 30000]
            current_idx = min(self.skill_books_bought, len(cost_curve) - 1)
            book_cost = cost_curve[current_idx]
            if self.gold >= book_cost and self.skill_books_bought < 10:
                self.gold -= book_cost
                self.skill_books_bought += 1

        # Consumable items (Bánh mì, mồi, đá mài)
        consumable_budget = int(self.gold * 0.15)
        self.gold -= consumable_budget

        # 5. Diamond Sinks
        if self.mode == "balanced":
            # Secondary sinks before gacha:
            # Expand inventory (100 KC per upgrade, up to 3 times)
            if self.diamonds >= 100 and self.inventory_upgrades < 3:
                self.diamonds -= 100
                self.inventory_upgrades += 1
            # Special dungeon / map license (30 KC every 5 days)
            if self.day_count % 5 == 0 and self.diamonds >= 30:
                self.diamonds -= 30

        # Gacha pulls: Standard 10 KC, Event 20 KC
        pull_cost = 10 if self.archetype == "casual" else 20
        while self.diamonds >= pull_cost:
            self.diamonds -= pull_cost
            self.gacha_pulls_done += 1
            if random.random() < 0.20:
                self.gear_points += 2

        # 6. Level progression
        self.xp += catches * 15
        while self.xp >= self.level * 100:
            self.xp -= self.level * 100
            self.level += 1


class AnglerEconomyModel(mesa.Model):
    def __init__(self, n_agents=100, casual_ratio=0.6, grinder_ratio=0.3, hardcore_ratio=0.1, mode="baseline"):
        super().__init__()
        self.num_agents = n_agents
        self.mode = mode

        for _ in range(int(n_agents * casual_ratio)):
            PlayerAgent(self, archetype="casual", mode=mode)

        for _ in range(int(n_agents * grinder_ratio)):
            PlayerAgent(self, archetype="grinder", mode=mode)

        for _ in range(int(n_agents * hardcore_ratio)):
            PlayerAgent(self, archetype="hardcore", mode=mode)

    def step(self):
        for agent in list(self.agents):
            agent.step()

    def run_simulation(self, days=30):
        records = []
        for day in range(1, days + 1):
            self.step()

            for arch in ["casual", "grinder", "hardcore"]:
                agents = [a for a in self.agents if a.archetype == arch]
                if not agents:
                    continue
                records.append({
                    "day": day,
                    "archetype": arch,
                    "mode": self.mode,
                    "mean_gold": round(float(np.mean([a.gold for a in agents])), 1),
                    "mean_diamonds": round(float(np.mean([a.diamonds for a in agents])), 1),
                    "mean_gacha_pulls": round(float(np.mean([a.gacha_pulls_done for a in agents])), 1),
                    "mean_skill_books": round(float(np.mean([a.skill_books_bought for a in agents])), 1),
                    "mean_level": round(float(np.mean([a.level for a in agents])), 1)
                })

        return pd.DataFrame(records)


def run_comparative_simulation(days=30, agents_per_model=100):
    print(f"\n🔬 === CHẠY THỬ NGHIỆM ĐỐI CHỨNG MÔ HÌNH KINH TẾ (30 NGÀY, {agents_per_model} NGƯỜI CHƠI ẢO) ===")
    
    # 1. Baseline Model
    model_a = AnglerEconomyModel(n_agents=agents_per_model, mode="baseline")
    df_a = model_a.run_simulation(days=days)
    day30_a = df_a[df_a["day"] == days]

    # 2. Balanced Model
    model_b = AnglerEconomyModel(n_agents=agents_per_model, mode="balanced")
    df_b = model_b.run_simulation(days=days)
    day30_b = df_b[df_b["day"] == days]

    print("\n" + "="*80)
    print("📌 KỊCH BẢN A: HIỆN TẠI (GIÁ SHOP CỐ ĐỊNH + KHÔNG CÓ TRẦN LỢI NHUẬN + CHƯA CÓ BỂ HÚT KC)")
    print("="*80)
    for _, row in day30_a.iterrows():
        print(f"* [{row['archetype'].upper()}]:")
        print(f"  - Cấp độ sau 30 ngày: Lv.{row['mean_level']}")
        print(f"  - Số dư Vàng tích lũy: {row['mean_gold']:,} Gold")
        print(f"  - Tổng lượt quay Gacha: {row['mean_gacha_pulls']} lượt")
        print(f"  - Sách kỹ năng đã mua (max 10): {row['mean_skill_books']} / 10 cuốn")

    print("\n" + "="*80)
    print("✨ KỊCH BẢN B: ĐỀ XUẤT CÂN BẰNG (PHÂN CẤP SHOP + ĐẶT TRẦN THƯỞNG + SÁCH LŨY TIẾN + BỂ HÚT KC)")
    print("="*80)
    for _, row in day30_b.iterrows():
        print(f"* [{row['archetype'].upper()}]:")
        print(f"  - Cấp độ sau 30 ngày: Lv.{row['mean_level']}")
        print(f"  - Số dư Vàng tích lũy: {row['mean_gold']:,} Gold (Kiểm soát lạm phát)")
        print(f"  - Tổng lượt quay Gacha: {row['mean_gacha_pulls']} lượt")
        print(f"  - Sách kỹ năng đã mua (max 10): {row['mean_skill_books']} / 10 cuốn (Tiến trình giãn đều)")


if __name__ == "__main__":
    run_comparative_simulation(days=30, agents_per_model=100)
