package cz.muni.fi.pv168.gui.models;

import java.util.List;

import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.model.Category;
import cz.muni.fi.pv168.wiring.Supported;

/**
 * Model of category data class in a tabular representation
 *
 * @author Jan Martinek, Radim Stejskal
 */
public class CategoryTableModel extends AbstractModel<Category> {

    public CategoryTableModel(Repository<Category> categories) {
        super(List.of(
            Column.readonly("Name", Category.class, self -> self, null)),
            categories
        );
    }

    @Override
    public String toString() {
        return Supported.CATEGORY;
    }
}
