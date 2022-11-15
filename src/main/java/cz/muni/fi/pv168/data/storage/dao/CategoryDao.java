package cz.muni.fi.pv168.data.storage.dao;

import cz.muni.fi.pv168.data.storage.DataStorageException;
import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.entity.CategoryEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class CategoryDao implements DataAccessObject<CategoryEntity> {
    private final Supplier<ConnectionHandler> connections;
    public CategoryDao(Supplier<ConnectionHandler> connections) { this.connections = connections; }

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
    public Collection<CategoryEntity> findAll() {
        var sql = """
                SELECT id,
                       name,
                       color
                    FROM Category
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            List<CategoryEntity> departments = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var department = categoryFromResultSet(resultSet);
                    departments.add(department);
                }
            }

            return departments;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all categories", ex);
        }
    }

    @Override
    public Optional<CategoryEntity> findById(long id) {
        var sql = """
                SELECT id,
                       name,
                       color
                    FROM Category
                    WHERE id = ?
                """;

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(categoryFromResultSet(resultSet));
            } else {
                // category not found
                return Optional.empty();
            }
        } catch (
                SQLException ex) {
            throw new DataStorageException("Failed to load category by id: " + id, ex);
        }
    }

    @Override
    public CategoryEntity update(CategoryEntity entity) {
        // TODO: not implemented yet
        throw new UnsupportedOperationException("Not implemented yet");

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

    private static CategoryEntity categoryFromResultSet(ResultSet resultSet) throws SQLException {
        return new CategoryEntity(resultSet.getLong("id"), resultSet.getString("name"),
                resultSet.getString("color"));
    }
}
