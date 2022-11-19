package cz.muni.fi.pv168.gui.frames;

import cz.muni.fi.pv168.data.service.RecipeService;
import cz.muni.fi.pv168.gui.action.ExportAction;
import cz.muni.fi.pv168.gui.action.ImportAction;
import cz.muni.fi.pv168.gui.frames.tabs.*;
import cz.muni.fi.pv168.gui.menu.CustomMenu;
import cz.muni.fi.pv168.gui.models.*;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.model.Category;
import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.Recipe;
import cz.muni.fi.pv168.model.Unit;
import cz.muni.fi.pv168.wiring.DependencyProvider;
import cz.muni.fi.pv168.wiring.TabNamesEnum;

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
    private static UnitsTableModel unitModel;
    private static CategoryTableModel categoryModel;
    private static IngredientTableModel ingredientModel;
    private static RecipeTableModel recipeModel;

    private final JMenuBar menuBar = new JMenuBar();

    public MainWindow(DependencyProvider dependencyProvider) {
        if (dependencyProvider == null) throw new NullPointerException("missing dependencies");
        if (frame != null) frame.dispose();
        frame = new JFrame();

        unitModel = new UnitsTableModel(dependencyProvider.getUnitRepository());
        categoryModel = new CategoryTableModel(dependencyProvider.getCategoryRepository());
        ingredientModel = new IngredientTableModel(dependencyProvider.getIngredientRepository());
        recipeModel = new RecipeTableModel(dependencyProvider.getRecipeRepository());

        setLayout(dependencyProvider);
    }

    private void setLayout(DependencyProvider dependencyProvider) {
        frame.setTitle(TITLE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(MINIMUM_SIZE);

        addTabs(dependencyProvider);
        addMenus();

        frame.setVisible(true);
    }

    public static Component getGlassPane() {
        return frame.getGlassPane();
    }

    public static Component getContentPane() {
        return frame.getContentPane();
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
        JMenu helpMenu = new CustomMenu("Help", info);
        helpMenu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(helpMenu);
    }
    //TODO: inject tables to exporetrs
    public void addTabs(DependencyProvider dependencyProvider) {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Recipes", new RecipeTab(
                (ImportAction<Recipe>) dependencyProvider.getImportAction(TabNamesEnum.RECIPES.getName()),
                null //(ExportAction<Recipe>) dependencyProvider.getExportAction(TabNamesEnum.RECIPES.getName())

        ));
        tabbedPane.addTab("Categories", new CategoriesTab(
                (ImportAction<Category>) dependencyProvider.getImportAction(TabNamesEnum.CATEGORIES.getName()),
                null //(ExportAction<Category>) dependencyProvider.getExportAction(TabNamesEnum.CATEGORIES.getName())
        ));
        tabbedPane.addTab("Ingredient", new IngredientsTab(
                (ImportAction<Ingredient>) dependencyProvider.getImportAction(TabNamesEnum.INGREDIENTS.getName()),
                null //(ExportAction<Ingredient>) dependencyProvider.getExportAction(TabNamesEnum.INGREDIENTS.getName())
        ));
        tabbedPane.addTab("Units", new UnitsTab(
                (ImportAction<Unit>) dependencyProvider.getImportAction(TabNamesEnum.UNITS.getName()),
                null // (ExportAction<Unit>) dependencyProvider.getExportAction(TabNamesEnum.UNITS.getName())
        ));
        frame.add(tabbedPane, BorderLayout.CENTER);
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
}
