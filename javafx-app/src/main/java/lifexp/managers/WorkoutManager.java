package lifexp.managers;

import lifexp.models.*;
import lifexp.repositories.*;
import lifexp.logic.*;
import lifexp.enums.*;

/**
 * Manager for workout tracking and completion operations.
 * Handles workout creation, completion, and XP rewards.
 */
public class WorkoutManager {
    private WorkoutRepository workoutRepository;
    private XpManager xpManager;

    public WorkoutManager(WorkoutRepository workoutRepository, XpManager xpManager) {
        this.workoutRepository = workoutRepository;
        this.xpManager = xpManager;
    }

    /**
     * Create a new workout
     */
    public Workout createWorkout(String userId, String name, WorkoutType type) {
        String workoutId = "workout_" + System.currentTimeMillis();
        Workout workout = new Workout(workoutId, userId, name, type);
        return workoutRepository.save(workout);
    }

    /**
     * Complete a workout and award XP
     */
    public void completeWorkout(String workoutId, String characterId, int intensity) {
        Workout workout = workoutRepository.findById(workoutId).orElse(null);
        if (workout == null) return;

        workout.setIntensity(intensity);
        workout.complete();

        // Award XP for workout
        long xpReward = XpCalculationEngine.calculateWorkoutXp(
                workout.getDurationMinutes(),
                intensity,
                workout.getType().getXpMultiplier()
        );
        xpManager.addXp(characterId, xpReward, "Workout: " + workout.getName());

        workoutRepository.update(workout);
    }

    /**
     * Get all workouts for a user
     */
    public java.util.List<Workout> getUserWorkouts(String userId) {
        return workoutRepository.findByUserId(userId);
    }

    /**
     * Get completed workouts for a user
     */
    public java.util.List<Workout> getCompletedWorkouts(String userId) {
        return workoutRepository.findCompletedByUserId(userId);
    }

    /**
     * Get workouts by type
     */
    public java.util.List<Workout> getWorkoutsByType(WorkoutType type) {
        return workoutRepository.findByType(type.getDisplayName());
    }

    /**
     * Get total calories burned by user
     */
    public int getTotalCaloriesBurned(String userId) {
        return workoutRepository.getTotalCaloriesByUserId(userId);
    }

    /**
     * Get total XP earned from workouts
     */
    public long getTotalWorkoutXp(String userId) {
        return workoutRepository.getTotalXpByUserId(userId);
    }

    /**
     * Delete a workout
     */
    public void deleteWorkout(String workoutId) {
        workoutRepository.deleteById(workoutId);
    }

    /**
     * Get workout statistics
     */
    public java.util.Map<String, Object> getWorkoutStats(String userId) {
        java.util.List<Workout> workouts = getUserWorkouts(userId);
        java.util.List<Workout> completed = getCompletedWorkouts(userId);
        java.util.Map<String, Object> stats = new java.util.HashMap<>();

        stats.put("totalWorkouts", workouts.size());
        stats.put("completedWorkouts", completed.size());
        stats.put("totalCalories", getTotalCaloriesBurned(userId));
        stats.put("totalXp", getTotalWorkoutXp(userId));

        int totalMinutes = completed.stream().mapToInt(Workout::getDurationMinutes).sum();
        stats.put("totalMinutes", totalMinutes);

        return stats;
    }

    /**
     * Get average workout intensity
     */
    public float getAverageIntensity(String userId) {
        java.util.List<Workout> completed = getCompletedWorkouts(userId);
        if (completed.isEmpty()) return 0;
        return (float) completed.stream().mapToInt(Workout::getIntensity).average().orElse(0);
    }
}
