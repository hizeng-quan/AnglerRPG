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
        COMMON, UNCOMMON, RARE, EPIC, BOSS;

        /** Mau ten hien thi theo do hiem. */
        public Color getColor() {
            switch (this) {
                case COMMON:   return Color.WHITE;
                case UNCOMMON: return Color.GREEN;
                case RARE:     return new Color(0.3f, 0.6f, 1f, 1f);   // xanh duong
                case EPIC:     return new Color(0.7f, 0.3f, 1f, 1f);   // tim
                case BOSS:     return new Color(1f, 0.15f, 0.15f, 1f); // do
                default:       return Color.WHITE;
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
               id.equals("quet_xuc_tu") || id.equals("muc_mu_hut_mau");
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
            default:                return "Kỹ năng đặc biệt của thủy tộc";
        }
    }

    // =========================================================================
    // Combat API (giu nguyen de PlayScreen khong doi)
    // =========================================================================

    public void takeDamage(float damage) {
        currentHp = Math.max(0, Math.min(maxHp, currentHp - damage));
    }

    public boolean isCaught() { return currentHp <= 0; }
    
    public void update(float delta, float extraIdleTime) {
        if (isCaught()) return;
        stateTimer -= delta;
        if (stateTimer <= 0) {
            if (state == State.IDLE) {
                state = State.PULLING;
                stateTimer = MathUtils.random(1.5f, 3.5f);
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
        if (rarity == Rarity.RARE || rarity == Rarity.EPIC)   badges.add("SPECIAL");
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
