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
import cz.muni.fi.pv168.data.manipulation.*;
import cz.muni.fi.pv168.data.service.*;
import cz.muni.fi.pv168.gui.action.ExportAction;
import cz.muni.fi.pv168.gui.action.ImportAction;
import cz.muni.fi.pv168.gui.elements.MultiChoiceButton;
import cz.muni.fi.pv168.gui.elements.PopupMenu;
import cz.muni.fi.pv168.gui.elements.text.RangeTextField;
import cz.muni.fi.pv168.gui.filters.SorterRecipe;
import cz.muni.fi.pv168.gui.frames.TabLayout;
import cz.muni.fi.pv168.gui.frames.forms.RecipeDetails;
import cz.muni.fi.pv168.gui.frames.forms.RecipeForm;
import cz.muni.fi.pv168.gui.models.*;
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
        super(new RecipeTableModel(), ICON_SIZE);

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

        var unitsService = new UnitsService((UnitsTableModel) TabLayout.getUnitsModel());
        service = new RecipeService(
            (RecipeTableModel) table.getModel(), unitsService,
            new CategoryService((CategoryTableModel) TabLayout.getCategoriesModel()),
            new IngredientService((IngredientTableModel) TabLayout.getIngredientsModel(),
            unitsService)
        );
        importAction = new ImportAction<>(table, new JsonImporterImpl(), (RecipeService) service, Recipe.class);
        exportAction = new ExportAction<>(table, new JsonExporterImpl(), (RecipeService) service);
    }

    @Override
    public void addSampleData(int sampleSize) {
        var recipeGenerator = new RecipeDataGenerator();
        var model = (RecipeTableModel) table.getModel();
        recipeGenerator.createTestData(sampleSize).stream().forEach(model::addRow);
    }

    @Override
    protected void initialize() {
        c = new GridBagConstraints();

        timeLabel     = new JLabel("Preparation time");
        portionsLabel = new JLabel("Portions");
        timeField     = new RangeTextField();
        portionsField = new RangeTextField();

        ingredientsFilter = new MultiChoiceButton( // TODO: dynamic
            "Choose ingredients",
            "Show recipes that contain all of the selected ingredients",
            MultiChoiceButton.NO_MNEMONIC,
            TabLayout.getIngredientsModel().getEntities().stream()
                                                         .map(Nameable::getName)
                                                         .toArray(String[]::new)
        );
        categoryFilter = new MultiChoiceButton( // TODO: dynamic
            "Choose categories",
            "Show recipes of any selected category",
            MultiChoiceButton.NO_MNEMONIC,
            TabLayout.getCategoriesModel().getEntities().stream()
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
        addComponent(panel, searchBar, 4, 0);

        // filters
        c.gridwidth = 1;
        addComponent(panel, categoryFilter, 4, 1);
        addComponent(panel, ingredientsFilter, 5, 1);

        // time
        c.weightx = 0.4;
        c.gridwidth = 2;
        addComponent(panel, timeLabel, 6, 0, right_anchor);
        c.gridwidth = 1;
        addComponent(panel, timeField.lower(), 6, 1);
        addComponent(panel, timeField.upper(), 7, 1);

        // portions
        c.weightx = 0.4;
        c.gridwidth = 2;
        addComponent(panel, portionsLabel, 8, 0, right_anchor);
        c.gridwidth = 1;
        addComponent(panel, portionsField.lower(), 8, 1);
        addComponent(panel, portionsField.upper(), 9, 1);

        c.weightx = 0.3;
        c.gridwidth = 2;
        c.gridheight = 2;
        addComponent(panel, searchButton, 0, 0);
        addComponent(panel, resetButton, 2, 0);

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
        new RecipeForm((Recipe) table.getAbstractModel().getEntity(table.convertRowIndexToModel(table.getSelectedRow())));
    }

    private void viewDetails(ActionEvent actionEvent) {
        showDetailsForm(table.convertRowIndexToModel(table.getSelectedRow()));
    }

    private void showDetailsForm(int row) {
        if (row < 0 || row >= table.getRowSorter().getModelRowCount()) return;
        new RecipeDetails((Recipe) table.getAbstractModel().getEntity(row));
    }

    @Override
    protected SorterRecipe createSorter() {
        return new SorterRecipe(table,
                                getModel(),
                                searchBar,
                                categoryFilter,
                                timeField,
                                portionsField,
                                ingredientsFilter);
    }
}
