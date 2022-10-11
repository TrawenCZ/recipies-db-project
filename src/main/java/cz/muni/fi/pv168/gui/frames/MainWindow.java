package cz.muni.fi.pv168.gui.frames;

import cz.muni.fi.pv168.gui.menu.CustomMenu;
import cz.muni.fi.pv168.gui.resources.Icons;

import javax.swing.*;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import static cz.muni.fi.pv168.gui.menu.MenuItemsEnum.EXPORT;
import static cz.muni.fi.pv168.gui.menu.MenuItemsEnum.IMPORT;
import static cz.muni.fi.pv168.gui.menu.MenuItemsEnum.INFO;
import static cz.muni.fi.pv168.gui.menu.MenuItemsEnum.SETTINGS;


public class MainWindow {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 500;
    private static final Dimension MINIMUM_SIZE = new Dimension(WIDTH - 200, HEIGHT - 200);
    private static final String TITLE = "Recipes app";

    private static JFrame frame;

    private JMenuBar menuBar;
    private TabLayout tabLayout;

    public MainWindow() {
        initialize();
    }

    private void initialize() {
        if (frame != null) {
            frame.dispose();
        }

        frame = new JFrame();
        menuBar = new JMenuBar();
        tabLayout = new TabLayout();

        frame.setTitle(TITLE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(MINIMUM_SIZE);

        tabLayout.createTabs(frame.getContentPane());
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
}