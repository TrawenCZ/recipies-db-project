package cz.muni.fi.pv168.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * @author Jan Martinek, Radim Stejskal
 */
public class Ingredient implements Nameable, Identifiable {

    private Long id;
    private String name;
    private double kcal;
    private Unit unit;

    @JsonCreator
    public Ingredient(@JsonProperty("name") String name,
                      @JsonProperty("kcal") double kcal,
                      @JsonProperty("unit") Unit unit) {
        this(null, name, kcal, unit);
    }

    @JsonIgnore
    public Ingredient(Long id,
                      String name,
                      double kcal,
                      Unit unit) {
        setId(id);
        setName(name);
        setKcal(kcal);
        setUnit(unit);
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("kcal")
    public double getKcal() {
        return kcal;
    }

    @JsonProperty("unit")
    public Unit getUnit() {
        return unit;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public void setKcal(double kcal) {
        if (Double.compare(kcal, 0.0d) < 0) throw new IllegalArgumentException();
        this.kcal = Objects.requireNonNull(kcal);
    }

    public void setUnit(Unit unit) {
        this.unit = Objects.requireNonNull(unit);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Ingredient other = (Ingredient) o;
        return Double.compare(other.kcal, kcal) == 0
            && name.equals(other.name)
            && unit.equals(other.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, kcal, unit);
    }

    @Override
    public String toString() {
        return name;
    }
}
