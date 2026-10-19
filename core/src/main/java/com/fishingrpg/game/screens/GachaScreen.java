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
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fishingrpg.game.FishingRPG;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.entities.equipment.Equipment;
import com.fishingrpg.game.entities.equipment.EquipmentGenerator;
import com.fishingrpg.game.entities.equipment.EquipmentType;
import com.fishingrpg.game.entities.equipment.Rarity;
import com.fishingrpg.game.systems.AudioManager;
import com.fishingrpg.game.ui.NotificationUI;

import java.util.ArrayList;
import java.util.List;

public class GachaScreen implements Screen {
    private final FishingRPG game;
    private final Player player;
    private final Screen previousScreen;

    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final ShapeRenderer sr;
    private final BitmapFont font;
    private final BitmapFont titleFont;
    private final GlyphLayout layout;
    
    private static final float W = 1280f;
    private static final float H = 720f;

    private String message = null;
    private float messageTimer = 0f;
    
    private List<Equipment> lastPull = new ArrayList<>();
    
    // Animation States
    private enum State { IDLE, SPINNING, RESULT }
    private State state = State.IDLE;
    private boolean isEventBanner = false;
    
    private float spinTimer = 0f;
    private final float totalSpinTime = 3.0f;
    private List<Equipment> spinItems = new ArrayList<>();
    private Equipment bestItem = null;
    
    // Visual effects
    private float animTimer = 0f;
    private static final int STAR_COUNT = 40;
    private float[] starX = new float[STAR_COUNT];
    private float[] starY = new float[STAR_COUNT];
    private float[] starSize = new float[STAR_COUNT];
    private float[] starSpeed = new float[STAR_COUNT];
    private float[] starAlpha = new float[STAR_COUNT];

    // Tooltip & Hover states
    private boolean showTooltip = false;
    private boolean hoverBack = false;
    private boolean hoverHelp = false;
    private boolean hoverTabNormal = false;
    private boolean hoverTabEvent = false;
    private boolean hoverBuyTicket = false;
    private boolean hoverPull1 = false;
    private boolean hoverPull10 = false;
    private boolean hoverResultDone = false;

    public GachaScreen(FishingRPG game, Screen previousScreen) {
        this.game = game;
        this.player = game.player;
        this.previousScreen = previousScreen;

        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(W, H, camera);
        this.camera.position.set(W / 2f, H / 2f, 0);

        this.sr = new ShapeRenderer();
        this.font = game.font;
        this.titleFont = game.titleFont != null ? game.titleFont : game.font;
        this.layout = new GlyphLayout();

        // Initialize background stars
        for (int i = 0; i < STAR_COUNT; i++) {
            starX[i] = MathUtils.random(0, W);
            starY[i] = MathUtils.random(0, H);
            starSize[i] = MathUtils.random(1.5f, 3.5f);
            starSpeed[i] = MathUtils.random(10f, 30f);
            starAlpha[i] = MathUtils.random(0.3f, 0.8f);
        }
    }

    private void showMessage(String msg) {
        message = msg;
        messageTimer = 2.5f;
    }

    @Override
    public void render(float delta) {
        animTimer += delta;
        if (messageTimer > 0) messageTimer -= delta;
        
        // Update stars
        for (int i = 0; i < STAR_COUNT; i++) {
            starY[i] += starSpeed[i] * delta;
            starX[i] += MathUtils.sin(animTimer + i) * 6f * delta;
            if (starY[i] > H + 10f) {
                starY[i] = -10f;
                starX[i] = MathUtils.random(0, W);
            }
        }

        if (state == State.SPINNING) {
            spinTimer += delta;
            if (spinTimer >= totalSpinTime) {
                spinTimer = totalSpinTime;
                state = State.RESULT;
            }
        }

        handleInput();
        if (game.getScreen() != this) return;

        ScreenUtils.clear(new Color(0.04f, 0.05f, 0.09f, 1f));

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        drawShapes();
        drawBatch();

        // Draw notification toast if active
        NotificationUI.drawNotification(sr, game.batch, font, message, messageTimer, 2.5f, W, H);
    }
    
    private void drawShapes() {
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Cosmic background gradient
        if (!isEventBanner) {
            // Royal Sapphire / Gold theme
            sr.rect(0, 0, W, H,
                new Color(0.02f, 0.04f, 0.10f, 1f), new Color(0.02f, 0.04f, 0.10f, 1f),
                new Color(0.08f, 0.12f, 0.24f, 1f), new Color(0.08f, 0.12f, 0.24f, 1f)
            );
        } else {
            // Lạc Hồng Ruby / Gold theme
            sr.rect(0, 0, W, H,
                new Color(0.08f, 0.02f, 0.04f, 1f), new Color(0.08f, 0.02f, 0.04f, 1f),
                new Color(0.20f, 0.06f, 0.08f, 1f), new Color(0.20f, 0.06f, 0.08f, 1f)
            );
        }

        // Drifting stars
        for (int i = 0; i < STAR_COUNT; i++) {
            float pulse = 0.5f + 0.5f * MathUtils.sin(animTimer * 2f + i);
            if (!isEventBanner) {
                sr.setColor(0.5f, 0.8f, 1f, starAlpha[i] * pulse);
            } else {
                sr.setColor(1f, 0.85f, 0.4f, starAlpha[i] * pulse);
            }
            sr.circle(starX[i], starY[i], starSize[i]);
        }

        // Header Bar
        sr.setColor(0.06f, 0.08f, 0.14f, 0.92f);
        sr.rect(0, H - 72, W, 72);
        sr.setColor(isEventBanner ? new Color(0.95f, 0.35f, 0.25f, 0.7f) : new Color(0.3f, 0.75f, 0.95f, 0.7f));
        sr.rect(0, H - 74, W, 2);

        // Back Button
        sr.setColor(hoverBack ? new Color(0.25f, 0.35f, 0.50f, 0.95f) : new Color(0.12f, 0.18f, 0.26f, 0.9f));
        drawRoundedRect(sr, 25, H - 62, 120, 42, 8);
        sr.setColor(hoverBack ? new Color(0.6f, 0.85f, 1f, 1f) : new Color(0.3f, 0.5f, 0.7f, 0.7f));
        drawRectOutline(sr, 25, H - 62, 120, 42, 1.5f);

        // Rate/Help Button
        sr.setColor(hoverHelp ? new Color(0.25f, 0.35f, 0.50f, 0.95f) : new Color(0.12f, 0.18f, 0.26f, 0.9f));
        drawRoundedRect(sr, 155, H - 62, 110, 42, 8);
        sr.setColor(hoverHelp ? new Color(0.6f, 0.85f, 1f, 1f) : new Color(0.3f, 0.5f, 0.7f, 0.7f));
        drawRectOutline(sr, 155, H - 62, 110, 42, 1.5f);

        // Top Right Resource Dock (Diamonds, Tickets, Premium Tickets)
        float resW = 440f;
        float resH = 46f;
        float resX = W - resW - 25f;
        float resY = H - 64f;
        sr.setColor(0.08f, 0.12f, 0.18f, 0.92f);
        drawRoundedRect(sr, resX, resY, resW, resH, 8);
        sr.setColor(0.25f, 0.45f, 0.65f, 0.8f);
        drawRectOutline(sr, resX, resY, resW, resH, 1.5f);

        // Banner selector tabs (Under header)
        float tabW = 260f;
        float tabH = 44f;
        float tab1X = 280f;
        float tab2X = 560f;
        float tabY = H - 128f;

        // Tab 1: Normal Banner
        boolean tab1Active = !isEventBanner;
        sr.setColor(tab1Active ? new Color(0.15f, 0.35f, 0.65f, 0.95f) : (hoverTabNormal ? new Color(0.12f, 0.22f, 0.35f, 0.9f) : new Color(0.08f, 0.12f, 0.18f, 0.85f)));
        drawRoundedRect(sr, tab1X, tabY, tabW, tabH, 8);
        sr.setColor(tab1Active ? new Color(0.4f, 0.8f, 1f, 1f) : new Color(0.2f, 0.35f, 0.5f, 0.7f));
        drawRectOutline(sr, tab1X, tabY, tabW, tabH, tab1Active ? 2.5f : 1.5f);

        // Tab 2: Event Banner
        boolean tab2Active = isEventBanner;
        sr.setColor(tab2Active ? new Color(0.55f, 0.18f, 0.18f, 0.95f) : (hoverTabEvent ? new Color(0.30f, 0.12f, 0.14f, 0.9f) : new Color(0.08f, 0.12f, 0.18f, 0.85f)));
        drawRoundedRect(sr, tab2X, tabY, tabW, tabH, 8);
        sr.setColor(tab2Active ? new Color(1f, 0.6f, 0.3f, 1f) : new Color(0.4f, 0.25f, 0.25f, 0.7f));
        drawRectOutline(sr, tab2X, tabY, tabW, tabH, tab2Active ? 2.5f : 1.5f);

        // --- IDLE STATE: Main Showcase Banner Card ---
        if (state == State.IDLE) {
            float cardX = 140f;
            float cardY = 120f;
            float cardW = 1000f;
            float cardH = 430f;

            // Card Shadow
            sr.setColor(0f, 0f, 0f, 0.5f);
            drawRoundedRect(sr, cardX + 6, cardY - 6, cardW, cardH, 16);

            // Card Background
            if (!isEventBanner) {
                sr.rect(cardX, cardY, cardW, cardH,
                    new Color(0.08f, 0.12f, 0.20f, 0.96f), new Color(0.08f, 0.12f, 0.20f, 0.96f),
                    new Color(0.12f, 0.18f, 0.30f, 0.96f), new Color(0.12f, 0.18f, 0.30f, 0.96f)
                );
            } else {
                sr.rect(cardX, cardY, cardW, cardH,
                    new Color(0.16f, 0.06f, 0.08f, 0.96f), new Color(0.16f, 0.06f, 0.08f, 0.96f),
                    new Color(0.26f, 0.09f, 0.12f, 0.96f), new Color(0.26f, 0.09f, 0.12f, 0.96f)
                );
            }

            // Card Border
            sr.setColor(isEventBanner ? new Color(0.95f, 0.45f, 0.25f, 0.85f) : new Color(0.35f, 0.75f, 0.95f, 0.85f));
            drawRectOutline(sr, cardX, cardY, cardW, cardH, 2.5f);

            // Left Showcase: Animated Divine Weapon Pedestal & Light rays
            float showcaseCX = cardX + 240f;
            float showcaseCY = cardY + 230f;

            // Rotating Light rays
            for (int r = 0; r < 8; r++) {
                float angle = animTimer * 25f + r * 45f;
                float rad = angle * MathUtils.degreesToRadians;
                float rx = showcaseCX + MathUtils.cos(rad) * 160f;
                float ry = showcaseCY + MathUtils.sin(rad) * 160f;
                sr.setColor(isEventBanner ? new Color(1f, 0.8f, 0.3f, 0.10f) : new Color(0.4f, 0.8f, 1f, 0.10f));
                sr.triangle(showcaseCX, showcaseCY, rx - 20, ry, rx + 20, ry);
            }

            // Pedestal platform
            sr.setColor(0.12f, 0.16f, 0.24f, 0.95f);
            drawRoundedRect(sr, showcaseCX - 120f, showcaseCY - 130f, 240f, 30f, 6);
            sr.setColor(isEventBanner ? new Color(0.95f, 0.75f, 0.25f, 0.9f) : new Color(0.4f, 0.8f, 1f, 0.9f));
            drawRectOutline(sr, showcaseCX - 120f, showcaseCY - 130f, 240f, 30f, 1.5f);

            // Divine weapon shape (Rod + Sacred Ring)
            float bob = MathUtils.sin(animTimer * 2.5f) * 8f;
            float wy = showcaseCY + bob;

            // Sacred Ring
            sr.setColor(isEventBanner ? new Color(0.95f, 0.6f, 0.2f, 0.4f) : new Color(0.3f, 0.7f, 1f, 0.4f));
            sr.circle(showcaseCX, wy, 70f);
            sr.setColor(isEventBanner ? new Color(0.18f, 0.08f, 0.10f, 1f) : new Color(0.08f, 0.14f, 0.22f, 1f));
            sr.circle(showcaseCX, wy, 60f);

            // Rod Staff (Diagonal golden lance)
            sr.setColor(isEventBanner ? new Color(1f, 0.85f, 0.35f, 1f) : new Color(0.6f, 0.9f, 1f, 1f));
            sr.rectLine(showcaseCX - 65, wy - 65, showcaseCX + 65, wy + 65, 6f);
            // Crystal headpiece
            sr.setColor(isEventBanner ? new Color(1f, 0.25f, 0.25f, 1f) : new Color(0.2f, 0.95f, 1f, 1f));
            sr.circle(showcaseCX + 65, wy + 65, 12f);
            sr.setColor(Color.WHITE);
            sr.circle(showcaseCX + 65, wy + 65, 5f);

            // Right Info Section Background Box
            float infoX = cardX + 460f;
            float infoY = cardY + 30f;
            float infoW = cardW - 490f;
            float infoH = cardH - 60f;

            sr.setColor(0.06f, 0.09f, 0.15f, 0.85f);
            drawRoundedRect(sr, infoX, infoY, infoW, infoH, 12);
            sr.setColor(0.2f, 0.35f, 0.5f, 0.6f);
            drawRectOutline(sr, infoX, infoY, infoW, infoH, 1.5f);

            // Pity progress bars
            float barX = infoX + 25f;
            float barW = infoW - 50f;
            float barH = 10f;

            if (!isEventBanner) {
                // Rare Pity Bar (10)
                float rP = MathUtils.clamp((float)player.pityRare / 10f, 0f, 1f);
                sr.setColor(0.08f, 0.12f, 0.18f, 1f);
                drawRoundedRect(sr, barX, infoY + 115f, barW, barH, 4);
                sr.setColor(0.3f, 0.7f, 1f, 1f);
                if (rP > 0) drawRoundedRect(sr, barX + 1, infoY + 116f, (barW - 2) * rP, barH - 2, 3);

                // Epic Pity Bar (50)
                float eP = MathUtils.clamp((float)player.pityEpic / 50f, 0f, 1f);
                sr.setColor(0.08f, 0.12f, 0.18f, 1f);
                drawRoundedRect(sr, barX, infoY + 65f, barW, barH, 4);
                sr.setColor(0.7f, 0.4f, 1f, 1f);
                if (eP > 0) drawRoundedRect(sr, barX + 1, infoY + 66f, (barW - 2) * eP, barH - 2, 3);

                // Legendary Pity Bar (100)
                float lP = MathUtils.clamp((float)player.pityLegendary / 100f, 0f, 1f);
                sr.setColor(0.08f, 0.12f, 0.18f, 1f);
                drawRoundedRect(sr, barX, infoY + 15f, barW, barH, 4);
                sr.setColor(0.95f, 0.75f, 0.25f, 1f);
                if (lP > 0) drawRoundedRect(sr, barX + 1, infoY + 16f, (barW - 2) * lP, barH - 2, 3);
            } else {
                // Limited Pity Bar (80)
                float limP = MathUtils.clamp((float)player.pityLimited / 80f, 0f, 1f);
                sr.setColor(0.08f, 0.12f, 0.18f, 1f);
                drawRoundedRect(sr, barX, infoY + 65f, barW, barH, 4);
                sr.setColor(1f, 0.35f, 0.25f, 1f);
                if (limP > 0) drawRoundedRect(sr, barX + 1, infoY + 66f, (barW - 2) * limP, barH - 2, 3);
            }
        }

        // --- SPINNING STATE: Holographic Roller Bar ---
        if (state == State.SPINNING) {
            float spinX = 140f;
            float spinY = 280f;
            float spinW = 1000f;
            float spinH = 150f;

            // Roller Base
            sr.setColor(0.06f, 0.08f, 0.14f, 0.95f);
            drawRoundedRect(sr, spinX, spinY, spinW, spinH, 14);
            sr.setColor(isEventBanner ? new Color(1f, 0.5f, 0.3f, 0.8f) : new Color(0.4f, 0.8f, 1f, 0.8f));
            drawRectOutline(sr, spinX, spinY, spinW, spinH, 2.5f);

            // Center Target Crosshair / Laser Reticle
            float cx = W / 2f;
            sr.setColor(1f, 0.85f, 0.3f, 0.85f);
            sr.rect(cx - 3, spinY, 6, spinH);
            sr.triangle(cx - 14, spinY + spinH, cx + 14, spinY + spinH, cx, spinY + spinH - 18);
            sr.triangle(cx - 14, spinY, cx + 14, spinY, cx, spinY + 18);
        }

        // --- RESULT STATE: Pull Results Grid ---
        if (state == State.RESULT) {
            float resBoxX = 100f;
            float resBoxY = 110f;
            float resBoxW = 1080f;
            float resBoxH = 450f;

            sr.setColor(0.06f, 0.09f, 0.15f, 0.96f);
            drawRoundedRect(sr, resBoxX, resBoxY, resBoxW, resBoxH, 16);
            sr.setColor(0.35f, 0.55f, 0.8f, 0.8f);
            drawRectOutline(sr, resBoxX, resBoxY, resBoxW, resBoxH, 2f);

            if (lastPull.size() == 1) {
                // Single Pull: 1 Large Card
                Equipment eq = lastPull.get(0);
                float cardW = 320f;
                float cardH = 340f;
                float cardX = W / 2f - cardW / 2f;
                float cardY = resBoxY + 55f;

                // Glowing card back
                sr.setColor(0.10f, 0.14f, 0.22f, 0.98f);
                drawRoundedRect(sr, cardX, cardY, cardW, cardH, 12);
                sr.setColor(eq.rarity.color);
                drawRectOutline(sr, cardX, cardY, cardW, cardH, 3f);

                // Type icon circle
                sr.setColor(eq.rarity.color.cpy().mul(0.3f));
                sr.circle(cardX + cardW / 2f, cardY + cardH - 80f, 40f);
            } else if (lastPull.size() > 1) {
                // 10x Pull: 2 rows of 5 cards
                float cW = 190f;
                float cH = 170f;
                float gapX = 20f;
                float gapY = 20f;
                float gridStartX = resBoxX + (resBoxW - (5 * cW + 4 * gapX)) / 2f;
                float gridStartY = resBoxY + resBoxH - 35f - cH;

                for (int i = 0; i < lastPull.size(); i++) {
                    int row = i / 5;
                    int col = i % 5;
                    float gx = gridStartX + col * (cW + gapX);
                    float gy = gridStartY - row * (cH + gapY);
                    Equipment eq = lastPull.get(i);

                    // Card background
                    sr.setColor(0.09f, 0.12f, 0.18f, 0.95f);
                    drawRoundedRect(sr, gx, gy, cW, cH, 8);

                    // Card border
                    boolean isTopRarity = (bestItem != null && eq.rarity == bestItem.rarity && eq.rarity.ordinal() >= Rarity.EPIC.ordinal());
                    if (isTopRarity) {
                        float p = 0.7f + 0.3f * MathUtils.sin(animTimer * 4f);
                        sr.setColor(eq.rarity.color.r * p, eq.rarity.color.g * p, eq.rarity.color.b * p, 1f);
                        drawRectOutline(sr, gx, gy, cW, cH, 2.5f);
                    } else {
                        sr.setColor(eq.rarity.color);
                        drawRectOutline(sr, gx, gy, cW, cH, 1.5f);
                    }
                }
            }
        }

        // --- BOTTOM ACTION BUTTONS ---
        if (state == State.IDLE || state == State.RESULT) {
            float btnY = 32f;
            float btnH = 54f;

            // Button 1: Buy Ticket (KC Exchange)
            float b1X = 220f;
            float b1W = 240f;
            sr.setColor(hoverBuyTicket ? new Color(0.20f, 0.45f, 0.75f, 0.95f) : new Color(0.12f, 0.28f, 0.48f, 0.9f));
            drawRoundedRect(sr, b1X, btnY, b1W, btnH, 10);
            sr.setColor(hoverBuyTicket ? new Color(0.6f, 0.85f, 1f, 1f) : new Color(0.3f, 0.6f, 0.9f, 0.8f));
            drawRectOutline(sr, b1X, btnY, b1W, btnH, 2f);

            // Button 2: Pull 1x
            float b2X = 490f;
            float b2W = 240f;
            sr.setColor(hoverPull1 ? new Color(0.25f, 0.60f, 0.40f, 0.95f) : new Color(0.15f, 0.42f, 0.28f, 0.9f));
            drawRoundedRect(sr, b2X, btnY, b2W, btnH, 10);
            sr.setColor(hoverPull1 ? new Color(0.6f, 1f, 0.7f, 1f) : new Color(0.3f, 0.85f, 0.5f, 0.8f));
            drawRectOutline(sr, b2X, btnY, b2W, btnH, 2f);

            // Button 3: Pull 10x
            float b3X = 760f;
            float b3W = 280f;
            sr.setColor(hoverPull10 ? new Color(0.85f, 0.50f, 0.18f, 0.95f) : new Color(0.65f, 0.35f, 0.12f, 0.9f));
            drawRoundedRect(sr, b3X, btnY, b3W, btnH, 10);
            sr.setColor(hoverPull10 ? new Color(1f, 0.85f, 0.4f, 1f) : new Color(0.95f, 0.65f, 0.25f, 0.8f));
            drawRectOutline(sr, b3X, btnY, b3W, btnH, 2.5f);
        }

        // Tooltip rates popup
        if (showTooltip) {
            float tipX = 155f;
            float tipY = H - 290f;
            float tipW = 260f;
            float tipH = 210f;
            sr.setColor(0.06f, 0.08f, 0.14f, 0.98f);
            drawRoundedRect(sr, tipX, tipY, tipW, tipH, 10);
            sr.setColor(0.35f, 0.6f, 0.85f, 0.9f);
            drawRectOutline(sr, tipX, tipY, tipW, tipH, 2f);
        }

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void drawBatch() {
        game.batch.begin();

        // Title
        titleFont.setColor(isEventBanner ? new Color(1f, 0.85f, 0.4f, 1f) : new Color(0.95f, 0.85f, 0.45f, 1f));
        layout.setText(titleFont, isEventBanner ? "ĐÀI QUAY SỰ KIỆN: LẠC HỒNG" : "ĐÀI QUAY TRANG BỊ HOÀNG GIA");
        titleFont.draw(game.batch, layout, W / 2f - layout.width / 2f, H - 22f);

        // Subtitle
        font.setColor(0.6f, 0.8f, 0.95f, 0.85f);
        layout.setText(font, isEventBanner ? "Săn tìm Thần Khí Lạc Hồng và các trang bị giới hạn Quốc Khánh" : "Thu thập trang bị câu cá huyền thoại và gia tăng lực kéo vượt bậc");
        font.draw(game.batch, layout, W / 2f - layout.width / 2f, H - 48f);

        // Back & Help Buttons Text
        font.setColor(hoverBack ? Color.WHITE : new Color(0.8f, 0.9f, 1f, 0.9f));
        layout.setText(font, "← QUAY LẠI");
        font.draw(game.batch, layout, 25f + (120f - layout.width) / 2f, H - 36f);

        font.setColor(hoverHelp ? Color.WHITE : new Color(0.8f, 0.9f, 1f, 0.9f));
        layout.setText(font, "TỶ LỆ");
        font.draw(game.batch, layout, 155f + (110f - layout.width) / 2f, H - 36f);

        // Resource Dock Text (Diamonds, Regular Tickets, Premium Tickets)
        float resW = 440f;
        float resX = W - resW - 25f;
        float resY = H - 64f;

        font.getData().setScale(0.85f);
        font.setColor(0.3f, 0.9f, 1f, 1f);
        font.draw(game.batch, "KC: " + player.getDiamonds(), resX + 15f, resY + 30f);

        font.setColor(0.95f, 0.85f, 0.3f, 1f);
        font.draw(game.batch, "Vé Thường: " + player.getGachaTickets(), resX + 130f, resY + 30f);

        font.setColor(1f, 0.5f, 0.35f, 1f);
        font.draw(game.batch, "Vé Cao Cấp: " + player.getPremiumTickets(), resX + 270f, resY + 30f);
        font.getData().setScale(1f);

        // Banner selector tabs text
        float tabW = 260f;
        float tab1X = 280f;
        float tab2X = 560f;
        float tabY = H - 128f;

        font.setColor(!isEventBanner ? Color.WHITE : new Color(0.6f, 0.75f, 0.9f, 0.75f));
        layout.setText(font, "VÒNG QUAY TIÊU CHUẨN");
        font.draw(game.batch, layout, tab1X + (tabW - layout.width) / 2f, tabY + 28f);

        font.setColor(isEventBanner ? Color.WHITE : new Color(0.9f, 0.65f, 0.65f, 0.75f));
        layout.setText(font, "★ SỰ KIỆN QUỐC KHÁNH ★");
        font.draw(game.batch, layout, tab2X + (tabW - layout.width) / 2f, tabY + 28f);

        // --- IDLE STATE TEXTS ---
        if (state == State.IDLE) {
            float cardX = 140f;
            float cardY = 120f;
            float infoX = cardX + 460f;
            float infoY = cardY + 30f;
            float infoW = 1000f - 490f;

            // Weapon Title & Set Name
            titleFont.setColor(isEventBanner ? new Color(1f, 0.4f, 0.3f, 1f) : new Color(0.4f, 0.85f, 1f, 1f));
            String setName = isEventBanner ? "BỘ THẦN KHÍ LẠC HỒNG" : "BỘ HOÀNG GIA ĐẠI DƯƠNG";
            layout.setText(titleFont, setName);
            titleFont.draw(game.batch, layout, infoX + 25f, infoY + 335f);

            // Rarity Tag
            font.setColor(isEventBanner ? new Color(1f, 0.85f, 0.35f, 1f) : new Color(0.95f, 0.45f, 0.95f, 1f));
            String rarTag = isEventBanner ? "✦ PHẨM CHẤT: THẦN THOẠI (GIỚI HẠN)" : "✦ PHẨM CHẤT: TRUYỀN THUYẾT (LEGENDARY)";
            font.draw(game.batch, rarTag, infoX + 25f, infoY + 300f);

            // Set Perk Description
            font.setColor(Color.WHITE);
            String perk1 = isEventBanner ? "• Hiệu ứng 3 Món: [Ý Chí Bất Diệt] Vô hiệu hóa Kỹ Năng Boss" : "• Hiệu ứng 3 Món: +30% Lực Kéo, +20% Tỷ Lệ Bạo Kích";
            String perk2 = isEventBanner ? "• Tăng 50% Lực kéo đối với Boss Thủy Quái & Cá Khổng Lồ" : "• Giảm 25% Tốc độ tăng lực căng dây cước khi cá kéo mạnh";
            font.draw(game.batch, perk1, infoX + 25f, infoY + 265f);
            font.draw(game.batch, perk2, infoX + 25f, infoY + 235f);

            // Pity Header
            font.setColor(0.7f, 0.85f, 1f, 0.9f);
            font.draw(game.batch, "TIẾN TRÌNH BẢO HIỂM MAY MẮN (PITY):", infoX + 25f, infoY + 195f);

            if (!isEventBanner) {
                // Rare Pity
                int remRare = Math.max(0, 10 - player.pityRare);
                font.setColor(0.3f, 0.7f, 1f, 1f);
                font.draw(game.batch, "Bảo hiểm Rare+: Còn " + remRare + " lượt (" + player.pityRare + "/10)", infoX + 25f, infoY + 140f);

                // Epic Pity
                int remEpic = Math.max(0, 50 - player.pityEpic);
                font.setColor(0.7f, 0.4f, 1f, 1f);
                font.draw(game.batch, "Bảo hiểm Epic+: Còn " + remEpic + " lượt (" + player.pityEpic + "/50)", infoX + 25f, infoY + 90f);

                // Legendary Pity
                int remLeg = Math.max(0, 100 - player.pityLegendary);
                font.setColor(0.95f, 0.75f, 0.25f, 1f);
                font.draw(game.batch, "Bảo hiểm Legendary: Còn " + remLeg + " lượt (" + player.pityLegendary + "/100)", infoX + 25f, infoY + 40f);
            } else {
                int remLim = Math.max(0, 80 - player.pityLimited);
                font.setColor(1f, 0.45f, 0.35f, 1f);
                font.draw(game.batch, "Bảo hiểm Thần Khí Lạc Hồng: Còn " + remLim + " lượt (" + player.pityLimited + "/80)", infoX + 25f, infoY + 90f);

                String eventTime = player.getEventManager().getEventTimeRemaining();
                font.setColor(0.95f, 0.85f, 0.4f, 1f);
                font.draw(game.batch, "Thời gian sự kiện còn lại: " + eventTime, infoX + 25f, infoY + 40f);
            }
        }

        // --- RESULT STATE TEXTS ---
        if (state == State.RESULT) {
            float resBoxX = 100f;
            float resBoxY = 110f;
            float resBoxW = 1080f;
            float resBoxH = 450f;

            if (lastPull.size() == 1) {
                Equipment eq = lastPull.get(0);
                float cardW = 320f;
                float cardH = 340f;
                float cardX = W / 2f - cardW / 2f;
                float cardY = resBoxY + 55f;

                titleFont.setColor(eq.rarity.color);
                layout.setText(titleFont, eq.name);
                titleFont.draw(game.batch, layout, cardX + (cardW - layout.width) / 2f, cardY + cardH - 25f);

                font.setColor(0.8f, 0.9f, 1f, 1f);
                String typeStr = "Loại: " + (eq.type == EquipmentType.ROD ? "Cần Câu" : eq.type == EquipmentType.LINE ? "Dây Cước" : "Lưỡi Câu");
                layout.setText(font, typeStr);
                font.draw(game.batch, layout, cardX + (cardW - layout.width) / 2f, cardY + cardH - 135f);

                font.setColor(eq.rarity.color);
                layout.setText(font, "[" + eq.rarity.name() + "]  Bộ: " + eq.setName);
                font.draw(game.batch, layout, cardX + (cardW - layout.width) / 2f, cardY + cardH - 165f);

                // Stats
                font.setColor(0.4f, 1f, 0.6f, 1f);
                String s1 = (eq.type == EquipmentType.ROD ? "Lực Kéo" : eq.type == EquipmentType.LINE ? "Độ Bền" : "Độ Sắc") + ": +" + (int)eq.mainStat1;
                layout.setText(font, s1);
                font.draw(game.batch, layout, cardX + (cardW - layout.width) / 2f, cardY + cardH - 200f);

                // Substats
                font.setColor(0.9f, 0.85f, 0.5f, 1f);
                float subY = cardY + cardH - 235f;
                for (com.fishingrpg.game.entities.equipment.Substat sub : eq.substats) {
                    String subStr = "• " + sub.toString();
                    layout.setText(font, subStr);
                    font.draw(game.batch, layout, cardX + (cardW - layout.width) / 2f, subY);
                    subY -= 25f;
                }
            } else if (lastPull.size() > 1) {
                float cW = 190f;
                float cH = 170f;
                float gapX = 20f;
                float gapY = 20f;
                float gridStartX = resBoxX + (resBoxW - (5 * cW + 4 * gapX)) / 2f;
                float gridStartY = resBoxY + resBoxH - 35f - cH;

                for (int i = 0; i < lastPull.size(); i++) {
                    int row = i / 5;
                    int col = i % 5;
                    float gx = gridStartX + col * (cW + gapX);
                    float gy = gridStartY - row * (cH + gapY);
                    Equipment eq = lastPull.get(i);

                    // Name
                    font.setColor(eq.rarity.color);
                    font.draw(game.batch, eq.name, gx + 8f, gy + cH - 15f, cW - 16f, Align.center, true);

                    // Type & Rarity
                    font.setColor(0.8f, 0.9f, 1f, 0.85f);
                    String typeStr = (eq.type == EquipmentType.ROD ? "Cần" : eq.type == EquipmentType.LINE ? "Cước" : "Lưỡi");
                    font.draw(game.batch, typeStr + " • " + eq.rarity.name(), gx + 8f, gy + cH - 75f, cW - 16f, Align.center, false);

                    // Set Name
                    font.setColor(0.7f, 0.75f, 0.85f, 0.75f);
                    font.draw(game.batch, "Bộ: " + eq.setName, gx + 8f, gy + cH - 105f, cW - 16f, Align.center, false);

                    // Main Stat
                    font.setColor(0.4f, 1f, 0.6f, 1f);
                    String s1 = "+" + (int)eq.mainStat1;
                    font.draw(game.batch, s1, gx + 8f, gy + 30f, cW - 16f, Align.center, false);
                }
            }
        }

        // --- BOTTOM BUTTONS TEXT ---
        if (state == State.IDLE || state == State.RESULT) {
            float btnY = 32f;
            float btnH = 54f;

            // Button 1: Buy Ticket
            float b1X = 220f;
            float b1W = 240f;
            font.setColor(hoverBuyTicket ? Color.WHITE : new Color(0.9f, 0.95f, 1f, 0.95f));
            String buyTxt = isEventBanner ? "ĐỔI VÉ CAO CẤP" : "ĐỔI VÉ QUAY";
            String costTxt = isEventBanner ? "(20 Kim Cương)" : "(10 Kim Cương)";
            layout.setText(font, buyTxt);
            font.draw(game.batch, layout, b1X + (b1W - layout.width) / 2f, btnY + 42f);
            font.setColor(0.4f, 0.9f, 1f, 1f);
            layout.setText(font, costTxt);
            font.draw(game.batch, layout, b1X + (b1W - layout.width) / 2f, btnY + 20f);

            // Button 2: Pull 1x
            float b2X = 490f;
            float b2W = 240f;
            font.setColor(hoverPull1 ? Color.WHITE : new Color(0.9f, 1f, 0.9f, 0.95f));
            layout.setText(font, "QUAY 1 LƯỢT");
            font.draw(game.batch, layout, b2X + (b2W - layout.width) / 2f, btnY + 42f);
            font.setColor(0.7f, 1f, 0.8f, 1f);
            String p1Cost = isEventBanner ? "(1 Vé Cao Cấp)" : "(1 Vé Quay)";
            font.getData().setScale(0.82f);
            layout.setText(font, p1Cost);
            font.draw(game.batch, layout, b2X + (b2W - layout.width) / 2f, btnY + 20f);
            font.getData().setScale(1f);

            // Button 3: Pull 10x
            float b3X = 760f;
            float b3W = 280f;
            font.setColor(hoverPull10 ? Color.WHITE : new Color(1f, 0.95f, 0.85f, 0.95f));
            layout.setText(font, "QUAY 10 LƯỢT");
            font.draw(game.batch, layout, b3X + (b3W - layout.width) / 2f, btnY + 42f);
            font.setColor(1f, 0.85f, 0.4f, 1f);
            String p10Cost = isEventBanner ? "(10 Vé Cao Cấp • Chắc chắn 4★+)" : "(10 Vé Quay • Chắc chắn 4★+)";
            font.getData().setScale(0.82f);
            layout.setText(font, p10Cost);
            font.draw(game.batch, layout, b3X + (b3W - layout.width) / 2f, btnY + 20f);
            font.getData().setScale(1f);
        }

        // Tooltip rates popup text
        if (showTooltip) {
            float tipX = 155f;
            float tipY = H - 290f;
            float tipW = 260f;
            float tipH = 210f;

            font.setColor(0.95f, 0.85f, 0.45f, 1f);
            layout.setText(font, "XÁC SUẤT QUAY");
            font.draw(game.batch, layout, tipX + (tipW - layout.width) / 2f, tipY + tipH - 18f);

            font.setColor(Rarity.COMMON.color);
            font.draw(game.batch, "• Common (1★): 50%", tipX + 20f, tipY + tipH - 52f);
            font.setColor(Rarity.UNCOMMON.color);
            font.draw(game.batch, "• Uncommon (2★): 30%", tipX + 20f, tipY + tipH - 82f);
            font.setColor(Rarity.RARE.color);
            font.draw(game.batch, "• Rare (3★): 15%", tipX + 20f, tipY + tipH - 112f);
            font.setColor(Rarity.EPIC.color);
            font.draw(game.batch, "• Epic (4★): 4%", tipX + 20f, tipY + tipH - 142f);
            font.setColor(Rarity.LEGENDARY.color);
            font.draw(game.batch, "• Legendary (5★): 1%", tipX + 20f, tipY + tipH - 172f);
        }

        game.batch.end();

        // --- DRAW SPINNER SCISSOR (SPINNING STATE) ---
        if (state == State.SPINNING) {
            game.batch.begin();
            game.batch.flush();
            Rectangle clipBounds = new Rectangle(140f, 280f, 1000f, 150f);
            Rectangle scissors = new Rectangle();
            ScissorStack.calculateScissors(camera, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight(), game.batch.getTransformMatrix(), clipBounds, scissors);

            if (ScissorStack.pushScissors(scissors)) {
                float progress = spinTimer / totalSpinTime;
                float easedProgress = Interpolation.pow5Out.apply(progress);

                float itemWidth = 160f;
                float spacing = 30f;
                float totalW = itemWidth + spacing;

                int targetIndex = 65;
                float targetOffset = -(targetIndex * totalW) + (1000f / 2f) - (itemWidth / 2f);
                float currentOffset = 140f + targetOffset * easedProgress;

                for (int i = 0; i < spinItems.size(); i++) {
                    float ix = currentOffset + i * totalW;
                    if (ix > 140f - itemWidth && ix < 1140f) {
                        Equipment eq = spinItems.get(i);
                        font.setColor(eq.rarity.color);
                        font.draw(game.batch, eq.name, ix, 385f, itemWidth, Align.center, true);
                        font.setColor(0.8f, 0.9f, 1f, 0.8f);
                        font.draw(game.batch, eq.rarity.name(), ix, 325f, itemWidth, Align.center, false);
                    }
                }

                game.batch.flush();
                ScissorStack.popScissors();
            }
            game.batch.end();
        }
    }

    private void handleInput() {
        Vector3 m = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(m);
        float mx = m.x;
        float my = m.y;

        // Hover tracking
        hoverBack = (mx >= 25f && mx <= 145f && my >= H - 62f && my <= H - 20f);
        hoverHelp = (mx >= 155f && mx <= 265f && my >= H - 62f && my <= H - 20f);
        showTooltip = hoverHelp;

        hoverTabNormal = (mx >= 280f && mx <= 540f && my >= H - 128f && my <= H - 84f);
        hoverTabEvent = (mx >= 560f && mx <= 820f && my >= H - 128f && my <= H - 84f);

        hoverBuyTicket = (mx >= 220f && mx <= 460f && my >= 32f && my <= 86f);
        hoverPull1 = (mx >= 490f && mx <= 730f && my >= 32f && my <= 86f);
        hoverPull10 = (mx >= 760f && mx <= 1040f && my >= 32f && my <= 86f);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (state == State.SPINNING) {
                spinTimer = totalSpinTime; // Click to skip
            } else {
                AudioManager.getInstance().playSFX("sfx_button");
                game.setScreen(previousScreen);
                dispose();
            }
            return;
        }

        if (Gdx.input.justTouched()) {
            if (state == State.SPINNING) {
                spinTimer = totalSpinTime; // Click anywhere to skip animation
                return;
            }

            if (hoverBack) {
                AudioManager.getInstance().playSFX("sfx_button");
                game.setScreen(previousScreen);
                dispose();
                return;
            }

            if (state == State.IDLE || state == State.RESULT) {
                if (hoverTabNormal && isEventBanner) {
                    AudioManager.getInstance().playSFX("sfx_button");
                    isEventBanner = false;
                    state = State.IDLE;
                    return;
                }
                if (hoverTabEvent && !isEventBanner) {
                    AudioManager.getInstance().playSFX("sfx_button");
                    isEventBanner = true;
                    state = State.IDLE;
                    return;
                }

                if (hoverBuyTicket) {
                    buyTicket();
                    return;
                }
                if (hoverPull1) {
                    pullGacha(1);
                    return;
                }
                if (hoverPull10) {
                    pullGacha(10);
                    return;
                }
            }
        }
    }

    private void buyTicket() {
        if (!isEventBanner) {
            if (player.getDiamonds() >= 10) {
                AudioManager.getInstance().playSFX("sfx_button");
                player.addDiamonds(-10);
                player.addGachaTickets(1);
                showMessage("Đã đổi thành công 1 Vé Quay thường!");
            } else {
                showMessage("Không đủ Kim Cương! Cần 10 Kim Cương.");
            }
        } else {
            if (player.getDiamonds() >= 20) {
                AudioManager.getInstance().playSFX("sfx_button");
                player.addDiamonds(-20);
                player.addPremiumTickets(1);
                showMessage("Đã đổi thành công 1 Vé Quay Cao Cấp!");
            } else {
                showMessage("Không đủ Kim Cương! Cần 20 Kim Cương.");
            }
        }
    }

    private void pullGacha(int amount) {
        if (!isEventBanner) {
            if (player.getGachaTickets() < amount) {
                showMessage("KHÔNG ĐỦ VÉ QUAY! Cần " + amount + " Vé.");
                return;
            }
            AudioManager.getInstance().playSFX("sfx_button");
            player.addGachaTickets(-amount);
        } else {
            if (player.getPremiumTickets() < amount) {
                showMessage("KHÔNG ĐỦ VÉ CAO CẤP! Cần " + amount + " Vé.");
                return;
            }
            AudioManager.getInstance().playSFX("sfx_button");
            player.addPremiumTickets(-amount);
            player.getEventManager().onEventGachaPulled(); // Quest / Mission tracking
        }
        
        player.getQuestManager().addProgress("gacha", amount);
        lastPull.clear();
        bestItem = null;
        
        for (int i = 0; i < amount; i++) {
            boolean guarantee = (amount == 10 && i == 9);
            Equipment eq;
            if (isEventBanner) {
                eq = EquipmentGenerator.rollGachaLimited(player);
            } else {
                eq = EquipmentGenerator.rollGacha(player, guarantee);
            }
            player.addEquipment(eq);
            lastPull.add(eq);
            
            if (bestItem == null || eq.rarity.ordinal() > bestItem.rarity.ordinal()) {
                bestItem = eq;
            }
        }
        
        generateSpinItems();
        state = State.SPINNING;
        spinTimer = 0f;
    }
    
    private void generateSpinItems() {
        spinItems.clear();
        for (int i = 0; i < 70; i++) {
            if (i == 65) {
                spinItems.add(bestItem);
            } else {
                Equipment fake = new Equipment();
                fake.type = EquipmentType.values()[MathUtils.random(0, 2)];
                fake.rarity = Rarity.values()[MathUtils.random(0, Rarity.EPIC.ordinal())];
                fake.name = com.fishingrpg.game.entities.equipment.EquipmentGenerator.getRandomName(fake.type);
                fake.level = player.getLevel();
                spinItems.add(fake);
            }
        }
    }

    private void drawRoundedRect(ShapeRenderer sr, float x, float y, float w, float h, float r) {
        sr.rect(x + r, y, w - 2 * r, h);
        sr.rect(x, y + r, r, h - 2 * r);
        sr.rect(x + w - r, y + r, r, h - 2 * r);
        sr.circle(x + r, y + r, r);
        sr.circle(x + w - r, y + r, r);
        sr.circle(x + r, y + h - r, r);
        sr.circle(x + w - r, y + h - r, r);
    }

    private void drawRectOutline(ShapeRenderer sr, float x, float y, float w, float h, float thickness) {
        sr.rect(x, y, w, thickness);
        sr.rect(x, y + h - thickness, w, thickness);
        sr.rect(x, y, thickness, h);
        sr.rect(x + w - thickness, y, thickness, h);
    }

    @Override
    public void show() {
        AudioManager.getInstance().playBGM("bgm_menu");
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override
    public void dispose() {
        sr.dispose();
    }
}

