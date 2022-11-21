package cz.muni.fi.pv168.data.manipulation.services;

import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.repository.RecipeRepository;
import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.model.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author Radim Stejskal, Jan Martinek
 */
public class RecipeService extends ServiceImpl<Recipe> {

    private Repository<Category> categories;
    private Repository<Ingredient> ingredients;
    private Repository<Unit> units;

    public RecipeService(Repository<Recipe> recipes,
                         Repository<Category> categories,
                         Repository<Ingredient> ingredients,
                         Repository<Unit> units,
                         Supplier<TransactionHandler> transactions
    ) {
        super(recipes, transactions);
        this.categories = Objects.requireNonNull(categories);
        this.ingredients = Objects.requireNonNull(ingredients);
    }

    @Override
    public int[] saveRecords(Collection<Recipe> records) {
        var rIngredient = records.stream().map(Recipe::getIngredients).flatMap(List::stream).toList();
        Collection<Category> categoryRecords = records.stream().map(Recipe::getCategory).toList();
        Collection<Ingredient> ingredientRecords = rIngredient.stream().map(RecipeIngredient::getIngredient).toList();
        Collection<Unit> baseUnitRecords = rIngredient.stream().map(RecipeIngredient::getIngredient).map(Ingredient::getUnit).toList();
        Collection<Unit> unitRecords = rIngredient.stream().map(RecipeIngredient::getUnit).filter(e -> !baseUnitRecords.contains(e)).toList();

        Collection<Recipe> exRecipes = getDuplicates(records, repository);
        Collection<Category> exCategories = getDuplicates(categoryRecords, categories);
        Collection<Ingredient> exIngredients = getDuplicates(ingredientRecords, ingredients);
        Collection<Unit> exBaseUnits = getDuplicates(baseUnitRecords, units);
        Collection<Unit> exUnits = getDuplicates(unitRecords, units);

        int imported = 0;
        int total = exRecipes.size() + exCategories.size() + exUnits.size() + exBaseUnits.size() + exIngredients.size();
        boolean replace = (total > 0) ? getDecision() : false;

        try (var transaction = transactions.get()) {
            imported += doImport(categoryRecords, exCategories, replace, categories, transaction::connection);
            imported += doImport(baseUnitRecords, exBaseUnits, replace, units, transaction::connection);
            imported += doImport(unitRecords, exUnits, replace, units, transaction::connection);
            imported += doImport(ingredientRecords, exIngredients, replace, ingredients, transaction::connection);
            imported += doImport(records, exRecipes, replace, repository, transaction::connection);
            transaction.commit();
        }
        return (replace)
            ? new int[]{imported, -total}
            : new int[]{imported, total};
    }

    protected static void create(Recipe entity, Repository<Recipe> repository, Supplier<ConnectionHandler> connection) {
        repository.uncomitted(entity, ((RecipeRepository) repository)::createUncommited, connection);
    }

    protected static void update(Recipe entity, Repository<Recipe> repository, Supplier<ConnectionHandler> connection) {
        repository.uncomitted(entity, ((RecipeRepository) repository)::updateUncommited, connection);
    }
}
