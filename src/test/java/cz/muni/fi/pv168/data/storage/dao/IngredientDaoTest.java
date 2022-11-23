package cz.muni.fi.pv168.data.storage.dao;

import cz.muni.fi.pv168.data.storage.DataStorageException;
import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.data.storage.entity.IngredientEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

final class IngredientDaoTest {
    private DatabaseManager manager;
    private IngredientDao ingredientDao;
    private UnitDao unitDao;
    @BeforeEach
    void setUp() {
        this.manager = DatabaseManager.createTestInstance(true);
        this.ingredientDao = new IngredientDao(manager::getConnectionHandler);
        this.unitDao = new UnitDao(manager::getConnectionHandler);
    }

    @AfterEach
    void tearDown() {
        manager.destroySchema();
    }

    @Test
    void listAllIngredients() {
        var allIngredients = ingredientDao.findAll();
        assertThat(allIngredients)
                .as("There should be 23 testing ingredients")
                .hasSize(23);
    }

    @Test
    void createNewIngredient() {
        var unit = unitDao.findByName("ml").orElseThrow();
        var params = new IngredientEntity("testing ingredient", 133, unit.id());
        var createdIngredient = ingredientDao.create(params);
        assertThat(createdIngredient)
                .as("There should be a new ingredient created")
                .isNotNull();
        assertThat(createdIngredient.id())
                .isGreaterThan(1);
        assertThat(createdIngredient.name()).isEqualTo(params.name());
        assertThat(createdIngredient.kcalPerUnit()).isEqualTo(params.kcalPerUnit());
        assertThat(createdIngredient.baseUnitId()).isEqualTo(params.baseUnitId());

        var foundCreated = ingredientDao.findById(createdIngredient.id());
        assertThat(foundCreated)
                .as("Created ingredient should be present")
                .isPresent()
                .contains(createdIngredient);
    }

    @Test
    void findExistingIngredient() {
        final long id = 1;
        var existingIngredientOpt = ingredientDao.findById(id);
        assertThat(existingIngredientOpt)
                .isPresent();
    }

    @Test
    void deleteExistingIngredientWithReference() {
        final long id = ingredientDao.findByName("červené víno").orElseThrow().id();
        var existingIngredientOpt = ingredientDao.findById(id);
        assertThat(existingIngredientOpt)
                .isPresent();
        var existingIngredient = existingIngredientOpt.orElseThrow();
        assertThatThrownBy(() -> ingredientDao.deleteById(existingIngredient.id()))
                .isInstanceOf(DataStorageException.class)
                .hasMessageContaining("Failed to delete ingredient " + id);
    }

    @Test
    void deleteExistingIngredientWithNoReference() {
        final long id = ingredientDao.findByName("karamel").orElseThrow().id();
        var existingIngredientOpt = ingredientDao.findById(id);
        assertThat(existingIngredientOpt)
                .isPresent();
        var existingIngredient = existingIngredientOpt.orElseThrow();
        assertThatNoException().isThrownBy(() -> ingredientDao.deleteById(existingIngredient.id()));
        var deletedIngredient = ingredientDao.findById(existingIngredient.id());
        assertThat(deletedIngredient).isEmpty();
    }
    @Test
    void updateIngredient() {
        var existingIngredientOpt = ingredientDao.findByName("česnek");
        assertThat(existingIngredientOpt)
                .isPresent();
        var existingIngredient = existingIngredientOpt.orElseThrow();
        final String newName = "pepřové kuličky";
        final Double newKcalPerUnit = 1313.24;
        final long newBaseUnitId = unitDao.findByName("pc(s)").orElseThrow().id();
        var newIngredient = new IngredientEntity(
                existingIngredient.id(),
                newName,
                newKcalPerUnit,
                newBaseUnitId
        );
        assertThatNoException().isThrownBy(() -> ingredientDao.update(newIngredient));
        var updatedIngredientOpt = ingredientDao.findById(existingIngredient.id());
        assertThat(updatedIngredientOpt)
                .isPresent();

        var updatedIngredient = updatedIngredientOpt.orElseThrow();

        assertThat(updatedIngredient.id()).isEqualTo(existingIngredient.id());
        assertThat(updatedIngredient.name()).isEqualTo(newName);
        assertThat(updatedIngredient.kcalPerUnit()).isEqualTo(newKcalPerUnit);
        assertThat(updatedIngredient.baseUnitId()).isEqualTo(newBaseUnitId);
    }

}
