package com.fishingrpg.game.entities.equipment;

import java.util.ArrayList;
import java.util.List;

public class Equipment {
    public String id; // e.g. "rod_beginner"
    public String name;
    public EquipmentType type;
    public Rarity rarity;
    public String setName; // e.g. "Tân Thủ"
    public int level;

    // Main stats
    public float mainStat1;
    public float mainStat2;

    public List<Substat> substats = new ArrayList<>();
    
    public String instanceId = java.util.UUID.randomUUID().toString();

    public Equipment() {} // For JSON serialization

    public Equipment(String id, String name, EquipmentType type, Rarity rarity, String setName, int level, float stat1, float stat2) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.rarity = rarity;
        this.setName = setName;
        this.level = level;
        this.mainStat1 = stat1;
        this.mainStat2 = stat2;
    }
}
