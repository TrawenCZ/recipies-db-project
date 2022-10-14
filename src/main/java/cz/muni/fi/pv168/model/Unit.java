package cz.muni.fi.pv168.model;

import java.util.Objects;

/**
 * Currently in a PLACEHOLDER STATE for rendering the tables
 */
public class Unit {
    
    private String name;
    private double value;
    private Unit baseUnit;

    public Unit(String name, double value) {
        this(name, value, null);
    }

    public Unit(String name, double value, Unit baseUnit) {
        setName(name);
        setValue(value);
        setBaseUnit(baseUnit);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public Unit getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(Unit baseUnit) {
        this.baseUnit = baseUnit; // no need for null check
    }

    @Override
    public String toString() {
        return name;
    }

}
