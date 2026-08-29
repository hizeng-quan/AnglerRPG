package com.fishingrpg.game.entities;

public class MapArea {
    public String id;
    public String name;
    public int requiredLevel;
    public int requiredFishCount; // Number of unique fish required from previous map
    public String prevMapId;

    public MapArea(String id, String name, int requiredLevel, int requiredFishCount, String prevMapId) {
        this.id = id;
        this.name = name;
        this.requiredLevel = requiredLevel;
        this.requiredFishCount = requiredFishCount;
        this.prevMapId = prevMapId;
    }

    public static final MapArea[] MAPS = {
        new MapArea("ao_lang", "Ao Làng", 1, 0, null),
        new MapArea("bai_bien", "Bãi Biển", 10, 11, "ao_lang"),
        new MapArea("dao_xa", "Đảo Xa", 20, 11, "bai_bien")
    };
    
    public static MapArea getMap(String id) {
        for (MapArea m : MAPS) {
            if (m.id.equals(id)) return m;
        }
        return MAPS[0];
    }
}
