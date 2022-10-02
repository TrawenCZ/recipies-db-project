package cz.muni.fi.pv168.gui.frames.forms;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractForm implements AddForm {
    private final JDialog dialog;
    private final String title;
    private GridBagConstraints constraints;

    protected AbstractForm(String title) {
        constraints = new GridBagConstraints();
        this.title = title;
        dialog = new JDialog(new JFrame(), title, true);
        dialog.setTitle(getTitle());
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
    }

    @Override
    public void addComponent(Container panel, JComponent label, int gridx, int gridy) {
        addComponent(panel, label, gridx, gridy, GridBagConstraints.WEST);
    }

    @Override
    public void addComponent(Container panel, JComponent label, int gridx, int gridy, int position) {
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.anchor = position;
        panel.add(label, constraints);
    }

    public JDialog getDialog() {
        return dialog;
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

    /**
     * Creates a pop-up dialog window, in case to
     * @param message content of the dialog window.
     * @param header title of the dialog window.
     * @param type static constants defined in the JOptionPane class:
     *             WARNING_MESSAGE,
     *             ERROR_MESSAGE,
     *             INFORMATION_MESSAGE
     */
    public void popUpDialog(String message, String header, int type) {
        JOptionPane.showMessageDialog(getDialog(), message, header, type);
    }
}
