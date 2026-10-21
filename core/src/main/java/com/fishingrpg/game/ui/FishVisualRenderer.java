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
                drawCaLangKhongLo(sr, cx, cy, scale, wag, timer);
                break;
            case "Thủy Quái Sông Trà":
                drawThuyQuaiSongTra(sr, cx, cy, scale, wag, timer);
                break;
            case "Cá Chỉ Vàng":
                drawCaChiVang(sr, cx, cy, scale, wag);
                break;
            case "Cá Đục":
                drawCaDuc(sr, cx, cy, scale, wag);
                break;
            case "Cá Cơm":
                drawCaCom(sr, cx, cy, scale, wagFine);
                break;
            case "Cá Mòi":
                drawCaMoi(sr, cx, cy, scale, wag);
                break;
            case "Cá Lẹp":
                drawCaLep(sr, cx, cy, scale, wag);
                break;
            case "Cá Bạc Má":
                drawCaBacMa(sr, cx, cy, scale, wag);
                break;
            case "Cá Nục":
                drawCaNuc(sr, cx, cy, scale, wag);
                break;
            case "Cá Đối":
                drawCaDoi(sr, cx, cy, scale, wag);
                break;
            case "Cá Trích":
                drawCaTrich(sr, cx, cy, scale, wag);
                break;
            case "Cá Chim Biển":
                drawCaChimBien(sr, cx, cy, scale, wag);
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
    // 3. CÁ CHÉP (Common Carp: King of Freshwater, Golden Armor Scales, Flowing Vermilion Fins, Barbels)
    // =========================================================================
    public static void drawCaChep(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float tailX = cx - 58f * s + wag * 4.8f * s;
        float tailY = cy + wag * 6.5f * s;

        // 1. Broad Flowing Forked Caudal Fin with Fiery Vermilion Rays & Amber Web
        // Translucent golden-amber web base
        sr.setColor(0.92f, 0.60f, 0.18f, 0.9f);
        sr.triangle(cx - 35f * s, cy, tailX - 26f * s, tailY + 28f * s, tailX - 10f * s, tailY + 4f * s);
        sr.triangle(cx - 35f * s, cy, tailX - 26f * s, tailY - 28f * s, tailX - 10f * s, tailY - 4f * s);
        // Fiery scarlet-vermilion fin margins (Đuôi cá chép sắc đỏ son tài lộc)
        sr.setColor(0.95f, 0.28f, 0.12f, 0.95f);
        sr.triangle(tailX - 10f * s, tailY + 4f * s, tailX - 26f * s, tailY + 28f * s, tailX - 20f * s, tailY + 14f * s);
        sr.triangle(tailX - 10f * s, tailY - 4f * s, tailX - 26f * s, tailY - 28f * s, tailX - 20f * s, tailY - 14f * s);
        // Radiating fin rays
        sr.setColor(0.75f, 0.20f, 0.08f, 1f);
        sr.rectLine(cx - 35f * s, cy, tailX - 24f * s, tailY + 24f * s, 1.5f * s);
        sr.rectLine(cx - 35f * s, cy, tailX - 24f * s, tailY - 24f * s, 1.5f * s);

        // 2. Anal & Pelvic Fins (Flowing orange-vermilion)
        sr.setColor(0.92f, 0.35f, 0.15f, 0.95f);
        sr.triangle(cx - 16f * s, cy - 14f * s, cx - 32f * s, cy - 12f * s, cx - 26f * s, cy - 26f * s);
        sr.triangle(cx + 8f * s, cy - 14f * s, cx - 2f * s, cy - 14f * s, cx + 2f * s, cy - 25f * s);

        // 3. Flowing Arched Dorsal Fin with Serrated Front Spine
        sr.setColor(0.88f, 0.45f, 0.12f, 0.95f);
        sr.triangle(cx - 24f * s, cy + 16f * s, cx + 18f * s, cy + 16f * s, cx - 6f * s, cy + 34f * s);
        // Serrated dark dorsal spine
        sr.setColor(0.65f, 0.25f, 0.08f, 1f);
        sr.rectLine(cx - 24f * s, cy + 16f * s, cx - 6f * s, cy + 34f * s, 2.2f * s);
        for (int d = 0; d < 6; d++) {
            float dx = cx - 20f * s + d * 6.5f * s;
            sr.rectLine(dx, cy + 16f * s, cx - 6f * s, cy + 34f * s, 1.1f * s);
        }

        // 4. Robust Arched Golden-Amber Body (Thân cá chép dầy, lưng gù uy dũng)
        // Dark golden-bronze dorsal arch
        sr.setColor(0.78f, 0.48f, 0.14f, 1f);
        sr.circle(cx - 2f * s, cy + 5f * s, 27f * s, 28);

        // Metallic golden-orange flank
        sr.setColor(0.95f, 0.65f, 0.18f, 1f);
        sr.circle(cx, cy, 26f * s, 28);
        sr.circle(cx - 15f * s, cy, 21f * s, 24);
        sr.circle(cx + 15f * s, cy, 22f * s, 24);

        // Bright butter-gold belly
        sr.setColor(1.0f, 0.85f, 0.35f, 1f);
        sr.circle(cx + 2f * s, cy - 7f * s, 18f * s, 24);
        sr.circle(cx - 10f * s, cy - 6f * s, 15f * s, 20);

        // 5. Dragon Armor Diamond Scale Pattern (Lớp vảy vàng xếp lớp như vảy rồng)
        sr.setColor(0.72f, 0.38f, 0.08f, 0.8f);
        for (int r = -1; r <= 1; r++) {
            for (int c = 0; c < 4; c++) {
                float sx = cx - 16f * s + c * 9.5f * s;
                float sy = cy + r * 8.5f * s;
                sr.circle(sx, sy, 4.5f * s);
                // Golden highlight glint on scale edge
                sr.setColor(1f, 0.88f, 0.45f, 0.7f);
                sr.circle(sx + 1.2f * s, sy + 1.2f * s, 1.8f * s);
                sr.setColor(0.72f, 0.38f, 0.08f, 0.8f);
            }
        }

        // 6. Translucent Pectoral Fin (Spade-shaped vermilion fin)
        sr.setColor(0.95f, 0.38f, 0.12f, 0.95f);
        sr.triangle(cx + 8f * s, cy - 3f * s, cx - 10f * s, cy - 20f * s, cx + 15f * s, cy - 15f * s);
        sr.setColor(1.0f, 0.75f, 0.35f, 0.7f);
        sr.triangle(cx + 8f * s, cy - 3f * s, cx - 3f * s, cy - 14f * s, cx + 12f * s, cy - 11f * s);

        // 7. Head & Noble Snout
        sr.setColor(0.88f, 0.55f, 0.16f, 1f);
        sr.triangle(cx + 25f * s, cy + 15f * s, cx + 25f * s, cy - 13f * s, cx + 48f * s, cy - 1f * s);
        // Operculum (nắp mang ánh vàng)
        sr.setColor(0.75f, 0.45f, 0.12f, 1f);
        sr.rectLine(cx + 20f * s, cy + 13f * s, cx + 24f * s, cy - 11f * s, 2.5f * s);
        // Thick fleshy lips (miệng cá chép dày)
        sr.setColor(0.95f, 0.55f, 0.35f, 1f);
        sr.circle(cx + 47f * s, cy - 2f * s, 3.5f * s);

        // 8. Signature Twin Pairs of Waving Carp Barbels (Râu cá chép mềm mại rước lộc)
        sr.setColor(0.95f, 0.65f, 0.15f, 1f);
        // Primary long whisker
        sr.rectLine(cx + 44f * s, cy - 4f * s, cx + 58f * s, cy - 15f * s + wag * 3f * s, 2.2f * s);
        // Secondary delicate upper whisker
        sr.rectLine(cx + 46f * s, cy - 1f * s, cx + 56f * s, cy - 7f * s + wag * 2f * s, 1.6f * s);

        // 9. Alert Golden-Ringed Eye with Sharp Specular Glint
        sr.setColor(1.0f, 0.85f, 0.25f, 1f);
        sr.circle(cx + 34f * s, cy + 4f * s, 5.5f * s);
        sr.setColor(0.12f, 0.08f, 0.05f, 1f);
        sr.circle(cx + 35f * s, cy + 4f * s, 3.2f * s);
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + 36.5f * s, cy + 5.5f * s, 1.2f * s);
    }

    // =========================================================================
    // 4. CÁ MÈ (Silver Carp: Deep Silvery Body, Big Head, Distinct Low-Set Eye)
    // =========================================================================
    public static void drawCaMe(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float tailX = cx - 58f * s + wag * 4.5f * s;
        float tailY = cy + wag * 5.8f * s;

        // 1. Sweeping Forked Caudal Fin with Silvery Sheen
        sr.setColor(0.72f, 0.78f, 0.86f, 0.88f);
        sr.triangle(cx - 34f * s, cy, tailX - 24f * s, tailY + 27f * s, tailX - 10f * s, tailY);
        sr.triangle(cx - 34f * s, cy, tailX - 24f * s, tailY - 27f * s, tailX - 10f * s, tailY);
        // Slate ray highlights
        sr.setColor(0.55f, 0.62f, 0.72f, 0.9f);
        sr.rectLine(cx - 34f * s, cy, tailX - 22f * s, tailY + 23f * s, 1.3f * s);
        sr.rectLine(cx - 34f * s, cy, tailX - 22f * s, tailY - 23f * s, 1.3f * s);

        // 2. Anal & Pelvic Fins (Translucent pearl-slate)
        sr.setColor(0.78f, 0.84f, 0.90f, 0.85f);
        sr.triangle(cx - 16f * s, cy - 14f * s, cx - 30f * s, cy - 12f * s, cx - 25f * s, cy - 25f * s);
        sr.triangle(cx + 6f * s, cy - 14f * s, cx - 2f * s, cy - 14f * s, cx + 2f * s, cy - 23f * s);

        // 3. Arched Triangular Dorsal Fin positioned mid-back
        sr.setColor(0.65f, 0.72f, 0.80f, 0.95f);
        sr.triangle(cx - 12f * s, cy + 18f * s, cx + 16f * s, cy + 18f * s, cx + 2f * s, cy + 35f * s);
        sr.setColor(0.50f, 0.58f, 0.68f, 1f);
        sr.rectLine(cx - 12f * s, cy + 18f * s, cx + 2f * s, cy + 35f * s, 1.8f * s);

        // 4. Broad Deep Silvery Body (Thân dẹp bên, vồng cao)
        // Slate-green dorsal curve
        sr.setColor(0.55f, 0.62f, 0.68f, 1f);
        sr.circle(cx - 2f * s, cy + 6f * s, 28f * s, 28);

        // Silvery-platinum midflank
        sr.setColor(0.82f, 0.88f, 0.94f, 1f);
        sr.circle(cx, cy + 1f * s, 27f * s, 28);
        sr.circle(cx - 15f * s, cy, 22f * s, 24);
        sr.circle(cx + 15f * s, cy, 23f * s, 24);

        // Radiant silver-white belly keel (Sống bụng lườn trắng bạc)
        sr.setColor(0.95f, 0.98f, 1.0f, 1f);
        sr.circle(cx + 2f * s, cy - 7f * s, 20f * s, 24);
        sr.circle(cx - 10f * s, cy - 6f * s, 16f * s, 20);

        // 5. Massive Aerodynamic Head Profile (Đặc trưng "Đầu cá mè" to quá khổ)
        sr.setColor(0.76f, 0.82f, 0.90f, 1f);
        sr.triangle(cx + 24f * s, cy + 20f * s, cx + 24f * s, cy - 15f * s, cx + 52f * s, cy + 1f * s);
        // Opercular plate contour
        sr.setColor(0.62f, 0.70f, 0.78f, 1f);
        sr.rectLine(cx + 20f * s, cy + 18f * s, cx + 26f * s, cy - 13f * s, 2.5f * s);

        // Upturned mouth for plankton filter feeding (Miệng hếch lên lọc sinh vật phù du)
        sr.setColor(0.55f, 0.62f, 0.72f, 1f);
        sr.rectLine(cx + 42f * s, cy - 4f * s, cx + 52f * s, cy + 2f * s, 2.5f * s);

        // 6. Signature Low-Set Eye (Mắt cá mè nằm thấp dưới đường trục miệng)
        sr.setColor(0.95f, 0.96f, 0.90f, 1f);
        sr.circle(cx + 36f * s, cy - 5.5f * s, 5.2f * s);
        sr.setColor(0.12f, 0.16f, 0.22f, 1f);
        sr.circle(cx + 37f * s, cy - 5.5f * s, 3.0f * s);
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + 38.2f * s, cy - 4.2f * s, 1.2f * s);

        // 7. Long Pectoral Fin sweeping back
        sr.setColor(0.72f, 0.80f, 0.88f, 0.92f);
        sr.triangle(cx + 10f * s, cy - 2f * s, cx - 8f * s, cy - 18f * s, cx + 18f * s, cy - 14f * s);
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
        float tailX = cx - 38f * s + wag * 4.0f * s;
        float tailY = cy + wag * 5.0f * s;

        // 1. Muscular Caudal Peduncle seamlessly connecting body directly into tail
        sr.setColor(0.36f, 0.48f, 0.28f, 1f);
        sr.triangle(cx - 18f * s, cy + 9f * s, cx - 18f * s, cy - 9f * s, tailX, tailY);
        sr.setColor(0.28f, 0.38f, 0.22f, 1f);
        sr.triangle(cx - 18f * s, cy + 9f * s, cx - 10f * s, cy + 9f * s, tailX, tailY);

        // 2. Rounded Paddle Tail with Signature Glowing Amber-Gold Outer Margin
        sr.setColor(0.24f, 0.32f, 0.18f, 0.95f);
        sr.circle(tailX - 2f * s, tailY, 13f * s);
        // Glowing warm amber rim (Vành đuôi cam vàng rực rỡ đặc trưng cá chuối/lóc con)
        sr.setColor(1.0f, 0.72f, 0.15f, 0.95f);
        sr.circle(tailX - 3f * s, tailY, 8.5f * s);
        // Dark juvenile ocellus center spot
        sr.setColor(0.12f, 0.16f, 0.10f, 1f);
        sr.circle(tailX - 4f * s, tailY, 4.5f * s);

        // 3. Long Dorsal & Anal Fin spanning almost entire length
        sr.setColor(0.25f, 0.35f, 0.20f, 0.95f);
        for (int i = 0; i < 9; i++) {
            float fx = cx - 26f * s + i * 5.8f * s;
            sr.rectLine(fx, cy + 7f * s, fx + 3f * s, cy + 16f * s, 2.5f * s);
            // Anal fin rays on underside
            if (i < 6) {
                float ax = cx - 26f * s + i * 5.5f * s;
                sr.rectLine(ax, cy - 7f * s, ax + 2f * s, cy - 14f * s, 2.0f * s);
            }
        }

        // 4. Slender Predatory Fry Cylindrical Body
        // Charcoal-olive dorsal saddle
        sr.setColor(0.28f, 0.38f, 0.22f, 1f);
        sr.rectLine(cx - 28f * s, cy + 2f * s, cx + 22f * s, cy + 2f * s, 17f * s);

        // Mid-flank olive green body
        sr.setColor(0.38f, 0.50f, 0.30f, 1f);
        sr.rectLine(cx - 28f * s, cy, cx + 22f * s, cy, 18f * s);
        sr.circle(cx - 4f * s, cy, 12.5f * s);

        // 5. Brilliant Glowing Golden-Orange Lateral Racing Stripe (Vệt vàng cam huyền thoại)
        sr.setColor(1.0f, 0.68f, 0.12f, 1f);
        sr.rectLine(cx - 30f * s, cy + 0.5f * s, tailX, tailY, 4.0f * s);
        // Radiant highlight through center of stripe
        sr.setColor(1.0f, 0.88f, 0.40f, 0.95f);
        sr.rectLine(cx - 26f * s, cy + 0.5f * s, tailX - 2f * s, tailY, 1.8f * s);

        // 6. Dark Camouflage Tiger Saddles along Spine (5 mottled predatory blotches)
        sr.setColor(0.12f, 0.18f, 0.10f, 0.95f);
        for (int i = 0; i < 5; i++) {
            float bx = cx - 22f * s + i * 9.0f * s;
            sr.triangle(bx, cy + 9f * s, bx + 5f * s, cy - 1f * s, bx - 3f * s, cy + 2f * s);
        }

        // 7. Cream/Butter-Yellow Ventral Belly
        sr.setColor(0.88f, 0.90f, 0.76f, 0.9f);
        sr.rectLine(cx - 22f * s, cy - 5.5f * s, cx + 18f * s, cy - 5.5f * s, 4.8f * s);

        // 8. Pectoral Fin (Fan-shaped)
        sr.setColor(0.48f, 0.60f, 0.36f, 0.9f);
        sr.triangle(cx + 12f * s, cy - 2f * s, cx + 2f * s, cy - 12f * s, cx + 16f * s, cy - 9f * s);

        // 9. Flattened Snake-Like Head Profile
        sr.setColor(0.32f, 0.44f, 0.25f, 1f);
        sr.triangle(cx + 18f * s, cy + 10f * s, cx + 18f * s, cy - 9f * s, cx + 42f * s, cy - 1f * s);
        // Lower jaw predatory protrusion
        sr.setColor(0.25f, 0.35f, 0.20f, 1f);
        sr.rectLine(cx + 32f * s, cy - 5f * s, cx + 42f * s, cy - 1f * s, 2.2f * s);

        // 10. Alert, Predatory Golden Eye
        sr.setColor(1.0f, 0.82f, 0.20f, 1f);
        sr.circle(cx + 30f * s, cy + 4.5f * s, 5.2f * s);
        sr.setColor(0.08f, 0.08f, 0.08f, 1f);
        sr.circle(cx + 31f * s, cy + 4.5f * s, 3.0f * s);
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + 32.5f * s, cy + 5.8f * s, 1.2f * s);
    }

    // =========================================================================
    // 6. CÁ TRÊ (Walking Catfish: Scaleless Mud-Brown Body, Broad Flat Head, 4 Waving Barbels)
    // =========================================================================
    public static void drawCaTre(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float tailX = cx - 58f * s + wag * 5.5f * s;
        float tailY = cy + wag * 6.8f * s;

        // 1. Long Rounded Paddle Tail seamlessly joined to muscular body
        sr.setColor(0.20f, 0.18f, 0.16f, 1f);
        sr.circle(tailX - 8f * s, tailY, 15f * s);
        // Peduncle connection
        sr.triangle(cx - 24f * s, cy + 10f * s, cx - 24f * s, cy - 10f * s, tailX - 6f * s, tailY);

        // 2. Continuous Long Dorsal & Anal Fin spanning almost full length
        sr.setColor(0.26f, 0.24f, 0.20f, 0.95f);
        for (int i = 0; i < 10; i++) {
            float fx = cx - 32f * s + i * 5.8f * s;
            sr.rectLine(fx, cy + 8f * s, fx + 2f * s, cy + 15f * s, 2.2f * s);
            if (i < 7) {
                float ax = cx - 30f * s + i * 5.5f * s;
                sr.rectLine(ax, cy - 8f * s, ax + 2f * s, cy - 14f * s, 2.0f * s);
            }
        }

        // 3. Smooth Scaleless Mud-Brown Body (Da trơn bóng màu xám đen bùn)
        // Dark charcoal-slate dorsal ridge
        sr.setColor(0.18f, 0.16f, 0.14f, 1f);
        sr.rectLine(cx - 30f * s, cy + 1.5f * s, cx + 12f * s, cy + 1.5f * s, 17f * s);

        // Velvet mud-brown flank
        sr.setColor(0.28f, 0.25f, 0.21f, 1f);
        sr.rectLine(cx - 30f * s, cy, cx + 12f * s, cy, 18f * s);
        sr.circle(cx, cy, 19f * s, 26);
        sr.circle(cx - 15f * s, cy, 16f * s, 22);

        // Pale creamy-ochre underbelly (Bụng cá trê màu vàng đất nhạt)
        sr.setColor(0.72f, 0.68f, 0.50f, 1f);
        sr.rectLine(cx - 22f * s, cy - 5.5f * s, cx + 12f * s, cy - 5.5f * s, 6.5f * s);
        sr.circle(cx + 4f * s, cy - 4f * s, 11f * s);

        // 4. Wide Armored Flat Spade Head (Đầu trê bẹp dẹp bọc xương giáp)
        sr.setColor(0.32f, 0.28f, 0.23f, 1f);
        sr.rect(cx + 8f * s, cy - 14f * s, 26f * s, 28f * s);
        sr.circle(cx + 34f * s, cy, 14f * s);
        // Operculum suture line
        sr.setColor(0.18f, 0.15f, 0.12f, 1f);
        sr.rectLine(cx + 10f * s, cy + 13f * s, cx + 14f * s, cy - 13f * s, 2.2f * s);

        // 5. Pectoral Fins with Sharp Defensive Spines (Ngạnh cá trê sắc nhọn hai bên)
        sr.setColor(0.38f, 0.32f, 0.25f, 0.95f);
        sr.triangle(cx + 12f * s, cy - 3f * s, cx - 2f * s, cy - 16f * s, cx + 16f * s, cy - 13f * s);
        // Stiff sharp pectoral spine needle
        sr.setColor(0.85f, 0.82f, 0.70f, 1f);
        sr.rectLine(cx + 12f * s, cy - 3f * s, cx - 2f * s, cy - 16f * s, 1.8f * s);

        // 6. Broad Sensual Catfish Mouth & 4 Long Waving Barbels (4 sợi râu trê uyển chuyển)
        sr.setColor(0.14f, 0.12f, 0.10f, 1f);
        sr.rectLine(cx + 30f * s, cy - 3f * s, cx + 46f * s, cy, 2.5f * s);

        sr.setColor(0.18f, 0.16f, 0.14f, 1f);
        // Upper barbels (râu mép trên vươn dài ngoe nguẩy)
        sr.rectLine(cx + 36f * s, cy + 7f * s, cx + 58f * s, cy + 20f * s + wag * 3.5f * s, 2.6f * s);
        sr.rectLine(cx + 40f * s, cy + 4f * s, cx + 65f * s, cy + 11f * s + wag * 2.5f * s, 2.2f * s);
        // Lower barbels (râu cằm dưới rà đáy tìm mồi)
        sr.rectLine(cx + 36f * s, cy - 7f * s, cx + 58f * s, cy - 20f * s - wag * 3.5f * s, 2.6f * s);
        sr.rectLine(cx + 40f * s, cy - 4f * s, cx + 65f * s, cy - 11f * s - wag * 2.5f * s, 2.2f * s);

        // 7. Small Beady Eyes on Top of Skull
        sr.setColor(0.88f, 0.85f, 0.60f, 1f);
        sr.circle(cx + 28f * s, cy + 7f * s, 4.2f * s);
        sr.setColor(0.08f, 0.08f, 0.08f, 1f);
        sr.circle(cx + 29f * s, cy + 7f * s, 2.5f * s);
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + 30.2f * s, cy + 8f * s, 1f * s);
    }

    // =========================================================================
    // 7. CÁ TRÔI (Cirrhinus mrigala: Emerald Torpedo, Ruby Eye Ring, Rosy Crimson Fins)
    // =========================================================================
    public static void drawCaTroi(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float tailX = cx - 58f * s + wag * 5.5f * s;
        float tailY = cy + wag * 7f * s;

        // 1. Muscular Caudal Peduncle (Cuống đuôi dẻo dai)
        sr.setColor(0.32f, 0.54f, 0.40f, 1f);
        sr.triangle(cx - 20f * s, cy + 13f * s, cx - 20f * s, cy - 11f * s, tailX, tailY);

        // 2. Deeply Forked Caudal Fin with Rosy-Crimson Margins (Vây đuôi chẻ sâu viền đỏ son)
        // Base dark membrane
        sr.setColor(0.24f, 0.40f, 0.30f, 0.95f);
        sr.triangle(tailX, tailY, tailX - 26f * s, tailY + 28f * s, tailX - 10f * s, tailY);
        sr.triangle(tailX, tailY, tailX - 26f * s, tailY - 28f * s, tailX - 10f * s, tailY);
        // Vibrant rosy crimson fin lobes
        sr.setColor(0.92f, 0.28f, 0.25f, 0.95f);
        sr.triangle(tailX - 4f * s, tailY, tailX - 28f * s, tailY + 29f * s, tailX - 18f * s, tailY + 12f * s);
        sr.triangle(tailX - 4f * s, tailY, tailX - 28f * s, tailY - 29f * s, tailX - 18f * s, tailY - 12f * s);
        // Radiating fin rays
        sr.setColor(0.85f, 0.95f, 0.90f, 0.85f);
        sr.rectLine(tailX, tailY, tailX - 26f * s, tailY + 26f * s, 1.3f * s);
        sr.rectLine(tailX, tailY, tailX - 26f * s, tailY - 26f * s, 1.3f * s);
        sr.rectLine(tailX, tailY, tailX - 20f * s, tailY + 14f * s, 1.2f * s);
        sr.rectLine(tailX, tailY, tailX - 20f * s, tailY - 14f * s, 1.2f * s);

        // 3. Ventral & Anal Fins with Rosy Tips (Vây bụng & Vây hậu môn)
        sr.setColor(0.90f, 0.30f, 0.25f, 0.9f);
        sr.triangle(cx - 16f * s, cy - 14f * s, cx - 8f * s, cy - 14f * s, cx - 18f * s, cy - 25f * s);
        sr.triangle(cx + 4f * s, cy - 16f * s, cx + 12f * s, cy - 16f * s, cx + 2f * s, cy - 27f * s);

        // 4. Crescent Dorsal Fin with Rosy Crest (Vây lưng cao viền son)
        sr.setColor(0.28f, 0.48f, 0.35f, 1f);
        sr.triangle(cx - 18f * s, cy + 14f * s, cx + 16f * s, cy + 14f * s, cx - 4f * s, cy + 32f * s);
        sr.setColor(0.92f, 0.28f, 0.25f, 0.9f);
        sr.triangle(cx - 8f * s, cy + 24f * s, cx + 14f * s, cy + 14f * s, cx - 4f * s, cy + 33f * s);
        // Dorsal fin rays
        sr.setColor(0.20f, 0.35f, 0.24f, 1f);
        sr.rectLine(cx - 16f * s, cy + 14f * s, cx - 4f * s, cy + 31f * s, 1.4f * s);
        sr.rectLine(cx - 4f * s, cy + 14f * s, cx + 2f * s, cy + 26f * s, 1.2f * s);
        sr.rectLine(cx + 6f * s, cy + 14f * s, cx + 10f * s, cy + 20f * s, 1.2f * s);

        // 5. Sleek Hydrodynamic Torpedo Body with Emerald Sheen (Thân ngư lôi màu xanh ngọc bích)
        // Dark mossy-emerald dorsal spine
        sr.setColor(0.22f, 0.42f, 0.28f, 1f);
        sr.circle(cx, cy + 4f * s, 23f * s, 32);
        sr.circle(cx - 14f * s, cy + 3f * s, 18f * s, 28);
        sr.circle(cx + 14f * s, cy + 3f * s, 19f * s, 28);

        // Main iridescent emerald flank
        sr.setColor(0.35f, 0.60f, 0.44f, 1f);
        sr.circle(cx, cy, 22f * s, 32);
        sr.circle(cx - 12f * s, cy, 18f * s, 28);
        sr.circle(cx + 12f * s, cy, 18f * s, 28);

        // Pearlescent silver-white belly keel
        sr.setColor(0.88f, 0.94f, 0.90f, 1f);
        sr.circle(cx + 2f * s, cy - 7f * s, 16f * s, 32);
        sr.circle(cx - 10f * s, cy - 6f * s, 13f * s, 28);

        // 6. Fine Pearly Scales with Rosy Edges (Hàng vảy ánh ngọc trai phớt hồng)
        for (int row = 0; row < 2; row++) {
            float ry = cy + 2f * s - row * 7f * s;
            for (int col = 0; col < 5; col++) {
                float rx = cx - 18f * s + col * 8f * s + (row % 2) * 4f * s;
                sr.setColor(0.92f, 0.55f, 0.50f, 0.40f);
                sr.circle(rx, ry, 3.8f * s);
                sr.setColor(0.45f, 0.75f, 0.55f, 0.60f);
                sr.circle(rx - 0.8f * s, ry, 2.8f * s);
            }
        }

        // 7. Gill Cover with Bronze & Rosy Flush (Nắp mang ánh đồng phớt hồng)
        sr.setColor(0.85f, 0.65f, 0.45f, 0.85f);
        sr.circle(cx + 22f * s, cy, 14f * s);
        sr.setColor(0.92f, 0.45f, 0.40f, 0.50f);
        sr.circle(cx + 24f * s, cy - 3f * s, 10f * s);

        // Pectoral fin with rosy hue
        sr.setColor(0.90f, 0.35f, 0.30f, 0.95f);
        sr.triangle(cx + 16f * s, cy - 5f * s, cx + 22f * s, cy - 14f * s, cx + 8f * s, cy - 18f * s);
        sr.setColor(0.35f, 0.55f, 0.40f, 0.8f);
        sr.triangle(cx + 16f * s, cy - 5f * s, cx + 20f * s, cy - 12f * s, cx + 11f * s, cy - 15f * s);

        // 8. Streamlined Snout & Inferior Horseshoe Mouth with Tiny Barbels
        sr.setColor(0.35f, 0.56f, 0.42f, 1f);
        sr.triangle(cx + 25f * s, cy + 13f * s, cx + 25f * s, cy - 11f * s, cx + 46f * s, cy);
        // Snout tip
        sr.setColor(0.85f, 0.88f, 0.80f, 1f);
        sr.circle(cx + 44f * s, cy - 2f * s, 4f * s);
        // Inferior mouth line
        sr.setColor(0.20f, 0.30f, 0.22f, 1f);
        sr.rectLine(cx + 38f * s, cy - 6f * s, cx + 44f * s, cy - 4f * s, 1.5f * s);
        // Tiny sensory barbel (râu mép nhỏ)
        sr.setColor(0.85f, 0.40f, 0.35f, 1f);
        sr.rectLine(cx + 40f * s, cy - 6f * s, cx + 46f * s, cy - 11f * s, 1.3f * s);

        // 9. Legendary Ruby-Red Rimmed Eye (Đôi mắt viền đỏ ruby rực lửa)
        sr.setColor(0.95f, 0.15f, 0.15f, 1f); // Vivid Ruby Ring
        sr.circle(cx + 32f * s, cy + 4f * s, 6f * s);
        sr.setColor(1.0f, 0.75f, 0.25f, 1f); // Inner golden iris
        sr.circle(cx + 32.5f * s, cy + 4f * s, 4.2f * s);
        sr.setColor(0.08f, 0.10f, 0.12f, 1f); // Deep pupil
        sr.circle(cx + 33f * s, cy + 4f * s, 2.7f * s);
        sr.setColor(1f, 1f, 1f, 1f); // Specular glint
        sr.circle(cx + 34.2f * s, cy + 5.2f * s, 1.2f * s);
    }

    // =========================================================================
    // 8. CÁ TRẮM CỎ (Grass Carp: Giant Muscular Torpedo, Olive & Gold, Diamond Scales)
    // =========================================================================
    public static void drawCaTramCo(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float tailX = cx - 64f * s + wag * 5f * s;
        float tailY = cy + wag * 6.5f * s;

        // 1. Heavy Muscular Caudal Peduncle (Cuống đuôi dầy cộp)
        sr.setColor(0.32f, 0.40f, 0.22f, 1f);
        sr.triangle(cx - 24f * s, cy + 15f * s, cx - 24f * s, cy - 13f * s, tailX, tailY);

        // 2. Powerful Caudal Fin (Vây đuôi đại bàng rộng bản màu ô-liu vàng)
        sr.setColor(0.24f, 0.30f, 0.16f, 0.95f);
        sr.triangle(tailX, tailY, tailX - 28f * s, tailY + 30f * s, tailX - 10f * s, tailY);
        sr.triangle(tailX, tailY, tailX - 28f * s, tailY - 30f * s, tailX - 10f * s, tailY);
        // Golden olive web
        sr.setColor(0.50f, 0.58f, 0.32f, 0.90f);
        sr.triangle(tailX - 4f * s, tailY, tailX - 26f * s, tailY + 28f * s, tailX - 14f * s, tailY + 10f * s);
        sr.triangle(tailX - 4f * s, tailY, tailX - 26f * s, tailY - 28f * s, tailX - 14f * s, tailY - 10f * s);
        // Heavy fin ray ridges
        sr.setColor(0.20f, 0.26f, 0.14f, 1f);
        for (int r = -2; r <= 2; r++) {
            sr.rectLine(tailX, tailY, tailX - 25f * s, tailY + r * 11f * s, 1.8f * s);
        }

        // 3. Thick Ventral & Anal Fins
        sr.setColor(0.45f, 0.52f, 0.28f, 0.95f);
        sr.triangle(cx - 18f * s, cy - 15f * s, cx - 6f * s, cy - 15f * s, cx - 16f * s, cy - 28f * s);
        sr.triangle(cx + 4f * s, cy - 17f * s, cx + 16f * s, cy - 17f * s, cx + 6f * s, cy - 30f * s);

        // 4. Compact Stout Dorsal Fin (Vây lưng dày dặn dựng đứng)
        sr.setColor(0.28f, 0.36f, 0.18f, 1f);
        sr.triangle(cx - 14f * s, cy + 15f * s, cx + 16f * s, cy + 15f * s, cx - 4f * s, cy + 32f * s);
        sr.setColor(0.48f, 0.56f, 0.30f, 0.85f);
        sr.triangle(cx - 8f * s, cy + 22f * s, cx + 14f * s, cy + 15f * s, cx - 4f * s, cy + 32f * s);
        for (int r = 0; r < 3; r++) {
            sr.setColor(0.18f, 0.24f, 0.12f, 1f);
            sr.rectLine(cx - 10f * s + r * 9f * s, cy + 15f * s, cx - 4f * s + r * 6f * s, cy + 28f * s - r * 4f * s, 1.5f * s);
        }

        // 5. Giant Cylindrical Torpedo Body (Thân khúc gỗ trụ dài vạm vỡ)
        // Dark olive dorsal ridge
        sr.setColor(0.25f, 0.32f, 0.18f, 1f);
        sr.circle(cx, cy + 4f * s, 25f * s, 32);
        sr.circle(cx - 18f * s, cy + 3f * s, 20f * s, 28);
        sr.circle(cx + 18f * s, cy + 3f * s, 21f * s, 28);

        // Golden olive flank (Lườn vàng lục ánh kim)
        sr.setColor(0.48f, 0.58f, 0.34f, 1f);
        sr.circle(cx, cy, 24f * s, 32);
        sr.circle(cx - 16f * s, cy, 19f * s, 28);
        sr.circle(cx + 16f * s, cy, 20f * s, 28);

        // Warm ivory belly
        sr.setColor(0.85f, 0.88f, 0.72f, 1f);
        sr.circle(cx + 2f * s, cy - 8f * s, 17f * s, 32);
        sr.circle(cx - 12f * s, cy - 7f * s, 14f * s, 28);

        // 6. Distinct Net-Patterned Diamond Scales (Hàng vảy mắt lưới quả trám trứ danh)
        for (int row = 0; row < 3; row++) {
            float sy = cy + 6f * s - row * 7f * s;
            for (int col = 0; col < 6; col++) {
                float sx = cx - 22f * s + col * 8f * s + (row % 2) * 4f * s;
                // Dark scale outline
                sr.setColor(0.18f, 0.25f, 0.12f, 0.85f);
                sr.circle(sx, sy, 4.5f * s);
                // Golden core
                sr.setColor(0.62f, 0.70f, 0.42f, 0.90f);
                sr.circle(sx - 0.8f * s, sy, 3.2f * s);
            }
        }

        // 7. Broad Blunt Head & Thick Firm Lips (Đầu to tù, miệng rộng gặm cỏ)
        sr.setColor(0.38f, 0.46f, 0.26f, 1f);
        sr.circle(cx + 34f * s, cy, 17f * s);
        sr.triangle(cx + 26f * s, cy + 14f * s, cx + 26f * s, cy - 12f * s, cx + 49f * s, cy);

        // Gill plate with golden sheen
        sr.setColor(0.65f, 0.68f, 0.40f, 0.75f);
        sr.circle(cx + 26f * s, cy, 14f * s);

        // Pectoral fin
        sr.setColor(0.38f, 0.45f, 0.24f, 0.95f);
        sr.triangle(cx + 20f * s, cy - 5f * s, cx + 28f * s, cy - 17f * s, cx + 12f * s, cy - 20f * s);
        sr.setColor(0.60f, 0.68f, 0.38f, 0.85f);
        sr.triangle(cx + 20f * s, cy - 5f * s, cx + 25f * s, cy - 15f * s, cx + 15f * s, cy - 17f * s);

        // Blunt snout & broad mouth (no barbels)
        sr.setColor(0.32f, 0.38f, 0.22f, 1f);
        sr.circle(cx + 47f * s, cy - 1f * s, 5f * s);
        sr.setColor(0.14f, 0.18f, 0.10f, 1f);
        sr.rectLine(cx + 42f * s, cy - 5f * s, cx + 49f * s, cy - 3f * s, 2.2f * s);

        // 8. Wise Golden-Amber Eye (Mắt vàng tĩnh tại thông thái)
        sr.setColor(0.95f, 0.82f, 0.28f, 1f); // Golden iris
        sr.circle(cx + 36f * s, cy + 5f * s, 6.2f * s);
        sr.setColor(0.08f, 0.10f, 0.08f, 1f); // Large black pupil
        sr.circle(cx + 37f * s, cy + 5f * s, 3.6f * s);
        sr.setColor(1f, 1f, 1f, 1f); // Specular highlight
        sr.circle(cx + 38.5f * s, cy + 6.5f * s, 1.4f * s);
    }

    // =========================================================================
    // 9. CÁ CHIM TRẮNG (White Pacu: Diamond Disc Body, Fiery Red Throat & Fins, Black Tail Rim)
    // =========================================================================
    public static void drawCaChimTrang(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float tailX = cx - 58f * s + wag * 5f * s;
        float tailY = cy + wag * 6f * s;

        // 1. Broad Caudal Fin with Jet-Black Trailing Margin (Vây đuôi viền đen nhánh)
        // Silver-white fin base
        sr.setColor(0.80f, 0.85f, 0.92f, 0.95f);
        sr.triangle(cx - 24f * s, cy, tailX - 26f * s, tailY + 28f * s, tailX - 10f * s, tailY);
        sr.triangle(cx - 24f * s, cy, tailX - 26f * s, tailY - 28f * s, tailX - 10f * s, tailY);
        // JET-BLACK TRAILING MARGIN (Đặc trưng viền đen đuôi cá chim trắng)
        sr.setColor(0.10f, 0.12f, 0.15f, 0.98f);
        sr.triangle(tailX - 20f * s, tailY + 20f * s, tailX - 28f * s, tailY + 29f * s, tailX - 26f * s, tailY + 12f * s);
        sr.triangle(tailX - 20f * s, tailY - 20f * s, tailX - 28f * s, tailY - 29f * s, tailX - 26f * s, tailY - 12f * s);
        sr.rectLine(tailX - 26f * s, tailY + 28f * s, tailX - 26f * s, tailY - 28f * s, 2.5f * s);
        // Fine fin rays
        sr.setColor(1f, 1f, 1f, 0.8f);
        sr.rectLine(cx - 24f * s, cy, tailX - 24f * s, tailY + 22f * s, 1.3f * s);
        sr.rectLine(cx - 24f * s, cy, tailX - 24f * s, tailY - 22f * s, 1.3f * s);

        // 2. VIVID FIERY-RED ANAL FIN & BELLY FINS (Vùng vây hậu môn & vây bụng đỏ cờ rực lửa)
        sr.setColor(0.95f, 0.18f, 0.12f, 1f);
        sr.triangle(cx - 18f * s, cy - 18f * s, cx + 8f * s, cy - 22f * s, cx - 6f * s, cy - 38f * s);
        sr.triangle(cx + 6f * s, cy - 22f * s, cx + 18f * s, cy - 20f * s, cx + 10f * s, cy - 35f * s);
        // Black tip on anal fin
        sr.setColor(0.12f, 0.10f, 0.12f, 1f);
        sr.triangle(cx - 10f * s, cy - 32f * s, cx - 2f * s, cy - 32f * s, cx - 6f * s, cy - 38f * s);
        // Fin rays
        sr.setColor(1f, 0.45f, 0.40f, 0.9f);
        sr.rectLine(cx - 8f * s, cy - 20f * s, cx - 6f * s, cy - 36f * s, 1.4f * s);
        sr.rectLine(cx + 10f * s, cy - 21f * s, cx + 10f * s, cy - 33f * s, 1.4f * s);

        // 3. Tall Dorsal Fin & Adipose Fin (Vây lưng cao & vây mỡ)
        // High falcate dorsal fin
        sr.setColor(0.40f, 0.46f, 0.54f, 1f);
        sr.triangle(cx - 10f * s, cy + 24f * s, cx + 14f * s, cy + 24f * s, cx - 2f * s, cy + 42f * s);
        sr.setColor(0.68f, 0.75f, 0.85f, 0.85f);
        sr.triangle(cx - 6f * s, cy + 28f * s, cx + 12f * s, cy + 24f * s, cx - 2f * s, cy + 42f * s);
        // Tiny adipose fin (vây mỡ)
        sr.setColor(0.35f, 0.40f, 0.48f, 1f);
        sr.triangle(cx - 20f * s, cy + 15f * s, cx - 14f * s, cy + 16f * s, cx - 18f * s, cy + 22f * s);

        // 4. Heavy Diamond Disc Body (Thân đĩa trám cao vạm vỡ màu bạc bạch kim)
        // Steel-gray dorsal hump
        sr.setColor(0.42f, 0.48f, 0.58f, 1f);
        sr.circle(cx, cy + 8f * s, 28f * s, 36);
        sr.circle(cx - 10f * s, cy + 6f * s, 23f * s, 32);

        // Bright platinum-silver main body
        sr.setColor(0.86f, 0.91f, 0.97f, 1f);
        sr.circle(cx, cy, 30f * s, 36);
        sr.circle(cx - 8f * s, cy, 26f * s, 32);
        sr.circle(cx + 8f * s, cy, 26f * s, 32);

        // 5. FIERY RED CHEST & THROAT (Ngực & Họng đỏ cờ rực rỡ đặc trưng)
        sr.setColor(0.95f, 0.20f, 0.15f, 0.95f);
        sr.circle(cx + 16f * s, cy - 12f * s, 14f * s);
        sr.circle(cx + 8f * s, cy - 15f * s, 12f * s);
        sr.setColor(1.0f, 0.42f, 0.35f, 0.80f);
        sr.circle(cx + 18f * s, cy - 10f * s, 9f * s);

        // 6. Scattered Black Flecks on Upper Body (Các chấm đốm đen li ti trên lưng)
        sr.setColor(0.18f, 0.22f, 0.28f, 0.70f);
        float[] spotXs = { cx - 14f * s, cx - 4f * s, cx + 6f * s, cx - 8f * s, cx + 2f * s, cx + 12f * s };
        float[] spotYs = { cy + 12f * s, cy + 15f * s, cy + 11f * s, cy + 6f * s, cy + 7f * s, cy + 4f * s };
        for (int i = 0; i < spotXs.length; i++) {
            sr.circle(spotXs[i], spotYs[i], 1.6f * s);
        }

        // 7. Powerful Head & Strong Jaw (Đầu dẹp, hàm ăn tạp cơ bắp)
        sr.setColor(0.78f, 0.84f, 0.90f, 1f);
        sr.triangle(cx + 18f * s, cy + 20f * s, cx + 18f * s, cy - 16f * s, cx + 46f * s, cy + 2f * s);
        // Snout & robust jutting lower jaw
        sr.setColor(0.68f, 0.74f, 0.82f, 1f);
        sr.circle(cx + 42f * s, cy + 2f * s, 7f * s);
        // Mouth line with crushing bite
        sr.setColor(0.14f, 0.16f, 0.22f, 1f);
        sr.rectLine(cx + 36f * s, cy - 2f * s, cx + 45f * s, cy + 1f * s, 2f * s);

        // Pectoral fin (silver-translucent with red base)
        sr.setColor(0.95f, 0.25f, 0.20f, 0.85f);
        sr.circle(cx + 18f * s, cy - 4f * s, 4f * s);
        sr.setColor(0.80f, 0.88f, 0.95f, 0.90f);
        sr.triangle(cx + 18f * s, cy - 4f * s, cx + 24f * s, cy - 16f * s, cx + 12f * s, cy - 18f * s);

        // 8. Fierce Metallic-Silver Eye (Mắt bạc viền đen sắc lẹm)
        sr.setColor(0.20f, 0.24f, 0.30f, 1f); // Dark outer ring
        sr.circle(cx + 30f * s, cy + 7f * s, 6.2f * s);
        sr.setColor(0.92f, 0.94f, 0.98f, 1f); // Bright silver iris
        sr.circle(cx + 30.5f * s, cy + 7f * s, 4.5f * s);
        sr.setColor(0.08f, 0.10f, 0.14f, 1f); // Black pupil
        sr.circle(cx + 31f * s, cy + 7f * s, 2.8f * s);
        sr.setColor(1f, 1f, 1f, 1f); // Glint
        sr.circle(cx + 32.2f * s, cy + 8.2f * s, 1.2f * s);
    }

    // =========================================================================
    // 10. CÁ LÓC BÔNG (Giant Chevron Snakehead: Apex Swamp Predator, Tiger Camo Blotches, Menacing Teeth)
    // =========================================================================
    public static void drawCaLocBong(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float tailX = cx - 66f * s + wag * 6.5f * s;
        float tailY = cy + wag * 7.5f * s;

        // 1. Muscular Peduncle & Broad Rounded Caudal Fin (Đuôi quạt tròn rằn ri cẩm thạch)
        // Dark base
        sr.setColor(0.14f, 0.20f, 0.12f, 1f);
        sr.triangle(cx - 32f * s, cy + 12f * s, cx - 32f * s, cy - 10f * s, tailX, tailY);
        // Rounded fan caudal fin
        sr.setColor(0.20f, 0.28f, 0.16f, 0.98f);
        sr.circle(tailX - 10f * s, tailY, 20f * s, 26);
        // Concentric tiger camouflage bands on tail
        sr.setColor(0.10f, 0.15f, 0.08f, 0.95f);
        sr.circle(tailX - 12f * s, tailY, 15f * s, 22);
        sr.setColor(0.32f, 0.44f, 0.24f, 0.90f);
        sr.circle(tailX - 14f * s, tailY, 10f * s, 18);
        // Fin rays
        sr.setColor(0.12f, 0.18f, 0.10f, 1f);
        for (int r = -2; r <= 2; r++) {
            sr.rectLine(tailX, tailY, tailX - 22f * s, tailY + r * 8f * s, 1.5f * s);
        }

        // 2. Long Predatory Dorsal Fin Running Across Whole Back (Vây lưng dài đặc trưng cá lóc)
        sr.setColor(0.16f, 0.22f, 0.12f, 1f);
        for (int i = 0; i < 9; i++) {
            float fx = cx - 34f * s + i * 8.5f * s;
            sr.rectLine(fx, cy + 10f * s, fx + 3f * s, cy + 24f * s, 2.5f * s);
            // Emerald tip
            sr.setColor(0.35f, 0.55f, 0.28f, 0.9f);
            sr.circle(fx + 3f * s, cy + 24f * s, 1.8f * s);
            sr.setColor(0.16f, 0.22f, 0.12f, 1f);
        }

        // 3. Elongated Anal Fin (Vây hậu môn dài dọc bụng dưới)
        sr.setColor(0.18f, 0.24f, 0.14f, 1f);
        for (int i = 0; i < 6; i++) {
            float ax = cx - 28f * s + i * 8f * s;
            sr.rectLine(ax, cy - 8f * s, ax + 2f * s, cy - 18f * s, 2.2f * s);
        }

        // 4. Elongated Muscular Predator Torpedo Body (Thân ngư lôi săn mồi lực lưỡng)
        // Dark moss-black dorsal ridge
        sr.setColor(0.14f, 0.20f, 0.12f, 1f);
        sr.circle(cx, cy + 3f * s, 22f * s, 30);
        sr.circle(cx - 20f * s, cy + 2f * s, 18f * s, 26);
        sr.circle(cx + 18f * s, cy + 2f * s, 19f * s, 26);

        // Rich olive-emerald flanks (Lườn xanh rêu săn mồi)
        sr.setColor(0.28f, 0.40f, 0.22f, 1f);
        sr.circle(cx, cy, 20f * s, 30);
        sr.circle(cx - 18f * s, cy, 16f * s, 26);
        sr.circle(cx + 16f * s, cy, 17f * s, 26);

        // Creamy pale belly with dark speckles (Bụng trắng ngà điểm đốm)
        sr.setColor(0.85f, 0.88f, 0.75f, 1f);
        sr.circle(cx + 2f * s, cy - 8f * s, 13f * s, 28);
        sr.circle(cx - 14f * s, cy - 7f * s, 11f * s, 24);

        // 5. Signature Chevron / Tiger Camo Blotches (Hoa văn rằn ri sọc cọp chữ V trứ danh)
        sr.setColor(0.08f, 0.12f, 0.06f, 0.95f);
        for (int i = 0; i < 6; i++) {
            float bx = cx - 30f * s + i * 10f * s;
            // Downward Chevron / Jagged stripes
            sr.triangle(bx, cy + 12f * s, bx + 7f * s, cy - 5f * s, bx - 3f * s, cy + 3f * s);
            sr.circle(bx + 4f * s, cy + 2f * s, 4f * s);
            sr.circle(bx + 8f * s, cy - 4f * s, 2.8f * s);
        }

        // 6. Broad Snake-like Armored Head & Protruding Lower Jaw (Đầu rắn dẹp bè, hàm dưới chìa)
        sr.setColor(0.22f, 0.32f, 0.18f, 1f);
        sr.circle(cx + 34f * s, cy, 16f * s);
        sr.triangle(cx + 22f * s, cy + 14f * s, cx + 22f * s, cy - 12f * s, cx + 52f * s, cy - 1f * s);

        // Broad predatory cheek & gill cover
        sr.setColor(0.35f, 0.46f, 0.26f, 0.85f);
        sr.circle(cx + 26f * s, cy, 12f * s);

        // Pectoral fin (Fan-shaped hunter fin)
        sr.setColor(0.25f, 0.35f, 0.20f, 0.95f);
        sr.triangle(cx + 18f * s, cy - 4f * s, cx + 26f * s, cy - 18f * s, cx + 10f * s, cy - 20f * s);
        sr.setColor(0.40f, 0.55f, 0.32f, 0.85f);
        sr.triangle(cx + 18f * s, cy - 4f * s, cx + 23f * s, cy - 15f * s, cx + 13f * s, cy - 17f * s);

        // Protruding menacing lower jaw with needle-sharp teeth (Hàm dưới chìa dài lởm chởm răng sắc)
        sr.setColor(0.14f, 0.20f, 0.12f, 1f);
        sr.rectLine(cx + 32f * s, cy - 8f * s, cx + 55f * s, cy - 2f * s, 3.2f * s);
        // Needle teeth (Răng nanh cá lóc)
        sr.setColor(0.95f, 0.95f, 0.90f, 1f);
        for (int t = 0; t < 4; t++) {
            float tx = cx + 40f * s + t * 3.5f * s;
            sr.rectLine(tx, cy - 4f * s, tx, cy - 1f * s, 1.2f * s);
        }

        // 7. Fierce Fiery-Amber Predator Eye (Đôi mắt hổ phách rực lửa săn mồi)
        sr.setColor(0.85f, 0.15f, 0.10f, 1f); // Red predatory eye ring
        sr.circle(cx + 38f * s, cy + 6f * s, 6.2f * s);
        sr.setColor(1.0f, 0.75f, 0.15f, 1f); // Fiery amber iris
        sr.circle(cx + 38.5f * s, cy + 6f * s, 4.5f * s);
        sr.setColor(0.06f, 0.08f, 0.06f, 1f); // Slit black pupil
        sr.rectLine(cx + 39f * s, cy + 3.5f * s, cx + 39f * s, cy + 8.5f * s, 2.2f * s);
        sr.setColor(1f, 1f, 1f, 1f); // Specular glint
        sr.circle(cx + 40.2f * s, cy + 7.2f * s, 1.2f * s);
    }

    // =========================================================================
    // 11. CÁ TRÊ LAI (Giant Marbled Hybrid Catfish: Marbled Leopard Print, Bony Skull, 4 Whiskers)
    // =========================================================================
    public static void drawCaTreLai(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float tailX = cx - 64f * s + wag * 6.5f * s;
        float tailY = cy + wag * 7.5f * s;

        // 1. Long Continuous Dorsal & Anal Ribbon Fins (Dải vây lưng & vây hậu môn uốn lượn)
        sr.setColor(0.20f, 0.16f, 0.10f, 1f);
        // Dorsal fin ribbon
        for (int i = 0; i < 9; i++) {
            float dx = cx - 32f * s + i * 8.5f * s;
            sr.rectLine(dx, cy + 12f * s, dx - 2f * s, cy + 22f * s, 2.2f * s);
            sr.setColor(0.55f, 0.45f, 0.20f, 0.85f);
            sr.circle(dx - 2f * s, cy + 22f * s, 1.8f * s);
            sr.setColor(0.20f, 0.16f, 0.10f, 1f);
        }
        // Anal fin ribbon
        for (int i = 0; i < 7; i++) {
            float ax = cx - 28f * s + i * 8f * s;
            sr.rectLine(ax, cy - 10f * s, ax - 2f * s, cy - 20f * s, 2.2f * s);
        }

        // 2. Powerful Caudal Fin (Vây đuôi tròn dầy dặn)
        sr.setColor(0.22f, 0.18f, 0.12f, 1f);
        sr.triangle(cx - 30f * s, cy + 10f * s, cx - 30f * s, cy - 10f * s, tailX, tailY);
        sr.setColor(0.28f, 0.22f, 0.14f, 0.98f);
        sr.circle(tailX - 10f * s, tailY, 19f * s, 24);
        sr.setColor(0.60f, 0.50f, 0.22f, 0.85f);
        sr.circle(tailX - 12f * s, tailY, 13f * s, 20);

        // 3. Heavy Stout Catfish Body (Thân đầm chắc màu da báo cẩm thạch)
        // Dark brown-olive upper dorsal
        sr.setColor(0.24f, 0.18f, 0.10f, 1f);
        sr.circle(cx, cy + 2f * s, 24f * s, 32);
        sr.circle(cx - 18f * s, cy + 1f * s, 19f * s, 26);
        sr.circle(cx + 18f * s, cy + 1f * s, 21f * s, 26);

        // Rich marbled golden ochre flanks (Lườn vàng nghệ cẩm thạch)
        sr.setColor(0.68f, 0.56f, 0.22f, 1f);
        sr.circle(cx, cy, 21f * s, 30);
        sr.circle(cx - 16f * s, cy, 17f * s, 26);
        sr.circle(cx + 16f * s, cy, 18f * s, 26);

        // Soft ivory-yellow belly
        sr.setColor(0.88f, 0.85f, 0.68f, 1f);
        sr.circle(cx + 2f * s, cy - 8f * s, 14f * s, 28);
        sr.circle(cx - 12f * s, cy - 7f * s, 11f * s, 24);

        // 4. Exquisite Marbled Leopard Spots (Hoa văn da báo cẩm thạch đốm vàng đen xen kẽ)
        for (int i = 0; i < 7; i++) {
            float sx = cx - 26f * s + i * 9.5f * s;
            // Dark irregular blotches
            sr.setColor(0.12f, 0.08f, 0.05f, 0.95f);
            sr.circle(sx, cy + 5f * s, 4.8f * s);
            sr.circle(sx + 3f * s, cy - 3f * s, 4.2f * s);
            // Golden marble highlight center
            sr.setColor(0.82f, 0.70f, 0.28f, 0.90f);
            sr.circle(sx - 0.8f * s, cy + 4.5f * s, 2.6f * s);
            sr.circle(sx + 2.2f * s, cy - 3.5f * s, 2.2f * s);
        }

        // 5. Broad Depressed Armored Spade Head (Đầu bẹt bọc giáp xương dầy cộp)
        sr.setColor(0.28f, 0.22f, 0.12f, 1f);
        sr.circle(cx + 34f * s, cy, 17f * s);
        sr.triangle(cx + 22f * s, cy + 15f * s, cx + 22f * s, cy - 13f * s, cx + 52f * s, cy);

        // Armored cranial helmet plates (Vết rãnh giáp đầu xương)
        sr.setColor(0.18f, 0.14f, 0.08f, 1f);
        sr.rectLine(cx + 26f * s, cy + 10f * s, cx + 44f * s, cy + 4f * s, 1.8f * s);
        sr.rectLine(cx + 26f * s, cy - 10f * s, cx + 44f * s, cy - 4f * s, 1.8f * s);

        // Broad transverse mouth with fleshy lips
        sr.setColor(0.12f, 0.08f, 0.05f, 1f);
        sr.rectLine(cx + 42f * s, cy - 4f * s, cx + 52f * s, cy - 2f * s, 2.8f * s);

        // Pectoral fin with serrated defensive bone spine (Ngạnh sắc bén cá trê)
        sr.setColor(0.90f, 0.88f, 0.78f, 1f); // Bone spine
        sr.rectLine(cx + 18f * s, cy - 6f * s, cx + 28f * s, cy - 22f * s, 2.8f * s);
        sr.setColor(0.25f, 0.20f, 0.12f, 0.95f); // Fin web
        sr.triangle(cx + 18f * s, cy - 6f * s, cx + 28f * s, cy - 22f * s, cx + 12f * s, cy - 22f * s);

        // 6. 4 Dynamic Catfish Barbels (4 chiếc râu dài linh hoạt)
        sr.setColor(0.22f, 0.16f, 0.10f, 1f);
        // Upper long whiskers (Râu mép dài vuốt ngược theo dòng nước)
        sr.rectLine(cx + 46f * s, cy + 6f * s, cx + 68f * s, cy + 24f * s + wag * 3.5f * s, 2.8f * s);
        sr.rectLine(cx + 46f * s, cy - 4f * s, cx + 68f * s, cy - 22f * s - wag * 3.5f * s, 2.8f * s);
        // Lower chin barbels (2 râu cằm ngắn hơn)
        sr.setColor(0.35f, 0.28f, 0.16f, 1f);
        sr.rectLine(cx + 48f * s, cy + 2f * s, cx + 60f * s, cy + 12f * s + wag * 2f * s, 1.8f * s);
        sr.rectLine(cx + 48f * s, cy - 2f * s, cx + 60f * s, cy - 12f * s - wag * 2f * s, 1.8f * s);

        // 7. Small Nocturnal Predator Eyes (Đôi mắt vàng ti hí săn đêm)
        sr.setColor(0.12f, 0.10f, 0.08f, 1f);
        sr.circle(cx + 36f * s, cy + 8f * s, 5f * s);
        sr.setColor(0.95f, 0.82f, 0.25f, 1f);
        sr.circle(cx + 36.5f * s, cy + 8f * s, 3.6f * s);
        sr.setColor(0.08f, 0.08f, 0.08f, 1f);
        sr.circle(cx + 37f * s, cy + 8f * s, 2.2f * s);
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + 37.8f * s, cy + 8.8f * s, 1f * s);
    }

    // =========================================================================
    // 12. CÁ CHÉP VÀNG (Imperial Golden Koi: Epic Radiant Aura, Flowing Butterfly Fins, 3D Dragon Scales)
    // =========================================================================
    public static void drawCaChepVang(ShapeRenderer sr, float cx, float cy, float s, float wag, float timer) {
        float tailX = cx - 68f * s + wag * 6.5f * s;
        float tailY = cy + wag * 8.5f * s;

        // 1. Mythical Radiant Golden Halo Aura (Hào quang hoàng kim linh thiêng)
        float pulse = 0.82f + 0.18f * MathUtils.sin(timer * 5f);
        sr.setColor(1f, 0.85f, 0.20f, 0.32f * pulse);
        sr.circle(cx, cy, 52f * s);
        sr.setColor(1f, 0.95f, 0.45f, 0.18f * pulse);
        sr.circle(cx, cy, 40f * s);

        // 2. Flowing Celestial Butterfly Caudal Fin (Vây đuôi bướm hoàng gia thướt tha uốn lượn)
        sr.setColor(0.95f, 0.65f, 0.12f, 0.95f);
        // Base peduncle
        sr.triangle(cx - 30f * s, cy + 12f * s, cx - 30f * s, cy - 10f * s, tailX, tailY);

        // Upper flowing butterfly lobe
        sr.setColor(1.0f, 0.78f, 0.18f, 0.95f);
        sr.triangle(tailX, tailY, tailX - 36f * s, tailY + 36f * s + wag * 4f * s, tailX - 10f * s, tailY + 8f * s);
        // Lower flowing butterfly lobe
        sr.triangle(tailX, tailY, tailX - 36f * s, tailY - 36f * s - wag * 4f * s, tailX - 10f * s, tailY - 8f * s);

        // Trailing transparent golden veil
        sr.setColor(1.0f, 0.92f, 0.45f, 0.80f);
        sr.triangle(tailX - 8f * s, tailY, tailX - 32f * s, tailY + 32f * s + wag * 3f * s, tailX - 16f * s, tailY + 12f * s);
        sr.triangle(tailX - 8f * s, tailY, tailX - 32f * s, tailY - 32f * s - wag * 3f * s, tailX - 16f * s, tailY - 12f * s);

        // Golden fin rays
        sr.setColor(0.85f, 0.55f, 0.08f, 1f);
        for (int r = -3; r <= 3; r++) {
            sr.rectLine(tailX, tailY, tailX - 32f * s, tailY + r * 10f * s, 1.5f * s);
        }

        // 3. Flowing Arched Dorsal Fin (Vây lưng cong vút hoàng kim)
        sr.setColor(0.95f, 0.65f, 0.10f, 1f);
        sr.triangle(cx - 24f * s, cy + 18f * s, cx + 22f * s, cy + 18f * s, cx - 2f * s, cy + 38f * s);
        sr.setColor(1.0f, 0.90f, 0.35f, 0.85f);
        sr.triangle(cx - 16f * s, cy + 24f * s, cx + 18f * s, cy + 18f * s, cx - 2f * s, cy + 38f * s);
        // Dorsal fin rays
        sr.setColor(0.82f, 0.50f, 0.06f, 1f);
        for (int i = 0; i < 4; i++) {
            sr.rectLine(cx - 16f * s + i * 10f * s, cy + 18f * s, cx - 4f * s + i * 6f * s, cy + 34f * s - i * 4f * s, 1.4f * s);
        }

        // 4. Majestic Deep Humped Body (Thân cá chép gù dũng mãnh, đầy đặn)
        // Dorsal deep gold-amber crest
        sr.setColor(0.92f, 0.62f, 0.10f, 1f);
        sr.circle(cx, cy + 4f * s, 28f * s, 32);
        sr.circle(cx - 18f * s, cy + 3f * s, 23f * s, 26);
        sr.circle(cx + 18f * s, cy + 3f * s, 23f * s, 26);

        // Radiant 24k Pure Gold Body Flanks
        sr.setColor(1.0f, 0.82f, 0.16f, 1f);
        sr.circle(cx, cy, 27f * s, 32);
        sr.circle(cx - 16f * s, cy, 22f * s, 26);
        sr.circle(cx + 16f * s, cy, 22f * s, 26);

        // Shimmering mother-of-pearl / white-gold belly
        sr.setColor(1.0f, 0.98f, 0.75f, 1f);
        sr.circle(cx + 2f * s, cy - 9f * s, 19f * s, 28);
        sr.circle(cx - 12f * s, cy - 8f * s, 15f * s, 24);

        // 5. 3D Golden Dragon Scales (Vảy rồng hoàng kim xếp lớp tinh xảo)
        for (int row = 0; row < 3; row++) {
            float sy = cy + 7f * s - row * 7.5f * s;
            for (int col = 0; col < 5; col++) {
                float sx = cx - 20f * s + col * 9f * s + (row % 2) * 4.5f * s;
                // Scale outer amber ridge
                sr.setColor(0.85f, 0.52f, 0.06f, 0.90f);
                sr.circle(sx, sy, 5f * s);
                // Golden core
                sr.setColor(1.0f, 0.92f, 0.40f, 0.95f);
                sr.circle(sx - 1f * s, sy + 0.5f * s, 3.6f * s);
            }
        }

        // 6. Broad Noble Head & Gill Plates (Đầu cá chép quý phái, mang ánh kim)
        sr.setColor(1.0f, 0.80f, 0.18f, 1f);
        sr.circle(cx + 34f * s, cy, 18f * s);
        sr.triangle(cx + 24f * s, cy + 18f * s, cx + 24f * s, cy - 14f * s, cx + 52f * s, cy);

        // Metallic gold gill plate
        sr.setColor(1.0f, 0.92f, 0.45f, 0.75f);
        sr.circle(cx + 26f * s, cy, 14f * s);

        // Broad butterfly pectoral fin
        sr.setColor(0.95f, 0.65f, 0.12f, 0.95f);
        sr.triangle(cx + 18f * s, cy - 6f * s, cx + 28f * s, cy - 24f * s, cx + 10f * s, cy - 26f * s);
        sr.setColor(1.0f, 0.92f, 0.45f, 0.85f);
        sr.triangle(cx + 18f * s, cy - 6f * s, cx + 25f * s, cy - 20f * s, cx + 13f * s, cy - 22f * s);

        // Snout & fleshy mouth
        sr.setColor(0.95f, 0.75f, 0.16f, 1f);
        sr.circle(cx + 49f * s, cy - 2f * s, 6f * s);
        sr.setColor(0.70f, 0.42f, 0.06f, 1f);
        sr.rectLine(cx + 44f * s, cy - 6f * s, cx + 52f * s, cy - 4f * s, 2f * s);

        // 7. Dynamic Golden Whiskers (Râu cá chép phong thủy uốn lượn mềm mại)
        sr.setColor(1.0f, 0.96f, 0.55f, 1f);
        // Primary long whisker
        sr.rectLine(cx + 46f * s, cy - 5f * s, cx + 64f * s, cy - 18f * s + wag * 3f * s, 2.5f * s);
        // Secondary delicate whisker
        sr.rectLine(cx + 48f * s, cy - 2f * s, cx + 66f * s, cy - 8f * s + wag * 1.5f * s, 2.0f * s);

        // 8. Celestial Golden-Amber Eye (Mắt vàng ngọc bích linh thiêng)
        sr.setColor(0.75f, 0.45f, 0.05f, 1f); // Dark amber ring
        sr.circle(cx + 37f * s, cy + 6f * s, 6.5f * s);
        sr.setColor(1.0f, 0.95f, 0.35f, 1f); // Luminous gold iris
        sr.circle(cx + 37.5f * s, cy + 6f * s, 4.8f * s);
        sr.setColor(0.08f, 0.08f, 0.08f, 1f); // Black pupil
        sr.circle(cx + 38f * s, cy + 6f * s, 3f * s);
        sr.setColor(1f, 1f, 1f, 1f); // Brilliant diamond glint
        sr.circle(cx + 39.5f * s, cy + 7.5f * s, 1.4f * s);
    }

    // =========================================================================
    // 13. CÁ LĂNG KHỔNG LỒ (Titan Bagrid Catfish, Slate Blue Armor, Jagged Spines)
    // =========================================================================
    public static void drawCaLangKhongLo(ShapeRenderer sr, float cx, float cy, float s, float wag, float timer) {
        float tailX = cx - 72f * s + wag * 7f * s;
        float tailY = cy + wag * 8f * s;

        // 1. Deeply Bifurcated Predatory Caudal Fin (Đuôi chẻ sâu mạnh mẽ viền đỏ ngọc)
        // Outer dark abyssal navy membrane
        sr.setColor(0.12f, 0.16f, 0.26f, 0.95f);
        sr.triangle(cx - 36f * s, cy, tailX - 35f * s, tailY + 36f * s, tailX - 10f * s, tailY + 4f * s);
        sr.triangle(cx - 36f * s, cy, tailX - 35f * s, tailY - 36f * s, tailX - 10f * s, tailY - 4f * s);

        // Radiant electric ice-blue fin rays (5 upper, 5 lower)
        sr.setColor(0.35f, 0.70f, 0.95f, 0.85f);
        for (int i = 0; i < 5; i++) {
            sr.rectLine(cx - 34f * s, cy + 2f * s, tailX - (33f - i * 4f) * s, tailY + (34f - i * 7f) * s, 1.6f * s);
            sr.rectLine(cx - 34f * s, cy - 2f * s, tailX - (33f - i * 4f) * s, tailY - (34f - i * 7f) * s, 1.6f * s);
        }

        // Trailing edge crimson-tinted fin tip accents
        sr.setColor(0.70f, 0.20f, 0.32f, 0.85f);
        sr.triangle(tailX - 35f * s, tailY + 36f * s, tailX - 22f * s, tailY + 24f * s, tailX - 38f * s, tailY + 28f * s);
        sr.triangle(tailX - 35f * s, tailY - 36f * s, tailX - 22f * s, tailY - 24f * s, tailX - 38f * s, tailY - 28f * s);

        // 2. Adipose Fin (Vây mỡ đặc trưng cá lăng đáy sông sâu)
        sr.setColor(0.18f, 0.25f, 0.38f, 0.90f);
        sr.triangle(cx - 38f * s, cy + 12f * s, cx - 20f * s, cy + 14f * s, cx - 28f * s, cy + 25f * s);
        sr.setColor(0.35f, 0.65f, 0.90f, 0.80f);
        sr.rectLine(cx - 38f * s, cy + 12f * s, cx - 28f * s, cy + 25f * s, 1.5f * s);

        // 3. Giant Serrated Prehistoric Bone Dorsal Spine (Ngạnh lưng khổng lồ sắc nhọn như dao cưa)
        // Webbed membrane connecting spine to dorsal hump
        sr.setColor(0.20f, 0.28f, 0.42f, 0.75f);
        sr.triangle(cx - 16f * s, cy + 16f * s, cx + 12f * s, cy + 16f * s, cx - 4f * s, cy + 46f * s);
        // Fine supporting fin rays in membrane
        sr.setColor(0.35f, 0.55f, 0.80f, 0.85f);
        for (int r = 0; r < 3; r++) {
            sr.rectLine(cx - 8f * s + r * 6f * s, cy + 16f * s, cx - 4f * s + r * 2f * s, cy + (40f - r * 8f) * s, 1.4f * s);
        }
        // Colossal rigid ivory bone spine shaft
        sr.setColor(0.92f, 0.90f, 0.82f, 1f);
        sr.rectLine(cx - 8f * s, cy + 16f * s, cx - 4f * s, cy + 48f * s, 3.2f * s);
        // Jagged serrated barbs on rear edge of the spine
        sr.setColor(0.95f, 0.92f, 0.80f, 1f);
        for (int b = 0; b < 4; b++) {
            float by = cy + (22f + b * 6.5f) * s;
            float bx = cx - (7f - b * 0.8f) * s;
            sr.triangle(bx, by, bx - 4.5f * s, by - 2.5f * s, bx - 1.5f * s, by - 5f * s);
        }
        // Glowing electric blue energy core in spine
        sr.setColor(0.40f, 0.85f, 1.0f, 0.95f);
        sr.rectLine(cx - 7f * s, cy + 17f * s, cx - 4.2f * s, cy + 46f * s, 1.4f * s);

        // 4. Massive Titan Muscular Body & Slate-Blue Armor (Thân bọc giáp titan xanh phiến thạch)
        // Dark abyssal slate-navy dorsal mantle
        sr.setColor(0.16f, 0.22f, 0.34f, 1f);
        sr.circle(cx + 6f * s, cy + 4f * s, 26f * s, 30);
        sr.circle(cx - 16f * s, cy + 2f * s, 22f * s, 26);
        sr.circle(cx - 36f * s, cy, 15f * s, 20);

        // Titan slate-blue body flanks
        sr.setColor(0.24f, 0.32f, 0.46f, 1f);
        sr.circle(cx + 6f * s, cy, 25f * s, 30);
        sr.circle(cx - 14f * s, cy, 20f * s, 26);

        // Shimmering Moonlight-Silver Underbelly (Bụng ánh bạc phát quang huyền ảo)
        sr.setColor(0.72f, 0.82f, 0.92f, 1f);
        sr.circle(cx + 8f * s, cy - 8f * s, 19f * s, 26);
        sr.circle(cx - 10f * s, cy - 7f * s, 15f * s, 22);
        sr.setColor(0.88f, 0.94f, 0.98f, 1f);
        sr.circle(cx + 8f * s, cy - 11f * s, 14f * s, 24);

        // 5. Bioluminescent Lateral Line (Đường cảm giác phát sáng xanh băng)
        sr.setColor(0.35f, 0.75f, 1.0f, 0.65f);
        sr.rectLine(cx - 38f * s, cy, cx + 22f * s, cy + 3f * s, 1.5f * s);
        for (int p = 0; p < 6; p++) {
            float px = cx - 34f * s + p * 10f * s;
            float py = cy + 0.5f * s + (p * 0.4f) * s;
            float glowPulse = 0.7f + 0.3f * MathUtils.sin(timer * 4f + p * 0.8f);
            sr.setColor(0.55f, 0.90f, 1.0f, glowPulse);
            sr.circle(px, py, 2.0f * s);
        }

        // 6. Giant Serrated Pectoral Stings (Ngạnh ngực cá lăng như cặp dao găm sát thủ)
        sr.setColor(0.20f, 0.28f, 0.40f, 0.90f);
        sr.triangle(cx + 12f * s, cy - 6f * s, cx - 12f * s, cy - 26f * s, cx + 2f * s, cy - 28f * s);
        sr.setColor(0.92f, 0.90f, 0.82f, 1f);
        sr.rectLine(cx + 12f * s, cy - 6f * s, cx - 10f * s, cy - 28f * s, 2.8f * s);
        // Barbs on pectoral sting
        sr.setColor(0.95f, 0.92f, 0.80f, 1f);
        for (int pb = 0; pb < 3; pb++) {
            float pbx = cx + (6f - pb * 5.5f) * s;
            float pby = cy - (12f + pb * 5.5f) * s;
            sr.triangle(pbx, pby, pbx - 3.5f * s, pby + 1.5f * s, pbx - 1.5f * s, pby - 4f * s);
        }

        // 7. Broad Predatory Armored Catfish Head & Operculum (Đầu bọc giáp dẹp & nắp mang)
        sr.setColor(0.26f, 0.34f, 0.48f, 1f);
        sr.circle(cx + 34f * s, cy, 19f * s);
        sr.triangle(cx + 26f * s, cy + 18f * s, cx + 26f * s, cy - 14f * s, cx + 54f * s, cy + 1f * s);
        sr.circle(cx + 50f * s, cy + 1f * s, 7f * s); // Broad predatory snout

        // Metallic slate operculum gill plate
        sr.setColor(0.32f, 0.42f, 0.56f, 0.85f);
        sr.circle(cx + 26f * s, cy + 2f * s, 13f * s);
        // Undershot predatory mouth
        sr.setColor(0.12f, 0.16f, 0.24f, 1f);
        sr.rectLine(cx + 42f * s, cy - 4f * s, cx + 54f * s, cy - 1f * s, 2.2f * s);

        // 8. Iconic Long Sinuous Catfish Whiskers (4 Cặp râu cá lăng rồng uốn lượn uy dũng)
        // Upper primary maxillary barbels (sweeping back with dynamic wave)
        sr.setColor(0.18f, 0.24f, 0.36f, 1f);
        sr.rectLine(cx + 48f * s, cy + 6f * s, cx + 66f * s, cy + 20f * s, 3.2f * s);
        sr.rectLine(cx + 66f * s, cy + 20f * s, cx + 86f * s, cy + 30f * s + wag * 4.5f * s, 2.2f * s);
        // Bioluminescent glowing cyan whisker tip
        sr.setColor(0.40f, 0.85f, 1.0f, 0.95f);
        sr.circle(cx + 86f * s, cy + 30f * s + wag * 4.5f * s, 2.2f * s);

        // Lower secondary chin barbels
        sr.setColor(0.20f, 0.26f, 0.38f, 1f);
        sr.rectLine(cx + 48f * s, cy - 4f * s, cx + 68f * s, cy - 18f * s - wag * 3f * s, 2.4f * s);
        // Inner delicate sensory chin whisker
        sr.rectLine(cx + 52f * s, cy - 1f * s, cx + 66f * s, cy - 8f * s, 1.6f * s);

        // 9. Piercing Arctic Ice-Blue Predatory Eye (Mắt xanh băng lạnh lùng của loài săn mồi vực thẳm)
        sr.setColor(0.08f, 0.12f, 0.18f, 1f); // Deep socket
        sr.circle(cx + 36f * s, cy + 7f * s, 6.5f * s);
        sr.setColor(0.35f, 0.85f, 1.0f, 1f); // Glowing ice-cyan iris
        sr.circle(cx + 36.5f * s, cy + 7f * s, 4.8f * s);
        sr.setColor(0.04f, 0.06f, 0.10f, 1f); // Vertical predator slit pupil
        sr.circle(cx + 37f * s, cy + 7f * s, 2.8f * s);
        sr.setColor(1f, 1f, 1f, 1f); // Diamond specular glint
        sr.circle(cx + 38.5f * s, cy + 8.5f * s, 1.4f * s);
    }

    // =========================================================================
    // 14. THỦY QUÁI SÔNG TRÀ (Apex Mythic Boss Behemoth, Demon Eye, Magma Armor)
    // =========================================================================
    public static void drawThuyQuaiSongTra(ShapeRenderer sr, float cx, float cy, float s, float wag, float timer) {
        float pulse = 0.8f + 0.2f * MathUtils.sin(timer * 7f);
        float fastPulse = 0.75f + 0.25f * MathUtils.sin(timer * 12f);
        float tailX = cx - 78f * s + wag * 8.5f * s;
        float tailY = cy + wag * 9.5f * s;

        // 1. Mythic Demonic Abyssal Aura (Hào quang Huyết Ma Vực Thẳm đa tầng rực lửa)
        // Outer abyssal blood vortex
        sr.setColor(0.65f, 0.08f, 0.18f, 0.22f * pulse);
        sr.circle(cx, cy, 82f * s, 36);
        // Molten magma energy ring
        sr.setColor(0.95f, 0.22f, 0.08f, 0.28f * pulse);
        sr.circle(cx, cy, 65f * s, 32);
        // Inner hellfire core
        sr.setColor(1.0f, 0.45f, 0.10f, 0.20f * pulse);
        sr.circle(cx, cy, 48f * s, 28);

        // 6 Orbiting demonic flame embers (Tàn lửa ma quỷ xoay tròn quanh bạo chúa)
        for (int i = 0; i < 6; i++) {
            float angle = timer * 2.5f + i * (MathUtils.PI / 3f);
            float orbitR = (62f + (i % 2) * 12f) * s;
            float ex = cx + MathUtils.cos(angle) * orbitR;
            float ey = cy + MathUtils.sin(angle) * (orbitR * 0.65f);
            sr.setColor(1.0f, 0.35f + (i % 2) * 0.4f, 0.1f, 0.75f * fastPulse);
            sr.circle(ex, ey, (3.2f + (i % 2) * 1.5f) * s);
            sr.setColor(1.0f, 0.92f, 0.45f, 0.90f);
            sr.circle(ex, ey, 1.2f * s);
        }

        // 2. Colossal Scythe-Like Demon Caudal Fin (Đuôi lưỡi hái bạo chúa khổng lồ)
        // Upper scythe blade
        sr.setColor(0.28f, 0.06f, 0.12f, 1f);
        sr.triangle(cx - 42f * s, cy, tailX - 42f * s, tailY + 48f * s, tailX - 12f * s, tailY + 8f * s);
        sr.setColor(0.75f, 0.14f, 0.10f, 0.90f);
        sr.triangle(cx - 38f * s, cy, tailX - 35f * s, tailY + 40f * s, tailX - 14f * s, tailY + 6f * s);

        // Lower scythe blade
        sr.setColor(0.28f, 0.06f, 0.12f, 1f);
        sr.triangle(cx - 42f * s, cy, tailX - 42f * s, tailY - 48f * s, tailX - 12f * s, tailY - 8f * s);
        sr.setColor(0.75f, 0.14f, 0.10f, 0.90f);
        sr.triangle(cx - 38f * s, cy, tailX - 35f * s, tailY - 40f * s, tailX - 14f * s, tailY - 6f * s);

        // Radiating molten magma veins in caudal fin
        sr.setColor(1.0f, 0.40f, 0.10f, 0.85f * pulse);
        for (int v = 0; v < 3; v++) {
            sr.rectLine(cx - 38f * s, cy, tailX - (36f - v * 8f) * s, tailY + (38f - v * 12f) * s, 1.8f * s);
            sr.rectLine(cx - 38f * s, cy, tailX - (36f - v * 8f) * s, tailY - (38f - v * 12f) * s, 1.8f * s);
        }

        // 3 Vicious Trailing Bone Talons on caudal margin
        sr.setColor(0.92f, 0.88f, 0.75f, 1f);
        sr.triangle(tailX - 42f * s, tailY + 48f * s, tailX - 34f * s, tailY + 42f * s, tailX - 54f * s, tailY + 54f * s);
        sr.triangle(tailX - 42f * s, tailY - 48f * s, tailX - 34f * s, tailY - 42f * s, tailX - 54f * s, tailY - 54f * s);
        sr.triangle(tailX - 22f * s, tailY + 4f * s, tailX - 22f * s, tailY - 4f * s, tailX - 40f * s, tailY);
        // Bloodstained talon tips
        sr.setColor(0.90f, 0.15f, 0.15f, 0.95f);
        sr.circle(tailX - 54f * s, tailY + 54f * s, 2.5f * s);
        sr.circle(tailX - 54f * s, tailY - 54f * s, 2.5f * s);

        // 3. Prehistoric Dragon Bone Dorsal Spikes (7 Dãy gai bạo chúa tiền sử rực lửa)
        for (int i = 0; i < 7; i++) {
            float sx = cx - 36f * s + i * 11.5f * s;
            float spikeH = 26f + (float) Math.sin(i * 0.52f) * 24f; // Horns rise dramatically up to 50f * s

            // Obsidian armor root
            sr.setColor(0.18f, 0.10f, 0.18f, 1f);
            sr.triangle(sx - 6f * s, cy + 18f * s, sx + 6f * s, cy + 18f * s, sx, cy + (18f + spikeH) * s);

            // Prehistoric bone horn
            sr.setColor(0.90f, 0.84f, 0.70f, 1f);
            sr.triangle(sx - 4.5f * s, cy + 18f * s, sx + 4.5f * s, cy + 18f * s, sx - 1.5f * s, cy + (18f + spikeH) * s);

            // Molten volcanic magma fissure running inside horn
            sr.setColor(1.0f, 0.35f, 0.12f, 0.95f);
            sr.rectLine(sx, cy + 18f * s, sx - 1.2f * s, cy + (18f + spikeH - 4f) * s, 1.8f * s);

            // Needle-sharp glowing tip glint
            sr.setColor(1.0f, 0.88f, 0.40f, 1f);
            sr.circle(sx - 1.5f * s, cy + (18f + spikeH) * s, 1.6f * s);
        }

        // 4. Titan Prehistoric Leviathan Dragonhide Body (Thân bọc giáp rồng hắc ám)
        // Dorsal obsidian dragon plate
        sr.setColor(0.12f, 0.08f, 0.15f, 1f);
        sr.circle(cx + 8f * s, cy + 4f * s, 32f * s, 32);
        sr.circle(cx - 18f * s, cy + 3f * s, 26f * s, 28);
        sr.circle(cx - 40f * s, cy, 18f * s, 24);

        // Main obsidian violet-black torso
        sr.setColor(0.18f, 0.12f, 0.20f, 1f);
        sr.circle(cx + 8f * s, cy, 30f * s, 32);
        sr.circle(cx - 16f * s, cy, 24f * s, 28);

        // Volcanic deep crimson lower flanks
        sr.setColor(0.42f, 0.10f, 0.14f, 1f);
        sr.circle(cx + 10f * s, cy - 10f * s, 22f * s, 28);
        sr.circle(cx - 12f * s, cy - 9f * s, 17f * s, 24);

        // 5. Dynamic Molten Volcanic Fissures (Hệ thống vết nứt magma rực lửa chạy dọc thân)
        // Upper fissure halo
        sr.setColor(0.95f, 0.18f, 0.08f, 0.90f * pulse);
        sr.rectLine(cx - 38f * s, cy + 8f * s, cx - 10f * s, cy + 12f * s, 4.0f * s);
        sr.rectLine(cx - 10f * s, cy + 12f * s, cx + 22f * s, cy + 8f * s, 3.5f * s);
        // Upper fissure incandescent core
        sr.setColor(1.0f, 0.82f, 0.25f, 1f);
        sr.rectLine(cx - 36f * s, cy + 8f * s, cx - 10f * s, cy + 12f * s, 2.0f * s);
        sr.rectLine(cx - 10f * s, cy + 12f * s, cx + 20f * s, cy + 8f * s, 1.8f * s);

        // Lower fissure halo
        sr.setColor(0.95f, 0.18f, 0.08f, 0.85f * pulse);
        sr.rectLine(cx - 30f * s, cy - 6f * s, cx - 6f * s, cy - 8f * s, 3.2f * s);
        sr.rectLine(cx - 6f * s, cy - 8f * s, cx + 18f * s, cy - 4f * s, 3.0f * s);
        // Lower fissure incandescent core
        sr.setColor(1.0f, 0.75f, 0.20f, 1f);
        sr.rectLine(cx - 28f * s, cy - 6f * s, cx - 6f * s, cy - 8f * s, 1.6f * s);
        sr.rectLine(cx - 6f * s, cy - 8f * s, cx + 16f * s, cy - 4f * s, 1.5f * s);

        // Vertical branch fissure
        sr.setColor(1.0f, 0.50f, 0.15f, 0.90f * pulse);
        sr.rectLine(cx - 10f * s, cy + 12f * s, cx - 6f * s, cy - 8f * s, 1.8f * s);

        // Lateral armored scutes (Vảy giáp sừng sắc bén)
        sr.setColor(0.32f, 0.18f, 0.30f, 1f);
        for (int j = 0; j < 4; j++) {
            float jx = cx - 22f * s + j * 12f * s;
            sr.triangle(jx - 4f * s, cy + 2f * s, jx + 4f * s, cy + 2f * s, jx, cy + 7f * s);
        }

        // 6. Winged Demon Pectoral Fin with Sharp Bone Claws (Vây ngực vuốt quỷ)
        sr.setColor(0.24f, 0.08f, 0.14f, 0.95f);
        sr.triangle(cx + 16f * s, cy - 8f * s, cx - 10f * s, cy - 30f * s, cx + 12f * s, cy - 32f * s);
        sr.setColor(0.65f, 0.15f, 0.12f, 0.85f);
        sr.triangle(cx + 16f * s, cy - 8f * s, cx - 6f * s, cy - 26f * s, cx + 8f * s, cy - 28f * s);

        // 3 Sharp bone talons on pectoral fin
        sr.setColor(0.90f, 0.85f, 0.72f, 1f);
        sr.rectLine(cx + 16f * s, cy - 8f * s, cx - 8f * s, cy - 32f * s, 2.4f * s);
        sr.rectLine(cx + 16f * s, cy - 8f * s, cx + 4f * s, cy - 34f * s, 2.2f * s);
        sr.rectLine(cx + 16f * s, cy - 8f * s, cx + 14f * s, cy - 32f * s, 2.0f * s);

        // 7. Terrifying Crocodilian Skull & Jaws (Đầu bạo chúa tiền sử & hàm răng nanh tử thần)
        // Skull cranium
        sr.setColor(0.18f, 0.12f, 0.20f, 1f);
        sr.circle(cx + 38f * s, cy + 4f * s, 22f * s);
        sr.setColor(0.16f, 0.10f, 0.18f, 1f);
        sr.triangle(cx + 26f * s, cy + 24f * s, cx + 26f * s, cy - 18f * s, cx + 66f * s, cy + 3f * s);

        // Backward-Curved Demonic Horns on the Crown (Sừng quỷ bạo chúa vuốt ngược về sau)
        sr.setColor(0.88f, 0.82f, 0.68f, 1f);
        sr.triangle(cx + 28f * s, cy + 22f * s, cx + 36f * s, cy + 20f * s, cx + 16f * s, cy + 40f * s);
        sr.setColor(1.0f, 0.35f, 0.12f, 1f);
        sr.rectLine(cx + 32f * s, cy + 21f * s, cx + 20f * s, cy + 36f * s, 1.8f * s);
        // Cheek armor horn
        sr.setColor(0.88f, 0.82f, 0.68f, 1f);
        sr.triangle(cx + 26f * s, cy - 8f * s, cx + 32f * s, cy - 12f * s, cx + 18f * s, cy - 24f * s);

        // Gaping Predatory Maw (Hàm há to khoe vòm họng đỏ rực tử thần)
        sr.setColor(0.50f, 0.05f, 0.08f, 1f);
        sr.triangle(cx + 40f * s, cy + 5f * s, cx + 40f * s, cy - 5f * s, cx + 64f * s, cy);
        sr.setColor(0.95f, 0.25f, 0.08f, 0.85f * fastPulse);
        sr.circle(cx + 44f * s, cy, 5.5f * s);

        // Interlocking Razor-Sharp Prehistoric Fangs (6 Răng nanh khổng lồ sắc lẹm)
        sr.setColor(0.98f, 0.98f, 0.90f, 1f);
        // Upper jaw fangs (pointing down)
        sr.triangle(cx + 46f * s, cy + 7f * s, cx + 52f * s, cy + 7f * s, cx + 49f * s, cy - 4f * s);
        sr.triangle(cx + 56f * s, cy + 5f * s, cx + 60f * s, cy + 5f * s, cx + 58f * s, cy - 2f * s);
        sr.triangle(cx + 40f * s, cy + 6f * s, cx + 44f * s, cy + 6f * s, cx + 42f * s, cy - 1f * s);
        // Lower jaw fangs (pointing up)
        sr.triangle(cx + 43f * s, cy - 7f * s, cx + 48f * s, cy - 7f * s, cx + 46f * s, cy + 3f * s);
        sr.triangle(cx + 52f * s, cy - 6f * s, cx + 56f * s, cy - 6f * s, cx + 54f * s, cy + 2f * s);
        sr.triangle(cx + 60f * s, cy - 4f * s, cx + 64f * s, cy - 4f * s, cx + 62f * s, cy + 1f * s);
        // Bloodstained fang tips
        sr.setColor(0.85f, 0.15f, 0.15f, 0.85f);
        sr.circle(cx + 49f * s, cy - 4f * s, 1.5f * s);
        sr.circle(cx + 46f * s, cy + 3f * s, 1.5f * s);
        sr.circle(cx + 58f * s, cy - 2f * s, 1.2f * s);

        // 8. Burning Demonic Eyes (Đôi Mắt Huyết Ma Bạo Chúa rực sáng)
        sr.setColor(0.06f, 0.02f, 0.06f, 1f); // Sunken eye socket
        sr.circle(cx + 40f * s, cy + 11f * s, 8.0f * s);
        sr.setColor(1.0f, 0.10f, 0.10f, 0.95f * pulse); // Outer fiery red halo
        sr.circle(cx + 40f * s, cy + 11f * s, 6.5f * s);
        sr.setColor(1.0f, 0.85f, 0.15f, 1f); // Inner molten gold iris
        sr.circle(cx + 40f * s, cy + 11f * s, 4.2f * s);
        sr.setColor(0.05f, 0.0f, 0.0f, 1f); // Demonic vertical slit pupil
        sr.rectLine(cx + 40f * s, cy + 7f * s, cx + 40f * s, cy + 15f * s, 2.2f * s);
        sr.setColor(1.0f, 1.0f, 0.85f, 1f); // Fiery diamond specular glint
        sr.circle(cx + 41.5f * s, cy + 12.5f * s, 1.5f * s);

        // Secondary temporal ocular gem (Mắt phụ phát sáng trên thái dương)
        sr.setColor(1.0f, 0.20f, 0.20f, 0.90f * pulse);
        sr.circle(cx + 31f * s, cy + 17f * s, 3.2f * s);
        sr.setColor(1.0f, 0.85f, 0.40f, 1f);
        sr.circle(cx + 31f * s, cy + 17f * s, 1.4f * s);

        // 9. Sinuous Demonic Barbels with Molten Tips (Râu rồng hắc ám phát sáng)
        sr.setColor(0.24f, 0.10f, 0.18f, 1f);
        sr.rectLine(cx + 52f * s, cy + 2f * s, cx + 70f * s, cy + 14f * s, 3.2f * s);
        sr.rectLine(cx + 70f * s, cy + 14f * s, cx + 90f * s, cy + 24f * s + wag * 4.5f * s, 2.2f * s);
        // Molten glowing tip on primary whisker
        sr.setColor(1.0f, 0.45f, 0.10f, 0.95f);
        sr.circle(cx + 90f * s, cy + 24f * s + wag * 4.5f * s, 2.5f * s);

        // Lower jaw chin barbel
        sr.setColor(0.24f, 0.10f, 0.18f, 1f);
        sr.rectLine(cx + 50f * s, cy - 6f * s, cx + 74f * s, cy - 20f * s - wag * 3f * s, 2.2f * s);
    }

    // =========================================================================
    // 16. CÁ CHỈ VÀNG (Yellowstripe Scad: Streamlined Silver Body, Broad Gold Stripe, Forked Yellow Tail)
    // =========================================================================
    public static void drawCaChiVang(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        // Fixed caudal peduncle attachment on body:
        float baseX = cx - 44f * s;
        float baseY = cy + 1f * s;
        // Gentle natural wag on outer fin lobes only:
        float finWag = wag * 3.0f * s;
        float tipX = cx - 74f * s + wag * 1.4f * s;
        float notchX = cx - 58f * s + wag * 0.7f * s;

        // 1. Caudal Fin (Forked yellow caudal fin: fixed base on body, waving fin lobes)
        sr.setColor(0.95f, 0.82f, 0.18f, 0.95f);
        sr.triangle(baseX, baseY, tipX, cy + 26f * s + finWag, notchX, cy + 3f * s + finWag * 0.5f);
        sr.triangle(baseX, baseY, tipX, cy - 26f * s + finWag, notchX, cy - 3f * s + finWag * 0.5f);
        sr.setColor(1f, 0.92f, 0.35f, 1f);
        sr.triangle(baseX - 2f * s, baseY, tipX + 4f * s, cy + 22f * s + finWag, notchX - 2f * s, cy + 3f * s + finWag * 0.5f);
        sr.triangle(baseX - 2f * s, baseY, tipX + 4f * s, cy - 22f * s + finWag, notchX - 2f * s, cy - 3f * s + finWag * 0.5f);
        sr.setColor(0.85f, 0.68f, 0.10f, 0.8f);
        sr.rectLine(baseX, baseY, tipX, cy + 24f * s + finWag, 1.2f * s);
        sr.rectLine(baseX, baseY, tipX, cy - 24f * s + finWag, 1.2f * s);

        // 2. Second Dorsal & Anal Fins (Soft translucent yellow fringe fins)
        sr.setColor(0.92f, 0.85f, 0.25f, 0.70f);
        sr.triangle(cx - 24f * s, cy + 18f * s, cx + 12f * s, cy + 22f * s, cx - 10f * s, cy + 34f * s);
        sr.triangle(cx - 22f * s, cy - 18f * s, cx + 10f * s, cy - 20f * s, cx - 8f * s, cy - 30f * s);

        // 3. Spiny First Dorsal Fin (Gai vây lưng trước mỏng manh)
        sr.setColor(0.35f, 0.55f, 0.65f, 0.85f);
        sr.triangle(cx + 8f * s, cy + 22f * s, cx + 24f * s, cy + 20f * s, cx + 16f * s, cy + 36f * s);
        sr.setColor(0.70f, 0.85f, 0.95f, 0.9f);
        sr.rectLine(cx + 12f * s, cy + 22f * s, cx + 16f * s, cy + 36f * s, 1.5f * s);

        // 4. Main Fusiform Body (Thân thoi bạc ánh xanh biển)
        sr.setColor(0.18f, 0.42f, 0.52f, 1f);
        sr.ellipse(cx - 48f * s, cy - 20f * s, 98f * s, 42f * s);
        sr.setColor(0.82f, 0.88f, 0.94f, 1f);
        sr.ellipse(cx - 46f * s, cy - 18f * s, 94f * s, 36f * s);
        sr.setColor(0.95f, 0.97f, 1f, 1f);
        sr.ellipse(cx - 42f * s, cy - 18f * s, 86f * s, 26f * s);

        // 5. SIGNATURE BROAD GOLDEN STRIPE (Dải sọc vàng kim óng ả đặc trưng - giữ yên trên thân)
        sr.setColor(1f, 0.86f, 0.16f, 1f);
        sr.rectLine(cx + 34f * s, cy + 3f * s, cx - 44f * s, cy + 1f * s, 5.5f * s);
        sr.setColor(1f, 0.96f, 0.55f, 0.95f);
        sr.rectLine(cx + 34f * s, cy + 3.5f * s, cx - 44f * s, cy + 1.5f * s, 2.2f * s);

        // Lateral scutes near tail (Gai vảy cứng đường bên ở đuôi)
        sr.setColor(0.88f, 0.72f, 0.15f, 0.85f);
        for (int i = 0; i < 5; i++) {
            float sx = cx - 22f * s - i * 6f * s;
            sr.rectLine(sx, cy - 1f * s, sx + 3f * s, cy + 5f * s, 1.5f * s);
        }

        // 6. Distinct Black Opercular Gill Spot (Đốm đen nắp mang đặc trưng)
        sr.setColor(0.12f, 0.14f, 0.18f, 0.95f);
        sr.circle(cx + 22f * s, cy + 7f * s, 3.8f * s);
        sr.setColor(0.35f, 0.40f, 0.48f, 0.7f);
        sr.circle(cx + 22f * s, cy + 7f * s, 2.2f * s);

        // Gill crescent line
        sr.setColor(0.45f, 0.55f, 0.65f, 0.8f);
        sr.rectLine(cx + 18f * s, cy + 14f * s, cx + 22f * s, cy - 8f * s, 1.8f * s);

        // 7. Pectoral Fin (Vây ngực hình lưỡi liềm vàng chanh)
        sr.setColor(0.95f, 0.82f, 0.22f, 0.9f);
        sr.triangle(cx + 14f * s, cy - 2f * s, cx - 12f * s, cy - 14f * s, cx + 4f * s, cy - 6f * s);
        sr.setColor(1f, 0.92f, 0.45f, 0.8f);
        sr.rectLine(cx + 14f * s, cy - 2f * s, cx - 10f * s, cy - 13f * s, 1.4f * s);

        // 8. Head, Snout, Mouth & Big Shiny Eye
        sr.setColor(0.25f, 0.48f, 0.58f, 1f);
        sr.triangle(cx + 36f * s, cy + 12f * s, cx + 54f * s, cy, cx + 36f * s, cy - 12f * s);
        sr.setColor(0.85f, 0.90f, 0.95f, 1f);
        sr.triangle(cx + 38f * s, cy + 8f * s, cx + 55f * s, cy + 1f * s, cx + 38f * s, cy - 8f * s);
        sr.setColor(0.18f, 0.22f, 0.28f, 1f);
        sr.rectLine(cx + 55f * s, cy + 1f * s, cx + 44f * s, cy - 2f * s, 1.6f * s);

        // Large bright eye (Mắt to tròn viền vàng kim)
        sr.setColor(0.95f, 0.82f, 0.18f, 1f);
        sr.circle(cx + 36f * s, cy + 5f * s, 6.2f * s);
        sr.setColor(0.08f, 0.10f, 0.14f, 1f);
        sr.circle(cx + 36f * s, cy + 5f * s, 4.2f * s);
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(cx + 37.5f * s, cy + 6.5f * s, 1.8f * s);
    }

    // =========================================================================
    // 17. CÁ ĐỤC (Sand Whiting: Cylindrical Sandy-Beige Torpedo, Long Conical Snout, Sandy Camo)
    // =========================================================================
    public static void drawCaDuc(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float baseX = cx - 48f * s;
        float baseY = cy + 1f * s;
        float finWag = wag * 2.8f * s;
        float tipX = cx - 74f * s + wag * 1.2f * s;
        float notchX = cx - 62f * s + wag * 0.6f * s;

        // 1. Caudal Fin (Sandy-tan notched tail: fixed base on body, waving fin tips)
        sr.setColor(0.82f, 0.70f, 0.45f, 0.95f);
        sr.triangle(baseX, baseY, tipX, cy + 22f * s + finWag, notchX, cy + finWag * 0.5f);
        sr.triangle(baseX, baseY, tipX, cy - 22f * s + finWag, notchX, cy - finWag * 0.5f);
        sr.setColor(0.68f, 0.55f, 0.32f, 0.8f);
        sr.rectLine(baseX, baseY, tipX, cy + 18f * s + finWag, 1.2f * s);
        sr.rectLine(baseX, baseY, tipX, cy - 18f * s + finWag, 1.2f * s);

        // 2. Dual Dorsal Fins (Vây lưng kép: vây trước có gai nhọn, vây sau thuôn dài)
        sr.setColor(0.85f, 0.74f, 0.50f, 0.9f);
        sr.triangle(cx + 10f * s, cy + 14f * s, cx + 25f * s, cy + 13f * s, cx + 18f * s, cy + 32f * s);
        sr.setColor(0.62f, 0.48f, 0.28f, 0.9f);
        sr.rectLine(cx + 12f * s, cy + 14f * s, cx + 18f * s, cy + 32f * s, 1.5f * s);
        sr.rectLine(cx + 18f * s, cy + 13f * s, cx + 22f * s, cy + 26f * s, 1.2f * s);

        // Long soft 2nd dorsal fin
        sr.setColor(0.88f, 0.76f, 0.52f, 0.75f);
        sr.triangle(cx - 28f * s, cy + 10f * s, cx + 4f * s, cy + 14f * s, cx - 12f * s, cy + 24f * s);

        // Long soft anal fin
        sr.setColor(0.88f, 0.76f, 0.52f, 0.75f);
        sr.triangle(cx - 26f * s, cy - 10f * s, cx + 2f * s, cy - 12f * s, cx - 12f * s, cy - 22f * s);

        // 3. Elongated Cylindrical Body (Thân dài thuôn hình ngư lôi màu cát)
        sr.setColor(0.68f, 0.55f, 0.35f, 1f);
        sr.ellipse(cx - 52f * s, cy - 14f * s, 108f * s, 29f * s);
        sr.setColor(0.86f, 0.76f, 0.54f, 1f);
        sr.ellipse(cx - 50f * s, cy - 13f * s, 104f * s, 26f * s);
        sr.setColor(0.96f, 0.94f, 0.88f, 1f);
        sr.ellipse(cx - 46f * s, cy - 14f * s, 96f * s, 18f * s);

        // 4. Sandy Camouflage Speckles & Subtle Lateral Line (Đốm cát ngụy trang & đường bên - cố định trên thân)
        sr.setColor(0.55f, 0.42f, 0.24f, 0.65f);
        for (int i = 0; i < 7; i++) {
            float sx = cx - 35f * s + i * 11f * s;
            float sy = cy + ((i % 2 == 0) ? 4f : -2f) * s;
            sr.circle(sx, sy, 1.8f * s);
            sr.circle(sx + 3f * s, sy + 3f * s, 1.2f * s);
        }
        sr.setColor(0.95f, 0.88f, 0.65f, 0.8f);
        sr.rectLine(cx + 36f * s, cy + 1f * s, cx - 48f * s, cy + 1f * s, 1.8f * s);

        // 5. Distinct Long Conical Snout & Head (Mõm nhọn hình nón sục cát)
        sr.setColor(0.82f, 0.70f, 0.48f, 1f);
        sr.triangle(cx + 38f * s, cy + 10f * s, cx + 64f * s, cy - 1f * s, cx + 38f * s, cy - 10f * s);
        sr.setColor(0.94f, 0.90f, 0.80f, 1f);
        sr.triangle(cx + 38f * s, cy + 3f * s, cx + 64f * s, cy - 1f * s, cx + 38f * s, cy - 9f * s);
        sr.setColor(0.35f, 0.28f, 0.18f, 1f);
        sr.rectLine(cx + 64f * s, cy - 1f * s, cx + 48f * s, cy - 4f * s, 1.5f * s);

        // Gill crescent
        sr.setColor(0.65f, 0.52f, 0.35f, 0.8f);
        sr.rectLine(cx + 28f * s, cy + 8f * s, cx + 32f * s, cy - 6f * s, 1.5f * s);

        // 6. Pectoral Fin (Vây ngực màu cát trong suốt)
        sr.setColor(0.88f, 0.78f, 0.52f, 0.85f);
        sr.triangle(cx + 22f * s, cy - 1f * s, cx + 2f * s, cy - 10f * s, cx + 14f * s, cy - 5f * s);

        // 7. Small Amber Eye (Mắt màu hổ phách sáng)
        sr.setColor(0.88f, 0.72f, 0.30f, 1f);
        sr.circle(cx + 44f * s, cy + 4f * s, 4.5f * s);
        sr.setColor(0.12f, 0.10f, 0.08f, 1f);
        sr.circle(cx + 44f * s, cy + 4f * s, 2.8f * s);
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(cx + 45.2f * s, cy + 5.2f * s, 1.2f * s);
    }

    // =========================================================================
    // 18. CÁ CƠM (Translucent Glass Anchovy: Delicate Crystal Body, Iridescent Silver Stripe, Schooling Motes)
    // =========================================================================
    public static void drawCaCom(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float baseX = cx - 38f * s;
        float baseY = cy;
        float finWag = wag * 2.8f * s;
        float tipX = cx - 62f * s + wag * 1.2f * s;
        float notchX = cx - 50f * s + wag * 0.6f * s;

        // 0. Schooling Miniature Anchovy Companions (Đàn cá cơm con bơi theo)
        sr.setColor(0.75f, 0.90f, 1f, 0.45f);
        float[] shoalsX = { cx - 35f * s, cx + 18f * s, cx - 15f * s };
        float[] shoalsY = { cy + 24f * s, cy + 26f * s, cy - 24f * s };
        for (int i = 0; i < 3; i++) {
            float scX = shoalsX[i];
            float scY = shoalsY[i];
            sr.ellipse(scX - 10f * s, scY - 2.5f * s, 20f * s, 5f * s);
            sr.triangle(scX - 10f * s, scY, scX - 16f * s, scY + 4f * s, scX - 16f * s, scY - 4f * s);
        }

        // 1. Caudal Fin (Clear translucent forked fin with fixed base on body)
        sr.setColor(0.80f, 0.92f, 1f, 0.85f);
        sr.triangle(baseX, baseY, tipX, cy + 18f * s + finWag, notchX, cy + finWag * 0.5f);
        sr.triangle(baseX, baseY, tipX, cy - 18f * s + finWag, notchX, cy - finWag * 0.5f);
        sr.setColor(1f, 1f, 1f, 0.9f);
        sr.rectLine(baseX, baseY, tipX, cy + 15f * s + finWag, 1.2f * s);
        sr.rectLine(baseX, baseY, tipX, cy - 15f * s + finWag, 1.2f * s);

        // 2. Dorsal & Anal Fin (Dainty transparent fins)
        sr.setColor(0.85f, 0.95f, 1f, 0.65f);
        sr.triangle(cx - 10f * s, cy + 8f * s, cx + 6f * s, cy + 8f * s, cx - 2f * s, cy + 18f * s);
        sr.triangle(cx - 18f * s, cy - 7f * s, cx - 2f * s, cy - 7f * s, cx - 10f * s, cy - 16f * s);

        // 3. Translucent Crystal Glass Body (Thân pha lê trong mờ ánh ngọc)
        sr.setColor(0.35f, 0.70f, 0.92f, 0.55f);
        sr.ellipse(cx - 42f * s, cy - 10f * s, 84f * s, 20f * s);
        sr.setColor(0.88f, 0.95f, 1f, 0.75f);
        sr.ellipse(cx - 40f * s, cy - 9f * s, 80f * s, 18f * s);
        sr.setColor(1f, 1f, 1f, 0.85f);
        sr.ellipse(cx - 36f * s, cy - 10f * s, 72f * s, 12f * s);

        // 4. Visible Internal Vertebrae & Silver Viscera (Đốt sống & túi nội tạng mờ ảo trong thân - giữ yên)
        sr.setColor(0.55f, 0.75f, 0.90f, 0.40f);
        sr.rectLine(cx + 18f * s, cy, cx - 38f * s, cy, 1.2f * s);
        for (int i = 0; i < 9; i++) {
            float vx = cx - 28f * s + i * 5.5f * s;
            sr.circle(vx, cy, 1.1f * s);
        }
        sr.setColor(0.78f, 0.86f, 0.95f, 0.70f);
        sr.ellipse(cx + 6f * s, cy - 6f * s, 14f * s, 8f * s);

        // 5. BRILLIANT IRIDESCENT SILVER-MIRROR STRIPE (Dải sọc gương bạc lấp lánh phản quang - cố định trên thân)
        sr.setColor(0.92f, 0.96f, 1f, 0.95f);
        sr.rectLine(cx + 26f * s, cy, cx - 38f * s, cy, 3.6f * s);
        sr.setColor(1f, 1f, 1f, 1f);
        sr.rectLine(cx + 26f * s, cy + 0.5f * s, cx - 38f * s, cy + 0.5f * s, 1.6f * s);

        // 6. Delicate Head, Translucent Snout & Deep Cleft Mouth (Đầu nhọn, miệng xẻ sâu)
        sr.setColor(0.78f, 0.90f, 1f, 0.85f);
        sr.triangle(cx + 28f * s, cy + 7f * s, cx + 46f * s, cy - 1f * s, cx + 28f * s, cy - 7f * s);
        sr.setColor(0.92f, 0.96f, 1f, 0.95f);
        sr.triangle(cx + 32f * s, cy + 3f * s, cx + 46f * s, cy - 1f * s, cx + 32f * s, cy - 5f * s);
        sr.setColor(0.25f, 0.40f, 0.55f, 0.9f);
        sr.rectLine(cx + 46f * s, cy - 1f * s, cx + 30f * s, cy - 6f * s, 1.4f * s);

        // 7. Pectoral Fin (Vây ngực mảnh như sợi tơ)
        sr.setColor(0.85f, 0.95f, 1f, 0.75f);
        sr.triangle(cx + 16f * s, cy - 1f * s, cx + 2f * s, cy - 8f * s, cx + 10f * s, cy - 4f * s);

        // 8. Innocent Shiny Black Eye with Silver Ring (Mắt to tròn ngây thơ viền bạc)
        sr.setColor(0.85f, 0.92f, 1f, 1f);
        sr.circle(cx + 32f * s, cy + 2.5f * s, 4.8f * s);
        sr.setColor(0.08f, 0.12f, 0.16f, 1f);
        sr.circle(cx + 32f * s, cy + 2.5f * s, 3.2f * s);
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(cx + 33.2f * s, cy + 3.6f * s, 1.4f * s);
    }

    // =========================================================================
    // 19. CÁ MÒI (Sardine / Pilchard: Steel-Blue Back, Flank Spots, Keeled Silver Belly)
    // =========================================================================
    public static void drawCaMoi(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float baseX = cx - 42f * s;
        float baseY = cy;
        float finWag = wag * 2.8f * s;
        float tipX = cx - 72f * s + wag * 1.4f * s;
        float notchX = cx - 56f * s + wag * 0.7f * s;

        // 1. Caudal Fin (Deeply notched steel-blue / dusky forked tail: fixed base on body)
        sr.setColor(0.25f, 0.42f, 0.58f, 0.95f);
        sr.triangle(baseX, baseY, tipX, cy + 24f * s + finWag, notchX, cy + 2f * s + finWag * 0.5f);
        sr.triangle(baseX, baseY, tipX, cy - 24f * s + finWag, notchX, cy - 2f * s + finWag * 0.5f);
        sr.setColor(0.65f, 0.80f, 0.92f, 0.85f);
        sr.triangle(baseX - 2f * s, baseY, tipX + 4f * s, cy + 20f * s + finWag, notchX - 2f * s, cy + 2f * s + finWag * 0.5f);
        sr.triangle(baseX - 2f * s, baseY, tipX + 4f * s, cy - 20f * s + finWag, notchX - 2f * s, cy - 2f * s + finWag * 0.5f);
        sr.setColor(0.18f, 0.32f, 0.45f, 0.9f);
        sr.rectLine(baseX, baseY, tipX, cy + 22f * s + finWag, 1.3f * s);
        sr.rectLine(baseX, baseY, tipX, cy - 22f * s + finWag, 1.3f * s);

        // 2. Dorsal Fin (Central triangular delicate fin)
        sr.setColor(0.32f, 0.50f, 0.65f, 0.85f);
        sr.triangle(cx - 10f * s, cy + 15f * s, cx + 14f * s, cy + 14f * s, cx + 2f * s, cy + 32f * s);
        sr.setColor(0.65f, 0.82f, 0.95f, 0.9f);
        sr.rectLine(cx - 8f * s, cy + 15f * s, cx + 2f * s, cy + 32f * s, 1.5f * s);
        sr.rectLine(cx - 1f * s, cy + 14f * s, cx + 6f * s, cy + 28f * s, 1.2f * s);
        sr.rectLine(cx + 6f * s, cy + 14f * s, cx + 11f * s, cy + 22f * s, 1.1f * s);

        // Pelvic and anal fins (Small, translucent silvery-white)
        sr.setColor(0.80f, 0.90f, 0.98f, 0.75f);
        sr.triangle(cx - 6f * s, cy - 14f * s, cx + 6f * s, cy - 13f * s, cx - 2f * s, cy - 24f * s);
        sr.triangle(cx - 30f * s, cy - 11f * s, cx - 16f * s, cy - 12f * s, cx - 24f * s, cy - 20f * s);

        // 3. Elongated Compressed Body (Lưng xanh thép thẫm, bụng trắng bạc)
        sr.setColor(0.12f, 0.28f, 0.44f, 1f); // Dark dorsal ridge
        sr.ellipse(cx - 48f * s, cy - 16f * s, 98f * s, 34f * s);
        sr.setColor(0.24f, 0.50f, 0.68f, 1f); // Metallic ocean blue
        sr.ellipse(cx - 46f * s, cy - 15f * s, 94f * s, 31f * s);
        sr.setColor(0.78f, 0.88f, 0.95f, 1f); // Silvery flank
        sr.ellipse(cx - 44f * s, cy - 15f * s, 90f * s, 25f * s);
        sr.setColor(0.96f, 0.98f, 1.0f, 1f); // Brilliant pearl belly
        sr.ellipse(cx - 40f * s, cy - 16f * s, 82f * s, 18f * s);

        // Sharp ventral keeled belly line
        sr.setColor(0.85f, 0.92f, 0.98f, 1f);
        sr.rectLine(cx - 35f * s, cy - 15f * s, cx + 25f * s, cy - 13f * s, 1.8f * s);

        // 4. SIGNATURE SARDINE SPOTS (Chuỗi đốm đen ngọc thạch đặc trưng dọc lưng cá mòi - cố định trên thân)
        for (int i = 0; i < 6; i++) {
            float spotX = cx + 14f * s - i * 8.5f * s;
            float spotY = cy + 5f * s;
            // Silver glow ring around spot
            sr.setColor(0.68f, 0.86f, 0.98f, 0.85f);
            sr.circle(spotX, spotY, 3.2f * s);
            // Crisp dark indigo/black core spot
            sr.setColor(0.10f, 0.16f, 0.24f, 1f);
            sr.circle(spotX, spotY, 2.0f * s);
        }

        // Faint gold-cyan lateral sheen above the spots
        sr.setColor(0.65f, 0.85f, 0.80f, 0.45f);
        sr.rectLine(cx + 25f * s, cy + 7f * s, cx - 36f * s, cy + 6f * s, 2.0f * s);

        // 5. Head & Operculum (Đầu thon, nắp mang ánh vàng chanh)
        sr.setColor(0.20f, 0.42f, 0.58f, 1f);
        sr.triangle(cx + 34f * s, cy + 12f * s, cx + 52f * s, cy, cx + 34f * s, cy - 10f * s);
        sr.setColor(0.88f, 0.92f, 0.96f, 1f);
        sr.triangle(cx + 36f * s, cy + 8f * s, cx + 52f * s, cy, cx + 36f * s, cy - 7f * s);
        // Golden glint on operculum cheek
        sr.setColor(0.95f, 0.85f, 0.35f, 0.70f);
        sr.circle(cx + 28f * s, cy - 1f * s, 7.5f * s);
        sr.setColor(0.85f, 0.92f, 0.98f, 0.95f);
        sr.circle(cx + 28f * s, cy - 1f * s, 6.0f * s);
        // Gill curve
        sr.setColor(0.35f, 0.52f, 0.65f, 0.85f);
        sr.rectLine(cx + 22f * s, cy + 10f * s, cx + 25f * s, cy - 8f * s, 1.6f * s);
        // Mouth
        sr.setColor(0.18f, 0.28f, 0.38f, 1f);
        sr.rectLine(cx + 52f * s, cy, cx + 42f * s, cy - 3f * s, 1.4f * s);

        // 6. Pectoral Fin (Vây ngực vát chéo)
        sr.setColor(0.75f, 0.88f, 0.96f, 0.85f);
        sr.triangle(cx + 18f * s, cy - 2f * s, cx + 2f * s, cy - 12f * s, cx + 10f * s, cy - 6f * s);
        sr.setColor(0.35f, 0.55f, 0.72f, 0.8f);
        sr.rectLine(cx + 18f * s, cy - 2f * s, cx + 3f * s, cy - 11f * s, 1.2f * s);

        // 7. Bright Eye (Mắt to sáng viền bạc)
        sr.setColor(0.92f, 0.96f, 1f, 1f);
        sr.circle(cx + 36f * s, cy + 3.5f * s, 5.2f * s);
        sr.setColor(0.08f, 0.12f, 0.18f, 1f);
        sr.circle(cx + 36f * s, cy + 3.5f * s, 3.4f * s);
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(cx + 37.2f * s, cy + 4.8f * s, 1.4f * s);
    }

    // =========================================================================
    // 20. CÁ LẸP (Thryssa / Anchovy-Scad: Ultra-Thin Knife Silhouette, Serrated Keel, Long Maxilla)
    // =========================================================================
    public static void drawCaLep(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float baseX = cx - 44f * s;
        float baseY = cy - 2f * s;
        float finWag = wag * 2.8f * s;
        float tipX = cx - 74f * s + wag * 1.4f * s;
        float notchX = cx - 56f * s + wag * 0.7f * s;

        // 1. Caudal Fin (Clear translucent glassy forked tail with pale lemon-cyan sheen: fixed base on body)
        sr.setColor(0.72f, 0.90f, 0.88f, 0.90f);
        sr.triangle(baseX, baseY, tipX, cy + 24f * s + finWag, notchX, cy + 1f * s + finWag * 0.5f);
        sr.triangle(baseX, baseY, tipX, cy - 24f * s + finWag, notchX, cy - 1f * s + finWag * 0.5f);
        sr.setColor(1f, 1f, 0.80f, 0.80f);
        sr.triangle(baseX - 2f * s, baseY, tipX + 4f * s, cy + 20f * s + finWag, notchX - 2f * s, cy + 1f * s + finWag * 0.5f);
        sr.triangle(baseX - 2f * s, baseY, tipX + 4f * s, cy - 20f * s + finWag, notchX - 2f * s, cy - 1f * s + finWag * 0.5f);
        sr.setColor(0.45f, 0.65f, 0.60f, 0.85f);
        sr.rectLine(baseX, baseY, tipX, cy + 22f * s + finWag, 1.3f * s);
        sr.rectLine(baseX, baseY, tipX, cy - 22f * s + finWag, 1.3f * s);

        // 2. SIGNATURE LONG ANAL FIN (Vây hậu môn thuôn dài dải quạt mỏng manh chạy dọc bụng sau)
        sr.setColor(0.85f, 0.95f, 0.90f, 0.75f);
        sr.triangle(cx - 40f * s, cy - 8f * s, cx - 2f * s, cy - 15f * s, cx - 26f * s, cy - 26f * s);
        sr.setColor(0.95f, 0.90f, 0.50f, 0.85f); // Amber-yellow edge fringe
        sr.rectLine(cx - 2f * s, cy - 15f * s, cx - 26f * s, cy - 26f * s, 1.5f * s);
        sr.rectLine(cx - 26f * s, cy - 26f * s, cx - 40f * s, cy - 8f * s, 1.2f * s);
        // Delicate fin rays along long anal fin
        sr.setColor(0.60f, 0.78f, 0.75f, 0.7f);
        for (int r = 0; r < 5; r++) {
            float rx = cx - 8f * s - r * 6f * s;
            sr.rectLine(rx, cy - 14f * s + r * 1.2f * s, rx - 3f * s, cy - 20f * s - r * 1f * s, 1.1f * s);
        }

        // Small dorsal fin (Vây lưng nhỏ lùi về sau)
        sr.setColor(0.65f, 0.85f, 0.80f, 0.8f);
        sr.triangle(cx - 16f * s, cy + 14f * s, cx + 4f * s, cy + 13f * s, cx - 6f * s, cy + 26f * s);
        sr.setColor(0.35f, 0.55f, 0.50f, 0.85f);
        sr.rectLine(cx - 14f * s, cy + 14f * s, cx - 6f * s, cy + 26f * s, 1.4f * s);

        // 3. ULTRA-THIN KNIFE BLADE BODY (Thân dẹp bên rất mỏng như lá tre, dải sống bụng sắc như lưỡi dao)
        sr.setColor(0.20f, 0.42f, 0.44f, 1f); // Dorsal teal-green contour
        sr.ellipse(cx - 46f * s, cy - 17f * s, 94f * s, 35f * s);
        sr.setColor(0.70f, 0.88f, 0.86f, 1f); // Pale seafoam iridescent body
        sr.ellipse(cx - 44f * s, cy - 16f * s, 90f * s, 32f * s);
        sr.setColor(0.92f, 0.97f, 0.96f, 1f); // Mirror silvery-white belly
        sr.ellipse(cx - 40f * s, cy - 16f * s, 82f * s, 26f * s);

        // 4. SERRATED KEEL BELLY SCUTES (Gai nhọn răng cưa sắc bén ở viền sống bụng)
        sr.setColor(1f, 1f, 1f, 1f); // Razor sharp white edge
        sr.rectLine(cx + 20f * s, cy - 14f * s, cx - 42f * s, cy - 6f * s, 2.2f * s);
        sr.setColor(0.55f, 0.70f, 0.65f, 0.9f);
        for (int k = 0; k < 8; k++) {
            float kx = cx + 16f * s - k * 7f * s;
            float ky = cy - 14f * s + k * 1.0f * s;
            sr.rectLine(kx, ky, kx - 2f * s, ky - 3.5f * s, 1.3f * s); // Serration teeth
        }

        // Subtle faint green-gold lateral line (Đường bên ánh xanh ngọc dịu - cố định trên thân)
        sr.setColor(0.60f, 0.85f, 0.75f, 0.6f);
        sr.rectLine(cx + 28f * s, cy + 1f * s, cx - 40f * s, cy + 2f * s, 1.8f * s);

        // 5. SIGNATURE EXTENDED SNOUT & LONG NEEDLE MAXILLA (Mõm nhọn, hàm trên kéo dài như chiếc gai nhọn)
        sr.setColor(0.65f, 0.82f, 0.80f, 1f);
        sr.triangle(cx + 30f * s, cy + 10f * s, cx + 50f * s, cy + 2f * s, cx + 30f * s, cy - 12f * s);
        sr.setColor(0.90f, 0.96f, 0.95f, 1f);
        sr.triangle(cx + 32f * s, cy + 6f * s, cx + 49f * s, cy + 2f * s, cx + 32f * s, cy - 8f * s);
        // Characteristic needle-like elongated maxilla running backward along lower head
        sr.setColor(0.25f, 0.38f, 0.35f, 1f);
        sr.rectLine(cx + 49f * s, cy + 1f * s, cx + 18f * s, cy - 8f * s, 1.8f * s);
        // Golden glint on sharp jaw tip
        sr.setColor(1f, 0.90f, 0.40f, 0.9f);
        sr.rectLine(cx + 49f * s, cy + 1f * s, cx + 44f * s, cy - 1f * s, 1.4f * s);

        // Gill crescent
        sr.setColor(0.40f, 0.60f, 0.58f, 0.8f);
        sr.rectLine(cx + 20f * s, cy + 8f * s, cx + 24f * s, cy - 6f * s, 1.5f * s);

        // 6. Delicate Pectoral Fin (Vây ngực mảnh)
        sr.setColor(0.80f, 0.92f, 0.88f, 0.85f);
        sr.triangle(cx + 16f * s, cy - 4f * s, cx + 2f * s, cy - 12f * s, cx + 10f * s, cy - 7f * s);

        // 7. Large Silver-Lemon Eye (Mắt to tròn ánh vàng chanh)
        sr.setColor(0.95f, 0.92f, 0.40f, 1f);
        sr.circle(cx + 34f * s, cy + 4f * s, 5.0f * s);
        sr.setColor(0.08f, 0.12f, 0.14f, 1f);
        sr.circle(cx + 34f * s, cy + 4f * s, 3.2f * s);
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(cx + 35.2f * s, cy + 5.2f * s, 1.3f * s);
    }

    // =========================================================================
    // 21. CÁ BẠC MÁ (Indian Mackerel: Fusiform Body, Detached Finlets, Shining Mirror Cheeks)
    // =========================================================================
    public static void drawCaBacMa(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float baseX = cx - 46f * s;
        float baseY = cy;
        float finWag = wag * 3.0f * s;
        float tipX = cx - 76f * s + wag * 1.5f * s;
        float notchX = cx - 60f * s + wag * 0.8f * s;

        // 1. Caudal Fin (Broad sickle tail with yellow-tipped lobes: fixed base on body)
        sr.setColor(0.18f, 0.38f, 0.35f, 0.95f);
        sr.triangle(baseX, baseY, tipX, cy + 28f * s + finWag, notchX, cy + 2f * s + finWag * 0.5f);
        sr.triangle(baseX, baseY, tipX, cy - 28f * s + finWag, notchX, cy - 2f * s + finWag * 0.5f);
        sr.setColor(0.95f, 0.85f, 0.25f, 0.90f); // Bright yellow fin tips
        sr.triangle(tipX + 10f * s, cy + 16f * s + finWag, tipX, cy + 28f * s + finWag, tipX + 4f * s, cy + 18f * s + finWag);
        sr.triangle(tipX + 10f * s, cy - 16f * s + finWag, tipX, cy - 28f * s + finWag, tipX + 4f * s, cy - 18f * s + finWag);
        sr.setColor(0.12f, 0.26f, 0.24f, 0.9f);
        sr.rectLine(baseX, baseY, tipX, cy + 26f * s + finWag, 1.4f * s);
        sr.rectLine(baseX, baseY, tipX, cy - 26f * s + finWag, 1.4f * s);

        // Slender caudal peduncle with small keels (Cuống đuôi mảnh)
        sr.setColor(0.25f, 0.45f, 0.42f, 1f);
        sr.rectLine(cx - 36f * s, cy, baseX, baseY, 6.0f * s);

        // 2. SIGNATURE DETACHED FINLETS (5 vây phụ nhỏ hình tam giác ở cuống đuôi - cố định trên thân)
        for (int f = 0; f < 5; f++) {
            float finX = cx - 28f * s - f * 3.6f * s;
            // Dorsal finlet
            sr.setColor(0.95f, 0.85f, 0.25f, 0.95f);
            sr.triangle(finX, cy + 5f * s, finX - 3.5f * s, cy + 11f * s, finX - 4.5f * s, cy + 5f * s);
            // Ventral finlet
            sr.triangle(finX, cy - 5f * s, finX - 3.5f * s, cy - 11f * s, finX - 4.5f * s, cy - 5f * s);
            // Finlet dark spine
            sr.setColor(0.15f, 0.30f, 0.28f, 1f);
            sr.rectLine(finX, cy + 5f * s, finX - 3.5f * s, cy + 11f * s, 1.1f * s);
            sr.rectLine(finX, cy - 5f * s, finX - 3.5f * s, cy - 11f * s, 1.1f * s);
        }

        // 3. Dual Dorsal Fins & Anal Fin (Vây lưng thứ nhất nhiều gai, vây thứ 2 nhỏ)
        // 1st spiny dorsal fin
        sr.setColor(0.20f, 0.45f, 0.42f, 0.85f);
        sr.triangle(cx + 2f * s, cy + 18f * s, cx + 22f * s, cy + 16f * s, cx + 10f * s, cy + 35f * s);
        sr.setColor(0.90f, 0.82f, 0.30f, 0.9f);
        sr.rectLine(cx + 4f * s, cy + 18f * s, cx + 10f * s, cy + 35f * s, 1.6f * s);
        sr.rectLine(cx + 10f * s, cy + 17f * s, cx + 15f * s, cy + 28f * s, 1.3f * s);
        sr.rectLine(cx + 16f * s, cy + 16f * s, cx + 19f * s, cy + 23f * s, 1.1f * s);

        // 2nd small dorsal & anal fin
        sr.setColor(0.85f, 0.78f, 0.25f, 0.85f);
        sr.triangle(cx - 24f * s, cy + 14f * s, cx - 12f * s, cy + 16f * s, cx - 18f * s, cy + 25f * s);
        sr.triangle(cx - 22f * s, cy - 14f * s, cx - 10f * s, cy - 16f * s, cx - 16f * s, cy - 25f * s);

        // 4. ROBUST FUSIFORM TORPEDO BODY (Thân hình thoi tròn lẳn, lực lưỡng chuyên bơi nhanh)
        sr.setColor(0.10f, 0.35f, 0.32f, 1f); // Deep emerald-teal back
        sr.ellipse(cx - 48f * s, cy - 18f * s, 100f * s, 38f * s);
        sr.setColor(0.20f, 0.52f, 0.48f, 1f); // Metallic sea-green midflank
        sr.ellipse(cx - 46f * s, cy - 17f * s, 96f * s, 34f * s);
        sr.setColor(0.82f, 0.90f, 0.94f, 1f); // Silvery flank
        sr.ellipse(cx - 44f * s, cy - 17f * s, 90f * s, 28f * s);
        sr.setColor(0.95f, 0.97f, 1.0f, 1f); // Pearlescent belly
        sr.ellipse(cx - 40f * s, cy - 17f * s, 82f * s, 20f * s);

        // 5. SIGNATURE WAVY VERMICULATED DORSAL PATTERNS (Vân rằn ri gợn sóng đặc trưng cá bạc má - cố định trên thân)
        sr.setColor(0.08f, 0.24f, 0.22f, 0.85f);
        for (int p = 0; p < 8; p++) {
            float px = cx + 18f * s - p * 7.5f * s;
            sr.rectLine(px, cy + 16f * s, px - 2.5f * s, cy + 7f * s, 1.8f * s);
            sr.rectLine(px - 2.5f * s, cy + 7f * s, px + 1.0f * s, cy + 3f * s, 1.5f * s);
        }

        // Luminous Golden Lateral Sheen Stripe (Dải sáng vàng ánh kim phản quang đường bên - cố định trên thân)
        sr.setColor(0.98f, 0.88f, 0.30f, 0.85f);
        sr.rectLine(cx + 30f * s, cy + 1f * s, cx - 44f * s, cy + 1f * s, 3.2f * s);
        sr.setColor(1.0f, 0.98f, 0.70f, 0.90f);
        sr.rectLine(cx + 30f * s, cy + 1.5f * s, cx - 44f * s, cy + 1.5f * s, 1.5f * s);

        // 6. SIGNATURE "BẠC MÁ" SHINING MIRROR-POLISHED CHEEKS (Đôi má nắp mang ánh gương bạc lấp lánh)
        sr.setColor(0.20f, 0.45f, 0.42f, 1f);
        sr.triangle(cx + 34f * s, cy + 12f * s, cx + 56f * s, cy + 1f * s, cx + 34f * s, cy - 10f * s);
        sr.setColor(0.88f, 0.94f, 0.98f, 1f);
        sr.triangle(cx + 36f * s, cy + 8f * s, cx + 55f * s, cy + 1f * s, cx + 36f * s, cy - 8f * s);

        // THE GLISTENING SILVER-PLATINUM CHEEK PLATE (Má Bạc Má)
        sr.setColor(0.85f, 0.75f, 0.25f, 0.9f); // Gold rim
        sr.ellipse(cx + 22f * s, cy - 8f * s, 16f * s, 18f * s);
        sr.setColor(0.96f, 0.98f, 1.0f, 1.0f); // Mirror platinum center
        sr.ellipse(cx + 23f * s, cy - 7f * s, 14f * s, 16f * s);
        sr.setColor(1f, 1f, 1f, 0.95f); // Crisp specular glint
        sr.ellipse(cx + 25f * s, cy - 5f * s, 8f * s, 10f * s);

        // Gill crescent line
        sr.setColor(0.25f, 0.42f, 0.40f, 0.9f);
        sr.rectLine(cx + 20f * s, cy + 12f * s, cx + 24f * s, cy - 9f * s, 1.8f * s);
        // Wide streamlined mouth
        sr.setColor(0.12f, 0.22f, 0.20f, 1f);
        sr.rectLine(cx + 56f * s, cy + 1f * s, cx + 42f * s, cy - 2f * s, 1.6f * s);

        // 7. Pectoral Fin (Vây ngực vát nhọn màu vàng viền đen)
        sr.setColor(0.95f, 0.82f, 0.25f, 0.9f);
        sr.triangle(cx + 18f * s, cy - 1f * s, cx + 2f * s, cy - 11f * s, cx + 10f * s, cy - 5f * s);
        sr.setColor(0.18f, 0.32f, 0.30f, 0.9f);
        sr.rectLine(cx + 18f * s, cy - 1f * s, cx + 3f * s, cy - 10f * s, 1.3f * s);

        // 8. Large Dark Eye with Adipose Eyelid (Mắt to tròn đặc trưng với mí mỡ trong mờ)
        // Adipose eyelid glassy border
        sr.setColor(0.70f, 0.88f, 0.95f, 0.65f);
        sr.ellipse(cx + 33f * s, cy - 1f * s, 14f * s, 12f * s);
        // Eye socket and golden iris
        sr.setColor(0.95f, 0.82f, 0.20f, 1f);
        sr.circle(cx + 39f * s, cy + 4f * s, 5.8f * s);
        sr.setColor(0.08f, 0.10f, 0.12f, 1f);
        sr.circle(cx + 39f * s, cy + 4f * s, 3.8f * s);
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(cx + 40.5f * s, cy + 5.5f * s, 1.5f * s);
    }

    // =========================================================================
    // 22. CÁ NỤC (Round Scad: Robust Cylindrical Torpedo, Lateral Scute Armor, Detached Finlet)
    // =========================================================================
    public static void drawCaNuc(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float baseX = cx - 46f * s;
        float baseY = cy;
        float finWag = wag * 3.0f * s;
        float tipX = cx - 76f * s + wag * 1.4f * s;
        float notchX = cx - 60f * s + wag * 0.7f * s;

        // 1. Caudal Fin (Forked olive-yellow fin with fixed base on body)
        sr.setColor(0.35f, 0.52f, 0.40f, 0.95f);
        sr.triangle(baseX, baseY, tipX, cy + 26f * s + finWag, notchX, cy + 2f * s + finWag * 0.5f);
        sr.triangle(baseX, baseY, tipX, cy - 26f * s + finWag, notchX, cy - 2f * s + finWag * 0.5f);
        sr.setColor(0.85f, 0.82f, 0.28f, 0.90f);
        sr.triangle(baseX - 2f * s, baseY, tipX + 4f * s, cy + 22f * s + finWag, notchX - 2f * s, cy + 2f * s + finWag * 0.5f);
        sr.triangle(baseX - 2f * s, baseY, tipX + 4f * s, cy - 22f * s + finWag, notchX - 2f * s, cy - 2f * s + finWag * 0.5f);
        sr.setColor(0.25f, 0.38f, 0.28f, 0.9f);
        sr.rectLine(baseX, baseY, tipX, cy + 25f * s + finWag, 1.3f * s);
        sr.rectLine(baseX, baseY, tipX, cy - 25f * s + finWag, 1.3f * s);

        // 2. Detached Terminal Finlets (Vây phụ rời ở cuống đuôi đặc trưng cá nục)
        sr.setColor(0.88f, 0.78f, 0.25f, 0.95f);
        sr.triangle(baseX + 2f * s, cy + 7f * s, baseX - 4f * s, cy + 14f * s, baseX - 6f * s, cy + 7f * s);
        sr.triangle(baseX + 2f * s, cy - 7f * s, baseX - 4f * s, cy - 14f * s, baseX - 6f * s, cy - 7f * s);

        // 3. Second Dorsal & Anal Fin
        sr.setColor(0.40f, 0.60f, 0.50f, 0.85f);
        sr.triangle(cx - 26f * s, cy + 14f * s, cx + 4f * s, cy + 15f * s, cx - 14f * s, cy + 28f * s);
        sr.triangle(cx - 24f * s, cy - 14f * s, cx + 2f * s, cy - 15f * s, cx - 12f * s, cy - 26f * s);

        // 1st spiny dorsal fin
        sr.setColor(0.28f, 0.45f, 0.38f, 0.9f);
        sr.triangle(cx + 8f * s, cy + 15f * s, cx + 24f * s, cy + 14f * s, cx + 16f * s, cy + 32f * s);
        sr.setColor(0.85f, 0.88f, 0.60f, 0.9f);
        sr.rectLine(cx + 10f * s, cy + 15f * s, cx + 16f * s, cy + 32f * s, 1.5f * s);

        // 4. Cylindrical Muscular Torpedo Body (Thân tròn lẳn hình trụ cơ bắp)
        sr.setColor(0.14f, 0.32f, 0.36f, 1f); // Metallic blue-green back
        sr.ellipse(cx - 48f * s, cy - 17f * s, 98f * s, 36f * s);
        sr.setColor(0.26f, 0.54f, 0.56f, 1f);
        sr.ellipse(cx - 46f * s, cy - 16f * s, 94f * s, 32f * s);
        sr.setColor(0.82f, 0.90f, 0.92f, 1f); // Silvery midflank
        sr.ellipse(cx - 44f * s, cy - 16f * s, 88f * s, 26f * s);
        sr.setColor(0.96f, 0.98f, 1.0f, 1f); // Pearlescent belly
        sr.ellipse(cx - 40f * s, cy - 16f * s, 80f * s, 18f * s);

        // 5. SIGNATURE HEAVY LATERAL SCUTES (Hàng vảy gai cứng đường bên gồ ghề của cá nục)
        sr.setColor(0.78f, 0.72f, 0.22f, 0.9f);
        sr.rectLine(cx + 28f * s, cy + 2f * s, cx - 10f * s, cy + 1f * s, 2.2f * s);
        // Heavy armored scutes on rear body
        for (int i = 0; i < 7; i++) {
            float scX = cx - 12f * s - i * 5.0f * s;
            sr.setColor(0.88f, 0.78f, 0.20f, 1f);
            sr.triangle(scX, cy - 1f * s, scX - 4f * s, cy + 4f * s, scX - 4f * s, cy - 4f * s);
            sr.setColor(0.40f, 0.35f, 0.10f, 1f);
            sr.rectLine(scX, cy, scX - 4f * s, cy, 1.4f * s);
        }

        // 6. Distinct Black Opercular Spot (Đốm đen nắp mang tròn rõ)
        sr.setColor(0.10f, 0.12f, 0.16f, 1f);
        sr.circle(cx + 22f * s, cy + 7f * s, 4.0f * s);
        sr.setColor(0.35f, 0.45f, 0.52f, 0.7f);
        sr.circle(cx + 22f * s, cy + 7f * s, 2.2f * s);

        // Head, snout & mouth
        sr.setColor(0.20f, 0.42f, 0.46f, 1f);
        sr.triangle(cx + 34f * s, cy + 12f * s, cx + 54f * s, cy + 1f * s, cx + 34f * s, cy - 10f * s);
        sr.setColor(0.88f, 0.92f, 0.95f, 1f);
        sr.triangle(cx + 36f * s, cy + 8f * s, cx + 54f * s, cy + 1f * s, cx + 36f * s, cy - 7f * s);
        sr.setColor(0.18f, 0.28f, 0.30f, 1f);
        sr.rectLine(cx + 54f * s, cy + 1f * s, cx + 42f * s, cy - 2f * s, 1.5f * s);

        // 7. Pectoral Fin
        sr.setColor(0.85f, 0.80f, 0.30f, 0.85f);
        sr.triangle(cx + 18f * s, cy - 2f * s, cx + 2f * s, cy - 12f * s, cx + 10f * s, cy - 6f * s);

        // 8. Large Dark Eye with Adipose Eyelid
        sr.setColor(0.92f, 0.96f, 1f, 1f);
        sr.circle(cx + 36f * s, cy + 4f * s, 5.5f * s);
        sr.setColor(0.08f, 0.12f, 0.16f, 1f);
        sr.circle(cx + 36f * s, cy + 4f * s, 3.6f * s);
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(cx + 37.2f * s, cy + 5.2f * s, 1.4f * s);
    }

    // =========================================================================
    // 23. CÁ ĐỐI (Mullet: Flat Broad Head, Blunt Snout, 6 Longitudinal Stripes, Dual Dorsals)
    // =========================================================================
    public static void drawCaDoi(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float baseX = cx - 46f * s;
        float baseY = cy;
        float finWag = wag * 2.8f * s;
        float tipX = cx - 74f * s + wag * 1.3f * s;
        float notchX = cx - 60f * s + wag * 0.6f * s;

        // 1. Caudal Fin (Sturdy, shallowly notched grey-silver fin with fixed base)
        sr.setColor(0.45f, 0.52f, 0.55f, 0.95f);
        sr.triangle(baseX, baseY, tipX, cy + 24f * s + finWag, notchX, cy + 1f * s + finWag * 0.5f);
        sr.triangle(baseX, baseY, tipX, cy - 24f * s + finWag, notchX, cy - 1f * s + finWag * 0.5f);
        sr.setColor(0.75f, 0.82f, 0.88f, 0.85f);
        sr.triangle(baseX - 2f * s, baseY, tipX + 4f * s, cy + 20f * s + finWag, notchX - 2f * s, cy + 1f * s + finWag * 0.5f);
        sr.triangle(baseX - 2f * s, baseY, tipX + 4f * s, cy - 20f * s + finWag, notchX - 2f * s, cy - 1f * s + finWag * 0.5f);
        sr.setColor(0.30f, 0.38f, 0.42f, 0.9f);
        sr.rectLine(baseX, baseY, tipX, cy + 22f * s + finWag, 1.3f * s);
        sr.rectLine(baseX, baseY, tipX, cy - 22f * s + finWag, 1.3f * s);

        // 2. TWO WIDELY SEPARATED DORSAL FINS (Hai vây lưng cách xa nhau đặc trưng cá đối)
        // 1st Spiny Dorsal (4 sharp spines, positioned forward)
        sr.setColor(0.55f, 0.62f, 0.68f, 0.9f);
        sr.triangle(cx + 4f * s, cy + 16f * s, cx + 20f * s, cy + 15f * s, cx + 12f * s, cy + 34f * s);
        sr.setColor(0.35f, 0.42f, 0.48f, 1f);
        sr.rectLine(cx + 6f * s, cy + 16f * s, cx + 12f * s, cy + 34f * s, 1.6f * s);
        sr.rectLine(cx + 12f * s, cy + 15f * s, cx + 16f * s, cy + 28f * s, 1.3f * s);

        // 2nd Soft Dorsal (Positioned well behind 1st dorsal)
        sr.setColor(0.65f, 0.72f, 0.78f, 0.85f);
        sr.triangle(cx - 28f * s, cy + 13f * s, cx - 12f * s, cy + 14f * s, cx - 20f * s, cy + 26f * s);

        // Anal fin
        sr.triangle(cx - 26f * s, cy - 13f * s, cx - 10f * s, cy - 14f * s, cx - 18f * s, cy - 26f * s);

        // 3. Robust Silvery Body (Thân tròn mập vảy bạc)
        sr.setColor(0.32f, 0.38f, 0.44f, 1f); // Steel olive-grey back
        sr.ellipse(cx - 48f * s, cy - 18f * s, 100f * s, 38f * s);
        sr.setColor(0.55f, 0.62f, 0.68f, 1f);
        sr.ellipse(cx - 46f * s, cy - 17f * s, 96f * s, 34f * s);
        sr.setColor(0.85f, 0.90f, 0.95f, 1f); // Silvery flank
        sr.ellipse(cx - 44f * s, cy - 17f * s, 90f * s, 28f * s);
        sr.setColor(0.96f, 0.98f, 1.0f, 1f); // Pure white belly
        sr.ellipse(cx - 40f * s, cy - 17f * s, 82f * s, 20f * s);

        // 4. SIGNATURE 6 LONGITUDINAL DARK STRIPES (6 sọc xám thẫm chạy dọc thân cá đối)
        sr.setColor(0.42f, 0.48f, 0.54f, 0.75f);
        for (int st = -2; st <= 3; st++) {
            float sy = cy + st * 4.2f * s;
            sr.rectLine(cx + 30f * s, sy, cx - 44f * s, sy, 1.3f * s);
        }

        // 5. SIGNATURE FLAT BROAD HEAD & BLUNT ROUND SNOUT (Đầu dẹp bằng, mõm tròn đặc trưng)
        sr.setColor(0.40f, 0.48f, 0.52f, 1f);
        sr.triangle(cx + 34f * s, cy + 13f * s, cx + 56f * s, cy + 2f * s, cx + 34f * s, cy - 10f * s);
        sr.setColor(0.82f, 0.88f, 0.94f, 1f);
        sr.triangle(cx + 36f * s, cy + 8f * s, cx + 55f * s, cy + 1f * s, cx + 36f * s, cy - 8f * s);
        // Broad rounded snout contour
        sr.circle(cx + 53f * s, cy + 1f * s, 5.5f * s);
        sr.setColor(0.25f, 0.32f, 0.36f, 1f);
        sr.rectLine(cx + 56f * s, cy, cx + 44f * s, cy - 2f * s, 1.6f * s);

        // 6. Broad Pectoral Fin with Dark Base Spot (Vây ngực bản rộng có đốm đen ở gốc)
        sr.setColor(0.80f, 0.88f, 0.92f, 0.85f);
        sr.triangle(cx + 20f * s, cy - 1f * s, cx + 4f * s, cy - 11f * s, cx + 14f * s, cy - 6f * s);
        sr.setColor(0.18f, 0.22f, 0.28f, 0.9f); // Dark axillary spot at pectoral base
        sr.circle(cx + 20f * s, cy - 1f * s, 2.5f * s);

        // 7. Shiny Eye
        sr.setColor(0.92f, 0.95f, 0.85f, 1f);
        sr.circle(cx + 38f * s, cy + 5f * s, 5.2f * s);
        sr.setColor(0.10f, 0.12f, 0.15f, 1f);
        sr.circle(cx + 38f * s, cy + 5f * s, 3.4f * s);
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(cx + 39.2f * s, cy + 6.2f * s, 1.3f * s);
    }

    // =========================================================================
    // 24. CÁ TRÍCH (Sea Herring: Slender Compressed Body, Sparkling Silver Scales, Deep Marine Cobalt)
    // =========================================================================
    public static void drawCaTrich(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float baseX = cx - 44f * s;
        float baseY = cy;
        float finWag = wag * 3.0f * s;
        float tipX = cx - 74f * s + wag * 1.4f * s;
        float notchX = cx - 58f * s + wag * 0.7f * s;

        // 0. Bubbles and schooling silver motes
        sr.setColor(0.70f, 0.90f, 1f, 0.50f);
        sr.circle(cx - 52f * s, cy + 22f * s, 2.0f * s);
        sr.circle(cx + 25f * s, cy + 24f * s, 1.6f * s);
        sr.circle(cx - 20f * s, cy - 22f * s, 2.2f * s);

        // 1. Caudal Fin (Deeply forked translucent silver fin with dark margins, fixed base)
        sr.setColor(0.20f, 0.38f, 0.55f, 0.95f);
        sr.triangle(baseX, baseY, tipX, cy + 26f * s + finWag, notchX, cy + 2f * s + finWag * 0.5f);
        sr.triangle(baseX, baseY, tipX, cy - 26f * s + finWag, notchX, cy - 2f * s + finWag * 0.5f);
        sr.setColor(0.85f, 0.94f, 1f, 0.85f);
        sr.triangle(baseX - 2f * s, baseY, tipX + 4f * s, cy + 22f * s + finWag, notchX - 2f * s, cy + 2f * s + finWag * 0.5f);
        sr.triangle(baseX - 2f * s, baseY, tipX + 4f * s, cy - 22f * s + finWag, notchX - 2f * s, cy - 2f * s + finWag * 0.5f);
        sr.setColor(0.12f, 0.25f, 0.38f, 0.9f);
        sr.rectLine(baseX, baseY, tipX, cy + 24f * s + finWag, 1.3f * s);
        sr.rectLine(baseX, baseY, tipX, cy - 24f * s + finWag, 1.3f * s);

        // 2. Delicate Mid-Body Dorsal & Anal Fin
        sr.setColor(0.35f, 0.55f, 0.75f, 0.85f);
        sr.triangle(cx - 8f * s, cy + 15f * s, cx + 12f * s, cy + 14f * s, cx + 2f * s, cy + 30f * s);
        sr.setColor(0.85f, 0.95f, 1f, 0.9f);
        sr.rectLine(cx - 6f * s, cy + 15f * s, cx + 2f * s, cy + 30f * s, 1.4f * s);

        // Anal fin
        sr.setColor(0.80f, 0.92f, 1f, 0.75f);
        sr.triangle(cx - 28f * s, cy - 11f * s, cx - 12f * s, cy - 12f * s, cx - 22f * s, cy - 20f * s);

        // 3. Slender Compressed Body (Lưng xanh cobalt thẫm, bụng dát bạc)
        sr.setColor(0.10f, 0.25f, 0.48f, 1f); // Deep marine cobalt back
        sr.ellipse(cx - 48f * s, cy - 16f * s, 98f * s, 34f * s);
        sr.setColor(0.22f, 0.46f, 0.72f, 1f); // Sapphire transition
        sr.ellipse(cx - 46f * s, cy - 15f * s, 94f * s, 30f * s);
        sr.setColor(0.86f, 0.92f, 0.98f, 1f); // Dazzling silver flank
        sr.ellipse(cx - 44f * s, cy - 15f * s, 90f * s, 24f * s);
        sr.setColor(0.98f, 0.99f, 1.0f, 1f); // Mirror white belly with keel
        sr.ellipse(cx - 40f * s, cy - 15f * s, 82f * s, 16f * s);

        // Sharp ventral keel line
        sr.setColor(1f, 1f, 1f, 1f);
        sr.rectLine(cx + 20f * s, cy - 14f * s, cx - 36f * s, cy - 12f * s, 1.8f * s);

        // 4. SIGNATURE SPARKLING DIAMOND SCALE REFLECTIONS (Vảy bạc lấp lánh như kim cương)
        sr.setColor(1f, 1f, 1f, 0.85f);
        for (int i = 0; i < 9; i++) {
            float scX = cx - 30f * s + i * 7.0f * s;
            float scY = cy + ((i % 2 == 0) ? 2f : -3f) * s;
            sr.rect(scX - 1.5f * s, scY - 1.5f * s, 1.5f * s, 1.5f * s, 3f * s, 3f * s, 1f, 1f, 45f);
        }

        // 5. Head & Operculum with Golden-Cyan Sheen
        sr.setColor(0.18f, 0.38f, 0.60f, 1f);
        sr.triangle(cx + 34f * s, cy + 11f * s, cx + 52f * s, cy, cx + 34f * s, cy - 10f * s);
        sr.setColor(0.90f, 0.95f, 1f, 1f);
        sr.triangle(cx + 36f * s, cy + 7f * s, cx + 52f * s, cy, cx + 36f * s, cy - 7f * s);
        // Golden gill highlight
        sr.setColor(0.95f, 0.88f, 0.35f, 0.65f);
        sr.circle(cx + 28f * s, cy - 1f * s, 6.5f * s);
        sr.setColor(0.35f, 0.50f, 0.70f, 0.85f);
        sr.rectLine(cx + 22f * s, cy + 9f * s, cx + 25f * s, cy - 7f * s, 1.5f * s);
        // Mouth
        sr.setColor(0.15f, 0.25f, 0.35f, 1f);
        sr.rectLine(cx + 52f * s, cy, cx + 42f * s, cy - 2f * s, 1.4f * s);

        // 6. Pectoral Fin
        sr.setColor(0.80f, 0.90f, 0.98f, 0.85f);
        sr.triangle(cx + 18f * s, cy - 2f * s, cx + 2f * s, cy - 11f * s, cx + 10f * s, cy - 6f * s);

        // 7. Shiny Golden-Rimmed Eye
        sr.setColor(0.95f, 0.88f, 0.25f, 1f);
        sr.circle(cx + 36f * s, cy + 3.5f * s, 5.2f * s);
        sr.setColor(0.08f, 0.10f, 0.15f, 1f);
        sr.circle(cx + 36f * s, cy + 3.5f * s, 3.4f * s);
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(cx + 37.2f * s, cy + 4.8f * s, 1.4f * s);
    }

    // =========================================================================
    // 25. CÁ CHIM BIỂN (Silver Pomfret: Deep Rhomboid Disc, High Falcate Wing Fins, Platinum Pearl)
    // =========================================================================
    public static void drawCaChimBien(ShapeRenderer sr, float cx, float cy, float s, float wag) {
        float baseX = cx - 40f * s;
        float baseY = cy;
        float finWag = wag * 3.2f * s;
        float tipX = cx - 76f * s + wag * 1.5f * s;
        float notchX = cx - 54f * s + wag * 0.7f * s;

        // 1. SIGNATURE DEEPLY FORKED LONG-LOBED TAIL (Đuôi chẻ sâu chữ V với thùy dưới dài hơn thùy trên)
        // Fixed base on body!
        sr.setColor(0.35f, 0.45f, 0.55f, 0.95f);
        sr.triangle(baseX, baseY, tipX + 4f * s, cy + 28f * s + finWag, notchX, cy + 1f * s + finWag * 0.5f);
        sr.triangle(baseX, baseY, tipX - 4f * s, cy - 34f * s + finWag, notchX, cy - 1f * s + finWag * 0.5f); // Longer lower lobe
        sr.setColor(0.85f, 0.92f, 0.98f, 0.85f);
        sr.triangle(baseX - 2f * s, baseY, tipX + 8f * s, cy + 24f * s + finWag, notchX - 2f * s, cy + 1f * s + finWag * 0.5f);
        sr.triangle(baseX - 2f * s, baseY, tipX, cy - 30f * s + finWag, notchX - 2f * s, cy - 1f * s + finWag * 0.5f);
        sr.setColor(0.20f, 0.30f, 0.40f, 0.9f);
        sr.rectLine(baseX, baseY, tipX + 4f * s, cy + 27f * s + finWag, 1.3f * s);
        sr.rectLine(baseX, baseY, tipX - 4f * s, cy - 33f * s + finWag, 1.3f * s);

        // 2. SIGNATURE HIGH-SWEPT FALCATE WING FINS (Đôi cánh vây lưng và vây bụng vút dài như cánh chim)
        // High Falcate Dorsal Fin
        sr.setColor(0.28f, 0.38f, 0.50f, 0.95f);
        sr.triangle(cx - 16f * s, cy + 22f * s, cx + 18f * s, cy + 18f * s, cx - 6f * s, cy + 46f * s);
        sr.setColor(0.80f, 0.90f, 0.98f, 0.85f);
        sr.triangle(cx - 14f * s, cy + 22f * s, cx + 14f * s, cy + 18f * s, cx - 4f * s, cy + 42f * s);
        sr.setColor(0.18f, 0.28f, 0.38f, 0.9f);
        sr.rectLine(cx + 12f * s, cy + 18f * s, cx - 6f * s, cy + 46f * s, 1.5f * s);

        // High Falcate Anal Fin
        sr.setColor(0.28f, 0.38f, 0.50f, 0.95f);
        sr.triangle(cx - 16f * s, cy - 22f * s, cx + 18f * s, cy - 18f * s, cx - 6f * s, cy - 46f * s);
        sr.setColor(0.80f, 0.90f, 0.98f, 0.85f);
        sr.triangle(cx - 14f * s, cy - 22f * s, cx + 14f * s, cy - 18f * s, cx - 4f * s, cy - 42f * s);
        sr.setColor(0.18f, 0.28f, 0.38f, 0.9f);
        sr.rectLine(cx + 12f * s, cy - 18f * s, cx - 6f * s, cy - 46f * s, 1.5f * s);

        // 3. DEEP RHOMBOID COMPRESSED DISC BODY (Thân hình thoi dẹp sâu như chiếc đĩa bay màu bạch kim)
        sr.setColor(0.32f, 0.36f, 0.48f, 1f); // Silvery purple dorsal contour
        sr.ellipse(cx - 42f * s, cy - 26f * s, 86f * s, 54f * s);
        sr.setColor(0.68f, 0.74f, 0.85f, 1f); // Platinum pearl sheen
        sr.ellipse(cx - 40f * s, cy - 25f * s, 82f * s, 50f * s);
        sr.setColor(0.88f, 0.94f, 0.98f, 1f); // Mirror silver
        sr.ellipse(cx - 38f * s, cy - 22f * s, 76f * s, 44f * s);
        sr.setColor(0.98f, 0.99f, 1.0f, 1f); // Pure white highlight
        sr.ellipse(cx - 32f * s, cy - 16f * s, 64f * s, 32f * s);

        // 4. Subtle lateral line
        sr.setColor(0.55f, 0.65f, 0.75f, 0.7f);
        sr.rectLine(cx + 22f * s, cy + 4f * s, cx - 38f * s, cy + 2f * s, 1.6f * s);

        // 5. Short Rounded Blunt Snout & Small Mouth
        sr.setColor(0.40f, 0.46f, 0.56f, 1f);
        sr.circle(cx + 36f * s, cy, 14f * s);
        sr.setColor(0.88f, 0.94f, 0.98f, 1f);
        sr.circle(cx + 36f * s, cy, 12f * s);
        sr.setColor(0.20f, 0.25f, 0.35f, 1f);
        sr.rectLine(cx + 46f * s, cy, cx + 38f * s, cy - 3f * s, 1.5f * s);

        // Gill crescent line
        sr.setColor(0.45f, 0.55f, 0.65f, 0.8f);
        sr.rectLine(cx + 20f * s, cy + 14f * s, cx + 24f * s, cy - 12f * s, 1.8f * s);

        // 6. Long Sickle Pectoral Fin (Vây ngực hình lưỡi liềm dài vút)
        sr.setColor(0.85f, 0.92f, 0.98f, 0.9f);
        sr.triangle(cx + 16f * s, cy - 2f * s, cx - 6f * s, cy - 18f * s, cx + 8f * s, cy - 8f * s);
        sr.setColor(0.35f, 0.45f, 0.55f, 0.85f);
        sr.rectLine(cx + 16f * s, cy - 2f * s, cx - 5f * s, cy - 17f * s, 1.4f * s);

        // 7. Small Bright Dark Eye
        sr.setColor(0.92f, 0.96f, 1f, 1f);
        sr.circle(cx + 34f * s, cy + 4f * s, 4.8f * s);
        sr.setColor(0.08f, 0.10f, 0.14f, 1f);
        sr.circle(cx + 34f * s, cy + 4f * s, 3.0f * s);
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(cx + 35.2f * s, cy + 5.2f * s, 1.2f * s);
    }

    // =========================================================================
    // GENERIC FISH FALLBACK (Streamlined torpedo, scales, rarity-tinted fins)
    // =========================================================================
    private static void drawGenericFish(ShapeRenderer sr, float cx, float cy, float s, float wag, Color col) {
        float baseX = cx - 32f * s;
        float baseY = cy;
        float finWag = wag * 2.8f * s;
        float tipX = cx - 68f * s + wag * 1.2f * s;
        float notchX = cx - 52f * s + wag * 0.6f * s;

        // Tail fin in rarity color (fixed base on body, waving fin tips)
        sr.setColor(col.r, col.g, col.b, 0.9f);
        sr.triangle(baseX, baseY, tipX, cy + 22f * s + finWag, notchX, cy + finWag * 0.5f);
        sr.triangle(baseX, baseY, tipX, cy - 22f * s + finWag, notchX, cy - finWag * 0.5f);

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
