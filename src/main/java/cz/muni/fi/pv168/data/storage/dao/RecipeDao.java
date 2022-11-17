package cz.muni.fi.pv168.data.storage.dao;

import cz.muni.fi.pv168.data.storage.DataStorageException;
import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.entity.RecipeEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.Supplier;

public class RecipeDao implements DataAccessObject<RecipeEntity>{
    private final Supplier<ConnectionHandler> connections;
    public RecipeDao(Supplier<ConnectionHandler> connections) { this.connections = connections; }

    @Override
    public RecipeEntity create(RecipeEntity entity) {
        var sql = """
                INSERT INTO Recipe (name,
                description,
                categoryId,
                portions,
                duration,
                instruction)
                VALUES (?, ?, ?, ?, ?, ?);
        """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.name());
            statement.setString(2, entity.description());
            statement.setLong(3, entity.categoryId());
            statement.setLong(4, entity.portions());
            statement.setLong(5, entity.duration());
            statement.setString(6, entity.instruction());
            statement.executeUpdate();

            try (ResultSet keyResultSet = statement.getGeneratedKeys()) {
                long recipeId;

                if (keyResultSet.next()) {
                    recipeId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + entity);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + entity);
                }

                return findById(recipeId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + entity, ex);
        }
    }

    @Override
    public Collection<RecipeEntity> findAll() {
        var sql = """
            SELECT id,
                   name,
                   description,
                   categoryId,
                   portions,
                   duration,
                   instruction
                FROM Recipe
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            List<RecipeEntity> recipes = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var unit = recipeFromResultSet(resultSet);
                    recipes.add(unit);
                }
            }
            return recipes;
           } catch (SQLException ex) {
                throw new DataStorageException("Failed to load all units", ex);
        }
    }

    @Override
    public Optional<RecipeEntity> findById(long id) {
        var sql = """
            SELECT id,
                   name,
                   description,
                   categoryId,
                   portions,
                   duration,
                   instruction
                FROM Recipe
                WHERE id = ?
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(recipeFromResultSet(resultSet));
            } else {
                // recipe not found
                return Optional.empty();
            }
        } catch (
                SQLException ex) {
            throw new DataStorageException("Failed to load recipe by id: " + id, ex);
        }
    }

    @Override
    public RecipeEntity update(RecipeEntity entity) {
        Objects.requireNonNull(entity.id(), "Entity id cannot be null");

        final var sql = """
                UPDATE Recipe
                    SET
                    name = ?,
                    description = ?,
                    categoryId = ?,
                    portions = ?,
                    duration = ?,
                    instruction = ?
                    WHERE id = ?
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, entity.name());
            statement.setString(2, entity.description());
            statement.setLong(3, entity.categoryId());
            statement.setLong(4, entity.portions());
            statement.setLong(5, entity.duration());
            statement.setString(6, entity.instruction());
            statement.setLong(7, entity.id());

            if (statement.executeUpdate() == 0) {
                throw new DataStorageException("Failed to update non-existing recipe: " + entity);
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update recipe with id: " + entity.id(), ex);
        }

        return findById(entity.id()).orElseThrow(); // returns changed entity
    }

    @Override
    public void deleteById(long entityId) {
        var sql = "DELETE FROM Recipe WHERE id = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, entityId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Recipe not found %d".formatted(entityId));
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException(
                        "More then 1 recipe (rows=%d) has been deleted: %d".formatted(rowsUpdated, entityId));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete recipe %d".formatted(entityId), ex);
        }

    }
    private static RecipeEntity recipeFromResultSet(ResultSet resultSet) throws SQLException {
        return new RecipeEntity
                (
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getLong("categoryId"),
                        resultSet.getLong("portions"),
                        resultSet.getLong("duration"),
                        resultSet.getString("instruction")
                );
    }
}
