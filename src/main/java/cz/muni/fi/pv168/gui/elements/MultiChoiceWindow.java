package cz.muni.fi.pv168.gui.elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.border.EtchedBorder;

/**
 * A scrollable window (i.e. no title bar) with a custom multi-choice checkbox.
 * This window is usually never disposed of during the applications run, only
 * hidden at times.
 *
 * @author Jan Martinek
 */
public final class MultiChoiceWindow extends AutoHideWindow {

    private final JScrollPane scrollpane;
    private final List<ChoiceItem> choices;

    /**
     * Support class for the window, implemented to block all events on checkboxes
     * aside from CLICKING (otherwise there is an issue with releasing/pressing).
     */
    private class ChoiceItem extends JCheckBoxMenuItem implements MouseListener {
        public ChoiceItem(String name) {
            if (name == null) throw new NullPointerException("choice name cannot be null");
            this.setText(name);
            this.setMargin(new Insets(4, 0, 4, 0));
            for (var l : this.getMouseListeners()) {
                this.removeMouseListener(l);
            }
            this.addMouseListener(this);
            choices.add(this);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            this.setState(!this.getState());
        }
    
        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    
        @Override
        public void mousePressed(MouseEvent e) {}
    
        @Override
        public void mouseReleased(MouseEvent e) {}
    }

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
        var panel = new JPanel();
        this.choices = new ArrayList<>();

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        Arrays.stream(choices).forEach(c -> {
            panel.add(new ChoiceItem(c));
            panel.add(new JSeparator());
        });
        scrollpane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        scrollpane.setViewportView(panel);
        scrollpane.setWheelScrollingEnabled(true);

        addTo(scrollpane);
        setHeight(DEFAULT_HEIGHT);
    }

    /**
     * Gets the titles of all currently checked items.
     *
     * @return checked item titles
     */
    public String[] getChecked() {
        return choices.stream().filter(c -> c.getState() == true).map(ChoiceItem::getText).toArray(String[]::new);
    }
}
