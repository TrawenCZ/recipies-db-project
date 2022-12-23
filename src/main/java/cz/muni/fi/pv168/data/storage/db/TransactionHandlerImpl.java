package cz.muni.fi.pv168.data.storage.db;


import cz.muni.fi.pv168.data.storage.DataStorageException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Transaction handler, the connection returned is ALWAYS THE SAME
 */
class TransactionHandlerImpl implements TransactionHandler {
    private final ConnectionHandler connectionHandler;

    /**
     * Creates new transaction over given connection
     * @param connection database connection
     */
    TransactionHandlerImpl(Connection connection) throws SQLException {
        Objects.requireNonNull(connection, "Missing connection object");
        connection.setAutoCommit(false);
        this.connectionHandler = new ConnectionHandlerImpl(connection);
    }

    @Override
    public ConnectionHandler connection() {
        return connectionHandler;
    }

    @Override
    public void rollback() {
        try {
            connectionHandler.use().rollback();
        } catch (SQLException e) {
            throw new DataStorageException("Unable to rollback transaction", e);
        }
    }

    @Override
    public void commit() {
        try {
            connectionHandler.use().commit();
        } catch (SQLException e) {
            throw new DataStorageException("Unable to commit transaction", e);
        }
    }

    @Override
    public void close() {
        try {
            connectionHandler.use().close();
        } catch (SQLException e) {
            throw new DataStorageException("Unable close database connection", e);
        }
    }

}
