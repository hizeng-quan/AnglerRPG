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

        # Set Bonuses & Consumables
        set_name = eq_stats.get("set_name", "")
        is_huyet_nguyet = (set_name == "Huyết Nguyệt Thần Vương")
        is_bang_gia = (set_name == "Băng Giá")
        perfect_cast_timer = 2.0 if is_huyet_nguyet else 0.0
        candle_charges = 1 if is_huyet_nguyet else 0  # Nến Hồn Ma Bảo Hộ

        # Player skills & stats scaling
        has_abilities = (eq_stats.get("mult", 0) >= 4)  # Experienced gear uses GAS abilities
        cd_keo_manh = 0.0
        cd_giat_kep = 0.0
        cd_pha_giap = 0.0
        cd_troi_buoc = 0.0
        cd_duong_suc = 0.0
        cd_luot_day = 0.0
        pha_giap_timer = 0.0
        luot_day_timer = 0.0
        stun_timer = 0.0
        frozen_timer = 0.0

        # Substats & Passive skills (Dây Thép +20% Max Tension, Giảm Lực Kéo 15%)
        if has_abilities:
            max_tension *= 1.20  # Dây Thép Lv.2
            pull_reduction *= 0.85  # Substats FISH_PULL_REDUCE

        # Thanatos specific mechanics
        is_thanatos = ("Thanatos" in fish_data.get("name", ""))
        thanatos_rehook_cd = 12.0
        thanatos_vuc_xoay_cd = 18.0

        is_boss = (fish_data.get("rarity") == "BOSS" or fish_pull_base >= 100.0)

        while time < max_time and fish_hp > 0:
            time += dt
            fish_timer -= dt
            if perfect_cast_timer > 0:
                perfect_cast_timer -= dt
            if stun_timer > 0:
                stun_timer -= dt
            if frozen_timer > 0:
                frozen_timer -= dt
            if pha_giap_timer > 0:
                pha_giap_timer -= dt
            if luot_day_timer > 0:
                luot_day_timer -= dt
            if cd_keo_manh > 0:
                cd_keo_manh -= dt
            if cd_giat_kep > 0:
                cd_giat_kep -= dt
            if cd_pha_giap > 0:
                cd_pha_giap -= dt
            if cd_troi_buoc > 0:
                cd_troi_buoc -= dt
            if cd_duong_suc > 0:
                cd_duong_suc -= dt
            if cd_luot_day > 0:
                cd_luot_day -= dt

            # Thanatos Re-Hook Event (Liên Hoàn Trảm)
            if is_thanatos:
                thanatos_rehook_cd -= dt
                if thanatos_rehook_cd <= 0:
                    thanatos_rehook_cd = random.uniform(12.0, 16.0)
                    roll = random.random()
                    if roll < 0.68:
                        # 2/2 Perfect Re-Hook: Thanatos stunned 3.5s and takes x2 dmg
                        stun_timer = 3.5
                        fish_hp -= base_dmg * 2.5
                    elif roll < 0.85:
                        # 1/2 Miss: -20 Stamina, moderate tension
                        current_stamina = max(0.0, current_stamina - 20.0)
                        current_tension += 20.0
                    else:
                        # 0/2 Full Miss: +40 Tension, Boss heals 5% HP
                        current_tension += 40.0
                        fish_hp = min(fish_max_hp, fish_hp + fish_max_hp * 0.05)

                thanatos_vuc_xoay_cd -= dt
                if thanatos_vuc_xoay_cd <= 0:
                    thanatos_vuc_xoay_cd = random.uniform(18.0, 24.0)
                    fish_hp = min(fish_max_hp, fish_hp + fish_max_hp * 0.03)
                    current_tension += 25.0

            if fish_timer <= 0:
                fish_state = "PULLING" if fish_state == "IDLE" else "IDLE"
                fish_timer = random.uniform(1.5, 3.5) if fish_state == "PULLING" else random.uniform(1.5, 3.5)

            # GAS Abilities usage by player
            if has_abilities and player_strategy == "optimal":
                # Trói buộc: freeze fish when it pulls or tension is getting high
                if cd_troi_buoc <= 0 and current_stamina >= 25.0 and (fish_state == "PULLING" or current_tension > max_tension * 0.50):
                    cd_troi_buoc = 16.0
                    frozen_timer = 3.0
                    current_stamina -= 25.0

                # Lướt dây: soften pull when pulling
                if cd_luot_day <= 0 and current_stamina >= 15.0 and fish_state == "PULLING" and frozen_timer <= 0:
                    cd_luot_day = 12.0
                    luot_day_timer = 3.5
                    current_stamina -= 15.0

                # Phá giáp: +30% damage debuff
                if cd_pha_giap <= 0 and current_stamina >= 20.0 and (fish_state == "IDLE" or stun_timer > 0):
                    cd_pha_giap = 14.0
                    pha_giap_timer = 4.0
                    current_stamina -= 20.0

                # Dưỡng sức: when tension dangerously high or stamina low
                if cd_duong_suc <= 0 and (current_stamina < 30.0 or current_tension > max_tension * 0.70):
                    cd_duong_suc = 14.0
                    current_stamina = min(max_stamina, current_stamina + 45.0)
                    current_tension = max(0.0, current_tension - max_tension * 0.25)

                # Giật kép: heavy burst damage
                if cd_giat_kep <= 0 and current_stamina >= 30.0 and (fish_state == "IDLE" or stun_timer > 0) and current_tension < max_tension * 0.50:
                    cd_giat_kep = 8.0
                    current_stamina -= 30.0
                    g_dmg = (base_dmg * 4.0 + 150.0) * (2.0 if stun_timer > 0 else 1.0)
                    fish_hp -= g_dmg
                    current_tension += 16.0

                # Kéo mạnh: burst damage during IDLE or STUN
                if cd_keo_manh <= 0 and current_stamina >= 20.0 and (fish_state == "IDLE" or stun_timer > 0) and current_tension < max_tension * 0.55:
                    cd_keo_manh = 3.5
                    current_stamina -= 20.0
                    burst_dmg = (base_dmg * 2.5 + 100.0) * (2.0 if stun_timer > 0 else 1.0)
                    if random.random() < 0.20:
                        burst_dmg *= 2.0  # Bạo kích
                    fish_hp -= burst_dmg
                    current_tension += 14.0

            # Player decision based on strategy
            effective_fish_pulling = (fish_state == "PULLING" and stun_timer <= 0 and frozen_timer <= 0)

            if player_strategy == "optimal":
                if is_boss:
                    # Against Bosses:
                    if effective_fish_pulling:
                        # Human latency / reaction delay: occasional 0.15s late release when fish suddenly starts pulling
                        is_space_down = (random.random() < 0.05 and current_tension < max_tension * 0.50)
                    else:
                        is_space_down = (current_tension < max_tension * 0.52)
                else:
                    is_space_down = (not effective_fish_pulling and current_tension < max_tension * 0.85) or (effective_fish_pulling and current_tension < max_tension * 0.20)
            elif player_strategy == "aggressive":
                is_space_down = current_tension < max_tension * 0.90
            elif player_strategy == "casual":
                if not effective_fish_pulling:
                    is_space_down = current_tension < max_tension * 0.75
                else:
                    is_space_down = random.random() < 0.20 and current_tension < max_tension * 0.40

            # Tension & Damage calculations
            tension_increase = 0.0

            if effective_fish_pulling:
                pull_mult = 1.5 if is_space_down else 0.4
            else:
                pull_mult = 0.03 if is_space_down else 0.0

            pull = fish_pull_base * pull_mult
            if "xuc_tu" in skills:
                pull *= (1.0 + (time / 10.0) * 0.1)
            pull *= pull_reduction
            if luot_day_timer > 0:
                pull *= 0.40  # Lướt dây giảm 60% lực kéo

            if not is_bang_gia and frozen_timer <= 0 and stun_timer <= 0:
                tension_increase += pull * dt

            # Stamina drain & passive regen
            drain_rate = 3.0 if effective_fish_pulling else 2.0
            if has_abilities:
                drain_rate *= 0.55
                if not effective_fish_pulling:
                    current_stamina = min(max_stamina, current_stamina + 3.0 * dt)

            current_stamina -= drain_rate * dt

            # Vampirism of Set Huyết Nguyệt: restores stamina and deals damage
            if is_huyet_nguyet and is_space_down:
                drain_per_sec = min(fish_hp * 0.03, 12.0)
                stamina_heal = drain_per_sec * dt
                current_stamina = min(max_stamina, current_stamina + stamina_heal)
                fish_hp -= stamina_heal

            if current_stamina <= 0:
                return {"success": False, "duration": time, "reason": "HẾT THỂ LỰC!", "final_hp": fish_hp, "max_tension_reached": max_tension_reached}

            if is_space_down:
                if effective_fish_pulling:
                    tension_increase += (26.0 + max_tension * 0.055) * dt
                else:
                    tension_increase += (10.0 + max_tension * 0.020) * dt

                # Apply damage
                eff_dmg = base_dmg * dmg_mult
                if pha_giap_timer > 0:
                    eff_dmg *= 1.30
                if perfect_cast_timer > 0:
                    eff_dmg *= 2.0
                if stun_timer > 0:
                    eff_dmg *= 2.0
                if random.random() < 0.15:
                    eff_dmg *= 1.5
                fish_hp -= eff_dmg * dt
                current_tension += tension_increase
            else:
                tension_dec = (6.0 + max_tension * 0.04) * dt
                if "xuc_tu" in skills:
                    tension_dec *= 0.7
                current_tension = max(0.0, current_tension - tension_dec) + tension_increase

            max_tension_reached = max(max_tension_reached, current_tension)

            # Check line break with candle safety net
            if current_tension >= max_tension:
                if candle_charges > 0:
                    candle_charges -= 1
                    current_tension = max_tension * 0.50
                else:
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
            "set_name": eq_stats["set_name"],
            "rarity": eq_stats["rarity"],
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
