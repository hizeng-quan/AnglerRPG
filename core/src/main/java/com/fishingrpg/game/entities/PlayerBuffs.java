package com.fishingrpg.game.entities;

/**
 * Temporary buff state from items.
 * Buffs are reset on exit if not saved - see SaveManager.
 */
public class PlayerBuffs {

    private boolean baitBuffActive        = false;
    private int     goldBuffCharges       = 0;
    private int     expBuffCharges        = 0;
    private boolean fishingNetActive      = false;
    private boolean riskyBaitActive       = false;
    private boolean overclockPotionActive = false;

    // --- Sweet Bait ---
    public boolean isBaitBuffActive()           { return baitBuffActive; }
    public void    setBaitBuffActive(boolean v) { baitBuffActive = v; }

    // --- Golden Amulet ---
    public int  getGoldBuffCharges()            { return goldBuffCharges; }
    public void addGoldBuffCharges(int charges) { goldBuffCharges += charges; }
    public void consumeGoldBuffCharge()         { if (goldBuffCharges > 0) goldBuffCharges--; }
    public void setGoldBuffCharges(int v)       { goldBuffCharges = Math.max(0, v); }

    // --- Book of Wisdom ---
    public int  getExpBuffCharges()             { return expBuffCharges; }
    public void addExpBuffCharges(int charges)  { expBuffCharges += charges; }
    public void consumeExpBuffCharge()          { if (expBuffCharges > 0) expBuffCharges--; }
    public void setExpBuffCharges(int v)        { expBuffCharges = Math.max(0, v); }

    // --- Fishing Net ---
    public boolean isFishingNetActive()            { return fishingNetActive; }
    public void    setFishingNetActive(boolean v)  { fishingNetActive = v; }

    // --- Risky Bait ---
    public boolean isRiskyBaitActive()             { return riskyBaitActive; }
    public void    setRiskyBaitActive(boolean v)   { riskyBaitActive = v; }

    // --- Overclock Potion ---
    public boolean isOverclockPotionActive()              { return overclockPotionActive; }
    public void    setOverclockPotionActive(boolean v)    { overclockPotionActive = v; }
}
