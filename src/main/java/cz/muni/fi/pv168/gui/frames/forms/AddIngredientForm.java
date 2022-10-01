package cz.muni.fi.pv168.gui.frames.forms;

import javax.swing.*;
import java.awt.*;

public class AddIngredientForm extends AbstractForm {
    private final JLabel nameLabel = new JLabel("Name");
    private final JTextField nameInput = new JTextField(12);
    private final JLabel energyValue = new JLabel("Energy value (kcal)");
    private final JTextField energyValueInput = new JTextField(12);
    private final JLabel grams = new JLabel("grams");
    private final JTextField gramsInput = new JTextField(12);
    private final JButton saveButton = new JButton("Save");
    private final JButton cancelButton = new JButton("Cancel");


    public AddIngredientForm() {
        super("Add ingredient");
        addFormComponents();
    }

    private void addFormComponents() {
        JPanel newPanel = new JPanel(new GridBagLayout());
        var frame = getDialog();
        GridBagConstraints constraints = getConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 10, 10);

        addComponent(newPanel, nameLabel, 0, 1);
        addComponent(newPanel, nameInput, 1, 1);
        addComponent(newPanel, energyValue, 0, 2);
        addComponent(newPanel, energyValueInput, 1, 2);
        addComponent(newPanel, grams, 0, 3);
        addComponent(newPanel, gramsInput, 1, 3);
        addComponent(newPanel, saveButton, 0, 6, GridBagConstraints.WEST);
        addComponent(newPanel, cancelButton, 1, 6, GridBagConstraints.EAST);

        saveButton.addActionListener(e -> frame.dispose());
        cancelButton.addActionListener(e -> frame.dispose());
        newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "New Ingredient"));
        frame.add(newPanel);
        frame.pack();
        frame.setModal(true);
        frame.setVisible(true);
    }
}
