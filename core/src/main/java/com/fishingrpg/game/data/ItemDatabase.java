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
        registerItem(new ItemData("Nước Tăng Lực", "Hồi 25% Thể lực tối đa", ItemUsageType.BOTH, 150), true);
        registerItem(new ItemData("Lưới Đánh Cá", "Cơ hội bắt được x2-x4 lượng cá trong lần tới", ItemUsageType.PRE_COMBAT, 300), true);
        registerItem(new ItemData("Bùa Hoàng Kim", "Tăng 50% Vàng nhận được (5 lượt)", ItemUsageType.PRE_COMBAT, 500), true);
        registerItem(new ItemData("Sách Thông Thái", "Tăng 50% EXP nhận được (5 lượt)", ItemUsageType.PRE_COMBAT, 500), true);
        registerItem(new ItemData("Mồi Rủi Ro", "Tăng mạnh tỷ lệ cá cực hiếm nhưng Tension tăng nhanh x2", ItemUsageType.PRE_COMBAT, 250), true);
        registerItem(new ItemData("Nước Bứt Phá", "Hồi đầy Thể lực, nhưng giảm 30% Max Tension lượt tới", ItemUsageType.BOTH, 400), true);
        
        // New items with unique mechanics
        registerItem(new ItemData("Bình Xịt Đóng Băng", "Đóng băng cá 2s trong lúc câu (ngừng giằng & không tăng Tension)", ItemUsageType.IN_COMBAT, 500), true);
        registerItem(new ItemData("Trà Thảo Mộc", "Hóa giải toàn bộ kỹ năng & trạng thái bất lợi của cá", ItemUsageType.IN_COMBAT, 750), true);
        registerItem(new ItemData("Đá Mài Lưỡi Câu", "Tăng +25% sát thương kéo cá & cá cắn câu nhanh hơn 40% (5 lượt)", ItemUsageType.PRE_COMBAT, 750), true);
        registerItem(new ItemData("Nam Châm Kho Báu", "Tăng 400% cơ hội câu trúng Rương Báu & Cổ Vật cổ xưa (3 lượt)", ItemUsageType.PRE_COMBAT, 800), true);
        registerItem(new ItemData("Kính Nhìn Xuyên Nước", "Thấu thị mặt nước: Dự báo trước loài cá & độ hiếm sắp cắn câu (3 lượt)", ItemUsageType.BOTH, 400), true);
        
        // Skill Books (Học mới, Đột phá +1 cấp hoặc Quy đổi +1 SP)
        registerItem(new ItemData("Sách: Dưỡng Sức", "Học/Đột phá kỹ năng Dưỡng Sức (+1 Lv hoặc +1 SP)", ItemUsageType.PRE_COMBAT, 1000), true);
        registerItem(new ItemData("Sách: Giật Kép", "Học/Đột phá kỹ năng Giật Kép (+1 Lv hoặc +1 SP)", ItemUsageType.PRE_COMBAT, 1500), true);
        registerItem(new ItemData("Sách: Mắt Đại Bàng", "Học/Đột phá kỹ năng Mắt Đại Bàng (+1 Lv hoặc +1 SP)", ItemUsageType.PRE_COMBAT, 2000), true);
        registerItem(new ItemData("Sách: Thuần Hóa", "Học/Đột phá kỹ năng Thuần Hóa (+1 Lv hoặc +1 SP)", ItemUsageType.PRE_COMBAT, 1800), false);
        registerItem(new ItemData("Sách: Trói Buộc", "Học/Đột phá kỹ năng Trói Buộc (+1 Lv hoặc +1 SP)", ItemUsageType.PRE_COMBAT, 2200), false);
        registerItem(new ItemData("Sách: Trái Tim ĐD", "Học/Đột phá kỹ năng Trái Tim Đại Dương (+1 Lv hoặc +1 SP)", ItemUsageType.PRE_COMBAT, 2500), false);
        // Halloween Exclusives (HLW-05 - Gia Vang chuan hoa x2 & Ban trong Shop Thuong)
        registerItem(new ItemData("Kẹo Trick-or-Treat", "Kẹo ma thuật tăng x2 tỷ lệ gặp cá sự kiện Halloween & cá hiếm (15 phút)", ItemUsageType.BOTH, 300), true);
        registerItem(new ItemData("Đèn Lồng Bí Ngô", "Quang minh trừ tà: +30% thời gian phản ứng Re-Hook khống chế cá nhảy (3 lần)", ItemUsageType.BOTH, 700), true);
        registerItem(new ItemData("Độc Dược Ma Quái", "Độc dược hắc ám: 3 lần tung kỹ năng tiếp theo tiêu hao 0 Thể lực", ItemUsageType.BOTH, 900), true);
        registerItem(new ItemData("Bùa Huyết Nguyệt", "Trong trận: +10% Sát thương Chuẩn (15s). Trước trận: Gọi Đêm Huyết Nguyệt 15 phút", ItemUsageType.BOTH, 1000), true);
        registerItem(new ItemData("Nến Hồn Ma Bảo Hộ", "Hồn ma bảo hộ: Cứu 1 lần đứt cước khi Tension chạm 100%, kéo Tension về 50%", ItemUsageType.BOTH, 1200), true);
        registerItem(new ItemData("Rương Ma Quái", "Chiến tích đánh bại Thanatos: Mở nhận 3-5 Vé Gacha, 30-80 KC & 20-30 Kẹo", ItemUsageType.BOTH, 0), false);

        items.put("Nước Phù Thủy Ma Quái", getItem("Độc Dược Ma Quái"));
        items.put("Trà Thảo Mộc Định Thần", getItem("Trà Thảo Mộc"));
        items.put("Nước Bức Phá", getItem("Nước Bứt Phá"));
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
