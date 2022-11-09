package cz.muni.fi.pv168.gui.coloring;

import java.awt.Color;

/**
 * Use on objects that actively (i.e. their coloration depends on it)
 * use {@link java.awt.Color}.
 *
 * @author Jan Martinek
 */
public interface Colorable {

    /**
     * Gets the color from of the object.
     *
     * @return      Color or null
     */
    public Color getColor();
}
