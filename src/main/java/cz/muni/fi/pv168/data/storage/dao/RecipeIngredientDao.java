package cz.muni.fi.pv168.data.storage.dao;

import cz.muni.fi.pv168.data.storage.DataStorageException;
import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.entity.RecipeIngredientEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.Supplier;

public class RecipeIngredientDao extends AbstractDao<RecipeIngredientEntity> {

    private final Supplier<ConnectionHandler> defaultConnections;

    public RecipeIngredientDao(Supplier<ConnectionHandler> connections) {
        super(connections);
        this.defaultConnections = Objects.requireNonNull(connections);
    }

    public void defaultConnection() {
        this.connections = defaultConnections;
    }

    public void customConnection(Supplier<ConnectionHandler> connection) {
        this.connections = connection;
    }

    @Override
    public String toString() {
        return "IngredientList";
    }

    @Override
    public RecipeIngredientEntity create(RecipeIngredientEntity entity) {
        String sql = "INSERT INTO IngredientList (recipeId, ingredientId, amount, unitId) VALUES (?, ?, ?, ?);";
        try (
            var connection = connections.get();
            var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setLong(1, entity.recipeId());
            statement.setLong(2, entity.ingredientId());
            statement.setDouble(3, entity.amount());
            statement.setLong(4, entity.unitId());
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

    public Optional<List<RecipeIngredientEntity>> findByRecipeId(long recipeId) {
        return findByCustomId("recipeId", recipeId);
    }

    public Optional<List<RecipeIngredientEntity>> findByIngredientId(long categoryId) {
        return findByCustomId("ingredientId", categoryId);
    }

    public Optional<List<RecipeIngredientEntity>> findByUnitId(long unitId) {
        return findByCustomId("unitId", unitId);
    }

    @Override
    public RecipeIngredientEntity update(RecipeIngredientEntity entity) {
        Objects.requireNonNull(entity.id(), "Entity id cannot be null");

        final var sql = """
                UPDATE IngredientList
                    SET
                    ingredientId = ?,
                    amount = ?,
                    unitId = ?
                    WHERE id = ?
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, entity.ingredientId());
            statement.setDouble(2, entity.amount());
            statement.setLong(3, entity.unitId());
            statement.setLong(4, entity.id());

            if (statement.executeUpdate() == 0) {
                throw new DataStorageException("Failed to update non-existing ingredient list: " + entity);
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update ingredient list with id: " + entity.id(), ex);
        }

        return findById(entity.id()).orElseThrow();
    }

    @Override
    public void deleteById(long entityId) {
        var sql = "DELETE FROM IngredientList WHERE id = ?";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, entityId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Ingredient List not found %d".formatted(entityId));
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException(
                        "More then 1 ingredient list (rows=%d) has been deleted: %d".formatted(rowsUpdated, entityId));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete ingredient list %d".formatted(entityId), ex);
        }
    }

    protected RecipeIngredientEntity fromResultSet(ResultSet resultSet) throws SQLException {
        return new RecipeIngredientEntity(
                resultSet.getLong("id"),
                resultSet.getLong("recipeId"),
                resultSet.getLong("ingredientId"),
                resultSet.getDouble("amount"),
                resultSet.getLong("unitId")
        );
    }

    private Optional<List<RecipeIngredientEntity>> findByCustomId(String idName, long id) {
        var sql = "SELECT * FROM IngredientList WHERE " + idName + " = ?";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();

            List<RecipeIngredientEntity> entities = new ArrayList<>();
            while (resultSet.next()) {
                entities.add(fromResultSet(resultSet));
            }
            return (entities.size() == 0) ? Optional.empty() : Optional.of(entities);
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load ingredient list by " + idName + " id: " + id, ex);
        }
    }
}
