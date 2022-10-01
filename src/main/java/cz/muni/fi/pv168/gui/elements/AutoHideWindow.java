package cz.muni.fi.pv168.gui.elements;

import java.awt.Container;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

public class AutoHideWindow implements MouseInputListener {

    private final static int DEFAULT_WIDTH = 120;
    private final static int DEFAULT_HEIGHT = 160;
    private final static int MINIMUM_SIZE = 50;

    private int width;
    private int height;

    private JWindow window;

    public AutoHideWindow() {
        window = new JWindow();
        window.setAutoRequestFocus(true);

        window.getGlassPane().setVisible(true);
        window.getGlassPane().addMouseListener(this);
        window.getGlassPane().addMouseMotionListener(this);

        setDimensions(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public void show(Component caller) {
        if (!window.isVisible()) {
            // Point p = caller.getLocationOnScreen();
            // p.x += Math.round(caller.getSize().getWidth());
            // window.setLocation(p);
            window.setLocationRelativeTo(caller);
            window.setVisible(true);
        }
    }

    public void hide() {
        if (window.isVisible()) {
            window.setVisible(false);
        }
    }

    protected void addToWindow(Component component) {
        window.add(component);
    }

    protected void setDimensions(int width, int height) {
        if (width < MINIMUM_SIZE || height < MINIMUM_SIZE) throw new IllegalArgumentException();
        this.width = width;
        this.height = height;
        window.setSize(this.width, this.height);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        dispatchEvent(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // dispatch not required here
    }

    @Override
    public void mouseExited(MouseEvent e) {
        testLocation(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dispatchEvent(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dispatchEvent(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        dispatchEvent(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // dispatch not required here
    }

    private void testLocation(MouseEvent e) {
        Point location = e.getPoint();
        Component glass = window.getGlassPane();
        if (location.x < 0
            || location.x >= glass.getX()
            || location.y < 0
            || location.y >= glass.getY())
        {
            hide();
        }
    }

    /**
     * Taken from {@link https://docs.oracle.com/javase/tutorial/uiswing/components/rootpane.html}
     * TODO: fix a bug where checkbox is (de)activated if mouseovered while dragging the slider
     * 
     * @param e event that will be dispatched to mouseovered component
     */
    private void dispatchEvent(MouseEvent e) {
        Point glassPanePoint = e.getPoint();
        Container container = window.getContentPane();
        Point containerPoint = SwingUtilities.convertPoint(window.getGlassPane(),
                glassPanePoint, container);

        if (containerPoint.y < 0) {
            // we're not in the content pane
        } else {
            // probably over the content pane.
            // find out exactly over which component
            Component component = SwingUtilities.getDeepestComponentAt(
                container,
                containerPoint.x,
                containerPoint.y
            );

            if (component != null) {
                Point componentPoint = SwingUtilities.convertPoint(
                    window.getGlassPane(),
                    glassPanePoint,
                    component
                );
                component.dispatchEvent(new MouseEvent(
                    component,
                    e.getID(),
                    e.getWhen(),
                    e.getModifiersEx(),
                    componentPoint.x,
                    componentPoint.y,
                    e.getClickCount(),
                    e.isPopupTrigger())
                );
            }
        }
    }
}
