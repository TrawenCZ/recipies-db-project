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
public class CategoryFilter extends RowFilter<AbstractModel<?>, Integer> {

    private final List<String> categories;

    public CategoryFilter(List<String> categories) {
        this.categories = new ArrayList<>(categories);
    }

    @Override
    public boolean include(RowFilter.Entry<? extends AbstractModel<?>, ? extends Integer> entry) {
        AbstractModel<? extends Nameable> model = entry.getModel();
        Recipe recipe = (Recipe) model.getEntity(entry.getIdentifier());
        return categories.size() == 0 || categories.contains(recipe.getCategory().getName());
    }
}
