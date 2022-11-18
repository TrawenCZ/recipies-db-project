package cz.muni.fi.pv168.data.service;

import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.exceptions.InconsistentRecordException;
import cz.muni.fi.pv168.model.Category;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author Radim Stejskal. Jan Martinek
 */
public class CategoryService extends AbstractService<Category> {

    public CategoryService(Repository<Category> categoryRepository, Supplier<TransactionHandler> transactions) {
        super(categoryRepository, transactions);
    }

    @Override
    public int saveRecords(Collection<Category> records, boolean disableVerification) throws InconsistentRecordException {
        records.removeIf(category -> category.getName().isBlank());
        return super.saveRecords(records, disableVerification);
    }

    @Override
    public void deleteRecords(Collection<Category> records) {

    }
}
