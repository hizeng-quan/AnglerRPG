package com.fishingrpg.game.loot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;
import com.fishingrpg.game.data.FishDatabase;
import com.fishingrpg.game.entities.Player;
import java.util.ArrayList;

public class LootTable {

    public static class LootTemplate {
        public String name;
        public CatchableType type;
        public int goldValue;
        public int xpValue;
        public String description;
    }

    public static class LootData {
        public ArrayList<LootTemplate> trash;
        public ArrayList<LootTemplate> consumable;
        public ArrayList<LootTemplate> treasure;
    }

    private static LootData data;

    private static final float W_FISH = 65f, W_TRASH = 20f, W_CONSUMABLE = 10f, W_TOTAL = 100f;

    public static void load() {
        if (data != null) return;
        Json json = new Json();
        data = json.fromJson(LootData.class, Gdx.files.internal("data/loot.json"));
    }

    public static Catchable rollCatch(Player player) {
        if (data == null) load();
        
        float treasureBonus = 0f;
        if (player != null && player.isSkillEquipped("tho_san_kho_bau")) {
            treasureBonus = player.getSkillTree().getThoSanKhoBauBonus();
        }
        if (player != null && player.isSetBonusActive() && player.getEquippedRod() != null && player.getEquippedRod().setName.equals("Hải Tặc")) {
            treasureBonus += 30f;
        }
        
        float wFish = W_FISH;
        float wTrash = W_TRASH;
        float wConsumable = W_CONSUMABLE;
        float wTreasure = (W_TOTAL - W_FISH - W_TRASH - W_CONSUMABLE) + treasureBonus;
        float total = wFish + wTrash + wConsumable + wTreasure;
        
        float r = MathUtils.random(total);
        if (player != null && player.isBaitBuffActive()) {
            r = MathUtils.random(wFish + wTreasure);
            if (r < wFish) return FishDatabase.getRandomFish(player);
            return pick(data.treasure);
        }
        if (r < wFish)                          return FishDatabase.getRandomFish(player);
        if (r < wFish + wTrash)                 return pick(data.trash);
        if (r < wFish + wTrash + wConsumable)   return pick(data.consumable);
        return pick(data.treasure);
    }
    
    public static Catchable rollTrashOrConsumable() {
        if (data == null) load();
        if (MathUtils.randomBoolean(0.3f)) return pick(data.consumable);
        return pick(data.trash);
    }

    private static LootItem pick(ArrayList<LootTemplate> arr) {
        if (arr == null || arr.isEmpty()) {
            // Fallback an toàn nếu danh sách loot rỗng
            return new LootItem("Rác", CatchableType.TRASH, 0, 0, "Không có gì");
        }
        LootTemplate t = arr.get(MathUtils.random(arr.size() - 1));
        return new LootItem(t.name, t.type, t.goldValue, t.xpValue, t.description);
    }
}