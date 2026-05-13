package lifexp.logic;

/**
 * Engine for XP calculation and progression logic.
 * Handles XP rewards, multipliers, and level progression formulas.
 */
public class XpCalculationEngine {
    
    private static final int XP_PER_LEVEL = 100;
    private static final float HABIT_COMPLETION_XP = 25.0f;
    private static final float WORKOUT_BASE_XP = 2.0f;
    private static final float STREAK_MULTIPLIER_PER_DAY = 0.05f;
    private static final float MAX_STREAK_MULTIPLIER = 2.0f;

    /**
     * Calculate level from total XP
     */
    public static int calculateLevel(long totalXp) {
        if (totalXp < 0) return 1;
        return (int) (totalXp / XP_PER_LEVEL) + 1;
    }

    /**
     * Calculate XP required to reach next level
     */
    public static long getXpForNextLevel(int currentLevel) {
        return (long) (currentLevel * XP_PER_LEVEL);
    }

    /**
     * Calculate XP required to reach a specific level
     */
    public static long getXpForLevel(int level) {
        if (level <= 1) return 0;
        return (long) ((level - 1) * XP_PER_LEVEL);
    }

    /**
     * Calculate XP for habit completion with streak bonus
     */
    public static long calculateHabitXp(int currentStreak) {
        float streakMultiplier = calculateStreakMultiplier(currentStreak);
        return Math.round(HABIT_COMPLETION_XP * streakMultiplier);
    }

    /**
     * Calculate XP for workout completion
     */
    public static long calculateWorkoutXp(int durationMinutes, int intensity, float workoutTypeMultiplier) {
        if (durationMinutes <= 0 || intensity <= 0) return 0;
        float baseXp = durationMinutes * WORKOUT_BASE_XP;
        float intensityBonus = baseXp * (intensity / 10.0f);
        return Math.round((baseXp + intensityBonus) * workoutTypeMultiplier);
    }

    /**
     * Calculate quest XP reward
     */
    public static long calculateQuestXp(int difficulty, boolean isBonus) {
        int baseXp = difficulty * 50;
        return isBonus ? Math.round(baseXp * 1.5f) : baseXp;
    }

    /**
     * Calculate streak multiplier (multiplicative bonus)
     */
    public static float calculateStreakMultiplier(int streak) {
        if (streak <= 0) return 1.0f;
        float multiplier = 1.0f + (streak * STREAK_MULTIPLIER_PER_DAY);
        return Math.min(multiplier, MAX_STREAK_MULTIPLIER);
    }

    /**
     * Apply multiple multipliers to XP
     */
    public static long applyMultipliers(long baseXp, float... multipliers) {
        float total = baseXp;
        for (float multiplier : multipliers) {
            if (multiplier > 0) {
                total *= multiplier;
            }
        }
        return Math.round(total);
    }

    /**
     * Calculate daily bonus XP (time-based rewards)
     */
    public static long calculateDailyBonusXp(int completedHabits, int completedQuests) {
        return (completedHabits * 10) + (completedQuests * 50);
    }

    /**
     * Calculate achievement reward XP
     */
    public static long calculateAchievementXp(String achievementType) {
        return switch (achievementType.toLowerCase()) {
            case "streak" -> 100L;
            case "level" -> 200L;
            case "workout" -> 150L;
            case "total_xp" -> 250L;
            case "habit_completion" -> 75L;
            case "social" -> 120L;
            case "special" -> 500L;
            default -> 50L;
        };
    }

    /**
     * Get XP required for next milestone
     */
    public static long getXpToNextMilestone(long currentXp, int milestoneInterval) {
        long currentMilestone = (currentXp / milestoneInterval) * milestoneInterval;
        long nextMilestone = currentMilestone + milestoneInterval;
        return nextMilestone - currentXp;
    }
}
