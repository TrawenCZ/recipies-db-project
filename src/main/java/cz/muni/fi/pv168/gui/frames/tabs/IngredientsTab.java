package cz.muni.fi.pv168.gui.frames.tabs;

import static cz.muni.fi.pv168.gui.resources.Messages.DELETING_ERR_TITLE;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;

import cz.muni.fi.pv168.data.generators.IngredientDataGenerator;
import cz.muni.fi.pv168.data.manipulation.JsonExporterImpl;
import cz.muni.fi.pv168.data.manipulation.JsonImporterImpl;
import cz.muni.fi.pv168.data.service.IngredientService;
import cz.muni.fi.pv168.data.service.UnitsService;
import cz.muni.fi.pv168.gui.action.ExportAction;
import cz.muni.fi.pv168.gui.action.ImportAction;
import cz.muni.fi.pv168.gui.frames.TabLayout;
import cz.muni.fi.pv168.gui.frames.forms.IngredientForm;
import cz.muni.fi.pv168.gui.models.IngredientTableModel;
import cz.muni.fi.pv168.gui.models.RecipeTableModel;
import cz.muni.fi.pv168.gui.models.UnitsTableModel;
import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.IngredientAmount;
import cz.muni.fi.pv168.model.Recipe;

public final class IngredientsTab extends AbstractTab {

    public IngredientsTab() {
        super(new IngredientTableModel());
        service = new IngredientService(
            (IngredientTableModel) table.getModel(),
            new UnitsService((UnitsTableModel) TabLayout.getUnitsModel())
        );
        importAction = new ImportAction<>(table, new JsonImporterImpl(), (IngredientService) service, Ingredient.class);
        exportAction = new ExportAction<>(table, new JsonExporterImpl(), (IngredientService) service);

    }

    @Override
    public void addSampleData(int sampleSize) {
        var model = (IngredientTableModel) table.getModel();
        IngredientDataGenerator.getAll().stream().forEach(model::addRow);
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
        Ingredient ingredient = TabLayout.getIngredientsModel().getEntity(table.convertRowIndexToModel(table.getSelectedRow()));
        new IngredientForm(ingredient);
    }

    private boolean deleteSafeSearchInRecipes() {
        for (int selectedRow : table.getSelectedRows()) {
            Ingredient ingredient = (Ingredient) table.getAbstractModel().getEntity(selectedRow);
            var recipesModel = (RecipeTableModel) TabLayout.getRecipesModel();
            for (int i = 0; i < recipesModel.getRowCount(); i++) {
                Recipe selectedRecipe = recipesModel.getEntity(i);
                List<IngredientAmount> usedIngredients = selectedRecipe.getIngredients();
                for (IngredientAmount ingredientAmount : usedIngredients) {
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
