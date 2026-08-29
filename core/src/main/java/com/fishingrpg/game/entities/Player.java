package com.fishingrpg.game.entities;

import com.badlogic.gdx.math.MathUtils;
import com.fishingrpg.game.data.ItemDatabase;
import com.fishingrpg.game.entities.equipment.Equipment;
import com.fishingrpg.game.entities.equipment.EquipmentType;
import com.fishingrpg.game.entities.equipment.Substat;
import com.fishingrpg.game.skills.SkillTree;
import com.fishingrpg.game.systems.QuestManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Player -- exists throughout the entire session.
 * Acts as a Facade pattern, delegating logic to smaller components.
 */
public class Player {

    // Components
    private final PlayerCombat combat;
    private final PlayerProgression progression;
    private final PlayerEquipment equipment;
    private final PlayerBuffs buffs;
    private final PlayerEconomy economy;
    private final SkillTree skillTree;
    private final Inventory inventory;
    
    private QuestManager questManager;
    private String currentMap = "ao_lang";
    private java.util.HashSet<String> claimedCollections = new java.util.HashSet<>();

    private String[] equippedActives = new String[4];
    private String[] equippedPassives = new String[2];

    public boolean justFilledStamina = false;
    
    // Gacha pity counters
    public int pityRare = 0;
    public int pityEpic = 0;
    public int pityLegendary = 0;

    public Player() { this(100f); }

    public Player(float maxStamina) {
        this.combat = new PlayerCombat(maxStamina);
        this.progression = new PlayerProgression();
        this.equipment = new PlayerEquipment();
        this.buffs = new PlayerBuffs();
        this.economy = new PlayerEconomy();
        this.skillTree = new SkillTree();
        this.inventory = new Inventory();
        
        inventory.addItem("Mồi Thơm", 1);
        inventory.addItem("Bánh Mì", 3);
        
        this.questManager = new QuestManager();
        this.questManager.initIfEmpty();
        applySkillBonuses();
    }

    // =========================================================================
    // Components Getters (for serialization & deep access)
    // =========================================================================
    public PlayerCombat getCombat()           { return combat; }
    public PlayerProgression getProgression() { return progression; }
    public PlayerEquipment getEquipment()     { return equipment; }
    public PlayerBuffs getBuffs()             { return buffs; }
    public PlayerEconomy getEconomy()         { return economy; }

    // =========================================================================
    // PlayerCombat Delegation
    // =========================================================================
    public void update(float delta) {
        float passiveRegenBonus = isSkillEquipped("hoi_phuc_nhanh") ? skillTree.getSkill("hoi_phuc_nhanh").getEffect() : 0f;
        combat.update(delta, passiveRegenBonus);
        if (combat.justFilledStamina) {
            this.justFilledStamina = true;
            combat.justFilledStamina = false;
        }
    }
    public void addStaminaRegen(float totalAmount, float ratePerSecond) { combat.addStaminaRegen(totalAmount, ratePerSecond); }
    public void resetForNewEncounter() { combat.resetForNewEncounter(); }
    public void increaseTension(float amount) { combat.increaseTension(amount); }
    public void decreaseTension(float amount) { combat.decreaseTension(amount); }
    public boolean isLineBroken() { return combat.isLineBroken(); }
    public boolean consumeStamina(float amount) { return combat.consumeStamina(amount); }
    
    public float getCurrentStamina()     { return combat.getCurrentStamina(); }
    public float getMaxStamina()         { return combat.getMaxStamina(); }
    public float getCurrentLineTension() { return combat.getCurrentLineTension(); }
    public float getMaxLineTension()     { return combat.getMaxLineTension(); }

    // =========================================================================
    // PlayerProgression Delegation
    // =========================================================================
    public int gainXp(int xp) { 
        return progression.gainXp(xp, equipment.getTotalSubstat(Substat.Type.EXP_BONUS), equipment.getActiveSetName(), questManager, () -> applySkillBonuses()); 
    }
    public void gainGold(int amount) { progression.gainGold(amount, equipment.getTotalSubstat(Substat.Type.GOLD_BONUS), questManager); }
    public void incrementFishCaught() { progression.incrementFishCaught(); }

    public int getLevel()              { return progression.getLevel(); }
    public void setLevel(int l)        { progression.setLevel(l); }
    public int getCurrentXp()          { return progression.getCurrentXp(); }
    public void setCurrentXp(int xp)   { progression.setCurrentXp(xp); }
    public int getGold()               { return progression.getGold(); }
    public void setGold(int g)         { progression.setGold(g); }
    public int getFishCaught()         { return progression.getFishCaught(); }
    public void setFishCaught(int c)   { progression.setFishCaught(c); }
    public int getSkillPoints()        { return progression.getSkillPoints(); }
    public void setSkillPoints(int sp) { progression.setSkillPoints(sp); }
    
    public int getDiamonds()           { return progression.getDiamonds(); }
    public void setDiamonds(int d)     { progression.setDiamonds(d); }
    public void addDiamonds(int a)     { progression.addDiamonds(a); }
    public int getGachaTickets()       { return progression.getGachaTickets(); }
    public void setGachaTickets(int t) { progression.setGachaTickets(t); }
    public void addGachaTickets(int t) { progression.addGachaTickets(t); }

    public int getXpToNextLevel()      { return progression.getXpToNextLevel(); }
    public int getXpForCurrentLevel()  { return progression.getXpForCurrentLevel(); }

    // =========================================================================
    // PlayerEquipment Delegation
    // =========================================================================
    public void addEquipment(Equipment eq) { equipment.addToInventory(eq); }
    public void equip(Equipment eq) { equipment.equip(eq); applySkillBonuses(); }
    public void unequip(EquipmentType type) { equipment.unequip(type); applySkillBonuses(); }
    
    public List<Equipment> getEquipmentInventory() { return equipment.getInventory(); }
    public Equipment getEquippedRod() { return equipment.getEquippedRod(); }
    public Equipment getEquippedLine() { return equipment.getEquippedLine(); }
    public Equipment getEquippedHook() { return equipment.getEquippedHook(); }
    public boolean isSetBonusActive() { return equipment.isSetBonusActive(); }
    public float getEquipmentTotalStat(Substat.Type type) { return equipment.getTotalSubstat(type); }

    public float getBasePullDamage() { return PlayerStatCalculator.getBasePullDamage(equipment); }
    public float getRarityBoost() { return equipment.getRarityBoost(); }
    public float getBaitRetention() { return equipment.getHookBaitRetention(); }

    // =========================================================================
    // PlayerBuffs Delegation
    // =========================================================================
    public boolean isBaitBuffActive() { return buffs.isBaitBuffActive(); }
    public void setBaitBuffActive(boolean active) { buffs.setBaitBuffActive(active); }
    public int getGoldBuffCharges() { return buffs.getGoldBuffCharges(); }
    public void addGoldBuffCharges(int charges) { buffs.addGoldBuffCharges(charges); }
    public void consumeGoldBuffCharge() { buffs.consumeGoldBuffCharge(); }
    public int getExpBuffCharges() { return buffs.getExpBuffCharges(); }
    public void addExpBuffCharges(int charges) { buffs.addExpBuffCharges(charges); }
    public void consumeExpBuffCharge() { buffs.consumeExpBuffCharge(); }
    public boolean isFishingNetActive() { return buffs.isFishingNetActive(); }
    public void setFishingNetActive(boolean active) { buffs.setFishingNetActive(active); }
    public boolean isRiskyBaitActive() { return buffs.isRiskyBaitActive(); }
    public void setRiskyBaitActive(boolean active) { buffs.setRiskyBaitActive(active); }
    public boolean isOverclockPotionActive() { return buffs.isOverclockPotionActive(); }
    public void setOverclockPotionActive(boolean active) { buffs.setOverclockPotionActive(active); }

    // =========================================================================
    // PlayerEconomy Delegation
    // =========================================================================
    public Map<String, Integer> getShopDiscounts() { return economy.getShopDiscounts(); }
    public void recordSuccessfulCatch() { economy.recordSuccessfulCatch(); }

    // =========================================================================
    // Skills & Inventory
    // =========================================================================
    public SkillTree getSkillTree() { return skillTree; }
    public Map<String, Integer> getInventory() { return inventory.getItems(); }
    public Inventory getInventoryObject() { return inventory; }
    public void addItem(String name, int count) { inventory.addItem(name, count); }
    public boolean hasItem(String name) { return inventory.hasItem(name); }
    public void consumeItem(String name) { inventory.consumeItem(name); }

    public String[] getEquippedActives()   { return equippedActives;    }
    public String[] getEquippedPassives()  { return equippedPassives;   }

    public boolean upgradeSkill(String skillId) {
        if (progression.getSkillPoints() > 0) {
            int cost = skillTree.upgrade(skillId, progression.getSkillPoints());
            if (cost > 0) {
                progression.consumeSkillPoints(cost);
                applySkillBonuses();
                return true;
            }
        }
        return false;
    }

    public boolean isSkillEquipped(String skillId) {
        if (skillId == null) return false;
        for (String id : equippedActives) {
            if (skillId.equals(id)) return true;
        }
        for (String id : equippedPassives) {
            if (skillId.equals(id)) return true;
        }
        return false;
    }

    public void applySkillBonuses() {
        PlayerStatCalculator.applySkillBonuses(this, combat, equipment, skillTree);
    }

    // =========================================================================
    // Misc
    // =========================================================================
    public QuestManager getQuestManager() { return questManager; }
    public void setQuestManager(QuestManager qm) { this.questManager = qm; }
    
    public String getCurrentMap() { return currentMap; }
    public void setCurrentMap(String mapId) { this.currentMap = mapId; }
    
    public java.util.HashSet<String> getClaimedCollections() { return claimedCollections; }
    public void claimCollection(String collectionId) { claimedCollections.add(collectionId); }
    public void setClaimedCollections(java.util.HashSet<String> collections) { this.claimedCollections = collections; }
}
