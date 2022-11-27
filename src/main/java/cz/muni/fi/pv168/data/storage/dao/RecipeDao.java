package cz.muni.fi.pv168.data.storage.dao;

import cz.muni.fi.pv168.data.storage.DataStorageException;
import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.entity.RecipeEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.*;
import java.util.function.Supplier;

public class RecipeDao extends AbstractDao<RecipeEntity>{

    private final Supplier<ConnectionHandler> defaultConnections;

    public RecipeDao(Supplier<ConnectionHandler> connections) {
        super(connections);
        this.defaultConnections = Objects.requireNonNull(connections);
    }

    @Override
    public String toString() {
        return "Recipe";
    }

    public void defaultConnection() {
        this.connections = defaultConnections;
    }

    public void customConnection(Supplier<ConnectionHandler> connection) {
        this.connections = connection;
    }

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
            if (entity.categoryId() == null) {
                statement.setNull(3, Types.BIGINT);
            } else {
                statement.setLong(3, entity.categoryId());
            }
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
            if (entity.categoryId() == null) {
                statement.setNull(3, java.sql.Types.NULL);
            } else {
                statement.setLong(3, entity.categoryId());
            }
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

    protected RecipeEntity fromResultSet(ResultSet resultSet) throws SQLException {
        return new RecipeEntity (
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
