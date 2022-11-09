package cz.muni.fi.pv168.data.manipulation;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonImporterImpl implements JsonImporter {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> List<T> loadEntities(String filePath, Class<T> contentClass) throws IOException {
        JavaType type = objectMapper.getTypeFactory().constructParametricType(List.class, contentClass);
        return objectMapper.readValue(new File(filePath), type);
    }
}
