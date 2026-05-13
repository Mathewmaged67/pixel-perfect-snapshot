package lifexp.enums;

/**
 * Represents quest difficulty levels with corresponding XP rewards.
 */
public enum QuestDifficulty {
    EASY(50, 1.0f),
    NORMAL(100, 1.5f),
    HARD(200, 2.0f),
    LEGENDARY(500, 3.0f);

    private final int baseXp;
    private final float rewardMultiplier;

    QuestDifficulty(int baseXp, float rewardMultiplier) {
        this.baseXp = baseXp;
        this.rewardMultiplier = rewardMultiplier;
    }

    public int getBaseXp() {
        return baseXp;
    }

    public float getRewardMultiplier() {
        return rewardMultiplier;
    }

    public int getTotalXp() {
        return (int) (baseXp * rewardMultiplier);
    }

    public String getDisplayName() {
        return switch (this) {
            case EASY -> "Easy";
            case NORMAL -> "Normal";
            case HARD -> "Hard";
            case LEGENDARY -> "Legendary";
        };
    }
}
