package cz.muni.fi.pv168.gui.elements;

import java.awt.Container;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;

// TODO this is broken rn, fix will come later
public final class RangeTextField {

    /**
     * Needed to allow empty windows
     */
    private class NullNumberFormatter extends NumberFormatter {

        public NullNumberFormatter(NumberFormat format) {
            super(format);
            this.setValueClass(Integer.class);
            this.setAllowsInvalid(false);
            this.setCommitsOnValidEdit(true);
            this.setMinimum(0);
        }

        @Override
        public Object stringToValue(String text) throws ParseException {
            return ("".equals(text)) ? null : super.stringToValue(text);
        }
    }

    // FUCK JAVA and replaceAll
    private final static String whitespace_chars =  "["
        + "\\u0009" // CHARACTER TABULATION
        + "\\u000A" // LINE FEED (LF)
        + "\\u000B" // LINE TABULATION
        + "\\u000C" // FORM FEED (FF)
        + "\\u000D" // CARRIAGE RETURN (CR)
        + "\\u0020" // SPACE
        + "\\u0085" // NEXT LINE (NEL) 
        + "\\u00A0" // NO-BREAK SPACE
        + "\\u1680" // OGHAM SPACE MARK
        + "\\u180E" // MONGOLIAN VOWEL SEPARATOR
        + "\\u2000" // EN QUAD 
        + "\\u2001" // EM QUAD 
        + "\\u2002" // EN SPACE
        + "\\u2003" // EM SPACE
        + "\\u2004" // THREE-PER-EM SPACE
        + "\\u2005" // FOUR-PER-EM SPACE
        + "\\u2006" // SIX-PER-EM SPACE
        + "\\u2007" // FIGURE SPACE
        + "\\u2008" // PUNCTUATION SPACE
        + "\\u2009" // THIN SPACE
        + "\\u200A" // HAIR SPACE
        + "\\u2028" // LINE SEPARATOR
        + "\\u2029" // PARAGRAPH SEPARATOR
        + "\\u202F" // NARROW NO-BREAK SPACE
        + "\\u205F" // MEDIUM MATHEMATICAL SPACE
        + "\\u3000" // IDEOGRAPHIC SPACE
        + "]";

    private final NullNumberFormatter formatterLower = new NullNumberFormatter(NumberFormat.getInstance());
    private final NullNumberFormatter formatterUpper = new NullNumberFormatter(NumberFormat.getInstance());
    
    private JFormattedTextField lower = new JFormattedTextField(formatterLower);
    private JFormattedTextField upper = new JFormattedTextField(formatterUpper);

    private DocumentListener lowerListener = new DocumentListener() {
        @Override
        public void removeUpdate(DocumentEvent e) {
            moveUpper();
        }
        @Override
        public void insertUpdate(DocumentEvent e) {
            moveUpper();
        }
        @Override
        public void changedUpdate(DocumentEvent e) {
            //moveUpper();
        }

        private void moveUpper() {
            Integer value = toInt(lower.getText());
            if (value != null) {
                formatterUpper.setMinimum(value);
                Integer upperValue = toInt(upper.getText());
                if (upperValue == null || upperValue <= value) {
                    String newText = value + 1 + "";
                    upper.setText(newText);
                }
            }
        }
    };

    private DocumentListener upperListener = new DocumentListener() {
        @Override
        public void removeUpdate(DocumentEvent e) {
            moveLower();
        }
        @Override
        public void insertUpdate(DocumentEvent e) {
            moveLower();
        }
        @Override
        public void changedUpdate(DocumentEvent e) {
            //moveLower();
        }

        private void moveLower() {
            Integer value = toInt(upper.getText());
            if (value != null) {
                formatterUpper.setMinimum(value);
                Integer lowerValue = toInt(upper.getText());
                if (lowerValue == null || lowerValue >= value) {
                    String newText = value - 1 + "";
                    lower.setText(newText);
                }
            }
        }
    };

    public RangeTextField() {
        this(6);
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

    private Integer toInt(String text) {
        text = text.replaceAll(whitespace_chars, "");
        if (!"".equals(text)) {
            System.out.println(text);
            return Integer.parseInt(text);
        }
        return null;
    }
}
