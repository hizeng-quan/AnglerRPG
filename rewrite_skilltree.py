
import re

code = """package com.fishingrpg.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fishingrpg.game.FishingRPG;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.skills.Skill;
import com.fishingrpg.game.skills.SkillTree;

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

    class NodeBounds {
        String id; float x; float y; float w; float h;
        NodeBounds(String id, float x, float y, float w, float h) {
            this.id = id; this.x = x; this.y = y; this.w = w; this.h = h;
        }
    }
    
    private NodeBounds[] nodes = {
        // --- S?c M?nh ---
        new NodeBounds("keo_manh", 80f, 600f, 160f, 60f),
        new NodeBounds("day_thep", 320f, 650f, 160f, 60f),
        new NodeBounds("the_luc_doi_dao", 320f, 550f, 160f, 60f),
        new NodeBounds("co_bap_thep", 560f, 650f, 160f, 60f),
        new NodeBounds("bao_kich", 560f, 550f, 160f, 60f),
        new NodeBounds("pha_giap", 800f, 600f, 160f, 60f),
        // --- Nhanh Nh?n ---
        new NodeBounds("giu_day", 80f, 420f, 160f, 60f),
        new NodeBounds("nhanh_tay", 320f, 470f, 160f, 60f),
        new NodeBounds("hoi_phuc_nhanh", 320f, 370f, 160f, 60f),
        new NodeBounds("phan_xa", 560f, 470f, 160f, 60f),
        new NodeBounds("nhip_nhang", 560f, 370f, 160f, 60f),
        new NodeBounds("luot_day", 800f, 420f, 160f, 60f),
        // --- May M?n ---
        new NodeBounds("chuyen_gia_moi", 80f, 240f, 160f, 60f),
        new NodeBounds("may_man_co_ban", 320f, 290f, 160f, 60f),
        new NodeBounds("nhat_mot", 320f, 190f, 160f, 60f),
        new NodeBounds("ngu_dan_tinh_anh", 560f, 290f, 160f, 60f),
        new NodeBounds("tho_san_kho_bau", 560f, 190f, 160f, 60f),
        new NodeBounds("nem_xa", 800f, 240f, 160f, 60f),
        // --- Bí M?t ---
        new NodeBounds("duong_suc", 80f, 70f, 160f, 60f),
        new NodeBounds("giat_kep", 320f, 70f, 160f, 60f),
        new NodeBounds("mat_dai_bang", 560f, 70f, 160f, 60f),
        new NodeBounds("thuan_hoa", 80f, -10f, 160f, 60f),
        new NodeBounds("trai_tim_dai_duong", 320f, -10f, 160f, 60f),
        new NodeBounds("loi_nguyen_troi_buoc", 560f, -10f, 160f, 60f)
    };
    
    private Rectangle upgradeBtn = new Rectangle(925f, 280f, 150f, 45f);
    private Rectangle equipBtn = new Rectangle(925f, 210f, 150f, 45f);
    
    private Rectangle[] activeSlots = new Rectangle[4];
    private Rectangle[] passiveSlots = new Rectangle[2];

    public SkillTreeScreen(FishingRPG game, Player player, Screen previousScreen) {
        this.game = game;
        this.player = player;
        this.previousScreen = previousScreen;
        this.sr = new ShapeRenderer();
        this.font = game.font;
        this.titleFont = game.titleFont;
        this.layout = new GlyphLayout();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(W, H, camera);
        this.camera.update();
        
        for (int i=0; i<4; i++) {
            activeSlots[i] = new Rectangle(100f + i*90f, -120f, 80f, 80f);
        }
        for (int i=0; i<2; i++) {
            passiveSlots[i] = new Rectangle(500f + i*90f, -120f, 80f, 80f);
        }
    }

    @Override
    public void render(float delta) {
        update();
        if (game.getScreen() != this) return;
        ScreenUtils.clear(0.05f, 0.05f, 0.1f, 1f);

        camera.update();
        sr.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        // Draw Magical Background
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.rect(0, 0, W, H, new Color(0.1f, 0.05f, 0.2f, 1f), new Color(0.1f, 0.05f, 0.2f, 1f), new Color(0.02f, 0.05f, 0.15f, 1f), new Color(0.02f, 0.05f, 0.15f, 1f));
        // Some glowing circles
        sr.setColor(0.3f, 0.1f, 0.5f, 0.2f);
        sr.circle(300, 400, 250);
        sr.circle(800, 600, 300);
        sr.circle(100, 100, 200);
        sr.end();
        
        sr.begin(ShapeRenderer.ShapeType.Filled);
        
        Rectangle clipBounds = new Rectangle(0, 0, 880, 650);
        Rectangle scissors = new Rectangle();
        ScissorStack.calculateScissors(camera, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight(), game.batch.getTransformMatrix(), clipBounds, scissors);
        
        if (ScissorStack.pushScissors(scissors)) {
            drawConnections();
            drawNodes();
            sr.end();
            
            game.batch.begin();
            drawTreeText();
            game.batch.end();
            
            ScissorStack.popScissors();
            sr.begin(ShapeRenderer.ShapeType.Filled);
        }
        
        drawRightPanel();
        drawEquipSlots();
        
        // Top Bar Background
        sr.setColor(0.1f, 0.1f, 0.15f, 0.8f);
        sr.rect(0, H - 70, W, 70);
        sr.setColor(0.3f, 0.2f, 0.5f, 1f);
        sr.rect(0, H - 72, W, 2);
        
        // Back Button
        sr.setColor(0.3f, 0.2f, 0.4f, 1f);
        sr.rect(20, H - 55, 100, 40);
        
        // Level & SP Box
        sr.setColor(0.15f, 0.15f, 0.2f, 1f);
        sr.rect(W - 320, H - 55, 300, 40);
        sr.setColor(0.4f, 0.3f, 0.6f, 1f);
        sr.rect(W - 320, H - 55, 300, 2);
        
        sr.end();
        
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();
        drawUIText();
        game.batch.end();
    }

    private void update() {
        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mouse);

        boolean inTreeArea = mouse.x < 880f && mouse.y < 650f;
        
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
                    selectedSkill = hoveredSkill;
                }
            }
        }
        
        if (selectedSkill != null && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (upgradeBtn.contains(mouse.x, mouse.y)) {
                if (tree.canUpgrade(selectedSkill.id, player.getSkillPoints())) {
                    player.upgradeSkill(selectedSkill.id);
                }
            } else if (equipBtn.contains(mouse.x, mouse.y) && selectedSkill.isUnlocked()) {
                toggleEquip(selectedSkill);
            }
        }
        
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (mouse.x >= 20 && mouse.x <= 120 && mouse.y >= H - 55 && mouse.y <= H - 15) {
                com.fishingrpg.game.data.SaveManager.savePlayer(player);
                game.setScreen(previousScreen);
                dispose();
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            com.fishingrpg.game.data.SaveManager.savePlayer(player);
            game.setScreen(previousScreen);
            dispose();
        }
    }

    private void toggleEquip(Skill s) {
        if (player.isSkillEquipped(s.id)) {
            // Unequip
            if (s.type == Skill.SkillType.ACTIVE) {
                for (int i=0; i<4; i++) if (s.id.equals(player.getEquippedActives()[i])) player.getEquippedActives()[i] = null;
            } else {
                for (int i=0; i<2; i++) if (s.id.equals(player.getEquippedPassives()[i])) player.getEquippedPassives()[i] = null;
            }
        } else {
            // Equip
            if (s.type == Skill.SkillType.ACTIVE) {
                for (int i=0; i<4; i++) {
                    if (player.getEquippedActives()[i] == null) {
                        player.getEquippedActives()[i] = s.id;
                        break;
                    }
                }
            } else {
                for (int i=0; i<2; i++) {
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
        sr.setColor(0.5f, 0.4f, 0.6f, 1f);
        // S?c M?nh
        sr.rectLine(240f + panX, 630f + panY, 320f + panX, 680f + panY, 4f); 
        sr.rectLine(240f + panX, 630f + panY, 320f + panX, 580f + panY, 4f); 
        sr.rectLine(480f + panX, 680f + panY, 560f + panX, 680f + panY, 4f); 
        sr.rectLine(480f + panX, 580f + panY, 560f + panX, 580f + panY, 4f); 
        sr.rectLine(720f + panX, 680f + panY, 800f + panX, 630f + panY, 4f); 
        sr.rectLine(720f + panX, 580f + panY, 800f + panX, 630f + panY, 4f); 
        
        // Nhanh Nh?n
        sr.rectLine(240f + panX, 450f + panY, 320f + panX, 500f + panY, 4f); 
        sr.rectLine(240f + panX, 450f + panY, 320f + panX, 400f + panY, 4f); 
        sr.rectLine(480f + panX, 500f + panY, 560f + panX, 500f + panY, 4f); 
        sr.rectLine(480f + panX, 400f + panY, 560f + panX, 400f + panY, 4f); 
        sr.rectLine(720f + panX, 500f + panY, 800f + panX, 450f + panY, 4f); 
        sr.rectLine(720f + panX, 400f + panY, 800f + panX, 450f + panY, 4f); 
        
        // May M?n
        sr.rectLine(240f + panX, 270f + panY, 320f + panX, 320f + panY, 4f); 
        sr.rectLine(240f + panX, 270f + panY, 320f + panX, 220f + panY, 4f); 
        sr.rectLine(480f + panX, 320f + panY, 560f + panX, 320f + panY, 4f); 
        sr.rectLine(480f + panX, 220f + panY, 560f + panX, 220f + panY, 4f); 
        sr.rectLine(720f + panX, 320f + panY, 800f + panX, 270f + panY, 4f); 
        sr.rectLine(720f + panX, 220f + panY, 800f + panX, 270f + panY, 4f); 
    }

    private void drawNodes() {
        SkillTree tree = player.getSkillTree();
        for (NodeBounds n : nodes) {
            float nx = n.x + panX;
            float ny = n.y + panY;
            Skill s = tree.getSkill(n.id);
            
            // Border
            if (player.isSkillEquipped(s.id)) {
                sr.setColor(1f, 0.8f, 0.2f, 1f); // Gold outline
            } else if (s.isMaxed()) {
                sr.setColor(0.9f, 0.4f, 0.9f, 1f); // Purple outline
            } else if (s.isUnlocked()) {
                sr.setColor(0.4f, 0.6f, 0.9f, 1f); // Blue outline
            } else if (tree.prerequisitesMet(s.id)) {
                sr.setColor(0.5f, 0.5f, 0.5f, 1f); // Gray outline
            } else {
                sr.setColor(0.2f, 0.2f, 0.2f, 1f); // Dark outline
            }
            sr.rect(nx - 2, ny - 2, n.w + 4, n.h + 4);
            
            // Fill
            if (s.isUnlocked()) {
                sr.setColor(0.15f, 0.15f, 0.25f, 0.9f);
            } else {
                sr.setColor(0.1f, 0.1f, 0.15f, 0.8f);
            }
            if (hoveredSkill == s || selectedSkill == s) {
                sr.setColor(0.3f, 0.25f, 0.4f, 0.9f);
            }
            sr.rect(nx, ny, n.w, n.h);
        }
    }

    private void drawRightPanel() {
        // Info Panel background
        sr.setColor(0.12f, 0.1f, 0.18f, 0.9f);
        sr.rect(880f, 100f, 380f, 530f);
        sr.setColor(0.4f, 0.3f, 0.6f, 1f);
        sr.rect(880f, 100f, 2f, 530f); // Left border
        sr.rect(880f, 628f, 380f, 2f); // Top border
        
        if (selectedSkill != null) {
            SkillTree tree = player.getSkillTree();
            
            // Upgrade Button
            if (!selectedSkill.isMaxed() && tree.canUpgrade(selectedSkill.id, player.getSkillPoints())) {
                sr.setColor(0.2f, 0.6f, 0.3f, 1f);
            } else {
                sr.setColor(0.4f, 0.4f, 0.4f, 1f);
            }
            sr.rect(upgradeBtn.x, upgradeBtn.y, upgradeBtn.width, upgradeBtn.height);
            
            // Equip Button
            if (selectedSkill.isUnlocked()) {
                if (player.isSkillEquipped(selectedSkill.id)) {
                    sr.setColor(0.8f, 0.3f, 0.3f, 1f); // THÁO
                } else {
                    sr.setColor(0.2f, 0.4f, 0.8f, 1f); // TRANG B?
                }
                sr.rect(equipBtn.x, equipBtn.y, equipBtn.width, equipBtn.height);
            }
        }
    }

    private void drawEquipSlots() {
        for (int i=0; i<4; i++) {
            sr.setColor(0.3f, 0.2f, 0.4f, 1f);
            sr.rect(activeSlots[i].x - 2, activeSlots[i].y - 2, activeSlots[i].width + 4, activeSlots[i].height + 4);
            sr.setColor(0.1f, 0.1f, 0.15f, 1f);
            sr.rect(activeSlots[i].x, activeSlots[i].y, activeSlots[i].width, activeSlots[i].height);
        }
        for (int i=0; i<2; i++) {
            sr.setColor(0.2f, 0.3f, 0.4f, 1f);
            sr.rect(passiveSlots[i].x - 2, passiveSlots[i].y - 2, passiveSlots[i].width + 4, passiveSlots[i].height + 4);
            sr.setColor(0.1f, 0.1f, 0.15f, 1f);
            sr.rect(passiveSlots[i].x, passiveSlots[i].y, passiveSlots[i].width, passiveSlots[i].height);
        }
    }

    private void drawTreeText() {
        SkillTree tree = player.getSkillTree();
        
        font.setColor(0.6f, 0.5f, 0.8f, 1f);
        font.draw(game.batch, "S?C M?NH", 20f + panX, 700f + panY);
        font.draw(game.batch, "NHANH NH?N", 20f + panX, 520f + panY);
        font.draw(game.batch, "MAY M?N", 20f + panX, 340f + panY);
        font.draw(game.batch, "Ð?C BI?T", 20f + panX, 150f + panY);

        for (NodeBounds n : nodes) {
            float nx = n.x + panX;
            float ny = n.y + panY;
            Skill s = tree.getSkill(n.id);
            String txt = s.name;
            String levelTxt = "(" + s.getLevel() + "/" + s.maxLevel + ")";
            
            font.getData().setScale(0.8f);
            font.setColor(Color.WHITE);
            layout.setText(font, txt);
            font.draw(game.batch, txt, nx + (n.w - layout.width)/2f, ny + n.h - 8f);
            
            font.getData().setScale(0.7f);
            font.setColor(0.8f, 0.8f, 0.8f, 1f);
            layout.setText(font, levelTxt);
            font.draw(game.batch, levelTxt, nx + (n.w - layout.width)/2f, ny + n.h - 26f);
            
            font.setColor(s.type == Skill.SkillType.ACTIVE ? Color.CORAL : Color.SKY);
            String type = s.type.name();
            layout.setText(font, type);
            font.draw(game.batch, type, nx + (n.w - layout.width)/2f, ny + 16f);
            
            font.setColor(Color.WHITE);
            font.getData().setScale(1f);
        }
    }

    private void drawUIText() {
        // Top Bar
        titleFont.setColor(Color.WHITE);
        layout.setText(titleFont, "CÂY K? NANG");
        titleFont.draw(game.batch, "CÂY K? NANG", (W - layout.width)/2f, H - 20f);
        
        font.setColor(Color.WHITE);
        layout.setText(font, "QUAY L?I");
        font.draw(game.batch, "QUAY L?I", 20 + (100 - layout.width)/2f, H - 35f + layout.height/2f);
        
        font.setColor(1f, 0.9f, 0.2f, 1f);
        String spText = "C?p d?: " + player.getLevel() + "  |  SP: " + player.getSkillPoints();
        layout.setText(font, spText);
        font.draw(game.batch, spText, (W - 320) + (300 - layout.width)/2f, H - 35f + layout.height/2f);

        SkillTree tree = player.getSkillTree();
        
        // Equip slots
        font.setColor(Color.CORAL);
        font.draw(game.batch, "ACTIVE (Q, W, E, R)", 100f, -20f);
        font.setColor(Color.SKY);
        font.draw(game.batch, "PASSIVE", 500f, -20f);
        
        font.setColor(Color.WHITE);
        for (int i=0; i<4; i++) {
            String id = player.getEquippedActives()[i];
            if (id != null) {
                String name = tree.getSkill(id).name;
                font.draw(game.batch, name, activeSlots[i].x + 5, activeSlots[i].y + 45f, 70f, 1, true);
            }
        }
        for (int i=0; i<2; i++) {
            String id = player.getEquippedPassives()[i];
            if (id != null) {
                String name = tree.getSkill(id).name;
                font.draw(game.batch, name, passiveSlots[i].x + 5, passiveSlots[i].y + 45f, 70f, 1, true);
            }
        }
        
        // Right Panel Text
        if (selectedSkill != null) {
            float cx = 880f + 380f/2f;
            
            font.setColor(Color.GOLD);
            layout.setText(font, selectedSkill.name + " (Lv " + selectedSkill.getLevel() + ")");
            font.draw(game.batch, selectedSkill.name + " (Lv " + selectedSkill.getLevel() + ")", cx - layout.width/2f, 590f);
            
            font.setColor(Color.WHITE);
            String desc = selectedSkill.description;
            if (selectedSkill.isUnlocked()) {
                desc += "\n\n[Hi?n t?i]: " + selectedSkill.getCurrentLevelEffectString();
            }
            if (!selectedSkill.isMaxed()) {
                desc += "\n[C?p ti?p theo]: " + selectedSkill.getNextLevelEffectString();
            }
            font.draw(game.batch, desc, 900f, 540f, 340f, -1, true);
            
            if (!selectedSkill.isMaxed()) {
                String cost = "Chi phí: " + selectedSkill.getUpgradeCost() + " SP";
                layout.setText(font, cost);
                font.draw(game.batch, cost, cx - layout.width/2f, upgradeBtn.y + 70f);
                
                layout.setText(font, "NÂNG C?P");
                font.draw(game.batch, "NÂNG C?P", upgradeBtn.x + (upgradeBtn.width - layout.width)/2f, upgradeBtn.y + 28f);
            } else {
                layout.setText(font, "Ðã d?t c?p t?i da.");
                font.draw(game.batch, "Ðã d?t c?p t?i da.", cx - layout.width/2f, upgradeBtn.y + 70f);
            }
            
            if (selectedSkill.isUnlocked()) {
                String btnText = player.isSkillEquipped(selectedSkill.id) ? "THÁO" : "TRANG B?";
                layout.setText(font, btnText);
                font.draw(game.batch, btnText, equipBtn.x + (equipBtn.width - layout.width)/2f, equipBtn.y + 28f);
            }
        }
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void show() {} @Override public void pause() {}
    @Override public void resume() {} @Override public void hide() {}
    @Override public void dispose() { sr.dispose(); }
}
"""
with open("core/src/main/java/com/fishingrpg/game/screens/SkillTreeScreen.java", "w", encoding="utf-8") as f:
    f.write(code)

