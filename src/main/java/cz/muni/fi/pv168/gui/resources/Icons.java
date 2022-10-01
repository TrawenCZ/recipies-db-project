package cz.muni.fi.pv168.gui.resources;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.net.URL;
import java.awt.Image;

public final class Icons {

    public static final Icon ADD_S = createIcon("add_48.png");
    public static final Icon ADD_L = createIcon("add_96.png");

    public static final Icon EDIT_S = createIcon("edit_48.png");
    public static final Icon EDIT_L = createIcon("edit_96.png");

    public static final Icon DELETE_S = createIcon("delete_48.png");
    public static final Icon DELETE_L = createIcon("delete_96.png");

    public static final Icon EXPORT_S = createIcon("export_48.png");
    public static final Icon EXPORT_L = createIcon("export_96.png");

    public static final Icon IMPORT_S = createIcon("import_48.png");
    public static final Icon IMPORT_L = createIcon("import_96.png");

    public static final Icon RESET_S = createIcon("reset_48.png");
    public static final Icon RESET_L = createIcon("reset_96.png");

    public static final Icon SEARCH_S = createIcon("search_48.png");
    public static final Icon SEARCH_L = createIcon("search_96.png");
    
    public static final Icon SETTINGS_S = createIcon("settings_48.png");
    public static final Icon SETTINGS_L = createIcon("settings_96.png");

    private Icons() {
        // INTENTIONALY PRIVATE
    }

    public static ImageIcon getScaledIcon(ImageIcon icon, int size) {
        return new ImageIcon(icon.getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT));
    }

    public static ImageIcon resizeIcon(Icon icon, int size) {
        if (!(icon instanceof ImageIcon)) throw new IllegalArgumentException("icon must be ImageIcon");
        return Icons.getScaledIcon((ImageIcon) icon, size);
    }

    private static ImageIcon createIcon(String name) {
        URL url = Icons.class.getResource(name);
        if (url == null) {
            throw new IllegalArgumentException("Icon resource not found on classpath: " + name);
        }
        return new ImageIcon(url);
    }
}