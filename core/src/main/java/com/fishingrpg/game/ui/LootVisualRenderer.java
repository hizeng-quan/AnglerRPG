package com.fishingrpg.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.fishingrpg.game.loot.Catchable;
import com.fishingrpg.game.loot.CatchableType;
import com.fishingrpg.game.loot.LootItem;

/**
 * Dedicated renderer for unique vector illustrations of all non-fish loot:
 * - Trash items (Ủng Cao Su Cũ, Chai Nhựa, Lốp Xe, Túi Rác)
 * - Treasure items (Bình Cổ, Nhẫn Vàng, Rương Chìm, Kiếm Cổ, Sách Kỹ Năng)
 * - Consumables & Quick Slot action bar item icons
 */
public class LootVisualRenderer {

    /**
     * Draw catchable loot (trash, treasure, consumable) at (cx, cy).
     */
    public static void drawLoot(ShapeRenderer sr, String name, CatchableType type, float cx, float cy, float scale, float timer) {
        if (name == null) name = "";

        if (name.equals("Ủng Cao Su Cũ")) {
            drawBoot(sr, cx, cy, scale);
        } else if (name.equals("Chai Nhựa")) {
            drawBottle(sr, cx, cy, scale, timer);
        } else if (name.equals("Lốp Xe")) {
            drawTire(sr, cx, cy, scale);
        } else if (name.equals("Túi Rác")) {
            drawTrashBag(sr, cx, cy, scale);
        } else if (name.equals("Bình Cổ")) {
            drawAncientVase(sr, cx, cy, scale, timer);
        } else if (name.equals("Nhẫn Vàng")) {
            drawGoldRing(sr, cx, cy, scale, timer);
        } else if (name.equals("Rương Chìm")) {
            drawSunkenChest(sr, cx, cy, scale, timer);
        } else if (name.equals("Kiếm Cổ")) {
            drawAncientSword(sr, cx, cy, scale, timer);
        } else if (name.startsWith("Sách") || name.equals("Sách Thông Thái")) {
            drawSpellBook(sr, cx, cy, scale, timer, name);
        } else if (name.equals("Mồi Thơm")) {
            drawAromaBait(sr, cx, cy, scale);
        } else if (name.equals("Mồi Rủi Ro")) {
            drawRiskyBait(sr, cx, cy, scale, timer);
        } else if (name.equals("Thuốc Hồi Stamina")) {
            drawStaminaPotion(sr, cx, cy, scale);
        } else if (name.equals("Nước Tăng Lực")) {
            drawEnergyDrink(sr, cx, cy, scale);
        } else if (name.equals("Nước Bức Phá") || name.equals("Nước Bứt Phá")) {
            drawOverdrivePotion(sr, cx, cy, scale);
        } else if (name.equals("Dây Câu Tốt")) {
            drawLineSpool(sr, cx, cy, scale);
        } else if (name.equals("Bánh Mì")) {
            drawBread(sr, cx, cy, scale);
        } else if (name.equals("Bùa Hoàng Kim")) {
            drawGoldTalisman(sr, cx, cy, scale, timer);
        } else if (name.equals("Lưới Đánh Cá")) {
            drawFishingNet(sr, cx, cy, scale);
        } else if (name.equals("Rương Ma Quái")) {
            drawSunkenChest(sr, cx, cy, scale, timer);
        } else if (name.equals("Kẹo Trick-or-Treat")) {
            drawHalloweenCandy(sr, cx, cy, scale, timer);
        } else if (name.equals("Đèn Lồng Bí Ngô")) {
            drawPumpkinLantern(sr, cx, cy, scale, timer);
        } else if (name.equals("Độc Dược Ma Quái") || name.equals("Nước Phù Thủy Ma Quái")) {
            drawGhostPotion(sr, cx, cy, scale, timer);
        } else if (name.equals("Bùa Huyết Nguyệt")) {
            drawBloodMoonTalisman(sr, cx, cy, scale, timer);
        } else if (name.equals("Nến Hồn Ma Bảo Hộ")) {
            drawGhostCandle(sr, cx, cy, scale, timer);
        } else if (name.equals("Bình Xịt Đóng Băng")) {
            drawCryoSpray(sr, cx, cy, scale, timer);
        } else if (name.equals("Trà Thảo Mộc Định Thần") || name.equals("Trà Thảo Mộc")) {
            drawHerbalTea(sr, cx, cy, scale, timer);
        } else if (name.equals("Đá Mài Lưỡi Câu")) {
            drawWhetstone(sr, cx, cy, scale, timer);
        } else if (name.equals("Nam Châm Kho Báu")) {
            drawTreasureMagnet(sr, cx, cy, scale, timer);
        } else if (name.equals("Kính Nhìn Xuyên Nước")) {
            drawSonarGoggles(sr, cx, cy, scale, timer);
        } else if (type == CatchableType.TRASH) {
            drawGenericTrash(sr, cx, cy, scale);
        } else if (type == CatchableType.TREASURE) {
            drawSunkenChest(sr, cx, cy, scale, timer);
        } else {
            drawGenericPotion(sr, cx, cy, scale);
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

    private static void fillEllipse(ShapeRenderer sr, float cx, float cy, float s, float svgX, float svgY, float rx, float ry) {
        sr.ellipse(sx(cx, svgX, s) - rx * s, sy(cy, svgY, s) - ry * s, rx * 2f * s, ry * 2f * s);
    }

    // =========================================================================
    // TRASH & TREASURE LOOT ITEMS (Step 1.5)
    // =========================================================================

    public static void drawBaitJar(ShapeRenderer sr, float cx, float cy, float s, boolean isRisky) {
        if (isRisky) {
            drawRiskyBait(sr, cx, cy, s, 0f);
        } else {
            drawAromaBait(sr, cx, cy, s);
        }
    }

    public static void drawSpellBook(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        drawSpellBook(sr, cx, cy, s, timer, "");
    }

    public static void drawSpellBook(ShapeRenderer sr, float cx, float cy, float s, float timer, String bookName) {
        if (bookName == null) bookName = "";
        if (bookName.contains("Thông Thái") || bookName.contains("Bị Động") ||
            bookName.contains("Mắt Đại Bàng") || bookName.contains("Thuần Hóa")) {
            drawPassiveSpellBook(sr, cx, cy, s, timer);
        } else {
            drawActiveSpellBook(sr, cx, cy, s, timer);
        }
    }

    private static void drawGenericTrash(ShapeRenderer sr, float cx, float cy, float s) {
        drawBoot(sr, cx, cy, s);
    }

    public static void drawBoot(ShapeRenderer sr, float cx, float cy, float s) {
        // --- LAYER: background_glow ---
        sr.setColor(0.28f, 0.33f, 0.41f, 0.25f);
        fillCircle(sr, cx, cy, s, 64.0f, 68.0f, 42.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.35f);
        fillEllipse(sr, cx, cy, s, 64.0f, 112.0f, 34.0f, 8.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.28f, 0.33f, 0.41f, 1.00f);
        fillQuad(sr, cx, cy, s, 42.0f, 24.0f, 70.0f, 24.0f, 70.0f, 76.0f, 42.0f, 76.0f);
        sr.setColor(0.12f, 0.16f, 0.23f, 1.00f);
        fillQuad(sr, cx, cy, s, 40.0f, 20.0f, 72.0f, 20.0f, 72.0f, 28.0f, 40.0f, 28.0f);
        sr.setColor(0.20f, 0.25f, 0.33f, 1.00f);
        fillCircle(sr, cx, cy, s, 56.0f, 74.0f, 24.0f);
        fillCircle(sr, cx, cy, s, 82.0f, 80.0f, 18.0f);
        fillQuad(sr, cx, cy, s, 42.0f, 70.0f, 88.0f, 70.0f, 88.0f, 94.0f, 42.0f, 94.0f);
        sr.setColor(0.06f, 0.09f, 0.16f, 1.00f);
        fillQuad(sr, cx, cy, s, 38.0f, 94.0f, 94.0f, 94.0f, 94.0f, 104.0f, 38.0f, 104.0f);
        sr.setColor(0.01f, 0.02f, 0.09f, 1.00f);
        fillQuad(sr, cx, cy, s, 42.0f, 100.0f, 48.0f, 100.0f, 48.0f, 105.0f, 42.0f, 105.0f);
        fillQuad(sr, cx, cy, s, 52.0f, 100.0f, 58.0f, 100.0f, 58.0f, 105.0f, 52.0f, 105.0f);
        fillQuad(sr, cx, cy, s, 62.0f, 100.0f, 68.0f, 100.0f, 68.0f, 105.0f, 62.0f, 105.0f);
        fillQuad(sr, cx, cy, s, 72.0f, 100.0f, 78.0f, 100.0f, 78.0f, 105.0f, 72.0f, 105.0f);
        fillQuad(sr, cx, cy, s, 82.0f, 100.0f, 88.0f, 100.0f, 88.0f, 105.0f, 82.0f, 105.0f);
        // --- LAYER: details_and_relief ---
        sr.setColor(0.47f, 0.21f, 0.06f, 1.00f);
        fillQuad(sr, cx, cy, s, 46.0f, 44.0f, 62.0f, 44.0f, 62.0f, 58.0f, 46.0f, 58.0f);
        sr.setColor(0.85f, 0.47f, 0.02f, 1.00f);
        drawLine(sr, cx, cy, s, 48.0f, 46.0f, 60.0f, 46.0f, 1.2f);
        drawLine(sr, cx, cy, s, 48.0f, 56.0f, 60.0f, 56.0f, 1.2f);
        drawLine(sr, cx, cy, s, 48.0f, 46.0f, 48.0f, 56.0f, 1.2f);
        drawLine(sr, cx, cy, s, 60.0f, 46.0f, 60.0f, 56.0f, 1.2f);
        sr.setColor(0.27f, 0.10f, 0.01f, 1.00f);
        fillCircle(sr, cx, cy, s, 50.0f, 84.0f, 2.0f);
        fillCircle(sr, cx, cy, s, 70.0f, 88.0f, 2.5f);
        fillCircle(sr, cx, cy, s, 78.0f, 74.0f, 1.5f);
        sr.setColor(0.22f, 0.74f, 0.97f, 1.00f);
        fillCircle(sr, cx, cy, s, 94.0f, 98.0f, 3.5f);
        fillTri(sr, cx, cy, s, 94.0f, 93.0f, 91.0f, 98.0f, 97.0f, 98.0f);
        // --- LAYER: specular_highlights ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.60f);
        drawLine(sr, cx, cy, s, 45.0f, 26.0f, 45.0f, 70.0f, 2.5f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.70f);
        drawLine(sr, cx, cy, s, 72.0f, 74.0f, 77.1f, 75.0f, 2.0f);
        drawLine(sr, cx, cy, s, 77.1f, 75.0f, 81.3f, 76.6f, 2.0f);
        drawLine(sr, cx, cy, s, 81.3f, 76.6f, 84.5f, 78.6f, 2.0f);
        drawLine(sr, cx, cy, s, 84.5f, 78.6f, 86.7f, 81.0f, 2.0f);
        drawLine(sr, cx, cy, s, 86.7f, 81.0f, 88.0f, 84.0f, 2.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 93.0f, 97.0f, 1.2f);
    }

    public static void drawBottle(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // --- LAYER: background_glow ---
        sr.setColor(0.01f, 0.52f, 0.78f, 0.25f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 42.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.30f);
        fillEllipse(sr, cx, cy, s, 64.0f, 112.0f, 26.0f, 7.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.49f, 0.83f, 0.99f, 0.75f);
        fillQuad(sr, cx, cy, s, 48.0f, 44.0f, 80.0f, 44.0f, 80.0f, 102.0f, 48.0f, 102.0f);
        sr.setColor(0.01f, 0.52f, 0.78f, 0.60f);
        fillEllipse(sr, cx, cy, s, 64.0f, 102.0f, 16.0f, 5.0f);
        sr.setColor(0.49f, 0.83f, 0.99f, 0.75f);
        fillQuad(sr, cx, cy, s, 48.0f, 44.0f, 80.0f, 44.0f, 72.0f, 26.0f, 56.0f, 26.0f);
        sr.setColor(0.22f, 0.74f, 0.97f, 0.80f);
        fillQuad(sr, cx, cy, s, 58.0f, 18.0f, 70.0f, 18.0f, 70.0f, 28.0f, 58.0f, 28.0f);
        sr.setColor(0.11f, 0.31f, 0.85f, 1.00f);
        fillQuad(sr, cx, cy, s, 56.0f, 12.0f, 72.0f, 12.0f, 72.0f, 20.0f, 56.0f, 20.0f);
        sr.setColor(0.15f, 0.39f, 0.92f, 1.00f);
        fillQuad(sr, cx, cy, s, 55.0f, 10.0f, 73.0f, 10.0f, 73.0f, 13.0f, 55.0f, 13.0f);
        // --- LAYER: details_and_relief ---
        sr.setColor(0.08f, 0.50f, 0.24f, 0.75f);
        fillQuad(sr, cx, cy, s, 50.0f, 74.0f, 78.0f, 74.0f, 78.0f, 100.0f, 50.0f, 100.0f);
        sr.setColor(0.09f, 0.40f, 0.20f, 0.80f);
        fillEllipse(sr, cx, cy, s, 64.0f, 74.0f, 14.0f, 3.0f);
        sr.setColor(0.97f, 0.98f, 0.99f, 1.00f);
        fillQuad(sr, cx, cy, s, 48.0f, 54.0f, 78.0f, 56.0f, 76.0f, 68.0f, 48.0f, 66.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        drawLine(sr, cx, cy, s, 50.0f, 60.0f, 74.0f, 62.0f, 2.0f);
        sr.setColor(0.73f, 0.90f, 0.99f, 1.00f);
        fillCircle(sr, cx, cy, s, 58.0f, 86.0f, 2.0f);
        fillCircle(sr, cx, cy, s, 70.0f, 92.0f, 1.5f);
        // --- LAYER: specular_highlights ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.85f);
        drawLine(sr, cx, cy, s, 52.0f, 46.0f, 52.0f, 100.0f, 2.5f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.50f);
        drawLine(sr, cx, cy, s, 56.0f, 48.0f, 56.0f, 98.0f, 1.0f);
        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 58.0f, 12.0f, 68.0f, 12.0f, 1.5f);
    }

    public static void drawTire(ShapeRenderer sr, float cx, float cy, float s) {
        // --- LAYER: background_glow ---
        sr.setColor(0.20f, 0.25f, 0.33f, 0.25f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 44.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.40f);
        fillEllipse(sr, cx, cy, s, 64.0f, 112.0f, 30.0f, 8.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.12f, 0.16f, 0.23f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 38.0f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 34.0f);
        sr.setColor(0.06f, 0.09f, 0.16f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 20.0f);
        sr.setColor(0.01f, 0.17f, 0.13f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 16.0f);
        // --- LAYER: details_and_relief ---
        sr.setColor(0.01f, 0.02f, 0.09f, 1.00f);
        fillQuad(sr, cx, cy, s, 62.0f, 26.0f, 66.0f, 26.0f, 66.0f, 32.0f, 62.0f, 32.0f);
        fillQuad(sr, cx, cy, s, 62.0f, 96.0f, 66.0f, 96.0f, 66.0f, 102.0f, 62.0f, 102.0f);
        fillQuad(sr, cx, cy, s, 26.0f, 62.0f, 32.0f, 62.0f, 32.0f, 66.0f, 26.0f, 66.0f);
        fillQuad(sr, cx, cy, s, 96.0f, 62.0f, 102.0f, 62.0f, 102.0f, 66.0f, 96.0f, 66.0f);
        fillQuad(sr, cx, cy, s, 37.0f, 37.0f, 42.0f, 37.0f, 42.0f, 42.0f, 37.0f, 42.0f);
        fillQuad(sr, cx, cy, s, 86.0f, 37.0f, 91.0f, 37.0f, 91.0f, 42.0f, 86.0f, 42.0f);
        fillQuad(sr, cx, cy, s, 37.0f, 86.0f, 42.0f, 86.0f, 42.0f, 91.0f, 37.0f, 91.0f);
        fillQuad(sr, cx, cy, s, 86.0f, 86.0f, 91.0f, 86.0f, 91.0f, 91.0f, 86.0f, 91.0f);
        sr.setColor(0.09f, 0.64f, 0.29f, 1.00f);
        drawLine(sr, cx, cy, s, 46.0f, 32.0f, 43.6f, 26.9f, 3.5f);
        drawLine(sr, cx, cy, s, 43.6f, 26.9f, 41.2f, 22.7f, 3.5f);
        drawLine(sr, cx, cy, s, 41.2f, 22.7f, 38.8f, 19.5f, 3.5f);
        drawLine(sr, cx, cy, s, 38.8f, 19.5f, 36.4f, 17.3f, 3.5f);
        drawLine(sr, cx, cy, s, 36.4f, 17.3f, 34.0f, 16.0f, 3.5f);
        sr.setColor(0.08f, 0.50f, 0.24f, 1.00f);
        drawLine(sr, cx, cy, s, 84.0f, 84.0f, 88.6f, 89.0f, 3.0f);
        drawLine(sr, cx, cy, s, 88.6f, 89.0f, 92.6f, 92.6f, 3.0f);
        drawLine(sr, cx, cy, s, 92.6f, 92.6f, 96.2f, 95.0f, 3.0f);
        drawLine(sr, cx, cy, s, 96.2f, 95.0f, 99.4f, 96.2f, 3.0f);
        drawLine(sr, cx, cy, s, 99.4f, 96.2f, 102.0f, 96.0f, 3.0f);
        sr.setColor(0.13f, 0.77f, 0.37f, 1.00f);
        fillCircle(sr, cx, cy, s, 34.0f, 16.0f, 3.0f);
        fillCircle(sr, cx, cy, s, 102.0f, 96.0f, 2.5f);
        // --- LAYER: specular_highlights ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.60f);
        drawLine(sr, cx, cy, s, 38.0f, 52.0f, 40.3f, 49.7f, 2.5f);
        drawLine(sr, cx, cy, s, 40.3f, 49.7f, 42.7f, 47.3f, 2.5f);
        drawLine(sr, cx, cy, s, 42.7f, 47.3f, 45.0f, 45.0f, 2.5f);
        drawLine(sr, cx, cy, s, 45.0f, 45.0f, 47.3f, 42.7f, 2.5f);
        drawLine(sr, cx, cy, s, 47.3f, 42.7f, 49.7f, 40.3f, 2.5f);
        drawLine(sr, cx, cy, s, 49.7f, 40.3f, 52.0f, 38.0f, 2.5f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.70f);
        drawLine(sr, cx, cy, s, 50.0f, 64.0f, 52.3f, 61.7f, 1.5f);
        drawLine(sr, cx, cy, s, 52.3f, 61.7f, 54.7f, 59.3f, 1.5f);
        drawLine(sr, cx, cy, s, 54.7f, 59.3f, 57.0f, 57.0f, 1.5f);
        drawLine(sr, cx, cy, s, 57.0f, 57.0f, 59.3f, 54.7f, 1.5f);
        drawLine(sr, cx, cy, s, 59.3f, 54.7f, 61.7f, 52.3f, 1.5f);
        drawLine(sr, cx, cy, s, 61.7f, 52.3f, 64.0f, 50.0f, 1.5f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 37.0f, 22.0f, 1.2f);
    }

    public static void drawTrashBag(ShapeRenderer sr, float cx, float cy, float s) {
        // --- LAYER: background_glow ---
        sr.setColor(0.12f, 0.16f, 0.23f, 0.25f);
        fillCircle(sr, cx, cy, s, 64.0f, 68.0f, 42.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.40f);
        fillEllipse(sr, cx, cy, s, 64.0f, 112.0f, 32.0f, 8.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.20f, 0.25f, 0.33f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 74.0f, 30.0f);
        fillCircle(sr, cx, cy, s, 48.0f, 78.0f, 22.0f);
        fillCircle(sr, cx, cy, s, 80.0f, 78.0f, 22.0f);
        sr.setColor(0.12f, 0.16f, 0.23f, 1.00f);
        fillQuad(sr, cx, cy, s, 54.0f, 40.0f, 74.0f, 40.0f, 74.0f, 52.0f, 54.0f, 52.0f);
        sr.setColor(0.20f, 0.25f, 0.33f, 1.00f);
        fillTri(sr, cx, cy, s, 56.0f, 42.0f, 42.0f, 24.0f, 50.0f, 38.0f);
        fillTri(sr, cx, cy, s, 72.0f, 42.0f, 86.0f, 24.0f, 78.0f, 38.0f);
        // --- LAYER: details_and_relief ---
        sr.setColor(0.06f, 0.09f, 0.16f, 1.00f);
        drawLine(sr, cx, cy, s, 46.0f, 64.0f, 53.2f, 68.5f, 3.0f);
        drawLine(sr, cx, cy, s, 53.2f, 68.5f, 60.4f, 70.7f, 3.0f);
        drawLine(sr, cx, cy, s, 60.4f, 70.7f, 67.6f, 70.7f, 3.0f);
        drawLine(sr, cx, cy, s, 67.6f, 70.7f, 74.8f, 68.5f, 3.0f);
        drawLine(sr, cx, cy, s, 74.8f, 68.5f, 82.0f, 64.0f, 3.0f);
        drawLine(sr, cx, cy, s, 40.0f, 82.0f, 49.6f, 86.5f, 2.5f);
        drawLine(sr, cx, cy, s, 49.6f, 86.5f, 59.2f, 88.7f, 2.5f);
        drawLine(sr, cx, cy, s, 59.2f, 88.7f, 68.8f, 88.7f, 2.5f);
        drawLine(sr, cx, cy, s, 68.8f, 88.7f, 78.4f, 86.5f, 2.5f);
        drawLine(sr, cx, cy, s, 78.4f, 86.5f, 88.0f, 82.0f, 2.5f);
        sr.setColor(0.94f, 0.27f, 0.27f, 1.00f);
        drawLine(sr, cx, cy, s, 54.0f, 46.0f, 58.0f, 47.3f, 2.5f);
        drawLine(sr, cx, cy, s, 58.0f, 47.3f, 62.0f, 47.9f, 2.5f);
        drawLine(sr, cx, cy, s, 62.0f, 47.9f, 66.0f, 47.9f, 2.5f);
        drawLine(sr, cx, cy, s, 66.0f, 47.9f, 70.0f, 47.3f, 2.5f);
        drawLine(sr, cx, cy, s, 70.0f, 47.3f, 74.0f, 46.0f, 2.5f);
        sr.setColor(0.97f, 0.44f, 0.44f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 48.0f, 2.0f);
        // --- LAYER: specular_highlights ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.60f);
        drawLine(sr, cx, cy, s, 44.0f, 60.0f, 52.0f, 64.5f, 2.0f);
        drawLine(sr, cx, cy, s, 52.0f, 64.5f, 60.0f, 66.7f, 2.0f);
        drawLine(sr, cx, cy, s, 60.0f, 66.7f, 68.0f, 66.7f, 2.0f);
        drawLine(sr, cx, cy, s, 68.0f, 66.7f, 76.0f, 64.5f, 2.0f);
        drawLine(sr, cx, cy, s, 76.0f, 64.5f, 84.0f, 60.0f, 2.0f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.70f);
        drawLine(sr, cx, cy, s, 50.0f, 70.0f, 60.0f, 82.0f, 1.8f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 45.0f, 27.0f, 1.5f);
    }

    public static void drawAncientVase(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // --- LAYER: background_glow ---
        sr.setColor(0.96f, 0.62f, 0.04f, 0.25f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 44.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.40f);
        fillEllipse(sr, cx, cy, s, 64.0f, 112.0f, 28.0f, 7.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.47f, 0.21f, 0.06f, 1.00f);
        fillQuad(sr, cx, cy, s, 48.0f, 96.0f, 80.0f, 96.0f, 80.0f, 106.0f, 48.0f, 106.0f);
        sr.setColor(0.05f, 0.58f, 0.53f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 68.0f, 30.0f);
        fillQuad(sr, cx, cy, s, 52.0f, 46.0f, 76.0f, 46.0f, 70.0f, 24.0f, 58.0f, 24.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillEllipse(sr, cx, cy, s, 64.0f, 24.0f, 16.0f, 5.0f);
        sr.setColor(0.07f, 0.37f, 0.35f, 1.00f);
        fillEllipse(sr, cx, cy, s, 64.0f, 23.0f, 12.0f, 3.5f);
        // --- LAYER: details_and_relief ---
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        drawLine(sr, cx, cy, s, 52.0f, 28.0f, 46.7f, 32.2f, 4.0f);
        drawLine(sr, cx, cy, s, 46.7f, 32.2f, 43.7f, 37.0f, 4.0f);
        drawLine(sr, cx, cy, s, 43.7f, 37.0f, 42.9f, 42.2f, 4.0f);
        drawLine(sr, cx, cy, s, 42.9f, 42.2f, 44.3f, 47.8f, 4.0f);
        drawLine(sr, cx, cy, s, 44.3f, 47.8f, 48.0f, 54.0f, 4.0f);
        drawLine(sr, cx, cy, s, 76.0f, 28.0f, 81.3f, 32.2f, 4.0f);
        drawLine(sr, cx, cy, s, 81.3f, 32.2f, 84.3f, 37.0f, 4.0f);
        drawLine(sr, cx, cy, s, 84.3f, 37.0f, 85.1f, 42.2f, 4.0f);
        drawLine(sr, cx, cy, s, 85.1f, 42.2f, 83.7f, 47.8f, 4.0f);
        drawLine(sr, cx, cy, s, 83.7f, 47.8f, 80.0f, 54.0f, 4.0f);
        sr.setColor(0.92f, 0.70f, 0.03f, 1.00f);
        fillQuad(sr, cx, cy, s, 36.0f, 64.0f, 92.0f, 64.0f, 92.0f, 72.0f, 36.0f, 72.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        fillCircle(sr, cx, cy, s, 44.0f, 68.0f, 2.5f);
        fillCircle(sr, cx, cy, s, 54.0f, 68.0f, 2.5f);
        fillCircle(sr, cx, cy, s, 64.0f, 68.0f, 2.5f);
        fillCircle(sr, cx, cy, s, 74.0f, 68.0f, 2.5f);
        fillCircle(sr, cx, cy, s, 84.0f, 68.0f, 2.5f);
        // --- LAYER: specular_highlights ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.85f);
        drawLine(sr, cx, cy, s, 44.0f, 54.0f, 48.8f, 50.4f, 2.5f);
        drawLine(sr, cx, cy, s, 48.8f, 50.4f, 53.6f, 47.6f, 2.5f);
        drawLine(sr, cx, cy, s, 53.6f, 47.6f, 58.4f, 45.6f, 2.5f);
        drawLine(sr, cx, cy, s, 58.4f, 45.6f, 63.2f, 44.4f, 2.5f);
        drawLine(sr, cx, cy, s, 63.2f, 44.4f, 68.0f, 44.0f, 2.5f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.60f);
        fillEllipse(sr, cx, cy, s, 64.0f, 22.0f, 14.0f, 2.0f);
        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 60.0f, 68.0f, 68.0f, 68.0f, 1.5f);
        drawLine(sr, cx, cy, s, 64.0f, 64.0f, 64.0f, 72.0f, 1.5f);
    }

    public static void drawGoldRing(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // --- LAYER: background_glow ---
        sr.setColor(0.98f, 0.80f, 0.08f, 0.25f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 44.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.35f);
        fillEllipse(sr, cx, cy, s, 64.0f, 112.0f, 26.0f, 7.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 70.0f, 32.0f);
        sr.setColor(0.06f, 0.09f, 0.16f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 70.0f, 20.0f);
        sr.setColor(0.01f, 0.02f, 0.09f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 70.0f, 18.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillQuad(sr, cx, cy, s, 50.0f, 28.0f, 78.0f, 28.0f, 78.0f, 42.0f, 50.0f, 42.0f);
        // --- LAYER: details_and_relief ---
        sr.setColor(0.75f, 0.07f, 0.24f, 1.00f);
        fillTri(sr, cx, cy, s, 52.0f, 32.0f, 56.0f, 16.0f, 64.0f, 12.0f);
        fillTri(sr, cx, cy, s, 52.0f, 32.0f, 64.0f, 12.0f, 72.0f, 16.0f);
        fillTri(sr, cx, cy, s, 72.0f, 16.0f, 76.0f, 32.0f, 52.0f, 32.0f);
        sr.setColor(0.88f, 0.11f, 0.28f, 1.00f);
        fillQuad(sr, cx, cy, s, 56.0f, 32.0f, 72.0f, 32.0f, 68.0f, 18.0f, 60.0f, 18.0f);
        sr.setColor(0.99f, 0.64f, 0.69f, 1.00f);
        fillTri(sr, cx, cy, s, 60.0f, 18.0f, 68.0f, 18.0f, 64.0f, 12.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillCircle(sr, cx, cy, s, 52.0f, 30.0f, 2.5f);
        fillCircle(sr, cx, cy, s, 76.0f, 30.0f, 2.5f);
        fillCircle(sr, cx, cy, s, 55.0f, 16.0f, 2.0f);
        fillCircle(sr, cx, cy, s, 73.0f, 16.0f, 2.0f);
        // --- LAYER: specular_highlights ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.95f);
        fillTri(sr, cx, cy, s, 62.0f, 18.0f, 66.0f, 18.0f, 64.0f, 14.0f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.80f);
        drawLine(sr, cx, cy, s, 40.0f, 58.0f, 41.0f, 52.6f, 2.0f);
        drawLine(sr, cx, cy, s, 41.0f, 52.6f, 42.6f, 47.8f, 2.0f);
        drawLine(sr, cx, cy, s, 42.6f, 47.8f, 44.6f, 43.4f, 2.0f);
        drawLine(sr, cx, cy, s, 44.6f, 43.4f, 47.0f, 39.4f, 2.0f);
        drawLine(sr, cx, cy, s, 47.0f, 39.4f, 50.0f, 36.0f, 2.0f);
        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 64.0f, 6.0f, 64.0f, 20.0f, 2.0f);
        drawLine(sr, cx, cy, s, 57.0f, 13.0f, 71.0f, 13.0f, 2.0f);
    }

    public static void drawSunkenChest(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // --- LAYER: background_glow ---
        sr.setColor(0.98f, 0.80f, 0.08f, 0.25f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 44.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.45f);
        fillEllipse(sr, cx, cy, s, 64.0f, 112.0f, 36.0f, 8.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.27f, 0.10f, 0.01f, 1.00f);
        fillQuad(sr, cx, cy, s, 28.0f, 56.0f, 100.0f, 56.0f, 100.0f, 98.0f, 28.0f, 98.0f);
        sr.setColor(0.47f, 0.21f, 0.06f, 1.00f);
        fillQuad(sr, cx, cy, s, 26.0f, 28.0f, 102.0f, 28.0f, 102.0f, 52.0f, 26.0f, 52.0f);
        sr.setColor(0.12f, 0.16f, 0.23f, 1.00f);
        fillQuad(sr, cx, cy, s, 36.0f, 56.0f, 46.0f, 56.0f, 46.0f, 98.0f, 36.0f, 98.0f);
        fillQuad(sr, cx, cy, s, 82.0f, 56.0f, 92.0f, 56.0f, 92.0f, 98.0f, 82.0f, 98.0f);
        sr.setColor(0.20f, 0.25f, 0.33f, 1.00f);
        fillQuad(sr, cx, cy, s, 36.0f, 28.0f, 46.0f, 28.0f, 46.0f, 52.0f, 36.0f, 52.0f);
        fillQuad(sr, cx, cy, s, 82.0f, 28.0f, 92.0f, 28.0f, 92.0f, 52.0f, 82.0f, 52.0f);
        // --- LAYER: details_and_relief ---
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillCircle(sr, cx, cy, s, 44.0f, 52.0f, 4.5f);
        fillCircle(sr, cx, cy, s, 52.0f, 50.0f, 5.0f);
        sr.setColor(0.99f, 0.88f, 0.28f, 1.00f);
        fillCircle(sr, cx, cy, s, 60.0f, 53.0f, 4.5f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillCircle(sr, cx, cy, s, 68.0f, 51.0f, 5.0f);
        sr.setColor(0.99f, 0.88f, 0.28f, 1.00f);
        fillCircle(sr, cx, cy, s, 76.0f, 53.0f, 4.5f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillCircle(sr, cx, cy, s, 84.0f, 51.0f, 4.0f);
        sr.setColor(0.02f, 0.71f, 0.83f, 1.00f);
        fillQuad(sr, cx, cy, s, 64.0f, 44.0f, 68.0f, 48.0f, 64.0f, 52.0f, 60.0f, 48.0f);
        sr.setColor(0.92f, 0.70f, 0.03f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 66.0f, 8.0f);
        sr.setColor(0.06f, 0.09f, 0.16f, 1.00f);
        fillQuad(sr, cx, cy, s, 62.0f, 70.0f, 66.0f, 70.0f, 66.0f, 76.0f, 62.0f, 76.0f);
        fillCircle(sr, cx, cy, s, 61.0f, 65.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 67.0f, 65.0f, 1.5f);
        sr.setColor(0.58f, 0.64f, 0.72f, 1.00f);
        fillCircle(sr, cx, cy, s, 34.0f, 90.0f, 3.5f);
        fillCircle(sr, cx, cy, s, 92.0f, 88.0f, 4.0f);
        // --- LAYER: specular_highlights ---
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 51.0f, 49.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 67.0f, 50.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 64.0f, 46.0f, 1.2f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.65f);
        drawLine(sr, cx, cy, s, 30.0f, 32.0f, 98.0f, 32.0f, 2.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 66.0f, 63.0f, 1.5f);
    }

    public static void drawAncientSword(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // --- LAYER: background_glow ---
        sr.setColor(0.22f, 0.74f, 0.97f, 0.25f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 44.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.35f);
        fillEllipse(sr, cx, cy, s, 64.0f, 112.0f, 22.0f, 6.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.97f, 0.98f, 0.99f, 1.00f);
        fillTri(sr, cx, cy, s, 60.0f, 78.0f, 60.0f, 26.0f, 64.0f, 12.0f);
        fillTri(sr, cx, cy, s, 60.0f, 78.0f, 64.0f, 12.0f, 68.0f, 26.0f);
        fillTri(sr, cx, cy, s, 68.0f, 26.0f, 68.0f, 78.0f, 60.0f, 78.0f);
        sr.setColor(0.01f, 0.52f, 0.78f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 26.0f, 64.0f, 76.0f, 2.5f);
        sr.setColor(0.02f, 0.59f, 0.41f, 1.00f);
        fillQuad(sr, cx, cy, s, 42.0f, 78.0f, 86.0f, 78.0f, 86.0f, 86.0f, 42.0f, 86.0f);
        sr.setColor(0.02f, 0.47f, 0.34f, 1.00f);
        fillTri(sr, cx, cy, s, 42.0f, 78.0f, 36.0f, 74.0f, 42.0f, 74.0f);
        fillTri(sr, cx, cy, s, 86.0f, 78.0f, 92.0f, 74.0f, 86.0f, 74.0f);
        sr.setColor(0.60f, 0.11f, 0.11f, 1.00f);
        fillQuad(sr, cx, cy, s, 61.0f, 86.0f, 67.0f, 86.0f, 67.0f, 104.0f, 61.0f, 104.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 106.0f, 5.0f);
        // --- LAYER: details_and_relief ---
        sr.setColor(0.22f, 0.74f, 0.97f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 32.0f, 2.0f);
        fillCircle(sr, cx, cy, s, 64.0f, 44.0f, 2.0f);
        fillCircle(sr, cx, cy, s, 64.0f, 56.0f, 2.0f);
        fillCircle(sr, cx, cy, s, 64.0f, 68.0f, 2.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillCircle(sr, cx, cy, s, 50.0f, 82.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 78.0f, 82.0f, 1.5f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 110.0f, 62.5f, 113.1f, 2.5f);
        drawLine(sr, cx, cy, s, 62.5f, 113.1f, 61.1f, 116.1f, 2.5f);
        drawLine(sr, cx, cy, s, 61.1f, 116.1f, 59.9f, 118.9f, 2.5f);
        drawLine(sr, cx, cy, s, 59.9f, 118.9f, 58.9f, 121.5f, 2.5f);
        drawLine(sr, cx, cy, s, 58.9f, 121.5f, 58.0f, 124.0f, 2.5f);
        sr.setColor(0.06f, 0.73f, 0.51f, 1.00f);
        fillCircle(sr, cx, cy, s, 61.0f, 116.0f, 1.8f);
        // --- LAYER: specular_highlights ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.90f);
        drawLine(sr, cx, cy, s, 67.0f, 28.0f, 67.0f, 76.0f, 1.5f);
        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 60.0f, 12.0f, 68.0f, 12.0f, 2.0f);
        drawLine(sr, cx, cy, s, 64.0f, 8.0f, 64.0f, 16.0f, 2.0f);
        fillCircle(sr, cx, cy, s, 63.0f, 105.0f, 1.5f);
    }

    public static void drawAromaBait(ShapeRenderer sr, float cx, float cy, float s) {
        // --- LAYER: background_glow ---
        sr.setColor(0.52f, 0.80f, 0.09f, 0.25f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 44.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.35f);
        fillEllipse(sr, cx, cy, s, 64.0f, 112.0f, 28.0f, 7.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.75f, 0.95f, 0.39f, 0.70f);
        fillQuad(sr, cx, cy, s, 42.0f, 42.0f, 86.0f, 42.0f, 86.0f, 100.0f, 42.0f, 100.0f);
        sr.setColor(0.52f, 0.80f, 0.09f, 0.80f);
        fillQuad(sr, cx, cy, s, 40.0f, 36.0f, 88.0f, 36.0f, 88.0f, 44.0f, 40.0f, 44.0f);
        sr.setColor(0.57f, 0.25f, 0.05f, 1.00f);
        fillQuad(sr, cx, cy, s, 46.0f, 36.0f, 82.0f, 36.0f, 78.0f, 22.0f, 50.0f, 22.0f);
        sr.setColor(0.85f, 0.47f, 0.02f, 1.00f);
        fillQuad(sr, cx, cy, s, 42.0f, 40.0f, 86.0f, 40.0f, 86.0f, 44.0f, 42.0f, 44.0f);
        // --- LAYER: details_and_relief ---
        sr.setColor(0.64f, 0.90f, 0.21f, 1.00f);
        fillCircle(sr, cx, cy, s, 54.0f, 74.0f, 9.0f);
        sr.setColor(0.52f, 0.80f, 0.09f, 1.00f);
        fillCircle(sr, cx, cy, s, 72.0f, 72.0f, 10.0f);
        sr.setColor(0.40f, 0.64f, 0.05f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 86.0f, 11.0f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        fillCircle(sr, cx, cy, s, 53.0f, 73.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 71.0f, 71.0f, 1.5f);
        sr.setColor(0.71f, 0.33f, 0.04f, 1.00f);
        fillQuad(sr, cx, cy, s, 76.0f, 44.0f, 86.0f, 54.0f, 80.0f, 64.0f, 70.0f, 54.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillTri(sr, cx, cy, s, 75.0f, 53.0f, 76.0f, 51.0f, 77.0f, 53.0f);
        fillTri(sr, cx, cy, s, 77.0f, 53.0f, 80.0f, 53.0f, 78.0f, 55.0f);
        fillTri(sr, cx, cy, s, 75.0f, 53.0f, 77.0f, 53.0f, 78.0f, 55.0f);
        fillTri(sr, cx, cy, s, 75.0f, 53.0f, 78.0f, 55.0f, 79.0f, 57.0f);
        fillTri(sr, cx, cy, s, 75.0f, 53.0f, 79.0f, 57.0f, 76.0f, 56.0f);
        fillTri(sr, cx, cy, s, 76.0f, 56.0f, 73.0f, 57.0f, 74.0f, 55.0f);
        fillTri(sr, cx, cy, s, 75.0f, 53.0f, 76.0f, 56.0f, 74.0f, 55.0f);
        fillTri(sr, cx, cy, s, 74.0f, 55.0f, 72.0f, 53.0f, 75.0f, 53.0f);
        sr.setColor(0.75f, 0.95f, 0.39f, 0.80f);
        drawLine(sr, cx, cy, s, 52.0f, 20.0f, 50.9f, 16.2f, 2.0f);
        drawLine(sr, cx, cy, s, 50.9f, 16.2f, 50.7f, 13.0f, 2.0f);
        drawLine(sr, cx, cy, s, 50.7f, 13.0f, 51.5f, 10.2f, 2.0f);
        drawLine(sr, cx, cy, s, 51.5f, 10.2f, 53.3f, 7.8f, 2.0f);
        drawLine(sr, cx, cy, s, 53.3f, 7.8f, 56.0f, 6.0f, 2.0f);
        drawLine(sr, cx, cy, s, 76.0f, 20.0f, 77.2f, 16.9f, 2.0f);
        drawLine(sr, cx, cy, s, 77.2f, 16.9f, 77.6f, 13.9f, 2.0f);
        drawLine(sr, cx, cy, s, 77.6f, 13.9f, 77.2f, 11.1f, 2.0f);
        drawLine(sr, cx, cy, s, 77.2f, 11.1f, 76.0f, 8.5f, 2.0f);
        drawLine(sr, cx, cy, s, 76.0f, 8.5f, 74.0f, 6.0f, 2.0f);
        // --- LAYER: specular_highlights ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.85f);
        drawLine(sr, cx, cy, s, 46.0f, 46.0f, 46.0f, 94.0f, 2.5f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.60f);
        drawLine(sr, cx, cy, s, 52.0f, 24.0f, 76.0f, 24.0f, 1.2f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 76.0f, 54.0f, 1.2f);
    }

    public static void drawRiskyBait(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // --- LAYER: background_glow ---
        sr.setColor(0.49f, 0.23f, 0.93f, 0.25f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 44.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.45f);
        fillEllipse(sr, cx, cy, s, 64.0f, 112.0f, 28.0f, 7.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.18f, 0.06f, 0.40f, 1.00f);
        fillQuad(sr, cx, cy, s, 42.0f, 40.0f, 86.0f, 40.0f, 86.0f, 100.0f, 42.0f, 100.0f);
        sr.setColor(0.88f, 0.11f, 0.28f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 74.0f, 18.0f);
        sr.setColor(0.20f, 0.25f, 0.33f, 1.00f);
        fillQuad(sr, cx, cy, s, 40.0f, 30.0f, 88.0f, 30.0f, 88.0f, 42.0f, 40.0f, 42.0f);
        sr.setColor(0.28f, 0.33f, 0.41f, 1.00f);
        fillQuad(sr, cx, cy, s, 38.0f, 28.0f, 90.0f, 28.0f, 90.0f, 31.0f, 38.0f, 31.0f);
        // --- LAYER: details_and_relief ---
        sr.setColor(0.97f, 0.98f, 0.99f, 1.00f);
        fillQuad(sr, cx, cy, s, 48.0f, 58.0f, 80.0f, 58.0f, 80.0f, 88.0f, 48.0f, 88.0f);
        sr.setColor(0.06f, 0.09f, 0.16f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 69.0f, 6.0f);
        fillQuad(sr, cx, cy, s, 62.0f, 73.0f, 66.0f, 73.0f, 66.0f, 78.0f, 62.0f, 78.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 62.0f, 68.0f, 1.2f);
        fillCircle(sr, cx, cy, s, 66.0f, 68.0f, 1.2f);
        sr.setColor(0.06f, 0.09f, 0.16f, 1.00f);
        drawLine(sr, cx, cy, s, 56.0f, 81.0f, 72.0f, 81.0f, 2.0f);
        fillCircle(sr, cx, cy, s, 56.0f, 81.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 72.0f, 81.0f, 1.5f);
        sr.setColor(0.75f, 0.52f, 0.99f, 1.00f);
        fillCircle(sr, cx, cy, s, 54.0f, 22.0f, 2.5f);
        sr.setColor(0.96f, 0.25f, 0.37f, 1.00f);
        fillCircle(sr, cx, cy, s, 74.0f, 18.0f, 3.0f);
        sr.setColor(0.91f, 0.47f, 0.98f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 14.0f, 2.0f);
        // --- LAYER: specular_highlights ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.75f);
        drawLine(sr, cx, cy, s, 45.0f, 42.0f, 45.0f, 98.0f, 2.0f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.60f);
        drawLine(sr, cx, cy, s, 42.0f, 30.0f, 86.0f, 30.0f, 1.5f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 60.0f, 70.0f, 1.8f);
    }

    public static void drawActiveSpellBook(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // --- LAYER: background_glow ---
        sr.setColor(0.94f, 0.27f, 0.27f, 0.25f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 44.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.40f);
        fillEllipse(sr, cx, cy, s, 64.0f, 112.0f, 34.0f, 8.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.50f, 0.11f, 0.11f, 1.00f);
        fillQuad(sr, cx, cy, s, 36.0f, 24.0f, 92.0f, 24.0f, 92.0f, 98.0f, 36.0f, 98.0f);
        sr.setColor(0.99f, 0.88f, 0.28f, 1.00f);
        fillQuad(sr, cx, cy, s, 86.0f, 28.0f, 94.0f, 28.0f, 94.0f, 94.0f, 86.0f, 94.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        drawLine(sr, cx, cy, s, 88.0f, 32.0f, 88.0f, 90.0f, 1.0f);
        drawLine(sr, cx, cy, s, 91.0f, 32.0f, 91.0f, 90.0f, 1.0f);
        // --- LAYER: details_and_relief ---
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillTri(sr, cx, cy, s, 36.0f, 24.0f, 50.0f, 24.0f, 36.0f, 38.0f);
        fillTri(sr, cx, cy, s, 86.0f, 24.0f, 72.0f, 24.0f, 86.0f, 38.0f);
        fillTri(sr, cx, cy, s, 36.0f, 98.0f, 50.0f, 98.0f, 36.0f, 84.0f);
        fillTri(sr, cx, cy, s, 86.0f, 98.0f, 72.0f, 98.0f, 86.0f, 84.0f);
        sr.setColor(0.27f, 0.04f, 0.04f, 1.00f);
        fillCircle(sr, cx, cy, s, 61.0f, 58.0f, 18.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillCircle(sr, cx, cy, s, 61.0f, 58.0f, 15.0f);
        sr.setColor(Color.WHITE);
        fillQuad(sr, cx, cy, s, 60.0f, 46.0f, 62.0f, 46.0f, 62.0f, 64.0f, 60.0f, 64.0f);
        fillTri(sr, cx, cy, s, 61.0f, 42.0f, 63.0f, 46.0f, 59.0f, 46.0f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        drawLine(sr, cx, cy, s, 56.0f, 64.0f, 66.0f, 64.0f, 2.5f);
        fillCircle(sr, cx, cy, s, 61.0f, 68.0f, 2.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        fillTri(sr, cx, cy, s, 68.0f, 118.0f, 68.0f, 96.0f, 76.0f, 96.0f);
        fillTri(sr, cx, cy, s, 76.0f, 96.0f, 76.0f, 118.0f, 72.0f, 112.0f);
        fillTri(sr, cx, cy, s, 76.0f, 96.0f, 72.0f, 112.0f, 68.0f, 118.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        drawLine(sr, cx, cy, s, 68.0f, 96.0f, 76.0f, 96.0f, 2.0f);
        // --- LAYER: specular_highlights ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.60f);
        drawLine(sr, cx, cy, s, 39.0f, 26.0f, 39.0f, 96.0f, 2.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 42.0f, 28.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 80.0f, 28.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 61.0f, 46.0f, 1.5f);
    }

    public static void drawPassiveSpellBook(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // --- LAYER: background_glow ---
        sr.setColor(0.39f, 0.40f, 0.95f, 0.25f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 44.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.40f);
        fillEllipse(sr, cx, cy, s, 64.0f, 112.0f, 34.0f, 8.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.19f, 0.18f, 0.51f, 1.00f);
        fillQuad(sr, cx, cy, s, 36.0f, 24.0f, 92.0f, 24.0f, 92.0f, 98.0f, 36.0f, 98.0f);
        sr.setColor(0.89f, 0.91f, 0.94f, 1.00f);
        fillQuad(sr, cx, cy, s, 86.0f, 28.0f, 94.0f, 28.0f, 94.0f, 94.0f, 86.0f, 94.0f);
        sr.setColor(0.58f, 0.64f, 0.72f, 1.00f);
        drawLine(sr, cx, cy, s, 88.0f, 32.0f, 88.0f, 90.0f, 1.0f);
        drawLine(sr, cx, cy, s, 91.0f, 32.0f, 91.0f, 90.0f, 1.0f);
        // --- LAYER: details_and_relief ---
        sr.setColor(0.80f, 0.84f, 0.88f, 1.00f);
        fillTri(sr, cx, cy, s, 36.0f, 24.0f, 50.0f, 24.0f, 36.0f, 38.0f);
        fillTri(sr, cx, cy, s, 86.0f, 24.0f, 72.0f, 24.0f, 86.0f, 38.0f);
        fillTri(sr, cx, cy, s, 36.0f, 98.0f, 50.0f, 98.0f, 36.0f, 84.0f);
        fillTri(sr, cx, cy, s, 86.0f, 98.0f, 72.0f, 98.0f, 86.0f, 84.0f);
        sr.setColor(0.06f, 0.09f, 0.16f, 1.00f);
        fillCircle(sr, cx, cy, s, 61.0f, 58.0f, 18.0f);
        sr.setColor(0.22f, 0.74f, 0.97f, 1.00f);
        fillCircle(sr, cx, cy, s, 61.0f, 58.0f, 15.0f);
        sr.setColor(0.12f, 0.11f, 0.29f, 1.00f);
        fillEllipse(sr, cx, cy, s, 61.0f, 58.0f, 10.0f, 6.0f);
        sr.setColor(0.22f, 0.74f, 0.97f, 1.00f);
        fillCircle(sr, cx, cy, s, 61.0f, 58.0f, 4.0f);
        sr.setColor(0.01f, 0.02f, 0.09f, 1.00f);
        fillCircle(sr, cx, cy, s, 61.0f, 58.0f, 2.0f);
        sr.setColor(0.01f, 0.52f, 0.78f, 1.00f);
        fillTri(sr, cx, cy, s, 68.0f, 116.0f, 68.0f, 96.0f, 76.0f, 96.0f);
        fillTri(sr, cx, cy, s, 76.0f, 96.0f, 76.0f, 116.0f, 72.0f, 112.0f);
        fillTri(sr, cx, cy, s, 76.0f, 96.0f, 72.0f, 112.0f, 68.0f, 116.0f);
        sr.setColor(0.22f, 0.74f, 0.97f, 1.00f);
        fillCircle(sr, cx, cy, s, 72.0f, 116.0f, 3.5f);
        // --- LAYER: specular_highlights ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.60f);
        drawLine(sr, cx, cy, s, 39.0f, 26.0f, 39.0f, 96.0f, 2.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 60.0f, 57.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 63.0f, 59.0f, 0.8f);
        fillCircle(sr, cx, cy, s, 71.0f, 115.0f, 1.2f);
        fillCircle(sr, cx, cy, s, 42.0f, 28.0f, 1.2f);
    }

    private static void drawGenericPotion(ShapeRenderer sr, float cx, float cy, float s) {
        drawStaminaPotion(sr, cx, cy, s);
    }

    public static void drawBread(ShapeRenderer sr, float cx, float cy, float s) {
        // --- LAYER: background_glow ---
        sr.setColor(0.47f, 0.21f, 0.06f, 0.00f);
        fillEllipse(sr, cx, cy, s, 64.0f, 68.0f, 46.0f, 28.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.35f);
        fillEllipse(sr, cx, cy, s, 64.0f, 86.0f, 42.0f, 12.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.71f, 0.33f, 0.04f, 1.00f);
        fillEllipse(sr, cx, cy, s, 64.0f, 68.0f, 44.0f, 26.0f);
        sr.setColor(0.96f, 0.62f, 0.04f, 1.00f);
        fillEllipse(sr, cx, cy, s, 64.0f, 64.0f, 40.0f, 21.0f);
        sr.setColor(0.99f, 0.88f, 0.28f, 1.00f);
        fillEllipse(sr, cx, cy, s, 64.0f, 62.0f, 36.0f, 16.0f);
        // --- LAYER: details_and_relief ---
        sr.setColor(0.47f, 0.21f, 0.06f, 1.00f);
        drawLine(sr, cx, cy, s, 44.0f, 50.0f, 52.0f, 74.0f, 3.5f);
        drawLine(sr, cx, cy, s, 60.0f, 50.0f, 68.0f, 74.0f, 3.5f);
        drawLine(sr, cx, cy, s, 76.0f, 50.0f, 84.0f, 74.0f, 3.5f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        drawLine(sr, cx, cy, s, 44.0f, 52.0f, 51.0f, 72.0f, 1.8f);
        drawLine(sr, cx, cy, s, 60.0f, 52.0f, 67.0f, 72.0f, 1.8f);
        drawLine(sr, cx, cy, s, 76.0f, 52.0f, 83.0f, 72.0f, 1.8f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillEllipse(sr, cx, cy, s, 48.0f, 62.0f, 3.0f, 1.5f);
        fillEllipse(sr, cx, cy, s, 64.0f, 62.0f, 3.5f, 2.0f);
        fillEllipse(sr, cx, cy, s, 80.0f, 62.0f, 3.0f, 1.5f);
        sr.setColor(1.00f, 0.98f, 0.76f, 1.00f);
        fillCircle(sr, cx, cy, s, 36.0f, 60.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 92.0f, 60.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 56.0f, 56.0f, 1.2f);
        fillCircle(sr, cx, cy, s, 72.0f, 56.0f, 1.2f);
        // --- LAYER: specular_highlights ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.85f);
        drawLine(sr, cx, cy, s, 38.0f, 56.0f, 51.0f, 53.0f, 1.2f);
        drawLine(sr, cx, cy, s, 51.0f, 53.0f, 64.0f, 52.0f, 1.2f);
        drawLine(sr, cx, cy, s, 64.0f, 52.0f, 77.0f, 53.0f, 1.2f);
        drawLine(sr, cx, cy, s, 77.0f, 53.0f, 90.0f, 56.0f, 1.2f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 63.0f, 61.0f, 1.2f);
        fillCircle(sr, cx, cy, s, 47.0f, 61.0f, 1.0f);
        fillCircle(sr, cx, cy, s, 79.0f, 61.0f, 1.0f);
    }

    public static void drawStaminaPotion(ShapeRenderer sr, float cx, float cy, float s) {
        // --- LAYER: background_glow ---
        sr.setColor(0.02f, 0.47f, 0.34f, 0.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 72.0f, 42.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.40f);
        fillEllipse(sr, cx, cy, s, 64.0f, 104.0f, 26.0f, 8.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.06f, 0.09f, 0.16f, 0.60f);
        fillCircle(sr, cx, cy, s, 64.0f, 74.0f, 32.0f);
        sr.setColor(0.06f, 0.73f, 0.51f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 74.0f, 28.0f);
        sr.setColor(0.20f, 0.25f, 0.33f, 1.00f);
        fillQuad(sr, cx, cy, s, 56.0f, 28.0f, 72.0f, 28.0f, 72.0f, 46.0f, 56.0f, 46.0f);
        sr.setColor(0.06f, 0.73f, 0.51f, 0.60f);
        fillQuad(sr, cx, cy, s, 58.0f, 30.0f, 70.0f, 30.0f, 70.0f, 46.0f, 58.0f, 46.0f);
        sr.setColor(0.39f, 0.45f, 0.55f, 1.00f);
        fillQuad(sr, cx, cy, s, 54.0f, 24.0f, 74.0f, 24.0f, 74.0f, 30.0f, 54.0f, 30.0f);
        sr.setColor(0.57f, 0.25f, 0.05f, 1.00f);
        fillQuad(sr, cx, cy, s, 56.0f, 24.0f, 72.0f, 24.0f, 70.0f, 14.0f, 58.0f, 14.0f);
        sr.setColor(0.47f, 0.21f, 0.06f, 1.00f);
        drawLine(sr, cx, cy, s, 57.0f, 18.0f, 71.0f, 18.0f, 1.5f);
        // --- LAYER: details_and_relief ---
        fillQuad(sr, cx, cy, s, 55.0f, 36.0f, 73.0f, 36.0f, 73.0f, 40.0f, 55.0f, 40.0f);
        sr.setColor(0.57f, 0.25f, 0.05f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 40.0f, 60.0f, 50.0f, 2.0f);
        sr.setColor(0.96f, 0.62f, 0.04f, 1.00f);
        fillCircle(sr, cx, cy, s, 60.0f, 52.0f, 2.5f);
        sr.setColor(0.65f, 0.95f, 0.82f, 1.00f);
        fillCircle(sr, cx, cy, s, 56.0f, 80.0f, 3.5f);
        fillCircle(sr, cx, cy, s, 72.0f, 70.0f, 4.0f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 2.5f);
        fillCircle(sr, cx, cy, s, 60.0f, 90.0f, 2.0f);
        fillCircle(sr, cx, cy, s, 74.0f, 84.0f, 2.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 55.0f, 79.0f, 1.2f);
        fillCircle(sr, cx, cy, s, 71.0f, 69.0f, 1.5f);
        // --- LAYER: specular_highlights ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.90f);
        drawLine(sr, cx, cy, s, 44.0f, 56.0f, 48.2f, 51.6f, 3.0f);
        drawLine(sr, cx, cy, s, 48.2f, 51.6f, 53.0f, 48.5f, 3.0f);
        drawLine(sr, cx, cy, s, 53.0f, 48.5f, 58.2f, 46.6f, 3.0f);
        drawLine(sr, cx, cy, s, 58.2f, 46.6f, 64.0f, 46.0f, 3.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 42.0f, 62.0f, 1.8f);
        sr.setColor(0.43f, 0.91f, 0.72f, 0.80f);
        drawLine(sr, cx, cy, s, 52.0f, 98.0f, 58.0f, 99.5f, 2.0f);
        drawLine(sr, cx, cy, s, 58.0f, 99.5f, 64.0f, 100.0f, 2.0f);
        drawLine(sr, cx, cy, s, 64.0f, 100.0f, 70.0f, 99.5f, 2.0f);
        drawLine(sr, cx, cy, s, 70.0f, 99.5f, 76.0f, 98.0f, 2.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 60.0f, 16.0f, 1.2f);
    }

    public static void drawEnergyDrink(ShapeRenderer sr, float cx, float cy, float s) {
        // --- LAYER: background_glow ---
        sr.setColor(0.73f, 0.11f, 0.11f, 0.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 44.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.40f);
        fillEllipse(sr, cx, cy, s, 64.0f, 112.0f, 24.0f, 7.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        fillQuad(sr, cx, cy, s, 42.0f, 28.0f, 86.0f, 28.0f, 86.0f, 104.0f, 42.0f, 104.0f);
        sr.setColor(0.97f, 0.98f, 0.99f, 1.00f);
        fillEllipse(sr, cx, cy, s, 64.0f, 104.0f, 22.0f, 6.0f);
        fillEllipse(sr, cx, cy, s, 64.0f, 28.0f, 20.0f, 5.0f);
        sr.setColor(0.20f, 0.25f, 0.33f, 1.00f);
        fillEllipse(sr, cx, cy, s, 64.0f, 27.0f, 17.0f, 4.0f);
        // --- LAYER: details_and_relief ---
        sr.setColor(0.58f, 0.64f, 0.72f, 1.00f);
        fillEllipse(sr, cx, cy, s, 64.0f, 26.0f, 8.0f, 2.5f);
        sr.setColor(0.12f, 0.16f, 0.23f, 1.00f);
        fillCircle(sr, cx, cy, s, 62.0f, 26.0f, 1.5f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillQuad(sr, cx, cy, s, 42.0f, 42.0f, 86.0f, 50.0f, 86.0f, 54.0f, 42.0f, 46.0f);
        fillQuad(sr, cx, cy, s, 42.0f, 84.0f, 86.0f, 92.0f, 86.0f, 96.0f, 42.0f, 88.0f);
        fillTri(sr, cx, cy, s, 66.0f, 60.0f, 78.0f, 60.0f, 56.0f, 94.0f);
        fillTri(sr, cx, cy, s, 66.0f, 60.0f, 56.0f, 94.0f, 62.0f, 68.0f);
        fillTri(sr, cx, cy, s, 68.0f, 36.0f, 66.0f, 60.0f, 62.0f, 68.0f);
        fillTri(sr, cx, cy, s, 62.0f, 68.0f, 50.0f, 68.0f, 68.0f, 36.0f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.80f);
        fillTri(sr, cx, cy, s, 65.0f, 62.0f, 74.0f, 62.0f, 59.0f, 88.0f);
        fillTri(sr, cx, cy, s, 65.0f, 62.0f, 59.0f, 88.0f, 63.0f, 66.0f);
        fillTri(sr, cx, cy, s, 67.0f, 40.0f, 65.0f, 62.0f, 63.0f, 66.0f);
        fillTri(sr, cx, cy, s, 63.0f, 66.0f, 54.0f, 66.0f, 67.0f, 40.0f);
        // --- LAYER: specular_highlights ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.85f);
        drawLine(sr, cx, cy, s, 48.0f, 30.0f, 48.0f, 102.0f, 2.5f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.50f);
        drawLine(sr, cx, cy, s, 52.0f, 32.0f, 52.0f, 100.0f, 1.0f);
        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 46.0f, 27.0f, 55.0f, 25.5f, 1.5f);
        drawLine(sr, cx, cy, s, 55.0f, 25.5f, 64.0f, 25.0f, 1.5f);
        drawLine(sr, cx, cy, s, 64.0f, 25.0f, 73.0f, 25.5f, 1.5f);
        drawLine(sr, cx, cy, s, 73.0f, 25.5f, 82.0f, 27.0f, 1.5f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.90f);
        fillCircle(sr, cx, cy, s, 78.0f, 46.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 82.0f, 74.0f, 1.2f);
        fillCircle(sr, cx, cy, s, 46.0f, 62.0f, 1.0f);
    }

    public static void drawOverdrivePotion(ShapeRenderer sr, float cx, float cy, float s) {
        // --- LAYER: background_glow ---
        sr.setColor(0.86f, 0.15f, 0.15f, 0.20f);
        fillCircle(sr, cx, cy, s, 64.0f, 72.0f, 44.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.45f);
        fillEllipse(sr, cx, cy, s, 64.0f, 108.0f, 30.0f, 8.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.12f, 0.16f, 0.23f, 0.60f);
        fillQuad(sr, cx, cy, s, 56.0f, 36.0f, 72.0f, 36.0f, 94.0f, 100.0f, 34.0f, 100.0f);
        sr.setColor(0.98f, 0.45f, 0.09f, 1.00f);
        fillQuad(sr, cx, cy, s, 58.0f, 60.0f, 70.0f, 60.0f, 90.0f, 98.0f, 38.0f, 98.0f);
        sr.setColor(0.60f, 0.11f, 0.11f, 1.00f);
        fillEllipse(sr, cx, cy, s, 64.0f, 98.0f, 26.0f, 6.0f);
        sr.setColor(0.20f, 0.25f, 0.33f, 1.00f);
        fillQuad(sr, cx, cy, s, 56.0f, 24.0f, 72.0f, 24.0f, 72.0f, 40.0f, 56.0f, 40.0f);
        sr.setColor(0.39f, 0.45f, 0.55f, 1.00f);
        fillQuad(sr, cx, cy, s, 54.0f, 20.0f, 74.0f, 20.0f, 74.0f, 25.0f, 54.0f, 25.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillTri(sr, cx, cy, s, 64.0f, 10.0f, 70.0f, 20.0f, 58.0f, 20.0f);
        // --- LAYER: details_and_relief ---
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        fillQuad(sr, cx, cy, s, 53.0f, 32.0f, 75.0f, 32.0f, 75.0f, 38.0f, 53.0f, 38.0f);
        sr.setColor(0.27f, 0.10f, 0.01f, 1.00f);
        fillCircle(sr, cx, cy, s, 56.0f, 35.0f, 1.2f);
        fillCircle(sr, cx, cy, s, 64.0f, 35.0f, 1.2f);
        fillCircle(sr, cx, cy, s, 72.0f, 35.0f, 1.2f);
        sr.setColor(0.99f, 0.88f, 0.28f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 78.0f, 9.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 64.0f, 78.0f, 5.0f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        fillTri(sr, cx, cy, s, 52.0f, 86.0f, 58.0f, 82.0f, 54.0f, 90.0f);
        fillTri(sr, cx, cy, s, 74.0f, 72.0f, 78.0f, 78.0f, 72.0f, 80.0f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.70f);
        drawLine(sr, cx, cy, s, 42.0f, 84.0f, 48.0f, 84.0f, 1.5f);
        drawLine(sr, cx, cy, s, 47.0f, 74.0f, 52.0f, 74.0f, 1.5f);
        drawLine(sr, cx, cy, s, 52.0f, 64.0f, 56.0f, 64.0f, 1.5f);
        // --- LAYER: specular_highlights ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.90f);
        drawLine(sr, cx, cy, s, 56.0f, 36.0f, 38.0f, 96.0f, 2.2f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.80f);
        drawLine(sr, cx, cy, s, 58.0f, 24.0f, 58.0f, 36.0f, 1.5f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 64.0f, 78.0f, 2.5f);
        fillCircle(sr, cx, cy, s, 64.0f, 12.0f, 1.2f);
    }

    public static void drawHerbalTea(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // --- LAYER: background_glow ---
        sr.setColor(0.02f, 0.37f, 0.27f, 0.00f);
        fillEllipse(sr, cx, cy, s, 64.0f, 76.0f, 42.0f, 24.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.35f);
        fillEllipse(sr, cx, cy, s, 64.0f, 102.0f, 36.0f, 10.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.89f, 0.91f, 0.94f, 1.00f);
        fillEllipse(sr, cx, cy, s, 64.0f, 94.0f, 42.0f, 12.0f);
        sr.setColor(0.02f, 0.59f, 0.41f, 0.30f);
        fillEllipse(sr, cx, cy, s, 64.0f, 93.0f, 36.0f, 9.0f);
        sr.setColor(0.89f, 0.91f, 0.94f, 1.00f);
        fillTri(sr, cx, cy, s, 36.0f, 60.0f, 92.0f, 60.0f, 89.5f, 72.2f);
        fillTri(sr, cx, cy, s, 36.0f, 60.0f, 89.5f, 72.2f, 84.0f, 81.0f);
        fillTri(sr, cx, cy, s, 36.0f, 60.0f, 84.0f, 81.0f, 75.5f, 86.2f);
        fillTri(sr, cx, cy, s, 36.0f, 60.0f, 75.5f, 86.2f, 64.0f, 88.0f);
        fillTri(sr, cx, cy, s, 36.0f, 60.0f, 64.0f, 88.0f, 52.5f, 86.2f);
        fillTri(sr, cx, cy, s, 36.0f, 60.0f, 52.5f, 86.2f, 44.0f, 81.0f);
        fillTri(sr, cx, cy, s, 44.0f, 81.0f, 38.5f, 72.2f, 36.0f, 60.0f);
        sr.setColor(0.80f, 0.84f, 0.88f, 1.00f);
        fillEllipse(sr, cx, cy, s, 64.0f, 60.0f, 28.0f, 10.0f);
        sr.setColor(0.20f, 0.83f, 0.60f, 1.00f);
        fillEllipse(sr, cx, cy, s, 64.0f, 61.0f, 25.0f, 8.0f);
        // --- LAYER: details_and_relief ---
        sr.setColor(0.02f, 0.37f, 0.27f, 1.00f);
        fillTri(sr, cx, cy, s, 57.0f, 58.5f, 60.0f, 58.0f, 63.0f, 58.5f);
        fillTri(sr, cx, cy, s, 57.0f, 58.5f, 63.0f, 58.5f, 66.0f, 60.0f);
        fillTri(sr, cx, cy, s, 57.0f, 58.5f, 66.0f, 60.0f, 63.0f, 61.5f);
        fillTri(sr, cx, cy, s, 57.0f, 58.5f, 63.0f, 61.5f, 60.0f, 62.0f);
        fillTri(sr, cx, cy, s, 57.0f, 58.5f, 60.0f, 62.0f, 57.0f, 61.5f);
        fillTri(sr, cx, cy, s, 54.0f, 60.0f, 57.0f, 58.5f, 57.0f, 61.5f);
        fillTri(sr, cx, cy, s, 54.0f, 60.0f, 57.0f, 61.5f, 54.0f, 60.0f);
        sr.setColor(0.20f, 0.83f, 0.60f, 1.00f);
        drawLine(sr, cx, cy, s, 55.0f, 60.0f, 65.0f, 60.0f, 0.8f);
        sr.setColor(0.02f, 0.47f, 0.34f, 1.00f);
        fillTri(sr, cx, cy, s, 70.9f, 60.9f, 73.5f, 60.8f, 75.9f, 61.4f);
        fillTri(sr, cx, cy, s, 70.9f, 60.9f, 75.9f, 61.4f, 78.0f, 63.0f);
        fillTri(sr, cx, cy, s, 70.9f, 60.9f, 78.0f, 63.0f, 75.5f, 64.1f);
        fillTri(sr, cx, cy, s, 70.9f, 60.9f, 75.5f, 64.1f, 73.0f, 64.2f);
        fillTri(sr, cx, cy, s, 70.9f, 60.9f, 73.0f, 64.2f, 70.5f, 63.6f);
        fillTri(sr, cx, cy, s, 68.0f, 62.0f, 70.9f, 60.9f, 70.5f, 63.6f);
        fillTri(sr, cx, cy, s, 68.0f, 62.0f, 70.5f, 63.6f, 68.0f, 62.0f);
        sr.setColor(0.96f, 0.62f, 0.04f, 1.00f);
        drawLine(sr, cx, cy, s, 44.0f, 66.0f, 46.2f, 71.8f, 1.8f);
        drawLine(sr, cx, cy, s, 46.2f, 71.8f, 49.0f, 77.0f, 1.8f);
        drawLine(sr, cx, cy, s, 49.0f, 77.0f, 52.2f, 81.8f, 1.8f);
        drawLine(sr, cx, cy, s, 52.2f, 81.8f, 56.0f, 86.0f, 1.8f);
        sr.setColor(0.99f, 0.88f, 0.28f, 1.00f);
        fillCircle(sr, cx, cy, s, 56.0f, 86.0f, 1.5f);
        sr.setColor(0.43f, 0.91f, 0.72f, 0.60f);
        drawLine(sr, cx, cy, s, 52.0f, 50.0f, 50.6f, 45.1f, 2.0f);
        drawLine(sr, cx, cy, s, 50.6f, 45.1f, 50.5f, 40.5f, 2.0f);
        drawLine(sr, cx, cy, s, 50.5f, 40.5f, 51.6f, 36.1f, 2.0f);
        drawLine(sr, cx, cy, s, 51.6f, 36.1f, 54.0f, 32.0f, 2.0f);
        drawLine(sr, cx, cy, s, 54.0f, 32.0f, 55.5f, 28.0f, 2.0f);
        drawLine(sr, cx, cy, s, 55.5f, 28.0f, 56.0f, 24.0f, 2.0f);
        drawLine(sr, cx, cy, s, 56.0f, 24.0f, 55.5f, 20.0f, 2.0f);
        drawLine(sr, cx, cy, s, 55.5f, 20.0f, 54.0f, 16.0f, 2.0f);
        sr.setColor(0.43f, 0.91f, 0.72f, 0.75f);
        drawLine(sr, cx, cy, s, 64.0f, 48.0f, 66.2f, 43.0f, 2.5f);
        drawLine(sr, cx, cy, s, 66.2f, 43.0f, 67.0f, 38.0f, 2.5f);
        drawLine(sr, cx, cy, s, 67.0f, 38.0f, 66.2f, 33.0f, 2.5f);
        drawLine(sr, cx, cy, s, 66.2f, 33.0f, 64.0f, 28.0f, 2.5f);
        drawLine(sr, cx, cy, s, 64.0f, 28.0f, 62.6f, 23.1f, 2.5f);
        drawLine(sr, cx, cy, s, 62.6f, 23.1f, 62.5f, 18.5f, 2.5f);
        drawLine(sr, cx, cy, s, 62.5f, 18.5f, 63.6f, 14.1f, 2.5f);
        drawLine(sr, cx, cy, s, 63.6f, 14.1f, 66.0f, 10.0f, 2.5f);
        sr.setColor(0.43f, 0.91f, 0.72f, 0.50f);
        drawLine(sr, cx, cy, s, 76.0f, 52.0f, 78.2f, 47.1f, 1.8f);
        drawLine(sr, cx, cy, s, 78.2f, 47.1f, 79.0f, 42.5f, 1.8f);
        drawLine(sr, cx, cy, s, 79.0f, 42.5f, 78.2f, 38.1f, 1.8f);
        drawLine(sr, cx, cy, s, 78.2f, 38.1f, 76.0f, 34.0f, 1.8f);
        drawLine(sr, cx, cy, s, 76.0f, 34.0f, 74.6f, 30.0f, 1.8f);
        drawLine(sr, cx, cy, s, 74.6f, 30.0f, 74.5f, 26.0f, 1.8f);
        drawLine(sr, cx, cy, s, 74.5f, 26.0f, 75.6f, 22.0f, 1.8f);
        drawLine(sr, cx, cy, s, 75.6f, 22.0f, 78.0f, 18.0f, 1.8f);
        // --- LAYER: specular_highlights ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.90f);
        drawLine(sr, cx, cy, s, 38.0f, 62.0f, 39.9f, 71.0f, 2.0f);
        drawLine(sr, cx, cy, s, 39.9f, 71.0f, 43.5f, 78.0f, 2.0f);
        drawLine(sr, cx, cy, s, 43.5f, 78.0f, 48.9f, 83.0f, 2.0f);
        drawLine(sr, cx, cy, s, 48.9f, 83.0f, 56.0f, 86.0f, 2.0f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.85f);
        fillEllipse(sr, cx, cy, s, 74.0f, 59.0f, 2.5f, 1.0f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.80f);
        drawLine(sr, cx, cy, s, 32.0f, 94.0f, 48.0f, 97.8f, 1.2f);
        drawLine(sr, cx, cy, s, 48.0f, 97.8f, 64.0f, 99.0f, 1.2f);
        drawLine(sr, cx, cy, s, 64.0f, 99.0f, 80.0f, 97.8f, 1.2f);
        drawLine(sr, cx, cy, s, 80.0f, 97.8f, 96.0f, 94.0f, 1.2f);
    }

    public static void drawLineSpool(ShapeRenderer sr, float cx, float cy, float s) {
        // --- LAYER: background_glow ---
        sr.setColor(0.01f, 0.41f, 0.63f, 0.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 44.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.40f);
        fillEllipse(sr, cx, cy, s, 64.0f, 106.0f, 32.0f, 8.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.28f, 0.33f, 0.41f, 1.00f);
        fillEllipse(sr, cx, cy, s, 36.0f, 64.0f, 10.0f, 34.0f);
        fillEllipse(sr, cx, cy, s, 92.0f, 64.0f, 10.0f, 34.0f);
        sr.setColor(0.22f, 0.74f, 0.97f, 1.00f);
        fillQuad(sr, cx, cy, s, 36.0f, 34.0f, 92.0f, 34.0f, 92.0f, 94.0f, 36.0f, 94.0f);
        sr.setColor(0.20f, 0.25f, 0.33f, 1.00f);
        fillEllipse(sr, cx, cy, s, 92.0f, 64.0f, 8.0f, 32.0f);
        sr.setColor(0.06f, 0.09f, 0.16f, 1.00f);
        fillEllipse(sr, cx, cy, s, 92.0f, 64.0f, 4.0f, 16.0f);
        // --- LAYER: details_and_relief ---
        sr.setColor(0.73f, 0.90f, 0.99f, 1.00f);
        drawLine(sr, cx, cy, s, 38.0f, 42.0f, 90.0f, 42.0f, 1.8f);
        sr.setColor(0.01f, 0.41f, 0.63f, 1.00f);
        drawLine(sr, cx, cy, s, 38.0f, 50.0f, 90.0f, 50.0f, 1.5f);
        sr.setColor(0.73f, 0.90f, 0.99f, 1.00f);
        drawLine(sr, cx, cy, s, 38.0f, 58.0f, 90.0f, 58.0f, 1.8f);
        sr.setColor(0.01f, 0.41f, 0.63f, 1.00f);
        drawLine(sr, cx, cy, s, 38.0f, 66.0f, 90.0f, 66.0f, 1.5f);
        sr.setColor(0.73f, 0.90f, 0.99f, 1.00f);
        drawLine(sr, cx, cy, s, 38.0f, 74.0f, 90.0f, 74.0f, 1.8f);
        sr.setColor(0.01f, 0.41f, 0.63f, 1.00f);
        drawLine(sr, cx, cy, s, 38.0f, 82.0f, 90.0f, 82.0f, 1.5f);
        sr.setColor(0.58f, 0.64f, 0.72f, 1.00f);
        drawLine(sr, cx, cy, s, 92.0f, 36.0f, 92.0f, 92.0f, 3.0f);
        drawLine(sr, cx, cy, s, 86.0f, 64.0f, 98.0f, 64.0f, 3.0f);
        sr.setColor(0.96f, 0.62f, 0.04f, 1.00f);
        fillQuad(sr, cx, cy, s, 74.0f, 56.0f, 80.0f, 56.0f, 80.0f, 72.0f, 74.0f, 72.0f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        fillCircle(sr, cx, cy, s, 77.0f, 64.0f, 1.5f);
        // --- LAYER: specular_highlights ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.90f);
        drawLine(sr, cx, cy, s, 30.0f, 38.0f, 27.8f, 51.0f, 2.0f);
        drawLine(sr, cx, cy, s, 27.8f, 51.0f, 27.0f, 64.0f, 2.0f);
        drawLine(sr, cx, cy, s, 27.0f, 64.0f, 27.8f, 77.0f, 2.0f);
        drawLine(sr, cx, cy, s, 27.8f, 77.0f, 30.0f, 90.0f, 2.0f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.80f);
        drawLine(sr, cx, cy, s, 42.0f, 38.0f, 84.0f, 38.0f, 1.2f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 92.0f, 64.0f, 1.5f);
    }

    public static void drawCryoSpray(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // --- LAYER: background_glow ---
        sr.setColor(0.65f, 0.95f, 0.99f, 0.20f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 44.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.40f);
        fillEllipse(sr, cx, cy, s, 56.0f, 112.0f, 20.0f, 6.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.01f, 0.52f, 0.78f, 1.00f);
        fillQuad(sr, cx, cy, s, 38.0f, 38.0f, 74.0f, 38.0f, 74.0f, 106.0f, 38.0f, 106.0f);
        fillEllipse(sr, cx, cy, s, 56.0f, 106.0f, 18.0f, 5.0f);
        sr.setColor(0.80f, 0.84f, 0.88f, 1.00f);
        fillEllipse(sr, cx, cy, s, 56.0f, 38.0f, 18.0f, 5.0f);
        sr.setColor(0.01f, 0.52f, 0.78f, 1.00f);
        fillQuad(sr, cx, cy, s, 50.0f, 24.0f, 62.0f, 24.0f, 62.0f, 36.0f, 50.0f, 36.0f);
        sr.setColor(0.89f, 0.91f, 0.94f, 1.00f);
        fillQuad(sr, cx, cy, s, 62.0f, 26.0f, 74.0f, 22.0f, 72.0f, 30.0f, 62.0f, 30.0f);
        // --- LAYER: details_and_relief ---
        sr.setColor(Color.WHITE);
        drawLine(sr, cx, cy, s, 56.0f, 62.0f, 56.0f, 82.0f, 2.5f);
        drawLine(sr, cx, cy, s, 46.0f, 72.0f, 66.0f, 72.0f, 2.5f);
        drawLine(sr, cx, cy, s, 49.0f, 65.0f, 63.0f, 79.0f, 2.0f);
        drawLine(sr, cx, cy, s, 49.0f, 79.0f, 63.0f, 65.0f, 2.0f);
        sr.setColor(0.65f, 0.95f, 0.99f, 1.00f);
        fillCircle(sr, cx, cy, s, 56.0f, 72.0f, 3.0f);
        sr.setColor(0.88f, 0.95f, 1.00f, 0.85f);
        fillTri(sr, cx, cy, s, 84.0f, 28.0f, 74.0f, 24.0f, 81.9f, 20.1f);
        fillTri(sr, cx, cy, s, 84.0f, 28.0f, 81.9f, 20.1f, 89.5f, 18.5f);
        fillTri(sr, cx, cy, s, 84.0f, 28.0f, 89.5f, 18.5f, 96.9f, 19.1f);
        fillTri(sr, cx, cy, s, 84.0f, 28.0f, 96.9f, 19.1f, 104.0f, 22.0f);
        fillTri(sr, cx, cy, s, 84.0f, 28.0f, 104.0f, 22.0f, 99.0f, 25.4f);
        fillTri(sr, cx, cy, s, 84.0f, 28.0f, 99.0f, 25.4f, 94.0f, 27.5f);
        fillTri(sr, cx, cy, s, 94.0f, 27.5f, 89.0f, 28.4f, 84.0f, 28.0f);
        sr.setColor(0.73f, 0.90f, 0.99f, 0.70f);
        fillCircle(sr, cx, cy, s, 92.0f, 18.0f, 5.0f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.80f);
        fillCircle(sr, cx, cy, s, 106.0f, 22.0f, 6.0f);
        sr.setColor(0.88f, 0.95f, 1.00f, 0.60f);
        fillCircle(sr, cx, cy, s, 100.0f, 32.0f, 4.5f);
        sr.setColor(0.88f, 0.95f, 1.00f, 1.00f);
        fillTri(sr, cx, cy, s, 38.0f, 102.0f, 34.0f, 92.0f, 42.0f, 98.0f);
        fillTri(sr, cx, cy, s, 74.0f, 102.0f, 78.0f, 90.0f, 70.0f, 96.0f);
        // --- LAYER: specular_highlights ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.90f);
        drawLine(sr, cx, cy, s, 42.0f, 40.0f, 42.0f, 100.0f, 2.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 56.0f, 72.0f, 1.2f);
        fillCircle(sr, cx, cy, s, 90.0f, 20.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 104.0f, 24.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 80.0f, 24.0f, 1.0f);
    }

    public static void drawWhetstone(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // --- LAYER: background_glow ---
        sr.setColor(0.20f, 0.25f, 0.33f, 0.00f);
        fillEllipse(sr, cx, cy, s, 64.0f, 68.0f, 46.0f, 26.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.35f);
        fillQuad(sr, cx, cy, s, 20.0f, 94.0f, 88.0f, 112.0f, 112.0f, 86.0f, 44.0f, 70.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.58f, 0.64f, 0.72f, 1.00f);
        fillQuad(sr, cx, cy, s, 56.0f, 36.0f, 106.0f, 52.0f, 74.0f, 74.0f, 24.0f, 56.0f);
        sr.setColor(0.39f, 0.45f, 0.55f, 1.00f);
        fillQuad(sr, cx, cy, s, 24.0f, 56.0f, 74.0f, 74.0f, 74.0f, 84.0f, 24.0f, 66.0f);
        sr.setColor(0.20f, 0.25f, 0.33f, 1.00f);
        fillQuad(sr, cx, cy, s, 24.0f, 66.0f, 74.0f, 84.0f, 74.0f, 96.0f, 24.0f, 78.0f);
        sr.setColor(0.28f, 0.33f, 0.41f, 1.00f);
        fillQuad(sr, cx, cy, s, 74.0f, 74.0f, 106.0f, 52.0f, 106.0f, 62.0f, 74.0f, 84.0f);
        sr.setColor(0.06f, 0.09f, 0.16f, 1.00f);
        fillQuad(sr, cx, cy, s, 74.0f, 84.0f, 106.0f, 62.0f, 106.0f, 74.0f, 74.0f, 96.0f);
        // --- LAYER: details_and_relief ---
        sr.setColor(0.96f, 0.62f, 0.04f, 1.00f);
        drawLine(sr, cx, cy, s, 24.0f, 66.0f, 74.0f, 84.0f, 1.8f);
        drawLine(sr, cx, cy, s, 74.0f, 84.0f, 106.0f, 62.0f, 1.8f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.70f);
        drawLine(sr, cx, cy, s, 42.0f, 52.0f, 86.0f, 66.0f, 2.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillTri(sr, cx, cy, s, 86.0f, 64.0f, 92.0f, 58.0f, 88.0f, 68.0f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        fillTri(sr, cx, cy, s, 94.0f, 62.0f, 100.0f, 60.0f, 96.0f, 66.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillCircle(sr, cx, cy, s, 82.0f, 56.0f, 1.5f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 98.0f, 54.0f, 1.2f);
        // --- LAYER: specular_highlights ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.95f);
        drawLine(sr, cx, cy, s, 24.0f, 56.0f, 74.0f, 74.0f, 2.5f);
        drawLine(sr, cx, cy, s, 74.0f, 74.0f, 106.0f, 52.0f, 2.0f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 74.0f, 74.0f, 2.0f);
        fillCircle(sr, cx, cy, s, 56.0f, 36.0f, 1.2f);
    }

    public static void drawTreasureMagnet(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // --- LAYER: background_glow ---
        sr.setColor(0.94f, 0.27f, 0.27f, 0.20f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 44.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.35f);
        fillEllipse(sr, cx, cy, s, 64.0f, 110.0f, 34.0f, 8.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        fillTri(sr, cx, cy, s, 46.0f, 84.0f, 28.0f, 84.0f, 28.0f, 54.0f);
        fillTri(sr, cx, cy, s, 46.0f, 84.0f, 28.0f, 54.0f, 30.2f, 39.1f);
        fillTri(sr, cx, cy, s, 46.0f, 84.0f, 30.2f, 39.1f, 37.0f, 28.5f);
        fillTri(sr, cx, cy, s, 37.0f, 28.5f, 48.2f, 22.1f, 64.0f, 20.0f);
        fillTri(sr, cx, cy, s, 37.0f, 28.5f, 64.0f, 20.0f, 79.8f, 22.1f);
        fillTri(sr, cx, cy, s, 37.0f, 28.5f, 79.8f, 22.1f, 91.0f, 28.5f);
        fillTri(sr, cx, cy, s, 37.0f, 28.5f, 91.0f, 28.5f, 97.8f, 39.1f);
        fillTri(sr, cx, cy, s, 97.8f, 39.1f, 100.0f, 54.0f, 100.0f, 84.0f);
        fillTri(sr, cx, cy, s, 97.8f, 39.1f, 100.0f, 84.0f, 82.0f, 84.0f);
        fillTri(sr, cx, cy, s, 97.8f, 39.1f, 82.0f, 84.0f, 82.0f, 54.0f);
        fillTri(sr, cx, cy, s, 97.8f, 39.1f, 82.0f, 54.0f, 80.9f, 47.0f);
        fillTri(sr, cx, cy, s, 97.8f, 39.1f, 80.9f, 47.0f, 77.5f, 42.0f);
        fillTri(sr, cx, cy, s, 97.8f, 39.1f, 77.5f, 42.0f, 71.9f, 39.0f);
        fillTri(sr, cx, cy, s, 37.0f, 28.5f, 97.8f, 39.1f, 71.9f, 39.0f);
        fillTri(sr, cx, cy, s, 37.0f, 28.5f, 71.9f, 39.0f, 64.0f, 38.0f);
        fillTri(sr, cx, cy, s, 37.0f, 28.5f, 64.0f, 38.0f, 56.1f, 39.0f);
        fillTri(sr, cx, cy, s, 37.0f, 28.5f, 56.1f, 39.0f, 50.5f, 42.0f);
        fillTri(sr, cx, cy, s, 37.0f, 28.5f, 50.5f, 42.0f, 47.1f, 47.0f);
        fillTri(sr, cx, cy, s, 37.0f, 28.5f, 47.1f, 47.0f, 46.0f, 54.0f);
        fillTri(sr, cx, cy, s, 37.0f, 28.5f, 46.0f, 54.0f, 46.0f, 84.0f);
        sr.setColor(0.89f, 0.91f, 0.94f, 1.00f);
        fillQuad(sr, cx, cy, s, 28.0f, 80.0f, 46.0f, 80.0f, 46.0f, 98.0f, 28.0f, 98.0f);
        sr.setColor(0.58f, 0.64f, 0.72f, 1.00f);
        fillQuad(sr, cx, cy, s, 28.0f, 94.0f, 46.0f, 94.0f, 46.0f, 98.0f, 28.0f, 98.0f);
        sr.setColor(0.89f, 0.91f, 0.94f, 1.00f);
        fillQuad(sr, cx, cy, s, 82.0f, 80.0f, 100.0f, 80.0f, 100.0f, 98.0f, 82.0f, 98.0f);
        sr.setColor(0.58f, 0.64f, 0.72f, 1.00f);
        fillQuad(sr, cx, cy, s, 82.0f, 94.0f, 100.0f, 94.0f, 100.0f, 98.0f, 82.0f, 98.0f);
        // --- LAYER: details_and_relief ---
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        drawLine(sr, cx, cy, s, 33.0f, 84.0f, 33.0f, 92.0f, 2.0f);
        drawLine(sr, cx, cy, s, 33.0f, 84.0f, 41.0f, 92.0f, 2.0f);
        drawLine(sr, cx, cy, s, 41.0f, 84.0f, 41.0f, 92.0f, 2.0f);
        sr.setColor(0.15f, 0.39f, 0.92f, 1.00f);
        drawLine(sr, cx, cy, s, 87.0f, 92.0f, 88.8f, 91.8f, 2.0f);
        drawLine(sr, cx, cy, s, 88.8f, 91.8f, 90.0f, 91.0f, 2.0f);
        drawLine(sr, cx, cy, s, 90.0f, 91.0f, 90.8f, 89.8f, 2.0f);
        drawLine(sr, cx, cy, s, 90.8f, 89.8f, 91.0f, 88.0f, 2.0f);
        drawLine(sr, cx, cy, s, 91.0f, 88.0f, 89.2f, 87.8f, 2.0f);
        drawLine(sr, cx, cy, s, 89.2f, 87.8f, 88.0f, 87.0f, 2.0f);
        drawLine(sr, cx, cy, s, 88.0f, 87.0f, 87.2f, 85.8f, 2.0f);
        drawLine(sr, cx, cy, s, 87.2f, 85.8f, 87.0f, 84.0f, 2.0f);
        drawLine(sr, cx, cy, s, 87.0f, 84.0f, 88.8f, 84.0f, 2.0f);
        drawLine(sr, cx, cy, s, 88.8f, 84.0f, 90.0f, 84.0f, 2.0f);
        drawLine(sr, cx, cy, s, 90.0f, 84.0f, 90.8f, 84.0f, 2.0f);
        drawLine(sr, cx, cy, s, 90.8f, 84.0f, 91.0f, 84.0f, 2.0f);
        sr.setColor(0.22f, 0.74f, 0.97f, 1.00f);
        drawLine(sr, cx, cy, s, 46.0f, 90.0f, 55.0f, 94.5f, 2.2f);
        drawLine(sr, cx, cy, s, 55.0f, 94.5f, 64.0f, 96.0f, 2.2f);
        drawLine(sr, cx, cy, s, 64.0f, 96.0f, 73.0f, 94.5f, 2.2f);
        drawLine(sr, cx, cy, s, 73.0f, 94.5f, 82.0f, 90.0f, 2.2f);
        drawLine(sr, cx, cy, s, 46.0f, 96.0f, 55.0f, 101.2f, 1.8f);
        drawLine(sr, cx, cy, s, 55.0f, 101.2f, 64.0f, 103.0f, 1.8f);
        drawLine(sr, cx, cy, s, 64.0f, 103.0f, 73.0f, 101.2f, 1.8f);
        drawLine(sr, cx, cy, s, 73.0f, 101.2f, 82.0f, 96.0f, 1.8f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 98.0f, 6.0f);
        sr.setColor(1.00f, 0.94f, 0.54f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 98.0f, 4.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillCircle(sr, cx, cy, s, 54.0f, 106.0f, 4.0f);
        fillCircle(sr, cx, cy, s, 74.0f, 106.0f, 4.0f);
        // --- LAYER: specular_highlights ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.90f);
        drawLine(sr, cx, cy, s, 32.0f, 80.0f, 32.0f, 54.0f, 2.5f);
        drawLine(sr, cx, cy, s, 32.0f, 54.0f, 34.0f, 40.9f, 2.5f);
        drawLine(sr, cx, cy, s, 34.0f, 40.9f, 40.0f, 31.5f, 2.5f);
        drawLine(sr, cx, cy, s, 40.0f, 31.5f, 50.0f, 25.9f, 2.5f);
        drawLine(sr, cx, cy, s, 50.0f, 25.9f, 64.0f, 24.0f, 2.5f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 30.0f, 82.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 84.0f, 82.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 63.0f, 97.0f, 1.2f);
    }

    public static void drawSonarGoggles(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // --- LAYER: background_glow ---
        sr.setColor(0.02f, 0.37f, 0.27f, 0.15f);
        fillEllipse(sr, cx, cy, s, 64.0f, 64.0f, 46.0f, 26.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.40f);
        fillEllipse(sr, cx, cy, s, 64.0f, 98.0f, 40.0f, 8.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.12f, 0.16f, 0.23f, 1.00f);
        drawLine(sr, cx, cy, s, 20.0f, 62.0f, 15.4f, 63.4f, 5.0f);
        drawLine(sr, cx, cy, s, 15.4f, 63.4f, 11.5f, 65.5f, 5.0f);
        drawLine(sr, cx, cy, s, 11.5f, 65.5f, 8.4f, 68.4f, 5.0f);
        drawLine(sr, cx, cy, s, 8.4f, 68.4f, 6.0f, 72.0f, 5.0f);
        drawLine(sr, cx, cy, s, 6.0f, 72.0f, 8.4f, 73.6f, 5.0f);
        drawLine(sr, cx, cy, s, 8.4f, 73.6f, 11.5f, 74.5f, 5.0f);
        drawLine(sr, cx, cy, s, 11.5f, 74.5f, 15.4f, 74.6f, 5.0f);
        drawLine(sr, cx, cy, s, 15.4f, 74.6f, 20.0f, 74.0f, 5.0f);
        drawLine(sr, cx, cy, s, 108.0f, 62.0f, 112.6f, 63.4f, 5.0f);
        drawLine(sr, cx, cy, s, 112.6f, 63.4f, 116.5f, 65.5f, 5.0f);
        drawLine(sr, cx, cy, s, 116.5f, 65.5f, 119.6f, 68.4f, 5.0f);
        drawLine(sr, cx, cy, s, 119.6f, 68.4f, 122.0f, 72.0f, 5.0f);
        drawLine(sr, cx, cy, s, 122.0f, 72.0f, 119.6f, 73.6f, 5.0f);
        drawLine(sr, cx, cy, s, 119.6f, 73.6f, 116.5f, 74.5f, 5.0f);
        drawLine(sr, cx, cy, s, 116.5f, 74.5f, 112.6f, 74.6f, 5.0f);
        drawLine(sr, cx, cy, s, 112.6f, 74.6f, 108.0f, 74.0f, 5.0f);
        sr.setColor(0.06f, 0.09f, 0.16f, 1.00f);
        fillQuad(sr, cx, cy, s, 22.0f, 44.0f, 106.0f, 44.0f, 106.0f, 84.0f, 22.0f, 84.0f);
        sr.setColor(0.92f, 0.70f, 0.03f, 1.00f);
        fillCircle(sr, cx, cy, s, 44.0f, 64.0f, 18.0f);
        fillCircle(sr, cx, cy, s, 84.0f, 64.0f, 18.0f);
        sr.setColor(0.06f, 0.73f, 0.51f, 1.00f);
        fillCircle(sr, cx, cy, s, 44.0f, 64.0f, 14.0f);
        fillCircle(sr, cx, cy, s, 84.0f, 64.0f, 14.0f);
        sr.setColor(0.92f, 0.70f, 0.03f, 1.00f);
        fillQuad(sr, cx, cy, s, 58.0f, 60.0f, 70.0f, 60.0f, 70.0f, 68.0f, 58.0f, 68.0f);
        // --- LAYER: details_and_relief ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.70f);
        drawLine(sr, cx, cy, s, 44.0f, 52.0f, 44.0f, 76.0f, 0.8f);
        drawLine(sr, cx, cy, s, 32.0f, 64.0f, 56.0f, 64.0f, 0.8f);
        drawLine(sr, cx, cy, s, 84.0f, 52.0f, 84.0f, 76.0f, 0.8f);
        drawLine(sr, cx, cy, s, 72.0f, 64.0f, 96.0f, 64.0f, 0.8f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillEllipse(sr, cx, cy, s, 86.0f, 62.0f, 3.5f, 1.8f);
        fillTri(sr, cx, cy, s, 82.0f, 62.0f, 79.0f, 59.0f, 79.0f, 65.0f);
        // --- LAYER: specular_highlights ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.90f);
        drawLine(sr, cx, cy, s, 36.0f, 54.0f, 48.0f, 70.0f, 2.5f);
        drawLine(sr, cx, cy, s, 76.0f, 54.0f, 88.0f, 70.0f, 2.5f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 34.0f, 52.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 74.0f, 52.0f, 1.5f);
    }

    public static void drawFishingNet(ShapeRenderer sr, float cx, float cy, float s) {
        // --- LAYER: background_glow ---
        sr.setColor(0.06f, 0.09f, 0.16f, 0.00f);
        fillEllipse(sr, cx, cy, s, 64.0f, 68.0f, 46.0f, 32.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.40f);
        fillEllipse(sr, cx, cy, s, 64.0f, 106.0f, 42.0f, 10.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.01f, 0.41f, 0.63f, 0.35f);
        fillTri(sr, cx, cy, s, 77.0f, 43.5f, 88.0f, 62.0f, 97.0f, 79.5f);
        fillTri(sr, cx, cy, s, 77.0f, 43.5f, 97.0f, 79.5f, 104.0f, 96.0f);
        fillTri(sr, cx, cy, s, 77.0f, 43.5f, 104.0f, 96.0f, 84.0f, 99.8f);
        fillTri(sr, cx, cy, s, 77.0f, 43.5f, 84.0f, 99.8f, 64.0f, 101.0f);
        fillTri(sr, cx, cy, s, 77.0f, 43.5f, 64.0f, 101.0f, 44.0f, 99.8f);
        fillTri(sr, cx, cy, s, 77.0f, 43.5f, 44.0f, 99.8f, 24.0f, 96.0f);
        fillTri(sr, cx, cy, s, 77.0f, 43.5f, 24.0f, 96.0f, 31.0f, 79.5f);
        fillTri(sr, cx, cy, s, 77.0f, 43.5f, 31.0f, 79.5f, 40.0f, 62.0f);
        fillTri(sr, cx, cy, s, 77.0f, 43.5f, 40.0f, 62.0f, 51.0f, 43.5f);
        fillTri(sr, cx, cy, s, 64.0f, 24.0f, 77.0f, 43.5f, 51.0f, 43.5f);
        fillTri(sr, cx, cy, s, 64.0f, 24.0f, 51.0f, 43.5f, 64.0f, 24.0f);
        sr.setColor(0.01f, 0.52f, 0.78f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 24.0f, 77.0f, 43.5f, 2.5f);
        drawLine(sr, cx, cy, s, 77.0f, 43.5f, 88.0f, 62.0f, 2.5f);
        drawLine(sr, cx, cy, s, 88.0f, 62.0f, 97.0f, 79.5f, 2.5f);
        drawLine(sr, cx, cy, s, 97.0f, 79.5f, 104.0f, 96.0f, 2.5f);
        drawLine(sr, cx, cy, s, 104.0f, 96.0f, 84.0f, 99.8f, 2.5f);
        drawLine(sr, cx, cy, s, 84.0f, 99.8f, 64.0f, 101.0f, 2.5f);
        drawLine(sr, cx, cy, s, 64.0f, 101.0f, 44.0f, 99.8f, 2.5f);
        drawLine(sr, cx, cy, s, 44.0f, 99.8f, 24.0f, 96.0f, 2.5f);
        drawLine(sr, cx, cy, s, 24.0f, 96.0f, 31.0f, 79.5f, 2.5f);
        drawLine(sr, cx, cy, s, 31.0f, 79.5f, 40.0f, 62.0f, 2.5f);
        drawLine(sr, cx, cy, s, 40.0f, 62.0f, 51.0f, 43.5f, 2.5f);
        drawLine(sr, cx, cy, s, 51.0f, 43.5f, 64.0f, 24.0f, 2.5f);
        sr.setColor(0.79f, 0.54f, 0.02f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 20.0f, 8.0f);
        sr.setColor(0.06f, 0.09f, 0.16f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 20.0f, 4.5f);
        // --- LAYER: details_and_relief ---
        sr.setColor(0.22f, 0.74f, 0.97f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 24.0f, 26.0f, 96.0f, 1.4f);
        drawLine(sr, cx, cy, s, 64.0f, 24.0f, 42.0f, 100.0f, 1.4f);
        drawLine(sr, cx, cy, s, 64.0f, 24.0f, 56.0f, 103.0f, 1.4f);
        drawLine(sr, cx, cy, s, 64.0f, 24.0f, 72.0f, 103.0f, 1.4f);
        drawLine(sr, cx, cy, s, 64.0f, 24.0f, 86.0f, 100.0f, 1.4f);
        drawLine(sr, cx, cy, s, 64.0f, 24.0f, 102.0f, 96.0f, 1.4f);
        drawLine(sr, cx, cy, s, 46.0f, 54.0f, 55.0f, 56.2f, 1.4f);
        drawLine(sr, cx, cy, s, 55.0f, 56.2f, 64.0f, 57.0f, 1.4f);
        drawLine(sr, cx, cy, s, 64.0f, 57.0f, 73.0f, 56.2f, 1.4f);
        drawLine(sr, cx, cy, s, 73.0f, 56.2f, 82.0f, 54.0f, 1.4f);
        drawLine(sr, cx, cy, s, 36.0f, 72.0f, 50.0f, 75.0f, 1.4f);
        drawLine(sr, cx, cy, s, 50.0f, 75.0f, 64.0f, 76.0f, 1.4f);
        drawLine(sr, cx, cy, s, 64.0f, 76.0f, 78.0f, 75.0f, 1.4f);
        drawLine(sr, cx, cy, s, 78.0f, 75.0f, 92.0f, 72.0f, 1.4f);
        drawLine(sr, cx, cy, s, 28.0f, 88.0f, 46.0f, 91.8f, 1.6f);
        drawLine(sr, cx, cy, s, 46.0f, 91.8f, 64.0f, 93.0f, 1.6f);
        drawLine(sr, cx, cy, s, 64.0f, 93.0f, 82.0f, 91.8f, 1.6f);
        drawLine(sr, cx, cy, s, 82.0f, 91.8f, 100.0f, 88.0f, 1.6f);
        sr.setColor(0.28f, 0.33f, 0.41f, 1.00f);
        fillCircle(sr, cx, cy, s, 24.0f, 96.0f, 4.0f);
        fillCircle(sr, cx, cy, s, 38.0f, 100.0f, 4.0f);
        fillCircle(sr, cx, cy, s, 52.0f, 103.0f, 4.0f);
        fillCircle(sr, cx, cy, s, 64.0f, 104.0f, 4.0f);
        fillCircle(sr, cx, cy, s, 76.0f, 103.0f, 4.0f);
        fillCircle(sr, cx, cy, s, 90.0f, 100.0f, 4.0f);
        fillCircle(sr, cx, cy, s, 104.0f, 96.0f, 4.0f);
        // --- LAYER: specular_highlights ---
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 23.0f, 95.0f, 1.2f);
        fillCircle(sr, cx, cy, s, 51.0f, 102.0f, 1.2f);
        fillCircle(sr, cx, cy, s, 63.0f, 103.0f, 1.2f);
        fillCircle(sr, cx, cy, s, 75.0f, 102.0f, 1.2f);
        fillCircle(sr, cx, cy, s, 103.0f, 95.0f, 1.2f);
        fillCircle(sr, cx, cy, s, 62.0f, 16.0f, 1.5f);
        sr.setColor(1.00f, 1.00f, 1.00f, 0.80f);
        drawLine(sr, cx, cy, s, 52.0f, 44.0f, 58.0f, 45.5f, 1.2f);
        drawLine(sr, cx, cy, s, 58.0f, 45.5f, 64.0f, 46.0f, 1.2f);
        drawLine(sr, cx, cy, s, 64.0f, 46.0f, 70.0f, 45.5f, 1.2f);
        drawLine(sr, cx, cy, s, 70.0f, 45.5f, 76.0f, 44.0f, 1.2f);
    }

    public static void drawGoldTalisman(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // --- LAYER: background_glow ---
        sr.setColor(0.86f, 0.15f, 0.15f, 0.20f);
        fillCircle(sr, cx, cy, s, 64.0f, 64.0f, 46.0f);
        sr.setColor(0.00f, 0.00f, 0.00f, 0.35f);
        fillQuad(sr, cx, cy, s, 46.0f, 24.0f, 82.0f, 24.0f, 86.0f, 112.0f, 42.0f, 112.0f);
        // --- LAYER: base_form ---
        sr.setColor(0.02f, 0.59f, 0.41f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 16.0f, 6.0f);
        sr.setColor(0.20f, 0.83f, 0.60f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 16.0f, 3.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 16.0f, 64.0f, 28.0f, 3.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillQuad(sr, cx, cy, s, 46.0f, 26.0f, 82.0f, 26.0f, 82.0f, 108.0f, 46.0f, 108.0f);
        // --- LAYER: details_and_relief ---
        sr.setColor(0.60f, 0.11f, 0.11f, 1.00f);
        fillQuad(sr, cx, cy, s, 54.0f, 36.0f, 64.0f, 32.0f, 74.0f, 36.0f, 64.0f, 40.0f);
        sr.setColor(0.99f, 0.88f, 0.28f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 36.0f, 2.5f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        drawLine(sr, cx, cy, s, 64.0f, 42.0f, 64.0f, 62.0f, 3.0f);
        drawLine(sr, cx, cy, s, 56.0f, 48.0f, 60.0f, 49.5f, 2.2f);
        drawLine(sr, cx, cy, s, 60.0f, 49.5f, 64.0f, 50.0f, 2.2f);
        drawLine(sr, cx, cy, s, 64.0f, 50.0f, 68.0f, 49.5f, 2.2f);
        drawLine(sr, cx, cy, s, 68.0f, 49.5f, 72.0f, 48.0f, 2.2f);
        drawLine(sr, cx, cy, s, 58.0f, 56.0f, 61.0f, 57.5f, 2.2f);
        drawLine(sr, cx, cy, s, 61.0f, 57.5f, 64.0f, 58.0f, 2.2f);
        drawLine(sr, cx, cy, s, 64.0f, 58.0f, 67.0f, 57.5f, 2.2f);
        drawLine(sr, cx, cy, s, 67.0f, 57.5f, 70.0f, 56.0f, 2.2f);
        sr.setColor(0.73f, 0.11f, 0.11f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 72.0f, 7.0f);
        sr.setColor(0.98f, 0.80f, 0.08f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 72.0f, 4.5f);
        sr.setColor(0.73f, 0.11f, 0.11f, 1.00f);
        fillCircle(sr, cx, cy, s, 64.0f, 72.0f, 2.0f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1.00f);
        drawLine(sr, cx, cy, s, 58.0f, 84.0f, 61.0f, 82.5f, 2.2f);
        drawLine(sr, cx, cy, s, 61.0f, 82.5f, 64.0f, 82.0f, 2.2f);
        drawLine(sr, cx, cy, s, 64.0f, 82.0f, 67.0f, 82.5f, 2.2f);
        drawLine(sr, cx, cy, s, 67.0f, 82.5f, 70.0f, 84.0f, 2.2f);
        drawLine(sr, cx, cy, s, 70.0f, 84.0f, 67.0f, 86.0f, 2.2f);
        drawLine(sr, cx, cy, s, 67.0f, 86.0f, 64.0f, 88.0f, 2.2f);
        drawLine(sr, cx, cy, s, 64.0f, 88.0f, 61.0f, 90.0f, 2.2f);
        drawLine(sr, cx, cy, s, 61.0f, 90.0f, 58.0f, 92.0f, 2.2f);
        drawLine(sr, cx, cy, s, 58.0f, 92.0f, 61.0f, 93.8f, 2.2f);
        drawLine(sr, cx, cy, s, 61.0f, 93.8f, 64.0f, 95.0f, 2.2f);
        drawLine(sr, cx, cy, s, 64.0f, 95.0f, 67.0f, 95.8f, 2.2f);
        drawLine(sr, cx, cy, s, 67.0f, 95.8f, 70.0f, 96.0f, 2.2f);
        sr.setColor(0.73f, 0.11f, 0.11f, 1.00f);
        fillTri(sr, cx, cy, s, 46.0f, 108.0f, 46.0f, 104.0f, 82.0f, 104.0f);
        fillTri(sr, cx, cy, s, 82.0f, 104.0f, 82.0f, 108.0f, 73.0f, 102.0f);
        fillTri(sr, cx, cy, s, 73.0f, 102.0f, 64.0f, 108.0f, 55.0f, 102.0f);
        fillTri(sr, cx, cy, s, 82.0f, 104.0f, 73.0f, 102.0f, 55.0f, 102.0f);
        fillTri(sr, cx, cy, s, 82.0f, 104.0f, 55.0f, 102.0f, 46.0f, 108.0f);
        // --- LAYER: specular_highlights ---
        sr.setColor(1.00f, 1.00f, 1.00f, 0.90f);
        drawLine(sr, cx, cy, s, 50.0f, 30.0f, 50.0f, 104.0f, 1.5f);
        sr.setColor(Color.WHITE);
        fillCircle(sr, cx, cy, s, 63.0f, 14.0f, 1.5f);
        fillCircle(sr, cx, cy, s, 63.0f, 71.0f, 1.2f);
        fillTri(sr, cx, cy, s, 64.0f, 28.0f, 66.0f, 32.0f, 62.0f, 32.0f);
    }

    // =========================================================================
    // HLW-05: 5 HALLOWEEN VECTOR PROPS
    // =========================================================================

    public static void drawHalloweenCandy(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // --- LAYER 1: background_glow ---
        sr.setColor(0.66f, 0.33f, 0.97f, 0.22f);
        fillCircle(sr, cx, cy, s, 64f, 64f, 52f);
        sr.setColor(0f, 0f, 0f, 0.32f);
        fillEllipse(sr, cx, cy, s, 64f, 104f, 34f, 9f);

        // --- LAYER 2: base_form ---
        // Left Twisted Wrapper Fan
        sr.setColor(0.58f, 0.20f, 0.92f, 1f);
        fillQuad(sr, cx, cy, s, 44f, 64f, 16f, 42f, 22f, 64f, 16f, 86f);
        sr.setColor(0.49f, 0.13f, 0.81f, 1f);
        fillQuad(sr, cx, cy, s, 44f, 64f, 20f, 52f, 24f, 64f, 20f, 76f);

        // Right Twisted Wrapper Fan
        sr.setColor(0.58f, 0.20f, 0.92f, 1f);
        fillQuad(sr, cx, cy, s, 84f, 64f, 112f, 42f, 106f, 64f, 112f, 86f);
        sr.setColor(0.49f, 0.13f, 0.81f, 1f);
        fillQuad(sr, cx, cy, s, 84f, 64f, 108f, 52f, 104f, 64f, 108f, 76f);

        // Gold Ribbon Ties
        sr.setColor(0.98f, 0.80f, 0.08f, 1f);
        fillQuad(sr, cx, cy, s, 41f, 54f, 47f, 54f, 47f, 74f, 41f, 74f);
        fillQuad(sr, cx, cy, s, 81f, 54f, 87f, 54f, 87f, 74f, 81f, 74f);

        // Round Hard Candy Core
        sr.setColor(0.98f, 0.45f, 0.09f, 1f);
        fillCircle(sr, cx, cy, s, 64f, 64f, 28f);
        sr.setColor(0.76f, 0.25f, 0.05f, 1f);
        fillCircle(sr, cx, cy, s, 64f, 64f, 26f);
        sr.setColor(0.99f, 0.58f, 0.14f, 1f);
        fillCircle(sr, cx, cy, s, 63f, 63f, 25f);

        // --- LAYER 3: details_and_relief ---
        // Curved Violet Sugar Swirls
        sr.setColor(0.49f, 0.13f, 0.81f, 1f);
        drawLine(sr, cx, cy, s, 44f, 50f, 56f, 42f, 4.5f);
        drawLine(sr, cx, cy, s, 56f, 42f, 70f, 44f, 4.5f);
        drawLine(sr, cx, cy, s, 70f, 44f, 90f, 56f, 4.5f);
        drawLine(sr, cx, cy, s, 40f, 68f, 52f, 78f, 4.5f);
        drawLine(sr, cx, cy, s, 52f, 78f, 66f, 78f, 4.5f);
        drawLine(sr, cx, cy, s, 66f, 78f, 88f, 70f, 4.5f);

        // Gold Honey Streaks
        sr.setColor(0.99f, 0.94f, 0.54f, 1f);
        drawLine(sr, cx, cy, s, 46f, 58f, 62f, 64f, 2.5f);
        drawLine(sr, cx, cy, s, 62f, 64f, 82f, 58f, 2.5f);

        // Spectral Sparkle
        sr.setColor(1f, 1f, 1f, 0.90f);
        fillQuad(sr, cx, cy, s, 64f, 48f, 68f, 54f, 64f, 60f, 60f, 54f);

        // --- LAYER 4: specular_highlights ---
        sr.setColor(1f, 1f, 1f, 0.75f);
        drawLine(sr, cx, cy, s, 48f, 50f, 62f, 44f, 3.0f);
        drawLine(sr, cx, cy, s, 62f, 44f, 76f, 44f, 3.0f);
        fillCircle(sr, cx, cy, s, 50f, 48f, 2.2f);
    }

    public static void drawPumpkinLantern(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        float pulse = 0.85f + 0.15f * MathUtils.sin(timer * 5f);

        // --- LAYER 1: background_glow ---
        sr.setColor(0.99f, 0.60f, 0.09f, 0.25f * pulse);
        fillCircle(sr, cx, cy, s, 64f, 70f, 54f);
        sr.setColor(0f, 0f, 0f, 0.40f);
        fillEllipse(sr, cx, cy, s, 64f, 112f, 42f, 10f);

        // --- LAYER 2: base_form ---
        // Twisted Gnarled Vine Stem
        sr.setColor(0.09f, 0.40f, 0.20f, 1f);
        fillTri(sr, cx, cy, s, 60f, 22f, 74f, 10f, 66f, 32f);
        // Small leaf curl
        sr.setColor(0.29f, 0.87f, 0.50f, 1f);
        fillTri(sr, cx, cy, s, 66f, 24f, 78f, 20f, 76f, 28f);

        // Outer Pumpkin Lobes (Left & Right Ribs)
        sr.setColor(0.76f, 0.25f, 0.05f, 1f);
        fillEllipse(sr, cx, cy, s, 44f, 72f, 24f, 32f);
        fillEllipse(sr, cx, cy, s, 84f, 72f, 24f, 32f);

        // Central Main Pumpkin Lobe
        sr.setColor(0.98f, 0.45f, 0.09f, 1f);
        fillEllipse(sr, cx, cy, s, 64f, 73f, 26f, 34f);

        // Grooves between Lobes
        sr.setColor(0.49f, 0.20f, 0.07f, 0.60f);
        drawLine(sr, cx, cy, s, 48f, 42f, 42f, 72f, 2.0f);
        drawLine(sr, cx, cy, s, 42f, 72f, 50f, 104f, 2.0f);
        drawLine(sr, cx, cy, s, 80f, 42f, 86f, 72f, 2.0f);
        drawLine(sr, cx, cy, s, 86f, 72f, 78f, 104f, 2.0f);

        // --- LAYER 3: details_and_relief (Jack-o' Face) ---
        sr.setColor(0.99f, 0.94f, 0.54f, pulse);
        // Triangle Eyes
        fillTri(sr, cx, cy, s, 44f, 60f, 56f, 60f, 50f, 48f);
        fillTri(sr, cx, cy, s, 72f, 60f, 84f, 60f, 78f, 48f);
        // Nose
        fillTri(sr, cx, cy, s, 60f, 68f, 68f, 68f, 64f, 62f);
        // Jagged Grin Mouth
        fillTri(sr, cx, cy, s, 40f, 76f, 88f, 76f, 64f, 92f);
        fillTri(sr, cx, cy, s, 46f, 84f, 58f, 86f, 52f, 77f);
        fillTri(sr, cx, cy, s, 70f, 86f, 82f, 84f, 76f, 77f);
        fillTri(sr, cx, cy, s, 60f, 96f, 68f, 96f, 64f, 90f);

        // White fiery inner core
        sr.setColor(1f, 1f, 1f, 0.95f * pulse);
        fillTri(sr, cx, cy, s, 47f, 59f, 53f, 59f, 50f, 50f);
        fillTri(sr, cx, cy, s, 75f, 59f, 81f, 59f, 78f, 50f);
        fillTri(sr, cx, cy, s, 61f, 67f, 67f, 67f, 64f, 63f);
    }

    public static void drawGhostPotion(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // --- LAYER 1: background_glow ---
        sr.setColor(0.29f, 0.87f, 0.50f, 0.25f);
        fillCircle(sr, cx, cy, s, 64f, 74f, 50f);
        sr.setColor(0f, 0f, 0f, 0.40f);
        fillEllipse(sr, cx, cy, s, 64f, 114f, 36f, 8f);

        // --- LAYER 2: base_form ---
        // Wood Cork Stopper
        sr.setColor(0.57f, 0.25f, 0.05f, 1f);
        fillQuad(sr, cx, cy, s, 56f, 12f, 72f, 12f, 70f, 24f, 58f, 24f);
        sr.setColor(0.47f, 0.21f, 0.06f, 1f);
        fillQuad(sr, cx, cy, s, 54f, 20f, 74f, 20f, 74f, 24f, 54f, 24f);

        // Flask Neck Lip & Neck
        sr.setColor(0.22f, 0.74f, 0.97f, 0.60f);
        fillQuad(sr, cx, cy, s, 52f, 24f, 76f, 24f, 76f, 30f, 52f, 30f);
        sr.setColor(0.22f, 0.74f, 0.97f, 0.35f);
        fillQuad(sr, cx, cy, s, 56f, 30f, 72f, 30f, 72f, 48f, 56f, 48f);

        // Round Flask Body (Glass)
        sr.setColor(0.12f, 0.11f, 0.29f, 0.70f);
        fillCircle(sr, cx, cy, s, 64f, 74f, 34f);
        sr.setColor(0.38f, 0.65f, 0.98f, 0.50f);
        drawLine(sr, cx, cy, s, 34f, 74f, 44f, 98f, 2.0f);

        // Violet Ghost Liquid Inside
        sr.setColor(0.49f, 0.13f, 0.81f, 0.92f);
        fillCircle(sr, cx, cy, s, 64f, 77f, 30f);
        sr.setColor(0.75f, 0.52f, 0.99f, 0.80f);
        fillEllipse(sr, cx, cy, s, 64f, 74f, 30f, 6f);

        // --- LAYER 3: details_and_relief (Bioluminescent Bubbles) ---
        float b1 = (timer * 20f) % 24f;
        float b2 = ((timer + 0.5f) * 20f) % 24f;
        sr.setColor(0.29f, 0.87f, 0.50f, 0.95f);
        fillCircle(sr, cx, cy, s, 54f, 96f - b1, 5f);
        fillCircle(sr, cx, cy, s, 74f, 94f - b2, 4f);
        fillCircle(sr, cx, cy, s, 62f, 98f - b2, 3f);
        fillCircle(sr, cx, cy, s, 48f, 92f - b1, 3f);

        // Center Rising Spirit Bubble
        fillCircle(sr, cx, cy, s, 64f, 62f, 6f);

        // Bubble white glints
        sr.setColor(1f, 1f, 1f, 0.90f);
        fillCircle(sr, cx, cy, s, 53f, 95f - b1, 1.8f);
        fillCircle(sr, cx, cy, s, 73f, 93f - b2, 1.5f);
        fillCircle(sr, cx, cy, s, 62f, 60f, 2.0f);

        // --- LAYER 4: specular_highlights ---
        sr.setColor(1f, 1f, 1f, 0.55f);
        drawLine(sr, cx, cy, s, 42f, 60f, 36f, 78f, 2.5f);
    }

    public static void drawBloodMoonTalisman(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // --- LAYER 1: background_glow ---
        sr.setColor(0.94f, 0.27f, 0.27f, 0.25f);
        fillCircle(sr, cx, cy, s, 64f, 62f, 54f);
        sr.setColor(0f, 0f, 0f, 0.40f);
        fillEllipse(sr, cx, cy, s, 64f, 116f, 32f, 7f);

        // --- LAYER 2: base_form ---
        // Obsidian Bead & Blood Cord
        sr.setColor(0.10f, 0.10f, 0.12f, 1f);
        fillCircle(sr, cx, cy, s, 64f, 14f, 5f);
        sr.setColor(0.94f, 0.27f, 0.27f, 1f);
        fillCircle(sr, cx, cy, s, 64f, 14f, 2.5f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1f);
        drawLine(sr, cx, cy, s, 64f, 14f, 64f, 24f, 2.8f);

        // Parchment Talisman Paper
        sr.setColor(0.99f, 0.90f, 0.54f, 1f);
        fillQuad(sr, cx, cy, s, 42f, 22f, 86f, 22f, 86f, 112f, 42f, 112f);
        sr.setColor(0.47f, 0.21f, 0.06f, 1f);
        drawLine(sr, cx, cy, s, 42f, 22f, 86f, 22f, 2.0f);
        drawLine(sr, cx, cy, s, 86f, 22f, 86f, 112f, 2.0f);
        drawLine(sr, cx, cy, s, 86f, 112f, 42f, 112f, 2.0f);
        drawLine(sr, cx, cy, s, 42f, 112f, 42f, 22f, 2.0f);

        // Vermilion Talisman Inner Borders
        sr.setColor(0.73f, 0.11f, 0.11f, 1f);
        drawLine(sr, cx, cy, s, 46f, 26f, 82f, 26f, 1.8f);
        drawLine(sr, cx, cy, s, 82f, 26f, 82f, 108f, 1.8f);
        drawLine(sr, cx, cy, s, 82f, 108f, 46f, 108f, 1.8f);
        drawLine(sr, cx, cy, s, 46f, 108f, 46f, 26f, 1.8f);

        // --- LAYER 3: details_and_relief (Blood Moon Seal & Runes) ---
        sr.setColor(0.86f, 0.15f, 0.15f, 1f);
        fillCircle(sr, cx, cy, s, 64f, 42f, 10f);
        // Crescent Inner Shadow
        sr.setColor(0.99f, 0.90f, 0.54f, 1f);
        fillCircle(sr, cx, cy, s, 67f, 40f, 8f);

        // Cinnabar Runes
        sr.setColor(0.73f, 0.11f, 0.11f, 1f);
        drawLine(sr, cx, cy, s, 54f, 58f, 64f, 54f, 2.5f);
        drawLine(sr, cx, cy, s, 64f, 54f, 74f, 58f, 2.5f);
        sr.setColor(0.86f, 0.15f, 0.15f, 1f);
        drawLine(sr, cx, cy, s, 64f, 55f, 64f, 92f, 3.2f);
        drawLine(sr, cx, cy, s, 54f, 66f, 74f, 66f, 2.2f);
        drawLine(sr, cx, cy, s, 56f, 76f, 72f, 76f, 2.2f);
        drawLine(sr, cx, cy, s, 52f, 86f, 76f, 86f, 2.2f);

        // Blood Drops
        fillCircle(sr, cx, cy, s, 64f, 98f, 3f);
        fillTri(sr, cx, cy, s, 62f, 98f, 66f, 98f, 64f, 94f);
    }

    public static void drawGhostCandle(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        float flicker = 0.85f + 0.15f * MathUtils.sin(timer * 12f);

        // --- LAYER 1: background_glow ---
        sr.setColor(0.22f, 0.74f, 0.97f, 0.30f * flicker);
        fillCircle(sr, cx, cy, s, 64f, 34f, 48f);
        sr.setColor(0f, 0f, 0f, 0.45f);
        fillEllipse(sr, cx, cy, s, 64f, 116f, 36f, 8f);

        // --- LAYER 2: base_form ---
        // White Wax Column
        sr.setColor(0.94f, 0.96f, 0.98f, 1f);
        fillQuad(sr, cx, cy, s, 50f, 48f, 78f, 48f, 78f, 112f, 50f, 112f);
        sr.setColor(0.58f, 0.64f, 0.72f, 1f);
        fillEllipse(sr, cx, cy, s, 64f, 112f, 14f, 4f);

        // Melted Wax Basin Top
        sr.setColor(0.95f, 0.96f, 0.98f, 1f);
        fillEllipse(sr, cx, cy, s, 64f, 48f, 14f, 5f);
        sr.setColor(0.79f, 0.84f, 0.88f, 1f);
        fillEllipse(sr, cx, cy, s, 64f, 49f, 12f, 4f);

        // Dripping Wax Tears along Sides
        sr.setColor(0.95f, 0.96f, 0.98f, 1f);
        fillTri(sr, cx, cy, s, 50f, 54f, 46f, 68f, 50f, 72f);
        fillCircle(sr, cx, cy, s, 48f, 74f, 2.5f);
        fillTri(sr, cx, cy, s, 78f, 58f, 82f, 72f, 78f, 76f);
        fillCircle(sr, cx, cy, s, 80f, 80f, 2.2f);

        // --- LAYER 3: details_and_relief (Wick & Cyan Soul Flame) ---
        // Black Wick
        sr.setColor(0.12f, 0.16f, 0.23f, 1f);
        drawLine(sr, cx, cy, s, 64f, 48f, 66f, 38f, 2.5f);

        // Tear-drop Ethereal Cyan Soul Flame
        sr.setColor(0.22f, 0.74f, 0.97f, 0.90f * flicker);
        fillTri(sr, cx, cy, s, 54f, 36f, 74f, 36f, 64f, (8f - flicker * 4f));
        fillCircle(sr, cx, cy, s, 64f, 36f, 10f);

        // Inner White Core Flame
        sr.setColor(1f, 1f, 1f, 0.95f);
        fillTri(sr, cx, cy, s, 58f, 36f, 70f, 36f, 64f, (16f - flicker * 3f));
        fillCircle(sr, cx, cy, s, 64f, 36f, 6f);
    }

}
