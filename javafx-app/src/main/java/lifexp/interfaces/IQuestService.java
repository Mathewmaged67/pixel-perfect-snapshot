package lifexp.interfaces;

import lifexp.models.Quest;
import java.util.List;

/**
 * Service interface for quest management operations.
 */
public interface IQuestService {
    
    /**
     * Create a new daily quest
     */
    Quest createDailyQuest(String characterId, String title, String description);

    /**
     * Get all active quests for a character
     */
    List<Quest> getActiveQuests(String characterId);

    /**
     * Get completed quests for a character
     */
    List<Quest> getCompletedQuests(String characterId);

    /**
     * Update quest progress
     */
    void updateQuestProgress(String questId, int progressAmount);

    /**
     * Complete a quest
     */
    void completeQuest(String questId);

    /**
     * Claim quest rewards
     */
    void claimRewards(String questId);

    /**
     * Get a quest by ID
     */
    Quest getQuestById(String questId);
}
