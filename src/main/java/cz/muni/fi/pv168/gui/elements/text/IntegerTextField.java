package cz.muni.fi.pv168.gui.elements.text;

import javax.swing.JFormattedTextField;

/**
 * Text field that allows inputting of only INTEGER values.
 *
 * @author Jan Martinek
 */
public final class IntegerTextField extends JFormattedTextField {

    public IntegerTextField(int lowerLimit, int columns) {
        this(lowerLimit, Integer.MAX_VALUE, columns);
    }

    public IntegerTextField(int lowerLimit, int upperLimit, int columns) {
        super(new IntegerFormatter(lowerLimit, upperLimit));
        this.setColumns(columns);
        this.setToolTipText("Accepts only integers");
        this.setValue(lowerLimit);
    }

    public int parse() {
        var formatter = (IntegerFormatter) this.getFormatter();
        try {
            return (int) formatter.stringToValue(this.getText());
        } catch (Exception e) {
            return formatter.getMin();
        }
    }
}
