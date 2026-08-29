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

    public Map<String, Integer> getShopDiscounts() { return shopDiscounts; }

    public void recordSuccessfulCatch() {
        successfulCatchesSinceShopUpdate++;
        if (successfulCatchesSinceShopUpdate >= 5) {
            successfulCatchesSinceShopUpdate = 0;
            shopDiscounts.clear();
            
            List<ItemData> shopItems = ItemDatabase.getShopItems();
            if (shopItems.size() > 0) {
                int numDiscounts = MathUtils.random(1, 3);
                List<ItemData> availableItems = new ArrayList<>(shopItems);
                
                for (int i = 0; i < numDiscounts && !availableItems.isEmpty(); i++) {
                    int randIdx = MathUtils.random(availableItems.size() - 1);
                    ItemData item = availableItems.remove(randIdx);
                    
                    int discountPercent = MathUtils.random(1, 5) * 10;
                    shopDiscounts.put(item.name, discountPercent);
                }
            }
        }
    }
}
