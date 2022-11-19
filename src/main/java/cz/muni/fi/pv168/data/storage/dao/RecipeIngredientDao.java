package cz.muni.fi.pv168.data.storage.dao;

import cz.muni.fi.pv168.data.storage.DataStorageException;
import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.entity.RecipeIngredientEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.Supplier;

public class RecipeIngredientDao implements DataAccessObject<RecipeIngredientEntity> {
    private final Supplier<ConnectionHandler> connections;
    public RecipeIngredientDao(Supplier<ConnectionHandler> connections) { this.connections = connections; }

    @Override
    public RecipeIngredientEntity create(RecipeIngredientEntity entity) {
        String sql = "INSERT INTO Unit (recipeId, ingredientId, amount, unitId) VALUES (?, ?, ?, ?);";
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

    @Override
    public Collection<RecipeIngredientEntity> findAll() {
        var sql = """
                SELECT id,
                       recipeId,
                       ingredientId,
                       amount,
                       unitId
                    FROM IngredientList
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            List<RecipeIngredientEntity> ingredientLists = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var ingredientList = ingredientListFromResultSet(resultSet);
                    ingredientLists.add(ingredientList);
                }
            }

            return ingredientLists;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all ingredient list", ex);
        }
    }

    @Override
    public Optional<RecipeIngredientEntity> findByName(String name) {
        var sql = """
                SELECT id,
                       recipeId,
                       ingredientId,
                       amount,
                       unitId
                    FROM IngredientList
                    WHERE name = ?
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, name);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(ingredientListFromResultSet(resultSet));
            } else {
                // ingredient list not found
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load ingredient list by id: " + name, ex);
        }
    }

    @Override
    public Optional<RecipeIngredientEntity> findById(long id) {
        var sql = """
                SELECT id,
                       recipeId,
                       ingredientId,
                       amount,
                       unitId
                    FROM IngredientList
                    WHERE id = ?
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(ingredientListFromResultSet(resultSet));
            } else {
                // ingredient list not found
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load ingredient list by id: " + id, ex);
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

    private static RecipeIngredientEntity ingredientListFromResultSet(ResultSet resultSet) throws SQLException {
        return new RecipeIngredientEntity(
                resultSet.getLong("id"),
                resultSet.getLong("recipeId"),
                resultSet.getLong("ingredientId"),
                resultSet.getDouble("amount"),
                resultSet.getLong("unitId")
        );
    }

    private Optional<List<RecipeIngredientEntity>> findByCustomId(String idName, long id) {
        var sql = """
                SELECT id,
                       recipeId,
                       ingredientId,
                       amount,
                       unitId
                    FROM IngredientList
                    WHERE ? = ?
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, idName);
            statement.setLong(2, id);
            var resultSet = statement.executeQuery();

            List<RecipeIngredientEntity> entities = new ArrayList<>();
            while (resultSet.next()) {
                entities.add(new RecipeIngredientEntity(
                    resultSet.getLong("id"),
                    resultSet.getLong("recipeId"),
                    resultSet.getLong("ingredientId"),
                    resultSet.getDouble("amount"),
                    resultSet.getLong("unitId")
                ));
            }
            return (entities.size() == 0) ? Optional.empty() : Optional.of(entities);
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load ingredient list by " + idName + " id: " + id, ex);
        }
    }
}
