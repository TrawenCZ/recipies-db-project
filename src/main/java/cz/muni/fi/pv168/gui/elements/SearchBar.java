package cz.muni.fi.pv168.gui.elements;

import javax.swing.JTextField;

/**
 * JTextField implementing IFilter used as a search bar
 *
 * @author Jan Martinek
 */
public class SearchBar extends JTextField implements Filterable<String> {

    public SearchBar(int columns) {
        super(columns);
    }

    @Override
    public String getFilters() {
        return ("".equals(this.getText()))
            ? ".*"
            : this.getText();
    }

    @Override
    public void resetFilters() {
        this.setText("");
    }
}
