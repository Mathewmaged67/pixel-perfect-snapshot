package lifexp.models;

import lifexp.enums.AchievementType;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents an achievement that a character can unlock.
 * Encapsulates achievement metadata and unlock status.
 */
public class Achievement {
    private String id;
    private String characterId;
    private String name;
    private String description;
    private AchievementType type;
    private String icon;
    private int rewardXp;
    private int milestone;
    private boolean unlocked;
    private LocalDateTime unlockedAt;
    private String condition;

    public Achievement(String id, String characterId, String name, String description, 
                      AchievementType type, int rewardXp, int milestone) {
        this.id = Objects.requireNonNull(id, "Achievement ID cannot be null");
        this.characterId = Objects.requireNonNull(characterId, "Character ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.description = Objects.requireNonNull(description, "Description cannot be null");
        this.type = Objects.requireNonNull(type, "Type cannot be null");
        this.rewardXp = rewardXp;
        this.milestone = milestone;
        this.unlocked = false;
    }

    // Unlock Management
    public void unlock() {
        if (!unlocked) {
            unlocked = true;
            unlockedAt = LocalDateTime.now();
        }
    }

    public boolean canUnlock(int currentValue) {
        return !unlocked && currentValue >= milestone;
    }

    public boolean requiresMilestone() {
        return milestone > 0;
    }

    // Setters
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    // Getters
    public String getId() { return id; }
    public String getCharacterId() { return characterId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public AchievementType getType() { return type; }
    public String getIcon() { return icon; }
    public int getRewardXp() { return rewardXp; }
    public int getMilestone() { return milestone; }
    public boolean isUnlocked() { return unlocked; }
    public LocalDateTime getUnlockedAt() { return unlockedAt; }
    public String getCondition() { return condition; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Achievement achievement = (Achievement) o;
        return Objects.equals(id, achievement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Achievement{id='%s', name='%s', type=%s, unlocked=%s}", 
            id, name, type.getDisplayName(), unlocked);
    }
}
