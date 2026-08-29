package com.fishingrpg.game.screens;
import com.fishingrpg.game.systems.AudioManager;

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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fishingrpg.game.FishingRPG;
import com.fishingrpg.game.entities.Player;
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

    private int scrollOffset = 0;
    private int maxVisibleRows = 5;
    private java.util.List<com.fishingrpg.game.entities.ItemData> shopItems;

    private boolean hoverBack = false;
    private String message = "";
    private float messageTimer = 0f;

    private boolean isDraggingScroll = false;

    public ShopScreen(FishingRPG game, Player player, Screen previousScreen) {
        this.game = game;
        this.player = player;
        this.previousScreen = previousScreen;
        this.shopItems = com.fishingrpg.game.data.ItemDatabase.getShopItems();

        sr = new ShapeRenderer();
        font = game.font;
        titleFont = game.titleFont;
        layout = new GlyphLayout();

        camera = new OrthographicCamera();
        viewport = new FitViewport(W, H, camera);
        camera.update();
    }

    @Override
    public void show() {
        AudioManager.getInstance().playBGM("bgm_menu");
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                int totalRows = (shopItems.size() + 2) / 3;
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
        player.update(delta);
        if (player.justFilledStamina) {
            player.justFilledStamina = false;
            showMessage("Thể lực đã hồi đầy!");
        }

        update(delta);
        if (game.getScreen() != this) return;

        ScreenUtils.clear(new Color(0.2f, 0.15f, 0.1f, 1f));
        camera.update();
        sr.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Vector3 m = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(m);

        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Wooden wall background
        sr.setColor(new Color(0.25f, 0.16f, 0.11f, 1f));
        sr.rect(0, 0, W, H - 80);
        
        // Wooden shelves lines and decorations
        float startX = 35f;
        float startY = 520f;
        float gapY = 100f;
        
        for (int r = 0; r < maxVisibleRows; r++) {
            float shelfY = startY - r * gapY - 15;
            
            // Draw specific shop items as decorations
            if (r == 0) {
                // Sách Thông Thái, Sách: Dưỡng Sức (Books)
                // Book 1 (Red)
                sr.setColor(new Color(0.7f, 0.2f, 0.2f, 1f));
                sr.rect(150, shelfY + 15, 20, 35);
                sr.setColor(new Color(0.9f, 0.9f, 0.8f, 1f));
                sr.rect(152, shelfY + 15, 16, 33);
                // Book 2 (Blue)
                sr.setColor(new Color(0.2f, 0.3f, 0.7f, 1f));
                sr.rect(172, shelfY + 15, 25, 40);
                sr.setColor(new Color(0.9f, 0.9f, 0.8f, 1f));
                sr.rect(174, shelfY + 15, 21, 38);
                // Book 3 (Brown - Leaning)
                sr.setColor(new Color(0.5f, 0.3f, 0.2f, 1f));
                sr.rectLine(205, shelfY + 15, 220, shelfY + 45, 18);
            } else if (r == 1) {
                // Nước Tăng Lực & Nước Bứt Phá (Potions)
                // Blue Potion
                sr.setColor(new Color(0.2f, 0.6f, 0.9f, 0.8f));
                sr.rect(350, shelfY + 15, 15, 25);
                sr.rect(355, shelfY + 40, 5, 10);
                // Red Potion
                sr.setColor(new Color(0.9f, 0.2f, 0.2f, 0.8f));
                sr.rect(380, shelfY + 15, 15, 25);
                sr.rect(385, shelfY + 40, 5, 10);
                
                // Mồi Thơm / Hộp mồi câu (Bait box)
                sr.setColor(new Color(0.4f, 0.6f, 0.4f, 1f));
                sr.rect(700, shelfY + 15, 40, 20);
                sr.setColor(new Color(0.3f, 0.5f, 0.3f, 1f));
                sr.rect(700, shelfY + 35, 40, 5);
            } else if (r == 2) {
                // Mũ câu (Fishing hat)
                sr.setColor(new Color(0.7f, 0.6f, 0.4f, 1f));
                sr.rect(500, shelfY + 15, 60, 10); // brim
                sr.rect(515, shelfY + 25, 30, 20); // crown
                sr.setColor(new Color(0.3f, 0.2f, 0.1f, 1f));
                sr.rect(515, shelfY + 25, 30, 5); // band
                
                // Lưới Đánh Cá (Fishing Net - Folded)
                sr.setColor(new Color(0.8f, 0.8f, 0.8f, 0.6f));
                for(int i=0; i<4; i++) {
                    sr.rectLine(800 + i*10, shelfY + 15, 830 - i*5, shelfY + 40, 2);
                    sr.rectLine(800, shelfY + 15 + i*8, 840, shelfY + 20 + i*5, 2);
                }
            } else if (r == 3) {
                // Móc câu (Hooks) - hanging from the shelf above
                sr.setColor(new Color(0.8f, 0.8f, 0.9f, 1f));
                // Hook 1
                sr.rect(250, shelfY + gapY - 20, 2, 20);
                sr.rect(250, shelfY + gapY - 20, 8, 2);
                sr.rect(258, shelfY + gapY - 20, 2, 8);
                // Hook 2
                sr.rect(280, shelfY + gapY - 25, 2, 25);
                sr.rect(280, shelfY + gapY - 25, 10, 2);
                sr.rect(290, shelfY + gapY - 25, 2, 10);
                
                // Bánh Mì (Bread)
                sr.setColor(new Color(0.8f, 0.6f, 0.3f, 1f));
                sr.rect(650, shelfY + 15, 35, 15);
                sr.setColor(new Color(0.9f, 0.7f, 0.4f, 1f));
                sr.rect(655, shelfY + 17, 25, 10);
            } else if (r == 4) {
                // Cần câu (Fishing rod) leaning on the shelf
                sr.setColor(new Color(0.2f, 0.2f, 0.2f, 1f));
                sr.rectLine(100, shelfY + 15, 250, shelfY + 350, 6); // Rod body
                sr.setColor(new Color(0.6f, 0.6f, 0.6f, 1f));
                sr.rectLine(120, shelfY + 50, 130, shelfY + 55, 10); // Reel
                sr.rectLine(100, shelfY + 15, 115, shelfY + 45, 8); // Handle
                
                // Bùa Hoàng Kim (Golden Amulet)
                sr.setColor(new Color(0.9f, 0.8f, 0.2f, 1f));
                sr.rect(900, shelfY + 15, 20, 30);
                sr.setColor(new Color(0.8f, 0.2f, 0.2f, 1f));
                sr.rect(905, shelfY + 20, 10, 10); // red gem inside
            }
            
            // The shelf board
            sr.setColor(new Color(0.18f, 0.1f, 0.05f, 1f));
            sr.rect(0, shelfY, W, 15);
            // Shadow under shelf
            sr.setColor(new Color(0.1f, 0.05f, 0.02f, 0.5f));
            sr.rect(0, shelfY - 5, W, 5);
        }

        // Draw Header
        sr.setColor(0.15f, 0.2f, 0.25f, 1f);
        sr.rect(0, H - 80, W, 80);
        sr.setColor(1f, 0.8f, 0.2f, 1f);
        sr.rect(0, H - 82, W, 2);

        // Back Button
        hoverBack = (m.x >= 20 && m.x <= 120 && m.y >= H - 60 && m.y <= H - 20);
        sr.setColor(hoverBack ? new Color(0.4f, 0.3f, 0.3f, 1f) : new Color(0.3f, 0.2f, 0.2f, 1f));
        sr.rect(20, H - 60, 100, 40);

        // Shop items dynamic rendering
        float itemW = 380f;
        float itemH = 90f;
        float gapX = 410f;
        
        int startIndex = scrollOffset * 3;
        int endIndex = Math.min(startIndex + maxVisibleRows * 3, shopItems.size());
        
        com.fishingrpg.game.entities.ItemData hoveredItem = null;
        float hoverX = 0, hoverY = 0;

        Map<String, Integer> discounts = player.getShopDiscounts();

        for (int i = startIndex; i < endIndex; i++) {
            int displayIdx = i - startIndex;
            int row = displayIdx / 3;
            int col = displayIdx % 3;
            float x = startX + col * gapX;
            float y = startY - row * gapY;
            
            com.fishingrpg.game.entities.ItemData item = shopItems.get(i);
            int discount = discounts.getOrDefault(item.name, 0);
            
            boolean isSoldOut = false;
            if (item.name.startsWith("Sách: ")) {
                String skillId = "";
                if (item.name.equals("Sách: Dưỡng Sức")) skillId = "duong_suc";
                else if (item.name.equals("Sách: Giật Kép")) skillId = "giat_kep";
                else if (item.name.equals("Sách: Mắt Đại Bàng")) skillId = "mat_dai_bang";
                
                com.fishingrpg.game.skills.Skill s = player.getSkillTree().getSkill(skillId);
                if (s != null && s.getLevel() > 0) isSoldOut = true;
            }

            boolean hoverBox = (m.x >= x && m.x <= x + itemW - 80 && m.y >= y && m.y <= y + itemH);
            if (hoverBox) {
                hoveredItem = item;
                hoverX = m.x;
                hoverY = m.y;
            }

            if (!isSoldOut) {
                boolean hoverBuy = (m.x >= x && m.x <= x + itemW - 80 && m.y >= y && m.y <= y + itemH);
                sr.setColor(hoverBuy ? new Color(0.25f, 0.35f, 0.45f, 1f) : new Color(0.15f, 0.25f, 0.35f, 1f));
                sr.rect(x, y, itemW - 80, itemH);
                sr.setColor(0.35f, 0.45f, 0.55f, 1f);
                sr.rect(x, y, itemW - 80, 2);
            } else {
                sr.setColor(new Color(0.15f, 0.15f, 0.15f, 0.8f));
                sr.rect(x, y, itemW - 80, itemH);
                sr.setColor(new Color(0.25f, 0.25f, 0.25f, 1f));
                sr.rect(x, y, itemW - 80, 2);
            }
            
            if (discount > 0) {
                // Red discount tag
                sr.setColor(0.8f, 0.1f, 0.1f, 1f);
                sr.rect(x - 5, y + itemH - 25, 60, 30);
            }
            
            if (player.hasItem(item.name)) {
                boolean hoverSell = (m.x >= x + itemW - 70 && m.x <= x + itemW && m.y >= y && m.y <= y + itemH);
                sr.setColor(hoverSell ? new Color(0.6f, 0.2f, 0.2f, 1f) : new Color(0.5f, 0.15f, 0.15f, 1f));
                sr.rect(x + itemW - 70, y, 70, itemH);
                sr.setColor(0.7f, 0.3f, 0.3f, 1f);
                sr.rect(x + itemW - 70, y, 70, 2);
            }
        }
        
        // Draw Scrollbar
        int totalRows = (shopItems.size() + 2) / 3;
        int maxScroll = Math.max(0, totalRows - maxVisibleRows);
        
        float scrollBarX = W - 25;
        float scrollBarY = 30;
        float scrollBarH = 590; 
        
        if (maxScroll > 0) {
            // Track
            sr.setColor(0.1f, 0.15f, 0.2f, 1f);
            sr.rect(scrollBarX, scrollBarY, 15, scrollBarH);
            
            // Thumb
            float thumbH = Math.max(40f, scrollBarH * ((float)maxVisibleRows / totalRows));
            float availableScrollHeight = scrollBarH - thumbH;
            float scrollPercent = (float)scrollOffset / maxScroll;
            float thumbY = scrollBarY + availableScrollHeight * (1f - scrollPercent);
            
            boolean hoverThumb = (m.x >= scrollBarX - 5 && m.x <= scrollBarX + 20 && m.y >= thumbY && m.y <= thumbY + thumbH);
            sr.setColor(hoverThumb || isDraggingScroll ? new Color(0.5f, 0.6f, 0.7f, 1f) : new Color(0.3f, 0.4f, 0.5f, 1f));
            sr.rect(scrollBarX, thumbY, 15, thumbH);
        }

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();
        titleFont.setColor(Color.WHITE);
        titleFont.draw(game.batch, "CỬA HÀNG", W / 2f, H - 30, 0, Align.center, false);
        font.setColor(Color.WHITE);
        layout.setText(font, "QUAY LẠI");
        font.draw(game.batch, "QUAY LẠI", 20f + (100f - layout.width)/2f + 3f, H - 40f + layout.height/2f);
        font.setColor(1f, 0.9f, 0.2f, 1f);
        font.draw(game.batch, "VÀNG HIỆN CÓ: " + player.getGold() + "g", W - 300, H - 35, 280, Align.right, false);

        for (int i = startIndex; i < endIndex; i++) {
            int displayIdx = i - startIndex;
            int row = displayIdx / 3;
            int col = displayIdx % 3;
            float x = startX + col * gapX;
            float y = startY - row * gapY;
            
            com.fishingrpg.game.entities.ItemData item = shopItems.get(i);
            int discount = discounts.getOrDefault(item.name, 0);
            int currentPrice = item.buyPrice * (100 - discount) / 100;
            
            boolean isSoldOut = false;
            String skillId = "";
            if (item.name.startsWith("Sách: ")) {
                if (item.name.equals("Sách: Dưỡng Sức")) skillId = "duong_suc";
                else if (item.name.equals("Sách: Giật Kép")) skillId = "giat_kep";
                else if (item.name.equals("Sách: Mắt Đại Bàng")) skillId = "mat_dai_bang";
                com.fishingrpg.game.skills.Skill s = player.getSkillTree().getSkill(skillId);
                if (s != null && s.getLevel() > 0) isSoldOut = true;
            }

            font.setColor(isSoldOut ? Color.GRAY : Color.WHITE);
            // Scale name if too long
            float origScale = font.getData().scaleX;
            font.getData().setScale(1f);
            layout.setText(font, item.name);
            if (layout.width > itemW - 140) {
                font.getData().setScale((itemW - 140) / layout.width);
            }
            font.draw(game.batch, item.name, x + 10, y + 65);
            font.getData().setScale(origScale);
            
            if (isSoldOut) {
                font.setColor(0.5f, 0.5f, 0.5f, 1f);
                layout.setText(font, "ĐÃ BÁN HẾT");
                font.draw(game.batch, "ĐÃ BÁN HẾT", x + itemW - 130f - layout.width / 2f, y + itemH / 2f + layout.height / 2f);
            } else {
                if (discount > 0) {
                    font.setColor(Color.WHITE);
                    font.getData().setScale(0.85f);
                    font.draw(game.batch, "-" + discount + "%", x, y + itemH - 3);
                    font.getData().setScale(origScale);
                    
                    font.setColor(0.6f, 0.6f, 0.6f, 1f);
                    font.draw(game.batch, item.buyPrice + "g", x + itemW - 140, y + 75);
                    font.setColor(1f, 0.3f, 0.3f, 1f); // red highlighted price
                    font.draw(game.batch, currentPrice + "g", x + itemW - 140, y + 45);
                } else {
                    font.setColor(1f, 0.8f, 0.2f, 1f);
                    font.draw(game.batch, currentPrice + "g", x + itemW - 140, y + 65);
                }
            }
            
            if (player.hasItem(item.name)) {
                font.setColor(1f, 0.8f, 0.8f, 1f);
                font.draw(game.batch, "BÁN", x + itemW - 55, y + 65);
                font.setColor(0.8f, 1f, 0.5f, 1f);
                font.draw(game.batch, "+" + item.sellPrice + "g", x + itemW - 65, y + 35);
            }
        }
        
        game.batch.end();

        // Draw hover tooltip on top
        if (hoveredItem != null && !isDraggingScroll) {
            drawTooltip(hoveredItem, hoverX, hoverY);
        }
        
        // Success Message rendering (Toast style)
        if (messageTimer > 0) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            com.fishingrpg.game.ui.NotificationUI.drawNotification(sr, game.batch, font, message, messageTimer, 2f, W, H);
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    private void drawTooltip(com.fishingrpg.game.entities.ItemData item, float mX, float mY) {
        float tipW = 320f;
        float tipH = 120f;
        float tipX = mX + 15f;
        float tipY = mY - tipH - 15f;
        
        if (tipX + tipW > W) tipX = mX - tipW - 15f;
        if (tipY < 0) tipY = mY + 15f;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(0.1f, 0.1f, 0.15f, 0.95f);
        sr.rect(tipX, tipY, tipW, tipH);
        sr.setColor(0.4f, 0.5f, 0.6f, 1f);
        sr.rect(tipX, tipY, tipW, 2);
        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();
        
        font.setColor(1f, 0.9f, 0.2f, 1f);
        float origScaleX = font.getData().scaleX;
        float origScaleY = font.getData().scaleY;
        font.getData().setScale(1f);
        layout.setText(font, item.name);
        if (layout.width > tipW - 20) {
            font.getData().setScale((tipW - 20) / layout.width);
        }
        font.draw(game.batch, item.name, tipX + 10, tipY + tipH - 15);
        font.getData().setScale(origScaleX, origScaleY);
        
        // Description auto-scale
        font.setColor(0.8f, 0.8f, 0.8f, 1f);
        font.getData().setScale(0.85f);
        
        float descMaxWidth = tipW - 20;
        float currentScale = 0.85f;
        boolean fits = false;
        
        while (!fits && currentScale > 0.4f) {
            layout.setText(font, item.description, Color.WHITE, descMaxWidth, Align.left, true);
            if (layout.height <= tipH - 50) {
                fits = true;
            } else {
                currentScale -= 0.05f;
                font.getData().setScale(currentScale);
            }
        }
        
        font.draw(game.batch, item.description, tipX + 10, tipY + tipH - 45, descMaxWidth, Align.left, true);
        
        // Usage type
        String usageText = "";
        if (item.usageType == com.fishingrpg.game.entities.ItemUsageType.IN_COMBAT) usageText = "Chỉ dùng lúc câu";
        else if (item.usageType == com.fishingrpg.game.entities.ItemUsageType.PRE_COMBAT) usageText = "Dùng trước khi câu";
        else usageText = "Dùng bất kỳ lúc nào";
        
        font.getData().setScale(0.8f);
        font.setColor(0.5f, 0.8f, 0.5f, 1f);
        font.draw(game.batch, usageText, tipX + 10, tipY + 25);

        font.getData().setScale(origScaleX, origScaleY);
        game.batch.end();
    }

    private void update(float delta) {
        if (messageTimer > 0) messageTimer -= delta;

        Vector3 m = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(m);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && hoverBack)) {
            game.setScreen(previousScreen);
            dispose();
            return;
        }

        int totalRows = (shopItems.size() + 2) / 3;
        int maxScroll = Math.max(0, totalRows - maxVisibleRows);
        
        // Drag scrollbar logic
        if (maxScroll > 0) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                if (!isDraggingScroll && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    float scrollBarX = W - 30;
                    if (m.x >= scrollBarX - 10 && m.x <= scrollBarX + 20 && m.y >= 30 && m.y <= 620) {
                        isDraggingScroll = true;
                    }
                }
                
                if (isDraggingScroll) {
                    float scrollBarY = 30;
                    float scrollBarH = 590;
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
            float startY = 520f;
            float itemW = 380f;
            float itemH = 90f;
            float gapY = 100f;
            float gapX = 410f;
            
            int startIndex = scrollOffset * 3;
            int endIndex = Math.min(startIndex + maxVisibleRows * 3, shopItems.size());
            
            Map<String, Integer> discounts = player.getShopDiscounts();
            
            for (int i = startIndex; i < endIndex; i++) {
                int displayIdx = i - startIndex;
                int row = displayIdx / 3;
                int col = displayIdx % 3;
                float x = startX + col * gapX;
                float y = startY - row * gapY;
                
                com.fishingrpg.game.entities.ItemData item = shopItems.get(i);
                int discount = discounts.getOrDefault(item.name, 0);
                int currentPrice = item.buyPrice * (100 - discount) / 100;
                
                boolean isSoldOut = false;
                String skillId = "";
                if (item.name.startsWith("Sách: ")) {
                    if (item.name.equals("Sách: Dưỡng Sức")) skillId = "duong_suc";
                    else if (item.name.equals("Sách: Giật Kép")) skillId = "giat_kep";
                    else if (item.name.equals("Sách: Mắt Đại Bàng")) skillId = "mat_dai_bang";
                    com.fishingrpg.game.skills.Skill s = player.getSkillTree().getSkill(skillId);
                    if (s != null && s.getLevel() > 0) isSoldOut = true;
                }
                
                boolean clickBuy = (m.x >= x && m.x <= x + itemW - 80 && m.y >= y && m.y <= y + itemH);
                if (clickBuy && !isSoldOut) {
                    if (player.getGold() >= currentPrice) {
                        player.gainGold(-currentPrice);
                        player.getQuestManager().addProgress("spend", currentPrice);
                        if (!skillId.isEmpty()) {
                            com.fishingrpg.game.skills.Skill s = player.getSkillTree().getSkill(skillId);
                            if (s != null) s.upgrade();
                            showMessage("Đã học thành công " + item.name.substring(6) + "!");
                        } else {
                            player.addItem(item.name, 1);
                            showMessage("Đã mua thành công " + item.name + "!");
                        }
                    } else {
                        showMessage("Bạn không đủ Vàng để mua!");
                    }
                    return; // one click per frame
                }
                
                if (player.hasItem(item.name)) {
                    boolean clickSell = (m.x >= x + itemW - 70 && m.x <= x + itemW && m.y >= y && m.y <= y + itemH);
                    if (clickSell) {
                        player.consumeItem(item.name);
                        player.gainGold(item.sellPrice);
                        showMessage("Đã bán " + item.name + " (+" + item.sellPrice + "g)!");
                        return; // one click per frame
                    }
                }
            }
        }
    }

    private void showMessage(String msg) {
        this.message = msg;
        this.messageTimer = 2f;
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        sr.dispose();
    }
}

