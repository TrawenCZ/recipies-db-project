package cz.muni.fi.pv168.model;

import java.util.Objects;

/**
 * Currently in a PLACEHOLDER STATE for rendering the tables
 */
public class Unit {
    
    private String name;
    private double value;

    public Unit(String name, double value) {
        setName(name);
        setValue(value);
    }

    public String getCategory() {
        return name;
    }

    public void setName(String name) {
        Objects.requireNonNull(name);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return name;
    }
}
