package lifexp.repositories;

import lifexp.models.LeaderboardEntry;

/**
 * Repository for Leaderboard Entry operations.
 * Extends generic CRUD functionality for leaderboard-specific operations.
 */
public class LeaderboardRepository extends GenericCrudRepository<LeaderboardEntry, String> {
    
    public LeaderboardRepository() {
        super(LeaderboardEntry::getCharacterId);
    }

    /**
     * Get top characters by XP
     */
    public java.util.List<LeaderboardEntry> getTopByXp(int limit) {
        return findAll().stream()
                .sorted((a, b) -> Long.compare(b.getTotalXp(), a.getTotalXp()))
                .limit(limit)
                .toList();
    }

    /**
     * Get top characters by level
     */
    public java.util.List<LeaderboardEntry> getTopByLevel(int limit) {
        return findAll().stream()
                .sorted((a, b) -> Integer.compare(b.getLevel(), a.getLevel()))
                .limit(limit)
                .toList();
    }

    /**
     * Find character rank by XP
     */
    public int findRankByXp(String characterId) {
        java.util.List<LeaderboardEntry> sorted = findAll().stream()
                .sorted((a, b) -> Long.compare(b.getTotalXp(), a.getTotalXp()))
                .toList();
        
        for (int i = 0; i < sorted.size(); i++) {
            if (sorted.get(i).getCharacterId().equals(characterId)) {
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * Find character rank by level
     */
    public int findRankByLevel(String characterId) {
        java.util.List<LeaderboardEntry> sorted = findAll().stream()
                .sorted((a, b) -> Integer.compare(b.getLevel(), a.getLevel()))
                .toList();
        
        for (int i = 0; i < sorted.size(); i++) {
            if (sorted.get(i).getCharacterId().equals(characterId)) {
                return i + 1;
            }
        }
        return -1;
    }
}
