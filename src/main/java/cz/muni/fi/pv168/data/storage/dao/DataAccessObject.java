package cz.muni.fi.pv168.data.storage.dao;

import cz.muni.fi.pv168.data.storage.DataStorageException;

import java.util.Collection;
import java.util.Optional;

/**
 * Generic interface for CRUD operations on entities.
 *
 * @param <E> type of the entity this DAO operates on
 */
public interface DataAccessObject<E> {

    /**
     * Creates a new entity using the underlying data source.
     *
     * @param entity entity to be persisted
     * @return Entity instance with set id
     * @throws IllegalArgumentException when the entity has already been persisted
     * @throws DataStorageException     when anything goes wrong with the underlying data source
     */
    E create(E entity);

    /**
     * Reads all entities from the underlying data source.
     *
     * @return collection of all entities known to the underlying data source
     * @throws DataStorageException when anything goes wrong with the underlying data source
     */
    Collection<E> findAll();

    /**
     * Finds entity by ID.
     *
     * @param id entity id
     * @return either empty if not found or the entity instance
     */
    Optional<E> findById(long id);

    /**
     * Updates an entity using the underlying data source.
     *
     * @param entity entity to be deleted
     * @throws IllegalArgumentException when the entity has not been persisted yet
     * @throws DataStorageException     when anything goes wrong with the underlying data source
     */
    E update(E entity);

    /**
     * Deletes an entity using the underlying data source.
     *
     * @param entityId entity id to be deleted
     * @throws IllegalArgumentException when the entity has not been persisted yet
     * @throws DataStorageException     when anything goes wrong with the underlying data source
     */
    void deleteById(long entityId);
}
