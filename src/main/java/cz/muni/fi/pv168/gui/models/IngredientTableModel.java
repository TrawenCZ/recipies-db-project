package cz.muni.fi.pv168.gui.models;

import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Model of ingredient data class in a tabular representation
 */
public class IngredientTableModel extends TableModel<Ingredient> {

    private final List<Ingredient> ingredients;

    private final List<Column<Ingredient, ?>> columns = List.of(
        Column.readonly("Name", String.class, Ingredient::getName),
        Column.readonly("Kcal in 1U", double.class, Ingredient::getKcal),
        Column.readonly("Unit", Unit.class, Ingredient::getUnit)
    );

    public IngredientTableModel() {
        this(new ArrayList<Ingredient>());
    }

    public IngredientTableModel(List<Ingredient> ingredients) {
        this.ingredients = new ArrayList<>(ingredients);
    }

    @Override
    protected List<Ingredient> getEntities() {
        return ingredients;
    }

    @Override
    protected List<Column<Ingredient, ?>> getColumns() {
        return columns;
    }
}