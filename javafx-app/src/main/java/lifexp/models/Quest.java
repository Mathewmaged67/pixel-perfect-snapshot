package lifexp.models;

import lifexp.enums.QuestDifficulty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a quest that a character can undertake for rewards.
 * Encapsulates quest objectives, completion tracking, and rewards.
 */
public class Quest {
    private String id;
    private String characterId;
    private String title;
    private String description;
    private QuestDifficulty difficulty;
    private String objective;
    private int progressRequired;
    private int currentProgress;
    private int xpReward;
    private int goldReward;
    private LocalDateTime createdAt;
    private LocalDate dueDate;
    private boolean completed;
    private boolean claimed;

    public Quest(String id, String characterId, String title, String description, QuestDifficulty difficulty) {
        this.id = Objects.requireNonNull(id, "Quest ID cannot be null");
        this.characterId = Objects.requireNonNull(characterId, "Character ID cannot be null");
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.description = Objects.requireNonNull(description, "Description cannot be null");
        this.difficulty = Objects.requireNonNull(difficulty, "Difficulty cannot be null");
        this.createdAt = LocalDateTime.now();
        this.dueDate = LocalDate.now().plusDays(1);
        this.xpReward = difficulty.getTotalXp();
        this.goldReward = difficulty.getBaseXp() / 2;
        this.progressRequired = 1;
        this.currentProgress = 0;
        this.completed = false;
        this.claimed = false;
    }

    // Progress Management
    public void updateProgress(int amount) {
        if (completed) return;
        currentProgress = Math.min(progressRequired, currentProgress + amount);
        if (currentProgress >= progressRequired) {
            markAsCompleted();
        }
    }

    public void markAsCompleted() {
        completed = true;
        currentProgress = progressRequired;
    }

    public void claimRewards() {
        if (!completed) {
            throw new IllegalStateException("Cannot claim rewards for incomplete quest");
        }
        claimed = true;
    }

    public float getCompletionPercentage() {
        if (progressRequired == 0) return 0;
        return (float) currentProgress / progressRequired * 100;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(dueDate);
    }

    public boolean isAvailable() {
        return !completed && !isExpired();
    }

    // Setters
    public void setObjective(String objective) {
        this.objective = objective;
    }

    public void setProgressRequired(int progressRequired) {
        this.progressRequired = Math.max(1, progressRequired);
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = Objects.requireNonNull(dueDate, "Due date cannot be null");
    }

    // Getters
    public String getId() { return id; }
    public String getCharacterId() { return characterId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public QuestDifficulty getDifficulty() { return difficulty; }
    public String getObjective() { return objective; }
    public int getProgressRequired() { return progressRequired; }
    public int getCurrentProgress() { return currentProgress; }
    public int getXpReward() { return xpReward; }
    public int getGoldReward() { return goldReward; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDate getDueDate() { return dueDate; }
    public boolean isCompleted() { return completed; }
    public boolean isClaimed() { return claimed; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quest quest = (Quest) o;
        return Objects.equals(id, quest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Quest{id='%s', title='%s', difficulty=%s, progress=%d/%d}", 
            id, title, difficulty.getDisplayName(), currentProgress, progressRequired);
    }
}
