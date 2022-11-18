package cz.muni.fi.pv168.gui.models;

import java.util.List;

import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.model.Category;
import cz.muni.fi.pv168.model.Recipe;

/**
 * Model of recipe data class in a tabular representation
 *
 * @author Jan Martinek
 */
public class RecipeTableModel extends AbstractModel<Recipe> {

    private final Repository<Recipe> recipes;

    public RecipeTableModel(Repository<Recipe> recipes) {
        super(List.of(
            Column.readonly("Name", String.class, Recipe::getName, 4),
            Column.readonly("Category", Category.class, Recipe::getCategory, 4),
            Column.readonly("Time required (minutes)", Integer.class, Recipe::getRequiredTime, 2),
            Column.readonly("Portions", Integer.class, Recipe::getPortions, 2),
            Column.readonly("Description", String.class, Recipe::getDescription, 4),
            Column.readonly("Ingredients", List.class, Recipe::getIngredients, null)
        ));
        this.recipes = recipes;
    }

    @Override
    public Repository<Recipe> getRepository() {
        return recipes;
    }


    @Override
    public String toString() {
        return "Recipes";
    }
}
