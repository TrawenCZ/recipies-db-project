package cz.muni.fi.pv168.gui.elements;

import java.awt.Container;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;

public final class RangeTextField {

    /**
     * Needed to allow empty windows
     */
    private class ZeroNumberFormatter extends NumberFormatter {

        public ZeroNumberFormatter(NumberFormat format) {
            super(format);
            this.setValueClass(Integer.class);
            this.setOverwriteMode(true);
            this.setAllowsInvalid(false);
            this.setCommitsOnValidEdit(false);
            this.setMinimum(0);
            this.setMaximum(999);
        }

        @Override
        public Object stringToValue(String text) throws ParseException {
            return ("".equals(text)) ? 0 : super.stringToValue(text);
        }
    }

    private final ZeroNumberFormatter formatter = new ZeroNumberFormatter(NumberFormat.getInstance());
    
    private JFormattedTextField lower = new JFormattedTextField(formatter);
    private JFormattedTextField upper = new JFormattedTextField(formatter);

    private DocumentListener lowerListener = new DocumentListener() {
        @Override
        public void removeUpdate(DocumentEvent e) {
            // NOT NEEDED
        }
        @Override
        public void insertUpdate(DocumentEvent e) {
            moveUpper(ParseToInt(lower.getText()));
        }
        @Override
        public void changedUpdate(DocumentEvent e) {
            // NOT NEEDED
        }
        private void moveUpper(int value) {
            int upperValue = ParseToInt(upper.getText());
    
            if (upperValue <= value) {
                upper.getDocument().removeDocumentListener(upperListener);
                upper.setText(Math.min(value + 1, 999) + "");
                upper.getDocument().addDocumentListener(upperListener);
            }
        }    
    };

    private DocumentListener upperListener = new DocumentListener() {
        @Override
        public void removeUpdate(DocumentEvent e) {
            // NOT NEEDED
        }
        @Override
        public void insertUpdate(DocumentEvent e) {
            moveLower(ParseToInt(upper.getText()));
        }
        @Override
        public void changedUpdate(DocumentEvent e) {
            // NOT NEEDED
        }
        private void moveLower(int value) {
            int lowerValue = ParseToInt(lower.getText());
            if (lowerValue > value) {
                lower.getDocument().removeDocumentListener(lowerListener);
                lower.setText(Math.max(value - 1, 0) + "");
                lower.getDocument().addDocumentListener(lowerListener);
            }
        }
    };

    public RangeTextField() {
        this(6);
        lower.setText(0 + "");
        upper.setText(999 + "");
    }

    public RangeTextField(int columns) {
        lower.setColumns(columns);
        upper.setColumns(columns);
        lower.getDocument().addDocumentListener(lowerListener);
        upper.getDocument().addDocumentListener(upperListener);
    }

    
    public void addLeft(Container parent, Object o) {
        parent.add(lower, o);
    }

    public void addRight(Container parent, Object o) {
        parent.add(upper, o);
    }

    public void addTo(Container parent) {
        parent.add(lower);
        parent.add(upper);
    }

    private int ParseToInt(String text) {
        try {
            return (int) formatter.stringToValue(text);
        } catch (Exception e) {
            return 0;
        }
    }
}
