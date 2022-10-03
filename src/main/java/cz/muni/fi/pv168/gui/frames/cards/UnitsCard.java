package cz.muni.fi.pv168.gui.frames.cards;

import cz.muni.fi.pv168.data.UnitDataGenerator;
import cz.muni.fi.pv168.gui.frames.forms.UnitForm;
import cz.muni.fi.pv168.gui.layouts.tables.UnitsTableLayout;

import java.awt.event.ActionEvent;

public final class UnitsCard extends AbstractCard {

    public UnitsCard() {
        super(new UnitsTableLayout());
    }

    @Override
    public void addSampleData(int sampleSize) {
        var model = (UnitsTableLayout) table.getModel();
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

