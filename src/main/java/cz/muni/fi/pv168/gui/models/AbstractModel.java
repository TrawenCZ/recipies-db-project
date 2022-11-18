package cz.muni.fi.pv168.gui.models;

import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.gui.coloring.Colorable;
import cz.muni.fi.pv168.model.Nameable;

import java.awt.Color;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Generalised abstract table model to make creating other table
 * models easier.
 * <p>
 * When implementing {@code TableModel} you need to provide 2 methods:
 * <pre>
 * protected List<T> getEntities();
 * protected List<Column<T, ?>> getColumns();
 * </pre>
 *
 * @author Jan Martinek
 */
public abstract class AbstractModel<T extends Nameable> extends AbstractTableModel {

    protected static final int UNDEFINED = -1;
    private Integer colorIndex = UNDEFINED;

    protected final List<Column<T, ?>> columns;

    protected AbstractModel(List<Column<T, ?>> columns) {
        this.columns = columns;
    }

    public Integer getColumnWidth(int columnIndex) {
        return columns.get(columnIndex).getColumnWidth();
    }

    public Integer getTotalColumnWidth() {
        return columns.stream()
                      .map(c -> (c.getColumnWidth() != null) ? c.getColumnWidth() : 0)
                      .reduce(0, Integer::sum);
    }

    @Override
    public int getRowCount() {
        return getRepository().getSize();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        T entity = getEntity(rowIndex);
        return columns.get(columnIndex).getValue(entity);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns.get(columnIndex).getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columns.get(columnIndex).getColumnType();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columns.get(columnIndex).isEditable();
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        columns.get(columnIndex).setValue(value, getEntity(rowIndex));
    }

    /**
     * Adds a new row to the table and fires the insert event.
     *
     * @param entity row we want to add
     */
    public void addRow(T entity) {
        var repository = getRepository();
        int newRowIndex = repository.getSize();
        repository.create(entity);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    /**
     * Fires the update event on the position of updated row.
     *
     * @param entity row we want to update
     */
    public void updateRow(int rowIndex, T entity) {
        getRepository().update(entity);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    /**
     * Fires the update event on the position of updated row.
     * Slower than {@link AbstractModel#updateRow(int, T)}.
     *
     * @param entity row we want to update
     */
    public void updateRow(T entity) {
        var rowIndex = getRepository().findAll().indexOf(entity);
        getRepository().update(entity);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    /**
     * Deletes the row from table and fires the delete event.
     *
     * @param rowIndex index of row we want to delete
     */
    public void deleteRow(int rowIndex) {
        getRepository().deleteByIndex(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    /**
     * Returns a single row that corresponds to the given index.
     *
     * @param rowIndex  of row we want
     * @return          single row entity OR null (if out of bounds)
     */
    public T getEntity(int rowIndex) {
        return getRepository().findByIndex(rowIndex).orElse(null);
    }

    /**
     * Returns a single row that corresponds to a given name.
     *
     * @param rowIndex  of row we want
     * @return          single row entity OR null (if out of bounds)
     */
    public T getEntity(String name) {
        return getRepository().findAll().stream()
            .dropWhile(e -> !e.getName().equals(name))
            .findFirst()
            .orElse(null);
    }

    /**
     * Returns the color of the given row.
     *
     * @param rowIndex number from 0 to rowCount
     * @return         Color or null
     */
    public Color getColor(int rowIndex) {
        if (colorIndex == UNDEFINED) colorIndex = getColorIndex();
        if (colorIndex <= UNDEFINED) return null;
        Colorable item = (Colorable) getValueAt(rowIndex, colorIndex);
        return (item != null) ? item.getColor() : null;
    }

    /**
     * Returns the repository of the given model.
     *
     * @return model's repository of type T
     */
    public abstract Repository<T> getRepository();

    /**
     * If it gets the position, it means there is a column that implements
     * the interface {@link Colorable}, i.e. it contains gettable color data.
     *
     * @return index or UNDEFINED-1
     */
    protected int getColorIndex() {
        for (int i = 0; i < getColumnCount(); i++) {
            if (Colorable.class.isAssignableFrom(getColumnClass(i))) {
                return i;
            }
        }
        return UNDEFINED - 1;
    }
}
