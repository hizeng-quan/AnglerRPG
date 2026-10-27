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
    private int     whetstoneCharges      = 0;
    private int     treasureMagnetCharges = 0;
    private int     sonarCharges          = 0;

    // --- Halloween HLW-05 Exclusives ---
    private int     halloweenCandyCharges   = 0;
    private float   halloweenCandyTimer     = 0f; // 900s (15 phut)
    private int     pumpkinLanternCharges   = 0;  // 3 lan Re-Hook +30% time
    private int     freeStaminaSkillCharges = 0;  // 3 lan dung skill 0 the luc
    private float   bloodMoonTalismanTimer  = 0f; // 15s +10% True damage in-combat
    private boolean ghostCandleActive       = false; // Cuu 1 lan dut day
    private float   manualBloodMoonTimer    = 0f; // 900s (15 phut) goi Blood Moon

    public void update(float delta) {
        if (halloweenCandyTimer > 0f) {
            halloweenCandyTimer = Math.max(0f, halloweenCandyTimer - delta);
        }
        if (bloodMoonTalismanTimer > 0f) {
            bloodMoonTalismanTimer = Math.max(0f, bloodMoonTalismanTimer - delta);
        }
        if (manualBloodMoonTimer > 0f) {
            manualBloodMoonTimer = Math.max(0f, manualBloodMoonTimer - delta);
        }
    }

    // --- Halloween Trick-or-Treat Candy ---
    public int   getHalloweenCandyCharges()                 { return halloweenCandyCharges; }
    public void  addHalloweenCandyCharges(int charges)      { halloweenCandyCharges += charges; }
    public void  consumeHalloweenCandyCharge()              { if (halloweenCandyCharges > 0) halloweenCandyCharges--; }
    public void  setHalloweenCandyCharges(int v)            { halloweenCandyCharges = Math.max(0, v); }
    public float getHalloweenCandyTimer()                   { return halloweenCandyTimer; }
    public void  setHalloweenCandyTimer(float v)            { halloweenCandyTimer = Math.max(0f, v); }
    public void  addHalloweenCandyTimer(float sec)          { halloweenCandyTimer += sec; }

    // --- Pumpkin Lantern (Quang minh trừ tà) ---
    public int   getPumpkinLanternCharges()                 { return pumpkinLanternCharges; }
    public void  addPumpkinLanternCharges(int charges)      { pumpkinLanternCharges += charges; }
    public void  consumePumpkinLanternCharge()              { if (pumpkinLanternCharges > 0) pumpkinLanternCharges--; }
    public void  setPumpkinLanternCharges(int v)            { pumpkinLanternCharges = Math.max(0, v); }

    // --- Witch / Ghost Potion (Độc Dược Ma Quái) ---
    public int   getFreeStaminaSkillCharges()               { return freeStaminaSkillCharges; }
    public void  addFreeStaminaSkillCharges(int charges)    { freeStaminaSkillCharges += charges; }
    public void  consumeFreeStaminaSkillCharge()            { if (freeStaminaSkillCharges > 0) freeStaminaSkillCharges--; }
    public void  setFreeStaminaSkillCharges(int v)          { freeStaminaSkillCharges = Math.max(0, v); }

    // --- Blood Moon Talisman (Bùa Huyết Nguyệt) ---
    public float getBloodMoonTalismanTimer()                { return bloodMoonTalismanTimer; }
    public void  setBloodMoonTalismanTimer(float v)         { bloodMoonTalismanTimer = Math.max(0f, v); }
    public float getManualBloodMoonTimer()                  { return manualBloodMoonTimer; }
    public void  setManualBloodMoonTimer(float v)           { manualBloodMoonTimer = Math.max(0f, v); }

    // --- Ghost Candle (Nến Hồn Ma Bảo Hộ) ---
    public boolean isGhostCandleActive()                    { return ghostCandleActive; }
    public void    setGhostCandleActive(boolean v)          { ghostCandleActive = v; }

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

    // --- Whetstone (Đá Mài Lưỡi Câu) ---
    public int  getWhetstoneCharges()                    { return whetstoneCharges; }
    public void addWhetstoneCharges(int charges)         { whetstoneCharges += charges; }
    public void consumeWhetstoneCharge()                 { if (whetstoneCharges > 0) whetstoneCharges--; }
    public void setWhetstoneCharges(int v)               { whetstoneCharges = Math.max(0, v); }

    // --- Treasure Magnet (Nam Châm Kho Báu) ---
    public int  getTreasureMagnetCharges()               { return treasureMagnetCharges; }
    public void addTreasureMagnetCharges(int charges)    { treasureMagnetCharges += charges; }
    public void consumeTreasureMagnetCharge()            { if (treasureMagnetCharges > 0) treasureMagnetCharges--; }
    public void setTreasureMagnetCharges(int v)          { treasureMagnetCharges = Math.max(0, v); }

    // --- Sonar Hydro-Goggles (Kính Nhìn Xuyên Nước) ---
    public int  getSonarCharges()                        { return sonarCharges; }
    public void addSonarCharges(int charges)             { sonarCharges += charges; }
    public void consumeSonarCharge()                     { if (sonarCharges > 0) sonarCharges--; }
    public void setSonarCharges(int v)                   { sonarCharges = Math.max(0, v); }
}
