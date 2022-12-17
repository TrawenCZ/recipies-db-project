package cz.muni.fi.pv168.data.manipulation.acceptors;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import cz.muni.fi.pv168.data.manipulation.Operation;
import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.model.Identifiable;
import cz.muni.fi.pv168.model.Nameable;

/**
 * One time importer that is setup for a single transaction.
 * Transaction may have multiple importer instances, but only
 * one should be run at a time.
 *
 * @author Jan Martinek
 */
public class ObjectAcceptor<M extends Nameable & Identifiable> {

    protected final Consumer<Operation> publish;

    protected final Function<String, Optional<M>> find;
    protected final Consumer<M> create;
    protected final Consumer<M> update;

    private boolean doReplace = false;

    public ObjectAcceptor(Repository<M> repository, Consumer<Operation> publish) {
        this(repository::findByName, repository::create, repository::update, publish);
    }

    public ObjectAcceptor(
        Function<String, Optional<M>> find,
        Consumer<M> create,
        Consumer<M> update,
        Consumer<Operation> publish
    ) {
        this.find = Objects.requireNonNull(find, "find is null");
        this.create = Objects.requireNonNull(create, "create is null");
        this.update = Objects.requireNonNull(update, "update is null");
        this.publish = Objects.requireNonNull(publish, "publish is null");
    }

    public void accept(M item) {
        var found = find.apply(item.getName());

        if (found.isPresent()) {
            item.setId(found.get().getId());
        }

        // create new instance
        if (found.isEmpty()) {
            create.accept(item);
            publish.accept(Operation.INSERT);
            item.setId(find.apply(item.getName()).orElseThrow().getId());
            return;
        }

        // dont update since it's the same
        if (doReplace && !found.get().equals(item)) {
            update.accept(item);
            publish.accept(Operation.UPDATE);
            return;
        }

        publish.accept(Operation.IGNORE);
    }

    public void setReplace() {
        doReplace = true;
    }
}
