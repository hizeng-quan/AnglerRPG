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
        
        public Collection(String id, String name, int rewardGold, int rewardXp, String... fishes) {
            this.id = id;
            this.name = name;
            this.rewardGold = rewardGold;
            this.rewardXp = rewardXp;
            this.fishNames = Arrays.asList(fishes);
        }
    }

    private static final List<Collection> collections = new ArrayList<>();

    static {
        // --- AO LÀNG ---
        collections.add(new Collection("al_dan_da", "Dân Dã Ao Làng", 200, 100, 
            "Cá Rô Đồng", "Cá Diếc", "Cá Mương", "Cá Lóc Nhí"));
            
        collections.add(new Collection("al_quen_thuoc", "Gương Mặt Quen Thuộc", 500, 250, 
            "Cá Chép", "Cá Mè", "Cá Trê"));
            
        collections.add(new Collection("al_kinh_ngu", "Kình Ngư Nước Ngọt", 800, 400, 
            "Cá Trôi", "Cá Trắm Cỏ", "Cá Chim Trắng"));
            
        collections.add(new Collection("al_san_moi", "Sát Thủ Đầm Lầy", 3000, 1500, 
            "Cá Lóc Bông", "Cá Trê Lai", "Cá Chép Vàng"));
            
        collections.add(new Collection("al_truyen_thuyet", "Truyền Thuyết Vực Thẳm", 10000, 5000, 
            "Cá Lăng Khổng Lồ", "Thủy Quái Sông Trà"));
            
        // --- BÃI BIỂN ---
        collections.add(new Collection("bb_ven_bo", "Ven Bờ", 500, 300, 
            "Cá Chỉ Vàng", "Cá Đục", "Cá Cơm"));
            
        collections.add(new Collection("bb_bap_benh", "Bập Bềnh", 800, 500, 
            "Cá Mòi", "Cá Lẹp", "Cá Bạc Má"));
            
        collections.add(new Collection("bb_xa_bo", "Xa Bờ", 1500, 800, 
            "Cá Nục", "Cá Đối", "Cá Trích", "Cá Chim Biển"));
            
        collections.add(new Collection("bb_sat_thu", "Sát Thủ Đáy Biển", 3000, 1500, 
            "Cá Mú", "Cá Bóp", "Cá Thu Hoàng Kim"));
            
        collections.add(new Collection("bb_vua_bien", "Vua Của Biển", 8000, 4000, 
            "Cá Ngừ Đại Dương", "Bạch Tuộc Khổng Lồ"));

        // --- ĐẢO XA ---
        collections.add(new Collection("dx_ran_san_ho", "Rạn San Hô", 1000, 500, 
            "Cá Ngựa Vằn", "Cá Hề", "Cá Đuối Nhỏ"));
            
        collections.add(new Collection("dx_doc_la", "Sinh Vật Độc Lạ", 2000, 1000, 
            "Mực Ống", "Cá Nóc", "Cá Phèn"));
            
        collections.add(new Collection("dx_cu_dan", "Cư Dân Đại Dương", 4000, 2000, 
            "Cá Mặt Trăng", "Cá Cờ", "Rùa Biển", "Cá Chuồn"));
            
        collections.add(new Collection("dx_san_moi", "Hung Thần Răng Cưa", 8000, 4000, 
            "Cá Mập Cáo", "Cá Kiếm"));
            
        collections.add(new Collection("dx_than_thoai", "Thần Thoại Biển Khơi", 20000, 10000, 
            "Cá Voi Lưng Gù", "Mực Khổng Lồ", "Thần Biển Leviathan"));
    }

    public static List<Collection> getAllCollections() {
        return collections;
    }
}
