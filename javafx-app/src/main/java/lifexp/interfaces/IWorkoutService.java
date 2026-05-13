package lifexp.interfaces;

import lifexp.models.Workout;
import java.util.List;

/**
 * Service interface for workout management operations.
 */
public interface IWorkoutService {
    
    /**
     * Create a new workout
     */
    Workout createWorkout(String userId, String name, String workoutType);

    /**
     * Get all workouts for a user
     */
    List<Workout> getUserWorkouts(String userId);

    /**
     * Complete a workout
     */
    void completeWorkout(String workoutId);

    /**
     * Get a workout by ID
     */
    Workout getWorkoutById(String workoutId);

    /**
     * Delete a workout
     */
    void deleteWorkout(String workoutId);

    /**
     * Get total XP earned from workouts
     */
    long getTotalWorkoutXp(String userId);
}
