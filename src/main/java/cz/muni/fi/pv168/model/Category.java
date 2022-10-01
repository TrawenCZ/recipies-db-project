package cz.muni.fi.pv168.model;

import java.util.Objects;

/**
 * Currently in a PLACEHOLDER STATE for rendering the tables
 */
public class Category {
    
    private String name;

    public Category(String name) {
        setName(name);
    }

    public String getCategory() {
        return name;
    }

    public void setName(String name) {
        Objects.requireNonNull(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
