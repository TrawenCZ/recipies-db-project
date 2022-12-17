package cz.muni.fi.pv168.data.manipulation.importers;

import cz.muni.fi.pv168.model.Identifiable;
import cz.muni.fi.pv168.model.Nameable;
import cz.muni.fi.pv168.data.manipulation.DuplicateException;
import cz.muni.fi.pv168.data.manipulation.Operation;
import cz.muni.fi.pv168.data.manipulation.Progress;
import cz.muni.fi.pv168.data.manipulation.acceptors.ObjectAcceptor;
import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.repository.Repository;

import java.sql.Connection;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.tinylog.Logger;

/**
 * Importer into database, handles the import of a given collection of objects.
 *
 * @param <M> the type of the model
 * @author Jan Martinek, Radim Stejskal
 */
public class ObjectImporter<M extends Nameable & Identifiable> {

    protected final Function<Supplier<ConnectionHandler>, Repository<M>> repositories;
    protected final Supplier<TransactionHandler> transactions;

    /**
     * Constructs the importer with a transaction supplier
     * and a repository supplier from supplied connections.
     *
     * @param repositories creater of repository for given connection
     * @param transactions transaction supplier
     */
    public ObjectImporter(
        Function<Supplier<ConnectionHandler>, Repository<M>> repositories,
        Supplier<TransactionHandler> transactions
    ) {
        this.repositories = repositories; // may be null (for extensibility)
        this.transactions = Objects.requireNonNull(transactions);
    }

    /**
     * Save transaction, this operation is atomic. However it needs
     * to be sanitized with a try-catch if you wish to prevent
     * application errors.
     *
     * @param records  list of records we want to save
     * @param progress progress tracker with replacement decision functionality
     */
    public void doImport(Collection<M> records, Progress progress) {
        progress.resetCount();
        progress.setWorkload(records.size());
        try (var transaction = setupTransaction()) {
            var acceptor = getAcceptor(transaction::connection, progress::submit);
            if (progress.isReplace()) acceptor.setReplace();
            for (var record : records) {
                acceptor.accept(record);
                if (!progress.ignoreDecided() && progress.getIgnore() > 0) {
                    throw new DuplicateException();
                }
                progress.submit(Operation.PROCESS);
            }
            transaction.commit();
        }
        progress.setIsDone();
    }

    protected ObjectAcceptor<M> getAcceptor(Supplier<ConnectionHandler> connection, Consumer<Operation> submit) {
        return new ObjectAcceptor<>(repositories.apply(connection), submit);
    }

    protected TransactionHandler setupTransaction() {
        TransactionHandler transaction = null;
        try {
            transaction = transactions.get();
            transaction.connection().use().setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        } catch (Exception e) {
            Logger.error("Could not setup transaction: " + e.getMessage());
            return null;
        }
        return transaction;
    }
}
