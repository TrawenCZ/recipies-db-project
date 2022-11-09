package cz.muni.fi.pv168.gui.filters;

import java.util.Objects;
import java.util.function.Function;

import javax.swing.RowFilter;

import cz.muni.fi.pv168.gui.models.AbstractModel;
import cz.muni.fi.pv168.model.Nameable;
import cz.muni.fi.pv168.model.Recipe;

/**
 * @author Jan Martinek
 */
public class NumericFilter extends RowFilter<AbstractModel<?>, Integer> {

    private int lower;
    private int upper;
    private Function<Recipe, Integer> getter;

    public NumericFilter(int lower, int upper, Function<Recipe, Integer> getter) {
        this.lower = lower;
        this.upper = upper;
        this.getter = Objects.requireNonNull(getter);
    }

    @Override
    public boolean include(RowFilter.Entry<? extends AbstractModel<?>, ? extends Integer> entry) {
        AbstractModel<? extends Nameable> model = entry.getModel();
        Recipe entity = (Recipe) model.getEntity(entry.getIdentifier());
        var value = getter.apply(entity);
        return lower <= value && value <= upper;
    }
}
