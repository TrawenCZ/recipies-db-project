package cz.muni.fi.pv168.gui.action;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import javax.swing.*;

import cz.muni.fi.pv168.data.manipulation.JsonExporter;
import cz.muni.fi.pv168.data.service.AbstractService;
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
        this.actionPerformed(e, IntStream.rangeClosed(0, table.getRowCount()).boxed().toList());
    }

    public void actionPerformed(ActionEvent e, List<Integer> indexes) {
        var fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

        int dialogResult = fileChooser.showOpenDialog(table);
        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            File importFile = fileChooser.getSelectedFile();
            try {
                List<T> exportedEntities = service.selectRecords(indexes);
                exporter.saveEntities(importFile.getAbsolutePath(), exportedEntities);
                JOptionPane.showMessageDialog(new Frame(), "The export was successfully created.");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(new JFrame(), "The records could not be exported, please " +
                                "check your permissions for the selected directory.", "Export error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
