package com.fishingrpg.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.fishingrpg.game.PlayScreen;
import com.fishingrpg.game.systems.FishingManager.State;
import com.fishingrpg.game.entities.Fish;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.loot.Catchable;

import java.util.Map;

public class GameHUD {

    private final ShapeRenderer sr;
    private final BitmapFont font;
    private final GlyphLayout layout;
    private final PlayScreen screen;

    private static final float W = 1280f, H = 720f;
    private static final float HUD_H = 42f;
    private static final float HUD_Y = H - HUD_H;
    
    private static final float LEFT_X = 20f, PANEL_Y = 100f;
    private static final float PANEL_W = 40f, PANEL_H = 300f;
    private static final float BAR_INSET = 4f;

    private static final float HP_W = 500f, HP_H = 30f;
    private static final float MSG_BAR_H = 35f;

    private static final float BTN_W = 120f, BTN_H = 40f;
    private static final float BTN_ACT_X = W - BTN_W - 20f;
    private static final float BTN_ACT_Y = HUD_Y - BTN_H - 10f;
    
    private static final float BTN_MENU_X = 20f;
    private static final float BTN_MENU_Y = H - 60f;
    
    private static final float BTN_CAST_X = (W - 220f) / 2f;
    private static final float BTN_CAST_Y = 80f;

    private boolean hoverAct = false;
    private boolean hoverMenu = false;
    private boolean hoverCast, hoverShop, hoverCatalog, hoverSkills, hoverGacha, hoverEquipment, hoverQuest;
    private boolean hoverCang = false;
    private boolean hoverClose = false;
    private boolean[] hoverActives = new boolean[4];

    private static final float BTN_ACTIVES_X = 300f; // Starting X
    private static final float BTN_ACTIVES_Y = HUD_Y - 75f;
    private static final float BTN_CANG_X = BTN_ACTIVES_X + 4 * 75f;
    private static final float BTN_CANG_Y = BTN_ACTIVES_Y;
    
    private static final float BTN_CLOSE_X = 510f;
    private static final float BTN_CLOSE_Y = 360f;

    private float timer = 0f;
    private String hoveredInventoryItem = null;

    public GameHUD(ShapeRenderer sr, BitmapFont font, PlayScreen screen) {
        this.sr = sr;
        this.font = font;
        this.layout = new GlyphLayout();
        this.screen = screen;
    }

    public void updateHover(Vector3 m, State state) {
        hoverCast = (state == State.IDLE || state == State.PRE_CAST || state == State.RESULT) &&
                    m.x >= BTN_CAST_X && m.x <= BTN_CAST_X + 220f &&
                    m.y >= BTN_CAST_Y && m.y <= BTN_CAST_Y + 60f;

        hoverMenu = m.x >= BTN_MENU_X && m.x <= BTN_MENU_X + 90f &&
                    m.y >= BTN_MENU_Y && m.y <= BTN_MENU_Y + 35f;

        hoverQuest     = state == State.IDLE && m.x >= 230 && m.x <= 350 && m.y >= H - 85 && m.y <= H - 20;
        hoverEquipment = state == State.IDLE && m.x >= 370 && m.x <= 490 && m.y >= H - 85 && m.y <= H - 20;
        hoverSkills    = state == State.IDLE && m.x >= 510 && m.x <= 630 && m.y >= H - 85 && m.y <= H - 20;
        hoverGacha     = state == State.IDLE && m.x >= 650 && m.x <= 770 && m.y >= H - 85 && m.y <= H - 20;
        hoverShop      = state == State.IDLE && m.x >= 790 && m.x <= 910 && m.y >= H - 85 && m.y <= H - 20;
        hoverCatalog   = state == State.IDLE && m.x >= 930 && m.x <= 1050 && m.y >= H - 85 && m.y <= H - 20;
        
        for (int i=0; i<4; i++) {
            hoverActives[i] = state == State.COMBAT && m.x >= BTN_ACTIVES_X + i * 75f && m.x <= BTN_ACTIVES_X + i * 75f + 65f && m.y >= BTN_ACTIVES_Y && m.y <= BTN_ACTIVES_Y + 65f;
        }
        hoverCang = state == State.COMBAT && m.x >= BTN_CANG_X && m.x <= BTN_CANG_X + 65f && m.y >= BTN_CANG_Y && m.y <= BTN_CANG_Y + 65f;
        hoverClose = state == State.RESULT && m.x >= BTN_CLOSE_X && m.x <= BTN_CLOSE_X + 40f && m.y >= BTN_CLOSE_Y && m.y <= BTN_CLOSE_Y + 40f;
        
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
        return hoverAct;
    }

    public boolean isMenuClicked(Vector3 m, State state) {
        return hoverMenu;
    }

    public boolean isShopClicked(Vector3 m, State state) {
        return hoverShop;
    }

    public boolean isCatalogClicked(Vector3 m, State state) {
        return hoverCatalog;
    }

    public boolean isSkillsClicked(Vector3 m, State state) {
        return hoverSkills;
    }

    public boolean isQuestClicked(Vector3 m, State state) { return hoverQuest; }
    public boolean isGachaClicked(Vector3 m, State state) {
        return hoverGacha;
    }

    public boolean isEquipmentClicked(Vector3 m, State state) {
        return hoverEquipment;
    }

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

    public void drawIdleShapes(Player player) {
        timer += com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        // --- SKY (Vibrant Sunset) ---
        sr.setColor(0.35f, 0.1f, 0.35f, 1f); sr.rect(0, 500, W, 220); // Deep purple top
        sr.setColor(0.75f, 0.2f, 0.3f, 1f);  sr.rect(0, 420, W, 80);  // Red-magenta middle
        sr.setColor(0.95f, 0.45f, 0.15f, 1f); sr.rect(0, 350, W, 70); // Bright orange bottom

        // --- SUN --- (Draw before water)
        float sunY = 340f + 10f * MathUtils.sin(timer * 0.5f);
        sr.setColor(1f, 0.45f, 0.15f, 0.9f); sr.circle(W / 2f, sunY, 60f, 40);
        sr.setColor(1f, 0.85f, 0.2f, 0.5f);  sr.circle(W / 2f, sunY, 75f + 5f * MathUtils.sin(timer * 2f), 40);

        // --- WATER ---
        sr.setColor(0.08f, 0.12f, 0.22f, 1f); sr.rect(0, 0, W, 350);
        sr.setColor(0.12f, 0.16f, 0.28f, 0.8f); sr.rect(0, 0, W, 280);
        
        // --- CLOUDS ---
        sr.setColor(0.85f, 0.65f, 0.55f, 0.4f);
        for (int j=0; j<3; j++) {
            float cloudX = (timer * 15f + j * 300) % (W + 200) - 100;
            float cloudY = 440 + j * 30;
            sr.circle(cloudX, cloudY, 25f + j * 5);
            sr.circle(cloudX + 30, cloudY + 10, 35f);
            sr.circle(cloudX + 60, cloudY - 5, 20f);
        }

        sr.end();
        sr.begin(ShapeRenderer.ShapeType.Line);
        
        // --- SEAGULLS ---
        sr.setColor(0.15f, 0.1f, 0.1f, 0.6f);
        for (int j=0; j<4; j++) {
            float birdX = W - ((timer * 40f + j*200) % (W + 200)) + 100;
            float birdY = 420 + 20f * MathUtils.sin(timer * 2f + j);
            float wingY = birdY + 12f * MathUtils.sin(timer * 8f + j);
            sr.line(birdX, birdY, birdX + 10, wingY);
            sr.line(birdX + 10, wingY, birdX + 20, birdY);
        }
        
        // Draw waves
        float step = 5f;
        sr.setColor(0.12f, 0.38f, 0.60f, 0.85f);
        for (float x = 0; x < W - step; x += step) {
            float y1 = 328 + 9f * MathUtils.sin((x       + timer * 55f) * 0.022f);
            float y2 = 328 + 9f * MathUtils.sin((x + step + timer * 55f) * 0.022f);
            sr.line(x, y1, x + step, y2);
        }
        sr.setColor(0.08f, 0.28f, 0.46f, 0.65f);
        for (float x = 0; x < W - step; x += step) {
            float y1 = 305 + 6f * MathUtils.sin((x       + timer * 38f + 25) * 0.018f);
            float y2 = 305 + 6f * MathUtils.sin((x + step + timer * 38f + 25) * 0.018f);
            sr.line(x, y1, x + step, y2);
        }
        sr.setColor(0.1f, 0.3f, 0.5f, 0.7f);
        for (float x = 0; x < W - step; x += step) {
            float y1 = 280 + 12f * MathUtils.sin((x       + timer * 70f) * 0.02f);
            float y2 = 280 + 12f * MathUtils.sin((x + step + timer * 70f) * 0.02f);
            sr.line(x, y1, x + step, y2);
        }
        sr.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // --- FISHING BOAT ON RIGHT ---
        float boatY = 320f + 5f * MathUtils.sin(timer * 2.5f);
        // Hull
        sr.setColor(0.15f, 0.1f, 0.05f, 1f); 
        sr.rect(900, boatY, 300, 50);
        sr.triangle(800, boatY + 50, 900, boatY + 50, 900, boatY);
        sr.triangle(1200, boatY + 50, 1260, boatY + 50, 1200, boatY);
        // Cabin
        sr.setColor(0.7f, 0.65f, 0.6f, 1f); 
        sr.rect(1000, boatY + 50, 120, 70);
        // Cabin roof
        sr.setColor(0.2f, 0.3f, 0.4f, 1f); 
        sr.rect(990, boatY + 120, 140, 16);
        // Window
        sr.setColor(0.8f, 0.9f, 1f, 0.6f);
        sr.rect(1020, boatY + 70, 30, 30);
        // Mast
        sr.setColor(0.1f, 0.08f, 0.05f, 1f);
        sr.rect(940, boatY + 50, 10, 160);
        // Flag
        sr.setColor(0.8f, 0.2f, 0.2f, 1f);
        sr.triangle(950, boatY + 200, 950, boatY + 170, 1010, boatY + 184);

        // --- DRAW DOCK ---
        float dockYOffset = 3f * MathUtils.sin(timer * 0.8f);
        
        // Pier platform
        sr.setColor(0.25f, 0.15f, 0.1f, 1f);
        sr.rect(0, dockYOffset, 280, 280);
        // Pier planks top
        sr.setColor(0.35f, 0.22f, 0.15f, 1f);
        sr.rect(0, 270 + dockYOffset, 290, 15);
        // Pier pillars
        sr.setColor(0.2f, 0.12f, 0.08f, 1f);
        sr.rect(60, 0, 20, 270 + dockYOffset);
        sr.rect(160, 0, 20, 270 + dockYOffset);
        sr.rect(250, 0, 20, 270 + dockYOffset);
        // Ropes around pillars
        sr.setColor(0.6f, 0.5f, 0.4f, 1f);
        sr.rect(58, 240 + dockYOffset, 24, 6);
        sr.rect(158, 220 + dockYOffset, 24, 6);
        sr.rect(248, 250 + dockYOffset, 24, 6);
        // Wooden crate on dock
        sr.setColor(0.4f, 0.25f, 0.15f, 1f);
        sr.rect(100, 285 + dockYOffset, 50, 40);
        sr.setColor(0.3f, 0.18f, 0.1f, 1f);
        sr.rectLine(100, 285 + dockYOffset, 150, 325 + dockYOffset, 3f);
        sr.rectLine(100, 325 + dockYOffset, 150, 285 + dockYOffset, 3f);

        sr.setColor(hoverCast ? new Color(1f, 0.5f, 0.1f, 0.95f) : new Color(0.85f, 0.35f, 0.1f, 0.85f));
        sr.rect(BTN_CAST_X, BTN_CAST_Y, 220f, 60f);
        sr.setColor(1f, 0.85f, 0.25f, 0.6f); sr.rect(BTN_CAST_X, BTN_CAST_Y + 56f, 220f, 4f);

        // Draw new top buttons (120x50, gap 20)
        // 0: Quest
        sr.setColor(hoverQuest ? new Color(0.8f, 0.7f, 0.2f, 1f) : new Color(0.7f, 0.6f, 0.1f, 1f));
        sr.rect(230, H - 85, 120, 65);
        sr.setColor(1f, 0.9f, 0.4f, 0.6f); sr.rect(230, H - 85 + 61, 120, 4); // Highlight top edge
        
        // 1: Equipment
        sr.setColor(hoverEquipment ? new Color(0.4f, 0.6f, 0.9f, 1f) : new Color(0.3f, 0.5f, 0.8f, 1f));
        sr.rect(370, H - 85, 120, 65);
        sr.setColor(0.6f, 0.8f, 1f, 0.6f); sr.rect(370, H - 85 + 61, 120, 4);

        // 2: Skills
        sr.setColor(hoverSkills ? new Color(0.9f, 0.6f, 0.2f, 1f) : new Color(0.8f, 0.5f, 0.1f, 1f));
        sr.rect(510, H - 85, 120, 65);
        sr.setColor(1f, 0.8f, 0.4f, 0.6f); sr.rect(510, H - 85 + 61, 120, 4);

        // 3: Gacha
        sr.setColor(hoverGacha ? new Color(0.9f, 0.4f, 0.6f, 1f) : new Color(0.8f, 0.3f, 0.5f, 1f));
        sr.rect(650, H - 85, 120, 65);
        sr.setColor(1f, 0.6f, 0.8f, 0.6f); sr.rect(650, H - 85 + 61, 120, 4);

        // 4: Shop
        sr.setColor(hoverShop ? new Color(0.3f, 0.7f, 0.4f, 1f) : new Color(0.2f, 0.6f, 0.3f, 1f));
        sr.rect(790, H - 85, 120, 65);
        sr.setColor(0.5f, 0.9f, 0.6f, 0.6f); sr.rect(790, H - 85 + 61, 120, 4);

        // 5: Catalog
        sr.setColor(hoverCatalog ? new Color(0.7f, 0.4f, 0.9f, 1f) : new Color(0.6f, 0.3f, 0.8f, 1f));
        sr.rect(930, H - 85, 120, 65);
        sr.setColor(0.9f, 0.6f, 1f, 0.6f); sr.rect(930, H - 85 + 61, 120, 4);
        if (player != null) {
            // Lv Box
            sr.setColor(0.2f, 0.6f, 0.4f, 0.9f);
            sr.rect(20, 10, 80, 35);
            // Gold Box
            sr.setColor(0.8f, 0.6f, 0.2f, 0.9f);
            sr.rect(110, 10, 180, 35);
            // Diamond Box
            sr.setColor(0.6f, 0.3f, 0.8f, 0.9f);
            sr.rect(300, 10, 120, 35);
        }

        
        // Draw simple icons using primitives
        // Quest (Diamond shape)
        sr.setColor(1f, 0.9f, 0.5f, 1f);
        sr.rect(290 - 6, H - 45 - 6, 6, 6, 12, 12, 1f, 1f, 45f); // rotated rect -> diamond
        // Equipment (Sword/Shield like - just a triangle and rect)
        sr.setColor(0.8f, 0.9f, 1f, 1f);
        sr.rect(430 - 3, H - 55, 6, 20);
        sr.triangle(425, H - 40, 435, H - 40, 430, H - 30);
        // Skills (Star-like or thunder)
        sr.setColor(1f, 1f, 0.6f, 1f);
        sr.triangle(570, H - 50, 575, H - 40, 565, H - 35);
        sr.triangle(570, H - 30, 565, H - 40, 575, H - 45);
        // Gacha (Box)
        sr.setColor(1f, 0.8f, 0.9f, 1f);
        sr.rect(710 - 8, H - 50, 16, 16);
        sr.setColor(0.8f, 0.3f, 0.5f, 1f);
        sr.rect(710 - 2, H - 50, 4, 16);
        sr.rect(710 - 8, H - 44, 16, 4);
        // Shop (Coin)
        sr.setColor(1f, 0.9f, 0.4f, 1f);
        sr.circle(850, H - 40, 8);
        sr.setColor(0.9f, 0.7f, 0.1f, 1f);
        sr.circle(850, H - 40, 5);
        // Catalog (Book)
        sr.setColor(0.9f, 0.8f, 0.7f, 1f);
        sr.rect(990 - 10, H - 50, 20, 15);
        sr.setColor(0.6f, 0.3f, 0.8f, 1f);
        sr.rectLine(990, H - 50, 990, H - 35, 2f);

        drawMenuBtn();
    }

    public void drawInventoryShapes(Player player) {
        if (player == null) return;
        float slotSize = 65f;
        float padding = 10f;
        float panelW = 3 * slotSize + 4 * padding;
        float panelH = 3 * slotSize + 4 * padding + 40f;
        float invX = W - panelW - 20f;
        float invY = 20f;
        
        // Draw panel background
        sr.setColor(0.15f, 0.15f, 0.25f, 0.85f);
        sr.rect(invX, invY, panelW, panelH);

        // Draw empty slots for visual
        for(int row=0; row<3; row++) {
            for(int col=0; col<3; col++) {
                float x = invX + padding + col * (slotSize + padding);
                float y = invY + panelH - 40f - padding - (row + 1) * slotSize - row * padding;
                sr.setColor(0.1f, 0.1f, 0.15f, 0.8f);
                sr.rect(x, y, slotSize, slotSize);
                sr.setColor(0.3f, 0.3f, 0.4f, 1f);
                sr.rectLine(x, y, x + slotSize, y, 2f);
                sr.rectLine(x, y + slotSize, x + slotSize, y + slotSize, 2f);
                sr.rectLine(x, y, x, y + slotSize, 2f);
                sr.rectLine(x + slotSize, y, x + slotSize, y + slotSize, 2f);
            }
        }

        int i = 0;
        for (Map.Entry<String, Integer> entry : player.getInventory().entrySet()) {
            String name = entry.getKey();
            if (entry.getValue() <= 0 || name.equals("Vàng")) continue;
            int row = i / 3;
            int col = i % 3;
            float x = invX + padding + col * (slotSize + padding);
            float y = invY + panelH - 40f - padding - (row + 1) * slotSize - row * padding;
            sr.setColor(0.2f, 0.4f, 0.6f, 0.8f);
            sr.rect(x+2, y+2, slotSize-4, slotSize-4);
            i++;
        }
    }

    public void drawIdleText(SpriteBatch batch, Player player) {
        font.setColor(Color.WHITE);
        font.draw(batch, "ĐI CÂU", BTN_CAST_X + 75f, BTN_CAST_Y + 36f);
        layout.setText(font, "QUAY LẠI");
        font.draw(batch, "QUAY LẠI", BTN_MENU_X + (90f - layout.width) / 2f, BTN_MENU_Y + 22f);

        layout.setText(font, "NHIỆM VỤ");
        font.draw(batch, "NHIỆM VỤ", 230 + (120 - layout.width) / 2f, H - 58);

        layout.setText(font, "TRANG BỊ");
        font.draw(batch, "TRANG BỊ", 370 + (120 - layout.width) / 2f, H - 58);
        
        layout.setText(font, "SKILLS");
        font.draw(batch, "SKILLS", 510 + (120 - layout.width) / 2f, H - 58);

        layout.setText(font, "GACHA");
        font.draw(batch, "GACHA", 650 + (120 - layout.width) / 2f, H - 58);

        layout.setText(font, "SHOP");
        font.draw(batch, "SHOP", 790 + (120 - layout.width) / 2f, H - 58);

        layout.setText(font, "CATALOG");
        font.draw(batch, "CATALOG", 930 + (120 - layout.width) / 2f, H - 58);
        
        if (player != null) {
            font.setColor(Color.WHITE);
            
            layout.setText(font, "Lv." + player.getLevel());
            font.draw(batch, "Lv." + player.getLevel(), 20 + (80 - layout.width)/2f, 35);
            
            layout.setText(font, player.getGold() + " Vàng");
            font.draw(batch, player.getGold() + " Vàng", 110 + (180 - layout.width)/2f, 35);
            
            layout.setText(font, player.getDiamonds() + " KC");
            font.draw(batch, player.getDiamonds() + " KC", 300 + (120 - layout.width)/2f, 35);

            if (player.isBaitBuffActive()) {
                font.setColor(0f, 1f, 0f, 1f);
                font.draw(batch, "BUFF: MỒI THƠM", W/2f - 60f, H - 20f);
            }
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
        
        font.setColor(1f, 0.8f, 0.2f, 1f);
        layout.setText(font, "QUICK SLOT");
        font.draw(batch, "QUICK SLOT", invX + (panelW - layout.width)/2f, invY + panelH - 10f);

        int i = 0;
        for (Map.Entry<String, Integer> entry : player.getInventory().entrySet()) {
            String name = entry.getKey();
            if (entry.getValue() <= 0 || name.equals("Vàng")) continue;
            int row = i / 3;
            int col = i % 3;
            float x = invX + padding + col * (slotSize + padding);
            float y = invY + panelH - 40f - padding - (row + 1) * slotSize - row * padding;
            
            font.setColor(Color.WHITE);
            String dispName = name;
            if (dispName.length() > 6) dispName = dispName.substring(0, 5) + ".";
            font.getData().setScale(0.75f);
            font.draw(batch, dispName, x + 2f, y + 55f);
            
            font.setColor(1f, 1f, 0f, 1f);
            font.draw(batch, "x" + entry.getValue(), x + 5f, y + 25f);
            
            font.setColor(0.5f, 0.8f, 1f, 1f);
            font.draw(batch, "[" + (i + 1) + "]", x + 40f, y + 25f);
            font.getData().setScale(1f);
            
            i++;
        }

        if (hoveredInventoryItem != null) {
            com.fishingrpg.game.entities.ItemData itemData = com.fishingrpg.game.data.ItemDatabase.getItem(hoveredInventoryItem);
            if (itemData != null) {
                float tooltipW = 220f;
                float tooltipH = 80f;
                Vector3 mouse = new Vector3(com.badlogic.gdx.Gdx.input.getX(), com.badlogic.gdx.Gdx.input.getY(), 0);
                screen.getViewport().unproject(mouse);
                float mouseX = mouse.x;
                float mouseY = mouse.y;
                
                // Adjust position so it doesn't go off screen
                float tipX = mouseX - tooltipW - 10f;
                if (tipX < 0) tipX = mouseX + 10f;
                float tipY = mouseY;
                if (tipY + tooltipH > H) tipY = H - tooltipH - 10f;
                
                batch.end();
                com.badlogic.gdx.Gdx.gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_BLEND);
                sr.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
                sr.setColor(0.1f, 0.1f, 0.15f, 0.9f);
                sr.rect(tipX, tipY, tooltipW, tooltipH);
                sr.setColor(0.3f, 0.4f, 0.5f, 1f);
                sr.rect(tipX, tipY, tooltipW, 2);
                sr.end();
                com.badlogic.gdx.Gdx.gl.glDisable(com.badlogic.gdx.graphics.GL20.GL_BLEND);
                batch.begin();
                
                font.setColor(1f, 0.9f, 0.2f, 1f);
                font.draw(batch, itemData.name, tipX + 10, tipY + 65);
                font.setColor(0.8f, 0.8f, 0.8f, 1f);
                font.getData().setScale(0.8f);
                font.draw(batch, itemData.description, tipX + 10, tipY + 40);
                
                String usageText = "";
                if (itemData.usageType == com.fishingrpg.game.entities.ItemUsageType.IN_COMBAT) usageText = "Chỉ dùng lúc câu";
                else if (itemData.usageType == com.fishingrpg.game.entities.ItemUsageType.PRE_COMBAT) usageText = "Dùng trước khi câu";
                else usageText = "Dùng bất kỳ lúc nào";
                
                font.setColor(0.5f, 0.8f, 0.5f, 1f);
                font.draw(batch, usageText, tipX + 10, tipY + 18);
                font.getData().setScale(1f);
            }
        }
    }

    public void drawShapes(Player player, Fish fish) {
        if (fish != null && screen.fishingManager.state == State.COMBAT) {
            float hpBarX = (W - HP_W) / 2f;
            float hpBarY = HUD_Y + (HUD_H - HP_H) / 2f;
            sr.setColor(0.25f, 0.05f, 0.05f, 1f); sr.rect(hpBarX, hpBarY, HP_W, HP_H);
            float ratio = fish.getCurrentHp() / fish.getMaxHp();
            sr.setColor(0.95f, 0.25f, 0.25f, 1f); sr.rect(hpBarX, hpBarY, HP_W * ratio, HP_H);
        }

        float tensionX = 20f;
        float tensionY = H / 2f - PANEL_H / 2f;
        
        if (screen.fishingManager.fishQuayBunTimer > 0) {
            // Draw blurred/hidden tension bar
            sr.setColor(0.3f, 0.2f, 0.1f, 0.9f);
            sr.rect(tensionX, tensionY, PANEL_W, PANEL_H);
            
            // Draw a red question mark or some indicator that it's blinded
            // (Handled in drawText)
        } else {
            sr.setColor(0.1f, 0.05f, 0.1f, 0.75f); sr.rect(tensionX, tensionY, PANEL_W, PANEL_H);
            float inH = PANEL_H - BAR_INSET * 2f;
            float barX = tensionX + BAR_INSET, barY = tensionY + BAR_INSET, barW = PANEL_W - BAR_INSET * 2f;
            sr.setColor(0.25f, 0.1f, 0.05f, 1f); sr.rect(barX, barY, barW, inH);
    
            float tRatio = player.getCurrentLineTension() / player.getMaxLineTension();
            float tY = barY + tRatio * inH;
            
            // Fill tension bar
            sr.setColor(1f, 0.9f, 0.2f, 1f);
            if (tRatio > 0.85f) {
                sr.setColor(1f, 0.2f, 0.2f, 1f);
            } else if (tRatio <= 0.01f) {
                if ((System.currentTimeMillis() / 200) % 2 == 0) {
                    sr.setColor(1f, 0.2f, 0.2f, 0.5f); // Blinking red when slacking
                } else {
                    sr.setColor(0.3f, 0.3f, 0.3f, 1f);
                }
            }
            sr.rect(barX, barY, barW, Math.max(tRatio * inH, 0));
    
            sr.setColor(1f, 1f, 1f, 1f);
            sr.rect(barX - 8, tY - 4, barW + 16, 8);
            sr.triangle(barX + barW + 10, tY, barX + barW + 20, tY + 6, barX + barW + 20, tY - 6);
        }

        // Stamina Bar
        float stmW = 250f;
        float stmH = 20f;
        float stmX = 20f;
        float stmY = H - 120f;
        sr.setColor(0.9f, 0.9f, 0.9f, 1f); sr.rect(stmX - 2, stmY - 2, stmW + 4, stmH + 4);
        sr.setColor(0.1f, 0.1f, 0.1f, 0.8f);
        sr.rect(stmX, stmY, stmW, stmH);
        
        float stmRatio = player.getCurrentStamina() / player.getMaxStamina();
        sr.setColor(0.2f, 0.85f, 0.2f, 1f);
        if (stmRatio < 0.3f) sr.setColor(0.85f, 0.2f, 0.2f, 1f);
        sr.rect(stmX, stmY, stmW * stmRatio, stmH);

        sr.setColor(0f, 0f, 0f, 0.7f); sr.rect(0, 0, W, MSG_BAR_H);

        if (screen.fishingManager.state == State.CASTING) {
            float castW = 400f;
            float castX = (W - castW) / 2f;
            float castY = H / 2f - 15f;
            
            // Background border
            sr.setColor(0.15f, 0.1f, 0.1f, 0.9f);
            sr.rect(castX - 4, castY - 4, castW + 8, 38);
            
            // Base Red (Miss)
            sr.setColor(0.7f, 0.2f, 0.2f, 1f);
            sr.rect(castX, castY, castW, 30);
            
            // Good area (Yellow)
            float goodW = castW * (80f / 200f); // 40%
            sr.setColor(0.8f, 0.7f, 0.1f, 1f);
            sr.rect(castX + (castW - goodW) / 2f, castY, goodW, 30);
            
            // Perfect area (Green)
            float perfectW = castW * (20f / 200f); // 10%
            if (player != null && player.isSkillEquipped("mat_dai_bang")) {
                perfectW *= player.getSkillTree().getMatDaiBangBonus();
            }
            sr.setColor(0.2f, 0.8f, 0.2f, 1f);
            sr.rect(castX + (castW - perfectW) / 2f, castY, perfectW, 30);
            
            // Add a glossy highlight to the bar
            sr.setColor(1f, 1f, 1f, 0.15f);
            sr.rect(castX, castY + 15, castW, 15);
            
            // Cursor
            float cursorRatio = screen.fishingManager.castCursorX / 200f;
            float cursorPixelX = castX + cursorRatio * castW;
            
            sr.setColor(0.9f, 0.9f, 1f, 1f);
            sr.rect(cursorPixelX - 4, castY - 8, 8, 46);
            sr.setColor(0.4f, 0.7f, 1f, 1f); // Inner blue glow
            sr.rect(cursorPixelX - 2, castY - 8, 4, 46);
        }

        if (screen.fishingManager.state == State.COMBAT) {
            String[] actives = player.getEquippedActives();
            for (int i=0; i<4; i++) {
                if (actives[i] != null) {
                    float cd = getCooldown(actives[i]);
                    float x = BTN_ACTIVES_X + i * 75f;
                    float y = BTN_ACTIVES_Y;
                    if (cd > 0) {
                        sr.setColor(0.3f, 0.1f, 0.1f, 0.9f);
                    } else {
                        sr.setColor(hoverActives[i] ? new Color(0.8f, 0.3f, 0.1f, 1f) : new Color(0.6f, 0.2f, 0.1f, 1f));
                    }
                    sr.rect(x, y, 65f, 65f);
                    sr.setColor(0.4f, 0.1f, 0.1f, 1f); sr.rect(x, y - 4f, 65f, 4f);
                }
            }
            
            sr.setColor(hoverCang ? new Color(0.1f, 0.6f, 0.8f, 1f) : new Color(0.1f, 0.4f, 0.6f, 1f));
            sr.rect(BTN_CANG_X, BTN_CANG_Y, 65f, 65f);
            sr.setColor(0.1f, 0.2f, 0.4f, 1f); sr.rect(BTN_CANG_X, BTN_CANG_Y - 4f, 65f, 4f);
        }

        if (screen.fishingManager.state == State.RESULT || screen.fishingManager.state == State.PRE_CAST) {
            if (screen.fishingManager.state == State.RESULT) {
                // Popup Background for ALL catches
                sr.setColor(0.1f, 0.15f, 0.2f, 0.95f);
                sr.rect((W - 300) / 2f, (H - 200) / 2f, 300, 200);
                sr.setColor(0.3f, 0.5f, 0.7f, 1f);
                sr.rect((W - 300) / 2f, (H - 200) / 2f, 300, 2);

                // sr.setColor(hoverClose ? new Color(0.8f, 0.2f, 0.2f, 1f) : new Color(0.6f, 0.1f, 0.1f, 1f));
                // sr.rect(BTN_CLOSE_X, BTN_CLOSE_Y, 40f, 40f);
            }

            sr.setColor(hoverCast ? new Color(1f, 0.5f, 0.1f, 0.95f) : new Color(0.85f, 0.35f, 0.1f, 0.85f));
            sr.rect(BTN_CAST_X, BTN_CAST_Y, 220f, 60f);
            sr.setColor(1f, 0.85f, 0.25f, 0.6f); sr.rect(BTN_CAST_X, BTN_CAST_Y + 56f, 220f, 4f);
        }
        
        // --- Screen overlay effects for fish ink skills ---
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

        drawMenuBtn();
        drawInventoryShapes(player);
        drawXpBar(player);
    }


    private void drawXpBar(Player player) {
        if (player == null) return;
        float barW = 300f;
        float barH = 15f;
        float barX = LEFT_X;
        float barY = 15f;
        
        sr.setColor(0.1f, 0.1f, 0.1f, 1f);
        sr.rect(barX, barY, barW, barH);
        
        float ratio = (float) player.getCurrentXp() / player.getXpForCurrentLevel();
        sr.setColor(0.3f, 0.6f, 1f, 1f);
        sr.rect(barX, barY, barW * ratio, barH);
    }

    public void drawText(SpriteBatch batch, Player player, Catchable item, String msg, String res) {
        if (item != null && screen.fishingManager.state != State.RESULT) {
            font.setColor(Color.WHITE);
            if (item instanceof Fish) font.setColor(((Fish)item).getRarityColor());
            float hpBarX = (W - HP_W) / 2f;
            float hpBarY = HUD_Y + (HUD_H - HP_H) / 2f;
            layout.setText(font, item.getName());
            font.draw(batch, item.getName(), hpBarX - layout.width - 15f, hpBarY + HP_H/2f + layout.height/2f);
        }

        // Draw XP Text
        if (player != null) {
            font.setColor(1f, 1f, 1f, 1f);
            font.draw(batch, "KN: " + player.getCurrentXp() + "/" + player.getXpForCurrentLevel(), LEFT_X, 15f + 12f);
        }

        float tensionX = 20f;
        float tensionY = H / 2f - PANEL_H / 2f;
        font.setColor(Color.WHITE);
        if (screen.fishingManager.fishQuayBunTimer > 0) {
            font.setColor(Color.RED);
            font.draw(batch, "ĐỘ CĂNG: ???/???", tensionX, tensionY - 10f);
            font.getData().setScale(2f);
            font.draw(batch, "?", tensionX + PANEL_W / 2f - 10f, tensionY + PANEL_H / 2f + 10f);
            font.getData().setScale(1f);
        } else {
            font.draw(batch, "ĐỘ CĂNG: " + (int)player.getCurrentLineTension() + "/" + (int)player.getMaxLineTension(), tensionX, tensionY - 10f);
        }
        
        float stmY = H - 120f;
        font.draw(batch, "THỂ LỰC: " + (int)player.getCurrentStamina() + "/" + (int)player.getMaxStamina(), 20f, stmY + 40f);
        if (player.getCurrentStamina() < player.getMaxStamina() * 0.2f) {
            float blinkAlpha = 0.5f + 0.5f * (float)Math.sin(timer * 5f);
            font.setColor(1f, 0.2f, 0.2f, blinkAlpha);
            font.draw(batch, "Cảnh báo: Thể lực cực thấp!", 20f, stmY + 20f);
            font.setColor(Color.WHITE);
        }

        if (screen.fishingManager.state == State.COMBAT) {
            String[] actives = player.getEquippedActives();
            String[] keys = {"[Q]", "[W]", "[E]", "[R]"};
            for (int i=0; i<4; i++) {
                if (actives[i] != null) {
                    float cd = getCooldown(actives[i]);
                    float x = BTN_ACTIVES_X + i * 75f;
                    float y = BTN_ACTIVES_Y;
                    font.setColor(cd > 0 ? Color.GRAY : Color.WHITE);
                    
                    String name = player.getSkillTree().getSkill(actives[i]).name;
                    if (name.length() > 6) name = name.substring(0, 5) + ".";
                    font.getData().setScale(0.85f);
                    if (cd > 0) {
                        font.draw(batch, name, x + 5f, y + 55f);
                        font.setColor(Color.YELLOW);
                        font.draw(batch, String.format("%.1f", cd), x + 15f, y + 35f);
                    } else {
                        font.draw(batch, name, x + 5f, y + 55f);
                    }
                    font.setColor(0.5f, 0.8f, 1f, 1f);
                    font.draw(batch, keys[i], x + 35f, y + 20f);
                    font.getData().setScale(1f);
                }
            }

            font.setColor(Color.WHITE);
            font.draw(batch, "CĂNG", BTN_CANG_X + 12f, BTN_CANG_Y + 50f);
            font.draw(batch, "DÂY", BTN_CANG_X + 15f, BTN_CANG_Y + 30f);
            font.setColor(0.5f, 0.8f, 1f, 1f);
            font.draw(batch, "[Spc]", BTN_CANG_X + 28f, BTN_CANG_Y + 20f);
        }

        if (msg != null && !msg.isEmpty() && screen.fishingManager.state != State.RESULT) {
            font.setColor(1f, 0.8f, 0.4f, 1f);
            layout.setText(font, msg);
            font.draw(batch, msg, (W - layout.width) / 2f, H / 2f + 100f);
        }
        
        if (screen.fishingManager.castResultTimer > 0) {
            float timer = screen.fishingManager.castResultTimer;
            float alpha = Math.min(1f, timer / 0.5f); // Fade out in last 0.5s
            float yOffset = (1.5f - timer) * 40f; // Move up
            font.getData().setScale(2f);
            if (screen.fishingManager.castResultText.equals("PERFECT!")) {
                font.setColor(1f, 0.8f, 0.2f, alpha); // Gold
            } else if (screen.fishingManager.castResultText.equals("GOOD")) {
                font.setColor(0.2f, 1f, 0.2f, alpha); // Green
            } else {
                font.setColor(0.8f, 0.8f, 0.8f, alpha); // Gray
            }
            layout.setText(font, screen.fishingManager.castResultText);
            font.draw(batch, screen.fishingManager.castResultText, (W - layout.width) / 2f, H / 2f + 130f + yOffset);
            font.getData().setScale(1f);
        }
        
        if (screen.fishingManager.state == State.COMBAT && item instanceof Fish) {
            Fish f = (Fish) item;
            boolean isPulling = f.getState() == Fish.State.PULLING;
            String stateMsg = isPulling ? "CÁ ĐANG KÉO! NHẢ DÂY!" : "CÁ ĐANG MỆT! THU DÂY!";
            
            // Draw background box for combat warning
            batch.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(isPulling ? new Color(0.8f, 0.1f, 0.1f, 0.6f) : new Color(0.1f, 0.8f, 0.1f, 0.6f));
            float boxW = 350f;
            sr.rect((W - boxW)/2f, H/2f + 110f, boxW, 50f);
            sr.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            batch.begin();

            font.getData().setScale(1.2f);
            font.setColor(Color.WHITE);
            layout.setText(font, stateMsg);
            font.draw(batch, stateMsg, (W - layout.width) / 2f, H / 2f + 145f);
            font.getData().setScale(1f);
        }

        if (screen.fishingManager.state == State.WAITING || screen.fishingManager.state == State.BITE) {
            float time = screen.fishingManager.stateTimer;
            float bobY = 0;
            if (screen.fishingManager.state == State.WAITING) {
                bobY = (float)Math.sin(time * 5f) * 5f; // Gentle bobbing
            } else {
                bobY = -15f + (float)Math.random() * 10f; // Jerking down violently
            }
            
            float centerX = W / 2f;
            float centerY = H / 2f + bobY;
            
            batch.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            sr.begin(ShapeRenderer.ShapeType.Filled);
            
            // Water ripples
            if (screen.fishingManager.state == State.WAITING) {
                sr.setColor(0.3f, 0.6f, 0.9f, 0.3f);
                sr.ellipse(centerX - 30f, H/2f - 10f, 60f, 20f);
                float rippleScale = (time % 1f);
                sr.setColor(0.3f, 0.6f, 0.9f, 0.3f * (1f - rippleScale));
                sr.ellipse(centerX - 30f - rippleScale*20f, H/2f - 10f - rippleScale*5f, 60f + rippleScale*40f, 20f + rippleScale*10f);
            } else {
                // Splash
                sr.setColor(0.8f, 0.9f, 1f, 0.6f);
                sr.ellipse(centerX - 40f, H/2f - 15f, 80f, 30f);
                sr.setColor(0.6f, 0.8f, 1f, 0.8f);
                for(int j=0; j<5; j++) {
                    sr.circle(centerX - 30f + (float)Math.random()*60f, H/2f + (float)Math.random()*20f, 2f + (float)Math.random()*4f);
                }
            }
            
            // Bobber top (red)
            sr.setColor(Color.RED);
            sr.rect(centerX - 5f, centerY, 10f, 15f);
            // Bobber bottom (white)
            sr.setColor(Color.WHITE);
            sr.rect(centerX - 5f, centerY - 15f, 10f, 15f);
            // Antenna
            sr.setColor(Color.DARK_GRAY);
            sr.rectLine(centerX, centerY + 15f, centerX, centerY + 25f, 2f);
            
            sr.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            batch.begin();
        }

        if (screen.fishingManager.state == State.BITE) {
            float time = screen.fishingManager.stateTimer;
            float scale = 3f + (float)Math.sin(time * 30f) * 0.5f; // Pulsate
            font.getData().setScale(scale);
            font.setColor(MathUtils.random(0.8f, 1f), 0f, 0f, 1f); // Flicker red
            layout.setText(font, "(!)");
            font.draw(batch, "(!)", (W - layout.width)/2f, H/2f + 80f + layout.height/2f);
            font.getData().setScale(1f);
        }

        if (screen.fishingManager.state == State.RESULT) {
            if (screen.fishingManager.lastEncounterSuccess) {
                if (screen.fishingManager.currentCatch instanceof Fish) {
                    Fish f = (Fish) screen.fishingManager.currentCatch;
                    float origScaleX = font.getData().scaleX;
                    font.getData().setScale(1.5f);
                    layout.setText(font, f.getName());
                    if (layout.width > 280f) { // fit in 300px box
                        font.getData().setScale(1.5f * (280f / layout.width));
                        layout.setText(font, f.getName());
                    }
                    font.setColor(f.getRarityColor());
                    font.draw(batch, f.getName(), (W - layout.width) / 2f, H / 2f + 80f);
                    font.getData().setScale(origScaleX);
                    
                    font.setColor(0.8f, 0.8f, 0.8f, 1f);
                    String rText = "Độ hiếm: " + f.getRarity().name();
                    layout.setText(font, rText);
                    font.draw(batch, rText, (W - layout.width) / 2f, H / 2f + 40f);
                    
                    font.setColor(1f, 1f, 1f, 1f);
                    String sizeText = "Chiều dài: " + String.format("%.1f", f.getLengthCm()) + " cm | Cân nặng: " + String.format("%.1f", f.getWeightKg()) + " kg";
                    layout.setText(font, sizeText);
                    font.draw(batch, sizeText, (W - layout.width) / 2f, H / 2f + 10f);
                    
                    if (msg != null && msg.contains("[KỶ LỤC]")) {
                        font.setColor(1f, 0.8f, 0.2f, 1f);
                        String recText = "KỶ LỤC MỚI!";
                        layout.setText(font, recText);
                        font.draw(batch, recText, (W - layout.width) / 2f, H / 2f - 20f);
                    } else if (msg != null && msg.contains("[MỚI]")) {
                        font.setColor(0.2f, 1f, 0.2f, 1f);
                        String newText = "CÁ MỚI!";
                        layout.setText(font, newText);
                        font.draw(batch, newText, (W - layout.width) / 2f, H / 2f - 20f);
                    }
                } else if (item != null) {
                    font.setColor(Color.WHITE);
                    layout.setText(font, "NHẬN ĐƯỢC: " + item.getName());
                    font.draw(batch, "NHẬN ĐƯỢC: " + item.getName(), (W - layout.width) / 2f, H / 2f + 40f);
                }
                
                if (res != null && !res.isEmpty()) {
                    font.setColor(0.5f, 1f, 0.5f, 1f);
                    layout.setText(font, res);
                    font.draw(batch, res, (W - layout.width) / 2f, H / 2f - 50f);
                }
            } else {
                String failMsg = msg != null && !msg.isEmpty() ? msg : "THẤT BẠI";
                float origScaleX = font.getData().scaleX;
                font.getData().setScale(1.5f);
                layout.setText(font, failMsg);
                if (layout.width > 280f) { // fit in 300px box
                    font.getData().setScale(1.5f * (280f / layout.width));
                    layout.setText(font, failMsg);
                }
                font.setColor(new Color(1f, 0.3f, 0.3f, 1f));
                font.draw(batch, failMsg, (W - layout.width) / 2f, H / 2f + 20f);
                font.getData().setScale(origScaleX);
            }

            font.setColor(Color.WHITE);
            font.draw(batch, "QUĂNG CẦN", BTN_CAST_X + 60f, BTN_CAST_Y + 36f);
            // font.draw(batch, "X", BTN_CLOSE_X + 15f, BTN_CLOSE_Y + 26f);
        } else if (res != null && !res.isEmpty()) {
            font.setColor(0.5f, 1f, 0.5f, 1f);
            layout.setText(font, res);
            font.draw(batch, res, (W - layout.width) / 2f, H / 2f + 60f);
        }
        
        if (screen.fishingManager.state == State.PRE_CAST) {
            font.setColor(Color.WHITE);
            font.draw(batch, "QUĂNG CẦN", BTN_CAST_X + 60f, BTN_CAST_Y + 36f);
        }
        
        font.setColor(Color.WHITE);
        layout.setText(font, "QUAY LẠI");
        font.draw(batch, "QUAY LẠI", BTN_MENU_X + (90f - layout.width) / 2f, BTN_MENU_Y + 22f);
        drawInventoryText(batch, player);
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
    
    private void drawMenuBtn() {
        sr.setColor(hoverMenu ? new Color(0.3f, 0.3f, 0.4f, 0.95f) : new Color(0.2f, 0.2f, 0.3f, 0.85f));
        sr.rect(BTN_MENU_X, BTN_MENU_Y, 90f, 35f);
        sr.setColor(0.5f, 0.5f, 0.6f, 0.6f); sr.rect(BTN_MENU_X, BTN_MENU_Y + 33f, 90f, 2f);
    }
}
