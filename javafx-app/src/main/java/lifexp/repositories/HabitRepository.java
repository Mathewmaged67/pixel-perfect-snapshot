package lifexp.repositories;

import lifexp.models.Habit;

/**
 * Repository for Habit entity operations.
 * Extends generic CRUD functionality for habit-specific operations.
 */
public class HabitRepository extends GenericCrudRepository<Habit, String> {
    
    public HabitRepository() {
        super(Habit::getId);
    }

    /**
     * Find all habits for a user
     */
    public java.util.List<Habit> findByUserId(String userId) {
        return findBy(h -> h.getUserId().equals(userId));
    }

    /**
     * Find all active habits for a user
     */
    public java.util.List<Habit> findActiveByUserId(String userId) {
        return findBy(h -> h.getUserId().equals(userId) && h.isActive());
    }

    /**
     * Find habits by category
     */
    public java.util.List<Habit> findByCategory(String category) {
        return findBy(h -> h.getCategory().getDisplayName().equalsIgnoreCase(category));
    }

    /**
     * Get habits completed today by user
     */
    public java.util.List<Habit> findCompletedTodayByUserId(String userId) {
        return findBy(h -> h.getUserId().equals(userId) && h.isCompletedToday());
    }

    /**
     * Get high streak habits (streak > 7 days)
     */
    public java.util.List<Habit> findHighStreakHabits(String userId) {
        return findBy(h -> h.getUserId().equals(userId) && h.getCurrentStreak() > 7);
    }

    /**
     * Count habits for a user
     */
    public long countByUserId(String userId) {
        return countBy(h -> h.getUserId().equals(userId));
    }
}
