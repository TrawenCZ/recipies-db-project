package cz.muni.fi.pv168.gui.frames.tabs;

import cz.muni.fi.pv168.data.IngredientDataGenerator;
import cz.muni.fi.pv168.gui.frames.forms.IngredientForm;
import cz.muni.fi.pv168.gui.models.IngredientTableModel;

import java.awt.event.ActionEvent;

public final class IngredientsTab extends AbstractTab {

    public IngredientsTab() {
        super(new IngredientTableModel());
    }

    @Override
    public void addSampleData(int sampleSize) {
        var model = (IngredientTableModel) table.getModel();
        IngredientDataGenerator.getAll().stream().forEach(model::addRow);
    }
    
    @Override
    protected void addRow(ActionEvent actionEvent) {
        new IngredientForm();
    }

    @Override
    protected void editSelectedRow(ActionEvent actionEvent) {
        new IngredientForm("EDITED ingredient", 113, 1);
    }
}

