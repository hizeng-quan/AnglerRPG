package com.fishingrpg.game.entities;

import com.fishingrpg.game.systems.QuestManager;

/**
 * Player progression manager: Level, XP, Gold, Resources, etc.
 */
public class PlayerProgression {

    private int level       = 1;
    private int currentXp   = 0;
    private int gold        = 0;
    private int fishCaught  = 0;
    private int skillPoints = 0;
    private int diamonds = 0;
    private int gachaTickets = 0;

    public static int xpRequired(int level) {
        int[] bases = {1, 2, 5};
        int idx = level - 1;
        if (idx < 0) idx = 0;
        int base = bases[idx % 3];
        int multiplier = (int) Math.pow(10, (idx / 3) + 1);
        return base * multiplier;
    }

    public int gainXp(int xp, float equipmentExpBonus, String activeSetName, QuestManager questManager, Runnable onLevelUpCallback) {
        if (xp <= 0) return 0;
        
        float bonus = 1f + equipmentExpBonus / 100f;
        if ("Tập Sự".equals(activeSetName)) bonus += 0.2f;
        int finalXp = (int)(xp * bonus);
        
        currentXp += finalXp;
        int levelsGained = 0;
        int threshold;
        
        while (currentXp >= (threshold = xpRequired(level))) {
            currentXp -= threshold;
            level++;
            levelsGained++;
            
            skillPoints++;
            addDiamonds(5);
            if (questManager != null) questManager.addProgress("level", 1);
            if (onLevelUpCallback != null) onLevelUpCallback.run();
        }
        return levelsGained;
    }

    public void gainGold(int amount, float equipmentGoldBonus, QuestManager questManager) {
        if (amount > 0) {
            float bonus = 1f + equipmentGoldBonus / 100f;
            amount = (int)(amount * bonus);
            if (questManager != null) questManager.addProgress("gold", amount);
        }
        gold = Math.max(0, gold + amount);
    }

    public void incrementFishCaught() { 
        fishCaught++; 
        if (fishCaught % 10 == 0) addDiamonds(5);
    }

    // Getters / Setters
    public int getLevel()              { return level; }
    public void setLevel(int l)        { this.level = l; }

    public int getCurrentXp()          { return currentXp; }
    public void setCurrentXp(int xp)   { this.currentXp = xp; }

    public int getGold()               { return gold; }
    public void setGold(int g)         { this.gold = g; }

    public int getFishCaught()         { return fishCaught; }
    public void setFishCaught(int c)   { this.fishCaught = c; }

    public int getSkillPoints()        { return skillPoints; }
    public void setSkillPoints(int sp) { this.skillPoints = sp; }
    public boolean consumeSkillPoints(int cost) {
        if (skillPoints >= cost) {
            skillPoints -= cost;
            return true;
        }
        return false;
    }

    public int getDiamonds()           { return diamonds; }
    public void setDiamonds(int d)     { diamonds = d; }
    public void addDiamonds(int a)     { diamonds += a; }

    public int getGachaTickets()       { return gachaTickets; }
    public void setGachaTickets(int t) { this.gachaTickets = t; }
    public void addGachaTickets(int t) { this.gachaTickets += t; }

    public int getXpToNextLevel()      { return xpRequired(level) - currentXp; }
    public int getXpForCurrentLevel()  { return xpRequired(level); }
}
