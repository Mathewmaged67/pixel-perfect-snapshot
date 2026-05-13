package lifexp.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for validation operations.
 */
public class ValidationUtils {
    
    /**
     * Check if string is null or empty
     */
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Check if string is not empty
     */
    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    /**
     * Validate username (3-20 alphanumeric characters)
     */
    public static boolean isValidUsername(String username) {
        if (isEmpty(username)) return false;
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    /**
     * Validate positive integer
     */
    public static boolean isPositive(int value) {
        return value > 0;
    }

    /**
     * Validate non-negative integer
     */
    public static boolean isNonNegative(int value) {
        return value >= 0;
    }

    /**
     * Validate range
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * Get validation errors for a value
     */
    public static List<String> validateUsername(String username) {
        List<String> errors = new ArrayList<>();
        if (isEmpty(username)) {
            errors.add("Username cannot be empty");
        } else if (username.length() < 3) {
            errors.add("Username must be at least 3 characters");
        } else if (username.length() > 20) {
            errors.add("Username must be at most 20 characters");
        } else if (!username.matches("^[a-zA-Z0-9_]+$")) {
            errors.add("Username can only contain letters, numbers, and underscores");
        }
        return errors;
    }

    /**
     * Get validation errors for an email
     */
    public static List<String> validateEmail(String email) {
        List<String> errors = new ArrayList<>();
        if (isEmpty(email)) {
            errors.add("Email cannot be empty");
        } else if (!isValidEmail(email)) {
            errors.add("Invalid email format");
        }
        return errors;
    }
}
