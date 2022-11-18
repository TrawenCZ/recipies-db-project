package cz.muni.fi.pv168.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.muni.fi.pv168.gui.coloring.Colorable;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * @author Jan Martinek, Radim Stejskal
 */
public class Unit implements Colorable, Nameable, Identifiable {

    private Long id;
    private String name;
    private double valueInBaseUnit;
    private BaseUnitsEnum baseUnit;

    @JsonCreator
    public Unit(@JsonProperty("name") String name,
                @JsonProperty("valueInBaseUnit") double valueInBaseUnit,
                @JsonProperty("baseUnit") BaseUnitsEnum baseUnit) {
        this(null, name, valueInBaseUnit, baseUnit);
    }

    public Unit(Long id, String name, double valueInBaseUnit, BaseUnitsEnum baseUnit) {
        this.id = id;
        this.name = name;
        this.valueInBaseUnit = valueInBaseUnit;
        this.baseUnit = baseUnit;
    }

    public Long getId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("valueInBaseUnit")
    public double getValueInBaseUnit() {
            return valueInBaseUnit;
    }

    @JsonProperty("baseUnit")
    public BaseUnitsEnum getBaseUnit() {
        return baseUnit;
    }

    @JsonIgnore
    public String getBaseUnitValue() {
        return baseUnit.getValue();
    }

    @JsonIgnore
    public Double getPrettyValue() {
        DecimalFormat format = new DecimalFormat("0.##");
        return Double.valueOf(format.format(valueInBaseUnit));
    }

    @Override @JsonIgnore
    public Color getColor() {
        // TODO: REVIEW after base units are finished, may need some changes
        return (name.equals(baseUnit.getValue())) ? Color.GRAY : null;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name){
        this.name = Objects.requireNonNull(name);
    }

    public void setValueInBaseUnit(double valueInBaseUnit) {
        this.valueInBaseUnit = valueInBaseUnit;
    }

    public void setBaseUnit(BaseUnitsEnum baseUnit) {
        this.baseUnit = baseUnit; // no need for null check
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Unit other = (Unit) o;
        return Math.abs(other.valueInBaseUnit - valueInBaseUnit) <= 0.0001f
            && name.equals(other.name)
            && baseUnit == other.baseUnit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, valueInBaseUnit, baseUnit);
    }

    @Override
    public String toString() {
        return name;
    }
}
