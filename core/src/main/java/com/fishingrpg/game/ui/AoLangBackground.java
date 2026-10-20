package com.fishingrpg.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

/**
 * Ultra-detailed, living Vietnamese Village Pond ("Ao Làng") background renderer.
 * Features:
 * - Lush multi-band freshwater jade gradient
 * - Atmospheric volumetric sunbeams slicing through water
 * - Natural pond bed with river stones, sediment curves & swaying aquatic reeds
 * - Ambient underwater fish silhouettes swimming in deep layers
 * - Floating lotus pads (lá sen) with leaf veins & droplets
 * - Handcrafted blooming pink lotus flowers (hoa sen) & buds
 * - Swaying weeping willow branches & leaves (cành liễu rủ) from canopy
 * - Darting red dragonflies (chuồn chuồn ớt) with fluttering wings
 * - Gentle glowing water fireflies and floating pollen motes
 * - Expanding water ripple rings
 */
public class AoLangBackground {

    public static void render(ShapeRenderer sr, float timer, float W, float H) {
        // =========================================================================
        // 1. DEEP TRANQUIL JADE-EMERALD POND WATER GRADIENT
        // =========================================================================
        int bands = 18;
        float bandH = H / bands;
        for (int i = 0; i < bands; i++) {
            float t = (float) i / (bands - 1);
            // Top: fresh morning turquoise-teal; Bottom: deep pond silt jade
            float r = MathUtils.lerp(0.04f, 0.14f, t);
            float g = MathUtils.lerp(0.18f, 0.44f, t);
            float b = MathUtils.lerp(0.22f, 0.48f, t);
            sr.setColor(r, g, b, 1f);
            sr.rect(0, i * bandH, W, bandH + 1.5f);
        }

        // =========================================================================
        // 2. VOLUMETRIC MORNING SUNLIGHT RAYS (Piercing through tree canopy)
        // =========================================================================
        sr.setColor(0.45f, 0.85f, 0.75f, 0.08f + 0.03f * MathUtils.sin(timer * 1.2f));
        sr.triangle(150f, H, 280f, H, 0f, 0f);
        sr.setColor(0.50f, 0.90f, 0.70f, 0.07f + 0.02f * MathUtils.sin(timer * 1.5f + 1f));
        sr.triangle(380f, H, 520f, H, 180f, 0f);
        sr.setColor(0.55f, 0.92f, 0.75f, 0.09f + 0.03f * MathUtils.sin(timer * 1.0f + 2f));
        sr.triangle(650f, H, 820f, H, 450f, 0f);
        sr.setColor(0.48f, 0.88f, 0.72f, 0.06f + 0.02f * MathUtils.sin(timer * 1.7f + 0.5f));
        sr.triangle(950f, H, 1100f, H, 780f, 0f);

        // Water Caustics Shimmer Layers
        sr.setColor(0.6f, 0.95f, 0.9f, 0.07f);
        for (int c = 0; c < 8; c++) {
            float cx = (c * 170f + timer * 22f) % (W + 200f) - 100f;
            float cy = 200f + 70f * MathUtils.sin(timer * 0.8f + c * 1.5f);
            sr.circle(cx, cy, 55f + 15f * MathUtils.sin(timer * 1.4f + c), 20);
        }

        // =========================================================================
        // 3. AMBIENT UNDERWATER FISH SILHOUETTES (Bóng cá bơi ngầm)
        // =========================================================================
        drawAmbientFish(sr, timer);

        // =========================================================================
        // 4. NATURAL POND BED & MUDDY SEDIMENT
        // =========================================================================
        // Deep bed base
        sr.setColor(0.08f, 0.12f, 0.10f, 0.95f);
        sr.rect(0, 0, W, 45f);

        // Undulating earthen mounds
        sr.setColor(0.12f, 0.16f, 0.12f, 0.95f);
        sr.circle(120f, -30f, 110f, 28);
        sr.circle(360f, -40f, 130f, 28);
        sr.circle(680f, -35f, 120f, 28);
        sr.circle(960f, -45f, 140f, 28);
        sr.circle(1200f, -30f, 110f, 28);

        // River stones on pond bed
        drawRiverStones(sr);

        // =========================================================================
        // 5. LUSH SWAYING AQUATIC REEDS & WATER CABBAGE (Rong rêu)
        // =========================================================================
        drawAquaticReeds(sr, timer, W);

        // =========================================================================
        // 6. RIPPLE RINGS (Gợn sóng nước lăn tăn)
        // =========================================================================
        drawWaterRipples(sr, timer);

        // =========================================================================
        // 7. FLOATING LOTUS PADS & LOTUS FLOWERS (Cụm Lá Sen & Hoa Sen Hồng)
        // =========================================================================
        // Left Lotus Cluster (Shifted lower and right to avoid stamina bar)
        drawLotusPad(sr, 90f, 90f, 52f, 0.15f);
        drawLotusPad(sr, 180f, 95f, 56f, -0.4f);
        drawLotusPad(sr, 270f, 115f, 48f, 0.8f);
        drawLotusFlower(sr, 230f, 140f, 1.0f, timer); // Fully bloomed pink lotus
        drawLotusBud(sr, 310f, 130f, 0.85f, timer);    // Closed lotus bud

        // Right Lotus Cluster
        drawLotusPad(sr, W - 130f, 150f, 72f, -0.2f);
        drawLotusPad(sr, W - 230f, 110f, 58f, 0.5f);
        drawLotusPad(sr, W - 90f, 85f, 45f, -0.6f);
        drawLotusFlower(sr, W - 170f, 175f, 1.0f, timer + 1.5f); // Bloomed lotus right

        // Mid-water floating smaller lotus pad
        drawLotusPad(sr, W * 0.42f, 95f, 38f, 0.3f);
        drawLotusPad(sr, W * 0.72f, 135f, 42f, -0.1f);

        // =========================================================================
        // 8. WEEPING WILLOW BRANCHES & LEAVES (Cành Liễu Rủ Ven Hồ)
        // =========================================================================
        drawWillowBranches(sr, timer, W, H);

        // =========================================================================
        // 9. DARTING RED DRAGONFLIES (Chuồn Chuồn Ớt)
        // =========================================================================
        drawDragonflies(sr, timer, W, H);

        // =========================================================================
        // 10. GLOWING POND PARTICLES & RISING OXYGEN BUBBLES
        // =========================================================================
        drawPondParticles(sr, timer, W, H);
    }

    // =========================================================================
    // SUB-RENDERERS
    // =========================================================================

    private static void drawAmbientFish(ShapeRenderer sr, float timer) {
        // Ambient fish 1: swimming right
        float f1x = (timer * 35f) % 1500f - 150f;
        float f1y = 380f + 25f * MathUtils.sin(timer * 1.1f);
        float wag1 = MathUtils.sin(timer * 6f);
        sr.setColor(0.08f, 0.22f, 0.25f, 0.35f);
        sr.circle(f1x, f1y, 14f, 16);
        sr.circle(f1x + 10f, f1y, 10f, 14);
        sr.triangle(f1x - 10f, f1y, f1x - 26f + wag1 * 3f, f1y + 9f + wag1 * 4f, f1x - 26f + wag1 * 3f, f1y - 9f + wag1 * 4f);

        // Ambient fish 2: swimming left
        float f2x = 1400f - (timer * 28f) % 1600f;
        float f2y = 260f + 30f * MathUtils.sin(timer * 0.9f + 2f);
        float wag2 = MathUtils.sin(timer * 5f);
        sr.setColor(0.06f, 0.20f, 0.22f, 0.40f);
        sr.circle(f2x, f2y, 18f, 16);
        sr.circle(f2x - 12f, f2y, 12f, 14);
        sr.triangle(f2x + 12f, f2y, f2x + 30f + wag2 * 4f, f2y + 11f + wag2 * 4f, f2x + 30f + wag2 * 4f, f2y - 11f + wag2 * 4f);

        // Ambient fish 3: small fish schooling near bottom
        for (int k = 0; k < 3; k++) {
            float f3x = (timer * 45f + k * 35f) % 1500f - 100f;
            float f3y = 120f + k * 18f + 12f * MathUtils.sin(timer * 1.5f + k);
            sr.setColor(0.07f, 0.24f, 0.26f, 0.30f);
            sr.circle(f3x, f3y, 7f, 12);
            sr.triangle(f3x - 5f, f3y, f3x - 15f, f3y + 5f, f3x - 15f, f3y - 5f);
        }
    }

    private static void drawRiverStones(ShapeRenderer sr) {
        // Smooth river pebble clusters
        float[][] stones = {
            {60f, 18f, 16f, 10f}, {110f, 14f, 22f, 12f}, {180f, 12f, 14f, 8f},
            {320f, 16f, 26f, 14f}, {410f, 12f, 18f, 9f}, {540f, 20f, 28f, 15f},
            {660f, 14f, 20f, 11f}, {820f, 16f, 24f, 13f}, {940f, 18f, 32f, 16f},
            {1080f, 12f, 16f, 9f}, {1180f, 16f, 26f, 14f}
        };
        for (float[] st : stones) {
            // Stone base shadow
            sr.setColor(0.06f, 0.09f, 0.08f, 1f);
            sr.ellipse(st[0], st[1], st[2] * 2f, st[3] * 2f);
            // Stone body
            sr.setColor(0.18f, 0.24f, 0.22f, 1f);
            sr.ellipse(st[0], st[1] + 2f, st[2] * 1.8f, st[3] * 1.7f);
            // Top moss highlight
            sr.setColor(0.24f, 0.36f, 0.22f, 0.85f);
            sr.ellipse(st[0] - 2f, st[1] + 4f, st[2] * 1.2f, st[3] * 0.9f);
        }
    }

    private static void drawAquaticReeds(ShapeRenderer sr, float timer, float W) {
        int reedStalks = 22;
        float spacing = W / reedStalks;
        for (int i = 0; i < reedStalks; i++) {
            float baseX = i * spacing + 25f + MathUtils.sin(i * 3.7f) * 15f;
            float px = baseX;
            float py = 10f;
            int segments = 5 + (i % 4);
            float segH = 24f + (i % 3) * 6f;

            // Varied lush green hues
            float greenHue = (i % 2 == 0) ? 0.38f : 0.48f;
            sr.setColor(0.10f, greenHue, 0.18f, 0.80f);

            for (int s = 0; s < segments; s++) {
                float wave = MathUtils.sin(timer * 1.8f + i * 0.6f + s * 0.4f);
                float nx = px + wave * (4f + s * 2.2f);
                float ny = py + segH;
                float thick = Math.max(1.5f, (segments - s) * 1.4f);
                sr.rectLine(px, py, nx, ny, thick);
                px = nx;
                py = ny;
            }
        }
    }

    private static void drawWaterRipples(ShapeRenderer sr, float timer) {
        sr.setColor(0.35f, 0.75f, 0.80f, 0.22f);
        for (int r = 0; r < 4; r++) {
            float cycle = (timer * 0.4f + r * 0.25f) % 1.0f;
            float radX = 30f + cycle * 120f;
            float radY = radX * 0.32f;
            float rippleAlpha = (1f - cycle) * 0.28f;
            sr.setColor(0.40f, 0.85f, 0.85f, rippleAlpha);
            sr.ellipse(150f, 190f, radX * 2f, radY * 2f);
            sr.ellipse(1120f, 175f, radX * 1.8f, radY * 1.8f);
        }
    }

    /**
     * Draw a detailed circular Vietnamese lotus pad (lá sen) with radial veins and leaf notch.
     */
    private static void drawLotusPad(ShapeRenderer sr, float cx, float cy, float radius, float angleOffset) {
        // Underwater stem shadow
        sr.setColor(0.06f, 0.16f, 0.12f, 0.7f);
        sr.rectLine(cx, cy, cx - 15f, 0f, 4f);

        // Outer Dark Green Rim
        sr.setColor(0.12f, 0.32f, 0.16f, 1f);
        sr.circle(cx, cy, radius, 32);

        // Rich Emerald Leaf Blade
        sr.setColor(0.18f, 0.48f, 0.22f, 1f);
        sr.circle(cx, cy, radius - 2.5f, 32);

        // Bright Inner Leaf Green
        sr.setColor(0.24f, 0.58f, 0.28f, 1f);
        sr.circle(cx, cy, radius * 0.65f, 28);

        // Radial leaf veins (8 veins radiating from central stem point)
        sr.setColor(0.16f, 0.42f, 0.20f, 0.9f);
        for (int v = 0; v < 8; v++) {
            float ang = angleOffset + v * (MathUtils.PI2 / 8f);
            float vx = cx + MathUtils.cos(ang) * (radius - 4f);
            float vy = cy + MathUtils.sin(ang) * (radius - 4f);
            sr.rectLine(cx, cy, vx, vy, 1.8f);
        }

        // Distinct V-shaped leaf notch (Khe khuyết lá sen)
        float notchAng = angleOffset + 0.6f;
        float nx1 = cx + MathUtils.cos(notchAng) * radius;
        float ny1 = cy + MathUtils.sin(notchAng) * radius;
        sr.setColor(0.10f, 0.28f, 0.32f, 0.95f);
        sr.triangle(cx, cy, nx1 - 12f, ny1, nx1 + 12f, ny1);

        // Lotus central stem attachment dot (Cuống lá sen)
        sr.setColor(0.85f, 0.78f, 0.25f, 1f);
        sr.circle(cx, cy, 3.5f, 12);

        // Glistening water droplet resting on lotus pad
        sr.setColor(0.85f, 0.98f, 1f, 0.85f);
        sr.circle(cx + radius * 0.35f, cy + radius * 0.25f, 3.2f, 10);
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(cx + radius * 0.35f - 0.8f, cy + radius * 0.25f + 0.8f, 1.2f, 8);
    }

    /**
     * Draw a handcrafted blooming pink Vietnamese lotus flower (hoa sen hồng) with petals & golden seedpod.
     */
    private static void drawLotusFlower(ShapeRenderer sr, float cx, float cy, float scale, float timer) {
        float bob = 2f * MathUtils.sin(timer * 1.4f);
        float py = cy + bob;

        // Slender green flower stem
        sr.setColor(0.15f, 0.38f, 0.18f, 1f);
        sr.rectLine(cx, py - 35f, cx, py - 5f, 3.5f * scale);

        // Outer Layer Petals (Deep Lotus Rose Pink)
        sr.setColor(0.88f, 0.32f, 0.52f, 1f);
        sr.triangle(cx - 24f * scale, py - 4f, cx, py + 26f * scale, cx + 24f * scale, py - 4f);
        sr.triangle(cx - 32f * scale, py, cx - 18f * scale, py + 22f * scale, cx, py - 4f);
        sr.triangle(cx + 32f * scale, py, cx + 18f * scale, py + 22f * scale, cx, py - 4f);

        // Middle Layer Petals (Vibrant Rose Lotus Pink)
        sr.setColor(0.96f, 0.48f, 0.68f, 1f);
        sr.triangle(cx - 18f * scale, py - 2f, cx - 8f * scale, py + 28f * scale, cx + 6f * scale, py - 2f);
        sr.triangle(cx + 18f * scale, py - 2f, cx + 8f * scale, py + 28f * scale, cx - 6f * scale, py - 2f);
        sr.triangle(cx - 12f * scale, py - 2f, cx, py + 32f * scale, cx + 12f * scale, py - 2f);

        // Inner Petals (Soft White-Blush Lotus Tips)
        sr.setColor(1f, 0.82f, 0.90f, 1f);
        sr.triangle(cx - 8f * scale, py + 2f, cx, py + 30f * scale, cx + 8f * scale, py + 2f);

        // Glowing Golden Stamen & Seedpod Core (Đài sen vàng)
        sr.setColor(1f, 0.85f, 0.22f, 1f);
        sr.circle(cx, py + 8f * scale, 7.5f * scale, 16);
        sr.setColor(0.72f, 0.55f, 0.12f, 1f);
        sr.circle(cx - 2.5f * scale, py + 8f * scale, 1.5f * scale, 8);
        sr.circle(cx + 2.5f * scale, py + 8f * scale, 1.5f * scale, 8);
        sr.circle(cx, py + 10.5f * scale, 1.5f * scale, 8);
    }

    /**
     * Draw an unopened lotus bud (nụ sen hồng e ấp).
     */
    private static void drawLotusBud(ShapeRenderer sr, float cx, float cy, float scale, float timer) {
        float bob = 1.5f * MathUtils.sin(timer * 1.6f + 0.8f);
        float py = cy + bob;

        // Slender stem
        sr.setColor(0.16f, 0.40f, 0.20f, 1f);
        sr.rectLine(cx, py - 30f, cx, py - 2f, 3f * scale);

        // Green sepals at base
        sr.setColor(0.20f, 0.46f, 0.22f, 1f);
        sr.triangle(cx - 9f * scale, py, cx, py + 12f * scale, cx + 9f * scale, py);

        // Pink bud body
        sr.setColor(0.92f, 0.38f, 0.58f, 1f);
        sr.triangle(cx - 8f * scale, py + 2f, cx, py + 24f * scale, cx + 8f * scale, py + 2f);
        sr.setColor(1f, 0.72f, 0.82f, 1f);
        sr.triangle(cx - 4f * scale, py + 5f, cx, py + 24f * scale, cx + 4f * scale, py + 5f);
    }

    /**
     * Draw weeping willow branches and delicate leaves hanging from top-left and top-right edges.
     */
    private static void drawWillowBranches(ShapeRenderer sr, float timer, float W, float H) {
        // --- Canopy Foliage Crowns along Top Corners (Vòm lá liễu xanh mướt góc trên) ---
        // Left Canopy Puffs
        sr.setColor(0.12f, 0.28f, 0.14f, 0.95f);
        sr.circle(40f, H + 10f, 85f, 28);
        sr.circle(130f, H, 75f, 28);
        sr.circle(230f, H - 15f, 65f, 28);
        sr.circle(320f, H - 35f, 55f, 24);
        sr.setColor(0.20f, 0.48f, 0.22f, 0.95f);
        sr.circle(70f, H - 10f, 65f, 24);
        sr.circle(170f, H - 20f, 58f, 24);
        sr.circle(260f, H - 38f, 48f, 24);
        sr.setColor(0.28f, 0.62f, 0.26f, 0.90f);
        sr.circle(110f, H - 25f, 48f, 20);
        sr.circle(210f, H - 35f, 42f, 20);

        // Right Canopy Puffs
        sr.setColor(0.12f, 0.28f, 0.14f, 0.95f);
        sr.circle(W - 40f, H + 10f, 85f, 28);
        sr.circle(W - 130f, H, 75f, 28);
        sr.circle(W - 230f, H - 15f, 65f, 28);
        sr.circle(W - 320f, H - 35f, 55f, 24);
        sr.setColor(0.20f, 0.48f, 0.22f, 0.95f);
        sr.circle(W - 70f, H - 10f, 65f, 24);
        sr.circle(W - 170f, H - 20f, 58f, 24);
        sr.circle(W - 260f, H - 38f, 48f, 24);
        sr.setColor(0.28f, 0.62f, 0.26f, 0.90f);
        sr.circle(W - 110f, H - 25f, 48f, 20);
        sr.circle(W - 210f, H - 35f, 42f, 20);

        // --- Left Willow Tree Branch System (Curved wooden bough) ---
        sr.setColor(0.20f, 0.14f, 0.10f, 1f);
        float px = 0f, py = H + 5f;
        for (int i = 1; i <= 6; i++) {
            float frac = (float) i / 6f;
            float nx = frac * 380f;
            float ny = H - frac * 140f - MathUtils.sin(frac * MathUtils.PI) * 15f;
            sr.rectLine(px, py, nx, ny, Math.max(3f, 16f - i * 2.2f));
            px = nx; py = ny;
        }

        // Left Tendril droops (8 weeping tendrils)
        for (int b = 0; b < 8; b++) {
            float startX = 60f + b * 45f;
            float startY = H - 35f - b * 16f;
            drawWillowTendril(sr, startX, startY, 140f + (b % 3) * 35f, timer, b);
        }

        // --- Right Willow Tree Branch System (Curved wooden bough) ---
        sr.setColor(0.20f, 0.14f, 0.10f, 1f);
        float rpx = W, rpy = H + 5f;
        for (int i = 1; i <= 6; i++) {
            float frac = (float) i / 6f;
            float nx = W - frac * 380f;
            float ny = H - frac * 140f - MathUtils.sin(frac * MathUtils.PI) * 15f;
            sr.rectLine(rpx, rpy, nx, ny, Math.max(3f, 16f - i * 2.2f));
            rpx = nx; rpy = ny;
        }

        // Right Tendril droops (7 weeping tendrils)
        for (int b = 0; b < 7; b++) {
            float startX = W - 70f - b * 46f;
            float startY = H - 38f - b * 17f;
            drawWillowTendril(sr, startX, startY, 135f + (b % 3) * 30f, timer, b + 10);
        }
    }

    private static void drawWillowTendril(ShapeRenderer sr, float sx, float sy, float length, float timer, int seed) {
        float px = sx;
        float py = sy;
        int segments = 8;
        float segLen = length / segments;

        for (int s = 0; s < segments; s++) {
            float sway = MathUtils.sin(timer * 1.6f + seed * 0.7f + s * 0.35f);
            float nx = px + sway * (3.5f + s * 1.6f);
            float ny = py - segLen;

            // Branch line
            sr.setColor(0.24f, 0.36f, 0.18f, 0.9f);
            sr.rectLine(px, py, nx, ny, Math.max(1.2f, (segments - s) * 0.5f));

            // Willow leaves (alternate left and right slender leaves)
            sr.setColor(0.32f, 0.62f, 0.22f, 0.95f);
            float leafAng = (s % 2 == 0 ? 35f : -35f) * MathUtils.degreesToRadians + sway * 0.2f;
            float lx = nx + MathUtils.cos(leafAng) * 11f;
            float ly = ny + MathUtils.sin(leafAng) * 6f;
            sr.triangle(nx, ny, lx, ly, nx, ny - 5f);

            px = nx;
            py = ny;
        }
    }

    /**
     * Draw vibrant Vietnamese red dragonflies (chuồn chuồn ớt) and damselflies darting across water.
     */
    private static void drawDragonflies(ShapeRenderer sr, float timer, float W, float H) {
        // Dragonfly 1: Red dragonfly hovering and darting over lotus pads
        float df1x = 220f + 60f * MathUtils.sin(timer * 1.5f) + 30f * MathUtils.sin(timer * 4.2f);
        float df1y = 230f + 25f * MathUtils.cos(timer * 1.8f) + 15f * MathUtils.sin(timer * 5.0f);
        float wingFlap1 = MathUtils.sin(timer * 35f);

        // Long red abdomen & thorax
        sr.setColor(0.92f, 0.18f, 0.15f, 1f);
        sr.rectLine(df1x, df1y, df1x - 22f, df1y - 3f, 2.5f); // Abdomen
        sr.circle(df1x + 2f, df1y, 4f, 10); // Thorax
        sr.circle(df1x + 5f, df1y, 2.5f, 8); // Head with eyes

        // Transparent glistening wings (Forewings & Hindwings)
        sr.setColor(0.85f, 0.95f, 1f, 0.65f);
        sr.triangle(df1x + 1f, df1y, df1x + 3f, df1y + 16f * wingFlap1, df1x - 8f, df1y + 12f * wingFlap1);
        sr.triangle(df1x + 1f, df1y, df1x - 3f, df1y - 15f * wingFlap1, df1x - 12f, df1y - 11f * wingFlap1);

        // Dragonfly 2: Cyan damselfly hovering on right side
        float df2x = W - 250f + 50f * MathUtils.cos(timer * 1.3f) + 20f * MathUtils.sin(timer * 3.8f);
        float df2y = 210f + 20f * MathUtils.sin(timer * 1.6f);
        float wingFlap2 = MathUtils.sin(timer * 38f + 1f);

        sr.setColor(0.20f, 0.85f, 0.95f, 1f);
        sr.rectLine(df2x, df2y, df2x + 20f, df2y - 2f, 2f);
        sr.circle(df2x - 2f, df2y, 3.5f, 10);
        sr.circle(df2x - 4f, df2y, 2.2f, 8);

        sr.setColor(0.9f, 0.98f, 1f, 0.65f);
        sr.triangle(df2x - 1f, df2y, df2x - 2f, df2y + 14f * wingFlap2, df2x + 8f, df2y + 10f * wingFlap2);
        sr.triangle(df2x - 1f, df2y, df2x + 2f, df2y - 13f * wingFlap2, df2x + 11f, df2y - 9f * wingFlap2);
    }

    /**
     * Draw glowing pond fireflies and rising oxygen bubbles.
     */
    private static void drawPondParticles(ShapeRenderer sr, float timer, float W, float H) {
        // Glowing fireflies / golden water motes drifting softly
        for (int i = 0; i < 18; i++) {
            float px = (i * 73f + timer * 16f) % W;
            float py = 80f + (i * 47f + timer * 22f) % (H - 120f);
            float pulse = 0.45f + 0.45f * MathUtils.sin(timer * 3f + i * 1.8f);
            sr.setColor(1f, 0.92f, 0.35f, pulse * 0.75f);
            sr.circle(px, py, 2.5f + (i % 3) * 0.8f, 8);
        }

        // Fresh oxygen bubbles rising from reeds
        sr.setColor(0.6f, 0.92f, 1f, 0.45f);
        for (int b = 0; b < 24; b++) {
            float bx = (b * 59f + timer * 8f) % W;
            float by = ((b * 93f + timer * 65f) % (H + 80f)) - 40f;
            float wobble = MathUtils.sin(timer * 2.2f + b) * 8f;
            sr.circle(bx + wobble, by, 1.8f + (b % 4) * 0.7f, 10);
        }
    }
}
