package cz.muni.fi.pv168.data.storage.mapper;

/**
 * Map from one entity to another
 * We are using this mappers map between the business models and database entities
 *
 * @param <E> Type of DTO (database entity)
 * @param <M> Type of the Entity (business entity)
 */
public interface EntityMapper<E, M> {
    E mapToEntity(M model);

    M mapToModel(E entity);
}
