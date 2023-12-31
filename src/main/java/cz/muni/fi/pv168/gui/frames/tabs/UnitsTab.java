package cz.muni.fi.pv168.gui.frames.tabs;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JOptionPane;

import cz.muni.fi.pv168.gui.action.ExportAction;
import cz.muni.fi.pv168.gui.action.ImportAction;
import cz.muni.fi.pv168.gui.frames.MainWindow;
import cz.muni.fi.pv168.gui.frames.forms.UnitForm;
import cz.muni.fi.pv168.gui.models.UnitsTableModel;
import cz.muni.fi.pv168.model.RecipeIngredient;
import cz.muni.fi.pv168.model.Recipe;
import cz.muni.fi.pv168.model.Unit;
import cz.muni.fi.pv168.wiring.Supported;

import static cz.muni.fi.pv168.gui.resources.Messages.*;

public final class UnitsTab extends AbstractTab {

    public UnitsTab() {
        super(new UnitsTableModel(MainWindow.getDependencies().getUnitRepository()));
    }

    public UnitsTableModel getModel() {
        return (UnitsTableModel) model;
    }

    @Override
    protected void lockInput() {
        MainWindow.getTabs().get(Supported.INGREDIENT).setInput(true);
        MainWindow.getTabs().get(Supported.UNIT).setInput(true);
        MainWindow.getTabs().get(Supported.RECIPE).setInput(true);
    }

    @Override
    protected void unlockInput() {
        MainWindow.getTabs().get(Supported.INGREDIENT).release();
        MainWindow.getTabs().get(Supported.UNIT).release();
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
            MainWindow.getDependencies().getUnitImporter(),
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
        new UnitForm();
        updateEntries();
    }

    @Override
    protected void deleteSelectedRows(ActionEvent actionEvent) {
        if (showConfirmDialog() != JOptionPane.YES_OPTION) return;
        if (!baseUnitsChecker(false)) return;
        if (!deleteSafeSearch(MainWindow.getIngredientModel(), entity -> entity.getUnit().getName())) return;
        if (!deleteSafeSearchInRecipes()) return;
        deleteRows();
    }

    @Override
    protected void editSelectedRow(ActionEvent actionEvent) {
        if (!baseUnitsChecker(true)) return;
        Unit unit = getModel().getEntity(table.convertRowIndexToModel(table.getSelectedRow()));
        new UnitForm(unit);
    }

    private boolean baseUnitsChecker(boolean editMode) {
        for (int selectedRow : table.getSelectedRows()) {
            String nameAtSelectedRow = table.getValueAt(selectedRow, 0).toString();
            if (nameAtSelectedRow.equals("g") || nameAtSelectedRow.equals("ml") || nameAtSelectedRow.equals("pc(s)")) {
                showErrorDialog("Cannot " + (editMode ? "edit" : "delete") + " base units (g, ml, pc(s))!",
                        editMode ? EDITING_ERR_TITLE : ADDING_ERR_TITLE);
                return false;
            }
        }
        return true;
    }

    private boolean deleteSafeSearchInRecipes() {
        for (int selectedRow : table.getSelectedRows()) {
            Unit unit = getModel().getEntity(selectedRow);
            var recipeModel = MainWindow.getRecipeModel();
            for (int i = 0; i < recipeModel.getRowCount(); i++) {
                Recipe selectedRecipe = recipeModel.getEntity(i);
                List<RecipeIngredient> usedIngredients = selectedRecipe.getIngredients();
                for (RecipeIngredient ingredientAmount : usedIngredients) {
                    if (ingredientAmount.getUnit().getName().equals(unit.getName())) {
                        showErrorDialog("Cannot delete '" + unit.getName() + "' because it exists in table " +
                                "'Recipes' in recipe '" + selectedRecipe.getName() + "'.", DELETING_ERR_TITLE);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
