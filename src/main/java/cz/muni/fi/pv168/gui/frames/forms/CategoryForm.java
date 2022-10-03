package cz.muni.fi.pv168.gui.frames.forms;

import javax.swing.*;
import java.awt.*;

public class CategoryForm extends AbstractForm {
    
    private final JLabel nameLabel = new JLabel("Name");
    private final JTextField nameInput = new JTextField(12);
    private final JButton saveButton = new JButton("Save");
    private final JButton cancelButton = new JButton("Cancel");

    public CategoryForm(String name) {
        super("Edit");
        var frame = addFormComponents();
        addSampleData(name);
        frame.setVisible(true);
    }
    public CategoryForm() {
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
        addComponent(newPanel, saveButton, 0, 6, GridBagConstraints.WEST);
        addComponent(newPanel, cancelButton, 1, 6, GridBagConstraints.EAST);

        saveButton.addActionListener(e -> frame.dispose());
        cancelButton.addActionListener(e -> frame.dispose());
        newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "New Category"));
        frame.add(newPanel);
        frame.pack();
        return frame;
    }

    private void addSampleData(String name) {
        if (name == null) throw new NullPointerException("name of ingredient cannot be null");
        nameInput.setText(name);
    }
}
