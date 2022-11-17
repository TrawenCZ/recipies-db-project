package cz.muni.fi.pv168.data.manipulation.importing;

import cz.muni.fi.pv168.data.manipulation.DataManipulationException;
import cz.muni.fi.pv168.data.manipulation.JsonImporterImpl;
import cz.muni.fi.pv168.model.BaseUnitsEnum;
import cz.muni.fi.pv168.model.Unit;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static org.assertj.core.api.Assertions.*;

public class JsonUnitImporterTest {
    private static final Path PROJECT_ROOT = Paths.get(System.getProperty("project.basedir", "")).toAbsolutePath();
    private static final Path TEST_RESOURCES = PROJECT_ROOT.resolve(Path.of("src", "test", "resources"));
    private final JsonImporterImpl importer = new JsonImporterImpl();

    @Test
    void wrongFormatEmptyFile(){
        Path importFilePath = TEST_RESOURCES.resolve("empty.json");
        assertThatThrownBy(() -> importer.loadEntities(importFilePath.toString(), Unit.class))
                .isInstanceOf(DataManipulationException.class)
                .hasMessage("Unable to read file");
    }

    @Test
    void wrongFormatFileNotExists(){
        Path importFilePath = TEST_RESOURCES.resolve("notReal.json");
        assertThatThrownBy(() -> importer.loadEntities(importFilePath.toString(), Unit.class))
                .isInstanceOf(DataManipulationException.class)
                .hasMessage("File does not exist");
    }

    @Test
    void wrongDifferentFormat(){
        Path importFilePath = TEST_RESOURCES.resolve("multi-ingredients.json");
        assertThatThrownBy(() -> importer.loadEntities(importFilePath.toString(), Unit.class))
                .isInstanceOf(DataManipulationException.class)
                .hasMessage("Unable to read file");
    }
    @Test
    void singleUnit() {
        Path importFilePath = TEST_RESOURCES.resolve("single-unit.json");
        Collection<Unit> units = importer.loadEntities(importFilePath.toString(), Unit.class);

        assertThat(units).containsExactly(
                new Unit("planckPiece", 23.0, BaseUnitsEnum.PIECE)
        );
    }

    @Test
    void multipleUnits() {
        Path importFilePath = TEST_RESOURCES.resolve("multi-units.json");
        Collection<Unit> units = importer.loadEntities(importFilePath.toString(), Unit.class);

        assertThat(units).containsExactly(
                new Unit("vetsiGram", 10.0, BaseUnitsEnum.GRAM),
                new Unit("jeste vetsi gram s mezerama", 10.1, BaseUnitsEnum.GRAM),
                new Unit("jeste vetsi vetsi gram s mezerama ale neni to gram",
                        10.3,
                        BaseUnitsEnum.MILLILITER)
        );
    }
    //TODO: add like conflict/ duplicates / pipeline (export->import) probably in different class / and other tests idk
}
