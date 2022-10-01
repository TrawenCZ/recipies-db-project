package cz.muni.fi.pv168.gui.layouts.tables;

import cz.muni.fi.pv168.model.Category;
import cz.muni.fi.pv168.model.Recipe;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: refactor
 */
public class RecipeTableLayout extends AbstractTableModel {    
    
    private final List<Recipe> recipes;
    
    private final String[] columnNames = {
        "Name",
        "Category",
        "Required time",
        "Portions",
        "Description"
    };

    private final int[] columnSizes = {
        100,
        80,
        60,
        60,
        120
    };

    public RecipeTableLayout(List<Recipe> recipes) {
        this.recipes = new ArrayList<>(recipes);
    }

    public RecipeTableLayout() {
        this.recipes = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return recipes.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var recipe = getEntity(rowIndex);

        switch (columnIndex) {
            case 0:
                return recipe.getName();
            case 1:
                return recipe.getCategory().getName();
            case 2:
                return recipe.getRequiredTime();
            case 3:
                return recipe.getPortions();
            case 4:
                return recipe.getDescription();
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
            case 1:
                return String.class;
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
            case 2:
            case 3:
            case 4:
                return true;
            default:
                throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        var recipe = recipes.get(rowIndex);
        switch (columnIndex) {
            case 0:
                recipe.setName((String) value);
                break;
            case 1:
                recipe.setCategory(new Category((String)value));
                break;
            case 2:
                recipe.setRequiredTime((int) value);
                break;
            case 3:
                recipe.setPortions((int) value);
                break;
            case 4:
                recipe.setDescription((String) value);
                break;
            default:
                throw new IndexOutOfBoundsException("Invalid column index: " + columnIndex);
        }
    }

    public void deleteRow(int rowIndex) {
        recipes.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(Recipe recipe) {
        int newRowIndex = recipes.size();
        recipes.add(recipe);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Recipe recipe) {
        int rowIndex = recipes.indexOf(recipe);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public Recipe getEntity(int rowIndex) {
        return recipes.get(rowIndex);
    }

    public int getSize(int colIndex) {
        return columnSizes[colIndex % columnSizes.length];
    }

}

