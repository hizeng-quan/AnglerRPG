package com.fishingrpg.game.screens;
import com.fishingrpg.game.systems.AudioManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fishingrpg.game.FishingRPG;
import com.fishingrpg.game.data.CollectionDatabase;
import com.fishingrpg.game.data.FishDatabase;
import com.fishingrpg.game.data.SizeRecord;
import com.fishingrpg.game.entities.Player;

import java.util.List;

import com.badlogic.gdx.InputProcessor;

public class CatalogScreen implements Screen, InputProcessor {

    private final FishingRPG game;
    private final Screen previousScreen;
    private final Player player;

    private ShapeRenderer sr;
    private BitmapFont font;
    private BitmapFont titleFont;
    private OrthographicCamera camera;
    private Viewport viewport;
    private com.badlogic.gdx.graphics.g2d.GlyphLayout layout;

    private static final float W = 1280f;
    private static final float H = 720f;

    private boolean hoverBack = false;
    private List<CollectionDatabase.Collection> collections;
    private float scrollY = 0f;
    private float maxScrollY = 0f;
    private int currentTab = 0; // 0: Ao Lang, 1: Bai Bien, 2: Dao Xa
    private boolean[] hoverTabs = new boolean[3];
    private java.util.List<CollectionDatabase.Collection> visibleCollections = new java.util.ArrayList<>();

    // To track hovered claim button
    private String hoveredClaimCollectionId = null;
    
    // To track hovered fish for tooltip
    private com.fishingrpg.game.entities.Fish hoveredFish = null;
    private float mouseX, mouseY;

    public CatalogScreen(FishingRPG game, Screen previousScreen, Player player) {
        this.game = game;
        this.previousScreen = previousScreen;
        this.player = player;
        this.collections = CollectionDatabase.getAllCollections();

        sr = new ShapeRenderer();
        font = game.font;
        layout = new com.badlogic.gdx.graphics.g2d.GlyphLayout();
        titleFont = game.titleFont;

        camera = new OrthographicCamera();
        viewport = new FitViewport(W, H, camera);
        camera.update();
        
        updateVisibleCollections();
    }

    private void updateVisibleCollections() {
        visibleCollections.clear();
        String prefix = currentTab == 0 ? "al_" : (currentTab == 1 ? "bb_" : "dx_");
        for (CollectionDatabase.Collection c : collections) {
            if (c.id.startsWith(prefix)) {
                visibleCollections.add(c);
            }
        }
        int rows = (visibleCollections.size() + 1) / 2;
        float totalHeight = rows * 220f;
        maxScrollY = Math.max(0, totalHeight - (H - 180f));
        scrollY = 0f;
    }

    @Override
    public void render(float delta) {
        update(delta);
        if (game.getScreen() != this) return;

        ScreenUtils.clear(new Color(0.05f, 0.1f, 0.15f, 1f));
        camera.update();
        sr.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Draw Collections
        float startX = 60f;
        float startY = H - 160f + scrollY; // Shifted down for tabs
        
        for (int i = 0; i < visibleCollections.size(); i++) {
            CollectionDatabase.Collection c = visibleCollections.get(i);
            
            float x = startX + (i % 2) * 590f;
            float y = startY - (i / 2) * 220f;
            
            // Skip rendering if out of screen (slightly larger bound for safety)
            if (y - 200 > H || y < 0) continue;
            
            // Draw Box
            sr.setColor(0.1f, 0.15f, 0.2f, 0.8f);
            sr.rect(x, y - 200, 560, 200);
            sr.setColor(0.2f, 0.4f, 0.5f, 1f);
            sr.rect(x, y - 200, 560, 2);
            sr.rect(x, y, 560, 2);
            
            // Check caught status
            int caughtCount = 0;
            for (String fName : c.fishNames) {
                if (!SizeRecord.isFirstCatch(fName)) caughtCount++;
            }
            
            boolean completed = (caughtCount == c.fishNames.size());
            boolean claimed = player.getClaimedCollections().contains(c.id);
            
            if (completed && !claimed) {
                // Claim button
                if (c.id.equals(hoveredClaimCollectionId)) {
                    sr.setColor(0.8f, 0.6f, 0.1f, 1f);
                } else {
                    sr.setColor(0.6f, 0.4f, 0.05f, 1f);
                }
                sr.rect(x + 360, y - 35, 180, 30);
            }
            
            // Draw Fishes in Collection
            for (int j = 0; j < c.fishNames.size(); j++) {
                String fishName = c.fishNames.get(j);
                com.fishingrpg.game.entities.Fish ft = FishDatabase.getFishByName(fishName);
                boolean isCaught = !SizeRecord.isFirstCatch(fishName);
                
                float fx = x + 10 + j * 135;
                float fy = y - 190;
                
                if (isCaught && ft != null) {
                    sr.setColor(0.2f, 0.25f, 0.3f, 1f);
                    sr.rect(fx, fy, 125, 140);
                    // Rarity border
                    Color rc = ft.getRarityColor();
                    sr.setColor(rc);
                    sr.rect(fx, fy, 125, 2);
                    sr.rect(fx, fy + 138, 125, 2);
                    sr.rect(fx, fy, 2, 140);
                    sr.rect(fx + 123, fy, 2, 140);
                } else {
                    sr.setColor(0.05f, 0.08f, 0.1f, 1f);
                    sr.rect(fx, fy, 125, 140);
                    sr.setColor(0.3f, 0.3f, 0.3f, 1f);
                    sr.rect(fx, fy, 125, 2);
                }
            }
        }

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();
        
        for (int i = 0; i < visibleCollections.size(); i++) {
            CollectionDatabase.Collection c = visibleCollections.get(i);
            
            float x = startX + (i % 2) * 590f;
            float y = startY - (i / 2) * 220f;
            if (y - 200 > H - 70 || y < 0) continue;
            
            // Title
            font.setColor(1f, 0.9f, 0.5f, 1f);
            font.draw(game.batch, c.name, x + 10, y - 15);
            
            int caughtCount = 0;
            for (String fName : c.fishNames) {
                if (!SizeRecord.isFirstCatch(fName)) caughtCount++;
            }
            
            boolean completed = (caughtCount == c.fishNames.size());
            boolean claimed = player.getClaimedCollections().contains(c.id);
            
            if (claimed) {
                font.setColor(0.5f, 1f, 0.5f, 1f);
                font.draw(game.batch, "ĐÃ NHẬN THƯỞNG", x + 380, y - 15);
            } else if (completed) {
                font.setColor(Color.WHITE);
                font.draw(game.batch, "NHẬN THƯỞNG", x + 395, y - 15);
                font.setColor(1f, 0.8f, 0.2f, 1f);
                font.draw(game.batch, "+" + c.rewardGold + "V +" + c.rewardXp + "XP", x + 400, y - 45);
            } else {
                font.setColor(0.7f, 0.7f, 0.7f, 1f);
                font.draw(game.batch, "Thưởng: " + c.rewardGold + "V, " + c.rewardXp + "XP", x + 360, y - 15);
            }
            
            for (int j = 0; j < c.fishNames.size(); j++) {
                String fishName = c.fishNames.get(j);
                com.fishingrpg.game.entities.Fish ft = FishDatabase.getFishByName(fishName);
                boolean isCaught = !SizeRecord.isFirstCatch(fishName);
                
                float fx = x + 10 + j * 135;
                float fy = y - 190;
                
                if (isCaught && ft != null) {
                    font.setColor(ft.getRarityColor());
                    // Scale down font for fish names if they are long
                    font.getData().setScale(0.8f);
                    // Split fish name if too long or just print
                    font.draw(game.batch, fishName, fx + 5, fy + 125);
                    font.getData().setScale(0.7f);
                    font.setColor(0.8f, 0.8f, 0.8f, 1f);
                    font.draw(game.batch, ft.getRarity().name(), fx + 5, fy + 100);
                    
                    font.setColor(1f, 0.8f, 0.2f, 1f);
                    float wRecord = SizeRecord.getRecord(fishName);
                    float lRecord = SizeRecord.getRecordLength(fishName);
                    font.draw(game.batch, String.format("%.1f", wRecord) + " kg", fx + 5, fy + 70);
                    font.draw(game.batch, String.format("%.1f", lRecord) + " cm", fx + 5, fy + 45);
                    font.getData().setScale(1f);
                } else {
                    font.setColor(0.4f, 0.4f, 0.4f, 1f);
                    font.draw(game.batch, "???", fx + 40, fy + 80);
                }
            }
        }
        
        game.batch.end();
        
        Gdx.gl.glEnable(GL20.GL_BLEND);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        
        // --- UI OVERLAY (TABS & HEADER) ---
        // Header Background
        sr.setColor(0.1f, 0.2f, 0.3f, 1f);
        sr.rect(0, H - 70, W, 70);
        sr.setColor(0.2f, 0.8f, 0.8f, 1f);
        sr.rect(0, H - 72, W, 2);
        
        // Obscure area behind tabs (so collections don't show through)
        sr.setColor(0.05f, 0.1f, 0.15f, 1f);
        sr.rect(0, H - 150, W, 78);

        // Draw Tabs
        float tabW = 200f, tabH = 50f;
        float tabX = W / 2f - (3 * tabW + 2 * 20f) / 2f;
        float tabY = H - 140f;
        for (int i = 0; i < 3; i++) {
            sr.setColor(hoverTabs[i] ? new Color(0.3f, 0.4f, 0.5f, 1f) : new Color(0.2f, 0.3f, 0.4f, 1f));
            if (currentTab == i) sr.setColor(0.4f, 0.6f, 0.8f, 1f);
            sr.rect(tabX + i * (tabW + 20f), tabY, tabW, tabH);
        }

        // Back Button
        sr.setColor(hoverBack ? new Color(0.3f, 0.4f, 0.4f, 1f) : new Color(0.2f, 0.3f, 0.3f, 1f));
        sr.rect(20, H - 55, 100, 40);

        sr.end();
        
        game.batch.begin();
        
        // Draw Tab Texts
        for (int i = 0; i < 3; i++) {
            String txt = i == 0 ? "Ao Làng" : (i == 1 ? "Bãi Biển" : "Đảo Xa");
            game.font.setColor(currentTab == i ? Color.WHITE : Color.LIGHT_GRAY);
            layout.setText(game.font, txt);
            game.font.draw(game.batch, layout, tabX + i * (tabW + 20f) + tabW/2 - layout.width/2, tabY + 35);
        }
        
        // Title Centered
        titleFont.setColor(Color.WHITE);
        titleFont.draw(game.batch, "DANH SÁCH CÁ", W / 2f - 150f, H - 25);
        
        font.setColor(Color.WHITE);
        font.draw(game.batch, "QUAY LẠI", 30, H - 30);

        game.batch.end();

        // Draw tooltip
        if (hoveredFish != null) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(0.1f, 0.1f, 0.15f, 0.9f);
            float tw = 250f;
            float th = 120f;
            float tx = mouseX + 15;
            float ty = mouseY - 15;
            if (tx + tw > W) tx = W - tw;
            if (ty - th < 0) ty = th;
            
            sr.rect(tx, ty - th, tw, th);
            sr.setColor(hoveredFish.getRarityColor());
            sr.rect(tx, ty - th, tw, 2);
            sr.rect(tx, ty, tw, 2);
            sr.rect(tx, ty - th, 2, th);
            sr.rect(tx + tw, ty - th, 2, th);
            sr.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            
            game.batch.begin();
            font.setColor(hoveredFish.getRarityColor());
            font.draw(game.batch, hoveredFish.getName(), tx + 10, ty - 10);
            font.setColor(0.8f, 0.8f, 0.8f, 1f);
            font.draw(game.batch, "HP: " + (int)hoveredFish.getMaxHp() + " | Kéo: " + (int)hoveredFish.getPullStrength(), tx + 10, ty - 35);
            
            String skillsStr = "Skill: ";
            if (hoveredFish.getSkills() == null || hoveredFish.getSkills().isEmpty()) {
                skillsStr += "Không có";
            } else {
                for (int si = 0; si < hoveredFish.getSkills().size(); si++) {
                    String sid = hoveredFish.getSkills().get(si).id;
                    String sname = sid;
                    if (sid.equals("vung_vay")) sname = "Vùng vẫy";
                    else if (sid.equals("ben_bi")) sname = "Bền bỉ";
                    else if (sid.equals("tron_truot")) sname = "Trơn trượt";
                    else if (sid.equals("lan_sau")) sname = "Lặn sâu";
                    else if (sid.equals("quay_bun")) sname = "Quẫy bùn";
                    else if (sid.equals("but_toc")) sname = "Bứt tốc";
                    else if (sid.equals("song_than")) sname = "Sóng thần";
                    else if (sid.equals("cuong_no")) sname = "Cuồng nộ";
                    skillsStr += sname;
                    if (si < hoveredFish.getSkills().size() - 1) skillsStr += ", ";
                }
            }
            font.draw(game.batch, skillsStr, tx + 10, ty - 60);
            
            font.setColor(0.6f, 0.6f, 0.6f, 1f);
            font.draw(game.batch, "Thuộc tính: " + hoveredFish.getRarity().name(), tx + 10, ty - 85);
            game.batch.end();
        }
    }

    private void update(float delta) {
        Vector3 m = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(m);
        mouseX = m.x;
        mouseY = m.y;

        hoverBack = (m.x >= 20 && m.x <= 120 && m.y >= H - 60 && m.y <= H - 20);
        hoveredClaimCollectionId = null;
        hoveredFish = null;

        // Check claim buttons
        float startX = 60f;
        float startY = H - 160f + scrollY;
        
        float tabW = 200f, tabH = 50f;
        float tabX = W / 2f - (3 * tabW + 2 * 20f) / 2f;
        float tabY = H - 140f;
        for (int i = 0; i < 3; i++) {
            hoverTabs[i] = m.x >= tabX + i * (tabW + 20f) && m.x <= tabX + i * (tabW + 20f) + tabW && m.y >= tabY && m.y <= tabY + tabH;
            if (hoverTabs[i] && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                currentTab = i;
                updateVisibleCollections();
            }
        }
        
        for (int i = 0; i < visibleCollections.size(); i++) {
            CollectionDatabase.Collection c = visibleCollections.get(i);
            float x = startX + (i % 2) * 590f;
            float y = startY - (i / 2) * 220f;
            
            if (y - 200 > H - 70 || y < 0) continue;
            
            int caughtCount = 0;
            for (String fName : c.fishNames) {
                if (!SizeRecord.isFirstCatch(fName)) caughtCount++;
            }
            boolean completed = (caughtCount == c.fishNames.size());
            boolean claimed = player.getClaimedCollections().contains(c.id);
            
            if (completed && !claimed) {
                if (m.x >= x + 360 && m.x <= x + 540 && m.y >= y - 35 && m.y <= y - 5) {
                    hoveredClaimCollectionId = c.id;
                    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                        // CLAIM!
                        player.claimCollection(c.id);
                        player.gainGold(c.rewardGold);
                        player.gainXp(c.rewardXp);
                        com.fishingrpg.game.data.SaveManager.savePlayer(player);
                    }
                }
            }
            
            // Check hovered fish
            for (int j = 0; j < c.fishNames.size(); j++) {
                String fishName = c.fishNames.get(j);
                boolean isCaught = !SizeRecord.isFirstCatch(fishName);
                if (isCaught) {
                    float fx = x + 10 + j * 135;
                    float fy = y - 190;
                    if (m.x >= fx && m.x <= fx + 125 && m.y >= fy && m.y <= fy + 140) {
                        hoveredFish = FishDatabase.getFishByName(fishName);
                    }
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && hoverBack)) {
            game.setScreen(previousScreen);
            dispose();
        }
        
        // Handle scroll
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) scrollY -= 400f * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) scrollY += 400f * delta;
        if (scrollY < 0) scrollY = 0;
        if (scrollY > maxScrollY && maxScrollY > 0) scrollY = maxScrollY;
    }

    @Override public void show() {
        AudioManager.getInstance().playBGM("bgm_menu");
        Gdx.input.setInputProcessor(this);
    }
    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {
        if (Gdx.input.getInputProcessor() == this) {
            Gdx.input.setInputProcessor(null);
        }
    }

    @Override
    public void dispose() {
        sr.dispose();
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
        scrollY += amountY * 60f;
        if (scrollY < 0) scrollY = 0;
        if (scrollY > maxScrollY && maxScrollY > 0) scrollY = maxScrollY;
        return true;
    }
}

