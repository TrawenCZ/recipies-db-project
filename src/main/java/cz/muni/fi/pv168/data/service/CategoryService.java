package cz.muni.fi.pv168.data.service;

import cz.muni.fi.pv168.gui.models.CategoryTableModel;
import cz.muni.fi.pv168.model.Category;

import java.util.List;

/**
 * @author Radim Stejskal. Jan Martinek
 */
public class CategoryService extends AbstractService<Category> {

    public CategoryService(CategoryTableModel categoryRepository) {
        super(categoryRepository, "Category");
    }

    @Override
    public void deleteRecords(List<Category> records) {

    }
}
