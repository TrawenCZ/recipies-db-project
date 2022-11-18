package cz.muni.fi.pv168.wiring;

import java.util.Objects;

import cz.muni.fi.pv168.data.service.*;
import cz.muni.fi.pv168.data.storage.dao.*;
import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.data.storage.mapper.*;
import cz.muni.fi.pv168.data.storage.repository.*;
import cz.muni.fi.pv168.data.validation.*;
import cz.muni.fi.pv168.model.*;

/**
 * Dependency provider common for all environments
 *
 * @author Jan Martinek
 */
public abstract class CommonDependencyProvider implements DependencyProvider {

    private final DatabaseManager databaseManager;
    private final Repository<Recipe> recipes;
    private final Repository<Category> categories;
    private final Repository<Ingredient> ingredients;
    private final Repository<Unit> units;

    // TODO: uncomment
    // private final Service<Recipe> recipeService;
    // private final Service<Category> categoryService;
    // private final Service<Ingredient> ingredientService;
    // private final Service<Unit> unitService;

    protected CommonDependencyProvider(DatabaseManager databaseManager) {
        this.databaseManager = Objects.requireNonNull(databaseManager);

        Validator<Recipe> recipeValidator = new RecipeValidator();
        Validator<Category> categoryValidator = new CategoryValidator();
        Validator<Ingredient> ingredientValidator = new IngredientValidator();
        Validator<Unit> unitValidator = new UnitValidator();

        // repositories
        categories = new CategoryRepository(
            new CategoryDao(databaseManager::getConnectionHandler),
            new CategoryMapper(categoryValidator)
        );

        units = new UnitRepository(
            new UnitDao(databaseManager::getConnectionHandler),
            new UnitMapper(unitValidator)
        );

        ingredients = new IngredientRepository(
            new IngredientDao(databaseManager::getConnectionHandler),
            new IngredientMapper(ingredientValidator, units::findById)
        );

        var recipeIngredientDao = new RecipeIngredientDao(databaseManager::getConnectionHandler);
        var recipeIngredientMapper = new RecipeIngredientMapper(units::findById, ingredients::findById);

        recipes = new RecipeRepository(
            new RecipeDao(databaseManager::getConnectionHandler),
            new RecipeMapper(recipeValidator, categories::findById, recipeIngredientDao::findByRecipeId, recipeIngredientMapper),
            recipeIngredientDao,
            recipeIngredientMapper
        );

        // TODO: finish it, currently not taking transactions (idk if we even want to do it like that)
        // operational services
        // categoryService = new CategoryService(categories);
        // unitService = new UnitsService(units);
        // ingredientService = new IngredientService(ingredients, unitService);
        // recipeService = new RecipeService(recipes, unitService, categoryService, ingredientService);
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public Repository<Recipe> getRecipeRepository() {
        return recipes;
    }

    @Override
    public Repository<Category> getCategoryRepository() {
        return categories;
    }

    @Override
    public Repository<Ingredient> getIngredientRepository() {
        return ingredients;
    }

    @Override
    public Repository<Unit> getUnitRepository() {
        return units;
    }

    @Override
    public Service<Recipe> getRecipeService() {
        // TODO: uncomment
        return null; //recipeService;
    }

    @Override
    public Service<Category> getCategoryService() {
        // TODO: uncomment
        return null; //categoryService;
    }

    @Override
    public Service<Ingredient> getIngredientService() {
        // TODO: uncomment
        return null; //ingredientService;
    }

    @Override
    public Service<Unit> getUnitService() {
        // TODO: uncomment
        return null; //unitService;
    }
}
