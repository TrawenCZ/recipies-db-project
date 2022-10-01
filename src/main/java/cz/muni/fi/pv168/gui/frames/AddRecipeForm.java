package cz.muni.fi.pv168.gui.frames;

import javax.swing.*;
import java.awt.*;

public class AddRecipeForm {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;
    private static final String TITLE = "Add Recipe";
    private JLabel portionLabel = new JLabel("Portions");
    private JTextField portionInput = new JTextField(12);
    private JLabel nameLabel = new JLabel("Name");
    private JTextField nameInput = new JTextField(12);
    private JLabel descriptionLabel = new JLabel("Description");
    private JScrollPane descriptionInput = new JScrollPane(new JTextArea(10,20));
    private JLabel instructionsLabel = new JLabel("Instructions");
    private JScrollPane instructionsInput = new JScrollPane(new JTextArea(10,20));
    private JLabel durationLabel = new JLabel("Duration");
    private JTextField durationInput = new JTextField(8);
    private JLabel minutesLabel = new JLabel("min");
    private JLabel categoryLabel = new JLabel("Category");
    private JComboBox<String> categoriesInput = new JComboBox<String>();

    private JButton saveButton = new JButton("Save");
    private JButton cancelButton = new JButton("Cancel");
    private JFrame frame;
    private JPanel newPanel;
    private GridBagConstraints constraints;


    public AddRecipeForm() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle(TITLE);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setSize(WIDTH, HEIGHT);
        addFormComponents();
        frame.pack();
        frame.setVisible(true);
    }

    private void addComponent(JComponent label, int gridx, int gridy) {
        addComponent(label, gridx, gridy, GridBagConstraints.CENTER);
    }
    private void addComponent(JComponent label, int gridx, int gridy, int position) {
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.anchor = position;
        newPanel.add(label, constraints);
    }
    private void addFormComponents() {
        //frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));
        newPanel = new JPanel(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 10, 10);

        addComponent(portionLabel, 0, 0);
        addComponent(portionInput, 1, 0);
        addComponent(nameLabel, 0, 1);
        addComponent(nameInput, 1, 1);
        addComponent(descriptionLabel, 0, 2);
        addComponent(descriptionInput, 1, 2);
        addComponent(instructionsLabel, 0, 3);
        addComponent(instructionsInput, 1, 3);
        addComponent(durationLabel, 0, 4);
        addComponent(durationInput, 1, 4);
        addComponent(minutesLabel, 1, 4, GridBagConstraints.EAST);
        addComponent(categoryLabel, 0, 5);
        addComponent(categoriesInput, 1, 5);
        addComponent(saveButton, 0, 6, GridBagConstraints.WEST);
        addComponent(cancelButton, 1, 6, GridBagConstraints.EAST);

        saveButton.addActionListener(e -> frame.dispose());
        cancelButton.addActionListener(e -> frame.dispose());
        newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "New Recipe"));
        frame.add(newPanel);
    }
}
