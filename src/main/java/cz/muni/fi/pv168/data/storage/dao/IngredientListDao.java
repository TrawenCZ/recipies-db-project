package cz.muni.fi.pv168.data.storage.dao;

import cz.muni.fi.pv168.data.storage.DataStorageException;
import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.entity.IngredientListEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class IngredientListDao implements DataAccessObject<IngredientListEntity> {
    private final Supplier<ConnectionHandler> connections;
    public IngredientListDao(Supplier<ConnectionHandler> connections) { this.connections = connections; }

    @Override
    public IngredientListEntity create(IngredientListEntity entity) {
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
    public Collection<IngredientListEntity> findAll() {
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
            List<IngredientListEntity> ingredientLists = new ArrayList<>();
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
    public Optional<IngredientListEntity> findById(long id) {
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
        } catch (
                SQLException ex) {
            throw new DataStorageException("Failed to load ingredient list by id: " + id, ex);
        }
    }

    public Optional<IngredientListEntity> findByRecipeId(long recipeId) {
        var sql = """
                SELECT id,
                       recipeId,
                       ingredientId,
                       amount,
                       unitId
                    FROM IngredientList
                    WHERE recipeId = ?
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, recipeId);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(ingredientListFromResultSet(resultSet));
            } else {
                // ingredient list not found
                return Optional.empty();
            }
        } catch (
                SQLException ex) {
            throw new DataStorageException("Failed to load ingredient list by id: " + recipeId, ex);
        }
    }

    @Override
    public IngredientListEntity update(IngredientListEntity entity) {
        throw new UnsupportedOperationException("Not implemented yet");
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

    private static IngredientListEntity ingredientListFromResultSet(ResultSet resultSet) throws SQLException {
        return new IngredientListEntity
                (
                        resultSet.getLong("id"),
                        resultSet.getLong("recipeId"),
                        resultSet.getLong("ingredientId"),
                        resultSet.getDouble("amount"),
                        resultSet.getLong("unitId")
                );
    }
}
