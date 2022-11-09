package cz.muni.fi.pv168.gui.filters;

import java.util.ArrayList;
import java.util.List;

import javax.swing.RowFilter;

import cz.muni.fi.pv168.gui.models.AbstractModel;
import cz.muni.fi.pv168.model.Nameable;
import cz.muni.fi.pv168.model.Recipe;

/**
 * @author Jan Martinek
 */
public class IngredientFilter extends RowFilter<AbstractModel<?>, Integer> {

    private final List<String> ingredients;

    public IngredientFilter(List<String> ingredients) {
        this.ingredients = new ArrayList<>(ingredients);
    }


    @Override
    public boolean include(RowFilter.Entry<? extends AbstractModel<?>, ? extends Integer> entry) {
        AbstractModel<? extends Nameable> model = entry.getModel();
        Recipe recipe = (Recipe) model.getEntity(entry.getIdentifier());

        var missingIngredients = new ArrayList<>(ingredients);
        for (var amount : recipe.getIngredients()) {
            missingIngredients.remove(amount.getIngredient().getName());
        }
        return missingIngredients.size() == 0;
    }
}
