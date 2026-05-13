package lifexp.utils;

import java.util.UUID;

/**
 * Utility class for ID generation.
 * Provides methods to generate unique identifiers.
 */
public class IdGenerator {
    
    /**
     * Generate a unique ID
     */
    public static String generateId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generate a prefixed unique ID
     */
    public static String generateId(String prefix) {
        return prefix + "_" + System.currentTimeMillis() + "_" + generateRandomSuffix();
    }

    /**
     * Generate a random suffix
     */
    private static String generateRandomSuffix() {
        return Integer.toHexString((int) (Math.random() * 0xFFFFFF));
    }

    /**
     * Generate a user ID
     */
    public static String generateUserId() {
        return generateId("usr");
    }

    /**
     * Generate a character ID
     */
    public static String generateCharacterId() {
        return generateId("char");
    }

    /**
     * Generate a habit ID
     */
    public static String generateHabitId() {
        return generateId("habit");
    }

    /**
     * Generate a workout ID
     */
    public static String generateWorkoutId() {
        return generateId("workout");
    }

    /**
     * Generate a quest ID
     */
    public static String generateQuestId() {
        return generateId("quest");
    }

    /**
     * Generate an achievement ID
     */
    public static String generateAchievementId() {
        return generateId("ach");
    }
}
