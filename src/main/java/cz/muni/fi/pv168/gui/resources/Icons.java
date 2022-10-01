package cz.muni.fi.pv168.gui.resources;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.net.URL;
import java.awt.Image;

public final class Icons {

    public static final Icon ADD_ICON_S = createIcon("add_48.png");
    public static final Icon ADD_ICON_L = createIcon("add_96.png");

    public static final Icon DELETE_ICON_S = createIcon("delete_48.png");
    public static final Icon DELETE_ICON_L = createIcon("delete_96.png");

    public static final Icon EXPORT_ICON_S = createIcon("export_48.png");
    public static final Icon EXPORT_ICON_L = createIcon("export_48.png");

    public static final Icon IMPORT_ICON_S = createIcon("import_48.png");
    public static final Icon IMPORT_ICON_L = createIcon("import_48.png");

    public static final Icon RESET_ICON_S = createIcon("reset_48.png");
    public static final Icon RESET_ICON_L = createIcon("reset_48.png");

    public static final Icon SEARCH_ICON_S = createIcon("search_48.png");
    public static final Icon SEARCH_ICON_L = createIcon("search_48.png");
    
    private Icons() {
        throw new AssertionError("This class is not instantiable");
    }

    public static ImageIcon getScaledIcon(ImageIcon icon, int size) {
        return new ImageIcon(icon.getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT));
    }

    private static ImageIcon createIcon(String name) {
        URL url = Icons.class.getResource(name);
        if (url == null) {
            throw new IllegalArgumentException("Icon resource not found on classpath: " + name);
        }
        return new ImageIcon(url);
    }
}