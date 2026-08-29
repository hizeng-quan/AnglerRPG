package com.fishingrpg.game.entities;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    
    private final Map<String, Integer> items = new HashMap<>();

    public void addItem(String name, int count) {
        items.put(name, items.getOrDefault(name, 0) + count);
    }

    public boolean hasItem(String name) {
        return items.getOrDefault(name, 0) > 0;
    }

    public void consumeItem(String name) {
        if (hasItem(name)) {
            items.put(name, items.get(name) - 1);
            if (items.get(name) == 0) {
                items.remove(name);
            }
        }
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public int getCount(String name) {
        return items.getOrDefault(name, 0);
    }
}
