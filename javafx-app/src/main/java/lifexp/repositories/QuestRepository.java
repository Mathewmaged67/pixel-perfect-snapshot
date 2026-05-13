package lifexp.repositories;

import lifexp.models.Quest;

/**
 * Repository for Quest entity operations.
 * Extends generic CRUD functionality for quest-specific operations.
 */
public class QuestRepository extends GenericCrudRepository<Quest, String> {
    
    public QuestRepository() {
        super(Quest::getId);
    }

    /**
     * Find all quests for a character
     */
    public java.util.List<Quest> findByCharacterId(String characterId) {
        return findBy(q -> q.getCharacterId().equals(characterId));
    }

    /**
     * Find active quests for a character
     */
    public java.util.List<Quest> findActiveByCharacterId(String characterId) {
        return findBy(q -> q.getCharacterId().equals(characterId) && q.isAvailable());
    }

    /**
     * Find completed quests for a character
     */
    public java.util.List<Quest> findCompletedByCharacterId(String characterId) {
        return findBy(q -> q.getCharacterId().equals(characterId) && q.isCompleted());
    }

    /**
     * Find unclaimed completed quests
     */
    public java.util.List<Quest> findUnclaimedByCharacterId(String characterId) {
        return findBy(q -> q.getCharacterId().equals(characterId) && q.isCompleted() && !q.isClaimed());
    }

    /**
     * Get total XP from completed quests
     */
    public long getTotalXpByCharacterId(String characterId) {
        return findCompletedByCharacterId(characterId)
                .stream()
                .mapToLong(Quest::getXpReward)
                .sum();
    }
}
