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
    private final Integer columnWidth;

    private Column(String name,
                   Class<T> columnClass,
                   Function<E, T> valueGetter,
                   BiConsumer<E, T> valueSetter,
                   Integer columnWidth
    ) {
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.columnType = Objects.requireNonNull(columnClass, "column class cannot be null");
        this.valueGetter = Objects.requireNonNull(valueGetter, "value getter cannot be null");
        this.valueSetter = valueSetter;
        this.columnWidth = columnWidth; // may be null (not set)
    }

    public static <E, T> Column<E, T> editable(String name,
                                               Class<T> columnClass,
                                               Function<E, T> valueGetter,
                                               BiConsumer<E, T> valueSetter,
                                               Integer width
    ) {
        return new Column<>(
            name,
            columnClass,
            valueGetter,
            Objects.requireNonNull(valueSetter, "value setter cannot be null"),
            width
        );
    }

    public static <E, T> Column<E, T> readonly(String name,
                                               Class<T> columnClass,
                                               Function<E, T> valueGetter,
                                               Integer width
    ) {
        return new Column<>(name, columnClass, valueGetter, null, width);
    }

    public void setValue(Object value, E entity) {
        if (valueSetter == null) {
            throw new UnsupportedOperationException("Cannot set value in readonly column: '" + name + "'");
        }
        valueSetter.accept(entity, columnType.cast(value));
    }

    public T getValue(E entity) {
        return valueGetter.apply(entity);
    }

    public String getName() {
        return name;
    }

    public Class<?> getColumnType() {
        return columnType;
    }

    public Integer getColumnWidth() {
        return columnWidth;
    }

    public boolean isEditable() {
        return valueSetter != null;
    }
}
