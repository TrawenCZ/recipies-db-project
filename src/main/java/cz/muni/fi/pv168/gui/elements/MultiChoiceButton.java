package cz.muni.fi.pv168.gui.elements;

import java.awt.event.KeyEvent;
import javax.swing.JButton;

/**
 * JButton extension that when clicked opens a window
 * with scrollable multichoice item list.
 *
 * @author Jan Martinek
 */
public class MultiChoiceButton extends JButton {
    
    public final static String NO_TOOLTIP = null;
    public final static int NO_MNEMONIC = KeyEvent.VK_UNDEFINED;

    private final MultiChoiceWindow window;

    /**
     * Tries to create a button that when clicked shows a new window anchored to it.
     * This window is a multichoice slideable checkbox.
     *
     * @param label Text that will be shown on the button
     * @param tooltip Text that will be shown when mouseovering the button
     * @param menmonic Keybind to open the window (needs to be in focus, uses 'ALT + mnemonic')
     *                 To see possible values head to {@link KeyEvent}.
     * @param choices Arguments for {@link MultiChoiceWindow}
     */
    public MultiChoiceButton(String label, String tooltip, int mnemonic, String... choices) {
        window = new MultiChoiceWindow(choices);

        this.setText(label);
        if (tooltip != NO_TOOLTIP) this.setToolTipText(tooltip);
        if (mnemonic != NO_MNEMONIC) this.setMnemonic(mnemonic);

        this.addActionListener(x -> window.show(this));
    }

    /**
     * Tries to create a button that when clicked shows a new window anchored to it.
     * This window is a multichoice slideable checkbox. This button will have no
     * tooltip and no keybind by default.
     *
     * @param label     Text that will be shown on the button
     * @param choices   Arguments for {@link MultiChoiceWindow}
     */
    public MultiChoiceButton(String label, String... choices) {
        this(label, NO_TOOLTIP, NO_MNEMONIC, choices);
    }
    
    /**
     * Gets the titles of all currently checked items.
     * {@link MultiChoiceWindow#getChecked()}.
     *
     * @return checked item titles
     */
    public String[] getChecked() {
        return window.getChecked();
    }

}
