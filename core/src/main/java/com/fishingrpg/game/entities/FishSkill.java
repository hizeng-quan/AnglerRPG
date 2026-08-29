package com.fishingrpg.game.entities;

public class FishSkill {
    public String id;
    public boolean isActive;
    public float triggerHpPercent; // 0.0 -> 1.0 (e.g. 0.4f for 40%)
    public transient boolean triggered; // Runtime flag to ensure active skills only fire once
}
