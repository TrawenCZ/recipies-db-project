package cz.muni.fi.pv168.gui.layouts.tables;

import cz.muni.fi.pv168.model.Category;
import cz.muni.fi.pv168.model.Ingredient;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class IngredientTableLayout extends AbstractTableModel {
    private final List<Ingredient> ingredients;

    private final String[] columnNames = {
            "Name",
            "Kcal"
    };


    public IngredientTableLayout(List<Ingredient> ingredients) {
        this.ingredients = new ArrayList<>(ingredients);
    }

    public IngredientTableLayout() {
        this.ingredients = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return ingredients.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Ingredient ingredient = getEntity(rowIndex);

        switch (columnIndex) {
            case 0:
                return ingredient.getName();
            case 1:
                return ingredient.getKcal();
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
            case 4:
                return String.class;
            case 1:
                return Category.class;
            case 2:
            case 3:
                return int.class;
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
        var ingredient = ingredients.get(rowIndex);
        switch (columnIndex) {
            case 0:
                ingredient.setName((String) value);
                break;
            case 1:
                ingredient.setKcal((Double) value);
                break;
            default:
                throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        }
    }

    public void deleteRow(int rowIndex) {
        ingredients.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(Ingredient ingredient) {
        int newRowIndex = ingredients.size();
        ingredients.add(ingredient);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Ingredient ingredient) {
        int rowIndex = ingredients.indexOf(ingredient);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public Ingredient getEntity(int rowIndex) {
        return ingredients.get(rowIndex);
    }
}
