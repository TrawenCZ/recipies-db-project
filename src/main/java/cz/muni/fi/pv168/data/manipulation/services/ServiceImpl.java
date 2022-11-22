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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import javax.swing.JOptionPane;

/**
 * Database manipulation service. Takes care of database transactions.
 *
 * @param <M> the type of the model
 * @author Jan Martinek, Radim Stejskal
 */
public class ServiceImpl<M extends Nameable & Identifiable> implements Service<M> {

    protected final Supplier<TransactionHandler> transactions;
    protected final Repository<M> repository;

    public ServiceImpl(Repository<M> repository, Supplier<TransactionHandler> transactions) {
        this.transactions = Objects.requireNonNull(transactions);
        this.repository = Objects.requireNonNull(repository);
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
        Collection<M> duplicates = getDuplicates(records, repository);

        int imported = 0;
        boolean replace = (duplicates.size() > 0) ? getDecision() : false;

        try (var transaction = transactions.get()) {
            imported += doImport(records, duplicates,  replace, repository, transaction::connection);
            transaction.commit();
        }
        return (replace)
            ? new int[]{imported, -duplicates.size()}
            : new int[]{imported, duplicates.size()};
    }

    protected static <NI extends Nameable & Identifiable> int doImport(
        Collection<NI> records,
        Collection<NI> duplicates,
        boolean doReplace,
        Repository<NI> repository,
        Supplier<ConnectionHandler> connection
    ) {
        int imported = 0;
        for (var record : records) {
            var found = getDuplicate(record, duplicates);
            if (found == null) {
                create(record, repository, connection);
                imported++;
            } else if (doReplace) {
                record.setId(found.getId());
                update(record, repository, connection);
            }
        }
        return imported;
    }

    protected static <N extends Nameable> void create(N entity, Repository<N> repository, Supplier<ConnectionHandler> connection) {
        repository.uncommitted(entity, repository::create, connection);
    }

    protected static <N extends Nameable> void update(N entity, Repository<N> repository, Supplier<ConnectionHandler> connection) {
        repository.uncommitted(entity, repository::update, connection);
    }

    protected static <N extends Nameable> Collection<N> getDuplicates(Collection<N> records, Repository<N> repository) {
        Set<N> duplicate = new HashSet<>();
        for (var r : records) {
            var found = repository.findByName(r.getName()).orElse(null);
            if (found != null) duplicate.add(found);
        }
        return duplicate;
    }

    protected static <N extends Nameable> N getDuplicate(N entity, Collection<N> duplicates) {
        return duplicates.stream()
                         .dropWhile(e -> !e.getName().equals(entity.getName()))
                         .findFirst()
                         .orElse(null);
    }

    protected static boolean getDecision() {
        String[] options = {"Replace all", "Skip all", "Cancel"};
        int n = JOptionPane.showOptionDialog(
            MainWindow.getContentPane(),
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
