package cz.muni.fi.pv168.data.service;

import cz.muni.fi.pv168.exceptions.InconsistentRecordException;
import cz.muni.fi.pv168.gui.models.CategoryTableModel;
import cz.muni.fi.pv168.model.Category;

import java.util.Collection;

/**
 * @author Radim Stejskal. Jan Martinek
 */
public class CategoryService extends AbstractService<Category> {

    public CategoryService(CategoryTableModel categoryRepository) {
        super(categoryRepository, "Category");
    }

    @Override
    protected int saveRecords(Collection<Category> records, boolean disableVerification) throws InconsistentRecordException {
        records.removeIf(category -> category.getName().isBlank());
        return super.saveRecords(records, disableVerification);
    }

    @Override
    public void deleteRecords(Collection<Category> records) {

    }
}
