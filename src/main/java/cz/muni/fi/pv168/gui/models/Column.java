package cz.muni.fi.pv168.gui.models;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Taken from {@link https://gitlab.fi.muni.cz/pv168/seminars/employee-evidence/}.
 * Simplifies the structure of table models. Use with {@code List<Column<Class, ?>>}.
 */
class Column<E, T> {

    private final String name;
    private final Function<E, T> valueGetter;
    private final BiConsumer<E, T> valueSetter;
    private final Class<T> columnType;

    private Column(String name, Class<T> columnClass, Function<E, T> valueGetter, BiConsumer<E, T> valueSetter) {
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.columnType = Objects.requireNonNull(columnClass, "column class cannot be null");
        this.valueGetter = Objects.requireNonNull(valueGetter, "value getter cannot be null");
        this.valueSetter = valueSetter;
    }

    // see Item 1: Consider static factory methods instead of constructors
    public static <E, T> Column<E, T> editable(String name, Class<T> columnClass, Function<E, T> valueGetter,
                                               BiConsumer<E, T> valueSetter) {
        return new Column<>(name, columnClass, valueGetter,
                Objects.requireNonNull(valueSetter, "value setter cannot be null"));
    }

    // see Item 1: Consider static factory methods instead of constructors
    public static <E, T> Column<E, T> readonly(String name, Class<T> columnClass, Function<E, T> valueGetter) {
        return new Column<>(name, columnClass, valueGetter, null);
    }

    void setValue(Object value, E entity) {
        if (valueSetter == null) {
            throw new UnsupportedOperationException("Cannot set value in readonly column: '" + name + "'");
        }
        valueSetter.accept(entity, columnType.cast(value)); // see Item 33: Consider type-safe heterogeneous containers
    }

    T getValue(E entity) {
        return valueGetter.apply(entity);
    }

    String getName() {
        return name;
    }

    Class<?> getColumnType() {
        return columnType;
    }

    boolean isEditable() {
        return valueSetter != null;
    }
}
