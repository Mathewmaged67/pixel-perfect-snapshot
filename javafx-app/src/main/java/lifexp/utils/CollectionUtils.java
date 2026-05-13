package lifexp.utils;

import java.util.Collection;
import java.util.List;

/**
 * Utility class for collection operations.
 */
public class CollectionUtils {
    
    /**
     * Check if collection is null or empty
     */
    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Check if collection is not empty
     */
    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }

    /**
     * Get element at index or null
     */
    public static <T> T getOrNull(List<T> list, int index) {
        if (isEmpty(list) || index < 0 || index >= list.size()) {
            return null;
        }
        return list.get(index);
    }

    /**
     * Find first element matching condition
     */
    public static <T> T findFirst(List<T> list, java.util.function.Predicate<T> predicate) {
        if (isEmpty(list)) return null;
        return list.stream().filter(predicate).findFirst().orElse(null);
    }

    /**
     * Filter collection by condition
     */
    public static <T> List<T> filter(Collection<T> collection, java.util.function.Predicate<T> predicate) {
        if (isEmpty(collection)) return List.of();
        return collection.stream().filter(predicate).toList();
    }

    /**
     * Sort collection and return as list
     */
    public static <T extends Comparable<T>> List<T> sorted(Collection<T> collection) {
        if (isEmpty(collection)) return List.of();
        return collection.stream().sorted().toList();
    }

    /**
     * Get maximum value from collection
     */
    public static <T extends Comparable<T>> T max(Collection<T> collection) {
        if (isEmpty(collection)) return null;
        return collection.stream().max(Comparable::compareTo).orElse(null);
    }

    /**
     * Get minimum value from collection
     */
    public static <T extends Comparable<T>> T min(Collection<T> collection) {
        if (isEmpty(collection)) return null;
        return collection.stream().min(Comparable::compareTo).orElse(null);
    }
}
