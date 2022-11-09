package cz.muni.fi.pv168.gui.frames.forms;

import javax.swing.*;

import cz.muni.fi.pv168.gui.elements.ScrollPane;
import cz.muni.fi.pv168.model.*;

import java.awt.*;

public class RecipeDetails extends RecipeForm {

    private final ScrollPane<SingleIngredient> ingredients = new ScrollPane<>();

    private class SingleIngredient extends JPanel {

        private JTextField ingredient = new JTextField(16);
        private JTextField unit = new JTextField(16);
        private JTextField amount = new JTextField(6);

        public SingleIngredient(IngredientAmount ingredientAmount) {
            super(new GridBagLayout());

            if (ingredientAmount == null) throw new NullPointerException();

            this.ingredient.setText(ingredientAmount.getIngredient().getName());
            this.amount.setText(String.format("%.2f", ingredientAmount.getAmount()));
            this.unit.setText(ingredientAmount.getUnit().getName());

            this.ingredient.setEditable(false);
            this.amount.setEditable(false);
            this.unit.setEditable(false);

            this.ingredient.setHorizontalAlignment(SwingConstants.CENTER);
            this.amount.setHorizontalAlignment(SwingConstants.RIGHT);
            this.unit.setHorizontalAlignment(SwingConstants.CENTER);

            initializeLayout();
            ingredients.addTyped(this);
        }

        private void initializeLayout() {
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;
            c.ipady = 10;

            c.weightx = 2;
            c.gridx = 0;
            this.add(ingredient, c);

            c.weightx = 0.5;
            c.gridx = 1;
            this.add(amount, c);

            c.weightx = 1;
            c.gridx = 2;
            this.add(unit, c);
        }
    }

    public RecipeDetails(Recipe recipe) {
        super(recipe.getName());
        initializeBody();
        pack();
        setData(recipe);
        setIngredients(recipe);
        show();
    }

    @Override
    protected void initializeBody() {
        super.initializeBody();

        // replace original ingredients with locked ones
        body.remove(super.ingredients);

        ingredients.setPreferredSize(new Dimension(410, 500));
        gridAdd(ingredients, 4, 1, 2, 10);

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

    private void setIngredients(Recipe recipe) {
        super.setIngredients(recipe, SingleIngredient::new);
    }
}
