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
import com.fishingrpg.game.ui.EquipmentIconRenderer;

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
    private static final int MAX_VISIBLE = 6;
    private static final float ROW_H = 78f;
    private static final float ROW_SPACING = 84f;
    private static final float INV_START_Y = 538f - ROW_H;

    private enum Tab { ALL, ROD, LINE, HOOK }
    private Tab currentTab = Tab.ALL;

    public enum SortMode {
        TIME("Thời Gian"),
        RARITY("Độ Hiếm"),
        POWER("Sức Mạnh");

        public final String label;
        SortMode(String label) { this.label = label; }
    }
    private SortMode currentSort = SortMode.TIME;
    
    private Equipment hoveredEq = null;
    private float hoverX = 0, hoverY = 0;
    private List<Equipment> cachedFilteredInventory = new ArrayList<>();

    private boolean hoverBack = false;
    private boolean hoverGearDock = false;
    private boolean hoverAutoEquip = false;
    private boolean hoverUnequipAll = false;
    private float animTimer = 0f;

    // Ambient floating particles
    private static final int PARTICLE_COUNT = 30;
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
            partSpeed[i] = MathUtils.random(12f, 30f);
            partAlpha[i] = MathUtils.random(0.25f, 0.65f);
        }
    }

    @Override
    public void render(float delta) {
        if (Gdx.graphics.getWidth() <= 0 || Gdx.graphics.getHeight() <= 0) return;
        animTimer += delta;
        update(delta);

        // Update ambient floating particles
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            partY[i] += partSpeed[i] * delta;
            partX[i] += MathUtils.sin(animTimer * 1.5f + i) * 8f * delta;
            if (partY[i] > H + 10f) {
                partY[i] = -10f;
                partX[i] = MathUtils.random(0, W);
            }
        }

        ScreenUtils.clear(new Color(0.03f, 0.05f, 0.08f, 1f));
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        Vector3 m = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(m);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);

        // --- 1. UPGRADED ARMORY BACKDROP GRADIENT ---
        // Rich multi-stop midnight abyss to deep cobalt glow
        sr.rect(0, 0, W, H,
            new Color(0.02f, 0.04f, 0.07f, 1f), new Color(0.02f, 0.04f, 0.07f, 1f),
            new Color(0.06f, 0.10f, 0.17f, 1f), new Color(0.06f, 0.10f, 0.17f, 1f)
        );

        // Center ambient oceanic illumination halo
        sr.setColor(0.08f, 0.22f, 0.36f, 0.18f);
        sr.circle(W / 2f, H / 2f, 420f, 48);
        sr.setColor(0.12f, 0.35f, 0.55f, 0.10f);
        sr.circle(W / 2f, H / 2f, 260f, 36);

        // Subtle high-tech perspective floor grid lines
        sr.setColor(0.18f, 0.32f, 0.52f, 0.08f);
        for (float gx = 40; gx < W; gx += 80) sr.rect(gx, 0, 1.2f, H);
        for (float gy = 30; gy < H; gy += 70) sr.rect(0, gy, W, 1.2f);

        // Luminous floating mana embers with glowing cores
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            float pulse = 0.55f + 0.45f * MathUtils.sin(animTimer * 2.5f + i);
            sr.setColor(0.35f, 0.85f, 1f, partAlpha[i] * pulse * 0.4f);
            sr.circle(partX[i], partY[i], 3.5f);
            sr.setColor(1f, 1f, 1f, partAlpha[i] * pulse * 0.9f);
            sr.circle(partX[i], partY[i], 1.2f);
        }

        // --- 2. HEADER DOCK BAR ---
        sr.setColor(0.05f, 0.08f, 0.13f, 0.96f);
        sr.rect(0, H - 76f, W, 76f);
        // Golden top highlight
        sr.setColor(1f, 0.82f, 0.25f, 0.65f);
        sr.rect(0, H - 3f, W, 3f);
        // Cyan neon bottom border
        sr.setColor(0.20f, 0.65f, 0.90f, 0.75f);
        sr.rect(0, H - 78f, W, 2.5f);

        // Back Button
        hoverBack = (m.x >= 25f && m.x <= 155f && m.y >= H - 62f && m.y <= H - 20f);
        sr.setColor(hoverBack ? new Color(0.25f, 0.38f, 0.55f, 0.98f) : new Color(0.11f, 0.16f, 0.24f, 0.92f));
        drawRoundedRect(sr, 25, H - 62, 130, 42, 8);
        sr.setColor(hoverBack ? new Color(0.7f, 0.9f, 1f, 1f) : new Color(0.35f, 0.55f, 0.75f, 0.75f));
        drawRectOutline(sr, 25, H - 62, 130, 42, 1.5f);

        // Top Right Player Level & Gear Power Dock (Interactive Popover Trigger)
        float gearDockW = 345f;
        float gearDockH = 42f;
        float gearDockX = W - gearDockW - 25f;
        float gearDockY = H - 62f;
        hoverGearDock = (m.x >= gearDockX && m.x <= gearDockX + gearDockW && m.y >= gearDockY && m.y <= gearDockY + gearDockH);

        float dockPulse = hoverGearDock ? (0.85f + 0.15f * MathUtils.sin(animTimer * 6f)) : 1f;
        sr.setColor(hoverGearDock ? new Color(0.14f * dockPulse, 0.22f * dockPulse, 0.34f * dockPulse, 0.98f)
                                  : new Color(0.08f, 0.12f, 0.19f, 0.95f));
        drawRoundedRect(sr, gearDockX, gearDockY, gearDockW, gearDockH, 8);
        sr.setColor(hoverGearDock ? new Color(1f * dockPulse, 0.85f * dockPulse, 0.35f * dockPulse, 1f)
                                  : new Color(0.3f, 0.75f, 0.6f, 0.8f));
        drawRectOutline(sr, gearDockX, gearDockY, gearDockW, gearDockH, hoverGearDock ? 2f : 1.5f);

        // --- 3. LEFT PANEL: EQUIPPED GEAR (Width 470px) ---
        float leftX = 25f;
        float leftY = 25f;
        float leftW = 470f;
        float leftH = H - 110f;

        sr.setColor(0.06f, 0.09f, 0.15f, 0.96f);
        drawRoundedRect(sr, leftX, leftY, leftW, leftH, 12);
        sr.setColor(0.20f, 0.35f, 0.52f, 0.75f);
        drawRectOutline(sr, leftX, leftY, leftW, leftH, 1.5f);

        // Header Pill Tag on Left Panel
        sr.setColor(0.11f, 0.16f, 0.26f, 0.95f);
        drawRoundedRect(sr, leftX + 15f, leftY + leftH - 35f, 235f, 26f, 6f);
        sr.setColor(0.35f, 0.70f, 0.95f, 0.7f);
        drawRectOutline(sr, leftX + 15f, leftY + leftH - 35f, 235f, 26f, 1f);

        // QoL Button: "Tháo Hết" (Unequip All)
        float uAllW = 85f;
        float uAllH = 26f;
        float uAllX = leftX + leftW - uAllW - 15f;
        float uAllY = leftY + leftH - 35f;
        hoverUnequipAll = (m.x >= uAllX && m.x <= uAllX + uAllW && m.y >= uAllY && m.y <= uAllY + uAllH);
        sr.setColor(hoverUnequipAll ? new Color(0.58f, 0.20f, 0.22f, 0.98f) : new Color(0.32f, 0.14f, 0.16f, 0.90f));
        drawRoundedRect(sr, uAllX, uAllY, uAllW, uAllH, 6);
        sr.setColor(hoverUnequipAll ? new Color(1f, 0.65f, 0.65f, 1f) : new Color(0.75f, 0.35f, 0.35f, 0.75f));
        drawRectOutline(sr, uAllX, uAllY, uAllW, uAllH, 1.2f);

        // Equipped Gear Slot Cards (Slot 1: Rod, Slot 2: Line, Slot 3: Hook)
        drawEquipSlot(sr, leftX + 15f, leftY + leftH - 165f, player.getEquippedRod(), EquipmentType.ROD, m);
        drawEquipSlot(sr, leftX + 15f, leftY + leftH - 295f, player.getEquippedLine(), EquipmentType.LINE, m);
        drawEquipSlot(sr, leftX + 15f, leftY + leftH - 425f, player.getEquippedHook(), EquipmentType.HOOK, m);

        // Set Bonus Status Plate (Bottom of Left Panel)
        float setPlateX = leftX + 15f;
        float setPlateY = leftY + 15f;
        float setPlateW = leftW - 30f;
        float setPlateH = 100f;

        boolean setBonusActive = player.isSetBonusActive() && player.getEquippedRod() != null;
        if (setBonusActive) {
            float pulse = 0.82f + 0.18f * MathUtils.sin(animTimer * 3.5f);
            sr.setColor(0.36f * pulse, 0.26f * pulse, 0.08f * pulse, 0.96f);
            drawRoundedRect(sr, setPlateX, setPlateY, setPlateW, setPlateH, 8);
            sr.setColor(1f * pulse, 0.85f * pulse, 0.25f * pulse, 1f);
            drawRectOutline(sr, setPlateX, setPlateY, setPlateW, setPlateH, 2f);
        } else {
            sr.setColor(0.08f, 0.11f, 0.17f, 0.90f);
            drawRoundedRect(sr, setPlateX, setPlateY, setPlateW, setPlateH, 8);
            sr.setColor(0.20f, 0.28f, 0.38f, 0.65f);
            drawRectOutline(sr, setPlateX, setPlateY, setPlateW, setPlateH, 1f);
        }

        // --- 4. RIGHT PANEL: INVENTORY (Width 740px) ---
        float rightX = 515f;
        float rightY = 25f;
        float rightW = W - rightX - 25f;
        float rightH = H - 110f;

        sr.setColor(0.06f, 0.09f, 0.15f, 0.96f);
        drawRoundedRect(sr, rightX, rightY, rightW, rightH, 12);
        sr.setColor(0.20f, 0.35f, 0.52f, 0.75f);
        drawRectOutline(sr, rightX, rightY, rightW, rightH, 1.5f);

        // Row 1: Filter Tabs
        float tabW = 105f;
        float tabH = 34f;
        float tabGap = 8f;
        float tabStartX = rightX + 15f;
        float tabY = rightY + rightH - 44f;

        Tab[] tabs = Tab.values();
        for (int t = 0; t < tabs.length; t++) {
            float tx = tabStartX + t * (tabW + tabGap);
            boolean isCur = (currentTab == tabs[t]);
            boolean hoverTab = (m.x >= tx && m.x <= tx + tabW && m.y >= tabY && m.y <= tabY + tabH);

            sr.setColor(isCur ? new Color(0.18f, 0.44f, 0.68f, 0.98f) : (hoverTab ? new Color(0.13f, 0.25f, 0.40f, 0.92f) : new Color(0.09f, 0.14f, 0.21f, 0.88f)));
            drawRoundedRect(sr, tx, tabY, tabW, tabH, 6);
            sr.setColor(isCur ? new Color(0.55f, 0.92f, 1f, 1f) : (hoverTab ? new Color(0.38f, 0.65f, 0.85f, 0.85f) : new Color(0.20f, 0.30f, 0.42f, 0.65f)));
            drawRectOutline(sr, tx, tabY, tabW, tabH, isCur ? 2f : 1f);
        }

        // Row 2: Sort Bar Buttons + QoL Auto-Equip Button
        float sortY = tabY - 36f;
        float sortH = 26f;
        float sortStartX = rightX + 85f;
        float sortBtnW = 105f;
        float sortGap = 8f;

        SortMode[] sortModes = SortMode.values();
        for (int s = 0; s < sortModes.length; s++) {
            float sx = sortStartX + s * (sortBtnW + sortGap);
            boolean isCurSort = (currentSort == sortModes[s]);
            boolean hoverSort = (m.x >= sx && m.x <= sx + sortBtnW && m.y >= sortY && m.y <= sortY + sortH);

            if (isCurSort) {
                sr.setColor(0.18f, 0.42f, 0.58f, 0.98f);
                drawRoundedRect(sr, sx, sortY, sortBtnW, sortH, 5);
                sr.setColor(0.45f, 0.92f, 1f, 1f);
                drawRectOutline(sr, sx, sortY, sortBtnW, sortH, 1.5f);
            } else {
                sr.setColor(hoverSort ? new Color(0.15f, 0.24f, 0.35f, 0.92f) : new Color(0.09f, 0.13f, 0.20f, 0.82f));
                drawRoundedRect(sr, sx, sortY, sortBtnW, sortH, 5);
                sr.setColor(hoverSort ? new Color(0.38f, 0.60f, 0.80f, 0.75f) : new Color(0.20f, 0.28f, 0.38f, 0.55f));
                drawRectOutline(sr, sx, sortY, sortBtnW, sortH, 1f);
            }
        }

        // QoL Button: "Mặc Nhanh" (Auto-Equip Strongest Gear)
        float autoBtnW = 125f;
        float autoBtnH = 26f;
        float autoBtnX = rightX + rightW - autoBtnW - 15f;
        float autoBtnY = sortY;
        hoverAutoEquip = (m.x >= autoBtnX && m.x <= autoBtnX + autoBtnW && m.y >= autoBtnY && m.y <= autoBtnY + autoBtnH);
        sr.setColor(hoverAutoEquip ? new Color(0.95f, 0.65f, 0.15f, 1f) : new Color(0.72f, 0.45f, 0.10f, 0.95f));
        drawRoundedRect(sr, autoBtnX, autoBtnY, autoBtnW, autoBtnH, 5);
        sr.setColor(hoverAutoEquip ? new Color(1f, 0.95f, 0.5f, 1f) : new Color(1f, 0.82f, 0.25f, 0.85f));
        drawRectOutline(sr, autoBtnX, autoBtnY, autoBtnW, autoBtnH, 1.5f);

        // Separator line under sort bar
        sr.setColor(0.20f, 0.32f, 0.48f, 0.50f);
        sr.rect(rightX + 15f, 544f, rightW - 30f, 1.2f);

        // Row 3: Inventory Items List (6 visible cards with 52x52 icon thumbnail!)
        List<Equipment> filteredInv = cachedFilteredInventory;
        float rowW = rightW - 40f;
        float rowH = ROW_H;
        float rowSpacing = ROW_SPACING;
        float invStartY = INV_START_Y;

        for (int i = 0; i < MAX_VISIBLE; i++) {
            int idx = scrollOffset + i;
            if (idx < filteredInv.size()) {
                Equipment eq = filteredInv.get(idx);
                float ry = invStartY - i * rowSpacing;

                boolean isEq = isEquipped(eq);
                boolean hoverRow = (m.x >= rightX + 15f && m.x <= rightX + 15f + rowW && m.y >= ry && m.y <= ry + rowH);

                // Row Base
                sr.setColor(hoverRow ? new Color(0.14f, 0.21f, 0.30f, 0.98f) : new Color(0.09f, 0.13f, 0.19f, 0.92f));
                drawRoundedRect(sr, rightX + 15f, ry, rowW, rowH, 8);

                // Rarity left stripe
                sr.setColor(eq.rarity.color);
                drawRoundedRect(sr, rightX + 15f, ry, 5f, rowH, 3f);

                // 52x52 Item Icon Thumbnail Box
                float iconBoxSize = 52f;
                float iconBoxX = rightX + 24f;
                float iconBoxY = ry + (rowH - iconBoxSize) / 2f;
                sr.setColor(0.05f, 0.07f, 0.12f, 0.95f);
                drawRoundedRect(sr, iconBoxX, iconBoxY, iconBoxSize, iconBoxSize, 6f);
                sr.setColor(eq.rarity.color);
                drawRectOutline(sr, iconBoxX, iconBoxY, iconBoxSize, iconBoxSize, 1.4f);

                // Render Item's Unique Vector Icon
                EquipmentIconRenderer.draw(sr, eq, eq.type, iconBoxX + iconBoxSize / 2f, iconBoxY + iconBoxSize / 2f, 19f, eq.rarity.color);

                // Row outline
                sr.setColor(hoverRow ? new Color(0.45f, 0.75f, 1f, 0.85f) : new Color(0.18f, 0.26f, 0.38f, 0.65f));
                drawRectOutline(sr, rightX + 15f, ry, rowW, rowH, 1.2f);

                // Equip / Unequip button
                float btnW = 92f;
                float btnH = 36f;
                float btnX = rightX + 15f + rowW - btnW - 12f;
                float btnY = ry + (rowH - btnH) / 2f;

                boolean hoverBtn = (m.x >= btnX && m.x <= btnX + btnW && m.y >= btnY && m.y <= btnY + btnH);
                if (isEq) {
                    sr.setColor(hoverBtn ? new Color(0.65f, 0.25f, 0.25f, 1f) : new Color(0.48f, 0.18f, 0.18f, 0.95f));
                    drawRoundedRect(sr, btnX, btnY, btnW, btnH, 6);
                    sr.setColor(hoverBtn ? new Color(1f, 0.7f, 0.7f, 1f) : new Color(0.85f, 0.38f, 0.38f, 0.85f));
                    drawRectOutline(sr, btnX, btnY, btnW, btnH, 1.5f);
                } else {
                    sr.setColor(hoverBtn ? new Color(0.24f, 0.62f, 0.40f, 1f) : new Color(0.16f, 0.45f, 0.28f, 0.95f));
                    drawRoundedRect(sr, btnX, btnY, btnW, btnH, 6);
                    sr.setColor(hoverBtn ? new Color(0.65f, 1f, 0.75f, 1f) : new Color(0.38f, 0.90f, 0.55f, 0.85f));
                    drawRectOutline(sr, btnX, btnY, btnW, btnH, 1.5f);
                }
            }
        }

        // Scrollbar
        if (filteredInv.size() > MAX_VISIBLE) {
            float scrollTrackY = 66f;
            float scrollTrackH = 472f;
            float scrollBarX = rightX + rightW - 14f;

            sr.setColor(0.08f, 0.12f, 0.18f, 0.85f);
            drawRoundedRect(sr, scrollBarX, scrollTrackY, 6f, scrollTrackH, 3f);

            float scrollRatio = (float)scrollOffset / (filteredInv.size() - MAX_VISIBLE);
            float barHeight = Math.max(35f, scrollTrackH * ((float)MAX_VISIBLE / filteredInv.size()));
            float barY = scrollTrackY + (scrollTrackH - barHeight) * (1f - scrollRatio);

            sr.setColor(0.35f, 0.72f, 1f, 0.95f);
            drawRoundedRect(sr, scrollBarX, barY, 6f, barHeight, 3f);
        }

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // --- DRAW BATCH TEXTS ---
        game.batch.begin();

        // Header Title
        titleFont.setColor(0.98f, 0.88f, 0.45f, 1f);
        titleFont.getData().setScale(0.92f);
        layout.setText(titleFont, "KHO TRANG BỊ CÂU CÁ");
        titleFont.draw(game.batch, layout, W / 2f - layout.width / 2f, H - 35f);
        titleFont.getData().setScale(1f);

        // Back button text
        font.setColor(hoverBack ? Color.WHITE : new Color(0.85f, 0.92f, 1f, 0.92f));
        layout.setText(font, "← QUAY LẠI");
        font.draw(game.batch, layout, 25f + (130f - layout.width) / 2f, H - 36f);

        // Top Right Gear Power Dock Text (with interactive cue)
        font.setColor(hoverGearDock ? new Color(1f, 0.95f, 0.5f, 1f) : new Color(0.4f, 1f, 0.7f, 1f));
        font.getData().setScale(0.85f);
        String pStr = "Cấp " + player.getLevel() + "  •  Sát Thương: " + (int)player.getBasePullDamage() + "  [ Xem Chỉ Số ]";
        layout.setText(font, pStr);
        font.draw(game.batch, layout, gearDockX + (gearDockW - layout.width) / 2f, gearDockY + 26f);
        font.getData().setScale(1f);

        // Left Panel Header Text
        font.setColor(0.85f, 0.92f, 1f, 0.95f);
        font.getData().setScale(0.82f);
        font.draw(game.batch, "TRANG BỊ ĐANG MẶC (3 MÓN)", leftX + 26f, leftY + leftH - 18f);

        // Unequip All button text
        font.setColor(hoverUnequipAll ? Color.WHITE : new Color(1f, 0.75f, 0.75f, 0.95f));
        font.getData().setScale(0.75f);
        layout.setText(font, "Tháo Hết");
        font.draw(game.batch, layout, uAllX + (uAllW - layout.width) / 2f, uAllY + 18f);
        font.getData().setScale(1f);

        // Render Equipped Slot Texts
        drawEquipSlotText(leftX + 15f, leftY + leftH - 165f, player.getEquippedRod(), EquipmentType.ROD);
        drawEquipSlotText(leftX + 15f, leftY + leftH - 295f, player.getEquippedLine(), EquipmentType.LINE);
        drawEquipSlotText(leftX + 15f, leftY + leftH - 425f, player.getEquippedHook(), EquipmentType.HOOK);

        // Set Bonus Status Text
        if (setBonusActive) {
            Equipment rod = player.getEquippedRod();
            String desc = com.fishingrpg.game.entities.equipment.EquipmentGenerator.getSetBonusDesc(rod.setName);
            font.setColor(1f, 0.90f, 0.35f, 1f);
            layout.setText(font, "★ KÍCH HOẠT SET: " + rod.setName.toUpperCase() + " (3/3)");
            font.draw(game.batch, layout, setPlateX + (setPlateW - layout.width) / 2f, setPlateY + setPlateH - 22f);

            font.setColor(Color.WHITE);
            font.getData().setScale(0.85f);
            layout.setText(font, desc, Color.WHITE, setPlateW - 20f, Align.center, true);
            font.draw(game.batch, desc, setPlateX + 10f, setPlateY + 52f, setPlateW - 20f, Align.center, true);
            font.getData().setScale(1f);
        } else {
            font.setColor(0.65f, 0.70f, 0.80f, 1f);
            layout.setText(font, "SET BONUS: CHƯA ĐỦ 3 MÓN");
            font.draw(game.batch, layout, setPlateX + (setPlateW - layout.width) / 2f, setPlateY + setPlateH - 25f);

            font.setColor(0.50f, 0.58f, 0.68f, 0.88f);
            font.getData().setScale(0.85f);
            layout.setText(font, "Mặc cùng 1 bộ Cần, Dây, Lưỡi để mở khóa nội tại kích hoạt");
            font.draw(game.batch, layout, setPlateX + (setPlateW - layout.width) / 2f, setPlateY + 45f);
            font.getData().setScale(1f);
        }

        // Right Panel Tab Texts
        String[] tabNames = {"TẤT CẢ", "CẦN CÂU", "DÂY CƯỚC", "LƯỠI CÂU"};

        font.getData().setScale(0.85f);
        for (int t = 0; t < tabNames.length; t++) {
            float tx = tabStartX + t * (tabW + tabGap);
            boolean isCur = (currentTab == tabs[t]);
            font.setColor(isCur ? Color.WHITE : new Color(0.72f, 0.82f, 0.92f, 0.82f));
            layout.setText(font, tabNames[t]);
            font.draw(game.batch, layout, tx + (tabW - layout.width) / 2f, tabY + 23f);
        }
        font.getData().setScale(1f);

        // Inventory Counter
        font.setColor(0.65f, 0.85f, 1f, 0.90f);
        font.getData().setScale(0.82f);
        String invCountStr = "Túi đồ: " + player.getEquipmentInventory().size() + " món (Hiển thị: " + filteredInv.size() + ")";
        layout.setText(font, invCountStr);
        font.draw(game.batch, layout, rightX + rightW - layout.width - 20f, tabY + 23f);
        font.getData().setScale(1f);

        // Sort Bar Texts
        font.setColor(0.65f, 0.75f, 0.88f, 0.85f);
        font.getData().setScale(0.78f);
        font.draw(game.batch, "Sắp xếp:", rightX + 15f, sortY + 18f);

        for (int s = 0; s < sortModes.length; s++) {
            float sx = sortStartX + s * (sortBtnW + sortGap);
            boolean isCurSort = (currentSort == sortModes[s]);
            boolean hoverSort = (m.x >= sx && m.x <= sx + sortBtnW && m.y >= sortY && m.y <= sortY + 26f);

            font.setColor(isCurSort ? new Color(1f, 0.90f, 0.45f, 1f) : (hoverSort ? Color.WHITE : new Color(0.75f, 0.85f, 0.95f, 0.85f)));
            font.getData().setScale(0.76f);
            String sortLabel = (isCurSort ? "• " : "") + sortModes[s].label;
            layout.setText(font, sortLabel);
            font.draw(game.batch, layout, sx + (sortBtnW - layout.width) / 2f, sortY + 18f);
        }

        // QoL Button Text: "Mặc Nhanh"
        font.setColor(hoverAutoEquip ? Color.WHITE : new Color(1f, 0.95f, 0.75f, 1f));
        font.getData().setScale(0.78f);
        layout.setText(font, "✦ MẶC NHANH");
        font.draw(game.batch, layout, autoBtnX + (autoBtnW - layout.width) / 2f, autoBtnY + 18f);
        font.getData().setScale(1f);

        // Inventory List Items Text (6 items)
        for (int i = 0; i < MAX_VISIBLE; i++) {
            int idx = scrollOffset + i;
            if (idx < filteredInv.size()) {
                Equipment eq = filteredInv.get(idx);
                float ry = invStartY - i * rowSpacing;
                boolean isEq = isEquipped(eq);

                // Name & Rarity (Rendered next to 52x52 icon thumbnail)
                titleFont.setColor(eq.rarity.color);
                layout.setText(titleFont, eq.name);
                float maxTitleW = 290f;
                if (layout.width > maxTitleW) {
                    titleFont.getData().setScale(0.68f * (maxTitleW / layout.width));
                } else {
                    titleFont.getData().setScale(0.68f);
                }
                titleFont.draw(game.batch, eq.name, rightX + 88f, ry + rowH - 10f);
                titleFont.getData().setScale(1f);

                // Type & Set Tag - spaced 26px below name for clean separation
                font.setColor(0.75f, 0.82f, 0.92f, 0.88f);
                font.getData().setScale(0.70f);
                String typeStr = (eq.type == EquipmentType.ROD ? "Cần" : eq.type == EquipmentType.LINE ? "Cước" : "Lưỡi") + " • Bộ: " + eq.setName;
                font.draw(game.batch, typeStr, rightX + 88f, ry + rowH - 36f);

                // Main Stats & Comparative Stat Diff - spaced 22px below type/set
                String stats = getEqMainStats(eq);
                font.setColor(0.4f, 1f, 0.6f, 1f);
                font.getData().setScale(0.70f);
                font.draw(game.batch, stats, rightX + 88f, ry + rowH - 58f);

                // Diff indicator if not equipped
                if (!isEq) {
                    Equipment currentlyEquipped = getEquippedByType(eq.type);
                    if (currentlyEquipped != null) {
                        float diff = eq.mainStat1 - currentlyEquipped.mainStat1;
                        if (Math.abs(diff) >= 0.5f) {
                            String diffStr = (diff > 0 ? "▲ +" : "▼ ") + (int)diff;
                            font.setColor(diff > 0 ? new Color(0.3f, 1f, 0.5f, 1f) : new Color(1f, 0.35f, 0.35f, 1f));
                            font.draw(game.batch, diffStr, rightX + 410f, ry + rowH - 58f);
                        }
                    }
                }
                font.getData().setScale(1f);

                // Button Text
                float btnW = 92f;
                float btnX = rightX + 15f + rowW - btnW - 12f;
                float btnY = ry + (rowH - 36f) / 2f;

                font.setColor(Color.WHITE);
                font.getData().setScale(0.82f);
                String btnText = isEq ? "Tháo Ra" : "Mặc Vào";
                layout.setText(font, btnText);
                font.draw(game.batch, layout, btnX + (btnW - layout.width) / 2f, btnY + 23f);
                font.getData().setScale(1f);
            }
        }

        game.batch.end();

        // --- 5. POPOVER & TOOLTIP RENDERING OVER EVERYTHING ---
        // Top-Right Gear Dock Total Stats Popover
        if (hoverGearDock) {
            float popW = 430f;
            float popX = W - popW - 20f;
            drawTotalStatsPopover(sr, popX, gearDockY - 8f);
            game.batch.begin();
            drawTotalStatsPopoverText(popX, gearDockY - 8f);
            game.batch.end();
        }

        // Expanded Equipment Hover Tooltip
        if (hoveredEq != null && !hoverGearDock) {
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
        sr.setColor(0.06f, 0.08f, 0.14f, 0.95f);
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
            sr.setColor(hoverUBtn ? new Color(0.65f, 0.25f, 0.25f, 1f) : new Color(0.45f, 0.18f, 0.18f, 0.95f));
            drawRoundedRect(sr, uBtnX, uBtnY, uBtnW, uBtnH, 6);
            sr.setColor(hoverUBtn ? new Color(1f, 0.65f, 0.65f, 1f) : new Color(0.75f, 0.35f, 0.35f, 0.8f));
            drawRectOutline(sr, uBtnX, uBtnY, uBtnW, uBtnH, 1.5f);
        } else {
            sr.setColor(0.20f, 0.26f, 0.36f, 0.5f);
            drawRectOutline(sr, sqX, sqY, sqSize, sqSize, 1f);
            drawRectOutline(sr, x, y, w, h, 1f);
        }

        // Draw Unique Slot Icon from EquipmentIconRenderer
        EquipmentIconRenderer.draw(sr, eq, type, sqX + sqSize / 2f, sqY + sqSize / 2f, 32f, eq != null ? eq.rarity.color : Color.GRAY);
    }

    private void drawEquipSlotText(float x, float y, Equipment eq, EquipmentType type) {
        float w = 440f;
        String typeName = type == EquipmentType.ROD ? "CẦN CÂU" : type == EquipmentType.LINE ? "DÂY CƯỚC" : "LƯỠI CÂU";

        if (eq != null) {
            titleFont.setColor(eq.rarity.color);
            layout.setText(titleFont, eq.name);
            float targetWidth = w - 190f;
            if (layout.width > targetWidth) {
                titleFont.getData().setScale(0.76f * (targetWidth / layout.width));
            } else {
                titleFont.getData().setScale(0.76f);
            }
            titleFont.draw(game.batch, eq.name, x + 115f, y + 98f);
            titleFont.getData().setScale(1f);

            font.setColor(0.72f, 0.82f, 0.92f, 0.88f);
            font.getData().setScale(0.76f);
            font.draw(game.batch, typeName + " • Bộ: " + eq.setName + " (" + eq.rarity.name() + ")", x + 115f, y + 66f);

            font.setColor(0.4f, 1f, 0.6f, 1f);
            String stats = getEqMainStats(eq);
            font.getData().setScale(0.76f);
            layout.setText(font, stats);
            float uBtnW = 80f;
            float uBtnX = x + w - uBtnW - 15f;
            float maxStatsW = uBtnX - (x + 115f) - 12f;
            if (layout.width > maxStatsW) {
                font.getData().setScale(0.76f * (maxStatsW / layout.width));
            }
            font.draw(game.batch, stats, x + 115f, y + 36f);
            font.getData().setScale(1f);

            // Unequip button text
            font.setColor(Color.WHITE);
            font.getData().setScale(0.85f);
            layout.setText(font, "Tháo");
            font.draw(game.batch, layout, uBtnX + (uBtnW - layout.width) / 2f, y + 36f);
            font.getData().setScale(1f);
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
            if (m.y >= leftY + leftH - 165f && m.y <= leftY + leftH - 50f && player.getEquippedRod() != null) hoveredEq = player.getEquippedRod();
            if (m.y >= leftY + leftH - 295f && m.y <= leftY + leftH - 180f && player.getEquippedLine() != null) hoveredEq = player.getEquippedLine();
            if (m.y >= leftY + leftH - 425f && m.y <= leftY + leftH - 310f && player.getEquippedHook() != null) hoveredEq = player.getEquippedHook();
        }

        // Hover inventory
        float rightX = 515f;
        float rightY = 25f;
        float rightH = H - 110f;
        float rowW = W - rightX - 65f;
        float rowH = ROW_H;
        float rowSpacing = ROW_SPACING;
        float invStartY = INV_START_Y;

        if (m.x >= rightX + 15f && m.x <= rightX + 15f + rowW) {
            for (int i = 0; i < MAX_VISIBLE; i++) {
                if (scrollOffset + i < cachedFilteredInventory.size()) {
                    float ry = invStartY - i * rowSpacing;
                    if (m.y >= ry && m.y <= ry + rowH) {
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

        // Scrollbar click & drag
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            float scrollTrackY = 66f;
            float scrollTrackH = 472f;
            float scrollBarX = rightX + (W - rightX - 25f) - 14f;
            if (m.x >= scrollBarX - 12f && m.x <= scrollBarX + 18f && m.y >= scrollTrackY && m.y <= scrollTrackY + scrollTrackH) {
                float relY = (scrollTrackY + scrollTrackH) - m.y;
                float pct = MathUtils.clamp(relY / scrollTrackH, 0f, 1f);
                int maxScroll = Math.max(0, cachedFilteredInventory.size() - MAX_VISIBLE);
                scrollOffset = Math.round(pct * maxScroll);
            }
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            // QoL: Unequip All button click
            if (hoverUnequipAll) {
                unequipAll();
                return;
            }

            // QoL: Auto-Equip button click
            if (hoverAutoEquip) {
                autoEquipBest();
                return;
            }

            // Filter Tabs
            float tabW = 105f;
            float tabH = 34f;
            float tabGap = 8f;
            float tabStartX = rightX + 15f;
            float tabY = rightY + rightH - 44f;

            Tab[] tabs = Tab.values();
            for (int t = 0; t < tabs.length; t++) {
                float tx = tabStartX + t * (tabW + tabGap);
                if (m.x >= tx && m.x <= tx + tabW && m.y >= tabY && m.y <= tabY + tabH) {
                    if (currentTab != tabs[t]) {
                        AudioManager.getInstance().playSFX("sfx_button");
                        currentTab = tabs[t];
                        scrollOffset = 0;
                        return;
                    }
                }
            }

            // Sort Buttons
            float sortY = tabY - 36f;
            float sortH = 26f;
            float sortStartX = rightX + 85f;
            float sortBtnW = 105f;
            float sortGap = 8f;

            SortMode[] sortModes = SortMode.values();
            for (int s = 0; s < sortModes.length; s++) {
                float sx = sortStartX + s * (sortBtnW + sortGap);
                if (m.x >= sx && m.x <= sx + sortBtnW && m.y >= sortY && m.y <= sortY + sortH) {
                    if (currentSort != sortModes[s]) {
                        AudioManager.getInstance().playSFX("sfx_button");
                        currentSort = sortModes[s];
                        scrollOffset = 0;
                        return;
                    }
                }
            }

            // Unequip buttons on equipped slots
            float w = 440f;
            float uBtnW = 80f;
            float uBtnH = 32f;
            float uBtnX = leftX + 15f + w - uBtnW - 15f;

            if (m.x >= uBtnX && m.x <= uBtnX + uBtnW) {
                float rodBtnY = leftY + leftH - 165f + 15f;
                if (m.y >= rodBtnY && m.y <= rodBtnY + uBtnH && player.getEquippedRod() != null) {
                    AudioManager.getInstance().playSFX("sfx_button");
                    player.unequip(EquipmentType.ROD); return;
                }
                float lineBtnY = leftY + leftH - 295f + 15f;
                if (m.y >= lineBtnY && m.y <= lineBtnY + uBtnH && player.getEquippedLine() != null) {
                    AudioManager.getInstance().playSFX("sfx_button");
                    player.unequip(EquipmentType.LINE); return;
                }
                float hookBtnY = leftY + leftH - 425f + 15f;
                if (m.y >= hookBtnY && m.y <= hookBtnY + uBtnH && player.getEquippedHook() != null) {
                    AudioManager.getInstance().playSFX("sfx_button");
                    player.unequip(EquipmentType.HOOK); return;
                }
            }

            // Equip / Unequip by clicking ANYWHERE on an inventory card row
            for (int i = 0; i < MAX_VISIBLE; i++) {
                if (scrollOffset + i < cachedFilteredInventory.size()) {
                    Equipment eq = cachedFilteredInventory.get(scrollOffset + i);
                    float ry = invStartY - i * rowSpacing;

                    if (m.x >= rightX + 15f && m.x <= rightX + 15f + rowW && m.y >= ry && m.y <= ry + rowH) {
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

    // =========================================================================
    // EXPANDED TOOLTIP BOX (Never overflows, shows unique icon and clean 2-line stats)
    // =========================================================================
    private void drawTooltip(Equipment eq, float x, float y) {
        float tipW = 420f;
        float tipH = 240f + eq.substats.size() * 26f;

        // Ensure tooltip stays completely within screen bounds
        if (x + tipW + 15f > W) {
            x -= tipW + 15f;
        } else {
            x += 15f;
        }
        x = MathUtils.clamp(x, 15f, W - tipW - 15f);

        if (y - tipH < 15f) {
            y = 15f + tipH;
        }
        if (y > H - 15f) {
            y = H - 15f;
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Dark obsidian panel
        sr.setColor(0.06f, 0.08f, 0.14f, 0.98f);
        drawRoundedRect(sr, x, y - tipH, tipW, tipH, 10f);

        // Top header banner with subtle rarity tint
        sr.setColor(eq.rarity.color.r * 0.25f, eq.rarity.color.g * 0.25f, eq.rarity.color.b * 0.25f, 0.95f);
        drawRoundedRect(sr, x, y - 62f, tipW, 62f, 8f);

        // Icon square in header
        float iconBoxSize = 48f;
        float iconBoxX = x + 10f;
        float iconBoxY = y - 55f;
        sr.setColor(0.04f, 0.06f, 0.10f, 0.95f);
        drawRoundedRect(sr, iconBoxX, iconBoxY, iconBoxSize, iconBoxSize, 6f);
        sr.setColor(eq.rarity.color);
        drawRectOutline(sr, iconBoxX, iconBoxY, iconBoxSize, iconBoxSize, 1.5f);

        // Draw unique equipment icon
        EquipmentIconRenderer.draw(sr, eq, eq.type, iconBoxX + iconBoxSize / 2f, iconBoxY + iconBoxSize / 2f, 18f, eq.rarity.color);

        // Outer border with rarity color
        sr.setColor(eq.rarity.color);
        drawRectOutline(sr, x, y - tipH, tipW, tipH, 2f);

        // Separator lines
        sr.setColor(0.25f, 0.35f, 0.50f, 0.5f);
        sr.rect(x + 12f, y - 66f, tipW - 24f, 1f);
        sr.rect(x + 12f, y - 146f, tipW - 24f, 1f);

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();

        // Name (scaled dynamically to fit without overflow)
        titleFont.setColor(eq.rarity.color);
        layout.setText(titleFont, eq.name);
        float maxTitleW = tipW - 75f;
        if (layout.width > maxTitleW) {
            titleFont.getData().setScale(0.80f * (maxTitleW / layout.width));
        } else {
            titleFont.getData().setScale(0.80f);
        }
        titleFont.draw(game.batch, eq.name, x + 66f, y - 15f);
        titleFont.getData().setScale(1f);

        // Subtitle: Level, Rarity, and Set Name (spaced 25px below name)
        font.setColor(0.8f, 0.9f, 1f, 0.9f);
        font.getData().setScale(0.74f);
        font.draw(game.batch, "Cấp " + eq.level + " (" + eq.rarity.name() + ")  •  Bộ: " + eq.setName, x + 66f, y - 40f);

        // [CHỈ SỐ CHÍNH]
        font.setColor(1f, 0.85f, 0.35f, 1f);
        font.getData().setScale(0.80f);
        font.draw(game.batch, "[CHỈ SỐ CHÍNH]", x + 16f, y - 80f);

        // Main stats cleanly separated on two distinct bullet lines
        font.setColor(0.35f, 1f, 0.65f, 1f);
        font.getData().setScale(0.78f);
        String[] mainStats = getEqMainStatLines(eq);
        font.draw(game.batch, "• " + mainStats[0], x + 16f, y - 104f);
        font.draw(game.batch, "• " + mainStats[1], x + 16f, y - 128f);

        // [CHỈ SỐ NỘI TẠI PHỤ]
        font.setColor(0.4f, 0.85f, 1f, 1f);
        font.getData().setScale(0.80f);
        font.draw(game.batch, "[CHỈ SỐ NỘI TẠI PHỤ]", x + 16f, y - 160f);

        // Substats list
        font.setColor(0.88f, 0.92f, 0.98f, 1f);
        font.getData().setScale(0.78f);
        float sy = y - 186f;
        for (Substat sub : eq.substats) {
            font.draw(game.batch, "• " + sub.toString(), x + 16f, sy);
            sy -= 24f;
        }
        font.getData().setScale(1f);

        game.batch.end();
    }

    private String[] getEqMainStatLines(Equipment eq) {
        if (eq == null) return new String[]{"", ""};
        if (eq.type == EquipmentType.ROD) {
            return new String[]{
                String.format("Sát thương kéo: +%.1f", eq.mainStat1),
                String.format("Thể lực tối đa: +%.1f", eq.mainStat2)
            };
        } else if (eq.type == EquipmentType.LINE) {
            return new String[]{
                String.format("Độ bền dây (Max Tension): +%.1f", eq.mainStat1),
                String.format("Kháng đứt dây: +%.1f", eq.mainStat2)
            };
        } else {
            return new String[]{
                String.format("Tỉ lệ cá hiếm / xịn: +%.1f%%", eq.mainStat1),
                String.format("Tỉ lệ giữ lại mồi: +%.1f%%", eq.mainStat2)
            };
        }
    }

    private String getEqMainStats(Equipment eq) {
        if (eq.type == EquipmentType.ROD) {
            return String.format("Sát thương: +%.0f, Thể lực: +%.0f", eq.mainStat1, eq.mainStat2);
        } else if (eq.type == EquipmentType.LINE) {
            return String.format("Lực căng: +%.0f, Kháng đứt: +%.1f", eq.mainStat1, eq.mainStat2);
        } else {
            return String.format("Tỉ lệ cá xịn: +%.1f%%, Giữ mồi: +%.1f%%", eq.mainStat1, eq.mainStat2);
        }
    }

    // =========================================================================
    // TOP-RIGHT TOTAL STATS POPOVER
    // =========================================================================
    private void drawTotalStatsPopover(ShapeRenderer sr, float x, float y) {
        float popW = 430f;
        float popH = 440f;
        if (x + popW > W - 15f) x = W - popW - 15f;
        if (x < 15f) x = 15f;
        float drawY = y - popH;
        if (drawY < 15f) drawY = 15f;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Glassmorphic dark card
        sr.setColor(0.06f, 0.09f, 0.16f, 0.98f);
        drawRoundedRect(sr, x, drawY, popW, popH, 10f);

        // Header accent background
        sr.setColor(0.12f, 0.18f, 0.30f, 0.95f);
        drawRoundedRect(sr, x, drawY + popH - 42f, popW, 42f, 8f);

        // Golden/cyan neon border
        sr.setColor(0.35f, 0.85f, 1f, 0.92f);
        drawRectOutline(sr, x, drawY, popW, popH, 1.8f);

        // Divider lines
        sr.setColor(0.25f, 0.45f, 0.65f, 0.5f);
        sr.rect(x + 12f, drawY + popH - 42f, popW - 24f, 1.2f);
        sr.rect(x + 12f, drawY + popH - 198f, popW - 24f, 1f);
        sr.rect(x + 12f, drawY + 76f, popW - 24f, 1f);

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void drawTotalStatsPopoverText(float x, float y) {
        float popW = 430f;
        float popH = 440f;
        if (x + popW > W - 15f) x = W - popW - 15f;
        if (x < 15f) x = 15f;
        float drawY = y - popH;
        if (drawY < 15f) drawY = 15f;

        // Header Title - dynamically measured & scaled to never overflow
        String popTitle = "★ TỔNG HỢP CHỈ SỐ NHÂN VẬT ★";
        titleFont.setColor(1f, 0.88f, 0.35f, 1f);
        layout.setText(titleFont, popTitle);
        float maxTitleW = popW - 40f;
        if (layout.width > maxTitleW) {
            titleFont.getData().setScale(0.68f * (maxTitleW / layout.width));
        } else {
            titleFont.getData().setScale(0.68f);
        }
        layout.setText(titleFont, popTitle);
        titleFont.draw(game.batch, layout, x + (popW - layout.width) / 2f, drawY + popH - 14f);
        titleFont.getData().setScale(1f);

        // Subtitle Level & Equipment count
        font.setColor(0.70f, 0.85f, 1f, 0.90f);
        font.getData().setScale(0.76f);
        font.draw(game.batch, "Cấp Độ: " + player.getLevel() + "  |  Trang Bị: " + getEquippedCount() + "/3 Món", x + 20f, drawY + popH - 52f);

        // --- SECTION 1: CORE COMBAT STATS ---
        float lineY = drawY + popH - 74f;
        float spacing = 20f;
        float valX = x + 315f;

        font.setColor(0.95f, 0.95f, 1f, 1f);
        font.draw(game.batch, "• Sát Thương Kéo:", x + 20f, lineY);
        font.setColor(0.3f, 1f, 0.6f, 1f);
        font.draw(game.batch, String.valueOf((int)player.getBasePullDamage()), valX, lineY);
        lineY -= spacing;

        font.setColor(0.95f, 0.95f, 1f, 1f);
        font.draw(game.batch, "• Thể Lực Tối Đa:", x + 20f, lineY);
        font.setColor(0.3f, 1f, 0.6f, 1f);
        font.draw(game.batch, String.valueOf((int)player.getMaxStamina()), valX, lineY);
        lineY -= spacing;

        font.setColor(0.95f, 0.95f, 1f, 1f);
        font.draw(game.batch, "• Độ Bền Dây (Max Tension):", x + 20f, lineY);
        font.setColor(0.3f, 1f, 0.6f, 1f);
        font.draw(game.batch, String.valueOf((int)player.getMaxLineTension()), valX, lineY);
        lineY -= spacing;

        font.setColor(0.95f, 0.95f, 1f, 1f);
        font.draw(game.batch, "• Kháng Đứt Dây:", x + 20f, lineY);
        font.setColor(0.4f, 0.85f, 1f, 1f);
        float breakRes = player.getEquippedLine() != null ? player.getEquippedLine().mainStat2 : 0f;
        font.draw(game.batch, String.format("+%.1f", breakRes), valX, lineY);
        lineY -= spacing;

        font.setColor(0.95f, 0.95f, 1f, 1f);
        font.draw(game.batch, "• Tỉ Lệ Cá Hiếm / Xịn:", x + 20f, lineY);
        font.setColor(0.4f, 0.85f, 1f, 1f);
        float rareB = player.getEquippedHook() != null ? player.getEquippedHook().mainStat1 : 0f;
        font.draw(game.batch, String.format("+%.1f%%", rareB), valX, lineY);
        lineY -= spacing;

        font.setColor(0.95f, 0.95f, 1f, 1f);
        font.draw(game.batch, "• Tỉ Lệ Giữ Lại Mồi:", x + 20f, lineY);
        font.setColor(0.4f, 0.85f, 1f, 1f);
        float baitB = player.getEquippedHook() != null ? player.getEquippedHook().mainStat2 : 0f;
        font.draw(game.batch, String.format("+%.1f%%", baitB), valX, lineY);
        lineY -= spacing;

        // --- SECTION 2: INDIVIDUAL SUBSTAT LINES ---
        lineY = drawY + popH - 212f;
        font.setColor(1f, 0.85f, 0.35f, 1f);
        font.getData().setScale(0.76f);
        font.draw(game.batch, "[DÒNG PHỤ TRANG BỊ TÍCH LŨY]", x + 20f, lineY);
        lineY -= 20f;

        float dmg = player.getEquipmentTotalStat(Substat.Type.DAMAGE_BONUS);
        float gold = player.getEquipmentTotalStat(Substat.Type.GOLD_BONUS);
        float exp = player.getEquipmentTotalStat(Substat.Type.EXP_BONUS);
        float pull = player.getEquipmentTotalStat(Substat.Type.FISH_PULL_REDUCE);
        float stam = player.getEquipmentTotalStat(Substat.Type.STAMINA_REGEN);

        boolean hasAnySub = false;
        font.getData().setScale(0.74f);

        if (dmg > 0) {
            hasAnySub = true;
            font.setColor(0.90f, 0.94f, 0.98f, 1f);
            font.draw(game.batch, "  • Tăng Sát Thương:", x + 20f, lineY);
            font.setColor(1f, 0.85f, 0.35f, 1f);
            font.draw(game.batch, String.format("+%.1f%%", dmg), valX, lineY);
            lineY -= 19f;
        }
        if (gold > 0) {
            hasAnySub = true;
            font.setColor(0.90f, 0.94f, 0.98f, 1f);
            font.draw(game.batch, "  • Thưởng Vàng Nhận Được:", x + 20f, lineY);
            font.setColor(1f, 0.85f, 0.35f, 1f);
            font.draw(game.batch, String.format("+%.1f%%", gold), valX, lineY);
            lineY -= 19f;
        }
        if (exp > 0) {
            hasAnySub = true;
            font.setColor(0.90f, 0.94f, 0.98f, 1f);
            font.draw(game.batch, "  • Thưởng Kinh Nghiệm (EXP):", x + 20f, lineY);
            font.setColor(1f, 0.85f, 0.35f, 1f);
            font.draw(game.batch, String.format("+%.1f%%", exp), valX, lineY);
            lineY -= 19f;
        }
        if (pull > 0) {
            hasAnySub = true;
            font.setColor(0.90f, 0.94f, 0.98f, 1f);
            font.draw(game.batch, "  • Giảm Sức Kéo Của Cá:", x + 20f, lineY);
            font.setColor(0.35f, 1f, 0.65f, 1f);
            font.draw(game.batch, String.format("-%.1f%%", pull), valX, lineY);
            lineY -= 19f;
        }
        if (stam > 0) {
            hasAnySub = true;
            font.setColor(0.90f, 0.94f, 0.98f, 1f);
            font.draw(game.batch, "  • Tốc Độ Hồi Phục Thể Lực:", x + 20f, lineY);
            font.setColor(0.35f, 1f, 0.65f, 1f);
            font.draw(game.batch, String.format("+%.1f%%", stam), valX, lineY);
            lineY -= 19f;
        }

        if (!hasAnySub) {
            font.setColor(0.55f, 0.60f, 0.70f, 0.9f);
            font.draw(game.batch, "  • Chưa có chỉ số phụ nào kích hoạt", x + 20f, lineY);
        }

        // --- SECTION 3: SET BONUS STATUS ---
        float setY = drawY + 54f;
        boolean setBonusActive = player.isSetBonusActive() && player.getEquippedRod() != null;
        if (setBonusActive) {
            Equipment rod = player.getEquippedRod();
            font.setColor(1f, 0.88f, 0.25f, 1f);
            font.getData().setScale(0.76f);
            font.draw(game.batch, "★ KÍCH HOẠT: BỘ " + rod.setName.toUpperCase(), x + 20f, setY);
            font.setColor(0.85f, 0.95f, 1f, 0.9f);
            font.getData().setScale(0.70f);
            String desc = com.fishingrpg.game.entities.equipment.EquipmentGenerator.getSetBonusDesc(rod.setName);
            font.draw(game.batch, desc, x + 20f, setY - 18f, popW - 40f, Align.left, true);
        } else {
            font.setColor(0.6f, 0.65f, 0.75f, 1f);
            font.getData().setScale(0.76f);
            font.draw(game.batch, "• Set Bonus: Chưa kích hoạt", x + 20f, setY);
            font.setColor(0.5f, 0.55f, 0.65f, 0.85f);
            font.getData().setScale(0.70f);
            font.draw(game.batch, "Cần trang bị đủ 3 món cùng bộ để mở khóa nội tại", x + 20f, setY - 18f);
        }
        font.getData().setScale(1f);
    }

    private String getSubstatsSummary() {
        float dmg = player.getEquipmentTotalStat(Substat.Type.DAMAGE_BONUS);
        float gold = player.getEquipmentTotalStat(Substat.Type.GOLD_BONUS);
        float exp = player.getEquipmentTotalStat(Substat.Type.EXP_BONUS);
        float stam = player.getEquipmentTotalStat(Substat.Type.STAMINA_REGEN);
        float pull = player.getEquipmentTotalStat(Substat.Type.FISH_PULL_REDUCE);

        StringBuilder sb = new StringBuilder();
        if (dmg > 0) sb.append("+").append((int)dmg).append("% Dmg ");
        if (gold > 0) sb.append("+").append((int)gold).append("% Vàng ");
        if (exp > 0) sb.append("+").append((int)exp).append("% Exp ");
        if (pull > 0) sb.append("-").append((int)pull).append("% Lực cá ");
        if (stam > 0) sb.append("+").append((int)stam).append("% Hồi thể lực ");
        if (sb.length() == 0) return "Chưa có chỉ số phụ";
        return sb.toString().trim();
    }

    private int getEquippedCount() {
        int c = 0;
        if (player.getEquippedRod() != null) c++;
        if (player.getEquippedLine() != null) c++;
        if (player.getEquippedHook() != null) c++;
        return c;
    }

    // =========================================================================
    // QoL FEATURES (Auto Equip Best Gear & Unequip All)
    // =========================================================================
    private void autoEquipBest() {
        List<Equipment> all = player.getEquipmentInventory();
        if (all.isEmpty()) return;

        Equipment bestRod = player.getEquippedRod();
        float bestRodPwr = bestRod != null ? getEquipmentPower(bestRod) : -1f;

        Equipment bestLine = player.getEquippedLine();
        float bestLinePwr = bestLine != null ? getEquipmentPower(bestLine) : -1f;

        Equipment bestHook = player.getEquippedHook();
        float bestHookPwr = bestHook != null ? getEquipmentPower(bestHook) : -1f;

        for (Equipment eq : all) {
            float pwr = getEquipmentPower(eq);
            if (eq.type == EquipmentType.ROD && pwr > bestRodPwr) {
                bestRod = eq;
                bestRodPwr = pwr;
            } else if (eq.type == EquipmentType.LINE && pwr > bestLinePwr) {
                bestLine = eq;
                bestLinePwr = pwr;
            } else if (eq.type == EquipmentType.HOOK && pwr > bestHookPwr) {
                bestHook = eq;
                bestHookPwr = pwr;
            }
        }

        if (bestRod != null && bestRod != player.getEquippedRod()) player.equip(bestRod);
        if (bestLine != null && bestLine != player.getEquippedLine()) player.equip(bestLine);
        if (bestHook != null && bestHook != player.getEquippedHook()) player.equip(bestHook);

        AudioManager.getInstance().playSFX("sfx_button");
    }

    private void unequipAll() {
        if (player.getEquippedRod() != null || player.getEquippedLine() != null || player.getEquippedHook() != null) {
            player.unequip(EquipmentType.ROD);
            player.unequip(EquipmentType.LINE);
            player.unequip(EquipmentType.HOOK);
            AudioManager.getInstance().playSFX("sfx_button");
        }
    }

    private List<Equipment> getFilteredInventory() {
        List<Equipment> all = player.getEquipmentInventory();
        List<Equipment> filtered = new ArrayList<>();
        if (currentTab == Tab.ALL) {
            filtered.addAll(all);
        } else {
            EquipmentType t = currentTab == Tab.ROD ? EquipmentType.ROD : (currentTab == Tab.LINE ? EquipmentType.LINE : EquipmentType.HOOK);
            for (Equipment eq : all) {
                if (eq.type == t) filtered.add(eq);
            }
        }

        // Apply sorting
        if (currentSort == SortMode.TIME) {
            // Newest acquired first (reverse of original list order)
            java.util.Collections.reverse(filtered);
        } else if (currentSort == SortMode.RARITY) {
            filtered.sort((a, b) -> {
                int rA = getRarityRank(a.rarity);
                int rB = getRarityRank(b.rarity);
                if (rA != rB) return Integer.compare(rB, rA);
                float pA = getEquipmentPower(a);
                float pB = getEquipmentPower(b);
                return Float.compare(pB, pA);
            });
        } else if (currentSort == SortMode.POWER) {
            filtered.sort((a, b) -> {
                float pA = getEquipmentPower(a);
                float pB = getEquipmentPower(b);
                if (pA != pB) return Float.compare(pB, pA);
                int rA = getRarityRank(a.rarity);
                int rB = getRarityRank(b.rarity);
                return Integer.compare(rB, rA);
            });
        }

        return filtered;
    }

    private int getRarityRank(Rarity r) {
        if (r == null) return 0;
        switch (r) {
            case LIMITED_PREMIUM: return 6;
            case LEGENDARY: return 5;
            case EPIC: return 4;
            case LIMITED_EVENT: return 3;
            case RARE: return 2;
            case UNCOMMON: return 1;
            case COMMON: default: return 0;
        }
    }

    private float getEquipmentPower(Equipment eq) {
        if (eq == null) return 0f;
        float base = eq.mainStat1 + eq.mainStat2;
        float subTotal = 0f;
        if (eq.substats != null) {
            for (Substat s : eq.substats) {
                subTotal += s.value;
            }
        }
        return base + subTotal * 1.5f + getRarityRank(eq.rarity) * 20f;
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
        if (width <= 0 || height <= 0) return;
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
