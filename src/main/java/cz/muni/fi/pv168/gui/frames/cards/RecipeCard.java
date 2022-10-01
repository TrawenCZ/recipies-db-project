package cz.muni.fi.pv168.gui.frames.cards;

import java.awt.*;
import javax.swing.*;
import java.util.List;

import cz.muni.fi.pv168.model.Recipe;
import cz.muni.fi.pv168.gui.elements.MultiChoiceButton;
import cz.muni.fi.pv168.gui.elements.RangeTextField;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.gui.frames.forms.AddRecipeForm;
import cz.muni.fi.pv168.gui.layouts.RecipeTableLayout;

public class RecipeCard extends JPanel {

    // ------ TOP PANEL ------
    private final JTextField searchBar = new JTextField(30);
    
    private final MultiChoiceButton categoryFilter;
    private final MultiChoiceButton ingredientsFilter;

    private final JLabel timeLabel = new JLabel("Preparation time");
    private final JLabel portionsLabel = new JLabel("Portions");

    private final RangeTextField timeField = new RangeTextField();
    private final RangeTextField portionsField = new RangeTextField();

    private final JButton search = new JButton(Icons.getScaledIcon((ImageIcon)Icons.SEARCH_ICON_S, 30));
    private final JButton addRecipe = new JButton(Icons.getScaledIcon((ImageIcon)Icons.ADD_ICON_S, 30));
    private final JButton resetFilters = new JButton(Icons.getScaledIcon((ImageIcon)Icons.RESET_ICON_S, 30));

    // ---- CENTER PANEL -----
    private final JTable recipesTable;

    // --- BOTTOM PANEL -----
    private final JLabel entries = new JLabel("Shown recipes XXX/XXX");

    public RecipeCard() {
        // TODO get all possible ingredients (maybe kept globally in some class)
        ingredientsFilter = new MultiChoiceButton(
            "Filter ingredients",
            "Show recipes that contain all of the selected ingredients",
            MultiChoiceButton.NO_MNEMONIC,
            "TODO:", "ingredient", "choicebox", "this", "is", "a sample", "not", "implemented", "yet"
        );
        // TODO get all possible categories
        categoryFilter = new MultiChoiceButton(
            "Filter categories",
            "Show recipes of any selected category",
            MultiChoiceButton.NO_MNEMONIC,
            "TODO:", "category", "choicebox","this", "is", "a sample", "not", "implemented", "yet"
        );
        addRecipe.addActionListener(e -> new AddRecipeForm());

        recipesTable = createRecipeTable();
        layoutPanels(createTopPanel(), new JScrollPane(recipesTable));
    }

    private void layoutPanels(JPanel top, JScrollPane table) {
        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());
        bottom.add(entries, BorderLayout.EAST);

        Color background = new Color(0xBDD2E5);
        this.setLayout(new BorderLayout());
        top.setBackground(background);
        table.setBackground(background);
        table.setOpaque(true);
        bottom.setBackground(background);
    
        this.add(top, BorderLayout.NORTH);
        this.add(table, BorderLayout.CENTER);
        this.add(bottom, BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        int default_anchor = GridBagConstraints.WEST;
        int right_anchor = GridBagConstraints.EAST;

        topPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = default_anchor;
        c.weightx = 1; // x is horizontal
        c.weighty = 1; // y is vertical

        // search
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        topPanel.add(searchBar, c);
        
        // buttons
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        topPanel.add(search, c);

        c.gridx = 1;
        c.gridy = 1;
        topPanel.add(resetFilters, c);

        c.gridx = 2;
        c.gridy = 1;
        topPanel.add(addRecipe, c);

        // filters
        c.weightx = 0;
        c.gridx = 3;
        c.gridy = 0;
        topPanel.add(categoryFilter,c);

        c.gridx = 3;
        c.gridy = 1;
        topPanel.add(ingredientsFilter, c);

        // time
        c.weightx = 0;
        c.anchor = right_anchor;
        c.gridx = 4;
        c.gridy = 0;
        topPanel.add(timeLabel, c);
        c.anchor = default_anchor;
        c.weightx = 0.4;
        c.gridx = 5;
        c.gridy = 0;
        timeField.addLeft(topPanel, c);
        c.gridx = 6;
        c.gridy = 0;
        timeField.addRight(topPanel, c);

        // portions
        c.weightx = 0;
        c.anchor = right_anchor;
        c.gridx = 4;
        c.gridy = 1;
        topPanel.add(portionsLabel, c);
        c.weightx = 0.4;
        c.anchor = default_anchor;
        c.gridx = 5;
        c.gridy = 1;
        portionsField.addLeft(topPanel, c);
        c.gridx = 6;
        c.gridy = 1;
        portionsField.addRight(topPanel, c);
        
        return topPanel;
    }

    private JTable createRecipeTable() {
        var layout = new RecipeTableLayout();
        var table = new JTable(layout);
        table.setAutoCreateRowSorter(true);

        //TODO: possibly some listeners here / popups
        //table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        //table.setComponentPopupMenu(createEmployeeTablePopupMenu());
        return table;
    }

}
