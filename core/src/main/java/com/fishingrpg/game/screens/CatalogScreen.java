package com.fishingrpg.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fishingrpg.game.FishingRPG;
import com.fishingrpg.game.data.CollectionDatabase;
import com.fishingrpg.game.data.FishDatabase;
import com.fishingrpg.game.data.SaveManager;
import com.fishingrpg.game.data.SizeRecord;
import com.fishingrpg.game.entities.Fish;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.systems.AudioManager;
import com.fishingrpg.game.ui.FishVisualRenderer;
import com.fishingrpg.game.ui.NotificationUI;

import java.util.ArrayList;
import java.util.List;

/**
 * Bách Khoa Toàn Thư Thủy Tộc - Sổ tay ngư phủ ghi chép toàn bộ loài cá và bộ sưu tập.
 * Thiết kế giao diện phong cách Hải Trình Thám Hiểm cao cấp với hiệu ứng hình họa procedural.
 */
public class CatalogScreen implements Screen, InputProcessor {

    private final FishingRPG game;
    private final Screen previousScreen;
    private final Player player;

    private ShapeRenderer sr;
    private BitmapFont font;
    private BitmapFont titleFont;
    private OrthographicCamera camera;
    private Viewport viewport;
    private GlyphLayout layout;

    private static final float W = 1280f;
    private static final float H = 720f;

    private float animTime = 0f;
    private boolean hoverBack = false;
    private boolean hoverClaimAll = false;
    private int currentTab = 0; // 0: Ao Làng, 1: Bãi Biển, 2: Đảo Xa
    private boolean[] hoverTabs = new boolean[3];

    private final List<CollectionDatabase.Collection> allCollections;
    private final List<CollectionDatabase.Collection> visibleCollections = new ArrayList<>();

    private float scrollY = 0f;
    private float maxScrollY = 0f;

    // Hover interactions
    private String hoveredClaimCollectionId = null;
    private Fish hoveredFish = null;
    private String hoveredFishName = null;
    private float mouseX, mouseY;
    private final Vector3 mousePos = new Vector3();

    // Cached collection statistics to eliminate per-frame heavy recalculations
    private int cachedAvailableClaimCount = 0;
    private int cachedTotalCaughtCount = 0;
    private final int[] cachedCompletedInBiome = new int[3];
    private final int[] cachedTotalColInBiome = new int[3];

    // Notification toast
    private String toastMsg = null;
    private float toastTimer = 0f;
    private static final float TOAST_DURATION = 2.5f;

    // Ambient floating particles
    private final float[] particleX = new float[25];
    private final float[] particleY = new float[25];
    private final float[] particleSpeed = new float[25];
    private final float[] particleSize = new float[25];

    public CatalogScreen(FishingRPG game, Screen previousScreen, Player player) {
        this.game = game;
        this.previousScreen = previousScreen;
        this.player = player;
        this.allCollections = CollectionDatabase.getAllCollections();

        sr = game.shapeRenderer;
        font = game.font;
        titleFont = game.titleFont;
        layout = new GlyphLayout();

        camera = new OrthographicCamera();
        viewport = new FitViewport(W, H, camera);
        camera.update();

        // Initialize particles
        for (int i = 0; i < particleX.length; i++) {
            particleX[i] = MathUtils.random(0, W);
            particleY[i] = MathUtils.random(0, H);
            particleSpeed[i] = MathUtils.random(15f, 45f);
            particleSize[i] = MathUtils.random(2f, 4.5f);
        }

        updateVisibleCollections();
    }

    private void updateVisibleCollections() {
        visibleCollections.clear();
        String prefix = currentTab == 0 ? "al_" : (currentTab == 1 ? "bb_" : "dx_");
        for (CollectionDatabase.Collection c : allCollections) {
            if (c.id.startsWith(prefix)) {
                visibleCollections.add(c);
            }
        }
        int rows = (visibleCollections.size() + 1) / 2;
        float totalHeight = rows * 260f;
        maxScrollY = Math.max(0, totalHeight - (H - 220f));
        scrollY = 0f;
    }

    private int getAvailableClaimCount() {
        int count = 0;
        for (CollectionDatabase.Collection c : allCollections) {
            if (!player.getClaimedCollections().contains(c.id)) {
                int caught = 0;
                for (String fn : c.fishNames) {
                    if (!SizeRecord.isFirstCatch(fn)) caught++;
                }
                if (caught == c.fishNames.size()) {
                    count++;
                }
            }
        }
        return count;
    }

    private int getTotalCaughtCount() {
        int caught = 0;
        for (FishDatabase.FishTemplate ft : FishDatabase.getAllFishes()) {
            if (!SizeRecord.isFirstCatch(ft.name)) caught++;
        }
        return caught;
    }

    private void refreshCollectionStats() {
        cachedAvailableClaimCount = getAvailableClaimCount();
        cachedTotalCaughtCount = getTotalCaughtCount();
        String[] tabPrefix = {"al_", "bb_", "dx_"};
        for (int i = 0; i < 3; i++) {
            int total = 0;
            int comp = 0;
            for (CollectionDatabase.Collection c : allCollections) {
                if (c.id.startsWith(tabPrefix[i])) {
                    total++;
                    int caught = 0;
                    for (String fn : c.fishNames) {
                        if (!SizeRecord.isFirstCatch(fn)) caught++;
                    }
                    if (caught == c.fishNames.size()) comp++;
                }
            }
            cachedTotalColInBiome[i] = total;
            cachedCompletedInBiome[i] = comp;
        }
    }

    @Override
    public void render(float delta) {
        if (Gdx.graphics.getWidth() <= 0 || Gdx.graphics.getHeight() <= 0) return;
        animTime += delta;
        if (toastTimer > 0) toastTimer -= delta;

        // Update particles
        for (int i = 0; i < particleX.length; i++) {
            particleY[i] += particleSpeed[i] * delta;
            particleX[i] += MathUtils.sin(animTime + i) * 10f * delta;
            if (particleY[i] > H + 10) {
                particleY[i] = -10;
                particleX[i] = MathUtils.random(0, W);
            }
        }

        updateInput(delta);
        if (game.getScreen() != this) return;

        ScreenUtils.clear(0.04f, 0.07f, 0.12f, 1f);
        camera.update();
        sr.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // 1. Draw Ambient Sea Particles
        sr.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < particleX.length; i++) {
            sr.setColor(0.3f, 0.7f, 1f, 0.25f + 0.15f * MathUtils.sin(animTime * 2f + i));
            sr.circle(particleX[i], particleY[i], particleSize[i]);
        }
        sr.end();

        // 2. Draw Collections with Scissor Clipping
        Rectangle scissor = new Rectangle();
        Rectangle clipBounds = new Rectangle(40f, 20f, W - 80f, H - 190f);
        ScissorStack.calculateScissors(camera, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight(), game.batch.getTransformMatrix(), clipBounds, scissor);
        boolean hasScissors = ScissorStack.pushScissors(scissor);

        drawCollectionCards();

        if (hasScissors) {
            ScissorStack.popScissors();
        }

        // 3. Draw Header, Biome Tabs, and Top Navigation
        drawHeaderAndTabs();

        // 4. Draw Tooltip if hovering over caught fish
        if (hoveredFish != null) {
            drawFishTooltip();
        }

        // 5. Draw Notification Toast
        if (toastTimer > 0 && toastMsg != null) {
            NotificationUI.drawNotification(sr, game.batch, font, toastMsg, toastTimer, TOAST_DURATION, W, H);
        }

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void drawCollectionCards() {
        float startX = 60f;
        float cardW = 560f;
        float cardH = 240f;
        float startY = H - 180f + scrollY;

        sr.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < visibleCollections.size(); i++) {
            CollectionDatabase.Collection c = visibleCollections.get(i);
            float cx = startX + (i % 2) * 590f;
            float cy = startY - (i / 2) * (cardH + 20f);

            if (cy - cardH > H || cy < -50f) continue;

            int caughtCount = 0;
            for (String fName : c.fishNames) {
                if (!SizeRecord.isFirstCatch(fName)) caughtCount++;
            }
            boolean completed = (caughtCount == c.fishNames.size());
            boolean claimed = player.getClaimedCollections().contains(c.id);

            // Card Background
            if (completed && !claimed) {
                sr.setColor(0.12f, 0.16f, 0.22f, 0.95f);
            } else if (claimed) {
                sr.setColor(0.08f, 0.13f, 0.16f, 0.9f);
            } else {
                sr.setColor(0.07f, 0.10f, 0.15f, 0.9f);
            }
            drawRoundedRect(sr, cx, cy - cardH, cardW, cardH, 10f);

            // Card Border
            if (completed && !claimed) {
                float pulse = 0.7f + 0.3f * MathUtils.sin(animTime * 4f);
                sr.setColor(1f, 0.8f, 0.2f, pulse);
                drawRectOutline(sr, cx, cy - cardH, cardW, cardH, 2.5f);
            } else if (claimed) {
                sr.setColor(0.2f, 0.75f, 0.45f, 0.8f);
                drawRectOutline(sr, cx, cy - cardH, cardW, cardH, 1.5f);
            } else {
                sr.setColor(0.2f, 0.35f, 0.5f, 0.6f);
                drawRectOutline(sr, cx, cy - cardH, cardW, cardH, 1.2f);
            }

            // Top Header Bar inside Card
            sr.setColor(0f, 0f, 0f, 0.3f);
            sr.rect(cx + 2f, cy - 45f, cardW - 4f, 43f);

            // Progress Bar Track
            float pbX = cx + 15f;
            float pbY = cy - 40f;
            float pbW = 220f;
            float pbH = 6f;
            sr.setColor(0.15f, 0.2f, 0.25f, 1f);
            sr.rect(pbX, pbY, pbW, pbH);

            float fillRatio = (float) caughtCount / (float) c.fishNames.size();
            if (completed) {
                sr.setColor(0.2f, 0.85f, 0.4f, 1f);
            } else {
                sr.setColor(0.2f, 0.65f, 1f, 1f);
            }
            sr.rect(pbX, pbY, pbW * fillRatio, pbH);

            // Claim button background if completed & unclaimed
            if (completed && !claimed) {
                boolean isHover = c.id.equals(hoveredClaimCollectionId);
                float btnX = cx + cardW - 150f;
                float btnY = cy - 38f;
                sr.setColor(isHover ? new Color(1f, 0.85f, 0.3f, 1f) : new Color(0.9f, 0.7f, 0.1f, 1f));
                drawRoundedRect(sr, btnX, btnY, 135f, 30f, 6f);
            }

            // Fish Slots
            int fishCount = c.fishNames.size();
            float slotW = Math.min(125f, (cardW - 30f - (fishCount - 1) * 10f) / fishCount);
            float slotH = 170f;
            float slotY = cy - cardH + 15f;

            for (int j = 0; j < fishCount; j++) {
                String fishName = c.fishNames.get(j);
                Fish ft = FishDatabase.getFishByName(fishName);
                boolean isCaught = !SizeRecord.isFirstCatch(fishName);
                float fx = cx + 15f + j * (slotW + 10f);

                if (isCaught && ft != null) {
                    // Caught Fish Slot
                    Color rarityCol = ft.getRarityColor();
                    sr.setColor(0.12f, 0.17f, 0.23f, 1f);
                    drawRoundedRect(sr, fx, slotY, slotW, slotH, 6f);

                    // Rarity Header Tab inside slot
                    sr.setColor(rarityCol.r, rarityCol.g, rarityCol.b, 0.25f);
                    sr.rect(fx, slotY + slotH - 24f, slotW, 24f);

                    // Rarity Border
                    sr.setColor(rarityCol.r, rarityCol.g, rarityCol.b, 0.75f);
                    drawRectOutline(sr, fx, slotY, slotW, slotH, 1.5f);

                    // Unique Vector Fish Illustration matching FishVisualRenderer!
                    FishVisualRenderer.drawFish(sr, fishName, fx + slotW / 2f, slotY + 98f, 0.52f, animTime + j, rarityCol);

                } else {
                    // Mystery Locked Slot
                    sr.setColor(0.06f, 0.08f, 0.12f, 0.95f);
                    drawRoundedRect(sr, fx, slotY, slotW, slotH, 6f);

                    sr.setColor(0.18f, 0.22f, 0.3f, 0.6f);
                    drawRectOutline(sr, fx, slotY, slotW, slotH, 1f);

                    // Draw silhouette shadow fish
                    drawSilhouetteFish(sr, fx + slotW / 2f, slotY + 95f, slotW * 0.65f);
                }
            }
        }

        sr.end();

        // Batch pass for text and labels
        game.batch.begin();

        for (int i = 0; i < visibleCollections.size(); i++) {
            CollectionDatabase.Collection c = visibleCollections.get(i);
            float cx = startX + (i % 2) * 590f;
            float cy = startY - (i / 2) * (cardH + 20f);

            if (cy - cardH > H || cy < -50f) continue;

            int caughtCount = 0;
            for (String fName : c.fishNames) {
                if (!SizeRecord.isFirstCatch(fName)) caughtCount++;
            }
            boolean completed = (caughtCount == c.fishNames.size());
            boolean claimed = player.getClaimedCollections().contains(c.id);

            // Collection Title
            font.setColor(1f, 0.92f, 0.6f, 1f);
            font.getData().setScale(1f);
            font.draw(game.batch, c.name, cx + 15f, cy - 12f);

            // Progress text (lifted upwards for optimal card balance)
            font.setColor(0.7f, 0.8f, 0.9f, 0.9f);
            font.getData().setScale(0.85f);
            font.draw(game.batch, caughtCount + "/" + c.fishNames.size() + " Loài", cx + 245f, cy - 25f);

            // Right header status / claim button
            if (claimed) {
                font.setColor(0.3f, 0.95f, 0.5f, 1f);
                layout.setText(font, "✓ ĐÃ HOÀN TẤT");
                font.draw(game.batch, layout, cx + cardW - 15f - layout.width, cy - 12f);
            } else if (completed) {
                font.setColor(0.1f, 0.1f, 0.15f, 1f);
                font.getData().setScale(0.9f);
                layout.setText(font, "NHẬN THƯỞNG");
                font.draw(game.batch, layout, cx + cardW - 150f + (135f - layout.width) / 2f, cy - 18f);

                font.setColor(1f, 0.85f, 0.2f, 1f);
                font.getData().setScale(0.8f);
                String rewStr = "+" + c.rewardGold + " Vàng  +" + c.rewardXp + " XP";
                layout.setText(font, rewStr);
                font.draw(game.batch, layout, cx + cardW - 150f + (135f - layout.width) / 2f, cy - 43f);
            } else {
                font.setColor(0.85f, 0.75f, 0.4f, 0.9f);
                font.getData().setScale(0.85f);
                String rewStr = "Thưởng: +" + c.rewardGold + "V • +" + c.rewardXp + "XP";
                layout.setText(font, rewStr);
                font.draw(game.batch, layout, cx + cardW - 15f - layout.width, cy - 12f);
            }

            // Fish Slot texts
            int fishCount = c.fishNames.size();
            float slotW = Math.min(125f, (cardW - 30f - (fishCount - 1) * 10f) / fishCount);
            float slotH = 170f;
            float slotY = cy - cardH + 15f;

            for (int j = 0; j < fishCount; j++) {
                String fishName = c.fishNames.get(j);
                Fish ft = FishDatabase.getFishByName(fishName);
                boolean isCaught = !SizeRecord.isFirstCatch(fishName);
                float fx = cx + 15f + j * (slotW + 10f);

                if (isCaught && ft != null) {
                    // Rarity pill text
                    font.setColor(ft.getRarityColor());
                    font.getData().setScale(0.72f);
                    layout.setText(font, ft.getRarity().name());
                    font.draw(game.batch, layout, fx + (slotW - layout.width) / 2f, slotY + slotH - 7f);

                    // Fish Name (Centered neatly at bottom, records shown on hover)
                    font.setColor(Color.WHITE);
                    font.getData().setScale(0.82f);
                    layout.setText(font, fishName);
                    if (layout.width > slotW - 8f) {
                        font.getData().setScale(0.82f * ((slotW - 8f) / layout.width));
                        layout.setText(font, fishName);
                    }
                    font.draw(game.batch, layout, fx + (slotW - layout.width) / 2f, slotY + 28f);
                } else {
                    // Locked Fish labels
                    font.setColor(0.4f, 0.45f, 0.55f, 0.8f);
                    font.getData().setScale(1f);
                    layout.setText(font, "???");
                    font.draw(game.batch, layout, fx + (slotW - layout.width) / 2f, slotY + 55f);

                    font.setColor(0.4f, 0.45f, 0.55f, 0.6f);
                    font.getData().setScale(0.7f);
                    layout.setText(font, "Chưa câu được");
                    font.draw(game.batch, layout, fx + (slotW - layout.width) / 2f, slotY + 30f);
                }
            }
        }

        font.getData().setScale(1f);
        game.batch.end();
    }

    private void drawHeaderAndTabs() {
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Header Background Glass Dock
        sr.setColor(0.06f, 0.09f, 0.14f, 0.98f);
        sr.rect(0, H - 75f, W, 75f);
        sr.setColor(0.18f, 0.35f, 0.55f, 1f);
        sr.rect(0, H - 77f, W, 2f);

        // Sub-bar for Tabs
        sr.setColor(0.05f, 0.08f, 0.12f, 0.95f);
        sr.rect(0, H - 150f, W, 73f);
        sr.setColor(0.15f, 0.25f, 0.4f, 0.8f);
        sr.rect(0, H - 152f, W, 2f);

        // Back Button
        sr.setColor(hoverBack ? new Color(0.25f, 0.35f, 0.5f, 1f) : new Color(0.15f, 0.22f, 0.32f, 1f));
        drawRoundedRect(sr, 25f, H - 60f, 130f, 44f, 8f);
        sr.setColor(hoverBack ? new Color(0.4f, 0.7f, 1f, 1f) : new Color(0.25f, 0.45f, 0.65f, 0.8f));
        drawRectOutline(sr, 25f, H - 60f, 130f, 44f, 1.5f);

        // Claim All Button (if any available)
        int availableClaim = cachedAvailableClaimCount;
        if (availableClaim > 0) {
            float pulse = 0.8f + 0.2f * MathUtils.sin(animTime * 5f);
            sr.setColor(hoverClaimAll ? new Color(1f, 0.85f, 0.3f, 1f) : new Color(0.9f * pulse, 0.7f * pulse, 0.1f * pulse, 1f));
            drawRoundedRect(sr, W - 240f, H - 60f, 215f, 44f, 8f);
            sr.setColor(1f, 1f, 0.6f, 1f);
            drawRectOutline(sr, W - 240f, H - 60f, 215f, 44f, 1.5f);
        } else {
            // Collection Summary Pill
            sr.setColor(0.12f, 0.18f, 0.25f, 0.9f);
            drawRoundedRect(sr, W - 240f, H - 60f, 215f, 44f, 8f);
            sr.setColor(0.25f, 0.45f, 0.65f, 0.8f);
            drawRectOutline(sr, W - 240f, H - 60f, 215f, 44f, 1.2f);
        }

        // Biome Tabs
        float tabW = 220f;
        float tabH = 46f;
        float tabSpacing = 25f;
        float totalTabW = 3 * tabW + 2 * tabSpacing;
        float tabStartX = (W - totalTabW) / 2f;
        float tabY = H - 138f;

        for (int i = 0; i < 3; i++) {
            boolean active = (currentTab == i);
            boolean hover = hoverTabs[i];
            float tx = tabStartX + i * (tabW + tabSpacing);

            if (active) {
                sr.setColor(0.18f, 0.32f, 0.48f, 1f);
            } else if (hover) {
                sr.setColor(0.12f, 0.20f, 0.30f, 1f);
            } else {
                sr.setColor(0.08f, 0.14f, 0.22f, 0.9f);
            }
            drawRoundedRect(sr, tx, tabY, tabW, tabH, 8f);

            // Tab Border & Active Bottom Indicator
            if (active) {
                sr.setColor(0.4f, 0.85f, 1f, 1f);
                drawRectOutline(sr, tx, tabY, tabW, tabH, 2f);
                sr.setColor(1f, 0.85f, 0.3f, 1f);
                sr.rect(tx + 20f, tabY - 2f, tabW - 40f, 4f);
            } else {
                sr.setColor(0.2f, 0.35f, 0.5f, 0.6f);
                drawRectOutline(sr, tx, tabY, tabW, tabH, 1.2f);
            }
        }

        sr.end();

        // Header Texts
        game.batch.begin();

        // Back Button Text
        font.setColor(hoverBack ? Color.WHITE : new Color(0.85f, 0.9f, 0.95f, 0.9f));
        layout.setText(font, "← QUAY LẠI");
        font.draw(game.batch, layout, 25f + (130f - layout.width) / 2f, H - 32f);

        // Screen Title
        titleFont.setColor(0.95f, 0.85f, 0.45f, 1f);
        layout.setText(titleFont, "BÁCH KHOA TOÀN THƯ THỦY TỘC");
        titleFont.draw(game.batch, layout, W / 2f - layout.width / 2f, H - 33f);

        // Right Action Text
        if (availableClaim > 0) {
            font.setColor(0.1f, 0.1f, 0.15f, 1f);
            font.getData().setScale(0.95f);
            String claimAllText = "★ NHẬN TẤT CẢ (" + availableClaim + ") ★";
            layout.setText(font, claimAllText);
            font.draw(game.batch, layout, W - 240f + (215f - layout.width) / 2f, H - 33f);
        } else {
            font.setColor(0.4f, 0.85f, 1f, 1f);
            font.getData().setScale(0.85f);
            int caughtTotal = cachedTotalCaughtCount;
            int totalFishes = FishDatabase.getAllFishes().size();
            String progText = "Đã Bắt: " + caughtTotal + " / " + totalFishes + " Loài";
            layout.setText(font, progText);
            font.draw(game.batch, layout, W - 240f + (215f - layout.width) / 2f, H - 33f);
        }

        // Biome Tab Texts
        String[] tabNames = {"AO LÀNG", "BÃI BIỂN", "ĐẢO XA"};

        for (int i = 0; i < 3; i++) {
            boolean active = (currentTab == i);
            float tx = tabStartX + i * (tabW + tabSpacing);

            int totalColInBiome = cachedTotalColInBiome[i];
            int completedInBiome = cachedCompletedInBiome[i];

            font.setColor(active ? Color.WHITE : new Color(0.7f, 0.8f, 0.9f, 0.85f));
            font.getData().setScale(0.95f);
            String tabTitle = tabNames[i];
            layout.setText(font, tabTitle);
            font.draw(game.batch, layout, tx + (tabW - layout.width) / 2f, tabY + 34f);

            // Sub badge
            font.setColor(active ? new Color(1f, 0.85f, 0.35f, 1f) : new Color(0.5f, 0.65f, 0.8f, 0.8f));
            font.getData().setScale(0.75f);
            String badge = completedInBiome + "/" + totalColInBiome + " Bộ Hoàn Tất";
            layout.setText(font, badge);
            font.draw(game.batch, layout, tx + (tabW - layout.width) / 2f, tabY + 16f);
        }

        font.getData().setScale(1f);
        game.batch.end();
    }

    private void drawProceduralFish(ShapeRenderer sr, float cx, float cy, float maxW, Color rarityColor, float seed) {
        float bodyW = maxW * 0.45f;
        float bodyH = bodyW * 0.55f;

        // Tail fin
        sr.setColor(rarityColor.r * 0.8f, rarityColor.g * 0.8f, rarityColor.b * 0.8f, 0.9f);
        sr.triangle(cx - bodyW * 0.6f, cy,
                    cx - bodyW * 1.15f, cy + bodyH * 0.65f,
                    cx - bodyW * 1.15f, cy - bodyH * 0.65f);

        // Dorsal fin
        sr.triangle(cx - bodyW * 0.1f, cy + bodyH * 0.4f,
                    cx + bodyW * 0.2f, cy + bodyH * 0.4f,
                    cx + bodyW * 0.05f, cy + bodyH * 0.95f);

        // Pectoral fin
        sr.triangle(cx + bodyW * 0.1f, cy - bodyH * 0.35f,
                    cx + bodyW * 0.35f, cy - bodyH * 0.35f,
                    cx + bodyW * 0.18f, cy - bodyH * 0.8f);

        // Fish Body Ellipse
        sr.setColor(rarityColor.r, rarityColor.g, rarityColor.b, 0.95f);
        sr.ellipse(cx - bodyW / 2f, cy - bodyH / 2f, bodyW, bodyH);

        // Belly lighter shading
        sr.setColor(1f, 1f, 1f, 0.25f);
        sr.ellipse(cx - bodyW * 0.35f, cy - bodyH * 0.35f, bodyW * 0.7f, bodyH * 0.4f);

        // Eye
        sr.setColor(1f, 1f, 1f, 1f);
        sr.circle(cx + bodyW * 0.25f, cy + bodyH * 0.1f, bodyH * 0.16f);
        sr.setColor(0.05f, 0.05f, 0.08f, 1f);
        sr.circle(cx + bodyW * 0.26f, cy + bodyH * 0.1f, bodyH * 0.09f);
    }

    private void drawSilhouetteFish(ShapeRenderer sr, float cx, float cy, float maxW) {
        float bodyW = maxW * 0.45f;
        float bodyH = bodyW * 0.55f;

        sr.setColor(0.12f, 0.16f, 0.22f, 0.6f);
        // Tail
        sr.triangle(cx - bodyW * 0.6f, cy,
                    cx - bodyW * 1.15f, cy + bodyH * 0.65f,
                    cx - bodyW * 1.15f, cy - bodyH * 0.65f);
        // Dorsal
        sr.triangle(cx - bodyW * 0.1f, cy + bodyH * 0.4f,
                    cx + bodyW * 0.2f, cy + bodyH * 0.4f,
                    cx + bodyW * 0.05f, cy + bodyH * 0.95f);
        // Body
        sr.ellipse(cx - bodyW / 2f, cy - bodyH / 2f, bodyW, bodyH);
    }

    private void drawFishTooltip() {
        float tw = 410f;
        float th = 238f;
        float tx = mouseX + 18f;
        float ty = mouseY - 15f;

        if (tx + tw > W - 15f) tx = mouseX - tw - 15f;
        if (ty - th < 20f) ty = 20f + th;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Drop shadow
        sr.setColor(0f, 0f, 0f, 0.70f);
        drawRoundedRect(sr, tx + 6f, ty - th - 6f, tw, th, 10f);

        // Card Backdrop
        sr.setColor(0.07f, 0.10f, 0.16f, 0.98f);
        drawRoundedRect(sr, tx, ty - th, tw, th, 10f);

        // Border in rarity color
        Color rc = hoveredFish.getRarityColor();
        sr.setColor(rc.r, rc.g, rc.b, 0.95f);
        drawRectOutline(sr, tx, ty - th, tw, th, 2f);

        // Header strip with actual alpha blend
        sr.setColor(rc.r * 0.25f, rc.g * 0.25f, rc.b * 0.25f, 0.95f);
        sr.rect(tx + 2f, ty - 38f, tw - 4f, 36f);

        // Rarity Pill Badge on the right of header
        float badgeW = 95f;
        float badgeH = 22f;
        float badgeX = tx + tw - badgeW - 12f;
        float badgeY = ty - 31f;
        sr.setColor(0.06f, 0.09f, 0.14f, 0.95f);
        drawRoundedRect(sr, badgeX, badgeY, badgeW, badgeH, 4f);
        sr.setColor(rc.r, rc.g, rc.b, 0.90f);
        drawRectOutline(sr, badgeX, badgeY, badgeW, badgeH, 1.2f);

        // Fish Visual Showcase Frame on Left
        float frameX = tx + 14f;
        float frameY = ty - 146f;
        float frameW = 98f;
        float frameH = 96f;
        sr.setColor(0.04f, 0.07f, 0.11f, 0.95f);
        drawRoundedRect(sr, frameX, frameY, frameW, frameH, 6f);
        sr.setColor(rc.r, rc.g, rc.b, 0.5f);
        drawRectOutline(sr, frameX, frameY, frameW, frameH, 1.2f);

        // Draw animated fish inside showcase frame
        FishVisualRenderer.drawFish(sr, hoveredFishName, frameX + frameW / 2f, frameY + frameH / 2f, 0.55f, animTime, rc);

        sr.end();

        game.batch.begin();

        // Title: Crisp white text
        font.setColor(Color.WHITE);
        font.getData().setScale(0.92f);
        font.draw(game.batch, hoveredFish.getName(), tx + 14f, ty - 15f);

        // Rarity Tag: Vibrant rarity color inside badge
        font.setColor(rc);
        font.getData().setScale(0.72f);
        layout.setText(font, hoveredFish.getRarity().name());
        font.draw(game.batch, layout, badgeX + (badgeW - layout.width) / 2f, badgeY + 16f);

        // Stats on the right of the fish frame
        float infoX = tx + 124f;
        float infoW = tw - 136f;
        font.setColor(0.85f, 0.92f, 1f, 0.95f);
        font.getData().setScale(0.76f);
        font.draw(game.batch, "❖ Thể lực: " + (int) hoveredFish.getMaxHp() + " HP", infoX, ty - 50f);
        font.draw(game.batch, "❖ Lực kéo: " + (int) hoveredFish.getPullStrength() + " (dmg/s)", infoX, ty - 68f);

        // Records (Weight & Length - Shown on hover!)
        float wRecord = SizeRecord.getRecord(hoveredFishName);
        float lRecord = SizeRecord.getRecordLength(hoveredFishName);
        font.setColor(1f, 0.85f, 0.35f, 1f);
        font.draw(game.batch, "❖ Kỷ lục: " + String.format(java.util.Locale.US, "%.1f", wRecord) + " kg • " + String.format(java.util.Locale.US, "%.1f", lRecord) + " cm", infoX, ty - 88f);

        // Skills info
        String skillsStr = "❖ Kỹ năng: ";
        if (hoveredFish.getSkills() == null || hoveredFish.getSkills().isEmpty()) {
            skillsStr += "Không có";
        } else {
            for (int si = 0; si < hoveredFish.getSkills().size(); si++) {
                String sid = hoveredFish.getSkills().get(si).id;
                skillsStr += Fish.getSkillDisplayName(sid);
                if (si < hoveredFish.getSkills().size() - 1) skillsStr += ", ";
            }
        }
        font.setColor(0.4f, 0.85f, 1f, 1f);
        font.draw(game.batch, skillsStr, infoX, ty - 106f, infoW, Align.left, true);

        // Description / Lore Line for each fish
        String desc = getFishLore(hoveredFishName);
        font.setColor(0.95f, 0.88f, 0.65f, 0.95f);
        font.getData().setScale(0.68f);
        font.draw(game.batch, "“" + desc + "”", tx + 14f, ty - 162f, tw - 28f, Align.left, true);

        // Flavor footer
        font.setColor(0.6f, 0.75f, 0.88f, 0.85f);
        font.getData().setScale(0.70f);
        font.draw(game.batch, "Thưởng gốc: +" + hoveredFish.getBaseGold() + " Vàng  +" + hoveredFish.getBaseXp() + " XP", tx + 14f, ty - 208f);

        font.getData().setScale(1f);
        game.batch.end();
    }

    public static String getFishLore(String name) {
        if (name == null) return "Sinh vật thủy sinh kỳ bí của thế giới AnglerRPG.";
        switch (name) {
            // Ao Làng
            case "Cá Rô Đồng": return "Loài cá gai góc kiên cường, sống dai dẳng dưới bùn lầy đồng quê.";
            case "Cá Diếc": return "Thân dẹp vảy bạc mịn màng, tính tình hiền lành bơi theo đàn sát đáy.";
            case "Cá Chép": return "Biểu tượng kiên trì vượt vũ môn, vảy rồng óng ánh và râu dài quý phái.";
            case "Cá Mè": return "Thân dẹp trắng bạc, mắt hạ thấp, tính nhút nhát nhưng quẫy nước cực khỏe.";
            case "Cá Mương": return "Thân hình lá liễu thon dài, bơi lội nhanh thoăn thoắt trên mặt nước ao hồ.";
            case "Cá Lóc Nhí": return "Cá lóc con hiếu động háu ăn, mang sọc vàng cam rực lửa dọc sống lưng.";
            case "Cá Trê": return "Sống ẩn mình dưới bùn lầy, sở hữu 4 râu nhạy cảm và ngạnh nhọn tự vệ.";
            case "Cá Trôi": return "Thân hình ngư lôi vạm vỡ, vây đuôi viền son đỏ son và mắt ruby kiên cường.";
            case "Cá Trắm Cỏ": return "Kình ngư ăn cỏ khổng lồ, thân khúc gỗ lực lưỡng bọc vảy quả trám trứ danh.";
            case "Cá Chim Trắng": return "Dáng đĩa dẹp lưng gù, ức và vây đỏ rực lửa, hàm răng sắc bén ăn tạp.";
            case "Cá Lóc Bông": return "Sát thủ đầm lầy, hoa văn rằn ri cọp sắc nhọn, hàm dưới lởm chởm răng nanh.";
            case "Cá Trê Lai": return "Thân bọc hoa văn cẩm thạch da báo vàng đen, đầu dẹp bọc giáp xương săn đêm.";
            case "Cá Chép Vàng": return "Cá chép hoàng kim linh thiêng, vây bướm thướt tha mang lại may mắn tột đỉnh.";
            case "Cá Lăng Khổng Lồ": return "Thủy quái cổ thạch ngự trị đáy vực thẳm, thân xám đá hoa cương khổng lồ.";
            case "Thủy Quái Sông Trà": return "Bá chủ truyền thuyết ngự trị vực thẳm sông Trà với sức mạnh hủy diệt.";

            // Bãi Biển
            case "Cá Chỉ Vàng": return "Dải sọc vàng rực óng ánh dọc thân bạc, món quà quen thuộc của biển cả.";
            case "Cá Đục": return "Thân tròn thuôn dài màu cát biển, vùi mình dưới lớp cát mịn ven bờ.";
            case "Cá Cơm": return "Thân hình nhỏ bé trong suốt như pha lê, bơi thành đàn hàng vạn con.";
            case "Cá Mòi": return "Lưng xanh lam bụng trắng lấp lánh, di cư theo mùa tạo thành bão cá.";
            case "Cá Lẹp": return "Thân mỏng dính như lưỡi dao, chuyên ẩn mình ngụy trang trên ngọn sóng dập dềnh.";
            case "Cá Bạc Má": return "Đôi mắt to tròn ngơ ngác, thân lấp lánh ánh kim lướt nhanh theo đàn ngàn con.";
            case "Cá Nục": return "Thân tròn lẳn cơ bắp, bơi lội khỏe khoắn chống chọi những ngọn sóng dữ.";
            case "Cá Đối": return "Chuyên búng mình nhảy vọt khỏi mặt nước, đầu dẹp mõm tròn kiếm ăn rong biển.";
            case "Cá Trích": return "Vảy bạc lấp lánh như dát bạc trên mặt biển xanh, bơi nhanh đón bình minh.";
            case "Cá Chim Biển": return "Dáng dấp cánh chim rẽ sóng đại dương, vây bơi vút dài lướt trong gió biển.";
            case "Cá Mú": return "Bậc thầy ngụy trang rạn san hô, miệng khổng lồ đớp gọn con mồi chớp nhoáng.";
            case "Cá Bóp": return "Chiến binh biển cả dũng mãnh, cú kéo giằng hung bạo thử thách mọi cần thủ.";
            case "Cá Thu Hoàng Kim": return "Bảo vật biển khơi lấp lánh ánh vàng kim, bơi như ngư lôi xé toạc đại dương.";
            case "Cá Ngừ Đại Dương": return "Vua tốc độ biển khơi, thân hình ngư lôi bơi không ngừng nghỉ giữa đại dương.";
            case "Bạch Tuộc Khổng Lồ": return "Quái vật xúc tu ngự trị đáy biển, phun mực mù mịt và siết chặt con mồi.";

            // Đảo Xa
            case "Cá Ngựa Vằn": return "Sọc trắng đen tương phản nổi bật giữa rạn san hô, dáng bơi uyển chuyển.";
            case "Cá Hề": return "Bộ cánh cam sọc trắng rực rỡ, cộng sinh hòa thuận bên những xúc tu hải quỳ.";
            case "Cá Đuối Nhỏ": return "Dáng dấp cánh diều lướt êm trên nền cát san hô, ngạnh đuôi tự vệ sắc bén.";
            case "Mực Ống": return "Thân trong suốt đổi màu linh hoạt, chuyển động phản lực nhanh như tên bắn.";
            case "Cá Nóc": return "Phồng tròn như quả cầu gai khi gặp nguy hiểm, thân chứa độc tố bí ẩn.";
            case "Cá Phèn": return "Đôi râu cằm linh hoạt sục sạo đáy cát tìm mồi, vảy ánh hồng cam rực rỡ.";
            case "Cá Mặt Trăng": return "Dáng tròn khổng lồ như chiếc đĩa bạc, thích nổi nghiêng phơi mình dưới ánh mặt trời.";
            case "Cá Cờ": return "Vây lưng cao vút như cánh buồm xé toạc đại dương, thợ săn tốc độ siêu hạng.";
            case "Rùa Biển": return "Sứ giả ngàn năm tuổi của đại dương, mai rùa hoa văn tuyệt mỹ bơi nghìn hải lý.";
            case "Cá Chuồn": return "Đôi vây ngực dang rộng như đôi cánh, lướt bay trên mặt đại dương bao la.";
            case "Cá Mập Cáo": return "Chiếc đuôi dài uyển chuyển như ngọn roi quất bầy cá, sát thủ săn mồi bóng đêm.";
            case "Cá Kiếm": return "Mũi kiếm xương dài nhọn hoắt như thanh kiếm báu, lao nhanh chém toạc sóng gió.";
            case "Cá Voi Lưng Gù": return "Người khổng lồ hiền lành của biển sâu, cất tiếng hát du dương giữa đại dương mênh mông.";
            case "Mực Khổng Lồ": return "Thủy quái bóng đêm ngự trị vực sâu ngàn mét, xúc tu khổng lồ với giác hút gai góc.";
            case "Thần Biển Leviathan": return "Huyền thoại tối thượng của biển khơi sâu thẳm, quyền năng sấm sét khuấy đảo sóng thần.";

            default: return "Loài thủy tộc độc đáo với tập tính đặc biệt trong hệ sinh thái AnglerRPG.";
        }
    }

    private void updateInput(float delta) {
        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mousePos);
        mouseX = mousePos.x;
        mouseY = mousePos.y;
        Vector3 m = mousePos;

        hoverBack = (m.x >= 25f && m.x <= 155f && m.y >= H - 60f && m.y <= H - 16f);
        hoverClaimAll = (m.x >= W - 240f && m.x <= W - 25f && m.y >= H - 60f && m.y <= H - 16f && cachedAvailableClaimCount > 0);

        // Biome tab hovers
        float tabW = 220f;
        float tabH = 46f;
        float tabSpacing = 25f;
        float totalTabW = 3 * tabW + 2 * tabSpacing;
        float tabStartX = (W - totalTabW) / 2f;
        float tabY = H - 138f;

        for (int i = 0; i < 3; i++) {
            float tx = tabStartX + i * (tabW + tabSpacing);
            hoverTabs[i] = (m.x >= tx && m.x <= tx + tabW && m.y >= tabY && m.y <= tabY + tabH);
            if (hoverTabs[i] && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                if (currentTab != i) {
                    currentTab = i;
                    AudioManager.getInstance().playSound("button_click");
                    updateVisibleCollections();
                }
            }
        }

        // Back button click
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || (hoverBack && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))) {
            AudioManager.getInstance().playSound("button_click");
            game.setScreen(previousScreen);
            return;
        }

        // Claim All button click
        if (hoverClaimAll && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            claimAllCollections();
            return;
        }

        // Cards and Fish hover detection
        hoveredClaimCollectionId = null;
        hoveredFish = null;
        hoveredFishName = null;

        float startX = 60f;
        float cardW = 560f;
        float cardH = 240f;
        float startY = H - 180f + scrollY;

        for (int i = 0; i < visibleCollections.size(); i++) {
            CollectionDatabase.Collection c = visibleCollections.get(i);
            float cx = startX + (i % 2) * 590f;
            float cy = startY - (i / 2) * (cardH + 20f);

            // Scissor clip bounds check
            if (cy - cardH > H - 160f || cy < 20f) continue;

            int caughtCount = 0;
            for (String fName : c.fishNames) {
                if (!SizeRecord.isFirstCatch(fName)) caughtCount++;
            }
            boolean completed = (caughtCount == c.fishNames.size());
            boolean claimed = player.getClaimedCollections().contains(c.id);

            // Single Claim button check
            if (completed && !claimed) {
                float btnX = cx + cardW - 150f;
                float btnY = cy - 38f;
                if (m.x >= btnX && m.x <= btnX + 135f && m.y >= btnY && m.y <= btnY + 30f) {
                    hoveredClaimCollectionId = c.id;
                    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                        claimSingleCollection(c);
                        return;
                    }
                }
            }

            // Fish slots hover
            int fishCount = c.fishNames.size();
            float slotW = Math.min(125f, (cardW - 30f - (fishCount - 1) * 10f) / fishCount);
            float slotH = 170f;
            float slotY = cy - cardH + 15f;

            for (int j = 0; j < fishCount; j++) {
                String fishName = c.fishNames.get(j);
                boolean isCaught = !SizeRecord.isFirstCatch(fishName);
                float fx = cx + 15f + j * (slotW + 10f);

                if (isCaught && m.x >= fx && m.x <= fx + slotW && m.y >= slotY && m.y <= slotY + slotH) {
                    hoveredFish = FishDatabase.getFishByName(fishName);
                    hoveredFishName = fishName;
                }
            }
        }

        // Scroll with keyboard
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) scrollY -= 450f * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) scrollY += 450f * delta;
        if (scrollY < 0) scrollY = 0;
        if (scrollY > maxScrollY && maxScrollY > 0) scrollY = maxScrollY;
    }

    private void claimSingleCollection(CollectionDatabase.Collection c) {
        player.claimCollection(c.id);
        player.gainGold(c.rewardGold);
        player.gainXp(c.rewardXp);
        SaveManager.savePlayer(player);
        AudioManager.getInstance().playSound("victory");
        refreshCollectionStats();
        toastMsg = "Đã nhận thưởng " + c.name + ": +" + c.rewardGold + " Vàng, +" + c.rewardXp + " XP!";
        toastTimer = TOAST_DURATION;
    }

    private void claimAllCollections() {
        int totalGold = 0;
        int totalXp = 0;
        int count = 0;

        for (CollectionDatabase.Collection c : allCollections) {
            if (!player.getClaimedCollections().contains(c.id)) {
                int caught = 0;
                for (String fn : c.fishNames) {
                    if (!SizeRecord.isFirstCatch(fn)) caught++;
                }
                if (caught == c.fishNames.size()) {
                    player.claimCollection(c.id);
                    player.gainGold(c.rewardGold);
                    player.gainXp(c.rewardXp);
                    totalGold += c.rewardGold;
                    totalXp += c.rewardXp;
                    count++;
                }
            }
        }

        if (count > 0) {
            SaveManager.savePlayer(player);
            AudioManager.getInstance().playSound("victory");
            refreshCollectionStats();
            toastMsg = "Nhận thành công " + count + " bộ sưu tập: +" + totalGold + " Vàng, +" + totalXp + " XP!";
            toastTimer = TOAST_DURATION;
        }
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

    @Override public void show() {
        AudioManager.getInstance().playBGM("bgm_menu");
        Gdx.input.setInputProcessor(this);
        refreshCollectionStats();
    }
    @Override public void resize(int w, int h) {
        if (w <= 0 || h <= 0) return;
        viewport.update(w, h, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {
        if (Gdx.input.getInputProcessor() == this) {
            Gdx.input.setInputProcessor(null);
        }
    }
    @Override public void dispose() {
    }

    // InputProcessor methods
    @Override public boolean keyDown(int keycode) { return false; }
    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        scrollY += amountY * 65f;
        if (scrollY < 0) scrollY = 0;
        if (scrollY > maxScrollY && maxScrollY > 0) scrollY = maxScrollY;
        return true;
    }
}
