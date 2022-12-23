package cz.muni.fi.pv168.data.storage.dao;

import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.data.storage.entity.RecipeEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

final class RecipeDaoTest {
    private DatabaseManager manager;
    private RecipeDao recipeDao;
    private CategoryDao categoryDao;

    private RecipeIngredientDao recipeIngredientDao;


    @BeforeEach
    void setUp() {
        this.manager = DatabaseManager.createTestInstance(true);
        this.recipeDao = new RecipeDao(manager::getConnectionHandler);
        this.categoryDao = new CategoryDao(manager::getConnectionHandler);
        this.recipeIngredientDao = new RecipeIngredientDao(manager::getConnectionHandler);
    }

    @AfterEach
    void tearDown() {
        manager.destroySchema();
    }

    @Test
    void listAllRecipes() {
        var allRecipes = recipeDao.findAll();
        assertThat(allRecipes)
                .as("There should be 5 testing recipes")
                .hasSize(5);
    }

    @Test
    void createNewRecipe() {
        var categoryId = categoryDao.findById(1).orElseThrow().id();
        var params = new RecipeEntity("testing recipe",
                "something idk",
                categoryId,
                3,
                40,
                "1.) Do this, 2.) Do this i guess, 3.) the End"
        );
        var createdRecipe = recipeDao.create(params);
        assertThat(createdRecipe)
                .as("There should be a new recipe created")
                .isNotNull();
        assertThat(createdRecipe.id())
                .isGreaterThan(1);
        assertThat(createdRecipe.name()).isEqualTo(params.name());
        assertThat(createdRecipe.description()).isEqualTo(params.description());
        assertThat(createdRecipe.categoryId()).isEqualTo(params.categoryId());
        assertThat(createdRecipe.portions()).isEqualTo(params.portions());
        assertThat(createdRecipe.duration()).isEqualTo(params.duration());
        assertThat(createdRecipe.instruction()).isEqualTo(params.instruction());

        var foundCreated = recipeDao.findById(createdRecipe.id());
        assertThat(foundCreated)
                .as("Created recipe should be present")
                .isPresent()
                .contains(createdRecipe);
    }
    @Test
    void findExistingRecipe() {
        final long id = 1;
        var existingRecipeOpt = recipeDao.findById(id);
        assertThat(existingRecipeOpt)
                .isPresent();
    }

    @Test
    void deleteRecipeWithCascade() {
        var existingRecipeOpt = recipeDao.findById(1);
        assertThat(existingRecipeOpt)
                .isPresent();
        var existingRecipe = existingRecipeOpt.orElseThrow();
        assertThatNoException().isThrownBy(() -> recipeDao.deleteById(existingRecipe.id()));
        var deletedRecipe = recipeDao.findById(existingRecipe.id());
        assertThat(deletedRecipe).isEmpty();
        var deletedRecipeIngredients = recipeIngredientDao.findByRecipeId(existingRecipe.id());
        assertThat(deletedRecipeIngredients).isEmpty();
    }

    @Test
    void updateRecipe() {
        var existingRecipeOpt = recipeDao.findById(1);
        assertThat(existingRecipeOpt)
                .isPresent();
        var existingRecipe = existingRecipeOpt.orElseThrow();
        final String newName = "new update name recipe";
        final String newDescription = "new description something";
        final long newCategoryId = categoryDao.findByName("mexické jídla").orElseThrow().id();
        final long newPortions = 13L;
        final long newDuration = 133L;
        final String newInstructions = "1.) TOTO NIKDY NEDELEJ, 2.) KONEC.";
        var newRecipe = new RecipeEntity(
                existingRecipe.id(),
                newName,
                newDescription,
                newCategoryId,
                newPortions,
                newDuration,
                newInstructions
        );
        assertThatNoException().isThrownBy(() -> recipeDao.update(newRecipe));
        var updatedRecipeOpt = recipeDao.findById(existingRecipe.id());
        assertThat(updatedRecipeOpt)
                .isPresent();

        var updatedRecipe = updatedRecipeOpt.orElseThrow();

        assertThat(updatedRecipe.id()).isEqualTo(existingRecipe.id());
        assertThat(updatedRecipe.name()).isEqualTo(newName);
        assertThat(updatedRecipe.description()).isEqualTo(newDescription);
        assertThat(updatedRecipe.categoryId()).isEqualTo(newCategoryId);
        assertThat(updatedRecipe.portions()).isEqualTo(newPortions);
        assertThat(updatedRecipe.duration()).isEqualTo(newDuration);
        assertThat(updatedRecipe.instruction()).isEqualTo(newInstructions);



    }
}
