package com.fishingrpg.game.entities.equipment;

import com.badlogic.gdx.graphics.Color;

public enum Rarity {
    COMMON(Color.WHITE, 0),
    UNCOMMON(Color.GREEN, 1),
    RARE(new Color(0.2f, 0.6f, 1f, 1f), 2),
    EPIC(Color.PURPLE, 3),
    LEGENDARY(Color.ORANGE, 4);

    public final Color color;
    public final int substatCount;

    Rarity(Color color, int substatCount) {
        this.color = color;
        this.substatCount = substatCount;
    }
}
