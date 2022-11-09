package cz.muni.fi.pv168.gui.frames.forms;

import cz.muni.fi.pv168.gui.TextValidator;
import cz.muni.fi.pv168.gui.elements.ScrollPane;
import cz.muni.fi.pv168.gui.elements.text.DoubleTextField;
import cz.muni.fi.pv168.gui.elements.text.IntegerTextField;
import cz.muni.fi.pv168.gui.frames.TabLayout;
import cz.muni.fi.pv168.gui.models.CategoryTableModel;
import cz.muni.fi.pv168.gui.models.IngredientTableModel;
import cz.muni.fi.pv168.gui.models.RecipeTableModel;
import cz.muni.fi.pv168.gui.models.UnitsTableModel;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.model.*;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.swing.*;

import static cz.muni.fi.pv168.gui.resources.Messages.ADDING_ERR_TITLE;
import static cz.muni.fi.pv168.gui.resources.Messages.EDITING_ERR_TITLE;

public class RecipeForm extends AbstractForm {

    protected final JTextField nameInput = new JTextField(24);
    protected final IntegerTextField portionInput = new IntegerTextField(1, 6);
    protected final IntegerTextField durationInput = new IntegerTextField(1, 6);

    protected final JTextArea descriptionInput = new JTextArea(3,24);
    protected final JTextArea instructionsInput = new JTextArea(8,24);

    protected final JComboBox<Category> categoriesInput = new JComboBox<>(getAllCategories());

    protected final ScrollPane<SingleIngredient> ingredients = new ScrollPane<>();
    protected final JButton ingredientButton = new JButton("Add ingredient", Icons.getScaledIcon((ImageIcon) Icons.ADD_S, 20));

    private Recipe recipe;

    private class SingleIngredient extends JPanel {

        private JComboBox<Ingredient> ingredientComboBox = new JComboBox<>(getAllIngredients());
        private JComboBox<Unit> unitComboBox;

        private DoubleTextField ingredientAmount = new DoubleTextField(0.01d, 4);
        private JButton removeIngredient         = new JButton(Icons.getScaledIcon((ImageIcon)Icons.DELETE_S, 16));

        public SingleIngredient(IngredientAmount ingredient) {
            super(new GridBagLayout());

            if (ingredient != null) {
                ingredientComboBox.setSelectedItem(ingredient.getIngredient());
                Ingredient selectedIngredient = (Ingredient) ingredientComboBox.getSelectedItem();
                unitComboBox = new JComboBox<>(getAllUnitsByBaseUnit(selectedIngredient.getUnit().getBaseUnit()));
                ingredientAmount.setText(String.valueOf(ingredient.getAmount()));
                unitComboBox.setSelectedItem(ingredient.getUnit());
            } else {
                Ingredient selectedIngredient = (Ingredient) ingredientComboBox.getSelectedItem();
                unitComboBox = new JComboBox<>(getAllUnitsByBaseUnit(selectedIngredient.getUnit().getBaseUnit()));
            }
            ingredientComboBox.setToolTipText("Enter an ingredient");
            ingredientAmount.setToolTipText("Enter an amount");
            unitComboBox.setToolTipText("Enter an unit");
            removeIngredient.setToolTipText("Delete an ingredient");
            initializeLayout();

            removeIngredient.addActionListener(e -> removeIngredient());
            ingredientComboBox.addItemListener(this::ingredientListener);

            ingredients.addTyped(this);
            refreshContent();
        }

        public SingleIngredient() {
            this(null);
        }

        public IngredientAmount getIngredientAmount() {
            return new IngredientAmount(
                (Ingredient) ingredientComboBox.getSelectedItem(),
                (double) ingredientAmount.parse(),
                (Unit) unitComboBox.getSelectedItem()
            );
        }

        private void initializeLayout() {
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;
            c.ipady = 10;

            c.weightx = 3;
            c.gridx = 0;
            this.add(ingredientComboBox, c);

            c.weightx = 1;
            c.gridx = 2;
            this.add(unitComboBox, c);

            c.weightx = 0.5;
            c.gridx = 1;
            this.add(ingredientAmount, c);
            c.gridx = 4;
            this.add(removeIngredient, c);
        }

        private void removeIngredient() {
            ingredients.remove(this);
            refreshContent();
        }

        private Ingredient[] getAllIngredients() {
            List<Ingredient> ingredients = new ArrayList<>();
            var ingredientsModel = (IngredientTableModel) TabLayout.getIngredientsModel();
            for (int i = 0; i < ingredientsModel.getRowCount(); i++) {
                ingredients.add(ingredientsModel.getEntity(i));
            }
            return ingredients.toArray(new Ingredient[0]);
        }

        private void ingredientListener(ItemEvent e) {
            Ingredient selectedIngredient = (Ingredient) ingredientComboBox.getSelectedItem();
            unitComboBox.removeAllItems();
            Unit[] allUnitsByBaseUnit = getAllUnitsByBaseUnit(selectedIngredient.getUnit().getBaseUnit());
            for (Unit unit : allUnitsByBaseUnit) {
                unitComboBox.addItem(unit);
            }
            unitComboBox.setSelectedItem(allUnitsByBaseUnit[0]);
        }

        private Unit[] getAllUnitsByBaseUnit(BaseUnitsEnum baseUnit) {
            List<Unit> units = new ArrayList<>();
            var unitsTableModel = (UnitsTableModel) TabLayout.getUnitsModel();
            for (int i = 0; i < unitsTableModel.getRowCount(); i++) {
                Unit selectedUnit = unitsTableModel.getEntity(i);
                if (selectedUnit.getBaseUnit().equals(baseUnit)) {
                    units.add(selectedUnit);
                }
            }
            return units.toArray(new Unit[0]);
        }
    }

    private RecipeForm(String title, String header) {
        super(title, header);
        initializeBody();
    }

    /**
     * Edit recipe
     */
    public RecipeForm(Recipe recipe) {
        this(EDIT, "Edit recipe");
        pack();
        setData(recipe);
        this.recipe = recipe;
        setIngredients(recipe, SingleIngredient::new);
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
    public RecipeForm(String header) {
        super("Details", header, null, "Go Back");
    }

    @Override
    protected void initializeBody() {
        nameInput.setToolTipText(NAME_TOOLTIP + "RECIPES!");
        descriptionInput.setLineWrap(true);
        descriptionInput.setWrapStyleWord(true);
        instructionsInput.setLineWrap(true);
        instructionsInput.setWrapStyleWord(true);

        ingredients.setPreferredSize(new Dimension(410, 500));
        ingredientButton.addActionListener(e -> new SingleIngredient());

        gridExtensions(GridBagConstraints.HORIZONTAL, 0, 5);

        // labels
        gridInsets(10, 10, 10, 10);
        gridAdd(new JLabel(" Name"), 0, 0);
        gridAdd(new JLabel(" Description"), 0, 2);
        gridAdd(new JLabel(" Category"), 0, 4);
        gridAdd(new JLabel(" Portions"), 2, 4);
        gridAdd(new JLabel(" Duration (min)"), 3, 4);
        gridAdd(new JLabel(" Instructions"), 0, 9);
        gridAdd(new JLabel(" Ingredients"), 4, 0);
        gridAdd(ingredientButton, 5, 0);

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
    }

    @Override
    protected boolean onAction() {
        var tableModel = (RecipeTableModel) TabLayout.getRecipesModel();
        if (!verifyName(tableModel, recipe, nameInput.getText())) {
            return false;
        }

        if (TextValidator.empty(descriptionInput.getText())) {
            // TODO: description empty
            return false;
        }
        if (TextValidator.empty(instructionsInput.getText())) {
            // TODO: instructions empty
            return false;
        }

        List<IngredientAmount> ingredientsList = ingredients.getAll().stream()
            .map(SingleIngredient::getIngredientAmount)
            .toList();

        if (ingredientsList == null || ingredientsList.size() == 0) {
            // TODO: at least one ingredient
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
        nameInput.setText(recipe.getName());
        descriptionInput.setText(recipe.getDescription());
        instructionsInput.setText(recipe.getInstructions());
        categoriesInput.setSelectedItem(recipe.getCategory());
        durationInput.setText(String.valueOf(recipe.getRequiredTime()));
        portionInput.setText(String.valueOf(recipe.getPortions()));
    }

    private Category[] getAllCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(Category.UNCATEGORIZED);
        var categoryModel = (CategoryTableModel) TabLayout.getCategoriesModel();
        for (int i = 0; i < categoryModel.getRowCount(); i++) {
            categories.add(TabLayout.getCategoriesModel().getEntity(i));
        }
        return categories.toArray(new Category[0]);
    }

    protected void setIngredients(Recipe recipe, Function<IngredientAmount, ?> constructor) {
        for (var i : recipe.getIngredients()) {
            constructor.apply(i);
        }
    }

    private void editRecipe(RecipeTableModel model, List<IngredientAmount> ingredients) {
        recipe.setName(nameInput.getText());
        recipe.setDescription(descriptionInput.getText());
        recipe.setCategory((Category) categoriesInput.getSelectedItem());
        recipe.setRequiredTime(durationInput.parse());
        recipe.setPortions(portionInput.parse());
        recipe.setInstructions(instructionsInput.getText());
        recipe.setIngredients(ingredients);
        model.updateRow(recipe);
    }

    private void addRecipe(RecipeTableModel model, List<IngredientAmount> ingredients) {
        model.addRow(new Recipe(
            nameInput.getText(),
            descriptionInput.getText(),
            instructionsInput.getText(),
            (Category) categoriesInput.getSelectedItem(),
            durationInput.parse(),
            portionInput.parse(),
            ingredients
        ));
    }
}
