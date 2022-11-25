package cz.muni.fi.pv168.gui.frames.tabs;

import static cz.muni.fi.pv168.gui.resources.Messages.DELETING_ERR_TITLE;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JOptionPane;

import cz.muni.fi.pv168.data.generators.IngredientDataGenerator;
import cz.muni.fi.pv168.gui.action.ExportAction;
import cz.muni.fi.pv168.gui.action.ImportAction;
import cz.muni.fi.pv168.gui.frames.MainWindow;
import cz.muni.fi.pv168.gui.frames.forms.IngredientForm;
import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.RecipeIngredient;
import cz.muni.fi.pv168.model.Recipe;

public final class IngredientsTab extends AbstractTab {

    public IngredientsTab() {
        super(MainWindow.getIngredientModel());
    }

    @Override
    public void addSampleData(int sampleSize) {
        var model = MainWindow.getIngredientModel();
        IngredientDataGenerator.getAll().stream().forEach(model::addRow);
    }

    @Override
    protected ImportAction<?> createImportAction() {
        return new ImportAction<>(
            MainWindow.getDependencies().getIngredientImporter(),
            Ingredient.class,
            () -> {
                MainWindow.getDependencies().getUnitRepository().refresh();
                MainWindow.getDependencies().getIngredientRepository().refresh();
                MainWindow.getUnitsModel().fireTableDataChanged();
                MainWindow.getIngredientModel().fireTableDataChanged();
            }
        );
    }

    @Override
    protected ExportAction<?> createExportAction() {
        return new ExportAction<>(table, MainWindow.getIngredientModel());
    }

    @Override
    protected void addRow(ActionEvent actionEvent) {
        new IngredientForm();
        updateEntries();
    }

    @Override
    protected void deleteSelectedRows(ActionEvent event) {
        if (showConfirmDialog() != JOptionPane.YES_OPTION) return;
        if (!deleteSafeSearchInRecipes()) return;
        deleteRows();
    }

    @Override
    protected void editSelectedRow(ActionEvent actionEvent) {
        Ingredient ingredient = MainWindow.getIngredientModel().getEntity(table.convertRowIndexToModel(table.getSelectedRow()));
        new IngredientForm(ingredient);
    }

    private boolean deleteSafeSearchInRecipes() {
        for (int selectedRow : table.getSelectedRows()) {
            Ingredient ingredient = MainWindow.getIngredientModel().getEntity(selectedRow);
            var recipeModel = MainWindow.getRecipeModel();
            for (int i = 0; i < recipeModel.getRowCount(); i++) {
                Recipe selectedRecipe = recipeModel.getEntity(i);
                List<RecipeIngredient> usedIngredients = selectedRecipe.getIngredients();
                for (RecipeIngredient ingredientAmount : usedIngredients) {
                    if (ingredientAmount.getIngredient().getName().equals(ingredient.getName())) {
                        showErrorDialog("Cannot delete '" + ingredient.getName() + "' because it exists in table " +
                                "'Recipes' in recipe '" + selectedRecipe.getName() + "'.", DELETING_ERR_TITLE);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
