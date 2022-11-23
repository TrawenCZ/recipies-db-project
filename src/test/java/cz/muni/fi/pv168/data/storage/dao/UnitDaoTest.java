package cz.muni.fi.pv168.data.storage.dao;

import cz.muni.fi.pv168.data.storage.DataStorageException;
import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.data.storage.entity.UnitEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

final class UnitDaoTest {
    private DatabaseManager manager;
    private UnitDao unitDao;
    @BeforeEach
    void setUp() {
        this.manager = DatabaseManager.createTestInstance(true);
        this.unitDao = new UnitDao(manager::getConnectionHandler);
    }

    @AfterEach
    void tearDown() {
        manager.destroySchema();
    }

    @Test
    void listAllUnits() {
        var allUnits = unitDao.findAll();
        assertThat(allUnits)
                .as("There should be 10 testing units")
                .hasSize(10);
    }

    @Test
    void createNewUnit() {
        var gram = unitDao.findByName("g").orElseThrow();
        var params = new UnitEntity("testing unit", 3.14, gram.id());
        var createdUnit = unitDao.create(params);
        assertThat(createdUnit)
                .as("There should be a new unit created")
                .isNotNull();
        assertThat(createdUnit.id())
                .isGreaterThan(1);
        assertThat(createdUnit.name()).isEqualTo(params.name());
        assertThat(createdUnit.amount()).isEqualTo(params.amount());
        assertThat(createdUnit.baseUnitId()).isEqualTo(params.baseUnitId());

        var foundCreated = unitDao.findById(createdUnit.id());
        assertThat(foundCreated)
                .as("Created unit should be present")
                .isPresent()
                .contains(createdUnit);
    }

    @Test
    void findExistingUnit(){
        final long id = 4;
        var existingUnitOpt = unitDao.findById(id);
        assertThat(existingUnitOpt)
                .isPresent();
    }

    @Test
    void deleteExistingUnitWithReference() {
        final long id = unitDao.findByName("kg").orElseThrow().id();
        var existingUnitOpt = unitDao.findById(id);
        assertThat(existingUnitOpt)
                .isPresent();
        var existingUnit = existingUnitOpt.orElseThrow();
        assertThatThrownBy(() -> unitDao.deleteById(existingUnit.id()))
                .isInstanceOf(DataStorageException.class)
                .hasMessageContaining("Failed to delete unit " + id);
    }

    @Test
    void deleteExistingUnitWithNoReference() {
        final long id = unitDao.findByName("cl").orElseThrow().id();
        var existingUnitOpt = unitDao.findById(id);
        assertThat(existingUnitOpt)
                .isPresent();
        var existingUnit = existingUnitOpt.orElseThrow();
        assertThatNoException().isThrownBy(() -> unitDao.deleteById(existingUnit.id()));
        var deletedUnit = unitDao.findById(existingUnit.id());
        assertThat(deletedUnit).isEmpty();
    }

    @Test
    void updateUnit() {
        var existingUnitOpt = unitDao.findByName("tuna");
        assertThat(existingUnitOpt)
                .isPresent();
        var existingUnit = existingUnitOpt.orElseThrow();
        String newName = "barel";
        Double newAmount = 119240.471;
        Long newBaseUnitId = unitDao.findByName("ml").orElseThrow().id();

        var newUnit = new UnitEntity(
                existingUnit.id(),
                newName,
                newAmount,
                newBaseUnitId
        );
        assertThatNoException().isThrownBy(()-> unitDao.update(newUnit));
        var updatedUnitOpt = unitDao.findById(existingUnit.id());
        assertThat(updatedUnitOpt)
                .isPresent();
        var updatedUnit = updatedUnitOpt.orElseThrow();
        assertThat(updatedUnit.id()).isEqualTo(existingUnit.id());
        assertThat(updatedUnit.name()).isEqualTo(newName);
        assertThat(updatedUnit.amount()).isEqualTo(newAmount);
        assertThat(updatedUnit.baseUnitId()).isEqualTo(newBaseUnitId);
    }

}
