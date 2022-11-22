package cz.muni.fi.pv168.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.muni.fi.pv168.gui.coloring.Colorable;

import java.awt.Color;
import java.util.Objects;

/**
 * @author Jan Martinek, Radim Stejskal
 */
public class Category implements Colorable, Nameable, Identifiable {

    private Long id;
    private String name;
    private Color color;

    /**
     * Serialized constructor, accessible to mapper via reflection. Takes color as an rgb-hex
     * string instead of a standalone color object.
     *
     * @param name  non-null string, used as identifier of the category
     * @param color string parsable to hex-int, non-null
     */
    @JsonCreator
    public Category(@JsonProperty("name") String name, @JsonProperty("color") String color) {
        this(null, name, color);
    }

    /**
     * Public constructor. Creates a new category with a given name and color
     *
     * @param id    identifier
     * @param name  non-null string, used as identifier of the category
     * @param color non-null color object
     */
    @JsonIgnore
    public Category(Long id, String name, Color color) {
        setId(id);
        setName(name);
        setColor(color);
    }

    @JsonIgnore
    public Category(Long id, String name, String color) {
        this(id, name, new Color(
                Integer.valueOf(color.substring(2, 4), 16),
                Integer.valueOf(color.substring(4, 6), 16),
                Integer.valueOf(color.substring(6, 8), 16),
                Integer.valueOf(color.substring(0, 2), 16)
        ));
    }

    public Category(String name, Color color) {
        this(null, name, color);
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("color")
    public String getSerializedColor() {
        return String.format("%08X", color.getRGB());
    }

    @Override
    @JsonIgnore
    public Color getColor() {
        return color;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public void setColor(Color color) {
        this.color = Objects.requireNonNull(color);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Category other = (Category) o;
        return name.equals(other.name) && color.equals(other.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }

    @Override
    public String toString() {
        return name;
    }
}
