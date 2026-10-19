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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fishingrpg.game.FishingRPG;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.entities.equipment.Equipment;
import com.fishingrpg.game.entities.equipment.EquipmentType;
import com.fishingrpg.game.entities.equipment.Rarity;
import com.fishingrpg.game.entities.equipment.Substat;
import com.fishingrpg.game.systems.AudioManager;

import java.util.ArrayList;
import java.util.List;

public class EquipmentScreen implements Screen {
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

    private int scrollOffset = 0;
    private static final int MAX_VISIBLE = 5;

    private enum Tab { ALL, ROD, LINE, HOOK }
    private Tab currentTab = Tab.ALL;
    
    private Equipment hoveredEq = null;
    private float hoverX = 0, hoverY = 0;
    private List<Equipment> cachedFilteredInventory = new ArrayList<>();

    private boolean hoverBack = false;
    private float animTimer = 0f;

    // Ambient floating particles
    private static final int PARTICLE_COUNT = 25;
    private float[] partX = new float[PARTICLE_COUNT];
    private float[] partY = new float[PARTICLE_COUNT];
    private float[] partSpeed = new float[PARTICLE_COUNT];
    private float[] partAlpha = new float[PARTICLE_COUNT];

    public EquipmentScreen(FishingRPG game, Screen previousScreen) {
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

        // Initialize particles
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            partX[i] = MathUtils.random(0, W);
            partY[i] = MathUtils.random(0, H);
            partSpeed[i] = MathUtils.random(10f, 25f);
            partAlpha[i] = MathUtils.random(0.2f, 0.5f);
        }
    }

    @Override
    public void render(float delta) {
        animTimer += delta;
        update(delta);

        // Update ambient particles
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            partY[i] += partSpeed[i] * delta;
            partX[i] += MathUtils.sin(animTimer + i) * 6f * delta;
            if (partY[i] > H + 10f) {
                partY[i] = -10f;
                partX[i] = MathUtils.random(0, W);
            }
        }

        ScreenUtils.clear(new Color(0.04f, 0.06f, 0.10f, 1f));
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        Vector3 m = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(m);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Armory Backdrop Gradient
        sr.rect(0, 0, W, H,
            new Color(0.03f, 0.05f, 0.09f, 1f), new Color(0.03f, 0.05f, 0.09f, 1f),
            new Color(0.07f, 0.11f, 0.18f, 1f), new Color(0.07f, 0.11f, 0.18f, 1f)
        );

        // Subtle decorative steel grid
        sr.setColor(0.15f, 0.25f, 0.4f, 0.06f);
        for (float gx = 60; gx < W; gx += 120) sr.rect(gx, 0, 1, H);
        for (float gy = 40; gy < H; gy += 100) sr.rect(0, gy, W, 1);

        // Ambient particles
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            float pulse = 0.5f + 0.5f * MathUtils.sin(animTimer * 2f + i);
            sr.setColor(0.4f, 0.75f, 1f, partAlpha[i] * pulse);
            sr.circle(partX[i], partY[i], 2f);
        }

        // Header Bar
        sr.setColor(0.06f, 0.09f, 0.14f, 0.95f);
        sr.rect(0, H - 72f, W, 72f);
        sr.setColor(0.25f, 0.65f, 0.85f, 0.7f);
        sr.rect(0, H - 74f, W, 2f);

        // Back Button
        hoverBack = (m.x >= 25f && m.x <= 155f && m.y >= H - 62f && m.y <= H - 20f);
        sr.setColor(hoverBack ? new Color(0.25f, 0.35f, 0.50f, 0.95f) : new Color(0.12f, 0.18f, 0.26f, 0.9f));
        drawRoundedRect(sr, 25, H - 62, 130, 42, 8);
        sr.setColor(hoverBack ? new Color(0.6f, 0.85f, 1f, 1f) : new Color(0.3f, 0.5f, 0.7f, 0.7f));
        drawRectOutline(sr, 25, H - 62, 130, 42, 1.5f);

        // Top Right Player Level & Gear Power Dock
        float gearDockW = 280f;
        float gearDockH = 42f;
        float gearDockX = W - gearDockW - 25f;
        float gearDockY = H - 62f;
        sr.setColor(0.09f, 0.13f, 0.20f, 0.95f);
        drawRoundedRect(sr, gearDockX, gearDockY, gearDockW, gearDockH, 8);
        sr.setColor(0.3f, 0.75f, 0.6f, 0.8f);
        drawRectOutline(sr, gearDockX, gearDockY, gearDockW, gearDockH, 1.5f);

        // --- LEFT PANEL: EQUIPPED GEAR (Width 470px) ---
        float leftX = 25f;
        float leftY = 25f;
        float leftW = 470f;
        float leftH = H - 110f;

        sr.setColor(0.07f, 0.10f, 0.16f, 0.95f);
        drawRoundedRect(sr, leftX, leftY, leftW, leftH, 12);
        sr.setColor(0.22f, 0.35f, 0.50f, 0.7f);
        drawRectOutline(sr, leftX, leftY, leftW, leftH, 1.5f);

        // Equipped Gear Slot Cards
        drawEquipSlot(sr, leftX + 15f, leftY + leftH - 150f, player.getEquippedRod(), EquipmentType.ROD, m);
        drawEquipSlot(sr, leftX + 15f, leftY + leftH - 280f, player.getEquippedLine(), EquipmentType.LINE, m);
        drawEquipSlot(sr, leftX + 15f, leftY + leftH - 410f, player.getEquippedHook(), EquipmentType.HOOK, m);

        // Set Bonus Status Plate (Bottom of Left Panel)
        float setPlateX = leftX + 15f;
        float setPlateY = leftY + 15f;
        float setPlateW = leftW - 30f;
        float setPlateH = 100f;

        boolean setBonusActive = player.isSetBonusActive() && player.getEquippedRod() != null;
        if (setBonusActive) {
            float pulse = 0.8f + 0.2f * MathUtils.sin(animTimer * 3f);
            sr.setColor(0.35f * pulse, 0.25f * pulse, 0.08f * pulse, 0.95f);
            drawRoundedRect(sr, setPlateX, setPlateY, setPlateW, setPlateH, 8);
            sr.setColor(1f * pulse, 0.8f * pulse, 0.2f * pulse, 1f);
            drawRectOutline(sr, setPlateX, setPlateY, setPlateW, setPlateH, 2f);
        } else {
            sr.setColor(0.09f, 0.12f, 0.18f, 0.85f);
            drawRoundedRect(sr, setPlateX, setPlateY, setPlateW, setPlateH, 8);
            sr.setColor(0.2f, 0.28f, 0.38f, 0.6f);
            drawRectOutline(sr, setPlateX, setPlateY, setPlateW, setPlateH, 1f);
        }

        // --- RIGHT PANEL: INVENTORY (Width 740px) ---
        float rightX = 515f;
        float rightY = 25f;
        float rightW = W - rightX - 25f;
        float rightH = H - 110f;

        sr.setColor(0.07f, 0.10f, 0.16f, 0.95f);
        drawRoundedRect(sr, rightX, rightY, rightW, rightH, 12);
        sr.setColor(0.22f, 0.35f, 0.50f, 0.7f);
        drawRectOutline(sr, rightX, rightY, rightW, rightH, 1.5f);

        // Filter Tabs
        float tabW = 125f;
        float tabH = 36f;
        float tabStartX = rightX + 15f;
        float tabY = rightY + rightH - 50f;

        Tab[] tabs = Tab.values();
        for (int t = 0; t < tabs.length; t++) {
            float tx = tabStartX + t * (tabW + 10f);
            boolean isCur = (currentTab == tabs[t]);
            boolean hoverTab = (m.x >= tx && m.x <= tx + tabW && m.y >= tabY && m.y <= tabY + tabH);

            sr.setColor(isCur ? new Color(0.18f, 0.42f, 0.65f, 0.95f) : (hoverTab ? new Color(0.12f, 0.24f, 0.38f, 0.9f) : new Color(0.10f, 0.15f, 0.22f, 0.85f)));
            drawRoundedRect(sr, tx, tabY, tabW, tabH, 6);
            sr.setColor(isCur ? new Color(0.5f, 0.9f, 1f, 1f) : (hoverTab ? new Color(0.35f, 0.6f, 0.8f, 0.8f) : new Color(0.2f, 0.3f, 0.42f, 0.6f)));
            drawRectOutline(sr, tx, tabY, tabW, tabH, isCur ? 2f : 1f);
        }

        // Inventory Items List
        List<Equipment> filteredInv = cachedFilteredInventory;
        float invStartY = rightY + rightH - 120f;
        float rowW = rightW - 40f;
        float rowH = 80f;
        float rowSpacing = 90f;

        for (int i = 0; i < MAX_VISIBLE; i++) {
            int idx = scrollOffset + i;
            if (idx < filteredInv.size()) {
                Equipment eq = filteredInv.get(idx);
                float ry = invStartY - i * rowSpacing;

                boolean isEq = isEquipped(eq);
                boolean hoverRow = (m.x >= rightX + 15f && m.x <= rightX + 15f + rowW - 110f && m.y >= ry && m.y <= ry + rowH);

                // Row Base
                sr.setColor(hoverRow ? new Color(0.14f, 0.20f, 0.28f, 0.95f) : new Color(0.10f, 0.14f, 0.20f, 0.9f));
                drawRoundedRect(sr, rightX + 15f, ry, rowW, rowH, 8);

                // Rarity left stripe
                sr.setColor(eq.rarity.color);
                drawRoundedRect(sr, rightX + 15f, ry, 6f, rowH, 3f);

                // Row outline
                sr.setColor(hoverRow ? new Color(0.4f, 0.7f, 0.95f, 0.8f) : new Color(0.18f, 0.26f, 0.36f, 0.6f));
                drawRectOutline(sr, rightX + 15f, ry, rowW, rowH, 1.2f);

                // Equip / Unequip button
                float btnW = 95f;
                float btnH = 38f;
                float btnX = rightX + 15f + rowW - btnW - 12f;
                float btnY = ry + (rowH - btnH) / 2f;

                boolean hoverBtn = (m.x >= btnX && m.x <= btnX + btnW && m.y >= btnY && m.y <= btnY + btnH);
                if (isEq) {
                    sr.setColor(hoverBtn ? new Color(0.6f, 0.25f, 0.25f, 1f) : new Color(0.45f, 0.18f, 0.18f, 0.95f));
                    drawRoundedRect(sr, btnX, btnY, btnW, btnH, 6);
                    sr.setColor(hoverBtn ? new Color(1f, 0.65f, 0.65f, 1f) : new Color(0.8f, 0.35f, 0.35f, 0.8f));
                    drawRectOutline(sr, btnX, btnY, btnW, btnH, 1.5f);
                } else {
                    sr.setColor(hoverBtn ? new Color(0.22f, 0.58f, 0.38f, 1f) : new Color(0.15f, 0.42f, 0.26f, 0.95f));
                    drawRoundedRect(sr, btnX, btnY, btnW, btnH, 6);
                    sr.setColor(hoverBtn ? new Color(0.6f, 1f, 0.7f, 1f) : new Color(0.35f, 0.85f, 0.5f, 0.8f));
                    drawRectOutline(sr, btnX, btnY, btnW, btnH, 1.5f);
                }
            }
        }

        // Scrollbar
        if (filteredInv.size() > MAX_VISIBLE) {
            float scrollBarX = rightX + rightW - 18f;
            float scrollBarY = rightY + 20f;
            float scrollBarH = rightH - 140f;

            sr.setColor(0.10f, 0.14f, 0.20f, 0.8f);
            drawRoundedRect(sr, scrollBarX, scrollBarY, 8f, scrollBarH, 4f);

            float scrollRatio = (float)scrollOffset / (filteredInv.size() - MAX_VISIBLE);
            float barHeight = Math.max(35f, scrollBarH * (MAX_VISIBLE / (float)filteredInv.size()));
            float barY = scrollBarY + (scrollBarH - barHeight) * (1f - scrollRatio);

            sr.setColor(0.35f, 0.65f, 0.9f, 0.9f);
            drawRoundedRect(sr, scrollBarX, barY, 8f, barHeight, 4f);
        }

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // --- DRAW BATCH TEXTS ---
        game.batch.begin();

        // Header Title
        titleFont.setColor(0.95f, 0.85f, 0.45f, 1f);
        layout.setText(titleFont, "KHO TRANG BỊ CÂU CÁ");
        titleFont.draw(game.batch, layout, W / 2f - layout.width / 2f, H - 22f);

        font.setColor(0.65f, 0.8f, 0.95f, 0.85f);
        layout.setText(font, "Quản lý Cần Câu, Dây Cước, Lưỡi Câu và kích hoạt hiệu ứng Thần Khí");
        font.draw(game.batch, layout, W / 2f - layout.width / 2f, H - 48f);

        // Back button text
        font.setColor(hoverBack ? Color.WHITE : new Color(0.8f, 0.9f, 1f, 0.9f));
        layout.setText(font, "← QUAY LẠI");
        font.draw(game.batch, layout, 25f + (130f - layout.width) / 2f, H - 36f);

        // Top Right Gear Power Dock Text
        font.setColor(0.4f, 1f, 0.7f, 1f);
        String pStr = "Cấp " + player.getLevel() + " • Lực Kéo: " + (int)player.getBasePullDamage();
        font.draw(game.batch, pStr, gearDockX + 16f, gearDockY + 26f);

        // Left Panel Header
        font.setColor(0.8f, 0.9f, 1f, 0.9f);
        font.draw(game.batch, "TRANG BỊ ĐANG MẶC (3 MÓN)", leftX + 18f, leftY + leftH - 18f);

        // Render Equipped Slot Texts
        drawEquipSlotText(leftX + 15f, leftY + leftH - 150f, player.getEquippedRod(), EquipmentType.ROD);
        drawEquipSlotText(leftX + 15f, leftY + leftH - 280f, player.getEquippedLine(), EquipmentType.LINE);
        drawEquipSlotText(leftX + 15f, leftY + leftH - 410f, player.getEquippedHook(), EquipmentType.HOOK);

        // Set Bonus Status Text
        if (setBonusActive) {
            Equipment rod = player.getEquippedRod();
            String desc = com.fishingrpg.game.entities.equipment.EquipmentGenerator.getSetBonusDesc(rod.setName);
            font.setColor(1f, 0.9f, 0.35f, 1f);
            layout.setText(font, "★ KÍCH HOẠT SET: " + rod.setName.toUpperCase() + " (3/3)");
            font.draw(game.batch, layout, setPlateX + (setPlateW - layout.width) / 2f, setPlateY + setPlateH - 22f);

            font.setColor(Color.WHITE);
            font.getData().setScale(0.85f);
            layout.setText(font, desc, Color.WHITE, setPlateW - 20f, Align.center, true);
            font.draw(game.batch, desc, setPlateX + 10f, setPlateY + 52f, setPlateW - 20f, Align.center, true);
            font.getData().setScale(1f);
        } else {
            font.setColor(0.6f, 0.65f, 0.75f, 1f);
            layout.setText(font, "SET BONUS: CHƯA ĐỦ 3 MÓN");
            font.draw(game.batch, layout, setPlateX + (setPlateW - layout.width) / 2f, setPlateY + setPlateH - 25f);

            font.setColor(0.5f, 0.55f, 0.65f, 0.85f);
            font.getData().setScale(0.85f);
            layout.setText(font, "Mặc cùng 1 bộ Cần, Dây, Lưỡi để mở khóa nội tại thần thoại");
            font.draw(game.batch, layout, setPlateX + (setPlateW - layout.width) / 2f, setPlateY + 45f);
            font.getData().setScale(1f);
        }

        // Right Panel Tab Texts
        String[] tabNames = {"TẤT CẢ", "CẦN CÂU", "DÂY CƯỚC", "LƯỠI CÂU"};
        for (int t = 0; t < tabNames.length; t++) {
            float tx = tabStartX + t * (tabW + 10f);
            boolean isCur = (currentTab == tabs[t]);
            font.setColor(isCur ? Color.WHITE : new Color(0.7f, 0.8f, 0.9f, 0.8f));
            layout.setText(font, tabNames[t]);
            font.draw(game.batch, layout, tx + (tabW - layout.width) / 2f, tabY + 24f);
        }

        // Inventory Counter
        font.setColor(0.6f, 0.8f, 0.95f, 0.85f);
        String invCountStr = "Túi đồ: " + filteredInv.size() + " món";
        font.draw(game.batch, invCountStr, rightX + rightW - 140f, tabY + 24f);

        // Inventory List Items Text
        for (int i = 0; i < MAX_VISIBLE; i++) {
            int idx = scrollOffset + i;
            if (idx < filteredInv.size()) {
                Equipment eq = filteredInv.get(idx);
                float ry = invStartY - i * rowSpacing;
                boolean isEq = isEquipped(eq);

                // Name & Rarity
                titleFont.setColor(eq.rarity.color);
                layout.setText(titleFont, eq.name);
                float maxTitleW = 340f;
                if (layout.width > maxTitleW) {
                    titleFont.getData().setScale(0.8f * (maxTitleW / layout.width));
                } else {
                    titleFont.getData().setScale(0.8f);
                }
                titleFont.draw(game.batch, eq.name, rightX + 35f, ry + rowH - 18f);
                titleFont.getData().setScale(1f);

                // Type & Set Tag
                font.setColor(0.75f, 0.8f, 0.9f, 0.85f);
                font.getData().setScale(0.85f);
                String typeStr = (eq.type == EquipmentType.ROD ? "Cần" : eq.type == EquipmentType.LINE ? "Cước" : "Lưỡi") + " • Bộ: " + eq.setName;
                font.draw(game.batch, typeStr, rightX + 35f, ry + rowH - 45f);

                // Main Stats & Comparative Stat Diff
                String stats = getEqMainStats(eq);
                font.setColor(0.4f, 1f, 0.6f, 1f);
                font.draw(game.batch, stats, rightX + 35f, ry + 22f);

                // Diff indicator if not equipped
                if (!isEq) {
                    Equipment currentlyEquipped = getEquippedByType(eq.type);
                    if (currentlyEquipped != null) {
                        float diff = eq.mainStat1 - currentlyEquipped.mainStat1;
                        if (Math.abs(diff) >= 0.5f) {
                            String diffStr = (diff > 0 ? "▲ +" : "▼ ") + (int)diff;
                            font.setColor(diff > 0 ? new Color(0.3f, 1f, 0.5f, 1f) : new Color(1f, 0.35f, 0.35f, 1f));
                            font.draw(game.batch, diffStr, rightX + 380f, ry + 22f);
                        }
                    }
                }
                font.getData().setScale(1f);

                // Button Text
                float btnW = 95f;
                float btnX = rightX + 15f + rowW - btnW - 12f;
                float btnY = ry + (rowH - 38f) / 2f;

                font.setColor(Color.WHITE);
                String btnText = isEq ? "Tháo Ra" : "Mặc Vào";
                layout.setText(font, btnText);
                font.draw(game.batch, layout, btnX + (btnW - layout.width) / 2f, btnY + 25f);
            }
        }

        game.batch.end();

        // Tooltip rendering over everything
        if (hoveredEq != null) {
            drawTooltip(hoveredEq, hoverX, hoverY);
        }
    }

    private void drawEquipSlot(ShapeRenderer sr, float x, float y, Equipment eq, EquipmentType type, Vector3 m) {
        float w = 440f;
        float h = 115f;

        // Slot Frame Base
        sr.setColor(0.10f, 0.14f, 0.22f, 0.95f);
        drawRoundedRect(sr, x, y, w, h, 8);

        // Icon Square (Left)
        float sqSize = 85f;
        float sqX = x + 15f;
        float sqY = y + 15f;
        sr.setColor(0.06f, 0.08f, 0.14f, 0.9f);
        drawRoundedRect(sr, sqX, sqY, sqSize, sqSize, 6);

        if (eq != null) {
            sr.setColor(eq.rarity.color);
            drawRectOutline(sr, sqX, sqY, sqSize, sqSize, 2f);

            // Frame outer glow
            sr.setColor(eq.rarity.color.r * 0.5f, eq.rarity.color.g * 0.5f, eq.rarity.color.b * 0.5f, 0.4f);
            drawRectOutline(sr, x, y, w, h, 1.2f);

            // Unequip Button Box
            float uBtnW = 80f;
            float uBtnH = 32f;
            float uBtnX = x + w - uBtnW - 15f;
            float uBtnY = y + 15f;

            boolean hoverUBtn = (m.x >= uBtnX && m.x <= uBtnX + uBtnW && m.y >= uBtnY && m.y <= uBtnY + uBtnH);
            sr.setColor(hoverUBtn ? new Color(0.6f, 0.25f, 0.25f, 1f) : new Color(0.4f, 0.18f, 0.18f, 0.95f));
            drawRoundedRect(sr, uBtnX, uBtnY, uBtnW, uBtnH, 6);
            sr.setColor(hoverUBtn ? new Color(1f, 0.6f, 0.6f, 1f) : new Color(0.7f, 0.35f, 0.35f, 0.8f));
            drawRectOutline(sr, uBtnX, uBtnY, uBtnW, uBtnH, 1.5f);
        } else {
            sr.setColor(0.2f, 0.25f, 0.35f, 0.5f);
            drawRectOutline(sr, sqX, sqY, sqSize, sqSize, 1f);
            drawRectOutline(sr, x, y, w, h, 1f);
        }

        // Draw Slot Icon (Rod / Line / Hook vector shape)
        drawTypeIcon(sr, type, sqX + sqSize / 2f, sqY + sqSize / 2f, eq != null ? eq.rarity.color : Color.GRAY);
    }

    private void drawTypeIcon(ShapeRenderer sr, EquipmentType type, float cx, float cy, Color col) {
        sr.setColor(col);
        if (type == EquipmentType.ROD) {
            sr.rectLine(cx - 20, cy - 20, cx + 20, cy + 20, 3f);
            sr.circle(cx - 15, cy - 15, 5f);
        } else if (type == EquipmentType.LINE) {
            sr.circle(cx, cy, 18f);
            sr.setColor(0.06f, 0.08f, 0.14f, 1f);
            sr.circle(cx, cy, 10f);
            sr.setColor(col);
            sr.circle(cx, cy, 4f);
        } else {
            // Hook
            sr.rectLine(cx - 6, cy + 18, cx - 6, cy - 8, 3f);
            sr.rectLine(cx - 6, cy - 8, cx + 12, cy - 8, 3f);
            sr.rectLine(cx + 12, cy - 8, cx + 12, cy + 8, 3f);
            sr.triangle(cx + 12, cy + 8, cx + 6, cy + 2, cx + 15, cy + 2);
        }
    }

    private void drawEquipSlotText(float x, float y, Equipment eq, EquipmentType type) {
        float w = 440f;
        String typeName = type == EquipmentType.ROD ? "CẦN CÂU" : type == EquipmentType.LINE ? "DÂY CƯỚC" : "LƯỠI CÂU";

        if (eq != null) {
            titleFont.setColor(eq.rarity.color);
            layout.setText(titleFont, eq.name);
            float targetWidth = w - 190f;
            if (layout.width > targetWidth) {
                titleFont.getData().setScale(0.8f * (targetWidth / layout.width));
            } else {
                titleFont.getData().setScale(0.8f);
            }
            titleFont.draw(game.batch, eq.name, x + 115f, y + 95f);
            titleFont.getData().setScale(1f);

            font.setColor(0.7f, 0.8f, 0.9f, 0.85f);
            font.getData().setScale(0.85f);
            font.draw(game.batch, typeName + " • Bộ: " + eq.setName + " (" + eq.rarity.name() + ")", x + 115f, y + 68f);

            font.setColor(0.4f, 1f, 0.6f, 1f);
            String stats = getEqMainStats(eq);
            font.draw(game.batch, stats, x + 115f, y + 42f);
            font.getData().setScale(1f);

            // Unequip button text
            float uBtnW = 80f;
            float uBtnX = x + w - uBtnW - 15f;
            font.setColor(Color.WHITE);
            layout.setText(font, "Tháo");
            font.draw(game.batch, layout, uBtnX + (uBtnW - layout.width) / 2f, y + 36f);
        } else {
            font.setColor(0.6f, 0.7f, 0.8f, 0.6f);
            font.draw(game.batch, typeName, x + 115f, y + 80f);
            font.setColor(0.4f, 0.45f, 0.55f, 0.8f);
            font.draw(game.batch, "Chưa trang bị (Trống)", x + 115f, y + 50f);
        }
    }

    private void update(float delta) {
        Vector3 m = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(m);

        cachedFilteredInventory = getFilteredInventory();

        // Handle hovering
        hoveredEq = null;
        hoverX = m.x;
        hoverY = m.y;

        float leftX = 25f;
        float leftY = 25f;
        float leftH = H - 110f;

        // Hover equipped slots
        if (m.x >= leftX + 15f && m.x <= leftX + 455f) {
            if (m.y >= leftY + leftH - 150f && m.y <= leftY + leftH - 35f && player.getEquippedRod() != null) hoveredEq = player.getEquippedRod();
            if (m.y >= leftY + leftH - 280f && m.y <= leftY + leftH - 165f && player.getEquippedLine() != null) hoveredEq = player.getEquippedLine();
            if (m.y >= leftY + leftH - 410f && m.y <= leftY + leftH - 295f && player.getEquippedHook() != null) hoveredEq = player.getEquippedHook();
        }

        // Hover inventory
        float rightX = 515f;
        float rightY = 25f;
        float rightH = H - 110f;
        float invStartY = rightY + rightH - 120f;
        float rowW = W - rightX - 65f;

        if (m.x >= rightX + 15f && m.x <= rightX + 15f + rowW - 110f) {
            for (int i = 0; i < MAX_VISIBLE; i++) {
                if (scrollOffset + i < cachedFilteredInventory.size()) {
                    float ry = invStartY - i * 90f;
                    if (m.y >= ry && m.y <= ry + 80f) {
                        hoveredEq = cachedFilteredInventory.get(scrollOffset + i);
                    }
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && hoverBack)) {
            AudioManager.getInstance().playSFX("sfx_button");
            game.setScreen(previousScreen);
            return;
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            // Tabs
            float tabW = 125f;
            float tabH = 36f;
            float tabStartX = rightX + 15f;
            float tabY = rightY + rightH - 50f;

            Tab[] tabs = Tab.values();
            for (int t = 0; t < tabs.length; t++) {
                float tx = tabStartX + t * (tabW + 10f);
                if (m.x >= tx && m.x <= tx + tabW && m.y >= tabY && m.y <= tabY + tabH) {
                    if (currentTab != tabs[t]) {
                        AudioManager.getInstance().playSFX("sfx_button");
                        currentTab = tabs[t];
                        scrollOffset = 0;
                        return;
                    }
                }
            }

            // Unequip buttons
            float w = 440f;
            float uBtnW = 80f;
            float uBtnH = 32f;
            float uBtnX = leftX + 15f + w - uBtnW - 15f;

            if (m.x >= uBtnX && m.x <= uBtnX + uBtnW) {
                float rodBtnY = leftY + leftH - 150f + 15f;
                if (m.y >= rodBtnY && m.y <= rodBtnY + uBtnH && player.getEquippedRod() != null) {
                    AudioManager.getInstance().playSFX("sfx_button");
                    player.unequip(EquipmentType.ROD); return;
                }
                float lineBtnY = leftY + leftH - 280f + 15f;
                if (m.y >= lineBtnY && m.y <= lineBtnY + uBtnH && player.getEquippedLine() != null) {
                    AudioManager.getInstance().playSFX("sfx_button");
                    player.unequip(EquipmentType.LINE); return;
                }
                float hookBtnY = leftY + leftH - 410f + 15f;
                if (m.y >= hookBtnY && m.y <= hookBtnY + uBtnH && player.getEquippedHook() != null) {
                    AudioManager.getInstance().playSFX("sfx_button");
                    player.unequip(EquipmentType.HOOK); return;
                }
            }

            // Equip / Unequip buttons in inventory
            float btnW = 95f;
            float btnH = 38f;
            float btnX = rightX + 15f + rowW - btnW - 12f;

            for (int i = 0; i < MAX_VISIBLE; i++) {
                if (scrollOffset + i < cachedFilteredInventory.size()) {
                    Equipment eq = cachedFilteredInventory.get(scrollOffset + i);
                    float ry = invStartY - i * 90f;
                    float btnY = ry + (80f - btnH) / 2f;

                    if (m.x >= btnX && m.x <= btnX + btnW && m.y >= btnY && m.y <= btnY + btnH) {
                        AudioManager.getInstance().playSFX("sfx_button");
                        if (isEquipped(eq)) {
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

    private boolean isEquipped(Equipment eq) {
        if (eq == null) return false;
        return (player.getEquippedRod() != null && eq.instanceId.equals(player.getEquippedRod().instanceId)) ||
               (player.getEquippedLine() != null && eq.instanceId.equals(player.getEquippedLine().instanceId)) ||
               (player.getEquippedHook() != null && eq.instanceId.equals(player.getEquippedHook().instanceId));
    }

    private Equipment getEquippedByType(EquipmentType type) {
        if (type == EquipmentType.ROD) return player.getEquippedRod();
        if (type == EquipmentType.LINE) return player.getEquippedLine();
        return player.getEquippedHook();
    }

    private void drawTooltip(Equipment eq, float x, float y) {
        float tipW = 320f;
        float tipH = 200f + eq.substats.size() * 26f;

        if (x + tipW > W) x -= tipW + 20f;
        else x += 20f;
        if (y - tipH < 0) y = tipH + 20f;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(0.06f, 0.08f, 0.14f, 0.98f);
        drawRoundedRect(sr, x, y - tipH, tipW, tipH, 8f);
        sr.setColor(eq.rarity.color);
        drawRectOutline(sr, x, y - tipH, tipW, tipH, 2f);
        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();
        titleFont.setColor(eq.rarity.color);
        titleFont.draw(game.batch, eq.name, x + 16f, y - 20f);

        font.setColor(Color.WHITE);
        font.draw(game.batch, "Cấp " + eq.level + " (" + eq.rarity.name() + ") • Bộ: " + eq.setName, x + 16f, y - 50f);

        font.setColor(1f, 0.85f, 0.35f, 1f);
        font.draw(game.batch, "[CHỈ SỐ CHÍNH]", x + 16f, y - 80f);

        font.setColor(0.4f, 1f, 0.7f, 1f);
        String stats = getEqMainStats(eq);
        font.draw(game.batch, stats, x + 16f, y - 105f);

        font.setColor(0.4f, 0.85f, 1f, 1f);
        font.draw(game.batch, "[CHỈ SỐ NỘI TẠI PHỤ]", x + 16f, y - 135f);

        font.setColor(0.85f, 0.9f, 0.95f, 1f);
        float sy = y - 162f;
        for (Substat sub : eq.substats) {
            font.draw(game.batch, "• " + sub.toString(), x + 16f, sy);
            sy -= 26f;
        }

        game.batch.end();
    }

    private String getEqMainStats(Equipment eq) {
        if (eq.type == EquipmentType.ROD) {
            return String.format("Sát thương: +%.1f, Thể lực: +%.1f", eq.mainStat1, eq.mainStat2);
        } else if (eq.type == EquipmentType.LINE) {
            return String.format("Lực căng: +%.1f, Kháng đứt: +%.1f", eq.mainStat1, eq.mainStat2);
        } else {
            return String.format("Tỉ lệ cá xịn: +%.1f%%, Giữ mồi: +%.1f%%", eq.mainStat1, eq.mainStat2);
        }
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
        Gdx.input.setInputProcessor(new com.badlogic.gdx.InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                scrollOffset += (int)amountY * 2;
                List<Equipment> filtered = getFilteredInventory();
                int maxScroll = Math.max(0, filtered.size() - MAX_VISIBLE);
                if (scrollOffset < 0) scrollOffset = 0;
                if (scrollOffset > maxScroll) scrollOffset = maxScroll;
                return true;
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

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

