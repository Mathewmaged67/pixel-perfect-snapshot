package lifexp.interfaces;

import lifexp.models.Achievement;
import java.util.List;

/**
 * Service interface for achievement management operations.
 */
public interface IAchievementService {
    
    /**
     * Get all achievements for a character
     */
    List<Achievement> getCharacterAchievements(String characterId);

    /**
     * Get unlocked achievements for a character
     */
    List<Achievement> getUnlockedAchievements(String characterId);

    /**
     * Check and unlock achievements based on conditions
     */
    void checkAndUnlockAchievements(String characterId);

    /**
     * Unlock a specific achievement
     */
    void unlockAchievement(String achievementId);

    /**
     * Get an achievement by ID
     */
    Achievement getAchievementById(String achievementId);
}
