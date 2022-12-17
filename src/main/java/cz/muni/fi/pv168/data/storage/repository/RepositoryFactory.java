package cz.muni.fi.pv168.data.storage.repository;

import java.util.Objects;
import java.util.function.Supplier;

import cz.muni.fi.pv168.data.storage.dao.*;
import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.mapper.*;
import cz.muni.fi.pv168.data.validation.*;
import cz.muni.fi.pv168.model.*;

/**
 * Simplifies creation of repositories.
 *
 * @author Jan Martinek
 */
public class RepositoryFactory {

    private Supplier<ConnectionHandler> connection;

    public RepositoryFactory(Supplier<ConnectionHandler> connection) {
        setConnection(connection);
    }

    public void setConnection(Supplier<ConnectionHandler> connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    public Repository<Category> buildCategoryRepository() {
        return new CategoryRepository(
            new CategoryDao(connection),
            new CategoryMapper(new CategoryValidator())
        );
    }

    public Repository<Unit> buildUnitRepository() {
        var dao = new UnitDao(connection);
        return new UnitRepository(
            dao,
            new UnitMapper(new UnitValidator(), dao::findById)
        );
    }

    public Repository<Ingredient> buildIngredientRepository(Repository<Unit> unitRepository) {
        return new IngredientRepository(
            new IngredientDao(connection),
            new IngredientMapper(new IngredientValidator(), unitRepository::findById)
        );
    }

    public Repository<Recipe> buildRecipeRepository(
        Supplier<TransactionHandler> transactions,
        Repository<Category> categoryRepository,
        Repository<Unit> unitRepository,
        Repository<Ingredient> ingredientRepository
    ) {
        return new RecipeRepository(
            new RecipeDao(connection),
            new RecipeMapper(new RecipeValidator(), categoryRepository::findById),
            new RecipeIngredientDao(connection),
            new RecipeIngredientMapper(unitRepository::findById, ingredientRepository::findById),
            transactions
        );
    }
}
