package cz.muni.fi.pv168.gui.frames.tabs;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

import cz.muni.fi.pv168.data.generators.CategoryDataGenerator;
import cz.muni.fi.pv168.gui.frames.MainWindow;
import cz.muni.fi.pv168.gui.frames.forms.CategoryForm;
import cz.muni.fi.pv168.model.Category;

public final class CategoriesTab extends AbstractTab {

    public CategoriesTab() {
        super(MainWindow.getCategoryModel());
        // importAction = new ImportAction<>(table, new JsonImporterImpl(), (CategoryService) service, Category.class);
        // exportAction = new ExportAction<>(table, new JsonExporterImpl(), (CategoryService) service);
    }

    @Override
    public void addSampleData(int sampleSize) {
        var model = MainWindow.getCategoryModel();
        CategoryDataGenerator.getAll().stream().forEach(model::addRow);
    }

    @Override
    protected void addRow(ActionEvent actionEvent) {
        new CategoryForm();
        updateEntries();
    }

    @Override
    protected void deleteSelectedRows(ActionEvent actionEvent) {
        if (showConfirmDialog() != JOptionPane.YES_OPTION) return;
        if (!deleteSafeSearch(MainWindow.getRecipeModel(), entity -> entity.getCategory().getName())) return;
        deleteRows();
    }

    @Override
    protected void editSelectedRow(ActionEvent actionEvent) {
        Category category = MainWindow.getCategoryModel().getEntity(table.convertRowIndexToModel(table.getSelectedRow()));
        new CategoryForm(category);
    }
}
