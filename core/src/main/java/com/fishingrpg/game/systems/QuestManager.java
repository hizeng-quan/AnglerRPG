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
        long currentTime = System.currentTimeMillis();
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
    }

    private void resetDailyQuests() {
        dailyQuests.clear();
        dailyPoints = 0;
        dailyChestClaimed = false;
        
        dailyQuests.add(new Quest("d_catch", Quest.Type.DAILY, "Thợ Câu Chăm Chỉ", "Câu thành công 5 con cá", 5, 25, 0));
        dailyQuests.add(new Quest("d_perfect", Quest.Type.DAILY, "Tay Câu Cừ Khôi", "Đạt PERFECT khi quăng cần 3 lần", 3, 25, 0));
        dailyQuests.add(new Quest("d_gacha", Quest.Type.DAILY, "Thử Vận May", "Quay Gacha 1 lần", 1, 25, 0));
        dailyQuests.add(new Quest("d_spend", Quest.Type.DAILY, "Mua Sắm", "Tiêu 100 Vàng", 100, 25, 0));
        dailyQuests.add(new Quest("d_rare", Quest.Type.DAILY, "Thợ Săn Đồ Hiếm", "Câu 1 con cá Rare trở lên", 1, 25, 0));
        dailyQuests.add(new Quest("d_skill", Quest.Type.DAILY, "Nâng Cao Tay Nghề", "Sử dụng Kỹ năng 5 lần", 5, 25, 0));
    }

    private void resetWeeklyQuests() {
        weeklyQuests.clear();
        weeklyPoints = 0;
        weeklyChestClaimed = false;

        weeklyQuests.add(new Quest("w_catch", Quest.Type.WEEKLY, "Vua Biển Cả", "Câu thành công 50 con cá", 50, 25, 0));
        weeklyQuests.add(new Quest("w_perfect", Quest.Type.WEEKLY, "Chuyên Gia", "Đạt PERFECT khi quăng cần 20 lần", 20, 25, 0));
        weeklyQuests.add(new Quest("w_gacha", Quest.Type.WEEKLY, "Người Chơi Hệ Gacha", "Quay Gacha 10 lần", 10, 25, 0));
        weeklyQuests.add(new Quest("w_spend", Quest.Type.WEEKLY, "Đại Gia Tiêu Tiền", "Tiêu 2000 Vàng", 2000, 25, 0));
        weeklyQuests.add(new Quest("w_rare", Quest.Type.WEEKLY, "Huyền Thoại", "Câu 1 con cá Legendary", 1, 25, 0));
        weeklyQuests.add(new Quest("w_level", Quest.Type.WEEKLY, "Thăng Tiến", "Lên 1 Level", 1, 25, 0));
    }

    private void initAchievements() {
        List<Quest> allAch = new ArrayList<>();
        // 1. Catch Fish
        allAch.add(new Quest("a_fish_1", Quest.Type.ACHIEVEMENT, "Bước Đầu", "Câu thành công 1 con cá", 1, 0, 10));
        allAch.add(new Quest("a_fish_10", Quest.Type.ACHIEVEMENT, "Làm Quen", "Câu thành công 10 con cá", 10, 0, 20));
        allAch.add(new Quest("a_fish_50", Quest.Type.ACHIEVEMENT, "Thợ Câu Mới Nổi", "Câu thành công 50 con cá", 50, 0, 50));
        allAch.add(new Quest("a_fish_100", Quest.Type.ACHIEVEMENT, "Khởi Đầu Gian Nan", "Câu thành công 100 con cá", 100, 0, 100));
        allAch.add(new Quest("a_fish_500", Quest.Type.ACHIEVEMENT, "Chuyên Gia Đánh Bắt", "Câu thành công 500 con cá", 500, 0, 300));
        allAch.add(new Quest("a_fish_1000", Quest.Type.ACHIEVEMENT, "Lão Ngư", "Câu thành công 1000 con cá", 1000, 0, 500));
        allAch.add(new Quest("a_fish_5000", Quest.Type.ACHIEVEMENT, "Sát Thủ Biển Sâu", "Câu thành công 5000 con cá", 5000, 0, 2000));

        // 2. Perfect Cast
        allAch.add(new Quest("a_perfect_1", Quest.Type.ACHIEVEMENT, "Canh Góc Tốt", "Đạt PERFECT 1 lần", 1, 0, 10));
        allAch.add(new Quest("a_perfect_10", Quest.Type.ACHIEVEMENT, "Quen Tay", "Đạt PERFECT 10 lần", 10, 0, 30));
        allAch.add(new Quest("a_perfect_50", Quest.Type.ACHIEVEMENT, "Bách Phát Bách Trúng", "Đạt PERFECT 50 lần", 50, 0, 150));
        allAch.add(new Quest("a_perfect_100", Quest.Type.ACHIEVEMENT, "Hoàn Mỹ", "Đạt PERFECT 100 lần", 100, 0, 400));
        allAch.add(new Quest("a_perfect_200", Quest.Type.ACHIEVEMENT, "Thần Đồng Cần Thủ", "Đạt PERFECT 200 lần", 200, 0, 800));
        allAch.add(new Quest("a_perfect_500", Quest.Type.ACHIEVEMENT, "Đỉnh Cao Kỹ Năng", "Đạt PERFECT 500 lần", 500, 0, 2000));

        // 3. Catch Rare Fish
        allAch.add(new Quest("a_rare_1", Quest.Type.ACHIEVEMENT, "May Mắn Đầu Tiên", "Câu 1 cá Rare trở lên", 1, 0, 20));
        allAch.add(new Quest("a_rare_10", Quest.Type.ACHIEVEMENT, "Săn Mồi Khủng", "Câu 10 cá Rare", 10, 0, 150));
        allAch.add(new Quest("a_rare_50", Quest.Type.ACHIEVEMENT, "Sát Thủ Cá Hiếm", "Câu 50 cá Rare", 50, 0, 800));
        allAch.add(new Quest("a_rare_200", Quest.Type.ACHIEVEMENT, "Kẻ Được Chọn", "Câu 200 cá Rare", 200, 0, 2500));
        allAch.add(new Quest("a_rare_500", Quest.Type.ACHIEVEMENT, "Vua Biển Cả", "Câu 500 cá Rare", 500, 0, 5000));

        // 4. Earn Gold
        allAch.add(new Quest("a_gold_1000", Quest.Type.ACHIEVEMENT, "Những Đồng Tiền Đầu Tiên", "Kiếm được 1000 Vàng", 1000, 0, 20));
        allAch.add(new Quest("a_gold_10k", Quest.Type.ACHIEVEMENT, "Làm Giàu Không Khó", "Kiếm được 10.000 Vàng", 10000, 0, 100));
        allAch.add(new Quest("a_gold_50k", Quest.Type.ACHIEVEMENT, "Túi Tiền Rủng Rỉnh", "Kiếm được 50.000 Vàng", 50000, 0, 300));
        allAch.add(new Quest("a_gold_200k", Quest.Type.ACHIEVEMENT, "Đại Gia", "Kiếm được 200.000 Vàng", 200000, 0, 1000));
        allAch.add(new Quest("a_gold_500k", Quest.Type.ACHIEVEMENT, "Chúa Tể Tiền Bạc", "Kiếm được 500.000 Vàng", 500000, 0, 3000));
        allAch.add(new Quest("a_gold_1m", Quest.Type.ACHIEVEMENT, "Tỷ Phú Biển Khơi", "Kiếm được 1.000.000 Vàng", 1000000, 0, 10000));

        // 5. Spend Gold
        allAch.add(new Quest("a_spend_1000", Quest.Type.ACHIEVEMENT, "Chi Tiêu Hợp Lý", "Tiêu 1000 Vàng", 1000, 0, 20));
        allAch.add(new Quest("a_spend_10k", Quest.Type.ACHIEVEMENT, "Đầu Tư Nâng Cấp", "Tiêu 10.000 Vàng", 10000, 0, 100));
        allAch.add(new Quest("a_spend_50k", Quest.Type.ACHIEVEMENT, "Khách Vip", "Tiêu 50.000 Vàng", 50000, 0, 400));
        allAch.add(new Quest("a_spend_100k", Quest.Type.ACHIEVEMENT, "Thương Gia", "Tiêu 100.000 Vàng", 100000, 0, 1000));
        allAch.add(new Quest("a_spend_500k", Quest.Type.ACHIEVEMENT, "Đốt Tiền Không Phanh", "Tiêu 500.000 Vàng", 500000, 0, 5000));

        // 6. Quay Gacha (gacha)
        allAch.add(new Quest("a_gacha_1", Quest.Type.ACHIEVEMENT, "Vận May Khởi Đầu", "Quay Gacha 1 lần", 1, 0, 10));
        allAch.add(new Quest("a_gacha_10", Quest.Type.ACHIEVEMENT, "Thử Thách Vận May", "Quay Gacha 10 lần", 10, 0, 50));
        allAch.add(new Quest("a_gacha_50", Quest.Type.ACHIEVEMENT, "Lún Sâu", "Quay Gacha 50 lần", 50, 0, 200));
        allAch.add(new Quest("a_gacha_100", Quest.Type.ACHIEVEMENT, "Tín Đồ Gacha", "Quay Gacha 100 lần", 100, 0, 500));
        allAch.add(new Quest("a_gacha_500", Quest.Type.ACHIEVEMENT, "Con Nghiện Gacha", "Quay Gacha 500 lần", 500, 0, 2000));
        allAch.add(new Quest("a_gacha_1000", Quest.Type.ACHIEVEMENT, "Hủy Diệt Nhân Phẩm", "Quay Gacha 1000 lần", 1000, 0, 5000));

        // 7. Use Skill
        allAch.add(new Quest("a_skill_1", Quest.Type.ACHIEVEMENT, "Sử Dụng Kỹ Năng", "Sử dụng Kỹ năng 1 lần", 1, 0, 10));
        allAch.add(new Quest("a_skill_10", Quest.Type.ACHIEVEMENT, "Dần Quen Tay", "Sử dụng Kỹ năng 10 lần", 10, 0, 50));
        allAch.add(new Quest("a_skill_50", Quest.Type.ACHIEVEMENT, "Thành Thạo", "Sử dụng Kỹ năng 50 lần", 50, 0, 200));
        allAch.add(new Quest("a_skill_100", Quest.Type.ACHIEVEMENT, "Bậc Thầy Kỹ Năng", "Sử dụng Kỹ năng 100 lần", 100, 0, 500));
        allAch.add(new Quest("a_skill_500", Quest.Type.ACHIEVEMENT, "Phù Thủy Đại Dương", "Sử dụng Kỹ năng 500 lần", 500, 0, 2000));

        // 8. Level Up
        allAch.add(new Quest("a_level_2", Quest.Type.ACHIEVEMENT, "Mới Vào Nghề", "Đạt Level 2", 2, 0, 20));
        allAch.add(new Quest("a_level_5", Quest.Type.ACHIEVEMENT, "Người Mới", "Đạt Level 5", 5, 0, 50));
        allAch.add(new Quest("a_level_10", Quest.Type.ACHIEVEMENT, "Cao Thủ", "Đạt Level 10", 10, 0, 150));
        allAch.add(new Quest("a_level_20", Quest.Type.ACHIEVEMENT, "Chuyên Gia", "Đạt Level 20", 20, 0, 400));
        allAch.add(new Quest("a_level_50", Quest.Type.ACHIEVEMENT, "Huyền Thoại Biển Khơi", "Đạt Level 50", 50, 0, 2000));
        allAch.add(new Quest("a_level_100", Quest.Type.ACHIEVEMENT, "Phá Đảo", "Đạt Level 100", 100, 0, 10000));
        
        for (Quest q : allAch) {
            boolean found = false;
            for (Quest existing : achievements) {
                if (existing.id.equals(q.id)) {
                    found = true;
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
}
