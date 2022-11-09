package cz.muni.fi.pv168.gui.filters;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.RowFilter;

import cz.muni.fi.pv168.gui.models.AbstractModel;
import cz.muni.fi.pv168.model.Nameable;

/**
 * @author Jan Martinek
 */
public class NameFilter extends RowFilter<AbstractModel<?>, Integer> {

    private Pattern pattern;

    public NameFilter(String regex) {
        pattern = Pattern.compile(Objects.requireNonNull(regex).toLowerCase());
    }

    @Override
    public boolean include(RowFilter.Entry<? extends AbstractModel<?>, ? extends Integer> entry) {
        AbstractModel<? extends Nameable> model = entry.getModel();
        Nameable entity = model.getEntity(entry.getIdentifier());
        Matcher matcher = pattern.matcher(entity.getName().toLowerCase());
        return matcher.find();
    }
}
