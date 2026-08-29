package com.fishingrpg.game.entities.equipment;

import com.badlogic.gdx.math.MathUtils;

public class EquipmentGenerator {
    
    public static class EqSet {
        public String setName;
        public String rodName;
        public String lineName;
        public String hookName;
        public int points;
        
        public EqSet(String setName, String rodName, String lineName, String hookName, int points) {
            this.setName = setName;
            this.rodName = rodName;
            this.lineName = lineName;
            this.hookName = hookName;
            this.points = points;
        }
    }

    private static final EqSet[] SETS = {
        new EqSet("Tập Sự", "Cần Câu Tre", "Dây Cước Thường", "Lưỡi Câu Chì", 0),
        new EqSet("Thợ Săn", "Gậy Phục Kích", "Sợi Tơ Kẽm", "Móng Vuốt Biển", 1),
        new EqSet("Băng Giá", "Cần Câu Băng", "Dây Lạnh Cóng", "Móc Câu Tuyết", 2),
        new EqSet("Đại Dương", "Trượng Biển Khơi", "Sợi Thủy Tinh", "Nanh Thủy Quái", 3),
        new EqSet("Hải Tặc", "Trụ Cột Buồm", "Xích Neo Nhỏ", "Lưỡi Hái Thép", 4),
        new EqSet("Vực Thẳm", "Trượng Hắc Ám", "Sợi Hắc Mạch", "Móng Vuốt Vực Sâu", 5),
        new EqSet("Hoàng Gia", "Cần Câu Bạch Kim", "Tơ Tằm Thượng Hạng", "Móc Cẩm Thạch", 6),
        new EqSet("Thời Không", "Trượng Thời Gian", "Sợi Dây Xuyên Thấu", "Lưỡi Câu Hư Không", 6),
        new EqSet("Thần Thoại", "Trượng Ánh Sáng", "Dây Chỉ Vàng", "Lưỡi Câu Thần", 7)
    };

    public static int getSetPoints(String setName) {
        for (EqSet s : SETS) {
            if (s.setName.equals(setName)) return s.points;
        }
        return 0; // fallback
    }

    public static int getRarityPoints(Rarity rarity) {
        switch(rarity) {
            case COMMON: return 0;
            case UNCOMMON: return 2;
            case RARE: return 5;
            case EPIC: return 9;
            case LEGENDARY: return 15;
            default: return 0;
        }
    }

    public static Equipment rollGacha(com.fishingrpg.game.entities.Player player, boolean guaranteeRare) {
        player.pityRare++;
        player.pityEpic++;
        player.pityLegendary++;

        Rarity rarity = rollRarity(guaranteeRare, player);
        EquipmentType type = EquipmentType.values()[MathUtils.random(0, 2)];
        EqSet set = SETS[MathUtils.random(0, SETS.length - 1)];
        
        Equipment eq = new Equipment();
        eq.type = type;
        eq.rarity = rarity;
        eq.setName = set.setName;
        eq.level = 1; // Unused, keeping for save compatibility
        
        int mult = set.points + getRarityPoints(rarity);
        
        switch (type) {
            case ROD:
                eq.id = "rod_" + set.setName.replaceAll(" ", "_").toLowerCase() + "_" + MathUtils.random(10000);
                eq.name = set.rodName;
                eq.mainStat1 = 10f + mult * 2f; // Base damage
                eq.mainStat2 = 50f + mult * 15f; // Max stamina
                break;
            case LINE:
                eq.id = "line_" + set.setName.replaceAll(" ", "_").toLowerCase() + "_" + MathUtils.random(10000);
                eq.name = set.lineName;
                eq.mainStat1 = 50f + mult * 20f; // Max tension
                eq.mainStat2 = 2f + mult * 0.4f; // Kháng đứt dây
                break;
            case HOOK:
                eq.id = "hook_" + set.setName.replaceAll(" ", "_").toLowerCase() + "_" + MathUtils.random(10000);
                eq.name = set.hookName;
                eq.mainStat1 = 5f + mult * 1.0f; // Tỷ lệ cá xịn (%)
                eq.mainStat2 = 10f + mult * 2.0f; // Tỷ lệ giữ mồi (%)
                break;
        }

        // Substats
        for (int i = 0; i < rarity.substatCount; i++) {
            Substat.Type subType = Substat.Type.values()[MathUtils.random(0, Substat.Type.values().length - 1)];
            float subVal = (MathUtils.random(1f, 3f) + (mult / 2f));
            eq.substats.add(new Substat(subType, Math.round(subVal)));
        }

        return eq;
    }

    private static Rarity rollRarity(boolean guaranteeRare, com.fishingrpg.game.entities.Player player) {
        Rarity rarity = null;
        
        if (player.pityLegendary >= 100) {
            rarity = Rarity.LEGENDARY;
        } else if (player.pityEpic >= 50) {
            rarity = Rarity.EPIC;
        } else if (player.pityRare >= 10 || guaranteeRare) {
            float roll = MathUtils.random();
            if (roll < 0.05f) rarity = Rarity.LEGENDARY;
            else if (roll < 0.25f) rarity = Rarity.EPIC;
            else rarity = Rarity.RARE;
        } else {
            float roll = MathUtils.random();
            if (roll < 0.50f) rarity = Rarity.COMMON;
            else if (roll < 0.80f) rarity = Rarity.UNCOMMON;
            else if (roll < 0.95f) rarity = Rarity.RARE;
            else if (roll < 0.99f) rarity = Rarity.EPIC;
            else rarity = Rarity.LEGENDARY;
        }
        
        // Reset pity counters
        if (rarity == Rarity.LEGENDARY) {
            player.pityLegendary = 0;
            player.pityEpic = 0;
            player.pityRare = 0;
        } else if (rarity == Rarity.EPIC) {
            player.pityEpic = 0;
            player.pityRare = 0;
        } else if (rarity == Rarity.RARE) {
            player.pityRare = 0;
        }
        
        return rarity;
    }

    public static String getRandomName(EquipmentType type) {
        EqSet set = SETS[MathUtils.random(0, SETS.length - 1)];
        switch (type) {
            case ROD: return set.rodName;
            case LINE: return set.lineName;
            case HOOK: return set.hookName;
            default: return "Unknown";
        }
    }

    public static String getSetBonusDesc(String setName) {
        if (setName == null) return "";
        switch (setName) {
            case "Tập Sự": return "Set 3 món: +20% Kinh Nghiệm (EXP)";
            case "Thợ Săn": return "Set 3 món: +20% Sát thương kéo cá";
            case "Băng Giá": return "Set 3 món: Đóng băng Thanh Căng Dây (Tension)";
            case "Đại Dương": return "Set 3 món: +20% Tỉ lệ câu Cá Hiếm";
            case "Hải Tặc": return "Set 3 món: +30% Cơ hội câu được Kho Báu";
            case "Vực Thẳm": return "Set 3 món: Tăng mạnh cơ hội chạm trán BOSS";
            case "Hoàng Gia": return "Set 3 món: +30% Vàng & +30% EXP";
            case "Thời Không": return "Set 3 món: -20% Thời gian hồi chiêu (Cooldown)";
            case "Thần Thoại": return "Set 3 món: +35% Mọi chỉ số phụ";
            default: return "Set 3 món: Kích hoạt sức mạnh ẩn";
        }
    }
}
