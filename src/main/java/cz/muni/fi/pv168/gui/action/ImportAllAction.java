package cz.muni.fi.pv168.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import cz.muni.fi.pv168.gui.frames.forms.ImportForm;
import cz.muni.fi.pv168.gui.resources.Icons;

public class ImportAllAction extends AbstractAction {

    public ImportAllAction() {
        super("Import", Icons.IMPORT_S);
        putValue(SHORT_DESCRIPTION, "Import into table of choice");
        putValue(MNEMONIC_KEY, KeyEvent.VK_I);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new ImportForm();
    }
}
