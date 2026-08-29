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

    private Player player;
    private GameHUD hud;
    public FishingManager fishingManager;
    private int framesToSkip = 5;
    private float timer = 0f;
    // Cached Vector3 để tránh allocate mời frame
    private final com.badlogic.gdx.math.Vector3 mousePos = new com.badlogic.gdx.math.Vector3();

    public PlayScreen(FishingRPG game) {
        this.game = game;
        this.shapeRenderer = new ShapeRenderer();
        this.font = game.font;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        this.player = game.player;
        this.fishingManager = new FishingManager(player);
        this.hud = new GameHUD(shapeRenderer, font, this);
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
    }

    @Override
    public void render(float delta) {
        if (framesToSkip > 0) {
            framesToSkip--;
        }
        update(delta);
        if (game.getScreen() != this) return;

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        if (fishingManager.state == FishingManager.State.IDLE) {
            ScreenUtils.clear(new Color(0.1f, 0.4f, 0.6f, 1f));
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
                shapeRenderer.setColor(0.1f, 0.3f, 0.5f, 1f);
                shapeRenderer.rect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
                shapeRenderer.setColor(0.05f, 0.2f, 0.4f, 1f);
                shapeRenderer.rect(0, 0, WORLD_WIDTH, WORLD_HEIGHT / 2f);
                
                shapeRenderer.setColor(0.2f, 0.15f, 0.1f, 0.9f);
                shapeRenderer.circle(80, 0, 60);
                shapeRenderer.circle(1150, -10, 80);
                
                shapeRenderer.setColor(0.1f, 0.4f, 0.2f, 0.6f);
                for(int i=0; i<10; i++) {
                    float sx = 80 + i * 110;
                    float px = sx, py = 0;
                    for(int j=0; j<5; j++) {
                        float nx = px + 10f * com.badlogic.gdx.math.MathUtils.sin(timer * 2f + i + j);
                        float ny = py + 30;
                        shapeRenderer.rectLine(px, py, nx, ny, 6f - j);
                        px = nx; py = ny;
                    }
                }
                
                shapeRenderer.setColor(0.2f, 0.5f, 0.7f, 0.2f);
                shapeRenderer.triangle(300, WORLD_HEIGHT, 500, WORLD_HEIGHT, 200, 0);
                shapeRenderer.triangle(800, WORLD_HEIGHT, 1000, WORLD_HEIGHT, WORLD_WIDTH - 200, 0);
                
                shapeRenderer.setColor(0.5f, 0.9f, 1f, 0.4f);
                for(int i=0; i<20; i++) {
                    float bx = (i * 83 + timer * 15f) % WORLD_WIDTH;
                    float by = ((i * 137 + timer * 65f) % (WORLD_HEIGHT + 100)) - 50;
                    shapeRenderer.circle(bx + com.badlogic.gdx.math.MathUtils.sin(timer + i)*20f, by, 3f + (i%4));
                }
            } else if (mapId.equals("bai_bien")) {
                // Beach: bright blue water, sand at bottom, sun rays
                shapeRenderer.setColor(0.15f, 0.45f, 0.65f, 1f);
                shapeRenderer.rect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
                
                shapeRenderer.setColor(0.7f, 0.6f, 0.3f, 1f);
                shapeRenderer.rect(0, 0, WORLD_WIDTH, 80);
                
                shapeRenderer.setColor(0.6f, 0.8f, 1f, 0.3f);
                for (int i=0; i<4; i++) {
                    shapeRenderer.triangle(100 + i*300, WORLD_HEIGHT, 300 + i*300, WORLD_HEIGHT, 200 + i*200, 0);
                }
                
                shapeRenderer.setColor(0.6f, 0.9f, 1f, 0.5f);
                for(int i=0; i<30; i++) {
                    float bx = (i * 73 + timer * 20f) % WORLD_WIDTH;
                    float by = ((i * 127 + timer * 80f) % (WORLD_HEIGHT + 100)) - 50;
                    shapeRenderer.circle(bx + com.badlogic.gdx.math.MathUtils.sin(timer*1.5f + i)*15f, by, 2f + (i%5));
                }
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

            hud.drawShapes(player, fishingManager.currentFish);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

            game.batch.begin();
            hud.drawText(game.batch, player, fishingManager.currentCatch, fishingManager.combatMessage, fishingManager.resultLine);
            game.batch.end();
        }

        if (messageTimer > 0 && message != null) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            com.fishingrpg.game.ui.NotificationUI.drawNotification(shapeRenderer, game.batch, game.font, message, messageTimer, 2f, WORLD_WIDTH, WORLD_HEIGHT);
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    private void update(float delta) {
        if (messageTimer > 0) messageTimer -= delta;
        player.update(delta);
        if (player.justFilledStamina) {
            player.justFilledStamina = false;
            message = "Thể lực đã hồi đầy!";
            messageTimer = 2f;
        }
        
        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mousePos);
        hud.updateHover(mousePos, fishingManager.state);

        String clickedItem = hud.getClickedInventoryItem(mousePos, fishingManager.state);
        boolean itemClicked = false;
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && clickedItem != null) {
            useItem(clickedItem);
            itemClicked = true;
        }

        boolean mouseJustClicked = framesToSkip <= 0 && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && !itemClicked;
        boolean actClicked = mouseJustClicked && hud.isCastHovered();
        
        if (fishingManager.state == FishingManager.State.IDLE && actClicked) {
            // Open MapScreen instead of casting directly
            game.setScreen(new com.fishingrpg.game.screens.MapScreen(game, this));
            return;
        }
        
        if (fishingManager.state == FishingManager.State.CASTING || fishingManager.state == FishingManager.State.BITE) {
            actClicked = actClicked || mouseJustClicked;
        }

        // Quick slots 1-9
        int i = 0;
        for (Map.Entry<String, Integer> entry : player.getInventory().entrySet()) {
            String name = entry.getKey();
            if (entry.getValue() <= 0 || name.equals("Vàng")) continue;
            int keyCode = Input.Keys.NUM_1 + i;
            if (i < 9 && Gdx.input.isKeyJustPressed(keyCode)) {
                useItem(name);
                break;
            }
            i++;
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
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
        }

        boolean menuClicked = framesToSkip <= 0 && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && hud.isMenuClicked(mousePos, fishingManager.state);
        boolean isSpaceDown = framesToSkip <= 0 && (Gdx.input.isKeyPressed(Input.Keys.SPACE) || (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && hud.isCangHovered()));
        boolean isSpaceJust = framesToSkip <= 0 && (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && hud.isCangHovered()));

        boolean[] activeSkillInputs = new boolean[4];
        if (framesToSkip <= 0) {
            activeSkillInputs[0] = Gdx.input.isKeyJustPressed(Input.Keys.Q) || (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && hud.isActiveSlotHovered(0));
            activeSkillInputs[1] = Gdx.input.isKeyJustPressed(Input.Keys.W) || (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && hud.isActiveSlotHovered(1));
            activeSkillInputs[2] = Gdx.input.isKeyJustPressed(Input.Keys.E) || (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && hud.isActiveSlotHovered(2));
            activeSkillInputs[3] = Gdx.input.isKeyJustPressed(Input.Keys.R) || (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && hud.isActiveSlotHovered(3));
        }

        boolean actClose = framesToSkip <= 0 && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && hud.isCloseHovered();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || menuClicked) {
            if (fishingManager.state != FishingManager.State.IDLE) {
                fishingManager.state = FishingManager.State.IDLE;
            } else {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
            return;
        }

        fishingManager.update(delta, actClicked, isSpaceJust, isSpaceDown, activeSkillInputs, actClose);
    }

    private void useItem(String itemName) {
        if (!player.hasItem(itemName)) return;

        com.fishingrpg.game.entities.ItemData item = com.fishingrpg.game.data.ItemDatabase.getItem(itemName);
        if (item == null) {
            if (itemName.startsWith("Sách: ")) handleSkillBook(itemName);
            return;
        }

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

        if (consume) player.consumeItem(itemName);
    }

    /**
     * Xử lý tiêu thụ Sách kỹ năng (không có trong ItemDatabase).
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
        if (s != null && s.getLevel() == 0) {
            s.setCurrentLevel(1);
            player.consumeItem(itemName);
            fishingManager.combatMessage = "Học thành công: " + s.name + "!";
            player.applySkillBonuses();
        } else {
            fishingManager.combatMessage = "Đã học kỹ năng này rồi!";
        }
    }

    /**
     * Áp dụng hiệu ứng của vật phẩm.
     * @return true nếu vật phẩm nên bị tiêu thụ, false nếu không mất.
     */
    private boolean applyItemEffect(String itemName) {
        float thucDuongBonus = player.isSkillEquipped("no_bung") ? player.getSkillTree().getNoBungBonus() : 0f;
        float healMult = 1f + (thucDuongBonus / 100f);

        switch (itemName) {
            case "Bánh Mì": {
                int heal = (int)(25f * healMult);
                player.consumeStamina(-heal);
                fishingManager.combatMessage = "Dùng Bánh Mì (+" + heal + " Thể lực)!";
                return true;
            }
            case "Nước Tăng Lực": {
                int heal = (int)(50f * healMult);
                player.consumeStamina(-heal);
                fishingManager.combatMessage = "Dùng Nước Tăng Lực (+" + heal + " Thể lực)!";
                return true;
            }
            case "Dây Câu Tốt":
                player.decreaseTension(20f);
                fishingManager.combatMessage = "Dùng Dây Câu Tốt (-20 Tension)!";
                return true;
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
                return consumed;
            }
            case "Lưới Đánh Cá":
                player.setFishingNetActive(true);
                fishingManager.combatMessage = "Đã giăng Lưới Đánh Cá (Cơ hội x2-x4 lượt tới)!";
                return true;
            case "Bùa Hoàng Kim":
                player.addGoldBuffCharges(5);
                fishingManager.combatMessage = "Dùng Bùa Hoàng Kim (Buff 5 lượt)!";
                return true;
            case "Sách Thông Thái":
                player.addExpBuffCharges(5);
                fishingManager.combatMessage = "Dùng Sách Thông Thái (Buff 5 lượt)!";
                return true;
            case "Mồi Rủi Ro":
                player.setRiskyBaitActive(true);
                fishingManager.combatMessage = "Dùng Mồi Rủi Ro (Nguy hiểm lượt tới)!";
                return true;
            case "Nước Bức Phá":
                player.consumeStamina(-player.getMaxStamina());
                player.setOverclockPotionActive(true);
                fishingManager.combatMessage = "Hồi đầy Thể lực, Giảm 30% Max Tension lượt tới!";
                return true;
            case "Thuốc Hồi Stamina":
                player.addStaminaRegen(50f, 10f);
                fishingManager.combatMessage = "Đang hồi 50 Thể lực (10/s)!";
                return true;
            default:
                return true; // Vật phẩm không rõ — vẫn tiêu thụ
        }
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {
        // Lưu khi rời màn hình (thay vì save mỗi lần câu)
        com.fishingrpg.game.data.SaveManager.savePlayer(player);
    }
    @Override public void dispose() { shapeRenderer.dispose(); }

    public Viewport getViewport() { return viewport; }
    public FishingManager getFishingManager() { return fishingManager; }
    public Player getPlayer() { return player; }
}
