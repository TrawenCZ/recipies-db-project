package cz.muni.fi.pv168.gui.frames.forms;

import cz.muni.fi.pv168.gui.elements.SingleIngredient;
import cz.muni.fi.pv168.model.*;

import java.util.List;

public class RecipeDetails extends RecipeForm {

    public RecipeDetails(Recipe recipe) {
        super(recipe, true);
    }

    @Override
    protected void initializeBody() {
        super.initializeBody();

        // set all to non-editable and hide the ingredients add button
        nameInput.setEditable(false);
        portionInput.setEditable(false);
        durationInput.setEditable(false);
        descriptionInput.setEditable(false);
        categoriesInput.setEnabled(false);
        instructionsInput.setEditable(false);
        ingredientButton.setVisible(false);
    }

    @Override
    protected boolean onAction() {
        return true;
    }

    @Override
    protected void setIngredients(List<RecipeIngredient> recipeIngredients) {
        for (var i : recipeIngredients) {
            ingredients.addTyped(new SingleIngredient(recipe, i));
        }
    }
}
