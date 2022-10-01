package cz.muni.fi.pv168.gui.frames.cards;

public enum CardNamesEnum {
    RECIPES("Recipes"),
    CATEGORIES("Categories"),
    INGREDIENTS("Ingredients"),
    UNITS("Units");

    private final String name;

    CardNamesEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

