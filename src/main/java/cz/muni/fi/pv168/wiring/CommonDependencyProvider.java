package cz.muni.fi.pv168.wiring;

import java.util.Objects;
import java.util.function.Supplier;

import cz.muni.fi.pv168.data.manipulation.JsonExporter;
import cz.muni.fi.pv168.data.manipulation.JsonExporterImpl;
import cz.muni.fi.pv168.data.manipulation.JsonImporter;
import cz.muni.fi.pv168.data.manipulation.JsonImporterImpl;
import cz.muni.fi.pv168.data.manipulation.importers.IngredientImporter;
import cz.muni.fi.pv168.data.manipulation.importers.RecipeImporter;
import cz.muni.fi.pv168.data.manipulation.importers.ObjectImporter;
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

    protected CommonDependencyProvider(DatabaseManager databaseManager) {
        this.databaseManager = Objects.requireNonNull(databaseManager);

        // repositories
        categories = newCategoryRepository(null);
        units = newUnitRepository(null);
        ingredients = newIngredientRepository(null, units);
        recipes = newRecipeRepository(null, null, categories, units, ingredients);
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public Repository<Category> getCategoryRepository() {
        return categories;
    }

    @Override
    public Repository<Unit> getUnitRepository() {
        return units;
    }

    @Override
    public Repository<Ingredient> getIngredientRepository() {
        return ingredients;
    }

    @Override
    public Repository<Recipe> getRecipeRepository() {
        return recipes;
    }

    @Override
    public ObjectImporter<Category> getCategoryImporter() {
        return new ObjectImporter<>(
            this::newCategoryRepository,
            databaseManager::getTransactionHandler
        );
    }

    @Override
    public ObjectImporter<Unit> getUnitImporter() {
        return new ObjectImporter<>(
            this::newUnitRepository,
            databaseManager::getTransactionHandler
        );
    }

    @Override
    public ObjectImporter<Ingredient> getIngredientImporter() {
        return new IngredientImporter(
            this::newUnitRepository,
            this::newIngredientRepository,
            databaseManager::getTransactionHandler
        );
    }

    @Override
    public ObjectImporter<Recipe> getRecipeImporter() {
        return new RecipeImporter(
            this::newCategoryRepository,
            this::newUnitRepository,
            this::newIngredientRepository,
            this::newRecipeRepository,
            databaseManager::getTransactionHandler
        );
    }

    public static JsonExporter getJsonExporter() {
        return new JsonExporterImpl();
    }

    public static JsonImporter getJsonImporter() {
        return new JsonImporterImpl();
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
        Repository<Category> categoryRepository,
        Repository<Unit> unitRepository,
        Repository<Ingredient> ingredientRepository
    ) {
        return newRecipeRepository(
            connection,
            databaseManager::getTransactionHandler,
            categoryRepository,
            unitRepository,
            ingredientRepository
        );
    }

    private Repository<Recipe> newRecipeRepository(
        Supplier<ConnectionHandler> connection,
        Supplier<TransactionHandler> transactions,
        Repository<Category> categoryRepository,
        Repository<Unit> unitRepository,
        Repository<Ingredient> ingredientRepository
    ) {
        return new RecipeRepository(
            new RecipeDao(getConnection(connection)),
            new RecipeMapper(new RecipeValidator(), categoryRepository::findById),
            new RecipeIngredientDao(getConnection(connection)),
            new RecipeIngredientMapper(unitRepository::findById, ingredientRepository::findById),
            getTransactions(transactions)
        );
    }

    private Supplier<ConnectionHandler> getConnection(Supplier<ConnectionHandler> connection) {
        return (connection == null) ? databaseManager::getConnectionHandler : connection;
    }

    private Supplier<TransactionHandler> getTransactions(Supplier<TransactionHandler> transactions) {
        return (transactions == null) ? databaseManager::getTransactionHandler : transactions;
    }
}
