package cz.muni.fi.pv168.data.manipulation.importers;

import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.model.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Radim Stejskal, Jan Martinek
 */
public class RecipeImporter extends ObjectImporter<Recipe> {

    private final Function<Supplier<ConnectionHandler>, Repository<Category>> categories;
    private final Function<Supplier<ConnectionHandler>, Repository<Unit>> units;
    private final Function<Supplier<ConnectionHandler>, Repository<Ingredient>> ingredients;

    private Repository<Recipe> repository;
    private Repository<Category> cateRepository;
    private Repository<Unit> unitRepository;
    private Repository<Ingredient> ingrRepository;

    /**
     * Constructs the importer with a transaction supplier
     * and a repository supplier from supplied connections.
     *
     * @param recipes      recipe repository generator from connections
     * @param categories   category repository generator from connections
     * @param ingredients  ingredient repository generator from connections
     * @param units        unit repository generator from connections
     * @param transactions transaction supplier
     */
    public RecipeImporter(
        Function<Supplier<ConnectionHandler>, Repository<Recipe>> recipes,
        Function<Supplier<ConnectionHandler>, Repository<Category>> categories,
        Function<Supplier<ConnectionHandler>, Repository<Unit>> units,
        Function<Supplier<ConnectionHandler>, Repository<Ingredient>> ingredients,
        Supplier<TransactionHandler> transactions
    ) {
        super(recipes, transactions);
        this.categories = Objects.requireNonNull(categories);
        this.units = Objects.requireNonNull(units);
        this.ingredients = Objects.requireNonNull(ingredients);
    }

    @Override
    public int[] saveRecords(Collection<Recipe> records) {
        Counter counter = new Counter();

        try (var transaction = transactions.get()) {
            repository = repositories.apply(transaction::connection);
            cateRepository = categories.apply(transaction::connection);
            unitRepository = units.apply(transaction::connection);
            ingrRepository = ingredients.apply(transaction::connection);
            doImport(
                records,
                repository::findByName,
                e -> doRecipeCreate(e),
                e -> doRecipeUpdate(e)
            );
            transaction.commit();
        }
        return (counter.doReplace)
            ? new int[]{counter.imported, -counter.actioned}
            : new int[]{counter.imported, counter.actioned};
    }

    private void doRecipeCreate(Recipe recipe) {
        doCategoryImport(recipe.getCategory());
        doIngredientsImport(recipe.getIngredients());
        repository.create(recipe);
    }

    private void doRecipeUpdate(Recipe recipe) {
        doCategoryImport(recipe.getCategory());
        doIngredientsImport(recipe.getIngredients());
        repository.update(recipe);
    }

    private void doCategoryImport(Category category) {
        doImport(
            List.of(category),
            cateRepository::findByName,
            cateRepository::create,
            cateRepository::update
        );
    }

    private void doUnitsImport(Collection<Unit> units) {
        doImport(
            units,
            unitRepository::findByName,
            unitRepository::create,
            unitRepository::update
        );
    }

    private void doIngredientsImport(Collection<RecipeIngredient> rIngredients) {
        var ingredients = rIngredients.stream()
            .map(RecipeIngredient::getIngredient)
            .collect(Collectors.toSet());

        doUnitsImport(Stream.concat(
                rIngredients.stream().map(RecipeIngredient::getUnit),
                ingredients.stream().map(Ingredient::getUnit)
            ).collect(Collectors.toSet())
        );

        doImport(
            ingredients,
            ingrRepository::findByName,
            ingrRepository::create,
            ingrRepository::update
        );
    }
}
