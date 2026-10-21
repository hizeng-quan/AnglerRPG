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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fishingrpg.game.FishingRPG;
import com.fishingrpg.game.PlayScreen;
import com.fishingrpg.game.data.FishDatabase;
import com.fishingrpg.game.data.SizeRecord;
import com.fishingrpg.game.entities.MapArea;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.systems.AudioManager;
import com.fishingrpg.game.ui.NotificationUI;

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
    private String message = null;
    private float messageTimer = 0f;
    
    private int hoveredMapIndex = -1;
    private float animTimer = 0f;

    // Background floating ocean particles
    private static final int PARTICLE_COUNT = 30;
    private float[] partX = new float[PARTICLE_COUNT];
    private float[] partY = new float[PARTICLE_COUNT];
    private float[] partSpeed = new float[PARTICLE_COUNT];
    private float[] partRadius = new float[PARTICLE_COUNT];
    private float[] partAlpha = new float[PARTICLE_COUNT];
    private InputAdapter inputAdapter;

    public MapScreen(FishingRPG game, PlayScreen playScreen) {
        this.game = game;
        this.playScreen = playScreen;
        this.player = playScreen.getPlayer();

        sr = new ShapeRenderer();
        layout = new GlyphLayout();

        camera = new OrthographicCamera();
        viewport = new FitViewport(W, H, camera);
        camera.position.set(W / 2, H / 2, 0);

        // Initialize particles
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            partX[i] = MathUtils.random(0, W);
            partY[i] = MathUtils.random(0, H);
            partSpeed[i] = MathUtils.random(15f, 40f);
            partRadius[i] = MathUtils.random(1.5f, 3.5f);
            partAlpha[i] = MathUtils.random(0.2f, 0.6f);
        }

        inputAdapter = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    goBackToHub();
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button != Input.Buttons.LEFT) return false;
                Vector3 m = new Vector3(screenX, screenY, 0);
                viewport.unproject(m);

                if (hoverBack || (m.x >= 25 && m.x <= 155 && m.y >= H - 65 && m.y <= H - 20)) {
                    goBackToHub();
                    return true;
                }

                if (hoveredMapIndex >= 0 && hoveredMapIndex < MapArea.MAPS.length) {
                    MapArea map = MapArea.MAPS[hoveredMapIndex];
                    if (player.getCurrentMap().equals(map.id)) {
                        AudioManager.getInstance().playSFX("sfx_button");
                        playScreen.getFishingManager().state = com.fishingrpg.game.systems.FishingManager.State.PRE_CAST;
                        game.setScreen(playScreen);
                        return true;
                    }

                    if (canUnlockMap(map)) {
                        AudioManager.getInstance().playSFX("sfx_button");
                        player.setCurrentMap(map.id);
                        playScreen.getFishingManager().state = com.fishingrpg.game.systems.FishingManager.State.PRE_CAST;
                        game.setScreen(playScreen);
                    } else {
                        AudioManager.getInstance().playSFX("sfx_button");
                        String reason = "Cần đạt Cấp " + map.requiredLevel;
                        if (map.requiredFishCount > 0 && map.prevMapId != null) {
                            MapArea prev = MapArea.getMap(map.prevMapId);
                            reason += " và câu đủ " + map.requiredFishCount + " loài tại " + prev.name;
                        }
                        showNotification("Chưa mở khóa " + map.name + ": " + reason + "!");
                    }
                    return true;
                }
                return false;
            }
        };
        Gdx.input.setInputProcessor(inputAdapter);
    }

    private void goBackToHub() {
        AudioManager.getInstance().playSFX("sfx_button");
        playScreen.getFishingManager().state = com.fishingrpg.game.systems.FishingManager.State.IDLE;
        player.setCurrentMap("ao_lang");
        game.setScreen(playScreen);
    }

    private void showNotification(String msg) {
        this.message = msg;
        this.messageTimer = 2.5f;
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
        if (Gdx.graphics.getWidth() <= 0 || Gdx.graphics.getHeight() <= 0) return;
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            goBackToHub();
            return;
        }

        animTimer += delta;
        if (messageTimer > 0) messageTimer -= delta;

        // Update particles
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            partY[i] += partSpeed[i] * delta;
            partX[i] += MathUtils.sin(animTimer * 1.5f + i) * 12f * delta;
            if (partY[i] > H + 10f) {
                partY[i] = -10f;
                partX[i] = MathUtils.random(0, W);
            }
        }

        ScreenUtils.clear(new Color(0.04f, 0.07f, 0.12f, 1f));
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        Vector3 m = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(m);

        // Back button hit area
        hoverBack = (m.x >= 25 && m.x <= 155 && m.y >= H - 65 && m.y <= H - 20);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // --- DRAW BACKGROUND SHAPES & PARTICLES ---
        sr.begin(ShapeRenderer.ShapeType.Filled);
        
        // Deep nautical gradient
        sr.rect(0, 0, W, H,
            new Color(0.02f, 0.05f, 0.09f, 1f), new Color(0.02f, 0.05f, 0.09f, 1f),
            new Color(0.06f, 0.12f, 0.20f, 1f), new Color(0.06f, 0.12f, 0.20f, 1f)
        );

        // Decorative subtle ocean grid / compass lines
        sr.setColor(0.2f, 0.5f, 0.7f, 0.04f);
        for (float gx = 80; gx < W; gx += 160) {
            sr.rect(gx, 0, 1, H);
        }
        for (float gy = 60; gy < H; gy += 140) {
            sr.rect(0, gy, W, 1);
        }

        // Drifting bioluminescent particles
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            float pulse = 0.5f + 0.5f * MathUtils.sin(animTimer * 2f + i);
            sr.setColor(0.3f, 0.8f, 0.95f, partAlpha[i] * pulse);
            sr.circle(partX[i], partY[i], partRadius[i]);
        }

        // Top Header Glass Bar
        sr.setColor(0.06f, 0.11f, 0.18f, 0.92f);
        sr.rect(0, H - 75, W, 75);
        sr.setColor(0.2f, 0.75f, 0.85f, 0.6f);
        sr.rect(0, H - 76, W, 2);

        // Back Button
        sr.setColor(hoverBack ? new Color(0.25f, 0.55f, 0.75f, 0.95f) : new Color(0.12f, 0.22f, 0.32f, 0.9f));
        drawRoundedRect(sr, 25, H - 65, 130, 44, 8);
        sr.setColor(hoverBack ? new Color(0.6f, 0.9f, 1f, 1f) : new Color(0.3f, 0.6f, 0.8f, 0.7f));
        drawRectOutline(sr, 25, H - 65, 130, 44, 2);

        // Player current status pill on top right
        float pillW = 260f;
        float pillH = 44f;
        float pillX = W - pillW - 25f;
        float pillY = H - 65f;
        sr.setColor(0.10f, 0.18f, 0.26f, 0.9f);
        drawRoundedRect(sr, pillX, pillY, pillW, pillH, 8);
        sr.setColor(0.25f, 0.7f, 0.5f, 0.8f);
        drawRectOutline(sr, pillX, pillY, pillW, pillH, 2);
        // Green status dot
        sr.setColor(0.3f, 0.95f, 0.4f, 0.9f);
        sr.circle(pillX + 22, pillY + pillH / 2f, 7);

        // --- MAP CARDS ---
        float cardWidth = 360f;
        float cardHeight = 520f;
        float gap = 45f;
        float startX = (W - (3 * cardWidth + 2 * gap)) / 2f;
        float startY = 40f;

        hoveredMapIndex = -1;

        for (int i = 0; i < MapArea.MAPS.length; i++) {
            MapArea map = MapArea.MAPS[i];
            float cx = startX + i * (cardWidth + gap);
            float cy = startY;

            boolean hover = m.x >= cx && m.x <= cx + cardWidth && m.y >= cy && m.y <= cy + cardHeight;
            if (hover) hoveredMapIndex = i;

            boolean unlocked = canUnlockMap(map);
            boolean isCurrent = player.getCurrentMap().equals(map.id);

            float drawY = cy + (hover ? 10f : 0f);

            // Card Shadow
            sr.setColor(0f, 0f, 0f, hover ? 0.55f : 0.35f);
            drawRoundedRect(sr, cx + 6, drawY - 6, cardWidth, cardHeight, 14);

            // Card Background Base
            sr.setColor(0.09f, 0.14f, 0.20f, 0.96f);
            drawRoundedRect(sr, cx, drawY, cardWidth, cardHeight, 14);

            // Card Header/Image Box (Top 210px)
            float imgX = cx + 8;
            float imgY = drawY + cardHeight - 218;
            float imgW = cardWidth - 16;
            float imgH = 210;

            // Draw procedural scenic artwork for each map
            drawMapIllustration(sr, map.id, imgX, imgY, imgW, imgH, unlocked, animTimer);

            // Inner divider line below scenery
            sr.setColor(0.18f, 0.32f, 0.45f, 0.9f);
            sr.rect(cx + 8, imgY - 3, cardWidth - 16, 2);

            // Card Outer Glow & Border
            if (isCurrent) {
                // Pulsing glowing golden/emerald border for active map
                float pulse = 0.75f + 0.25f * MathUtils.sin(animTimer * 4f);
                sr.setColor(0.3f * pulse, 0.9f * pulse, 0.5f * pulse, 1f);
                drawRectOutline(sr, cx, drawY, cardWidth, cardHeight, 3);
            } else if (hover && unlocked) {
                sr.setColor(0.4f, 0.85f, 1f, 0.9f);
                drawRectOutline(sr, cx, drawY, cardWidth, cardHeight, 3);
            } else {
                sr.setColor(unlocked ? new Color(0.20f, 0.35f, 0.50f, 0.7f) : new Color(0.16f, 0.20f, 0.26f, 0.7f));
                drawRectOutline(sr, cx, drawY, cardWidth, cardHeight, 2);
            }

            // Requirements Progress Bars
            float barX = cx + 24f;
            float barW = cardWidth - 48f;
            float barH = 10f;
            float barY1 = drawY + 115f; // Level bar
            float barY2 = drawY + 70f;  // Fish bar

            // Level Bar
            sr.setColor(0.06f, 0.09f, 0.14f, 1f);
            drawRoundedRect(sr, barX, barY1, barW, barH, 4);
            float lvlP = MathUtils.clamp((float)player.getLevel() / Math.max(1, map.requiredLevel), 0f, 1f);
            sr.setColor(lvlP >= 1f ? new Color(0.25f, 0.85f, 0.45f, 1f) : new Color(0.3f, 0.65f, 0.95f, 1f));
            if (lvlP > 0) {
                drawRoundedRect(sr, barX + 1, barY1 + 1, (barW - 2) * lvlP, barH - 2, 3);
            }

            // Fish Bar (if required)
            if (map.requiredFishCount > 0 && map.prevMapId != null) {
                sr.setColor(0.06f, 0.09f, 0.14f, 1f);
                drawRoundedRect(sr, barX, barY2, barW, barH, 4);
                int caught = getCaughtFishCountForMap(map.prevMapId);
                float fishP = MathUtils.clamp((float)caught / map.requiredFishCount, 0f, 1f);
                sr.setColor(fishP >= 1f ? new Color(0.25f, 0.85f, 0.45f, 1f) : new Color(0.95f, 0.70f, 0.2f, 1f));
                if (fishP > 0) {
                    drawRoundedRect(sr, barX + 1, barY2 + 1, (barW - 2) * fishP, barH - 2, 3);
                }
            }

            // Bottom Action Button Box
            float btnX = cx + 24f;
            float btnY = drawY + 16f;
            float btnW = cardWidth - 48f;
            float btnH = 40f;

            if (isCurrent) {
                sr.setColor(0.12f, 0.35f, 0.22f, 0.95f);
                drawRoundedRect(sr, btnX, btnY, btnW, btnH, 8);
                sr.setColor(0.35f, 0.90f, 0.55f, 0.9f);
                drawRectOutline(sr, btnX, btnY, btnW, btnH, 2);
            } else if (unlocked) {
                boolean btnHover = hover && m.y >= btnY && m.y <= btnY + btnH;
                sr.setColor(btnHover ? new Color(0.22f, 0.58f, 0.82f, 1f) : new Color(0.14f, 0.38f, 0.56f, 0.95f));
                drawRoundedRect(sr, btnX, btnY, btnW, btnH, 8);
                sr.setColor(btnHover ? new Color(0.6f, 0.95f, 1f, 1f) : new Color(0.3f, 0.75f, 0.95f, 0.8f));
                drawRectOutline(sr, btnX, btnY, btnW, btnH, 2);
            } else {
                sr.setColor(0.12f, 0.14f, 0.18f, 0.9f);
                drawRoundedRect(sr, btnX, btnY, btnW, btnH, 8);
                sr.setColor(0.3f, 0.32f, 0.38f, 0.6f);
                drawRectOutline(sr, btnX, btnY, btnW, btnH, 1.5f);
            }
        }

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // --- DRAW TEXTS & LABELS ---
        game.batch.begin();

        // Header Title
        game.titleFont.setColor(0.95f, 0.85f, 0.45f, 1f);
        layout.setText(game.titleFont, "HẢI TRÌNH KHÁM PHÁ");
        game.titleFont.draw(game.batch, layout, W / 2f - layout.width / 2f, H - 36f);

        // Back button text
        game.font.setColor(hoverBack ? Color.WHITE : new Color(0.8f, 0.9f, 1f, 0.9f));
        layout.setText(game.font, "← QUAY LẠI");
        game.font.draw(game.batch, layout, 25 + (130 - layout.width) / 2f, H - 38);

        // Current status pill text
        MapArea curArea = MapArea.getMap(player.getCurrentMap());
        game.font.setColor(0.7f, 0.9f, 0.8f, 1f);
        String statusStr = "Cấp " + player.getLevel() + " • " + curArea.name;
        game.font.draw(game.batch, statusStr, pillX + 38, pillY + 28);

        // Draw Map Card Texts
        for (int i = 0; i < MapArea.MAPS.length; i++) {
            MapArea map = MapArea.MAPS[i];
            float cx = startX + i * (cardWidth + gap);
            float cy = startY;
            boolean hover = (hoveredMapIndex == i);
            boolean unlocked = canUnlockMap(map);
            boolean isCurrent = player.getCurrentMap().equals(map.id);
            float drawY = cy + (hover ? 10f : 0f);

            // Map Name
            game.titleFont.setColor(unlocked ? (isCurrent ? new Color(0.4f, 1f, 0.6f, 1f) : Color.WHITE) : Color.GRAY);
            layout.setText(game.titleFont, map.name);
            game.titleFont.draw(game.batch, layout, cx + cardWidth / 2f - layout.width / 2f, drawY + cardHeight - 235f);

            // Difficulty / Rating & Specie info
            game.font.setColor(0.95f, 0.8f, 0.3f, unlocked ? 1f : 0.6f);
            String stars = (i == 0) ? "★☆☆☆☆  Độ khó: Dễ" : (i == 1) ? "★★★☆☆  Độ khó: Vừa" : "★★★★★  Độ khó: Cực Khó";
            layout.setText(game.font, stars);
            game.font.draw(game.batch, layout, cx + cardWidth / 2f - layout.width / 2f, drawY + cardHeight - 265f);

            // Descriptive Subtitle
            game.font.setColor(0.6f, 0.75f, 0.85f, unlocked ? 0.9f : 0.5f);
            String desc = (i == 0) ? "Ao hồ yên ả, nguồn nước trù phú" : (i == 1) ? "Sóng vỗ rì rào, đa dạng cá ven bờ" : "Vực sâu thăm thẳm, ngự trị thủy quái";
            game.font.getData().setScale(0.85f);
            layout.setText(game.font, desc);
            game.font.draw(game.batch, layout, cx + cardWidth / 2f - layout.width / 2f, drawY + cardHeight - 290f);
            game.font.getData().setScale(1f);

            // Fish count badge
            int totalFish = getTotalFishCountForMap(map.id);
            int caughtFish = getCaughtFishCountForMap(map.id);
            game.font.setColor(0.4f, 0.85f, 0.95f, unlocked ? 1f : 0.6f);
            String fishStat = "Hệ sinh thái: " + caughtFish + "/" + totalFish + " loài cá";
            game.font.getData().setScale(0.88f);
            layout.setText(game.font, fishStat);
            game.font.draw(game.batch, layout, cx + cardWidth / 2f - layout.width / 2f, drawY + cardHeight - 315f);
            game.font.getData().setScale(1f);

            // Requirements Section
            float tx = cx + 24f;

            // Level text
            boolean lvlOk = player.getLevel() >= map.requiredLevel;
            game.font.setColor(lvlOk ? new Color(0.7f, 0.9f, 0.7f, 1f) : new Color(1f, 0.45f, 0.45f, 1f));
            String lvlText = "Yêu cầu: Cấp " + map.requiredLevel + " (Hiện tại: Cấp " + player.getLevel() + ")";
            game.font.draw(game.batch, lvlText, tx, drawY + 140f);

            // Fish requirement text
            if (map.requiredFishCount > 0 && map.prevMapId != null) {
                MapArea prev = MapArea.getMap(map.prevMapId);
                int c = getCaughtFishCountForMap(map.prevMapId);
                boolean fishOk = c >= map.requiredFishCount;
                game.font.setColor(fishOk ? new Color(0.7f, 0.9f, 0.7f, 1f) : new Color(1f, 0.45f, 0.45f, 1f));
                String fishReqText = "Đã bắt ở " + prev.name + ": " + c + "/" + map.requiredFishCount + " loài";
                game.font.getData().setScale(0.9f);
                game.font.draw(game.batch, fishReqText, tx, drawY + 95f);
                game.font.getData().setScale(1f);
            }

            // Bottom Action Button Text
            float btnX = cx + 24f;
            float btnY = drawY + 16f;
            float btnW = cardWidth - 48f;

            if (isCurrent) {
                game.font.setColor(0.5f, 1f, 0.7f, 1f);
                layout.setText(game.font, "✓ ĐI CÂU NGAY");
                game.font.draw(game.batch, layout, btnX + (btnW - layout.width) / 2f, btnY + 26f);
            } else if (unlocked) {
                game.font.setColor(Color.WHITE);
                layout.setText(game.font, "RA KHƠI NGAY  →");
                game.font.draw(game.batch, layout, btnX + (btnW - layout.width) / 2f, btnY + 26f);
            } else {
                game.font.setColor(0.6f, 0.6f, 0.65f, 1f);
                layout.setText(game.font, "CHƯA MỞ KHÓA");
                game.font.draw(game.batch, layout, btnX + (btnW - layout.width) / 2f, btnY + 26f);
            }
        }

        game.batch.end();

        // Draw Modern Notification Toast if active
        NotificationUI.drawNotification(sr, game.batch, game.font, message, messageTimer, 2.5f, W, H);
    }

    /**
     * Draws procedural visual illustrations for the 3 map zones
     */
    private void drawMapIllustration(ShapeRenderer sr, String mapId, float x, float y, float w, float h, boolean unlocked, float time) {
        if ("ao_lang".equals(mapId)) {
            // Calm Village Pond
            // Water gradient
            sr.rect(x, y, w, h,
                new Color(0.06f, 0.22f, 0.18f, 1f), new Color(0.06f, 0.22f, 0.18f, 1f),
                new Color(0.12f, 0.44f, 0.35f, 1f), new Color(0.12f, 0.44f, 0.35f, 1f)
            );

            // Water ripple circles
            float rip1 = (time * 0.4f) % 1f;
            sr.setColor(0.2f, 0.6f, 0.5f, 0.25f * (1f - rip1));
            sr.circle(x + w * 0.45f, y + h * 0.45f, 15f + rip1 * 40f);

            // Lily pads
            sr.setColor(0.18f, 0.58f, 0.25f, 0.95f);
            sr.circle(x + 60, y + 60, 24);
            sr.circle(x + w - 75, y + 80, 20);
            sr.circle(x + w * 0.5f, y + 130, 28);
            // Lily pad notches (cutout effect with water color)
            sr.setColor(0.08f, 0.30f, 0.24f, 1f);
            sr.triangle(x + 60, y + 60, x + 75, y + 80, x + 85, y + 65);
            sr.triangle(x + w * 0.5f, y + 130, x + w * 0.5f + 25, y + 150, x + w * 0.5f + 30, y + 135);

            // Pink Lotus Blossom
            sr.setColor(0.95f, 0.45f, 0.65f, 0.95f);
            sr.triangle(x + w * 0.5f - 12, y + 125, x + w * 0.5f, y + 150, x + w * 0.5f + 12, y + 125);
            sr.setColor(1f, 0.8f, 0.9f, 1f);
            sr.triangle(x + w * 0.5f - 6, y + 125, x + w * 0.5f, y + 145, x + w * 0.5f + 6, y + 125);

            // Reeds / Cattails on left and right borders
            sr.setColor(0.12f, 0.35f, 0.16f, 0.85f);
            sr.rect(x + 12, y, 4, 110);
            sr.rect(x + 22, y, 4, 90);
            sr.rect(x + w - 20, y, 4, 100);
            // Reed heads
            sr.setColor(0.38f, 0.25f, 0.15f, 0.95f);
            drawRoundedRect(sr, x + 10, y + 70, 8, 35, 3);
            drawRoundedRect(sr, x + 20, y + 55, 8, 30, 3);
            drawRoundedRect(sr, x + w - 22, y + 65, 8, 32, 3);

            // Fireflies
            for (int f = 0; f < 5; f++) {
                float fx = x + 40 + (f * 55 + MathUtils.sin(time * 2f + f) * 15f) % (w - 80);
                float fy = y + 40 + (f * 35 + MathUtils.cos(time * 1.5f + f) * 20f) % (h - 80);
                float fPulse = 0.4f + 0.6f * MathUtils.sin(time * 3f + f);
                sr.setColor(1f, 0.9f, 0.4f, 0.8f * fPulse);
                sr.circle(fx, fy, 3.5f);
            }

        } else if ("bai_bien".equals(mapId)) {
            // Tropical Coast / Beach
            // Sky gradient
            sr.rect(x, y + h * 0.45f, w, h * 0.55f,
                new Color(0.25f, 0.65f, 0.88f, 1f), new Color(0.25f, 0.65f, 0.88f, 1f),
                new Color(0.95f, 0.75f, 0.45f, 1f), new Color(0.95f, 0.75f, 0.45f, 1f)
            );
            // Tropical ocean gradient
            sr.rect(x, y, w, h * 0.45f,
                new Color(0.12f, 0.48f, 0.65f, 1f), new Color(0.12f, 0.48f, 0.65f, 1f),
                new Color(0.20f, 0.72f, 0.82f, 1f), new Color(0.20f, 0.72f, 0.82f, 1f)
            );

            // Sun in sky
            sr.setColor(1f, 0.92f, 0.65f, 0.9f);
            sr.circle(x + w * 0.75f, y + h * 0.80f, 22f);

            // Flying gulls
            sr.setColor(1f, 1f, 1f, 0.85f);
            drawGull(sr, x + 80, y + h * 0.75f, 12);
            drawGull(sr, x + 115, y + h * 0.82f, 9);

            // Sandy Shore Arc
            sr.setColor(0.90f, 0.78f, 0.48f, 1f);
            sr.triangle(x, y, x + w * 0.65f, y, x, y + 80);

            // Foam wave crest
            float wavePulse = MathUtils.sin(time * 2f) * 6f;
            sr.setColor(1f, 1f, 1f, 0.7f);
            sr.rectLine(x, y + 80 + wavePulse, x + w * 0.65f + wavePulse, y, 4);

            // Coconut Palm Silhouette in bottom left
            sr.setColor(0.12f, 0.28f, 0.20f, 0.95f);
            // Curved trunk
            sr.rectLine(x + 20, y, x + 40, y + 90, 6);
            // Fronds
            sr.rectLine(x + 40, y + 90, x + 10, y + 105, 3);
            sr.rectLine(x + 40, y + 90, x + 25, y + 115, 3);
            sr.rectLine(x + 40, y + 90, x + 65, y + 115, 3);
            sr.rectLine(x + 40, y + 90, x + 75, y + 95, 3);

        } else if ("dao_xa".equals(mapId)) {
            // Far Island / Abyssal Depths
            // Deep cosmic violet abyss
            sr.rect(x, y, w, h,
                new Color(0.04f, 0.05f, 0.14f, 1f), new Color(0.04f, 0.05f, 0.14f, 1f),
                new Color(0.16f, 0.08f, 0.32f, 1f), new Color(0.16f, 0.08f, 0.32f, 1f)
            );

            // Moon in dark sky
            sr.setColor(0.9f, 0.92f, 1f, 0.9f);
            sr.circle(x + w * 0.22f, y + h * 0.82f, 18);
            sr.setColor(0.14f, 0.08f, 0.28f, 1f);
            sr.circle(x + w * 0.22f + 6, y + h * 0.82f + 4, 15); // crescent cutout

            // Jagged sea rock silhouette
            sr.setColor(0.08f, 0.09f, 0.18f, 1f);
            sr.triangle(x + w * 0.65f, y, x + w * 0.85f, y + 110, x + w, y);
            sr.triangle(x + w * 0.55f, y, x + w * 0.68f, y + 75, x + w * 0.80f, y);

            // Bioluminescent Glowing Jellyfish
            float jPulse = MathUtils.sin(time * 2.5f) * 8f;
            float jx = x + w * 0.42f;
            float jy = y + 80f + jPulse;

            // Jellyfish dome
            sr.setColor(0.35f, 0.85f, 1f, 0.85f);
            sr.circle(jx, jy, 22f);
            sr.setColor(0.85f, 0.35f, 0.95f, 0.7f);
            sr.circle(jx, jy - 2, 16f);

            // Tentacles
            sr.setColor(0.4f, 0.9f, 1f, 0.6f);
            for (int t = -3; t <= 3; t++) {
                float wave = MathUtils.sin(time * 3f + t) * 5f;
                sr.rectLine(jx + t * 5, jy - 15, jx + t * 7 + wave, jy - 55, 2f);
            }

            // Rising glowing bubbles
            for (int b = 0; b < 6; b++) {
                float bx = x + 30 + (b * 45) % (w - 60);
                float by = y + 20 + ((b * 30 + time * 35f) % (h - 40));
                sr.setColor(0.4f, 0.85f, 1f, 0.5f);
                sr.circle(bx, by, 3f);
            }
        }

        // If locked: draw modern frosted glass lock overlay
        if (!unlocked) {
            sr.setColor(0.06f, 0.08f, 0.12f, 0.82f);
            sr.rect(x, y, w, h);

            // Lock plate
            float lkX = x + w / 2f;
            float lkY = y + h / 2f;

            // Lock Shackle (arc)
            sr.setColor(0.85f, 0.75f, 0.35f, 1f);
            sr.circle(lkX, lkY + 18, 18);
            sr.setColor(0.06f, 0.08f, 0.12f, 1f);
            sr.circle(lkX, lkY + 18, 12);
            sr.rect(lkX - 18, lkY + 2, 36, 12);

            // Lock Body
            sr.setColor(0.85f, 0.75f, 0.35f, 1f);
            drawRoundedRect(sr, lkX - 22, lkY - 20, 44, 32, 6);

            // Keyhole
            sr.setColor(0.1f, 0.1f, 0.15f, 1f);
            sr.circle(lkX, lkY - 6, 4.5f);
            sr.triangle(lkX - 3, lkY - 14, lkX, lkY - 6, lkX + 3, lkY - 14);
        }
    }

    private void drawGull(ShapeRenderer sr, float x, float y, float size) {
        sr.rectLine(x - size, y, x - size * 0.3f, y + size * 0.4f, 2f);
        sr.rectLine(x - size * 0.3f, y + size * 0.4f, x, y, 2f);
        sr.rectLine(x, y, x + size * 0.3f, y + size * 0.4f, 2f);
        sr.rectLine(x + size * 0.3f, y + size * 0.4f, x + size, y, 2f);
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
        Gdx.input.setInputProcessor(inputAdapter);
    }

    @Override
    public void resize(int w, int h) {
        if (w <= 0 || h <= 0) return;
        viewport.update(w, h, true);
    }

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

