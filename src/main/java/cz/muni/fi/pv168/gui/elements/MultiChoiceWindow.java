package cz.muni.fi.pv168.gui.elements;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

/**
 * A simple scrollable window (i.e. no title bar) with a custom multi-choice checkbox.
 * Window is not destroyed while the program is running, only hidden at times.
 * 
 * @author Jan Martinek
 */
public final class MultiChoiceWindow extends AutoHideWindow {

    JScrollPane scrollpane;
    JPanel panel;

    /**
     * Takes >=1 strings and creates a scrollable auto-hide multi-choice checkbock
     * 
     * @choices string arguments, interpreted as choices (no duplicity check done)
     */
    public MultiChoiceWindow(String... choices) {
        super();

        // for debug
        if (choices.length < 1) throw new NullPointerException("missing choices");

        scrollpane = new JScrollPane();
        panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        Arrays.stream(choices).forEach(c -> panel.add(new JCheckBoxMenuItem(c)));

        scrollpane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        scrollpane.setViewportView(panel);

        addToWindow(scrollpane);
    }

    /**
     * Gets the names of checked values in an string array.
     * Will propably take a second look at this later when needed.
     * 
     * @return array of names of values that were crossed
     */
    public String[] getChecked() {
       List<String> checked = new ArrayList<>();
        for (Component c : panel.getComponents()) {
            if (!(c instanceof JCheckBoxMenuItem)) continue;
            
            JCheckBoxMenuItem i = (JCheckBoxMenuItem) c;
            JLabel label = (JLabel) i.getSelectedObjects()[0];

            if (label != null) checked.add(label.getText());
        }
        return checked.toArray(new String[0]);
    }
}
