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
    public int saveRecords(Collection<M> records) {
        Collection<M> duplicates = getDuplicates(records);
        boolean replace = (duplicates.size() > 0) ? getDecision() : false;

        try (var transaction = transactions.get()) {
            for (var record : records) {
                var found = getDuplicate(record, duplicates);
                if (found == null) {
                    create(record, transaction::connection);
                } else if (replace) {
                    record.setId(found.getId());
                    update(record, transaction::connection);
                }
            }
            transaction.commit();
        }
        return duplicates.size();
    }

    protected void create(M entity, Supplier<ConnectionHandler> connection) {
        repository.uncomitted(entity, repository::create, connection);
    }

    protected void update(M entity, Supplier<ConnectionHandler> connection) {
        repository.uncomitted(entity, repository::update, connection);
    }

    private Collection<M> getDuplicates(Collection<M> records) {
        List<M> duplicate = new ArrayList<>();
        for (var record : records) {
            var found = repository.findByName(record.getName()).orElse(null);
            if (found != null) duplicate.add(record);
        }
        return duplicate;
    }

    private M getDuplicate(M entity, Collection<M> duplicates) {
        return duplicates.stream()
                         .dropWhile(e -> !e.getName().equals(entity.getName()))
                         .findFirst()
                         .orElse(null);
    }

    private boolean getDecision() {
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
