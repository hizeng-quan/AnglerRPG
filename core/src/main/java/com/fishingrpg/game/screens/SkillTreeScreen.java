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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fishingrpg.game.FishingRPG;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.skills.Skill;
import com.fishingrpg.game.skills.SkillTree;
import com.fishingrpg.game.systems.AudioManager;
import com.fishingrpg.game.ui.NotificationUI;

public class SkillTreeScreen implements Screen {

    private final FishingRPG game;
    private final Player player;
    private final Screen previousScreen;

    private ShapeRenderer sr;
    private BitmapFont font, titleFont;
    private GlyphLayout layout;
    private OrthographicCamera camera;
    private Viewport viewport;

    private static final float W = 1280f;
    private static final float H = 720f;

    private Skill hoveredSkill = null;
    private Skill selectedSkill = null;

    private float panX = 0f;
    private float panY = 0f;
    private String message = null;
    private float messageTimer = 0f;
    private boolean hoverBack = false;
    private float animTimer = 0f;

    class NodeBounds {
        String id; float x; float y; float w; float h; int branch; // 0: Str, 1: Agi, 2: Luck, 3: Secret
        NodeBounds(String id, float x, float y, float w, float h, int branch) {
            this.id = id; this.x = x; this.y = y; this.w = w; this.h = h; this.branch = branch;
        }
    }
    
    private NodeBounds[] nodes = {
        // --- Sức Mạnh (Branch 0: Flame Orange) ---
        new NodeBounds("keo_manh", 80f, 650f, 170f, 65f, 0),
        new NodeBounds("day_thep", 320f, 750f, 170f, 65f, 0),
        new NodeBounds("co_bap_thep", 560f, 750f, 170f, 65f, 0),
        new NodeBounds("bao_kich", 320f, 650f, 170f, 65f, 0),
        new NodeBounds("pha_giap", 560f, 650f, 170f, 65f, 0),
        new NodeBounds("the_luc_doi_dao", 320f, 550f, 170f, 65f, 0),
        new NodeBounds("ben_bi", 560f, 550f, 170f, 65f, 0),
        new NodeBounds("no_bung", 800f, 550f, 170f, 65f, 0),
        // --- Nhanh Nhẹn (Branch 1: Cyan Wind) ---
        new NodeBounds("giu_day", 80f, 420f, 170f, 65f, 1),
        new NodeBounds("nhanh_tay", 320f, 470f, 170f, 65f, 1),
        new NodeBounds("hoi_phuc_nhanh", 320f, 370f, 170f, 65f, 1),
        new NodeBounds("phan_xa", 560f, 470f, 170f, 65f, 1),
        new NodeBounds("nhip_nhang", 560f, 370f, 170f, 65f, 1),
        new NodeBounds("luot_day", 800f, 420f, 170f, 65f, 1),
        // --- May Mắn (Branch 2: Emerald Clover) ---
        new NodeBounds("chuyen_gia_moi", 80f, 240f, 170f, 65f, 2),
        new NodeBounds("may_man_co_ban", 320f, 290f, 170f, 65f, 2),
        new NodeBounds("nhat_mot", 320f, 190f, 170f, 65f, 2),
        new NodeBounds("ngu_dan_tinh_anh", 560f, 290f, 170f, 65f, 2),
        new NodeBounds("tho_san_kho_bau", 560f, 190f, 170f, 65f, 2),
        new NodeBounds("nem_xa", 800f, 240f, 170f, 65f, 2),
        // --- Bí Mật (Branch 3: Arcane Purple) ---
        new NodeBounds("duong_suc", 80f, 60f, 170f, 65f, 3),
        new NodeBounds("giat_kep", 320f, 60f, 170f, 65f, 3),
        new NodeBounds("mat_dai_bang", 560f, 60f, 170f, 65f, 3),
        new NodeBounds("thuan_hoa", 80f, -20f, 170f, 65f, 3),
        new NodeBounds("trai_tim_dai_duong", 320f, -20f, 170f, 65f, 3),
        new NodeBounds("loi_nguyen_troi_buoc", 560f, -20f, 170f, 65f, 3)
    };
    
    private Rectangle upgradeBtn = new Rectangle(925f, 220f, 320f, 48f);
    private Rectangle equipBtn = new Rectangle(925f, 155f, 320f, 48f);
    
    private Rectangle[] activeSlots = new Rectangle[4];
    private Rectangle[] passiveSlots = new Rectangle[2];

    public SkillTreeScreen(FishingRPG game, Player player, Screen previousScreen) {
        this.game = game;
        this.player = player;
        this.previousScreen = previousScreen;
        this.sr = new ShapeRenderer();
        this.font = game.font;
        this.titleFont = game.titleFont != null ? game.titleFont : game.font;
        this.layout = new GlyphLayout();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(W, H, camera);
        this.camera.position.set(W / 2f, H / 2f, 0);
        
        for (int i = 0; i < 4; i++) {
            activeSlots[i] = new Rectangle(65f + i * 110f, 20f, 85f, 85f);
        }
        for (int i = 0; i < 2; i++) {
            passiveSlots[i] = new Rectangle(580f + i * 120f, 20f, 85f, 85f);
        }
    }

    @Override
    public void render(float delta) {
        animTimer += delta;
        player.update(delta);
        if (player.justFilledStamina) {
            player.justFilledStamina = false;
            message = "Thể lực đã hồi đầy!";
            messageTimer = 2f;
        }
        if (messageTimer > 0) messageTimer -= delta;

        update();
        if (game.getScreen() != this) return;
        ScreenUtils.clear(0.04f, 0.05f, 0.10f, 1f);

        camera.update();
        sr.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mouse);

        hoverBack = (mouse.x >= 25 && mouse.x <= 155 && mouse.y >= H - 62 && mouse.y <= H - 20);

        // Cosmic Nebula Tree Background
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.rect(0, 0, W, H,
            new Color(0.03f, 0.04f, 0.09f, 1f), new Color(0.03f, 0.04f, 0.09f, 1f),
            new Color(0.09f, 0.06f, 0.16f, 1f), new Color(0.09f, 0.06f, 0.16f, 1f)
        );

        // Ambient glowing nebula orbs
        sr.setColor(0.4f, 0.15f, 0.5f, 0.12f);
        sr.circle(350, 420, 240);
        sr.setColor(0.15f, 0.35f, 0.6f, 0.12f);
        sr.circle(750, 520, 260);
        sr.setColor(0.15f, 0.55f, 0.35f, 0.10f);
        sr.circle(450, 200, 200);

        sr.end();
        
        // Tree area clipping
        sr.begin(ShapeRenderer.ShapeType.Filled);
        Rectangle clipBounds = new Rectangle(0, 140, 880, 508);
        Rectangle scissors = new Rectangle();
        ScissorStack.calculateScissors(camera, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight(), game.batch.getTransformMatrix(), clipBounds, scissors);
        
        if (ScissorStack.pushScissors(scissors)) {
            drawConnections();
            drawNodes(mouse);
            sr.end();
            
            game.batch.begin();
            drawTreeText();
            game.batch.end();
            
            ScissorStack.popScissors();
            sr.begin(ShapeRenderer.ShapeType.Filled);
        }
        
        // Bottom Loadout Bar Background
        sr.setColor(0.06f, 0.08f, 0.14f, 0.96f);
        sr.rect(0, 0, 880, 140);
        sr.setColor(0.3f, 0.5f, 0.75f, 0.8f);
        sr.rect(0, 139, 880, 2);
        
        drawRightPanel(mouse);
        drawEquipSlots(mouse);
        
        // Top Header Bar
        sr.setColor(0.06f, 0.08f, 0.14f, 0.95f);
        sr.rect(0, H - 72, W, 72);
        sr.setColor(0.35f, 0.65f, 0.95f, 0.7f);
        sr.rect(0, H - 74, W, 2);
        
        // Back Button
        sr.setColor(hoverBack ? new Color(0.25f, 0.35f, 0.50f, 0.95f) : new Color(0.12f, 0.18f, 0.26f, 0.9f));
        drawRoundedRect(sr, 25, H - 62, 130, 42, 8);
        sr.setColor(hoverBack ? new Color(0.6f, 0.85f, 1f, 1f) : new Color(0.3f, 0.5f, 0.7f, 0.7f));
        drawRectOutline(sr, 25, H - 62, 130, 42, 1.5f);
        
        // Level & SP Box (Top Right)
        float spBoxW = 280f;
        float spBoxH = 42f;
        float spBoxX = W - spBoxW - 25f;
        float spBoxY = H - 62f;
        sr.setColor(0.10f, 0.14f, 0.22f, 0.95f);
        drawRoundedRect(sr, spBoxX, spBoxY, spBoxW, spBoxH, 8);
        sr.setColor(1f, 0.82f, 0.35f, 0.9f);
        drawRectOutline(sr, spBoxX, spBoxY, spBoxW, spBoxH, 1.5f);
        
        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();
        drawUIText();

        if (messageTimer > 0 && message != null) {
            game.batch.end();
            NotificationUI.drawNotification(sr, game.batch, font, message, messageTimer, 2f, W, H);
            game.batch.begin();
        }
        
        game.batch.end();
    }

    private void update() {
        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mouse);

        boolean inTreeArea = mouse.x < 880f && mouse.y > 140f && mouse.y < 648f;
        
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && inTreeArea) {
            if (!Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                float dx = Gdx.input.getDeltaX() * (viewport.getWorldWidth() / Gdx.graphics.getWidth());
                float dy = -Gdx.input.getDeltaY() * (viewport.getWorldHeight() / Gdx.graphics.getHeight());
                panX += dx;
                panY += dy;
                
                if (panX > 100f) panX = 100f;
                if (panX < -600f) panX = -600f;
                if (panY > 400f) panY = 400f;
                if (panY < -200f) panY = -200f;
            }
        }

        hoveredSkill = null;
        SkillTree tree = player.getSkillTree();
        for (NodeBounds n : nodes) {
            float nx = n.x + panX;
            float ny = n.y + panY;
            if (mouse.x >= nx && mouse.x <= nx + n.w && mouse.y >= ny && mouse.y <= ny + n.h) {
                if (inTreeArea) hoveredSkill = tree.getSkill(n.id);
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && inTreeArea) {
                    AudioManager.getInstance().playSFX("sfx_button");
                    selectedSkill = hoveredSkill;
                }
            }
        }
        
        if (selectedSkill != null && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (upgradeBtn.contains(mouse.x, mouse.y)) {
                if (tree.canUpgrade(selectedSkill.id, player.getSkillPoints())) {
                    AudioManager.getInstance().playSFX("sfx_button");
                    player.upgradeSkill(selectedSkill.id);
                }
            } else if (equipBtn.contains(mouse.x, mouse.y) && selectedSkill.isUnlocked()) {
                AudioManager.getInstance().playSFX("sfx_button");
                toggleEquip(selectedSkill);
            }
        }
        
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (mouse.x >= 25 && mouse.x <= 155 && mouse.y >= H - 62 && mouse.y <= H - 20) {
                AudioManager.getInstance().playSFX("sfx_button");
                com.fishingrpg.game.data.SaveManager.savePlayer(player);
                game.setScreen(previousScreen);
                dispose();
                return;
            }
            
            // Check active slots click to unequip
            for (int i = 0; i < 4; i++) {
                if (activeSlots[i].contains(mouse.x, mouse.y)) {
                    if (player.getEquippedActives()[i] != null) {
                        AudioManager.getInstance().playSFX("sfx_button");
                        player.getEquippedActives()[i] = null;
                        player.applySkillBonuses();
                    }
                }
            }
            
            // Check passive slots click to unequip
            for (int i = 0; i < 2; i++) {
                if (passiveSlots[i].contains(mouse.x, mouse.y)) {
                    if (player.getEquippedPassives()[i] != null) {
                        AudioManager.getInstance().playSFX("sfx_button");
                        player.getEquippedPassives()[i] = null;
                        player.applySkillBonuses();
                    }
                }
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            AudioManager.getInstance().playSFX("sfx_button");
            com.fishingrpg.game.data.SaveManager.savePlayer(player);
            game.setScreen(previousScreen);
            dispose();
        }
    }

    private void toggleEquip(Skill s) {
        if (player.isSkillEquipped(s.id)) {
            // Unequip
            if (s.type == Skill.SkillType.ACTIVE) {
                for (int i = 0; i < 4; i++) if (s.id.equals(player.getEquippedActives()[i])) player.getEquippedActives()[i] = null;
            } else {
                for (int i = 0; i < 2; i++) if (s.id.equals(player.getEquippedPassives()[i])) player.getEquippedPassives()[i] = null;
            }
        } else {
            // Equip
            if (s.type == Skill.SkillType.ACTIVE) {
                for (int i = 0; i < 4; i++) {
                    if (player.getEquippedActives()[i] == null) {
                        player.getEquippedActives()[i] = s.id;
                        break;
                    }
                }
            } else {
                for (int i = 0; i < 2; i++) {
                    if (player.getEquippedPassives()[i] == null) {
                        player.getEquippedPassives()[i] = s.id;
                        break;
                    }
                }
            }
        }
        player.applySkillBonuses();
    }

    private void drawConnections() {
        // Glowing animated energy lines
        float pulse = 0.6f + 0.4f * MathUtils.sin(animTimer * 2.5f);
        sr.setColor(0.35f * pulse, 0.55f * pulse, 0.85f * pulse, 0.7f);

        // Sức Mạnh
        drawLine(250f + panX, 682f + panY, 320f + panX, 782f + panY);
        drawLine(250f + panX, 682f + panY, 320f + panX, 682f + panY);
        drawLine(250f + panX, 682f + panY, 320f + panX, 582f + panY);
        drawLine(490f + panX, 782f + panY, 560f + panX, 782f + panY);
        drawLine(490f + panX, 682f + panY, 560f + panX, 682f + panY);
        drawLine(490f + panX, 582f + panY, 560f + panX, 582f + panY);
        drawLine(730f + panX, 582f + panY, 800f + panX, 582f + panY);
        
        // Nhanh Nhẹn
        drawLine(250f + panX, 452f + panY, 320f + panX, 502f + panY); 
        drawLine(250f + panX, 452f + panY, 320f + panX, 402f + panY); 
        drawLine(490f + panX, 502f + panY, 560f + panX, 502f + panY); 
        drawLine(490f + panX, 402f + panY, 560f + panX, 402f + panY); 
        drawLine(730f + panX, 502f + panY, 800f + panX, 452f + panY); 
        drawLine(730f + panX, 402f + panY, 800f + panX, 452f + panY); 
        
        // May Mắn
        drawLine(250f + panX, 272f + panY, 320f + panX, 322f + panY); 
        drawLine(250f + panX, 272f + panY, 320f + panX, 222f + panY); 
        drawLine(490f + panX, 322f + panY, 560f + panX, 322f + panY); 
        drawLine(490f + panX, 222f + panY, 560f + panX, 222f + panY); 
        drawLine(730f + panX, 322f + panY, 800f + panX, 272f + panY); 
        drawLine(730f + panX, 222f + panY, 800f + panX, 272f + panY); 
    }

    private void drawLine(float x1, float y1, float x2, float y2) {
        sr.rectLine(x1, y1, x2, y2, 3.5f);
    }

    private void drawNodes(Vector3 mouse) {
        SkillTree tree = player.getSkillTree();
        for (NodeBounds n : nodes) {
            float nx = n.x + panX;
            float ny = n.y + panY;
            Skill s = tree.getSkill(n.id);
            boolean isEq = player.isSkillEquipped(s.id);
            boolean isSel = (selectedSkill == s);
            boolean isHov = (hoveredSkill == s);

            // Node Shadow
            sr.setColor(0f, 0f, 0f, 0.45f);
            drawRoundedRect(sr, nx + 4, ny - 4, n.w, n.h, 10);

            // Node Background
            if (s.isUnlocked()) {
                sr.setColor(isSel || isHov ? new Color(0.20f, 0.28f, 0.44f, 0.95f) : new Color(0.12f, 0.16f, 0.26f, 0.92f));
            } else {
                sr.setColor(isSel || isHov ? new Color(0.16f, 0.18f, 0.24f, 0.9f) : new Color(0.08f, 0.10f, 0.16f, 0.85f));
            }
            drawRoundedRect(sr, nx, ny, n.w, n.h, 10);

            // Node Border
            if (isEq) {
                float p = 0.8f + 0.2f * MathUtils.sin(animTimer * 4f);
                sr.setColor(1f * p, 0.85f * p, 0.25f * p, 1f);
                drawRectOutline(sr, nx, ny, n.w, n.h, 2.5f);
            } else if (s.isMaxed()) {
                sr.setColor(0.9f, 0.45f, 0.95f, 0.9f);
                drawRectOutline(sr, nx, ny, n.w, n.h, 2f);
            } else if (s.isUnlocked()) {
                sr.setColor(0.35f, 0.75f, 1f, 0.85f);
                drawRectOutline(sr, nx, ny, n.w, n.h, 1.8f);
            } else if (tree.prerequisitesMet(s.id)) {
                sr.setColor(0.5f, 0.55f, 0.65f, 0.7f);
                drawRectOutline(sr, nx, ny, n.w, n.h, 1.2f);
            } else {
                sr.setColor(0.25f, 0.28f, 0.35f, 0.5f);
                drawRectOutline(sr, nx, ny, n.w, n.h, 1f);
            }

            // Level Progress Mini Bar at bottom of node
            float pbW = n.w - 24f;
            float pbH = 4f;
            float pbX = nx + 12f;
            float pbY = ny + 8f;
            sr.setColor(0.06f, 0.08f, 0.12f, 0.8f);
            drawRoundedRect(sr, pbX, pbY, pbW, pbH, 2);
            float ratio = (float)s.getLevel() / s.maxLevel;
            if (ratio > 0) {
                sr.setColor(s.isMaxed() ? new Color(0.9f, 0.4f, 1f, 1f) : new Color(0.3f, 0.85f, 0.5f, 1f));
                drawRoundedRect(sr, pbX, pbY, pbW * ratio, pbH, 2);
            }
        }
    }

    private void drawRightPanel(Vector3 mouse) {
        float pX = 880f;
        float pY = 0f;
        float pW = W - pX;
        float pH = H - 74f;

        // Right Inspector Base
        sr.setColor(0.08f, 0.11f, 0.18f, 0.98f);
        sr.rect(pX, pY, pW, pH);
        sr.setColor(0.25f, 0.45f, 0.65f, 0.7f);
        sr.rect(pX, pY, 2f, pH);

        if (selectedSkill != null) {
            SkillTree tree = player.getSkillTree();
            
            // Skill icon box in inspector
            sr.setColor(0.12f, 0.16f, 0.26f, 0.95f);
            drawRoundedRect(sr, pX + 25f, pH - 110f, 75f, 75f, 8);
            sr.setColor(selectedSkill.isUnlocked() ? new Color(0.35f, 0.75f, 1f, 0.9f) : new Color(0.4f, 0.45f, 0.55f, 0.6f));
            drawRectOutline(sr, pX + 25f, pH - 110f, 75f, 75f, 1.5f);

            // Upgrade Button
            boolean canUp = !selectedSkill.isMaxed() && tree.canUpgrade(selectedSkill.id, player.getSkillPoints());
            boolean hoverUp = upgradeBtn.contains(mouse.x, mouse.y);
            if (canUp) {
                sr.setColor(hoverUp ? new Color(0.25f, 0.70f, 0.42f, 1f) : new Color(0.18f, 0.52f, 0.30f, 0.95f));
                drawRoundedRect(sr, upgradeBtn.x, upgradeBtn.y, upgradeBtn.width, upgradeBtn.height, 8);
                sr.setColor(hoverUp ? new Color(0.6f, 1f, 0.75f, 1f) : new Color(0.35f, 0.85f, 0.5f, 0.8f));
                drawRectOutline(sr, upgradeBtn.x, upgradeBtn.y, upgradeBtn.width, upgradeBtn.height, 1.8f);
            } else {
                sr.setColor(0.14f, 0.16f, 0.22f, 0.9f);
                drawRoundedRect(sr, upgradeBtn.x, upgradeBtn.y, upgradeBtn.width, upgradeBtn.height, 8);
                sr.setColor(0.28f, 0.32f, 0.4f, 0.6f);
                drawRectOutline(sr, upgradeBtn.x, upgradeBtn.y, upgradeBtn.width, upgradeBtn.height, 1.2f);
            }
            
            // Equip Button
            if (selectedSkill.isUnlocked()) {
                boolean isEq = player.isSkillEquipped(selectedSkill.id);
                boolean hoverEq = equipBtn.contains(mouse.x, mouse.y);
                if (isEq) {
                    sr.setColor(hoverEq ? new Color(0.65f, 0.25f, 0.25f, 1f) : new Color(0.45f, 0.18f, 0.18f, 0.95f));
                    drawRoundedRect(sr, equipBtn.x, equipBtn.y, equipBtn.width, equipBtn.height, 8);
                    sr.setColor(hoverEq ? new Color(1f, 0.65f, 0.65f, 1f) : new Color(0.8f, 0.35f, 0.35f, 0.8f));
                    drawRectOutline(sr, equipBtn.x, equipBtn.y, equipBtn.width, equipBtn.height, 1.8f);
                } else {
                    sr.setColor(hoverEq ? new Color(0.22f, 0.55f, 0.85f, 1f) : new Color(0.15f, 0.38f, 0.62f, 0.95f));
                    drawRoundedRect(sr, equipBtn.x, equipBtn.y, equipBtn.width, equipBtn.height, 8);
                    sr.setColor(hoverEq ? new Color(0.6f, 0.9f, 1f, 1f) : new Color(0.3f, 0.7f, 0.95f, 0.8f));
                    drawRectOutline(sr, equipBtn.x, equipBtn.y, equipBtn.width, equipBtn.height, 1.8f);
                }
            }
        }
    }

    private void drawEquipSlots(Vector3 mouse) {
        // Frame Active
        sr.setColor(0.10f, 0.13f, 0.20f, 0.9f);
        drawRoundedRect(sr, 40f, 10f, 470f, 120f, 8);
        sr.setColor(0.85f, 0.55f, 0.25f, 0.8f);
        drawRectOutline(sr, 40f, 10f, 470f, 120f, 1.5f);
        
        // Frame Passive
        sr.setColor(0.10f, 0.13f, 0.20f, 0.9f);
        drawRoundedRect(sr, 530f, 10f, 320f, 120f, 8);
        sr.setColor(0.35f, 0.75f, 0.95f, 0.8f);
        drawRectOutline(sr, 530f, 10f, 320f, 120f, 1.5f);

        // 4 Active Slots (Q, W, E, R)
        for (int i = 0; i < 4; i++) {
            boolean hoverSlot = activeSlots[i].contains(mouse.x, mouse.y);
            sr.setColor(hoverSlot ? new Color(0.18f, 0.24f, 0.35f, 0.95f) : new Color(0.08f, 0.10f, 0.16f, 0.9f));
            drawRoundedRect(sr, activeSlots[i].x, activeSlots[i].y, activeSlots[i].width, activeSlots[i].height, 8);
            sr.setColor(hoverSlot ? new Color(1f, 0.85f, 0.4f, 1f) : new Color(0.55f, 0.4f, 0.25f, 0.7f));
            drawRectOutline(sr, activeSlots[i].x, activeSlots[i].y, activeSlots[i].width, activeSlots[i].height, 1.5f);
        }

        // 2 Passive Slots
        for (int i = 0; i < 2; i++) {
            boolean hoverSlot = passiveSlots[i].contains(mouse.x, mouse.y);
            sr.setColor(hoverSlot ? new Color(0.18f, 0.24f, 0.35f, 0.95f) : new Color(0.08f, 0.10f, 0.16f, 0.9f));
            drawRoundedRect(sr, passiveSlots[i].x, passiveSlots[i].y, passiveSlots[i].width, passiveSlots[i].height, 8);
            sr.setColor(hoverSlot ? new Color(0.6f, 0.9f, 1f, 1f) : new Color(0.25f, 0.55f, 0.75f, 0.7f));
            drawRectOutline(sr, passiveSlots[i].x, passiveSlots[i].y, passiveSlots[i].width, passiveSlots[i].height, 1.5f);
        }
    }

    private void drawTreeText() {
        SkillTree tree = player.getSkillTree();
        
        // Branch Headers
        font.setColor(1f, 0.65f, 0.3f, 1f);
        font.draw(game.batch, "❖ SỨC MẠNH", 20f + panX, 740f + panY);
        font.setColor(0.35f, 0.85f, 1f, 1f);
        font.draw(game.batch, "❖ NHANH NHẸN", 20f + panX, 520f + panY);
        font.setColor(0.4f, 1f, 0.6f, 1f);
        font.draw(game.batch, "❖ MAY MẮN", 20f + panX, 340f + panY);
        font.setColor(0.9f, 0.5f, 1f, 1f);
        font.draw(game.batch, "❖ BÍ KÍP CỔ", 20f + panX, 150f + panY);

        for (NodeBounds n : nodes) {
            float nx = n.x + panX;
            float ny = n.y + panY;
            Skill s = tree.getSkill(n.id);
            
            // Skill Name
            font.getData().setScale(0.85f);
            font.setColor(s.isUnlocked() ? Color.WHITE : Color.LIGHT_GRAY);
            layout.setText(font, s.name);
            font.draw(game.batch, s.name, nx + (n.w - layout.width) / 2f, ny + n.h - 10f);
            
            // Level ratio
            font.getData().setScale(0.75f);
            font.setColor(s.isMaxed() ? new Color(1f, 0.85f, 0.4f, 1f) : new Color(0.7f, 0.8f, 0.9f, 0.85f));
            String lvlStr = s.isMaxed() ? "MAX (Cấp " + s.maxLevel + ")" : "Cấp " + s.getLevel() + "/" + s.maxLevel;
            layout.setText(font, lvlStr);
            font.draw(game.batch, lvlStr, nx + (n.w - layout.width) / 2f, ny + n.h - 28f);
            
            // Skill Type Pill
            font.setColor(s.type == Skill.SkillType.ACTIVE ? new Color(1f, 0.6f, 0.3f, 1f) : new Color(0.4f, 0.85f, 1f, 1f));
            String typeStr = s.type == Skill.SkillType.ACTIVE ? "[CHỦ ĐỘNG]" : "[BỊ ĐỘNG]";
            layout.setText(font, typeStr);
            font.draw(game.batch, typeStr, nx + (n.w - layout.width) / 2f, ny + 20f);
            
            font.getData().setScale(1f);
        }
    }

    private void drawUIText() {
        // Header Bar
        titleFont.setColor(0.95f, 0.85f, 0.45f, 1f);
        layout.setText(titleFont, "CÂY THIÊN PHÚ NGƯ PHỦ");
        titleFont.draw(game.batch, layout, W / 2f - layout.width / 2f, H - 22f);

        font.setColor(0.65f, 0.8f, 0.95f, 0.85f);
        layout.setText(font, "Phát triển thiên phú câu cá và gán chiêu thức giằng co phím Q, W, E, R");
        font.draw(game.batch, layout, W / 2f - layout.width / 2f, H - 48f);
        
        // Back Button Text
        font.setColor(hoverBack ? Color.WHITE : new Color(0.8f, 0.9f, 1f, 0.9f));
        layout.setText(font, "← QUAY LẠI");
        font.draw(game.batch, layout, 25f + (130f - layout.width) / 2f, H - 36f);
        
        // Level & SP Box Text
        float spBoxW = 280f;
        float spBoxX = W - spBoxW - 25f;
        float spBoxY = H - 62f;
        font.setColor(1f, 0.85f, 0.3f, 1f);
        String spText = "Cấp " + player.getLevel() + " • SP: " + player.getSkillPoints() + " Điểm";
        layout.setText(font, spText);
        font.draw(game.batch, layout, spBoxX + (spBoxW - layout.width) / 2f, spBoxY + 26f);

        SkillTree tree = player.getSkillTree();
        
        // Active Slots Headers
        font.setColor(1f, 0.7f, 0.35f, 1f);
        font.draw(game.batch, "KỸ NĂNG CHỦ ĐỘNG (BẤM Q, W, E, R)", 50f, 120f);
        
        font.setColor(0.35f, 0.85f, 1f, 1f);
        font.draw(game.batch, "NỘI TẠI BỊ ĐỘNG", 540f, 120f);
        
        // Slot Keycap Badges
        String[] keys = {"Q", "W", "E", "R"};
        for (int i = 0; i < 4; i++) {
            // Key label
            font.setColor(1f, 0.85f, 0.4f, 1f);
            font.draw(game.batch, "[" + keys[i] + "]", activeSlots[i].x + 8f, activeSlots[i].y + activeSlots[i].height - 8f);

            String id = player.getEquippedActives()[i];
            if (id != null) {
                String name = tree.getSkill(id).name;
                font.setColor(Color.WHITE);
                font.getData().setScale(0.85f);
                layout.setText(font, name, Color.WHITE, activeSlots[i].width - 10f, Align.center, true);
                font.draw(game.batch, name, activeSlots[i].x + 5f, activeSlots[i].y + 40f + layout.height / 2f, activeSlots[i].width - 10f, Align.center, true);
                font.getData().setScale(1f);
            } else {
                font.setColor(0.45f, 0.5f, 0.6f, 0.7f);
                layout.setText(font, "(Trống)");
                font.draw(game.batch, layout, activeSlots[i].x + (activeSlots[i].width - layout.width) / 2f, activeSlots[i].y + 35f);
            }
        }

        for (int i = 0; i < 2; i++) {
            font.setColor(0.4f, 0.8f, 1f, 1f);
            font.draw(game.batch, "[P" + (i + 1) + "]", passiveSlots[i].x + 8f, passiveSlots[i].y + passiveSlots[i].height - 8f);

            String id = player.getEquippedPassives()[i];
            if (id != null) {
                String name = tree.getSkill(id).name;
                font.setColor(Color.WHITE);
                font.getData().setScale(0.85f);
                layout.setText(font, name, Color.WHITE, passiveSlots[i].width - 10f, Align.center, true);
                font.draw(game.batch, name, passiveSlots[i].x + 5f, passiveSlots[i].y + 40f + layout.height / 2f, passiveSlots[i].width - 10f, Align.center, true);
                font.getData().setScale(1f);
            } else {
                font.setColor(0.45f, 0.5f, 0.6f, 0.7f);
                layout.setText(font, "(Trống)");
                font.draw(game.batch, layout, passiveSlots[i].x + (passiveSlots[i].width - layout.width) / 2f, passiveSlots[i].y + 35f);
            }
        }
        
        // Right Panel Text
        if (selectedSkill != null) {
            float pX = 880f;
            float pW = W - pX;
            float pH = H - 74f;

            // Name (Auto-scale to fit available panel width)
            titleFont.setColor(0.95f, 0.85f, 0.4f, 1f);
            layout.setText(titleFont, selectedSkill.name);
            float maxSkillNameW = pW - 135f;
            if (layout.width > maxSkillNameW) {
                titleFont.getData().setScale(maxSkillNameW / layout.width);
            }
            titleFont.draw(game.batch, selectedSkill.name, pX + 115f, pH - 45f);
            titleFont.getData().setScale(1f);

            // Level & Type Tag
            font.setColor(selectedSkill.type == Skill.SkillType.ACTIVE ? new Color(1f, 0.6f, 0.3f, 1f) : new Color(0.35f, 0.85f, 1f, 1f));
            String typeBadge = (selectedSkill.type == Skill.SkillType.ACTIVE ? "Chủ Động" : "Bị Động") + " • Cấp " + selectedSkill.getLevel() + "/" + selectedSkill.maxLevel;
            font.draw(game.batch, typeBadge, pX + 115f, pH - 78f);

            // Description
            font.setColor(0.85f, 0.9f, 0.95f, 1f);
            font.draw(game.batch, selectedSkill.description, pX + 25f, pH - 135f, pW - 50f, Align.left, true);

            // Current effect
            float curY = pH - 240f;
            if (selectedSkill.isUnlocked()) {
                font.setColor(0.4f, 1f, 0.65f, 1f);
                font.draw(game.batch, "❖ Hiệu ứng hiện tại:", pX + 25f, curY);
                font.setColor(Color.WHITE);
                font.draw(game.batch, selectedSkill.getCurrentLevelEffectString(), pX + 25f, curY - 25f, pW - 50f, Align.left, true);
                curY -= 65f;
            }

            // Next level effect
            if (!selectedSkill.isMaxed()) {
                font.setColor(0.4f, 0.85f, 1f, 1f);
                font.draw(game.batch, "❖ Cấp tiếp theo:", pX + 25f, curY);
                font.setColor(Color.WHITE);
                font.draw(game.batch, selectedSkill.getNextLevelEffectString(), pX + 25f, curY - 25f, pW - 50f, Align.left, true);
            }

            // Upgrade Button Text
            if (!selectedSkill.isMaxed()) {
                String cost = (selectedSkill.isUnlocked() ? "NÂNG CẤP (" : "MỞ KHÓA (") + selectedSkill.getUpgradeCost() + " SP)";
                font.setColor(Color.WHITE);
                layout.setText(font, cost);
                font.draw(game.batch, layout, upgradeBtn.x + (upgradeBtn.width - layout.width) / 2f, upgradeBtn.y + 30f);
            } else {
                font.setColor(0.9f, 0.5f, 1f, 1f);
                layout.setText(font, "★ ĐÃ ĐẠT CẤP TỐI ĐA ★");
                font.draw(game.batch, layout, upgradeBtn.x + (upgradeBtn.width - layout.width) / 2f, upgradeBtn.y + 30f);
            }

            // Equip Button Text
            if (selectedSkill.isUnlocked()) {
                boolean isEq = player.isSkillEquipped(selectedSkill.id);
                font.setColor(Color.WHITE);
                String eqText = isEq ? "THÁO KHỎI THANH KỸ NĂNG" : "GÁN VÀO THANH KỸ NĂNG";
                font.getData().setScale(0.88f);
                layout.setText(font, eqText);
                font.draw(game.batch, layout, equipBtn.x + (equipBtn.width - layout.width) / 2f, equipBtn.y + 30f);
                font.getData().setScale(1f);
            }
        } else {
            font.setColor(0.5f, 0.6f, 0.7f, 0.8f);
            String noSkillMsg = "Chọn một kỹ năng trên cây thiên phú để xem thông tin chi tiết và nâng cấp";
            font.draw(game.batch, noSkillMsg, 880f + 25f, H / 2f, 350f, Align.center, true);
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
    @Override public void show() {
        AudioManager.getInstance().playBGM("bgm_menu");
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { sr.dispose(); }
}
