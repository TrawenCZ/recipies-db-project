package cz.muni.fi.pv168.gui.frames.forms;

import cz.muni.fi.pv168.gui.elements.PopupMenu;
import cz.muni.fi.pv168.gui.resources.Icons;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AddRecipeForm extends AbstractForm {
    private final JLabel portionLabel = new JLabel("Portions");
    private final JTextField portionInput = new JTextField(12);
    private final JLabel nameLabel = new JLabel("Name");
    private final JTextField nameInput = new JTextField(12);
    private final JLabel descriptionLabel = new JLabel("Description");
    private final JScrollPane descriptionInput = new JScrollPane(new JTextArea(6,20));
    private final JLabel instructionsLabel = new JLabel("Instructions");
    private final JScrollPane instructionsInput = new JScrollPane(new JTextArea(6,20));
    private final JLabel durationLabel = new JLabel("Duration");
    private final JTextField durationInput = new JTextField(8);
    private final JLabel minutesLabel = new JLabel("min");
    private final JLabel categoryLabel = new JLabel("Category");
    private final JComboBox<String> categoriesInput = new JComboBox<String>();
    private final JLabel ingredientsLabel = new JLabel("Ingredients");
    private JPanel ingredientPanel = new JPanel();

    private List<IngredientComponentData> ingredientsComponents = new ArrayList<>();

    //private final JComboBox<String> ingredientInput = new JComboBox<>();
    //private final JTextField ingredientValue = new JTextField(3);
    //private final JComboBox<String> unitInput = new JComboBox<>();
    //private final JButton removeIngredient = new JButton(Icons.getScaledIcon((ImageIcon)Icons.DELETE_S, 16));
    private final JButton addIngredient = new JButton(Icons.getScaledIcon((ImageIcon)Icons.ADD_S, 16));
    private final JButton saveButton = new JButton("Save");
    private final JButton cancelButton = new JButton("Cancel");
    //private final JScrollPane ingredientsPanel = new JScrollPane(new JPanel(new GridBagLayout()));



    public AddRecipeForm() {
        super("Add recipe");
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
        addComponent(newPanel, portionLabel, 0, 0);
        addComponent(newPanel, portionInput, 1, 0);
        addComponent(newPanel, descriptionLabel, 0, 2);
        addComponent(newPanel, descriptionInput, 1, 2);
        addComponent(newPanel, instructionsLabel, 0, 3);
        addComponent(newPanel, instructionsInput, 1, 3);
        addComponent(newPanel, durationLabel, 0, 4);
        addComponent(newPanel, durationInput, 1, 4);
        addComponent(newPanel, minutesLabel, 1, 4, GridBagConstraints.EAST);
        addComponent(newPanel, categoryLabel, 0, 5);
        addComponent(newPanel, categoriesInput, 1, 5);
        addComponent(newPanel, ingredientsLabel, 0, 6, GridBagConstraints.NORTHWEST);
        // DYNAMIC (NO LOGIC YET IMPLEMENTED)
        ingredientPanel.setLayout(new BoxLayout(ingredientPanel, BoxLayout.Y_AXIS));
        var scrollPane = new JScrollPane(ingredientPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setMaximumSize(new Dimension(210, 120));
        scrollPane.setPreferredSize(new Dimension(210, 120));
        scrollPane.setBorder(null);
        addComponent(newPanel, scrollPane, 1, 6, GridBagConstraints.WEST);
        addComponent(newPanel, addIngredient, 0, 6, GridBagConstraints.WEST);
        // BUTTONS
        addComponent(newPanel, saveButton, 0, 8, GridBagConstraints.WEST);
        addComponent(newPanel, cancelButton, 1, 8, GridBagConstraints.EAST);
        saveButton.addActionListener(e -> popUpDialog("Generic error!", "Error", JOptionPane.WARNING_MESSAGE));
        cancelButton.addActionListener(e -> frame.dispose());
        addIngredient.addActionListener(e -> addNewIngredient());
        newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "New Recipe"));
        frame.add(newPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private void addNewIngredient()
    {
        var panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        var components = new IngredientComponentData(panel, ingredientsComponents.size());
        components.getRemoveIngredient().addActionListener(e -> removeIngredient(components.getIndex()));
        ingredientsComponents.add(components);
        ingredientPanel.add(panel,ingredientsComponents.size() - 1); // last
        refreshContent();
    }
    private void removeIngredient(int index){
        ingredientPanel.remove(index);
        ingredientsComponents.remove(index);
        refreshContent();
    }

    private void refreshContent(){
        var content = getDialog().getContentPane();
        content.validate();
        content.repaint();
        getDialog().pack();
    }
}
