package cz.muni.fi.pv168.gui.models;

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
public abstract class TableModel<T> extends AbstractTableModel {

    @Override
    public int getRowCount() {
        return getEntities().size();
    }

    @Override
    public int getColumnCount() {
        return getColumns().size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        T entity = getEntity(rowIndex);
        return getColumns().get(columnIndex).getValue(entity);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return getColumns().get(columnIndex).getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getColumns().get(columnIndex).getColumnType();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return getColumns().get(columnIndex).isEditable();
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        T entity = getEntity(rowIndex);
        getColumns().get(columnIndex).setValue(value, entity);
    }

    /**
     * Adds a new row to the table and fires the insert event.
     *
     * @param entity row we want to add
     */
    public void addRow(T entity) {
        int newRowIndex = getEntities().size();
        getEntities().add(entity);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    /**
     * Fires the update event on the position of updated row.
     *
     * @param entity row we want to update
     */
    public void updateRow(T entity) {
        int rowIndex = getEntities().indexOf(entity);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    /**
     * Deletes the row from table and fires the delete event.
     *
     * @param rowIndex index of row we want to delete
     */
    public void deleteRow(int rowIndex) {
        getEntities().remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    /**
     * Returns a single row that corresponds to the given index.
     *
     * @param rowIndex  of row we want
     * @return          single row entity
     */
    public T getEntity(int rowIndex) {
        return getEntities().get(rowIndex);
    }

    /**
     * Gets the list of all table entries. This list should be
     * modifiable to support adding/removing rows.
     *
     * @return all entities of the table
     */
    protected abstract List<T> getEntities();

    /**
     * Gets the list of all table columns with their methods. This
     * list does not have to be modifiable.
     *
     * @return all columns of the table
     */
    protected abstract List<Column<T, ?>> getColumns();
}
