package cz.muni.fi.pv168.gui.frames.forms;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractForm implements AddForm {
    private final JFrame frame;
    private final String title;
    private GridBagConstraints constraints;

    protected AbstractForm(String title) {
        this.frame = new JFrame();
        this.title = title;
        constraints = new GridBagConstraints();
        frame.setTitle(getTitle());
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    @Override
    public void addComponent(JPanel panel, JComponent label, int gridx, int gridy) {
        addComponent(panel, label, gridx, gridy, GridBagConstraints.WEST);
    }

    @Override
    public void addComponent(JPanel panel, JComponent label, int gridx, int gridy, int position) {
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.anchor = position;
        panel.add(label, constraints);
    }

    public JFrame getFrame() {
        return frame;
    }

    public String getTitle() {
        return title;
    }

    public GridBagConstraints getConstraints() {
        return constraints;
    }

    public void setConstraints(GridBagConstraints constraints) {
        this.constraints = constraints;
    }
}
