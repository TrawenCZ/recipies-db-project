package cz.muni.fi.pv168.gui;

import java.util.Collection;
import java.util.function.Function;

import cz.muni.fi.pv168.gui.models.AbstractModel;
import cz.muni.fi.pv168.model.Identifiable;
import cz.muni.fi.pv168.model.Nameable;

/**
 * @author Jan Martinek
 */
public class Validator {

    /**
     * Looks for an entity with a given {@code String name}
     * in a given {@code model}.
     *
     * @param model data it will look through
     * @param name  name it compares to
     * @return      true if unique
     */
    public static boolean isUnique(AbstractModel<?> model, String name) {
        if (model == null || name == null) throw new NullPointerException();
        return model.getEntity(name) == null;
    }

    /**
     * Looks for an entity with a given {@code String name}
     * in a given {@code model}.
     *
     * @param model data it will look through
     * @param item  entity it compares to
     * @return      true if unique
     */
    public static boolean isUnique(AbstractModel<?> model, Nameable item) {
        if (model == null || item == null) throw new NullPointerException();
        return model.getEntity(item.getName()) == null;
    }

    /**
     * Looks for a given {@code K item} in a given {@code model}. Comparison
     * done with usage of {@code equals()} on results from {@code valueGetter}
     * and the given {@code item}. Generalized method.
     *
     * @param <T>   Type of entity saved in model (i.e. Category, Recipe, ...)
     * @param <K>   Type of checked item (i.e. String, Unit, ...)
     * @param model data it will look through
     * @param item  item it will search for
     * @return      true if unique
     */
    public static <T extends Nameable & Identifiable, K> boolean isUnique(
        AbstractModel<T> model,
        Function<T, K> valueGetter,
        K item
    ) {
        if (model == null || valueGetter == null) throw new NullPointerException();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (valueGetter.apply(model.getEntity(i)).equals(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compares two entities: {@code T item} and its counterpart in the
     * same type of {@code model}.
     *
     * @param model data it will look into
     * @param item  item it compares
     * @return      true if found AND not equal
     */
    public static boolean duplicateNotEqual(AbstractModel<?> model, Nameable item) {
        if (model == null || item == null) throw new NullPointerException();
        var savedItem = model.getEntity(item.getName());
        return savedItem != null && !item.equals(savedItem);
    }

    /**
     * Looks through list to find non-equal items with same name as given
     * {@code T item} and its counterpart in the {@code List<T> list}.
     *
     * @param list  data it will look into
     * @param item  item it compares
     * @return      true if found AND not equal
     */
    public static <T extends Nameable> boolean duplicateNotEqual(Collection<T> list, T item) {
        if (list == null || item == null) throw new NullPointerException();
        for (var other : list) {
            if (item.getName().equals(other.getName()) && !item.equals(other)) {
                return true;
            }
        }
        return false;
    }
}
