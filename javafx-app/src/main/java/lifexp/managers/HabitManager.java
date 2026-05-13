package lifexp.managers;

import lifexp.models.*;
import lifexp.repositories.*;
import lifexp.logic.*;
import lifexp.enums.*;
import java.time.LocalDate;

/**
 * Manager for habit tracking and completion operations.
 * Handles habit creation, completion, streak tracking, and progress.
 */
public class HabitManager {
    private HabitRepository habitRepository;
    private XpManager xpManager;

    public HabitManager(HabitRepository habitRepository, XpManager xpManager) {
        this.habitRepository = habitRepository;
        this.xpManager = xpManager;
    }

    /**
     * Create a new habit for a user
     */
    public Habit createHabit(String userId, String name, HabitCategory category, int frequencyPerWeek) {
        String habitId = "habit_" + System.currentTimeMillis();
        Habit habit = new Habit(habitId, userId, name, category, frequencyPerWeek);
        return habitRepository.save(habit);
    }

    /**
     * Mark a habit as completed
     */
    public void completeHabit(String habitId, String characterId) {
        Habit habit = habitRepository.findById(habitId).orElse(null);
        if (habit == null || !habit.isActive()) return;

        boolean wasCompletedToday = habit.isCompletedToday();
        habit.markAsCompleted();

        if (!wasCompletedToday) {
            habit.incrementStreak();
            // Award XP for habit completion
            long xpReward = XpCalculationEngine.calculateHabitXp(habit.getCurrentStreak());
            xpManager.addXp(characterId, xpReward, "Habit: " + habit.getName());
        }

        habitRepository.update(habit);
    }

    /**
     * Get all habits for a user
     */
    public java.util.List<Habit> getUserHabits(String userId) {
        return habitRepository.findByUserId(userId);
    }

    /**
     * Get active habits for a user
     */
    public java.util.List<Habit> getActiveHabits(String userId) {
        return habitRepository.findActiveByUserId(userId);
    }

    /**
     * Get habits completed today
     */
    public java.util.List<Habit> getCompletedToday(String userId) {
        return habitRepository.findCompletedTodayByUserId(userId);
    }

    /**
     * Get high streak habits
     */
    public java.util.List<Habit> getHighStreakHabits(String userId) {
        return habitRepository.findHighStreakHabits(userId);
    }

    /**
     * Update habit frequency
     */
    public void updateFrequency(String habitId, int newFrequency) {
        Habit habit = habitRepository.findById(habitId).orElse(null);
        if (habit != null) {
            habit.setFrequencyPerWeek(newFrequency);
            habitRepository.update(habit);
        }
    }

    /**
     * Delete a habit
     */
    public void deleteHabit(String habitId) {
        habitRepository.deleteById(habitId);
    }

    /**
     * Get habit statistics
     */
    public java.util.Map<String, Object> getHabitStats(String userId) {
        java.util.List<Habit> habits = getUserHabits(userId);
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        
        stats.put("totalHabits", habits.size());
        stats.put("activeHabits", getActiveHabits(userId).size());
        stats.put("completedToday", getCompletedToday(userId).size());
        
        long totalCompletions = habits.stream().mapToLong(Habit::getTotalCompletions).sum();
        stats.put("totalCompletions", totalCompletions);
        
        int maxStreak = habits.stream().mapToInt(Habit::getMaxStreak).max().orElse(0);
        stats.put("maxStreak", maxStreak);
        
        return stats;
    }
}
