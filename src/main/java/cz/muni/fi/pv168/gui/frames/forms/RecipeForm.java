package cz.muni.fi.pv168.gui.frames.forms;

import cz.muni.fi.pv168.data.validation.FieldMaxLengths;
import cz.muni.fi.pv168.gui.TextValidator;
import cz.muni.fi.pv168.gui.elements.ScrollPane;
import cz.muni.fi.pv168.gui.elements.SingleIngredient;
import cz.muni.fi.pv168.gui.elements.text.IntegerTextField;
import cz.muni.fi.pv168.gui.frames.MainWindow;
import cz.muni.fi.pv168.gui.models.RecipeTableModel;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.model.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.*;

public class RecipeForm extends AbstractForm {

    protected final JTextField nameInput = new JTextField(24);
    protected final IntegerTextField portionInput = new IntegerTextField(1, 6);
    protected final IntegerTextField durationInput = new IntegerTextField(1, 6);

    protected final JTextArea descriptionInput = new JTextArea(3,24);
    protected final JTextArea instructionsInput = new JTextArea(8,24);

    protected final JComboBox<Category> categoriesInput = new JComboBox<>(getAllCategories());

    protected final ScrollPane<SingleIngredient> ingredients = new ScrollPane<>();
    protected final JButton ingredientButton = new JButton("Add ingredient", Icons.getScaledIcon((ImageIcon) Icons.ADD_S, 20));

    protected Recipe recipe;

    private RecipeForm(String title, String header) {
        super(title, header);
        initializeBody();
    }

    /**
     * Edit recipe
     */
    public RecipeForm(Recipe recipe) {
        this(EDIT, "Edit recipe");
        setData(recipe);
        show();
    }

    /**
     * Add recipe
     */
    public RecipeForm() {
        this("Add", "New recipe");
        show();
    }

    /**
     * Recipe details
     */
    public RecipeForm(Recipe recipe, boolean isDetails) {
        super("Details", recipe.getName(), null, "Close");
        initializeBody();
        setData(recipe);
        show();
    }

    @Override
    protected void initializeBody() {
        nameInput.setToolTipText(NAME_TOOLTIP + "RECIPES!");
        descriptionInput.setLineWrap(true);
        descriptionInput.setWrapStyleWord(true);
        instructionsInput.setLineWrap(true);
        instructionsInput.setWrapStyleWord(true);

        ingredients.setPreferredSize(new Dimension(410, 500));
        ingredientButton.addActionListener(e -> {
            if (MainWindow.getIngredientModel().getRowCount() == 0) {
                showErrorDialog("No ingredients available", "Missing ingredients");
            } else {
                ingredients.addTyped(new SingleIngredient(recipe, this::removeIngredient));
                refreshContent();
            }
        });

        gridExtensions(GridBagConstraints.HORIZONTAL, 0, 5);

        // labels
        gridInsets(10, 10, 10, 10);
        gridAdd(new JLabel(" Name (*)"), 0, 0);
        gridAdd(new JLabel(" Description (*)"), 0, 2);
        gridAdd(new JLabel(" Category"), 0, 4);
        gridAdd(new JLabel(" Portions (*)"), 2, 4);
        gridAdd(new JLabel(" Preparation time (*)"), 3, 4);
        gridAdd(new JLabel(" Instructions (*)"), 0, 9);
        gridAdd(new JLabel(" Ingredients (* at least one)"), 4, 0);
        gridAdd(ingredientButton, 5, 0);

        // preparation time minute label
        gridInsets(-10, -40, 10, 10);
        gridAdd(new JLabel("min"), 4, 5);

        // input fields
        gridInsets(-10, 10, 10, 10);
        gridAdd(nameInput, 0, 1, 4, 1);
        gridAdd(new JScrollPane(descriptionInput), 0, 3, 4, 1);
        gridAdd(categoriesInput, 0, 5, 2, 1);
        gridAdd(portionInput, 2, 5);
        gridAdd(durationInput, 3, 5);
        gridAdd(new JScrollPane(instructionsInput), 0, 10, 4, 1);

        // ingredients
        gridInsets(-21, 10, 0, 10);
        gridAdd(ingredients, 4, 1, 2, 10);
        pack();
    }

    @Override
    protected boolean onAction() {
        var tableModel = MainWindow.getRecipeModel();
        if (!verifyName(tableModel, recipe, nameInput.getText())) {
            return false;
        }

        if (TextValidator.empty(descriptionInput.getText())) {
            showErrorDialog("Description is required and cannot be empty!", "Missing description");
            return false;
        }
        if (TextValidator.empty(instructionsInput.getText())) {
            showErrorDialog("Instructions are required and cannot be empty!", "Missing instructions");
            return false;
        }
        if (TextValidator.longerThanMaxLength(descriptionInput.getText(), FieldMaxLengths.RECIPE_DESCRIPTION)) {
            showErrorDialog("Description should not be longer than " + FieldMaxLengths.RECIPE_DESCRIPTION +
                    " characters! (Currently is '" +
                    descriptionInput.getText().length() + "' characters long)", "Too long description");
            return false;
        }
        if (TextValidator.longerThanMaxLength(instructionsInput.getText(), FieldMaxLengths.RECIPE_INSTRUCTIONS)) {
            showErrorDialog("Description should not be longer than " + FieldMaxLengths.RECIPE_INSTRUCTIONS +
                    " characters! (Currently is '" +
                    instructionsInput.getText().length() + "' characters long)", "Too long instructions");
            return false;
        }

        List<RecipeIngredient> ingredientsList = ingredients.getAll().stream()
            .map(SingleIngredient::getIngredient)
            .toList();

        if (ingredientsList.size() == 0) {
            showErrorDialog("Recipe must have at least ONE ingredient!", "Missing ingredients");
            return false;
        }

        if (isEdit()) {
            editRecipe(tableModel, ingredientsList);
        } else {
            addRecipe(tableModel, ingredientsList);
        }
        return true;
    }

    protected void setData(Recipe recipe) {
        this.recipe = Objects.requireNonNull(recipe);
        nameInput.setText(recipe.getName());
        descriptionInput.setText(recipe.getDescription());
        instructionsInput.setText(recipe.getInstructions());
        categoriesInput.setSelectedItem(recipe.getCategory());
        durationInput.setText(String.valueOf(recipe.getRequiredTime()));
        portionInput.setText(String.valueOf(recipe.getPortions()));
        setIngredients(recipe.getIngredients());
        refreshContent();
    }

    protected void setIngredients(List<RecipeIngredient> recipeIngredients) {
        for (var i : recipeIngredients) {
            ingredients.addTyped(new SingleIngredient(recipe, i, this::removeIngredient));
        }
    }

    protected void removeIngredient(SingleIngredient ingredient) {
        ingredients.remove(ingredient);
        refreshContent();
    }

    private Category[] getAllCategories() {
        List<Category> categories = MainWindow.getCategoryModel().getRepository().findAll();
        List<Category> categoriesWithUncategorized = new ArrayList<>();
        categoriesWithUncategorized.add(Category.UNCATEGORIZED);
        categoriesWithUncategorized.addAll(categories);
        return categoriesWithUncategorized.toArray(new Category[0]);
    }

    private void editRecipe(RecipeTableModel model, List<RecipeIngredient> ingredients) {
        recipe.setName(nameInput.getText());
        recipe.setDescription(descriptionInput.getText());
        recipe.setCategory((Category) categoriesInput.getSelectedItem());
        recipe.setRequiredTime(durationInput.parse());
        recipe.setPortions(portionInput.parse());
        recipe.setInstructions(instructionsInput.getText());
        recipe.setIngredients(ingredients);
        model.updateRow(recipe);
    }

    private void addRecipe(RecipeTableModel model, List<RecipeIngredient> ingredients) {
        Recipe recipe = new Recipe(
            nameInput.getText(),
            descriptionInput.getText(),
            instructionsInput.getText(),
            (Category) categoriesInput.getSelectedItem(),
            durationInput.parse(),
            portionInput.parse(),
            ingredients
        );
        model.addRow(recipe);
    }
}
