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
import com.badlogic.gdx.utils.Align;
import com.fishingrpg.game.PlayScreen;
import com.fishingrpg.game.entities.Fish;
import com.fishingrpg.game.entities.MapArea;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.loot.Catchable;
import com.fishingrpg.game.loot.CatchableType;
import com.fishingrpg.game.loot.LootItem;
import com.fishingrpg.game.screens.SkillTreeScreen;
import com.fishingrpg.game.systems.AudioManager;
import com.fishingrpg.game.systems.FishingManager;
import com.fishingrpg.game.systems.FishingManager.State;

import java.util.List;
import java.util.ArrayList;
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
    private static final float PANEL_W = 44f, PANEL_H = 300f;
    private static final float BAR_INSET = 4f;

    // Quick Slots Action Bar Layout
    private static final float INV_SLOT_SIZE = 68f;
    private static final float INV_PADDING = 8f;
    private static final float INV_PANEL_W = 3 * INV_SLOT_SIZE + 4 * INV_PADDING; // 236f
    private static final float INV_PANEL_H = 3 * INV_SLOT_SIZE + 4 * INV_PADDING + 28f; // 264f
    private static final float INV_X = W - INV_PANEL_W - 16f;
    private static final float INV_Y = 16f;

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

    // Information / News Button (Square button with 'i' logo in top-right corner of Game HUD)
    public static final float BTN_INFO_SIZE = 40f;
    public static final float BTN_INFO_X = W - 20f - BTN_INFO_SIZE; // 1220f
    public static final float BTN_INFO_Y = H - 60f; // 660f

    // Settings Button (Square button with cog/gear logo directly below Info button)
    public static final float BTN_SETTING_SIZE = 40f;
    public static final float BTN_SETTING_X = BTN_INFO_X; // 1220f
    public static final float BTN_SETTING_Y = BTN_INFO_Y - BTN_SETTING_SIZE - 10f; // 610f

    // Top Navigation Buttons (7 buttons)
    private static final float TOP_BTN_W = 120f, TOP_BTN_H = 65f;
    private static final float TOP_BTN_Y = H - 85f;

    // Combat Skill Slots (Q, W, E, R)
    private static final float BTN_SLOT_SIZE = 70f;
    private static final float BTN_SLOT_SPACING = 16f;
    private static final float BTN_ACTIVES_TOTAL_W = 4 * BTN_SLOT_SIZE + 3 * BTN_SLOT_SPACING; // 328f
    private static final float BTN_ACTIVES_X = (W - BTN_ACTIVES_TOTAL_W) / 2f; // 476f
    private static final float BTN_ACTIVES_Y = 22f;
    private static final float BTN_CANG_X = -999f;
    private static final float BTN_CANG_Y = -999f;

    // Hover States
    private boolean hoverCast = false;
    private boolean hoverMenu = false;
    private boolean hoverNews = false;
    private boolean hoverSetting = false;
    private boolean hoverQuest = false;
    private boolean hoverEquipment = false;
    private boolean hoverSkills = false;
    private boolean hoverGacha = false;
    private boolean hoverShop = false;
    private boolean hoverCatalog = false;
    private boolean hoverEvent = false;
    private boolean hoverCang = false;
    private boolean hoverClose = false;
    private boolean hoverCatchClose = false;
    private boolean hoverBuff = false;
    private final boolean[] hoverActives = new boolean[4];

    private float timer = 0f;
    private float fishTrailingHp = -1f;
    private String hoveredInventoryItem = null;

    // Quick Slots Pagination
    public static final int INV_PAGE_SIZE = 9;
    public int quickSlotPage = 0;
    private boolean hoverQuickslotPrev = false;
    private boolean hoverQuickslotNext = false;
    private final List<Map.Entry<String, Integer>> cachedValidInventory = new ArrayList<>();
    private final List<String> cachedPageItems = new ArrayList<>();

    // News Ticker / Marquee Banner
    private static final String[] TICKER_MESSAGES = {
        "Tin vực biển: Xuất hiện luồng cá Bạo Chúa ngoài khơi Đảo Hoàng Hôn vào ban đêm!",
        "Mẹo hay: Sử dụng [Mồi Thơm] giúp cá cắn câu nhanh gấp 3 lần và tiết kiệm thời gian!",
        "Lời khuyên: Canh chuẩn thanh giằng co kéo cá để không bị đứt dây câu quý giá.",
        "Kỷ lục: Ngư dân kỳ cựu vừa trục vớt thành công [Rương Báu Hoàng Gia] chứa 500 Kim Cương!",
        "Thiên phú: Nhánh [Sức Mạnh] giúp rút ngắn thời gian giằng co với các loài thủy quái hung dữ.",
        "May mắn: Nâng cấp [Ngư Dân Tinh Anh] sẽ tăng mạnh cơ hội bắt được cá Huyền Thoại!",
        "Bí kíp: Thu thập đủ [Sách Kỹ Năng] để khai mở các bí thuật thượng cổ của dòng họ Ngư Phủ.",
        "Thời tiết: Biển êm gió lặng, cực kỳ thích hợp để ra khơi săn cá Rồng và Cá Voi Xanh!",
        "Sự kiện: Điểm danh hàng ngày và hoàn thành nhiệm vụ tuần để nhận Rương Ngọc Quý!",
        "Cần câu: Cần cấp cao không chỉ tăng sát thương mà còn tăng độ bền bỉ của dây câu.",
        "Kỹ năng: Chiêu thức [Trói Buộc] có thể đóng băng cá tức thời khi thanh Tension sắp đạt đỉnh!",
        "Hồi phục: Khi cạn thể lực, hãy dùng [Bánh Mì] hoặc [Thuốc Hồi Stamina] tại thanh Phím Tắt."
    };

    private static final float TICKER_X = 215f;
    private static final float TICKER_Y = 595f;
    private static final float TICKER_W = 990f;
    private static final float TICKER_H = 28f;
    private static final float TICKER_SPEED = 70f;
    private float tickerOffset = TICKER_W - 150f;
    private int tickerMsgIndex = MathUtils.random(0, TICKER_MESSAGES.length - 1);
    private final Rectangle tickerClipBounds = new Rectangle();
    private final Rectangle tickerScissors = new Rectangle();

    public GameHUD(ShapeRenderer sr, BitmapFont font, PlayScreen screen) {
        this.sr = sr;
        this.font = font;
        this.layout = new GlyphLayout();
        this.screen = screen;
        this.titleFont = (screen.getGame() != null && screen.getGame().titleFont != null) ? screen.getGame().titleFont : font;
        this.tickerMsgIndex = MathUtils.random(0, TICKER_MESSAGES.length - 1);
    }

    public void updateHover(Vector3 m, State state) {
        hoverCast = (state == State.IDLE || state == State.PRE_CAST || state == State.RESULT) &&
                    m.x >= BTN_CAST_X && m.x <= BTN_CAST_X + BTN_CAST_W &&
                    m.y >= BTN_CAST_Y && m.y <= BTN_CAST_Y + BTN_CAST_H;

        hoverMenu = m.x >= BTN_MENU_X && m.x <= BTN_MENU_X + BTN_MENU_W &&
                    m.y >= BTN_MENU_Y && m.y <= BTN_MENU_Y + BTN_MENU_H;

        hoverNews = (state == State.IDLE) &&
                    m.x >= BTN_INFO_X && m.x <= BTN_INFO_X + BTN_INFO_SIZE &&
                    m.y >= BTN_INFO_Y && m.y <= BTN_INFO_Y + BTN_INFO_SIZE;

        hoverSetting = (state == State.IDLE) &&
                       m.x >= BTN_SETTING_X && m.x <= BTN_SETTING_X + BTN_SETTING_SIZE &&
                       m.y >= BTN_SETTING_Y && m.y <= BTN_SETTING_Y + BTN_SETTING_SIZE;

        hoverQuest     = state == State.IDLE && m.x >= 230 && m.x <= 350 && m.y >= TOP_BTN_Y && m.y <= TOP_BTN_Y + TOP_BTN_H;
        hoverEquipment = state == State.IDLE && m.x >= 370 && m.x <= 490 && m.y >= TOP_BTN_Y && m.y <= TOP_BTN_Y + TOP_BTN_H;
        hoverSkills    = state == State.IDLE && m.x >= 510 && m.x <= 630 && m.y >= TOP_BTN_Y && m.y <= TOP_BTN_Y + TOP_BTN_H;
        hoverGacha     = state == State.IDLE && m.x >= 650 && m.x <= 770 && m.y >= TOP_BTN_Y && m.y <= TOP_BTN_Y + TOP_BTN_H;
        hoverShop      = state == State.IDLE && m.x >= 790 && m.x <= 910 && m.y >= TOP_BTN_Y && m.y <= TOP_BTN_Y + TOP_BTN_H;
        hoverCatalog   = state == State.IDLE && m.x >= 930 && m.x <= 1050 && m.y >= TOP_BTN_Y && m.y <= TOP_BTN_Y + TOP_BTN_H;
        hoverEvent     = state == State.IDLE && m.x >= 1070 && m.x <= 1190 && m.y >= TOP_BTN_Y && m.y <= TOP_BTN_Y + TOP_BTN_H;

        for (int i = 0; i < 4; i++) {
            float sx = BTN_ACTIVES_X + i * (BTN_SLOT_SIZE + BTN_SLOT_SPACING);
            hoverActives[i] = state == State.COMBAT &&
                              m.x >= sx && m.x <= sx + BTN_SLOT_SIZE &&
                              m.y >= BTN_ACTIVES_Y && m.y <= BTN_ACTIVES_Y + BTN_SLOT_SIZE;
        }

        hoverCang = false;

        float buffBtnX = 438f;
        float buffBtnY = H - 54f;
        float buffBtnW = 105f;
        float buffBtnH = 36f;
        hoverBuff = m.x >= buffBtnX && m.x <= buffBtnX + buffBtnW &&
                    m.y >= buffBtnY && m.y <= buffBtnY + buffBtnH;

        // Close button on catch sidebar
        float sbX = INV_X;
        float sbY = INV_Y + INV_PANEL_H + 10f;
        float sbW = INV_PANEL_W;
        float sbH = 325f;
        hoverCatchClose = screen.fishingManager.showCatchSidebar &&
                          m.x >= sbX + sbW - 24f && m.x <= sbX + sbW - 4f &&
                          m.y >= sbY + sbH - 24f && m.y <= sbY + sbH - 4f;
        hoverClose = hoverCatchClose;

        // Quick Slots pagination buttons hover
        Player player = screen.getPlayer();
        int totalPages = getTotalQuickslotPages(player);
        if (quickSlotPage >= totalPages) quickSlotPage = totalPages - 1;
        if (quickSlotPage < 0) quickSlotPage = 0;

        float prevBtnX = INV_X + 6f;
        float prevBtnY = INV_Y + INV_PANEL_H - 24f;
        float btnW = 22f;
        float btnH = 20f;
        float nextBtnX = INV_X + INV_PANEL_W - 28f;
        float nextBtnY = INV_Y + INV_PANEL_H - 24f;

        hoverQuickslotPrev = (totalPages > 1 && m.x >= prevBtnX && m.x <= prevBtnX + btnW && m.y >= prevBtnY && m.y <= prevBtnY + btnH);
        hoverQuickslotNext = (totalPages > 1 && m.x >= nextBtnX && m.x <= nextBtnX + btnW && m.y >= nextBtnY && m.y <= nextBtnY + btnH);

        hoveredInventoryItem = getClickedInventoryItem(m, state);
    }

    public boolean isCastHovered() { return hoverCast; }
    public boolean isCloseHovered() { return hoverClose; }
    public boolean isCatchCloseHovered() { return hoverCatchClose; }
    public boolean isActiveSlotHovered(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < 4) return hoverActives[slotIndex];
        return false;
    }
    public boolean isCangHovered() { return false; }
    public boolean isActionClicked(Vector3 m, State state) {
        if (state == State.IDLE) return hoverCast;
        return hoverCast;
    }
    public boolean isMenuClicked(Vector3 m, State state) { return hoverMenu; }
    public boolean isNewsClicked(Vector3 m, State state) { return state == State.IDLE && hoverNews; }
    public boolean isSettingClicked(Vector3 m, State state) { return state == State.IDLE && hoverSetting; }
    public boolean isSettingHovered() { return hoverSetting; }
    public boolean isShopClicked(Vector3 m, State state) { return hoverShop; }
    public boolean isCatalogClicked(Vector3 m, State state) { return hoverCatalog; }
    public boolean isSkillsClicked(Vector3 m, State state) { return hoverSkills; }
    public boolean isQuestClicked(Vector3 m, State state) { return hoverQuest; }
    public boolean isGachaClicked(Vector3 m, State state) { return hoverGacha; }
    public boolean isEquipmentClicked(Vector3 m, State state) { return hoverEquipment; }
    public boolean isEventClicked(Vector3 m, State state) { return hoverEvent; }
    public boolean isQuickslotPrevClicked(Vector3 m) { return hoverQuickslotPrev; }
    public boolean isQuickslotNextClicked(Vector3 m) { return hoverQuickslotNext; }

    public void prevQuickslotPage() {
        Player player = screen.getPlayer();
        if (player == null) return;
        int totalPages = getTotalQuickslotPages(player);
        if (totalPages <= 1) return;
        quickSlotPage = (quickSlotPage - 1 + totalPages) % totalPages;
        AudioManager.getInstance().playSFX("sfx_button");
    }

    public void nextQuickslotPage() {
        Player player = screen.getPlayer();
        if (player == null) return;
        int totalPages = getTotalQuickslotPages(player);
        if (totalPages <= 1) return;
        quickSlotPage = (quickSlotPage + 1) % totalPages;
        AudioManager.getInstance().playSFX("sfx_button");
    }

    public List<Map.Entry<String, Integer>> getValidInventoryEntries(Player player) {
        cachedValidInventory.clear();
        if (player == null) return cachedValidInventory;
        for (Map.Entry<String, Integer> entry : player.getInventory().entrySet()) {
            if (entry.getValue() > 0 && !entry.getKey().equals("Vàng")) {
                cachedValidInventory.add(entry);
            }
        }
        return cachedValidInventory;
    }

    public int getTotalQuickslotPages(Player player) {
        if (player == null) return 1;
        int count = 0;
        for (Map.Entry<String, Integer> entry : player.getInventory().entrySet()) {
            if (entry.getValue() > 0 && !entry.getKey().equals("Vàng")) {
                count++;
            }
        }
        return Math.max(1, (count + INV_PAGE_SIZE - 1) / INV_PAGE_SIZE);
    }

    public List<String> getItemsOnCurrentPage() {
        cachedPageItems.clear();
        Player player = screen.getPlayer();
        if (player == null) return cachedPageItems;
        List<Map.Entry<String, Integer>> validEntries = getValidInventoryEntries(player);
        int totalPages = Math.max(1, (validEntries.size() + INV_PAGE_SIZE - 1) / INV_PAGE_SIZE);
        if (quickSlotPage >= totalPages) quickSlotPage = totalPages - 1;
        if (quickSlotPage < 0) quickSlotPage = 0;

        int startIndex = quickSlotPage * INV_PAGE_SIZE;
        int endIndex = Math.min(validEntries.size(), startIndex + INV_PAGE_SIZE);
        for (int i = startIndex; i < endIndex; i++) {
            cachedPageItems.add(validEntries.get(i).getKey());
        }
        return cachedPageItems;
    }

    public String getClickedInventoryItem(Vector3 m, State state) {
        Player player = screen.getPlayer();
        if (player == null) return null;

        List<Map.Entry<String, Integer>> validEntries = getValidInventoryEntries(player);
        int totalPages = Math.max(1, (validEntries.size() + INV_PAGE_SIZE - 1) / INV_PAGE_SIZE);
        if (quickSlotPage >= totalPages) quickSlotPage = totalPages - 1;
        if (quickSlotPage < 0) quickSlotPage = 0;

        int startIndex = quickSlotPage * INV_PAGE_SIZE;
        int endIndex = Math.min(validEntries.size(), startIndex + INV_PAGE_SIZE);

        for (int i = startIndex; i < endIndex; i++) {
            int slotIndex = i - startIndex;
            int row = slotIndex / 3;
            int col = slotIndex % 3;
            float x = INV_X + INV_PADDING + col * (INV_SLOT_SIZE + INV_PADDING);
            float y = INV_Y + INV_PANEL_H - 28f - INV_PADDING - (row + 1) * INV_SLOT_SIZE - row * INV_PADDING;

            if (m.x >= x && m.x <= x + INV_SLOT_SIZE && m.y >= y && m.y <= y + INV_SLOT_SIZE) {
                return validEntries.get(i).getKey();
            }
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

        // 5. Back to Menu Button, News Button & Settings Button (Only in IDLE HUD)
        drawMenuBtn();
        drawIdleNewsBtn();
        drawIdleSettingBtn();

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
        float pulse = (screen.fishingManager.state == State.IDLE || screen.fishingManager.state == State.PRE_CAST)
                ? (0.92f + 0.08f * MathUtils.sin(timer * 5f)) : 1f;

        // 1. Ambient outer glow pulse aura
        sr.setColor(1f, 0.55f, 0.15f, (hoverCast ? 0.35f : 0.18f) * pulse);
        sr.rect(BTN_CAST_X - 6f, BTN_CAST_Y - 6f, BTN_CAST_W + 12f, BTN_CAST_H + 12f);

        // 2. Base metallic backing chassis (Dark obsidian bronze)
        sr.setColor(0.12f, 0.08f, 0.06f, 0.98f);
        sr.rect(BTN_CAST_X, BTN_CAST_Y, BTN_CAST_W, BTN_CAST_H);

        // 3. Inner dual-tier gradient fill (Fiery ember amber)
        Color btmColor = hoverCast ? new Color(0.92f, 0.40f, 0.10f, 1f) : new Color(0.78f, 0.32f, 0.08f, 0.96f);
        Color topColor = hoverCast ? new Color(1.00f, 0.62f, 0.18f, 1f) : new Color(0.90f, 0.48f, 0.14f, 0.96f);
        sr.setColor(btmColor);
        sr.rect(BTN_CAST_X + 3f, BTN_CAST_Y + 3f, BTN_CAST_W - 6f, BTN_CAST_H / 2f - 3f);
        sr.setColor(topColor);
        sr.rect(BTN_CAST_X + 3f, BTN_CAST_Y + BTN_CAST_H / 2f, BTN_CAST_W - 6f, BTN_CAST_H / 2f - 3f);

        // 4. Glossy highlight specular ribbon along the top edge
        sr.setColor(1f, 0.92f, 0.45f, hoverCast ? 0.65f : 0.45f);
        sr.rect(BTN_CAST_X + 4f, BTN_CAST_Y + BTN_CAST_H - 7f, BTN_CAST_W - 8f, 4f);

        // 5. Stylized Fishing Bobber Emblem - Balanced dynamically with label
        font.getData().setScale(1f);
        layout.setText(font, label);
        float totalGroupW = 20f + 12f + layout.width;
        float groupStartX = BTN_CAST_X + (BTN_CAST_W - totalGroupW) / 2f;
        float bx = groupStartX + 10f;
        float by = BTN_CAST_Y + BTN_CAST_H / 2f;

        // Ripple ellipse
        sr.setColor(0.25f, 0.85f, 1f, 0.4f * pulse);
        sr.ellipse(bx - 12f, by - 10f, 24f, 8f);
        // Bobber antenna rod
        sr.setColor(1f, 0.92f, 0.3f, 1f);
        sr.rect(bx - 1f, by + 3f, 2f, 8f);
        sr.setColor(1f, 0.25f, 0.25f, 1f);
        sr.circle(bx, by + 11f, 2f);
        // Lower white hemisphere
        sr.setColor(0.92f, 0.94f, 0.98f, 1f);
        sr.circle(bx, by - 1f, 7.5f);
        // Upper red hemisphere
        sr.setColor(0.95f, 0.22f, 0.22f, 1f);
        sr.arc(bx, by - 1f, 7.5f, 0f, 180f);
        // Gold equator ring
        sr.setColor(1f, 0.85f, 0.25f, 1f);
        sr.rect(bx - 7.5f, by - 2f, 15f, 2.5f);

        // 6. Multi-tier Gold Bevel Border & Corner Accents
        sr.setColor(1f, 0.85f, 0.35f, hoverCast ? 1f : 0.85f);
        drawBoxBorder(BTN_CAST_X, BTN_CAST_Y, BTN_CAST_W, BTN_CAST_H, 2.2f);
        // Corner gold brackets
        sr.setColor(1f, 0.95f, 0.55f, 1f);
        sr.rect(BTN_CAST_X, BTN_CAST_Y + BTN_CAST_H - 6f, 6f, 6f);
        sr.rect(BTN_CAST_X + BTN_CAST_W - 6f, BTN_CAST_Y + BTN_CAST_H - 6f, 6f, 6f);
        sr.rect(BTN_CAST_X, BTN_CAST_Y, 6f, 6f);
        sr.rect(BTN_CAST_X + BTN_CAST_W - 6f, BTN_CAST_Y, 6f, 6f);
    }

    private void drawMenuBtn() {
        // Slate-navy adventure chassis with cyan/gold accents
        sr.setColor(hoverMenu ? new Color(0.18f, 0.26f, 0.38f, 0.98f) : new Color(0.10f, 0.14f, 0.22f, 0.92f));
        sr.rect(BTN_MENU_X, BTN_MENU_Y, BTN_MENU_W, BTN_MENU_H);

        // Top glossy highlight stripe
        sr.setColor(1f, 1f, 1f, hoverMenu ? 0.32f : 0.16f);
        sr.rect(BTN_MENU_X + 2f, BTN_MENU_Y + BTN_MENU_H - 3f, BTN_MENU_W - 4f, 2f);

        // Border
        sr.setColor(hoverMenu ? new Color(0.45f, 0.85f, 1f, 1f) : new Color(0.35f, 0.55f, 0.75f, 0.8f));
        drawBoxBorder(BTN_MENU_X, BTN_MENU_Y, BTN_MENU_W, BTN_MENU_H, 1.5f);

        // Vector Back Arrow (←) on the left of button
        float arrX = BTN_MENU_X + 16f;
        float arrY = BTN_MENU_Y + BTN_MENU_H / 2f;
        sr.setColor(hoverMenu ? new Color(1f, 0.92f, 0.4f, 1f) : new Color(0.75f, 0.9f, 1f, 0.9f));
        sr.rectLine(arrX - 6f, arrY, arrX + 6f, arrY, 2f);
        sr.rectLine(arrX - 6f, arrY, arrX - 1f, arrY + 5f, 2f);
        sr.rectLine(arrX - 6f, arrY, arrX - 1f, arrY - 5f, 2f);
    }

    private void drawIdleNewsBtn() {
        // Square Information Button [ⓘ] in the top-right corner of Game HUD
        sr.setColor(hoverNews ? new Color(0.18f, 0.26f, 0.38f, 0.98f) : new Color(0.10f, 0.14f, 0.22f, 0.92f));
        sr.rect(BTN_INFO_X, BTN_INFO_Y, BTN_INFO_SIZE, BTN_INFO_SIZE);

        // Glossy top highlight stripe
        sr.setColor(1f, 1f, 1f, hoverNews ? 0.32f : 0.15f);
        sr.rect(BTN_INFO_X + 2f, BTN_INFO_Y + BTN_INFO_SIZE - 3f, BTN_INFO_SIZE - 4f, 2f);

        // Outer chassis border
        sr.setColor(hoverNews ? new Color(0.45f, 0.85f, 1f, 1f) : new Color(0.35f, 0.55f, 0.75f, 0.8f));
        drawBoxBorder(BTN_INFO_X, BTN_INFO_Y, BTN_INFO_SIZE, BTN_INFO_SIZE, 1.5f);

        float cx = BTN_INFO_X + BTN_INFO_SIZE / 2f;
        float cy = BTN_INFO_Y + BTN_INFO_SIZE / 2f;

        // Circular ring around 'i'
        sr.setColor(hoverNews ? new Color(0.55f, 0.95f, 1f, 0.9f) : new Color(0.40f, 0.75f, 0.95f, 0.7f));
        int segments = 24;
        float r = 13.5f;
        for (int s = 0; s < segments; s++) {
            float a1 = (float)(s * 2 * Math.PI / segments);
            float a2 = (float)((s + 1) * 2 * Math.PI / segments);
            sr.rectLine(cx + r * MathUtils.cos(a1), cy + r * MathUtils.sin(a1),
                        cx + r * MathUtils.cos(a2), cy + r * MathUtils.sin(a2), 1.5f);
        }

        // 'i' glyph: Dot on top
        sr.setColor(hoverNews ? new Color(1f, 0.92f, 0.45f, 1f) : new Color(0.85f, 0.95f, 1f, 0.95f));
        sr.circle(cx, cy + 6f, 2.2f);

        // 'i' glyph: Vertical stem with serif base and hook
        sr.rectLine(cx, cy - 6.5f, cx, cy + 1.5f, 2.6f);
        sr.rectLine(cx - 3f, cy - 6.5f, cx + 3f, cy - 6.5f, 2f);
        sr.rectLine(cx - 2.5f, cy + 0.2f, cx, cy + 1.5f, 1.8f);

        // Radiant notification dot (ONLY when there is unread update notes!)
        if (screen != null && screen.isNewsUnread()) {
            float dotX = BTN_INFO_X + BTN_INFO_SIZE - 4f;
            float dotY = BTN_INFO_Y + BTN_INFO_SIZE - 4f;
            float dotPulse = 0.5f + 0.5f * MathUtils.sin(timer * 6f);
            sr.setColor(1f, 0.25f, 0.25f, 0.8f + 0.2f * dotPulse);
            sr.circle(dotX, dotY, 4f);
            sr.setColor(1f, 0.85f, 0.3f, 0.9f);
            sr.circle(dotX, dotY, 1.8f);
        }
    }

    private void drawIdleSettingBtn() {
        // Square Settings Button [⚙] directly below Information Button in Game HUD
        sr.setColor(hoverSetting ? new Color(0.18f, 0.26f, 0.38f, 0.98f) : new Color(0.10f, 0.14f, 0.22f, 0.92f));
        sr.rect(BTN_SETTING_X, BTN_SETTING_Y, BTN_SETTING_SIZE, BTN_SETTING_SIZE);

        // Glossy top highlight stripe
        sr.setColor(1f, 1f, 1f, hoverSetting ? 0.32f : 0.15f);
        sr.rect(BTN_SETTING_X + 2f, BTN_SETTING_Y + BTN_SETTING_SIZE - 3f, BTN_SETTING_SIZE - 4f, 2f);

        // Outer chassis border
        sr.setColor(hoverSetting ? new Color(0.45f, 0.85f, 1f, 1f) : new Color(0.35f, 0.55f, 0.75f, 0.8f));
        drawBoxBorder(BTN_SETTING_X, BTN_SETTING_Y, BTN_SETTING_SIZE, BTN_SETTING_SIZE, 1.5f);

        float cx = BTN_SETTING_X + BTN_SETTING_SIZE / 2f;
        float cy = BTN_SETTING_Y + BTN_SETTING_SIZE / 2f;

        // Gear Teeth (8 spokes radiating outwards)
        sr.setColor(hoverSetting ? new Color(1f, 0.92f, 0.45f, 1f) : new Color(0.85f, 0.95f, 1f, 0.95f));
        float baseAngle = hoverSetting ? (timer * 2.5f) : 0f;
        int spokes = 8;
        float spokeLen = 13.5f;
        float hubRadius = 6.5f;

        for (int i = 0; i < spokes; i++) {
            float angle = baseAngle + (float)(i * Math.PI / 4.0);
            float cosA = MathUtils.cos(angle);
            float sinA = MathUtils.sin(angle);
            sr.rectLine(cx + hubRadius * cosA, cy + hubRadius * sinA,
                        cx + spokeLen * cosA, cy + spokeLen * sinA, 3.2f);
        }

        // Circular rim connecting spokes
        int rimSegments = 24;
        for (int s = 0; s < rimSegments; s++) {
            float a1 = (float)(s * 2 * Math.PI / rimSegments);
            float a2 = (float)((s + 1) * 2 * Math.PI / rimSegments);
            sr.rectLine(cx + 8.5f * MathUtils.cos(a1), cy + 8.5f * MathUtils.sin(a1),
                        cx + 8.5f * MathUtils.cos(a2), cy + 8.5f * MathUtils.sin(a2), 2.2f);
        }

        // Hollow center axle (drawn in background color to create inner circle)
        sr.setColor(hoverSetting ? new Color(0.18f, 0.26f, 0.38f, 0.98f) : new Color(0.10f, 0.14f, 0.22f, 0.92f));
        sr.circle(cx, cy, 3.8f);

        // Inner axle pin
        sr.setColor(hoverSetting ? new Color(1f, 0.92f, 0.45f, 1f) : new Color(0.85f, 0.95f, 1f, 0.95f));
        sr.circle(cx, cy, 1.6f);
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
        // Diamond Icon (Universal glowing diamond)
        EquipmentIconRenderer.drawDiamondIcon(sr, goldX + 16f, diaY + resH / 2f, 8f);
    }

    public void drawIdleText(SpriteBatch batch, Player player) {
        // Primary Action Button text
        String castLabel = "RA KHƠI CÂU CÁ";
        font.getData().setScale(1f);
        layout.setText(font, castLabel);
        float totalGroupW = 20f + 12f + layout.width;
        float groupStartX = BTN_CAST_X + (BTN_CAST_W - totalGroupW) / 2f;
        float textX = groupStartX + 20f + 12f;
        float textY = BTN_CAST_Y + (BTN_CAST_H + layout.height) / 2f;

        font.setColor(0.12f, 0.05f, 0.02f, 0.95f);
        font.draw(batch, castLabel, textX + 1.2f, textY - 1.2f);
        font.setColor(hoverCast ? new Color(1f, 0.98f, 0.6f, 1f) : Color.WHITE);
        font.draw(batch, castLabel, textX, textY);

        // Menu Back Button -> MENU
        String mapLabel = "MENU";
        layout.setText(font, mapLabel);
        float mbx = BTN_MENU_X + 16f + (BTN_MENU_W - 16f - layout.width) / 2f;
        float mby = BTN_MENU_Y + (BTN_MENU_H + layout.height) / 2f;
        font.setColor(0f, 0f, 0f, 0.85f);
        font.draw(batch, mapLabel, mbx + 1f, mby - 1f);
        font.setColor(hoverMenu ? new Color(1f, 0.92f, 0.4f, 1f) : Color.WHITE);
        font.draw(batch, mapLabel, mbx, mby);

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
            int nextIdx = tickerMsgIndex;
            if (TICKER_MESSAGES.length > 1) {
                while (nextIdx == tickerMsgIndex) {
                    nextIdx = MathUtils.random(0, TICKER_MESSAGES.length - 1);
                }
            }
            tickerMsgIndex = nextIdx;
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
        if (mapId.equals("bai_bien")) return "Bãi Biển";
        if (mapId.equals("dao_xa")) return "Đảo Xa";
        return mapId;
    }

    // =========================================================================
    // QUICK SLOTS INVENTORY (BOTTOM RIGHT)
    // =========================================================================

    public void drawInventoryShapes(Player player) {
        if (player == null) return;

        // Panel background (Dark obsidian glass)
        sr.setColor(0.08f, 0.11f, 0.17f, 0.94f);
        sr.rect(INV_X, INV_Y, INV_PANEL_W, INV_PANEL_H);
        sr.setColor(0.75f, 0.55f, 0.22f, 0.75f);
        drawBoxBorder(INV_X, INV_Y, INV_PANEL_W, INV_PANEL_H, 1.6f);

        // Header bar
        sr.setColor(0.14f, 0.18f, 0.28f, 0.95f);
        sr.rect(INV_X, INV_Y + INV_PANEL_H - 28f, INV_PANEL_W, 28f);
        sr.setColor(0.85f, 0.65f, 0.25f, 0.6f);
        sr.rect(INV_X, INV_Y + INV_PANEL_H - 28f, INV_PANEL_W, 1.5f);

        List<Map.Entry<String, Integer>> validEntries = getValidInventoryEntries(player);
        int totalPages = Math.max(1, (validEntries.size() + INV_PAGE_SIZE - 1) / INV_PAGE_SIZE);
        if (quickSlotPage >= totalPages) quickSlotPage = totalPages - 1;
        if (quickSlotPage < 0) quickSlotPage = 0;

        // Draw header pagination buttons if more than 1 page
        if (totalPages > 1) {
            float prevBtnX = INV_X + 6f;
            float prevBtnY = INV_Y + INV_PANEL_H - 24f;
            float btnW = 22f;
            float btnH = 20f;
            float nextBtnX = INV_X + INV_PANEL_W - 28f;
            float nextBtnY = INV_Y + INV_PANEL_H - 24f;

            sr.setColor(hoverQuickslotPrev ? new Color(0.28f, 0.40f, 0.60f, 0.95f) : new Color(0.18f, 0.22f, 0.32f, 0.90f));
            sr.rect(prevBtnX, prevBtnY, btnW, btnH);
            sr.setColor(hoverQuickslotPrev ? new Color(1f, 0.88f, 0.35f, 1f) : new Color(0.40f, 0.55f, 0.75f, 0.75f));
            drawBoxBorder(prevBtnX, prevBtnY, btnW, btnH, 1.2f);

            sr.setColor(hoverQuickslotNext ? new Color(0.28f, 0.40f, 0.60f, 0.95f) : new Color(0.18f, 0.22f, 0.32f, 0.90f));
            sr.rect(nextBtnX, nextBtnY, btnW, btnH);
            sr.setColor(hoverQuickslotNext ? new Color(1f, 0.88f, 0.35f, 1f) : new Color(0.40f, 0.55f, 0.75f, 0.75f));
            drawBoxBorder(nextBtnX, nextBtnY, btnW, btnH, 1.2f);
        }

        // Draw empty slots (always 3x3)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                float x = INV_X + INV_PADDING + col * (INV_SLOT_SIZE + INV_PADDING);
                float y = INV_Y + INV_PANEL_H - 28f - INV_PADDING - (row + 1) * INV_SLOT_SIZE - row * INV_PADDING;
                sr.setColor(0.11f, 0.14f, 0.20f, 0.9f);
                sr.rect(x, y, INV_SLOT_SIZE, INV_SLOT_SIZE);
                sr.setColor(0.22f, 0.28f, 0.38f, 0.6f);
                drawBoxBorder(x, y, INV_SLOT_SIZE, INV_SLOT_SIZE, 1.2f);
            }
        }

        // Draw filled item slots for CURRENT PAGE ONLY
        int startIndex = quickSlotPage * INV_PAGE_SIZE;
        int endIndex = Math.min(validEntries.size(), startIndex + INV_PAGE_SIZE);

        for (int i = startIndex; i < endIndex; i++) {
            Map.Entry<String, Integer> entry = validEntries.get(i);
            String name = entry.getKey();
            int slotIndex = i - startIndex;
            int row = slotIndex / 3;
            int col = slotIndex % 3;
            float x = INV_X + INV_PADDING + col * (INV_SLOT_SIZE + INV_PADDING);
            float y = INV_Y + INV_PANEL_H - 28f - INV_PADDING - (row + 1) * INV_SLOT_SIZE - row * INV_PADDING;

            boolean isHovered = (hoveredInventoryItem != null && hoveredInventoryItem.equals(name));
            sr.setColor(isHovered ? new Color(0.22f, 0.32f, 0.48f, 0.95f) : new Color(0.14f, 0.18f, 0.26f, 0.92f));
            sr.rect(x, y, INV_SLOT_SIZE, INV_SLOT_SIZE);
            sr.setColor(isHovered ? new Color(1f, 0.88f, 0.35f, 1f) : new Color(0.40f, 0.55f, 0.75f, 0.75f));
            drawBoxBorder(x, y, INV_SLOT_SIZE, INV_SLOT_SIZE, isHovered ? 2f : 1.2f);

            // UNIQUE VECTOR ART ICON FOR THE ITEM!
            LootVisualRenderer.drawLoot(sr, name, com.fishingrpg.game.loot.CatchableType.CONSUMABLE,
                    x + INV_SLOT_SIZE / 2f, y + INV_SLOT_SIZE / 2f - 2f, 0.70f, timer);

            // Special glow & badge backing for skill books
            if (name.startsWith("Sách: ")) {
                String skillId = getSkillIdFromBookName(name);
                com.fishingrpg.game.skills.Skill s = skillId != null ? player.getSkillTree().getSkill(skillId) : null;
                if (s != null) {
                    if (s.getLevel() == 0) {
                        sr.setColor(0.08f, 0.38f, 0.18f, 0.95f);
                        sr.rect(x + INV_SLOT_SIZE - 29f, y + INV_SLOT_SIZE - 17f, 27f, 15f);
                    } else if (!s.isMaxed()) {
                        float pulse = 0.5f + 0.5f * MathUtils.sin(timer * 5f);
                        sr.setColor(1f, 0.82f, 0.2f, 0.8f + 0.2f * pulse);
                        drawBoxBorder(x, y, INV_SLOT_SIZE, INV_SLOT_SIZE, 2f);
                        sr.setColor(0.55f, 0.38f, 0.08f, 0.95f);
                        sr.rect(x + INV_SLOT_SIZE - 29f, y + INV_SLOT_SIZE - 17f, 27f, 15f);
                    } else {
                        float pulse = 0.5f + 0.5f * MathUtils.sin(timer * 5f);
                        sr.setColor(0.4f, 0.85f, 1f, 0.8f + 0.2f * pulse);
                        drawBoxBorder(x, y, INV_SLOT_SIZE, INV_SLOT_SIZE, 2f);
                        sr.setColor(0.12f, 0.35f, 0.52f, 0.95f);
                        sr.rect(x + INV_SLOT_SIZE - 29f, y + INV_SLOT_SIZE - 17f, 27f, 15f);
                    }
                }
            }

            // Key badge backing in top-left
            sr.setColor(0.06f, 0.08f, 0.14f, 0.92f);
            sr.rect(x + 2f, y + INV_SLOT_SIZE - 17f, 20f, 15f);

            // Quantity badge backing in bottom-right
            int qtyVal = entry.getValue();
            float qbW = (qtyVal >= 100) ? 32f : (qtyVal >= 10) ? 26f : 20f;
            sr.setColor(0.06f, 0.08f, 0.14f, 0.92f);
            sr.rect(x + INV_SLOT_SIZE - qbW - 2f, y + 2f, qbW, 15f);
        }
    }

    public void drawInventoryText(SpriteBatch batch, Player player) {
        if (player == null) return;

        List<Map.Entry<String, Integer>> validEntries = getValidInventoryEntries(player);
        int totalPages = Math.max(1, (validEntries.size() + INV_PAGE_SIZE - 1) / INV_PAGE_SIZE);
        if (quickSlotPage >= totalPages) quickSlotPage = totalPages - 1;
        if (quickSlotPage < 0) quickSlotPage = 0;

        font.setColor(1f, 0.88f, 0.3f, 1f);
        String headerTitle = (totalPages > 1) ? "Ô NHANH (" + (quickSlotPage + 1) + "/" + totalPages + ")" : "PHÍM TẮT NHANH";
        drawCenteredText(font, headerTitle, INV_X, INV_Y + INV_PANEL_H - 28f, INV_PANEL_W, 28f);

        if (totalPages > 1) {
            float prevBtnX = INV_X + 6f;
            float prevBtnY = INV_Y + INV_PANEL_H - 24f;
            float btnW = 22f;
            float btnH = 20f;
            float nextBtnX = INV_X + INV_PANEL_W - 28f;
            float nextBtnY = INV_Y + INV_PANEL_H - 24f;

            font.setColor(hoverQuickslotPrev ? Color.YELLOW : Color.CYAN);
            font.getData().setScale(0.70f);
            layout.setText(font, "◀");
            font.draw(batch, layout, prevBtnX + (btnW - layout.width) / 2f, prevBtnY + (btnH + layout.height) / 2f);

            font.setColor(hoverQuickslotNext ? Color.YELLOW : Color.CYAN);
            layout.setText(font, "▶");
            font.draw(batch, layout, nextBtnX + (btnW - layout.width) / 2f, nextBtnY + (btnH + layout.height) / 2f);
            font.getData().setScale(1f);
        }

        int startIndex = quickSlotPage * INV_PAGE_SIZE;
        int endIndex = Math.min(validEntries.size(), startIndex + INV_PAGE_SIZE);

        for (int i = startIndex; i < endIndex; i++) {
            Map.Entry<String, Integer> entry = validEntries.get(i);
            String name = entry.getKey();
            int slotIndex = i - startIndex;
            int row = slotIndex / 3;
            int col = slotIndex % 3;
            float x = INV_X + INV_PADDING + col * (INV_SLOT_SIZE + INV_PADDING);
            float y = INV_Y + INV_PANEL_H - 28f - INV_PADDING - (row + 1) * INV_SLOT_SIZE - row * INV_PADDING;

            // Top-left: dynamic quickslot hotkey badge in bright cyan
            font.setColor(0.35f, 0.9f, 1f, 1f);
            String hotkeyStr = "[" + com.fishingrpg.game.systems.KeybindManager.getInstance().getQuickslotKeyDisplayName(slotIndex) + "]";
            font.getData().setScale(hotkeyStr.length() > 3 ? 0.55f : 0.70f);
            layout.setText(font, hotkeyStr);
            font.draw(batch, layout, x + 2f + (20f - layout.width) / 2f, y + INV_SLOT_SIZE - 17f + (15f + layout.height) / 2f);
            font.getData().setScale(1f);

            // Top-right skill book status badge text
            if (name.startsWith("Sách: ")) {
                String skillId = getSkillIdFromBookName(name);
                com.fishingrpg.game.skills.Skill s = skillId != null ? player.getSkillTree().getSkill(skillId) : null;
                if (s != null) {
                    font.getData().setScale(0.65f);
                    if (s.getLevel() == 0) {
                        font.setColor(0.45f, 1f, 0.55f, 1f);
                        layout.setText(font, "HỌC");
                        font.draw(batch, layout, x + INV_SLOT_SIZE - 29f + (27f - layout.width) / 2f, y + INV_SLOT_SIZE - 17f + (15f + layout.height) / 2f);
                    } else if (!s.isMaxed()) {
                        font.setColor(1f, 0.92f, 0.3f, 1f);
                        layout.setText(font, "★UP");
                        font.draw(batch, layout, x + INV_SLOT_SIZE - 29f + (27f - layout.width) / 2f, y + INV_SLOT_SIZE - 17f + (15f + layout.height) / 2f);
                    } else {
                        font.setColor(0.45f, 0.95f, 1f, 1f);
                        layout.setText(font, "+SP");
                        font.draw(batch, layout, x + INV_SLOT_SIZE - 29f + (27f - layout.width) / 2f, y + INV_SLOT_SIZE - 17f + (15f + layout.height) / 2f);
                    }
                    font.getData().setScale(1f);
                }
            }

            // Bottom-right: xN quantity in bright gold/white
            font.setColor(1f, 0.9f, 0.25f, 1f);
            font.getData().setScale(0.72f);
            String qtyStr = "x" + entry.getValue();
            layout.setText(font, qtyStr);
            int qtyVal = entry.getValue();
            float qbW = (qtyVal >= 100) ? 32f : (qtyVal >= 10) ? 26f : 20f;
            float qbX = x + INV_SLOT_SIZE - qbW - 2f;
            font.draw(batch, layout, qbX + (qbW - layout.width) / 2f, y + 2f + (15f + layout.height) / 2f);
            font.getData().setScale(1f);
        }

        // Empty state indicator
        if (validEntries.isEmpty()) {
            font.setColor(0.55f, 0.65f, 0.8f, 0.7f);
            font.getData().setScale(0.78f);
            drawCenteredText(font, "Túi rỗng\n(Mua ở Cửa Hàng)", INV_X, INV_Y + 70f, INV_PANEL_W, 40f);
            font.getData().setScale(1f);
        }

        // Tooltip for hovered inventory item
        if (hoveredInventoryItem != null) {
            com.fishingrpg.game.entities.ItemData itemData = com.fishingrpg.game.data.ItemDatabase.getItem(hoveredInventoryItem);
            if (itemData != null) {
                boolean isBook = hoveredInventoryItem.startsWith("Sách: ");
                float tipW = isBook ? 340f : 300f;

                // Tinh chieu cao mo ta va tong chieu cao hop tooltip dong
                font.getData().setScale(0.8f);
                layout.setText(font, itemData.description, Color.WHITE, tipW - 24f, Align.left, true);
                float descH = layout.height;
                float tipH = isBook ? (28f + descH + 12f + 20f + 20f + 16f) : (28f + descH + 12f + 20f + 16f);

                Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                screen.getViewport().unproject(mouse);
                float tipX = mouse.x - tipW - 14f;
                if (tipX < 10f) tipX = mouse.x + 14f;
                float tipY = mouse.y - 10f;
                if (tipY + tipH > H - 10f) tipY = H - tipH - 10f;
                if (tipY < 10f) tipY = 10f;

                batch.end();
                Gdx.gl.glEnable(GL20.GL_BLEND);
                sr.begin(ShapeRenderer.ShapeType.Filled);
                sr.setColor(0.08f, 0.11f, 0.18f, 0.96f);
                sr.rect(tipX, tipY, tipW, tipH);
                sr.setColor(0.85f, 0.65f, 0.25f, 0.85f);
                drawBoxBorder(tipX, tipY, tipW, tipH, 1.6f);
                sr.end();
                Gdx.gl.glDisable(GL20.GL_BLEND);
                batch.begin();

                float curY = tipY + tipH - 16f;
                font.setColor(1f, 0.88f, 0.3f, 1f);
                font.getData().setScale(0.95f);
                font.draw(batch, itemData.name, tipX + 12f, curY);
                curY -= 22f;

                font.setColor(0.85f, 0.88f, 0.95f, 1f);
                font.getData().setScale(0.8f);
                font.draw(batch, itemData.description, tipX + 12f, curY, tipW - 24f, Align.left, true);
                curY -= (descH + 10f);

                if (isBook) {
                    String skillId = getSkillIdFromBookName(hoveredInventoryItem);
                    com.fishingrpg.game.skills.Skill s = skillId != null ? player.getSkillTree().getSkill(skillId) : null;

                    if (s == null || s.getLevel() == 0) {
                        font.setColor(0.4f, 1f, 0.5f, 1f);
                        font.draw(batch, "• Bấm phím / Nhấp: Học kỹ năng (Cấp 1)", tipX + 12f, curY, tipW - 24f, Align.left, true);
                    } else if (!s.isMaxed()) {
                        font.setColor(1f, 0.85f, 0.25f, 1f);
                        font.draw(batch, "• Bấm phím / Nhấp: ĐỘT PHÁ Cấp " + (s.getLevel() + 1) + " (0 SP)", tipX + 12f, curY, tipW - 24f, Align.left, true);
                    } else {
                        font.setColor(0.4f, 0.9f, 1f, 1f);
                        font.draw(batch, "• Bấm phím / Nhấp: QUY ĐỔI thành +1 Điểm Kỹ Năng (SP)", tipX + 12f, curY, tipW - 24f, Align.left, true);
                    }
                    curY -= 20f;

                    font.setColor(0.9f, 0.75f, 0.45f, 1f);
                    font.draw(batch, "• Hoặc đem bán tại Cửa Hàng để nhận " + itemData.sellPrice + " Vàng", tipX + 12f, curY, tipW - 24f, Align.left, true);
                    font.getData().setScale(1f);
                } else {
                    String usageText = "";
                    if (itemData.usageType == com.fishingrpg.game.entities.ItemUsageType.IN_COMBAT) usageText = "• Chỉ dùng lúc giằng co";
                    else if (itemData.usageType == com.fishingrpg.game.entities.ItemUsageType.PRE_COMBAT) usageText = "• Dùng trước khi quăng cần";
                    else usageText = "• Dùng bất kỳ lúc nào";

                    font.setColor(0.4f, 0.9f, 0.5f, 1f);
                    font.draw(batch, usageText, tipX + 12f, curY, tipW - 24f, Align.left, true);
                    font.getData().setScale(1f);
                }
            }
        }
    }

    // =========================================================================
    // COMBAT & CASTING HUD (SHAPES)
    // =========================================================================

    public void drawShapes(Player player, Fish fish) {
        // 0. Water Bobber in WAITING or BITE
        if (screen.fishingManager.state == State.WAITING || screen.fishingManager.state == State.BITE) {
            drawWaterBobberShapes();

            // Sonar detection radar box backing (chuyen xuong nua duoi man hinh Y=200f de khong bi che boi cast buff card o Y=500-550f)
            if (player != null && player.getSonarCharges() > 0 && screen.fishingManager.predictedCatch != null) {
                float boxW = 500f;
                float boxH = 36f;
                float boxX = (W - boxW) / 2f;
                float boxY = 200f;
                sr.setColor(0.04f, 0.12f, 0.20f, 0.92f);
                sr.rect(boxX, boxY, boxW, boxH);
                sr.setColor(0.25f, 0.85f, 1f, 0.85f);
                drawBoxBorder(boxX, boxY, boxW, boxH, 1.5f);
            }
        }

        // 0. Combat Fish in Water & Dynamic Bending Rod in COMBAT
        if (fish != null && screen.fishingManager.state == State.COMBAT) {
            drawCombatFishAndFishingLine(player, fish);
        }

        // 1. Top Fish HP Bar
        if (fish != null && screen.fishingManager.state == State.COMBAT) {
            float hpBarX = (W - HP_W) / 2f;
            float hpBarY = H - 98f; // Moved down to avoid overlapping the top header pills

            // Trailing damage ghost bar decay
            float curHp = fish.getCurrentHp();
            float maxHp = Math.max(1f, fish.getMaxHp());
            if (fishTrailingHp < 0f || fishTrailingHp < curHp) {
                fishTrailingHp = curHp;
            } else if (fishTrailingHp > curHp) {
                float decaySpeed = Math.max(25f, (fishTrailingHp - curHp) * 4f);
                fishTrailingHp = Math.max(curHp, fishTrailingHp - Gdx.graphics.getDeltaTime() * decaySpeed);
            }

            // Outer drop shadow
            sr.setColor(0.02f, 0.04f, 0.08f, 0.65f);
            sr.rect(hpBarX - 3f, hpBarY - 3f, HP_W + 6f, HP_H + 6f);

            // Frame backing
            sr.setColor(0.08f, 0.10f, 0.16f, 0.96f);
            sr.rect(hpBarX, hpBarY, HP_W, HP_H);

            // Empty Track
            sr.setColor(0.18f, 0.06f, 0.08f, 1f);
            sr.rect(hpBarX + 3f, hpBarY + 3f, HP_W - 6f, HP_H - 6f);

            // Trailing ghost bar (Amber / Pale Yellow flare)
            float trailRatio = MathUtils.clamp(fishTrailingHp / maxHp, 0f, 1f);
            if (trailRatio > 0) {
                sr.setColor(1f, 0.82f, 0.35f, 0.85f);
                sr.rect(hpBarX + 3f, hpBarY + 3f, (HP_W - 6f) * trailRatio, HP_H - 6f);
            }

            // Actual HP Fill (Dynamic Color: Emerald Green -> Radiant Gold -> Ruby Red)
            float ratio = MathUtils.clamp(curHp / maxHp, 0f, 1f);
            Color hpCol;
            Color hpHiCol;
            if (ratio > 0.50f) {
                hpCol = new Color(0.18f, 0.82f, 0.38f, 0.98f);
                hpHiCol = new Color(0.45f, 0.95f, 0.55f, 0.75f);
            } else if (ratio > 0.25f) {
                hpCol = new Color(0.98f, 0.72f, 0.15f, 0.98f);
                hpHiCol = new Color(1f, 0.88f, 0.35f, 0.75f);
            } else {
                float dangerPulse = 0.85f + 0.15f * MathUtils.sin(timer * 10f);
                hpCol = new Color(0.95f * dangerPulse, 0.18f, 0.22f, 0.98f);
                hpHiCol = new Color(1f, 0.45f, 0.45f, 0.75f);
            }
            sr.setColor(hpCol);
            sr.rect(hpBarX + 3f, hpBarY + 3f, (HP_W - 6f) * ratio, HP_H - 6f);

            // Top glossy specular line on HP fill
            if (ratio > 0.02f) {
                sr.setColor(hpHiCol);
                sr.rect(hpBarX + 3f, hpBarY + HP_H - 8f, (HP_W - 6f) * ratio, 3f);
            }

            // SOUL SHIELD / ARMOR OVERLAY (HLW-03.4)
            if (fish.hasShield() && fish.getMaxShieldHp() > 0f) {
                float sRatio = MathUtils.clamp(fish.getShieldHp() / fish.getMaxShieldHp(), 0f, 1f);
                sr.setColor(0.12f, 0.85f, 1.0f, 0.82f); // Neon Cyan Shield
                sr.rect(hpBarX + 3f, hpBarY + 3f, (HP_W - 6f) * sRatio, HP_H - 6f);
                // Specular line on top of shield
                sr.setColor(0.70f, 0.95f, 1.0f, 0.90f);
                sr.rect(hpBarX + 3f, hpBarY + HP_H - 7f, (HP_W - 6f) * sRatio, 2.5f);
            }

            // Tick marks at 25%, 50%, 75%
            sr.setColor(0.08f, 0.10f, 0.16f, 0.75f);
            float innerW = HP_W - 6f;
            sr.rect(hpBarX + 3f + innerW * 0.25f - 1f, hpBarY + 3f, 2f, HP_H - 6f);
            sr.rect(hpBarX + 3f + innerW * 0.50f - 1f, hpBarY + 3f, 2f, HP_H - 6f);
            sr.rect(hpBarX + 3f + innerW * 0.75f - 1f, hpBarY + 3f, 2f, HP_H - 6f);

            // Gold Outer Border
            sr.setColor(1f, 0.82f, 0.25f, 0.85f);
            drawBoxBorder(hpBarX, hpBarY, HP_W, HP_H, 1.8f);
        }

        // 2. Vertical Tension Gauge (Left Side) - Modernized Cylinder Housing
        float tensionX = 28f;
        float tensionY = 210f;
        float tensionH = 280f;
        float tensionW = 42f;

        // Tension Header Card Box with Vector Gauge Icon
        float thBoxW = 66f;
        float thBoxH = 46f;
        float thBoxX = tensionX - 12f;
        float thBoxY = tensionY + tensionH + 6f;
        sr.setColor(0.08f, 0.11f, 0.18f, 0.95f);
        sr.rect(thBoxX, thBoxY, thBoxW, thBoxH);
        sr.setColor(0.85f, 0.65f, 0.25f, 0.7f);
        drawBoxBorder(thBoxX, thBoxY, thBoxW, thBoxH, 1.4f);

        // Vector Tension Gauge Icon (Dial meter with needle)
        if (screen.fishingManager.fishQuayBunTimer <= 0) {
            float gcx = thBoxX + thBoxW / 2f;
            float gcy = thBoxY + thBoxH - 14f;
            float gr = 9f;

            // Dial rim
            sr.setColor(0.18f, 0.22f, 0.32f, 1f);
            sr.circle(gcx, gcy, gr, 16);
            sr.setColor(0.06f, 0.08f, 0.14f, 1f);
            sr.circle(gcx, gcy, gr - 1.8f, 16);

            // Arc color tick indicators
            sr.setColor(0.2f, 0.85f, 0.4f, 0.85f);
            sr.arc(gcx, gcy, gr - 2f, 120f, 60f, 8);
            sr.setColor(1f, 0.75f, 0.2f, 0.85f);
            sr.arc(gcx, gcy, gr - 2f, 60f, 60f, 8);
            sr.setColor(0.95f, 0.2f, 0.25f, 0.85f);
            sr.arc(gcx, gcy, gr - 2f, 0f, 60f, 8);

            // Center needle based on tension ratio
            float tRatio = MathUtils.clamp(player.getCurrentLineTension() / Math.max(1f, player.getMaxLineTension()), 0f, 1f);
            float needleAngle = 180f - tRatio * 180f;
            float nRad = needleAngle * MathUtils.degreesToRadians;
            sr.setColor(1f, 0.25f, 0.25f, 1f);
            sr.rectLine(gcx, gcy, gcx + MathUtils.cos(nRad) * 6.5f, gcy + MathUtils.sin(nRad) * 6.5f, 1.5f);
            sr.setColor(1f, 0.95f, 0.5f, 1f);
            sr.circle(gcx, gcy, 2f);
        }

        if (screen.fishingManager.fishQuayBunTimer > 0) {
            // Squid Ink / Blinded Gauge
            sr.setColor(0.05f, 0.02f, 0.06f, 0.95f);
            sr.rect(tensionX, tensionY, tensionW, tensionH);
            sr.setColor(0.5f, 0.1f, 0.6f, 0.85f);
            drawBoxBorder(tensionX, tensionY, tensionW, tensionH, 2f);
        } else {
            // Gauge Cylinder Outer Housing
            sr.setColor(0.08f, 0.10f, 0.16f, 0.95f);
            sr.rect(tensionX, tensionY, tensionW, tensionH);

            // Metallic End Caps
            sr.setColor(0.18f, 0.22f, 0.32f, 1f);
            sr.rect(tensionX - 2f, tensionY + tensionH - 6f, tensionW + 4f, 8f);
            sr.rect(tensionX - 2f, tensionY - 2f, tensionW + 4f, 8f);

            float inH = tensionH - BAR_INSET * 2f;
            float barX = tensionX + BAR_INSET;
            float barY = tensionY + BAR_INSET;
            float barW = tensionW - BAR_INSET * 2f;

            // Ty le Max Tension huu dung theo thoi gian thuc (realtime)
            float maxTensionRatio = screen.fishingManager.getEffectiveMaxTensionRatio();
            float usableH = inH * maxTensionRatio;

            // Empty Track
            sr.setColor(0.06f, 0.08f, 0.12f, 1f);
            sr.rect(barX, barY, barW, inH);

            // Colored Zone Stripes on Usable Track (Safe, Warning, Danger)
            sr.setColor(0.15f, 0.65f, 0.35f, 0.15f); sr.rect(barX, barY, barW, usableH * 0.50f);
            sr.setColor(0.95f, 0.75f, 0.15f, 0.15f); sr.rect(barX, barY + usableH * 0.50f, barW, usableH * 0.35f);
            sr.setColor(0.95f, 0.20f, 0.20f, 0.22f); sr.rect(barX, barY + usableH * 0.85f, barW, usableH * 0.15f);

            // Graduation Tick marks
            sr.setColor(0.4f, 0.5f, 0.65f, 0.7f);
            sr.rect(barX, barY + usableH * 0.25f, 6f, 1.5f);
            sr.rect(barX, barY + usableH * 0.50f, 9f, 2f);
            sr.rect(barX, barY + usableH * 0.75f, 6f, 1.5f);
            sr.setColor(1f, 0.3f, 0.3f, 0.8f);
            sr.rect(barX, barY + usableH * 0.85f, 12f, 2f);

            // Thanh nho bao phan tension da bi giam & Dat moc o duoi moc max
            if (maxTensionRatio < 0.999f) {
                float reducedH = inH - usableH;
                float reducedY = barY + usableH;

                // 1. Thanh nho bao phan tension da bi giam (Hazard Striped Cap)
                sr.setColor(0.45f, 0.06f, 0.10f, 0.88f);
                sr.rect(barX, reducedY, barW, reducedH);

                // 2. Cac vach cheo soc do canh bao bi can mon
                sr.setColor(0.95f, 0.20f, 0.25f, 0.80f);
                int stripes = Math.max(2, (int) (reducedH / 8f));
                for (int s = 0; s < stripes; s++) {
                    float sy = reducedY + (s * 8f);
                    if (sy + 5f <= barY + inH) {
                        sr.rectLine(barX + 2f, sy, barX + barW - 2f, sy + 5f, 1.8f);
                    }
                }

                // 3. Dat moc o duoi moc max (Vach moc do neon nhap nhay tai nguong no day moi)
                float pulse = 0.85f + 0.15f * MathUtils.sin(timer * 12f);
                sr.setColor(1f, 0.22f, 0.25f, pulse);
                sr.rect(barX - 4f, reducedY - 1.5f, barW + 8f, 3f);

                // Mui ten canh bao tam giac chi vao moc
                sr.triangle(barX + barW + 4f, reducedY,
                            barX + barW + 11f, reducedY + 4.5f,
                            barX + barW + 11f, reducedY - 4.5f);
                sr.setColor(1f, 0.90f, 0.30f, 1f);
                sr.circle(barX - 3f, reducedY, 2.2f, 8);
            }

            // Tension Fill (Energy Liquid)
            float tRatio = MathUtils.clamp(player.getCurrentLineTension() / Math.max(1f, player.getMaxLineTension()), 0f, 1f);
            float tUsableRatio = MathUtils.clamp(player.getCurrentLineTension() / Math.max(1f, screen.fishingManager.getEffectiveMaxTension()), 0f, 1f);

            if (tUsableRatio > 0.85f) {
                float pulse = 0.8f + 0.2f * MathUtils.sin(timer * 12f);
                sr.setColor(1f, 0.18f * pulse, 0.18f * pulse, 1f); // Danger Red
            } else if (tUsableRatio > 0.50f) {
                sr.setColor(1f, 0.78f, 0.18f, 0.95f); // Amber
            } else {
                sr.setColor(0.18f, 0.88f, 0.55f, 0.95f); // Emerald
            }
            float fillH = Math.max(0, tRatio * inH);
            sr.rect(barX, barY, barW, fillH);

            // Top meniscus shine line
            if (fillH > 3f) {
                sr.setColor(1f, 1f, 1f, 0.8f);
                sr.rect(barX + 2f, barY + fillH - 2f, barW - 4f, 2f);
            }

            // Glass shine stripe along left edge
            sr.setColor(1f, 1f, 1f, 0.12f);
            sr.rect(barX + 3f, barY, 4f, inH);

            // Precision Chevron Pointer Needle
            float tY = barY + tRatio * inH;
            sr.setColor(1f, 1f, 1f, 1f);
            sr.rect(barX - 2f, tY - 2.5f, barW + 4f, 5f);
            sr.setColor(1f, 0.88f, 0.3f, 1f);
            sr.triangle(barX + barW + 2f, tY,
                        barX + barW + 14f, tY + 6f,
                        barX + barW + 14f, tY - 6f);
            sr.setColor(1f, 1f, 1f, 1f);
            sr.rect(barX + barW + 14f - 2f, tY - 2f, 2f, 2f, 4f, 4f, 1f, 1f, 45f);

            // Outer Gauge Border
            sr.setColor(tRatio > 0.85f ? new Color(1f, 0.25f, 0.25f, 0.95f) : new Color(0.45f, 0.55f, 0.75f, 0.8f));
            drawBoxBorder(tensionX, tensionY, tensionW, tensionH, 1.8f);

            // Flashing hazard badge if tRatio > 0.85
            if (tRatio > 0.85f) {
                float warnY = tensionY - 24f;
                float pulse = 0.7f + 0.3f * MathUtils.sin(timer * 10f);
                sr.setColor(0.7f, 0.12f, 0.12f, pulse);
                sr.rect(tensionX - 8f, warnY, 58f, 20f);
                sr.setColor(1f, 0.85f, 0.2f, 1f);
                drawBoxBorder(tensionX - 8f, warnY, 58f, 20f, 1.2f);
            }
        }

        // 3. Player Stamina Bar (Relocated cleanly to 28f, 140f with Lightning Badge)
        float stmW = 220f;
        float stmH = 26f;
        float stmX = 28f;
        float stmY = 140f;

        // Frame backing
        sr.setColor(0.08f, 0.10f, 0.16f, 0.95f);
        sr.rect(stmX, stmY, stmW, stmH);

        // Stamina Lightning Icon Badge (26x26)
        sr.setColor(0.14f, 0.18f, 0.28f, 1f);
        sr.rect(stmX, stmY, 26f, stmH);
        sr.setColor(1f, 0.85f, 0.25f, 1f);
        sr.triangle(stmX + 14f, stmY + stmH - 4f,
                    stmX + 7f,  stmY + 12f,
                    stmX + 13f, stmY + 12f);
        sr.triangle(stmX + 13f, stmY + 14f,
                    stmX + 19f, stmY + 14f,
                    stmX + 11f, stmY + 4f);

        // Bar track
        float trackX = stmX + 28f;
        float trackY = stmY + 3f;
        float trackW = stmW - 31f;
        float trackH = stmH - 6f;
        sr.setColor(0.06f, 0.08f, 0.12f, 1f);
        sr.rect(trackX, trackY, trackW, trackH);

        // Energy Fill
        float stmRatio = MathUtils.clamp(player.getCurrentStamina() / Math.max(1f, player.getMaxStamina()), 0f, 1f);
        if (stmRatio < 0.25f) {
            float pulse = 0.8f + 0.2f * MathUtils.sin(timer * 8f);
            sr.setColor(0.95f, 0.2f * pulse, 0.2f * pulse, 1f);
        } else {
            sr.setColor(0.18f, 0.88f, 0.48f, 1f);
        }
        sr.rect(trackX, trackY, trackW * stmRatio, trackH);

        // Top glossy highlight stripe on stamina fill
        if (stmRatio > 0.05f) {
            sr.setColor(1f, 1f, 1f, 0.25f);
            sr.rect(trackX, trackY + trackH * 0.55f, trackW * stmRatio, trackH * 0.45f);
        }

        // Golden outer frame & divider
        sr.setColor(0.85f, 0.65f, 0.25f, 0.75f);
        drawBoxBorder(stmX, stmY, stmW, stmH, 1.5f);
        sr.rect(stmX + 26f, stmY, 1.5f, stmH);

        // 4. Casting Meter Gauge (CASTING State) - Modernized Heavy Gauge
        if (screen.fishingManager.state == State.CASTING) {
            float castW = 460f;
            float castH = 38f;
            float castX = (W - castW) / 2f;
            float castY = H / 2f - 20f;

            // Heavy Titanium container backing
            sr.setColor(0.06f, 0.08f, 0.14f, 0.96f);
            sr.rect(castX - 6f, castY - 6f, castW + 12f, castH + 12f);

            // Miss Area (Crimson Red ends)
            sr.setColor(0.78f, 0.18f, 0.22f, 1f);
            sr.rect(castX, castY, castW, castH);

            // Good Area (Warm Amber Gold)
            float goodW = castW * 0.45f;
            sr.setColor(0.95f, 0.72f, 0.16f, 1f);
            sr.rect(castX + (castW - goodW) / 2f, castY, goodW, castH);

            // Perfect Area (Emerald Core with pulsating glow)
            float perfectW = castW * 0.12f;
            if (player.isSkillEquipped("mat_dai_bang")) {
                perfectW *= player.getSkillTree().getMatDaiBangBonus();
            }
            float pulseGlow = 0.85f + 0.15f * MathUtils.sin(timer * 8f);
            sr.setColor(0.15f * pulseGlow, 0.95f * pulseGlow, 0.45f * pulseGlow, 1f);
            sr.rect(castX + (castW - perfectW) / 2f, castY, perfectW, castH);

            // Zone Boundary Tick Markers
            sr.setColor(1f, 1f, 1f, 0.7f);
            sr.rect(castX + (castW - goodW) / 2f - 1f, castY, 2f, castH);
            sr.rect(castX + (castW + goodW) / 2f - 1f, castY, 2f, castH);
            sr.setColor(1f, 0.9f, 0.3f, 0.9f);
            sr.rect(castX + (castW - perfectW) / 2f - 1f, castY, 2f, castH);
            sr.rect(castX + (castW + perfectW) / 2f - 1f, castY, 2f, castH);

            // High-tech Cursor Needle
            float cursorRatio = screen.fishingManager.castCursorX / 200f;
            float cursorPixelX = castX + cursorRatio * castW;

            // Needle light beam
            sr.setColor(0.2f, 0.85f, 1f, 0.6f);
            sr.rect(cursorPixelX - 6f, castY - 10f, 12f, castH + 20f);
            sr.setColor(1f, 1f, 1f, 1f);
            sr.rect(cursorPixelX - 2f, castY - 12f, 4f, castH + 24f);

            // Top and Bottom Diamond Chevrons
            sr.setColor(0.3f, 0.95f, 1f, 1f);
            sr.rect(cursorPixelX - 5f, castY + castH + 5f, 5f, 5f, 10f, 10f, 1f, 1f, 45f);
            sr.rect(cursorPixelX - 5f, castY - 15f, 5f, 5f, 10f, 10f, 1f, 1f, 45f);

            // Gold Outer Frame
            sr.setColor(0.85f, 0.65f, 0.25f, 0.85f);
            drawBoxBorder(castX - 6f, castY - 6f, castW + 12f, castH + 12f, 2f);
        }

        // 5. Active Skill Buttons (Q, W, E, R) in COMBAT - Unified Vector Graphics from Skill Tree
        if (screen.fishingManager.state == State.COMBAT) {
            String[] actives = player.getEquippedActives();
            for (int i = 0; i < 4; i++) {
                float x = BTN_ACTIVES_X + i * (BTN_SLOT_SIZE + BTN_SLOT_SPACING);
                float y = BTN_ACTIVES_Y;
                float size = BTN_SLOT_SIZE;
                float cx = x + size / 2f;
                float cy = y + size / 2f + 3f;
                String skillId = actives[i];
                float cd = (skillId != null) ? getCooldown(skillId) : 0f;
                boolean isEquipped = (skillId != null && player.getSkillTree().getSkill(skillId) != null);

                // Slot Base Frame (Obsidian Glass)
                sr.setColor(0.08f, 0.11f, 0.17f, 0.95f);
                sr.rect(x, y, size, size);

                if (isEquipped) {
                    // Render Vector Skill Icon using SkillTreeScreen.drawSkillIcon
                    SkillTreeScreen.drawSkillIcon(sr, skillId, cx, cy, 22f, new Color(1f, 0.72f, 0.25f, 1f), cd <= 0);

                    // Cooldown dark overlay
                    if (cd > 0) {
                        sr.setColor(0.05f, 0.05f, 0.08f, 0.72f);
                        sr.rect(x + 2f, y + 2f, size - 4f, size - 4f);
                    }
                } else {
                    // Empty slot subtle ring
                    sr.setColor(0.2f, 0.25f, 0.35f, 0.4f);
                    sr.circle(cx, cy, 14f, 16);
                }

                // Hotkey badge background pill at bottom center of slot
                sr.setColor(0.12f, 0.16f, 0.24f, 0.92f);
                sr.rect(x + (size - 32f) / 2f, y + 2f, 32f, 16f);
                sr.setColor(0.35f, 0.75f, 0.95f, 0.7f);
                drawBoxBorder(x + (size - 32f) / 2f, y + 2f, 32f, 16f, 1f);

                // Slot Outer Border (Gold if ready/hover, Slate if on cooldown or empty)
                if (isEquipped && cd <= 0) {
                    sr.setColor(hoverActives[i] ? new Color(1f, 0.95f, 0.4f, 1f) : new Color(0.95f, 0.72f, 0.25f, 0.85f));
                    drawBoxBorder(x, y, size, size, hoverActives[i] ? 2.2f : 1.6f);
                } else {
                    sr.setColor(0.35f, 0.40f, 0.50f, 0.6f);
                    drawBoxBorder(x, y, size, size, 1.2f);
                }
            }
        }

        // 6. Action Primary Button
        if (screen.fishingManager.state == State.PRE_CAST) {
            drawCastButton("QUĂNG CẦN");
        }

        // 7. Persistent Top Header Bar (Level, EXP, Map, Buffs, Gold, Diamonds)
        drawTopPlayerHeaderShapes(player);

        // 8. Catch Notification on Right Sidebar (above Quick Slots)
        if (screen.fishingManager.showCatchSidebar && screen.fishingManager.lastCatch != null) {
            drawCatchSidebarShapes(screen.fishingManager.lastCatch);
        }

        // Reel-Up Leap Animation (Hoạt ảnh kéo cá/rác/kho báu lên khỏi mặt nước)
        if (screen.fishingManager.reelUpTimer > 0 && screen.fishingManager.reelUpCatch != null) {
            drawReelUpAnimationShapes();
        }

        // 9. Screen Ink Overlay Skills
        drawInkOverlays();

        // 10. Back Menu button & Quick Slot Action Dock
        drawMenuBtn();
        drawInventoryShapes(player);
    }

    private void drawTopPlayerHeaderShapes(Player player) {
        if (player == null) return;

        float barY = H - 54f;
        float barH = 36f;

        // 1. Player Level & EXP Pill (Left side, next to Menu button at X=20..120)
        float lvlX = 130f;
        float lvlW = 165f;
        sr.setColor(0.08f, 0.11f, 0.18f, 0.92f);
        sr.rect(lvlX, barY, lvlW, barH);
        sr.setColor(0.85f, 0.65f, 0.25f, 0.7f);
        drawBoxBorder(lvlX, barY, lvlW, barH, 1.4f);

        // Level shield badge (left side of pill)
        float shieldX = lvlX + 20f;
        float shieldY = barY + barH / 2f;
        sr.setColor(0.95f, 0.75f, 0.2f, 1f);
        sr.circle(shieldX, shieldY, 13f, 16);
        sr.setColor(0.12f, 0.16f, 0.26f, 1f);
        sr.circle(shieldX, shieldY, 10.5f, 16);

        // Mini EXP bar
        float expX = lvlX + 42f;
        float expY = barY + 5f;
        float expW = lvlW - 50f;
        float expH = 9f;
        sr.setColor(0.05f, 0.07f, 0.12f, 0.95f);
        sr.rect(expX, expY, expW, expH);
        float xpRatio = MathUtils.clamp((float) player.getCurrentXp() / Math.max(1, player.getXpForCurrentLevel()), 0f, 1f);
        sr.setColor(0.2f, 0.75f, 1f, 0.95f);
        sr.rect(expX, expY, expW * xpRatio, expH);
        sr.setColor(0.4f, 0.85f, 1f, 0.7f);
        drawBoxBorder(expX, expY, expW, expH, 1f);

        // 2. Map Location Pill
        float locX = 305f;
        float locW = 115f;
        sr.setColor(0.06f, 0.14f, 0.18f, 0.90f);
        sr.rect(locX, barY, locW, barH);
        sr.setColor(0.25f, 0.75f, 0.65f, 0.7f);
        drawBoxBorder(locX, barY, locW, barH, 1.3f);
        // Map pin icon: small circle + triangle
        sr.setColor(0.35f, 0.9f, 0.65f, 1f);
        sr.circle(locX + 16f, barY + barH / 2f + 2f, 4.5f);
        sr.triangle(locX + 12f, barY + barH / 2f + 1f,
                    locX + 20f, barY + barH / 2f + 1f,
                    locX + 16f, barY + barH / 2f - 6f);
        sr.setColor(1f, 1f, 1f, 0.9f);
        sr.circle(locX + 16f, barY + barH / 2f + 2f, 1.8f);

        // 3. Persistent Buff Button (BUFF / BUFF (N))
        java.util.List<ActiveBuffDisplay> activeBuffs = getActiveBuffs(player);
        int buffCount = activeBuffs.size();
        float buffBtnX = 438f;
        float buffBtnW = 105f;
        float buffBtnH = barH;
        float buffBtnY = barY;

        if (buffCount > 0) {
            // Active style: Deep teal/emerald plate with glowing border
            sr.setColor(hoverBuff ? new Color(0.12f, 0.28f, 0.22f, 0.95f) : new Color(0.08f, 0.18f, 0.16f, 0.92f));
            sr.rect(buffBtnX, buffBtnY, buffBtnW, buffBtnH);
            sr.setColor(hoverBuff ? new Color(0.45f, 1f, 0.75f, 1f) : new Color(0.3f, 0.85f, 0.55f, 0.85f));
            drawBoxBorder(buffBtnX, buffBtnY, buffBtnW, buffBtnH, hoverBuff ? 1.6f : 1.3f);

            // Pulsing dot indicator
            float pulse = 0.6f + 0.4f * MathUtils.sin(timer * 6f);
            sr.setColor(0.3f, 1f, 0.5f, pulse);
            sr.circle(buffBtnX + 16f, buffBtnY + buffBtnH / 2f, 4.5f);
            sr.setColor(1f, 1f, 1f, 0.9f);
            sr.circle(buffBtnX + 16f, buffBtnY + buffBtnH / 2f, 1.8f);
        } else {
            // Inactive style: Subtle obsidian plate
            sr.setColor(hoverBuff ? new Color(0.12f, 0.16f, 0.22f, 0.92f) : new Color(0.07f, 0.09f, 0.14f, 0.85f));
            sr.rect(buffBtnX, buffBtnY, buffBtnW, buffBtnH);
            sr.setColor(hoverBuff ? new Color(0.5f, 0.65f, 0.85f, 0.8f) : new Color(0.25f, 0.32f, 0.42f, 0.6f));
            drawBoxBorder(buffBtnX, buffBtnY, buffBtnW, buffBtnH, 1.2f);

            // Small indicator icon
            sr.setColor(0.45f, 0.55f, 0.65f, 0.7f);
            sr.circle(buffBtnX + 16f, buffBtnY + buffBtnH / 2f, 3.5f);
        }

        // 4. Gold Pill (Right side)
        float goldW = 150f;
        float goldX = W - goldW - 170f; // 960f
        sr.setColor(0.18f, 0.14f, 0.06f, 0.92f);
        sr.rect(goldX, barY, goldW, barH);
        sr.setColor(1f, 0.85f, 0.25f, 0.75f);
        drawBoxBorder(goldX, barY, goldW, barH, 1.4f);
        // Gold Coin Icon
        float coinX = goldX + 17f;
        float coinY = barY + barH / 2f;
        sr.setColor(1f, 0.85f, 0.2f, 1f);
        sr.circle(coinX, coinY, 8.5f);
        sr.setColor(0.85f, 0.65f, 0.1f, 1f);
        sr.circle(coinX, coinY, 5.5f);
        sr.setColor(1f, 0.95f, 0.5f, 0.9f);
        sr.circle(coinX - 1.5f, coinY + 1.5f, 2f);

        // 5. Diamonds Pill (Far Right side)
        float diaW = 145f;
        float diaX = W - diaW - 16f; // 1119f
        sr.setColor(0.14f, 0.08f, 0.22f, 0.92f);
        sr.rect(diaX, barY, diaW, barH);
        sr.setColor(0.75f, 0.45f, 0.95f, 0.75f);
        drawBoxBorder(diaX, barY, diaW, barH, 1.4f);
        // Diamond Gem Icon
        float gemX = diaX + 17f;
        float gemY = barY + barH / 2f;
        sr.setColor(0.55f, 0.88f, 1f, 1f);
        sr.rect(gemX - 5.5f, gemY - 5.5f, 5.5f, 5.5f, 11f, 11f, 1f, 1f, 45f);
        sr.setColor(1f, 1f, 1f, 0.9f);
        sr.circle(gemX, gemY, 2.5f);
    }

    private void drawCatchSidebarShapes(Catchable catchable) {
        if (catchable == null) return;
        float sbX = INV_X;
        float sbY = INV_Y + INV_PANEL_H + 10f;
        float sbW = INV_PANEL_W;
        float sbH = 325f;

        Color themeColor = Color.GOLD;
        if (catchable instanceof Fish) {
            themeColor = ((Fish) catchable).getRarityColor();
        } else if (catchable instanceof LootItem) {
            LootItem li = (LootItem) catchable;
            if (li.getType() == CatchableType.TREASURE) themeColor = new Color(1f, 0.85f, 0.25f, 1f);
            else if (li.getType() == CatchableType.TRASH) themeColor = new Color(0.6f, 0.75f, 0.8f, 1f);
            else themeColor = new Color(0.35f, 0.95f, 0.65f, 1f);
        }

        // Panel background (Dark obsidian glass)
        sr.setColor(0.08f, 0.11f, 0.17f, 0.95f);
        sr.rect(sbX, sbY, sbW, sbH);
        sr.setColor(themeColor.r, themeColor.g, themeColor.b, 0.85f);
        drawBoxBorder(sbX, sbY, sbW, sbH, 1.6f);

        // Header bar
        sr.setColor(0.14f, 0.18f, 0.28f, 0.95f);
        sr.rect(sbX, sbY + sbH - 26f, sbW, 26f);
        sr.setColor(themeColor.r, themeColor.g, themeColor.b, 0.7f);
        sr.rect(sbX, sbY + sbH - 26f, sbW, 1.5f);

        // Close button backing (top right corner of header)
        sr.setColor(hoverCatchClose ? new Color(0.65f, 0.2f, 0.2f, 0.95f) : new Color(0.18f, 0.22f, 0.32f, 0.8f));
        sr.rect(sbX + sbW - 22f, sbY + sbH - 22f, 18f, 18f);

        // Center illustration & pedestal
        float cx = sbX + sbW / 2f;
        float cy = sbY + 185f;
        sr.setColor(themeColor.r, themeColor.g, themeColor.b, 0.15f);
        sr.circle(cx, cy, 38f, 24);

        // Concentric, perfectly centered pedestal platform ellipses!
        float pw = 84f, ph = 18f;
        sr.setColor(0.12f, 0.16f, 0.24f, 0.85f);
        sr.ellipse(cx - pw / 2f, cy - 24f - ph / 2f, pw, ph);
        sr.setColor(themeColor.r, themeColor.g, themeColor.b, 0.6f);
        sr.ellipse(cx - (pw - 8f) / 2f, cy - 24f - (ph - 4f) / 2f, pw - 8f, ph - 4f);
        sr.setColor(0.08f, 0.10f, 0.16f, 0.95f);
        sr.ellipse(cx - (pw - 16f) / 2f, cy - 23f - (ph - 6f) / 2f, pw - 16f, ph - 6f);

        // Draw the vector art illustration!
        if (catchable instanceof Fish) {
            Fish f = (Fish) catchable;
            FishVisualRenderer.drawFish(sr, f.getName(), cx, cy, 0.82f, timer, themeColor);
        } else if (catchable instanceof LootItem) {
            LootItem loot = (LootItem) catchable;
            LootVisualRenderer.drawLoot(sr, loot.getName(), loot.getType(), cx, cy, 0.88f, timer);
        }

        // Inner specs & rewards card
        sr.setColor(0.06f, 0.08f, 0.13f, 0.92f);
        sr.rect(sbX + 8f, sbY + 8f, sbW - 16f, 118f);
        sr.setColor(0.22f, 0.28f, 0.38f, 0.7f);
        drawBoxBorder(sbX + 8f, sbY + 8f, sbW - 16f, 118f, 1.2f);
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
        // 0. Persistent Top Header Bar (Level, EXP, Map, Buffs, Gold, Diamonds)
        drawTopPlayerHeaderText(batch, player);

        // Sonar detection banner during WAITING or BITE (only when player has active sonar charges)
        if (player != null && player.getSonarCharges() > 0 
                && (screen.fishingManager.state == State.WAITING || screen.fishingManager.state == State.BITE) 
                && screen.fishingManager.predictedCatch != null) {
            float boxW = 500f;
            float boxH = 36f;
            float boxX = (W - boxW) / 2f;
            float boxY = 200f; // Chuyen xuong nua duoi man hinh Y=200f dong bo voi drawShapes
            Catchable pc = screen.fishingManager.predictedCatch;
            font.setColor(0.35f, 0.95f, 1f, 1f);
            font.getData().setScale(0.85f);
            String radarText = "✦ RADAR THẤU THỊ: Đang tiếp cận [" + pc.getName() + "] (Còn " + player.getSonarCharges() + " lượt)";
            layout.setText(font, radarText);
            font.draw(batch, layout, boxX + (boxW - layout.width) / 2f, boxY + (boxH + layout.height) / 2f);
            font.getData().setScale(1f);
        }

        // 1. Top Fish HP and Creature Name in COMBAT
        if (item != null && screen.fishingManager.state == State.COMBAT) {
            float hpBarX = (W - HP_W) / 2f;
            float hpBarY = H - 98f; // Moved down to avoid overlapping the top header pills

            Color nameColor = Color.WHITE;
            if (item instanceof Fish) nameColor = ((Fish) item).getRarityColor();

            // Fish Name with drop shadow
            String fishTitle = item.getName();
            if (item instanceof Fish && ((Fish) item).getRarity() == Fish.Rarity.LEGENDARY) {
                fishTitle = "[LEGENDARY] " + fishTitle;
            } else if (item instanceof Fish && ((Fish) item).getRarity() == Fish.Rarity.BOSS) {
                fishTitle = "[BOSS] " + fishTitle;
            }

            // Fish HP Value
            String hpString = "";
            if (item instanceof Fish) {
                Fish f = (Fish) item;
                if (f.hasShield()) {
                    hpString = (int) f.getCurrentHp() + " / " + (int) f.getMaxHp() + " HP [GIÁP: " + (int) f.getShieldHp() + "]";
                } else {
                    hpString = (int) f.getCurrentHp() + " / " + (int) f.getMaxHp() + " HP (" +
                                      (int) (MathUtils.clamp(f.getCurrentHp() / f.getMaxHp(), 0f, 1f) * 100) + "%)";
                }
            }
            layout.setText(font, hpString);
            float hpTextW = layout.width;

            // Auto-scale fish title if it would collide with HP string
            float maxTitleW = HP_W - hpTextW - 35f;
            layout.setText(font, fishTitle);
            if (layout.width > maxTitleW) {
                font.getData().setScale(maxTitleW / layout.width);
            }
            // Drop shadow
            font.setColor(0f, 0f, 0f, 0.85f);
            font.draw(batch, fishTitle, hpBarX + 13f, hpBarY + HP_H - 11f);
            // Title text
            font.setColor(nameColor);
            font.draw(batch, fishTitle, hpBarX + 12f, hpBarY + HP_H - 10f);
            font.getData().setScale(1f);

            if (!hpString.isEmpty()) {
                // Drop shadow
                font.setColor(0f, 0f, 0f, 0.85f);
                font.draw(batch, hpString, hpBarX + HP_W - hpTextW - 11f, hpBarY + HP_H - 11f);
                // HP text
                font.setColor(Color.WHITE);
                font.draw(batch, hpString, hpBarX + HP_W - hpTextW - 12f, hpBarY + HP_H - 10f);
            }
        }

        // 2. Tension Gauge Readings (Integrated with Header Card)
        float tensionX = 28f;
        float tensionY = 210f;
        float tensionW = 42f;
        float tensionH = 280f;
        float thBoxW = 66f;
        float thBoxH = 46f;
        float thBoxX = tensionX - 12f;
        float thBoxY = tensionY + tensionH + 6f;

        if (screen.fishingManager.fishQuayBunTimer > 0) {
            // Header box centered "???" (NO "MẤT TÍN HIỆU" text)
            font.getData().setScale(0.85f);
            font.setColor(0f, 0f, 0f, 0.90f);
            drawCenteredText(font, "???", thBoxX + 1f, thBoxY - 1f, thBoxW, thBoxH);
            font.setColor(new Color(1f, 0.45f, 0.45f, 1f));
            drawCenteredText(font, "???", thBoxX, thBoxY, thBoxW, thBoxH);

            // Gauge cylinder centered "???"
            font.getData().setScale(1.4f);
            layout.setText(font, "???");
            float qx = tensionX + (tensionW - layout.width) / 2f;
            float qy = tensionY + (tensionH + layout.height) / 2f;
            font.setColor(0f, 0f, 0f, 0.95f);
            font.draw(batch, "???", qx + 1.5f, qy - 1.5f);
            font.setColor(new Color(1f, 0.35f, 0.4f, 1f));
            font.draw(batch, "???", qx, qy);
            font.getData().setScale(1f);
        } else {
            float tRatio = player.getCurrentLineTension() / Math.max(1f, player.getMaxLineTension());
            // Centered tension reading numbers without "ĐỘ CĂNG" text
            String tStr = (int) player.getCurrentLineTension() + "/" + (int) player.getMaxLineTension();
            font.getData().setScale(0.70f);
            layout.setText(font, tStr);
            float tx = thBoxX + (thBoxW - layout.width) / 2f;
            float ty = thBoxY + 16f;
            // Shadow
            font.setColor(0f, 0f, 0f, 0.9f);
            font.draw(batch, tStr, tx + 1f, ty - 1f);
            // Main text (So giu nguyen khong doi theo yeu cau nguoi dung)
            font.setColor(tRatio > 0.85f ? new Color(1f, 0.3f, 0.3f, 1f) : Color.WHITE);
            font.draw(batch, tStr, tx, ty);
            font.getData().setScale(1f);

            // Text chi bao gioi han bi giam canh vach moc
            float effRatio = screen.fishingManager.getEffectiveMaxTensionRatio();
            if (effRatio < 0.999f) {
                float inH = tensionH - BAR_INSET * 2f;
                float mY = (tensionY + BAR_INSET) + inH * effRatio;
                font.setColor(1f, 0.3f, 0.3f, 1f);
                font.getData().setScale(0.60f);
                font.draw(batch, "GIỚI HẠN: -" + (int) Math.round((1f - effRatio) * 100) + "%", tensionX + tensionW + 14f, mY + 4f);
                font.getData().setScale(1f);
            }

            if (tRatio > 0.85f) {
                font.setColor(1f, 0.95f, 0.4f, 1f);
                font.getData().setScale(0.68f);
                font.draw(batch, "CĂNG DÂY!", tensionX - 4f, tensionY - 10f);
                font.getData().setScale(1f);
            }
        }

        // 3. Player Stamina in Combat (Only numbers, bold & clear with single razor-sharp drop shadow)
        float stmW = 220f;
        float stmH = 26f;
        float stmX = 28f;
        float stmY = 140f;
        String stmStr = (int) player.getCurrentStamina() + " / " + (int) player.getMaxStamina();
        font.getData().setScale(0.82f);
        // Single crisp drop shadow (fixed duplicate opposite offset bug)
        font.setColor(0.02f, 0.04f, 0.08f, 0.95f);
        drawCenteredText(font, stmStr, stmX + 28f + 1f, stmY - 1f, stmW - 28f, stmH);
        // Crisp Ivory White
        font.setColor(1f, 1f, 1f, 1f);
        drawCenteredText(font, stmStr, stmX + 28f, stmY, stmW - 28f, stmH);
        font.getData().setScale(1f);

        // 4. Combat Guidance & Fish Skill Alert Banner (Center Screen)
        if (screen.fishingManager.state == State.COMBAT && item instanceof Fish) {
            Fish f = (Fish) item;

            // Check for fish skill alerts
            boolean isSkillActive = false;
            boolean isPulling = false;
            boolean isTired = false;
            String bannerText;
            Color bannerBgCol;
            Color bannerBorderCol;

            if (screen.fishingManager.fishCuongNoTimer > 0) {
                isSkillActive = true;
                bannerText = "[!] [CUỒNG NỘ] BẤT TỬ & GIẢI HIỆU ỨNG! [!]";
                bannerBgCol = new Color(0.78f, 0.10f, 0.12f, 0.92f);
                bannerBorderCol = new Color(1f, 0.35f, 0.25f, 1f);
            } else if (screen.fishingManager.fishQuayBunTimer > 0) {
                isSkillActive = true;
                bannerText = "[!] [QUẪY BÙN] MẤT ĐO ĐỘ CĂNG DÂY! [!]";
                bannerBgCol = new Color(0.48f, 0.12f, 0.58f, 0.92f);
                bannerBorderCol = new Color(0.85f, 0.45f, 1f, 1f);
            } else if (screen.fishingManager.fishSongThanTimer > 0) {
                isSkillActive = true;
                bannerText = "[!] [SÓNG THẦN] THỂ LỰC ĐANG SỤT GIẢM! [!]";
                bannerBgCol = new Color(0.10f, 0.28f, 0.68f, 0.92f);
                bannerBorderCol = new Color(0.35f, 0.75f, 1f, 1f);
            } else if (screen.fishingManager.fishButTocTimer > 0) {
                isSkillActive = true;
                bannerText = "[!] [BỨT TỐC] LỰC KÉO CỦA CÁ x2! [!]";
                bannerBgCol = new Color(0.85f, 0.36f, 0.10f, 0.92f);
                bannerBorderCol = new Color(1f, 0.75f, 0.25f, 1f);
            } else if (screen.fishingManager.fishPhunMucTimer > 0) {
                isSkillActive = true;
                bannerText = "[!] [PHUN MỰC] TẦM NHÌN BỊ CHE KHUẤT! [!]";
                bannerBgCol = new Color(0.12f, 0.12f, 0.16f, 0.95f);
                bannerBorderCol = new Color(0.6f, 0.6f, 0.7f, 1f);
            } else if (screen.fishingManager.fishGiatDienTimer > 0) {
                isSkillActive = true;
                bannerText = "[!] [GIẬT ĐIỆN] TRÁNH GIỮ DÂY NGAY! [!]";
                bannerBgCol = new Color(0.72f, 0.62f, 0.10f, 0.92f);
                bannerBorderCol = new Color(1f, 0.95f, 0.35f, 1f);
            } else if (f.getState() == Fish.State.PULLING) {
                isPulling = true;
                bannerText = "◄◄ CÁ ĐANG GIÃY MẠNH! THẢ DÂY NGAY! ◄◄";
                bannerBgCol = new Color(0.84f, 0.12f, 0.14f, 0.92f);
                bannerBorderCol = new Color(1f, 0.70f, 0.22f, 1f);
            } else {
                isTired = true;
                String pullKeyName = com.fishingrpg.game.systems.KeybindManager.getInstance().getKeyDisplayName(com.fishingrpg.game.systems.KeybindManager.ACTION_PULL);
                bannerText = "►► CÁ ĐANG MỆT! GIỮ [" + pullKeyName + "] KÉO CẦN! ►►";
                bannerBgCol = new Color(0.08f, 0.58f, 0.32f, 0.92f);
                bannerBorderCol = new Color(0.35f, 1f, 0.65f, 1f);
            }

            float bannerW = isSkillActive ? 520f : 460f;
            float bannerH = 46f;
            float bannerX = (W - bannerW) / 2f;
            float bannerY = H / 2f + 115f;
            float pulse = isSkillActive ? (0.85f + 0.15f * MathUtils.sin(timer * 8f)) : 1f;

            batch.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            sr.begin(ShapeRenderer.ShapeType.Filled);

            // Banner shadow
            sr.setColor(0.02f, 0.04f, 0.08f, 0.55f);
            sr.rect(bannerX + 3f, bannerY - 3f, bannerW, bannerH);

            // Banner background fill
            sr.setColor(bannerBgCol.r * pulse, bannerBgCol.g * pulse, bannerBgCol.b * pulse, bannerBgCol.a);
            sr.rect(bannerX, bannerY, bannerW, bannerH);

            // Top gloss specular ribbon
            sr.setColor(1f, 1f, 1f, 0.32f);
            sr.rect(bannerX, bannerY + bannerH - 3f, bannerW, 3f);

            // Side Accents for Pulling & Tired States
            if (isPulling) {
                float hPulse = 0.65f + 0.35f * MathUtils.sin(timer * 12f);
                sr.setColor(1f, 0.85f, 0.2f, 0.9f * hPulse);
                sr.rect(bannerX + 6f, bannerY + 6f, 16f, bannerH - 12f);
                sr.rect(bannerX + bannerW - 22f, bannerY + 6f, 16f, bannerH - 12f);
            } else if (isTired) {
                float cPulse = 0.7f + 0.3f * MathUtils.sin(timer * 8f);
                sr.setColor(0.45f, 1f, 0.75f, 0.85f * cPulse);
                sr.triangle(bannerX + 8f, bannerY + bannerH / 2f, bannerX + 20f, bannerY + bannerH - 8f, bannerX + 20f, bannerY + 8f);
                sr.triangle(bannerX + bannerW - 8f, bannerY + bannerH / 2f, bannerX + bannerW - 20f, bannerY + bannerH - 8f, bannerX + bannerW - 20f, bannerY + 8f);
            }

            // Border
            sr.setColor(bannerBorderCol.r, bannerBorderCol.g, bannerBorderCol.b, bannerBorderCol.a * pulse);
            drawBoxBorder(bannerX, bannerY, bannerW, bannerH, (isSkillActive || isPulling) ? 2.4f : 1.8f);

            sr.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            batch.begin();

            // Text with drop shadow
            font.setColor(0f, 0f, 0f, 0.90f);
            drawCenteredText(font, bannerText, bannerX + 1.2f, bannerY - 1.2f, bannerW, bannerH);
            font.setColor(isPulling ? new Color(1f, 0.95f, 0.6f, 1f) : isTired ? new Color(0.95f, 1f, 0.95f, 1f) : Color.WHITE);
            drawCenteredText(font, bannerText, bannerX, bannerY, bannerW, bannerH);
        }

        // 5. Active Skills Text & Hotkeys (Dynamic Keybinds)
        if (screen.fishingManager.state == State.COMBAT) {
            String[] actives = player.getEquippedActives();
            com.fishingrpg.game.systems.KeybindManager km = com.fishingrpg.game.systems.KeybindManager.getInstance();
            for (int i = 0; i < 4; i++) {
                float x = BTN_ACTIVES_X + i * (BTN_SLOT_SIZE + BTN_SLOT_SPACING);
                float y = BTN_ACTIVES_Y;
                float size = BTN_SLOT_SIZE;
                String skillId = actives[i];
                float cd = (skillId != null) ? getCooldown(skillId) : 0f;

                // Hotkey badge text centered inside bottom pill
                String keyLabel = "[" + km.getSkillKeyDisplayName(i) + "]";
                font.getData().setScale(keyLabel.length() > 4 ? 0.56f : 0.68f);
                font.setColor(0.45f, 0.85f, 1f, 1f);
                drawCenteredText(font, keyLabel, x + (size - 32f) / 2f, y + 2f, 32f, 16f);
                font.getData().setScale(1f);

                if (skillId != null) {
                    // Cooldown countdown text centered over slot
                    if (cd > 0) {
                        font.getData().setScale(0.92f);
                        String cdStr = String.format("%.1fs", cd);
                        layout.setText(font, cdStr);
                        // Shadow
                        font.setColor(0f, 0f, 0f, 0.95f);
                        font.draw(batch, cdStr, x + (size - layout.width) / 2f + 1f, y + size / 2f + 8f);
                        // Radiant Yellow
                        font.setColor(1f, 0.88f, 0.25f, 1f);
                        font.draw(batch, cdStr, x + (size - layout.width) / 2f, y + size / 2f + 9f);
                        font.getData().setScale(1f);
                    }
                }
            }

            // Draw hover tooltip for active skill in combat
            for (int i = 0; i < 4; i++) {
                if (hoverActives[i] && actives[i] != null) {
                    String skillId = actives[i];
                    com.fishingrpg.game.skills.Skill s = player.getSkillTree().getSkill(skillId);
                    if (s != null) {
                        float slotX = BTN_ACTIVES_X + i * (BTN_SLOT_SIZE + BTN_SLOT_SPACING);
                        float tipW = 310f;
                        float tipH = 92f;
                        float tipX = MathUtils.clamp(slotX + BTN_SLOT_SIZE / 2f - tipW / 2f, 10f, W - tipW - 10f);
                        float tipY = BTN_ACTIVES_Y + BTN_SLOT_SIZE + 12f;

                        batch.end();
                        Gdx.gl.glEnable(GL20.GL_BLEND);
                        sr.begin(ShapeRenderer.ShapeType.Filled);
                        // Shadow
                        sr.setColor(0f, 0f, 0f, 0.45f);
                        sr.rect(tipX + 3f, tipY - 3f, tipW, tipH);
                        // Obsidian Plate
                        sr.setColor(0.07f, 0.10f, 0.16f, 0.96f);
                        sr.rect(tipX, tipY, tipW, tipH);
                        // Gold Trim Border
                        sr.setColor(0.85f, 0.65f, 0.25f, 0.90f);
                        drawBoxBorder(tipX, tipY, tipW, tipH, 1.6f);
                        sr.end();
                        Gdx.gl.glDisable(GL20.GL_BLEND);
                        batch.begin();

                        // Title, Level & Hotkey
                        font.getData().setScale(0.85f);
                        font.setColor(1f, 0.85f, 0.35f, 1f);
                        String titleStr = s.name + (s.isUnlocked() ? (" (Lv." + s.getLevel() + ")") : "") + " [" + km.getSkillKeyDisplayName(i) + "]";
                        font.draw(batch, titleStr, tipX + 12f, tipY + tipH - 12f);

                        // Description / Level-scaled effect
                        font.setColor(0.85f, 0.90f, 0.98f, 1f);
                        font.getData().setScale(0.68f);
                        String effectDesc = s.isUnlocked() ? s.getCurrentLevelEffectString() : s.description;
                        font.draw(batch, effectDesc, tipX + 12f, tipY + tipH - 34f, tipW - 24f, com.badlogic.gdx.utils.Align.left, true);

                        // Cooldown & Stamina Cost at the bottom
                        font.getData().setScale(0.68f);
                        float cdVal = s.getCooldown();
                        float stamCost = 0f;
                        if (skillId.equals("keo_manh")) stamCost = player.getSkillTree().getKeoManhStaminaCost();
                        else if (skillId.equals("giat_kep")) stamCost = 40f;
                        else if (skillId.equals("pha_giap")) stamCost = 30f;

                        font.setColor(0.4f, 0.85f, 1f, 1f);
                        String statsStr = "• Hồi chiêu: " + String.format(java.util.Locale.US, "%.1fs", cdVal)
                                + (stamCost > 0 ? ("  •  Tiêu hao: " + (int)stamCost + " Thể lực") : "  •  Tiêu hao: 0 Thể lực");
                        font.draw(batch, statsStr, tipX + 12f, tipY + 18f);
                        font.getData().setScale(1f);
                    }
                    break;
                }
            }
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

        // 8. Catch Notification on Right Sidebar Content
        if (screen.fishingManager.showCatchSidebar && screen.fishingManager.lastCatch != null) {
            drawCatchSidebarText(batch, screen.fishingManager.lastCatch, screen.fishingManager.lastCatchExtra, screen.fishingManager.lastCatchResultLine, screen.fishingManager.lastCatchDiamonds);
        }

        // Active Buffs Report Card during casting
        if (screen.fishingManager.castBuffBannerTimer > 0) {
            float timer = screen.fishingManager.castBuffBannerTimer;
            float alpha = Math.min(1f, timer / 0.6f);
            java.util.List<String> buffs = screen.fishingManager.castBuffList;
            int buffCount = (buffs != null) ? buffs.size() : 0;
            float cardW = 440f;
            float cardH = 34f + buffCount * 22f + 14f;
            float cardX = (W - cardW) / 2f;
            float cardY = H - 65f - cardH;

            batch.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            sr.begin(ShapeRenderer.ShapeType.Filled);

            // Shadow
            sr.setColor(0.02f, 0.04f, 0.08f, 0.55f * alpha);
            sr.rect(cardX + 4f, cardY - 4f, cardW, cardH);

            // Glassmorphism card body (deep navy/slate)
            sr.setColor(0.08f, 0.12f, 0.20f, 0.92f * alpha);
            sr.rect(cardX, cardY, cardW, cardH);

            // Specular top highlight
            sr.setColor(0.4f, 0.8f, 1f, 0.25f * alpha);
            sr.rect(cardX + 2f, cardY + cardH - 3f, cardW - 4f, 2f);

            // Card gold border
            sr.setColor(0.85f, 0.70f, 0.25f, 0.85f * alpha);
            drawBoxBorder(cardX, cardY, cardW, cardH, 1.5f);

            sr.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            batch.begin();

            // Header: ✦ HIỆU ỨNG TỶ LỆ CÂU KÍCH HOẠT ✦
            font.getData().setScale(0.78f);
            String cardHeader = "✦ HIỆU ỨNG TỶ LỆ CÂU KÍCH HOẠT ✦";
            layout.setText(font, cardHeader);
            font.setColor(0f, 0f, 0f, 0.85f * alpha);
            font.draw(batch, cardHeader, cardX + (cardW - layout.width) / 2f + 1f, cardY + cardH - 10f - 1f);
            font.setColor(1f, 0.88f, 0.35f, alpha);
            font.draw(batch, cardHeader, cardX + (cardW - layout.width) / 2f, cardY + cardH - 10f);

            // List each buff row
            font.getData().setScale(0.72f);
            float rowY = cardY + cardH - 32f;
            if (buffs != null) {
                for (String b : buffs) {
                    font.setColor(0f, 0f, 0f, 0.85f * alpha);
                    font.draw(batch, b, cardX + 20f + 1f, rowY - 1f);
                    font.setColor(0.45f, 1f, 0.75f, alpha);
                    font.draw(batch, b, cardX + 20f, rowY);
                    rowY -= 22f;
                }
            }
            font.getData().setScale(1f);
        }

        // Fish Escape / Bite Timeout Notice Banner
        if (screen.fishingManager.fishEscapeTimer > 0) {
            float timer = screen.fishingManager.fishEscapeTimer;
            float alpha = Math.min(1f, timer / 0.5f);
            float bannerW = 560f;
            float bannerH = 46f;
            float bannerX = (W - bannerW) / 2f;
            float bannerY = H / 2f + 55f;

            batch.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            sr.begin(ShapeRenderer.ShapeType.Filled);

            // Shadow
            sr.setColor(0.02f, 0.04f, 0.08f, 0.6f * alpha);
            sr.rect(bannerX + 3f, bannerY - 3f, bannerW, bannerH);

            // Dark ruby/obsidian chassis
            sr.setColor(0.55f, 0.08f, 0.10f, 0.92f * alpha);
            sr.rect(bannerX, bannerY, bannerW, bannerH);

            // Glowing hazard border
            sr.setColor(1f, 0.35f, 0.25f, 0.95f * alpha);
            drawBoxBorder(bannerX, bannerY, bannerW, bannerH, 2f);

            // Warning icon flags on left and right
            sr.setColor(1f, 0.85f, 0.2f, alpha);
            sr.triangle(bannerX + 18f, bannerY + 10f, bannerX + 32f, bannerY + 10f, bannerX + 25f, bannerY + 34f);
            sr.triangle(bannerX + bannerW - 32f, bannerY + 10f, bannerX + bannerW - 18f, bannerY + 10f, bannerX + bannerW - 25f, bannerY + 34f);

            sr.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            batch.begin();

            String escText = (screen.fishingManager.fishEscapeMessage != null && !screen.fishingManager.fishEscapeMessage.isEmpty())
                    ? screen.fishingManager.fishEscapeMessage
                    : "CHẬM TAY RỒI! CÁ ĐÃ ĂN HẾT MỒI VÀ CHẠY MẤT!";
            font.getData().setScale(0.85f);
            layout.setText(font, escText);
            float ex = bannerX + (bannerW - layout.width) / 2f;
            float ey = bannerY + (bannerH + layout.height) / 2f;
            // Shadow
            font.setColor(0f, 0f, 0f, 0.95f * alpha);
            font.draw(batch, escText, ex + 1.2f, ey - 1.2f);
            // Crisp White/Yellow
            font.setColor(1f, 0.95f, 0.65f, alpha);
            font.draw(batch, escText, ex, ey);
            font.getData().setScale(1f);
        }

        if (screen.fishingManager.state == State.PRE_CAST) {
            String castLabel = "QUĂNG CẦN";
            font.getData().setScale(1f);
            layout.setText(font, castLabel);
            float totalGroupW = 20f + 12f + layout.width;
            float groupStartX = BTN_CAST_X + (BTN_CAST_W - totalGroupW) / 2f;
            float textX = groupStartX + 20f + 12f;
            float textY = BTN_CAST_Y + (BTN_CAST_H + layout.height) / 2f;

            font.setColor(0.12f, 0.05f, 0.02f, 0.95f);
            font.draw(batch, castLabel, textX + 1.2f, textY - 1.2f);
            font.setColor(hoverCast ? new Color(1f, 0.98f, 0.6f, 1f) : Color.WHITE);
            font.draw(batch, castLabel, textX, textY);
        }

        // Menu Back button -> BẢN ĐỒ (Khi đang trong màn hình chơi câu cá)
        String mapLabel = "BẢN ĐỒ";
        layout.setText(font, mapLabel);
        float mbx = BTN_MENU_X + 16f + (BTN_MENU_W - 16f - layout.width) / 2f;
        float mby = BTN_MENU_Y + (BTN_MENU_H + layout.height) / 2f;
        font.setColor(0f, 0f, 0f, 0.85f);
        font.draw(batch, mapLabel, mbx + 1f, mby - 1f);
        font.setColor(hoverMenu ? new Color(1f, 0.92f, 0.4f, 1f) : Color.WHITE);
        font.draw(batch, mapLabel, mbx, mby);

        drawInventoryText(batch, player);
    }

    private void drawTopPlayerHeaderText(SpriteBatch batch, Player player) {
        if (player == null) return;
        float barY = H - 54f;
        float barH = 36f;

        // 1. Level text - Number only, centered in the golden medallion shield!
        float lvlX = 130f;
        float shieldX = lvlX + 20f;
        float shieldY = barY + barH / 2f;
        String lvlStr = String.valueOf(player.getLevel());
        font.getData().setScale(0.85f);
        layout.setText(font, lvlStr);
        // Dark drop shadow
        font.setColor(0.05f, 0.08f, 0.12f, 0.95f);
        font.draw(batch, lvlStr, shieldX - layout.width / 2f + 1f, shieldY + layout.height / 2f - 1f);
        // Radiant golden-yellow number
        font.setColor(1f, 0.92f, 0.35f, 1f);
        font.draw(batch, lvlStr, shieldX - layout.width / 2f, shieldY + layout.height / 2f);
        font.getData().setScale(1f);

        // EXP text
        float expX = lvlX + 42f;
        int xpPercent = (int) (MathUtils.clamp((float) player.getCurrentXp() / Math.max(1, player.getXpForCurrentLevel()), 0f, 1f) * 100);
        font.setColor(0.82f, 0.92f, 1f, 1f);
        font.getData().setScale(0.68f);
        font.draw(batch, player.getCurrentXp() + "/" + player.getXpForCurrentLevel() + " (" + xpPercent + "%)", expX + 4f, barY + barH - 4f);
        font.getData().setScale(1f);

        // 2. Map Location text (Dynamic based on current map)
        MapArea curArea = MapArea.getMap(player.getCurrentMap());
        String locName = curArea != null ? curArea.name : "Ao Làng";
        float locX = 305f;
        font.setColor(0.75f, 0.95f, 0.88f, 1f);
        font.getData().setScale(0.80f);
        font.draw(batch, locName, locX + 30f, barY + barH / 2f + 5f);
        font.getData().setScale(1f);

        // 3. Persistent Buff Button (BUFF / BUFF (N))
        java.util.List<ActiveBuffDisplay> activeBuffs = getActiveBuffs(player);
        int buffCount = activeBuffs.size();
        float buffBtnX = 438f;
        float buffBtnW = 105f;
        float buffBtnH = barH;
        float buffBtnY = barY;

        String buffBtnText = (buffCount > 0) ? ("BUFF (" + buffCount + ")") : "BUFF";
        font.getData().setScale(0.78f);
        layout.setText(font, buffBtnText);
        float btx = buffBtnX + 26f + (buffBtnW - 26f - layout.width) / 2f;
        float bty = buffBtnY + (buffBtnH + layout.height) / 2f;

        font.setColor(0f, 0f, 0f, 0.85f);
        font.draw(batch, buffBtnText, btx + 1f, bty - 1f);
        if (buffCount > 0) {
            font.setColor(hoverBuff ? new Color(1f, 1f, 0.8f, 1f) : new Color(0.45f, 1f, 0.7f, 1f));
        } else {
            font.setColor(hoverBuff ? Color.WHITE : new Color(0.6f, 0.7f, 0.8f, 0.9f));
        }
        font.draw(batch, buffBtnText, btx, bty);
        font.getData().setScale(1f);

        // Tooltip when hovering over BUFF button
        if (hoverBuff) {
            float tipX = buffBtnX;
            float tipW = 410f;
            float tipH = (buffCount > 0) ? (34f + buffCount * 38f + 14f) : 80f;
            float tipY = buffBtnY - tipH - 8f;

            batch.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            sr.begin(ShapeRenderer.ShapeType.Filled);

            // Drop shadow
            sr.setColor(0.02f, 0.04f, 0.08f, 0.55f);
            sr.rect(tipX + 4f, tipY - 4f, tipW, tipH);

            // Obsidian background
            sr.setColor(0.07f, 0.10f, 0.16f, 0.96f);
            sr.rect(tipX, tipY, tipW, tipH);

            // Header bar
            sr.setColor(0.12f, 0.16f, 0.25f, 0.95f);
            sr.rect(tipX, tipY + tipH - 28f, tipW, 28f);

            // Golden border
            sr.setColor(0.85f, 0.65f, 0.25f, 0.92f);
            drawBoxBorder(tipX, tipY, tipW, tipH, 1.5f);
            sr.rect(tipX, tipY + tipH - 28f, tipW, 1.2f);

            // Row backgrounds if any buffs
            if (buffCount > 0) {
                float rowY = tipY + tipH - 28f - 38f;
                for (ActiveBuffDisplay b : activeBuffs) {
                    sr.setColor(b.bgColor.r, b.bgColor.g, b.bgColor.b, 0.45f);
                    sr.rect(tipX + 8f, rowY + 3f, tipW - 16f, 32f);
                    sr.setColor(b.color.r, b.color.g, b.color.b, 0.85f);
                    sr.circle(tipX + 22f, rowY + 19f, 4f);
                    rowY -= 38f;
                }
            }

            sr.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            batch.begin();

            // Header Title
            font.getData().setScale(0.80f);
            String hTitle = (buffCount > 0) ? ("✦ HIỆU LỰC VẬT PHẨM ĐANG KÍCH HOẠT (" + buffCount + ") ✦") : "✦ HIỆU LỰC VẬT PHẨM ✦";
            layout.setText(font, hTitle);
            font.setColor(0f, 0f, 0f, 0.85f);
            font.draw(batch, hTitle, tipX + (tipW - layout.width) / 2f + 1f, tipY + tipH - 8f - 1f);
            font.setColor(1f, 0.88f, 0.35f, 1f);
            font.draw(batch, hTitle, tipX + (tipW - layout.width) / 2f, tipY + tipH - 8f);

            if (buffCount > 0) {
                float rowY = tipY + tipH - 28f - 10f;
                for (ActiveBuffDisplay b : activeBuffs) {
                    // Buff Name (Label)
                    font.getData().setScale(0.78f);
                    font.setColor(0f, 0f, 0f, 0.85f);
                    font.draw(batch, b.label, tipX + 34f + 1f, rowY - 1f);
                    font.setColor(b.color);
                    font.draw(batch, b.label, tipX + 34f, rowY);

                    // Buff Detail / Remaining charges
                    font.getData().setScale(0.68f);
                    font.setColor(0.82f, 0.88f, 0.95f, 1f);
                    font.draw(batch, b.detail, tipX + 34f, rowY - 14f);

                    rowY -= 38f;
                }
            } else {
                font.getData().setScale(0.72f);
                font.setColor(0.75f, 0.80f, 0.88f, 1f);
                font.draw(batch, "Hiện không có hiệu ứng vật phẩm nào đang kích hoạt.", tipX + 16f, tipY + tipH - 36f);
                font.setColor(1f, 0.85f, 0.45f, 0.9f);
                font.draw(batch, "Mẹo: Dùng mồi câu, bùa, đá mài... trong túi để nhận buff!", tipX + 16f, tipY + tipH - 54f);
            }
            font.getData().setScale(1f);
        }

        // 4. Gold text
        float goldW = 150f;
        float goldX = W - goldW - 170f;
        font.setColor(1f, 0.88f, 0.3f, 1f);
        font.getData().setScale(0.82f);
        font.draw(batch, String.format("%,d", player.getGold()) + " Vàng", goldX + 34f, barY + barH / 2f + 5f);
        font.getData().setScale(1f);

        // 5. Diamonds text
        float diaW = 145f;
        float diaX = W - diaW - 16f;
        font.setColor(0.65f, 0.9f, 1f, 1f);
        font.getData().setScale(0.82f);
        font.draw(batch, String.format("%,d", player.getDiamonds()) + " KC", diaX + 34f, barY + barH / 2f + 5f);
        font.getData().setScale(1f);
    }

    private void drawBobberText(SpriteBatch batch) {
        if (screen.fishingManager.state == State.WAITING || screen.fishingManager.state == State.BITE) {
            float time = screen.fishingManager.stateTimer;
            boolean isBite = (screen.fishingManager.state == State.BITE);
            float bobY = H / 2f + (isBite ? (-15f + MathUtils.sin(time * 25f) * 10f) : (MathUtils.sin(timer * 3f) * 6f));
            float bobX = W / 2f;

            if (isBite) {
                float badgeY = bobY + 80f;
                // Exclamation mark inside the comic badge
                font.getData().setScale(1.6f);
                String excl = "!";
                layout.setText(font, excl);
                // Thick shadow
                font.setColor(0f, 0f, 0f, 0.95f);
                font.draw(batch, excl, bobX - layout.width / 2f + 1.5f, badgeY + layout.height / 2f - 1.5f);
                // Luminous Yellow Core
                font.setColor(1f, 0.95f, 0.35f, 1f);
                font.draw(batch, excl, bobX - layout.width / 2f, badgeY + layout.height / 2f);
                font.getData().setScale(1f);

                // Prompt Callout Subtext below emblem
                font.getData().setScale(0.85f);
                String pullKeyName = com.fishingrpg.game.systems.KeybindManager.getInstance().getKeyDisplayName(com.fishingrpg.game.systems.KeybindManager.ACTION_PULL);
                String bitePrompt = "✦ CẮN CÂU! GIẬT [" + pullKeyName + "] NGAY! ✦";
                layout.setText(font, bitePrompt);
                float px = bobX - layout.width / 2f;
                float py = badgeY + 44f;
                // Shadow
                font.setColor(0.05f, 0.05f, 0.08f, 0.95f);
                font.draw(batch, bitePrompt, px + 1.2f, py - 1.2f);
                // Pulsing Radiant Ember
                float promptPulse = 0.85f + 0.15f * MathUtils.sin(time * 30f);
                font.setColor(1f, 0.88f * promptPulse, 0.25f, 1f);
                font.draw(batch, bitePrompt, px, py);
                font.getData().setScale(1f);
            }
        }
    }

    private void drawCatchSidebarText(SpriteBatch batch, Catchable catchable, String extra, String resLine, int diamonds) {
        if (catchable == null) return;
        float sbX = INV_X;
        float sbY = INV_Y + INV_PANEL_H + 10f;
        float sbW = INV_PANEL_W;
        float sbH = 325f;

        Color themeColor = Color.GOLD;
        String headerTitle = "CHIẾN LỢI PHẨM";
        if (catchable instanceof Fish) {
            themeColor = ((Fish) catchable).getRarityColor();
            headerTitle = "★ CHIẾN LỢI PHẨM ★";
        } else if (catchable instanceof LootItem) {
            LootItem li = (LootItem) catchable;
            if (li.getType() == CatchableType.TREASURE) {
                themeColor = new Color(1f, 0.85f, 0.25f, 1f);
                headerTitle = "✦ KHO BÁU CỔ XƯA ✦";
            } else if (li.getType() == CatchableType.TRASH) {
                themeColor = new Color(0.6f, 0.75f, 0.8f, 1f);
                headerTitle = "✦ PHẾ LIỆU HỒ ✦";
            } else {
                themeColor = new Color(0.35f, 0.95f, 0.65f, 1f);
                headerTitle = "★ VẬT PHẨM MỚI ★";
            }
        }

        // 1. Header Title
        font.setColor(themeColor);
        font.getData().setScale(0.68f);
        drawCenteredText(font, headerTitle, sbX + 4f, sbY + sbH - 26f, sbW - 28f, 26f);

        // Close '✕' button text
        font.setColor(hoverCatchClose ? Color.WHITE : new Color(0.75f, 0.8f, 0.9f, 0.8f));
        font.getData().setScale(0.70f);
        layout.setText(font, "✕");
        font.draw(batch, layout, sbX + sbW - 22f + (18f - layout.width) / 2f, sbY + sbH - 22f + (18f + layout.height) / 2f);

        // 2. Name & Category Tag
        font.setColor(themeColor);
        font.getData().setScale(0.92f);
        layout.setText(font, catchable.getName());
        if (layout.width > sbW - 16f) {
            font.getData().setScale(0.92f * ((sbW - 16f) / layout.width));
            layout.setText(font, catchable.getName());
        }
        font.draw(batch, layout, sbX + (sbW - layout.width) / 2f, sbY + sbH - 34f);

        // Subtitle category tag
        font.setColor(0.7f, 0.82f, 0.9f, 0.85f);
        font.getData().setScale(0.68f);
        String subTag;
        if (catchable instanceof Fish) {
            subTag = ((Fish) catchable).getRarity().name();
        } else if (catchable instanceof LootItem) {
            LootItem li = (LootItem) catchable;
            if (li.getType() == CatchableType.TREASURE) subTag = "Kho Báu Quý Giá";
            else if (li.getType() == CatchableType.TRASH) subTag = "Phế Liệu Thu Thập";
            else subTag = "Vật Phẩm Tiêu Hao";
        } else {
            subTag = "Thu Hoạch";
        }
        layout.setText(font, subTag);
        font.draw(batch, layout, sbX + (sbW - layout.width) / 2f, sbY + sbH - 52f);

        // 3. Inner Specs & Rewards Box Content
        float innerBoxX = sbX + 8f;
        float innerBoxY = sbY + 8f;
        float innerBoxW = sbW - 16f;

        if (catchable instanceof Fish) {
            Fish f = (Fish) catchable;

            // Line 1: Length & Weight
            font.setColor(0.85f, 0.92f, 1f, 1f);
            font.getData().setScale(0.72f);
            String spec = String.format("%.1f", f.getLengthCm()) + " cm  •  " + String.format("%.1f", f.getWeightKg()) + " kg";
            drawCenteredText(font, spec, innerBoxX, innerBoxY + 88f, innerBoxW, 20f);

            // Line 2: Record / First / Catch status
            if (extra != null && extra.contains("[KỶ LỤC]")) {
                font.setColor(1f, 0.85f, 0.2f, 1f);
                drawCenteredText(font, "★ PHÁ KỶ LỤC DÀI! ★", innerBoxX, innerBoxY + 64f, innerBoxW, 20f);
            } else if (extra != null && extra.contains("[MỚI]")) {
                font.setColor(0.35f, 1f, 0.5f, 1f);
                drawCenteredText(font, "★ LOÀI CÁ MỚI! ★", innerBoxX, innerBoxY + 64f, innerBoxW, 20f);
            } else {
                font.setColor(0.45f, 0.8f, 1f, 1f);
                drawCenteredText(font, "Thu hoạch thành công!", innerBoxX, innerBoxY + 64f, innerBoxW, 20f);
            }

            // Line 3: XP & Gold
            font.setColor(0.4f, 1f, 0.5f, 1f);
            font.getData().setScale(0.76f);
            String rLine = (resLine != null && !resLine.isEmpty()) ? resLine : ("+" + f.getXpValue() + " XP  +" + f.getGoldValue() + " V");
            drawCenteredText(font, rLine, innerBoxX, innerBoxY + 38f, innerBoxW, 20f);

            // Line 4: Diamonds bonus or tip
            if (diamonds > 0) {
                font.setColor(0.4f, 0.9f, 1f, 1f);
                font.getData().setScale(0.70f);
                drawCenteredText(font, "+ " + diamonds + " Kim Cương!", innerBoxX, innerBoxY + 12f, innerBoxW, 20f);
            } else {
                font.setColor(0.65f, 0.75f, 0.85f, 0.7f);
                font.getData().setScale(0.66f);
                drawCenteredText(font, "Tiếp tục quăng cần câu!", innerBoxX, innerBoxY + 12f, innerBoxW, 20f);
            }
        } else if (catchable instanceof LootItem) {
            LootItem loot = (LootItem) catchable;

            // Description
            font.setColor(0.88f, 0.92f, 0.98f, 1f);
            font.getData().setScale(0.70f);
            font.draw(batch, loot.getDescription(), innerBoxX + 6f, innerBoxY + 104f, innerBoxW - 12f, Align.center, true);

            // Single unified reward line (no duplicate!)
            font.setColor(1f, 0.88f, 0.25f, 1f);
            font.getData().setScale(0.76f);
            String rw = "+" + loot.getGoldValue() + " Vàng  •  +" + loot.getXpValue() + " XP";
            drawCenteredText(font, rw, innerBoxX, innerBoxY + 36f, innerBoxW, 20f);

            // Prompt
            font.setColor(0.65f, 0.75f, 0.85f, 0.7f);
            font.getData().setScale(0.66f);
            drawCenteredText(font, "Tiếp tục quăng cần câu!", innerBoxX, innerBoxY + 12f, innerBoxW, 20f);
        }

        font.getData().setScale(1f);
    }

    private void drawCombatFishAndFishingLine(Player player, Fish fish) {
        boolean isPulling = (fish.getState() == Fish.State.PULLING);
        boolean isReeling = screen.fishingManager.isReeling;
        float fishHpRatio = MathUtils.clamp(fish.getCurrentHp() / Math.max(1f, fish.getMaxHp()), 0f, 1f);
        float tRatio = MathUtils.clamp(player.getCurrentLineTension() / Math.max(1f, player.getMaxLineTension()), 0f, 1f);

        // 1. DYNAMIC DISTANCE (Kéo ra xa khi giãy mạnh, kéo lại gần khi kiệt sức và thu cước)
        // targetDist: 0.0f = rất gần cần thủ (ở sát bờ/thuyền), 1.0f = bơi xa tít ngoài khơi
        float targetDist = fishHpRatio * 0.55f + (isPulling ? 0.35f : 0.05f) + (tRatio * 0.15f);
        if (isReeling && !isPulling) targetDist -= 0.22f;
        targetDist = MathUtils.clamp(targetDist, 0.05f, 1.0f);

        // Tỷ lệ co giãn kích thước theo khoảng cách: gần thì to (1.48x), xa thì nhỏ lại (0.95x)
        float baseScale = MathUtils.lerp(1.48f, 0.95f, targetDist);

        // Tọa độ nước gốc (khi cá kéo ra xa sẽ lệch về góc trên bên trái; khi thu dây lại gần sẽ về góc dưới bên phải)
        float baseX = W / 2f - 30f - (targetDist * 85f);
        float baseY = H / 2f - 35f + (targetDist * 30f);

        // 2. VIBRANT STRUGGLING & THRASHING (Hoạt ảnh cá vùng vẫy dữ dội trên mặt nước)
        float thrashX = 0f;
        float thrashY = 0f;
        float animationTimer = timer;

        if (screen.fishingManager.troiBuocTimer > 0) {
            // Đóng băng / Trói buộc: cá run rẩy vi mô, vây đuôi ngừng quẫy
            thrashX = MathUtils.sin(timer * 40f) * 2f;
            thrashY = MathUtils.cos(timer * 40f) * 2f;
            animationTimer *= 0.1f;
        } else if (isPulling) {
            // Cá đang giằng giãy: quẫy đuôi tốc độ cực cao, lắc đầu quẫy mình chống cự
            float struggleSpeed = (screen.fishingManager.fishButTocTimer > 0) ? 35f : 20f;
            float struggleAmp = (screen.fishingManager.fishButTocTimer > 0) ? 32f : 22f;
            animationTimer *= 3.8f;
            thrashX = -Math.abs(MathUtils.sin(timer * 4.5f)) * 35f + MathUtils.sin(timer * struggleSpeed) * struggleAmp;
            thrashY = MathUtils.cos(timer * (struggleSpeed * 0.8f)) * (struggleAmp * 0.65f);
        } else {
            // Cá mệt mỏi: trôi nhẹ theo dòng nước, nhịp thu dây kéo giật từng nhịp
            animationTimer *= 1.4f;
            float reelTug = (isReeling ? MathUtils.sin(timer * 12f) * 9f : 0f);
            thrashX = MathUtils.sin(timer * 2.8f) * 14f + reelTug;
            thrashY = MathUtils.sin(timer * 2.2f) * 10f;
        }

        // 3. SPECIAL STATE OVERLAYS (Nhảy vọt trên không, Lặn sâu vực thẳm, Bứt tốc)
        float specialOffsetY = 0f;
        float specialOffsetX = 0f;

        // Nhảy vọt (Cá vọt khỏi mặt nước bay lên không trung theo đường cung parabol)
        if (screen.fishingManager.fishNhayVotTimer > 0) {
            float jumpDur = 1.8f;
            float jumpP = MathUtils.clamp(1f - (screen.fishingManager.fishNhayVotTimer / jumpDur), 0f, 1f);
            specialOffsetY += MathUtils.sin(jumpP * MathUtils.PI) * 115f;
            specialOffsetX += MathUtils.cos(jumpP * MathUtils.PI) * 35f;
            baseScale *= (1f + 0.18f * MathUtils.sin(jumpP * MathUtils.PI));
        }

        // Lặn sâu (Cá lặn chúc xuống đáy sâu)
        if (screen.fishingManager.fishLanSauTimer > 0) {
            specialOffsetY -= 42f;
            baseScale *= 0.85f;
        }

        // Bứt tốc (Phóng vút về bên trái xé nước)
        if (screen.fishingManager.fishButTocTimer > 0) {
            specialOffsetX -= 55f;
        }

        float fishX = baseX + thrashX + specialOffsetX;
        float fishY = baseY + thrashY + specialOffsetY;

        // 4. WATER RIPPLES, WAKE & FOAM (Bọt nước cuộn trào & vệt sóng rẽ nước)
        if (screen.fishingManager.troiBuocTimer > 0) {
            // Vòng băng giá phát quang
            sr.setColor(0.35f, 0.9f, 1f, 0.8f);
            sr.circle(fishX, fishY, 46f * baseScale, 6);
        } else if (screen.fishingManager.fishNhayVotTimer > 0) {
            // Bóng cá trên mặt nước khi đang trên không
            sr.setColor(0.04f, 0.08f, 0.15f, 0.45f);
            sr.ellipse(fishX, baseY, 68f * baseScale, 20f * baseScale);
            // Giọt nước vung tóe rơi rớt từ bụng cá
            sr.setColor(0.85f, 0.95f, 1f, 0.75f);
            sr.circle(fishX + MathUtils.sin(timer * 20f) * 14f, fishY - 18f, 3.5f);
            sr.circle(fishX - MathUtils.sin(timer * 26f) * 14f, fishY - 26f, 2.5f);
        } else if (isPulling) {
            // Bọt nước quẫy trắng xóa và sóng rẽ dữ dội
            float wakeSize = 36f * baseScale;
            sr.setColor(0.95f, 0.98f, 1f, 0.70f);
            sr.ellipse(fishX + 42f * baseScale, fishY, wakeSize + 12f * MathUtils.sin(timer * 18f), wakeSize * 0.5f);
            sr.ellipse(fishX - 35f * baseScale, fishY - 5f, wakeSize + 8f * MathUtils.cos(timer * 22f), wakeSize * 0.4f);
            sr.setColor(0.55f, 0.85f, 1f, 0.45f);
            sr.circle(fishX + 22f * baseScale, fishY + 8f, 22f * baseScale, 16);
            sr.circle(fishX - 42f * baseScale, fishY - 4f, 26f * baseScale, 16);
        } else {
            // Sóng nước điều hòa lan tỏa nhẹ nhàng
            sr.setColor(0.35f, 0.80f, 0.92f, 0.32f);
            float rW = (85f + 15f * MathUtils.sin(timer * 3f)) * baseScale;
            float rH = (26f + 6f * MathUtils.sin(timer * 3f)) * baseScale;
            sr.ellipse(fishX, fishY - 6f, rW, rH);
        }

        // Draw the exact fish species vector artwork with dynamic scale & animationTimer
        FishVisualRenderer.drawFish(sr, fish.getName(), fishX, fishY, baseScale, animationTimer, fish.getRarityColor());

        // Dynamic Bending Fishing Rod from bottom right
        float rodBaseX = W - 100f;
        float rodBaseY = 0f;

        float rodLength = 280f;
        float rodAngle = 138f - tRatio * 32f + (tRatio > 0.85f ? MathUtils.sin(timer * 40f) * 4f : 0f);
        float rad = rodAngle * MathUtils.degreesToRadians;
        float rodTipX = rodBaseX + MathUtils.cos(rad) * rodLength;
        float rodTipY = rodBaseY + MathUtils.sin(rad) * rodLength;

        // Rod shaft with parabolic deflection curve
        sr.setColor(0.20f, 0.16f, 0.14f, 1f);
        float px = rodBaseX, py = rodBaseY;
        for (int s = 1; s <= 7; s++) {
            float frac = (float) s / 7f;
            float bend = tRatio * 50f * MathUtils.sin(frac * MathUtils.PI);
            float nx = rodBaseX + MathUtils.cos(rad) * (rodLength * frac) - bend * 0.7f;
            float ny = rodBaseY + MathUtils.sin(rad) * (rodLength * frac) - bend;
            sr.rectLine(px, py, nx, ny, Math.max(2.5f, 9.5f - s * 1.0f));
            px = nx; py = ny;
        }

        // Rod guide rings (Silver metal line guides)
        sr.setColor(0.85f, 0.88f, 0.95f, 0.9f);
        sr.circle(rodTipX, rodTipY, 3.5f, 8);

        // Fishing Line connecting rod tip to fish's mouth
        float mouthX = fishX + 42f * baseScale;
        float mouthY = fishY;
        float lineJitter = (tRatio > 0.85f) ? MathUtils.sin(timer * 50f) * 3f : 0f;

        if (tRatio > 0.85f) {
            sr.setColor(1f, 0.25f, 0.25f, 0.95f); // Danger glowing red line
        } else if (tRatio > 0.5f) {
            sr.setColor(1f, 0.85f, 0.35f, 0.9f); // Warning amber line
        } else {
            sr.setColor(0.75f, 0.95f, 1f, 0.85f); // Crisp cyan line
        }
        sr.rectLine(rodTipX, rodTipY + lineJitter, mouthX, mouthY, tRatio > 0.85f ? 2.8f : 1.8f);

        // Splash ring where line pierces water at mouth
        sr.setColor(0.85f, 0.98f, 1f, 0.7f);
        sr.circle(mouthX, mouthY, (7f + 3f * MathUtils.sin(timer * 12f)) * baseScale, 12);
    }

    private void drawWaterBobberShapes() {
        float time = screen.fishingManager.stateTimer;
        boolean isBite = (screen.fishingManager.state == State.BITE);
        float bobY = H / 2f + (isBite ? (-15f + MathUtils.sin(time * 25f) * 10f) : (MathUtils.sin(timer * 3f) * 6f));
        float bobX = W / 2f;

        // Parabolic Flight Animation when cast is traveling from rod to water
        if (screen.fishingManager.castFlightTimer > 0) {
            float dur = FishingManager.CAST_FLIGHT_DURATION;
            float progress = MathUtils.clamp(1f - (screen.fishingManager.castFlightTimer / dur), 0f, 1f);
            float rodTipX = W - 280f;
            float rodTipY = 170f;
            float targetX = W / 2f;
            float targetY = H / 2f;
            float apex = 160f;

            bobX = rodTipX + (targetX - rodTipX) * progress;
            bobY = rodTipY + (targetY - rodTipY) * progress + apex * MathUtils.sin(progress * MathUtils.PI);

            // Trailing motion particles behind flying bobber
            for (int i = 1; i <= 4; i++) {
                float trailP = Math.max(0f, progress - i * 0.06f);
                float tx = rodTipX + (targetX - rodTipX) * trailP;
                float ty = rodTipY + (targetY - rodTipY) * trailP + apex * MathUtils.sin(trailP * MathUtils.PI);
                sr.setColor(1f, 0.85f, 0.3f, 0.4f * (1f - (float) i / 5f));
                sr.circle(tx, ty, 3.5f - i * 0.6f);
            }
        }

        // Expanding water ripple rings (only if bobber has reached water)
        if (screen.fishingManager.castFlightTimer <= 0.1f) {
            for (int r = 0; r < 3; r++) {
                float cycle = (timer * (isBite ? 1.2f : 0.4f) + r * 0.33f) % 1.0f;
                float radX = 18f + cycle * (isBite ? 75f : 45f);
                float radY = radX * 0.35f;
                sr.setColor(0.5f, 0.9f, 0.95f, (1f - cycle) * (isBite ? 0.7f : 0.3f));
                sr.ellipse(bobX - radX, bobY - 4f - radY, radX * 2f, radY * 2f);
            }
        }

        // Dynamic Shockwave Water Splash & Comic Burst when Fish Bites (!)
        if (isBite) {
            // Rapid splash shockwaves
            for (int s = 0; s < 2; s++) {
                float sc = (time * 4f + s * 0.5f) % 1f;
                sr.setColor(0.4f, 0.85f, 1f, (1f - sc) * 0.85f);
                sr.ellipse(bobX - sc * 90f, bobY - 8f - sc * 30f, sc * 180f, sc * 60f);
            }
            // Bursting droplets
            for (int d = 0; d < 6; d++) {
                float da = d * 60f * MathUtils.degreesToRadians;
                float dDist = 20f + (time * 80f) % 45f;
                sr.setColor(0.8f, 0.95f, 1f, 0.8f);
                sr.circle(bobX + MathUtils.cos(da) * dDist, bobY + MathUtils.sin(da) * dDist * 0.6f, 3.5f);
            }

            // Stylized Comic Diamond Alert Emblem behind (!)
            float badgeY = bobY + 80f;
            float pulse = 1f + 0.12f * MathUtils.sin(time * 25f);
            // Outer glow
            sr.setColor(1f, 0.25f, 0.15f, 0.35f);
            sr.circle(bobX, badgeY, 36f * pulse, 24);
            // Crimson backing diamond
            sr.setColor(0.85f, 0.12f, 0.16f, 0.95f);
            sr.triangle(bobX, badgeY + 28f * pulse, bobX - 22f * pulse, badgeY, bobX + 22f * pulse, badgeY);
            sr.triangle(bobX, badgeY - 28f * pulse, bobX - 22f * pulse, badgeY, bobX + 22f * pulse, badgeY);
            // Inner gold diamond trim
            sr.setColor(1f, 0.92f, 0.35f, 0.95f);
            sr.triangle(bobX, badgeY + 22f * pulse, bobX - 16f * pulse, badgeY, bobX + 16f * pulse, badgeY);
            sr.triangle(bobX, badgeY - 22f * pulse, bobX - 16f * pulse, badgeY, bobX + 16f * pulse, badgeY);
            // Core ruby fill
            sr.setColor(0.92f, 0.15f, 0.18f, 1f);
            sr.triangle(bobX, badgeY + 18f * pulse, bobX - 13f * pulse, badgeY, bobX + 13f * pulse, badgeY);
            sr.triangle(bobX, badgeY - 18f * pulse, bobX - 13f * pulse, badgeY, bobX + 13f * pulse, badgeY);
        }

        // Sonar Radar active rings and sweeping scanner
        if (screen.getPlayer() != null && screen.getPlayer().getSonarCharges() > 0) {
            float sonarAngle = (timer * 220f) % 360f;
            float pulse = 0.5f + 0.5f * MathUtils.sin(timer * 6f);
            sr.setColor(0.1f, 0.9f, 1f, 0.35f * pulse);
            sr.circle(bobX, bobY, 60f, 32);
            sr.setColor(0.2f, 0.7f, 1f, 0.22f);
            sr.circle(bobX, bobY, 110f, 36);
            // Sweeping radar scan line
            float sweepRad = 110f;
            float swX = bobX + sweepRad * MathUtils.cosDeg(sonarAngle);
            float swY = bobY + sweepRad * MathUtils.sinDeg(sonarAngle);
            sr.setColor(0.4f, 1f, 1f, 0.75f);
            sr.rectLine(bobX, bobY, swX, swY, 2f);
        }

        // Float lower white body
        sr.setColor(0.92f, 0.94f, 0.98f, 1f);
        sr.circle(bobX, bobY - 2f, 8f, 14);

        // Float upper red body
        sr.setColor(0.95f, 0.22f, 0.22f, 1f);
        sr.circle(bobX, bobY + 5f, 8f, 14);

        // Antenna stem
        sr.setColor(0.18f, 0.18f, 0.18f, 1f);
        sr.rectLine(bobX, bobY + 5f, bobX, bobY + 24f, 2f);

        // Glowing antenna tip
        sr.setColor(isBite ? new Color(1f, 0.2f, 0.2f, 1f) : new Color(1f, 0.92f, 0.25f, 1f));
        sr.circle(bobX, bobY + 24f, 4f, 10);

        // Fishing Rod from bottom right connecting line to bobber
        float rodBaseX = W - 100f;
        float rodBaseY = 0f;
        float rodTipX = rodBaseX - 180f;
        float rodTipY = 170f;
        sr.setColor(0.20f, 0.16f, 0.14f, 1f);
        sr.rectLine(rodBaseX, rodBaseY, rodTipX, rodTipY, 5f);
        sr.setColor(0.75f, 0.92f, 1f, 0.7f);
        sr.rectLine(rodTipX, rodTipY, bobX, bobY + 24f, 1.2f);
    }

    private float getCooldown(String skillId) {
        if (skillId == null || screen == null || screen.fishingManager == null) return 0f;
        float cd = screen.fishingManager.getAbilitySystem().getCooldown(skillId);
        if (cd > 0f) return cd;
        if (skillId.equals("keo_manh")) return screen.fishingManager.keoManhCooldown;
        if (skillId.equals("giat_kep")) return screen.fishingManager.giatKepCooldown;
        if (skillId.equals("duong_suc")) return screen.fishingManager.duongSucCooldown;
        if (skillId.equals("pha_giap")) return screen.fishingManager.phaGiapCooldown;
        if (skillId.equals("luot_day")) return screen.fishingManager.luotDayCooldown;
        if (skillId.equals("loi_nguyen_troi_buoc")) return screen.fishingManager.troiBuocCooldown;
        return 0f;
    }

    private void drawReelUpAnimationShapes() {
        Catchable catchable = screen.fishingManager.reelUpCatch;
        if (catchable == null) return;

        float timer = screen.fishingManager.reelUpTimer;
        float dur = FishingManager.REEL_UP_DURATION;
        float progress = MathUtils.clamp(1f - (timer / dur), 0f, 1f);

        float startX = screen.fishingManager.reelUpStartX;
        float startY = screen.fishingManager.reelUpStartY;

        // Leap trajectory: parabolic arc leaping high out of the water
        float targetX = 640f;
        float curX = startX + (targetX - startX) * progress;
        float apexHeight = 260f;
        float curY = startY + apexHeight * MathUtils.sin(progress * MathUtils.PI);

        // 1. Water Surface Splash Rings & Ripple at takeoff spot
        float splashAlpha = Math.max(0f, 1f - progress * 1.3f);
        if (splashAlpha > 0f) {
            float r1 = 15f + progress * 85f;
            sr.setColor(0.35f, 0.85f, 1f, splashAlpha * 0.7f);
            sr.ellipse(startX - r1, startY - r1 * 0.35f, r1 * 2f, r1 * 0.7f);

            float r2 = 5f + progress * 50f;
            sr.setColor(1f, 1f, 1f, splashAlpha * 0.85f);
            sr.ellipse(startX - r2, startY - r2 * 0.35f, r2 * 2f, r2 * 0.7f);

            // Water droplet spray particles flying outward
            for (int i = 0; i < 8; i++) {
                float angle = (i / 8f) * MathUtils.PI;
                float dist = (35f + i * 8f) * progress;
                float px = startX + MathUtils.cos(angle) * dist * 1.5f;
                float py = startY + MathUtils.sin(angle) * dist * 1.8f - 80f * progress * progress;
                sr.setColor(0.7f, 0.95f, 1f, splashAlpha);
                sr.circle(px, py, Math.max(1f, 3.5f * (1f - progress)));
            }
        }

        // 2. Dynamic Taut Fishing Line connecting from rod tip to catch
        float rodTipX = W - 280f;
        float rodTipY = 160f;
        sr.setColor(0.35f, 0.85f, 1f, 0.45f);
        sr.rectLine(rodTipX, rodTipY, curX, curY, 2.5f);
        sr.setColor(1f, 1f, 1f, 0.92f);
        sr.rectLine(rodTipX, rodTipY, curX, curY, 1.2f);

        // 3. Trailing water droplets dripping from the catch
        for (int i = 1; i <= 5; i++) {
            float trailFrac = (float) i / 6f;
            float trailProg = Math.max(0f, progress - trailFrac * 0.15f);
            float tx = startX + (targetX - startX) * trailProg + MathUtils.sin(timer * 10f + i) * 6f;
            float ty = startY + apexHeight * MathUtils.sin(trailProg * MathUtils.PI) - 12f * trailFrac;
            sr.setColor(0.5f, 0.9f, 1f, 0.6f * (1f - trailFrac));
            sr.circle(tx, ty, 3.5f - trailFrac * 2f);
        }

        // 4. Vector artwork of the leaping catch!
        Color themeColor = Color.GOLD;
        if (catchable instanceof Fish) {
            themeColor = ((Fish) catchable).getRarityColor();
        } else if (catchable instanceof LootItem) {
            LootItem li = (LootItem) catchable;
            if (li.getType() == CatchableType.TREASURE) themeColor = new Color(1f, 0.85f, 0.25f, 1f);
            else if (li.getType() == CatchableType.TRASH) themeColor = new Color(0.6f, 0.75f, 0.8f, 1f);
            else themeColor = new Color(0.35f, 0.95f, 0.65f, 1f);
        }

        // Radiance halo behind catch
        sr.setColor(themeColor.r, themeColor.g, themeColor.b, 0.30f);
        sr.circle(curX, curY, 48f + 12f * MathUtils.sin(this.timer * 15f), 24);

        if (catchable instanceof Fish) {
            Fish f = (Fish) catchable;
            FishVisualRenderer.drawFish(sr, f.getName(), curX, curY, 1.45f, this.timer, themeColor);
        } else if (catchable instanceof LootItem) {
            LootItem loot = (LootItem) catchable;
            LootVisualRenderer.drawLoot(sr, loot.getName(), loot.getType(), curX, curY, 1.35f, this.timer);
        }
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

    private String getSkillIdFromBookName(String bookName) {
        if (bookName == null) return null;
        switch (bookName) {
            case "Sách: Dưỡng Sức":   return "duong_suc";
            case "Sách: Giật Kép":    return "giat_kep";
            case "Sách: Mắt Đại Bàng":return "mat_dai_bang";
            case "Sách: Thuần Hóa":   return "thuan_hoa";
            case "Sách: Trói Buộc":   return "loi_nguyen_troi_buoc";
            case "Sách: Trái Tim ĐD": return "trai_tim_dai_duong";
            default: return null;
        }
    }

    private static class ActiveBuffDisplay {
        final String label;
        final String detail;
        final Color color;
        final Color bgColor;
        ActiveBuffDisplay(String label, String detail, Color color, Color bgColor) {
            this.label = label;
            this.detail = detail;
            this.color = color;
            this.bgColor = bgColor;
        }
    }

    private java.util.List<ActiveBuffDisplay> getActiveBuffs(Player player) {
        java.util.List<ActiveBuffDisplay> list = new java.util.ArrayList<>();
        if (player == null) return list;
        if (player.isBaitBuffActive()) {
            list.add(new ActiveBuffDisplay("Mồi Thơm", "100% Cá & Rương (0% Rác, cá cắn nhanh x3)", new Color(0.35f, 0.95f, 0.45f, 1f), new Color(0.08f, 0.22f, 0.12f, 0.95f)));
        }
        if (player.getWhetstoneCharges() > 0) {
            list.add(new ActiveBuffDisplay("Đá Mài Lưỡi Câu", "+25% Sát thương kéo cá (Còn " + player.getWhetstoneCharges() + " lượt)", new Color(0.95f, 0.75f, 0.35f, 1f), new Color(0.24f, 0.18f, 0.08f, 0.95f)));
        }
        if (player.getGoldBuffCharges() > 0) {
            list.add(new ActiveBuffDisplay("Bùa Hoàng Kim", "+50% Vàng nhận thêm (Còn " + player.getGoldBuffCharges() + " lượt)", new Color(1f, 0.88f, 0.30f, 1f), new Color(0.25f, 0.18f, 0.06f, 0.95f)));
        }
        if (player.getExpBuffCharges() > 0) {
            list.add(new ActiveBuffDisplay("Sách Thông Thái", "+50% Kinh nghiệm nhận thêm (Còn " + player.getExpBuffCharges() + " lượt)", new Color(0.40f, 0.85f, 1f, 1f), new Color(0.08f, 0.18f, 0.28f, 0.95f)));
        }
        if (player.getTreasureMagnetCharges() > 0) {
            list.add(new ActiveBuffDisplay("Nam Châm Kho Báu", "+50% Tỷ lệ câu trúng Rương (Còn " + player.getTreasureMagnetCharges() + " lượt)", new Color(0.85f, 0.55f, 1f, 1f), new Color(0.20f, 0.10f, 0.28f, 0.95f)));
        }
        if (player.getSonarCharges() > 0) {
            list.add(new ActiveBuffDisplay("Kính Nhìn Xuyên Nước", "Thấu thị radar soi trước cá cắn (Còn " + player.getSonarCharges() + " lượt)", new Color(0.35f, 0.90f, 0.90f, 1f), new Color(0.06f, 0.20f, 0.22f, 0.95f)));
        }
        if (player.isFishingNetActive()) {
            list.add(new ActiveBuffDisplay("Lưới Đánh Cá", "Thu hoạch x2 đến x4 sản lượng lượt tới", new Color(0.45f, 0.75f, 1f, 1f), new Color(0.10f, 0.18f, 0.32f, 0.95f)));
        }
        if (player.isRiskyBaitActive()) {
            list.add(new ActiveBuffDisplay("Mồi Rủi Ro", "x2 Lực kéo cá & x2 Phần thưởng lượt tới", new Color(1f, 0.40f, 0.30f, 1f), new Color(0.28f, 0.08f, 0.08f, 0.95f)));
        }
        if (player.isOverclockPotionActive()) {
            list.add(new ActiveBuffDisplay("Nước Bứt Phá", "+150% Tốc độ cuộn máy câu & -30% Max Tension", new Color(0.95f, 0.35f, 0.90f, 1f), new Color(0.26f, 0.06f, 0.24f, 0.95f)));
        }

        // --- Halloween HLW-05 Buffs Display ---
        if (player.getHalloweenCandyTimer() > 0f) {
            int mins = (int)(player.getHalloweenCandyTimer() / 60);
            int secs = (int)(player.getHalloweenCandyTimer() % 60);
            String tStr = String.format(java.util.Locale.US, "%02d:%02d", mins, secs);
            list.add(new ActiveBuffDisplay("Kẹo Trick-or-Treat", "+50% Tỷ lệ cá Halloween & cá hiếm (Còn " + tStr + ")", new Color(1f, 0.65f, 0.15f, 1f), new Color(0.25f, 0.12f, 0.04f, 0.95f)));
        }
        if (player.getPumpkinLanternCharges() > 0) {
            list.add(new ActiveBuffDisplay("Đèn Lồng Bí Ngô", "+30% Cửa sổ phản ứng Re-Hook cá nhảy (Còn " + player.getPumpkinLanternCharges() + " lần)", new Color(1f, 0.85f, 0.25f, 1f), new Color(0.24f, 0.16f, 0.05f, 0.95f)));
        }
        if (player.getFreeStaminaSkillCharges() > 0) {
            list.add(new ActiveBuffDisplay("Độc Dược Ma Quái", "Miễn phí 0 Thể lực khi tung kỹ năng (Còn " + player.getFreeStaminaSkillCharges() + " lần)", new Color(0.85f, 0.45f, 1f, 1f), new Color(0.22f, 0.08f, 0.26f, 0.95f)));
        }
        if (player.getBloodMoonTalismanTimer() > 0f) {
            String sStr = String.format(java.util.Locale.US, "%.1fs", player.getBloodMoonTalismanTimer());
            list.add(new ActiveBuffDisplay("Bùa Huyết Nguyệt", "+10% Sát thương Chuẩn từ mọi nguồn (Còn " + sStr + ")", new Color(1f, 0.35f, 0.35f, 1f), new Color(0.28f, 0.06f, 0.08f, 0.95f)));
        }
        if (player.isGhostCandleActive()) {
            list.add(new ActiveBuffDisplay("Nến Hồn Ma Bảo Hộ", "Bảo vệ đứt cước 1 lần! Hồi 50% Tension khi chạm đỉnh", new Color(0.35f, 0.95f, 0.95f, 1f), new Color(0.06f, 0.22f, 0.24f, 0.95f)));
        }
        boolean isBloodMoon = (player.getManualBloodMoonTimer() > 0f);
        if (!isBloodMoon) {
            java.time.LocalTime now = java.time.LocalTime.now();
            int hour = now.getHour();
            if (hour >= 18 || hour < 6) {
                isBloodMoon = ((now.getMinute() / 15) % 2) == 0;
            }
        }
        if (isBloodMoon) {
            list.add(new ActiveBuffDisplay("Đêm Huyết Nguyệt", "x2 Tỷ lệ cá Halloween, +20% Vàng & +20% EXP", new Color(1f, 0.25f, 0.25f, 1f), new Color(0.30f, 0.04f, 0.06f, 0.95f)));
        }
        return list;
    }
}
