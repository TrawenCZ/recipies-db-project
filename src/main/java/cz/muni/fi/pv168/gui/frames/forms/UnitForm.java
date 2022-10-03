package cz.muni.fi.pv168.gui.frames.forms;

import javax.swing.*;
import java.awt.*;

public class UnitForm extends AbstractForm {
    
    private JLabel nameLabel = new JLabel("Name");
    private JTextField nameInput = new JTextField(12);
    private JLabel gramsLabel = new JLabel("Equivalent in grams");
    private JTextField gramsInput = new JTextField(12);
    private JButton saveButton = new JButton("Save");
    private JButton cancelButton = new JButton("Cancel");

    public UnitForm(String name, int grams) {
        super("Edit");
        var frame = addFormComponents();
        addData(name, grams);
        frame.setVisible(true);
    }

    public UnitForm() {
        super("Add");
        addFormComponents().setVisible(true);
    }


    private JDialog addFormComponents() {
        JPanel newPanel = new JPanel(new GridBagLayout());
        var frame = getDialog();
        GridBagConstraints constraints = getConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 10, 10);

        addComponent(newPanel, nameLabel, 0, 1);
        addComponent(newPanel, nameInput, 1, 1);
        addComponent(newPanel, gramsLabel, 0, 2);
        addComponent(newPanel, gramsInput, 1, 2);
        addComponent(newPanel, saveButton, 0, 6, GridBagConstraints.WEST);
        addComponent(newPanel, cancelButton, 1, 6, GridBagConstraints.EAST);

        saveButton.addActionListener(e -> frame.dispose());
        cancelButton.addActionListener(e -> frame.dispose());
        newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "New Unit"));
        frame.add(newPanel);
        frame.pack();
        return frame;
    }

    private void addData(String name, int grams) {
        if (name == null) throw new NullPointerException("unit name cannot be null");
        nameInput.setText(name);
        gramsInput.setText(grams + "");
    }
}
