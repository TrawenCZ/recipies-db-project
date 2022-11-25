package cz.muni.fi.pv168.data.manipulation.services;

import cz.muni.fi.pv168.gui.frames.MainWindow;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.model.Identifiable;
import cz.muni.fi.pv168.model.Nameable;
import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.repository.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import javax.swing.*;

/**
 * Database manipulation service. Handles bulk save and get.
 *
 * @param <M> the type of the model
 * @author Jan Martinek, Radim Stejskal
 */
public class ServiceImpl<M extends Nameable & Identifiable> implements Service<M> {

    protected class Counter {
        public int imported = 0;
        public int actioned = 0;
        public boolean doReplace = false;
    }

    protected final Supplier<TransactionHandler> transactions;
    protected final Repository<M> repository;

    public ServiceImpl(Repository<M> repository, Supplier<TransactionHandler> transactions) {
        this.transactions = Objects.requireNonNull(transactions);
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Collection<M> findAll() {
        return repository.findAll();
    }

    @Override
    public List<M> getRecords(int[] indexes) {
        if (indexes == null || indexes.length == 0) return List.of();
        List<M> records = new ArrayList<>();
        for (var i : indexes) {
            records.add(repository.findByIndex(i).orElseThrow());
        }
        return records;
    }

    @Override
    public int[] saveRecords(Collection<M> records) {
        Counter counter = new Counter();

        try (var transaction = transactions.get()) {
            doImport(records, counter, repository, transaction::connection);
            transaction.commit();
        }
        return (counter.doReplace)
            ? new int[]{counter.imported, -counter.actioned}
            : new int[]{counter.imported, counter.actioned};
    }

    @Override
    public Optional<M> findRecordByName(String name) {
        return repository.findByName(name);
    }

    protected <NI extends Nameable & Identifiable> void doImport(
        Collection<NI> records,
        Counter counter,
        Repository<NI> repository,
        Supplier<ConnectionHandler> connection
    ) {
        for (var record : records) {
            var found = repository.findUncommitted(record.getName(), connection);
            if (found.isEmpty()) {
                create(record, repository, connection);
                counter.imported++;
            } else {
                if (found.get().equals(record)) {
                    continue;
                }
                if (counter.actioned == 0) counter.doReplace = getDecision();
                if (counter.doReplace) {
                    record.setId(found.get().getId());
                    update(record, repository, connection);
                }
                counter.actioned++;
            }
        }
    }

    protected static <N extends Nameable> void create(N entity, Repository<N> repository, Supplier<ConnectionHandler> connection) {
        repository.uncommitted(entity, repository::create, connection);
    }

    protected static <N extends Nameable> void update(N entity, Repository<N> repository, Supplier<ConnectionHandler> connection) {
        repository.uncommitted(entity, repository::update, connection);
    }

    protected static boolean getDecision() {
        String[] options = {"Replace all", "Skip all", "Cancel"};
        int n = JOptionPane.showOptionDialog(
            new JFrame(),
            "Duplicates were found during import! Please select an action:",
            "Import error",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            Icons.IMPORT_S,
            options,
            options[1]
        );
        if (n == JOptionPane.CANCEL_OPTION) throw new RuntimeException("Import canceled by user");
        return n == JOptionPane.YES_OPTION;
    }
}
