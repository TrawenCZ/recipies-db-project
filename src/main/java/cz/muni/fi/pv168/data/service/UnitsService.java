package cz.muni.fi.pv168.data.service;

import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.model.Unit;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author Radim Stejskal. Jan Martinek
 */
public class UnitsService extends AbstractService<Unit> {

    public UnitsService(Repository<Unit> unitRepository, Supplier<TransactionHandler> transactions) {
        super(unitRepository, transactions);
    }

    @Override
    public void deleteRecords(Collection<Unit> records) {

    }
}
