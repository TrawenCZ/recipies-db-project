package cz.muni.fi.pv168.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.*;

import cz.muni.fi.pv168.data.manipulation.JsonExporter;
import cz.muni.fi.pv168.gui.elements.JsonFileChooser;
import cz.muni.fi.pv168.gui.models.AbstractModel;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.model.Identifiable;
import cz.muni.fi.pv168.model.Nameable;
import cz.muni.fi.pv168.wiring.CommonDependencyProvider;

/**
 * @author Jan Martinek
 */
public class ExportAction<T extends Nameable & Identifiable> extends AbstractAction {

    private final JsonExporter exporter = CommonDependencyProvider.getJsonExporter();
    private final JTable table;
    private final AbstractModel<T> model;

    public ExportAction(JTable table, AbstractModel<T> model) {
        super("Export", Icons.EXPORT_S);
        this.table = Objects.requireNonNull(table);
        this.model = Objects.requireNonNull(model);
        putValue(SHORT_DESCRIPTION, "Exports records from current tab to a file");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var fileChooser = new JsonFileChooser(false, true);
        fileChooser.setSelectedFile(new File(model.toString().toLowerCase()));

        if (fileChooser.showDialog(table, "Save") == JFileChooser.APPROVE_OPTION) {
            try {
                List<T> exportedEntities;
                if (e == null) {
                    exportedEntities = model.getRepository().findAll();
                } else {
                    exportedEntities = getSelectedObjects(getSelectedRows());
                }
                exporter.saveEntities(fileChooser.getJsonPath(), exportedEntities);
                showSuccessMessage(exportedEntities.size());
            } catch (IOException ex) {
                showErrorMessage();
            }
        }
    }

    private List<T> getSelectedObjects(int[] indexes) {
        List<T> objects = new ArrayList<>();
        for (var i : indexes) {
            objects.add(model.getEntity(i));
        }
        return objects;
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
