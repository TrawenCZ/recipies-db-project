package cz.muni.fi.pv168.gui.elements;

import java.awt.Container;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

/**
 * Custom window, hides automatically when mouse is moved out of it.
 * Use show() and hide() instead of setVisible().
 * Only height is changeable via setSize(int) -> width is automatic.
 */
public class AutoHideWindow implements MouseInputListener, MouseWheelListener {

    private final static int DEFAULT_WIDTH = 150;
    private final static int DEFAULT_HEIGHT = 200;
    private final static int MINIMUM_SIZE = 100;

    private JWindow window;

    public AutoHideWindow() {
        window = new JWindow();

        window.setAutoRequestFocus(true);
        window.getGlassPane().setVisible(true);
        window.getGlassPane().addMouseListener(this);
        window.getGlassPane().addMouseMotionListener(this);
        window.getGlassPane().addMouseWheelListener(this);

        window.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public void show(Component caller) {
        if (!window.isVisible()) {
            Point p = caller.getLocationOnScreen();
            window.setLocation(p);
            window.setVisible(true);
        }
    }

    public void hide() {
        if (window.isVisible()) {
            window.setVisible(false);
        }
    }

    public void addTo(Component c) {
        window.add(c);
    }

    public void setHeight(int height) {
        if (height < MINIMUM_SIZE) throw new IllegalArgumentException("arg2:" + height);
        window.pack();
        window.setSize(window.getSize().width + 20, height);
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

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        dispatchEvent(e);
    }

    private void testLocation(MouseEvent e) {
        Point location = e.getPoint();
        Component glass = window.getGlassPane();
        if (location.x < 0
            || location.x >= glass.getX()
            || location.y < 0
            || location.y >= glass.getY())
        {
            this.hide();
        }
    }

    /**
     * Taken from {@link https://docs.oracle.com/javase/tutorial/uiswing/components/rootpane.html}
     * 
     * @param e Event fired while moving/clicking mouse while over glass pane
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

    /**
     * Copy of previous, except for mouse wheel. Also amplifies the scrolling.
     * 
     * @param e Event fired when mouse wheel moves while over the glass pane
     */
    private void dispatchEvent(MouseWheelEvent e) {
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
                component.dispatchEvent(new MouseWheelEvent(
                    component,
                    e.getID(),
                    e.getWhen(),
                    e.getModifiersEx(),
                    componentPoint.x,
                    componentPoint.y,
                    e.getClickCount(),
                    e.isPopupTrigger(),
                    e.getScrollType(),
                    e.getScrollAmount() + 6,
                    e.getWheelRotation()
                    )
                );
            }
        }
    }
}
