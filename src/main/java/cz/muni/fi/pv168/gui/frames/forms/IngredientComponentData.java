package cz.muni.fi.pv168.gui.frames.forms;

import cz.muni.fi.pv168.gui.resources.Icons;

import javax.swing.*;
import java.awt.*;

public class IngredientComponentData {

    private JComboBox ingredientInput = new JComboBox<>();
    private JTextField ingredientValue = new JTextField(3);
    private JComboBox unitInput = new JComboBox<>();
    private JButton removeIngredient = new JButton(Icons.getScaledIcon((ImageIcon)Icons.DELETE_S, 16));
    
    public int index;

    public IngredientComponentData(Container container, int index) {
        container.add(ingredientInput);
        container.add(ingredientValue);
        container.add(unitInput);
        container.add(removeIngredient);
        this.index = index;
    }

    public JButton getRemoveIngredient(){
        return removeIngredient;
    }
}
