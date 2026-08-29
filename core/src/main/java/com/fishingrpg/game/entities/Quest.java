package com.fishingrpg.game.entities;

public class Quest {
    public enum Type {
        DAILY, WEEKLY, ACHIEVEMENT
    }

    public String id;
    public Type type;
    public String title;
    public String description;
    public int target;
    public int progress;
    public boolean claimed;

    // Daily/Weekly quests give points, Achievements give diamonds directly
    public int rewardPoints;
    public int rewardDiamonds;

    // For serialization
    public Quest() {}

    public Quest(String id, Type type, String title, String description, int target, int rewardPoints, int rewardDiamonds) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.target = target;
        this.progress = 0;
        this.claimed = false;
        this.rewardPoints = rewardPoints;
        this.rewardDiamonds = rewardDiamonds;
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
}
