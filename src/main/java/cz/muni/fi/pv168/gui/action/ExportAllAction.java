package cz.muni.fi.pv168.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import cz.muni.fi.pv168.gui.frames.forms.ExportForm;
import cz.muni.fi.pv168.gui.resources.Icons;

public class ExportAllAction extends AbstractAction {

    public ExportAllAction() {
        super("Export", Icons.EXPORT_S);
        putValue(SHORT_DESCRIPTION, "Export from table of choice");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new ExportForm();
    }
}
