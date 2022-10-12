package cz.muni.fi.pv168.gui.models;

import cz.muni.fi.pv168.model.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Model of units data class in a tabular representation
 */
public class UnitsTableModel extends TableModel<Unit> {

    private final List<Unit> units;

    private final List<Column<Unit, ?>> columns = List.of(
        Column.readonly("Name", String.class, Unit::getName),
        Column.readonly("Value", double.class, Unit::getValue),
        Column.readonly("Base unit", Unit.class, Unit::getBaseUnit)
    );

    public UnitsTableModel() {
        this(new ArrayList<Unit>());
    }

    public UnitsTableModel(List<Unit> units) {
        this.units = new ArrayList<>(units);
    }

    @Override
    protected List<Unit> getEntities() {
        return units;
    }

    @Override
    protected List<Column<Unit, ?>> getColumns() {
        return columns;
    }
}

