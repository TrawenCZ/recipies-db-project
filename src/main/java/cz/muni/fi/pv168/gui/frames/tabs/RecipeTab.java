package cz.muni.fi.pv168.gui.frames.tabs;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;

import cz.muni.fi.pv168.gui.action.ExportAction;
import cz.muni.fi.pv168.gui.action.ImportAction;
import cz.muni.fi.pv168.gui.elements.MultiChoiceButton;
import cz.muni.fi.pv168.gui.elements.PopupMenu;
import cz.muni.fi.pv168.gui.elements.Toolbar;
import cz.muni.fi.pv168.gui.elements.text.RangeTextField;
import cz.muni.fi.pv168.gui.filters.SorterRecipe;
import cz.muni.fi.pv168.gui.frames.MainWindow;
import cz.muni.fi.pv168.gui.frames.forms.RecipeDetails;
import cz.muni.fi.pv168.gui.frames.forms.RecipeForm;
import cz.muni.fi.pv168.gui.models.RecipeTableModel;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.model.Category;
import cz.muni.fi.pv168.model.Nameable;
import cz.muni.fi.pv168.wiring.Supported;

public final class RecipeTab extends AbstractTab {

    protected final static int ICON_SIZE = 40;

    private MultiChoiceButton categoryFilter;
    private MultiChoiceButton ingredientsFilter;

    private JLabel timeLabel;
    private JLabel portionsLabel;
    private RangeTextField timeField;
    private RangeTextField portionsField;
    private GridBagConstraints c;

    public RecipeTab() {
        super(new RecipeTableModel(MainWindow.getDependencies().getRecipeRepository()));

        // hide ingredients column from user
        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(table.getColumnCount() - 1));
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    showDetailsForm(table.convertRowIndexToModel(table.rowAtPoint(mouseEvent.getPoint())));
                }
            }
        });
    }

    public RecipeTableModel getModel() {
        return (RecipeTableModel) model;
    }

    @Override
    protected Toolbar createToolbar() {
        var tools = new Toolbar(
            new JButton[]{searchButton, resetButton},
            this::addRow, this::editSelectedRow, this::deleteSelectedRows, this::importEntities, this::exportEntities
        );
        tools.setFloatable(false);
        tools.setBorderPainted(false);
        return tools;
    }

    @Override
    protected void lockInput() {
        MainWindow.getTabs().get(Supported.INGREDIENT).setInput(true);
        MainWindow.getTabs().get(Supported.UNIT).setInput(true);
        MainWindow.getTabs().get(Supported.CATEGORY).setInput(true);
        MainWindow.getTabs().get(Supported.RECIPE).setInput(true);
    }

    @Override
    protected void unlockInput() {
        MainWindow.getTabs().get(Supported.INGREDIENT).release();
        MainWindow.getTabs().get(Supported.UNIT).release();
        MainWindow.getTabs().get(Supported.CATEGORY).release();
        MainWindow.getTabs().get(Supported.RECIPE).release();
    }

    @Override
    protected void refreshTables() {
        MainWindow.getDependencies().getCategoryRepository().refresh();
        MainWindow.getCategoryModel().fireTableDataChanged();

        MainWindow.getDependencies().getUnitRepository().refresh();
        MainWindow.getUnitsModel().fireTableDataChanged();

        MainWindow.getDependencies().getIngredientRepository().refresh();
        MainWindow.getIngredientModel().fireTableDataChanged();

        getModel().getRepository().refresh();
        getModel().fireTableDataChanged();
    }

    @Override
    protected ImportAction<?> createImportAction() {
        return new ImportAction<>(
            MainWindow.getDependencies().getRecipeImporter(),
            this::lockInput,
            () -> {
                refreshTables();
                unlockInput();
            }
        );
    }

    @Override
    protected ExportAction<?> createExportAction() {
        return new ExportAction<>(table, getModel());
    }

    @Override
    protected void initialize() {
        c = new GridBagConstraints();

        timeLabel     = new JLabel("Preparation (min)");
        portionsLabel = new JLabel("Portions");
        timeField     = new RangeTextField();
        portionsField = new RangeTextField();

        ingredientsFilter = createIngredientFilter();
        categoryFilter = createCategoryFilter();
    }

    @Override
    protected JPanel createFilterPanel() {
        var panel = new JPanel();
        int right_anchor = GridBagConstraints.EAST;

        panel.setLayout(new GridBagLayout());
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(4, 4, 2, 2);
        c.ipady = 5;
        c.weightx = 1;

        // search
        c.gridwidth = 2;
        addComponent(panel, searchBar, 0, 0);

        // filters
        c.gridwidth = 1;
        addComponent(panel, categoryFilter, 0, 1);
        addComponent(panel, ingredientsFilter, 1, 1);

        // time
        c.weightx = 0.4;
        c.gridwidth = 2;
        addComponent(panel, timeLabel, 2, 0, right_anchor);
        c.gridwidth = 1;
        addComponent(panel, timeField.lower(), 2, 1);
        addComponent(panel, timeField.upper(), 3, 1);

        // portions
        c.weightx = 0.4;
        c.gridwidth = 2;
        addComponent(panel, portionsLabel, 4, 0, right_anchor);
        c.gridwidth = 1;
        addComponent(panel, portionsField.lower(), 4, 1);
        addComponent(panel, portionsField.upper(), 5, 1);

        return panel;
    }

    @Override
    protected PopupMenu createPopupMenu() {
        var popup = new PopupMenu();
        popup.addItem(new JMenuItem("Details", Icons.SEARCH_S), this::viewDetails, "Shows instructions and ingredients of selected recipe", "ctrl D", 'a');
        popup.setEnabledItem("details", false);
        return createPopupMenu(popup);
    }

    @Override
    protected void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        int activeRows = table.getSelectedRowCount();
        popupMenu.setEnabledItem("details", activeRows == 1);
        super.rowSelectionChanged(listSelectionEvent);
    }

    @Override
    protected void addRow(ActionEvent actionEvent) {
        new RecipeForm();
        updateEntries();
    }

    @Override
    protected void editSelectedRow(ActionEvent actionEvent) {
        new RecipeForm(getModel().getEntity(table.convertRowIndexToModel(table.getSelectedRow())));
    }

    @Override
    protected SorterRecipe createSorter() {
        return new SorterRecipe(table,
                                getModel(),
                                searchBar,
                                categoryFilter,
                                timeField,
                                portionsField,
                                ingredientsFilter
        );
    }

    private List<String> getCategoryNames() {
        var categories = new ArrayList<>(MainWindow.getCategoryModel().getRepository().findAll());
        categories.add(Category.UNCATEGORIZED);
        return categories.stream().map(Nameable::getName).toList();
    }

    private MultiChoiceButton createCategoryFilter() {
        var filter = new MultiChoiceButton(
            "Choose categories",
            "Show recipes of any selected category",
            MultiChoiceButton.NO_MNEMONIC,
            this.getCategoryNames()
        );

        MainWindow.getCategoryModel().addTableModelListener(e -> filter.refreshFilters(this.getCategoryNames()));

        return filter;
    }

    private MultiChoiceButton createIngredientFilter() {
        var filter = new MultiChoiceButton(
            "Choose ingredients",
            "Show recipes that contain all of the selected ingredients",
            MultiChoiceButton.NO_MNEMONIC,
            MainWindow.getIngredientModel().getRepository().findAll().stream().map(Nameable::getName).toList()
        );

        MainWindow.getIngredientModel().addTableModelListener(e -> filter.refreshFilters(
            MainWindow.getIngredientModel().getRepository().findAll().stream().map(Nameable::getName).toList()
        ));

        return filter;
    }

    private void addComponent(Container panel, Component component, int gridx, int gridy) {
        addComponent(panel, component, gridx, gridy, GridBagConstraints.WEST);
    }

    private void addComponent(Container panel, Component component, int gridx, int gridy, int position) {
        c.gridx = gridx;
        c.gridy = gridy;
        c.anchor = position;
        panel.add(component, c);
    }

    private void viewDetails(ActionEvent actionEvent) {
        showDetailsForm(table.convertRowIndexToModel(table.getSelectedRow()));
    }

    private void showDetailsForm(int row) {
        if (row < 0 || row >= table.getRowSorter().getModelRowCount()) return;
        new RecipeDetails(getModel().getEntity(row));
    }
}
