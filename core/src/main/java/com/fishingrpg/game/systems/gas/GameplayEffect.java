package com.fishingrpg.game.systems.gas;

/**
 * Hiệu Ứng Trạng Thái Trong Gameplay Ability System (Gameplay Effect).
 * Đại diện cho Buff người chơi hoặc Debuff lên cá trong quá trình câu cá.
 */
public class GameplayEffect {

    private final String id;
    private final String name;
    private float duration;
    private float timer;

    // Các chỉ số điều chỉnh (Stat Modifiers)
    private float damageMultiplier = 1.0f;
    private float fishPullMultiplier = 1.0f;
    private boolean freezeFish = false;
    private float staminaRegenPerSec = 0f;

    public GameplayEffect(String id, String name, float duration) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.timer = duration;
    }

    public void update(float delta) {
        if (timer > 0f) {
            timer -= delta;
        }
    }

    public boolean isExpired() {
        return timer <= 0f;
    }

    public float getProgress() {
        if (duration <= 0f) return 0f;
        return Math.max(0f, timer / duration);
    }

    // Getters & Fluent Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public float getDuration() { return duration; }
    public float getTimer() { return timer; }

    public float getDamageMultiplier() { return damageMultiplier; }
    public GameplayEffect setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
        return this;
    }

    public float getFishPullMultiplier() { return fishPullMultiplier; }
    public GameplayEffect setFishPullMultiplier(float fishPullMultiplier) {
        this.fishPullMultiplier = fishPullMultiplier;
        return this;
    }

    public boolean isFreezeFish() { return freezeFish; }
    public GameplayEffect setFreezeFish(boolean freezeFish) {
        this.freezeFish = freezeFish;
        return this;
    }

    public float getStaminaRegenPerSec() { return staminaRegenPerSec; }
    public GameplayEffect setStaminaRegenPerSec(float staminaRegenPerSec) {
        this.staminaRegenPerSec = staminaRegenPerSec;
        return this;
    }
}
