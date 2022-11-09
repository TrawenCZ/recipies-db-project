package cz.muni.fi.pv168.data.manipulation;

import java.io.IOException;
import java.util.List;

/**
 * @author Jan Martinek
 */
public interface JsonExporter {

    public <T> void saveEntities(String filePath, List<T> values) throws IOException;

}
