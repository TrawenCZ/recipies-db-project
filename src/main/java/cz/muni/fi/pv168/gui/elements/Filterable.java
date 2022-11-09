package cz.muni.fi.pv168.gui.elements;

/**
 * Interface thats needed for a checkbox/window/text field to be
 * used as a filter.
 *
 * @author Jan Martinek
 */
public interface Filterable<T> {

    /**
     * Returns all elements of type {@code T} that are set for filtering.
     * Usually in regex form.
     *
     * @return all active filters
     */
    public T getFilters();

    /**
     * Resets all active elements
     */
    public void resetFilters();

}
