package com.fishingrpg.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.fishingrpg.game.entities.Fish;
import com.fishingrpg.game.loot.Catchable;
import com.fishingrpg.game.loot.LootItem;

/**
 * Dedicated renderer for unique vector illustrations of every fish species in AnglerRPG.
 * Each fish has distinct silhouettes, fin structures, scale patterns, colors, and animations.
 */
public class FishVisualRenderer {

    /**
     * Draw any catchable item (Fish, Trash, Treasure, Consumable).
     */
    public static void drawCatchable(ShapeRenderer sr, Catchable catchable, float cx, float cy, float scale, float timer) {
        if (catchable == null) return;
        if (catchable instanceof Fish) {
            Fish f = (Fish) catchable;
            drawFish(sr, f.getName(), cx, cy, scale, timer, f.getRarityColor());
        } else if (catchable instanceof LootItem) {
            LootItem loot = (LootItem) catchable;
            LootVisualRenderer.drawLoot(sr, loot.getName(), loot.getType(), cx, cy, scale, timer);
        }
    }

    /**
     * Draw a fish species at (cx, cy) with the given scale and animation timer.
     */
    public static void drawFish(ShapeRenderer sr, String fishName, float cx, float cy, float scale, float timer, Color rarityColor) {
        if (fishName == null) fishName = "";

        // Tail wagging animation
        float wag = MathUtils.sin(timer * 5f);
        float wagFine = MathUtils.sin(timer * 8f);

        switch (fishName) {
            case "Cá Rô Đồng":
                drawCaRoDong(sr, cx, cy, scale, wag);
                break;
            case "Cá Diếc":
                drawCaDiec(sr, cx, cy, scale, wag);
                break;
            case "Cá Chép":
                drawCaChep(sr, cx, cy, scale, wag);
                break;
            case "Cá Mè":
                drawCaMe(sr, cx, cy, scale, wag);
                break;
            case "Cá Mương":
                drawCaMuong(sr, cx, cy, scale, wagFine);
                break;
            case "Cá Lóc Nhí":
                drawCaLocNhi(sr, cx, cy, scale, wagFine);
                break;
            case "Cá Trê":
                drawCaTre(sr, cx, cy, scale, wag);
                break;
            case "Cá Trôi":
                drawCaTroi(sr, cx, cy, scale, wag);
                break;
            case "Cá Trắm Cỏ":
                drawCaTramCo(sr, cx, cy, scale, wag);
                break;
            case "Cá Chim Trắng":
                drawCaChimTrang(sr, cx, cy, scale, wag);
                break;
            case "Cá Lóc Bông":
                drawCaLocBong(sr, cx, cy, scale, wag);
                break;
            case "Cá Trê Lai":
                drawCaTreLai(sr, cx, cy, scale, wag);
                break;
            case "Cá Chép Vàng":
                drawCaChepVang(sr, cx, cy, scale, wag, timer);
                break;
            case "Cá Lăng Khổng Lồ":
                drawCaLangKhongLo(sr, cx, cy, scale, wag);
                break;
            case "Thủy Quái Sông Trà":
                drawThuyQuaiSongTra(sr, cx, cy, scale, wag, timer);
                break;
            default:
                drawGenericFish(sr, cx, cy, scale, wag, rarityColor);
                break;
        }
    }

    // =========================================================================
    // =========================================================================
    // 1. CÁ RÔ ĐỒNG (Climbing Perch: Olive-Bronze, Sharp Spines, Black Gill Spot)
    // =========================================================================
    public static void drawCaRoDong(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float tailX = cx - 56f * s + wag * 4.5f * s;
        float tailY = cy + wag * 6.5f * s;

        // 1. Caudal Fin (Spiky, fan-shaped tail with fin rays)
        // Base dark membrane
        sr.setColor(0.30f, 0.25f, 0.14f, 0.95f);
        sr.triangle(cx - 36f * s, cy, tailX - 26f * s, tailY + 24f * s, tailX - 26f * s, tailY - 24f * s);
        // Translucent bronze-amber web
        sr.setColor(0.48f, 0.42f, 0.24f, 0.90f);
        sr.triangle(cx - 36f * s, cy, tailX - 22f * s, tailY + 18f * s, tailX - 22f * s, tailY - 18f * s);
        // Radiating tail ray spines (5 fine rays)
        sr.setColor(0.22f, 0.18f, 0.10f, 1f);
        for (int r = -2; r <= 2; r++) {
            sr.rectLine(cx - 36f * s, cy, tailX - 24f * s, tailY + r * 9f * s, 1.4f * s);
        }

        // 2. Anal Fin with Sharp Spines (Belly rear)
        sr.setColor(0.32f, 0.26f, 0.15f, 0.95f);
        sr.triangle(cx - 18f * s, cy - 12f * s, cx - 35f * s, cy - 10f * s, cx - 28f * s, cy - 25f * s);
        sr.setColor(0.20f, 0.16f, 0.08f, 1f);
        sr.rectLine(cx - 18f * s, cy - 12f * s, cx - 28f * s, cy - 25f * s, 1.8f * s);
        sr.rectLine(cx - 26f * s, cy - 11f * s, cx - 32f * s, cy - 22f * s, 1.5f * s);

        // 3. Iconic Sharp Spiny Dorsal Fin (Characteristic aggressive spines of climbing perch)
        // Webbed membrane connecting the spines
        sr.setColor(0.45f, 0.38f, 0.20f, 0.85f);
        sr.triangle(cx - 28f * s, cy + 12f * s, cx + 22f * s, cy + 12f * s, cx - 2f * s, cy + 32f * s);

        // Individual rigid spine needles (9 distinct sharp spines)
        for (int i = 0; i < 9; i++) {
            float fx = cx - 28f * s + i * 6.2f * s;
            float spineH = (i < 3) ? (22f + i * 4.5f) : (35f - (i - 3) * 3f);
            // Spine needle
            sr.setColor(0.24f, 0.18f, 0.10f, 1f);
            sr.rectLine(fx, cy + 12f * s, fx + 2f * s, cy + spineH * s, 1.8f * s);
            // Needle sharp tip glint
            sr.setColor(0.85f, 0.78f, 0.50f, 0.9f);
            sr.rectLine(fx + 1.2f * s, cy + (spineH - 4f) * s, fx + 2f * s, cy + spineH * s, 1.2f * s);
        }

        // 4. Main Anatomical Body (Robust, deep-bodied olive-bronze climbing perch)
        // Dark dorsal ridge
        sr.setColor(0.28f, 0.26f, 0.15f, 1f);
        sr.circle(cx, cy + 4f * s, 25f * s, 28);
        sr.circle(cx - 14f * s, cy + 3f * s, 21f * s, 24);
        sr.circle(cx + 14f * s, cy + 3f * s, 21f * s, 24);

        // Metallic bronze-olive midflank
        sr.setColor(0.44f, 0.40f, 0.23f, 1f);
        sr.circle(cx, cy, 24f * s, 28);
        sr.circle(cx - 14f * s, cy, 20f * s, 24);
        sr.circle(cx + 14f * s, cy, 20f * s, 24);

        // Warm butter-ochre belly (Vùng bụng vàng đồng đặc trưng cá rô béo)
        sr.setColor(0.68f, 0.62f, 0.36f, 1f);
        sr.circle(cx + 2f * s, cy - 7f * s, 17f * s, 24);
        sr.circle(cx - 10f * s, cy - 6f * s, 14f * s, 20);

        // 5. Dark Camouflage Vertical Tiger Bands (5 wavy transverse bars)
        sr.setColor(0.20f, 0.18f, 0.10f, 0.85f);
        for (int i = 0; i < 5; i++) {
            float sx = cx - 22f * s + i * 9.5f * s;
            sr.rectLine(sx, cy - 14f * s, sx + 3f * s, cy + 18f * s, 3.2f * s);
            // Lateral scale notch
            sr.circle(sx + 1.5f * s, cy + 2f * s, 2.2f * s);
        }

        // 6. Operculum (Gill Cover) with Signature Velvety Black Spot & Gold Halo
        float gillX = cx + 20f * s, gillY = cy + 3f * s;
        // Gill curved contour
        sr.setColor(0.22f, 0.18f, 0.10f, 1f);
        sr.rectLine(cx + 16f * s, cy + 15f * s, cx + 22f * s, cy - 10f * s, 2.5f * s);
        // Opercular spine prickle
        sr.setColor(0.18f, 0.14f, 0.08f, 1f);
        sr.triangle(cx + 21f * s, cy + 1f * s, cx + 21f * s, cy - 4f * s, cx + 26f * s, cy - 2f * s);

        // Gold iridescent halo around black spot
        sr.setColor(0.85f, 0.72f, 0.25f, 0.95f);
        sr.circle(gillX, gillY, 6.2f * s);
        // Deep velvety black spot (Dấu vết nhận diện bất hủ cá rô đồng)
        sr.setColor(0.08f, 0.06f, 0.05f, 1f);
        sr.circle(gillX, gillY, 4.5f * s);

        // 7. Pectoral Fin (Spiky, muscular fin used to climb on land)
        sr.setColor(0.60f, 0.52f, 0.30f, 0.95f);
        sr.triangle(cx + 8f * s, cy - 3f * s, cx - 10f * s, cy - 18f * s, cx + 15f * s, cy - 14f * s);
        sr.setColor(0.35f, 0.28f, 0.15f, 1f);
        sr.rectLine(cx + 8f * s, cy - 3f * s, cx - 10f * s, cy - 18f * s, 1.6f * s);

        // 8. Head & Rugged Snout
        sr.setColor(0.42f, 0.38f, 0.22f, 1f);
        sr.triangle(cx + 25f * s, cy + 15f * s, cx + 25f * s, cy - 12f * s, cx + 48f * s, cy - 1f * s);
        // Rugged mouth line
        sr.setColor(0.18f, 0.15f, 0.10f, 1f);
        sr.rectLine(cx + 38f * s, cy - 4f * s, cx + 48f * s, cy - 1f * s, 2.2f * s);

        // 9. Piercing Amber/Orange Eye with Black Pupil & Specular Glint
        sr.setColor(0.92f, 0.55f, 0.10f, 1f);
        sr.circle(cx + 34f * s, cy + 4f * s, 5.5f * s);
        sr.setColor(0.08f, 0.06f, 0.05f, 1f);
        sr.circle(cx + 35f * s, cy + 4f * s, 3.2f * s);
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + 36.5f * s, cy + 5.5f * s, 1.2f * s);
    }

    // =========================================================================
    // 2. CÁ DIẾC (Crucian Carp: High-Backed, Silvery-Pearl Scales, Soft Rosy Fins)
    // =========================================================================
    public static void drawCaDiec(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float tailX = cx - 55f * s + wag * 4.2f * s;
        float tailY = cy + wag * 5.2f * s;

        // 1. Forked Translucent Caudal Fin with Rosy Amber Tinge
        sr.setColor(0.80f, 0.85f, 0.90f, 0.85f);
        sr.triangle(cx - 32f * s, cy, tailX - 25f * s, tailY + 26f * s, tailX - 12f * s, tailY);
        sr.triangle(cx - 32f * s, cy, tailX - 25f * s, tailY - 26f * s, tailX - 12f * s, tailY);
        // Soft rosy-amber hue at fin edges
        sr.setColor(0.95f, 0.72f, 0.65f, 0.75f);
        sr.triangle(tailX - 12f * s, tailY, tailX - 25f * s, tailY + 26f * s, tailX - 22f * s, tailY + 12f * s);
        sr.triangle(tailX - 12f * s, tailY, tailX - 25f * s, tailY - 26f * s, tailX - 22f * s, tailY - 12f * s);
        // Delicate fin rays
        sr.setColor(0.70f, 0.75f, 0.82f, 0.9f);
        sr.rectLine(cx - 32f * s, cy, tailX - 23f * s, tailY + 22f * s, 1.2f * s);
        sr.rectLine(cx - 32f * s, cy, tailX - 23f * s, tailY - 22f * s, 1.2f * s);

        // 2. Anal and Pelvic Fins (Translucent peach-rose)
        sr.setColor(0.92f, 0.80f, 0.75f, 0.85f);
        sr.triangle(cx - 15f * s, cy - 14f * s, cx - 28f * s, cy - 12f * s, cx - 24f * s, cy - 24f * s);
        sr.triangle(cx + 6f * s, cy - 14f * s, cx - 2f * s, cy - 14f * s, cx + 2f * s, cy - 23f * s);

        // 3. Arched Dorsal Fin (Lưng gù đặc trưng cá diếc ao làng)
        sr.setColor(0.74f, 0.80f, 0.88f, 0.90f);
        sr.triangle(cx - 20f * s, cy + 16f * s, cx + 18f * s, cy + 16f * s, cx - 2f * s, cy + 33f * s);
        // Soft rays along dorsal fin
        sr.setColor(0.60f, 0.68f, 0.78f, 0.95f);
        for (int d = 0; d < 6; d++) {
            float dx = cx - 18f * s + d * 6.5f * s;
            sr.rectLine(dx, cy + 16f * s, cx - 2f * s, cy + 33f * s, 1.1f * s);
        }

        // 4. Plump High-Backed Body (Dáng dẹt tròn, lưng vồng cao)
        // Silvery-olive dorsal arch
        sr.setColor(0.65f, 0.72f, 0.70f, 1f);
        sr.circle(cx - 2f * s, cy + 5f * s, 26f * s, 28);

        // Silvery-platinum midflank
        sr.setColor(0.85f, 0.90f, 0.95f, 1f);
        sr.circle(cx, cy, 25f * s, 28);
        sr.circle(cx - 14f * s, cy, 20f * s, 24);
        sr.circle(cx + 14f * s, cy, 20f * s, 24);

        // Gleaming white pearlescent belly
        sr.setColor(0.96f, 0.98f, 1.0f, 1f);
        sr.circle(cx + 2f * s, cy - 7f * s, 18f * s, 24);
        sr.circle(cx - 10f * s, cy - 6f * s, 15f * s, 20);

        // 5. Shimmering Scale Texture (Delicate silvery arcs catching pond light)
        sr.setColor(0.70f, 0.78f, 0.88f, 0.65f);
        for (int row = -1; row <= 1; row++) {
            for (int col = 0; col < 4; col++) {
                float sx = cx - 16f * s + col * 9f * s;
                float sy = cy + row * 8f * s;
                sr.circle(sx, sy, 4.2f * s);
            }
        }

        // 6. Translucent Pectoral Fin with subtle amber highlight
        sr.setColor(0.92f, 0.95f, 0.98f, 0.92f);
        sr.triangle(cx + 8f * s, cy - 3f * s, cx - 8f * s, cy - 16f * s, cx + 12f * s, cy - 12f * s);
        sr.setColor(0.95f, 0.75f, 0.60f, 0.6f);
        sr.triangle(cx + 8f * s, cy - 3f * s, cx - 3f * s, cy - 12f * s, cx + 10f * s, cy - 10f * s);

        // 7. Head & Gentle Rounded Snout
        sr.setColor(0.82f, 0.88f, 0.92f, 1f);
        sr.triangle(cx + 22f * s, cy + 14f * s, cx + 22f * s, cy - 12f * s, cx + 44f * s, cy - 1f * s);
        // Opercular gill flap curve (Ánh bạc nắp mang)
        sr.setColor(0.68f, 0.75f, 0.84f, 1f);
        sr.rectLine(cx + 18f * s, cy + 12f * s, cx + 22f * s, cy - 10f * s, 2.2f * s);

        // 8. Expressive Pearlescent Amber Eye
        sr.setColor(0.96f, 0.88f, 0.55f, 1f);
        sr.circle(cx + 30f * s, cy + 4f * s, 5f * s);
        sr.setColor(0.12f, 0.15f, 0.20f, 1f);
        sr.circle(cx + 31f * s, cy + 4f * s, 2.8f * s);
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + 32.5f * s, cy + 5.2f * s, 1.2f * s);
    }

    // =========================================================================
    // 3. CÁ CHÉP (Golden Orange Scales, Barbels/Whiskers, Wavy Red-Orange Fins)
    // =========================================================================
    private static void drawCaChep(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float tailX = cx - 60f * s + wag * 5f * s;
        float tailY = cy + wag * 7f * s;

        // Flowing orange-red caudal fin
        sr.setColor(0.90f, 0.35f, 0.15f, 0.95f);
        sr.triangle(cx - 36f * s, cy, tailX - 26f * s, tailY + 28f * s, tailX - 8f * s, tailY + 6f * s);
        sr.triangle(cx - 36f * s, cy, tailX - 26f * s, tailY - 28f * s, tailX - 8f * s, tailY - 6f * s);

        // Golden dorsal fin
        sr.setColor(0.85f, 0.50f, 0.12f, 1f);
        sr.triangle(cx - 20f * s, cy + 16f * s, cx + 18f * s, cy + 16f * s, cx - 4f * s, cy + 32f * s);

        // Robust golden-amber body
        sr.setColor(0.92f, 0.62f, 0.18f, 1f);
        sr.circle(cx, cy, 27f * s, 28);
        sr.circle(cx - 16f * s, cy, 22f * s, 24);
        sr.circle(cx + 16f * s, cy, 22f * s, 24);

        // Golden yellow belly
        sr.setColor(0.98f, 0.82f, 0.35f, 1f);
        sr.circle(cx + 2f * s, cy - 7f * s, 19f * s, 24);

        // Crosshatch scale patterns
        sr.setColor(0.78f, 0.42f, 0.10f, 0.75f);
        for (int i = 0; i < 4; i++) {
            float sx = cx - 18f * s + i * 10f * s;
            sr.circle(sx, cy + 2f * s, 5f * s);
            sr.circle(sx + 5f * s, cy - 4f * s, 4f * s);
        }

        // Pectoral Fin
        sr.setColor(0.92f, 0.40f, 0.15f, 0.95f);
        sr.triangle(cx + 8f * s, cy - 4f * s, cx - 10f * s, cy - 20f * s, cx + 14f * s, cy - 15f * s);

        // Head & Snout
        sr.setColor(0.88f, 0.55f, 0.16f, 1f);
        sr.triangle(cx + 25f * s, cy + 14f * s, cx + 25f * s, cy - 12f * s, cx + 48f * s, cy - 2f * s);

        // Signature Carp Barbels / Whiskers (Râu cá chép)
        sr.setColor(0.95f, 0.70f, 0.20f, 1f);
        sr.rectLine(cx + 42f * s, cy - 6f * s, cx + 52f * s, cy - 14f * s + wag * 2f * s, 2.5f * s);
        sr.rectLine(cx + 44f * s, cy - 4f * s, cx + 55f * s, cy - 8f * s, 2f * s);

        // Golden eye
        sr.setColor(1.0f, 0.88f, 0.30f, 1f);
        sr.circle(cx + 34f * s, cy + 3f * s, 5.5f * s);
        sr.setColor(0.12f, 0.08f, 0.04f, 1f);
        sr.circle(cx + 35f * s, cy + 3f * s, 3f * s);
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + 36f * s, cy + 4f * s, 1.2f * s);
    }

    // =========================================================================
    // 4. CÁ MÈ (Broad Silver Body, Low Set Eye, High Profile)
    // =========================================================================
    private static void drawCaMe(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float tailX = cx - 58f * s + wag * 5f * s;
        float tailY = cy + wag * 6f * s;

        // Tail fin
        sr.setColor(0.70f, 0.76f, 0.84f, 0.9f);
        sr.triangle(cx - 32f * s, cy, tailX - 22f * s, tailY + 26f * s, tailX - 8f * s, tailY);
        sr.triangle(cx - 32f * s, cy, tailX - 22f * s, tailY - 26f * s, tailX - 8f * s, tailY);

        // Dorsal fin
        sr.setColor(0.65f, 0.72f, 0.80f, 0.95f);
        sr.triangle(cx - 10f * s, cy + 18f * s, cx + 16f * s, cy + 18f * s, cx + 2f * s, cy + 34f * s);

        // Deep broad silver body (high back)
        sr.setColor(0.80f, 0.85f, 0.92f, 1f);
        sr.circle(cx, cy + 2f * s, 28f * s, 28);
        sr.circle(cx - 15f * s, cy, 22f * s, 24);
        sr.circle(cx + 15f * s, cy, 24f * s, 24);

        // Silver-white belly
        sr.setColor(0.92f, 0.95f, 0.98f, 1f);
        sr.circle(cx + 2f * s, cy - 6f * s, 20f * s, 24);

        // Large curved head with low-set eye (Characteristics of bighead/silver carp)
        sr.setColor(0.75f, 0.82f, 0.90f, 1f);
        sr.triangle(cx + 24f * s, cy + 18f * s, cx + 24f * s, cy - 14f * s, cx + 48f * s, cy);

        // Low-set Eye (Eye is uniquely set below the midline of the head)
        sr.setColor(0.95f, 0.95f, 0.90f, 1f);
        sr.circle(cx + 34f * s, cy - 5f * s, 5f * s);
        sr.setColor(0.15f, 0.18f, 0.22f, 1f);
        sr.circle(cx + 35f * s, cy - 5f * s, 2.8f * s);
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + 36f * s, cy - 4f * s, 1.2f * s);

        // Pectoral fin
        sr.setColor(0.72f, 0.78f, 0.86f, 0.9f);
        sr.triangle(cx + 8f * s, cy - 4f * s, cx - 10f * s, cy - 18f * s, cx + 12f * s, cy - 14f * s);
    }

    // =========================================================================
    // =========================================================================
    // 5A. CÁ MƯƠNG (Sharpbelly: Ultra-Streamlined, Electric Neon Lateral Stripe)
    // =========================================================================
    public static void drawCaMuong(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float tailX = cx - 58f * s + wag * 3.5f * s;
        float tailY = cy + wag * 4.5f * s;

        // 1. Deeply Forked Swallowtail Caudal Fin (Clear, dynamic tail)
        sr.setColor(0.70f, 0.88f, 0.92f, 0.85f);
        sr.triangle(cx - 36f * s, cy, tailX - 22f * s, tailY + 22f * s, tailX - 10f * s, tailY);
        sr.triangle(cx - 36f * s, cy, tailX - 22f * s, tailY - 22f * s, tailX - 10f * s, tailY);
        // Cyan-silver ray accents
        sr.setColor(0.35f, 0.90f, 0.85f, 0.7f);
        sr.rectLine(cx - 36f * s, cy, tailX - 20f * s, tailY + 20f * s, 1.2f * s);
        sr.rectLine(cx - 36f * s, cy, tailX - 20f * s, tailY - 20f * s, 1.2f * s);

        // 2. Small Posterior Dorsal & Ventral Fins
        sr.setColor(0.75f, 0.88f, 0.94f, 0.85f);
        sr.triangle(cx - 10f * s, cy + 7f * s, cx + 2f * s, cy + 7f * s, cx - 4f * s, cy + 18f * s);
        sr.triangle(cx - 12f * s, cy - 7f * s, cx - 2f * s, cy - 7f * s, cx - 7f * s, cy - 16f * s);

        // 3. Ultra-Streamlined Torpedo Body (Thon dài, sống lưng xanh ngọc, sống bụng sắc)
        // Metallic steel-blue dorsal edge
        sr.setColor(0.55f, 0.72f, 0.80f, 1f);
        sr.rectLine(cx - 38f * s, cy + 2f * s, cx + 36f * s, cy + 2f * s, 14f * s);

        // Polished chrome-silver midbody
        sr.setColor(0.85f, 0.92f, 0.96f, 1f);
        sr.rectLine(cx - 38f * s, cy, cx + 36f * s, cy, 18f * s);
        sr.circle(cx + 10f * s, cy, 12f * s);
        sr.circle(cx - 12f * s, cy, 11f * s);

        // 4. Electric Neon Emerald-Turquoise Lateral Stripe (Đặc trưng lóng lánh phát sáng cá mương)
        sr.setColor(0.15f, 0.92f, 0.80f, 0.95f);
        sr.rectLine(cx - 34f * s, cy + 1.2f * s, cx + 32f * s, cy + 1.2f * s, 3.2f * s);
        // Intense bright core of the lateral streak
        sr.setColor(0.65f, 1.0f, 0.92f, 0.9f);
        sr.rectLine(cx - 30f * s, cy + 1.2f * s, cx + 28f * s, cy + 1.2f * s, 1.4f * s);

        // 5. Razor Sharp Pearl-White Belly Keel (Sống bụng lườn sắc bén)
        sr.setColor(0.96f, 0.98f, 1.0f, 1f);
        sr.rectLine(cx - 32f * s, cy - 5f * s, cx + 28f * s, cy - 5f * s, 6.5f * s);

        // 6. Translucent Pectoral Fin
        sr.setColor(0.80f, 0.90f, 0.96f, 0.85f);
        sr.triangle(cx + 14f * s, cy - 2f * s, cx + 2f * s, cy - 12f * s, cx + 18f * s, cy - 9f * s);

        // 7. Acute Pointed Snout with Upturned Lower Jaw (Miệng hếch lên đớp mồi trên mặt nước)
        sr.setColor(0.82f, 0.90f, 0.95f, 1f);
        sr.triangle(cx + 32f * s, cy + 8f * s, cx + 32f * s, cy - 7f * s, cx + 50f * s, cy + 1f * s);
        // Upturned lower jaw tip
        sr.setColor(0.68f, 0.78f, 0.85f, 1f);
        sr.rectLine(cx + 42f * s, cy - 3f * s, cx + 50f * s, cy + 1f * s, 1.8f * s);

        // 8. Bright Silver-Blue Eye with Black Center
        sr.setColor(0.75f, 0.92f, 1.0f, 1f);
        sr.circle(cx + 38f * s, cy + 3f * s, 3.8f * s);
        sr.setColor(0.08f, 0.15f, 0.22f, 1f);
        sr.circle(cx + 39f * s, cy + 3f * s, 2.2f * s);
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + 40f * s, cy + 4f * s, 1f * s);
    }

    // =========================================================================
    // 5B. CÁ LÓC NHÍ (Juvenile Snakehead Fry: Golden Racing Stripe, Tiger Saddles)
    // =========================================================================
    public static void drawCaLocNhi(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float tailX = cx - 50f * s + wag * 4.8f * s;
        float tailY = cy + wag * 5.8f * s;

        // 1. Rounded Paddle Tail with Signature Glowing Amber-Gold Outer Margin
        sr.setColor(0.24f, 0.32f, 0.18f, 0.95f);
        sr.circle(tailX - 6f * s, tailY, 14f * s);
        // Glowing warm amber rim (Vành đuôi cam vàng rực rỡ đặc trưng cá chuối/lóc con)
        sr.setColor(1.0f, 0.72f, 0.15f, 0.95f);
        sr.circle(tailX - 7f * s, tailY, 9f * s);
        // Dark juvenile ocellus center spot
        sr.setColor(0.12f, 0.16f, 0.10f, 1f);
        sr.circle(tailX - 8f * s, tailY, 5f * s);

        // 2. Long Dorsal & Anal Fin spanning almost entire length
        sr.setColor(0.25f, 0.35f, 0.20f, 0.95f);
        for (int i = 0; i < 9; i++) {
            float fx = cx - 28f * s + i * 6.5f * s;
            sr.rectLine(fx, cy + 7f * s, fx + 3f * s, cy + 16f * s, 2.5f * s);
            // Anal fin rays on underside
            if (i < 6) {
                float ax = cx - 28f * s + i * 6.0f * s;
                sr.rectLine(ax, cy - 7f * s, ax + 2f * s, cy - 14f * s, 2.0f * s);
            }
        }

        // 3. Slender Predatory Fry Cylindrical Body
        // Charcoal-olive dorsal saddle
        sr.setColor(0.28f, 0.38f, 0.22f, 1f);
        sr.rectLine(cx - 30f * s, cy + 2f * s, cx + 22f * s, cy + 2f * s, 17f * s);

        // Mid-flank olive green body
        sr.setColor(0.38f, 0.50f, 0.30f, 1f);
        sr.rectLine(cx - 30f * s, cy, cx + 22f * s, cy, 18f * s);
        sr.circle(cx - 4f * s, cy, 12.5f * s);

        // 4. Brilliant Glowing Golden-Orange Lateral Racing Stripe (Vệt vàng cam huyền thoại)
        sr.setColor(1.0f, 0.68f, 0.12f, 1f);
        sr.rectLine(cx - 32f * s, cy + 0.5f * s, cx + 26f * s, cy + 1f * s, 4.2f * s);
        // Radiant highlight through center of stripe
        sr.setColor(1.0f, 0.88f, 0.40f, 0.95f);
        sr.rectLine(cx - 28f * s, cy + 0.5f * s, cx + 24f * s, cy + 1f * s, 1.8f * s);

        // 5. Dark Camouflage Tiger Saddles along Spine (5 mottled predatory blotches)
        sr.setColor(0.12f, 0.18f, 0.10f, 0.95f);
        for (int i = 0; i < 5; i++) {
            float bx = cx - 24f * s + i * 9.5f * s;
            sr.triangle(bx, cy + 9f * s, bx + 5f * s, cy - 1f * s, bx - 3f * s, cy + 2f * s);
        }

        // 6. Cream/Butter-Yellow Ventral Belly
        sr.setColor(0.88f, 0.90f, 0.76f, 0.9f);
        sr.rectLine(cx - 24f * s, cy - 5.5f * s, cx + 18f * s, cy - 5.5f * s, 4.8f * s);

        // 7. Pectoral Fin (Fan-shaped)
        sr.setColor(0.48f, 0.60f, 0.36f, 0.9f);
        sr.triangle(cx + 12f * s, cy - 2f * s, cx + 2f * s, cy - 12f * s, cx + 16f * s, cy - 9f * s);

        // 8. Flattened Snake-Like Head Profile
        sr.setColor(0.32f, 0.44f, 0.25f, 1f);
        sr.triangle(cx + 18f * s, cy + 10f * s, cx + 18f * s, cy - 9f * s, cx + 42f * s, cy - 1f * s);
        // Lower jaw predatory protrusion
        sr.setColor(0.25f, 0.35f, 0.20f, 1f);
        sr.rectLine(cx + 32f * s, cy - 5f * s, cx + 42f * s, cy - 1f * s, 2.2f * s);

        // 9. Alert, Predatory Golden Eye
        sr.setColor(1.0f, 0.82f, 0.20f, 1f);
        sr.circle(cx + 30f * s, cy + 4.5f * s, 5.2f * s);
        sr.setColor(0.08f, 0.08f, 0.08f, 1f);
        sr.circle(cx + 31f * s, cy + 4.5f * s, 3.0f * s);
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + 32.5f * s, cy + 5.8f * s, 1.2f * s);
    }

    // =========================================================================
    // 6. CÁ TRÊ (Flat Broad Head, Smooth Dark Slate Skin, 4 Long Barbels)
    // =========================================================================
    private static void drawCaTre(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float tailX = cx - 58f * s + wag * 6f * s;
        float tailY = cy + wag * 7f * s;

        // Long rounded paddle-tail fin
        sr.setColor(0.22f, 0.20f, 0.18f, 1f);
        sr.circle(tailX - 10f * s, tailY, 16f * s);
        sr.rectLine(cx - 35f * s, cy, tailX - 10f * s, tailY, 14f * s);

        // Long dorsal & anal fin running along body
        sr.setColor(0.28f, 0.25f, 0.22f, 1f);
        sr.rectLine(cx - 30f * s, cy + 9f * s, cx + 10f * s, cy + 9f * s, 4f * s);
        sr.rectLine(cx - 30f * s, cy - 9f * s, cx + 10f * s, cy - 9f * s, 4f * s);

        // Smooth scaleless dark slate/mud-brown body
        sr.setColor(0.26f, 0.24f, 0.20f, 1f);
        sr.circle(cx, cy, 20f * s, 26);
        sr.circle(cx - 15f * s, cy, 16f * s, 22);

        // Wide flat spade head (Đầu trê dẹp bè)
        sr.setColor(0.32f, 0.29f, 0.24f, 1f);
        sr.rect(cx + 10f * s, cy - 14f * s, 28f * s, 28f * s);
        sr.circle(cx + 36f * s, cy, 14f * s);

        // Pale yellowish-grey underbelly
        sr.setColor(0.72f, 0.68f, 0.52f, 1f);
        sr.circle(cx + 6f * s, cy - 4f * s, 12f * s);

        // 4 Long Catfish Whiskers / Barbels (4 sợi râu cá trê ngoe nguẩy)
        sr.setColor(0.20f, 0.18f, 0.15f, 1f);
        // Upper barbels
        sr.rectLine(cx + 38f * s, cy + 8f * s, cx + 58f * s, cy + 20f * s + wag * 3f * s, 3f * s);
        sr.rectLine(cx + 42f * s, cy + 5f * s, cx + 64f * s, cy + 12f * s + wag * 2f * s, 2.5f * s);
        // Lower barbels
        sr.rectLine(cx + 38f * s, cy - 8f * s, cx + 58f * s, cy - 20f * s - wag * 3f * s, 3f * s);
        sr.rectLine(cx + 42f * s, cy - 5f * s, cx + 64f * s, cy - 12f * s - wag * 2f * s, 2.5f * s);

        // Small beady eyes placed on top of flat head
        sr.setColor(0.85f, 0.85f, 0.65f, 1f);
        sr.circle(cx + 30f * s, cy + 6f * s, 4f * s);
        sr.setColor(0.08f, 0.08f, 0.08f, 1f);
        sr.circle(cx + 31f * s, cy + 6f * s, 2.2f * s);
    }

    // =========================================================================
    // 7. CÁ TRÔI (Emerald Green Flank, Rosy-Tipped Fins, Red Eye Rim)
    // =========================================================================
    private static void drawCaTroi(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float tailX = cx - 56f * s + wag * 5f * s;
        float tailY = cy + wag * 6f * s;

        // Deeply forked tail with red-tinted tips
        sr.setColor(0.85f, 0.35f, 0.35f, 0.9f);
        sr.triangle(cx - 32f * s, cy, tailX - 24f * s, tailY + 26f * s, tailX - 10f * s, tailY);
        sr.triangle(cx - 32f * s, cy, tailX - 24f * s, tailY - 26f * s, tailX - 10f * s, tailY);

        // Dorsal fin with rosy margin
        sr.setColor(0.40f, 0.55f, 0.42f, 1f);
        sr.triangle(cx - 14f * s, cy + 14f * s, cx + 16f * s, cy + 14f * s, cx - 2f * s, cy + 30f * s);
        sr.setColor(0.85f, 0.35f, 0.35f, 0.85f);
        sr.triangle(cx - 2f * s, cy + 24f * s, cx + 16f * s, cy + 14f * s, cx - 2f * s, cy + 30f * s);

        // Sleek torpedo body with emerald sheen
        sr.setColor(0.38f, 0.58f, 0.45f, 1f);
        sr.circle(cx, cy, 25f * s, 28);
        sr.circle(cx - 15f * s, cy, 20f * s, 24);
        sr.circle(cx + 15f * s, cy, 21f * s, 24);

        // Silvery-white belly
        sr.setColor(0.85f, 0.92f, 0.90f, 1f);
        sr.circle(cx + 2f * s, cy - 6f * s, 17f * s, 24);

        // Snout
        sr.setColor(0.42f, 0.60f, 0.48f, 1f);
        sr.triangle(cx + 24f * s, cy + 13f * s, cx + 24f * s, cy - 11f * s, cx + 45f * s, cy);

        // Distinct Ruby-red rimmed eye
        sr.setColor(0.92f, 0.22f, 0.22f, 1f);
        sr.circle(cx + 31f * s, cy + 3f * s, 5.5f * s);
        sr.setColor(0.10f, 0.12f, 0.14f, 1f);
        sr.circle(cx + 32f * s, cy + 3f * s, 3f * s);
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + 33f * s, cy + 4f * s, 1.2f * s);
    }

    // =========================================================================
    // 8. CÁ TRẮM CỎ (Elongated Stout Body, Olive Back, Big Distinct Scales)
    // =========================================================================
    private static void drawCaTramCo(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float tailX = cx - 62f * s + wag * 5f * s;
        float tailY = cy + wag * 6f * s;

        // Broad muscular caudal fin
        sr.setColor(0.35f, 0.42f, 0.28f, 1f);
        sr.triangle(cx - 36f * s, cy, tailX - 25f * s, tailY + 28f * s, tailX - 10f * s, tailY);
        sr.triangle(cx - 36f * s, cy, tailX - 25f * s, tailY - 28f * s, tailX - 10f * s, tailY);

        // Stout elongated grass carp cylinder body
        sr.setColor(0.42f, 0.50f, 0.32f, 1f);
        sr.rectLine(cx - 34f * s, cy, cx + 25f * s, cy, 32f * s);
        sr.circle(cx, cy, 23f * s);

        // Golden olive belly
        sr.setColor(0.72f, 0.78f, 0.52f, 1f);
        sr.circle(cx + 2f * s, cy - 6f * s, 18f * s);

        // Big net-like scale markings
        sr.setColor(0.28f, 0.35f, 0.20f, 0.75f);
        for (int i = 0; i < 5; i++) {
            float sx = cx - 22f * s + i * 10f * s;
            sr.circle(sx, cy + 2f * s, 5.5f * s);
            sr.circle(sx + 5f * s, cy - 4f * s, 4.5f * s);
        }

        // Broad blunt snout
        sr.setColor(0.46f, 0.54f, 0.35f, 1f);
        sr.circle(cx + 34f * s, cy, 14f * s);

        // Calm green-gold eye
        sr.setColor(0.88f, 0.85f, 0.40f, 1f);
        sr.circle(cx + 35f * s, cy + 4f * s, 5f * s);
        sr.setColor(0.12f, 0.12f, 0.10f, 1f);
        sr.circle(cx + 36f * s, cy + 4f * s, 2.8f * s);
    }

    // =========================================================================
    // 9. CÁ CHIM TRẮNG (Deep Diamond Disc Body, Vivid Red Lower Fins)
    // =========================================================================
    private static void drawCaChimTrang(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float tailX = cx - 56f * s + wag * 5f * s;
        float tailY = cy + wag * 6f * s;

        // Caudal fin
        sr.setColor(0.75f, 0.82f, 0.88f, 0.9f);
        sr.triangle(cx - 30f * s, cy, tailX - 22f * s, tailY + 24f * s, tailX - 8f * s, tailY);
        sr.triangle(cx - 30f * s, cy, tailX - 22f * s, tailY - 24f * s, tailX - 8f * s, tailY);

        // Vivid fiery-red anal & belly fin (Đặc trưng cá chim trắng nước ngọt)
        sr.setColor(0.92f, 0.20f, 0.18f, 1f);
        sr.triangle(cx - 10f * s, cy - 18f * s, cx + 14f * s, cy - 18f * s, cx, cy - 36f * s);

        // Diamond-disc tall circular body
        sr.setColor(0.85f, 0.90f, 0.96f, 1f);
        sr.circle(cx, cy, 30f * s, 32);

        // Red chin / throat accent
        sr.setColor(0.92f, 0.35f, 0.30f, 0.85f);
        sr.circle(cx + 18f * s, cy - 12f * s, 10f * s);

        // Blunt deep head
        sr.setColor(0.80f, 0.86f, 0.92f, 1f);
        sr.triangle(cx + 20f * s, cy + 20f * s, cx + 20f * s, cy - 18f * s, cx + 44f * s, cy);

        // Eye
        sr.setColor(0.95f, 0.95f, 0.90f, 1f);
        sr.circle(cx + 28f * s, cy + 4f * s, 5f * s);
        sr.setColor(0.12f, 0.15f, 0.20f, 1f);
        sr.circle(cx + 29f * s, cy + 4f * s, 2.8f * s);
    }

    // =========================================================================
    // 10. CÁ LÓC BÔNG (Fierce Snakehead, Tiger Camo Blotches, Menacing Jaw)
    // =========================================================================
    private static void drawCaLocBong(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float tailX = cx - 64f * s + wag * 6f * s;
        float tailY = cy + wag * 7f * s;

        // Broad rounded fan tail
        sr.setColor(0.20f, 0.28f, 0.18f, 1f);
        sr.circle(tailX - 8f * s, tailY, 18f * s);

        // Long menacing dorsal fin running along the spine
        sr.setColor(0.28f, 0.38f, 0.22f, 1f);
        for (int i = 0; i < 8; i++) {
            float fx = cx - 35f * s + i * 9f * s;
            sr.rectLine(fx, cy + 10f * s, fx + 4f * s, cy + 22f * s, 3f * s);
        }

        // Long muscular predatory body
        sr.setColor(0.32f, 0.44f, 0.26f, 1f);
        sr.rectLine(cx - 36f * s, cy, cx + 25f * s, cy, 26f * s);
        sr.circle(cx, cy, 18f * s);

        // Distinct dark chevron / tiger camo blotches (Hoa văn rằn ri cá lóc bông)
        sr.setColor(0.12f, 0.18f, 0.10f, 0.95f);
        for (int i = 0; i < 5; i++) {
            float bx = cx - 28f * s + i * 12f * s;
            sr.triangle(bx, cy + 8f * s, bx + 6f * s, cy - 8f * s, bx - 4f * s, cy);
            sr.circle(bx + 4f * s, cy + 3f * s, 4f * s);
        }

        // Flat snake-like predatory head & menacing jutting jaw
        sr.setColor(0.28f, 0.38f, 0.22f, 1f);
        sr.triangle(cx + 20f * s, cy + 14f * s, cx + 20f * s, cy - 14f * s, cx + 52f * s, cy - 2f * s);
        // Lower jaw protruding forward
        sr.setColor(0.22f, 0.30f, 0.18f, 1f);
        sr.rectLine(cx + 30f * s, cy - 10f * s, cx + 54f * s, cy - 4f * s, 4f * s);

        // Fierce predatory amber eye
        sr.setColor(1.0f, 0.70f, 0.15f, 1f);
        sr.circle(cx + 36f * s, cy + 5f * s, 5.5f * s);
        sr.setColor(0.08f, 0.08f, 0.08f, 1f);
        sr.circle(cx + 37f * s, cy + 5f * s, 3f * s);
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + 38f * s, cy + 6f * s, 1.2f * s);
    }

    // =========================================================================
    // 11. CÁ TRÊ LAI (Giant Marbled Hybrid Catfish, Yellow-Black Leopard Pattern)
    // =========================================================================
    private static void drawCaTreLai(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float tailX = cx - 62f * s + wag * 6f * s;
        float tailY = cy + wag * 7f * s;

        // Muscular caudal fin
        sr.setColor(0.25f, 0.22f, 0.15f, 1f);
        sr.circle(tailX - 10f * s, tailY, 18f * s);

        // Heavy stout catfish body
        sr.setColor(0.42f, 0.36f, 0.20f, 1f);
        sr.rectLine(cx - 36f * s, cy, cx + 22f * s, cy, 30f * s);
        sr.circle(cx, cy, 22f * s);

        // Yellow-black marbled leopard spots
        sr.setColor(0.72f, 0.62f, 0.25f, 1f);
        for (int i = 0; i < 6; i++) {
            float px = cx - 26f * s + i * 9f * s;
            sr.circle(px, cy + 4f * s, 5f * s);
            sr.setColor(0.15f, 0.12f, 0.08f, 0.95f);
            sr.circle(px + 2f * s, cy - 3f * s, 4.5f * s);
            sr.setColor(0.72f, 0.62f, 0.25f, 1f);
        }

        // Wide spade head
        sr.setColor(0.35f, 0.30f, 0.18f, 1f);
        sr.circle(cx + 36f * s, cy, 16f * s);

        // 4 Catfish Whiskers
        sr.setColor(0.22f, 0.18f, 0.12f, 1f);
        sr.rectLine(cx + 42f * s, cy + 8f * s, cx + 65f * s, cy + 22f * s + wag * 3f * s, 3.5f * s);
        sr.rectLine(cx + 42f * s, cy - 8f * s, cx + 65f * s, cy - 22f * s - wag * 3f * s, 3.5f * s);

        // Eyes
        sr.setColor(0.95f, 0.85f, 0.30f, 1f);
        sr.circle(cx + 32f * s, cy + 7f * s, 4.5f * s);
        sr.setColor(0.1f, 0.1f, 0.1f, 1f);
        sr.circle(cx + 33f * s, cy + 7f * s, 2.5f * s);
    }

    // =========================================================================
    // 12. CÁ CHÉP VÀNG (Royal Imperial Golden Koi, Radiant Gilded Glow)
    // =========================================================================
    private static void drawCaChepVang(ShapeRenderer sr, float cx, float cy, float s, float wag, float timer) {
        float tailX = cx - 65f * s + wag * 6f * s;
        float tailY = cy + wag * 8f * s;

        // Radiant Golden Aura Halo
        float pulse = 0.85f + 0.15f * MathUtils.sin(timer * 6f);
        sr.setColor(1f, 0.85f, 0.25f, 0.28f * pulse);
        sr.circle(cx, cy, 48f * s);

        // Long flowing celestial butterfly caudal fin (Đuôi cá chép bướm vàng óng)
        sr.setColor(1.0f, 0.78f, 0.18f, 0.95f);
        sr.triangle(cx - 36f * s, cy, tailX - 32f * s, tailY + 34f * s, tailX - 10f * s, tailY + 8f * s);
        sr.triangle(cx - 36f * s, cy, tailX - 32f * s, tailY - 34f * s, tailX - 10f * s, tailY - 8f * s);

        // Long flowing dorsal fin with golden sheen
        sr.setColor(1.0f, 0.88f, 0.30f, 1f);
        sr.triangle(cx - 24f * s, cy + 18f * s, cx + 20f * s, cy + 18f * s, cx - 2f * s, cy + 36f * s);

        // Brilliant pure gold body
        sr.setColor(1.0f, 0.82f, 0.15f, 1f);
        sr.circle(cx, cy, 28f * s, 30);
        sr.circle(cx - 16f * s, cy, 23f * s, 24);
        sr.circle(cx + 16f * s, cy, 23f * s, 24);

        // Shimmering white-gold belly
        sr.setColor(1.0f, 0.96f, 0.65f, 1f);
        sr.circle(cx + 2f * s, cy - 7f * s, 20f * s, 24);

        // Brilliant golden dragon scales
        sr.setColor(0.92f, 0.60f, 0.08f, 0.85f);
        for (int i = 0; i < 4; i++) {
            float sx = cx - 18f * s + i * 11f * s;
            sr.circle(sx, cy + 3f * s, 5.5f * s);
            sr.circle(sx + 5f * s, cy - 3f * s, 4.5f * s);
        }

        // Royal whiskers
        sr.setColor(1f, 0.95f, 0.4f, 1f);
        sr.rectLine(cx + 44f * s, cy - 6f * s, cx + 58f * s, cy - 16f * s + wag * 2f * s, 3f * s);
        sr.rectLine(cx + 46f * s, cy - 3f * s, cx + 62f * s, cy - 8f * s, 2.5f * s);

        // Head
        sr.setColor(1.0f, 0.80f, 0.15f, 1f);
        sr.triangle(cx + 26f * s, cy + 16f * s, cx + 26f * s, cy - 12f * s, cx + 50f * s, cy - 2f * s);

        // Glowing divine eye
        sr.setColor(1.0f, 1.0f, 0.60f, 1f);
        sr.circle(cx + 36f * s, cy + 4f * s, 6f * s);
        sr.setColor(0.18f, 0.10f, 0.02f, 1f);
        sr.circle(cx + 37f * s, cy + 4f * s, 3.2f * s);
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + 38f * s, cy + 5f * s, 1.5f * s);
    }

    // =========================================================================
    // 13. CÁ LĂNG KHỔNG LỒ (Titan Bagrid Catfish, Slate Blue Armor, Jagged Spines)
    // =========================================================================
    private static void drawCaLangKhongLo(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float tailX = cx - 68f * s + wag * 6f * s;
        float tailY = cy + wag * 7f * s;

        // Powerful notched caudal fin
        sr.setColor(0.20f, 0.25f, 0.35f, 1f);
        sr.triangle(cx - 38f * s, cy, tailX - 28f * s, tailY + 32f * s, tailX - 8f * s, tailY);
        sr.triangle(cx - 38f * s, cy, tailX - 28f * s, tailY - 32f * s, tailX - 8f * s, tailY);

        // Prominent jagged dorsal spine (Ngạnh sắc nhọn cá lăng khổng lồ)
        sr.setColor(0.70f, 0.75f, 0.85f, 1f);
        sr.triangle(cx - 15f * s, cy + 18f * s, cx + 8f * s, cy + 18f * s, cx - 6f * s, cy + 42f * s);

        // Massive titan armored slate-blue body
        sr.setColor(0.25f, 0.32f, 0.44f, 1f);
        sr.rectLine(cx - 38f * s, cy, cx + 26f * s, cy, 36f * s);
        sr.circle(cx, cy, 26f * s);

        // Silvery blue underbelly
        sr.setColor(0.68f, 0.78f, 0.90f, 1f);
        sr.circle(cx + 4f * s, cy - 8f * s, 21f * s);

        // Huge predatory catfish head
        sr.setColor(0.28f, 0.36f, 0.48f, 1f);
        sr.circle(cx + 38f * s, cy, 18f * s);

        // Giant dragon-like barbels
        sr.setColor(0.18f, 0.22f, 0.32f, 1f);
        sr.rectLine(cx + 44f * s, cy + 10f * s, cx + 72f * s, cy + 26f * s + wag * 3f * s, 4f * s);
        sr.rectLine(cx + 44f * s, cy - 10f * s, cx + 72f * s, cy - 26f * s - wag * 3f * s, 4f * s);

        // Intimidating icy blue eye
        sr.setColor(0.55f, 0.85f, 1.0f, 1f);
        sr.circle(cx + 35f * s, cy + 7f * s, 6f * s);
        sr.setColor(0.08f, 0.12f, 0.20f, 1f);
        sr.circle(cx + 36f * s, cy + 7f * s, 3.2f * s);
    }

    // =========================================================================
    // 14. THỦY QUÁI SÔNG TRÀ (Prehistoric Boss Behemoth, Demon Eye, Armored Spikes)
    // =========================================================================
    private static void drawThuyQuaiSongTra(ShapeRenderer sr, float cx, float cy, float s, float wag, float timer) {
        float tailX = cx - 75f * s + wag * 8f * s;
        float tailY = cy + wag * 9f * s;

        // Mythic Demonic Whirlpool Aura
        float pulse = 0.8f + 0.2f * MathUtils.sin(timer * 8f);
        sr.setColor(0.65f, 0.10f, 0.25f, 0.35f * pulse);
        sr.circle(cx, cy, 60f * s);
        sr.setColor(0.95f, 0.20f, 0.10f, 0.20f * pulse);
        sr.circle(cx, cy, 75f * s);

        // Massive armored serrated caudal fin
        sr.setColor(0.45f, 0.12f, 0.18f, 1f);
        sr.triangle(cx - 42f * s, cy, tailX - 35f * s, tailY + 42f * s, tailX - 10f * s, tailY + 10f * s);
        sr.triangle(cx - 42f * s, cy, tailX - 35f * s, tailY - 42f * s, tailX - 10f * s, tailY - 10f * s);

        // Row of colossal prehistoric bone dorsal spikes
        sr.setColor(0.92f, 0.85f, 0.70f, 1f);
        for (int i = 0; i < 6; i++) {
            float sx = cx - 30f * s + i * 12f * s;
            sr.triangle(sx - 5f * s, cy + 20f * s, sx + 5f * s, cy + 20f * s, sx, cy + (38f + (i % 3) * 8f) * s);
        }

        // Colossal prehistoric leviathan body
        sr.setColor(0.22f, 0.15f, 0.22f, 1f);
        sr.rectLine(cx - 44f * s, cy, cx + 30f * s, cy, 44f * s);
        sr.circle(cx, cy, 32f * s);

        // Glowing crimson bloodline fissures along body
        sr.setColor(1.0f, 0.25f, 0.15f, 0.95f);
        sr.rectLine(cx - 38f * s, cy + 6f * s, cx + 25f * s, cy + 6f * s, 3.5f * s);
        sr.rectLine(cx - 32f * s, cy - 6f * s, cx + 20f * s, cy - 6f * s, 3.5f * s);

        // Armored skull with jutting crocodile-like jaw and sharp white fangs
        sr.setColor(0.18f, 0.12f, 0.18f, 1f);
        sr.triangle(cx + 25f * s, cy + 22f * s, cx + 25f * s, cy - 22f * s, cx + 62f * s, cy);

        // Protruding terrifying white fangs
        sr.setColor(1.0f, 1.0f, 1.0f, 1f);
        sr.triangle(cx + 42f * s, cy + 8f * s, cx + 46f * s, cy + 8f * s, cx + 44f * s, cy + 18f * s);
        sr.triangle(cx + 48f * s, cy + 6f * s, cx + 52f * s, cy + 6f * s, cx + 50f * s, cy + 15f * s);
        sr.triangle(cx + 45f * s, cy - 8f * s, cx + 49f * s, cy - 8f * s, cx + 47f * s, cy - 18f * s);

        // Glowing Crimson Demonic Eye
        sr.setColor(1.0f, 0.15f, 0.15f, 1f);
        sr.circle(cx + 40f * s, cy + 8f * s, 7f * s);
        sr.setColor(1.0f, 0.85f, 0.20f, 1f);
        sr.circle(cx + 41f * s, cy + 8f * s, 4f * s);
        sr.setColor(0.05f, 0.0f, 0.0f, 1f);
        sr.circle(cx + 42f * s, cy + 8f * s, 2f * s);
    }

    // =========================================================================
    // GENERIC FISH FALLBACK (Streamlined torpedo, scales, rarity-tinted fins)
    // =========================================================================
    private static void drawGenericFish(ShapeRenderer sr, float cx, float cy, float s, float wag, Color col) {
        float tailX = cx - 55f * s + wag * 4f * s;
        float tailY = cy + wag * 5f * s;

        // Tail fin in rarity color
        sr.setColor(col.r, col.g, col.b, 0.9f);
        sr.triangle(cx - 32f * s, cy, tailX - 22f * s, tailY + 24f * s, tailX - 8f * s, tailY);
        sr.triangle(cx - 32f * s, cy, tailX - 22f * s, tailY - 24f * s, tailX - 8f * s, tailY);

        // Dorsal fin
        sr.setColor(col.r * 0.85f, col.g * 0.85f, col.b * 0.85f, 0.95f);
        sr.triangle(cx - 12f * s, cy + 14f * s, cx + 16f * s, cy + 14f * s, cx + 2f * s, cy + 28f * s);

        // Main body
        sr.setColor(0.70f, 0.78f, 0.86f, 1f);
        sr.circle(cx, cy, 25f * s, 28);
        sr.circle(cx - 14f * s, cy, 20f * s, 24);
        sr.circle(cx + 14f * s, cy, 21f * s, 24);

        // Belly
        sr.setColor(0.90f, 0.95f, 1.0f, 1f);
        sr.circle(cx + 2f * s, cy - 6f * s, 17f * s, 24);

        // Snout
        sr.setColor(0.72f, 0.80f, 0.88f, 1f);
        sr.triangle(cx + 24f * s, cy + 14f * s, cx + 24f * s, cy - 12f * s, cx + 46f * s, cy);

        // Eye
        sr.setColor(0.95f, 0.95f, 0.90f, 1f);
        sr.circle(cx + 32f * s, cy + 3f * s, 5f * s);
        sr.setColor(0.12f, 0.15f, 0.20f, 1f);
        sr.circle(cx + 33f * s, cy + 3f * s, 2.8f * s);
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + 34f * s, cy + 4f * s, 1.2f * s);
    }
}
