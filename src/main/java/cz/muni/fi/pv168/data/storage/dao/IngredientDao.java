package cz.muni.fi.pv168.data.storage.dao;

import cz.muni.fi.pv168.data.storage.DataStorageException;
import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.entity.IngredientEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.Supplier;

public class IngredientDao extends AbstractDao<IngredientEntity> {

    public IngredientDao(Supplier<ConnectionHandler> connections) {
        super(connections);
    }

    @Override
    public String toString() {
        return "Ingredient";
    }

    @Override
    public IngredientEntity create(IngredientEntity entity) {
        String sql = "INSERT INTO Ingredient (name, kcalPerUnit, baseUnitId) VALUES (?, ?, ?);";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.name());
            statement.setDouble(2, entity.kcalPerUnit());
            statement.setLong(3, entity.baseUnitId());
            statement.executeUpdate();

            try (ResultSet keyResultSet = statement.getGeneratedKeys()) {
                long ingredientId;

                if (keyResultSet.next()) {
                    ingredientId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + entity);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + entity);
                }

                return findById(ingredientId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + entity, ex);
        }
    }

    @Override
    public IngredientEntity update(IngredientEntity entity) {
        Objects.requireNonNull(entity.id(), "Entity id cannot be null");
        final var sql = """
                UPDATE Ingredient
                    SET
                    name = ?,
                    kcalPerUnit = ?,
                    baseUnitId = ?
                    WHERE id = ?
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, entity.name());
            statement.setDouble(2, entity.kcalPerUnit());
            statement.setLong(3, entity.baseUnitId());
            statement.setLong(4, entity.id());

            if (statement.executeUpdate() == 0) {
                throw new DataStorageException("Failed to update non-existing ingredient: " + entity);
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update ingredient with id: " + entity.id(), ex);
        }
        return findById(entity.id()).orElseThrow(); // returns changed entity
    }

    @Override
    public void deleteById(long entityId) {
        var sql = "DELETE FROM Ingredient WHERE id = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, entityId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Ingredient not found %d".formatted(entityId));
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException(
                        "More then 1 ingredient (rows=%d) has been deleted: %d".formatted(rowsUpdated, entityId));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete ingredient %d".formatted(entityId), ex);
        }
    }

    protected IngredientEntity fromResultSet(ResultSet resultSet) throws SQLException {
        return new IngredientEntity
        (
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getDouble("kcalPerUnit"),
            resultSet.getLong("baseUnitId")
        );
    }
}
