package cz.muni.fi.pv168.gui.action;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Objects;

import javax.swing.*;

import cz.muni.fi.pv168.data.manipulation.DataManipulationException;
import cz.muni.fi.pv168.data.manipulation.JsonImporter;
import cz.muni.fi.pv168.data.service.AbstractService;
import cz.muni.fi.pv168.exceptions.ImportVsDatabaseRecordsConflictException;
import cz.muni.fi.pv168.gui.elements.JsonFileChooser;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.model.Nameable;

/**
 * @author Jan Martinek, Radim Stejskal
 */
public class ImportAction<T extends Nameable> extends AbstractAction {

    private final JsonImporter JSONImporter;
    private final Class<T> aClass;
    private final AbstractService<T> service;
    private final Object[] options = {"Abort", "Skip conflicts", "Force import"};

    public ImportAction(JsonImporter JSONImporter, AbstractService<T> service, Class<T> aClass) {
        super("Import", Icons.IMPORT_S);
        this.JSONImporter = Objects.requireNonNull(JSONImporter);
        this.aClass = aClass;
        this.service = service;
        putValue(SHORT_DESCRIPTION, "Imports records to current tab from a file");
        putValue(MNEMONIC_KEY, KeyEvent.VK_I);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));
    }

    public void handleSkipInconsistent(List<T> records) {
        int recordsSaved = service.saveRecords(records, true);
        showSuccessfulImportMessage(recordsSaved, records.size());

    }

    public void handleSaveOrUpdate(List<T> records) {
        int recordsSaved = service.saveOrUpdateRecords(records);
        showSuccessfulImportMessage(recordsSaved, records.size());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var fileChooser = new JsonFileChooser(false, true);
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            List<T> records;
            try {
                records = JSONImporter.loadEntities(
                    fileChooser.getSelectedFile().getAbsolutePath(),
                    aClass
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        new JFrame(),
                        """
                            Please check that you are trying to import records of the same
                            type as the current tab and that the JSON file fits the format.
                        """,
                        "Invalid import",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            if (!service.validateRecordsBatch(records)) { // import file alone contains inconsistencies
                JOptionPane.showMessageDialog(
                        new JFrame(),
                        "The import file contains records of the same name but of different properties. Please " +
                                "resolve the conflicts manually and try again.",
                        "Inconsistent import file",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            try {
                showSuccessfulImportMessage(
                    service.saveRecords(records),
                    records.size()
                );
            }
            catch (ImportVsDatabaseRecordsConflictException ex) { // import file and the database contain non-equal duplicates
                int n = JOptionPane.showOptionDialog(new JFrame(), "The file contains some records of the same name as records in the database" +
                        " but with different properties. How would you like to proceed?", "Import conflicting data",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (n == 0) {
                    JOptionPane.showMessageDialog(new JFrame(),
                            "No records have been imported.",
                            "Import abort",
                            JOptionPane.INFORMATION_MESSAGE);
                } else if (n==1) {
                    handleSkipInconsistent(records);
                } else if (n==2) {
                    //TODO: implement this
                    handleSaveOrUpdate(records);
                }
            } catch (DataManipulationException ex) {

            }
        }
    }

    private void showSuccessfulImportMessage(int savedCount, int originalCount) {
        String message;
        if (savedCount == 0) {
            message =  "The file contains no new records.";
        } else {
            message = "Successfully imported " + savedCount + " records.";
        }
        int duplicates = originalCount - savedCount;
        if (duplicates > 0) {
            message += "\nDiscarded " + duplicates + " duplicate" + ((duplicates > 1) ? "s!" : "!");
        }
        JOptionPane.showMessageDialog(new Frame(), message);
    }
}
