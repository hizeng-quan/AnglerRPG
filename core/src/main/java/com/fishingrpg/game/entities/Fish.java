package com.fishingrpg.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.fishingrpg.game.loot.Catchable;
import com.fishingrpg.game.loot.CatchableType;

import java.util.ArrayList;
import java.util.List;

/**
 * Mot con ca cu the, voi kich thuoc ngau nhien trong khoang cua loai.
 * Implement Catchable de dung chung voi he thong LootTable.
 */
public class Fish implements Catchable {

    // =========================================================================
    // Enum do hiem
    // =========================================================================

    public enum Rarity {
        COMMON, UNCOMMON, RARE, EPIC, LEGENDARY, BOSS;

        /** Mau ten hien thi theo do hiem. */
        public Color getColor() {
            switch (this) {
                case COMMON:    return Color.WHITE;
                case UNCOMMON:  return Color.GREEN;
                case RARE:      return new Color(0.3f, 0.6f, 1f, 1f);   // xanh duong
                case EPIC:      return new Color(0.7f, 0.3f, 1f, 1f);   // tim
                case LEGENDARY: return new Color(1f, 0.65f, 0.1f, 1f);   // cam vang neon
                case BOSS:      return new Color(1f, 0.15f, 0.15f, 1f); // do
                default:        return Color.WHITE;
            }
        }
    }

    // =========================================================================
    // Fields
    // =========================================================================

    // --- Combat ---
    private final String name;
    private final float  maxHp;
    private       float  currentHp;
    private final float  pullStrength; // luc keo day (units/giay)
    private       float  shieldHp = 0f;
    private       float  maxShieldHp = 0f;
    
    public enum State { IDLE, PULLING }
    private State state = State.IDLE;
    private float stateTimer = 0f;

    // --- Loai & Kich thuoc ---
    private final Rarity rarity;
    private final float  baseWeightKg;  // can nang trung binh cua loai
    private final float  weightKg;      // can nang thuc te lan nay (ngau nhien)
    private final float  lengthCm;      // chieu dai tuong ung

    // --- Phần thưởng ---
    private final int baseXp;
    private final int baseGold;

    // --- Kỹ năng ---
    private java.util.ArrayList<FishSkill> skills;

    // --- Meta ---
    private boolean firstCatch = false; // duoc set boi SizeRecord sau khi spawn

    // =========================================================================
    // Constructor (duoc goi boi FishDatabase)
    // =========================================================================

    /**
     * @param name          Ten loai ca
     * @param maxHp         HP toi da
     * @param pullStrength  Luc keo day (units/giay)
     * @param rarity        Do hiem
     * @param baseWeightKg  Can nang trung binh cua loai (kg)
     * @param baseLengthCm  Chieu dai trung binh cua loai (cm)
     * @param baseXp        XP co ban (truoc khi nhan he so kich thuoc)
     * @param baseGold      Vang co ban
     * @param skills        Danh sach ky nang cua ca
     */
    public Fish(String name, float maxHp, float pullStrength,
                Rarity rarity, float baseWeightKg, float baseLengthCm,
                int baseXp, int baseGold, java.util.ArrayList<FishSkill> skills) {
        this.name         = name;
        this.maxHp        = maxHp;
        this.currentHp    = maxHp;
        this.pullStrength = pullStrength;
        this.rarity       = rarity;
        this.baseWeightKg = baseWeightKg;
        this.baseXp       = baseXp;
        this.baseGold     = baseGold;
        this.skills       = skills != null ? skills : new java.util.ArrayList<FishSkill>();

        // Ngau nhien hoa kich thuoc: 0.5x den 2.0x so voi base
        float sizeMultiplier = MathUtils.random(0.5f, 2.0f);
        this.weightKg = baseWeightKg * sizeMultiplier;
        // Chieu dai tang cham hon can nang (quan he the tich ~ chieu dai^3)
        this.lengthCm = baseLengthCm * (float) Math.pow(sizeMultiplier, 0.333);
        
        // Fix up skills from JSON (JSON might have missing HP triggers or wrong isActive flags)
        int totalActive = 0;
        for (FishSkill s : this.skills) {
            if (isActiveSkill(s.id)) {
                s.isActive = true;
                totalActive++;
            } else {
                s.isActive = false;
            }
        }
        
        float[] hpTriggers;
        if (totalActive == 1) {
            hpTriggers = new float[]{ 0.5f }; // Trigger at 50%
        } else if (totalActive == 2) {
            hpTriggers = new float[]{ 0.6f, 0.3f }; // Trigger at 60% and 30%
        } else {
            hpTriggers = new float[]{ 0.8f, 0.5f, 0.2f }; // Trigger at 80%, 50%, 20%
        }
        
        int activeIdx = 0;
        for (FishSkill s : this.skills) {
            if (s.isActive && s.triggerHpPercent <= 0f) {
                s.triggerHpPercent = hpTriggers[Math.min(activeIdx, hpTriggers.length - 1)];
                activeIdx++;
            }
        }
    }
    
    private boolean isActiveSkill(String id) {
        return id.equals("quay_bun") || id.equals("but_toc") || id.equals("lan_sau") ||
               id.equals("song_than") || id.equals("cuong_no") || id.equals("phun_muc") ||
               id.equals("giat_dien") || id.equals("nhay_vot") || id.equals("song_am") ||
               id.equals("phun_muc_mu") || id.equals("hut_mau") || id.equals("bao_to") ||
               id.equals("quet_xuc_tu") || id.equals("muc_mu_hut_mau") ||
               id.equals("xoay_nuoc") || id.equals("vu_dieu_co_buom") ||
               id.equals("vuc_da_nuot_chung") || id.equals("can_xe_dien_cuong") ||
               id.equals("vu_dieu_luot_song") || id.equals("cuong_phong_ngu_kiem") ||
               id.equals("hoa_dang_jack_o") || id.equals("xung_kich_doat_hon") ||
               id.equals("tieng_than_u_am") || id.equals("sam_set_hoi_sinh") ||
               id.equals("mang_xa_siet_cuoc") || id.equals("doc_to_am_ty") ||
               id.equals("tieng_hu_dia_nguc") || id.equals("vuc_xoay_am_ty") ||
               id.equals("am_hon_quay_pha") || id.equals("lien_hoan_tram_doat_menh");
    }

    public static String getSkillDisplayName(String id) {
        if (id == null) return "";
        switch (id) {
            case "vung_vay":     return "Vùng vẫy";
            case "ben_bi":       return "Bền bỉ";
            case "tron_truot":   return "Trơn trượt";
            case "an_minh":      return "Ẩn mình";
            case "vo_cung":      return "Vỏ cứng";
            case "lan_sau":      return "Lặn sâu";
            case "quay_bun":     return "Quẫy bùn";
            case "but_toc":      return "Bứt tốc";
            case "nhay_vot":     return "Nhảy vọt";
            case "song_than":    return "Sóng thần";
            case "cuong_no":     return "Cuồng nộ";
            case "phun_muc":     return "Phun mực";
            case "phun_muc_mu":  return "Phun mực mù";
            case "giat_dien":    return "Giật điện";
            case "song_am":      return "Sóng âm";
            case "bao_to":       return "Bão tố";
            case "xuc_tu":       return "Xúc tu siết";
            case "quet_xuc_tu":  return "Quét xúc tu";
            case "hut_mau":      return "Hút máu";
            case "muc_mu_hut_mau": return "Mực mù hút máu";
            case "gai_doc_san_ho": return "Gai độc san hô";
            case "can_day":      return "Răng nanh cắt dây";
            case "doi_huong":    return "Đổi hướng đột ngột";
            case "tro_luc":      return "Trơ lực kéo";
            case "nuong_hai_luu": return "Nương hải lưu";
            case "than_dao":     return "Thân đao xé gió";
            case "phong_dien":   return "Gai đuôi phóng điện";
            case "xoay_nuoc":    return "Xoáy nước đại dương";
            case "vu_dieu_co_buom": return "Vũ điệu cờ buồm";
            case "than_ho_co_thach": return "Thân hộ cổ thạch";
            case "vuc_da_nuot_chung": return "Vực đá nuốt chửng";
            case "thinh_mui_mau": return "Thính mùi máu";
            case "can_xe_dien_cuong": return "Cắn xé điên cuồng";
            case "mui_kiem_xe_bien": return "Mũi kiếm xé biển";
            case "vu_dieu_luot_song": return "Vũ điệu lướt sóng";
            case "cuong_phong_ngu_kiem": return "Cuồng phong ngự kiếm";
            // Kỹ năng Halloween (HLW-03)
            case "cot_cach_bat_diet": return "Cốt Cách Bất Diệt";
            case "hoa_nguc_lan_tinh": return "Hỏa Ngục Lân Tinh";
            case "hoa_dang_jack_o": return "Hỏa Đăng Jack-o'";
            case "khat_mau_dem_ram": return "Khát Máu Đêm Rằm";
            case "xung_kich_doat_hon": return "Xung Kích Đoạt Hồn";
            case "the_xac_hu_vo": return "Thể Xác Hư Vô";
            case "tieng_than_u_am": return "Tiếng Than U Ám";
            case "qua_tai_dien_nang": return "Quá Tải Điện Năng";
            case "sam_set_hoi_sinh": return "Sấm Sét Hồi Sinh";
            case "uon_luon_ma_quai": return "Uốn Lượn Ma Quái";
            case "mang_xa_siet_cuoc": return "Mãng Xà Siết Cước";
            case "doc_to_am_ty": return "Độc Tố Âm Ty";
            case "uy_ap_tu_than": return "Uy Áp Tử Thần";
            case "tieng_hu_dia_nguc": return "Tiếng Hú Địa Ngục";
            case "vuc_xoay_am_ty": return "Vực Xoáy Âm Ty";
            case "am_hon_quay_pha": return "Âm Hồn Quấy Phá";
            case "lien_hoan_tram_doat_menh": return "Liên Hoàn Trảm Đoạt Mệnh";
            case "tu_than_boc_phat": return "Tử Thần Bộc Phát";
            default:             return id.replace("_", " ");
        }
    }

    public static String getSkillDescription(String id) {
        if (id == null) return "";
        switch (id) {
            case "quay_bun":        return "Quẫy bùn che khuất hoàn toàn thanh Tension (4.5s)";
            case "lan_sau":         return "Lặn sâu hang đá ngầm, giật tăng đột ngột +30% Tension";
            case "cuong_no":        return "Dưới 25% máu: Giải khống chế, bất tử 3.5s & x1.8 lực kéo";
            case "but_toc":         return "Bứt tốc cực mạnh, tăng lực kéo x2 (3s)";
            case "song_than":       return "Dậy sóng thần dữ dội, tiêu hao thể lực cần thủ liên tục";
            case "giat_dien":       return "Phóng sét dọc dây câu, choáng váng nếu giữ kéo dây (2s)";
            case "nhay_vot":        return "Phóng lên không trung, cần nhả dây kẻo tiếp nước đứt cước";
            case "phun_muc":        return "Phun mực đen loang lổ che khuất tầm nhìn (4s)";
            case "phun_muc_mu":     return "Màn đêm mực mù che tối hoàn toàn màn hình (4.5s)";
            case "song_am":         return "Sóng âm chấn động gây nhiễu loạn kim đo Tension (2s)";
            case "quet_xuc_tu":     return "Quét xúc tu tăng +35% Tension và trừ 25 Thể lực";
            case "hut_mau":         return "Mỏ quạ cắn xé hút thể lực cần thủ hồi sinh mệnh cá";
            case "muc_mu_hut_mau":  return "Mực mù che màn đêm & Hút thể lực hồi máu cho cá";
            case "bao_to":          return "Triệu hồi bão tố biển sâu, sóng giật cực đại";
            case "vo_cung":         return "Nội tại: Giáp sừng/vảy rùa đá giảm 20% sát thương nhận vào";
            case "ben_bi":          return "Nội tại: Thể chất dẻo dai giảm 15% sát thương nhận vào";
            case "tron_truot":      return "Nội tại: Lớp nhớt trơn trượt giảm 20% sát thương nhận vào";
            case "an_minh":         return "Nội tại: Khi IDLE giảm 25% sát thương nhận vào";
            case "vung_vay":        return "Nội tại: Thỉnh thoảng quẫy mạnh đổi hướng giằng co";
            case "xuc_tu":          return "Nội tại: Xúc tu quấn chặt làm dây chùng chậm hơn 30%";
            case "gai_doc_san_ho":  return "Nội tại: Gai độc cọ xát rút 1.5 Thể lực/s khi cần thủ kéo dây lúc cá PULLING";
            case "can_day":         return "Nội tại: 25% tỷ lệ khi PULLING cắn mòn giảm 10% Max Tension an toàn (4s)";
            case "doi_huong":       return "Nội tại: Bơi lượn đổi góc làm kim đo Tension dao động chao đảo ±10% (1.5s)";
            case "tro_luc":         return "Nội tại: Da lì lợm có 25% tỷ lệ miễn nhiễm hoàn toàn sát thương kéo từ phím Space";
            case "nuong_hai_luu":   return "Nội tại: Khi Tension trên 55%, lực kéo của cá tự động tăng thêm +18%";
            case "than_dao":        return "Nội tại: Pha PULLING rút ngắn 40% thời gian nhưng lực kéo tăng +40% và giật +12% Tension khi bắt đầu bơi";
            case "phong_dien":      return "Nội tại: Giữ phím kéo Space liên tục quá 2.5s sẽ bị sốc điện trừ 12 Thể lực";
            case "xoay_nuoc":       return "Bơi xoáy làm Tension chao đảo ±15% và giảm 40% tốc độ xả chùng dây (3.5s)";
            case "vu_dieu_co_buom": return "Phóng lướt 2 nhịp trên mặt sóng; mỗi nhịp nếu không Perfect Re-Hook sẽ giật +30 Tension";
            case "than_ho_co_thach": return "Nội tại: Vảy rạn ngầm ngàn năm giảm phẳng -15 sát thương mỗi đòn kéo dây";
            case "vuc_da_nuot_chung": return "Hút xoáy đáy ngầm giật +20% Tension và giảm 30% hồi Thể lực người chơi từ mọi nguồn đến hết trận";
            case "thinh_mui_mau":   return "Nội tại: Khi Thể lực cần thủ dưới 50%, cá mập kích động tăng +20% lực kéo";
            case "can_xe_dien_cuong": return "Ngoạm cước giảm 20% Max Tension (5s); nếu kích hoạt Thính Mùi Máu sẽ giảm 30% Max Tension và trừ 30 Thể lực";
            case "mui_kiem_xe_bien": return "Nội tại: Ghìm kéo >1.2s làm Tension dâng nhanh +20%/s. Kháng khống chế";
            case "vu_dieu_luot_song": return "Cứ 8s phi thân 0.9s; tiếp nước sốc +20% Tension, -15 Thể lực & hồi 8% máu boss trừ khi Perfect Re-Hook";
            case "cuong_phong_ngu_kiem": return "Dưới 30% HP: Mỗi lần Vũ Điệu Lướt Sóng tăng +10% lực kéo; khi kích hoạt giúp boss bất tử khi ở trên không";
            // Kỹ năng Halloween (HLW-03)
            case "cot_cach_bat_diet": return "Nội tại: Giảm 20% sát thương đòn kéo thường, nhưng chịu thêm +50% sát thương từ kỹ năng";
            case "hoa_nguc_lan_tinh": return "Nội tại: Khi cá PULLING tăng +25% sinh tension; nếu tension > 70% thiêu đốt 3 Thể lực/giây";
            case "hoa_dang_jack_o": return "Tụ lửa ma quái 1.2s rồi nổ giật +15% Tension và thiêu đốt 10 Thể lực (ngắt được bằng Trói Buộc)";
            case "khat_mau_dem_ram": return "Nội tại: Khi Thể lực người chơi < 50%, cá ngửi mùi máu hồi phục +1% Max HP mỗi giây";
            case "xung_kich_doat_hon": return "Lướt cướp linh hồn Re-Hook 0.6s; nếu trượt phạt giật +25 Tension và cá hồi 15% máu đã mất";
            case "the_xac_hu_vo": return "Nội tại: Cứ mỗi 8s toàn thân hóa khói hư vô trong 2.5s, hoàn toàn miễn nhiễm mọi sát thương";
            case "tieng_than_u_am": return "Hút thẳng 15 Thể lực hồi 5% HP cho cá, giảm 50% hiệu quả hồi phục Thể lực người chơi trong 5s";
            case "qua_tai_dien_nang": return "Nội tại: Kéo dây liên tục quá 1.5s phóng ngược giật điện trừ 10 Thể lực & ngắt kéo dây 0.6s";
            case "sam_set_hoi_sinh": return "Duy nhất 1 lần khi cạn máu: Frankenstein phóng sét hồi sinh 20% HP & giật +25% Tension";
            case "uon_luon_ma_quai": return "Nội tại: Giảm 30% tốc độ xả chùng Slack; mỗi khi đổi sang PULLING quất đuôi giật +10 Tension";
            case "mang_xa_siet_cuoc": return "Cuộn thân quanh cước khóa cứng Tension không thể giảm trong 3s; kéo lúc này sinh tension x2";
            case "doc_to_am_ty": return "Phun nọc hắc xà rút 5 Thể lực/giây trong 5s và phủ sương mù tím ma mị che tầm nhìn";
            case "uy_ap_tu_than": return "Nội tại: Uy áp Thần Chết tăng +20% tiêu hao Thể lực của toàn bộ kỹ năng người chơi";
            case "tieng_hu_dia_nguc": return "Tiếng rống địa ngục phát sóng âm câm lặng: Vô hiệu hóa kỹ năng tiếp theo của người chơi";
            case "vuc_xoay_am_ty": return "Xoáy nước kéo căng dây +15 Tension/s trong 3s VÀ hút sinh lực hồi 3% Max HP cho Thanatos";
            case "am_hon_quay_pha": return "Nuốt linh hồn tạo Soul Shield 500 HP; sát thương thường trừ vào giáp trước, Phá Giáp đánh xuyên máu thật";
            case "lien_hoan_tram_doat_menh": return "Tuyệt kỹ 2 nhịp Re-Hook (0.5s & 0.6s): 2/2 choáng boss 3.5s nhận x2 dmg; 1/2 phạt tê liệt 4s; 0/2 phong ấn skill, khóa hồi TL & đứt dây nếu tension > 90%";
            case "tu_than_boc_phat": return "Dưới 30% HP: Giảm 30% thời gian hồi chiêu tất cả kỹ năng thường của Thanatos";
            default:                return "Kỹ năng đặc biệt của thủy tộc";
        }
    }

    // =========================================================================
    // Combat API (giu nguyen de PlayScreen khong doi)
    // =========================================================================

    public void takeDamage(float damage) {
        if (hasShield()) {
            float remaining = damageShield(damage);
            if (remaining > 0f) {
                currentHp = Math.max(0, Math.min(maxHp, currentHp - remaining));
            }
        } else {
            currentHp = Math.max(0, Math.min(maxHp, currentHp - damage));
        }
    }

    /** Sát thương chuẩn / xuyên giáp: trừ trực tiếp vào HP thật của cá. */
    public void takeTrueDamage(float damage) {
        currentHp = Math.max(0, Math.min(maxHp, currentHp - damage));
    }

    public void heal(float amount) {
        if (isCaught() || amount <= 0f) return;
        currentHp = Math.min(maxHp, currentHp + amount);
    }

    public float getShieldHp() { return shieldHp; }
    public float getMaxShieldHp() { return maxShieldHp; }
    public void setShield(float amount) {
        this.shieldHp = amount;
        this.maxShieldHp = amount;
    }
    public void clearShield() {
        this.shieldHp = 0f;
        this.maxShieldHp = 0f;
    }
    public boolean hasShield() {
        return shieldHp > 0f;
    }

    /**
     * Trừ sát thương vào lớp giáp. Trả về lượng sát thương dư thừa (nếu giáp bị vỡ).
     */
    public float damageShield(float amount) {
        if (shieldHp <= 0f) return amount;
        if (shieldHp >= amount) {
            shieldHp -= amount;
            return 0f;
        } else {
            float remaining = amount - shieldHp;
            shieldHp = 0f;
            return remaining;
        }
    }

    public boolean isCaught() { return currentHp <= 0; }
    
    public void update(float delta, float extraIdleTime) {
        if (isCaught()) return;
        stateTimer -= delta;
        if (stateTimer <= 0) {
            if (state == State.IDLE) {
                state = State.PULLING;
                float dur = MathUtils.random(1.5f, 3.5f);
                if (getPassiveSkill("than_dao") != null) {
                    dur *= 0.60f; // Pha PULLING rut ngan 40% thoi gian
                }
                stateTimer = dur;
            } else {
                state = State.IDLE;
                float anMinhBonus = (getPassiveSkill("an_minh") != null) ? 1.5f : 0f;
                stateTimer = MathUtils.random(1.5f, 3.5f) + extraIdleTime + anMinhBonus;
            }
        }
    }
    
    public float getCurrentPull() {
        return state == State.PULLING ? pullStrength : 0f;
    }

    public float getPullStrength() {
        return pullStrength;
    }

    public java.util.ArrayList<FishSkill> getSkills() {
        return skills;
    }
    
    public FishSkill getPassiveSkill(String id) {
        for (FishSkill s : skills) {
            if (!s.isActive && s.id.equals(id)) return s;
        }
        return null;
    }

    public FishSkill getSkill(String id) {
        for (FishSkill s : skills) {
            if (s.id.equals(id)) return s;
        }
        return null;
    }

    // =========================================================================
    // Catchable impl
    // =========================================================================

    @Override public String       getName()        { return name; }
    @Override public CatchableType getType()        { return CatchableType.FISH; }
    @Override public boolean      requiresCombat() { return true; }

    /** Vang tinh theo kich thuoc + do hiem. */
    @Override
    public int getGoldValue() {
        float sizeRatio  = weightKg / baseWeightKg;
        float sizeBonus  = isBig() ? 1.5f : 1.0f;
        return Math.round(baseGold * sizeRatio * sizeBonus);
    }

    /** XP tinh theo kich thuoc. */
    @Override
    public int getXpValue() {
        float sizeRatio = weightKg / baseWeightKg;
        return Math.round(baseXp * sizeRatio);
    }

    // =========================================================================
    // Badge logic
    // =========================================================================

    /** Ca to hon 120% can nang trung binh. */
    public boolean isBig() { return weightKg > baseWeightKg * 1.2f; }

    /**
     * Danh sach badge xuat hien khi ca cat cau.
     * firstCatch phai duoc set truoc khi goi ham nay.
     */
    public List<String> getBadges() {
        List<String> badges = new ArrayList<>();
        if (firstCatch)                                         badges.add("NEW");
        if (isBig())                                            badges.add("BIG");
        if (rarity == Rarity.RARE || rarity == Rarity.EPIC || rarity == Rarity.LEGENDARY) badges.add("SPECIAL");
        if (rarity == Rarity.BOSS)                             badges.add("BOSS");
        return badges;
    }

    // =========================================================================
    // Getters
    // =========================================================================

    public float   getCurrentHp()   { return currentHp;    }
    public float   getMaxHp()       { return maxHp;        }
    public Rarity  getRarity()      { return rarity;       }
    public Color   getRarityColor() { return rarity.getColor(); }
    public float   getBaseWeightKg(){ return baseWeightKg; }
    public float   getWeightKg()    { return weightKg;     }
    public float   getLengthCm()    { return lengthCm;     }
    public int     getBaseGold()    { return baseGold;     }
    public int     getBaseXp()      { return baseXp;       }
    public boolean isFirstCatch()   { return firstCatch;   }
    public void    setFirstCatch(boolean v) { firstCatch = v; }
    public State   getState()       { return state;        }
}
