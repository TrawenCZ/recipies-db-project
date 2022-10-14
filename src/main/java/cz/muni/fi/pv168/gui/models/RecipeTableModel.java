package cz.muni.fi.pv168.gui.models;

import cz.muni.fi.pv168.model.Category;
import cz.muni.fi.pv168.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Model of recipe data class in a tabular representation
 *
 * @author Jan Martinek
 */
public class RecipeTableModel extends TableModel<Recipe> {

    private final List<Recipe> recipes;

    private final List<Column<Recipe, ?>> columns = List.of(
        Column.readonly("Name", String.class, Recipe::getName),
        Column.readonly("Category", Category.class, Recipe::getCategory),
        Column.readonly("Required time", int.class, Recipe::getRequiredTime),
        Column.readonly("Portions", int.class, Recipe::getPortions),
        Column.readonly("Description", String.class, Recipe::getDescription)
    );

    public RecipeTableModel() {
        this(new ArrayList<Recipe>());
    }

    public RecipeTableModel(List<Recipe> recipes) {
        this.recipes = new ArrayList<>(recipes);
    }

    @Override
    protected List<Recipe> getEntities() {
        return recipes;
    }

    @Override
    protected List<Column<Recipe, ?>> getColumns() {
        return columns;
    }
}

