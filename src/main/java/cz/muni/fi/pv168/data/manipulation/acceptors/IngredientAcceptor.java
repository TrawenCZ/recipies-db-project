package cz.muni.fi.pv168.data.manipulation.acceptors;

import java.util.function.Consumer;

import cz.muni.fi.pv168.data.manipulation.Operation;
import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.Unit;

/**
 * One time importer that is setup for a single transaction.
 * Transaction may have multiple importer instances, but only
 * one should be run at a time.
 *
 * @author Jan Martinek
 */
public class IngredientAcceptor extends ObjectAcceptor<Ingredient> {

    private final ObjectAcceptor<Unit> unitAcceptor;

    public IngredientAcceptor(
        Repository<Ingredient> repository,
        Consumer<Operation> publish,
        Repository<Unit> unitRepository
    ) {
        super(repository, publish);
        this.unitAcceptor = new UnitAcceptor(unitRepository, publish);
    }

    @Override
    public void accept(Ingredient item) {
        unitAcceptor.accept(item.getUnit());
        super.accept(item);
    }

    @Override
    public void setReplace() {
        super.setReplace();
        unitAcceptor.setReplace();
    }
}
