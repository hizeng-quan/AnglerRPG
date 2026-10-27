package com.fishingrpg.game.data;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class CollectionDatabase {

    public static class Collection {
        public String id;
        public String name;
        public List<String> fishNames;
        public int rewardGold;
        public int rewardXp;
        public int rewardDiamonds;
        
        public Collection(String id, String name, int rewardGold, int rewardXp, int rewardDiamonds, String... fishes) {
            this.id = id;
            this.name = name;
            this.rewardGold = rewardGold;
            this.rewardXp = rewardXp;
            this.rewardDiamonds = rewardDiamonds;
            this.fishNames = Arrays.asList(fishes);
        }

        public Collection(String id, String name, int rewardGold, int rewardXp, String... fishes) {
            this(id, name, rewardGold, rewardXp, 0, fishes);
        }
    }

    private static final List<Collection> collections = new ArrayList<>();

    static {
        // --- AO LÀNG ---
        collections.add(new Collection("al_dan_da", "Dân Dã Ao Làng", 200, 100, 10,
            "Cá Rô Đồng", "Cá Diếc", "Cá Mương", "Cá Lóc Nhí"));
            
        collections.add(new Collection("al_quen_thuoc", "Gương Mặt Quen Thuộc", 500, 250, 15,
            "Cá Chép", "Cá Mè", "Cá Trê"));
            
        collections.add(new Collection("al_kinh_ngu", "Kình Ngư Nước Ngọt", 800, 400, 20,
            "Cá Trôi", "Cá Trắm Cỏ", "Cá Chim Trắng"));
            
        collections.add(new Collection("al_san_moi", "Sát Thủ Đầm Lầy", 3000, 1500, 35,
            "Cá Lóc Bông", "Cá Trê Lai", "Cá Chép Vàng"));
            
        collections.add(new Collection("al_truyen_thuyet", "Truyền Thuyết Sông Hồ", 10000, 5000, 60,
            "Cá Lăng Khổng Lồ", "Thủy Quái Sông Trà"));
            
        // --- BÃI BIỂN ---
        collections.add(new Collection("bb_ven_bo", "Ven Bờ", 500, 300, 15,
            "Cá Chỉ Vàng", "Cá Đục", "Cá Cơm"));
            
        collections.add(new Collection("bb_bap_benh", "Bập Bềnh", 800, 500, 20,
            "Cá Mòi", "Cá Lẹp", "Cá Bạc Má"));
            
        collections.add(new Collection("bb_xa_bo", "Xa Bờ", 1500, 800, 25,
            "Cá Nục", "Cá Đối", "Cá Trích", "Cá Chim Biển"));
            
        collections.add(new Collection("bb_sat_thu", "Thợ Săn Đáy Biển", 3000, 1500, 40,
            "Cá Mú", "Cá Bóp", "Cá Thu Hoàng Kim"));
            
        collections.add(new Collection("bb_vua_bien", "Vua Của Biển Cả", 8000, 4000, 70,
            "Cá Ngừ Đại Dương", "Bạch Tuộc Khổng Lồ"));

        // --- ĐẢO XA ---
        collections.add(new Collection("dx_ran_san_ho", "Rạn San Hô Đảo Xa", 1500, 800, 25,
            "Cá Mú Đỏ", "Cá Hồng Chuối", "Cá Dìa Biển Đảo"));
            
        collections.add(new Collection("dx_kinh_ngu_nong", "Kình Ngư Nước Nông", 2500, 1200, 35,
            "Cá Nhồng Biển", "Cá Bè Trang", "Cá Nục Heo"));
            
        collections.add(new Collection("dx_dong_chay_khoi", "Dòng Chảy Khơi Xa", 5000, 2500, 50,
            "Cá Bớp Khơi", "Cá Cam Biển", "Cá Thu Ngàng", "Cá Đuối Hoa Cương"));
            
        collections.add(new Collection("dx_chien_ham_dai_duong", "Chiến Hạm Đại Dương", 10000, 5000, 75,
            "Cá Ngừ Vây Vàng", "Cá Cờ Gòn", "Cá Mú Nghệ Khơi", "Cá Mập Rạn Đen"));
            
        collections.add(new Collection("dx_huyen_thoai_bien_sau", "Huyền Thoại Biển Sâu", 25000, 12000, 120,
            "Cá Cờ Xanh Khổng Lồ"));

        // --- HALLOWEEN TOÀN CẦU (HLW-03.6) ---
        collections.add(new Collection("hlw_dem_ma_quai", "Huyền Thoại Đêm Halloween", 50000, 25000, 500,
            "Cá Skeleton", "Cá Đèn Lồng Jack-o'", "Cá Đầu Lâu Huyết Nguyệt",
            "Cá Dạng Ma", "Thủy Quái Frankenstein", "Hải Xà Dạ Xoa", "Kình Ngư Tử Thần Thanatos"));
    }

    public static List<Collection> getAllCollections() {
        return collections;
    }
}
