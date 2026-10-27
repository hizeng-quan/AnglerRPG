package com.fishingrpg.game.entities;

import com.badlogic.gdx.math.MathUtils;
import com.fishingrpg.game.data.ItemDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages economy and shop (discounts).
 */
public class PlayerEconomy {
    private Player player;
    private int successfulCatchesSinceShopUpdate = 0;
    private final Map<String, Integer> shopDiscounts = new HashMap<>();

    public PlayerEconomy() {}

    public PlayerEconomy(Player player) {
        this.player = player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Map<String, Integer> getShopDiscounts() {
        if (shopDiscounts.isEmpty()) {
            refreshDiscounts();
        }
        return shopDiscounts;
    }

    public void recordSuccessfulCatch() {
        successfulCatchesSinceShopUpdate++;
        if (successfulCatchesSinceShopUpdate >= 4) {
            successfulCatchesSinceShopUpdate = 0;
            refreshDiscounts();
        }
    }

    public void refreshDiscounts() {
        shopDiscounts.clear();
        List<ItemData> shopItems = ItemDatabase.getShopItems();
        if (shopItems.isEmpty()) return;

        List<ItemData> pool = new ArrayList<>();
        for (ItemData item : shopItems) {
            // Loại bỏ các sách kỹ năng đã bán hết / đạt cấp tối đa khỏi danh sách giảm giá
            if (player != null && item.name.startsWith("Sách: ")) {
                String skillId = getSkillIdFromBook(item.name);
                if (!skillId.isEmpty() && player.getSkillTree() != null) {
                    com.fishingrpg.game.skills.Skill s = player.getSkillTree().getSkill(skillId);
                    if (s != null) {
                        int remaining = s.maxLevel - s.getLevel() - player.getItemCount(item.name);
                        if (remaining <= 0) {
                            continue; // Đã bán hết, không giảm giá
                        }
                    }
                }
            }
            pool.add(item);
        }
        if (pool.isEmpty()) return;

        // Chọn đúng 2 vật phẩm ngẫu nhiên để áp dụng giảm giá từ 10% đến 50%
        int count = Math.min(2, pool.size());
        for (int i = 0; i < count && !pool.isEmpty(); i++) {
            int randIdx = MathUtils.random(pool.size() - 1);
            ItemData item = pool.remove(randIdx);
            int discountPercent = MathUtils.random(1, 5) * 10; // 10%, 20%, 30%, 40%, 50%
            shopDiscounts.put(item.name, discountPercent);
        }
    }

    public static String getSkillIdFromBook(String bookName) {
        switch (bookName) {
            case "Sách: Dưỡng Sức":   return "duong_suc";
            case "Sách: Giật Kép":    return "giat_kep";
            case "Sách: Mắt Đại Bàng":return "mat_dai_bang";
            case "Sách: Thuần Hóa":   return "thuan_hoa";
            case "Sách: Trói Buộc":   return "loi_nguyen_troi_buoc";
            case "Sách: Trái Tim ĐD": return "trai_tim_dai_duong";
            default: return "";
        }
    }
}
