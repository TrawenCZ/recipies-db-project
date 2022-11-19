package cz.muni.fi.pv168.data.service;

import java.util.Collection;
import java.util.List;

import cz.muni.fi.pv168.exceptions.ImportVsDatabaseRecordsConflictException;
import cz.muni.fi.pv168.exceptions.InconsistentRecordException;
import cz.muni.fi.pv168.model.Nameable;

/**
 * Represents a service for any model
 *
 * @param <M> the type of the model
 * @author Jan Martinek
 */
public interface Service<M> {

    /**
     * Selects up to {@code count} records (from top to bottom).
     *
     * @param indexes indexes of rows to select
     * @return list of selected records
     */
    public List<M> selectRecords(Collection<Integer> indexes);

    /**
     * Removes duplicate records and verifies it with its repository,
     * detecting inconsistencies (same name, different values).
     *
     * @param records items we want to check against service's repository
     * @throws ImportVsDatabaseRecordsConflictException whenever a duplicite with different
     *                                     values to repository is found
     */
    @Deprecated
    public Collection<M> verifyRecords(Collection<M> records) throws InconsistentRecordException;


    /**
     * Removes duplicate records and verifies it with its repository,
     * detecting inconsistencies (same name, different values).
     *
     * @param records items we want to check against service's repository
     * @param skipInconsistent if true, the method skips inconsistent records rather than throwing an exception
     * @throws ImportVsDatabaseRecordsConflictException if skipInconsistent is false and there is non-equal-duplicate between records and the database
     */
    public Collection<M> filterValidRecords(Collection<M> records, boolean skipInconsistent);

    /**
     * Save transaction, first does all the validity checks, then tries
     * to save the given records.
     *
     * @param records list of records we want to save
     * @throws ImportVsDatabaseRecordsConflictException whenever a validity check fails
     */
    public int saveRecords(Collection<M> records) throws ImportVsDatabaseRecordsConflictException;

    /**
     * Save transaction WITH TOGGLEABLE VERIFICATION.
     *
     * @param records               list of records we want to save
     * @param skipInconsistencies   gets propagated to filterValidRecords
     * @throws ImportVsDatabaseRecordsConflictException whenever a validity check fails
     */
    public int saveRecords(Collection<M> records, boolean skipInconsistencies) throws ImportVsDatabaseRecordsConflictException;

    /**
     * Delete transaction, first does all the validity checks, then tries
     * to delete the given records.
     *
     * @param records list of records we want to delete
     * @throws ImportVsDatabaseRecordsConflictException whenever a validity check fails
     */
    public abstract void deleteRecords(Collection<M> records);

    int saveOrUpdateRecords(Collection<M> records);
}
