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
    // 4. ĐẠI DƯƠNG SET
    // =========================================================================
    private static void drawDaiDuongRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Deep sapphire trident scepter
        sr.setColor(0.12f, 0.35f, 0.75f, 1f);
        sr.rectLine(cx - r * 0.7f, cy - r * 0.7f, cx + r * 0.5f, cy + r * 0.5f, 4f);
        // Wave trident prongs
        sr.setColor(0.25f, 0.75f, 0.95f, 1f);
        sr.triangle(cx + r * 0.4f, cy + r * 0.4f, cx + r * 0.85f, cy + r * 0.85f, cx + r * 0.6f, cy + r * 0.85f);
        sr.triangle(cx + r * 0.4f, cy + r * 0.4f, cx + r * 0.45f, cy + r * 0.95f, cx + r * 0.65f, cy + r * 0.75f);
        sr.triangle(cx + r * 0.4f, cy + r * 0.4f, cx + r * 0.95f, cy + r * 0.45f, cx + r * 0.75f, cy + r * 0.65f);
        // Ocean pearl gem
        sr.setColor(0.85f, 0.98f, 1f, 1f);
        sr.circle(cx + r * 0.45f, cy + r * 0.45f, r * 0.16f);
    }

    private static void drawDaiDuongLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Glowing aqua fiber-optic glass spool
        sr.setColor(0.10f, 0.45f, 0.65f, 1f);
        sr.circle(cx, cy, r * 0.75f);
        sr.setColor(0.35f, 0.95f, 0.90f, 0.9f);
        sr.circle(cx, cy, r * 0.55f);
        sr.setColor(0.08f, 0.20f, 0.35f, 1f);
        sr.circle(cx, cy, r * 0.3f);
        // Glowing cyan center gem
        sr.setColor(0.6f, 1f, 0.95f, 1f);
        sr.circle(cx, cy, r * 0.15f);
    }

    private static void drawDaiDuongHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Leviathan tooth hook bound in kelp
        sr.setColor(0.20f, 0.85f, 0.80f, 1f);
        sr.rectLine(cx - r * 0.2f, cy + r * 0.6f, cx - r * 0.2f, cy - r * 0.3f, 4f);
        sr.rectLine(cx - r * 0.2f, cy - r * 0.3f, cx + r * 0.4f, cy - r * 0.3f, 4f);
        // Jagged tooth tip
        sr.triangle(cx + r * 0.35f, cy - r * 0.3f, cx + r * 0.55f, cy + r * 0.35f, cx + r * 0.25f, cy + r * 0.1f);
        // Sea kelp green band
        sr.setColor(0.15f, 0.55f, 0.25f, 1f);
        sr.rect(cx - r * 0.28f, cy + r * 0.1f, r * 0.2f, r * 0.15f);
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
    // 8. THỜI KHÔNG SET
    // =========================================================================
    private static void drawThoiKhongRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Chrono celestial hourglass staff with clockwork gear
        sr.setColor(0.4f, 0.2f, 0.8f, 0.3f);
        sr.circle(cx, cy, r * 0.85f);
        sr.setColor(0.85f, 0.75f, 0.35f, 1f);
        sr.rectLine(cx - r * 0.7f, cy - r * 0.7f, cx + r * 0.45f, cy + r * 0.45f, 3.5f);
        // Astrolabe ring
        float ax = cx + r * 0.55f, ay = cy + r * 0.55f;
        sr.setColor(0.35f, 0.65f, 0.95f, 1f);
        sr.circle(ax, ay, r * 0.32f);
        sr.setColor(0.08f, 0.10f, 0.20f, 1f);
        sr.circle(ax, ay, r * 0.22f);
        // Clock hands
        sr.setColor(1f, 0.88f, 0.35f, 1f);
        sr.rectLine(ax, ay, ax + 5f, ay + 6f, 1.8f);
        sr.rectLine(ax, ay, ax - 4f, ay + 2f, 1.8f);
    }

    private static void drawThoiKhongLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Cosmic purple wormhole spiral spool
        sr.setColor(0.35f, 0.15f, 0.65f, 1f);
        sr.circle(cx, cy, r * 0.75f);
        sr.setColor(0.75f, 0.40f, 1f, 0.9f);
        sr.circle(cx, cy, r * 0.55f);
        sr.setColor(0.10f, 0.05f, 0.25f, 1f);
        sr.circle(cx, cy, r * 0.35f);
        // Neon cyan temporal distortion core
        sr.setColor(0.4f, 0.95f, 1f, 1f);
        sr.circle(cx, cy, r * 0.18f);
    }

    private static void drawThoiKhongHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Void singularity black hole hook with cosmic accretion ring
        sr.setColor(0.55f, 0.35f, 0.95f, 0.4f);
        sr.circle(cx + r * 0.15f, cy, r * 0.65f);
        sr.setColor(0.20f, 0.15f, 0.40f, 1f);
        sr.rectLine(cx - r * 0.2f, cy + r * 0.55f, cx - r * 0.2f, cy - r * 0.35f, 4f);
        sr.rectLine(cx - r * 0.2f, cy - r * 0.35f, cx + r * 0.4f, cy - r * 0.35f, 4f);
        // Singularity void tip
        sr.setColor(0.85f, 0.45f, 1f, 1f);
        sr.triangle(cx + r * 0.35f, cy - r * 0.35f, cx + r * 0.6f, cy + r * 0.35f, cx + r * 0.25f, cy + r * 0.1f);
        // Glowing star spark in center
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + r * 0.1f, cy, 2.5f);
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
    private static void drawVeQuocRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Heroic bamboo battle-spear rod with fluttering red banner
        sr.setColor(0.65f, 0.55f, 0.22f, 1f);
        sr.rectLine(cx - r * 0.7f, cy - r * 0.7f, cx + r * 0.65f, cy + r * 0.65f, 4.5f);
        // Bronze spear tip
        sr.setColor(0.85f, 0.55f, 0.15f, 1f);
        sr.triangle(cx + r * 0.55f, cy + r * 0.55f, cx + r * 0.95f, cy + r * 0.95f, cx + r * 0.65f, cy + r * 0.95f);
        sr.triangle(cx + r * 0.55f, cy + r * 0.55f, cx + r * 0.95f, cy + r * 0.95f, cx + r * 0.95f, cy + r * 0.65f);
        // Fluttering crimson banner
        sr.setColor(0.90f, 0.18f, 0.20f, 1f);
        sr.triangle(cx + r * 0.5f, cy + r * 0.5f, cx + r * 0.85f, cy + r * 0.25f, cx + r * 0.5f, cy + r * 0.25f);
        // Gold star on banner
        sr.setColor(1f, 0.88f, 0.25f, 1f);
        sr.circle(cx + r * 0.62f, cy + r * 0.33f, 2.5f);
    }

    private static void drawVeQuocLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Braided tricolor red-and-gold defense cord on bronze spool
        sr.setColor(0.75f, 0.52f, 0.22f, 1f); // Bronze
        sr.circle(cx, cy, r * 0.75f);
        sr.setColor(0.85f, 0.18f, 0.20f, 1f); // Red cord
        sr.circle(cx, cy, r * 0.55f);
        sr.setColor(1f, 0.85f, 0.25f, 1f); // Gold cord core
        sr.circle(cx, cy, r * 0.35f);
        // Center bronze hub
        sr.setColor(0.45f, 0.28f, 0.12f, 1f);
        sr.circle(cx, cy, r * 0.18f);
    }

    private static void drawVeQuocHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        // Heroic forged steel hook crowned with brilliant golden 5-pointed star
        sr.setColor(0.85f, 0.55f, 0.20f, 1f);
        sr.rectLine(cx - r * 0.2f, cy + r * 0.55f, cx - r * 0.2f, cy - r * 0.35f, 4f);
        sr.rectLine(cx - r * 0.2f, cy - r * 0.35f, cx + r * 0.4f, cy - r * 0.35f, 4f);
        sr.rectLine(cx + r * 0.4f, cy - r * 0.35f, cx + r * 0.4f, cy + r * 0.25f, 4f);
        // Golden 5-pointed Star at top eyelet
        float sx = cx - r * 0.2f, sy = cy + r * 0.58f;
        sr.setColor(1f, 0.88f, 0.20f, 1f);
        sr.circle(sx, sy, r * 0.22f);
        sr.rect(sx - 3f, sy - 3f, 3f, 3f, 6f, 6f, 1f, 1f, 45f);
    }

    // =========================================================================
    // 11. THẦN KHÍ LẠC HỒNG SET
    // =========================================================================
    private static void drawLacHongRod(ShapeRenderer sr, float cx, float cy, float r, Color col) {
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

    private static void drawLacHongLine(ShapeRenderer sr, float cx, float cy, float r, Color col) {
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

    private static void drawLacHongHook(ShapeRenderer sr, float cx, float cy, float r, Color col) {
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
