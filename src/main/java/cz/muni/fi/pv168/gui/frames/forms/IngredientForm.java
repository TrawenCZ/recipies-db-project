package cz.muni.fi.pv168.gui.frames.forms;

import cz.muni.fi.pv168.gui.elements.text.DoubleFormatter;
import cz.muni.fi.pv168.gui.elements.text.DoubleTextField;
import cz.muni.fi.pv168.gui.frames.MainWindow;
import cz.muni.fi.pv168.model.BaseUnitsEnum;
import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.Unit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

public class IngredientForm extends AbstractForm {

    private final static String DEFAULT_ENERGY_TEXT = " Energy value (kcal) per 100g (*)";
    private final static String DEFAULT_CONVERSION_TEXT = "Enable kcal/unit conversion";

    // all of those have added spaces to make it look better
    private final JLabel nameLabel = new JLabel(" Name (*)");
    private final JLabel energyLabel = new JLabel(DEFAULT_ENERGY_TEXT);
    private final JLabel ingredientValueLabel = new JLabel();

    private final JTextField nameInput = new JTextField(24);
    private final DoubleTextField energyInput = new DoubleTextField(0.01d, 8);
    private final DoubleTextField ingredientValueInput = new DoubleTextField(0.01d, 8);

    private final JToggleButton toggleButton = new JToggleButton(DEFAULT_CONVERSION_TEXT);
    private final JLabel unitsLabel = new JLabel(" Units");
    private final JComboBox<Unit> unitsComboBox = new JComboBox<>(getAllUnits());

    private boolean customEnergyEnabled;
    private Ingredient ingredient;

    private IngredientForm(String title, String header) {
        super(title, header);
        initializeBody();
    }

    public IngredientForm(Ingredient ingredient) {
        this(EDIT, "Edit ingredient");
        this.ingredient = ingredient;
        addSampleData(ingredient);
        show();
    }

    public IngredientForm() {
        this("Add", "New ingredient");
        show();
    }

    @Override
    protected void initializeBody() {
        nameInput.setToolTipText(NAME_TOOLTIP + "INGREDIENTS!");

        gridExtensions(GridBagConstraints.HORIZONTAL, 0, 5);

        // labels
        gridInsets(10);
        gridAdd(nameLabel, 0, 1);
        gridAdd(energyLabel, 0, 2);
        gridAdd(ingredientValueLabel, 0, 3);
        gridAdd(unitsLabel, 0, 4);
        gridAdd(toggleButton, 0, 5, 2, 1);

        // spacing
        gridInsets(10, 10, 10, 250);
        gridAdd(new JLabel(), 0, 0, 1, 4);

        // inputs
        gridInsets(10, -80, 10, 10);
        gridAdd(nameInput, 1, 1);
        gridAdd(energyInput, 1, 2);
        gridAdd(ingredientValueInput, 1, 3);
        gridAdd(unitsComboBox, 1, 4);

        ingredientValueLabel.setEnabled(false);
        ingredientValueInput.setEnabled(false);
        ingredientValueInput.setText("100");
        unitsLabel.setEnabled(false);
        unitsComboBox.setEnabled(false);

        // load default value to ingredientValueLabel
        comboBoxListener(null);

        unitsComboBox.addItemListener(this::comboBoxListener);
        toggleButton.addItemListener(this::toggleListener);
    }

    @Override
    protected boolean onAction() {
        var tableModel = MainWindow.getIngredientModel();
        if (!verifyName(tableModel, ingredient, nameInput.getText())) {
            return false;
        }

        saveIngredient();
        return true;
    }

    private void saveIngredient() {
        double energyValueNumber = (double) energyInput.parse();
        BaseUnitsEnum baseUnit = BaseUnitsEnum.GRAM;
        Unit selected = null;

        if (customEnergyEnabled) {
            selected = (Unit) unitsComboBox.getSelectedItem();
            double baseUnitValue = selected.getValueInBaseUnit();
            baseUnit = selected.getBaseUnit();
            energyValueNumber /= (double) ingredientValueInput.parse() * baseUnitValue;
        } else {
            energyValueNumber /= 100;
        }

        Unit unit = (baseUnit == null) ? selected : MainWindow.getUnitsModel().getEntity(baseUnit.getValue());
        var tableModel = MainWindow.getIngredientModel();
        if (isEdit()) {
            ingredient.setName(nameInput.getText());
            ingredient.setKcal(energyValueNumber);
            ingredient.setUnit(unit);
            tableModel.updateRow(ingredient);
        } else {
            Ingredient ingredient = new Ingredient(nameInput.getText(), energyValueNumber, unit);
            tableModel.addRow(ingredient);
        }

    }

    private void toggleListener(ItemEvent itemEvent) {
        boolean condition = itemEvent.getStateChange() == ItemEvent.SELECTED;

        if (condition) {
            toggleButton.setText("Disable kcal/unit conversion");
            energyLabel.setText(" Energy value (kcal) per ingredient (*)");
        } else {
            toggleButton.setText(DEFAULT_CONVERSION_TEXT);
            energyLabel.setText(DEFAULT_ENERGY_TEXT);
        }

        customEnergyEnabled = condition;
        ingredientValueLabel.setEnabled(condition);
        ingredientValueInput.setEnabled(condition);
        unitsLabel.setEnabled(condition);
        unitsComboBox.setEnabled(condition);

        refreshContent();
    }

    private void comboBoxListener(ItemEvent itemEvent) {
        Unit selected = (Unit) unitsComboBox.getSelectedItem();
        String baseUnit = BaseUnitsEnum.GRAM.getValue();

        if (selected != null && selected.getBaseUnit() == null) {
            baseUnit = selected.getName();
        } else if (selected != null) {
            baseUnit = selected.getBaseUnit().getValue();
        }

        switch (baseUnit) {
            case "g" -> ingredientValueLabel.setText(" Ingredient weight");
            case "ml" -> ingredientValueLabel.setText(" Ingredient volume");
            case "pc(s)" -> ingredientValueLabel.setText(" Ingredient count");
        }
    }

    private Unit[] getAllUnitsByBaseUnit(BaseUnitsEnum baseUnit) {
        return MainWindow.getUnitsModel().getRepository().findAll().stream()
                .filter(e ->
                        (e.getBaseUnit() == null && baseUnit.getValue().equals(e.getName())) ||
                                (e.getBaseUnit() != null && baseUnit.equals(e.getBaseUnit()))
                ).toArray(Unit[]::new);
    }

    private void addSampleData(Ingredient ingredient) {
        nameInput.setText(ingredient.getName());
        energyInput.setText(DoubleFormatter.stringValueOfWithConversion(ingredient.getKcal() * 100));

        Unit baseUnit = ingredient.getUnit();
        Unit[] allUnitsByBaseUnit = getAllUnitsByBaseUnit(BaseUnitsEnum.stringToEnum(baseUnit.toString()));
        unitsComboBox.removeAllItems();

        for (Unit unit : allUnitsByBaseUnit) {
            unitsComboBox.addItem(unit);
        }

        if (!baseUnit.toString().equals("g")) {
            toggleButton.doClick();
            ingredientValueInput.setText("100");
            unitsComboBox.setSelectedItem(baseUnit);
        }
    }

    private Unit[] getAllUnits() {
        List<Unit> units = new ArrayList<>();
        var unitsTableModel = MainWindow.getUnitsModel();
        for (int i = 0; i < unitsTableModel.getRowCount(); i++) {
            units.add(unitsTableModel.getEntity(i));
        }
        return units.toArray(new Unit[0]);
    }
}
