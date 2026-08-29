package com.fishingrpg.game.data;

import com.fishingrpg.game.entities.ItemData;
import com.fishingrpg.game.entities.ItemUsageType;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class ItemDatabase {
    
    private static final Map<String, ItemData> items = new HashMap<>();
    private static final List<ItemData> shopItems = new ArrayList<>();

    static {
        // Base items
        registerItem(new ItemData("Bánh Mì", "Hồi 25 Thể lực", ItemUsageType.BOTH, 50), true);
        registerItem(new ItemData("Mồi Thơm", "Tăng tỷ lệ cá hiếm", ItemUsageType.PRE_COMBAT, 100), true);
        registerItem(new ItemData("Dây Câu Tốt", "Giảm 20 Tension", ItemUsageType.IN_COMBAT, 50), true);
        registerItem(new ItemData("Thuốc Hồi Stamina", "Hồi 50 Thể lực (10/s)", ItemUsageType.BOTH, 100), true);
        
        // New proposed items
        registerItem(new ItemData("Nước Tăng Lực", "Hồi 50 Thể lực", ItemUsageType.BOTH, 150), true);
        registerItem(new ItemData("Lưới Đánh Cá", "Cơ hội bắt được x2-x4 lượng cá trong lần tới", ItemUsageType.PRE_COMBAT, 300), true);
        registerItem(new ItemData("Bùa Hoàng Kim", "Tăng 50% Vàng nhận được (5 lượt)", ItemUsageType.PRE_COMBAT, 500), true);
        registerItem(new ItemData("Sách Thông Thái", "Tăng 50% EXP nhận được (5 lượt)", ItemUsageType.PRE_COMBAT, 500), true);
        registerItem(new ItemData("Mồi Rủi Ro", "Tăng mạnh tỷ lệ cá cực hiếm nhưng Tension tăng nhanh x2", ItemUsageType.PRE_COMBAT, 250), true);
        registerItem(new ItemData("Nước Bứt Phá", "Hồi đầy Thể lực, nhưng giảm 30% Max Tension lượt tới", ItemUsageType.BOTH, 400), true);
        
        // Skill Books
        registerItem(new ItemData("Sách: Dưỡng Sức", "Học kỹ năng Dưỡng Sức", ItemUsageType.PRE_COMBAT, 1000), true);
        registerItem(new ItemData("Sách: Giật Kép", "Học kỹ năng Giật Kép", ItemUsageType.PRE_COMBAT, 1500), true);
        registerItem(new ItemData("Sách: Mắt Đại Bàng", "Học kỹ năng Mắt Đại Bàng", ItemUsageType.PRE_COMBAT, 2000), true);
    }
    
    private static void registerItem(ItemData item, boolean inShop) {
        items.put(item.name, item);
        if (inShop) {
            shopItems.add(item);
        }
    }
    
    public static ItemData getItem(String name) {
        return items.get(name);
    }
    
    public static List<ItemData> getShopItems() {
        return shopItems;
    }
}
