package cz.muni.fi.pv168.data.storage.entity;

/**
 * Represents the Recipe entity
 *
 * @param id           Primary key in the database (sequential)
 * @param name         Name of the Recipe
 * @param description  Description of the Recipe
 * @param categoryId   Category (id) used in a Recipe, can be null!
 * @param portions     Portions (as in number) of the Recipe
 * @param duration     Duration of how long it will take to finish the Recipe
 * @param instruction  Instruction of the Recipe
 */

public record RecipeEntity (
        Long id,
        String name,
        String description,
        Long categoryId, // foreign key, also can be null!
        long portions,
        long duration,
        String instruction
) {
    public RecipeEntity(String name,
                        String description,
                        Long categoryId,
                        long portions,
                        long duration,
                        String instruction
    ) {
        this(null, name, description, categoryId, portions, duration, instruction);
    }
}
