package cz.muni.fi.pv168.data.storage.repository;

import java.util.List;
import java.util.Optional;

/**
 * Represents a repository for any model
 *
 * @param <M> the type of the model
 */
public interface Repository<M> {

    public int getSize();

    public Optional<M> findById(long id);

    public Optional<M> findByIndex(int index);

    public List<M> findAll();

    public void refresh();

    public void create(M newEntity);

    public void update(M entity);

    public void deleteByIndex(int index);
}
