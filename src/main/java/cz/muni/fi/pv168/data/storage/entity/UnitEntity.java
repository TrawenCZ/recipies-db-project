package cz.muni.fi.pv168.data.storage.entity;

/**
 * Represents the Unit entity
 *
 * @param id           Primary key in the database (sequential)
 * @param name         Name of the Unit
 * @param value        Value (in conjunction with baseUnit) used for ration of the Unit.
 * @param baseUnitId   Unit used as a base for Unit
 */

public record UnitEntity (
        Long id,
        String name,
        double value,
        long baseUnitId // foreign key
) {
    /**
     *
     */
    public UnitEntity(
            String name,
            double value,
            long baseUnitId
    ) {
        this(null, name, value, baseUnitId);
    }

}
