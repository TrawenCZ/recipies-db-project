package cz.muni.fi.pv168.gui.elements.text;

import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.text.NumberFormatter;

/**
 * @author Jan Martinek
 */
public class IntegerFormatter extends NumberFormatter {

    private int min;

    public IntegerFormatter(int lower, int upper) {
        super(NumberFormat.getIntegerInstance());
        this.setValueClass(Integer.class);
        this.setOverwriteMode(true);
        this.setAllowsInvalid(true);
        this.setCommitsOnValidEdit(false);
        this.setMinimum(lower);
        this.setMaximum(upper);
    }

    public void setMinimum(int min) {
        super.setMinimum(min);
        this.min = min;
    }

    public int getMin() {
        return min;
    }

    @Override
    public Object stringToValue(String text) throws ParseException {
        return ("".equals(text)) ? min : super.stringToValue(text);
    }
}
