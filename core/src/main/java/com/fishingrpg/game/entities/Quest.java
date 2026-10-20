package com.fishingrpg.game.entities;

public class Quest {
    public enum Type {
        DAILY, WEEKLY, ACHIEVEMENT, EVENT
    }

    public String id;
    public Type type;
    public String title;
    public String description;
    public int target;
    public int progress;
    public boolean claimed;

    // Daily/Weekly quests give points, Achievements give diamonds directly, Events can give Gold, Tickets, Items
    public int rewardPoints;
    public int rewardDiamonds;
    public int rewardGold;
    public String rewardItem;
    public int rewardItemCount;

    // For serialization
    public Quest() {}

    public Quest(String id, Type type, String title, String description, int target, int rewardPoints, int rewardDiamonds) {
        this(id, type, title, description, target, rewardPoints, rewardDiamonds, 0, null, 0);
    }

    public Quest(String id, Type type, String title, String description, int target, int rewardPoints, int rewardDiamonds, int rewardGold, String rewardItem, int rewardItemCount) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.target = target;
        this.progress = 0;
        this.claimed = false;
        this.rewardPoints = rewardPoints;
        this.rewardDiamonds = rewardDiamonds;
        this.rewardGold = rewardGold;
        this.rewardItem = rewardItem;
        this.rewardItemCount = rewardItemCount;
    }

    public void addProgress(int amount) {
        if (progress < target) {
            progress += amount;
            if (progress > target) progress = target;
        }
    }

    public boolean isCompleted() {
        return progress >= target;
    }

    public String getRewardSummary() {
        StringBuilder sb = new StringBuilder();
        if (rewardPoints > 0) sb.append("+").append(rewardPoints).append(" Điểm  ");
        if (rewardGold > 0) sb.append("+").append(String.format("%,d", rewardGold)).append(" Vàng  ");
        if (rewardDiamonds > 0) sb.append("+").append(rewardDiamonds).append(" KC  ");
        if (rewardItem != null && !rewardItem.isEmpty()) {
            if (rewardItemCount > 1) sb.append(rewardItemCount).append("x ").append(rewardItem);
            else sb.append("+1 ").append(rewardItem);
        }
        return sb.toString().trim();
    }
}
