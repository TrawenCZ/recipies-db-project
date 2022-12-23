package cz.muni.fi.pv168.gui;

/**
 * @author Jan Martinek
 */
public class TextValidator {

    public static boolean empty(String text) {
        return text.strip().isBlank();
    }

    public static boolean longerThanMaxLength(String text, int maxLength) {
        return text.length() > maxLength;
    }
}
