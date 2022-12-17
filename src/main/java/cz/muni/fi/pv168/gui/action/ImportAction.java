package cz.muni.fi.pv168.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Objects;

import javax.swing.*;

import org.tinylog.Logger;

import cz.muni.fi.pv168.data.manipulation.DuplicateException;
import cz.muni.fi.pv168.data.manipulation.Importer;
import cz.muni.fi.pv168.data.manipulation.Progress;
import cz.muni.fi.pv168.gui.elements.JsonFileChooser;
import cz.muni.fi.pv168.gui.frames.MainWindow;
import cz.muni.fi.pv168.gui.resources.Icons;
import cz.muni.fi.pv168.gui.workers.AsyncImporter;
import cz.muni.fi.pv168.model.Identifiable;
import cz.muni.fi.pv168.model.Nameable;

/**
 * @author Jan Martinek, Radim Stejskal
 */
public class ImportAction<T extends Nameable & Identifiable> extends AbstractAction {

    private final Importer importer;

    public ImportAction(Importer importer, Runnable callbefore, Runnable callback) {
        super("Import", Icons.IMPORT_S);

        Objects.requireNonNull(importer, "importer cannot be null");
        Objects.requireNonNull(callbefore, "callbefore cannot be null (may be empty function if needed)");
        Objects.requireNonNull(callback, "callback cannot be null (may be empty function if needed)");

        this.importer = new AsyncImporter(
            importer,
            callbefore,
            (error, filePath) -> {
                handlerError(error, filePath);
                callback.run();
            },
            (progress) -> {
                handlerFinish(progress);
                callback.run();
            }
        );

        putValue(SHORT_DESCRIPTION, "Imports records to current tab from a file");
        putValue(MNEMONIC_KEY, KeyEvent.VK_I);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        var fileChooser = new JsonFileChooser(false, true);
        if (fileChooser.showOpenDialog(MainWindow.getContentPane()) == JFileChooser.APPROVE_OPTION) {
            importer.importData(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void handlerError(Throwable error, String filePath) {
        if (error.getCause() instanceof DuplicateException) {
            int decision = getDecision();
            if (decision == JOptionPane.YES_OPTION) {
                this.importer.getProgress().setReplace();
            } else if (decision == JOptionPane.NO_OPTION) {
                this.importer.getProgress().setIgnore();
            } else {
                Logger.info("Import cancelled by user");
                return;
            }
            this.importer.importData(filePath);
        } else if (error.getCause() != null) {
            showInvalidFormatMessage(error.getCause().getMessage());
        } else {
            showInvalidFormatMessage(error.getMessage());
        }
    }

    private void handlerFinish(Progress progress) {
        if (progress.hasChanges()) {
            showSuccessfulImportMessage(progress, progress.isReplace());
        } else {
            showNoRowsImportedMessage(progress);
        }
    }

    private static int getDecision() {
        String[] options = {"Replace all", "Skip all", "Cancel"};
        int n = JOptionPane.showOptionDialog(
            MainWindow.getGlassPane(),
            "Duplicates were found during import! Please select an action:",
            "Import error",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            Icons.IMPORT_S,
            options,
            options[2]
        );
        return n;
    }

    private static void showInvalidFormatMessage(String msg) {
        JOptionPane.showMessageDialog(
            MainWindow.getGlassPane(),
            """
            Please check that you are trying to import records of correct type
            and that the JSON file fits the format.
            """
            + "\n" + msg,
            "Import failure",
            JOptionPane.ERROR_MESSAGE
        );
    }

    private static void showNoRowsImportedMessage(Progress progress) {
        JOptionPane.showMessageDialog(
            MainWindow.getGlassPane(),
            "!! Nothing was imported !!" + getAction(progress.getIgnore(), "Discarded"),
            "Import success",
            JOptionPane.WARNING_MESSAGE
        );
    }

    private static void showSuccessfulImportMessage(Progress progress, boolean replaceWasSet) {
        String message = "Import successful.";
        message += getAction(progress.getInsert(), "Created");
        message += getAction(progress.getUpdate(), "Replaced");
        message += getAction(progress.getIgnore(), replaceWasSet ? "Unchanged" : "Ignored");
        JOptionPane.showMessageDialog(
            MainWindow.getGlassPane(),
            message,
            "Import success",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private static String getAction(int count, String action) {
        return (count > 0) ? "\n" + action + ": " + count : "";
    }
}
