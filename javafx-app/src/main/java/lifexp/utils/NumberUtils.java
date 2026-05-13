package lifexp.utils;

/**
 * Utility class for number formatting and calculations.
 */
public class NumberUtils {
    
    /**
     * Format large numbers with suffixes (K, M, B)
     */
    public static String formatLargeNumber(long number) {
        if (number < 1000) return String.valueOf(number);
        
        int unitIndex = 0;
        double value = number;
        String[] units = {"", "K", "M", "B", "T"};
        
        while (value >= 1000 && unitIndex < units.length - 1) {
            value /= 1000;
            unitIndex++;
        }
        
        return String.format("%.1f%s", value, units[unitIndex]);
    }

    /**
     * Clamp a value between min and max
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Clamp a value between min and max (float)
     */
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Get percentage of value relative to total
     */
    public static float getPercentage(long value, long total) {
        if (total == 0) return 0;
        return (float) value / total * 100;
    }

    /**
     * Format number as percentage
     */
    public static String formatPercentage(float percentage) {
        return String.format("%.1f%%", percentage);
    }

    /**
     * Round to nearest integer
     */
    public static int round(float value) {
        return Math.round(value);
    }

    /**
     * Format number with thousand separators
     */
    public static String formatWithSeparators(long number) {
        return String.format("%,d", number);
    }
}
