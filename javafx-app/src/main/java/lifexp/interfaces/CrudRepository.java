package lifexp.interfaces;

import java.util.List;
import java.util.Optional;

/**
 * Generic CRUD interface for data access operations.
 * Provides standard database operations for any entity type.
 *
 * @param <T> The entity type
 * @param <ID> The ID type
 */
public interface CrudRepository<T, ID> {
    
    /**
     * Save an entity
     */
    T save(T entity);

    /**
     * Save multiple entities
     */
    List<T> saveAll(List<T> entities);

    /**
     * Find entity by ID
     */
    Optional<T> findById(ID id);

    /**
     * Find all entities
     */
    List<T> findAll();

    /**
     * Update an entity
     */
    T update(T entity);

    /**
     * Delete entity by ID
     */
    boolean deleteById(ID id);

    /**
     * Delete an entity
     */
    boolean delete(T entity);

    /**
     * Delete all entities
     */
    void deleteAll();

    /**
     * Count total entities
     */
    long count();

    /**
     * Check if entity exists by ID
     */
    boolean existsById(ID id);
}
