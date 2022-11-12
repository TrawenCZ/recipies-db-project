package cz.muni.fi.pv168.data.service;

import cz.muni.fi.pv168.exceptions.InconsistentRecordException;
import cz.muni.fi.pv168.gui.models.IngredientTableModel;
import cz.muni.fi.pv168.model.Ingredient;

import java.util.Collection;

/**
 * @author Radim Stejskal, Jan Martinek
 */
public class IngredientService extends AbstractService<Ingredient> {

    private UnitsService unitsService;

    public IngredientService(IngredientTableModel ingredientRepository, UnitsService unitsService) {
        super(ingredientRepository, "Ingredient");
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
