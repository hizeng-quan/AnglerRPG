package com.fishingrpg.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fishingrpg.game.FishingRPG;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.entities.equipment.Equipment;
import com.fishingrpg.game.entities.equipment.EquipmentGenerator;
import com.fishingrpg.game.entities.equipment.EquipmentType;
import com.fishingrpg.game.entities.equipment.Rarity;
import com.fishingrpg.game.systems.AudioManager;
import com.fishingrpg.game.systems.EventManager;
import com.fishingrpg.game.systems.EventManager.DailyReward;
import com.fishingrpg.game.ui.NotificationUI;

public class EventScreen implements Screen {

    private final FishingRPG game;
    private final Player player;
    private final EventManager eventManager;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final ShapeRenderer sr;
    private final BitmapFont font;
    private final BitmapFont titleFont;
    private final GlyphLayout layout;

    private static final float W = 1280f;
    private static final float H = 720f;
    private final Screen previousScreen;

    private int selectedTab = 0; // 0: Nhiệm vụ Sự Kiện, 1: Điểm Danh Tháng, 2: Shop Đổi Quà
    private float animTimer = 0f;

    // Toast Notification
    private String message = null;
    private float messageTimer = 0f;

    // Background floating ember particles
    private static final int PARTICLE_COUNT = 30;
    private final float[] particleX = new float[PARTICLE_COUNT];
    private final float[] particleY = new float[PARTICLE_COUNT];
    private final float[] particleSpeed = new float[PARTICLE_COUNT];
    private final float[] particleSize = new float[PARTICLE_COUNT];
    private final float[] particlePhase = new float[PARTICLE_COUNT];

    // Tooltip for shop
    private int hoveredShopItem = -1;

    // Vector3 cache for mouse coordinate conversion
    private final Vector3 mousePos = new Vector3();

    public EventScreen(FishingRPG game, Screen previousScreen) {
        this.game = game;
        this.player = game.player;
        this.eventManager = player.getEventManager();
        this.previousScreen = previousScreen;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(W, H, camera);
        this.camera.position.set(W / 2f, H / 2f, 0);
        this.sr = new ShapeRenderer();
        this.font = game.font;
        this.titleFont = game.titleFont != null ? game.titleFont : game.font;
        this.layout = new GlyphLayout();

        // Initialize atmospheric floating particles
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            particleX[i] = MathUtils.random(0, W);
            particleY[i] = MathUtils.random(0, H);
            particleSpeed[i] = MathUtils.random(15f, 40f);
            particleSize[i] = MathUtils.random(2f, 4.5f);
            particlePhase[i] = MathUtils.random(0f, 6.28f);
        }

        eventManager.checkAndResetDailyMissions();
    }

    private void showMessage(String msg) {
        this.message = msg;
        this.messageTimer = 2.5f;
    }

    @Override
    public void show() {
        AudioManager.getInstance().playBGM("bgm_menu");
    }

    @Override
    public void render(float delta) {
        animTimer += delta;
        if (messageTimer > 0) messageTimer -= delta;

        // Update ember particles
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            particleY[i] += particleSpeed[i] * delta;
            particleX[i] += MathUtils.sin(animTimer + particlePhase[i]) * 10f * delta;
            if (particleY[i] > H + 10f) {
                particleY[i] = -10f;
                particleX[i] = MathUtils.random(0, W);
            }
        }

        handleInput();
        if (game.getScreen() != this) return;

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.06f, 0.08f, 0.12f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // 1. Draw shapes
        sr.begin(ShapeRenderer.ShapeType.Filled);
        drawBackgroundShapes();
        drawTabShapes();
        drawContentContainerShapes();
        if (selectedTab == 0) drawMissionsShapes();
        else if (selectedTab == 1) drawAttendanceShapes();
        else if (selectedTab == 2) drawShopShapes();
        sr.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        // 2. Draw text and sprites
        game.batch.begin();
        drawHeaderTexts();
        drawTabTexts();
        if (selectedTab == 0) drawMissionsTexts();
        else if (selectedTab == 1) drawAttendanceTexts();
        else if (selectedTab == 2) drawShopTexts();
        game.batch.end();

        // 3. Draw tooltip for hovered shop item if needed
        if (selectedTab == 2 && hoveredShopItem >= 0 && hoveredShopItem <= 2) {
            drawShopTooltip(hoveredShopItem);
        }

        // 4. Draw notification popup
        if (messageTimer > 0 && message != null) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            NotificationUI.drawNotification(sr, game.batch, font, message, messageTimer, 2.5f, W, H);
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    // =========================================================================
    // BACKGROUND & HEADER SHAPES
    // =========================================================================

    private void drawBackgroundShapes() {
        // Deep mythical festive gradient: dark midnight violet to deep charcoal blue
        sr.rect(0, 0, W, H,
                new Color(0.04f, 0.06f, 0.10f, 1f),
                new Color(0.04f, 0.06f, 0.10f, 1f),
                new Color(0.12f, 0.06f, 0.15f, 1f),
                new Color(0.12f, 0.06f, 0.15f, 1f));

        // Floating ember particles
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            float alpha = 0.35f + 0.35f * MathUtils.sin(animTimer * 2f + particlePhase[i]);
            if (i % 3 == 0) {
                sr.setColor(1f, 0.8f, 0.3f, alpha); // Gold ember
            } else {
                sr.setColor(0.95f, 0.35f, 0.3f, alpha); // Ruby ember
            }
            sr.circle(particleX[i], particleY[i], particleSize[i]);
        }

        // Top Navigation Bar Header background
        sr.setColor(0.09f, 0.12f, 0.18f, 0.95f);
        sr.rect(0, H - 70f, W, 70f);
        sr.setColor(0.85f, 0.65f, 0.25f, 0.8f);
        sr.rect(0, H - 72f, W, 2f); // Golden accent line

        // Back button shape
        boolean hoverBack = isMouseOver(25f, H - 58f, 115f, 42f);
        sr.setColor(hoverBack ? new Color(0.75f, 0.2f, 0.25f, 1f) : new Color(0.22f, 0.15f, 0.22f, 0.95f));
        sr.rect(25f, H - 58f, 115f, 42f);
        sr.setColor(hoverBack ? new Color(1f, 0.85f, 0.4f, 1f) : new Color(0.6f, 0.4f, 0.5f, 0.8f));
        drawBoxBorder(25f, H - 58f, 115f, 42f, 2f);

        // Countdown pill badge shape
        sr.setColor(0.15f, 0.12f, 0.2f, 0.9f);
        sr.rect(W - 470f, H - 56f, 230f, 38f);
        sr.setColor(0.85f, 0.65f, 0.25f, 0.6f);
        drawBoxBorder(W - 470f, H - 56f, 230f, 38f, 1.5f);

        // Event points badge shape
        sr.setColor(0.22f, 0.16f, 0.08f, 0.95f);
        sr.rect(W - 225f, H - 56f, 200f, 38f);
        sr.setColor(1f, 0.85f, 0.3f, 0.9f);
        drawBoxBorder(W - 225f, H - 56f, 200f, 38f, 1.5f);
        // Star badge accent
        sr.setColor(1f, 0.85f, 0.25f, 1f);
        sr.circle(W - 208f, H - 37f, 9f);
        sr.setColor(0.9f, 0.4f, 0.1f, 1f);
        sr.circle(W - 208f, H - 37f, 4f);
    }

    private void drawTabShapes() {
        float tabY = H - 120f;
        float tabH = 44f;
        float tabW = 340f;
        float gap = 20f;
        float startX = (W - (3 * tabW + 2 * gap)) / 2f;

        for (int i = 0; i < 3; i++) {
            float tx = startX + i * (tabW + gap);
            boolean isSelected = (selectedTab == i);
            boolean isHovered = isMouseOver(tx, tabY, tabW, tabH);

            if (isSelected) {
                // Festive active tab gradient
                sr.rect(tx, tabY, tabW, tabH,
                        new Color(0.72f, 0.18f, 0.2f, 1f),
                        new Color(0.72f, 0.18f, 0.2f, 1f),
                        new Color(0.92f, 0.45f, 0.18f, 1f),
                        new Color(0.92f, 0.45f, 0.18f, 1f));
                // Bright golden underline
                sr.setColor(1f, 0.9f, 0.35f, 1f);
                sr.rect(tx, tabY, tabW, 4f);
                sr.setColor(1f, 0.85f, 0.4f, 0.7f);
                drawBoxBorder(tx, tabY, tabW, tabH, 1.5f);
            } else {
                sr.setColor(isHovered ? new Color(0.2f, 0.24f, 0.35f, 0.95f) : new Color(0.13f, 0.17f, 0.25f, 0.9f));
                sr.rect(tx, tabY, tabW, tabH);
                sr.setColor(isHovered ? new Color(0.5f, 0.65f, 0.85f, 0.7f) : new Color(0.25f, 0.32f, 0.45f, 0.6f));
                drawBoxBorder(tx, tabY, tabW, tabH, 1.5f);
            }

            // Notification indicator on Tab 1 if login not claimed today
            if (i == 1 && !eventManager.hasClaimedLoginToday() && player.getTotalLoginDaysThisMonth() < 25) {
                float pulse = 0.85f + 0.15f * MathUtils.sin(animTimer * 6f);
                sr.setColor(1f, 0.2f, 0.2f, pulse);
                sr.circle(tx + tabW - 18f, tabY + tabH - 12f, 6.5f);
                sr.setColor(1f, 0.9f, 0.3f, pulse);
                sr.circle(tx + tabW - 18f, tabY + tabH - 12f, 3.5f);
            }
        }
    }

    private void drawContentContainerShapes() {
        float panelX = 45f;
        float panelY = 25f;
        float panelW = W - 90f;
        float panelH = H - 155f;

        // Main glassmorphism background
        sr.setColor(0.09f, 0.12f, 0.19f, 0.94f);
        sr.rect(panelX, panelY, panelW, panelH);

        // Multi-line border
        sr.setColor(0.25f, 0.35f, 0.5f, 0.55f);
        drawBoxBorder(panelX, panelY, panelW, panelH, 2f);
        sr.setColor(0.85f, 0.65f, 0.25f, 0.4f);
        drawBoxBorder(panelX + 3f, panelY + 3f, panelW - 6f, panelH - 6f, 1f);

        // Golden corner accent notches
        float notchSize = 14f;
        sr.setColor(1f, 0.85f, 0.35f, 0.9f);
        // Top-left
        sr.rect(panelX, panelY + panelH - 3f, notchSize, 3f);
        sr.rect(panelX, panelY + panelH - notchSize, 3f, notchSize);
        // Top-right
        sr.rect(panelX + panelW - notchSize, panelY + panelH - 3f, notchSize, 3f);
        sr.rect(panelX + panelW - 3f, panelY + panelH - notchSize, 3f, notchSize);
        // Bottom-left
        sr.rect(panelX, panelY, notchSize, 3f);
        sr.rect(panelX, panelY, 3f, notchSize);
        // Bottom-right
        sr.rect(panelX + panelW - notchSize, panelY, notchSize, 3f);
        sr.rect(panelX + panelW - 3f, panelY, 3f, notchSize);
    }

    // =========================================================================
    // TAB 0: NHIỆM VỤ SỰ KIỆN (MISSIONS)
    // =========================================================================

    private void drawMissionsShapes() {
        float startX = 75f;
        float cardW = W - 150f;

        // Top Summary Banner
        float bannerY = H - 225f;
        float bannerH = 58f;
        sr.setColor(0.14f, 0.18f, 0.28f, 0.95f);
        sr.rect(startX, bannerY, cardW, bannerH);
        sr.setColor(0.85f, 0.65f, 0.25f, 0.7f);
        drawBoxBorder(startX, bannerY, cardW, bannerH, 1.5f);

        // Left accent stripe
        boolean allDone = eventManager.dailyMissionsCompleted[0] &&
                          eventManager.dailyMissionsCompleted[1] &&
                          eventManager.dailyMissionsCompleted[2];
        sr.setColor(allDone ? new Color(0.2f, 0.85f, 0.4f, 1f) : new Color(0.95f, 0.6f, 0.15f, 1f));
        sr.rect(startX, bannerY, 6f, bannerH);

        // 3 Mission Cards
        float[] cardY = { H - 330f, H - 435f, H - 540f };
        float cardH = 92f;

        for (int i = 0; i < 3; i++) {
            float cy = cardY[i];
            boolean done = eventManager.dailyMissionsCompleted[i];
            boolean hoverCard = isMouseOver(startX, cy, cardW, cardH);

            // Card background
            if (done) {
                sr.setColor(hoverCard ? new Color(0.12f, 0.24f, 0.22f, 0.95f) : new Color(0.10f, 0.19f, 0.18f, 0.9f));
            } else {
                sr.setColor(hoverCard ? new Color(0.15f, 0.20f, 0.30f, 0.95f) : new Color(0.11f, 0.15f, 0.23f, 0.9f));
            }
            sr.rect(startX, cy, cardW, cardH);

            // Border
            sr.setColor(done ? new Color(0.25f, 0.75f, 0.45f, 0.8f) : new Color(0.25f, 0.35f, 0.5f, 0.7f));
            drawBoxBorder(startX, cy, cardW, cardH, 1.5f);

            // Left colored stripe
            sr.setColor(done ? new Color(0.25f, 0.9f, 0.45f, 1f) : new Color(0.95f, 0.65f, 0.2f, 1f));
            sr.rect(startX, cy, 6f, cardH);

            // Icon Badge Circle
            float iconX = startX + 45f;
            float iconY = cy + cardH / 2f;
            sr.setColor(done ? new Color(0.18f, 0.4f, 0.25f, 1f) : new Color(0.2f, 0.26f, 0.38f, 1f));
            sr.circle(iconX, iconY, 26f);
            sr.setColor(done ? new Color(0.35f, 0.9f, 0.5f, 1f) : new Color(0.85f, 0.65f, 0.25f, 1f));
            sr.circle(iconX, iconY, 23f);
            sr.setColor(done ? new Color(0.15f, 0.3f, 0.2f, 1f) : new Color(0.15f, 0.18f, 0.28f, 1f));
            sr.circle(iconX, iconY, 20f);

            // Distinct geometric icon inside circle
            if (i == 0) {
                // Fish emblem
                sr.setColor(done ? new Color(0.35f, 0.9f, 0.5f, 1f) : new Color(0.3f, 0.75f, 1f, 1f));
                sr.ellipse(iconX - 2f, iconY, 14f, 8f);
                sr.triangle(iconX + 6f, iconY, iconX + 13f, iconY + 7f, iconX + 13f, iconY - 7f);
            } else if (i == 1) {
                // Coin emblem
                sr.setColor(done ? new Color(0.35f, 0.9f, 0.5f, 1f) : new Color(1f, 0.85f, 0.3f, 1f));
                sr.circle(iconX, iconY, 11f);
                sr.setColor(0.15f, 0.18f, 0.28f, 1f);
                sr.circle(iconX, iconY, 7f);
            } else {
                // Gacha chest emblem
                sr.setColor(done ? new Color(0.35f, 0.9f, 0.5f, 1f) : new Color(1f, 0.45f, 0.65f, 1f));
                sr.rect(iconX - 9f, iconY - 8f, 18f, 16f);
                sr.setColor(1f, 0.85f, 0.3f, 1f);
                sr.rect(iconX - 2f, iconY - 8f, 4f, 16f);
            }

            // Progress Bar
            float barX = startX + 500f;
            float barY = cy + 24f;
            float barW = 380f;
            float barH = 18f;

            // Bar background
            sr.setColor(0.08f, 0.1f, 0.15f, 0.95f);
            sr.rect(barX, barY, barW, barH);

            // Bar fill
            float progress = 0f;
            if (i == 0) progress = MathUtils.clamp((float) eventManager.fishCaughtToday / 50f, 0f, 1f);
            else if (i == 1) progress = MathUtils.clamp((float) eventManager.goldSpentToday / 2000f, 0f, 1f);
            else if (i == 2) progress = MathUtils.clamp((float) eventManager.eventGachaPulledToday / 1f, 0f, 1f);

            if (progress > 0) {
                if (done) {
                    sr.setColor(0.25f, 0.85f, 0.4f, 0.95f);
                } else {
                    sr.setColor(0.95f, 0.65f, 0.2f, 0.95f);
                }
                sr.rect(barX, barY, barW * progress, barH);
            }
            sr.setColor(0.3f, 0.4f, 0.55f, 0.7f);
            drawBoxBorder(barX, barY, barW, barH, 1.2f);

            // Status Badge Button
            float btnX = startX + cardW - 190f;
            float btnY = cy + 26f;
            float btnW = 160f;
            float btnH = 40f;

            if (done) {
                sr.setColor(0.18f, 0.45f, 0.25f, 0.95f);
                sr.rect(btnX, btnY, btnW, btnH);
                sr.setColor(0.35f, 0.9f, 0.5f, 0.9f);
                drawBoxBorder(btnX, btnY, btnW, btnH, 1.5f);
            } else {
                sr.setColor(0.18f, 0.24f, 0.35f, 0.9f);
                sr.rect(btnX, btnY, btnW, btnH);
                sr.setColor(0.45f, 0.55f, 0.75f, 0.7f);
                drawBoxBorder(btnX, btnY, btnW, btnH, 1.5f);
            }
        }
    }

    // =========================================================================
    // TAB 1: ĐIỂM DANH THÁNG (ATTENDANCE)
    // =========================================================================

    private void drawAttendanceShapes() {
        float startX = 72f;
        float cardW = W - 144f;

        // Top Summary & Claim Bar
        float headerY = H - 218f;
        float headerH = 54f;

        sr.setColor(0.13f, 0.17f, 0.26f, 0.95f);
        sr.rect(startX, headerY, cardW, headerH);
        sr.setColor(0.85f, 0.65f, 0.25f, 0.6f);
        drawBoxBorder(startX, headerY, cardW, headerH, 1.5f);

        // Claim Button on Right
        float claimBtnX = startX + cardW - 270f;
        float claimBtnY = headerY + 7f;
        float claimBtnW = 255f;
        float claimBtnH = 40f;

        boolean claimedToday = eventManager.hasClaimedLoginToday();
        boolean hoverClaim = isMouseOver(claimBtnX, claimBtnBtnY(headerY), claimBtnW, claimBtnH);

        if (!claimedToday && player.getTotalLoginDaysThisMonth() < 25) {
            float pulse = 0.88f + 0.12f * MathUtils.sin(animTimer * 5f);
            sr.rect(claimBtnX, claimBtnY, claimBtnW, claimBtnH,
                    new Color(0.8f * pulse, 0.55f * pulse, 0.1f * pulse, 1f),
                    new Color(0.8f * pulse, 0.55f * pulse, 0.1f * pulse, 1f),
                    new Color(0.95f * pulse, 0.75f * pulse, 0.2f * pulse, 1f),
                    new Color(0.95f * pulse, 0.75f * pulse, 0.2f * pulse, 1f));
            sr.setColor(1f, 0.95f, 0.4f, 1f);
            drawBoxBorder(claimBtnX, claimBtnY, claimBtnW, claimBtnH, 2f);
        } else {
            sr.setColor(0.18f, 0.35f, 0.22f, 0.9f);
            sr.rect(claimBtnX, claimBtnY, claimBtnW, claimBtnH);
            sr.setColor(0.3f, 0.7f, 0.4f, 0.7f);
            drawBoxBorder(claimBtnX, claimBtnY, claimBtnW, claimBtnH, 1.5f);
        }

        // 25 Days Grid (5 cols x 5 rows)
        int daysLogged = player.getTotalLoginDaysThisMonth();
        float gridStartX = 72f;
        float gridStartY = H - 290f;
        float boxW = 216f;
        float boxH = 74f;
        float gapX = 14f;
        float gapY = 10f;

        for (int i = 0; i < 25; i++) {
            int dayNum = i + 1;
            int col = i % 5;
            int row = i / 5;
            float bx = gridStartX + col * (boxW + gapX);
            float by = gridStartY - row * (boxH + gapY);

            boolean isClaimed = dayNum <= daysLogged;
            boolean isToday = !claimedToday && (dayNum == daysLogged + 1);
            boolean hoverBox = isMouseOver(bx, by, boxW, boxH);
            DailyReward reward = eventManager.getDailyReward(dayNum);

            if (isClaimed) {
                // Done / Claimed
                sr.setColor(hoverBox ? new Color(0.14f, 0.26f, 0.20f, 0.95f) : new Color(0.10f, 0.20f, 0.16f, 0.9f));
                sr.rect(bx, by, boxW, boxH);
                sr.setColor(0.25f, 0.7f, 0.4f, 0.7f);
                drawBoxBorder(bx, by, boxW, boxH, 1.5f);
            } else if (isToday) {
                // Active Today!
                float pulse = 0.88f + 0.12f * MathUtils.sin(animTimer * 5f);
                sr.setColor(new Color(0.32f * pulse, 0.24f * pulse, 0.10f * pulse, 0.98f));
                sr.rect(bx, by, boxW, boxH);
                sr.setColor(1f, 0.85f, 0.35f, pulse);
                drawBoxBorder(bx, by, boxW, boxH, 2.2f);
            } else {
                // Upcoming / Locked
                sr.setColor(hoverBox ? new Color(0.15f, 0.19f, 0.28f, 0.95f) : new Color(0.10f, 0.13f, 0.20f, 0.85f));
                sr.rect(bx, by, boxW, boxH);
                sr.setColor(reward.isMilestone ? new Color(0.85f, 0.65f, 0.25f, 0.6f) : new Color(0.22f, 0.28f, 0.4f, 0.5f));
                drawBoxBorder(bx, by, boxW, boxH, 1.2f);
            }

            // Milestone Banner across top of milestone card
            if (reward.isMilestone) {
                sr.setColor(1f, 0.8f, 0.25f, 0.85f);
                sr.rect(bx, by + boxH - 6f, boxW, 6f);
            }
        }
    }

    private float claimBtnBtnY(float headerY) {
        return headerY + 7f;
    }

    // =========================================================================
    // TAB 2: TIỆM ĐỔI QUÀ (EVENT SHOP)
    // =========================================================================

    private void drawShopShapes() {
        float startX = 72f;
        float cardW = W - 144f;

        // Top Set Bonus Banner
        float bannerY = H - 222f;
        float bannerH = 58f;

        sr.rect(startX, bannerY, cardW, bannerH,
                new Color(0.28f, 0.08f, 0.12f, 0.98f),
                new Color(0.28f, 0.08f, 0.12f, 0.98f),
                new Color(0.42f, 0.12f, 0.18f, 0.98f),
                new Color(0.42f, 0.12f, 0.18f, 0.98f));
        sr.setColor(1f, 0.82f, 0.3f, 0.85f);
        drawBoxBorder(startX, bannerY, cardW, bannerH, 1.8f);

        // 6 Shop Cards (3 cols x 2 rows)
        float itemW = 360f;
        float itemH = 188f;
        float gapX = 27f;
        float gapY = 16f;
        float gridStartX = startX;
        float gridStartY = H - 435f;

        hoveredShopItem = -1;

        for (int i = 0; i < 6; i++) {
            int col = i % 3;
            int row = i / 3;
            float ix = gridStartX + col * (itemW + gapX);
            float iy = gridStartY - row * (itemH + gapY);

            boolean hoverItem = isMouseOver(ix, iy, itemW, itemH);
            if (hoverItem) hoveredShopItem = i;

            int price = getShopItemPrice(i);
            boolean canAfford = player.getEventPoints() >= price;

            // Card background
            sr.setColor(hoverItem ? new Color(0.15f, 0.19f, 0.28f, 0.98f) : new Color(0.11f, 0.14f, 0.21f, 0.92f));
            sr.rect(ix, iy, itemW, itemH);

            // Card border (Rarity color: Crimson for limited gear, gold/purple for tickets)
            Color borderColor = getShopItemBorderColor(i);
            sr.setColor(borderColor.r, borderColor.g, borderColor.b, hoverItem ? 0.95f : 0.65f);
            drawBoxBorder(ix, iy, itemW, itemH, hoverItem ? 2f : 1.2f);

            // Item Icon Box on the left
            float iconBoxX = ix + 14f;
            float iconBoxY = iy + itemH - 85f;
            float iconBoxSize = 70f;
            sr.setColor(0.08f, 0.10f, 0.15f, 0.95f);
            sr.rect(iconBoxX, iconBoxY, iconBoxSize, iconBoxSize);
            sr.setColor(borderColor.r, borderColor.g, borderColor.b, 0.8f);
            drawBoxBorder(iconBoxX, iconBoxY, iconBoxSize, iconBoxSize, 1.5f);

            // Draw primitive icon inside box
            drawShopItemIcon(i, iconBoxX + iconBoxSize / 2f, iconBoxY + iconBoxSize / 2f);

            // Buy button shape
            float btnX = ix + 14f;
            float btnY = iy + 14f;
            float btnW = itemW - 28f;
            float btnH = 38f;

            boolean hoverBtn = isMouseOver(btnX, btnY, btnW, btnH);

            if (canAfford) {
                sr.setColor(hoverBtn ? new Color(0.95f, 0.75f, 0.2f, 1f) : new Color(0.85f, 0.65f, 0.15f, 1f));
                sr.rect(btnX, btnY, btnW, btnH);
                sr.setColor(1f, 0.95f, 0.5f, 1f);
                drawBoxBorder(btnX, btnY, btnW, btnH, 1.8f);
            } else {
                sr.setColor(0.18f, 0.22f, 0.30f, 0.85f);
                sr.rect(btnX, btnY, btnW, btnH);
                sr.setColor(0.35f, 0.42f, 0.55f, 0.55f);
                drawBoxBorder(btnX, btnY, btnW, btnH, 1.2f);
            }
        }
    }

    private void drawShopItemIcon(int index, float cx, float cy) {
        if (index == 0) {
            // Rod icon
            sr.setColor(1f, 0.3f, 0.35f, 1f);
            sr.rectLine(cx - 20f, cy - 20f, cx + 20f, cy + 20f, 4.5f);
            sr.setColor(1f, 0.85f, 0.3f, 1f);
            sr.circle(cx + 20f, cy + 20f, 4f);
        } else if (index == 1) {
            // Line icon
            sr.setColor(0.4f, 0.8f, 1f, 1f);
            sr.circle(cx, cy, 18f);
            sr.setColor(0.08f, 0.10f, 0.15f, 1f);
            sr.circle(cx, cy, 12f);
            sr.setColor(1f, 0.35f, 0.4f, 1f);
            sr.circle(cx, cy, 6f);
        } else if (index == 2) {
            // Hook icon
            sr.setColor(1f, 0.85f, 0.3f, 1f);
            sr.rectLine(cx, cy + 18f, cx, cy - 8f, 4f);
            sr.rectLine(cx, cy - 8f, cx - 12f, cy - 8f, 4f);
            sr.rectLine(cx - 12f, cy - 8f, cx - 12f, cy + 6f, 4f);
        } else if (index == 3) {
            // Premium Ticket (Purple ticket)
            sr.setColor(0.7f, 0.35f, 0.9f, 1f);
            sr.rect(cx - 18f, cy - 12f, 36f, 24f);
            sr.setColor(1f, 0.85f, 0.3f, 1f);
            sr.circle(cx, cy, 5f);
        } else if (index == 4) {
            // Standard Ticket (Golden ticket)
            sr.setColor(0.95f, 0.7f, 0.2f, 1f);
            sr.rect(cx - 18f, cy - 12f, 36f, 24f);
            sr.setColor(0.1f, 0.15f, 0.25f, 1f);
            sr.circle(cx, cy, 5f);
        } else if (index == 5) {
            // Gold sack
            sr.setColor(1f, 0.85f, 0.25f, 1f);
            sr.circle(cx, cy - 3f, 16f);
            sr.rect(cx - 6f, cy + 8f, 12f, 8f);
            sr.setColor(0.85f, 0.35f, 0.2f, 1f);
            sr.rect(cx - 8f, cy + 7f, 16f, 3f);
        }
    }

    private int getShopItemPrice(int index) {
        switch (index) {
            case 0: return 5; // Cần Câu Vệ Quốc
            case 1: return 5; // Dây Cước Vệ Quốc
            case 2: return 5; // Lưỡi Câu Vệ Quốc
            case 3: return 3; // Vé Quay Cao Cấp x3
            case 4: return 2; // Vé Quay Thường x5
            case 5: return 1; // Túi 10,000 Vàng
            default: return 1;
        }
    }

    private Color getShopItemBorderColor(int index) {
        if (index <= 2) return new Color(0.92f, 0.25f, 0.28f, 1f); // LIMITED_EVENT Red
        if (index == 3) return new Color(0.75f, 0.4f, 0.95f, 1f); // Epic/Premium Purple
        if (index == 4) return new Color(1f, 0.8f, 0.25f, 1f);   // Rare Gold
        return new Color(0.95f, 0.75f, 0.2f, 1f);                 // Gold
    }

    // =========================================================================
    // TEXT RENDERING
    // =========================================================================

    private void drawHeaderTexts() {
        // Back Button
        font.setColor(Color.WHITE);
        drawCenteredText(font, "◀ QUAY LẠI", 25f, H - 58f, 115f, 42f);

        // Center Title
        titleFont.setColor(0f, 0f, 0f, 0.6f);
        drawCenteredText(titleFont, "HÀO KHÍ LẠC HỒNG", 2f, H - 42f, W, 30f); // shadow
        titleFont.setColor(1f, 0.88f, 0.35f, 1f);
        drawCenteredText(titleFont, "HÀO KHÍ LẠC HỒNG", 0f, H - 40f, W, 30f);

        // Subtitle
        font.setColor(0.85f, 0.85f, 0.85f, 0.9f);
        drawCenteredText(font, "★ SỰ KIỆN GIỚI HẠN MÙA LỄ HỘI • THẦN KHÍ & VỆ QUỐC VANG DANH ★", 0f, H - 65f, W, 20f);

        // Countdown Timer Badge
        font.setColor(1f, 0.9f, 0.35f, 1f);
        String eventTime = eventManager.getEventTimeRemaining();
        drawCenteredText(font, "🕒 " + eventTime, W - 470f, H - 56f, 230f, 38f);

        // Event Points Badge
        font.setColor(1f, 0.95f, 0.4f, 1f);
        font.draw(game.batch, "Điểm Sự Kiện: " + player.getEventPoints(), W - 190f, H - 32f);
    }

    private void drawTabTexts() {
        float tabY = H - 120f;
        float tabH = 44f;
        float tabW = 340f;
        float gap = 20f;
        float startX = (W - (3 * tabW + 2 * gap)) / 2f;

        String[] tabNames = {
            "NHIỆM VỤ SỰ KIỆN",
            "ĐIỂM DANH THÁNG",
            "TIỆM ĐỔI QUÀ [HOT]"
        };

        for (int i = 0; i < 3; i++) {
            float tx = startX + i * (tabW + gap);
            boolean isSelected = (selectedTab == i);
            font.setColor(isSelected ? Color.WHITE : new Color(0.75f, 0.82f, 0.92f, 0.85f));
            drawCenteredText(font, tabNames[i], tx, tabY, tabW, tabH);
        }
    }

    private void drawMissionsTexts() {
        float startX = 75f;
        float cardW = W - 150f;

        // Banner text
        float bannerY = H - 225f;
        float bannerH = 58f;

        titleFont.getData().setScale(0.6f);
        titleFont.setColor(1f, 0.85f, 0.3f, 1f);
        titleFont.draw(game.batch, "NHIỆM VỤ HÀNG NGÀY • LÀM MỚI LÚC 00:00", startX + 25f, bannerY + bannerH - 12f);
        titleFont.getData().setScale(1f);

        font.setColor(0.85f, 0.9f, 0.95f, 0.9f);
        font.draw(game.batch, "Hoàn thành đủ 3 nhiệm vụ mỗi ngày nhận ngay +1 Điểm Sự Kiện!", startX + 25f, bannerY + 22f);

        int doneCount = (eventManager.dailyMissionsCompleted[0] ? 1 : 0) +
                        (eventManager.dailyMissionsCompleted[1] ? 1 : 0) +
                        (eventManager.dailyMissionsCompleted[2] ? 1 : 0);

        if (eventManager.allMissionsClaimedToday || doneCount == 3) {
            font.setColor(0.35f, 1f, 0.5f, 1f);
            drawCenteredText(font, "✔ ĐÃ NHẬN THƯỞNG TỔNG KẾT (+1 ĐIỂM SK)!", startX + cardW - 380f, bannerY, 360f, bannerH);
        } else {
            font.setColor(1f, 0.85f, 0.35f, 1f);
            drawCenteredText(font, "Tiến độ hôm nay: " + doneCount + " / 3 hoàn thành", startX + cardW - 360f, bannerY, 340f, bannerH);
        }

        // Mission Cards Text
        String[] titles = {
            "Cần Mẫn Đánh Bắt",
            "Đầu Tư Tiêu Pha",
            "Vận May Chiêu Tài (Banner Sự Kiện)"
        };
        String[] descs = {
            "Câu đủ 50 con cá trong ngày hôm nay",
            "Tiêu 2,000 Vàng trong ngày (mua đồ, nâng cấp)",
            "Quay ít nhất 1 lần tại Banner Gacha Sự Kiện hôm nay"
        };
        String[] progressTexts = {
            eventManager.fishCaughtToday + " / 50 Con (" + Math.min(100, (int)(eventManager.fishCaughtToday / 50f * 100)) + "%)",
            eventManager.goldSpentToday + " / 2,000 Vàng (" + Math.min(100, (int)(eventManager.goldSpentToday / 2000f * 100)) + "%)",
            eventManager.eventGachaPulledToday + " / 1 Lần (" + (eventManager.eventGachaPulledToday >= 1 ? "100" : "0") + "%)"
        };
        String[] rewards = {
            "Phần thưởng: +500 Vàng",
            "Phần thưởng: +1,000 Vàng",
            "Phần thưởng: +1 Vé Quay Thường"
        };

        float[] cardY = { H - 330f, H - 435f, H - 540f };
        float cardH = 92f;

        for (int i = 0; i < 3; i++) {
            float cy = cardY[i];
            boolean done = eventManager.dailyMissionsCompleted[i];

            // Title
            font.setColor(done ? new Color(0.4f, 1f, 0.6f, 1f) : new Color(1f, 0.88f, 0.35f, 1f));
            font.draw(game.batch, titles[i], startX + 90f, cy + cardH - 22f);

            // Description
            font.setColor(0.85f, 0.88f, 0.95f, 0.85f);
            font.draw(game.batch, descs[i], startX + 90f, cy + cardH - 52f);

            // Reward callout
            font.setColor(0.95f, 0.75f, 0.25f, 0.95f);
            font.draw(game.batch, rewards[i], startX + 500f, cy + cardH - 22f);

            // Progress text inside/above bar
            font.setColor(Color.WHITE);
            font.draw(game.batch, progressTexts[i], startX + 500f, cy + 38f);

            // Button status label
            float btnX = startX + cardW - 190f;
            float btnY = cy + 26f;
            float btnW = 160f;
            float btnH = 40f;

            if (done) {
                font.setColor(0.4f, 1f, 0.6f, 1f);
                drawCenteredText(font, "✔ HOÀN THÀNH", btnX, btnY, btnW, btnH);
            } else {
                font.setColor(0.85f, 0.92f, 1f, 0.9f);
                drawCenteredText(font, "ĐANG LÀM", btnX, btnY, btnW, btnH);
            }
        }
    }

    private void drawAttendanceTexts() {
        float startX = 72f;
        float cardW = W - 144f;
        float headerY = H - 218f;
        float headerH = 54f;

        int daysLogged = player.getTotalLoginDaysThisMonth();
        boolean claimedToday = eventManager.hasClaimedLoginToday();

        // Left Header Text
        titleFont.getData().setScale(0.6f);
        titleFont.setColor(1f, 0.85f, 0.3f, 1f);
        titleFont.draw(game.batch, "ĐÃ ĐIỂM DANH: " + daysLogged + " / 25 NGÀY THÁNG NÀY", startX + 25f, headerY + headerH - 12f);
        titleFont.getData().setScale(1f);

        font.setColor(0.85f, 0.88f, 0.95f, 0.85f);
        font.draw(game.batch, "Điểm danh đều đặn để nhận trọn bộ quà & Cần Câu Vệ Quốc tại ngày 25!", startX + 25f, headerY + 20f);

        // Claim Button on Right
        float claimBtnX = startX + cardW - 270f;
        float claimBtnY = headerY + 7f;
        float claimBtnW = 255f;
        float claimBtnH = 40f;

        if (!claimedToday && daysLogged < 25) {
            font.setColor(0.12f, 0.1f, 0.05f, 1f); // Dark bold text on glowing button
            drawCenteredText(font, "🎁 ĐIỂM DANH HÔM NAY", claimBtnX, claimBtnY, claimBtnW, claimBtnH);
        } else {
            font.setColor(0.4f, 1f, 0.6f, 1f);
            drawCenteredText(font, "✔ HÔM NAY ĐÃ NHẬN", claimBtnX, claimBtnY, claimBtnW, claimBtnH);
        }

        // 25 Days Grid
        float gridStartX = 72f;
        float gridStartY = H - 290f;
        float boxW = 216f;
        float boxH = 74f;
        float gapX = 14f;
        float gapY = 10f;

        for (int i = 0; i < 25; i++) {
            int dayNum = i + 1;
            int col = i % 5;
            int row = i / 5;
            float bx = gridStartX + col * (boxW + gapX);
            float by = gridStartY - row * (boxH + gapY);

            boolean isClaimed = dayNum <= daysLogged;
            boolean isToday = !claimedToday && (dayNum == daysLogged + 1);
            DailyReward reward = eventManager.getDailyReward(dayNum);

            // Day Title
            if (isClaimed) {
                font.setColor(0.4f, 0.85f, 0.55f, 1f);
                font.draw(game.batch, "Ngày " + dayNum + " (Đã nhận)", bx + 10f, by + boxH - 12f);
            } else if (isToday) {
                font.setColor(1f, 0.9f, 0.35f, 1f);
                font.draw(game.batch, "Ngày " + dayNum + " [HÔM NAY]", bx + 10f, by + boxH - 12f);
            } else {
                font.setColor(reward.isMilestone ? new Color(1f, 0.85f, 0.35f, 1f) : new Color(0.85f, 0.9f, 0.98f, 0.85f));
                font.draw(game.batch, "Ngày " + dayNum + (reward.isMilestone ? " ★" : ""), bx + 10f, by + boxH - 12f);
            }

            // Reward description
            font.setColor(isClaimed ? new Color(0.6f, 0.75f, 0.65f, 0.85f) : (isToday ? Color.WHITE : new Color(0.95f, 0.8f, 0.3f, 0.95f)));
            font.draw(game.batch, reward.rewardSummary, bx + 10f, by + 30f);
        }
    }

    private void drawShopTexts() {
        float startX = 72f;
        float cardW = W - 144f;
        float bannerY = H - 222f;
        float bannerH = 58f;

        // Set Banner Text
        titleFont.getData().setScale(0.62f);
        titleFont.setColor(1f, 0.88f, 0.35f, 1f);
        titleFont.draw(game.batch, "BỘ TRANG BỊ GIỚI HẠN: VỆ QUỐC VANG DANH (LIMITED EVENT)", startX + 20f, bannerY + bannerH - 12f);
        titleFont.getData().setScale(1f);

        font.setColor(0.95f, 0.9f, 0.85f, 0.95f);
        font.draw(game.batch, "Kích hoạt hiệu ứng (3 món): +29% Tỉ lệ cá hiếm, Kháng đứt dây +10%!", startX + 20f, bannerY + 20f);

        // Balance text on right
        font.setColor(1f, 0.92f, 0.4f, 1f);
        drawCenteredText(font, "Số dư: " + player.getEventPoints() + " Điểm SK", startX + cardW - 250f, bannerY, 230f, bannerH);

        // 6 Shop Items
        float itemW = 360f;
        float itemH = 188f;
        float gapX = 27f;
        float gapY = 16f;
        float gridStartX = startX;
        float gridStartY = H - 435f;

        String[] itemNames = {
            "Cần Câu Vệ Quốc",
            "Dây Cước Vệ Quốc",
            "Lưỡi Câu Vệ Quốc",
            "Vé Quay Cao Cấp x3",
            "Vé Quay Thường x5",
            "Túi Vàng Đại Phú"
        };
        String[] itemSubtitles = {
            "Cần Câu Limited Event",
            "Dây Cước Limited Event",
            "Lưỡi Câu Limited Event",
            "Vé Gacha Banner Sự Kiện",
            "Vé Gacha Banner Tiêu Chuẩn",
            "Nhận ngay 10,000 Vàng"
        };
        String[] itemStats = {
            "Sát thương +20 • Thể lực +125",
            "Sức căng +225 • Kháng đứt +4",
            "Cá hiếm +10% • Giữ mồi +20%",
            "Chiêu mộ trang bị siêu phẩm",
            "Chiêu mộ trang bị tiêu chuẩn",
            "Nâng cấp kỹ năng & đồ nghề"
        };

        for (int i = 0; i < 6; i++) {
            int col = i % 3;
            int row = i / 3;
            float ix = gridStartX + col * (itemW + gapX);
            float iy = gridStartY - row * (itemH + gapY);

            int price = getShopItemPrice(i);
            boolean canAfford = player.getEventPoints() >= price;

            // Name
            Color nameColor = getShopItemBorderColor(i);
            font.setColor(nameColor);
            font.draw(game.batch, itemNames[i], ix + 95f, iy + itemH - 22f);

            // Subtitle
            font.setColor(0.75f, 0.8f, 0.9f, 0.85f);
            font.draw(game.batch, itemSubtitles[i], ix + 95f, iy + itemH - 46f);

            // Stats preview
            font.setColor(1f, 0.9f, 0.45f, 0.95f);
            font.draw(game.batch, itemStats[i], ix + 95f, iy + itemH - 70f);

            // Price / Exchange button text
            float btnX = ix + 14f;
            float btnY = iy + 14f;
            float btnW = itemW - 28f;
            float btnH = 38f;

            if (canAfford) {
                font.setColor(0.12f, 0.1f, 0.05f, 1f); // Dark bold text on glowing button
                drawCenteredText(font, "ĐỔI (" + price + " ĐIỂM SK)", btnX, btnY, btnW, btnH);
            } else {
                font.setColor(0.7f, 0.75f, 0.85f, 0.75f);
                drawCenteredText(font, "THIẾU ĐIỂM (" + price + " Điểm)", btnX, btnY, btnW, btnH);
            }
        }
    }

    // =========================================================================
    // TOOLTIP & POPUPS
    // =========================================================================

    private void drawShopTooltip(int itemIndex) {
        float tipW = 340f;
        float tipH = 150f;
        float tipX = mousePos.x + 18f;
        float tipY = mousePos.y - tipH - 10f;

        if (tipX + tipW > W - 15f) tipX = mousePos.x - tipW - 15f;
        if (tipY < 15f) tipY = 15f;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(0.08f, 0.10f, 0.16f, 0.96f);
        sr.rect(tipX, tipY, tipW, tipH);
        sr.setColor(0.92f, 0.25f, 0.28f, 0.9f);
        drawBoxBorder(tipX, tipY, tipW, tipH, 1.8f);
        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();
        font.setColor(0.95f, 0.3f, 0.35f, 1f);
        if (itemIndex == 0) {
            font.draw(game.batch, "Cần Câu Vệ Quốc (LIMITED EVENT)", tipX + 15f, tipY + tipH - 18f);
            font.setColor(Color.WHITE);
            font.draw(game.batch, "• Loại: Cần Câu (Rod) | Cấp độ: Lv.1", tipX + 15f, tipY + tipH - 42f);
            font.draw(game.batch, "• Sát thương kéo: +20 Sức kéo", tipX + 15f, tipY + tipH - 64f);
            font.draw(game.batch, "• Thể lực tăng thêm: +125 Điểm", tipX + 15f, tipY + tipH - 86f);
            font.setColor(1f, 0.85f, 0.35f, 1f);
            font.draw(game.batch, "Set Vệ Quốc: +29% Cá hiếm, Kháng đứt +10%", tipX + 15f, tipY + tipH - 114f);
        } else if (itemIndex == 1) {
            font.draw(game.batch, "Dây Cước Vệ Quốc (LIMITED EVENT)", tipX + 15f, tipY + tipH - 18f);
            font.setColor(Color.WHITE);
            font.draw(game.batch, "• Loại: Dây Cước (Line) | Cấp độ: Lv.1", tipX + 15f, tipY + tipH - 42f);
            font.draw(game.batch, "• Sức căng dây: +225 Điểm", tipX + 15f, tipY + tipH - 64f);
            font.draw(game.batch, "• Kháng đứt dây: +4.0%", tipX + 15f, tipY + tipH - 86f);
            font.setColor(1f, 0.85f, 0.35f, 1f);
            font.draw(game.batch, "Set Vệ Quốc: +29% Cá hiếm, Kháng đứt +10%", tipX + 15f, tipY + tipH - 114f);
        } else if (itemIndex == 2) {
            font.draw(game.batch, "Lưỡi Câu Vệ Quốc (LIMITED EVENT)", tipX + 15f, tipY + tipH - 18f);
            font.setColor(Color.WHITE);
            font.draw(game.batch, "• Loại: Lưỡi Câu (Hook) | Cấp độ: Lv.1", tipX + 15f, tipY + tipH - 42f);
            font.draw(game.batch, "• Tỉ lệ cá hiếm: +10.0%", tipX + 15f, tipY + tipH - 64f);
            font.draw(game.batch, "• Tỉ lệ giữ mồi: +20.0%", tipX + 15f, tipY + tipH - 86f);
            font.setColor(1f, 0.85f, 0.35f, 1f);
            font.draw(game.batch, "Set Vệ Quốc: +29% Cá hiếm, Kháng đứt +10%", tipX + 15f, tipY + tipH - 114f);
        }
        game.batch.end();
    }

    // =========================================================================
    // INPUT HANDLING
    // =========================================================================

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            goBack();
            return;
        }

        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mousePos);

        if (Gdx.input.justTouched()) {
            // 1. Back button
            if (isMouseOver(25f, H - 58f, 115f, 42f)) {
                AudioManager.getInstance().playSFX("sfx_button");
                goBack();
                return;
            }

            // 2. Tabs
            float tabY = H - 120f;
            float tabH = 44f;
            float tabW = 340f;
            float gap = 20f;
            float startX = (W - (3 * tabW + 2 * gap)) / 2f;

            for (int i = 0; i < 3; i++) {
                float tx = startX + i * (tabW + gap);
                if (isMouseOver(tx, tabY, tabW, tabH)) {
                    if (selectedTab != i) {
                        selectedTab = i;
                        AudioManager.getInstance().playSFX("sfx_button");
                    }
                    return;
                }
            }

            // 3. Tab 1: Claim attendance button or active day card click
            if (selectedTab == 1) {
                float cardW = W - 144f;
                float headerY = H - 218f;
                float claimBtnX = 72f + cardW - 270f;
                float claimBtnY = headerY + 7f;
                float claimBtnW = 255f;
                float claimBtnH = 40f;

                boolean clickedClaim = isMouseOver(claimBtnX, claimBtnY, claimBtnW, claimBtnH);

                // Also check today's active card
                int daysLogged = player.getTotalLoginDaysThisMonth();
                if (!clickedClaim && !eventManager.hasClaimedLoginToday() && daysLogged < 25) {
                    int todayIdx = daysLogged;
                    int col = todayIdx % 5;
                    int row = todayIdx / 5;
                    float bx = 72f + col * (216f + 14f);
                    float by = H - 290f - row * (74f + 10f);
                    if (isMouseOver(bx, by, 216f, 74f)) {
                        clickedClaim = true;
                    }
                }

                if (clickedClaim) {
                    if (eventManager.hasClaimedLoginToday()) {
                        showMessage("Bạn đã điểm danh hôm nay rồi! Hãy quay lại vào ngày mai.");
                    } else if (daysLogged >= 25) {
                        showMessage("Bạn đã hoàn thành đủ 25 ngày điểm danh của tháng!");
                    } else {
                        String result = eventManager.claimDailyLoginReward();
                        if (result != null) {
                            AudioManager.getInstance().playSFX("sfx_catch");
                            showMessage(result);
                        }
                    }
                    return;
                }
            }

            // 4. Tab 2: Shop Buy buttons
            if (selectedTab == 2) {
                float itemW = 360f;
                float itemH = 188f;
                float gapX = 27f;
                float gapY = 16f;
                float gridStartX = 72f;
                float gridStartY = H - 435f;

                for (int i = 0; i < 6; i++) {
                    int col = i % 3;
                    int row = i / 3;
                    float ix = gridStartX + col * (itemW + gapX);
                    float iy = gridStartY - row * (itemH + gapY);

                    float btnX = ix + 14f;
                    float btnY = iy + 14f;
                    float btnW = itemW - 28f;
                    float btnH = 38f;

                    if (isMouseOver(btnX, btnY, btnW, btnH)) {
                        buyShopItem(i);
                        return;
                    }
                }
            }
        }
    }

    private void buyShopItem(int index) {
        int price = getShopItemPrice(index);
        if (player.getEventPoints() < price) {
            AudioManager.getInstance().playSFX("sfx_button");
            showMessage("Không đủ Điểm Sự Kiện! Cần " + price + " Điểm để đổi.");
            return;
        }

        player.addEventPoints(-price);
        AudioManager.getInstance().playSFX("sfx_catch");

        switch (index) {
            case 0:
                player.addEquipment(EquipmentGenerator.createFreeEventEquipment(EquipmentType.ROD));
                showMessage("Đổi thành công: Cần Câu Vệ Quốc (Limited Event)!");
                break;
            case 1:
                player.addEquipment(EquipmentGenerator.createFreeEventEquipment(EquipmentType.LINE));
                showMessage("Đổi thành công: Dây Cước Vệ Quốc (Limited Event)!");
                break;
            case 2:
                player.addEquipment(EquipmentGenerator.createFreeEventEquipment(EquipmentType.HOOK));
                showMessage("Đổi thành công: Lưỡi Câu Vệ Quốc (Limited Event)!");
                break;
            case 3:
                player.addPremiumTickets(3);
                showMessage("Đổi thành công: +3 Vé Quay Cao Cấp!");
                break;
            case 4:
                player.addGachaTickets(5);
                showMessage("Đổi thành công: +5 Vé Quay Thường!");
                break;
            case 5:
                player.gainGold(10000);
                showMessage("Đổi thành công: +10,000 Vàng!");
                break;
        }
    }

    private void goBack() {
        game.setScreen(previousScreen);
        dispose();
    }

    // =========================================================================
    // HELPER UTILITIES
    // =========================================================================

    private boolean isMouseOver(float x, float y, float w, float h) {
        return mousePos.x >= x && mousePos.x <= x + w &&
               mousePos.y >= y && mousePos.y <= y + h;
    }

    private void drawBoxBorder(float x, float y, float w, float h, float thickness) {
        sr.rectLine(x, y, x + w, y, thickness);
        sr.rectLine(x, y + h, x + w, y + h, thickness);
        sr.rectLine(x, y, x, y + h, thickness);
        sr.rectLine(x + w, y, x + w, y + h, thickness);
    }

    private void drawCenteredText(BitmapFont f, String text, float x, float y, float width, float height) {
        layout.setText(f, text);
        f.draw(game.batch, text, x + (width - layout.width) / 2f, y + (height + layout.height) / 2f);
    }

    @Override public void resize(int w, int h) {
        viewport.update(w, h, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        sr.dispose();
    }
}
