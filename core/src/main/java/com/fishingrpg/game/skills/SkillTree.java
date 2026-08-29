package com.fishingrpg.game.skills;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Cay ky nang cua nguoi choi.
 * Giai doan 1 -- Nhanh Suc Manh (5 ky nang).
 * Giai doan 2 se mo rong them nhanh Kinh Nghiem va Trang Bi.
 */
public class SkillTree {

    private final Map<String, Skill> skills = new LinkedHashMap<>();

    public SkillTree() {
        initSkills();
    }

    private void initSkills() {
        // --- NHÁNH SỨC MẠNH ---
        // 1. Keo Manh (3 cap) -- Active
        add(new Skill("keo_manh", "Kéo Mạnh", "Giật cần gây sát thương cho cá.", 
            Skill.SkillType.ACTIVE, 3, new int[]{1, 1, 2}, 
            new float[]{ 20f, 35f, 50f }, new float[]{ 20f, 18f, 15f }, new String[]{}));
            
        // 2. Day Thep (3 cap) -- Passive (Can Keo Manh >= 1)
        add(new Skill("day_thep", "Dây Thép", "Tăng sức chịu đựng tối đa của dây câu.", 
            Skill.SkillType.PASSIVE, 3, new int[]{1, 2, 2}, 
            new float[]{ 10f, 20f, 30f }, new String[]{"keo_manh"}));
            
        // 3. The luc doi dao (3 cap) -- Passive (Can Keo Manh >= 1)
        add(new Skill("the_luc_doi_dao", "Thể Lực Dồi Dào", "Tăng lượng Thể lực (Stamina) tối đa.", 
            Skill.SkillType.PASSIVE, 3, new int[]{1, 2, 2}, 
            new float[]{ 20f, 40f, 60f }, new String[]{"keo_manh"}));
            
        // 4. Co bap thep (2 cap) -- Passive (Can Day Thep >= 1)
        add(new Skill("co_bap_thep", "Cơ Bắp Thép", "Giảm tốc độ tụt Thể lực tự nhiên khi chiến đấu với cá.", 
            Skill.SkillType.PASSIVE, 2, new int[]{2, 3}, 
            new float[]{ 30f, 50f }, new String[]{"day_thep"}));
            
        // 5. Bao kich (2 cap) -- Passive (Can Keo Manh >= 1)
        add(new Skill("bao_kich", "Bạo Kích", "Khi dùng Kéo Mạnh, có tỷ lệ x2 sát thương.", 
            Skill.SkillType.PASSIVE, 2, new int[]{2, 3}, 
            new float[]{ 15f, 30f }, new String[]{"keo_manh"}));
            
        // 6. Pha giap (2 cap) -- Active (Can Bao Kich)
        add(new Skill("pha_giap", "Phá Giáp", "Gây sát thương nhỏ, khiến cá nhận thêm sát thương mọi nguồn trong 10s.", 
            Skill.SkillType.ACTIVE, 2, new int[]{3, 4}, 
            new float[]{ 10f, 20f }, new float[]{ 20f, 40f }, new String[]{"bao_kich"}));

        // 7. Bền Bỉ (3 cap) -- Passive (Can The luc doi dao)
        add(new Skill("ben_bi", "Bền Bỉ", "Tăng % Thể lực hồi lại sau mỗi lượt câu.", 
            Skill.SkillType.PASSIVE, 3, new int[]{1, 2, 2}, 
            new float[]{ 5f, 10f, 15f }, new String[]{"the_luc_doi_dao"}));

        // 8. No Bụng (3 cap) -- Passive (Can Ben Bi)
        add(new Skill("no_bung", "No Bụng", "Tăng thêm lượng Thể lực hồi được khi tiêu thụ vật phẩm.", 
            Skill.SkillType.PASSIVE, 3, new int[]{2, 2, 3}, 
            new float[]{ 20f, 35f, 50f }, new String[]{"ben_bi"}));

        // --- NHÁNH NHANH NHẸN ---
        // 1. Giu Day (3 cap) -- Passive
        add(new Skill("giu_day", "Giữ Dây", "Giảm tốc độ tăng Tension tự nhiên khi cá chạy.", 
            Skill.SkillType.PASSIVE, 3, new int[]{0, 1, 2}, 
            new float[]{ 3f, 6f, 9f }, new String[]{}));
            
        // 2. Nhanh Tay (2 cap) -- Passive (Can Giu Day >= 1)
        add(new Skill("nhanh_tay", "Nhanh Tay", "Kéo dài thời gian cửa sổ cắn câu.", 
            Skill.SkillType.PASSIVE, 2, new int[]{1, 2}, 
            new float[]{ 0.5f, 1.0f }, new String[]{"giu_day"}));
            
        // 3. Hoi phuc nhanh (3 cap) -- Passive
        add(new Skill("hoi_phuc_nhanh", "Hồi Phục Nhanh", "Tăng tốc độ hồi phục Thể lực khi nghỉ ngơi.", 
            Skill.SkillType.PASSIVE, 3, new int[]{2, 2, 3}, 
            new float[]{ 1f, 2f, 3f }, new String[]{"giu_day"}));
            
        // 4. Phan Xa (2 cap) -- Passive (Can Nhanh Tay >= 1)
        add(new Skill("phan_xa", "Phản Xạ", "Nếu bấm dính cá ngay, giảm tốc độ tụt Thể lực trong toàn phiên.", 
            Skill.SkillType.PASSIVE, 2, new int[]{2, 3}, 
            new float[]{ 0.3f, 0.5f }, new float[]{ 30f, 30f }, new String[]{"nhanh_tay"}));
            
        // 5. Nhip nhang (2 cap) -- Passive (Can Hoi phuc nhanh >= 1)
        add(new Skill("nhip_nhang", "Nhịp Nhàng", "Khi giữ Space, có tỷ lệ bỏ qua hoàn toàn lực kéo của cá (cá kéo không lên Tension).", 
            Skill.SkillType.PASSIVE, 2, new int[]{2, 3}, 
            new float[]{ 15f, 30f }, new String[]{"hoi_phuc_nhanh"}));
            
        // 6. Luot day (2 cap) -- Active (Can Phan Xa)
        add(new Skill("luot_day", "Lướt Dây", "Tạm thời làm giảm mạnh lượng Tension bị tăng thêm do cá vùng vẫy.", 
            Skill.SkillType.ACTIVE, 2, new int[]{3, 4}, 
            new float[]{ 2f, 3f }, new float[]{ 5f, 7f }, new String[]{"phan_xa"}));

        // --- NHÁNH MAY MẮN ---
        // 1. Chuyen Gia Moi (3 cap) -- Passive
        add(new Skill("chuyen_gia_moi", "Chuyên Gia Mồi", "Tỷ lệ % không tiêu hao Mồi Thơm khi sử dụng.", 
            Skill.SkillType.PASSIVE, 3, new int[]{1, 1, 2}, 
            new float[]{ 20f, 40f, 60f }, new String[]{}));
            
        // 2. May Man Co Ban (3 cap) -- Passive
        add(new Skill("may_man_co_ban", "May Mắn Cơ Bản", "Tăng tỷ lệ ra cá độ hiếm RARE trở lên.", 
            Skill.SkillType.PASSIVE, 3, new int[]{1, 2, 2}, 
            new float[]{ 5f, 10f, 15f }, new String[]{"chuyen_gia_moi"}));
            
        // 3. Nhat Mot (2 cap) -- Passive
        add(new Skill("nhat_mot", "Nhặt Mót", "Tỷ lệ câu thêm 1 Rác/Consumable kèm theo.", 
            Skill.SkillType.PASSIVE, 2, new int[]{2, 3}, 
            new float[]{ 15f, 30f }, new String[]{"chuyen_gia_moi"}));
            
        // 4. Ngu dan tinh anh (2 cap) -- Passive
        add(new Skill("ngu_dan_tinh_anh", "Ngư Dân Tinh Anh", "Tăng mạnh tỷ lệ ra cá EPIC & LEGENDARY.", 
            Skill.SkillType.PASSIVE, 2, new int[]{2, 3}, 
            new float[]{ 3f, 8f }, new String[]{"may_man_co_ban"}));
            
        // 5. Tho san kho bau (2 cap) -- Passive
        add(new Skill("tho_san_kho_bau", "Thợ Săn Kho Báu", "Tăng cơ hội câu được Kho Báu (Treasure).", 
            Skill.SkillType.PASSIVE, 2, new int[]{2, 3}, 
            new float[]{ 5f, 10f }, new String[]{"nhat_mot"}));
            
        // 6. Nem Xa (1 cap) -- Active
        add(new Skill("nem_xa", "Ném Xa", "Kích hoạt: Cú quăng cần tiếp theo chắc chắn đạt Perfect.", 
            Skill.SkillType.ACTIVE, 1, new int[]{3}, 
            new float[]{ 1f }, new String[]{"ngu_dan_tinh_anh"}));

        // --- NHÓM KỸ NĂNG BÍ MẬT --- (Requires Book)
        // 1. Duong Suc (3 cap) -- Active
        add(new Skill("duong_suc", "Dưỡng Sức", "Vận sức 2s hồi 50 Thể lực (10/s). Cấp cao giảm t/g vận. Yêu cầu Sách Kỹ Năng.", 
            Skill.SkillType.ACTIVE, 3, new int[]{0, 2, 3}, 
            new float[]{ 2f, 1.5f, 1f }, new float[]{ 50f, 50f, 50f }, new String[]{}, true));
            
        // 2. Giat Kep (3 cap) -- Active
        add(new Skill("giat_kep", "Giật Kép", "Gây sát thương x2/2.5/3 nhưng tốn thêm Thể lực. Cần Sách mở.", 
            Skill.SkillType.ACTIVE, 3, new int[]{0, 2, 3}, 
            new float[]{ 2f, 2.5f, 3f }, null, new String[]{}, true));
            
        // 3. Mat Dai Bang (2 cap) -- Passive
        add(new Skill("mat_dai_bang", "Mắt Đại Bàng", "Mở rộng vùng Perfect khi quăng cần. Cần Sách mở.", 
            Skill.SkillType.PASSIVE, 2, new int[]{0, 3}, 
            new float[]{ 1.5f, 2.0f }, null, new String[]{}, true));
            
        // 4. Thuan Hoa (3 cap) -- Passive
        add(new Skill("thuan_hoa", "Thuần Hóa", "Khi cá chuyển trạng thái IDLE, kéo dài thời gian nghỉ ngơi thêm 1s/1.5s/2s. Cần Sách mở.", 
            Skill.SkillType.PASSIVE, 3, new int[]{0, 3, 4}, 
            new float[]{ 1f, 1.5f, 2.0f }, null, new String[]{}, true));
            
        // 5. Loi nguyen troi buoc (3 cap) -- Active
        add(new Skill("loi_nguyen_troi_buoc", "Trói Buộc", "Đóng băng cá (không tăng Tension) trong 0.5s/cấp. Cần Sách mở.", 
            Skill.SkillType.ACTIVE, 3, new int[]{0, 3, 4}, 
            new float[]{ 0.5f, 1f, 1.5f }, null, new String[]{}, true));
            
        // 6. Trai tim dai duong (2 cap) -- Passive
        add(new Skill("trai_tim_dai_duong", "Trái Tim Đại Dương", "Khi chuẩn bị đứt dây, có tỷ lệ cứu. Cần Sách mở.", 
            Skill.SkillType.PASSIVE, 2, new int[]{0, 4}, 
            new float[]{ 5f, 12f }, null, new String[]{}, true));
    }

    // =========================================================================
    // API public
    // =========================================================================

    private void add(Skill s) { skills.put(s.id, s); }

    public Skill getSkill(String id) { return skills.get(id); }

    public Collection<Skill> getAllSkills() { return skills.values(); }

    /** Kiem tra tat ca prerequisites da unlock (bat ky level nao). */
    public boolean prerequisitesMet(String skillId) {
        Skill skill = skills.get(skillId);
        if (skill == null) return false;
        for (String prereq : skill.prerequisites) {
            Skill p = skills.get(prereq);
            if (p == null || !p.isUnlocked()) return false;
        }
        return true;
    }

    public boolean canUpgrade(String skillId, int playerSp) {
        Skill skill = skills.get(skillId);
        if (skill == null || skill.isMaxed()) return false;
        
        // Neu yeu cau sach de mo khoa thi khong the dung SP nang cap tu cap 0
        if (skill.requiresBookToUnlock && skill.getLevel() == 0) return false;
        
        if (!prerequisitesMet(skillId)) return false;
        return playerSp >= skill.getUpgradeCost();
    }

    public int upgrade(String skillId, int playerSp) {
        if (!canUpgrade(skillId, playerSp)) return -1;
        Skill skill = skills.get(skillId);
        int cost = skill.getUpgradeCost();
        skill.upgrade();
        return cost;
    }

    // =========================================================================
    // API Lấy chỉ số hiệu quả của 24 Kỹ năng
    // =========================================================================

    // --- SỨC MẠNH ---
    public float getKeoManhDamage() { Skill s = skills.get("keo_manh"); return (s != null && s.isUnlocked()) ? s.getEffect() : 20f; }
    public float getKeoManhStaminaCost() { Skill s = skills.get("keo_manh"); return (s != null && s.isUnlocked()) ? s.getEffect2() : 20f; }
    public float getDayThepBonus() { Skill s = skills.get("day_thep"); return (s != null) ? s.getEffect() : 0f; }
    public float getTheLucDoiDaoBonus() { Skill s = skills.get("the_luc_doi_dao"); return (s != null) ? s.getEffect() : 0f; }
    public float getCoBapThepBonus() { Skill s = skills.get("co_bap_thep"); return (s != null) ? s.getEffect() : 0f; }
    public float getBaoKichChance() { Skill s = skills.get("bao_kich"); return (s != null) ? s.getEffect() : 0f; }
    public boolean hasPhaGiap() { Skill s = skills.get("pha_giap"); return s != null && s.isUnlocked(); }
    public float getPhaGiapDamage() { Skill s = skills.get("pha_giap"); return (s != null) ? s.getEffect() : 0f; }
    public float getPhaGiapDebuff() { Skill s = skills.get("pha_giap"); return (s != null) ? s.getEffect2() : 0f; }
    public float getBenBiBonus() { Skill s = skills.get("ben_bi"); return (s != null) ? s.getEffect() : 0f; }
    public float getNoBungBonus() { Skill s = skills.get("no_bung"); return (s != null) ? s.getEffect() : 0f; }

    // --- NHANH NHẸN ---
    public float getGiuDayBonus() { Skill s = skills.get("giu_day"); return (s != null) ? s.getEffect() : 0f; }
    public float getNhanhTayBonusTime() { Skill s = skills.get("nhanh_tay"); return (s != null) ? s.getEffect() : 0f; }
    public float getHoiPhucNhanhBonus() { Skill s = skills.get("hoi_phuc_nhanh"); return (s != null) ? s.getEffect() : 0f; }
    public boolean hasPhanXa() { Skill s = skills.get("phan_xa"); return s != null && s.isUnlocked(); }
    public float getPhanXaTime() { Skill s = skills.get("phan_xa"); return (s != null) ? s.getEffect() : 0f; }
    public float getPhanXaBonus() { Skill s = skills.get("phan_xa"); return (s != null) ? s.getEffect2() : 0f; }
    public float getNhipNhangChance() { Skill s = skills.get("nhip_nhang"); return (s != null) ? s.getEffect() : 0f; }
    public boolean hasLuotDay() { Skill s = skills.get("luot_day"); return s != null && s.isUnlocked(); }
    public float getLuotDayMultiplier() { Skill s = skills.get("luot_day"); return (s != null) ? s.getEffect() : 1f; }
    public float getLuotDayDuration() { Skill s = skills.get("luot_day"); return (s != null) ? s.getEffect2() : 0f; }

    // --- MAY MẮN ---
    public float getChuyenGiaMoiChance() { Skill s = skills.get("chuyen_gia_moi"); return (s != null) ? s.getEffect() : 0f; }
    public float getMayManCoBanBonus() { Skill s = skills.get("may_man_co_ban"); return (s != null) ? s.getEffect() : 0f; }
    public float getNhatMotChance() { Skill s = skills.get("nhat_mot"); return (s != null) ? s.getEffect() : 0f; }
    public float getNguDanTinhAnhBonus() { Skill s = skills.get("ngu_dan_tinh_anh"); return (s != null) ? s.getEffect() : 0f; }
    public float getThoSanKhoBauBonus() { Skill s = skills.get("tho_san_kho_bau"); return (s != null) ? s.getEffect() : 0f; }
    public boolean hasNemXa() { Skill s = skills.get("nem_xa"); return s != null && s.isUnlocked(); }

    // --- BÍ MẬT ---
    public float getDuongSucChannelTime() { Skill s = skills.get("duong_suc"); return (s != null) ? s.getEffect() : 2f; }
    public float getDuongSucHeal() { Skill s = skills.get("duong_suc"); return (s != null) ? s.getEffect2() : 100f; }
    public float getGiatKepDamageMult() { Skill s = skills.get("giat_kep"); return (s != null && s.isUnlocked()) ? s.getEffect() : 1f; }
    public float getMatDaiBangBonus() { Skill s = skills.get("mat_dai_bang"); return (s != null && s.isUnlocked()) ? s.getEffect() : 1f; }
    public float getThuanHoaBonus() { Skill s = skills.get("thuan_hoa"); return (s != null) ? s.getEffect() : 0f; }
    public float getLoiNguyenTroiBuocDuration() { Skill s = skills.get("loi_nguyen_troi_buoc"); return (s != null) ? s.getEffect() : 0f; }
    public float getTraiTimDaiDuongChance() { Skill s = skills.get("trai_tim_dai_duong"); return (s != null) ? s.getEffect() : 0f; }
}
