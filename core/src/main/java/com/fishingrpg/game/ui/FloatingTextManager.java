package com.fishingrpg.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class FloatingTextManager {

    private static class FloatingText {
        String text;
        float x, y;
        float vx, vy;
        Color color;
        float lifeTimer;
        float maxLife;
        boolean isCrit;
        boolean isDamage;
        boolean isHeal;

        public FloatingText(String text, float x, float y, Color color, float maxLife) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.color = new Color(color);
            this.maxLife = maxLife;
            this.lifeTimer = maxLife;
            this.vx = MathUtils.random(-15f, 15f);
            this.vy = MathUtils.random(45f, 65f); // upward drift

            this.isDamage = text.startsWith("-");
            this.isHeal = text.startsWith("+");
            this.isCrit = false;

            if (isDamage) {
                try {
                    int val = Integer.parseInt(text.substring(1).trim());
                    if (val >= 25) this.isCrit = true;
                } catch (Exception ignored) {}
            }
        }
    }

    private final Array<FloatingText> activeTexts;
    private final GlyphLayout layout;

    public FloatingTextManager() {
        activeTexts = new Array<>();
        layout = new GlyphLayout();
    }

    public void addText(String text, float x, float y, Color color) {
        activeTexts.add(new FloatingText(text, x, y, color, 1.4f));
    }

    public void updateAndRender(float delta, SpriteBatch batch, BitmapFont font) {
        for (int i = activeTexts.size - 1; i >= 0; i--) {
            FloatingText ft = activeTexts.get(i);

            ft.lifeTimer -= delta;
            if (ft.lifeTimer <= 0) {
                activeTexts.removeIndex(i);
                continue;
            }

            ft.x += ft.vx * delta;
            ft.y += ft.vy * delta;
            ft.vy = Math.max(20f, ft.vy - 20f * delta); // gentle deceleration

            float progress = 1f - (ft.lifeTimer / ft.maxLife); // 0.0 -> 1.0

            // Alpha fade out in the last 40% of lifetime
            float alpha = 1f;
            if (ft.lifeTimer < ft.maxLife * 0.4f) {
                alpha = ft.lifeTimer / (ft.maxLife * 0.4f);
            }
            ft.color.a = alpha;

            // Pop-in bounce scale animation
            float baseScale = ft.isCrit ? 1.35f : (ft.isDamage ? 1.05f : (ft.isHeal ? 1.15f : 1.1f));
            float scale;
            if (progress < 0.15f) {
                float pop = progress / 0.15f;
                scale = baseScale * (1.5f - 0.5f * pop); // Start at 1.5x and snap to 1.0x
            } else {
                scale = baseScale;
            }

            font.getData().setScale(scale);
            layout.setText(font, ft.text);
            float drawX = ft.x - layout.width / 2f;
            float drawY = ft.y;

            // 1. High contrast drop shadow in black
            font.setColor(0f, 0f, 0f, 0.90f * alpha);
            font.draw(batch, ft.text, drawX + 1.5f, drawY - 1.5f);
            font.draw(batch, ft.text, drawX - 1f, drawY - 1f);

            // 2. High-impact text color
            if (ft.isCrit) {
                font.setColor(1f, 0.88f, 0.25f, alpha); // Radiant Gold for critical hit
            } else if (ft.isDamage) {
                font.setColor(1f, 0.22f, 0.25f, alpha); // Vivid Ruby Crimson for normal hit
            } else if (ft.isHeal) {
                font.setColor(0.25f, 1f, 0.45f, alpha); // Bright Emerald for heals
            } else {
                font.setColor(ft.color.r, ft.color.g, ft.color.b, alpha);
            }

            font.draw(batch, ft.text, drawX, drawY);
        }

        // Reset font
        font.getData().setScale(1f);
        font.setColor(Color.WHITE);
    }

    public void clear() {
        activeTexts.clear();
    }
}
