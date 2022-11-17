package cz.muni.fi.pv168.gui.frames;

import java.awt.*;
import javax.swing.*;

import cz.muni.fi.pv168.gui.frames.tabs.*;
import cz.muni.fi.pv168.gui.models.AbstractModel;
import cz.muni.fi.pv168.model.*;

public class TabLayout {
    private static final AbstractTab units = new UnitsTab();
    private static final AbstractTab categories = new CategoriesTab();
    private static final AbstractTab ingredients = new IngredientsTab();
    private static final AbstractTab recipes = new RecipeTab();

    public static final String RECIPES = "recipes";
    public static final String CATEGORIES = "categories";
    public static final String INGREDIENTS = "ingredients";
    public static final String UNITS = "units";

    public void createTabs(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(RECIPES, recipes);
        tabbedPane.addTab(CATEGORIES, categories);
        tabbedPane.addTab(INGREDIENTS, ingredients);
        tabbedPane.addTab(UNITS, units);

        pane.add(tabbedPane, BorderLayout.CENTER);
    }

    public static AbstractTab getTab(String name) {
        switch (name.toLowerCase()) {
            case RECIPES: return recipes;
            case CATEGORIES: return categories;
            case INGREDIENTS: return ingredients;
            case UNITS: return units;
    }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static AbstractModel<Recipe> getRecipesModel() {
        return (AbstractModel<Recipe>) recipes.getModel();
    }

    @SuppressWarnings("unchecked")
    public static AbstractModel<Category> getCategoriesModel() {
        return (AbstractModel<Category>) categories.getModel();
    }

    @SuppressWarnings("unchecked")
    public static AbstractModel<Ingredient> getIngredientsModel() {
        return (AbstractModel<Ingredient>) ingredients.getModel();
    }

    @SuppressWarnings("unchecked")
    public static AbstractModel<Unit> getUnitsModel() {
        return (AbstractModel<Unit>) units.getModel();
    }
}
