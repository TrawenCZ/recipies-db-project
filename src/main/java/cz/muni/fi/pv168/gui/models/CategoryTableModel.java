package cz.muni.fi.pv168.gui.models;

import java.util.List;

import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.model.Category;

/**
 * Model of category data class in a tabular representation
 *
 * @author Jan Martinek, Radim Stejskal
 */
public class CategoryTableModel extends AbstractModel<Category> {

    private final Repository<Category> categories;

    public CategoryTableModel(Repository<Category> categories) {
        super(List.of(
            Column.readonly("Name", Category.class, self -> self, null)
        ));
        this.categories = categories;
    }

    @Override
    public Repository<Category> getRepository() {
        return categories;
    }

    @Override
    public String toString() {
        return "Categories";
    }
}
