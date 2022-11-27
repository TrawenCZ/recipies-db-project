package cz.muni.fi.pv168.data.storage.entity;

/**
 * Represents the Category entity
 *
 * @param id           Primary key in the database (sequential)
 * @param name         Name of the Category
 * @param color        Color used in the category UI
 */

public record CategoryEntity (
    Long id,
    String name,
    String color
) {
    public CategoryEntity(
            String name,
            String color
    ) {
        this(null, name, color);
    }
}
