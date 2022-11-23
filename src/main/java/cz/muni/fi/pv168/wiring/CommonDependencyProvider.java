package cz.muni.fi.pv168.wiring;

import java.util.Objects;
import java.util.function.Supplier;

import cz.muni.fi.pv168.data.manipulation.JsonExporter;
import cz.muni.fi.pv168.data.manipulation.JsonExporterImpl;
import cz.muni.fi.pv168.data.manipulation.JsonImporter;
import cz.muni.fi.pv168.data.manipulation.JsonImporterImpl;
import cz.muni.fi.pv168.data.manipulation.services.IngredientService;
import cz.muni.fi.pv168.data.manipulation.services.RecipeService;
import cz.muni.fi.pv168.data.manipulation.services.Service;
import cz.muni.fi.pv168.data.manipulation.services.ServiceImpl;
import cz.muni.fi.pv168.data.storage.dao.*;
import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
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

    private final Service<Recipe> recipeService;
    private final Service<Ingredient> ingredientService;
    private final Service<Category> categoryService;
    private final Service<Unit> unitService;

    protected CommonDependencyProvider(DatabaseManager databaseManager) {
        this.databaseManager = Objects.requireNonNull(databaseManager);

        // repositories
<<<<<<< src/main/java/cz/muni/fi/pv168/wiring/CommonDependencyProvider.java
        categories = new CategoryRepository(
            new CategoryDao(databaseManager::getConnectionHandler),
            new CategoryMapper(categoryValidator)
        );

        var unitDao = new UnitDao(databaseManager::getConnectionHandler);

        units = new UnitRepository(
            unitDao,
            new UnitMapper(unitValidator, unitDao::findById)
        );


        ingredients = new IngredientRepository(
            new IngredientDao(databaseManager::getConnectionHandler),
            new IngredientMapper(ingredientValidator, units::findById)
        );

        var recipeDao = new RecipeDao(databaseManager::getConnectionHandler);

        recipes = new RecipeRepository(
            recipeDao,
            new RecipeMapper(recipeValidator, categories::findById),
            new RecipeIngredientDao(databaseManager::getConnectionHandler),
            new RecipeIngredientMapper(units::findById, ingredients::findById, recipeDao::findById),
            databaseManager::getConnectionHandler,
            databaseManager::getTransactionHandler
        );
        // databaseManager.initData("test");
=======
        categories = newCategoryRepository(null);
        units = newUnitRepository(null);
        ingredients = newIngredientRepository(null, units);
        recipes = newRecipeRepository(null, null, categories, units, ingredients);
>>>>>>> src/main/java/cz/muni/fi/pv168/wiring/CommonDependencyProvider.java

        categoryService = new ServiceImpl<>(categories, databaseManager::getTransactionHandler);
        unitService = new ServiceImpl<>(units, databaseManager::getTransactionHandler);
        ingredientService = new IngredientService(ingredients, units, databaseManager::getTransactionHandler);
        recipeService = new RecipeService(recipes, categories, ingredients, units, databaseManager::getTransactionHandler);
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
        return recipeService;
    }

    @Override
    public Service<Category> getCategoryService() {
        return categoryService;
    }

    @Override
    public Service<Ingredient> getIngredientService() {
        return ingredientService;
    }

    @Override
    public Service<Unit> getUnitService() {
        return unitService;
    }

    @Override
    public Service<?> getService(String name) {
        return switch (name) {
            case Supported.CATEGORY -> categoryService;
            case Supported.UNIT -> unitService;
            case Supported.INGREDIENT -> ingredientService;
            case Supported.RECIPE -> recipeService;
            default -> recipeService;
        };
    }

    public static JsonExporter getJsonExporter() {
        return new JsonExporterImpl();
    }

    public static JsonImporter getJsonImporter() {
        return new JsonImporterImpl();
    }


    private Supplier<ConnectionHandler> getConnection(Supplier<ConnectionHandler> connection) {
        return (connection == null) ? databaseManager::getConnectionHandler : connection;
    }

    private Supplier<TransactionHandler> getTransactions(Supplier<TransactionHandler> transactions) {
        return (transactions == null) ? databaseManager::getTransactionHandler : transactions;
    }

    private Repository<Category> newCategoryRepository(Supplier<ConnectionHandler> connection) {
        return new CategoryRepository(
            new CategoryDao(getConnection(connection)),
            new CategoryMapper(new CategoryValidator())
        );
    }

    private Repository<Unit> newUnitRepository(Supplier<ConnectionHandler> connection) {
        var dao = new UnitDao(getConnection(connection));
        return new UnitRepository(
            dao,
            new UnitMapper(new UnitValidator(), dao::findById)
        );
    }


    private Repository<Ingredient> newIngredientRepository(
        Supplier<ConnectionHandler> connection,
        Repository<Unit> unitRepository
    ) {
        return new IngredientRepository(
            new IngredientDao(getConnection(connection)),
            new IngredientMapper(new IngredientValidator(), unitRepository::findById)
        );
    }

    private Repository<Recipe> newRecipeRepository(
        Supplier<ConnectionHandler> connection,
        Supplier<TransactionHandler> transactions,
        Repository<Category> categoryRepository,
        Repository<Unit> unitRepository,
        Repository<Ingredient> ingredientRepository
    ) {
        var dao = new RecipeDao(getConnection(connection));
        return new RecipeRepository(
            dao,
            new RecipeMapper(new RecipeValidator(), categoryRepository::findById),
            new RecipeIngredientDao(getConnection(connection)),
            new RecipeIngredientMapper(unitRepository::findById, ingredientRepository::findById, dao::findById),
            getTransactions(transactions)
        );
    }
}
