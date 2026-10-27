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
    private int premiumTickets = 0;
    private int eventPoints = 0;
    
    private int totalLoginDaysThisMonth = 0;
    private long lastLoginTimestamp = 0;
    private long lastEventMissionResetTimestamp = 0;
    private boolean halloweenResetDone = false;

    public static final int MAX_LEVEL = 100;

    public static int xpRequired(int level) {
        if (level >= MAX_LEVEL) return Integer.MAX_VALUE;
        // Công thức đa thức mượt mà cân bằng: XP(Level) = 35 * Level^1.65 + 40
        // Lv 1: 75 XP | Lv 10: 1,600 XP (Tổng tích lũy lên Lv 10: 5,485 XP ~ 200 con cá Ao Làng)
        // Lv 20: 4,950 XP (Tổng tích lũy lên Lv 20: ~35,345 XP ~ 575 con cá Bãi Biển)
        int val = (int) (35.0 * Math.pow(level, 1.65) + 40.0);
        return val >= 100 ? (val / 10) * 10 : val;
    }

    public int gainXp(int xp, float equipmentExpBonus, String activeSetName, QuestManager questManager, Runnable onLevelUpCallback) {
        if (xp <= 0) return 0;
        
        float bonus = 1f + equipmentExpBonus / 100f;
        if ("Tập Sự".equals(activeSetName) || "Dạ Hội Bí Ngô".equals(activeSetName)) bonus += 0.2f;
        int finalXp = (int)(xp * bonus);
        
        currentXp += finalXp;
        int levelsGained = 0;
        int threshold;
        
        while (level < MAX_LEVEL && (threshold = xpRequired(level)) > 0 && currentXp >= threshold) {
            currentXp -= threshold;
            level++;
            levelsGained++;
            
            skillPoints++;
            // Thưởng cấp: Mỗi mốc 5 cấp (5, 10, 15, 20...) thưởng 10 KC, các cấp thường thưởng 2 KC
            if (level % 5 == 0) {
                addDiamonds(10);
            } else {
                addDiamonds(2);
            }
            if (questManager != null) questManager.addProgress("level", 1);
            if (onLevelUpCallback != null) onLevelUpCallback.run();
        }
        if (level >= MAX_LEVEL) {
            currentXp = 0;
        }
        return levelsGained;
    }

    public void gainGold(int amount, float equipmentGoldBonus, QuestManager questManager) {
        if (amount > 0) {
            float bonus = 1f + equipmentGoldBonus / 100f;
            amount = (int)(amount * bonus);
            if (questManager != null) questManager.addProgress("gold", amount);
        }
        long newGold = (long) gold + amount;
        if (newGold > Integer.MAX_VALUE) {
            gold = Integer.MAX_VALUE;
        } else if (newGold < 0) {
            gold = 0;
        } else {
            gold = (int) newGold;
        }
    }

    public void incrementFishCaught() { 
        fishCaught++; 
        // Thưởng kiên trì cần thủ: Mỗi 25 con cá câu thành công thưởng +3 KC (trước đây mỗi 10 con +5 KC)
        if (fishCaught % 25 == 0) addDiamonds(3);
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

    public int getPremiumTickets()       { return premiumTickets; }
    public void setPremiumTickets(int t) { this.premiumTickets = t; }
    public void addPremiumTickets(int t) { this.premiumTickets += t; }

    public int getEventPoints()          { return eventPoints; }
    public void setEventPoints(int p)    { this.eventPoints = p; }
    public void addEventPoints(int p)    { this.eventPoints += p; }

    public int getTotalLoginDaysThisMonth() { return totalLoginDaysThisMonth; }
    public void setTotalLoginDaysThisMonth(int days) { this.totalLoginDaysThisMonth = days; }

    public long getLastLoginTimestamp() { return lastLoginTimestamp; }
    public void setLastLoginTimestamp(long ts) { this.lastLoginTimestamp = ts; }

    public long getLastEventMissionResetTimestamp() { return lastEventMissionResetTimestamp; }
    public void setLastEventMissionResetTimestamp(long ts) { this.lastEventMissionResetTimestamp = ts; }

    public boolean isHalloweenResetDone() { return halloweenResetDone; }
    public void setHalloweenResetDone(boolean done) { this.halloweenResetDone = done; }

    public int getXpToNextLevel()      { return xpRequired(level) - currentXp; }
    public int getXpForCurrentLevel()  { return xpRequired(level); }
}
