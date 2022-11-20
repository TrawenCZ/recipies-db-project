package cz.muni.fi.pv168.wiring;

import cz.muni.fi.pv168.data.storage.db.DatabaseManager;

public final class TestDependencyProvider extends CommonDependencyProvider {

    public TestDependencyProvider(DatabaseManager databaseManager) {
        super(databaseManager);
    }
}

