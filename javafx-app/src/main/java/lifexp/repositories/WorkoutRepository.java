package lifexp.repositories;

import lifexp.models.Workout;

/**
 * Repository for Workout entity operations.
 * Extends generic CRUD functionality for workout-specific operations.
 */
public class WorkoutRepository extends GenericCrudRepository<Workout, String> {
    
    public WorkoutRepository() {
        super(Workout::getId);
    }

    /**
     * Find all workouts for a user
     */
    public java.util.List<Workout> findByUserId(String userId) {
        return findBy(w -> w.getUserId().equals(userId));
    }

    /**
     * Find completed workouts for a user
     */
    public java.util.List<Workout> findCompletedByUserId(String userId) {
        return findBy(w -> w.getUserId().equals(userId) && w.isCompleted());
    }

    /**
     * Find workouts by type
     */
    public java.util.List<Workout> findByType(String workoutType) {
        return findBy(w -> w.getType().getDisplayName().equalsIgnoreCase(workoutType));
    }

    /**
     * Get total XP earned by user from workouts
     */
    public long getTotalXpByUserId(String userId) {
        return findBy(w -> w.getUserId().equals(userId) && w.isCompleted())
                .stream()
                .mapToLong(Workout::getXpReward)
                .sum();
    }

    /**
     * Get total calories burned by user
     */
    public int getTotalCaloriesByUserId(String userId) {
        return (int) findBy(w -> w.getUserId().equals(userId) && w.isCompleted())
                .stream()
                .mapToLong(Workout::getCaloriesBurned)
                .sum();
    }

    /**
     * Count completed workouts for a user
     */
    public long countCompletedByUserId(String userId) {
        return countBy(w -> w.getUserId().equals(userId) && w.isCompleted());
    }
}
