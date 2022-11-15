package cz.muni.fi.pv168.data.storage.repository;

import java.util.List;
import java.util.Optional;

/**
 * Represents a repository for any model
 *
 * @param <M> the type of the model
 */
public interface Repository<M> {

    int getSize();

    Optional<M> findById(long id);

    Optional<M> findByIndex(int index);

    List<M> findAll();

    void refresh();

    void create(M newEntity);

    void update(M entity);

    void deleteByIndex(int index);
}
