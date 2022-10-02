package cz.muni.fi.pv168.gui.frames.cards;

import cz.muni.fi.pv168.data.CategoryDataGenerator;
import cz.muni.fi.pv168.gui.frames.forms.AddCategoryForm;
import cz.muni.fi.pv168.gui.layouts.tables.CategoryTableLayout;

import java.awt.event.ActionEvent;

public final class CategoriesCard extends AbstractCard {

    public CategoriesCard() {
        super(new CategoryTableLayout());
    }

    @Override
    public void addSampleData(int sampleSize) {
        var model = (CategoryTableLayout) table.getModel();
        CategoryDataGenerator.getAll().stream().forEach(model::addRow);
    }
    
    @Override
    protected void addRow(ActionEvent actionEvent) {
        new AddCategoryForm();
    }
}
