package cz.muni.fi.pv168.wiring;


import cz.muni.fi.pv168.data.storage.DataStorageException;
import cz.muni.fi.pv168.data.storage.db.DatabaseManager;

import java.sql.SQLException;

/**
 * Dependency provider for production environment
 */
public final class ProductionDependencyProvider extends CommonDependencyProvider {

    public ProductionDependencyProvider() {
        super(createDatabaseManager());
    }

    private static DatabaseManager createDatabaseManager() {
        DatabaseManager databaseManager = DatabaseManager.createProductionInstance();
        databaseManager.initSchema();
        try {
            databaseManager.initData("production");
        } catch (DataStorageException e) {}

        return databaseManager;
    }
}
