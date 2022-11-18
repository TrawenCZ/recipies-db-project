package cz.muni.fi.pv168.data.service;

import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.exceptions.InconsistentRecordException;
import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.Unit;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author Radim Stejskal, Jan Martinek
 */
public class IngredientService extends AbstractService<Ingredient> {

    private Service<Unit> unitsService;

    public IngredientService(Repository<Ingredient> ingredientRepository, Service<Unit> unitsService, Supplier<TransactionHandler> transactions) {
        super(ingredientRepository, transactions);
        this.unitsService = unitsService;
    }

    @Override
    public int saveRecords(Collection<Ingredient> records) throws InconsistentRecordException {
        records = verifyRecords(records);
        unitsService.saveRecords(records.stream()
                                        .map(Ingredient::getUnit)
                                        .toList());
        return saveRecords(records, true);
    }

    @Override
    public void deleteRecords(Collection<Ingredient> records) {

    }
}
