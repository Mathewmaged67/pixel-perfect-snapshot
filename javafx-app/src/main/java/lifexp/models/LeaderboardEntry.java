package lifexp.models;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a leaderboard entry showing character rankings.
 * Used for displaying global and friend rankings.
 */
public class LeaderboardEntry {
    private String characterId;
    private String characterName;
    private int rank;
    private int level;
    private long totalXp;
    private int totalHabitsCompleted;
    private int totalWorkoutsCompleted;
    private LocalDateTime lastUpdated;

    public LeaderboardEntry(String characterId, String characterName, int level, long totalXp) {
        this.characterId = Objects.requireNonNull(characterId, "Character ID cannot be null");
        this.characterName = Objects.requireNonNull(characterName, "Character name cannot be null");
        this.level = level;
        this.totalXp = totalXp;
        this.lastUpdated = LocalDateTime.now();
    }

    // Ranking Management
    public void setRank(int rank) {
        this.rank = rank;
    }

    public void updateStats(int level, long totalXp, int habitsCompleted, int workoutsCompleted) {
        this.level = level;
        this.totalXp = totalXp;
        this.totalHabitsCompleted = habitsCompleted;
        this.totalWorkoutsCompleted = workoutsCompleted;
        this.lastUpdated = LocalDateTime.now();
    }

    // Comparison for sorting
    public int compareByXp(LeaderboardEntry other) {
        return Long.compare(other.totalXp, this.totalXp);
    }

    public int compareByLevel(LeaderboardEntry other) {
        return Integer.compare(other.level, this.level);
    }

    // Getters
    public String getCharacterId() { return characterId; }
    public String getCharacterName() { return characterName; }
    public int getRank() { return rank; }
    public int getLevel() { return level; }
    public long getTotalXp() { return totalXp; }
    public int getTotalHabitsCompleted() { return totalHabitsCompleted; }
    public int getTotalWorkoutsCompleted() { return totalWorkoutsCompleted; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeaderboardEntry entry = (LeaderboardEntry) o;
        return Objects.equals(characterId, entry.characterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterId);
    }

    @Override
    public String toString() {
        return String.format("LeaderboardEntry{rank=%d, name='%s', level=%d, xp=%d}", 
            rank, characterName, level, totalXp);
    }
}
