"""
AnglerRPG Fishing Combat Simulator:
Monte Carlo simulation of the real-time fishing tension mechanic in FishingManager.java.
Evaluates Time-To-Catch (TTK), Win Rate, Line Snap Rate, and Tension Dynamics.
"""

import math
import random
import numpy as np
from data_extractor import get_fishes_data, calculate_equipment_stats

class CombatSimulator:

    @staticmethod
    def simulate_single_encounter(fish_data, eq_stats, player_strategy="optimal", dt=0.05, max_time=180.0):
        """
        Simulates 1 fishing encounter tick-by-tick (dt seconds).
        Returns: { 'success': bool, 'duration': float, 'reason': str, 'final_hp': float, 'max_tension_reached': float }
        """
        fish_max_hp = float(fish_data.get("maxHp", 50.0))
        fish_hp = fish_max_hp
        fish_pull_base = float(fish_data.get("pullStrength", 10.0))
        skills = [s.get("id") if isinstance(s, dict) else s for s in fish_data.get("skills", [])]

        rod = eq_stats["rod"]
        line = eq_stats["line"]

        max_tension = line["max_tension"]
        if "ap_luc_sau" in skills:
            max_tension *= 0.75

        khang_dut = line["khang_dut"]
        pull_reduction = max(0.25, 1.0 - (khang_dut * 1.5) / 100.0)

        base_dmg = 20.0 + rod["base_pull_damage"] # Player base 20 + Rod bonus
        max_stamina = 100.0 + rod["max_stamina"]   # Player base 100 + Rod bonus
        current_stamina = max_stamina
        current_tension = 0.0
        max_tension_reached = 0.0

        time = 0.0
        fish_state = "IDLE" # IDLE or PULLING
        fish_timer = random.uniform(1.2, 2.5)

        # Damage reduction passives
        dmg_mult = 1.0
        if "tron_truot" in skills: dmg_mult *= 0.80
        if "ben_bi" in skills:     dmg_mult *= 0.85
        if "vo_cung" in skills:    dmg_mult *= 0.80

        while time < max_time and fish_hp > 0:
            time += dt
            fish_timer -= dt
            if fish_timer <= 0:
                fish_state = "PULLING" if fish_state == "IDLE" else "IDLE"
                fish_timer = random.uniform(1.5, 3.5) if fish_state == "PULLING" else random.uniform(1.5, 3.5)

            # Player decision based on strategy
            is_space_down = False
            if player_strategy == "optimal":
                # Reel during IDLE as long as tension has safety margin (<85%)
                # Only reel during PULLING if tension is very low (<25%)
                is_space_down = (fish_state == "IDLE" and current_tension < max_tension * 0.85) or (fish_state == "PULLING" and current_tension < max_tension * 0.25)
            elif player_strategy == "aggressive":
                # Spam reel almost constantly up to 90% tension
                is_space_down = current_tension < max_tension * 0.90
            elif player_strategy == "casual":
                # Casual player: reels during IDLE, occasionally panic reels during PULLING
                if fish_state == "IDLE":
                    is_space_down = current_tension < max_tension * 0.80
                else:
                    is_space_down = random.random() < 0.20 and current_tension < max_tension * 0.50

            # Tension & Damage calculations
            tension_increase = 0.0

            # Fish pull: 1.5x / 0.8x in PULLING, 0.15x / 0.10x in IDLE
            if fish_state == "PULLING":
                pull_mult = 1.5 if is_space_down else 0.8
            else:
                pull_mult = 0.03 if is_space_down else 0.0

            pull = fish_pull_base * pull_mult
            if "xuc_tu" in skills:
                pull *= (1.0 + (time / 10.0) * 0.1)
            pull *= pull_reduction
            tension_increase += pull * dt

            # Stamina drain: 2.0/s in game
            current_stamina -= 2.0 * dt
            if current_stamina <= 0:
                return {"success": False, "duration": time, "reason": "HẾT THỂ LỰC!", "final_hp": fish_hp, "max_tension_reached": max_tension_reached}

            if is_space_down:
                if fish_state == "PULLING":
                    tension_increase += (26.0 + max_tension * 0.055) * dt
                else:
                    tension_increase += (10.0 + max_tension * 0.020) * dt

                # Apply damage
                eff_dmg = base_dmg * dmg_mult
                # 15% base crit chance for 1.5x dmg
                if random.random() < 0.15:
                    eff_dmg *= 1.5
                fish_hp -= eff_dmg * dt
                current_tension += tension_increase
            else:
                # Slack: natural decay reduced heavily to 6.0 + max_tension * 0.04
                tension_dec = (6.0 + max_tension * 0.04) * dt
                if "xuc_tu" in skills:
                    tension_dec *= 0.7
                current_tension = max(0.0, current_tension - tension_dec) + tension_increase

            max_tension_reached = max(max_tension_reached, current_tension)

            # Check line break
            if current_tension >= max_tension:
                return {"success": False, "duration": time, "reason": "ĐỨT DÂY CÂU!", "final_hp": fish_hp, "max_tension_reached": max_tension_reached}

        if fish_hp <= 0:
            return {"success": True, "duration": time, "reason": "THÀNH CÔNG!", "final_hp": 0.0, "max_tension_reached": max_tension_reached}
        else:
            return {"success": False, "duration": time, "reason": "HẾT GIỜ (CÁ THOÁT)!", "final_hp": fish_hp, "max_tension_reached": max_tension_reached}

    @staticmethod
    def simulate_matchup(fish_name, set_name="Tập Sự", rarity="COMMON", iterations=500, strategy="optimal"):
        """
        Runs Monte Carlo simulation for a specific fish and equipment loadout.
        """
        fishes = get_fishes_data()
        fish = next((f for f in fishes if f["name"].lower() == fish_name.lower()), None)
        if not fish:
            raise ValueError(f"Fish '{fish_name}' not found.")

        eq_stats = calculate_equipment_stats(set_name, rarity)

        durations = []
        successes = 0
        snaps = 0
        stamina_outs = 0

        for _ in range(iterations):
            res = CombatSimulator.simulate_single_encounter(fish, eq_stats, player_strategy=strategy)
            durations.append(res["duration"])
            if res["success"]:
                successes += 1
            elif res["reason"] == "ĐỨT DÂY CÂU!":
                snaps += 1
            elif "THỂ LỰC" in res["reason"]:
                stamina_outs += 1

        durations = np.array(durations)
        succ_durations = durations[:successes] if successes > 0 else durations

        return {
            "fish_name": fish["name"],
            "fish_rarity": fish["rarity"],
            "fish_hp": fish["maxHp"],
            "set_name": set_name,
            "rarity": rarity,
            "iterations": iterations,
            "strategy": strategy,
            "win_rate_pct": round((successes / iterations) * 100.0, 1),
            "line_snap_pct": round((snaps / iterations) * 100.0, 1),
            "stamina_out_pct": round((stamina_outs / iterations) * 100.0, 1),
            "mean_ttk_sec": round(float(np.mean(succ_durations)), 2),
            "std_ttk_sec": round(float(np.std(succ_durations)), 2),
            "median_ttk_sec": round(float(np.median(succ_durations)), 2),
            "p90_ttk_sec": round(float(np.percentile(succ_durations, 90)), 2),
            "min_ttk_sec": round(float(np.min(succ_durations)), 2),
            "max_ttk_sec": round(float(np.max(succ_durations)), 2)
        }
