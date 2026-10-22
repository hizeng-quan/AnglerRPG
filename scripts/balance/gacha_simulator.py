"""
AnglerRPG Gacha Probability & Pity Simulator:
Monte Carlo simulation of standard & limited event gacha banners.
Solves the Coupon Collector's problem with pity constraints and rate-up mechanisms.
"""

import random
import numpy as np
from scipy import stats

class GachaSimulator:

    @staticmethod
    def simulate_limited_event_pulls(target_unique_pieces=3, rate_up_chance=0.5, pity_threshold=80):
        """
        Simulates pulls until obtaining all 3 unique set pieces (e.g. Rod, Line, Hook of Lạc Hồng).
        Returns: { 'total_pulls': int, 'total_6star': int, 'pieces_collected': set }
        """
        collected = set()
        pulls = 0
        pity = 0
        total_6star = 0

        while len(collected) < target_unique_pieces:
            pulls += 1
            pity += 1

            # Roll for 6-star
            is_6star = False
            if pity >= pity_threshold:
                is_6star = True
            elif random.random() < 0.015: # 1.5% base rate
                is_6star = True

            if is_6star:
                pity = 0
                total_6star += 1
                # Check if it hits the rate-up event set
                if random.random() < rate_up_chance:
                    # Randomly pick 1 of 3 pieces: 0: Rod, 1: Line, 2: Hook
                    piece = random.randint(0, 2)
                    collected.add(piece)

        return pulls, total_6star

    @staticmethod
    def run_monte_carlo_gacha(simulations=10000, target_unique_pieces=3, diamond_cost_per_pull=20, banner_type="event"):
        """
        Runs Monte Carlo simulation for full set collection.
        Computes mean, median, standard deviation, and key percentiles.
        In AnglerRPG:
        - Standard banner: 10 diamonds / pull
        - Event banner: 20 diamonds / pull
        """
        if banner_type.lower() == "standard" and diamond_cost_per_pull == 20:
            diamond_cost_per_pull = 10
        pull_counts = []
        six_star_counts = []

        for _ in range(simulations):
            p, s = GachaSimulator.simulate_limited_event_pulls(target_unique_pieces=target_unique_pieces)
            pull_counts.append(p)
            six_star_counts.append(s)

        arr = np.array(pull_counts)

        mean_pulls = float(np.mean(arr))
        median_pulls = float(np.median(arr))
        std_pulls = float(np.std(arr))
        p25 = float(np.percentile(arr, 25))
        p75 = float(np.percentile(arr, 75))
        p90 = float(np.percentile(arr, 90))
        p99 = float(np.percentile(arr, 99))

        return {
            "simulations": simulations,
            "target_pieces": target_unique_pieces,
            "mean_pulls": round(mean_pulls, 1),
            "median_pulls": int(median_pulls),
            "std_pulls": round(std_pulls, 1),
            "p25_pulls": int(p25),
            "p75_pulls": int(p75),
            "p90_pulls": int(p90),
            "p99_pulls": int(p99),
            "mean_diamond_cost": int(mean_pulls * diamond_cost_per_pull),
            "median_diamond_cost": int(median_pulls * diamond_cost_per_pull),
            "p90_diamond_cost": int(p90 * diamond_cost_per_pull),
            "p99_diamond_cost": int(p99 * diamond_cost_per_pull)
        }
