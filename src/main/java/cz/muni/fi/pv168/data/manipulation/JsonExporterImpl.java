package cz.muni.fi.pv168.data.manipulation;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Jan Martinek
 */
public class JsonExporterImpl implements JsonExporter {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> void saveEntities(String filePath, List<T> values) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), values);
    }
}
