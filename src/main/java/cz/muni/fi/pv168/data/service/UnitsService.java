package cz.muni.fi.pv168.data.service;

import cz.muni.fi.pv168.gui.models.UnitsTableModel;
import cz.muni.fi.pv168.model.Unit;

import java.util.Collection;

/**
 * @author Radim Stejskal. Jan Martinek
 */
public class UnitsService extends AbstractService<Unit> {

    public UnitsService(UnitsTableModel unitRepository) {
        super(unitRepository, "Unit");
    }

    @Override
    public void deleteRecords(Collection<Unit> records) {

    }
}
