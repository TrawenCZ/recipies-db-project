package cz.muni.fi.pv168.gui.models;

import java.util.List;

import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.model.Unit;

/**
 * Model of units data class in a tabular representation
 *
 * @author Jan Martinek, Radim Stejskal
 */
public class UnitsTableModel extends AbstractModel<Unit> {

    private final Repository<Unit> units;

    public UnitsTableModel(Repository<Unit> units) {
        super(List.of(
            Column.readonly("Name", Unit.class, self -> self, 4),
            Column.readonly("Value", Double.class, Unit::getPrettyValue, 2),
            Column.readonly("Base unit", String.class, Unit::getBaseUnitValue, 4)
        ));
        this.units = units;
    }

    @Override
    public Repository<Unit> getRepository() {
        return units;
    }

    @Override
    public String toString() {
        return "Units";
    }
}
