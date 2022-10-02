package cz.muni.fi.pv168.gui.frames.cards;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

import cz.muni.fi.pv168.data.RecipeDataGenerator;
import cz.muni.fi.pv168.gui.elements.MultiChoiceButton;
import cz.muni.fi.pv168.gui.elements.PopupMenu;
import cz.muni.fi.pv168.gui.elements.RangeTextField;
import cz.muni.fi.pv168.gui.frames.forms.AddRecipeForm;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.gui.layouts.tables.RecipeTableLayout;

public final class RecipeCard extends AbstractCard {

    protected final static int ICON_SIZE = 16;

    private MultiChoiceButton categoryFilter;
    private MultiChoiceButton ingredientsFilter;
    private JButton resetButton;
    private JLabel timeLabel;
    private JLabel portionsLabel;
    private RangeTextField timeField;
    private RangeTextField portionsField;

    public RecipeCard() {
        super(new RecipeTableLayout(), ICON_SIZE);
    }

    @Override
    public void addSampleData(int sampleSize) {
        var recipeGenerator = new RecipeDataGenerator();
        var model = (RecipeTableLayout) table.getModel();
        recipeGenerator.createTestData(sampleSize).stream().forEach(model::addRow);
    }

    @Override
    protected void initialize() {
        timeLabel = new JLabel("Preparation time");
        portionsLabel = new JLabel("Portions");
        timeField = new RangeTextField();
        portionsField = new RangeTextField();
        resetButton = new JButton(Icons.getScaledIcon((ImageIcon)Icons.RESET_S, ICON_SIZE));

        ingredientsFilter = new MultiChoiceButton( // TODO: dynamic updates
            "Filter ingredients",
            "Show recipes that contain all of the selected ingredients",
            MultiChoiceButton.NO_MNEMONIC,
            "TODO:", "ingredient", "choicebox", "this", "is", "a sample", "not", "implemented", "yet"
        );
        categoryFilter = new MultiChoiceButton( // TODO: dynamic updates
            "Filter categories",
            "Show recipes of any selected category",
            MultiChoiceButton.NO_MNEMONIC,
            "TODO:", "category", "choicebox","this", "is", "a sample", "not", "implemented", "yet"
        );
    }

    @Override
    protected JPanel createFilterPanel() {
        initialize();

        var panel = new JPanel();
        int default_anchor = GridBagConstraints.WEST;
        int right_anchor = GridBagConstraints.EAST;

        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = default_anchor;
        c.weightx = 1; // x is horizontal
        c.weighty = 1; // y is vertical

        // search
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(searchBar, c);
        
        // buttons
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(searchButton, c);

        c.gridx = 1;
        c.gridy = 1;
        panel.add(resetButton, c);

        // filters
        c.weightx = 0;
        c.gridx = 3;
        c.gridy = 0;
        panel.add(categoryFilter,c);

        c.gridx = 3;
        c.gridy = 1;
        panel.add(ingredientsFilter, c);

        // time
        c.weightx = 0;
        c.anchor = right_anchor;
        c.gridx = 4;
        c.gridy = 0;
        panel.add(timeLabel, c);
        c.anchor = default_anchor;
        c.weightx = 0.4;
        c.gridx = 5;
        c.gridy = 0;
        timeField.addLeft(panel, c);
        c.gridx = 6;
        c.gridy = 0;
        timeField.addRight(panel, c);

        // portions
        c.weightx = 0;
        c.anchor = right_anchor;
        c.gridx = 4;
        c.gridy = 1;
        panel.add(portionsLabel, c);
        c.weightx = 0.4;
        c.anchor = default_anchor;
        c.gridx = 5;
        c.gridy = 1;
        portionsField.addLeft(panel, c);
        c.gridx = 6;
        c.gridy = 1;
        portionsField.addRight(panel, c);
        
        return panel;
    }

    @Override
    protected PopupMenu createPopupMenu() {
        var popup = new PopupMenu();
        popup.addItem(new JMenuItem("Details", Icons.SEARCH_S), this::viewDetails, "Shows instructions and ingredients of selected recipe", "ctrl D", 'a');
        return createPopupMenu(popup);
    }

    @Override
    protected void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        int activeRows = table.getSelectedRowCount();
        if (activeRows == 1) {
            popupMenu.enableItem("details");
        } else {
            popupMenu.disableItem("details");
        }
        super.rowSelectionChanged(listSelectionEvent);
    }
    
    @Override
    protected void addRow(ActionEvent actionEvent) {
        new AddRecipeForm();
    }

    private void viewDetails(ActionEvent actionEvent) {
        // TODO: add window
    }
}
