#!/usr/bin/env python3
"""
Vector Studio MCP Server
Implements Model Context Protocol (MCP) Stdio JSON-RPC 2.0 interface.
Exposes vector graphics drawing, conversion, visual auditing, and LibGDX ShapeRenderer transpilation tools.
"""

import os
import sys
import json
from vector_engine import VectorEngine

WORKSPACE_ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", "..", ".."))
PREVIEWS_DIR = os.path.join(WORKSPACE_ROOT, "assets", "vectors", "previews")
ARTIFACTS_DIR = os.path.join(os.path.expanduser("~"), ".gemini", "antigravity-ide", "brain")


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
            "name": "vector-studio-mcp",
            "version": "1.0.0"
        }
    })


def handle_tools_list(req_id):
    tools = [
        {
            "name": "render_vector_preview",
            "description": "Render an SVG vector string into a crystal-clear PNG preview file on disk, automatically saving to assets and providing the file path for inspection.",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "svg_content": {
                        "type": "string",
                        "description": "Raw SVG XML string with <svg>, <circle>, <rect>, <polygon>, <path> tags"
                    },
                    "asset_name": {
                        "type": "string",
                        "description": "Identifier for the asset (e.g. rod_vuctham, fish_thosan, gem_amethyst)"
                    },
                    "width": {
                        "type": "integer",
                        "description": "Width in pixels (default: 256)"
                    },
                    "height": {
                        "type": "integer",
                        "description": "Height in pixels (default: 256)"
                    }
                },
                "required": ["svg_content", "asset_name"]
            }
        },
        {
            "name": "audit_vector_artwork",
            "description": "Inspect and audit vector artwork against game quality standards (specular highlights, color contrast, facet shading, polygon density).",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "svg_content": {
                        "type": "string",
                        "description": "Raw SVG XML string to audit"
                    }
                },
                "required": ["svg_content"]
            }
        },
        {
            "name": "svg_to_libgdx_code",
            "description": "Transpile SVG vector shapes into clean, high-performance LibGDX ShapeRenderer Java code (sr.circle, sr.rect, sr.triangle).",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "svg_content": {
                        "type": "string",
                        "description": "Raw SVG XML string to convert"
                    },
                    "method_name": {
                        "type": "string",
                        "description": "Target Java method name (e.g. drawHaiTacHook, drawAbyssFish)"
                    }
                },
                "required": ["svg_content"]
            }
        },
        {
            "name": "preview_libgdx_code",
            "description": "Simulate and render LibGDX ShapeRenderer Java statements (sr.setColor, sr.rect, sr.circle, sr.triangle) into an instant PNG preview image to visually inspect the rendered Java code.",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "java_code": {
                        "type": "string",
                        "description": "Java code snippet containing sr.setColor(...), sr.triangle(...), sr.circle(...), etc."
                    },
                    "asset_name": {
                        "type": "string",
                        "description": "Name for the preview file"
                    },
                    "width": {
                        "type": "integer",
                        "description": "Preview width in px (default: 256)"
                    },
                    "height": {
                        "type": "integer",
                        "description": "Preview height in px (default: 256)"
                    }
                },
                "required": ["java_code", "asset_name"]
            }
        },
        {
            "name": "cleanup_pipeline_files",
            "description": "Final pipeline step: Delete unnecessary intermediate files, test previews (e.g. old_*, temp_*, preview_*), and temporary drafts after the vector artwork has been applied into the game code.",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "target": {
                        "type": "string",
                        "enum": ["intermediate", "asset", "previews", "all"],
                        "description": "'intermediate' for temp/old/test files, 'asset' for specific asset preview, 'previews' for all previews, 'all' for full cleanup (default: intermediate)"
                    },
                    "asset_name": {
                        "type": "string",
                        "description": "Optional asset name filter (e.g. 'thoi_khong') when target='asset'"
                    },
                    "dry_run": {
                        "type": "boolean",
                        "description": "If true, only report files that would be deleted without actually deleting them"
                    }
                }
            }
        }
    ]
    send_response(req_id, {"tools": tools})


def handle_tool_call(req_id, params):
    tool_name = params.get("name")
    args = params.get("arguments", {})

    os.makedirs(PREVIEWS_DIR, exist_ok=True)

    if tool_name == "render_vector_preview":
        svg_content = args.get("svg_content", "")
        asset_name = args.get("asset_name", "unnamed_asset")
        w = int(args.get("width", 256))
        h = int(args.get("height", 256))

        out_png = os.path.join(PREVIEWS_DIR, f"{asset_name}.png")
        svg_file = os.path.join(WORKSPACE_ROOT, "assets", "vectors", f"{asset_name}.svg")

        # Save SVG file
        with open(svg_file, "w", encoding="utf-8") as f:
            f.write(svg_content)

        # Render to PNG
        res = VectorEngine.render_svg_to_png(svg_content, out_png, width=w, height=h)
        if res.get("success"):
            audit = VectorEngine.audit_vector(svg_content)
            txt = (f"Rendered SVG preview successfully!\n"
                   f"- PNG Preview: {out_png}\n"
                   f"- SVG Source: {svg_file}\n"
                   f"- Resolution: {w}x{h}\n"
                   f"- Quality Score: {audit.get('score', 100)}/100 (Passed: {audit.get('passed', True)})\n"
                   f"- Highlights: {'Co specular glint' if audit.get('specular_highlights') else 'Chua co specular glint'}")
            send_response(req_id, {"content": [{"type": "text", "text": txt}]})
        else:
            send_response(req_id, {"content": [{"type": "text", "text": f"Error: {res.get('error')}"}], "isError": True})

    elif tool_name == "audit_vector_artwork":
        svg_content = args.get("svg_content", "")
        audit = VectorEngine.audit_vector(svg_content)
        txt = json.dumps(audit, ensure_ascii=False, indent=2)
        send_response(req_id, {"content": [{"type": "text", "text": txt}]})

    elif tool_name == "svg_to_libgdx_code":
        svg_content = args.get("svg_content", "")
        m_name = args.get("method_name", "drawCustomVector")
        java_code = VectorEngine.svg_to_libgdx(svg_content, method_name=m_name)
        send_response(req_id, {"content": [{"type": "text", "text": java_code}]})

    elif tool_name == "preview_libgdx_code":
        java_code = args.get("java_code", "")
        asset_name = args.get("asset_name", "libgdx_preview")
        w = int(args.get("width", 256))
        h = int(args.get("height", 256))

        out_png = os.path.join(PREVIEWS_DIR, f"{asset_name}.png")
        res = VectorEngine.render_libgdx_to_png(java_code, out_png, width=w, height=h, cx=w//2, cy=h//2, r=min(w, h)//4)
        if res.get("success"):
            txt = (f"Rendered LibGDX ShapeRenderer preview successfully!\n"
                   f"- PNG Preview: {out_png}\n"
                   f"- Resolution: {w}x{h}")
            send_response(req_id, {"content": [{"type": "text", "text": txt}]})
        else:
            send_response(req_id, {"content": [{"type": "text", "text": "Error rendering ShapeRenderer preview"}], "isError": True})

    elif tool_name == "cleanup_pipeline_files":
        target = args.get("target", "intermediate")
        asset_name = args.get("asset_name")
        dry_run = bool(args.get("dry_run", False))

        res = VectorEngine.cleanup_pipeline(
            workspace_root=WORKSPACE_ROOT,
            target=target,
            asset_name=asset_name,
            dry_run=dry_run
        )
        report_txt = f"{res.get('message')}\n"
        if res.get("deleted_files"):
            report_txt += "Deleted items:\n"
            for f in res.get("deleted_files"):
                report_txt += f"  - {f['name']} ({f['size_bytes']} bytes)\n"

        send_response(req_id, {"content": [{"type": "text", "text": report_txt}]})

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
