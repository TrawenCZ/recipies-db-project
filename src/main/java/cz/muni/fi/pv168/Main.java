package cz.muni.fi.pv168;

import java.util.NoSuchElementException;
import org.tinylog.Logger;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;

import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.data.validation.ValidationException;
import cz.muni.fi.pv168.gui.frames.MainWindow;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.wiring.DependencyProvider;
import cz.muni.fi.pv168.wiring.ProductionDependencyProvider;


public class Main {

    public static void main(String[] args) {
        Config.load();
        initFlatlafLookAndFeel(Config.THEME);
        UIManager.getLookAndFeelDefaults().put("defaultFont", Config.FONT);
        try {
            final DependencyProvider dependencyProvider = new ProductionDependencyProvider();
            SwingUtilities.invokeLater(() -> new MainWindow(dependencyProvider));
        } catch (ValidationException|NoSuchElementException e) {
            Logger.error("Database corrupted: %s".formatted(e.getMessage()));
            goNuclear();
        }
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
            Logger.error("Nimbus layout initialization failed: " + ex.getMessage());
        }
    }

    private static void goNuclear() {
        int n = JOptionPane.showOptionDialog(
            null,
            "CRITICAL ERROR:\nDatabase is corrupted!\n\nYou can resolve this issue by deleting all data.\nDo you want to delete them?",
            "Nuclear quit",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.ERROR_MESSAGE,
            Icons.DELETE_L,
            null, null);
        if (n == JOptionPane.OK_OPTION) {
            DatabaseManager.createProductionInstance().destroySchema();
        }
        System.exit(n);
    }
}
