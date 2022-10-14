package cz.muni.fi.pv168.model;

import java.util.Objects;

/**
 * Template for ingredients, each ingredient has a separate name and
 * an energy value in kcal/unit.
 */
public class Ingredient {
    
    private String name;
    private double kcal;
    private Unit unit;

    public Ingredient(String name, double kcal, Unit unit) {
        setName(name);
        setKcal(kcal);
        setUnit(unit);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        if (Double.compare(kcal, 0.0d) <= 0) throw new IllegalArgumentException();
        this.kcal = Objects.requireNonNull(kcal);
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = Objects.requireNonNull(unit);
    }

    @Override
    public String toString() {
        return name;
    }
}
