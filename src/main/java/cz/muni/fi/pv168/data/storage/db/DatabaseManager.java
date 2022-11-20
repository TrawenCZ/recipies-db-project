package cz.muni.fi.pv168.data.storage.db;

import cz.muni.fi.pv168.data.storage.DataStorageException;
import org.h2.jdbcx.JdbcConnectionPool;

import javax.sql.DataSource;
import javax.swing.JOptionPane;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

/**
 * The class is responsible for managing H2 database connection and schemas
 */
public final class DatabaseManager {

    private static final String PROJECT_NAME = "pv168-recipies-db";
    private static final String DB_PROPERTIES_STRING = "DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false";

    private final DataSource dataSource;
    private final SqlFileExecutor sqlFileExecutor;

    private DatabaseManager(String jdbcUri) {
        // connection pool with empty credentials
        this.dataSource = JdbcConnectionPool.create(jdbcUri, "", "");
        this.sqlFileExecutor = new SqlFileExecutor(this::getTransactionHandler, DatabaseManager.class);
    }

    public static DatabaseManager createServerInstance() {
        String connectionString = "jdbc:h2:%s;%s".formatted("tcp://localhost/" + createDbFileSystemPath(), DB_PROPERTIES_STRING);
        // We need this for debugging purposes
        System.out.println("JDBC connection URI: " + connectionString);
        return new DatabaseManager(connectionString);
    }

    public static DatabaseManager createProductionInstance() {
        String connectionString = "jdbc:h2:%s;%s".formatted(createDbFileSystemPath(), DB_PROPERTIES_STRING);
        // We need this for debugging purposes
        System.out.println("JDBC connection URI: " + connectionString);
        return new DatabaseManager(connectionString);
    }

    public static DatabaseManager createTestInstance() {
        String connectionString = "jdbc:h2:mem:%s;%s".formatted(PROJECT_NAME, DB_PROPERTIES_STRING);
        var databaseManager = new DatabaseManager(connectionString);
        databaseManager.load();
        return databaseManager;
    }

    public void load() {
        this.initSchema();
        try {
            sqlFileExecutor.executeSelect("verify_columns.sql");
        } catch (Exception e) {
            int n = JOptionPane.showOptionDialog(
                null,
                """
                    CRITICAL ERROR:
                    Incompatible database model!

                    Purge of current databse is required, do you want to proceed?
                """,
                "Database error",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                null,
                null
            );
            if (n == JOptionPane.OK_OPTION) {
                this.destroySchema();
                this.initSchema();
            } else {
                System.exit(-1);
            }
        }
    }

    public ConnectionHandler getConnectionHandler() {
        try {
            return new ConnectionHandlerImpl(dataSource.getConnection());
        } catch (SQLException e) {
            throw new DataStorageException("Unable to get a new connection", e);
        }
    }

    public TransactionHandler getTransactionHandler() {
        try {
            return new TransactionHandlerImpl(dataSource.getConnection());
        } catch (SQLException e) {
            throw new DataStorageException("Unable to get a new connection", e);
        }
    }

    public void destroySchema() {
        sqlFileExecutor.execute("drop.sql");
    }

    public void initSchema() {
        sqlFileExecutor.execute("init.sql");
    }

    public void initData(String environment) {
        sqlFileExecutor.execute("data_%s.sql".formatted(environment));
    }

    private static Path createDbFileSystemPath() {
        String userHomeDir = System.getProperty("user.home");
        Path projectDbPath = Paths.get(userHomeDir, "pv168", "db", PROJECT_NAME);

        File parentDir = projectDbPath.getParent().toFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        return projectDbPath;
    }
}
