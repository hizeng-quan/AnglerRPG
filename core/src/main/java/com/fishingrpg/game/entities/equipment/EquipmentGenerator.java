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
        new EqSet("Quang Minh", "Trượng Ánh Sáng", "Dây Chỉ Vàng", "Lưỡi Câu Thần", 7),
        new EqSet("Vệ Quốc Vang Danh", "Cần Câu Khởi Nghĩa", "Cước Vệ Quốc", "Lưỡi Sao Vàng", 4), // Same as RARE
        new EqSet("Thần Khí Lạc Hồng", "Trượng Lạc Hồng", "Dây Rồng Thiêng", "Móc Âu Cơ", 8), // Supreme 6★
        new EqSet("Dạ Hội Bí Ngô", "Cần Dạ Hội Bí Ngô", "Dây Cước Tơ Nhện Quỷ", "Lưỡi Kẹo Ma Quái", 4), // 4★ Limited Halloween
        new EqSet("Huyết Nguyệt Thần Vương", "Huyết Nguyệt Thần Trượng", "Dây Huyết Ma Cơ", "Lưỡi Huyết Nguyệt Ma Trảo", 8) // 6★ Limited Premium Halloween
    };

    public static int getSetPoints(String setName) {
        if ("Thần Thoại".equals(setName)) return 7;
        for (EqSet s : SETS) {
            if (s.setName.equals(setName)) return s.points;
        }
        return 0; // fallback
    }

    public static int getRarityPoints(Rarity rarity) {
        switch(rarity) {
            case COMMON: return 0;
            case UNCOMMON: return 2;
            case RARE: return 4;
            case EPIC: return 7;
            case LEGENDARY: return 10;
            case LIMITED_EVENT: return 4;
            case LIMITED_PREMIUM: return 10;
            default: return 0;
        }
    }

    public static Equipment rollGacha(com.fishingrpg.game.entities.Player player, boolean guaranteeRare) {
        player.pityRare++;
        player.pityEpic++;
        player.pityLegendary++;

        Rarity rarity = rollRarity(guaranteeRare, player);
        EquipmentType type = EquipmentType.values()[MathUtils.random(0, 2)];
        // Standard Gacha strictly excludes Limited sets
        EqSet set;
        do {
            set = SETS[MathUtils.random(0, SETS.length - 1)];
        } while (set.setName.equals("Thần Khí Lạc Hồng") || set.setName.equals("Vệ Quốc Vang Danh")
              || set.setName.equals("Huyết Nguyệt Thần Vương") || set.setName.equals("Dạ Hội Bí Ngô"));
        
        Equipment eq = new Equipment();
        eq.type = type;
        eq.rarity = rarity;
        eq.setName = set.setName;
        eq.level = 1; // Unused, keeping for save compatibility
        
        // Strict safety check: Set Lạc Hồng always has fixed rarity LIMITED_PREMIUM
        if ("Thần Khí Lạc Hồng".equals(eq.setName)) {
            eq.rarity = Rarity.LIMITED_PREMIUM;
        }
        
        int mult = set.points + getRarityPoints(rarity);
        
        switch (type) {
            case ROD:
                eq.id = "rod_" + set.setName.replaceAll(" ", "_").toLowerCase() + "_" + MathUtils.random(10000);
                eq.name = set.rodName;
                eq.mainStat1 = 10f + mult * 1.2f; // Base damage (Nerfed from 2.0f)
                eq.mainStat2 = 50f + mult * 8f;   // Max stamina (Nerfed from 15f)
                break;
            case LINE:
                eq.id = "line_" + set.setName.replaceAll(" ", "_").toLowerCase() + "_" + MathUtils.random(10000);
                eq.name = set.lineName;
                eq.mainStat1 = 100f + mult * 15f; // Max tension (Nerfed from 25f)
                eq.mainStat2 = 2f + mult * 0.22f; // Kháng đứt dây (Nerfed from 0.4f)
                break;
            case HOOK:
                eq.id = "hook_" + set.setName.replaceAll(" ", "_").toLowerCase() + "_" + MathUtils.random(10000);
                eq.name = set.hookName;
                eq.mainStat1 = 5f + mult * 0.6f;  // Tỷ lệ cá hiếm (%)
                eq.mainStat2 = 10f + mult * 1.2f; // Tỷ lệ giữ mồi (%)
                break;
        }

        // Substats
        for (int i = 0; i < rarity.substatCount; i++) {
            Substat.Type subType = Substat.Type.values()[MathUtils.random(0, Substat.Type.values().length - 1)];
            float subVal = (MathUtils.random(1f, 2.5f) + (mult / 4f));
            eq.substats.add(new Substat(subType, Math.max(1, Math.round(subVal))));
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

    private static Rarity rollRarityLimited(com.fishingrpg.game.entities.Player player) {
        if (player.pityLimited >= 80) return Rarity.LIMITED_PREMIUM;
        float roll = MathUtils.random();
        if (roll < 0.015f) return Rarity.LIMITED_PREMIUM; // 1.5% chance
        if (roll < 0.10f) return Rarity.EPIC;
        if (roll < 0.40f) return Rarity.RARE;
        if (roll < 0.80f) return Rarity.UNCOMMON;
        return Rarity.COMMON;
    }

    public static Equipment rollGachaLimited(com.fishingrpg.game.entities.Player player) {
        player.pityLimited++;
        Rarity rarity = rollRarityLimited(player);
        
        if (rarity == Rarity.LIMITED_PREMIUM) {
            player.pityLimited = 0;
        }
        
        EquipmentType type = EquipmentType.values()[MathUtils.random(0, 2)];
        EqSet set = null;
        
        if (rarity == Rarity.LIMITED_PREMIUM) {
            String featuredSet = com.fishingrpg.game.systems.EventManager.isHalloweenActive() ? "Huyết Nguyệt Thần Vương" : "Thần Khí Lạc Hồng";
            // 50% Rate-up đúng Set tiêu điểm (Huyết Nguyệt khi Halloween, Lạc Hồng khi sự kiện Lạc Hồng)
            if (MathUtils.randomBoolean(0.5f)) {
                for (EqSet s : SETS) {
                    if (s.setName.equals(featuredSet)) {
                        set = s; break;
                    }
                }
            } else {
                String[] offBannerSets = {"Quang Minh", "Thời Không", "Hoàng Gia"};
                String picked = offBannerSets[MathUtils.random(0, offBannerSets.length - 1)];
                for (EqSet s : SETS) {
                    if (s.setName.equals(picked)) {
                        set = s; break;
                    }
                }
            }
            if (set == null) {
                for (EqSet s : SETS) {
                    if (s.setName.equals(featuredSet)) { set = s; break; }
                }
            }
        } else {
            // normal set (exclude limited ones; Dạ Hội Bí Ngô only drops during Halloween when LIMITED_EVENT)
            do {
                set = SETS[MathUtils.random(0, SETS.length - 1)];
            } while (set.setName.equals("Thần Khí Lạc Hồng") || set.setName.equals("Vệ Quốc Vang Danh")
                  || set.setName.equals("Huyết Nguyệt Thần Vương")
                  || (!com.fishingrpg.game.systems.EventManager.isHalloweenActive() && set.setName.equals("Dạ Hội Bí Ngô")));
        }
        
        Equipment eq = new Equipment();
        eq.type = type;
        eq.rarity = rarity;
        eq.setName = set.setName;
        eq.level = 1;
        
        // Strict safety rule: Set Lạc Hồng and Huyết Nguyệt Thần Vương ONLY exist with fixed rarity LIMITED_PREMIUM (6★ explosion)
        if ("Thần Khí Lạc Hồng".equals(eq.setName) || "Huyết Nguyệt Thần Vương".equals(eq.setName)) {
            eq.rarity = Rarity.LIMITED_PREMIUM;
        }
        
        int mult = set.points + getRarityPoints(eq.rarity);
        
        switch (type) {
            case ROD:
                eq.id = "rod_" + set.setName.replaceAll(" ", "_").toLowerCase() + "_" + MathUtils.random(10000);
                eq.name = set.rodName;
                eq.mainStat1 = 10f + mult * 1.2f; // Base damage
                eq.mainStat2 = 50f + mult * 8f;   // Max stamina
                break;
            case LINE:
                eq.id = "line_" + set.setName.replaceAll(" ", "_").toLowerCase() + "_" + MathUtils.random(10000);
                eq.name = set.lineName;
                eq.mainStat1 = 100f + mult * 15f; // Max tension
                eq.mainStat2 = 2f + mult * 0.22f; // Kháng đứt dây
                break;
            case HOOK:
                eq.id = "hook_" + set.setName.replaceAll(" ", "_").toLowerCase() + "_" + MathUtils.random(10000);
                eq.name = set.hookName;
                eq.mainStat1 = 5f + mult * 0.6f;  // Tỷ lệ cá hiếm (%)
                eq.mainStat2 = 10f + mult * 1.2f; // Tỷ lệ giữ mồi (%)
                break;
        }

        // Substats
        for (int i = 0; i < eq.rarity.substatCount; i++) {
            Substat.Type subType = Substat.Type.values()[MathUtils.random(0, Substat.Type.values().length - 1)];
            float subVal = (MathUtils.random(1f, 2.5f) + (mult / 4f));
            eq.substats.add(new Substat(subType, Math.max(1, Math.round(subVal))));
        }

        return eq;
    }

    public static Equipment createFreeEventEquipment(EquipmentType type) {
        if (com.fishingrpg.game.systems.EventManager.isHalloweenActive()) {
            return createPumpkinCarnivalEquipment(type);
        }

        EqSet set = null;
        for (EqSet s : SETS) {
            if (s.setName.equals("Vệ Quốc Vang Danh")) {
                set = s;
                break;
            }
        }
        
        Equipment eq = new Equipment();
        eq.type = type;
        eq.rarity = Rarity.LIMITED_EVENT;
        eq.setName = set.setName;
        eq.level = 1;
        
        int mult = set.points + getRarityPoints(eq.rarity);
        
        switch (type) {
            case ROD:
                eq.id = "rod_" + set.setName.replaceAll(" ", "_").toLowerCase() + "_" + MathUtils.random(10000);
                eq.name = set.rodName;
                eq.mainStat1 = 10f + mult * 1.2f; 
                eq.mainStat2 = 50f + mult * 8f; 
                break;
            case LINE:
                eq.id = "line_" + set.setName.replaceAll(" ", "_").toLowerCase() + "_" + MathUtils.random(10000);
                eq.name = set.lineName;
                eq.mainStat1 = 100f + mult * 15f; 
                eq.mainStat2 = 2f + mult * 0.22f; 
                break;
            case HOOK:
                eq.id = "hook_" + set.setName.replaceAll(" ", "_").toLowerCase() + "_" + MathUtils.random(10000);
                eq.name = set.hookName;
                eq.mainStat1 = 5f + mult * 0.6f; 
                eq.mainStat2 = 10f + mult * 1.2f; 
                break;
        }

        for (int i = 0; i < eq.rarity.substatCount; i++) {
            Substat.Type subType = Substat.Type.values()[MathUtils.random(0, Substat.Type.values().length - 1)];
            float subVal = (MathUtils.random(1f, 2.5f) + (mult / 4f));
            eq.substats.add(new Substat(subType, Math.max(1, Math.round(subVal))));
        }

        return eq;
    }

    public static Equipment createPumpkinCarnivalEquipment(EquipmentType type) {
        EqSet set = null;
        for (EqSet s : SETS) {
            if (s.setName.equals("Dạ Hội Bí Ngô")) {
                set = s;
                break;
            }
        }
        if (set == null) return createFreeEventEquipment(type);

        Equipment eq = new Equipment();
        eq.type = type;
        eq.rarity = Rarity.LIMITED_EVENT;
        eq.setName = set.setName;
        eq.level = 1;

        switch (type) {
            case ROD:
                eq.id = "rod_pumpkin_" + MathUtils.random(10000);
                eq.name = set.rodName;
                eq.mainStat1 = 34.0f; // Sát thương cơ bản
                eq.mainStat2 = 210.0f; // Thể lực tối đa
                break;
            case LINE:
                eq.id = "line_pumpkin_" + MathUtils.random(10000);
                eq.name = set.lineName;
                eq.mainStat1 = 400.0f; // Sức căng tối đa
                eq.mainStat2 = 6.4f; // Kháng đứt dây
                break;
            case HOOK:
                eq.id = "hook_pumpkin_" + MathUtils.random(10000);
                eq.name = set.hookName;
                eq.mainStat1 = 17.0f; // Tỉ lệ cá hiếm (+17%)
                eq.mainStat2 = 34.0f; // Tỉ lệ giữ mồi (+34%)
                break;
        }

        int mult = set.points + getRarityPoints(eq.rarity);
        for (int i = 0; i < eq.rarity.substatCount; i++) {
            Substat.Type subType = Substat.Type.values()[MathUtils.random(0, Substat.Type.values().length - 1)];
            float subVal = (MathUtils.random(1f, 2.5f) + (mult / 4f));
            eq.substats.add(new Substat(subType, Math.max(1, Math.round(subVal))));
        }

        return eq;
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

    public static Equipment createHuyetNguyetEquipment(EquipmentType type) {
        EqSet set = null;
        for (EqSet s : SETS) {
            if (s.setName.equals("Huyết Nguyệt Thần Vương")) {
                set = s;
                break;
            }
        }
        if (set == null) return createPumpkinCarnivalEquipment(type);

        Equipment eq = new Equipment();
        eq.type = type;
        eq.rarity = Rarity.LIMITED_PREMIUM;
        eq.setName = set.setName;
        eq.level = 1;

        switch (type) {
            case ROD:
                eq.id = "rod_huyet_nguyet_" + MathUtils.random(10000);
                eq.name = set.rodName;
                eq.mainStat1 = 48.0f; // Sát thương cơ bản khủng (6★ Limited)
                eq.mainStat2 = 280.0f; // Thể lực tối đa cực lớn
                break;
            case LINE:
                eq.id = "line_huyet_nguyet_" + MathUtils.random(10000);
                eq.name = set.lineName;
                eq.mainStat1 = 520.0f; // Sức căng tối đa áp đảo
                eq.mainStat2 = 8.5f; // Kháng đứt dây siêu việt
                break;
            case HOOK:
                eq.id = "hook_huyet_nguyet_" + MathUtils.random(10000);
                eq.name = set.hookName;
                eq.mainStat1 = 28.0f; // Tỉ lệ cá hiếm (+28%)
                eq.mainStat2 = 45.0f; // Tỉ lệ giữ mồi (+45%)
                break;
        }

        int mult = set.points + getRarityPoints(eq.rarity);
        for (int i = 0; i < eq.rarity.substatCount; i++) {
            Substat.Type subType = Substat.Type.values()[MathUtils.random(0, Substat.Type.values().length - 1)];
            float subVal = (MathUtils.random(2f, 4.0f) + (mult / 3.5f));
            eq.substats.add(new Substat(subType, Math.max(2, Math.round(subVal))));
        }

        return eq;
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
            case "Quang Minh":
            case "Thần Thoại": return "Set 3 món: +35% Mọi chỉ số phụ";
            case "Vệ Quốc Vang Danh": return "Set 3 món: +29% Cá Hiếm, Kháng đứt dây +10%";
            case "Thần Khí Lạc Hồng": return "Ý Chí Bất Diệt: Vô hiệu hóa kỹ năng Boss, hồi 25% Thể lực!";
            case "Dạ Hội Bí Ngô": return "Lễ Hội Kẹo Ngọt: +35% EXP, +25% Vàng & 35% rơi thêm Kẹo Trick-or-Treat!";
            case "Huyết Nguyệt Thần Vương": return "Bá Quyền Trăng Máu: Quăng Perfect x2 Sát thương (2s đầu). Kéo dây hút 3% HP cá (tối đa 12/s) thành Thể lực!";
            default: return "Set 3 món: Kích hoạt sức mạnh ẩn";
        }
    }
}
