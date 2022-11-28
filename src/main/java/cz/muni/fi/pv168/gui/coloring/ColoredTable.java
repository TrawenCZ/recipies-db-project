package cz.muni.fi.pv168.gui.coloring;

import cz.muni.fi.pv168.Config;
import cz.muni.fi.pv168.gui.models.AbstractModel;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 * Extension of {@link JTable} that colors rows by the first
 * column of Color type it finds in the row.
 *
 * Constructors are limited to models only.
 *
 * @author Jan Martinek
 */
public class ColoredTable extends JTable {

    public static final Color DEFAULT_COLOR = new Color(255, 255, 255, 0);
    public static final Color SECONDARY_COLOR = new Color(0, 0, 0, 10);

    private final AbstractModel<?> model;

    public ColoredTable() {
        this(null, null, null);
    }

    public ColoredTable(AbstractModel<?> dm) {
        this(dm, null, null);
    }

    public ColoredTable(AbstractModel<?> dm, TableColumnModel cm) {
        this(dm, cm, null);
    }

    /**
     * Calls {@code TableModel constructor} AND looks for column of color.
     *
     * @param dm table model it will use
     * @param cm table column model it will use (usually null)
     * @param sm list selection model it will use (usually null)
     */
    public ColoredTable(AbstractModel<?> dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
        model = dm;
        setSelectionForeground(Color.BLACK);
        setAutoCreateRowSorter(false);
        setRowHeight(30);
    }

    @Override
    public void doLayout() {
        var total = model.getTotalColumnWidth();
        for (int i = 0; i < getColumnCount(); i++) {
            var width = model.getColumnWidth(i);
            if (width != null) getColumnModel().getColumn(i).setPreferredWidth(
                Math.round(getWidth() * width / total)
            );
        }
        super.doLayout();
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
        Component c = super.prepareRenderer(renderer, row, col);
        Color color = model.getColor(this.convertRowIndexToModel(row));

        if (color != null) {
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), Config.OPACITY);
        } else if (row % 2 == 1) {
            color = SECONDARY_COLOR;
        } else {
            color = DEFAULT_COLOR;
        }

        if (isRowSelected(row)) {
            color = Color.YELLOW;
        }

        c.setBackground(color);

        return c;
    }

    /**
     * You cannot override getModel if model is null.
     */
    public AbstractModel<?> getAbstractModel() {
        return model;
    }
}
