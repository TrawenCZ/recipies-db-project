package cz.muni.fi.pv168.model;

import java.awt.Color;
import java.util.Objects;

/**
 * Currently in a PLACEHOLDER STATE for rendering the tables
 */
public class Category {
    
    private String name;
    private Color color;

    public Category(String name, Color color) {
        setName(name);
        setColor(color);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = Objects.requireNonNull(color);
    }

    @Override
    public String toString() {
        return name;
    }
}
