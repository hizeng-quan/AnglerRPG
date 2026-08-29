package com.fishingrpg.game.loot;

/** Phan loai moi thu co the cau duoc. */
public enum CatchableType {
    /** Ca -- co vong lap combat. */
    FISH,
    /** Rac -- khong combat, nhat len ngay. */
    TRASH,
    /** Vat pham tieu hao (moi, thuoc...) -- vao inventory. */
    CONSUMABLE,
    /** Bau vat -- khong combat, gia tri vang cao. */
    TREASURE
}
