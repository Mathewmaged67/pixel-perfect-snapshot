package lifexp.interfaces;

/**
 * Service interface for managing XP and experience points.
 */
public interface IXpService {
    
    /**
     * Add XP to a character
     */
    void addXp(String characterId, long amount, String source);

    /**
     * Get total XP for a character
     */
    long getTotalXp(String characterId);

    /**
     * Calculate current level based on XP
     */
    int calculateLevel(long totalXp);

    /**
     * Apply streak multiplier to XP reward
     */
    long applyStreakMultiplier(long baseXp, int streak);

    /**
     * Get XP required for next level
     */
    long getXpForNextLevel(int currentLevel);
}
