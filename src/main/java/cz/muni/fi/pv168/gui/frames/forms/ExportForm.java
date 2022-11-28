package cz.muni.fi.pv168.gui.frames.forms;

import cz.muni.fi.pv168.Config;
import cz.muni.fi.pv168.gui.frames.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.util.stream.Stream;

/**
 * Form that lets the user select from any available export.
 *
 * @author Jan Martinek
 */
public final class ExportForm extends AbstractForm {

    private JComboBox<String> input = new JComboBox<>(
        Stream.of(MainWindow.getTabs()).map(e -> e.toString()).toArray(String[]::new)
    );

    public ExportForm() {
        super("Export", "Select table");
        initializeBody();
        show();
    }

    @Override
    protected void initializeBody() {
        input.setToolTipText("Select the table from which the export is done");

        gridExtensions(GridBagConstraints.HORIZONTAL, 0, 5);

        gridInsets(10);
        gridAdd(input, 0, 1, 2, 1);

        input.setSelectedItem(Config.THEME);
    }

    @Override
    protected boolean onAction() {
        if (getConfirmation() != JOptionPane.YES_OPTION) return false;
        MainWindow.getTabs()[input.getSelectedIndex()].exportAction.actionPerformed(null);
        return true;
    }

    private int getConfirmation() {
        return JOptionPane.showConfirmDialog(
            body,
            "Export all %ss?".formatted(input.getSelectedItem().toString().toLowerCase()),
            "Confirm",
            JOptionPane.YES_NO_OPTION
        );
    }
}
