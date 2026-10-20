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
        } else if (name.equals("Mồi Thơm") || name.equals("Mồi Rủi Ro")) {
            drawBaitJar(sr, cx, cy, scale, name.equals("Mồi Rủi Ro"));
        } else if (name.equals("Thuốc Hồi Stamina") || name.equals("Nước Tăng Lực") || name.equals("Nước Bức Phá") || name.equals("Nước Bứt Phá")) {
            drawPotionFlask(sr, cx, cy, scale, name);
        } else if (name.equals("Dây Câu Tốt")) {
            drawLineSpool(sr, cx, cy, scale);
        } else if (name.equals("Bánh Mì")) {
            drawBread(sr, cx, cy, scale);
        } else if (name.equals("Bùa Hoàng Kim")) {
            drawGoldTalisman(sr, cx, cy, scale, timer);
        } else if (name.equals("Lưới Đánh Cá")) {
            drawFishingNet(sr, cx, cy, scale);
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
    // TRASH ITEMS
    // =========================================================================

    /**
     * 1. Ủng Cao Su Cũ (Worn rubber boot with tread sole, patch & water drip)
     */
    public static void drawBoot(ShapeRenderer sr, float cx, float cy, float s) {
        // Boot shaft (cổ ủng)
        sr.setColor(0.24f, 0.28f, 0.26f, 1f);
        sr.rect(cx - 18f * s, cy - 10f * s, 26f * s, 48f * s);

        // Boot top rim fold
        sr.setColor(0.18f, 0.22f, 0.20f, 1f);
        sr.rect(cx - 20f * s, cy + 34f * s, 30f * s, 7f * s);

        // Boot foot base & toe (mũi ủng cong)
        sr.setColor(0.24f, 0.28f, 0.26f, 1f);
        sr.circle(cx - 5f * s, cy - 12f * s, 22f * s, 24);
        sr.circle(cx + 22f * s, cy - 18f * s, 16f * s, 20);
        sr.rect(cx - 18f * s, cy - 30f * s, 42f * s, 22f * s);

        // Heavy corrugated rubber tread sole (đế cao su có rãnh)
        sr.setColor(0.12f, 0.12f, 0.14f, 1f);
        sr.rect(cx - 22f * s, cy - 36f * s, 62f * s, 8f * s);
        for (int r = 0; r < 5; r++) {
            sr.setColor(0.06f, 0.06f, 0.08f, 1f);
            sr.rect(cx - 18f * s + r * 12f * s, cy - 36f * s, 4f * s, 5f * s);
        }

        // Leather stitched patch (miếng vá nâu)
        sr.setColor(0.45f, 0.32f, 0.18f, 1f);
        sr.rect(cx - 14f * s, cy + 10f * s, 14f * s, 12f * s);
        sr.setColor(0.72f, 0.65f, 0.45f, 1f);
        sr.rect(cx - 12f * s, cy + 12f * s, 10f * s, 1.5f * s);
        sr.rect(cx - 12f * s, cy + 18f * s, 10f * s, 1.5f * s);

        // Dripping water droplet
        sr.setColor(0.4f, 0.85f, 1f, 0.85f);
        sr.circle(cx + 34f * s, cy - 42f * s, 3.5f * s, 10);
        sr.triangle(cx + 34f * s, cy - 36f * s, cx + 31f * s, cy - 42f * s, cx + 37f * s, cy - 42f * s);
    }

    /**
     * 2. Chai Nhựa (Plastic bottle with blue screw cap, label, murky water)
     */
    public static void drawBottle(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // Transparent cyan plastic body
        sr.setColor(0.35f, 0.72f, 0.85f, 0.65f);
        sr.rect(cx - 14f * s, cy - 30f * s, 28f * s, 45f * s);
        sr.circle(cx, cy - 28f * s, 14f * s, 20);

        // Murky water inside bottle
        sr.setColor(0.22f, 0.45f, 0.35f, 0.75f);
        sr.rect(cx - 12f * s, cy - 30f * s, 24f * s, 22f * s);

        // Tapered shoulder
        sr.setColor(0.40f, 0.78f, 0.90f, 0.70f);
        sr.triangle(cx - 14f * s, cy + 15f * s, cx + 14f * s, cy + 15f * s, cx, cy + 32f * s);

        // Bottle neck
        sr.setColor(0.45f, 0.82f, 0.95f, 0.85f);
        sr.rect(cx - 6f * s, cy + 28f * s, 12f * s, 12f * s);

        // Blue screw cap (nắp xanh)
        sr.setColor(0.15f, 0.42f, 0.85f, 1f);
        sr.rect(cx - 8f * s, cy + 38f * s, 16f * s, 8f * s);
        sr.rect(cx - 9f * s, cy + 44f * s, 18f * s, 3f * s);

        // Peeling label (nhãn dán rách)
        sr.setColor(0.92f, 0.90f, 0.82f, 0.9f);
        sr.rect(cx - 13f * s, cy - 10f * s, 26f * s, 16f * s);
        sr.setColor(0.85f, 0.22f, 0.20f, 1f);
        sr.rect(cx - 10f * s, cy - 4f * s, 20f * s, 4f * s);

        // Glistening plastic highlight
        sr.setColor(1f, 1f, 1f, 0.75f);
        sr.rect(cx - 11f * s, cy - 25f * s, 3f * s, 35f * s);
    }

    /**
     * 3. Lốp Xe (Heavy black rubber tire with hollow rim & green seaweed)
     */
    public static void drawTire(ShapeRenderer sr, float cx, float cy, float s) {
        // Outer dark rubber body
        sr.setColor(0.14f, 0.15f, 0.16f, 1f);
        sr.circle(cx, cy, 38f * s, 32);

        // Outer tread layer
        sr.setColor(0.22f, 0.24f, 0.25f, 1f);
        sr.circle(cx, cy, 35f * s, 32);

        // Tire tread notches (8 tread cuts)
        sr.setColor(0.08f, 0.09f, 0.10f, 1f);
        for (int i = 0; i < 8; i++) {
            float ang = i * (MathUtils.PI2 / 8f);
            float tx = cx + MathUtils.cos(ang) * 32f * s;
            float ty = cy + MathUtils.sin(ang) * 32f * s;
            sr.circle(tx, ty, 3.5f * s, 8);
        }

        // Inner rim dark hollow
        sr.setColor(0.10f, 0.11f, 0.12f, 1f);
        sr.circle(cx, cy, 22f * s, 28);
        sr.setColor(0.06f, 0.14f, 0.18f, 1f); // Water inside rim
        sr.circle(cx, cy, 18f * s, 24);

        // Clinging green pond weed (rong rêu bám)
        sr.setColor(0.18f, 0.48f, 0.22f, 0.95f);
        sr.rectLine(cx - 15f * s, cy + 28f * s, cx - 25f * s, cy + 42f * s, 3.5f * s);
        sr.rectLine(cx + 18f * s, cy - 22f * s, cx + 32f * s, cy - 35f * s, 3f * s);
        sr.circle(cx - 26f * s, cy + 42f * s, 4f * s, 8);
    }

    /**
     * 4. Túi Rác (Dark plastic garbage bag tied with knot)
     */
    public static void drawTrashBag(ShapeRenderer sr, float cx, float cy, float s) {
        // Bulging bag base
        sr.setColor(0.12f, 0.16f, 0.14f, 1f);
        sr.circle(cx, cy - 10f * s, 32f * s, 28);
        sr.circle(cx - 14f * s, cy - 16f * s, 24f * s, 24);
        sr.circle(cx + 14f * s, cy - 16f * s, 24f * s, 24);

        // Tied neck
        sr.setColor(0.16f, 0.22f, 0.18f, 1f);
        sr.rect(cx - 10f * s, cy + 14f * s, 20f * s, 10f * s);

        // Knotted ears (tai thắt nút)
        sr.setColor(0.20f, 0.28f, 0.22f, 1f);
        sr.triangle(cx - 4f * s, cy + 22f * s, cx - 18f * s, cy + 38f * s, cx, cy + 28f * s);
        sr.triangle(cx + 4f * s, cy + 22f * s, cx + 18f * s, cy + 38f * s, cx, cy + 28f * s);

        // Plastic shine wrinkles
        sr.setColor(0.32f, 0.45f, 0.36f, 0.6f);
        sr.rectLine(cx - 12f * s, cy - 2f * s, cx - 2f * s, cy + 8f * s, 2.5f * s);
        sr.rectLine(cx + 10f * s, cy - 6f * s, cx + 18f * s, cy + 4f * s, 2f * s);
    }

    private static void drawGenericTrash(ShapeRenderer sr, float cx, float cy, float s) {
        drawBoot(sr, cx, cy, s);
    }

    // =========================================================================
    // TREASURE ITEMS
    // =========================================================================

    /**
     * 1. Bình Cổ (Ancient porcelain / terracotta vase with gold trim & dragon glaze)
     */
    public static void drawAncientVase(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // Glowing aura
        sr.setColor(1f, 0.85f, 0.35f, 0.22f);
        sr.circle(cx, cy, 48f * s, 28);

        // Pedestal foot
        sr.setColor(0.65f, 0.45f, 0.18f, 1f);
        sr.rect(cx - 16f * s, cy - 35f * s, 32f * s, 8f * s);

        // Round vase belly (Bầu bình cổ)
        sr.setColor(0.28f, 0.48f, 0.46f, 1f); // Antique celadon jade
        sr.circle(cx, cy - 6f * s, 30f * s, 32);

        // Golden ornamental belly band
        sr.setColor(1f, 0.82f, 0.22f, 1f);
        sr.rect(cx - 28f * s, cy - 10f * s, 56f * s, 8f * s);
        sr.setColor(0.85f, 0.25f, 0.20f, 1f);
        for (int i = 0; i < 4; i++) {
            sr.circle(cx - 20f * s + i * 13f * s, cy - 6f * s, 2.5f * s, 8);
        }

        // Tapered neck
        sr.setColor(0.32f, 0.54f, 0.52f, 1f);
        sr.triangle(cx - 22f * s, cy + 14f * s, cx + 22f * s, cy + 14f * s, cx, cy + 42f * s);
        sr.rect(cx - 10f * s, cy + 18f * s, 20f * s, 20f * s);

        // Fluted Golden Lip
        sr.setColor(1f, 0.85f, 0.25f, 1f);
        sr.ellipse(cx - 15f * s, cy + 35f * s, 30f * s, 10f * s);
        sr.setColor(0.18f, 0.32f, 0.30f, 1f);
        sr.ellipse(cx - 11f * s, cy + 37f * s, 22f * s, 6f * s);

        // Dual dragon ear handles (Quai rồng hai bên)
        sr.setColor(1f, 0.82f, 0.22f, 1f);
        sr.rectLine(cx - 12f * s, cy + 30f * s, cx - 26f * s, cy + 18f * s, 3.5f * s);
        sr.rectLine(cx - 26f * s, cy + 18f * s, cx - 18f * s, cy + 4f * s, 3.5f * s);
        sr.rectLine(cx + 12f * s, cy + 30f * s, cx + 26f * s, cy + 18f * s, 3.5f * s);
        sr.rectLine(cx + 26f * s, cy + 18f * s, cx + 18f * s, cy + 4f * s, 3.5f * s);

        // Sparkle glints
        float pulse = 0.5f + 0.5f * MathUtils.sin(timer * 5f);
        sr.setColor(1f, 1f, 0.8f, pulse);
        sr.circle(cx - 12f * s, cy + 12f * s, 3f * s, 8);
    }

    /**
     * 2. Nhẫn Vàng (Shimmering gold signet ring with sparkling ruby)
     */
    public static void drawGoldRing(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // Glowing gold halo
        sr.setColor(1f, 0.85f, 0.25f, 0.28f);
        sr.circle(cx, cy, 45f * s, 28);

        // Thick Gold Band
        sr.setColor(1f, 0.82f, 0.20f, 1f);
        sr.circle(cx, cy - 4f * s, 30f * s, 32);

        // Inner finger hole
        sr.setColor(0.10f, 0.14f, 0.22f, 1f);
        sr.circle(cx, cy - 4f * s, 18f * s, 28);

        // Golden gemstone bezel setting
        sr.setColor(0.95f, 0.75f, 0.15f, 1f);
        sr.rect(cx - 14f * s, cy + 18f * s, 28f * s, 10f * s);

        // Faceted Crimson Ruby (Viên hồng ngọc đỏ thắm)
        sr.setColor(0.95f, 0.18f, 0.25f, 1f);
        sr.triangle(cx - 12f * s, cy + 22f * s, cx + 12f * s, cy + 22f * s, cx, cy + 38f * s);
        sr.triangle(cx - 12f * s, cy + 22f * s, cx + 12f * s, cy + 22f * s, cx, cy + 16f * s);

        // Ruby facet gleam
        sr.setColor(1f, 0.65f, 0.75f, 0.95f);
        sr.triangle(cx - 6f * s, cy + 22f * s, cx + 3f * s, cy + 22f * s, cx, cy + 34f * s);

        // Radiating 4-point sparkle stars
        float sp = 0.5f + 0.5f * MathUtils.sin(timer * 6f);
        sr.setColor(1f, 0.95f, 0.5f, sp);
        sr.rectLine(cx + 18f * s, cy + 28f * s, cx + 28f * s, cy + 28f * s, 2f * s);
        sr.rectLine(cx + 23f * s, cy + 23f * s, cx + 23f * s, cy + 33f * s, 2f * s);
    }

    /**
     * 3. Rương Chìm (Sunken wooden pirate chest with iron bands, barnacles & gold coins)
     */
    public static void drawSunkenChest(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // Golden treasure radiance
        sr.setColor(1f, 0.85f, 0.25f, 0.25f);
        sr.circle(cx, cy, 55f * s, 32);

        // Chest Lower Body (Gỗ rương ngâm nước)
        sr.setColor(0.32f, 0.18f, 0.10f, 1f);
        sr.rect(cx - 36f * s, cy - 25f * s, 72f * s, 32f * s);

        // Chest Curved Lid (Nắp rương hé mở)
        sr.setColor(0.42f, 0.24f, 0.14f, 1f);
        sr.rect(cx - 38f * s, cy + 10f * s, 76f * s, 18f * s);
        sr.circle(cx, cy + 28f * s, 16f * s, 24);

        // Overflowing Gold Coins & Gems inside crack
        sr.setColor(1f, 0.88f, 0.25f, 1f);
        for (int c = 0; c < 7; c++) {
            sr.circle(cx - 24f * s + c * 8f * s, cy + 8f * s + (c % 2) * 3f * s, 5f * s, 12);
        }
        sr.setColor(0.2f, 0.95f, 1f, 1f); // Cyan diamond gem
        sr.circle(cx + 4f * s, cy + 12f * s, 4f * s, 8);

        // Corroded iron reinforcing straps & lock (Đai sắt rỉ sét & ổ khóa)
        sr.setColor(0.18f, 0.20f, 0.22f, 1f);
        sr.rect(cx - 36f * s, cy - 25f * s, 8f * s, 32f * s);
        sr.rect(cx + 28f * s, cy - 25f * s, 8f * s, 32f * s);
        sr.rect(cx - 38f * s, cy + 10f * s, 8f * s, 18f * s);
        sr.rect(cx + 30f * s, cy + 10f * s, 8f * s, 18f * s);

        // Large Brass Skull Lock in center
        sr.setColor(0.92f, 0.75f, 0.20f, 1f);
        sr.circle(cx, cy + 2f * s, 9f * s, 16);
        sr.setColor(0.15f, 0.12f, 0.08f, 1f);
        sr.rect(cx - 2f * s, cy - 1f * s, 4f * s, 7f * s);

        // Clinging sea barnacles & moss (Hàu biển bám rương)
        sr.setColor(0.72f, 0.75f, 0.65f, 1f);
        sr.circle(cx - 28f * s, cy - 20f * s, 4f * s, 8);
        sr.circle(cx + 22f * s, cy - 18f * s, 5f * s, 8);
    }

    /**
     * 4. Kiếm Cổ (Ancient ceremonial blade with jade dragon guard & glowing runes)
     */
    public static void drawAncientSword(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // Mythic blue blade aura
        sr.setColor(0.3f, 0.75f, 1f, 0.25f);
        sr.circle(cx, cy, 50f * s, 28);

        // Steel Blade (Pointing up)
        sr.setColor(0.85f, 0.92f, 0.98f, 1f);
        sr.rect(cx - 5f * s, cy - 10f * s, 10f * s, 42f * s);
        sr.triangle(cx - 5f * s, cy + 32f * s, cx + 5f * s, cy + 32f * s, cx, cy + 46f * s);

        // Glowing Ancient Runes along blade fuller
        sr.setColor(0.2f, 0.85f, 1f, 0.95f);
        sr.rect(cx - 1.5f * s, cy - 6f * s, 3f * s, 36f * s);
        for (int r = 0; r < 4; r++) {
            sr.circle(cx, cy + r * 9f * s, 2.5f * s, 8);
        }

        // Dragon Head Crossguard (Thanh chắn kiếm hình đầu rồng ngọc)
        sr.setColor(0.25f, 0.65f, 0.45f, 1f); // Imperial Jade
        sr.rect(cx - 24f * s, cy - 15f * s, 48f * s, 7f * s);
        sr.setColor(1f, 0.82f, 0.20f, 1f); // Gold accents
        sr.triangle(cx - 24f * s, cy - 15f * s, cx - 30f * s, cy - 9f * s, cx - 24f * s, cy - 8f * s);
        sr.triangle(cx + 24f * s, cy - 15f * s, cx + 30f * s, cy - 9f * s, cx + 24f * s, cy - 8f * s);
        sr.circle(cx, cy - 11.5f * s, 5f * s, 12);

        // Red wrapped hilt & golden pommel (Chuôi kiếm quấn dây đỏ & ngọc chuôi)
        sr.setColor(0.75f, 0.18f, 0.18f, 1f);
        sr.rect(cx - 3.5f * s, cy - 32f * s, 7f * s, 17f * s);
        sr.setColor(1f, 0.82f, 0.20f, 1f);
        sr.circle(cx, cy - 34f * s, 6.5f * s, 14);

        // Hanging red silk tassel
        sr.setColor(0.85f, 0.2f, 0.2f, 0.9f);
        sr.rectLine(cx, cy - 36f * s, cx - 5f * s, cy - 48f * s, 2.5f * s);
    }

    /**
     * 5. Sách Kỹ Năng / Bí Kíp (Ancient mystical leather spellbook with arcane rune & ribbon)
     */
    public static void drawSpellBook(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        drawSpellBook(sr, cx, cy, s, timer, "");
    }

    public static void drawSpellBook(ShapeRenderer sr, float cx, float cy, float s, float timer, String bookName) {
        if (bookName == null) bookName = "";
        Color coverColor = new Color(0.35f, 0.16f, 0.10f, 1f); // default leather brown
        Color auraColor = new Color(0.55f, 0.3f, 0.95f, 0.25f);
        Color symbolColor = new Color(0.2f, 0.95f, 1f, 1f);

        if (bookName.contains("Dưỡng Sức")) {
            coverColor = new Color(0.12f, 0.38f, 0.22f, 1f); // Jade green
            auraColor = new Color(0.2f, 0.85f, 0.45f, 0.30f);
            symbolColor = new Color(0.4f, 1f, 0.6f, 1f);
        } else if (bookName.contains("Giật Kép")) {
            coverColor = new Color(0.52f, 0.15f, 0.12f, 1f); // Crimson
            auraColor = new Color(0.95f, 0.3f, 0.2f, 0.30f);
            symbolColor = new Color(1f, 0.85f, 0.2f, 1f);
        } else if (bookName.contains("Mắt Đại Bàng")) {
            coverColor = new Color(0.12f, 0.25f, 0.52f, 1f); // Sapphire
            auraColor = new Color(0.2f, 0.6f, 1f, 0.30f);
            symbolColor = new Color(1f, 0.9f, 0.35f, 1f);
        } else if (bookName.contains("Thông Thái")) {
            coverColor = new Color(0.38f, 0.15f, 0.52f, 1f); // Arcane purple
            auraColor = new Color(0.7f, 0.3f, 1f, 0.30f);
            symbolColor = new Color(1f, 0.9f, 0.4f, 1f);
        } else if (bookName.contains("Thuần Hóa")) {
            coverColor = new Color(0.16f, 0.42f, 0.28f, 1f); // Forest jade
            auraColor = new Color(0.25f, 0.9f, 0.55f, 0.30f);
            symbolColor = new Color(0.5f, 1f, 0.7f, 1f);
        } else if (bookName.contains("Trói Buộc")) {
            coverColor = new Color(0.22f, 0.12f, 0.36f, 1f); // Deep chain indigo
            auraColor = new Color(0.45f, 0.25f, 0.85f, 0.30f);
            symbolColor = new Color(0.75f, 0.5f, 1f, 1f);
        } else if (bookName.contains("Trái Tim ĐD")) {
            coverColor = new Color(0.08f, 0.28f, 0.44f, 1f); // Oceanic deep teal
            auraColor = new Color(0.2f, 0.75f, 0.95f, 0.30f);
            symbolColor = new Color(0.35f, 0.95f, 1f, 1f);
        }

        // Arcane glow
        sr.setColor(auraColor);
        sr.circle(cx, cy, 48f * s, 28);

        // Leather Book Cover (Bìa sách cổ)
        sr.setColor(coverColor);
        sr.rect(cx - 28f * s, cy - 30f * s, 56f * s, 60f * s);

        // Aged Parchment Pages edge (Trang sách cổ vàng ố)
        sr.setColor(0.92f, 0.88f, 0.72f, 1f);
        sr.rect(cx + 20f * s, cy - 27f * s, 6f * s, 54f * s);

        // Golden Corner Brackets
        sr.setColor(1f, 0.82f, 0.22f, 1f);
        sr.triangle(cx - 28f * s, cy + 30f * s, cx - 16f * s, cy + 30f * s, cx - 28f * s, cy + 18f * s);
        sr.triangle(cx + 20f * s, cy + 30f * s, cx + 8f * s, cy + 30f * s, cx + 20f * s, cy + 18f * s);
        sr.triangle(cx - 28f * s, cy - 30f * s, cx - 16f * s, cy - 30f * s, cx - 28f * s, cy - 18f * s);

        // Glowing Arcane Symbol on cover (Phù hiệu bí thuật phát sáng)
        float pulse = 0.7f + 0.3f * MathUtils.sin(timer * 4f);
        sr.setColor(symbolColor.r, symbolColor.g, symbolColor.b, pulse);
        sr.circle(cx - 4f * s, cy, 14f * s, 24);
        sr.setColor(coverColor);
        sr.circle(cx - 4f * s, cy, 10f * s, 20);
        sr.setColor(1f, 0.88f, 0.25f, pulse);
        sr.circle(cx - 4f * s, cy, 5f * s, 12);

        // Silken Red Bookmark ribbon
        sr.setColor(0.85f, 0.18f, 0.18f, 1f);
        sr.rect(cx + 4f * s, cy - 38f * s, 6f * s, 12f * s);
        sr.triangle(cx + 4f * s, cy - 38f * s, cx + 10f * s, cy - 38f * s, cx + 7f * s, cy - 44f * s);
    }

    // =========================================================================
    // CONSUMABLES & QUICK SLOT ICONS
    // =========================================================================

    public static void drawBread(ShapeRenderer sr, float cx, float cy, float s) {
        float bw = 48f * s;
        float bh = 26f * s;

        // Shadow under loaf
        sr.setColor(0.14f, 0.08f, 0.04f, 0.35f);
        sr.ellipse(cx - bw / 2f + 2f * s, cy - bh / 2f - 4f * s, bw, bh * 0.7f);

        // Golden baked crust loaf (perfectly centered at cx, cy)
        sr.setColor(0.82f, 0.52f, 0.20f, 1f);
        sr.ellipse(cx - bw / 2f, cy - bh / 2f, bw, bh);

        // Fluffy warm golden-yellow inner dome
        sr.setColor(0.96f, 0.76f, 0.38f, 1f);
        sr.ellipse(cx - bw * 0.44f, cy - bh * 0.38f, bw * 0.88f, bh * 0.76f);

        // Golden center highlight
        sr.setColor(1f, 0.88f, 0.55f, 0.85f);
        sr.ellipse(cx - bw * 0.32f, cy - bh * 0.22f, bw * 0.64f, bh * 0.45f);

        // 3 artisan score cuts perfectly aligned across the bread body
        sr.setColor(0.55f, 0.28f, 0.10f, 1f);
        sr.rectLine(cx - 12f * s, cy - 6f * s, cx - 6f * s, cy + 7f * s, 2.8f * s);
        sr.rectLine(cx - 2f * s, cy - 6f * s, cx + 4f * s, cy + 7f * s, 2.8f * s);
        sr.rectLine(cx + 8f * s, cy - 6f * s, cx + 14f * s, cy + 7f * s, 2.8f * s);

        // Flour dust specks along cuts
        sr.setColor(1f, 0.98f, 0.92f, 0.8f);
        sr.circle(cx - 6f * s, cy + 7f * s, 1.8f * s);
        sr.circle(cx + 4f * s, cy + 7f * s, 1.8f * s);
        sr.circle(cx + 14f * s, cy + 7f * s, 1.8f * s);
    }

    public static void drawBaitJar(ShapeRenderer sr, float cx, float cy, float s, boolean isRisky) {
        // Jar body
        sr.setColor(isRisky ? new Color(0.38f, 0.12f, 0.42f, 1f) : new Color(0.85f, 0.38f, 0.58f, 1f));
        sr.rect(cx - 16f * s, cy - 20f * s, 32f * s, 32f * s);
        sr.circle(cx, cy - 18f * s, 16f * s, 20);

        // Cork lid & twine tie
        sr.setColor(0.55f, 0.38f, 0.22f, 1f);
        sr.rect(cx - 12f * s, cy + 12f * s, 24f * s, 10f * s);
        sr.setColor(1f, 0.85f, 0.35f, 1f);
        sr.rect(cx - 14f * s, cy + 10f * s, 28f * s, 3f * s);

        // Emblem (Star for aroma, Skull for risk)
        if (isRisky) {
            sr.setColor(0.95f, 0.2f, 0.2f, 1f);
            sr.circle(cx, cy - 4f * s, 6f * s, 10);
        } else {
            sr.setColor(1f, 0.9f, 0.35f, 1f);
            sr.circle(cx, cy - 4f * s, 5f * s, 10);
        }
    }

    public static void drawPotionFlask(ShapeRenderer sr, float cx, float cy, float s, String name) {
        Color liquidColor = new Color(0.2f, 0.95f, 0.45f, 1f); // Stamina green
        if (name.equals("Nước Tăng Lực")) liquidColor = new Color(0.2f, 0.75f, 1f, 1f);
        else if (name.equals("Nước Bức Phá") || name.equals("Nước Bứt Phá")) liquidColor = new Color(0.95f, 0.22f, 0.20f, 1f);

        // Round potion flask body
        sr.setColor(0.85f, 0.95f, 1f, 0.4f); // Glass
        sr.circle(cx, cy - 6f * s, 24f * s, 24);

        // Bubbling liquid inside
        sr.setColor(liquidColor);
        sr.circle(cx, cy - 8f * s, 20f * s, 20);

        // Bubbles
        sr.setColor(1f, 1f, 1f, 0.85f);
        sr.circle(cx - 6f * s, cy - 6f * s, 3f * s, 8);
        sr.circle(cx + 5f * s, cy - 12f * s, 2f * s, 6);

        // Flask neck
        sr.setColor(0.85f, 0.95f, 1f, 0.5f);
        sr.rect(cx - 7f * s, cy + 10f * s, 14f * s, 14f * s);

        // Cork stopper
        sr.setColor(0.60f, 0.42f, 0.24f, 1f);
        sr.rect(cx - 8f * s, cy + 20f * s, 16f * s, 8f * s);
    }

    public static void drawLineSpool(ShapeRenderer sr, float cx, float cy, float s) {
        // Line spool flanges (2 golden discs)
        sr.setColor(0.35f, 0.38f, 0.42f, 1f);
        sr.rect(cx - 22f * s, cy - 18f * s, 44f * s, 36f * s);

        // Braided golden line wound around core
        sr.setColor(1f, 0.85f, 0.25f, 1f);
        sr.rect(cx - 16f * s, cy - 14f * s, 32f * s, 28f * s);
        sr.setColor(0.85f, 0.65f, 0.15f, 1f);
        for (int i = 0; i < 4; i++) {
            sr.rect(cx - 16f * s, cy - 10f * s + i * 7f * s, 32f * s, 2f * s);
        }

        // Outer spool rim lip
        sr.setColor(0.18f, 0.20f, 0.24f, 1f);
        sr.rect(cx - 24f * s, cy - 18f * s, 6f * s, 36f * s);
        sr.rect(cx + 18f * s, cy - 18f * s, 6f * s, 36f * s);
    }

    public static void drawGoldTalisman(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // Radiant golden aura
        float p = 0.5f + 0.5f * MathUtils.sin(timer * 5f);
        sr.setColor(1f, 0.85f, 0.25f, 0.3f * p);
        sr.circle(cx, cy, 32f * s, 20);

        // Yellow talisman paper
        sr.setColor(0.95f, 0.82f, 0.22f, 1f);
        sr.rect(cx - 14f * s, cy - 24f * s, 28f * s, 48f * s);

        // Red sacred rune calligraphy
        sr.setColor(0.85f, 0.15f, 0.15f, 1f);
        sr.circle(cx, cy + 12f * s, 5f * s, 10);
        sr.rect(cx - 6f * s, cy + 2f * s, 12f * s, 3f * s);
        sr.rect(cx - 2f * s, cy - 14f * s, 4f * s, 16f * s);
        sr.rect(cx - 8f * s, cy - 8f * s, 16f * s, 2.5f * s);
    }

    public static void drawFishingNet(ShapeRenderer sr, float cx, float cy, float s) {
        // Folded diamond fishing net mesh
        sr.setColor(0.2f, 0.75f, 0.95f, 0.95f);
        sr.rectLine(cx - 20f * s, cy - 16f * s, cx + 20f * s, cy + 16f * s, 2.5f * s);
        sr.rectLine(cx - 20f * s, cy + 16f * s, cx + 20f * s, cy - 16f * s, 2.5f * s);
        sr.rectLine(cx - 20f * s, cy, cx + 20f * s, cy, 2.5f * s);
        sr.rectLine(cx, cy - 16f * s, cx, cy + 16f * s, 2.5f * s);

        // Outer cork floats on net border
        sr.setColor(0.85f, 0.55f, 0.22f, 1f);
        sr.circle(cx - 20f * s, cy + 16f * s, 4.5f * s, 8);
        sr.circle(cx, cy + 16f * s, 4.5f * s, 8);
        sr.circle(cx + 20f * s, cy + 16f * s, 4.5f * s, 8);
    }

    private static void drawGenericPotion(ShapeRenderer sr, float cx, float cy, float s) {
        drawPotionFlask(sr, cx, cy, s, "Thuốc Hồi Stamina");
    }

    // =========================================================================
    // NEW TACTICAL & UTILITY ITEMS
    // =========================================================================

    public static void drawCryoSpray(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // Frost aura
        float pulse = 0.5f + 0.5f * MathUtils.sin(timer * 5f);
        sr.setColor(0.3f, 0.85f, 1f, 0.25f * pulse);
        sr.circle(cx, cy, 38f * s, 24);

        // Canister metallic cyan body
        sr.setColor(0.18f, 0.42f, 0.62f, 1f);
        sr.rect(cx - 15f * s, cy - 24f * s, 30f * s, 42f * s);
        sr.circle(cx, cy - 22f * s, 15f * s, 20);

        // Chrome metallic gradient strip
        sr.setColor(0.65f, 0.90f, 1f, 0.85f);
        sr.rect(cx - 11f * s, cy - 20f * s, 5f * s, 36f * s);

        // Canister shoulder & nozzle collar
        sr.setColor(0.75f, 0.82f, 0.88f, 1f);
        sr.triangle(cx - 15f * s, cy + 18f * s, cx + 15f * s, cy + 18f * s, cx, cy + 28f * s);
        sr.rect(cx - 6f * s, cy + 24f * s, 12f * s, 8f * s);

        // Spray push button nozzle
        sr.setColor(0.2f, 0.75f, 0.95f, 1f);
        sr.rect(cx - 5f * s, cy + 30f * s, 10f * s, 8f * s);
        // Nozzle spout angled to the left
        sr.rect(cx - 10f * s, cy + 33f * s, 6f * s, 4f * s);

        // Snowflake frost emblem on body
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.rectLine(cx - 8f * s, cy, cx + 8f * s, cy, 2.5f * s);
        sr.rectLine(cx, cy - 8f * s, cx, cy + 8f * s, 2.5f * s);
        sr.rectLine(cx - 6f * s, cy - 6f * s, cx + 6f * s, cy + 6f * s, 2f * s);
        sr.rectLine(cx - 6f * s, cy + 6f * s, cx + 6f * s, cy - 6f * s, 2f * s);

        // Freezing cryo vapor puffs escaping from nozzle
        sr.setColor(0.7f, 0.95f, 1f, 0.75f);
        sr.circle(cx - 14f * s, cy + 35f * s, 4.5f * s, 10);
        sr.circle(cx - 21f * s, cy + 39f * s, 6.5f * s, 10);
    }

    public static void drawHerbalTea(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // Dark polished wooden saucer
        float sw = 50f * s;
        float sh = 16f * s;
        sr.setColor(0.35f, 0.22f, 0.12f, 1f);
        sr.ellipse(cx - sw / 2f, cy - 24f * s - sh / 2f, sw, sh);
        sr.setColor(0.50f, 0.32f, 0.18f, 1f);
        sr.ellipse(cx - sw * 0.42f, cy - 23f * s - sh * 0.42f, sw * 0.84f, sh * 0.84f);

        // Celadon jade ceramic teacup bowl
        sr.setColor(0.28f, 0.52f, 0.42f, 1f);
        sr.circle(cx, cy - 6f * s, 22f * s, 24);
        sr.rect(cx - 20f * s, cy - 6f * s, 40f * s, 16f * s);

        // Warm jade interior glaze
        sr.setColor(0.42f, 0.68f, 0.55f, 1f);
        sr.circle(cx, cy - 6f * s, 19f * s, 20);

        // Soothing emerald herbal tea surface
        float lw = 36f * s;
        float lh = 14f * s;
        sr.setColor(0.18f, 0.62f, 0.32f, 1f);
        sr.ellipse(cx - lw / 2f, cy + 4f * s - lh / 2f, lw, lh);
        sr.setColor(0.35f, 0.82f, 0.45f, 0.8f);
        sr.ellipse(cx - lw * 0.38f, cy + 4f * s - lh * 0.35f, lw * 0.76f, lh * 0.7f);

        // Floating fresh green tea leaf
        sr.setColor(0.12f, 0.45f, 0.18f, 1f);
        sr.triangle(cx - 6f * s, cy + 5f * s, cx + 8f * s, cy + 3f * s, cx + 2f * s, cy + 8f * s);

        // Gentle aromatic steam vapors rising from cup
        float wag1 = MathUtils.sin(timer * 3f) * 4f * s;
        float wag2 = MathUtils.cos(timer * 2.5f) * 5f * s;
        sr.setColor(0.9f, 1f, 0.95f, 0.5f);
        sr.rectLine(cx - 6f * s, cy + 12f * s, cx - 4f * s + wag1, cy + 32f * s, 2.5f * s);
        sr.rectLine(cx + 8f * s, cy + 12f * s, cx + 6f * s + wag2, cy + 36f * s, 2.5f * s);
    }

    public static void drawWhetstone(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // Coarse bottom stone layer (charcoal slate)
        sr.setColor(0.24f, 0.26f, 0.30f, 1f);
        sr.rect(cx - 26f * s, cy - 20f * s, 52f * s, 18f * s);
        // Base edge bevel
        sr.setColor(0.16f, 0.18f, 0.22f, 1f);
        sr.rect(cx - 26f * s, cy - 23f * s, 52f * s, 4f * s);

        // Fine top sharpening layer (warm amber-tan oilstone)
        sr.setColor(0.85f, 0.65f, 0.38f, 1f);
        sr.rect(cx - 26f * s, cy - 2f * s, 52f * s, 20f * s);

        // Smooth top hone surface with wet sheen
        sr.setColor(0.96f, 0.80f, 0.52f, 1f);
        sr.rect(cx - 24f * s, cy + 10f * s, 48f * s, 6f * s);
        sr.setColor(1f, 1f, 1f, 0.65f);
        sr.rect(cx - 18f * s, cy + 12f * s, 32f * s, 2.5f * s);

        // Heavy forged steel fishing hook laid across stone
        sr.setColor(0.72f, 0.78f, 0.85f, 1f);
        sr.rectLine(cx - 18f * s, cy - 8f * s, cx + 12f * s, cy + 22f * s, 4.5f * s);
        // Curved barb tip
        sr.rectLine(cx + 12f * s, cy + 22f * s, cx + 22f * s, cy + 12f * s, 4f * s);
        sr.triangle(cx + 22f * s, cy + 12f * s, cx + 24f * s, cy + 18f * s, cx + 28f * s, cy + 10f * s);

        // Brilliant sharpening spark glint on hook tip
        float sparkPulse = 0.5f + 0.5f * MathUtils.sin(timer * 6f);
        sr.setColor(1f, 0.95f, 0.4f, sparkPulse);
        sr.circle(cx + 25f * s, cy + 13f * s, 5f * s * sparkPulse, 12);
        sr.rectLine(cx + 18f * s, cy + 13f * s, cx + 32f * s, cy + 13f * s, 2f * s);
        sr.rectLine(cx + 25f * s, cy + 6f * s, cx + 25f * s, cy + 20f * s, 2f * s);
    }

    public static void drawTreasureMagnet(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // Red lacquer horseshoe magnet body
        sr.setColor(0.85f, 0.18f, 0.18f, 1f);
        // Left prong
        sr.rect(cx - 25f * s, cy - 12f * s, 14f * s, 36f * s);
        // Right prong
        sr.rect(cx + 11f * s, cy - 12f * s, 14f * s, 36f * s);
        // Top curved arch bridge
        sr.rect(cx - 25f * s, cy + 14f * s, 50f * s, 16f * s);
        sr.circle(cx - 18f * s, cy + 22f * s, 10f * s, 16);
        sr.circle(cx + 18f * s, cy + 22f * s, 10f * s, 16);

        // Center cutout arch
        sr.setColor(0.12f, 0.10f, 0.08f, 0.9f);
        sr.rect(cx - 11f * s, cy - 15f * s, 22f * s, 30f * s);
        sr.circle(cx, cy + 14f * s, 11f * s, 16);

        // Chrome silver magnetic pole shoes
        sr.setColor(0.82f, 0.88f, 0.92f, 1f);
        sr.rect(cx - 25f * s, cy - 24f * s, 14f * s, 12f * s);
        sr.rect(cx + 11f * s, cy - 24f * s, 14f * s, 12f * s);

        // Silver rim lips
        sr.setColor(0.95f, 0.98f, 1f, 1f);
        sr.rect(cx - 26f * s, cy - 25f * s, 16f * s, 3f * s);
        sr.rect(cx + 10f * s, cy - 25f * s, 16f * s, 3f * s);

        // Floating magnetic gold coin in between
        sr.setColor(1f, 0.85f, 0.22f, 1f);
        sr.circle(cx, cy - 14f * s, 8f * s, 16);
        sr.setColor(0.85f, 0.65f, 0.15f, 1f);
        sr.circle(cx, cy - 14f * s, 5f * s, 14);

        // Pulsing golden magnetic flux arcs
        float wave = 0.5f + 0.5f * MathUtils.sin(timer * 5f);
        sr.setColor(1f, 0.9f, 0.3f, 0.6f * wave);
        sr.rectLine(cx - 18f * s, cy - 30f * s, cx + 18f * s, cy - 30f * s, 2.5f * s);
        sr.circle(cx - 22f * s, cy - 34f * s, 2.5f * s);
        sr.circle(cx + 22f * s, cy - 34f * s, 2.5f * s);
    }

    public static void drawSonarGoggles(ShapeRenderer sr, float cx, float cy, float s, float timer) {
        // Brown riveted leather strap
        sr.setColor(0.42f, 0.25f, 0.14f, 1f);
        sr.rect(cx - 36f * s, cy - 5f * s, 72f * s, 10f * s);
        sr.setColor(0.28f, 0.16f, 0.08f, 1f);
        sr.circle(cx - 32f * s, cy, 3f * s);
        sr.circle(cx + 32f * s, cy, 3f * s);

        // Brass nose bridge bridge
        sr.setColor(0.92f, 0.75f, 0.25f, 1f);
        sr.rect(cx - 6f * s, cy - 3f * s, 12f * s, 6f * s);

        // Left & Right Brass Goggle Outer Eyepieces
        sr.setColor(0.85f, 0.68f, 0.22f, 1f);
        sr.circle(cx - 16f * s, cy, 18f * s, 24);
        sr.circle(cx + 16f * s, cy, 18f * s, 24);

        // Dark inner bezel
        sr.setColor(0.12f, 0.18f, 0.22f, 1f);
        sr.circle(cx - 16f * s, cy, 14f * s, 20);
        sr.circle(cx + 16f * s, cy, 14f * s, 20);

        // Glowing deep ocean sonar screen
        sr.setColor(0.08f, 0.45f, 0.65f, 0.95f);
        sr.circle(cx - 16f * s, cy, 12f * s, 20);
        sr.circle(cx + 16f * s, cy, 12f * s, 20);

        // Concentric radar sweep rings
        sr.setColor(0.35f, 0.95f, 1f, 0.85f);
        sr.circle(cx - 16f * s, cy, 8f * s, 16);
        sr.circle(cx + 16f * s, cy, 8f * s, 16);
        sr.circle(cx - 16f * s, cy, 3.5f * s, 12);
        sr.circle(cx + 16f * s, cy, 3.5f * s, 12);

        // Crosshairs in lenses
        sr.rectLine(cx - 24f * s, cy, cx - 8f * s, cy, 1.5f * s);
        sr.rectLine(cx - 16f * s, cy - 8f * s, cx - 16f * s, cy + 8f * s, 1.5f * s);
        sr.rectLine(cx + 8f * s, cy, cx + 24f * s, cy, 1.5f * s);
        sr.rectLine(cx + 16f * s, cy - 8f * s, cx + 16f * s, cy + 8f * s, 1.5f * s);

        // Glass reflection sheen
        sr.setColor(1f, 1f, 1f, 0.65f);
        sr.rectLine(cx - 21f * s, cy + 5f * s, cx - 14f * s, cy + 10f * s, 2f * s);
        sr.rectLine(cx + 11f * s, cy + 5f * s, cx + 18f * s, cy + 10f * s, 2f * s);
    }
}
