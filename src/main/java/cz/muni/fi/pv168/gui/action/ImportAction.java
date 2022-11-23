package cz.muni.fi.pv168.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Objects;

import javax.swing.*;

import cz.muni.fi.pv168.data.manipulation.DataManipulationException;
import cz.muni.fi.pv168.data.manipulation.JsonImporter;
import cz.muni.fi.pv168.data.manipulation.services.Service;
import cz.muni.fi.pv168.gui.elements.JsonFileChooser;
import cz.muni.fi.pv168.gui.frames.MainWindow;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.model.Nameable;
import cz.muni.fi.pv168.wiring.CommonDependencyProvider;

/**
 * @author Jan Martinek, Radim Stejskal
 */
public class ImportAction<T extends Nameable> extends AbstractAction {

    private final JsonImporter JSONImporter = CommonDependencyProvider.getJsonImporter();
    private final JTable table;
    private final Class<T> aClass;
    private final Service<T> service;

    public ImportAction(JTable table, Service<T> service, Class<T> aClass) {
        super("Import", Icons.IMPORT_S);
        this.table = Objects.requireNonNull(table);
        this.service = Objects.requireNonNull(service);
        this.aClass = Objects.requireNonNull(aClass);
        putValue(SHORT_DESCRIPTION, "Imports records to current tab from a file");
        putValue(MNEMONIC_KEY, KeyEvent.VK_I);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        var fileChooser = new JsonFileChooser(false, true);
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                var records = JSONImporter.loadEntities(
                    fileChooser.getSelectedFile().getAbsolutePath(),
                    aClass
                );
                int[] count = service.saveRecords(records);
                if (count[0] > 0 || count[1] < 0) {
                    table.getRowSorter().allRowsChanged();
                    showSuccessfulImportMessage(count[1], count[0]);
                } else {
                    showNoRowsImportedMessage(count[1]);
                }
                table.revalidate();
                table.repaint();
            } catch (DataManipulationException e) {
                showInvalidFormatMessage(e.getMessage());
                e.printStackTrace();
            } catch (NullPointerException e) {
                showInvalidFormatMessage();
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showInvalidFormatMessage(String msg) {
        JOptionPane.showMessageDialog(
            MainWindow.getGlassPane(),
            """
            Please check that you are trying to import records of the same
            type as the current tab and that the JSON file fits the format.
            """
            + "\n" + msg,
            "Import failure",
            JOptionPane.ERROR_MESSAGE
        );
    }

    private void showInvalidFormatMessage() {
        showInvalidFormatMessage("");
    }

    private void showNoRowsImportedMessage(int discardedCount) {
        JOptionPane.showMessageDialog(
            MainWindow.getGlassPane(),
            "!! Nothing was imported !!" + getAction(discardedCount, "Discarded"),
            "Import success",
            JOptionPane.WARNING_MESSAGE
        );
    }

    private void showSuccessfulImportMessage(int actionCount, int createdCount) {
        String message = "Import successful.";
        message += getAction(createdCount, "Created");
        message += getAction(-actionCount, "Replaced");
        message += getAction(actionCount, "Discarded");
        JOptionPane.showMessageDialog(
            MainWindow.getGlassPane(),
            message,
            "Import success",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private String getAction(int count, String action) {
        return (count > 0) ? "\n" + action + ": " + count : "";
    }
}
