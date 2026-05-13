package lifexp.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for date and time formatting operations.
 */
public class DateTimeUtils {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Format LocalDate to string
     */
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "";
    }

    /**
     * Format LocalDateTime to string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATETIME_FORMATTER) : "";
    }

    /**
     * Format LocalDateTime to time string
     */
    public static String formatTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(TIME_FORMATTER) : "";
    }

    /**
     * Get days between two dates
     */
    public static long daysBetween(LocalDate from, LocalDate to) {
        return java.time.temporal.ChronoUnit.DAYS.between(from, to);
    }

    /**
     * Check if date is today
     */
    public static boolean isToday(LocalDate date) {
        return date.isEqual(LocalDate.now());
    }

    /**
     * Check if date is yesterday
     */
    public static boolean isYesterday(LocalDate date) {
        return date.isEqual(LocalDate.now().minusDays(1));
    }

    /**
     * Get formatted relative time (e.g., "2 hours ago")
     */
    public static String getRelativeTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        
        LocalDateTime now = LocalDateTime.now();
        long minutes = java.time.temporal.ChronoUnit.MINUTES.between(dateTime, now);
        
        if (minutes < 1) return "just now";
        if (minutes < 60) return minutes + " minutes ago";
        
        long hours = java.time.temporal.ChronoUnit.HOURS.between(dateTime, now);
        if (hours < 24) return hours + " hours ago";
        
        long days = java.time.temporal.ChronoUnit.DAYS.between(dateTime, now);
        if (days < 7) return days + " days ago";
        
        return formatDate(dateTime.toLocalDate());
    }
}
