package com.fishingrpg.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
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

    // News Ticker / Marquee Banner
    private static final String[] TICKER_MESSAGES = {
        "Tin vực biển: Xuất hiện luồng cá Bạo Chúa ngoài khơi Đảo Hoàng Hôn vào ban đêm!",
        "Mẹo hay: Sử dụng [Mồi Thơm] giúp cá cắn câu nhanh gấp 3 lần và tiết kiệm thời gian!",
        "Lời khuyên: Canh chuẩn thanh giằng co phím Space để không bị đứt dây câu quý giá.",
        "Kỷ lục: Ngư dân kỳ cựu vừa trục vớt thành công [Rương Báu Hoàng Gia] chứa 500 Kim Cương!",
        "Thiên phú: Nhánh [Sức Mạnh] giúp rút ngắn thời gian giằng co với các loài thủy quái hung dữ.",
        "May mắn: Nâng cấp [Ngư Dân Tinh Anh] sẽ tăng mạnh cơ hội bắt được cá Huyền Thoại!",
        "Bí kíp: Thu thập đủ [Sách Kỹ Năng] để khai mở các bí thuật thượng cổ của dòng họ Ngư Phủ.",
        "Thời tiết: Biển êm gió lặng, cực kỳ thích hợp để ra khơi săn cá Rồng và Cá Voi Xanh!",
        "Sự kiện: Điểm danh hàng ngày và hoàn thành nhiệm vụ tuần để nhận Rương Ngọc Quý!",
        "Cần câu: Cần cấp cao không chỉ tăng sát thương mà còn tăng độ bền bỉ của dây câu.",
        "Kỹ năng: Chiêu thức [Trói Buộc] có thể đóng băng cá tức thời khi thanh Tension sắp đạt đỉnh!",
        "Hồi phục: Khi cạn thể lực, hãy dùng [Bánh Mì] hoặc [Thuốc Hồi Stamina] tại Phím Tắt (1-9)."
    };

    private static final float TICKER_X = 215f;
    private static final float TICKER_Y = 595f;
    private static final float TICKER_W = 990f;
    private static final float TICKER_H = 28f;
    private static final float TICKER_SPEED = 70f;
    private float tickerOffset = TICKER_W - 150f;
    private int tickerMsgIndex = 0;
    private final Rectangle tickerClipBounds = new Rectangle();
    private final Rectangle tickerScissors = new Rectangle();

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

        // News Ticker Marquee Ribbon
        drawNewsTickerShapes();

        // 4. Primary "RA KHƠI CÂU CÁ" button in IDLE
        drawCastButton("RA KHƠI CÂU CÁ");

        // 5. Back to Menu Button
        drawMenuBtn();

        // 6. Player Profile & Resource Dashboard (Bottom Left)
        drawPlayerProfileCardShapes(player);

        // 7. Quick Slots Inventory (Bottom Right)
        drawInventoryShapes(player);
    }

    private void drawNewsTickerShapes() {
        // Outer ticker container
        sr.setColor(0.06f, 0.08f, 0.14f, 0.88f);
        sr.rect(TICKER_X, TICKER_Y, TICKER_W, TICKER_H);
        sr.setColor(0.85f, 0.65f, 0.25f, 0.5f);
        drawBoxBorder(TICKER_X, TICKER_Y, TICKER_W, TICKER_H, 1.2f);

        // Left Tag pill
        float tagW = 125f;
        sr.setColor(0.14f, 0.18f, 0.28f, 0.95f);
        sr.rect(TICKER_X, TICKER_Y, tagW, TICKER_H);
        sr.setColor(0.85f, 0.65f, 0.25f, 0.65f);
        sr.rect(TICKER_X + tagW, TICKER_Y, 2f, TICKER_H);

        // Pulsing amber beacon dot
        float pulse = 0.7f + 0.3f * MathUtils.sin(timer * 5f);
        sr.setColor(1f, 0.82f, 0.2f, pulse);
        sr.circle(TICKER_X + 16f, TICKER_Y + TICKER_H / 2f, 4.5f);
        sr.setColor(1f, 1f, 1f, 0.9f);
        sr.circle(TICKER_X + 16f, TICKER_Y + TICKER_H / 2f, 2f);
    }

    private void drawScenery() {
        // -------------------------------------------------------------
        // 1. SKY MULTI-BAND GRADIENT (Y: 320 to 720, height 400)
        // -------------------------------------------------------------
        float skyBaseY = 320f;
        float skyHeight = H - skyBaseY; // 400f
        int skyBands = 20;
        float bandH = skyHeight / skyBands;

        // Key sunset colors from zenith to horizon:
        // Zenith: Deep dusk indigo (0.16, 0.08, 0.24)
        // Mid-zenith: Twilight purple (0.32, 0.12, 0.36)
        // Upper dusk: Rich crimson-wine (0.58, 0.16, 0.30)
        // Mid dusk: Sunset coral flame (0.80, 0.24, 0.20)
        // Low sky: Vivid amber orange (0.96, 0.48, 0.14)
        // Horizon: Radiant warm gold (1.00, 0.72, 0.26)
        for (int i = 0; i < skyBands; i++) {
            float t = (float) i / (skyBands - 1); // 0 at horizon, 1 at top
            float r, g, b;
            if (t > 0.7f) {
                float f = (t - 0.7f) / 0.3f;
                r = MathUtils.lerp(0.32f, 0.16f, f);
                g = MathUtils.lerp(0.12f, 0.08f, f);
                b = MathUtils.lerp(0.36f, 0.24f, f);
            } else if (t > 0.45f) {
                float f = (t - 0.45f) / 0.25f;
                r = MathUtils.lerp(0.58f, 0.32f, f);
                g = MathUtils.lerp(0.16f, 0.12f, f);
                b = MathUtils.lerp(0.30f, 0.36f, f);
            } else if (t > 0.25f) {
                float f = (t - 0.25f) / 0.20f;
                r = MathUtils.lerp(0.80f, 0.58f, f);
                g = MathUtils.lerp(0.24f, 0.16f, f);
                b = MathUtils.lerp(0.20f, 0.30f, f);
            } else if (t > 0.10f) {
                float f = (t - 0.10f) / 0.15f;
                r = MathUtils.lerp(0.96f, 0.80f, f);
                g = MathUtils.lerp(0.48f, 0.24f, f);
                b = MathUtils.lerp(0.14f, 0.20f, f);
            } else {
                float f = t / 0.10f;
                r = MathUtils.lerp(1.00f, 0.96f, f);
                g = MathUtils.lerp(0.72f, 0.48f, f);
                b = MathUtils.lerp(0.26f, 0.14f, f);
            }
            sr.setColor(r, g, b, 1f);
            sr.rect(0, skyBaseY + i * bandH, W, bandH + 1f);
        }

        // -------------------------------------------------------------
        // 2. SETTING SUN & CORONAL HALO (Center of horizon)
        // -------------------------------------------------------------
        float sunX = 540f;
        float sunY = 320f + 4f * MathUtils.sin(timer * 0.4f);

        // Atmospheric glowing haze halos
        sr.setColor(1.0f, 0.60f, 0.15f, 0.14f);
        sr.circle(sunX, sunY, 140f, 40);
        sr.setColor(1.0f, 0.68f, 0.18f, 0.24f);
        sr.circle(sunX, sunY, 100f, 40);
        sr.setColor(1.0f, 0.78f, 0.22f, 0.42f);
        sr.circle(sunX, sunY, 70f, 36);
        sr.setColor(1.0f, 0.90f, 0.35f, 0.90f);
        sr.circle(sunX, sunY, 48f, 36);
        sr.setColor(1.0f, 0.98f, 0.82f, 1.0f);
        sr.circle(sunX, sunY, 32f, 32);

        // -------------------------------------------------------------
        // 3. DISTANT MOUNTAIN SILHOUETTES ON HORIZON
        // -------------------------------------------------------------
        // Layer 1: Distant misty peaks in purple-orange haze
        sr.setColor(0.36f, 0.14f, 0.26f, 0.70f);
        sr.triangle(80f, 320f, 220f, 375f, 360f, 320f);
        sr.triangle(280f, 320f, 420f, 360f, 560f, 320f);
        sr.triangle(650f, 320f, 780f, 385f, 920f, 320f);
        sr.triangle(840f, 320f, 980f, 395f, 1140f, 320f);
        sr.triangle(1060f, 320f, 1190f, 365f, 1280f, 320f);

        // Layer 2: Closer coastal headland silhouettes
        sr.setColor(0.24f, 0.10f, 0.20f, 0.85f);
        sr.triangle(0f, 320f, 140f, 355f, 280f, 320f);
        sr.triangle(600f, 320f, 720f, 345f, 850f, 320f);
        sr.triangle(960f, 320f, 1080f, 350f, 1220f, 320f);

        // -------------------------------------------------------------
        // 4. VOLUMETRIC SUNSET CLOUDS (Lit by setting sun)
        // -------------------------------------------------------------
        drawCloudCluster((timer * 10f) % (W + 300f) - 150f, 520f, 1.1f);
        drawCloudCluster((timer * 14f + 380f) % (W + 300f) - 150f, 470f, 0.85f);
        drawCloudCluster((timer * 12f + 760f) % (W + 300f) - 150f, 545f, 1.25f);
        drawCloudCluster((timer * 8f + 1050f) % (W + 300f) - 150f, 430f, 0.70f);

        // -------------------------------------------------------------
        // 5. COASTAL CLIFF & LIGHTHOUSE ON LEFT
        // -------------------------------------------------------------
        sr.setColor(0.12f, 0.08f, 0.12f, 1f);
        sr.triangle(70f, 320f, 130f, 355f, 190f, 320f);
        sr.rect(95f, 320f, 70f, 30f);

        float lhX = 125f;
        float lhBaseY = 350f;
        sr.setColor(0.85f, 0.82f, 0.80f, 1f);
        sr.rect(lhX - 7f, lhBaseY, 14f, 35f);
        sr.setColor(0.75f, 0.18f, 0.18f, 1f);
        sr.rect(lhX - 7f, lhBaseY + 12f, 14f, 10f);
        sr.setColor(0.15f, 0.12f, 0.12f, 1f);
        sr.rect(lhX - 9f, lhBaseY + 35f, 18f, 4f);
        sr.setColor(1.0f, 0.95f, 0.60f, 0.95f);
        sr.rect(lhX - 6f, lhBaseY + 39f, 12f, 10f);
        sr.setColor(0.20f, 0.15f, 0.15f, 1f);
        sr.triangle(lhX - 7f, lhBaseY + 49f, lhX, lhBaseY + 57f, lhX + 7f, lhBaseY + 49f);
        sr.setColor(1.0f, 0.90f, 0.40f, 0.8f);
        sr.circle(lhX, lhBaseY + 44f, 7f, 16);

        // Sweeping rotating light beam across dusk sky
        float sweepAngle = 26f * MathUtils.sin(timer * 0.7f) - 18f;
        float beamLength = 260f;
        float rad1 = MathUtils.degreesToRadians * (sweepAngle - 7f);
        float rad2 = MathUtils.degreesToRadians * (sweepAngle + 7f);
        float bx1 = lhX + beamLength * MathUtils.cos(rad1);
        float by1 = (lhBaseY + 44f) + beamLength * MathUtils.sin(rad1);
        float bx2 = lhX + beamLength * MathUtils.cos(rad2);
        float by2 = (lhBaseY + 44f) + beamLength * MathUtils.sin(rad2);
        float beamPulse = 0.22f + 0.12f * MathUtils.sin(timer * 3f);
        sr.setColor(1.0f, 0.92f, 0.55f, beamPulse);
        sr.triangle(lhX, lhBaseY + 44f, bx1, by1, bx2, by2);

        // -------------------------------------------------------------
        // 6. DEEP OCEAN WATER & SUNSET DEPTH GRADIENT (Y: 0 to 320)
        // -------------------------------------------------------------
        int seaBands = 14;
        float seaBandH = skyBaseY / seaBands;
        for (int i = 0; i < seaBands; i++) {
            float t = (float) i / (seaBands - 1);
            float r = MathUtils.lerp(0.03f, 0.13f, t);
            float g = MathUtils.lerp(0.06f, 0.18f, t);
            float b = MathUtils.lerp(0.14f, 0.32f, t);
            sr.setColor(r, g, b, 1f);
            sr.rect(0, i * seaBandH, W, seaBandH + 1f);
        }

        // -------------------------------------------------------------
        // 7. GOLDEN SUN REFLECTION SHIMMER TRAIL (Under Sun on ocean)
        // -------------------------------------------------------------
        for (int j = 0; j < 16; j++) {
            float y = 316f - j * 10f;
            if (y < 140f) break;
            float progress = (float) j / 16f;
            float barW = 35f + progress * 140f;
            float shimmerX = sunX + 8f * MathUtils.sin(timer * 3.5f + j * 0.8f);
            float alpha = (1f - progress * 0.75f) * (0.45f + 0.25f * MathUtils.sin(timer * 4.5f + j * 1.2f));
            sr.setColor(1.0f, 0.75f, 0.25f, alpha);
            sr.rect(shimmerX - barW / 2f, y, barW, 2.5f + progress * 1.5f);
        }

        // -------------------------------------------------------------
        // 8. SEABIRDS & ANIMATED OCEAN WAVES (Line mode)
        // -------------------------------------------------------------
        sr.end();
        sr.begin(ShapeRenderer.ShapeType.Line);

        // Seabirds (Flock of 5 gulls)
        sr.setColor(0.18f, 0.08f, 0.12f, 0.75f);
        for (int j = 0; j < 5; j++) {
            float birdX = W - ((timer * 35f + j * 220f) % (W + 220f)) + 100f;
            float birdY = 430f + 25f * MathUtils.sin(timer * 1.8f + j * 1.2f);
            float flap = 9f * MathUtils.sin(timer * 7f + j * 1.8f);
            sr.line(birdX, birdY, birdX + 8f, birdY + flap);
            sr.line(birdX + 8f, birdY + flap, birdX + 16f, birdY);
        }

        // Rolling ocean swells
        float step = 6f;
        sr.setColor(0.38f, 0.58f, 0.78f, 0.75f);
        for (float x = 0; x < W - step; x += step) {
            float y1 = 320f + 4f * MathUtils.sin((x       + timer * 45f) * 0.025f);
            float y2 = 320f + 4f * MathUtils.sin((x + step + timer * 45f) * 0.025f);
            sr.line(x, y1, x + step, y2);
        }
        sr.setColor(0.18f, 0.42f, 0.62f, 0.60f);
        for (float x = 0; x < W - step; x += step) {
            float y1 = 295f + 6f * MathUtils.sin((x       + timer * 32f + 40f) * 0.018f);
            float y2 = 295f + 6f * MathUtils.sin((x + step + timer * 32f + 40f) * 0.018f);
            sr.line(x, y1, x + step, y2);
        }
        sr.setColor(0.10f, 0.28f, 0.48f, 0.50f);
        for (float x = 0; x < W - step; x += step) {
            float y1 = 265f + 7f * MathUtils.sin((x       + timer * 25f + 90f) * 0.015f);
            float y2 = 265f + 7f * MathUtils.sin((x + step + timer * 25f + 90f) * 0.015f);
            sr.line(x, y1, x + step, y2);
        }

        // Rigging ropes on fishing boat
        float boatBob = 5f * MathUtils.sin(timer * 2.2f);
        float boatBx = 920f;
        float boatBy = 280f + boatBob;
        sr.setColor(0.12f, 0.10f, 0.10f, 0.75f);
        sr.line(boatBx + 45f, boatBy + 190f, boatBx - 30f, boatBy + 48f);
        sr.line(boatBx + 45f, boatBy + 190f, boatBx + 220f, boatBy + 48f);
        sr.line(boatBx + 45f, boatBy + 190f, boatBx + 115f, boatBy + 110f);

        sr.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // -------------------------------------------------------------
        // 9. DETAILED WOODEN FISHING TRAWLER (Bobbing on right)
        // -------------------------------------------------------------
        // Hull reflection in water
        sr.setColor(0.08f, 0.10f, 0.16f, 0.45f);
        sr.rect(boatBx - 20f, boatBy - 30f, 260f, 30f);

        // Lower Hull
        sr.setColor(0.14f, 0.12f, 0.16f, 1f);
        sr.rect(boatBx, boatBy, 210f, 48f);
        sr.triangle(boatBx - 35f, boatBy + 48f, boatBx, boatBy + 48f, boatBx, boatBy);
        sr.triangle(boatBx + 210f, boatBy + 48f, boatBx + 245f, boatBy + 48f, boatBx + 210f, boatBy);

        // Waterline stripe
        sr.setColor(0.72f, 0.22f, 0.18f, 1f);
        sr.rect(boatBx - 20f, boatBy + 12f, 250f, 6f);

        // Upper deck bulwark rail
        sr.setColor(0.25f, 0.20f, 0.18f, 1f);
        sr.rect(boatBx - 30f, boatBy + 44f, 270f, 5f);

        // Wheelhouse Cabin
        float cabinX = boatBx + 110f;
        float cabinY = boatBy + 48f;
        sr.setColor(0.82f, 0.78f, 0.72f, 1f);
        sr.rect(cabinX, cabinY, 95f, 62f);
        sr.setColor(0.22f, 0.25f, 0.32f, 1f);
        sr.rect(cabinX - 5f, cabinY + 62f, 105f, 9f);
        // Cabin windows
        sr.setColor(1.0f, 0.88f, 0.45f, 0.95f);
        sr.rect(cabinX + 15f, cabinY + 22f, 28f, 24f);
        sr.rect(cabinX + 55f, cabinY + 22f, 26f, 24f);
        sr.setColor(0.22f, 0.18f, 0.15f, 1f);
        sr.rect(cabinX + 28f, cabinY + 22f, 2f, 24f);
        sr.rect(cabinX + 67f, cabinY + 22f, 2f, 24f);

        // Foredeck Crane / Trawler Rigging Mast
        float mastX = boatBx + 42f;
        float mastY = boatBy + 48f;
        sr.setColor(0.18f, 0.15f, 0.12f, 1f);
        sr.rect(mastX, mastY, 7f, 145f);
        sr.rect(mastX - 18f, mastY + 115f, 44f, 5f);
        sr.setColor(0.9f, 0.9f, 0.9f, 1f);
        sr.rect(cabinX + 38f, cabinY + 71f, 18f, 7f);

        // Waving red maritime pennant flag atop mast
        float flagWave = 4f * MathUtils.sin(timer * 6f);
        sr.setColor(0.90f, 0.22f, 0.20f, 1f);
        sr.triangle(mastX + 7f, mastY + 145f, mastX + 7f, mastY + 125f, mastX + 42f + flagWave, mastY + 135f);

        // -------------------------------------------------------------
        // 10. WEATHERED WOODEN PIER / FISHING WHARF (Bottom Left)
        // -------------------------------------------------------------
        float dockBob = 1.5f * MathUtils.sin(timer * 0.7f);
        float dockTopY = 275f + dockBob;

        // Vertical wooden pilings
        sr.setColor(0.14f, 0.09f, 0.06f, 1f);
        sr.rect(55f, 0f, 22f, dockTopY);
        sr.rect(145f, 0f, 22f, dockTopY);
        sr.rect(235f, 0f, 22f, dockTopY);

        // Cross-bracing
        sr.setColor(0.18f, 0.12f, 0.08f, 1f);
        sr.rectLine(55f, 30f, 167f, dockTopY - 20f, 8f);
        sr.rectLine(145f, 30f, 257f, dockTopY - 20f, 8f);

        // Under-deck planking mass
        sr.setColor(0.22f, 0.14f, 0.09f, 1f);
        sr.rect(0f, 0f, 275f, dockTopY);

        // Pier Deck Planks
        sr.setColor(0.38f, 0.25f, 0.16f, 1f);
        sr.rect(0f, dockTopY - 14f, 285f, 14f);
        sr.setColor(0.52f, 0.36f, 0.24f, 0.9f);
        sr.rect(0f, dockTopY - 2f, 285f, 3f);
        sr.setColor(0.12f, 0.07f, 0.04f, 0.8f);
        for (int p = 20; p < 280; p += 32) {
            sr.rect(p, dockTopY - 14f, 2f, 14f);
        }

        // Mooring Bollard
        sr.setColor(0.18f, 0.16f, 0.16f, 1f);
        sr.rect(260f, dockTopY, 14f, 16f);
        sr.rect(255f, dockTopY + 12f, 24f, 5f);

        // Hanging nautical brass lantern
        float lanternSwing = 2.5f * MathUtils.sin(timer * 2.0f);
        float lx = 190f + lanternSwing;
        float ly = dockTopY - 45f;
        sr.setColor(1.0f, 0.85f, 0.35f, 0.30f);
        sr.circle(lx, ly + 8f, 24f, 20);
        sr.setColor(0.70f, 0.52f, 0.18f, 1f);
        sr.rect(lx - 6f, ly, 12f, 16f);
        sr.setColor(1.0f, 0.95f, 0.60f, 1f);
        sr.circle(lx, ly + 8f, 4f, 12);
    }

    private void drawCloudCluster(float cx, float cy, float scale) {
        // Lower shadow (dusk purple-slate)
        sr.setColor(0.42f, 0.20f, 0.32f, 0.55f);
        sr.circle(cx - 30f * scale, cy - 4f * scale, 24f * scale, 24);
        sr.circle(cx, cy - 2f * scale, 34f * scale, 28);
        sr.circle(cx + 38f * scale, cy - 5f * scale, 26f * scale, 24);
        sr.circle(cx + 70f * scale, cy - 8f * scale, 18f * scale, 20);

        // Upper warm sunlit highlight (golden peach)
        sr.setColor(0.95f, 0.68f, 0.52f, 0.65f);
        sr.circle(cx - 30f * scale, cy + 4f * scale, 22f * scale, 24);
        sr.circle(cx, cy + 8f * scale, 31f * scale, 28);
        sr.circle(cx + 38f * scale, cy + 5f * scale, 23f * scale, 24);
        sr.circle(cx + 70f * scale, cy + 2f * scale, 16f * scale, 20);
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
        // 0: Quest - Ancient Golden Parchment Scroll with Red Wax Seal
        float qx = 290f, qy = 676f;
        sr.setColor(1f, 0.85f, 0.3f, 0.25f);
        sr.circle(qx, qy, 15f);

        // Parchment scroll rolled ends (Left & Right golden rods)
        sr.setColor(0.72f, 0.52f, 0.18f, 1f);
        sr.rect(qx - 14f, qy - 10f, 3f, 20f);
        sr.rect(qx + 11f, qy - 10f, 3f, 20f);
        sr.circle(qx - 12.5f, qy + 10f, 2.5f);
        sr.circle(qx - 12.5f, qy - 10f, 2.5f);
        sr.circle(qx + 12.5f, qy + 10f, 2.5f);
        sr.circle(qx + 12.5f, qy - 10f, 2.5f);

        // Parchment paper sheet
        sr.setColor(0.96f, 0.92f, 0.80f, 1f);
        sr.rect(qx - 11f, qy - 8f, 22f, 16f);

        // Parchment text runes/lines
        sr.setColor(0.55f, 0.42f, 0.28f, 0.85f);
        sr.rect(qx - 8f, qy + 3f, 16f, 1.8f);
        sr.rect(qx - 8f, qy - 0.5f, 12f, 1.8f);
        sr.rect(qx - 8f, qy - 4f, 14f, 1.8f);

        // Red Wax Seal at bottom center with ribbon tails
        sr.setColor(0.85f, 0.18f, 0.18f, 1f);
        sr.triangle(qx - 3f, qy - 6f, qx - 7f, qy - 13f, qx - 1f, qy - 9f);
        sr.triangle(qx + 3f, qy - 6f, qx + 7f, qy - 13f, qx + 1f, qy - 9f);
        sr.circle(qx, qy - 6f, 4.5f);
        sr.setColor(1f, 0.88f, 0.3f, 1f);
        sr.circle(qx, qy - 6f, 2f);

        // 1: Equipment - Radiant Mythic Greatsword with Winged Guard & Gem
        float eqx = 430f, eqy = 676f;
        sr.setColor(0.3f, 0.7f, 1f, 0.25f);
        sr.circle(eqx, eqy, 15f);

        // Sword Blade (Pointing up)
        sr.setColor(0.92f, 0.96f, 1f, 1f);
        sr.rect(eqx - 2.5f, eqy - 3f, 5f, 14f);
        sr.triangle(eqx - 2.5f, eqy + 11f, eqx + 2.5f, eqy + 11f, eqx, eqy + 17f);

        // Central blade fuller (Blue gleam)
        sr.setColor(0.45f, 0.72f, 0.95f, 1f);
        sr.rect(eqx - 0.8f, eqy - 2f, 1.6f, 13f);

        // Winged Golden Crossguard
        sr.setColor(1f, 0.82f, 0.22f, 1f);
        sr.rect(eqx - 9f, eqy - 4f, 18f, 3.5f);
        sr.triangle(eqx - 9f, eqy - 4f, eqx - 12f, eqy, eqx - 9f, eqy - 0.5f);
        sr.triangle(eqx + 9f, eqy - 4f, eqx + 12f, eqy, eqx + 9f, eqy - 0.5f);

        // Cyan Mana Core Gem in center of guard
        sr.setColor(0.2f, 0.95f, 1f, 1f);
        sr.circle(eqx, eqy - 2.2f, 2.5f);

        // Leather Hilt & Golden Pommel
        sr.setColor(0.35f, 0.22f, 0.15f, 1f);
        sr.rect(eqx - 1.5f, eqy - 11f, 3f, 7f);
        sr.setColor(1f, 0.82f, 0.22f, 1f);
        sr.circle(eqx, eqy - 12f, 3f);

        // 2: Skills - Arcane Lightning Bolt with Solar Core
        float skx = 570f, sky = 676f;
        sr.setColor(1f, 0.75f, 0.15f, 0.25f);
        sr.circle(skx, sky, 15f);

        // Plasma disc background
        sr.setColor(0.35f, 0.18f, 0.08f, 0.8f);
        sr.circle(skx, sky, 12f);
        sr.setColor(1f, 0.65f, 0.15f, 0.5f);
        sr.circle(skx, sky, 9f);

        // Jagged Lightning Bolt (3 sharp connected segments)
        sr.setColor(1f, 0.55f, 0.08f, 1f);
        sr.triangle(skx + 2f, sky + 14f, skx - 5f, sky + 3f, skx + 4f, sky + 3f);
        sr.triangle(skx - 4f, sky + 4f, skx + 3f, sky - 2f, skx - 5f, sky - 2f);
        sr.triangle(skx + 1f, sky - 1f, skx - 3f, sky - 1f, skx - 2f, sky - 14f);

        // Bright Golden-white core
        sr.setColor(1f, 0.98f, 0.75f, 1f);
        sr.triangle(skx + 1.5f, sky + 12f, skx - 3.5f, sky + 4f, skx + 2.5f, sky + 4f);
        sr.triangle(skx - 3f, sky + 5f, skx + 2f, sky - 1f, skx - 3.5f, sky - 1f);
        sr.triangle(skx + 0.5f, sky, skx - 2f, sky, skx - 1.5f, sky - 11f);

        // Radiating electric sparks
        sr.setColor(1f, 0.95f, 0.4f, 1f);
        sr.circle(skx + 8f, sky + 7f, 1.5f);
        sr.circle(skx - 8f, sky - 5f, 1.5f);
        sr.circle(skx + 7f, sky - 8f, 1.2f);

        // 3: Gacha - Royal Treasure Chest with Golden Trim & Gem Lock
        float gcx = 710f, gcy = 675f;
        sr.setColor(0.95f, 0.35f, 0.7f, 0.25f);
        sr.circle(gcx, gcy, 15f);

        // Chest lower body (Royal purple wood)
        sr.setColor(0.38f, 0.12f, 0.28f, 1f);
        sr.rect(gcx - 12f, gcy - 10f, 24f, 13f);

        // Curved Chest Lid
        sr.setColor(0.52f, 0.18f, 0.38f, 1f);
        sr.rect(gcx - 13f, gcy + 3f, 26f, 8f);
        sr.circle(gcx, gcy + 11f, 6f);

        // Golden Trim & Corner Straps
        sr.setColor(1f, 0.82f, 0.22f, 1f);
        sr.rect(gcx - 12f, gcy - 10f, 3.5f, 13f);
        sr.rect(gcx + 8.5f, gcy - 10f, 3.5f, 13f);
        sr.rect(gcx - 14f, gcy + 2f, 28f, 2.5f);
        sr.rect(gcx - 2f, gcy + 3f, 4f, 9f);

        // Golden Lock Escutcheon & Ruby Core
        sr.setColor(1f, 0.85f, 0.3f, 1f);
        sr.rect(gcx - 3.5f, gcy - 2f, 7f, 7f);
        sr.setColor(0.95f, 0.2f, 0.4f, 1f);
        sr.circle(gcx, gcy + 1.5f, 2f);

        // 4: Shop - Merchant Velvet Gold Pouch & Gleaming Coins
        float shx = 850f, shy = 675f;
        sr.setColor(0.2f, 0.85f, 0.4f, 0.25f);
        sr.circle(shx, shy, 15f);

        // Pouch Gathered Top Ruffle
        sr.setColor(0.16f, 0.48f, 0.25f, 1f);
        sr.triangle(shx - 7f, shy + 6f, shx, shy + 12f, shx + 7f, shy + 6f);
        sr.triangle(shx - 9f, shy + 10f, shx - 4f, shy + 6f, shx - 2f, shy + 11f);
        sr.triangle(shx + 9f, shy + 10f, shx + 4f, shy + 6f, shx + 2f, shy + 11f);

        // Tied Golden Rope Cord with Tassels
        sr.setColor(1f, 0.85f, 0.25f, 1f);
        sr.rect(shx - 6f, shy + 5f, 12f, 2.2f);
        sr.rectLine(shx - 4f, shy + 5f, shx - 8f, shy - 1f, 1.8f);
        sr.circle(shx - 8f, shy - 1.5f, 1.5f);

        // Pouch Plump Belly
        sr.setColor(0.22f, 0.60f, 0.32f, 1f);
        sr.circle(shx, shy - 2f, 9.5f);

        // Big Gleaming Gold Coin in front
        sr.setColor(1f, 0.85f, 0.2f, 1f);
        sr.circle(shx + 2f, shy - 2f, 6.5f);
        sr.setColor(0.85f, 0.65f, 0.12f, 1f);
        sr.circle(shx + 2f, shy - 2f, 4.5f);
        sr.setColor(1f, 0.95f, 0.5f, 1f);
        sr.rect(shx + 2f - 1.5f, shy - 2f - 1.5f, 1.5f, 1.5f, 3f, 3f, 1f, 1f, 45f);

        // 5: Catalog - Ancient Fisherman's Compendium with Gilded Fish Emblem
        float cax = 990f, cay = 676f;
        sr.setColor(0.7f, 0.35f, 0.95f, 0.25f);
        sr.circle(cax, cay, 15f);

        // Book Cover (Deep Arcane Leather)
        sr.setColor(0.38f, 0.16f, 0.58f, 1f);
        sr.rect(cax - 11f, cay - 11f, 22f, 22f);

        // Parchment Page Edge on Right
        sr.setColor(0.96f, 0.92f, 0.82f, 1f);
        sr.rect(cax + 9f, cay - 9.5f, 3f, 19f);

        // Gilded Spine on Left
        sr.setColor(0.85f, 0.65f, 0.20f, 1f);
        sr.rect(cax - 12f, cay - 11f, 3.5f, 22f);
        sr.setColor(0.3f, 0.12f, 0.45f, 1f);
        sr.rect(cax - 12f, cay + 4f, 3.5f, 1.8f);
        sr.rect(cax - 12f, cay - 6f, 3.5f, 1.8f);

        // Golden Corner Brackets
        sr.setColor(1f, 0.85f, 0.28f, 1f);
        sr.triangle(cax - 9f, cay + 11f, cax - 4f, cay + 11f, cax - 9f, cay + 6f);
        sr.triangle(cax + 7f, cay + 11f, cax + 7f, cay + 6f, cax + 2f, cay + 11f);
        sr.triangle(cax + 7f, cay - 11f, cax + 7f, cay - 6f, cax + 2f, cay - 11f);

        // Golden Fish Insignia in center of cover
        sr.setColor(1f, 0.88f, 0.35f, 1f);
        sr.triangle(cax - 4f, cay, cax + 3f, cay + 3.5f, cax + 3f, cay - 3.5f);
        sr.triangle(cax + 3f, cay, cax + 6.5f, cay + 3.5f, cax + 6.5f, cay - 3.5f);
        sr.circle(cax - 1.5f, cay + 1f, 1f);

        // Red Silk Bookmark Ribbon dangling out bottom
        sr.setColor(0.9f, 0.2f, 0.25f, 1f);
        sr.rect(cax - 2f, cay - 14f, 3f, 5f);
        sr.triangle(cax - 2f, cay - 14f, cax + 1f, cay - 14f, cax - 0.5f, cay - 16.5f);

        // 6: Event - Grand Festival Star Emblem with Ruby Core & Ribbons
        float evx = 1130f, evy = 676f;
        float evPulse = 0.85f + 0.15f * MathUtils.sin(timer * 5f);
        sr.setColor(1f, 0.3f, 0.3f, 0.30f * evPulse);
        sr.circle(evx, evy, 16f);

        // Celebratory Crimson Ribbons behind
        sr.setColor(0.85f, 0.18f, 0.22f, 1f);
        sr.triangle(evx - 3f, evy - 4f, evx - 10f, evy - 13f, evx - 1f, evy - 8f);
        sr.triangle(evx + 3f, evy - 4f, evx + 10f, evy - 13f, evx + 1f, evy - 8f);

        // 4 Diagonal Points of Star (Amber gold)
        sr.setColor(0.95f, 0.65f, 0.15f, 1f);
        sr.rect(evx - 7.5f, evy - 7.5f, 7.5f, 7.5f, 15f, 15f, 1f, 1f, 45f);

        // 4 Primary Points of Star (Brilliant Radiant Gold Cross)
        sr.setColor(1f, 0.88f, 0.25f, 1f);
        sr.triangle(evx - 3.5f, evy, evx + 3.5f, evy, evx, evy + 13f);
        sr.triangle(evx - 3.5f, evy, evx + 3.5f, evy, evx, evy - 13f);
        sr.triangle(evx, evy - 3.5f, evx, evy + 3.5f, evx + 13f, evy);
        sr.triangle(evx, evy - 3.5f, evx, evy + 3.5f, evx - 13f, evy);

        // Ruby Jewel Core
        sr.setColor(0.95f, 0.20f, 0.25f, 1f);
        sr.circle(evx, evy, 4f);
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.circle(evx - 1f, evy + 1f, 1.5f);
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

        // Level Badge Medallion (Left side)
        float shieldX = panelX + 38f;
        float shieldY = panelY + panelH / 2f;
        
        // Outer soft glow aura
        sr.setColor(1f, 0.85f, 0.3f, 0.25f);
        sr.circle(shieldX, shieldY, 32f);

        // Outer Gold Ring
        sr.setColor(0.95f, 0.75f, 0.2f, 1f);
        sr.circle(shieldX, shieldY, 28f);

        // Outer Inset Dark Ring
        sr.setColor(0.12f, 0.16f, 0.26f, 1f);
        sr.circle(shieldX, shieldY, 25f);

        // Inner Medallion Core
        sr.setColor(0.06f, 0.09f, 0.16f, 1f);
        sr.circle(shieldX, shieldY, 22f);

        // 4 Cardinal Star Pips on the rim
        sr.setColor(1f, 0.9f, 0.45f, 1f);
        sr.circle(shieldX, shieldY + 26f, 2.5f);
        sr.circle(shieldX, shieldY - 26f, 2.5f);
        sr.circle(shieldX + 26f, shieldY, 2.5f);
        sr.circle(shieldX - 26f, shieldY, 2.5f);

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

        // Active Bait Buff Banner if active (Expanded window to 340px)
        if (player.isBaitBuffActive()) {
            float buffY = panelY + panelH + 8f;
            float buffW = 340f;
            float buffH = 28f;
            sr.setColor(0.08f, 0.22f, 0.12f, 0.95f);
            sr.rect(panelX, buffY, buffW, buffH);
            sr.setColor(0.35f, 0.95f, 0.45f, 0.9f);
            drawBoxBorder(panelX, buffY, buffW, buffH, 1.4f);

            // Animated green status indicator
            float pulse = 0.6f + 0.4f * MathUtils.sin(timer * 5f);
            sr.setColor(0.2f, 1f, 0.4f, pulse);
            sr.circle(panelX + 14f, buffY + buffH / 2f, 4.5f);
            sr.setColor(1f, 1f, 1f, 0.8f);
            sr.circle(panelX + 14f, buffY + buffH / 2f, 2f);
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

        // News Ticker Marquee Text
        drawNewsTickerText(batch);

        // Player Dashboard text
        if (player != null) {
            float panelX = 20f;
            float panelY = 15f;
            float panelH = 88f;

            // Level Medallion Text (Left side) - Centered level number without "CẤP"
            float shieldX = panelX + 38f;
            float shieldY = panelY + panelH / 2f;

            String lvlNum = String.valueOf(player.getLevel());
            titleFont.getData().setScale(1.15f);
            layout.setText(titleFont, lvlNum);
            float numX = shieldX - layout.width / 2f;
            float numY = shieldY + layout.height / 2f;

            // Subtle gold drop shadow for depth
            titleFont.setColor(0.65f, 0.40f, 0.08f, 0.85f);
            titleFont.draw(batch, lvlNum, numX + 1.2f, numY - 1.2f);

            // Main bright centered level number
            titleFont.setColor(1f, 0.95f, 0.85f, 1f);
            titleFont.draw(batch, lvlNum, numX, numY);
            titleFont.getData().setScale(1f);
            font.getData().setScale(1f);

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

            // Bait Buff string inside widened 340px box
            if (player.isBaitBuffActive()) {
                float buffY = panelY + panelH + 8f;
                font.setColor(0.4f, 1f, 0.5f, 1f);
                font.getData().setScale(0.82f);
                font.draw(batch, "BUFF: MỒI THƠM (+TỐC ĐỘ CẮN)", panelX + 28f, buffY + 20f);
                font.getData().setScale(1f);
            }

            // Quick Slots Inventory Text
            drawInventoryText(batch, player);
        }
    }

    private void drawNewsTickerText(SpriteBatch batch) {
        // 1. Flush any text that was queued before the ticker (Menu, Cast button, Top 7 buttons)
        batch.flush();

        // 2. Draw Tag badge label
        font.setColor(1f, 0.88f, 0.35f, 1f);
        font.getData().setScale(0.80f);
        font.draw(batch, "✦ BẢN TIN", TICKER_X + 28f, TICKER_Y + 19f);
        font.getData().setScale(1f);
        batch.flush(); // Flush the tag text before enabling scissor!

        // 3. Update scrolling ticker position
        float dt = Gdx.graphics.getDeltaTime();
        tickerOffset -= TICKER_SPEED * dt;
        String currentMsg = TICKER_MESSAGES[tickerMsgIndex];
        font.getData().setScale(0.82f);
        layout.setText(font, currentMsg);

        float clipX = TICKER_X + 130f;
        float clipW = TICKER_W - 135f;
        float clipY = TICKER_Y;
        float clipH = TICKER_H;

        if (tickerOffset < -layout.width) {
            tickerOffset = clipW + 15f;
            tickerMsgIndex = (tickerMsgIndex + 1 + MathUtils.random(1, TICKER_MESSAGES.length - 2)) % TICKER_MESSAGES.length;
            currentMsg = TICKER_MESSAGES[tickerMsgIndex];
            layout.setText(font, currentMsg);
        }

        // 4. Scissor clipping for smooth marquee effect
        tickerClipBounds.set(clipX, clipY, clipW, clipH);
        ScissorStack.calculateScissors(screen.getCamera(), screen.getViewport().getScreenX(), screen.getViewport().getScreenY(),
            screen.getViewport().getScreenWidth(), screen.getViewport().getScreenHeight(),
            batch.getTransformMatrix(), tickerClipBounds, tickerScissors);

        if (ScissorStack.pushScissors(tickerScissors)) {
            font.setColor(0.94f, 0.96f, 1.0f, 0.95f);
            font.draw(batch, currentMsg, clipX + tickerOffset, TICKER_Y + 19f);
            batch.flush(); // Flush ticker text WHILE scissor is active
            ScissorStack.popScissors();
        }
        font.getData().setScale(1f);
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

            // Formatted item name with clean scaling and centering
            font.setColor(Color.WHITE);
            font.getData().setScale(0.68f);
            layout.setText(font, name);
            if (layout.width > slotSize - 6f) {
                font.getData().setScale(0.68f * ((slotSize - 6f) / layout.width));
                layout.setText(font, name);
            }
            font.draw(batch, name, x + (slotSize - layout.width) / 2f, y + 54f);
            font.getData().setScale(1f);

            // Item quantity
            font.setColor(1f, 0.9f, 0.2f, 1f);
            font.getData().setScale(0.75f);
            font.draw(batch, "x" + entry.getValue(), x + 4f, y + 18f);

            // Hotkey [1-9]
            font.setColor(0.4f, 0.85f, 1f, 1f);
            font.draw(batch, "[" + (i + 1) + "]", x + slotSize - 22f, y + 18f);
            font.getData().setScale(1f);
            i++;
        }

        // Empty state indicator
        if (i == 0) {
            font.setColor(0.55f, 0.65f, 0.8f, 0.7f);
            font.getData().setScale(0.78f);
            drawCenteredText(font, "Túi rỗng\n(Mua ở Cửa Hàng)", invX, invY + 80f, panelW, 40f);
            font.getData().setScale(1f);
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
