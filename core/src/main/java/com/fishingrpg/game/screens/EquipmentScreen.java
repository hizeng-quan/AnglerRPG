package com.fishingrpg.game.screens;
import com.fishingrpg.game.systems.AudioManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.fishingrpg.game.FishingRPG;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.entities.equipment.Equipment;
import com.fishingrpg.game.entities.equipment.EquipmentType;
import com.fishingrpg.game.entities.equipment.Substat;

import java.util.ArrayList;
import java.util.List;

public class EquipmentScreen implements Screen {
    private final FishingRPG game;
    private final Player player;
    private final OrthographicCamera camera;
    private final ShapeRenderer sr;
    private final BitmapFont font;
    private final BitmapFont titleFont;
    private final GlyphLayout layout;

    private final float W = 1280f;
    private final float H = 800f;
    private final Screen previousScreen;

    private int scrollOffset = 0;
    
    // =========================================================================
    // Layout constants (thay cho magic numbers trong render/update)
    // =========================================================================
    private static final float LEFT_PANEL_X   = 50f;
    private static final float INV_LIST_X     = 520f;
    private static final float INV_LIST_W     = 710f;
    private static final float INV_ROW_H      = 80f;
    private static final float INV_ROW_SPACING= 90f;
    private static final float INV_START_Y    = 510f;
    private static final int   MAX_VISIBLE    = 5;
    private static final float TAB_Y          = 580f;
    private static final float TAB_W          = 120f;
    private static final float TAB_H          = 40f;
    private static final float EQUIP_BTN_X    = 1110f;
    private static final float SCROLL_X       = 1235f;
    private static final float SCROLL_Y       = 60f;
    private static final float SCROLL_H       = 450f;

    private enum Tab { ALL, ROD, LINE, HOOK }

    private Tab currentTab = Tab.ALL;
    
    private Equipment hoveredEq = null;
    private float hoverX = 0, hoverY = 0;
    // Cache list sau khi filter để tránh tạo 2 lần/frame
    private List<Equipment> cachedFilteredInventory = new ArrayList<>();

    public EquipmentScreen(FishingRPG game, Screen previousScreen) {
        this.game = game;
        this.player = game.player;
        this.previousScreen = previousScreen;
        this.camera = new OrthographicCamera(W, H);
        this.camera.position.set(W / 2, H / 2, 0);
        this.sr = new ShapeRenderer();
        this.font = game.font;
        this.titleFont = game.titleFont != null ? game.titleFont : game.font;
        this.layout = new GlyphLayout();
    }

    @Override
    public void render(float delta) {
        update(delta);
        com.badlogic.gdx.utils.ScreenUtils.clear(new Color(0.08f, 0.1f, 0.15f, 1f));
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        sr.begin(ShapeRenderer.ShapeType.Filled);


        // Armory Background (Gradients)
        sr.rect(0, 0, W, H, new Color(0.05f, 0.05f, 0.08f, 1f), new Color(0.05f, 0.05f, 0.08f, 1f), new Color(0.1f, 0.15f, 0.2f, 1f), new Color(0.1f, 0.15f, 0.2f, 1f));
        // Draw some "rack" lines
        sr.setColor(0.08f, 0.1f, 0.15f, 1f);
        for (float i = 0; i < H; i += 80f) {
            sr.rect(0, i, W, 4f);
        }
        for (float i = 0; i < W; i += 120f) {
            sr.rect(i, 0, 4f, H);
        }

        // Header Background
        sr.setColor(0.12f, 0.16f, 0.22f, 1f);
        sr.rect(0, H - 70f, W, 70f);
        sr.setColor(0.2f, 0.3f, 0.4f, 1f);
        sr.rect(0, H - 72f, W, 2f);

        // --- Left Panel Background (Equipped) ---
        sr.setColor(0.15f, 0.18f, 0.24f, 1f);
        sr.rect(30f, 40f, 440f, 580f);
        sr.setColor(0.2f, 0.25f, 0.35f, 1f); // border
        sr.rect(30f, 40f, 440f, 2f);
        sr.rect(30f, 618f, 440f, 2f);
        sr.rect(30f, 40f, 2f, 580f);
        sr.rect(468f, 40f, 2f, 580f);

        // --- Right Panel Background (Inventory) ---
        sr.setColor(0.15f, 0.18f, 0.24f, 1f);
        sr.rect(500f, 40f, 750f, 580f);
        sr.setColor(0.2f, 0.25f, 0.35f, 1f); // border
        sr.rect(500f, 40f, 750f, 2f);
        sr.rect(500f, 618f, 750f, 2f);
        sr.rect(500f, 40f, 2f, 580f);
        sr.rect(1248f, 40f, 2f, 580f);
        
        // Draw Equip Slots
        drawEquipCard(50f, 420f, player.getEquippedRod(), EquipmentType.ROD);
        drawEquipCard(50f, 260f, player.getEquippedLine(), EquipmentType.LINE);
        drawEquipCard(50f, 100f, player.getEquippedHook(), EquipmentType.HOOK);

        // Back button
        drawButton(20f, H - 55f, 100f, 40f, new Color(0.3f, 0.2f, 0.2f, 1f));
        
        // Tabs
        float tabW = 120f;
        float tabH = 40f;
        float tabY = 620f;
        drawButton(520f, tabY, tabW, tabH, currentTab == Tab.ALL ? new Color(0.3f, 0.4f, 0.6f, 1f) : new Color(0.15f, 0.2f, 0.3f, 1f));
        drawButton(650f, tabY, tabW, tabH, currentTab == Tab.ROD ? new Color(0.3f, 0.4f, 0.6f, 1f) : new Color(0.15f, 0.2f, 0.3f, 1f));
        drawButton(780f, tabY, tabW, tabH, currentTab == Tab.LINE ? new Color(0.3f, 0.4f, 0.6f, 1f) : new Color(0.15f, 0.2f, 0.3f, 1f));
        drawButton(910f, tabY, tabW, tabH, currentTab == Tab.HOOK ? new Color(0.3f, 0.4f, 0.6f, 1f) : new Color(0.15f, 0.2f, 0.3f, 1f));

        // Draw inventory list bg
        List<Equipment> filteredInv = cachedFilteredInventory; // Dùng cache từ update()
        float startY = 510f;
        int maxVisible = 5; // Reduced to 5 to make items bigger
        for (int i = 0; i < maxVisible; i++) {
            if (scrollOffset + i < filteredInv.size()) {
                Equipment eq = filteredInv.get(scrollOffset + i);
                float y = startY - i * 90f; // Increased spacing
                
                // Item bg
                sr.setColor(0.18f, 0.22f, 0.3f, 1f);
                sr.rect(520f, y, 710f, 80f);
                
                // Rarity left border
                sr.setColor(eq.rarity.color);
                sr.rect(520f, y, 8f, 80f);

                // Equip button bg
                boolean isEq = (player.getEquippedRod() != null && eq.instanceId.equals(player.getEquippedRod().instanceId)) || 
                               (player.getEquippedLine() != null && eq.instanceId.equals(player.getEquippedLine().instanceId)) || 
                               (player.getEquippedHook() != null && eq.instanceId.equals(player.getEquippedHook().instanceId));
                               
                drawButton(1110f, y + 20f, 100f, 40f, isEq ? new Color(0.4f, 0.2f, 0.2f, 1f) : new Color(0.2f, 0.5f, 0.3f, 1f));
            }
        }
        
        // Scrollbar
        if (filteredInv.size() > maxVisible) {
            sr.setColor(0.1f, 0.15f, 0.2f, 1f);
            sr.rect(1235f, 60f, 8f, 450f);
            
            float scrollRatio = (float)scrollOffset / (filteredInv.size() - maxVisible);
            float barHeight = Math.max(30f, 450f * (maxVisible / (float)filteredInv.size()));
            float barY = 60f + (450f - barHeight) * (1 - scrollRatio);
            sr.setColor(0.4f, 0.5f, 0.6f, 1f);
            sr.rect(1235f, barY, 8f, barHeight);
        }
        
        // --- Set Bonus Banner ---
        if (player.isSetBonusActive() && player.getEquippedRod() != null) {
            sr.setColor(0.4f, 0.3f, 0.1f, 1f); // Gold background
            sr.rect(40f, 50f, 420f, 40f);
            sr.setColor(1f, 0.8f, 0.2f, 1f); // Gold border
            sr.rect(40f, 50f, 420f, 2f);
            sr.rect(40f, 88f, 420f, 2f);
            sr.rect(40f, 50f, 2f, 40f);
            sr.rect(458f, 50f, 2f, 40f);
        }

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();
        
        // Titles
        titleFont.setColor(Color.WHITE);
        drawCenteredText(titleFont, "TRANG BỊ", 0, H - 65f, W, 40f);
        
        font.setColor(Color.WHITE);
        drawCenteredText(font, "QUAY LẠI", 20f, H - 55f, 100f, 40f);

        // Tab Texts
        drawCenteredText(font, "TẤT CẢ", 520f, tabY, 120f, 40f);
        drawCenteredText(font, "CẦN CÂU", 650f, tabY, 120f, 40f);
        drawCenteredText(font, "DÂY CÂU", 780f, tabY, 120f, 40f);
        drawCenteredText(font, "MÓC CÂU", 910f, tabY, 120f, 40f);

        // Left Panel Text
        font.setColor(Color.LIGHT_GRAY);
        font.draw(game.batch, "TRANG BỊ ĐANG MẶC", 50f, 590f);
        
        drawEquipText(50f, 420f, player.getEquippedRod(), EquipmentType.ROD);
        drawEquipText(50f, 260f, player.getEquippedLine(), EquipmentType.LINE);
        drawEquipText(50f, 100f, player.getEquippedHook(), EquipmentType.HOOK);

        // Inventory list text
        font.setColor(Color.WHITE);
        font.draw(game.batch, "KHO ĐỒ (" + filteredInv.size() + ")", 1110f, 645f);

        for (int i = 0; i < maxVisible; i++) {
            int idx = scrollOffset + i;
            if (idx < filteredInv.size()) {
                Equipment eq = filteredInv.get(idx);
                float y = startY - i * 90f;
                
                titleFont.setColor(eq.rarity.color);
                layout.setText(titleFont, eq.name);
                float targetWidthInv = 550f;
                if (layout.width > targetWidthInv) {
                    titleFont.getData().setScale(0.8f * (targetWidthInv / layout.width));
                } else {
                    titleFont.getData().setScale(0.8f);
                }
                titleFont.draw(game.batch, eq.name, 545f, y + 65f);
                titleFont.getData().setScale(1f);

                
                font.setColor(Color.LIGHT_GRAY);
                String stats = getEqMainStats(eq);
                font.draw(game.batch, stats, 545f, y + 30f);
                
                font.setColor(Color.WHITE);
                boolean isEq = (player.getEquippedRod() != null && eq.instanceId.equals(player.getEquippedRod().instanceId)) || 
                               (player.getEquippedLine() != null && eq.instanceId.equals(player.getEquippedLine().instanceId)) || 
                               (player.getEquippedHook() != null && eq.instanceId.equals(player.getEquippedHook().instanceId));
                drawCenteredText(font, isEq ? "Tháo" : "Mặc", 1110f, y + 20f, 100f, 40f);
            }
        }

        // Set bonus status text
        if (player.isSetBonusActive() && player.getEquippedRod() != null) {
            String desc = com.fishingrpg.game.entities.equipment.EquipmentGenerator.getSetBonusDesc(player.getEquippedRod().setName);
            font.setColor(Color.WHITE);
            drawCenteredText(font, desc, 40f, 50f, 420f, 40f);
        } else {
            font.setColor(Color.DARK_GRAY);
            drawCenteredText(font, "Set Bonus: Chưa kích hoạt", 40f, 50f, 420f, 40f);
        }

        game.batch.end();

        // Tooltip rendering over everything
        if (hoveredEq != null) {
            drawTooltip(hoveredEq, hoverX, hoverY);
        }
    }

    private void drawEquipCard(float x, float y, Equipment eq, EquipmentType type) {
        float w = 400f;
        float h = 140f;
        sr.setColor(0.1f, 0.12f, 0.16f, 1f);
        sr.rect(x, y, w, h);
        
        // Slot square
        sr.setColor(0.05f, 0.08f, 0.12f, 1f);
        sr.rect(x + 10f, y + 10f, 120f, 120f);
        
        if (eq != null) {
            sr.setColor(eq.rarity.color);
            sr.rect(x + 10f, y + 10f, 120f, 2f);
            sr.rect(x + 10f, y + 128f, 120f, 2f);
            sr.rect(x + 10f, y + 10f, 2f, 120f);
            sr.rect(x + 128f, y + 10f, 2f, 120f);
            
            drawButton(x + w - 90f, y + 10f, 80f, 40f, new Color(0.6f, 0.3f, 0.3f, 1f));
        } else {
            sr.setColor(0.3f, 0.3f, 0.3f, 1f);
            sr.rect(x + 10f, y + 10f, 120f, 2f);
            sr.rect(x + 10f, y + 128f, 120f, 2f);
            sr.rect(x + 10f, y + 10f, 2f, 120f);
            sr.rect(x + 128f, y + 10f, 2f, 120f);
        }
    }

    private void drawEquipText(float x, float y, Equipment eq, EquipmentType type) {
        float w = 400f;
        String typeName = type == EquipmentType.ROD ? "CẦN CÂU" : type == EquipmentType.LINE ? "DÂY CÂU" : "MÓC CÂU";
        
        if (eq != null) {
            font.setColor(Color.LIGHT_GRAY);
            drawCenteredText(font, typeName, x + 10f, y + 10f, 120f, 120f);
            
            titleFont.setColor(eq.rarity.color);
            layout.setText(titleFont, eq.name);
            float targetWidth = w - 160f;
            if (layout.width > targetWidth) {
                titleFont.getData().setScale(0.8f * (targetWidth / layout.width));
            }
            titleFont.draw(game.batch, eq.name, x + 145f, y + 125f);
            titleFont.getData().setScale(1f);

            
            font.setColor(Color.LIGHT_GRAY);
            String stats = getEqMainStats(eq);
            // Split stats into lines for better fitting
            String[] splitStats = stats.split(", ");
            for(int i=0; i<splitStats.length; i++){
                font.draw(game.batch, splitStats[i], x + 145f, y + 85f - i * 25f);
            }
            
            drawCenteredText(font, "Tháo", x + w - 90f, y + 10f, 80f, 40f);
        } else {
            font.setColor(Color.DARK_GRAY);
            drawCenteredText(font, typeName, x + 10f, y + 10f, 120f, 120f);
            font.setColor(Color.GRAY);
            font.draw(game.batch, "Chưa trang bị", x + 145f, y + 80f);
        }
    }


    private void update(float delta) {
        Vector3 m = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(m);
        // Cache filtered list một lần cho cả update lần render cùng frame
        cachedFilteredInventory = getFilteredInventory();

        // Handle hovering
        hoveredEq = null;
        hoverX = m.x;
        hoverY = m.y;
        
        // Hover equipped
        if (m.x >= 50f && m.x <= 450f) {
            if (m.y >= 420f && m.y <= 560f && player.getEquippedRod() != null) hoveredEq = player.getEquippedRod();
            if (m.y >= 260f && m.y <= 400f && player.getEquippedLine() != null) hoveredEq = player.getEquippedLine();
            if (m.y >= 100f && m.y <= 240f && player.getEquippedHook() != null) hoveredEq = player.getEquippedHook();
        }
        
        // Hover inventory
        List<Equipment> filtered = getFilteredInventory();
        if (m.x >= 520f && m.x <= 1100f) { 
            float startY = 510f;
            for (int i = 0; i < 5; i++) {
                if (scrollOffset + i < filtered.size()) {
                    float rowY = startY - i * 90f;
                    if (m.y >= rowY && m.y <= rowY + 80f) {
                        hoveredEq = filtered.get(scrollOffset + i);
                    }
                }
            }
        }

        // Scroll logic is now handled in show() by InputProcessor

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            // Tabs
            float tabY = 620f;
            if (m.y >= tabY && m.y <= tabY + 40f) {
                if (m.x >= 520f && m.x <= 640f) { currentTab = Tab.ALL; scrollOffset = 0; return; }
                if (m.x >= 650f && m.x <= 770f) { currentTab = Tab.ROD; scrollOffset = 0; return; }
                if (m.x >= 780f && m.x <= 900f) { currentTab = Tab.LINE; scrollOffset = 0; return; }
                if (m.x >= 910f && m.x <= 1030f) { currentTab = Tab.HOOK; scrollOffset = 0; return; }
            }

            // Back button
            if (m.x >= 20f && m.x <= 120f && m.y >= H - 55f && m.y <= H - 15f) {
                game.setScreen(previousScreen);
                return;
            }

            // Unequip buttons
            float w = 400f;
            if (m.x >= 50f + w - 90f && m.x <= 50f + w - 10f) {
                if (m.y >= 420f + 10f && m.y <= 420f + 50f && player.getEquippedRod() != null) {
                    player.unequip(EquipmentType.ROD); return;
                }
                if (m.y >= 260f + 10f && m.y <= 260f + 50f && player.getEquippedLine() != null) {
                    player.unequip(EquipmentType.LINE); return;
                }
                if (m.y >= 100f + 10f && m.y <= 100f + 50f && player.getEquippedHook() != null) {
                    player.unequip(EquipmentType.HOOK); return;
                }
            }

            // Equip/Unequip buttons
            List<Equipment> filteredInv = getFilteredInventory();
            float startY = 510f;
            int maxVisible = 5;
            for (int i = 0; i < maxVisible; i++) {
                if (scrollOffset + i < filteredInv.size()) {
                    Equipment eq = filteredInv.get(scrollOffset + i);
                    float y = startY - i * 90f;
                    if (m.x >= 1110f && m.x <= 1210f && m.y >= y + 20f && m.y <= y + 60f) {
                        boolean isEq = (player.getEquippedRod() != null && eq.instanceId.equals(player.getEquippedRod().instanceId)) || 
                                       (player.getEquippedLine() != null && eq.instanceId.equals(player.getEquippedLine().instanceId)) || 
                                       (player.getEquippedHook() != null && eq.instanceId.equals(player.getEquippedHook().instanceId));
                        if (isEq) {
                            player.unequip(eq.type);
                        } else {
                            player.equip(eq);
                        }
                        return;
                    }
                }
            }
        }
    }

    @Override public void show() {
        AudioManager.getInstance().playBGM("bgm_menu");
        Gdx.input.setInputProcessor(new com.badlogic.gdx.InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                scrollOffset += (int)amountY * 2;
                List<Equipment> filtered = getFilteredInventory();
                int maxScroll = Math.max(0, filtered.size() - 5);
                if (scrollOffset < 0) scrollOffset = 0;
                if (scrollOffset > maxScroll) scrollOffset = maxScroll;
                return true;
            }
        });
    }
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {
        Gdx.input.setInputProcessor(null);
    }
    @Override public void dispose() {
        sr.dispose();
    }

    private String getEqMainStats(Equipment eq) {
        if (eq.type == EquipmentType.ROD) {
            return String.format("Sát thương: %.1f, Thể lực: %.1f", eq.mainStat1, eq.mainStat2);
        } else if (eq.type == EquipmentType.LINE) {
            return String.format("Lực căng: %.1f, Kháng đứt: %.1f", eq.mainStat1, eq.mainStat2);
        } else {
            return String.format("Tỉ lệ xịn: %.1f%%, Giữ mồi: %.1f%%", eq.mainStat1, eq.mainStat2);
        }
    }
    
    private void drawTooltip(Equipment eq, float x, float y) {
        float tipW = 300f;
        float tipH = 220f + eq.substats.size() * 25f;
        
        // Ensure tooltip stays on screen
        if (x + tipW > W) x -= tipW + 20f;
        else x += 20f;
        if (y - tipH < 0) y = tipH + 20f;
        
        Gdx.gl.glEnable(GL20.GL_BLEND);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(0.05f, 0.05f, 0.08f, 0.95f);
        sr.rect(x, y - tipH, tipW, tipH);
        sr.setColor(eq.rarity.color);
        sr.rectLine(x, y - tipH, x + tipW, y - tipH, 2f);
        sr.rectLine(x, y, x + tipW, y, 2f);
        sr.rectLine(x, y - tipH, x, y, 2f);
        sr.rectLine(x + tipW, y - tipH, x + tipW, y, 2f);
        sr.end();
        
        game.batch.begin();
        font.setColor(eq.rarity.color);
        font.draw(game.batch, eq.name, x + 15f, y - 20f);
        font.setColor(Color.WHITE);
        font.draw(game.batch, "Lv " + eq.level + " (" + eq.rarity.name() + ")", x + 15f, y - 50f);
        font.draw(game.batch, "Set: " + eq.setName, x + 15f, y - 80f);
        
        font.setColor(Color.YELLOW);
        font.draw(game.batch, "[Chỉ số chính]", x + 15f, y - 110f);
        font.setColor(Color.WHITE);
        String[] mstats = getEqMainStats(eq).split(", ");
        if (mstats.length > 0) font.draw(game.batch, mstats[0], x + 15f, y - 135f);
        if (mstats.length > 1) font.draw(game.batch, mstats[1], x + 15f, y - 160f);
        
        font.setColor(Color.CYAN);
        font.draw(game.batch, "[Chỉ số phụ]", x + 15f, y - 190f);
        font.setColor(Color.LIGHT_GRAY);
        float sy = y - 215f;
        for (Substat sub : eq.substats) {
            String subTxt = "";
            if (sub.type == com.fishingrpg.game.entities.equipment.Substat.Type.DAMAGE_BONUS) subTxt = "Sát thương thêm: +" + String.format("%.1f", sub.value);
            else if (sub.type == com.fishingrpg.game.entities.equipment.Substat.Type.STAMINA_REGEN) subTxt = "Hồi thể lực: +" + String.format("%.1f", sub.value) + "%";
            else if (sub.type == com.fishingrpg.game.entities.equipment.Substat.Type.GOLD_BONUS) subTxt = "Vàng thêm: +" + String.format("%.1f", sub.value) + "%";
            else if (sub.type == com.fishingrpg.game.entities.equipment.Substat.Type.EXP_BONUS) subTxt = "EXP thêm: +" + String.format("%.1f", sub.value) + "%";
            else if (sub.type == com.fishingrpg.game.entities.equipment.Substat.Type.FISH_PULL_REDUCE) subTxt = "Lực kéo cá: -" + String.format("%.1f", sub.value) + "%";
            font.draw(game.batch, "- " + subTxt, x + 15f, sy);
            sy -= 25f;
        }
        
        game.batch.end();
    }

    private void drawButton(float x, float y, float w, float h, Color color) {
        sr.setColor(color);
        sr.rect(x, y, w, h);
        sr.setColor(color.cpy().mul(0.7f));
        sr.rectLine(x, y, x + w, y, 2f);
        sr.rectLine(x, y + h, x + w, y + h, 2f);
        sr.rectLine(x, y, x, y + h, 2f);
        sr.rectLine(x + w, y, x + w, y + h, 2f);
    }
    
    private void drawCenteredText(BitmapFont f, String text, float x, float y, float width, float height) {
        layout.setText(f, text);
        f.draw(game.batch, text, x + (width - layout.width) / 2f, y + (height + layout.height) / 2f);
    }
    
    private List<Equipment> getFilteredInventory() {
        List<Equipment> all = player.getEquipmentInventory();
        if (currentTab == Tab.ALL) return all;
        List<Equipment> filtered = new ArrayList<>();
        EquipmentType t = currentTab == Tab.ROD ? EquipmentType.ROD : (currentTab == Tab.LINE ? EquipmentType.LINE : EquipmentType.HOOK);
        for (Equipment eq : all) {
            if (eq.type == t) filtered.add(eq);
        }
        return filtered;
    }

}

