package lifexp.enums;

/**
 * Represents different types of achievements for tracking progress milestones.
 */
public enum AchievementType {
    STREAK("Streak", "Maintain a habit streak"),
    LEVEL("Level", "Reach specific levels"),
    WORKOUT("Workout", "Complete workout milestones"),
    TOTAL_XP("Total XP", "Accumulate total XP"),
    HABIT_COMPLETION("Habit Completion", "Complete habits"),
    SOCIAL("Social", "Social milestones"),
    SPECIAL("Special", "Special achievements");

    private final String displayName;
    private final String description;

    AchievementType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
