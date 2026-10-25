package com.fishingrpg.game.systems;

import com.fishingrpg.game.entities.Quest;
import java.util.ArrayList;
import java.util.List;

public class QuestManager {
    public List<Quest> dailyQuests = new ArrayList<>();
    public List<Quest> weeklyQuests = new ArrayList<>();
    public List<Quest> achievements = new ArrayList<>();

    public int dailyPoints = 0;
    public boolean dailyChestClaimed = false;

    public int weeklyPoints = 0;
    public boolean weeklyChestClaimed = false;

    public long lastDailyResetTime = 0;
    public long lastWeeklyResetTime = 0;

    // Callbacks cho notification
    public interface QuestCallback {
        void onQuestCompleted(Quest quest);
    }
    public transient QuestCallback callback;

    public QuestManager() {
    }

    public void initIfEmpty() {
        if (dailyQuests.isEmpty()) resetDailyQuests();
        if (weeklyQuests.isEmpty()) resetWeeklyQuests();
        initAchievements();
    }

    public void checkResets() {
        long currentTime = TimeSyncService.getCurrentTimeMillis();
        java.util.Calendar now = java.util.Calendar.getInstance();
        now.setTimeInMillis(currentTime);
        
        java.util.Calendar lastDaily = java.util.Calendar.getInstance();
        lastDaily.setTimeInMillis(lastDailyResetTime == 0 ? currentTime : lastDailyResetTime);

        if (lastDailyResetTime == 0 || now.get(java.util.Calendar.YEAR) > lastDaily.get(java.util.Calendar.YEAR) || 
            (now.get(java.util.Calendar.YEAR) == lastDaily.get(java.util.Calendar.YEAR) && now.get(java.util.Calendar.DAY_OF_YEAR) > lastDaily.get(java.util.Calendar.DAY_OF_YEAR))) {
            resetDailyQuests();
            lastDailyResetTime = currentTime;
        }

        java.util.Calendar lastWeekly = java.util.Calendar.getInstance();
        lastWeekly.setTimeInMillis(lastWeeklyResetTime == 0 ? currentTime : lastWeeklyResetTime);
        
        if (lastWeeklyResetTime == 0 || now.get(java.util.Calendar.YEAR) > lastWeekly.get(java.util.Calendar.YEAR) ||
            (now.get(java.util.Calendar.YEAR) == lastWeekly.get(java.util.Calendar.YEAR) && now.get(java.util.Calendar.WEEK_OF_YEAR) > lastWeekly.get(java.util.Calendar.WEEK_OF_YEAR))) {
            resetWeeklyQuests();
            lastWeeklyResetTime = currentTime;
        }
        syncProgressWithPlayer();
    }

    private transient com.fishingrpg.game.entities.Player player;

    public void setPlayer(com.fishingrpg.game.entities.Player player) {
        this.player = player;
        syncProgressWithPlayer();
    }

    public static class QuestTemplate {
        public String id;
        public String title;
        public String description;
        public int target;
        public int rewardPoints;
        public int rewardDiamonds;

        public QuestTemplate(String id, String title, String description, int target, int rewardPoints, int rewardDiamonds) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.target = target;
            this.rewardPoints = rewardPoints;
            this.rewardDiamonds = rewardDiamonds;
        }

        public Quest createQuest() {
            return new Quest(id, Quest.Type.DAILY, title, description, target, rewardPoints, rewardDiamonds);
        }
    }

    private static final List<QuestTemplate> DAILY_POOL = new ArrayList<>();
    static {
        DAILY_POOL.add(new QuestTemplate("d_catch", "Ngư Phủ Cần Mẫn", "Câu thành công 5 con cá bất kỳ", 5, 25, 0));
        DAILY_POOL.add(new QuestTemplate("d_perfect", "Canh Góc Chuẩn Xác", "Đạt PERFECT khi quăng cần 3 lần", 3, 25, 0));
        DAILY_POOL.add(new QuestTemplate("d_rare", "Săn Cá Quý Hiếm", "Câu được 1 con cá bậc Rare trở lên", 1, 25, 0));
        DAILY_POOL.add(new QuestTemplate("d_boss", "Khiêu Chiến Thủy Quái", "Đánh bại 1 Thủy Quái Boss thế giới", 1, 25, 0));
        DAILY_POOL.add(new QuestTemplate("d_skill", "Vận Dụng Tuyệt Kỹ", "Sử dụng kỹ năng chiến đấu 4 lần", 4, 25, 0));
        DAILY_POOL.add(new QuestTemplate("d_item", "Dưỡng Sức Tiếp Năng", "Sử dụng 2 vật phẩm tiêu hao hỗ trợ", 2, 25, 0));
        DAILY_POOL.add(new QuestTemplate("d_trash", "Làm Sạch Vùng Nước", "Trục vớt được 1 rác thải hoặc rương báu", 1, 25, 0));
        DAILY_POOL.add(new QuestTemplate("d_heavy", "Chiến Lợi Nặng Ký", "Câu được 1 con cá nặng từ 10kg trở lên", 1, 25, 0));
        DAILY_POOL.add(new QuestTemplate("d_clean", "Kỹ Thuật Điêu Luyện", "Bắt 1 cá mà không để Tension vượt 85%", 1, 25, 0));
        DAILY_POOL.add(new QuestTemplate("d_spend", "Giao Thương Hội Quán", "Tiêu 150 Vàng tại Tiệm Tạp Hóa", 150, 25, 0));
        DAILY_POOL.add(new QuestTemplate("d_gacha", "Vận Mệnh Biển Cả", "Thử vận may 1 lần tại Vòng Quay Gacha", 1, 25, 0));
        DAILY_POOL.add(new QuestTemplate("d_travel", "Hải Trình Viễn Du", "Đổi vùng nước câu sang bản đồ khác", 1, 25, 0));
    }

    private void resetDailyQuests() {
        dailyQuests.clear();
        dailyPoints = 0;
        dailyChestClaimed = false;
        
        List<QuestTemplate> pool = new ArrayList<>(DAILY_POOL);
        java.util.Collections.shuffle(pool);
        int pickCount = Math.min(6, pool.size());
        for (int i = 0; i < pickCount; i++) {
            dailyQuests.add(pool.get(i).createQuest());
        }
    }

    private void resetWeeklyQuests() {
        weeklyQuests.clear();
        weeklyPoints = 0;
        weeklyChestClaimed = false;

        weeklyQuests.add(new Quest("w_catch", Quest.Type.WEEKLY, "Vua Biển Cả", "Câu thành công 40 con cá", 40, 25, 0));
        weeklyQuests.add(new Quest("w_perfect", Quest.Type.WEEKLY, "Xạ Thủ Cần Câu", "Đạt PERFECT khi quăng cần 15 lần", 15, 25, 0));
        weeklyQuests.add(new Quest("w_boss", Quest.Type.WEEKLY, "Kẻ Diệt Thủy Quái", "Đánh bại 3 Thủy Quái Boss", 3, 25, 0));
        weeklyQuests.add(new Quest("w_rare", Quest.Type.WEEKLY, "Săn Tìm Huyền Thoại", "Câu được 4 cá bậc Epic trở lên", 4, 25, 0));
        weeklyQuests.add(new Quest("w_heavy", Quest.Type.WEEKLY, "Kình Ngư Khổng Lồ", "Câu 2 con cá nặng từ 20kg trở lên", 2, 25, 0));
        weeklyQuests.add(new Quest("w_skill", Quest.Type.WEEKLY, "Bậc Thầy Bí Kỹ", "Sử dụng kỹ năng 20 lần trong chiến đấu", 20, 25, 0));
        weeklyQuests.add(new Quest("w_spend", Quest.Type.WEEKLY, "Đại Gia Vùng Vịnh", "Tiêu 1.500 Vàng tại Tiệm Tạp Hóa", 1500, 25, 0));
    }

    private void initAchievements() {
        List<Quest> allAch = new ArrayList<>();
        // 1. Catch Fish
        allAch.add(new Quest("a_fish_1", Quest.Type.ACHIEVEMENT, "Bước Đầu", "Câu thành công 1 con cá", 1, 0, 2));
        allAch.add(new Quest("a_fish_10", Quest.Type.ACHIEVEMENT, "Làm Quen", "Câu thành công 10 con cá", 10, 0, 3));
        allAch.add(new Quest("a_fish_50", Quest.Type.ACHIEVEMENT, "Thợ Câu Mới Nổi", "Câu thành công 50 con cá", 50, 0, 5));
        allAch.add(new Quest("a_fish_100", Quest.Type.ACHIEVEMENT, "Khởi Đầu Gian Nan", "Câu thành công 100 con cá", 100, 0, 8));
        allAch.add(new Quest("a_fish_500", Quest.Type.ACHIEVEMENT, "Chuyên Gia Đánh Bắt", "Câu thành công 500 con cá", 500, 0, 12));
        allAch.add(new Quest("a_fish_1000", Quest.Type.ACHIEVEMENT, "Lão Ngư", "Câu thành công 1.000 con cá", 1000, 0, 18));
        allAch.add(new Quest("a_fish_5000", Quest.Type.ACHIEVEMENT, "Sát Thủ Biển Sâu", "Câu thành công 5.000 con cá", 5000, 0, 25));

        // 2. Perfect Cast
        allAch.add(new Quest("a_perfect_1", Quest.Type.ACHIEVEMENT, "Canh Góc Tốt", "Đạt PERFECT 1 lần", 1, 0, 2));
        allAch.add(new Quest("a_perfect_10", Quest.Type.ACHIEVEMENT, "Quen Tay", "Đạt PERFECT 10 lần", 10, 0, 3));
        allAch.add(new Quest("a_perfect_50", Quest.Type.ACHIEVEMENT, "Bách Phát Bách Trúng", "Đạt PERFECT 50 lần", 50, 0, 6));
        allAch.add(new Quest("a_perfect_100", Quest.Type.ACHIEVEMENT, "Hoàn Mỹ", "Đạt PERFECT 100 lần", 100, 0, 10));
        allAch.add(new Quest("a_perfect_200", Quest.Type.ACHIEVEMENT, "Thần Đồng Cần Thủ", "Đạt PERFECT 200 lần", 200, 0, 15));
        allAch.add(new Quest("a_perfect_500", Quest.Type.ACHIEVEMENT, "Đỉnh Cao Kỹ Năng", "Đạt PERFECT 500 lần", 500, 0, 25));

        // 3. Catch Rare Fish
        allAch.add(new Quest("a_rare_1", Quest.Type.ACHIEVEMENT, "May Mắn Đầu Tiên", "Câu 1 cá Hiếm (Rare) trở lên", 1, 0, 3));
        allAch.add(new Quest("a_rare_10", Quest.Type.ACHIEVEMENT, "Săn Mồi Khủng", "Câu 10 cá Hiếm (Rare) trở lên", 10, 0, 5));
        allAch.add(new Quest("a_rare_50", Quest.Type.ACHIEVEMENT, "Sát Thủ Cá Hiếm", "Câu 50 cá Hiếm (Rare) trở lên", 50, 0, 10));
        allAch.add(new Quest("a_rare_200", Quest.Type.ACHIEVEMENT, "Kẻ Được Chọn", "Câu 200 cá Hiếm (Rare) trở lên", 200, 0, 18));
        allAch.add(new Quest("a_rare_500", Quest.Type.ACHIEVEMENT, "Vua Biển Cả", "Câu 500 cá Hiếm (Rare) trở lên", 500, 0, 30));

        // 4. Earn Gold
        allAch.add(new Quest("a_gold_1000", Quest.Type.ACHIEVEMENT, "Những Đồng Tiền Đầu Tiên", "Kiếm được 1.000 Vàng", 1000, 0, 2));
        allAch.add(new Quest("a_gold_10k", Quest.Type.ACHIEVEMENT, "Làm Giàu Không Khó", "Kiếm được 10.000 Vàng", 10000, 0, 4));
        allAch.add(new Quest("a_gold_50k", Quest.Type.ACHIEVEMENT, "Túi Tiền Rủng Rỉnh", "Kiếm được 50.000 Vàng", 50000, 0, 8));
        allAch.add(new Quest("a_gold_200k", Quest.Type.ACHIEVEMENT, "Đại Gia", "Kiếm được 200.000 Vàng", 200000, 0, 14));
        allAch.add(new Quest("a_gold_500k", Quest.Type.ACHIEVEMENT, "Chúa Tể Tiền Bạc", "Kiếm được 500.000 Vàng", 500000, 0, 20));
        allAch.add(new Quest("a_gold_1m", Quest.Type.ACHIEVEMENT, "Tỷ Phú Biển Khơi", "Kiếm được 1.000.000 Vàng", 1000000, 0, 30));

        // 5. Spend Gold
        allAch.add(new Quest("a_spend_1000", Quest.Type.ACHIEVEMENT, "Chi Tiêu Hợp Lý", "Tiêu 1.000 Vàng", 1000, 0, 2));
        allAch.add(new Quest("a_spend_10k", Quest.Type.ACHIEVEMENT, "Đầu Tư Nâng Cấp", "Tiêu 10.000 Vàng", 10000, 0, 4));
        allAch.add(new Quest("a_spend_50k", Quest.Type.ACHIEVEMENT, "Khách VIP", "Tiêu 50.000 Vàng", 50000, 0, 8));
        allAch.add(new Quest("a_spend_100k", Quest.Type.ACHIEVEMENT, "Thương Gia", "Tiêu 100.000 Vàng", 100000, 0, 14));
        allAch.add(new Quest("a_spend_500k", Quest.Type.ACHIEVEMENT, "Đốt Tiền Không Phanh", "Tiêu 500.000 Vàng", 500000, 0, 25));

        // 6. Quay Gacha (gacha)
        allAch.add(new Quest("a_gacha_1", Quest.Type.ACHIEVEMENT, "Vận May Khởi Đầu", "Quay Gacha 1 lần", 1, 0, 2));
        allAch.add(new Quest("a_gacha_10", Quest.Type.ACHIEVEMENT, "Thử Thách Vận May", "Quay Gacha 10 lần", 10, 0, 4));
        allAch.add(new Quest("a_gacha_50", Quest.Type.ACHIEVEMENT, "Lún Sâu", "Quay Gacha 50 lần", 50, 0, 8));
        allAch.add(new Quest("a_gacha_100", Quest.Type.ACHIEVEMENT, "Tín Đồ Gacha", "Quay Gacha 100 lần", 100, 0, 12));
        allAch.add(new Quest("a_gacha_500", Quest.Type.ACHIEVEMENT, "Con Nghiện Gacha", "Quay Gacha 500 lần", 500, 0, 20));
        allAch.add(new Quest("a_gacha_1000", Quest.Type.ACHIEVEMENT, "Hủy Diệt Nhân Phẩm", "Quay Gacha 1.000 lần", 1000, 0, 30));

        // 7. Use Skill
        allAch.add(new Quest("a_skill_1", Quest.Type.ACHIEVEMENT, "Sử Dụng Kỹ Năng", "Sử dụng Kỹ năng 1 lần", 1, 0, 2));
        allAch.add(new Quest("a_skill_10", Quest.Type.ACHIEVEMENT, "Dần Quen Tay", "Sử dụng Kỹ năng 10 lần", 10, 0, 3));
        allAch.add(new Quest("a_skill_50", Quest.Type.ACHIEVEMENT, "Thành Thạo", "Sử dụng Kỹ năng 50 lần", 50, 0, 6));
        allAch.add(new Quest("a_skill_100", Quest.Type.ACHIEVEMENT, "Bậc Thầy Kỹ Năng", "Sử dụng Kỹ năng 100 lần", 100, 0, 10));
        allAch.add(new Quest("a_skill_500", Quest.Type.ACHIEVEMENT, "Phù Thủy Đại Dương", "Sử dụng Kỹ năng 500 lần", 500, 0, 20));

        // 8. Level Up
        allAch.add(new Quest("a_level_2", Quest.Type.ACHIEVEMENT, "Mới Vào Nghề", "Đạt Cấp 2", 2, 0, 2));
        allAch.add(new Quest("a_level_5", Quest.Type.ACHIEVEMENT, "Người Mới", "Đạt Cấp 5", 5, 0, 4));
        allAch.add(new Quest("a_level_10", Quest.Type.ACHIEVEMENT, "Cao Thủ", "Đạt Cấp 10", 10, 0, 8));
        allAch.add(new Quest("a_level_20", Quest.Type.ACHIEVEMENT, "Chuyên Gia", "Đạt Cấp 20", 20, 0, 12));
        allAch.add(new Quest("a_level_50", Quest.Type.ACHIEVEMENT, "Huyền Thoại Biển Khơi", "Đạt Cấp 50", 50, 0, 20));
        allAch.add(new Quest("a_level_100", Quest.Type.ACHIEVEMENT, "Phá Đảo", "Đạt Cấp 100", 100, 0, 30));
        
        for (Quest q : allAch) {
            boolean found = false;
            for (Quest existing : achievements) {
                if (existing.id.equals(q.id)) {
                    found = true;
                    existing.rewardDiamonds = q.rewardDiamonds;
                    existing.target = q.target;
                    existing.description = q.description;
                    existing.title = q.title;
                    break;
                }
            }
            if (!found) achievements.add(q);
        }
    }

    public void addProgress(String prefix, int amount) {
        checkResets();
        updateList(dailyQuests, "d_" + prefix, amount);
        updateList(weeklyQuests, "w_" + prefix, amount);
        updateList(achievements, "a_" + prefix, amount);
        if ("catch".equals(prefix)) {
            updateList(achievements, "a_fish", amount);
        }
        if (player != null && player.getEventManager() != null) {
            player.getEventManager().addProgress(prefix, amount);
        }
    }

    public void syncProgressWithPlayer() {
        if (player == null) return;
        int lvl = player.getLevel();
        for (Quest q : achievements) {
            if (q.id.startsWith("a_level_") && !q.isCompleted()) {
                if (lvl >= q.target) {
                    q.progress = q.target;
                    if (callback != null) callback.onQuestCompleted(q);
                }
            }
        }
    }

    private void updateList(List<Quest> list, String matchId, int amount) {
        for (Quest q : list) {
            if (q.id.startsWith(matchId) && !q.isCompleted()) {
                q.addProgress(amount);
                if (q.isCompleted() && callback != null) {
                    callback.onQuestCompleted(q);
                }
            }
        }
    }

    public boolean hasAnyClaimable() {
        for (Quest q : dailyQuests) {
            if (q.isCompleted() && !q.claimed) return true;
        }
        for (Quest q : weeklyQuests) {
            if (q.isCompleted() && !q.claimed) return true;
        }
        for (Quest q : achievements) {
            if (q.isCompleted() && !q.claimed) return true;
        }
        if (dailyPoints >= 100 && !dailyChestClaimed) return true;
        if (weeklyPoints >= 100 && !weeklyChestClaimed) return true;
        return false;
    }
}
