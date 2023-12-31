package cz.muni.fi.pv168.wiring;

import cz.muni.fi.pv168.data.storage.db.DatabaseManager;

/**
 * Dependency provider for production environment
 */
public final class ProductionDependencyProvider extends CommonDependencyProvider {

    public ProductionDependencyProvider() {
        super(createDatabaseManager());
    }

    private static DatabaseManager createDatabaseManager() {
        DatabaseManager databaseManager = DatabaseManager.createProductionInstance();
        databaseManager.load();
        return databaseManager;
    }
}
