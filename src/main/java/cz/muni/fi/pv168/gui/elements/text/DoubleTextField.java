package cz.muni.fi.pv168.gui.elements.text;

import javax.swing.JFormattedTextField;

/**
 * Text field that allows inputting of only DOUBLE values.
 *
 * @author Jan Martinek
 */
public final class DoubleTextField extends JFormattedTextField {

    public DoubleTextField(double lowerLimit, int columns) {
        this(lowerLimit, Double.MAX_VALUE, columns);
    }

    public DoubleTextField(double lowerLimit, double upperLimit, int columns) {
        super(new DoubleFormatter(lowerLimit, upperLimit));
        this.setColumns(columns);
        this.setToolTipText("Accepts only decimals");
    }


    public double parse() {
        var formatter = (DoubleFormatter) this.getFormatter();
        try {
            return (double) formatter.stringToValue(this.getText());
        } catch (Exception e) {
            return formatter.getMin();
        }
    }
}
