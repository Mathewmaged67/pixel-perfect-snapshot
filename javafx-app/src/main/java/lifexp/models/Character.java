package lifexp.models;

import lifexp.enums.CharacterClass;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a character/avatar controlled by a user.
 * Encapsulates character progression, stats, and gameplay data.
 */
public class Character {
    private String id;
    private String userId;
    private String name;
    private CharacterClass characterClass;
    private int level;
    private long totalXp;
    private int currentHp;
    private int maxHp;
    private int currentMp;
    private int maxMp;
    private float strength;
    private float intelligence;
    private float agility;
    private long gold;
    private int currentStreak;
    private int maxStreak;
    private LocalDateTime createdAt;

    public Character(String id, String userId, String name, CharacterClass characterClass) {
        this.id = Objects.requireNonNull(id, "Character ID cannot be null");
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.name = Objects.requireNonNull(name, "Character name cannot be null");
        this.characterClass = Objects.requireNonNull(characterClass, "Character class cannot be null");
        this.level = 1;
        this.totalXp = 0L;
        this.maxHp = 100;
        this.currentHp = 100;
        this.maxMp = 50;
        this.currentMp = 50;
        this.strength = 10.0f * characterClass.getStrengthBonus();
        this.intelligence = 10.0f * characterClass.getIntelligenceBonus();
        this.agility = 10.0f * characterClass.getAgilityBonus();
        this.gold = 0L;
        this.currentStreak = 0;
        this.maxStreak = 0;
        this.createdAt = LocalDateTime.now();
    }

    // XP Management
    public void addXp(long xp) {
        if (xp <= 0) return;
        this.totalXp += xp;
        updateLevel();
    }

    private void updateLevel() {
        int newLevel = (int) (totalXp / 100);
        if (newLevel > level) {
            level = newLevel;
            onLevelUp();
        }
    }

    private void onLevelUp() {
        maxHp += 10;
        currentHp = maxHp;
        maxMp += 5;
        currentMp = maxMp;
    }

    // HP Management
    public void takeDamage(int damage) {
        currentHp = Math.max(0, currentHp - damage);
    }

    public void heal(int amount) {
        currentHp = Math.min(maxHp, currentHp + amount);
    }

    public boolean isAlive() {
        return currentHp > 0;
    }

    // MP Management
    public void useMp(int amount) {
        currentMp = Math.max(0, currentMp - amount);
    }

    public void restoreMp(int amount) {
        currentMp = Math.min(maxMp, currentMp + amount);
    }

    // Stat Management
    public void addStat(String statName, float amount) {
        switch (statName.toLowerCase()) {
            case "strength" -> strength += amount;
            case "intelligence" -> intelligence += amount;
            case "agility" -> agility += amount;
        }
    }

    // Streak Management
    public void incrementStreak() {
        currentStreak++;
        maxStreak = Math.max(maxStreak, currentStreak);
    }

    public void resetStreak() {
        currentStreak = 0;
    }

    // Gold Management
    public void addGold(long amount) {
        if (amount > 0) {
            gold += amount;
        }
    }

    public void spendGold(long amount) {
        if (amount > 0 && gold >= amount) {
            gold -= amount;
        }
    }

    public boolean canSpendGold(long amount) {
        return gold >= amount;
    }

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public CharacterClass getCharacterClass() { return characterClass; }
    public int getLevel() { return level; }
    public long getTotalXp() { return totalXp; }
    public int getCurrentHp() { return currentHp; }
    public int getMaxHp() { return maxHp; }
    public int getCurrentMp() { return currentMp; }
    public int getMaxMp() { return maxMp; }
    public float getStrength() { return strength; }
    public float getIntelligence() { return intelligence; }
    public float getAgility() { return agility; }
    public long getGold() { return gold; }
    public int getCurrentStreak() { return currentStreak; }
    public int getMaxStreak() { return maxStreak; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return Objects.equals(id, character.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Character{id='%s', name='%s', class=%s, level=%d, xp=%d}", 
            id, name, characterClass.getDisplayName(), level, totalXp);
    }
}
