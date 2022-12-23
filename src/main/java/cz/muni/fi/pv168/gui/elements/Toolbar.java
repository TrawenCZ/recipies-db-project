package cz.muni.fi.pv168.gui.elements;

import cz.muni.fi.pv168.gui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Toolbar extends JToolBar {

    private final JButton addButton = new JButton(Icons.ADD_S);
    private final JButton editButton = new JButton(Icons.EDIT_S);
    private final JButton deleteButton = new JButton(Icons.DELETE_S);
    private final JButton importButton = new JButton(Icons.IMPORT_S);
    private final JButton exportButton = new JButton(Icons.EXPORT_S);

    public Toolbar(JButton[] otherButtons, ActionListener addRow, ActionListener editRow, ActionListener deleteRows, ActionListener importEntities, ActionListener exportEntities) {
        for (var button : otherButtons) {
            add(button);
            addSeparator();
        }

        addButton.addActionListener(addRow);
        addButton.setToolTipText("Adds a row");
        add(addButton);
        addSeparator();

        editButton.addActionListener(editRow);
        editButton.setToolTipText("Edits selected row");
        add(editButton);
        editButton.setEnabled(false);
        addSeparator();

        deleteButton.addActionListener(deleteRows);
        deleteButton.setToolTipText("Deletes selected row(s)");
        add(deleteButton);
        deleteButton.setEnabled(false);
        addSeparator();


        importButton.addActionListener(importEntities);
        importButton.setToolTipText("Imports records");
        add(importButton);
        importButton.setEnabled(true);
        addSeparator();

        exportButton.addActionListener(exportEntities);
        exportButton.setToolTipText("Exports selected records");
        add(exportButton);
        exportButton.setEnabled(false);
    }

    public Toolbar(ActionListener addRow, ActionListener editRow, ActionListener deleteRows, ActionListener importEntities, ActionListener exportEntities) {
        this(new JButton[0], addRow, editRow, deleteRows, importEntities, exportEntities);
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getImportButton() {
        return importButton;
    }

    public JButton getExportButton() {
        return exportButton;
    }

    public JButton getEditButton() {
        return editButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

}
