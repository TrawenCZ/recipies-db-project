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
public class RecipeTableModel extends AbstractModel<Recipe> {

    private final List<Recipe> recipes;

    public RecipeTableModel() {
        this(new ArrayList<Recipe>());
    }

    public RecipeTableModel(List<Recipe> recipes) {
        super(List.of(
            Column.readonly("Name", String.class, Recipe::getName, 4),
            Column.readonly("Category", Category.class, Recipe::getCategory, 4),
            Column.readonly("Time required (minutes)", Integer.class, Recipe::getRequiredTime, 2),
            Column.readonly("Portions", Integer.class, Recipe::getPortions, 2),
            Column.readonly("Description", String.class, Recipe::getDescription, 4),
            Column.readonly("Ingredients", List.class, Recipe::getIngredients, null)
        ));
        this.recipes = new ArrayList<>(recipes);
    }

    @Override
    public List<Recipe> getEntities() {
        return recipes;
    }


    @Override
    public String toString() {
        return "Recipes";
    }
}
