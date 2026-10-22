package com.fishingrpg.game.systems;

import com.badlogic.gdx.utils.TimeUtils;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.entities.Quest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

public class EventManager {

    private transient Player player;
    
    // Event specific constants
    public static final long EVENT_END_TIMESTAMP;
    
    // 6 Daily Event Quests & Milestone
    public final List<Quest> eventQuests = new ArrayList<>();
    public boolean milestoneClaimedToday = false;

    static {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.set(2026, Calendar.DECEMBER, 31, 23, 59, 59);
        long end = cal.getTimeInMillis();
        if (end <= TimeUtils.millis()) {
            end = TimeUtils.millis() + 60L * 24 * 60 * 60 * 1000L;
        }
        EVENT_END_TIMESTAMP = end;
    }

    public static class EventQuestTemplate {
        public String id;
        public String title;
        public String description;
        public int target;
        public int rewardGold;
        public int rewardDiamonds;
        public String rewardItem;
        public int rewardItemCount;

        public EventQuestTemplate(String id, String title, String description, int target, int rewardGold, int rewardDiamonds, String rewardItem, int rewardItemCount) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.target = target;
            this.rewardGold = rewardGold;
            this.rewardDiamonds = rewardDiamonds;
            this.rewardItem = rewardItem;
            this.rewardItemCount = rewardItemCount;
        }

        public Quest createQuest() {
            return new Quest(id, Quest.Type.EVENT, title, description, target, 0, rewardDiamonds, rewardGold, rewardItem, rewardItemCount);
        }
    }

    private static final List<EventQuestTemplate> EVENT_QUEST_POOL = new ArrayList<>();
    static {
        EVENT_QUEST_POOL.add(new EventQuestTemplate("ev_catch", "Hào Khí Đánh Bắt", "Câu thành công 5 con cá", 5, 300, 0, null, 0));
        EVENT_QUEST_POOL.add(new EventQuestTemplate("ev_perfect", "Tay Cần Lạc Hồng", "Đạt PERFECT khi quăng cần 2 lần", 2, 0, 5, null, 0));
        EVENT_QUEST_POOL.add(new EventQuestTemplate("ev_spend", "Giao Thương Hội Làng", "Tiêu 150 Vàng trong ngày", 150, 300, 0, null, 0));
        EVENT_QUEST_POOL.add(new EventQuestTemplate("ev_gacha", "Vận May Sự Kiện", "Quay Gacha 1 lần", 1, 0, 0, "Vé Quay", 1));
        EVENT_QUEST_POOL.add(new EventQuestTemplate("ev_rare", "Cá Quý Đại Dương", "Câu 1 con cá Rare trở lên", 1, 0, 5, null, 0));
        EVENT_QUEST_POOL.add(new EventQuestTemplate("ev_skill", "Khí Phách Ngư Dân", "Dùng 3 lần Kỹ năng khi câu cá", 3, 250, 0, null, 0));
        EVENT_QUEST_POOL.add(new EventQuestTemplate("ev_catch_3", "Ra Khơi Quyết Thắng", "Câu thành công 3 con cá", 3, 200, 0, null, 0));
        EVENT_QUEST_POOL.add(new EventQuestTemplate("ev_perfect_1", "Bách Phát Bách Trúng", "Đạt PERFECT 1 lần khi quăng cần", 1, 0, 3, null, 0));
        EVENT_QUEST_POOL.add(new EventQuestTemplate("ev_spend_100", "Trù Bị Ngư Cụ", "Tiêu 100 Vàng tại Tiệm Tạp Hóa", 100, 200, 0, null, 0));
        EVENT_QUEST_POOL.add(new EventQuestTemplate("ev_skill_2", "Tuyệt Kỹ Ngư Gia", "Dùng 2 lần Kỹ năng trong lúc câu", 2, 200, 0, null, 0));
        EVENT_QUEST_POOL.add(new EventQuestTemplate("ev_rare_boss", "Chiến Tích Vang Danh", "Bắt 1 con cá Rare trở lên", 1, 0, 8, null, 0));
        EVENT_QUEST_POOL.add(new EventQuestTemplate("ev_catch_7", "Ngư Phủ Tinh Anh", "Câu thành công 7 con cá", 7, 400, 0, null, 0));
    }

    public static class DailyReward {
        public final int day;
        public final String name;
        public final String rewardSummary;
        public final boolean isMilestone;
        
        public DailyReward(int day, String name, String rewardSummary, boolean isMilestone) {
            this.day = day;
            this.name = name;
            this.rewardSummary = rewardSummary;
            this.isMilestone = isMilestone;
        }
    }

    public EventManager(Player player) {
        this.player = player;
        checkAndResetDailyMissions();
    }

    public void checkAndResetDailyMissions() {
        long currentDayStart = getStartOfDay(TimeUtils.millis());
        if (player.getLastEventMissionResetTimestamp() < currentDayStart || eventQuests.isEmpty()) {
            player.setLastEventMissionResetTimestamp(currentDayStart);
            resetEventQuests();
        }
    }

    public void resetEventQuests() {
        eventQuests.clear();
        milestoneClaimedToday = false;
        List<EventQuestTemplate> pool = new ArrayList<>(EVENT_QUEST_POOL);
        Collections.shuffle(pool);
        int pickCount = Math.min(6, pool.size());
        for (int i = 0; i < pickCount; i++) {
            eventQuests.add(pool.get(i).createQuest());
        }
    }

    public int getCompletedMissionsCount() {
        int count = 0;
        for (Quest q : eventQuests) {
            if (q.isCompleted()) count++;
        }
        return count;
    }

    public boolean canClaimMilestone() {
        return !milestoneClaimedToday && getCompletedMissionsCount() >= 4;
    }

    public String claimMilestone() {
        if (!canClaimMilestone()) return null;
        milestoneClaimedToday = true;
        player.addEventPoints(1);
        player.gainGold(500);
        player.addDiamonds(10);
        return "Nhận Mốc 4/6: +1 Điểm Sự Kiện, +500 Vàng, +10 KC!";
    }

    public String claimQuestReward(Quest q) {
        if (q == null || !q.isCompleted() || q.claimed) return null;
        q.claimed = true;
        StringBuilder sb = new StringBuilder("Nhận thưởng " + q.title + ": ");
        if (q.rewardGold > 0) {
            player.gainGold(q.rewardGold);
            sb.append("+").append(q.rewardGold).append(" Vàng ");
        }
        if (q.rewardDiamonds > 0) {
            player.addDiamonds(q.rewardDiamonds);
            sb.append("+").append(q.rewardDiamonds).append(" KC ");
        }
        if (q.rewardItem != null && !q.rewardItem.isEmpty()) {
            if (q.rewardItem.equals("Vé Quay")) {
                player.addGachaTickets(q.rewardItemCount > 0 ? q.rewardItemCount : 1);
            } else {
                player.addItem(q.rewardItem, q.rewardItemCount > 0 ? q.rewardItemCount : 1);
            }
            sb.append("+").append(q.rewardItemCount > 0 ? q.rewardItemCount : 1).append(" ").append(q.rewardItem).append(" ");
        }
        return sb.toString().trim() + "!";
    }

    public void addProgress(String prefix, int amount) {
        checkAndResetDailyMissions();
        for (Quest q : eventQuests) {
            if (q.id.startsWith("ev_" + prefix) && !q.isCompleted()) {
                q.addProgress(amount);
            }
        }
    }

    public void onFishCaught() {
        addProgress("catch", 1);
    }

    public void onGoldSpent(int amount) {
        addProgress("spend", amount);
    }

    public void onEventGachaPulled() {
        addProgress("gacha", 1);
    }

    public boolean hasClaimedLoginToday() {
        long currentDayStart = getStartOfDay(TimeUtils.millis());
        return player.getLastLoginTimestamp() >= currentDayStart;
    }

    public DailyReward getDailyReward(int day) {
        if (day < 1) day = 1;
        if (day > 25) day = 25;
        switch (day) {
            case 1:  return new DailyReward(1, "500 Vàng", "+500 Vàng", false);
            case 2:  return new DailyReward(2, "10 Kim Cương", "+10 KC", false);
            case 3:  return new DailyReward(3, "3x Mồi Thơm", "x3 Mồi Thơm", false);
            case 4:  return new DailyReward(4, "1,000 Vàng", "+1000 Vàng", false);
            case 5:  return new DailyReward(5, "Mốc 5 Ngày", "+1 Điểm SK, +1 Vé Quay", true);
            case 6:  return new DailyReward(6, "1,200 Vàng", "+1200 Vàng", false);
            case 7:  return new DailyReward(7, "15 Kim Cương", "+15 KC", false);
            case 8:  return new DailyReward(8, "3x Bánh Mì", "x3 Bánh Mì", false);
            case 9:  return new DailyReward(9, "1,500 Vàng", "+1500 Vàng", false);
            case 10: return new DailyReward(10, "Mốc 10 Ngày", "+1 Điểm SK, +2 Vé Quay", true);
            case 11: return new DailyReward(11, "2,000 Vàng", "+2000 Vàng", false);
            case 12: return new DailyReward(12, "15 Kim Cương", "+15 KC", false);
            case 13: return new DailyReward(13, "5x Mồi Rủi Ro", "x5 Mồi Rủi Ro", false);
            case 14: return new DailyReward(14, "2,500 Vàng", "+2500 Vàng", false);
            case 15: return new DailyReward(15, "Mốc 15 Ngày", "+2 Điểm SK, +1 Vé Cao Cấp", true);
            case 16: return new DailyReward(16, "3,000 Vàng", "+3000 Vàng", false);
            case 17: return new DailyReward(17, "20 Kim Cương", "+20 KC", false);
            case 18: return new DailyReward(18, "3,500 Vàng", "+3500 Vàng", false);
            case 19: return new DailyReward(19, "20 Kim Cương", "+20 KC", false);
            case 20: return new DailyReward(20, "Mốc 20 Ngày", "+2 Điểm SK, +2 Vé Cao Cấp", true);
            case 21: return new DailyReward(21, "4,000 Vàng", "+4000 Vàng", false);
            case 22: return new DailyReward(22, "25 Kim Cương", "+25 KC", false);
            case 23: return new DailyReward(23, "5,000 Vàng", "+5000 Vàng", false);
            case 24: return new DailyReward(24, "30 Kim Cương", "+30 KC", false);
            case 25: return new DailyReward(25, "Đại Mốc 25 Ngày", "+3 Điểm SK, Cần Vệ Quốc", true);
            default: return new DailyReward(day, "1,000 Vàng", "+1000 Vàng", false);
        }
    }

    public String claimDailyLoginReward() {
        checkAndResetDailyMissions();
        if (hasClaimedLoginToday()) return null;

        long currentDayStart = getStartOfDay(TimeUtils.millis());
        player.setLastLoginTimestamp(currentDayStart);

        int currentDays = player.getTotalLoginDaysThisMonth();
        int nextDay = Math.min(25, currentDays + 1);
        player.setTotalLoginDaysThisMonth(nextDay);

        DailyReward r = getDailyReward(nextDay);
        switch (nextDay) {
            case 1:  player.gainGold(500); break;
            case 2:  player.addDiamonds(10); break;
            case 3:  player.addItem("Mồi Thơm", 3); break;
            case 4:  player.gainGold(1000); break;
            case 5:  player.addEventPoints(1); player.addGachaTickets(1); break;
            case 6:  player.gainGold(1200); break;
            case 7:  player.addDiamonds(15); break;
            case 8:  player.addItem("Bánh Mì", 3); break;
            case 9:  player.gainGold(1500); break;
            case 10: player.addEventPoints(1); player.addGachaTickets(2); break;
            case 11: player.gainGold(2000); break;
            case 12: player.addDiamonds(15); break;
            case 13: player.addItem("Mồi Rủi Ro", 5); break;
            case 14: player.gainGold(2500); break;
            case 15: player.addEventPoints(2); player.addPremiumTickets(1); break;
            case 16: player.gainGold(3000); break;
            case 17: player.addDiamonds(20); break;
            case 18: player.gainGold(3500); break;
            case 19: player.addDiamonds(20); break;
            case 20: player.addEventPoints(2); player.addPremiumTickets(2); break;
            case 21: player.gainGold(4000); break;
            case 22: player.addDiamonds(25); break;
            case 23: player.gainGold(5000); break;
            case 24: player.addDiamonds(30); break;
            case 25: 
                player.addEventPoints(3); 
                player.addPremiumTickets(3); 
                player.addEquipment(com.fishingrpg.game.entities.equipment.EquipmentGenerator.createFreeEventEquipment(com.fishingrpg.game.entities.equipment.EquipmentType.ROD)); 
                break;
            default: player.gainGold(1000); break;
        }

        return "Điểm danh Ngày " + nextDay + ": Nhận " + r.rewardSummary + "!";
    }

    public boolean hasNotification() {
        checkAndResetDailyMissions();
        if (!hasClaimedLoginToday() && player.getTotalLoginDaysThisMonth() < 25) {
            return true;
        }
        for (Quest q : eventQuests) {
            if (q.isCompleted() && !q.claimed) return true;
        }
        if (canClaimMilestone()) return true;
        return false;
    }

    public String getEventTimeRemaining() {
        long now = TimeUtils.millis();
        long diff = EVENT_END_TIMESTAMP - now;
        if (diff <= 0) return "SỰ KIỆN KẾT THÚC";
        
        long days = diff / (24 * 60 * 60 * 1000L);
        long hours = (diff % (24 * 60 * 60 * 1000L)) / (60 * 60 * 1000L);
        return days + " ngày " + hours + " giờ";
    }

    public long getStartOfDay(long millis) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTimeInMillis(millis);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
}
