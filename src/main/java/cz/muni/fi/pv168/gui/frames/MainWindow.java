package cz.muni.fi.pv168.gui.frames;

import cz.muni.fi.pv168.gui.frames.cards.CardNamesEnum;
import cz.muni.fi.pv168.gui.frames.forms.AddCategoryForm;
import cz.muni.fi.pv168.gui.frames.forms.AddIngredientForm;
import cz.muni.fi.pv168.gui.frames.forms.AddUnitForm;
import cz.muni.fi.pv168.gui.layouts.CustomCardLayout;
import cz.muni.fi.pv168.gui.menu.CustomMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import static cz.muni.fi.pv168.gui.menu.MenuItemsEnum.EXPORT;
import static cz.muni.fi.pv168.gui.menu.MenuItemsEnum.IMPORT;
import static cz.muni.fi.pv168.gui.menu.MenuItemsEnum.INFO;
import static cz.muni.fi.pv168.gui.menu.MenuItemsEnum.SETTINGS;


public class MainWindow {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 500;
    private static final String TITLE = "Recipes app";

    private JFrame frame;
    private JMenuBar menuBar;
    private CustomCardLayout cardLayout;



    public MainWindow() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        menuBar = new JMenuBar();
        cardLayout = new CustomCardLayout();

        frame.setTitle(TITLE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        cardLayout.createCards(frame.getContentPane());
        setCardsContent();
        addMenus();

        frame.setVisible(true);
    }

    private void addMenus() {
        addFileMenu();
        addHelpMenu();
        frame.setJMenuBar(menuBar);
    }

    private void addFileMenu() {
        JMenuItem settings = new JMenuItem(SETTINGS.getLabel(), KeyEvent.VK_S);
        JMenuItem imports = new JMenuItem(IMPORT.getLabel(), KeyEvent.VK_I);
        JMenuItem export = new JMenuItem(EXPORT.getLabel(), KeyEvent.VK_E);

        JMenu fileMenu = new CustomMenu("File", settings, imports, export);
        fileMenu.setMnemonic(KeyEvent.VK_F);

        menuBar.add(fileMenu);
    }

    private void addHelpMenu() {
        JMenuItem info = new JMenuItem(INFO.getLabel(), KeyEvent.VK_I);

        // TODO: possibly add shortcuts overview (if not built into buttons themselves)
        JMenu helpMenu = new CustomMenu("Help", info);
        helpMenu.setMnemonic(KeyEvent.VK_H);

        menuBar.add(helpMenu);
    }

    private void setCardsContent() {
        JPanel recipes = cardLayout.getCardByName(CardNamesEnum.RECIPES);
        JPanel categories = cardLayout.getCardByName(CardNamesEnum.CATEGORIES);
        JPanel ingredients = cardLayout.getCardByName(CardNamesEnum.INGREDIENTS);
        JPanel units = cardLayout.getCardByName(CardNamesEnum.UNITS);
        setCategoriesCardContent(categories);
        setIngredientsCardContent(ingredients);
        setUnitsCardContent(units);
    }

    private void setCategoriesCardContent(JPanel categories) {
        JButton addCategory = new JButton("Add category");

        categories.setLayout(new BorderLayout());
        // recipes.setBackground(Color.RED);
        JPanel categoriesFilterPanel = new JPanel();
        // recipesFilterPanel.setBackground(Color.CYAN);
        categories.add(categoriesFilterPanel, BorderLayout.NORTH);
        categoriesFilterPanel.setLayout(new FlowLayout());
        addCategory.addActionListener(e -> new AddCategoryForm());
        categoriesFilterPanel.add(addCategory);
    }

    private void setIngredientsCardContent(JPanel ingredients) {
        JButton addIngredient = new JButton("Add ingredient");

        ingredients.setLayout(new BorderLayout());
        // recipes.setBackground(Color.RED);
        JPanel ingredientsFilterPanel = new JPanel();
        // recipesFilterPanel.setBackground(Color.CYAN);
        ingredients.add(ingredientsFilterPanel, BorderLayout.NORTH);
        ingredientsFilterPanel.setLayout(new FlowLayout());
        addIngredient.addActionListener(e -> new AddIngredientForm());
        ingredientsFilterPanel.add(addIngredient);
    }

    private void setUnitsCardContent(JPanel units){
        JButton addIngredient = new JButton("Add Unit");

        units.setLayout(new BorderLayout());
        // recipes.setBackground(Color.RED);
        JPanel unitsFilterPanel = new JPanel();
        // recipesFilterPanel.setBackground(Color.CYAN);
        units.add(unitsFilterPanel, BorderLayout.NORTH);
        unitsFilterPanel.setLayout(new FlowLayout());
        addIngredient.addActionListener(e -> new AddUnitForm());
        unitsFilterPanel.add(addIngredient);
    }
}
