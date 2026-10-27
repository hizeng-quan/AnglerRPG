package com.fishingrpg.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fishingrpg.game.FishingRPG;
import com.fishingrpg.game.data.SaveManager;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.systems.AudioManager;
import com.fishingrpg.game.systems.KeybindManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingsDialog {

    public interface SettingsListener {
        void onDataReloaded();
    }
    private SettingsListener listener;
    public void setListener(SettingsListener listener) { this.listener = listener; }

    private final FishingRPG game;
    private boolean visible = false;
    private int currentTab = 0; // 0: Audio, 1: Display, 2: Controls, 3: Data

    private static final float W = 880f;
    private static final float H = 540f;
    private final float dlgX = (1280f - W) / 2f; // 200f
    private final float dlgY = (720f - H) / 2f;  // 90f

    private final GlyphLayout layout = new GlyphLayout();

    // Hover states for tabs
    private boolean hoverTab0 = false;
    private boolean hoverTab1 = false;
    private boolean hoverTab2 = false;
    private boolean hoverTab3 = false;
    private boolean hoverClose = false;

    // Data Management Status Message
    private String statusMessage = null;
    private float statusTimer = 0f;
    private boolean statusIsSuccess = true;

    // Reset Confirmation Modal State
    private boolean showResetModal = false;
    private float resetCountdown = 10f;
    private int modalHoveredButton = -1; // 0: Cancel, 1: Confirm

    // Audio Buttons Hover & Drag States
    private boolean hoverBgmMute = false;
    private boolean hoverBgmDown = false;
    private boolean hoverBgmUp = false;
    private boolean hoverSfxMute = false;
    private boolean hoverSfxDown = false;
    private boolean hoverSfxUp = false;
    private boolean isDraggingBgm = false;
    private boolean isDraggingSfx = false;

    // Display Buttons Hover
    private boolean hoverFullscreen = false;
    private boolean hoverWindowed = false;

    // Controls Rebind State (Single Column Scrollable List)
    private int waitingKeyAction = -1; // -1: none, 0..13: action index being rebound
    private InputProcessor previousInputProcessor = null;
    private int hoverKeyAction = -1;
    private boolean hoverResetKeys = false;
    private String keybindMessage = null;
    private float keybindMsgTimer = 0f;
    private boolean keybindMsgSuccess = true;

    // Scrollbar in Controls Tab
    private float scrollY = 0f;
    private float maxScrollY = 445f;
    private boolean isDraggingScrollbar = false;
    private final Rectangle clipBounds = new Rectangle();
    private final Rectangle scissors = new Rectangle();

    // Data Buttons Hover
    private boolean hoverExport = false;
    private boolean hoverImport = false;
    private boolean hoverReset = false;

    public SettingsDialog(FishingRPG game) {
        this.game = game;
    }

    public void open() {
        this.visible = true;
        this.statusMessage = null;
        this.keybindMessage = null;
        this.showResetModal = false;
        this.isDraggingBgm = false;
        this.isDraggingSfx = false;
        this.isDraggingScrollbar = false;
        this.scrollY = 0f;
        cancelKeyBinding();
        AudioManager.getInstance().playSFX("sfx_button");
    }

    public void close() {
        cancelKeyBinding();
        this.visible = false;
        this.showResetModal = false;
        this.isDraggingBgm = false;
        this.isDraggingSfx = false;
        this.isDraggingScrollbar = false;
        AudioManager.getInstance().playSFX("sfx_button");
    }

    public boolean isOpen() {
        return visible;
    }

    public void handleScroll(float amountY) {
        if (!visible) return;
        if (currentTab == 2) {
            scrollY = MathUtils.clamp(scrollY + amountY * 42f, 0f, maxScrollY);
        }
    }

    private void cancelKeyBinding() {
        if (waitingKeyAction != -1) {
            waitingKeyAction = -1;
            if (previousInputProcessor != null) {
                Gdx.input.setInputProcessor(previousInputProcessor);
                previousInputProcessor = null;
            }
        }
    }

    private void applyKeyBinding(int targetAction, int keycode) {
        if (targetAction < 0 || keycode <= 0 || keycode == Input.Keys.ESCAPE) return;
        KeybindManager km = KeybindManager.getInstance();
        int oldKey = km.getKeyCode(targetAction);
        int conflict = km.setKeyCodeWithSwap(targetAction, keycode);
        if (conflict != -1) {
            keybindMessage = "Đã gán phím [" + KeybindManager.formatKeyName(keycode) + "] cho " + KeybindManager.ACTION_NAMES[targetAction]
                + " (hoán đổi [" + KeybindManager.formatKeyName(oldKey) + "] sang " + KeybindManager.ACTION_NAMES[conflict] + ")!";
        } else {
            keybindMessage = "Đã gán phím [" + KeybindManager.formatKeyName(keycode) + "] cho " + KeybindManager.ACTION_NAMES[targetAction] + "!";
        }
        keybindMsgTimer = 3.5f;
        keybindMsgSuccess = true;
        AudioManager.getInstance().playSFX("sfx_button");
        cancelKeyBinding();
    }

    private void startKeyBinding(int action) {
        this.waitingKeyAction = action;
        this.previousInputProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (waitingKeyAction == -1) return false;
                if (keycode == Input.Keys.ESCAPE) {
                    cancelKeyBinding();
                    AudioManager.getInstance().playSFX("sfx_button");
                    return true;
                }
                if (keycode > 0) {
                    applyKeyBinding(waitingKeyAction, keycode);
                    return true;
                }
                return false;
            }
        });
    }

    public void update(float delta) {
        if (!visible) return;
        if (statusTimer > 0f) {
            statusTimer -= delta;
            if (statusTimer <= 0f) statusMessage = null;
        }
        if (keybindMsgTimer > 0f) {
            keybindMsgTimer -= delta;
            if (keybindMsgTimer <= 0f) keybindMessage = null;
        }
        if (showResetModal && resetCountdown > 0f) {
            resetCountdown = Math.max(0f, resetCountdown - delta);
        }
        if (waitingKeyAction != -1) {
            // Backup direct hardware key polling in case InputProcessor is bypassed by IME or Unikey
            for (int k = 1; k < 256; k++) {
                if (k == Input.Keys.ESCAPE) continue;
                if (Gdx.input.isKeyJustPressed(k)) {
                    applyKeyBinding(waitingKeyAction, k);
                    break;
                }
            }
        }
    }

    public boolean handleInput(Vector3 mouse) {
        if (!visible) return false;

        // Reset confirmation modal takes top priority
        if (showResetModal) {
            float modalW = 600f;
            float modalH = 340f;
            float modalX = (1280f - modalW) / 2f;
            float modalY = (720f - modalH) / 2f;

            float btnModalW = 220f;
            float btnModalH = 46f;
            float btnModalY = modalY + 24f;
            float btnCancelX = modalX + 45f;
            float btnConfirmX = modalX + modalW - 45f - btnModalW;

            boolean hoverCancel = mouse.x >= btnCancelX && mouse.x <= btnCancelX + btnModalW &&
                                  mouse.y >= btnModalY && mouse.y <= btnModalY + btnModalH;
            boolean hoverConfirm = mouse.x >= btnConfirmX && mouse.x <= btnConfirmX + btnModalW &&
                                   mouse.y >= btnModalY && mouse.y <= btnModalY + btnModalH;

            modalHoveredButton = hoverCancel ? 0 : (hoverConfirm && resetCountdown <= 0f ? 1 : -1);

            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                if (hoverCancel) {
                    AudioManager.getInstance().playSFX("sfx_button");
                    showResetModal = false;
                    return true;
                }
                if (hoverConfirm) {
                    if (resetCountdown <= 0f) {
                        performReset();
                        return true;
                    } else {
                        AudioManager.getInstance().playSFX("sfx_fail");
                    }
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                AudioManager.getInstance().playSFX("sfx_button");
                showResetModal = false;
                return true;
            }
            return true;
        }

        // Key rebind waiting takes next priority
        if (waitingKeyAction != -1) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                cancelKeyBinding();
                AudioManager.getInstance().playSFX("sfx_button");
                return true;
            }
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                cancelKeyBinding();
                AudioManager.getInstance().playSFX("sfx_button");
                return true;
            }
            return true;
        }

        // Close button
        float closeBtnW = 180f;
        float closeBtnH = 42f;
        float closeBtnX = dlgX + (W - closeBtnW) / 2f;
        float closeBtnY = dlgY + 20f;
        hoverClose = (mouse.x >= closeBtnX && mouse.x <= closeBtnX + closeBtnW &&
                      mouse.y >= closeBtnY && mouse.y <= closeBtnY + closeBtnH);

        // Tabs
        float tabW = (W - 50f) / 4f;
        float tabH = 38f;
        float tabY = dlgY + H - 105f;
        float t0X = dlgX + 25f;
        float t1X = t0X + tabW;
        float t2X = t1X + tabW;
        float t3X = t2X + tabW;

        hoverTab0 = (mouse.x >= t0X && mouse.x <= t0X + tabW && mouse.y >= tabY && mouse.y <= tabY + tabH);
        hoverTab1 = (mouse.x >= t1X && mouse.x <= t1X + tabW && mouse.y >= tabY && mouse.y <= tabY + tabH);
        hoverTab2 = (mouse.x >= t2X && mouse.x <= t2X + tabW && mouse.y >= tabY && mouse.y <= tabY + tabH);
        hoverTab3 = (mouse.x >= t3X && mouse.x <= t3X + tabW && mouse.y >= tabY && mouse.y <= tabY + tabH);

        // Content Area Coordinates
        float contX = dlgX + 25f;
        float contY = dlgY + 75f;
        float contW = W - 50f;
        float contH = H - 190f;

        if (currentTab == 0) {
            // Audio Tab Interactions
            AudioManager am = AudioManager.getInstance();
            float card1Y = contY + contH - 120f;
            float card2Y = contY + contH - 230f;

            // BGM buttons
            hoverBgmMute = (mouse.x >= contX + 360f && mouse.x <= contX + 450f && mouse.y >= card1Y + 22f && mouse.y <= card1Y + 58f);
            hoverBgmDown = (mouse.x >= contX + 465f && mouse.x <= contX + 505f && mouse.y >= card1Y + 22f && mouse.y <= card1Y + 58f);
            hoverBgmUp   = (mouse.x >= contX + 685f && mouse.x <= contX + 725f && mouse.y >= card1Y + 22f && mouse.y <= card1Y + 58f);

            // SFX buttons
            hoverSfxMute = (mouse.x >= contX + 360f && mouse.x <= contX + 450f && mouse.y >= card2Y + 22f && mouse.y <= card2Y + 58f);
            hoverSfxDown = (mouse.x >= contX + 465f && mouse.x <= contX + 505f && mouse.y >= card2Y + 22f && mouse.y <= card2Y + 58f);
            hoverSfxUp   = (mouse.x >= contX + 685f && mouse.x <= contX + 725f && mouse.y >= card2Y + 22f && mouse.y <= card2Y + 58f);

            // Volume bar drag handling
            float barX = contX + 515f;
            float barW = 160f;
            float barH = 18f;
            float bgmBarY = card1Y + 31f;
            float sfxBarY = card2Y + 31f;

            boolean inBgmBar = (mouse.x >= barX - 10f && mouse.x <= barX + barW + 10f && mouse.y >= bgmBarY - 8f && mouse.y <= bgmBarY + barH + 8f);
            boolean inSfxBar = (mouse.x >= barX - 10f && mouse.x <= barX + barW + 10f && mouse.y >= sfxBarY - 8f && mouse.y <= sfxBarY + barH + 8f);

            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                if (!isDraggingSfx && (inBgmBar || isDraggingBgm)) {
                    isDraggingBgm = true;
                    float pct = MathUtils.clamp((mouse.x - barX) / barW, 0f, 1f);
                    am.setBgmVolume(pct);
                    if (pct > 0f && am.isBgmMuted()) {
                        am.setBgmMuted(false);
                    }
                } else if (!isDraggingBgm && (inSfxBar || isDraggingSfx)) {
                    isDraggingSfx = true;
                    float pct = MathUtils.clamp((mouse.x - barX) / barW, 0f, 1f);
                    am.setSfxVolume(pct);
                    if (pct > 0f && am.isSfxMuted()) {
                        am.setSfxMuted(false);
                    }
                }
            } else {
                isDraggingBgm = false;
                isDraggingSfx = false;
            }
        } else {
            isDraggingBgm = false;
            isDraggingSfx = false;
        }

        if (currentTab == 1) {
            // Display Tab Interactions
            float cardW = 360f;
            float cardH = 150f;
            float cardY = contY + contH - 220f;
            float btnFullX = contX + 35f;
            float btnWinX = contX + 435f;

            hoverFullscreen = (mouse.x >= btnFullX && mouse.x <= btnFullX + cardW && mouse.y >= cardY && mouse.y <= cardY + cardH);
            hoverWindowed   = (mouse.x >= btnWinX && mouse.x <= btnWinX + cardW && mouse.y >= cardY && mouse.y <= cardY + cardH);
        } else if (currentTab == 2) {
            // Controls Tab Interactions (Single Column Scrollable List)
            hoverKeyAction = -1;

            float listX = contX + 20f;
            float listY = contY + 48f;
            float listW = contW - 40f;
            float listH = contH - 66f;

            // Scrollbar track interactions (Click & Drag thumb or track)
            float trackX = listX + listW - 10f;
            float trackY = listY + 4f;
            float trackH = listH - 8f;
            boolean inTrack = (mouse.x >= trackX - 12f && mouse.x <= trackX + 18f && mouse.y >= trackY && mouse.y <= trackY + trackH);

            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                if (inTrack || isDraggingScrollbar) {
                    isDraggingScrollbar = true;
                    if (maxScrollY > 0) {
                        float pct = MathUtils.clamp(1f - (mouse.y - trackY) / trackH, 0f, 1f);
                        scrollY = pct * maxScrollY;
                    }
                }
            } else {
                isDraggingScrollbar = false;
            }

            // Scroll keys UP/DOWN
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                scrollY = Math.max(0f, scrollY - 44f);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                scrollY = Math.min(maxScrollY, scrollY + 44f);
            }

            // Check hover inside scrollable viewport
            boolean inListView = (mouse.x >= listX && mouse.x <= listX + listW && mouse.y >= listY && mouse.y <= listY + listH);

            if (inListView && !isDraggingScrollbar) {
                float rowW = listW - 30f;
                float rowH = 42f;
                float rowX = listX + 8f;
                float btnKeyW = 145f;
                float btnKeyH = 32f;
                float btnKeyX = rowX + rowW - btnKeyW - 12f;

                float curY = listY + listH - 36f + scrollY;

                // 1. Group Header 1: Fishing & Skills (height: 32f)
                curY -= 32f;

                // 5 Action rows (0..4)
                for (int i = 0; i < 5; i++) {
                    float rY = curY;
                    float bY = rY + 5f;
                    if (rY + rowH >= listY && rY <= listY + listH) {
                        if (mouse.x >= btnKeyX && mouse.x <= btnKeyX + btnKeyW && mouse.y >= bY && mouse.y <= bY + btnKeyH) {
                            hoverKeyAction = i;
                        }
                    }
                    curY -= 46f;
                }

                // 2. Group Header 2: Quickslots (height: 38f)
                curY -= 38f;

                // 9 Quickslot rows (5..13)
                for (int s = 0; s < 9; s++) {
                    int actIdx = KeybindManager.ACTION_QUICK_1 + s;
                    float rY = curY;
                    float bY = rY + 5f;
                    if (rY + rowH >= listY && rY <= listY + listH) {
                        if (mouse.x >= btnKeyX && mouse.x <= btnKeyX + btnKeyW && mouse.y >= bY && mouse.y <= bY + btnKeyH) {
                            hoverKeyAction = actIdx;
                        }
                    }
                    curY -= 46f;
                }
            }

            // Reset Keys Button (Bottom Left)
            float btnDefW = 190f;
            float btnDefH = 34f;
            float btnDefX = contX + 25f;
            float btnDefY = contY + 8f;
            hoverResetKeys = (mouse.x >= btnDefX && mouse.x <= btnDefX + btnDefW && mouse.y >= btnDefY && mouse.y <= btnDefY + btnDefH);
        } else if (currentTab == 3) {
            // Data Management Tab Interactions
            float btnDataW = 680f;
            float btnDataH = 64f;
            float btnDataX = contX + (contW - btnDataW) / 2f;
            float bExportY = contY + contH - 95f;
            float bImportY = contY + contH - 175f;
            float bResetY  = contY + contH - 255f;

            hoverExport = (mouse.x >= btnDataX && mouse.x <= btnDataX + btnDataW && mouse.y >= bExportY && mouse.y <= bExportY + btnDataH);
            hoverImport = (mouse.x >= btnDataX && mouse.x <= btnDataX + btnDataW && mouse.y >= bImportY && mouse.y <= bImportY + btnDataH);
            hoverReset  = (mouse.x >= btnDataX && mouse.x <= btnDataX + btnDataW && mouse.y >= bResetY && mouse.y <= bResetY + btnDataH);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            close();
            return true;
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (hoverClose) {
                close();
                return true;
            }
            if (hoverTab0 && currentTab != 0) { currentTab = 0; scrollY = 0f; AudioManager.getInstance().playSFX("sfx_button"); return true; }
            if (hoverTab1 && currentTab != 1) { currentTab = 1; scrollY = 0f; AudioManager.getInstance().playSFX("sfx_button"); return true; }
            if (hoverTab2 && currentTab != 2) { currentTab = 2; scrollY = 0f; AudioManager.getInstance().playSFX("sfx_button"); return true; }
            if (hoverTab3 && currentTab != 3) { currentTab = 3; scrollY = 0f; AudioManager.getInstance().playSFX("sfx_button"); return true; }

            if (currentTab == 0) {
                AudioManager am = AudioManager.getInstance();
                if (hoverBgmMute) {
                    am.setBgmMuted(!am.isBgmMuted());
                    am.playSFX("sfx_button");
                    return true;
                }
                if (hoverBgmDown) {
                    am.setBgmVolume(Math.max(0f, am.getBgmVolume() - 0.1f));
                    if (am.isBgmMuted()) am.setBgmMuted(false);
                    am.playSFX("sfx_button");
                    return true;
                }
                if (hoverBgmUp) {
                    am.setBgmVolume(Math.min(1f, am.getBgmVolume() + 0.1f));
                    if (am.isBgmMuted()) am.setBgmMuted(false);
                    am.playSFX("sfx_button");
                    return true;
                }
                if (hoverSfxMute) {
                    am.setSfxMuted(!am.isSfxMuted());
                    am.playSFX("sfx_button");
                    return true;
                }
                if (hoverSfxDown) {
                    am.setSfxVolume(Math.max(0f, am.getSfxVolume() - 0.1f));
                    if (am.isSfxMuted()) am.setSfxMuted(false);
                    am.playSFX("sfx_button");
                    return true;
                }
                if (hoverSfxUp) {
                    am.setSfxVolume(Math.min(1f, am.getSfxVolume() + 0.1f));
                    if (am.isSfxMuted()) am.setSfxMuted(false);
                    am.playSFX("sfx_button");
                    return true;
                }
            } else if (currentTab == 1) {
                if (hoverFullscreen && !Gdx.graphics.isFullscreen()) {
                    Graphics.DisplayMode mode = Gdx.graphics.getDisplayMode();
                    Gdx.graphics.setFullscreenMode(mode);
                    AudioManager.getInstance().playSFX("sfx_button");
                    return true;
                }
                if (hoverWindowed && Gdx.graphics.isFullscreen()) {
                    Gdx.graphics.setWindowedMode(1280, 720);
                    AudioManager.getInstance().playSFX("sfx_button");
                    return true;
                }
            } else if (currentTab == 2) {
                if (hoverKeyAction != -1) {
                    startKeyBinding(hoverKeyAction);
                    AudioManager.getInstance().playSFX("sfx_button");
                    return true;
                }
                if (hoverResetKeys) {
                    KeybindManager.getInstance().resetDefaults();
                    keybindMessage = "Đã khôi phục tất cả phím bấm về mặc định!";
                    keybindMsgTimer = 3.0f;
                    keybindMsgSuccess = true;
                    AudioManager.getInstance().playSFX("sfx_button");
                    return true;
                }
            } else if (currentTab == 3) {
                if (hoverExport) {
                    handleExport();
                    return true;
                }
                if (hoverImport) {
                    handleImport();
                    return true;
                }
                if (hoverReset) {
                    openResetModal();
                    return true;
                }
            }

            // Click outside dialog closes it
            if (mouse.x < dlgX || mouse.x > dlgX + W || mouse.y < dlgY || mouse.y > dlgY + H) {
                close();
                return true;
            }
            return true;
        }

        return true;
    }

    private void handleExport() {
        try {
            SaveManager.savePlayer(game.player);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String ts = sdf.format(new Date());
            String filename = "angler_save_" + ts + ".sav";

            File fileTs = new File(filename);
            SaveManager.exportToFile(fileTs);

            // Also keep standard fallback file
            File fileStd = new File("angler_save.sav");
            SaveManager.exportToFile(fileStd);

            // Also maintain angler_save.json for backward compatibility
            File fileLegacy = new File("angler_save.json");
            SaveManager.exportToFile(fileLegacy);

            try {
                String encryptedPayload = SaveManager.exportDataToString();
                if (encryptedPayload != null && encryptedPayload.length() < 16384 && Gdx.app.getClipboard() != null) {
                    Gdx.app.getClipboard().setContents(encryptedPayload);
                }
            } catch (Throwable ignored) {
                // Large save payloads cannot be placed into LWJGL MemoryStack clipboard (causes OutOfMemoryError)
            }

            statusMessage = "Thành công: Đã xuất tệp '" + filename + "' an toàn!";
            statusTimer = 6.0f;
            statusIsSuccess = true;
            AudioManager.getInstance().playSFX("sfx_success");
        } catch (Throwable e) {
            Gdx.app.error("Settings", "Export failed", e);
            statusMessage = "Lỗi xuất dữ liệu: " + e.getMessage();
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
                fd = new java.awt.FileDialog((java.awt.Frame) null, "Chọn file lưu trữ AnglerRPG (*.sav, *.json, *.bak)", java.awt.FileDialog.LOAD);
                fd.setFile("*.sav;*.json;*.bak");
                fd.setAlwaysOnTop(true);
                fd.setVisible(true);
                String f = fd.getFile();
                String d = fd.getDirectory();
                if (f != null && d != null) {
                    File selected = new File(d, f);
                    if (selected.exists() && SaveManager.importFromFile(selected, game.player)) {
                        sourceName = selected.getName();
                        jsonContent = "OK";
                    }
                }
            } catch (Throwable t) {
                Gdx.app.log("Settings", "Native FileDialog fallback: " + t.getMessage());
            } finally {
                if (fd != null) {
                    try { fd.dispose(); } catch (Throwable ignored) {}
                }
            }

            // Fallbacks
            if (jsonContent == null) {
                File stdSav = new File("angler_save.sav");
                if (stdSav.exists() && SaveManager.importFromFile(stdSav, game.player)) {
                    sourceName = "angler_save.sav";
                    jsonContent = "OK";
                }
            }
            if (jsonContent == null) {
                File std = new File("angler_save.json");
                if (std.exists() && SaveManager.importFromFile(std, game.player)) {
                    sourceName = "angler_save.json";
                    jsonContent = "OK";
                }
            }
            if (jsonContent == null) {
                try {
                    String clip = Gdx.app.getClipboard().getContents();
                    if (clip != null && (clip.contains(SaveManager.ENCRYPTION_HEADER) || clip.contains("playerData")) && SaveManager.importDataFromString(clip, game.player)) {
                        sourceName = "Clipboard";
                        jsonContent = "OK";
                    }
                } catch (Exception ignored) {}
            }

            final String fJson = jsonContent;
            final String fSource = sourceName;
            Gdx.app.postRunnable(() -> {
                if (fJson != null) {
                    if (listener != null) listener.onDataReloaded();
                    statusMessage = "Thành công: Nạp từ " + fSource + " (Cấp " + game.player.getLevel() + " - " + game.player.getGold() + " Vàng)!";
                    statusTimer = 6.0f;
                    statusIsSuccess = true;
                    AudioManager.getInstance().playSFX("sfx_success");
                } else {
                    statusMessage = "Không tìm thấy dữ liệu hợp lệ (Dữ liệu bị lỗi hoặc đã qua chỉnh sửa)!";
                    statusTimer = 5.0f;
                    statusIsSuccess = false;
                    AudioManager.getInstance().playSFX("sfx_fail");
                }
            });
        });
        importThread.setDaemon(true);
        importThread.start();
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
            try {
                SaveManager.savePlayer(game.player);
                File backup = new File("angler_save_before_reset.json");
                SaveManager.exportToFile(backup);
            } catch (Throwable ignored) {}

            SaveManager.clearSave();
            game.player = new Player();
            SaveManager.savePlayer(game.player);
            if (listener != null) listener.onDataReloaded();

            statusMessage = "ĐÃ ĐẶT LẠI DỮ LIỆU! Người chơi trở về Cấp 1 (Đã tự động sao lưu).";
            statusTimer = 6.0f;
            statusIsSuccess = true;
            AudioManager.getInstance().playSFX("sfx_success");
        } catch (Exception e) {
            Gdx.app.error("Settings", "Reset failed", e);
            statusMessage = "Lỗi khi đặt lại dữ liệu: " + e.getMessage();
            statusTimer = 5.0f;
            statusIsSuccess = false;
            AudioManager.getInstance().playSFX("sfx_fail");
        }
    }

    public void render(ShapeRenderer sr, SpriteBatch batch, BitmapFont font, BitmapFont titleFont, BitmapFont smallFont, float animTimer) {
        render(sr, batch, font, titleFont, smallFont, animTimer, null);
    }

    public void render(ShapeRenderer sr, SpriteBatch batch, BitmapFont font, BitmapFont titleFont, BitmapFont smallFont, float animTimer, Camera camera) {
        if (!visible) return;

        Camera cam = camera;
        if (cam == null) {
            if (game.getScreen() instanceof com.fishingrpg.game.PlayScreen) {
                cam = ((com.fishingrpg.game.PlayScreen) game.getScreen()).getCamera();
            } else if (game.getScreen() instanceof com.fishingrpg.game.screens.MainMenuScreen) {
                cam = ((com.fishingrpg.game.screens.MainMenuScreen) game.getScreen()).getCamera();
            }
        }

        // 1. DIM BACKDROP
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(0.01f, 0.02f, 0.05f, 0.88f);
        sr.rect(0, 0, 1280f, 720f);

        // 2. MAIN DIALOG CHASSIS (FANTASY RPG GLASSMORPHISM)
        // Shadow
        sr.setColor(0f, 0f, 0f, 0.65f);
        sr.rect(dlgX + 10f, dlgY - 10f, W, H);

        // Deep Abyssal Chassis
        sr.setColor(0.05f, 0.08f, 0.15f, 0.98f);
        sr.rect(dlgX, dlgY, W, H);

        // Header Background Bar
        sr.setColor(0.09f, 0.14f, 0.24f, 1f);
        sr.rect(dlgX, dlgY + H - 65f, W, 65f);

        // Gold top accent line
        sr.setColor(1f, 0.82f, 0.25f, 0.95f);
        sr.rect(dlgX, dlgY + H - 3f, W, 3f);

        // Neon / Brass outer double border
        sr.setColor(0.25f, 0.65f, 0.95f, 0.85f);
        drawBoxBorder(sr, dlgX, dlgY, W, H, 2f);
        sr.setColor(0.85f, 0.70f, 0.25f, 0.35f);
        drawBoxBorder(sr, dlgX + 3f, dlgY + 3f, W - 6f, H - 6f, 1f);

        // 4 Golden Corner Ornaments (Vibe RPG Fantasy)
        drawCornerOrnaments(sr, dlgX, dlgY, W, H);

        // 3. TABS
        float tabW = (W - 50f) / 4f;
        float tabH = 38f;
        float tabY = dlgY + H - 105f;
        float t0X = dlgX + 25f;

        for (int i = 0; i < 4; i++) {
            float tx = t0X + i * tabW;
            boolean isCur = (currentTab == i);
            boolean isHov = (i == 0 && hoverTab0) || (i == 1 && hoverTab1) || (i == 2 && hoverTab2) || (i == 3 && hoverTab3);

            if (isCur) {
                sr.setColor(0.14f, 0.25f, 0.42f, 1f);
            } else {
                sr.setColor(isHov ? new Color(0.10f, 0.16f, 0.26f, 1f) : new Color(0.07f, 0.10f, 0.17f, 1f));
            }
            sr.rect(tx, tabY, tabW, tabH);

            if (isCur) {
                // Top shine line for active tab
                sr.setColor(1f, 0.88f, 0.35f, 1f);
                sr.rect(tx, tabY + tabH - 2.5f, tabW, 2.5f);
                sr.setColor(0.40f, 0.88f, 1f, 1f);
                drawBoxBorder(sr, tx, tabY, tabW, tabH, 2f);
            } else {
                sr.setColor(0.20f, 0.32f, 0.48f, 0.7f);
                drawBoxBorder(sr, tx, tabY, tabW, tabH, 1f);
            }
        }

        // 4. CONTENT CONTAINER
        float contX = dlgX + 25f;
        float contY = dlgY + 75f;
        float contW = W - 50f;
        float contH = H - 190f;

        sr.setColor(0.03f, 0.05f, 0.10f, 0.95f);
        sr.rect(contX, contY, contW, contH);
        sr.setColor(0.18f, 0.28f, 0.42f, 0.8f);
        drawBoxBorder(sr, contX, contY, contW, contH, 1.4f);

        // 5. CONTENT TAB SHAPES
        if (currentTab == 0) {
            drawAudioTabShapes(sr, contX, contY, contW, contH);
        } else if (currentTab == 1) {
            drawDisplayTabShapes(sr, contX, contY, contW, contH);
        } else if (currentTab == 2) {
            drawControlsTabShapes(sr, contX, contY, contW, contH, cam, batch);
        } else if (currentTab == 3) {
            drawDataTabShapes(sr, contX, contY, contW, contH);
        }

        // 6. CLOSE BUTTON (EMERALD & GOLD RPG BUTTON)
        float closeBtnW = 180f;
        float closeBtnH = 42f;
        float closeBtnX = dlgX + (W - closeBtnW) / 2f;
        float closeBtnY = dlgY + 20f;

        sr.setColor(hoverClose ? new Color(0.18f, 0.58f, 0.35f, 1f) : new Color(0.10f, 0.38f, 0.22f, 0.95f));
        sr.rect(closeBtnX, closeBtnY, closeBtnW, closeBtnH);
        // Top highlight
        sr.setColor(0.45f, 0.95f, 0.65f, hoverClose ? 0.9f : 0.5f);
        sr.rect(closeBtnX + 2f, closeBtnY + closeBtnH - 2f, closeBtnW - 4f, 2f);
        // Gold / emerald border
        sr.setColor(hoverClose ? new Color(1f, 0.90f, 0.45f, 1f) : new Color(0.35f, 0.85f, 0.55f, 0.85f));
        drawBoxBorder(sr, closeBtnX, closeBtnY, closeBtnW, closeBtnH, hoverClose ? 2.2f : 1.6f);

        // 7. RESET MODAL SHAPES IF ACTIVE
        if (showResetModal) {
            drawResetModalShapes(sr);
        }

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // 8. TEXT RENDERING
        batch.begin();

        if (showResetModal) {
            drawResetModalTexts(batch, font, titleFont);
        } else {
            // Header Title (Winged flourishes)
            titleFont.getData().setScale(0.72f);
            titleFont.setColor(1f, 0.88f, 0.35f, 1f);
            layout.setText(titleFont, "✦ CÀI ĐẶT TRÒ CHƠI ✦");
            titleFont.draw(batch, layout, dlgX + (W - layout.width) / 2f, dlgY + H - 18f);

            // Subtitle
            font.getData().setScale(0.75f);
            font.setColor(0.65f, 0.85f, 0.98f, 0.9f);
            layout.setText(font, "Tùy chỉnh Âm thanh, Hiển thị, Phím bấm & Quản lý Dữ liệu trò chơi");
            font.draw(batch, layout, dlgX + (W - layout.width) / 2f, dlgY + H - 42f);

            // Tab Labels
            String[] tabNames = { "ÂM THANH", "HIỂN THỊ", "PHÍM BẤM", "DỮ LIỆU" };
            font.getData().setScale(0.85f);
            for (int i = 0; i < 4; i++) {
                float tx = t0X + i * tabW;
                boolean isCur = (currentTab == i);
                font.setColor(isCur ? new Color(1f, 0.95f, 0.45f, 1f) : new Color(0.70f, 0.82f, 0.92f, 0.85f));
                layout.setText(font, tabNames[i]);
                font.draw(batch, layout, tx + (tabW - layout.width) / 2f, tabY + 25f);
            }

            // Close Button Text
            font.getData().setScale(0.88f);
            font.setColor(hoverClose ? Color.WHITE : new Color(0.90f, 0.98f, 0.92f, 1f));
            layout.setText(font, "ĐÓNG");
            font.draw(batch, layout, closeBtnX + (closeBtnW - layout.width) / 2f, closeBtnY + 27f);

            // Tab Content Texts
            if (currentTab == 0) {
                drawAudioTabTexts(batch, font, smallFont, contX, contY, contW, contH);
            } else if (currentTab == 1) {
                drawDisplayTabTexts(batch, font, smallFont, contX, contY, contW, contH);
            } else if (currentTab == 2) {
                drawControlsTabTexts(batch, font, smallFont, contX, contY, contW, contH, cam);
            } else if (currentTab == 3) {
                drawDataTabTexts(batch, font, smallFont, contX, contY, contW, contH);
            }
        }

        font.getData().setScale(1f);
        if (smallFont != null) smallFont.getData().setScale(1f);
        batch.end();
    }

    private void drawCornerOrnaments(ShapeRenderer sr, float x, float y, float w, float h) {
        float s = 18f;
        sr.setColor(1f, 0.85f, 0.30f, 0.95f);
        // Top-Left
        sr.rect(x - 2f, y + h - 2f, s, 4f);
        sr.rect(x - 2f, y + h - s, 4f, s);
        sr.rect(x + 5f, y + h - 5f, 3f, 3f);
        // Top-Right
        sr.rect(x + w - s + 2f, y + h - 2f, s, 4f);
        sr.rect(x + w - 2f, y + h - s, 4f, s);
        sr.rect(x + w - 8f, y + h - 5f, 3f, 3f);
        // Bottom-Left
        sr.rect(x - 2f, y - 2f, s, 4f);
        sr.rect(x - 2f, y - 2f, 4f, s);
        sr.rect(x + 5f, y + 2f, 3f, 3f);
        // Bottom-Right
        sr.rect(x + w - s + 2f, y - 2f, s, 4f);
        sr.rect(x + w - 2f, y - 2f, 4f, s);
        sr.rect(x + w - 8f, y + 2f, 3f, 3f);
    }

    private void drawAudioTabShapes(ShapeRenderer sr, float cx, float cy, float cw, float ch) {
        AudioManager am = AudioManager.getInstance();
        float card1Y = cy + ch - 120f;
        float card2Y = cy + ch - 230f;
        float cardW = cw - 60f;
        float cardH = 80f;

        // --- BGM CARD ---
        sr.setColor(0.06f, 0.09f, 0.16f, 0.90f);
        sr.rect(cx + 30f, card1Y, cardW, cardH);
        sr.setColor(0.20f, 0.35f, 0.55f, 0.35f);
        sr.rect(cx + 31f, card1Y + cardH - 2f, cardW - 2f, 2f);
        sr.setColor(0.18f, 0.30f, 0.48f, 0.75f);
        drawBoxBorder(sr, cx + 30f, card1Y, cardW, cardH, 1.2f);

        // BGM Mute Button
        sr.setColor(am.isBgmMuted() ? new Color(0.45f, 0.15f, 0.15f, 0.95f) : (hoverBgmMute ? new Color(0.18f, 0.42f, 0.60f, 1f) : new Color(0.10f, 0.24f, 0.38f, 0.95f)));
        sr.rect(cx + 360f, card1Y + 22f, 90f, 36f);
        sr.setColor(am.isBgmMuted() ? new Color(1f, 0.35f, 0.35f, 0.95f) : new Color(0.35f, 0.90f, 0.65f, 0.95f));
        drawBoxBorder(sr, cx + 360f, card1Y + 22f, 90f, 36f, 1.4f);

        // BGM [-] and [+]
        sr.setColor(hoverBgmDown ? new Color(0.25f, 0.48f, 0.75f, 1f) : new Color(0.12f, 0.22f, 0.38f, 0.95f));
        sr.rect(cx + 465f, card1Y + 22f, 40f, 36f);
        drawBoxBorder(sr, cx + 465f, card1Y + 22f, 40f, 36f, 1.2f);

        sr.setColor(hoverBgmUp ? new Color(0.25f, 0.48f, 0.75f, 1f) : new Color(0.12f, 0.22f, 0.38f, 0.95f));
        sr.rect(cx + 685f, card1Y + 22f, 40f, 36f);
        drawBoxBorder(sr, cx + 685f, card1Y + 22f, 40f, 36f, 1.2f);

        // BGM Volume Bar (Obsidian Groove + Emerald Fill + Drag Knob Thumb)
        float barX = cx + 515f;
        float barW = 160f;
        float barH = 18f;
        float barY = card1Y + 31f;

        sr.setColor(0.04f, 0.07f, 0.12f, 1f);
        sr.rect(barX, barY, barW, barH);
        // Notches at 25%, 50%, 75%
        sr.setColor(0.16f, 0.26f, 0.38f, 0.8f);
        sr.rect(barX + barW * 0.25f, barY, 1f, barH);
        sr.rect(barX + barW * 0.50f, barY, 1f, barH);
        sr.rect(barX + barW * 0.75f, barY, 1f, barH);

        float fillW = barW * (am.isBgmMuted() ? 0f : am.getBgmVolume());
        sr.setColor(0.25f, 0.85f, 0.55f, 1f);
        sr.rect(barX, barY, fillW, barH);
        sr.setColor(isDraggingBgm ? new Color(1f, 0.88f, 0.35f, 1f) : new Color(0.35f, 0.65f, 0.85f, 0.8f));
        drawBoxBorder(sr, barX, barY, barW, barH, isDraggingBgm ? 2f : 1.2f);

        // Draggable Knob / Thumb
        float thumbX = barX + fillW - 6f;
        sr.setColor(1f, 0.88f, 0.35f, 1f);
        sr.rect(thumbX, barY - 4f, 12f, barH + 8f);
        sr.setColor(1f, 1f, 1f, 0.95f);
        drawBoxBorder(sr, thumbX, barY - 4f, 12f, barH + 8f, 1.4f);

        // --- SFX CARD ---
        sr.setColor(0.06f, 0.09f, 0.16f, 0.90f);
        sr.rect(cx + 30f, card2Y, cardW, cardH);
        sr.setColor(0.20f, 0.35f, 0.55f, 0.35f);
        sr.rect(cx + 31f, card2Y + cardH - 2f, cardW - 2f, 2f);
        sr.setColor(0.18f, 0.30f, 0.48f, 0.75f);
        drawBoxBorder(sr, cx + 30f, card2Y, cardW, cardH, 1.2f);

        // SFX Mute Button
        sr.setColor(am.isSfxMuted() ? new Color(0.45f, 0.15f, 0.15f, 0.95f) : (hoverSfxMute ? new Color(0.18f, 0.42f, 0.60f, 1f) : new Color(0.10f, 0.24f, 0.38f, 0.95f)));
        sr.rect(cx + 360f, card2Y + 22f, 90f, 36f);
        sr.setColor(am.isSfxMuted() ? new Color(1f, 0.35f, 0.35f, 0.95f) : new Color(0.35f, 0.90f, 0.65f, 0.95f));
        drawBoxBorder(sr, cx + 360f, card2Y + 22f, 90f, 36f, 1.4f);

        // SFX [-] and [+]
        sr.setColor(hoverSfxDown ? new Color(0.25f, 0.48f, 0.75f, 1f) : new Color(0.12f, 0.22f, 0.38f, 0.95f));
        sr.rect(cx + 465f, card2Y + 22f, 40f, 36f);
        drawBoxBorder(sr, cx + 465f, card2Y + 22f, 40f, 36f, 1.2f);

        sr.setColor(hoverSfxUp ? new Color(0.25f, 0.48f, 0.75f, 1f) : new Color(0.12f, 0.22f, 0.38f, 0.95f));
        sr.rect(cx + 685f, card2Y + 22f, 40f, 36f);
        drawBoxBorder(sr, cx + 685f, card2Y + 22f, 40f, 36f, 1.2f);

        // SFX Volume Bar (Cyan Frost Fill + Drag Knob Thumb)
        float sbarY = card2Y + 31f;
        sr.setColor(0.04f, 0.07f, 0.12f, 1f);
        sr.rect(barX, sbarY, barW, barH);
        sr.setColor(0.16f, 0.26f, 0.38f, 0.8f);
        sr.rect(barX + barW * 0.25f, sbarY, 1f, barH);
        sr.rect(barX + barW * 0.50f, sbarY, 1f, barH);
        sr.rect(barX + barW * 0.75f, sbarY, 1f, barH);

        float sfillW = barW * (am.isSfxMuted() ? 0f : am.getSfxVolume());
        sr.setColor(0.30f, 0.85f, 1f, 1f);
        sr.rect(barX, sbarY, sfillW, barH);
        sr.setColor(isDraggingSfx ? new Color(1f, 0.88f, 0.35f, 1f) : new Color(0.35f, 0.65f, 0.85f, 0.8f));
        drawBoxBorder(sr, barX, sbarY, barW, barH, isDraggingSfx ? 2f : 1.2f);

        // SFX Draggable Knob / Thumb
        float sthumbX = barX + sfillW - 6f;
        sr.setColor(0.35f, 0.95f, 1f, 1f);
        sr.rect(sthumbX, sbarY - 4f, 12f, barH + 8f);
        sr.setColor(1f, 1f, 1f, 0.95f);
        drawBoxBorder(sr, sthumbX, sbarY - 4f, 12f, barH + 8f, 1.4f);
    }

    private void drawAudioTabTexts(SpriteBatch batch, BitmapFont font, BitmapFont smallFont, float cx, float cy, float cw, float ch) {
        AudioManager am = AudioManager.getInstance();
        float card1Y = cy + ch - 120f;
        float card2Y = cy + ch - 230f;

        // BGM Label & Subtitle
        font.getData().setScale(0.92f);
        font.setColor(1f, 0.90f, 0.5f, 1f);
        font.draw(batch, "Nhạc Nền (BGM):", cx + 50f, card1Y + 54f);
        smallFont.getData().setScale(0.74f);
        smallFont.setColor(0.65f, 0.82f, 0.95f, 0.85f);
        smallFont.draw(batch, "Nhạc nền đại dương & nhạc không gian chiến đấu", cx + 50f, card1Y + 30f);

        // BGM Mute text
        font.getData().setScale(0.82f);
        font.setColor(am.isBgmMuted() ? new Color(1f, 0.5f, 0.5f, 1f) : Color.WHITE);
        String bgmMuteTxt = am.isBgmMuted() ? "TẮT" : "BẬT";
        layout.setText(font, bgmMuteTxt);
        font.draw(batch, layout, cx + 360f + (90f - layout.width) / 2f, card1Y + 45f);

        // BGM [-] and [+]
        font.setColor(Color.WHITE);
        layout.setText(font, "-");
        font.draw(batch, layout, cx + 465f + (40f - layout.width) / 2f, card1Y + 46f);
        layout.setText(font, "+");
        font.draw(batch, layout, cx + 685f + (40f - layout.width) / 2f, card1Y + 46f);

        // BGM Percent
        int bgmPct = am.isBgmMuted() ? 0 : Math.round(am.getBgmVolume() * 100f);
        font.getData().setScale(0.82f);
        font.setColor(0.85f, 0.95f, 1f, 1f);
        layout.setText(font, bgmPct + "%");
        font.draw(batch, layout, cx + 735f, card1Y + 44f);

        // SFX Label & Subtitle
        font.getData().setScale(0.92f);
        font.setColor(1f, 0.90f, 0.5f, 1f);
        font.draw(batch, "Hiệu Ứng (SFX):", cx + 50f, card2Y + 54f);
        smallFont.getData().setScale(0.74f);
        smallFont.setColor(0.65f, 0.82f, 0.95f, 0.85f);
        smallFont.draw(batch, "Tiếng quẫy cá, cắn câu, cuộn dây & âm kỹ năng", cx + 50f, card2Y + 30f);

        // SFX Mute text
        font.getData().setScale(0.82f);
        font.setColor(am.isSfxMuted() ? new Color(1f, 0.5f, 0.5f, 1f) : Color.WHITE);
        String sfxMuteTxt = am.isSfxMuted() ? "TẮT" : "BẬT";
        layout.setText(font, sfxMuteTxt);
        font.draw(batch, layout, cx + 360f + (90f - layout.width) / 2f, card2Y + 45f);

        // SFX [-] and [+]
        font.setColor(Color.WHITE);
        layout.setText(font, "-");
        font.draw(batch, layout, cx + 465f + (40f - layout.width) / 2f, card2Y + 46f);
        layout.setText(font, "+");
        font.draw(batch, layout, cx + 685f + (40f - layout.width) / 2f, card2Y + 46f);

        // SFX Percent
        int sfxPct = am.isSfxMuted() ? 0 : Math.round(am.getSfxVolume() * 100f);
        font.getData().setScale(0.82f);
        font.setColor(0.85f, 0.95f, 1f, 1f);
        layout.setText(font, sfxPct + "%");
        font.draw(batch, layout, cx + 735f, card2Y + 44f);
    }

    private void drawDisplayTabShapes(ShapeRenderer sr, float cx, float cy, float cw, float ch) {
        boolean isFull = Gdx.graphics.isFullscreen();
        float cardW = 360f;
        float cardH = 150f;
        float cardY = cy + ch - 220f;
        float btnFullX = cx + 35f;
        float btnWinX = cx + 435f;

        // Fullscreen Card Body
        sr.setColor(isFull ? new Color(0.12f, 0.24f, 0.40f, 0.95f) : (hoverFullscreen ? new Color(0.09f, 0.16f, 0.28f, 0.95f) : new Color(0.06f, 0.10f, 0.18f, 0.90f)));
        sr.rect(btnFullX, cardY, cardW, cardH);
        sr.setColor(isFull ? new Color(1f, 0.85f, 0.35f, 0.9f) : new Color(0.25f, 0.50f, 0.80f, 0.4f));
        sr.rect(btnFullX + 2f, cardY + cardH - 3f, cardW - 4f, 3f);
        sr.setColor(isFull ? new Color(1f, 0.82f, 0.25f, 1f) : (hoverFullscreen ? new Color(0.40f, 0.80f, 1f, 0.9f) : new Color(0.20f, 0.35f, 0.55f, 0.7f)));
        drawBoxBorder(sr, btnFullX, cardY, cardW, cardH, isFull ? 2.5f : 1.2f);

        // Windowed Card Body
        sr.setColor(!isFull ? new Color(0.12f, 0.24f, 0.40f, 0.95f) : (hoverWindowed ? new Color(0.09f, 0.16f, 0.28f, 0.95f) : new Color(0.06f, 0.10f, 0.18f, 0.90f)));
        sr.rect(btnWinX, cardY, cardW, cardH);
        sr.setColor(!isFull ? new Color(1f, 0.85f, 0.35f, 0.9f) : new Color(0.25f, 0.50f, 0.80f, 0.4f));
        sr.rect(btnWinX + 2f, cardY + cardH - 3f, cardW - 4f, 3f);
        sr.setColor(!isFull ? new Color(1f, 0.82f, 0.25f, 1f) : (hoverWindowed ? new Color(0.40f, 0.80f, 1f, 0.9f) : new Color(0.20f, 0.35f, 0.55f, 0.7f)));
        drawBoxBorder(sr, btnWinX, cardY, cardW, cardH, !isFull ? 2.5f : 1.2f);
    }

    private void drawDisplayTabTexts(SpriteBatch batch, BitmapFont font, BitmapFont smallFont, float cx, float cy, float cw, float ch) {
        boolean isFull = Gdx.graphics.isFullscreen();
        float cardW = 360f;
        float cardH = 150f;
        float cardY = cy + ch - 220f;
        float btnFullX = cx + 35f;
        float btnWinX = cx + 435f;

        font.getData().setScale(0.95f);
        font.setColor(1f, 0.90f, 0.45f, 1f);
        layout.setText(font, "✦ CHỌN CHẾ ĐỘ HIỂN THỊ MÀN HÌNH ✦");
        font.draw(batch, layout, cx + (cw - layout.width) / 2f, cy + ch - 35f);

        // Fullscreen Title
        font.getData().setScale(0.90f);
        font.setColor(isFull ? new Color(1f, 0.95f, 0.5f, 1f) : Color.WHITE);
        layout.setText(font, "TOÀN MÀN HÌNH (FULLSCREEN)");
        font.draw(batch, layout, btnFullX + 25f, cardY + cardH - 25f);

        // Fullscreen Description
        smallFont.getData().setScale(0.76f);
        smallFont.setColor(0.70f, 0.85f, 0.95f, 0.9f);
        smallFont.draw(batch, "Độ phân giải hiển thị phủ trọn màn hình.\nTối ưu cho trải nghiệm đắm chìm đại dương.", btnFullX + 25f, cardY + cardH - 58f);

        // Fullscreen Status
        font.getData().setScale(0.82f);
        if (isFull) {
            font.setColor(0.35f, 1f, 0.55f, 1f);
            font.draw(batch, "[✓ ĐANG KÍCH HOẠT]", btnFullX + 25f, cardY + 35f);
        } else {
            font.setColor(hoverFullscreen ? Color.WHITE : new Color(0.40f, 0.80f, 1f, 0.9f));
            font.draw(batch, "[ CHỌN CHẾ ĐỘ NÀY ]", btnFullX + 25f, cardY + 35f);
        }

        // Windowed Title
        font.getData().setScale(0.90f);
        font.setColor(!isFull ? new Color(1f, 0.95f, 0.5f, 1f) : Color.WHITE);
        layout.setText(font, "CỬA SỔ (WINDOWED - 1280x720)");
        font.draw(batch, layout, btnWinX + 25f, cardY + cardH - 25f);

        // Windowed Description
        smallFont.getData().setScale(0.76f);
        smallFont.setColor(0.70f, 0.85f, 0.95f, 0.9f);
        smallFont.draw(batch, "Chế độ cửa sổ tỷ lệ 16:9 chuẩn HD.\nThuận tiện đa nhiệm cùng lúc trên máy tính.", btnWinX + 25f, cardY + cardH - 58f);

        // Windowed Status
        font.getData().setScale(0.82f);
        if (!isFull) {
            font.setColor(0.35f, 1f, 0.55f, 1f);
            font.draw(batch, "[✓ ĐANG KÍCH HOẠT]", btnWinX + 25f, cardY + 35f);
        } else {
            font.setColor(hoverWindowed ? Color.WHITE : new Color(0.40f, 0.80f, 1f, 0.9f));
            font.draw(batch, "[ CHỌN CHẾ ĐỘ NÀY ]", btnWinX + 25f, cardY + 35f);
        }

        // F11 Shortcut tip banner at bottom
        font.getData().setScale(0.80f);
        font.setColor(1f, 0.85f, 0.35f, 0.9f);
        layout.setText(font, "✦ MẸO: Bạn có thể nhấn phím [F11] bất kỳ lúc nào để chuyển nhanh Toàn màn hình & Cửa sổ!");
        font.draw(batch, layout, cx + (cw - layout.width) / 2f, cy + 38f);
    }

    private void drawControlsTabShapes(ShapeRenderer sr, float cx, float cy, float cw, float ch, Camera cam, SpriteBatch batch) {
        float listX = cx + 20f;
        float listY = cy + 48f;
        float listW = cw - 40f;
        float listH = ch - 66f;

        // Reset to Defaults Button (Bottom Left)
        float btnDefW = 190f;
        float btnDefH = 34f;
        float btnDefX = cx + 25f;
        float btnDefY = cy + 8f;

        sr.setColor(hoverResetKeys ? new Color(0.35f, 0.25f, 0.15f, 1f) : new Color(0.18f, 0.14f, 0.08f, 0.95f));
        sr.rect(btnDefX, btnDefY, btnDefW, btnDefH);
        sr.setColor(hoverResetKeys ? new Color(1f, 0.9f, 0.45f, 1f) : new Color(0.75f, 0.65f, 0.30f, 0.8f));
        drawBoxBorder(sr, btnDefX, btnDefY, btnDefW, btnDefH, 1.4f);

        // Recessed List Box Viewport Frame (Hộp hiển thị danh sách cuộn)
        sr.setColor(0.02f, 0.04f, 0.08f, 0.95f);
        sr.rect(listX, listY, listW, listH);
        // Inner depth top shadow
        sr.setColor(0.01f, 0.01f, 0.03f, 0.6f);
        sr.rect(listX, listY + listH - 4f, listW, 4f);
        // Cyber-fantasy frame border
        sr.setColor(0.14f, 0.22f, 0.35f, 0.9f);
        drawBoxBorder(sr, listX, listY, listW, listH, 1.4f);

        // Scissor clipping for the scrollable list
        sr.flush(); // FLUSH all prior shapes (chassis, tabs, container, recessed box) before enabling scissors!

        Viewport vp = null;
        if (game.getScreen() instanceof com.fishingrpg.game.PlayScreen) {
            vp = ((com.fishingrpg.game.PlayScreen) game.getScreen()).getViewport();
        } else if (game.getScreen() instanceof com.fishingrpg.game.screens.MainMenuScreen) {
            vp = ((com.fishingrpg.game.screens.MainMenuScreen) game.getScreen()).getViewport();
        }

        boolean pushed = false;
        if (cam != null) {
            try {
                clipBounds.set(listX + 1.5f, listY + 1.5f, listW - 3f, listH - 3f);
                if (vp != null) {
                    ScissorStack.calculateScissors(cam, vp.getScreenX(), vp.getScreenY(), vp.getScreenWidth(), vp.getScreenHeight(), sr.getTransformMatrix(), clipBounds, scissors);
                } else {
                    ScissorStack.calculateScissors(cam, sr.getTransformMatrix(), clipBounds, scissors);
                }
                pushed = ScissorStack.pushScissors(scissors);
            } catch (Throwable ignored) {
                pushed = false;
            }
        }

        float rowW = listW - 30f;
        float rowH = 42f;
        float rowX = listX + 8f;
        float btnKeyW = 145f;
        float btnKeyH = 32f;
        float btnKeyX = rowX + rowW - btnKeyW - 12f;

        float boxTop = listY + listH;
        float boxBottom = listY;

        float curY = listY + listH - 36f + scrollY;

        // Group 1 Header banner background
        if (curY + 28f >= boxBottom && curY - 2f <= boxTop) {
            sr.setColor(0.08f, 0.14f, 0.24f, 0.75f);
            sr.rect(rowX, curY - 2f, rowW, 28f);
            sr.setColor(0.35f, 0.65f, 0.95f, 0.8f);
            sr.rect(rowX, curY - 2f, 4f, 28f);
            sr.rect(rowX, curY + 25f, rowW, 1.5f);
        }
        curY -= 32f;

        // 5 Action rows (0..4)
        for (int i = 0; i < 5; i++) {
            float rY = curY;
            float bY = rY + 5f;
            boolean isWaiting = (waitingKeyAction == i);
            boolean isHov = (hoverKeyAction == i);

            if (rY + rowH >= boxBottom && rY <= boxTop) {
                // Row Card Body
                sr.setColor(0.06f, 0.09f, 0.16f, 0.88f);
                sr.rect(rowX, rY, rowW, rowH);
                // Top specular shine
                sr.setColor(0.20f, 0.35f, 0.55f, 0.35f);
                sr.rect(rowX + 1f, rY + rowH - 1.5f, rowW - 2f, 1.5f);
                sr.setColor(isHov ? new Color(0.35f, 0.85f, 1f, 0.85f) : new Color(0.16f, 0.24f, 0.36f, 0.6f));
                drawBoxBorder(sr, rowX, rY, rowW, rowH, isHov ? 1.4f : 1f);

                // Badge Background Box
                if (i == 0) {
                    sr.setColor(0.08f, 0.22f, 0.35f, 0.9f);
                    sr.rect(rowX + 12f, rY + 8f, 75f, 26f);
                    sr.setColor(0.30f, 0.70f, 0.95f, 0.75f);
                    drawBoxBorder(sr, rowX + 12f, rY + 8f, 75f, 26f, 1f);
                } else {
                    sr.setColor(0.14f, 0.16f, 0.36f, 0.9f);
                    sr.rect(rowX + 12f, rY + 8f, 92f, 26f);
                    sr.setColor(0.45f, 0.55f, 0.95f, 0.75f);
                    drawBoxBorder(sr, rowX + 12f, rY + 8f, 92f, 26f, 1f);
                }

                // Key Button Body
                if (isWaiting) {
                    sr.setColor(0.22f, 0.45f, 0.65f, 1f);
                } else {
                    sr.setColor(isHov ? new Color(0.18f, 0.28f, 0.44f, 1f) : new Color(0.10f, 0.15f, 0.24f, 0.95f));
                }
                sr.rect(btnKeyX, bY, btnKeyW, btnKeyH);

                // Key Button Border
                if (isWaiting) {
                    sr.setColor(1f, 0.85f, 0.25f, 1f);
                    drawBoxBorder(sr, btnKeyX, bY, btnKeyW, btnKeyH, 2.2f);
                } else {
                    sr.setColor(isHov ? new Color(0.45f, 0.85f, 1f, 1f) : new Color(0.25f, 0.40f, 0.58f, 0.8f));
                    drawBoxBorder(sr, btnKeyX, bY, btnKeyW, btnKeyH, 1.2f);
                }
            }

            curY -= 46f;
        }

        // Group 2 Header banner background
        if (curY + 28f >= boxBottom && curY - 2f <= boxTop) {
            sr.setColor(0.08f, 0.14f, 0.24f, 0.75f);
            sr.rect(rowX, curY - 2f, rowW, 28f);
            sr.setColor(1f, 0.82f, 0.25f, 0.8f);
            sr.rect(rowX, curY - 2f, 4f, 28f);
            sr.rect(rowX, curY + 25f, rowW, 1.5f);
        }
        curY -= 38f;

        // 9 Quickslot rows (5..13)
        for (int s = 0; s < 9; s++) {
            int actIdx = KeybindManager.ACTION_QUICK_1 + s;
            float rY = curY;
            float bY = rY + 5f;
            boolean isWaiting = (waitingKeyAction == actIdx);
            boolean isHov = (hoverKeyAction == actIdx);

            if (rY + rowH >= boxBottom && rY <= boxTop) {
                // Row Card Body
                sr.setColor(0.06f, 0.09f, 0.16f, 0.88f);
                sr.rect(rowX, rY, rowW, rowH);
                // Top specular shine
                sr.setColor(0.20f, 0.35f, 0.55f, 0.35f);
                sr.rect(rowX + 1f, rY + rowH - 1.5f, rowW - 2f, 1.5f);
                sr.setColor(isHov ? new Color(0.35f, 0.85f, 1f, 0.85f) : new Color(0.16f, 0.24f, 0.36f, 0.6f));
                drawBoxBorder(sr, rowX, rY, rowW, rowH, isHov ? 1.4f : 1f);

                // Quickslot Badge Box
                sr.setColor(0.25f, 0.18f, 0.08f, 0.9f);
                sr.rect(rowX + 12f, rY + 8f, 54f, 26f);
                sr.setColor(0.95f, 0.75f, 0.25f, 0.75f);
                drawBoxBorder(sr, rowX + 12f, rY + 8f, 54f, 26f, 1f);

                // Key Button Body
                if (isWaiting) {
                    sr.setColor(0.22f, 0.45f, 0.65f, 1f);
                } else {
                    sr.setColor(isHov ? new Color(0.18f, 0.28f, 0.44f, 1f) : new Color(0.10f, 0.15f, 0.24f, 0.95f));
                }
                sr.rect(btnKeyX, bY, btnKeyW, btnKeyH);

                // Key Button Border
                if (isWaiting) {
                    sr.setColor(1f, 0.85f, 0.25f, 1f);
                    drawBoxBorder(sr, btnKeyX, bY, btnKeyW, btnKeyH, 2.2f);
                } else {
                    sr.setColor(isHov ? new Color(0.45f, 0.85f, 1f, 1f) : new Color(0.25f, 0.40f, 0.58f, 0.8f));
                    drawBoxBorder(sr, btnKeyX, bY, btnKeyW, btnKeyH, 1.2f);
                }
            }

            curY -= 46f;
        }

        if (pushed) {
            sr.flush(); // FLUSH all scrollable list shapes to GPU while scissor is active!
            ScissorStack.popScissors();
        }

        // Calculate max scroll bounds
        float totalHeight = 32f + 5 * 46f + 38f + 9 * 46f + 15f; // ~730f
        maxScrollY = Math.max(0f, totalHeight - listH);

        // Render scrollbar track and thumb on right
        if (maxScrollY > 0) {
            float trackX = listX + listW - 10f;
            float trackY = listY + 4f;
            float trackH = listH - 8f;
            float barH = Math.max(34f, trackH * (listH / totalHeight));
            float pct = scrollY / maxScrollY;
            float barY = trackY + (trackH - barH) * (1f - pct);

            // Track groove
            sr.setColor(0.05f, 0.08f, 0.14f, 0.85f);
            sr.rect(trackX, trackY, 6f, trackH);
            sr.setColor(0.18f, 0.26f, 0.38f, 0.6f);
            drawBoxBorder(sr, trackX, trackY, 6f, trackH, 1f);

            // Thumb
            sr.setColor(isDraggingScrollbar ? new Color(1f, 0.90f, 0.40f, 1f) : new Color(0.90f, 0.75f, 0.25f, 0.95f));
            sr.rect(trackX, barY, 6f, barH);
            // Thumb inner shine
            sr.setColor(1f, 1f, 1f, 0.5f);
            sr.rect(trackX + 2f, barY + 2f, 2f, barH - 4f);
        }
    }

    private void drawControlsTabTexts(SpriteBatch batch, BitmapFont font, BitmapFont smallFont, float cx, float cy, float cw, float ch, Camera cam) {
        KeybindManager km = KeybindManager.getInstance();

        float listX = cx + 20f;
        float listY = cy + 48f;
        float listW = cw - 40f;
        float listH = ch - 66f;

        // Reset to Defaults Button text
        float btnDefW = 190f;
        float btnDefX = cx + 25f;
        float btnDefY = cy + 8f;

        font.getData().setScale(0.78f);
        font.setColor(hoverResetKeys ? Color.WHITE : new Color(1f, 0.92f, 0.6f, 1f));
        layout.setText(font, "ĐẶT LẠI MẶC ĐỊNH");
        font.draw(batch, layout, btnDefX + (btnDefW - layout.width) / 2f, btnDefY + 23f);

        // Anti-collision / Success message next to Reset button
        if (keybindMessage != null && keybindMsgTimer > 0f) {
            font.getData().setScale(0.80f);
            font.setColor(keybindMsgSuccess ? new Color(0.35f, 1f, 0.55f, 1f) : new Color(1f, 0.35f, 0.35f, 1f));
            layout.setText(font, keybindMessage);
            font.draw(batch, layout, cx + 230f, cy + 30f);
        }

        // Flush batch before enabling scissor
        batch.flush();

        Viewport vp = null;
        if (game.getScreen() instanceof com.fishingrpg.game.PlayScreen) {
            vp = ((com.fishingrpg.game.PlayScreen) game.getScreen()).getViewport();
        } else if (game.getScreen() instanceof com.fishingrpg.game.screens.MainMenuScreen) {
            vp = ((com.fishingrpg.game.screens.MainMenuScreen) game.getScreen()).getViewport();
        }

        boolean pushed = false;
        if (cam != null) {
            try {
                clipBounds.set(listX + 1.5f, listY + 1.5f, listW - 3f, listH - 3f);
                if (vp != null) {
                    ScissorStack.calculateScissors(cam, vp.getScreenX(), vp.getScreenY(), vp.getScreenWidth(), vp.getScreenHeight(), batch.getTransformMatrix(), clipBounds, scissors);
                } else {
                    ScissorStack.calculateScissors(cam, batch.getTransformMatrix(), clipBounds, scissors);
                }
                pushed = ScissorStack.pushScissors(scissors);
            } catch (Throwable ignored) {
                pushed = false;
            }
        }

        float rowW = listW - 30f;
        float rowH = 42f;
        float rowX = listX + 8f;
        float btnKeyW = 145f;
        float btnKeyX = rowX + rowW - btnKeyW - 12f;

        float boxTop = listY + listH;
        float boxBottom = listY;

        float curY = listY + listH - 36f + scrollY;

        // 1. Group Header 1
        if (curY + 28f >= boxBottom && curY - 2f <= boxTop) {
            font.getData().setScale(0.82f);
            font.setColor(0.35f, 0.85f, 1f, 1f);
            font.draw(batch, "✦ THAO TÁC CÂU CÁ & KỸ NĂNG CHỦ ĐỘNG", rowX + 16f, curY + 18f);
        }
        curY -= 32f;

        // 5 Action rows (0..4)
        for (int i = 0; i < 5; i++) {
            float rY = curY;
            float bY = rY + 5f;
            boolean isWaiting = (waitingKeyAction == i);

            if (rY + rowH >= boxBottom && rY <= boxTop) {
                // Action Badge tag
                smallFont.getData().setScale(0.70f);
                smallFont.setColor(0.35f, 0.75f, 0.95f, 0.9f);
                String tag = (i == 0) ? "[CÂU CÁ]" : "[KỸ NĂNG " + i + "]";
                layout.setText(smallFont, tag);
                float badgeBoxW = (i == 0) ? 75f : 92f;
                smallFont.draw(batch, layout, rowX + 12f + (badgeBoxW - layout.width) / 2f, rY + 26f);

                // Action Name
                font.getData().setScale(0.84f);
                font.setColor(1f, 0.94f, 0.75f, 1f);
                String actName = (i == 0) ? "Kéo Dây Cần Câu / Giằng Co" : KeybindManager.ACTION_NAMES[i];
                font.draw(batch, actName, rowX + 115f, rY + 27f);

                // Key button text
                String keyStr = isWaiting ? "NHẤN PHÍM..." : "[ " + km.getKeyDisplayName(i) + " ]";
                font.getData().setScale(0.80f);
                font.setColor(isWaiting ? new Color(1f, 0.85f, 0.2f, 1f) : (hoverKeyAction == i ? Color.WHITE : new Color(0.85f, 0.95f, 1f, 1f)));
                layout.setText(font, keyStr);
                font.draw(batch, layout, btnKeyX + (btnKeyW - layout.width) / 2f, bY + 22f);
            }

            curY -= 46f;
        }

        // 2. Group Header 2
        if (curY + 28f >= boxBottom && curY - 2f <= boxTop) {
            font.getData().setScale(0.82f);
            font.setColor(1f, 0.85f, 0.35f, 1f);
            font.draw(batch, "✦ THANH PHÍM TẮT NHANH (QUICKSLOT 1 - 9)", rowX + 16f, curY + 18f);
        }
        curY -= 38f;

        // 9 Quickslot rows (5..13)
        for (int s = 0; s < 9; s++) {
            int actIdx = KeybindManager.ACTION_QUICK_1 + s;
            float rY = curY;
            float bY = rY + 5f;
            boolean isWaiting = (waitingKeyAction == actIdx);

            if (rY + rowH >= boxBottom && rY <= boxTop) {
                // Quickslot Badge tag
                smallFont.getData().setScale(0.70f);
                smallFont.setColor(1f, 0.82f, 0.35f, 0.9f);
                String tag = "[Ô " + (s + 1) + "]";
                layout.setText(smallFont, tag);
                smallFont.draw(batch, layout, rowX + 12f + (54f - layout.width) / 2f, rY + 26f);

                // Action Name
                font.getData().setScale(0.84f);
                font.setColor(0.85f, 0.94f, 1f, 1f);
                font.draw(batch, "Phím Tắt Vật Phẩm / Mồi Câu Ô " + (s + 1), rowX + 76f, rY + 27f);

                // Key button text
                String keyStr = isWaiting ? "NHẤN PHÍM..." : "[ " + km.getQuickslotKeyDisplayName(s) + " ]";
                font.getData().setScale(0.80f);
                font.setColor(isWaiting ? new Color(1f, 0.85f, 0.2f, 1f) : (hoverKeyAction == actIdx ? Color.WHITE : new Color(0.35f, 0.92f, 1f, 1f)));
                layout.setText(font, keyStr);
                font.draw(batch, layout, btnKeyX + (btnKeyW - layout.width) / 2f, bY + 22f);
            }

            curY -= 46f;
        }

        batch.flush();
        if (pushed) {
            ScissorStack.popScissors();
        }

        font.getData().setScale(1f);
        smallFont.getData().setScale(1f);
    }

    private void drawDataTabShapes(ShapeRenderer sr, float cx, float cy, float cw, float ch) {
        float btnDataW = 680f;
        float btnDataH = 64f;
        float btnDataX = cx + (cw - btnDataW) / 2f;
        float bExportY = cy + ch - 95f;
        float bImportY = cy + ch - 175f;
        float bResetY  = cy + ch - 255f;

        // Export Card
        sr.setColor(hoverExport ? new Color(0.14f, 0.26f, 0.42f, 0.95f) : new Color(0.08f, 0.14f, 0.24f, 0.90f));
        sr.rect(btnDataX, bExportY, btnDataW, btnDataH);
        sr.setColor(0.30f, 0.70f, 1f, 0.5f);
        sr.rect(btnDataX + 2f, bExportY + btnDataH - 2f, btnDataW - 4f, 2f);
        sr.setColor(hoverExport ? new Color(0.40f, 0.85f, 1f, 1f) : new Color(0.20f, 0.45f, 0.70f, 0.8f));
        drawBoxBorder(sr, btnDataX, bExportY, btnDataW, btnDataH, hoverExport ? 1.8f : 1.2f);

        // Export Badge Box
        sr.setColor(0.12f, 0.28f, 0.45f, 0.9f);
        sr.rect(btnDataX + 16f, bExportY + 16f, 76f, 32f);
        drawBoxBorder(sr, btnDataX + 16f, bExportY + 16f, 76f, 32f, 1f);

        // Import Card
        sr.setColor(hoverImport ? new Color(0.25f, 0.24f, 0.14f, 0.95f) : new Color(0.14f, 0.13f, 0.08f, 0.90f));
        sr.rect(btnDataX, bImportY, btnDataW, btnDataH);
        sr.setColor(1f, 0.85f, 0.35f, 0.5f);
        sr.rect(btnDataX + 2f, bImportY + btnDataH - 2f, btnDataW - 4f, 2f);
        sr.setColor(hoverImport ? new Color(1f, 0.88f, 0.4f, 1f) : new Color(0.70f, 0.60f, 0.25f, 0.8f));
        drawBoxBorder(sr, btnDataX, bImportY, btnDataW, btnDataH, hoverImport ? 1.8f : 1.2f);

        // Import Badge Box
        sr.setColor(0.30f, 0.25f, 0.10f, 0.9f);
        sr.rect(btnDataX + 16f, bImportY + 16f, 76f, 32f);
        drawBoxBorder(sr, btnDataX + 16f, bImportY + 16f, 76f, 32f, 1f);

        // Reset Card
        sr.setColor(hoverReset ? new Color(0.38f, 0.12f, 0.12f, 0.95f) : new Color(0.20f, 0.08f, 0.08f, 0.90f));
        sr.rect(btnDataX, bResetY, btnDataW, btnDataH);
        sr.setColor(1f, 0.40f, 0.40f, 0.5f);
        sr.rect(btnDataX + 2f, bResetY + btnDataH - 2f, btnDataW - 4f, 2f);
        sr.setColor(hoverReset ? new Color(1f, 0.45f, 0.45f, 1f) : new Color(0.75f, 0.25f, 0.25f, 0.8f));
        drawBoxBorder(sr, btnDataX, bResetY, btnDataW, btnDataH, hoverReset ? 1.8f : 1.2f);

        // Reset Badge Box
        sr.setColor(0.40f, 0.12f, 0.12f, 0.9f);
        sr.rect(btnDataX + 16f, bResetY + 16f, 76f, 32f);
        drawBoxBorder(sr, btnDataX + 16f, bResetY + 16f, 76f, 32f, 1f);
    }

    private void drawDataTabTexts(SpriteBatch batch, BitmapFont font, BitmapFont smallFont, float cx, float cy, float cw, float ch) {
        float btnDataW = 680f;
        float btnDataH = 64f;
        float btnDataX = cx + (cw - btnDataW) / 2f;
        float bExportY = cy + ch - 95f;
        float bImportY = cy + ch - 175f;
        float bResetY  = cy + ch - 255f;

        // 1. Export
        font.getData().setScale(0.76f);
        font.setColor(0.40f, 0.85f, 1f, 1f);
        layout.setText(font, "XUẤT");
        font.draw(batch, layout, btnDataX + 16f + (76f - layout.width) / 2f, bExportY + 38f);

        font.getData().setScale(0.88f);
        font.setColor(hoverExport ? Color.WHITE : new Color(0.90f, 0.96f, 1f, 1f));
        font.draw(batch, "XUẤT DỮ LIỆU TIẾN TRÌNH (TẠO FILE .JSON)", btnDataX + 110f, bExportY + 46f);
        smallFont.getData().setScale(0.74f);
        smallFont.setColor(0.65f, 0.82f, 0.95f, 0.85f);
        smallFont.draw(batch, "Sao lưu toàn bộ cấp độ, trang bị, kho cá ra file và Clipboard", btnDataX + 110f, bExportY + 24f);

        // 2. Import
        font.getData().setScale(0.76f);
        font.setColor(1f, 0.85f, 0.35f, 1f);
        layout.setText(font, "NẠP");
        font.draw(batch, layout, btnDataX + 16f + (76f - layout.width) / 2f, bImportY + 38f);

        font.getData().setScale(0.88f);
        font.setColor(hoverImport ? Color.WHITE : new Color(1f, 0.96f, 0.85f, 1f));
        font.draw(batch, "NẠP DỮ LIỆU TIẾN TRÌNH (CHỌN FILE LƯU TRỮ)", btnDataX + 110f, bImportY + 46f);
        smallFont.getData().setScale(0.74f);
        smallFont.setColor(0.90f, 0.82f, 0.65f, 0.85f);
        smallFont.draw(batch, "Khôi phục nhân vật từ file sao lưu .json hoặc bộ nhớ tạm Clipboard", btnDataX + 110f, bImportY + 24f);

        // 3. Reset
        font.getData().setScale(0.74f);
        font.setColor(1f, 0.40f, 0.40f, 1f);
        layout.setText(font, "XÓA");
        font.draw(batch, layout, btnDataX + 16f + (76f - layout.width) / 2f, bResetY + 38f);

        font.getData().setScale(0.88f);
        font.setColor(hoverReset ? Color.WHITE : new Color(1f, 0.85f, 0.85f, 1f));
        font.draw(batch, "ĐẶT LẠI DỮ LIỆU TRÒ CHƠI (RESET GAME)", btnDataX + 110f, bResetY + 46f);
        smallFont.getData().setScale(0.74f);
        smallFont.setColor(0.95f, 0.65f, 0.65f, 0.85f);
        smallFont.draw(batch, "Khởi tạo lại tiến trình về Cấp 1 (Hệ thống tự động sao lưu trước khi xóa)", btnDataX + 110f, bResetY + 24f);

        // Status Notification Message if any
        if (statusMessage != null && statusTimer > 0f) {
            font.getData().setScale(0.82f);
            font.setColor(statusIsSuccess ? new Color(0.35f, 1f, 0.55f, 1f) : new Color(1f, 0.45f, 0.45f, 1f));
            layout.setText(font, statusMessage);
            font.draw(batch, layout, cx + (cw - layout.width) / 2f, cy + 28f);
        }
    }

    private void drawResetModalShapes(ShapeRenderer sr) {
        float modalW = 600f;
        float modalH = 340f;
        float modalX = (1280f - modalW) / 2f;
        float modalY = (720f - modalH) / 2f;

        // Dark dimming shroud over the entire screen
        sr.setColor(0f, 0f, 0f, 0.88f);
        sr.rect(0, 0, 1280f, 720f);

        // Modal chassis
        sr.setColor(0.12f, 0.05f, 0.05f, 0.98f);
        sr.rect(modalX, modalY, modalW, modalH);

        // Modal border
        sr.setColor(0.95f, 0.25f, 0.25f, 1f);
        drawBoxBorder(sr, modalX, modalY, modalW, modalH, 2.5f);

        float btnModalW = 220f;
        float btnModalH = 46f;
        float btnModalY = modalY + 24f;
        float btnCancelX = modalX + 45f;
        float btnConfirmX = modalX + modalW - 45f - btnModalW;

        // Cancel button
        sr.setColor(modalHoveredButton == 0 ? new Color(0.25f, 0.35f, 0.5f, 1f) : new Color(0.15f, 0.22f, 0.32f, 1f));
        sr.rect(btnCancelX, btnModalY, btnModalW, btnModalH);
        sr.setColor(0.4f, 0.7f, 1f, 0.8f);
        drawBoxBorder(sr, btnCancelX, btnModalY, btnModalW, btnModalH, 1.5f);

        // Confirm button
        boolean canConfirm = resetCountdown <= 0f;
        if (canConfirm) {
            sr.setColor(modalHoveredButton == 1 ? new Color(0.85f, 0.15f, 0.15f, 1f) : new Color(0.6f, 0.1f, 0.1f, 1f));
        } else {
            sr.setColor(0.2f, 0.1f, 0.1f, 0.8f);
        }
        sr.rect(btnConfirmX, btnModalY, btnModalW, btnModalH);
        sr.setColor(canConfirm ? new Color(1f, 0.3f, 0.3f, 1f) : new Color(0.5f, 0.2f, 0.2f, 0.6f));
        drawBoxBorder(sr, btnConfirmX, btnModalY, btnModalW, btnModalH, 1.5f);
    }

    private void drawResetModalTexts(SpriteBatch batch, BitmapFont font, BitmapFont titleFont) {
        float modalW = 600f;
        float modalH = 340f;
        float modalX = (1280f - modalW) / 2f;
        float modalY = (720f - modalH) / 2f;

        titleFont.getData().setScale(0.75f);
        titleFont.setColor(1f, 0.3f, 0.3f, 1f);
        layout.setText(titleFont, "CẢNH BÁO: XÓA DỮ LIỆU!");
        titleFont.draw(batch, layout, modalX + (modalW - layout.width) / 2f, modalY + modalH - 25f);

        font.getData().setScale(0.82f);
        font.setColor(1f, 0.9f, 0.9f, 0.95f);
        font.draw(batch, "Hành động này sẽ xóa toàn bộ cấp độ, vàng, trang bị, cần câu\nvà kỷ lục cá của bạn. Game sẽ tự động sao lưu 1 bản trước khi xóa.", modalX + 45f, modalY + modalH - 75f);

        if (resetCountdown > 0f) {
            int sec = (int) Math.ceil(resetCountdown);
            font.setColor(1f, 0.8f, 0.2f, 1f);
            font.draw(batch, "Vui lòng chờ suy nghĩ kỹ trong: " + sec + " giây...", modalX + 45f, modalY + 110f);
        } else {
            font.setColor(0.4f, 1f, 0.5f, 1f);
            font.draw(batch, "- Đã sẵn sàng. Bạn có thể xác nhận nếu thực sự muốn xóa.", modalX + 45f, modalY + 110f);
        }

        // Cancel text
        float btnModalW = 220f;
        float btnModalY = modalY + 24f;
        float btnCancelX = modalX + 45f;
        float btnConfirmX = modalX + modalW - 45f - btnModalW;

        font.setColor(Color.WHITE);
        layout.setText(font, "HỦY BỎ (ESC)");
        font.draw(batch, layout, btnCancelX + (btnModalW - layout.width) / 2f, btnModalY + 30f);

        // Confirm text
        font.setColor(resetCountdown <= 0f ? Color.WHITE : new Color(0.6f, 0.6f, 0.6f, 0.6f));
        String confirmTxt = (resetCountdown > 0f) ? ("XÓA (" + (int) Math.ceil(resetCountdown) + "s)") : "XÁC NHẬN XÓA";
        layout.setText(font, confirmTxt);
        font.draw(batch, layout, btnConfirmX + (btnModalW - layout.width) / 2f, btnModalY + 30f);
    }

    private void drawBoxBorder(ShapeRenderer sr, float x, float y, float w, float h, float thickness) {
        sr.rect(x, y, w, thickness);
        sr.rect(x, y + h - thickness, w, thickness);
        sr.rect(x, y, thickness, h);
        sr.rect(x + w - thickness, y, thickness, h);
    }
}
