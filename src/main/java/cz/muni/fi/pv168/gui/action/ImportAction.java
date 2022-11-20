package cz.muni.fi.pv168.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Objects;

import javax.swing.*;

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
                int count = service.saveRecords(records);
                if (count != 0) {
                    table.getRowSorter().allRowsChanged();
                    showSuccessfulImportMessage(count, records.size());
                } else {
                    showNoRowsImportedMessage(records.size());
                }
                table.revalidate();
                table.repaint();
            } catch (NullPointerException e) {
                // TODO log
                e.printStackTrace();
                showInvalidFormatMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showInvalidFormatMessage() {
        JOptionPane.showMessageDialog(
            MainWindow.getContentPane(),
            """
                Please check that you are trying to import records of the same
                type as the current tab and that the JSON file fits the format.
            """,
            "Import failure",
            JOptionPane.ERROR_MESSAGE
        );
    }

    private void showNoRowsImportedMessage(int discardedCount) {
        JOptionPane.showMessageDialog(
            MainWindow.getContentPane(),
            "!! Nothing was imported !!" + getAction(discardedCount, "Discarded"),
            "Import success",
            JOptionPane.WARNING_MESSAGE
        );
    }

    private void showSuccessfulImportMessage(int count, int originalCount) {
        String message = "Successfully imported " + originalCount +
            " row" + ((originalCount > 1) ? "s" : "") +
            " in current tab.";
        message += getAction(-count, "Total replaced");
        message += getAction(count, "Total discarded");
        JOptionPane.showMessageDialog(
            MainWindow.getContentPane(),
            message,
            "Import success",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private String getAction(int count, String action) {
        return (count > 0) ? "\n" + action + ": " + count + " object" + ((count > 1) ? "s!" : "!") : "";
    }
}
