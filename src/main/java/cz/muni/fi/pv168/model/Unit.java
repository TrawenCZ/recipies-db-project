package cz.muni.fi.pv168.model;

import java.util.Objects;

/**
 * Currently in a PLACEHOLDER STATE for rendering the tables
 */
public class Unit {
    
    private String name;
    private double valueInGrams;

    public Unit(String name, double valueInGrams) {
        setName(name);
        setValueInGrams(valueInGrams);
    }

    public double getValueInGrams() {
        return valueInGrams;
    }

    public void setValueInGrams(double valueInGrams) {
        this.valueInGrams = valueInGrams;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public String toString() {
        return name;
    }

}
