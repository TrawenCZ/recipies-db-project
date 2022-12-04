package cz.muni.fi.pv168.gui.elements;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.BevelBorder;

/**
 * A scrollable window (i.e. no title bar) with a custom multi-choice checkbox.
 * This window is usually never disposed of during the applications run, only
 * hidden at times.
 *
 * @author Jan Martinek
 */
public final class MultiChoiceWindow extends AutoHideWindow implements Filterable<List<String>> {

    private final JPanel content = new JPanel();
    private final List<ChoiceItem> choices = new ArrayList<>();

    /**
     * Support class for the window, implemented to block all events on checkboxes
     * aside from CLICKING (otherwise there is an issue with releasing/pressing).
     */
    private class ChoiceItem extends JCheckBoxMenuItem implements MouseListener {
        public ChoiceItem(String name) {
            if (name == null) throw new NullPointerException("choice name cannot be null");
            this.setText(name);
            for (var l : this.getMouseListeners()) {
                this.removeMouseListener(l);
            }
            this.addMouseListener(this);
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

        @Override
        public boolean equals(Object o) {
            if (o == null || this.getClass() != o.getClass()) return false;
            ChoiceItem other = (ChoiceItem) o;
            return this.getText().equals(other.getText());
        }

        @Override
        public int hashCode() {
            return this.getText().hashCode();
        }
    }

    private class ChoiceItemComparator implements Comparator<ChoiceItem> {
        public int compare(ChoiceItem a, ChoiceItem b) {
            return a.getText().compareTo(b.getText());
        }
    }

    /**
     * Takes >=1 strings and creates a scrollable auto-hide multi-choice checkbock
     *
     * @param strItems choices to be shown
     */
    public MultiChoiceWindow(List<String> strItems) {
        super();

        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));

        // stop resizing
        var wrapper = new JPanel();
        wrapper.add(content);

        var scrollpane = new JScrollPane(wrapper);
        scrollpane.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        scrollpane.setWheelScrollingEnabled(true);

        addTo(scrollpane);
        refresh(strItems);
    }

    /**
     * Reloads the contents of the window to match given string list.
     *
     * @param strItems names of choices
     */
    public void refresh(List<String> strItems) {
        var items = strItems.stream().map(ChoiceItem::new).collect(Collectors.toList());

        choices.stream().filter(e -> !items.contains(e)).toList().forEach(choices::remove);
        items.stream().filter(e -> !choices.contains(e)).forEach(choices::add);

        this.choices.sort(new ChoiceItemComparator());

        this.content.removeAll();
        this.choices.forEach(e -> {
            content.add(e);
            content.add(new JSeparator());
        });
        if (choices.size() > 0) content.remove(content.getComponentCount() - 1);

        setHeight(DEFAULT_HEIGHT);
    }

    @Override
    public List<String> getFilters() {
        return choices.stream()
                      .filter(ChoiceItem::getState)
                      .map(ChoiceItem::getText)
                      .toList();
    }

    @Override
    public void resetFilters() {
        choices.forEach(item -> item.setState(false));
    }
}
