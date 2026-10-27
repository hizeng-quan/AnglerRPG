package com.fishingrpg.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.fishingrpg.game.entities.equipment.Equipment;
import com.fishingrpg.game.entities.equipment.EquipmentType;

/**
 * Dedicated renderer for unique vector icons for all equipment pieces and sets in AnglerRPG.
 * Each of the 33 pieces across 11 sets has a unique, distinct visual identity.
 */
public class EquipmentIconRenderer {

    public static void draw(ShapeRenderer sr, Equipment eq, EquipmentType fallbackType, float cx, float cy, float r, Color color) {
        if (eq == null) {
            drawEmptySlot(sr, fallbackType, cx, cy, r);
            return;
        }

        EquipmentType type = eq.type != null ? eq.type : fallbackType;
        String setName = eq.setName != null ? eq.setName : "";
        Color mainCol = eq.rarity != null ? eq.rarity.color : color;

        // Route to the specific unique vector icon based on setName and type
        switch (setName) {
            case "Tập Sự":
                if (type == EquipmentType.ROD) drawTapSuRod(sr, cx, cy, r, mainCol);
                else if (type == EquipmentType.LINE) drawTapSuLine(sr, cx, cy, r, mainCol);
                else drawTapSuHook(sr, cx, cy, r, mainCol);
                break;

            case "Thợ Săn":
                if (type == EquipmentType.ROD) drawThoSanRod(sr, cx, cy, r, mainCol);
                else if (type == EquipmentType.LINE) drawThoSanLine(sr, cx, cy, r, mainCol);
                else drawThoSanHook(sr, cx, cy, r, mainCol);
                break;

            case "Băng Giá":
                if (type == EquipmentType.ROD) drawBangGiaRod(sr, cx, cy, r, mainCol);
                else if (type == EquipmentType.LINE) drawBangGiaLine(sr, cx, cy, r, mainCol);
                else drawBangGiaHook(sr, cx, cy, r, mainCol);
                break;

            case "Đại Dương":
                if (type == EquipmentType.ROD) drawDaiDuongRod(sr, cx, cy, r, mainCol);
                else if (type == EquipmentType.LINE) drawDaiDuongLine(sr, cx, cy, r, mainCol);
                else drawDaiDuongHook(sr, cx, cy, r, mainCol);
                break;

            case "Hải Tặc":
                if (type == EquipmentType.ROD) drawHaiTacRod(sr, cx, cy, r, mainCol);
                else if (type == EquipmentType.LINE) drawHaiTacLine(sr, cx, cy, r, mainCol);
                else drawHaiTacHook(sr, cx, cy, r, mainCol);
                break;

            case "Vực Thẳm":
                if (type == EquipmentType.ROD) drawVucThamRod(sr, cx, cy, r, mainCol);
                else if (type == EquipmentType.LINE) drawVucThamLine(sr, cx, cy, r, mainCol);
                else drawVucThamHook(sr, cx, cy, r, mainCol);
                break;

            case "Hoàng Gia":
                if (type == EquipmentType.ROD) drawHoangGiaRod(sr, cx, cy, r, mainCol);
                else if (type == EquipmentType.LINE) drawHoangGiaLine(sr, cx, cy, r, mainCol);
                else drawHoangGiaHook(sr, cx, cy, r, mainCol);
                break;

            case "Thời Không":
                if (type == EquipmentType.ROD) drawThoiKhongRod(sr, cx, cy, r, mainCol);
                else if (type == EquipmentType.LINE) drawThoiKhongLine(sr, cx, cy, r, mainCol);
                else drawThoiKhongHook(sr, cx, cy, r, mainCol);
                break;

            case "Quang Minh":
            case "Thần Thoại":
                if (type == EquipmentType.ROD) drawQuangMinhRod(sr, cx, cy, r, mainCol);
                else if (type == EquipmentType.LINE) drawQuangMinhLine(sr, cx, cy, r, mainCol);
                else drawQuangMinhHook(sr, cx, cy, r, mainCol);
                break;

            case "Vệ Quốc Vang Danh":
                if (type == EquipmentType.ROD) drawVeQuocRod(sr, cx, cy, r, mainCol);
                else if (type == EquipmentType.LINE) drawVeQuocLine(sr, cx, cy, r, mainCol);
                else drawVeQuocHook(sr, cx, cy, r, mainCol);
                break;

            case "Thần Khí Lạc Hồng":
            case "Lạc Hồng":
                if (type == EquipmentType.ROD) drawLacHongRod(sr, cx, cy, r, mainCol);
                else if (type == EquipmentType.LINE) drawLacHongLine(sr, cx, cy, r, mainCol);
                else drawLacHongHook(sr, cx, cy, r, mainCol);
                break;

            case "Dạ Hội Bí Ngô":
            case "Bí Ngô":
                if (type == EquipmentType.ROD) drawPumpkinRod(sr, cx, cy, r, mainCol);
                else if (type == EquipmentType.LINE) drawSpiderSilkLine(sr, cx, cy, r, mainCol);
                else drawGhostCandyHook(sr, cx, cy, r, mainCol);
                break;

            case "Huyết Nguyệt Thần Vương":
            case "Huyết Nguyệt":
                if (type == EquipmentType.ROD) drawHuyetNguyetRod(sr, cx, cy, r, mainCol);
                else if (type == EquipmentType.LINE) drawHuyetNguyetLine(sr, cx, cy, r, mainCol);
                else drawHuyetNguyetHook(sr, cx, cy, r, mainCol);
                break;

            default:
                // Generic fallback
                drawGenericType(sr, type, cx, cy, r, mainCol);
                break;
        }
    }

    public static void drawSetPiece(ShapeRenderer sr, String setName, EquipmentType type, float cx, float cy, float r, Color color) {
        Equipment fake = new Equipment();
        fake.setName = setName;
        fake.type = type;
        draw(sr, fake, type, cx, cy, r, color);
    }

    // =========================================================================
    // CURRENCY ICONS: DIAMOND, NORMAL TICKET, PREMIUM TICKET (UNIVERSAL)
    // =========================================================================
    public static void drawDiamondIcon(ShapeRenderer sr, float cx, float cy, float r) {
        float hw = r * 0.72f;
        float hh = r * 1.15f;

        // 1. Soft atmospheric cyan glow aura
        sr.setColor(0.25f, 0.75f, 1f, 0.22f);
        sr.circle(cx, cy, r * 1.35f);

        // 2. Base Rhombic Diamond Body
        // Left facet (Vibrant royal cyan)
        sr.setColor(0.20f, 0.65f, 0.95f, 1f);
        sr.triangle(cx, cy + hh, cx - hw, cy, cx, cy - hh);
        // Right facet (Deep abyssal sapphire shadow)
        sr.setColor(0.10f, 0.42f, 0.80f, 1f);
        sr.triangle(cx, cy + hh, cx + hw, cy, cx, cy - hh);

        // 3. Multi-angle crystal facets & light reflections
        // Top-left reflective facet (Bright aqua-white)
        sr.setColor(0.75f, 0.95f, 1f, 1f);
        sr.triangle(cx, cy + hh, cx - hw * 0.45f, cy + hh * 0.2f, cx, cy);
        // Center core facet (Luminous crystalline cyan)
        sr.setColor(0.45f, 0.88f, 1f, 0.95f);
        sr.triangle(cx, cy + hh * 0.55f, cx - hw * 0.35f, cy, cx, cy - hh * 0.55f);
        sr.triangle(cx, cy + hh * 0.55f, cx + hw * 0.35f, cy, cx, cy - hh * 0.55f);

        // 4. Brilliant specular glint on upper-left vertex & peak
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(cx - hw * 0.25f, cy + hh * 0.35f, Math.max(1.5f, r * 0.16f));
        sr.circle(cx, cy + hh - 1.5f, Math.max(1f, r * 0.11f));
    }

    public static void drawGoldCoinIcon(ShapeRenderer sr, float cx, float cy, float r) {
        // 1. Warm golden aura
        sr.setColor(0.95f, 0.72f, 0.15f, 0.22f);
        sr.circle(cx, cy, r * 1.30f);

        // 2. Outer rim of the gold coin
        sr.setColor(0.85f, 0.65f, 0.12f, 1f);
        sr.circle(cx, cy, r);

        // 3. Main coin body (Radiant polished gold)
        sr.setColor(1.0f, 0.85f, 0.22f, 1f);
        sr.circle(cx, cy, r * 0.82f);

        // 4. Inner embossed ring
        sr.setColor(0.88f, 0.68f, 0.15f, 1f);
        sr.circle(cx, cy, r * 0.65f);
        sr.setColor(1.0f, 0.88f, 0.28f, 1f);
        sr.circle(cx, cy, r * 0.52f);

        // 5. Specular glint on upper-left rim
        sr.setColor(1f, 1f, 0.85f, 0.9f);
        sr.circle(cx - r * 0.3f, cy + r * 0.3f, Math.max(1f, r * 0.22f));
    }

    public static void drawNormalTicketIcon(ShapeRenderer sr, float cx, float cy, float w, float h) {
        // 1. Warm Golden ticket base
        sr.setColor(0.95f, 0.70f, 0.20f, 1f);
        sr.rect(cx - w / 2f, cy - h / 2f, w, h);

        // 2. Inner border / decorative rim
        sr.setColor(1f, 0.88f, 0.35f, 1f);
        sr.rect(cx - w / 2f + 1.5f, cy - h / 2f + 1.5f, w - 3f, h - 3f);
        sr.setColor(0.92f, 0.65f, 0.15f, 1f);
        sr.rect(cx - w / 2f + 2.5f, cy - h / 2f + 2.5f, w - 5f, h - 5f);

        // 3. Side semi-circle ticket notches
        sr.setColor(0.08f, 0.12f, 0.18f, 1f);
        sr.circle(cx - w / 2f, cy, h * 0.26f);
        sr.circle(cx + w / 2f, cy, h * 0.26f);

        // 4. Center punch hole (dark circle like in event shop!)
        sr.setColor(0.10f, 0.14f, 0.22f, 1f);
        sr.circle(cx, cy, h * 0.22f);
        sr.setColor(0.14f, 0.18f, 0.28f, 1f);
        sr.circle(cx, cy, h * 0.12f);
    }

    public static void drawPremiumTicketIcon(ShapeRenderer sr, float cx, float cy, float w, float h) {
        // 1. Royal Purple / Violet ticket base (matching Event shop!)
        sr.setColor(0.70f, 0.32f, 0.88f, 1f);
        sr.rect(cx - w / 2f, cy - h / 2f, w, h);

        // 2. Inner border / decorative rim
        sr.setColor(0.85f, 0.45f, 1f, 1f);
        sr.rect(cx - w / 2f + 1.5f, cy - h / 2f + 1.5f, w - 3f, h - 3f);
        sr.setColor(0.58f, 0.22f, 0.75f, 1f);
        sr.rect(cx - w / 2f + 2.5f, cy - h / 2f + 2.5f, w - 5f, h - 5f);

        // 3. Side semi-circle ticket notches
        sr.setColor(0.08f, 0.12f, 0.18f, 1f);
        sr.circle(cx - w / 2f, cy, h * 0.26f);
        sr.circle(cx + w / 2f, cy, h * 0.26f);

        // 4. Center punch hole (Golden circle emblem like in event shop!)
        sr.setColor(1f, 0.85f, 0.30f, 1f);
        sr.circle(cx, cy, h * 0.22f);
        sr.setColor(1f, 0.95f, 0.55f, 1f);
        sr.circle(cx, cy, h * 0.12f);
    }

    private static void drawEmptySlot(ShapeRenderer sr, EquipmentType type, float cx, float cy, float r) {
        sr.setColor(0.25f, 0.35f, 0.45f, 0.35f);
        if (type == EquipmentType.ROD) {
            sr.rectLine(cx - r * 0.65f, cy - r * 0.65f, cx + r * 0.65f, cy + r * 0.65f, 2f);
            sr.circle(cx - r * 0.5f, cy - r * 0.5f, r * 0.18f);
        } else if (type == EquipmentType.LINE) {
            sr.circle(cx, cy, r * 0.6f);
            sr.setColor(0.06f, 0.08f, 0.14f, 1f);
            sr.circle(cx, cy, r * 0.35f);
        } else {
            sr.rectLine(cx - r * 0.2f, cy + r * 0.6f, cx - r * 0.2f, cy - r * 0.3f, 2f);
            sr.rectLine(cx - r * 0.2f, cy - r * 0.3f, cx + r * 0.4f, cy - r * 0.3f, 2f);
            sr.rectLine(cx + r * 0.4f, cy - r * 0.3f, cx + r * 0.4f, cy + r * 0.2f, 2f);
        }
    }

    // =========================================================================
    // SVG PIXEL-PERFECT VECTOR RENDERING HELPERS (ViewBox 128x128 -> Screen)
    // =========================================================================
    private static float sx(float cx, float svgX, float s) {
        return cx + (svgX - 64f) * s;
    }

    private static float sy(float cy, float svgY, float s) {
        return cy + (64f - svgY) * s;
    }

    private static void fillTri(ShapeRenderer sr, float cx, float cy, float s, float x1, float y1, float x2, float y2, float x3, float y3) {
        sr.triangle(sx(cx, x1, s), sy(cy, y1, s), sx(cx, x2, s), sy(cy, y2, s), sx(cx, x3, s), sy(cy, y3, s));
    }

    private static void fillQuad(ShapeRenderer sr, float cx, float cy, float s, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        fillTri(sr, cx, cy, s, x1, y1, x2, y2, x3, y3);
        fillTri(sr, cx, cy, s, x1, y1, x3, y3, x4, y4);
    }

    private static void drawLine(ShapeRenderer sr, float cx, float cy, float s, float x1, float y1, float x2, float y2, float width) {
        sr.rectLine(sx(cx, x1, s), sy(cy, y1, s), sx(cx, x2, s), sy(cy, y2, s), Math.max(1f, width * s));
    }

    private static void fillCircle(ShapeRenderer sr, float cx, float cy, float s, float svgX, float svgY, float r) {
        sr.circle(sx(cx, svgX, s), sy(cy, svgY, s), Math.max(0.8f, r * s));
    }

    // =========================================================================
    // 1. TẬP SỰ SET
    // =========================================================================
    public static void drawTapSuRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Warm sunlit rustic aura (Golden amber / fresh bamboo light)
        sr.setColor(0.92f, 0.78f, 0.42f, 0.22f);
        sr.circle(cx, cy, r * 1.30f);
        sr.setColor(0.45f, 0.80f, 0.35f, 0.15f);
        sr.circle(cx, cy, r * 1.05f);

        // 2. Main Bamboo Pole with natural gentle curve (Aged golden yellow culm)
        float startX = cx - r * 0.78f;
        float startY = cy - r * 0.78f;
        float endX = cx + r * 0.65f;
        float endY = cy + r * 0.65f;

        // Dark underlying core shadow
        sr.setColor(0.35f, 0.28f, 0.10f, 1f);
        sr.rectLine(startX, startY, endX, endY, Math.max(3.8f, r * 0.14f));

        // Golden aged bamboo body
        sr.setColor(0.82f, 0.72f, 0.32f, 1f);
        sr.rectLine(startX, startY, endX, endY, Math.max(2.6f, r * 0.095f));

        // Bamboo top highlight reflection
        sr.setColor(0.96f, 0.90f, 0.55f, 0.85f);
        sr.rectLine(startX + 0.8f, startY + 0.8f, endX, endY, Math.max(1.2f, r * 0.045f));

        // 3. Bamboo Segment Nodes & Dark Annular Rings (Các lóng và đốt tre già)
        float[] nodeFracs = { 0.15f, 0.38f, 0.60f, 0.80f };
        for (float nf : nodeFracs) {
            float nx = startX + (endX - startX) * nf;
            float ny = startY + (endY - startY) * nf;

            // Swollen node ring base
            sr.setColor(0.48f, 0.36f, 0.12f, 1f);
            sr.circle(nx, ny, Math.max(2.2f, r * 0.08f));
            // Dark ridge line across node
            sr.setColor(0.25f, 0.18f, 0.06f, 1f);
            sr.rectLine(nx - r * 0.06f, ny + r * 0.06f, nx + r * 0.06f, ny - r * 0.06f, Math.max(1.5f, r * 0.05f));
            // Bright node rim highlight
            sr.setColor(0.95f, 0.85f, 0.45f, 0.9f);
            sr.circle(nx - r * 0.02f, ny - r * 0.02f, Math.max(1.2f, r * 0.04f));
        }

        // 4. Traditional Hemp Twine Wrapping on Handle (Bọc dây gai bện quấn tay cầm)
        float handleEnd = 0.32f;
        float hx1 = startX;
        float hy1 = startY;
        float hx2 = startX + (endX - startX) * handleEnd;
        float hy2 = startY + (endY - startY) * handleEnd;

        // Dark under-grip
        sr.setColor(0.38f, 0.26f, 0.14f, 1f);
        sr.rectLine(hx1, hy1, hx2, hy2, Math.max(4.6f, r * 0.17f));

        // Cross-coiled hemp rope bindings
        for (int i = 0; i <= 6; i++) {
            float hf = (float) i / 6f;
            float px = hx1 + (hx2 - hx1) * hf;
            float py = hy1 + (hy2 - hy1) * hf;
            sr.setColor((i % 2 == 0) ? new Color(0.68f, 0.52f, 0.30f, 1f) : new Color(0.55f, 0.40f, 0.22f, 1f));
            sr.rectLine(px - r * 0.07f, py + r * 0.07f, px + r * 0.07f, py - r * 0.07f, Math.max(1.8f, r * 0.065f));
        }

        // Carved wooden butt cap pommel at handle base (Chuôi gỗ tiện mộc mạc)
        sr.setColor(0.28f, 0.18f, 0.08f, 1f);
        sr.circle(startX, startY, Math.max(3.8f, r * 0.15f));
        sr.setColor(0.60f, 0.44f, 0.24f, 1f);
        sr.circle(startX, startY, Math.max(2.6f, r * 0.10f));
        sr.setColor(0.88f, 0.72f, 0.42f, 1f);
        sr.circle(startX - r * 0.03f, startY + r * 0.03f, Math.max(1.2f, r * 0.045f));

        // 5. Brass Line Guides tied with Red Thread (Khoen đồng buộc chỉ đỏ truyền thống)
        float[] guideFracs = { 0.48f, 0.70f, 0.90f };
        for (float gf : guideFracs) {
            float gx = startX + (endX - startX) * gf;
            float gy = startY + (endY - startY) * gf;

            // Red silk thread wrap collar (Chỉ đỏ buộc khoen)
            sr.setColor(0.85f, 0.18f, 0.15f, 1f);
            sr.rectLine(gx - r * 0.04f, gy - r * 0.04f, gx + r * 0.04f, gy + r * 0.04f, Math.max(3.4f, r * 0.12f));

            // Brass guide leg & ring (Khoen đồng thau)
            float rx = gx - r * 0.09f;
            float ry = gy + r * 0.09f;
            sr.setColor(0.78f, 0.65f, 0.25f, 1f);
            sr.rectLine(gx, gy, rx, ry, Math.max(1.6f, r * 0.06f));
            sr.circle(rx, ry, Math.max(2.2f, r * 0.08f));
            sr.setColor(0.12f, 0.15f, 0.20f, 1f);
            sr.circle(rx, ry, Math.max(1.2f, r * 0.04f));
        }

        // 6. Flexible Tip with Fresh Bamboo Sprout (Chồi lộc tre xanh biếc đầy sức sống)
        float tipX = cx + r * 0.65f;
        float tipY = cy + r * 0.65f;
        // Tip ferrule
        sr.setColor(0.85f, 0.72f, 0.28f, 1f);
        sr.circle(tipX, tipY, Math.max(2.2f, r * 0.08f));

        // Fresh green bamboo leaves sprouting at tip
        sr.setColor(0.28f, 0.75f, 0.25f, 1f);
        sr.triangle(tipX, tipY, tipX + r * 0.32f, tipY + r * 0.18f, tipX + r * 0.14f, tipY + r * 0.28f);
        sr.setColor(0.42f, 0.90f, 0.35f, 1f);
        sr.triangle(tipX, tipY, tipX + r * 0.20f, tipY + r * 0.35f, tipX + r * 0.05f, tipY + r * 0.30f);
        // Central leaf vein
        sr.setColor(0.85f, 0.98f, 0.60f, 0.9f);
        sr.rectLine(tipX, tipY, tipX + r * 0.24f, tipY + r * 0.22f, 1f);

        // 7. Looping Monofilament Line & Nostalgic Red-White Cork Float (Dây cước phao xốp đỏ-trắng)
        float floatX = cx + r * 0.35f;
        float floatY = cy - r * 0.12f;

        // Fine fishing line drooping from tip
        sr.setColor(0.88f, 0.94f, 1f, 0.75f);
        sr.rectLine(tipX, tipY, floatX, floatY + r * 0.16f, 1.2f);
        sr.rectLine(floatX, floatY - r * 0.16f, floatX - r * 0.15f, floatY - r * 0.45f, 1.2f);

        // Oval cork bobber (Phao bầu dục đỏ trắng mộc mạc)
        float fW = Math.max(4.5f, r * 0.16f);
        float fH = Math.max(7.5f, r * 0.28f);
        // Upper red half
        sr.setColor(0.92f, 0.18f, 0.15f, 1f);
        sr.ellipse(floatX - fW / 2f, floatY, fW, fH * 0.5f);
        // Lower white/cork half
        sr.setColor(0.95f, 0.95f, 0.92f, 1f);
        sr.ellipse(floatX - fW / 2f, floatY - fH * 0.5f, fW, fH * 0.5f);
        // Black dividing center ring & stem
        sr.setColor(0.20f, 0.15f, 0.10f, 1f);
        sr.rect(floatX - fW / 2f, floatY - 1f, fW, 2f);
        sr.rectLine(floatX, floatY + fH * 0.5f, floatX, floatY - fH * 0.5f, 1.2f);
    }

    public static void drawTapSuLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Serene cyan-amber rustic aura
        sr.setColor(0.40f, 0.75f, 0.95f, 0.20f);
        sr.circle(cx, cy, r * 1.30f);
        sr.setColor(0.85f, 0.72f, 0.40f, 0.15f);
        sr.circle(cx, cy, r * 1.05f);

        // 2. Heavy Natural Hardwood Spool Frame (Vành cuộn gỗ mộc hạt dẻ)
        // Outer wood flange shadow
        sr.setColor(0.24f, 0.15f, 0.08f, 1f);
        sr.circle(cx, cy, r * 0.90f);
        // Rich walnut/rosewood body
        sr.setColor(0.48f, 0.32f, 0.18f, 1f);
        sr.circle(cx, cy, r * 0.85f);
        // Golden wood-grain ring
        sr.setColor(0.68f, 0.48f, 0.28f, 0.9f);
        sr.circle(cx, cy, r * 0.80f);
        sr.setColor(0.38f, 0.24f, 0.12f, 1f);
        sr.circle(cx, cy, r * 0.75f);

        // 3. Multilayered Glistening Monofilament Nylon Line (Lớp cuộn cước trong suốt phản quang)
        // Dark spool well
        sr.setColor(0.18f, 0.28f, 0.40f, 1f);
        sr.circle(cx, cy, r * 0.72f);
        // Deep blue-cyan transparent monofilament body
        sr.setColor(0.32f, 0.65f, 0.88f, 0.95f);
        sr.circle(cx, cy, r * 0.68f);
        // Bright translucent mono layer with light penetration
        sr.setColor(0.65f, 0.88f, 1f, 0.90f);
        sr.circle(cx, cy, r * 0.60f);

        // Concentric winding ridges (Vòng gân các sợi cước quấn khít)
        sr.setColor(0.85f, 0.95f, 1f, 0.70f);
        for (int i = 0; i < 4; i++) {
            float rr = r * (0.42f + i * 0.065f);
            sr.circle(cx, cy, rr);
        }
        sr.setColor(0.40f, 0.70f, 0.92f, 1f);
        sr.circle(cx, cy, r * 0.38f);

        // 4. Center Spool Hub & Vintage Parchment Label (Nhãn giấy mộc cổ điển & khuy đồng)
        // Dark inner well
        sr.setColor(0.20f, 0.14f, 0.08f, 1f);
        sr.circle(cx, cy, r * 0.35f);
        // Aged vintage cream paper label
        sr.setColor(0.94f, 0.90f, 0.78f, 1f);
        sr.circle(cx, cy, r * 0.30f);
        // Inner gold seal rim
        sr.setColor(0.82f, 0.68f, 0.25f, 1f);
        sr.circle(cx, cy, r * 0.25f);
        // Center brass spindle axle pin (Trục đồng thau)
        sr.setColor(0.30f, 0.22f, 0.08f, 1f);
        sr.circle(cx, cy, r * 0.16f);
        sr.setColor(0.92f, 0.78f, 0.32f, 1f);
        sr.circle(cx, cy, r * 0.11f);
        sr.setColor(1f, 0.95f, 0.70f, 1f);
        sr.circle(cx - r * 0.03f, cy + r * 0.03f, r * 0.045f);

        // 5. Loose Leader Line, Figure-Eight Loop & Orange Rubber Stopper Bead (Đầu dây cước bung ra & hạt chặn)
        float lx1 = cx + r * 0.55f;
        float ly1 = cy + r * 0.45f;
        float lx2 = cx + r * 0.88f;
        float ly2 = cy + r * 0.68f;
        float lx3 = cx + r * 0.98f;
        float ly3 = cy + r * 0.35f;

        // Flying monofilament strand
        sr.setColor(1f, 1f, 1f, 0.92f);
        sr.rectLine(lx1, ly1, lx2, ly2, Math.max(1.8f, r * 0.07f));
        sr.rectLine(lx2, ly2, lx3, ly3, Math.max(1.8f, r * 0.07f));

        // Terminal tied loop
        sr.circle(lx3, ly3, Math.max(3.0f, r * 0.11f));
        sr.setColor(0.08f, 0.12f, 0.18f, 1f);
        sr.circle(lx3, ly3, Math.max(1.5f, r * 0.055f));

        // Bright Orange Rubber Bobber Stopper Bead (Hạt chặn phao cao su màu cam rực rỡ)
        sr.setColor(0.98f, 0.42f, 0.10f, 1f);
        sr.circle(lx2 - r * 0.05f, ly2 - r * 0.03f, Math.max(3.5f, r * 0.13f));
        sr.setColor(1f, 0.75f, 0.35f, 1f);
        sr.circle(lx2 - r * 0.07f, ly2 - r * 0.01f, Math.max(1.5f, r * 0.055f));
    }

    public static void drawTapSuHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Subtle silver-amber aura
        sr.setColor(0.70f, 0.75f, 0.82f, 0.20f);
        sr.circle(cx, cy, r * 1.30f);
        sr.setColor(0.85f, 0.65f, 0.25f, 0.14f);
        sr.circle(cx, cy, r * 1.05f);

        // Hook geometry points
        float eyeX = cx - r * 0.28f;
        float eyeY = cy + r * 0.68f;
        float shankBottomX = cx - r * 0.28f;
        float shankBottomY = cy - r * 0.35f;
        float bendApexX = cx + r * 0.15f;
        float bendApexY = cy - r * 0.55f;
        float barbX = cx + r * 0.42f;
        float barbY = cy - r * 0.05f;
        float tipX = cx + r * 0.38f;
        float tipY = cy + r * 0.22f;

        // 2. High-Carbon Tempered Steel Hook Body (Thân lưỡi câu thép đen tôi nguội)
        // Under-shadow / heavy metallic gauge
        sr.setColor(0.12f, 0.14f, 0.18f, 1f);
        sr.rectLine(eyeX, eyeY, shankBottomX, shankBottomY, Math.max(4.2f, r * 0.16f));
        sr.rectLine(shankBottomX, shankBottomY, bendApexX, bendApexY, Math.max(4.2f, r * 0.16f));
        sr.rectLine(bendApexX, bendApexY, barbX, barbY, Math.max(4.2f, r * 0.16f));
        sr.rectLine(barbX, barbY, tipX, tipY, Math.max(3.2f, r * 0.12f));

        // Dark gunmetal polished steel
        sr.setColor(0.38f, 0.42f, 0.48f, 1f);
        sr.rectLine(eyeX, eyeY, shankBottomX, shankBottomY, Math.max(2.6f, r * 0.10f));
        sr.rectLine(shankBottomX, shankBottomY, bendApexX, bendApexY, Math.max(2.6f, r * 0.10f));
        sr.rectLine(bendApexX, bendApexY, barbX, barbY, Math.max(2.6f, r * 0.10f));
        sr.rectLine(barbX, barbY, tipX, tipY, Math.max(2.0f, r * 0.075f));

        // Bright steel edge specular highlight
        sr.setColor(0.85f, 0.90f, 0.98f, 0.9f);
        sr.rectLine(eyeX + 0.8f, eyeY, shankBottomX + 0.8f, shankBottomY, Math.max(1.2f, r * 0.04f));
        sr.rectLine(shankBottomX, shankBottomY - 0.8f, bendApexX, bendApexY - 0.8f, Math.max(1.2f, r * 0.04f));

        // Forged eyelet loop at top of shank (Khoen xỏ cước đỉnh cán)
        sr.setColor(0.18f, 0.22f, 0.28f, 1f);
        sr.circle(eyeX, eyeY, Math.max(4.0f, r * 0.16f));
        sr.setColor(0.55f, 0.60f, 0.68f, 1f);
        sr.circle(eyeX, eyeY, Math.max(2.8f, r * 0.11f));
        sr.setColor(0.08f, 0.10f, 0.14f, 1f);
        sr.circle(eyeX, eyeY, Math.max(1.5f, r * 0.06f));

        // Razor-sharp needle point & barb (Ngạnh câu & mũi nhọn)
        sr.setColor(0.92f, 0.95f, 1f, 1f);
        sr.triangle(tipX, tipY, tipX - r * 0.08f, tipY - r * 0.18f, tipX + r * 0.06f, tipY - r * 0.18f);
        // Backward barb fang (Ngạnh câu giữ mồi)
        sr.setColor(0.35f, 0.40f, 0.46f, 1f);
        sr.triangle(barbX, barbY + r * 0.04f, barbX - r * 0.16f, barbY - r * 0.10f, barbX + r * 0.02f, barbY - r * 0.10f);
        sr.setColor(0.85f, 0.92f, 1f, 1f);
        sr.rectLine(barbX, barbY + r * 0.04f, barbX - r * 0.16f, barbY - r * 0.10f, 1f);

        // 3. Realistic Metallic Split-Shot Lead Sinker clamped onto shank (Viên chì kẹp xẻ rãnh kim loại)
        float leadCX = eyeX;
        float leadCY = cy + r * 0.32f;
        float leadR = Math.max(5.5f, r * 0.24f);

        // Outer lead drop shadow
        sr.setColor(0.15f, 0.17f, 0.20f, 1f);
        sr.circle(leadCX, leadCY, leadR);
        // Heavy gray lead body
        sr.setColor(0.44f, 0.48f, 0.54f, 1f);
        sr.circle(leadCX, leadCY, leadR * 0.90f);
        // Clamp split groove (Rãnh kẹp chì)
        sr.setColor(0.18f, 0.20f, 0.24f, 1f);
        sr.rectLine(leadCX - leadR * 0.85f, leadCY, leadCX + leadR * 0.85f, leadCY, Math.max(1.8f, r * 0.07f));
        // Soft metallic sheen highlight
        sr.setColor(0.78f, 0.84f, 0.90f, 0.85f);
        sr.circle(leadCX - leadR * 0.28f, leadCY + leadR * 0.32f, leadR * 0.35f);
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(leadCX - leadR * 0.28f, leadCY + leadR * 0.32f, leadR * 0.15f);

        // 4. Wriggling Red Earthworm Bait hooked on the bend (Chú giun đất đỏ tươi móc mồi sống động)
        float w1X = bendApexX + r * 0.12f;
        float w1Y = bendApexY + r * 0.08f;
        float w2X = bendApexX + r * 0.28f;
        float w2Y = bendApexY - r * 0.12f;
        float w3X = bendApexX + r * 0.45f;
        float w3Y = bendApexY - r * 0.02f;

        // Worm fleshy red-crimson body
        sr.setColor(0.82f, 0.22f, 0.20f, 1f);
        sr.rectLine(w1X, w1Y, w2X, w2Y, Math.max(3.2f, r * 0.12f));
        sr.rectLine(w2X, w2Y, w3X, w3Y, Math.max(2.8f, r * 0.10f));
        sr.circle(w3X, w3Y, Math.max(2.2f, r * 0.08f));
        // Worm segments & highlight
        sr.setColor(0.96f, 0.48f, 0.42f, 0.9f);
        sr.circle(w1X, w1Y, Math.max(1.6f, r * 0.06f));
        sr.circle(w2X, w2Y, Math.max(1.6f, r * 0.06f));
        sr.circle(w3X - 1f, w3Y + 1f, Math.max(1.0f, r * 0.04f));
    }

    // =========================================================================
    // 2. THỢ SĂN SET (Ambush Predator Harpoon Rod, Zinc-Silk Wire Spool, Sea Beast Talon Hook)
    // =========================================================================
    public static void drawThoSanRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Predatory Camo Aura
        sr.setColor(0.32f, 0.72f, 0.38f, 0.22f);
        sr.circle(cx, cy, r * 1.32f);
        sr.setColor(0.70f, 0.55f, 0.20f, 0.15f);
        sr.circle(cx, cy, r * 1.05f);

        // 2. Heavy Wrought-Iron Reinforced Core Shaft
        sr.setColor(0.14f, 0.18f, 0.12f, 1f);
        sr.rectLine(cx - r * 0.78f, cy - r * 0.78f, cx + r * 0.65f, cy + r * 0.65f, Math.max(4.2f, r * 0.15f));

        // 3. Dense Moss-Green & Hardened Ironwood Shaft
        sr.setColor(0.32f, 0.44f, 0.22f, 1f);
        sr.rectLine(cx - r * 0.76f, cy - r * 0.76f, cx + r * 0.62f, cy + r * 0.62f, Math.max(2.8f, r * 0.10f));

        // 4. Rawhide Leather Cross-Bindings & Gold-Bronze Ferrule Rings
        sr.setColor(0.58f, 0.36f, 0.16f, 1f);
        sr.rectLine(cx - r * 0.48f, cy - r * 0.42f, cx - r * 0.42f, cy - r * 0.48f, Math.max(2.2f, r * 0.08f));
        sr.rectLine(cx - r * 0.42f, cy - r * 0.48f, cx - r * 0.48f, cy - r * 0.42f, Math.max(2.2f, r * 0.08f));
        sr.rectLine(cx - r * 0.18f, cy - r * 0.12f, cx - r * 0.12f, cy - r * 0.18f, Math.max(2.2f, r * 0.08f));
        sr.rectLine(cx - r * 0.12f, cy - r * 0.18f, cx - r * 0.18f, cy - r * 0.12f, Math.max(2.2f, r * 0.08f));
        sr.rectLine(cx + r * 0.12f, cy + r * 0.18f, cx + r * 0.18f, cy + r * 0.12f, Math.max(2.2f, r * 0.08f));

        // Polished brass joint rings
        sr.setColor(0.88f, 0.74f, 0.28f, 1f);
        sr.circle(cx - r * 0.55f, cy - r * 0.55f, Math.max(2f, r * 0.08f));
        sr.circle(cx - r * 0.25f, cy - r * 0.25f, Math.max(2f, r * 0.08f));
        sr.circle(cx + r * 0.05f, cy + r * 0.05f, Math.max(2f, r * 0.08f));
        sr.circle(cx + r * 0.35f, cy + r * 0.35f, Math.max(2f, r * 0.08f));

        // 5. Heavy Beast Fang Pommel & Amber Tiger-Eye
        sr.setColor(0.24f, 0.28f, 0.22f, 1f);
        sr.circle(cx - r * 0.78f, cy - r * 0.78f, Math.max(4.5f, r * 0.20f));
        sr.setColor(0.85f, 0.70f, 0.25f, 1f);
        sr.circle(cx - r * 0.78f, cy - r * 0.78f, Math.max(3.2f, r * 0.14f));
        // Amber Eye Core
        sr.setColor(1f, 0.72f, 0.12f, 1f);
        sr.circle(cx - r * 0.78f, cy - r * 0.78f, Math.max(2f, r * 0.09f));
        // Vertical slit pupil
        sr.setColor(0.12f, 0.12f, 0.12f, 1f);
        sr.rectLine(cx - r * 0.78f, cy - r * 0.82f, cx - r * 0.78f, cy - r * 0.74f, 1.2f);

        // 6. 3 Hunter Ceramic Line Guides (Khoen dẫn dây ngọc bích)
        for (int i = 0; i < 3; i++) {
            float gx = cx - r * 0.15f + i * r * 0.25f;
            float gy = cy - r * 0.15f + i * r * 0.25f;
            sr.setColor(0.75f, 0.65f, 0.25f, 1f);
            sr.rectLine(gx, gy, gx - r * 0.08f, gy + r * 0.08f, 1.8f);
            sr.setColor(0.35f, 0.90f, 0.50f, 1f);
            sr.circle(gx - r * 0.08f, gy + r * 0.08f, Math.max(1.5f, r * 0.07f));
        }

        // 7. Massive Damascus Harpoon Spearhead (Mũi phóng lao Damascus ngạnh kép)
        float tipBaseX = cx + r * 0.52f;
        float tipBaseY = cy + r * 0.52f;
        float spearTipX = cx + r * 0.98f;
        float spearTipY = cy + r * 0.98f;

        // Dark forged steel spear collar
        sr.setColor(0.20f, 0.25f, 0.20f, 1f);
        sr.circle(tipBaseX, tipBaseY, Math.max(3.5f, r * 0.14f));

        // Lateral twin predator barbs (Ngạnh móc xé thịt kép)
        sr.setColor(0.68f, 0.74f, 0.70f, 1f);
        sr.triangle(tipBaseX, tipBaseY, cx + r * 0.72f, cy + r * 0.44f, cx + r * 0.60f, cy + r * 0.60f);
        sr.triangle(tipBaseX, tipBaseY, cx + r * 0.44f, cy + r * 0.72f, cx + r * 0.60f, cy + r * 0.60f);

        // Main spearhead blade body (Damascus layered steel)
        sr.setColor(0.30f, 0.36f, 0.32f, 1f);
        sr.triangle(cx + r * 0.58f, cy + r * 0.58f, cx + r * 0.68f, cy + r * 0.88f, spearTipX, spearTipY);
        sr.triangle(cx + r * 0.58f, cy + r * 0.58f, cx + r * 0.88f, cy + r * 0.68f, spearTipX, spearTipY);

        // Silver-etched sharp blade facet
        sr.setColor(0.88f, 0.92f, 0.88f, 1f);
        sr.triangle(cx + r * 0.66f, cy + r * 0.66f, cx + r * 0.74f, cy + r * 0.84f, spearTipX, spearTipY);

        // Emerald venom groove (Rãnh độc xanh ngọc sắc sảo)
        sr.setColor(0.35f, 1.0f, 0.55f, 0.95f);
        sr.rectLine(cx + r * 0.62f, cy + r * 0.62f, cx + r * 0.88f, cy + r * 0.88f, Math.max(1.8f, r * 0.06f));

        // Specular glint on spearhead tip
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(spearTipX, spearTipY, Math.max(1.5f, r * 0.05f));
    }

    public static void drawThoSanLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. High-Tensile Zinc Aura
        sr.setColor(0.32f, 0.72f, 0.40f, 0.20f);
        sr.circle(cx, cy, r * 1.30f);
        sr.setColor(0.60f, 0.70f, 0.80f, 0.15f);
        sr.circle(cx, cy, r * 1.08f);

        // 2. Heavy Forged Iron Beast-Trap Rotor Frame (Khung lồng sắt tôi nguội 6 chấu ngàm)
        sr.setColor(0.16f, 0.20f, 0.18f, 1f);
        sr.circle(cx, cy, r * 0.92f);
        // Outer bronze gear rim with notches
        sr.setColor(0.72f, 0.60f, 0.24f, 1f);
        sr.circle(cx, cy, r * 0.84f);
        sr.setColor(0.20f, 0.25f, 0.22f, 1f);
        sr.circle(cx, cy, r * 0.76f);

        // 6 Predator Trap Teeth / Spokes around the rim
        sr.setColor(0.85f, 0.75f, 0.32f, 1f);
        for (int i = 0; i < 6; i++) {
            float ang = i * 60f * MathUtils.degreesToRadians;
            float cos = MathUtils.cos(ang);
            float sin = MathUtils.sin(ang);
            float tx = cx + cos * r * 0.80f;
            float ty = cy + sin * r * 0.80f;
            sr.triangle(tx, ty, tx - sin * r * 0.08f, ty + cos * r * 0.08f, cx + cos * r * 0.95f, cy + sin * r * 0.95f);
        }

        // 3. Deep Spool Well & Braided Zinc-Silk Wire (Cuộn dây cáp tơ kẽm xoắn titan)
        sr.setColor(0.10f, 0.13f, 0.12f, 1f);
        sr.circle(cx, cy, r * 0.70f);

        // Concentric tightly coiled metallic wire layers
        sr.setColor(0.48f, 0.56f, 0.62f, 1f);
        sr.circle(cx, cy, r * 0.65f);
        sr.setColor(0.70f, 0.78f, 0.84f, 1f);
        sr.circle(cx, cy, r * 0.54f);
        sr.setColor(0.35f, 0.42f, 0.48f, 1f);
        sr.circle(cx, cy, r * 0.44f);
        sr.setColor(0.82f, 0.88f, 0.92f, 1f);
        sr.circle(cx, cy, r * 0.35f);

        // Braided metallic cable texture lines (Các vòng xoắn tơ kẽm chéo)
        sr.setColor(0.25f, 0.32f, 0.38f, 0.90f);
        for (int i = 0; i < 8; i++) {
            float ang = (i * 45f + 22.5f) * MathUtils.degreesToRadians;
            float cos = MathUtils.cos(ang);
            float sin = MathUtils.sin(ang);
            sr.rectLine(cx + cos * r * 0.36f, cy + sin * r * 0.36f, cx + cos * r * 0.64f, cy + sin * r * 0.64f, 1.4f);
        }

        // 4. Center Octagonal Drag Hub with Tiger-Eye Jewel (Trục hãm ngọc mắt hổ)
        sr.setColor(0.18f, 0.22f, 0.18f, 1f);
        sr.circle(cx, cy, r * 0.26f);
        sr.setColor(0.88f, 0.72f, 0.25f, 1f);
        sr.circle(cx, cy, r * 0.18f);
        // Tiger eye core
        sr.setColor(1.0f, 0.75f, 0.15f, 1f);
        sr.circle(cx, cy, r * 0.11f);
        sr.setColor(0.10f, 0.10f, 0.10f, 1f);
        sr.rectLine(cx, cy - r * 0.10f, cx, cy + r * 0.10f, 1.2f);
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(cx - r * 0.04f, cy + r * 0.04f, 1.2f);

        // 5. Heavy Drag Bail Arm & Outgoing Tense Cable (Càng tời & sợi tơ kẽm phóng ra)
        sr.setColor(0.75f, 0.62f, 0.22f, 1f);
        sr.rectLine(cx + r * 0.55f, cy - r * 0.25f, cx + r * 0.85f, cy + r * 0.45f, Math.max(2.5f, r * 0.08f));
        // Emerald roller bearing
        sr.setColor(0.35f, 0.95f, 0.55f, 1f);
        sr.circle(cx + r * 0.85f, cy + r * 0.45f, Math.max(2.8f, r * 0.10f));
        // High-tensile line streaming out with electric spark
        sr.setColor(0.85f, 0.92f, 0.98f, 1f);
        sr.rectLine(cx + r * 0.85f, cy + r * 0.45f, cx + r * 1.15f, cy + r * 0.95f, Math.max(1.8f, r * 0.06f));
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + r * 1.0f, cy + r * 0.70f, 1.5f);
    }

    public static void drawThoSanHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        // 1. Primal Beast Predator Aura
        sr.setColor(0.13f, 0.77f, 0.37f, 0.16f);
        fillCircle(sr, cx, cy, s, 64, 64, 54);
        sr.setColor(0.71f, 0.33f, 0.04f, 0.15f);
        fillCircle(sr, cx, cy, s, 64, 64, 42);

        // 2. Heavy Primal Bone Socket Mount & Eyelet (Top-Left)
        // Iron bone socket clamp (polygon 32,24 54,20 62,34 38,40)
        sr.setColor(0.08f, 0.15f, 0.09f, 1f);
        fillQuad(sr, cx, cy, s, 32, 24, 54, 20, 62, 34, 38, 40);
        sr.setColor(0.85f, 0.47f, 0.02f, 1f);
        drawLine(sr, cx, cy, s, 32, 24, 54, 20, 1.8f);
        drawLine(sr, cx, cy, s, 54, 20, 62, 34, 1.8f);
        drawLine(sr, cx, cy, s, 62, 34, 38, 40, 1.8f);
        drawLine(sr, cx, cy, s, 38, 40, 32, 24, 1.8f);

        // Eyelet circle at (44, 28)
        sr.setColor(0.01f, 0.02f, 0.09f, 1f);
        fillCircle(sr, cx, cy, s, 44, 28, 4.0f);
        sr.setColor(0.98f, 0.75f, 0.14f, 1f);
        fillCircle(sr, cx, cy, s, 44, 28, 2.5f);
        sr.setColor(0.01f, 0.02f, 0.09f, 1f);
        fillCircle(sr, cx, cy, s, 44, 28, 1.6f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 43, 27, 1.4f);

        // Rawhide leather criss-cross wraps
        sr.setColor(0.47f, 0.21f, 0.06f, 1f);
        drawLine(sr, cx, cy, s, 36, 24, 48, 38, 2.2f);
        drawLine(sr, cx, cy, s, 44, 22, 56, 36, 2.2f);

        // Dangling Tiger-Eye Fang Charm
        sr.setColor(0.99f, 0.94f, 0.54f, 1f);
        fillTri(sr, cx, cy, s, 56, 36, 60, 52, 64, 36);
        sr.setColor(0.85f, 0.47f, 0.02f, 1f);
        drawLine(sr, cx, cy, s, 56, 36, 60, 52, 1.0f);
        drawLine(sr, cx, cy, s, 60, 52, 64, 36, 1.0f);
        sr.setColor(0.96f, 0.62f, 0.04f, 1f);
        fillCircle(sr, cx, cy, s, 60, 40, 1.8f);

        // 3. Prehistoric Beast Talon Sickle Body (Massive Raptor Sickle-Claw Curve)
        // Dark outer armored spine backing
        sr.setColor(0.08f, 0.15f, 0.09f, 1f);
        drawLine(sr, cx, cy, s, 44, 38, 62, 41, 7.5f);
        drawLine(sr, cx, cy, s, 62, 41, 78, 48, 8.5f);
        drawLine(sr, cx, cy, s, 78, 48, 88, 72, 9.5f);
        drawLine(sr, cx, cy, s, 88, 72, 92, 90, 9.5f);
        drawLine(sr, cx, cy, s, 92, 90, 78, 106, 9.0f);
        drawLine(sr, cx, cy, s, 78, 106, 62, 110, 8.0f);
        drawLine(sr, cx, cy, s, 62, 110, 46, 104, 6.0f);

        // Massive Ivory Fossil Bone Body (Solid filled polygon)
        sr.setColor(0.89f, 0.91f, 0.94f, 1f);
        fillQuad(sr, cx, cy, s, 44, 38, 62, 41, 64, 50, 54, 42);
        fillQuad(sr, cx, cy, s, 62, 41, 78, 48, 76, 62, 64, 50);
        fillQuad(sr, cx, cy, s, 78, 48, 88, 72, 74, 80, 76, 62);
        fillQuad(sr, cx, cy, s, 88, 72, 92, 90, 72, 95, 74, 80);
        fillQuad(sr, cx, cy, s, 92, 90, 78, 106, 62, 102, 72, 95);
        fillQuad(sr, cx, cy, s, 78, 106, 62, 110, 48, 104, 62, 102);
        fillTri(sr, cx, cy, s, 62, 110, 46, 104, 48, 104);

        // Warm Amber Ageing Patina overlay
        sr.setColor(0.99f, 0.95f, 0.78f, 0.85f);
        fillQuad(sr, cx, cy, s, 52, 43, 76, 50, 72, 62, 60, 54);
        fillQuad(sr, cx, cy, s, 76, 50, 82, 72, 72, 80, 72, 62);
        fillQuad(sr, cx, cy, s, 82, 72, 84, 86, 70, 92, 72, 80);
        fillQuad(sr, cx, cy, s, 84, 86, 76, 98, 62, 96, 70, 92);

        // 4. 3 Wicked Prehistoric Serrations on Outer Curve Spine
        sr.setColor(0.08f, 0.15f, 0.09f, 1f);
        fillTri(sr, cx, cy, s, 68, 48, 76, 40, 76, 52);
        fillTri(sr, cx, cy, s, 80, 60, 90, 54, 86, 66);
        fillTri(sr, cx, cy, s, 86, 76, 98, 72, 90, 82);
        sr.setColor(0.85f, 0.47f, 0.02f, 1f);
        drawLine(sr, cx, cy, s, 68, 48, 76, 40, 1.0f);
        drawLine(sr, cx, cy, s, 80, 60, 90, 54, 1.0f);
        drawLine(sr, cx, cy, s, 86, 76, 98, 72, 1.0f);

        // 5. Emerald Venom Blood Fuller Groove
        sr.setColor(0.06f, 0.73f, 0.51f, 1f);
        drawLine(sr, cx, cy, s, 54, 44, 70, 54, 2.4f);
        drawLine(sr, cx, cy, s, 70, 54, 78, 74, 2.4f);
        drawLine(sr, cx, cy, s, 78, 74, 80, 86, 2.2f);
        drawLine(sr, cx, cy, s, 80, 86, 66, 100, 2.0f);
        sr.setColor(0.43f, 0.91f, 0.72f, 1f);
        drawLine(sr, cx, cy, s, 54, 44, 70, 54, 1.1f);
        drawLine(sr, cx, cy, s, 70, 54, 78, 74, 1.1f);
        drawLine(sr, cx, cy, s, 78, 74, 80, 86, 1.0f);
        drawLine(sr, cx, cy, s, 80, 86, 66, 100, 0.9f);

        // 6. Razor-Sharp Incurved Raptor Sickle Tip
        sr.setColor(Color.WHITE);
        fillTri(sr, cx, cy, s, 46, 104, 54, 98, 52, 106);
        fillCircle(sr, cx, cy, s, 47, 104, 2.2f);
        fillCircle(sr, cx, cy, s, 90, 72, 1.4f);
        fillCircle(sr, cx, cy, s, 43, 27, 1.2f);
    }

    // =========================================================================
    // 3. BĂNG GIÁ SET (Glacial Permafrost Scepter, Absolute-Zero Spool, Cryo Hook)
    // =========================================================================
    public static void drawBangGiaRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        // 1. Soft Glacial Blizzard & Celestial Frost Halo Aura
        sr.setColor(0.22f, 0.74f, 0.97f, 0.18f);
        fillCircle(sr, cx, cy, s, 64, 64, 54);
        sr.setColor(0.73f, 0.90f, 0.99f, 0.20f);
        fillCircle(sr, cx, cy, s, 82, 46, 38);

        // 2. Sleek, Smooth Faceted Permafrost Crystal Shaft (NO THORNS, NO SPURS)
        // Deep sapphire inner core spine
        sr.setColor(0.05f, 0.18f, 0.37f, 1f);
        drawLine(sr, cx, cy, s, 24, 104, 86, 42, 6.0f);
        // Radiant glacial cyan crystal body
        sr.setColor(0.01f, 0.52f, 0.78f, 1f);
        drawLine(sr, cx, cy, s, 24, 104, 86, 42, 4.2f);
        // Luminous turquoise crystal channel
        sr.setColor(0.22f, 0.74f, 0.97f, 1f);
        drawLine(sr, cx, cy, s, 24, 104, 86, 42, 2.4f);
        // Smooth pristine ice specular edge
        sr.setColor(0.94f, 0.98f, 1f, 1f);
        drawLine(sr, cx, cy, s, 25, 103, 85, 43, 1.0f);

        // 3. Sleek Platinum Frost Bands with Smooth Embedded Azure Jewels (Flush with rod)
        sr.setColor(0.88f, 0.95f, 1f, 1f);
        drawLine(sr, cx, cy, s, 38, 93, 44, 87, 3.5f);
        drawLine(sr, cx, cy, s, 53, 78, 59, 72, 3.5f);
        drawLine(sr, cx, cy, s, 68, 63, 74, 57, 3.5f);

        sr.setColor(0.22f, 0.74f, 0.97f, 1f);
        fillCircle(sr, cx, cy, s, 41, 90, 1.6f);
        fillCircle(sr, cx, cy, s, 56, 75, 1.6f);
        fillCircle(sr, cx, cy, s, 71, 60, 1.6f);

        // 4. Smooth Ice Crystal Line Guides (Rounded Rings, NO spikes)
        sr.setColor(0.73f, 0.90f, 0.99f, 1f);
        fillCircle(sr, cx, cy, s, 52, 71, 3.0f);
        sr.setColor(0.01f, 0.52f, 0.78f, 1f);
        fillCircle(sr, cx, cy, s, 52, 71, 1.8f);

        sr.setColor(0.73f, 0.90f, 0.99f, 1f);
        fillCircle(sr, cx, cy, s, 67, 56, 2.8f);
        sr.setColor(0.01f, 0.52f, 0.78f, 1f);
        fillCircle(sr, cx, cy, s, 67, 56, 1.7f);

        // 5. Sleek Polished Ice Diamond Pommel (Bottom-Left)
        sr.setColor(0.01f, 0.52f, 0.78f, 1f);
        fillQuad(sr, cx, cy, s, 20, 102, 26, 108, 22, 112, 16, 106);
        sr.setColor(0.73f, 0.90f, 0.99f, 1f);
        drawLine(sr, cx, cy, s, 20, 102, 26, 108, 1.0f);
        drawLine(sr, cx, cy, s, 26, 108, 22, 112, 1.0f);
        drawLine(sr, cx, cy, s, 22, 112, 16, 106, 1.0f);
        drawLine(sr, cx, cy, s, 16, 106, 20, 102, 1.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 21, 107, 1.6f);

        // 6. Crown Headpiece: Majestic 6-Pointed Arctic Snowflake Star (Top-Right)
        sr.setColor(0.22f, 0.74f, 0.97f, 0.25f);
        fillCircle(sr, cx, cy, s, 86, 42, 16.0f);

        // 6 Smooth Symmetrical Crystal Rays
        sr.setColor(0.73f, 0.90f, 0.99f, 1f);
        drawLine(sr, cx, cy, s, 86, 26, 86, 58, 2.2f);
        drawLine(sr, cx, cy, s, 72, 34, 100, 50, 2.2f);
        drawLine(sr, cx, cy, s, 72, 50, 100, 34, 2.2f);

        // Rhombic Star Ray Diamonds
        sr.setColor(0.88f, 0.95f, 1f, 1f);
        fillTri(sr, cx, cy, s, 86, 26, 83, 32, 89, 32);
        fillTri(sr, cx, cy, s, 86, 58, 83, 52, 89, 52);
        fillTri(sr, cx, cy, s, 72, 34, 78, 32, 76, 38);
        fillTri(sr, cx, cy, s, 100, 50, 94, 52, 96, 46);
        fillTri(sr, cx, cy, s, 72, 50, 78, 52, 76, 46);
        fillTri(sr, cx, cy, s, 100, 34, 94, 32, 96, 38);

        // Glowing Central Ice Prism Hexagon & Diamond Core
        sr.setColor(0.01f, 0.52f, 0.78f, 1f);
        fillTri(sr, cx, cy, s, 86, 42, 86, 36, 91, 39);
        fillTri(sr, cx, cy, s, 86, 42, 91, 39, 91, 45);
        fillTri(sr, cx, cy, s, 86, 42, 91, 45, 86, 48);
        fillTri(sr, cx, cy, s, 86, 42, 86, 48, 81, 45);
        fillTri(sr, cx, cy, s, 86, 42, 81, 45, 81, 39);
        fillTri(sr, cx, cy, s, 86, 42, 81, 39, 86, 36);

        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 86, 36, 91, 39, 1.2f);
        drawLine(sr, cx, cy, s, 91, 39, 91, 45, 1.2f);
        drawLine(sr, cx, cy, s, 91, 45, 86, 48, 1.2f);
        drawLine(sr, cx, cy, s, 86, 48, 81, 45, 1.2f);
        drawLine(sr, cx, cy, s, 81, 45, 81, 39, 1.2f);
        drawLine(sr, cx, cy, s, 81, 39, 86, 36, 1.2f);

        sr.setColor(0.22f, 0.74f, 0.97f, 1f);
        fillQuad(sr, cx, cy, s, 86, 38, 89, 42, 86, 46, 83, 42);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 86, 42, 2.5f);

        // Specular Glints
        fillCircle(sr, cx, cy, s, 86, 26, 1.8f);
        fillCircle(sr, cx, cy, s, 100, 34, 1.5f);
        fillCircle(sr, cx, cy, s, 72, 34, 1.5f);
        fillCircle(sr, cx, cy, s, 41, 90, 1.0f);
    }

    public static void drawBangGiaLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        // 1. Absolute-Zero Frost Vortex Aura
        sr.setColor(0.22f, 0.74f, 0.97f, 0.22f);
        fillCircle(sr, cx, cy, s, 64, 64, 54);
        sr.setColor(0.15f, 0.39f, 0.92f, 0.18f);
        fillCircle(sr, cx, cy, s, 64, 64, 44);

        // 2. Hexagonal Frost Crystal Spool Frame with 6 Ice Horns
        // Outer Hexagon: (64,16 106,40 106,88 64,112 22,88 22,40) in #0f172a
        sr.setColor(0.06f, 0.09f, 0.16f, 1f);
        fillTri(sr, cx, cy, s, 64, 64, 64, 16, 106, 40);
        fillTri(sr, cx, cy, s, 64, 64, 106, 40, 106, 88);
        fillTri(sr, cx, cy, s, 64, 64, 106, 88, 64, 112);
        fillTri(sr, cx, cy, s, 64, 64, 64, 112, 22, 88);
        fillTri(sr, cx, cy, s, 64, 64, 22, 88, 22, 40);
        fillTri(sr, cx, cy, s, 64, 64, 22, 40, 64, 16);

        sr.setColor(0.01f, 0.52f, 0.78f, 1f);
        drawLine(sr, cx, cy, s, 64, 16, 106, 40, 2.2f);
        drawLine(sr, cx, cy, s, 106, 40, 106, 88, 2.2f);
        drawLine(sr, cx, cy, s, 106, 88, 64, 112, 2.2f);
        drawLine(sr, cx, cy, s, 64, 112, 22, 88, 2.2f);
        drawLine(sr, cx, cy, s, 22, 88, 22, 40, 2.2f);
        drawLine(sr, cx, cy, s, 22, 40, 64, 16, 2.2f);

        // Inner Hexagon: (64,22 100,43 100,85 64,106 28,85 28,43) in #1e3a8a
        sr.setColor(0.12f, 0.23f, 0.54f, 1f);
        fillTri(sr, cx, cy, s, 64, 64, 64, 22, 100, 43);
        fillTri(sr, cx, cy, s, 64, 64, 100, 43, 100, 85);
        fillTri(sr, cx, cy, s, 64, 64, 100, 85, 64, 106);
        fillTri(sr, cx, cy, s, 64, 64, 64, 106, 28, 85);
        fillTri(sr, cx, cy, s, 64, 64, 28, 85, 28, 43);
        fillTri(sr, cx, cy, s, 64, 64, 28, 43, 64, 22);

        sr.setColor(0.22f, 0.74f, 0.97f, 1f);
        drawLine(sr, cx, cy, s, 64, 22, 100, 43, 1.4f);
        drawLine(sr, cx, cy, s, 100, 43, 100, 85, 1.4f);
        drawLine(sr, cx, cy, s, 100, 85, 64, 106, 1.4f);
        drawLine(sr, cx, cy, s, 64, 106, 28, 85, 1.4f);
        drawLine(sr, cx, cy, s, 28, 85, 28, 43, 1.4f);
        drawLine(sr, cx, cy, s, 28, 43, 64, 22, 1.4f);

        // 6 Radial Ice Crystal Horn Tips
        sr.setColor(0.73f, 0.90f, 0.99f, 1f);
        fillTri(sr, cx, cy, s, 64, 16, 60, 8, 68, 8);
        fillTri(sr, cx, cy, s, 64, 112, 60, 120, 68, 120);
        fillTri(sr, cx, cy, s, 106, 40, 114, 35, 110, 45);
        fillTri(sr, cx, cy, s, 106, 88, 114, 93, 110, 83);
        fillTri(sr, cx, cy, s, 22, 40, 14, 35, 18, 45);
        fillTri(sr, cx, cy, s, 22, 88, 14, 93, 18, 83);

        // 3. Coiled Superconducting Cryo-Thread
        sr.setColor(0.01f, 0.52f, 0.78f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 34);
        sr.setColor(0.22f, 0.74f, 0.97f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 30);
        sr.setColor(0.49f, 0.83f, 0.99f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 26);

        // Shimmering Crystalline Winding Rings
        sr.setColor(0.88f, 0.95f, 1f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 32.5f);
        sr.setColor(0.22f, 0.74f, 0.97f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 31.3f);

        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 64, 64, 28.5f);
        sr.setColor(0.49f, 0.83f, 0.99f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 27.5f);

        // 4. Center Hexagonal Diamond Ice Core & Snowflake Facets
        sr.setColor(0.01f, 0.41f, 0.63f, 1f);
        fillTri(sr, cx, cy, s, 64, 64, 64, 48, 78, 56);
        fillTri(sr, cx, cy, s, 64, 64, 78, 56, 78, 72);
        fillTri(sr, cx, cy, s, 64, 64, 78, 72, 64, 80);
        fillTri(sr, cx, cy, s, 64, 64, 64, 80, 50, 72);
        fillTri(sr, cx, cy, s, 64, 64, 50, 72, 50, 56);
        fillTri(sr, cx, cy, s, 64, 64, 50, 56, 64, 48);

        sr.setColor(0.73f, 0.90f, 0.99f, 1f);
        drawLine(sr, cx, cy, s, 64, 48, 78, 56, 1.4f);
        drawLine(sr, cx, cy, s, 78, 56, 78, 72, 1.4f);
        drawLine(sr, cx, cy, s, 78, 72, 64, 80, 1.4f);
        drawLine(sr, cx, cy, s, 64, 80, 50, 72, 1.4f);
        drawLine(sr, cx, cy, s, 50, 72, 50, 56, 1.4f);
        drawLine(sr, cx, cy, s, 50, 56, 64, 48, 1.4f);

        // Inner Hexagon Core
        sr.setColor(0.22f, 0.74f, 0.97f, 1f);
        fillTri(sr, cx, cy, s, 64, 64, 64, 52, 74, 58);
        fillTri(sr, cx, cy, s, 64, 64, 74, 58, 74, 70);
        fillTri(sr, cx, cy, s, 64, 64, 74, 70, 64, 76);
        fillTri(sr, cx, cy, s, 64, 64, 64, 76, 54, 70);
        fillTri(sr, cx, cy, s, 64, 64, 54, 70, 54, 58);
        fillTri(sr, cx, cy, s, 64, 64, 54, 58, 64, 52);

        // Center Crystalline Star
        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 64, 56, 64, 72, 1.5f);
        drawLine(sr, cx, cy, s, 57, 60, 71, 68, 1.5f);
        drawLine(sr, cx, cy, s, 57, 68, 71, 60, 1.5f);
        fillCircle(sr, cx, cy, s, 64, 64, 2.5f);

        // 5. Dangling Icicle Thread Strand with Frost Droplet
        sr.setColor(0.88f, 0.95f, 1f, 1f);
        drawLine(sr, cx, cy, s, 88, 48, 106, 40, 1.8f);
        drawLine(sr, cx, cy, s, 106, 40, 110, 24, 1.8f);
        drawLine(sr, cx, cy, s, 110, 24, 98, 16, 1.8f);

        sr.setColor(0.22f, 0.74f, 0.97f, 1f);
        fillTri(sr, cx, cy, s, 98, 16, 94, 10, 102, 12);

        // Pure Diamond Specular Glints
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 98, 16, 1.8f);
        fillCircle(sr, cx, cy, s, 64, 8, 1.5f);
        fillCircle(sr, cx, cy, s, 64, 64, 1.5f);
    }

    public static void drawBangGiaHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        // 1. Glacial Mist & Arctic Blizzard Aura
        sr.setColor(0.22f, 0.74f, 0.97f, 0.20f);
        fillCircle(sr, cx, cy, s, 64, 64, 54);
        sr.setColor(0.75f, 0.86f, 1f, 0.18f);
        fillCircle(sr, cx, cy, s, 64, 64, 42);

        // 2. Hexagonal Cryo Crystal Eyelet (Top-Center)
        // Polygon 64,18 74,24 74,36 64,42 54,36 54,24 in #0c2d5e
        sr.setColor(0.05f, 0.18f, 0.37f, 1f);
        fillTri(sr, cx, cy, s, 64, 30, 64, 18, 74, 24);
        fillTri(sr, cx, cy, s, 64, 30, 74, 24, 74, 36);
        fillTri(sr, cx, cy, s, 64, 30, 74, 36, 64, 42);
        fillTri(sr, cx, cy, s, 64, 30, 64, 42, 54, 36);
        fillTri(sr, cx, cy, s, 64, 30, 54, 36, 54, 24);
        fillTri(sr, cx, cy, s, 64, 30, 54, 24, 64, 18);

        sr.setColor(0.22f, 0.74f, 0.97f, 1f);
        drawLine(sr, cx, cy, s, 64, 18, 74, 24, 1.8f);
        drawLine(sr, cx, cy, s, 74, 24, 74, 36, 1.8f);
        drawLine(sr, cx, cy, s, 74, 36, 64, 42, 1.8f);
        drawLine(sr, cx, cy, s, 64, 42, 54, 36, 1.8f);
        drawLine(sr, cx, cy, s, 54, 36, 54, 24, 1.8f);
        drawLine(sr, cx, cy, s, 54, 24, 64, 18, 1.8f);

        // Inner eye circles
        sr.setColor(0.01f, 0.52f, 0.78f, 1f);
        fillCircle(sr, cx, cy, s, 64, 30, 5.0f);
        sr.setColor(0.73f, 0.90f, 0.99f, 1f);
        drawLine(sr, cx, cy, s, 60, 30, 68, 30, 1.2f);
        sr.setColor(0.06f, 0.09f, 0.16f, 1f);
        fillCircle(sr, cx, cy, s, 64, 30, 2.5f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 62.5f, 28.5f, 1.4f);

        // 3. Faceted Crystalline Ice Anchor Pillar Shank (Centered Vertical)
        sr.setColor(0.05f, 0.18f, 0.37f, 1f);
        fillQuad(sr, cx, cy, s, 61, 42, 67, 42, 69, 76, 59, 76);
        sr.setColor(0.01f, 0.52f, 0.78f, 1f);
        fillQuad(sr, cx, cy, s, 62, 42, 66, 42, 68, 76, 60, 76);
        sr.setColor(0.22f, 0.74f, 0.97f, 1f);
        drawLine(sr, cx, cy, s, 62, 42, 60, 76, 1.0f);
        drawLine(sr, cx, cy, s, 66, 42, 68, 76, 1.0f);
        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 64, 42, 64, 76, 1.6f);

        // 4. Dual Crystalline Frost Anchor Wings & Symmetrical Barbs (Cánh mỏ neo băng giá)
        // Left Frost Wing (Solid filled curved polygon)
        sr.setColor(0.01f, 0.52f, 0.78f, 1f);
        fillQuad(sr, cx, cy, s, 60, 74, 48, 76, 46, 88, 62, 82);
        fillQuad(sr, cx, cy, s, 48, 76, 38, 82, 38, 100, 46, 88);
        fillQuad(sr, cx, cy, s, 38, 82, 30, 94, 28, 101, 38, 100);
        fillTri(sr, cx, cy, s, 46, 88, 38, 100, 50, 95);
        fillTri(sr, cx, cy, s, 46, 88, 50, 95, 62, 82);

        // Left Wing Cyan Stroke Outline
        sr.setColor(0.22f, 0.74f, 0.97f, 1f);
        drawLine(sr, cx, cy, s, 60, 74, 48, 76, 1.5f);
        drawLine(sr, cx, cy, s, 48, 76, 38, 82, 1.5f);
        drawLine(sr, cx, cy, s, 38, 82, 30, 94, 1.5f);
        drawLine(sr, cx, cy, s, 30, 94, 28, 101, 1.5f);
        drawLine(sr, cx, cy, s, 28, 101, 38, 100, 1.5f);
        drawLine(sr, cx, cy, s, 38, 100, 50, 95, 1.5f);
        drawLine(sr, cx, cy, s, 50, 95, 62, 82, 1.5f);

        // Left Razor Barb
        sr.setColor(0.73f, 0.90f, 0.99f, 1f);
        fillTri(sr, cx, cy, s, 30, 94, 22, 86, 34, 88);
        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 30, 94, 22, 86, 0.8f);
        drawLine(sr, cx, cy, s, 22, 86, 34, 88, 0.8f);

        // Left Ice Facet
        sr.setColor(0.49f, 0.83f, 0.99f, 1f);
        fillQuad(sr, cx, cy, s, 58, 76, 44, 79, 44, 90, 58, 82);
        fillTri(sr, cx, cy, s, 44, 79, 34, 92, 44, 90);

        // Right Frost Wing (Exact Symmetrical Mirror across X=64)
        sr.setColor(0.01f, 0.52f, 0.78f, 1f);
        fillQuad(sr, cx, cy, s, 68, 74, 80, 76, 82, 88, 66, 82);
        fillQuad(sr, cx, cy, s, 80, 76, 90, 82, 90, 100, 82, 88);
        fillQuad(sr, cx, cy, s, 90, 82, 98, 94, 100, 101, 90, 100);
        fillTri(sr, cx, cy, s, 82, 88, 90, 100, 78, 95);
        fillTri(sr, cx, cy, s, 82, 88, 78, 95, 66, 82);

        // Right Wing Cyan Stroke Outline
        sr.setColor(0.22f, 0.74f, 0.97f, 1f);
        drawLine(sr, cx, cy, s, 68, 74, 80, 76, 1.5f);
        drawLine(sr, cx, cy, s, 80, 76, 90, 82, 1.5f);
        drawLine(sr, cx, cy, s, 90, 82, 98, 94, 1.5f);
        drawLine(sr, cx, cy, s, 98, 94, 100, 101, 1.5f);
        drawLine(sr, cx, cy, s, 100, 101, 90, 100, 1.5f);
        drawLine(sr, cx, cy, s, 90, 100, 78, 95, 1.5f);
        drawLine(sr, cx, cy, s, 78, 95, 66, 82, 1.5f);

        // Right Razor Barb
        sr.setColor(0.73f, 0.90f, 0.99f, 1f);
        fillTri(sr, cx, cy, s, 98, 94, 106, 86, 94, 88);
        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 98, 94, 106, 86, 0.8f);
        drawLine(sr, cx, cy, s, 106, 86, 94, 88, 0.8f);

        // Right Ice Facet
        sr.setColor(0.49f, 0.83f, 0.99f, 1f);
        fillQuad(sr, cx, cy, s, 70, 76, 84, 79, 84, 90, 70, 82);
        fillTri(sr, cx, cy, s, 84, 79, 94, 92, 84, 90);

        // 5. Central 5-Pointed Snowflake Core Medallion on Shank
        sr.setColor(0.22f, 0.74f, 0.97f, 0.35f);
        fillCircle(sr, cx, cy, s, 64, 62, 10.0f);

        // Star polygon points: 64,54 68,60 74,60 70,64 72,70 64,66 56,70 58,64 54,60 60,60
        sr.setColor(0.88f, 0.95f, 1f, 1f);
        fillTri(sr, cx, cy, s, 64, 62, 64, 54, 68, 60);
        fillTri(sr, cx, cy, s, 64, 62, 68, 60, 74, 60);
        fillTri(sr, cx, cy, s, 64, 62, 74, 60, 70, 64);
        fillTri(sr, cx, cy, s, 64, 62, 70, 64, 72, 70);
        fillTri(sr, cx, cy, s, 64, 62, 72, 70, 64, 66);
        fillTri(sr, cx, cy, s, 64, 62, 64, 66, 56, 70);
        fillTri(sr, cx, cy, s, 64, 62, 56, 70, 58, 64);
        fillTri(sr, cx, cy, s, 64, 62, 58, 64, 54, 60);
        fillTri(sr, cx, cy, s, 64, 62, 54, 60, 60, 60);
        fillTri(sr, cx, cy, s, 64, 62, 60, 60, 64, 54);

        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 64, 54, 68, 60, 0.8f);
        drawLine(sr, cx, cy, s, 68, 60, 74, 60, 0.8f);
        drawLine(sr, cx, cy, s, 74, 60, 70, 64, 0.8f);
        drawLine(sr, cx, cy, s, 70, 64, 72, 70, 0.8f);
        drawLine(sr, cx, cy, s, 72, 70, 64, 66, 0.8f);
        drawLine(sr, cx, cy, s, 64, 66, 56, 70, 0.8f);
        drawLine(sr, cx, cy, s, 56, 70, 58, 64, 0.8f);
        drawLine(sr, cx, cy, s, 58, 64, 54, 60, 0.8f);
        drawLine(sr, cx, cy, s, 54, 60, 60, 60, 0.8f);
        drawLine(sr, cx, cy, s, 60, 60, 64, 54, 0.8f);
        fillCircle(sr, cx, cy, s, 64, 62, 2.5f);

        // 6. Bottom Glacier Point (Chóp nhọn mỏ neo băng đáy)
        sr.setColor(0.01f, 0.52f, 0.78f, 1f);
        fillTri(sr, cx, cy, s, 60, 80, 68, 80, 64, 96);
        sr.setColor(0.22f, 0.74f, 0.97f, 1f);
        drawLine(sr, cx, cy, s, 60, 80, 64, 96, 1.0f);
        drawLine(sr, cx, cy, s, 68, 80, 64, 96, 1.0f);
        sr.setColor(Color.WHITE);
        fillTri(sr, cx, cy, s, 62, 80, 66, 80, 64, 94);

        // 7. Specular Glints
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 22, 86, 2.0f);
        fillCircle(sr, cx, cy, s, 106, 86, 2.0f);
        fillCircle(sr, cx, cy, s, 64, 95, 1.6f);
        fillCircle(sr, cx, cy, s, 64, 62, 1.5f);
    }

    // =========================================================================
    // 4. ĐẠI DƯƠNG SET (OCEANUS / LEVIATHAN)
    // =========================================================================
    public static void drawDaiDuongRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Abyssal Bioluminescent Vortex Aura
        sr.setColor(0.12f, 0.60f, 0.95f, 0.20f);
        sr.circle(cx, cy, r * 0.95f);
        sr.setColor(0.20f, 0.85f, 1f, 0.22f);
        sr.circle(cx + r * 0.40f, cy + r * 0.40f, r * 0.55f);

        // 2. Abyssal Cobalt-Steel & Coral-Gold Scepter Shaft
        float x1 = cx - r * 0.72f, y1 = cy - r * 0.72f;
        float x2 = cx + r * 0.38f, y2 = cy + r * 0.38f;

        // Dark deep-sea cobalt spine
        sr.setColor(0.08f, 0.20f, 0.45f, 1f);
        sr.rectLine(x1, y1, x2, y2, Math.max(4.2f, r * 0.18f));
        // Luminous aquamarine core conduit
        sr.setColor(0.35f, 0.95f, 1f, 0.95f);
        sr.rectLine(x1, y1, x2, y2, Math.max(1.8f, r * 0.08f));

        // Golden spiral sea-coral wraps and node rings along shaft
        sr.setColor(0.95f, 0.80f, 0.25f, 1f);
        for (float f = 0.2f; f <= 0.8f; f += 0.3f) {
            float nx = x1 + (x2 - x1) * f;
            float ny = y1 + (y2 - y1) * f;
            sr.circle(nx, ny, Math.max(2.5f, r * 0.11f));
        }

        // 3. Neptune Wave Trident Crown Headpiece
        float tx = cx + r * 0.50f, ty = cy + r * 0.50f;

        // Center Wave Trident Blade (Pointing northeast)
        sr.setColor(0.25f, 0.80f, 1f, 1f);
        float pCX = tx + r * 0.38f, pCY = ty + r * 0.38f;
        sr.triangle(tx, ty, pCX, pCY, tx + r * 0.22f, ty + r * 0.45f);
        sr.triangle(tx, ty, pCX, pCY, tx + r * 0.45f, ty + r * 0.22f);

        // Left Curved Wave Prong
        float pLX = tx - r * 0.08f + r * 0.38f, pLY = ty + r * 0.25f + r * 0.38f;
        sr.setColor(0.18f, 0.65f, 0.95f, 1f);
        sr.triangle(tx, ty, pLX, pLY, tx + r * 0.05f, ty + r * 0.35f);
        // Left prong barb
        sr.setColor(0.65f, 0.95f, 1f, 1f);
        sr.triangle(pLX, pLY, pLX - r * 0.12f, pLY + r * 0.05f, tx + r * 0.05f, ty + r * 0.35f);

        // Right Curved Wave Prong
        float pRX = tx + r * 0.25f + r * 0.38f, pRY = ty - r * 0.08f + r * 0.38f;
        sr.setColor(0.18f, 0.65f, 0.95f, 1f);
        sr.triangle(tx, ty, pRX, pRY, tx + r * 0.35f, ty + r * 0.05f);
        // Right prong barb
        sr.setColor(0.65f, 0.95f, 1f, 1f);
        sr.triangle(pRX, pRY, pRX + r * 0.05f, pRY - r * 0.12f, tx + r * 0.35f, ty + r * 0.05f);

        // Trident Center Spine Highlight
        sr.setColor(1f, 1f, 1f, 0.9f);
        sr.rectLine(tx, ty, pCX, pCY, 1.8f);

        // Central Ocean Heart Gemstone (Royal Aquamarine Diamond)
        sr.setColor(0.08f, 0.45f, 0.75f, 1f);
        sr.circle(tx, ty, r * 0.22f);
        sr.setColor(0.40f, 0.92f, 1f, 1f);
        sr.circle(tx, ty, r * 0.15f);

        // Iridescent Giant Ocean Pearl Finial at base of trident
        sr.setColor(0.92f, 0.98f, 1f, 1f);
        sr.circle(tx, ty, r * 0.09f);
        sr.setColor(Color.WHITE);
        sr.circle(tx - 1.5f, ty + 1.5f, 2f);
    }

    public static void drawDaiDuongLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Oceanic Abyssal Swirl Aura
        sr.setColor(0.10f, 0.45f, 0.85f, 0.25f);
        sr.circle(cx, cy, r * 0.95f);
        sr.setColor(0.20f, 0.85f, 1f, 0.20f);
        sr.circle(cx, cy, r * 0.80f);

        // 2. Heavy Nautilus Shell & Deep-Sea Bronze Outer Bezel
        sr.setColor(0.85f, 0.68f, 0.25f, 1f);
        sr.circle(cx, cy, r * 0.76f);
        // Wave-carved teeth notches around circumference (8 wave notches)
        for (int w = 0; w < 8; w++) {
            float wAng = w * 45f;
            float wx = cx + com.badlogic.gdx.math.MathUtils.cosDeg(wAng) * (r * 0.76f + 2f);
            float wy = cy + com.badlogic.gdx.math.MathUtils.sinDeg(wAng) * (r * 0.76f + 2f);
            sr.circle(wx, wy, Math.max(2f, r * 0.08f));
        }

        // Inner cobalt rim
        sr.setColor(0.08f, 0.18f, 0.35f, 1f);
        sr.circle(cx, cy, r * 0.66f);

        // 3. Spun Aquamarine Fiber-Optic Glass Filament Thread
        sr.setColor(0.25f, 0.85f, 1f, 0.95f);
        sr.circle(cx, cy, r * 0.54f);
        sr.setColor(0.10f, 0.35f, 0.65f, 1f);
        sr.circle(cx, cy, r * 0.44f);
        sr.setColor(0.65f, 0.98f, 1f, 0.95f);
        sr.circle(cx, cy, r * 0.34f);

        // Optical pulse nodes on thread
        sr.setColor(Color.WHITE);
        sr.circle(cx - r * 0.38f, cy, 2.5f);
        sr.circle(cx + r * 0.38f, cy, 2.5f);
        sr.circle(cx, cy - r * 0.38f, 2.5f);
        sr.circle(cx, cy + r * 0.38f, 2.5f);

        // 4. Abyssal Core with Golden Compass Star & Ocean Pearl
        sr.setColor(0.05f, 0.12f, 0.25f, 1f);
        sr.circle(cx, cy, r * 0.22f);
        // Golden compass star cross
        sr.setColor(1f, 0.88f, 0.35f, 1f);
        sr.rectLine(cx - r * 0.18f, cy, cx + r * 0.18f, cy, 1.8f);
        sr.rectLine(cx, cy - r * 0.18f, cx, cy + r * 0.18f, 1.8f);

        // Central Iridescent Pearl
        sr.setColor(0.90f, 0.98f, 1f, 1f);
        sr.circle(cx, cy, r * 0.10f);
        sr.setColor(Color.WHITE);
        sr.circle(cx - 1f, cy + 1f, 1.5f);
    }

    public static void drawDaiDuongHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        // 1. Deep-Sea Leviathan Jaw & Oceanic Swirl Aura
        sr.setColor(0.02f, 0.71f, 0.83f, 0.20f);
        fillCircle(sr, cx, cy, s, 64, 64, 54);
        sr.setColor(0.01f, 0.41f, 0.63f, 0.18f);
        fillCircle(sr, cx, cy, s, 64, 64, 42);

        // 2. Crown Eyelet: Whale-Tail Fluke / Dolphin Nautical Crest (Top-Left)
        // Fluke wings around (44, 34)
        sr.setColor(0.85f, 0.47f, 0.02f, 1f);
        fillTri(sr, cx, cy, s, 44, 38, 32, 24, 40, 20);
        fillTri(sr, cx, cy, s, 44, 38, 40, 20, 44, 26);
        fillTri(sr, cx, cy, s, 44, 38, 56, 24, 48, 20);
        fillTri(sr, cx, cy, s, 44, 38, 48, 20, 44, 26);

        sr.setColor(0.98f, 0.75f, 0.14f, 1f);
        drawLine(sr, cx, cy, s, 32, 24, 40, 20, 1.2f);
        drawLine(sr, cx, cy, s, 40, 20, 44, 26, 1.2f);
        drawLine(sr, cx, cy, s, 44, 26, 48, 20, 1.2f);
        drawLine(sr, cx, cy, s, 48, 20, 56, 24, 1.2f);
        drawLine(sr, cx, cy, s, 56, 24, 44, 38, 1.2f);
        drawLine(sr, cx, cy, s, 44, 38, 32, 24, 1.2f);

        // Nautical eyelet ring at (44, 34)
        sr.setColor(0.03f, 0.18f, 0.29f, 1f);
        fillCircle(sr, cx, cy, s, 44, 34, 5.0f);
        sr.setColor(0.98f, 0.75f, 0.14f, 1f);
        fillCircle(sr, cx, cy, s, 44, 34, 4.0f);
        sr.setColor(0.01f, 0.52f, 0.78f, 1f);
        fillCircle(sr, cx, cy, s, 44, 34, 2.5f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 42.5f, 32.5f, 1.2f);

        // 3. Ancient Leviathan Jaw Tooth Shank & Sweeping Marine Curve
        // Deep cobalt armor spine (stroke-width 8.5)
        sr.setColor(0.03f, 0.18f, 0.29f, 1f);
        drawLine(sr, cx, cy, s, 44, 38, 44, 78, 8.5f);
        drawLine(sr, cx, cy, s, 44, 78, 52, 98, 8.5f);
        drawLine(sr, cx, cy, s, 52, 98, 72, 106, 8.5f);
        drawLine(sr, cx, cy, s, 72, 106, 92, 98, 8.5f);
        drawLine(sr, cx, cy, s, 92, 98, 98, 76, 8.5f);
        drawLine(sr, cx, cy, s, 98, 76, 96, 54, 8.5f);

        // Ancient Leviathan Ivory Tooth Blade (stroke-width 5.5)
        sr.setColor(0.01f, 0.52f, 0.78f, 1f);
        drawLine(sr, cx, cy, s, 44, 38, 44, 78, 5.5f);
        drawLine(sr, cx, cy, s, 44, 78, 52, 98, 5.5f);
        drawLine(sr, cx, cy, s, 52, 98, 72, 106, 5.5f);
        drawLine(sr, cx, cy, s, 72, 106, 92, 98, 5.5f);
        drawLine(sr, cx, cy, s, 92, 98, 98, 76, 5.5f);
        drawLine(sr, cx, cy, s, 98, 76, 96, 54, 5.5f);

        // Bioluminescent Seafoam Vein (stroke-width 2.2)
        sr.setColor(0.40f, 0.91f, 0.98f, 1f);
        drawLine(sr, cx, cy, s, 44, 38, 44, 78, 2.2f);
        drawLine(sr, cx, cy, s, 44, 78, 52, 98, 2.2f);
        drawLine(sr, cx, cy, s, 52, 98, 72, 106, 2.2f);
        drawLine(sr, cx, cy, s, 72, 106, 92, 98, 2.2f);
        drawLine(sr, cx, cy, s, 92, 98, 98, 76, 2.2f);
        drawLine(sr, cx, cy, s, 98, 76, 96, 54, 2.2f);

        // Specular blade rim
        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 45, 38, 45, 78, 0.9f);
        drawLine(sr, cx, cy, s, 45, 78, 53, 97, 0.9f);
        drawLine(sr, cx, cy, s, 53, 97, 72, 104, 0.9f);
        drawLine(sr, cx, cy, s, 72, 104, 91, 97, 0.9f);
        drawLine(sr, cx, cy, s, 91, 97, 97, 76, 0.9f);
        drawLine(sr, cx, cy, s, 97, 76, 95, 56, 0.9f);

        // 4. 2 Dorsal Shark/Manta Wave Fins protruding backwards on Shank
        sr.setColor(0.03f, 0.18f, 0.29f, 1f);
        fillTri(sr, cx, cy, s, 44, 50, 28, 58, 44, 62);
        fillTri(sr, cx, cy, s, 44, 66, 26, 76, 44, 80);
        sr.setColor(0.22f, 0.74f, 0.97f, 1f);
        drawLine(sr, cx, cy, s, 44, 50, 28, 58, 1.0f);
        drawLine(sr, cx, cy, s, 28, 58, 44, 62, 1.0f);
        drawLine(sr, cx, cy, s, 44, 66, 26, 76, 1.0f);
        drawLine(sr, cx, cy, s, 26, 76, 44, 80, 1.0f);
        sr.setColor(0.40f, 0.91f, 0.98f, 1f);
        fillCircle(sr, cx, cy, s, 28, 58, 1.2f);
        fillCircle(sr, cx, cy, s, 26, 76, 1.2f);

        // 5. Massive Neptune Tidal Trident Wave Harpoon Point (Right)
        // Center Harpoon Spearhead
        sr.setColor(0.01f, 0.52f, 0.78f, 1f);
        fillTri(sr, cx, cy, s, 96, 54, 90, 68, 102, 68);
        sr.setColor(0.40f, 0.91f, 0.98f, 1f);
        drawLine(sr, cx, cy, s, 96, 54, 90, 68, 1.0f);
        drawLine(sr, cx, cy, s, 96, 54, 102, 68, 1.0f);

        // Razor Sharp Ivory Spear Point
        sr.setColor(0.94f, 0.98f, 1f, 1f);
        fillTri(sr, cx, cy, s, 96, 44, 91, 56, 101, 56);

        // Outer Tidal Wave Hook Flukes (Ngạnh sóng kép đối xứng)
        sr.setColor(0.05f, 0.65f, 0.91f, 1f);
        fillTri(sr, cx, cy, s, 96, 66, 86, 78, 96, 70);
        fillTri(sr, cx, cy, s, 96, 66, 84, 68, 86, 78);
        fillTri(sr, cx, cy, s, 96, 66, 106, 78, 96, 70);
        fillTri(sr, cx, cy, s, 96, 66, 108, 68, 106, 78);

        sr.setColor(0.73f, 0.90f, 0.99f, 1f);
        drawLine(sr, cx, cy, s, 96, 66, 84, 68, 0.8f);
        drawLine(sr, cx, cy, s, 84, 68, 86, 78, 0.8f);
        drawLine(sr, cx, cy, s, 96, 66, 108, 68, 0.8f);
        drawLine(sr, cx, cy, s, 108, 68, 106, 78, 0.8f);

        // 6. Dangling Luminescent Angler-Fish Lure Pearl Charm
        sr.setColor(0.98f, 0.75f, 0.14f, 1f);
        drawLine(sr, cx, cy, s, 44, 42, 30, 48, 1.2f);
        sr.setColor(0.97f, 0.98f, 0.99f, 1f);
        fillCircle(sr, cx, cy, s, 28, 49, 4.0f);
        sr.setColor(0.02f, 0.71f, 0.83f, 1f);
        drawLine(sr, cx, cy, s, 25, 49, 31, 49, 1.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 27, 48, 1.5f);

        // 7. Specular Glints
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 96, 44, 2.2f);
        fillCircle(sr, cx, cy, s, 44, 32, 1.4f);
        fillCircle(sr, cx, cy, s, 72, 104, 1.6f);
    }

    // =========================================================================
    // 5. HẢI TẶC SET (Pirate Galleon Mast, Anchor Chain Wheel, Reaper Scythe Hook)
    // =========================================================================
    public static void drawHaiTacRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        // LAYER 1: CURSED TREASURE & GUNPOWDER SMOKE AURA
        sr.setColor(0.96f, 0.62f, 0.04f, 0.22f);
        fillCircle(sr, cx, cy, s, 64, 64, 56);
        sr.setColor(0.85f, 0.40f, 0.12f, 0.15f);
        fillTri(sr, cx, cy, s, 64, 8, 120, 64, 64, 120);
        fillTri(sr, cx, cy, s, 64, 8, 8, 64, 64, 120);

        // LAYER 2: FACET BODY - GALLEON MAST & RIGGING
        // Yardarm Cross-Spar
        sr.setColor(0.27f, 0.15f, 0.08f, 1f);
        fillQuad(sr, cx, cy, s, 26, 82, 30, 80, 50, 100, 46, 102);
        sr.setColor(0.71f, 0.53f, 0.18f, 1f);
        fillCircle(sr, cx, cy, s, 28, 81, 2.5f);
        fillCircle(sr, cx, cy, s, 48, 101, 2.5f);

        // Pirate Storm Lantern
        sr.setColor(0.71f, 0.53f, 0.18f, 1f);
        drawLine(sr, cx, cy, s, 36, 92, 34, 99, 1.5f);
        fillTri(sr, cx, cy, s, 34, 99, 31, 102, 37, 102);
        sr.setColor(0.96f, 0.62f, 0.04f, 1f);
        fillQuad(sr, cx, cy, s, 31, 102, 37, 102, 36, 110, 32, 110);
        sr.setColor(0.99f, 0.94f, 0.54f, 1f);
        fillCircle(sr, cx, cy, s, 34, 106, 2.0f);
        sr.setColor(0.71f, 0.53f, 0.18f, 1f);
        drawLine(sr, cx, cy, s, 34, 102, 34, 110, 1.2f);
        fillQuad(sr, cx, cy, s, 30, 110, 38, 110, 37, 112, 31, 112);

        // Main Weathered Mahogany Mast Spar
        sr.setColor(0.18f, 0.10f, 0.06f, 1f);
        fillQuad(sr, cx, cy, s, 18, 108, 22, 112, 102, 32, 98, 28);
        sr.setColor(0.36f, 0.20f, 0.12f, 1f);
        fillQuad(sr, cx, cy, s, 19, 107, 23, 111, 101, 33, 97, 29);
        sr.setColor(0.57f, 0.33f, 0.18f, 1f);
        drawLine(sr, cx, cy, s, 21, 109, 99, 31, 1.8f);

        // Forged Brass Bands with Golden Rivets
        sr.setColor(0.85f, 0.65f, 0.20f, 1f);
        fillQuad(sr, cx, cy, s, 33, 89, 39, 95, 41, 93, 35, 87);
        fillQuad(sr, cx, cy, s, 51, 71, 57, 77, 59, 75, 53, 69);
        fillQuad(sr, cx, cy, s, 69, 53, 75, 59, 77, 57, 71, 51);
        fillQuad(sr, cx, cy, s, 87, 35, 93, 41, 95, 39, 89, 33);
        sr.setColor(0.99f, 0.90f, 0.40f, 1f);
        fillCircle(sr, cx, cy, s, 35, 91, 1.2f); fillCircle(sr, cx, cy, s, 39, 93, 1.2f);
        fillCircle(sr, cx, cy, s, 53, 73, 1.2f); fillCircle(sr, cx, cy, s, 57, 75, 1.2f);
        fillCircle(sr, cx, cy, s, 71, 55, 1.2f); fillCircle(sr, cx, cy, s, 75, 57, 1.2f);
        fillCircle(sr, cx, cy, s, 89, 37, 1.2f); fillCircle(sr, cx, cy, s, 93, 39, 1.2f);

        // Rigging Block & Pulley Wheel at (62, 66)
        sr.setColor(0.27f, 0.15f, 0.08f, 1f);
        fillCircle(sr, cx, cy, s, 62, 66, 11.0f);
        sr.setColor(0.85f, 0.65f, 0.20f, 1f);
        fillCircle(sr, cx, cy, s, 62, 66, 8.5f);
        sr.setColor(0.18f, 0.10f, 0.06f, 1f);
        fillCircle(sr, cx, cy, s, 62, 66, 5.0f);
        sr.setColor(0.99f, 0.90f, 0.40f, 1f);
        fillCircle(sr, cx, cy, s, 62, 66, 2.5f);

        // Hemp Rigging Rope
        sr.setColor(0.85f, 0.70f, 0.48f, 1f);
        drawLine(sr, cx, cy, s, 62, 71, 48, 101, 1.8f);
        drawLine(sr, cx, cy, s, 66, 62, 98, 30, 1.8f);

        // Masthead Cap & Jolly Roger Flag
        sr.setColor(0.85f, 0.65f, 0.20f, 1f);
        fillCircle(sr, cx, cy, s, 100, 30, 4.5f);
        sr.setColor(0.09f, 0.09f, 0.11f, 1f);
        fillTri(sr, cx, cy, s, 100, 30, 122, 22, 102, 44);
        fillTri(sr, cx, cy, s, 122, 22, 114, 32, 102, 44);
        // Ivory Skull
        sr.setColor(0.96f, 0.96f, 0.96f, 1f);
        fillCircle(sr, cx, cy, s, 108, 32, 3.2f);
        fillQuad(sr, cx, cy, s, 106.5f, 33, 109.5f, 33, 109.5f, 36, 106.5f, 36);
        sr.setColor(0.94f, 0.15f, 0.15f, 1f);
        fillCircle(sr, cx, cy, s, 107, 32, 0.8f);
        fillCircle(sr, cx, cy, s, 109, 32, 0.8f);

        // LAYER 4: SPECULAR GLINTS
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 100, 28, 1.8f);
        fillCircle(sr, cx, cy, s, 61, 64, 1.5f);
        fillCircle(sr, cx, cy, s, 34, 105, 1.5f);
        fillCircle(sr, cx, cy, s, 90, 35, 1.2f);
    }

    public static void drawHaiTacLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        // LAYER 1: CURSED VORTEX & AZTEC GOLD AURA
        sr.setColor(0.96f, 0.62f, 0.04f, 0.25f);
        fillCircle(sr, cx, cy, s, 64, 64, 56);
        sr.setColor(0.18f, 0.10f, 0.06f, 0.40f);
        fillCircle(sr, cx, cy, s, 64, 64, 48);

        // LAYER 2: FACET BODY - ORNATE SHIP'S WHEEL & ANCHOR
        // Outer Wheel Rim
        sr.setColor(0.27f, 0.15f, 0.08f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 46);
        sr.setColor(0.85f, 0.65f, 0.20f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 42);
        sr.setColor(0.09f, 0.09f, 0.11f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 38);

        // 8 Turned Spokes & Grips
        sr.setColor(0.36f, 0.20f, 0.12f, 1f);
        drawLine(sr, cx, cy, s, 64, 64, 64, 16, 2.5f);
        drawLine(sr, cx, cy, s, 64, 64, 98, 30, 2.5f);
        drawLine(sr, cx, cy, s, 64, 64, 112, 64, 2.5f);
        drawLine(sr, cx, cy, s, 64, 64, 98, 98, 2.5f);
        drawLine(sr, cx, cy, s, 64, 64, 64, 112, 2.5f);
        drawLine(sr, cx, cy, s, 64, 64, 30, 98, 2.5f);
        drawLine(sr, cx, cy, s, 64, 64, 16, 64, 2.5f);
        drawLine(sr, cx, cy, s, 64, 64, 30, 30, 2.5f);

        sr.setColor(0.85f, 0.65f, 0.20f, 1f);
        fillCircle(sr, cx, cy, s, 64, 12, 3.2f);
        fillCircle(sr, cx, cy, s, 101, 27, 3.2f);
        fillCircle(sr, cx, cy, s, 116, 64, 3.2f);
        fillCircle(sr, cx, cy, s, 101, 101, 3.2f);
        fillCircle(sr, cx, cy, s, 64, 116, 3.2f);
        fillCircle(sr, cx, cy, s, 27, 101, 3.2f);
        fillCircle(sr, cx, cy, s, 12, 64, 3.2f);
        fillCircle(sr, cx, cy, s, 27, 27, 3.2f);

        // Forged-Iron Admiralty Anchor
        sr.setColor(0.18f, 0.20f, 0.25f, 1f);
        drawLine(sr, cx, cy, s, 64, 28, 64, 94, 4.0f);
        sr.setColor(0.36f, 0.20f, 0.12f, 1f);
        drawLine(sr, cx, cy, s, 42, 38, 86, 38, 3.5f);
        sr.setColor(0.85f, 0.65f, 0.20f, 1f);
        fillCircle(sr, cx, cy, s, 42, 38, 2.5f);
        fillCircle(sr, cx, cy, s, 86, 38, 2.5f);

        sr.setColor(0.18f, 0.20f, 0.25f, 1f);
        drawLine(sr, cx, cy, s, 44, 82, 64, 96, 3.8f);
        drawLine(sr, cx, cy, s, 84, 82, 64, 96, 3.8f);
        sr.setColor(0.58f, 0.63f, 0.72f, 1f);
        fillTri(sr, cx, cy, s, 44, 82, 38, 76, 48, 76);
        fillTri(sr, cx, cy, s, 84, 82, 80, 76, 90, 76);

        // Coiled Wrought-Iron Chain Links
        float[][] chainCoords = {
            {64, 32}, {86, 42}, {96, 64}, {86, 86},
            {64, 96}, {42, 86}, {32, 64}, {42, 42}
        };
        for (float[] pt : chainCoords) {
            sr.setColor(0.18f, 0.20f, 0.25f, 1f);
            fillCircle(sr, cx, cy, s, pt[0], pt[1], 5.5f);
            sr.setColor(0.58f, 0.63f, 0.72f, 1f);
            fillCircle(sr, cx, cy, s, pt[0] - 0.5f, pt[1] - 0.5f, 3.8f);
            sr.setColor(0.09f, 0.09f, 0.11f, 1f);
            fillCircle(sr, cx, cy, s, pt[0], pt[1], 2.0f);
        }

        // LAYER 3: CORE - AZTEC GOLD DOUBLOON MEDALLION
        sr.setColor(0.71f, 0.53f, 0.18f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 18.0f);
        sr.setColor(0.85f, 0.65f, 0.20f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 15.0f);
        sr.setColor(0.96f, 0.75f, 0.15f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 12.0f);

        // Ivory Skull Center Boss
        sr.setColor(0.96f, 0.96f, 0.96f, 1f);
        fillCircle(sr, cx, cy, s, 64, 63, 6.0f);
        fillQuad(sr, cx, cy, s, 61, 65, 67, 65, 66, 70, 62, 70);
        // Cursed Emerald Eye Sockets
        sr.setColor(0.06f, 0.73f, 0.51f, 1f);
        fillCircle(sr, cx, cy, s, 62, 63, 1.5f);
        fillCircle(sr, cx, cy, s, 66, 63, 1.5f);

        // LAYER 4: SPECULAR GLINTS
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 61, 62, 1.8f);
        fillCircle(sr, cx, cy, s, 64, 12, 1.5f);
        fillCircle(sr, cx, cy, s, 116, 64, 1.5f);
        fillCircle(sr, cx, cy, s, 86, 41, 1.2f);
        fillCircle(sr, cx, cy, s, 42, 41, 1.2f);
    }

    public static void drawHaiTacHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        // LAYER 1: MARITIME BLOOD-TIDE AURA
        sr.setColor(0.88f, 0.11f, 0.14f, 0.22f);
        fillCircle(sr, cx, cy, s, 64, 66, 54);

        // LAYER 2: FACET BODY - DAMASCUS SCYTHE & BASKET GUARD
        // Brass Shackle Eyelet
        sr.setColor(0.71f, 0.53f, 0.18f, 1f);
        fillCircle(sr, cx, cy, s, 38, 24, 12.0f);
        sr.setColor(0.85f, 0.65f, 0.20f, 1f);
        fillCircle(sr, cx, cy, s, 38, 24, 9.5f);
        sr.setColor(0.09f, 0.09f, 0.11f, 1f);
        fillCircle(sr, cx, cy, s, 38, 24, 5.0f);

        // Brass Basket Knuckle-Guard
        sr.setColor(0.71f, 0.53f, 0.18f, 1f);
        drawLine(sr, cx, cy, s, 38, 28, 20, 36, 4.0f);
        drawLine(sr, cx, cy, s, 20, 36, 18, 54, 3.5f);
        drawLine(sr, cx, cy, s, 18, 54, 34, 68, 3.5f);
        sr.setColor(0.96f, 0.75f, 0.15f, 1f);
        fillCircle(sr, cx, cy, s, 18, 54, 3.5f);

        // Dangling Spanish 8-Reales Silver Coin
        sr.setColor(0.58f, 0.63f, 0.72f, 1f);
        drawLine(sr, cx, cy, s, 18, 54, 18, 70, 1.5f);
        sr.setColor(0.80f, 0.84f, 0.90f, 1f);
        fillCircle(sr, cx, cy, s, 18, 74, 5.5f);
        sr.setColor(0.09f, 0.09f, 0.11f, 1f);
        fillCircle(sr, cx, cy, s, 18, 74, 1.8f);
        sr.setColor(0.85f, 0.65f, 0.20f, 1f);
        fillCircle(sr, cx, cy, s, 18, 74, 1.0f);

        // Damascus Steel Shank Facets
        sr.setColor(0.18f, 0.20f, 0.25f, 1f);
        fillQuad(sr, cx, cy, s, 32, 34, 44, 34, 46, 68, 34, 68);
        sr.setColor(0.31f, 0.35f, 0.44f, 1f);
        fillQuad(sr, cx, cy, s, 32, 34, 38, 34, 40, 68, 34, 68);
        sr.setColor(0.18f, 0.20f, 0.25f, 1f);
        fillQuad(sr, cx, cy, s, 38, 34, 44, 34, 46, 68, 40, 68);

        // Lower Shank to Keel Arc
        sr.setColor(0.18f, 0.20f, 0.25f, 1f);
        fillQuad(sr, cx, cy, s, 34, 68, 46, 68, 50, 92, 38, 92);
        sr.setColor(0.31f, 0.35f, 0.44f, 1f);
        fillQuad(sr, cx, cy, s, 34, 68, 40, 68, 44, 92, 38, 92);
        sr.setColor(0.18f, 0.20f, 0.25f, 1f);
        fillQuad(sr, cx, cy, s, 38, 92, 50, 92, 70, 106, 58, 106);
        fillQuad(sr, cx, cy, s, 58, 106, 70, 106, 88, 96, 78, 92);
        sr.setColor(0.31f, 0.35f, 0.44f, 1f);
        fillQuad(sr, cx, cy, s, 44, 92, 50, 92, 70, 106, 64, 106);
        fillQuad(sr, cx, cy, s, 64, 106, 70, 106, 88, 96, 82, 94);

        // Ascending Throat & Scythe Blade
        sr.setColor(0.18f, 0.20f, 0.25f, 1f);
        fillQuad(sr, cx, cy, s, 78, 92, 88, 96, 98, 66, 88, 68);
        sr.setColor(0.31f, 0.35f, 0.44f, 1f);
        fillQuad(sr, cx, cy, s, 82, 94, 88, 96, 98, 66, 92, 68);

        // Wicked Scythe Barb
        sr.setColor(0.31f, 0.35f, 0.44f, 1f);
        fillTri(sr, cx, cy, s, 88, 68, 76, 58, 92, 60);
        sr.setColor(0.58f, 0.63f, 0.72f, 1f);
        fillTri(sr, cx, cy, s, 92, 60, 76, 58, 90, 56);

        // Razor Talon Point
        sr.setColor(0.58f, 0.63f, 0.72f, 1f);
        fillQuad(sr, cx, cy, s, 88, 68, 98, 66, 100, 44, 94, 54);
        sr.setColor(0.88f, 0.91f, 0.96f, 1f);
        fillQuad(sr, cx, cy, s, 92, 68, 98, 66, 100, 44, 96, 52);

        // LAYER 3: CORE - CURSED CRIMSON BLOOD FULLER & RUBY GEMS
        sr.setColor(0.88f, 0.11f, 0.14f, 1f);
        drawLine(sr, cx, cy, s, 39, 40, 41, 90, 2.5f);
        drawLine(sr, cx, cy, s, 41, 90, 66, 103, 2.5f);
        drawLine(sr, cx, cy, s, 66, 103, 94, 60, 2.0f);
        drawLine(sr, cx, cy, s, 94, 60, 98, 48, 1.5f);
        sr.setColor(0.94f, 0.15f, 0.15f, 1f);
        fillCircle(sr, cx, cy, s, 40, 65, 2.2f);
        fillCircle(sr, cx, cy, s, 64, 103, 2.2f);
        fillCircle(sr, cx, cy, s, 92, 64, 2.2f);

        // LAYER 4: SPECULAR GLINTS
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 100, 44, 2.2f);
        fillCircle(sr, cx, cy, s, 76, 58, 1.8f);
        fillCircle(sr, cx, cy, s, 18, 54, 1.5f);
        fillCircle(sr, cx, cy, s, 18, 73, 1.5f);
        fillCircle(sr, cx, cy, s, 41, 21, 1.5f);
    }

    // =========================================================================
    // 6. VỰC THẲM SET (Obsidian Nether Staff, Void Vein Spool, Dragon Claw Hook)
    // =========================================================================
    public static void drawVucThamRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        // LAYER 1: VOID MIASMA & NETHER ELDRITCH AURA
        sr.setColor(0.66f, 0.33f, 0.97f, 0.25f);
        fillCircle(sr, cx, cy, s, 64, 64, 56);
        sr.setColor(0.35f, 0.09f, 0.58f, 0.15f);
        fillTri(sr, cx, cy, s, 20, 108, 108, 20, 100, 12);

        // LAYER 2: FACET BODY - OBSIDIAN NETHER SHAFT & HORNS
        // Spiky Bone Horn Spire Bands
        sr.setColor(0.18f, 0.05f, 0.40f, 1f);
        fillTri(sr, cx, cy, s, 36, 84, 26, 86, 38, 92);
        fillTri(sr, cx, cy, s, 44, 92, 42, 102, 38, 92);
        fillTri(sr, cx, cy, s, 54, 66, 44, 68, 56, 74);
        fillTri(sr, cx, cy, s, 62, 74, 60, 84, 56, 74);
        fillTri(sr, cx, cy, s, 72, 48, 62, 50, 74, 56);
        fillTri(sr, cx, cy, s, 80, 56, 78, 66, 74, 56);
        // Horn tips glowing magenta
        sr.setColor(0.75f, 0.15f, 0.83f, 1f);
        fillCircle(sr, cx, cy, s, 26, 86, 1.8f);
        fillCircle(sr, cx, cy, s, 42, 102, 1.8f);
        fillCircle(sr, cx, cy, s, 44, 68, 1.8f);
        fillCircle(sr, cx, cy, s, 60, 84, 1.8f);
        fillCircle(sr, cx, cy, s, 62, 50, 1.8f);
        fillCircle(sr, cx, cy, s, 78, 66, 1.8f);

        // Obsidian Nether Shaft
        sr.setColor(0.06f, 0.09f, 0.16f, 1f);
        fillQuad(sr, cx, cy, s, 18, 110, 22, 106, 92, 36, 88, 40);
        sr.setColor(0.12f, 0.11f, 0.29f, 1f);
        fillQuad(sr, cx, cy, s, 19, 109, 21, 107, 91, 37, 89, 39);

        // Void Plasma Conduit
        sr.setColor(0.75f, 0.15f, 0.83f, 1f);
        drawLine(sr, cx, cy, s, 20, 108, 90, 38, 2.5f);
        sr.setColor(0.91f, 0.47f, 0.98f, 1f);
        drawLine(sr, cx, cy, s, 20, 108, 90, 38, 1.2f);

        // Void Crescent Blade Headpiece
        sr.setColor(0.06f, 0.09f, 0.16f, 1f);
        fillTri(sr, cx, cy, s, 90, 38, 68, 24, 88, 16);
        fillTri(sr, cx, cy, s, 90, 38, 88, 16, 106, 20);
        sr.setColor(0.35f, 0.09f, 0.58f, 1f);
        fillTri(sr, cx, cy, s, 68, 24, 88, 16, 76, 14);
        sr.setColor(0.75f, 0.15f, 0.83f, 1f);
        drawLine(sr, cx, cy, s, 68, 24, 88, 16, 1.8f);
        drawLine(sr, cx, cy, s, 88, 16, 106, 20, 1.8f);

        // LAYER 3: CORE - ALL-SEEING NETHER EYE
        sr.setColor(0.06f, 0.09f, 0.16f, 1f);
        fillCircle(sr, cx, cy, s, 94, 34, 11.0f);
        sr.setColor(0.58f, 0.20f, 0.92f, 1f);
        fillCircle(sr, cx, cy, s, 94, 34, 8.0f);
        sr.setColor(0.86f, 0.15f, 0.45f, 1f);
        fillCircle(sr, cx, cy, s, 94, 34, 5.5f);
        sr.setColor(0.04f, 0.02f, 0.08f, 1f);
        fillTri(sr, cx, cy, s, 94, 28, 92, 34, 94, 40);
        fillTri(sr, cx, cy, s, 94, 28, 96, 34, 94, 40);

        // Pommel Void Eye
        sr.setColor(0.58f, 0.20f, 0.92f, 1f);
        fillCircle(sr, cx, cy, s, 20, 108, 5.5f);
        sr.setColor(0.04f, 0.02f, 0.08f, 1f);
        fillCircle(sr, cx, cy, s, 20, 108, 2.5f);

        // LAYER 4: SPECULAR GLINTS
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 92, 32, 2.0f);
        fillCircle(sr, cx, cy, s, 68, 24, 1.8f);
        fillCircle(sr, cx, cy, s, 88, 16, 1.8f);
        fillCircle(sr, cx, cy, s, 106, 20, 1.8f);
        fillCircle(sr, cx, cy, s, 19, 107, 1.5f);
    }

    public static void drawVucThamLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        // LAYER 1: VOID VORTEX AURA
        sr.setColor(0.66f, 0.33f, 0.97f, 0.25f);
        fillCircle(sr, cx, cy, s, 64, 64, 56);

        // LAYER 2: FACET BODY - OCTAGONAL NETHER-TITANIUM SPOOL & 8 HORNS
        sr.setColor(0.06f, 0.09f, 0.16f, 1f);
        fillQuad(sr, cx, cy, s, 48, 16, 80, 16, 112, 48, 16, 48);
        fillQuad(sr, cx, cy, s, 16, 48, 112, 48, 112, 80, 16, 80);
        fillQuad(sr, cx, cy, s, 16, 80, 112, 80, 80, 112, 48, 112);

        sr.setColor(0.12f, 0.11f, 0.29f, 1f);
        fillQuad(sr, cx, cy, s, 49, 19, 79, 19, 109, 49, 19, 49);
        fillQuad(sr, cx, cy, s, 19, 49, 109, 49, 109, 79, 19, 79);
        fillQuad(sr, cx, cy, s, 19, 79, 109, 79, 79, 109, 49, 109);

        // 8 Demonic Horn Spines
        sr.setColor(0.35f, 0.09f, 0.58f, 1f);
        fillTri(sr, cx, cy, s, 60, 16, 68, 16, 64, 4);
        fillTri(sr, cx, cy, s, 60, 112, 68, 112, 64, 124);
        fillTri(sr, cx, cy, s, 16, 60, 16, 68, 4, 64);
        fillTri(sr, cx, cy, s, 112, 60, 112, 68, 124, 64);
        fillTri(sr, cx, cy, s, 28, 36, 36, 28, 20, 20);
        fillTri(sr, cx, cy, s, 100, 36, 92, 28, 108, 20);
        fillTri(sr, cx, cy, s, 28, 92, 36, 100, 20, 108);
        fillTri(sr, cx, cy, s, 100, 92, 92, 100, 108, 108);

        // Nether Vein Silk Filament Spool
        sr.setColor(0.04f, 0.02f, 0.08f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 40);
        sr.setColor(0.35f, 0.09f, 0.58f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 36);
        sr.setColor(0.58f, 0.20f, 0.92f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 31);
        sr.setColor(0.75f, 0.15f, 0.83f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 25);
        sr.setColor(0.04f, 0.02f, 0.08f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 19);

        // LAYER 3: CORE - CENTRAL NETHER MEDALLION & EYE
        sr.setColor(0.18f, 0.05f, 0.40f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 15.0f);
        sr.setColor(0.58f, 0.20f, 0.92f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 11.0f);
        sr.setColor(0.04f, 0.02f, 0.08f, 1f);
        fillTri(sr, cx, cy, s, 64, 55, 61, 64, 64, 73);
        fillTri(sr, cx, cy, s, 64, 55, 67, 64, 64, 73);
        sr.setColor(0.96f, 0.45f, 0.71f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 2.0f);

        // LAYER 4: SPECULAR GLINTS
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 62, 62, 1.8f);
        fillCircle(sr, cx, cy, s, 64, 5, 1.8f);
        fillCircle(sr, cx, cy, s, 123, 64, 1.8f);
        fillCircle(sr, cx, cy, s, 107, 21, 1.8f);
        fillCircle(sr, cx, cy, s, 21, 21, 1.8f);
        fillCircle(sr, cx, cy, s, 5, 64, 1.8f);
    }

    public static void drawVucThamHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        // LAYER 1: VOID MIASMA AURA
        sr.setColor(0.66f, 0.33f, 0.97f, 0.25f);
        fillCircle(sr, cx, cy, s, 64, 66, 54);

        // LAYER 2: FACET BODY - CHITIN DRAGON CLAW HOOK
        // Outer Chitin Spines
        sr.setColor(0.12f, 0.11f, 0.29f, 1f);
        fillTri(sr, cx, cy, s, 32, 44, 18, 52, 34, 60);
        sr.setColor(0.19f, 0.18f, 0.51f, 1f);
        fillTri(sr, cx, cy, s, 34, 60, 18, 52, 34, 54);

        sr.setColor(0.12f, 0.11f, 0.29f, 1f);
        fillTri(sr, cx, cy, s, 36, 72, 20, 86, 38, 88);
        sr.setColor(0.23f, 0.03f, 0.39f, 1f);
        fillTri(sr, cx, cy, s, 38, 88, 20, 86, 40, 82);

        sr.setColor(0.09f, 0.05f, 0.16f, 1f);
        fillTri(sr, cx, cy, s, 52, 100, 48, 118, 64, 106);
        sr.setColor(0.18f, 0.06f, 0.40f, 1f);
        fillTri(sr, cx, cy, s, 64, 106, 48, 118, 60, 102);

        // Eyelet Demonic Horn
        sr.setColor(0.18f, 0.06f, 0.40f, 1f);
        fillTri(sr, cx, cy, s, 34, 20, 22, 10, 40, 16);
        sr.setColor(0.30f, 0.11f, 0.58f, 1f);
        fillTri(sr, cx, cy, s, 40, 16, 22, 10, 38, 24);

        // Eyelet Outer Rings
        sr.setColor(0.06f, 0.09f, 0.16f, 1f);
        fillCircle(sr, cx, cy, s, 38, 26, 14.0f);
        sr.setColor(0.12f, 0.11f, 0.29f, 1f);
        fillCircle(sr, cx, cy, s, 38, 26, 11.0f);
        sr.setColor(0.04f, 0.02f, 0.08f, 1f);
        fillCircle(sr, cx, cy, s, 38, 26, 7.0f);

        // Shank Chitin Facets
        sr.setColor(0.09f, 0.05f, 0.16f, 1f);
        fillQuad(sr, cx, cy, s, 32, 38, 44, 38, 46, 68, 34, 68);
        sr.setColor(0.18f, 0.06f, 0.40f, 1f);
        fillQuad(sr, cx, cy, s, 32, 38, 38, 38, 40, 68, 34, 68);
        sr.setColor(0.06f, 0.09f, 0.16f, 1f);
        fillQuad(sr, cx, cy, s, 38, 38, 44, 38, 46, 68, 40, 68);

        // Lower Shank to Heel Curve
        sr.setColor(0.09f, 0.05f, 0.16f, 1f);
        fillQuad(sr, cx, cy, s, 34, 68, 46, 68, 50, 92, 38, 92);
        sr.setColor(0.18f, 0.06f, 0.40f, 1f);
        fillQuad(sr, cx, cy, s, 34, 68, 40, 68, 44, 92, 38, 92);

        // Heel / Keel Arc
        sr.setColor(0.06f, 0.09f, 0.16f, 1f);
        fillQuad(sr, cx, cy, s, 38, 92, 50, 92, 70, 106, 58, 106);
        fillQuad(sr, cx, cy, s, 58, 106, 70, 106, 88, 96, 78, 92);
        sr.setColor(0.18f, 0.06f, 0.40f, 1f);
        fillQuad(sr, cx, cy, s, 44, 92, 50, 92, 70, 106, 64, 106);
        sr.setColor(0.23f, 0.03f, 0.39f, 1f);
        fillQuad(sr, cx, cy, s, 64, 106, 70, 106, 88, 96, 82, 94);

        // Ascending Throat & Talon
        sr.setColor(0.06f, 0.09f, 0.16f, 1f);
        fillQuad(sr, cx, cy, s, 78, 92, 88, 96, 98, 66, 88, 68);
        sr.setColor(0.18f, 0.06f, 0.40f, 1f);
        fillQuad(sr, cx, cy, s, 82, 94, 88, 96, 98, 66, 92, 68);

        // Deadly Reverse Barb
        sr.setColor(0.23f, 0.03f, 0.39f, 1f);
        fillTri(sr, cx, cy, s, 88, 68, 76, 58, 92, 60);
        sr.setColor(0.35f, 0.11f, 0.53f, 1f);
        fillTri(sr, cx, cy, s, 92, 60, 76, 58, 90, 56);

        // Razor Talon Point
        sr.setColor(0.18f, 0.06f, 0.40f, 1f);
        fillQuad(sr, cx, cy, s, 88, 68, 98, 66, 100, 44, 94, 54);
        sr.setColor(0.30f, 0.11f, 0.58f, 1f);
        fillQuad(sr, cx, cy, s, 92, 68, 98, 66, 100, 44, 96, 52);

        // LAYER 3: CORE - PLASMA FULLER GROOVE & NETHER EYE
        sr.setColor(0.75f, 0.15f, 0.83f, 1f);
        drawLine(sr, cx, cy, s, 39, 40, 41, 90, 2.5f);
        drawLine(sr, cx, cy, s, 41, 90, 66, 103, 2.5f);
        drawLine(sr, cx, cy, s, 66, 103, 94, 60, 2.0f);
        sr.setColor(0.91f, 0.47f, 0.98f, 1f);
        drawLine(sr, cx, cy, s, 94, 60, 98, 48, 1.5f);

        // Glowing Nether Eye in Socket
        sr.setColor(0.66f, 0.33f, 0.97f, 1f);
        fillCircle(sr, cx, cy, s, 38, 26, 5.0f);
        sr.setColor(0.04f, 0.02f, 0.08f, 1f);
        fillTri(sr, cx, cy, s, 38, 22, 36, 26, 38, 30);
        fillTri(sr, cx, cy, s, 38, 22, 40, 26, 38, 30);
        sr.setColor(0.96f, 0.45f, 0.71f, 1f);
        fillCircle(sr, cx, cy, s, 38, 26, 1.5f);

        // LAYER 4: SPECULAR GLINTS
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 100, 44, 2.2f);
        fillCircle(sr, cx, cy, s, 76, 58, 1.8f);
        fillCircle(sr, cx, cy, s, 18, 52, 1.5f);
        fillCircle(sr, cx, cy, s, 20, 86, 1.5f);
        fillCircle(sr, cx, cy, s, 48, 118, 1.8f);
        fillCircle(sr, cx, cy, s, 22, 10, 1.5f);
        fillCircle(sr, cx, cy, s, 41, 23, 1.5f);
    }

    // =========================================================================
    // 7. HOÀNG GIA SET (Platinum Crown Scepter, Imperial Silk Spool, White Jade Hook)
    // =========================================================================
    public static void drawHoangGiaRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        // LAYER 1: RADIANT ROYAL AURA
        sr.setColor(0.98f, 0.75f, 0.14f, 0.25f);
        fillCircle(sr, cx, cy, s, 64, 64, 56);

        // LAYER 2: FACET BODY - ROYAL PLATINUM & GOLD SCEPTER
        // Pommel: Golden Crown & Royal Blue Sapphire Orb
        sr.setColor(0.12f, 0.23f, 0.54f, 1f);
        fillCircle(sr, cx, cy, s, 18, 110, 8.0f);
        sr.setColor(0.15f, 0.39f, 0.92f, 1f);
        fillCircle(sr, cx, cy, s, 18, 110, 6.0f);
        sr.setColor(0.85f, 0.47f, 0.02f, 1f);
        fillTri(sr, cx, cy, s, 14, 106, 26, 98, 20, 102);
        fillTri(sr, cx, cy, s, 20, 102, 18, 94, 16, 102);
        fillTri(sr, cx, cy, s, 16, 102, 10, 98, 22, 106);

        // Royal Grip: Platinum & Gold Filigree
        sr.setColor(0.80f, 0.84f, 0.88f, 1f);
        fillQuad(sr, cx, cy, s, 18, 104, 24, 104, 38, 90, 32, 90);
        sr.setColor(0.97f, 0.98f, 0.99f, 1f);
        fillQuad(sr, cx, cy, s, 20, 104, 24, 104, 38, 90, 34, 90);
        sr.setColor(0.96f, 0.62f, 0.04f, 1f);
        drawLine(sr, cx, cy, s, 21, 101, 26, 96, 2.0f);
        drawLine(sr, cx, cy, s, 27, 95, 32, 90, 2.0f);

        // Reel Seat / Fleur-de-lis Golden Sovereign Mount
        sr.setColor(0.71f, 0.33f, 0.04f, 1f);
        fillCircle(sr, cx, cy, s, 36, 86, 9.0f);
        sr.setColor(0.96f, 0.62f, 0.04f, 1f);
        fillCircle(sr, cx, cy, s, 36, 86, 7.0f);
        sr.setColor(0.99f, 0.94f, 0.54f, 1f);
        fillTri(sr, cx, cy, s, 36, 76, 39, 83, 33, 83);
        fillTri(sr, cx, cy, s, 45, 84, 40, 88, 41, 94);
        fillTri(sr, cx, cy, s, 27, 84, 32, 88, 31, 94);
        sr.setColor(0.12f, 0.23f, 0.54f, 1f);
        fillCircle(sr, cx, cy, s, 36, 86, 3.5f);
        sr.setColor(0.38f, 0.65f, 0.98f, 1f);
        fillCircle(sr, cx, cy, s, 36, 86, 2.0f);

        // Main Shaft: Polished Royal Platinum
        // Lower Section
        sr.setColor(0.80f, 0.84f, 0.88f, 1f);
        fillQuad(sr, cx, cy, s, 36, 84, 42, 84, 66, 60, 62, 56);
        sr.setColor(0.97f, 0.98f, 0.99f, 1f);
        fillQuad(sr, cx, cy, s, 38, 84, 42, 84, 66, 60, 64, 58);
        // Gold Ferrule 1
        sr.setColor(0.96f, 0.62f, 0.04f, 1f);
        fillQuad(sr, cx, cy, s, 61, 59, 67, 59, 69, 55, 63, 55);

        // Mid Section
        sr.setColor(0.80f, 0.84f, 0.88f, 1f);
        fillQuad(sr, cx, cy, s, 64, 56, 68, 56, 88, 36, 85, 33);
        sr.setColor(0.97f, 0.98f, 0.99f, 1f);
        fillQuad(sr, cx, cy, s, 66, 56, 68, 56, 88, 36, 86, 34);
        // Gold Ferrule 2
        sr.setColor(0.96f, 0.62f, 0.04f, 1f);
        fillQuad(sr, cx, cy, s, 84, 35, 89, 35, 91, 31, 86, 31);

        // Tip Section
        sr.setColor(0.89f, 0.91f, 0.94f, 1f);
        fillQuad(sr, cx, cy, s, 87, 32, 90, 32, 106, 16, 104, 14);
        sr.setColor(Color.WHITE);
        fillQuad(sr, cx, cy, s, 88, 32, 90, 32, 106, 16, 105, 15);

        // Royal Line Guides & Golden Line
        sr.setColor(0.85f, 0.47f, 0.02f, 1f);
        fillCircle(sr, cx, cy, s, 58, 68, 4.5f);
        sr.setColor(0.09f, 0.09f, 0.11f, 1f);
        fillCircle(sr, cx, cy, s, 58, 68, 2.5f);
        sr.setColor(0.85f, 0.47f, 0.02f, 1f);
        fillCircle(sr, cx, cy, s, 78, 46, 3.5f);
        sr.setColor(0.09f, 0.09f, 0.11f, 1f);
        fillCircle(sr, cx, cy, s, 78, 46, 1.8f);

        sr.setColor(0.99f, 0.94f, 0.54f, 1f);
        drawLine(sr, cx, cy, s, 36, 86, 58, 68, 1.2f);
        drawLine(sr, cx, cy, s, 58, 68, 78, 46, 1.2f);
        drawLine(sr, cx, cy, s, 78, 46, 105, 15, 1.2f);

        // LAYER 3: CORE - ROYAL CROWN FINIAL & RUBY GEM
        sr.setColor(0.85f, 0.47f, 0.02f, 1f);
        fillTri(sr, cx, cy, s, 104, 18, 114, 8, 109, 12);
        fillTri(sr, cx, cy, s, 104, 18, 106, 6, 108, 18);
        fillTri(sr, cx, cy, s, 108, 18, 98, 8, 103, 12);

        // Glowing Royal Ruby Jewel
        sr.setColor(0.86f, 0.15f, 0.15f, 1f);
        fillTri(sr, cx, cy, s, 106, 6, 110, 10, 106, 14);
        fillTri(sr, cx, cy, s, 106, 6, 102, 10, 106, 14);
        sr.setColor(0.94f, 0.27f, 0.27f, 1f);
        fillTri(sr, cx, cy, s, 106, 6, 109, 10, 106, 13);
        fillTri(sr, cx, cy, s, 106, 6, 103, 10, 106, 13);

        // LAYER 4: SPECULAR GLINTS
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 106, 8, 1.8f);
        fillCircle(sr, cx, cy, s, 89, 31, 1.5f);
        fillCircle(sr, cx, cy, s, 66, 55, 1.5f);
        fillCircle(sr, cx, cy, s, 36, 84, 1.8f);
        fillCircle(sr, cx, cy, s, 16, 108, 1.8f);
    }

    public static void drawHoangGiaLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        // LAYER 1: REGAL PURPLE & GOLD AURA
        sr.setColor(0.75f, 0.52f, 0.99f, 0.25f);
        fillCircle(sr, cx, cy, s, 64, 64, 56);

        // LAYER 2: FACET BODY - 8-SPOKE ROYAL GOLD WHEEL
        // Outer Gold Rim
        sr.setColor(0.47f, 0.21f, 0.06f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 50.0f);
        sr.setColor(0.85f, 0.47f, 0.02f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 47.0f);
        sr.setColor(0.96f, 0.62f, 0.04f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 44.0f);
        sr.setColor(0.71f, 0.33f, 0.04f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 41.0f);

        // 8 Royal Crown Spoke Projections
        sr.setColor(0.99f, 0.94f, 0.54f, 1f);
        fillTri(sr, cx, cy, s, 64, 10, 69, 19, 59, 19);
        fillTri(sr, cx, cy, s, 64, 118, 69, 109, 59, 109);
        fillTri(sr, cx, cy, s, 10, 64, 19, 59, 19, 69);
        fillTri(sr, cx, cy, s, 118, 64, 109, 59, 109, 69);
        fillTri(sr, cx, cy, s, 26, 26, 35, 28, 28, 35);
        fillTri(sr, cx, cy, s, 102, 26, 93, 28, 100, 35);
        fillTri(sr, cx, cy, s, 26, 102, 35, 100, 28, 93);
        fillTri(sr, cx, cy, s, 102, 102, 93, 100, 100, 93);

        // Royal Purple Silk Spool
        sr.setColor(0.18f, 0.06f, 0.40f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 40.0f);
        sr.setColor(0.35f, 0.11f, 0.53f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 37.0f);
        sr.setColor(0.49f, 0.23f, 0.93f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 33.0f);
        sr.setColor(0.58f, 0.20f, 0.92f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 29.0f);

        // Golden Silk Thread Waves & Trailing Line
        sr.setColor(0.99f, 0.94f, 0.54f, 1f);
        drawLine(sr, cx, cy, s, 88, 40, 102, 28, 2.2f);
        drawLine(sr, cx, cy, s, 102, 28, 116, 14, 2.2f);
        fillCircle(sr, cx, cy, s, 116, 14, 2.5f);

        // LAYER 3: CORE - ROYAL SAPPHIRE & GOLD MEDALLION
        sr.setColor(0.85f, 0.47f, 0.02f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 21.0f);
        sr.setColor(0.96f, 0.62f, 0.04f, 1f);
        fillCircle(sr, cx, cy, s, 64, 64, 18.0f);

        sr.setColor(0.99f, 0.94f, 0.54f, 1f);
        fillTri(sr, cx, cy, s, 64, 44, 67, 50, 61, 50);
        fillTri(sr, cx, cy, s, 64, 84, 67, 78, 61, 78);
        fillTri(sr, cx, cy, s, 44, 64, 50, 67, 50, 61);
        fillTri(sr, cx, cy, s, 84, 64, 78, 67, 78, 61);

        // Brilliant Cut Royal Sapphire Gem
        sr.setColor(0.12f, 0.23f, 0.54f, 1f);
        fillQuad(sr, cx, cy, s, 54, 54, 74, 54, 78, 64, 50, 64);
        fillQuad(sr, cx, cy, s, 50, 64, 78, 64, 74, 74, 54, 74);
        sr.setColor(0.15f, 0.39f, 0.92f, 1f);
        fillQuad(sr, cx, cy, s, 56, 56, 72, 56, 75, 64, 53, 64);
        fillQuad(sr, cx, cy, s, 53, 64, 75, 64, 72, 72, 56, 72);
        sr.setColor(0.23f, 0.51f, 0.96f, 1f);
        fillQuad(sr, cx, cy, s, 58, 58, 70, 58, 72, 64, 56, 64);
        fillQuad(sr, cx, cy, s, 56, 64, 72, 64, 70, 70, 58, 70);
        sr.setColor(0.38f, 0.65f, 0.98f, 1f);
        fillQuad(sr, cx, cy, s, 60, 59, 68, 59, 70, 64, 58, 64);

        // LAYER 4: SPECULAR GLINTS
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 61, 59, 2.0f);
        fillCircle(sr, cx, cy, s, 64, 11, 1.5f);
        fillCircle(sr, cx, cy, s, 117, 14, 1.8f);
        fillCircle(sr, cx, cy, s, 102, 27, 1.5f);
        fillCircle(sr, cx, cy, s, 117, 64, 1.5f);
        fillCircle(sr, cx, cy, s, 27, 27, 1.5f);
        fillCircle(sr, cx, cy, s, 11, 64, 1.5f);
    }

    public static void drawHoangGiaHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        // LAYER 1: RADIANT CHAMPAGNE GOLD AURA
        sr.setColor(0.99f, 0.94f, 0.54f, 0.25f);
        fillCircle(sr, cx, cy, s, 64, 66, 54);

        // LAYER 2: FACET BODY - WHITE JADE & GOLD FILIGREE HOOK
        // Golden Crown Eyelet Prongs
        sr.setColor(0.85f, 0.47f, 0.02f, 1f);
        fillTri(sr, cx, cy, s, 30, 16, 38, 6, 46, 16);
        sr.setColor(0.96f, 0.62f, 0.04f, 1f);
        fillTri(sr, cx, cy, s, 32, 16, 38, 8, 44, 16);
        sr.setColor(0.71f, 0.33f, 0.04f, 1f);
        fillTri(sr, cx, cy, s, 26, 14, 32, 20, 28, 24);
        fillTri(sr, cx, cy, s, 50, 14, 44, 20, 48, 24);

        // Polished Gold Eyelet Ring
        sr.setColor(0.71f, 0.33f, 0.04f, 1f);
        fillCircle(sr, cx, cy, s, 38, 24, 12.0f);
        sr.setColor(0.96f, 0.62f, 0.04f, 1f);
        fillCircle(sr, cx, cy, s, 38, 24, 9.5f);
        sr.setColor(0.12f, 0.11f, 0.29f, 1f);
        fillCircle(sr, cx, cy, s, 38, 24, 5.0f);

        // Hanging Royal Pearl Charm
        sr.setColor(0.85f, 0.47f, 0.02f, 1f);
        drawLine(sr, cx, cy, s, 28, 24, 22, 38, 1.5f);
        sr.setColor(0.95f, 0.91f, 1.0f, 1f);
        fillCircle(sr, cx, cy, s, 20, 44, 5.5f);
        sr.setColor(0.98f, 0.96f, 1.0f, 1f);
        fillCircle(sr, cx, cy, s, 19, 43, 4.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 18, 41, 1.5f);

        // Gold Outer Reinforcing Ridge
        sr.setColor(0.85f, 0.47f, 0.02f, 1f);
        fillQuad(sr, cx, cy, s, 30, 34, 35, 34, 39, 68, 34, 68);
        fillQuad(sr, cx, cy, s, 34, 68, 39, 68, 45, 94, 40, 94);
        fillQuad(sr, cx, cy, s, 40, 94, 45, 94, 67, 108, 62, 108);
        fillQuad(sr, cx, cy, s, 62, 108, 67, 108, 89, 98, 84, 94);
        fillQuad(sr, cx, cy, s, 84, 94, 89, 98, 100, 64, 95, 64);

        // White Jade Core Shank & Curve (Faceted)
        // Upper Shank
        sr.setColor(0.89f, 0.91f, 0.94f, 1f);
        fillQuad(sr, cx, cy, s, 34, 34, 44, 34, 45, 68, 35, 68);
        sr.setColor(Color.WHITE);
        fillQuad(sr, cx, cy, s, 36, 34, 42, 34, 43, 68, 37, 68);

        // Lower Shank to Bend
        sr.setColor(0.89f, 0.91f, 0.94f, 1f);
        fillQuad(sr, cx, cy, s, 35, 68, 45, 68, 51, 92, 41, 92);
        sr.setColor(Color.WHITE);
        fillQuad(sr, cx, cy, s, 37, 68, 43, 68, 49, 92, 43, 92);

        // Keel Arc
        sr.setColor(0.89f, 0.91f, 0.94f, 1f);
        fillQuad(sr, cx, cy, s, 41, 92, 51, 92, 70, 105, 60, 105);
        sr.setColor(Color.WHITE);
        fillQuad(sr, cx, cy, s, 43, 92, 49, 92, 68, 105, 62, 105);

        sr.setColor(0.89f, 0.91f, 0.94f, 1f);
        fillQuad(sr, cx, cy, s, 60, 105, 70, 105, 86, 94, 76, 90);
        sr.setColor(Color.WHITE);
        fillQuad(sr, cx, cy, s, 62, 105, 68, 105, 84, 94, 78, 92);

        // Ascending Throat & Spear
        sr.setColor(0.89f, 0.91f, 0.94f, 1f);
        fillQuad(sr, cx, cy, s, 76, 90, 86, 94, 97, 64, 87, 64);
        sr.setColor(Color.WHITE);
        fillQuad(sr, cx, cy, s, 78, 92, 84, 94, 95, 64, 89, 64);

        // Royal Golden Barb
        sr.setColor(0.85f, 0.47f, 0.02f, 1f);
        fillTri(sr, cx, cy, s, 87, 64, 74, 54, 91, 56);
        sr.setColor(0.96f, 0.62f, 0.04f, 1f);
        fillTri(sr, cx, cy, s, 91, 56, 74, 54, 89, 52);
        sr.setColor(0.99f, 0.94f, 0.54f, 1f);
        fillTri(sr, cx, cy, s, 89, 52, 74, 54, 87, 51);

        // Razor Golden / Jade Point
        sr.setColor(0.96f, 0.62f, 0.04f, 1f);
        fillQuad(sr, cx, cy, s, 87, 64, 97, 64, 100, 42, 93, 52);
        sr.setColor(0.99f, 0.94f, 0.54f, 1f);
        fillQuad(sr, cx, cy, s, 91, 64, 97, 64, 100, 42, 95, 50);

        // LAYER 3: CORE - RUBY ACCENTS
        sr.setColor(0.86f, 0.15f, 0.15f, 1f);
        fillTri(sr, cx, cy, s, 38, 18, 41, 21, 35, 21);
        fillTri(sr, cx, cy, s, 38, 24, 41, 21, 35, 21);
        fillCircle(sr, cx, cy, s, 88, 58, 2.2f);
        sr.setColor(0.97f, 0.44f, 0.44f, 1f);
        fillCircle(sr, cx, cy, s, 88, 58, 1.3f);

        // LAYER 4: SPECULAR GLINTS
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 100, 42, 2.2f);
        fillCircle(sr, cx, cy, s, 74, 54, 1.6f);
        fillCircle(sr, cx, cy, s, 18, 41, 1.5f);
        fillCircle(sr, cx, cy, s, 38, 7, 1.5f);
        fillCircle(sr, cx, cy, s, 26, 14, 1.2f);
        fillCircle(sr, cx, cy, s, 50, 14, 1.2f);
        fillCircle(sr, cx, cy, s, 41, 52, 1.5f);
        fillCircle(sr, cx, cy, s, 64, 105, 1.5f);
    }

    // =========================================================================
    // 8. THỜI KHÔNG SET (CHRONO / SPACE-TIME)
    // =========================================================================
    public static void drawThoiKhongRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float scale = (r * 2f) / 128f;

        // 1. Cosmic Temporal Rift & Tachyon Singularity Aura
        sr.setColor(0.48f, 0.22f, 0.95f, 0.16f);
        sr.circle(cx, cy, r * 0.92f);
        sr.setColor(0.22f, 0.74f, 0.98f, 0.18f);
        sr.circle(cx + 14f * scale, cy + 14f * scale, r * 0.62f);

        // 2. Celestial Astrolabe Ring & Clockwork Headpiece (Center-Top-Right)
        float hx = cx + 18f * scale;
        float hy = cy + 18f * scale;
        float hRad = 24f * scale;

        // Astrolabe Gold Gear Bezel
        sr.setColor(0.85f, 0.47f, 0.02f, 1f);
        sr.circle(hx, hy, hRad);
        sr.setColor(0.04f, 0.05f, 0.10f, 1f);
        sr.circle(hx, hy, hRad - 2.5f * scale);

        // 8 Clockwork Gear Teeth along rim
        sr.setColor(0.98f, 0.75f, 0.14f, 1f);
        for (int i = 0; i < 8; i++) {
            float ang = i * 45f;
            float gx = hx + MathUtils.cosDeg(ang) * (hRad + 1.5f * scale);
            float gy = hy + MathUtils.sinDeg(ang) * (hRad + 1.5f * scale);
            sr.circle(gx, gy, 2.5f * scale);
        }

        // Astrolabe Celestial Dial Face & Cosmic Orbitals
        sr.setColor(0.12f, 0.11f, 0.29f, 1f);
        sr.circle(hx, hy, 14f * scale);
        sr.setColor(0.48f, 0.22f, 0.95f, 0.7f);
        sr.circle(hx, hy, 9f * scale);
        sr.setColor(0.12f, 0.11f, 0.29f, 1f);
        sr.circle(hx, hy, 6.5f * scale);

        // Clock Hands (Tachyon gold pointing to past & future)
        sr.setColor(0.98f, 0.75f, 0.14f, 1f);
        sr.rectLine(hx, hy, hx + 9f * scale, hy + 4f * scale, Math.max(1.8f, 2f * scale));
        sr.rectLine(hx, hy, hx - 5f * scale, hy + 8f * scale, Math.max(1.8f, 2.2f * scale));
        sr.setColor(Color.WHITE);
        sr.circle(hx, hy, 2.5f * scale);

        // 3. Diagonal Scepter Staff Shaft (Bottom-Left to Astrolabe)
        float sx1 = cx - 38f * scale;
        float sy1 = cy - 38f * scale;
        float sx2 = hx;
        float sy2 = hy;

        // Dark celestial titanium under-spine
        sr.setColor(0.06f, 0.09f, 0.16f, 1f);
        sr.rectLine(sx1, sy1, sx2, sy2, Math.max(3.5f, 5.5f * scale));

        // Luminous Cyan Plasma Conduit
        sr.setColor(0.22f, 0.74f, 0.98f, 0.95f);
        sr.rectLine(sx1, sy1, sx2, sy2, Math.max(1.8f, 2.5f * scale));
        sr.setColor(Color.WHITE);
        sr.rectLine(sx1, sy1, sx2, sy2, Math.max(1f, 1.2f * scale));

        // Golden Chronometer Node Rings along shaft
        sr.setColor(0.98f, 0.75f, 0.14f, 1f);
        float nx1 = sx1 + (sx2 - sx1) * 0.35f;
        float ny1 = sy1 + (sy2 - sy1) * 0.35f;
        sr.circle(nx1, ny1, 4.5f * scale);
        sr.setColor(0.06f, 0.09f, 0.16f, 1f);
        sr.circle(nx1, ny1, 2f * scale);

        sr.setColor(0.98f, 0.75f, 0.14f, 1f);
        float nx2 = sx1 + (sx2 - sx1) * 0.70f;
        float ny2 = sy1 + (sy2 - sy1) * 0.70f;
        sr.circle(nx2, ny2, 5f * scale);
        sr.setColor(0.22f, 0.74f, 0.98f, 1f);
        sr.circle(nx2, ny2, 2.5f * scale);

        // Amethyst magnetic coils
        sr.setColor(0.75f, 0.52f, 0.99f, 0.9f);
        for (float f = 0.20f; f <= 0.80f; f += 0.25f) {
            float mx = sx1 + (sx2 - sx1) * f;
            float my = sy1 + (sy2 - sy1) * f;
            sr.rectLine(mx - 4f * scale, my + 4f * scale, mx + 4f * scale, my - 4f * scale, Math.max(1.5f, 2f * scale));
        }

        // 4. Pommel Base & Counterweight (Bottom-Left)
        sr.setColor(0.12f, 0.11f, 0.29f, 1f);
        sr.circle(sx1 - 5f * scale, sy1 - 5f * scale, 7f * scale);
        sr.setColor(0.98f, 0.75f, 0.14f, 1f);
        sr.circle(sx1 - 5f * scale, sy1 - 5f * scale, 5f * scale);
        sr.setColor(0.22f, 0.74f, 0.98f, 1f);
        sr.circle(sx1 - 5f * scale, sy1 - 5f * scale, 3.2f * scale);

        // 5. Floating Antigravity Hourglass Crystal Finial (Top-Right)
        float fx = hx + 20f * scale;
        float fy = hy + 8f * scale;
        sr.setColor(0.22f, 0.74f, 0.98f, 0.95f);
        sr.triangle(fx - 4f * scale, fy + 8f * scale, fx + 4f * scale, fy + 8f * scale, fx, fy);
        sr.setColor(0.48f, 0.22f, 0.95f, 0.95f);
        sr.triangle(fx - 4f * scale, fy - 8f * scale, fx + 4f * scale, fy - 8f * scale, fx, fy);
        sr.setColor(0.98f, 0.75f, 0.14f, 1f);
        sr.rectLine(fx - 5f * scale, fy + 8f * scale, fx + 5f * scale, fy + 8f * scale, Math.max(1.5f, 1.6f * scale));
        sr.rectLine(fx - 5f * scale, fy - 8f * scale, fx + 5f * scale, fy - 8f * scale, Math.max(1.5f, 1.6f * scale));

        // 6. Brilliant Specular Starlight Glints
        sr.setColor(Color.WHITE);
        sr.circle(fx, fy + 9f * scale, 2f * scale);
        sr.circle(hx - 4f * scale, hy + 12f * scale, 2.2f * scale);
        sr.circle(nx2 - 2f * scale, ny2 + 2f * scale, 1.8f * scale);
        sr.circle(sx1 - 6f * scale, sy1 - 4f * scale, 1.5f * scale);
        sr.circle(cx - 16f * scale, cy + 24f * scale, 1.5f * scale);
    }

    public static void drawThoiKhongLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float scale = (r * 2f) / 128f;

        // 1. Spacetime Distortion Vortex Aura
        sr.setColor(0.48f, 0.22f, 0.95f, 0.18f);
        sr.circle(cx, cy, r * 0.95f);
        sr.setColor(0.22f, 0.74f, 0.98f, 0.16f);
        sr.circle(cx, cy, r * 0.78f);

        // 2. Celestial Astrolabe Spool Outer Gear Rim (Brass Gold)
        sr.setColor(0.85f, 0.47f, 0.02f, 1f);
        sr.circle(cx, cy, 42f * scale);
        sr.setColor(0.06f, 0.09f, 0.16f, 1f);
        sr.circle(cx, cy, 38f * scale);

        // 12 Precision Gear Notches along rim
        sr.setColor(0.98f, 0.75f, 0.14f, 1f);
        for (int i = 0; i < 12; i++) {
            float ang = i * 30f;
            float gx = cx + MathUtils.cosDeg(ang) * (42f * scale);
            float gy = cy + MathUtils.sinDeg(ang) * (42f * scale);
            sr.circle(gx, gy, 3f * scale);
        }

        // 3. Tachyon Spool Thread Core (Nested Winding Rings)
        sr.setColor(0.12f, 0.11f, 0.29f, 1f);
        sr.circle(cx, cy, 35f * scale);

        // Cyan Plasma Filament Coil
        sr.setColor(0.22f, 0.74f, 0.98f, 0.95f);
        sr.circle(cx, cy, 28f * scale);
        // Amethyst Secondary Coil
        sr.setColor(0.75f, 0.52f, 0.99f, 0.95f);
        sr.circle(cx, cy, 23f * scale);
        // Pure Starlight Inner Coil
        sr.setColor(0.99f, 0.94f, 0.54f, 0.95f);
        sr.circle(cx, cy, 18f * scale);

        // 4. Radial Astrolabe Sunburst Spokes (6 Gold Spokes)
        sr.setColor(0.98f, 0.75f, 0.14f, 0.95f);
        for (int s = 0; s < 6; s++) {
            float sAng = s * 60f;
            float sx1 = cx + MathUtils.cosDeg(sAng) * (18f * scale);
            float sy1 = cy + MathUtils.sinDeg(sAng) * (18f * scale);
            float sx2 = cx + MathUtils.cosDeg(sAng) * (36f * scale);
            float sy2 = cy + MathUtils.sinDeg(sAng) * (36f * scale);
            sr.rectLine(sx1, sy1, sx2, sy2, Math.max(1.8f, 2.2f * scale));
        }

        // 5. Spacetime Singularity Core & Event Horizon
        sr.setColor(0.01f, 0.03f, 0.07f, 1f);
        sr.circle(cx, cy, 13f * scale);
        sr.setColor(0.98f, 0.75f, 0.14f, 1f);
        sr.circle(cx, cy, 7f * scale);
        sr.setColor(Color.WHITE);
        sr.circle(cx, cy, 3.5f * scale);

        // 6. Transdimensional Infinity Ribbon (Mobius Warp Loops)
        sr.setColor(0.22f, 0.74f, 0.98f, 0.85f);
        sr.circle(cx - 14f * scale, cy + 8f * scale, 9f * scale);
        sr.setColor(0.12f, 0.11f, 0.29f, 1f);
        sr.circle(cx - 14f * scale, cy + 8f * scale, 6.5f * scale);

        sr.setColor(0.75f, 0.52f, 0.99f, 0.85f);
        sr.circle(cx + 14f * scale, cy - 8f * scale, 9f * scale);
        sr.setColor(0.12f, 0.11f, 0.29f, 1f);
        sr.circle(cx + 14f * scale, cy - 8f * scale, 6.5f * scale);

        // 7. Trailing Dimensional Warp Thread (Leading off spool)
        sr.setColor(0.22f, 0.74f, 0.98f, 1f);
        sr.rectLine(cx + 32f * scale, cy + 12f * scale, cx + 46f * scale, cy + 24f * scale, Math.max(1.8f, 2.5f * scale));
        sr.rectLine(cx + 46f * scale, cy + 24f * scale, cx + 58f * scale, cy + 38f * scale, Math.max(1.8f, 2.5f * scale));
        sr.setColor(Color.WHITE);
        sr.circle(cx + 58f * scale, cy + 38f * scale, 3f * scale);

        // 8. Brilliant Specular Glints
        sr.setColor(Color.WHITE);
        sr.circle(cx - 8f * scale, cy + 26f * scale, 2.5f * scale);
        sr.circle(cx + 10f * scale, cy - 24f * scale, 2.2f * scale);
        sr.circle(cx + 28f * scale, cy + 12f * scale, 1.8f * scale);
    }

    public static void drawThoiKhongHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float scale = (r * 2f) / 128f;

        // 1. Dimensional Void Singularity Aura
        sr.setColor(0.48f, 0.22f, 0.95f, 0.16f);
        sr.circle(cx, cy, r * 0.95f);
        sr.setColor(0.22f, 0.74f, 0.98f, 0.18f);
        sr.circle(cx - 8f * scale, cy + 4f * scale, r * 0.72f);

        // 2. Crown Eyelet: Chrono Clockwork Astrolabe Ring (Top)
        float ex = cx - 16f * scale;
        float ey = cy + 36f * scale;
        float eRad = 14f * scale;

        sr.setColor(0.85f, 0.47f, 0.02f, 1f);
        sr.circle(ex, ey, eRad);
        sr.setColor(0.06f, 0.09f, 0.16f, 1f);
        sr.circle(ex, ey, eRad - 2.5f * scale);

        // Glowing temporal jewel in eyelet
        sr.setColor(0.22f, 0.74f, 0.98f, 1f);
        sr.circle(ex, ey, 4.5f * scale);
        sr.setColor(Color.WHITE);
        sr.circle(ex, ey, 2f * scale);

        // Eyelet Gear Teeth (4 cardinal ticks)
        sr.setColor(0.98f, 0.75f, 0.14f, 1f);
        sr.circle(ex, ey + eRad + 1f * scale, 2.5f * scale);
        sr.circle(ex, ey - eRad - 1f * scale, 2.5f * scale);
        sr.circle(ex - eRad - 1f * scale, ey, 2.5f * scale);
        sr.circle(ex + eRad + 1f * scale, ey, 2.5f * scale);

        // Hanging Gravity Pendulum with Amethyst Diamond Gem (Left)
        float px = ex - 24f * scale;
        float py = ey - 32f * scale;
        sr.setColor(0.98f, 0.75f, 0.14f, 1f);
        sr.rectLine(ex - 8f * scale, ey - 8f * scale, px, py + 8f * scale, Math.max(1.5f, 1.6f * scale));

        // Faceted Amethyst Diamond
        sr.setColor(0.48f, 0.22f, 0.95f, 1f);
        sr.triangle(px, py + 10f * scale, px - 6f * scale, py, px + 6f * scale, py);
        sr.setColor(0.35f, 0.12f, 0.78f, 1f);
        sr.triangle(px - 6f * scale, py, px + 6f * scale, py, px, py - 10f * scale);
        sr.setColor(0.22f, 0.74f, 0.98f, 1f);
        sr.circle(px, py, 2.8f * scale);
        sr.setColor(Color.WHITE);
        sr.circle(px, py, 1.4f * scale);

        // 3. Heavy Void Obsidian Hook Body (Centered Hook Shank & Arc)
        float shankX = ex;
        float shankTopY = ey - eRad;
        float shankBotY = cy - 12f * scale;
        float bendApexX = cx + 16f * scale;
        float bendApexY = cy - 42f * scale;
        float barbX = cx + 42f * scale;
        float barbY = cy - 12f * scale;
        float tipX = cx + 42f * scale;
        float tipY = cy + 18f * scale;

        // Dark matter under-shadow spine
        sr.setColor(0.01f, 0.03f, 0.07f, 1f);
        sr.rectLine(shankX, shankTopY, shankX, shankBotY, Math.max(4.5f, 8f * scale));
        sr.rectLine(shankX, shankBotY, bendApexX, bendApexY, Math.max(4.5f, 8f * scale));
        sr.rectLine(bendApexX, bendApexY, barbX, barbY, Math.max(4.5f, 8f * scale));
        sr.rectLine(barbX, barbY, tipX, tipY, Math.max(4.5f, 7f * scale));

        // Void obsidian casing
        sr.setColor(0.12f, 0.11f, 0.29f, 1f);
        sr.rectLine(shankX, shankTopY, shankX, shankBotY, Math.max(3f, 5f * scale));
        sr.rectLine(shankX, shankBotY, bendApexX, bendApexY, Math.max(3f, 5f * scale));
        sr.rectLine(bendApexX, bendApexY, barbX, barbY, Math.max(3f, 5f * scale));
        sr.rectLine(barbX, barbY, tipX, tipY, Math.max(3f, 4.5f * scale));

        // Luminous Chrono Conduit Center
        sr.setColor(0.22f, 0.74f, 0.98f, 0.95f);
        sr.rectLine(shankX, shankTopY, shankX, shankBotY, Math.max(1.5f, 2f * scale));
        sr.rectLine(shankX, shankBotY, bendApexX, bendApexY, Math.max(1.5f, 2f * scale));
        sr.rectLine(bendApexX, bendApexY, barbX, barbY, Math.max(1.5f, 2f * scale));
        sr.rectLine(barbX, barbY, tipX, tipY, Math.max(1.5f, 2f * scale));

        // 4. Luminous Chrono Runes on Shank
        for (int i = 0; i < 3; i++) {
            float ry = shankTopY - 10f * scale - i * 12f * scale;
            sr.setColor(0.22f, 0.74f, 0.98f, 1f);
            sr.circle(shankX, ry, 3.5f * scale);
            sr.setColor(Color.WHITE);
            sr.circle(shankX, ry, 1.6f * scale);
        }

        // 5. Dimensional Rift Crescent Barb & Razor Hook Point
        sr.setColor(0.65f, 0.33f, 0.97f, 1f);
        sr.triangle(barbX, barbY, barbX - 16f * scale, barbY - 6f * scale, barbX, barbY - 18f * scale);
        sr.setColor(Color.WHITE);
        sr.rectLine(barbX, barbY, barbX - 16f * scale, barbY - 6f * scale, Math.max(1.2f, 1.5f * scale));

        // Razor Sharp Singularity Hook Tip (Gold & Diamond Point)
        sr.setColor(0.98f, 0.75f, 0.14f, 1f);
        sr.triangle(tipX - 4f * scale, tipY - 8f * scale, tipX + 4f * scale, tipY - 8f * scale, tipX, tipY + 12f * scale);
        sr.setColor(Color.WHITE);
        sr.circle(tipX, tipY + 12f * scale, 2.5f * scale);

        // 6. Accretion Swirl & Specular Glints
        sr.setColor(Color.WHITE);
        sr.circle(bendApexX, bendApexY, 2.5f * scale);
        sr.circle(barbX, barbY, 2.2f * scale);
        sr.circle(ex - 4f * scale, ey + 4f * scale, 2f * scale);
        sr.circle(px - 2f * scale, py + 2f * scale, 1.5f * scale);
    }

    // =========================================================================
    // 9. QUANG MINH SET (Formerly Thần Thoại: Solar Deity Scepter, Sun-Thread Spool, Archangel Hook)
    // =========================================================================
    public static void drawQuangMinhRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        sr.setColor(0.98f, 0.80f, 0.08f, 0.16f);
        fillCircle(sr, cx, cy, s, 64.0f, 40.0f, 50.0f);
        sr.setColor(1.00f, 0.94f, 0.54f, 0.22f);
        fillCircle(sr, cx, cy, s, 64.0f, 40.0f, 34.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 0.10f);
        fillCircle(sr, cx, cy, s, 64.0f, 80.0f, 26.0f);
        sr.setColor(0.47f, 0.21f, 0.06f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 116.0f, 64.0f, 34.0f, 8.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 116.0f, 64.0f, 34.0f, 5.5f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 114.0f, 64.0f, 36.0f, 3.2f);
        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 64.0f, 114.0f, 64.0f, 36.0f, 1.2f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 94.0f, 5.0f);
        sr.setColor(0.92f, 0.35f, 0.05f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 94.0f, 2.8f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 63.0f, 93.0f, 1.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 72.0f, 5.2f);
        sr.setColor(0.92f, 0.35f, 0.05f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 72.0f, 3.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 63.0f, 71.0f, 1.1f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        fillQuad(sr, cx, cy, s, 58.0f, 98.0f, 70.0f, 98.0f, 70.0f, 112.0f, 58.0f, 112.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        drawLine(sr, cx, cy, s, 59.0f, 102.0f, 69.0f, 102.0f, 1.2f);
        drawLine(sr, cx, cy, s, 59.0f, 106.0f, 69.0f, 106.0f, 1.2f);
        fillTri(sr, cx, cy, s, 64.0f, 124.0f, 57.0f, 114.0f, 71.0f, 114.0f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 123.0f, 2.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 64.0f, 123.0f, 0.8f);
        sr.setColor(0.99f, 0.88f, 0.28f, 1.00f);
        fillQuad(sr, cx, cy, s, 64.0f, 48.0f, 16.0f, 30.0f, 24.0f, 44.0f, 48.0f, 52.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        fillQuad(sr, cx, cy, s, 64.0f, 48.0f, 24.0f, 44.0f, 32.0f, 58.0f, 54.0f, 54.0f);
        sr.setColor(Color.WHITE);
        fillQuad(sr, cx, cy, s, 64.0f, 47.0f, 22.0f, 34.0f, 30.0f, 44.0f, 50.0f, 50.0f);
        sr.setColor(0.99f, 0.88f, 0.28f, 1.00f);
        fillQuad(sr, cx, cy, s, 64.0f, 48.0f, 112.0f, 30.0f, 104.0f, 44.0f, 80.0f, 52.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        fillQuad(sr, cx, cy, s, 64.0f, 48.0f, 104.0f, 44.0f, 96.0f, 58.0f, 74.0f, 54.0f);
        sr.setColor(Color.WHITE);
        fillQuad(sr, cx, cy, s, 64.0f, 47.0f, 106.0f, 34.0f, 98.0f, 44.0f, 78.0f, 50.0f);
        sr.setColor(0.47f, 0.21f, 0.06f, 1.00f);
        drawLine(sr, cx, cy, s, 42.0f, 44.0f, 86.0f, 44.0f, 5.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        drawLine(sr, cx, cy, s, 42.0f, 44.0f, 86.0f, 44.0f, 3.0f);
        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 44.0f, 44.0f, 84.0f, 44.0f, 1.0f);
        sr.setColor(0.92f, 0.35f, 0.05f, 1.00f);
        fillCircle(sr, cx, cy, s, 42.0f, 44.0f, 3.2f);
        fillCircle(sr, cx, cy, s, 86.0f, 44.0f, 3.2f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillTri(sr, cx, cy, s, 64.0f, 8.0f, 60.0f, 24.0f, 68.0f, 24.0f);
        fillTri(sr, cx, cy, s, 48.0f, 24.0f, 64.0f, 20.0f, 64.0f, 28.0f);
        fillTri(sr, cx, cy, s, 80.0f, 24.0f, 64.0f, 20.0f, 64.0f, 28.0f);
        fillTri(sr, cx, cy, s, 64.0f, 40.0f, 60.0f, 24.0f, 68.0f, 24.0f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        fillQuad(sr, cx, cy, s, 64.0f, 16.0f, 71.0f, 24.0f, 64.0f, 32.0f, 57.0f, 24.0f);
        sr.setColor(Color.WHITE);
        fillQuad(sr, cx, cy, s, 64.0f, 18.0f, 69.0f, 24.0f, 64.0f, 30.0f, 59.0f, 24.0f);
        fillCircle(sr, cx, cy, s, 63.0f, 22.0f, 1.8f);
        fillCircle(sr, cx, cy, s, 18.0f, 31.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 110.0f, 31.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 42.0f, 43.0f, 1.0f);
        fillCircle(sr, cx, cy, s, 86.0f, 43.0f, 1.0f);
        fillCircle(sr, cx, cy, s, 64.0f, 56.0f, 1.4f);
    }

    public static void drawQuangMinhLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        sr.setColor(0.98f, 0.80f, 0.08f, 0.16f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 54.0f);
        sr.setColor(1.00f, 0.94f, 0.54f, 0.22f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 40.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillQuad(sr, cx, cy, s, 64.0f, 12.0f, 52.0f, 28.0f, 64.0f, 36.0f, 76.0f, 28.0f);
        sr.setColor(Color.WHITE);
        fillQuad(sr, cx, cy, s, 64.0f, 15.0f, 56.0f, 27.0f, 64.0f, 33.0f, 72.0f, 27.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillQuad(sr, cx, cy, s, 64.0f, 116.0f, 52.0f, 100.0f, 64.0f, 92.0f, 76.0f, 100.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        fillQuad(sr, cx, cy, s, 64.0f, 113.0f, 56.0f, 101.0f, 64.0f, 95.0f, 72.0f, 101.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillQuad(sr, cx, cy, s, 12.0f, 64.0f, 28.0f, 52.0f, 36.0f, 64.0f, 28.0f, 76.0f);
        sr.setColor(Color.WHITE);
        fillQuad(sr, cx, cy, s, 15.0f, 64.0f, 27.0f, 56.0f, 33.0f, 64.0f, 27.0f, 72.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillQuad(sr, cx, cy, s, 116.0f, 64.0f, 100.0f, 52.0f, 92.0f, 64.0f, 100.0f, 76.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        fillQuad(sr, cx, cy, s, 113.0f, 64.0f, 101.0f, 56.0f, 95.0f, 64.0f, 101.0f, 72.0f);
        sr.setColor(0.99f, 0.88f, 0.28f, 1.00f);
        fillQuad(sr, cx, cy, s, 50.0f, 28.0f, 28.0f, 50.0f, 36.0f, 54.0f, 54.0f, 34.0f);
        sr.setColor(Color.WHITE);
        fillQuad(sr, cx, cy, s, 48.0f, 30.0f, 32.0f, 48.0f, 36.0f, 50.0f, 50.0f, 34.0f);
        sr.setColor(0.99f, 0.88f, 0.28f, 1.00f);
        fillQuad(sr, cx, cy, s, 78.0f, 28.0f, 100.0f, 50.0f, 92.0f, 54.0f, 74.0f, 34.0f);
        sr.setColor(Color.WHITE);
        fillQuad(sr, cx, cy, s, 80.0f, 30.0f, 96.0f, 48.0f, 92.0f, 50.0f, 78.0f, 34.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        fillQuad(sr, cx, cy, s, 28.0f, 78.0f, 50.0f, 100.0f, 54.0f, 94.0f, 36.0f, 74.0f);
        fillQuad(sr, cx, cy, s, 100.0f, 78.0f, 78.0f, 100.0f, 74.0f, 94.0f, 92.0f, 74.0f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 26.0f, 102.0f, 64.0f, 1.6f);
        drawLine(sr, cx, cy, s, 102.0f, 64.0f, 64.0f, 102.0f, 1.6f);
        drawLine(sr, cx, cy, s, 64.0f, 102.0f, 26.0f, 64.0f, 1.6f);
        drawLine(sr, cx, cy, s, 26.0f, 64.0f, 64.0f, 26.0f, 1.6f);
        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 40.0f, 40.0f, 88.0f, 88.0f, 1.0f);
        drawLine(sr, cx, cy, s, 88.0f, 40.0f, 40.0f, 88.0f, 1.0f);
        sr.setColor(0.47f, 0.21f, 0.06f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 22.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 18.0f);
        sr.setColor(0.92f, 0.35f, 0.05f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 14.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 9.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 62.0f, 62.0f, 4.5f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 116.0f, 64.0f, 124.0f, 1.5f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 124.0f, 3.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 63.5f, 123.5f, 1.0f);
        fillCircle(sr, cx, cy, s, 64.0f, 14.0f, 1.6f);
        fillCircle(sr, cx, cy, s, 14.0f, 64.0f, 1.6f);
        fillCircle(sr, cx, cy, s, 114.0f, 64.0f, 1.6f);
        fillCircle(sr, cx, cy, s, 64.0f, 60.0f, 2.0f);
        fillCircle(sr, cx, cy, s, 50.0f, 30.0f, 1.2f);
        fillCircle(sr, cx, cy, s, 78.0f, 30.0f, 1.2f);
    }

    public static void drawQuangMinhHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        sr.setColor(0.98f, 0.80f, 0.08f, 0.16f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 54.0f);
        sr.setColor(1.00f, 0.94f, 0.54f, 0.22f);
        fillCircle(sr, cx, cy, s, 64.0f, 40.0f, 32.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 0.14f);
        fillCircle(sr, cx, cy, s, 64.0f, 90.0f, 30.0f);
        sr.setColor(0.47f, 0.21f, 0.06f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 22.0f, 8.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 22.0f, 5.5f);
        sr.setColor(0.06f, 0.09f, 0.16f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 22.0f, 3.0f);
        sr.setColor(0.99f, 0.88f, 0.28f, 1.00f);
        fillQuad(sr, cx, cy, s, 64.0f, 26.0f, 36.0f, 12.0f, 44.0f, 28.0f, 62.0f, 32.0f);
        sr.setColor(Color.WHITE);
        fillQuad(sr, cx, cy, s, 64.0f, 26.0f, 38.0f, 16.0f, 44.0f, 24.0f, 62.0f, 30.0f);
        sr.setColor(0.99f, 0.88f, 0.28f, 1.00f);
        fillQuad(sr, cx, cy, s, 64.0f, 26.0f, 92.0f, 12.0f, 84.0f, 28.0f, 66.0f, 32.0f);
        sr.setColor(Color.WHITE);
        fillQuad(sr, cx, cy, s, 64.0f, 26.0f, 90.0f, 16.0f, 84.0f, 24.0f, 66.0f, 30.0f);
        sr.setColor(0.47f, 0.21f, 0.06f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 28.0f, 64.0f, 88.0f, 8.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 28.0f, 64.0f, 88.0f, 5.5f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 28.0f, 64.0f, 88.0f, 3.0f);
        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 64.0f, 30.0f, 64.0f, 86.0f, 1.2f);
        sr.setColor(0.92f, 0.35f, 0.05f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 44.0f, 3.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 63.5f, 43.5f, 1.0f);
        sr.setColor(0.92f, 0.35f, 0.05f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 3.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 63.5f, 63.5f, 1.0f);
        sr.setColor(0.47f, 0.21f, 0.06f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 88.0f, 62.0f, 98.8f, 7.5f);
        drawLine(sr, cx, cy, s, 62.0f, 98.8f, 56.8f, 105.2f, 7.5f);
        drawLine(sr, cx, cy, s, 56.8f, 105.2f, 49.3f, 107.3f, 7.5f);
        drawLine(sr, cx, cy, s, 49.3f, 107.3f, 40.7f, 105.5f, 7.5f);
        drawLine(sr, cx, cy, s, 40.7f, 105.5f, 32.0f, 100.0f, 7.5f);
        drawLine(sr, cx, cy, s, 32.0f, 100.0f, 27.1f, 93.6f, 7.5f);
        drawLine(sr, cx, cy, s, 27.1f, 93.6f, 24.2f, 86.3f, 7.5f);
        drawLine(sr, cx, cy, s, 24.2f, 86.3f, 22.6f, 78.5f, 7.5f);
        drawLine(sr, cx, cy, s, 22.6f, 78.5f, 22.1f, 70.4f, 7.5f);
        drawLine(sr, cx, cy, s, 22.1f, 70.4f, 22.0f, 62.0f, 7.5f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 88.0f, 62.0f, 98.8f, 5.0f);
        drawLine(sr, cx, cy, s, 62.0f, 98.8f, 56.8f, 105.2f, 5.0f);
        drawLine(sr, cx, cy, s, 56.8f, 105.2f, 49.3f, 107.3f, 5.0f);
        drawLine(sr, cx, cy, s, 49.3f, 107.3f, 40.7f, 105.5f, 5.0f);
        drawLine(sr, cx, cy, s, 40.7f, 105.5f, 32.0f, 100.0f, 5.0f);
        drawLine(sr, cx, cy, s, 32.0f, 100.0f, 27.1f, 93.6f, 5.0f);
        drawLine(sr, cx, cy, s, 27.1f, 93.6f, 24.2f, 86.3f, 5.0f);
        drawLine(sr, cx, cy, s, 24.2f, 86.3f, 22.6f, 78.5f, 5.0f);
        drawLine(sr, cx, cy, s, 22.6f, 78.5f, 22.1f, 70.4f, 5.0f);
        drawLine(sr, cx, cy, s, 22.1f, 70.4f, 22.0f, 62.0f, 5.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 88.0f, 62.0f, 98.8f, 2.6f);
        drawLine(sr, cx, cy, s, 62.0f, 98.8f, 56.8f, 105.2f, 2.6f);
        drawLine(sr, cx, cy, s, 56.8f, 105.2f, 49.3f, 107.3f, 2.6f);
        drawLine(sr, cx, cy, s, 49.3f, 107.3f, 40.7f, 105.5f, 2.6f);
        drawLine(sr, cx, cy, s, 40.7f, 105.5f, 32.0f, 100.0f, 2.6f);
        drawLine(sr, cx, cy, s, 32.0f, 100.0f, 27.1f, 93.6f, 2.6f);
        drawLine(sr, cx, cy, s, 27.1f, 93.6f, 24.2f, 86.3f, 2.6f);
        drawLine(sr, cx, cy, s, 24.2f, 86.3f, 22.6f, 78.5f, 2.6f);
        drawLine(sr, cx, cy, s, 22.6f, 78.5f, 22.1f, 70.4f, 2.6f);
        drawLine(sr, cx, cy, s, 22.1f, 70.4f, 22.0f, 62.0f, 2.6f);
        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 64.0f, 88.0f, 62.1f, 97.9f, 1.0f);
        drawLine(sr, cx, cy, s, 62.1f, 97.9f, 57.1f, 103.7f, 1.0f);
        drawLine(sr, cx, cy, s, 57.1f, 103.7f, 50.0f, 105.6f, 1.0f);
        drawLine(sr, cx, cy, s, 50.0f, 105.6f, 41.6f, 104.0f, 1.0f);
        drawLine(sr, cx, cy, s, 41.6f, 104.0f, 33.0f, 99.0f, 1.0f);
        drawLine(sr, cx, cy, s, 33.0f, 99.0f, 28.6f, 93.1f, 1.0f);
        drawLine(sr, cx, cy, s, 28.6f, 93.1f, 25.9f, 86.2f, 1.0f);
        drawLine(sr, cx, cy, s, 25.9f, 86.2f, 24.6f, 78.9f, 1.0f);
        drawLine(sr, cx, cy, s, 24.6f, 78.9f, 24.1f, 71.4f, 1.0f);
        drawLine(sr, cx, cy, s, 24.1f, 71.4f, 24.0f, 64.0f, 1.0f);
        sr.setColor(0.47f, 0.21f, 0.06f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 88.0f, 66.0f, 98.8f, 7.5f);
        drawLine(sr, cx, cy, s, 66.0f, 98.8f, 71.2f, 105.2f, 7.5f);
        drawLine(sr, cx, cy, s, 71.2f, 105.2f, 78.7f, 107.3f, 7.5f);
        drawLine(sr, cx, cy, s, 78.7f, 107.3f, 87.3f, 105.5f, 7.5f);
        drawLine(sr, cx, cy, s, 87.3f, 105.5f, 96.0f, 100.0f, 7.5f);
        drawLine(sr, cx, cy, s, 96.0f, 100.0f, 100.9f, 93.6f, 7.5f);
        drawLine(sr, cx, cy, s, 100.9f, 93.6f, 103.8f, 86.3f, 7.5f);
        drawLine(sr, cx, cy, s, 103.8f, 86.3f, 105.4f, 78.5f, 7.5f);
        drawLine(sr, cx, cy, s, 105.4f, 78.5f, 105.9f, 70.4f, 7.5f);
        drawLine(sr, cx, cy, s, 105.9f, 70.4f, 106.0f, 62.0f, 7.5f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 88.0f, 66.0f, 98.8f, 5.0f);
        drawLine(sr, cx, cy, s, 66.0f, 98.8f, 71.2f, 105.2f, 5.0f);
        drawLine(sr, cx, cy, s, 71.2f, 105.2f, 78.7f, 107.3f, 5.0f);
        drawLine(sr, cx, cy, s, 78.7f, 107.3f, 87.3f, 105.5f, 5.0f);
        drawLine(sr, cx, cy, s, 87.3f, 105.5f, 96.0f, 100.0f, 5.0f);
        drawLine(sr, cx, cy, s, 96.0f, 100.0f, 100.9f, 93.6f, 5.0f);
        drawLine(sr, cx, cy, s, 100.9f, 93.6f, 103.8f, 86.3f, 5.0f);
        drawLine(sr, cx, cy, s, 103.8f, 86.3f, 105.4f, 78.5f, 5.0f);
        drawLine(sr, cx, cy, s, 105.4f, 78.5f, 105.9f, 70.4f, 5.0f);
        drawLine(sr, cx, cy, s, 105.9f, 70.4f, 106.0f, 62.0f, 5.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 88.0f, 66.0f, 98.8f, 2.6f);
        drawLine(sr, cx, cy, s, 66.0f, 98.8f, 71.2f, 105.2f, 2.6f);
        drawLine(sr, cx, cy, s, 71.2f, 105.2f, 78.7f, 107.3f, 2.6f);
        drawLine(sr, cx, cy, s, 78.7f, 107.3f, 87.3f, 105.5f, 2.6f);
        drawLine(sr, cx, cy, s, 87.3f, 105.5f, 96.0f, 100.0f, 2.6f);
        drawLine(sr, cx, cy, s, 96.0f, 100.0f, 100.9f, 93.6f, 2.6f);
        drawLine(sr, cx, cy, s, 100.9f, 93.6f, 103.8f, 86.3f, 2.6f);
        drawLine(sr, cx, cy, s, 103.8f, 86.3f, 105.4f, 78.5f, 2.6f);
        drawLine(sr, cx, cy, s, 105.4f, 78.5f, 105.9f, 70.4f, 2.6f);
        drawLine(sr, cx, cy, s, 105.9f, 70.4f, 106.0f, 62.0f, 2.6f);
        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 64.0f, 88.0f, 65.9f, 97.9f, 1.0f);
        drawLine(sr, cx, cy, s, 65.9f, 97.9f, 70.9f, 103.7f, 1.0f);
        drawLine(sr, cx, cy, s, 70.9f, 103.7f, 78.0f, 105.6f, 1.0f);
        drawLine(sr, cx, cy, s, 78.0f, 105.6f, 86.4f, 104.0f, 1.0f);
        drawLine(sr, cx, cy, s, 86.4f, 104.0f, 95.0f, 99.0f, 1.0f);
        drawLine(sr, cx, cy, s, 95.0f, 99.0f, 99.4f, 93.1f, 1.0f);
        drawLine(sr, cx, cy, s, 99.4f, 93.1f, 102.1f, 86.2f, 1.0f);
        drawLine(sr, cx, cy, s, 102.1f, 86.2f, 103.4f, 78.9f, 1.0f);
        drawLine(sr, cx, cy, s, 103.4f, 78.9f, 103.9f, 71.4f, 1.0f);
        drawLine(sr, cx, cy, s, 103.9f, 71.4f, 104.0f, 64.0f, 1.0f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        fillTri(sr, cx, cy, s, 22.0f, 50.0f, 14.0f, 64.0f, 22.0f, 62.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        fillTri(sr, cx, cy, s, 22.0f, 50.0f, 30.0f, 64.0f, 22.0f, 62.0f);
        sr.setColor(Color.WHITE);
        fillTri(sr, cx, cy, s, 22.0f, 51.0f, 17.0f, 63.0f, 22.0f, 61.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillQuad(sr, cx, cy, s, 22.0f, 72.0f, 30.0f, 78.0f, 28.0f, 82.0f, 22.0f, 78.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        fillTri(sr, cx, cy, s, 106.0f, 50.0f, 98.0f, 64.0f, 106.0f, 62.0f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        fillTri(sr, cx, cy, s, 106.0f, 50.0f, 114.0f, 64.0f, 106.0f, 62.0f);
        sr.setColor(Color.WHITE);
        fillTri(sr, cx, cy, s, 106.0f, 51.0f, 111.0f, 63.0f, 106.0f, 61.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillQuad(sr, cx, cy, s, 106.0f, 72.0f, 98.0f, 78.0f, 100.0f, 82.0f, 106.0f, 78.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 64.0f, 14.0f, 1.6f);
        fillCircle(sr, cx, cy, s, 22.0f, 52.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 106.0f, 52.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 64.0f, 38.0f, 1.2f);
        fillCircle(sr, cx, cy, s, 32.0f, 104.0f, 1.4f);
        fillCircle(sr, cx, cy, s, 96.0f, 104.0f, 1.4f);
    }

    // Compatibility aliases
    private static void drawThanThoaiRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        drawQuangMinhRod(sr, cx, cy, r, col);
    }
    private static void drawThanThoaiLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        drawQuangMinhLine(sr, cx, cy, r, col);
    }
    private static void drawThanThoaiHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        drawQuangMinhHook(sr, cx, cy, r, col);
    }

    // =========================================================================
    // 10. VỆ QUỐC VANG DANH SET
    // =========================================================================
    // =========================================================================
    // 10. VỆ QUỐC VANG DANH SET
    // =========================================================================
    public static void drawVeQuocRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;
        sr.setColor(0.86f, 0.15f, 0.15f, 0.16f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 54.0f);
        sr.setColor(0.92f, 0.70f, 0.03f, 0.14f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 42.0f);
        sr.setColor(0.27f, 0.04f, 0.04f, 1.00f);
        drawLine(sr, cx, cy, s, 26.0f, 102.0f, 88.0f, 40.0f, 7.5f);
        sr.setColor(0.60f, 0.11f, 0.11f, 1.00f);
        drawLine(sr, cx, cy, s, 26.0f, 102.0f, 88.0f, 40.0f, 5.2f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        drawLine(sr, cx, cy, s, 26.0f, 102.0f, 88.0f, 40.0f, 2.8f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        drawLine(sr, cx, cy, s, 27.0f, 101.0f, 87.0f, 41.0f, 1.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        fillCircle(sr, cx, cy, s, 42.0f, 86.0f, 4.5f);
        sr.setColor(0.27f, 0.04f, 0.04f, 1.00f);
        fillCircle(sr, cx, cy, s, 42.0f, 86.0f, 4.5f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        fillTri(sr, cx, cy, s, 42.0f, 83.0f, 43.0f, 85.0f, 45.0f, 85.0f);
        fillTri(sr, cx, cy, s, 42.0f, 83.0f, 45.0f, 85.0f, 43.5f, 86.5f);
        fillTri(sr, cx, cy, s, 42.0f, 83.0f, 43.5f, 86.5f, 44.0f, 89.0f);
        fillTri(sr, cx, cy, s, 42.0f, 83.0f, 44.0f, 89.0f, 42.0f, 87.5f);
        fillTri(sr, cx, cy, s, 42.0f, 83.0f, 42.0f, 87.5f, 40.0f, 89.0f);
        fillTri(sr, cx, cy, s, 42.0f, 83.0f, 40.0f, 89.0f, 40.5f, 86.5f);
        fillTri(sr, cx, cy, s, 42.0f, 83.0f, 40.5f, 86.5f, 39.0f, 85.0f);
        fillTri(sr, cx, cy, s, 42.0f, 83.0f, 39.0f, 85.0f, 41.0f, 85.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        fillCircle(sr, cx, cy, s, 58.0f, 70.0f, 4.8f);
        sr.setColor(0.27f, 0.04f, 0.04f, 1.00f);
        fillCircle(sr, cx, cy, s, 58.0f, 70.0f, 4.8f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        fillTri(sr, cx, cy, s, 58.0f, 67.0f, 59.0f, 69.0f, 61.0f, 69.0f);
        fillTri(sr, cx, cy, s, 58.0f, 67.0f, 61.0f, 69.0f, 59.5f, 70.5f);
        fillTri(sr, cx, cy, s, 58.0f, 67.0f, 59.5f, 70.5f, 60.0f, 73.0f);
        fillTri(sr, cx, cy, s, 58.0f, 67.0f, 60.0f, 73.0f, 58.0f, 71.5f);
        fillTri(sr, cx, cy, s, 58.0f, 67.0f, 58.0f, 71.5f, 56.0f, 73.0f);
        fillTri(sr, cx, cy, s, 58.0f, 67.0f, 56.0f, 73.0f, 56.5f, 70.5f);
        fillTri(sr, cx, cy, s, 58.0f, 67.0f, 56.5f, 70.5f, 55.0f, 69.0f);
        fillTri(sr, cx, cy, s, 58.0f, 67.0f, 55.0f, 69.0f, 57.0f, 69.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        fillCircle(sr, cx, cy, s, 74.0f, 54.0f, 5.0f);
        sr.setColor(0.27f, 0.04f, 0.04f, 1.00f);
        fillCircle(sr, cx, cy, s, 74.0f, 54.0f, 5.0f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        fillTri(sr, cx, cy, s, 74.0f, 51.0f, 75.0f, 53.0f, 77.0f, 53.0f);
        fillTri(sr, cx, cy, s, 74.0f, 51.0f, 77.0f, 53.0f, 75.5f, 54.5f);
        fillTri(sr, cx, cy, s, 74.0f, 51.0f, 75.5f, 54.5f, 76.0f, 57.0f);
        fillTri(sr, cx, cy, s, 74.0f, 51.0f, 76.0f, 57.0f, 74.0f, 55.5f);
        fillTri(sr, cx, cy, s, 74.0f, 51.0f, 74.0f, 55.5f, 72.0f, 57.0f);
        fillTri(sr, cx, cy, s, 74.0f, 51.0f, 72.0f, 57.0f, 72.5f, 54.5f);
        fillTri(sr, cx, cy, s, 74.0f, 51.0f, 72.5f, 54.5f, 71.0f, 53.0f);
        fillTri(sr, cx, cy, s, 74.0f, 51.0f, 71.0f, 53.0f, 73.0f, 53.0f);
        sr.setColor(0.50f, 0.11f, 0.11f, 1.00f);
        fillQuad(sr, cx, cy, s, 22.0f, 94.0f, 38.0f, 94.0f, 38.0f, 104.0f, 22.0f, 104.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        drawLine(sr, cx, cy, s, 24.0f, 101.0f, 33.0f, 92.0f, 1.2f);
        drawLine(sr, cx, cy, s, 27.0f, 104.0f, 36.0f, 95.0f, 1.2f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        fillCircle(sr, cx, cy, s, 21.0f, 107.0f, 4.2f);
        sr.setColor(0.27f, 0.04f, 0.04f, 1.00f);
        fillCircle(sr, cx, cy, s, 21.0f, 107.0f, 4.2f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        fillQuad(sr, cx, cy, s, 21.0f, 107.0f, 15.0f, 116.0f, 19.0f, 118.0f, 21.0f, 110.0f);
        sr.setColor(0.94f, 0.27f, 0.27f, 1.00f);
        fillQuad(sr, cx, cy, s, 21.0f, 107.0f, 19.0f, 118.0f, 24.0f, 116.0f, 21.0f, 110.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        fillTri(sr, cx, cy, s, 88.0f, 40.0f, 122.0f, 32.0f, 110.0f, 46.0f);
        fillTri(sr, cx, cy, s, 88.0f, 40.0f, 110.0f, 46.0f, 124.0f, 54.0f);
        fillTri(sr, cx, cy, s, 88.0f, 40.0f, 124.0f, 54.0f, 88.0f, 48.0f);
        sr.setColor(0.94f, 0.27f, 0.27f, 1.00f);
        fillTri(sr, cx, cy, s, 88.0f, 42.0f, 114.0f, 35.0f, 106.0f, 46.0f);
        fillTri(sr, cx, cy, s, 88.0f, 42.0f, 106.0f, 46.0f, 118.0f, 52.0f);
        fillTri(sr, cx, cy, s, 88.0f, 42.0f, 118.0f, 52.0f, 88.0f, 47.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillTri(sr, cx, cy, s, 104.0f, 39.0f, 105.5f, 42.5f, 109.0f, 43.0f);
        fillTri(sr, cx, cy, s, 104.0f, 39.0f, 109.0f, 43.0f, 106.5f, 45.5f);
        fillTri(sr, cx, cy, s, 104.0f, 39.0f, 106.5f, 45.5f, 107.5f, 49.0f);
        fillTri(sr, cx, cy, s, 104.0f, 39.0f, 107.5f, 49.0f, 104.0f, 47.0f);
        fillTri(sr, cx, cy, s, 104.0f, 39.0f, 104.0f, 47.0f, 100.5f, 49.0f);
        fillTri(sr, cx, cy, s, 104.0f, 39.0f, 100.5f, 49.0f, 101.5f, 45.5f);
        fillTri(sr, cx, cy, s, 104.0f, 39.0f, 101.5f, 45.5f, 99.0f, 43.0f);
        fillTri(sr, cx, cy, s, 104.0f, 39.0f, 99.0f, 43.0f, 102.5f, 42.5f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        fillCircle(sr, cx, cy, s, 88.0f, 40.0f, 3.5f);
        sr.setColor(0.27f, 0.04f, 0.04f, 1.00f);
        fillCircle(sr, cx, cy, s, 88.0f, 40.0f, 3.5f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        fillCircle(sr, cx, cy, s, 88.0f, 40.0f, 1.8f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 104.0f, 42.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 88.0f, 38.0f, 1.4f);
        fillCircle(sr, cx, cy, s, 74.0f, 52.0f, 1.2f);
        fillCircle(sr, cx, cy, s, 21.0f, 105.0f, 1.2f);
    }

    public static void drawVeQuocLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        sr.setColor(0.86f, 0.15f, 0.15f, 0.16f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 54.0f);
        sr.setColor(0.96f, 0.62f, 0.04f, 0.20f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 42.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        fillTri(sr, cx, cy, s, 64.0f, 10.0f, 58.0f, 24.0f, 70.0f, 24.0f);
        fillTri(sr, cx, cy, s, 118.0f, 64.0f, 104.0f, 58.0f, 104.0f, 70.0f);
        fillTri(sr, cx, cy, s, 64.0f, 118.0f, 70.0f, 104.0f, 58.0f, 104.0f);
        fillTri(sr, cx, cy, s, 10.0f, 64.0f, 24.0f, 70.0f, 24.0f, 58.0f);
        fillTri(sr, cx, cy, s, 102.0f, 26.0f, 89.0f, 34.0f, 97.0f, 42.0f);
        fillTri(sr, cx, cy, s, 102.0f, 102.0f, 97.0f, 86.0f, 89.0f, 94.0f);
        fillTri(sr, cx, cy, s, 26.0f, 102.0f, 39.0f, 94.0f, 31.0f, 86.0f);
        fillTri(sr, cx, cy, s, 26.0f, 26.0f, 31.0f, 42.0f, 39.0f, 34.0f);
        sr.setColor(0.47f, 0.21f, 0.06f, 1.00f);
        fillTri(sr, cx, cy, s, 39.0f, 34.0f, 58.0f, 24.0f, 70.0f, 24.0f);
        fillTri(sr, cx, cy, s, 39.0f, 34.0f, 70.0f, 24.0f, 89.0f, 34.0f);
        fillTri(sr, cx, cy, s, 39.0f, 34.0f, 89.0f, 34.0f, 97.0f, 42.0f);
        fillTri(sr, cx, cy, s, 39.0f, 34.0f, 97.0f, 42.0f, 104.0f, 58.0f);
        fillTri(sr, cx, cy, s, 39.0f, 34.0f, 104.0f, 58.0f, 104.0f, 70.0f);
        fillTri(sr, cx, cy, s, 39.0f, 34.0f, 104.0f, 70.0f, 97.0f, 86.0f);
        fillTri(sr, cx, cy, s, 39.0f, 34.0f, 97.0f, 86.0f, 89.0f, 94.0f);
        fillTri(sr, cx, cy, s, 39.0f, 34.0f, 89.0f, 94.0f, 70.0f, 104.0f);
        fillTri(sr, cx, cy, s, 39.0f, 34.0f, 70.0f, 104.0f, 58.0f, 104.0f);
        fillTri(sr, cx, cy, s, 39.0f, 34.0f, 58.0f, 104.0f, 39.0f, 94.0f);
        fillTri(sr, cx, cy, s, 39.0f, 34.0f, 39.0f, 94.0f, 31.0f, 86.0f);
        fillTri(sr, cx, cy, s, 39.0f, 34.0f, 31.0f, 86.0f, 24.0f, 70.0f);
        fillTri(sr, cx, cy, s, 39.0f, 34.0f, 24.0f, 70.0f, 24.0f, 58.0f);
        fillTri(sr, cx, cy, s, 24.0f, 58.0f, 31.0f, 42.0f, 39.0f, 34.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        fillTri(sr, cx, cy, s, 40.0f, 37.0f, 56.0f, 28.0f, 72.0f, 28.0f);
        fillTri(sr, cx, cy, s, 40.0f, 37.0f, 72.0f, 28.0f, 88.0f, 37.0f);
        fillTri(sr, cx, cy, s, 40.0f, 37.0f, 88.0f, 37.0f, 95.0f, 44.0f);
        fillTri(sr, cx, cy, s, 40.0f, 37.0f, 95.0f, 44.0f, 100.0f, 56.0f);
        fillTri(sr, cx, cy, s, 40.0f, 37.0f, 100.0f, 56.0f, 100.0f, 72.0f);
        fillTri(sr, cx, cy, s, 40.0f, 37.0f, 100.0f, 72.0f, 95.0f, 84.0f);
        fillTri(sr, cx, cy, s, 40.0f, 37.0f, 95.0f, 84.0f, 88.0f, 91.0f);
        fillTri(sr, cx, cy, s, 40.0f, 37.0f, 88.0f, 91.0f, 72.0f, 100.0f);
        fillTri(sr, cx, cy, s, 40.0f, 37.0f, 72.0f, 100.0f, 56.0f, 100.0f);
        fillTri(sr, cx, cy, s, 40.0f, 37.0f, 56.0f, 100.0f, 40.0f, 91.0f);
        fillTri(sr, cx, cy, s, 40.0f, 37.0f, 40.0f, 91.0f, 33.0f, 84.0f);
        fillTri(sr, cx, cy, s, 40.0f, 37.0f, 33.0f, 84.0f, 28.0f, 72.0f);
        fillTri(sr, cx, cy, s, 40.0f, 37.0f, 28.0f, 72.0f, 28.0f, 56.0f);
        fillTri(sr, cx, cy, s, 28.0f, 56.0f, 33.0f, 44.0f, 40.0f, 37.0f);
        sr.setColor(0.27f, 0.04f, 0.04f, 1.00f);
        fillTri(sr, cx, cy, s, 42.0f, 40.0f, 54.0f, 32.0f, 74.0f, 32.0f);
        fillTri(sr, cx, cy, s, 42.0f, 40.0f, 74.0f, 32.0f, 86.0f, 40.0f);
        fillTri(sr, cx, cy, s, 42.0f, 40.0f, 86.0f, 40.0f, 92.0f, 46.0f);
        fillTri(sr, cx, cy, s, 42.0f, 40.0f, 92.0f, 46.0f, 96.0f, 54.0f);
        fillTri(sr, cx, cy, s, 42.0f, 40.0f, 96.0f, 54.0f, 96.0f, 74.0f);
        fillTri(sr, cx, cy, s, 42.0f, 40.0f, 96.0f, 74.0f, 92.0f, 82.0f);
        fillTri(sr, cx, cy, s, 42.0f, 40.0f, 92.0f, 82.0f, 86.0f, 88.0f);
        fillTri(sr, cx, cy, s, 42.0f, 40.0f, 86.0f, 88.0f, 74.0f, 96.0f);
        fillTri(sr, cx, cy, s, 42.0f, 40.0f, 74.0f, 96.0f, 54.0f, 96.0f);
        fillTri(sr, cx, cy, s, 42.0f, 40.0f, 54.0f, 96.0f, 42.0f, 88.0f);
        fillTri(sr, cx, cy, s, 42.0f, 40.0f, 42.0f, 88.0f, 36.0f, 82.0f);
        fillTri(sr, cx, cy, s, 42.0f, 40.0f, 36.0f, 82.0f, 32.0f, 74.0f);
        fillTri(sr, cx, cy, s, 42.0f, 40.0f, 32.0f, 74.0f, 32.0f, 54.0f);
        fillTri(sr, cx, cy, s, 32.0f, 54.0f, 36.0f, 46.0f, 42.0f, 40.0f);
        sr.setColor(0.50f, 0.11f, 0.11f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 28.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 24.0f);
        sr.setColor(0.96f, 0.62f, 0.04f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 20.0f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        drawLine(sr, cx, cy, s, 46.0f, 52.0f, 82.0f, 76.0f, 1.2f);
        drawLine(sr, cx, cy, s, 46.0f, 76.0f, 82.0f, 52.0f, 1.2f);
        sr.setColor(0.27f, 0.04f, 0.04f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 16.0f);
        sr.setColor(0.60f, 0.11f, 0.11f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 13.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillTri(sr, cx, cy, s, 61.0f, 61.0f, 64.0f, 53.0f, 67.0f, 61.0f);
        fillTri(sr, cx, cy, s, 67.0f, 61.0f, 75.0f, 61.0f, 69.0f, 66.0f);
        fillTri(sr, cx, cy, s, 61.0f, 61.0f, 67.0f, 61.0f, 69.0f, 66.0f);
        fillTri(sr, cx, cy, s, 61.0f, 61.0f, 69.0f, 66.0f, 71.0f, 74.0f);
        fillTri(sr, cx, cy, s, 61.0f, 61.0f, 71.0f, 74.0f, 64.0f, 69.0f);
        fillTri(sr, cx, cy, s, 61.0f, 61.0f, 64.0f, 69.0f, 57.0f, 74.0f);
        fillTri(sr, cx, cy, s, 61.0f, 61.0f, 57.0f, 74.0f, 59.0f, 66.0f);
        fillTri(sr, cx, cy, s, 59.0f, 66.0f, 53.0f, 61.0f, 61.0f, 61.0f);
        sr.setColor(Color.WHITE);
        fillTri(sr, cx, cy, s, 61.5f, 61.0f, 64.0f, 54.0f, 66.5f, 61.0f);
        fillTri(sr, cx, cy, s, 66.5f, 61.0f, 73.0f, 61.0f, 68.0f, 65.5f);
        fillTri(sr, cx, cy, s, 61.5f, 61.0f, 66.5f, 61.0f, 68.0f, 65.5f);
        fillTri(sr, cx, cy, s, 61.5f, 61.0f, 68.0f, 65.5f, 70.0f, 72.0f);
        fillTri(sr, cx, cy, s, 61.5f, 61.0f, 70.0f, 72.0f, 64.0f, 68.0f);
        fillTri(sr, cx, cy, s, 61.5f, 61.0f, 64.0f, 68.0f, 58.0f, 72.0f);
        fillTri(sr, cx, cy, s, 61.5f, 61.0f, 58.0f, 72.0f, 60.0f, 65.5f);
        fillTri(sr, cx, cy, s, 60.0f, 65.5f, 55.0f, 61.0f, 61.5f, 61.0f);
        sr.setColor(0.47f, 0.21f, 0.06f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 64.0f, 98.0f, 34.0f, 4.5f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 64.0f, 98.0f, 34.0f, 2.5f);
        sr.setColor(0.47f, 0.21f, 0.06f, 1.00f);
        fillCircle(sr, cx, cy, s, 98.0f, 34.0f, 5.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        fillCircle(sr, cx, cy, s, 98.0f, 34.0f, 2.8f);
        fillTri(sr, cx, cy, s, 100.0f, 42.0f, 98.0f, 36.0f, 112.0f, 40.0f);
        fillTri(sr, cx, cy, s, 100.0f, 42.0f, 112.0f, 40.0f, 106.0f, 44.0f);
        fillTri(sr, cx, cy, s, 106.0f, 44.0f, 116.0f, 48.0f, 100.0f, 42.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 64.0f, 12.0f, 1.4f);
        fillCircle(sr, cx, cy, s, 98.0f, 32.0f, 1.6f);
        fillCircle(sr, cx, cy, s, 64.0f, 55.0f, 1.4f);
        fillCircle(sr, cx, cy, s, 102.0f, 27.0f, 1.2f);
        fillCircle(sr, cx, cy, s, 26.0f, 27.0f, 1.2f);
    }

    public static void drawVeQuocHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        // --- LAYER: background_glow ---
        sr.setColor(0.94f, 0.27f, 0.27f, 0.16f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 42.0f);
        sr.setColor(0.96f, 0.62f, 0.04f, 0.18f);
        fillCircle(sr, cx, cy, s, 80.0f, 26.0f, 16.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        fillTri(sr, cx, cy, s, 92.0f, 52.0f, 86.0f, 36.0f, 90.8f, 42.5f);
        fillTri(sr, cx, cy, s, 92.0f, 52.0f, 90.8f, 42.5f, 95.0f, 50.0f);
        fillTri(sr, cx, cy, s, 92.0f, 52.0f, 95.0f, 50.0f, 98.8f, 58.5f);
        fillTri(sr, cx, cy, s, 92.0f, 52.0f, 98.8f, 58.5f, 102.0f, 68.0f);
        fillTri(sr, cx, cy, s, 92.0f, 52.0f, 102.0f, 68.0f, 99.9f, 64.8f);
        fillTri(sr, cx, cy, s, 92.0f, 52.0f, 99.9f, 64.8f, 97.5f, 61.0f);
        fillTri(sr, cx, cy, s, 97.5f, 61.0f, 94.9f, 56.8f, 92.0f, 52.0f);
        sr.setColor(0.73f, 0.11f, 0.11f, 1.00f);
        fillTri(sr, cx, cy, s, 96.0f, 60.0f, 88.0f, 40.0f, 96.4f, 49.5f);
        fillTri(sr, cx, cy, s, 96.0f, 60.0f, 96.4f, 49.5f, 103.5f, 60.0f);
        fillTri(sr, cx, cy, s, 96.0f, 60.0f, 103.5f, 60.0f, 109.4f, 71.5f);
        fillTri(sr, cx, cy, s, 96.0f, 60.0f, 109.4f, 71.5f, 114.0f, 84.0f);
        fillTri(sr, cx, cy, s, 96.0f, 60.0f, 114.0f, 84.0f, 109.9f, 78.8f);
        fillTri(sr, cx, cy, s, 96.0f, 60.0f, 109.9f, 78.8f, 105.5f, 73.0f);
        fillTri(sr, cx, cy, s, 105.5f, 73.0f, 100.9f, 66.8f, 96.0f, 60.0f);
        sr.setColor(0.50f, 0.11f, 0.11f, 1.00f);
        fillTri(sr, cx, cy, s, 114.0f, 84.0f, 116.0f, 92.0f, 108.0f, 88.0f);
        sr.setColor(0.47f, 0.21f, 0.06f, 1.00f);
        fillTri(sr, cx, cy, s, 76.0f, 34.0f, 84.0f, 34.0f, 84.0f, 72.0f);
        fillTri(sr, cx, cy, s, 74.0f, 34.0f, 76.0f, 34.0f, 84.0f, 72.0f);
        fillTri(sr, cx, cy, s, 74.0f, 34.0f, 84.0f, 72.0f, 82.2f, 86.0f);
        fillTri(sr, cx, cy, s, 74.0f, 34.0f, 82.2f, 86.0f, 77.0f, 96.0f);
        fillTri(sr, cx, cy, s, 77.0f, 96.0f, 68.2f, 102.0f, 56.0f, 104.0f);
        fillTri(sr, cx, cy, s, 77.0f, 96.0f, 56.0f, 104.0f, 43.8f, 102.5f);
        fillTri(sr, cx, cy, s, 77.0f, 96.0f, 43.8f, 102.5f, 35.0f, 98.0f);
        fillTri(sr, cx, cy, s, 35.0f, 98.0f, 29.8f, 90.5f, 28.0f, 80.0f);
        fillTri(sr, cx, cy, s, 35.0f, 98.0f, 28.0f, 80.0f, 28.9f, 72.4f);
        fillTri(sr, cx, cy, s, 35.0f, 98.0f, 28.9f, 72.4f, 31.5f, 65.5f);
        fillTri(sr, cx, cy, s, 35.0f, 98.0f, 31.5f, 65.5f, 35.9f, 59.4f);
        fillTri(sr, cx, cy, s, 35.9f, 59.4f, 42.0f, 54.0f, 46.0f, 62.0f);
        fillTri(sr, cx, cy, s, 35.9f, 59.4f, 46.0f, 62.0f, 41.6f, 66.1f);
        fillTri(sr, cx, cy, s, 35.9f, 59.4f, 41.6f, 66.1f, 38.5f, 70.5f);
        fillTri(sr, cx, cy, s, 35.9f, 59.4f, 38.5f, 70.5f, 36.6f, 75.1f);
        fillTri(sr, cx, cy, s, 35.9f, 59.4f, 36.6f, 75.1f, 36.0f, 80.0f);
        fillTri(sr, cx, cy, s, 35.0f, 98.0f, 35.9f, 59.4f, 36.0f, 80.0f);
        fillTri(sr, cx, cy, s, 35.0f, 98.0f, 36.0f, 80.0f, 37.2f, 87.0f);
        fillTri(sr, cx, cy, s, 35.0f, 98.0f, 37.2f, 87.0f, 41.0f, 92.0f);
        fillTri(sr, cx, cy, s, 35.0f, 98.0f, 41.0f, 92.0f, 47.2f, 95.0f);
        fillTri(sr, cx, cy, s, 35.0f, 98.0f, 47.2f, 95.0f, 56.0f, 96.0f);
        fillTri(sr, cx, cy, s, 77.0f, 96.0f, 35.0f, 98.0f, 56.0f, 96.0f);
        fillTri(sr, cx, cy, s, 77.0f, 96.0f, 56.0f, 96.0f, 63.9f, 94.5f);
        fillTri(sr, cx, cy, s, 77.0f, 96.0f, 63.9f, 94.5f, 69.5f, 90.0f);
        fillTri(sr, cx, cy, s, 77.0f, 96.0f, 69.5f, 90.0f, 72.9f, 82.5f);
        fillTri(sr, cx, cy, s, 77.0f, 96.0f, 72.9f, 82.5f, 74.0f, 72.0f);
        fillTri(sr, cx, cy, s, 77.0f, 96.0f, 74.0f, 72.0f, 74.0f, 34.0f);
        sr.setColor(0.85f, 0.47f, 0.02f, 1.00f);
        fillTri(sr, cx, cy, s, 42.0f, 54.0f, 48.0f, 42.0f, 46.0f, 62.0f);
        sr.setColor(0.57f, 0.25f, 0.05f, 1.00f);
        fillTri(sr, cx, cy, s, 82.0f, 38.0f, 82.0f, 72.0f, 80.4f, 85.1f);
        fillTri(sr, cx, cy, s, 80.4f, 85.1f, 75.5f, 94.5f, 67.4f, 100.1f);
        fillTri(sr, cx, cy, s, 67.4f, 100.1f, 56.0f, 102.0f, 45.5f, 100.6f);
        fillTri(sr, cx, cy, s, 67.4f, 100.1f, 45.5f, 100.6f, 38.0f, 96.5f);
        fillTri(sr, cx, cy, s, 38.0f, 96.5f, 33.5f, 89.6f, 32.0f, 80.0f);
        fillTri(sr, cx, cy, s, 38.0f, 96.5f, 32.0f, 80.0f, 32.6f, 73.1f);
        fillTri(sr, cx, cy, s, 38.0f, 96.5f, 32.6f, 73.1f, 34.5f, 66.5f);
        fillTri(sr, cx, cy, s, 38.0f, 96.5f, 34.5f, 66.5f, 37.6f, 60.1f);
        fillTri(sr, cx, cy, s, 37.6f, 60.1f, 42.0f, 54.0f, 44.0f, 64.0f);
        fillTri(sr, cx, cy, s, 37.6f, 60.1f, 44.0f, 64.0f, 41.4f, 68.1f);
        fillTri(sr, cx, cy, s, 37.6f, 60.1f, 41.4f, 68.1f, 39.5f, 72.5f);
        fillTri(sr, cx, cy, s, 37.6f, 60.1f, 39.5f, 72.5f, 38.4f, 77.1f);
        fillTri(sr, cx, cy, s, 37.6f, 60.1f, 38.4f, 77.1f, 38.0f, 82.0f);
        fillTri(sr, cx, cy, s, 38.0f, 96.5f, 37.6f, 60.1f, 38.0f, 82.0f);
        fillTri(sr, cx, cy, s, 38.0f, 96.5f, 38.0f, 82.0f, 39.1f, 89.0f);
        fillTri(sr, cx, cy, s, 38.0f, 96.5f, 39.1f, 89.0f, 42.5f, 94.0f);
        fillTri(sr, cx, cy, s, 38.0f, 96.5f, 42.5f, 94.0f, 48.1f, 97.0f);
        fillTri(sr, cx, cy, s, 67.4f, 100.1f, 38.0f, 96.5f, 48.1f, 97.0f);
        fillTri(sr, cx, cy, s, 67.4f, 100.1f, 48.1f, 97.0f, 56.0f, 98.0f);
        fillTri(sr, cx, cy, s, 67.4f, 100.1f, 56.0f, 98.0f, 66.5f, 96.2f);
        fillTri(sr, cx, cy, s, 67.4f, 100.1f, 66.5f, 96.2f, 74.0f, 91.0f);
        fillTri(sr, cx, cy, s, 80.4f, 85.1f, 67.4f, 100.1f, 74.0f, 91.0f);
        fillTri(sr, cx, cy, s, 80.4f, 85.1f, 74.0f, 91.0f, 78.5f, 82.2f);
        fillTri(sr, cx, cy, s, 80.4f, 85.1f, 78.5f, 82.2f, 80.0f, 70.0f);
        fillTri(sr, cx, cy, s, 82.0f, 38.0f, 80.4f, 85.1f, 80.0f, 70.0f);
        fillTri(sr, cx, cy, s, 82.0f, 38.0f, 80.0f, 70.0f, 80.0f, 38.0f);
        fillTri(sr, cx, cy, s, 82.0f, 38.0f, 80.0f, 38.0f, 74.0f, 38.0f);
        sr.setColor(0.47f, 0.21f, 0.06f, 1.00f);
        fillCircle(sr, cx, cy, s, 80.0f, 26.0f, 14.0f);
        sr.setColor(0.06f, 0.09f, 0.16f, 1.00f);
        fillCircle(sr, cx, cy, s, 80.0f, 26.0f, 8.0f);
        // --- LAYER: details_and_relief ---
        sr.setColor(0.12f, 0.16f, 0.23f, 1.00f);
        fillTri(sr, cx, cy, s, 84.0f, 46.0f, 92.0f, 42.0f, 84.0f, 54.0f);
        fillTri(sr, cx, cy, s, 84.0f, 58.0f, 93.0f, 56.0f, 84.0f, 66.0f);
        fillTri(sr, cx, cy, s, 82.0f, 70.0f, 91.0f, 72.0f, 80.0f, 78.0f);
        fillTri(sr, cx, cy, s, 74.0f, 84.0f, 81.0f, 90.0f, 70.0f, 92.0f);
        fillTri(sr, cx, cy, s, 62.0f, 96.0f, 66.0f, 105.0f, 54.0f, 102.0f);
        fillTri(sr, cx, cy, s, 46.0f, 102.0f, 44.0f, 111.0f, 36.0f, 98.0f);
        sr.setColor(0.47f, 0.21f, 0.06f, 1.00f);
        fillCircle(sr, cx, cy, s, 80.0f, 26.0f, 6.0f);
        sr.setColor(0.92f, 0.70f, 0.03f, 1.00f);
        fillTri(sr, cx, cy, s, 78.2f, 24.8f, 80.0f, 21.0f, 81.8f, 24.8f);
        fillTri(sr, cx, cy, s, 78.2f, 24.8f, 81.8f, 24.8f, 86.0f, 25.4f);
        fillTri(sr, cx, cy, s, 78.2f, 24.8f, 86.0f, 25.4f, 83.0f, 28.2f);
        fillTri(sr, cx, cy, s, 78.2f, 24.8f, 83.0f, 28.2f, 83.7f, 32.4f);
        fillTri(sr, cx, cy, s, 78.2f, 24.8f, 83.7f, 32.4f, 80.0f, 30.4f);
        fillTri(sr, cx, cy, s, 78.2f, 24.8f, 80.0f, 30.4f, 76.3f, 32.4f);
        fillTri(sr, cx, cy, s, 78.2f, 24.8f, 76.3f, 32.4f, 77.0f, 28.2f);
        fillTri(sr, cx, cy, s, 77.0f, 28.2f, 74.0f, 25.4f, 78.2f, 24.8f);
        sr.setColor(0.96f, 0.62f, 0.04f, 1.00f);
        drawLine(sr, cx, cy, s, 79.0f, 38.0f, 79.0f, 72.0f, 1.5f);
        drawLine(sr, cx, cy, s, 79.0f, 72.0f, 77.6f, 80.8f, 1.5f);
        drawLine(sr, cx, cy, s, 77.6f, 80.8f, 73.2f, 87.0f, 1.5f);
        drawLine(sr, cx, cy, s, 73.2f, 87.0f, 66.1f, 90.8f, 1.5f);
        drawLine(sr, cx, cy, s, 66.1f, 90.8f, 56.0f, 92.0f, 1.5f);
        drawLine(sr, cx, cy, s, 56.0f, 92.0f, 49.9f, 91.2f, 1.5f);
        drawLine(sr, cx, cy, s, 49.9f, 91.2f, 45.5f, 89.0f, 1.5f);
        drawLine(sr, cx, cy, s, 45.5f, 89.0f, 42.9f, 85.2f, 1.5f);
        drawLine(sr, cx, cy, s, 42.9f, 85.2f, 42.0f, 80.0f, 1.5f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        fillCircle(sr, cx, cy, s, 76.0f, 38.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 82.0f, 38.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 79.0f, 50.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 79.0f, 62.0f, 1.5f);
        // --- LAYER: specular_highlights ---
        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 72.0f, 20.0f, 76.0f, 17.8f, 1.5f);
        drawLine(sr, cx, cy, s, 76.0f, 17.8f, 80.0f, 17.0f, 1.5f);
        drawLine(sr, cx, cy, s, 80.0f, 17.0f, 84.0f, 17.8f, 1.5f);
        drawLine(sr, cx, cy, s, 84.0f, 17.8f, 88.0f, 20.0f, 1.5f);
        fillTri(sr, cx, cy, s, 48.0f, 42.0f, 46.0f, 47.0f, 44.0f, 45.0f);
        drawLine(sr, cx, cy, s, 48.0f, 42.0f, 42.0f, 54.0f, 1.0f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.85f);
        drawLine(sr, cx, cy, s, 44.0f, 64.0f, 41.4f, 68.0f, 1.0f);
        drawLine(sr, cx, cy, s, 41.4f, 68.0f, 39.5f, 72.0f, 1.0f);
        drawLine(sr, cx, cy, s, 39.5f, 72.0f, 38.4f, 76.0f, 1.0f);
        drawLine(sr, cx, cy, s, 38.4f, 76.0f, 38.0f, 80.0f, 1.0f);
        drawLine(sr, cx, cy, s, 38.0f, 80.0f, 39.1f, 86.1f, 1.0f);
        drawLine(sr, cx, cy, s, 39.1f, 86.1f, 42.5f, 90.5f, 1.0f);
        drawLine(sr, cx, cy, s, 42.5f, 90.5f, 48.1f, 93.1f, 1.0f);
        drawLine(sr, cx, cy, s, 48.1f, 93.1f, 56.0f, 94.0f, 1.0f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.75f);
        drawLine(sr, cx, cy, s, 75.0f, 40.0f, 75.0f, 68.0f, 1.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 80.0f, 26.0f, 1.0f);
    }

    // =========================================================================
    // 11. THẦN KHÍ LẠC HỒNG SET (Imperial Dong Son Bronze Drum Staff, Sacred Dragon Spool, Au Co Lotus Hook)
    // =========================================================================
    public static void drawLacHongRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        sr.setColor(0.86f, 0.15f, 0.15f, 0.22f);
        fillCircle(sr, cx, cy, s, 86.0f, 38.0f, 40.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 0.25f);
        fillCircle(sr, cx, cy, s, 86.0f, 38.0f, 30.0f);
        sr.setColor(0.06f, 0.73f, 0.51f, 0.16f);
        fillCircle(sr, cx, cy, s, 54.0f, 74.0f, 34.0f);
        sr.setColor(0.11f, 0.10f, 0.09f, 1.00f);
        drawLine(sr, cx, cy, s, 20.0f, 108.0f, 84.0f, 44.0f, 8.5f);
        sr.setColor(0.47f, 0.21f, 0.06f, 1.00f);
        drawLine(sr, cx, cy, s, 20.0f, 108.0f, 84.0f, 44.0f, 6.2f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        drawLine(sr, cx, cy, s, 22.0f, 106.0f, 82.0f, 46.0f, 3.6f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        drawLine(sr, cx, cy, s, 22.0f, 106.0f, 82.0f, 46.0f, 1.2f);
        sr.setColor(0.02f, 0.37f, 0.27f, 1.00f);
        drawLine(sr, cx, cy, s, 28.0f, 100.0f, 31.9f, 95.9f, 5.0f);
        drawLine(sr, cx, cy, s, 31.9f, 95.9f, 35.5f, 93.5f, 5.0f);
        drawLine(sr, cx, cy, s, 35.5f, 93.5f, 38.9f, 92.9f, 5.0f);
        drawLine(sr, cx, cy, s, 38.9f, 92.9f, 42.0f, 94.0f, 5.0f);
        drawLine(sr, cx, cy, s, 42.0f, 94.0f, 44.9f, 95.1f, 5.0f);
        drawLine(sr, cx, cy, s, 44.9f, 95.1f, 47.5f, 94.5f, 5.0f);
        drawLine(sr, cx, cy, s, 47.5f, 94.5f, 49.9f, 92.1f, 5.0f);
        drawLine(sr, cx, cy, s, 49.9f, 92.1f, 52.0f, 88.0f, 5.0f);
        drawLine(sr, cx, cy, s, 52.0f, 88.0f, 54.2f, 83.9f, 5.0f);
        drawLine(sr, cx, cy, s, 54.2f, 83.9f, 57.0f, 81.5f, 5.0f);
        drawLine(sr, cx, cy, s, 57.0f, 81.5f, 60.2f, 80.9f, 5.0f);
        drawLine(sr, cx, cy, s, 60.2f, 80.9f, 64.0f, 82.0f, 5.0f);
        drawLine(sr, cx, cy, s, 64.0f, 82.0f, 67.8f, 83.0f, 5.0f);
        drawLine(sr, cx, cy, s, 67.8f, 83.0f, 71.0f, 82.0f, 5.0f);
        drawLine(sr, cx, cy, s, 71.0f, 82.0f, 73.8f, 79.0f, 5.0f);
        drawLine(sr, cx, cy, s, 73.8f, 79.0f, 76.0f, 74.0f, 5.0f);
        drawLine(sr, cx, cy, s, 76.0f, 74.0f, 78.1f, 69.0f, 5.0f);
        drawLine(sr, cx, cy, s, 78.1f, 69.0f, 80.5f, 66.0f, 5.0f);
        drawLine(sr, cx, cy, s, 80.5f, 66.0f, 83.1f, 65.0f, 5.0f);
        drawLine(sr, cx, cy, s, 83.1f, 65.0f, 86.0f, 66.0f, 5.0f);
        sr.setColor(0.06f, 0.73f, 0.51f, 1.00f);
        drawLine(sr, cx, cy, s, 28.0f, 100.0f, 31.9f, 95.9f, 3.0f);
        drawLine(sr, cx, cy, s, 31.9f, 95.9f, 35.5f, 93.5f, 3.0f);
        drawLine(sr, cx, cy, s, 35.5f, 93.5f, 38.9f, 92.9f, 3.0f);
        drawLine(sr, cx, cy, s, 38.9f, 92.9f, 42.0f, 94.0f, 3.0f);
        drawLine(sr, cx, cy, s, 42.0f, 94.0f, 44.9f, 95.1f, 3.0f);
        drawLine(sr, cx, cy, s, 44.9f, 95.1f, 47.5f, 94.5f, 3.0f);
        drawLine(sr, cx, cy, s, 47.5f, 94.5f, 49.9f, 92.1f, 3.0f);
        drawLine(sr, cx, cy, s, 49.9f, 92.1f, 52.0f, 88.0f, 3.0f);
        drawLine(sr, cx, cy, s, 52.0f, 88.0f, 54.2f, 83.9f, 3.0f);
        drawLine(sr, cx, cy, s, 54.2f, 83.9f, 57.0f, 81.5f, 3.0f);
        drawLine(sr, cx, cy, s, 57.0f, 81.5f, 60.2f, 80.9f, 3.0f);
        drawLine(sr, cx, cy, s, 60.2f, 80.9f, 64.0f, 82.0f, 3.0f);
        drawLine(sr, cx, cy, s, 64.0f, 82.0f, 67.8f, 83.0f, 3.0f);
        drawLine(sr, cx, cy, s, 67.8f, 83.0f, 71.0f, 82.0f, 3.0f);
        drawLine(sr, cx, cy, s, 71.0f, 82.0f, 73.8f, 79.0f, 3.0f);
        drawLine(sr, cx, cy, s, 73.8f, 79.0f, 76.0f, 74.0f, 3.0f);
        drawLine(sr, cx, cy, s, 76.0f, 74.0f, 78.1f, 69.0f, 3.0f);
        drawLine(sr, cx, cy, s, 78.1f, 69.0f, 80.5f, 66.0f, 3.0f);
        drawLine(sr, cx, cy, s, 80.5f, 66.0f, 83.1f, 65.0f, 3.0f);
        drawLine(sr, cx, cy, s, 83.1f, 65.0f, 86.0f, 66.0f, 3.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        drawLine(sr, cx, cy, s, 28.0f, 100.0f, 31.9f, 95.9f, 1.0f);
        drawLine(sr, cx, cy, s, 31.9f, 95.9f, 35.5f, 93.5f, 1.0f);
        drawLine(sr, cx, cy, s, 35.5f, 93.5f, 38.9f, 92.9f, 1.0f);
        drawLine(sr, cx, cy, s, 38.9f, 92.9f, 42.0f, 94.0f, 1.0f);
        drawLine(sr, cx, cy, s, 42.0f, 94.0f, 44.9f, 95.1f, 1.0f);
        drawLine(sr, cx, cy, s, 44.9f, 95.1f, 47.5f, 94.5f, 1.0f);
        drawLine(sr, cx, cy, s, 47.5f, 94.5f, 49.9f, 92.1f, 1.0f);
        drawLine(sr, cx, cy, s, 49.9f, 92.1f, 52.0f, 88.0f, 1.0f);
        drawLine(sr, cx, cy, s, 52.0f, 88.0f, 54.2f, 83.9f, 1.0f);
        drawLine(sr, cx, cy, s, 54.2f, 83.9f, 57.0f, 81.5f, 1.0f);
        drawLine(sr, cx, cy, s, 57.0f, 81.5f, 60.2f, 80.9f, 1.0f);
        drawLine(sr, cx, cy, s, 60.2f, 80.9f, 64.0f, 82.0f, 1.0f);
        drawLine(sr, cx, cy, s, 64.0f, 82.0f, 67.8f, 83.0f, 1.0f);
        drawLine(sr, cx, cy, s, 67.8f, 83.0f, 71.0f, 82.0f, 1.0f);
        drawLine(sr, cx, cy, s, 71.0f, 82.0f, 73.8f, 79.0f, 1.0f);
        drawLine(sr, cx, cy, s, 73.8f, 79.0f, 76.0f, 74.0f, 1.0f);
        drawLine(sr, cx, cy, s, 76.0f, 74.0f, 78.1f, 69.0f, 1.0f);
        drawLine(sr, cx, cy, s, 78.1f, 69.0f, 80.5f, 66.0f, 1.0f);
        drawLine(sr, cx, cy, s, 80.5f, 66.0f, 83.1f, 65.0f, 1.0f);
        drawLine(sr, cx, cy, s, 83.1f, 65.0f, 86.0f, 66.0f, 1.0f);
        fillTri(sr, cx, cy, s, 40.0f, 88.0f, 44.0f, 84.0f, 43.0f, 91.0f);
        fillTri(sr, cx, cy, s, 54.0f, 80.0f, 58.0f, 76.0f, 57.0f, 83.0f);
        fillTri(sr, cx, cy, s, 68.0f, 74.0f, 72.0f, 70.0f, 71.0f, 77.0f);
        sr.setColor(0.47f, 0.21f, 0.06f, 1.00f);
        fillCircle(sr, cx, cy, s, 86.0f, 38.0f, 22.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        fillCircle(sr, cx, cy, s, 86.0f, 38.0f, 18.0f);
        sr.setColor(0.27f, 0.10f, 0.01f, 1.00f);
        fillCircle(sr, cx, cy, s, 86.0f, 38.0f, 14.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillTri(sr, cx, cy, s, 86.0f, 18.0f, 83.0f, 26.0f, 89.0f, 26.0f);
        fillTri(sr, cx, cy, s, 106.0f, 38.0f, 98.0f, 35.0f, 98.0f, 41.0f);
        fillTri(sr, cx, cy, s, 86.0f, 58.0f, 89.0f, 50.0f, 83.0f, 50.0f);
        fillTri(sr, cx, cy, s, 66.0f, 38.0f, 74.0f, 41.0f, 74.0f, 35.0f);
        sr.setColor(0.92f, 0.35f, 0.05f, 1.00f);
        fillTri(sr, cx, cy, s, 100.0f, 24.0f, 92.0f, 30.0f, 96.0f, 34.0f);
        fillTri(sr, cx, cy, s, 100.0f, 52.0f, 96.0f, 44.0f, 92.0f, 48.0f);
        fillTri(sr, cx, cy, s, 72.0f, 52.0f, 80.0f, 48.0f, 76.0f, 44.0f);
        fillTri(sr, cx, cy, s, 72.0f, 24.0f, 76.0f, 34.0f, 80.0f, 30.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        fillCircle(sr, cx, cy, s, 86.0f, 38.0f, 7.0f);
        sr.setColor(0.98f, 0.45f, 0.09f, 1.00f);
        fillCircle(sr, cx, cy, s, 86.0f, 38.0f, 4.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 85.0f, 37.0f, 1.8f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillQuad(sr, cx, cy, s, 84.0f, 34.0f, 62.0f, 20.0f, 72.0f, 12.0f, 88.0f, 26.0f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        fillQuad(sr, cx, cy, s, 84.0f, 33.0f, 66.0f, 22.0f, 74.0f, 16.0f, 86.0f, 26.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillQuad(sr, cx, cy, s, 88.0f, 32.0f, 98.0f, 8.0f, 108.0f, 16.0f, 94.0f, 30.0f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        fillQuad(sr, cx, cy, s, 88.0f, 31.0f, 98.0f, 12.0f, 104.0f, 18.0f, 92.0f, 30.0f);
        sr.setColor(0.99f, 0.88f, 0.28f, 1.00f);
        fillQuad(sr, cx, cy, s, 88.0f, 30.0f, 114.0f, 20.0f, 118.0f, 22.0f, 92.0f, 34.0f);
        sr.setColor(Color.WHITE);
        fillTri(sr, cx, cy, s, 114.0f, 20.0f, 124.0f, 16.0f, 116.0f, 23.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        fillTri(sr, cx, cy, s, 90.0f, 28.0f, 78.0f, 14.0f, 86.0f, 22.0f);
        fillCircle(sr, cx, cy, s, 94.0f, 28.0f, 2.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 93.5f, 27.5f, 0.8f);
        sr.setColor(0.96f, 0.62f, 0.04f, 1.00f);
        fillQuad(sr, cx, cy, s, 82.0f, 44.0f, 70.0f, 62.0f, 76.0f, 64.0f, 86.0f, 46.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        fillQuad(sr, cx, cy, s, 80.0f, 46.0f, 64.0f, 68.0f, 68.0f, 70.0f, 82.0f, 48.0f);
        sr.setColor(0.02f, 0.37f, 0.27f, 1.00f);
        fillQuad(sr, cx, cy, s, 22.0f, 96.0f, 38.0f, 96.0f, 38.0f, 108.0f, 22.0f, 108.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        fillQuad(sr, cx, cy, s, 20.0f, 108.0f, 10.0f, 118.0f, 16.0f, 122.0f, 24.0f, 112.0f);
        sr.setColor(0.06f, 0.73f, 0.51f, 1.00f);
        fillCircle(sr, cx, cy, s, 12.0f, 120.0f, 2.2f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        fillTri(sr, cx, cy, s, 14.0f, 120.0f, 10.0f, 126.0f, 6.6f, 125.6f);
        fillTri(sr, cx, cy, s, 6.6f, 125.6f, 4.5f, 124.5f, 3.6f, 122.6f);
        fillTri(sr, cx, cy, s, 6.6f, 125.6f, 3.6f, 122.6f, 4.0f, 120.0f);
        fillTri(sr, cx, cy, s, 6.6f, 125.6f, 4.0f, 120.0f, 6.1f, 121.5f);
        fillTri(sr, cx, cy, s, 6.6f, 125.6f, 6.1f, 121.5f, 8.5f, 122.0f);
        fillTri(sr, cx, cy, s, 6.6f, 125.6f, 8.5f, 122.0f, 11.1f, 121.5f);
        fillTri(sr, cx, cy, s, 6.6f, 125.6f, 11.1f, 121.5f, 14.0f, 120.0f);
        sr.setColor(0.92f, 0.35f, 0.05f, 1.00f);
        fillTri(sr, cx, cy, s, 20.0f, 128.0f, 18.0f, 122.0f, 20.0f, 124.4f);
        fillTri(sr, cx, cy, s, 20.0f, 128.0f, 20.0f, 124.4f, 22.0f, 125.5f);
        fillTri(sr, cx, cy, s, 20.0f, 128.0f, 22.0f, 125.5f, 24.0f, 125.4f);
        fillTri(sr, cx, cy, s, 24.0f, 125.4f, 26.0f, 124.0f, 26.4f, 125.8f);
        fillTri(sr, cx, cy, s, 20.0f, 128.0f, 24.0f, 125.4f, 26.4f, 125.8f);
        fillTri(sr, cx, cy, s, 20.0f, 128.0f, 26.4f, 125.8f, 25.5f, 127.0f);
        fillTri(sr, cx, cy, s, 25.5f, 127.0f, 23.4f, 127.8f, 20.0f, 128.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 122.0f, 17.0f, 1.6f);
        fillCircle(sr, cx, cy, s, 70.0f, 14.0f, 1.4f);
        fillCircle(sr, cx, cy, s, 102.0f, 10.0f, 1.4f);
        fillCircle(sr, cx, cy, s, 86.0f, 20.0f, 1.2f);
        fillCircle(sr, cx, cy, s, 85.0f, 36.0f, 2.0f);
        fillCircle(sr, cx, cy, s, 43.0f, 91.0f, 1.2f);
        fillCircle(sr, cx, cy, s, 24.0f, 104.0f, 1.2f);
    }

    public static void drawLacHongLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        sr.setColor(0.86f, 0.15f, 0.15f, 0.20f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 54.0f);
        sr.setColor(0.06f, 0.73f, 0.51f, 0.22f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 44.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 0.25f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 32.0f);
        sr.setColor(0.02f, 0.31f, 0.23f, 1.00f);
        drawLine(sr, cx, cy, s, 30.0f, 64.0f, 32.2f, 48.1f, 8.0f);
        drawLine(sr, cx, cy, s, 32.2f, 48.1f, 38.2f, 36.3f, 8.0f);
        drawLine(sr, cx, cy, s, 38.2f, 36.3f, 46.8f, 28.1f, 8.0f);
        drawLine(sr, cx, cy, s, 46.8f, 28.1f, 57.1f, 23.5f, 8.0f);
        drawLine(sr, cx, cy, s, 57.1f, 23.5f, 68.0f, 22.0f, 8.0f);
        drawLine(sr, cx, cy, s, 68.0f, 22.0f, 76.3f, 22.9f, 8.0f);
        drawLine(sr, cx, cy, s, 76.3f, 22.9f, 83.9f, 25.7f, 8.0f);
        drawLine(sr, cx, cy, s, 83.9f, 25.7f, 90.2f, 30.2f, 8.0f);
        drawLine(sr, cx, cy, s, 90.2f, 30.2f, 94.4f, 36.3f, 8.0f);
        drawLine(sr, cx, cy, s, 94.4f, 36.3f, 96.0f, 44.0f, 8.0f);
        sr.setColor(0.06f, 0.73f, 0.51f, 1.00f);
        drawLine(sr, cx, cy, s, 30.0f, 64.0f, 32.2f, 48.1f, 5.5f);
        drawLine(sr, cx, cy, s, 32.2f, 48.1f, 38.2f, 36.3f, 5.5f);
        drawLine(sr, cx, cy, s, 38.2f, 36.3f, 46.8f, 28.1f, 5.5f);
        drawLine(sr, cx, cy, s, 46.8f, 28.1f, 57.1f, 23.5f, 5.5f);
        drawLine(sr, cx, cy, s, 57.1f, 23.5f, 68.0f, 22.0f, 5.5f);
        drawLine(sr, cx, cy, s, 68.0f, 22.0f, 76.3f, 22.9f, 5.5f);
        drawLine(sr, cx, cy, s, 76.3f, 22.9f, 83.9f, 25.7f, 5.5f);
        drawLine(sr, cx, cy, s, 83.9f, 25.7f, 90.2f, 30.2f, 5.5f);
        drawLine(sr, cx, cy, s, 90.2f, 30.2f, 94.4f, 36.3f, 5.5f);
        drawLine(sr, cx, cy, s, 94.4f, 36.3f, 96.0f, 44.0f, 5.5f);
        sr.setColor(0.43f, 0.91f, 0.72f, 1.00f);
        drawLine(sr, cx, cy, s, 30.0f, 64.0f, 32.2f, 48.1f, 1.8f);
        drawLine(sr, cx, cy, s, 32.2f, 48.1f, 38.2f, 36.3f, 1.8f);
        drawLine(sr, cx, cy, s, 38.2f, 36.3f, 46.8f, 28.1f, 1.8f);
        drawLine(sr, cx, cy, s, 46.8f, 28.1f, 57.1f, 23.5f, 1.8f);
        drawLine(sr, cx, cy, s, 57.1f, 23.5f, 68.0f, 22.0f, 1.8f);
        drawLine(sr, cx, cy, s, 68.0f, 22.0f, 76.3f, 22.9f, 1.8f);
        drawLine(sr, cx, cy, s, 76.3f, 22.9f, 83.9f, 25.7f, 1.8f);
        drawLine(sr, cx, cy, s, 83.9f, 25.7f, 90.2f, 30.2f, 1.8f);
        drawLine(sr, cx, cy, s, 90.2f, 30.2f, 94.4f, 36.3f, 1.8f);
        drawLine(sr, cx, cy, s, 94.4f, 36.3f, 96.0f, 44.0f, 1.8f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillTri(sr, cx, cy, s, 44.0f, 32.0f, 46.0f, 24.0f, 50.0f, 30.0f);
        fillTri(sr, cx, cy, s, 58.0f, 24.0f, 62.0f, 16.0f, 66.0f, 24.0f);
        fillTri(sr, cx, cy, s, 76.0f, 24.0f, 82.0f, 18.0f, 82.0f, 28.0f);
        fillQuad(sr, cx, cy, s, 94.0f, 40.0f, 106.0f, 34.0f, 102.0f, 46.0f, 92.0f, 46.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        fillTri(sr, cx, cy, s, 98.0f, 34.0f, 110.0f, 26.0f, 104.0f, 36.0f);
        sr.setColor(0.92f, 0.35f, 0.05f, 1.00f);
        drawLine(sr, cx, cy, s, 96.0f, 36.0f, 104.0f, 22.0f, 2.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        fillCircle(sr, cx, cy, s, 98.0f, 40.0f, 1.8f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 97.5f, 39.5f, 0.7f);
        sr.setColor(0.02f, 0.31f, 0.23f, 1.00f);
        drawLine(sr, cx, cy, s, 98.0f, 64.0f, 95.8f, 79.9f, 8.0f);
        drawLine(sr, cx, cy, s, 95.8f, 79.9f, 89.8f, 91.7f, 8.0f);
        drawLine(sr, cx, cy, s, 89.8f, 91.7f, 81.2f, 99.9f, 8.0f);
        drawLine(sr, cx, cy, s, 81.2f, 99.9f, 70.9f, 104.5f, 8.0f);
        drawLine(sr, cx, cy, s, 70.9f, 104.5f, 60.0f, 106.0f, 8.0f);
        drawLine(sr, cx, cy, s, 60.0f, 106.0f, 51.7f, 105.1f, 8.0f);
        drawLine(sr, cx, cy, s, 51.7f, 105.1f, 44.1f, 102.3f, 8.0f);
        drawLine(sr, cx, cy, s, 44.1f, 102.3f, 37.8f, 97.8f, 8.0f);
        drawLine(sr, cx, cy, s, 37.8f, 97.8f, 33.6f, 91.7f, 8.0f);
        drawLine(sr, cx, cy, s, 33.6f, 91.7f, 32.0f, 84.0f, 8.0f);
        sr.setColor(0.06f, 0.73f, 0.51f, 1.00f);
        drawLine(sr, cx, cy, s, 98.0f, 64.0f, 95.8f, 79.9f, 5.5f);
        drawLine(sr, cx, cy, s, 95.8f, 79.9f, 89.8f, 91.7f, 5.5f);
        drawLine(sr, cx, cy, s, 89.8f, 91.7f, 81.2f, 99.9f, 5.5f);
        drawLine(sr, cx, cy, s, 81.2f, 99.9f, 70.9f, 104.5f, 5.5f);
        drawLine(sr, cx, cy, s, 70.9f, 104.5f, 60.0f, 106.0f, 5.5f);
        drawLine(sr, cx, cy, s, 60.0f, 106.0f, 51.7f, 105.1f, 5.5f);
        drawLine(sr, cx, cy, s, 51.7f, 105.1f, 44.1f, 102.3f, 5.5f);
        drawLine(sr, cx, cy, s, 44.1f, 102.3f, 37.8f, 97.8f, 5.5f);
        drawLine(sr, cx, cy, s, 37.8f, 97.8f, 33.6f, 91.7f, 5.5f);
        drawLine(sr, cx, cy, s, 33.6f, 91.7f, 32.0f, 84.0f, 5.5f);
        sr.setColor(0.43f, 0.91f, 0.72f, 1.00f);
        drawLine(sr, cx, cy, s, 98.0f, 64.0f, 95.8f, 79.9f, 1.8f);
        drawLine(sr, cx, cy, s, 95.8f, 79.9f, 89.8f, 91.7f, 1.8f);
        drawLine(sr, cx, cy, s, 89.8f, 91.7f, 81.2f, 99.9f, 1.8f);
        drawLine(sr, cx, cy, s, 81.2f, 99.9f, 70.9f, 104.5f, 1.8f);
        drawLine(sr, cx, cy, s, 70.9f, 104.5f, 60.0f, 106.0f, 1.8f);
        drawLine(sr, cx, cy, s, 60.0f, 106.0f, 51.7f, 105.1f, 1.8f);
        drawLine(sr, cx, cy, s, 51.7f, 105.1f, 44.1f, 102.3f, 1.8f);
        drawLine(sr, cx, cy, s, 44.1f, 102.3f, 37.8f, 97.8f, 1.8f);
        drawLine(sr, cx, cy, s, 37.8f, 97.8f, 33.6f, 91.7f, 1.8f);
        drawLine(sr, cx, cy, s, 33.6f, 91.7f, 32.0f, 84.0f, 1.8f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillTri(sr, cx, cy, s, 84.0f, 96.0f, 82.0f, 104.0f, 78.0f, 98.0f);
        fillTri(sr, cx, cy, s, 70.0f, 104.0f, 66.0f, 112.0f, 62.0f, 104.0f);
        fillTri(sr, cx, cy, s, 52.0f, 104.0f, 46.0f, 110.0f, 46.0f, 100.0f);
        fillQuad(sr, cx, cy, s, 34.0f, 88.0f, 22.0f, 94.0f, 26.0f, 82.0f, 36.0f, 82.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        fillTri(sr, cx, cy, s, 30.0f, 94.0f, 18.0f, 102.0f, 24.0f, 92.0f);
        sr.setColor(0.92f, 0.35f, 0.05f, 1.00f);
        drawLine(sr, cx, cy, s, 32.0f, 92.0f, 24.0f, 106.0f, 2.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        fillCircle(sr, cx, cy, s, 30.0f, 88.0f, 1.8f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 30.5f, 87.5f, 0.7f);
        sr.setColor(0.47f, 0.21f, 0.06f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 28.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 25.0f);
        sr.setColor(0.27f, 0.04f, 0.04f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 22.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillTri(sr, cx, cy, s, 64.0f, 38.0f, 61.0f, 48.0f, 67.0f, 48.0f);
        fillTri(sr, cx, cy, s, 90.0f, 64.0f, 80.0f, 61.0f, 80.0f, 67.0f);
        fillTri(sr, cx, cy, s, 64.0f, 90.0f, 67.0f, 80.0f, 61.0f, 80.0f);
        fillTri(sr, cx, cy, s, 38.0f, 64.0f, 48.0f, 67.0f, 48.0f, 61.0f);
        sr.setColor(0.92f, 0.35f, 0.05f, 1.00f);
        fillTri(sr, cx, cy, s, 82.0f, 46.0f, 72.0f, 50.0f, 75.0f, 55.0f);
        fillTri(sr, cx, cy, s, 82.0f, 82.0f, 75.0f, 73.0f, 72.0f, 78.0f);
        fillTri(sr, cx, cy, s, 46.0f, 82.0f, 56.0f, 78.0f, 53.0f, 73.0f);
        fillTri(sr, cx, cy, s, 46.0f, 46.0f, 53.0f, 55.0f, 56.0f, 50.0f);
        sr.setColor(0.50f, 0.11f, 0.11f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 16.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 12.0f);
        sr.setColor(0.92f, 0.35f, 0.05f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 8.0f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        fillCircle(sr, cx, cy, s, 62.0f, 62.0f, 4.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 61.0f, 61.0f, 2.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 106.0f, 64.0f, 116.0f, 1.5f);
        sr.setColor(0.06f, 0.73f, 0.51f, 1.00f);
        fillQuad(sr, cx, cy, s, 60.0f, 116.0f, 68.0f, 116.0f, 66.0f, 126.0f, 62.0f, 126.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 120.0f, 1.5f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 61.0f, 60.0f, 2.0f);
        fillCircle(sr, cx, cy, s, 64.0f, 40.0f, 1.2f);
        fillCircle(sr, cx, cy, s, 104.0f, 28.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 24.0f, 100.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 62.0f, 18.0f, 1.6f);
        fillCircle(sr, cx, cy, s, 66.0f, 110.0f, 1.6f);
    }

    public static void drawLacHongHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        // --- LAYER: background_glow ---
        sr.setColor(0.99f, 0.88f, 0.28f, 0.16f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 44.0f);
        sr.setColor(0.96f, 0.45f, 0.71f, 0.18f);
        fillCircle(sr, cx, cy, s, 76.0f, 24.0f, 16.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        fillTri(sr, cx, cy, s, 90.0f, 48.0f, 82.0f, 28.0f, 89.4f, 35.8f);
        fillTri(sr, cx, cy, s, 90.0f, 48.0f, 89.4f, 35.8f, 95.5f, 45.0f);
        fillTri(sr, cx, cy, s, 90.0f, 48.0f, 95.5f, 45.0f, 100.4f, 55.8f);
        fillTri(sr, cx, cy, s, 90.0f, 48.0f, 100.4f, 55.8f, 104.0f, 68.0f);
        fillTri(sr, cx, cy, s, 90.0f, 48.0f, 104.0f, 68.0f, 100.1f, 64.5f);
        fillTri(sr, cx, cy, s, 90.0f, 48.0f, 100.1f, 64.5f, 96.5f, 60.0f);
        fillTri(sr, cx, cy, s, 96.5f, 60.0f, 93.1f, 54.5f, 90.0f, 48.0f);
        sr.setColor(0.96f, 0.62f, 0.04f, 1.00f);
        fillTri(sr, cx, cy, s, 94.0f, 56.0f, 84.0f, 32.0f, 94.0f, 42.5f);
        fillTri(sr, cx, cy, s, 94.0f, 56.0f, 94.0f, 42.5f, 102.0f, 54.0f);
        fillTri(sr, cx, cy, s, 94.0f, 56.0f, 102.0f, 54.0f, 108.0f, 66.5f);
        fillTri(sr, cx, cy, s, 94.0f, 56.0f, 108.0f, 66.5f, 112.0f, 80.0f);
        fillTri(sr, cx, cy, s, 94.0f, 56.0f, 112.0f, 80.0f, 107.9f, 74.8f);
        fillTri(sr, cx, cy, s, 94.0f, 56.0f, 107.9f, 74.8f, 103.5f, 69.0f);
        fillTri(sr, cx, cy, s, 103.5f, 69.0f, 98.9f, 62.8f, 94.0f, 56.0f);
        sr.setColor(0.60f, 0.11f, 0.11f, 1.00f);
        fillTri(sr, cx, cy, s, 112.0f, 80.0f, 116.0f, 88.0f, 108.0f, 84.0f);
        sr.setColor(0.02f, 0.59f, 0.41f, 1.00f);
        fillTri(sr, cx, cy, s, 80.0f, 32.0f, 80.0f, 74.0f, 78.2f, 88.0f);
        fillTri(sr, cx, cy, s, 80.0f, 32.0f, 78.2f, 88.0f, 73.0f, 98.0f);
        fillTri(sr, cx, cy, s, 73.0f, 98.0f, 64.2f, 104.0f, 52.0f, 106.0f);
        fillTri(sr, cx, cy, s, 73.0f, 98.0f, 52.0f, 106.0f, 39.8f, 104.5f);
        fillTri(sr, cx, cy, s, 73.0f, 98.0f, 39.8f, 104.5f, 31.0f, 100.0f);
        fillTri(sr, cx, cy, s, 31.0f, 100.0f, 25.8f, 92.5f, 24.0f, 82.0f);
        fillTri(sr, cx, cy, s, 31.0f, 100.0f, 24.0f, 82.0f, 24.9f, 73.4f);
        fillTri(sr, cx, cy, s, 31.0f, 100.0f, 24.9f, 73.4f, 27.5f, 65.5f);
        fillTri(sr, cx, cy, s, 31.0f, 100.0f, 27.5f, 65.5f, 31.9f, 58.4f);
        fillTri(sr, cx, cy, s, 31.9f, 58.4f, 38.0f, 52.0f, 42.0f, 60.0f);
        fillTri(sr, cx, cy, s, 31.9f, 58.4f, 42.0f, 60.0f, 37.6f, 64.4f);
        fillTri(sr, cx, cy, s, 31.9f, 58.4f, 37.6f, 64.4f, 34.5f, 69.5f);
        fillTri(sr, cx, cy, s, 31.9f, 58.4f, 34.5f, 69.5f, 32.6f, 75.4f);
        fillTri(sr, cx, cy, s, 31.9f, 58.4f, 32.6f, 75.4f, 32.0f, 82.0f);
        fillTri(sr, cx, cy, s, 31.0f, 100.0f, 31.9f, 58.4f, 32.0f, 82.0f);
        fillTri(sr, cx, cy, s, 31.0f, 100.0f, 32.0f, 82.0f, 33.2f, 89.0f);
        fillTri(sr, cx, cy, s, 31.0f, 100.0f, 33.2f, 89.0f, 37.0f, 94.0f);
        fillTri(sr, cx, cy, s, 31.0f, 100.0f, 37.0f, 94.0f, 43.2f, 97.0f);
        fillTri(sr, cx, cy, s, 31.0f, 100.0f, 43.2f, 97.0f, 52.0f, 98.0f);
        fillTri(sr, cx, cy, s, 73.0f, 98.0f, 31.0f, 100.0f, 52.0f, 98.0f);
        fillTri(sr, cx, cy, s, 73.0f, 98.0f, 52.0f, 98.0f, 60.8f, 96.5f);
        fillTri(sr, cx, cy, s, 73.0f, 98.0f, 60.8f, 96.5f, 67.0f, 92.0f);
        fillTri(sr, cx, cy, s, 73.0f, 98.0f, 67.0f, 92.0f, 70.8f, 84.5f);
        fillTri(sr, cx, cy, s, 80.0f, 32.0f, 73.0f, 98.0f, 70.8f, 84.5f);
        fillTri(sr, cx, cy, s, 80.0f, 32.0f, 70.8f, 84.5f, 72.0f, 74.0f);
        fillTri(sr, cx, cy, s, 72.0f, 32.0f, 80.0f, 32.0f, 72.0f, 74.0f);
        fillTri(sr, cx, cy, s, 72.0f, 32.0f, 72.0f, 74.0f, 72.0f, 32.0f);
        sr.setColor(0.86f, 0.15f, 0.47f, 1.00f);
        fillTri(sr, cx, cy, s, 44.2f, 100.1f, 49.3f, 99.1f, 54.7f, 99.1f);
        fillTri(sr, cx, cy, s, 44.2f, 100.1f, 54.7f, 99.1f, 59.8f, 100.1f);
        fillTri(sr, cx, cy, s, 44.2f, 100.1f, 59.8f, 100.1f, 64.0f, 102.0f);
        fillTri(sr, cx, cy, s, 44.2f, 100.1f, 64.0f, 102.0f, 59.8f, 108.7f);
        fillTri(sr, cx, cy, s, 44.2f, 100.1f, 59.8f, 108.7f, 54.7f, 112.1f);
        fillTri(sr, cx, cy, s, 44.2f, 100.1f, 54.7f, 112.1f, 49.3f, 112.1f);
        fillTri(sr, cx, cy, s, 44.2f, 100.1f, 49.3f, 112.1f, 44.2f, 108.7f);
        fillTri(sr, cx, cy, s, 40.0f, 102.0f, 44.2f, 100.1f, 44.2f, 108.7f);
        fillTri(sr, cx, cy, s, 40.0f, 102.0f, 44.2f, 108.7f, 40.0f, 102.0f);
        fillTri(sr, cx, cy, s, 30.6f, 93.4f, 33.6f, 95.4f, 36.9f, 97.8f);
        fillTri(sr, cx, cy, s, 30.6f, 93.4f, 36.9f, 97.8f, 40.4f, 100.6f);
        fillTri(sr, cx, cy, s, 30.6f, 93.4f, 40.4f, 100.6f, 44.0f, 104.0f);
        fillTri(sr, cx, cy, s, 30.6f, 93.4f, 44.0f, 104.0f, 38.7f, 107.7f);
        fillTri(sr, cx, cy, s, 30.6f, 93.4f, 38.7f, 107.7f, 33.2f, 107.6f);
        fillTri(sr, cx, cy, s, 30.6f, 93.4f, 33.2f, 107.6f, 28.7f, 104.3f);
        fillTri(sr, cx, cy, s, 30.6f, 93.4f, 28.7f, 104.3f, 26.6f, 98.8f);
        fillTri(sr, cx, cy, s, 28.0f, 92.0f, 30.6f, 93.4f, 26.6f, 98.8f);
        fillTri(sr, cx, cy, s, 28.0f, 92.0f, 26.6f, 98.8f, 28.0f, 92.0f);
        fillTri(sr, cx, cy, s, 63.6f, 100.6f, 67.1f, 97.8f, 70.4f, 95.4f);
        fillTri(sr, cx, cy, s, 63.6f, 100.6f, 70.4f, 95.4f, 73.4f, 93.4f);
        fillTri(sr, cx, cy, s, 63.6f, 100.6f, 73.4f, 93.4f, 76.0f, 92.0f);
        fillTri(sr, cx, cy, s, 63.6f, 100.6f, 76.0f, 92.0f, 77.4f, 98.8f);
        fillTri(sr, cx, cy, s, 63.6f, 100.6f, 77.4f, 98.8f, 75.3f, 104.3f);
        fillTri(sr, cx, cy, s, 63.6f, 100.6f, 75.3f, 104.3f, 70.8f, 107.6f);
        fillTri(sr, cx, cy, s, 63.6f, 100.6f, 70.8f, 107.6f, 65.3f, 107.7f);
        fillTri(sr, cx, cy, s, 60.0f, 104.0f, 63.6f, 100.6f, 65.3f, 107.7f);
        fillTri(sr, cx, cy, s, 60.0f, 104.0f, 65.3f, 107.7f, 60.0f, 104.0f);
        sr.setColor(0.96f, 0.62f, 0.04f, 1.00f);
        fillTri(sr, cx, cy, s, 46.0f, 100.0f, 52.0f, 94.0f, 58.0f, 100.0f);
        fillTri(sr, cx, cy, s, 36.0f, 94.0f, 40.0f, 88.0f, 44.0f, 95.0f);
        fillTri(sr, cx, cy, s, 60.0f, 95.0f, 64.0f, 88.0f, 68.0f, 94.0f);
        fillTri(sr, cx, cy, s, 38.0f, 52.0f, 46.0f, 38.0f, 42.0f, 60.0f);
        sr.setColor(0.99f, 0.88f, 0.28f, 1.00f);
        fillTri(sr, cx, cy, s, 45.6f, 46.2f, 48.0f, 50.0f, 45.1f, 48.6f);
        fillTri(sr, cx, cy, s, 44.5f, 43.0f, 45.6f, 46.2f, 45.1f, 48.6f);
        fillTri(sr, cx, cy, s, 44.5f, 43.0f, 45.1f, 48.6f, 42.5f, 48.5f);
        fillTri(sr, cx, cy, s, 44.6f, 40.2f, 44.5f, 43.0f, 42.5f, 48.5f);
        fillTri(sr, cx, cy, s, 44.6f, 40.2f, 42.5f, 48.5f, 40.1f, 49.6f);
        fillTri(sr, cx, cy, s, 44.6f, 40.2f, 40.1f, 49.6f, 38.0f, 52.0f);
        fillTri(sr, cx, cy, s, 46.0f, 38.0f, 44.6f, 40.2f, 38.0f, 52.0f);
        sr.setColor(0.96f, 0.62f, 0.04f, 1.00f);
        fillTri(sr, cx, cy, s, 38.0f, 60.0f, 42.0f, 68.0f, 38.2f, 72.0f);
        fillTri(sr, cx, cy, s, 38.0f, 60.0f, 38.2f, 72.0f, 35.0f, 76.0f);
        fillTri(sr, cx, cy, s, 38.0f, 60.0f, 35.0f, 76.0f, 32.2f, 80.0f);
        fillTri(sr, cx, cy, s, 38.0f, 60.0f, 32.2f, 80.0f, 30.0f, 84.0f);
        fillTri(sr, cx, cy, s, 38.0f, 60.0f, 30.0f, 84.0f, 29.8f, 78.0f);
        fillTri(sr, cx, cy, s, 38.0f, 60.0f, 29.8f, 78.0f, 31.0f, 72.0f);
        fillTri(sr, cx, cy, s, 31.0f, 72.0f, 33.8f, 66.0f, 38.0f, 60.0f);
        // --- LAYER: details_and_relief ---
        fillCircle(sr, cx, cy, s, 76.0f, 24.0f, 12.0f);
        sr.setColor(0.88f, 0.95f, 1.00f, 1.00f);
        fillCircle(sr, cx, cy, s, 76.0f, 24.0f, 8.0f);
        sr.setColor(0.99f, 0.88f, 0.28f, 1.00f);
        drawLine(sr, cx, cy, s, 73.0f, 42.0f, 75.2f, 41.5f, 1.5f);
        drawLine(sr, cx, cy, s, 75.2f, 41.5f, 76.8f, 42.0f, 1.5f);
        drawLine(sr, cx, cy, s, 76.8f, 42.0f, 77.7f, 43.5f, 1.5f);
        drawLine(sr, cx, cy, s, 77.7f, 43.5f, 78.0f, 46.0f, 1.5f);
        drawLine(sr, cx, cy, s, 78.0f, 46.0f, 76.1f, 47.1f, 1.5f);
        drawLine(sr, cx, cy, s, 76.1f, 47.1f, 75.5f, 48.5f, 1.5f);
        drawLine(sr, cx, cy, s, 75.5f, 48.5f, 76.1f, 50.1f, 1.5f);
        drawLine(sr, cx, cy, s, 76.1f, 50.1f, 78.0f, 52.0f, 1.5f);
        drawLine(sr, cx, cy, s, 78.0f, 52.0f, 76.1f, 53.1f, 1.5f);
        drawLine(sr, cx, cy, s, 76.1f, 53.1f, 75.5f, 54.5f, 1.5f);
        drawLine(sr, cx, cy, s, 75.5f, 54.5f, 76.1f, 56.1f, 1.5f);
        drawLine(sr, cx, cy, s, 76.1f, 56.1f, 78.0f, 58.0f, 1.5f);
        drawLine(sr, cx, cy, s, 78.0f, 58.0f, 76.1f, 59.1f, 1.5f);
        drawLine(sr, cx, cy, s, 76.1f, 59.1f, 75.5f, 60.5f, 1.5f);
        drawLine(sr, cx, cy, s, 75.5f, 60.5f, 76.1f, 62.1f, 1.5f);
        drawLine(sr, cx, cy, s, 76.1f, 62.1f, 78.0f, 64.0f, 1.5f);
        sr.setColor(0.96f, 0.62f, 0.04f, 1.00f);
        drawLine(sr, cx, cy, s, 72.0f, 36.0f, 80.0f, 36.0f, 1.5f);
        drawLine(sr, cx, cy, s, 72.0f, 70.0f, 80.0f, 70.0f, 1.5f);
        sr.setColor(0.94f, 0.27f, 0.27f, 1.00f);
        fillCircle(sr, cx, cy, s, 43.0f, 48.0f, 2.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 42.5f, 47.5f, 0.6f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        drawLine(sr, cx, cy, s, 52.0f, 96.0f, 52.0f, 114.0f, 1.0f);
        drawLine(sr, cx, cy, s, 36.0f, 94.0f, 32.0f, 108.0f, 1.0f);
        drawLine(sr, cx, cy, s, 68.0f, 94.0f, 72.0f, 108.0f, 1.0f);
        // --- LAYER: specular_highlights ---
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 74.0f, 21.0f, 2.0f);
        fillTri(sr, cx, cy, s, 46.0f, 38.0f, 48.0f, 42.0f, 44.0f, 40.0f);
        drawLine(sr, cx, cy, s, 46.0f, 38.0f, 48.0f, 50.0f, 0.8f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.80f);
        drawLine(sr, cx, cy, s, 42.0f, 104.0f, 47.0f, 109.2f, 1.0f);
        drawLine(sr, cx, cy, s, 47.0f, 109.2f, 52.0f, 111.0f, 1.0f);
        drawLine(sr, cx, cy, s, 52.0f, 111.0f, 57.0f, 109.2f, 1.0f);
        drawLine(sr, cx, cy, s, 57.0f, 109.2f, 62.0f, 104.0f, 1.0f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.85f);
        drawLine(sr, cx, cy, s, 74.0f, 34.0f, 74.0f, 68.0f, 1.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 76.0f, 12.0f, 1.5f);
    }

    // =========================================================================
    // GENERIC FALLBACK
    // =========================================================================
    private static void drawGenericType(ShapeRenderer sr, EquipmentType type, float cx, float cy, float r, Color col) {
        sr.setColor(col);
        if (type == EquipmentType.ROD) {
            sr.rectLine(cx - r * 0.6f, cy - r * 0.6f, cx + r * 0.6f, cy + r * 0.6f, 3.5f);
            sr.circle(cx - r * 0.45f, cy - r * 0.45f, r * 0.16f);
        } else if (type == EquipmentType.LINE) {
            sr.circle(cx, cy, r * 0.65f);
            sr.setColor(0.06f, 0.08f, 0.14f, 1f);
            sr.circle(cx, cy, r * 0.35f);
            sr.setColor(col);
            sr.circle(cx, cy, r * 0.15f);
        } else {
            sr.rectLine(cx - r * 0.2f, cy + r * 0.6f, cx - r * 0.2f, cy - r * 0.3f, 3.5f);
            sr.rectLine(cx - r * 0.2f, cy - r * 0.3f, cx + r * 0.4f, cy - r * 0.3f, 3.5f);
            sr.rectLine(cx + r * 0.4f, cy - r * 0.3f, cx + r * 0.4f, cy + r * 0.25f, 3.5f);
            sr.triangle(cx + r * 0.4f, cy + r * 0.25f, cx + r * 0.2f, cy + r * 0.1f, cx + r * 0.55f, cy + r * 0.1f);
        }
    }

    // =========================================================================
    // HALLOWEEN LIMITED EVENT: DẠ HỘI BÍ NGÔ (4★)
    // =========================================================================
    public static void drawPumpkinRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Spooky festive aura: Phantom violet and radiant pumpkin fire
        sr.setColor(0.52f, 0.16f, 0.78f, 0.22f);
        sr.circle(cx, cy, r * 1.35f);
        sr.setColor(1.00f, 0.48f, 0.08f, 0.25f);
        sr.circle(cx, cy, r * 1.05f);

        // 2. Twisted Obsidian Thorn Rod Blank
        float startX = cx - r * 0.76f;
        float startY = cy - r * 0.76f;
        float endX = cx + r * 0.68f;
        float endY = cy + r * 0.68f;

        // Shadow core
        sr.setColor(0.12f, 0.08f, 0.18f, 1f);
        sr.rectLine(startX, startY, endX, endY, Math.max(4.0f, r * 0.15f));
        // Violet-tinted spine
        sr.setColor(0.25f, 0.16f, 0.35f, 1f);
        sr.rectLine(startX, startY, endX, endY, Math.max(2.6f, r * 0.10f));

        // Jack-o'-Lantern spiral vine wraps
        sr.setColor(0.96f, 0.52f, 0.10f, 1f);
        for (int i = 0; i < 4; i++) {
            float t = 0.22f + i * 0.18f;
            float wx = startX + (endX - startX) * t;
            float wy = startY + (endY - startY) * t;
            sr.rectLine(wx - 2.5f, wy + 2.5f, wx + 2.5f, wy - 2.5f, Math.max(2.0f, r * 0.07f));
        }

        // 3. Mini Carved Jack-o'-Lantern Reel / Accent
        float px = cx - r * 0.35f;
        float py = cy - r * 0.35f;
        float pr = Math.max(5f, r * 0.28f);

        // Pumpkin lobes
        sr.setColor(0.85f, 0.35f, 0.05f, 1f);
        sr.circle(px - pr * 0.35f, py, pr * 0.85f);
        sr.circle(px + pr * 0.35f, py, pr * 0.85f);
        sr.setColor(0.98f, 0.55f, 0.10f, 1f);
        sr.circle(px, py, pr);

        // Green stem on pumpkin
        sr.setColor(0.25f, 0.72f, 0.25f, 1f);
        sr.rectLine(px, py + pr * 0.8f, px + pr * 0.2f, py + pr * 1.35f, Math.max(1.8f, r * 0.06f));

        // Carved glowing eyes & jagged smile
        sr.setColor(1.0f, 0.95f, 0.30f, 1f);
        sr.triangle(px - pr * 0.45f, py + pr * 0.12f, px - pr * 0.15f, py + pr * 0.12f, px - pr * 0.30f, py + pr * 0.45f);
        sr.triangle(px + pr * 0.15f, py + pr * 0.12f, px + pr * 0.45f, py + pr * 0.12f, px + pr * 0.30f, py + pr * 0.45f);
        sr.triangle(px - pr * 0.35f, py - pr * 0.15f, px + pr * 0.35f, py - pr * 0.15f, px, py - pr * 0.50f);

        // Rod Guides: Gold and glowing ember
        sr.setColor(1.0f, 0.85f, 0.25f, 1f);
        sr.circle(endX, endY, Math.max(2.5f, r * 0.11f));
        sr.circle(cx + r * 0.25f, cy + r * 0.25f, Math.max(2.0f, r * 0.08f));
        sr.setColor(0.12f, 0.08f, 0.18f, 1f);
        sr.circle(endX, endY, Math.max(1.2f, r * 0.05f));

        // 4. Specular glint & magical ember
        sr.setColor(1f, 1f, 1f, 0.90f);
        sr.circle(px - pr * 0.25f, py + pr * 0.35f, Math.max(1.0f, r * 0.05f));
        sr.setColor(0.40f, 0.95f, 1.0f, 0.95f);
        sr.circle(endX + 2.5f, endY + 2.5f, Math.max(1.2f, r * 0.05f));
    }

    public static void drawSpiderSilkLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Cobweb & Phantom aura
        sr.setColor(0.45f, 0.18f, 0.72f, 0.24f);
        sr.circle(cx, cy, r * 1.35f);
        sr.setColor(0.15f, 0.75f, 0.85f, 0.16f);
        sr.circle(cx, cy, r * 1.02f);

        // 2. Midnight Obsidian Spool
        sr.setColor(0.12f, 0.08f, 0.18f, 1f);
        sr.circle(cx, cy, r * 0.85f);
        sr.setColor(0.68f, 0.32f, 0.95f, 1f);
        sr.circle(cx, cy, r * 0.78f);
        sr.setColor(0.08f, 0.05f, 0.12f, 1f);
        sr.circle(cx, cy, r * 0.68f);

        // 3. Intricate Spider Web Pattern (8 silk spokes + web rings)
        sr.setColor(0.85f, 0.92f, 1.0f, 0.88f);
        for (int i = 0; i < 8; i++) {
            float angle = (i * 45f) * MathUtils.degreesToRadians;
            float cos = MathUtils.cos(angle);
            float sin = MathUtils.sin(angle);
            sr.rectLine(cx, cy, cx + cos * (r * 0.65f), cy + sin * (r * 0.65f), Math.max(1.2f, r * 0.045f));
        }

        // Web Ring Arcs
        float[] ringRadii = { r * 0.32f, r * 0.52f };
        for (float rad : ringRadii) {
            for (int i = 0; i < 8; i++) {
                float a1 = (i * 45f) * MathUtils.degreesToRadians;
                float a2 = ((i + 1) * 45f) * MathUtils.degreesToRadians;
                sr.rectLine(cx + MathUtils.cos(a1) * rad, cy + MathUtils.sin(a1) * rad,
                            cx + MathUtils.cos(a2) * rad, cy + MathUtils.sin(a2) * rad, Math.max(1.0f, r * 0.035f));
            }
        }

        // Spooky Skull / Spider Center Crest
        sr.setColor(0.88f, 0.25f, 0.55f, 1f);
        sr.circle(cx, cy, r * 0.20f);
        sr.setColor(1.0f, 0.85f, 0.25f, 1f);
        sr.circle(cx - r * 0.07f, cy + r * 0.03f, Math.max(1.0f, r * 0.04f));
        sr.circle(cx + r * 0.07f, cy + r * 0.03f, Math.max(1.0f, r * 0.04f));

        // 4. Specular dewdrops
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(cx + r * 0.36f, cy + r * 0.36f, Math.max(1.2f, r * 0.05f));
        sr.circle(cx - r * 0.22f, cy + r * 0.46f, Math.max(1.0f, r * 0.04f));
    }

    public static void drawGhostCandyHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Ghostly mist aura (Spectral Mint & Candy Pink)
        sr.setColor(0.20f, 0.85f, 0.50f, 0.20f);
        sr.circle(cx, cy, r * 1.35f);
        sr.setColor(0.95f, 0.30f, 0.60f, 0.18f);
        sr.circle(cx, cy, r * 1.05f);

        // 2. Candy Cane Curved Hook Body
        float topX = cx - r * 0.20f;
        float topY = cy + r * 0.62f;
        float botY = cy - r * 0.30f;
        float bendX = cx + r * 0.15f;
        float bendY = cy - r * 0.55f;
        float tipX = cx + r * 0.42f;
        float tipY = cy + r * 0.15f;

        // Hook eyelet
        sr.setColor(0.95f, 0.85f, 0.25f, 1f);
        sr.circle(topX, topY, Math.max(3.5f, r * 0.15f));
        sr.setColor(0.08f, 0.06f, 0.12f, 1f);
        sr.circle(topX, topY, Math.max(1.8f, r * 0.07f));

        // Main striped candy cane hook shank & bend
        float hookW = Math.max(3.8f, r * 0.14f);
        sr.setColor(0.18f, 0.08f, 0.15f, 1f);
        sr.rectLine(topX, topY, topX, botY, hookW + 1.6f);
        sr.rectLine(topX, botY, bendX, bendY, hookW + 1.6f);
        sr.rectLine(bendX, bendY, tipX, tipY, hookW + 1.6f);

        // Candy cane striped body (Orange & Bone-white)
        sr.setColor(1.0f, 0.45f, 0.10f, 1f);
        sr.rectLine(topX, topY, topX, botY, hookW);
        sr.rectLine(topX, botY, bendX, bendY, hookW);
        sr.rectLine(bendX, bendY, tipX, tipY, hookW);

        // White spiral candy stripes
        sr.setColor(1.0f, 0.95f, 0.90f, 1f);
        float stripeW = Math.max(2.2f, r * 0.08f);
        sr.rectLine(topX - 3f, topY - r * 0.25f, topX + 3f, topY - r * 0.15f, stripeW);
        sr.rectLine(topX - 3f, topY - r * 0.55f, topX + 3f, topY - r * 0.45f, stripeW);
        sr.rectLine(bendX - 2f, bendY - 2f, bendX + 4f, bendY + 3f, stripeW);
        sr.rectLine(tipX - 4f, tipY - r * 0.18f, tipX + 2f, tipY - r * 0.08f, stripeW);

        // Sharp barb
        sr.setColor(0.98f, 0.92f, 0.35f, 1f);
        sr.triangle(tipX, tipY, tipX - r * 0.15f, tipY - r * 0.12f, tipX - r * 0.02f, tipY - r * 0.22f);

        // 3. Mini Ghost Charm near eyelet
        float gx = cx + r * 0.24f;
        float gy = cy + r * 0.42f;
        sr.setColor(0.95f, 0.98f, 1.0f, 0.95f);
        sr.circle(gx, gy, Math.max(3f, r * 0.14f));
        sr.triangle(gx - r * 0.12f, gy, gx + r * 0.12f, gy, gx, gy - r * 0.22f);
        // Ghost eyes
        sr.setColor(0.15f, 0.10f, 0.25f, 1f);
        sr.circle(gx - r * 0.04f, gy + r * 0.02f, Math.max(0.8f, r * 0.03f));
        sr.circle(gx + r * 0.04f, gy + r * 0.02f, Math.max(0.8f, r * 0.03f));

        // 4. Specular glint
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(topX - 1.5f, topY + 1.5f, Math.max(1.0f, r * 0.04f));
        sr.circle(tipX, tipY, Math.max(1.2f, r * 0.045f));
    }

    // =========================================================================
    // HALLOWEEN EVENT ITEMS: PUMPKIN LANTERN & TRICK-OR-TREAT CANDY
    // =========================================================================
    public static void drawPumpkinLantern(ShapeRenderer sr, float cx, float cy, float r) {
        // 1. Radiant fiery pumpkin aura
        sr.setColor(1.0f, 0.42f, 0.05f, 0.28f);
        sr.circle(cx, cy, r * 1.35f);
        sr.setColor(1.0f, 0.85f, 0.20f, 0.18f);
        sr.circle(cx, cy, r * 0.95f);

        // 2. Segmented Pumpkin Lobes
        sr.setColor(0.80f, 0.30f, 0.02f, 1f);
        sr.circle(cx - r * 0.42f, cy - r * 0.05f, r * 0.52f);
        sr.circle(cx + r * 0.42f, cy - r * 0.05f, r * 0.52f);

        sr.setColor(0.95f, 0.48f, 0.05f, 1f);
        sr.circle(cx - r * 0.22f, cy - r * 0.03f, r * 0.58f);
        sr.circle(cx + r * 0.22f, cy - r * 0.03f, r * 0.58f);

        sr.setColor(1.00f, 0.60f, 0.08f, 1f);
        sr.circle(cx, cy - r * 0.02f, r * 0.60f);

        // Pumpkin Stem
        sr.setColor(0.22f, 0.55f, 0.18f, 1f);
        sr.rectLine(cx, cy + r * 0.50f, cx + r * 0.15f, cy + r * 0.92f, Math.max(2.5f, r * 0.10f));
        sr.setColor(0.40f, 0.80f, 0.25f, 1f);
        sr.circle(cx + r * 0.15f, cy + r * 0.92f, Math.max(1.5f, r * 0.06f));

        // 3. Carved Glowing Fiery Face (Sinister Jack-o'-Lantern)
        sr.setColor(1.0f, 0.95f, 0.32f, 1f);
        // Left Eye
        sr.triangle(cx - r * 0.36f, cy + r * 0.08f, cx - r * 0.12f, cy + r * 0.08f, cx - r * 0.24f, cy + r * 0.26f);
        // Right Eye
        sr.triangle(cx + r * 0.12f, cy + r * 0.08f, cx + r * 0.36f, cy + r * 0.08f, cx + r * 0.24f, cy + r * 0.26f);
        // Nose
        sr.triangle(cx - r * 0.08f, cy - r * 0.02f, cx + r * 0.08f, cy - r * 0.02f, cx, cy + r * 0.10f);
        // Jagged Smile
        sr.triangle(cx - r * 0.45f, cy - r * 0.10f, cx - r * 0.20f, cy - r * 0.35f, cx - r * 0.10f, cy - r * 0.10f);
        sr.triangle(cx - r * 0.10f, cy - r * 0.10f, cx, cy - r * 0.42f, cx + r * 0.10f, cy - r * 0.10f);
        sr.triangle(cx + r * 0.10f, cy - r * 0.10f, cx + r * 0.20f, cy - r * 0.35f, cx + r * 0.45f, cy - r * 0.10f);

        // Inner incandescent core
        sr.setColor(1.0f, 1.0f, 0.75f, 1f);
        sr.circle(cx, cy - r * 0.18f, Math.max(1.8f, r * 0.08f));

        // 4. Specular Highlights
        sr.setColor(1f, 1f, 1f, 0.85f);
        sr.circle(cx - r * 0.25f, cy + r * 0.38f, Math.max(1.2f, r * 0.05f));
    }

    public static void drawTrickOrTreatCandy(ShapeRenderer sr, float cx, float cy, float r) {
        // 1. Spooky sweet violet glow
        sr.setColor(0.68f, 0.22f, 0.88f, 0.25f);
        sr.circle(cx, cy, r * 1.30f);
        sr.setColor(1.0f, 0.55f, 0.10f, 0.20f);
        sr.circle(cx, cy, r * 0.95f);

        // 2. Wrapper Twists (Left & Right Fans)
        float twistW = r * 0.65f;
        float twistH = r * 0.48f;

        // Left wrapper
        sr.setColor(0.55f, 0.18f, 0.75f, 1f);
        sr.triangle(cx - r * 0.25f, cy, cx - twistW - r * 0.25f, cy + twistH, cx - twistW - r * 0.25f, cy - twistH);
        sr.setColor(0.85f, 0.35f, 0.95f, 1f);
        sr.triangle(cx - r * 0.25f, cy, cx - twistW - r * 0.20f, cy + twistH * 0.6f, cx - twistW - r * 0.20f, cy - twistH * 0.6f);

        // Right wrapper
        sr.setColor(0.55f, 0.18f, 0.75f, 1f);
        sr.triangle(cx + r * 0.25f, cy, cx + twistW + r * 0.25f, cy + twistH, cx + twistW + r * 0.25f, cy - twistH);
        sr.setColor(0.85f, 0.35f, 0.95f, 1f);
        sr.triangle(cx + r * 0.25f, cy, cx + twistW + r * 0.20f, cy + twistH * 0.6f, cx + twistW + r * 0.20f, cy - twistH * 0.6f);

        // 3. Central Round Candy Ball
        float cr = r * 0.55f;
        sr.setColor(0.98f, 0.52f, 0.08f, 1f);
        sr.circle(cx, cy, cr);

        // Spiral candy stripes
        sr.setColor(1.0f, 0.95f, 0.90f, 1f);
        sr.rectLine(cx - cr * 0.7f, cy - cr * 0.4f, cx + cr * 0.7f, cy + cr * 0.4f, Math.max(2.2f, r * 0.09f));
        sr.setColor(0.25f, 0.12f, 0.35f, 1f);
        sr.rectLine(cx - cr * 0.5f, cy + cr * 0.5f, cx + cr * 0.5f, cy - cr * 0.5f, Math.max(2.2f, r * 0.09f));

        // Golden Cinch Rings
        sr.setColor(1.0f, 0.85f, 0.25f, 1f);
        sr.rectLine(cx - r * 0.25f, cy - r * 0.22f, cx - r * 0.25f, cy + r * 0.22f, Math.max(2.0f, r * 0.07f));
        sr.rectLine(cx + r * 0.25f, cy - r * 0.22f, cx + r * 0.25f, cy + r * 0.22f, Math.max(2.0f, r * 0.07f));

        // 4. Glossy Specular Highlight
        sr.setColor(1f, 1f, 1f, 0.90f);
        sr.circle(cx - cr * 0.30f, cy + cr * 0.35f, Math.max(1.2f, r * 0.06f));
    }

    // =========================================================================
    // 17. HUYẾT NGUYỆT THẦN VƯƠNG SET (6★ LIMITED PREMIUM - HALLOWEEN EXCLUSIVE)
    // =========================================================================
    public static void drawHuyetNguyetRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        // 1. Cataclysmic Blood Moon Eclipse & Crimson Flame Halo
        sr.setColor(0.60f, 0.11f, 0.11f, 0.28f);
        fillCircle(sr, cx, cy, s, 86.0f, 40.0f, 46.0f);
        sr.setColor(0.94f, 0.27f, 0.27f, 0.25f);
        fillCircle(sr, cx, cy, s, 86.0f, 40.0f, 34.0f);
        sr.setColor(0.35f, 0.11f, 0.53f, 0.20f);
        fillCircle(sr, cx, cy, s, 50.0f, 76.0f, 38.0f);

        // 2. Demonic Vertebrae War-Staff Blank (Diagonal Stance)
        sr.setColor(0.04f, 0.01f, 0.01f, 1.0f);
        drawLine(sr, cx, cy, s, 18.0f, 110.0f, 86.0f, 42.0f, 9.0f);
        sr.setColor(0.27f, 0.04f, 0.04f, 1.0f);
        drawLine(sr, cx, cy, s, 18.0f, 110.0f, 86.0f, 42.0f, 6.5f);
        sr.setColor(0.60f, 0.11f, 0.11f, 1.0f);
        drawLine(sr, cx, cy, s, 20.0f, 108.0f, 84.0f, 44.0f, 4.0f);
        sr.setColor(0.94f, 0.27f, 0.27f, 1.0f);
        drawLine(sr, cx, cy, s, 20.0f, 108.0f, 84.0f, 44.0f, 1.6f);
        sr.setColor(1.00f, 0.80f, 0.80f, 1.0f);
        drawLine(sr, cx, cy, s, 21.0f, 107.0f, 83.0f, 45.0f, 0.8f);

        // Dragon Vertebrae Rib Plates Along Staff
        sr.setColor(0.97f, 0.44f, 0.44f, 1.0f);
        fillTri(sr, cx, cy, s, 34.0f, 94.0f, 40.0f, 88.0f, 38.0f, 96.0f);
        fillTri(sr, cx, cy, s, 46.0f, 82.0f, 52.0f, 76.0f, 50.0f, 84.0f);
        fillTri(sr, cx, cy, s, 58.0f, 70.0f, 64.0f, 64.0f, 62.0f, 72.0f);
        fillTri(sr, cx, cy, s, 70.0f, 58.0f, 76.0f, 52.0f, 74.0f, 60.0f);

        // 3. Gigantic Crescent Blood Moon Reaper Scythe Blade
        // Outer Blade Silhouette
        sr.setColor(0.09f, 0.02f, 0.02f, 1.0f);
        drawLine(sr, cx, cy, s, 76.0f, 50.0f, 72.0f, 30.0f, 6.0f);
        drawLine(sr, cx, cy, s, 72.0f, 30.0f, 90.0f, 8.0f, 6.5f);
        drawLine(sr, cx, cy, s, 90.0f, 8.0f, 118.0f, 6.0f, 5.5f);
        drawLine(sr, cx, cy, s, 118.0f, 6.0f, 124.0f, 11.0f, 4.5f);
        drawLine(sr, cx, cy, s, 124.0f, 11.0f, 114.0f, 24.0f, 5.0f);
        drawLine(sr, cx, cy, s, 114.0f, 24.0f, 102.0f, 36.0f, 5.5f);
        drawLine(sr, cx, cy, s, 102.0f, 36.0f, 94.0f, 54.0f, 6.0f);
        fillTri(sr, cx, cy, s, 76.0f, 50.0f, 90.0f, 8.0f, 118.0f, 6.0f);
        fillTri(sr, cx, cy, s, 76.0f, 50.0f, 118.0f, 6.0f, 102.0f, 36.0f);
        fillTri(sr, cx, cy, s, 76.0f, 50.0f, 102.0f, 36.0f, 94.0f, 54.0f);

        // Radiant Blood Crimson Mid-Blade
        sr.setColor(0.86f, 0.15f, 0.15f, 1.0f);
        drawLine(sr, cx, cy, s, 78.0f, 48.0f, 76.0f, 32.0f, 3.5f);
        drawLine(sr, cx, cy, s, 76.0f, 32.0f, 92.0f, 12.0f, 3.5f);
        drawLine(sr, cx, cy, s, 92.0f, 12.0f, 116.0f, 9.0f, 3.0f);
        drawLine(sr, cx, cy, s, 116.0f, 9.0f, 113.0f, 21.0f, 3.0f);
        drawLine(sr, cx, cy, s, 113.0f, 21.0f, 103.0f, 34.0f, 3.5f);
        drawLine(sr, cx, cy, s, 103.0f, 34.0f, 94.0f, 50.0f, 3.5f);
        fillTri(sr, cx, cy, s, 78.0f, 48.0f, 92.0f, 12.0f, 113.0f, 21.0f);
        fillTri(sr, cx, cy, s, 78.0f, 48.0f, 113.0f, 21.0f, 94.0f, 50.0f);

        // Razor Edge Line
        sr.setColor(0.99f, 0.65f, 0.65f, 1.0f);
        drawLine(sr, cx, cy, s, 80.0f, 46.0f, 94.0f, 15.0f, 1.8f);
        drawLine(sr, cx, cy, s, 94.0f, 15.0f, 114.0f, 11.0f, 1.8f);
        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 80.0f, 46.0f, 94.0f, 15.0f, 0.8f);
        drawLine(sr, cx, cy, s, 94.0f, 15.0f, 114.0f, 11.0f, 0.8f);

        // Serrated Teeth
        sr.setColor(0.97f, 0.44f, 0.44f, 1.0f);
        fillTri(sr, cx, cy, s, 98.0f, 40.0f, 102.0f, 42.0f, 96.0f, 46.0f);
        fillTri(sr, cx, cy, s, 106.0f, 28.0f, 110.0f, 31.0f, 104.0f, 34.0f);
        fillTri(sr, cx, cy, s, 113.0f, 17.0f, 116.0f, 20.0f, 111.0f, 23.0f);

        // 4. Blood Moon Eclipse Core Orb at (86, 42)
        sr.setColor(0.94f, 0.27f, 0.27f, 1.0f);
        fillTri(sr, cx, cy, s, 86.0f, 22.0f, 83.0f, 30.0f, 89.0f, 30.0f);
        fillTri(sr, cx, cy, s, 106.0f, 42.0f, 98.0f, 39.0f, 98.0f, 45.0f);
        fillTri(sr, cx, cy, s, 86.0f, 62.0f, 89.0f, 54.0f, 83.0f, 54.0f);
        fillTri(sr, cx, cy, s, 66.0f, 42.0f, 74.0f, 45.0f, 74.0f, 39.0f);
        sr.setColor(0.97f, 0.44f, 0.44f, 1.0f);
        fillTri(sr, cx, cy, s, 100.0f, 28.0f, 92.0f, 34.0f, 96.0f, 38.0f);
        fillTri(sr, cx, cy, s, 100.0f, 56.0f, 96.0f, 48.0f, 92.0f, 52.0f);
        fillTri(sr, cx, cy, s, 72.0f, 56.0f, 80.0f, 52.0f, 76.0f, 48.0f);
        fillTri(sr, cx, cy, s, 72.0f, 28.0f, 76.0f, 38.0f, 80.0f, 34.0f);

        // Blood Moon Sphere
        sr.setColor(0.50f, 0.11f, 0.11f, 1.0f);
        fillCircle(sr, cx, cy, s, 86.0f, 42.0f, 14.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.0f);
        fillCircle(sr, cx, cy, s, 86.0f, 42.0f, 11.0f);
        sr.setColor(0.11f, 0.02f, 0.02f, 1.0f);
        fillCircle(sr, cx, cy, s, 84.0f, 42.0f, 8.5f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.0f);
        fillCircle(sr, cx, cy, s, 87.0f, 42.0f, 7.0f);

        // 5. Demonic Spiked Butt Cap with Chains & Blood Drops
        sr.setColor(0.11f, 0.02f, 0.02f, 1.0f);
        fillCircle(sr, cx, cy, s, 16.0f, 112.0f, 6.0f);
        sr.setColor(0.60f, 0.11f, 0.11f, 1.0f);
        fillCircle(sr, cx, cy, s, 16.0f, 112.0f, 5.0f);
        sr.setColor(0.94f, 0.27f, 0.27f, 1.0f);
        fillTri(sr, cx, cy, s, 16.0f, 112.0f, 10.0f, 124.0f, 15.0f, 121.0f);
        fillTri(sr, cx, cy, s, 16.0f, 112.0f, 16.0f, 126.0f, 19.0f, 121.0f);
        fillTri(sr, cx, cy, s, 16.0f, 112.0f, 19.0f, 121.0f, 24.0f, 124.0f);

        // Falling Blood Drops
        fillCircle(sr, cx, cy, s, 120.0f, 16.0f, 2.2f);
        fillTri(sr, cx, cy, s, 120.0f, 13.0f, 118.0f, 17.0f, 122.0f, 17.0f);
        fillCircle(sr, cx, cy, s, 112.0f, 28.0f, 1.6f);

        // 6. Specular Highlights & Glints
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 124.0f, 9.0f, 2.0f);
        fillCircle(sr, cx, cy, s, 86.0f, 40.0f, 2.2f);
        fillCircle(sr, cx, cy, s, 80.0f, 46.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 16.0f, 110.0f, 1.5f);
    }

    public static void drawHuyetNguyetLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        // 1. Atmospheric Blood Moon Void & Miasma Aureole
        sr.setColor(0.72f, 0.11f, 0.11f, 0.20f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 58.0f);
        sr.setColor(0.35f, 0.11f, 0.53f, 0.22f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 46.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 0.24f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 34.0f);

        // 2. Four Gothic Demon Lord Wings Spanning Diagonal Corners (Unique Silhouette)
        // NW Wing
        sr.setColor(0.09f, 0.01f, 0.02f, 1.0f);
        fillTri(sr, cx, cy, s, 14.0f, 14.0f, 36.0f, 44.0f, 44.0f, 36.0f);
        sr.setColor(0.60f, 0.11f, 0.11f, 1.0f);
        fillTri(sr, cx, cy, s, 14.0f, 14.0f, 26.0f, 38.0f, 36.0f, 44.0f);
        sr.setColor(0.27f, 0.04f, 0.04f, 1.0f);
        fillTri(sr, cx, cy, s, 14.0f, 14.0f, 38.0f, 26.0f, 44.0f, 36.0f);
        sr.setColor(0.94f, 0.27f, 0.27f, 1.0f);
        fillTri(sr, cx, cy, s, 8.0f, 26.0f, 18.0f, 24.0f, 24.0f, 32.0f);

        // NE Wing
        sr.setColor(0.09f, 0.01f, 0.02f, 1.0f);
        fillTri(sr, cx, cy, s, 114.0f, 14.0f, 92.0f, 36.0f, 100.0f, 44.0f);
        sr.setColor(0.60f, 0.11f, 0.11f, 1.0f);
        fillTri(sr, cx, cy, s, 114.0f, 14.0f, 92.0f, 36.0f, 102.0f, 26.0f);
        sr.setColor(0.27f, 0.04f, 0.04f, 1.0f);
        fillTri(sr, cx, cy, s, 114.0f, 14.0f, 102.0f, 38.0f, 100.0f, 44.0f);
        sr.setColor(0.94f, 0.27f, 0.27f, 1.0f);
        fillTri(sr, cx, cy, s, 120.0f, 26.0f, 104.0f, 32.0f, 110.0f, 24.0f);

        // SE Wing
        sr.setColor(0.09f, 0.01f, 0.02f, 1.0f);
        fillTri(sr, cx, cy, s, 114.0f, 114.0f, 100.0f, 84.0f, 92.0f, 92.0f);
        sr.setColor(0.60f, 0.11f, 0.11f, 1.0f);
        fillTri(sr, cx, cy, s, 114.0f, 114.0f, 102.0f, 90.0f, 100.0f, 84.0f);
        sr.setColor(0.27f, 0.04f, 0.04f, 1.0f);
        fillTri(sr, cx, cy, s, 114.0f, 114.0f, 90.0f, 102.0f, 92.0f, 92.0f);
        sr.setColor(0.94f, 0.27f, 0.27f, 1.0f);
        fillTri(sr, cx, cy, s, 120.0f, 102.0f, 110.0f, 104.0f, 104.0f, 96.0f);

        // SW Wing
        sr.setColor(0.09f, 0.01f, 0.02f, 1.0f);
        fillTri(sr, cx, cy, s, 14.0f, 114.0f, 44.0f, 92.0f, 36.0f, 84.0f);
        sr.setColor(0.60f, 0.11f, 0.11f, 1.0f);
        fillTri(sr, cx, cy, s, 14.0f, 114.0f, 38.0f, 102.0f, 44.0f, 92.0f);
        sr.setColor(0.27f, 0.04f, 0.04f, 1.0f);
        fillTri(sr, cx, cy, s, 14.0f, 114.0f, 26.0f, 90.0f, 36.0f, 84.0f);
        sr.setColor(0.94f, 0.27f, 0.27f, 1.0f);
        fillTri(sr, cx, cy, s, 8.0f, 102.0f, 24.0f, 96.0f, 18.0f, 104.0f);

        // 3. Heavy Obsidian Dragon-Bone Spool Rim (Bát Giác Cấm Thuật)
        sr.setColor(0.05f, 0.01f, 0.02f, 1.0f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 49.0f);
        sr.setColor(0.14f, 0.02f, 0.03f, 1.0f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 45.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.0f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 41.0f);
        sr.setColor(0.96f, 0.62f, 0.04f, 1.0f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 39.0f);
        sr.setColor(0.04f, 0.01f, 0.01f, 1.0f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 37.0f);

        // 8 Curved Demon Dragon Fangs Lining the Rim
        sr.setColor(0.94f, 0.27f, 0.27f, 1.0f);
        fillTri(sr, cx, cy, s, 64.0f, 8.0f, 58.0f, 20.0f, 70.0f, 20.0f);
        fillTri(sr, cx, cy, s, 106.0f, 22.0f, 96.0f, 28.0f, 102.0f, 36.0f);
        fillTri(sr, cx, cy, s, 120.0f, 64.0f, 108.0f, 58.0f, 108.0f, 70.0f);
        fillTri(sr, cx, cy, s, 106.0f, 106.0f, 102.0f, 92.0f, 96.0f, 100.0f);
        fillTri(sr, cx, cy, s, 64.0f, 120.0f, 70.0f, 108.0f, 58.0f, 108.0f);
        fillTri(sr, cx, cy, s, 22.0f, 106.0f, 26.0f, 92.0f, 32.0f, 100.0f);
        fillTri(sr, cx, cy, s, 8.0f, 64.0f, 20.0f, 70.0f, 20.0f, 58.0f);
        fillTri(sr, cx, cy, s, 22.0f, 22.0f, 32.0f, 28.0f, 26.0f, 36.0f);

        // 4. Sacred Blood Geometry Star Weave (Tơ Huyết Ngôi Sao Bát Giác)
        // Diamond 1
        sr.setColor(0.60f, 0.11f, 0.11f, 1.0f);
        drawLine(sr, cx, cy, s, 64.0f, 26.0f, 102.0f, 64.0f, 2.6f);
        drawLine(sr, cx, cy, s, 102.0f, 64.0f, 64.0f, 102.0f, 2.6f);
        drawLine(sr, cx, cy, s, 64.0f, 102.0f, 26.0f, 64.0f, 2.6f);
        drawLine(sr, cx, cy, s, 26.0f, 64.0f, 64.0f, 26.0f, 2.6f);
        // Diamond 2 (Rotated 45 deg)
        drawLine(sr, cx, cy, s, 91.0f, 37.0f, 91.0f, 91.0f, 2.6f);
        drawLine(sr, cx, cy, s, 91.0f, 91.0f, 37.0f, 91.0f, 2.6f);
        drawLine(sr, cx, cy, s, 37.0f, 91.0f, 37.0f, 37.0f, 2.6f);
        drawLine(sr, cx, cy, s, 37.0f, 37.0f, 91.0f, 37.0f, 2.6f);

        // Radiant Crimson Star Highlights
        sr.setColor(0.94f, 0.27f, 0.27f, 1.0f);
        drawLine(sr, cx, cy, s, 64.0f, 28.0f, 100.0f, 64.0f, 1.5f);
        drawLine(sr, cx, cy, s, 100.0f, 64.0f, 64.0f, 100.0f, 1.5f);
        drawLine(sr, cx, cy, s, 64.0f, 100.0f, 28.0f, 64.0f, 1.5f);
        drawLine(sr, cx, cy, s, 28.0f, 64.0f, 64.0f, 28.0f, 1.5f);
        drawLine(sr, cx, cy, s, 89.0f, 39.0f, 89.0f, 89.0f, 1.5f);
        drawLine(sr, cx, cy, s, 89.0f, 89.0f, 39.0f, 89.0f, 1.5f);
        drawLine(sr, cx, cy, s, 39.0f, 89.0f, 39.0f, 39.0f, 1.5f);
        drawLine(sr, cx, cy, s, 39.0f, 39.0f, 89.0f, 39.0f, 1.5f);

        // Circular Arterial Coils
        sr.setColor(0.27f, 0.04f, 0.04f, 1.0f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 28.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.0f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 26.0f);
        sr.setColor(0.97f, 0.44f, 0.44f, 1.0f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 23.0f);
        sr.setColor(0.09f, 0.02f, 0.02f, 1.0f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 21.0f);

        // 5. Demonic Spool Core: The Faceted Eclipse Blood Heart
        sr.setColor(0.96f, 0.62f, 0.04f, 1.0f);
        fillTri(sr, cx, cy, s, 64.0f, 44.0f, 84.0f, 64.0f, 64.0f, 84.0f);
        fillTri(sr, cx, cy, s, 64.0f, 44.0f, 44.0f, 64.0f, 64.0f, 84.0f);
        // Top-Left Facet
        sr.setColor(0.94f, 0.27f, 0.27f, 1.0f);
        fillTri(sr, cx, cy, s, 64.0f, 46.0f, 46.0f, 64.0f, 64.0f, 64.0f);
        // Top-Right Facet
        sr.setColor(0.86f, 0.15f, 0.15f, 1.0f);
        fillTri(sr, cx, cy, s, 64.0f, 46.0f, 82.0f, 64.0f, 64.0f, 64.0f);
        // Bottom-Left Facet
        sr.setColor(0.60f, 0.11f, 0.11f, 1.0f);
        fillTri(sr, cx, cy, s, 46.0f, 64.0f, 64.0f, 82.0f, 64.0f, 64.0f);
        // Bottom-Right Facet
        sr.setColor(0.35f, 0.03f, 0.07f, 1.0f);
        fillTri(sr, cx, cy, s, 82.0f, 64.0f, 64.0f, 82.0f, 64.0f, 64.0f);

        // 4 Crimson Tendrils Grasping Heart
        sr.setColor(0.99f, 0.65f, 0.65f, 1.0f);
        drawLine(sr, cx, cy, s, 64.0f, 34.0f, 64.0f, 46.0f, 1.5f);
        drawLine(sr, cx, cy, s, 94.0f, 64.0f, 82.0f, 64.0f, 1.5f);
        drawLine(sr, cx, cy, s, 64.0f, 94.0f, 64.0f, 82.0f, 1.5f);
        drawLine(sr, cx, cy, s, 34.0f, 64.0f, 46.0f, 64.0f, 1.5f);

        // All-Seeing Demon Eye in Core
        sr.setColor(0.05f, 0.01f, 0.01f, 1.0f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 9.5f);
        sr.setColor(0.94f, 0.27f, 0.27f, 1.0f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 8.2f);
        sr.setColor(0.50f, 0.11f, 0.11f, 1.0f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 7.0f);
        sr.setColor(0.03f, 0.00f, 0.01f, 1.0f);
        fillTri(sr, cx, cy, s, 64.0f, 58.0f, 62.0f, 64.0f, 66.0f, 64.0f);
        fillTri(sr, cx, cy, s, 64.0f, 70.0f, 62.0f, 64.0f, 66.0f, 64.0f);
        // Burning Ember Slit Pupil Core
        sr.setColor(0.98f, 0.80f, 0.08f, 1.0f);
        fillTri(sr, cx, cy, s, 64.0f, 60.2f, 63.1f, 64.0f, 64.9f, 64.0f);
        fillTri(sr, cx, cy, s, 64.0f, 67.8f, 63.1f, 64.0f, 64.9f, 64.0f);

        // 6. Floating Blood Crystal Shards
        sr.setColor(0.94f, 0.27f, 0.27f, 1.0f);
        fillTri(sr, cx, cy, s, 64.0f, 3.0f, 67.0f, 8.0f, 61.0f, 8.0f);
        fillTri(sr, cx, cy, s, 64.0f, 13.0f, 67.0f, 8.0f, 61.0f, 8.0f);
        fillTri(sr, cx, cy, s, 125.0f, 64.0f, 120.0f, 67.0f, 120.0f, 61.0f);
        fillTri(sr, cx, cy, s, 115.0f, 64.0f, 120.0f, 67.0f, 120.0f, 61.0f);
        fillTri(sr, cx, cy, s, 64.0f, 125.0f, 61.0f, 120.0f, 67.0f, 120.0f);
        fillTri(sr, cx, cy, s, 64.0f, 115.0f, 61.0f, 120.0f, 67.0f, 120.0f);
        fillTri(sr, cx, cy, s, 3.0f, 64.0f, 8.0f, 61.0f, 8.0f, 67.0f);
        fillTri(sr, cx, cy, s, 13.0f, 64.0f, 8.0f, 61.0f, 8.0f, 67.0f);

        // 7. Diamond Specular Glints & Brilliant Razor Sparkles
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 14.0f, 14.0f, 2.0f);
        fillCircle(sr, cx, cy, s, 114.0f, 14.0f, 2.0f);
        fillCircle(sr, cx, cy, s, 114.0f, 114.0f, 2.0f);
        fillCircle(sr, cx, cy, s, 14.0f, 114.0f, 2.0f);
        fillCircle(sr, cx, cy, s, 64.0f, 46.0f, 1.6f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 1.2f);
        fillCircle(sr, cx, cy, s, 46.0f, 64.0f, 1.2f);
    }

    public static void drawHuyetNguyetHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        float s = r / 64f;

        // 1. Atmospheric Blood Moon Void & Miasma Aureole
        sr.setColor(0.72f, 0.11f, 0.11f, 0.20f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 58.0f);
        sr.setColor(0.35f, 0.11f, 0.53f, 0.22f);
        fillCircle(sr, cx, cy, s, 48.0f, 40.0f, 38.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 0.24f);
        fillCircle(sr, cx, cy, s, 96.0f, 80.0f, 32.0f);

        // 2. Demonic Horned Skull Crown Top (Khoen Vương Miện Sừng Rồng Ma Vương)
        // Left Horn Body
        sr.setColor(0.09f, 0.02f, 0.02f, 1.0f);
        fillTri(sr, cx, cy, s, 44.0f, 22.0f, 22.0f, 12.0f, 14.0f, 20.0f);
        fillTri(sr, cx, cy, s, 44.0f, 22.0f, 14.0f, 20.0f, 28.0f, 26.0f);
        fillTri(sr, cx, cy, s, 44.0f, 22.0f, 28.0f, 26.0f, 38.0f, 25.0f);
        sr.setColor(0.60f, 0.11f, 0.11f, 1.0f);
        fillTri(sr, cx, cy, s, 42.0f, 22.0f, 24.0f, 15.0f, 17.0f, 20.0f);
        fillTri(sr, cx, cy, s, 42.0f, 22.0f, 17.0f, 20.0f, 30.0f, 24.0f);
        // Left Horn Spines
        sr.setColor(0.94f, 0.27f, 0.27f, 1.0f);
        fillTri(sr, cx, cy, s, 26.0f, 13.0f, 22.0f, 7.0f, 30.0f, 12.0f);
        fillTri(sr, cx, cy, s, 36.0f, 17.0f, 33.0f, 10.0f, 40.0f, 16.0f);

        // Right Horn Body
        sr.setColor(0.09f, 0.02f, 0.02f, 1.0f);
        fillTri(sr, cx, cy, s, 52.0f, 22.0f, 74.0f, 12.0f, 82.0f, 20.0f);
        fillTri(sr, cx, cy, s, 52.0f, 22.0f, 82.0f, 20.0f, 68.0f, 26.0f);
        fillTri(sr, cx, cy, s, 52.0f, 22.0f, 68.0f, 26.0f, 58.0f, 25.0f);
        sr.setColor(0.27f, 0.04f, 0.04f, 1.0f);
        fillTri(sr, cx, cy, s, 54.0f, 22.0f, 72.0f, 15.0f, 79.0f, 20.0f);
        fillTri(sr, cx, cy, s, 54.0f, 22.0f, 79.0f, 20.0f, 66.0f, 24.0f);
        // Right Horn Spines
        sr.setColor(0.94f, 0.27f, 0.27f, 1.0f);
        fillTri(sr, cx, cy, s, 70.0f, 13.0f, 74.0f, 7.0f, 66.0f, 12.0f);
        fillTri(sr, cx, cy, s, 60.0f, 17.0f, 63.0f, 10.0f, 56.0f, 16.0f);

        // Central Demon Skull Crown Eyelet
        sr.setColor(0.96f, 0.62f, 0.04f, 1.0f);
        fillTri(sr, cx, cy, s, 48.0f, 13.0f, 54.0f, 18.0f, 42.0f, 18.0f);
        fillTri(sr, cx, cy, s, 48.0f, 29.0f, 54.0f, 18.0f, 42.0f, 18.0f);
        sr.setColor(0.04f, 0.01f, 0.01f, 1.0f);
        fillCircle(sr, cx, cy, s, 48.0f, 22.0f, 8.5f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.0f);
        fillCircle(sr, cx, cy, s, 48.0f, 22.0f, 6.8f);
        sr.setColor(0.50f, 0.11f, 0.11f, 1.0f);
        fillCircle(sr, cx, cy, s, 48.0f, 22.0f, 5.0f);
        sr.setColor(0.03f, 0.00f, 0.01f, 1.0f);
        fillCircle(sr, cx, cy, s, 48.0f, 22.0f, 2.8f);

        // Demonic Chain Wrapping Skull Neck
        sr.setColor(0.96f, 0.62f, 0.04f, 1.0f);
        drawLine(sr, cx, cy, s, 42.0f, 29.0f, 54.0f, 29.0f, 3.5f);
        sr.setColor(0.09f, 0.02f, 0.02f, 1.0f);
        fillCircle(sr, cx, cy, s, 39.0f, 31.0f, 2.2f);
        fillCircle(sr, cx, cy, s, 37.0f, 36.0f, 2.0f);

        // 3. Segmented Dragon Vertebrae Spine Shank (Thân Đốt Sống Rồng)
        // Dorsal Bone Spines
        sr.setColor(0.86f, 0.15f, 0.15f, 1.0f);
        fillTri(sr, cx, cy, s, 42.0f, 42.0f, 32.0f, 46.0f, 43.0f, 50.0f);
        fillTri(sr, cx, cy, s, 42.0f, 56.0f, 30.0f, 62.0f, 43.0f, 66.0f);
        fillTri(sr, cx, cy, s, 43.0f, 70.0f, 32.0f, 77.0f, 44.0f, 81.0f);

        // Layer 1: Obsidian Black Outline / Shadow
        sr.setColor(0.03f, 0.005f, 0.01f, 1.0f);
        drawLine(sr, cx, cy, s, 48.0f, 31.0f, 48.0f, 78.0f, 11.0f);
        drawLine(sr, cx, cy, s, 48.0f, 78.0f, 52.0f, 96.0f, 11.0f);
        drawLine(sr, cx, cy, s, 52.0f, 96.0f, 64.0f, 110.0f, 11.0f);
        drawLine(sr, cx, cy, s, 64.0f, 110.0f, 78.0f, 114.0f, 11.0f);
        drawLine(sr, cx, cy, s, 78.0f, 114.0f, 94.0f, 108.0f, 11.0f);
        drawLine(sr, cx, cy, s, 94.0f, 108.0f, 104.0f, 92.0f, 11.0f);
        drawLine(sr, cx, cy, s, 104.0f, 92.0f, 108.0f, 68.0f, 11.0f);

        // Layer 2: Dark Ruby Spine
        sr.setColor(0.27f, 0.04f, 0.04f, 1.0f);
        drawLine(sr, cx, cy, s, 48.0f, 31.0f, 48.0f, 78.0f, 8.0f);
        drawLine(sr, cx, cy, s, 48.0f, 78.0f, 52.0f, 96.0f, 8.0f);
        drawLine(sr, cx, cy, s, 52.0f, 96.0f, 64.0f, 110.0f, 8.0f);
        drawLine(sr, cx, cy, s, 64.0f, 110.0f, 78.0f, 114.0f, 8.0f);
        drawLine(sr, cx, cy, s, 78.0f, 114.0f, 94.0f, 108.0f, 8.0f);
        drawLine(sr, cx, cy, s, 94.0f, 108.0f, 104.0f, 92.0f, 8.0f);
        drawLine(sr, cx, cy, s, 104.0f, 92.0f, 108.0f, 68.0f, 8.0f);

        // Layer 3: Blood Crimson Vein Core
        sr.setColor(0.86f, 0.15f, 0.15f, 1.0f);
        drawLine(sr, cx, cy, s, 48.0f, 31.0f, 48.0f, 78.0f, 5.0f);
        drawLine(sr, cx, cy, s, 48.0f, 78.0f, 52.0f, 96.0f, 5.0f);
        drawLine(sr, cx, cy, s, 52.0f, 96.0f, 64.0f, 110.0f, 5.0f);
        drawLine(sr, cx, cy, s, 64.0f, 110.0f, 78.0f, 114.0f, 5.0f);
        drawLine(sr, cx, cy, s, 78.0f, 114.0f, 94.0f, 108.0f, 5.0f);
        drawLine(sr, cx, cy, s, 94.0f, 108.0f, 104.0f, 92.0f, 5.0f);
        drawLine(sr, cx, cy, s, 104.0f, 92.0f, 108.0f, 68.0f, 5.0f);

        // Layer 4: Razor Sharp Highlights along Edge
        sr.setColor(0.99f, 0.65f, 0.65f, 1.0f);
        drawLine(sr, cx, cy, s, 50.0f, 33.0f, 50.0f, 76.0f, 1.8f);
        drawLine(sr, cx, cy, s, 50.0f, 76.0f, 64.0f, 106.0f, 1.8f);
        drawLine(sr, cx, cy, s, 64.0f, 106.0f, 78.0f, 110.0f, 1.8f);
        drawLine(sr, cx, cy, s, 78.0f, 110.0f, 100.0f, 92.0f, 1.8f);
        drawLine(sr, cx, cy, s, 100.0f, 92.0f, 106.0f, 70.0f, 1.8f);
        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 50.0f, 33.0f, 50.0f, 76.0f, 0.8f);
        drawLine(sr, cx, cy, s, 50.0f, 76.0f, 78.0f, 110.0f, 0.8f);
        drawLine(sr, cx, cy, s, 78.0f, 110.0f, 106.0f, 70.0f, 0.8f);

        // 4 Segmented Vertebrae Rib Plates along Shank
        sr.setColor(0.96f, 0.62f, 0.04f, 1.0f);
        drawLine(sr, cx, cy, s, 44.0f, 41.0f, 52.0f, 41.0f, 2.5f);
        drawLine(sr, cx, cy, s, 44.0f, 51.0f, 52.0f, 51.0f, 2.5f);
        drawLine(sr, cx, cy, s, 44.0f, 65.0f, 52.0f, 65.0f, 2.5f);
        drawLine(sr, cx, cy, s, 44.0f, 77.0f, 52.0f, 77.0f, 2.5f);

        // Blood Tear Soul Crystal at Shank Center (48, 56)
        sr.setColor(0.96f, 0.62f, 0.04f, 1.0f);
        fillTri(sr, cx, cy, s, 48.0f, 47.0f, 55.0f, 56.0f, 48.0f, 65.0f);
        fillTri(sr, cx, cy, s, 48.0f, 47.0f, 41.0f, 56.0f, 48.0f, 65.0f);
        sr.setColor(0.94f, 0.27f, 0.27f, 1.0f);
        fillTri(sr, cx, cy, s, 48.0f, 48.0f, 43.0f, 56.0f, 48.0f, 56.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.0f);
        fillTri(sr, cx, cy, s, 48.0f, 48.0f, 53.0f, 56.0f, 48.0f, 56.0f);
        sr.setColor(0.60f, 0.11f, 0.11f, 1.0f);
        fillTri(sr, cx, cy, s, 43.0f, 56.0f, 48.0f, 64.0f, 48.0f, 56.0f);
        sr.setColor(0.35f, 0.03f, 0.07f, 1.0f);
        fillTri(sr, cx, cy, s, 53.0f, 56.0f, 48.0f, 64.0f, 48.0f, 56.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.0f);
        fillCircle(sr, cx, cy, s, 48.0f, 56.0f, 2.0f);

        // 4. Demon Monarch Talon Point & Multi-Barb System
        // Primary Vicious Inward Hook Barb (Mũi Vuốt Quỷ Chính)
        sr.setColor(0.09f, 0.02f, 0.02f, 1.0f);
        fillTri(sr, cx, cy, s, 108.0f, 60.0f, 90.0f, 75.0f, 98.0f, 82.0f);
        sr.setColor(0.94f, 0.27f, 0.27f, 1.0f);
        fillTri(sr, cx, cy, s, 108.0f, 60.0f, 92.0f, 74.0f, 97.0f, 79.0f);
        sr.setColor(Color.WHITE);
        fillTri(sr, cx, cy, s, 108.0f, 60.0f, 95.0f, 73.0f, 98.0f, 76.0f);

        // Secondary Outer Armor-Piercer Barb (Ngạnh Ngoài Phá Giáp)
        sr.setColor(0.09f, 0.02f, 0.02f, 1.0f);
        fillTri(sr, cx, cy, s, 110.0f, 70.0f, 119.0f, 79.0f, 109.0f, 83.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.0f);
        fillTri(sr, cx, cy, s, 110.0f, 70.0f, 117.0f, 78.0f, 110.0f, 81.0f);
        sr.setColor(0.99f, 0.65f, 0.65f, 1.0f);
        fillTri(sr, cx, cy, s, 110.0f, 70.0f, 115.0f, 77.0f, 111.0f, 79.0f);

        // Tertiary Gut-Hook Barb in Bend (Ngạnh Trong Vòm Uốn)
        sr.setColor(0.94f, 0.27f, 0.27f, 1.0f);
        fillTri(sr, cx, cy, s, 88.0f, 96.0f, 98.0f, 102.0f, 91.0f, 106.0f);

        // Blood Fuller Groove along Talon
        sr.setColor(0.94f, 0.27f, 0.27f, 1.0f);
        drawLine(sr, cx, cy, s, 104.0f, 65.0f, 92.0f, 94.0f, 1.6f);
        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 104.0f, 65.0f, 92.0f, 94.0f, 0.6f);

        // 5. Bleeding Crimson Droplets Falling From Talon Point
        sr.setColor(0.94f, 0.27f, 0.27f, 1.0f);
        fillCircle(sr, cx, cy, s, 108.0f, 54.0f, 2.2f);
        fillTri(sr, cx, cy, s, 108.0f, 50.0f, 105.0f, 55.0f, 111.0f, 55.0f);
        fillCircle(sr, cx, cy, s, 108.0f, 44.0f, 1.6f);
        fillCircle(sr, cx, cy, s, 108.0f, 35.0f, 1.2f);

        // 6. Specular Glints & Diamond Sparkles
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 14.0f, 20.0f, 1.8f);
        fillCircle(sr, cx, cy, s, 82.0f, 20.0f, 1.8f);
        fillCircle(sr, cx, cy, s, 48.0f, 14.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 108.0f, 60.0f, 2.0f);
        fillCircle(sr, cx, cy, s, 119.0f, 79.0f, 1.4f);
        fillCircle(sr, cx, cy, s, 78.0f, 114.0f, 1.8f);
        fillCircle(sr, cx, cy, s, 48.0f, 49.0f, 1.2f);
    }
}
