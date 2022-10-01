package cz.muni.fi.pv168.gui.frames;

import cz.muni.fi.pv168.gui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Toolbar extends JToolBar{

    private final JButton addButton = new JButton(Icons.ADD_S);
    private final JButton editButton = new JButton(Icons.EDIT_S);
    private final JButton deleteButton = new JButton(Icons.DELETE_S);

    public Toolbar(ActionListener addRow, ActionListener editRow, ActionListener deleteRows) {
        addButton.addActionListener(addRow);
        addButton.setToolTipText("Adds a row");
        add(addButton);
        addSeparator();

        editButton.addActionListener(editRow);
        editButton.setToolTipText("Edits selected row");
        add(editButton);
        addSeparator();

        deleteButton.addActionListener(deleteRows);
        deleteButton.setToolTipText("Deletes selected row");
        add(deleteButton);
        addSeparator();
    }
}
