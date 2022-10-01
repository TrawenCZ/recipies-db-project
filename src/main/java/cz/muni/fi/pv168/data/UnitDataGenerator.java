package cz.muni.fi.pv168.data;

import cz.muni.fi.pv168.model.Unit;

import java.util.Collections;
import java.util.List;

public class UnitDataGenerator extends AbstractDataGenerator<Unit> {
    
    private static final List<Unit> UNITS = List.of(
        new Unit("g", 1d),
        new Unit("dg", 10d),
        new Unit("kg", 1000d),
        new Unit("lžíčka", 50d),
        new Unit("hrst", 100d),
        new Unit("hrnek", 400d)
    );

    public Unit createTestEntity() {
        return selectRandom(UNITS);
    }

    public static List<Unit> getAll() {
        return Collections.unmodifiableList(UNITS);
    }

}
