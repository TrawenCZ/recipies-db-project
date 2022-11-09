package cz.muni.fi.pv168.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.muni.fi.pv168.gui.coloring.Colorable;
import java.awt.Color;
import java.util.Objects;
import cz.muni.fi.pv168.gui.coloring.DisplayableColor;

/**
 * @author Jan Martinek, Radim Stejskal
 */
public class Category implements Colorable, Nameable {

    public final static Category UNCATEGORIZED = new Category("", new DisplayableColor(0x000000));

    private String name;
    private DisplayableColor color;

    /**
     * Serialized constructor, accessible to mapper via reflection. Takes color as an rgb-hex
     * string instead of a standalone color object.
     *
     * @param name  non-null string, used as identifier of the category
     * @param color string parsable to hex-int, non-null
     */
    @JsonCreator
    private Category(@JsonProperty("name") String name, @JsonProperty("color") String color) {
        this(name, new DisplayableColor(Integer.parseInt(color,16)));
    }

    /**
     * Public constructor. Creates a new category with a given name and color
     *
     * @param name  non-null string, used as identifier of the category
     * @param color non-null color object
     */

    public Category(String name, DisplayableColor color) {
        setName(name);
        setColor(color);
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("color")
    public String getSerializedColor() {
        return color.getRGB() + "";
    }

    @Override
    @JsonIgnore
    public Color getColor() {
        return (this == Category.UNCATEGORIZED) ? null : color;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public void setColor(DisplayableColor color) {
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
