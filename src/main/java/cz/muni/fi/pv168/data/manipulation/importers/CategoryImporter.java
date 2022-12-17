package cz.muni.fi.pv168.data.manipulation.importers;

import cz.muni.fi.pv168.model.Category;
import cz.muni.fi.pv168.data.manipulation.Operation;
import cz.muni.fi.pv168.data.manipulation.acceptors.CategoryAcceptor;
import cz.muni.fi.pv168.data.manipulation.acceptors.ObjectAcceptor;
import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.repository.Repository;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Importer into database, handles the import of a category objects.
 *
 * @author Jan Martinek
 */
public class CategoryImporter extends ObjectImporter<Category> {

    public CategoryImporter(Function<Supplier<ConnectionHandler>, Repository<Category>> categories, Supplier<TransactionHandler> transactions) {
        super(categories, transactions);
    }

    @Override
    protected ObjectAcceptor<Category> getAcceptor(Supplier<ConnectionHandler> connection, Consumer<Operation> submit) {
        return new CategoryAcceptor(repositories.apply(connection), submit);
    }
}
