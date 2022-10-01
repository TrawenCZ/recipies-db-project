package cz.muni.fi.pv168.gui.frames.forms;

import javax.swing.*;

public interface AddForm {

    void addComponent(JPanel panel, JComponent label, int gridx, int gridy);

    void addComponent(JPanel panel, JComponent label, int gridx, int gridy, int position);
}
