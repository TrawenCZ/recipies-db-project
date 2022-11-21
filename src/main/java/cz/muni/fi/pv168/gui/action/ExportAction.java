package cz.muni.fi.pv168.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.swing.*;

import cz.muni.fi.pv168.data.manipulation.JsonExporter;
import cz.muni.fi.pv168.data.manipulation.services.Service;
import cz.muni.fi.pv168.gui.elements.JsonFileChooser;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.model.Identifiable;
import cz.muni.fi.pv168.model.Nameable;
import cz.muni.fi.pv168.wiring.CommonDependencyProvider;

/**
 * @author Jan Martinek
 */
public class ExportAction<T extends Nameable & Identifiable> extends AbstractAction {

    private final JsonExporter exporter = CommonDependencyProvider.getJsonExporter();
    private final Service<T> service;
    private final JTable table;
    private final String tabName;

    public ExportAction(JTable table, Service<T> service, String tabName) {
        super("Export", Icons.EXPORT_S);
        this.service = Objects.requireNonNull(service);
        this.table = Objects.requireNonNull(table);
        this.tabName = tabName;
        putValue(SHORT_DESCRIPTION, "Exports records from current tab to a file");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var fileChooser = new JsonFileChooser(false, true);
        fileChooser.setSelectedFile(new File(tabName));

        if (fileChooser.showDialog(table, "Save") == JFileChooser.APPROVE_OPTION) {
            try {
                List<T> exportedEntities = service.getRecords(getSelectedRows());
                exporter.saveEntities(fileChooser.getJsonPath(), exportedEntities);
                showSuccessMessage(exportedEntities.size());
            } catch (IOException ex) {
                // ex.printStackTrace(); TODO: log
                showErrorMessage();
            }
        }
    }

    private int[] getSelectedRows() {
        var indexes = table.getSelectedRows();
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = table.convertRowIndexToModel(indexes[i]);
        }
        return indexes;
    }

    private void showSuccessMessage(int count) {
        JOptionPane.showMessageDialog(
            table,
            "Successfully exported %d rows!".formatted(count),
            "Export success",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showErrorMessage() {
        JOptionPane.showMessageDialog(
            table,
            """
                The records could not be exported, please check the write
                permissions to the selected directory.
            """,
            "Export error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
