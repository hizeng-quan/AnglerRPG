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

        // --- HALLOWEEN GLOBAL ENCOUNTER LOGIC (HLW-03.2) ---
        boolean isHalloween = com.fishingrpg.game.systems.EventManager.isHalloweenActive();
        if (isHalloween) {
            float halloweenChance = 0.15f; // 15% ty le co ban
            if (player != null) {
                if (player.getHalloweenCandyCharges() > 0) {
                    halloweenChance += 0.15f; // +15% khi co buff Keo Trick-or-Treat
                }
                String activeSet = (player.getEquipment() != null) ? player.getEquipment().getActiveSetName() : "";
                if ("Dạ Hội Bí Ngô".equals(activeSet)) {
                    halloweenChance += 0.10f; // +10%
                }
                // HLW-05: Nhan doi ty le toan bo ca Halloween trong Dem Huyet Nguyet
                boolean isBloodMoon = (player.getManualBloodMoonTimer() > 0f);
                if (!isBloodMoon) {
                    java.time.LocalTime now = java.time.LocalTime.now();
                    int hour = now.getHour();
                    if (hour >= 18 || hour < 6) {
                        isBloodMoon = ((now.getMinute() / 15) % 2) == 0;
                    }
                }
                if (isBloodMoon) {
                    halloweenChance *= 2.0f;
                }
                if (player.getHalloweenCandyTimer() > 0f) {
                    halloweenChance *= 1.5f;
                }
            }

            if (MathUtils.randomBoolean(Math.min(halloweenChance, 0.85f))) {
                float wSkeleton = 40.0f;
                float wJackO = 30.0f;
                float wDauLau = 15.0f;
                float wDangMa = 6.0f;
                float wFrank = 4.0f;
                float wHaiXa = 3.0f;
                float wThanatos = 2.0f;

                if (player != null) {
                    float rareMulti = 1.0f;
                    if (player.getHalloweenCandyCharges() > 0) {
                        rareMulti += 0.50f;
                        player.consumeHalloweenCandyCharge();
                    }
                    String activeSet = (player.getEquipment() != null) ? player.getEquipment().getActiveSetName() : "";
                    if ("Dạ Hội Bí Ngô".equals(activeSet)) rareMulti += 0.35f;
                    wDauLau *= rareMulti;
                    wDangMa *= rareMulti;
                    wFrank *= rareMulti;
                    wHaiXa *= (rareMulti * 1.25f);
                    wThanatos *= (rareMulti * 1.50f);
                }

                float[] hlwWeights = { wSkeleton, wJackO, wDauLau, wDangMa, wFrank, wHaiXa, wThanatos };
                String[] hlwNames = {
                    "Cá Skeleton", "Cá Đèn Lồng Jack-o'", "Cá Đầu Lâu Huyết Nguyệt",
                    "Cá Dạng Ma", "Thủy Quái Frankenstein", "Hải Xà Dạ Xoa", "Kình Ngư Tử Thần Thanatos"
                };

                float totalHlw = 0f;
                for (float w : hlwWeights) totalHlw += w;
                float rollHlw = MathUtils.random(totalHlw);
                float cumHlw = 0f;
                String selectedHlw = hlwNames[0];
                for (int i = 0; i < hlwWeights.length; i++) {
                    cumHlw += hlwWeights[i];
                    if (rollHlw <= cumHlw) {
                        selectedHlw = hlwNames[i];
                        break;
                    }
                }
                Fish hlwFish = getFishByName(selectedHlw);
                if (hlwFish != null) return hlwFish;
            }
        }

        // --- MAP STANDARD FISH ROLL ---
        int rarityIdx = weightedRandom(player);
        Rarity targetRarity = Rarity.values()[Math.min(rarityIdx, Rarity.values().length - 1)];
        java.util.ArrayList<FishTemplate> filtered = new java.util.ArrayList<>();
        String currentMap = player != null ? player.getCurrentMap() : "ao_lang";
        for (FishTemplate t : templates) {
            // Chu y: Bo qua ca Halloween (t.map == null) trong pool ca thuong cua map
            if (t.rarity == targetRarity && currentMap.equals(t.map)) {
                filtered.add(t);
            }
        }

        if (filtered.isEmpty()) {
            for (FishTemplate t : templates) {
                if (currentMap.equals(t.map)) filtered.add(t);
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
        java.util.ArrayList<com.fishingrpg.game.entities.FishSkill> copiedSkills = new java.util.ArrayList<>();
        if (t.skills != null) {
            for (com.fishingrpg.game.entities.FishSkill s : t.skills) {
                com.fishingrpg.game.entities.FishSkill copy = new com.fishingrpg.game.entities.FishSkill();
                copy.id = s.id;
                copy.isActive = s.isActive;
                copy.triggerHpPercent = s.triggerHpPercent;
                copy.triggered = false;
                copiedSkills.add(copy);
            }
        }

        return new Fish(
            t.name, t.maxHp, t.pullStrength,
            t.rarity, t.baseWeightKg, t.baseLengthCm,
            t.baseXp, t.baseGold, copiedSkills
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
            float timingBoost = player.getCastRarityBoost() / 100f;
            if (timingBoost > 0) {
                for (int i = 2; i < weights.length; i++) weights[i] *= (1f + timingBoost);
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
