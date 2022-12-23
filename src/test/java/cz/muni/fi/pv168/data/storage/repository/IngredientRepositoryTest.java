package cz.muni.fi.pv168.data.storage.repository;

import cz.muni.fi.pv168.data.storage.DataStorageException;
import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.model.BaseUnitsEnum;
import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.Unit;
import cz.muni.fi.pv168.wiring.TestDependencyProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

final class IngredientRepositoryTest {
    private DatabaseManager databaseManager;
    private Repository<Ingredient> ingredientRepository;
    private Repository<Unit> unitRepository;

    @BeforeEach
    void setUp() {
        this.databaseManager = DatabaseManager.createTestInstance(true);
        var dependencyProvider = new TestDependencyProvider(databaseManager);
        this.ingredientRepository = dependencyProvider.getIngredientRepository();
        this.unitRepository = dependencyProvider.getUnitRepository();
    }

    @AfterEach
    void tearDown() {
        databaseManager.destroySchema();
    }

    @Test
    void listAllTestingIngredients() {
        List<Ingredient> ingredients = ingredientRepository
                .findAll();

        assertThat(ingredients).hasSize(23); // including base units;
    }

    @Test
    void findIngredientByIndex() {
        Optional<Ingredient> storedIngredient = ingredientRepository
                .findByIndex(1);

        assertThat(storedIngredient).isPresent();
    }

    @Test
    void findIngredientByIndexShouldFailForIndexTooHigh() {
        Optional<Ingredient> storedIngredient = ingredientRepository
                .findByIndex(1000);

        assertThat(storedIngredient).isEmpty();
    }

    @Test
    void findIngredientById() {
        Optional<Ingredient> storedIngredient = ingredientRepository
                .findById(1);

        assertThat(storedIngredient).isPresent();
    }

    @Test
    void findIngredientByIndexShouldFailForIdTooHigh() {
        Optional<Ingredient> storedIngredient = ingredientRepository
                .findById(1000);

        assertThat(storedIngredient).isEmpty();
    }

    @Test
    void findIngredientByName() {
        Optional<Ingredient> storedIngredientOpt = ingredientRepository.
                findByName("vejce");

        assertThat(storedIngredientOpt).isPresent();
        var storedIngredient = storedIngredientOpt.orElseThrow();
        assertThat(storedIngredient.getId()).isNotNull();
        assertThat(storedIngredient.getName()).isEqualTo("vejce");
        assertThat(storedIngredient.getKcal()).isEqualTo(151.0);
        assertThat(storedIngredient.getUnit()).isEqualTo(unitRepository.findByName("pc(s)").orElseThrow());

    }

    @Test
    void findIngredientByNameShouldFailForNonExistingName() {
        Optional<Ingredient> storedIngredient = ingredientRepository.
                findByName("non existing ingredient");

        assertThat(storedIngredient).isEmpty();
    }

    @Test
    void createIngredient() {
        final String name = "new ingredient";
        final double kcal = 150.0;
        final Unit unit = unitRepository.findByIndex(unitRepository.getSize() - 1).orElseThrow();
        Ingredient ingredientToCreate = new Ingredient(name, kcal, unit);
        ingredientRepository.create(ingredientToCreate);

        Ingredient storedIngredient = ingredientRepository
                .findByIndex(ingredientRepository.getSize() - 1)
                .orElseThrow(); // last

        assertThat(storedIngredient.getId()).isNotNull();
        assertThat(storedIngredient.getName()).isEqualTo(ingredientToCreate.getName());
        assertThat(storedIngredient.getKcal()).isEqualTo(ingredientToCreate.getKcal());
        assertThat(storedIngredient.getUnit()).isEqualTo(ingredientToCreate.getUnit());
    }

    @Test
    void createExistingIngredient() {
        // test data should contain at least one ingredient
        assertThat(ingredientRepository.findAll()).isNotEmpty();
        final Ingredient existing = ingredientRepository.findAll().get(0);
        final Optional<Unit> existingUnit = unitRepository.findById(existing.getUnit().getId());
        assertThat(existingUnit).isPresent();

        // creation should throw error
        assertThatThrownBy(() -> ingredientRepository.create(existing))
            .isInstanceOf(DataStorageException.class)
            .hasMessageContaining("Failed to store:");

        // check for no changes
        final Optional<Ingredient> check = ingredientRepository.findById(existing.getId());
        assertThat(check).isPresent();
        assertThat(check.get().getId()).isEqualTo(existing.getId());
        assertThat(check.get()).isEqualTo(existing);

        final Optional<Unit> checkUnit = unitRepository.findById(existingUnit.get().getId());
        assertThat(checkUnit).isPresent();
        assertThat(checkUnit.get().getId()).isEqualTo(existingUnit.get().getId());
        assertThat(checkUnit.get()).isEqualTo(existingUnit.get());
    }

    @Test
    void createIngredientWithNonExistentUnit() {
        final String name = "new ingredient";
        final String unitName = "non-existent unit";
        final double kcal = 150.0;
        final Unit unit = new Unit(unitName, 10d, null);

        Ingredient ingredientToCreate = new Ingredient(name, kcal, unit);

        assertThatThrownBy(() -> ingredientRepository.create(ingredientToCreate))
            .isInstanceOf(NullPointerException.class);

        assertThat(ingredientRepository.findByName(name)).isEmpty();
        assertThat(unitRepository.findByName(unitName)).isEmpty();
    }

    @Test
    void updateIngredient() {
        var ingredient = ingredientRepository.findAll()
                .stream()
                .filter(e -> e.getName().equals("jahody"))
                .findFirst()
                .orElseThrow();

        ingredient.setName("update ingredient name");
        ingredient.setKcal(1333.42);
        ingredient.setUnit(unitRepository.findByName("ml").orElseThrow());
        ingredientRepository.update(ingredient);

        assertThat(ingredientRepository.findByName("jahody")).isEmpty();
        Ingredient updatedIngredient = ingredientRepository.findById(ingredient.getId()).orElseThrow();

        assertThat(updatedIngredient.getId()).isEqualTo(ingredient.getId());
        assertThat(updatedIngredient.getName()).isEqualTo("update ingredient name");
        assertThat(updatedIngredient.getKcal()).isEqualTo(1333.42);
        assertThat(updatedIngredient.getUnit()).isEqualTo(unitRepository.findByName("ml").orElseThrow());
    }

    @Test
    void updateNullIdIngredient() {
        assertThatThrownBy(() -> ingredientRepository.update(
            new Ingredient(null, "SOME NAME", 10d, new Unit("some unit", 10, BaseUnitsEnum.GRAM))
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    void updateNonExistentIngredient() {
        long id = 1;
        while (ingredientRepository.findById(id).isPresent()) {
            id++;
        }
        final Unit unit = unitRepository.findByIndex(0).orElseThrow();
        final Ingredient ingredient = new Ingredient(id, "some ingredient", 10d, unit);

        assertThatThrownBy(() -> ingredientRepository.update(ingredient))
            .isInstanceOf(DataStorageException.class)
            .hasMessageContaining("Failed to update non-existing");

        assertThat(unitRepository.findByIndex(0).orElseThrow()).isEqualTo(unit);
    }

    @Test
    void deleteIngredientReferenced() {
        var ingredients = ingredientRepository.findAll();
        for (int i = 0 ; i < ingredients.size() ; i++ ) {
            if (ingredients.get(i).getName().equals("červené víno")) {
                int deleteIndex = i; // lambda
                String errorIndex =  String.valueOf(deleteIndex + 1);
                assertThatThrownBy(() -> ingredientRepository.deleteByIndex(deleteIndex))
                        .isInstanceOf(DataStorageException.class)
                        .hasMessageContaining("Failed to delete ingredient " + errorIndex);
                return;
            }
        }
    }

    @Test
    void deleteIngredientNonReferenced() {
        var ingredients = ingredientRepository.findAll();
        for (int i = 0 ; i < ingredients.size() ; i++ ) {
            if (ingredients.get(i).getName().equals("hrášek")) {
                int deleteIndex = i;
                assertThatNoException().isThrownBy(() -> ingredientRepository.deleteByIndex(deleteIndex));
                assertThat(ingredientRepository.findByName("hrášek")).isEmpty();
                return;
            }
        }
    }
}
