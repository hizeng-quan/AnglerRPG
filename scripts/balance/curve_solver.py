"""
AnglerRPG Curve Solver & Mathematical Optimizer:
Uses SymPy for analytical equations and SciPy for numerical curve fitting and optimization.
Calculates optimal Fish HP and Equipment Damage multipliers to meet target TTK bounds.
"""

import sympy as sp
import numpy as np
from scipy.optimize import curve_fit, minimize
from data_extractor import get_fishes_data, calculate_equipment_stats

class CurveSolver:

    @staticmethod
    def solve_fish_hp_scaling(target_ttks=None):
        """
        Uses SciPy to optimize Fish HP curve across 5 tiers:
        Tier 0: COMMON (target 10s)
        Tier 1: UNCOMMON (target 18s)
        Tier 2: RARE (target 25s)
        Tier 3: EPIC (target 38s)
        Tier 4: BOSS (target 65s)
        """
        if target_ttks is None:
            target_ttks = [10.0, 18.0, 25.0, 38.0, 65.0]

        tiers = np.array([0, 1, 2, 3, 4])
        targets = np.array(target_ttks)

        # Baseline player damage at respective tiers
        # Tier 0 (Tập sự): ~10 dmg/s
        # Tier 1 (Thợ săn): ~16 dmg/s
        # Tier 2 (Băng giá): ~24 dmg/s
        # Tier 3 (Đại dương/Hải tặc): ~35 dmg/s
        # Tier 4 (Vực thẳm/Thời không/Lạc Hồng): ~50 dmg/s
        player_dps = np.array([10.0, 16.0, 24.0, 35.0, 50.0])

        # Target HP = target_ttk * player_dps (factoring ~40% idle duty cycle of reeling)
        # Reeling duty cycle is ~0.55 (player only reels ~55% of time during fight)
        duty_cycle = 0.55
        target_hps = targets * player_dps * duty_cycle

        # Fit an exponential/power law: HP(tier) = a * (tier + 1)^b + c
        def power_law(t, a, b, c):
            return a * np.power(t + 1, b) + c

        popt, _ = curve_fit(power_law, tiers, target_hps, p0=[40.0, 2.0, 10.0])

        calculated_hps = [round(float(power_law(t, *popt)), 1) for t in tiers]

        return {
            "tiers": ["COMMON", "UNCOMMON", "RARE", "EPIC", "BOSS"],
            "target_ttks_sec": target_ttks,
            "recommended_hps": calculated_hps,
            "formula": f"HP(tier) = {popt[0]:.2f} * (tier + 1)^{popt[1]:.2f} + {popt[2]:.2f}",
            "fit_params": {
                "a": round(float(popt[0]), 3),
                "b": round(float(popt[1]), 3),
                "c": round(float(popt[2]), 3)
            }
        }

    @staticmethod
    def solve_analytical_damage_formula():
        """
        Uses SymPy to derive the analytical TTK formula.
        """
        hp, base_dmg, set_mult, dmg_red = sp.symbols('hp base_dmg set_mult dmg_red', positive=True)
        duty_cycle = sp.Symbol('duty_cycle', positive=True)

        effective_dmg_per_sec = base_dmg * (1 - dmg_red) * duty_cycle
        ttk = hp / effective_dmg_per_sec

        # Partial derivatives
        d_ttk_d_hp = sp.diff(ttk, hp)
        d_ttk_d_dmg = sp.diff(ttk, base_dmg)

        return {
            "ttk_symbolic": str(ttk),
            "sensitivity_to_hp": str(d_ttk_d_hp),
            "sensitivity_to_dmg": str(d_ttk_d_dmg)
        }
