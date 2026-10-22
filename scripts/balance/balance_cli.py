#!/usr/bin/env python3
"""
AnglerRPG Game Data & Balance Pipeline CLI:
Commands:
  combat    --fish "Cá Chép" [--set "Tập Sự"] [--rarity "COMMON"] [--iter 500]
  gacha     [--sims 10000] [--target 3]
  economy   [--days 30] [--agents 100]
  solve     [--target-boss 65]
  all       (Runs comprehensive balance test suite)
"""

import sys
import os
import argparse
import json

if hasattr(sys.stdout, 'reconfigure'):
    sys.stdout.reconfigure(encoding='utf-8')

# Add current folder to path
CURRENT_DIR = os.path.dirname(os.path.abspath(__file__))
if CURRENT_DIR not in sys.path:
    sys.path.insert(0, CURRENT_DIR)

from data_extractor import get_fishes_data, calculate_equipment_stats
from combat_simulator import CombatSimulator
from gacha_simulator import GachaSimulator
from economy_model import AnglerEconomyModel
from curve_solver import CurveSolver


def cmd_combat(args):
    print(f"\n🎣 === COMBAT & TENSION SIMULATION: {args.fish} vs [{args.set} ({args.rarity})] ===")
    res = CombatSimulator.simulate_matchup(
        fish_name=args.fish,
        set_name=args.set,
        rarity=args.rarity,
        iterations=args.iter,
        strategy=args.strategy
    )
    print(f"- Cá mục tiêu: {res['fish_name']} ({res['fish_rarity']}, HP: {res['fish_hp']})")
    print(f"- Bộ trang bị: {res['set_name']} [{res['rarity']}]")
    print(f"- Tỷ lệ kéo thành công (Win Rate): {res['win_rate_pct']}% ({args.iter} lượt mô phỏng)")
    print(f"- Tỷ lệ đứt dây (Line Snap): {res['line_snap_pct']}% | Hết thể lực: {res['stamina_out_pct']}%")
    print(f"- Thời gian kéo trung bình (Mean TTK): {res['mean_ttk_sec']}s (±{res['std_ttk_sec']}s)")
    print(f"- Median TTK: {res['median_ttk_sec']}s | 90% người chơi kéo xong trong: {res['p90_ttk_sec']}s")
    print(f"- Nhanh nhất: {res['min_ttk_sec']}s | Lâu nhất: {res['max_ttk_sec']}s")

    # Evaluation
    ttk = res['mean_ttk_sec']
    if res['win_rate_pct'] < 50:
        print("⚠️ CẢNH BÁO CÂN BẰNG: Tỷ lệ thất bại quá cao (>50%). Cá quá khỏe so với trang bị này.")
    elif ttk < 5.0 and res['fish_rarity'] in ("EPIC", "BOSS"):
        print("⚠️ CẢNH BÁO CÂN BẰNG: Cá chết quá nhanh (< 5s). Mất độ kịch tính của Boss!")
    else:
        print("✅ ĐÁNH GIÁ: Thông số giằng co nằm trong ngưỡng trải nghiệm tốt.")
    return 0


def cmd_gacha(args):
    cost_per_pull = args.cost if args.cost is not None else (20 if args.banner == "event" else 10)
    banner_label = "Sự Kiện (Limited Event)" if args.banner == "event" else "Tiêu Chuẩn (Standard)"
    print(f"\n💎 === GACHA MONTE CARLO & PITY SIMULATION: Banner {banner_label} ({cost_per_pull} KC/vé, {args.sims} runs) ===")
    res = GachaSimulator.run_monte_carlo_gacha(
        simulations=args.sims,
        target_unique_pieces=args.target,
        diamond_cost_per_pull=cost_per_pull,
        banner_type=args.banner
    )
    print(f"- Loại Banner: {banner_label} | Đơn giá: {cost_per_pull} Kim Cương / lượt")
    print(f"- Mục tiêu: Thu thập đủ {res['target_pieces']}/3 món Set (Trượng, Dây, Lưỡi)")
    print(f"- Số lượt quay trung bình: {res['mean_pulls']} vé (~{res['mean_diamond_cost']:,} Kim Cương)")
    print(f"- Trung vị (Median 50% người chơi): {res['median_pulls']} vé (~{res['median_diamond_cost']:,} Kim Cương)")
    print(f"- 75% người chơi hoàn thành trong: {res['p75_pulls']} vé (~{int(res['p75_pulls'] * cost_per_pull):,} Kim Cương)")
    print(f"- 90% người chơi hoàn thành trong: {res['p90_pulls']} vé (~{res['p90_diamond_cost']:,} Kim Cương)")
    print(f"- Trường hợp đen đủi nhất (P99): {res['p99_pulls']} vé (~{res['p99_diamond_cost']:,} Kim Cương)")
    return 0


def cmd_economy(args):
    print(f"\n📊 === AGENT-BASED ECONOMY SIMULATION (Mesa Framework: {args.agents} agents, {args.days} days) ===")
    model = AnglerEconomyModel(n_agents=args.agents)
    df = model.run_simulation(days=args.days)

    day_30 = df[df["day"] == args.days]
    print(f"\n--- Kết quả sau {args.days} ngày mô phỏng: ---")
    for _, row in day_30.iterrows():
        print(f"* Nhóm [{row['archetype'].upper()}]:")
        print(f"  - Cấp độ trung bình: Lv.{row['mean_level']}")
        print(f"  - Số dư Vàng trung bình: {row['mean_gold']:,} Gold")
        print(f"  - Số dư Kim Cương còn lại: {row['mean_diamonds']} KC")
        print(f"  - Tổng lượt Gacha đã thực hiện: {row['mean_gacha_pulls']} lượt")
    return 0


def cmd_solve(args):
    print("\n📐 === SCIPY & SYMPY MATHEMATICAL CURVE SOLVER ===")
    target_ttks = [10.0, 18.0, 25.0, 38.0, float(args.target_boss)]
    res = CurveSolver.solve_fish_hp_scaling(target_ttks=target_ttks)

    print(f"- Công thức đường cong tối ưu hóa (Power Law):")
    print(f"  {res['formula']}")
    print(f"- Bảng máu cá khuyến nghị theo các bậc để đạt TTK chuẩn:")
    for tier, ttk, hp in zip(res['tiers'], res['target_ttks_sec'], res['recommended_hps']):
        print(f"  * {tier:10s} -> Target TTK: {ttk:4.1f}s | Recommended MaxHP: {hp:6.1f}")
    return 0


def cmd_exp(args):
    from exp_curve_analyzer import analyze
    analyze()
    return 0


def cmd_shop(args):
    from shop_roi_simulator import run_roi_cli
    run_roi_cli()
    return 0


def main():
    parser = argparse.ArgumentParser(description="AnglerRPG Game Data & Balance Pipeline CLI")
    subparsers = parser.add_subparsers(dest="command", required=True)

    # combat
    p_combat = subparsers.add_parser("combat", help="Simulate fishing combat & tension")
    p_combat.add_argument("--fish", default="Cá Chép", help="Name of fish")
    p_combat.add_argument("--set", default="Tập Sự", help="Name of equipment set")
    p_combat.add_argument("--rarity", default="COMMON", help="Equipment rarity")
    p_combat.add_argument("--iter", type=int, default=500, help="Number of iterations")
    p_combat.add_argument("--strategy", default="optimal", choices=["optimal", "aggressive", "casual"])

    # gacha
    p_gacha = subparsers.add_parser("gacha", help="Monte Carlo gacha simulation")
    p_gacha.add_argument("--sims", type=int, default=10000, help="Number of simulations")
    p_gacha.add_argument("--target", type=int, default=3, help="Unique set pieces to collect")
    p_gacha.add_argument("--banner", default="event", choices=["event", "standard"], help="Banner type: event (20 KC) or standard (10 KC)")
    p_gacha.add_argument("--cost", type=int, default=None, help="Custom diamond cost per pull (defaults to 20 for event, 10 for standard)")

    # economy
    p_econ = subparsers.add_parser("economy", help="Agent-based economy simulation")
    p_econ.add_argument("--days", type=int, default=30, help="Number of days to simulate")
    p_econ.add_argument("--agents", type=int, default=100, help="Number of agents")

    # solve
    p_solve = subparsers.add_parser("solve", help="Curve fitting for fish HP & damage")
    p_solve.add_argument("--target-boss", default=65.0, type=float, help="Target TTK for boss")

    # exp
    subparsers.add_parser("exp", help="Audit EXP progression curve and level milestones")

    # shop
    subparsers.add_parser("shop", help="Analyze shop item ROI and economy balance across maps")

    args = parser.parse_args()
    if args.command == "combat":
        sys.exit(cmd_combat(args))
    elif args.command == "gacha":
        sys.exit(cmd_gacha(args))
    elif args.command == "economy":
        sys.exit(cmd_economy(args))
    elif args.command == "solve":
        sys.exit(cmd_solve(args))
    elif args.command == "exp":
        sys.exit(cmd_exp(args))
    elif args.command == "shop":
        sys.exit(cmd_shop(args))


if __name__ == "__main__":
    main()
