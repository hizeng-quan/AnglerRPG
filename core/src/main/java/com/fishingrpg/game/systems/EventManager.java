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

    public static final String LAC_HONG_EVENT_ID = "lac_hong_2026";
    public static final String HALLOWEEN_EVENT_ID = "halloween_2026";
    public static final String CURRENT_EVENT_ID = LAC_HONG_EVENT_ID; // Backward-compatible alias
    public static final long EVENT_END_TIMESTAMP;
    public static final long HALLOWEEN_START_TIMESTAMP;
    public static final long HALLOWEEN_END_TIMESTAMP;

    public enum EventPhase {
        LAC_HONG_ACTIVE,
        PRE_HALLOWEEN_INTERMISSION,
        HALLOWEEN_ACTIVE,
        POST_EVENT_GRACE

    }

    // 6 Daily Event Quests & Milestone

    public final List<Quest> eventQuests = new ArrayList<>();
    public boolean milestoneClaimedToday = false;

    static {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.set(2026, Calendar.OCTOBER, 29, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 999);
        EVENT_END_TIMESTAMP = cal.getTimeInMillis();
        Calendar calHlw = Calendar.getInstance(TimeZone.getDefault());
        calHlw.set(2026, Calendar.OCTOBER, 30, 0, 0, 0);
        calHlw.set(Calendar.MILLISECOND, 0);
        HALLOWEEN_START_TIMESTAMP = calHlw.getTimeInMillis();
        Calendar calHlwEnd = Calendar.getInstance(TimeZone.getDefault());
        calHlwEnd.set(2026, Calendar.NOVEMBER, 26, 23, 59, 59);
        calHlwEnd.set(Calendar.MILLISECOND, 999);
        HALLOWEEN_END_TIMESTAMP = calHlwEnd.getTimeInMillis();

    }

    public static EventPhase getCurrentPhase() {
        long now = TimeSyncService.getCurrentTimeMillis();

        if (now <= EVENT_END_TIMESTAMP) {
            return EventPhase.LAC_HONG_ACTIVE;
        } else if (now < HALLOWEEN_START_TIMESTAMP) {
            return EventPhase.PRE_HALLOWEEN_INTERMISSION;
        } else if (now <= HALLOWEEN_END_TIMESTAMP) {
            return EventPhase.HALLOWEEN_ACTIVE;
        } else {
            return EventPhase.POST_EVENT_GRACE;
        }

    }

    public static String getCurrentEventId() {
        return isHalloweenActive() ? HALLOWEEN_EVENT_ID : LAC_HONG_EVENT_ID;

    }

    public static boolean isEventActive() {
        EventPhase phase = getCurrentPhase();
        return phase == EventPhase.LAC_HONG_ACTIVE || phase == EventPhase.HALLOWEEN_ACTIVE;

    }

    public static boolean isHalloweenActive() {
        return getCurrentPhase() == EventPhase.HALLOWEEN_ACTIVE;

    }

    public static boolean isLacHongActive() {
        return getCurrentPhase() == EventPhase.LAC_HONG_ACTIVE;

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

        public EventQuestTemplate(String id, String title, String description, int target, int rewardGold,
                int rewardDiamonds, String rewardItem, int rewardItemCount) {
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
            return new Quest(id, Quest.Type.EVENT, title, description, target, 0, rewardDiamonds, rewardGold,
                    rewardItem, rewardItemCount);
        }

    }

    private static final List<EventQuestTemplate> EVENT_QUEST_POOL = new ArrayList<>();

    static {
        EVENT_QUEST_POOL.add(
                new EventQuestTemplate("ev_catch", "Hào Khí Đánh Bắt", "Câu thành công 5 con cá", 5, 0, 0, null, 0));
        EVENT_QUEST_POOL.add(new EventQuestTemplate("ev_perfect", "Tay Cần Lạc Hồng", "Đạt PERFECT khi quăng cần 2 lần",
                2, 0, 0, null, 0));
        EVENT_QUEST_POOL.add(new EventQuestTemplate("ev_spend", "Giao Thương Hội Làng", "Tiêu 150 Vàng trong ngày", 150,
                0, 0, null, 0));
        EVENT_QUEST_POOL
                .add(new EventQuestTemplate("ev_gacha", "Vận May Sự Kiện", "Quay Gacha 1 lần", 1, 0, 0, null, 0));
        EVENT_QUEST_POOL.add(
                new EventQuestTemplate("ev_rare", "Cá Quý Đại Dương", "Câu 1 con cá Rare trở lên", 1, 0, 0, null, 0));
        EVENT_QUEST_POOL.add(new EventQuestTemplate("ev_skill", "Khí Phách Ngư Dân", "Dùng 3 lần Kỹ năng khi câu cá", 3,
                0, 0, null, 0));
        EVENT_QUEST_POOL.add(new EventQuestTemplate("ev_catch_3", "Ra Khơi Quyết Thắng", "Câu thành công 3 con cá", 3,
                0, 0, null, 0));
        EVENT_QUEST_POOL.add(new EventQuestTemplate("ev_perfect_1", "Bách Phát Bách Trúng",
                "Đạt PERFECT 1 lần khi quăng cần", 1, 0, 0, null, 0));
        EVENT_QUEST_POOL.add(new EventQuestTemplate("ev_spend_100", "Trù Bị Ngư Cụ", "Tiêu 100 Vàng tại Tiệm Tạp Hóa",
                100, 0, 0, null, 0));
        EVENT_QUEST_POOL.add(new EventQuestTemplate("ev_skill_2", "Tuyệt Kỹ Ngư Gia",
                "Dùng 2 lần Kỹ năng trong lúc câu", 2, 0, 0, null, 0));
        EVENT_QUEST_POOL.add(new EventQuestTemplate("ev_rare_boss", "Chiến Tích Vang Danh", "Bắt 1 con cá Rare trở lên",
                1, 0, 0, null, 0));
        EVENT_QUEST_POOL.add(
                new EventQuestTemplate("ev_catch_7", "Ngư Phủ Tinh Anh", "Câu thành công 7 con cá", 7, 0, 0, null, 0));

    }

    private static final List<EventQuestTemplate> HALLOWEEN_QUEST_POOL = new ArrayList<>();

    static {
        HALLOWEEN_QUEST_POOL.add(
                new EventQuestTemplate("ev_catch", "Săn Lùng Hồn Ma", "Câu thành công 5 con cá", 5, 0, 0, null, 0));
        HALLOWEEN_QUEST_POOL.add(new EventQuestTemplate("ev_perfect", "Ánh Sáng Bí Ngô",
                "Đạt PERFECT khi quăng cần 2 lần", 2, 0, 0, null, 0));
        HALLOWEEN_QUEST_POOL.add(new EventQuestTemplate("ev_spend", "Giao Thương Chợ Đêm", "Tiêu 150 Vàng trong ngày",
                150, 0, 0, null, 0));
        HALLOWEEN_QUEST_POOL
                .add(new EventQuestTemplate("ev_gacha", "Chiêu Hồn Ma Quái", "Quay Gacha 1 lần", 1, 0, 0, null, 0));
        HALLOWEEN_QUEST_POOL.add(
                new EventQuestTemplate("ev_rare", "Thủy Tộc Biến Dị", "Câu 1 con cá Rare trở lên", 1, 0, 0, null, 0));
        HALLOWEEN_QUEST_POOL.add(new EventQuestTemplate("ev_skill", "Bùa Chú Trừ Tà", "Dùng 3 lần Kỹ năng khi câu cá",
                3, 0, 0, null, 0));
        HALLOWEEN_QUEST_POOL.add(
                new EventQuestTemplate("ev_catch_3", "Khám Phá Vực Tối", "Câu thành công 3 con cá", 3, 0, 0, null, 0));
        HALLOWEEN_QUEST_POOL.add(new EventQuestTemplate("ev_perfect_1", "Nguyện Vọng Trăng Máu",
                "Đạt PERFECT 1 lần khi quăng cần", 1, 0, 0, null, 0));
        HALLOWEEN_QUEST_POOL.add(new EventQuestTemplate("ev_spend_100", "Trù Bị Lễ Hội",
                "Tiêu 100 Vàng tại Tiệm Tạp Hóa", 100, 0, 0, null, 0));
        HALLOWEEN_QUEST_POOL.add(new EventQuestTemplate("ev_skill_2", "Phép Thuật Huyền Bí",
                "Dùng 2 lần Kỹ năng trong lúc câu", 2, 0, 0, null, 0));
        HALLOWEEN_QUEST_POOL.add(new EventQuestTemplate("ev_rare_boss", "Chiến Tích Ma Quái",
                "Bắt 1 con cá Rare trở lên", 1, 0, 0, null, 0));
        HALLOWEEN_QUEST_POOL.add(
                new EventQuestTemplate("ev_catch_7", "Thợ Săn Linh Hồn", "Câu thành công 7 con cá", 7, 0, 0, null, 0));

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
        checkEventTransition();
        long currentTime = TimeSyncService.getCurrentTimeMillis();
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTimeInMillis(currentTime);
        long lastResetTime = player.getLastEventMissionResetTimestamp();
        Calendar lastReset = Calendar.getInstance(TimeZone.getDefault());
        lastReset.setTimeInMillis(lastResetTime);
        boolean isNewDay = (lastResetTime <= 0L)
                || (now.get(Calendar.YEAR) != lastReset.get(Calendar.YEAR))
                || (now.get(Calendar.DAY_OF_YEAR) != lastReset.get(Calendar.DAY_OF_YEAR));

        // Kiểm tra xem pool nhiệm vụ hiện tại có khớp với mùa sự kiện đang diễn ra
        // không
        boolean wrongPool = false;
        if (!eventQuests.isEmpty()) {
            boolean isHlw = isHalloweenActive();
            // Nếu Halloween đang active mà nhiệm vụ có tên thời Lạc Hồng (vd: Tay Cần Lạc
            // Hồng, Giao Thương Hội Làng, Hào Khí Đánh Bắt)
            String firstTitle = eventQuests.get(0).title;
            boolean hasHlwTemplate = false;
            for (EventQuestTemplate t : HALLOWEEN_QUEST_POOL) {
                if (t.title.equals(firstTitle)) {
                    hasHlwTemplate = true;
                    break;
                }
            }
            if (isHlw && !hasHlwTemplate)
                wrongPool = true;
            else if (!isHlw && hasHlwTemplate)
                wrongPool = true;
        }

        if (isNewDay || eventQuests.isEmpty() || wrongPool) {
            player.setLastEventMissionResetTimestamp(currentTime);
            resetEventQuests();
        }

    }

    public String getTimeUntilMidnight() {
        long now = TimeSyncService.getCurrentTimeMillis();
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTimeInMillis(now);
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long diff = Math.max(0L, cal.getTimeInMillis() - now);
        long hours = diff / (60 * 60 * 1000L);
        long minutes = (diff % (60 * 60 * 1000L)) / (60 * 1000L);
        return String.format(java.util.Locale.US, "%02dh%02dp", hours, minutes);

    }

    private boolean isTransitioning = false;

    public void checkEventTransition() {
        if (isTransitioning)
            return;
        isTransitioning = true;
        try {
            EventPhase phase = getCurrentPhase();

            // 1. Khi Lạc Hồng kết thúc bước vào Intermission: quy đổi điểm dư
            if (phase == EventPhase.PRE_HALLOWEEN_INTERMISSION) {
                if (player.getEventPoints() > 0) {
                    int leftover = player.getEventPoints();
                    int compensation = leftover * 1000;
                    player.setEventPoints(0);
                    player.gainGold(compensation);
                    com.badlogic.gdx.Gdx.app.log("EventManager", "Sự kiện Lạc Hồng kết thúc: Đã quy đổi " + leftover
                            + " Điểm SK thành " + compensation + " Vàng!");
                }
            }

            // 2. Khi bước sang mùa Halloween: tự động reset điểm danh & điểm SK cho mùa mới
            // 1 lần duy nhất
            if (phase == EventPhase.HALLOWEEN_ACTIVE) {
                if (!player.isHalloweenResetDone()) {
                    int leftover = player.getEventPoints();
                    player.setEventPoints(0);
                    player.setTotalLoginDaysThisMonth(0);
                    player.setHalloweenResetDone(true);
                    resetEventQuests();
                    if (leftover > 0) {
                        int compensation = leftover * 1000;
                        player.gainGold(compensation);
                        com.badlogic.gdx.Gdx.app.log("EventManager",
                                "Tự động quy đổi " + leftover + " Điểm SK tồn đọng thành " + compensation + " Vàng!");
                    }
                    com.badlogic.gdx.Gdx.app.log("EventManager",
                            "Khởi động Sự Kiện Halloween 2026: Đã kích hoạt mùa mới!");
                }
            }

            // 3. Khi bế mạc sau Halloween:
            if (phase == EventPhase.POST_EVENT_GRACE) {
                if (player.getEventPoints() > 0) {
                    int leftover = player.getEventPoints();
                    int compensation = leftover * 1000;
                    player.setEventPoints(0);
                    player.gainGold(compensation);
                    com.badlogic.gdx.Gdx.app.log("EventManager", "Sự kiện Halloween kết thúc: Đã quy đổi " + leftover
                            + " Điểm SK thành " + compensation + " Vàng!");
                }
            }
        } finally {
            isTransitioning = false;
        }
    }

    public void resetEventQuests() {
        eventQuests.clear();
        milestoneClaimedToday = false;
        List<EventQuestTemplate> sourcePool = isHalloweenActive() ? HALLOWEEN_QUEST_POOL : EVENT_QUEST_POOL;
        List<EventQuestTemplate> pool = new ArrayList<>(sourcePool);
        Collections.shuffle(pool);
        int pickCount = Math.min(6, pool.size());

        for (int i = 0; i < pickCount; i++) {
            eventQuests.add(pool.get(i).createQuest());
        }

    }

    public int getCompletedMissionsCount() {
        int count = 0;

        for (Quest q : eventQuests) {
            if (q.isCompleted())
                count++;
        }

        return count;

    }

    public boolean canClaimMilestone() {
        return !milestoneClaimedToday && getCompletedMissionsCount() >= 4;

    }

    public String claimMilestone() {
        if (!canClaimMilestone())
            return null;
        milestoneClaimedToday = true;
        player.addEventPoints(100);
        player.gainGold(500);
        player.addDiamonds(10);
        return "Nhận Mốc 4/6: +100 Điểm Sự Kiện, +500 Vàng, +10 KC!";

    }

    public String claimQuestReward(Quest q) {
        if (q == null || !q.isCompleted() || q.claimed)
            return null;
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

            sb.append("+").append(q.rewardItemCount > 0 ? q.rewardItemCount : 1).append(" ").append(q.rewardItem)
                    .append(" ");
        }

        return sb.toString().trim() + "!";

    }

    public void addProgress(String prefix, int amount) {
        checkAndResetDailyMissions();

        for (Quest q : eventQuests) {
            if ((q.id.startsWith("ev_" + prefix) || q.id.startsWith("ev_hlw_" + prefix)) && !q.isCompleted()) {
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
        long currentDayStart = getStartOfDay(TimeSyncService.getCurrentTimeMillis());
        return player.getLastLoginTimestamp() >= currentDayStart;

    }

    public DailyReward getDailyReward(int day) {
        if (day < 1)
            day = 1;
        if (day > 25)
            day = 25;

        if (isHalloweenActive()) {
            return getHalloweenDailyReward(day);
        } else {
            return getLacHongDailyReward(day);
        }

    }

    public DailyReward getLacHongDailyReward(int day) {
        switch (day) {
            case 1:
                return new DailyReward(1, "500 Vàng", "+500 Vàng", false);
            case 2:
                return new DailyReward(2, "10 Kim Cương", "+10 KC", false);
            case 3:
                return new DailyReward(3, "3x Mồi Thơm", "x3 Mồi Thơm", false);
            case 4:
                return new DailyReward(4, "1,000 Vàng", "+1000 Vàng", false);
            case 5:
                return new DailyReward(5, "Mốc 5 Ngày", "+1 Điểm SK, +1 Vé Quay", true);
            case 6:
                return new DailyReward(6, "1,200 Vàng", "+1200 Vàng", false);
            case 7:
                return new DailyReward(7, "15 Kim Cương", "+15 KC", false);
            case 8:
                return new DailyReward(8, "3x Bánh Mì", "x3 Bánh Mì", false);
            case 9:
                return new DailyReward(9, "1,500 Vàng", "+1500 Vàng", false);
            case 10:
                return new DailyReward(10, "Mốc 10 Ngày", "+1 Điểm SK, +2 Vé Quay", true);
            case 11:
                return new DailyReward(11, "2,000 Vàng", "+2000 Vàng", false);
            case 12:
                return new DailyReward(12, "15 Kim Cương", "+15 KC", false);
            case 13:
                return new DailyReward(13, "5x Mồi Rủi Ro", "x5 Mồi Rủi Ro", false);
            case 14:
                return new DailyReward(14, "2,500 Vàng", "+2500 Vàng", false);
            case 15:
                return new DailyReward(15, "Mốc 15 Ngày", "+2 Điểm SK, +1 Vé Cao Cấp", true);
            case 16:
                return new DailyReward(16, "3,000 Vàng", "+3000 Vàng", false);
            case 17:
                return new DailyReward(17, "20 Kim Cương", "+20 KC", false);
            case 18:
                return new DailyReward(18, "3,500 Vàng", "+3500 Vàng", false);
            case 19:
                return new DailyReward(19, "20 Kim Cương", "+20 KC", false);
            case 20:
                return new DailyReward(20, "Mốc 20 Ngày", "+2 Điểm SK, +2 Vé Cao Cấp", true);
            case 21:
                return new DailyReward(21, "4,000 Vàng", "+4000 Vàng", false);
            case 22:
                return new DailyReward(22, "25 Kim Cương", "+25 KC", false);
            case 23:
                return new DailyReward(23, "5,000 Vàng", "+5000 Vàng", false);
            case 24:
                return new DailyReward(24, "30 Kim Cương", "+30 KC", false);
            case 25:
                return new DailyReward(25, "Đại Mốc 25 Ngày", "+3 Điểm SK, Cần Vệ Quốc", true);
            default:
                return new DailyReward(day, "1,000 Vàng", "+1000 Vàng", false);
        }

    }

    public DailyReward getHalloweenDailyReward(int day) {
        switch (day) {
            case 1:
                return new DailyReward(1, "1,000 Vàng", "+1000 Vàng", false);
            case 2:
                return new DailyReward(2, "15 Kim Cương", "+15 KC", false);
            case 3:
                return new DailyReward(3, "2x Kẹo Ma Thuật", "x2 Kẹo Trick-or-Treat", false);
            case 4:
                return new DailyReward(4, "1,500 Vàng", "+1500 Vàng", false);
            case 5:
                return new DailyReward(5, "Mốc 5 Ngày", "+1 Điểm SK, +2 Vé Quay, 1x Kẹo", true);
            case 6:
                return new DailyReward(6, "2,000 Vàng", "+2000 Vàng", false);
            case 7:
                return new DailyReward(7, "20 Kim Cương", "+20 KC", false);
            case 8:
                return new DailyReward(8, "1x Đèn Lồng Bí Ngô", "1x Đèn Lồng Bí Ngô", false);
            case 9:
                return new DailyReward(9, "2,500 Vàng", "+2500 Vàng", false);
            case 10:
                return new DailyReward(10, "Mốc 10 Ngày", "+1 Điểm SK, +3 Vé Quay, 1x Đèn", true);
            case 11:
                return new DailyReward(11, "3,000 Vàng", "+3000 Vàng", false);
            case 12:
                return new DailyReward(12, "25 Kim Cương", "+25 KC", false);
            case 13:
                return new DailyReward(13, "3x Kẹo Ma Thuật", "x3 Kẹo Trick-or-Treat", false);
            case 14:
                return new DailyReward(14, "3,500 Vàng", "+3500 Vàng", false);
            case 15:
                return new DailyReward(15, "Mốc 15 Ngày", "+2 Điểm SK, +1 Vé Cao Cấp, 2x Kẹo", true);
            case 16:
                return new DailyReward(16, "4,000 Vàng", "+4000 Vàng", false);
            case 17:
                return new DailyReward(17, "30 Kim Cương", "+30 KC", false);
            case 18:
                return new DailyReward(18, "2x Đèn Lồng Bí Ngô", "2x Đèn Lồng Bí Ngô", false);
            case 19:
                return new DailyReward(19, "4,500 Vàng", "+4500 Vàng", false);
            case 20:
                return new DailyReward(20, "Mốc 20 Ngày", "+2 Điểm SK, +2 Vé Cao Cấp, 2x Đèn", true);
            case 21:
                return new DailyReward(21, "5,000 Vàng", "+5000 Vàng", false);
            case 22:
                return new DailyReward(22, "35 Kim Cương", "+35 KC", false);
            case 23:
                return new DailyReward(23, "6,000 Vàng", "+6000 Vàng", false);
            case 24:
                return new DailyReward(24, "40 Kim Cương", "+40 KC", false);
            case 25:
                return new DailyReward(25, "Đại Mốc 25 Ngày", "+3 Điểm SK, +3 Vé CC, Cần Bí Ngô", true);
            default:
                return new DailyReward(day, "1,000 Vàng", "+1000 Vàng", false);
        }

    }

    public String claimDailyLoginReward() {
        checkAndResetDailyMissions();
        if (hasClaimedLoginToday())
            return null;
        long currentDayStart = getStartOfDay(TimeSyncService.getCurrentTimeMillis());
        player.setLastLoginTimestamp(currentDayStart);
        int currentDays = player.getTotalLoginDaysThisMonth();
        int nextDay = Math.min(25, currentDays + 1);
        player.setTotalLoginDaysThisMonth(nextDay);
        DailyReward r = getDailyReward(nextDay);

        if (isHalloweenActive()) {
            switch (nextDay) {
                case 1:
                    player.gainGold(1000);
                    break;
                case 2:
                    player.addDiamonds(15);
                    break;
                case 3:
                    player.addItem("Kẹo Trick-or-Treat", 2);
                    break;
                case 4:
                    player.gainGold(1500);
                    break;
                case 5:
                    player.addEventPoints(1);
                    player.addGachaTickets(2);
                    player.addItem("Kẹo Trick-or-Treat", 1);
                    break;
                case 6:
                    player.gainGold(2000);
                    break;
                case 7:
                    player.addDiamonds(20);
                    break;
                case 8:
                    player.addItem("Đèn Lồng Bí Ngô", 1);
                    break;
                case 9:
                    player.gainGold(2500);
                    break;
                case 10:
                    player.addEventPoints(1);
                    player.addGachaTickets(3);
                    player.addItem("Đèn Lồng Bí Ngô", 1);
                    break;
                case 11:
                    player.gainGold(3000);
                    break;
                case 12:
                    player.addDiamonds(25);
                    break;
                case 13:
                    player.addItem("Kẹo Trick-or-Treat", 3);
                    break;
                case 14:
                    player.gainGold(3500);
                    break;
                case 15:
                    player.addEventPoints(2);
                    player.addPremiumTickets(1);
                    player.addItem("Kẹo Trick-or-Treat", 2);
                    break;
                case 16:
                    player.gainGold(4000);
                    break;
                case 17:
                    player.addDiamonds(30);
                    break;
                case 18:
                    player.addItem("Đèn Lồng Bí Ngô", 2);
                    break;
                case 19:
                    player.gainGold(4500);
                    break;
                case 20:
                    player.addEventPoints(2);
                    player.addPremiumTickets(2);
                    player.addItem("Đèn Lồng Bí Ngô", 2);
                    break;
                case 21:
                    player.gainGold(5000);
                    break;
                case 22:
                    player.addDiamonds(35);
                    break;
                case 23:
                    player.gainGold(6000);
                    break;
                case 24:
                    player.addDiamonds(40);
                    break;
                case 25:
                    player.addEventPoints(3);
                    player.addPremiumTickets(3);
                    player.addEquipment(com.fishingrpg.game.entities.equipment.EquipmentGenerator
                            .createPumpkinCarnivalEquipment(com.fishingrpg.game.entities.equipment.EquipmentType.ROD));
                    break;
                default:
                    player.gainGold(1000);
                    break;
            }
        } else {
            switch (nextDay) {
                case 1:
                    player.gainGold(500);
                    break;
                case 2:
                    player.addDiamonds(10);
                    break;
                case 3:
                    player.addItem("Mồi Thơm", 3);
                    break;
                case 4:
                    player.gainGold(1000);
                    break;
                case 5:
                    player.addEventPoints(1);
                    player.addGachaTickets(1);
                    break;
                case 6:
                    player.gainGold(1200);
                    break;
                case 7:
                    player.addDiamonds(15);
                    break;
                case 8:
                    player.addItem("Bánh Mì", 3);
                    break;
                case 9:
                    player.gainGold(1500);
                    break;
                case 10:
                    player.addEventPoints(1);
                    player.addGachaTickets(2);
                    break;
                case 11:
                    player.gainGold(2000);
                    break;
                case 12:
                    player.addDiamonds(15);
                    break;
                case 13:
                    player.addItem("Mồi Rủi Ro", 5);
                    break;
                case 14:
                    player.gainGold(2500);
                    break;
                case 15:
                    player.addEventPoints(2);
                    player.addPremiumTickets(1);
                    break;
                case 16:
                    player.gainGold(3000);
                    break;
                case 17:
                    player.addDiamonds(20);
                    break;
                case 18:
                    player.gainGold(3500);
                    break;
                case 19:
                    player.addDiamonds(20);
                    break;
                case 20:
                    player.addEventPoints(2);
                    player.addPremiumTickets(2);
                    break;
                case 21:
                    player.gainGold(4000);
                    break;
                case 22:
                    player.addDiamonds(25);
                    break;
                case 23:
                    player.gainGold(5000);
                    break;
                case 24:
                    player.addDiamonds(30);
                    break;
                case 25:
                    player.addEventPoints(3);
                    player.addPremiumTickets(3);
                    player.addEquipment(com.fishingrpg.game.entities.equipment.EquipmentGenerator
                            .createFreeEventEquipment(com.fishingrpg.game.entities.equipment.EquipmentType.ROD));
                    break;
                default:
                    player.gainGold(1000);
                    break;
            }
        }

        return "Điểm danh Ngày " + nextDay + ": Nhận " + r.rewardSummary + "!";

    }

    public boolean hasNotification() {
        checkAndResetDailyMissions();

        if (!hasClaimedLoginToday() && player.getTotalLoginDaysThisMonth() < 25) {
            return true;
        }

        if (canClaimMilestone())
            return true;
        return false;

    }

    public String getEventTimeRemaining() {
        long now = TimeSyncService.getCurrentTimeMillis();
        EventPhase phase = getCurrentPhase();

        if (phase == EventPhase.LAC_HONG_ACTIVE) {
            long diff = EVENT_END_TIMESTAMP - now;
            long days = diff / (24 * 60 * 60 * 1000L);
            long hours = (diff % (24 * 60 * 60 * 1000L)) / (60 * 60 * 1000L);
            return days + " ngày " + hours + " giờ";
        } else if (phase == EventPhase.PRE_HALLOWEEN_INTERMISSION) {
            long remHlw = HALLOWEEN_START_TIMESTAMP - now;
            long d = remHlw / (24 * 60 * 60 * 1000L);
            long h = (remHlw % (24 * 60 * 60 * 1000L)) / (60 * 60 * 1000L);
            return "ĐÃ KẾT THÚC (Halloween sau: " + (d > 0 ? (d + "d ") : "") + h + "h)";
        } else if (phase == EventPhase.HALLOWEEN_ACTIVE) {
            long diff = HALLOWEEN_END_TIMESTAMP - now;
            long days = diff / (24 * 60 * 60 * 1000L);
            long hours = (diff % (24 * 60 * 60 * 1000L)) / (60 * 60 * 1000L);
            return days + " ngày " + hours + " giờ";
        } else {
            return "SỰ KIỆN KẾT THÚC";
        }

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
