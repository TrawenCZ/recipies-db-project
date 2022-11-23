package cz.muni.fi.pv168.gui.frames.forms;

import cz.muni.fi.pv168.gui.TextValidator;
import cz.muni.fi.pv168.gui.Validator;
import cz.muni.fi.pv168.gui.frames.MainWindow;
import cz.muni.fi.pv168.gui.models.AbstractModel;
import cz.muni.fi.pv168.model.BaseUnitsEnum;
import cz.muni.fi.pv168.model.Nameable;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Class that makes form design easier.
 * <p>
 * Provides two renameable buttons at the bottom by default which can be
 * turned off. Both buttons dispose of the form by default, with 1st
 * providing overridable function {@link AbstractForm#onAction()}.
 * </p>
 * Provides: gridAdd, gridExtensions, gridInsets
 */
public abstract class AbstractForm {

    protected static final Dimension BUTTON_SIZE = new Dimension(100, 36);
    protected static final String NAME_TOOLTIP = "Name must be UNIQUE in ";
    protected static final String EDIT = "Edit";

    // window
    private final JDialog dialog;

    // accessible for customization
    protected final JPanel body = new JPanel(new GridBagLayout());

    private final GridBagConstraints c = new GridBagConstraints();
    private final JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    /**
     * Constructs the basic layout of form missing body, i.e. only header and
     * footer are both created and placed, with body beign empty.
     *
     * @param title         name of the window
     * @param header        name of the form (i.e. New recipe ...)
     * @param actionButton  name of the 1st button, button not added if null
     * @param closeButton   name of the 2nd button, button not added if null
     */
    protected AbstractForm(String title, String header, String actionButton, String closeButton) {
        if (title == null) throw new NullPointerException("title cannot be null");

        dialog = new JDialog(new JFrame(), title, true);

        dialog.setTitle(title);
        dialog.setResizable(false);

        initializeHeader(header);
        initializeFooter(actionButton, closeButton);

        dialog.setLayout(new BorderLayout());
        dialog.add(body, BorderLayout.CENTER);
        dialog.add(footer, BorderLayout.PAGE_END);
    }

    /**
     * Construct the basic layout of the form with default named
     * buttons ("Save", "Close").
     *
     * @param title         name of the window
     * @param header        name of the form (i.e. New recipe ...)
     *
     * @see AbstractForm#AbstractForm(String, String, String, String)
     */
    protected AbstractForm(String title, String header) {
        this(title, header, "OK", "Cancel");
    }

    /**
     * Fills the body of the form with contentent. Expected use is once
     * in constructor of the derived class. Use of methods starting with
     * 'grid' is advised.
     *
     * @implNote {@link AbstractForm#body} is accessible
     */
    protected abstract void initializeBody();

    /**
     * Optional method, implement if you are using the action button
     * and want to make it do something aside from form disposal.
     */
    protected abstract boolean onAction();

    protected boolean verifyName(AbstractModel<?> model, Nameable item, String input) {
        if (TextValidator.empty(input)) {
            showErrorDialog("Name cannot be empty!", "Missing name");
            return false;
        }
        if (!Validator.isUnique(model, input) && !(isEdit() && item.getName().equals(input))) {
            showErrorDialog("Name must be unique!", "Duplicate name");
            return false;
        }
        return true;
    }

    protected boolean isEdit() {
        return "Edit".equals(dialog.getTitle());
    }

    protected void refreshContent() {
        var content = dialog.getContentPane();
        content.validate();
        content.repaint();
    }

    protected void show() {
        pack();
        dialog.setVisible(true);
    }

    protected void pack() {
        dialog.pack();
        dialog.setSize(dialog.getWidth() + 100, dialog.getHeight() + 50);
        dialog.setLocationRelativeTo(MainWindow.getGlassPane());
    }

    protected void gridAdd(Component component, int x, int y) {
        gridAdd(component, x, y, GridBagConstraints.CENTER);
    }

    protected void gridAdd(Component component, int x, int y, int anchor) {
        gridAdd(component, x, y, 1, 1, anchor);
    }

    protected void gridAdd(Component component, int x, int y, int width, int height) {
        gridAdd(component, x, y, width, height, GridBagConstraints.CENTER);
    }

    protected void gridAdd(Component component, int x, int y, double weightx, double weighty) {
        gridAdd(component, x, y, 1, 1, weightx, weighty, GridBagConstraints.CENTER);
    }

    protected void gridAdd(Component component, int x, int y, int width, int height, int anchor) {
        gridAdd(component, x, y, width, height, 1.0d, 1.0d, anchor);
    }

    protected void gridAdd(Component component, int x, int y, int width, int height, double weightx, double weighty, int anchor) {
        c.anchor = anchor;
        c.weightx = weightx;
        c.weighty = weighty;
        c.gridwidth = width;
        c.gridheight = height;
        c.gridx = x;
        c.gridy = y;
        body.add(component, c);
    }

    /**
     * Specifies size extension parameters
     *
     * @param fill GridBagConstraints.VERTICAL/HORIZONTAL/BOTH
     * @param padx how much to add to each component's x size, default before setting = 0
     * @param pady how much to add to each component's y size, default before setting = 0
     */
    protected void gridExtensions(int fill, int padx, int pady) {
        c.fill = fill;
        c.ipadx = padx;
        c.ipady = pady;
    }

    /**
     * Specifies the margins on all sides of the components
     *
     * @param all any int, sets all of TOP,LEFT,BOTTOM,RIGHT
     */
    protected void gridInsets(int all) {
        gridInsets(all, all, all, all);
    }

    /**
     * Specifies the margins between each component
     *
     * @param top       any int, default 0
     * @param left      any int, default 0
     * @param bottom    any int, default 0
     * @param right     any int, default 0
     */
    protected void gridInsets(int top, int left, int bottom, int right) {
        c.insets = new Insets(top, left, bottom, right);
    }

    /**
     * Creates a pop-up dialog window, used for notifications/errors.
     *
     * @param message content of the dialog window.
     * @param header  title of the dialog window.
     * @param type    static constants defined in the JOptionPane class:
     *                WARNING_MESSAGE,
     *                ERROR_MESSAGE,
     *                INFORMATION_MESSAGE
     */
    protected void popUpDialog(String message, String header, int type) {
        JOptionPane.showMessageDialog(dialog, message, header, type);
    }

    private void initializeHeader(String title) {
        body.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title));

        // set font;
        TitledBorder border = ((TitledBorder) body.getBorder());
        border.setTitleFont(new Font(border.getTitleFont().getName(), Font.BOLD, 22));
    }
    protected void showErrorDialog(String message, String title) {
        JOptionPane.showMessageDialog(dialog, message, title, JOptionPane.ERROR_MESSAGE);
    }

    protected BaseUnitsEnum baseUnitFromString(String baseUnitString) {
        return switch (baseUnitString) {
            case "ml" -> BaseUnitsEnum.MILLILITER;
            case "pc(s)" -> BaseUnitsEnum.PIECE;
            default -> BaseUnitsEnum.GRAM;
        };
    }

    private void initializeFooter(String action, String close) {
        if (action != null) {
            JButton actionButton = new JButton(action);
            actionButton.setPreferredSize(BUTTON_SIZE);
            actionButton.addActionListener(e -> {
                if (!onAction()) return;
                dialog.dispose();
            });
            footer.add(actionButton);
        }
        if (close != null) {
            JButton closeButton = new JButton(close);
            closeButton.setPreferredSize(BUTTON_SIZE);
            closeButton.addActionListener(e -> dialog.dispose());
            footer.add(closeButton);
        }
    }
}
