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
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.fishingrpg.game.FishingRPG;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.entities.equipment.Equipment;
import com.fishingrpg.game.entities.equipment.EquipmentGenerator;
import com.fishingrpg.game.entities.equipment.EquipmentType;
import com.fishingrpg.game.entities.equipment.Rarity;
import com.fishingrpg.game.ui.NotificationUI;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class GachaScreen implements Screen {
    private final FishingRPG game;
    private final Player player;
    private final OrthographicCamera camera;
    private final ShapeRenderer sr;
    private final BitmapFont font;
    private final BitmapFont titleFont;
    private final GlyphLayout layout;
    
    private final float W = 1280f;
    private final float H = 800f;
    private final Screen previousScreen;

    private String message = null;
    private float messageTimer = 0f;
    
    private List<Equipment> lastPull = new ArrayList<>();
    
    // Animation States
    private enum State { IDLE, SPINNING, RESULT }
    private State state = State.IDLE;
    
    private float spinTimer = 0f;
    private final float totalSpinTime = 3.0f;
    private List<Equipment> spinItems = new ArrayList<>();
    private Equipment bestItem = null;
    
    // Tooltip
    private boolean showTooltip = false;

    public GachaScreen(FishingRPG game, Screen previousScreen) {
        this.game = game;
        this.player = game.player;
        this.previousScreen = previousScreen;
        this.camera = new OrthographicCamera(W, H);
        this.camera.position.set(W / 2, H / 2, 0);
        this.sr = new ShapeRenderer();
        this.font = game.font;
        this.titleFont = game.titleFont != null ? game.titleFont : game.font;
        this.layout = new GlyphLayout();
    }

    private void showMessage(String msg) {
        message = msg;
        messageTimer = 2.0f;
    }

    @Override
    public void render(float delta) {
        if (messageTimer > 0) messageTimer -= delta;
        
        if (state == State.SPINNING) {
            spinTimer += delta;
            if (spinTimer >= totalSpinTime) {
                spinTimer = totalSpinTime;
                state = State.RESULT;
            }
        }

        handleInput();
        if (game.getScreen() != this) return;

        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        drawUI();

        NotificationUI.drawNotification(sr, game.batch, font, message, messageTimer, 2.0f, W, H);
    }
    
    private void drawUI() {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        
        // Treasure Chest Background
        // Base Wood
        sr.setColor(0.25f, 0.15f, 0.08f, 1f);
        sr.rect(100f, 50f, W - 200f, H - 150f);
        
        // Wood panels (darker lines)
        sr.setColor(0.18f, 0.10f, 0.05f, 1f);
        for (int i = 1; i <= 4; i++) {
            sr.rect(100f, 50f + i * ((H - 150f) / 5f), W - 200f, 8f);
        }
        
        // Chest Borders (Gold/Brass)
        sr.setColor(0.8f, 0.65f, 0.15f, 1f);
        sr.rect(80f, 30f, W - 160f, 20f); // Bottom border
        sr.rect(80f, H - 100f, W - 160f, 20f); // Top border
        sr.rect(80f, 30f, 20f, H - 110f); // Left border
        sr.rect(W - 100f, 30f, 20f, H - 110f); // Right border
        
        // Chest Lock
        sr.setColor(0.7f, 0.7f, 0.7f, 1f);
        sr.rect(W / 2f - 40f, H - 120f, 80f, 60f); // Silver lock plate
        sr.setColor(0.1f, 0.1f, 0.1f, 1f);
        sr.circle(W / 2f, H - 90f, 10f); // Keyhole top
        sr.triangle(W / 2f, H - 90f, W / 2f - 8f, H - 105f, W / 2f + 8f, H - 105f); // Keyhole bottom
        
        // Top right box for diamonds and tickets
        sr.setColor(0.1f, 0.1f, 0.15f, 0.8f);
        sr.rect(W - 250f, H - 90f, 230f, 70f);
        sr.setColor(0.4f, 0.4f, 0.5f, 1f);
        sr.rectLine(W - 250f, H - 90f, W - 20f, H - 90f, 2f);
        
        // Buttons
        if (state == State.IDLE || state == State.RESULT) {
            drawButton(300f, 100f, 200f, 60f, new Color(0.3f, 0.5f, 0.8f, 1f));
            drawButton(550f, 100f, 200f, 60f, new Color(0.3f, 0.6f, 0.4f, 1f));
            drawButton(800f, 100f, 200f, 60f, new Color(0.8f, 0.5f, 0.2f, 1f));
        }
        
        // Back button
        drawButton(20f, H - 60f, 120f, 40f, new Color(0.3f, 0.2f, 0.2f, 1f));
        
        // Tooltip hover area (left side)
        float tooltipX = 20f, tooltipY = H - 120f, tooltipW = 120f, tooltipH = 40f;
        drawButton(tooltipX, tooltipY, tooltipW, tooltipH, new Color(0.2f, 0.3f, 0.4f, 1f));
        
        // Spinning area background
        if (state != State.IDLE) {
            sr.setColor(0.1f, 0.1f, 0.12f, 1f);
            sr.rect(140f, 450f, 1000f, 120f);
            // highlight center
            sr.setColor(0.2f, 0.2f, 0.2f, 1f);
            sr.rect(640f - 5f, 450f, 10f, 120f);
        }

        sr.end();
        
        game.batch.begin();
        
        // Title
        titleFont.setColor(Color.WHITE);
        layout.setText(titleFont, "QUAY TRANG BỊ");
        titleFont.draw(game.batch, "QUAY TRANG BỊ", (W - layout.width) / 2f, 760f);
        
        // Back & Tooltip text
        font.setColor(Color.WHITE);
        drawCenteredText(font, "QUAY LẠI", 20f, H - 60f, 120f, 40f);
        drawCenteredText(font, "(?) TỶ LỆ", tooltipX, tooltipY, tooltipW, tooltipH);
        
        // Top right text
        font.setColor(Color.CYAN);
        font.draw(game.batch, "Kim Cương: " + player.getDiamonds(), W - 230f, H - 35f);
        font.setColor(Color.YELLOW);
        font.draw(game.batch, "Vé Quay: " + player.getGachaTickets(), W - 230f, H - 65f);
        
        // Pity info (aligned right)
        font.setColor(Color.LIGHT_GRAY);
        String pityRareText = "Còn " + Math.max(0, 10 - player.pityRare) + " lượt chắc chắn ra Rare+";
        String pityEpicText = "Còn " + Math.max(0, 50 - player.pityEpic) + " lượt chắc chắn ra Epic+";
        String pityLegendText = "Còn " + Math.max(0, 100 - player.pityLegendary) + " lượt chắc chắn ra Legendary";
        
        layout.setText(font, pityRareText);
        font.draw(game.batch, pityRareText, W - 20f - layout.width, H - 110f);
        layout.setText(font, pityEpicText);
        font.draw(game.batch, pityEpicText, W - 20f - layout.width, H - 140f);
        layout.setText(font, pityLegendText);
        font.draw(game.batch, pityLegendText, W - 20f - layout.width, H - 170f);
        
        // Button Texts
        if (state == State.IDLE || state == State.RESULT) {
            font.setColor(Color.WHITE);
            drawCenteredText(font, "Đổi 1 Vé", 300f, 100f + 30f, 200f, 30f); // Top half
            drawCenteredText(font, "(10 KC)", 300f, 100f, 200f, 30f); // Bottom half
            
            drawCenteredText(font, "Quay 1x", 550f, 100f + 30f, 200f, 30f);
            drawCenteredText(font, "(1 Vé)", 550f, 100f, 200f, 30f);
            
            drawCenteredText(font, "Quay 10x", 800f, 100f + 30f, 200f, 30f);
            drawCenteredText(font, "(10 Vé)", 800f, 100f, 200f, 30f);
        }

        // Showcase in IDLE
        if (state == State.IDLE) {
            font.setColor(Color.YELLOW);
            layout.setText(font, "*** Trang bị siêu cấp: Hoàng Gia ***");
            font.draw(game.batch, "*** Trang bị siêu cấp: Hoàng Gia ***", (W - layout.width) / 2f, 480f);
            
            font.setColor(Color.LIGHT_GRAY);
            layout.setText(font, "(Có thể nhận được qua hệ thống quay gacha với tỷ lệ cực hiếm)");
            font.draw(game.batch, "(Có thể nhận được qua hệ thống quay gacha với tỷ lệ cực hiếm)", (W - layout.width) / 2f, 450f);
        }

        game.batch.end();

        // Draw Spinner Scissor
        if (state != State.IDLE) {
            game.batch.begin();
            game.batch.flush();
            Rectangle clipBounds = new Rectangle(140f, 450f, 1000f, 120f);
            Rectangle scissors = new Rectangle();
            ScissorStack.calculateScissors(camera, game.batch.getTransformMatrix(), clipBounds, scissors);
            if (ScissorStack.pushScissors(scissors)) {
                
                float progress = spinTimer / totalSpinTime;
                float easedProgress = Interpolation.pow5Out.apply(progress);
                
                // item width = 150, spacing = 20
                float itemWidth = 150f;
                float spacing = 50f;
                float totalW = itemWidth + spacing;
                
                // 70 items. We want the best item to be at index 65, ending exactly in the middle.
                int targetIndex = 65;
                // At progress = 0, x offset is 0. 
                // At progress = 1, x offset should be such that index 26 is at 640.
                float targetOffset = - (targetIndex * totalW) + (1000f / 2f) - (itemWidth / 2f);
                float currentOffset = 140f + targetOffset * easedProgress; // start from right side 140 is the box start

                for (int i = 0; i < spinItems.size(); i++) {
                    float ix = currentOffset + i * totalW;
                    if (ix > 140f - itemWidth && ix < 1140f) {
                        Equipment eq = spinItems.get(i);
                        font.setColor(eq.rarity.color);
                        font.draw(game.batch, eq.name, ix, 560f, itemWidth, com.badlogic.gdx.utils.Align.center, true);
                        font.draw(game.batch, eq.rarity.name(), ix, 500f, itemWidth, com.badlogic.gdx.utils.Align.center, false);
                        font.setColor(Color.WHITE);
                        
                    }
                }
                
                game.batch.flush();
                ScissorStack.popScissors();
            }
            game.batch.end();
            
            // Draw full result below if x10
            if (state == State.RESULT && lastPull.size() > 1) {
                game.batch.begin();
                float ry = 380f;
                float rxOffset = 230f;
                for (int i = 0; i < lastPull.size(); i++) {
                    Equipment eq = lastPull.get(i);
                    font.setColor(eq.rarity.color);
                    font.draw(game.batch, "- " + eq.name + " (" + eq.rarity.name() + ")", rxOffset, ry);
                    ry -= 30f;
                    if (i == 4) {
                        ry = 380f;
                        rxOffset = 650f;
                    }
                }
                game.batch.end();
            }
        }

        // Draw Tooltip (on top of everything)
        if (showTooltip) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(0.1f, 0.1f, 0.1f, 0.9f);
            sr.rect(150f, H - 250f, 200f, 180f);
            sr.setColor(0.5f, 0.5f, 0.5f, 1f);
            sr.rectLine(150f, H - 250f, 350f, H - 250f, 1f);
            sr.rectLine(150f, H - 70f, 350f, H - 70f, 1f);
            sr.rectLine(150f, H - 250f, 150f, H - 70f, 1f);
            sr.rectLine(350f, H - 250f, 350f, H - 70f, 1f);
            sr.end();
            
            game.batch.begin();
            font.setColor(Color.WHITE); font.draw(game.batch, "Common: 50%", 160f, H - 90f);
            font.setColor(Color.GREEN); font.draw(game.batch, "Uncommon: 30%", 160f, H - 120f);
            font.setColor(new Color(0.3f, 0.6f, 1f, 1f)); font.draw(game.batch, "Rare: 15%", 160f, H - 150f);
            font.setColor(new Color(0.7f, 0.3f, 1f, 1f)); font.draw(game.batch, "Epic: 4%", 160f, H - 180f);
            font.setColor(new Color(1f, 0.15f, 0.15f, 1f)); font.draw(game.batch, "Legendary: 1%", 160f, H - 210f);
            game.batch.end();
        }
    }

    private void drawButton(float x, float y, float w, float h, Color color) {
        sr.setColor(color);
        sr.rect(x, y, w, h);
        sr.setColor(color.cpy().mul(0.7f));
        sr.rectLine(x, y, x + w, y, 2f);
        sr.rectLine(x, y + h, x + w, y + h, 2f);
        sr.rectLine(x, y, x, y + h, 2f);
        sr.rectLine(x + w, y, x + w, y + h, 2f);
    }
    
    private void drawCenteredText(BitmapFont f, String text, float x, float y, float width, float height) {
        layout.setText(f, text);
        f.draw(game.batch, text, x + (width - layout.width) / 2f, y + (height + layout.height) / 2f);
    }

    private void handleInput() {
        Vector3 m = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(m);
        float mx = m.x;
        float my = m.y;

        showTooltip = (mx >= 20f && mx <= 140f && my >= H - 120f && my <= H - 80f);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (state == State.SPINNING) {
                // skip animation
                spinTimer = totalSpinTime;
            } else {
                game.setScreen(previousScreen);
                dispose();
            }
            return;
        }

        if (Gdx.input.justTouched()) {
            if (state == State.SPINNING) {
                spinTimer = totalSpinTime; // click to skip
                return;
            }

            if (mx >= 20 && mx <= 140 && my >= H - 60 && my <= H - 20) {
                game.setScreen(previousScreen);
                dispose();
                return;
            }

            if (state == State.IDLE || state == State.RESULT) {
                if (my >= 100 && my <= 160) {
                    if (mx >= 300 && mx <= 500) {
                        buyTicket();
                    } else if (mx >= 550 && mx <= 750) {
                        pullGacha(1);
                    } else if (mx >= 800 && mx <= 1000) {
                        pullGacha(10);
                    }
                }
            }
        }
    }

    private void buyTicket() {
        if (player.getDiamonds() >= 10) {
            player.addDiamonds(-10);
            player.addGachaTickets(1);
            showMessage("Đã đổi 1 Vé Quay!");
        } else {
            showMessage("Không đủ Kim Cương!");
        }
    }

    private void pullGacha(int amount) {
        if (player.getGachaTickets() < amount) {
            message = "KHÔNG ĐỦ VÉ QUAY!";
            messageTimer = 2.0f;
            return;
        }
        player.addGachaTickets(-amount);
        player.getQuestManager().addProgress("gacha", amount);
        lastPull.clear();
        bestItem = null;
        
        for (int i = 0; i < amount; i++) {
            boolean guarantee = (amount == 10 && i == 9);
            Equipment eq = EquipmentGenerator.rollGacha(player, guarantee);
            player.addEquipment(eq);
            lastPull.add(eq);
            
            if (bestItem == null || eq.rarity.ordinal() > bestItem.rarity.ordinal()) {
                bestItem = eq;
            }
        }
        
        generateSpinItems();
        
        state = State.SPINNING;
        spinTimer = 0f;
    }
    
    private void generateSpinItems() {
        spinItems.clear();
        // Generate ~70 items for super fast spin
        for (int i = 0; i < 70; i++) {
            if (i == 65) {
                spinItems.add(bestItem);
            } else {
                // fake items (just visual)
                Equipment fake = new Equipment();
                fake.type = EquipmentType.values()[MathUtils.random(0, 2)];
                fake.rarity = Rarity.values()[MathUtils.random(0, Rarity.EPIC.ordinal())];
                fake.name = com.fishingrpg.game.entities.equipment.EquipmentGenerator.getRandomName(fake.type);
                fake.level = player.getLevel();
                spinItems.add(fake);
            }
        }
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        sr.dispose();
    }
}
