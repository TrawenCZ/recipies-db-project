package cz.muni.fi.pv168.data.manipulation;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public abstract class AbstractJsonImporterTest<T> {
    protected static final Path PROJECT_ROOT = Paths.get(System.getProperty("project.basedir", "")).toAbsolutePath();
    protected static final Path TEST_RESOURCES = PROJECT_ROOT.resolve(Path.of("src", "test", "resources"));
    protected final JsonImporterImpl importer = new JsonImporterImpl();
    private final Class<T> typeParameterClass;

    public AbstractJsonImporterTest(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }
    @Test
    void wrongFormatEmptyFile(){
        Path importFilePath = TEST_RESOURCES.resolve("empty.json");
        assertThatThrownBy(() -> importer.loadEntities(importFilePath.toString(), typeParameterClass))
                .isInstanceOf(DataManipulationException.class)
                .hasMessage("Unable to read file");
    }

    @Test
    void wrongFormatFileNotExists(){
        Path importFilePath = TEST_RESOURCES.resolve("notReal.json");
        assertThatThrownBy(() -> importer.loadEntities(importFilePath.toString(), typeParameterClass))
                .isInstanceOf(DataManipulationException.class)
                .hasMessage("File does not exist");
    }


    void wrongDifferentFormat(String fileName){
        Path importFilePath = TEST_RESOURCES.resolve(fileName);
        assertThatThrownBy(() -> importer.loadEntities(importFilePath.toString(), typeParameterClass))
                .isInstanceOf(DataManipulationException.class)
                .hasMessage("Unable to read file");
    }
}
