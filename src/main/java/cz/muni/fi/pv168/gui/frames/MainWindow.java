package cz.muni.fi.pv168.gui.frames;

import cz.muni.fi.pv168.gui.frames.tabs.*;
import cz.muni.fi.pv168.gui.menu.CustomMenu;
import cz.muni.fi.pv168.gui.models.*;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.wiring.DependencyProvider;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import static cz.muni.fi.pv168.gui.menu.MenuItemsEnum.EXPORT;
import static cz.muni.fi.pv168.gui.menu.MenuItemsEnum.IMPORT;
import static cz.muni.fi.pv168.gui.menu.MenuItemsEnum.INFO;
import static cz.muni.fi.pv168.gui.menu.MenuItemsEnum.SETTINGS;

public class MainWindow {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 600;
    private static final Dimension MINIMUM_SIZE = new Dimension(WIDTH, HEIGHT - HEIGHT / 3);
    private static final String TITLE = "Recipes app";

    // we expect only one such object during runtime
    private static JFrame frame;

    private static DependencyProvider dependencies;
    private static UnitsTableModel unitModel;
    private static CategoryTableModel categoryModel;
    private static IngredientTableModel ingredientModel;
    private static RecipeTableModel recipeModel;

    private final JMenuBar menuBar = new JMenuBar();

    public MainWindow(DependencyProvider dependencyProvider) {
        if (dependencyProvider == null) throw new NullPointerException("missing dependencies");
        if (frame != null) frame.dispose();
        frame = new JFrame();

        dependencies = dependencyProvider;
        unitModel = new UnitsTableModel(dependencyProvider.getUnitRepository());
        categoryModel = new CategoryTableModel(dependencyProvider.getCategoryRepository());
        ingredientModel = new IngredientTableModel(dependencyProvider.getIngredientRepository());
        recipeModel = new RecipeTableModel(dependencyProvider.getRecipeRepository());

        setLayout();
    }

    private void setLayout() {
        frame.setTitle(TITLE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(MINIMUM_SIZE);

        getContentPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addTabs();
        addMenus();

        frame.setVisible(true);
    }

    public static Component getGlassPane() {
        return frame.getGlassPane();
    }

    public static JPanel getContentPane() {
        return (JPanel) frame.getContentPane();
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

        int iSize = 20;
        settings.setIcon(Icons.resizeIcon(Icons.SETTINGS_S, iSize));
        imports.setIcon(Icons.resizeIcon(Icons.IMPORT_S, iSize));
        export.setIcon(Icons.resizeIcon(Icons.EXPORT_S, iSize));

        JMenu fileMenu = new CustomMenu("File", settings, imports, export);
        fileMenu.setMnemonic(KeyEvent.VK_F);

        menuBar.add(fileMenu);
    }

    private void addHelpMenu() {
        JMenuItem info = new JMenuItem(INFO.getLabel(), KeyEvent.VK_I);
        info.addActionListener(e -> JOptionPane.showConfirmDialog(
                null,
                "Recipes app for storing your favorite recipes made by Jan Martinek (mainly), Radim Stejskal, Marek Skácelík and Adam Slíva.",
                "Info",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE
        ));

        JMenu helpMenu = new CustomMenu("Help", info);
        helpMenu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(helpMenu);
    }

    public void addTabs() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Recipes", new RecipeTab());
        tabbedPane.addTab("Categories", new CategoriesTab());
        tabbedPane.addTab("Ingredient", new IngredientsTab());
        tabbedPane.addTab("Units", new UnitsTab());
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    public static UnitsTableModel getUnitsModel() {
        return unitModel;
    }

    public static IngredientTableModel getIngredientModel() {
        return ingredientModel;
    }

    public static CategoryTableModel getCategoryModel() {
        return categoryModel;
    }

    public static RecipeTableModel getRecipeModel() {
        return recipeModel;
    }

    public static DependencyProvider getDependencies() {
        return dependencies;
    }
}
