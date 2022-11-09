package cz.muni.fi.pv168.data.generators;

import cz.muni.fi.pv168.model.BaseUnitsEnum;
import cz.muni.fi.pv168.model.Unit;

import java.util.Collections;
import java.util.List;

public class UnitDataGenerator extends AbstractDataGenerator<Unit> {

    private static final Unit gram = new Unit("g", 1d, BaseUnitsEnum.GRAM);
    private static final Unit piece = new Unit("pc(s)", 1, BaseUnitsEnum.PIECE);
    private static final Unit milliliter = new Unit("ml", 1, BaseUnitsEnum.MILLILITER);

    private static final List<Unit> UNITS = List.of(
        gram,
        piece,
        milliliter,
        new Unit("dg", 100, BaseUnitsEnum.GRAM),
        new Unit("kg", 1000, BaseUnitsEnum.GRAM),
        new Unit("kubík", 1000000, BaseUnitsEnum.MILLILITER),
        new Unit("lžíčka", 20, BaseUnitsEnum.MILLILITER),
        new Unit("hrst", 100, BaseUnitsEnum.GRAM),
        new Unit("tucet", 12, BaseUnitsEnum.PIECE),
        new Unit("hrnek", 250, BaseUnitsEnum.MILLILITER)
    );

    public Unit createTestEntity() {
        return selectRandom(UNITS);
    }

    public static List<Unit> getAll() {
        return Collections.unmodifiableList(UNITS);
    }

}
