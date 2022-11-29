package cz.muni.fi.pv168.gui.elements;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Custom JScrollPane class that resizes its components only
 * in horizontal direction (no vertical) and prevents artifical
 * padding the y-axis that normaly occurs whenever the content
 * fits into the view with enough space for more components.
 *
 * @author Jan Martinek
 */
public class ScrollPane<T extends Component> extends JScrollPane {

    private final JPanel view;

    /**
     * Constructs with given parameters and adds a wrapped panel to it.
     * @see JScrollPane
     *
     * @param vsbPolicy vertical scrollbar setting
     * @param hsbPolicy horizontal scrollbar setting
     */
    public ScrollPane(int vsbPolicy, int hsbPolicy) {
        super(vsbPolicy, hsbPolicy);
        view = new JPanel(new GridLayout(0, 1));
        this.setViewportView(new JPanel().add(view).getParent());
    }

    /**
     * Calls {@link ScrollPane#ScrollPane(int, int)} vertical scrollbar
     * shown and horizontal hidden.
     */
    public ScrollPane() {
        this(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);
    }

    /**
     * Also sets the maximum and minimum size.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void setPreferredSize(Dimension dimension) {
        super.setPreferredSize(dimension);
        this.setMaximumSize(this.getPreferredSize());
        this.setMinimumSize(this.getPreferredSize());
    }

    /**
     * Use {@link ScrollPane#addTyped(T)} instead.
     */
    @Override @Deprecated
    public Component add(Component c) {
        return super.add(c);
    }

    /**
     * Matches the size of {@code T item} to viewport's and adds it.
     * WARNING: This function has a side effect (resizing of item).
     *
     * @param item  item to be added
     * @return      added item
     * @throws NullPointerException when item is null
     */
    public T addTyped(T item) {
        if (item == null) throw new NullPointerException("cannot add 'null'");

        item.setPreferredSize(new Dimension(
            Math.max(this.getPreferredSize().width, 100),
            item.getPreferredSize().height
        ));
        item.setMinimumSize(item.getPreferredSize());
        item.setMaximumSize(item.getPreferredSize());

        view.add(item);
        return item;
    }

    @Override
    public void remove(int index) {
        view.remove(index);
    }

    @Override
    public void remove(Component c) {
        view.remove(c);
    }

    /**
     * Returns {@code T item} on position of index from view.
     *
     * @param index index of returned item
     * @return      item or exception
     */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        return (T) view.getComponent(index);
    }

    /**
     * Returns all {@code T item} from view.
     *
     * @return list of all items casted to type T
     */
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return List.of((T[]) view.getComponents());
    }
}
