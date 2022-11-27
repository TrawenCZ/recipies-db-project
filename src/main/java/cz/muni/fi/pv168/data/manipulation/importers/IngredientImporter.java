package cz.muni.fi.pv168.data.manipulation.importers;

import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.model.*;

import java.util.Collection;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Jan Martinek
 */
public final class IngredientImporter extends ObjectImporter<Ingredient> {

    private final BiFunction<Supplier<ConnectionHandler>, Repository<Unit>, Repository<Ingredient>> ingredients;
    private final Function<Supplier<ConnectionHandler>, Repository<Unit>> units;

    private Repository<Ingredient> ingrRepository;
    private Repository<Unit> unitRepository;

    /**
     * Constructs the importer with a transaction supplier
     * and a repository supplier from supplied connections.
     *
     * @param ingredients  ingredient repository generator from connections
     * @param units        unit repository generator from connections
     * @param transactions transaction supplier
     */
    public IngredientImporter(
        Function<Supplier<ConnectionHandler>, Repository<Unit>> units,
        BiFunction<Supplier<ConnectionHandler>, Repository<Unit>, Repository<Ingredient>> ingredients,
        Supplier<TransactionHandler> transactions
    ) {
        super(null, transactions);
        this.ingredients = Objects.requireNonNull(ingredients);
        this.units = Objects.requireNonNull(units);
    }

    @Override
    public int[] saveRecords(Collection<Ingredient> records) {
        counter = new Counter();

        try (var transaction = setupTransaction()) {
            unitRepository = units.apply(transaction::connection);
            ingrRepository = ingredients.apply(transaction::connection, unitRepository);
            doImport(
                records,
                ingrRepository::findByName,
                e -> doIngredientCreate(e),
                e -> doIngredientUpdate(e)
            );
            transaction.commit();
        }
        return (counter.doReplace)
            ? new int[]{counter.imported, -counter.actioned}
            : new int[]{counter.imported, counter.actioned};
    }

    private void doIngredientCreate(Ingredient ingredient) {
        doIngredientAction(ingredient, ingrRepository::create);
    }

    private void doIngredientUpdate(Ingredient ingredient) {
        doIngredientAction(ingredient, ingrRepository::update);
    }

    private void doIngredientAction(Ingredient ingredient, Consumer<Ingredient> action) {
        doImport(ingredient.getUnit(), unitRepository);
        ingredient.getUnit().setId(unitRepository.findByName(ingredient.getUnit().getName()).orElseThrow().getId());
        action.accept(ingredient);
    }
}
