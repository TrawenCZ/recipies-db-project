package cz.muni.fi.pv168.data.service;

import cz.muni.fi.pv168.exceptions.InconsistentRecordException;
import cz.muni.fi.pv168.gui.models.AbstractModel;
import cz.muni.fi.pv168.model.Nameable;
import cz.muni.fi.pv168.data.Validator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Database (TODO: not yet) manipulation service. Provides all of the
 * needed database operations.
 *
 * @author Jan Martinek, Radim Stejskal
 */
public abstract class AbstractService<T extends Nameable> {

    protected final AbstractModel<T> repository; // TODO: later will be DB connection
    protected final String repositoryName;

    protected AbstractService(AbstractModel<T> repository, String repositoryName) {
        this.repository = Objects.requireNonNull(repository);
        this.repositoryName = Objects.requireNonNull(repositoryName);
    }

    /**
     * Selects up to {@code count} records (from top to bottom).
     *
     * @param indexes indexes of rows to select
     * @return list of selected records
     */
    public List<T> selectRecords(List<Integer> indexes) {
        if (indexes == null || indexes.size() == 0) return List.of();
        var hashedIndexes = new HashSet<>(indexes);
        List<T> records = new ArrayList<>();
        for (var i : hashedIndexes) {
            var e = repository.getEntity(i);
            if (e != null) records.add(e);
        }
        return records;
    }

    /**
     * Removes duplicate records and verifies the rest agains its repository,
     * detecting inconsistencies (duplicates with different values).
     *
     * @param records items we want to check against service's repository
     * @return        list of all valid records
     * @throws InconsistentRecordException whenever a duplicite with different
     *                                     values to repository is found
     */
    public List<T> verifyRecords(List<T> records) throws InconsistentRecordException {
        List<T> validRecords = new ArrayList<>();

        // remove equal duplicates
        records = new ArrayList<>(new HashSet<>(records));

        // check records for unequal duplicates
        for (var record : records) {
            if (Validator.duplicateNotEqual(records, record)) {
                throwError(record);
            }
        }

        // check database
        for (var record : records) {
            if (Validator.duplicateNotEqual(repository, record)) {
                throwError(record);
            } else if (Validator.isUnique(repository, record)) {
                validRecords.add(record);
            }
        }

        return validRecords;
    }

    /**
     * Save transaction, first does all the validity checks, then tries
     * to save the given records.
     *
     * @param records list of records we want to save
     * @throws InconsistentRecordException whenever a validity check fails
     */
    public int saveRecords(List<T> records) throws InconsistentRecordException {
        records = new ArrayList<>(new HashSet<>(records));
        return saveRecords(records, false);
    }

    /**
     * Save transaction WITH TOGGLEABLE VERIFICATION.
     *
     * @param records list of records we want to save
     * @throws InconsistentRecordException whenever a validity check fails
     */
    public int saveRecords(List<T> records, boolean disableVerification) throws InconsistentRecordException {
        records = new ArrayList<>(new HashSet<>(records));
        if (disableVerification) {
            records.forEach(repository::addRow);
        } else {
            records = verifyRecords(records);
            records.forEach(repository::addRow);
        }
        return records.size();
    }

    /**
     * Delete transaction, first does all the validity checks, then tries
     * to delete the given records.
     *
     * @param records list of records we want to delete
     * @throws InconsistentRecordException whenever a validity check fails
     */
    public abstract void deleteRecords(List<T> records);

    private void throwError(T record) throws InconsistentRecordException {
        throw new InconsistentRecordException(
            repositoryName + " of name " +
                    record.getName() +
                    " already exists with different properties"
        );
    }
}
