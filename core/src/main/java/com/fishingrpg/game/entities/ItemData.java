package com.fishingrpg.game.entities;

public class ItemData {
    public final String name;
    public final String description;
    public final ItemUsageType usageType;
    public final int buyPrice;
    public final int sellPrice;

    public ItemData(String name, String description, ItemUsageType usageType, int buyPrice) {
        this.name = name;
        this.description = description;
        this.usageType = usageType;
        this.buyPrice = buyPrice;
        this.sellPrice = buyPrice / 2;
    }
}
