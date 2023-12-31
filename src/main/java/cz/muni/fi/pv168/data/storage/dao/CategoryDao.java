package cz.muni.fi.pv168.data.storage.dao;

import cz.muni.fi.pv168.data.storage.DataStorageException;
import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.entity.CategoryEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.Supplier;

public class CategoryDao extends AbstractDao<CategoryEntity> {

    public CategoryDao(Supplier<ConnectionHandler> connections) {
        super(connections);
    }

    @Override
    public String toString() {
        return "Category";
    }

    @Override
    public CategoryEntity create(CategoryEntity entity) {
        String sql = "INSERT INTO Category (name, color) VALUES (?, ?);";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.name());
            statement.setString(2, entity.color());
            statement.executeUpdate();

            try (ResultSet keyResultSet = statement.getGeneratedKeys()) {
                long categoryId;

                if (keyResultSet.next()) {
                    categoryId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + entity);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + entity);
                }

                return findById(categoryId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + entity, ex);
        }

    }

    @Override
    public CategoryEntity update(CategoryEntity entity) {
        Objects.requireNonNull(entity.id(), "Entity id cannot be null");

        final var sql = """
                UPDATE Category
                    SET
                    name = ?,
                    color = ?
                    WHERE id = ?
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, entity.name());
            statement.setString(2, entity.color());
            statement.setLong(3, entity.id());

            if (statement.executeUpdate() == 0) {
                throw new DataStorageException("Failed to update non-existing category: " + entity);
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update category with id: " + entity.id(), ex);
        }

        return findById(entity.id()).orElseThrow(); // returns changed entity

    }

    @Override
    public void deleteById(long entityId) {
        var sql = "DELETE FROM Category WHERE id = ?";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, entityId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Category not found %d".formatted(entityId));
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException(
                        "More then 1 category (rows=%d) has been deleted: %d".formatted(rowsUpdated, entityId));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete category %d".formatted(entityId), ex);
        }
    }

    protected CategoryEntity fromResultSet(ResultSet resultSet) throws SQLException {
        return new CategoryEntity(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("color")
        );
    }
}
