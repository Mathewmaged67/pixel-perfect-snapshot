package lifexp.repositories;

import lifexp.models.Character;

/**
 * Repository for Character entity operations.
 * Extends generic CRUD functionality for character-specific operations.
 */
public class CharacterRepository extends GenericCrudRepository<Character, String> {
    
    public CharacterRepository() {
        super(Character::getId);
    }

    /**
     * Find all characters for a user
     */
    public java.util.List<Character> findByUserId(String userId) {
        return findBy(c -> c.getUserId().equals(userId));
    }

    /**
     * Find character by name
     */
    public Character findByName(String name) {
        return findBy(c -> c.getName().equalsIgnoreCase(name))
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Get character with highest level for a user
     */
    public Character findHighestLevelCharacter(String userId) {
        return findBy(c -> c.getUserId().equals(userId))
                .stream()
                .max((c1, c2) -> Integer.compare(c1.getLevel(), c2.getLevel()))
                .orElse(null);
    }

    /**
     * Count characters for a user
     */
    public long countByUserId(String userId) {
        return countBy(c -> c.getUserId().equals(userId));
    }
}
