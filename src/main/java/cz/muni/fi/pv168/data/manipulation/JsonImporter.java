package cz.muni.fi.pv168.data.manipulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import cz.muni.fi.pv168.data.manipulation.importers.ObjectImporter;
import cz.muni.fi.pv168.model.Identifiable;
import cz.muni.fi.pv168.model.Nameable;

/**
 * This class is not thread safe - only one thread should be allowed to
 * access same object of this class during the whole import procedure
 * (which includes restart for decision making).
 *
 * @author Jan Martinek, Radim Stejskal
 */
public class JsonImporter<M extends Nameable & Identifiable> implements Importer {

    private final JavaType type;
    private final ObjectMapper objectMapper;
    private final ObjectImporter<M> objectImporter;

    private Progress progress = new Progress();

    public JsonImporter(Class<M> contentClass, ObjectImporter<M> objectImporter) {
        this.objectMapper = new ObjectMapper();
        this.type = this.objectMapper.getTypeFactory().constructParametricType(
            List.class,
            Objects.requireNonNull(contentClass, "contentClass cannot be null")
        );
        this.objectImporter = Objects.requireNonNull(objectImporter, "objectImporter cannot be null");
    }

    @Override
    public Progress getProgress() {
        return progress;
    }

    @Override
    public void importData(String filePath) {
        try {
            if (progress.isDone()) progress.resetSettings();
            objectImporter.doImport(objectMapper.readValue(new File(filePath), type), progress);
        } catch (FileNotFoundException ex) {
            throw new DataManipulationException("File does not exist", ex);
        } catch (IOException ex) {
            throw new DataManipulationException("Unable to read file", ex);
        }
    }
}
