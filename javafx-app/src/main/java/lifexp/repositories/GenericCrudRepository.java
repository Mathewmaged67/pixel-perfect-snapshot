package lifexp.repositories;

import lifexp.interfaces.CrudRepository;
import java.util.*;

/**
 * Generic CRUD repository implementation using in-memory ArrayList storage.
 * Provides reusable CRUD operations for all entity types.
 * Database integration can be added by extending this class.
 *
 * @param <T> The entity type
 * @param <ID> The ID type
 */
public class GenericCrudRepository<T, ID> implements CrudRepository<T, ID> {
    protected List<T> storage = new ArrayList<>();
    protected java.util.function.Function<T, ID> idExtractor;

    public GenericCrudRepository(java.util.function.Function<T, ID> idExtractor) {
        this.idExtractor = Objects.requireNonNull(idExtractor, "ID extractor cannot be null");
    }

    @Override
    public T save(T entity) {
        Objects.requireNonNull(entity, "Entity cannot be null");
        if (!storage.contains(entity)) {
            storage.add(entity);
        }
        return entity;
    }

    @Override
    public List<T> saveAll(List<T> entities) {
        Objects.requireNonNull(entities, "Entities list cannot be null");
        for (T entity : entities) {
            save(entity);
        }
        return entities;
    }

    @Override
    public Optional<T> findById(ID id) {
        Objects.requireNonNull(id, "ID cannot be null");
        return storage.stream()
                .filter(e -> id.equals(idExtractor.apply(e)))
                .findFirst();
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage);
    }

    @Override
    public T update(T entity) {
        Objects.requireNonNull(entity, "Entity cannot be null");
        ID id = idExtractor.apply(entity);
        int index = -1;
        for (int i = 0; i < storage.size(); i++) {
            if (id.equals(idExtractor.apply(storage.get(i)))) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            storage.set(index, entity);
        }
        return entity;
    }

    @Override
    public boolean deleteById(ID id) {
        Objects.requireNonNull(id, "ID cannot be null");
        return storage.removeIf(e -> id.equals(idExtractor.apply(e)));
    }

    @Override
    public boolean delete(T entity) {
        Objects.requireNonNull(entity, "Entity cannot be null");
        return storage.remove(entity);
    }

    @Override
    public void deleteAll() {
        storage.clear();
    }

    @Override
    public long count() {
        return storage.size();
    }

    @Override
    public boolean existsById(ID id) {
        Objects.requireNonNull(id, "ID cannot be null");
        return storage.stream()
                .anyMatch(e -> id.equals(idExtractor.apply(e)));
    }

    /**
     * Find entities by a predicate
     */
    public List<T> findBy(java.util.function.Predicate<T> predicate) {
        return storage.stream()
                .filter(predicate)
                .toList();
    }

    /**
     * Get total count of entities matching a predicate
     */
    public long countBy(java.util.function.Predicate<T> predicate) {
        return storage.stream()
                .filter(predicate)
                .count();
    }
}
