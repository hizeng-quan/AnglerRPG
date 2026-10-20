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

            case "Thần Thoại":
                if (type == EquipmentType.ROD) drawThanThoaiRod(sr, cx, cy, r, mainCol);
                else if (type == EquipmentType.LINE) drawThanThoaiLine(sr, cx, cy, r, mainCol);
                else drawThanThoaiHook(sr, cx, cy, r, mainCol);
                break;

            case "Vệ Quốc Vang Danh":
                if (type == EquipmentType.ROD) drawVeQuocRod(sr, cx, cy, r, mainCol);
                else if (type == EquipmentType.LINE) drawVeQuocLine(sr, cx, cy, r, mainCol);
                else drawVeQuocHook(sr, cx, cy, r, mainCol);
                break;

            case "Thần Khí Lạc Hồng":
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
    private static void drawTapSuRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Bamboo pole with segmented joints and green leaf sprout
        sr.setColor(0.72f, 0.68f, 0.25f, 1f);
        sr.rectLine(cx - r * 0.7f, cy - r * 0.7f, cx + r * 0.7f, cy + r * 0.7f, 3.5f);
        // Darker bamboo rings
        sr.setColor(0.45f, 0.38f, 0.12f, 1f);
        sr.circle(cx - r * 0.35f, cy - r * 0.35f, 2.5f);
        sr.circle(cx, cy, 2.5f);
        sr.circle(cx + r * 0.35f, cy + r * 0.35f, 2.5f);
        // Green bamboo leaf at tip
        sr.setColor(0.35f, 0.85f, 0.35f, 1f);
        sr.triangle(cx + r * 0.65f, cy + r * 0.65f, cx + r * 0.85f, cy + r * 0.80f, cx + r * 0.75f, cy + r * 0.55f);
        // Red-white cork bobber
        sr.setColor(0.9f, 0.2f, 0.2f, 1f);
        sr.circle(cx + r * 0.4f, cy + r * 0.1f, 3.5f);
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + r * 0.4f, cy + r * 0.02f, 2f);
    }

    private static void drawTapSuLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Classic blue spool with clear monofilament
        sr.setColor(0.22f, 0.45f, 0.75f, 1f);
        sr.circle(cx, cy, r * 0.75f);
        sr.setColor(0.75f, 0.90f, 1f, 0.9f);
        sr.circle(cx, cy, r * 0.55f);
        sr.setColor(0.12f, 0.25f, 0.45f, 1f);
        sr.circle(cx, cy, r * 0.3f);
        sr.setColor(col);
        sr.circle(cx, cy, r * 0.15f);
    }

    private static void drawTapSuHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Rustic hook with gray lead split-shot sinker
        sr.setColor(0.65f, 0.70f, 0.75f, 1f);
        sr.rectLine(cx - r * 0.25f, cy + r * 0.55f, cx - r * 0.25f, cy - r * 0.35f, 3f);
        sr.rectLine(cx - r * 0.25f, cy - r * 0.35f, cx + r * 0.35f, cy - r * 0.35f, 3f);
        sr.rectLine(cx + r * 0.35f, cy - r * 0.35f, cx + r * 0.35f, cy + r * 0.2f, 3f);
        sr.triangle(cx + r * 0.35f, cy + r * 0.2f, cx + r * 0.15f, cy + r * 0.05f, cx + r * 0.45f, cy + r * 0.05f);
        // Dark lead ball sinker at top
        sr.setColor(0.35f, 0.38f, 0.42f, 1f);
        sr.circle(cx - r * 0.25f, cy + r * 0.35f, r * 0.22f);
        sr.setColor(0.55f, 0.58f, 0.62f, 1f);
        sr.circle(cx - r * 0.28f, cy + r * 0.38f, r * 0.1f);
    }

    // =========================================================================
    // 2. THỢ SĂN SET
    // =========================================================================
    private static void drawThoSanRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Camo green/brown predator spear staff
        sr.setColor(0.35f, 0.45f, 0.22f, 1f);
        sr.rectLine(cx - r * 0.75f, cy - r * 0.75f, cx + r * 0.75f, cy + r * 0.75f, 4f);
        // Cross wraps (leather bindings)
        sr.setColor(0.55f, 0.35f, 0.15f, 1f);
        sr.rectLine(cx - r * 0.3f, cy - r * 0.25f, cx - r * 0.25f, cy - r * 0.3f, 3f);
        sr.rectLine(cx + r * 0.1f, cy + r * 0.15f, cx + r * 0.15f, cy + r * 0.1f, 3f);
        // Serrated spearhead tip
        sr.setColor(0.85f, 0.85f, 0.75f, 1f);
        sr.triangle(cx + r * 0.65f, cy + r * 0.65f, cx + r * 0.95f, cy + r * 0.95f, cx + r * 0.65f, cy + r * 0.95f);
        sr.triangle(cx + r * 0.65f, cy + r * 0.65f, cx + r * 0.95f, cy + r * 0.95f, cx + r * 0.95f, cy + r * 0.65f);
    }

    private static void drawThoSanLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Metallic zinc/wire spool with sharp coils
        sr.setColor(0.45f, 0.52f, 0.58f, 1f);
        sr.circle(cx, cy, r * 0.75f);
        sr.setColor(0.70f, 0.75f, 0.80f, 1f);
        // Coiled wire pattern
        sr.circle(cx, cy, r * 0.60f);
        sr.setColor(0.20f, 0.25f, 0.30f, 1f);
        sr.circle(cx, cy, r * 0.38f);
        // Hexagonal metal center nut
        sr.setColor(0.85f, 0.75f, 0.30f, 1f);
        sr.rect(cx - r * 0.18f, cy - r * 0.18f, r * 0.18f, r * 0.18f, r * 0.36f, r * 0.36f, 1f, 1f, 45f);
    }

    private static void drawThoSanHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Curved sea beast predator bone talon hook
        sr.setColor(0.88f, 0.85f, 0.72f, 1f);
        sr.rectLine(cx - r * 0.2f, cy + r * 0.6f, cx - r * 0.2f, cy - r * 0.3f, 4.5f);
        sr.rectLine(cx - r * 0.2f, cy - r * 0.3f, cx + r * 0.45f, cy - r * 0.25f, 4.5f);
        // Talon sharp hook tip
        sr.triangle(cx + r * 0.35f, cy - r * 0.3f, cx + r * 0.55f, cy + r * 0.35f, cx + r * 0.25f, cy + r * 0.1f);
        // Barbed tooth on spine
        sr.triangle(cx - r * 0.2f, cy - r * 0.05f, cx - r * 0.45f, cy + r * 0.15f, cx - r * 0.2f, cy + r * 0.15f);
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
    // 7. HOÀNG GIA SET
    // =========================================================================
    private static void drawHoangGiaRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Platinum rod with royal gold crown finial
        sr.setColor(0.85f, 0.88f, 0.95f, 1f); // Platinum shaft
        sr.rectLine(cx - r * 0.7f, cy - r * 0.7f, cx + r * 0.5f, cy + r * 0.5f, 4f);
        // Golden scrollwork bands
        sr.setColor(1f, 0.85f, 0.25f, 1f);
        sr.circle(cx - r * 0.2f, cy - r * 0.2f, 3f);
        sr.circle(cx + r * 0.15f, cy + r * 0.15f, 3f);
        // Royal Crown Finial
        float hx = cx + r * 0.65f, hy = cy + r * 0.65f;
        sr.rect(hx - 6f, hy - 2f, 12f, 4f);
        sr.triangle(hx - 6f, hy + 2f, hx - 6f, hy + 8f, hx - 2f, hy + 2f);
        sr.triangle(hx - 2f, hy + 2f, hx, hy + 10f, hx + 2f, hy + 2f);
        sr.triangle(hx + 2f, hy + 2f, hx + 6f, hy + 8f, hx + 6f, hy + 2f);
    }

    private static void drawHoangGiaLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Imperial ivory spool with crimson and gold silk
        sr.setColor(0.95f, 0.92f, 0.85f, 1f); // Ivory
        sr.circle(cx, cy, r * 0.75f);
        sr.setColor(0.85f, 0.18f, 0.22f, 1f); // Royal Crimson Silk
        sr.circle(cx, cy, r * 0.58f);
        sr.setColor(1f, 0.85f, 0.25f, 1f); // Gold Thread core
        sr.circle(cx, cy, r * 0.35f);
        sr.setColor(0.95f, 0.92f, 0.85f, 1f);
        sr.circle(cx, cy, r * 0.18f);
    }

    private static void drawHoangGiaHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Carved imperial jadeite hook with gold tassel
        sr.setColor(0.20f, 0.75f, 0.45f, 1f); // Jade green
        sr.rectLine(cx - r * 0.2f, cy + r * 0.55f, cx - r * 0.2f, cy - r * 0.35f, 4.5f);
        sr.rectLine(cx - r * 0.2f, cy - r * 0.35f, cx + r * 0.4f, cy - r * 0.35f, 4.5f);
        sr.rectLine(cx + r * 0.4f, cy - r * 0.35f, cx + r * 0.4f, cy + r * 0.25f, 4.5f);
        // Gold wire wrap
        sr.setColor(1f, 0.85f, 0.25f, 1f);
        sr.rect(cx - r * 0.28f, cy + r * 0.1f, r * 0.16f, 3f);
        sr.rect(cx - r * 0.28f, cy + r * 0.22f, r * 0.16f, 3f);
        // Red ruby tassel
        sr.setColor(0.85f, 0.18f, 0.22f, 1f);
        sr.triangle(cx - r * 0.2f, cy + r * 0.55f, cx - r * 0.35f, cy + r * 0.8f, cx - r * 0.05f, cy + r * 0.8f);
    }

    // =========================================================================
    // 8. THỜI KHÔNG SET (CHRONO / SPACE-TIME)
    // =========================================================================
    public static void drawThoiKhongRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Cosmic Temporal Rift Aura
        sr.setColor(0.35f, 0.85f, 1f, 0.20f);
        sr.circle(cx, cy, r * 0.95f);
        sr.setColor(0.65f, 0.25f, 0.95f, 0.22f);
        sr.circle(cx + r * 0.45f, cy + r * 0.45f, r * 0.55f);

        // 2. Dual-twist astral titanium shaft with energy channel
        float x1 = cx - r * 0.72f, y1 = cy - r * 0.72f;
        float x2 = cx + r * 0.42f, y2 = cy + r * 0.42f;
        // Outer dark titanium spine
        sr.setColor(0.16f, 0.18f, 0.28f, 1f);
        sr.rectLine(x1, y1, x2, y2, Math.max(4f, r * 0.18f));
        // Luminous cyan plasma core conduit
        sr.setColor(0.35f, 0.95f, 1f, 0.95f);
        sr.rectLine(x1, y1, x2, y2, Math.max(1.8f, r * 0.08f));

        // 3. Gold chronometer node rings along shaft
        sr.setColor(1f, 0.85f, 0.35f, 1f);
        for (float f = 0.2f; f <= 0.8f; f += 0.3f) {
            float nx = x1 + (x2 - x1) * f;
            float ny = y1 + (y2 - y1) * f;
            sr.circle(nx, ny, Math.max(2.5f, r * 0.11f));
        }

        // 4. Astrolabe Chrono Gear Headpiece
        float ax = cx + r * 0.55f, ay = cy + r * 0.55f;
        float ringR = r * 0.38f;

        // Outer Gear Teeth Ring (Brass Gold)
        sr.setColor(0.95f, 0.75f, 0.25f, 1f);
        sr.circle(ax, ay, ringR);
        // Gear teeth notches (8 teeth)
        for (int t = 0; t < 8; t++) {
            float tAng = t * 45f;
            float tx = ax + MathUtils.cosDeg(tAng) * (ringR + 2f);
            float ty = ay + MathUtils.sinDeg(tAng) * (ringR + 2f);
            sr.circle(tx, ty, Math.max(2f, r * 0.08f));
        }

        // Deep cosmos dial face
        sr.setColor(0.06f, 0.08f, 0.18f, 0.95f);
        sr.circle(ax, ay, ringR * 0.75f);

        // Rotating orbital ring
        sr.setColor(0.4f, 0.95f, 1f, 0.8f);
        sr.circle(ax, ay, ringR * 0.5f);
        sr.setColor(0.06f, 0.08f, 0.18f, 1f);
        sr.circle(ax, ay, ringR * 0.35f);

        // Core Singularity Crystal (Amethyst / Cyan)
        sr.setColor(0.85f, 0.35f, 1f, 1f);
        sr.circle(ax, ay, ringR * 0.22f);
        sr.setColor(Color.WHITE);
        sr.circle(ax, ay, ringR * 0.10f);

        // Chrono Clock Hands (Gold)
        sr.setColor(1f, 0.9f, 0.4f, 1f);
        sr.rectLine(ax, ay, ax + r * 0.22f, ay + r * 0.08f, 1.8f);
        sr.rectLine(ax, ay, ax - r * 0.05f, ay + r * 0.20f, 1.8f);

        // Floating Hourglass Crystal Finial atop staff
        float fx = ax + r * 0.28f, fy = ay + r * 0.28f;
        sr.setColor(0.35f, 0.95f, 1f, 1f);
        sr.triangle(fx - 4f, fy + 8f, fx + 4f, fy + 8f, fx, fy);
        sr.triangle(fx - 4f, fy - 8f, fx + 4f, fy - 8f, fx, fy);
    }

    public static void drawThoiKhongLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Spatio-temporal wormhole distortion aura
        sr.setColor(0.45f, 0.18f, 0.85f, 0.25f);
        sr.circle(cx, cy, r * 0.95f);
        sr.setColor(0.2f, 0.85f, 1f, 0.20f);
        sr.circle(cx, cy, r * 0.80f);

        // 2. Heavy Chrono Brass Spool Outer Bezel with Gear Rim
        sr.setColor(0.85f, 0.65f, 0.22f, 1f);
        sr.circle(cx, cy, r * 0.76f);
        for (int g = 0; g < 12; g++) {
            float gAng = g * 30f;
            float gx = cx + MathUtils.cosDeg(gAng) * (r * 0.78f);
            float gy = cy + MathUtils.sinDeg(gAng) * (r * 0.78f);
            sr.circle(gx, gy, 2.2f);
        }

        // Inner dark void bevel
        sr.setColor(0.08f, 0.06f, 0.16f, 1f);
        sr.circle(cx, cy, r * 0.66f);

        // 3. Shimmering Tachyon Filament Thread (Interwoven Cyan & Violet spool winding)
        sr.setColor(0.3f, 0.95f, 1f, 0.95f);
        sr.circle(cx, cy, r * 0.56f);
        sr.setColor(0.75f, 0.25f, 0.95f, 1f);
        sr.circle(cx, cy, r * 0.44f);
        sr.setColor(0.25f, 0.85f, 0.95f, 1f);
        sr.circle(cx, cy, r * 0.32f);

        // 4. Central Space-Time Singularity Core
        sr.setColor(0.04f, 0.02f, 0.08f, 1f);
        sr.circle(cx, cy, r * 0.22f);
        // Glowing event horizon ring
        sr.setColor(1f, 0.92f, 0.45f, 1f);
        sr.circle(cx, cy, r * 0.12f);
        sr.setColor(Color.WHITE);
        sr.circle(cx, cy, r * 0.06f);

        // 4 Cardinal temporal warp rays
        sr.setColor(0.4f, 0.95f, 1f, 0.8f);
        sr.rectLine(cx - r * 0.26f, cy, cx + r * 0.26f, cy, 1.5f);
        sr.rectLine(cx, cy - r * 0.26f, cx, cy + r * 0.26f, 1.5f);
    }

    public static void drawThoiKhongHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // 1. Cosmic Void Singularity Aura
        sr.setColor(0.55f, 0.25f, 0.95f, 0.22f);
        sr.circle(cx, cy, r * 0.95f);

        float shankX = cx - r * 0.22f;
        float topY = cy + r * 0.58f;
        float botY = cy - r * 0.38f;
        float bendX = cx + r * 0.42f;

        // 2. Void Obsidian Hook Body with Cosmic Fracture
        sr.setColor(0.14f, 0.12f, 0.24f, 1f); // Dark matter obsidian
        sr.rectLine(shankX, topY, shankX, botY, Math.max(4.5f, r * 0.16f));
        sr.rectLine(shankX, botY, bendX, botY, Math.max(4.5f, r * 0.16f));
        sr.rectLine(bendX, botY, bendX, cy + r * 0.25f, Math.max(4.2f, r * 0.15f));

        // 3. Luminous Cyan Chrono Runes along shank
        sr.setColor(0.35f, 0.95f, 1f, 0.95f);
        sr.rectLine(shankX, topY - r * 0.15f, shankX, botY + r * 0.15f, 1.8f);
        // Rune tick marks
        sr.circle(shankX, cy + r * 0.25f, 2f);
        sr.circle(shankX, cy, 2f);
        sr.circle(shankX, cy - r * 0.25f, 2f);

        // 4. Dimensional Rift Crescent Barb (Cuts through reality)
        sr.setColor(0.85f, 0.35f, 1f, 1f); // Amethyst void blade
        sr.triangle(bendX, cy + r * 0.25f, bendX + r * 0.32f, cy + r * 0.48f, bendX - r * 0.08f, cy + r * 0.15f);
        // Razor starlight edge
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.rectLine(bendX, cy + r * 0.25f, bendX + r * 0.32f, cy + r * 0.48f, 1.6f);

        // 5. Crown Eyelet with Chrono Orb & Hanging Pendulum
        sr.setColor(0.95f, 0.75f, 0.25f, 1f); // Brass eyelet
        sr.circle(shankX, topY, r * 0.24f);
        sr.setColor(0.06f, 0.08f, 0.18f, 1f);
        sr.circle(shankX, topY, r * 0.13f);
        // Glowing temporal jewel in eyelet
        sr.setColor(0.4f, 0.95f, 1f, 1f);
        sr.circle(shankX, topY, r * 0.08f);

        // Hanging stardust pendulum charm
        sr.setColor(0.95f, 0.75f, 0.25f, 0.9f);
        sr.rectLine(shankX, topY, shankX - r * 0.28f, topY - r * 0.25f, 1.8f);
        sr.setColor(0.85f, 0.35f, 1f, 1f);
        sr.circle(shankX - r * 0.28f, topY - r * 0.25f, 3.5f);
    }

    // =========================================================================
    // 9. THẦN THOẠI SET
    // =========================================================================
    private static void drawThanThoaiRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Solar Deity sceptre radiating 8 holy sunbeams
        sr.setColor(1f, 0.85f, 0.25f, 0.35f);
        sr.circle(cx + r * 0.5f, cy + r * 0.5f, r * 0.45f);
        // Golden staff
        sr.setColor(1f, 0.88f, 0.30f, 1f);
        sr.rectLine(cx - r * 0.7f, cy - r * 0.7f, cx + r * 0.45f, cy + r * 0.45f, 4.5f);
        // Solar disc
        float sx = cx + r * 0.5f, sy = cy + r * 0.5f;
        sr.setColor(1f, 0.95f, 0.45f, 1f);
        sr.circle(sx, sy, r * 0.22f);
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(sx, sy, r * 0.12f);
        // 4 Cardinal sun rays
        sr.setColor(1f, 0.85f, 0.2f, 1f);
        sr.rectLine(sx, sy + 7f, sx, sy + 13f, 2f);
        sr.rectLine(sx, sy - 7f, sx, sy - 13f, 2f);
        sr.rectLine(sx + 7f, sy, sx + 13f, sy, 2f);
        sr.rectLine(sx - 7f, sy, sx - 13f, sy, 2f);
    }

    private static void drawThanThoaiLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Divine golden starlight filament spool
        sr.setColor(0.95f, 0.75f, 0.20f, 1f);
        sr.circle(cx, cy, r * 0.75f);
        sr.setColor(1f, 0.92f, 0.45f, 1f);
        sr.circle(cx, cy, r * 0.55f);
        sr.setColor(0.45f, 0.28f, 0.08f, 1f);
        sr.circle(cx, cy, r * 0.32f);
        // Holy divine star in center
        sr.setColor(1f, 1f, 1f, 1f);
        sr.rect(cx - 3f, cy - 3f, 3f, 3f, 6f, 6f, 1f, 1f, 45f);
    }

    private static void drawThanThoaiHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Winged Archangel Celestial Hook
        sr.setColor(1f, 0.85f, 0.25f, 1f);
        sr.rectLine(cx - r * 0.2f, cy + r * 0.6f, cx - r * 0.2f, cy - r * 0.35f, 4.5f);
        sr.rectLine(cx - r * 0.2f, cy - r * 0.35f, cx + r * 0.4f, cy - r * 0.35f, 4.5f);
        sr.rectLine(cx + r * 0.4f, cy - r * 0.35f, cx + r * 0.4f, cy + r * 0.25f, 4.5f);
        // Angelic wings on the shank
        sr.setColor(1f, 0.95f, 0.80f, 1f);
        sr.triangle(cx - r * 0.2f, cy + r * 0.2f, cx - r * 0.65f, cy + r * 0.5f, cx - r * 0.2f, cy + r * 0.4f);
        sr.triangle(cx - r * 0.2f, cy + r * 0.1f, cx - r * 0.55f, cy + r * 0.3f, cx - r * 0.2f, cy + r * 0.25f);
        // Golden hook spear tip
        sr.setColor(1f, 1f, 0.7f, 1f);
        sr.triangle(cx + r * 0.4f, cy + r * 0.25f, cx + r * 0.2f, cy + r * 0.1f, cx + r * 0.55f, cy + r * 0.1f);
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
    // 11. THẦN KHÍ LẠC HỒNG SET
    // =========================================================================
    public static void drawLacHongRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Dong Son bronze drum totem staff crested with majestic Chim Lac bird
        sr.setColor(1f, 0.75f, 0.2f, 0.35f);
        sr.circle(cx + r * 0.5f, cy + r * 0.5f, r * 0.45f);
        // Bronze drum totem staff
        sr.setColor(0.80f, 0.55f, 0.18f, 1f);
        sr.rectLine(cx - r * 0.7f, cy - r * 0.7f, cx + r * 0.45f, cy + r * 0.45f, 5f);
        // Dong Son sun concentric rings
        sr.setColor(1f, 0.85f, 0.25f, 1f);
        sr.circle(cx - r * 0.1f, cy - r * 0.1f, 4f);
        sr.circle(cx + r * 0.2f, cy + r * 0.2f, 4f);
        // Chim Lac bird in full flight atop staff
        float bx = cx + r * 0.65f, by = cy + r * 0.65f;
        sr.setColor(1f, 0.92f, 0.35f, 1f);
        // Beak & Head
        sr.triangle(bx, by, bx + 10f, by + 4f, bx + 2f, by + 8f);
        // Wings spreading
        sr.triangle(bx, by, bx - 6f, by + 12f, bx + 2f, by + 8f);
        sr.triangle(bx, by, bx - 8f, by - 6f, bx - 2f, by);
    }

    public static void drawLacHongLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Sacred Dragon Sinew wound on imperial Dong Son bronze spool
        sr.setColor(0.85f, 0.62f, 0.22f, 1f);
        sr.circle(cx, cy, r * 0.75f);
        sr.setColor(1f, 0.82f, 0.25f, 1f);
        sr.circle(cx, cy, r * 0.58f);
        sr.setColor(0.55f, 0.25f, 0.10f, 1f);
        sr.circle(cx, cy, r * 0.38f);
        // Divine dragon pearl in center
        sr.setColor(1f, 0.3f, 0.35f, 1f);
        sr.circle(cx, cy, r * 0.20f);
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx, cy, r * 0.08f);
    }

    public static void drawLacHongHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Celestial Au Co Lotus Hook cradling glowing ocean pearl
        sr.setColor(1f, 0.82f, 0.25f, 1f);
        sr.rectLine(cx - r * 0.2f, cy + r * 0.55f, cx - r * 0.2f, cy - r * 0.35f, 4.5f);
        sr.rectLine(cx - r * 0.2f, cy - r * 0.35f, cx + r * 0.4f, cy - r * 0.35f, 4.5f);
        sr.rectLine(cx + r * 0.4f, cy - r * 0.35f, cx + r * 0.4f, cy + r * 0.25f, 4.5f);
        // Pink lotus flower petal cradle
        sr.setColor(0.95f, 0.45f, 0.65f, 1f);
        sr.triangle(cx - r * 0.2f, cy + r * 0.55f, cx - r * 0.45f, cy + r * 0.8f, cx - r * 0.2f, cy + r * 0.75f);
        sr.triangle(cx - r * 0.2f, cy + r * 0.55f, cx + r * 0.05f, cy + r * 0.8f, cx - r * 0.2f, cy + r * 0.75f);
        // Sacred Pearl in center
        sr.setColor(1f, 0.98f, 0.88f, 1f);
        sr.circle(cx - r * 0.2f, cy + r * 0.62f, r * 0.15f);
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
