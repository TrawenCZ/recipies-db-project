package cz.muni.fi.pv168.gui.action;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import javax.swing.*;

import cz.muni.fi.pv168.data.manipulation.JsonExporter;
import cz.muni.fi.pv168.data.service.AbstractService;
import cz.muni.fi.pv168.gui.elements.JsonFileChooser;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.model.Nameable;

/**
 * @author Jan Martinek
 */
public class ExportAction<T extends Nameable> extends AbstractAction {

    private final JTable table;
    private final JsonExporter exporter;
    private final AbstractService<T> service;

    public ExportAction(JTable table, JsonExporter exporter, AbstractService<T> service) {
        super("Export", Icons.EXPORT_S);
        this.table = Objects.requireNonNull(table);
        this.exporter = Objects.requireNonNull(exporter);
        this.service = Objects.requireNonNull(service);
        putValue(SHORT_DESCRIPTION, "Exports records from current tab to a file");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var indexes = IntStream.rangeClosed(0, table.getRowCount()).boxed().toList();
        var fileChooser = new JsonFileChooser(false, true);

        if (fileChooser.showOpenDialog(table) == JFileChooser.APPROVE_OPTION) {
            try {
                List<T> exportedEntities = service.selectRecords(indexes);
                exporter.saveEntities(fileChooser.getJsonPath(), exportedEntities);
                JOptionPane.showMessageDialog(new Frame(), "Exporting was successful.");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                    new JFrame(),
                    """
                        The records could not be exported, please check the write
                        permissions to the selected directory.
                    """,
                    "Export error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
