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

    public static class StandardSetTheme {
        public String setName;
        public String rodName;
        public String lineName;
        public String hookName;
        public int points;
        public Color primaryColor;
        public Color secondaryColor;
        public Color glowColor;
        public String designType;

        public StandardSetTheme(String setName, String rodName, String lineName, String hookName, int points,
                                Color primaryColor, Color secondaryColor, Color glowColor, String designType) {
            this.setName = setName;
            this.rodName = rodName;
            this.lineName = lineName;
            this.hookName = hookName;
            this.points = points;
            this.primaryColor = primaryColor;
            this.secondaryColor = secondaryColor;
            this.glowColor = glowColor;
            this.designType = designType;
        }
    }

    private static final StandardSetTheme[] STANDARD_SETS = {
        new StandardSetTheme("Đại Dương", "Trượng Biển Khơi", "Sợi Thủy Tinh", "Nanh Thủy Quái", 3,
            new Color(0.25f, 0.75f, 1f, 1f), new Color(0.08f, 0.35f, 0.65f, 1f), new Color(0.3f, 0.9f, 1f, 0.4f), "OCEAN"),
        new StandardSetTheme("Băng Giá", "Cần Câu Băng", "Dây Lạnh Cóng", "Móc Câu Tuyết", 2,
            new Color(0.6f, 0.95f, 1f, 1f), new Color(0.2f, 0.5f, 0.85f, 1f), new Color(0.7f, 1f, 1f, 0.4f), "FROST"),
        new StandardSetTheme("Hải Tặc", "Trụ Cột Buồm", "Xích Neo Nhỏ", "Lưỡi Hái Thép", 4,
            new Color(0.95f, 0.45f, 0.2f, 1f), new Color(0.65f, 0.22f, 0.15f, 1f), new Color(1f, 0.55f, 0.2f, 0.4f), "PIRATE"),
        new StandardSetTheme("Hoàng Gia", "Cần Câu Bạch Kim", "Tơ Tằm Thượng Hạng", "Móc Cẩm Thạch", 6,
            new Color(1f, 0.85f, 0.3f, 1f), new Color(0.55f, 0.25f, 0.75f, 1f), new Color(1f, 0.9f, 0.4f, 0.4f), "ROYAL"),
        new StandardSetTheme("Thời Không", "Trượng Thời Gian", "Sợi Dây Xuyên Thấu", "Lưỡi Câu Hư Không", 6,
            new Color(0.3f, 0.95f, 0.85f, 1f), new Color(0.5f, 0.2f, 0.85f, 1f), new Color(0.4f, 0.9f, 1f, 0.4f), "CHRONO"),
        new StandardSetTheme("Thần Thoại", "Trượng Ánh Sáng", "Dây Chỉ Vàng", "Lưỡi Câu Thần", 7,
            new Color(1f, 0.92f, 0.5f, 1f), new Color(0.95f, 0.65f, 0.15f, 1f), new Color(1f, 0.85f, 0.3f, 0.5f), "MYTHIC"),
        new StandardSetTheme("Vực Thẳm", "Trượng Hắc Ám", "Sợi Hắc Mạch", "Móng Vuốt Vực Sâu", 5,
            new Color(0.75f, 0.35f, 0.95f, 1f), new Color(0.18f, 0.08f, 0.28f, 1f), new Color(0.6f, 0.2f, 0.8f, 0.4f), "ABYSS"),
        new StandardSetTheme("Thợ Săn", "Gậy Phục Kích", "Sợi Tơ Kẽm", "Móng Vuốt Biển", 1,
            new Color(0.4f, 0.9f, 0.45f, 1f), new Color(0.2f, 0.5f, 0.25f, 1f), new Color(0.4f, 1f, 0.5f, 0.35f), "HUNTER"),
        new StandardSetTheme("Tập Sự", "Cần Câu Tre", "Dây Cước Thường", "Lưỡi Câu Chì", 0,
            new Color(0.85f, 0.72f, 0.45f, 1f), new Color(0.45f, 0.65f, 0.3f, 1f), new Color(0.9f, 0.8f, 0.5f, 0.35f), "APPRENTICE")
    };

    private int currentStandardSetIndex = 0;

    public void randomizeStandardSet() {
        if (STANDARD_SETS.length <= 1) return;
        int next;
        do {
            next = MathUtils.random(0, STANDARD_SETS.length - 1);
        } while (next == currentStandardSetIndex);
        currentStandardSetIndex = next;
    }

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

        randomizeStandardSet();
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

        // Draw Rate Tooltip Overlay (guaranteed on top and offset to avoid tab overlap)
        drawRateTooltipOverlay();

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

        // Banner selector tabs (Centered)
        float tabW = 260f;
        float tabH = 44f;
        float gap = 20f;
        float startTabX = (W - (2 * tabW + gap)) / 2f; // 370f
        float tab1X = startTabX;
        float tab2X = startTabX + tabW + gap;
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

            StandardSetTheme curTheme = STANDARD_SETS[currentStandardSetIndex];

            // Rotating Light rays matching current set theme
            for (int r = 0; r < 8; r++) {
                float angle = animTimer * 25f + r * 45f;
                float rad = angle * MathUtils.degreesToRadians;
                float rx = showcaseCX + MathUtils.cos(rad) * 160f;
                float ry = showcaseCY + MathUtils.sin(rad) * 160f;
                Color rayCol = isEventBanner ? new Color(1f, 0.8f, 0.3f, 0.10f) :
                    new Color(curTheme.primaryColor.r, curTheme.primaryColor.g, curTheme.primaryColor.b, 0.12f);
                sr.setColor(rayCol);
                sr.triangle(showcaseCX, showcaseCY, rx - 20, ry, rx + 20, ry);
            }

            // Pedestal platform
            sr.setColor(0.12f, 0.16f, 0.24f, 0.95f);
            drawRoundedRect(sr, showcaseCX - 120f, showcaseCY - 130f, 240f, 30f, 6);
            sr.setColor(isEventBanner ? new Color(0.95f, 0.75f, 0.25f, 0.9f) : curTheme.primaryColor);
            drawRectOutline(sr, showcaseCX - 120f, showcaseCY - 130f, 240f, 30f, 1.5f);

            // Animated weapon emblem for current set
            drawShowcaseEmblem(showcaseCX, showcaseCY);

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
            float barH = 12f;

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
                drawRoundedRect(sr, barX, infoY + 72f, barW, barH, 4);
                sr.setColor(0.7f, 0.4f, 1f, 1f);
                if (eP > 0) drawRoundedRect(sr, barX + 1, infoY + 73f, (barW - 2) * eP, barH - 2, 3);

                // Legendary Pity Bar (100)
                float lP = MathUtils.clamp((float)player.pityLegendary / 100f, 0f, 1f);
                sr.setColor(0.08f, 0.12f, 0.18f, 1f);
                drawRoundedRect(sr, barX, infoY + 28f, barW, barH, 4);
                sr.setColor(0.95f, 0.75f, 0.25f, 1f);
                if (lP > 0) drawRoundedRect(sr, barX + 1, infoY + 29f, (barW - 2) * lP, barH - 2, 3);
            } else {
                // Limited Pity Bar (80)
                float limP = MathUtils.clamp((float)player.pityLimited / 80f, 0f, 1f);
                sr.setColor(0.08f, 0.12f, 0.18f, 1f);
                drawRoundedRect(sr, barX, infoY + 74f, barW, barH, 4);
                sr.setColor(1f, 0.35f, 0.25f, 1f);
                if (limP > 0) drawRoundedRect(sr, barX + 1, infoY + 75f, (barW - 2) * limP, barH - 2, 3);
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

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void drawBatch() {
        game.batch.begin();

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

        // Center Title & Subtitle strictly inside available space (between Rate btn at 265f and Resource Dock at resX)
        float centerAreaX = 275f;
        float centerAreaW = resX - centerAreaX - 15f; // ~525px

        titleFont.setColor(isEventBanner ? new Color(1f, 0.85f, 0.4f, 1f) : new Color(0.95f, 0.85f, 0.45f, 1f));
        titleFont.getData().setScale(0.82f);
        String bannerTitle = isEventBanner ? "ĐÀI QUAY SỰ KIỆN: LẠC HỒNG" : "ĐÀI QUAY TRANG BỊ HOÀNG GIA";
        layout.setText(titleFont, bannerTitle);
        if (layout.width > centerAreaW) {
            titleFont.getData().setScale(0.82f * (centerAreaW / layout.width));
        }
        titleFont.draw(game.batch, bannerTitle, centerAreaX + (centerAreaW - layout.width) / 2f, H - 20f);
        titleFont.getData().setScale(1f);

        // Subtitle
        font.setColor(0.6f, 0.8f, 0.95f, 0.85f);
        font.getData().setScale(0.75f);
        String subTitle = isEventBanner ? "Săn tìm Thần Khí Lạc Hồng và trang bị giới hạn Quốc Khánh" : "Thu thập trang bị câu cá huyền thoại và gia tăng lực kéo vượt bậc";
        layout.setText(font, subTitle);
        if (layout.width > centerAreaW) {
            font.getData().setScale(0.75f * (centerAreaW / layout.width));
        }
        font.draw(game.batch, subTitle, centerAreaX + (centerAreaW - layout.width) / 2f, H - 48f);
        font.getData().setScale(1f);

        // Banner selector tabs text (Centered)
        float tabW = 260f;
        float gap = 20f;
        float startTabX = (W - (2 * tabW + gap)) / 2f;
        float tab1X = startTabX;
        float tab2X = startTabX + tabW + gap;
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

            if (!isEventBanner) {
                StandardSetTheme curSet = STANDARD_SETS[currentStandardSetIndex];

                // Weapon Title & Set Name
                titleFont.setColor(curSet.primaryColor);
                String setName = "BỘ " + curSet.setName.toUpperCase();
                layout.setText(titleFont, setName);
                titleFont.draw(game.batch, layout, infoX + 25f, infoY + 340f);

                // Set Tag (NO FIXED RARITY)
                font.setColor(new Color(0.85f, 0.9f, 1f, 0.95f));
                font.getData().setScale(0.82f);
                String setTag = "✦ THIẾT KẾ ĐẶC QUYỀN • BỘ 3 MÓN ĐỒNG BỘ";
                font.draw(game.batch, setTag, infoX + 25f, infoY + 308f);

                // Equipment Items in Set
                font.setColor(0.7f, 0.85f, 1f, 1f);
                font.getData().setScale(0.78f);
                String itemsStr = "• Gồm: " + curSet.rodName + "  |  " + curSet.lineName + "  |  " + curSet.hookName;
                font.draw(game.batch, itemsStr, infoX + 25f, infoY + 280f);

                // Set Bonus Effect
                font.setColor(Color.WHITE);
                font.getData().setScale(0.82f);
                String bonusDesc = "• Hiệu ứng: " + EquipmentGenerator.getSetBonusDesc(curSet.setName);
                font.draw(game.batch, bonusDesc, infoX + 25f, infoY + 252f);

                // Multiplier / Set Points
                font.setColor(1f, 0.85f, 0.35f, 1f);
                float multVal = 1f + curSet.points * 0.15f;
                String multDesc = "• Hệ số Bộ (Multiplier): +" + curSet.points + " Cấp Điểm (x" + String.format(java.util.Locale.US, "%.2f", multVal) + " Sức mạnh cơ bản)";
                font.draw(game.batch, multDesc, infoX + 25f, infoY + 224f);

                font.setColor(0.5f, 0.9f, 0.65f, 1f);
                font.getData().setScale(0.74f);
                font.draw(game.batch, "(Phẩm chất trang bị khi quay là ngẫu nhiên, chỉ số nhân theo phẩm chất và hệ số bộ này)", infoX + 25f, infoY + 196f);
                font.getData().setScale(1f);

                // Pity Header
                font.setColor(0.7f, 0.85f, 1f, 0.9f);
                font.getData().setScale(0.85f);
                font.draw(game.batch, "TIẾN TRÌNH BẢO HIỂM MAY MẮN (PITY):", infoX + 25f, infoY + 168f);

                // Rare Pity
                int remRare = Math.max(0, 10 - player.pityRare);
                font.setColor(0.3f, 0.7f, 1f, 1f);
                font.draw(game.batch, "Bảo hiểm Rare+: Còn " + remRare + " lượt (" + player.pityRare + "/10)", infoX + 25f, infoY + 138f);

                // Epic Pity
                int remEpic = Math.max(0, 50 - player.pityEpic);
                font.setColor(0.7f, 0.4f, 1f, 1f);
                font.draw(game.batch, "Bảo hiểm Epic+: Còn " + remEpic + " lượt (" + player.pityEpic + "/50)", infoX + 25f, infoY + 95f);

                // Legendary Pity
                int remLeg = Math.max(0, 100 - player.pityLegendary);
                font.setColor(0.95f, 0.75f, 0.25f, 1f);
                font.draw(game.batch, "Bảo hiểm Legendary: Còn " + remLeg + " lượt (" + player.pityLegendary + "/100)", infoX + 25f, infoY + 52f);
            } else {
                // Weapon Title & Set Name
                titleFont.setColor(new Color(1f, 0.4f, 0.3f, 1f));
                String setName = "BỘ THẦN KHÍ LẠC HỒNG";
                layout.setText(titleFont, setName);
                titleFont.draw(game.batch, layout, infoX + 25f, infoY + 335f);

                // Rarity Tag
                font.setColor(new Color(1f, 0.85f, 0.35f, 1f));
                String rarTag = "✦ PHẨM CHẤT: THẦN THOẠI (GIỚI HẠN)";
                font.draw(game.batch, rarTag, infoX + 25f, infoY + 300f);

                // Set Perk Description
                font.setColor(Color.WHITE);
                font.getData().setScale(0.80f);
                String perk1 = "• Hiệu ứng 3 Món: [Ý Chí Bất Diệt] Vô hiệu hóa Kỹ Năng Boss";
                String perk2 = "• Tăng 50% Lực kéo đối với Boss Thủy Quái & Cá Khổng Lồ";
                font.draw(game.batch, perk1, infoX + 25f, infoY + 265f);
                font.draw(game.batch, perk2, infoX + 25f, infoY + 235f);
                font.getData().setScale(1f);

                // Pity Header
                font.setColor(0.7f, 0.85f, 1f, 0.9f);
                font.getData().setScale(0.85f);
                font.draw(game.batch, "TIẾN TRÌNH BẢO HIỂM MAY MẮN (PITY):", infoX + 25f, infoY + 195f);

                int remLim = Math.max(0, 80 - player.pityLimited);
                font.setColor(1f, 0.45f, 0.35f, 1f);
                font.draw(game.batch, "Bảo hiểm Thần Khí Lạc Hồng: Còn " + remLim + " lượt (" + player.pityLimited + "/80)", infoX + 25f, infoY + 100f);

                String eventTime = player.getEventManager().getEventTimeRemaining();
                font.setColor(0.95f, 0.85f, 0.4f, 1f);
                font.draw(game.batch, "Thời gian sự kiện còn lại: " + eventTime, infoX + 25f, infoY + 52f);
            }
            font.getData().setScale(1f);
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

        hoverTabNormal = (mx >= 370f && mx <= 630f && my >= H - 128f && my <= H - 84f);
        hoverTabEvent = (mx >= 650f && mx <= 910f && my >= H - 128f && my <= H - 84f);

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
                if (hoverTabNormal) {
                    AudioManager.getInstance().playSFX("sfx_button");
                    isEventBanner = false;
                    state = State.IDLE;
                    randomizeStandardSet();
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

    private void drawShowcaseEmblem(float cx, float cy) {
        float bob = MathUtils.sin(animTimer * 2.5f) * 8f;
        float wy = cy + bob;

        if (isEventBanner) {
            // Sacred Ring
            sr.setColor(0.95f, 0.6f, 0.2f, 0.4f);
            sr.circle(cx, wy, 70f);
            sr.setColor(0.18f, 0.08f, 0.10f, 1f);
            sr.circle(cx, wy, 60f);

            // Rod Staff (Diagonal golden lance)
            sr.setColor(1f, 0.85f, 0.35f, 1f);
            sr.rectLine(cx - 65, wy - 65, cx + 65, wy + 65, 6f);
            // Crystal headpiece
            sr.setColor(1f, 0.25f, 0.25f, 1f);
            sr.circle(cx + 65, wy + 65, 12f);
            sr.setColor(Color.WHITE);
            sr.circle(cx + 65, wy + 65, 5f);
            return;
        }

        StandardSetTheme theme = STANDARD_SETS[currentStandardSetIndex];
        
        // Aura / Halo Ring
        sr.setColor(theme.glowColor);
        sr.circle(cx, wy, 72f);
        sr.setColor(0.08f, 0.12f, 0.18f, 1f);
        sr.circle(cx, wy, 60f);

        // Core Weapon Geometry based on designType
        switch (theme.designType) {
            case "OCEAN": // Oceanic Trident
                sr.setColor(theme.primaryColor);
                sr.rectLine(cx - 60f, wy - 60f, cx + 55f, wy + 55f, 5.5f);
                // Trident prongs
                sr.rectLine(cx + 35f, wy + 65f, cx + 65f, wy + 35f, 4f);
                sr.rectLine(cx + 45f, wy + 75f, cx + 75f, wy + 45f, 3.5f);
                sr.circle(cx + 65f, wy + 65f, 9f);
                sr.setColor(theme.secondaryColor);
                sr.circle(cx + 65f, wy + 65f, 4.5f);
                break;

            case "FROST": // Glacial Ice Spear
                sr.setColor(theme.primaryColor);
                sr.rectLine(cx - 65f, wy - 65f, cx + 65f, wy + 65f, 6f);
                // Hexagonal Ice crystal flakes
                for (int f = 0; f < 6; f++) {
                    float fAng = animTimer * 20f + f * 60f;
                    float fx = cx + MathUtils.cosDeg(fAng) * 45f;
                    float fy = wy + MathUtils.sinDeg(fAng) * 45f;
                    sr.setColor(theme.glowColor);
                    sr.circle(fx, fy, 6f);
                }
                sr.setColor(Color.WHITE);
                sr.circle(cx + 65f, wy + 65f, 10f);
                sr.setColor(theme.primaryColor);
                sr.circle(cx + 65f, wy + 65f, 5f);
                break;

            case "PIRATE": // Anchor & Cutlass
                sr.setColor(theme.secondaryColor);
                sr.circle(cx - 30f, wy - 30f, 22f);
                sr.setColor(0.08f, 0.12f, 0.18f, 1f);
                sr.circle(cx - 30f, wy - 30f, 15f);
                sr.setColor(theme.primaryColor);
                sr.rectLine(cx - 50f, wy - 50f, cx + 70f, wy + 70f, 7f);
                sr.setColor(1f, 0.85f, 0.3f, 1f);
                sr.rectLine(cx + 25f, wy + 45f, cx + 45f, wy + 25f, 6f);
                sr.setColor(theme.primaryColor);
                sr.circle(cx + 70f, wy + 70f, 11f);
                break;

            case "ROYAL": // Platinum Crown Scepter
                sr.setColor(theme.secondaryColor);
                sr.rectLine(cx - 65f, wy - 65f, cx + 60f, wy + 60f, 6.5f);
                // Golden Crown Head
                sr.setColor(theme.primaryColor);
                sr.circle(cx + 60f, wy + 60f, 16f);
                sr.setColor(theme.secondaryColor);
                sr.circle(cx + 60f, wy + 60f, 10f);
                sr.setColor(Color.WHITE);
                sr.circle(cx + 60f, wy + 60f, 5f);
                // Filigree Gems
                sr.setColor(1f, 0.9f, 0.4f, 1f);
                sr.circle(cx + 45f, wy + 75f, 4f);
                sr.circle(cx + 75f, wy + 45f, 4f);
                break;

            case "CHRONO": // Orbital Chrono Dial
                sr.setColor(theme.primaryColor);
                float rot = animTimer * 45f;
                // Double dial ring
                sr.setColor(theme.secondaryColor);
                sr.circle(cx, wy, 45f);
                sr.setColor(0.08f, 0.12f, 0.18f, 1f);
                sr.circle(cx, wy, 38f);
                // Clock hands
                sr.setColor(theme.primaryColor);
                sr.rectLine(cx, wy, cx + MathUtils.cosDeg(rot) * 35f, wy + MathUtils.sinDeg(rot) * 35f, 4f);
                sr.rectLine(cx, wy, cx - MathUtils.sinDeg(rot * 0.5f) * 25f, wy + MathUtils.cosDeg(rot * 0.5f) * 25f, 3.5f);
                sr.circle(cx, wy, 7f);
                break;

            case "MYTHIC": // Solar Sunburst Staff
                sr.setColor(theme.primaryColor);
                for (int s = 0; s < 8; s++) {
                    float sAng = animTimer * 30f + s * 45f;
                    float sx = cx + MathUtils.cosDeg(sAng) * 50f;
                    float sy = wy + MathUtils.sinDeg(sAng) * 50f;
                    sr.triangle(cx, wy, sx - 8, sy - 8, sx + 8, sy + 8);
                }
                sr.rectLine(cx - 65f, wy - 65f, cx + 60f, wy + 60f, 6.5f);
                sr.setColor(Color.WHITE);
                sr.circle(cx + 60f, wy + 60f, 12f);
                sr.setColor(theme.secondaryColor);
                sr.circle(cx + 60f, wy + 60f, 6f);
                break;

            case "ABYSS": // Abyssal Scythe
                sr.setColor(theme.primaryColor);
                sr.rectLine(cx - 50f, wy - 70f, cx + 45f, wy + 55f, 6f);
                // Scythe Curved Blade
                sr.triangle(cx + 45f, wy + 55f, cx + 75f, wy + 70f, cx + 25f, wy + 75f);
                sr.triangle(cx + 75f, wy + 70f, cx + 85f, wy + 50f, cx + 45f, wy + 55f);
                sr.setColor(1f, 0.2f, 0.3f, 1f);
                sr.circle(cx + 45f, wy + 55f, 6f);
                break;

            case "HUNTER": // Hunter Javelin & Barbs
                sr.setColor(theme.secondaryColor);
                sr.rectLine(cx - 70f, wy - 70f, cx + 65f, wy + 65f, 6f);
                sr.setColor(theme.primaryColor);
                // Harpoon head
                sr.triangle(cx + 65f, wy + 65f, cx + 50f, wy + 80f, cx + 80f, wy + 50f);
                sr.setColor(0.2f, 0.9f, 0.4f, 1f);
                sr.circle(cx + 65f, wy + 65f, 5f);
                break;

            case "APPRENTICE": // Bamboo Sage Rod
            default:
                sr.setColor(theme.primaryColor);
                sr.rectLine(cx - 65f, wy - 65f, cx + 60f, wy + 60f, 5f);
                // Rings on bamboo
                sr.setColor(theme.secondaryColor);
                sr.circle(cx - 20f, wy - 20f, 6f);
                sr.circle(cx + 20f, wy + 20f, 6f);
                // Red & White float bobber
                sr.setColor(1f, 0.25f, 0.25f, 1f);
                sr.circle(cx + 60f, wy + 60f, 10f);
                sr.setColor(Color.WHITE);
                sr.circle(cx + 60f, wy + 60f, 5f);
                break;
        }
    }

    private void drawRateTooltipOverlay() {
        if (!showTooltip) return;

        // Positioned at X: 55px (Width 270px -> ends at 325px, safely away from tab button at 370px!)
        float tipX = 55f;
        float tipY = H - 295f;
        float tipW = 270f;
        float tipH = 215f;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        
        // Shadow
        sr.setColor(0f, 0f, 0f, 0.65f);
        drawRoundedRect(sr, tipX + 5f, tipY - 5f, tipW, tipH, 10);

        // Background
        sr.setColor(0.06f, 0.09f, 0.16f, 0.98f);
        drawRoundedRect(sr, tipX, tipY, tipW, tipH, 10);

        // Header accent bar
        sr.setColor(0.12f, 0.18f, 0.28f, 0.9f);
        sr.rect(tipX + 2, tipY + tipH - 32f, tipW - 4, 30f);

        // Border
        sr.setColor(0.4f, 0.75f, 1f, 0.9f);
        drawRectOutline(sr, tipX, tipY, tipW, tipH, 2f);
        sr.end();

        game.batch.begin();
        font.setColor(0.95f, 0.85f, 0.45f, 1f);
        layout.setText(font, "XÁC SUẤT QUAY");
        font.draw(game.batch, layout, tipX + (tipW - layout.width) / 2f, tipY + tipH - 10f);

        font.setColor(Rarity.COMMON.color);
        font.draw(game.batch, "• Common (1★): 50%", tipX + 20f, tipY + tipH - 46f);
        font.setColor(Rarity.UNCOMMON.color);
        font.draw(game.batch, "• Uncommon (2★): 30%", tipX + 20f, tipY + tipH - 76f);
        font.setColor(Rarity.RARE.color);
        font.draw(game.batch, "• Rare (3★): 15%", tipX + 20f, tipY + tipH - 106f);
        font.setColor(Rarity.EPIC.color);
        font.draw(game.batch, "• Epic (4★): 4%", tipX + 20f, tipY + tipH - 136f);
        font.setColor(Rarity.LEGENDARY.color);
        font.draw(game.batch, "• Legendary (5★): 1%", tipX + 20f, tipY + tipH - 166f);
        game.batch.end();
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

