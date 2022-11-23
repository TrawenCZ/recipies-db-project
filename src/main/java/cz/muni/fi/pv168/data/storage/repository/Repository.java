package cz.muni.fi.pv168.data.storage.repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;

/**
 * Represents a repository for any model
 *
 * @param <M> the type of the model
 * @author Jan Martinek
 */
public interface Repository<M> {

    public int getSize();

    public Optional<M> findById(long id);

    public Optional<M> findByName(String name);

    public Optional<M> findUncommitted(String name, Supplier<ConnectionHandler> connection);

    public Optional<M> findByIndex(int index);

    public List<M> findAll();

    public void refresh();

    public void uncommitted(M entity, Consumer<M> action, Supplier<ConnectionHandler> connection);

    public void create(M newEntity);

    public void update(M oldEntity);

    public void deleteByIndex(int index);
}
