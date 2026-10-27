package com.fishingrpg.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
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
import com.fishingrpg.game.ui.EquipmentIconRenderer;
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
    private static final int STAR_COUNT = 45;
    private float[] starX = new float[STAR_COUNT];
    private float[] starY = new float[STAR_COUNT];
    private float[] starSize = new float[STAR_COUNT];
    private float[] starSpeed = new float[STAR_COUNT];
    private float[] starAlpha = new float[STAR_COUNT];

    // Modals & Hover states
    private boolean showRateLootModal = false;
    private boolean hoverBack = false;
    private boolean hoverHelp = false;
    private boolean hoverTabNormal = false;
    private boolean hoverTabEvent = false;
    private boolean hoverBuyTicket = false;
    private boolean hoverPull1 = false;
    private boolean hoverPull10 = false;
    private boolean hoverSet = false;
    private boolean showSetDetailModal = false;
    private boolean hoverCloseDetailModal = false;
    private boolean hoverCloseModal = false;
    private float modalLootScrollY = 0f;
    private boolean hoverModalScrollUp = false;
    private boolean hoverModalScrollDown = false;
    private float mouseX = 0f;
    private float mouseY = 0f;
    private final Vector3 mousePos = new Vector3();

    private static final Color BG_NORM_BOT = new Color(0.02f, 0.04f, 0.10f, 1f);
    private static final Color BG_NORM_TOP = new Color(0.08f, 0.12f, 0.24f, 1f);
    private static final Color BG_EVT_BOT = new Color(0.08f, 0.02f, 0.04f, 1f);
    private static final Color BG_EVT_TOP = new Color(0.22f, 0.06f, 0.08f, 1f);

    // Centered dock and button element coordinates
    private float dockDiaIconX, dockDiaTxtX;
    private float dockNormIconX, dockNormTxtX;
    private float dockPremIconX, dockPremTxtX;
    private float btn1IconX, btn1TxtX;
    private float btn2IconX, btn2TxtX;
    private float btn3IconX, btn3TxtX;

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
        new StandardSetTheme("Quang Minh", "Trượng Ánh Sáng", "Dây Chỉ Vàng", "Lưỡi Câu Thần", 7,
            new Color(1f, 0.92f, 0.45f, 1f), new Color(0.95f, 0.65f, 0.15f, 1f), new Color(1f, 0.85f, 0.3f, 0.5f), "MYTHIC"),
        new StandardSetTheme("Hoàng Gia", "Cần Câu Bạch Kim", "Tơ Tằm Thượng Hạng", "Móc Cẩm Thạch", 6,
            new Color(1f, 0.85f, 0.3f, 1f), new Color(0.55f, 0.25f, 0.75f, 1f), new Color(1f, 0.9f, 0.4f, 0.4f), "ROYAL"),
        new StandardSetTheme("Thời Không", "Trượng Thời Gian", "Sợi Dây Xuyên Thấu", "Lưỡi Câu Hư Không", 6,
            new Color(0.3f, 0.95f, 0.85f, 1f), new Color(0.5f, 0.2f, 0.85f, 1f), new Color(0.4f, 0.9f, 1f, 0.4f), "CHRONO"),
        new StandardSetTheme("Vực Thẳm", "Trượng Hắc Ám", "Sợi Hắc Mạch", "Móng Vuốt Vực Sâu", 5,
            new Color(0.75f, 0.35f, 0.95f, 1f), new Color(0.18f, 0.08f, 0.28f, 1f), new Color(0.6f, 0.2f, 0.8f, 0.4f), "ABYSS"),
        new StandardSetTheme("Hải Tặc", "Trụ Cột Buồm", "Xích Neo Nhỏ", "Lưỡi Hái Thép", 4,
            new Color(0.95f, 0.45f, 0.2f, 1f), new Color(0.65f, 0.22f, 0.15f, 1f), new Color(1f, 0.55f, 0.2f, 0.4f), "PIRATE"),
        new StandardSetTheme("Đại Dương", "Trượng Biển Khơi", "Sợi Thủy Tinh", "Nanh Thủy Quái", 3,
            new Color(0.25f, 0.75f, 1f, 1f), new Color(0.08f, 0.35f, 0.65f, 1f), new Color(0.3f, 0.9f, 1f, 0.4f), "OCEAN"),
        new StandardSetTheme("Băng Giá", "Cần Câu Băng", "Dây Lạnh Cóng", "Móc Câu Tuyết", 2,
            new Color(0.6f, 0.95f, 1f, 1f), new Color(0.2f, 0.5f, 0.85f, 1f), new Color(0.7f, 1f, 1f, 0.4f), "FROST"),
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

        this.sr = game.shapeRenderer;
        this.font = game.font;
        this.titleFont = game.titleFont != null ? game.titleFont : game.font;
        this.layout = new GlyphLayout();

        // Initialize background stars
        for (int i = 0; i < STAR_COUNT; i++) {
            starX[i] = MathUtils.random(0, W);
            starY[i] = MathUtils.random(0, H);
            starSize[i] = MathUtils.random(1.5f, 3.5f);
            starSpeed[i] = MathUtils.random(10f, 28f);
            starAlpha[i] = MathUtils.random(0.3f, 0.85f);
        }

        randomizeStandardSet();
    }

    private void showMessage(String msg) {
        message = msg;
        messageTimer = 2.5f;
    }

    @Override
    public void render(float delta) {
        if (Gdx.graphics.getWidth() <= 0 || Gdx.graphics.getHeight() <= 0) return;
        animTimer += delta;
        if (messageTimer > 0) messageTimer -= delta;
        
        // Update stars
        for (int i = 0; i < STAR_COUNT; i++) {
            starY[i] += starSpeed[i] * delta;
            starX[i] += MathUtils.sin(animTimer + i) * 5f * delta;
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

        ScreenUtils.clear(0.04f, 0.05f, 0.09f, 1f);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Pre-calculate centered icon & text coordinates for top dock
        float resW = 345f;
        float resH = 46f;
        float resX = W - resW - 25f;
        float secW = resW / 3f;

        font.getData().setScale(0.88f);
        // Section 0: Diamonds
        String diaStr = String.valueOf(player.getDiamonds());
        layout.setText(font, diaStr);
        float diaGroupW = 16f + 7f + layout.width;
        float diaStartX = resX + (secW - diaGroupW) / 2f;
        dockDiaIconX = diaStartX + 8f;
        dockDiaTxtX = diaStartX + 16f + 7f;

        // Section 1: Normal Tickets
        String normStr = String.valueOf(player.getGachaTickets());
        layout.setText(font, normStr);
        float normGroupW = 24f + 7f + layout.width;
        float normStartX = resX + secW + (secW - normGroupW) / 2f;
        dockNormIconX = normStartX + 12f;
        dockNormTxtX = normStartX + 24f + 7f;

        // Section 2: Premium Tickets
        String premStr = String.valueOf(player.getPremiumTickets());
        layout.setText(font, premStr);
        float premGroupW = 24f + 7f + layout.width;
        float premStartX = resX + secW * 2f + (secW - premGroupW) / 2f;
        dockPremIconX = premStartX + 12f;
        dockPremTxtX = premStartX + 24f + 7f;

        // Pre-calculate centered icon & text coordinates for bottom action buttons
        // Button 1: Buy Ticket
        String costTxt = isEventBanner ? "20" : "10";
        font.getData().setScale(1f);
        layout.setText(font, costTxt);
        float b1GroupW = 16f + 8f + layout.width;
        float b1StartX = 220f + (240f - b1GroupW) / 2f;
        btn1IconX = b1StartX + 8f;
        btn1TxtX = b1StartX + 16f + 8f;

        // Button 2: Pull 1x
        font.getData().setScale(0.85f);
        layout.setText(font, "1");
        float b2GroupW = 22f + 8f + layout.width;
        float b2StartX = 490f + (240f - b2GroupW) / 2f;
        btn2IconX = b2StartX + 11f;
        btn2TxtX = b2StartX + 22f + 8f;

        // Button 3: Pull 10x
        font.getData().setScale(0.82f);
        layout.setText(font, "10  •  Chắc chắn 4★+");
        float b3GroupW = 22f + 8f + layout.width;
        float b3StartX = 760f + (280f - b3GroupW) / 2f;
        btn3IconX = b3StartX + 11f;
        btn3TxtX = b3StartX + 22f + 8f;
        font.getData().setScale(1f);

        drawShapes();
        drawBatch();

        // Draw Rate & Loot Table Modal (guaranteed on top of everything)
        if (showRateLootModal) {
            drawRateAndLootModal();
        }

        // Draw notification toast if active
        NotificationUI.drawNotification(sr, game.batch, font, message, messageTimer, 2.5f, W, H);
    }
    
    private void drawShapes() {
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // 1. Dynamic Background
        if (!isEventBanner) {
            // Royal Sapphire / Cosmic theme
            sr.rect(0, 0, W, H, BG_NORM_BOT, BG_NORM_BOT, BG_NORM_TOP, BG_NORM_TOP);
            // Drifting stars
            for (int i = 0; i < STAR_COUNT; i++) {
                float pulse = 0.5f + 0.5f * MathUtils.sin(animTimer * 2f + i);
                sr.setColor(0.5f, 0.8f, 1f, starAlpha[i] * pulse);
                sr.circle(starX[i], starY[i], starSize[i]);
            }
        } else {
            // Lạc Hồng Ruby / Imperial Bronze theme
            sr.rect(0, 0, W, H, BG_EVT_BOT, BG_EVT_BOT, BG_EVT_TOP, BG_EVT_TOP);

            // Watermark rotating Dong Son drum sunburst in center background
            float drumCX = W / 2f;
            float drumCY = H / 2f;
            sr.setColor(0.95f, 0.65f, 0.2f, 0.05f);
            sr.circle(drumCX, drumCY, 240f);
            sr.circle(drumCX, drumCY, 170f);
            sr.circle(drumCX, drumCY, 100f);
            for (int s = 0; s < 12; s++) {
                float sAng = animTimer * 8f + s * 30f;
                float sx1 = drumCX + MathUtils.cosDeg(sAng - 5f) * 160f;
                float sy1 = drumCY + MathUtils.sinDeg(sAng - 5f) * 160f;
                float sx2 = drumCX + MathUtils.cosDeg(sAng + 5f) * 160f;
                float sy2 = drumCY + MathUtils.sinDeg(sAng + 5f) * 160f;
                float sx3 = drumCX + MathUtils.cosDeg(sAng) * 230f;
                float sy3 = drumCY + MathUtils.sinDeg(sAng) * 230f;
                sr.triangle(sx1, sy1, sx2, sy2, sx3, sy3);
            }

            // Flying Chim Lac silhouette in sky
            float birdX = (animTimer * 40f) % (W + 200f) - 100f;
            float birdY = H - 95f + MathUtils.sin(animTimer * 1.5f) * 12f;
            sr.setColor(0.95f, 0.75f, 0.3f, 0.18f);
            sr.triangle(birdX, birdY, birdX + 26f, birdY + 6f, birdX + 8f, birdY + 12f);
            sr.triangle(birdX + 8f, birdY + 12f, birdX - 8f, birdY + 28f, birdX + 16f, birdY + 8f);

            // Floating festive embers
            for (int i = 0; i < STAR_COUNT; i++) {
                float pulse = 0.5f + 0.5f * MathUtils.sin(animTimer * 2f + i);
                if (i % 2 == 0) {
                    sr.setColor(1f, 0.82f, 0.3f, starAlpha[i] * pulse);
                } else {
                    sr.setColor(0.95f, 0.3f, 0.25f, starAlpha[i] * pulse);
                }
                sr.circle(starX[i], starY[i], starSize[i]);
            }
        }

        // 2. Header Bar
        sr.setColor(0.06f, 0.08f, 0.14f, 0.95f);
        sr.rect(0, H - 72f, W, 72f);
        sr.setColor(isEventBanner ? new Color(0.95f, 0.35f, 0.25f, 0.75f) : new Color(0.3f, 0.75f, 0.95f, 0.75f));
        sr.rect(0, H - 74f, W, 2f);

        // Back Button
        sr.setColor(hoverBack ? new Color(0.25f, 0.35f, 0.50f, 0.95f) : new Color(0.12f, 0.18f, 0.26f, 0.9f));
        drawRoundedRect(sr, 25f, H - 62f, 120f, 42f, 8);
        sr.setColor(hoverBack ? new Color(0.6f, 0.85f, 1f, 1f) : new Color(0.3f, 0.5f, 0.7f, 0.7f));
        drawRectOutline(sr, 25f, H - 62f, 120f, 42f, 1.5f);

        // Rate/Loot Button
        sr.setColor(hoverHelp ? new Color(0.25f, 0.35f, 0.50f, 0.95f) : new Color(0.12f, 0.18f, 0.26f, 0.9f));
        drawRoundedRect(sr, 155f, H - 62f, 130f, 42f, 8);
        sr.setColor(hoverHelp ? new Color(0.6f, 0.85f, 1f, 1f) : new Color(0.3f, 0.5f, 0.7f, 0.7f));
        drawRectOutline(sr, 155f, H - 62f, 130f, 42f, 1.5f);

        // Top Right Resource Dock (Diamonds, Normal Tickets, Premium Tickets) with dedicated Vector Icons
        float resW = 345f;
        float resH = 46f;
        float resX = W - resW - 25f;
        float resY = H - 64f;
        float secW = resW / 3f;
        sr.setColor(0.08f, 0.12f, 0.18f, 0.95f);
        drawRoundedRect(sr, resX, resY, resW, resH, 8);
        sr.setColor(0.25f, 0.45f, 0.65f, 0.8f);
        drawRectOutline(sr, resX, resY, resW, resH, 1.5f);

        // Section Separator dividers
        sr.setColor(0.20f, 0.32f, 0.48f, 0.65f);
        sr.rectLine(resX + secW, resY + 8f, resX + secW, resY + resH - 8f, 1f);
        sr.rectLine(resX + secW * 2f, resY + 8f, resX + secW * 2f, resY + resH - 8f, 1f);

        // Render Currency Icons inside Dock (Precisely Centered)
        float iconY = resY + resH / 2f;
        EquipmentIconRenderer.drawDiamondIcon(sr, dockDiaIconX, iconY, 9f);
        EquipmentIconRenderer.drawNormalTicketIcon(sr, dockNormIconX, iconY, 24f, 15f);
        EquipmentIconRenderer.drawPremiumTicketIcon(sr, dockPremIconX, iconY, 24f, 15f);

        // 3. Banner Selector Tabs (Centered)
        float tabW = 260f;
        float tabH = 44f;
        float gap = 20f;
        float startTabX = (W - (2 * tabW + gap)) / 2f;
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

        // 4. MAIN SHOWCASE BANNER CARD (IDLE STATE)
        if (state == State.IDLE) {
            float cardX = 140f;
            float cardY = 120f;
            float cardW = 1000f;
            float cardH = 430f;

            // Card Shadow
            sr.setColor(0f, 0f, 0f, 0.5f);
            drawRoundedRect(sr, cardX + 6f, cardY - 6f, cardW, cardH, 16);

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

            // Left Showcase: Altar & Gacha Wheel
            float showcaseCX = cardX + 235f;
            float wheelCY = cardY + 280f;
            float wheelRadius = 82f;

            // Rotating Light rays behind wheel
            StandardSetTheme curTheme = STANDARD_SETS[currentStandardSetIndex];
            Color rayCol = isEventBanner ? new Color(1f, 0.8f, 0.3f, 0.12f) :
                new Color(curTheme.primaryColor.r, curTheme.primaryColor.g, curTheme.primaryColor.b, 0.14f);
            for (int r = 0; r < 8; r++) {
                float angle = animTimer * 20f + r * 45f;
                float rad = angle * MathUtils.degreesToRadians;
                float rx = showcaseCX + MathUtils.cos(rad) * 165f;
                float ry = wheelCY + MathUtils.sin(rad) * 165f;
                sr.setColor(rayCol);
                sr.triangle(showcaseCX, wheelCY, rx - 20, ry, rx + 20, ry);
            }

            // Draw Upgraded Grand Prize Wheel
            drawGachaWheel(sr, showcaseCX, wheelCY, wheelRadius, isEventBanner);

            // Featured Set Equipment Pedestal with 3 Slots
            float pedW = 320f;
            float pedH = 124f;
            float pedX = showcaseCX - pedW / 2f;
            float pedY = cardY + 20f;

            sr.setColor(0.09f, 0.12f, 0.18f, 0.95f);
            drawRoundedRect(sr, pedX, pedY, pedW, pedH, 8);
            if (hoverSet && state == State.IDLE && !showSetDetailModal && !showRateLootModal) {
                float pulse = 0.82f + 0.18f * MathUtils.sin(animTimer * 6f);
                Color baseC = isEventBanner ? new Color(1f, 0.75f, 0.35f, 1f) : curTheme.primaryColor;
                sr.setColor(baseC.r * pulse, baseC.g * pulse, baseC.b * pulse, 1f);
                drawRectOutline(sr, pedX, pedY, pedW, pedH, 2.2f);
            } else {
                sr.setColor(isEventBanner ? new Color(0.95f, 0.65f, 0.25f, 0.85f) : curTheme.primaryColor);
                drawRectOutline(sr, pedX, pedY, pedW, pedH, 1.5f);
            }

            // 3 Equipment Slots (Rod, Line, Hook) with Full Vector Art Display
            String curSetName = isEventBanner ? "Thần Khí Lạc Hồng" : curTheme.setName;
            Color curSetCol = isEventBanner ? new Color(1f, 0.85f, 0.35f, 1f) : curTheme.primaryColor;

            float slotW = 92f;
            float slotH = 88f;
            float slotY = pedY + 28f;
            float slot1X = pedX + 10f;
            float slot2X = pedX + 114f;
            float slot3X = pedX + 218f;

            // Slot 1: Rod
            sr.setColor(0.06f, 0.08f, 0.13f, 0.95f);
            drawRoundedRect(sr, slot1X, slotY, slotW, slotH, 6);
            sr.setColor(curSetCol.r, curSetCol.g, curSetCol.b, 0.65f);
            drawRectOutline(sr, slot1X, slotY, slotW, slotH, 1.2f);
            sr.setColor(curSetCol.r, curSetCol.g, curSetCol.b, 0.18f);
            sr.circle(slot1X + slotW / 2f, slotY + slotH / 2f, 40f);
            EquipmentIconRenderer.drawSetPiece(sr, curSetName, EquipmentType.ROD, slot1X + slotW / 2f, slotY + slotH / 2f, 36f, curSetCol);

            // Slot 2: Line
            sr.setColor(0.06f, 0.08f, 0.13f, 0.95f);
            drawRoundedRect(sr, slot2X, slotY, slotW, slotH, 6);
            sr.setColor(curSetCol.r, curSetCol.g, curSetCol.b, 0.65f);
            drawRectOutline(sr, slot2X, slotY, slotW, slotH, 1.2f);
            sr.setColor(curSetCol.r, curSetCol.g, curSetCol.b, 0.18f);
            sr.circle(slot2X + slotW / 2f, slotY + slotH / 2f, 40f);
            EquipmentIconRenderer.drawSetPiece(sr, curSetName, EquipmentType.LINE, slot2X + slotW / 2f, slotY + slotH / 2f, 36f, curSetCol);

            // Slot 3: Hook
            sr.setColor(0.06f, 0.08f, 0.13f, 0.95f);
            drawRoundedRect(sr, slot3X, slotY, slotW, slotH, 6);
            sr.setColor(curSetCol.r, curSetCol.g, curSetCol.b, 0.65f);
            drawRectOutline(sr, slot3X, slotY, slotW, slotH, 1.2f);
            sr.setColor(curSetCol.r, curSetCol.g, curSetCol.b, 0.18f);
            sr.circle(slot3X + slotW / 2f, slotY + slotH / 2f, 40f);
            EquipmentIconRenderer.drawSetPiece(sr, curSetName, EquipmentType.HOOK, slot3X + slotW / 2f, slotY + slotH / 2f, 36f, curSetCol);

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
                // Limited Pity Bar (80) - MOVED UP to eliminate awkward empty space!
                float limP = MathUtils.clamp((float)player.pityLimited / 80f, 0f, 1f);
                sr.setColor(0.08f, 0.12f, 0.18f, 1f);
                drawRoundedRect(sr, barX, infoY + 120f, barW, barH, 4);
                sr.setColor(1f, 0.35f, 0.25f, 1f);
                if (limP > 0) drawRoundedRect(sr, barX + 1, infoY + 121f, (barW - 2) * limP, barH - 2, 3);
            }
        }

        // 5. SPINNING STATE: Roller Bar (CS:GO Reel) with Full Vector Artwork
        if (state == State.SPINNING) {
            float spinX = 100f;
            float spinY = 240f;
            float spinW = 1080f;
            float spinH = 225f;

            // Roller container background
            sr.setColor(0.05f, 0.07f, 0.12f, 0.98f);
            drawRoundedRect(sr, spinX, spinY, spinW, spinH, 14);
            sr.setColor(isEventBanner ? new Color(1f, 0.5f, 0.3f, 0.85f) : new Color(0.35f, 0.75f, 0.95f, 0.85f));
            drawRectOutline(sr, spinX, spinY, spinW, spinH, 2.5f);

            // Scissor clip for equipment cards
            sr.flush();
            Rectangle clipBounds = new Rectangle(spinX + 4f, spinY + 4f, spinW - 8f, spinH - 8f);
            Rectangle scissors = new Rectangle();
            ScissorStack.calculateScissors(camera, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight(), sr.getTransformMatrix(), clipBounds, scissors);

            if (ScissorStack.pushScissors(scissors)) {
                float progress = spinTimer / totalSpinTime;
                float easedProgress = Interpolation.pow5Out.apply(progress);

                float itemWidth = 160f;
                float itemHeight = 200f;
                float spacing = 20f;
                float totalW = itemWidth + spacing;

                int targetIndex = 65;
                float targetOffset = -(targetIndex * totalW) + (spinW / 2f) - (itemWidth / 2f);
                float currentOffset = spinX + targetOffset * easedProgress;

                for (int i = 0; i < spinItems.size(); i++) {
                    float ix = currentOffset + i * totalW;
                    if (ix + itemWidth > spinX && ix < spinX + spinW) {
                        Equipment eq = spinItems.get(i);
                        float cardY = spinY + 12f;

                        // Card background
                        sr.setColor(0.08f, 0.11f, 0.18f, 0.96f);
                        drawRoundedRect(sr, ix, cardY, itemWidth, itemHeight, 10);

                        // Rarity aura glow behind equipment icon
                        Color rCol = eq.rarity != null ? eq.rarity.color : Color.WHITE;
                        float iconCX = ix + itemWidth / 2f;
                        float iconCY = cardY + itemHeight - 72f;
                        sr.setColor(rCol.r, rCol.g, rCol.b, 0.22f);
                        sr.circle(iconCX, iconCY, 50f);

                        // Card border
                        sr.setColor(rCol.r, rCol.g, rCol.b, 0.85f);
                        drawRectOutline(sr, ix, cardY, itemWidth, itemHeight, 1.8f);

                        // Full-size vector equipment artwork!
                        EquipmentIconRenderer.draw(sr, eq, eq.type, iconCX, iconCY, 44f, rCol);
                    }
                }
                sr.flush();
                ScissorStack.popScissors();
            }

            // Center Target Crosshair Indicator (over top of cards)
            float cx = W / 2f;
            sr.setColor(1f, 0.85f, 0.3f, 0.95f);
            sr.rect(cx - 3, spinY, 6, spinH);
            sr.triangle(cx - 15, spinY + spinH, cx + 15, spinY + spinH, cx, spinY + spinH - 20);
            sr.triangle(cx - 15, spinY, cx + 15, spinY, cx, spinY + 20);
        }

        // 6. RESULT STATE: Pull Results Grid
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
                Equipment eq = lastPull.get(0);
                float cardW = 340f;
                float cardH = 380f;
                float cardX = W / 2f - cardW / 2f;
                float cardY = resBoxY + (resBoxH - cardH) / 2f;

                sr.setColor(0.10f, 0.14f, 0.22f, 0.98f);
                drawRoundedRect(sr, cardX, cardY, cardW, cardH, 12);
                sr.setColor(eq.rarity.color);
                drawRectOutline(sr, cardX, cardY, cardW, cardH, 3f);

                // Aura glow
                float iconCY = cardY + cardH - 116f;
                sr.setColor(eq.rarity.color.r, eq.rarity.color.g, eq.rarity.color.b, 0.22f);
                sr.circle(cardX + cardW / 2f, iconCY, 45f);

                // Draw exact equipment vector icon (Full-size showcase)
                EquipmentIconRenderer.draw(sr, eq, eq.type, cardX + cardW / 2f, iconCY, 40f, eq.rarity.color);
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

                    sr.setColor(0.09f, 0.12f, 0.18f, 0.95f);
                    drawRoundedRect(sr, gx, gy, cW, cH, 8);

                    boolean isTopRarity = (bestItem != null && eq.rarity == bestItem.rarity && eq.rarity.ordinal() >= Rarity.EPIC.ordinal());
                    if (isTopRarity) {
                        float p = 0.7f + 0.3f * MathUtils.sin(animTimer * 4f);
                        sr.setColor(eq.rarity.color.r * p, eq.rarity.color.g * p, eq.rarity.color.b * p, 1f);
                        drawRectOutline(sr, gx, gy, cW, cH, 2.5f);
                    } else {
                        sr.setColor(eq.rarity.color);
                        drawRectOutline(sr, gx, gy, cW, cH, 1.5f);
                    }

                    // Aura glow behind item in grid
                    float iconCY = gy + cH - 56f;
                    sr.setColor(eq.rarity.color.r, eq.rarity.color.g, eq.rarity.color.b, 0.18f);
                    sr.circle(gx + cW / 2f, iconCY, 42f);

                    // Vector icon for each item in grid (Full detailed display)
                    EquipmentIconRenderer.draw(sr, eq, eq.type, gx + cW / 2f, iconCY, 36f, eq.rarity.color);
                }
            }
        }

        // 7. BOTTOM ACTION BUTTONS
        if (state == State.IDLE || state == State.RESULT) {
            float btnY = 32f;
            float btnH = 54f;

            // Button 1: Buy Ticket
            float b1X = 220f;
            float b1W = 240f;
            sr.setColor(hoverBuyTicket ? new Color(0.20f, 0.45f, 0.75f, 0.95f) : new Color(0.12f, 0.28f, 0.48f, 0.9f));
            drawRoundedRect(sr, b1X, btnY, b1W, btnH, 10);
            sr.setColor(hoverBuyTicket ? new Color(0.6f, 0.85f, 1f, 1f) : new Color(0.3f, 0.6f, 0.9f, 0.8f));
            drawRectOutline(sr, b1X, btnY, b1W, btnH, 2f);

            // Draw diamond icon in button 1 (Centered)
            EquipmentIconRenderer.drawDiamondIcon(sr, btn1IconX, btnY + 20f, 8f);

            // Button 2: Pull 1x
            float b2X = 490f;
            float b2W = 240f;
            sr.setColor(hoverPull1 ? new Color(0.25f, 0.60f, 0.40f, 0.95f) : new Color(0.15f, 0.42f, 0.28f, 0.9f));
            drawRoundedRect(sr, b2X, btnY, b2W, btnH, 10);
            sr.setColor(hoverPull1 ? new Color(0.6f, 1f, 0.7f, 1f) : new Color(0.3f, 0.85f, 0.5f, 0.8f));
            drawRectOutline(sr, b2X, btnY, b2W, btnH, 2f);

            // Draw ticket icon in button 2 (Centered)
            if (isEventBanner) {
                EquipmentIconRenderer.drawPremiumTicketIcon(sr, btn2IconX, btnY + 20f, 22f, 14f);
            } else {
                EquipmentIconRenderer.drawNormalTicketIcon(sr, btn2IconX, btnY + 20f, 22f, 14f);
            }

            // Button 3: Pull 10x
            float b3X = 760f;
            float b3W = 280f;
            sr.setColor(hoverPull10 ? new Color(0.85f, 0.50f, 0.18f, 0.95f) : new Color(0.65f, 0.35f, 0.12f, 0.9f));
            drawRoundedRect(sr, b3X, btnY, b3W, btnH, 10);
            sr.setColor(hoverPull10 ? new Color(1f, 0.85f, 0.4f, 1f) : new Color(0.95f, 0.65f, 0.25f, 0.8f));
            drawRectOutline(sr, b3X, btnY, b3W, btnH, 2.5f);

            // Draw ticket icon in button 3 (Centered)
            if (isEventBanner) {
                EquipmentIconRenderer.drawPremiumTicketIcon(sr, btn3IconX, btnY + 20f, 22f, 14f);
            } else {
                EquipmentIconRenderer.drawNormalTicketIcon(sr, btn3IconX, btnY + 20f, 22f, 14f);
            }
        }

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void drawBatch() {
        game.batch.begin();

        // 1. Back & Rate Buttons
        font.setColor(hoverBack ? Color.WHITE : new Color(0.8f, 0.9f, 1f, 0.9f));
        layout.setText(font, "← QUAY LẠI");
        font.draw(game.batch, layout, 25f + (120f - layout.width) / 2f, H - 36f);

        font.setColor(hoverHelp ? Color.WHITE : new Color(0.8f, 0.9f, 1f, 0.9f));
        layout.setText(font, "TỶ LỆ & KHO");
        font.draw(game.batch, layout, 155f + (130f - layout.width) / 2f, H - 36f);

        // 2. Resource Dock Text (Only quantities next to vector icons)
        float resH = 46f;
        float resY = H - 64f;

        font.getData().setScale(0.88f);
        // Diamond quantity (Centered)
        font.setColor(0.4f, 0.95f, 1f, 1f);
        font.draw(game.batch, String.valueOf(player.getDiamonds()), dockDiaTxtX, resY + 29f);

        // Regular Ticket quantity (Centered)
        font.setColor(1f, 0.88f, 0.35f, 1f);
        font.draw(game.batch, String.valueOf(player.getGachaTickets()), dockNormTxtX, resY + 29f);

        // Premium Ticket quantity (Centered)
        font.setColor(1f, 0.55f, 0.65f, 1f);
        font.draw(game.batch, String.valueOf(player.getPremiumTickets()), dockPremTxtX, resY + 29f);
        font.getData().setScale(1f);

        // 3. Centered Header Title
        titleFont.setColor(isEventBanner ? new Color(1f, 0.85f, 0.4f, 1f) : new Color(0.95f, 0.85f, 0.45f, 1f));
        titleFont.getData().setScale(0.85f);
        String bannerTitle = isEventBanner ? "ĐÀI QUAY SỰ KIỆN: LẠC HỒNG" : "ĐÀI QUAY TRANG BỊ THƯỜNG";
        layout.setText(titleFont, bannerTitle);
        titleFont.draw(game.batch, bannerTitle, (W - layout.width) / 2f, H - 35f);
        titleFont.getData().setScale(1f);

        // 4. Banner Selector Tabs Text (Centered)
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
        layout.setText(font, "★ SỰ KIỆN LẠC HỒNG ★");
        font.draw(game.batch, layout, tab2X + (tabW - layout.width) / 2f, tabY + 28f);

        // 5. Main Showcase Info (IDLE STATE)
        if (state == State.IDLE) {
            float cardX = 140f;
            float cardY = 120f;
            float showcaseCX = cardX + 235f;
            float infoX = cardX + 460f;
            float infoY = cardY + 30f;

            // Pedestal equipment slot labels - Perfectly aligned under each 92px slot!
            float pedW = 320f;
            float pedH = 124f;
            float pedX = showcaseCX - pedW / 2f;
            float pedY = cardY + 20f;
            float slotW = 92f;
            float slot1X = pedX + 10f;
            float slot2X = pedX + 114f;
            float slot3X = pedX + 218f;

            font.getData().setScale(0.70f);
            font.setColor(0.85f, 0.9f, 1f, 0.90f);
            drawCenteredText(font, "Cần Câu", slot1X, pedY + 6f, slotW, 18f);
            drawCenteredText(font, "Dây Cước", slot2X, pedY + 6f, slotW, 18f);
            drawCenteredText(font, "Lưỡi Câu", slot3X, pedY + 6f, slotW, 18f);
            font.getData().setScale(1f);

            if (!isEventBanner) {
                StandardSetTheme curSet = STANDARD_SETS[currentStandardSetIndex];

                // Weapon Title & Set Name
                titleFont.setColor(curSet.primaryColor);
                String setName = "BỘ " + curSet.setName.toUpperCase();
                layout.setText(titleFont, setName);
                titleFont.draw(game.batch, layout, infoX + 25f, infoY + 340f);

                // Set Tag
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

                // (Line "(Phẩm chất trang bị khi quay là ngẫu nhiên...)" is fully removed!)

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
                // Event Banner Info
                titleFont.setColor(new Color(1f, 0.45f, 0.35f, 1f));
                String setName = "BỘ THẦN KHÍ LẠC HỒNG";
                layout.setText(titleFont, setName);
                titleFont.draw(game.batch, layout, infoX + 25f, infoY + 340f);

                // Rarity Tag
                font.setColor(new Color(1f, 0.85f, 0.35f, 1f));
                font.getData().setScale(0.82f);
                String rarTag = "✦ PHẨM CHẤT: THẦN THOẠI (GIỚI HẠN) • 15 CẤP ĐIỂM";
                font.draw(game.batch, rarTag, infoX + 25f, infoY + 308f);

                // Items list
                font.setColor(0.95f, 0.85f, 0.65f, 1f);
                font.getData().setScale(0.78f);
                String itemsStr = "• Gồm: Trượng Lạc Hồng  |  Dây Rồng Thiêng  |  Móc Âu Cơ";
                font.draw(game.batch, itemsStr, infoX + 25f, infoY + 280f);

                // Set Perks
                font.setColor(Color.WHITE);
                font.getData().setScale(0.80f);
                String perk1 = "• Hiệu ứng 3 Món: [Ý Chí Bất Diệt] Vô hiệu hóa Kỹ Năng Boss";
                String perk2 = "• Tăng 50% Lực kéo đối với Boss Thủy Quái & Cá Khổng Lồ";
                font.draw(game.batch, perk1, infoX + 25f, infoY + 252f);
                font.draw(game.batch, perk2, infoX + 25f, infoY + 224f);

                // Pity Header (MOVED UP to prevent empty space!)
                font.setColor(0.7f, 0.85f, 1f, 0.9f);
                font.getData().setScale(0.85f);
                font.draw(game.batch, "TIẾN TRÌNH BẢO HIỂM MAY MẮN (PITY):", infoX + 25f, infoY + 185f);

                int remLim = Math.max(0, 80 - player.pityLimited);
                font.setColor(1f, 0.45f, 0.35f, 1f);
                font.draw(game.batch, "Bảo hiểm Thần Khí Lạc Hồng: Còn " + remLim + " lượt (" + player.pityLimited + "/80)", infoX + 25f, infoY + 152f);

                String eventTime = player.getEventManager().getEventTimeRemaining();
                font.setColor(0.95f, 0.85f, 0.4f, 1f);
                font.draw(game.batch, "Thời gian sự kiện còn lại: " + eventTime, infoX + 25f, infoY + 80f);

                font.setColor(0.6f, 0.8f, 1f, 0.75f);
                font.getData().setScale(0.72f);
                font.draw(game.batch, "★ Tiến độ bảo hiểm được bảo lưu cho các đợt sự kiện tiếp theo ★", infoX + 25f, infoY + 52f);
            }
            font.getData().setScale(1f);
        }

        // 6. Result State Texts
        if (state == State.RESULT) {
            float resBoxX = 100f;
            float resBoxY = 110f;
            float resBoxW = 1080f;
            float resBoxH = 450f;

            if (lastPull.size() == 1) {
                Equipment eq = lastPull.get(0);
                float cardW = 340f;
                float cardH = 380f;
                float cardX = W / 2f - cardW / 2f;
                float cardY = resBoxY + (resBoxH - cardH) / 2f;

                titleFont.setColor(eq.rarity.color);
                layout.setText(titleFont, eq.name);
                titleFont.draw(game.batch, layout, cardX + (cardW - layout.width) / 2f, cardY + cardH - 26f);

                font.setColor(0.8f, 0.9f, 1f, 1f);
                String typeStr = "Loại: " + (eq.type == EquipmentType.ROD ? "Cần Câu" : eq.type == EquipmentType.LINE ? "Dây Cước" : "Lưỡi Câu");
                layout.setText(font, typeStr);
                font.draw(game.batch, layout, cardX + (cardW - layout.width) / 2f, cardY + cardH - 188f);

                font.setColor(eq.rarity.color);
                layout.setText(font, "[" + eq.rarity.name() + "]  Bộ: " + eq.setName);
                font.draw(game.batch, layout, cardX + (cardW - layout.width) / 2f, cardY + cardH - 216f);

                // Stats
                font.setColor(0.4f, 1f, 0.6f, 1f);
                String s1 = (eq.type == EquipmentType.ROD ? "Lực Kéo" : eq.type == EquipmentType.LINE ? "Độ Bền" : "Độ Sắc") + ": +" + (int)eq.mainStat1;
                layout.setText(font, s1);
                font.draw(game.batch, layout, cardX + (cardW - layout.width) / 2f, cardY + cardH - 248f);

                // Substats
                font.setColor(0.9f, 0.85f, 0.5f, 1f);
                float subY = cardY + cardH - 280f;
                for (com.fishingrpg.game.entities.equipment.Substat sub : eq.substats) {
                    String subStr = "• " + sub.toString();
                    layout.setText(font, subStr);
                    font.draw(game.batch, layout, cardX + (cardW - layout.width) / 2f, subY);
                    subY -= 24f;
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
                    font.draw(game.batch, eq.name, gx + 8f, gy + cH - 12f, cW - 16f, Align.center, true);

                    // Type & Rarity
                    font.setColor(0.8f, 0.9f, 1f, 0.85f);
                    String typeStr = (eq.type == EquipmentType.ROD ? "Cần" : eq.type == EquipmentType.LINE ? "Cước" : "Lưỡi");
                    font.draw(game.batch, typeStr + " • " + eq.rarity.name(), gx + 8f, gy + cH - 98f, cW - 16f, Align.center, false);

                    // Set Name
                    font.setColor(0.7f, 0.75f, 0.85f, 0.75f);
                    font.draw(game.batch, "Bộ: " + eq.setName, gx + 8f, gy + cH - 120f, cW - 16f, Align.center, false);

                    // Main Stat
                    font.setColor(0.4f, 1f, 0.6f, 1f);
                    String s1 = "+" + (int)eq.mainStat1;
                    font.draw(game.batch, s1, gx + 8f, gy + 22f, cW - 16f, Align.center, false);
                }
            }
        }

        // 7. Bottom Buttons Text
        if (state == State.IDLE || state == State.RESULT) {
            float btnY = 32f;
            float btnH = 54f;

            // Button 1: Buy Ticket
            float b1X = 220f;
            float b1W = 240f;
            font.setColor(hoverBuyTicket ? Color.WHITE : new Color(0.9f, 0.95f, 1f, 0.95f));
            String buyTxt = isEventBanner ? "ĐỔI VÉ CAO CẤP" : "ĐỔI VÉ QUAY";
            layout.setText(font, buyTxt);
            font.draw(game.batch, layout, b1X + (b1W - layout.width) / 2f, btnY + 44f);

            font.setColor(0.4f, 0.95f, 1f, 1f);
            String costTxt = isEventBanner ? "20" : "10";
            font.draw(game.batch, costTxt, btn1TxtX, btnY + 26f);

            // Button 2: Pull 1x
            float b2X = 490f;
            float b2W = 240f;
            font.setColor(hoverPull1 ? Color.WHITE : new Color(0.9f, 1f, 0.9f, 0.95f));
            layout.setText(font, "QUAY 1 LƯỢT");
            font.draw(game.batch, layout, b2X + (b2W - layout.width) / 2f, btnY + 44f);

            font.setColor(0.7f, 1f, 0.8f, 1f);
            font.getData().setScale(0.85f);
            font.draw(game.batch, "1", btn2TxtX, btnY + 26f);
            font.getData().setScale(1f);

            // Button 3: Pull 10x
            float b3X = 760f;
            float b3W = 280f;
            font.setColor(hoverPull10 ? Color.WHITE : new Color(1f, 0.95f, 0.85f, 0.95f));
            layout.setText(font, "QUAY 10 LƯỢT");
            font.draw(game.batch, layout, b3X + (b3W - layout.width) / 2f, btnY + 44f);

            font.setColor(1f, 0.85f, 0.4f, 1f);
            font.getData().setScale(0.82f);
            font.draw(game.batch, "10  •  Chắc chắn 4★+", btn3TxtX, btnY + 26f);
            font.getData().setScale(1f);
        }

        game.batch.end();

        // 8. Draw Spinner Scissor Clip
        if (state == State.SPINNING) {
            game.batch.begin();
            game.batch.flush();
            float spinX = 100f;
            float spinY = 240f;
            float spinW = 1080f;
            float spinH = 225f;

            Rectangle clipBounds = new Rectangle(spinX + 4f, spinY + 4f, spinW - 8f, spinH - 8f);
            Rectangle scissors = new Rectangle();
            ScissorStack.calculateScissors(camera, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight(), game.batch.getTransformMatrix(), clipBounds, scissors);

            if (ScissorStack.pushScissors(scissors)) {
                float progress = spinTimer / totalSpinTime;
                float easedProgress = Interpolation.pow5Out.apply(progress);

                float itemWidth = 160f;
                float itemHeight = 200f;
                float spacing = 20f;
                float totalW = itemWidth + spacing;

                int targetIndex = 65;
                float targetOffset = -(targetIndex * totalW) + (spinW / 2f) - (itemWidth / 2f);
                float currentOffset = spinX + targetOffset * easedProgress;

                for (int i = 0; i < spinItems.size(); i++) {
                    float ix = currentOffset + i * totalW;
                    if (ix + itemWidth > spinX && ix < spinX + spinW) {
                        Equipment eq = spinItems.get(i);
                        float cardY = spinY + 12f;

                        // Equipment Name in rarity color
                        font.setColor(eq.rarity != null ? eq.rarity.color : Color.WHITE);
                        font.getData().setScale(0.72f);
                        font.draw(game.batch, eq.name, ix + 6f, cardY + 54f, itemWidth - 12f, Align.center, true);

                        // Equipment Type & Rarity tag
                        font.setColor(0.8f, 0.9f, 1f, 0.85f);
                        font.getData().setScale(0.64f);
                        String typeLabel = (eq.type == EquipmentType.ROD ? "Cần" : eq.type == EquipmentType.LINE ? "Cước" : "Lưỡi");
                        String rarName = (eq.rarity != null ? eq.rarity.name() : "");
                        font.draw(game.batch, typeLabel + " • " + rarName, ix + 6f, cardY + 22f, itemWidth - 12f, Align.center, false);
                        font.getData().setScale(1f);
                    }
                }

                game.batch.flush();
                ScissorStack.popScissors();
            }
            game.batch.end();
        }

        // 9. Draw Set Detail Modal when opened by click
        if (showSetDetailModal && state == State.IDLE && !showRateLootModal) {
            drawSetDetailModal();
        }
    }

    private void drawGachaWheel(ShapeRenderer sr, float cx, float cy, float r, boolean isEvent) {
        float rot = (state == State.SPINNING) ? 
            (animTimer * 900f * (1f - spinTimer / totalSpinTime)) : 
            (animTimer * 16f);

        // Outer glow
        sr.setColor(isEvent ? new Color(1f, 0.4f, 0.2f, 0.15f) : new Color(0.3f, 0.7f, 1f, 0.15f));
        sr.circle(cx, cy, r + 14f);

        // Heavy metallic outer rim
        sr.setColor(isEvent ? new Color(0.65f, 0.45f, 0.18f, 1f) : new Color(0.85f, 0.65f, 0.25f, 1f));
        sr.circle(cx, cy, r + 8f);
        sr.setColor(0.08f, 0.11f, 0.18f, 1f);
        sr.circle(cx, cy, r + 4f);

        // 12 alternating prize sectors
        for (int i = 0; i < 12; i++) {
            float a1 = rot + i * 30f;
            float a2 = rot + (i + 1) * 30f;
            float rad1 = a1 * MathUtils.degreesToRadians;
            float rad2 = a2 * MathUtils.degreesToRadians;
            float p1x = cx + MathUtils.cos(rad1) * r;
            float p1y = cy + MathUtils.sin(rad1) * r;
            float p2x = cx + MathUtils.cos(rad2) * r;
            float p2y = cy + MathUtils.sin(rad2) * r;

            if (i % 2 == 0) {
                if (isEvent) {
                    sr.setColor(0.55f, 0.15f, 0.20f, 1f); // Ruby crimson
                } else {
                    sr.setColor(0.12f, 0.22f, 0.42f, 1f); // Royal sapphire
                }
            } else {
                if (isEvent) {
                    sr.setColor(0.85f, 0.60f, 0.20f, 1f); // Bronze gold
                } else {
                    sr.setColor(0.82f, 0.68f, 0.30f, 1f); // Warm gold
                }
            }
            sr.triangle(cx, cy, p1x, p1y, p2x, p2y);

            // Sector divider ray
            sr.setColor(isEvent ? new Color(1f, 0.85f, 0.4f, 0.7f) : new Color(1f, 0.92f, 0.55f, 0.7f));
            sr.rectLine(cx, cy, p1x, p1y, 1.5f);

            // Outer stud / jewel rivet
            sr.setColor(1f, 0.95f, 0.65f, 1f);
            sr.circle(p1x, p1y, 2.5f);
        }

        // Center hub plate (Circular plate with circular rim)
        float hubR = r * 0.42f;
        sr.setColor(isEvent ? new Color(0.95f, 0.75f, 0.25f, 1f) : new Color(0.45f, 0.8f, 1f, 1f));
        sr.circle(cx, cy, hubR);
        sr.setColor(0.08f, 0.10f, 0.16f, 1f);
        sr.circle(cx, cy, hubR - 2.5f);

        // Spinning core star emblem
        for (int s = 0; s < (isEvent ? 12 : 8); s++) {
            float sAng = -rot * 0.8f + s * (isEvent ? 30f : 45f);
            float sx = cx + MathUtils.cosDeg(sAng) * (hubR * 0.85f);
            float sy = cy + MathUtils.sinDeg(sAng) * (hubR * 0.85f);
            sr.setColor(isEvent ? new Color(1f, 0.85f, 0.35f, 0.9f) : new Color(1f, 0.95f, 0.55f, 0.9f));
            sr.rectLine(cx, cy, sx, sy, 2f);
        }
        sr.setColor(isEvent ? new Color(0.9f, 0.2f, 0.25f, 1f) : new Color(0.3f, 0.85f, 1f, 1f));
        sr.circle(cx, cy, hubR * 0.35f);
        sr.setColor(Color.WHITE);
        sr.circle(cx, cy, hubR * 0.15f);

        // Top Needle Pointer
        float pointerTipY = cy + r - 4f;
        float pointerBaseY = cy + r + 14f;
        sr.setColor(0.95f, 0.80f, 0.25f, 1f);
        sr.triangle(cx - 7f, pointerBaseY, cx + 7f, pointerBaseY, cx, pointerTipY);
        sr.setColor(isEvent ? new Color(1f, 0.25f, 0.25f, 1f) : new Color(0.3f, 0.9f, 1f, 1f));
        sr.circle(cx, pointerBaseY + 2f, 5f);
        sr.setColor(Color.WHITE);
        sr.circle(cx, pointerBaseY + 2f, 2.5f);
    }

    private void drawSetDetailModal() {
        float tipW = 700f;
        float tipH = 370f;
        float tipX = (W - tipW) / 2f;
        float tipY = (H - tipH) / 2f;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // 1. Semi-transparent backdrop to focus attention on modal
        sr.setColor(0f, 0f, 0f, 0.65f);
        sr.rect(0, 0, W, H);

        // 2. Drop shadow
        sr.setColor(0f, 0f, 0f, 0.8f);
        drawRoundedRect(sr, tipX + 6f, tipY - 6f, tipW, tipH, 12);

        // 3. Modal Background
        sr.setColor(0.07f, 0.10f, 0.16f, 0.98f);
        drawRoundedRect(sr, tipX, tipY, tipW, tipH, 12);

        StandardSetTheme curSet = isEventBanner ? null : STANDARD_SETS[currentStandardSetIndex];
        String setName = isEventBanner ? "Thần Khí Lạc Hồng" : curSet.setName;
        Color borderCol = isEventBanner ? new Color(1f, 0.65f, 0.3f, 1f) : curSet.primaryColor;

        // 4. Header strip
        sr.setColor(borderCol.r * 0.25f, borderCol.g * 0.25f, borderCol.b * 0.25f, 0.95f);
        sr.rect(tipX + 2f, tipY + tipH - 42f, tipW - 4f, 40f);

        // 5. Modal Border Outline
        sr.setColor(borderCol);
        drawRectOutline(sr, tipX, tipY, tipW, tipH, 2.5f);

        // Close button [X] at top-right
        float closeX = tipX + tipW - 38f;
        float closeY = tipY + tipH - 36f;
        float closeSize = 28f;
        sr.setColor(hoverCloseDetailModal ? new Color(0.9f, 0.25f, 0.25f, 0.95f) : new Color(0.2f, 0.25f, 0.35f, 0.9f));
        drawRoundedRect(sr, closeX, closeY, closeSize, closeSize, 6);
        sr.setColor(hoverCloseDetailModal ? Color.WHITE : new Color(0.7f, 0.8f, 0.9f, 1f));
        sr.rectLine(closeX + 7f, closeY + 7f, closeX + closeSize - 7f, closeY + closeSize - 7f, 2.2f);
        sr.rectLine(closeX + 7f, closeY + closeSize - 7f, closeX + closeSize - 7f, closeY + 7f, 2.2f);

        // 6. Centered 3 Item Boxes (width 200, height 205, gap 22 -> perfectly centered with 28px margins!)
        float boxW = 200f;
        float boxH = 205f;
        float boxY = tipY + 20f;
        float gap = 22f;
        float startX = tipX + (tipW - (3 * boxW + 2 * gap)) / 2f;

        for (int i = 0; i < 3; i++) {
            float bx = startX + i * (boxW + gap);

            // Item box container
            sr.setColor(0.05f, 0.07f, 0.12f, 0.96f);
            drawRoundedRect(sr, bx, boxY, boxW, boxH, 8);
            sr.setColor(borderCol.r, borderCol.g, borderCol.b, 0.55f);
            drawRectOutline(sr, bx, boxY, boxW, boxH, 1.5f);

            // Type badge pill at top
            sr.setColor(0.12f, 0.17f, 0.26f, 0.90f);
            drawRoundedRect(sr, bx + 35f, boxY + boxH - 30f, boxW - 70f, 20f, 4);

            // Aura glow behind 2x scaled icon
            float iconCX = bx + boxW / 2f;
            float iconCY = boxY + 105f;
            sr.setColor(borderCol.r, borderCol.g, borderCol.b, 0.18f);
            sr.circle(iconCX, iconCY, 46f);

            // 2X Zoomed Vector Icon (radius = 38f instead of 20f!)
            EquipmentType type = (i == 0 ? EquipmentType.ROD : (i == 1 ? EquipmentType.LINE : EquipmentType.HOOK));
            EquipmentIconRenderer.drawSetPiece(sr, setName, type, iconCX, iconCY, 38f, borderCol);
        }

        sr.end();

        game.batch.begin();

        // Title
        font.setColor(borderCol);
        font.getData().setScale(0.88f);
        String titleStr = "THÔNG TIN CHI TIẾT: BỘ " + setName.toUpperCase();
        layout.setText(font, titleStr);
        font.draw(game.batch, layout, tipX + 20f, tipY + tipH - 12f);

        // Sub-header 1: Cấp Điểm & Hệ số
        int points = isEventBanner ? 15 : curSet.points;
        float multVal = 1f + points * 0.15f;
        font.setColor(1f, 0.85f, 0.35f, 1f);
        font.getData().setScale(0.74f);
        String pointsStr = "✦ CẤP ĐIỂM BỘ: +" + points + " Cấp Điểm  (Hệ số Multiplier: x" + String.format(java.util.Locale.US, "%.2f", multVal) + " Sức mạnh cơ bản)";
        font.draw(game.batch, pointsStr, tipX + 22f, tipY + tipH - 52f);

        // Sub-header 2: Set Bonus description
        String perkDesc = isEventBanner ? 
            "[Ý Chí Bất Diệt] Vô hiệu hóa Kỹ Năng Boss, +50% Sát thương Boss" : 
            EquipmentGenerator.getSetBonusDesc(setName);
        font.setColor(0.45f, 0.95f, 0.65f, 1f);
        font.getData().setScale(0.74f);
        String bonusStr = "★ Hiệu ứng 3 món: " + perkDesc;
        font.draw(game.batch, bonusStr, tipX + 22f, tipY + tipH - 74f);

        // Inside the 3 boxes:
        String[] pNames = isEventBanner ?
            new String[]{"Trượng Lạc Hồng", "Dây Rồng Thiêng", "Móc Âu Cơ"} :
            new String[]{curSet.rodName, curSet.lineName, curSet.hookName};
        String[] pTypes = {"CẦN CÂU", "DÂY CƯỚC", "LƯỠI CÂU"};

        for (int i = 0; i < 3; i++) {
            float bx = startX + i * (boxW + gap);

            // Type badge text
            font.setColor(0.65f, 0.85f, 1f, 0.95f);
            font.getData().setScale(0.64f);
            drawCenteredText(font, pTypes[i], bx, boxY + boxH - 26f, boxW, 14f);

            // Item Name (Crisp & bold white, centered at bottom)
            font.setColor(Color.WHITE);
            font.getData().setScale(0.76f);
            drawCenteredText(font, pNames[i], bx + 6f, boxY + 22f, boxW - 12f, 18f);
        }

        font.getData().setScale(1f);
        game.batch.end();
    }

    private void drawRateAndLootModal() {
        float modalW = 1080f;
        float modalH = 580f;
        float modalX = (W - modalW) / 2f;
        float modalY = (H - modalH) / 2f;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Backdrop dark scrim
        sr.setColor(0f, 0f, 0f, 0.75f);
        sr.rect(0, 0, W, H);

        // Modal shadow & container
        sr.setColor(0f, 0f, 0f, 0.8f);
        drawRoundedRect(sr, modalX + 8f, modalY - 8f, modalW, modalH, 14);

        sr.setColor(0.07f, 0.09f, 0.15f, 0.98f);
        drawRoundedRect(sr, modalX, modalY, modalW, modalH, 14);

        // Header bar
        sr.setColor(0.12f, 0.16f, 0.25f, 0.95f);
        sr.rect(modalX + 2f, modalY + modalH - 52f, modalW - 4f, 50f);

        // Modal Border
        sr.setColor(isEventBanner ? new Color(1f, 0.65f, 0.35f, 0.9f) : new Color(0.35f, 0.75f, 1f, 0.9f));
        drawRectOutline(sr, modalX, modalY, modalW, modalH, 2.5f);

        // Close button [X]
        float closeX = modalX + modalW - 46f;
        float closeY = modalY + modalH - 44f;
        sr.setColor(hoverCloseModal ? new Color(0.85f, 0.25f, 0.25f, 1f) : new Color(0.3f, 0.15f, 0.18f, 0.9f));
        drawRoundedRect(sr, closeX, closeY, 34f, 34f, 6);
        sr.setColor(1f, 0.85f, 0.4f, 1f);
        drawRectOutline(sr, closeX, closeY, 34f, 34f, 1.2f);

        // Left Column (Rates): Width 310px
        float col1X = modalX + 18f;
        float col1W = 310f;
        float colH = modalH - 74f;
        float colY = modalY + 16f;

        sr.setColor(0.05f, 0.07f, 0.11f, 0.95f);
        drawRoundedRect(sr, col1X, colY, col1W, colH, 8);
        sr.setColor(0.2f, 0.3f, 0.45f, 0.6f);
        drawRectOutline(sr, col1X, colY, col1W, colH, 1.2f);

        // Divider line in Left Column
        sr.setColor(0.15f, 0.25f, 0.38f, 0.6f);
        sr.rectLine(col1X + 14f, colY + colH - 200f, col1X + col1W - 14f, colY + colH - 200f, 1.2f);

        // Right Column (Loot Table Sets): Width 720px
        float col2X = modalX + 342f;
        float col2W = modalW - 360f;
        sr.setColor(0.05f, 0.07f, 0.11f, 0.95f);
        drawRoundedRect(sr, col2X, colY, col2W, colH, 8);
        sr.setColor(0.2f, 0.3f, 0.45f, 0.6f);
        drawRectOutline(sr, col2X, colY, col2W, colH, 1.2f);

        // Gather all sets to display
        List<StandardSetTheme> displaySets = new ArrayList<>();
        if (isEventBanner) {
            displaySets.add(new StandardSetTheme("Thần Khí Lạc Hồng", "Trượng Lạc Hồng", "Dây Rồng Thiêng", "Móc Âu Cơ", 15,
                new Color(1f, 0.45f, 0.35f, 1f), new Color(0.85f, 0.25f, 0.2f, 1f), new Color(1f, 0.6f, 0.4f, 0.5f), "LIMITED"));
            displaySets.add(new StandardSetTheme("Vệ Quốc Vang Danh", "Cần Khởi Nghĩa", "Cước Vệ Quốc", "Lưỡi Sao Vàng", 5,
                new Color(1f, 0.85f, 0.35f, 1f), new Color(0.85f, 0.4f, 0.1f, 1f), new Color(1f, 0.8f, 0.3f, 0.5f), "EVENT"));
        }
        for (StandardSetTheme st : STANDARD_SETS) {
            displaySets.add(st);
        }

        float cardW = col2W - 24f; // 696f
        float cardH = 195f;
        float cardSpacing = 12f;
        float totalListH = displaySets.size() * (cardH + cardSpacing);
        float visibleListH = colH - 46f;
        float maxScrollY = Math.max(0f, totalListH - visibleListH);
        modalLootScrollY = MathUtils.clamp(modalLootScrollY, 0f, maxScrollY);

        // Scroll buttons background if maxScrollY > 0
        float btnUpX = col2X + col2W - 68f;
        float btnDownX = col2X + col2W - 36f;
        float btnY = colY + colH - 34f;
        if (maxScrollY > 0) {
            sr.setColor(hoverModalScrollUp ? new Color(0.25f, 0.4f, 0.6f, 1f) : new Color(0.12f, 0.18f, 0.28f, 0.9f));
            drawRoundedRect(sr, btnUpX, btnY, 28f, 22f, 4);
            sr.setColor(0.35f, 0.65f, 0.95f, 0.8f);
            drawRectOutline(sr, btnUpX, btnY, 28f, 22f, 1f);

            sr.setColor(hoverModalScrollDown ? new Color(0.25f, 0.4f, 0.6f, 1f) : new Color(0.12f, 0.18f, 0.28f, 0.9f));
            drawRoundedRect(sr, btnDownX, btnY, 28f, 22f, 4);
            sr.setColor(0.35f, 0.65f, 0.95f, 0.8f);
            drawRectOutline(sr, btnDownX, btnY, 28f, 22f, 1f);
        }

        // CRITICAL: Flush all non-clipped shapes first!
        sr.flush();

        // Clip the cards inside right column
        Rectangle clipBounds = new Rectangle(col2X + 4f, colY + 4f, col2W - 8f, visibleListH);
        Rectangle scissors = new Rectangle();
        ScissorStack.calculateScissors(camera, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight(), sr.getTransformMatrix(), clipBounds, scissors);
        boolean hasScissors = ScissorStack.pushScissors(scissors);

        if (hasScissors) {
            float startCardY = colY + colH - 44f - cardH + modalLootScrollY;
            float boxW = 216f;
            float boxH = 142f;
            float gap = 12f;

            for (int i = 0; i < displaySets.size(); i++) {
                StandardSetTheme st = displaySets.get(i);
                float cY = startCardY - i * (cardH + cardSpacing);
                float cX = col2X + 12f;

                if (cY + cardH < colY || cY > colY + colH) continue;

                // Card background
                sr.setColor(0.08f, 0.11f, 0.17f, 0.95f);
                drawRoundedRect(sr, cX, cY, cardW, cardH, 8);
                sr.setColor(st.primaryColor.r, st.primaryColor.g, st.primaryColor.b, 0.45f);
                drawRectOutline(sr, cX, cY, cardW, cardH, 1.2f);

                // 3 Vertical Showcase Item Boxes (Rod, Line, Hook)
                float boxY = cY + 12f;
                EquipmentType[] eqTypes = { EquipmentType.ROD, EquipmentType.LINE, EquipmentType.HOOK };

                for (int s = 0; s < 3; s++) {
                    float bx = cX + 12f + s * (boxW + gap);
                    sr.setColor(0.04f, 0.06f, 0.10f, 0.95f);
                    drawRoundedRect(sr, bx, boxY, boxW, boxH, 6);
                    sr.setColor(st.primaryColor.r, st.primaryColor.g, st.primaryColor.b, 0.5f);
                    drawRectOutline(sr, bx, boxY, boxW, boxH, 1f);

                    // Type badge pill at top of box
                    sr.setColor(0.09f, 0.13f, 0.20f, 0.9f);
                    drawRoundedRect(sr, bx + 24f, boxY + boxH - 24f, boxW - 48f, 18f, 4);

                    // Full detailed vector icon with aura (r = 34f!)
                    float iconCX = bx + boxW / 2f;
                    float iconCY = boxY + 68f;
                    sr.setColor(st.primaryColor.r, st.primaryColor.g, st.primaryColor.b, 0.20f);
                    sr.circle(iconCX, iconCY, 42f);

                    EquipmentIconRenderer.drawSetPiece(sr, st.setName, eqTypes[s], iconCX, iconCY, 34f, st.primaryColor);
                }
            }
            sr.flush(); // FLUSH BEFORE POPPING SCISSORS!
            ScissorStack.popScissors();
        }

        sr.end();

        // Modal Texts
        game.batch.begin();

        // Header Title
        titleFont.setColor(isEventBanner ? new Color(1f, 0.85f, 0.4f, 1f) : new Color(0.95f, 0.85f, 0.45f, 1f));
        titleFont.getData().setScale(0.85f);
        String mTitle = isEventBanner ? "XÁC SUẤT & KHO TRANG BỊ: SỰ KIỆN LẠC HỒNG" : "XÁC SUẤT & KHO TRANG BỊ: TIÊU CHUẨN";
        layout.setText(titleFont, mTitle);
        titleFont.draw(game.batch, layout, modalX + 22f, modalY + modalH - 18f);
        titleFont.getData().setScale(1f);

        // Close button text
        font.setColor(Color.WHITE);
        font.draw(game.batch, "✕", closeX + 11f, closeY + 24f);

        // --- Left Column: Rates breakdown (GUARANTEED VISIBLE, NOT SCISSORED) ---
        font.setColor(0.95f, 0.85f, 0.35f, 1f);
        font.getData().setScale(0.80f);
        font.draw(game.batch, "XÁC SUẤT PHẨM CHẤT (RATES)", col1X + 16f, colY + colH - 18f);

        font.getData().setScale(0.72f);
        float rLineY = colY + colH - 50f;
        if (!isEventBanner) {
            font.setColor(Rarity.COMMON.color);
            font.draw(game.batch, "• Common (1★): 50.0%", col1X + 16f, rLineY);
            font.setColor(Rarity.UNCOMMON.color);
            font.draw(game.batch, "• Uncommon (2★): 30.0%", col1X + 16f, rLineY - 25f);
            font.setColor(Rarity.RARE.color);
            font.draw(game.batch, "• Rare (3★): 15.0%", col1X + 16f, rLineY - 50f);
            font.setColor(Rarity.EPIC.color);
            font.draw(game.batch, "• Epic (4★): 4.0%", col1X + 16f, rLineY - 75f);
            font.setColor(Rarity.LEGENDARY.color);
            font.draw(game.batch, "• Legendary (5★): 1.0%", col1X + 16f, rLineY - 100f);

            // Pity Rules summary
            font.setColor(0.95f, 0.85f, 0.35f, 1f);
            font.getData().setScale(0.78f);
            font.draw(game.batch, "QUY TẮC BẢO HIỂM (PITY):", col1X + 16f, rLineY - 150f);
            font.getData().setScale(0.68f);
            font.setColor(0.80f, 0.88f, 0.98f, 0.92f);
            font.draw(game.batch, "• Chắc chắn nhận Rare+ sau 10 lượt", col1X + 16f, rLineY - 176f);
            font.draw(game.batch, "• Chắc chắn nhận Epic+ sau 50 lượt", col1X + 16f, rLineY - 200f);
            font.draw(game.batch, "• Chắc chắn nhận Legendary sau 100 lượt", col1X + 16f, rLineY - 224f);
            font.draw(game.batch, "• Quay 10 lượt luôn có tối thiểu 1 món 4★+", col1X + 16f, rLineY - 248f);
            font.draw(game.batch, "• Nhận đồ phẩm chất cao sẽ reset bảo hiểm", col1X + 16f, rLineY - 272f);
        } else {
            font.setColor(Rarity.COMMON.color);
            font.draw(game.batch, "• Common (1★): 20.0%", col1X + 16f, rLineY);
            font.setColor(Rarity.UNCOMMON.color);
            font.draw(game.batch, "• Uncommon (2★): 40.0%", col1X + 16f, rLineY - 25f);
            font.setColor(Rarity.RARE.color);
            font.draw(game.batch, "• Rare (3★): 30.0%", col1X + 16f, rLineY - 50f);
            font.setColor(Rarity.EPIC.color);
            font.draw(game.batch, "• Epic (4★): 8.5%", col1X + 16f, rLineY - 75f);
            font.setColor(new Color(1f, 0.45f, 0.35f, 1f));
            font.draw(game.batch, "• Thần Khí Lạc Hồng (6★): 1.5%", col1X + 16f, rLineY - 100f);

            // Limited Pity Rules
            font.setColor(0.95f, 0.85f, 0.35f, 1f);
            font.getData().setScale(0.78f);
            font.draw(game.batch, "QUY TẮC BẢO HIỂM GIỚI HẠN:", col1X + 16f, rLineY - 150f);
            font.getData().setScale(0.68f);
            font.setColor(0.80f, 0.88f, 0.98f, 0.92f);
            font.draw(game.batch, "• Chắc chắn nhận Thần Khí ở lượt 80", col1X + 16f, rLineY - 176f);
            font.draw(game.batch, "• Quay 10 lượt luôn có tối thiểu 1 món 4★+", col1X + 16f, rLineY - 200f);
            font.draw(game.batch, "• Tiến độ bảo hiểm được bảo lưu qua các đợt", col1X + 16f, rLineY - 224f);
            font.draw(game.batch, "• Sau sự kiện, trang bị giới hạn sẽ tạm đóng", col1X + 16f, rLineY - 248f);
        }

        // --- Right Column: Header & Scroll Buttons ---
        font.setColor(0.95f, 0.85f, 0.35f, 1f);
        font.getData().setScale(0.80f);
        font.draw(game.batch, "DANH SÁCH BỘ TRANG BỊ CÓ THỂ QUAY (LOOT TABLE)", col2X + 16f, colY + colH - 18f);

        if (maxScrollY > 0) {
            font.setColor(hoverModalScrollUp ? Color.WHITE : new Color(0.7f, 0.85f, 1f, 0.9f));
            font.getData().setScale(0.75f);
            font.draw(game.batch, "▲", btnUpX + 9f, btnY + 16f);

            font.setColor(hoverModalScrollDown ? Color.WHITE : new Color(0.7f, 0.85f, 1f, 0.9f));
            font.draw(game.batch, "▼", btnDownX + 9f, btnY + 16f);
        }

        // CRITICAL: Flush batch before clipping cards text!
        game.batch.flush();

        // Text cards pass with Scissor clipping
        ScissorStack.calculateScissors(camera, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight(), game.batch.getTransformMatrix(), clipBounds, scissors);
        boolean hasBatchScissors = ScissorStack.pushScissors(scissors);

        if (hasBatchScissors) {
            float startCardY = colY + colH - 44f - cardH + modalLootScrollY;
            float boxW = 216f;
            float boxH = 142f;
            float gap = 12f;

            for (int i = 0; i < displaySets.size(); i++) {
                StandardSetTheme st = displaySets.get(i);
                float cY = startCardY - i * (cardH + cardSpacing);
                float cX = col2X + 12f;

                if (cY + cardH < colY || cY > colY + colH) continue;

                float multVal = 1f + st.points * 0.15f;

                // 1. Top Title: Set Name & Points & Multiplier
                font.setColor(st.primaryColor);
                font.getData().setScale(0.85f);
                font.draw(game.batch, "✦ BỘ " + st.setName.toUpperCase(), cX + 16f, cY + cardH - 10f);

                font.setColor(1f, 0.85f, 0.35f, 1f);
                font.getData().setScale(0.72f);
                String badge = "[+" + st.points + " Cấp Điểm • Hệ số: x" + String.format(java.util.Locale.US, "%.2f", multVal) + "]";
                font.draw(game.batch, badge, cX + 220f, cY + cardH - 10f);

                font.setColor(0.45f, 0.95f, 0.65f, 1f);
                font.getData().setScale(0.68f);
                String bonus = "★ 3 Món: " + EquipmentGenerator.getSetBonusDesc(st.setName);
                font.draw(game.batch, bonus, cX + 16f, cY + cardH - 28f);

                // 2. 3 Vertical Showcase Item Boxes (Rod, Line, Hook)
                float boxY = cY + 12f;
                String[] pTypes = { "CẦN CÂU", "DÂY CƯỚC", "LƯỠI CÂU" };
                String[] pNames = { st.rodName, st.lineName, st.hookName };

                for (int s = 0; s < 3; s++) {
                    float bx = cX + 12f + s * (boxW + gap);

                    // Type subtitle in pill
                    font.setColor(0.7f, 0.85f, 1f, 0.85f);
                    font.getData().setScale(0.58f);
                    font.draw(game.batch, pTypes[s], bx + 4f, boxY + boxH - 10f, boxW - 8f, Align.center, false);

                    // Item Name under icon
                    font.setColor(Color.WHITE);
                    font.getData().setScale(0.72f);
                    font.draw(game.batch, pNames[s], bx + 4f, boxY + 22f, boxW - 8f, Align.center, false);
                }
            }

            game.batch.flush(); // FLUSH TEXT CARDS BEFORE POPPING!
            ScissorStack.popScissors();
        }

        font.getData().setScale(1f);
        game.batch.end();
    }

    private void handleInput() {
        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mousePos);
        mouseX = mousePos.x;
        mouseY = mousePos.y;

        // Hover tracking
        hoverBack = (mouseX >= 25f && mouseX <= 145f && mouseY >= H - 62f && mouseY <= H - 20f);
        hoverHelp = (mouseX >= 155f && mouseX <= 285f && mouseY >= H - 62f && mouseY <= H - 20f);

        hoverTabNormal = (mouseX >= 370f && mouseX <= 630f && mouseY >= H - 128f && mouseY <= H - 84f);
        hoverTabEvent = (mouseX >= 650f && mouseX <= 910f && mouseY >= H - 128f && mouseY <= H - 84f);

        hoverBuyTicket = (mouseX >= 220f && mouseX <= 460f && mouseY >= 32f && mouseY <= 86f);
        hoverPull1 = (mouseX >= 490f && mouseX <= 730f && mouseY >= 32f && mouseY <= 86f);
        hoverPull10 = (mouseX >= 760f && mouseX <= 1040f && mouseY >= 32f && mouseY <= 86f);

        // Set card & 3 slots hover
        float cardX = 140f;
        float cardY = 120f;
        float infoX = cardX + 460f;
        float infoY = cardY + 30f;
        float infoW = 1000f - 490f;
        float infoH = 430f - 60f;
        float showcaseCX = cardX + 235f;

        hoverSet = !showRateLootModal && (
            (mouseX >= infoX && mouseX <= infoX + infoW && mouseY >= infoY && mouseY <= infoY + infoH) ||
            (mouseX >= showcaseCX - 160f && mouseX <= showcaseCX + 160f && mouseY >= cardY + 20f && mouseY <= cardY + 144f)
        );

        // Set Detail Modal Close Button Hover
        float setDetailW = 700f;
        float setDetailH = 370f;
        float setDetailX = (W - setDetailW) / 2f;
        float setDetailY = (H - setDetailH) / 2f;
        float setDetailCloseX = setDetailX + setDetailW - 38f;
        float setDetailCloseY = setDetailY + setDetailH - 36f;
        float setDetailCloseSize = 28f;
        hoverCloseDetailModal = showSetDetailModal && (mouseX >= setDetailCloseX && mouseX <= setDetailCloseX + setDetailCloseSize && mouseY >= setDetailCloseY && mouseY <= setDetailCloseY + setDetailCloseSize);

        // Modal Close Button Hover
        float modalW = 1080f;
        float modalH = 580f;
        float modalX = (W - modalW) / 2f;
        float modalY = (H - modalH) / 2f;
        float closeX = modalX + modalW - 46f;
        float closeY = modalY + modalH - 44f;
        hoverCloseModal = showRateLootModal && (mouseX >= closeX && mouseX <= closeX + 34f && mouseY >= closeY && mouseY <= closeY + 34f);

        float col2X = modalX + 342f;
        float col2W = modalW - 360f;
        float colH = modalH - 74f;
        float colY = modalY + 16f;
        float btnUpX = col2X + col2W - 68f;
        float btnDownX = col2X + col2W - 36f;
        float btnY = colY + colH - 34f;
        hoverModalScrollUp = showRateLootModal && (mouseX >= btnUpX && mouseX <= btnUpX + 28f && mouseY >= btnY && mouseY <= btnY + 22f);
        hoverModalScrollDown = showRateLootModal && (mouseX >= btnDownX && mouseX <= btnDownX + 28f && mouseY >= btnY && mouseY <= btnY + 22f);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (showSetDetailModal) {
                showSetDetailModal = false;
                return;
            }
            if (showRateLootModal) {
                showRateLootModal = false;
                return;
            }
            if (state == State.SPINNING) {
                spinTimer = totalSpinTime; // Click to skip
            } else {
                AudioManager.getInstance().playSFX("sfx_button");
                game.setScreen(previousScreen);
            }
            return;
        }

        if (showRateLootModal) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                modalLootScrollY = Math.max(0, modalLootScrollY - 65f);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                modalLootScrollY += 65f;
            }
        }

        if (Gdx.input.justTouched()) {
            if (showSetDetailModal) {
                if (hoverCloseDetailModal || mouseX < setDetailX || mouseX > setDetailX + setDetailW || mouseY < setDetailY || mouseY > setDetailY + setDetailH) {
                    showSetDetailModal = false;
                    AudioManager.getInstance().playSFX("sfx_button");
                    return;
                }
                return;
            }

            if (showRateLootModal) {
                if (hoverCloseModal || mouseX < modalX || mouseX > modalX + modalW || mouseY < modalY || mouseY > modalY + modalH) {
                    showRateLootModal = false;
                    AudioManager.getInstance().playSFX("sfx_button");
                    return;
                }
                if (hoverModalScrollUp) {
                    modalLootScrollY = Math.max(0, modalLootScrollY - 65f);
                    AudioManager.getInstance().playSFX("sfx_button");
                    return;
                }
                if (hoverModalScrollDown) {
                    modalLootScrollY += 65f;
                    AudioManager.getInstance().playSFX("sfx_button");
                    return;
                }
                return;
            }

            if (state == State.SPINNING) {
                spinTimer = totalSpinTime; // Click anywhere to skip animation
                return;
            }

            if (hoverBack) {
                AudioManager.getInstance().playSFX("sfx_button");
                game.setScreen(previousScreen);
                return;
            }

            if (hoverHelp) {
                AudioManager.getInstance().playSFX("sfx_button");
                showRateLootModal = !showRateLootModal;
                showSetDetailModal = false;
                modalLootScrollY = 0f;
                return;
            }

            if (state == State.IDLE || state == State.RESULT) {
                if (hoverTabNormal && isEventBanner) {
                    AudioManager.getInstance().playSFX("sfx_button");
                    isEventBanner = false;
                    showSetDetailModal = false;
                    state = State.IDLE;
                    randomizeStandardSet();
                    return;
                }
                if (hoverTabEvent && !isEventBanner) {
                    AudioManager.getInstance().playSFX("sfx_button");
                    isEventBanner = true;
                    showSetDetailModal = false;
                    state = State.IDLE;
                    return;
                }

                if (hoverBuyTicket) {
                    showSetDetailModal = false;
                    buyTicket();
                    return;
                }
                if (hoverPull1) {
                    showSetDetailModal = false;
                    pullGacha(1);
                    return;
                }
                if (hoverPull10) {
                    showSetDetailModal = false;
                    pullGacha(10);
                    return;
                }

                if (state == State.IDLE && !showRateLootModal && hoverSet) {
                    showSetDetailModal = true;
                    AudioManager.getInstance().playSFX("sfx_button");
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
                StandardSetTheme theme = STANDARD_SETS[MathUtils.random(0, STANDARD_SETS.length - 1)];
                fake.setName = theme.setName;
                if (fake.type == EquipmentType.ROD) fake.name = theme.rodName;
                else if (fake.type == EquipmentType.LINE) fake.name = theme.lineName;
                else fake.name = theme.hookName;
                fake.level = player.getLevel();
                spinItems.add(fake);
            }
        }
    }

    private void drawCenteredText(BitmapFont f, String text, float x, float y, float w, float h) {
        layout.setText(f, text);
        f.draw(game.batch, text, x + (w - layout.width) / 2f, y + (h + layout.height) / 2f);
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
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                if (showRateLootModal) {
                    modalLootScrollY += amountY * 65f;
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override 
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }
}
