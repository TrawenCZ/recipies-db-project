package cz.muni.fi.pv168.gui.frames;

import cz.muni.fi.pv168.gui.action.ConfigAction;
import cz.muni.fi.pv168.gui.action.ExportAllAction;
import cz.muni.fi.pv168.gui.action.ImportAllAction;
import cz.muni.fi.pv168.gui.elements.CustomMenu;
import cz.muni.fi.pv168.gui.frames.tabs.*;
import cz.muni.fi.pv168.gui.models.*;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.wiring.DependencyProvider;
import cz.muni.fi.pv168.wiring.Supported;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class MainWindow {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 600;
    private static final Dimension MINIMUM_SIZE = new Dimension(WIDTH, HEIGHT - HEIGHT / 3);
    private static final String TITLE = "Recipes app";
    private static final Map<String, AbstractTab> tabs = new HashMap<String, AbstractTab>();

    // we expect only one such object during runtime
    private static JFrame frame;

    private static DependencyProvider dependencies;


    private final JMenuBar menuBar = new JMenuBar();

    public MainWindow(DependencyProvider dependencyProvider) {
        if (dependencyProvider == null) throw new NullPointerException("missing dependencies");
        if (frame != null) frame.dispose();
        frame = new JFrame();

        dependencies = dependencyProvider;
        tabs.put(Supported.CATEGORY, new CategoriesTab());
        tabs.put(Supported.UNIT, new UnitsTab());
        tabs.put(Supported.INGREDIENT, new IngredientsTab());
        tabs.put(Supported.RECIPE, new RecipeTab());

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
        JMenuItem settings = new JMenuItem(new ConfigAction());
        JMenuItem imports = new JMenuItem(new ImportAllAction());
        JMenuItem export = new JMenuItem(new ExportAllAction());

        int iSize = 20;
        settings.setIcon(Icons.resizeIcon(settings.getIcon(), iSize));
        imports.setIcon(Icons.resizeIcon(imports.getIcon(), iSize));
        export.setIcon(Icons.resizeIcon(export.getIcon(), iSize));

        JMenu fileMenu = new CustomMenu("File", settings, imports, export);
        fileMenu.setMnemonic(KeyEvent.VK_F);

        menuBar.add(fileMenu);
    }

    private void addHelpMenu() {
        JMenuItem info = new JMenuItem("Info", KeyEvent.VK_I);
        info.addActionListener(e -> JOptionPane.showConfirmDialog(
                MainWindow.getContentPane(),
                """
                    Application for a personal storage of all the recipes that you can think of.

                    It was created as a semestral project on FI MUNI for PV168 by:
                        Team lead:
                            Marek Skácelík
                        Developers:
                            Adam Slíva
                            Jan Martinek
                            Radim Stejskal

                    Many thanks to:
                        Project Manager - Michael Koudela
                        Technical Coach - Jakub Smadiš
                """,
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
        tabbedPane.addTab(Supported.RECIPE, tabs.get(Supported.RECIPE));
        tabbedPane.addTab(Supported.INGREDIENT, tabs.get(Supported.INGREDIENT));
        tabbedPane.addTab(Supported.UNIT, tabs.get(Supported.UNIT));
        tabbedPane.addTab(Supported.CATEGORY, tabs.get(Supported.CATEGORY));
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    public static UnitsTableModel getUnitsModel() {
        return ((UnitsTab) tabs.get(Supported.UNIT)).getModel();
    }

    public static IngredientTableModel getIngredientModel() {
        return ((IngredientsTab) tabs.get(Supported.INGREDIENT)).getModel();
    }

    public static CategoryTableModel getCategoryModel() {
        return ((CategoriesTab) tabs.get(Supported.CATEGORY)).getModel();
    }

    public static RecipeTableModel getRecipeModel() {
        return ((RecipeTab) tabs.get(Supported.RECIPE)).getModel();
    }

    public static Map<String, AbstractTab> getTabs() {
        return tabs;
    }

    public static DependencyProvider getDependencies() {
        return dependencies;
    }
}
