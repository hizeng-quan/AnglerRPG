package com.fishingrpg.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class NotificationUI {
    private static final GlyphLayout layout = new GlyphLayout();

    public static void drawNotification(ShapeRenderer sr, SpriteBatch batch, BitmapFont font, String message, float timer, float maxTime, float screenWidth, float screenHeight) {
        if (timer <= 0 || message == null || message.isEmpty()) return;

        // Auto-scale font to fit if it's too long
        float originalScaleX = font.getData().scaleX;
        float originalScaleY = font.getData().scaleY;
        
        font.getData().setScale(1.2f);
        layout.setText(font, message);
        
        // Max width for phone-style notification box
        float maxBoxWidth = 600f;
        if (layout.width > maxBoxWidth - 40f) {
            float newScale = 1.2f * (maxBoxWidth - 40f) / layout.width;
            font.getData().setScale(newScale);
            layout.setText(font, message);
        }

        float boxW = layout.width + 60f;
        float boxH = layout.height + 40f;
        float boxX = (screenWidth - boxW) / 2f;
        
        // Slide down animation
        float animDuration = 0.3f;
        float fraction = 1f; // 1 means fully visible
        
        if (maxTime - timer < animDuration) { // appearing
            float t = (maxTime - timer) / animDuration;
            fraction = 1f - (float)Math.pow(1f - t, 3); // ease out cubic, 0 -> 1
        } else if (timer < animDuration) { // disappearing
            float t = timer / animDuration;
            fraction = 1f - (float)Math.pow(1f - t, 3); // ease out cubic, 0 -> 1
        }
        
        float targetY = screenHeight - 20f - boxH;
        float hiddenY = screenHeight;
        float boxY = hiddenY + (targetY - hiddenY) * fraction;

        // End active batch to draw shapes
        if (batch.isDrawing()) batch.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);
        // Draw shadow
        sr.setColor(0f, 0f, 0f, 0.4f);
        sr.rect(boxX + 5, boxY - 5, boxW, boxH);
        
        // Draw box (phone style)
        sr.setColor(0.15f, 0.18f, 0.22f, 0.95f); // Dark elegant gray
        sr.rect(boxX, boxY, boxW, boxH);
        
        // Draw border
        sr.setColor(0.3f, 0.6f, 0.9f, 1f); // Blue accent
        sr.rect(boxX, boxY, 6f, boxH); // Left accent bar
        sr.end();

        // Draw text
        batch.begin();
        font.setColor(1f, 1f, 1f, 1f);
        font.draw(batch, message, boxX + 30f, boxY + boxH/2f + layout.height/2f);
        font.getData().setScale(originalScaleX, originalScaleY);
        // Do not call batch.end() here, let the caller handle it if needed, wait, we ended batch earlier. Let's leave it drawing so caller can continue or just end it if it wasn't drawing.
        batch.end();
    }
}
