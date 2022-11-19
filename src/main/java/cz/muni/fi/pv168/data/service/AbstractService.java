package cz.muni.fi.pv168.data.service;

import cz.muni.fi.pv168.exceptions.ImportVsDatabaseRecordsConflictException;
import cz.muni.fi.pv168.exceptions.InconsistentRecordException;
import cz.muni.fi.pv168.gui.Validator;
import cz.muni.fi.pv168.model.Identifiable;
import cz.muni.fi.pv168.model.Nameable;
import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.repository.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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

    public Collection<M> filterValidRecords(Collection<M> records, boolean skipInconsistent) {
        records = new HashSet<>(records);
        Set<M> recordsToSave = new HashSet<>();

        for (var record : records) {
            if (repository.findByName(record.getName()).isPresent()) {
                if (Validator.duplicateNotEqual(repository, record) && !skipInconsistent) {
                    throw new ImportVsDatabaseRecordsConflictException();
                }
            } else {
                recordsToSave.add(record);
            }
        }
        return recordsToSave;
    }

    @Override
    public Collection<M> verifyRecords(Collection<M> records) throws InconsistentRecordException {
        var hashed = new HashSet<>(records);
        var unique = new HashSet<M>();

         // validate
         for (var record : hashed) {
             if (Validator.duplicateNotEqual(hashed, record)) {
                 throwError(record, "selected file");
             } else if (Validator.duplicateNotEqual(repository, record)) {
                 throwError(record, "database");
             } else if (Validator.isUnique(repository, record)) {
                 unique.add(record);
             }
         }
        return unique;
    }

    @Override
    public int saveRecords(Collection<M> records) throws ImportVsDatabaseRecordsConflictException {
        return saveRecords(records, false);
    }


    // TODO: implement dis
    @Override
    public int saveOrUpdateRecords(Collection<M> records) {
        records = new HashSet<>(records);
//
//        for (var record : records) {
//            if (repository.findByName(record.getName()).isPresent()) {
//                if (Validator.duplicateNotEqual(repository, record)) {
//                    M oldRecord = repository.findByName(record.getName()).get();
//                    repository.update(oldRecord, record);
//                    throw new ImportVsDatabaseRecordsConflictException();
//                }
//            }
//        }
        return 0;
    }

    @Override
    public int saveRecords(Collection<M> records, boolean skipInconsistencies) throws ImportVsDatabaseRecordsConflictException {
        try (var transaction = transactions.get()) {
            records = filterValidRecords(records, skipInconsistencies);
            records.forEach(repository::create);
            return records.size();
        }
    }

    /**
     * Delete transaction, first does all the validity checks, then tries
     * to delete the given records.
     *
     * @param records list of records we want to delete
     * @throws ImportVsDatabaseRecordsConflictException whenever a validity check fails
     */
    @Override
    public abstract void deleteRecords(Collection<M> records);

    private void throwError(M record, String location) throws InconsistentRecordException {
        throw new ImportVsDatabaseRecordsConflictException(
            repository + " of name " +
            record.getName() + " already exists in " +
            location + ", but with different properties!"
        );
    }
    public boolean validateRecordsBatch(Collection<M> records) {
        return Validator.containsNonEqualNameDuplicates(records);
    }
}
