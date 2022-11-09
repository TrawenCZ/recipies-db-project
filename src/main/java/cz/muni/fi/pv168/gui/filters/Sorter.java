package cz.muni.fi.pv168.gui.filters;

import java.util.Objects;

import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

import cz.muni.fi.pv168.gui.elements.Filterable;
import cz.muni.fi.pv168.gui.models.AbstractModel;

/**
 * @author Jan Martinek
 */
public class Sorter extends TableRowSorter<AbstractModel<?>> {

    private final JTable table;
    private final Filterable<String> searchBar;

    public Sorter(JTable table, AbstractModel<?> model, Filterable<String> searchBar) {
        super(Objects.requireNonNull(model));
        this.searchBar = Objects.requireNonNull(searchBar);
        this.table = Objects.requireNonNull(table);
        this.setSortsOnUpdates(true);
    }

    public void applyFilters() {
        table.setRowSorter(this);
        this.setRowFilter(newFilter());
    }

    public void resetFilters() {
        searchBar.resetFilters();
        applyFilters();
    }

    protected RowFilter<AbstractModel<?>, Integer> newFilter() {
        RowFilter<AbstractModel<?>, Integer> nameFilter = null;
        try {
            nameFilter = new NameFilter(searchBar.getFilters());
        } catch (java.util.regex.PatternSyntaxException e) {
            return null;
        }
        return nameFilter;
    }
}
