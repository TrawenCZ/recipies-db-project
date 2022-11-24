package cz.muni.fi.pv168.data.storage.repository;

import cz.muni.fi.pv168.data.storage.DataStorageException;
import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
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
    private Repository<Unit> unitRepositry;

    @BeforeEach
    void setUp() {
        this.databaseManager = DatabaseManager.createTestInstance(true);
        var dependencyProvider = new TestDependencyProvider(databaseManager);
        this.ingredientRepository = dependencyProvider.getIngredientRepository();
        this.unitRepositry = dependencyProvider.getUnitRepository();
    }

    @AfterEach
    void tearDown() {
        databaseManager.destroySchema();
    }

    @Test
    void createNewIngredient() {
        final String name = "new ingredient";
        final double kcal = 150.0;
        final Unit unit = unitRepositry.findByIndex(unitRepositry.getSize() - 1).orElseThrow();
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
        assertThat(storedIngredient.getUnit()).isEqualTo(unitRepositry.findByName("pc(s)").orElseThrow());

    }

    @Test
    void findIngredientByNameShouldFailForNonExistingName() {
        Optional<Ingredient> storedIngredient = ingredientRepository.
                findByName("non existing ingredient");

        assertThat(storedIngredient).isEmpty();
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
        ingredient.setUnit(unitRepositry.findByName("ml").orElseThrow());
        ingredientRepository.update(ingredient);

        assertThat(ingredientRepository.findByName("jahody")).isEmpty();
        Ingredient updatedIngredient = ingredientRepository.findById(ingredient.getId()).orElseThrow();

        assertThat(updatedIngredient.getId()).isEqualTo(ingredient.getId());
        assertThat(updatedIngredient.getName()).isEqualTo("update ingredient name");
        assertThat(updatedIngredient.getKcal()).isEqualTo(1333.42);
        assertThat(updatedIngredient.getUnit()).isEqualTo(unitRepositry.findByName("ml").orElseThrow());
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
