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
    private static void drawBangGiaRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Glacial crystal ice spear staff
        sr.setColor(0.35f, 0.75f, 0.95f, 0.4f);
        sr.circle(cx, cy, r * 0.8f);
        // Sharp ice crystal shaft
        sr.setColor(0.75f, 0.92f, 1f, 1f);
        sr.rectLine(cx - r * 0.7f, cy - r * 0.7f, cx + r * 0.7f, cy + r * 0.7f, 4f);
        // Icicle spikes
        sr.setColor(0.55f, 0.85f, 1f, 1f);
        sr.triangle(cx, cy, cx - r * 0.25f, cy + r * 0.25f, cx + r * 0.1f, cy + r * 0.1f);
        sr.triangle(cx + r * 0.3f, cy + r * 0.3f, cx + r * 0.15f, cy + r * 0.55f, cx + r * 0.4f, cy + r * 0.4f);
        // Snowflake crystal star tip
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + r * 0.7f, cy + r * 0.7f, r * 0.18f);
        sr.rect(cx + r * 0.7f - 2f, cy + r * 0.7f - 6f, 4f, 12f);
        sr.rect(cx + r * 0.7f - 6f, cy + r * 0.7f - 2f, 12f, 4f);
    }

    private static void drawBangGiaLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Frosted ice-blue crystal spool with snowflakes
        sr.setColor(0.40f, 0.80f, 1f, 1f);
        sr.circle(cx, cy, r * 0.75f);
        sr.setColor(0.85f, 0.95f, 1f, 1f);
        sr.circle(cx, cy, r * 0.55f);
        sr.setColor(0.15f, 0.35f, 0.65f, 1f);
        sr.circle(cx, cy, r * 0.32f);
        // Ice crystal pips
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(cx - r * 0.42f, cy, 2.5f);
        sr.circle(cx + r * 0.42f, cy, 2.5f);
        sr.circle(cx, cy - r * 0.42f, 2.5f);
        sr.circle(cx, cy + r * 0.42f, 2.5f);
    }

    private static void drawBangGiaHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Crystalline ice hook with snowflake eyelet
        sr.setColor(0.65f, 0.90f, 1f, 1f);
        sr.rectLine(cx - r * 0.2f, cy + r * 0.55f, cx - r * 0.2f, cy - r * 0.35f, 3.8f);
        sr.rectLine(cx - r * 0.2f, cy - r * 0.35f, cx + r * 0.4f, cy - r * 0.35f, 3.8f);
        sr.rectLine(cx + r * 0.4f, cy - r * 0.35f, cx + r * 0.4f, cy + r * 0.25f, 3.8f);
        // Sharp frozen icicle barb
        sr.setColor(0.9f, 0.98f, 1f, 1f);
        sr.triangle(cx + r * 0.4f, cy + r * 0.25f, cx + r * 0.2f, cy + r * 0.1f, cx + r * 0.55f, cy + r * 0.1f);
        // Snowflake eyelet at top
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx - r * 0.2f, cy + r * 0.6f, r * 0.2f);
        sr.setColor(0.2f, 0.4f, 0.6f, 1f);
        sr.circle(cx - r * 0.2f, cy + r * 0.6f, r * 0.08f);
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
    // 5. HẢI TẶC SET
    // =========================================================================
    private static void drawHaiTacRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Galleon timber mast spar with pirate pulley and flag
        sr.setColor(0.32f, 0.20f, 0.12f, 1f);
        sr.rectLine(cx - r * 0.7f, cy - r * 0.7f, cx + r * 0.7f, cy + r * 0.7f, 5f);
        // Brass pulley block
        sr.setColor(0.85f, 0.65f, 0.20f, 1f);
        sr.circle(cx + r * 0.4f, cy + r * 0.4f, r * 0.22f);
        sr.setColor(0.18f, 0.12f, 0.08f, 1f);
        sr.circle(cx + r * 0.4f, cy + r * 0.4f, r * 0.08f);
        // Black Jolly Roger pennant flag
        sr.setColor(0.12f, 0.10f, 0.12f, 1f);
        sr.triangle(cx + r * 0.65f, cy + r * 0.65f, cx + r * 0.95f, cy + r * 0.45f, cx + r * 0.65f, cy + r * 0.35f);
        sr.setColor(0.9f, 0.9f, 0.9f, 1f);
        sr.circle(cx + r * 0.75f, cy + r * 0.48f, 2f);
    }

    private static void drawHaiTacLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Heavy iron anchor chain coiled on rustic nautical wheel
        sr.setColor(0.28f, 0.18f, 0.12f, 1f);
        sr.circle(cx, cy, r * 0.75f);
        sr.setColor(0.65f, 0.65f, 0.70f, 1f);
        // Iron chain links
        for (int a = 0; a < 6; a++) {
            float ang = a * 60f * MathUtils.degreesToRadians;
            sr.circle(cx + r * 0.52f * MathUtils.cos(ang), cy + r * 0.52f * MathUtils.sin(ang), 3.5f);
        }
        sr.setColor(0.15f, 0.15f, 0.18f, 1f);
        sr.circle(cx, cy, r * 0.32f);
        // Skull center dot
        sr.setColor(0.9f, 0.85f, 0.80f, 1f);
        sr.circle(cx, cy + 1f, 3.5f);
    }

    private static void drawHaiTacHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Brutal steel reaper scythe hook
        sr.setColor(0.25f, 0.25f, 0.30f, 1f);
        sr.rectLine(cx - r * 0.25f, cy + r * 0.6f, cx - r * 0.25f, cy - r * 0.35f, 4.5f);
        sr.rectLine(cx - r * 0.25f, cy - r * 0.35f, cx + r * 0.45f, cy - r * 0.35f, 4.5f);
        // Broad scythe blade curved upward
        sr.setColor(0.85f, 0.30f, 0.25f, 1f);
        sr.triangle(cx + r * 0.35f, cy - r * 0.35f, cx + r * 0.65f, cy + r * 0.45f, cx + r * 0.20f, cy + r * 0.1f);
        // Beveled steel edge
        sr.setColor(0.95f, 0.95f, 1f, 1f);
        sr.rectLine(cx + r * 0.25f, cy + r * 0.1f, cx + r * 0.65f, cy + r * 0.45f, 1.8f);
    }

    // =========================================================================
    // 6. VỰC THẲM SET
    // =========================================================================
    private static void drawVucThamRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Obsidian void staff with glowing purple demonic eye
        sr.setColor(0.4f, 0.1f, 0.5f, 0.35f);
        sr.circle(cx, cy, r * 0.8f);
        sr.setColor(0.12f, 0.08f, 0.16f, 1f);
        sr.rectLine(cx - r * 0.7f, cy - r * 0.7f, cx + r * 0.5f, cy + r * 0.5f, 4.5f);
        // Eldritch eye socket
        sr.setColor(0.65f, 0.15f, 0.85f, 1f);
        sr.circle(cx + r * 0.55f, cy + r * 0.55f, r * 0.28f);
        // Glowing void pupil
        sr.setColor(1f, 0.2f, 0.4f, 1f);
        sr.circle(cx + r * 0.55f, cy + r * 0.55f, r * 0.16f);
        sr.setColor(1f, 0.9f, 0.3f, 1f);
        sr.circle(cx + r * 0.55f, cy + r * 0.55f, r * 0.06f);
    }

    private static void drawVucThamLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Dark purple void tendril spool
        sr.setColor(0.20f, 0.08f, 0.30f, 1f);
        sr.circle(cx, cy, r * 0.75f);
        sr.setColor(0.65f, 0.20f, 0.85f, 0.9f);
        sr.circle(cx, cy, r * 0.55f);
        sr.setColor(0.08f, 0.04f, 0.12f, 1f);
        sr.circle(cx, cy, r * 0.32f);
        // Void core
        sr.setColor(0.95f, 0.25f, 0.65f, 1f);
        sr.circle(cx, cy, r * 0.16f);
    }

    private static void drawVucThamHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Triple-fanged shadow fiend claw hook
        sr.setColor(0.22f, 0.10f, 0.32f, 1f);
        sr.rectLine(cx - r * 0.2f, cy + r * 0.55f, cx - r * 0.2f, cy - r * 0.35f, 4.5f);
        sr.rectLine(cx - r * 0.2f, cy - r * 0.35f, cx + r * 0.4f, cy - r * 0.35f, 4.5f);
        // Purple demonic talon tips
        sr.setColor(0.85f, 0.20f, 0.75f, 1f);
        sr.triangle(cx + r * 0.35f, cy - r * 0.35f, cx + r * 0.65f, cy + r * 0.35f, cx + r * 0.3f, cy + r * 0.1f);
        sr.triangle(cx + r * 0.25f, cy - r * 0.35f, cx + r * 0.45f, cy + r * 0.45f, cx + r * 0.2f, cy + r * 0.1f);
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
