package cz.muni.fi.pv168.gui.frames.tabs;

import cz.muni.fi.pv168.data.CategoryDataGenerator;
import cz.muni.fi.pv168.gui.frames.forms.CategoryForm;
import cz.muni.fi.pv168.gui.models.CategoryTableModel;

import java.awt.event.ActionEvent;

public final class CategoriesTab extends AbstractTab {

    public CategoriesTab() {
        super(new CategoryTableModel());
    }

    @Override
    public void addSampleData(int sampleSize) {
        var model = (CategoryTableModel) table.getModel();
        CategoryDataGenerator.getAll().stream().forEach(model::addRow);
    }
    
    @Override
    protected void addRow(ActionEvent actionEvent) {
        new CategoryForm();
    }

    @Override
    protected void editSelectedRow(ActionEvent actionEvent) {
        new CategoryForm("EDITED category");
    }
}
