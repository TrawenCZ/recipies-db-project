package cz.muni.fi.pv168.data.manipulation.acceptors;

import java.util.function.Consumer;

import cz.muni.fi.pv168.data.manipulation.Operation;
import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.model.Category;

/**
 * One time importer that is setup for a single transaction.
 * Transaction may have multiple importer instances, but only
 * one should be run at a time.
 *
 * @author Jan Martinek
 */
public class CategoryAcceptor extends ObjectAcceptor<Category> {

    public CategoryAcceptor(Repository<Category> repository, Consumer<Operation> publish) {
        super(repository, publish);
    }

    @Override
    public void accept(Category item) {
        if (Category.UNCATEGORIZED.equals(item)) {
            publish.accept(Operation.NONE);
        } else {
            super.accept(item);
        }
    }
}
