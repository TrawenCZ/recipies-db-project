package cz.muni.fi.pv168.data.storage.dao;

import cz.muni.fi.pv168.data.storage.DataStorageException;
import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.entity.UnitEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.Supplier;

public class UnitDao extends AbstractDao<UnitEntity> {

    public UnitDao(Supplier<ConnectionHandler> connections) {
        super(connections);
    }

    @Override
    public String toString() {
        return "Unit";
    }

    @Override
    public UnitEntity create(UnitEntity entity) {
        String sql = "INSERT INTO Unit (name, amount, baseUnitId) VALUES (?, ?, ?);";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.name());
            statement.setDouble(2, entity.amount());
            if (entity.baseUnitId() == 0) {
                statement.setNull(3, 0);
            } else {
                statement.setLong(3, entity.baseUnitId());
            }
            statement.executeUpdate();

            try (ResultSet keyResultSet = statement.getGeneratedKeys()) {
                long unitId;
                if (keyResultSet.next()) {
                    unitId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + entity);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + entity);
                }
                return findById(unitId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + entity, ex);
        }
    }

    public Optional<UnitEntity> findByBaseUnitId(long id) {
        var sql = "SELECT * FROM Unit WHERE baseUnitId = ?";
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
            throw new DataStorageException("Failed to load unit by id: " + id, ex);
        }
    }


    @Override
    public UnitEntity update(UnitEntity entity) {
        Objects.requireNonNull(entity.id(), "Entity id cannot be null");

        final var sql = """
                UPDATE Unit
                    SET
                    name = ?,
                    amount = ?,
                    baseUnitId = ?
                    WHERE id = ?
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, entity.name());
            statement.setDouble(2, entity.amount());
            if (entity.baseUnitId() == 0) {
                statement.setNull(3, java.sql.Types.NULL);
            } else {
                statement.setLong(3, entity.baseUnitId());
            }
            statement.setLong(4, entity.id());

            if (statement.executeUpdate() == 0) {
                throw new DataStorageException("Failed to update non-existing unit: " + entity);
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update unit with id: " + entity.id(), ex);
        }

        return findById(entity.id()).orElseThrow(); // returns changed entity
    }

    @Override
    public void deleteById(long entityId) {
        var sql = "DELETE FROM Unit WHERE id = ?";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, entityId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Unit not found %d".formatted(entityId));
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException(
                        "More then 1 unit (rows=%d) has been deleted: %d".formatted(rowsUpdated, entityId));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete unit %d".formatted(entityId), ex);
        }
    }

    protected UnitEntity fromResultSet(ResultSet resultSet) throws SQLException {
        return new UnitEntity (
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getDouble("amount"),
            resultSet.getLong("baseUnitId")
        );
    }
}
