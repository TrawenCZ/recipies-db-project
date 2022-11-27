package cz.muni.fi.pv168.data.storage.repository;

import cz.muni.fi.pv168.data.storage.DataStorageException;
import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.model.*;
import cz.muni.fi.pv168.wiring.TestDependencyProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

final class RecipeRepositoryTest {
    private DatabaseManager databaseManager;
    private Repository<Category> categoryRepository;
    private Repository<Ingredient> ingredientRepository;
    private Repository<Unit> unitRepository;
    private Repository<Recipe> recipeRepository;

    @BeforeEach
    void setUp() {
        this.databaseManager = DatabaseManager.createTestInstance(true);
        var dependencyProvider = new TestDependencyProvider(databaseManager);
        this.ingredientRepository = dependencyProvider.getIngredientRepository();
        this.unitRepository = dependencyProvider.getUnitRepository();
        this.categoryRepository = dependencyProvider.getCategoryRepository();
        this.recipeRepository = dependencyProvider.getRecipeRepository();
    }

    @AfterEach
    void tearDown() {
        databaseManager.destroySchema();
    }

    @Test
    void listAllTestingRecipes() {
        List<Recipe> recipes = recipeRepository
                .findAll();

        assertThat(recipes).hasSize(5);
    }

    @Test
    void findRecipeByIndex() {
        Optional<Recipe> storedRecipeOpt = recipeRepository
                .findByIndex(1);

        assertThat(storedRecipeOpt).isPresent();
    }

    @Test
    void findRecipeByIndexShouldFailForIndexTooHigh() {
        Optional<Recipe> storedRecipeOpt = recipeRepository
                .findByIndex(1000);

        assertThat(storedRecipeOpt).isEmpty();
    }

    @Test
    void findRecipeById() {
        Optional<Recipe> storedRecipeOpt = recipeRepository
                .findById(1);

        assertThat(storedRecipeOpt).isPresent();
    }

    @Test
    void findRecipeByIndexShouldFailForIdTooHigh() {
        Optional<Recipe> storedRecipeOpt = recipeRepository
                .findById(1000);

        assertThat(storedRecipeOpt).isEmpty();
    }

    @Test
    void findRecipeByName() {
        Optional<Recipe> storedRecipeOpt = recipeRepository
                .findByName("Játrová paštika domácí");

        assertThat(storedRecipeOpt).isPresent();

        Recipe storedRecipe = storedRecipeOpt.orElseThrow();
        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        recipeIngredients.add(new RecipeIngredient(
                ingredientRepository.findByName("červené víno").orElseThrow(),
                0.5,
                unitRepository.findByName("l").orElseThrow()
        ));
        recipeIngredients.add(new RecipeIngredient(
                ingredientRepository.findByName("vejce").orElseThrow(),
                1.0,
                unitRepository.findByName("pc(s)").orElseThrow())
        );
        final String name = "Játrová paštika domácí";
        final String description = "Játrová paštika namazaná na" +
                " plátku čerstvého chleba je velmi oblíbená " +
                "svačinka na rodinné výlety i během klidného " +
                "víkendu. Připravte si jednoduchou a jemnou " +
                "paštiku podle našeho receptu a překvapte své blízké.";
        final Category category = categoryRepository.findByName("pokrm").orElseThrow();
        final int portions = 1;
        final int duration = 210;
        final String instruction = """
                1.) Bůček rozkrojíme na menší kusy, prosolíme a pečeme 1 hodinu při 200 °C s cibulí rozkrojenou na čvtrtky.
                2.) Necháme zchladnout a umeleme na masovém strojku.
                3.) Přidáme 1 kg pomletých syrových jater.
                4.) Ochutíme novým kořením, pepřem, paštikovým kořením, promícháme a plníme do 3/4 do připravených sklenic.
                5.) Uzavřeme a sterilujeme při 95 °C 2 hodiny.""";
        Recipe knowRecipe = new Recipe(
                name,
                description,
                instruction,
                category,
                duration,
                portions,
                recipeIngredients
        );
        compareRecipeAssert(storedRecipe, knowRecipe, false);
    }

    @Test
    void findRecipeByNameShouldFailForNonExistingName() {
        Optional<Recipe> storedRecipeOpt = recipeRepository
                .findByName("non existing recipe");

        assertThat(storedRecipeOpt).isEmpty();
    }

    @Test
    void createRecipe() {
        final String name = "new recipe name";
        final String description = "Some random description text ...";
        final Category category = categoryRepository
                .findByIndex(categoryRepository.getSize() - 1)
                .orElseThrow();
        final int portions = 4;
        final int duration = 30;
        final String instructions = "1.) do this, 2.) Not this 3.), something, 4.) The end";
        final List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        recipeIngredients.add(new RecipeIngredient(
                ingredientRepository.findByName("vejce").orElseThrow(),
                50.0,
                unitRepository.findByName("tucet").orElseThrow()
        ));

        recipeIngredients.add(new RecipeIngredient(
                ingredientRepository.findByName("sůl").orElseThrow(),
                4.0,
                unitRepository.findByName("g").orElseThrow()
        ));
        Recipe recipe = new Recipe(name, description, instructions, category, duration, portions, recipeIngredients);
        recipeRepository.create(recipe);

        var storedRecipeOpt = recipeRepository
                .findByIndex(recipeRepository.getSize() - 1);
        assertThat(storedRecipeOpt).isPresent();
        Recipe storedRecipe = storedRecipeOpt.orElseThrow();
        compareRecipeAssert(storedRecipe, recipe, false);
    }

    @Test
    void createExistingRecipe() {
        // test data should contain at least one ingredient
        final Recipe existing = recipeRepository.findByIndex(0).orElseThrow();

        // creation should throw error
        assertThatThrownBy(() -> recipeRepository.create(existing))
            .isInstanceOf(DataStorageException.class)
            .hasMessageContaining("Failed to store:");

        compareRecipeAssert(recipeRepository.findByIndex(0).orElseThrow(), existing, true);
    }

    @Test
    void createRecipeWithNoIngredients() {
        final String name = "new Recipe Name something";
        final String description = "really cool, but not cool you know";
        final Category category = categoryRepository.findByIndex(0).orElseThrow();
        final String instruction = "easy PZ";

        final Recipe recipe = new Recipe(
            name,
            description,
            instruction,
            category,
            10,
            20,
            List.of()
        );

        assertThatThrownBy(() -> recipeRepository.create(recipe))
            .isInstanceOf(NoSuchElementException.class);

        assertThat(recipeRepository.findByName(name)).isEmpty();
        assertThat(categoryRepository.findByIndex(0).orElseThrow()).isEqualTo(category);
    }

    @Test
    void createRecipeWithNonExistentCategory() {

        final String name = "new Recipe Name something";
        final String description = "really cool, but not cool you know";
        final var ingredients = recipeRepository.findByIndex(0).orElseThrow().getIngredients();
        final String instruction = "easy PZ";

        long id = 1;
        while (categoryRepository.findById(id).isPresent()) {
            id++;
        }
        final Category category = new Category(id, "invalid category", new Color(0x00ffff));

        final Recipe recipe = new Recipe(
            name,
            description,
            instruction,
            category,
            10,
            20,
            ingredients
        );

        assertThatThrownBy(() -> recipeRepository.create(recipe))
            .isInstanceOf(DataStorageException.class);

        assertThat(recipeRepository.findByName(name)).isEmpty();
        assertThat(categoryRepository.findById(id)).isEmpty();
    }

    @Test
    void updateRecipe() {
        var recipe = recipeRepository.findAll()
                .stream()
                .filter(e -> e.getName().equals("Kuřecí stehna pečená s rýží z jednoho pekáčku"))
                .findFirst()
                .orElseThrow();


        final String name = "new Recipe Name something";
        final String description = "really cool, but not cool you know";
        final Category category = categoryRepository.findByName("vegan").orElseThrow();
        final int portions = 4;
        final int duration = 500;
        final String instruction = "easy PZ";
        recipe.setName(name);
        recipe.setDescription(description);
        recipe.setCategory(category);
        recipe.setPortions(portions);
        recipe.setRequiredTime(duration);
        recipe.setInstructions(instruction);
        var getIngredient = recipe.getIngredients().get(0);
        getIngredient.setIngredient(ingredientRepository.findByName("meloun kantalupe").orElseThrow());
        getIngredient.setAmount(51.45);
        getIngredient.setUnit(unitRepository.findByName("tuna").orElseThrow());
        recipe.getIngredients().add(new RecipeIngredient(
                ingredientRepository.findByName("máslo").orElseThrow(),
                41.2,
                unitRepository.findByName("g").orElseThrow()
        ));
        recipe.getIngredients().add(new RecipeIngredient(
                ingredientRepository.findByName("máslo").orElseThrow(),
                41.2,
                unitRepository.findByName("g").orElseThrow()
        )); // should work



        assertThatNoException().isThrownBy(() -> recipeRepository.update(recipe));

        assertThat(
                recipeRepository.findAll()
                .stream()
                .filter(e -> e.getName().equals("Kuřecí stehna pečená s rýží z jednoho pekáčku"))
                .findFirst()
        ).isEmpty();



        var updatedRecipeOpt = recipeRepository.findAll()
                .stream()
                .filter(e -> e.getName().equals(name))
                .findFirst();
        assertThat(updatedRecipeOpt).isPresent();
        Recipe updatedRecipe = updatedRecipeOpt.orElseThrow();
        compareRecipeAssert(updatedRecipe, recipe, true);
    }

    @Test
    void deleteRecipe() {
        var recipes = recipeRepository.findAll();
        for (int i = 0 ; i < recipes.size() ; i++ ) {
            if (recipes.get(i).getName().equals("Valašská kyselica ze Vsetína")) {
                int deleteIndex = i; // lambda
                assertThatNoException().isThrownBy(() -> recipeRepository.deleteByIndex(deleteIndex));
                assertThat(recipeRepository.findByName("Valašská kyselica ze Vsetína")).isEmpty();
                return;
            }
        }
    }
    private void compareRecipeAssert(
            Recipe recipeFromDb,
            Recipe localRecipe,
            boolean knowId
    ) {
        assertThat(recipeFromDb.getId()).isNotNull();
        if (knowId)
            assertThat(recipeFromDb.getId()).isEqualTo(localRecipe.getId());
        assertThat(recipeFromDb.getName()).isEqualTo(localRecipe.getName());
        assertThat(recipeFromDb.getDescription()).isEqualTo(localRecipe.getDescription());
        assertThat(recipeFromDb.getCategory()).isEqualTo(localRecipe.getCategory());
        assertThat(recipeFromDb.getPortions()).isEqualTo(localRecipe.getPortions());
        assertThat(recipeFromDb.getRequiredTime()).isEqualTo(localRecipe.getRequiredTime());
        assertThat(recipeFromDb.getInstructions()).isEqualTo(localRecipe.getInstructions());
        // -- RecipeIngredient check
        assertThat(recipeFromDb.getIngredients()).containsExactlyInAnyOrderElementsOf(localRecipe.getIngredients());
    }
}
