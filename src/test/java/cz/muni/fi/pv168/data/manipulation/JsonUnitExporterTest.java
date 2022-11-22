package cz.muni.fi.pv168.data.manipulation;

import cz.muni.fi.pv168.model.BaseUnitsEnum;
import cz.muni.fi.pv168.model.Unit;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

final class JsonUnitExporterTest extends AbstractJsonExporterTest<Unit> {
    @Test
    void oneUnit() throws IOException {
        var unit = new Unit("planckPiece", 23.0, BaseUnitsEnum.PIECE);
        testDirSave(List.of(unit));
        assertExportedContent("""
                [ {
                  "name" : "planckPiece",
                  "valueInBaseUnit" : 23.0,
                  "baseUnit" : "PIECE"
                } ]
                """);
    }
    @Test
    void multipleUnits() throws IOException {
        var units = List.of(
                new Unit("vetsiGram", 10.0, BaseUnitsEnum.GRAM),
                new Unit("jeste vetsi gram s mezerama", 10.1, BaseUnitsEnum.GRAM),
                new Unit("jeste vetsi vetsi gram s mezerama ale neni to gram",
                        10.3,
                        BaseUnitsEnum.MILLILITER)
        );
        testDirSave(units);
        assertExportedContent("""
                [ {
                  "name" : "vetsiGram",
                  "valueInBaseUnit" : 10.0,
                  "baseUnit" : "GRAM"
                }, {
                  "name" : "jeste vetsi gram s mezerama",
                  "valueInBaseUnit" : 10.1,
                  "baseUnit" : "GRAM"
                }, {
                  "name" : "jeste vetsi vetsi gram s mezerama ale neni to gram",
                  "valueInBaseUnit" : 10.3,
                  "baseUnit" : "MILLILITER"
                } ]
                """);
    }
}
