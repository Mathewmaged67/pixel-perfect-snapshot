package lifexp.networking;

/**
 * Protocol handler for LifeXP client-server communication.
 * Defines message formats and protocol rules.
 */
public class ProtocolHandler {
    
    private static final String MESSAGE_DELIMITER = "|";
    private static final String COMMAND_XP_UPDATE = "XP_UPDATE";
    private static final String COMMAND_GET_LEADERBOARD = "GET_LEADERBOARD";
    private static final String COMMAND_ACHIEVEMENT_UNLOCK = "ACHIEVEMENT_UNLOCK";
    private static final String COMMAND_SYNC_PROFILE = "SYNC_PROFILE";

    /**
     * Create XP update message
     */
    public static String createXpUpdateMessage(String characterId, long totalXp, int level) {
        return COMMAND_XP_UPDATE + MESSAGE_DELIMITER + characterId + MESSAGE_DELIMITER + 
               totalXp + MESSAGE_DELIMITER + level;
    }

    /**
     * Create leaderboard request message
     */
    public static String createLeaderboardRequest(int limit) {
        return COMMAND_GET_LEADERBOARD + MESSAGE_DELIMITER + limit;
    }

    /**
     * Create achievement unlock message
     */
    public static String createAchievementUnlockMessage(String characterId, String achievementId) {
        return COMMAND_ACHIEVEMENT_UNLOCK + MESSAGE_DELIMITER + characterId + MESSAGE_DELIMITER + achievementId;
    }

    /**
     * Create profile sync message
     */
    public static String createProfileSyncMessage(String characterId, String characterName, int level, long totalXp) {
        return COMMAND_SYNC_PROFILE + MESSAGE_DELIMITER + characterId + MESSAGE_DELIMITER + 
               characterName + MESSAGE_DELIMITER + level + MESSAGE_DELIMITER + totalXp;
    }

    /**
     * Parse message into components
     */
    public static String[] parseMessage(String message) {
        if (message == null || message.isEmpty()) {
            return new String[0];
        }
        return message.split("\\|");
    }

    /**
     * Get command from message
     */
    public static String getCommand(String message) {
        String[] parts = parseMessage(message);
        return parts.length > 0 ? parts[0] : "";
    }

    /**
     * Validate message format
     */
    public static boolean isValidMessage(String message) {
        return message != null && message.contains(MESSAGE_DELIMITER);
    }
}
