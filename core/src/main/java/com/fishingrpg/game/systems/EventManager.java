package com.fishingrpg.game.systems;

import com.badlogic.gdx.utils.TimeUtils;
import com.fishingrpg.game.entities.Player;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EventManager {

    private Player player;
    
    // Event specific constants
    public static final long EVENT_END_TIMESTAMP;
    
    // Daily missions status: 0 = not done, 1 = done
    // index 0: Catch 50 fish
    // index 1: Spend 2000 gold
    // index 2: Pull Event Gacha 1 time
    public boolean[] dailyMissionsCompleted = new boolean[3];
    public boolean allMissionsClaimedToday = false;
    public int fishCaughtToday = 0;
    public int goldSpentToday = 0;
    public int eventGachaPulledToday = 0;

    static {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.set(2026, Calendar.DECEMBER, 31, 23, 59, 59);
        long end = cal.getTimeInMillis();
        if (end <= TimeUtils.millis()) {
            end = TimeUtils.millis() + 60L * 24 * 60 * 60 * 1000L;
        }
        EVENT_END_TIMESTAMP = end;
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
        if (player.getLastEventMissionResetTimestamp() < currentDayStart) {
            // New day! Reset missions.
            player.setLastEventMissionResetTimestamp(currentDayStart);
            for (int i = 0; i < 3; i++) {
                dailyMissionsCompleted[i] = false;
            }
            allMissionsClaimedToday = false;
            fishCaughtToday = 0;
            goldSpentToday = 0;
            eventGachaPulledToday = 0;
        }
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
            case 2:  return new DailyReward(2, "15 Kim Cương", "+15 KC", false);
            case 3:  return new DailyReward(3, "3x Mồi Thơm", "x3 Mồi Thơm", false);
            case 4:  return new DailyReward(4, "1,000 Vàng", "+1000 Vàng", false);
            case 5:  return new DailyReward(5, "Mốc 5 Ngày", "+1 Điểm SK, +1 Vé Quay", true);
            case 6:  return new DailyReward(6, "1,200 Vàng", "+1200 Vàng", false);
            case 7:  return new DailyReward(7, "20 Kim Cương", "+20 KC", false);
            case 8:  return new DailyReward(8, "3x Bánh Mì", "x3 Bánh Mì", false);
            case 9:  return new DailyReward(9, "1,500 Vàng", "+1500 Vàng", false);
            case 10: return new DailyReward(10, "Mốc 10 Ngày", "+1 Điểm SK, +2 Vé Quay", true);
            case 11: return new DailyReward(11, "2,000 Vàng", "+2000 Vàng", false);
            case 12: return new DailyReward(12, "25 Kim Cương", "+25 KC", false);
            case 13: return new DailyReward(13, "5x Mồi Rủi Ro", "x5 Mồi Rủi Ro", false);
            case 14: return new DailyReward(14, "2,500 Vàng", "+2500 Vàng", false);
            case 15: return new DailyReward(15, "Mốc 15 Ngày", "+2 Điểm SK, +1 Vé Cao Cấp", true);
            case 16: return new DailyReward(16, "3,000 Vàng", "+3000 Vàng", false);
            case 17: return new DailyReward(17, "30 Kim Cương", "+30 KC", false);
            case 18: return new DailyReward(18, "3,500 Vàng", "+3500 Vàng", false);
            case 19: return new DailyReward(19, "35 Kim Cương", "+35 KC", false);
            case 20: return new DailyReward(20, "Mốc 20 Ngày", "+2 Điểm SK, +2 Vé Cao Cấp", true);
            case 21: return new DailyReward(21, "4,000 Vàng", "+4000 Vàng", false);
            case 22: return new DailyReward(22, "40 Kim Cương", "+40 KC", false);
            case 23: return new DailyReward(23, "5,000 Vàng", "+5000 Vàng", false);
            case 24: return new DailyReward(24, "50 Kim Cương", "+50 KC", false);
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
            case 2:  player.addDiamonds(15); break;
            case 3:  player.addItem("Mồi Thơm", 3); break;
            case 4:  player.gainGold(1000); break;
            case 5:  player.addEventPoints(1); player.addGachaTickets(1); break;
            case 6:  player.gainGold(1200); break;
            case 7:  player.addDiamonds(20); break;
            case 8:  player.addItem("Bánh Mì", 3); break;
            case 9:  player.gainGold(1500); break;
            case 10: player.addEventPoints(1); player.addGachaTickets(2); break;
            case 11: player.gainGold(2000); break;
            case 12: player.addDiamonds(25); break;
            case 13: player.addItem("Mồi Rủi Ro", 5); break;
            case 14: player.gainGold(2500); break;
            case 15: player.addEventPoints(2); player.addPremiumTickets(1); break;
            case 16: player.gainGold(3000); break;
            case 17: player.addDiamonds(30); break;
            case 18: player.gainGold(3500); break;
            case 19: player.addDiamonds(35); break;
            case 20: player.addEventPoints(2); player.addPremiumTickets(2); break;
            case 21: player.gainGold(4000); break;
            case 22: player.addDiamonds(40); break;
            case 23: player.gainGold(5000); break;
            case 24: player.addDiamonds(50); break;
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
        for (boolean done : dailyMissionsCompleted) {
            if (!done) return true;
        }
        return false;
    }

    public void onFishCaught() {
        checkAndResetDailyMissions();
        if (!dailyMissionsCompleted[0]) {
            fishCaughtToday++;
            if (fishCaughtToday >= 50) {
                dailyMissionsCompleted[0] = true;
                checkAllCompleted();
            }
        }
    }

    public void onGoldSpent(int amount) {
        checkAndResetDailyMissions();
        if (!dailyMissionsCompleted[1]) {
            goldSpentToday += amount;
            if (goldSpentToday >= 2000) {
                dailyMissionsCompleted[1] = true;
                checkAllCompleted();
            }
        }
    }

    public void onEventGachaPulled() {
        checkAndResetDailyMissions();
        if (!dailyMissionsCompleted[2]) {
            eventGachaPulledToday++;
            if (eventGachaPulledToday >= 1) {
                dailyMissionsCompleted[2] = true;
                checkAllCompleted();
            }
        }
    }

    private void checkAllCompleted() {
        if (!allMissionsClaimedToday && dailyMissionsCompleted[0] && dailyMissionsCompleted[1] && dailyMissionsCompleted[2]) {
            allMissionsClaimedToday = true;
            player.addEventPoints(1);
        }
    }

    public String getEventTimeRemaining() {
        long now = TimeUtils.millis();
        long diff = EVENT_END_TIMESTAMP - now;
        if (diff <= 0) return "SỰ KIỆN KẾT THÚC";
        
        long days = diff / (24 * 60 * 60 * 1000L);
        long hours = (diff % (24 * 60 * 60 * 1000L)) / (60 * 60 * 1000L);
        return "Còn lại: " + days + " ngày " + hours + " giờ";
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
