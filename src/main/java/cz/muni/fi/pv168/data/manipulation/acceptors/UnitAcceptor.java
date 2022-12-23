package cz.muni.fi.pv168.data.manipulation.acceptors;

import java.util.function.Consumer;

import cz.muni.fi.pv168.data.manipulation.Operation;
import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.model.Unit;

/**
 * One time importer that is setup for a single transaction.
 * Transaction may have multiple importer instances, but only
 * one should be run at a time.
 *
 * @author Jan Martinek
 */
public class UnitAcceptor extends ObjectAcceptor<Unit> {

    public UnitAcceptor(Repository<Unit> repository, Consumer<Operation> publish) {
        super(repository, publish);
    }

    @Override
    public void accept(Unit item) {
        if (item != null && item.getBaseUnit() == null) {
            var found = find.apply(item.getName());
            if (found.isPresent() && item.equals(found.get())) {
                item.setId(found.get().getId());
                return;
            }
        }
        super.accept(item);
    }
}
