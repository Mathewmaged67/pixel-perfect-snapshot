package lifexp.logic;

/**
 * Engine for achievement unlock logic and milestone tracking.
 * Determines which achievements should be unlocked based on conditions.
 */
public class AchievementEngine {
    
    /**
     * Check if habit streak achievement should unlock
     */
    public static boolean shouldUnlockStreakAchievement(int streak) {
        int[] streakMilestones = {1, 3, 7, 14, 30, 60, 100, 365};
        for (int milestone : streakMilestones) {
            if (streak == milestone) return true;
        }
        return false;
    }

    /**
     * Check if level achievement should unlock
     */
    public static boolean shouldUnlockLevelAchievement(int level) {
        int[] levelMilestones = {5, 10, 25, 50, 100};
        for (int milestone : levelMilestones) {
            if (level == milestone) return true;
        }
        return false;
    }

    /**
     * Check if workout achievement should unlock
     */
    public static boolean shouldUnlockWorkoutAchievement(int workoutCount) {
        int[] workoutMilestones = {1, 10, 50, 100, 250, 500};
        for (int milestone : workoutMilestones) {
            if (workoutCount == milestone) return true;
        }
        return false;
    }

    /**
     * Check if total XP achievement should unlock
     */
    public static boolean shouldUnlockXpAchievement(long totalXp) {
        long[] xpMilestones = {1000, 5000, 10000, 50000, 100000, 500000};
        for (long milestone : xpMilestones) {
            if (totalXp == milestone) return true;
        }
        return false;
    }

    /**
     * Check if habit completion achievement should unlock
     */
    public static boolean shouldUnlockHabitAchievement(int habitCount) {
        int[] habitMilestones = {1, 5, 10, 20, 50};
        for (int milestone : habitMilestones) {
            if (habitCount == milestone) return true;
        }
        return false;
    }

    /**
     * Get achievement unlock percentage
     */
    public static float getUnlockPercentage(long currentValue, long targetValue) {
        if (targetValue == 0) return 0;
        return Math.min(100, (float) currentValue / targetValue * 100);
    }

    /**
     * Check if achievement is close to unlocking
     */
    public static boolean isCloseToUnlocking(long currentValue, long targetValue) {
        return getUnlockPercentage(currentValue, targetValue) >= 80;
    }
}
