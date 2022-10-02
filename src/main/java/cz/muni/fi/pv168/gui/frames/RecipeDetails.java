package cz.muni.fi.pv168.gui.frames;

import cz.muni.fi.pv168.gui.frames.forms.AbstractForm;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RecipeDetails extends AbstractForm {
    private JLabel nameValue;
    private final JLabel portionLabel = new JLabel("Portions");
    private JLabel portionValue;
    private final JLabel descriptionLabel = new JLabel("Description");
    private JScrollPane descriptionValue;
    private final JLabel instructionsLabel = new JLabel("Instructions");
    private JScrollPane instructionsValue;
    private final JLabel durationLabel = new JLabel("Duration");
    private JLabel durationValue;
    private final JLabel categoryLabel = new JLabel("Category");
    private JLabel categoryValue;
    private final JButton backButton = new JButton("Back");
    public RecipeDetails(ArrayList<Object> values) {
        super("Recipe details");
        Font font = categoryLabel.getFont();
        categoryLabel.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
        durationLabel.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
        portionLabel.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
        descriptionLabel.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
        instructionsLabel.setFont(font.deriveFont(font.getStyle() | Font.BOLD));

        nameValue = new JLabel(values.get(0).toString());
        categoryValue = new JLabel(values.get(1).toString());
        durationValue = new JLabel(values.get(2).toString() + " min");
        portionValue = new JLabel(values.get(3).toString());

        descriptionValue = createScrollPane(values.get(4).toString());
        instructionsValue = createScrollPane("Tady budou instrukce");

        addComponents();
    }

    private JScrollPane createScrollPane(String value) {
        JTextArea textArea = new JTextArea(10, 20);
        textArea.setText(value);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return scrollPane;
    }

    private void addComponents() {
        JPanel newPanel = new JPanel(new GridBagLayout());
        JDialog frame = getDialog();
        GridBagConstraints constraints = getConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 10, 10);


        addComponent(newPanel, categoryLabel, 0, 0);
        addComponent(newPanel, categoryValue, 1,0);
        addComponent(newPanel, durationLabel, 0, 1);
        addComponent(newPanel, durationValue, 1, 1);
        addComponent(newPanel, portionLabel, 0,2);
        addComponent(newPanel, portionValue,1, 2);
        addComponent(newPanel, descriptionLabel, 0, 3);
        addComponent(newPanel, descriptionValue, 1, 3);
        addComponent(newPanel, instructionsLabel, 0, 4);
        addComponent(newPanel, instructionsValue, 1, 4);
        addComponent(newPanel, backButton, 1, 5, GridBagConstraints.EAST);

        backButton.addActionListener(e -> frame.dispose());

        newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                nameValue.getText()));
        Font borderFont = ((javax.swing.border.TitledBorder) newPanel.getBorder()).getTitleFont();
        ((javax.swing.border.TitledBorder) newPanel.getBorder()).setTitleFont(new Font(borderFont.getName(), Font.BOLD, 22));
        frame.add(newPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
