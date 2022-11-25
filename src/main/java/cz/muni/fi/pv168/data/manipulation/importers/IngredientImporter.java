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

/**
 * @author Jan Martinek
 */
public final class IngredientImporter extends ObjectImporter<Ingredient> {

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
        Function<Supplier<ConnectionHandler>, Repository<Ingredient>> ingredients,
        Function<Supplier<ConnectionHandler>, Repository<Unit>> units,
        Supplier<TransactionHandler> transactions
    ) {
        super(ingredients, transactions);
        this.units = Objects.requireNonNull(units);
    }

    @Override
    public int[] saveRecords(Collection<Ingredient> records) {
        counter = new Counter();

        try (var transaction = transactions.get()) {
            ingrRepository = repositories.apply(transaction::connection);
            unitRepository = units.apply(transaction::connection);
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
        doUnitImport(ingredient.getUnit());
        ingrRepository.create(ingredient);
    }

    private void doIngredientUpdate(Ingredient ingredient) {
        doUnitImport(ingredient.getUnit());
        ingrRepository.update(ingredient);
    }

    private void doUnitImport(Unit unit) {
        doImport(
            List.of(unit),
            unitRepository::findByName,
            unitRepository::create,
            unitRepository::update
        );
    }

    // private void ingrImport(Collection<Ingredient> records, Counter counter, Supplier<ConnectionHandler> connection) {
    //     records.forEach(e -> setId(e.getUnit(), connection));
    //     super.doImport(records, counter, repository, connection);
    // }

    // private void setId(Unit newEntity, Supplier<ConnectionHandler> connection) {
    //     newEntity.setId(units.findUncommitted(newEntity.getName(), connection).get().getId());
    // }
}
