package cz.muni.fi.pv168.data.manipulation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.model.Identifiable;
import cz.muni.fi.pv168.model.Nameable;
import cz.muni.fi.pv168.wiring.DependencyProvider;
import cz.muni.fi.pv168.wiring.TestDependencyProvider;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public abstract class AbstractJsonImporterTest<T extends Nameable & Identifiable> {
    protected static final Path PROJECT_ROOT = Paths.get(System.getProperty("project.basedir", "")).toAbsolutePath();
    protected static final Path TEST_RESOURCES = PROJECT_ROOT.resolve(Path.of("src", "test", "resources"));

    protected Importer importer;
    protected DatabaseManager databaseManager;
    protected DependencyProvider dependencyProvider;

    @BeforeEach
    void setUp() {
        this.databaseManager = DatabaseManager.createTestInstance(true);
        this.dependencyProvider = new TestDependencyProvider(databaseManager);
        this.importer = getImporter();
    }

    @AfterEach
    void tearDown() {
        databaseManager.destroySchema();
    }

    protected abstract Importer getImporter();

    @Test
    void wrongFormatEmptyFile(){
        Path importFilePath = TEST_RESOURCES.resolve("empty.json");
        assertThatThrownBy(() -> importer.importData(importFilePath.toString()))
                .isInstanceOf(DataManipulationException.class)
                .hasMessage("Unable to read file");
    }

    @Test
    void wrongFormatFileNotExists(){
        Path importFilePath = TEST_RESOURCES.resolve("notReal.json");
        assertThatThrownBy(() -> importer.importData(importFilePath.toString()))
                .isInstanceOf(DataManipulationException.class)
                .hasMessage("File does not exist");
    }


    void wrongDifferentFormat(String fileName){
        Path importFilePath = TEST_RESOURCES.resolve(fileName);
        assertThatThrownBy(() -> importer.importData(importFilePath.toString()))
                .isInstanceOf(DataManipulationException.class)
                .hasMessage("Unable to read file");
    }
}
