package cz.muni.fi.pv168.data.service;

import java.util.Collection;
import java.util.List;

import cz.muni.fi.pv168.exceptions.InconsistentRecordException;

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
     * @throws InconsistentRecordException whenever a duplicite with different
     *                                     values to repository is found
     */
    public Collection<M> verifyRecords(Collection<M> records) throws InconsistentRecordException;

    /**
     * Save transaction, first does all the validity checks, then tries
     * to save the given records.
     *
     * @param records list of records we want to save
     * @throws InconsistentRecordException whenever a validity check fails
     */
    public int saveRecords(Collection<M> records) throws InconsistentRecordException;

    /**
     * Save transaction WITH TOGGLEABLE VERIFICATION.
     *
     * @param records               list of records we want to save
     * @param disableVerification   true disables verifyRecords call
     * @throws InconsistentRecordException whenever a validity check fails
     */
    public int saveRecords(Collection<M> records, boolean disableVerification) throws InconsistentRecordException;

    /**
     * Delete transaction, first does all the validity checks, then tries
     * to delete the given records.
     *
     * @param records list of records we want to delete
     * @throws InconsistentRecordException whenever a validity check fails
     */
    public abstract void deleteRecords(Collection<M> records);
}
