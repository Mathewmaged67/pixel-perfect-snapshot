package lifexp.app;

import lifexp.enums.CharacterClass;
import lifexp.enums.AchievementType;
import lifexp.models.Achievement;
import lifexp.models.Character;
import lifexp.models.User;
import lifexp.utils.IdGenerator;

/**
 * LifeXP Application - Professional gamified productivity and fitness desktop application.
 * 
 * Main entry point for the LifeXP backend system.
 * Initializes all systems and provides access to core functionality.
 */
public class LifeXPApplication {
    
    private static AppContext appContext;
    private static LifeXPApplication instance;

    private LifeXPApplication() {
        appContext = new AppContext();
    }

    /**
     * Get singleton instance
     */
    public static LifeXPApplication getInstance() {
        if (instance == null) {
            instance = new LifeXPApplication();
        }
        return instance;
    }

    /**
     * Get application context
     */
    public AppContext getAppContext() {
        return appContext;
    }

    /**
     * Create a new user and character
     */
    public Character createNewUser(String username, String email, String characterName, CharacterClass characterClass) {
        // Create user
        String userId = IdGenerator.generateUserId();
        User user = new User(userId, username, email);
        appContext.getUserRepository().save(user);

        // Create character
        String characterId = IdGenerator.generateCharacterId();
        Character character = new Character(characterId, userId, characterName, characterClass);
        appContext.getCharacterRepository().save(character);

        return character;
    }

    /**
     * Initialize default achievements for a character
     */
    public void initializeAchievements(String characterId) {
        // Create default achievement set
        int[] streamMilestones = {1, 3, 7, 14, 30, 60, 100, 365};
        for (int milestone : streamMilestones) {
            String achId = "ach_streak_" + milestone;
            Achievement achievement = new Achievement(
                    achId,
                    characterId,
                    "Streak: " + milestone + " Days",
                    "Maintain a " + milestone + " day streak",
                    AchievementType.STREAK,
                    100 * milestone,
                    milestone
            );
            appContext.getAchievementRepository().save(achievement);
        }

        // Level achievements
        int[] levelMilestones = {5, 10, 25, 50, 100};
        for (int level : levelMilestones) {
            String achId = "ach_level_" + level;
            Achievement achievement = new Achievement(
                    achId,
                    characterId,
                    "Level " + level,
                    "Reach level " + level,
                    AchievementType.LEVEL,
                    500 * level,
                    level
            );
            appContext.getAchievementRepository().save(achievement);
        }
    }

    /**
     * Get system statistics
     */
    public java.util.Map<String, Object> getSystemStats() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        
        stats.put("totalUsers", appContext.getUserRepository().count());
        stats.put("totalCharacters", appContext.getCharacterRepository().count());
        stats.put("totalHabits", appContext.getHabitRepository().count());
        stats.put("totalWorkouts", appContext.getWorkoutRepository().count());
        stats.put("totalQuests", appContext.getQuestRepository().count());
        stats.put("totalAchievements", appContext.getAchievementRepository().count());
        
        return stats;
    }

    /**
     * Shutdown the application
     */
    public void shutdown() {
        appContext.shutdown();
        System.out.println("LifeXP Application shutdown complete");
    }

    /**
     * Print application info
     */
    public static void printApplicationInfo() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         LifeXP - Backend System        ║");
        System.out.println("║   Gamified Productivity & Fitness      ║");
        System.out.println("║              Version 1.0.0             ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Core Systems:");
        System.out.println("  ✓ XP & Level Management");
        System.out.println("  ✓ Habit Tracking");
        System.out.println("  ✓ Workout Management");
        System.out.println("  ✓ Quest System");
        System.out.println("  ✓ Achievement System");
        System.out.println("  ✓ Leaderboard System");
        System.out.println("  ✓ Background Tasks");
        System.out.println("  ✓ Analytics & Charts");
        System.out.println();
    }
}
