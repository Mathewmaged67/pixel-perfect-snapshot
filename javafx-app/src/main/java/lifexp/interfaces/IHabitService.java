package lifexp.interfaces;

import lifexp.models.Habit;
import java.util.List;

/**
 * Service interface for habit management operations.
 */
public interface IHabitService {
    
    /**
     * Create a new habit for a user
     */
    Habit createHabit(String userId, String name, String category, int frequencyPerWeek);

    /**
     * Get all habits for a user
     */
    List<Habit> getUserHabits(String userId);

    /**
     * Mark a habit as completed
     */
    void completeHabit(String habitId);

    /**
     * Get a habit by ID
     */
    Habit getHabitById(String habitId);

    /**
     * Delete a habit
     */
    void deleteHabit(String habitId);

    /**
     * Get active habits for a user
     */
    List<Habit> getActiveHabits(String userId);
}
