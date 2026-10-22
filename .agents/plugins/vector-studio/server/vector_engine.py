"""
Vector Engine for AnglerRPG:
- High-speed SVG parser and rasterizer to PNG via Pillow
- LibGDX ShapeRenderer parser and visualizer (simulates OpenGL coordinate system)
- SVG to LibGDX ShapeRenderer Java code transpiler
- Vector Artwork Quality & Aesthetic Auditor
"""

import math
import os
import re
import xml.etree.ElementTree as ET
from PIL import Image, ImageDraw

# Standard Fantasy RPG Color Palette Tokens
THEME_PALETTES = {
    "abyssal_dark": [(11, 19, 38), (15, 23, 42), (8, 14, 24), (2, 6, 13)],
    "cyan_diamond": [(56, 189, 248), (14, 165, 233), (2, 132, 199), (186, 230, 253)],
    "celestial_gold": [(251, 191, 36), (245, 158, 11), (217, 119, 6), (254, 243, 199)],
    "emerald_growth": [(16, 185, 129), (5, 150, 105), (4, 120, 87), (209, 250, 229)],
    "crimson_flame": [(239, 68, 68), (220, 38, 38), (185, 28, 28), (254, 202, 202)],
    "void_amethyst": [(168, 85, 247), (124, 58, 237), (109, 40, 217), (243, 232, 255)],
    "pure_specular": [(255, 255, 255)]
}


def parse_color(c_str, default_alpha=1.0):
    if not c_str or c_str == "none":
        return None
    c_str = c_str.strip()
    if c_str.startswith("#"):
        hex_val = c_str.lstrip("#")
        if len(hex_val) == 3:
            hex_val = "".join([c * 2 for c in hex_val])
        if len(hex_val) == 6:
            r = int(hex_val[0:2], 16)
            g = int(hex_val[2:4], 16)
            b = int(hex_val[4:6], 16)
            return (r, g, b, int(default_alpha * 255))
        elif len(hex_val) == 8:
            r = int(hex_val[0:2], 16)
            g = int(hex_val[2:4], 16)
            b = int(hex_val[4:6], 16)
            a = int(hex_val[6:8], 16)
            return (r, g, b, int(a * default_alpha))
    elif c_str.startswith("rgb"):
        m = re.findall(r"[\d.]+", c_str)
        if len(m) >= 3:
            r = int(float(m[0]))
            g = int(float(m[1]))
            b = int(float(m[2]))
            a = float(m[3]) if len(m) >= 4 else default_alpha
            return (r, g, b, int(a * 255))
    named = {
        "white": (255, 255, 255, int(default_alpha * 255)),
        "black": (0, 0, 0, int(default_alpha * 255)),
        "gold": (255, 215, 0, int(default_alpha * 255)),
        "cyan": (0, 255, 255, int(default_alpha * 255)),
        "red": (255, 0, 0, int(default_alpha * 255)),
        "blue": (0, 0, 255, int(default_alpha * 255)),
    }
    return named.get(c_str.lower(), (255, 255, 255, int(default_alpha * 255)))


def parse_path_d(d_str):
    tokens = re.findall(r"([a-zA-Z])|([-+]?(?:\d*\.\d+|\d+)(?:[eE][-+]?\d+)?)", d_str)
    commands = []
    current_cmd = None
    args = []
    for cmd, val in tokens:
        if cmd:
            if current_cmd:
                commands.append((current_cmd, args))
                args = []
            current_cmd = cmd
        elif val:
            args.append(float(val))
    if current_cmd:
        commands.append((current_cmd, args))

    points = []
    cur_x, cur_y = 0.0, 0.0
    start_x, start_y = 0.0, 0.0

    for cmd, nums in commands:
        if cmd in ("M", "m"):
            i = 0
            while i < len(nums) - 1:
                if cmd == "m" and points:
                    cur_x += nums[i]
                    cur_y += nums[i + 1]
                else:
                    cur_x = nums[i]
                    cur_y = nums[i + 1]
                if not points:
                    start_x, start_y = cur_x, cur_y
                points.append((cur_x, cur_y))
                i += 2
        elif cmd in ("L", "l"):
            i = 0
            while i < len(nums) - 1:
                if cmd == "l":
                    cur_x += nums[i]
                    cur_y += nums[i + 1]
                else:
                    cur_x = nums[i]
                    cur_y = nums[i + 1]
                points.append((cur_x, cur_y))
                i += 2
        elif cmd in ("H", "h"):
            for n in nums:
                cur_x = (cur_x + n) if cmd == "h" else n
                points.append((cur_x, cur_y))
        elif cmd in ("V", "v"):
            for n in nums:
                cur_y = (cur_y + n) if cmd == "v" else n
                points.append((cur_x, cur_y))
        elif cmd in ("C", "c"):
            i = 0
            while i < len(nums) - 5:
                if cmd == "c":
                    x1, y1 = cur_x + nums[i], cur_y + nums[i + 1]
                    x2, y2 = cur_x + nums[i + 2], cur_y + nums[i + 3]
                    x3, y3 = cur_x + nums[i + 4], cur_y + nums[i + 5]
                else:
                    x1, y1 = nums[i], nums[i + 1]
                    x2, y2 = nums[i + 2], nums[i + 3]
                    x3, y3 = nums[i + 4], nums[i + 5]
                for step in range(1, 9):
                    t = step / 8.0
                    it = 1.0 - t
                    px = it**3 * cur_x + 3 * it**2 * t * x1 + 3 * it * t**2 * x2 + t**3 * x3
                    py = it**3 * cur_y + 3 * it**2 * t * y1 + 3 * it * t**2 * y2 + t**3 * y3
                    points.append((px, py))
                cur_x, cur_y = x3, y3
                i += 6
        elif cmd in ("Z", "z"):
            points.append((start_x, start_y))
            cur_x, cur_y = start_x, start_y
    return points


class VectorEngine:

    @staticmethod
    def render_svg_to_png(svg_content, out_path, width=256, height=256, bg_color=(11, 19, 38, 255)):
        os.makedirs(os.path.dirname(os.path.abspath(out_path)), exist_ok=True)
        img = Image.new("RGBA", (width, height), bg_color if bg_color else (0, 0, 0, 0))
        draw = ImageDraw.Draw(img)

        try:
            root = ET.fromstring(svg_content)
        except Exception as e:
            return {"success": False, "error": f"Invalid SVG XML: {str(e)}"}

        vb = root.attrib.get("viewBox")
        vb_w, vb_h = float(width), float(height)
        vb_x, vb_y = 0.0, 0.0
        if vb:
            parts = [float(p) for p in re.findall(r"[-+]?(?:\d*\.\d+|\d+)", vb)]
            if len(parts) >= 4:
                vb_x, vb_y, vb_w, vb_h = parts[0], parts[1], parts[2], parts[3]
        else:
            vb_w = float(root.attrib.get("width", width))
            vb_h = float(root.attrib.get("height", height))

        sx = width / vb_w if vb_w else 1.0
        sy = height / vb_h if vb_h else 1.0

        def tx(x): return (float(x) - vb_x) * sx
        def ty(y): return (float(y) - vb_y) * sy

        for elem in root.iter():
            tag = elem.tag.split("}")[-1]
            fill = parse_color(elem.attrib.get("fill", "#ffffff"))
            stroke = parse_color(elem.attrib.get("stroke", None))
            sw = float(elem.attrib.get("stroke-width", 1.0)) * sx

            if tag == "rect":
                x = tx(elem.attrib.get("x", 0))
                y = ty(elem.attrib.get("y", 0))
                w = float(elem.attrib.get("width", 0)) * sx
                h = float(elem.attrib.get("height", 0)) * sy
                rx = float(elem.attrib.get("rx", 0)) * sx
                if rx > 0:
                    draw.rounded_rectangle([x, y, x + w, y + h], radius=rx, fill=fill, outline=stroke, width=int(max(1, sw)) if stroke else 0)
                else:
                    draw.rectangle([x, y, x + w, y + h], fill=fill, outline=stroke, width=int(max(1, sw)) if stroke else 0)

            elif tag in ("circle", "ellipse"):
                cx = tx(elem.attrib.get("cx", 0))
                cy = ty(elem.attrib.get("cy", 0))
                rx = float(elem.attrib.get("r", elem.attrib.get("rx", 0))) * sx
                ry = float(elem.attrib.get("r", elem.attrib.get("ry", 0))) * sy
                draw.ellipse([cx - rx, cy - ry, cx + rx, cy + ry], fill=fill, outline=stroke, width=int(max(1, sw)) if stroke else 0)

            elif tag == "polygon":
                pts_str = elem.attrib.get("points", "")
                coords = [float(p) for p in re.findall(r"[-+]?(?:\d*\.\d+|\d+)", pts_str)]
                pts = [(tx(coords[i]), ty(coords[i + 1])) for i in range(0, len(coords) - 1, 2)]
                if len(pts) >= 3:
                    draw.polygon(pts, fill=fill, outline=stroke)

            elif tag == "polyline":
                pts_str = elem.attrib.get("points", "")
                coords = [float(p) for p in re.findall(r"[-+]?(?:\d*\.\d+|\d+)", pts_str)]
                pts = [(tx(coords[i]), ty(coords[i + 1])) for i in range(0, len(coords) - 1, 2)]
                if len(pts) >= 2:
                    draw.line(pts, fill=stroke if stroke else fill, width=int(max(1, sw)))

            elif tag == "line":
                x1 = tx(elem.attrib.get("x1", 0))
                y1 = ty(elem.attrib.get("y1", 0))
                x2 = tx(elem.attrib.get("x2", 0))
                y2 = ty(elem.attrib.get("y2", 0))
                draw.line([(x1, y1), (x2, y2)], fill=stroke if stroke else fill, width=int(max(1, sw)))

            elif tag == "path":
                d_str = elem.attrib.get("d", "")
                pts = parse_path_d(d_str)
                t_pts = [(tx(p[0]), ty(p[1])) for p in pts]
                if len(t_pts) >= 3 and fill:
                    draw.polygon(t_pts, fill=fill, outline=stroke)
                elif len(t_pts) >= 2:
                    draw.line(t_pts, fill=stroke if stroke else fill, width=int(max(1, sw)))

        img.save(out_path, "PNG")
        return {"success": True, "out_path": out_path, "width": width, "height": height}

    @staticmethod
    def render_libgdx_to_png(java_code, out_path, width=256, height=256, cx=128, cy=128, r=64):
        """Simulates LibGDX ShapeRenderer execution into a 2D PNG preview."""
        os.makedirs(os.path.dirname(os.path.abspath(out_path)), exist_ok=True)
        img = Image.new("RGBA", (width, height), (11, 19, 38, 255))
        draw = ImageDraw.Draw(img)

        cur_color = (255, 255, 255, 255)

        # Environment variables for math evaluation
        env = {
            "cx": cx, "cy": cy, "r": r,
            "hw": r * 0.72, "hh": r * 1.15,
            "Math": math, "min": min, "max": max
        }

        def eval_val(val_str):
            s = val_str.strip().rstrip("f")
            try:
                return float(eval(s, {"__builtins__": None}, env))
            except Exception:
                nums = re.findall(r"[-+]?(?:\d*\.\d+|\d+)", s)
                return float(nums[0]) if nums else 0.0

        def flip_y(y):
            # LibGDX has Y=0 at bottom, Pillow has Y=0 at top
            return height - y

        lines = java_code.split("\n")
        for line in lines:
            line = line.strip()
            # 1. sr.setColor(r, g, b, a)
            m_col = re.search(r"sr\.setColor\(([^)]+)\)", line)
            if m_col:
                args = [a.strip() for a in m_col.group(1).split(",")]
                if len(args) == 4:
                    cr = int(eval_val(args[0]) * 255)
                    cg = int(eval_val(args[1]) * 255)
                    cb = int(eval_val(args[2]) * 255)
                    ca = int(eval_val(args[3]) * 255)
                    cur_color = (max(0, min(255, cr)), max(0, min(255, cg)), max(0, min(255, cb)), max(0, min(255, ca)))
                elif len(args) == 1:
                    # New Color(...)
                    c_inner = re.findall(r"[-+]?(?:\d*\.\d+|\d+)", args[0])
                    if len(c_inner) >= 3:
                        cr = int(float(c_inner[0]) * 255)
                        cg = int(float(c_inner[1]) * 255)
                        cb = int(float(c_inner[2]) * 255)
                        ca = int(float(c_inner[3]) * 255) if len(c_inner) >= 4 else 255
                        cur_color = (cr, cg, cb, ca)

            # 2. sr.circle(cx, cy, radius)
            m_circ = re.search(r"sr\.circle\(([^)]+)\)", line)
            if m_circ:
                args = [a.strip() for a in m_circ.group(1).split(",")]
                if len(args) >= 3:
                    ccx = eval_val(args[0])
                    ccy = flip_y(eval_val(args[1]))
                    rad = eval_val(args[2])
                    draw.ellipse([ccx - rad, ccy - rad, ccx + rad, ccy + rad], fill=cur_color)

            # 3. sr.triangle(x1, y1, x2, y2, x3, y3)
            m_tri = re.search(r"sr\.triangle\(([^)]+)\)", line)
            if m_tri:
                args = [a.strip() for a in m_tri.group(1).split(",")]
                if len(args) >= 6:
                    x1, y1 = eval_val(args[0]), flip_y(eval_val(args[1]))
                    x2, y2 = eval_val(args[2]), flip_y(eval_val(args[3]))
                    x3, y3 = eval_val(args[4]), flip_y(eval_val(args[5]))
                    draw.polygon([(x1, y1), (x2, y2), (x3, y3)], fill=cur_color)

            # 4. sr.rect(x, y, w, h)
            m_rect = re.search(r"sr\.rect\(([^)]+)\)", line)
            if m_rect:
                args = [a.strip() for a in m_rect.group(1).split(",")]
                if len(args) >= 4:
                    rx = eval_val(args[0])
                    rw = eval_val(args[2])
                    rh = eval_val(args[3])
                    ry = flip_y(eval_val(args[1]) + rh)
                    draw.rectangle([rx, ry, rx + rw, ry + rh], fill=cur_color)

            # 5. sr.line(x1, y1, x2, y2)
            m_line = re.search(r"sr\.line\(([^)]+)\)", line)
            if m_line:
                args = [a.strip() for a in m_line.group(1).split(",")]
                if len(args) >= 4:
                    lx1, ly1 = eval_val(args[0]), flip_y(eval_val(args[1]))
                    lx2, ly2 = eval_val(args[2]), flip_y(eval_val(args[3]))
                    draw.line([(lx1, ly1), (lx2, ly2)], fill=cur_color, width=2)

        img.save(out_path, "PNG")
        return {"success": True, "out_path": out_path, "width": width, "height": height}

    @staticmethod
    def svg_to_libgdx(svg_content, method_name="drawCustomVector", scale_factor=1.0):
        try:
            root = ET.fromstring(svg_content)
        except Exception as e:
            return f"// Error parsing SVG: {e}"

        vb = root.attrib.get("viewBox")
        vb_w, vb_h = 100.0, 100.0
        if vb:
            parts = [float(p) for p in re.findall(r"[-+]?(?:\d*\.\d+|\d+)", vb)]
            if len(parts) >= 4:
                vb_w, vb_h = parts[2], parts[3]

        lines = [
            f"    public static void {method_name}(ShapeRenderer sr, float cx, float cy, float r, Color color) {{",
            f"        // Generated by Vector Studio Pipeline for AnglerRPG",
            f"        float scale = (r * 2f) / {vb_w:.1f}f;",
        ]

        def col_to_sr(c_tuple):
            r, g, b, a = c_tuple[0] / 255.0, c_tuple[1] / 255.0, c_tuple[2] / 255.0, c_tuple[3] / 255.0
            return f"sr.setColor({r:.2f}f, {g:.2f}f, {b:.2f}f, {a:.2f}f);"

        half_w = vb_w / 2.0
        half_h = vb_h / 2.0

        for elem in root.iter():
            tag = elem.tag.split("}")[-1]
            fill = parse_color(elem.attrib.get("fill"))
            stroke = parse_color(elem.attrib.get("stroke"))

            if fill:
                lines.append(f"        {col_to_sr(fill)}")

            if tag == "circle":
                ecx = float(elem.attrib.get("cx", half_w)) - half_w
                ecy = half_h - float(elem.attrib.get("cy", half_h))
                erad = float(elem.attrib.get("r", 10))
                lines.append(f"        sr.circle(cx + ({ecx:.1f}f * scale), cy + ({ecy:.1f}f * scale), {erad:.1f}f * scale);")

            elif tag == "rect":
                rx = float(elem.attrib.get("x", 0)) - half_w
                rw = float(elem.attrib.get("width", 10))
                rh = float(elem.attrib.get("height", 10))
                ry = half_h - float(elem.attrib.get("y", 0)) - rh
                lines.append(f"        sr.rect(cx + ({rx:.1f}f * scale), cy + ({ry:.1f}f * scale), {rw:.1f}f * scale, {rh:.1f}f * scale);")

            elif tag == "polygon":
                pts_str = elem.attrib.get("points", "")
                coords = [float(p) for p in re.findall(r"[-+]?(?:\d*\.\d+|\d+)", pts_str)]
                pts = []
                for i in range(0, len(coords) - 1, 2):
                    px = coords[i] - half_w
                    py = half_h - coords[i + 1]
                    pts.append((px, py))
                if len(pts) == 3:
                    lines.append(f"        sr.triangle(cx + ({pts[0][0]:.1f}f * scale), cy + ({pts[0][1]:.1f}f * scale), "
                                 f"cx + ({pts[1][0]:.1f}f * scale), cy + ({pts[1][1]:.1f}f * scale), "
                                 f"cx + ({pts[2][0]:.1f}f * scale), cy + ({pts[2][1]:.1f}f * scale));")

        lines.append("    }")
        return "\n".join(lines)

    @staticmethod
    def audit_vector(svg_content):
        report = {
            "passed": True,
            "score": 100,
            "warnings": [],
            "suggestions": [],
            "palette_match": [],
            "specular_highlights": False,
            "polygon_count": 0
        }

        try:
            root = ET.fromstring(svg_content)
        except Exception as e:
            return {"passed": False, "score": 0, "error": f"Invalid XML: {e}"}

        elements = list(root.iter())
        report["element_count"] = len(elements)

        has_white_glint = False
        colors_found = []

        for elem in elements:
            fill_str = elem.attrib.get("fill")
            if fill_str:
                col = parse_color(fill_str)
                if col:
                    colors_found.append(col)
                    if col[0] > 240 and col[1] > 240 and col[2] > 240:
                        has_white_glint = True

        report["specular_highlights"] = has_white_glint
        if not has_white_glint:
            report["warnings"].append("Thiếu điểm sáng chói (Specular Glint / White Highlight). Game RPG cần đốm sáng trắng để tạo độ bóng và tương phản.")
            report["score"] -= 15

        if len(elements) < 4:
            report["warnings"].append("Độ chi tiết hình học quá thấp (dưới 4 lớp đa giác/shapes). Nên bổ sung bóng đổ (shading) và facet góc cạnh.")
            report["score"] -= 15

        if len(elements) > 80:
            report["warnings"].append("Số lượng đỉnh và thẻ vẽ quá lớn (> 80 elements). Có thể ảnh hưởng đến batching của ShapeRenderer ở 60 FPS.")
            report["score"] -= 10

        if report["score"] < 70:
            report["passed"] = False

        report["suggestions"].append("Đảm bảo sử dụng bảng màu Cyan Sapphire hoặc Hoàng Kim để hòa hợp với phong cách đại dương thần thoại của AnglerRPG.")
        return report

    @staticmethod
    def cleanup_pipeline(workspace_root=None, target="intermediate", asset_name=None, dry_run=False):
        """
        Final step of the graphics pipeline:
        Cleans up intermediate files, temporary previews, test renders, and obsolete files
        after the official vector artwork has been applied into the game code.

        target:
          - 'intermediate': Deletes temporary previews (e.g. old_*, temp_*, test_*, preview_*, draft_*).
          - 'asset': Deletes preview and temporary files for a specific asset name (e.g. 'thoi_khong').
          - 'previews': Deletes all rendered PNG preview files in assets/vectors/previews.
          - 'all': Deletes intermediate files, all previews, and workspace temporary save snapshots.
        """
        if workspace_root is None:
            workspace_root = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", "..", ".."))

        previews_dir = os.path.join(workspace_root, "assets", "vectors", "previews")
        deleted_files = []
        total_freed_bytes = 0

        candidates = []

        if os.path.exists(previews_dir):
            for fname in os.listdir(previews_dir):
                fpath = os.path.join(previews_dir, fname)
                if not os.path.isfile(fpath):
                    continue

                should_delete = False
                lower_name = fname.lower()

                if target == "intermediate":
                    # Delete temporary prefixes or test files
                    if any(lower_name.startswith(prefix) for prefix in ("old_", "temp_", "test_", "preview_", "draft_")):
                        should_delete = True
                elif target == "asset":
                    if asset_name and asset_name.lower() in lower_name:
                        should_delete = True
                elif target in ("previews", "all"):
                    should_delete = True

                if should_delete:
                    candidates.append(fpath)

        # Also search for temporary/draft SVGs in assets/vectors/
        vectors_dir = os.path.join(workspace_root, "assets", "vectors")
        if os.path.exists(vectors_dir):
            for root, dirs, files in os.walk(vectors_dir):
                if os.path.basename(root) == "previews":
                    continue
                for fname in files:
                    lower_name = fname.lower()
                    if target == "intermediate":
                        if lower_name.startswith(("old_", "temp_", "test_", "draft_", "demo_")):
                            candidates.append(os.path.join(root, fname))
                    elif target == "asset":
                        if asset_name and asset_name.lower() in lower_name and lower_name.startswith(("old_", "temp_", "test_", "draft_")):
                            candidates.append(os.path.join(root, fname))

        if target == "all":
            # Also clean temporary snapshot save files in workspace root (keeping angler_save.json safe)
            for fname in os.listdir(workspace_root):
                lower_name = fname.lower()
                if lower_name.startswith("angler_save_") and lower_name.endswith(".json") and lower_name != "angler_save.json":
                    candidates.append(os.path.join(workspace_root, fname))

        for fpath in candidates:
            try:
                size = os.path.getsize(fpath)
                if not dry_run:
                    os.remove(fpath)
                deleted_files.append({
                    "path": fpath,
                    "name": os.path.basename(fpath),
                    "size_bytes": size
                })
                total_freed_bytes += size
            except Exception as e:
                pass

        action_desc = "Would delete" if dry_run else "Successfully deleted"
        return {
            "success": True,
            "target": target,
            "dry_run": dry_run,
            "deleted_count": len(deleted_files),
            "freed_bytes": total_freed_bytes,
            "deleted_files": deleted_files,
            "message": f"{action_desc} {len(deleted_files)} intermediate file(s) ({total_freed_bytes / 1024:.1f} KB freed)."
        }

