package lifexp.repositories;

import lifexp.models.Achievement;

/**
 * Repository for Achievement entity operations.
 * Extends generic CRUD functionality for achievement-specific operations.
 */
public class AchievementRepository extends GenericCrudRepository<Achievement, String> {
    
    public AchievementRepository() {
        super(Achievement::getId);
    }

    /**
     * Find all achievements for a character
     */
    public java.util.List<Achievement> findByCharacterId(String characterId) {
        return findBy(a -> a.getCharacterId().equals(characterId));
    }

    /**
     * Find unlocked achievements for a character
     */
    public java.util.List<Achievement> findUnlockedByCharacterId(String characterId) {
        return findBy(a -> a.getCharacterId().equals(characterId) && a.isUnlocked());
    }

    /**
     * Find locked achievements for a character
     */
    public java.util.List<Achievement> findLockedByCharacterId(String characterId) {
        return findBy(a -> a.getCharacterId().equals(characterId) && !a.isUnlocked());
    }

    /**
     * Find achievements by type
     */
    public java.util.List<Achievement> findByType(String type) {
        return findBy(a -> a.getType().getDisplayName().equalsIgnoreCase(type));
    }

    /**
     * Get total XP from unlocked achievements
     */
    public long getTotalXpByCharacterId(String characterId) {
        return findUnlockedByCharacterId(characterId)
                .stream()
                .mapToLong(Achievement::getRewardXp)
                .sum();
    }

    /**
     * Count unlocked achievements for a character
     */
    public long countUnlockedByCharacterId(String characterId) {
        return countBy(a -> a.getCharacterId().equals(characterId) && a.isUnlocked());
    }

    /**
     * Count all achievements for a character
     */
    public long countByCharacterId(String characterId) {
        return countBy(a -> a.getCharacterId().equals(characterId));
    }
}
