package com.fishingrpg.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

/**
 * Ultra-detailed, living Tropical Beach & Coral Lagoon ("Bãi Biển") background renderer.
 * Features:
 * - Multi-band tropical turquoise to deep sapphire ocean water gradient
 * - Dynamic moving ocean caustics light network & shimmering sunbeams
 * - Pristine golden sand seabed with depth contours, shells & colorful starfish
 * - Magnificent multi-colored coral reef structures (staghorn, brain corals, sea fans)
 * - Bioluminescent swaying sea anemones with waving tentacles
 * - Lush forests of emerald sea kelp swaying with the ocean tide
 * - Swimming ambient marine life: schools of tropical fish, sea turtle, pulsing jellyfish
 * - Rising oxygen bubbles with organic wobble
 * - Tropical coconut palm fronds framing top corners swaying in ocean breeze
 */
public class BaiBienBackground {

    public static void render(ShapeRenderer sr, float timer, float W, float H) {
        // =========================================================================
        // 1. TROPICAL OCEAN WATER GRADIENT (Turquoise Lagoon to Deep Sapphire)
        // =========================================================================
        int bands = 18;
        float bandH = H / bands;
        for (int i = 0; i < bands; i++) {
            float t = (float) i / (bands - 1);
            // Top: dazzling crystal cyan-turquoise; Bottom: deep sapphire reef blue
            float r = MathUtils.lerp(0.04f, 0.12f, t);
            float g = MathUtils.lerp(0.24f, 0.65f, t);
            float b = MathUtils.lerp(0.48f, 0.88f, t);
            sr.setColor(r, g, b, 1f);
            sr.rect(0, i * bandH, W, bandH + 1.5f);
        }

        // =========================================================================
        // 2. VOLUMETRIC TROPICAL SUNBEAMS & SHIMMERING OCEAN CAUSTICS
        // =========================================================================
        // Golden tropical sunbeams slicing down through clear waves
        sr.setColor(0.65f, 0.92f, 1.0f, 0.09f + 0.03f * MathUtils.sin(timer * 1.3f));
        sr.triangle(120f, H, 260f, H, 0f, 40f);
        sr.setColor(0.70f, 0.95f, 0.90f, 0.08f + 0.02f * MathUtils.sin(timer * 1.6f + 1f));
        sr.triangle(340f, H, 500f, H, 160f, 20f);
        sr.setColor(0.75f, 0.98f, 1.0f, 0.10f + 0.03f * MathUtils.sin(timer * 1.1f + 2f));
        sr.triangle(620f, H, 800f, H, 420f, 10f);
        sr.setColor(0.68f, 0.94f, 0.85f, 0.07f + 0.02f * MathUtils.sin(timer * 1.8f + 0.5f));
        sr.triangle(920f, H, 1080f, H, 740f, 30f);
        sr.setColor(0.72f, 0.96f, 1.0f, 0.08f + 0.03f * MathUtils.sin(timer * 1.4f + 3f));
        sr.triangle(1160f, H, 1280f, H, 1020f, 40f);

        // Dynamic Sea Caustics Network (Lưới ánh sáng khúc xạ đáy biển)
        sr.setColor(0.75f, 1.0f, 0.95f, 0.08f);
        for (int c = 0; c < 10; c++) {
            float cx = (c * 155f + timer * 28f) % (W + 200f) - 100f;
            float cy = 180f + 90f * MathUtils.sin(timer * 0.9f + c * 1.4f);
            float cRadius = 45f + 18f * MathUtils.sin(timer * 1.5f + c * 2f);
            sr.circle(cx, cy, cRadius, 20);
        }

        // =========================================================================
        // 3. BACKGROUND AMBIENT MARINE LIFE (Bóng cá nhiệt đới, sứa & rùa biển)
        // =========================================================================
        // School of small turquoise-silver tropical fish swimming right
        sr.setColor(0.20f, 0.55f, 0.75f, 0.40f);
        for (int f = 0; f < 6; f++) {
            float fx = (f * 45f + timer * 45f) % (W + 200f) - 100f;
            float fy = 380f + (f % 3) * 35f + MathUtils.sin(timer * 2.5f + f) * 12f;
            sr.triangle(fx, fy, fx - 16f, fy + 5f, fx - 16f, fy - 5f);
            sr.triangle(fx - 14f, fy, fx - 22f, fy + 7f, fx - 22f, fy - 7f);
        }

        // Graceful Sea Turtle gliding in distant waters
        float turtleX = (W + 300f) - ((timer * 22f) % (W + 500f));
        float turtleY = 460f + MathUtils.sin(timer * 0.6f) * 20f;
        sr.setColor(0.14f, 0.38f, 0.48f, 0.35f);
        sr.circle(turtleX, turtleY, 24f, 24); // Carapace
        sr.circle(turtleX - 26f, turtleY + 2f, 9f); // Head
        // Flippers
        sr.triangle(turtleX - 10f, turtleY + 12f, turtleX - 42f, turtleY + 32f + MathUtils.sin(timer * 2f) * 10f, turtleX + 6f, turtleY + 18f);
        sr.triangle(turtleX - 10f, turtleY - 12f, turtleX - 42f, turtleY - 32f - MathUtils.sin(timer * 2f) * 10f, turtleX + 6f, turtleY - 18f);

        // Pulsing Translucent Jellyfish (Sứa biển bơi lượn)
        for (int j = 0; j < 2; j++) {
            float jx = 220f + j * 750f + MathUtils.sin(timer * 0.8f + j * 3f) * 45f;
            float jy = 280f + ((timer * 18f + j * 160f) % 360f);
            float pulse = 0.85f + 0.15f * MathUtils.sin(timer * 3f + j * 2f);

            // Jellyfish dome bell
            sr.setColor(0.85f, 0.55f, 0.95f, 0.28f);
            sr.circle(jx, jy, 22f * pulse, 24);
            sr.setColor(0.25f, 0.85f, 1.0f, 0.35f);
            sr.circle(jx, jy + 2f, 18f * pulse, 20);

            // Trailing tentacles
            sr.setColor(0.90f, 0.65f, 1.0f, 0.22f);
            for (int t = -2; t <= 2; t++) {
                float wave = MathUtils.sin(timer * 3f + t * 0.8f) * 6f;
                sr.rectLine(jx + t * 6f, jy - 6f, jx + t * 8f + wave, jy - 36f, 1.8f);
            }
        }

        // =========================================================================
        // 4. GOLDEN SAND SEABED & REEF CONTOURS (Đáy biển cát vàng uốn lượn)
        // =========================================================================
        // Deep sand shadow base
        sr.setColor(0.58f, 0.48f, 0.24f, 1f);
        sr.rect(0, 0, W, 85f);

        // Smooth undulating dunes
        sr.setColor(0.74f, 0.62f, 0.32f, 1f);
        for (int i = 0; i <= 16; i++) {
            float dx = i * (W / 16f);
            float dh = 72f + MathUtils.sin(i * 0.6f) * 22f;
            sr.circle(dx, dh - 10f, 52f, 24);
        }
        sr.rect(0, 0, W, 68f);

        // Bright sunlit sand crust
        sr.setColor(0.86f, 0.74f, 0.42f, 1f);
        for (int i = 0; i <= 18; i++) {
            float dx = i * (W / 18f);
            float dh = 55f + MathUtils.cos(i * 0.75f) * 16f;
            sr.circle(dx, dh - 8f, 44f, 20);
        }
        sr.rect(0, 0, W, 48f);

        // Sea shells & pebbles scattered on the sand
        sr.setColor(0.95f, 0.88f, 0.72f, 0.9f);
        float[] shellX = { 85f, 210f, 410f, 580f, 780f, 950f, 1140f };
        float[] shellY = { 38f, 44f, 32f, 50f, 36f, 42f, 30f };
        for (int s = 0; s < shellX.length; s++) {
            sr.circle(shellX[s], shellY[s], 4.5f + (s % 3) * 1.5f);
        }

        // Colorful Starfish (Sao biển đỏ cam & tím)
        drawStarfish(sr, 160f, 34f, 11f, new Color(0.95f, 0.35f, 0.20f, 1f));
        drawStarfish(sr, 860f, 38f, 12f, new Color(0.85f, 0.30f, 0.60f, 1f));
        drawStarfish(sr, 1180f, 28f, 10f, new Color(1.0f, 0.55f, 0.15f, 1f));

        // =========================================================================
        // 5. LIVING CORAL REEFS (San Hô Cành, San Hô Quạt, Hải Quỳ & San Hô Não)
        // =========================================================================
        // Left Coral Grove (Staghorn Corals & Sea Anemones)
        drawCoralCluster(sr, 80f, 45f, 1.25f, timer, 0f);
        drawCoralCluster(sr, 260f, 52f, 0.95f, timer, 1.5f);

        // Center-Right Coral Formations
        drawCoralCluster(sr, 680f, 48f, 1.10f, timer, 2.2f);
        drawCoralCluster(sr, 1040f, 42f, 1.35f, timer, 0.8f);

        // =========================================================================
        // 6. SWAYING EMERALD SEAWEED & KELP FORESTS (Rừng Rong Biển Đung Đưa)
        // =========================================================================
        drawKelpCluster(sr, 30f, 50f, 140f, timer, 0.2f);
        drawKelpCluster(sr, 190f, 55f, 165f, timer, 1.1f);
        drawKelpCluster(sr, 380f, 45f, 130f, timer, 2.3f);
        drawKelpCluster(sr, 540f, 52f, 150f, timer, 0.7f);
        drawKelpCluster(sr, 810f, 48f, 160f, timer, 1.9f);
        drawKelpCluster(sr, 970f, 55f, 135f, timer, 2.8f);
        drawKelpCluster(sr, 1220f, 45f, 155f, timer, 1.4f);

        // =========================================================================
        // 7. RISING OCEAN OXYGEN BUBBLES (Bọt khí đại dương ngoi lên mặt nước)
        // =========================================================================
        sr.setColor(0.75f, 0.95f, 1.0f, 0.45f);
        for (int b = 0; b < 28; b++) {
            float bx = (b * 47f + timer * 14f) % W;
            float by = ((b * 89f + timer * 65f) % (H + 60f)) - 30f;
            float wobble = MathUtils.sin(timer * 3f + b * 1.5f) * 12f;
            float bubbleR = 2.0f + (b % 4) * 1.2f;
            sr.circle(bx + wobble, by, bubbleR);
            // Specular glint on bubble
            sr.setColor(1f, 1f, 1f, 0.65f);
            sr.circle(bx + wobble - 0.7f, by + 0.7f, bubbleR * 0.35f);
            sr.setColor(0.75f, 0.95f, 1.0f, 0.45f);
        }

        // =========================================================================
        // 8. TROPICAL COCONUT PALM FRONDS (Tán Dừa Biển Rủ Xuống 2 Bên Góc Trên)
        // =========================================================================
        drawPalmCanopy(sr, timer, W, H);

        // Surface Ocean Crest Foam Line (Đường bọt sóng biển trên cùng)
        sr.setColor(1f, 1f, 1f, 0.25f + 0.10f * MathUtils.sin(timer * 2.5f));
        sr.rect(0, H - 12f, W, 8f);
        sr.setColor(0.85f, 0.98f, 1f, 0.35f);
        for (int w = 0; w < 24; w++) {
            float wx = w * (W / 24f);
            float wy = H - 8f + MathUtils.sin(timer * 3f + w * 0.8f) * 4f;
            sr.circle(wx, wy, 14f, 12);
        }
    }

    // =========================================================================
    // HELPER: COLORFUL STARFISH (Sao biển 5 cánh)
    // =========================================================================
    private static void drawStarfish(ShapeRenderer sr, float cx, float cy, float r, Color col) {
        sr.setColor(col);
        sr.circle(cx, cy, r * 0.45f);
        for (int i = 0; i < 5; i++) {
            float angle = i * 72f * MathUtils.degreesToRadians;
            float tipX = cx + MathUtils.cos(angle) * r;
            float tipY = cy + MathUtils.sin(angle) * r;
            float leftA = angle - 28f * MathUtils.degreesToRadians;
            float rightA = angle + 28f * MathUtils.degreesToRadians;
            sr.triangle(cx + MathUtils.cos(leftA) * (r * 0.4f), cy + MathUtils.sin(leftA) * (r * 0.4f),
                        cx + MathUtils.cos(rightA) * (r * 0.4f), cy + MathUtils.sin(rightA) * (r * 0.4f),
                        tipX, tipY);
        }
        // Center jewel dot
        sr.setColor(1f, 0.95f, 0.7f, 1f);
        sr.circle(cx, cy, r * 0.18f);
    }

    // =========================================================================
    // HELPER: CORAL REEF CLUSTER (Cụm San Hô Biển Đa Sắc)
    // =========================================================================
    private static void drawCoralCluster(ShapeRenderer sr, float cx, float cy, float scale, float timer, float phase) {
        // 1. Golden Brain Coral mound (Khối san hô não tròn)
        sr.setColor(0.90f, 0.72f, 0.25f, 0.95f);
        sr.circle(cx, cy + 18f * scale, 22f * scale, 20);
        sr.setColor(0.75f, 0.58f, 0.18f, 0.95f);
        for (int k = -1; k <= 1; k++) {
            sr.rectLine(cx - 12f * scale, cy + (18f + k * 8f) * scale, cx + 12f * scale, cy + (18f + k * 8f) * scale, 2f * scale);
        }

        // 2. Pink & Crimson Staghorn Coral Branches (Cành san hô đỏ hồng)
        sr.setColor(0.95f, 0.32f, 0.48f, 1f);
        // Central main stem
        sr.rectLine(cx - 8f * scale, cy, cx - 18f * scale, cy + 54f * scale, 6f * scale);
        sr.circle(cx - 18f * scale, cy + 54f * scale, 3.5f * scale);
        // Secondary antler branches
        sr.rectLine(cx - 14f * scale, cy + 24f * scale, cx - 32f * scale, cy + 45f * scale, 4.5f * scale);
        sr.circle(cx - 32f * scale, cy + 45f * scale, 3f * scale);
        sr.rectLine(cx - 16f * scale, cy + 34f * scale, cx - 8f * scale, cy + 62f * scale, 4f * scale);
        sr.circle(cx - 8f * scale, cy + 62f * scale, 2.5f * scale);

        // 3. Bioluminescent Sea Anemone (Hải quỳ tím phát quang đung đưa)
        float anemoneX = cx + 22f * scale;
        float anemoneY = cy + 10f * scale;
        sr.setColor(0.75f, 0.25f, 0.85f, 1f);
        sr.circle(anemoneX, anemoneY, 14f * scale);
        // Waving tentacles
        sr.setColor(0.35f, 0.85f, 1.0f, 0.95f);
        for (int t = 0; t < 7; t++) {
            float sway = MathUtils.sin(timer * 2.8f + phase + t * 0.9f) * (6f * scale);
            sr.rectLine(anemoneX + (t - 3) * 3.5f * scale, anemoneY,
                        anemoneX + (t - 3) * 5f * scale + sway, anemoneY + 22f * scale, 2.2f * scale);
            sr.circle(anemoneX + (t - 3) * 5f * scale + sway, anemoneY + 22f * scale, 1.6f * scale);
        }
    }

    // =========================================================================
    // HELPER: SWAYING KELP FOREST (Rừng Rong Biển Lượn Sóng)
    // =========================================================================
    private static void drawKelpCluster(ShapeRenderer sr, float rx, float ry, float maxH, float timer, float phase) {
        int segments = 8;
        float segH = maxH / segments;

        // Dark forest emerald backing
        sr.setColor(0.10f, 0.40f, 0.24f, 0.85f);
        float prevX = rx;
        float prevY = ry;
        for (int s = 1; s <= segments; s++) {
            float sway = MathUtils.sin(timer * 2.2f + phase + s * 0.4f) * (s * 3.2f);
            float nextX = rx + sway;
            float nextY = ry + s * segH;
            sr.rectLine(prevX, prevY, nextX, nextY, 7.5f - s * 0.5f);
            // Leaf blades
            if (s % 2 == 0) {
                float leafDir = (s % 4 == 0) ? 18f : -18f;
                sr.triangle(nextX, nextY, nextX + leafDir, nextY + 12f, nextX, nextY + 20f);
            }
            prevX = nextX;
            prevY = nextY;
        }

        // Vibrant Lime-Green Foreground Kelp Frond
        sr.setColor(0.20f, 0.65f, 0.35f, 0.95f);
        prevX = rx + 8f;
        prevY = ry;
        for (int s = 1; s <= segments; s++) {
            float sway = MathUtils.sin(timer * 2.2f + phase + 0.6f + s * 0.4f) * (s * 3.6f);
            float nextX = rx + 8f + sway;
            float nextY = ry + s * (segH * 0.85f);
            sr.rectLine(prevX, prevY, nextX, nextY, 6f - s * 0.5f);
            if (s % 2 == 1) {
                float leafDir = (s % 4 == 1) ? -15f : 15f;
                sr.triangle(nextX, nextY, nextX + leafDir, nextY + 10f, nextX, nextY + 16f);
            }
            prevX = nextX;
            prevY = nextY;
        }
    }

    // =========================================================================
    // HELPER: TROPICAL COCONUT PALM CANOPY (Lá Dừa Nhiệt Đới Rủ Xuống)
    // =========================================================================
    private static void drawPalmCanopy(ShapeRenderer sr, float timer, float W, float H) {
        // Left Palm Fronds (Vươn từ góc trên bên trái)
        float swayL = MathUtils.sin(timer * 1.5f) * 6f;
        drawSinglePalmFrond(sr, -20f, H + 20f, 320f + swayL, H - 140f, 1);
        drawSinglePalmFrond(sr, -40f, H + 40f, 260f + swayL * 0.8f, H - 200f, 1);

        // Right Palm Fronds (Vươn từ góc trên bên phải)
        float swayR = MathUtils.cos(timer * 1.5f) * 6f;
        drawSinglePalmFrond(sr, W + 20f, H + 20f, W - 320f + swayR, H - 140f, -1);
        drawSinglePalmFrond(sr, W + 40f, H + 40f, W - 260f + swayR * 0.8f, H - 200f, -1);
    }

    private static void drawSinglePalmFrond(ShapeRenderer sr, float startX, float startY, float endX, float endY, int dir) {
        int frondSteps = 10;
        // Central thick leaf spine (sống lá dừa)
        sr.setColor(0.18f, 0.45f, 0.15f, 0.95f);
        float pX = startX;
        float pY = startY;
        for (int i = 1; i <= frondSteps; i++) {
            float t = (float) i / frondSteps;
            float nX = MathUtils.lerp(startX, endX, t);
            float nY = MathUtils.lerp(startY, endY, t) - (float) Math.sin(t * MathUtils.PI) * 45f;
            sr.rectLine(pX, pY, nX, nY, 5f - t * 3.5f);

            // Serrated palm leaflets hanging down
            sr.setColor(0.24f, 0.58f, 0.20f, 0.92f);
            sr.triangle(nX, nY, nX + dir * 14f, nY - 32f, nX + dir * 3f, nY - 38f);
            sr.setColor(0.18f, 0.45f, 0.15f, 0.95f);

            pX = nX;
            pY = nY;
        }
    }
}
