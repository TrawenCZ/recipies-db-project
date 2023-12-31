package cz.muni.fi.pv168.gui.frames.tabs;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

import cz.muni.fi.pv168.gui.action.ExportAction;
import cz.muni.fi.pv168.gui.action.ImportAction;
import cz.muni.fi.pv168.gui.frames.MainWindow;
import cz.muni.fi.pv168.gui.frames.forms.CategoryForm;
import cz.muni.fi.pv168.gui.models.CategoryTableModel;
import cz.muni.fi.pv168.model.Category;
import cz.muni.fi.pv168.wiring.Supported;

public final class CategoriesTab extends AbstractTab {

    public CategoriesTab() {
        super(new CategoryTableModel(MainWindow.getDependencies().getCategoryRepository()));
    }

    public CategoryTableModel getModel() {
        return (CategoryTableModel) model;
    }

    @Override
    protected void lockInput() {
        MainWindow.getTabs().get(Supported.CATEGORY).setInput(true);
        MainWindow.getTabs().get(Supported.RECIPE).setInput(true);
    }

    @Override
    protected void unlockInput() {
        MainWindow.getTabs().get(Supported.CATEGORY).release();
        MainWindow.getTabs().get(Supported.RECIPE).release();
    }

    @Override
    protected void refreshTables() {
        getModel().getRepository().refresh();
        getModel().fireTableDataChanged();
    }

    @Override
    protected ImportAction<?> createImportAction() {
        return new ImportAction<>(
            MainWindow.getDependencies().getCategoryImporter(),
            this::lockInput,
            () -> {
                refreshTables();
                unlockInput();
            }
        );
    }

    @Override
    protected ExportAction<?> createExportAction() {
        return new ExportAction<>(table, getModel());
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
        Category category = getModel().getEntity(table.convertRowIndexToModel(table.getSelectedRow()));
        new CategoryForm(category);
    }
}
