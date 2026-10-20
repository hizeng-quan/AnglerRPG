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
    private int successfulCatchesSinceShopUpdate = 0;
    private final Map<String, Integer> shopDiscounts = new HashMap<>();

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

        List<ItemData> pool = new ArrayList<>(shopItems);
        // Chọn đúng 2 vật phẩm ngẫu nhiên để áp dụng giảm giá từ 10% đến 50%
        int count = Math.min(2, pool.size());
        for (int i = 0; i < count && !pool.isEmpty(); i++) {
            int randIdx = MathUtils.random(pool.size() - 1);
            ItemData item = pool.remove(randIdx);
            int discountPercent = MathUtils.random(1, 5) * 10; // 10%, 20%, 30%, 40%, 50%
            shopDiscounts.put(item.name, discountPercent);
        }
    }
}
