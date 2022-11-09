package cz.muni.fi.pv168.gui.models;

import cz.muni.fi.pv168.model.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Model of units data class in a tabular representation
 *
 * @author Jan Martinek, Radim Stejskal
 */
public class UnitsTableModel extends AbstractModel<Unit> {

    private final List<Unit> units;

    private final List<Column<Unit, ?>> columns = List.of(
        Column.readonly("Name", Unit.class, self -> self),
        Column.readonly("Value", String.class, Unit::getPrettyValue),
        Column.readonly("Base unit", String.class, Unit::getBaseUnitValue)
    );

    public UnitsTableModel() {
        this(new ArrayList<Unit>());
    }

    public UnitsTableModel(List<Unit> units) {
        this.units = new ArrayList<>(units);
    }

    @Override
    public List<Unit> getEntities() {
        return units;
    }

    @Override
    public List<Column<Unit, ?>> getColumns() {
        return columns;
    }

    @Override
    public String toString() {
        return "Units";
    }
}
