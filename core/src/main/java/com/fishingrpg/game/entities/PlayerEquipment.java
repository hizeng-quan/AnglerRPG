package com.fishingrpg.game.entities;

import com.fishingrpg.game.entities.equipment.Equipment;
import com.fishingrpg.game.entities.equipment.EquipmentType;
import com.fishingrpg.game.entities.equipment.Substat;
import java.util.ArrayList;
import java.util.List;

/**
 * Player's equipment inventory and currently equipped slots.
 * Manages equip/unequip, set bonuses, and substat aggregation.
 */
public class PlayerEquipment {

    private final List<Equipment> inventory = new ArrayList<>();
    private Equipment equippedRod  = null;
    private Equipment equippedLine = null;
    private Equipment equippedHook = null;

    // =========================================================================
    // Inventory
    // =========================================================================

    public List<Equipment> getInventory()           { return inventory; }
    public void addToInventory(Equipment eq)        { if (eq != null) inventory.add(eq); }

    // =========================================================================
    // Equip / Unequip
    // =========================================================================

    public void equip(Equipment eq) {
        if (eq == null) return;
        switch (eq.type) {
            case ROD:  equippedRod  = eq; break;
            case LINE: equippedLine = eq; break;
            case HOOK: equippedHook = eq; break;
        }
    }

    public void unequip(EquipmentType type) {
        switch (type) {
            case ROD:  equippedRod  = null; break;
            case LINE: equippedLine = null; break;
            case HOOK: equippedHook = null; break;
        }
    }

    public Equipment getEquippedRod()  { return equippedRod;  }
    public Equipment getEquippedLine() { return equippedLine; }
    public Equipment getEquippedHook() { return equippedHook; }

    // =========================================================================
    // Set Bonus
    // =========================================================================

    public boolean isSetBonusActive() {
        if (equippedRod == null || equippedLine == null || equippedHook == null) return false;
        return equippedRod.setName.equals(equippedLine.setName)
            && equippedLine.setName.equals(equippedHook.setName);
    }

    /**
     * Active set name. Returns null if not a full 3-piece set.
     * Used to check set names in FishingManager / Player.
     */
    public String getActiveSetName() {
        return isSetBonusActive() && equippedRod != null ? equippedRod.setName : null;
    }

    // =========================================================================
    // Substat tổng hợp
    // =========================================================================

    /**
     * Total sum of a specific substat from all equipped gear,
     * including Thần Thoại set bonus (+35%).
     */
    public float getTotalSubstat(Substat.Type type) {
        float total = sumSubstat(equippedRod, type)
                    + sumSubstat(equippedLine, type)
                    + sumSubstat(equippedHook, type);
        if (isSetBonusActive() && "Thần Thoại".equals(getActiveSetName())) {
            total *= 1.35f;
        }
        return total;
    }

    private float sumSubstat(Equipment eq, Substat.Type type) {
        if (eq == null) return 0f;
        float sum = 0f;
        for (Substat s : eq.substats) {
            if (s.type == type) sum += s.value;
        }
        return sum;
    }

    // =========================================================================
    // Main stats of each slot (used by PlayerStatCalculator)
    // =========================================================================

    /** Rod mainStat1: Base pull damage */
    public float getRodDamageBonus()    { return equippedRod  != null ? equippedRod.mainStat1  : 0f; }
    /** Rod mainStat2: Max Stamina bonus */
    public float getRodStaminaBonus()   { return equippedRod  != null ? equippedRod.mainStat2  : 0f; }
    /** Line mainStat1: Max Tension bonus */
    public float getLineTensionBonus()  { return equippedLine != null ? equippedLine.mainStat1 : 0f; }
    /** Line mainStat2: Line break resistance */
    public float getLineBreakResist()   { return equippedLine != null ? equippedLine.mainStat2 : 0f; }
    /** Hook mainStat1: Rare fish chance bonus */
    public float getHookRarityBonus()   { return equippedHook != null ? equippedHook.mainStat1 : 0f; }
    /** Hook mainStat2: Bait retention chance */
    public float getHookBaitRetention() { return equippedHook != null ? equippedHook.mainStat2 : 0f; }

    /**
     * Rare fish chance bonus from hook mainStat1 + Hải Tặc set bonus.
     */
    public float getRarityBoost() {
        float b = getHookRarityBonus();
        if (isSetBonusActive() && "Hải Tặc".equals(getActiveSetName())) b += 15f;
        return b;
    }
}
