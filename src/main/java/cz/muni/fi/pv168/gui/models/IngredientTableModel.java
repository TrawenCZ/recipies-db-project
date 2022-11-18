package cz.muni.fi.pv168.gui.models;

import java.util.List;

import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.Unit;

/**
 * Model of ingredient data class in a tabular representation
 *
 * @author Jan Martinek, Radim Stejskal
 */
public class IngredientTableModel extends AbstractModel<Ingredient> {

    private final Repository<Ingredient> ingredients;

    public IngredientTableModel(Repository<Ingredient> ingredients) {
        super(List.of(
            Column.readonly("Name", String.class, Ingredient::getName, 4),
            Column.readonly("Kcal in 1U", Double.class, Ingredient::getKcal, 2),
            Column.readonly("Unit", Unit.class, Ingredient::getUnit, 4)
        ));
        this.ingredients = ingredients;
    }

    @Override
    public Repository<Ingredient> getRepository() {
        return ingredients;
    }

    /**
     * Overwritten to disable coloring by base units.
     *
     * @return value < {@link AbstractModel#UNDEFINED}
     */
    @Override
    protected int getColorIndex() {
        return UNDEFINED - 1;
    }

    @Override
    public String toString() {
        return "Ingredients";
    }
}
