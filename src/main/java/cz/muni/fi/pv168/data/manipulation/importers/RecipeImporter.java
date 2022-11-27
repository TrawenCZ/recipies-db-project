package cz.muni.fi.pv168.data.manipulation.importers;

import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.repository.RecipeRepository;
import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.model.*;

import java.util.Collection;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Radim Stejskal, Jan Martinek
 */
public class RecipeImporter extends ObjectImporter<Recipe> {

    @FunctionalInterface
    public interface FFunction<A, B, C, D, R> {
        R apply(A a, B b, C c, D d);
    }

    private final Function<Supplier<ConnectionHandler>, Repository<Category>> categories;
    private final Function<Supplier<ConnectionHandler>, Repository<Unit>> units;
    private final BiFunction<Supplier<ConnectionHandler>, Repository<Unit>, Repository<Ingredient>> ingredients;
    private final FFunction<Supplier<ConnectionHandler>,
                            Repository<Category>,
                            Repository<Unit>,
                            Repository<Ingredient>,
                            Repository<Recipe>> recipes;

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
        Function<Supplier<ConnectionHandler>, Repository<Category>> categories,
        Function<Supplier<ConnectionHandler>, Repository<Unit>> units,
        BiFunction<Supplier<ConnectionHandler>, Repository<Unit>, Repository<Ingredient>> ingredients,
        FFunction<Supplier<ConnectionHandler>,
                           Repository<Category>,
                           Repository<Unit>,
                           Repository<Ingredient>,
                           Repository<Recipe>> recipes,
        Supplier<TransactionHandler> transactions
    ) {
        super(null, transactions);
        this.categories = Objects.requireNonNull(categories);
        this.units = Objects.requireNonNull(units);
        this.ingredients = Objects.requireNonNull(ingredients);
        this.recipes = Objects.requireNonNull(recipes);
    }

    @Override
    public int[] saveRecords(Collection<Recipe> records) {
        counter = new Counter();

        try (var transaction = setupTransaction()) {
            cateRepository = categories.apply(transaction::connection);
            unitRepository = units.apply(transaction::connection);
            ingrRepository = ingredients.apply(transaction::connection, unitRepository);
            repository = recipes.apply(
                transaction::connection,
                cateRepository,
                unitRepository,
                ingrRepository
            );
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
        doRecipeAction(recipe, ((RecipeRepository) repository)::createUncommitted);
    }

    private void doRecipeUpdate(Recipe recipe) {
        doRecipeAction(recipe, ((RecipeRepository) repository)::updateUncommitted);
    }

    private void setIds(Recipe recipe) {
        var category = recipe.getCategory();
        category.setId(cateRepository.findByName(category.getName()).orElseThrow().getId());

        var ingredients = recipe.getIngredients();
        ingredients.forEach(e -> e.getIngredient().setId(ingrRepository.findByName(e.getIngredient().getName()).orElseThrow().getId()));
        ingredients.forEach(e -> e.getUnit().setId(unitRepository.findByName(e.getUnit().getName()).orElseThrow().getId()));
    }

    private void doRecipeAction(Recipe recipe, Consumer<Recipe> action) {
        doImport(recipe.getCategory(), cateRepository);
        doIngredientsImport(recipe.getIngredients());
        setIds(recipe);
        action.accept(recipe);
    }

    private void doIngredientsImport(Collection<RecipeIngredient> rIngredients) {
        var ingredients = rIngredients.stream()
            .map(RecipeIngredient::getIngredient)
            .toList();

        doImport(Stream.concat(
                rIngredients.stream().map(RecipeIngredient::getUnit),
                ingredients.stream().map(Ingredient::getUnit)
            ).collect(Collectors.toSet()),
            unitRepository
        );

        ingredients.forEach(e -> e.getUnit().setId(unitRepository.findByName(e.getUnit().getName()).orElseThrow().getId()));

        doImport(ingredients, ingrRepository);
    }
}
