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
        // 1. Predatory Talon Aura
        sr.setColor(0.32f, 0.72f, 0.38f, 0.20f);
        sr.circle(cx, cy, r * 1.25f);
        sr.setColor(0.75f, 0.65f, 0.30f, 0.15f);
        sr.circle(cx, cy, r * 1.05f);

        // 2. Heavy Brass Ring Eyelet & Predator Fang Talisman (Khuyên đồng & bùa nanh thú)
        float eyeX = cx - r * 0.22f;
        float eyeY = cy + r * 0.70f;
        sr.setColor(0.20f, 0.24f, 0.20f, 1f);
        sr.circle(eyeX, eyeY, Math.max(4.5f, r * 0.22f));
        sr.setColor(0.88f, 0.74f, 0.28f, 1f);
        sr.circle(eyeX, eyeY, Math.max(3.5f, r * 0.16f));
        sr.setColor(0.08f, 0.11f, 0.14f, 1f);
        sr.circle(eyeX, eyeY, Math.max(1.8f, r * 0.08f));

        // Rawhide wraps binding the eyelet
        sr.setColor(0.58f, 0.38f, 0.18f, 1f);
        sr.rect(eyeX - r * 0.12f, eyeY - r * 0.24f, r * 0.24f, r * 0.14f);
        sr.setColor(0.85f, 0.75f, 0.30f, 1f);
        sr.rectLine(eyeX - r * 0.12f, eyeY - r * 0.17f, eyeX + r * 0.12f, eyeY - r * 0.17f, 1.5f);

        // Hanging carved beast fang talisman (Nanh quái thú biển đeo lủng lẳng)
        sr.setColor(0.92f, 0.90f, 0.80f, 1f);
        sr.triangle(eyeX - r * 0.08f, eyeY - r * 0.24f, eyeX - r * 0.18f, eyeY - r * 0.50f, eyeX, eyeY - r * 0.24f);

        // 3. Thick Prehistoric Beast Talon Shank (Thân vuốt hóa thạch bọc sống lưng thép)
        float shankX = cx - r * 0.20f;
        float bendBottomY = cy - r * 0.42f;

        // Dark reinforced steel backplate
        sr.setColor(0.18f, 0.22f, 0.18f, 1f);
        sr.rectLine(shankX, eyeY - r * 0.20f, shankX, bendBottomY, Math.max(5.5f, r * 0.22f));

        // Ivory fossil talon bone core
        sr.setColor(0.88f, 0.86f, 0.76f, 1f);
        sr.rectLine(shankX, eyeY - r * 0.20f, shankX, bendBottomY, Math.max(3.8f, r * 0.15f));

        // 3 Reverse Serrated Predator Barbs along the spine (3 ngạnh răng cưa lưng vuốt)
        sr.setColor(0.28f, 0.34f, 0.28f, 1f);
        for (int i = 0; i < 3; i++) {
            float by = eyeY - r * 0.32f - i * r * 0.24f;
            sr.triangle(shankX, by, shankX - r * 0.24f, by + r * 0.10f, shankX, by + r * 0.12f);
            sr.setColor(0.85f, 0.88f, 0.82f, 1f);
            sr.triangle(shankX, by + 1f, shankX - r * 0.18f, by + r * 0.08f, shankX, by + r * 0.10f);
            sr.setColor(0.28f, 0.34f, 0.28f, 1f);
        }

        // 4. Sweeping U-Bend & Lethal Recurve (Cung uốn cong đáy vuốt quái thú)
        sr.setColor(0.18f, 0.22f, 0.18f, 1f);
        sr.rectLine(shankX, bendBottomY, cx + r * 0.45f, bendBottomY + r * 0.08f, Math.max(5.5f, r * 0.22f));
        sr.setColor(0.88f, 0.86f, 0.76f, 1f);
        sr.rectLine(shankX, bendBottomY, cx + r * 0.45f, bendBottomY + r * 0.08f, Math.max(3.8f, r * 0.15f));

        // 5. Razor-Honed Talon Point & Inward Retaining Barb (Mũi móng vuốt cong & ngạnh khóa mồi)
        float hookTipX = cx + r * 0.50f;
        float hookTipY = cy + r * 0.38f;

        // Upward talon curve
        sr.setColor(0.18f, 0.22f, 0.18f, 1f);
        sr.triangle(cx + r * 0.35f, bendBottomY + r * 0.04f, cx + r * 0.62f, cy + r * 0.10f, hookTipX, hookTipY);
        // Ivory cutting core
        sr.setColor(0.88f, 0.86f, 0.76f, 1f);
        sr.triangle(cx + r * 0.38f, bendBottomY + r * 0.06f, cx + r * 0.56f, cy + r * 0.08f, hookTipX, hookTipY);

        // Vicious Inward Secondary Barb (Ngạnh móc ngược giữ chặt mồi)
        sr.setColor(0.92f, 0.94f, 0.90f, 1f);
        sr.triangle(hookTipX, hookTipY, cx + r * 0.42f, cy + r * 0.18f, cx + r * 0.26f, cy + r * 0.20f);

        // Emerald venomous etching line
        sr.setColor(0.35f, 1.0f, 0.55f, 0.95f);
        sr.rectLine(cx + r * 0.38f, bendBottomY + r * 0.15f, hookTipX - r * 0.05f, hookTipY - r * 0.05f, 1.5f);

        // Specular glint on talon tip
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(hookTipX, hookTipY, Math.max(1.5f, r * 0.06f));
    }

    // =========================================================================
    // 3. BĂNG GIÁ SET
    // =========================================================================
    // 3. BĂNG GIÁ SET (Glacial Permafrost Scepter, Absolute-Zero Spool, Cryo Hook)
    // =========================================================================
    public static void drawBangGiaRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Glacial Blizzard & Diamond Frost Aura
        sr.setColor(0.35f, 0.85f, 1f, 0.22f);
        sr.circle(cx, cy, r * 0.95f);
        sr.setColor(0.65f, 0.95f, 1f, 0.18f);
        sr.circle(cx + r * 0.35f, cy + r * 0.35f, r * 0.55f);

        // 2. Faceted Permafrost Glacier Crystal Shaft (Diagonal)
        float startX = cx - r * 0.72f, startY = cy - r * 0.72f;
        float endX = cx + r * 0.45f, endY = cy + r * 0.45f;

        // Dark deep sapphire crystal spine
        sr.setColor(0.12f, 0.32f, 0.60f, 1f);
        sr.rectLine(startX, startY, endX, endY, Math.max(5.5f, r * 0.22f));

        // Translucent glacial cyan crystal body
        sr.setColor(0.65f, 0.90f, 1f, 0.95f);
        sr.rectLine(startX + 1f, startY + 1f, endX, endY, Math.max(3.8f, r * 0.16f));

        // Faceted diamond ice specular highlight
        sr.setColor(0.92f, 0.98f, 1f, 1f);
        sr.rectLine(startX + 2f, startY + 2f, endX - r * 0.08f, endY - r * 0.08f, 1.6f);

        // 3. Platinum-Silver Frost Rune Bands with Cold Crystal Studs
        for (int b = 0; b < 3; b++) {
            float frac = 0.22f + b * 0.28f;
            float bx = startX + (endX - startX) * frac;
            float by = startY + (endY - startY) * frac;
            sr.setColor(0.85f, 0.92f, 1f, 1f);
            sr.rectLine(bx - 3.2f, by + 3.2f, bx + 3.2f, by - 3.2f, Math.max(3.2f, r * 0.12f));
            sr.setColor(0.25f, 0.75f, 1f, 1f);
            sr.circle(bx, by, 1.8f);
        }

        // 4. Lateral Jagged Icicle Spurs growing along shaft
        sr.setColor(0.55f, 0.88f, 1f, 0.95f);
        sr.triangle(cx - r * 0.15f, cy - r * 0.15f, cx - r * 0.35f, cy + r * 0.05f, cx - r * 0.05f, cy - r * 0.05f);
        sr.triangle(cx + r * 0.15f, cy + r * 0.15f, cx + r * 0.02f, cy + r * 0.38f, cx + r * 0.25f, cy + r * 0.25f);
        // Frost spur razor tips
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.rectLine(cx - r * 0.15f, cy - r * 0.15f, cx - r * 0.35f, cy + r * 0.05f, 1.4f);
        sr.rectLine(cx + r * 0.15f, cy + r * 0.15f, cx + r * 0.02f, cy + r * 0.38f, 1.4f);

        // 5. Crown Headpiece: 6-Pointed Arctic Snowflake Star (Hoa Tuyết Cực Bắc)
        float hx = endX + r * 0.12f, hy = endY + r * 0.12f;
        // Outer snowflake aura
        sr.setColor(0.50f, 0.92f, 1f, 0.35f);
        sr.circle(hx, hy, r * 0.30f);

        // 6 Symmetric crystalline rays
        sr.setColor(0.85f, 0.96f, 1f, 1f);
        for (int a = 0; a < 6; a++) {
            float ang = a * 60f;
            float cos = MathUtils.cosDeg(ang);
            float sin = MathUtils.sinDeg(ang);
            float rx = hx + cos * r * 0.32f;
            float ry = hy + sin * r * 0.32f;
            // Ray spine
            sr.rectLine(hx, hy, rx, ry, 2f);
            // Ray branching barbs
            float bx1 = hx + cos * r * 0.20f;
            float by1 = hy + sin * r * 0.20f;
            float bCos = MathUtils.cosDeg(ang + 45f) * r * 0.08f;
            float bSin = MathUtils.sinDeg(ang + 45f) * r * 0.08f;
            sr.rectLine(bx1, by1, bx1 + bCos, by1 + bSin, 1.2f);
            float bCos2 = MathUtils.cosDeg(ang - 45f) * r * 0.08f;
            float bSin2 = MathUtils.sinDeg(ang - 45f) * r * 0.08f;
            sr.rectLine(bx1, by1, bx1 + bCos2, by1 + bSin2, 1.2f);
        }

        // Brilliant pure-white cryogenic diamond core
        sr.setColor(0.30f, 0.75f, 1f, 1f);
        sr.circle(hx, hy, r * 0.12f);
        sr.setColor(Color.WHITE);
        sr.circle(hx, hy, r * 0.07f);
        sr.circle(hx - 1f, hy + 1f, 1.8f);

        // Dangling faceted frost prism pendant
        sr.setColor(0.70f, 0.90f, 1f, 0.85f);
        sr.rectLine(hx, hy - r * 0.12f, hx - r * 0.15f, hy - r * 0.38f, 1.5f);
        sr.setColor(0.85f, 0.98f, 1f, 1f);
        sr.triangle(hx - r * 0.15f, hy - r * 0.38f, hx - r * 0.22f, hy - r * 0.50f, hx - r * 0.08f, hy - r * 0.50f);
        sr.triangle(hx - r * 0.22f, hy - r * 0.50f, hx - r * 0.08f, hy - r * 0.50f, hx - r * 0.15f, hy - r * 0.60f);
    }

    public static void drawBangGiaLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Sub-Zero Freezing Mist Aura
        sr.setColor(0.35f, 0.85f, 1f, 0.22f);
        sr.circle(cx, cy, r * 0.95f);
        sr.setColor(0.12f, 0.40f, 0.75f, 0.35f);
        sr.circle(cx, cy, r * 0.80f);

        // 2. Heavy Carved Permafrost Glacier Wheel Rim
        sr.setColor(0.40f, 0.78f, 0.95f, 1f);
        sr.circle(cx, cy, r * 0.76f);

        // 6 Diamond Ice Crystal Spurs around circumference
        for (int i = 0; i < 6; i++) {
            float ang = i * 60f;
            float cos = MathUtils.cosDeg(ang);
            float sin = MathUtils.sinDeg(ang);
            float px = cx + cos * (r * 0.76f + 2f);
            float py = cy + sin * (r * 0.76f + 2f);
            sr.setColor(0.85f, 0.98f, 1f, 1f);
            sr.circle(px, py, Math.max(2.2f, r * 0.09f));
            sr.setColor(0.20f, 0.65f, 0.95f, 1f);
            sr.circle(px, py, Math.max(1.2f, r * 0.05f));
        }

        // Inner deep sapphire ice trench
        sr.setColor(0.08f, 0.22f, 0.45f, 1f);
        sr.circle(cx, cy, r * 0.65f);

        // 3. Absolute-Zero Superconducting Frost Filament (Cuộn cước băng lam)
        sr.setColor(0.70f, 0.92f, 1f, 0.95f);
        sr.circle(cx, cy, r * 0.54f);
        sr.setColor(0.12f, 0.35f, 0.65f, 1f);
        sr.circle(cx, cy, r * 0.44f);
        sr.setColor(0.88f, 0.98f, 1f, 0.95f);
        sr.circle(cx, cy, r * 0.34f);

        // 4 Freezing optical pulse nodes on thread
        sr.setColor(Color.WHITE);
        sr.circle(cx - r * 0.38f, cy, 2.2f);
        sr.circle(cx + r * 0.38f, cy, 2.2f);
        sr.circle(cx, cy - r * 0.38f, 2.2f);
        sr.circle(cx, cy + r * 0.38f, 2.2f);

        // 4. Center Hub: Ornate Hexagonal True-Ice Snowflake Medallion
        sr.setColor(0.18f, 0.50f, 0.85f, 1f);
        sr.circle(cx, cy, r * 0.24f);

        // Snowflake core cross
        sr.setColor(1f, 1f, 1f, 1f);
        sr.rectLine(cx - r * 0.18f, cy, cx + r * 0.18f, cy, 1.8f);
        sr.rectLine(cx, cy - r * 0.18f, cx, cy + r * 0.18f, 1.8f);
        sr.rectLine(cx - r * 0.12f, cy - r * 0.12f, cx + r * 0.12f, cy + r * 0.12f, 1.4f);
        sr.rectLine(cx - r * 0.12f, cy + r * 0.12f, cx + r * 0.12f, cy - r * 0.12f, 1.4f);

        // Sparkling center crystal glint
        sr.setColor(0.95f, 1f, 1f, 1f);
        sr.circle(cx, cy, r * 0.07f);
        sr.circle(cx - 1f, cy + 1f, 1.5f);
    }

    public static void drawBangGiaHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Cryogenic Frostbite Reaping Aura
        sr.setColor(0.38f, 0.88f, 1f, 0.25f);
        sr.circle(cx, cy, r * 0.95f);
        sr.setColor(0.65f, 0.95f, 1f, 0.18f);
        sr.circle(cx, cy, r * 0.78f);

        // 2. Heavy Sculpted Permafrost Glacier Shank
        float shankX = cx - r * 0.22f;
        float topY = cy + r * 0.62f;
        float bendY = cy - r * 0.45f;
        float barbX = cx + r * 0.48f;
        float barbY = cy + r * 0.28f;

        // Dark glacial cobalt reinforcement
        sr.setColor(0.10f, 0.28f, 0.55f, 1f);
        sr.rectLine(shankX, topY, shankX, bendY, Math.max(5.5f, r * 0.22f));
        sr.rectLine(shankX, bendY, barbX, bendY, Math.max(5.5f, r * 0.22f));

        // Faceted cyan ice crystal shank body
        sr.setColor(0.65f, 0.92f, 1f, 1f);
        sr.rectLine(shankX, topY, shankX, bendY, Math.max(3.8f, r * 0.15f));
        sr.rectLine(shankX, bendY, barbX, bendY, Math.max(3.8f, r * 0.15f));
        sr.rectLine(barbX, bendY, barbX, barbY, Math.max(3.8f, r * 0.15f));

        // Frost rime highlight along edge
        sr.setColor(0.92f, 0.98f, 1f, 1f);
        sr.rectLine(shankX + 1.2f, topY, shankX + 1.2f, bendY, 1.4f);
        sr.rectLine(shankX + 1.2f, bendY + 1.2f, barbX, bendY + 1.2f, 1.4f);

        // 3. Dual Razor-Sharp Glacier Icicle Barbs
        // Main sharp tip pointing inward/upward
        sr.setColor(0.85f, 0.96f, 1f, 1f);
        sr.triangle(barbX - r * 0.08f, barbY - r * 0.1f, barbX + r * 0.08f, barbY - r * 0.1f, barbX - r * 0.14f, barbY + r * 0.25f);
        // Razor white glint on tip
        sr.setColor(Color.WHITE);
        sr.rectLine(barbX, barbY - r * 0.1f, barbX - r * 0.14f, barbY + r * 0.25f, 1.6f);

        // Secondary jagged frost barb on inner curve
        sr.setColor(0.50f, 0.85f, 1f, 1f);
        sr.triangle(barbX - r * 0.04f, barbY - r * 0.24f, barbX + r * 0.06f, barbY - r * 0.24f, barbX - r * 0.20f, barbY - r * 0.06f);

        // 4. Glowing Sub-Zero Freezing Runes along Shank
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(shankX, cy + r * 0.25f, r * 0.07f);
        sr.circle(shankX, cy, r * 0.07f);
        sr.circle(shankX, cy - r * 0.25f, r * 0.07f);
        sr.setColor(0.35f, 0.90f, 1f, 1f);
        sr.circle(shankX, cy + r * 0.25f, r * 0.04f);
        sr.circle(shankX, cy, r * 0.04f);
        sr.circle(shankX, cy - r * 0.25f, r * 0.04f);

        // 5. Crown Eyelet: 6-Spoke Snowflake Crystal Crown
        sr.setColor(0.55f, 0.88f, 1f, 1f);
        sr.circle(shankX, topY, r * 0.24f);
        sr.setColor(0.08f, 0.18f, 0.35f, 1f);
        sr.circle(shankX, topY, r * 0.12f);
        // Crystal snowflake points around crown
        sr.setColor(1f, 1f, 1f, 0.95f);
        for (int a = 0; a < 6; a++) {
            float ang = a * 60f;
            float cos = MathUtils.cosDeg(ang);
            float sin = MathUtils.sinDeg(ang);
            sr.circle(shankX + cos * r * 0.24f, topY + sin * r * 0.24f, 1.8f);
        }

        // Dangling cryo pearl charm
        sr.setColor(0.65f, 0.90f, 1f, 0.85f);
        sr.rectLine(shankX, topY, shankX - r * 0.28f, topY - r * 0.22f, 1.5f);
        sr.setColor(0.85f, 0.98f, 1f, 1f);
        sr.circle(shankX - r * 0.28f, topY - r * 0.22f, 4.5f);
        sr.setColor(Color.WHITE);
        sr.circle(shankX - r * 0.28f - 1f, topY - r * 0.22f + 1f, 1.8f);
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
        // 1. Deep-Sea Leviathan Jaw Aura
        sr.setColor(0.12f, 0.65f, 0.90f, 0.20f);
        sr.circle(cx, cy, r * 0.95f);

        // 2. Heavy Ancient Leviathan Bone Fang Hook Body
        float shankX = cx - r * 0.25f;
        float topY = cy + r * 0.60f;
        float bendY = cy - r * 0.45f;
        float barbX = cx + r * 0.48f;
        float barbY = cy + r * 0.25f;

        // Abyssal Cobalt Spine Reinforcement
        sr.setColor(0.08f, 0.22f, 0.45f, 1f);
        sr.rectLine(shankX - 2f, topY, shankX - 2f, bendY, Math.max(4.5f, r * 0.20f));
        sr.rectLine(shankX, bendY, barbX, bendY, Math.max(4.5f, r * 0.20f));

        // Ancient Leviathan Ivory Tooth Shank
        sr.setColor(0.85f, 0.92f, 0.95f, 1f);
        sr.rectLine(shankX, topY, shankX, bendY, Math.max(3.2f, r * 0.14f));
        sr.rectLine(shankX, bendY, barbX, bendY, Math.max(3.2f, r * 0.14f));
        sr.rectLine(barbX, bendY, barbX, barbY, Math.max(3.5f, r * 0.16f));

        // Luminous Oceanic Wave Runes along Shank
        sr.setColor(0.35f, 0.95f, 1f, 1f);
        sr.circle(shankX, cy + r * 0.25f, r * 0.08f);
        sr.circle(shankX, cy, r * 0.08f);
        sr.circle(shankX, cy - r * 0.25f, r * 0.08f);

        // 3. Dual Razor-Sharp Sea Serpent Tooth Barbs
        // Main Barb Tip
        sr.setColor(0.30f, 0.85f, 1f, 1f);
        sr.triangle(barbX - r * 0.08f, barbY - r * 0.1f, barbX + r * 0.08f, barbY - r * 0.1f, barbX - r * 0.15f, barbY + r * 0.25f);
        // Razor-sharp cyan glint edge
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.rectLine(barbX, barbY - r * 0.1f, barbX - r * 0.15f, barbY + r * 0.25f, 1.5f);
        // Secondary Inner Wave Barb
        sr.setColor(0.20f, 0.75f, 0.95f, 1f);
        sr.triangle(barbX - r * 0.04f, barbY - r * 0.22f, barbX + r * 0.06f, barbY - r * 0.22f, barbX - r * 0.20f, barbY - r * 0.05f);

        // 4. Crown Eyelet: Golden Anchor-Knot Medallion
        sr.setColor(0.85f, 0.70f, 0.25f, 1f);
        sr.circle(shankX, topY, r * 0.26f);
        sr.setColor(0.08f, 0.14f, 0.25f, 1f);
        sr.circle(shankX, topY, r * 0.14f);
        // Golden Anchor Ring Crown
        sr.setColor(1f, 0.88f, 0.35f, 1f);
        sr.circle(shankX, topY + r * 0.05f, r * 0.12f);

        // Dangling Luminescent Angler-Fish Lure Pearl Charm
        sr.setColor(0.25f, 0.85f, 1f, 0.85f);
        sr.rectLine(shankX, topY, shankX - r * 0.30f, topY - r * 0.22f, 1.8f);
        sr.setColor(0.40f, 0.95f, 1f, 0.35f);
        sr.circle(shankX - r * 0.30f, topY - r * 0.22f, 7f);
        sr.setColor(0.95f, 1f, 1f, 1f);
        sr.circle(shankX - r * 0.30f, topY - r * 0.22f, 3.5f);
    }

    // =========================================================================
    // 5. HẢI TẶC SET (Pirate Galleon Mast, Anchor Chain Wheel, Reaper Scythe Hook)
    // =========================================================================
    public static void drawHaiTacRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Cursed Treasure & Gunpowder Smoke Aura
        sr.setColor(0.85f, 0.40f, 0.12f, 0.20f);
        sr.circle(cx, cy, r * 0.95f);
        sr.setColor(0.95f, 0.20f, 0.15f, 0.15f);
        sr.circle(cx, cy, r * 0.75f);

        // 2. Weathered Black-Iron Galleon Mast Timber (Diagonal Spar)
        float startX = cx - r * 0.72f, startY = cy - r * 0.72f;
        float endX = cx + r * 0.65f, endY = cy + r * 0.65f;

        // Dark aged ebony core
        sr.setColor(0.18f, 0.12f, 0.08f, 1f);
        sr.rectLine(startX, startY, endX, endY, Math.max(6.5f, r * 0.26f));

        // Weathered dark mahogany wood body
        sr.setColor(0.38f, 0.22f, 0.14f, 1f);
        sr.rectLine(startX + 1f, startY + 1f, endX, endY, Math.max(4.2f, r * 0.18f));

        // Wood grain highlight streak
        sr.setColor(0.55f, 0.35f, 0.20f, 0.85f);
        sr.rectLine(startX + 2f, startY + 2f, endX - r * 0.1f, endY - r * 0.1f, 1.8f);

        // 3. Forged Brass & Wrought-Iron Mast Fastener Bands with Rivets
        for (int b = 0; b < 4; b++) {
            float frac = 0.20f + b * 0.22f;
            float bx = startX + (endX - startX) * frac;
            float by = startY + (endY - startY) * frac;
            // Brass ring band
            sr.setColor(0.85f, 0.68f, 0.22f, 1f);
            sr.rectLine(bx - 3.5f, by + 3.5f, bx + 3.5f, by - 3.5f, Math.max(3.5f, r * 0.12f));
            // Dark iron inner groove
            sr.setColor(0.12f, 0.10f, 0.08f, 1f);
            sr.rectLine(bx - 2f, by + 2f, bx + 2f, by - 2f, 1.2f);
            // Golden rivet dots
            sr.setColor(1f, 0.88f, 0.35f, 1f);
            sr.circle(bx - 2f, by + 2f, 1.2f);
            sr.circle(bx + 2f, by - 2f, 1.2f);
        }

        // 4. Heavy Brass Galleon Rigging Block / Pulley
        float px = cx + r * 0.20f, py = cy + r * 0.20f;
        // Wooden/brass cheek housing
        sr.setColor(0.24f, 0.15f, 0.09f, 1f);
        sr.circle(px, py, r * 0.28f);
        sr.setColor(0.85f, 0.65f, 0.20f, 1f);
        sr.circle(px, py, r * 0.22f);
        // Dark sheave groove & rope spool
        sr.setColor(0.15f, 0.10f, 0.08f, 1f);
        sr.circle(px, py, r * 0.14f);
        // Golden axle hub bolt
        sr.setColor(1f, 0.85f, 0.3f, 1f);
        sr.circle(px, py, r * 0.06f);

        // Coiled Rigging Hemp Rope
        sr.setColor(0.70f, 0.55f, 0.35f, 1f);
        sr.rectLine(px - r * 0.18f, py - r * 0.10f, cx - r * 0.15f, cy - r * 0.40f, 2.2f);
        sr.rectLine(px + r * 0.12f, py - r * 0.12f, cx + r * 0.05f, cy - r * 0.35f, 1.8f);

        // 5. Yardarm Cross-Spar & Pirate Storm Lantern
        float yx = cx - r * 0.35f, yy = cy - r * 0.35f;
        sr.setColor(0.32f, 0.18f, 0.10f, 1f);
        sr.rectLine(yx - r * 0.20f, yy + r * 0.20f, yx + r * 0.20f, yy - r * 0.20f, 3.5f);
        // Hanging brass lantern
        sr.setColor(0.85f, 0.65f, 0.20f, 1f);
        sr.rect(yx - r * 0.22f, yy + r * 0.12f, r * 0.14f, r * 0.18f);
        // Glowing cursed flame inside
        sr.setColor(1f, 0.85f, 0.25f, 1f);
        sr.circle(yx - r * 0.15f, yy + r * 0.21f, r * 0.06f);

        // 6. Flying Tattered Black Jolly Roger Pennant Flag
        float fx = endX - r * 0.05f, fy = endY - r * 0.05f;
        // Masthead cap
        sr.setColor(0.85f, 0.70f, 0.25f, 1f);
        sr.circle(endX, endY, r * 0.12f);

        // Black ragged swallowtail banner fluttering rightward
        sr.setColor(0.10f, 0.08f, 0.10f, 0.98f);
        sr.triangle(fx, fy + r * 0.15f, fx + r * 0.45f, fy + r * 0.10f, fx, fy - r * 0.25f);
        sr.triangle(fx + r * 0.45f, fy + r * 0.10f, fx + r * 0.28f, fy - r * 0.08f, fx, fy - r * 0.25f);

        // Bleached ivory skull on flag
        sr.setColor(0.92f, 0.90f, 0.85f, 1f);
        float skX = fx + r * 0.15f, skY = fy - r * 0.02f;
        sr.circle(skX, skY + 2f, r * 0.08f);
        sr.rect(skX - r * 0.04f, skY - r * 0.05f, r * 0.08f, r * 0.05f);
        // Glowing ruby red skull eye dots
        sr.setColor(0.95f, 0.15f, 0.15f, 1f);
        sr.circle(skX - 1.5f, skY + 2f, 1.2f);
        sr.circle(skX + 1.5f, skY + 2f, 1.2f);
        // Crossed pirate cutlass bones behind skull
        sr.setColor(0.85f, 0.82f, 0.78f, 1f);
        sr.rectLine(skX - r * 0.08f, skY - r * 0.06f, skX + r * 0.08f, skY + r * 0.08f, 1.2f);
        sr.rectLine(skX + r * 0.08f, skY - r * 0.06f, skX - r * 0.08f, skY + r * 0.08f, 1.2f);
    }

    public static void drawHaiTacLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Cursed Galleon Whirlpool & Spanish Gold Aura
        sr.setColor(0.85f, 0.55f, 0.15f, 0.20f);
        sr.circle(cx, cy, r * 0.95f);
        sr.setColor(0.18f, 0.12f, 0.08f, 0.40f);
        sr.circle(cx, cy, r * 0.85f);

        // 2. Ornate Nautical Galleon Ship's Wheel (Mahogany & Brass)
        // Outer wheel rim
        sr.setColor(0.28f, 0.16f, 0.10f, 1f);
        sr.circle(cx, cy, r * 0.76f);
        // Brass tire inlay
        sr.setColor(0.85f, 0.68f, 0.22f, 1f);
        sr.circle(cx, cy, r * 0.70f);
        // Inner wheel opening
        sr.setColor(0.08f, 0.07f, 0.09f, 0.95f);
        sr.circle(cx, cy, r * 0.62f);

        // 8 Turned Wheel Spokes with Handles extending beyond rim
        for (int a = 0; a < 8; a++) {
            float ang = a * 45f;
            float cos = MathUtils.cosDeg(ang);
            float sin = MathUtils.sinDeg(ang);
            // Spoke spindle
            sr.setColor(0.45f, 0.28f, 0.16f, 1f);
            sr.rectLine(cx, cy, cx + cos * r * 0.72f, cy + sin * r * 0.72f, 2.5f);
            // Turned handle grip extending outwards
            sr.setColor(0.85f, 0.68f, 0.22f, 1f);
            sr.circle(cx + cos * r * 0.84f, cy + sin * r * 0.84f, Math.max(2.2f, r * 0.08f));
            sr.setColor(0.32f, 0.18f, 0.10f, 1f);
            sr.circle(cx + cos * r * 0.84f, cy + sin * r * 0.84f, Math.max(1.2f, r * 0.04f));
        }

        // 3. Heavy Forged-Iron Admiralty Anchor crossed in center
        float ancX = cx, ancY = cy;
        // Anchor shank (vertical)
        sr.setColor(0.22f, 0.22f, 0.26f, 1f);
        sr.rectLine(ancX, ancY - r * 0.45f, ancX, ancY + r * 0.45f, Math.max(3.5f, r * 0.14f));
        // Anchor stock (horizontal wooden crossbar)
        sr.setColor(0.40f, 0.25f, 0.14f, 1f);
        sr.rectLine(ancX - r * 0.32f, ancY + r * 0.28f, ancX + r * 0.32f, ancY + r * 0.28f, Math.max(3f, r * 0.12f));
        // Brass stock tips
        sr.setColor(0.85f, 0.68f, 0.22f, 1f);
        sr.circle(ancX - r * 0.32f, ancY + r * 0.28f, 2.2f);
        sr.circle(ancX + r * 0.32f, ancY + r * 0.28f, 2.2f);

        // Anchor curved arms / flukes
        sr.setColor(0.22f, 0.22f, 0.26f, 1f);
        sr.rectLine(ancX - r * 0.30f, ancY - r * 0.30f, ancX, ancY - r * 0.48f, Math.max(3.2f, r * 0.13f));
        sr.rectLine(ancX + r * 0.30f, ancY - r * 0.30f, ancX, ancY - r * 0.48f, Math.max(3.2f, r * 0.13f));
        // Spade flukes
        sr.setColor(0.55f, 0.58f, 0.65f, 1f);
        sr.triangle(ancX - r * 0.30f, ancY - r * 0.30f, ancX - r * 0.38f, ancY - r * 0.22f, ancX - r * 0.22f, ancY - r * 0.22f);
        sr.triangle(ancX + r * 0.30f, ancY - r * 0.30f, ancX + r * 0.38f, ancY - r * 0.22f, ancX + r * 0.22f, ancY - r * 0.22f);

        // 4. Coiled Heavy Wrought-Iron Chain Links wrapping the wheel
        for (int c = 0; c < 10; c++) {
            float cAng = c * 36f;
            float cosC = MathUtils.cosDeg(cAng);
            float sinC = MathUtils.sinDeg(cAng);
            float lx = cx + cosC * (r * 0.50f);
            float ly = cy + sinC * (r * 0.50f);

            // Outer forged iron link ring
            sr.setColor(0.18f, 0.18f, 0.22f, 1f);
            sr.circle(lx, ly, Math.max(3.8f, r * 0.13f));
            // Metallic silver highlight
            sr.setColor(0.68f, 0.72f, 0.78f, 1f);
            sr.circle(lx - 0.8f, ly + 0.8f, Math.max(2.4f, r * 0.08f));
            // Inner hollow link
            sr.setColor(0.08f, 0.08f, 0.10f, 1f);
            sr.circle(lx, ly, Math.max(1.4f, r * 0.05f));
        }

        // 5. Golden Pirate Doubloon Wheel Hub Medallion
        sr.setColor(0.90f, 0.72f, 0.20f, 1f);
        sr.circle(cx, cy, r * 0.24f);
        sr.setColor(0.65f, 0.48f, 0.12f, 1f);
        sr.circle(cx, cy, r * 0.20f);
        // Center ivory skull boss
        sr.setColor(0.95f, 0.92f, 0.85f, 1f);
        sr.circle(cx, cy + 1f, r * 0.10f);
        sr.rect(cx - r * 0.05f, cy - r * 0.07f, r * 0.10f, r * 0.07f);
        // Glowing emerald eye sockets (Cursed Aztec/Pirate Treasure)
        sr.setColor(0.15f, 0.95f, 0.35f, 1f);
        sr.circle(cx - 1.8f, cy + 1f, 1.2f);
        sr.circle(cx + 1.8f, cy + 1f, 1.2f);
    }

    public static void drawHaiTacHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Blood-Tide Maritime Curse Aura
        sr.setColor(0.90f, 0.22f, 0.15f, 0.22f);
        sr.circle(cx, cy, r * 0.95f);
        sr.setColor(0.65f, 0.10f, 0.10f, 0.18f);
        sr.circle(cx, cy, r * 0.78f);

        // 2. Heavy Forged Damascus Steel Shank & Spine
        float shankX = cx - r * 0.22f;
        float topY = cy + r * 0.62f;
        float bendY = cy - r * 0.48f;
        float bladeEndX = cx + r * 0.55f;
        float bladeEndY = cy + r * 0.40f;

        // Dark forged iron spine reinforcement
        sr.setColor(0.12f, 0.12f, 0.15f, 1f);
        sr.rectLine(shankX, topY, shankX, bendY, Math.max(5.5f, r * 0.22f));
        sr.rectLine(shankX, bendY, cx + r * 0.30f, bendY, Math.max(5.5f, r * 0.22f));

        // Damascus slate steel shank body
        sr.setColor(0.35f, 0.38f, 0.44f, 1f);
        sr.rectLine(shankX, topY, shankX, bendY, Math.max(3.8f, r * 0.15f));
        sr.rectLine(shankX, bendY, cx + r * 0.30f, bendY, Math.max(3.8f, r * 0.15f));

        // 3. Brutal Sweeping Reaper Scythe Blade (Curved Upward)
        // Main broad scythe blade
        sr.setColor(0.48f, 0.52f, 0.60f, 1f);
        sr.triangle(cx + r * 0.10f, bendY - 2f, bladeEndX, bladeEndY, cx + r * 0.18f, cy - r * 0.05f);
        sr.triangle(cx + r * 0.28f, bendY - 2f, bladeEndX, bladeEndY, cx + r * 0.38f, cy + r * 0.10f);

        // Reverse wicked barb on inner scythe curve
        sr.setColor(0.70f, 0.74f, 0.82f, 1f);
        sr.triangle(bladeEndX, bladeEndY, bladeEndX - r * 0.22f, bladeEndY - r * 0.28f, bladeEndX - r * 0.06f, bladeEndY - r * 0.32f);

        // Razor-sharp mirror polished blade edge
        sr.setColor(0.96f, 0.98f, 1f, 1f);
        sr.rectLine(bladeEndX, bladeEndY, cx + r * 0.18f, cy - r * 0.05f, 2f);
        sr.rectLine(cx + r * 0.18f, cy - r * 0.05f, shankX + 2f, bendY + 2f, 2f);

        // 4. Etched Cursed Bloodline Fuller with Glowing Crimson Runes
        sr.setColor(0.95f, 0.15f, 0.15f, 0.95f);
        sr.rectLine(shankX, topY - r * 0.15f, shankX, bendY + r * 0.10f, 1.8f);
        sr.rectLine(shankX + 2f, bendY, cx + r * 0.25f, bendY, 1.8f);
        // Bloodline curve up the scythe spine
        sr.rectLine(cx + r * 0.25f, bendY, bladeEndX - r * 0.08f, bladeEndY - r * 0.08f, 1.6f);

        // Glowing blood drop / ruby node on fuller
        sr.setColor(1f, 0.35f, 0.35f, 1f);
        sr.circle(shankX, cy, 2.2f);
        sr.circle(cx + r * 0.15f, bendY, 2.2f);
        sr.circle(bladeEndX - r * 0.12f, bladeEndY - r * 0.12f, 2.2f);

        // 5. Pirate Cutlass Brass Basket Handguard & Eyelet Shackle
        // Eyelet ring at top
        sr.setColor(0.85f, 0.68f, 0.22f, 1f);
        sr.circle(shankX, topY, r * 0.24f);
        sr.setColor(0.08f, 0.08f, 0.10f, 1f);
        sr.circle(shankX, topY, r * 0.12f);

        // Brass basket guard curving outwards to protect eyelet
        sr.setColor(0.78f, 0.60f, 0.18f, 1f);
        sr.rectLine(shankX, topY - r * 0.08f, shankX - r * 0.30f, topY - r * 0.28f, 3.2f);
        sr.rectLine(shankX - r * 0.30f, topY - r * 0.28f, shankX - r * 0.12f, cy + r * 0.10f, 2.8f);
        // Brass guard knuckle stud
        sr.setColor(1f, 0.88f, 0.35f, 1f);
        sr.circle(shankX - r * 0.30f, topY - r * 0.28f, 2.8f);

        // 6. Dangling Spanish 8-Reales Silver Coin & Pirate Feather Charm
        float chX = shankX - r * 0.12f, chY = cy + r * 0.10f;
        sr.setColor(0.75f, 0.78f, 0.82f, 1f);
        sr.rectLine(chX, chY, chX - r * 0.10f, chY - r * 0.25f, 1.5f);
        // Pierced Spanish silver coin
        sr.setColor(0.82f, 0.85f, 0.90f, 1f);
        sr.circle(chX - r * 0.10f, chY - r * 0.25f, r * 0.13f);
        sr.setColor(0.12f, 0.12f, 0.15f, 1f);
        sr.circle(chX - r * 0.10f, chY - r * 0.25f, 1.5f);
        // Gold edge trim on coin
        sr.setColor(0.95f, 0.80f, 0.25f, 1f);
        sr.circle(chX - r * 0.10f, chY - r * 0.25f, 2.2f);
    }

    // =========================================================================
    // 6. VỰC THẲM SET (ABYSSAL VOID: Obsidian Nether Staff, Void Vein Spool, Fiend Dragon Claw Hook)
    // =========================================================================
    public static void drawVucThamRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Abyssal Void Miasma & Eldritch Aura
        sr.setColor(0.40f, 0.08f, 0.60f, 0.22f);
        sr.circle(cx, cy, r * 0.95f);
        sr.setColor(0.80f, 0.20f, 0.95f, 0.25f);
        sr.circle(cx + r * 0.45f, cy + r * 0.45f, r * 0.55f);

        // 2. Obsidian Nether-Titanium Spine
        float x1 = cx - r * 0.72f, y1 = cy - r * 0.72f;
        float x2 = cx + r * 0.38f, y2 = cy + r * 0.38f;

        // Dark obsidian core
        sr.setColor(0.10f, 0.06f, 0.16f, 1f);
        sr.rectLine(x1, y1, x2, y2, Math.max(5f, r * 0.22f));

        // Deep shadow-purple chassis
        sr.setColor(0.24f, 0.10f, 0.36f, 1f);
        sr.rectLine(x1, y1, x2, y2, Math.max(3.4f, r * 0.14f));

        // Luminous void plasma core conduit
        sr.setColor(0.90f, 0.25f, 0.98f, 0.95f);
        sr.rectLine(x1, y1, x2, y2, Math.max(1.8f, r * 0.08f));

        // 3. Spiky Demonic Bone Horn Spire Bands along the shaft
        for (int b = 0; b < 3; b++) {
            float frac = 0.25f + b * 0.26f;
            float bx = x1 + (x2 - x1) * frac;
            float by = y1 + (y2 - y1) * frac;

            // Lateral spires jutting outward
            sr.setColor(0.35f, 0.12f, 0.48f, 1f);
            sr.triangle(bx, by, bx - r * 0.14f, by + r * 0.12f, bx - r * 0.04f, by + r * 0.18f);
            sr.triangle(bx, by, bx + r * 0.12f, by - r * 0.14f, bx + r * 0.18f, by - r * 0.04f);

            // Spire tips glowing void magenta
            sr.setColor(0.95f, 0.30f, 1f, 1f);
            sr.circle(bx - r * 0.12f, by + r * 0.15f, 1.8f);
            sr.circle(bx + r * 0.15f, by - r * 0.12f, 1.8f);

            // Bone ring band
            sr.setColor(0.65f, 0.22f, 0.85f, 1f);
            sr.circle(bx, by, Math.max(2.8f, r * 0.11f));
            sr.setColor(0.12f, 0.06f, 0.18f, 1f);
            sr.circle(bx, by, 1.5f);
        }

        // 4. Eldritch Void Scythe & All-Seeing Nether Eye Headpiece
        float tx = cx + r * 0.46f, ty = cy + r * 0.46f;

        // Curved Void Crescent Blade jutting northwest
        sr.setColor(0.16f, 0.08f, 0.24f, 1f);
        float scyX = tx - r * 0.42f, scyY = ty + r * 0.35f;
        sr.triangle(tx, ty, scyX, scyY, tx + r * 0.05f, ty + r * 0.32f);
        sr.triangle(tx, ty, scyX, scyY, tx - r * 0.30f, ty + r * 0.15f);

        // Razor-sharp void blade edge
        sr.setColor(0.92f, 0.25f, 0.90f, 1f);
        sr.triangle(tx, ty + r * 0.18f, scyX, scyY, scyX + r * 0.08f, scyY - r * 0.06f);

        // Spine blade spikes
        sr.setColor(0.60f, 0.15f, 0.80f, 1f);
        sr.triangle(tx + r * 0.15f, ty + r * 0.10f, tx + r * 0.38f, ty + r * 0.32f, tx + r * 0.08f, ty + r * 0.26f);

        // 5. Central All-Seeing Nether Eye (Multi-layered Demonic Eye)
        sr.setColor(0.20f, 0.08f, 0.30f, 1f);
        sr.circle(tx, ty, r * 0.28f);
        sr.setColor(0.85f, 0.15f, 0.40f, 1f); // Crimson outer sclera
        sr.circle(tx, ty, r * 0.20f);
        sr.setColor(0.95f, 0.40f, 1f, 1f); // Vivid magenta iris
        sr.circle(tx, ty, r * 0.14f);

        // Slit-pupil of the Abyss Fiend
        sr.setColor(0.06f, 0.02f, 0.10f, 1f);
        sr.triangle(tx, ty + r * 0.12f, tx, ty - r * 0.12f, tx - 2f, ty);
        sr.triangle(tx, ty + r * 0.12f, tx, ty - r * 0.12f, tx + 2f, ty);

        // Specular glint
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(tx - 1.5f, ty + 2.5f, 2f);

        // 6. Void Pommel Crystal
        sr.setColor(0.85f, 0.25f, 0.95f, 1f);
        sr.circle(x1, y1, r * 0.14f);
        sr.setColor(Color.WHITE);
        sr.circle(x1 - 1f, y1 + 1f, 1.8f);
    }

    public static void drawVucThamLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Spatio-Temporal Void Vortex Aura
        sr.setColor(0.35f, 0.08f, 0.50f, 0.22f);
        sr.circle(cx, cy, r * 0.96f);
        sr.setColor(0.75f, 0.18f, 0.90f, 0.20f);
        sr.circle(cx, cy, r * 0.80f);

        // 2. Heavy Abyssal Carapace Spool Frame with 8 Demonic Horns
        sr.setColor(0.18f, 0.08f, 0.26f, 1f);
        sr.circle(cx, cy, r * 0.76f);

        // 8 jagged demonic horn prongs around circumference
        for (int h = 0; h < 8; h++) {
            float ang = h * 45f;
            float cos = MathUtils.cosDeg(ang);
            float sin = MathUtils.sinDeg(ang);
            float hx = cx + cos * (r * 0.72f);
            float hy = cy + sin * (r * 0.72f);
            float tipX = cx + cos * (r * 0.90f);
            float tipY = cy + sin * (r * 0.90f);

            sr.setColor(0.38f, 0.14f, 0.52f, 1f);
            sr.triangle(hx - sin * 3.5f, hy + cos * 3.5f, hx + sin * 3.5f, hy - cos * 3.5f, tipX, tipY);

            // Glowing purple horn tip
            sr.setColor(0.95f, 0.30f, 1f, 1f);
            sr.circle(tipX, tipY, 1.8f);
        }

        // Inner void bezel
        sr.setColor(0.08f, 0.04f, 0.12f, 1f);
        sr.circle(cx, cy, r * 0.65f);

        // 3. Shimmering Nether Vein Filament (Interwoven Violet & Blood-crimson)
        sr.setColor(0.85f, 0.20f, 0.95f, 0.95f);
        sr.circle(cx, cy, r * 0.55f);
        sr.setColor(0.60f, 0.10f, 0.35f, 0.95f);
        sr.circle(cx, cy, r * 0.44f);
        sr.setColor(0.10f, 0.04f, 0.15f, 1f);
        sr.circle(cx, cy, r * 0.34f);

        // 4. Central Nether Core & Eye
        sr.setColor(0.28f, 0.10f, 0.38f, 1f);
        sr.circle(cx, cy, r * 0.26f);
        sr.setColor(0.95f, 0.35f, 0.90f, 1f);
        sr.circle(cx, cy, r * 0.18f);

        // Void Singularity Pupil
        sr.setColor(0.05f, 0.02f, 0.08f, 1f);
        sr.circle(cx, cy, r * 0.10f);
        sr.setColor(Color.WHITE);
        sr.circle(cx - 1.5f, cy + 1.5f, 1.8f);
    }

    public static void drawVucThamHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Abyssal Shadow Miasma Aura
        sr.setColor(0.40f, 0.08f, 0.60f, 0.22f);
        sr.circle(cx, cy, r * 0.96f);

        // 2. Dragon Bone Shank & Bend
        float shankX = cx - r * 0.22f;
        float topY = cy + r * 0.56f;
        float bendY = cy - r * 0.38f;
        float barbX = cx + r * 0.38f;

        // Dark obsidian bone shank
        sr.setColor(0.14f, 0.06f, 0.20f, 1f);
        sr.rectLine(shankX, topY, shankX, bendY, Math.max(5f, r * 0.22f));
        sr.rectLine(shankX, bendY, barbX, bendY, Math.max(5f, r * 0.22f));
        sr.rectLine(barbX, bendY, barbX, cy + r * 0.20f, Math.max(4.5f, r * 0.20f));

        // Violet-crimson void fuller (rãnh năng lượng dọc thân)
        sr.setColor(0.90f, 0.25f, 0.95f, 0.95f);
        sr.rectLine(shankX, topY - r * 0.12f, shankX, bendY + 2f, Math.max(1.8f, r * 0.08f));
        sr.rectLine(shankX, bendY, barbX, bendY, Math.max(1.8f, r * 0.08f));

        // 3. Spiky Demon Bone Scales on Shank Back
        for (int s = 0; s < 4; s++) {
            float sy = topY - r * 0.15f - s * r * 0.18f;
            sr.setColor(0.38f, 0.14f, 0.52f, 1f);
            sr.triangle(shankX - r * 0.08f, sy, shankX - r * 0.24f, sy + r * 0.08f, shankX - r * 0.08f, sy + r * 0.14f);
            sr.setColor(0.95f, 0.35f, 1f, 1f);
            sr.circle(shankX - r * 0.24f, sy + r * 0.08f, 1.5f);
        }

        // 4. Triple Barbed Fiend Talons & Nether Point
        float pointX = barbX + r * 0.02f;
        float pointY = cy + r * 0.28f;

        // Main spear talon
        sr.setColor(0.85f, 0.20f, 0.85f, 1f);
        sr.triangle(barbX - r * 0.10f, cy + r * 0.15f, barbX + r * 0.10f, cy + r * 0.15f, pointX, pointY + r * 0.22f);

        // Deadly dual backward barbed fangs
        sr.setColor(0.98f, 0.30f, 0.75f, 1f);
        sr.triangle(barbX - r * 0.02f, cy + r * 0.24f, barbX - r * 0.22f, cy + r * 0.08f, barbX - r * 0.06f, cy + r * 0.12f);
        sr.triangle(barbX + r * 0.06f, cy + r * 0.24f, barbX + r * 0.24f, cy + r * 0.10f, barbX + r * 0.08f, cy + r * 0.14f);

        // Talon core glow
        sr.setColor(Color.WHITE);
        sr.circle(pointX, pointY + r * 0.16f, 2f);

        // 5. Crown Eyelet: Demonic Horned Skull & Blood Talisman
        sr.setColor(0.30f, 0.10f, 0.42f, 1f);
        sr.circle(shankX, topY, r * 0.25f);
        sr.setColor(0.08f, 0.04f, 0.12f, 1f);
        sr.circle(shankX, topY, r * 0.14f);

        // Demon Horns atop eyelet
        sr.setColor(0.65f, 0.20f, 0.85f, 1f);
        sr.triangle(shankX - r * 0.08f, topY + r * 0.12f, shankX - r * 0.22f, topY + r * 0.26f, shankX, topY + r * 0.20f);
        sr.triangle(shankX + r * 0.08f, topY + r * 0.12f, shankX + r * 0.22f, topY + r * 0.26f, shankX, topY + r * 0.20f);

        // Dangling Blood Pearl Charm
        sr.setColor(0.85f, 0.25f, 0.95f, 0.85f);
        sr.rectLine(shankX, topY, shankX - r * 0.28f, topY - r * 0.20f, 1.8f);
        sr.setColor(0.95f, 0.18f, 0.45f, 1f);
        sr.circle(shankX - r * 0.28f, topY - r * 0.20f, 4.5f);
        sr.setColor(Color.WHITE);
        sr.circle(shankX - r * 0.28f - 1f, topY - r * 0.20f + 1f, 1.6f);
    }

    // =========================================================================
    // 7. HOÀNG GIA SET (Platinum Royal Crown Scepter, Imperial Silk Spool, Royal Jadeite Gold Hook)
    // =========================================================================
    public static void drawHoangGiaRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Royal Golden & Violet Celestial Aura
        sr.setColor(1.0f, 0.85f, 0.32f, 0.22f);
        sr.circle(cx, cy, r * 1.32f);
        sr.setColor(0.65f, 0.25f, 0.85f, 0.16f);
        sr.circle(cx, cy, r * 1.05f);

        // 2. Heavy Polished Platinum Core Shaft (Thân trượng bạch kim vương giả)
        float startX = cx - r * 0.78f, startY = cy - r * 0.78f;
        float endX = cx + r * 0.60f, endY = cy + r * 0.60f;
        sr.setColor(0.70f, 0.74f, 0.84f, 1f); // Dark platinum backing
        sr.rectLine(startX, startY, endX, endY, Math.max(4.2f, r * 0.15f));
        sr.setColor(0.92f, 0.95f, 1.0f, 1f); // Gleaming white platinum core
        sr.rectLine(startX, startY, endX, endY, Math.max(2.6f, r * 0.09f));

        // 3. Imperial Crimson Velvet Grip & Spiraling 24K Gold Wire (Bọc nhung đỏ thẫm & chỉ vàng)
        float gripStartX = cx - r * 0.75f, gripStartY = cy - r * 0.75f;
        float gripEndX = cx - r * 0.28f, gripEndY = cy - r * 0.28f;
        sr.setColor(0.72f, 0.12f, 0.22f, 1f); // Royal crimson velvet
        sr.rectLine(gripStartX, gripStartY, gripEndX, gripEndY, Math.max(5.2f, r * 0.19f));

        // 24K Gold spiraling bands around velvet
        sr.setColor(1.0f, 0.88f, 0.35f, 1f);
        for (int i = 0; i < 4; i++) {
            float bx = gripStartX + i * r * 0.14f;
            float by = gripStartY + i * r * 0.14f;
            sr.rectLine(bx - r * 0.06f, by + r * 0.06f, bx + r * 0.06f, by - r * 0.06f, Math.max(1.8f, r * 0.06f));
        }

        // Golden filigree joint rings & ferrule collars
        sr.setColor(1.0f, 0.82f, 0.25f, 1f);
        sr.circle(gripEndX, gripEndY, Math.max(3.2f, r * 0.12f));
        sr.circle(cx + r * 0.05f, cy + r * 0.05f, Math.max(2.6f, r * 0.10f));
        sr.circle(cx + r * 0.32f, cy + r * 0.32f, Math.max(2.6f, r * 0.10f));

        // 4. Imperial Crown Butt Cap with Royal Ruby Jewel (Chuôi trượng vương miện ngọc ruby)
        sr.setColor(1.0f, 0.82f, 0.22f, 1f);
        sr.circle(startX, startY, Math.max(4.5f, r * 0.20f));
        // Embedded Oval Ruby
        sr.setColor(0.95f, 0.12f, 0.25f, 1f);
        sr.circle(startX, startY, Math.max(2.8f, r * 0.12f));
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(startX - 0.8f, startY + 0.8f, 1.2f);

        // 5. 3 Golden Ceramic Guides with Embedded Royal Sapphires (Khoen vàng nạm lam ngọc)
        for (int i = 0; i < 3; i++) {
            float gx = cx - r * 0.12f + i * r * 0.24f;
            float gy = cy - r * 0.12f + i * r * 0.24f;
            sr.setColor(1.0f, 0.82f, 0.25f, 1f);
            sr.rectLine(gx, gy, gx - r * 0.09f, gy + r * 0.09f, 2f);
            // Sapphire jewel ring
            sr.setColor(0.20f, 0.45f, 0.95f, 1f);
            sr.circle(gx - r * 0.09f, gy + r * 0.09f, Math.max(2f, r * 0.08f));
            sr.setColor(0.60f, 0.85f, 1.0f, 1f);
            sr.circle(gx - r * 0.09f, gy + r * 0.09f, Math.max(1f, r * 0.04f));
        }

        // 6. Sculpted Imperial Golden Crown Finial (Vương miện hoàng gia trên đỉnh trượng)
        float hx = cx + r * 0.72f, hy = cy + r * 0.72f;
        // Crown Base circlet
        sr.setColor(1.0f, 0.85f, 0.28f, 1f);
        sr.circle(hx, hy, Math.max(4.5f, r * 0.18f));
        // 3 Crown fleur-de-lis points
        sr.setColor(1.0f, 0.88f, 0.35f, 1f);
        sr.triangle(hx - r * 0.18f, hy, hx - r * 0.28f, hy + r * 0.24f, hx - r * 0.08f, hy + r * 0.08f);
        sr.triangle(hx, hy, hx - r * 0.04f, hy + r * 0.32f, hx + r * 0.08f, hy + r * 0.08f);
        sr.triangle(hx, hy - r * 0.18f, hx + r * 0.24f, hy - r * 0.28f, hx + r * 0.08f, hy - r * 0.08f);
        // Center crown peak gem (Luminous star diamond)
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(hx - r * 0.04f, hy + r * 0.32f, Math.max(1.8f, r * 0.07f));
        sr.setColor(0.95f, 0.15f, 0.25f, 1f);
        sr.circle(hx, hy, Math.max(2.2f, r * 0.09f));
    }

    public static void drawHoangGiaLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Imperial Royal Aura
        sr.setColor(1.0f, 0.85f, 0.30f, 0.22f);
        sr.circle(cx, cy, r * 1.30f);
        sr.setColor(0.75f, 0.15f, 0.25f, 0.15f);
        sr.circle(cx, cy, r * 1.05f);

        // 2. Sculpted Imperial Ivory & 24K Gold Rotor Frame (Khung tời ngà voi dát vàng)
        sr.setColor(0.95f, 0.93f, 0.86f, 1f); // Polished Ivory
        sr.circle(cx, cy, r * 0.92f);
        // Outer 24k gold filigree ring
        sr.setColor(1.0f, 0.85f, 0.25f, 1f);
        sr.circle(cx, cy, r * 0.84f);
        sr.setColor(0.14f, 0.12f, 0.16f, 1f);
        sr.circle(cx, cy, r * 0.74f);

        // 8 Imperial Gold Petal Crown Crests around the rim
        sr.setColor(1.0f, 0.88f, 0.35f, 1f);
        for (int i = 0; i < 8; i++) {
            float ang = i * 45f * MathUtils.degreesToRadians;
            float cos = MathUtils.cos(ang);
            float sin = MathUtils.sin(ang);
            float tx = cx + cos * r * 0.78f;
            float ty = cy + sin * r * 0.78f;
            sr.circle(tx, ty, Math.max(2.5f, r * 0.10f));
            sr.triangle(tx, ty, tx - sin * r * 0.06f, ty + cos * r * 0.06f, cx + cos * r * 0.96f, cy + sin * r * 0.96f);
        }

        // 3. Deep Spool Well of Crimson & Gold Imperial Silk (Tơ tằm thượng hạng màu chu sa)
        sr.setColor(0.12f, 0.08f, 0.10f, 1f);
        sr.circle(cx, cy, r * 0.68f);

        // Woven royal crimson silk layers
        sr.setColor(0.78f, 0.12f, 0.20f, 1f); // Deep imperial crimson silk
        sr.circle(cx, cy, r * 0.62f);
        sr.setColor(1.0f, 0.85f, 0.30f, 1f); // 24K Gold spun thread
        sr.circle(cx, cy, r * 0.52f);
        sr.setColor(0.85f, 0.16f, 0.25f, 1f); // Scarlet silk
        sr.circle(cx, cy, r * 0.42f);
        sr.setColor(1.0f, 0.92f, 0.45f, 1f); // Core gold thread
        sr.circle(cx, cy, r * 0.32f);

        // Spiral silk brocade weave texture (Hoa văn dệt tơ tằm chéo)
        sr.setColor(1.0f, 0.82f, 0.25f, 0.80f);
        for (int i = 0; i < 8; i++) {
            float ang = (i * 45f + 22.5f) * MathUtils.degreesToRadians;
            float cos = MathUtils.cos(ang);
            float sin = MathUtils.sin(ang);
            sr.rectLine(cx + cos * r * 0.34f, cy + sin * r * 0.34f, cx + cos * r * 0.62f, cy + sin * r * 0.62f, 1.4f);
        }

        // 4. Center Imperial Seal Hub with Royal Ruby (Trục ấn triều đình nạm ngọc ruby)
        sr.setColor(0.95f, 0.92f, 0.85f, 1f); // Ivory collar
        sr.circle(cx, cy, r * 0.24f);
        sr.setColor(1.0f, 0.82f, 0.22f, 1f); // Gold mount
        sr.circle(cx, cy, r * 0.17f);
        // Ruby core
        sr.setColor(0.95f, 0.12f, 0.25f, 1f);
        sr.circle(cx, cy, r * 0.10f);
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(cx - r * 0.03f, cy + r * 0.03f, 1.2f);

        // 5. Gilded Bail Arm & Flowing Royal Silk Line (Càng tời dát vàng & sợi tơ tằm tỏa sáng)
        sr.setColor(1.0f, 0.82f, 0.22f, 1f);
        sr.rectLine(cx + r * 0.52f, cy - r * 0.28f, cx + r * 0.84f, cy + r * 0.42f, Math.max(2.6f, r * 0.09f));
        // Diamond roller guide
        sr.setColor(0.55f, 0.88f, 1.0f, 1f);
        sr.circle(cx + r * 0.84f, cy + r * 0.42f, Math.max(2.8f, r * 0.10f));
        // Glowing gold-crimson silk thread trailing out
        sr.setColor(1.0f, 0.88f, 0.35f, 1f);
        sr.rectLine(cx + r * 0.84f, cy + r * 0.42f, cx + r * 1.15f, cy + r * 0.92f, Math.max(1.8f, r * 0.06f));
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + r * 1.02f, cy + r * 0.70f, 1.5f);
    }

    public static void drawHoangGiaHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Imperial Jade & Gold Aura
        sr.setColor(0.25f, 0.85f, 0.50f, 0.22f);
        sr.circle(cx, cy, r * 1.25f);
        sr.setColor(1.0f, 0.85f, 0.30f, 0.16f);
        sr.circle(cx, cy, r * 1.05f);

        // 2. Imperial Golden Lotus Eyelet & Royal Ruby Charm (Khuyên hoa sen vàng & chuỗi ngọc)
        float eyeX = cx - r * 0.22f;
        float eyeY = cy + r * 0.70f;
        sr.setColor(1.0f, 0.82f, 0.22f, 1f);
        sr.circle(eyeX, eyeY, Math.max(4.6f, r * 0.22f));
        sr.setColor(0.08f, 0.12f, 0.16f, 1f);
        sr.circle(eyeX, eyeY, Math.max(2.0f, r * 0.10f));

        // 24K Gold filigree crown collar
        sr.setColor(1.0f, 0.88f, 0.35f, 1f);
        sr.rect(eyeX - r * 0.14f, eyeY - r * 0.22f, r * 0.28f, r * 0.12f);

        // Hanging Royal Crimson Silk Tassel with dangling Ruby Gem (Tua rua nhung đỏ nạm ngọc ruby)
        sr.setColor(0.85f, 0.14f, 0.22f, 1f);
        sr.triangle(eyeX, eyeY - r * 0.22f, eyeX - r * 0.18f, eyeY - r * 0.52f, eyeX + r * 0.04f, eyeY - r * 0.52f);
        // Ruby pendant bead
        sr.setColor(0.95f, 0.15f, 0.25f, 1f);
        sr.circle(eyeX - r * 0.07f, eyeY - r * 0.54f, Math.max(2.2f, r * 0.09f));
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(eyeX - r * 0.09f, eyeY - r * 0.52f, 1.2f);

        // 3. Carved Imperial Emerald-Jadeite Shank (Thân ngọc cẩm thạch ngọc bích chạm rồng)
        float shankX = cx - r * 0.20f;
        float bendBottomY = cy - r * 0.42f;

        // Deep translucent jadeite stone
        sr.setColor(0.12f, 0.58f, 0.34f, 1f);
        sr.rectLine(shankX, eyeY - r * 0.18f, shankX, bendBottomY, Math.max(5.8f, r * 0.24f));
        // Luminous emerald-jade inner core
        sr.setColor(0.28f, 0.88f, 0.54f, 1f);
        sr.rectLine(shankX, eyeY - r * 0.18f, shankX, bendBottomY, Math.max(3.6f, r * 0.15f));

        // 24K Gold Dragon Ribbon Wraps around jade shank (Dải rồng vàng quấn quanh thân ngọc)
        sr.setColor(1.0f, 0.85f, 0.25f, 1f);
        for (int i = 0; i < 3; i++) {
            float by = eyeY - r * 0.32f - i * r * 0.24f;
            sr.rectLine(shankX - r * 0.14f, by + r * 0.06f, shankX + r * 0.14f, by - r * 0.06f, Math.max(2.2f, r * 0.08f));
        }

        // 4. Sweeping U-Bend of Carved Jadeite & Gold Spine (Đáy uốn cong cẩm thạch bọc vàng)
        sr.setColor(0.12f, 0.58f, 0.34f, 1f);
        sr.rectLine(shankX, bendBottomY, cx + r * 0.45f, bendBottomY + r * 0.08f, Math.max(5.8f, r * 0.24f));
        sr.setColor(0.28f, 0.88f, 0.54f, 1f);
        sr.rectLine(shankX, bendBottomY, cx + r * 0.45f, bendBottomY + r * 0.08f, Math.max(3.6f, r * 0.15f));
        // Gold reinforcement spine under the bend
        sr.setColor(1.0f, 0.85f, 0.25f, 1f);
        sr.rectLine(shankX, bendBottomY - r * 0.04f, cx + r * 0.35f, bendBottomY + r * 0.04f, 2f);

        // 5. Polished Platinum Recurve Point & Razor Barb (Mũi bạch kim siêu bén & ngạnh khóa mồi)
        float hookTipX = cx + r * 0.50f;
        float hookTipY = cy + r * 0.38f;

        // Jade upward curve
        sr.setColor(0.12f, 0.58f, 0.34f, 1f);
        sr.triangle(cx + r * 0.35f, bendBottomY + r * 0.04f, cx + r * 0.62f, cy + r * 0.10f, hookTipX, hookTipY);
        // Pure Platinum forged razor cutting point
        sr.setColor(0.85f, 0.88f, 0.96f, 1f);
        sr.triangle(cx + r * 0.38f, bendBottomY + r * 0.06f, cx + r * 0.56f, cy + r * 0.12f, hookTipX, hookTipY);

        // Platinum Inward Barb (Ngạnh móc ngược bạch kim)
        sr.setColor(0.95f, 0.97f, 1.0f, 1f);
        sr.triangle(hookTipX, hookTipY, cx + r * 0.42f, cy + r * 0.18f, cx + r * 0.26f, cy + r * 0.20f);

        // Gold inlay accent line
        sr.setColor(1.0f, 0.88f, 0.35f, 0.95f);
        sr.rectLine(cx + r * 0.38f, bendBottomY + r * 0.15f, hookTipX - r * 0.05f, hookTipY - r * 0.05f, 1.5f);

        // Specular glint on diamond-honed platinum tip
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(hookTipX, hookTipY, Math.max(1.5f, r * 0.06f));
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
        // 1. Radiant Solar Corona & Golden Aura
        sr.setColor(1f, 0.88f, 0.25f, 0.16f);
        sr.circle(cx, cy, r * 0.98f);
        sr.setColor(1f, 0.95f, 0.50f, 0.25f);
        sr.circle(cx + r * 0.48f, cy + r * 0.48f, r * 0.52f);

        // 2. Pure White-Gold Celestial Shaft (Thân trượng vàng ròng khảm bạch kim)
        float x1 = cx - r * 0.72f, y1 = cy - r * 0.72f;
        float x2 = cx + r * 0.45f, y2 = cy + r * 0.45f;
        // Burnished gold outer bevel
        sr.setColor(0.92f, 0.72f, 0.16f, 1f);
        sr.rectLine(x1, y1, x2, y2, Math.max(4.2f, r * 0.20f));
        // Luminous ivory-platinum core
        sr.setColor(1.0f, 0.98f, 0.82f, 1f);
        sr.rectLine(x1, y1, x2, y2, Math.max(2.2f, r * 0.10f));

        // 3. 4 Ornate Solar Node Rings with embedded Sunstone gems
        for (float f = 0.22f; f <= 0.82f; f += 0.20f) {
            float nx = x1 + (x2 - x1) * f;
            float ny = y1 + (y2 - y1) * f;
            // Golden node collar
            sr.setColor(0.85f, 0.65f, 0.12f, 1f);
            sr.circle(nx, ny, Math.max(3.2f, r * 0.14f));
            // Inset Ruby / Sunstone jewel
            sr.setColor(1.0f, 0.35f, 0.20f, 1f);
            sr.circle(nx, ny, Math.max(1.8f, r * 0.08f));
            sr.setColor(1f, 1f, 1f, 0.9f);
            sr.circle(nx + 0.6f, ny + 0.6f, Math.max(0.8f, r * 0.04f));
        }

        // 4. White Silk Grip & Gold Cross-Lacing (Chuôi trượng bọc lụa trắng ánh kim)
        float gx = x1 + (x2 - x1) * 0.12f;
        float gy = y1 + (y2 - y1) * 0.12f;
        sr.setColor(0.95f, 0.95f, 0.92f, 1f);
        sr.circle(gx, gy, Math.max(3.8f, r * 0.16f));
        sr.setColor(1.0f, 0.85f, 0.25f, 1f);
        sr.rectLine(x1, y1, gx + 2f, gy + 2f, 1.4f);

        // 5. Stylized Angelic Feather Plumes flanking neck
        float neckX = cx + r * 0.35f, neckY = cy + r * 0.35f;
        sr.setColor(1f, 0.95f, 0.80f, 0.95f);
        sr.triangle(neckX, neckY, neckX - r * 0.25f, neckY + r * 0.12f, neckX - r * 0.08f, neckY + r * 0.22f);
        sr.triangle(neckX, neckY, neckX + r * 0.12f, neckY - r * 0.25f, neckX + r * 0.22f, neckY - r * 0.08f);
        sr.setColor(0.95f, 0.80f, 0.25f, 1f);
        sr.rectLine(neckX, neckY, neckX - r * 0.25f, neckY + r * 0.12f, 1.2f);
        sr.rectLine(neckX, neckY, neckX + r * 0.12f, neckY - r * 0.25f, 1.2f);

        // 6. Grand Solar Corona Headpiece (Đỉnh Trượng Thái Dương Hoàng Kim)
        float sx = cx + r * 0.52f, sy = cy + r * 0.52f;
        // Outer radiant ring
        sr.setColor(0.90f, 0.70f, 0.15f, 1f);
        sr.circle(sx, sy, r * 0.34f);
        sr.setColor(1.0f, 0.90f, 0.35f, 1f);
        sr.circle(sx, sy, r * 0.28f);
        sr.setColor(0.35f, 0.18f, 0.05f, 1f); // Inner shadow bezel
        sr.circle(sx, sy, r * 0.20f);

        // 8 Radiating Solar Rays (4 Cardinal + 4 Diagonal)
        sr.setColor(1f, 0.88f, 0.25f, 1f);
        float rayL = Math.max(9f, r * 0.40f);
        float rayS = Math.max(6f, r * 0.26f);
        // Cardinal
        sr.rectLine(sx, sy + r * 0.28f, sx, sy + r * 0.28f + rayL, 2.2f);
        sr.rectLine(sx, sy - r * 0.28f, sx, sy - r * 0.28f - rayL, 2.2f);
        sr.rectLine(sx + r * 0.28f, sy, sx + r * 0.28f + rayL, sy, 2.2f);
        sr.rectLine(sx - r * 0.28f, sy, sx - r * 0.28f - rayL, sy, 2.2f);
        // Diagonal
        float dOff = (r * 0.28f) * 0.707f;
        float dRay = rayS * 0.707f;
        sr.setColor(1f, 0.96f, 0.60f, 1f);
        sr.rectLine(sx + dOff, sy + dOff, sx + dOff + dRay, sy + dOff + dRay, 1.6f);
        sr.rectLine(sx - dOff, sy + dOff, sx - dOff - dRay, sy + dOff + dRay, 1.6f);
        sr.rectLine(sx + dOff, sy - dOff, sx + dOff + dRay, sy - dOff - dRay, 1.6f);
        sr.rectLine(sx - dOff, sy - dOff, sx - dOff - dRay, sy - dOff - dRay, 1.6f);

        // 7. Central Radiant Sunstone Prism Core
        sr.setColor(1f, 0.95f, 0.45f, 1f);
        sr.circle(sx, sy, r * 0.15f);
        sr.setColor(1f, 1f, 1f, 1f);
        float dSize = Math.max(3f, r * 0.12f);
        sr.rect(sx - dSize, sy - dSize, dSize, dSize, dSize * 2f, dSize * 2f, 1f, 1f, 45f);
        sr.circle(sx + 0.8f, sy + 0.8f, Math.max(1f, r * 0.05f));
    }

    public static void drawQuangMinhLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Radiant Solar Spool Aura
        sr.setColor(1f, 0.88f, 0.25f, 0.18f);
        sr.circle(cx, cy, r * 0.98f);
        sr.setColor(1f, 0.96f, 0.55f, 0.15f);
        sr.circle(cx, cy, r * 0.88f);

        // 2. Heavy Antique Celestial Gold Spool Frame (Khung cuộn vàng kim 24K)
        sr.setColor(0.85f, 0.65f, 0.15f, 1f);
        sr.circle(cx, cy, r * 0.84f);
        sr.setColor(1.0f, 0.90f, 0.38f, 1f);
        sr.circle(cx, cy, r * 0.76f);
        sr.setColor(0.28f, 0.16f, 0.04f, 1f); // Inner spool chamber
        sr.circle(cx, cy, r * 0.68f);

        // 3. 12-Toothed Sunburst Cog Rim (Vành răng cưa mặt trời)
        sr.setColor(1f, 0.88f, 0.30f, 1f);
        for (int i = 0; i < 12; i++) {
            float rad = (i * 30f) * MathUtils.degreesToRadians;
            float px = cx + MathUtils.cos(rad) * r * 0.84f;
            float py = cy + MathUtils.sin(rad) * r * 0.84f;
            sr.circle(px, py, Math.max(1.8f, r * 0.07f));
        }

        // 4. 8 Radiant Sunburst Spokes
        sr.setColor(0.95f, 0.80f, 0.25f, 0.9f);
        for (int i = 0; i < 8; i++) {
            float rad = (i * 45f) * MathUtils.degreesToRadians;
            float sx = cx + MathUtils.cos(rad) * r * 0.68f;
            float sy = cy + MathUtils.sin(rad) * r * 0.68f;
            sr.rectLine(cx, cy, sx, sy, 2f);
        }

        // 5. SIGNATURE DÂY CHỈ VÀNG (Cuộn sợi chỉ vàng ròng đa tầng phản quang lộng lẫy)
        sr.setColor(0.88f, 0.65f, 0.12f, 1f); // Deep gold base
        sr.circle(cx, cy, r * 0.55f);
        sr.setColor(1.0f, 0.88f, 0.30f, 1f); // Vibrant 24K gold strands
        sr.circle(cx, cy, r * 0.46f);
        sr.setColor(1.0f, 0.98f, 0.65f, 1f); // Specular highlight ring
        sr.circle(cx, cy, r * 0.36f);
        sr.setColor(0.35f, 0.20f, 0.06f, 1f); // Center hub socket
        sr.circle(cx, cy, r * 0.26f);

        // 6. Central 8-Pointed Star of Dawn & Sunstone Prism
        sr.setColor(1f, 0.90f, 0.30f, 1f);
        float starS = Math.max(3.5f, r * 0.15f);
        sr.rect(cx - starS, cy - starS, starS, starS, starS * 2f, starS * 2f, 1f, 1f, 0f);
        sr.rect(cx - starS, cy - starS, starS, starS, starS * 2f, starS * 2f, 1f, 1f, 45f);
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx, cy, Math.max(2.0f, r * 0.08f));

        // 7. Flowing Golden Thread Filament curling out with Sun-pearl weight
        sr.setColor(1f, 0.92f, 0.40f, 0.95f);
        sr.rectLine(cx + r * 0.52f, cy - r * 0.10f, cx + r * 0.82f, cy - r * 0.45f, 1.8f);
        sr.rectLine(cx + r * 0.82f, cy - r * 0.45f, cx + r * 0.70f, cy - r * 0.75f, 1.4f);
        // Sun-pearl bead
        sr.setColor(1f, 0.85f, 0.25f, 1f);
        sr.circle(cx + r * 0.70f, cy - r * 0.75f, Math.max(2.8f, r * 0.11f));
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + r * 0.71f, cy - r * 0.74f, Math.max(1.2f, r * 0.05f));
    }

    public static void drawQuangMinhHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Holy Archangel Divine Aura
        sr.setColor(1f, 0.90f, 0.30f, 0.18f);
        sr.circle(cx, cy, r * 0.95f);
        sr.setColor(1f, 0.98f, 0.60f, 0.14f);
        sr.circle(cx, cy, r * 0.82f);

        // 2. Heavy Forged Celestial Gold Shank & J-Bend (Thân móc câu vàng ròng)
        float shankX = cx - r * 0.18f;
        float topY = cy + r * 0.55f;
        float bendY = cy - r * 0.38f;
        float barbX = cx + r * 0.42f;
        float tipY = cy + r * 0.22f;

        // Outer warm gold spine
        sr.setColor(0.90f, 0.70f, 0.15f, 1f);
        sr.rectLine(shankX, topY, shankX, bendY, Math.max(4.5f, r * 0.18f));
        sr.rectLine(shankX, bendY, barbX, bendY, Math.max(4.5f, r * 0.18f));
        sr.rectLine(barbX, bendY, barbX, tipY, Math.max(4.5f, r * 0.18f));

        // Inner luminous white-gold razor edge
        sr.setColor(1f, 0.98f, 0.78f, 1f);
        sr.rectLine(shankX + 1f, topY - 2f, shankX + 1f, bendY + 1f, Math.max(2.2f, r * 0.09f));
        sr.rectLine(shankX + 1f, bendY + 1f, barbX - 1f, bendY + 1f, Math.max(2.2f, r * 0.09f));
        sr.rectLine(barbX - 1f, bendY + 1f, barbX - 1f, tipY - 2f, Math.max(2.2f, r * 0.09f));

        // 3. SPREAD ANGELIC WINGS ON HOOK SHANK (Đôi cánh thiên thần 3 tầng lông vũ)
        // Tier 1 (High plume)
        sr.setColor(1f, 0.95f, 0.82f, 1f);
        sr.triangle(shankX, cy + r * 0.40f, shankX - r * 0.68f, cy + r * 0.62f, shankX, cy + r * 0.52f);
        sr.setColor(0.92f, 0.75f, 0.22f, 1f);
        sr.rectLine(shankX, cy + r * 0.40f, shankX - r * 0.68f, cy + r * 0.62f, 1.4f);

        // Tier 2 (Mid plume)
        sr.setColor(1f, 0.98f, 0.90f, 1f);
        sr.triangle(shankX, cy + r * 0.22f, shankX - r * 0.60f, cy + r * 0.40f, shankX, cy + r * 0.36f);
        sr.setColor(0.92f, 0.75f, 0.22f, 1f);
        sr.rectLine(shankX, cy + r * 0.22f, shankX - r * 0.60f, cy + r * 0.40f, 1.4f);

        // Tier 3 (Lower plume)
        sr.setColor(1f, 0.92f, 0.78f, 1f);
        sr.triangle(shankX, cy + r * 0.06f, shankX - r * 0.45f, cy + r * 0.20f, shankX, cy + r * 0.20f);
        sr.setColor(0.92f, 0.75f, 0.22f, 1f);
        sr.rectLine(shankX, cy + r * 0.06f, shankX - r * 0.45f, cy + r * 0.20f, 1.2f);

        // 4. Holy Spearhead Barb (Mũi giáo ngạnh thần thánh sắc nhọn)
        sr.setColor(1f, 0.90f, 0.30f, 1f);
        sr.triangle(barbX, tipY, barbX - r * 0.20f, tipY - r * 0.16f, barbX + r * 0.14f, tipY - r * 0.16f);
        sr.setColor(1f, 1f, 1f, 1f); // Diamond tip gleam
        sr.triangle(barbX, tipY, barbX - r * 0.08f, tipY - r * 0.10f, barbX + r * 0.08f, tipY - r * 0.10f);

        // 5. Hovering Holy Halo & Eyelet (Vòng hào quang thánh quang)
        // Golden eyelet ring
        sr.setColor(0.88f, 0.68f, 0.15f, 1f);
        sr.circle(shankX, topY, Math.max(4.2f, r * 0.18f));
        sr.setColor(0.12f, 0.08f, 0.04f, 1f);
        sr.circle(shankX, topY, Math.max(2.0f, r * 0.09f));

        // Floating Holy Halo Ring
        float haloY = topY + r * 0.18f;
        sr.setColor(1f, 0.92f, 0.45f, 0.85f);
        sr.rect(shankX - r * 0.26f, haloY - 1.5f, r * 0.52f, 3f);
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.rect(shankX - r * 0.18f, haloY - 0.8f, r * 0.36f, 1.6f);
        // Tiny sunbeams from halo
        sr.setColor(1f, 0.92f, 0.40f, 0.9f);
        sr.rectLine(shankX, haloY + 2f, shankX, haloY + 7f, 1.6f);
        sr.rectLine(shankX - r * 0.22f, haloY + 1f, shankX - r * 0.28f, haloY + 5f, 1.2f);
        sr.rectLine(shankX + r * 0.22f, haloY + 1f, shankX + r * 0.28f, haloY + 5f, 1.2f);
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
        // 1. Glowing battle aura behind spear-rod
        sr.setColor(1f, 0.85f, 0.25f, 0.20f);
        sr.circle(cx, cy, r * 0.95f);

        // 2. Heavy lacquered red-and-gold battle bamboo rod
        float x1 = cx - r * 0.75f, y1 = cy - r * 0.75f;
        float x2 = cx + r * 0.55f, y2 = cy + r * 0.55f;
        sr.setColor(0.72f, 0.14f, 0.16f, 1f); // Imperial crimson lacquer
        sr.rectLine(x1, y1, x2, y2, Math.max(3.5f, r * 0.18f));

        // 3. Gold-leaf bamboo node rings
        sr.setColor(1f, 0.85f, 0.25f, 1f);
        for (float f = 0.2f; f <= 0.8f; f += 0.25f) {
            float nx = x1 + (x2 - x1) * f;
            float ny = y1 + (y2 - y1) * f;
            sr.circle(nx, ny, Math.max(2.5f, r * 0.12f));
        }

        // 4. Dong Son Bronze Spear Tip atop rod
        float tipX = cx + r * 0.85f, tipY = cy + r * 0.85f;
        sr.setColor(0.92f, 0.65f, 0.18f, 1f); // Antique Bronze
        sr.triangle(x2, y2, tipX, tipY, cx + r * 0.45f, cy + r * 0.75f);
        sr.triangle(x2, y2, tipX, tipY, cx + r * 0.75f, cy + r * 0.45f);
        // Steel edge highlight
        sr.setColor(1f, 0.95f, 0.75f, 1f);
        sr.rectLine(x2, y2, tipX, tipY, 1.8f);

        // 5. Fluttering Crimson Battle Pennant (Cờ Lệnh Ngũ Sắc)
        sr.setColor(0.90f, 0.15f, 0.18f, 1f);
        sr.triangle(cx + r * 0.4f, cy + r * 0.4f, cx + r * 0.85f, cy + r * 0.05f, cx + r * 0.35f, cy + r * 0.12f);
        // Gold flame border on pennant
        sr.setColor(1f, 0.85f, 0.25f, 1f);
        sr.rectLine(cx + r * 0.4f, cy + r * 0.4f, cx + r * 0.85f, cy + r * 0.05f, 1.6f);
        sr.rectLine(cx + r * 0.85f, cy + r * 0.05f, cx + r * 0.35f, cy + r * 0.12f, 1.6f);

        // 6. Brilliant Gold Star on pennant
        float starX = cx + r * 0.52f, starY = cy + r * 0.22f;
        sr.setColor(1f, 0.90f, 0.25f, 1f);
        sr.circle(starX, starY, Math.max(2.5f, r * 0.12f));
        sr.rect(starX - 2f, starY - 2f, 2f, 2f, 4f, 4f, 1f, 1f, 45f);

        // 7. Cyan spirit fishing line connecting rod guides
        sr.setColor(0.35f, 0.95f, 1f, 0.85f);
        sr.rectLine(x1 + r * 0.15f, y1 + r * 0.15f, tipX, tipY, 1.2f);
    }

    public static void drawVeQuocLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Radiant bronze drum aura
        sr.setColor(0.95f, 0.65f, 0.2f, 0.18f);
        sr.circle(cx, cy, r * 0.95f);

        // 2. Heavy Antique Bronze Spool outer rim
        sr.setColor(0.72f, 0.48f, 0.18f, 1f);
        sr.circle(cx, cy, r * 0.82f);
        sr.setColor(0.92f, 0.72f, 0.25f, 1f);
        sr.circle(cx, cy, r * 0.75f);
        sr.setColor(0.35f, 0.22f, 0.08f, 1f);
        sr.circle(cx, cy, r * 0.68f);

        // 3. Dong Son 8-Pointed Sunburst Spool Spokes
        sr.setColor(1f, 0.85f, 0.35f, 0.9f);
        for (int i = 0; i < 8; i++) {
            float rad = (i * 45f) * com.badlogic.gdx.math.MathUtils.degreesToRadians;
            float sx = cx + com.badlogic.gdx.math.MathUtils.cos(rad) * r * 0.68f;
            float sy = cy + com.badlogic.gdx.math.MathUtils.sin(rad) * r * 0.68f;
            sr.rectLine(cx, cy, sx, sy, 2f);
        }

        // 4. Braided Crimson & Golden Silk Defense Cord
        sr.setColor(0.85f, 0.18f, 0.22f, 1f); // Crimson braided silk
        sr.circle(cx, cy, r * 0.52f);
        sr.setColor(1f, 0.88f, 0.30f, 1f); // Gold thread accents
        sr.circle(cx, cy, r * 0.38f);
        sr.setColor(0.75f, 0.15f, 0.18f, 1f);
        sr.circle(cx, cy, r * 0.26f);

        // 5. Central Bronze Spindle & Star Rivet
        sr.setColor(0.25f, 0.15f, 0.08f, 1f);
        sr.circle(cx, cy, r * 0.16f);
        sr.setColor(1f, 0.90f, 0.35f, 1f);
        sr.circle(cx, cy, r * 0.09f);

        // 6. Bronze crank handle
        float hx = cx + r * 0.55f, hy = cy + r * 0.45f;
        sr.setColor(0.85f, 0.60f, 0.20f, 1f);
        sr.rectLine(cx, cy, hx, hy, 2.5f);
        sr.setColor(0.6f, 0.15f, 0.15f, 1f);
        sr.circle(hx, hy, r * 0.12f);
        sr.setColor(1f, 0.85f, 0.3f, 1f);
        sr.circle(hx, hy, r * 0.06f);
    }

    public static void drawVeQuocHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Dragon claw aura
        sr.setColor(1f, 0.3f, 0.3f, 0.18f);
        sr.circle(cx, cy, r * 0.95f);

        // 2. Thick sacred bronze spine shank
        float shankX = cx - r * 0.25f;
        float topY = cy + r * 0.60f;
        float bendY = cy - r * 0.45f;
        float barbX = cx + r * 0.48f;
        float barbY = cy + r * 0.25f;

        // Shank & Bend
        sr.setColor(0.78f, 0.52f, 0.18f, 1f); // Antique Bronze
        sr.rectLine(shankX, topY, shankX, bendY, Math.max(3.8f, r * 0.18f));
        sr.rectLine(shankX, bendY, barbX, bendY, Math.max(3.8f, r * 0.18f));
        sr.rectLine(barbX, bendY, barbX, barbY, Math.max(3.5f, r * 0.16f));

        // 3. Razor-sharp Dragon Claw Barb Tip
        sr.setColor(0.95f, 0.75f, 0.25f, 1f);
        sr.triangle(barbX - r * 0.08f, barbY - r * 0.1f, barbX + r * 0.08f, barbY - r * 0.1f, barbX - r * 0.15f, barbY + r * 0.25f);
        // Bloodline razor edge
        sr.setColor(1f, 0.2f, 0.25f, 1f);
        sr.rectLine(barbX, barbY - r * 0.1f, barbX - r * 0.15f, barbY + r * 0.25f, 1.5f);

        // 4. Hook spine wave notches (Dong Son Wave Motif)
        sr.setColor(1f, 0.88f, 0.35f, 1f);
        sr.circle(shankX, cy + r * 0.25f, r * 0.09f);
        sr.circle(shankX, cy, r * 0.09f);
        sr.circle(shankX, cy - r * 0.25f, r * 0.09f);

        // 5. Crown Eyelet with Golden 5-Pointed Star
        sr.setColor(0.85f, 0.60f, 0.20f, 1f);
        sr.circle(shankX, topY, r * 0.25f);
        sr.setColor(0.12f, 0.15f, 0.22f, 1f);
        sr.circle(shankX, topY, r * 0.14f);
        sr.setColor(1f, 0.90f, 0.30f, 1f);
        sr.circle(shankX, topY + r * 0.04f, r * 0.12f);
        sr.rect(shankX - 2.5f, topY + r * 0.04f - 2.5f, 2.5f, 2.5f, 5f, 5f, 1f, 1f, 45f);

        // 6. Fluttering Red Silk Tassel from eyelet
        sr.setColor(0.90f, 0.15f, 0.20f, 1f);
        sr.rectLine(shankX, topY, shankX - r * 0.35f, topY - r * 0.25f, 2.5f);
        sr.rectLine(shankX, topY, shankX - r * 0.22f, topY - r * 0.35f, 2f);
    }

    // =========================================================================
    // 11. THẦN KHÍ LẠC HỒNG SET (Imperial Dong Son Bronze Drum Staff, Sacred Dragon Spool, Au Co Lotus Hook)
    // =========================================================================
    public static void drawLacHongRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Radiant Dong Son Solar Bronze & Crimson Ruby Flame Aura
        sr.setColor(1f, 0.72f, 0.18f, 0.18f);
        sr.circle(cx, cy, r * 0.98f);
        sr.setColor(0.95f, 0.18f, 0.22f, 0.22f);
        sr.circle(cx + r * 0.48f, cy + r * 0.48f, r * 0.54f);

        // 2. Antique Dong Son Bronze & 24K Gold Celestial Shaft
        float x1 = cx - r * 0.72f, y1 = cy - r * 0.72f;
        float x2 = cx + r * 0.45f, y2 = cy + r * 0.45f;
        // Deep antique bronze mantle
        sr.setColor(0.68f, 0.42f, 0.14f, 1f);
        sr.rectLine(x1, y1, x2, y2, Math.max(4.5f, r * 0.22f));
        // Burnished 24K royal gold inlay
        sr.setColor(1.0f, 0.84f, 0.25f, 1f);
        sr.rectLine(x1, y1, x2, y2, Math.max(2.6f, r * 0.12f));
        // Luminous imperial emerald jade core vein
        sr.setColor(0.18f, 0.78f, 0.45f, 0.95f);
        sr.rectLine(x1, y1, x2, y2, Math.max(1.2f, r * 0.05f));

        // 3. 4 Raised Dong Son Geometric Collar Rings with Embedded Glowing Rubies
        for (float f = 0.20f; f <= 0.80f; f += 0.20f) {
            float nx = x1 + (x2 - x1) * f;
            float ny = y1 + (y2 - y1) * f;
            // Bronze collar ring
            sr.setColor(0.85f, 0.62f, 0.18f, 1f);
            sr.circle(nx, ny, Math.max(3.4f, r * 0.15f));
            // Inset flaming Ruby gem
            sr.setColor(0.96f, 0.15f, 0.25f, 1f);
            sr.circle(nx, ny, Math.max(1.8f, r * 0.08f));
            sr.setColor(1f, 1f, 1f, 0.9f);
            sr.circle(nx + 0.6f, ny + 0.6f, Math.max(0.8f, r * 0.04f));
        }

        // 4. Imperial Jade Grip & Cross-Laced Crimson Silk Cord
        float gx = x1 + (x2 - x1) * 0.10f;
        float gy = y1 + (y2 - y1) * 0.10f;
        sr.setColor(0.15f, 0.65f, 0.38f, 1f);
        sr.circle(gx, gy, Math.max(4.0f, r * 0.18f));
        sr.setColor(0.90f, 0.15f, 0.20f, 1f);
        sr.rectLine(x1, y1, gx + 2f, gy + 2f, 1.8f);
        // Hanging Dong Son bronze bell pommel
        sr.setColor(0.85f, 0.62f, 0.18f, 1f);
        sr.triangle(x1, y1, x1 - r * 0.12f, y1 - r * 0.18f, x1 - r * 0.18f, y1 - r * 0.10f);

        // 5. Royal Crimson Silk Tassels & Dangling Jade Beads
        sr.setColor(0.90f, 0.15f, 0.22f, 0.95f);
        sr.rectLine(gx, gy, gx - r * 0.26f, gy - r * 0.18f, 2.2f);
        sr.rectLine(gx, gy, gx - r * 0.18f, gy - r * 0.28f, 1.8f);
        sr.setColor(0.20f, 0.85f, 0.50f, 1f);
        sr.circle(gx - r * 0.26f, gy - r * 0.18f, Math.max(1.6f, r * 0.07f));
        sr.circle(gx - r * 0.18f, gy - r * 0.28f, Math.max(1.4f, r * 0.06f));

        // 6. Miniature Dong Son Bronze Drum Headpiece (Trống Đồng Đông Sơn Cổ Điển)
        float sx = cx + r * 0.50f, sy = cy + r * 0.50f;
        // Outer bronze drum flange
        sr.setColor(0.72f, 0.45f, 0.14f, 1f);
        sr.circle(sx, sy, r * 0.36f);
        sr.setColor(0.98f, 0.82f, 0.22f, 1f);
        sr.circle(sx, sy, r * 0.30f);
        sr.setColor(0.40f, 0.20f, 0.06f, 1f); // Inner drum face recess
        sr.circle(sx, sy, r * 0.22f);

        // 14-Pointed Dong Son Sunburst Rays (Mặt trời 14 tia sáng rực lửa)
        sr.setColor(1.0f, 0.88f, 0.28f, 1f);
        for (int i = 0; i < 14; i++) {
            float rad = (i * (360f / 14f)) * MathUtils.degreesToRadians;
            float px1 = sx + MathUtils.cos(rad) * r * 0.12f;
            float py1 = sy + MathUtils.sin(rad) * r * 0.12f;
            float px2 = sx + MathUtils.cos(rad) * r * 0.22f;
            float py2 = sy + MathUtils.sin(rad) * r * 0.22f;
            sr.rectLine(px1, py1, px2, py2, 1.4f);
        }

        // Tangential dot circle pattern (Vòng hoa văn chấm tròn tiếp tuyến)
        sr.setColor(1.0f, 0.92f, 0.45f, 0.9f);
        for (int i = 0; i < 10; i++) {
            float rad = (i * 36f) * MathUtils.degreesToRadians;
            float dx = sx + MathUtils.cos(rad) * r * 0.26f;
            float dy = sy + MathUtils.sin(rad) * r * 0.26f;
            sr.circle(dx, dy, Math.max(1.2f, r * 0.04f));
        }

        // 7. MAJESTIC CHIM LẠC BIRD IN FULL FLIGHT (Chim Lạc sải cánh thần thoại)
        float bx = sx + r * 0.05f, by = sy + r * 0.05f;
        // Majestic outstretched wings (Tầng cánh chim Lạc vươn cao kiêu hãnh)
        sr.setColor(1f, 0.88f, 0.25f, 1f);
        sr.triangle(bx, by, bx - r * 0.32f, by + r * 0.24f, bx - r * 0.10f, by + r * 0.35f);
        sr.triangle(bx, by, bx + r * 0.24f, by - r * 0.32f, bx + r * 0.35f, by - r * 0.10f);
        // Wing feather serrations
        sr.setColor(1f, 0.96f, 0.55f, 1f);
        sr.triangle(bx, by, bx - r * 0.28f, by + r * 0.20f, bx - r * 0.18f, by + r * 0.28f);
        sr.triangle(bx, by, bx + r * 0.20f, by - r * 0.28f, bx + r * 0.28f, by - r * 0.18f);

        // Long stylized neck, head & characteristic crest (Đầu, mỏ nhọn và mào chim Lạc vươn cao)
        sr.setColor(1f, 0.92f, 0.30f, 1f);
        sr.triangle(bx, by, bx + r * 0.26f, by + r * 0.26f, bx + r * 0.08f, by + r * 0.30f);
        // Beak (Mỏ dài thanh thoát)
        sr.setColor(1f, 1f, 0.80f, 1f);
        sr.triangle(bx + r * 0.24f, by + r * 0.24f, bx + r * 0.42f, by + r * 0.38f, bx + r * 0.28f, by + r * 0.28f);
        // Swept-back royal crest (Mào chim Lạc uốn cong)
        sr.setColor(0.96f, 0.18f, 0.25f, 1f);
        sr.triangle(bx + r * 0.12f, by + r * 0.28f, bx - r * 0.05f, by + r * 0.42f, bx + r * 0.18f, by + r * 0.32f);

        // Eye: Piercing Ruby Star
        sr.setColor(0.95f, 0.10f, 0.18f, 1f);
        sr.circle(bx + r * 0.22f, by + r * 0.26f, Math.max(1.8f, r * 0.07f));
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(bx + r * 0.24f, by + r * 0.27f, Math.max(0.9f, r * 0.035f));

        // Long streaming tail plumes (Đuôi chim Lạc trải dài uốn lượn)
        sr.setColor(1.0f, 0.82f, 0.20f, 0.95f);
        sr.triangle(bx, by, bx - r * 0.35f, by - r * 0.20f, bx - r * 0.22f, by - r * 0.32f);
        sr.setColor(0.95f, 0.20f, 0.25f, 0.9f);
        sr.rectLine(bx, by, bx - r * 0.38f, by - r * 0.26f, 1.8f);

        // 8. Central Radiant Ruby Sun Core
        sr.setColor(0.98f, 0.18f, 0.25f, 1f);
        sr.circle(sx, sy, r * 0.11f);
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(sx + 0.5f, sy + 0.5f, Math.max(1.0f, r * 0.045f));
    }

    public static void drawLacHongLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Dragon Pearl Aura & Solar Flame Halo
        sr.setColor(0.95f, 0.22f, 0.22f, 0.20f);
        sr.circle(cx, cy, r * 0.98f);
        sr.setColor(1.0f, 0.85f, 0.25f, 0.18f);
        sr.circle(cx, cy, r * 0.88f);

        // 2. Heavy Antique Dong Son Bronze Drum Spool Casing
        sr.setColor(0.70f, 0.44f, 0.15f, 1f);
        sr.circle(cx, cy, r * 0.85f);
        sr.setColor(1.0f, 0.84f, 0.26f, 1f);
        sr.circle(cx, cy, r * 0.77f);
        sr.setColor(0.28f, 0.14f, 0.05f, 1f); // Recessed spool chamber
        sr.circle(cx, cy, r * 0.68f);

        // 3. 16-Toothed Dong Son Sunburst Cog Rim (Vành răng cưa mặt trời Đông Sơn)
        sr.setColor(1.0f, 0.88f, 0.30f, 1f);
        for (int i = 0; i < 16; i++) {
            float rad = (i * 22.5f) * MathUtils.degreesToRadians;
            float px = cx + MathUtils.cos(rad) * r * 0.85f;
            float py = cy + MathUtils.sin(rad) * r * 0.85f;
            sr.circle(px, py, Math.max(1.8f, r * 0.07f));
        }

        // 4. Coiled Sacred Dragon Sinew (Hàng trăm vòng gân rồng thiêng vàng kim & đỏ ruby)
        for (int l = 0; l < 4; l++) {
            float lr = r * (0.68f - l * 0.09f);
            sr.setColor((l % 2 == 0) ? new Color(1.0f, 0.84f, 0.25f, 0.95f) : new Color(0.95f, 0.20f, 0.25f, 0.95f));
            sr.circle(cx, cy, lr);
        }
        sr.setColor(0.35f, 0.12f, 0.05f, 1f);
        sr.circle(cx, cy, r * 0.38f);

        // 5. DUAL CELESTIAL DRAGONS MOUNT (Song Long Chầu Nguyệt / Rồng Thiêng Lạc Long Quân ôm cuộn cước)
        // Upper Celestial Dragon arching over spool
        sr.setColor(0.18f, 0.75f, 0.42f, 1f); // Emerald dragon body
        sr.rectLine(cx - r * 0.55f, cy + r * 0.45f, cx + r * 0.55f, cy + r * 0.45f, Math.max(3.0f, r * 0.13f));
        sr.setColor(1.0f, 0.85f, 0.25f, 1f); // Golden dragon dorsal fin spines
        for (int d = -3; d <= 3; d++) {
            float dx = cx + d * r * 0.14f;
            sr.triangle(dx - 1.5f, cy + r * 0.48f, dx + 1.5f, cy + r * 0.48f, dx, cy + r * 0.62f);
        }
        // Dragon Head with horns and fierce whiskers (Đầu Rồng Lạc Long Quân)
        float dhx = cx + r * 0.60f, dhy = cy + r * 0.45f;
        sr.setColor(1.0f, 0.85f, 0.25f, 1f);
        sr.triangle(dhx, dhy, dhx + r * 0.24f, dhy + r * 0.08f, dhx + r * 0.10f, dhy + r * 0.20f);
        // Antler Horn
        sr.setColor(0.96f, 0.20f, 0.25f, 1f);
        sr.rectLine(dhx + r * 0.06f, dhy + r * 0.15f, dhx - r * 0.05f, dhy + r * 0.32f, 2f);
        // Dragon Whisker (Râu rồng uốn lượn)
        sr.setColor(1.0f, 0.92f, 0.45f, 1f);
        sr.rectLine(dhx + r * 0.20f, dhy + r * 0.05f, dhx + r * 0.34f, dhy - r * 0.12f, 1.4f);

        // 6. CENTRAL DIVINE DRAGON PEARL (Viên Ngọc Rồng Lạc Hồng rực lửa)
        sr.setColor(1.0f, 0.85f, 0.25f, 1f);
        // 8 Radiant sun rays emerging from pearl
        for (int i = 0; i < 8; i++) {
            float rad = (i * 45f) * MathUtils.degreesToRadians;
            float px1 = cx + MathUtils.cos(rad) * r * 0.15f;
            float py1 = cy + MathUtils.sin(rad) * r * 0.15f;
            float px2 = cx + MathUtils.cos(rad) * r * 0.32f;
            float py2 = cy + MathUtils.sin(rad) * r * 0.32f;
            sr.rectLine(px1, py1, px2, py2, 1.8f);
        }

        // Deep Ruby dragon pearl body
        sr.setColor(0.96f, 0.15f, 0.22f, 1f);
        sr.circle(cx, cy, r * 0.22f);
        sr.setColor(1.0f, 0.45f, 0.30f, 1f);
        sr.circle(cx, cy, r * 0.15f);
        sr.setColor(1.0f, 0.95f, 0.60f, 1f);
        sr.circle(cx, cy, r * 0.08f);
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + 0.8f, cy + 0.8f, Math.max(1.0f, r * 0.045f));

        // 7. Trailing Golden Dragon Filament & Hanging Imperial Jade Talisman
        sr.setColor(1.0f, 0.85f, 0.25f, 0.95f);
        sr.rectLine(cx + r * 0.45f, cy - r * 0.40f, cx + r * 0.22f, cy - r * 0.72f, 2.2f);
        sr.rectLine(cx + r * 0.22f, cy - r * 0.72f, cx - r * 0.10f, cy - r * 0.82f, 1.8f);
        // Dangling Imperial Jade Token (Lệnh bài Lạc Hồng)
        sr.setColor(0.18f, 0.78f, 0.45f, 1f);
        sr.rect(cx - r * 0.18f, cy - r * 0.90f, r * 0.16f, r * 0.18f);
        sr.setColor(1.0f, 0.85f, 0.25f, 1f);
        sr.circle(cx - r * 0.10f, cy - r * 0.81f, Math.max(1.2f, r * 0.05f));
    }

    public static void drawLacHongHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Sacred Lotus Halo & Celestial Aura
        sr.setColor(0.96f, 0.35f, 0.55f, 0.18f);
        sr.circle(cx, cy, r * 0.98f);
        sr.setColor(1.0f, 0.85f, 0.25f, 0.18f);
        sr.circle(cx + r * 0.10f, cy - r * 0.10f, r * 0.82f);

        // 2. CELESTIAL METEORIC BRONZE-GOLD HOOK (Thiên Thạch Thần Kim)
        float shankX = cx - r * 0.22f;
        float topY = cy + r * 0.56f;
        float bendY = cy - r * 0.62f;
        float tipX = cx + r * 0.52f;
        float tipY = cy + r * 0.28f;

        // Antique Bronze spine
        sr.setColor(0.70f, 0.44f, 0.15f, 1f);
        sr.rectLine(shankX, topY, shankX, bendY, Math.max(4.8f, r * 0.22f));
        sr.rectLine(shankX, bendY, tipX, bendY, Math.max(4.8f, r * 0.22f));
        sr.rectLine(tipX, bendY, tipX, tipY, Math.max(4.8f, r * 0.22f));

        // 24K Polished Gold razor blade edge
        sr.setColor(1.0f, 0.86f, 0.28f, 1f);
        sr.rectLine(shankX, topY, shankX, bendY, Math.max(2.4f, r * 0.11f));
        sr.rectLine(shankX, bendY, tipX, bendY, Math.max(2.4f, r * 0.11f));
        sr.rectLine(tipX, bendY, tipX, tipY, Math.max(2.4f, r * 0.11f));

        // Engraved Turquoise Jade Wave Inlays along hook shank
        sr.setColor(0.18f, 0.80f, 0.55f, 0.95f);
        sr.rectLine(shankX, topY - r * 0.20f, shankX, bendY + r * 0.20f, Math.max(1.2f, r * 0.05f));

        // 3. BLOSSOMING SACRED LOTUS FLOWER CRADLE (Đóa Sen Tiên Mẫu Âu Cơ)
        float lx = shankX;
        float ly = topY - r * 0.10f;
        // Lotus Jade Sepals
        sr.setColor(0.18f, 0.72f, 0.40f, 1f);
        sr.triangle(lx, ly, lx - r * 0.32f, ly - r * 0.12f, lx - r * 0.20f, ly - r * 0.24f);
        sr.triangle(lx, ly, lx + r * 0.32f, ly - r * 0.12f, lx + r * 0.20f, ly - r * 0.24f);
        // Outer Pink Lotus Petals
        sr.setColor(0.96f, 0.38f, 0.58f, 1f);
        sr.triangle(lx, ly, lx - r * 0.36f, ly + r * 0.24f, lx - r * 0.12f, ly + r * 0.32f);
        sr.triangle(lx, ly, lx + r * 0.36f, ly + r * 0.24f, lx + r * 0.12f, ly + r * 0.32f);
        // Center Tender Petals with Gold Rim
        sr.setColor(1.0f, 0.88f, 0.94f, 1f);
        sr.triangle(lx, ly, lx - r * 0.16f, ly + r * 0.36f, lx + r * 0.16f, ly + r * 0.36f);
        sr.setColor(1.0f, 0.86f, 0.28f, 1f);
        sr.circle(lx, ly + r * 0.15f, Math.max(1.8f, r * 0.08f));

        // 4. CHIM LẠC BEAK SPEARHEAD BARB & POINT (Ngạnh câu tạo hình mỏ Chim Lạc)
        sr.setColor(1.0f, 0.88f, 0.28f, 1f);
        // Inward facing spearhead barb
        sr.triangle(tipX, tipY - r * 0.14f, tipX, tipY + r * 0.22f, tipX - r * 0.28f, tipY + r * 0.04f);
        // Razor white blade facet
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.triangle(tipX, tipY, tipX, tipY + r * 0.22f, tipX - r * 0.16f, tipY + r * 0.14f);
        // Ruby gem set into barb
        sr.setColor(0.96f, 0.15f, 0.22f, 1f);
        sr.circle(tipX - r * 0.08f, tipY + r * 0.02f, Math.max(1.5f, r * 0.065f));

        // 5. CROWN EYELET & TIÊN CHÂU BÁCH NOÃN (Viên Ngọc Tiên Châu Âu Cơ)
        sr.setColor(1.0f, 0.86f, 0.28f, 1f);
        sr.circle(shankX, topY + r * 0.18f, Math.max(3.8f, r * 0.17f));
        // Luminous Iridescent Opal Pearl (Bách Noãn Tiên Châu)
        sr.setColor(0.98f, 0.98f, 0.92f, 1f);
        sr.circle(shankX, topY + r * 0.18f, Math.max(2.4f, r * 0.11f));
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(shankX + 0.6f, topY + r * 0.20f, Math.max(1.0f, r * 0.045f));

        // 6. Fluttering Twin Royal Crimson Silk Ribbons
        sr.setColor(0.90f, 0.15f, 0.22f, 1f);
        sr.rectLine(shankX, topY + r * 0.12f, shankX - r * 0.32f, topY - r * 0.12f, 2.2f);
        sr.rectLine(shankX, topY + r * 0.12f, shankX - r * 0.22f, topY - r * 0.25f, 1.8f);
        // Dangling Bronze Bell beads
        sr.setColor(0.85f, 0.62f, 0.18f, 1f);
        sr.circle(shankX - r * 0.32f, topY - r * 0.12f, Math.max(1.6f, r * 0.07f));
        sr.circle(shankX - r * 0.22f, topY - r * 0.25f, Math.max(1.4f, r * 0.06f));
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
}
