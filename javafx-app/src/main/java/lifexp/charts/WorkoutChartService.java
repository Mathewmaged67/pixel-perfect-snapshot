package lifexp.charts;

import java.util.*;

/**
 * Service for generating workout analytics charts.
 * Prepares data for workout statistics visualization.
 */
public class WorkoutChartService {
    
    /**
     * Generate workout frequency data
     */
    public static List<ChartDataPoint> generateWorkoutFrequencyData(Map<String, Integer> workoutCounts) {
        List<ChartDataPoint> data = new ArrayList<>();
        
        workoutCounts.forEach((workoutType, count) -> {
            data.add(new ChartDataPoint(workoutType, count));
        });
        
        return data;
    }

    /**
     * Generate calories burned data
     */
    public static List<ChartDataPoint> generateCaloriesData(List<Integer> dailyCalories) {
        List<ChartDataPoint> data = new ArrayList<>();
        
        for (int i = 0; i < dailyCalories.size(); i++) {
            data.add(new ChartDataPoint("Day " + (i + 1), dailyCalories.get(i)));
        }
        
        return data;
    }

    /**
     * Get workout statistics
     */
    public static Map<String, Object> getWorkoutStats(List<Integer> workoutDurations, List<Integer> workoutIntensities) {
        Map<String, Object> stats = new HashMap<>();
        
        if (workoutDurations.isEmpty()) {
            stats.put("avgDuration", 0);
            stats.put("totalDuration", 0);
            stats.put("avgIntensity", 0.0f);
            return stats;
        }
        
        int totalDuration = workoutDurations.stream().mapToInt(Integer::intValue).sum();
        int avgDuration = totalDuration / workoutDurations.size();
        float avgIntensity = (float) workoutIntensities.stream().mapToInt(Integer::intValue).average().orElse(0);
        
        stats.put("totalDuration", totalDuration);
        stats.put("avgDuration", avgDuration);
        stats.put("maxDuration", workoutDurations.stream().mapToInt(Integer::intValue).max().orElse(0));
        stats.put("avgIntensity", avgIntensity);
        stats.put("workoutCount", workoutDurations.size());
        
        return stats;
    }

    /**
     * Data point for charting
     */
    public static class ChartDataPoint {
        public String label;
        public long value;

        public ChartDataPoint(String label, long value) {
            this.label = label;
            this.value = value;
        }

        public String getLabel() { return label; }
        public long getValue() { return value; }
    }
}
