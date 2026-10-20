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
import com.fishingrpg.game.entities.Quest;
import com.fishingrpg.game.entities.equipment.EquipmentGenerator;
import com.fishingrpg.game.entities.equipment.EquipmentType;
import com.fishingrpg.game.systems.AudioManager;
import com.fishingrpg.game.systems.EventManager;
import com.fishingrpg.game.systems.EventManager.DailyReward;
import com.fishingrpg.game.ui.EquipmentIconRenderer;
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
    private static final int PARTICLE_COUNT = 35;
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

        // Initialize atmospheric floating embers
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            particleX[i] = MathUtils.random(0, W);
            particleY[i] = MathUtils.random(0, H);
            particleSpeed[i] = MathUtils.random(15f, 42f);
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
    // BACKGROUND & HEADER SHAPES (ĐÔNG SƠN LẠC HỒNG THEME)
    // =========================================================================

    private void drawBackgroundShapes() {
        // Deep imperial festive gradient: Midnight crimson ruby to royal indigo
        sr.rect(0, 0, W, H,
                new Color(0.08f, 0.03f, 0.05f, 1f),
                new Color(0.08f, 0.03f, 0.05f, 1f),
                new Color(0.12f, 0.07f, 0.16f, 1f),
                new Color(0.12f, 0.07f, 0.16f, 1f));

        // Majestic Dong Son Bronze Drum Watermark in background center
        float drumCX = W / 2f;
        float drumCY = H / 2f - 25f;

        // Concentric bronze drum rings
        sr.setColor(0.95f, 0.75f, 0.25f, 0.05f);
        sr.circle(drumCX, drumCY, 320f);
        sr.setColor(0.08f, 0.03f, 0.05f, 1f);
        sr.circle(drumCX, drumCY, 314f);

        sr.setColor(0.95f, 0.75f, 0.25f, 0.06f);
        sr.circle(drumCX, drumCY, 240f);
        sr.setColor(0.08f, 0.03f, 0.05f, 1f);
        sr.circle(drumCX, drumCY, 235f);

        sr.setColor(0.95f, 0.75f, 0.25f, 0.07f);
        sr.circle(drumCX, drumCY, 160f);
        sr.setColor(0.08f, 0.03f, 0.05f, 1f);
        sr.circle(drumCX, drumCY, 156f);

        // Radiant 14-pointed Sunburst rays at drum core
        float sunR = 75f;
        sr.setColor(1f, 0.85f, 0.3f, 0.07f);
        for (int i = 0; i < 14; i++) {
            float rad1 = (i * (360f / 14f)) * MathUtils.degreesToRadians;
            float rad2 = ((i + 0.5f) * (360f / 14f)) * MathUtils.degreesToRadians;
            float rad3 = ((i + 1f) * (360f / 14f)) * MathUtils.degreesToRadians;
            float x1 = drumCX + MathUtils.cos(rad1) * 22f;
            float y1 = drumCY + MathUtils.sin(rad1) * 22f;
            float x2 = drumCX + MathUtils.cos(rad2) * sunR;
            float y2 = drumCY + MathUtils.sin(rad2) * sunR;
            float x3 = drumCX + MathUtils.cos(rad3) * 22f;
            float y3 = drumCY + MathUtils.sin(rad3) * 22f;
            sr.triangle(x1, y1, x2, y2, x3, y3);
        }

        // Stylized Chim Lac birds silhouettes circling along middle ring
        for (int b = 0; b < 4; b++) {
            float bAngle = (b * 90f + animTimer * 4f) * MathUtils.degreesToRadians;
            float bx = drumCX + MathUtils.cos(bAngle) * 200f;
            float by = drumCY + MathUtils.sin(bAngle) * 200f;
            sr.setColor(1f, 0.85f, 0.35f, 0.08f);
            sr.triangle(bx, by, bx + 18f, by - 6f, bx - 8f, by - 12f);
            sr.triangle(bx, by, bx - 14f, by + 12f, bx + 6f, by + 4f);
        }

        // Floating ember particles
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            float alpha = 0.35f + 0.35f * MathUtils.sin(animTimer * 2f + particlePhase[i]);
            if (i % 3 == 0) {
                sr.setColor(1f, 0.82f, 0.3f, alpha); // Gold ember
            } else {
                sr.setColor(0.95f, 0.3f, 0.25f, alpha); // Ruby ember
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

        // SEPARATE TOP RIGHT PILLS:
        // 1. Event points badge pill (Rightmost)
        float ptsPillW = 175f;
        float ptsPillH = 38f;
        float ptsPillX = W - ptsPillW - 25f; // 1080f
        float ptsPillY = H - 56f;

        sr.setColor(0.22f, 0.16f, 0.08f, 0.95f);
        sr.rect(ptsPillX, ptsPillY, ptsPillW, ptsPillH);
        sr.setColor(1f, 0.85f, 0.3f, 0.9f);
        drawBoxBorder(ptsPillX, ptsPillY, ptsPillW, ptsPillH, 1.5f);
        // Star badge icon
        sr.setColor(1f, 0.85f, 0.25f, 1f);
        sr.circle(ptsPillX + 18f, ptsPillY + ptsPillH / 2f, 8f);
        sr.setColor(0.9f, 0.4f, 0.1f, 1f);
        sr.circle(ptsPillX + 18f, ptsPillY + ptsPillH / 2f, 4f);

        // 2. Countdown time pill badge (Separated to the left with 18px gap)
        float timePillW = 225f;
        float timePillH = 38f;
        float timePillX = ptsPillX - timePillW - 18f; // 837f
        float timePillY = H - 56f;

        sr.setColor(0.14f, 0.12f, 0.22f, 0.92f);
        sr.rect(timePillX, timePillY, timePillW, timePillH);
        sr.setColor(0.85f, 0.65f, 0.25f, 0.7f);
        drawBoxBorder(timePillX, timePillY, timePillW, timePillH, 1.5f);
        // Clock / hourglass small icon
        sr.setColor(0.85f, 0.75f, 0.3f, 1f);
        float tIconX = timePillX + 18f;
        float tIconY = timePillY + timePillH / 2f;
        sr.circle(tIconX, tIconY, 7f);
        sr.setColor(0.14f, 0.12f, 0.22f, 1f);
        sr.circle(tIconX, tIconY, 4.5f);
        sr.setColor(0.95f, 0.85f, 0.4f, 1f);
        sr.rectLine(tIconX, tIconY, tIconX, tIconY + 4f, 1.5f);
        sr.rectLine(tIconX, tIconY, tIconX + 3f, tIconY, 1.5f);
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

            // Notification indicator on Tab 0 if any quest is completed & unclaimed
            if (i == 0) {
                boolean hasQuestClaim = false;
                for (Quest q : eventManager.eventQuests) {
                    if (q.isCompleted() && !q.claimed) {
                        hasQuestClaim = true;
                        break;
                    }
                }
                if (hasQuestClaim || eventManager.canClaimMilestone()) {
                    float pulse = 0.85f + 0.15f * MathUtils.sin(animTimer * 6f);
                    sr.setColor(1f, 0.85f, 0.2f, pulse);
                    sr.circle(tx + tabW - 18f, tabY + tabH - 12f, 6.5f);
                    sr.setColor(0.9f, 0.2f, 0.1f, pulse);
                    sr.circle(tx + tabW - 18f, tabY + tabH - 12f, 3.5f);
                }
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
        sr.setColor(0.08f, 0.11f, 0.17f, 0.94f);
        sr.rect(panelX, panelY, panelW, panelH);

        // Multi-line festive border
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
    // TAB 0: NHIỆM VỤ SỰ KIỆN (GRID 2x3 & MILESTONE BAR LIKE QUEST SCREEN)
    // =========================================================================

    private void drawMissionsShapes() {
        float startX = 65f;
        float containerW = W - 130f;

        // 1. Top Milestone Banner (Span full width)
        float bannerY = H - 225f;
        float bannerH = 58f;
        sr.setColor(0.12f, 0.16f, 0.25f, 0.95f);
        sr.rect(startX, bannerY, containerW, bannerH);
        sr.setColor(0.85f, 0.65f, 0.25f, 0.7f);
        drawBoxBorder(startX, bannerY, containerW, bannerH, 1.5f);

        // Milestone Progress Track Bar inside Banner
        float pbX = startX + 18f;
        float pbY = bannerY + 12f;
        float pbW = containerW - 390f;
        float pbH = 12f;

        sr.setColor(0.06f, 0.08f, 0.13f, 0.95f);
        sr.rect(pbX, pbY, pbW, pbH);

        int completedCount = eventManager.getCompletedMissionsCount();
        float mRatio = MathUtils.clamp(completedCount / 4f, 0f, 1f);
        if (mRatio > 0f) {
            sr.setColor(completedCount >= 4 ? new Color(0.25f, 0.88f, 0.45f, 1f) : new Color(0.25f, 0.65f, 1f, 1f));
            sr.rect(pbX, pbY, Math.max(8f, pbW * mRatio), pbH);
        }
        sr.setColor(0.25f, 0.45f, 0.65f, 0.7f);
        drawBoxBorder(pbX, pbY, pbW, pbH, 1f);

        // Milestone Claim Button on Right of Banner
        float mBtnW = 180f;
        float mBtnH = 40f;
        float mBtnX = startX + containerW - mBtnW - 14f;
        float mBtnY = bannerY + 9f;

        if (eventManager.milestoneClaimedToday) {
            sr.setColor(0.14f, 0.22f, 0.18f, 0.9f);
            sr.rect(mBtnX, mBtnY, mBtnW, mBtnH);
            sr.setColor(0.25f, 0.65f, 0.4f, 0.6f);
            drawBoxBorder(mBtnX, mBtnY, mBtnW, mBtnH, 1.2f);
        } else if (completedCount >= 4) {
            float pulse = 0.8f + 0.2f * MathUtils.sin(animTimer * 5f);
            sr.rect(mBtnX, mBtnY, mBtnW, mBtnH,
                    new Color(0.85f * pulse, 0.6f * pulse, 0.12f * pulse, 1f),
                    new Color(0.85f * pulse, 0.6f * pulse, 0.12f * pulse, 1f),
                    new Color(1f * pulse, 0.85f * pulse, 0.25f * pulse, 1f),
                    new Color(1f * pulse, 0.85f * pulse, 0.25f * pulse, 1f));
            sr.setColor(1f, 0.95f, 0.5f, 1f);
            drawBoxBorder(mBtnX, mBtnY, mBtnW, mBtnH, 2f);
        } else {
            sr.setColor(0.12f, 0.16f, 0.24f, 0.85f);
            sr.rect(mBtnX, mBtnY, mBtnW, mBtnH);
            sr.setColor(0.3f, 0.4f, 0.55f, 0.5f);
            drawBoxBorder(mBtnX, mBtnY, mBtnW, mBtnH, 1.2f);
        }

        // Optional Claim All Button (to the left of Milestone Button)
        boolean hasUnclaimed = false;
        for (Quest q : eventManager.eventQuests) {
            if (q.isCompleted() && !q.claimed) {
                hasUnclaimed = true;
                break;
            }
        }
        if (hasUnclaimed) {
            float caW = 160f;
            float caH = 40f;
            float caX = mBtnX - caW - 12f;
            float caY = mBtnY;
            sr.setColor(0.18f, 0.35f, 0.24f, 0.95f);
            sr.rect(caX, caY, caW, caH);
            sr.setColor(0.35f, 0.9f, 0.5f, 0.85f);
            drawBoxBorder(caX, caY, caW, caH, 1.4f);
        }

        // 2. 6 Event Quest Cards (2 columns x 3 rows)
        float cardGapX = 18f;
        float cardGapY = 12f;
        float cardW = (containerW - cardGapX) / 2f;
        float cardH = 110f;
        float gridStartY = H - 245f;

        for (int i = 0; i < eventManager.eventQuests.size() && i < 6; i++) {
            Quest q = eventManager.eventQuests.get(i);
            int col = i % 2;
            int row = i / 2;
            float qx = startX + col * (cardW + cardGapX);
            float qy = gridStartY - row * (cardH + cardGapY) - cardH;

            boolean hoverCard = isMouseOver(qx, qy, cardW, cardH);

            // Card background
            if (q.claimed) {
                sr.setColor(0.09f, 0.12f, 0.16f, 0.85f);
            } else if (q.isCompleted()) {
                sr.setColor(hoverCard ? new Color(0.14f, 0.24f, 0.20f, 0.98f) : new Color(0.10f, 0.19f, 0.16f, 0.92f));
            } else {
                sr.setColor(hoverCard ? new Color(0.14f, 0.18f, 0.26f, 0.95f) : new Color(0.10f, 0.14f, 0.20f, 0.90f));
            }
            sr.rect(qx, qy, cardW, cardH);

            // Card border
            if (q.claimed) {
                sr.setColor(0.2f, 0.3f, 0.4f, 0.45f);
            } else if (q.isCompleted()) {
                sr.setColor(0.35f, 0.85f, 0.5f, 0.85f);
            } else {
                sr.setColor(0.25f, 0.35f, 0.5f, 0.65f);
            }
            drawBoxBorder(qx, qy, cardW, cardH, q.isCompleted() && !q.claimed ? 1.8f : 1.2f);

            // Left decorative festive stripe
            sr.setColor(q.claimed ? new Color(0.3f, 0.4f, 0.5f, 0.5f) : (q.isCompleted() ? new Color(0.3f, 0.9f, 0.5f, 1f) : new Color(0.95f, 0.65f, 0.2f, 1f)));
            sr.rect(qx, qy, 5f, cardH);

            // Reward pill background on top right
            float pillW = 160f;
            float pillH = 26f;
            float pillX = qx + cardW - pillW - 14f;
            float pillY = qy + cardH - 32f;
            sr.setColor(0.18f, 0.14f, 0.08f, 0.9f);
            sr.rect(pillX, pillY, pillW, pillH);
            sr.setColor(0.85f, 0.65f, 0.25f, 0.7f);
            drawBoxBorder(pillX, pillY, pillW, pillH, 1.2f);

            // Progress bar
            float qpbX = qx + 18f;
            float qpbY = qy + 14f;
            float qpbW = cardW - 155f;
            float qpbH = 12f;

            sr.setColor(0.06f, 0.08f, 0.12f, 0.95f);
            sr.rect(qpbX, qpbY, qpbW, qpbH);

            float pRatio = MathUtils.clamp((float) q.progress / q.target, 0f, 1f);
            if (pRatio > 0) {
                sr.setColor(q.isCompleted() ? new Color(0.25f, 0.85f, 0.45f, 1f) : new Color(0.25f, 0.65f, 1f, 1f));
                sr.rect(qpbX, qpbY, Math.max(8f, qpbW * pRatio), qpbH);
            }
            sr.setColor(0.25f, 0.35f, 0.48f, 0.65f);
            drawBoxBorder(qpbX, qpbY, qpbW, qpbH, 1f);

            // Claim / Status Button
            float btnW = 115f;
            float btnH = 36f;
            float btnX = qx + cardW - btnW - 14f;
            float btnY = qy + 12f;

            if (q.claimed) {
                sr.setColor(0.12f, 0.18f, 0.15f, 0.8f);
                sr.rect(btnX, btnY, btnW, btnH);
                sr.setColor(0.25f, 0.5f, 0.35f, 0.5f);
                drawBoxBorder(btnX, btnY, btnW, btnH, 1.2f);
            } else if (q.isCompleted()) {
                float pulse = 0.8f + 0.2f * MathUtils.sin(animTimer * 5f);
                sr.rect(btnX, btnY, btnW, btnH,
                        new Color(0.15f, 0.45f * pulse, 0.25f * pulse, 1f),
                        new Color(0.15f, 0.45f * pulse, 0.25f * pulse, 1f),
                        new Color(0.25f, 0.85f * pulse, 0.45f * pulse, 1f),
                        new Color(0.25f, 0.85f * pulse, 0.45f * pulse, 1f));
                sr.setColor(0.4f, 1f, 0.6f, 1f);
                drawBoxBorder(btnX, btnY, btnW, btnH, 1.8f);
            } else {
                sr.setColor(0.12f, 0.16f, 0.24f, 0.8f);
                sr.rect(btnX, btnY, btnW, btnH);
                sr.setColor(0.3f, 0.4f, 0.55f, 0.5f);
                drawBoxBorder(btnX, btnY, btnW, btnH, 1.2f);
            }
        }
    }

    // =========================================================================
    // TAB 1: ĐIỂM DANH THÁNG (ATTENDANCE) - SPACING FIXED (NO OVERLAP)
    // =========================================================================

    private void drawAttendanceShapes() {
        float startX = 72f;
        float cardW = W - 144f;

        // Top Summary & Claim Bar (Height 50px)
        float headerY = H - 212f;
        float headerH = 50f;

        sr.setColor(0.13f, 0.17f, 0.26f, 0.95f);
        sr.rect(startX, headerY, cardW, headerH);
        sr.setColor(0.85f, 0.65f, 0.25f, 0.6f);
        drawBoxBorder(startX, headerY, cardW, headerH, 1.5f);

        // Claim Button on Right of summary
        float claimBtnX = startX + cardW - 270f;
        float claimBtnY = headerY + 6f;
        float claimBtnW = 255f;
        float claimBtnH = 38f;

        boolean claimedToday = eventManager.hasClaimedLoginToday();
        int daysLogged = player.getTotalLoginDaysThisMonth();

        if (!claimedToday && daysLogged < 25) {
            float pulse = 0.8f + 0.2f * MathUtils.sin(animTimer * 5f);
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

        // 25 Days Grid (5 cols x 5 rows) - START Y ADJUSTED TO H - 296f FOR 16px CLEARANCE
        float gridStartX = 72f;
        float gridStartY = H - 296f;
        float boxW = 216f;
        float boxH = 68f;
        float gapX = 14f;
        float gapY = 8f;

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
                sr.setColor(hoverBox ? new Color(0.14f, 0.26f, 0.20f, 0.95f) : new Color(0.10f, 0.20f, 0.16f, 0.9f));
                sr.rect(bx, by, boxW, boxH);
                sr.setColor(0.25f, 0.7f, 0.4f, 0.7f);
                drawBoxBorder(bx, by, boxW, boxH, 1.5f);
            } else if (isToday) {
                float pulse = 0.8f + 0.2f * MathUtils.sin(animTimer * 4f);
                sr.setColor(hoverBox ? new Color(0.28f, 0.22f, 0.12f, 0.95f) : new Color(0.22f, 0.17f, 0.08f, 0.95f));
                sr.rect(bx, by, boxW, boxH);
                sr.setColor(1f, 0.85f, 0.35f, pulse);
                drawBoxBorder(bx, by, boxW, boxH, 2.2f);
            } else {
                sr.setColor(hoverBox ? new Color(0.15f, 0.19f, 0.28f, 0.95f) : new Color(0.10f, 0.13f, 0.20f, 0.85f));
                sr.rect(bx, by, boxW, boxH);
                sr.setColor(reward.isMilestone ? new Color(0.85f, 0.65f, 0.25f, 0.6f) : new Color(0.22f, 0.28f, 0.4f, 0.5f));
                drawBoxBorder(bx, by, boxW, boxH, 1.2f);
            }

            // Milestone Banner across top of milestone card
            if (reward.isMilestone) {
                sr.setColor(1f, 0.8f, 0.25f, 0.85f);
                sr.rect(bx, by + boxH - 5f, boxW, 5f);
            }
        }
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

            // DRAW REVOLUTIONARY ARTWORK FOR VỆ QUỐC SET
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
            // Cần Câu Vệ Quốc: Use glorious vector illustration
            EquipmentIconRenderer.drawVeQuocRod(sr, cx, cy, 26f, Color.GOLD);
        } else if (index == 1) {
            // Dây Cước Vệ Quốc: Use glorious vector illustration
            EquipmentIconRenderer.drawVeQuocLine(sr, cx, cy, 26f, Color.GOLD);
        } else if (index == 2) {
            // Lưỡi Câu Vệ Quốc: Use glorious vector illustration
            EquipmentIconRenderer.drawVeQuocHook(sr, cx, cy, 26f, Color.GOLD);
        } else if (index == 3) {
            // Premium Ticket (Purple ticket)
            EquipmentIconRenderer.drawPremiumTicketIcon(sr, cx, cy, 36f, 24f);
        } else if (index == 4) {
            // Standard Ticket (Golden ticket)
            EquipmentIconRenderer.drawNormalTicketIcon(sr, cx, cy, 36f, 24f);
        } else if (index == 5) {
            // Gold coin pouch
            sr.setColor(0.85f, 0.65f, 0.15f, 1f);
            sr.circle(cx, cy - 3f, 14f);
            sr.triangle(cx - 10f, cy + 6f, cx + 10f, cy + 6f, cx, cy + 18f);
            sr.setColor(0.95f, 0.2f, 0.25f, 1f);
            sr.rect(cx - 8f, cy + 3f, 16f, 3.5f);
        }
    }

    // =========================================================================
    // TEXT RENDERING
    // =========================================================================

    private void drawHeaderTexts() {
        // Back Button Text
        boolean hoverBack = isMouseOver(25f, H - 58f, 115f, 42f);
        font.setColor(hoverBack ? Color.WHITE : new Color(0.85f, 0.85f, 0.95f, 0.9f));
        layout.setText(font, "← QUAY LẠI");
        font.draw(game.batch, layout, 25f + (115f - layout.width) / 2f, H - 32f);

        // Center Title (Clean, prominent, no subtitle)
        float centerAreaX = 145f;
        float centerAreaW = (W - 225f - 175f - 40f) - centerAreaX - 10f;

        titleFont.setColor(0f, 0f, 0f, 0.6f);
        drawCenteredText(titleFont, "HÀO KHÍ LẠC HỒNG", centerAreaX + 2f, H - 76f, centerAreaW, 74f); // shadow
        titleFont.setColor(1f, 0.88f, 0.35f, 1f);
        drawCenteredText(titleFont, "HÀO KHÍ LẠC HỒNG", centerAreaX, H - 74f, centerAreaW, 74f);

        // Top Right Pills:
        float ptsPillW = 175f;
        float ptsPillH = 38f;
        float ptsPillX = W - ptsPillW - 25f;
        float ptsPillY = H - 56f;

        float timePillW = 225f;
        float timePillH = 38f;
        float timePillX = ptsPillX - timePillW - 18f;
        float timePillY = H - 56f;

        // 1. Time Remaining Badge
        font.setColor(1f, 0.9f, 0.35f, 1f);
        font.getData().setScale(0.80f);
        String eventTime = eventManager.getEventTimeRemaining();
        drawCenteredText(font, "Thời gian: " + eventTime, timePillX + 15f, timePillY, timePillW - 20f, timePillH);

        // 2. Event Points Badge
        font.setColor(1f, 0.95f, 0.4f, 1f);
        font.getData().setScale(0.85f);
        font.draw(game.batch, "Điểm: " + player.getEventPoints() + " SK", ptsPillX + 34f, H - 32f);
        font.getData().setScale(1f);
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
        float startX = 65f;
        float containerW = W - 130f;
        float bannerY = H - 225f;

        // 1. Top Milestone Banner Text
        int completedCount = eventManager.getCompletedMissionsCount();
        font.setColor(1f, 0.85f, 0.35f, 1f);
        font.getData().setScale(0.88f);
        font.draw(game.batch, "NHIỆM VỤ SỰ KIỆN HẰNG NGÀY • LÀM MỚI LÚC 00:00", startX + 18f, bannerY + 44f);

        // Milestone Button Text
        float mBtnW = 180f;
        float mBtnH = 40f;
        float mBtnX = startX + containerW - mBtnW - 14f;
        float mBtnY = bannerY + 9f;

        font.getData().setScale(0.82f);
        if (eventManager.milestoneClaimedToday) {
            font.setColor(0.4f, 0.85f, 0.55f, 1f);
            drawCenteredText(font, "✓ ĐÃ MỞ RƯƠNG", mBtnX, mBtnY, mBtnW, mBtnH);
        } else if (completedCount >= 4) {
            font.setColor(0.1f, 0.1f, 0.15f, 1f);
            drawCenteredText(font, "★ NHẬN MỐC THƯỞNG ★", mBtnX, mBtnY, mBtnW, mBtnH);
        } else {
            font.setColor(0.7f, 0.8f, 0.9f, 0.75f);
            drawCenteredText(font, completedCount + "/4 ĐỂ MỞ", mBtnX, mBtnY, mBtnW, mBtnH);
        }

        // Optional Claim All Button Text
        boolean hasUnclaimed = false;
        for (Quest q : eventManager.eventQuests) {
            if (q.isCompleted() && !q.claimed) {
                hasUnclaimed = true;
                break;
            }
        }
        if (hasUnclaimed) {
            float caW = 160f;
            float caH = 40f;
            float caX = mBtnX - caW - 12f;
            float caY = mBtnY;
            font.setColor(0.4f, 1f, 0.65f, 1f);
            drawCenteredText(font, "★ NHẬN TẤT CẢ ★", caX, caY, caW, caH);
        }

        // 2. 6 Event Quest Cards Texts
        float cardGapX = 18f;
        float cardGapY = 12f;
        float cardW = (containerW - cardGapX) / 2f;
        float cardH = 110f;
        float gridStartY = H - 245f;

        for (int i = 0; i < eventManager.eventQuests.size() && i < 6; i++) {
            Quest q = eventManager.eventQuests.get(i);
            int col = i % 2;
            int row = i / 2;
            float qx = startX + col * (cardW + cardGapX);
            float qy = gridStartY - row * (cardH + cardGapY) - cardH;

            float pillW = 160f;
            float pillH = 26f;
            float pillX = qx + cardW - pillW - 14f;
            float pillY = qy + cardH - 32f;

            float qpbX = qx + 18f;
            float qpbY = qy + 14f;
            float qpbW = cardW - 155f;

            float btnW = 115f;
            float btnH = 36f;
            float btnX = qx + cardW - btnW - 14f;
            float btnY = qy + 12f;

            // Line 1 Left: Quest Title
            font.setColor(q.claimed ? new Color(0.6f, 0.7f, 0.8f, 0.8f) : new Color(1f, 0.88f, 0.35f, 1f));
            font.getData().setScale(0.88f);
            layout.setText(font, q.title);
            float maxTitleW = pillX - qx - 28f;
            if (layout.width > maxTitleW) {
                font.getData().setScale(0.88f * (maxTitleW / layout.width));
            }
            font.draw(game.batch, q.title, qx + 18f, qy + cardH - 14f);

            // Line 1 Right: Reward Pill Text
            font.setColor(1f, 0.85f, 0.3f, 1f);
            font.getData().setScale(0.76f);
            drawCenteredText(font, q.getRewardSummary(), pillX, pillY, pillW, pillH);

            // Line 2: Description (Clean, concise - NO redundant "Tiến độ mục tiêu" text!)
            font.setColor(0.78f, 0.85f, 0.92f, 0.9f);
            font.getData().setScale(0.78f);
            layout.setText(font, q.description);
            float maxDescW = cardW - 36f;
            if (layout.width > maxDescW) {
                font.getData().setScale(0.78f * (maxDescW / layout.width));
            }
            font.draw(game.batch, q.description, qx + 18f, qy + cardH - 44f);

            // Line 3: Progress count text (raised up so it does not collide with progress bar)
            font.setColor(0.85f, 0.92f, 1f, 0.9f);
            font.getData().setScale(0.72f);
            String progText = q.progress + " / " + q.target;
            layout.setText(font, progText);
            font.draw(game.batch, layout, qpbX + qpbW - layout.width, qpbY + 30f);

            // Line 3 Right: Button Status Label
            font.getData().setScale(0.82f);
            if (q.claimed) {
                font.setColor(0.4f, 0.85f, 0.55f, 1f);
                drawCenteredText(font, "✓ ĐÃ NHẬN", btnX, btnY, btnW, btnH);
            } else if (q.isCompleted()) {
                font.setColor(Color.WHITE);
                drawCenteredText(font, "NHẬN", btnX, btnY, btnW, btnH);
            } else {
                font.setColor(0.7f, 0.82f, 0.95f, 0.85f);
                drawCenteredText(font, "ĐANG LÀM", btnX, btnY, btnW, btnH);
            }
        }
        font.getData().setScale(1f);
    }

    private void drawAttendanceTexts() {
        float startX = 72f;
        float cardW = W - 144f;
        float headerY = H - 212f;
        float headerH = 50f;

        int daysLogged = player.getTotalLoginDaysThisMonth();
        boolean claimedToday = eventManager.hasClaimedLoginToday();

        // Left Header Text
        titleFont.getData().setScale(0.6f);
        titleFont.setColor(1f, 0.85f, 0.3f, 1f);
        titleFont.draw(game.batch, "ĐÃ ĐIỂM DANH: " + daysLogged + " / 25 NGÀY THÁNG NÀY", startX + 25f, headerY + headerH - 12f);
        titleFont.getData().setScale(1f);

        font.setColor(0.85f, 0.88f, 0.95f, 0.85f);
        font.draw(game.batch, "Điểm danh đều đặn để nhận trọn bộ quà & Cần Câu Vệ Quốc tại ngày 25!", startX + 25f, headerY + 18f);

        // Claim Button on Right
        float claimBtnX = startX + cardW - 270f;
        float claimBtnY = headerY + 6f;
        float claimBtnW = 255f;
        float claimBtnH = 38f;

        if (!claimedToday && daysLogged < 25) {
            font.setColor(0.12f, 0.1f, 0.05f, 1f);
            drawCenteredText(font, "ĐIỂM DANH HÔM NAY", claimBtnX, claimBtnY, claimBtnW, claimBtnH);
        } else {
            font.setColor(0.4f, 1f, 0.6f, 1f);
            drawCenteredText(font, "✔ HÔM NAY ĐÃ NHẬN", claimBtnX, claimBtnY, claimBtnW, claimBtnH);
        }

        // 25 Days Grid
        float gridStartX = 72f;
        float gridStartY = H - 296f;
        float boxW = 216f;
        float boxH = 68f;
        float gapX = 14f;
        float gapY = 8f;

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
            font.getData().setScale(0.82f);
            String dayTitle = isClaimed ? ("Ngày " + dayNum + " (Đã nhận)") :
                              (isToday ? ("Ngày " + dayNum + " [HÔM NAY]") :
                              ("Ngày " + dayNum + (reward.isMilestone ? " ★" : "")));
            if (isClaimed) {
                font.setColor(0.4f, 0.85f, 0.55f, 1f);
            } else if (isToday) {
                font.setColor(1f, 0.9f, 0.35f, 1f);
            } else if (reward.isMilestone) {
                font.setColor(1f, 0.8f, 0.25f, 1f);
            } else {
                font.setColor(0.85f, 0.9f, 0.95f, 0.9f);
            }
            font.draw(game.batch, dayTitle, bx + 12f, by + boxH - 12f);

            // Reward Summary text
            font.getData().setScale(0.74f);
            font.setColor(reward.isMilestone ? new Color(1f, 0.9f, 0.45f, 1f) : new Color(0.75f, 0.95f, 0.85f, 1f));
            font.draw(game.batch, reward.rewardSummary, bx + 12f, by + 22f);
        }
        font.getData().setScale(1f);
    }

    private void drawShopTexts() {
        float startX = 72f;
        float cardW = W - 144f;
        float bannerY = H - 222f;
        float bannerH = 58f;

        // Top Banner Title
        titleFont.setColor(1f, 0.88f, 0.35f, 1f);
        titleFont.getData().setScale(0.68f);
        titleFont.draw(game.batch, "BỘ TRANG BỊ GIỚI HẠN: VỆ QUỐC VANG DANH (LIMITED EVENT)", startX + 20f, bannerY + bannerH - 12f);
        titleFont.getData().setScale(1f);

        // Top Banner Subtitle
        font.setColor(1f, 0.92f, 0.75f, 0.95f);
        font.getData().setScale(0.82f);
        font.draw(game.batch, "Kích hoạt hiệu ứng (3 món): +29% Tỉ lệ cá hiếm, Kháng đứt dây +10%!", startX + 20f, bannerY + 22f);

        // Player current points indicator
        font.setColor(1f, 0.85f, 0.35f, 1f);
        font.getData().setScale(0.85f);
        String ptsStr = "Số dư: " + player.getEventPoints() + " Điểm SK";
        layout.setText(font, ptsStr);
        font.draw(game.batch, ptsStr, startX + cardW - layout.width - 20f, bannerY + bannerH / 2f + 6f);
        font.getData().setScale(1f);

        // 6 Shop Cards
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
            "Sức căng +225 • Kháng đứt +4%",
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

            // Title
            font.setColor(getShopItemBorderColor(i));
            font.getData().setScale(0.85f);
            font.draw(game.batch, itemNames[i], ix + 95f, iy + itemH - 18f);

            // Subtitle
            font.setColor(0.7f, 0.75f, 0.85f, 0.85f);
            font.getData().setScale(0.72f);
            font.draw(game.batch, itemSubtitles[i], ix + 95f, iy + itemH - 42f);

            // Stats
            font.setColor(1f, 0.88f, 0.35f, 0.95f);
            font.getData().setScale(0.72f);
            font.draw(game.batch, itemStats[i], ix + 95f, iy + itemH - 64f);

            // Buy button text
            float btnX = ix + 14f;
            float btnY = iy + 14f;
            float btnW = itemW - 28f;
            float btnH = 38f;

            font.getData().setScale(0.82f);
            if (canAfford) {
                font.setColor(0.12f, 0.1f, 0.05f, 1f);
                drawCenteredText(font, "ĐỔI (" + price + " ĐIỂM SK)", btnX, btnY, btnW, btnH);
            } else {
                font.setColor(0.6f, 0.7f, 0.8f, 0.7f);
                drawCenteredText(font, "THIẾU ĐIỂM (" + price + " Điểm)", btnX, btnY, btnW, btnH);
            }
        }
        font.getData().setScale(1f);
    }

    private void drawShopTooltip(int itemIndex) {
        float tipW = 320f;
        float tipH = 145f;
        float tipX = mousePos.x + 15f;
        float tipY = mousePos.y - tipH - 10f;

        if (tipX + tipW > W - 20f) tipX = W - tipW - 20f;
        if (tipY < 20f) tipY = 20f;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(0.06f, 0.09f, 0.15f, 0.98f);
        sr.rect(tipX, tipY, tipW, tipH);
        sr.setColor(1f, 0.4f, 0.45f, 0.85f);
        drawBoxBorder(tipX, tipY, tipW, tipH, 1.8f);
        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();
        float maxTipTextW = tipW - 30f;
        String tipTitle = "";
        String line1 = "", line2 = "", line3 = "";
        String setBonus = "Set Vệ Quốc: +29% Cá hiếm, Kháng đứt +10%";

        if (itemIndex == 0) {
            tipTitle = "Cần Câu Vệ Quốc (LIMITED EVENT)";
            line1 = "• Loại: Cần Câu (Rod) | Cấp độ: Lv.1";
            line2 = "• Sát thương kéo: +20 Sức kéo";
            line3 = "• Thể lực tăng thêm: +125 Điểm";
        } else if (itemIndex == 1) {
            tipTitle = "Dây Cước Vệ Quốc (LIMITED EVENT)";
            line1 = "• Loại: Dây Cước (Line) | Cấp độ: Lv.1";
            line2 = "• Sức căng dây: +225 Điểm";
            line3 = "• Kháng đứt dây: +4.0%";
        } else if (itemIndex == 2) {
            tipTitle = "Lưỡi Câu Vệ Quốc (LIMITED EVENT)";
            line1 = "• Loại: Lưỡi Câu (Hook) | Cấp độ: Lv.1";
            line2 = "• Tỉ lệ cá hiếm: +10.0%";
            line3 = "• Tỉ lệ giữ mồi: +20.0%";
        }

        font.setColor(0.95f, 0.35f, 0.38f, 1f);
        font.getData().setScale(0.88f);
        layout.setText(font, tipTitle);
        if (layout.width > maxTipTextW) {
            font.getData().setScale(0.88f * (maxTipTextW / layout.width));
        }
        font.draw(game.batch, tipTitle, tipX + 15f, tipY + tipH - 18f);

        font.setColor(Color.WHITE);
        font.getData().setScale(0.80f);
        font.draw(game.batch, line1, tipX + 15f, tipY + tipH - 44f);
        font.draw(game.batch, line2, tipX + 15f, tipY + tipH - 68f);
        font.draw(game.batch, line3, tipX + 15f, tipY + tipH - 92f);

        font.setColor(1f, 0.88f, 0.35f, 1f);
        font.getData().setScale(0.80f);
        layout.setText(font, setBonus);
        if (layout.width > maxTipTextW) {
            font.getData().setScale(0.80f * (maxTipTextW / layout.width));
        }
        font.draw(game.batch, setBonus, tipX + 15f, tipY + 28f);

        font.getData().setScale(1f);
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

            // 3. Tab 0: Event Missions & Milestone Click
            if (selectedTab == 0) {
                float mStartX = 65f;
                float containerW = W - 130f;
                float bannerY = H - 225f;

                float mBtnW = 180f;
                float mBtnH = 40f;
                float mBtnX = mStartX + containerW - mBtnW - 14f;
                float mBtnY = bannerY + 9f;

                // Click Milestone Button (Hoàn thành 4/6 để nhận mốc thưởng)
                if (isMouseOver(mBtnX, mBtnY, mBtnW, mBtnH)) {
                    if (eventManager.milestoneClaimedToday) {
                        showMessage("Bạn đã mở rương mốc sự kiện hôm nay rồi!");
                    } else if (eventManager.canClaimMilestone()) {
                        String res = eventManager.claimMilestone();
                        AudioManager.getInstance().playSFX("sfx_success");
                        showMessage(res);
                    } else {
                        showMessage("Hãy hoàn thành ít nhất 4/6 nhiệm vụ để mở mốc thưởng!");
                    }
                    return;
                }

                // Click "Nhận tất cả" Button
                boolean hasUnclaimed = false;
                for (Quest q : eventManager.eventQuests) {
                    if (q.isCompleted() && !q.claimed) {
                        hasUnclaimed = true;
                        break;
                    }
                }
                if (hasUnclaimed) {
                    float caW = 160f;
                    float caH = 40f;
                    float caX = mBtnX - caW - 12f;
                    float caY = mBtnY;
                    if (isMouseOver(caX, caY, caW, caH)) {
                        int claimedCount = 0;
                        for (Quest q : eventManager.eventQuests) {
                            if (q.isCompleted() && !q.claimed) {
                                eventManager.claimQuestReward(q);
                                claimedCount++;
                            }
                        }
                        if (eventManager.canClaimMilestone()) {
                            eventManager.claimMilestone();
                        }
                        AudioManager.getInstance().playSFX("sfx_catch");
                        showMessage("Đã nhận tất cả (" + claimedCount + " nhiệm vụ hoàn thành)!");
                        return;
                    }
                }

                // Click individual quest claim button
                float cardGapX = 18f;
                float cardGapY = 12f;
                float cardW = (containerW - cardGapX) / 2f;
                float cardH = 110f;
                float gridStartY = H - 245f;

                for (int i = 0; i < eventManager.eventQuests.size() && i < 6; i++) {
                    Quest q = eventManager.eventQuests.get(i);
                    int col = i % 2;
                    int row = i / 2;
                    float qx = mStartX + col * (cardW + cardGapX);
                    float qy = gridStartY - row * (cardH + cardGapY) - cardH;

                    float btnW = 115f;
                    float btnH = 36f;
                    float btnX = qx + cardW - btnW - 14f;
                    float btnY = qy + 12f;

                    if (isMouseOver(btnX, btnY, btnW, btnH)) {
                        if (q.claimed) {
                            showMessage("Bạn đã nhận phần thưởng nhiệm vụ này rồi!");
                        } else if (q.isCompleted()) {
                            String res = eventManager.claimQuestReward(q);
                            AudioManager.getInstance().playSFX("sfx_catch");
                            showMessage(res);
                        } else {
                            showMessage("Chưa hoàn thành mục tiêu: " + q.description);
                        }
                        return;
                    }
                }
            }

            // 4. Tab 1: Attendance claim
            if (selectedTab == 1) {
                float cardW = W - 144f;
                float headerY = H - 212f;
                float claimBtnX = 72f + cardW - 270f;
                float claimBtnY = headerY + 6f;
                float claimBtnW = 255f;
                float claimBtnH = 38f;

                boolean clickedClaim = isMouseOver(claimBtnX, claimBtnY, claimBtnW, claimBtnH);

                int daysLogged = player.getTotalLoginDaysThisMonth();
                if (!clickedClaim && !eventManager.hasClaimedLoginToday() && daysLogged < 25) {
                    int todayIdx = daysLogged;
                    int col = todayIdx % 5;
                    int row = todayIdx / 5;
                    float bx = 72f + col * (216f + 14f);
                    float by = H - 296f - row * (68f + 8f);
                    if (isMouseOver(bx, by, 216f, 68f)) {
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

            // 5. Tab 2: Shop Buy buttons
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
                showMessage("Đổi thành công: +5 Vé Quay Tiêu Chuẩn!");
                break;
            case 5:
                player.gainGold(10000);
                showMessage("Đổi thành công: +10,000 Vàng!");
                break;
        }
    }

    private int getShopItemPrice(int index) {
        switch (index) {
            case 0: return 5;
            case 1: return 5;
            case 2: return 5;
            case 3: return 3;
            case 4: return 2;
            case 5: return 1;
            default: return 99;
        }
    }

    private Color getShopItemBorderColor(int index) {
        if (index <= 2) {
            return new Color(0.95f, 0.35f, 0.38f, 1f); // Crimson for limited gear
        } else if (index == 3) {
            return new Color(0.75f, 0.45f, 0.95f, 1f); // Purple
        } else if (index == 4) {
            return new Color(1f, 0.82f, 0.3f, 1f); // Gold
        } else {
            return new Color(0.95f, 0.8f, 0.25f, 1f);
        }
    }

    private void goBack() {
        if (previousScreen != null) {
            game.setScreen(previousScreen);
        } else {
            game.setScreen(new MainMenuScreen(game));
        }
        dispose();
    }

    private boolean isMouseOver(float x, float y, float w, float h) {
        return mousePos.x >= x && mousePos.x <= x + w && mousePos.y >= y && mousePos.y <= y + h;
    }

    private void drawBoxBorder(float x, float y, float w, float h, float thickness) {
        sr.rect(x, y, w, thickness);
        sr.rect(x, y + h - thickness, w, thickness);
        sr.rect(x, y, thickness, h);
        sr.rect(x + w - thickness, y, thickness, h);
    }

    private void drawCenteredText(BitmapFont f, String text, float x, float y, float w, float h) {
        layout.setText(f, text);
        f.draw(game.batch, text, x + (w - layout.width) / 2f, y + (h + layout.height) / 2f);
    }

    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        sr.dispose();
    }
}
