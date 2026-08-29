package com.fishingrpg.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class FloatingTextManager {

    private static class FloatingText {
        String text;
        float x, y;
        float vy;
        Color color;
        float lifeTimer;
        float maxLife;

        public FloatingText(String text, float x, float y, Color color, float maxLife) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.color = new Color(color); // copy color since we will modify alpha
            this.maxLife = maxLife;
            this.lifeTimer = maxLife;
            this.vy = 50f; // float up speed
        }
    }

    private Array<FloatingText> activeTexts;

    public FloatingTextManager() {
        activeTexts = new Array<>();
    }

    public void addText(String text, float x, float y, Color color) {
        activeTexts.add(new FloatingText(text, x, y, color, 1.5f));
    }

    public void updateAndRender(float delta, SpriteBatch batch, BitmapFont font) {
        for (int i = activeTexts.size - 1; i >= 0; i--) {
            FloatingText ft = activeTexts.get(i);
            
            ft.lifeTimer -= delta;
            if (ft.lifeTimer <= 0) {
                activeTexts.removeIndex(i);
                continue;
            }

            ft.y += ft.vy * delta;
            
            // Fade out in the last 50% of life
            float alpha = 1f;
            if (ft.lifeTimer < ft.maxLife / 2f) {
                alpha = ft.lifeTimer / (ft.maxLife / 2f);
            }
            ft.color.a = alpha;

            font.setColor(ft.color);
            font.draw(batch, ft.text, ft.x, ft.y);
        }
        
        // Reset font color to white to avoid messing up other renders
        font.setColor(Color.WHITE);
    }
    
    public void clear() {
        activeTexts.clear();
    }
}
