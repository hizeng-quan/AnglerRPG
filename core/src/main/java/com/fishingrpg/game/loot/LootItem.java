package com.fishingrpg.game.loot;

/**
 * Dai dien cho cac vat pham khong phai ca: rac, consumable, bau vat.
 * Khong co vong lap combat -- nhat len ngay khi cau duoc.
 */
public class LootItem implements Catchable {

    private final String       name;
    private final CatchableType type;
    private final int          goldValue;
    private final int          xpValue;
    private final String       description;

    public LootItem(String name, CatchableType type,
                    int goldValue, int xpValue, String description) {
        if (type == CatchableType.FISH)
            throw new IllegalArgumentException("LootItem khong duoc la FISH, dung class Fish.");
        this.name        = name;
        this.type        = type;
        this.goldValue   = goldValue;
        this.xpValue     = xpValue;
        this.description = description;
    }

    // -------------------------------------------------------------------------

    @Override public String       getName()        { return name;    }
    @Override public CatchableType getType()        { return type;    }
    @Override public int          getGoldValue()   { return goldValue; }
    @Override public int          getXpValue()     { return xpValue;   }
    @Override public boolean      requiresCombat() { return false;   }

    public String getDescription() { return description; }

    @Override
    public String toString() {
        return "[" + type + "] " + name + " (+" + goldValue + "g, +" + xpValue + "xp)";
    }
}
