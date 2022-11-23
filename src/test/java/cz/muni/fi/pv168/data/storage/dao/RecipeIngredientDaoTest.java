package cz.muni.fi.pv168.data.storage.dao;

import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.data.storage.entity.RecipeIngredientEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

public class RecipeIngredientDaoTest {
    private DatabaseManager manager;
    private RecipeIngredientDao recipeIngredientDao;
    private RecipeDao recipeDao;
    private IngredientDao ingredientDao;
    private UnitDao unitDao;

    @BeforeEach
    void setUp() {
        this.manager = DatabaseManager.createTestInstance(true);
        this.recipeIngredientDao = new RecipeIngredientDao(manager::getConnectionHandler);
        this.recipeDao = new RecipeDao(manager::getConnectionHandler);
        this.ingredientDao = new IngredientDao(manager::getConnectionHandler);
        this.unitDao = new UnitDao(manager::getConnectionHandler);
    }

    @AfterEach
    void tearDown() {
        manager.destroySchema();
    }

    @Test
    void listAllRecipeIngredients() {
        var allRecipeIngredients = recipeIngredientDao.findAll();
        assertThat(allRecipeIngredients)
                .as("There should be 14 testing recipe ingredients")
                .hasSize(14);
    }

    @Test
    void listAllRecipeIngredientsByRecipeId() {
        long recipeId = recipeDao.findByName("Špagety s kuřecím masem").orElseThrow().id();
        var allRecipeIngredients = recipeIngredientDao.findByRecipeId(recipeId).orElseThrow();
        assertThat(allRecipeIngredients)
                .as("There should be 4 recipe ingredients in recipe id=" + recipeId)
                .hasSize(4);
    }

    @Test
    void createRecipeIngredient() {
        final long recipeId = recipeDao.findByName("Špagety s kuřecím masem").orElseThrow().id();
        final long ingredientId = ingredientDao.findByName("sůl").orElseThrow().id();
        final long unitId = unitDao.findByName("g").orElseThrow().id();
        var params = new RecipeIngredientEntity(recipeId,
                ingredientId,
                3.14,
                unitId
        );
        var createdRecipeIngredient = recipeIngredientDao.create(params);
        assertThat(createdRecipeIngredient)
                .as("There should be a new recipe ingredient created")
                .isNotNull();
        assertThat(createdRecipeIngredient.id())
                .isGreaterThan(1);
        assertThat(createdRecipeIngredient.recipeId()).isEqualTo(params.recipeId());
        assertThat(createdRecipeIngredient.ingredientId()).isEqualTo(params.ingredientId());
        assertThat(createdRecipeIngredient.amount()).isEqualTo(params.amount());
        assertThat(createdRecipeIngredient.unitId()).isEqualTo(params.unitId());

        var foundCreated = recipeIngredientDao.findById(createdRecipeIngredient.id());
        assertThat(foundCreated)
                .as("Created recipe ingredient should be present")
                .isPresent()
                .contains(createdRecipeIngredient);
    }

    @Test
    void findExistingRecipeIngredient() {
        final long id = 1;
        var existingRecipeIngredientOpt = recipeDao.findById(id);
        assertThat(existingRecipeIngredientOpt)
                .isPresent();
    }

    @Test
    void deleteRecipeIngredient() {
        var existingRecipeIngredientOpt = recipeIngredientDao.findById(1);
        assertThat(existingRecipeIngredientOpt)
                .isPresent();
        var existingRecipeIngredient = existingRecipeIngredientOpt.orElseThrow();
        assertThatNoException().isThrownBy(() -> recipeDao.deleteById(existingRecipeIngredient.id()));
        var deletedRecipe = recipeDao.findById(existingRecipeIngredient.id());
        assertThat(deletedRecipe).isEmpty();
    }

    @Test
    void updateRecipeIngredient() {
        var existingRecipeIngredientOpt = recipeIngredientDao.findByRecipeId(recipeDao.findByName("Špagety s kuřecím masem").orElseThrow().id());
        assertThat(existingRecipeIngredientOpt)
                .isPresent();
        var existingRecipeIngredient = existingRecipeIngredientOpt.orElseThrow().get(0); // first ingredient
        final long newIngredientId = ingredientDao.findByName("kukuřičné lupínky").orElseThrow().id();
        final double newAmount = 6.28;
        final long newUnitId = unitDao.findByName("dkg").orElseThrow().id();
        var newRecipeIngredient = new RecipeIngredientEntity(
                existingRecipeIngredient.id(),
                existingRecipeIngredient.recipeId(),
                newIngredientId,
                newAmount,
                newUnitId
        );
        assertThatNoException().isThrownBy(() -> recipeIngredientDao.update(newRecipeIngredient));
        var updatedRecipeIngredientOpt = recipeIngredientDao.findById(existingRecipeIngredient.id());
        assertThat(updatedRecipeIngredientOpt)
                .isPresent();

        var updatedRecipeIngredient = updatedRecipeIngredientOpt.orElseThrow();

        assertThat(updatedRecipeIngredient.id()).isEqualTo(existingRecipeIngredient.id());
        assertThat(updatedRecipeIngredient.recipeId()).isEqualTo(updatedRecipeIngredient.recipeId());
        assertThat(updatedRecipeIngredient.ingredientId()).isEqualTo(newIngredientId);
        assertThat(updatedRecipeIngredient.amount()).isEqualTo(newAmount);
        assertThat(updatedRecipeIngredient.unitId()).isEqualTo(newUnitId);
    }
}
