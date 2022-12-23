package cz.muni.fi.pv168.data.storage.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import cz.muni.fi.pv168.data.storage.DataStorageException;
import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;

/**
 * @author Jan Martinek
 */
public abstract class AbstractDao<M> implements DataAccessObject<M> {

    private final String SELECT_CMD = "SELECT * FROM " + toString() + " WHERE ";
    protected Supplier<ConnectionHandler> connections;

    protected AbstractDao(Supplier<ConnectionHandler> connections) {
        this.connections = Objects.requireNonNull(connections);
    }

    @Override
    public Optional<M> findById(long id) {
        var sql = SELECT_CMD + "id = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(fromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load " + toString() + " by id: " + id, ex);
        }
    }

    @Override
    public Optional<M> findByName(String name) {
        var sql = SELECT_CMD + "name = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, name);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(fromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load " + toString() + " by name: " + name, ex);
        }
    }

    @Override
    public Collection<M> findAll() {
        var sql = SELECT_CMD + "1 = 1";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            List<M> entities = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    entities.add(fromResultSet(resultSet));
                }
            }
            return entities;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all entities from " + toString(), ex);
        }
    }

    protected abstract M fromResultSet(ResultSet resultSet) throws SQLException;
}
