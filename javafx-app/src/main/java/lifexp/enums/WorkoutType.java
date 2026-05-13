package lifexp.enums;

/**
 * Represents different types of workouts for categorization and XP calculation.
 */
public enum WorkoutType {
    STRENGTH("Strength", 1.2f),
    CARDIO("Cardio", 1.0f),
    FLEXIBILITY("Flexibility", 0.8f),
    ENDURANCE("Endurance", 1.1f),
    SPORTS("Sports", 1.3f),
    RECOVERY("Recovery", 0.5f);

    private final String displayName;
    private final float xpMultiplier;

    WorkoutType(String displayName, float xpMultiplier) {
        this.displayName = displayName;
        this.xpMultiplier = xpMultiplier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public float getXpMultiplier() {
        return xpMultiplier;
    }
}
