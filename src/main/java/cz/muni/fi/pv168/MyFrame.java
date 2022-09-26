package cz.muni.fi.pv168;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MyFrame extends JFrame {

    private static final int HEIGHT = 500;
    private static final int WIDTH = 500;
    MyFrame(){

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setLayout(new FlowLayout());

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem settings = new JMenuItem("Settings");
        JMenuItem units = new JMenuItem("Units");
        JMenuItem importFile = new JMenuItem("Import");
        JMenuItem exportFile = new JMenuItem("Export");

        fileMenu.setMnemonic(KeyEvent.VK_F);

        settings.setMnemonic(KeyEvent.VK_S);
        units.setMnemonic(KeyEvent.VK_U);
        importFile.setMnemonic(KeyEvent.VK_I);
        exportFile.setMnemonic(KeyEvent.VK_E);


        fileMenu.add(settings);
        //-----------------------------------
        fileMenu.addSeparator();
        fileMenu.add(units);
        //-----------------------------------
        fileMenu.addSeparator();
        fileMenu.add(importFile);
        fileMenu.add(exportFile);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem info = new JMenuItem("Info");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        info.setMnemonic(KeyEvent.VK_I);
        helpMenu.add(info);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        this.setJMenuBar(menuBar);

        this.setVisible(true);
        this.setResizable(false);
    }
}
