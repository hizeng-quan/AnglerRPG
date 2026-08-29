package com.fishingrpg.game.entities;

/**
 * Manages combat state: stamina, tension.
 */
public class PlayerCombat {

    private float maxStamina;
    private float currentStamina;

    private float maxLineTension   = 100f;
    private float currentLineTension = 0f;

    private float staminaRegenAmount = 0f;
    private float staminaRegenRate = 0f;

    public boolean justFilledStamina = false;

    public PlayerCombat(float maxStamina) {
        this.maxStamina = maxStamina;
        this.currentStamina = maxStamina;
    }

    public void update(float delta, float passiveRegenBonus) {
        float oldStamina = currentStamina;
        
        // Passive regen: 0.5% max stamina per second (1% per 2s) + Fast Recovery bonus
        if (currentStamina < maxStamina) {
            consumeStamina(-(maxStamina * 0.005f * delta) - (passiveRegenBonus * delta));
        }

        if (staminaRegenAmount > 0) {
            float heal = staminaRegenRate * delta;
            if (heal > staminaRegenAmount) heal = staminaRegenAmount;
            
            consumeStamina(-heal); // Negative to heal
            
            staminaRegenAmount -= heal;
            if (staminaRegenAmount <= 0) {
                staminaRegenAmount = 0;
            }
        }
        
        if (oldStamina < maxStamina && currentStamina >= maxStamina) {
            justFilledStamina = true;
        }
    }

    public void addStaminaRegen(float totalAmount, float ratePerSecond) {
        this.staminaRegenAmount += totalAmount;
        this.staminaRegenRate = ratePerSecond;
    }

    public void resetForNewEncounter() {
        currentLineTension = 0f;
    }

    public void applyStatBonuses(float baseStamina, float theLucBonus, float rodStaminaBonus, float dayThepBonus, float lineTensionBonus) {
        maxLineTension = 100f + dayThepBonus + lineTensionBonus;

        float oldMax = maxStamina;
        maxStamina = baseStamina + theLucBonus + rodStaminaBonus;
        if (maxStamina > oldMax) {
            currentStamina += (maxStamina - oldMax);
        }
        currentStamina = Math.min(currentStamina, maxStamina);
    }

    public void increaseTension(float amount) {
        currentLineTension = Math.min(currentLineTension + amount, maxLineTension);
    }

    public void decreaseTension(float amount) {
        currentLineTension = Math.max(0, currentLineTension - amount);
    }

    public boolean isLineBroken() { return currentLineTension >= maxLineTension; }

    /**
     * Consume stamina.
     * @param amount stamina to use (negative = heal).
     * @return true if successful (enough stamina), false otherwise.
     */
    public boolean consumeStamina(float amount) {
        if (amount < 0) {
            // Heal
            currentStamina = Math.min(maxStamina, currentStamina - amount);
            return true;
        }
        if (currentStamina >= amount) {
            currentStamina -= amount;
            return true;
        }
        return false;
    }

    public float getCurrentStamina()     { return currentStamina; }
    public float getMaxStamina()         { return maxStamina; }
    public float getCurrentLineTension() { return currentLineTension; }
    public float getMaxLineTension()     { return maxLineTension; }
}
