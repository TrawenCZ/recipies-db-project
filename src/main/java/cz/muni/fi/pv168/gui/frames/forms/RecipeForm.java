package cz.muni.fi.pv168.gui.frames.forms;

import cz.muni.fi.pv168.gui.TextValidator;
import cz.muni.fi.pv168.gui.elements.ScrollPane;
import cz.muni.fi.pv168.gui.elements.text.DoubleTextField;
import cz.muni.fi.pv168.gui.elements.text.IntegerTextField;
import cz.muni.fi.pv168.gui.frames.MainWindow;
import cz.muni.fi.pv168.gui.models.RecipeTableModel;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.model.*;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.function.Function;

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

    private Recipe recipe;

    private class SingleIngredient extends JPanel {

        private Long id = null;
        private JComboBox<Ingredient> ingredientComboBox = new JComboBox<>(getAllIngredients());
        private JComboBox<Unit> unitComboBox;

        private DoubleTextField ingredientAmount = new DoubleTextField(0.01d, 4);
        private JButton removeIngredient         = new JButton(Icons.getScaledIcon((ImageIcon)Icons.DELETE_S, 16));

        public SingleIngredient(RecipeIngredient ingredient) {
            super(new GridBagLayout());

            if (ingredient != null) {
                id = ingredient.getId();
                ingredientComboBox.setSelectedItem(ingredient.getIngredient());
                ingredientAmount.setText(String.valueOf(ingredient.getAmount()));
            }

            unitComboBox = new JComboBox<>(getAllUnitsByBaseUnit(safeBaseComboboxSelect()));
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

        public RecipeIngredient getIngredientAmount() {
            return new RecipeIngredient(
                id,
                (recipe == null) ? null : recipe.getId(),
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
            return MainWindow.getIngredientModel().getRepository().findAll().stream()
                .toArray(size -> new Ingredient[size]);
        }

        private Unit[] getAllUnitsByBaseUnit(BaseUnitsEnum baseUnit) {
            return MainWindow.getUnitsModel().getRepository().findAll().stream()
                .filter(e ->
                    (e.getBaseUnit() == null && baseUnit.getValue().equals(e.getName())) ||
                    (e.getBaseUnit() != null && baseUnit.equals(e.getBaseUnit()))
                ).toArray(size -> new Unit[size]);
        }

        private void ingredientListener(ItemEvent e) {
            Unit[] allUnitsByBaseUnit = getAllUnitsByBaseUnit(safeBaseComboboxSelect());
            unitComboBox.removeAllItems();

            for (Unit unit : allUnitsByBaseUnit) {
                unitComboBox.addItem(unit);
            }
            unitComboBox.setSelectedItem(allUnitsByBaseUnit[0]);
        }

        private BaseUnitsEnum safeBaseComboboxSelect() {
            Ingredient selectedIngredient = (Ingredient) ingredientComboBox.getSelectedItem();
            var selected = BaseUnitsEnum.GRAM;

            if (selectedIngredient != null) {
                selected = selectedIngredient.getUnit().getBaseUnit();
                if (selected == null) selected = BaseUnitsEnum.stringToEnum(selectedIngredient.getUnit().getName());
            }

            return selected;
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
        super("Details", header, null, "Close");
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
                new SingleIngredient();
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

        List<RecipeIngredient> ingredientsList = ingredients.getAll().stream()
            .map(SingleIngredient::getIngredientAmount)
            .toList();

        if (ingredientsList == null || ingredientsList.size() == 0) {
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
        nameInput.setText(recipe.getName());
        descriptionInput.setText(recipe.getDescription());
        instructionsInput.setText(recipe.getInstructions());
        categoriesInput.setSelectedItem(recipe.getCategory());
        durationInput.setText(String.valueOf(recipe.getRequiredTime()));
        portionInput.setText(String.valueOf(recipe.getPortions()));
    }

    private Category[] getAllCategories() {
        List<Category> categories = MainWindow.getCategoryModel().getRepository().findAll();
        return categories.toArray(new Category[0]);
    }

    protected void setIngredients(Recipe recipe, Function<RecipeIngredient, ?> constructor) {
        for (var i : recipe.getIngredients()) {
            constructor.apply(i);
        }
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
