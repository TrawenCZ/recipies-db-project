package cz.muni.fi.pv168.data.manipulation.services;

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
    public int saveRecords(Collection<Ingredient> records) {
        Collection<Unit> unitRecords = records.stream().map(Ingredient::getUnit).toList();
        Collection<Ingredient> exIngredients = getDuplicates(records, repository);
        Collection<Unit> exUnits = getDuplicates(unitRecords, units);

        int total = exUnits.size() + exIngredients.size();
        boolean replace = (total > 0) ? getDecision() : false;

        try (var transaction = transactions.get()) {
            doImport(unitRecords, exUnits, replace, units, transaction::connection);
            doImport(records, exIngredients, replace, repository, transaction::connection);
            transaction.commit();
        }
        return (replace) ? -total : total;
    }
}
