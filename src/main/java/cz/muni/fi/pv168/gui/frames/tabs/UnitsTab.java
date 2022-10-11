package cz.muni.fi.pv168.gui.frames.tabs;

import cz.muni.fi.pv168.data.UnitDataGenerator;
import cz.muni.fi.pv168.gui.frames.forms.UnitForm;
import cz.muni.fi.pv168.gui.models.UnitsTableModel;

import java.awt.event.ActionEvent;

public final class UnitsTab extends AbstractTab {

    public UnitsTab() {
        super(new UnitsTableModel());
    }

    @Override
    public void addSampleData(int sampleSize) {
        var model = (UnitsTableModel) table.getModel();
        UnitDataGenerator.getAll().stream().forEach(model::addRow);
    }
    
    @Override
    protected void addRow(ActionEvent actionEvent) {
        new UnitForm();
    }

    @Override
    protected void editSelectedRow(ActionEvent actionEvent) {
        new UnitForm("EDITED unit", 254);
    }

}

