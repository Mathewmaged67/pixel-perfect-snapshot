package lifexp.logic;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Engine for streak and consistency tracking logic.
 * Manages streak calculation, maintenance, and rewards.
 */
public class StreakEngine {
    
    /**
     * Check if a streak should be maintained (completed yesterday and today)
     */
    public static boolean isStreakValid(LocalDate lastCompletionDate) {
        if (lastCompletionDate == null) return false;
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return lastCompletionDate.isEqual(yesterday) || lastCompletionDate.isEqual(LocalDate.now());
    }

    /**
     * Calculate days since last completion
     */
    public static long daysSinceCompletion(LocalDate lastCompletionDate) {
        if (lastCompletionDate == null) return Long.MAX_VALUE;
        return ChronoUnit.DAYS.between(lastCompletionDate, LocalDate.now());
    }

    /**
     * Check if streak is broken
     */
    public static boolean isStreakBroken(LocalDate lastCompletionDate) {
        if (lastCompletionDate == null) return true;
        long daysSince = daysSinceCompletion(lastCompletionDate);
        return daysSince >= 2;
    }

    /**
     * Get streak category (beginner, intermediate, advanced, expert)
     */
    public static String getStreakCategory(int streak) {
        if (streak < 7) return "BEGINNER";
        if (streak < 30) return "INTERMEDIATE";
        if (streak < 100) return "ADVANCED";
        return "EXPERT";
    }

    /**
     * Calculate streak multiplier (multiplicative bonus)
     */
    public static float calculateStreakMultiplier(int streak) {
        if (streak <= 0) return 1.0f;
        float multiplier = 1.0f + (streak * 0.05f);
        return Math.min(multiplier, 2.0f);
    }

    /**
     * Get streak bonus tier
     */
    public static int getStreakBonusTier(int streak) {
        if (streak < 3) return 0;
        if (streak < 7) return 1;
        if (streak < 14) return 2;
        if (streak < 30) return 3;
        return 4;
    }

    /**
     * Check if streak milestone is reached
     */
    public static boolean isMilestoneReached(int streak) {
        int[] milestones = {1, 3, 7, 14, 30, 60, 100, 365};
        for (int milestone : milestones) {
            if (streak == milestone) return true;
        }
        return false;
    }

    /**
     * Get next streak milestone
     */
    public static int getNextMilestone(int currentStreak) {
        int[] milestones = {1, 3, 7, 14, 30, 60, 100, 365};
        for (int milestone : milestones) {
            if (currentStreak < milestone) return milestone;
        }
        return 365 + ((currentStreak / 365 + 1) * 365);
    }

    /**
     * Get milestone name
     */
    public static String getMilestoneName(int streak) {
        return switch (streak) {
            case 1 -> "First Step";
            case 3 -> "On Fire";
            case 7 -> "Week Warrior";
            case 14 -> "Fortnight Force";
            case 30 -> "Monthly Master";
            case 60 -> "Seasonal Sage";
            case 100 -> "Century Champion";
            case 365 -> "Year Warrior";
            default -> "Streak Milestone";
        };
    }
}
