package cz.muni.fi.pv168.data.storage.repository;

import cz.muni.fi.pv168.data.storage.DataStorageException;
import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.model.BaseUnitsEnum;
import cz.muni.fi.pv168.model.Unit;
import cz.muni.fi.pv168.wiring.TestDependencyProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

final class UnitRepositoryTest {
    private DatabaseManager databaseManager;
    private Repository<Unit> unitRepository;

    @BeforeEach
    void setUp() {
        this.databaseManager = DatabaseManager.createTestInstance(true);
        var dependencyProvider = new TestDependencyProvider(databaseManager);
        this.unitRepository = dependencyProvider.getUnitRepository();
    }

    @AfterEach
    void tearDown() {
        databaseManager.destroySchema();
    }

    @Test
    void createNewUnit() {
        final String name = "megagram";
        final double valueInBaseUnit = 150.0;
        Unit unitToCreate = new Unit(name, valueInBaseUnit, BaseUnitsEnum.GRAM);
        unitRepository.create(unitToCreate);

        Unit storedUnit = unitRepository
                .findByIndex(unitRepository.getSize() - 1)
                .orElseThrow();

        assertThat(storedUnit.getName()).isEqualTo(unitToCreate.getName());
        assertThat(storedUnit.getValueInBaseUnit()).isEqualTo(unitToCreate.getValueInBaseUnit());
        assertThat(storedUnit.getId()).isNotNull();
    }

    @Test
    void listAllTestingUnits() {
        List<Unit> units = unitRepository
                .findAll();

        assertThat(units).hasSize(10); // including base units;
    }

    @Test
    void findUnitByIndex() {
        Optional<Unit> storedUnit = unitRepository
                .findByIndex(1);

        assertThat(storedUnit).isPresent();
    }

    @Test
    void findUnitByIndexShouldFailForIndexTooHigh() {
        Optional<Unit> storedUnit = unitRepository
                .findByIndex(1000);

        assertThat(storedUnit).isEmpty();
    }

    @Test
    void findUnitById() {
        Optional<Unit> storedUnit = unitRepository
                .findById(1);

        assertThat(storedUnit).isPresent();
    }

    @Test
    void findUnitByIndexShouldFailForIdTooHigh() {
        Optional<Unit> storedUnit = unitRepository
                .findById(1000);

        assertThat(storedUnit).isEmpty();
    }

    @Test
    void findUnitByName() {
        Optional<Unit> storedUnitOpt = unitRepository.
                findByName("kg");

        assertThat(storedUnitOpt).isPresent();
        var storedUnit = storedUnitOpt.orElseThrow();
        assertThat(storedUnit.getId()).isNotNull();
        assertThat(storedUnit.getName()).isEqualTo("kg");
        assertThat(storedUnit.getValueInBaseUnit()).isEqualTo(1000.0);
        assertThat(storedUnit.getBaseUnit()).isEqualTo(BaseUnitsEnum.GRAM);

    }

    @Test
    void findUnitByNameShouldFailForNonExistingName() {
        Optional<Unit> storedUnit = unitRepository.
                findByName("non existing unit");

        assertThat(storedUnit).isEmpty();
    }

    @Test
    void updateUnit() {
        var unit = unitRepository.findAll()
                .stream()
                .filter(e -> e.getName().equals("tucet"))
                .findFirst()
                .orElseThrow();

        unit.setName("update unit name");
        unit.setValueInBaseUnit(24.8046);
        unit.setBaseUnit(BaseUnitsEnum.PIECE);
        unitRepository.update(unit);

        assertThat(unitRepository.findByName("tucet")).isEmpty();
        Unit updatedUnit = unitRepository.findById(unit.getId()).orElseThrow();

        assertThat(updatedUnit.getId()).isEqualTo(unit.getId());
        assertThat(updatedUnit.getName()).isEqualTo("update unit name");
        assertThat(updatedUnit.getValueInBaseUnit()).isEqualTo(unit.getValueInBaseUnit());
        assertThat(updatedUnit.getBaseUnit()).isEqualTo(unit.getBaseUnit());


    }

    @Test
    void deleteUnitReferenced() {
        var units = unitRepository.findAll();
        for (int i = 0 ; i < units.size() ; i++ ) {
            if (units.get(i).getName().equals("kg")) {
                int deleteIndex = i; // lambda
                String errorIndex =  String.valueOf(deleteIndex + 1);
                assertThatThrownBy(() -> unitRepository.deleteByIndex(deleteIndex))
                        .isInstanceOf(DataStorageException.class)
                        .hasMessageContaining("Failed to delete unit " + errorIndex);
                return;
            }
        }
    }

    @Test
    void deleteUnitNonReferenced() {
        var units = unitRepository.findAll();
        for (int i = 0 ; i < units.size() ; i++ ) {
            if (units.get(i).getName().equals("cl")) {
                int deleteIndex = i;
                assertThatNoException().isThrownBy(() -> unitRepository.deleteByIndex(deleteIndex));
                assertThat(unitRepository.findByName("cl")).isEmpty();
                return;
            }
        }
    }

}
