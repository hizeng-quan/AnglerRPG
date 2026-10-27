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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fishingrpg.game.FishingRPG;
import com.fishingrpg.game.entities.ItemData;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.systems.AudioManager;
import com.fishingrpg.game.ui.EquipmentIconRenderer;
import com.fishingrpg.game.ui.LootVisualRenderer;
import com.fishingrpg.game.ui.NotificationUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopScreen implements Screen {

    private final FishingRPG game;
    private final Player player;
    private final Screen previousScreen;

    private ShapeRenderer sr;
    private BitmapFont font;
    private BitmapFont titleFont;
    private OrthographicCamera camera;
    private Viewport viewport;
    private GlyphLayout layout;

    private static final float W = 1280f;
    private static final float H = 720f;

    // Filter categories: 0: ALL, 1: BAIT & RESTORE, 2: SKILL BOOKS, 3: SPECIAL GEAR
    private int currentCategory = 0;
    private final String[] CATEGORY_NAMES = {"TẤT CẢ", "MỒI & HỒI PHỤC", "BÍ KÍP KỸ NĂNG", "ĐẠO CỤ ĐẶC BIỆT"};

    private int scrollOffset = 0;
    private int maxVisibleRows = 4;
    private List<ItemData> allShopItems;
    private List<ItemData> filteredItems = new ArrayList<>();

    private boolean hoverBack = false;
    private String message = "";
    private float messageTimer = 0f;

    private boolean isDraggingScroll = false;
    private float animTimer = 0f;

    // Ambient floating warm shop dust particles
    private static final int MOTE_COUNT = 25;
    private float[] moteX = new float[MOTE_COUNT];
    private float[] moteY = new float[MOTE_COUNT];
    private float[] moteSpeed = new float[MOTE_COUNT];
    private float[] moteRadius = new float[MOTE_COUNT];
    private float[] moteAlpha = new float[MOTE_COUNT];

    private final Vector3 mousePos = new Vector3();
    private static final Color BACKDROP_TOP = new Color(0.24f, 0.16f, 0.10f, 1f);
    private static final Color BACKDROP_BOT = new Color(0.14f, 0.09f, 0.06f, 1f);

    public ShopScreen(FishingRPG game, Player player, Screen previousScreen) {
        this.game = game;
        this.player = player;
        this.previousScreen = previousScreen;
        this.allShopItems = com.fishingrpg.game.data.ItemDatabase.getShopItems();

        sr = game.shapeRenderer;
        font = game.font;
        titleFont = game.titleFont != null ? game.titleFont : game.font;
        layout = new GlyphLayout();

        camera = new OrthographicCamera();
        viewport = new FitViewport(W, H, camera);
        camera.position.set(W / 2f, H / 2f, 0);

        // Initialize motes
        for (int i = 0; i < MOTE_COUNT; i++) {
            moteX[i] = MathUtils.random(0, W);
            moteY[i] = MathUtils.random(0, H);
            moteSpeed[i] = MathUtils.random(10f, 25f);
            moteRadius[i] = MathUtils.random(1.5f, 3.5f);
            moteAlpha[i] = MathUtils.random(0.2f, 0.6f);
        }

        refreshShopItemsList();
    }

    private void refreshShopItemsList() {
        this.allShopItems = new ArrayList<>(com.fishingrpg.game.data.ItemDatabase.getShopItems());
        if (player != null && player.getInventory() != null) {
            for (String ownedName : player.getInventory().keySet()) {
                if (ownedName.equals("Vàng") || player.getItemCount(ownedName) <= 0) continue;
                boolean alreadyInShop = false;
                for (ItemData it : allShopItems) {
                    if (it.name.equals(ownedName)) {
                        alreadyInShop = true;
                        break;
                    }
                }
                if (!alreadyInShop) {
                    ItemData itemData = com.fishingrpg.game.data.ItemDatabase.getItem(ownedName);
                    if (itemData != null) {
                        allShopItems.add(itemData);
                    } else {
                        allShopItems.add(new ItemData(ownedName, "Vật phẩm câu được từ biển sâu", com.fishingrpg.game.entities.ItemUsageType.BOTH, 200));
                    }
                }
            }
        }
        filterItems();
    }

    private void filterItems() {
        filteredItems.clear();
        for (ItemData item : allShopItems) {
            if (currentCategory == 0) {
                filteredItems.add(item);
            } else if (currentCategory == 1) {
                if (item.name.contains("Mồi") || item.name.contains("Nước") || item.name.contains("Bánh") || item.name.contains("Trà") || item.name.contains("Thuốc") || item.name.contains("Bình Xịt")) {
                    filteredItems.add(item);
                }
            } else if (currentCategory == 2) {
                if (item.name.startsWith("Sách: ") || item.name.contains("Sách")) {
                    filteredItems.add(item);
                }
            } else if (currentCategory == 3) {
                if (!item.name.contains("Mồi") && !item.name.contains("Nước") && !item.name.contains("Bánh") && !item.name.contains("Trà") && !item.name.contains("Thuốc") && !item.name.contains("Bình Xịt") && !item.name.startsWith("Sách: ") && !item.name.contains("Sách")) {
                    filteredItems.add(item);
                }
            }
        }
        scrollOffset = 0;
    }

    @Override
    public void show() {
        AudioManager.getInstance().playBGM("bgm_menu");
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                int totalRows = (filteredItems.size() + 2) / 3;
                int maxScroll = Math.max(0, totalRows - maxVisibleRows);
                scrollOffset += (int)Math.signum(amountY);
                if (scrollOffset < 0) scrollOffset = 0;
                if (scrollOffset > maxScroll) scrollOffset = maxScroll;
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        if (Gdx.graphics.getWidth() <= 0 || Gdx.graphics.getHeight() <= 0) return;
        animTimer += delta;
        player.update(delta);
        if (player.justFilledStamina) {
            player.justFilledStamina = false;
            showMessage("Thể lực đã hồi đầy!");
        }

        update(delta);
        if (game.getScreen() != this) return;

        // Update motes
        for (int i = 0; i < MOTE_COUNT; i++) {
            moteY[i] += moteSpeed[i] * delta;
            moteX[i] += MathUtils.sin(animTimer + i) * 6f * delta;
            if (moteY[i] > H + 10f) {
                moteY[i] = -10f;
                moteX[i] = MathUtils.random(0, W);
            }
        }

        ScreenUtils.clear(0.12f, 0.08f, 0.05f, 1f);
        camera.update();
        sr.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mousePos);
        Vector3 m = mousePos;

        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Warm Wooden Store Backdrop
        sr.rect(0, 0, W, H, BACKDROP_BOT, BACKDROP_BOT, BACKDROP_TOP, BACKDROP_TOP);

        // Wood plank divider lines
        sr.setColor(0.08f, 0.05f, 0.03f, 0.7f);
        for (float py = 0; py < H - 80; py += 65f) {
            sr.rect(0, py, W, 2f);
        }

        // Floating cozy dust motes
        for (int i = 0; i < MOTE_COUNT; i++) {
            float pulse = 0.5f + 0.5f * MathUtils.sin(animTimer * 2f + i);
            sr.setColor(1f, 0.85f, 0.45f, moteAlpha[i] * pulse);
            sr.circle(moteX[i], moteY[i], moteRadius[i]);
        }

        // Header Glass Dock
        sr.setColor(0.10f, 0.07f, 0.05f, 0.95f);
        sr.rect(0, H - 74, W, 74);
        sr.setColor(0.85f, 0.65f, 0.25f, 0.85f);
        sr.rect(0, H - 76, W, 2);

        // Back Button
        hoverBack = (m.x >= 25 && m.x <= 155 && m.y >= H - 64 && m.y <= H - 20);
        sr.setColor(hoverBack ? new Color(0.35f, 0.22f, 0.15f, 0.95f) : new Color(0.20f, 0.13f, 0.08f, 0.9f));
        drawRoundedRect(sr, 25, H - 64, 130, 44, 8);
        sr.setColor(hoverBack ? new Color(1f, 0.85f, 0.4f, 1f) : new Color(0.7f, 0.5f, 0.25f, 0.7f));
        drawRectOutline(sr, 25, H - 64, 130, 44, 1.5f);

        // Player Currency Docks (Top Right: Gold & Diamonds)
        float goldDockW = 145f;
        float goldDockH = 44f;
        float goldDockX = W - 305f;
        float goldDockY = H - 64f;

        float diaDockW = 125f;
        float diaDockH = 44f;
        float diaDockX = W - 150f;
        float diaDockY = H - 64f;

        // Gold Dock
        sr.setColor(0.18f, 0.12f, 0.08f, 0.95f);
        drawRoundedRect(sr, goldDockX, goldDockY, goldDockW, goldDockH, 8);
        sr.setColor(0.95f, 0.75f, 0.25f, 0.9f);
        drawRectOutline(sr, goldDockX, goldDockY, goldDockW, goldDockH, 1.5f);
        // Golden Coin circle icon
        sr.setColor(1f, 0.85f, 0.2f, 1f);
        sr.circle(goldDockX + 22, goldDockY + goldDockH / 2f, 11);
        sr.setColor(0.85f, 0.65f, 0.1f, 1f);
        sr.circle(goldDockX + 22, goldDockY + goldDockH / 2f, 8);

        // Diamond Dock
        sr.setColor(0.08f, 0.14f, 0.22f, 0.95f);
        drawRoundedRect(sr, diaDockX, diaDockY, diaDockW, diaDockH, 8);
        sr.setColor(0.35f, 0.75f, 1f, 0.9f);
        drawRectOutline(sr, diaDockX, diaDockY, diaDockW, diaDockH, 1.5f);
        // Diamond icon
        float dcx = diaDockX + 20f;
        float dcy = diaDockY + diaDockH / 2f;
        EquipmentIconRenderer.drawDiamondIcon(sr, dcx, dcy, 9f);

        // Category Filter Tabs
        float tabW = 260f;
        float tabH = 36f;
        float tabStartX = (W - (4 * tabW + 3 * 15f)) / 2f;
        float tabY = H - 122f;

        for (int c = 0; c < CATEGORY_NAMES.length; c++) {
            float tx = tabStartX + c * (tabW + 15f);
            boolean isCur = (currentCategory == c);
            boolean hoverTab = (m.x >= tx && m.x <= tx + tabW && m.y >= tabY && m.y <= tabY + tabH);

            sr.setColor(isCur ? new Color(0.45f, 0.28f, 0.12f, 0.95f) : (hoverTab ? new Color(0.28f, 0.18f, 0.10f, 0.9f) : new Color(0.16f, 0.10f, 0.06f, 0.85f)));
            drawRoundedRect(sr, tx, tabY, tabW, tabH, 6);
            sr.setColor(isCur ? new Color(1f, 0.82f, 0.35f, 1f) : (hoverTab ? new Color(0.7f, 0.5f, 0.25f, 0.8f) : new Color(0.4f, 0.28f, 0.16f, 0.6f)));
            drawRectOutline(sr, tx, tabY, tabW, tabH, isCur ? 2f : 1f);
        }

        // Shop items dynamic rendering
        float startX = 35f;
        float startY = 445f;
        float itemW = 385f;
        float itemH = 105f;
        float gapX = 405f;
        float gapY = 120f;

        int startIndex = scrollOffset * 3;
        int endIndex = Math.min(startIndex + maxVisibleRows * 3, filteredItems.size());

        ItemData hoveredItem = null;
        float hoverX = 0, hoverY = 0;

        Map<String, Integer> discounts = player.getShopDiscounts();

        for (int i = startIndex; i < endIndex; i++) {
            int displayIdx = i - startIndex;
            int row = displayIdx / 3;
            int col = displayIdx % 3;
            float x = startX + col * gapX;
            float y = startY - row * gapY;

            ItemData item = filteredItems.get(i);
            int discount = discounts.getOrDefault(item.name, 0);

            boolean isSoldOut = false;
            int remBooks = getRemainingSkillBooks(item);
            if (remBooks == 0) {
                isSoldOut = true;
            }

            boolean hoverCard = (m.x >= x && m.x <= x + itemW && m.y >= y && m.y <= y + itemH);
            if (hoverCard) {
                hoveredItem = item;
                hoverX = m.x;
                hoverY = m.y;
            }

            // Shelf wooden bracket under card
            sr.setColor(0.12f, 0.07f, 0.04f, 0.9f);
            sr.rect(x - 5, y - 6, itemW + 10, 6);
            sr.setColor(0.6f, 0.45f, 0.2f, 0.8f);
            sr.rect(x - 5, y, itemW + 10, 2);

            // Card Base
            sr.setColor(isSoldOut ? new Color(0.12f, 0.10f, 0.08f, 0.85f) : (hoverCard ? new Color(0.24f, 0.16f, 0.11f, 0.95f) : new Color(0.18f, 0.12f, 0.08f, 0.92f)));
            drawRoundedRect(sr, x, y, itemW, itemH, 10);
            sr.setColor(isSoldOut ? new Color(0.25f, 0.22f, 0.20f, 0.6f) : (hoverCard ? new Color(0.95f, 0.75f, 0.35f, 0.9f) : new Color(0.55f, 0.38f, 0.20f, 0.7f)));
            drawRectOutline(sr, x, y, itemW, itemH, hoverCard ? 2f : 1.2f);

            // Item Icon Frame (Left portion)
            float iconX = x + 10f;
            float iconY = y + 10f;
            float iconW = 85f;
            float iconH = 85f;
            sr.setColor(0.10f, 0.07f, 0.05f, 0.9f);
            drawRoundedRect(sr, iconX, iconY, iconW, iconH, 8);
            sr.setColor(0.45f, 0.32f, 0.18f, 0.8f);
            drawRectOutline(sr, iconX, iconY, iconW, iconH, 1f);

            // Draw Category Icon
            drawItemCategoryIcon(sr, item.name, iconX + iconW / 2f, iconY + iconH / 2f, isSoldOut);

            // Buy Button Box
            float buyBtnW = player.hasItem(item.name) ? 120f : 150f;
            float buyBtnH = 34f;
            float buyBtnX = x + itemW - buyBtnW - 12f;
            float buyBtnY = y + 12f;

            if (!isSoldOut) {
                boolean hoverBuy = (m.x >= buyBtnX && m.x <= buyBtnX + buyBtnW && m.y >= buyBtnY && m.y <= buyBtnY + buyBtnH);
                sr.setColor(hoverBuy ? new Color(0.22f, 0.55f, 0.35f, 1f) : new Color(0.15f, 0.38f, 0.24f, 0.95f));
                drawRoundedRect(sr, buyBtnX, buyBtnY, buyBtnW, buyBtnH, 6);
                sr.setColor(hoverBuy ? new Color(0.6f, 1f, 0.7f, 1f) : new Color(0.35f, 0.8f, 0.5f, 0.8f));
                drawRectOutline(sr, buyBtnX, buyBtnY, buyBtnW, buyBtnH, 1.5f);
            } else {
                sr.setColor(0.15f, 0.12f, 0.12f, 0.9f);
                drawRoundedRect(sr, buyBtnX, buyBtnY, buyBtnW, buyBtnH, 6);
                sr.setColor(0.35f, 0.3f, 0.3f, 0.6f);
                drawRectOutline(sr, buyBtnX, buyBtnY, buyBtnW, buyBtnH, 1f);
            }

            // Sell Button Box (if owned)
            if (player.hasItem(item.name)) {
                float sellBtnW = 100f;
                float sellBtnH = 34f;
                float sellBtnX = buyBtnX - sellBtnW - 8f;
                float sellBtnY = y + 12f;

                boolean hoverSell = (m.x >= sellBtnX && m.x <= sellBtnX + sellBtnW && m.y >= sellBtnY && m.y <= sellBtnY + sellBtnH);
                sr.setColor(hoverSell ? new Color(0.65f, 0.22f, 0.22f, 1f) : new Color(0.45f, 0.15f, 0.15f, 0.95f));
                drawRoundedRect(sr, sellBtnX, sellBtnY, sellBtnW, sellBtnH, 6);
                sr.setColor(hoverSell ? new Color(1f, 0.6f, 0.6f, 1f) : new Color(0.8f, 0.35f, 0.35f, 0.8f));
                drawRectOutline(sr, sellBtnX, sellBtnY, sellBtnW, sellBtnH, 1.5f);
            }

            // Flash sale discount ribbon & original strikethrough price bar
            if (discount > 0 && !isSoldOut) {
                sr.setColor(0.85f, 0.18f, 0.18f, 0.95f);
                drawRoundedRect(sr, x + 8, y + itemH - 24, 75, 20, 4);

                // Red strikethrough line over original price
                float origPx = x + itemW - 12f - 75f;
                float origPy = y + itemH - 36f;
                sr.setColor(0.95f, 0.3f, 0.3f, 0.95f);
                sr.rect(origPx, origPy + 4f, 72f, 2f);
            }
        }

        // Scrollbar
        int totalRows = (filteredItems.size() + 2) / 3;
        int maxScroll = Math.max(0, totalRows - maxVisibleRows);

        float scrollBarX = W - 22;
        float scrollBarY = 25;
        float scrollBarH = H - 170;

        if (maxScroll > 0) {
            sr.setColor(0.12f, 0.08f, 0.05f, 0.8f);
            drawRoundedRect(sr, scrollBarX, scrollBarY, 12, scrollBarH, 4);

            float thumbH = Math.max(40f, scrollBarH * ((float)maxVisibleRows / totalRows));
            float availableH = scrollBarH - thumbH;
            float scrollPercent = (float)scrollOffset / maxScroll;
            float thumbY = scrollBarY + availableH * (1f - scrollPercent);

            boolean hoverThumb = (m.x >= scrollBarX - 5 && m.x <= scrollBarX + 18 && m.y >= thumbY && m.y <= thumbY + thumbH);
            sr.setColor(hoverThumb || isDraggingScroll ? new Color(0.95f, 0.75f, 0.35f, 1f) : new Color(0.65f, 0.45f, 0.22f, 0.9f));
            drawRoundedRect(sr, scrollBarX, thumbY, 12, thumbH, 4);
        }

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // --- DRAW BATCH TEXTS ---
        game.batch.begin();

        // Title
        titleFont.setColor(0.95f, 0.85f, 0.45f, 1f);
        layout.setText(titleFont, "TIỆM TẠP HÓA NGƯ ÔNG");
        titleFont.draw(game.batch, layout, W / 2f - layout.width / 2f, H - 35f);

        // Back button text
        font.setColor(hoverBack ? Color.WHITE : new Color(0.9f, 0.8f, 0.65f, 0.9f));
        layout.setText(font, "← QUAY LẠI");
        font.draw(game.batch, layout, 25f + (130f - layout.width) / 2f, H - 36f);

        // Gold & Diamond Currency Texts
        font.setColor(1f, 0.9f, 0.35f, 1f);
        font.getData().setScale(0.85f);
        layout.setText(font, player.getGold() + " V");
        font.draw(game.batch, layout, goldDockX + 38f, goldDockY + (goldDockH + layout.height) / 2f);

        font.setColor(0.4f, 0.9f, 1f, 1f);
        layout.setText(font, player.getDiamonds() + " KC");
        font.draw(game.batch, layout, diaDockX + 36f, diaDockY + (diaDockH + layout.height) / 2f);
        font.getData().setScale(1f);

        // Category filter tab texts
        for (int c = 0; c < CATEGORY_NAMES.length; c++) {
            float tx = tabStartX + c * (tabW + 15f);
            boolean isCur = (currentCategory == c);
            font.setColor(isCur ? new Color(1f, 0.95f, 0.8f, 1f) : new Color(0.8f, 0.7f, 0.55f, 0.8f));
            layout.setText(font, CATEGORY_NAMES[c]);
            font.draw(game.batch, layout, tx + (tabW - layout.width) / 2f, tabY + 24f);
        }

        // Item Cards Text
        for (int i = startIndex; i < endIndex; i++) {
            int displayIdx = i - startIndex;
            int row = displayIdx / 3;
            int col = displayIdx % 3;
            float x = startX + col * gapX;
            float y = startY - row * gapY;

            ItemData item = filteredItems.get(i);
            int discount = discounts.getOrDefault(item.name, 0);
            int currentPrice = item.buyPrice * (100 - discount) / 100;

            boolean isSoldOut = false;
            int remBooks = getRemainingSkillBooks(item);
            if (remBooks == 0) {
                isSoldOut = true;
            }

            // Item Name
            font.setColor(isSoldOut ? Color.GRAY : Color.WHITE);
            float origScale = font.getData().scaleX;
            font.getData().setScale(1f);
            layout.setText(font, item.name);
            if (layout.width > itemW - 120) {
                font.getData().setScale((itemW - 120) / layout.width);
            }
            font.draw(game.batch, item.name, x + 105f, y + itemH - 18f);
            font.getData().setScale(origScale);

            // Owned count tag
            int ownedCount = player.getItemCount(item.name);
            font.setColor(0.6f, 0.8f, 0.7f, 0.85f);
            font.getData().setScale(0.85f);
            String countTag = "Đang có: " + ownedCount;
            if (remBooks >= 0) {
                countTag += (remBooks == 0) ? " [TỐI ĐA]" : " [Còn: " + remBooks + "]";
            }
            font.draw(game.batch, countTag, x + 105f, y + itemH - 42f);
            font.getData().setScale(1f);

            // Buy Button Text
            float buyBtnW = player.hasItem(item.name) ? 120f : 150f;
            float buyBtnH = 34f;
            float buyBtnX = x + itemW - buyBtnW - 12f;
            float buyBtnY = y + 12f;

            if (isSoldOut) {
                font.setColor(0.6f, 0.6f, 0.6f, 1f);
                font.getData().setScale(0.85f);
                layout.setText(font, "ĐÃ TỐI ĐA");
                font.draw(game.batch, layout, buyBtnX + (buyBtnW - layout.width) / 2f, buyBtnY + (buyBtnH + layout.height) / 2f);
            } else {
                font.setColor(Color.WHITE);
                font.getData().setScale(0.82f);
                String buyTxt = "MUA: " + currentPrice + " V";
                layout.setText(font, buyTxt);
                if (layout.width > buyBtnW - 8f) {
                    font.getData().setScale(0.82f * ((buyBtnW - 8f) / layout.width));
                    layout.setText(font, buyTxt);
                }
                font.draw(game.batch, layout, buyBtnX + (buyBtnW - layout.width) / 2f, buyBtnY + (buyBtnH + layout.height) / 2f);
            }

            // Sell Button Text (if owned)
            if (player.hasItem(item.name)) {
                float sellBtnW = 100f;
                float sellBtnH = 34f;
                float sellBtnX = buyBtnX - sellBtnW - 8f;
                float sellBtnY = y + 12f;

                font.setColor(1f, 0.88f, 0.88f, 1f);
                font.getData().setScale(0.82f);
                String sellTxt = "BÁN: +" + item.sellPrice + " V";
                layout.setText(font, sellTxt);
                if (layout.width > sellBtnW - 8f) {
                    font.getData().setScale(0.82f * ((sellBtnW - 8f) / layout.width));
                    layout.setText(font, sellTxt);
                }
                font.draw(game.batch, layout, sellBtnX + (sellBtnW - layout.width) / 2f, sellBtnY + (sellBtnH + layout.height) / 2f);
            }

            // Discount ribbon tag text and original strikethrough price
            if (discount > 0 && !isSoldOut) {
                font.setColor(Color.WHITE);
                font.getData().setScale(0.8f);
                layout.setText(font, "-" + discount + "%");
                font.draw(game.batch, layout, x + 8 + (75 - layout.width) / 2f, y + itemH - 24 + (20f + layout.height) / 2f);

                // Original crossed out price
                font.setColor(0.75f, 0.75f, 0.75f, 0.85f);
                font.getData().setScale(0.82f);
                String origTxt = item.buyPrice + " V";
                layout.setText(font, origTxt);
                float origPx = x + itemW - 12f - 75f;
                float origPy = y + itemH - 36f;
                font.draw(game.batch, origTxt, origPx + (72f - layout.width) / 2f, origPy + 9f);
            }
            font.getData().setScale(1f);
        }

        game.batch.end();

        // Tooltip
        if (hoveredItem != null && !isDraggingScroll) {
            drawTooltip(hoveredItem, hoverX, hoverY);
        }

        // Notification Toast
        NotificationUI.drawNotification(sr, game.batch, font, message, messageTimer, 2f, W, H);
    }

    private void drawItemCategoryIcon(ShapeRenderer sr, String itemName, float cx, float cy, boolean isSoldOut) {
        LootVisualRenderer.drawLoot(sr, itemName, com.fishingrpg.game.loot.CatchableType.CONSUMABLE, cx, cy, 0.85f, animTimer);
        if (isSoldOut) {
            sr.setColor(0.08f, 0.08f, 0.08f, 0.65f);
            sr.circle(cx, cy, 34f);
        }
    }

    private void drawTooltip(ItemData item, float mX, float mY) {
        float tipW = 320f;
        float tipH = 135f;
        float tipX = mX + 15f;
        float tipY = mY - tipH - 15f;

        if (tipX + tipW > W) tipX = mX - tipW - 15f;
        if (tipY < 0) tipY = mY + 15f;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(0.08f, 0.06f, 0.04f, 0.98f);
        drawRoundedRect(sr, tipX, tipY, tipW, tipH, 8);
        sr.setColor(0.85f, 0.65f, 0.25f, 0.9f);
        drawRectOutline(sr, tipX, tipY, tipW, tipH, 1.5f);
        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();

        font.setColor(1f, 0.85f, 0.35f, 1f);
        float origScaleX = font.getData().scaleX;
        float origScaleY = font.getData().scaleY;
        font.getData().setScale(1f);
        layout.setText(font, item.name);
        font.draw(game.batch, item.name, tipX + 14, tipY + tipH - 15);

        // Description
        font.setColor(0.85f, 0.85f, 0.85f, 1f);
        font.getData().setScale(0.85f);
        font.draw(game.batch, item.description, tipX + 14, tipY + tipH - 42, tipW - 28, Align.left, true);

        // Usage tag
        String usageText = (item.usageType == com.fishingrpg.game.entities.ItemUsageType.IN_COMBAT) ? "• Chỉ dùng lúc giằng co" : (item.usageType == com.fishingrpg.game.entities.ItemUsageType.PRE_COMBAT) ? "• Dùng trước khi quăng cần" : "• Dùng bất kỳ lúc nào";
        font.getData().setScale(0.8f);
        font.setColor(0.4f, 0.9f, 0.6f, 1f);
        font.draw(game.batch, usageText, tipX + 14, tipY + 25);

        font.getData().setScale(origScaleX, origScaleY);
        game.batch.end();
    }

    private void update(float delta) {
        if (messageTimer > 0) messageTimer -= delta;

        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mousePos);
        Vector3 m = mousePos;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && hoverBack)) {
            AudioManager.getInstance().playSFX("sfx_button");
            game.setScreen(previousScreen);
            return;
        }

        // Category Filter Tabs Click
        float tabW = 260f;
        float tabH = 36f;
        float tabStartX = (W - (4 * tabW + 3 * 15f)) / 2f;
        float tabY = H - 122f;

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            for (int c = 0; c < CATEGORY_NAMES.length; c++) {
                float tx = tabStartX + c * (tabW + 15f);
                if (m.x >= tx && m.x <= tx + tabW && m.y >= tabY && m.y <= tabY + tabH) {
                    if (currentCategory != c) {
                        AudioManager.getInstance().playSFX("sfx_button");
                        currentCategory = c;
                        filterItems();
                        return;
                    }
                }
            }
        }

        int totalRows = (filteredItems.size() + 2) / 3;
        int maxScroll = Math.max(0, totalRows - maxVisibleRows);

        // Drag scrollbar logic
        if (maxScroll > 0) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                if (!isDraggingScroll && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    float scrollBarX = W - 25;
                    if (m.x >= scrollBarX - 10 && m.x <= scrollBarX + 20 && m.y >= 25 && m.y <= H - 145) {
                        isDraggingScroll = true;
                    }
                }

                if (isDraggingScroll) {
                    float scrollBarY = 25;
                    float scrollBarH = H - 170;
                    float thumbH = Math.max(40f, scrollBarH * ((float)maxVisibleRows / totalRows));
                    float availableH = scrollBarH - thumbH;

                    float relativeY = m.y - scrollBarY - thumbH / 2f;
                    float percent = 1f - (relativeY / availableH);
                    percent = Math.max(0f, Math.min(1f, percent));

                    scrollOffset = Math.round(percent * maxScroll);
                }
            } else {
                isDraggingScroll = false;
            }
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && !isDraggingScroll) {
            float startX = 35f;
            float startY = 445f;
            float itemW = 385f;
            float itemH = 105f;
            float gapX = 405f;
            float gapY = 120f;

            int startIndex = scrollOffset * 3;
            int endIndex = Math.min(startIndex + maxVisibleRows * 3, filteredItems.size());

            Map<String, Integer> discounts = player.getShopDiscounts();

            for (int i = startIndex; i < endIndex; i++) {
                int displayIdx = i - startIndex;
                int row = displayIdx / 3;
                int col = displayIdx % 3;
                float x = startX + col * gapX;
                float y = startY - row * gapY;

                ItemData item = filteredItems.get(i);
                int discount = discounts.getOrDefault(item.name, 0);
                int currentPrice = item.buyPrice * (100 - discount) / 100;

                boolean isSoldOut = false;
                String skillId = "";
                if (item.name.startsWith("Sách: ")) {
                    skillId = com.fishingrpg.game.entities.PlayerEconomy.getSkillIdFromBook(item.name);
                    int rem = getRemainingSkillBooks(item);
                    if (rem == 0) isSoldOut = true;
                }

                float buyBtnW = player.hasItem(item.name) ? 120f : 150f;
                float buyBtnH = 34f;
                float buyBtnX = x + itemW - buyBtnW - 12f;
                float buyBtnY = y + 12f;

                boolean clickBuy = (m.x >= buyBtnX && m.x <= buyBtnX + buyBtnW && m.y >= buyBtnY && m.y <= buyBtnY + buyBtnH);
                if (clickBuy && !isSoldOut) {
                    if (player.consumeGold(currentPrice)) {
                        AudioManager.getInstance().playSFX("sfx_button");
                        if (!skillId.isEmpty()) {
                            com.fishingrpg.game.skills.Skill s = player.getSkillTree().getSkill(skillId);
                            if (s != null && s.getLevel() == 0) {
                                s.upgrade();
                                player.applySkillBonuses();
                                showMessage("Đã học thành công bí kíp " + item.name.substring(6) + " (Cấp 1)!");
                            } else {
                                player.addItem(item.name, 1);
                                showMessage("Đã mua " + item.name + "! Dùng tại Phím tắt để Đột phá hoặc Quy đổi SP.");
                            }
                        } else {
                            player.addItem(item.name, 1);
                            showMessage("Đã mua thành công " + item.name + " (-" + currentPrice + " Vàng)!");
                        }
                    } else {
                        showMessage("Bạn không đủ Vàng để mua!");
                    }
                    return;
                }

                if (player.hasItem(item.name)) {
                    float sellBtnW = 100f;
                    float sellBtnH = 34f;
                    float sellBtnX = buyBtnX - sellBtnW - 8f;
                    float sellBtnY = y + 12f;

                    boolean clickSell = (m.x >= sellBtnX && m.x <= sellBtnX + sellBtnW && m.y >= sellBtnY && m.y <= sellBtnY + sellBtnH);
                    if (clickSell) {
                        AudioManager.getInstance().playSFX("sfx_button");
                        player.consumeItem(item.name);
                        player.gainGold(item.sellPrice);
                        showMessage("Đã bán " + item.name + " (+" + item.sellPrice + " Vàng)!");
                        if (player.getItemCount(item.name) <= 0) {
                            refreshShopItemsList();
                        }
                        return;
                    }
                }
            }
        }
    }

    private int getRemainingSkillBooks(ItemData item) {
        if (item == null || !item.name.startsWith("Sách: ")) return -1;
        String skillId = com.fishingrpg.game.entities.PlayerEconomy.getSkillIdFromBook(item.name);
        if (skillId.isEmpty() || player.getSkillTree() == null) return -1;
        com.fishingrpg.game.skills.Skill s = player.getSkillTree().getSkill(skillId);
        if (s == null) return -1;
        int inBag = player.getItemCount(item.name);
        return Math.max(0, s.maxLevel - s.getLevel() - inBag);
    }

    private void showMessage(String msg) {
        this.message = msg;
        this.messageTimer = 2f;
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

    @Override public void resize(int w, int h) {
        if (w <= 0 || h <= 0) return;
        viewport.update(w, h, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
    }
}

