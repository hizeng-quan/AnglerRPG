package com.fishingrpg.game.entities;

import com.fishingrpg.game.loot.CatchableType;

public class Item {
    private String name;
    private String description;
    private CatchableType type;

    public Item(String name, String description, CatchableType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public CatchableType getType() { return type; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item item = (Item) obj;
        return name.equals(item.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
