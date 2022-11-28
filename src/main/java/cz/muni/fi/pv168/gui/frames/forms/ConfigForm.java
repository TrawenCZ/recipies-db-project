package cz.muni.fi.pv168.gui.frames.forms;

import cz.muni.fi.pv168.Config;

import javax.swing.*;
import java.awt.*;

/**
 * Form that is used for the customization of the application.
 *
 * @author Jan Martinek
 */
public final class ConfigForm extends AbstractForm {

    private JLabel themesLabel = new JLabel("Theme");
    private JLabel opacityLabel = new JLabel("Table color opacity");

    private JComboBox<String> themesInput = new JComboBox<>(Config.THEMES);
    private JSlider opacityInput = new JSlider(Config.MIN_OPACITY, Config.MAX_OPACITY);

    public ConfigForm() {
        super("Settings", "Configurable options");
        initializeBody();
        show();
    }

    @Override
    protected void initializeBody() {
        themesInput.setToolTipText("Changes the color theme of the application. Requires restart.");
        opacityInput.setToolTipText("Changes opacity of table rows that have custom color.");

        gridExtensions(GridBagConstraints.HORIZONTAL, 0, 5);

        gridInsets(10);
        gridAdd(themesLabel, 0, 0);
        gridAdd(opacityLabel, 0, 2);

        gridInsets(-20, 10, 10, 10);
        gridAdd(themesInput, 0, 1, 2, 1);
        gridAdd(opacityInput, 0, 3, 2, 1);

        themesInput.setSelectedItem(Config.THEME);
        opacityInput.setValue(Config.OPACITY);
    }

    @Override
    protected boolean onAction() {
        var theme = (String) themesInput.getSelectedItem();
        var opacity = opacityInput.getValue();

        if (!Config.THEME.equals(theme) || !(Config.OPACITY == opacity)) {
            if (getConfirmation() == JOptionPane.YES_OPTION) {
                Config.THEME = theme;
                Config.OPACITY = opacity;
                if (!Config.save()) {
                    showSaveError();
                    return false;
                }
                System.exit(0);
            }
        }
        return true;
    }

    private int getConfirmation() {
        return JOptionPane.showConfirmDialog(
            body,
            "Changing settings requires the restart of application. Do you want to exit the application and apply the changes?",
            "Confirm changes",
            JOptionPane.YES_NO_OPTION
        );
    }

    private void showSaveError() {
        JOptionPane.showMessageDialog(
            body,
            "Settings could not be saved, please verify that the application can write to its directory.",
            "Save error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
