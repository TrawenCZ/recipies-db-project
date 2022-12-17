package cz.muni.fi.pv168.gui.frames.forms;

import cz.muni.fi.pv168.Config;
import cz.muni.fi.pv168.gui.frames.MainWindow;

import javax.swing.*;
import java.awt.*;

/**
 * Form that lets the user select from any available import.
 *
 * @author Jan Martinek
 */
public final class ImportForm extends AbstractForm {

    private JComboBox<String> input = new JComboBox<>(
        MainWindow.getTabs().keySet().stream().toArray(String[]::new)
    );

    public ImportForm() {
        super("Import", "Select table");
        initializeBody();
        show();
    }

    @Override
    protected void initializeBody() {
        input.setToolTipText("Select the table to which the import is done");

        gridExtensions(GridBagConstraints.HORIZONTAL, 0, 5);

        gridInsets(10);
        gridAdd(input, 0, 1, 2, 1);

        input.setSelectedItem(Config.THEME);
    }

    @Override
    protected boolean onAction() {
        if (getConfirmation() != JOptionPane.YES_OPTION) return false;
        MainWindow.getTabs().get(input.getSelectedItem()).importAction.actionPerformed(null);
        return true;
    }

    private int getConfirmation() {
        return JOptionPane.showConfirmDialog(
            body,
            "Proceed to file selection for import of %ss?".formatted(input.getSelectedItem().toString().toLowerCase()),
            "Confirm",
            JOptionPane.YES_NO_OPTION
        );
    }
}
