package cz.muni.fi.pv168.gui.elements;

import java.util.Map;
import java.util.HashMap;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import cz.muni.fi.pv168.gui.resources.Icons;

import java.awt.event.ActionListener;

public class PopupMenu extends JPopupMenu {
    
    private final static int ICON_SIZE = 22;

    private Map<String, JMenuItem> items;

    public PopupMenu() {
        items = new HashMap<>();
    }

    public void addItem(JMenuItem item, ActionListener action) {
        addItem(item, action, null, null, null);
    }

    public void addItem(JMenuItem item, ActionListener action, String tooltip) {
        addItem(item, action, tooltip, null, null);
    }

    public void addItem(JMenuItem item, ActionListener action, String shortcut, Character mnemonic) {
        addItem(item, action, null, shortcut, mnemonic);
    }
    
    public void addItem(JMenuItem item, ActionListener action, String tooltip, String shortcut, Character mnemonic) {
        addItem(item, action, tooltip, shortcut, mnemonic, ICON_SIZE);
    }
    
    public void addItem(JMenuItem item, ActionListener action, String tooltip, String shortcut, Character mnemonic, Integer size) {
        if (item == null || action == null) throw new NullPointerException();
        
        item.addActionListener(action);
        if (tooltip != null) item.setToolTipText(tooltip);
        if (shortcut != null) item.setAccelerator(KeyStroke.getKeyStroke(shortcut));
        if (mnemonic != null) item.setMnemonic(mnemonic);

        if (size != null && size >= 10) {
            var icon = item.getIcon();
            item.setIcon(Icons.resizeIcon(icon, size));
        }
        
        this.items.put(item.getText().toLowerCase(), item);
        this.add(item);
    }

    public void enableItem(String name) {
        changeItemState(name, true);
    }

    public void disableItem(String name) {
        changeItemState(name, false);
    }

    private void changeItemState(String name, boolean value) {
        if (name == null) throw new NullPointerException();
        var item = items.get(name.toLowerCase());
        if (item != null) item.setEnabled(value);
    }

}
