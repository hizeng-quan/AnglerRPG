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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fishingrpg.game.FishingRPG;
import com.fishingrpg.game.PlayScreen;
import com.fishingrpg.game.data.SaveManager;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.entities.Quest;
import com.fishingrpg.game.systems.AudioManager;
import com.fishingrpg.game.systems.QuestManager;
import com.fishingrpg.game.ui.NotificationUI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Bảng Treo Thưởng Hiệp Hội Ngư Phủ (Quest & Bounty Guild).
 * Thiết kế giao diện phong cách Tavern / Adventurer Guild hiện đại, cao cấp với hiệu ứng ánh sáng.
 */
public class QuestScreen implements Screen {

    private final FishingRPG game;
    private final PlayScreen playScreen;
    private final Player player;
    private final QuestManager qm;

    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final ShapeRenderer sr;
    private final BitmapFont font;
    private final BitmapFont titleFont;
    private final GlyphLayout layout;

    private static final float W = 1280f;
    private static final float H = 720f;

    private float animTime = 0f;
    private int currentTab = 0; // 0: Daily, 1: Weekly, 2: Achievement
    private float scrollY = 0f;

    // Toast message
    private String toastMsg = null;
    private float toastTimer = 0f;
    private static final float TOAST_DURATION = 2.5f;

    // Hover states
    private boolean hoverBack = false;
    private boolean hoverClaimChest = false;
    private boolean hoverClaimAll = false;
    private final boolean[] hoverTabs = new boolean[3];
    private String hoveredClaimQuestId = null;

    // Ambient floating golden embers
    private final float[] emberX = new float[20];
    private final float[] emberY = new float[20];
    private final float[] emberSpeed = new float[20];
    private final float[] emberSize = new float[20];

    public QuestScreen(FishingRPG game, PlayScreen playScreen, Player player) {
        this.game = game;
        this.playScreen = playScreen;
        this.player = player;
        this.qm = player.getQuestManager();

        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(W, H, camera);
        this.sr = new ShapeRenderer();
        this.font = game.font;
        this.titleFont = game.titleFont;
        this.layout = new GlyphLayout();

        for (int i = 0; i < emberX.length; i++) {
            emberX[i] = MathUtils.random(0, W);
            emberY[i] = MathUtils.random(0, H);
            emberSpeed[i] = MathUtils.random(20f, 50f);
            emberSize[i] = MathUtils.random(2f, 4f);
        }
    }

    @Override
    public void show() {
        AudioManager.getInstance().playBGM("bgm_menu");
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                scrollY += amountY * 65f;
                if (scrollY < 0) scrollY = 0;

                int itemCount = (currentTab == 0) ? qm.dailyQuests.size() : (currentTab == 1) ? qm.weeklyQuests.size() : qm.achievements.size();
                float itemHeight = (currentTab < 2) ? 140f : 110f;
                if (currentTab < 2) itemCount = (int) Math.ceil(itemCount / 2.0);
                float maxScroll = Math.max(0, (itemCount * itemHeight) - ((currentTab < 2) ? 410f : 530f));
                if (scrollY > maxScroll) scrollY = maxScroll;

                return true;
            }
        });
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        animTime += delta;
        if (toastTimer > 0) toastTimer -= delta;

        // Update ambient embers
        for (int i = 0; i < emberX.length; i++) {
            emberY[i] += emberSpeed[i] * delta;
            emberX[i] += MathUtils.sin(animTime * 1.5f + i) * 12f * delta;
            if (emberY[i] > H + 10) {
                emberY[i] = -10;
                emberX[i] = MathUtils.random(0, W);
            }
        }

        if (handleInput(delta)) return;

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        ScreenUtils.clear(0.06f, 0.08f, 0.12f, 1f);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // 1. Draw Embers
        sr.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < emberX.length; i++) {
            sr.setColor(1f, 0.8f, 0.3f, 0.3f + 0.2f * MathUtils.sin(animTime * 3f + i));
            sr.circle(emberX[i], emberY[i], emberSize[i]);
        }
        sr.end();

        // 2. Draw Left Sidebar Panel
        drawSidebar();

        // 3. Draw Right Main Panel & Header
        drawMainPanelHeader();

        // 4. Draw Quest Cards inside Scissor Clipping
        drawQuestCards();

        // 5. Draw Notification Toast
        if (toastTimer > 0 && toastMsg != null) {
            NotificationUI.drawNotification(sr, game.batch, font, toastMsg, toastTimer, TOAST_DURATION, W, H);
        }

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void drawSidebar() {
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Sidebar Background
        sr.setColor(0.08f, 0.11f, 0.16f, 0.98f);
        drawRoundedRect(sr, 20f, 20f, 240f, H - 40f, 12f);
        sr.setColor(0.2f, 0.35f, 0.5f, 0.7f);
        drawRectOutline(sr, 20f, 20f, 240f, H - 40f, 1.5f);

        // Back Button
        sr.setColor(hoverBack ? new Color(0.25f, 0.35f, 0.5f, 1f) : new Color(0.15f, 0.22f, 0.32f, 1f));
        drawRoundedRect(sr, 35f, H - 85f, 210f, 48f, 8f);
        sr.setColor(hoverBack ? new Color(0.4f, 0.75f, 1f, 1f) : new Color(0.25f, 0.45f, 0.65f, 0.8f));
        drawRectOutline(sr, 35f, H - 85f, 210f, 48f, 1.5f);

        // Category Tabs
        String[] tabTitles = {"NHIỆM VỤ NGÀY", "NHIỆM VỤ TUẦN", "THÀNH TỰU"};
        String[] tabSubtitles = {"Đặt lại mỗi ngày", "Đặt lại mỗi tuần", "Mốc vinh danh"};

        for (int i = 0; i < 3; i++) {
            boolean active = (currentTab == i);
            boolean hover = hoverTabs[i];
            float ty = H - 165f - i * 78f;

            if (active) {
                sr.setColor(0.18f, 0.32f, 0.48f, 1f);
            } else if (hover) {
                sr.setColor(0.12f, 0.20f, 0.30f, 1f);
            } else {
                sr.setColor(0.10f, 0.15f, 0.22f, 0.85f);
            }
            drawRoundedRect(sr, 35f, ty, 210f, 66f, 8f);

            if (active) {
                sr.setColor(0.4f, 0.85f, 1f, 1f);
                drawRectOutline(sr, 35f, ty, 210f, 66f, 2f);
                sr.setColor(1f, 0.85f, 0.3f, 1f);
                sr.rect(35f, ty + 12f, 5f, 42f);
            } else {
                sr.setColor(0.2f, 0.3f, 0.45f, 0.6f);
                drawRectOutline(sr, 35f, ty, 210f, 66f, 1.2f);
            }

            // Notification dot on tab if uncollected quests exist
            int uncollected = getUnclaimedCountForTab(i);
            if (uncollected > 0) {
                float pulse = 0.8f + 0.2f * MathUtils.sin(animTime * 6f);
                sr.setColor(1f, 0.25f, 0.25f, pulse);
                sr.circle(235f, ty + 50f, 8f);
            }
        }

        // Currency Display Dock at Bottom
        float curBoxY = 35f;
        sr.setColor(0.06f, 0.08f, 0.12f, 0.9f);
        drawRoundedRect(sr, 35f, curBoxY, 210f, 145f, 8f);
        sr.setColor(0.18f, 0.28f, 0.4f, 0.6f);
        drawRectOutline(sr, 35f, curBoxY, 210f, 145f, 1.2f);

        // Gold coin icon circle
        sr.setColor(1f, 0.8f, 0.2f, 1f);
        sr.circle(55f, curBoxY + 115f, 9f);
        // Diamond gem diamond
        sr.setColor(0.4f, 0.85f, 1f, 1f);
        sr.circle(55f, curBoxY + 75f, 9f);
        // Ticket badge
        sr.setColor(1f, 0.35f, 0.45f, 1f);
        sr.circle(55f, curBoxY + 35f, 9f);

        sr.end();

        // Texts for Sidebar
        game.batch.begin();

        // Back Button Text
        font.setColor(hoverBack ? Color.WHITE : new Color(0.85f, 0.9f, 0.95f, 0.9f));
        layout.setText(font, "← QUAY LẠI");
        font.draw(game.batch, layout, 35f + (210f - layout.width) / 2f, H - 56f);

        // Tab Texts
        for (int i = 0; i < 3; i++) {
            boolean active = (currentTab == i);
            float ty = H - 165f - i * 78f;

            font.setColor(active ? Color.WHITE : new Color(0.75f, 0.85f, 0.95f, 0.9f));
            font.getData().setScale(0.92f);
            font.draw(game.batch, tabTitles[i], 50f, ty + 46f);

            font.setColor(active ? new Color(1f, 0.85f, 0.35f, 1f) : new Color(0.5f, 0.65f, 0.8f, 0.8f));
            font.getData().setScale(0.72f);
            font.draw(game.batch, tabSubtitles[i], 50f, ty + 24f);

            int uncollected = getUnclaimedCountForTab(i);
            if (uncollected > 0) {
                font.setColor(Color.WHITE);
                font.getData().setScale(0.7f);
                layout.setText(font, String.valueOf(uncollected));
                font.draw(game.batch, layout, 235f - layout.width / 2f, ty + 54f);
            }
        }

        // Currency Texts
        font.getData().setScale(0.85f);
        font.setColor(1f, 0.9f, 0.5f, 1f);
        font.draw(game.batch, player.getGold() + " Vàng", 75f, curBoxY + 122f);

        font.setColor(0.6f, 0.9f, 1f, 1f);
        font.draw(game.batch, player.getDiamonds() + " Kim Cương", 75f, curBoxY + 82f);

        font.setColor(1f, 0.65f, 0.75f, 1f);
        font.draw(game.batch, player.getGachaTickets() + " Vé Quay", 75f, curBoxY + 42f);

        font.getData().setScale(1f);
        game.batch.end();
    }

    private void drawMainPanelHeader() {
        sr.begin(ShapeRenderer.ShapeType.Filled);

        float panelX = 280f;
        float panelW = W - panelX - 25f;

        // Top Header Box
        sr.setColor(0.08f, 0.12f, 0.18f, 0.98f);
        drawRoundedRect(sr, panelX, H - 85f, panelW, 65f, 10f);
        sr.setColor(0.2f, 0.35f, 0.5f, 0.7f);
        drawRectOutline(sr, panelX, H - 85f, panelW, 65f, 1.5f);

        // "Claim All" Button if available in current tab
        int claimableInTab = getUnclaimedCountForTab(currentTab);
        if (claimableInTab > 0) {
            float pulse = 0.8f + 0.2f * MathUtils.sin(animTime * 5f);
            sr.setColor(hoverClaimAll ? new Color(1f, 0.85f, 0.3f, 1f) : new Color(0.9f * pulse, 0.7f * pulse, 0.1f * pulse, 1f));
            drawRoundedRect(sr, panelX + panelW - 220f, H - 75f, 205f, 44f, 8f);
            sr.setColor(1f, 1f, 0.6f, 1f);
            drawRectOutline(sr, panelX + panelW - 220f, H - 75f, 205f, 44f, 1.5f);
        }

        // Daily / Weekly Activity Milestone Panel
        if (currentTab < 2) {
            float barY = H - 180f;
            sr.setColor(0.09f, 0.13f, 0.20f, 0.95f);
            drawRoundedRect(sr, panelX, barY, panelW, 85f, 10f);
            sr.setColor(0.2f, 0.35f, 0.5f, 0.6f);
            drawRectOutline(sr, panelX, barY, panelW, 85f, 1.2f);

            int pts = currentTab == 0 ? qm.dailyPoints : qm.weeklyPoints;
            int maxPts = 100;
            float ratio = Math.min(1f, (float) pts / maxPts);

            // Progress Bar Track
            float pbX = panelX + 25f;
            float pbY = barY + 22f;
            float pbW = panelW - 235f;
            float pbH = 18f;

            sr.setColor(0.06f, 0.08f, 0.12f, 1f);
            drawRoundedRect(sr, pbX, pbY, pbW, pbH, 6f);

            // Filled bar
            if (ratio > 0) {
                sr.setColor(0.2f, 0.75f, 1f, 1f);
                drawRoundedRect(sr, pbX, pbY, Math.max(12f, pbW * ratio), pbH, 6f);
            }

            // Chest Claim Button
            boolean claimed = currentTab == 0 ? qm.dailyChestClaimed : qm.weeklyChestClaimed;
            float chestBtnX = panelX + panelW - 185f;
            float chestBtnY = barY + 16f;

            if (claimed) {
                sr.setColor(0.15f, 0.22f, 0.28f, 0.9f);
            } else if (pts >= maxPts) {
                float pulse = 0.8f + 0.2f * MathUtils.sin(animTime * 6f);
                sr.setColor(hoverClaimChest ? new Color(1f, 0.85f, 0.3f, 1f) : new Color(0.9f * pulse, 0.7f * pulse, 0.1f * pulse, 1f));
            } else {
                sr.setColor(0.12f, 0.18f, 0.25f, 0.8f);
            }
            drawRoundedRect(sr, chestBtnX, chestBtnY, 165f, 52f, 8f);

            if (!claimed && pts >= maxPts) {
                sr.setColor(1f, 1f, 0.6f, 1f);
                drawRectOutline(sr, chestBtnX, chestBtnY, 165f, 52f, 1.5f);
            } else {
                sr.setColor(0.2f, 0.35f, 0.5f, 0.6f);
                drawRectOutline(sr, chestBtnX, chestBtnY, 165f, 52f, 1.2f);
            }
        }

        sr.end();

        // Texts for Main Header
        game.batch.begin();

        String panelTitle = currentTab == 0 ? "BẢNG TREO THƯỞNG HẰNG NGÀY" : (currentTab == 1 ? "THỬ THÁCH ĐẠI DƯƠNG HẰNG TUẦN" : "DANH VỌNG VÀ THÀNH TỰU NGƯ PHỦ");
        titleFont.setColor(0.95f, 0.85f, 0.45f, 1f);
        titleFont.draw(game.batch, panelTitle, panelX + 25f, H - 43f);

        if (claimableInTab > 0) {
            font.setColor(0.1f, 0.1f, 0.15f, 1f);
            font.getData().setScale(0.92f);
            String claimAllStr = "★ NHẬN TẤT CẢ (" + claimableInTab + ") ★";
            layout.setText(font, claimAllStr);
            font.draw(game.batch, layout, panelX + panelW - 220f + (205f - layout.width) / 2f, H - 48f);
        }

        if (currentTab < 2) {
            float barY = H - 180f;
            int pts = currentTab == 0 ? qm.dailyPoints : qm.weeklyPoints;
            int maxPts = 100;
            boolean claimed = currentTab == 0 ? qm.dailyChestClaimed : qm.weeklyChestClaimed;

            font.setColor(0.85f, 0.9f, 0.95f, 1f);
            font.getData().setScale(0.9f);
            String ptsText = "ĐIỂM NĂNG ĐỘNG: " + pts + " / " + maxPts + " ĐIỂM";
            font.draw(game.batch, ptsText, panelX + 25f, barY + 68f);

            font.setColor(1f, 0.85f, 0.3f, 1f);
            font.getData().setScale(0.75f);
            String chestRewardPreview = currentTab == 0 ? "(Rương Ngày: +20 KC, +200 Vàng, +200 XP)" : "(Rương Tuần: +100 KC, +1000 Vàng, +1000 XP)";
            font.draw(game.batch, chestRewardPreview, panelX + 280f, barY + 67f);

            // Chest Button Text
            float chestBtnX = panelX + panelW - 185f;
            float chestBtnY = barY + 16f;

            if (claimed) {
                font.setColor(0.4f, 0.85f, 0.5f, 1f);
                font.getData().setScale(0.85f);
                layout.setText(font, "✓ ĐÃ MỞ RƯƠNG");
                font.draw(game.batch, layout, chestBtnX + (165f - layout.width) / 2f, chestBtnY + 32f);
            } else if (pts >= maxPts) {
                font.setColor(0.1f, 0.1f, 0.15f, 1f);
                font.getData().setScale(0.88f);
                layout.setText(font, "MỞ RƯƠNG BÁU");
                font.draw(game.batch, layout, chestBtnX + (165f - layout.width) / 2f, chestBtnY + 33f);
            } else {
                font.setColor(0.6f, 0.7f, 0.8f, 0.8f);
                font.getData().setScale(0.8f);
                layout.setText(font, "CHƯA ĐỦ ĐIỂM");
                font.draw(game.batch, layout, chestBtnX + (165f - layout.width) / 2f, chestBtnY + 32f);
            }
        }

        font.getData().setScale(1f);
        game.batch.end();
    }

    private void drawQuestCards() {
        float panelX = 280f;
        float panelW = W - panelX - 25f;
        float clipY = 20f;
        float clipH = (currentTab < 2) ? H - 215f : H - 115f;

        Rectangle scissor = new Rectangle();
        Rectangle clipBounds = new Rectangle(panelX, clipY, panelW, clipH);
        ScissorStack.calculateScissors(camera, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight(), game.batch.getTransformMatrix(), clipBounds, scissor);
        ScissorStack.pushScissors(scissor);

        List<Quest> list = (currentTab == 0) ? qm.dailyQuests : (currentTab == 1) ? qm.weeklyQuests : qm.achievements;
        List<Quest> displayList = new ArrayList<>(list);
        displayList.sort(new Comparator<Quest>() {
            @Override
            public int compare(Quest q1, Quest q2) {
                int s1 = q1.claimed ? 2 : (q1.isCompleted() ? 0 : 1);
                int s2 = q2.claimed ? 2 : (q2.isCompleted() ? 0 : 1);
                return Integer.compare(s1, s2);
            }
        });

        float startY = (currentTab < 2 ? H - 195f : H - 95f) + scrollY;

        sr.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < displayList.size(); i++) {
            Quest q = displayList.get(i);
            float qx, qy, cardW, cardH;

            if (currentTab < 2) {
                int col = i % 2;
                int row = i / 2;
                cardW = (panelW - 15f) / 2f;
                cardH = 130f;
                qx = panelX + col * (cardW + 15f);
                qy = startY - row * (cardH + 15f);
            } else {
                cardW = panelW;
                cardH = 100f;
                qx = panelX;
                qy = startY - i * (cardH + 12f);
            }

            if (qy - cardH > H || qy < -50f) continue;

            // Card Background
            if (q.claimed) {
                sr.setColor(0.08f, 0.12f, 0.15f, 0.85f);
            } else if (q.isCompleted()) {
                sr.setColor(0.12f, 0.16f, 0.22f, 0.95f);
            } else {
                sr.setColor(0.09f, 0.13f, 0.18f, 0.9f);
            }
            drawRoundedRect(sr, qx, qy - cardH, cardW, cardH, 8f);

            // Card Border
            if (q.claimed) {
                sr.setColor(0.2f, 0.35f, 0.3f, 0.6f);
                drawRectOutline(sr, qx, qy - cardH, cardW, cardH, 1.2f);
            } else if (q.isCompleted()) {
                float pulse = 0.7f + 0.3f * MathUtils.sin(animTime * 5f);
                sr.setColor(1f, 0.85f, 0.3f, pulse);
                drawRectOutline(sr, qx, qy - cardH, cardW, cardH, 2f);
            } else {
                sr.setColor(0.2f, 0.35f, 0.5f, 0.6f);
                drawRectOutline(sr, qx, qy - cardH, cardW, cardH, 1.2f);
            }

            // Progress bar
            float pbW = (currentTab < 2) ? cardW - 160f : cardW - 320f;
            float pbH = 12f;
            float pbX = qx + 18f;
            float pbY = qy - cardH + 18f;

            sr.setColor(0.06f, 0.08f, 0.12f, 1f);
            drawRoundedRect(sr, pbX, pbY, pbW, pbH, 4f);

            float pRatio = Math.min(1f, (float) q.progress / q.target);
            if (pRatio > 0) {
                sr.setColor(q.isCompleted() ? new Color(0.25f, 0.85f, 0.45f, 1f) : new Color(0.25f, 0.65f, 1f, 1f));
                drawRoundedRect(sr, pbX, pbY, Math.max(8f, pbW * pRatio), pbH, 4f);
            }

            // Claim / Status Button
            float btnW = (currentTab < 2) ? 120f : 130f;
            float btnH = 42f;
            float btnX = qx + cardW - btnW - 15f;
            float btnY = qy - cardH + 18f;

            if (q.claimed) {
                sr.setColor(0.12f, 0.18f, 0.22f, 0.8f);
            } else if (q.isCompleted()) {
                boolean isHov = q.id.equals(hoveredClaimQuestId);
                sr.setColor(isHov ? new Color(1f, 0.85f, 0.3f, 1f) : new Color(0.9f, 0.7f, 0.1f, 1f));
            } else {
                sr.setColor(0.14f, 0.20f, 0.28f, 0.8f);
            }
            drawRoundedRect(sr, btnX, btnY, btnW, btnH, 6f);

            if (q.isCompleted() && !q.claimed) {
                sr.setColor(1f, 1f, 0.6f, 1f);
                drawRectOutline(sr, btnX, btnY, btnW, btnH, 1.5f);
            }
        }

        sr.end();

        // Texts for cards
        game.batch.begin();

        for (int i = 0; i < displayList.size(); i++) {
            Quest q = displayList.get(i);
            float qx, qy, cardW, cardH;

            if (currentTab < 2) {
                int col = i % 2;
                int row = i / 2;
                cardW = (panelW - 15f) / 2f;
                cardH = 130f;
                qx = panelX + col * (cardW + 15f);
                qy = startY - row * (cardH + 15f);
            } else {
                cardW = panelW;
                cardH = 100f;
                qx = panelX;
                qy = startY - i * (cardH + 12f);
            }

            if (qy - cardH > H || qy < -50f) continue;

            // Quest Title
            font.setColor(q.claimed ? new Color(0.6f, 0.7f, 0.8f, 0.8f) : new Color(1f, 0.9f, 0.5f, 1f));
            font.getData().setScale(0.95f);
            font.draw(game.batch, q.title, qx + 18f, qy - 18f);

            // Rewards pill on top right of card
            String rewardText = "";
            if (q.rewardPoints > 0) rewardText += "+" + q.rewardPoints + " Điểm  ";
            if (q.rewardDiamonds > 0) rewardText += "+" + q.rewardDiamonds + " KC";
            font.setColor(1f, 0.85f, 0.3f, 1f);
            font.getData().setScale(0.82f);
            layout.setText(font, rewardText);
            font.draw(game.batch, layout, qx + cardW - layout.width - 18f, qy - 18f);

            // Description
            font.setColor(0.75f, 0.82f, 0.9f, 0.85f);
            font.getData().setScale(0.82f);
            font.draw(game.batch, q.description, qx + 18f, qy - 48f);

            // Progress text
            float pbW = (currentTab < 2) ? cardW - 160f : cardW - 320f;
            float pbX = qx + 18f;
            float pbY = qy - cardH + 18f;

            font.setColor(0.85f, 0.9f, 0.95f, 1f);
            font.getData().setScale(0.72f);
            String pText = q.progress + " / " + q.target;
            layout.setText(font, pText);
            font.draw(game.batch, layout, pbX + pbW - layout.width, pbY + 24f);

            // Button label
            float btnW = (currentTab < 2) ? 120f : 130f;
            float btnX = qx + cardW - btnW - 15f;
            float btnY = qy - cardH + 18f;

            font.getData().setScale(0.85f);
            if (q.claimed) {
                font.setColor(0.4f, 0.8f, 0.5f, 1f);
                layout.setText(font, "✓ ĐÃ NHẬN");
            } else if (q.isCompleted()) {
                font.setColor(0.1f, 0.1f, 0.15f, 1f);
                layout.setText(font, "NHẬN");
            } else {
                font.setColor(0.5f, 0.6f, 0.7f, 0.8f);
                layout.setText(font, "CHƯA XONG");
            }
            font.draw(game.batch, layout, btnX + (btnW - layout.width) / 2f, btnY + 27f);
        }

        font.getData().setScale(1f);
        game.batch.end();

        ScissorStack.popScissors();
    }

    private int getUnclaimedCountForTab(int tabIndex) {
        int count = 0;
        List<Quest> list = (tabIndex == 0) ? qm.dailyQuests : (tabIndex == 1) ? qm.weeklyQuests : qm.achievements;
        for (Quest q : list) {
            if (q.isCompleted() && !q.claimed) count++;
        }
        if (tabIndex == 0 && qm.dailyPoints >= 100 && !qm.dailyChestClaimed) count++;
        if (tabIndex == 1 && qm.weeklyPoints >= 100 && !qm.weeklyChestClaimed) count++;
        return count;
    }

    private boolean handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            AudioManager.getInstance().playSound("button_click");
            game.setScreen(playScreen);
            dispose();
            return true;
        }

        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mouse);
        float mx = mouse.x;
        float my = mouse.y;

        // Back button hover
        hoverBack = (mx >= 35f && mx <= 245f && my >= H - 85f && my <= H - 37f);
        if (hoverBack && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            AudioManager.getInstance().playSound("button_click");
            game.setScreen(playScreen);
            dispose();
            return true;
        }

        // Tabs hover
        for (int i = 0; i < 3; i++) {
            float ty = H - 165f - i * 78f;
            hoverTabs[i] = (mx >= 35f && mx <= 245f && my >= ty && my <= ty + 66f);
            if (hoverTabs[i] && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                if (currentTab != i) {
                    currentTab = i;
                    scrollY = 0;
                    AudioManager.getInstance().playSound("button_click");
                }
            }
        }

        float panelX = 280f;
        float panelW = W - panelX - 25f;

        // Claim All button
        int claimableInTab = getUnclaimedCountForTab(currentTab);
        hoverClaimAll = (claimableInTab > 0 && mx >= panelX + panelW - 220f && mx <= panelX + panelW - 15f && my >= H - 75f && my <= H - 31f);
        if (hoverClaimAll && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            claimAllInCurrentTab();
            return false;
        }

        // Milestone Chest Claim
        if (currentTab < 2) {
            float barY = H - 180f;
            int pts = currentTab == 0 ? qm.dailyPoints : qm.weeklyPoints;
            boolean claimed = currentTab == 0 ? qm.dailyChestClaimed : qm.weeklyChestClaimed;
            float chestBtnX = panelX + panelW - 185f;
            float chestBtnY = barY + 16f;

            hoverClaimChest = (!claimed && pts >= 100 && mx >= chestBtnX && mx <= chestBtnX + 165f && my >= chestBtnY && my <= chestBtnY + 52f);
            if (hoverClaimChest && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                claimMilestoneChest();
                return false;
            }
        }

        // Individual Quest Claim Buttons
        hoveredClaimQuestId = null;
        float clipY = 20f;
        float clipH = (currentTab < 2) ? H - 215f : H - 115f;

        if (my >= clipY && my <= clipY + clipH && mx >= panelX && mx <= panelX + panelW) {
            List<Quest> list = (currentTab == 0) ? qm.dailyQuests : (currentTab == 1) ? qm.weeklyQuests : qm.achievements;
            List<Quest> displayList = new ArrayList<>(list);
            displayList.sort(new Comparator<Quest>() {
                @Override
                public int compare(Quest q1, Quest q2) {
                    int s1 = q1.claimed ? 2 : (q1.isCompleted() ? 0 : 1);
                    int s2 = q2.claimed ? 2 : (q2.isCompleted() ? 0 : 1);
                    return Integer.compare(s1, s2);
                }
            });

            float startY = (currentTab < 2 ? H - 195f : H - 95f) + scrollY;

            for (int i = 0; i < displayList.size(); i++) {
                Quest q = displayList.get(i);
                float qx, qy, cardW, cardH;

                if (currentTab < 2) {
                    int col = i % 2;
                    int row = i / 2;
                    cardW = (panelW - 15f) / 2f;
                    cardH = 130f;
                    qx = panelX + col * (cardW + 15f);
                    qy = startY - row * (cardH + 15f);
                } else {
                    cardW = panelW;
                    cardH = 100f;
                    qx = panelX;
                    qy = startY - i * (cardH + 12f);
                }

                float btnW = (currentTab < 2) ? 120f : 130f;
                float btnH = 42f;
                float btnX = qx + cardW - btnW - 15f;
                float btnY = qy - cardH + 18f;

                if (q.isCompleted() && !q.claimed) {
                    if (mx >= btnX && mx <= btnX + btnW && my >= btnY && my <= btnY + btnH) {
                        hoveredClaimQuestId = q.id;
                        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                            claimSingleQuest(q);
                            return false;
                        }
                    }
                }
            }
        }

        // Keyboard scroll
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) scrollY -= 450f * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) scrollY += 450f * delta;
        if (scrollY < 0) scrollY = 0;

        return false;
    }

    private void claimSingleQuest(Quest q) {
        q.claimed = true;
        if (currentTab == 0) qm.dailyPoints += q.rewardPoints;
        if (currentTab == 1) qm.weeklyPoints += q.rewardPoints;
        player.addDiamonds(q.rewardDiamonds);
        SaveManager.savePlayer(player);
        AudioManager.getInstance().playSound("victory");

        String rew = "";
        if (q.rewardPoints > 0) rew += "+" + q.rewardPoints + " Điểm ";
        if (q.rewardDiamonds > 0) rew += "+" + q.rewardDiamonds + " KC";
        toastMsg = "Hoàn thành: " + q.title + " (" + rew + ")!";
        toastTimer = TOAST_DURATION;
    }

    private void claimMilestoneChest() {
        if (currentTab == 0) {
            qm.dailyChestClaimed = true;
            player.addDiamonds(20);
            player.gainGold(200);
            player.gainXp(200);
            toastMsg = "MỞ RƯƠNG NGÀY: +20 KC, +200 Vàng, +200 XP!";
        } else {
            qm.weeklyChestClaimed = true;
            player.addDiamonds(100);
            player.gainGold(1000);
            player.gainXp(1000);
            toastMsg = "MỞ RƯƠNG TUẦN: +100 KC, +1000 Vàng, +1000 XP!";
        }
        toastTimer = TOAST_DURATION;
        SaveManager.savePlayer(player);
        AudioManager.getInstance().playSound("victory");
    }

    private void claimAllInCurrentTab() {
        List<Quest> list = (currentTab == 0) ? qm.dailyQuests : (currentTab == 1) ? qm.weeklyQuests : qm.achievements;
        int totalDiamonds = 0;
        int count = 0;

        for (Quest q : list) {
            if (q.isCompleted() && !q.claimed) {
                q.claimed = true;
                if (currentTab == 0) qm.dailyPoints += q.rewardPoints;
                if (currentTab == 1) qm.weeklyPoints += q.rewardPoints;
                totalDiamonds += q.rewardDiamonds;
                player.addDiamonds(q.rewardDiamonds);
                count++;
            }
        }

        // Also claim chest if ready
        if (currentTab == 0 && qm.dailyPoints >= 100 && !qm.dailyChestClaimed) {
            qm.dailyChestClaimed = true;
            player.addDiamonds(20);
            player.gainGold(200);
            player.gainXp(200);
            totalDiamonds += 20;
            count++;
        } else if (currentTab == 1 && qm.weeklyPoints >= 100 && !qm.weeklyChestClaimed) {
            qm.weeklyChestClaimed = true;
            player.addDiamonds(100);
            player.gainGold(1000);
            player.gainXp(1000);
            totalDiamonds += 100;
            count++;
        }

        if (count > 0) {
            SaveManager.savePlayer(player);
            AudioManager.getInstance().playSound("victory");
            toastMsg = "Đã nhận " + count + " mục thưởng (" + totalDiamonds + " Kim Cương)!";
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

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() { sr.dispose(); }
}
