package cz.muni.fi.pv168.gui.elements;

import cz.muni.fi.pv168.gui.elements.text.DoubleFormatter;
import cz.muni.fi.pv168.model.RecipeIngredient;

import cz.muni.fi.pv168.gui.elements.text.DoubleTextField;
import cz.muni.fi.pv168.gui.frames.MainWindow;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.model.*;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.*;

/**
 * Element that renders a single recipe ingredient for use in GUI.
 * This class can be both editable and not.
 *
 * @author Jan Martinek
 */
public class SingleIngredient extends JPanel {

    private Long id = null;
    private Long recipeId = null;
    private JComboBox<Ingredient> ingredientComboBox = new JComboBox<>(getAllIngredients());
    private JComboBox<Unit> unitComboBox;

    private DoubleTextField ingredientAmount = new DoubleTextField(0.01d, 4);
    private JButton removeIngredient         = new JButton(Icons.getScaledIcon((ImageIcon)Icons.DELETE_S, 16));

    private SingleIngredient(Recipe recipe, RecipeIngredient ingredient, Consumer<SingleIngredient> remove, boolean isEditable) {
        super(new GridBagLayout());
        recipeId = (recipe == null) ? null : recipe.getId();

        if (ingredient != null) loadIngredient(ingredient);
        unitComboBox = new JComboBox<>(getAllUnitsByBaseUnit(safeBaseComboboxSelect()));
        if (ingredient != null) unitComboBox.setSelectedItem(ingredient.getUnit());

        ingredientComboBox.setToolTipText("Enter an ingredient");
        ingredientAmount.setToolTipText("Enter an amount");
        unitComboBox.setToolTipText("Enter an unit");
        removeIngredient.setToolTipText("Delete an ingredient");

        if (isEditable) removeIngredient.addActionListener(e -> remove.accept(this));
        if (isEditable) ingredientComboBox.addItemListener(this::ingredientListener);

        initializeLayout(isEditable);
    }

    /**
     * Creates an editable single ingredient, with values already set.
     *
     * @param recipe recipe this ingredient belongs to
     * @param remove method of removal from parent
     */
    public SingleIngredient(Recipe recipe, RecipeIngredient ingredient, Consumer<SingleIngredient> remove) {
        this(recipe, ingredient, remove, true);
    }

    /**
     * Creates an editable single ingredient, with no values set yet.
     *
     * @param recipe recipe this ingredient belongs to
     * @param remove method of removal from parent
     */
    public SingleIngredient(Recipe recipe, Consumer<SingleIngredient> remove) {
        this(recipe, null, remove);
    }

    /**
     * Creates an uneditable single ingredient.
     *
     * @param recipe        recipe this ingredient belongs to
     * @param ingredient    ingredient
     */
    public SingleIngredient(Recipe recipe, RecipeIngredient ingredient) {
        this(
            recipe,
            Objects.requireNonNull(ingredient, "Ingredient cannot be null when not editable"),
            null,
            false
        );
    }



    public RecipeIngredient getIngredient() {
        return new RecipeIngredient(
            id,
            recipeId,
            (Ingredient) ingredientComboBox.getSelectedItem(),
            (double) ingredientAmount.parse(),
            (Unit) unitComboBox.getSelectedItem()
        );
    }

    private void loadIngredient(RecipeIngredient ingredient) {
        id = ingredient.getId();
        ingredientComboBox.setSelectedItem(ingredient.getIngredient());
        ingredientAmount.setText(DoubleFormatter.stringValueOfWithConversion(ingredient.getAmount()));
    }

    private void initializeLayout(boolean isEditable) {
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

        if (isEditable) {
            c.weightx = 0.5;
            c.gridx = 4;
            this.add(removeIngredient, c);
        } else {
            disableEditing();
        }

        setMaxLengths();
    }

    private void setMaxLengths() {
        Unit protoryUnit = new Unit("LEN_8___", 1, null);
        Ingredient prototypeIngredient = new Ingredient("LEN_22________________", 1, protoryUnit);
        unitComboBox.setPrototypeDisplayValue(protoryUnit);
        ingredientComboBox.setPrototypeDisplayValue(prototypeIngredient);
    }

    private void disableEditing() {
        ingredientComboBox.setEnabled(false);
        unitComboBox.setEnabled(false);
        ingredientAmount.setEnabled(false);
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
