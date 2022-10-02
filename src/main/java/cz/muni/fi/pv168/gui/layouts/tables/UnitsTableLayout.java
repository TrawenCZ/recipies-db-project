package cz.muni.fi.pv168.gui.layouts.tables;

import cz.muni.fi.pv168.model.Unit;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: refactor
 */
public class UnitsTableLayout extends AbstractTableModel {

    private final List<Unit> units;

    private final String[] columnNames = {
        "Name",
        "In grams",
    };


    public UnitsTableLayout(List<Unit> units) {
        this.units = new ArrayList<>(units);
    }

    public UnitsTableLayout() {
        this.units = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return units.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var unit = getEntity(rowIndex);

        switch (columnIndex) {
            case 0:
                return unit.getName();
            case 1:
                return unit.getValueInGrams();
            default:
                throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex >= getColumnCount()) {
            throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        }

        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return Double.class;
            default:
                throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
                return false;
            default:
                throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        var unit = units.get(rowIndex);
        switch (columnIndex) {
            case 0:
                unit.setName((String) value);
                break;
            case 1:
                unit.setValueInGrams((Double) value);
                break;
            default:
                throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        }
    }

    public void deleteRow(int rowIndex) {
        units.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(Unit unit) {
        int newRowIndex = units.size();
        units.add(unit);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Unit unit) {
        int rowIndex = units.indexOf(unit);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public Unit getEntity(int rowIndex) {
        return units.get(rowIndex);
    }
}

