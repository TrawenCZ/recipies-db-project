package cz.muni.fi.pv168.gui.frames.cards;

import cz.muni.fi.pv168.data.CategoryDataGenerator;
import cz.muni.fi.pv168.gui.frames.Toolbar;
import cz.muni.fi.pv168.gui.frames.forms.AddCategoryForm;
import cz.muni.fi.pv168.gui.layouts.tables.CategoryTableLayout;
import cz.muni.fi.pv168.gui.resources.Icons;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CategoriesCard extends JPanel {

    // ------ TOP PANEL ------
    private final JTextField searchBar = new JTextField(30);

    private final JButton search = new JButton(Icons.getScaledIcon((ImageIcon)Icons.SEARCH_S, 30));

    // ---- CENTER PANEL -----
    private final JTable categoryTable;

    // --- BOTTOM PANEL -----
    private final JLabel entries = new JLabel("Shown categories XXX/XXX");

    public CategoriesCard() {
        categoryTable = createCategoryTable();
        layoutPanels(createTopPanel(), new JScrollPane(categoryTable));

        addSampleData();
    }

    private void layoutPanels(JPanel top, JScrollPane table) {
        JPanel nestedTopPanel = new JPanel(new BorderLayout());
        JPanel bottom = new JPanel(new BorderLayout());
        this.setLayout(new BorderLayout());

        Toolbar tools = new Toolbar(this::addRow, this::editSelectedRow, this::deleteSelectedRows);
        tools.setFloatable(false);
        tools.setBorderPainted(false);

        Color background = new Color(0xBDD2E5);
        nestedTopPanel.setBackground(background);
        nestedTopPanel.setOpaque(true);
        top.setBackground(background);
        tools.setBackground(background);
        table.setBackground(background);
        table.setOpaque(true);
        bottom.setBackground(background);

        bottom.add(entries, BorderLayout.EAST);
        nestedTopPanel.add(tools, BorderLayout.EAST);
        nestedTopPanel.add(top, BorderLayout.CENTER);
        this.add(nestedTopPanel, BorderLayout.NORTH);
        this.add(table, BorderLayout.CENTER);
        this.add(bottom, BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();

        topPanel.setLayout(new MigLayout());
        topPanel.add(search);
        topPanel.add(searchBar, "span, grow");

        return topPanel;
    }

    private JTable createCategoryTable() {
        var layout = new CategoryTableLayout();
        var table = new JTable(layout);
        table.setAutoCreateRowSorter(true);

        //TODO: possibly some listeners here / popups
        //table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        //table.setComponentPopupMenu(createEmployeeTablePopupMenu());
        return table;
    }

    private void deleteSelectedRows(ActionEvent actionEvent) {
    }

    private void editSelectedRow(ActionEvent actionEvent) {
    }

    private void addRow(ActionEvent actionEvent) {
    }

    private void addSampleData() {
        var model = (CategoryTableLayout) categoryTable.getModel();
        CategoryDataGenerator.getAll().stream().forEach(model::addRow);
    }
}
