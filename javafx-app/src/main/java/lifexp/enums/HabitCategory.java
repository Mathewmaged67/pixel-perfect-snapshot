package lifexp.enums;

/**
 * Represents different categories of habits for organization and filtering.
 */
public enum HabitCategory {
    HEALTH("Health"),
    PRODUCTIVITY("Productivity"),
    LEARNING("Learning"),
    WELLNESS("Wellness"),
    FINANCIAL("Financial"),
    PERSONAL("Personal"),
    SOCIAL("Social"),
    CREATIVITY("Creativity");

    private final String displayName;

    HabitCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
