package cz.muni.fi.pv168;

import cz.muni.fi.pv168.gui.frames.MainWindow;
import com.formdev.flatlaf.*;
import cz.muni.fi.pv168.wiring.DependencyProvider;
import cz.muni.fi.pv168.wiring.ProductionDependencyProvider;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class Main {

    public static final String THEME = "intellij";
    public static void main(String[] args) {
        final DependencyProvider dependencyProvider = new ProductionDependencyProvider();
        initFlatlafLookAndFeel(THEME); // can be reworked to load preconfigured
        SwingUtilities.invokeLater(MainWindow::new);
    }

    /**
     * Call before creating main window. Sets the look and feel to Flatlaf:
     * <pre>
     * Possible options:
     * intellij = light theme modeled after IntelliJ
     * darcula = dark theme modeled after Darcula
     * dark = default flatlaf dark theme
     * light = default flatlaf light theme
     * </pre>
     * If none of those is used as an argument, sets the theme to NIMBUS.
     */
    private static void initFlatlafLookAndFeel(String theme) {
        if (theme != null && theme.toLowerCase().equals("intellij")) {
            FlatIntelliJLaf.setup();
        } else if (theme != null && theme.toLowerCase().equals("darcula")) {
            FlatDarculaLaf.setup();
        } else if (theme != null && theme.toLowerCase().equals("dark")) {
            FlatDarkLaf.setup();
        } else if (theme != null && theme.toLowerCase().equals("light")) {
            FlatLightLaf.setup();
        } else {
            initNimbusLookAndFeel();
        }
    }

    private static void initNimbusLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Nimbus layout initialization failed", ex);
        }
    }
}
