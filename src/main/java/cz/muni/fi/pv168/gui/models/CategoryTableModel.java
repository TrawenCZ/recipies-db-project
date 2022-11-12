package cz.muni.fi.pv168.gui.models;

import cz.muni.fi.pv168.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Model of category data class in a tabular representation
 *
 * @author Jan Martinek, Radim Stejskal
 */
public class CategoryTableModel extends AbstractModel<Category> {

    private final List<Category> categories;

    public CategoryTableModel() {
        this(new ArrayList<Category>());
    }

    public CategoryTableModel(List<Category> categories) {
        super(List.of(
            Column.readonly("Name", Category.class, self -> self, null)
        ));
        this.categories = new ArrayList<>(categories);
    }

    @Override
    public List<Category> getEntities() {
        return categories;
    }

    @Override
    public String toString() {
        return "Categories";
    }

}
