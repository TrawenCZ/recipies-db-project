package cz.muni.fi.pv168.data.storage.dao;

import cz.muni.fi.pv168.data.storage.DataStorageException;
import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.entity.CategoryEntity;
import cz.muni.fi.pv168.data.storage.entity.IngredientEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class IngredientDao implements DataAccessObject<IngredientEntity> {
    private final Supplier<ConnectionHandler> connections;
    public IngredientDao(Supplier<ConnectionHandler> connections) { this.connections = connections; }

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
    public Collection<IngredientEntity> findAll() {
        var sql = """
                SELECT id,
                       name,
                       kcalPerUnit,
                       baseUnitId
                    FROM Ingredient
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            List<IngredientEntity> ingredients = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var ingredient = ingredientFromResultSet(resultSet);
                    ingredients.add(ingredient);
                }
            }

            return ingredients;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all categories", ex);
        }
    }

    @Override
    public Optional<IngredientEntity> findById(long id) {
        var sql = """
               SELECT id,
                      name,
                      kcalPerUnit,
                      baseUnitId
                   FROM Ingredient
                   WHERE id = ?
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(ingredientFromResultSet(resultSet));
            } else {
                // ingredient not found
                return Optional.empty();
            }
        } catch (
                SQLException ex) {
            throw new DataStorageException("Failed to load ingredient by id: " + id, ex);
        }
    }

    @Override
    public IngredientEntity update(IngredientEntity entity) {
        // TODO: not implemented yet
        throw new UnsupportedOperationException("Not implemented yet");

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

    private static IngredientEntity ingredientFromResultSet(ResultSet resultSet) throws SQLException {
        return new IngredientEntity
                (
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("kcalPerUnit"),
                    resultSet.getLong("baseUnitId")
                );
    }
}
