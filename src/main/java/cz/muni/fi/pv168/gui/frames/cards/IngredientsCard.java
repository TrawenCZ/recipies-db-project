package cz.muni.fi.pv168.gui.frames.cards;

import cz.muni.fi.pv168.data.IngredientDataGenerator;
import cz.muni.fi.pv168.gui.frames.forms.IngredientForm;
import cz.muni.fi.pv168.gui.layouts.tables.IngredientTableLayout;

import java.awt.event.ActionEvent;

public final class IngredientsCard extends AbstractCard {

    public IngredientsCard() {
        super(new IngredientTableLayout());
    }

    @Override
    public void addSampleData(int sampleSize) {
        var model = (IngredientTableLayout) table.getModel();
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

