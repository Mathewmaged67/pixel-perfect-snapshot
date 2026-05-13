package lifexp.charts;

import java.util.*;

/**
 * Service for generating XP history analytics.
 * Prepares data for XP progression charts.
 */
public class XpChartService {
    
    /**
     * Generate XP progression data
     */
    public static List<ChartDataPoint> generateXpProgressionData(List<Long> dailyXpValues) {
        List<ChartDataPoint> data = new ArrayList<>();
        
        for (int i = 0; i < dailyXpValues.size(); i++) {
            data.add(new ChartDataPoint("Day " + (i + 1), dailyXpValues.get(i)));
        }
        
        return data;
    }

    /**
     * Generate cumulative XP data
     */
    public static List<ChartDataPoint> generateCumulativeXpData(List<Long> dailyXpValues) {
        List<ChartDataPoint> data = new ArrayList<>();
        long cumulative = 0;
        
        for (int i = 0; i < dailyXpValues.size(); i++) {
            cumulative += dailyXpValues.get(i);
            data.add(new ChartDataPoint("Day " + (i + 1), cumulative));
        }
        
        return data;
    }

    /**
     * Get XP statistics
     */
    public static Map<String, Object> getXpStats(List<Long> dailyXpValues) {
        Map<String, Object> stats = new HashMap<>();
        
        if (dailyXpValues.isEmpty()) {
            stats.put("average", 0L);
            stats.put("max", 0L);
            stats.put("min", 0L);
            stats.put("total", 0L);
            return stats;
        }
        
        long total = dailyXpValues.stream().mapToLong(Long::longValue).sum();
        long average = total / dailyXpValues.size();
        long max = dailyXpValues.stream().mapToLong(Long::longValue).max().orElse(0);
        long min = dailyXpValues.stream().mapToLong(Long::longValue).min().orElse(0);
        
        stats.put("total", total);
        stats.put("average", average);
        stats.put("max", max);
        stats.put("min", min);
        stats.put("count", dailyXpValues.size());
        
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
