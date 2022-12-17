package cz.muni.fi.pv168.wiring;

import java.util.Objects;

import cz.muni.fi.pv168.data.manipulation.Importer;
import cz.muni.fi.pv168.data.manipulation.JsonImporter;
import cz.muni.fi.pv168.data.manipulation.importers.*;
import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.data.storage.repository.*;
import cz.muni.fi.pv168.model.*;

/**
 * Dependency provider common for all environments
 *
 * @author Jan Martinek
 */
public abstract class CommonDependencyProvider implements DependencyProvider {

    private final DatabaseManager databaseManager;
    private final RepositoryFactory rFactory;

    private final Repository<Recipe> recipes;
    private final Repository<Category> categories;
    private final Repository<Ingredient> ingredients;
    private final Repository<Unit> units;

    protected CommonDependencyProvider(DatabaseManager databaseManager) {
        this.databaseManager = Objects.requireNonNull(databaseManager);

        rFactory = new RepositoryFactory(this.databaseManager::getConnectionHandler);

        categories = rFactory.buildCategoryRepository();
        units = rFactory.buildUnitRepository();
        ingredients = rFactory.buildIngredientRepository(units);
        recipes = rFactory.buildRecipeRepository(this.databaseManager::getTransactionHandler, categories, units, ingredients);
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
    public Importer getCategoryImporter() {
        return new JsonImporter<>(Category.class, new CategoryImporter(
            connection -> new RepositoryFactory(connection).buildCategoryRepository(),
            databaseManager::getTransactionHandler
        ));
    }

    @Override
    public Importer getUnitImporter() {
        return new JsonImporter<>(Unit.class, new UnitImporter(
            connection -> new RepositoryFactory(connection).buildUnitRepository(),
            databaseManager::getTransactionHandler
        ));
    }

    @Override
    public Importer getIngredientImporter() {
        return new JsonImporter<>(Ingredient.class, new IngredientImporter(
            new RepositoryFactory(databaseManager::getConnectionHandler),
            databaseManager::getTransactionHandler
        ));
    }

    @Override
    public Importer getRecipeImporter() {
        return new JsonImporter<>(Recipe.class, new RecipeImporter(
            new RepositoryFactory(databaseManager::getConnectionHandler),
            databaseManager::getTransactionHandler
        ));
    }
}
