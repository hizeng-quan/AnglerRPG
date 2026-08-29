package com.fishingrpg.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;
import com.fishingrpg.game.entities.Fish;
import com.fishingrpg.game.entities.Fish.Rarity;
import com.fishingrpg.game.entities.Player;
import java.util.ArrayList;

/**
 * Danh sach tat ca loai ca trong game duoc doc tu file json.
 */
public class FishDatabase {

    public static class FishTemplate {
        public String name;
        public float maxHp;
        public float pullStrength;
        public Rarity rarity;
        public float baseWeightKg;
        public float baseLengthCm;
        public int baseXp;
        public int baseGold;
        public String map;
        public java.util.ArrayList<com.fishingrpg.game.entities.FishSkill> skills;
    }

    private static ArrayList<FishTemplate> templates;
    private static final float[] RARITY_WEIGHTS = { 60f, 60f, 25f, 12f, 3f, 0.5f };
    private static final float TOTAL_WEIGHT;

    static {
        float t = 0;
        for (float w : RARITY_WEIGHTS) t += w;
        TOTAL_WEIGHT = t;
    }

    public static void load() {
        if (templates != null) return;
        Json json = new Json();
        templates = json.fromJson(ArrayList.class, FishTemplate.class, Gdx.files.internal("data/fishes.json"));
    }

    public static java.util.List<FishTemplate> getAllFishes() {
        if (templates == null) load();
        return templates;
    }

    public static Fish getRandomFish(Player player) {
        if (templates == null) load();
        int rarityIdx = weightedRandom(player);
        Rarity targetRarity = Rarity.values()[Math.min(rarityIdx, Rarity.values().length - 1)];
        
        java.util.ArrayList<FishTemplate> filtered = new java.util.ArrayList<>();
        String currentMap = player != null ? player.getCurrentMap() : "ao_lang";
        for (FishTemplate t : templates) {
            if (t.rarity == targetRarity && (t.map == null || t.map.equals(currentMap))) {
                filtered.add(t);
            }
        }
        
        if (filtered.isEmpty()) {
            for (FishTemplate t : templates) {
                if (t.map == null || t.map.equals(currentMap)) filtered.add(t);
            }
        }
        
        if (filtered.isEmpty()) return buildFish(templates.get(MathUtils.random(templates.size() - 1)));
        return buildFish(filtered.get(MathUtils.random(filtered.size() - 1)));
    }

    public static Fish getFishByName(String name) {
        if (templates == null) load();
        for (FishTemplate t : templates) {
            if (t.name.equals(name)) return buildFish(t);
        }
        return null;
    }

    private static Fish buildFish(FishTemplate t) {
        return new Fish(
            t.name, t.maxHp, t.pullStrength,
            t.rarity, t.baseWeightKg, t.baseLengthCm,
            t.baseXp, t.baseGold, t.skills
        );
    }

    private static int weightedRandom(Player player) {
        float[] weights = new float[RARITY_WEIGHTS.length];
        System.arraycopy(RARITY_WEIGHTS, 0, weights, 0, weights.length);

        if (player != null) {
            if (player.isSkillEquipped("may_man_co_ban")) {
                float bonus = player.getSkillTree().getMayManCoBanBonus() / 100f; // e.g., 0.15 for 15%
                // index 2 (RARE), 3 (EPIC), 4 (LEGENDARY), 5 (MYTHIC)
                for (int i = 2; i < weights.length; i++) weights[i] *= (1f + bonus);
            }
            if (player.isSkillEquipped("ngu_dan_tinh_anh")) {
                float bonus = player.getSkillTree().getNguDanTinhAnhBonus() / 100f;
                // index 3 (EPIC), 4 (LEGENDARY), 5 (MYTHIC)
                for (int i = 3; i < weights.length; i++) weights[i] *= (1f + bonus);
            }
            
            float hookBoost = player.getRarityBoost() / 100f;
            if (hookBoost > 0) {
                for (int i = 2; i < weights.length; i++) weights[i] *= (1f + hookBoost);
            }
            if (player.isSetBonusActive() && player.getEquippedRod() != null) {
                if (player.getEquippedRod().setName.equals("Đại Dương")) {
                    for (int i = 2; i < weights.length; i++) weights[i] *= 1.20f;
                }
                if (player.getEquippedRod().setName.equals("Vực Thẳm")) {
                    if (weights.length > 4) weights[4] *= 3.0f;
                }
            }
        }

        float total = 0;
        for (float w : weights) total += w;

        float roll = MathUtils.random(total), cum = 0;
        for (int i = 0; i < weights.length; i++) {
            cum += weights[i];
            if (roll <= cum) return i;
        }
        return weights.length - 1;
    }
}