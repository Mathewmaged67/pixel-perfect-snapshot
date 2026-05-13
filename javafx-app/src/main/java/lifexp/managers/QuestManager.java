package lifexp.managers;

import lifexp.models.*;
import lifexp.repositories.*;
import lifexp.logic.*;
import lifexp.enums.*;

/**
 * Manager for quest operations.
 * Handles quest creation, completion, progress tracking, and rewards.
 */
public class QuestManager {
    private QuestRepository questRepository;
    private XpManager xpManager;

    public QuestManager(QuestRepository questRepository, XpManager xpManager) {
        this.questRepository = questRepository;
        this.xpManager = xpManager;
    }

    /**
     * Create a new daily quest
     */
    public Quest createDailyQuest(String characterId, String title, String description, QuestDifficulty difficulty) {
        String questId = "quest_" + System.currentTimeMillis();
        Quest quest = new Quest(questId, characterId, title, description, difficulty);
        return questRepository.save(quest);
    }

    /**
     * Update quest progress
     */
    public void updateProgress(String questId, int amount) {
        Quest quest = questRepository.findById(questId).orElse(null);
        if (quest != null && quest.isAvailable()) {
            quest.updateProgress(amount);
            questRepository.update(quest);
        }
    }

    /**
     * Complete a quest
     */
    public void completeQuest(String questId) {
        Quest quest = questRepository.findById(questId).orElse(null);
        if (quest != null) {
            quest.markAsCompleted();
            questRepository.update(quest);
        }
    }

    /**
     * Claim quest rewards
     */
    public void claimRewards(String questId, String characterId) {
        Quest quest = questRepository.findById(questId).orElse(null);
        if (quest == null || !quest.isCompleted()) return;

        quest.claimRewards();
        xpManager.addXp(characterId, quest.getXpReward(), "Quest: " + quest.getTitle());
        questRepository.update(quest);
    }

    /**
     * Get active quests for a character
     */
    public java.util.List<Quest> getActiveQuests(String characterId) {
        return questRepository.findActiveByCharacterId(characterId);
    }

    /**
     * Get completed quests for a character
     */
    public java.util.List<Quest> getCompletedQuests(String characterId) {
        return questRepository.findCompletedByCharacterId(characterId);
    }

    /**
     * Get unclaimed rewards
     */
    public java.util.List<Quest> getUnclaimedRewards(String characterId) {
        return questRepository.findUnclaimedByCharacterId(characterId);
    }

    /**
     * Get daily quests for a character
     */
    public java.util.List<Quest> getDailyQuests(String characterId) {
        return getActiveQuests(characterId).stream()
                .filter(q -> java.time.temporal.ChronoUnit.DAYS.between(
                        q.getCreatedAt().toLocalDate(),
                        java.time.LocalDate.now()) == 0)
                .toList();
    }

    /**
     * Get total XP from completed quests
     */
    public long getTotalQuestXp(String characterId) {
        return questRepository.getTotalXpByCharacterId(characterId);
    }

    /**
     * Delete a quest
     */
    public void deleteQuest(String questId) {
        questRepository.deleteById(questId);
    }

    /**
     * Get quest statistics
     */
    public java.util.Map<String, Object> getQuestStats(String characterId) {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        java.util.List<Quest> active = getActiveQuests(characterId);
        java.util.List<Quest> completed = getCompletedQuests(characterId);
        java.util.List<Quest> unclaimed = getUnclaimedRewards(characterId);

        stats.put("activeQuests", active.size());
        stats.put("completedQuests", completed.size());
        stats.put("unclaimedRewards", unclaimed.size());
        stats.put("totalXp", getTotalQuestXp(characterId));

        long unclaimedXp = unclaimed.stream().mapToLong(Quest::getXpReward).sum();
        stats.put("unclaimedXp", unclaimedXp);

        return stats;
    }
}
