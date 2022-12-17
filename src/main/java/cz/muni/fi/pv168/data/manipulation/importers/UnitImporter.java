package cz.muni.fi.pv168.data.manipulation.importers;

import cz.muni.fi.pv168.model.Unit;
import cz.muni.fi.pv168.data.manipulation.Operation;
import cz.muni.fi.pv168.data.manipulation.acceptors.ObjectAcceptor;
import cz.muni.fi.pv168.data.manipulation.acceptors.UnitAcceptor;
import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.repository.Repository;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Importer into database, handles the import of a unit objects.
 *
 * @author Jan Martinek
 */
public class UnitImporter extends ObjectImporter<Unit> {

    public UnitImporter(Function<Supplier<ConnectionHandler>, Repository<Unit>> categories, Supplier<TransactionHandler> transactions) {
        super(categories, transactions);
    }

    @Override
    protected ObjectAcceptor<Unit> getAcceptor(Supplier<ConnectionHandler> connection, Consumer<Operation> submit) {
        return new UnitAcceptor(repositories.apply(connection), submit);
    }
}
