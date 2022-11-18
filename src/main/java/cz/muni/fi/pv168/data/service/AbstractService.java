package cz.muni.fi.pv168.data.service;

import cz.muni.fi.pv168.exceptions.InconsistentRecordException;
import cz.muni.fi.pv168.gui.Validator;
import cz.muni.fi.pv168.model.Nameable;
import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.repository.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Database manipulation service. Takes care of database transactions.
 *
 * @param <M> the type of the model
 * @author Jan Martinek, Radim Stejskal
 */
public abstract class AbstractService<M extends Nameable> implements Service<M> {

    protected final Supplier<TransactionHandler> transactions;
    protected final Repository<M> repository;

    protected AbstractService(Repository<M> repository, Supplier<TransactionHandler> transactions) {
        this.transactions = Objects.requireNonNull(transactions);
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public List<M> selectRecords(Collection<Integer> indexes) {
        if (indexes == null || indexes.size() == 0) return List.of();
        var hashedIndexes = new HashSet<>(indexes);
        List<M> records = new ArrayList<>();
        for (var i : hashedIndexes) {
            var e = repository.findByIndex(i);
            if (e.isPresent()) records.add(e.get());
        }
        return records;
    }

    @Override
    public Collection<M> verifyRecords(Collection<M> records) throws InconsistentRecordException {
        var hashed = new HashSet<>(records);
        var unique = new HashSet<M>();

        // // validate
        // for (var record : hashed) {
        //     if (Validator.duplicateNotEqual(hashed, record)) {
        //         throwError(record, "selected file");
        //     } else if (Validator.duplicateNotEqual(repository, record)) {
        //         throwError(record, "database");
        //     } else if (Validator.isUnique(repository, record)) {
        //         unique.add(record);
        //     }
        // }
        return unique;
    }

    @Override
    public int saveRecords(Collection<M> records) throws InconsistentRecordException {
        return saveRecords(records, false);
    }

    @Override
    public int saveRecords(Collection<M> records, boolean disableVerification) throws InconsistentRecordException {
        try (var transaction = transactions.get()) {
        if (disableVerification) {
            records.forEach(repository::create);
        } else {
            records = verifyRecords(records);
            records.forEach(repository::create);
        }
        return records.size();
        }
    }

    /**
     * Delete transaction, first does all the validity checks, then tries
     * to delete the given records.
     *
     * @param records list of records we want to delete
     * @throws InconsistentRecordException whenever a validity check fails
     */
    @Override
    public abstract void deleteRecords(Collection<M> records);

    private void throwError(M record, String location) throws InconsistentRecordException {
        throw new InconsistentRecordException(
            repository + " of name " +
            record.getName() + " already exists in " +
            location + ", but with different properties!"
        );
    }
}
