package com.fishingrpg.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
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
    private InputProcessor previousInputProcessor;

    private static final float W = 1280f;
    private static final float H = 720f;

    private Skill hoveredSkill = null;
    private Skill selectedSkill = null;

    private float panX = 0f;
    private float panY = -350f; // Center Branch 0 initially
    private String message = null;
    private float messageTimer = 0f;
    private boolean hoverBack = false;
    private float animTimer = 0f;

    // Cosmic Starfield
    private static final int STAR_COUNT = 70;
    private final float[] starX = new float[STAR_COUNT];
    private final float[] starY = new float[STAR_COUNT];
    private final float[] starSize = new float[STAR_COUNT];
    private final float[] starSpeed = new float[STAR_COUNT];
    private final float[] starPhase = new float[STAR_COUNT];

    class NodeBounds {
        String id; float x; float y; float w; float h; int branch; // 0: Str, 1: Agi, 2: Luck, 3: Secret
        NodeBounds(String id, float x, float y, float w, float h, int branch) {
            this.id = id; this.x = x; this.y = y; this.w = w; this.h = h; this.branch = branch;
        }
    }
    
    // Spaced out coordinates with 118px+ gap between all 4 branches
    private final NodeBounds[] nodes = {
        // --- Sức Mạnh (Branch 0: Flame Orange) ---
        new NodeBounds("keo_manh", 80f, 810f, 185f, 82f, 0),
        new NodeBounds("day_thep", 320f, 920f, 185f, 82f, 0),
        new NodeBounds("co_bap_thep", 560f, 920f, 185f, 82f, 0),
        new NodeBounds("bao_kich", 320f, 810f, 185f, 82f, 0),
        new NodeBounds("pha_giap", 560f, 810f, 185f, 82f, 0),
        new NodeBounds("the_luc_doi_dao", 320f, 700f, 185f, 82f, 0),
        new NodeBounds("ben_bi", 560f, 700f, 185f, 82f, 0),
        new NodeBounds("no_bung", 800f, 700f, 185f, 82f, 0),
        // --- Nhanh Nhẹn (Branch 1: Cyan Wind) ---
        new NodeBounds("giu_day", 80f, 390f, 185f, 82f, 1),
        new NodeBounds("nhanh_tay", 320f, 500f, 185f, 82f, 1),
        new NodeBounds("hoi_phuc_nhanh", 320f, 280f, 185f, 82f, 1),
        new NodeBounds("phan_xa", 560f, 500f, 185f, 82f, 1),
        new NodeBounds("nhip_nhang", 560f, 280f, 185f, 82f, 1),
        new NodeBounds("luot_day", 800f, 390f, 185f, 82f, 1),
        // --- May Mắn (Branch 2: Emerald Clover) ---
        new NodeBounds("chuyen_gia_moi", 80f, -30f, 185f, 82f, 2),
        new NodeBounds("may_man_co_ban", 320f, 80f, 185f, 82f, 2),
        new NodeBounds("nhat_mot", 320f, -140f, 185f, 82f, 2),
        new NodeBounds("ngu_dan_tinh_anh", 560f, 80f, 185f, 82f, 2),
        new NodeBounds("tho_san_kho_bau", 560f, -140f, 185f, 82f, 2),
        new NodeBounds("nem_xa", 800f, -30f, 185f, 82f, 2),
        // --- Bí Mật (Branch 3: Arcane Purple) ---
        new NodeBounds("duong_suc", 80f, -340f, 185f, 82f, 3),
        new NodeBounds("giat_kep", 320f, -340f, 185f, 82f, 3),
        new NodeBounds("mat_dai_bang", 560f, -340f, 185f, 82f, 3),
        new NodeBounds("thuan_hoa", 80f, -460f, 185f, 82f, 3),
        new NodeBounds("trai_tim_dai_duong", 320f, -460f, 185f, 82f, 3),
        new NodeBounds("loi_nguyen_troi_buoc", 560f, -460f, 185f, 82f, 3)
    };
    
    private final Rectangle upgradeBtn = new Rectangle(925f, 220f, 320f, 48f);
    private final Rectangle equipBtn = new Rectangle(925f, 155f, 320f, 48f);
    
    private final Rectangle[] activeSlots = new Rectangle[4];
    private final Rectangle[] passiveSlots = new Rectangle[2];

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

        // Initialize Starfield
        for (int i = 0; i < STAR_COUNT; i++) {
            starX[i] = MathUtils.random(0f, W);
            starY[i] = MathUtils.random(0f, H);
            starSize[i] = MathUtils.random(1.2f, 2.6f);
            starSpeed[i] = MathUtils.random(1.5f, 4f);
            starPhase[i] = MathUtils.random(0f, MathUtils.PI2);
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
        ScreenUtils.clear(0.03f, 0.04f, 0.08f, 1f);

        camera.update();
        sr.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mouse);

        hoverBack = (mouse.x >= 25 && mouse.x <= 155 && mouse.y >= H - 62 && mouse.y <= H - 20);

        // ==========================================
        // UPGRADED COSMIC NEBULA & STARFIELD BACKGROUND
        // ==========================================
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.rect(0, 0, W, H,
            new Color(0.02f, 0.03f, 0.07f, 1f), new Color(0.02f, 0.03f, 0.07f, 1f),
            new Color(0.06f, 0.04f, 0.12f, 1f), new Color(0.06f, 0.04f, 0.12f, 1f)
        );

        // Rotating Sacred Astrolabe Rune Rings
        float astroCX = 440f;
        float astroCY = 390f;
        sr.setColor(0.25f, 0.45f, 0.85f, 0.07f);
        sr.circle(astroCX, astroCY, 260f);
        sr.setColor(0.025f, 0.035f, 0.08f, 1f);
        sr.circle(astroCX, astroCY, 256f);
        sr.setColor(0.35f, 0.55f, 0.95f, 0.08f);
        sr.circle(astroCX, astroCY, 190f);
        sr.setColor(0.025f, 0.035f, 0.08f, 1f);
        sr.circle(astroCX, astroCY, 187f);

        // Astrolabe rotating radial spokes
        for (int i = 0; i < 8; i++) {
            float angle = animTimer * 4f + i * 45f;
            float rad = angle * MathUtils.degreesToRadians;
            float cos = MathUtils.cos(rad);
            float sin = MathUtils.sin(rad);
            sr.setColor(0.4f, 0.6f, 1f, 0.06f);
            sr.rectLine(astroCX + cos * 50f, astroCY + sin * 50f, astroCX + cos * 255f, astroCY + sin * 255f, 1.5f);
        }

        // 4 Realm-Specific Glowing Ambient Nebulae linked to branch camera
        // Branch 0 (Sức Mạnh - Flame Ember Nebula)
        sr.setColor(0.85f, 0.32f, 0.10f, 0.11f);
        sr.circle(440f + panX * 0.3f, 820f + panY, 280f);
        sr.setColor(1.0f, 0.55f, 0.15f, 0.06f);
        sr.circle(320f + panX * 0.3f, 850f + panY, 190f);

        // Branch 1 (Nhanh Nhẹn - Cyan Tempest Nebula)
        sr.setColor(0.12f, 0.55f, 0.85f, 0.12f);
        sr.circle(440f + panX * 0.3f, 400f + panY, 270f);
        sr.setColor(0.25f, 0.80f, 1.0f, 0.07f);
        sr.circle(540f + panX * 0.3f, 380f + panY, 180f);

        // Branch 2 (May Mắn - Emerald Jade Nebula)
        sr.setColor(0.10f, 0.70f, 0.40f, 0.10f);
        sr.circle(440f + panX * 0.3f, -30f + panY, 270f);
        sr.setColor(0.35f, 0.90f, 0.55f, 0.06f);
        sr.circle(360f + panX * 0.3f, -10f + panY, 180f);

        // Branch 3 (Bí Kíp Cổ - Arcane Mystic Violet Nebula)
        sr.setColor(0.60f, 0.20f, 0.85f, 0.12f);
        sr.circle(440f + panX * 0.3f, -400f + panY, 270f);
        sr.setColor(0.80f, 0.35f, 1.0f, 0.07f);
        sr.circle(520f + panX * 0.3f, -380f + panY, 180f);

        // Drifting twinkling starfield with subtle parallax
        for (int i = 0; i < STAR_COUNT; i++) {
            float tw = 0.35f + 0.65f * MathUtils.sin(animTimer * starSpeed[i] + starPhase[i]);
            float sx = (starX[i] + animTimer * 2f + panX * 0.12f) % W;
            if (sx < 0) sx += W;
            float sy = (starY[i] + panY * 0.12f) % H;
            if (sy < 0) sy += H;
            sr.setColor(0.75f * tw, 0.88f * tw, 1f * tw, tw * 0.85f);
            sr.circle(sx, sy, starSize[i]);
        }

        sr.end();
        
        // Tree area clipping
        sr.begin(ShapeRenderer.ShapeType.Filled);
        Rectangle clipBounds = new Rectangle(0, 140, 880, 508);
        Rectangle scissors = new Rectangle();
        ScissorStack.calculateScissors(camera, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight(), game.batch.getTransformMatrix(), clipBounds, scissors);
        
        if (ScissorStack.pushScissors(scissors)) {
            drawBranchDecorations();
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

    private void clampPan() {
        if (panX > 60f) panX = 60f;
        if (panX < -260f) panX = -260f;
        if (panY > 640f) panY = 640f;
        if (panY < -420f) panY = -420f;
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
                clampPan();
            }
        }

        // Keyboard navigation
        float dt = Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            panY -= 450f * dt;
            clampPan();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            panY += 450f * dt;
            clampPan();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            panX += 450f * dt;
            clampPan();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            panX -= 450f * dt;
            clampPan();
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

    private NodeBounds getNode(String id) {
        for (NodeBounds n : nodes) {
            if (n.id.equals(id)) return n;
        }
        return null;
    }

    private void connectNodes(String id1, String id2) {
        NodeBounds a = getNode(id1);
        NodeBounds b = getNode(id2);
        if (a != null && b != null) {
            drawLine(a.x + a.w + panX, a.y + a.h / 2f + panY, b.x + panX, b.y + b.h / 2f + panY);
        }
    }

    private void drawConnections() {
        // Glowing animated energy lines
        float pulse = 0.6f + 0.4f * MathUtils.sin(animTimer * 2.5f);
        sr.setColor(0.35f * pulse, 0.55f * pulse, 0.85f * pulse, 0.7f);

        // Sức Mạnh
        connectNodes("keo_manh", "day_thep");
        connectNodes("keo_manh", "bao_kich");
        connectNodes("keo_manh", "the_luc_doi_dao");
        connectNodes("day_thep", "co_bap_thep");
        connectNodes("bao_kich", "pha_giap");
        connectNodes("the_luc_doi_dao", "ben_bi");
        connectNodes("ben_bi", "no_bung");
        
        // Nhanh Nhẹn
        connectNodes("giu_day", "nhanh_tay");
        connectNodes("giu_day", "hoi_phuc_nhanh");
        connectNodes("nhanh_tay", "phan_xa");
        connectNodes("hoi_phuc_nhanh", "nhip_nhang");
        connectNodes("phan_xa", "luot_day");
        connectNodes("nhip_nhang", "luot_day");
        
        // May Mắn
        connectNodes("chuyen_gia_moi", "may_man_co_ban");
        connectNodes("chuyen_gia_moi", "nhat_mot");
        connectNodes("may_man_co_ban", "ngu_dan_tinh_anh");
        connectNodes("nhat_mot", "tho_san_kho_bau");
        connectNodes("ngu_dan_tinh_anh", "nem_xa");
        connectNodes("tho_san_kho_bau", "nem_xa");

        // Bí Kíp Cổ
        connectNodes("duong_suc", "giat_kep");
        connectNodes("giat_kep", "mat_dai_bang");
        connectNodes("thuan_hoa", "trai_tim_dai_duong");
        connectNodes("trai_tim_dai_duong", "loi_nguyen_troi_buoc");
    }

    private void drawLine(float x1, float y1, float x2, float y2) {
        sr.rectLine(x1, y1, x2, y2, 3.5f);
    }

    private void drawBranchDecorations() {
        float[] headerYs = {1040f, 620f, 200f, -220f};
        Color[] headerColors = {
            new Color(1f, 0.6f, 0.25f, 1f),
            new Color(0.25f, 0.8f, 1f, 1f),
            new Color(0.3f, 0.95f, 0.55f, 1f),
            new Color(0.85f, 0.45f, 1f, 1f)
        };
        
        for (int i = 0; i < 4; i++) {
            float hy = headerYs[i] + panY;
            Color col = headerColors[i];
            
            // Background banner plate
            sr.setColor(col.r, col.g, col.b, 0.12f);
            drawRoundedRect(sr, 20f + panX, hy - 20f, 850f, 28f, 6);
            
            // Glowing accent marker
            sr.setColor(col.r, col.g, col.b, 0.9f);
            sr.rect(20f + panX, hy - 20f, 4f, 28f);
            
            // Horizontal glowing divider line
            sr.setColor(col.r, col.g, col.b, 0.35f);
            sr.rectLine(24f + panX, hy - 20f, 870f + panX, hy - 20f, 1.5f);
        }
    }

    private Color getBranchColor(int branch) {
        switch (branch) {
            case 0: return new Color(1f, 0.6f, 0.25f, 1f);
            case 1: return new Color(0.25f, 0.8f, 1f, 1f);
            case 2: return new Color(0.3f, 0.95f, 0.55f, 1f);
            case 3: return new Color(0.85f, 0.45f, 1f, 1f);
            default: return new Color(0.7f, 0.8f, 0.9f, 1f);
        }
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

            // Draw Custom Unique Skill Icon Badge on left of node
            Color branchCol = getBranchColor(n.branch);
            drawSkillIcon(sr, s.id, nx + 28f, ny + 43f, 18f, branchCol, s.isUnlocked());

            // Level Progress Mini Bar at bottom of node
            float pbW = n.w - 24f;
            float pbH = 4.5f;
            float pbX = nx + 12f;
            float pbY = ny + 6f;
            sr.setColor(0.06f, 0.08f, 0.12f, 0.85f);
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

            // Draw enlarged skill icon in inspector preview box
            Color skColor = selectedSkill.type == Skill.SkillType.ACTIVE ? new Color(1f, 0.65f, 0.3f, 1f) : new Color(0.35f, 0.85f, 1f, 1f);
            drawSkillIcon(sr, selectedSkill.id, pX + 25f + 37.5f, pH - 110f + 37.5f, 26f, skColor, selectedSkill.isUnlocked());

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
        SkillTree tree = player.getSkillTree();

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

            // Render Skill Icon if equipped
            String id = player.getEquippedActives()[i];
            if (id != null) {
                Skill sk = tree.getSkill(id);
                if (sk != null) {
                    drawSkillIcon(sr, id, activeSlots[i].x + activeSlots[i].width / 2f, activeSlots[i].y + 49f, 17f, new Color(1f, 0.65f, 0.3f, 1f), true);
                }
            }
        }

        // 2 Passive Slots
        for (int i = 0; i < 2; i++) {
            boolean hoverSlot = passiveSlots[i].contains(mouse.x, mouse.y);
            sr.setColor(hoverSlot ? new Color(0.18f, 0.24f, 0.35f, 0.95f) : new Color(0.08f, 0.10f, 0.16f, 0.9f));
            drawRoundedRect(sr, passiveSlots[i].x, passiveSlots[i].y, passiveSlots[i].width, passiveSlots[i].height, 8);
            sr.setColor(hoverSlot ? new Color(0.6f, 0.9f, 1f, 1f) : new Color(0.25f, 0.55f, 0.75f, 0.7f));
            drawRectOutline(sr, passiveSlots[i].x, passiveSlots[i].y, passiveSlots[i].width, passiveSlots[i].height, 1.5f);

            // Render Skill Icon if equipped
            String id = player.getEquippedPassives()[i];
            if (id != null) {
                Skill sk = tree.getSkill(id);
                if (sk != null) {
                    drawSkillIcon(sr, id, passiveSlots[i].x + passiveSlots[i].width / 2f, passiveSlots[i].y + 49f, 17f, new Color(0.35f, 0.85f, 1f, 1f), true);
                }
            }
        }
    }

    private void drawTreeText() {
        SkillTree tree = player.getSkillTree();
        
        // Branch Domain Headers (With clean spacing above nodes)
        font.getData().setScale(0.92f);
        font.setColor(1f, 0.65f, 0.3f, 1f);
        font.draw(game.batch, "❖ NHÁNH SỨC MẠNH — CƯỜNG HÓA LỰC KÉO & SÁT THƯƠNG", 32f + panX, 1040f + panY);

        font.setColor(0.35f, 0.85f, 1f, 1f);
        font.draw(game.batch, "❖ NHÁNH NHANH NHẸN — KIỂM SOÁT DÂY & HỒI PHỤC", 32f + panX, 620f + panY);

        font.setColor(0.4f, 1f, 0.6f, 1f);
        font.draw(game.batch, "❖ NHÁNH MAY MẮN — CƠ HỘI ĐỘ HIẾM & KHO BÁU", 32f + panX, 200f + panY);

        font.setColor(0.9f, 0.5f, 1f, 1f);
        font.draw(game.batch, "❖ NHÁNH BÍ KÍP CỔ — BÍ THUẬT NGƯ DÂN THƯỢNG CỔ", 32f + panX, -220f + panY);
        font.getData().setScale(1f);

        for (NodeBounds n : nodes) {
            float nx = n.x + panX;
            float ny = n.y + panY;
            Skill s = tree.getSkill(n.id);
            
            float textStartX = nx + 54f;
            float maxTextW = n.w - 58f; // ~127px
            
            // Skill Name
            font.getData().setScale(0.80f);
            layout.setText(font, s.name);
            if (layout.width > maxTextW) {
                font.getData().setScale(0.80f * (maxTextW / layout.width));
                layout.setText(font, s.name);
            }
            font.setColor(s.isUnlocked() ? Color.WHITE : Color.LIGHT_GRAY);
            font.draw(game.batch, s.name, textStartX + (maxTextW - layout.width) / 2f, ny + n.h - 13f);
            
            // Level ratio
            font.getData().setScale(0.72f);
            font.setColor(s.isMaxed() ? new Color(1f, 0.85f, 0.4f, 1f) : new Color(0.7f, 0.8f, 0.9f, 0.85f));
            String lvlStr = s.isMaxed() ? "MAX (Cấp " + s.maxLevel + ")" : "Cấp " + s.getLevel() + "/" + s.maxLevel;
            layout.setText(font, lvlStr);
            font.draw(game.batch, lvlStr, textStartX + (maxTextW - layout.width) / 2f, ny + n.h - 31f);
            
            // Skill Type Pill: shifted up to ny + 28f (17.5px above progress bar top at ny + 10.5f)
            font.getData().setScale(0.68f);
            font.setColor(s.type == Skill.SkillType.ACTIVE ? new Color(1f, 0.6f, 0.3f, 1f) : new Color(0.4f, 0.85f, 1f, 1f));
            String typeStr = s.type == Skill.SkillType.ACTIVE ? "[CHỦ ĐỘNG]" : "[BỊ ĐỘNG]";
            layout.setText(font, typeStr);
            font.draw(game.batch, typeStr, textStartX + (maxTextW - layout.width) / 2f, ny + 28f);
            
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
        
        // Slot Keycap Badges, Icons & Skill Names
        String[] keys = {"Q", "W", "E", "R"};
        for (int i = 0; i < 4; i++) {
            // Key label
            font.setColor(1f, 0.85f, 0.4f, 1f);
            font.getData().setScale(0.75f);
            font.draw(game.batch, "[" + keys[i] + "]", activeSlots[i].x + 6f, activeSlots[i].y + activeSlots[i].height - 6f);
            font.getData().setScale(1f);

            String id = player.getEquippedActives()[i];
            if (id != null) {
                Skill sk = tree.getSkill(id);
                String name = sk != null ? sk.name : id;
                font.setColor(Color.WHITE);
                font.getData().setScale(0.70f);
                layout.setText(font, name);
                float maxW = activeSlots[i].width - 8f;
                if (layout.width > maxW) {
                    font.getData().setScale(0.70f * (maxW / layout.width));
                    layout.setText(font, name);
                }
                font.draw(game.batch, name, activeSlots[i].x + (activeSlots[i].width - layout.width) / 2f, activeSlots[i].y + 17f);
                font.getData().setScale(1f);
            } else {
                font.setColor(0.45f, 0.5f, 0.6f, 0.7f);
                font.getData().setScale(0.78f);
                layout.setText(font, "(Trống)");
                font.draw(game.batch, layout, activeSlots[i].x + (activeSlots[i].width - layout.width) / 2f, activeSlots[i].y + 42f);
                font.getData().setScale(1f);
            }
        }

        for (int i = 0; i < 2; i++) {
            font.setColor(0.4f, 0.8f, 1f, 1f);
            font.getData().setScale(0.75f);
            font.draw(game.batch, "[P" + (i + 1) + "]", passiveSlots[i].x + 6f, passiveSlots[i].y + passiveSlots[i].height - 6f);
            font.getData().setScale(1f);

            String id = player.getEquippedPassives()[i];
            if (id != null) {
                Skill sk = tree.getSkill(id);
                String name = sk != null ? sk.name : id;
                font.setColor(Color.WHITE);
                font.getData().setScale(0.70f);
                layout.setText(font, name);
                float maxW = passiveSlots[i].width - 8f;
                if (layout.width > maxW) {
                    font.getData().setScale(0.70f * (maxW / layout.width));
                    layout.setText(font, name);
                }
                font.draw(game.batch, name, passiveSlots[i].x + (passiveSlots[i].width - layout.width) / 2f, passiveSlots[i].y + 17f);
                font.getData().setScale(1f);
            } else {
                font.setColor(0.45f, 0.5f, 0.6f, 0.7f);
                font.getData().setScale(0.78f);
                layout.setText(font, "(Trống)");
                font.draw(game.batch, layout, passiveSlots[i].x + (passiveSlots[i].width - layout.width) / 2f, passiveSlots[i].y + 42f);
                font.getData().setScale(1f);
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

    /**
     * Renders a distinct, handmade vector icon for every single skill in the game.
     */
    private void drawSkillIcon(ShapeRenderer sr, String id, float cx, float cy, float r, Color baseColor, boolean unlocked) {
        float s = r / 20f;

        // Base circular medallion backplate with depth
        sr.setColor(0.06f, 0.08f, 0.13f, 0.98f);
        sr.circle(cx, cy, r);

        // Outer beveled metallic rim
        if (unlocked) {
            sr.setColor(baseColor.r * 0.95f, baseColor.g * 0.95f, baseColor.b * 0.95f, 0.95f);
        } else {
            sr.setColor(0.32f, 0.36f, 0.45f, 0.6f);
        }
        sr.circle(cx, cy, r);

        if (unlocked) {
            sr.setColor(baseColor.r * 0.55f, baseColor.g * 0.55f, baseColor.b * 0.55f, 0.95f);
        } else {
            sr.setColor(0.20f, 0.22f, 0.28f, 0.6f);
        }
        sr.circle(cx, cy, r - 1.2f * s);

        // Inner dark slate plate
        sr.setColor(0.09f, 0.12f, 0.19f, 0.98f);
        sr.circle(cx, cy, r - 2.2f * s);

        // Dynamic emblem color palette
        Color c = unlocked ? baseColor : new Color(0.42f, 0.46f, 0.55f, 0.6f);
        Color cLight = unlocked ? new Color(Math.min(1f, baseColor.r * 1.35f), Math.min(1f, baseColor.g * 1.35f), Math.min(1f, baseColor.b * 1.35f), 1f) : new Color(0.55f, 0.60f, 0.70f, 0.6f);
        Color gold = unlocked ? new Color(1f, 0.85f, 0.35f, 1f) : new Color(0.52f, 0.50f, 0.45f, 0.5f);
        Color white = unlocked ? Color.WHITE : new Color(0.65f, 0.68f, 0.75f, 0.65f);

        if (id == null) {
            sr.setColor(c); sr.circle(cx, cy, 5f * s);
            return;
        }

        switch (id) {
            // ==================== NHÁNH SỨC MẠNH (8 skills) ====================
            case "keo_manh": { // Kéo Mạnh: Muscular Flexing Arm / Bicep (Bắp tay cơ bắp)
                // 1. Upper Arm & Shoulder base
                sr.setColor(c);
                sr.rectLine(cx - 10f * s, cy - 7f * s, cx - 4f * s, cy - 7f * s, 6f * s);
                
                // 2. Forearm angling upward towards clenched fist
                sr.rectLine(cx - 7f * s, cy - 6f * s, cx + 2f * s, cy + 5f * s, 6.5f * s);
                
                // 3. Bulging Bicep peak (Bắp tay cuồn cuộn)
                sr.setColor(cLight);
                sr.circle(cx - 1f * s, cy + 1f * s, 5.5f * s);
                
                // 4. Tricep curve beneath
                sr.setColor(c);
                sr.circle(cx - 4f * s, cy - 5f * s, 4.5f * s);
                
                // 5. Clenched Fist at top
                sr.setColor(cLight);
                sr.circle(cx + 3.5f * s, cy + 6.5f * s, 4.2f * s);
                // Thumb & knuckles
                sr.setColor(white);
                sr.circle(cx + 1.5f * s, cy + 4.5f * s, 2f * s); // Thumb
                sr.rectLine(cx + 2f * s, cy + 8.5f * s, cx + 6.5f * s, cy + 7f * s, 1.8f * s); // Knuckle ridge
                
                // 6. Muscle definition shade line
                sr.setColor(0.08f, 0.10f, 0.16f, 0.85f);
                sr.rectLine(cx - 3.5f * s, cy - 1f * s, cx + 0.5f * s, cy + 3.5f * s, 1.5f * s);
                
                // 7. Power aura sparks around the flexed bicep
                sr.setColor(gold);
                sr.triangle(cx - 4f * s, cy + 9f * s, cx - 1f * s, cy + 13f * s, cx, cy + 8f * s);
                sr.triangle(cx + 8f * s, cy + 9f * s, cx + 12f * s, cy + 11f * s, cx + 8f * s, cy + 5f * s);
                sr.circle(cx - 7f * s, cy + 4f * s, 1.2f * s);
                break;
            }

            case "day_thep": { // Dây Thép: Reinforced Steel Cable & Hex Core
                sr.setColor(cLight);
                sr.circle(cx, cy, 11f * s);
                sr.setColor(0.09f, 0.12f, 0.19f, 1f);
                sr.circle(cx, cy, 7.5f * s);
                
                // 4 Crossing Braided Steel Strands
                sr.setColor(c);
                sr.rectLine(cx - 9f * s, cy - 9f * s, cx + 9f * s, cy + 9f * s, 3f * s);
                sr.setColor(cLight);
                sr.rectLine(cx - 9f * s, cy + 9f * s, cx + 9f * s, cy - 9f * s, 3f * s);
                
                // Center Hex Nut Core
                sr.setColor(gold);
                sr.circle(cx, cy, 4.5f * s);
                sr.setColor(0.09f, 0.12f, 0.19f, 1f);
                sr.circle(cx, cy, 2.2f * s);
                sr.setColor(white);
                sr.circle(cx + 1f * s, cy + 1f * s, 1.2f * s);
                break;
            }

            case "co_bap_thep": { // Cơ Bắp Thép: Heavy Plated Steel Barbell & Power Core
                // Knurled Steel Bar
                sr.setColor(white);
                sr.rectLine(cx - 11f * s, cy, cx + 11f * s, cy, 3.8f * s);
                
                // Inner Plates (Tier 1)
                sr.setColor(c);
                sr.rect(cx - 9f * s, cy - 8.5f * s, 3.5f * s, 17f * s);
                sr.rect(cx + 5.5f * s, cy - 8.5f * s, 3.5f * s, 17f * s);
                
                // Outer Plates (Tier 2)
                sr.setColor(cLight);
                sr.rect(cx - 12.5f * s, cy - 6.5f * s, 2.8f * s, 13f * s);
                sr.rect(cx + 9.7f * s, cy - 6.5f * s, 2.8f * s, 13f * s);
                
                // Collar rings
                sr.setColor(gold);
                sr.circle(cx - 5.5f * s, cy, 2.8f * s);
                sr.circle(cx + 5.5f * s, cy, 2.8f * s);
                
                // Center Diamond Power Crest
                sr.triangle(cx, cy + 6f * s, cx - 4f * s, cy, cx + 4f * s, cy);
                sr.triangle(cx, cy - 6f * s, cx - 4f * s, cy, cx + 4f * s, cy);
                sr.setColor(white);
                sr.circle(cx, cy, 1.8f * s);
                break;
            }

            case "bao_kich": { // Bạo Kích: 8-Point Nova Critical Strike Burst
                // 4 Diagonal secondary orange spikes
                sr.setColor(c);
                sr.triangle(cx, cy, cx - 9f * s, cy + 9f * s, cx, cy + 3f * s);
                sr.triangle(cx, cy, cx + 9f * s, cy + 9f * s, cx + 3f * s, cy);
                sr.triangle(cx, cy, cx - 9f * s, cy - 9f * s, cx - 3f * s, cy);
                sr.triangle(cx, cy, cx + 9f * s, cy - 9f * s, cx, cy - 3f * s);
                
                // 4 Primary golden razor-sharp spikes
                sr.setColor(gold);
                sr.triangle(cx, cy + 14f * s, cx - 4f * s, cy, cx + 4f * s, cy);
                sr.triangle(cx, cy - 14f * s, cx - 4f * s, cy, cx + 4f * s, cy);
                sr.triangle(cx - 14f * s, cy, cx, cy - 4f * s, cx, cy + 4f * s);
                sr.triangle(cx + 14f * s, cy, cx, cy - 4f * s, cx, cy + 4f * s);
                
                // Blinding white-hot diamond core
                sr.setColor(white);
                sr.triangle(cx, cy + 5f * s, cx - 3f * s, cy, cx + 3f * s, cy);
                sr.triangle(cx, cy - 5f * s, cx - 3f * s, cy, cx + 3f * s, cy);
                sr.circle(cx, cy, 2.2f * s);
                break;
            }

            case "pha_giap": { // Phá Giáp: Cracked Knight's Breastplate (Áo giáp bị nứt)
                // 1. Armor Main Body (Chestplate / Cuirass)
                sr.setColor(c);
                sr.triangle(cx - 10f * s, cy + 6f * s, cx + 10f * s, cy + 6f * s, cx, cy - 11f * s);
                sr.rect(cx - 10f * s, cy + 1f * s, 20f * s, 5f * s);
                
                // 2. Shoulders / Pauldrons
                sr.setColor(cLight);
                sr.circle(cx - 8f * s, cy + 6f * s, 3.8f * s);
                sr.circle(cx + 8f * s, cy + 6f * s, 3.8f * s);
                
                // 3. Neck notch cutout
                sr.setColor(0.09f, 0.12f, 0.19f, 1f);
                sr.circle(cx, cy + 9f * s, 4.5f * s);
                
                // 4. Armor Pectoral Plates & Waist Rim
                sr.setColor(cLight);
                sr.rectLine(cx - 8f * s, cy - 9f * s, cx + 8f * s, cy - 9f * s, 2f * s);
                sr.rectLine(cx - 7f * s, cy + 2f * s, cx - 1f * s, cy + 2f * s, 1.5f * s);
                sr.rectLine(cx + 1f * s, cy + 2f * s, cx + 7f * s, cy + 2f * s, 1.5f * s);
                
                // 5. THE JAGGED CRACK (Vết nứt vỡ xuyên suốt giáp)
                sr.setColor(0.06f, 0.08f, 0.12f, 1f);
                sr.rectLine(cx + 4f * s, cy + 9f * s, cx + 1f * s, cy + 3f * s, 2.8f * s);
                sr.rectLine(cx + 1f * s, cy + 3f * s, cx - 3f * s, cy - 1f * s, 2.8f * s);
                sr.rectLine(cx - 3f * s, cy - 1f * s, cx - 1f * s, cy - 6f * s, 2.8f * s);
                sr.rectLine(cx - 1f * s, cy - 6f * s, cx - 4f * s, cy - 11f * s, 2.8f * s);
                sr.rectLine(cx + 1f * s, cy + 3f * s, cx + 6f * s, cy + 1f * s, 2.2f * s);
                
                // 6. Glowing fracture heat & flying armor shards
                sr.setColor(gold);
                sr.rectLine(cx + 4f * s, cy + 9f * s, cx + 1f * s, cy + 3f * s, 1.2f * s);
                sr.rectLine(cx + 1f * s, cy + 3f * s, cx - 3f * s, cy - 1f * s, 1.2f * s);
                sr.rectLine(cx - 3f * s, cy - 1f * s, cx - 1f * s, cy - 6f * s, 1.2f * s);
                sr.rectLine(cx - 1f * s, cy - 6f * s, cx - 4f * s, cy - 11f * s, 1.2f * s);
                sr.triangle(cx + 7f * s, cy + 4f * s, cx + 9f * s, cy + 2f * s, cx + 8f * s, cy + 6f * s);
                sr.triangle(cx - 6f * s, cy - 3f * s, cx - 8f * s, cy - 1f * s, cx - 8f * s, cy - 4f * s);
                break;
            }

            case "the_luc_doi_dao": { // Thể Lực Dồi Dào: Radiant Vitality Heart & Golden Wings
                // Golden Angelic Wings behind
                sr.setColor(gold);
                sr.triangle(cx - 2f * s, cy + 4f * s, cx - 13f * s, cy + 9f * s, cx - 8f * s, cy);
                sr.triangle(cx + 2f * s, cy + 4f * s, cx + 13f * s, cy + 9f * s, cx + 8f * s, cy);
                
                // Rich Ruby Heart
                sr.setColor(new Color(0.95f, 0.28f, 0.42f, unlocked ? 1f : 0.6f));
                sr.circle(cx - 4.5f * s, cy + 3.5f * s, 5.2f * s);
                sr.circle(cx + 4.5f * s, cy + 3.5f * s, 5.2f * s);
                sr.triangle(cx - 9.2f * s, cy + 2.5f * s, cx + 9.2f * s, cy + 2.5f * s, cx, cy - 9.5f * s);
                
                // Specular highlight on left lobe
                sr.setColor(white);
                sr.circle(cx - 4.5f * s, cy + 5f * s, 1.8f * s);
                
                // Vitality Pulse Cross in center
                sr.rect(cx - 1.4f * s, cy - 3f * s, 2.8f * s, 7.5f * s);
                sr.rect(cx - 3.8f * s, cy - 0.5f * s, 7.6f * s, 2.8f * s);
                break;
            }

            case "ben_bi": { // Bền Bỉ: Epic Blacksmith Anvil (Cái đe rèn kiên cố)
                // 1. Heavy Stepped Steel Base
                sr.setColor(c);
                sr.rect(cx - 10f * s, cy - 10f * s, 20f * s, 3.5f * s);
                sr.setColor(cLight);
                sr.rectLine(cx - 10f * s, cy - 6.5f * s, cx + 10f * s, cy - 6.5f * s, 1.5f * s);
                
                // 2. Waisted Pedestal Column
                sr.setColor(c);
                sr.triangle(cx - 10f * s, cy - 6.5f * s, cx - 5f * s, cy - 1f * s, cx - 5f * s, cy - 6.5f * s);
                sr.triangle(cx + 10f * s, cy - 6.5f * s, cx + 5f * s, cy - 1f * s, cx + 5f * s, cy - 6.5f * s);
                sr.rect(cx - 5f * s, cy - 6.5f * s, 10f * s, 5.5f * s);
                
                // 3. Main Anvil Body & Table
                sr.rect(cx - 6f * s, cy - 1f * s, 14f * s, 5.5f * s);
                
                // 4. Anvil Horn (Mũi đe nhọn dài bên trái)
                sr.setColor(cLight);
                sr.triangle(cx - 6f * s, cy + 4.5f * s, cx - 14f * s, cy + 2.5f * s, cx - 6f * s, cy - 1f * s);
                sr.circle(cx - 14f * s, cy + 2.5f * s, 1.2f * s);
                
                // 5. Heel & Step with Hardy Hole
                sr.setColor(c);
                sr.rect(cx + 8f * s, cy, 5f * s, 4.5f * s);
                sr.setColor(0.09f, 0.12f, 0.19f, 1f);
                sr.rect(cx + 9.5f * s, cy + 2f * s, 2.2f * s, 2.2f * s);
                
                // 6. Polished Steel Working Face
                sr.setColor(white);
                sr.rectLine(cx - 13.5f * s, cy + 4.5f * s, cx + 13f * s, cy + 4.5f * s, 2f * s);
                
                // 7. Forge Sparks flying from striking face
                sr.setColor(gold);
                sr.circle(cx - 2f * s, cy + 8f * s, 1.4f * s);
                sr.circle(cx + 3f * s, cy + 11f * s, 1.8f * s);
                sr.circle(cx + 6f * s, cy + 7.5f * s, 1.2f * s);
                sr.triangle(cx + 2f * s, cy + 10f * s, cx + 4f * s, cy + 13f * s, cx + 4f * s, cy + 9f * s);
                break;
            }

            case "no_bung": { // No Bụng: Succulent Roast Feast & Aroma Steam
                // Golden-Brown Roasted Meat Body
                sr.setColor(new Color(0.85f, 0.48f, 0.18f, unlocked ? 1f : 0.6f));
                sr.circle(cx - 2f * s, cy + 1f * s, 8.5f * s);
                
                // Glaze highlight & grill sear marks
                sr.setColor(gold);
                sr.circle(cx - 3f * s, cy + 3f * s, 5f * s);
                sr.setColor(new Color(0.55f, 0.25f, 0.08f, 1f));
                sr.rectLine(cx - 6f * s, cy + 3f * s, cx - 1f * s, cy - 2f * s, 1.8f * s);
                sr.rectLine(cx - 2f * s, cy + 6f * s, cx + 2f * s, cy + 1f * s, 1.8f * s);
                
                // Ivory Bone Handle with Knuckles
                sr.setColor(white);
                sr.rectLine(cx + 3.5f * s, cy - 2.5f * s, cx + 10.5f * s, cy - 8.5f * s, 3.5f * s);
                sr.circle(cx + 10.5f * s, cy - 7f * s, 2.5f * s);
                sr.circle(cx + 9f * s, cy - 10f * s, 2.5f * s);
                
                // Rising Fragrant Steam Ribbons
                sr.setColor(new Color(0.88f, 0.95f, 1f, unlocked ? 0.85f : 0.45f));
                sr.rectLine(cx - 7f * s, cy + 8.5f * s, cx - 4.5f * s, cy + 13.5f * s, 2f * s);
                sr.rectLine(cx - 1f * s, cy + 9.5f * s, cx + 1.5f * s, cy + 14.5f * s, 2f * s);
                sr.circle(cx + 5f * s, cy + 12f * s, 1.2f * s);
                break;
            }

            // ==================== NHÁNH NHANH NHẸN (6 skills) ====================
            case "giu_day": { // Giữ Dây: Heavy Fishing Reel Spool & Brake Clamp
                // Outer Spool Flanges
                sr.setColor(c);
                sr.rect(cx - 8f * s, cy + 7f * s, 16f * s, 3.5f * s);
                sr.rect(cx - 8f * s, cy - 10.5f * s, 16f * s, 3.5f * s);
                sr.rect(cx - 5f * s, cy - 8f * s, 10f * s, 16f * s);
                
                // Coiled Braided Fishing Line
                sr.setColor(gold);
                sr.rect(cx - 6.5f * s, cy - 6f * s, 13f * s, 12f * s);
                sr.setColor(new Color(0.8f, 0.65f, 0.2f, 1f));
                sr.rectLine(cx - 6.5f * s, cy + 2f * s, cx + 6.5f * s, cy + 2f * s, 1.5f * s);
                sr.rectLine(cx - 6.5f * s, cy - 2f * s, cx + 6.5f * s, cy - 2f * s, 1.5f * s);
                
                // Mechanical Drag Clamp
                sr.setColor(cLight);
                sr.rectLine(cx - 11f * s, cy, cx + 11f * s, cy, 3.2f * s);
                sr.setColor(white);
                sr.circle(cx, cy, 3f * s);
                sr.setColor(0.09f, 0.12f, 0.19f, 1f);
                sr.circle(cx, cy, 1.4f * s);
                break;
            }

            case "nhanh_tay": { // Nhanh Tay: Triple Swift Speed Arrows (Đầy đủ đầu mũi tên)
                // 1. Top Arrow
                sr.setColor(cLight);
                sr.rectLine(cx - 11f * s, cy + 6.5f * s, cx + 5f * s, cy + 6.5f * s, 2.6f * s);
                sr.triangle(cx + 5f * s, cy + 9.5f * s, cx + 11f * s, cy + 6.5f * s, cx + 5f * s, cy + 3.5f * s);
                
                // 2. Middle Arrow (Longest, leading arrow)
                sr.setColor(white);
                sr.rectLine(cx - 8f * s, cy, cx + 8.5f * s, cy, 3.2f * s);
                sr.setColor(gold);
                sr.triangle(cx + 8.5f * s, cy + 4.5f * s, cx + 15f * s, cy, cx + 8.5f * s, cy - 4.5f * s);
                
                // 3. Bottom Arrow (Complete with sharp arrowhead!)
                sr.setColor(cLight);
                sr.rectLine(cx - 11f * s, cy - 6.5f * s, cx + 5f * s, cy - 6.5f * s, 2.6f * s);
                sr.triangle(cx + 5f * s, cy - 3.5f * s, cx + 11f * s, cy - 6.5f * s, cx + 5f * s, cy - 9.5f * s);
                
                // 4. Trailing wind speed lines
                sr.setColor(c);
                sr.rectLine(cx - 14f * s, cy + 6.5f * s, cx - 12f * s, cy + 6.5f * s, 1.8f * s);
                sr.rectLine(cx - 13f * s, cy, cx - 9.5f * s, cy, 2.2f * s);
                sr.rectLine(cx - 14f * s, cy - 6.5f * s, cx - 12f * s, cy - 6.5f * s, 1.8f * s);
                break;
            }

            case "hoi_phuc_nhanh": { // Hồi Phục Nhanh: Rebirth Energy Vortex & Stamina Flask
                sr.setColor(c);
                sr.circle(cx, cy, 10.5f * s);
                sr.setColor(0.09f, 0.12f, 0.19f, 1f);
                sr.circle(cx, cy, 7.5f * s);
                
                // Two directional arrowheads along the ring
                sr.setColor(cLight);
                sr.triangle(cx - 10f * s, cy - 1f * s, cx - 6f * s, cy + 6f * s, cx - 12f * s, cy + 6f * s);
                sr.triangle(cx + 10f * s, cy + 1f * s, cx + 6f * s, cy - 6f * s, cx + 12f * s, cy - 6f * s);
                
                // Stamina Dewdrop in center
                sr.setColor(gold);
                sr.circle(cx, cy - 1.5f * s, 4.2f * s);
                sr.triangle(cx - 4.2f * s, cy - 1.5f * s, cx + 4.2f * s, cy - 1.5f * s, cx, cy + 6f * s);
                sr.setColor(white);
                sr.circle(cx + 1f * s, cy - 1f * s, 1.6f * s);
                break;
            }

            case "phan_xa": { // Phản Xạ: Predator Reflex Eye & Electric Arc
                // Outer Eye Sclera Outline
                sr.setColor(c);
                sr.circle(cx, cy, 10f * s);
                sr.setColor(0.09f, 0.12f, 0.19f, 1f);
                sr.circle(cx, cy + 6f * s, 9f * s);
                sr.circle(cx, cy - 6f * s, 9f * s);
                
                // Brilliant Golden Iris
                sr.setColor(gold);
                sr.circle(cx, cy, 5.5f * s);
                
                // Vertical Predator Slit Pupil
                sr.setColor(0.09f, 0.12f, 0.19f, 1f);
                sr.triangle(cx, cy + 5f * s, cx - 1.5f * s, cy, cx + 1.5f * s, cy);
                sr.triangle(cx, cy - 5f * s, cx - 1.5f * s, cy, cx + 1.5f * s, cy);
                
                // Electric Lightning Strike across the eye
                sr.setColor(white);
                sr.rectLine(cx - 11f * s, cy + 8f * s, cx - 4f * s, cy + 2f * s, 2f * s);
                sr.rectLine(cx - 4f * s, cy + 2f * s, cx + 4f * s, cy + 5f * s, 2f * s);
                sr.rectLine(cx + 4f * s, cy + 5f * s, cx + 11f * s, cy - 4f * s, 2f * s);
                sr.circle(cx + 1.8f * s, cy + 2f * s, 1.4f * s);
                break;
            }

            case "nhip_nhang": { // Nhịp Nhàng: Harmonious Golden Eighth Notes & Soundwave
                sr.setColor(c);
                sr.circle(cx, cy, 11f * s);
                sr.setColor(0.09f, 0.12f, 0.19f, 1f);
                sr.circle(cx, cy, 9f * s);
                
                // Twin Note Heads
                sr.setColor(gold);
                sr.circle(cx - 5.5f * s, cy - 4.5f * s, 4.2f * s);
                sr.circle(cx + 4.5f * s, cy - 2f * s, 4.2f * s);
                
                // Upright Note Stems
                sr.setColor(cLight);
                sr.rectLine(cx - 2.5f * s, cy - 4.5f * s, cx - 2.5f * s, cy + 8f * s, 2.8f * s);
                sr.rectLine(cx + 7.5f * s, cy - 2f * s, cx + 7.5f * s, cy + 10.5f * s, 2.8f * s);
                
                // Connecting Heavy Beam Banner
                sr.setColor(white);
                sr.rectLine(cx - 3.5f * s, cy + 8f * s, cx + 8.5f * s, cy + 10.5f * s, 3.5f * s);
                
                sr.circle(cx - 8f * s, cy + 6f * s, 1.2f * s);
                sr.circle(cx + 9f * s, cy + 3f * s, 1.4f * s);
                break;
            }

            case "luot_day": { // Lướt Dây: Ocean Wave Crest & Hydrofoil Skim
                // Curving Tidal Wave
                sr.setColor(c);
                sr.circle(cx - 2f * s, cy - 2f * s, 10f * s);
                sr.setColor(0.09f, 0.12f, 0.19f, 1f);
                sr.circle(cx - 2f * s, cy + 4.5f * s, 9f * s);
                
                // Sea Foam Crest on Wave Lip
                sr.setColor(white);
                sr.circle(cx + 6f * s, cy + 3.5f * s, 2.8f * s);
                sr.circle(cx + 3.5f * s, cy + 6f * s, 2.2f * s);
                
                // Hydrofoil Skimming Fin
                sr.setColor(cLight);
                sr.triangle(cx - 9f * s, cy - 3f * s, cx + 7f * s, cy + 6.5f * s, cx + 11f * s, cy + 2f * s);
                
                // Water spray droplets
                sr.setColor(white);
                sr.circle(cx + 10f * s, cy + 6.5f * s, 1.6f * s);
                sr.circle(cx + 7.5f * s, cy + 9.5f * s, 1.2f * s);
                sr.circle(cx + 12f * s, cy + 4f * s, 1.2f * s);
                break;
            }

            // ==================== NHÁNH MAY MẮN (6 skills) ====================
            case "chuyen_gia_moi": { // Chuyên Gia Mồi: Master Feather Lure & Four Wings
                sr.setColor(gold);
                sr.circle(cx, cy + 3.5f * s, 5.5f * s);
                sr.circle(cx, cy - 3f * s, 4.2f * s);
                
                // 4 Filigree Wings Outspread
                sr.setColor(cLight);
                sr.triangle(cx, cy + 3.5f * s, cx - 12f * s, cy + 9f * s, cx - 6f * s, cy + 1f * s);
                sr.triangle(cx, cy + 3.5f * s, cx + 12f * s, cy + 9f * s, cx + 6f * s, cy + 1f * s);
                sr.triangle(cx, cy + 1f * s, cx - 10f * s, cy - 3f * s, cx - 5f * s, cy - 4f * s);
                sr.triangle(cx, cy + 1f * s, cx + 10f * s, cy - 3f * s, cx + 5f * s, cy - 4f * s);
                
                // Golden Hook beneath
                sr.setColor(white);
                sr.rectLine(cx, cy - 3f * s, cx, cy - 9f * s, 2.2f * s);
                sr.rectLine(cx, cy - 9f * s, cx + 4.5f * s, cy - 9f * s, 2.2f * s);
                sr.triangle(cx + 4.5f * s, cy - 9f * s, cx + 4.5f * s, cy - 5.5f * s, cx + 2f * s, cy - 7f * s);
                sr.circle(cx, cy + 5f * s, 1.8f * s);
                break;
            }

            case "may_man_co_ban": { // May Mắn Cơ Bản: 4-Leaf Lucky Shamrock
                sr.setColor(c);
                sr.circle(cx, cy + 6f * s, 4.6f * s);
                sr.circle(cx, cy - 6f * s, 4.6f * s);
                sr.circle(cx - 6f * s, cy, 4.6f * s);
                sr.circle(cx + 6f * s, cy, 4.6f * s);
                
                sr.setColor(cLight);
                sr.circle(cx, cy + 6f * s, 2.5f * s);
                sr.circle(cx, cy - 6f * s, 2.5f * s);
                sr.circle(cx - 6f * s, cy, 2.5f * s);
                sr.circle(cx + 6f * s, cy, 2.5f * s);
                
                sr.rectLine(cx, cy, cx - 5f * s, cy - 11f * s, 2.5f * s);
                
                sr.setColor(gold);
                sr.triangle(cx, cy + 3f * s, cx - 2f * s, cy, cx + 2f * s, cy);
                sr.triangle(cx, cy - 3f * s, cx - 2f * s, cy, cx + 2f * s, cy);
                sr.triangle(cx - 3f * s, cy, cx, cy - 2f * s, cx, cy + 2f * s);
                sr.triangle(cx + 3f * s, cy, cx, cy - 2f * s, cx, cy + 2f * s);
                sr.setColor(white);
                sr.circle(cx, cy, 1.5f * s);
                break;
            }

            case "nhat_mot": { // Nhặt Mót: Salvage Grapnel & Sealed Message Bottle
                sr.setColor(cLight);
                sr.rectLine(cx - 3f * s, cy + 12f * s, cx - 3f * s, cy + 2f * s, 2.8f * s);
                sr.rectLine(cx - 8f * s, cy + 2f * s, cx + 3f * s, cy + 2f * s, 2.8f * s);
                sr.triangle(cx - 8f * s, cy + 2f * s, cx - 8f * s, cy + 6.5f * s, cx - 5.5f * s, cy + 4f * s);
                sr.triangle(cx + 3f * s, cy + 2f * s, cx + 3f * s, cy + 6.5f * s, cx + 0.5f * s, cy + 4f * s);
                
                // Sealed Glass Bottle caught in hook
                sr.setColor(gold);
                sr.rect(cx - 2f * s, cy - 10f * s, 8.5f * s, 8.5f * s);
                sr.rect(cx + 0.5f * s, cy - 1.5f * s, 3.5f * s, 4f * s);
                sr.setColor(new Color(0.9f, 0.3f, 0.3f, 1f)); // Red wax
                sr.rect(cx + 0.2f * s, cy + 1.5f * s, 4.1f * s, 2.2f * s);
                sr.setColor(white);
                sr.rectLine(cx - 0.5f * s, cy - 8.5f * s, cx - 0.5f * s, cy - 3f * s, 1.4f * s);
                break;
            }

            case "ngu_dan_tinh_anh": { // Ngư Dân Tinh Anh: Imperial Golden Leviathan with Royal Crown
                sr.setColor(gold);
                sr.circle(cx - 2f * s, cy - 2.5f * s, 7.5f * s);
                sr.triangle(cx - 2f * s, cy - 2.5f * s, cx + 11.5f * s, cy + 4.5f * s, cx + 11.5f * s, cy - 9.5f * s);
                
                sr.setColor(cLight);
                sr.triangle(cx - 4f * s, cy + 3.5f * s, cx + 2f * s, cy + 7f * s, cx + 1f * s, cy + 2f * s);
                sr.triangle(cx - 3f * s, cy - 4f * s, cx + 2f * s, cy - 8f * s, cx - 1f * s, cy - 2f * s);
                
                sr.setColor(0.09f, 0.12f, 0.19f, 1f);
                sr.circle(cx - 5f * s, cy - 1f * s, 1.8f * s);
                sr.setColor(white);
                sr.circle(cx - 5.5f * s, cy - 0.5f * s, 0.8f * s);
                
                // 3-Peak Royal Gold Crown with Ruby Jewels
                sr.setColor(white);
                sr.triangle(cx - 7f * s, cy + 4f * s, cx - 4.5f * s, cy + 11.5f * s, cx - 2.5f * s, cy + 5f * s);
                sr.triangle(cx - 2.5f * s, cy + 5f * s, cx, cy + 13.5f * s, cx + 2.5f * s, cy + 5f * s);
                sr.triangle(cx + 2.5f * s, cy + 5f * s, cx + 4.5f * s, cy + 11.5f * s, cx + 7f * s, cy + 4f * s);
                sr.setColor(new Color(0.95f, 0.25f, 0.35f, 1f));
                sr.circle(cx, cy + 8f * s, 1.3f * s);
                break;
            }

            case "tho_san_kho_bau": { // Thợ Săn Kho Báu: Overflowing Pirate Treasure Chest
                sr.setColor(new Color(0.6f, 0.35f, 0.12f, unlocked ? 1f : 0.6f));
                sr.rect(cx - 9.5f * s, cy - 8.5f * s, 19f * s, 10f * s);
                
                // Iron Straps & Rivets
                sr.setColor(c);
                sr.rect(cx - 9.5f * s, cy - 8.5f * s, 3f * s, 10f * s);
                sr.rect(cx + 6.5f * s, cy - 8.5f * s, 3f * s, 10f * s);
                
                // Arched Lid opened
                sr.setColor(new Color(0.7f, 0.42f, 0.15f, unlocked ? 1f : 0.6f));
                sr.triangle(cx - 10f * s, cy + 1.5f * s, cx + 10f * s, cy + 1.5f * s, cx, cy + 8f * s);
                sr.circle(cx, cy + 3.5f * s, 7f * s);
                
                // Overflowing Gold & Gems
                sr.setColor(gold);
                sr.circle(cx - 4f * s, cy + 1.5f * s, 3.2f * s);
                sr.circle(cx + 4f * s, cy + 1.5f * s, 3.2f * s);
                sr.circle(cx, cy + 3.5f * s, 3.5f * s);
                sr.setColor(new Color(0.2f, 0.85f, 0.95f, 1f));
                sr.circle(cx - 1.5f * s, cy + 2f * s, 1.8f * s);
                
                // Keyhole & Golden Latch
                sr.setColor(gold);
                sr.rect(cx - 2f * s, cy - 3.5f * s, 4f * s, 4.5f * s);
                sr.setColor(0.09f, 0.12f, 0.19f, 1f);
                sr.circle(cx, cy - 1.5f * s, 1.2f * s);
                break;
            }

            case "nem_xa": { // Ném Xa: Precision Sniper Reticle & Cast Trajectory
                sr.setColor(c);
                sr.circle(cx, cy, 10.5f * s);
                sr.setColor(0.09f, 0.12f, 0.19f, 1f);
                sr.circle(cx, cy, 7.5f * s);
                sr.setColor(cLight);
                sr.circle(cx, cy, 4.5f * s);
                sr.setColor(0.09f, 0.12f, 0.19f, 1f);
                sr.circle(cx, cy, 2.2f * s);
                
                // Crosshairs
                sr.setColor(cLight);
                sr.rectLine(cx, cy + 7.5f * s, cx, cy + 11.5f * s, 2f * s);
                sr.rectLine(cx, cy - 11.5f * s, cx, cy - 7.5f * s, 2f * s);
                sr.rectLine(cx + 7.5f * s, cy, cx + 11.5f * s, cy, 2f * s);
                sr.rectLine(cx - 11.5f * s, cy, cx - 7.5f * s, cy, 2f * s);
                
                // Parabolic Arrow Striking Bullseye
                sr.setColor(gold);
                sr.rectLine(cx - 11f * s, cy - 10f * s, cx + 10f * s, cy + 11f * s, 2.5f * s);
                sr.setColor(white);
                sr.triangle(cx + 12.5f * s, cy + 13.5f * s, cx + 6.5f * s, cy + 11f * s, cx + 10f * s, cy + 5f * s);
                sr.circle(cx, cy, 1.8f * s);
                break;
            }

            // ==================== NHÁNH BÍ KÍP CỔ (6 skills) ====================
            case "duong_suc": { // Dưỡng Sức: Sacred Arcane Lotus & Mana Flame
                sr.setColor(new Color(0.85f, 0.45f, 0.95f, unlocked ? 1f : 0.6f));
                sr.circle(cx, cy, 6.2f * s);
                sr.triangle(cx, cy + 12f * s, cx - 4.5f * s, cy + 2f * s, cx + 4.5f * s, cy + 2f * s);
                sr.triangle(cx - 11f * s, cy + 5f * s, cx - 2f * s, cy, cx - 3.5f * s, cy - 6f * s);
                sr.triangle(cx + 11f * s, cy + 5f * s, cx + 2f * s, cy, cx + 3.5f * s, cy - 6f * s);
                sr.triangle(cx - 7f * s, cy - 8f * s, cx + 7f * s, cy - 8f * s, cx, cy - 1f * s);
                
                sr.setColor(new Color(0.95f, 0.65f, 1f, unlocked ? 1f : 0.6f));
                sr.triangle(cx, cy + 9f * s, cx - 3f * s, cy + 1f * s, cx + 3f * s, cy + 1f * s);
                
                // Sacred Soul Flame Core
                sr.setColor(gold);
                sr.circle(cx, cy + 1f * s, 3.2f * s);
                sr.triangle(cx - 2.5f * s, cy + 1f * s, cx + 2.5f * s, cy + 1f * s, cx, cy + 8f * s);
                sr.setColor(white);
                sr.circle(cx, cy + 1.5f * s, 1.4f * s);
                break;
            }

            case "giat_kep": { // Giật Kép: Twin Heavy Gaffing Hooks 'X'
                // Hook 1: Top-Left to Bottom-Right
                sr.setColor(cLight);
                sr.rectLine(cx - 9f * s, cy + 10f * s, cx + 5f * s, cy - 5f * s, 3.2f * s);
                sr.rectLine(cx + 5f * s, cy - 5f * s, cx + 9f * s, cy - 1f * s, 3.2f * s);
                sr.triangle(cx + 9f * s, cy - 1f * s, cx + 6f * s, cy - 2f * s, cx + 8f * s, cy - 4.5f * s);
                
                // Hook 2: Top-Right to Bottom-Left
                sr.setColor(gold);
                sr.rectLine(cx + 9f * s, cy + 10f * s, cx - 5f * s, cy - 5f * s, 3.2f * s);
                sr.rectLine(cx - 5f * s, cy - 5f * s, cx - 9f * s, cy - 1f * s, 3.2f * s);
                sr.triangle(cx - 9f * s, cy - 1f * s, cx - 6f * s, cy - 2f * s, cx - 8f * s, cy - 4.5f * s);
                
                // Forged Eyelet Rings
                sr.setColor(white);
                sr.circle(cx - 9f * s, cy + 10f * s, 2.8f * s);
                sr.circle(cx + 9f * s, cy + 10f * s, 2.8f * s);
                sr.setColor(0.09f, 0.12f, 0.19f, 1f);
                sr.circle(cx - 9f * s, cy + 10f * s, 1.4f * s);
                sr.circle(cx + 9f * s, cy + 10f * s, 1.4f * s);
                
                // Center Tie Knot
                sr.setColor(white);
                sr.circle(cx, cy, 3.2f * s);
                break;
            }

            case "mat_dai_bang": { // Mắt Đại Bàng: Eagle Vision Eye & Precision Rangefinder
                sr.setColor(c);
                sr.triangle(cx - 13f * s, cy + 1f * s, cx + 4f * s, cy + 8f * s, cx + 4f * s, cy - 7f * s);
                sr.triangle(cx + 13f * s, cy + 1f * s, cx - 4f * s, cy + 8f * s, cx - 4f * s, cy - 7f * s);
                
                // Golden Iris
                sr.setColor(gold);
                sr.circle(cx, cy, 5.8f * s);
                
                // Raptor Slit Pupil
                sr.setColor(0.09f, 0.12f, 0.19f, 1f);
                sr.rect(cx - 1.4f * s, cy - 5.5f * s, 2.8f * s, 11f * s);
                
                // Rangefinder Scope Crosshairs
                sr.setColor(new Color(0.9f, 0.45f, 1f, unlocked ? 0.9f : 0.5f));
                sr.circle(cx, cy, 9.5f * s);
                sr.setColor(0.09f, 0.12f, 0.19f, 1f);
                sr.circle(cx, cy, 8f * s);
                sr.setColor(white);
                sr.circle(cx + 1.8f * s, cy + 2f * s, 1.4f * s);
                break;
            }

            case "thuan_hoa": { // Thuần Hóa: Soothing Hand & Calming Ripples
                // Concentric Calming Ripples below
                sr.setColor(c);
                sr.circle(cx, cy - 3f * s, 10.5f * s);
                sr.setColor(0.09f, 0.12f, 0.19f, 1f);
                sr.circle(cx, cy - 3f * s, 8.5f * s);
                sr.setColor(cLight);
                sr.circle(cx, cy - 3f * s, 6.5f * s);
                sr.setColor(0.09f, 0.12f, 0.19f, 1f);
                sr.circle(cx, cy - 3f * s, 4.5f * s);
                
                // Outstretched Gentle Palm facing downward
                sr.setColor(gold);
                sr.rect(cx - 5f * s, cy + 2f * s, 10f * s, 4.5f * s);
                sr.circle(cx - 4f * s, cy + 5f * s, 2f * s);
                sr.rectLine(cx - 2.5f * s, cy + 2f * s, cx - 2.5f * s, cy + 9f * s, 2f * s);
                sr.rectLine(cx, cy + 2f * s, cx, cy + 10f * s, 2.2f * s);
                sr.rectLine(cx + 2.5f * s, cy + 2f * s, cx + 2.5f * s, cy + 9f * s, 2f * s);
                sr.rectLine(cx + 4.8f * s, cy + 2f * s, cx + 4.8f * s, cy + 7.5f * s, 1.8f * s);
                sr.setColor(white);
                sr.circle(cx, cy - 3f * s, 2.2f * s);
                break;
            }

            case "loi_nguyen_troi_buoc": { // Trói Buộc: Two Crossed Heavy Chains (2 Sợi xích đan chéo)
                // Chain 1: Diagonal Top-Left to Bottom-Right (\)
                sr.setColor(cLight);
                sr.rectLine(cx - 11f * s, cy + 11f * s, cx + 11f * s, cy - 11f * s, 4.5f * s);
                
                // Chain 2: Diagonal Top-Right to Bottom-Left (/)
                sr.setColor(c);
                sr.rectLine(cx + 11f * s, cy + 11f * s, cx - 11f * s, cy - 11f * s, 4.5f * s);
                
                // Chain 1 Hollow links cutout
                sr.setColor(0.09f, 0.12f, 0.19f, 1f);
                sr.rectLine(cx - 8f * s, cy + 8f * s, cx - 5f * s, cy + 5f * s, 2.2f * s);
                sr.rectLine(cx + 5f * s, cy - 5f * s, cx + 8f * s, cy - 8f * s, 2.2f * s);
                
                // Chain 2 Hollow links cutout
                sr.rectLine(cx + 8f * s, cy + 8f * s, cx + 5f * s, cy + 5f * s, 2.2f * s);
                sr.rectLine(cx - 5f * s, cy - 5f * s, cx - 8f * s, cy - 8f * s, 2.2f * s);
                
                // Metallic edge highlights on both chains
                sr.setColor(white);
                sr.circle(cx - 9f * s, cy + 9f * s, 1.5f * s);
                sr.circle(cx + 9f * s, cy + 9f * s, 1.5f * s);
                sr.circle(cx - 9f * s, cy - 9f * s, 1.5f * s);
                sr.circle(cx + 9f * s, cy - 9f * s, 1.5f * s);
                
                // Central Runic Binding Clasp
                sr.setColor(new Color(0.9f, 0.45f, 1f, unlocked ? 1f : 0.6f));
                sr.circle(cx, cy, 5.2f * s);
                sr.setColor(gold);
                sr.circle(cx, cy, 3.2f * s);
                sr.setColor(white);
                sr.circle(cx, cy, 1.5f * s);
                break;
            }

            case "trai_tim_dai_duong": { // Trái Tim Đại Dương: Faceted Sapphire Heart & Tidal Waves
                // Tidal Wing Waves flanking both sides
                sr.setColor(cLight);
                sr.triangle(cx - 3f * s, cy + 5f * s, cx - 13f * s, cy + 8f * s, cx - 8f * s, cy - 2f * s);
                sr.triangle(cx + 3f * s, cy + 5f * s, cx + 13f * s, cy + 8f * s, cx + 8f * s, cy - 2f * s);
                
                // Brilliant Faceted Gemstone Heart
                sr.setColor(new Color(0.18f, 0.55f, 0.95f, unlocked ? 1f : 0.6f));
                sr.triangle(cx - 9.5f * s, cy + 2.5f * s, cx + 9.5f * s, cy + 2.5f * s, cx, cy - 10f * s);
                sr.triangle(cx - 9.5f * s, cy + 2.5f * s, cx - 5f * s, cy + 8f * s, cx, cy + 2.5f * s);
                sr.triangle(cx + 9.5f * s, cy + 2.5f * s, cx + 5f * s, cy + 8f * s, cx, cy + 2.5f * s);
                sr.triangle(cx - 5f * s, cy + 8f * s, cx + 5f * s, cy + 8f * s, cx, cy + 2.5f * s);
                
                // Facet Reflections
                sr.setColor(new Color(0.5f, 0.85f, 1f, 1f));
                sr.triangle(cx - 5f * s, cy + 2.5f * s, cx, cy + 6.5f * s, cx, cy + 2.5f * s);
                sr.setColor(white);
                sr.circle(cx - 3f * s, cy + 4f * s, 1.8f * s);
                sr.circle(cx + 4f * s, cy + 4f * s, 1.2f * s);
                break;
            }

            default: {
                sr.setColor(c);
                sr.circle(cx, cy, 6f * s);
                sr.setColor(white);
                sr.circle(cx, cy, 2.5f * s);
                break;
            }
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
        previousInputProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                panY += amountY * 45f;
                clampPan();
                return true;
            }
        });
    }

    @Override public void hide() {
        if (previousInputProcessor != null) {
            Gdx.input.setInputProcessor(previousInputProcessor);
        }
    }

    @Override public void pause() {}
    @Override public void resume() {}

    @Override public void dispose() {
        if (previousInputProcessor != null) {
            Gdx.input.setInputProcessor(previousInputProcessor);
        }
        sr.dispose();
    }
}
