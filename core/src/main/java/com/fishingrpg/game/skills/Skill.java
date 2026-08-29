package com.fishingrpg.game.skills;

/**
 * Mot node trong cay ky nang.
 * effects[]  -- hieu ung chinh moi cap (VD: damage, tension-reduction...).
 * effects2[] -- hieu ung phu moi cap (VD: stamina cost), co the null.
 * Ky nang phai unlock (level >= 1) truoc khi co hieu luc.
 */
public class Skill {

    public enum SkillType { ACTIVE, PASSIVE }
    
    public final String   id;
    public final String   name;
    public final String   description;
    public final SkillType type;
    public final String[] prerequisites; // ID cac ky nang can co truoc
    public final boolean  requiresBookToUnlock; // True neu bat buoc dung sach de hoc cap 1

    public final int   maxLevel;
    private      int   currentLevel = 0;

    /** Chi phi SP de nang cap len level tiep theo. cost[i] = phi de len level (i+1). */
    public final int[]   spCost;
    /** Hieu ung chinh tai moi level (index 0 = level 1). */
    public final float[] effects;
    /** Hieu ung phu tai moi level. Null neu khong co. */
    public final float[] effects2;

    // -------------------------------------------------------------------------

    /** Constructor co ca hai mang effects. */
    public Skill(String id, String name, String description, SkillType type,
                 int maxLevel, int[] spCost,
                 float[] effects, float[] effects2,
                 String[] prerequisites, boolean requiresBookToUnlock) {
        this.id            = id;
        this.name          = name;
        this.description   = description;
        this.type          = type;
        this.maxLevel      = maxLevel;
        this.spCost        = spCost;
        this.effects       = effects;
        this.effects2      = effects2;
        this.prerequisites = prerequisites;
        this.requiresBookToUnlock = requiresBookToUnlock;
    }

    /** Constructor co ca hai mang effects, requiresBookToUnlock mac dinh false. */
    public Skill(String id, String name, String description, SkillType type,
                 int maxLevel, int[] spCost,
                 float[] effects, float[] effects2,
                 String[] prerequisites) {
        this(id, name, description, type, maxLevel, spCost, effects, effects2, prerequisites, false);
    }

    /** Constructor chi co effects chinh. */
    public Skill(String id, String name, String description, SkillType type,
                 int maxLevel, int[] spCost, float[] effects,
                 String[] prerequisites) {
        this(id, name, description, type, maxLevel, spCost, effects, null, prerequisites);
    }

    // -------------------------------------------------------------------------

    public int     getLevel()     { return currentLevel; }
    public int     getCurrentLevel() { return currentLevel; }
    public void    setCurrentLevel(int l) { this.currentLevel = l; }
    public boolean isUnlocked()   { return currentLevel > 0; }
    public boolean isMaxed()      { return currentLevel >= maxLevel; }

    /** Hieu ung chinh tai level hien tai (0 neu chua unlock). */
    public float getEffect()  { return currentLevel > 0 ? effects[currentLevel - 1]  : 0f; }

    /** Hieu ung phu tai level hien tai (0 neu null hoac chua unlock). */
    public float getEffect2() {
        return (currentLevel > 0 && effects2 != null) ? effects2[currentLevel - 1] : 0f;
    }

    /** Chi phi SP de nang cap len cap tiep theo; -1 neu da max. */
    public int getUpgradeCost() {
        if (isMaxed()) return -1;
        return spCost[currentLevel]; // currentLevel == index cua level tiep theo
    }

    /** Tang level. Nguoi goi tu kiem tra dieu kien truoc. */
    public void upgrade() {
        if (!isMaxed()) currentLevel++;
    }

    @Override
    public String toString() {
        return name + " [Lv." + currentLevel + "/" + maxLevel + "]";
    }
    
    public String getNextLevelEffectString() {
        if (isMaxed()) return "";
        float e1 = effects[currentLevel];
        float e2 = effects2 != null ? effects2[currentLevel] : 0f;
        return formatEffectString(e1, e2);
    }
    
    public String getCurrentLevelEffectString() {
        if (!isUnlocked()) return "Chưa học";
        float e1 = effects[currentLevel - 1];
        float e2 = effects2 != null ? effects2[currentLevel - 1] : 0f;
        return formatEffectString(e1, e2);
    }
    
    private String formatEffectString(float e1, float e2) {
        switch (id) {
            case "keo_manh": return String.format("Gây %.0f sát thương / Tốn %.0f Thể lực", e1, e2);
            case "day_thep": return String.format("Tăng thêm %.0f sức chịu đựng dây", e1);
            case "the_luc_doi_dao": return String.format("Tăng thêm %.0f Thể lực tối đa", e1);
            case "co_bap_thep": return String.format("Giảm %.0f%% tốc độ tụt Thể lực", e1);
            case "bao_kich": return String.format("Tỷ lệ bạo kích x2 sát thương: %.0f%%", e1);
            case "pha_giap": return String.format("Gây %.0f sát thương / Suy yếu %.0f giây", e1, e2);
            case "giu_day": return String.format("Tự giảm %.0f Tension/giây khi chùng dây", e1);
            case "nhanh_tay": return String.format("Cửa sổ cắn câu kéo dài thêm %.1f giây", e1);
            case "hoi_phuc_nhanh": return String.format("Tăng %d Thể lực/giây khi nghỉ ngơi", (int)e1);
            case "phan_xa": return String.format("Giảm %.0f%% tụt Thể lực (Yêu cầu bấm trong %.1fs)", e2, e1);
            case "nhip_nhang": return String.format("Tỉ lệ vô hiệu hóa lực cá: %.0f%%", e1);
            case "luot_day": return String.format("Giảm %.0f%% lực kéo của cá trong %.0f giây", (1 - 1f/e1) * 100f, e2);
            case "chuyen_gia_moi": return String.format("Tỉ lệ không mất mồi: %.0f%%", e1);
            case "may_man_co_ban": return String.format("Tỉ lệ cá RARE trở lên: +%.0f%%", e1);
            case "nhat_mot": return String.format("Tỉ lệ rơi vật phẩm: %.0f%%", e1);
            case "ngu_dan_tinh_anh": return String.format("Tỉ lệ cá EPIC/BOSS: +%.0f%%", e1);
            case "tho_san_kho_bau": return String.format("Cơ hội rớt Kho Báu: +%.0f%%", e1);
            case "nem_xa": return "Đảm bảo đạt Perfect";
            case "duong_suc": return String.format("Thời gian vận sức: %.1fs", e1);
            case "giat_kep": return String.format("Hệ số sát thương: x%.1f", e1);
            case "mat_dai_bang": return String.format("Mở rộng vùng Perfect: x%.1f", e1);
            case "thuan_hoa": return String.format("Kéo dài thời gian nghỉ ngơi thêm %.1fs", e1);
            case "loi_nguyen_troi_buoc": return String.format("Đóng băng lực kéo cá %.1f giây", e1);
            case "trai_tim_dai_duong": return String.format("Tỷ lệ cứu đứt dây: %.0f%%", e1);
            case "ben_bi": return String.format("Tăng %.0f%% hồi Thể lực sau câu", e1);
            case "no_bung": return String.format("Tăng %.0f%% hồi Thể lực khi dùng vật phẩm", e1);
            default:
                StringBuilder sb = new StringBuilder("Thông số: ");
                sb.append(String.format("%.1f", e1));
                if (effects2 != null) sb.append(" / ").append(String.format("%.1f", e2));
                return sb.toString();
        }
    }
}
