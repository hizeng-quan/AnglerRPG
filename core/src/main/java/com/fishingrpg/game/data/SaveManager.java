package com.fishingrpg.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.entities.equipment.Equipment;
import com.fishingrpg.game.entities.equipment.EquipmentGenerator;
import com.fishingrpg.game.entities.equipment.EquipmentType;
import com.fishingrpg.game.entities.equipment.Substat;
import com.fishingrpg.game.skills.Skill;
import com.fishingrpg.game.systems.QuestManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SaveManager {

    private static final String PREF_NAME    = "AnglerRPG_Save";
    private static final String TAG          = "SaveManager";
    /** Increment when adding new fields that are backwards incompatible. */
    private static final int    SAVE_VERSION = 3;

    // =========================================================================
    // Save
    // =========================================================================

    public static void savePlayer(Player player) {
        Preferences prefs = Gdx.app.getPreferences(PREF_NAME);
        prefs.putInteger("saveVersion",  SAVE_VERSION);
        prefs.putInteger("level",        player.getLevel());
        prefs.putInteger("currentXp",    player.getCurrentXp());
        prefs.putInteger("gold",         player.getGold());
        prefs.putInteger("diamonds",     player.getDiamonds());
        prefs.putInteger("gachaTickets", player.getGachaTickets());
        prefs.putInteger("fishCaught",   player.getFishCaught());
        prefs.putInteger("skillPoints",  player.getSkillPoints());

        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);

        prefs.putString("inventory",         json.toJson(player.getInventory()));
        prefs.putString("claimedCollections",json.toJson(player.getClaimedCollections()));
        prefs.putString("equippedActives",   json.toJson(player.getEquippedActives()));
        prefs.putString("equippedPassives",  json.toJson(player.getEquippedPassives()));
        prefs.putString("currentMap",        player.getCurrentMap());

        // Skills
        Map<String, Integer> skillLevels = new HashMap<>();
        for (Skill s : player.getSkillTree().getAllSkills()) {
            skillLevels.put(s.id, s.getCurrentLevel());
        }
        prefs.putString("skills", json.toJson(skillLevels));

        // Quests
        if (player.getQuestManager() != null) {
            prefs.putString("questManager", json.toJson(player.getQuestManager()));
        }

        // Equipment
        prefs.putString("equipmentInventory", json.toJson(player.getEquipmentInventory()));
        prefs.putString("equippedRod",  json.toJson(player.getEquippedRod(),  Equipment.class));
        prefs.putString("equippedLine", json.toJson(player.getEquippedLine(), Equipment.class));
        prefs.putString("equippedHook", json.toJson(player.getEquippedHook(), Equipment.class));
        prefs.putInteger("pityRare",      player.pityRare);
        prefs.putInteger("pityEpic",      player.pityEpic);
        prefs.putInteger("pityLegendary", player.pityLegendary);
        
        // Buffs
        prefs.putString("playerBuffs", json.toJson(player.getBuffs()));

        prefs.flush();
    }

    // =========================================================================
    // Load
    // =========================================================================

    @SuppressWarnings("unchecked")
    public static void loadPlayer(Player player) {
        Preferences prefs = Gdx.app.getPreferences(PREF_NAME);
        if (!prefs.contains("level")) return; // No save data

        // Check save version
        int savedVersion = prefs.getInteger("saveVersion", 1);
        if (savedVersion < SAVE_VERSION) {
            Gdx.app.log(TAG, "Old save version (" + savedVersion + " < " + SAVE_VERSION
                    + "). Some new fields will use default values.");
        }

        // Primitive fields — safe, no try-catch needed
        player.setLevel(prefs.getInteger("level"));
        player.setCurrentXp(prefs.getInteger("currentXp"));
        player.setGold(prefs.getInteger("gold"));
        player.setDiamonds(prefs.getInteger("diamonds", 0));
        player.setGachaTickets(prefs.getInteger("gachaTickets", 0));
        player.setFishCaught(prefs.getInteger("fishCaught"));
        player.setCurrentMap(prefs.getString("currentMap", "ao_lang"));
        player.setSkillPoints(prefs.getInteger("skillPoints", 0));

        Json json = new Json();

        // --- Inventory ---
        try {
            Map<String, Integer> inv = json.fromJson(HashMap.class, prefs.getString("inventory", "{}"));
            if (inv != null) {
                player.getInventory().clear();
                for (Map.Entry<String, Integer> e : inv.entrySet()) {
                    if (e.getValue() > 0) player.addItem(e.getKey(), e.getValue());
                }
            }
        } catch (Exception e) {
            Gdx.app.error(TAG, "Error parsing inventory — keeping defaults.", e);
        }

        // --- Skills ---
        try {
            Map<String, Integer> skillLevels = json.fromJson(HashMap.class, prefs.getString("skills", "{}"));
            if (skillLevels != null) {
                for (Map.Entry<String, Integer> e : skillLevels.entrySet()) {
                    Skill s = player.getSkillTree().getSkill(e.getKey());
                    if (s != null) s.setCurrentLevel(e.getValue());
                }
            }
        } catch (Exception e) {
            Gdx.app.error(TAG, "Error parsing skills — keeping defaults.", e);
        }

        // --- Equipped skill slots ---
        try {
            String[] ea = json.fromJson(String[].class, prefs.getString("equippedActives", "[]"));
            if (ea != null) {
                for (int i = 0; i < Math.min(ea.length, player.getEquippedActives().length); i++) {
                    player.getEquippedActives()[i] = ea[i];
                }
            }
            String[] ep = json.fromJson(String[].class, prefs.getString("equippedPassives", "[]"));
            if (ep != null) {
                for (int i = 0; i < Math.min(ep.length, player.getEquippedPassives().length); i++) {
                    player.getEquippedPassives()[i] = ep[i];
                }
            }
        } catch (Exception e) {
            Gdx.app.error(TAG, "Error parsing equipped slots — keeping defaults.", e);
        }

        // --- Claimed collections ---
        try {
            HashSet claimed = json.fromJson(HashSet.class, prefs.getString("claimedCollections", "[]"));
            if (claimed != null) player.setClaimedCollections(claimed);
        } catch (Exception e) {
            Gdx.app.error(TAG, "Error parsing claimedCollections.", e);
        }

        // --- Quest Manager ---
        try {
            String questJson = prefs.getString("questManager", null);
            if (questJson != null && !questJson.isEmpty()) {
                QuestManager qm = json.fromJson(QuestManager.class, questJson);
                if (qm != null) {
                    qm.initIfEmpty();
                    player.setQuestManager(qm);
                }
            }
        } catch (Exception e) {
            Gdx.app.error(TAG, "Error parsing questManager — resetting to defaults.", e);
        }
        if (player.getQuestManager() == null) {
            player.setQuestManager(new QuestManager());
            player.getQuestManager().initIfEmpty();
        }
        player.getQuestManager().checkResets();

        // --- Equipment ---
        try {
            String eqInvJson = prefs.getString("equipmentInventory", "[]");
            if (!eqInvJson.equals("[]")) {
                ArrayList<Equipment> eqInv = json.fromJson(ArrayList.class, Equipment.class, eqInvJson);
                if (eqInv != null) {
                    player.getEquipmentInventory().clear();
                    player.getEquipmentInventory().addAll(eqInv);
                }
            }

            // Re-apply new formula for old equipment
            for (Equipment eq : player.getEquipmentInventory()) {
                int mult = EquipmentGenerator.getSetPoints(eq.setName)
                         + EquipmentGenerator.getRarityPoints(eq.rarity);
                if (eq.type == EquipmentType.ROD) {
                    eq.mainStat1 = 10f + mult * 2f;
                    eq.mainStat2 = 50f + mult * 15f;
                } else if (eq.type == EquipmentType.LINE) {
                    eq.mainStat1 = 50f + mult * 20f;
                    eq.mainStat2 = 2f  + mult * 0.4f;
                } else if (eq.type == EquipmentType.HOOK) {
                    eq.mainStat1 = 5f  + mult * 1.0f;
                    eq.mainStat2 = 10f + mult * 2.0f;
                }
                for (Substat sub : eq.substats) {
                    sub.value = Math.max(1f, Math.round(2f + mult / 2f));
                }
            }

            // Re-equip exact instance from inventory
            Equipment rod  = json.fromJson(Equipment.class, prefs.getString("equippedRod",  "null"));
            Equipment line = json.fromJson(Equipment.class, prefs.getString("equippedLine", "null"));
            Equipment hook = json.fromJson(Equipment.class, prefs.getString("equippedHook", "null"));
            for (Equipment eq : player.getEquipmentInventory()) {
                if (rod  != null && eq.instanceId.equals(rod.instanceId))  player.equip(eq);
                if (line != null && eq.instanceId.equals(line.instanceId)) player.equip(eq);
                if (hook != null && eq.instanceId.equals(hook.instanceId)) player.equip(eq);
            }
        } catch (Exception e) {
            Gdx.app.error(TAG, "Error parsing equipment — keeping empty.", e);
        }

        player.pityRare      = prefs.getInteger("pityRare",      0);
        player.pityEpic      = prefs.getInteger("pityEpic",      0);
        player.pityLegendary = prefs.getInteger("pityLegendary", 0);

        // --- Buffs ---
        try {
            String buffsJson = prefs.getString("playerBuffs", null);
            if (buffsJson != null && !buffsJson.isEmpty()) {
                com.fishingrpg.game.entities.PlayerBuffs savedBuffs = json.fromJson(com.fishingrpg.game.entities.PlayerBuffs.class, buffsJson);
                if (savedBuffs != null) {
                    com.fishingrpg.game.entities.PlayerBuffs buffs = player.getBuffs();
                    buffs.setBaitBuffActive(savedBuffs.isBaitBuffActive());
                    buffs.setGoldBuffCharges(savedBuffs.getGoldBuffCharges());
                    buffs.setExpBuffCharges(savedBuffs.getExpBuffCharges());
                    buffs.setFishingNetActive(savedBuffs.isFishingNetActive());
                    buffs.setRiskyBaitActive(savedBuffs.isRiskyBaitActive());
                    buffs.setOverclockPotionActive(savedBuffs.isOverclockPotionActive());
                }
            }
        } catch (Exception e) {
            Gdx.app.error(TAG, "Error parsing playerBuffs — keeping defaults.", e);
        }

        player.applySkillBonuses();
    }
}
