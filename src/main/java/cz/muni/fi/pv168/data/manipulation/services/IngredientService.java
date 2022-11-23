package cz.muni.fi.pv168.data.manipulation.services;

import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.model.*;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author Jan Martinek
 */
public class IngredientService extends ServiceImpl<Ingredient> {

    private Repository<Unit> units;

    public IngredientService(Repository<Ingredient> ingredients,
                             Repository<Unit> units,
                             Supplier<TransactionHandler> transactions
    ) {
        super(ingredients, transactions);
        this.units = Objects.requireNonNull(units);
    }

    @Override
    public int[] saveRecords(Collection<Ingredient> records) {
        Counter counter = new Counter();
        Collection<Unit> unitRecords = records.stream().map(Ingredient::getUnit).toList();

        try (var transaction = transactions.get()) {
            doImport(unitRecords, counter, units, transaction::connection);
            ingrImport(records, counter, transaction::connection);
            transaction.commit();
        }
        return (counter.doReplace)
            ? new int[]{counter.imported, -counter.actioned}
            : new int[]{counter.imported, counter.actioned};
    }

    private void ingrImport(Collection<Ingredient> records, Counter counter, Supplier<ConnectionHandler> connection) {
        records.forEach(e -> setId(e.getUnit(), connection));
        super.doImport(records, counter, repository, connection);
    }

    private void setId(Unit newEntity, Supplier<ConnectionHandler> connection) {
        newEntity.setId(units.findUncommitted(newEntity.getName(), connection).get().getId());
    }
}
