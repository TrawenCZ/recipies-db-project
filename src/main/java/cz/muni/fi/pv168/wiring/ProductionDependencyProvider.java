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
        // TODO: change to production
        DatabaseManager databaseManager = DatabaseManager.createServerInstance();
        databaseManager.load();
        return databaseManager;
    }
}
