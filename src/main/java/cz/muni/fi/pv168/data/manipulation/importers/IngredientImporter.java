package cz.muni.fi.pv168.data.manipulation.importers;

import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.data.manipulation.Operation;
import cz.muni.fi.pv168.data.manipulation.acceptors.IngredientAcceptor;
import cz.muni.fi.pv168.data.manipulation.acceptors.ObjectAcceptor;
import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.repository.RepositoryFactory;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Importer into database, handles the import of a ingredient objects.
 *
 * @author Jan Martinek, Radim Stejskal
 */
public class IngredientImporter extends ObjectImporter<Ingredient> {

    private final RepositoryFactory factory;

    public IngredientImporter(RepositoryFactory factory, Supplier<TransactionHandler> transactions) {
        super(null, transactions);
        this.factory = factory;
    }

    @Override
    protected ObjectAcceptor<Ingredient> getAcceptor(Supplier<ConnectionHandler> connection, Consumer<Operation> submit) {
        factory.setConnection(connection);
        var units = factory.buildUnitRepository();
        return new IngredientAcceptor(
            factory.buildIngredientRepository(units),
            submit,
            units
        );
    }
}
