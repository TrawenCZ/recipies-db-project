package cz.muni.fi.pv168.gui.layouts;

import cz.muni.fi.pv168.gui.frames.cards.CardNamesEnum;
import cz.muni.fi.pv168.gui.frames.cards.CategoriesCard;
import cz.muni.fi.pv168.gui.frames.cards.IngredientsCard;
import cz.muni.fi.pv168.gui.frames.cards.RecipeCard;
import cz.muni.fi.pv168.gui.frames.cards.UnitsCard;

import javax.swing.*;
import java.awt.*;

public class CustomCardLayout {
    final static String RECIPES = CardNamesEnum.RECIPES.getName();
    final static String CATEGORIES = CardNamesEnum.CATEGORIES.getName();
    final static String INGREDIENTS = CardNamesEnum.INGREDIENTS.getName();
    final static String UNITS = CardNamesEnum.UNITS.getName();

    private JPanel recipesCard;
    private JPanel ingredientsCard;
    private JPanel categoriesCard;
    private JPanel unitsCard;

    public void createCards(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();
        recipesCard = new RecipeCard();
        ingredientsCard = new IngredientsCard();
        categoriesCard = new CategoriesCard();
        unitsCard = new UnitsCard();


        tabbedPane.addTab(RECIPES, recipesCard);
        tabbedPane.addTab(CATEGORIES, categoriesCard);
        tabbedPane.addTab(INGREDIENTS, ingredientsCard);
        tabbedPane.addTab(UNITS, unitsCard);

        pane.add(tabbedPane, BorderLayout.CENTER);
    }

    public JPanel getCardByName(CardNamesEnum name) {
        switch (name) {
            case RECIPES -> {
                return recipesCard;
            }
            case CATEGORIES -> {
                return categoriesCard;
            }
            case INGREDIENTS -> {
                return ingredientsCard;
            }
            case UNITS -> {
                return unitsCard;
            }
            default -> {
                return null;
            }
        }
    }
}
