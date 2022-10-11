package cz.muni.fi.pv168.gui.elements;

import java.awt.Container;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import cz.muni.fi.pv168.gui.frames.MainWindow;

/**
 * Custom window. WARNING: tied to {@link MainWindow}. Automatically
 * hides itself whenever there's a mouse click or press outside of
 * its region OR the mouse leaves the region of MainWindow.
 * <p>
 * Available methods:
 * <p>
 * {@link AutoHideWindow#show(Component)},
 * {@link AutoHideWindow#show(Component, int, int)},
 * {@link AutoHideWindow#hide()},
 * {@link AutoHideWindow#addTo(Component)},
 * {@link AutoHideWindow#setHeight(int)}
 *
 * @author Jan Martinek
 */
public class AutoHideWindow extends MouseInputAdapter {

    protected final static int DEFAULT_WIDTH = 150;
    protected final static int DEFAULT_HEIGHT = 220;
    protected final static int MINIMUM_SIZE = 50;

    private final JWindow window;

    /**
     * Constructs the class with a custom window of default size.
     */
    public AutoHideWindow() {
        window = new JWindow();
        window.setAutoRequestFocus(true);
        window.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Calls {@link AutoHideWindow#show(Component, int, int)} with both offsets
     * set to 0. See the linked method for more description.
     *
     * @param caller anchor
     */
    public void show(Component caller) {
        show(caller, 0, 0);
    }

    /**
     * Sets the visibility of the window to true. {@code x} location will be set to
     * the middle of the caller with the given offset, {@code y} will be anchored
     * to the top of it with the given offset. Turns on automatic hiding based on
     * {@link MainWindow}.
     *
     * @param caller anchor
     * @param x horizontal offset
     * @param y vertical offset
     */
    public void show(Component caller, int x, int y) {
        if (!window.isVisible()) {
            if (MainWindow.getGlassPane() != null) {
                MainWindow.getGlassPane().setVisible(true);
                MainWindow.getGlassPane().addMouseListener(this);
                MainWindow.getGlassPane().addMouseMotionListener(this);
                MainWindow.getGlassPane().addMouseWheelListener(this);
            } else {
                throw new IllegalArgumentException("cannot get main window");
            }

            Point p = caller.getLocationOnScreen();
            window.setLocation(p);
            window.setVisible(true);
        }
    }


    /**
     * Hides the window and removes its listeners from {@link MainWindow}.
     * WARNING: side effect HIDES MainWindow's glass pane.
     */
    public void hide() {
        if (window.isVisible()) {
            if (MainWindow.getGlassPane() != null) {
                MainWindow.getGlassPane().setVisible(false);
                MainWindow.getGlassPane().removeMouseListener(this);
                MainWindow.getGlassPane().removeMouseMotionListener(this);
                MainWindow.getGlassPane().removeMouseWheelListener(this);
            } else {
                throw new IllegalArgumentException("cannot get main window");
            }

            window.setVisible(false);
        }
    }

    /**
     * Adds component to this window.
     *
     * @param c component to be added
     */
    public void addTo(Component c) {
        window.add(c);
    }

    /**
     * Sets the height of the window (throws error if minimum size is understepped)
     * and rebalances the width to better match the contents. Height will be set
     * UP TO given {@code height} (if there is less items it is AT LEAST
     * {@code MINIMUM_SIZE}).
     *
     * @param height int to be set
     */
    public void setHeight(int height) {
        if (height < MINIMUM_SIZE) throw new IllegalArgumentException("arg2:" + height);
        window.pack();
        if (window.getHeight() < height && window.getHeight() >= MINIMUM_SIZE) {
            height = window.getHeight();
        }
        window.setSize(window.getWidth() + 30, height);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!isMouseovered(e)) this.hide();
        dispatchEvent(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseClicked(e);
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
    public void mouseWheelMoved(MouseWheelEvent e) {
        mouseClicked(e);
    }

    /**
     * Tests whether the mouseEvent happened in the region of this window
     *
     * @param e mouseEvent which is to be checked
     * @return  true if yes, false otherwise
     */
    private boolean isMouseovered(MouseEvent e) {
        Point location = e.getLocationOnScreen();
        Point windowLocation = window.getLocationOnScreen();
        return location.x >= windowLocation.x
            && location.x <= windowLocation.x + window.getWidth()
            && location.y >= windowLocation.y
            && location.y <= windowLocation.y + window.getHeight();
    }

    /**
     * Taken from {@link https://docs.oracle.com/javase/tutorial/uiswing/components/rootpane.html}
     *
     * @param e Event fired while a mouseEvent happens while over glasPane set in this.show()
     */
    private void dispatchEvent(MouseEvent e) {
        Point glassPanePoint = e.getPoint();
        Container container = (Container) MainWindow.getContentPane();
        Point containerPoint = SwingUtilities.convertPoint(
            MainWindow.getGlassPane(),
            glassPanePoint,
            container
        );

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
                    MainWindow.getGlassPane(),
                    glassPanePoint,
                    component
                );
                if (e instanceof MouseWheelEvent) {
                    dispatchToTarget((MouseWheelEvent) e, component, componentPoint);
                } else {
                    dispatchToTarget(e, component, componentPoint);
                }
            }
        }
    }

    private void dispatchToTarget(MouseEvent e, Component target, Point location) {
        target.dispatchEvent(new MouseEvent(
            target,
            e.getID(),
            e.getWhen(),
            e.getModifiersEx(),
            location.x,
            location.y,
            e.getClickCount(),
            e.isPopupTrigger())
        );
    }

    private void dispatchToTarget(MouseWheelEvent e, Component target, Point location) {
        target.dispatchEvent(new MouseWheelEvent(
            target,
            e.getID(),
            e.getWhen(),
            e.getModifiersEx(),
            location.x,
            location.y,
            e.getClickCount(),
            e.isPopupTrigger(),
            e.getScrollType(),
            e.getScrollAmount() + 6,
            e.getWheelRotation()
            )
        );
    }
}
