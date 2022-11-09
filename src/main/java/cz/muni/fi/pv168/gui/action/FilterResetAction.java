package cz.muni.fi.pv168.gui.action;

import java.awt.event.ActionEvent;

import cz.muni.fi.pv168.gui.filters.Sorter;

public class FilterResetAction extends FilterAction {

    public FilterResetAction(Sorter sorter) {
        super(sorter);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        sorter.resetFilters();
        sorter.applyFilters();
    }
}
