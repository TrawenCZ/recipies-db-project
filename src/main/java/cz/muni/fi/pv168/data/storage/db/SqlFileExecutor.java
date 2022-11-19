package cz.muni.fi.pv168.data.storage.db;

import cz.muni.fi.pv168.data.storage.DataStorageException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * Execute the SQL files
 */
final class SqlFileExecutor {
    private final Class<?> resourceRoot;
    private final Supplier<TransactionHandler> transactions;

    SqlFileExecutor(
            Supplier<TransactionHandler> transactions,
            Class<?> resourceRoot
    ) {
        this.transactions = transactions;
        this.resourceRoot = resourceRoot;
    }

    void execute(String... fileNames) {
        execute(List.of(fileNames));
    }

    void execute(Collection<String> fileNames) {
        try (var transaction = transactions.get()) {
            var connection = transaction.connection().use();
            for (var fileName : fileNames) {
                executeSQLFile(connection, fileName);
            }
            transaction.commit();
        }
    }

    private void executeSQLFile(Connection connection, String fileName) {
        final String initSchemaSql = loadSQLFromResources(fileName);

        try (Statement statement = connection.createStatement()) {

            // Note: This solution is just for simplification and educational purposes only.
            // DO NOT USE THIS IN PRODUCTION!
            // Executing multiple statements might not be supported/enabled by default,
            // by the DB drivers you are using in H2, this support for multiple statements is enabled,
            // in MySQL you need to add property `allowMultiQueries=true` to the connection string
            // There is a security risk to use multiple SQL statements in single statement,
            // due to the SQL Injection
            statement.executeUpdate(initSchemaSql);

        } catch (SQLException e) {
            throw new DataStorageException("Unable to execute the SQL statements in the file: " + fileName, e);
        }
    }


    private String loadSQLFromResources(final String fileName) {
        URL resource = resourceRoot.getResource(fileName);
        if (resource == null) {
            throw new DataStorageException("Expected SQL file does not exit: " + fileName);
        }

        try {
            Path path = Paths.get(resource.toURI());

            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (URISyntaxException e) {
            throw new DataStorageException("Unable to convert resource URL to URI: " + resource);
        } catch (IOException e) {
            throw new DataStorageException("Unable to read the SQL resource content: " + resource, e);
        }
    }
}
