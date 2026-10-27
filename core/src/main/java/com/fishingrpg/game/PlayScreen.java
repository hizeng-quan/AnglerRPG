package com.fishingrpg.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.screens.MainMenuScreen;
import com.fishingrpg.game.systems.FishingManager;
import com.fishingrpg.game.ui.GameHUD;
import java.util.Map;
import com.fishingrpg.game.ui.FloatingTextManager;
import com.fishingrpg.game.systems.AudioManager;

public class PlayScreen implements Screen {

    private FishingRPG game;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Viewport viewport;
    private static final float WORLD_WIDTH  = 1280f;
    private static final float WORLD_HEIGHT = 720f;
    private String message = null;
    private float messageTimer = 0f;
    private float bloodMoonAlpha = 0f;
    private boolean prevBloodMoonActive = false;

    private Player player;
    private GameHUD hud;
    public FishingManager fishingManager;
    private int framesToSkip = 5;
    private float timer = 0f;
    // Cached Vector3 để tránh allocate mời frame
    private final com.badlogic.gdx.math.Vector3 mousePos = new com.badlogic.gdx.math.Vector3();
    private final boolean[] activeSkillInputs = new boolean[4];
    
    private FloatingTextManager floatingTextManager;
    private final com.fishingrpg.game.systems.vfx.ScreenShakeController screenShake = new com.fishingrpg.game.systems.vfx.ScreenShakeController(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f);
    private final com.fishingrpg.game.ui.PatchNotesDialog patchNotesDialog;
    private final com.fishingrpg.game.ui.SettingsDialog settingsDialog;

    public PlayScreen(FishingRPG game) {
        this.game = game;
        this.shapeRenderer = game.shapeRenderer;
        this.font = game.font;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        this.player = game.player;
        this.fishingManager = new FishingManager(player);
        this.hud = new GameHUD(shapeRenderer, font, this);
        this.floatingTextManager = new FloatingTextManager();
        this.fishingManager.setFloatingTextManager(this.floatingTextManager);
        this.fishingManager.setScreenShake(this.screenShake);
        this.patchNotesDialog = new com.fishingrpg.game.ui.PatchNotesDialog(game);
        this.settingsDialog = new com.fishingrpg.game.ui.SettingsDialog(game);
        this.settingsDialog.setListener(this::syncPlayerState);
    }

    public void syncPlayerState() {
        this.player = game.player;
        if (this.fishingManager != null) {
            this.fishingManager.setPlayer(this.player);
        }
    }

    @Override
    public void show() {
        if (player.getQuestManager() != null) {
            player.getQuestManager().callback = new com.fishingrpg.game.systems.QuestManager.QuestCallback() {
                @Override
                public void onQuestCompleted(com.fishingrpg.game.entities.Quest quest) {
                    message = "NHIỆM VỤ HOÀN THÀNH: " + quest.title + "!"; messageTimer = 3f;
                }
            };
        }
        framesToSkip = 5; // Prevent accidental double clicks when entering screen
        Gdx.input.setInputProcessor(new com.badlogic.gdx.InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                if (settingsDialog != null && settingsDialog.isOpen()) {
                    settingsDialog.handleScroll(amountY);
                    return true;
                }
                if (patchNotesDialog != null && patchNotesDialog.isOpen()) {
                    patchNotesDialog.handleScroll(amountY);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        if (Gdx.graphics.getWidth() <= 0 || Gdx.graphics.getHeight() <= 0) return;
        if (framesToSkip > 0) {
            framesToSkip--;
        }
        update(delta);
        if (game.getScreen() != this) return;
        
        if (fishingManager.state == FishingManager.State.IDLE || fishingManager.state == FishingManager.State.RESULT) {
            AudioManager.getInstance().playBGM("bgm_menu");
        } else {
            String mapId = player.getCurrentMap();
            String bgmName = "bgm_pond";
            if (mapId.equals("bai_bien")) bgmName = "bgm_beach";
            else if (mapId.equals("dao_xa")) bgmName = "bgm_island";
            AudioManager.getInstance().playBGM(bgmName);
        }
        
        if (fishingManager.bossSkillJustUsed) {
            fishingManager.bossSkillJustUsed = false;
            screenShake.shakeHeavy();
        }
        if (fishingManager.state == FishingManager.State.COMBAT) {
            float tensionRatio = player.getCurrentLineTension() / Math.max(1f, player.getMaxLineTension());
            // Giảm mốc bắt đầu rung xuống 0.80f và tăng cường độ rung khi gần chạm max (+0.02f trauma)
            if (tensionRatio > 0.80f) {
                float maxTrauma = (tensionRatio > 0.94f) ? 0.45f : ((tensionRatio > 0.88f) ? 0.30f : 0.18f);
                float traumaAdd = (tensionRatio > 0.94f) ? 0.035f : 0.02f;
                if (screenShake.getTrauma() < maxTrauma) {
                    screenShake.addTrauma(traumaAdd);
                }
            }
        }

        screenShake.update(delta, camera);
        game.batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        if (fishingManager.state == FishingManager.State.IDLE) {
            ScreenUtils.clear(0.1f, 0.4f, 0.6f, 1f);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            hud.drawIdleShapes(player);
            shapeRenderer.end();
            game.batch.begin();
            hud.drawIdleText(game.batch, player);
            game.batch.end();
        } else {
            timer += delta;
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            String mapId = player.getCurrentMap();
            if (mapId.equals("ao_lang")) {
                com.fishingrpg.game.ui.AoLangBackground.render(shapeRenderer, timer, WORLD_WIDTH, WORLD_HEIGHT);
            } else if (mapId.equals("bai_bien")) {
                com.fishingrpg.game.ui.BaiBienBackground.render(shapeRenderer, timer, WORLD_WIDTH, WORLD_HEIGHT);
            } else if (mapId.equals("dao_xa")) {
                // Abyss: very dark blue, glowing particles
                shapeRenderer.setColor(0.01f, 0.02f, 0.05f, 1f);
                shapeRenderer.rect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
                
                shapeRenderer.setColor(0.05f, 0.1f, 0.15f, 0.5f);
                shapeRenderer.circle(WORLD_WIDTH/2, -100, 400);
                
                for(int i=0; i<40; i++) {
                    shapeRenderer.setColor((i%3==0)?0f:0.2f, (i%2==0)?0.8f:0.2f, 1f, 0.6f);
                    float bx = (i * 97 + timer * 5f) % WORLD_WIDTH;
                    float by = ((i * 151 + timer * 25f) % (WORLD_HEIGHT + 100)) - 50;
                    shapeRenderer.circle(bx + com.badlogic.gdx.math.MathUtils.sin(timer*0.5f + i)*30f, by, 1f + (i%3));
                }
            }

            // HLW-05: Chuyen sac em ai 3.0s sang Bau Troi Huyet Nguyet
            boolean bmActive = fishingManager.isBloodMoonActive();
            if (bmActive) {
                bloodMoonAlpha = Math.min(1.0f, bloodMoonAlpha + delta / 3.0f);
                if (!prevBloodMoonActive) {
                    prevBloodMoonActive = true;
                    message = "✦ BẦU TRỜI HUYẾT NGUYỆT GIÁNG TRẦN ✦ (+20% Vàng, +20% EXP, x2 Cá Halloween!)";
                    messageTimer = 4.0f;
                    com.fishingrpg.game.systems.AudioManager.getInstance().playSFX("sfx_blood_moon_roar");
                }
            } else {
                bloodMoonAlpha = Math.max(0f, bloodMoonAlpha - delta / 3.0f);
                prevBloodMoonActive = false;
            }

            if (bloodMoonAlpha > 0.01f) {
                drawBloodMoonOverlay(shapeRenderer, bloodMoonAlpha, timer);
            }

            com.fishingrpg.game.ui.CombatVFXManager.getInstance().drawShapes(shapeRenderer);
            hud.drawShapes(player, fishingManager.currentFish);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

            game.batch.begin();
            hud.drawText(game.batch, player, fishingManager.currentCatch, fishingManager.combatMessage, fishingManager.resultLine);
            floatingTextManager.updateAndRender(delta, game.batch, font);
            game.batch.end();
        }

        if (messageTimer > 0 && message != null) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            com.fishingrpg.game.ui.NotificationUI.drawNotification(shapeRenderer, game.batch, game.font, message, messageTimer, 2f, WORLD_WIDTH, WORLD_HEIGHT);
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        if (patchNotesDialog != null && patchNotesDialog.isOpen()) {
            patchNotesDialog.render(shapeRenderer, game.batch, font, game.titleFont != null ? game.titleFont : font, game.smallFont != null ? game.smallFont : font, timer, camera);
        }

        if (settingsDialog != null && settingsDialog.isOpen()) {
            settingsDialog.render(shapeRenderer, game.batch, font, game.titleFont != null ? game.titleFont : font, game.smallFont != null ? game.smallFont : font, timer, camera);
        }
    }

    private void update(float delta) {
        com.fishingrpg.game.ui.CombatVFXManager.getInstance().update(delta);
        if (messageTimer > 0) messageTimer -= delta;
        player.update(delta);
        if (player.justFilledStamina) {
            player.justFilledStamina = false;
            message = "Thể lực đã hồi đầy!";
            messageTimer = 2f;
        }
        
        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mousePos);

        if (settingsDialog != null && settingsDialog.isOpen()) {
            settingsDialog.update(delta);
            if (settingsDialog.handleInput(mousePos)) return;
        }

        if (patchNotesDialog != null && patchNotesDialog.isOpen()) {
            if (patchNotesDialog.handleInput(mousePos)) return;
        }

        hud.updateHover(mousePos, fishingManager.state);

        if (framesToSkip <= 0 && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (hud.isQuickslotPrevClicked(mousePos)) {
                hud.prevQuickslotPage();
                return;
            }
            if (hud.isQuickslotNextClicked(mousePos)) {
                hud.nextQuickslotPage();
                return;
            }
        }

        String clickedItem = hud.getClickedInventoryItem(mousePos, fishingManager.state);
        boolean itemClicked = false;
        if (framesToSkip <= 0 && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && clickedItem != null) {
            useItem(clickedItem);
            itemClicked = true;
        }

        boolean actClose = framesToSkip <= 0 && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && hud.isCloseHovered();
        if (actClose) {
            fishingManager.showCatchSidebar = false;
        }

        boolean mouseJustClicked = framesToSkip <= 0 && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && !itemClicked && !actClose;
        boolean actClicked = mouseJustClicked && hud.isCastHovered();
        
        if (fishingManager.state == FishingManager.State.IDLE && actClicked) {
            // Open MapScreen instead of casting directly
            game.setScreen(new com.fishingrpg.game.screens.MapScreen(game, this));
            return;
        }
        
        if (fishingManager.state == FishingManager.State.CASTING || fishingManager.state == FishingManager.State.BITE) {
            actClicked = actClicked || mouseJustClicked;
        }

        // Quick slots 1-9 for the currently active inventory page
        java.util.List<String> pageItems = hud.getItemsOnCurrentPage();
        for (int k = 0; k < pageItems.size() && k < 9; k++) {
            int keyCode = com.fishingrpg.game.systems.KeybindManager.getInstance().getQuickslotKey(k);
            if (Gdx.input.isKeyJustPressed(keyCode)) {
                useItem(pageItems.get(k));
                break;
            }
        }

        if (framesToSkip <= 0 && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (hud.isShopClicked(mousePos, fishingManager.state)) {
                game.setScreen(new com.fishingrpg.game.screens.ShopScreen(game, player, this));
                // do not dispose yet, because ShopScreen will return back to this screen
                return;
            }
            if (hud.isCatalogClicked(mousePos, fishingManager.state)) {
                game.setScreen(new com.fishingrpg.game.screens.CatalogScreen(game, this, player));
                // do not dispose yet
                return;
            }
            if (hud.isSkillsClicked(mousePos, fishingManager.state)) {
                game.setScreen(new com.fishingrpg.game.screens.SkillTreeScreen(game, player, this));
                return;
            }
            if (hud.isGachaClicked(mousePos, fishingManager.state)) {
                game.setScreen(new com.fishingrpg.game.screens.GachaScreen(game, this));
                return;
            }
            if (hud.isEquipmentClicked(mousePos, fishingManager.state)) {
                game.setScreen(new com.fishingrpg.game.screens.EquipmentScreen(game, this));
                return;
            }
            if (hud.isQuestClicked(mousePos, fishingManager.state)) {
                game.setScreen(new com.fishingrpg.game.screens.QuestScreen(game, this, player));
                return;
            }
            if (hud.isEventClicked(mousePos, fishingManager.state)) {
                game.setScreen(new com.fishingrpg.game.screens.EventScreen(game, this));
                return;
            }
        }

        boolean menuClicked = framesToSkip <= 0 && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && hud.isMenuClicked(mousePos, fishingManager.state);
        int pullKey = com.fishingrpg.game.systems.KeybindManager.getInstance().getPullKey();
        boolean isSpaceDown = framesToSkip <= 0 && (Gdx.input.isKeyPressed(pullKey) || (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && hud.isCangHovered()));
        boolean isSpaceJust = framesToSkip <= 0 && (Gdx.input.isKeyJustPressed(pullKey) || (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && hud.isCangHovered()));

        activeSkillInputs[0] = false;
        activeSkillInputs[1] = false;
        activeSkillInputs[2] = false;
        activeSkillInputs[3] = false;
        if (framesToSkip <= 0) {
            com.fishingrpg.game.systems.KeybindManager km = com.fishingrpg.game.systems.KeybindManager.getInstance();
            int k0 = km.getSkillKey(0);
            int k1 = km.getSkillKey(1);
            int k2 = km.getSkillKey(2);
            int k3 = km.getSkillKey(3);
            activeSkillInputs[0] = (k0 > 0 && Gdx.input.isKeyJustPressed(k0)) || (k0 != Input.Keys.Q && Gdx.input.isKeyJustPressed(Input.Keys.Q)) || (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && hud.isActiveSlotHovered(0));
            activeSkillInputs[1] = (k1 > 0 && Gdx.input.isKeyJustPressed(k1)) || (k1 != Input.Keys.W && Gdx.input.isKeyJustPressed(Input.Keys.W)) || (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && hud.isActiveSlotHovered(1));
            activeSkillInputs[2] = (k2 > 0 && Gdx.input.isKeyJustPressed(k2)) || (k2 != Input.Keys.E && Gdx.input.isKeyJustPressed(Input.Keys.E)) || (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && hud.isActiveSlotHovered(2));
            activeSkillInputs[3] = (k3 > 0 && Gdx.input.isKeyJustPressed(k3)) || (k3 != Input.Keys.R && Gdx.input.isKeyJustPressed(Input.Keys.R)) || (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && hud.isActiveSlotHovered(3));
        }

        boolean settingClicked = framesToSkip <= 0 && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && hud.isSettingClicked(mousePos, fishingManager.state);
        if (settingClicked) {
            settingsDialog.open();
            return;
        }

        boolean newsClicked = framesToSkip <= 0 && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && hud.isNewsClicked(mousePos, fishingManager.state);
        if (newsClicked) {
            patchNotesDialog.open();
            return;
        }

        boolean escPressed = framesToSkip <= 0 && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);

        if (menuClicked || escPressed) {
            if (settingsDialog != null && settingsDialog.isOpen()) {
                settingsDialog.close();
                return;
            }
            if (patchNotesDialog != null && patchNotesDialog.isOpen()) {
                patchNotesDialog.close();
                return;
            }
            com.fishingrpg.game.data.SaveManager.savePlayer(player);
            if (fishingManager.state == FishingManager.State.IDLE) {
                // Game HUD: Quay lại Menu chính
                game.setScreen(new MainMenuScreen(game));
                dispose();
            } else {
                // Màn hình chơi câu cá: Quay lại Màn hình chọn bản đồ
                game.setScreen(new com.fishingrpg.game.screens.MapScreen(game, this));
            }
            return;
        }

        fishingManager.update(delta, actClicked, isSpaceJust, isSpaceDown, activeSkillInputs, actClose);
    }

    private void useItem(String itemName) {
        if (!player.hasItem(itemName)) return;

        com.fishingrpg.game.entities.ItemData item = com.fishingrpg.game.data.ItemDatabase.getItem(itemName);
        if (itemName.startsWith("Sách: ")) {
            handleSkillBook(itemName);
            return;
        }
        if (item == null) return;

        // Kiểm tra điều kiện sử dụng theo trạng thái game
        boolean isCombat = (fishingManager.state == FishingManager.State.COMBAT
                         || fishingManager.state == FishingManager.State.CASTING
                         || fishingManager.state == FishingManager.State.BITE);
        if (item.usageType == com.fishingrpg.game.entities.ItemUsageType.IN_COMBAT && !isCombat) {
            fishingManager.combatMessage = "Chỉ được dùng " + itemName + " trong lúc câu!";
            return;
        }
        if (item.usageType == com.fishingrpg.game.entities.ItemUsageType.PRE_COMBAT && isCombat) {
            fishingManager.combatMessage = "Không thể dùng " + itemName + " trong lúc câu!";
            return;
        }

        boolean consume = applyItemEffect(itemName);

        // Bait Retention: trang bị Hook có thể giữ lại mồi
        boolean isBait = itemName.equals("Mồi Thơm") || itemName.equals("Mồi Rủi Ro");
        if (consume && isBait && player.getBaitRetention() > 0) {
            if (com.badlogic.gdx.math.MathUtils.random(100f) < player.getBaitRetention()) {
                consume = false;
                message = "Trang bị: Giữ được mồi (" + itemName + ")!";
                messageTimer = 2.0f;
            }
        }

        if (consume) {
            player.consumeItem(itemName);
            if (player.getQuestManager() != null) {
                player.getQuestManager().addProgress("item", 1);
            }
        }
    }

    /**
     * Xử lý tiêu thụ Sách kỹ năng:
     * - Cấp 0: Học kỹ năng lên Cấp 1
     * - Cấp 1..Max-1: Đột phá cấp độ (+1 Cấp) thay cho 1 Skill Point
     * - Cấp Max: Quy đổi thành +1 Điểm Kỹ Năng (SP)
     * Chỉ dùng được khi đang IDLE hoặc PRE_CAST.
     */
    private void handleSkillBook(String itemName) {
        if (fishingManager.state != FishingManager.State.IDLE
                && fishingManager.state != FishingManager.State.PRE_CAST) {
            fishingManager.combatMessage = "Chỉ được dùng Sách trước khi câu!";
            return;
        }
        String skillId = null;
        switch (itemName) {
            case "Sách: Dưỡng Sức":   skillId = "duong_suc";           break;
            case "Sách: Giật Kép":    skillId = "giat_kep";            break;
            case "Sách: Mắt Đại Bàng":skillId = "mat_dai_bang";       break;
            case "Sách: Thuần Hóa":   skillId = "thuan_hoa";           break;
            case "Sách: Trói Buộc":   skillId = "loi_nguyen_troi_buoc"; break;
            case "Sách: Trái Tim ĐD": skillId = "trai_tim_dai_duong";  break;
        }
        if (skillId == null) return;

        com.fishingrpg.game.skills.Skill s = player.getSkillTree().getSkill(skillId);
        if (s == null) return;

        if (s.getLevel() == 0) {
            s.setCurrentLevel(1);
            player.consumeItem(itemName);
            fishingManager.combatMessage = "HỌC THÀNH CÔNG: " + s.name + " (Cấp 1)!";
            player.applySkillBonuses();
            AudioManager.getInstance().playSFX("sfx_success");
            com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerItemUseVFX(itemName, 640f, 360f);
        } else if (!s.isMaxed()) {
            s.upgrade();
            player.consumeItem(itemName);
            fishingManager.combatMessage = "ĐỘT PHÁ BÍ KÍP! " + s.name + " đã lên Cấp " + s.getLevel() + " (Miễn phí 1 SP)!";
            player.applySkillBonuses();
            AudioManager.getInstance().playSFX("sfx_success");
            com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerItemUseVFX(itemName, 640f, 360f);
        } else {
            player.getProgression().setSkillPoints(player.getSkillPoints() + 1);
            player.consumeItem(itemName);
            fishingManager.combatMessage = "KỸ NĂNG ĐÃ TỐI ĐA: Quy đổi " + itemName + " thành +1 Điểm Kỹ Năng (SP)!";
            AudioManager.getInstance().playSFX("sfx_success");
            com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerItemUseVFX(itemName, 640f, 360f);
        }
    }

    /**
     * Áp dụng hiệu ứng của vật phẩm.
     * @return true nếu vật phẩm nên bị tiêu thụ, false nếu không mất.
     */
    private boolean applyItemEffect(String itemName) {
        float thucDuongBonus = player.isSkillEquipped("no_bung") ? player.getSkillTree().getNoBungBonus() : 0f;
        float healMult = 1f + (thucDuongBonus / 100f);
        boolean success = true;

        switch (itemName) {
            case "Bánh Mì": {
                int heal = (int)(25f * healMult);
                player.consumeStamina(-heal);
                fishingManager.combatMessage = "Dùng Bánh Mì (+" + heal + " Thể lực)!";
                break;
            }
            case "Nước Tăng Lực": {
                int heal = (int)(player.getMaxStamina() * 0.25f * healMult);
                player.consumeStamina(-heal);
                fishingManager.combatMessage = "Nước Tăng Lực: Hồi 25% Thể lực (+" + heal + ")!";
                break;
            }
            case "Dây Câu Tốt":
                player.decreaseTension(20f);
                fishingManager.combatMessage = "Dùng Dây Câu Tốt (-20 Tension)!";
                break;
            case "Mồi Thơm": {
                if (player.isBaitBuffActive()) {
                    fishingManager.combatMessage = "Mồi Thơm đang hoạt động rồi!";
                    return false;
                }
                boolean consumed = true;
                // Chuyên Gia Mồi: cơ hội không tiêu hao mồi
                if (player.isSkillEquipped("chuyen_gia_moi")
                        && player.getSkillTree().getSkill("chuyen_gia_moi").isUnlocked()) {
                    float chance = player.getSkillTree().getSkill("chuyen_gia_moi").getEffect() / 100f;
                    if (com.badlogic.gdx.math.MathUtils.random() < chance) {
                        consumed = false;
                        message = "Chuyên Gia Mồi: Không tiêu hao Mồi Thơm!";
                        messageTimer = 2.0f;
                    }
                }
                player.setBaitBuffActive(true);
                fishingManager.combatMessage = "Dùng Mồi Thơm (Tăng cá hiếm)!" + (!consumed ? " [Không mất mồi]" : "");
                success = consumed;
                break;
            }
            case "Lưới Đánh Cá":
                if (player.isFishingNetActive()) {
                    fishingManager.combatMessage = "Lưới Đánh Cá đang giăng sẵn cho lượt tới!";
                    return false;
                }
                player.setFishingNetActive(true);
                fishingManager.combatMessage = "Đã giăng Lưới Đánh Cá (Cơ hội x2-x4 lượt tới)!";
                break;
            case "Bùa Hoàng Kim":
                if (player.getGoldBuffCharges() > 0) {
                    fishingManager.combatMessage = "Bùa Hoàng Kim vẫn đang có hiệu lực (" + player.getGoldBuffCharges() + " lượt)!";
                    return false;
                }
                player.addGoldBuffCharges(5);
                fishingManager.combatMessage = "Dùng Bùa Hoàng Kim (Buff 5 lượt)!";
                break;
            case "Sách Thông Thái":
                if (player.getExpBuffCharges() > 0) {
                    fishingManager.combatMessage = "Sách Thông Thái vẫn đang có hiệu lực (" + player.getExpBuffCharges() + " lượt)!";
                    return false;
                }
                player.addExpBuffCharges(5);
                fishingManager.combatMessage = "Dùng Sách Thông Thái (Buff 5 lượt)!";
                break;
            case "Mồi Rủi Ro":
                if (player.isRiskyBaitActive()) {
                    fishingManager.combatMessage = "Mồi Rủi Ro đang hoạt động rồi!";
                    return false;
                }
                player.setRiskyBaitActive(true);
                fishingManager.combatMessage = "Dùng Mồi Rủi Ro (Nguy hiểm lượt tới)!";
                break;
            case "Nước Bức Phá":
            case "Nước Bứt Phá":
                if (player.isOverclockPotionActive()) {
                    fishingManager.combatMessage = "Nước Bứt Phá vẫn đang có hiệu lực!";
                    return false;
                }
                player.consumeStamina(-player.getMaxStamina());
                player.setOverclockPotionActive(true);
                fishingManager.combatMessage = "Hồi đầy Thể lực, Giảm 30% Max Tension lượt tới!";
                break;
            case "Thuốc Hồi Stamina":
                player.addStaminaRegen(50f, 10f);
                fishingManager.combatMessage = "Đang hồi 50 Thể lực (10/s)!";
                break;
            case "Bình Xịt Đóng Băng":
                fishingManager.troiBuocTimer = 2.0f;
                fishingManager.combatMessage = "ĐÓNG BĂNG! Cá bị đóng băng hoàn toàn trong 2.0 giây!";
                break;
            case "Trà Thảo Mộc Định Thần":
            case "Trà Thảo Mộc": {
                fishingManager.fishCuongNoTimer = 0f;
                fishingManager.fishButTocTimer = 0f;
                fishingManager.fishQuayBunTimer = 0f;
                fishingManager.fishSongThanTimer = 0f;
                fishingManager.fishGiatDienTimer = 0f;
                fishingManager.fishHutMauTimer = 0f;
                fishingManager.fishPhunMucTimer = 0f;
                fishingManager.fishPhunMucMuTimer = 0f;
                fishingManager.fishSongAmTimer = 0f;
                fishingManager.fishBaoToTimer = 0f;
                fishingManager.combatMessage = "Trà Thảo Mộc: Hóa giải toàn bộ trạng thái đặc biệt của cá!";
                break;
            }
            case "Đá Mài Lưỡi Câu":
                if (player.getWhetstoneCharges() > 0) {
                    fishingManager.combatMessage = "Đá Mài Lưỡi Câu vẫn đang có hiệu lực (" + player.getWhetstoneCharges() + " lượt)!";
                    return false;
                }
                player.addWhetstoneCharges(5);
                fishingManager.combatMessage = "Đá Mài: +25% sát thương kéo cá & cắn câu siêu tốc (5 lượt)!";
                break;
            case "Nam Châm Kho Báu":
                if (player.getTreasureMagnetCharges() > 0) {
                    fishingManager.combatMessage = "Nam Châm Kho Báu vẫn đang có hiệu lực (" + player.getTreasureMagnetCharges() + " lượt)!";
                    return false;
                }
                player.addTreasureMagnetCharges(3);
                fishingManager.combatMessage = "Nam Châm Kho Báu: Tăng 400% cơ hội trúng Cổ Vật & Rương Báu (3 lượt)!";
                break;
            case "Kính Nhìn Xuyên Nước":
                if (player.getSonarCharges() > 0) {
                    fishingManager.combatMessage = "Kính Nhìn Xuyên Nước vẫn đang có hiệu lực (" + player.getSonarCharges() + " lượt)!";
                    return false;
                }
                player.addSonarCharges(3);
                fishingManager.combatMessage = "Kính Nhìn Xuyên Nước: Kích hoạt Thấu thị mặt nước (3 lượt)!";
                break;
            case "Kẹo Trick-or-Treat":
                player.addHalloweenCandyTimer(900f);
                player.addHalloweenCandyCharges(1);
                fishingManager.combatMessage = "Ăn Kẹo Trick-or-Treat: +50% Gặp Cá Halloween trong 15 phút!";
                if (hud != null && floatingTextManager != null) {
                    floatingTextManager.addText("KẸO MA THUẬT! (15 Phút)", 640f, 380f, Color.ORANGE);
                }
                com.fishingrpg.game.systems.AudioManager.getInstance().playSFX("sfx_halloween_candy");
                break;
            case "Đèn Lồng Bí Ngô":
                player.addPumpkinLanternCharges(3);
                fishingManager.combatMessage = "Đèn Lồng Bí Ngô: Quang Minh Trừ Tà (+30% phản ứng Re-Hook 3 lần)!";
                if (hud != null && floatingTextManager != null) {
                    floatingTextManager.addText("ĐÈN LỒNG: +30% PHẢN ỨNG RE-HOOK!", 640f, 380f, Color.GOLD);
                }
                com.fishingrpg.game.systems.AudioManager.getInstance().playSFX("sfx_pumpkin_lantern");
                break;
            case "Độc Dược Ma Quái":
            case "Nước Phù Thủy Ma Quái":
                player.addFreeStaminaSkillCharges(3);
                fishingManager.combatMessage = "Độc Dược Ma Quái: 3 lần dùng kỹ năng tiếp theo tiêu hao 0 Thể lực!";
                if (hud != null && floatingTextManager != null) {
                    floatingTextManager.addText("ĐỘC DƯỢC: 3 KỸ NĂNG 0 THỂ LỰC!", 640f, 380f, Color.PURPLE);
                }
                com.fishingrpg.game.systems.AudioManager.getInstance().playSFX("sfx_ghost_potion");
                break;
            case "Bùa Huyết Nguyệt":
                boolean isCombatNow = (fishingManager.state == com.fishingrpg.game.systems.FishingManager.State.COMBAT
                                    || fishingManager.state == com.fishingrpg.game.systems.FishingManager.State.CASTING
                                    || fishingManager.state == com.fishingrpg.game.systems.FishingManager.State.BITE);
                if (isCombatNow) {
                    player.setBloodMoonTalismanTimer(15f);
                    fishingManager.combatMessage = "Bùa Huyết Nguyệt: +10% Sát thương Chuẩn từ mọi nguồn (15 giây)!";
                    if (hud != null && floatingTextManager != null) {
                        floatingTextManager.addText("+10% SÁT THƯƠNG CHUẨN! (15s)", 640f, 380f, Color.SCARLET);
                    }
                } else {
                    if (!com.fishingrpg.game.systems.EventManager.isHalloweenActive()) {
                        fishingManager.combatMessage = "Sự kiện Halloween chưa diễn ra, không thể triệu hồi Huyết Nguyệt!";
                        com.fishingrpg.game.systems.AudioManager.getInstance().playSFX("sfx_button");
                        return false;
                    }
                    player.setManualBloodMoonTimer(900f);
                    fishingManager.combatMessage = "Kích hoạt Đêm Huyết Nguyệt 15 phút! +20% Vàng, +20% EXP, x2 Cá Halloween!";
                    if (hud != null && floatingTextManager != null) {
                        floatingTextManager.addText("✦ ĐÊM HUYẾT NGUYỆT GIÁNG TRẦN ✦", 640f, 400f, Color.RED);
                    }
                }
                com.fishingrpg.game.systems.AudioManager.getInstance().playSFX("sfx_blood_moon_talisman");
                break;
            case "Nến Hồn Ma Bảo Hộ":
                player.setGhostCandleActive(true);
                fishingManager.combatMessage = "Nến Hồn Ma: Bảo hiểm đứt cước 1 lần! Hồi 50% Tension khi chạm đỉnh!";
                if (hud != null && floatingTextManager != null) {
                    floatingTextManager.addText("NẾN HỒN MA: BẢO VỆ ĐỨT DÂY!", 640f, 380f, Color.CYAN);
                }
                com.fishingrpg.game.systems.AudioManager.getInstance().playSFX("sfx_ghost_candle");
                break;
            case "Rương Ma Quái": {
                int tickets = com.badlogic.gdx.math.MathUtils.random(3, 5);
                int dias = com.badlogic.gdx.math.MathUtils.random(30, 80);
                int candys = com.badlogic.gdx.math.MathUtils.random(20, 30);
                player.addGachaTickets(tickets);
                player.addDiamonds(dias);
                player.addItem("Kẹo Trick-or-Treat", candys);
                fishingManager.combatMessage = "MỞ RƯƠNG MA QUÁI: +" + tickets + " Vé Gacha, +" + dias + " KC, +" + candys + " Kẹo!";
                if (hud != null && floatingTextManager != null) {
                    floatingTextManager.addText("MỞ RƯƠNG: +" + tickets + " VÉ, +" + dias + " KC, +" + candys + " KẸO!", 640f, 400f, Color.GOLD);
                }
                com.fishingrpg.game.systems.AudioManager.getInstance().playSFX("sfx_success");
                break;
            }
            default:
                break; // Vật phẩm không rõ — vẫn tiêu thụ
        }

        com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerItemUseVFX(itemName, 640f, 360f);
        return success;
    }

    private void drawBloodMoonOverlay(com.badlogic.gdx.graphics.glutils.ShapeRenderer sr, float alpha, float animTime) {
        // Red Wine Gothic Sky tint
        sr.setColor(0.35f, 0.04f, 0.08f, 0.38f * alpha);
        sr.rect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        // Blood Moon Orb in top right sky
        float moonX = WORLD_WIDTH - 180f;
        float moonY = WORLD_HEIGHT - 120f;
        float pulse = 0.85f + 0.15f * com.badlogic.gdx.math.MathUtils.sin(animTime * 3f);

        sr.setColor(0.95f, 0.10f, 0.15f, 0.18f * alpha * pulse);
        sr.circle(moonX, moonY, 70f);
        sr.setColor(0.90f, 0.15f, 0.20f, 0.28f * alpha * pulse);
        sr.circle(moonX, moonY, 48f);
        sr.setColor(0.98f, 0.25f, 0.25f, 0.85f * alpha);
        sr.circle(moonX, moonY, 32f);

        // Floating blood embers
        for (int i = 0; i < 15; i++) {
            float ex = (i * 88f + animTime * 20f) % WORLD_WIDTH;
            float ey = (i * 53f + animTime * 35f) % WORLD_HEIGHT;
            sr.setColor(1.0f, 0.35f, 0.15f, 0.5f * alpha * com.badlogic.gdx.math.MathUtils.sin(animTime * 4f + i));
            sr.circle(ex, ey, 2.5f);
        }
    }

    @Override public void resize(int w, int h) {
        if (w <= 0 || h <= 0) return;
        viewport.update(w, h, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {
        // Lưu khi rời màn hình (thay vì save mỗi lần câu)
        com.fishingrpg.game.data.SaveManager.savePlayer(player);
        Gdx.input.setInputProcessor(null);
    }
    @Override public void dispose() {}

    public Viewport getViewport() { return viewport; }
    public OrthographicCamera getCamera() { return camera; }
    public FishingManager getFishingManager() { return fishingManager; }
    public Player getPlayer() { return player; }
    public FishingRPG getGame() { return game; }
    public boolean isNewsUnread() { return patchNotesDialog != null && patchNotesDialog.isUnread(); }
}
