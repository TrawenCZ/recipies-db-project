package cz.muni.fi.pv168.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import cz.muni.fi.pv168.gui.filters.Sorter;


public class FilterAction implements ActionListener {

    protected final Sorter sorter;

    public FilterAction(Sorter sorter) {
        this.sorter = Objects.requireNonNull(sorter);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        sorter.applyFilters();
    }
}
