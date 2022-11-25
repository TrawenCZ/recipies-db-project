package cz.muni.fi.pv168.gui.frames.tabs;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

import cz.muni.fi.pv168.data.generators.CategoryDataGenerator;
import cz.muni.fi.pv168.gui.action.ExportAction;
import cz.muni.fi.pv168.gui.action.ImportAction;
import cz.muni.fi.pv168.gui.frames.MainWindow;
import cz.muni.fi.pv168.gui.frames.forms.CategoryForm;
import cz.muni.fi.pv168.model.Category;

public final class CategoriesTab extends AbstractTab {

    public CategoriesTab() {
        super(MainWindow.getCategoryModel());
    }

    @Override
    public void addSampleData(int sampleSize) {
        var model = MainWindow.getCategoryModel();
        CategoryDataGenerator.getAll().stream().forEach(model::addRow);
    }

    @Override
    protected ImportAction<?> createImportAction() {
        return new ImportAction<>(
            MainWindow.getDependencies().getCategoryImporter(),
            Category.class,
            () -> MainWindow.getDependencies().getCategoryRepository().refresh()
        );
    }

    @Override
    protected ExportAction<?> createExportAction() {
        return new ExportAction<>(table, MainWindow.getCategoryModel());
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
