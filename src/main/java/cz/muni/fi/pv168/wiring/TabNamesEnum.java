package cz.muni.fi.pv168.wiring;

public enum TabNamesEnum {
    RECIPES("recipes"),
    CATEGORIES("categories"),
    UNITS("units"),
    INGREDIENTS("ingredients");

    private final String name;

    TabNamesEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
