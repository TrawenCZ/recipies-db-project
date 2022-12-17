package cz.muni.fi.pv168.data.manipulation;

import java.io.IOException;
import java.util.List;

/**
 * @author Jan Martinek, Radim Stejskal
 */
public interface Exporter {

    Progress getProgress();

    /**
     * Always use this function before doing export, otherwise,
     * there will be no data loaded in the exporter.
     */
    public void createDataCopy(List<?> source);

    /**
     * Exports the data from saved image of data source to the filePath
     * defined destination.
     *
     * This function should take data only from copy of data source
     * for compatibility with asynchronous workers.
     *
     * @param filePath destination
     * @param indexes  if none exports all, else items on given indexes
     */
    public int exportData(String filePath, int... indexes) throws IOException;
}
