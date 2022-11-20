package cz.muni.fi.pv168.data.storage.dao;

import java.util.Objects;
import java.util.function.Supplier;

import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;

/**
 * Abstract class, able to change connections without creating
 * new DAOs. Handle with care, as there are no checks for
 * the connection being commited/closed before changing.
 *
 * @author Jan Martinek
 */
public abstract class AbstractDao<M> implements DataAccessObject<M> {

    private final Supplier<ConnectionHandler> defaultConnections;
    protected Supplier<ConnectionHandler> connections;

    protected AbstractDao(Supplier<ConnectionHandler> connections) {
        this.defaultConnections = Objects.requireNonNull(connections);
        this.connections = connections;
    }

    @Override
    public void defaultConnection() {
        this.connections = defaultConnections;
    }

    @Override
    public void customConnection(Supplier<ConnectionHandler> connection) {
        this.connections = connection;
    }

}
