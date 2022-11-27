package cz.muni.fi.pv168.data.importers;

import cz.muni.fi.pv168.data.manipulation.importers.ObjectImporter;
import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.model.BaseUnitsEnum;
import cz.muni.fi.pv168.model.Nameable;
import cz.muni.fi.pv168.model.Unit;
import cz.muni.fi.pv168.wiring.TestDependencyProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import java.util.List;

public class UnitImportTest {
    private DatabaseManager databaseManager;
    private ObjectImporter<Unit> unitService;

    @BeforeEach
    void setUp() {
        this.databaseManager = DatabaseManager.createTestInstance(true);
        var dependencyProvider = new TestDependencyProvider(databaseManager);
        this.unitService = dependencyProvider.getUnitImporter();
    }

    @AfterEach
    void tearDown() {
        databaseManager.destroySchema();
    }

    @Test
    @Disabled
    void shouldSaveAllUnits() {
//        List<Unit> units = List.of(new Unit("ton", 1000000d, BaseUnitsEnum.GRAM),
//                new Unit("dolphin", 1, BaseUnitsEnum.PIECE),
//                new Unit("cup", 250d,BaseUnitsEnum.MILLILITER));
//        unitService.saveRecords(units);
//        List<String> saved = unitService.findAll().stream().map(Nameable::getName).toList();
//        List<String> toSave = units.stream().map(Nameable::getName).toList();
//        assertThat(saved).containsAll(toSave);
    }

}
