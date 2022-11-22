package cz.muni.fi.pv168.data.storage.entity;

/**
 * Represents the Ingredient entity
 *
 * @param id           Primary key in the database (sequential)
 * @param name         Name of the Ingredient
 * @param kcalPerUnit  Color used in the category UI
 * @param baseUnitId   Unit used for calculation of kcalPerUnit
 */

public record IngredientEntity (
        Long id,
        String name,
        double kcalPerUnit,
        Long baseUnitId // foreign key
) {
    /**
     *
     */
    public IngredientEntity(
            String name,
            double kcalPerUnit,
            Long baseUnitId
    ) {
        this(null, name, kcalPerUnit, baseUnitId);
    }

}
