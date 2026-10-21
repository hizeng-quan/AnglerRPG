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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fishingrpg.game.FishingRPG;
import com.fishingrpg.game.PlayScreen;
import com.fishingrpg.game.systems.AudioManager;

public class MainMenuScreen implements Screen {

    private static final float W = 1280f;
    private static final float H = 720f;

    private static final String[] ITEMS = { "BẮT ĐẦU CÂU", "XUẤT DỮ LIỆU", "NẠP DỮ LIỆU", "RESET DỮ LIỆU", "THOÁT" };
    private static final float BTN_W = 380f;
    private static final float BTN_H = 46f;
    private static final float BTN_START_Y = 280f;
    private static final float BTN_SPACING = 56f;

    private String statusMessage = null;
    private float statusTimer = 0f;
    private boolean statusIsSuccess = true;

    // Reset confirmation modal state
    private boolean showResetModal = false;
    private float resetCountdown = 10f;
    private int modalHoveredButton = -1; // 0: Cancel, 1: Confirm

    private final FishingRPG game;
    private final ShapeRenderer sr;
    private final BitmapFont subFont;
    private final BitmapFont menuFont;
    private final GlyphLayout layout = new GlyphLayout();

    private final OrthographicCamera camera;
    private final Viewport viewport;

    private int selected = -1;
    private float timer = 0f;

    // Atmospheric stars in twilight sky
    private static final int STAR_COUNT = 45;
    private final float[] starX = new float[STAR_COUNT];
    private final float[] starY = new float[STAR_COUNT];
    private final float[] starSize = new float[STAR_COUNT];
    private final float[] starAlpha = new float[STAR_COUNT];

    // Floating coastal embers / bioluminescent sea motes
    private static final int MOTE_COUNT = 32;
    private final float[] moteX = new float[MOTE_COUNT];
    private final float[] moteY = new float[MOTE_COUNT];
    private final float[] moteSpeed = new float[MOTE_COUNT];
    private final float[] moteSize = new float[MOTE_COUNT];
    private final float[] moteAlpha = new float[MOTE_COUNT];

    // Distant fish silhouettes in ocean
    private final float[] fishX = new float[4];
    private final float[] fishY = new float[4];
    private final float[] fishSpeed = new float[4];
    private final float[] fishSize = new float[4];

    public MainMenuScreen(FishingRPG game) {
        this.game = game;
        this.sr = new ShapeRenderer();

        this.subFont = game.font;
        this.menuFont = game.titleFont;

        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(W, H, camera);
        this.camera.position.set(W / 2f, H / 2f, 0);
        this.camera.update();

        // Initialize stars
        for (int i = 0; i < STAR_COUNT; i++) {
            starX[i] = MathUtils.random(0, W);
            starY[i] = MathUtils.random(330f, H);
            starSize[i] = MathUtils.random(1.2f, 2.8f);
            starAlpha[i] = MathUtils.random(0.25f, 0.85f);
        }

        // Initialize floating motes
        for (int i = 0; i < MOTE_COUNT; i++) {
            moteX[i] = MathUtils.random(0, W);
            moteY[i] = MathUtils.random(20f, 320f);
            moteSpeed[i] = MathUtils.random(12f, 32f);
            moteSize[i] = MathUtils.random(2.0f, 4.5f);
            moteAlpha[i] = MathUtils.random(0.3f, 0.85f);
        }

        // Initialize fish
        for (int i = 0; i < fishX.length; i++) {
            resetFish(i, true);
        }
    }

    private void resetFish(int i, boolean anywhere) {
        fishX[i] = anywhere ? MathUtils.random(0, W) : -50f;
        fishY[i] = MathUtils.random(130f, 240f);
        fishSpeed[i] = MathUtils.random(20f, 48f);
        fishSize[i] = MathUtils.random(16f, 26f);
    }

    @Override
    public void render(float delta) {
        if (Gdx.graphics.getWidth() <= 0 || Gdx.graphics.getHeight() <= 0) return;
        update(delta);
        if (game.getScreen() != this) return;
        draw();
    }

    private void update(float delta) {
        timer += delta;
        if (statusTimer > 0f) {
            statusTimer -= delta;
            if (statusTimer <= 0f) statusMessage = null;
        }

        // Update motes drifting upward
        for (int i = 0; i < MOTE_COUNT; i++) {
            moteY[i] += moteSpeed[i] * delta;
            moteX[i] += MathUtils.sin(timer * 1.5f + i) * 12f * delta;
            if (moteY[i] > 360f) {
                moteY[i] = MathUtils.random(10f, 60f);
                moteX[i] = MathUtils.random(0, W);
            }
        }

        // Update fish
        for (int i = 0; i < fishX.length; i++) {
            fishX[i] += fishSpeed[i] * delta;
            if (fishX[i] > W + 60f) resetFish(i, false);
        }

        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mousePos);

        // Reset confirmation modal active interaction
        if (showResetModal) {
            if (resetCountdown > 0f) {
                resetCountdown = Math.max(0f, resetCountdown - delta);
            }

            float modalW = 620f;
            float modalH = 350f;
            float modalX = (W - modalW) / 2f;
            float modalY = (H - modalH) / 2f;

            float btnModalW = 230f;
            float btnModalH = 46f;
            float btnModalY = modalY + 22f;
            float btnCancelX = modalX + 45f;
            float btnConfirmX = modalX + modalW - 45f - btnModalW;

            boolean hoverCancel = mousePos.x >= btnCancelX && mousePos.x <= btnCancelX + btnModalW &&
                                  mousePos.y >= btnModalY && mousePos.y <= btnModalY + btnModalH;
            boolean hoverConfirm = mousePos.x >= btnConfirmX && mousePos.x <= btnConfirmX + btnModalW &&
                                   mousePos.y >= btnModalY && mousePos.y <= btnModalY + btnModalH;

            int newModalHovered = -1;
            if (hoverCancel) {
                newModalHovered = 0;
            } else if (hoverConfirm && resetCountdown <= 0f) {
                newModalHovered = 1;
            }

            boolean mouseMoved = Gdx.input.getDeltaX() != 0 || Gdx.input.getDeltaY() != 0;
            if (mouseMoved && newModalHovered != modalHoveredButton && newModalHovered != -1) {
                AudioManager.getInstance().playSFX("sfx_button");
            }
            modalHoveredButton = newModalHovered;

            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                if (hoverCancel) {
                    AudioManager.getInstance().playSFX("sfx_button");
                    showResetModal = false;
                    return;
                }
                if (hoverConfirm) {
                    if (resetCountdown <= 0f) {
                        performReset();
                        return;
                    } else {
                        AudioManager.getInstance().playSFX("sfx_fail");
                    }
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                AudioManager.getInstance().playSFX("sfx_button");
                showResetModal = false;
                return;
            }

            // Block any background interaction while modal is active
            return;
        }

        float btnX = (W - BTN_W) / 2f;
        boolean mouseMoved = Gdx.input.getDeltaX() != 0 || Gdx.input.getDeltaY() != 0;
        int hovered = -1;

        for (int i = 0; i < ITEMS.length; i++) {
            float btnY = BTN_START_Y - i * BTN_SPACING;
            if (mousePos.x >= btnX && mousePos.x <= btnX + BTN_W &&
                mousePos.y >= btnY && mousePos.y <= btnY + BTN_H) {
                hovered = i;
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    AudioManager.getInstance().playSFX("sfx_button");
                    activate(i);
                    return;
                }
            }
        }

        if (mouseMoved) {
            if (hovered != -1 && hovered != selected) {
                AudioManager.getInstance().playSFX("sfx_button");
            }
            selected = hovered;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            AudioManager.getInstance().playSFX("sfx_button");
            selected = (selected <= 0) ? ITEMS.length - 1 : selected - 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            AudioManager.getInstance().playSFX("sfx_button");
            selected = (selected == -1 || selected >= ITEMS.length - 1) ? 0 : selected + 1;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (selected != -1) {
                AudioManager.getInstance().playSFX("sfx_button");
                activate(selected);
                return;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            System.exit(0);
            return;
        }
    }

    private void activate(int item) {
        Gdx.app.postRunnable(() -> {
            if (item == 0) {
                game.setScreen(new PlayScreen(game));
                dispose();
            } else if (item == 1) {
                handleExport();
            } else if (item == 2) {
                handleImport();
            } else if (item == 3) {
                openResetModal();
            } else {
                Gdx.app.exit();
                System.exit(0);
            }
        });
    }

    private void openResetModal() {
        showResetModal = true;
        resetCountdown = 10.0f;
        modalHoveredButton = -1;
        AudioManager.getInstance().playSFX("sfx_button");
    }

    private void performReset() {
        showResetModal = false;
        try {
            // Safety auto-backup of current state before wiping
            try {
                com.fishingrpg.game.data.SaveManager.savePlayer(game.player);
                java.io.File backupFile = new java.io.File("angler_save_before_reset.json");
                com.fishingrpg.game.data.SaveManager.exportToFile(backupFile);
            } catch (Throwable t) {
                Gdx.app.log("MainMenu", "Auto-backup before reset: " + t.getMessage());
            }

            // Wipe save preferences and records
            com.fishingrpg.game.data.SaveManager.clearSave();

            // Create fresh new Player and save baseline
            game.player = new com.fishingrpg.game.entities.Player();
            com.fishingrpg.game.data.SaveManager.savePlayer(game.player);

            statusMessage = "✓ ĐÃ RESET DỮ LIỆU! Người chơi trở về Cấp 1 (Đã tự động sao lưu).";
            statusTimer = 6.0f;
            statusIsSuccess = true;
            AudioManager.getInstance().playSFX("sfx_success");
        } catch (Exception e) {
            Gdx.app.error("MainMenu", "Reset failed", e);
            statusMessage = "✕ Lỗi khi đặt lại dữ liệu: " + e.getMessage();
            statusTimer = 5.0f;
            statusIsSuccess = false;
            AudioManager.getInstance().playSFX("sfx_fail");
        }
    }

    private void handleExport() {
        try {
            com.fishingrpg.game.data.SaveManager.savePlayer(game.player);
            java.io.File file = new java.io.File("angler_save.json");
            com.fishingrpg.game.data.SaveManager.exportToFile(file);
            String json = com.fishingrpg.game.data.SaveManager.exportDataToString();
            Gdx.app.getClipboard().setContents(json);

            statusMessage = "✓ Đã xuất thành công: 'angler_save.json' & Clipboard!";
            statusTimer = 5.0f;
            statusIsSuccess = true;
            AudioManager.getInstance().playSFX("sfx_success");
        } catch (Exception e) {
            Gdx.app.error("MainMenu", "Export failed", e);
            statusMessage = "✕ Lỗi xuất dữ liệu: " + e.getMessage();
            statusTimer = 5.0f;
            statusIsSuccess = false;
            AudioManager.getInstance().playSFX("sfx_fail");
        }
    }

    private void handleImport() {
        Thread importThread = new Thread(() -> {
            String jsonContent = null;
            String sourceName = "";

            java.awt.FileDialog fd = null;
            try {
                fd = new java.awt.FileDialog((java.awt.Frame) null, "Chọn file lưu trữ AnglerRPG (*.json, *.bak)", java.awt.FileDialog.LOAD);
                fd.setFile("*.json;*.bak");
                fd.setAlwaysOnTop(true);
                fd.setVisible(true);
                String f = fd.getFile();
                String d = fd.getDirectory();
                if (f != null && d != null) {
                    java.io.File selected = new java.io.File(d, f);
                    if (selected.exists() && com.fishingrpg.game.data.SaveManager.importFromFile(selected, game.player)) {
                        sourceName = selected.getName();
                        jsonContent = "OK";
                    }
                }
            } catch (Throwable t) {
                Gdx.app.log("MainMenu", "Native FileDialog fallback: " + t.getMessage());
            } finally {
                if (fd != null) {
                    try { fd.dispose(); } catch (Throwable ignored) {}
                }
            }

            // Fallback 1: check local angler_save_restored.json
            if (jsonContent == null) {
                java.io.File restored = new java.io.File("angler_save_restored.json");
                if (restored.exists() && com.fishingrpg.game.data.SaveManager.importFromFile(restored, game.player)) {
                    sourceName = "angler_save_restored.json";
                    jsonContent = "OK";
                }
            }

            // Fallback 2: check local angler_save.json
            if (jsonContent == null) {
                java.io.File local = new java.io.File("angler_save.json");
                if (local.exists() && com.fishingrpg.game.data.SaveManager.importFromFile(local, game.player)) {
                    sourceName = "angler_save.json";
                    jsonContent = "OK";
                }
            }

            // Fallback 3: check user home .prefs/AnglerRPG_Save.bak
            if (jsonContent == null) {
                java.io.File bakFile = new java.io.File(System.getProperty("user.home") + "/.prefs/AnglerRPG_Save.bak");
                if (bakFile.exists() && com.fishingrpg.game.data.SaveManager.importFromFile(bakFile, game.player)) {
                    sourceName = "AnglerRPG_Save.bak";
                    jsonContent = "OK";
                }
            }

            // Fallback 4: check clipboard
            if (jsonContent == null) {
                try {
                    String clip = Gdx.app.getClipboard().getContents();
                    if (clip != null && clip.contains("playerData") && com.fishingrpg.game.data.SaveManager.importDataFromString(clip, game.player)) {
                        sourceName = "Clipboard";
                        jsonContent = "OK";
                    }
                } catch (Exception ignored) {}
            }

            final String fJson = jsonContent;
            final String fSource = sourceName;
            Gdx.app.postRunnable(() -> {
                if (fJson != null) {
                    statusMessage = "✓ Nạp thành công từ " + fSource + " (Cấp " + game.player.getLevel() + " • " + game.player.getGold() + " Vàng)!";
                    statusTimer = 5.0f;
                    statusIsSuccess = true;
                    AudioManager.getInstance().playSFX("sfx_success");
                } else {
                    statusMessage = "✕ Không tìm thấy dữ liệu hợp lệ (Hãy sao chép vào Clipboard hoặc lưu file 'angler_save.json')!";
                    statusTimer = 5.0f;
                    statusIsSuccess = false;
                    AudioManager.getInstance().playSFX("sfx_fail");
                }
            });
        });
        importThread.setDaemon(true);
        importThread.start();
    }

    private void draw() {
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.04f, 0.06f, 0.12f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // 1. Shapes Layer: Sky, Ocean, Coastline, Lights, Embers, and Button backplates
        sr.begin(ShapeRenderer.ShapeType.Filled);
        drawAtmosphericSky();
        drawCelestialSunset();
        drawDistantMountains();
        drawLighthouseAndBeam();
        drawOceanVista();
        drawShorelineAndPier();
        drawFishSilhouettes();
        drawFloatingMotes();
        drawLogoEmblemShapes();
        drawButtonsShapes();
        drawStatusMessageShape();
        sr.end();

        // 2. Line details: Waves, seabirds, and crest accents
        sr.begin(ShapeRenderer.ShapeType.Line);
        drawWaveCrests();
        drawSeabirds();
        sr.end();

        // 3. Batch Layer: Pure Typographic Game Logo, Menu Buttons, Subtitles & Version
        game.batch.begin();
        drawTypographicLogoAndTexts();
        drawButtonTexts();
        drawStatusMessageText();
        drawFooterInfo();
        game.batch.end();

        // 4. Modal Layer: Rendered STRICTLY on top of entire screen when active
        if (showResetModal) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            sr.begin(ShapeRenderer.ShapeType.Filled);
            drawResetModalShapes();
            sr.end();

            sr.begin(ShapeRenderer.ShapeType.Line);
            drawResetModalLines();
            sr.end();

            game.batch.begin();
            drawResetModalTexts();
            game.batch.end();
        }
    }

    // =========================================================================
    // ATMOSPHERIC BACKGROUND RENDERING
    // =========================================================================
    private void drawAtmosphericSky() {
        // Multi-stop vertical twilight gradient
        float horizonY = 260f;
        int bands = 14;
        float bandH = (H - horizonY) / bands;

        for (int b = 0; b < bands; b++) {
            float y1 = horizonY + b * bandH;
            float t = (float)b / bands;

            Color c1, c2;
            if (t < 0.4f) {
                // Horizon warm sunset glow (amber-coral to deep violet)
                float subT = t / 0.4f;
                c1 = new Color(0.48f, 0.22f, 0.24f, 1f).lerp(new Color(0.24f, 0.12f, 0.25f, 1f), subT);
                c2 = new Color(0.48f, 0.22f, 0.24f, 1f).lerp(new Color(0.24f, 0.12f, 0.25f, 1f), (float)(b + 1) / bands / 0.4f);
            } else {
                // Upper twilight into midnight navy
                float subT = (t - 0.4f) / 0.6f;
                c1 = new Color(0.24f, 0.12f, 0.25f, 1f).lerp(new Color(0.04f, 0.06f, 0.15f, 1f), subT);
                c2 = new Color(0.24f, 0.12f, 0.25f, 1f).lerp(new Color(0.04f, 0.06f, 0.15f, 1f), Math.min(1f, subT + 1f / (bands * 0.6f)));
            }
            sr.rect(0, y1, W, bandH + 1f, c1, c1, c2, c2);
        }

        // Faint twinkling stars in upper atmosphere
        for (int i = 0; i < STAR_COUNT; i++) {
            float pulse = 0.5f + 0.5f * MathUtils.sin(timer * 2.2f + i * 1.7f);
            sr.setColor(0.85f, 0.92f, 1f, starAlpha[i] * pulse);
            sr.circle(starX[i], starY[i], starSize[i]);
        }
    }

    private void drawCelestialSunset() {
        float sunCX = 980f;
        float sunCY = 310f;

        // Radiant sunset halo rings
        sr.setColor(1f, 0.45f, 0.15f, 0.06f);
        sr.circle(sunCX, sunCY, 160f);

        sr.setColor(1f, 0.62f, 0.22f, 0.12f);
        sr.circle(sunCX, sunCY, 105f);

        sr.setColor(1f, 0.80f, 0.35f, 0.25f);
        sr.circle(sunCX, sunCY, 65f);

        // Radiant twilight solar core
        sr.setColor(1f, 0.95f, 0.75f, 0.90f);
        sr.circle(sunCX, sunCY, 34f);
        sr.setColor(Color.WHITE);
        sr.circle(sunCX, sunCY, 18f);
    }

    private void drawDistantMountains() {
        float horizonY = 260f;

        // Back mountain ridge (soft indigo haze)
        sr.setColor(0.14f, 0.10f, 0.22f, 0.85f);
        sr.triangle(0f, horizonY, 180f, horizonY + 65f, 380f, horizonY);
        sr.triangle(320f, horizonY, 520f, horizonY + 80f, 720f, horizonY);
        sr.triangle(650f, horizonY, 840f, horizonY + 55f, 1040f, horizonY);

        // Front promontory cliff on the right side
        sr.setColor(0.07f, 0.08f, 0.16f, 0.98f);
        sr.triangle(900f, horizonY, 1140f, horizonY + 75f, W, horizonY + 25f);
        sr.rect(1020f, horizonY, W - 1020f, 40f);
    }

    private void drawLighthouseAndBeam() {
        float lx = 1140f;
        float ly = 300f;

        // Lighthouse stone tower silhouette
        sr.setColor(0.18f, 0.22f, 0.32f, 1f);
        sr.rect(lx - 8f, ly, 16f, 35f);
        // Lantern room gallery
        sr.setColor(0.85f, 0.70f, 0.25f, 1f);
        sr.circle(lx, ly + 40f, 6f);
        sr.setColor(Color.WHITE);
        sr.circle(lx, ly + 40f, 3f);
        // Cap dome
        sr.setColor(0.12f, 0.15f, 0.22f, 1f);
        sr.triangle(lx - 9f, ly + 42f, lx + 9f, ly + 42f, lx, ly + 52f);

        // Sweeping golden searchlight beam across ocean
        float beamAng = (timer * 35f) % 360f;
        if (beamAng > 105f && beamAng < 275f) {
            float rad = beamAng * MathUtils.degreesToRadians;
            float beamLen = 520f;
            float spread = 28f * MathUtils.degreesToRadians;

            float bx1 = lx + MathUtils.cos(rad - spread) * beamLen;
            float by1 = ly + 40f + MathUtils.sin(rad - spread) * beamLen * 0.45f;
            float bx2 = lx + MathUtils.cos(rad + spread) * beamLen;
            float by2 = ly + 40f + MathUtils.sin(rad + spread) * beamLen * 0.45f;

            sr.setColor(1f, 0.92f, 0.5f, 0.12f);
            sr.triangle(lx, ly + 40f, bx1, by1, bx2, by2);
            sr.setColor(1f, 0.96f, 0.7f, 0.22f);
            sr.triangle(lx, ly + 40f, bx1 + (bx2 - bx1) * 0.35f, by1 + (by2 - by1) * 0.35f, bx1 + (bx2 - bx1) * 0.65f, by1 + (by2 - by1) * 0.65f);
        }
    }

    private void drawOceanVista() {
        float horizonY = 260f;
        float shorelineY = 95f;

        // Ocean depth gradient
        sr.rect(0, shorelineY, W, horizonY - shorelineY,
            new Color(0.04f, 0.08f, 0.18f, 1f), new Color(0.04f, 0.08f, 0.18f, 1f),
            new Color(0.08f, 0.15f, 0.28f, 1f), new Color(0.08f, 0.15f, 0.28f, 1f)
        );

        // Shimmering reflective sunset column below the sun
        float sunCX = 980f;
        for (float y = shorelineY + 4f; y < horizonY; y += 7f) {
            float t = (horizonY - y) / (horizonY - shorelineY);
            float spread = 35f + t * 90f;
            float shimmer = 0.5f + 0.5f * MathUtils.sin(timer * 3.5f + y * 0.18f);

            sr.setColor(1f, 0.72f, 0.30f, 0.28f * shimmer * (1f - t * 0.4f));
            sr.rect(sunCX - spread * shimmer, y, spread * 2f * shimmer, 2.5f);
        }
    }

    private void drawShorelineAndPier() {
        // Curved wet sand shoal
        sr.rect(0, 0, W, 95f,
            new Color(0.12f, 0.11f, 0.15f, 1f), new Color(0.12f, 0.11f, 0.15f, 1f),
            new Color(0.20f, 0.16f, 0.20f, 1f), new Color(0.20f, 0.16f, 0.20f, 1f)
        );

        // Wet tide foam creeping along shore
        float tideOffset = 7f * MathUtils.sin(timer * 1.5f);
        sr.setColor(0.6f, 0.85f, 0.95f, 0.35f);
        sr.rect(0, 92f + tideOffset, W, 8f);

        // Left Pier Wooden Pilings silhouette
        sr.setColor(0.06f, 0.08f, 0.12f, 1f);
        sr.rect(70f, 60f, 18f, 110f); // Post 1
        sr.rect(110f, 50f, 16f, 95f);  // Post 2
        sr.rect(60f, 125f, 78f, 14f);  // Cross plank

        // Coiled nautical rope around post
        sr.setColor(0.72f, 0.55f, 0.25f, 0.85f);
        sr.rect(70f, 98f, 18f, 5f);
        sr.rect(70f, 106f, 18f, 5f);
        sr.rect(110f, 88f, 16f, 4f);

        // Silhouette Fishing Rod leaning elegantly on pier towards the stars
        sr.setColor(0.10f, 0.12f, 0.18f, 1f);
        sr.rectLine(82f, 120f, 210f, 280f, 4.2f);
        // Rod reel and guides
        sr.setColor(0.85f, 0.65f, 0.22f, 1f);
        sr.circle(105f, 148f, 7f);
        sr.setColor(0.35f, 0.85f, 1f, 0.9f);
        sr.circle(105f, 148f, 3.5f);
    }

    private void drawFishSilhouettes() {
        for (int i = 0; i < fishX.length; i++) {
            float alpha = 0.28f + 0.15f * MathUtils.sin(timer * 1.2f + i);
            sr.setColor(0.08f, 0.25f, 0.42f, alpha);
            float cx = fishX[i], cy = fishY[i], s = fishSize[i];

            // Smooth fish body
            for (int j = 0; j < 8; j++) {
                float a1 = (float)(j * Math.PI * 2 / 8);
                float a2 = (float)((j + 1) * Math.PI * 2 / 8);
                sr.triangle(cx, cy,
                    cx + MathUtils.cos(a1) * s, cy + MathUtils.sin(a1) * s * 0.42f,
                    cx + MathUtils.cos(a2) * s, cy + MathUtils.sin(a2) * s * 0.42f
                );
            }
            // Tail fin
            sr.triangle(cx - s, cy,
                cx - s - s * 0.5f, cy + s * 0.35f,
                cx - s - s * 0.5f, cy - s * 0.35f
            );
        }
    }

    private void drawFloatingMotes() {
        for (int i = 0; i < MOTE_COUNT; i++) {
            float pulse = 0.5f + 0.5f * MathUtils.sin(timer * 2.5f + i);
            if (i % 2 == 0) {
                sr.setColor(1f, 0.82f, 0.35f, moteAlpha[i] * pulse);
            } else {
                sr.setColor(0.35f, 0.85f, 1f, moteAlpha[i] * pulse);
            }
            sr.circle(moteX[i], moteY[i], moteSize[i]);
        }
    }

    private void drawWaveCrests() {
        // Drifting wave lines on ocean
        sr.setColor(0.28f, 0.65f, 0.85f, 0.45f);
        float step = 6f;
        for (float x = 0; x < W - step; x += step) {
            float y1 = 230f + 4f * MathUtils.sin((x + timer * 40f) * 0.025f);
            float y2 = 230f + 4f * MathUtils.sin((x + step + timer * 40f) * 0.025f);
            sr.line(x, y1, x + step, y2);
        }

        sr.setColor(0.85f, 0.95f, 1f, 0.55f);
        for (float x = 0; x < W - step; x += step) {
            float y1 = 150f + 6f * MathUtils.sin((x - timer * 55f) * 0.035f);
            float y2 = 150f + 6f * MathUtils.sin((x + step - timer * 55f) * 0.035f);
            sr.line(x, y1, x + step, y2);
        }
    }

    private void drawSeabirds() {
        sr.setColor(0.22f, 0.20f, 0.28f, 0.75f);
        for (int j = 0; j < 4; j++) {
            float birdX = W - ((timer * 45f + j * 220f) % (W + 200f)) + 100f;
            float birdY = 510f + 25f * MathUtils.sin(timer * 1.2f + j);
            float wingY = birdY + 14f * MathUtils.sin(timer * 7f + j);
            sr.line(birdX, birdY, birdX + 16f, wingY);
            sr.line(birdX + 16f, wingY, birdX + 32f, birdY);
        }
    }

    // =========================================================================
    // TYPOGRAPHIC LOGO EMBLEM SHAPES (HERALDIC FILIGREE & CREST)
    // =========================================================================
    private void drawLogoEmblemShapes() {
        float logoCX = W / 2f;
        float logoCY = 560f;

        // Soft celestial background aura
        sr.setColor(1f, 0.75f, 0.25f, 0.08f);
        sr.circle(logoCX, logoCY, 220f);
        sr.setColor(0.35f, 0.85f, 1f, 0.06f);
        sr.circle(logoCX, logoCY, 140f);

        // Heraldic Arched Rod Scroll Wings (Left and Right)
        sr.setColor(0.95f, 0.75f, 0.25f, 0.75f);
        // Left Wing Arc
        sr.rectLine(logoCX - 240f, logoCY - 15f, logoCX - 120f, logoCY + 36f, 2.5f);
        sr.rectLine(logoCX - 120f, logoCY + 36f, logoCX - 40f, logoCY + 45f, 2.5f);
        sr.circle(logoCX - 240f, logoCY - 15f, 4f);
        sr.circle(logoCX - 180f, logoCY + 12f, 3f);

        // Right Wing Arc
        sr.rectLine(logoCX + 240f, logoCY - 15f, logoCX + 120f, logoCY + 36f, 2.5f);
        sr.rectLine(logoCX + 120f, logoCY + 36f, logoCX + 40f, logoCY + 45f, 2.5f);
        sr.circle(logoCX + 240f, logoCY - 15f, 4f);
        sr.circle(logoCX + 180f, logoCY + 12f, 3f);

        // Central Heraldic Crown Emblem (Twin Golden Hooks & Compass Star)
        float crestY = logoCY + 52f;
        // Left hook curve
        sr.rectLine(logoCX - 15f, crestY + 12f, logoCX - 15f, crestY - 8f, 2.5f);
        sr.rectLine(logoCX - 15f, crestY - 8f, logoCX - 3f, crestY - 14f, 2.5f);
        sr.rectLine(logoCX - 3f, crestY - 14f, logoCX, crestY - 12f, 2.5f);
        // Right hook curve
        sr.rectLine(logoCX + 15f, crestY + 12f, logoCX + 15f, crestY - 8f, 2.5f);
        sr.rectLine(logoCX + 15f, crestY - 8f, logoCX + 3f, crestY - 14f, 2.5f);
        sr.rectLine(logoCX + 3f, crestY - 14f, logoCX, crestY - 12f, 2.5f);

        // 4-pointed radiant compass star in crest
        sr.setColor(1f, 0.95f, 0.7f, 1f);
        sr.rect(logoCX - 1.5f, crestY - 6f, 3f, 12f);
        sr.rect(logoCX - 6f, crestY - 1.5f, 12f, 3f);
        sr.setColor(Color.WHITE);
        sr.circle(logoCX, crestY, 2.5f);

        // Divider filigree below title
        float divY = logoCY - 48f;
        sr.setColor(0.85f, 0.65f, 0.22f, 0.65f);
        sr.rectLine(logoCX - 220f, divY, logoCX - 18f, divY, 1.5f);
        sr.rectLine(logoCX + 18f, divY, logoCX + 220f, divY, 1.5f);
        // Center diamond
        sr.setColor(1f, 0.85f, 0.35f, 0.9f);
        sr.rect(logoCX - 5f, divY - 5f, 5f, 5f, 10f, 10f, 1f, 1f, 45f);
    }

    // =========================================================================
    // BUTTON SHAPES (GLASSMORPHISM & HOVER GLOW)
    // =========================================================================
    private void drawButtonsShapes() {
        float btnX = (W - BTN_W) / 2f;

        for (int i = 0; i < ITEMS.length; i++) {
            float btnY = BTN_START_Y - i * BTN_SPACING;
            boolean isSel = (i == selected);

            if (isSel) {
                // Outer radiant glow
                float pulse = 0.5f + 0.5f * MathUtils.sin(timer * 5f);
                if (i == 3) {
                    sr.setColor(1f, 0.35f, 0.30f, 0.22f + 0.12f * pulse);
                } else {
                    sr.setColor(1f, 0.85f, 0.35f, 0.22f + 0.12f * pulse);
                }
                drawRoundedRect(sr, btnX - 6f, btnY - 6f, BTN_W + 12f, BTN_H + 12f, 16);

                // Button plate background
                if (i == 3) {
                    sr.setColor(0.24f, 0.08f, 0.12f, 0.96f);
                } else {
                    sr.setColor(0.12f, 0.18f, 0.32f, 0.96f);
                }
                drawRoundedRect(sr, btnX, btnY, BTN_W, BTN_H, 12);

                // High-polish border
                if (i == 3) {
                    sr.setColor(1f, 0.45f, 0.40f, 1f);
                } else {
                    sr.setColor(1f, 0.88f, 0.35f, 1f);
                }
                drawRectOutline(sr, btnX, btnY, BTN_W, BTN_H, 2.5f);

                // Side accent indicator bars
                if (i == 3) {
                    sr.setColor(1f, 0.65f, 0.60f, 1f);
                } else {
                    sr.setColor(1f, 0.92f, 0.5f, 1f);
                }
                sr.rect(btnX + 12f, btnY + 12f, 4f, BTN_H - 24f);
                sr.rect(btnX + BTN_W - 16f, btnY + 12f, 4f, BTN_H - 24f);
            } else {
                // Glassmorphism translucent obsidian plate
                if (i == 3) {
                    sr.setColor(0.12f, 0.06f, 0.08f, 0.88f);
                } else {
                    sr.setColor(0.06f, 0.09f, 0.16f, 0.88f);
                }
                drawRoundedRect(sr, btnX, btnY, BTN_W, BTN_H, 12);

                // Subtle metallic border
                if (i == 3) {
                    sr.setColor(0.55f, 0.25f, 0.28f, 0.70f);
                } else {
                    sr.setColor(0.25f, 0.40f, 0.60f, 0.70f);
                }
                drawRectOutline(sr, btnX, btnY, BTN_W, BTN_H, 1.5f);
            }
        }
    }

    private void drawStatusMessageShape() {
        if (statusMessage == null || statusTimer <= 0f) return;
        float alpha = Math.min(1f, statusTimer / 0.5f);
        float bannerY = 338f;
        layout.setText(subFont, statusMessage);
        float bannerW = Math.max(480f, layout.width + 48f);
        float bannerH = 40f;
        float bannerX = (W - bannerW) / 2f;

        sr.setColor(0.04f, 0.08f, 0.16f, 0.94f * alpha);
        drawRoundedRect(sr, bannerX, bannerY, bannerW, bannerH, 10);

        if (statusIsSuccess) {
            sr.setColor(0.20f, 0.88f, 0.50f, 0.85f * alpha);
        } else {
            sr.setColor(0.95f, 0.32f, 0.32f, 0.85f * alpha);
        }
        drawRectOutline(sr, bannerX, bannerY, bannerW, bannerH, 1.8f);
    }

    // =========================================================================
    // TYPOGRAPHIC TEXTS & LABELS
    // =========================================================================
    private void drawTypographicLogoAndTexts() {
        float logoCX = W / 2f;
        float logoCY = 560f;

        // Primary Title: "ANGLER RPG" using bigFont (size 48 bold)
        BitmapFont titleFont = (game.bigFont != null ? game.bigFont : menuFont);
        titleFont.getData().setScale(1.18f);
        layout.setText(titleFont, "ANGLER RPG");
        float titleX = logoCX - layout.width / 2f;
        float titleY = logoCY + layout.height / 2f;

        // 3D Multi-Pass Drop Shadow for solid weight and depth
        titleFont.setColor(0.01f, 0.02f, 0.05f, 0.95f);
        titleFont.draw(game.batch, "ANGLER RPG", titleX + 4f, titleY - 4f);
        titleFont.draw(game.batch, "ANGLER RPG", titleX + 3f, titleY - 3f);
        titleFont.draw(game.batch, "ANGLER RPG", titleX + 2f, titleY - 2f);

        // Rich Warm Gold Base
        titleFont.setColor(0.92f, 0.65f, 0.15f, 1f);
        titleFont.draw(game.batch, "ANGLER RPG", titleX, titleY - 1f);

        // Radiant Sunburst Highlight on top
        titleFont.setColor(1f, 0.95f, 0.68f, 1f);
        titleFont.draw(game.batch, "ANGLER RPG", titleX, titleY);
        titleFont.getData().setScale(1f);

        // Subtitle: "✦ HUYỀN THOẠI CẦN THỦ • THE WILD ODYSSEY ✦"
        subFont.setColor(0.85f, 0.92f, 1f, 0.95f);
        subFont.getData().setScale(0.92f);
        String sub = "✦  HUYỀN THOẠI CẦN THỦ  •  THE WILD ODYSSEY  ✦";
        layout.setText(subFont, sub);
        subFont.draw(game.batch, layout, logoCX - layout.width / 2f, logoCY - 65f);
        subFont.getData().setScale(1f);
    }

    private void drawButtonTexts() {
        float btnX = (W - BTN_W) / 2f;

        for (int i = 0; i < ITEMS.length; i++) {
            float btnY = BTN_START_Y - i * BTN_SPACING;
            boolean isSel = (i == selected);

            String text = isSel ? ("►  " + ITEMS[i] + "  ◄") : ITEMS[i];

            if (isSel) {
                // Drop shadow
                menuFont.setColor(0.02f, 0.04f, 0.08f, 0.9f);
                layout.setText(menuFont, text);
                menuFont.draw(game.batch, text,
                    btnX + (BTN_W - layout.width) / 2f + 2f,
                    btnY + (BTN_H + layout.height) / 2f - 2f
                );

                if (i == 3) {
                    menuFont.setColor(1f, 0.60f, 0.55f, 1f);
                } else {
                    menuFont.setColor(1f, 0.96f, 0.78f, 1f);
                }
                menuFont.draw(game.batch, text,
                    btnX + (BTN_W - layout.width) / 2f,
                    btnY + (BTN_H + layout.height) / 2f
                );
            } else {
                if (i == 3) {
                    menuFont.setColor(0.90f, 0.65f, 0.65f, 0.88f);
                } else {
                    menuFont.setColor(0.70f, 0.82f, 0.92f, 0.88f);
                }
                layout.setText(menuFont, text);
                menuFont.draw(game.batch, text,
                    btnX + (BTN_W - layout.width) / 2f,
                    btnY + (BTN_H + layout.height) / 2f
                );
            }
        }
    }

    private void drawStatusMessageText() {
        if (statusMessage == null || statusTimer <= 0f) return;
        float alpha = Math.min(1f, statusTimer / 0.5f);
        float bannerY = 338f;
        float bannerH = 40f;
        layout.setText(subFont, statusMessage);
        float bannerX = (W - layout.width) / 2f;
        float textY = bannerY + (bannerH + layout.height) / 2f;

        subFont.setColor(0.01f, 0.02f, 0.04f, 0.8f * alpha);
        subFont.draw(game.batch, statusMessage, bannerX + 1.2f, textY - 1.2f);

        if (statusIsSuccess) {
            subFont.setColor(0.55f, 1f, 0.75f, alpha);
        } else {
            subFont.setColor(1f, 0.75f, 0.75f, alpha);
        }
        subFont.draw(game.batch, statusMessage, bannerX, textY);
    }

    private void drawFooterInfo() {
        // Controls guide on bottom left
        subFont.setColor(0.55f, 0.68f, 0.82f, 0.75f);
        subFont.getData().setScale(0.78f);
        subFont.draw(game.batch, "Điều khiển: [↑/↓/W/S] Chọn mục  •  [Enter/Space] Bắt đầu", 25f, 26f);

        // Version tag on bottom right
        String ver = "v0.4.15 • Fishing RPG";
        layout.setText(subFont, ver);
        subFont.draw(game.batch, ver, W - layout.width - 25f, 26f);
        subFont.getData().setScale(1f);
    }

    // =========================================================================
    // RESET CONFIRMATION MODAL RENDERING
    // =========================================================================
    private void drawResetModalShapes() {
        // 1. Fullscreen dark backdrop overlay
        sr.setColor(0.02f, 0.03f, 0.07f, 0.88f);
        sr.rect(0, 0, W, H);

        float modalW = 620f;
        float modalH = 350f;
        float modalX = (W - modalW) / 2f;
        float modalY = (H - modalH) / 2f;

        // 2. Soft outer glow around modal
        sr.setColor(0.6f, 0.1f, 0.1f, 0.18f);
        drawRoundedRect(sr, modalX - 6f, modalY - 6f, modalW + 12f, modalH + 12f, 18);

        // 3. Dark obsidian card body
        sr.setColor(0.06f, 0.08f, 0.13f, 0.98f);
        drawRoundedRect(sr, modalX, modalY, modalW, modalH, 14);

        // 4. Header plate (Crimson accent)
        sr.setColor(0.25f, 0.06f, 0.08f, 0.95f);
        drawRoundedRect(sr, modalX + 4f, modalY + modalH - 54f, modalW - 8f, 50f, 10);

        // 5. Warning content box
        sr.setColor(0.10f, 0.05f, 0.08f, 0.85f);
        drawRoundedRect(sr, modalX + 24f, modalY + 98f, modalW - 48f, 184f, 8);

        // 6. 10-Second countdown progress bar
        float barX = modalX + 24f;
        float barY = modalY + 84f;
        float barW = modalW - 48f;
        float barH = 6f;

        // Progress bar background track
        sr.setColor(0.14f, 0.16f, 0.22f, 0.9f);
        drawRoundedRect(sr, barX, barY, barW, barH, 3);

        // Progress bar fill
        float progress = MathUtils.clamp((10f - resetCountdown) / 10f, 0f, 1f);
        if (resetCountdown > 0f) {
            sr.setColor(1f, 0.70f, 0.20f, 0.95f);
            drawRoundedRect(sr, barX, barY, Math.max(6f, barW * progress), barH, 3);
        } else {
            sr.setColor(0.25f, 0.85f, 0.45f, 0.95f);
            drawRoundedRect(sr, barX, barY, barW, barH, 3);
        }

        // 7. Buttons: Cancel and Confirm
        float btnModalW = 230f;
        float btnModalH = 46f;
        float btnModalY = modalY + 22f;
        float btnCancelX = modalX + 45f;
        float btnConfirmX = modalX + modalW - 45f - btnModalW;

        // Cancel Button
        if (modalHoveredButton == 0) {
            sr.setColor(0.14f, 0.28f, 0.44f, 0.98f);
            drawRoundedRect(sr, btnCancelX, btnModalY, btnModalW, btnModalH, 10);
            sr.setColor(0.40f, 0.80f, 1f, 1f);
            drawRectOutline(sr, btnCancelX, btnModalY, btnModalW, btnModalH, 2f);
        } else {
            sr.setColor(0.08f, 0.13f, 0.20f, 0.92f);
            drawRoundedRect(sr, btnCancelX, btnModalY, btnModalW, btnModalH, 10);
            sr.setColor(0.22f, 0.36f, 0.52f, 0.8f);
            drawRectOutline(sr, btnCancelX, btnModalY, btnModalW, btnModalH, 1.5f);
        }

        // Confirm Button
        if (resetCountdown > 0f) {
            // Locked appearance
            sr.setColor(0.10f, 0.11f, 0.14f, 0.85f);
            drawRoundedRect(sr, btnConfirmX, btnModalY, btnModalW, btnModalH, 10);
            sr.setColor(0.24f, 0.25f, 0.28f, 0.55f);
            drawRectOutline(sr, btnConfirmX, btnModalY, btnModalW, btnModalH, 1.5f);
        } else {
            // Unlocked active appearance
            if (modalHoveredButton == 1) {
                sr.setColor(0.95f, 0.18f, 0.18f, 1f);
                drawRoundedRect(sr, btnConfirmX - 2f, btnModalY - 2f, btnModalW + 4f, btnModalH + 4f, 11);
                sr.setColor(1f, 0.80f, 0.30f, 1f);
                drawRectOutline(sr, btnConfirmX - 2f, btnModalY - 2f, btnModalW + 4f, btnModalH + 4f, 2.5f);
            } else {
                sr.setColor(0.72f, 0.14f, 0.16f, 0.95f);
                drawRoundedRect(sr, btnConfirmX, btnModalY, btnModalW, btnModalH, 10);
                sr.setColor(0.95f, 0.35f, 0.32f, 0.9f);
                drawRectOutline(sr, btnConfirmX, btnModalY, btnModalW, btnModalH, 2f);
            }
        }
    }

    private void drawResetModalLines() {
        float modalW = 620f;
        float modalH = 350f;
        float modalX = (W - modalW) / 2f;
        float modalY = (H - modalH) / 2f;

        // Outer pulsing red/amber border
        float pulse = 0.5f + 0.5f * MathUtils.sin(timer * 3f);
        sr.setColor(0.85f, 0.25f, 0.25f, 0.70f + 0.30f * pulse);
        drawRectOutline(sr, modalX, modalY, modalW, modalH, 2f);

        // Content box subtle inner outline
        sr.setColor(0.42f, 0.16f, 0.18f, 0.5f);
        drawRectOutline(sr, modalX + 24f, modalY + 98f, modalW - 48f, 184f, 1.2f);
    }

    private void drawResetModalTexts() {
        float modalW = 620f;
        float modalH = 350f;
        float modalX = (W - modalW) / 2f;
        float modalY = (H - modalH) / 2f;

        // 1. Header Title
        String title = "✦  CẢNH BÁO ĐẶT LẠI DỮ LIỆU  ✦";
        layout.setText(menuFont, title);
        menuFont.setColor(0.01f, 0.02f, 0.04f, 0.9f);
        menuFont.draw(game.batch, title, modalX + (modalW - layout.width) / 2f + 1.5f, modalY + modalH - 18.5f);
        menuFont.setColor(1f, 0.42f, 0.38f, 1f);
        menuFont.draw(game.batch, title, modalX + (modalW - layout.width) / 2f, modalY + modalH - 17f);

        // 2. Warning Description Lines
        float textX = modalX + 44f;
        float startY = modalY + modalH - 76f;
        float lineSpacing = 26f;

        subFont.setColor(1f, 0.90f, 0.88f, 1f);
        subFont.draw(game.batch, "Hành động này sẽ XÓA TOÀN BỘ dữ liệu tiến trình hiện tại:", textX, startY);

        subFont.setColor(0.84f, 0.88f, 0.92f, 0.92f);
        subFont.draw(game.batch, "• Toàn bộ Cấp độ, Kinh nghiệm, Vàng, Kim cương và Điểm sự kiện", textX, startY - lineSpacing);
        subFont.draw(game.batch, "• Toàn bộ Cần câu, Dây câu, Lưỡi câu, Kỹ năng & Bộ sưu tập cá", textX, startY - lineSpacing * 2f);
        subFont.draw(game.batch, "• Bạn sẽ bắt đầu lại từ Cấp 1 như một người chơi mới hoàn toàn.", textX, startY - lineSpacing * 3f);

        // 3. Countdown / Status prompt
        if (resetCountdown > 0f) {
            int sec = (int) Math.ceil(resetCountdown);
            String note = "► Vui lòng chờ " + sec + " giây để có thể nhấn nút Đồng ý...";
            subFont.setColor(1f, 0.82f, 0.28f, 1f);
            subFont.draw(game.batch, note, textX, startY - lineSpacing * 4.2f);
        } else {
            String note = "► Nút Đồng ý đã mở khóa! Hãy cân nhắc kỹ trước khi bấm.";
            subFont.setColor(0.35f, 1f, 0.60f, 1f);
            subFont.draw(game.batch, note, textX, startY - lineSpacing * 4.2f);
        }

        // 4. Buttons text
        float btnModalW = 230f;
        float btnModalH = 46f;
        float btnModalY = modalY + 22f;
        float btnCancelX = modalX + 45f;
        float btnConfirmX = modalX + modalW - 45f - btnModalW;

        // Cancel Text
        String cancelTxt = "✕  HỦY BỎ";
        layout.setText(subFont, cancelTxt);
        subFont.setColor(0.88f, 0.95f, 1f, 1f);
        subFont.draw(game.batch, cancelTxt,
            btnCancelX + (btnModalW - layout.width) / 2f,
            btnModalY + (btnModalH + layout.height) / 2f
        );

        // Confirm Text
        String confirmTxt;
        if (resetCountdown > 0f) {
            int sec = (int) Math.ceil(resetCountdown);
            confirmTxt = "ĐỒNG Ý (" + sec + "s)";
            subFont.setColor(0.55f, 0.58f, 0.62f, 0.8f);
        } else {
            confirmTxt = "✓  ĐỒNG Ý XÓA";
            subFont.setColor(1f, 1f, 1f, 1f);
        }
        layout.setText(subFont, confirmTxt);
        subFont.draw(game.batch, confirmTxt,
            btnConfirmX + (btnModalW - layout.width) / 2f,
            btnModalY + (btnModalH + layout.height) / 2f
        );
    }

    // =========================================================================
    // UTILITY SHAPE HELPERS
    // =========================================================================
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
    }

    @Override
    public void resize(int w, int h) {
        if (w <= 0 || h <= 0) return;
        viewport.update(w, h, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        sr.dispose();
    }
}
