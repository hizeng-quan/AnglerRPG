package com.fishingrpg.game.screens;
import com.fishingrpg.game.systems.AudioManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fishingrpg.game.FishingRPG;
import com.fishingrpg.game.PlayScreen;

public class MainMenuScreen implements Screen {

    private static final float W = 1280f;
    private static final float H = 720f;

    private static final String[] ITEMS = { "BẮT ĐẦU CÂU", "THOÁT" };

    private final FishingRPG   game;
    private final ShapeRenderer sr;
    private final BitmapFont    subFont;
    private final BitmapFont    menuFont;
    private final GlyphLayout   layout = new GlyphLayout();
    private final Texture       logo;

    private final OrthographicCamera camera;
    private final Viewport           viewport;

    private int   selected  = -1;
    private float timer     = 0f;

    private final float[] fishX     = new float[5];
    private final float[] fishY     = new float[5];
    private final float[] fishSpeed = new float[5];
    private final float[] fishSize  = new float[5];

    public MainMenuScreen(FishingRPG game) {
        this.game = game;
        this.sr   = new ShapeRenderer();

        subFont = game.font;
        menuFont = game.titleFont;
        logo = new Texture("logo.png");

        camera   = new OrthographicCamera();
        viewport = new FitViewport(W, H, camera);
        camera.update();

        for (int i = 0; i < fishX.length; i++) resetFish(i, true);
    }

    private void resetFish(int i, boolean anywhere) {
        fishX[i]     = anywhere ? MathUtils.random(0, W) : -40f;
        fishY[i]     = MathUtils.random(200f, 350f);
        fishSpeed[i] = MathUtils.random(25f, 65f);
        fishSize[i]  = MathUtils.random(14f, 28f);
    }

    @Override
    public void render(float delta) {
        update(delta);
        if (game.getScreen() != this) return; 
        draw();
    }

    private void update(float delta) {
        timer += delta;

        com.badlogic.gdx.math.Vector3 mousePos = new com.badlogic.gdx.math.Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mousePos);

        float btnW = 400f, btnH = 80f;
        float btnX = (W - btnW) / 2f;
        boolean mouseMoved = Gdx.input.getDeltaX() != 0 || Gdx.input.getDeltaY() != 0;
        int hovered = -1;

        for (int i = 0; i < ITEMS.length; i++) {
            float btnY = 250f - i * 110f;
            if (mousePos.x >= btnX && mousePos.x <= btnX + btnW &&
                mousePos.y >= btnY && mousePos.y <= btnY + btnH) {
                hovered = i;
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    activate(i);
                    return;
                }
            }
        }
        
        if (mouseMoved) {
            selected = hovered;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)   || Gdx.input.isKeyJustPressed(Input.Keys.W))
            selected = selected == -1 ? ITEMS.length - 1 : (selected - 1 + ITEMS.length) % ITEMS.length;
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)  || Gdx.input.isKeyJustPressed(Input.Keys.S))
            selected = selected == -1 ? 0 : (selected + 1) % ITEMS.length;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (selected != -1) activate(selected);
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            return;
        }

        for (int i = 0; i < fishX.length; i++) {
            fishX[i] += fishSpeed[i] * delta;
            if (fishX[i] > W + 50) resetFish(i, false);
        }
    }

    private void activate(int item) {
        Gdx.app.postRunnable(() -> {
            if (item == 0) {
                game.setScreen(new PlayScreen(game));
                dispose();
            } else {
                Gdx.app.exit();
            }
        });
    }

    private void draw() {
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.5f, 0.75f, 0.9f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        drawBackground();
        drawBeachDetails();
        drawSun();
        drawClouds();
        drawFishSilhouettes();
        drawToolkit();
        drawButtons();
        sr.end();
        
        sr.begin(ShapeRenderer.ShapeType.Line);
        drawWaves();
        drawSeagulls();
        sr.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();
        drawTextAndLogo();
        game.batch.end();
    }

    private void drawBackground() {
        // Sky
        sr.setColor(0.5f, 0.75f, 0.9f, 1f); sr.rect(0, 380, W, H - 380);
        sr.setColor(0.6f, 0.85f, 0.95f, 1f); sr.rect(0, 380, W, 100);

        // Ocean
        sr.setColor(0.1f, 0.5f, 0.7f, 1f); sr.rect(0, 180, W, 200);
        sr.setColor(0.15f, 0.6f, 0.8f, 0.8f); sr.rect(0, 180, W, 150);

        // White sandy beach
        sr.setColor(0.95f, 0.95f, 0.85f, 1f); sr.rect(0, 0, W, 180);
        // Beach curve / wet sand
        sr.setColor(0.85f, 0.85f, 0.75f, 1f); sr.rect(0, 175, W, 5);
    }

    private void drawSun() {
        sr.setColor(1f, 0.95f, 0.3f, 0.9f);
        sr.circle(W - 200f, 600f, 55f, 40);
        sr.setColor(1f, 0.95f, 0.5f, 0.5f);
        sr.circle(W - 200f, 600f, 75f + 5f * MathUtils.sin(timer * 3f), 40);
    }

    private void drawClouds() {
        sr.setColor(1f, 1f, 1f, 0.8f);
        for (int i = 0; i < 8; i++) {
            float cloudX = (timer * 20f + i * 200) % (W + 200) - 100;
            float cloudY = 500 + (i % 3) * 60;
            sr.circle(cloudX, cloudY, 30f);
            sr.circle(cloudX + 35, cloudY + 15, 40f);
            sr.circle(cloudX + 70, cloudY, 25f);
        }
    }

    private void drawFishSilhouettes() {
        for (int i = 0; i < fishX.length; i++) {
            float alpha = 0.4f + 0.2f * MathUtils.sin(timer * 0.8f + i);
            sr.setColor(0.05f, 0.3f, 0.5f, alpha);
            float cx = fishX[i], cy = fishY[i], s = fishSize[i];
            for (int j = 0; j < 8; j++) {
                float a1 = (float)(j     * Math.PI * 2 / 8);
                float a2 = (float)((j+1) * Math.PI * 2 / 8);
                sr.triangle(cx, cy,
                    cx + MathUtils.cos(a1) * s,        cy + MathUtils.sin(a1) * s * 0.45f,
                    cx + MathUtils.cos(a2) * s,        cy + MathUtils.sin(a2) * s * 0.45f);
            }
            sr.triangle(cx - s, cy,
                cx - s - s*0.55f, cy + s*0.38f,
                cx - s - s*0.55f, cy - s*0.38f);
        }
    }

    private void drawBeachDetails() {
        // Sandcastle
        sr.setColor(0.9f, 0.85f, 0.7f, 1f); 
        sr.rect(800, 110, 60, 40); 
        sr.rect(810, 150, 40, 20); 
        sr.rect(820, 170, 20, 20); 
        sr.triangle(820, 190, 840, 190, 830, 210); 
        sr.setColor(0.8f, 0.75f, 0.6f, 1f); 
        sr.rect(825, 110, 10, 15);
        sr.rect(805, 150, 10, 10);
        sr.rect(845, 150, 10, 10);
        
        // Seashells
        sr.setColor(1f, 0.8f, 0.8f, 1f); 
        sr.circle(450, 120, 6);
        sr.circle(445, 118, 4);
        sr.circle(455, 118, 4);
        sr.setColor(0.9f, 0.9f, 1f, 1f); 
        sr.circle(380, 105, 5);
        sr.circle(375, 103, 3);
        
        // Rocks
        sr.setColor(0.4f, 0.45f, 0.5f, 1f);
        sr.circle(80, 100, 35);
        sr.circle(40, 95, 25);
        sr.circle(110, 90, 20);
        sr.setColor(0.35f, 0.4f, 0.45f, 1f);
        sr.circle(80, 115, 15);
        
        // Umbrella
        float ux = 300f, uy = 120f;
        sr.setColor(0.5f, 0.3f, 0.1f, 1f); // Stick
        sr.rectLine(ux, uy, ux + 30, uy + 180, 10f);
        sr.setColor(0.9f, 0.2f, 0.2f, 1f); // Red part
        sr.triangle(ux - 120, uy + 130, ux + 30, uy + 180, ux + 30, uy + 230);
        sr.setColor(1f, 0.9f, 0.9f, 1f); // White part
        sr.triangle(ux + 180, uy + 130, ux + 30, uy + 180, ux + 30, uy + 230);
        
        // Palm tree on the right
        float px = 1150f;
        sr.setColor(0.4f, 0.25f, 0.15f, 1f);
        sr.rectLine(px, 130, px - 20, 380, 15f); // Trunk leans left
        sr.setColor(0.2f, 0.6f, 0.2f, 1f);
        sr.circle(px - 20, 380, 40);
        sr.triangle(px - 20, 380, px - 100, 350, px - 50, 420);
        sr.triangle(px - 20, 380, px - 50, 460, px + 20, 430);
        sr.triangle(px - 20, 380, px + 50, 360, px, 330);
        sr.triangle(px - 20, 380, px - 70, 320, px - 30, 340);
        sr.setColor(0.3f, 0.2f, 0.1f, 1f);
        sr.circle(px - 30, 360, 12);
        sr.circle(px - 10, 365, 10);
        sr.circle(px - 20, 350, 11);
    }

    private void drawButtons() {
        float btnW = 400f, btnH = 80f;
        float btnX = (W - btnW) / 2f;
        for (int i = 0; i < ITEMS.length; i++) {
            float btnY = 250f - i * 110f;
            if (i == selected) {
                sr.setColor(0.2f, 0.6f, 1f, 0.35f);
                sr.rect(btnX - 8, btnY - 8, btnW + 16, btnH + 16);
                sr.setColor(0.1f, 0.4f, 0.8f, 0.95f);
            } else {
                sr.setColor(0.1f, 0.2f, 0.4f, 0.85f);
            }
            sr.rect(btnX, btnY, btnW, btnH);
            sr.setColor(i == selected ? 1f : 0.4f, i == selected ? 0.85f : 0.25f, 0.2f, 0.6f);
            sr.rect(btnX, btnY + btnH - 6, btnW, 6);
        }
    }



    private void drawWaves() {
        float step = 5f;
        sr.setColor(0.2f, 0.65f, 0.85f, 0.8f);
        for (float x = 0; x < W - step; x += step) {
            float y1 = 330 + 6f * MathUtils.sin((x       + timer * 60f) * 0.02f);
            float y2 = 330 + 6f * MathUtils.sin((x + step + timer * 60f) * 0.02f);
            sr.line(x, y1, x + step, y2);
        }
        
        sr.setColor(1f, 1f, 1f, 0.9f);
        float waveOffset = 10f * MathUtils.sin(timer * 2f);
        for (float x = 0; x < W - step; x += step) {
            float y1 = 180 + waveOffset + 3f * MathUtils.sin((x       + timer * 100f) * 0.05f);
            float y2 = 180 + waveOffset + 3f * MathUtils.sin((x + step + timer * 100f) * 0.05f);
            sr.line(x, y1, x + step, y2);
        }
        sr.setColor(0.9f, 0.95f, 1f, 0.7f);
        for (float x = 0; x < W - step; x += step) {
            float y1 = 175 + waveOffset + 4f * MathUtils.sin((x       + timer * 80f) * 0.04f);
            float y2 = 175 + waveOffset + 4f * MathUtils.sin((x + step + timer * 80f) * 0.04f);
            sr.line(x, y1, x + step, y2);
        }
    }
    
    private void drawSeagulls() {
        sr.setColor(0.3f, 0.3f, 0.3f, 0.7f);
        for (int j=0; j<5; j++) {
            float birdX = W - ((timer * 50f + j*180) % (W + 200)) + 100;
            float birdY = 450 + 30f * MathUtils.sin(timer * 1.5f + j);
            float wingY = birdY + 15f * MathUtils.sin(timer * 8f + j);
            sr.line(birdX, birdY, birdX + 15, wingY);
            sr.line(birdX + 15, wingY, birdX + 30, birdY);
        }
    }

    private void drawToolkit() {
        // Tackle box
        sr.setColor(0.2f, 0.5f, 0.3f, 1f); 
        sr.rect(130, 110, 80, 45);
        sr.setColor(0.15f, 0.4f, 0.25f, 1f);
        sr.rect(130, 110, 80, 15);
        sr.setColor(0.1f, 0.1f, 0.1f, 1f);
        sr.rect(160, 155, 20, 10);
        sr.setColor(0.2f, 0.5f, 0.3f, 1f);
        sr.rect(163, 155, 14, 7);
        sr.setColor(0.8f, 0.8f, 0.8f, 1f);
        sr.rect(165, 120, 10, 10);

        // Fishing rod resting
        sr.setColor(0.1f, 0.1f, 0.1f, 1f);
        sr.rectLine(50, 100, 200, 200, 4f);
        sr.setColor(0.8f, 0.2f, 0.2f, 1f);
        sr.rectLine(50, 100, 80, 120, 6f);

        // Bucket
        sr.setColor(0.7f, 0.7f, 0.75f, 1f); 
        sr.rect(250, 110, 40, 50);
        sr.setColor(0.6f, 0.6f, 0.65f, 1f); 
        sr.rect(245, 155, 50, 8); 
    }

    private void drawTextAndLogo() {
        float logoW = 600f;
        float logoH = logoW * logo.getHeight() / logo.getWidth();
        game.batch.draw(logo, (W - logoW) / 2f, H - logoH - 40f, logoW, logoH);

        subFont.setColor(0.2f, 0.3f, 0.5f, 0.9f);
        String sub = "~ Bờ Cát Trắng ~";
        layout.setText(subFont, sub);
        // subFont.draw(game.batch, sub, (W - layout.width) / 2f, H - logoH - 60f); // Actually the logo has subtitle so we can skip this, or keep it below.

        float btnW = 400f, btnH = 80f;
        float btnX = (W - btnW) / 2f;
        for (int i = 0; i < ITEMS.length; i++) {
            float btnY = 250f - i * 110f;
            menuFont.setColor(i == selected ? Color.WHITE : new Color(0.85f, 0.60f, 0.45f, 1f));
            menuFont.getData().setScale(1.2f); // Make text larger to fit new btn size
            layout.setText(menuFont, ITEMS[i]);
            menuFont.draw(game.batch, ITEMS[i],
                btnX + (btnW - layout.width) / 2f,
                btnY + btnH - 22f);
            menuFont.getData().setScale(1f);
        }

        subFont.setColor(0.80f, 0.88f, 0.96f, 1f);
        layout.setText(subFont, "v0.4 Beta");
        subFont.draw(game.batch, "v0.4 Beta", W - layout.width - 12f, 22f);
    }

    @Override public void show()   {
        AudioManager.getInstance().playBGM("bgm_menu");}
    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        sr.dispose();
        logo.dispose();
    }
}

