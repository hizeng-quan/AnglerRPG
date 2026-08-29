package com.fishingrpg.game.loot;

/**
 * Interface chung cho moi thu co the cau duoc: ca, rac, vat pham, bau vat.
 * PlayScreen va LootTable deu lam viec voi Catchable de khong can biet cu the la gi.
 */
public interface Catchable {
    /** Ten hien thi (ten ca, ten item...). */
    String getName();

    /** Loai (FISH / TRASH / CONSUMABLE / TREASURE). */
    CatchableType getType();

    /** Vang nhan duoc khi cau len (co the 0 voi rac). */
    int getGoldValue();

    /** XP nhan duoc. */
    int getXpValue();

    /** Co can vong lap combat khong (chi true voi FISH). */
    boolean requiresCombat();
}
