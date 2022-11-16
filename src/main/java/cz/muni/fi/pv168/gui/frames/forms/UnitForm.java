package cz.muni.fi.pv168.gui.frames.forms;

import cz.muni.fi.pv168.gui.elements.text.DoubleTextField;
import cz.muni.fi.pv168.gui.frames.TabLayout;
import cz.muni.fi.pv168.gui.models.UnitsTableModel;
import cz.muni.fi.pv168.model.BaseUnitsEnum;
import cz.muni.fi.pv168.model.Unit;

import javax.swing.*;
import java.awt.*;

import static cz.muni.fi.pv168.gui.resources.Messages.ADDING_ERR_TITLE;
import static cz.muni.fi.pv168.gui.resources.Messages.EDITING_ERR_TITLE;

public class UnitForm extends AbstractForm {

    JPanel newPanel = new JPanel(new GridBagLayout());

    private JLabel nameLabel = new JLabel("Name");
    private JLabel unitLabel = new JLabel("Value");
    private JLabel equivalentLabel = new JLabel("per unit");

    private JTextField nameInput = new JTextField(24);
    private DoubleTextField unitInput = new DoubleTextField(0.01d, 8);
    private JComboBox<String> equivalentInput = new JComboBox<>(BaseUnitsEnum.getAllValues());
    private Unit unit;

    private UnitForm(String title, String header) {
        super(title, header);
        initializeBody();
    }

    public UnitForm(Unit unit) {
        this(EDIT, "Edit unit");
        addData(unit);
        this.unit = unit;
        show();
    }

    public UnitForm() {
        this("Add", "New unit");
        show();
    }

    @Override
    protected void initializeBody() {
        nameInput.setToolTipText(NAME_TOOLTIP + "UNITS!");

        gridExtensions(GridBagConstraints.HORIZONTAL, 0, 5);

        gridInsets(10);
        gridAdd(nameLabel, 0, 0);
        gridAdd(unitLabel, 0, 2);
        gridAdd(equivalentLabel, 1, 2);

        gridInsets(-20, 10, 10, 10);
        gridAdd(nameInput, 0, 1, 2, 1);
        gridAdd(unitInput, 0, 3);
        gridAdd(equivalentInput, 1, 3);
    }

    @Override
    protected boolean onAction() {
        var tableModel = (UnitsTableModel) TabLayout.getUnitsModel();
        if (!verifyName(tableModel, unit, nameInput.getText())) {
            return false;
        }

        BaseUnitsEnum baseUnit = baseUnitFromString(equivalentInput.getSelectedItem().toString());
        double valueInBaseUnit = unitInput.parse();
        if (isEdit()) {
            unit.setName(nameInput.getText());
            unit.setValueInBaseUnit(valueInBaseUnit);
            unit.setBaseUnit(baseUnit);
            tableModel.updateRow(unit);
        } else {
            //tableModel.addRow(new Unit(nameInput.getText(), valueInBaseUnit, baseUnit));
        }
        return true;
    }

    private void addData(Unit unit) {
        nameInput.setText(unit.getName());
        unitInput.setText(String.valueOf(unit.getValueInBaseUnit()));
        equivalentInput.setSelectedItem(unit.getBaseUnitValue());
    }
}
