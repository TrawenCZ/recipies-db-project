package cz.muni.fi.pv168.data.manipulation.importers;

import cz.muni.fi.pv168.gui.frames.MainWindow;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.model.Identifiable;
import cz.muni.fi.pv168.model.Nameable;
import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.repository.Repository;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.*;

/**
 * Importer into database, handles the import of a given collection of objects.
 *
 * @param <M> the type of the model
 * @author Jan Martinek, Radim Stejskal
 */
public class ObjectImporter<M extends Nameable & Identifiable> {

    protected class Counter {
        public int imported = 0;
        public int actioned = 0;
        public boolean doReplace = false;
    }

    protected final Supplier<TransactionHandler> transactions;
    protected final Function<Supplier<ConnectionHandler>, Repository<M>> repositories;

    protected Counter counter;

    /**
     * Constructs the importer with a transaction supplier
     * and a repository supplier from supplied connections.
     *
     * @param repositories repository generator from supplied connections
     * @param transactions transaction supplier
     */
    public ObjectImporter(
        Function<Supplier<ConnectionHandler>, Repository<M>> repositories,
        Supplier<TransactionHandler> transactions
    ) {
        this.transactions = Objects.requireNonNull(transactions);
        this.repositories = Objects.requireNonNull(repositories);
    }

    /**
     * Save transaction, this operation is atomic. However it needs
     * to be sanitized with a try-catch if you wish to prevent
     * application errors.
     *
     * @param records list of records we want to save
     * @return int[2] array: [0] => imported, [1] => (-)replaced/(+)discarded
     */
    public int[] saveRecords(Collection<M> records) {
        counter = new Counter();
        try (var transaction = transactions.get()) {
            var repository = repositories.apply(transaction::connection);
            doImport(
                records,
                repository::findByName,
                repository::create,
                repository::update
            );
            transaction.commit();
        }
        return (counter.doReplace)
            ? new int[]{counter.imported, -counter.actioned}
            : new int[]{counter.imported, counter.actioned};
    }

    protected <N extends Nameable & Identifiable> void doImport(
        Collection<N> records,
        Function<String, Optional<N>> find,
        Consumer<N> create,
        Consumer<N> update
    ) {
        for (var record : records) {
            var found = find.apply(record.getName());
            if (found.isEmpty()) {
                create.accept(record);
                counter.imported++;
            } else {
                if (found.get().equals(record)) continue;
                if (counter.actioned == 0) counter.doReplace = getDecision();
                if (counter.doReplace) {
                    record.setId(found.get().getId());
                    update.accept(record);
                }
                counter.actioned++;
            }
        }
    }

    protected static boolean getDecision() {
        String[] options = {"Replace all", "Skip all", "Cancel"};
        int n = JOptionPane.showOptionDialog(
            MainWindow.getGlassPane(),
            "Duplicates were found during import! Please select an action:",
            "Import error",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            Icons.IMPORT_S,
            options,
            options[1]
        );
        if (n == JOptionPane.CANCEL_OPTION)
            throw new RuntimeException("Import cancelled by the user");
        return n == JOptionPane.YES_OPTION;
    }
}
