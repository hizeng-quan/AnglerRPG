package com.fishingrpg.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.fishingrpg.game.PlayScreen;
import com.fishingrpg.game.entities.Fish;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.loot.Catchable;
import com.fishingrpg.game.systems.FishingManager.State;

import java.util.Map;

public class GameHUD {

    private final ShapeRenderer sr;
    private final BitmapFont font;
    private final BitmapFont titleFont;
    private final GlyphLayout layout;
    private final PlayScreen screen;

    private static final float W = 1280f, H = 720f;
    private static final float HUD_H = 42f;
    private static final float HUD_Y = H - HUD_H;

    // Combat Tension Bar Layout
    private static final float PANEL_W = 46f, PANEL_H = 320f;
    private static final float BAR_INSET = 4f;

    // Top Fish Boss / Combat Bar
    private static final float HP_W = 600f, HP_H = 34f;
    private static final float MSG_BAR_H = 35f;

    // Cast / Action Button
    private static final float BTN_CAST_W = 240f, BTN_CAST_H = 58f;
    private static final float BTN_CAST_X = (W - BTN_CAST_W) / 2f;
    private static final float BTN_CAST_Y = 65f;

    // Back / Menu Button
    private static final float BTN_MENU_X = 20f;
    private static final float BTN_MENU_Y = H - 60f;
    private static final float BTN_MENU_W = 100f, BTN_MENU_H = 40f;

    // Top Navigation Buttons (7 buttons)
    private static final float TOP_BTN_W = 120f, TOP_BTN_H = 65f;
    private static final float TOP_BTN_Y = H - 85f;

    // Combat Skill Slots (Q, W, E, R) + CĂNG DÂY (Space)
    private static final float BTN_ACTIVES_X = 350f;
    private static final float BTN_ACTIVES_Y = 25f;
    private static final float BTN_CANG_X = BTN_ACTIVES_X + 4 * 76f + 12f;
    private static final float BTN_CANG_Y = BTN_ACTIVES_Y;

    // Hover States
    private boolean hoverCast = false;
    private boolean hoverMenu = false;
    private boolean hoverQuest = false;
    private boolean hoverEquipment = false;
    private boolean hoverSkills = false;
    private boolean hoverGacha = false;
    private boolean hoverShop = false;
    private boolean hoverCatalog = false;
    private boolean hoverEvent = false;
    private boolean hoverCang = false;
    private boolean hoverClose = false;
    private final boolean[] hoverActives = new boolean[4];

    private float timer = 0f;
    private String hoveredInventoryItem = null;

    public GameHUD(ShapeRenderer sr, BitmapFont font, PlayScreen screen) {
        this.sr = sr;
        this.font = font;
        this.layout = new GlyphLayout();
        this.screen = screen;
        this.titleFont = (screen.getGame() != null && screen.getGame().titleFont != null) ? screen.getGame().titleFont : font;
    }

    public void updateHover(Vector3 m, State state) {
        hoverCast = (state == State.IDLE || state == State.PRE_CAST || state == State.RESULT) &&
                    m.x >= BTN_CAST_X && m.x <= BTN_CAST_X + BTN_CAST_W &&
                    m.y >= BTN_CAST_Y && m.y <= BTN_CAST_Y + BTN_CAST_H;

        hoverMenu = m.x >= BTN_MENU_X && m.x <= BTN_MENU_X + BTN_MENU_W &&
                    m.y >= BTN_MENU_Y && m.y <= BTN_MENU_Y + BTN_MENU_H;

        hoverQuest     = state == State.IDLE && m.x >= 230 && m.x <= 350 && m.y >= TOP_BTN_Y && m.y <= TOP_BTN_Y + TOP_BTN_H;
        hoverEquipment = state == State.IDLE && m.x >= 370 && m.x <= 490 && m.y >= TOP_BTN_Y && m.y <= TOP_BTN_Y + TOP_BTN_H;
        hoverSkills    = state == State.IDLE && m.x >= 510 && m.x <= 630 && m.y >= TOP_BTN_Y && m.y <= TOP_BTN_Y + TOP_BTN_H;
        hoverGacha     = state == State.IDLE && m.x >= 650 && m.x <= 770 && m.y >= TOP_BTN_Y && m.y <= TOP_BTN_Y + TOP_BTN_H;
        hoverShop      = state == State.IDLE && m.x >= 790 && m.x <= 910 && m.y >= TOP_BTN_Y && m.y <= TOP_BTN_Y + TOP_BTN_H;
        hoverCatalog   = state == State.IDLE && m.x >= 930 && m.x <= 1050 && m.y >= TOP_BTN_Y && m.y <= TOP_BTN_Y + TOP_BTN_H;
        hoverEvent     = state == State.IDLE && m.x >= 1070 && m.x <= 1190 && m.y >= TOP_BTN_Y && m.y <= TOP_BTN_Y + TOP_BTN_H;

        for (int i = 0; i < 4; i++) {
            hoverActives[i] = state == State.COMBAT &&
                              m.x >= BTN_ACTIVES_X + i * 76f && m.x <= BTN_ACTIVES_X + i * 76f + 66f &&
                              m.y >= BTN_ACTIVES_Y && m.y <= BTN_ACTIVES_Y + 66f;
        }

        hoverCang = state == State.COMBAT &&
                    m.x >= BTN_CANG_X && m.x <= BTN_CANG_X + 85f &&
                    m.y >= BTN_CANG_Y && m.y <= BTN_CANG_Y + 66f;

        // Modal Result continue button
        hoverClose = state == State.RESULT && hoverCast;

        hoveredInventoryItem = getClickedInventoryItem(m, state);
    }

    public boolean isCastHovered() { return hoverCast; }
    public boolean isCloseHovered() { return hoverClose; }
    public boolean isActiveSlotHovered(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < 4) return hoverActives[slotIndex];
        return false;
    }
    public boolean isCangHovered() { return hoverCang; }
    public boolean isActionClicked(Vector3 m, State state) {
        if (state == State.IDLE) return hoverCast;
        return hoverCast;
    }
    public boolean isMenuClicked(Vector3 m, State state) { return hoverMenu; }
    public boolean isShopClicked(Vector3 m, State state) { return hoverShop; }
    public boolean isCatalogClicked(Vector3 m, State state) { return hoverCatalog; }
    public boolean isSkillsClicked(Vector3 m, State state) { return hoverSkills; }
    public boolean isQuestClicked(Vector3 m, State state) { return hoverQuest; }
    public boolean isGachaClicked(Vector3 m, State state) { return hoverGacha; }
    public boolean isEquipmentClicked(Vector3 m, State state) { return hoverEquipment; }
    public boolean isEventClicked(Vector3 m, State state) { return hoverEvent; }

    public String getClickedInventoryItem(Vector3 m, State state) {
        float slotSize = 65f;
        float padding = 10f;
        float panelW = 3 * slotSize + 4 * padding;
        float panelH = 3 * slotSize + 4 * padding + 40f;
        float invX = W - panelW - 20f;
        float invY = 20f;

        int i = 0;
        Player player = screen.getPlayer();
        if (player == null) return null;

        for (Map.Entry<String, Integer> entry : player.getInventory().entrySet()) {
            String name = entry.getKey();
            if (entry.getValue() <= 0 || name.equals("Vàng")) continue;
            int row = i / 3;
            int col = i % 3;
            float x = invX + padding + col * (slotSize + padding);
            float y = invY + panelH - 40f - padding - (row + 1) * slotSize - row * padding;

            if (m.x >= x && m.x <= x + slotSize && m.y >= y && m.y <= y + slotSize) {
                return name;
            }
            i++;
        }
        return null;
    }

    // =========================================================================
    // IDLE SCENERY & TOP DOCK
    // =========================================================================

    public void drawIdleShapes(Player player) {
        timer += Gdx.graphics.getDeltaTime();

        // 1. Sky & Sun & Sea Scenery
        drawScenery();

        // 2. Top Navigation Dock Frame
        sr.setColor(0.08f, 0.11f, 0.18f, 0.85f);
        sr.rect(215f, TOP_BTN_Y - 5f, 990f, TOP_BTN_H + 10f);
        sr.setColor(0.85f, 0.65f, 0.25f, 0.5f);
        sr.rect(215f, TOP_BTN_Y + TOP_BTN_H + 3f, 990f, 2f);

        // 3. Top Buttons (7 buttons)
        drawTopButton(0, 230f, hoverQuest,     new Color(0.75f, 0.62f, 0.15f, 1f), new Color(0.9f, 0.75f, 0.25f, 1f));
        drawTopButton(1, 370f, hoverEquipment, new Color(0.25f, 0.48f, 0.78f, 1f), new Color(0.35f, 0.6f, 0.9f, 1f));
        drawTopButton(2, 510f, hoverSkills,    new Color(0.82f, 0.45f, 0.15f, 1f), new Color(0.95f, 0.58f, 0.22f, 1f));
        drawTopButton(3, 650f, hoverGacha,     new Color(0.78f, 0.28f, 0.52f, 1f), new Color(0.92f, 0.38f, 0.65f, 1f));
        drawTopButton(4, 790f, hoverShop,      new Color(0.22f, 0.65f, 0.35f, 1f), new Color(0.32f, 0.78f, 0.45f, 1f));
        drawTopButton(5, 930f, hoverCatalog,   new Color(0.55f, 0.28f, 0.78f, 1f), new Color(0.68f, 0.38f, 0.92f, 1f));
        drawTopButton(6, 1070f, hoverEvent,    new Color(0.82f, 0.18f, 0.22f, 1f), new Color(0.95f, 0.28f, 0.32f, 1f));

        // Top button icons
        drawTopButtonIcons();

        // Notification Badges on Quest and Event
        drawTopNotificationBadges(player);

        // 4. Primary "RA KHƠI CÂU CÁ" button in IDLE
        drawCastButton("RA KHƠI CÂU CÁ");

        // 5. Back to Menu Button
        drawMenuBtn();

        // 6. Player Profile & Resource Dashboard (Bottom Left)
        drawPlayerProfileCardShapes(player);

        // 7. Quick Slots Inventory (Bottom Right)
        drawInventoryShapes(player);
    }

    private void drawScenery() {
        // Sky gradient
        sr.setColor(0.35f, 0.10f, 0.35f, 1f); sr.rect(0, 500, W, 220);
        sr.setColor(0.75f, 0.20f, 0.30f, 1f); sr.rect(0, 420, W, 80);
        sr.setColor(0.95f, 0.45f, 0.15f, 1f); sr.rect(0, 350, W, 70);

        // Sun
        float sunY = 340f + 10f * MathUtils.sin(timer * 0.5f);
        sr.setColor(1f, 0.45f, 0.15f, 0.9f); sr.circle(W / 2f, sunY, 60f, 40);
        sr.setColor(1f, 0.85f, 0.20f, 0.5f); sr.circle(W / 2f, sunY, 75f + 5f * MathUtils.sin(timer * 2f), 40);

        // Water
        sr.setColor(0.08f, 0.12f, 0.22f, 1f); sr.rect(0, 0, W, 350);
        sr.setColor(0.12f, 0.16f, 0.28f, 0.8f); sr.rect(0, 0, W, 280);

        // Clouds
        sr.setColor(0.85f, 0.65f, 0.55f, 0.4f);
        for (int j = 0; j < 3; j++) {
            float cloudX = (timer * 15f + j * 300) % (W + 200) - 100;
            float cloudY = 440 + j * 30;
            sr.circle(cloudX, cloudY, 25f + j * 5);
            sr.circle(cloudX + 30, cloudY + 10, 35f);
            sr.circle(cloudX + 60, cloudY - 5, 20f);
        }

        sr.end();
        sr.begin(ShapeRenderer.ShapeType.Line);
        // Seagulls
        sr.setColor(0.15f, 0.10f, 0.10f, 0.6f);
        for (int j = 0; j < 4; j++) {
            float birdX = W - ((timer * 40f + j * 200) % (W + 200)) + 100;
            float birdY = 420 + 20f * MathUtils.sin(timer * 2f + j);
            float wingY = birdY + 12f * MathUtils.sin(timer * 8f + j);
            sr.line(birdX, birdY, birdX + 10, wingY);
            sr.line(birdX + 10, wingY, birdX + 20, birdY);
        }
        // Waves
        float step = 5f;
        sr.setColor(0.12f, 0.38f, 0.60f, 0.85f);
        for (float x = 0; x < W - step; x += step) {
            float y1 = 328 + 9f * MathUtils.sin((x       + timer * 55f) * 0.022f);
            float y2 = 328 + 9f * MathUtils.sin((x + step + timer * 55f) * 0.022f);
            sr.line(x, y1, x + step, y2);
        }
        sr.setColor(0.08f, 0.28f, 0.46f, 0.65f);
        for (float x = 0; x < W - step; x += step) {
            float y1 = 305 + 6f * MathUtils.sin((x       + timer * 38f + 25) * 0.018f);
            float y2 = 305 + 6f * MathUtils.sin((x + step + timer * 38f + 25) * 0.018f);
            sr.line(x, y1, x + step, y2);
        }
        sr.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Fishing boat on right
        float boatY = 320f + 5f * MathUtils.sin(timer * 2.5f);
        sr.setColor(0.15f, 0.10f, 0.05f, 1f);
        sr.rect(900, boatY, 300, 50);
        sr.triangle(800, boatY + 50, 900, boatY + 50, 900, boatY);
        sr.triangle(1200, boatY + 50, 1260, boatY + 50, 1200, boatY);
        sr.setColor(0.7f, 0.65f, 0.6f, 1f);
        sr.rect(1000, boatY + 50, 120, 70);
        sr.setColor(0.2f, 0.3f, 0.4f, 1f);
        sr.rect(990, boatY + 120, 140, 16);
        sr.setColor(0.8f, 0.9f, 1f, 0.6f);
        sr.rect(1020, boatY + 70, 30, 30);
        sr.setColor(0.1f, 0.08f, 0.05f, 1f);
        sr.rect(940, boatY + 50, 10, 160);
        sr.setColor(0.8f, 0.2f, 0.2f, 1f);
        sr.triangle(950, boatY + 200, 950, boatY + 170, 1010, boatY + 184);

        // Dock Platform
        float dockYOffset = 3f * MathUtils.sin(timer * 0.8f);
        sr.setColor(0.25f, 0.15f, 0.1f, 1f);
        sr.rect(0, dockYOffset, 280, 280);
        sr.setColor(0.35f, 0.22f, 0.15f, 1f);
        sr.rect(0, 270 + dockYOffset, 290, 15);
        sr.setColor(0.2f, 0.12f, 0.08f, 1f);
        sr.rect(60, 0, 20, 270 + dockYOffset);
        sr.rect(160, 0, 20, 270 + dockYOffset);
        sr.rect(250, 0, 20, 270 + dockYOffset);
    }

    private void drawTopButton(int index, float x, boolean hover, Color baseCol, Color hoverCol) {
        sr.setColor(hover ? hoverCol : baseCol);
        sr.rect(x, TOP_BTN_Y, TOP_BTN_W, TOP_BTN_H);
        sr.setColor(1f, 0.95f, 0.6f, hover ? 0.9f : 0.5f);
        sr.rect(x, TOP_BTN_Y + TOP_BTN_H - 4f, TOP_BTN_W, 4f); // Top highlight rim
        sr.setColor(0f, 0f, 0f, 0.35f);
        sr.rect(x, TOP_BTN_Y, TOP_BTN_W, 4f); // Bottom shadow
    }

    private void drawTopButtonIcons() {
        // 0: Quest (Diamond)
        sr.setColor(1f, 0.9f, 0.5f, 1f);
        sr.rect(290 - 6, H - 45 - 6, 6, 6, 12, 12, 1f, 1f, 45f);

        // 1: Equipment (Sword)
        sr.setColor(0.8f, 0.92f, 1f, 1f);
        sr.rect(430 - 3, H - 55, 6, 20);
        sr.triangle(425, H - 40, 435, H - 40, 430, H - 30);

        // 2: Skills (Thunderbolt)
        sr.setColor(1f, 1f, 0.55f, 1f);
        sr.triangle(570, H - 50, 575, H - 40, 565, H - 35);
        sr.triangle(570, H - 30, 565, H - 40, 575, H - 45);

        // 3: Gacha (Chest box)
        sr.setColor(1f, 0.8f, 0.9f, 1f);
        sr.rect(710 - 8, H - 50, 16, 16);
        sr.setColor(0.8f, 0.3f, 0.5f, 1f);
        sr.rect(710 - 2, H - 50, 4, 16);
        sr.rect(710 - 8, H - 44, 16, 4);

        // 4: Shop (Gold Coin)
        sr.setColor(1f, 0.9f, 0.35f, 1f);
        sr.circle(850, H - 42, 8);
        sr.setColor(0.9f, 0.7f, 0.1f, 1f);
        sr.circle(850, H - 42, 5);

        // 5: Catalog (Book)
        sr.setColor(0.95f, 0.85f, 0.75f, 1f);
        sr.rect(990 - 10, H - 50, 20, 15);
        sr.setColor(0.6f, 0.3f, 0.8f, 1f);
        sr.rectLine(990, H - 50, 990, H - 35, 2f);

        // 6: Event (Star Festival Emblem)
        sr.setColor(1f, 0.88f, 0.25f, 1f);
        sr.rect(1130 - 6, H - 45 - 6, 6, 6, 12, 12, 1f, 1f, 45f);
        sr.setColor(0.95f, 0.25f, 0.2f, 1f);
        sr.circle(1130, H - 45, 3.5f);
    }

    private void drawTopNotificationBadges(Player player) {
        if (player == null) return;

        // Event notification dot
        if (player.getEventManager() != null && player.getEventManager().hasNotification()) {
            float pulse = 0.85f + 0.15f * MathUtils.sin(timer * 6f);
            sr.setColor(1f, 0.2f, 0.2f, pulse);
            sr.circle(1070 + 106, TOP_BTN_Y + TOP_BTN_H - 12f, 6.5f);
            sr.setColor(1f, 0.95f, 0.2f, pulse);
            sr.circle(1070 + 106, TOP_BTN_Y + TOP_BTN_H - 12f, 3.5f);
        }

        // Quest notification dot
        if (player.getQuestManager() != null && player.getQuestManager().hasAnyClaimable()) {
            float pulse = 0.85f + 0.15f * MathUtils.sin(timer * 6f);
            sr.setColor(1f, 0.2f, 0.2f, pulse);
            sr.circle(230 + 106, TOP_BTN_Y + TOP_BTN_H - 12f, 6.5f);
            sr.setColor(1f, 0.95f, 0.2f, pulse);
            sr.circle(230 + 106, TOP_BTN_Y + TOP_BTN_H - 12f, 3.5f);
        }
    }

    private void drawCastButton(String label) {
        float pulse = (screen.fishingManager.state == State.IDLE) ? (0.92f + 0.08f * MathUtils.sin(timer * 4f)) : 1f;

        sr.setColor(hoverCast ? new Color(1f * pulse, 0.55f * pulse, 0.15f * pulse, 0.98f)
                              : new Color(0.85f * pulse, 0.38f * pulse, 0.12f * pulse, 0.92f));
        sr.rect(BTN_CAST_X, BTN_CAST_Y, BTN_CAST_W, BTN_CAST_H);

        // Top glossy highlight
        sr.setColor(1f, 0.9f, 0.35f, 0.8f);
        sr.rect(BTN_CAST_X, BTN_CAST_Y + BTN_CAST_H - 4f, BTN_CAST_W, 4f);

        // Outer glow frame
        sr.setColor(1f, 0.85f, 0.4f, hoverCast ? 0.9f : 0.4f);
        drawBoxBorder(BTN_CAST_X, BTN_CAST_Y, BTN_CAST_W, BTN_CAST_H, 2f);
    }

    private void drawMenuBtn() {
        sr.setColor(hoverMenu ? new Color(0.35f, 0.22f, 0.25f, 0.98f) : new Color(0.20f, 0.15f, 0.18f, 0.92f));
        sr.rect(BTN_MENU_X, BTN_MENU_Y, BTN_MENU_W, BTN_MENU_H);
        sr.setColor(hoverMenu ? new Color(0.95f, 0.45f, 0.5f, 0.9f) : new Color(0.5f, 0.35f, 0.4f, 0.6f));
        drawBoxBorder(BTN_MENU_X, BTN_MENU_Y, BTN_MENU_W, BTN_MENU_H, 1.5f);
    }

    // =========================================================================
    // PLAYER DASHBOARD & RESOURCE BAR (BOTTOM LEFT)
    // =========================================================================

    private void drawPlayerProfileCardShapes(Player player) {
        if (player == null) return;

        float panelX = 20f;
        float panelY = 15f;
        float panelW = 485f;
        float panelH = 88f;

        // Glassmorphism Card Frame
        sr.setColor(0.09f, 0.12f, 0.18f, 0.92f);
        sr.rect(panelX, panelY, panelW, panelH);
        sr.setColor(0.85f, 0.65f, 0.25f, 0.65f);
        drawBoxBorder(panelX, panelY, panelW, panelH, 1.8f);

        // Level Badge Shield (Left side)
        float shieldX = panelX + 38f;
        float shieldY = panelY + panelH / 2f;
        sr.setColor(0.18f, 0.24f, 0.36f, 1f);
        sr.circle(shieldX, shieldY, 28f);
        sr.setColor(1f, 0.85f, 0.3f, 1f);
        sr.circle(shieldX, shieldY, 25f);
        sr.setColor(0.12f, 0.16f, 0.25f, 1f);
        sr.circle(shieldX, shieldY, 22f);

        // XP Progress Bar (Center)
        float barX = panelX + 78f;
        float barY = panelY + 16f;
        float barW = 220f;
        float barH = 15f;

        sr.setColor(0.06f, 0.08f, 0.12f, 0.95f);
        sr.rect(barX, barY, barW, barH);

        float xpRatio = MathUtils.clamp((float) player.getCurrentXp() / Math.max(1, player.getXpForCurrentLevel()), 0f, 1f);
        sr.setColor(0.22f, 0.65f, 1f, 0.95f);
        sr.rect(barX, barY, barW * xpRatio, barH);
        sr.setColor(0.4f, 0.75f, 1f, 0.7f);
        drawBoxBorder(barX, barY, barW, barH, 1.2f);

        // Resource Boxes (Right Column)
        // Gold Pill
        float goldX = panelX + 315f;
        float goldY = panelY + 48f;
        float resW = 155f;
        float resH = 28f;
        sr.setColor(0.22f, 0.18f, 0.08f, 0.92f);
        sr.rect(goldX, goldY, resW, resH);
        sr.setColor(1f, 0.85f, 0.25f, 0.75f);
        drawBoxBorder(goldX, goldY, resW, resH, 1.2f);
        // Gold Coin Icon
        sr.setColor(1f, 0.85f, 0.2f, 1f);
        sr.circle(goldX + 16f, goldY + resH / 2f, 8f);
        sr.setColor(0.85f, 0.65f, 0.1f, 1f);
        sr.circle(goldX + 16f, goldY + resH / 2f, 5f);

        // Diamond Pill
        float diaY = panelY + 14f;
        sr.setColor(0.16f, 0.10f, 0.24f, 0.92f);
        sr.rect(goldX, diaY, resW, resH);
        sr.setColor(0.75f, 0.45f, 0.95f, 0.75f);
        drawBoxBorder(goldX, diaY, resW, resH, 1.2f);
        // Diamond Icon (Diamond shape)
        sr.setColor(0.6f, 0.9f, 1f, 1f);
        sr.rect(goldX + 16f - 5f, diaY + resH / 2f - 5f, 5f, 5f, 10f, 10f, 1f, 1f, 45f);

        // Active Bait Buff Banner if active
        if (player.isBaitBuffActive()) {
            float buffY = panelY + panelH + 8f;
            sr.setColor(0.12f, 0.26f, 0.16f, 0.95f);
            sr.rect(panelX, buffY, 240f, 26f);
            sr.setColor(0.35f, 0.9f, 0.45f, 0.85f);
            drawBoxBorder(panelX, buffY, 240f, 26f, 1.2f);
        }
    }

    public void drawIdleText(SpriteBatch batch, Player player) {
        // Primary Action Button text
        font.setColor(Color.WHITE);
        drawCenteredText(font, "RA KHƠI CÂU CÁ", BTN_CAST_X, BTN_CAST_Y, BTN_CAST_W, BTN_CAST_H);

        // Menu Back Button
        drawCenteredText(font, "MENU", BTN_MENU_X, BTN_MENU_Y, BTN_MENU_W, BTN_MENU_H);

        // 7 Top Buttons Text
        String[] topNames = {"NHIỆM VỤ", "TRANG BỊ", "KỸ NĂNG", "GACHA", "CỬA HÀNG", "SỔ TAY", "SỰ KIỆN"};
        float[] topXs = {230f, 370f, 510f, 650f, 790f, 930f, 1070f};

        font.getData().setScale(0.85f);
        for (int i = 0; i < 7; i++) {
            layout.setText(font, topNames[i]);
            font.draw(batch, topNames[i], topXs[i] + (TOP_BTN_W - layout.width) / 2f, TOP_BTN_Y + 18f);
        }
        font.getData().setScale(1f);

        // Player Dashboard text
        if (player != null) {
            float panelX = 20f;
            float panelY = 15f;

            // Level text inside Shield
            font.setColor(1f, 0.92f, 0.4f, 1f);
            layout.setText(font, "Lv." + player.getLevel());
            font.draw(batch, "Lv." + player.getLevel(), panelX + 38f - layout.width / 2f, panelY + 49f);

            // Current Map Name (with overflow guard)
            font.setColor(0.4f, 0.85f, 1f, 1f);
            String mapName = "Khu vực: " + getMapDisplayName(player.getCurrentMap());
            layout.setText(font, mapName);
            if (layout.width > 220f) {
                font.getData().setScale(220f / layout.width);
            }
            font.draw(batch, mapName, panelX + 78f, panelY + 70f);
            font.getData().setScale(1f);

            // XP String
            font.setColor(Color.WHITE);
            String xpText = "KN: " + player.getCurrentXp() + " / " + player.getXpForCurrentLevel();
            font.getData().setScale(0.85f);
            font.draw(batch, xpText, panelX + 82f, panelY + 28f);
            font.getData().setScale(1f);

            // Gold & Diamonds amounts (with overflow guard)
            float goldX = panelX + 315f;
            float resW = 155f;
            font.setColor(1f, 0.88f, 0.25f, 1f);
            String goldText = player.getGold() + " Vàng";
            layout.setText(font, goldText);
            if (layout.width > resW - 40f) {
                font.getData().setScale((resW - 40f) / layout.width);
            }
            font.draw(batch, goldText, goldX + 32f, panelY + 67f);
            font.getData().setScale(1f);

            font.setColor(0.85f, 0.75f, 1f, 1f);
            String diaText = player.getDiamonds() + " KC";
            layout.setText(font, diaText);
            if (layout.width > resW - 40f) {
                font.getData().setScale((resW - 40f) / layout.width);
            }
            font.draw(batch, diaText, goldX + 32f, panelY + 33f);
            font.getData().setScale(1f);

            // Bait Buff string
            if (player.isBaitBuffActive()) {
                font.setColor(0.4f, 1f, 0.5f, 1f);
                font.draw(batch, "BUFF: MỒI THƠM (+TỐC ĐỘ CẮN)", panelX + 12f, panelY + 88f + 25f);
            }
        }
    }

    private String getMapDisplayName(String mapId) {
        if (mapId == null) return "Ao Làng";
        if (mapId.equals("ao_lang")) return "Ao Làng";
        if (mapId.equals("bai_bien")) return "Bờ Biển";
        if (mapId.equals("dao_xa")) return "Đảo Xa Vực Thẳm";
        return mapId;
    }

    // =========================================================================
    // QUICK SLOTS INVENTORY (BOTTOM RIGHT)
    // =========================================================================

    public void drawInventoryShapes(Player player) {
        if (player == null) return;
        float slotSize = 65f;
        float padding = 10f;
        float panelW = 3 * slotSize + 4 * padding;
        float panelH = 3 * slotSize + 4 * padding + 40f;
        float invX = W - panelW - 20f;
        float invY = 20f;

        // Panel background
        sr.setColor(0.09f, 0.12f, 0.18f, 0.92f);
        sr.rect(invX, invY, panelW, panelH);
        sr.setColor(0.35f, 0.45f, 0.6f, 0.65f);
        drawBoxBorder(invX, invY, panelW, panelH, 1.5f);

        // Header bar
        sr.setColor(0.14f, 0.18f, 0.28f, 0.9f);
        sr.rect(invX, invY + panelH - 32f, panelW, 32f);

        // Draw empty slots for visual
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                float x = invX + padding + col * (slotSize + padding);
                float y = invY + panelH - 40f - padding - (row + 1) * slotSize - row * padding;
                sr.setColor(0.12f, 0.15f, 0.22f, 0.9f);
                sr.rect(x, y, slotSize, slotSize);
                sr.setColor(0.25f, 0.32f, 0.45f, 0.7f);
                drawBoxBorder(x, y, slotSize, slotSize, 1.2f);
            }
        }

        // Draw filled item slots
        int i = 0;
        for (Map.Entry<String, Integer> entry : player.getInventory().entrySet()) {
            String name = entry.getKey();
            if (entry.getValue() <= 0 || name.equals("Vàng")) continue;
            int row = i / 3;
            int col = i % 3;
            float x = invX + padding + col * (slotSize + padding);
            float y = invY + panelH - 40f - padding - (row + 1) * slotSize - row * padding;

            sr.setColor(0.2f, 0.35f, 0.55f, 0.85f);
            sr.rect(x + 2, y + 2, slotSize - 4, slotSize - 4);
            sr.setColor(0.45f, 0.75f, 1f, 0.8f);
            drawBoxBorder(x + 2, y + 2, slotSize - 4, slotSize - 4, 1.2f);
            i++;
        }
    }

    public void drawInventoryText(SpriteBatch batch, Player player) {
        if (player == null) return;
        float slotSize = 65f;
        float padding = 10f;
        float panelW = 3 * slotSize + 4 * padding;
        float panelH = 3 * slotSize + 4 * padding + 40f;
        float invX = W - panelW - 20f;
        float invY = 20f;

        font.setColor(1f, 0.85f, 0.3f, 1f);
        drawCenteredText(font, "PHÍM TẮT (1-9)", invX, invY + panelH - 32f, panelW, 32f);

        int i = 0;
        for (Map.Entry<String, Integer> entry : player.getInventory().entrySet()) {
            String name = entry.getKey();
            if (entry.getValue() <= 0 || name.equals("Vàng")) continue;
            int row = i / 3;
            int col = i % 3;
            float x = invX + padding + col * (slotSize + padding);
            float y = invY + panelH - 40f - padding - (row + 1) * slotSize - row * padding;

            font.setColor(Color.WHITE);
            String dispName = name;
            if (dispName.length() > 6) dispName = dispName.substring(0, 5) + ".";
            font.getData().setScale(0.75f);
            font.draw(batch, dispName, x + 3f, y + 55f);

            font.setColor(1f, 0.9f, 0.2f, 1f);
            font.draw(batch, "x" + entry.getValue(), x + 5f, y + 20f);

            font.setColor(0.5f, 0.85f, 1f, 1f);
            font.draw(batch, "[" + (i + 1) + "]", x + 40f, y + 20f);
            font.getData().setScale(1f);
            i++;
        }

        // Tooltip for hovered inventory item
        if (hoveredInventoryItem != null) {
            com.fishingrpg.game.entities.ItemData itemData = com.fishingrpg.game.data.ItemDatabase.getItem(hoveredInventoryItem);
            if (itemData != null) {
                float tipW = 240f;
                float tipH = 90f;
                Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                screen.getViewport().unproject(mouse);
                float tipX = mouse.x - tipW - 10f;
                if (tipX < 0) tipX = mouse.x + 10f;
                float tipY = mouse.y;
                if (tipY + tipH > H) tipY = H - tipH - 10f;

                batch.end();
                Gdx.gl.glEnable(GL20.GL_BLEND);
                sr.begin(ShapeRenderer.ShapeType.Filled);
                sr.setColor(0.08f, 0.11f, 0.18f, 0.95f);
                sr.rect(tipX, tipY, tipW, tipH);
                sr.setColor(0.85f, 0.65f, 0.25f, 0.85f);
                drawBoxBorder(tipX, tipY, tipW, tipH, 1.5f);
                sr.end();
                Gdx.gl.glDisable(GL20.GL_BLEND);
                batch.begin();

                font.setColor(1f, 0.88f, 0.3f, 1f);
                font.draw(batch, itemData.name, tipX + 10, tipY + 72);
                font.setColor(0.85f, 0.88f, 0.95f, 1f);
                font.getData().setScale(0.8f);
                font.draw(batch, itemData.description, tipX + 10, tipY + 48);

                String usageText = "";
                if (itemData.usageType == com.fishingrpg.game.entities.ItemUsageType.IN_COMBAT) usageText = "• Chỉ dùng lúc giằng co";
                else if (itemData.usageType == com.fishingrpg.game.entities.ItemUsageType.PRE_COMBAT) usageText = "• Dùng trước khi quăng cần";
                else usageText = "• Dùng bất kỳ lúc nào";

                font.setColor(0.4f, 0.9f, 0.5f, 1f);
                font.draw(batch, usageText, tipX + 10, tipY + 24);
                font.getData().setScale(1f);
            }
        }
    }

    // =========================================================================
    // COMBAT & CASTING HUD (SHAPES)
    // =========================================================================

    public void drawShapes(Player player, Fish fish) {
        // 1. Top Fish HP Bar
        if (fish != null && screen.fishingManager.state == State.COMBAT) {
            float hpBarX = (W - HP_W) / 2f;
            float hpBarY = H - 65f;

            // Frame backing
            sr.setColor(0.08f, 0.10f, 0.16f, 0.95f);
            sr.rect(hpBarX, hpBarY, HP_W, HP_H);

            // Empty Track
            sr.setColor(0.20f, 0.05f, 0.05f, 1f);
            sr.rect(hpBarX + 3f, hpBarY + 3f, HP_W - 6f, HP_H - 6f);

            // HP Fill (Gradient from Crimson to Ruby Red)
            float ratio = MathUtils.clamp(fish.getCurrentHp() / Math.max(1f, fish.getMaxHp()), 0f, 1f);
            sr.setColor(0.95f, 0.22f, 0.22f, 0.95f);
            sr.rect(hpBarX + 3f, hpBarY + 3f, (HP_W - 6f) * ratio, HP_H - 6f);

            // Gold Outer Border
            sr.setColor(1f, 0.82f, 0.25f, 0.85f);
            drawBoxBorder(hpBarX, hpBarY, HP_W, HP_H, 1.8f);
        }

        // 2. Vertical Tension Gauge (Left Side)
        float tensionX = 35f;
        float tensionY = H / 2f - PANEL_H / 2f;

        if (screen.fishingManager.fishQuayBunTimer > 0) {
            // Squid Ink / Blinded Gauge
            sr.setColor(0.05f, 0.02f, 0.06f, 0.95f);
            sr.rect(tensionX, tensionY, PANEL_W, PANEL_H);
            sr.setColor(0.4f, 0.1f, 0.5f, 0.8f);
            drawBoxBorder(tensionX, tensionY, PANEL_W, PANEL_H, 2f);
        } else {
            // Gauge Frame
            sr.setColor(0.08f, 0.10f, 0.16f, 0.95f);
            sr.rect(tensionX, tensionY, PANEL_W, PANEL_H);

            float inH = PANEL_H - BAR_INSET * 2f;
            float barX = tensionX + BAR_INSET;
            float barY = tensionY + BAR_INSET;
            float barW = PANEL_W - BAR_INSET * 2f;

            // Empty Track
            sr.setColor(0.12f, 0.14f, 0.18f, 1f);
            sr.rect(barX, barY, barW, inH);

            // Colored Zone Stripes on Track (Safe, Warning, Danger)
            sr.setColor(0.2f, 0.8f, 0.3f, 0.2f);  sr.rect(barX, barY, barW, inH * 0.5f);
            sr.setColor(1f, 0.8f, 0.2f, 0.2f);    sr.rect(barX, barY + inH * 0.5f, barW, inH * 0.35f);
            sr.setColor(1f, 0.2f, 0.2f, 0.25f);   sr.rect(barX, barY + inH * 0.85f, barW, inH * 0.15f);

            // Tension Fill
            float tRatio = MathUtils.clamp(player.getCurrentLineTension() / Math.max(1f, player.getMaxLineTension()), 0f, 1f);
            if (tRatio > 0.85f) {
                float pulse = 0.85f + 0.15f * MathUtils.sin(timer * 10f);
                sr.setColor(1f, 0.2f * pulse, 0.2f * pulse, 1f); // Danger Flashing Red!
            } else if (tRatio > 0.50f) {
                sr.setColor(1f, 0.75f, 0.2f, 1f); // Caution Amber
            } else {
                sr.setColor(0.25f, 0.85f, 0.45f, 1f); // Safe Green
            }
            sr.rect(barX, barY, barW, Math.max(0, tRatio * inH));

            // Tension Needle Pointer
            float tY = barY + tRatio * inH;
            sr.setColor(1f, 1f, 1f, 1f);
            sr.rect(barX - 4f, tY - 3f, barW + 8f, 6f);
            sr.setColor(1f, 0.85f, 0.3f, 1f);
            sr.triangle(barX + barW + 4f, tY, barX + barW + 16f, tY + 6f, barX + barW + 16f, tY - 6f);

            // Outer Gauge Border
            sr.setColor(tRatio > 0.85f ? new Color(1f, 0.25f, 0.25f, 1f) : new Color(0.45f, 0.55f, 0.75f, 0.8f));
            drawBoxBorder(tensionX, tensionY, PANEL_W, PANEL_H, 1.8f);
        }

        // 3. Player Stamina Bar
        float stmW = 240f;
        float stmH = 22f;
        float stmX = 35f;
        float stmY = H / 2f - PANEL_H / 2f - 40f;

        sr.setColor(0.08f, 0.10f, 0.16f, 0.95f);
        sr.rect(stmX, stmY, stmW, stmH);
        float stmRatio = MathUtils.clamp(player.getCurrentStamina() / Math.max(1f, player.getMaxStamina()), 0f, 1f);
        sr.setColor(stmRatio < 0.25f ? new Color(0.95f, 0.25f, 0.25f, 1f) : new Color(0.25f, 0.85f, 0.45f, 1f));
        sr.rect(stmX + 2f, stmY + 2f, (stmW - 4f) * stmRatio, stmH - 4f);
        sr.setColor(0.35f, 0.55f, 0.85f, 0.8f);
        drawBoxBorder(stmX, stmY, stmW, stmH, 1.5f);

        // 4. Casting Meter Gauge (CASTING State)
        if (screen.fishingManager.state == State.CASTING) {
            float castW = 440f;
            float castH = 34f;
            float castX = (W - castW) / 2f;
            float castY = H / 2f - 20f;

            // Frame backing
            sr.setColor(0.08f, 0.11f, 0.18f, 0.95f);
            sr.rect(castX - 6f, castY - 6f, castW + 12f, castH + 12f);

            // Miss Area (Red)
            sr.setColor(0.75f, 0.22f, 0.22f, 1f);
            sr.rect(castX, castY, castW, castH);

            // Good Area (Yellow)
            float goodW = castW * 0.45f;
            sr.setColor(0.92f, 0.75f, 0.18f, 1f);
            sr.rect(castX + (castW - goodW) / 2f, castY, goodW, castH);

            // Perfect Area (Emerald / Gold)
            float perfectW = castW * 0.12f;
            if (player.isSkillEquipped("mat_dai_bang")) {
                perfectW *= player.getSkillTree().getMatDaiBangBonus();
            }
            sr.setColor(0.2f, 0.9f, 0.4f, 1f);
            sr.rect(castX + (castW - perfectW) / 2f, castY, perfectW, castH);

            // High-tech Cursor Needle
            float cursorRatio = screen.fishingManager.castCursorX / 200f;
            float cursorPixelX = castX + cursorRatio * castW;

            sr.setColor(1f, 1f, 1f, 1f);
            sr.rect(cursorPixelX - 4f, castY - 8f, 8f, castH + 16f);
            sr.setColor(0.2f, 0.75f, 1f, 1f);
            sr.rect(cursorPixelX - 2f, castY - 8f, 4f, castH + 16f);

            sr.setColor(0.85f, 0.65f, 0.25f, 0.8f);
            drawBoxBorder(castX - 6f, castY - 6f, castW + 12f, castH + 12f, 1.8f);
        }

        // 5. Active Skill Buttons (Q, W, E, R) & CĂNG DÂY in COMBAT
        if (screen.fishingManager.state == State.COMBAT) {
            String[] actives = player.getEquippedActives();
            for (int i = 0; i < 4; i++) {
                float x = BTN_ACTIVES_X + i * 76f;
                float y = BTN_ACTIVES_Y;
                float cd = (actives[i] != null) ? getCooldown(actives[i]) : 0f;

                if (cd > 0) {
                    sr.setColor(0.18f, 0.10f, 0.12f, 0.92f);
                } else {
                    sr.setColor(hoverActives[i] ? new Color(0.85f, 0.38f, 0.15f, 1f) : new Color(0.65f, 0.25f, 0.12f, 0.95f));
                }
                sr.rect(x, y, 66f, 66f);
                sr.setColor(cd > 0 ? new Color(0.4f, 0.3f, 0.35f, 0.6f) : new Color(1f, 0.85f, 0.4f, 0.85f));
                drawBoxBorder(x, y, 66f, 66f, 1.8f);
            }

            // CĂNG DÂY (Spacebar) Button
            sr.setColor(hoverCang ? new Color(0.2f, 0.7f, 0.95f, 1f) : new Color(0.12f, 0.45f, 0.75f, 0.95f));
            sr.rect(BTN_CANG_X, BTN_CANG_Y, 85f, 66f);
            sr.setColor(1f, 0.85f, 0.4f, 0.85f);
            drawBoxBorder(BTN_CANG_X, BTN_CANG_Y, 85f, 66f, 1.8f);
        }

        // 6. Action / Result Primary Button
        if (screen.fishingManager.state == State.RESULT || screen.fishingManager.state == State.PRE_CAST) {
            drawCastButton(screen.fishingManager.state == State.RESULT ? "TIẾP TỤC CÂU" : "QUĂNG CẦN");
        }

        // 7. Epic Result Modal Frame
        if (screen.fishingManager.state == State.RESULT) {
            drawResultModalShapes();
        }

        // 8. Screen Ink Overlay Skills
        drawInkOverlays();

        // 9. Back Menu button & Inventory
        drawMenuBtn();
        drawInventoryShapes(player);
    }

    private void drawResultModalShapes() {
        float modalW = 560f;
        float modalH = 390f;
        float modalX = (W - modalW) / 2f;
        float modalY = (H - modalH) / 2f + 25f;

        boolean success = screen.fishingManager.lastEncounterSuccess;

        // Modal backing
        sr.setColor(0.08f, 0.11f, 0.18f, 0.97f);
        sr.rect(modalX, modalY, modalW, modalH);

        // Header Ribbon
        float ribbonH = 55f;
        float ribbonY = modalY + modalH - ribbonH;
        if (success) {
            sr.rect(modalX, ribbonY, modalW, ribbonH,
                    new Color(0.25f, 0.18f, 0.08f, 1f),
                    new Color(0.25f, 0.18f, 0.08f, 1f),
                    new Color(0.45f, 0.32f, 0.10f, 1f),
                    new Color(0.45f, 0.32f, 0.10f, 1f));
            sr.setColor(1f, 0.85f, 0.35f, 1f);
            sr.rect(modalX, ribbonY, modalW, 2.5f);
        } else {
            sr.setColor(0.35f, 0.10f, 0.12f, 1f);
            sr.rect(modalX, ribbonY, modalW, ribbonH);
            sr.setColor(0.9f, 0.25f, 0.25f, 1f);
            sr.rect(modalX, ribbonY, modalW, 2.5f);
        }

        // Rewards / Specs Card Inner Box
        float boxX = modalX + 25f;
        float boxY = modalY + 65f;
        float boxW = modalW - 50f;
        float boxH = 145f;
        sr.setColor(0.12f, 0.16f, 0.25f, 0.9f);
        sr.rect(boxX, boxY, boxW, boxH);
        sr.setColor(0.3f, 0.4f, 0.55f, 0.6f);
        drawBoxBorder(boxX, boxY, boxW, boxH, 1.2f);

        // Outer Metallic Border with Gold Corner Notches
        sr.setColor(success ? new Color(1f, 0.85f, 0.35f, 0.85f) : new Color(0.9f, 0.3f, 0.3f, 0.85f));
        drawBoxBorder(modalX, modalY, modalW, modalH, 2.2f);
    }

    private void drawInkOverlays() {
        if (screen.fishingManager.fishPhunMucTimer > 0) {
            float alpha = Math.min(1f, screen.fishingManager.fishPhunMucTimer / 4f);
            sr.setColor(0.02f, 0.02f, 0.08f, 0.75f * alpha);
            sr.rect(0, 0, W, H);
            sr.setColor(0f, 0f, 0.06f, 0.6f * alpha);
            sr.circle(W * 0.3f, H * 0.6f, 80f);
            sr.circle(W * 0.7f, H * 0.4f, 60f);
            sr.circle(W * 0.5f, H * 0.75f, 50f);
        }
        if (screen.fishingManager.fishPhunMucMuTimer > 0) {
            float alpha = Math.min(1f, screen.fishingManager.fishPhunMucMuTimer / 4f);
            sr.setColor(0f, 0f, 0f, 0.92f * alpha);
            sr.rect(0, 0, W, H);
            sr.setColor(0.02f, 0.01f, 0.04f, 0.85f * alpha);
            sr.circle(W * 0.25f, H * 0.5f, 120f);
            sr.circle(W * 0.75f, H * 0.55f, 100f);
            sr.circle(W * 0.5f, H * 0.3f, 90f);
        }
    }

    // =========================================================================
    // COMBAT & RESULT TEXT RENDERING
    // =========================================================================

    public void drawText(SpriteBatch batch, Player player, Catchable item, String msg, String res) {
        // 1. Top Fish HP and Creature Name in COMBAT
        if (item != null && screen.fishingManager.state == State.COMBAT) {
            float hpBarX = (W - HP_W) / 2f;
            float hpBarY = H - 65f;

            Color nameColor = Color.WHITE;
            if (item instanceof Fish) nameColor = ((Fish) item).getRarityColor();

            // Fish Name
            font.setColor(nameColor);
            String fishTitle = item.getName();
            if (item instanceof Fish && ((Fish) item).getRarity() == Fish.Rarity.BOSS) {
                fishTitle = "[BOSS] " + fishTitle;
            }

            // Fish HP Value
            String hpString = "";
            if (item instanceof Fish) {
                Fish f = (Fish) item;
                hpString = (int) f.getCurrentHp() + " / " + (int) f.getMaxHp() + " HP (" +
                                  (int) (MathUtils.clamp(f.getCurrentHp() / f.getMaxHp(), 0f, 1f) * 100) + "%)";
            }
            layout.setText(font, hpString);
            float hpTextW = layout.width;

            // Auto-scale fish title if it would collide with HP string
            float maxTitleW = HP_W - hpTextW - 35f;
            layout.setText(font, fishTitle);
            if (layout.width > maxTitleW) {
                font.getData().setScale(maxTitleW / layout.width);
            }
            font.draw(batch, fishTitle, hpBarX + 12f, hpBarY + HP_H - 10f);
            font.getData().setScale(1f);

            if (!hpString.isEmpty()) {
                font.setColor(Color.WHITE);
                font.draw(batch, hpString, hpBarX + HP_W - hpTextW - 12f, hpBarY + HP_H - 10f);
            }
        }

        // 2. Tension Gauge Readings
        float tensionX = 35f;
        float tensionY = H / 2f - PANEL_H / 2f;

        if (screen.fishingManager.fishQuayBunTimer > 0) {
            font.setColor(new Color(1f, 0.35f, 0.35f, 1f));
            font.draw(batch, "[MẤT TÍN HIỆU]", tensionX - 8f, tensionY + PANEL_H + 24f);
            font.getData().setScale(1.8f);
            font.draw(batch, "???", tensionX + 4f, tensionY + PANEL_H / 2f + 15f);
            font.getData().setScale(1f);
        } else {
            float tRatio = player.getCurrentLineTension() / Math.max(1f, player.getMaxLineTension());
            font.setColor(tRatio > 0.85f ? new Color(1f, 0.25f, 0.25f, 1f) : new Color(0.85f, 0.9f, 0.98f, 1f));
            font.draw(batch, "ĐỘ CĂNG", tensionX, tensionY + PANEL_H + 38f);
            font.draw(batch, (int) player.getCurrentLineTension() + " / " + (int) player.getMaxLineTension() + " kg",
                      tensionX - 6f, tensionY + PANEL_H + 18f);

            if (tRatio > 0.85f) {
                font.setColor(1f, 0.2f, 0.2f, 1f);
                font.draw(batch, "[!] CẢNH BÁO!", tensionX - 10f, tensionY - 12f);
            }
        }

        // 3. Player Stamina in Combat
        float stmX = 35f;
        float stmY = H / 2f - PANEL_H / 2f - 40f;
        font.setColor(Color.WHITE);
        font.draw(batch, "THỂ LỰC: " + (int) player.getCurrentStamina() + " / " + (int) player.getMaxStamina(),
                  stmX, stmY + 40f);

        // 4. Combat Guidance Banner (Center Screen)
        if (screen.fishingManager.state == State.COMBAT && item instanceof Fish) {
            Fish f = (Fish) item;
            boolean isPulling = (f.getState() == Fish.State.PULLING);
            String bannerText = isPulling ? "◄◄ CÁ ĐANG GIÃY MẠNH! THẢ DÂY! ◄◄" : "►► CÁ ĐANG MỆT! GIỮ SPACE ĐỂ KÉO! ►►";

            batch.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(isPulling ? new Color(0.85f, 0.15f, 0.18f, 0.85f) : new Color(0.15f, 0.75f, 0.35f, 0.85f));
            float bannerW = 440f;
            float bannerH = 46f;
            sr.rect((W - bannerW) / 2f, H / 2f + 115f, bannerW, bannerH);
            sr.setColor(1f, 0.95f, 0.5f, 0.9f);
            drawBoxBorder((W - bannerW) / 2f, H / 2f + 115f, bannerW, bannerH, 1.8f);
            sr.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            batch.begin();

            font.setColor(Color.WHITE);
            drawCenteredText(font, bannerText, (W - bannerW) / 2f, H / 2f + 115f, bannerW, bannerH);
        }

        // 5. Active Skills Text & Hotkeys
        if (screen.fishingManager.state == State.COMBAT) {
            String[] actives = player.getEquippedActives();
            String[] keys = {"[Q]", "[W]", "[E]", "[R]"};
            for (int i = 0; i < 4; i++) {
                if (actives[i] != null) {
                    float cd = getCooldown(actives[i]);
                    float x = BTN_ACTIVES_X + i * 76f;
                    float y = BTN_ACTIVES_Y;

                    String name = player.getSkillTree().getSkill(actives[i]).name;
                    if (name.length() > 6) name = name.substring(0, 5) + ".";

                    font.getData().setScale(0.82f);
                    font.setColor(cd > 0 ? Color.GRAY : Color.WHITE);
                    font.draw(batch, name, x + 4f, y + 54f);

                    if (cd > 0) {
                        font.setColor(1f, 0.85f, 0.2f, 1f);
                        font.draw(batch, String.format("%.1fs", cd), x + 16f, y + 34f);
                    }
                    font.setColor(0.45f, 0.85f, 1f, 1f);
                    font.draw(batch, keys[i], x + 38f, y + 18f);
                    font.getData().setScale(1f);
                }
            }

            // CĂNG DÂY
            font.setColor(Color.WHITE);
            font.draw(batch, "CĂNG DÂY", BTN_CANG_X + 8f, BTN_CANG_Y + 46f);
            font.setColor(1f, 0.9f, 0.35f, 1f);
            font.draw(batch, "[SPACE]", BTN_CANG_X + 16f, BTN_CANG_Y + 24f);
        }

        // 6. Casting Result Feedback Animation
        if (screen.fishingManager.castResultTimer > 0) {
            float timer = screen.fishingManager.castResultTimer;
            float alpha = Math.min(1f, timer / 0.5f);
            float yOffset = (1.5f - timer) * 40f;

            titleFont.setColor(0f, 0f, 0f, 0.5f * alpha);
            layout.setText(titleFont, screen.fishingManager.castResultText);
            titleFont.draw(batch, screen.fishingManager.castResultText, (W - layout.width) / 2f + 2f, H / 2f + 140f + yOffset - 2f);

            if (screen.fishingManager.castResultText.equals("PERFECT!")) {
                titleFont.setColor(1f, 0.88f, 0.25f, alpha);
            } else if (screen.fishingManager.castResultText.equals("GOOD")) {
                titleFont.setColor(0.35f, 1f, 0.45f, alpha);
            } else {
                titleFont.setColor(0.85f, 0.85f, 0.85f, alpha);
            }
            titleFont.draw(batch, screen.fishingManager.castResultText, (W - layout.width) / 2f, H / 2f + 140f + yOffset);
        }

        // 7. Water Bobber & Bite Notice
        drawBobberText(batch);

        // 8. Result Modal Content
        if (screen.fishingManager.state == State.RESULT) {
            drawResultModalText(batch, msg, res);
            font.setColor(Color.WHITE);
            drawCenteredText(font, "TIẾP TỤC CÂU", BTN_CAST_X, BTN_CAST_Y, BTN_CAST_W, BTN_CAST_H);
        } else if (screen.fishingManager.state == State.PRE_CAST) {
            font.setColor(Color.WHITE);
            drawCenteredText(font, "QUĂNG CẦN", BTN_CAST_X, BTN_CAST_Y, BTN_CAST_W, BTN_CAST_H);
        }

        // Menu Back button
        drawCenteredText(font, "MENU", BTN_MENU_X, BTN_MENU_Y, BTN_MENU_W, BTN_MENU_H);
        drawInventoryText(batch, player);
    }

    private void drawBobberText(SpriteBatch batch) {
        if (screen.fishingManager.state == State.WAITING || screen.fishingManager.state == State.BITE) {
            float time = screen.fishingManager.stateTimer;
            float bobY = (screen.fishingManager.state == State.WAITING)
                         ? (float) Math.sin(time * 5f) * 5f
                         : -15f + (float) Math.random() * 10f;

            float centerX = W / 2f;
            float centerY = H / 2f + bobY;

            if (screen.fishingManager.state == State.BITE) {
                float scale = 2.4f + (float) Math.sin(time * 25f) * 0.4f;
                font.getData().setScale(scale);
                font.setColor(1f, 0.2f, 0.2f, 1f);
                layout.setText(font, "(!)");
                font.draw(batch, "(!)", (W - layout.width) / 2f, H / 2f + 85f);
                font.getData().setScale(1f);
            }
        }
    }

    private void drawResultModalText(SpriteBatch batch, String msg, String res) {
        float modalW = 560f;
        float modalH = 390f;
        float modalX = (W - modalW) / 2f;
        float modalY = (H - modalH) / 2f + 25f;
        boolean success = screen.fishingManager.lastEncounterSuccess;

        if (success) {
            // Header Title
            titleFont.setColor(1f, 0.90f, 0.35f, 1f);
            drawCenteredText(titleFont, "★ CHIẾN LỢI PHẨM THU HOẠCH ★", modalX, modalY + modalH - 55f, modalW, 55f);

            if (screen.fishingManager.currentCatch instanceof Fish) {
                Fish f = (Fish) screen.fishingManager.currentCatch;

                // Fish Name in Rarity Color
                font.setColor(f.getRarityColor());
                font.getData().setScale(1.4f);
                layout.setText(font, f.getName());
                font.draw(batch, f.getName(), (W - layout.width) / 2f, modalY + modalH - 80f);
                font.getData().setScale(1f);

                // Rarity & Category Tag
                font.setColor(0.85f, 0.9f, 0.98f, 0.9f);
                String tag = "Độ hiếm: " + f.getRarity().name();
                layout.setText(font, tag);
                font.draw(batch, tag, (W - layout.width) / 2f, modalY + modalH - 118f);

                // Size Specs inside Inner Box
                float boxY = modalY + 65f;
                font.setColor(Color.WHITE);
                String spec = "Chiều dài: " + String.format("%.1f", f.getLengthCm()) + " cm  •  Cân nặng: " +
                              String.format("%.1f", f.getWeightKg()) + " kg";
                drawCenteredText(font, spec, modalX + 25f, boxY + 95f, modalW - 50f, 30f);

                // Grade / Record Badge
                if (msg != null && msg.contains("[KỶ LỤC]")) {
                    font.setColor(1f, 0.85f, 0.2f, 1f);
                    drawCenteredText(font, "★ PHÁ KỶ LỤC CHIỀU DÀI MỚI! ★", modalX + 25f, boxY + 60f, modalW - 50f, 25f);
                } else if (msg != null && msg.contains("[MỚI]")) {
                    font.setColor(0.35f, 1f, 0.5f, 1f);
                    drawCenteredText(font, "★ LOÀI CÁ MỚI ĐÃ ĐƯỢC GHI VÀO SỔ TAY! ★", modalX + 25f, boxY + 60f, modalW - 50f, 25f);
                } else {
                    font.setColor(0.4f, 0.85f, 1f, 1f);
                    drawCenteredText(font, "Thu hoạch xuất sắc!", modalX + 25f, boxY + 60f, modalW - 50f, 25f);
                }

                // Earnings summary line
                if (res != null && !res.isEmpty()) {
                    font.setColor(0.45f, 1f, 0.55f, 1f);
                    drawCenteredText(font, res, modalX + 25f, boxY + 20f, modalW - 50f, 25f);
                }
            } else if (screen.fishingManager.currentCatch != null) {
                font.setColor(Color.WHITE);
                drawCenteredText(font, "NHẬN ĐƯỢC: " + screen.fishingManager.currentCatch.getName(),
                                 modalX + 25f, modalY + modalH / 2f, modalW - 50f, 40f);
            }
        } else {
            // Failed Catch Header
            titleFont.setColor(1f, 0.35f, 0.35f, 1f);
            drawCenteredText(titleFont, "✕ CÁ ĐÃ THOÁT MẤT! ✕", modalX, modalY + modalH - 55f, modalW, 55f);

            String failReason = (msg != null && !msg.isEmpty()) ? msg : "Dây câu bị đứt hoặc cá đã bơi thoát mất!";
            font.setColor(0.95f, 0.85f, 0.85f, 1f);
            drawCenteredText(font, failReason, modalX + 25f, modalY + modalH / 2f - 20f, modalW - 50f, 40f);
        }
    }

    private float getCooldown(String skillId) {
        if (skillId == null) return 0f;
        if (skillId.equals("keo_manh")) return screen.fishingManager.keoManhCooldown;
        if (skillId.equals("giat_kep")) return screen.fishingManager.giatKepCooldown;
        if (skillId.equals("duong_suc")) return screen.fishingManager.duongSucCooldown;
        if (skillId.equals("pha_giap")) return screen.fishingManager.phaGiapCooldown;
        if (skillId.equals("luot_day")) return screen.fishingManager.luotDayCooldown;
        if (skillId.equals("loi_nguyen_troi_buoc")) return screen.fishingManager.troiBuocCooldown;
        return 0f;
    }

    private void drawBoxBorder(float x, float y, float w, float h, float thickness) {
        sr.rectLine(x, y, x + w, y, thickness);
        sr.rectLine(x, y + h, x + w, y + h, thickness);
        sr.rectLine(x, y, x, y + h, thickness);
        sr.rectLine(x + w, y, x + w, y + h, thickness);
    }

    private void drawCenteredText(BitmapFont f, String text, float x, float y, float width, float height) {
        layout.setText(f, text);
        f.draw(screen.getGame().batch, text, x + (width - layout.width) / 2f, y + (height + layout.height) / 2f);
    }
}
