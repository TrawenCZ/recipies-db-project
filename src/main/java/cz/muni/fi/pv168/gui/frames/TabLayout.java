package cz.muni.fi.pv168.gui.frames;

import javax.swing.*;

import cz.muni.fi.pv168.gui.frames.tabs.TabNamesEnum;
import cz.muni.fi.pv168.gui.frames.tabs.CategoriesTab;
import cz.muni.fi.pv168.gui.frames.tabs.IngredientsTab;
import cz.muni.fi.pv168.gui.frames.tabs.RecipeTab;
import cz.muni.fi.pv168.gui.frames.tabs.UnitsTab;

import java.awt.*;

public class TabLayout {
    final static String RECIPES = TabNamesEnum.RECIPES.getName();
    final static String CATEGORIES = TabNamesEnum.CATEGORIES.getName();
    final static String INGREDIENTS = TabNamesEnum.INGREDIENTS.getName();
    final static String UNITS = TabNamesEnum.UNITS.getName();

    private JPanel recipes;
    private JPanel ingredients;
    private JPanel categories;
    private JPanel units;

    public void createTabs(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();
        recipes = new RecipeTab();
        ingredients = new IngredientsTab();
        categories = new CategoriesTab();
        units = new UnitsTab();


        tabbedPane.addTab(RECIPES, recipes);
        tabbedPane.addTab(CATEGORIES, ingredients);
        tabbedPane.addTab(INGREDIENTS, categories);
        tabbedPane.addTab(UNITS, units);

        pane.add(tabbedPane, BorderLayout.CENTER);
    }

    public JPanel getTabByName(TabNamesEnum name) {
        switch (name) {
            case RECIPES -> {
                return recipes;
            }
            case CATEGORIES -> {
                return ingredients;
            }
            case INGREDIENTS -> {
                return categories;
            }
            case UNITS -> {
                return units;
            }
            default -> {
                return null;
            }
        }
    }
}
