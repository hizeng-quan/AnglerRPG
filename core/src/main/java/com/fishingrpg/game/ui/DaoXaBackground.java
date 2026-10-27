package com.fishingrpg.game.ui;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

/**
 * Ultra-detailed, living Remote Oceanic Island ("Đảo Xa") background renderer.
 * Features:
 * - Deep midnight cosmic sky & starry oceanic horizon gradient
 * - Twinkling open-ocean starfield with multiple star magnitudes & hues
 * - Shimmering crescent moon with multi-tier volumetric lunar halos & specular rim glint
 * - Silhouetted oceanic albatrosses gliding gracefully across the night sky
 * - Towering volcanic sea stacks & craggy basalt cliffs with moon-facing highlight facets
 * - Solitary lighthouse keeper's cottage with pitched roof, stone chimney & warm glowing hearth window
 * - Classic oceanic lighthouse with painted crimson/white nautical bands & lightning finial
 * - Dynamic 360-degree rotating light beam casting volumetric atmospheric illumination & lens flare
 * - Multi-tier rolling ocean swells with foam crests over a continuous deep-ocean water gradient
 * - Shimmering vertical moonbeam reflection path trailing across the waves
 * - Dramatic crashing surf & rising sea spray mist against sea stack bases
 * - Majestic Blue Marlin (Cá Cờ Xanh) silhouette slicing through the swell with sail-fin and spear rostrum
 * - Shark dorsal fin patrolling mid-ground waves with surface wake ripples
 * - School of pelagic oceanic fish swimming in mid-depths
 * - Abyssal ocean depths with ethereal pulsing bioluminescent jellyfish & drifting sea-spark plankton motes
 */
public class DaoXaBackground {

    // Deterministic star field data (xNorm, yNorm, baseAlpha, twinkleSpeed, size)
    private static final float[] STAR_DATA = {
        0.05f, 0.92f, 0.7f, 2.1f, 1.8f,
        0.12f, 0.84f, 0.5f, 1.4f, 1.2f,
        0.18f, 0.95f, 0.8f, 2.7f, 2.0f,
        0.24f, 0.78f, 0.6f, 1.8f, 1.4f,
        0.29f, 0.89f, 0.9f, 3.1f, 2.2f,
        0.35f, 0.81f, 0.4f, 1.2f, 1.0f,
        0.41f, 0.94f, 0.8f, 2.4f, 1.8f,
        0.48f, 0.86f, 0.5f, 1.9f, 1.3f,
        0.53f, 0.76f, 0.7f, 2.8f, 1.6f,
        0.59f, 0.91f, 0.6f, 1.5f, 1.4f,
        0.65f, 0.83f, 0.8f, 3.3f, 2.0f,
        0.71f, 0.96f, 0.5f, 1.7f, 1.2f,
        0.77f, 0.88f, 0.7f, 2.2f, 1.7f,
        0.83f, 0.79f, 0.6f, 1.3f, 1.3f,
        0.89f, 0.93f, 0.9f, 2.9f, 2.1f,
        0.95f, 0.85f, 0.5f, 1.6f, 1.2f,
        0.08f, 0.72f, 0.6f, 2.5f, 1.5f,
        0.15f, 0.67f, 0.4f, 1.1f, 1.1f,
        0.22f, 0.61f, 0.7f, 2.0f, 1.6f,
        0.31f, 0.69f, 0.5f, 1.6f, 1.3f,
        0.38f, 0.63f, 0.8f, 3.0f, 1.9f,
        0.45f, 0.71f, 0.6f, 1.8f, 1.4f,
        0.52f, 0.65f, 0.4f, 1.4f, 1.0f,
        0.61f, 0.68f, 0.7f, 2.3f, 1.7f,
        0.68f, 0.62f, 0.5f, 1.9f, 1.2f,
        0.74f, 0.71f, 0.8f, 2.6f, 1.8f,
        0.81f, 0.66f, 0.6f, 1.7f, 1.4f,
        0.88f, 0.72f, 0.4f, 1.2f, 1.1f,
        0.94f, 0.64f, 0.7f, 2.4f, 1.6f,
        0.03f, 0.59f, 0.5f, 1.5f, 1.2f,
        0.11f, 0.55f, 0.6f, 2.2f, 1.5f,
        0.28f, 0.54f, 0.5f, 1.8f, 1.3f,
        0.47f, 0.56f, 0.7f, 2.5f, 1.6f,
        0.64f, 0.53f, 0.4f, 1.3f, 1.0f,
        0.86f, 0.55f, 0.6f, 2.0f, 1.4f
    };

    public static void render(ShapeRenderer sr, float timer, float W, float H) {
        float horizonY = 368f;

        // =========================================================================
        // 1. MIDNIGHT OCEANIC SKY GRADIENT (Cosmic Space Navy -> Horizon Indigo)
        // =========================================================================
        int skyBands = 16;
        float skyH = H - horizonY;
        float bandSkyH = skyH / skyBands;
        for (int i = 0; i < skyBands; i++) {
            float t = (float) i / (skyBands - 1);
            // Top (i=skyBands-1): Deep interstellar space (#02050e); Bottom (i=0): Oceanic horizon indigo (#0e1a32)
            float r = MathUtils.lerp(0.055f, 0.008f, t);
            float g = MathUtils.lerp(0.100f, 0.020f, t);
            float b = MathUtils.lerp(0.195f, 0.055f, t);
            sr.setColor(r, g, b, 1f);
            sr.rect(0, horizonY + i * bandSkyH, W, bandSkyH + 1.5f);
        }

        // =========================================================================
        // 2. TWINKLING OPEN-OCEAN STARFIELD
        // =========================================================================
        drawStarfield(sr, timer, W, H, horizonY);

        // =========================================================================
        // 3. CRESCENT MOON & VOLUMETRIC LUNAR HALO
        // =========================================================================
        float moonX = 250f;
        float moonY = 590f;
        drawMoonAndLunarGlow(sr, timer, moonX, moonY);

        // =========================================================================
        // 4. SOFT OCEANIC NIGHT CIRRUS MIST & HORIZON SEA STACKS
        // =========================================================================
        drawNightMist(sr, timer, W, horizonY);
        drawDistantHorizonSeaStacks(sr, horizonY);

        // =========================================================================
        // 5. DISTANT GLIDING OCEANIC SEABIRDS (Hải Âu bay lượn dưới ánh trăng)
        // =========================================================================
        drawAlbatrosses(sr, timer, W, horizonY);

        // =========================================================================
        // 6. GRAND REMOTE VOLCANIC ISLAND & BASALT CLIFFS (Mỏm Đảo Đá Xa Bờ)
        // =========================================================================
        drawIslandCliffs(sr, timer, horizonY);

        // =========================================================================
        // 7. LIGHTHOUSE KEEPER'S COTTAGE (Ngôi nhà người gác đèn biển)
        // =========================================================================
        drawKeeperCottage(sr, timer, 942f, horizonY + 112f);

        // =========================================================================
        // 8. OCEAN LIGHTHOUSE (Ngọn Hải Đăng Viễn Xứ & Chùm Sáng Quét Xoay)
        // =========================================================================
        float lhX = 1010f;
        float lhY = horizonY + 112f;
        drawLighthouseAndSweepingBeam(sr, timer, lhX, lhY, W, H, horizonY);

        // =========================================================================
        // 9. CONTINUOUS DEEP OCEAN WATER GRADIENT (Horizon Dark Navy -> Abyssal Void)
        // =========================================================================
        int waterBands = 18;
        float bandWaterH = horizonY / waterBands;
        for (int i = 0; i < waterBands; i++) {
            float t = (float) i / (waterBands - 1);
            // Bottom (t=0): Abyssal trench black-navy (#01040a); Top (t=1): Surface midnight swell (#091c34)
            float r = MathUtils.lerp(0.005f, 0.038f, t);
            float g = MathUtils.lerp(0.015f, 0.110f, t);
            float b = MathUtils.lerp(0.040f, 0.205f, t);
            sr.setColor(r, g, b, 1f);
            sr.rect(0, i * bandWaterH, W, bandWaterH + 1.5f);
        }

        // =========================================================================
        // 10. SHIMMERING MOONBEAM WATER REFLECTION PATH (Cột Ánh Trăng Rọi Biển)
        // =========================================================================
        drawMoonlightWaterReflection(sr, timer, moonX, horizonY);

        // =========================================================================
        // 11. ROLLING OCEANIC SWELL & DYNAMIC WAVE CRESTS (Sóng Biển Cuồn Cuộn)
        // =========================================================================
        drawOceanWaves(sr, timer, W, horizonY);

        // =========================================================================
        // 12. CRASHING SURF & FOAM BREAKER AT CLIFF BASES (Bọt Sóng Xô Bờ Đá)
        // =========================================================================
        drawCrashingSurf(sr, timer, horizonY);

        // =========================================================================
        // 13. PELAGIC MARINE LIFE (Cá Cờ Xanh, Đàn Cá Biển & Vây Cá Mập)
        // =========================================================================
        drawPelagicFishSchool(sr, timer, W);
        drawBlueMarlin(sr, timer, W);
        drawSharkFin(sr, timer, W);

        // =========================================================================
        // 14. ABYSSAL DEPTHS: BIOLUMINESCENT JELLYFISH & NOCTILUCA SEA-SPARK
        // =========================================================================
        drawAbyssalJellyfish(sr, timer, 175f, 140f, 1.15f, 0.0f, true);   // Cyan-Teal Jellyfish
        drawAbyssalJellyfish(sr, timer, 650f, 100f, 0.88f, 2.4f, false);  // Magenta-Violet Jellyfish
        drawBioluminescentPlankton(sr, timer, W, horizonY);
        drawAbyssalSeaFloor(sr, W);
    }

    // =========================================================================
    // SUB-RENDERERS
    // =========================================================================

    private static void drawStarfield(ShapeRenderer sr, float timer, float W, float H, float horizonY) {
        int count = STAR_DATA.length / 5;
        for (int i = 0; i < count; i++) {
            int idx = i * 5;
            float nx = STAR_DATA[idx];
            float ny = STAR_DATA[idx + 1];
            float baseAlpha = STAR_DATA[idx + 2];
            float speed = STAR_DATA[idx + 3];
            float size = STAR_DATA[idx + 4];

            float sx = nx * W;
            float sy = horizonY + ny * (H - horizonY);

            // Sinusoidal twinkle
            float twinkle = 0.45f + 0.55f * MathUtils.sin(timer * speed + i * 1.7f);
            float alpha = baseAlpha * twinkle;

            // Subtle color tint (cool cyan, pale gold, pure white)
            if (i % 3 == 0) {
                sr.setColor(0.65f, 0.88f, 1.0f, alpha); // Cyan-ice star
            } else if (i % 5 == 0) {
                sr.setColor(1.0f, 0.95f, 0.75f, alpha); // Pale gold star
            } else {
                sr.setColor(0.92f, 0.96f, 1.0f, alpha); // Silvery star
            }
            sr.circle(sx, sy, size * 0.85f);
        }
    }

    private static void drawMoonAndLunarGlow(ShapeRenderer sr, float timer, float mx, float my) {
        // Multi-layered ethereal lunar aura with soft fading transparency
        float pulse = 1f + 0.03f * MathUtils.sin(timer * 1.2f);
        sr.setColor(0.50f, 0.70f, 0.95f, 0.035f);
        sr.circle(mx, my, 80f * pulse, 32);
        sr.setColor(0.65f, 0.82f, 1.0f, 0.065f);
        sr.circle(mx, my, 52f * pulse, 28);
        sr.setColor(0.80f, 0.90f, 1.0f, 0.110f);
        sr.circle(mx, my, 32f * pulse, 24);

        // Crisp Crescent Moon body
        sr.setColor(0.97f, 0.98f, 1.0f, 0.95f);
        sr.circle(mx, my, 20f, 26);

        // Crescent cutout (shadow sphere using dark sky color)
        sr.setColor(0.015f, 0.032f, 0.082f, 1f);
        sr.circle(mx + 7.0f, my + 4.0f, 17.5f, 26);

        // Specular lunar rim glint
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(mx - 11f, my - 2f, 2.0f);
    }

    private static void drawNightMist(ShapeRenderer sr, float timer, float W, float horizonY) {
        // Soft, wispy oceanic night fog banks drifting across horizon
        sr.setColor(0.12f, 0.18f, 0.32f, 0.08f);
        for (int c = 0; c < 5; c++) {
            float cx = (c * 290f + timer * 7f) % (W + 350f) - 175f;
            float cy = horizonY + 50f + (c % 3) * 35f;
            sr.ellipse(cx, cy, 260f, 32f);
            sr.ellipse(cx + 60f, cy + 12f, 180f, 24f);
        }
    }

    private static void drawDistantHorizonSeaStacks(ShapeRenderer sr, float horizonY) {
        // Distant sea stack 1 (Left far horizon, shrouded in ocean mist)
        sr.setColor(0.04f, 0.07f, 0.16f, 0.55f);
        sr.triangle(50f, horizonY, 95f, horizonY + 36f, 140f, horizonY);
        sr.triangle(115f, horizonY, 155f, horizonY + 26f, 195f, horizonY);

        // Distant needle rock pillar (Mid-left)
        sr.setColor(0.05f, 0.08f, 0.18f, 0.60f);
        sr.triangle(460f, horizonY, 485f, horizonY + 42f, 510f, horizonY);
        sr.triangle(500f, horizonY, 520f, horizonY + 28f, 540f, horizonY);
    }

    private static void drawAlbatrosses(ShapeRenderer sr, float timer, float W, float horizonY) {
        // Pair of ocean albatrosses soaring across the night sky near the moon
        sr.setColor(0.06f, 0.10f, 0.18f, 0.85f);
        for (int b = 0; b < 2; b++) {
            float bx = (160f + b * 115f + timer * 18f) % (W + 200f) - 100f;
            float by = horizonY + 165f + b * 42f + MathUtils.sin(timer * 0.8f + b) * 12f;
            float flap = MathUtils.sin(timer * 2.2f + b * 1.5f) * 3.5f;

            // Wing left
            sr.rectLine(bx, by, bx - 13f, by + 5f + flap, 1.8f);
            sr.rectLine(bx - 13f, by + 5f + flap, bx - 25f, by + 2.5f + flap * 1.2f, 1.3f);
            // Wing right
            sr.rectLine(bx, by, bx + 13f, by + 5f + flap, 1.8f);
            sr.rectLine(bx + 13f, by + 5f + flap, bx + 25f, by + 2.5f + flap * 1.2f, 1.3f);
            // Slender body
            sr.circle(bx, by, 2.0f);
        }
    }

    private static void drawIslandCliffs(ShapeRenderer sr, float timer, float horizonY) {
        // Main Volcanic Rock Mass & Sea Stacks (Right quadrant X: 720f - 1280f)
        // Background cliff ridge (Distant silhouette)
        sr.setColor(0.035f, 0.060f, 0.130f, 0.95f);
        sr.triangle(760f, horizonY, 860f, horizonY + 95f, 970f, horizonY);
        sr.triangle(910f, horizonY, 1050f, horizonY + 160f, 1200f, horizonY);
        sr.triangle(1100f, horizonY, 1210f, horizonY + 115f, 1280f, horizonY);

        // Near massive basalt cliff formation (Rising from the ocean)
        // Left sea stack pillar (Tháp đá nhọn đứng sừng sững giữa sóng)
        sr.setColor(0.045f, 0.080f, 0.165f, 1f);
        sr.triangle(735f, horizonY - 12f, 795f, horizonY + 110f, 855f, horizonY - 12f);
        // Moon-facing highlight facet on left sea stack
        sr.setColor(0.085f, 0.145f, 0.265f, 0.85f);
        sr.triangle(735f, horizonY - 12f, 795f, horizonY + 110f, 785f, horizonY - 12f);

        // Main cliff plateau body (Vách đá kiên cố đỡ ngọn hải đăng)
        sr.setColor(0.050f, 0.088f, 0.175f, 1f);
        sr.triangle(815f, horizonY - 15f, 955f, horizonY + 140f, 1075f, horizonY - 15f);
        sr.triangle(905f, horizonY - 15f, 1010f, horizonY + 125f, 1175f, horizonY - 15f);
        sr.triangle(1040f, horizonY - 15f, 1150f, horizonY + 105f, 1280f, horizonY - 15f);

        // Cliff surface craggy facets & angular rock planes
        sr.setColor(0.075f, 0.125f, 0.235f, 0.95f);
        sr.triangle(855f, horizonY, 955f, horizonY + 140f, 920f, horizonY);
        sr.triangle(940f, horizonY, 1010f, horizonY + 125f, 1050f, horizonY);

        // Lighthouse rock foundation plateau
        sr.setColor(0.065f, 0.105f, 0.200f, 1f);
        sr.rect(925f, horizonY + 105f, 140f, 8f);

        // Windswept Maritime Pines & Dwarf Shrub Silhouettes (Cây tùng biển gió lộng)
        drawMaritimePines(sr, timer, horizonY);
    }

    private static void drawMaritimePines(ShapeRenderer sr, float timer, float horizonY) {
        sr.setColor(0.025f, 0.045f, 0.085f, 0.95f);

        // Pine 1 on left crag ridge (X: 865f)
        float p1x = 865f;
        float p1y = horizonY + 70f;
        sr.rectLine(p1x, p1y, p1x - 14f, p1y + 18f, 2.5f);
        sr.rectLine(p1x - 14f, p1y + 18f, p1x - 30f, p1y + 25f, 2.0f);
        // Wind-swept canopy foliage clumps
        sr.circle(p1x - 20f, p1y + 24f, 8.5f);
        sr.circle(p1x - 32f, p1y + 27f, 10.5f);
        sr.circle(p1x - 40f, p1y + 25f, 7.5f);

        // Pine 2 on right cliff ledge (X: 1090f)
        float p2x = 1090f;
        float p2y = horizonY + 75f;
        sr.rectLine(p2x, p2y, p2x - 8f, p2y + 16f, 2.2f);
        sr.circle(p2x - 10f, p2y + 22f, 8f);
        sr.circle(p2x - 18f, p2y + 24f, 9.5f);
    }

    private static void drawKeeperCottage(ShapeRenderer sr, float timer, float cotX, float cotY) {
        // Solitary stone lighthouse keeper's cottage
        // 1. Stone cottage masonry walls (Width: 32f, Height: 18f)
        sr.setColor(0.085f, 0.125f, 0.215f, 1f);
        sr.rect(cotX, cotY, 32f, 18f);

        // 2. Weather-beaten pitched slate roof (Triangle)
        sr.setColor(0.050f, 0.075f, 0.135f, 1f);
        sr.triangle(cotX - 4f, cotY + 18f, cotX + 16f, cotY + 30f, cotX + 36f, cotY + 18f);

        // 3. Small stone chimney
        sr.setColor(0.070f, 0.100f, 0.170f, 1f);
        sr.rect(cotX + 24f, cotY + 22f, 6f, 12f);
        // Faint chimney smoke puffs drifting left in sea breeze
        sr.setColor(0.65f, 0.75f, 0.85f, 0.18f);
        for (int sm = 0; sm < 3; sm++) {
            float smX = cotX + 26f - sm * 7f - ((timer * 8f + sm * 10f) % 20f);
            float smY = cotY + 36f + sm * 7f + ((timer * 12f + sm * 8f) % 24f);
            sr.circle(smX, smY, 3.0f + sm * 1.5f);
        }

        // 4. Warm Hearth Glowing Window (Cửa sổ ánh lửa ấm cúng giữa màn đêm biển lạnh)
        float winX = cotX + 9f;
        float winY = cotY + 5f;
        // Warm halo glow
        sr.setColor(1.0f, 0.75f, 0.25f, 0.25f + 0.05f * MathUtils.sin(timer * 3f));
        sr.circle(winX + 4f, winY + 4f, 14f);
        // Glass pane
        sr.setColor(1.0f, 0.82f, 0.35f, 0.95f);
        sr.rect(winX, winY, 8f, 8f);
        // Window mullion cross
        sr.setColor(0.12f, 0.16f, 0.24f, 0.85f);
        sr.rectLine(winX + 4f, winY, winX + 4f, winY + 8f, 1.2f);
        sr.rectLine(winX, winY + 4f, winX + 8f, winY + 4f, 1.2f);
    }

    private static void drawLighthouseAndSweepingBeam(ShapeRenderer sr, float timer, float lhX, float lhY, float W, float H, float horizonY) {
        // =========================================================================
        // LIGHTHOUSE ARCHITECTURE
        // =========================================================================
        // 1. Dark stone masonry pedestal
        sr.setColor(0.080f, 0.115f, 0.200f, 1f);
        sr.rect(lhX - 14f, lhY, 28f, 8f);

        // 2. Tapered masonry tower (Height: 52f, Base: 22f wide, Top: 14f wide)
        // Base white body
        sr.setColor(0.88f, 0.92f, 0.96f, 1f);
        sr.triangle(lhX - 11f, lhY + 8f, lhX - 7f, lhY + 54f, lhX + 7f, lhY + 54f);
        sr.triangle(lhX - 11f, lhY + 8f, lhX + 7f, lhY + 54f, lhX + 11f, lhY + 8f);

        // Crimson nautical horizontal stripes (Đai đỏ hải quân)
        sr.setColor(0.82f, 0.18f, 0.20f, 1f);
        // Stripe 1 (Lower)
        sr.triangle(lhX - 10f, lhY + 16f, lhX - 9f, lhY + 26f, lhX + 9f, lhY + 26f);
        sr.triangle(lhX - 10f, lhY + 16f, lhX + 9f, lhY + 26f, lhX + 10f, lhY + 16f);
        // Stripe 2 (Upper)
        sr.triangle(lhX - 8.2f, lhY + 36f, lhX - 7.5f, lhY + 46f, lhX + 7.5f, lhY + 46f);
        sr.triangle(lhX - 8.2f, lhY + 36f, lhX + 7.5f, lhY + 46f, lhX + 8.2f, lhY + 36f);

        // Shadow facet on right side of tower (Chiều sâu hình trụ)
        sr.setColor(0.04f, 0.07f, 0.14f, 0.35f);
        sr.triangle(lhX + 2f, lhY + 8f, lhX + 1f, lhY + 54f, lhX + 7f, lhY + 54f);
        sr.triangle(lhX + 2f, lhY + 8f, lhX + 7f, lhY + 54f, lhX + 11f, lhY + 8f);

        // 3. Catwalk Gallery & Iron Railing
        sr.setColor(0.10f, 0.14f, 0.22f, 1f);
        sr.rect(lhX - 10f, lhY + 54f, 20f, 4f);
        sr.rectLine(lhX - 9f, lhY + 58f, lhX + 9f, lhY + 58f, 1.5f);

        // 4. Lantern Room Glass Housing (Phòng đèn kính)
        float lanternY = lhY + 58f;
        sr.setColor(1.0f, 0.95f, 0.65f, 0.95f); // Radiant golden core
        sr.rect(lhX - 5.5f, lanternY, 11f, 10f);

        // 5. Copper Dome Roof & Finial Spire
        sr.setColor(0.08f, 0.12f, 0.20f, 1f);
        sr.triangle(lhX - 7.5f, lanternY + 10f, lhX, lanternY + 18f, lhX + 7.5f, lanternY + 10f);
        // Lightning rod spire
        sr.rectLine(lhX, lanternY + 18f, lhX, lanternY + 26f, 1.6f);
        // Brass ball finial
        sr.setColor(0.95f, 0.82f, 0.35f, 1f);
        sr.circle(lhX, lanternY + 26f, 1.8f);

        // =========================================================================
        // DYNAMIC 360-DEGREE ROTATING LIGHT BEAM (Tia Sáng Hải Đăng Quét Đại Dương)
        // =========================================================================
        float bulbX = lhX;
        float bulbY = lanternY + 5f;

        // Rotation angle
        float rot = timer * 0.55f;
        float cosA = MathUtils.cos(rot);
        float sinA = MathUtils.sin(rot);

        // Conical sweeping beam projection (length: 1100f)
        float beamLen = 1100f;
        float targetX = bulbX + cosA * beamLen;
        float targetY = bulbY + sinA * 260f;

        // Perpendicular spread for conical beam
        float spread = 70f;
        float perpX = -sinA * spread;
        float perpY = cosA * spread;

        // Beam alpha modulated by 3D facing: brighter when sweeping across horizon towards camera
        float facingViewer = -sinA;
        float beamAlpha = MathUtils.clamp(0.07f + 0.09f * facingViewer, 0.02f, 0.16f);

        // Outer soft glow cone
        sr.setColor(0.85f, 0.92f, 1.0f, beamAlpha * 0.45f);
        sr.triangle(bulbX, bulbY, targetX + perpX * 1.5f, targetY + perpY * 1.5f, targetX - perpX * 1.5f, targetY - perpY * 1.5f);

        // Inner concentrated beam cone
        sr.setColor(1.0f, 0.98f, 0.75f, beamAlpha);
        sr.triangle(bulbX, bulbY, targetX + perpX * 0.7f, targetY + perpY * 0.7f, targetX - perpX * 0.7f, targetY - perpY * 0.7f);

        // Core bright beam shaft
        sr.setColor(1.0f, 1.0f, 0.9f, beamAlpha * 1.35f);
        sr.rectLine(bulbX, bulbY, targetX, targetY, 2.2f);

        // Lens Flare Burst when beam sweeps facing directly towards player
        if (facingViewer > 0.65f) {
            float flareIntensity = (facingViewer - 0.65f) / 0.35f; // 0.0 to 1.0
            sr.setColor(1.0f, 0.95f, 0.75f, 0.35f * flareIntensity);
            sr.circle(bulbX, bulbY, 32f * flareIntensity);
            sr.setColor(1.0f, 1.0f, 1.0f, 0.75f * flareIntensity);
            sr.circle(bulbX, bulbY, 12f * flareIntensity);
            // 4-Point cross star flash
            float flSpan = 45f * flareIntensity;
            sr.rectLine(bulbX - flSpan, bulbY, bulbX + flSpan, bulbY, 2.0f);
            sr.rectLine(bulbX, bulbY - flSpan, bulbX, bulbY + flSpan, 2.0f);
        }
    }

    private static void drawMoonlightWaterReflection(ShapeRenderer sr, float timer, float moonX, float horizonY) {
        // Golden-silver shimmering trail from moon onto ocean waves
        for (int row = 0; row < 18; row++) {
            float ry = horizonY - 12f - row * 16f;
            if (ry < 20f) break;

            float width = 24f + row * 16f + MathUtils.sin(timer * 2.5f + row * 0.8f) * 10f;
            float driftX = moonX + MathUtils.sin(timer * 1.5f + row * 0.6f) * 7f;
            float rowAlpha = MathUtils.clamp(0.20f - row * 0.008f, 0.03f, 0.22f);

            sr.setColor(0.85f, 0.95f, 1.0f, rowAlpha);
            sr.rectLine(driftX - width * 0.5f, ry, driftX + width * 0.5f, ry, 2.0f);
        }
    }

    private static void drawOceanWaves(ShapeRenderer sr, float timer, float W, float horizonY) {
        // 4 Tiers of dynamic ocean waves with sinusoidal swell and foam highlights
        // Tier 1: Far horizon gentle swell (Y: horizonY - 15f)
        float t1Y = horizonY - 15f;
        drawWaveCrest(sr, timer * 1.2f, t1Y, W, 22, 5.0f, 0.018f, 0.18f, 0.32f, 0.55f, 0.35f);

        // Tier 2: Mid-distance rolling ocean waves (Y: horizonY - 75f)
        float t2Y = horizonY - 75f;
        drawWaveCrest(sr, timer * 1.6f, t2Y, W, 18, 8.5f, 0.014f, 0.22f, 0.42f, 0.68f, 0.40f);

        // Tier 3: Near oceanic swells (Y: horizonY - 145f)
        float t3Y = horizonY - 145f;
        drawWaveCrest(sr, timer * 2.0f, t3Y, W, 14, 12.0f, 0.010f, 0.28f, 0.52f, 0.78f, 0.45f);

        // Tier 4: Foreground deep swell (Y: horizonY - 225f)
        float t4Y = horizonY - 225f;
        drawWaveCrest(sr, timer * 2.4f, t4Y, W, 10, 15.0f, 0.008f, 0.32f, 0.60f, 0.88f, 0.50f);
    }

    private static void drawWaveCrest(ShapeRenderer sr, float time, float baseY, float W, int segs, float amp, float freq, float r, float g, float b, float a) {
        float segW = W / segs;
        float px = 0f;
        float py = baseY + MathUtils.sin(time + px * freq) * amp;

        for (int i = 1; i <= segs; i++) {
            float nx = i * segW;
            float ny = baseY + MathUtils.sin(time + nx * freq) * amp;

            // Deep wave ridge body
            sr.setColor(r * 0.5f, g * 0.5f, b * 0.5f, a * 0.7f);
            sr.rectLine(px, py - 2f, nx, ny - 2f, 4.0f);

            // Foaming crest rim on the upper edge
            sr.setColor(r, g, b, a);
            sr.rectLine(px, py, nx, ny, 2.0f);

            px = nx;
            py = ny;
        }
    }

    private static void drawCrashingSurf(ShapeRenderer sr, float timer, float horizonY) {
        // Dramatic breakers and foamy wash crashing against basalt sea stacks (X: 730f - 850f)
        float surfX = 780f;
        float surfY = horizonY - 10f;

        // Sea foam froth mound
        sr.setColor(0.85f, 0.95f, 1.0f, 0.42f + 0.12f * MathUtils.sin(timer * 3f));
        sr.circle(surfX - 35f, surfY, 18f, 16);
        sr.circle(surfX, surfY + 3f, 22f, 18);
        sr.circle(surfX + 45f, surfY - 2f, 16f, 16);
        sr.circle(surfX + 85f, surfY - 5f, 14f, 14);

        // Rising sea spray mist droplets
        sr.setColor(0.92f, 0.98f, 1.0f, 0.50f);
        for (int s = 0; s < 12; s++) {
            float spX = surfX - 40f + s * 11f + MathUtils.sin(timer * 4f + s * 1.3f) * 8f;
            float spY = surfY + 8f + ((timer * 35f + s * 14f) % 45f);
            sr.circle(spX, spY, 1.5f + (s % 3) * 0.8f);
        }
    }

    private static void drawPelagicFishSchool(ShapeRenderer sr, float timer, float W) {
        // School of pelagic fish (Tuna/Mackerel) swimming rapidly in mid-depths
        sr.setColor(0.12f, 0.28f, 0.45f, 0.45f);
        for (int f = 0; f < 5; f++) {
            float fx = (f * 48f + timer * 52f) % (W + 240f) - 120f;
            float fy = 295f + (f % 2) * 22f + MathUtils.sin(timer * 2.8f + f * 1.2f) * 10f;
            float wag = MathUtils.sin(timer * 6f + f);

            // Streamlined fish body
            sr.ellipse(fx, fy, 26f, 9f);
            // Forked tail
            sr.triangle(fx - 12f, fy, fx - 22f + wag * 2f, fy + 7f, fx - 22f + wag * 2f, fy - 7f);
        }
    }

    private static void drawBlueMarlin(ShapeRenderer sr, float timer, float W) {
        // Majestic Giant Blue Marlin (Cá Cờ Xanh) slicing through distant oceanic swell
        // Swims gracefully left to right across the mid-ocean
        float marlinX = (timer * 32f + 400f) % (W + 350f) - 150f;
        float marlinY = 255f + MathUtils.sin(timer * 1.6f) * 15f;
        float wag = MathUtils.sin(timer * 4.2f);

        // Deep oceanic blue silhouette with subtle rim lighting
        sr.setColor(0.07f, 0.16f, 0.28f, 0.70f);

        // 1. Aerodynamic Torpedo Body
        sr.ellipse(marlinX, marlinY, 78f, 22f);
        sr.triangle(marlinX + 25f, marlinY + 4f, marlinX + 48f, marlinY, marlinX + 25f, marlinY - 4f);

        // 2. Iconic Spear / Sword Rostrum (Mũi kiếm nhọn dài đặc trưng)
        sr.rectLine(marlinX + 46f, marlinY, marlinX + 86f, marlinY + 1f, 2.2f);
        sr.rectLine(marlinX + 80f, marlinY + 1f, marlinX + 96f, marlinY + 1.2f, 1.2f);

        // 3. Tall Dorsal Sail-Fin (Vây cờ / vây buồm cao vút)
        sr.triangle(marlinX - 10f, marlinY + 8f, marlinX + 6f, marlinY + 34f, marlinX + 22f, marlinY + 7f);
        sr.triangle(marlinX - 22f, marlinY + 6f, marlinX - 10f, marlinY + 8f, marlinX + 6f, marlinY + 34f);

        // 4. Pectoral Fin (Vây ngực)
        sr.triangle(marlinX + 8f, marlinY - 4f, marlinX - 12f, marlinY - 20f, marlinX - 2f, marlinY - 4f);

        // 5. Crescent Sickle Caudal Tail (Đuôi trăng khuyết)
        float tailBaseX = marlinX - 38f;
        float tailBaseY = marlinY;
        sr.rectLine(tailBaseX, tailBaseY, tailBaseX - 14f + wag * 3f, tailBaseY + wag * 4f, 3.5f);
        float finTipX = tailBaseX - 14f + wag * 3f;
        float finTipY = tailBaseY + wag * 4f;
        sr.triangle(finTipX, finTipY, finTipX - 16f, finTipY + 24f, finTipX - 6f, finTipY + 4f);
        sr.triangle(finTipX, finTipY, finTipX - 16f, finTipY - 24f, finTipX - 6f, finTipY - 4f);
    }

    private static void drawSharkFin(ShapeRenderer sr, float timer, float W) {
        // Blacktip reef shark dorsal fin gliding silently across mid-ground waves
        float sharkX = (W + 280f) - ((timer * 24f) % (W + 450f));
        float sharkY = 205f + MathUtils.sin(timer * 0.9f) * 10f;

        // Dark ominous fin silhouette
        sr.setColor(0.035f, 0.075f, 0.150f, 0.85f);
        sr.triangle(sharkX - 14f, sharkY, sharkX - 4f, sharkY + 22f, sharkX + 10f, sharkY);

        // Trailing V-shaped surface wake ripples
        sr.setColor(0.35f, 0.65f, 0.85f, 0.35f);
        sr.rectLine(sharkX - 4f, sharkY, sharkX + 28f, sharkY + 5f, 1.5f);
        sr.rectLine(sharkX - 4f, sharkY, sharkX + 28f, sharkY - 5f, 1.5f);
    }

    private static void drawAbyssalJellyfish(ShapeRenderer sr, float timer, float cx, float cy, float scale, float phase, boolean isCyan) {
        // Bioluminescent deep-sea pulsing jellyfish
        float pulse = 0.88f + 0.12f * MathUtils.sin(timer * 2.6f + phase);
        float bobY = cy + MathUtils.sin(timer * 1.4f + phase) * 20f;

        // Translucent outer bell dome
        if (isCyan) {
            sr.setColor(0.10f, 0.80f, 0.95f, 0.30f);
        } else {
            sr.setColor(0.85f, 0.25f, 0.95f, 0.26f);
        }
        sr.circle(cx, bobY, 26f * scale * pulse, 24);

        // Radiant inner organ core
        if (isCyan) {
            sr.setColor(0.40f, 0.95f, 1.0f, 0.65f);
        } else {
            sr.setColor(1.0f, 0.50f, 0.90f, 0.60f);
        }
        sr.circle(cx, bobY + 3f * scale, 14f * scale * pulse, 18);
        sr.setColor(1f, 1f, 1f, 0.75f);
        sr.circle(cx, bobY + 4f * scale, 5f * scale);

        // Undulating flowing tentacles
        if (isCyan) {
            sr.setColor(0.20f, 0.85f, 1.0f, 0.42f);
        } else {
            sr.setColor(0.90f, 0.40f, 1.0f, 0.38f);
        }

        for (int t = -3; t <= 3; t++) {
            float tentX = cx + t * 5.5f * scale;
            float tentY = bobY - 4f * scale;
            float sway1 = MathUtils.sin(timer * 2.8f + phase + t * 0.7f) * 9f * scale;
            float sway2 = MathUtils.sin(timer * 3.4f + phase + t * 1.1f) * 16f * scale;

            sr.rectLine(tentX, tentY, tentX + sway1 * 0.5f, tentY - 22f * scale, 2.0f * scale);
            sr.rectLine(tentX + sway1 * 0.5f, tentY - 22f * scale, tentX + sway2, tentY - 52f * scale, 1.4f * scale);
        }
    }

    private static void drawBioluminescentPlankton(ShapeRenderer sr, float timer, float W, float horizonY) {
        // Glowing pelagic plankton & sea sparkle motes drifting in the deep currents
        for (int p = 0; p < 36; p++) {
            float px = (p * 37f + timer * 9f) % W;
            float py = ((p * 71f + timer * 24f) % (horizonY - 20f)) + 10f;
            float drift = MathUtils.sin(timer * 1.5f + p * 1.8f) * 14f;
            float pSize = 1.2f + (p % 3) * 1.0f;
            float pAlpha = 0.35f + 0.35f * MathUtils.sin(timer * 2.2f + p);

            if (p % 2 == 0) {
                sr.setColor(0.15f, 0.90f, 0.85f, pAlpha); // Neon cyan-teal
            } else {
                sr.setColor(0.70f, 0.40f, 1.0f, pAlpha); // Violet-amethyst
            }
            sr.circle(px + drift, py, pSize);
        }
    }

    private static void drawAbyssalSeaFloor(ShapeRenderer sr, float W) {
        // Dark abyssal trench seabed contours at the very bottom
        sr.setColor(0.008f, 0.022f, 0.065f, 0.98f);
        sr.rect(0, 0, W, 28f);

        // Rugged abyssal rocky mounts
        sr.setColor(0.012f, 0.035f, 0.090f, 1.0f);
        sr.circle(120f, -15f, 75f, 20);
        sr.circle(380f, -25f, 95f, 22);
        sr.circle(720f, -20f, 85f, 20);
        sr.circle(1080f, -15f, 80f, 20);

        // Faint bioluminescent sea moss accents on seabed mounds
        sr.setColor(0.10f, 0.55f, 0.65f, 0.22f);
        sr.rectLine(90f, 26f, 150f, 28f, 2.0f);
        sr.rectLine(340f, 24f, 410f, 27f, 2.0f);
        sr.rectLine(690f, 25f, 750f, 26f, 2.0f);
        sr.rectLine(1050f, 24f, 1110f, 27f, 2.0f);
    }
}

