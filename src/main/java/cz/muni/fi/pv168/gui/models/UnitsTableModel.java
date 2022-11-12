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

    public UnitsTableModel() {
        this(new ArrayList<Unit>());
    }

    public UnitsTableModel(List<Unit> units) {
        super(List.of(
            Column.readonly("Name", Unit.class, self -> self, 4),
            Column.readonly("Value", Double.class, Unit::getPrettyValue, 2),
            Column.readonly("Base unit", String.class, Unit::getBaseUnitValue, 4)
        ));
        this.units = new ArrayList<>(units);
    }

    @Override
    public List<Unit> getEntities() {
        return units;
    }

    @Override
    public String toString() {
        return "Units";
    }
}
