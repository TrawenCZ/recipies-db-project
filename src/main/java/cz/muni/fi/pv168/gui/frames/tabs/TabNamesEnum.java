package cz.muni.fi.pv168.gui.frames.tabs;

public enum TabNamesEnum {
    RECIPES("Recipes"),
    CATEGORIES("Categories"),
    INGREDIENTS("Ingredients"),
    UNITS("Units");

    private final String name;

    TabNamesEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

