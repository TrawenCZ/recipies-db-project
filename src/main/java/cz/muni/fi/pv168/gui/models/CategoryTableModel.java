package cz.muni.fi.pv168.gui.models;

import cz.muni.fi.pv168.model.Category;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Model of category data class in a tabular representation
 */
public class CategoryTableModel extends TableModel<Category> {

    private final List<Category> categories;

    private final List<Column<Category, ?>> columns = List.of(
        Column.readonly("Name", String.class, Category::getName),
        Column.readonly("Color", Color.class, Category::getColor)
    );

    public CategoryTableModel() {
        this(new ArrayList<Category>());
    }

    public CategoryTableModel(List<Category> categories) {
        this.categories = new ArrayList<>(categories);
    }

    @Override
    protected List<Category> getEntities() {
        return categories;
    }

    @Override
    protected List<Column<Category, ?>> getColumns() {
        return columns;
    }
}

