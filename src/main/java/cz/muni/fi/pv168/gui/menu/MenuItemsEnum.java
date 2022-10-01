package cz.muni.fi.pv168.gui.menu;

public enum MenuItemsEnum {
    SETTINGS("Settings"),
    IMPORT("Import"),
    EXPORT("Export"),
    INFO("Info");

    private final String label;

    MenuItemsEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
