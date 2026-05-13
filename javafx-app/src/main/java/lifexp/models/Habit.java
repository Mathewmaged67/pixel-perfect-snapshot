package lifexp.models;

import lifexp.enums.HabitCategory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a habit that a user can track and complete.
 * Encapsulates habit progress, streaks, and completion history.
 */
public class Habit {
    private String id;
    private String userId;
    private String name;
    private String description;
    private HabitCategory category;
    private int frequencyPerWeek;
    private LocalDateTime createdAt;
    private LocalDateTime lastCompletedAt;
    private int currentStreak;
    private int maxStreak;
    private int totalCompletions;
    private boolean active;
    private Set<LocalDate> completionDates;

    public Habit(String id, String userId, String name, HabitCategory category, int frequencyPerWeek) {
        this.id = Objects.requireNonNull(id, "Habit ID cannot be null");
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.name = Objects.requireNonNull(name, "Habit name cannot be null");
        this.category = Objects.requireNonNull(category, "Category cannot be null");
        this.frequencyPerWeek = Math.max(1, frequencyPerWeek);
        this.createdAt = LocalDateTime.now();
        this.currentStreak = 0;
        this.maxStreak = 0;
        this.totalCompletions = 0;
        this.active = true;
        this.completionDates = new HashSet<>();
    }

    // Completion Management
    public void markAsCompleted() {
        LocalDate today = LocalDate.now();
        if (!completionDates.contains(today)) {
            completionDates.add(today);
            totalCompletions++;
            lastCompletedAt = LocalDateTime.now();
            updateStreak();
        }
    }

    public boolean isCompletedToday() {
        return completionDates.contains(LocalDate.now());
    }

    public boolean wasCompletedOn(LocalDate date) {
        return completionDates.contains(date);
    }

    private void updateStreak() {
        LocalDate today = LocalDate.now();
        LocalDate checkDate = today.minusDays(1);
        int streak = 0;

        while (completionDates.contains(checkDate)) {
            streak++;
            checkDate = checkDate.minusDays(1);
        }

        currentStreak = streak + 1;
        maxStreak = Math.max(maxStreak, currentStreak);
    }

    public void resetStreak() {
        currentStreak = 0;
    }

    public void incrementStreak() {
        currentStreak++;
    }

    public int getCompletionPercentage() {
        if (totalCompletions == 0) return 0;
        int daysSinceCreation = (int) java.time.temporal.ChronoUnit.DAYS.between(createdAt.toLocalDate(), LocalDate.now());
        if (daysSinceCreation == 0) return 0;
        return Math.min(100, (totalCompletions * 100) / daysSinceCreation);
    }

    // Setters
    public void setDescription(String description) {
        this.description = description;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setFrequencyPerWeek(int frequencyPerWeek) {
        this.frequencyPerWeek = Math.max(1, frequencyPerWeek);
    }

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public HabitCategory getCategory() { return category; }
    public int getFrequencyPerWeek() { return frequencyPerWeek; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastCompletedAt() { return lastCompletedAt; }
    public int getCurrentStreak() { return currentStreak; }
    public int getMaxStreak() { return maxStreak; }
    public int getTotalCompletions() { return totalCompletions; }
    public boolean isActive() { return active; }
    public Set<LocalDate> getCompletionDates() { return new HashSet<>(completionDates); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Habit habit = (Habit) o;
        return Objects.equals(id, habit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Habit{id='%s', name='%s', category=%s, streak=%d, total=%d}", 
            id, name, category.getDisplayName(), currentStreak, totalCompletions);
    }
}
