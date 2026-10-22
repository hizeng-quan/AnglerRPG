#!/usr/bin/env python3
"""
Balance Studio MCP Server for AnglerRPG:
Implements Model Context Protocol (MCP) Stdio JSON-RPC 2.0 interface.
Exposes Monte Carlo combat tension simulation, Gacha probability solvers,
Mesa agent-based economy progression models, and SciPy curve optimizers.
"""

import os
import sys
import json

if hasattr(sys.stdout, 'reconfigure'):
    sys.stdout.reconfigure(encoding='utf-8')

SCRIPTS_BALANCE_DIR = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", "..", "..", "scripts", "balance"))
if SCRIPTS_BALANCE_DIR not in sys.path:
    sys.path.insert(0, SCRIPTS_BALANCE_DIR)

from data_extractor import get_fishes_data, calculate_equipment_stats
from combat_simulator import CombatSimulator
from gacha_simulator import GachaSimulator
from economy_model import AnglerEconomyModel
from curve_solver import CurveSolver


def send_response(request_id, result=None, error=None):
    resp = {"jsonrpc": "2.0", "id": request_id}
    if error is not None:
        resp["error"] = error
    else:
        resp["result"] = result
    out = json.dumps(resp, ensure_ascii=False)
    sys.stdout.write(out + "\n")
    sys.stdout.flush()


def handle_initialize(req_id, params):
    send_response(req_id, {
        "protocolVersion": "2024-11-05",
        "capabilities": {
            "tools": {}
        },
        "serverInfo": {
            "name": "balance-studio-mcp",
            "version": "1.0.0"
        }
    })


def handle_tools_list(req_id):
    tools = [
        {
            "name": "simulate_fishing_combat",
            "description": "Simulates 500+ real-time fishing combat encounters between a specific equipment set and a fish. Returns Win Rate %, Mean TTK (Time to Catch), Line Snap %, Stamina Out %, and balance assessment.",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "fish_name": {
                        "type": "string",
                        "description": "Exact name of the fish (e.g. 'Cá Chép', 'Bạch Tuộc Khổng Lồ', 'Cá Rô Đồng')"
                    },
                    "set_name": {
                        "type": "string",
                        "description": "Equipment set name (e.g. 'Tập Sự', 'Thời Không', 'Băng Giá', 'Thần Khí Lạc Hồng')"
                    },
                    "rarity": {
                        "type": "string",
                        "enum": ["COMMON", "UNCOMMON", "RARE", "EPIC", "LEGENDARY", "LIMITED_PREMIUM"],
                        "description": "Rarity tier of the equipment"
                    },
                    "strategy": {
                        "type": "string",
                        "enum": ["optimal", "aggressive", "casual"],
                        "description": "Simulated player behavior strategy"
                    },
                    "iterations": {
                        "type": "integer",
                        "description": "Number of Monte Carlo simulation runs (default: 500)"
                    }
                },
                "required": ["fish_name"]
            }
        },
        {
            "name": "simulate_gacha_pity",
            "description": "Runs high-speed Monte Carlo gacha simulations (10,000+ sessions) with Pity (80 pulls) and 50% Event Rate-Up. Solves the Coupon Collector's problem for obtaining all 3 set pieces.",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "simulations": {
                        "type": "integer",
                        "description": "Number of player gacha careers to simulate (default: 10000)"
                    },
                    "target_unique_pieces": {
                        "type": "integer",
                        "description": "Number of unique set pieces required (default: 3 for full Rod, Line, Hook)"
                    },
                    "banner_type": {
                        "type": "string",
                        "enum": ["event", "standard"],
                        "description": "Gacha banner type: 'event' (20 KC) or 'standard' (10 KC)"
                    },
                    "diamond_cost_per_pull": {
                        "type": "integer",
                        "description": "Custom diamond cost per pull (defaults to 20 for event, 10 for standard)"
                    }
                }
            }
        },
        {
            "name": "simulate_economy_lifecycle",
            "description": "Runs an Agent-Based Model (Mesa framework) simulating player economy over 30 days. Tracks Gold accumulation, Diamond (KC) flow, level progression, and inflation across Casual, Grinder, and Hardcore players.",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "days": {
                        "type": "integer",
                        "description": "Number of in-game days to simulate (default: 30)"
                    },
                    "agents": {
                        "type": "integer",
                        "description": "Number of simulated player agents (default: 50)"
                    }
                }
            }
        },
        {
            "name": "solve_balance_curves",
            "description": "Uses SciPy and SymPy to fit optimal Power-Law and Exponential scaling curves for Fish HP and Player Pull Damage, guaranteeing target TTK bounds.",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "target_boss_ttk": {
                        "type": "number",
                        "description": "Desired average Time-To-Catch for Bosses in seconds (default: 65.0)"
                    }
                }
            }
        }
    ]
    send_response(req_id, {"tools": tools})


def handle_tool_call(req_id, params):
    tool_name = params.get("name")
    args = params.get("arguments", {})

    if tool_name == "simulate_fishing_combat":
        fish_name = args.get("fish_name", "Cá Chép")
        set_name = args.get("set_name", "Tập Sự")
        rarity = args.get("rarity", "COMMON")
        strategy = args.get("strategy", "optimal")
        iterations = int(args.get("iterations", 500))

        try:
            res = CombatSimulator.simulate_matchup(
                fish_name=fish_name,
                set_name=set_name,
                rarity=rarity,
                iterations=iterations,
                strategy=strategy
            )
            txt = (f"=== COMBAT SIMULATION REPORT: {res['fish_name']} vs [{res['set_name']} ({res['rarity']})] ===\n"
                   f"- Win Rate: {res['win_rate_pct']}% ({iterations} matches)\n"
                   f"- Mean TTK: {res['mean_ttk_sec']}s (±{res['std_ttk_sec']}s)\n"
                   f"- Median TTK: {res['median_ttk_sec']}s | 90% Win by: {res['p90_ttk_sec']}s\n"
                   f"- Line Snap Rate: {res['line_snap_pct']}% | Stamina Out: {res['stamina_out_pct']}%\n"
                   f"- Min / Max TTK: {res['min_ttk_sec']}s / {res['max_ttk_sec']}s")
            send_response(req_id, {"content": [{"type": "text", "text": txt}]})
        except Exception as e:
            send_response(req_id, {"content": [{"type": "text", "text": f"Error: {e}"}], "isError": True})

    elif tool_name == "simulate_gacha_pity":
        sims = int(args.get("simulations", 10000))
        target = int(args.get("target_unique_pieces", 3))
        banner = args.get("banner_type", "event")
        default_cost = 20 if banner == "event" else 10
        cost = int(args.get("diamond_cost_per_pull", default_cost))

        res = GachaSimulator.run_monte_carlo_gacha(
            simulations=sims,
            target_unique_pieces=target,
            diamond_cost_per_pull=cost,
            banner_type=banner
        )
        txt = (f"=== GACHA MONTE CARLO ANALYSIS ({sims:,} runs, Banner: {banner.upper()} @ {cost} KC/pull) ===\n"
               f"- Goal: Full {target}/3 unique set pieces\n"
               f"- Mean Pulls: {res['mean_pulls']} (~{res['mean_diamond_cost']:,} Diamonds)\n"
               f"- Median (50% players): {res['median_pulls']} pulls (~{res['median_diamond_cost']:,} Diamonds)\n"
               f"- 90th Percentile: {res['p90_pulls']} pulls (~{res['p90_diamond_cost']:,} Diamonds)\n"
               f"- 99th Percentile (Worst-case): {res['p99_pulls']} pulls (~{res['p99_diamond_cost']:,} Diamonds)")
        send_response(req_id, {"content": [{"type": "text", "text": txt}]})

    elif tool_name == "simulate_economy_lifecycle":
        days = int(args.get("days", 30))
        agents = int(args.get("agents", 50))

        model = AnglerEconomyModel(n_agents=agents)
        df = model.run_simulation(days=days)
        final_day = df[df["day"] == days]

        txt = f"=== 30-DAY AGENT-BASED ECONOMY REPORT (Mesa) ===\n"
        for _, row in final_day.iterrows():
            txt += (f"[{row['archetype'].upper()} Cohort]:\n"
                    f"  - Level: Lv.{row['mean_level']}\n"
                    f"  - Gold Balance: {row['mean_gold']:,} Gold\n"
                    f"  - Diamond Balance: {row['mean_diamonds']} KC\n"
                    f"  - Gacha Pulls Done: {row['mean_gacha_pulls']} pulls\n")
        send_response(req_id, {"content": [{"type": "text", "text": txt}]})

    elif tool_name == "solve_balance_curves":
        boss_target = float(args.get("target_boss_ttk", 65.0))
        res = CurveSolver.solve_fish_hp_scaling(target_ttks=[10.0, 18.0, 25.0, 38.0, boss_target])
        txt = (f"=== SCIPY CURVE OPTIMIZATION RESULT ===\n"
               f"- Formula: {res['formula']}\n"
               f"- Recommended Fish HP Scaling:\n")
        for tier, ttk, hp in zip(res['tiers'], res['target_ttks_sec'], res['recommended_hps']):
            txt += f"  * {tier:10s} -> Target TTK: {ttk:4.1f}s | MaxHP: {hp:6.1f}\n"
        send_response(req_id, {"content": [{"type": "text", "text": txt}]})

    else:
        send_response(req_id, error={"code": -32601, "message": f"Method not found: {tool_name}"})


def main():
    while True:
        line = sys.stdin.readline()
        if not line:
            break
        line = line.strip()
        if not line:
            continue
        try:
            req = json.loads(line)
        except Exception:
            continue

        req_id = req.get("id")
        method = req.get("method")
        params = req.get("params", {})

        if method == "initialize":
            handle_initialize(req_id, params)
        elif method == "notifications/initialized":
            pass
        elif method == "ping":
            send_response(req_id, {})
        elif method == "tools/list":
            handle_tools_list(req_id)
        elif method == "tools/call":
            handle_tool_call(req_id, params)
        else:
            if req_id is not None:
                send_response(req_id, error={"code": -32601, "message": f"Method not found: {method}"})


if __name__ == "__main__":
    main()
