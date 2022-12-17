package cz.muni.fi.pv168.gui.frames.tabs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;

import org.tinylog.Logger;

import cz.muni.fi.pv168.gui.Validator;
import cz.muni.fi.pv168.gui.action.*;
import cz.muni.fi.pv168.gui.coloring.ColoredTable;
import cz.muni.fi.pv168.gui.elements.PopupMenu;
import cz.muni.fi.pv168.gui.elements.SearchBar;
import cz.muni.fi.pv168.gui.elements.Toolbar;
import cz.muni.fi.pv168.gui.filters.Sorter;
import cz.muni.fi.pv168.gui.models.AbstractModel;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.model.Identifiable;
import cz.muni.fi.pv168.model.Nameable;

import static cz.muni.fi.pv168.gui.resources.Messages.DELETING_ERR_TITLE;

public abstract class AbstractTab extends JPanel {

    protected final static Color BACKGROUND_COLOR = new Color(0xBDD2E5);
    protected final static int ICON_SIZE = 35;
    protected final static int SEARCH_BAR_SIZE = 30;

    protected final ColoredTable table;
    protected final Toolbar tools;
    protected final PopupMenu popupMenu;
    protected final JPanel filterPanel;
    protected final JButton searchButton;
    protected final JButton resetButton;
    protected final SearchBar searchBar;
    protected final JLabel entries;
    protected final AbstractModel<?> model;

    public final ImportAction<?> importAction;
    public final ExportAction<?> exportAction;

    protected AtomicInteger workingThreads = new AtomicInteger(0);

    protected AbstractTab(AbstractModel<?> model) {
        this(model, SEARCH_BAR_SIZE);
    }

    protected AbstractTab(AbstractModel<?> model, int searchBarSize) {
        this.model = Objects.requireNonNull(model, "Model cannot be null");

        initialize();

        this.table = new ColoredTable(model);
        searchBar = new SearchBar(searchBarSize);

        this.importAction = createImportAction();
        this.exportAction = createExportAction();

        var sorter = createSorter();
        searchButton = createSearchButton(searchBarSize, sorter);
        resetButton = createResetButton(searchBarSize, sorter);
        filterPanel = createFilterPanel();
        popupMenu = createPopupMenu();
        tools = createToolbar();

        searchBar.addActionListener(new FilterAction(sorter));
        searchBar.addActionListener(e -> SwingUtilities.invokeLater(this::updateEntries));

        this.table.setComponentPopupMenu(popupMenu);
        this.table.setRowSorter(sorter);
        this.table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);

        entries = new JLabel();
        updateEntries();
        setLayout();
    }

    protected abstract ImportAction<?> createImportAction();

    protected abstract ExportAction<?> createExportAction();

    protected abstract void addRow(ActionEvent event);

    protected abstract void editSelectedRow(ActionEvent actionEvent);

    /**
     * Function that disables/enables user input.
     * Input buttons are: [edit, delete, export, import]
     *
     * @param isWorker if true, disables the user input. If false,
     *                 checks if there are any worker threads and
     *                 if not then enables inputs.
     */
    public void setInput(boolean isWorker) {
        boolean inputEnabled = (isWorker ? workingThreads.incrementAndGet() : workingThreads.get()) <= 0;
        int activeRows = table.getSelectedRowCount();

        tools.getEditButton().setEnabled(inputEnabled && activeRows == 1);
        tools.getDeleteButton().setEnabled(inputEnabled && activeRows >= 1);
        tools.getExportButton().setEnabled(inputEnabled && activeRows >= 1);

        tools.getAddButton().setEnabled(inputEnabled);
        tools.getImportButton().setEnabled(inputEnabled);

        popupMenu.setEnabledItem("export", inputEnabled && activeRows >= 1);
        popupMenu.setEnabledItem("remove", inputEnabled && activeRows >= 1);
        popupMenu.setEnabledItem("edit", inputEnabled && activeRows >= 1);

        popupMenu.setEnabledItem("add", inputEnabled);
        popupMenu.setEnabledItem("import", inputEnabled);
    }

    /**
     * This function should never be called outside of callbacks
     * for importers/exporters. Decrements the worker count and unlocks
     * the inputs if its 0.
     */
    public void release() {
        if (workingThreads.decrementAndGet() < 0) Logger.error("Workers in tab < 0, this should never happen");
        setInput(false);
    }

    public void deleteRows() {
        var model = (AbstractModel<?>) table.getModel();
        Arrays.stream(table.getSelectedRows())
                // view row index must be converted to model row index
                .map(table::convertRowIndexToModel)
                .boxed()
                // We need to delete rows in descending order to not change index of rows
                // which are not deleted yet
                .sorted(Comparator.reverseOrder())
                .forEach(model::deleteRow);
        updateEntries();
    }

    protected void initialize() {
        // DO NOTHING HERE (for children that needs to create some values)
    }

    protected JPanel createFilterPanel() {
        var panel = new JPanel(new GridBagLayout());
        var c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(4, 4, 2, 2);
        c.ipady = 10;

        c.weightx = 1;
        panel.add(searchBar, c);

        searchButton.setIcon(Icons.resizeIcon(searchButton.getIcon(), ICON_SIZE));
        resetButton.setIcon(Icons.resizeIcon(resetButton.getIcon(), ICON_SIZE));

        searchButton.setBorderPainted(true);
        resetButton.setBorderPainted(true);

        searchButton.setPreferredSize(new Dimension(ICON_SIZE + 10, ICON_SIZE));
        searchButton.setMinimumSize(searchButton.getPreferredSize());
        searchButton.setMaximumSize(searchButton.getPreferredSize());

        resetButton.setPreferredSize(new Dimension(ICON_SIZE + 10, ICON_SIZE));
        resetButton.setMinimumSize(resetButton.getPreferredSize());
        resetButton.setMaximumSize(resetButton.getPreferredSize());

        c.weightx = 0;
        panel.add(searchButton, c);
        panel.add(resetButton, c);

        c.weightx = 2;
        panel.add(Box.createHorizontalStrut(200), c);

        return panel;
    }

    protected PopupMenu createPopupMenu() {
        return createPopupMenu(new PopupMenu());
    }

    protected PopupMenu createPopupMenu(PopupMenu popup) {
        popup.addItem(new JMenuItem("Add", Icons.ADD_S), this::addRow, "Create a new row", "ctrl A", 'a');
        popup.addItem(new JMenuItem("Edit", Icons.EDIT_S), this::editSelectedRow, "Edit the currently selected row", "ctrl E", 'e');
        popup.addItem(new JMenuItem("Remove", Icons.DELETE_S), this::deleteSelectedRows, "Delete selected rows", "ctrl R", 'r');
        popup.addItem(new JMenuItem("Import", Icons.IMPORT_S), this::importEntities, "Import records", "ctrl I", 'i');
        popup.addItem(new JMenuItem("Export", Icons.EXPORT_S), this::exportEntities, "Export selected records", "ctrl X", 'x');
        popup.setEnabledItem("edit", false);
        popup.setEnabledItem("remove", false);
        popup.setEnabledItem("export", false);
        return popup;
    }

    protected Toolbar createToolbar() {
        var tools = new Toolbar(this::addRow, this::editSelectedRow, this::deleteSelectedRows, this::importEntities, this::exportEntities);
        tools.setFloatable(false);
        tools.setBorderPainted(false);
        return tools;
    }

    protected void deleteSelectedRows(ActionEvent actionEvent) {
        if (showConfirmDialog() != JOptionPane.YES_OPTION) return;
        deleteRows();
    }

    protected void importEntities(ActionEvent event) {
        importAction.actionPerformed(event);
        updateEntries();
    }

    protected void exportEntities(ActionEvent event) {
        exportAction.actionPerformed(event);
    }

    protected void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        setInput(false);
    }

    protected int showConfirmDialog() {
        int rowCount = table.getSelectedRowCount();
        return JOptionPane.showConfirmDialog(this,
                "Delete " + rowCount + " record" + (rowCount > 1 ? "s" : "") + "?",
                "Delete", JOptionPane.YES_NO_CANCEL_OPTION);
    }

    protected void showErrorDialog(String message, String title) {
        JOptionPane.showMessageDialog(
                this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    protected <K extends Nameable & Identifiable, V> boolean deleteSafeSearch(AbstractModel<K> model, Function<K, String> nameGetter) {
        for (int selectedRow : table.getSelectedRows()) {
            String selectedName = table.getAbstractModel().getEntity(selectedRow).getName();
            if (!Validator.isUnique(model, nameGetter, selectedName)) {
                showErrorDialog("Item of name '" + selectedName + "' is in use '" + model + " table'!  You have to delete it first!", DELETING_ERR_TITLE);
                return false;
            }
        }
        return true;
    }

    protected void updateEntries() {
        entries.setText("Shown entries " + table.getRowSorter().getViewRowCount() +
                        "/" + table.getRowSorter().getModelRowCount() + "  ");
    }

    protected Sorter createSorter() {
        return new Sorter(table, table.getAbstractModel(), searchBar);
    }

    private void setLayout() {
        this.setLayout(new BorderLayout());
        var topPanel = new JPanel(new BorderLayout());
        var bottomPanel = new JPanel(new BorderLayout());
        var centerPanel = new JScrollPane(table);

        topPanel.add(filterPanel, BorderLayout.CENTER);
        topPanel.add(tools, BorderLayout.EAST);
        bottomPanel.add(entries, BorderLayout.EAST);

        topPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

        this.add(topPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createSearchButton(int iconSize, Sorter sorter) {
        var button = new JButton(Icons.SEARCH_S);
        button.addActionListener(new FilterAction(sorter));
        button.addActionListener(e -> SwingUtilities.invokeLater(this::updateEntries));
        button.setToolTipText("Filters the viewed content");
        button.setBorderPainted(false);
        return button;
    }

    private JButton createResetButton(int iconSize, Sorter sorter) {
        var button = new JButton(Icons.RESET_S);
        button.addActionListener(new FilterResetAction(sorter));
        button.addActionListener(e -> SwingUtilities.invokeLater(this::updateEntries));
        button.setToolTipText("Resets all filters");
        button.setBorderPainted(false);
        return button;
    }

    @Override
    public String toString() {
        return model.toString();
    }
}
