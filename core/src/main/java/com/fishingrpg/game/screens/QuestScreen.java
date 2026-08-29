package com.fishingrpg.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
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
import com.fishingrpg.game.PlayScreen;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.entities.Quest;
import com.fishingrpg.game.systems.QuestManager;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class QuestScreen implements Screen {
    private final FishingRPG game;
    private final PlayScreen playScreen;
    private final Player player;
    private final QuestManager qm;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final ShapeRenderer sr;
    private final BitmapFont font;
    private final GlyphLayout layout;

    private static final float W = 1280f;
    private static final float H = 720f;

    private int currentTab = 0; // 0: Daily, 1: Weekly, 2: Achievement
    private float scrollY = 0f;
    private String message = "";
    private float messageTimer = 0f;

    public QuestScreen(FishingRPG game, PlayScreen playScreen, Player player) {
        this.game = game;
        this.playScreen = playScreen;
        this.player = player;
        this.qm = player.getQuestManager();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(W, H, camera);
        this.sr = new ShapeRenderer();
        this.font = game.font;
        this.layout = new GlyphLayout();
    }

    @Override
    public void show() { 
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                scrollY += amountY * 60;
                if (scrollY < 0) scrollY = 0;
                
                int itemCount = (currentTab == 0) ? qm.dailyQuests.size() : (currentTab == 1) ? qm.weeklyQuests.size() : qm.achievements.size();
                float itemHeight = (currentTab < 2) ? 150 : 120;
                if (currentTab < 2) itemCount = (int)Math.ceil(itemCount / 2.0);
                float maxScroll = Math.max(0, (itemCount * itemHeight) - ((currentTab < 2) ? 450 : 550));
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
        if (messageTimer > 0) messageTimer -= delta;

        if (handleInput()) return;

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        ScreenUtils.clear(0.1f, 0.15f, 0.2f, 1f);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Sidebar tabs
        float[] tabColors = {0.8f, 0.4f, 0.4f,  0.4f, 0.8f, 0.4f,  0.4f, 0.4f, 0.8f};
        for (int i = 0; i < 3; i++) {
            boolean active = (currentTab == i);
            sr.setColor(active ? new Color(tabColors[i*3], tabColors[i*3+1], tabColors[i*3+2], 1f) : new Color(tabColors[i*3]*0.6f, tabColors[i*3+1]*0.6f, tabColors[i*3+2]*0.6f, 1f));
            sr.rect(50, 500 - i * 100, 200, 80);
        }
        // Main panel
        sr.setColor(0.15f, 0.2f, 0.25f, 1f);
        sr.rect(280, 50, 950, 620);

        // Back btn
        sr.setColor(0.3f, 0.3f, 0.4f, 1f);
        sr.rect(50, 630, 200, 60);
        
        // Currency boxes
        if (player != null) {
            // Gold
            sr.setColor(0.8f, 0.6f, 0.2f, 0.9f);
            sr.rect(50, 150, 200, 40);
            // Diamond
            sr.setColor(0.6f, 0.3f, 0.8f, 0.9f);
            sr.rect(50, 100, 200, 40);
            // Tickets
            sr.setColor(0.8f, 0.2f, 0.3f, 0.9f);
            sr.rect(50, 50, 200, 40);
        }
        
        // Milestone panel
        if (currentTab < 2) {
            sr.setColor(0.2f, 0.25f, 0.3f, 1f);
            sr.rect(300, 550, 910, 100);
            
            // Progress bar
            int pts = currentTab == 0 ? qm.dailyPoints : qm.weeklyPoints;
            int maxPts = 100;
            float pRatio = Math.min(1f, (float)pts / maxPts);
            
            sr.setColor(0.1f, 0.1f, 0.15f, 1f);
            sr.rect(320, 580, 700, 30);
            sr.setColor(0.9f, 0.7f, 0.2f, 1f);
            sr.rect(320, 580, 700 * pRatio, 30);
            
            // Claim button
            boolean claimed = currentTab == 0 ? qm.dailyChestClaimed : qm.weeklyChestClaimed;
            if (claimed) {
                sr.setColor(0.4f, 0.4f, 0.4f, 1f);
            } else if (pts >= maxPts) {
                sr.setColor(0.2f, 0.8f, 0.2f, 1f);
            } else {
                sr.setColor(0.5f, 0.5f, 0.5f, 1f);
            }
            sr.rect(1050, 570, 140, 50);
        }

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();
        
        font.setColor(Color.WHITE);
        layout.setText(font, "NHIỆM VỤ NGÀY");
        font.draw(game.batch, "NHIỆM VỤ NGÀY", 50 + (200 - layout.width)/2f, 550);
        
        layout.setText(font, "NHIỆM VỤ TUẦN");
        font.draw(game.batch, "NHIỆM VỤ TUẦN", 50 + (200 - layout.width)/2f, 450);
        
        layout.setText(font, "THÀNH TỰU");
        font.draw(game.batch, "THÀNH TỰU", 50 + (200 - layout.width)/2f, 350);
        
        layout.setText(font, "QUAY LẠI");
        font.draw(game.batch, "QUAY LẠI", 50 + (200 - layout.width)/2f, 665);
        
        

        List<Quest> list = (currentTab == 0) ? qm.dailyQuests : (currentTab == 1) ? qm.weeklyQuests : qm.achievements;

        if (currentTab < 2) {
            int pts = currentTab == 0 ? qm.dailyPoints : qm.weeklyPoints;
            int maxPts = 100;
            boolean claimed = currentTab == 0 ? qm.dailyChestClaimed : qm.weeklyChestClaimed;
            
            font.setColor(Color.WHITE);
            font.draw(game.batch, "ĐIỂM NĂNG ĐỘNG: " + pts + " / " + maxPts, 320, 630);
            
            layout.setText(font, claimed ? "ĐÃ NHẬN" : "MỞ RƯƠNG");
            font.draw(game.batch, claimed ? "ĐÃ NHẬN" : "MỞ RƯƠNG", 1050 + (140 - layout.width)/2f, 570 + 35);
        }
        
        float sy = (currentTab < 2 ? 500 : 600) + scrollY;
        
        game.batch.end();
        
        Rectangle scissor = new Rectangle();
        Rectangle clipBounds = new Rectangle(280, 50, 950, (currentTab < 2) ? 470 : 570);
        ScissorStack.calculateScissors(camera, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight(), game.batch.getTransformMatrix(), clipBounds, scissor);
        ScissorStack.pushScissors(scissor);
        
        List<Quest> displayList = new ArrayList<>(list);
        displayList.sort(new Comparator<Quest>() {
            @Override
            public int compare(Quest q1, Quest q2) {
                int s1 = q1.claimed ? 2 : (q1.isCompleted() ? 0 : 1);
                int s2 = q2.claimed ? 2 : (q2.isCompleted() ? 0 : 1);
                return Integer.compare(s1, s2);
            }
        });
        
        for (int i = 0; i < displayList.size(); i++) {
            Quest q = displayList.get(i);
            float qy, qx, boxW, pbW, claimX;
            if (currentTab < 2) {
                int col = i % 2;
                int row = i / 2;
                boxW = 445;
                qx = 300 + col * 465;
                qy = sy - row * 150;
                pbW = 280;
                claimX = qx + boxW - 120;
            } else {
                boxW = 910;
                qx = 300;
                qy = sy - i * 120;
                pbW = 400;
                claimX = 1050;
            }
            
            // Only draw if within reasonable bounds to save performance
            if (qy > 1200 || qy < -500) continue; 
            
            Gdx.gl.glEnable(GL20.GL_BLEND);
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(0.2f, 0.25f, 0.3f, 1f);
            sr.rect(qx, qy - 110, boxW, 110);
            
            // PB
            float pRatio = Math.min(1f, (float)q.progress / q.target);
            sr.setColor(0.1f, 0.1f, 0.15f, 1f);
            sr.rect(qx + 20, qy - 95, pbW, 25);
            sr.setColor(0.3f, 0.7f, 0.9f, 1f);
            sr.rect(qx + 20, qy - 95, pbW * pRatio, 25);
            
            // claim btn
            if (q.claimed) {
                sr.setColor(0.4f, 0.4f, 0.4f, 1f);
            } else if (q.isCompleted()) {
                sr.setColor(0.8f, 0.6f, 0.1f, 1f);
            } else {
                sr.setColor(0.5f, 0.5f, 0.5f, 1f);
            }
            sr.rect(claimX, qy - 90, 100, 50);
            
            sr.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            
            game.batch.begin();
            
            font.setColor(1f, 0.9f, 0.5f, 1f);
            font.draw(game.batch, q.title, qx + 20, qy - 15);
            font.setColor(0.8f, 0.8f, 0.8f, 1f);
            font.draw(game.batch, q.description, qx + 20, qy - 45);
            
            font.setColor(Color.WHITE);
            String pText = q.progress + " / " + q.target;
            layout.setText(font, pText);
            font.draw(game.batch, pText, qx + 20 + pbW/2f - layout.width/2f, qy - 77);
            
            String rewardText = "";
            if (q.rewardPoints > 0) rewardText += "+" + q.rewardPoints + " Điểm  ";
            if (q.rewardDiamonds > 0) rewardText += "+" + q.rewardDiamonds + " KC";
            font.setColor(1f, 0.8f, 0.2f, 1f);
            font.draw(game.batch, rewardText, qx + boxW - 120, qy - 15);
            
            font.setColor(Color.WHITE);
            layout.setText(font, q.claimed ? "ĐÃ NHẬN" : "NHẬN");
            font.draw(game.batch, q.claimed ? "ĐÃ NHẬN" : "NHẬN", claimX + (100 - layout.width)/2f, qy - 65 + layout.height/2f);
            game.batch.end();
        }
        
        ScissorStack.popScissors();
        game.batch.begin();

        if (messageTimer > 0) {
            font.setColor(0.3f, 1f, 0.3f, 1f);
            layout.setText(font, message);
            font.draw(game.batch, message, (W - layout.width) / 2f, H - 20);
        }

        if (player != null) {
            font.setColor(Color.WHITE);
            String gText = player.getGold() + " Vàng";
            layout.setText(font, gText);
            font.draw(game.batch, gText, 50 + (200 - layout.width)/2f, 175);
            
            String kText = player.getDiamonds() + " KC";
            layout.setText(font, kText);
            font.draw(game.batch, kText, 50 + (200 - layout.width)/2f, 125);
            
            String tText = player.getGachaTickets() + " Vé Quay";
            layout.setText(font, tText);
            font.draw(game.batch, tText, 50 + (200 - layout.width)/2f, 75);
        }

        game.batch.end();
    }

    private boolean handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(playScreen);
            dispose();
            return true;
        }

        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mouse);
        float mx = mouse.x;
        float my = mouse.y;

        // Scroll
        if (mx > 280) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) scrollY -= 50;
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) scrollY += 50;
            if (scrollY < 0) scrollY = 0;
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (mx >= 50 && mx <= 250 && my >= 630 && my <= 690) {
                game.setScreen(playScreen);
                dispose();
                return true;
            }
            // Tabs
            if (mx >= 50 && mx <= 250) {
                for (int i = 0; i < 3; i++) {
                    if (my >= 500 - i * 100 && my <= 580 - i * 100) {
                        currentTab = i;
                        scrollY = 0;
                    }
                }
            }
            
            // Milestone claim
            if (currentTab < 2 && mx >= 1050 && mx <= 1190 && my >= 570 && my <= 620) {
                int pts = currentTab == 0 ? qm.dailyPoints : qm.weeklyPoints;
                int maxPts = 100;
                boolean claimed = currentTab == 0 ? qm.dailyChestClaimed : qm.weeklyChestClaimed;
                if (!claimed && pts >= maxPts) {
                    if (currentTab == 0) {
                        qm.dailyChestClaimed = true;
                        player.addDiamonds(20);
                        player.gainGold(200);
                        player.gainXp(200);
                        message = "NHẬN THƯỞNG NGÀY: +20 KC, +200 Vàng, +200 XP!";
                    } else {
                        qm.weeklyChestClaimed = true;
                        player.addDiamonds(100);
                        player.gainGold(1000);
                        player.gainXp(1000);
                        message = "NHẬN THƯỞNG TUẦN: +100 KC, +1000 Vàng, +1000 XP!";
                    }
                    messageTimer = 3f;
                    com.fishingrpg.game.data.SaveManager.savePlayer(player);
                }
            }

            // Quests claim
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
            float sy = (currentTab < 2 ? 500 : 600) + scrollY;
            for (int i = 0; i < displayList.size(); i++) {
                Quest q = displayList.get(i);
                float qy, qx, boxW, claimX;
                if (currentTab < 2) {
                    int col = i % 2;
                    int row = i / 2;
                    boxW = 445;
                    qx = 300 + col * 465;
                    qy = sy - row * 150;
                    claimX = qx + boxW - 120;
                } else {
                    boxW = 910;
                    qx = 300;
                    qy = sy - i * 120;
                    claimX = 1050;
                }
                
                // Only allow clicks if within the scissor bounds!
                float maxY = (currentTab < 2) ? 520 : 620;
                float minY = 50;
                if (my > maxY || my < minY) continue;
                
                if (mx >= claimX && mx <= claimX + 100 && my >= qy - 90 && my <= qy - 40) {
                    if (q.isCompleted() && !q.claimed) {
                        q.claimed = true;
                        if (currentTab == 0) qm.dailyPoints += q.rewardPoints;
                        if (currentTab == 1) qm.weeklyPoints += q.rewardPoints;
                        player.addDiamonds(q.rewardDiamonds);
                        message = "Đã nhận thưởng nhiệm vụ!";
                        messageTimer = 2f;
                        com.fishingrpg.game.data.SaveManager.savePlayer(player);
                    }
                }
            }
        }
        return false;
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() { sr.dispose(); }
}
