package cz.muni.fi.pv168.gui.menu;

import javax.swing.*;
import java.util.Arrays;

public class CustomMenu extends JMenu {

    private final JMenuItem[] items;

    public CustomMenu(String title, JMenuItem... items) {
        this.setText(title);
        this.items = items;
        addItems();
    }

    private void addItems() {
        Arrays.stream(items).forEach(this::add);
    }
}
