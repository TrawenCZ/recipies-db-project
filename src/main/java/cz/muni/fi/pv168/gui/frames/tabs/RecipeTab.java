package cz.muni.fi.pv168.gui.frames.tabs;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;

import cz.muni.fi.pv168.data.generators.RecipeDataGenerator;
import cz.muni.fi.pv168.gui.action.ExportAction;
import cz.muni.fi.pv168.gui.action.ImportAction;
import cz.muni.fi.pv168.gui.elements.MultiChoiceButton;
import cz.muni.fi.pv168.gui.elements.PopupMenu;
import cz.muni.fi.pv168.gui.elements.text.RangeTextField;
import cz.muni.fi.pv168.gui.filters.SorterRecipe;
import cz.muni.fi.pv168.gui.frames.MainWindow;
import cz.muni.fi.pv168.gui.frames.forms.RecipeDetails;
import cz.muni.fi.pv168.gui.frames.forms.RecipeForm;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.model.Nameable;
import cz.muni.fi.pv168.model.Recipe;

public final class RecipeTab extends AbstractTab {

    private final static int ICON_SIZE = 40;

    private MultiChoiceButton categoryFilter;
    private MultiChoiceButton ingredientsFilter;

    private JLabel timeLabel;
    private JLabel portionsLabel;
    private RangeTextField timeField;
    private RangeTextField portionsField;
    private GridBagConstraints c;

    public RecipeTab() {
        super(MainWindow.getRecipeModel(), ICON_SIZE);

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

    @Override
    public void addSampleData(int sampleSize) {
        var recipeGenerator = new RecipeDataGenerator();
        var model = MainWindow.getRecipeModel();
        recipeGenerator.createTestData(sampleSize).stream().forEach(model::addRow);
    }

    @Override
    protected ImportAction<?> createImportAction() {
        return new ImportAction<>(
            MainWindow.getDependencies().getRecipeImporter(),
            Recipe.class,
            () -> {
                MainWindow.getDependencies().getCategoryRepository().refresh();
                MainWindow.getDependencies().getUnitRepository().refresh();
                MainWindow.getDependencies().getIngredientRepository().refresh();
                MainWindow.getDependencies().getRecipeRepository().refresh();
                MainWindow.getCategoryModel().fireTableDataChanged();
                MainWindow.getUnitsModel().fireTableDataChanged();
                MainWindow.getIngredientModel().fireTableDataChanged();
                MainWindow.getRecipeModel().fireTableDataChanged();
            }
        );
    }

    @Override
    protected ExportAction<?> createExportAction() {
        return new ExportAction<>(table, MainWindow.getRecipeModel());
    }

    @Override
    protected void initialize() {
        c = new GridBagConstraints();

        timeLabel     = new JLabel("Preparation (min)");
        portionsLabel = new JLabel("Portions");
        timeField     = new RangeTextField();
        portionsField = new RangeTextField();

        ingredientsFilter = new MultiChoiceButton( // TODO: dynamic
            "Choose ingredients",
            "Show recipes that contain all of the selected ingredients",
            MultiChoiceButton.NO_MNEMONIC,
            MainWindow.getIngredientModel().getRepository().findAll().stream()
                                                                     .map(Nameable::getName)
                                                                     .toArray(String[]::new)
        );
        categoryFilter = new MultiChoiceButton( // TODO: dynamic
            "Choose categories",
            "Show recipes of any selected category",
            MultiChoiceButton.NO_MNEMONIC,
            MainWindow.getCategoryModel().getRepository().findAll().stream()
                                                                   .map(Nameable::getName)
                                                                   .toArray(String[]::new)
        );
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

        c.weightx = 0.3;
        c.gridwidth = 2;
        c.gridheight = 2;
        addComponent(panel, searchButton, 6, 0);
        addComponent(panel, resetButton, 8, 0);

        return panel;
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
        new RecipeForm(MainWindow.getRecipeModel().getEntity(table.convertRowIndexToModel(table.getSelectedRow())));
    }

    private void viewDetails(ActionEvent actionEvent) {
        showDetailsForm(table.convertRowIndexToModel(table.getSelectedRow()));
    }

    private void showDetailsForm(int row) {
        if (row < 0 || row >= table.getRowSorter().getModelRowCount()) return;
        new RecipeDetails(MainWindow.getRecipeModel().getEntity(row));
    }

    @Override
    protected SorterRecipe createSorter() {
        return new SorterRecipe(table,
                                MainWindow.getRecipeModel(),
                                searchBar,
                                categoryFilter,
                                timeField,
                                portionsField,
                                ingredientsFilter);
    }
}
