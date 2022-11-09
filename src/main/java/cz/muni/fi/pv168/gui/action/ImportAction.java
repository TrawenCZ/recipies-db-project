package cz.muni.fi.pv168.gui.action;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.swing.*;

import cz.muni.fi.pv168.data.Validator;
import cz.muni.fi.pv168.data.manipulation.JsonImporter;
import cz.muni.fi.pv168.data.service.AbstractService;
import cz.muni.fi.pv168.exceptions.InconsistentRecordException;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.model.Nameable;

public class ImportAction<T extends Nameable> extends AbstractAction {

    private final JTable table;
    private final JsonImporter JSONImporter;
    private final Class<T> aClass;
    private final AbstractService<T> service;

    public ImportAction(JTable table, JsonImporter JSONImporter, AbstractService<T> service, Class<T> aClass) {
        super("Import", Icons.IMPORT_S);
        this.table = Objects.requireNonNull(table);
        this.JSONImporter = Objects.requireNonNull(JSONImporter);
        this.aClass = aClass;
        this.service = service;
        putValue(SHORT_DESCRIPTION, "Imports records to current tab from a file");
        putValue(MNEMONIC_KEY, KeyEvent.VK_I);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

        int dialogResult = fileChooser.showOpenDialog(table);
        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            File importFile = fileChooser.getSelectedFile();
            try {
                List<T> importedEntities = JSONImporter.loadEntities(importFile.getAbsolutePath(), aClass);
                if (Validator.containsNonEqualDuplicates(importedEntities)) {
                    JOptionPane.showMessageDialog(new JFrame(), "The file contains multiple records " +
                                    "of the same name but with different attributes. Please check the import " +
                                    "file for inconsistencies.", "Import error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    int savedRecordsCount = service.saveRecords(importedEntities);
                    showSuccessfulImportMessage(savedRecordsCount);
                } catch (InconsistentRecordException ex) {
                    JOptionPane.showMessageDialog(new JFrame(), "The file contains some records of the same name " +
                                    "as the records that are already saved but with different attributes. Please " +
                                    "resolve the inconsistencies first.", "Import error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(new JFrame(), "Please check that you are trying to import records of the " +
                                "same type as the current tab and that the JSON file is in the" +
                                " correct format.", "Import error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showSuccessfulImportMessage(int succesfullImports) {
        String message;
        if (succesfullImports == 0) {
            message =  "The file contains no new records.";
        } else if (succesfullImports == 1){
            message = "Successfully imported 1 record";
        } else {
            message = "Successfully imported " + succesfullImports + " records." +
                    "\nDuplicates were ignored.";
        }
        JOptionPane.showMessageDialog(new Frame(), message);
    }
}
