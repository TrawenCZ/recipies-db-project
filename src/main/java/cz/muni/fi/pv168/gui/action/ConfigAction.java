package cz.muni.fi.pv168.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import cz.muni.fi.pv168.gui.frames.forms.ConfigForm;
import cz.muni.fi.pv168.gui.resources.Icons;

public class ConfigAction extends AbstractAction {

    public ConfigAction() {
        super("Settings", Icons.SETTINGS_S);
        putValue(SHORT_DESCRIPTION, "Configures the application");
        putValue(MNEMONIC_KEY, KeyEvent.VK_S);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl S"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new ConfigForm();
    }
}
