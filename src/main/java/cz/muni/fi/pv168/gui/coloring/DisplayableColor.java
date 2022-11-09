package cz.muni.fi.pv168.gui.coloring;

import java.awt.Color;

public class DisplayableColor extends Color {

    public DisplayableColor(int rgb) {
        super(rgb);
    }

    public String toHex() {
        return "0x" +
            String.format("%02X", this.getRed()).toUpperCase() +
            String.format("%02X", this.getGreen()).toUpperCase() +
            String.format("%02X", this.getBlue()).toUpperCase();
    }

    @Override
    public String toString() {
        return "";
    }
}
