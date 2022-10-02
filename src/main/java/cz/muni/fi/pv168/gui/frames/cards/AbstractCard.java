package cz.muni.fi.pv168.gui.frames.cards;

import cz.muni.fi.pv168.gui.elements.PopupMenu;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.gui.elements.Toolbar;
import net.miginfocom.swing.MigLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;

public abstract class AbstractCard extends JPanel {
    
    protected final static Color BACKGROUND_COLOR = new Color(0xBDD2E5);
    protected final static int ICON_SIZE = 30;
    protected final static int SEARCH_BAR_SIZE = 30;

    protected final JTable table;
    protected final Toolbar tools;
    protected final PopupMenu popupMenu;
    protected final JPanel filterPanel;
    protected final JButton searchButton;
    protected final JTextField searchBar = new JTextField(SEARCH_BAR_SIZE);
    protected final JLabel entries = new JLabel("Shown entries XXX/XXX");

    protected AbstractCard(AbstractTableModel model) {
        this(model, SEARCH_BAR_SIZE);
    }

    protected AbstractCard(AbstractTableModel model, int searchBarSize) {
        if (model == null) throw new NullPointerException("Model cannot be null");
        
        initialize();

        this.table = new JTable(model);
        searchButton = createSearchButton(searchBarSize);
        filterPanel = createFilterPanel();
        popupMenu = createPopupMenu();
        tools = createToolbar();

        this.table.setComponentPopupMenu(popupMenu);
        this.table.setAutoCreateRowSorter(true);
        this.table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);

        setLayout();
        setColors(filterPanel, tools);

        addSampleData(100);
    }

    public abstract void addSampleData(int sampleSize);

    protected void initialize() {
        // DO NOTHING HERE (for children that need some values)
    }

    protected JPanel createFilterPanel() {
        JPanel panel = new JPanel();

        panel.setLayout(new MigLayout());
        panel.add(searchButton);
        panel.add(searchBar, "span, grow");

        return panel;
    }

    protected PopupMenu createPopupMenu() {
        return createPopupMenu(new PopupMenu());
    }

    protected PopupMenu createPopupMenu(PopupMenu popup) {
        popup.addItem(new JMenuItem("Add", Icons.ADD_S), this::addRow, "Create a new row", "ctrl A", 'a');
        popup.addItem(new JMenuItem("Edit", Icons.EDIT_S), this::editSelectedRow, "Edit the currently selected row", "ctrl E", 'e');
        popup.addItem(new JMenuItem("Remove", Icons.DELETE_S), this::deleteSelectedRows, "Delete selected rows", "ctrl R", 'r');
        popup.disableItem("edit");
        popup.disableItem("remove");
        return popup;
    }

    protected Toolbar createToolbar() {
        var tools = new Toolbar(this::addRow, this::editSelectedRow, this::deleteSelectedRows);

        tools.setFloatable(false);
        tools.setBorderPainted(false);

        return tools;
    }

    protected void addRow(ActionEvent event) {}

    protected void editSelectedRow(ActionEvent actionEvent) {}

    protected void deleteSelectedRows(ActionEvent actionEvent) {
        int rowCount = table.getSelectedRowCount();
        int input = JOptionPane.showConfirmDialog(null,
                "Delete " + rowCount + " record" + (rowCount > 1 ? "s" : "") + "?",
                "Delete", JOptionPane.YES_NO_CANCEL_OPTION);
    }

    protected void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        int activeRows = table.getSelectedRowCount();
        if (activeRows == 1) {
            tools.getEditButton().setEnabled(true);
            tools.getDeleteButton().setEnabled(true);
            popupMenu.enableItem("remove");
            popupMenu.enableItem("edit");
        } else if (activeRows > 1) {
            tools.getDeleteButton().setEnabled(true);
            tools.getEditButton().setEnabled(false);
            popupMenu.enableItem("remove");
            popupMenu.disableItem("edit");
        } else if (activeRows == 0) {
            tools.getDeleteButton().setEnabled(false);
            tools.getEditButton().setEnabled(false);
            popupMenu.disableItem("remove");
            popupMenu.disableItem("edit");
        }
    }

    private void setLayout() {
        this.setLayout(new BorderLayout());
        var topPanel = new JPanel(new BorderLayout());
        var bottomPanel = new JPanel(new BorderLayout());
        var centerPanel = new JScrollPane(table);

        setColors(topPanel, bottomPanel, centerPanel);

        topPanel.add(filterPanel, BorderLayout.CENTER);
        topPanel.add(tools, BorderLayout.EAST);
        bottomPanel.add(entries, BorderLayout.EAST);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setColors(JComponent... components) {
        for (var c : components) {
            if (c != null) {
                c.setBackground(BACKGROUND_COLOR);
                c.setOpaque(true);
            }
        }
    }

    private JButton createSearchButton(int iconSize) {
        return new JButton(Icons.getScaledIcon((ImageIcon)Icons.SEARCH_S, iconSize));
    }
}
