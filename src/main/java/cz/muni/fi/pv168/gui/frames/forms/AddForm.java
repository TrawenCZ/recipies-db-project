package cz.muni.fi.pv168.gui.frames.forms;

import javax.swing.*;
import java.awt.*;

public interface AddForm {

    void addComponent(Container panel, JComponent label, int gridx, int gridy);

    void addComponent(Container panel, JComponent label, int gridx, int gridy, int position);
}
