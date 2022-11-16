package cz.muni.fi.pv168.data.generators;

import cz.muni.fi.pv168.model.BaseUnitsEnum;
import cz.muni.fi.pv168.model.Unit;

import java.util.Collections;
import java.util.List;

public class UnitDataGenerator extends AbstractDataGenerator<Unit> {

    private static final Unit gram = new Unit(0,"g", 1d, BaseUnitsEnum.GRAM);
    private static final Unit piece = new Unit(0,"pc(s)", 1, BaseUnitsEnum.PIECE);
    private static final Unit milliliter = new Unit(0,"ml", 1, BaseUnitsEnum.MILLILITER);

    private static final List<Unit> UNITS = List.of(
        gram,
        piece,
        milliliter,
        new Unit(0,"dg", 100, BaseUnitsEnum.GRAM),
        new Unit(0,"kg", 1000, BaseUnitsEnum.GRAM),
        new Unit(0,"kubík", 1000000, BaseUnitsEnum.MILLILITER),
        new Unit(0,"lžíčka", 20, BaseUnitsEnum.MILLILITER),
        new Unit(0,"hrst", 100, BaseUnitsEnum.GRAM),
        new Unit(0,"tucet", 12, BaseUnitsEnum.PIECE),
        new Unit(0,"hrnek", 250, BaseUnitsEnum.MILLILITER)
    );

    public Unit createTestEntity() {
        return selectRandom(UNITS);
    }

    public static List<Unit> getAll() {
        return Collections.unmodifiableList(UNITS);
    }

}
