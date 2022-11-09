package cz.muni.fi.pv168.gui.elements.text;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.text.NumberFormatter;

/**
 * @author Jan Martinek
 */
public class DoubleFormatter extends NumberFormatter {

    private double min;
    private double max;

    public DoubleFormatter(double lower, double upper) {
        super();
        this.setValueClass(Double.class);
        NumberFormat f = DecimalFormat.getInstance(Locale.getDefault());
        f.setRoundingMode(RoundingMode.HALF_UP);
        f.setMinimumFractionDigits(2);
        f.setMaximumFractionDigits(2);
        this.setFormat(f);
        this.setOverwriteMode(true);
        this.setAllowsInvalid(true);
        this.setCommitsOnValidEdit(false);
        this.setMinimum(lower);
        this.setMaximum(upper);
    }

    public void setMinimum(double min) {
        super.setMinimum(min);
        this.min = min;
    }

    public void setMaximum(double max) {
        super.setMaximum(max);
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    @Override
    public Object stringToValue(String text) throws ParseException {
        return ("".equals(text)) ? Math.min(Math.max(min, 1.0d), max) : super.stringToValue(text);
    }
}
