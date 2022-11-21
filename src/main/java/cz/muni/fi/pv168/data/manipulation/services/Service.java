package cz.muni.fi.pv168.data.manipulation.services;

import java.util.Collection;
import java.util.List;

/**
 * Represents a service for any model
 *
 * @param <M> the type of the model
 * @author Jan Martinek
 */
public interface Service<M> {

    /**
     * Returns all selected rows.
     *
     * @param indexes list of rows we want to export
     * @return number of exported rows
     */
    public List<M> getRecords(int[] indexes);

    /**
     * Save transaction, this operation is atomic. However it needs
     * to be sanitized with a try-catch if you wish to prevent
     * application errors.
     *
     * @param records list of records we want to save
     * @return int[2] array: [0] => imported, [1] => (-)replaced/(+)discarded
     */
    public int[] saveRecords(Collection<M> records);
}
