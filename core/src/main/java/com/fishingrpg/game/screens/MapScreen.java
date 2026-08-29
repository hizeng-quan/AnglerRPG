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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fishingrpg.game.FishingRPG;
import com.fishingrpg.game.PlayScreen;
import com.fishingrpg.game.data.FishDatabase;
import com.fishingrpg.game.data.SizeRecord;
import com.fishingrpg.game.entities.MapArea;
import com.fishingrpg.game.entities.Player;

public class MapScreen implements Screen {

    private final FishingRPG game;
    private final Player player;
    private final PlayScreen playScreen;

    private ShapeRenderer sr;
    private OrthographicCamera camera;
    private Viewport viewport;
    private GlyphLayout layout;

    private static final float W = 1280f;
    private static final float H = 720f;

    private boolean hoverBack = false;
    private String message = "";
    private float messageTimer = 0f;
    
    private int hoveredMapIndex = -1;

    public MapScreen(FishingRPG game, PlayScreen playScreen) {
        this.game = game;
        this.playScreen = playScreen;
        this.player = playScreen.getPlayer();

        sr = new ShapeRenderer();
        layout = new GlyphLayout();

        camera = new OrthographicCamera();
        viewport = new FitViewport(W, H, camera);
        camera.position.set(W / 2, H / 2, 0);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button != Input.Buttons.LEFT) return false;
                Vector3 m = new Vector3(screenX, screenY, 0);
                viewport.unproject(m);

                if (hoverBack) {
                    game.setScreen(playScreen);
                    return true;
                }

                if (hoveredMapIndex >= 0 && hoveredMapIndex < MapArea.MAPS.length) {
                    MapArea map = MapArea.MAPS[hoveredMapIndex];
                    if (canUnlockMap(map)) {
                        player.setCurrentMap(map.id);
                        game.setScreen(playScreen);
                        playScreen.getFishingManager().state = com.fishingrpg.game.systems.FishingManager.State.PRE_CAST;
                    } else {
                        message = "Chưa đủ điều kiện mở khóa " + map.name + "!";
                        messageTimer = 2f;
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private boolean canUnlockMap(MapArea map) {
        if (player.getLevel() < map.requiredLevel) return false;
        if (map.requiredFishCount > 0 && map.prevMapId != null) {
            int count = getCaughtFishCountForMap(map.prevMapId);
            if (count < map.requiredFishCount) return false;
        }
        return true;
    }

    private int getCaughtFishCountForMap(String mapId) {
        int count = 0;
        for (FishDatabase.FishTemplate t : FishDatabase.getAllFishes()) {
            if (t.map != null && t.map.equals(mapId)) {
                if (!SizeRecord.isFirstCatch(t.name)) {
                    count++;
                }
            }
        }
        return count;
    }
    
    private int getTotalFishCountForMap(String mapId) {
        int count = 0;
        for (FishDatabase.FishTemplate t : FishDatabase.getAllFishes()) {
            if (t.map != null && t.map.equals(mapId)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void render(float delta) {
        if (messageTimer > 0) messageTimer -= delta;

        ScreenUtils.clear(new Color(0.05f, 0.1f, 0.15f, 1f));
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        Vector3 m = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(m);

        // Move Back button to Top Left
        hoverBack = m.x >= 20 && m.x <= 120 && m.y >= H - 60 && m.y <= H - 20;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Header
        sr.setColor(0.1f, 0.2f, 0.3f, 1f);
        sr.rect(0, H - 70, W, 70);
        sr.setColor(0.2f, 0.8f, 0.8f, 1f);
        sr.rect(0, H - 72, W, 2);

        // Back button
        sr.setColor(hoverBack ? new Color(0.3f, 0.4f, 0.4f, 1f) : new Color(0.2f, 0.3f, 0.3f, 1f));
        sr.rect(20, H - 55, 100, 40);
        
        float cardWidth = 360f;
        float cardHeight = 480f;
        float startX = (W - (3 * cardWidth + 2 * 60f)) / 2f;
        float startY = (H - cardHeight) / 2f - 10f;
        
        hoveredMapIndex = -1;

        for (int i = 0; i < MapArea.MAPS.length; i++) {
            MapArea map = MapArea.MAPS[i];
            float x = startX + i * (cardWidth + 60f);
            float y = startY;
            
            boolean hover = m.x >= x && m.x <= x + cardWidth && m.y >= y && m.y <= y + cardHeight;
            if (hover) hoveredMapIndex = i;
            boolean unlocked = canUnlockMap(map);
            boolean isCurrent = player.getCurrentMap().equals(map.id);
            
            // Hover pop-up effect
            float drawY = y + (hover && unlocked ? 15f : 0f);
            
            // Card background (Dark gray)
            sr.setColor(0.12f, 0.15f, 0.2f, 1f);
            sr.rect(x, drawY, cardWidth, cardHeight);
            
            // Outer Border
            if (isCurrent) sr.setColor(0.4f, 0.8f, 0.4f, 1f);
            else if (hover && unlocked) sr.setColor(0.6f, 0.8f, 1f, 1f);
            else sr.setColor(0.2f, 0.3f, 0.4f, 1f);
            
            sr.rect(x, drawY, cardWidth, 4);
            sr.rect(x, drawY + cardHeight - 4, cardWidth, 4);
            sr.rect(x, drawY, 4, cardHeight);
            sr.rect(x + cardWidth - 4, drawY, 4, cardHeight);
            
            // Image Placeholder area (Top half)
            float imgY = drawY + cardHeight - 220f;
            if (map.id.equals("ao_lang")) sr.setColor(0.2f, 0.5f, 0.8f, 1f);
            else if (map.id.equals("bai_bien")) sr.setColor(0.6f, 0.8f, 0.9f, 1f);
            else if (map.id.equals("dao_xa")) sr.setColor(0.05f, 0.1f, 0.2f, 1f);
            
            if (!unlocked) sr.setColor(0.15f, 0.15f, 0.15f, 1f); // Darken if locked
            sr.rect(x + 10, imgY, cardWidth - 20, 210);
            
            // Inner line separator
            sr.setColor(0.2f, 0.3f, 0.4f, 1f);
            sr.rect(x + 10, imgY - 4, cardWidth - 20, 2);
            
            // Progress bars background
            float barX = x + 30f;
            float barY1 = drawY + 80f; // Level bar
            float barY2 = drawY + 30f; // Fish bar
            float barW = cardWidth - 60f;
            float barH = 12f;
            
            sr.setColor(0.1f, 0.1f, 0.15f, 1f);
            sr.rect(barX, barY1, barW, barH);
            if (map.requiredFishCount > 0) {
                sr.rect(barX, barY2, barW, barH);
            }
            
            // Progress bars fill
            float lvlP = Math.min(1f, (float)player.getLevel() / Math.max(1, map.requiredLevel));
            sr.setColor(0.3f, 0.6f, 0.9f, 1f);
            if (lvlP >= 1f) sr.setColor(0.3f, 0.8f, 0.3f, 1f);
            sr.rect(barX + 1, barY1 + 1, (barW - 2) * lvlP, barH - 2);
            
            if (map.requiredFishCount > 0 && map.prevMapId != null) {
                int c = getCaughtFishCountForMap(map.prevMapId);
                float fishP = Math.min(1f, (float)c / map.requiredFishCount);
                sr.setColor(0.8f, 0.6f, 0.2f, 1f);
                if (fishP >= 1f) sr.setColor(0.3f, 0.8f, 0.3f, 1f);
                sr.rect(barX + 1, barY2 + 1, (barW - 2) * fishP, barH - 2);
            }
            
            // Overlay dim if locked
            if (!unlocked) {
                sr.setColor(0f, 0f, 0f, 0.6f);
                sr.rect(x, drawY, cardWidth, cardHeight);
            }
        }
        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();
        
        // Header Texts
        game.titleFont.setColor(Color.WHITE);
        game.titleFont.draw(game.batch, "CHỌN BẢN ĐỒ", W / 2f - 140f, H - 25);
        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, "QUAY LẠI", 30, H - 30);
        
        for (int i = 0; i < MapArea.MAPS.length; i++) {
            MapArea map = MapArea.MAPS[i];
            float x = startX + i * (cardWidth + 60f);
            float y = startY;
            boolean hover = m.x >= x && m.x <= x + cardWidth && m.y >= y && m.y <= y + cardHeight;
            boolean unlocked = canUnlockMap(map);
            boolean isCurrent = player.getCurrentMap().equals(map.id);
            float drawY = y + (hover && unlocked ? 15f : 0f);
            
            // Title
            game.titleFont.setColor(unlocked ? Color.WHITE : Color.GRAY);
            layout.setText(game.titleFont, map.name);
            game.titleFont.draw(game.batch, layout, x + cardWidth/2 - layout.width/2, drawY + cardHeight - 250f);
            
            // Status Text
            if (isCurrent) {
                game.font.setColor(0.4f, 1f, 0.4f, 1f);
                layout.setText(game.font, "ĐANG Ở ĐÂY");
                game.font.draw(game.batch, layout, x + cardWidth/2 - layout.width/2, drawY + cardHeight - 290f);
            } else if (!unlocked) {
                game.font.setColor(1f, 0.3f, 0.3f, 1f);
                layout.setText(game.font, "ĐÃ KHÓA");
                game.font.draw(game.batch, layout, x + cardWidth/2 - layout.width/2, drawY + cardHeight - 290f);
            }
            
            // Requirments text
            float tx = x + 30f;
            
            // Level
            game.font.setColor(Color.LIGHT_GRAY);
            String lvlText = "Yêu cầu: Cấp " + map.requiredLevel;
            if (player.getLevel() < map.requiredLevel) game.font.setColor(1f, 0.4f, 0.4f, 1f);
            game.font.draw(game.batch, lvlText, tx, drawY + 115f);
            
            // Fish count
            if (map.requiredFishCount > 0 && map.prevMapId != null) {
                String prevMapName = "";
                for(MapArea m_area : MapArea.MAPS) if(m_area.id.equals(map.prevMapId)) prevMapName = m_area.name;
                
                int c = getCaughtFishCountForMap(map.prevMapId);
                int total = getTotalFishCountForMap(map.prevMapId);
                
                game.font.setColor(Color.LIGHT_GRAY);
                String fishText = "Đã câu " + prevMapName + ": " + c + "/" + total + " (Cần: " + map.requiredFishCount + ")";
                if (c < map.requiredFishCount) game.font.setColor(1f, 0.4f, 0.4f, 1f);
                
                // shrink text slightly if it's too long
                game.font.getData().setScale(0.85f);
                game.font.draw(game.batch, fishText, tx, drawY + 65f);
                game.font.getData().setScale(1f);
            }
        }

        if (messageTimer > 0 && !message.isEmpty()) {
            game.font.setColor(Color.RED);
            layout.setText(game.font, message);
            game.font.draw(game.batch, layout, W / 2f - layout.width / 2f, 100);
        }

        game.batch.end();
    }

    @Override public void show() {}
    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {
        if (Gdx.input.getInputProcessor() != null) {
            Gdx.input.setInputProcessor(null);
        }
    }

    @Override
    public void dispose() {
        sr.dispose();
    }
}
