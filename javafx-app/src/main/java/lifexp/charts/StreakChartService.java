package lifexp.charts;

import java.util.*;

/**
 * Service for generating streak analytics charts.
 * Prepares data for streak and consistency tracking visualization.
 */
public class StreakChartService {
    
    /**
     * Generate streak history data
     */
    public static List<ChartDataPoint> generateStreakHistoryData(List<Integer> streakValues) {
        List<ChartDataPoint> data = new ArrayList<>();
        
        for (int i = 0; i < streakValues.size(); i++) {
            data.add(new ChartDataPoint("Habit " + (i + 1), streakValues.get(i)));
        }
        
        return data;
    }

    /**
     * Generate consistency data (completion rates)
     */
    public static List<ChartDataPoint> generateConsistencyData(Map<String, Float> habitCompletionRates) {
        List<ChartDataPoint> data = new ArrayList<>();
        
        habitCompletionRates.forEach((habitName, rate) -> {
            data.add(new ChartDataPoint(habitName, (long) (rate * 100)));
        });
        
        return data;
    }

    /**
     * Get streak statistics
     */
    public static Map<String, Object> getStreakStats(List<Integer> streakValues) {
        Map<String, Object> stats = new HashMap<>();
        
        if (streakValues.isEmpty()) {
            stats.put("maxStreak", 0);
            stats.put("avgStreak", 0);
            stats.put("totalStreaks", 0);
            return stats;
        }
        
        int maxStreak = streakValues.stream().mapToInt(Integer::intValue).max().orElse(0);
        float avgStreak = (float) streakValues.stream().mapToInt(Integer::intValue).average().orElse(0);
        int activeStreaks = (int) streakValues.stream().filter(s -> s > 0).count();
        
        stats.put("maxStreak", maxStreak);
        stats.put("avgStreak", avgStreak);
        stats.put("activeStreaks", activeStreaks);
        stats.put("totalHabits", streakValues.size());
        
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
