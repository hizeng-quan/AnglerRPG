#!/usr/bin/env python3
"""
AnglerRPG Vector Graphics Pipeline CLI
Commands:
  render <file.svg> [--out preview.png] [--size 256]
  audit <file.svg | dir>
  transpile <file.svg> [--method drawCustomIcon]
  preview-java "<code snippet>" [--name preview]
  batch-render [--dir assets/vectors]
"""

import sys
import os
import argparse
import glob

# Add plugin server directory to path
SERVER_DIR = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".agents", "plugins", "vector-studio", "server"))
if SERVER_DIR not in sys.path:
    sys.path.insert(0, SERVER_DIR)

from vector_engine import VectorEngine

WORKSPACE_ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))
DEFAULT_PREVIEWS = os.path.join(WORKSPACE_ROOT, "assets", "vectors", "previews")


def cmd_render(args):
    svg_path = args.file
    if not os.path.exists(svg_path):
        print(f"Error: File '{svg_path}' not found.")
        return 1

    with open(svg_path, "r", encoding="utf-8") as f:
        svg_content = f.read()

    base_name = os.path.splitext(os.path.basename(svg_path))[0]
    out_png = args.out if args.out else os.path.join(DEFAULT_PREVIEWS, f"{base_name}.png")
    w = args.size
    h = args.size

    res = VectorEngine.render_svg_to_png(svg_content, out_png, width=w, height=h)
    if res.get("success"):
        print(f"[OK] Rendered preview: {out_png} ({w}x{h})")
        return 0
    else:
        print(f"[ERROR] Failed to render: {res.get('error')}")
        return 1


def cmd_audit(args):
    target = args.target
    files = []
    if os.path.isdir(target):
        files = glob.glob(os.path.join(target, "**", "*.svg"), recursive=True)
    elif os.path.isfile(target):
        files = [target]
    else:
        print(f"Error: Target '{target}' not found.")
        return 1

    print(f"=== Auditing {len(files)} Vector Asset(s) ===")
    total_passed = 0
    for fpath in files:
        with open(fpath, "r", encoding="utf-8") as f:
            content = f.read()
        res = VectorEngine.audit_vector(content)
        status = "PASSED" if res.get("passed") else "FAILED"
        if res.get("passed"):
            total_passed += 1
        print(f"\n- [{status}] {os.path.basename(fpath)} (Score: {res.get('score')}/100, Elements: {res.get('element_count')})")
        if res.get("warnings"):
            for w in res.get("warnings"):
                print(f"  * Warning: {w}")

    print(f"\nAudit complete: {total_passed}/{len(files)} passed.")
    return 0


def cmd_transpile(args):
    svg_path = args.file
    if not os.path.exists(svg_path):
        print(f"Error: File '{svg_path}' not found.")
        return 1

    with open(svg_path, "r", encoding="utf-8") as f:
        svg_content = f.read()

    method_name = args.method if args.method else "draw" + os.path.splitext(os.path.basename(svg_path))[0].title().replace("_", "")
    code = VectorEngine.svg_to_libgdx(svg_content, method_name=method_name)
    print("\n// ===== GENERATED LIBGDX SHAPERENDERER CODE =====")
    print(code)
    print("// ===============================================")
    return 0


def cmd_preview_java(args):
    java_code = args.code
    name = args.name if args.name else "java_preview"
    out_png = os.path.join(DEFAULT_PREVIEWS, f"{name}.png")
    w = args.size
    h = args.size

    res = VectorEngine.render_libgdx_to_png(java_code, out_png, width=w, height=h, cx=w//2, cy=h//2, r=min(w, h)//4)
    if res.get("success"):
        print(f"[OK] Generated Java ShapeRenderer preview: {out_png} ({w}x{h})")
        return 0
    else:
        print("[ERROR] Failed to simulate ShapeRenderer code.")
        return 1


def cmd_clean(args):
    print("=== Vector Graphics Pipeline: Final Cleanup Step ===")
    res = VectorEngine.cleanup_pipeline(
        workspace_root=WORKSPACE_ROOT,
        target=args.target,
        asset_name=args.asset,
        dry_run=args.dry_run
    )
    print(res.get("message"))
    if res.get("deleted_files"):
        print("Items:")
        for item in res.get("deleted_files"):
            print(f"  - {item['name']} ({item['size_bytes']} bytes)")
    return 0


def main():
    parser = argparse.ArgumentParser(description="AnglerRPG Vector Graphics Pipeline")
    subparsers = parser.add_subparsers(dest="command", required=True)

    # render
    p_render = subparsers.add_parser("render", help="Render SVG to PNG preview")
    p_render.add_argument("file", help="Path to SVG file")
    p_render.add_argument("--out", help="Output PNG path")
    p_render.add_argument("--size", type=int, default=256, help="Width/Height in px")

    # audit
    p_audit = subparsers.add_parser("audit", help="Audit vector asset quality")
    p_audit.add_argument("target", help="SVG file or directory to audit")

    # transpile
    p_trans = subparsers.add_parser("transpile", help="Convert SVG to Java ShapeRenderer code")
    p_trans.add_argument("file", help="Path to SVG file")
    p_trans.add_argument("--method", help="Java method name")

    # preview-java
    p_pjava = subparsers.add_parser("preview-java", help="Preview ShapeRenderer Java code directly")
    p_pjava.add_argument("code", help="Java code snippet")
    p_pjava.add_argument("--name", default="java_preview", help="Output preview name")
    p_pjava.add_argument("--size", type=int, default=256, help="Width/Height in px")

    # clean / cleanup
    p_clean = subparsers.add_parser("clean", aliases=["cleanup"], help="Final pipeline step: Delete unnecessary intermediate/temp files after applying code to game")
    p_clean.add_argument("--target", choices=["intermediate", "asset", "previews", "all"], default="intermediate",
                         help="Target scope: 'intermediate' (old_*, temp_*, test_* drafts), 'asset', 'previews', or 'all'")
    p_clean.add_argument("--asset", help="Asset name filter (e.g. thoi_khong) when target='asset'")
    p_clean.add_argument("--dry-run", action="store_true", help="Simulate deletion without actually removing files")

    args = parser.parse_args()
    if args.command == "render":
        sys.exit(cmd_render(args))
    elif args.command == "audit":
        sys.exit(cmd_audit(args))
    elif args.command == "transpile":
        sys.exit(cmd_transpile(args))
    elif args.command == "preview-java":
        sys.exit(cmd_preview_java(args))
    elif args.command in ("clean", "cleanup"):
        sys.exit(cmd_clean(args))


if __name__ == "__main__":
    main()

