package cz.muni.fi.pv168.gui.elements.text;

import java.awt.Component;
import java.util.List;

import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import cz.muni.fi.pv168.gui.elements.Filterable;

/**
 * Provides two logically connected text fields. Those fields take
 * only numerical input, with {@code lower} field always having
 * smaller value then {@code upper} (automatically adjusted).
 *
 * @author Jan Martinek
 */
public final class RangeTextField implements Filterable<List<Integer>> {

    private final IntegerFormatter formatter = new IntegerFormatter(0, 999);
    private final JFormattedTextField lower = new JFormattedTextField(formatter);
    private final JFormattedTextField upper = new JFormattedTextField(formatter);

    /**
     * Adjusts the values in upper text field based on lower.
     */
    private DocumentListener lowerListener = new DocumentListener() {
        @Override
        public void removeUpdate(DocumentEvent e) {
            // NOT NEEDED
        }
        @Override
        public void insertUpdate(DocumentEvent e) {
            moveUpper(parse(lower.getText()));
        }
        @Override
        public void changedUpdate(DocumentEvent e) {
            // NOT NEEDED
        }
        private void moveUpper(int value) {
            int upperValue = parse(upper.getText());
            if (upperValue <= value) {
                upper.getDocument().removeDocumentListener(upperListener);
                upper.setText(Math.min(value + 1, 999) + "");
                upper.getDocument().addDocumentListener(upperListener);
            }
        }
    };

    /**
     * Adjusts the values in lower text field based on upper.
     */
    private DocumentListener upperListener = new DocumentListener() {
        @Override
        public void removeUpdate(DocumentEvent e) {
            // NOT NEEDED
        }
        @Override
        public void insertUpdate(DocumentEvent e) {
            moveLower(parse(upper.getText()));
        }
        @Override
        public void changedUpdate(DocumentEvent e) {
            // NOT NEEDED
        }
        private void moveLower(int value) {
            int lowerValue = parse(lower.getText());
            if (lowerValue > value) {
                lower.getDocument().removeDocumentListener(lowerListener);
                lower.setText(Math.max(value - 1, formatter.getMin()) + "");
                lower.getDocument().addDocumentListener(lowerListener);
            }
        }
    };

    /**
     * Constructs with default width, sets lower = 0, upper = 999.
     */
    public RangeTextField() {
        this(4);
    }

    /**
     * Constructs with given width, lower and upper empty.
     *
     * @param columns width of text fields
     */
    public RangeTextField(int columns) {
        lower.setToolTipText("Cannot go higher than the right field,\n accepts only integers up to 999");
        upper.setToolTipText("Cannot go lower than the left field,\n accepts only integers up to 999");
        lower.setValue(0);
        upper.setValue(999);
        lower.setColumns(columns);
        upper.setColumns(columns);
        lower.getDocument().addDocumentListener(lowerListener);
        upper.getDocument().addDocumentListener(upperListener);
    }

    /**
     * Gets the lower text field.
     *
     * @return JFormattedTextField
     */
    public Component lower() {
        return lower;
    }

    /**
     * Gets the upper text field.
     *
     * @return JFormattedTextField
     */
    public Component upper() {
        return upper;
    }

    @Override
    public List<Integer> getFilters() {
        return List.of(
            parse(lower.getText()),
            parse(upper.getText())
        );
    }

    @Override
    public void resetFilters() {
        lower.setText(0 + "");
        upper.setText(999 + "");
    }

    private int parse(String text) {
        try {
            return (int) formatter.stringToValue(text);
        } catch (Exception e) {
            return formatter.getMin();
        }
    }
}
