package lifexp.repositories;

import lifexp.models.XpLog;

/**
 * Repository for XP Log entity operations.
 * Extends generic CRUD functionality for xp log-specific operations.
 */
public class XpLogRepository extends GenericCrudRepository<XpLog, String> {
    
    public XpLogRepository() {
        super(XpLog::getId);
    }

    /**
     * Find all XP logs for a character
     */
    public java.util.List<XpLog> findByCharacterId(String characterId) {
        return findBy(x -> x.getCharacterId().equals(characterId));
    }

    /**
     * Find XP logs by source
     */
    public java.util.List<XpLog> findBySource(String source) {
        return findBy(x -> x.getSource().equalsIgnoreCase(source));
    }

    /**
     * Get total XP for a character
     */
    public long getTotalXpByCharacterId(String characterId) {
        return findByCharacterId(characterId)
                .stream()
                .mapToLong(XpLog::getXpAmount)
                .sum();
    }

    /**
     * Get XP from specific source
     */
    public long getXpByCharacterAndSource(String characterId, String source) {
        return findBy(x -> x.getCharacterId().equals(characterId) && x.getSource().equalsIgnoreCase(source))
                .stream()
                .mapToLong(XpLog::getXpAmount)
                .sum();
    }
}
