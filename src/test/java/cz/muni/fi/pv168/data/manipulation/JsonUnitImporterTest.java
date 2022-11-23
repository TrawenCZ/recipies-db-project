package cz.muni.fi.pv168.data.manipulation;

import cz.muni.fi.pv168.data.manipulation.DataManipulationException;
import cz.muni.fi.pv168.data.manipulation.JsonImporterImpl;
import cz.muni.fi.pv168.model.BaseUnitsEnum;
import cz.muni.fi.pv168.model.Unit;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Collection;

import static org.assertj.core.api.Assertions.*;

public final class JsonUnitImporterTest extends AbstractJsonImporterTest<Unit> {

    public JsonUnitImporterTest() {
        super(Unit.class);
    }

    @Test
    void wrongDifferentFormat(){
        super.wrongDifferentFormat("multi-ingredients.json");
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
