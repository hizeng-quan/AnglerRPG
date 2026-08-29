package com.fishingrpg.game.entities.equipment;

public class Substat {
    public enum Type {
        STAMINA_REGEN,    // +% Hồi thể lực
        GOLD_BONUS,       // +% Vàng
        EXP_BONUS,        // +% Exp
        DAMAGE_BONUS,     // +% Sát thương
        FISH_PULL_REDUCE  // -% Lực kéo của cá
    }

    public Type type;
    public float value;

    public Substat() {}

    public Substat(Type type, float value) {
        this.type = type;
        this.value = value;
    }

    public String toString() {
        switch (type) {
            case STAMINA_REGEN: return String.format("+%.1f%% Hồi Thể Lực", value);
            case GOLD_BONUS: return String.format("+%.1f%% Vàng", value);
            case EXP_BONUS: return String.format("+%.1f%% EXP", value);
            case DAMAGE_BONUS: return String.format("+%.1f%% Sát thương", value);
            case FISH_PULL_REDUCE: return String.format("-%.1f%% Lực kéo cá", value);
            default: return "";
        }
    }
}
