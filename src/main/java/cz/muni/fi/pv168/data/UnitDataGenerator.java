package cz.muni.fi.pv168.data;

import cz.muni.fi.pv168.model.Unit;

import java.util.Collections;
import java.util.List;

public class UnitDataGenerator extends AbstractDataGenerator<Unit> {

    private static final Unit gram = new Unit("g", 1d);
    private static final Unit liter = new Unit("g", 1d);

    private static final List<Unit> UNITS = List.of(
        gram,
        new Unit("dg", 10d, gram),
        new Unit("kg", 1000d, gram),
        liter,
        new Unit("kubík", 1000d, liter),
        new Unit("lžíčka", 1d),
        new Unit("hrst", 1d),
        new Unit("hrnek", 1d)
    );

    public Unit createTestEntity() {
        return selectRandom(UNITS);
    }

    public static List<Unit> getAll() {
        return Collections.unmodifiableList(UNITS);
    }

}
