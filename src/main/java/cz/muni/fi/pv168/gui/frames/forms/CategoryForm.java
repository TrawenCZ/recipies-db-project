package cz.muni.fi.pv168.gui.frames.forms;

import java.awt.Color;
import java.awt.GridBagConstraints;

import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import cz.muni.fi.pv168.gui.frames.MainWindow;
import cz.muni.fi.pv168.model.Category;

public class CategoryForm extends AbstractForm {

    private final JLabel nameLabel = new JLabel("Enter name (*)");
    private final JLabel colorLabel = new JLabel("Select any color");

    private final JTextField nameInput = new JTextField(12);
    private final JColorChooser colorInput = new JColorChooser(Color.WHITE);
    private Category category;

    private CategoryForm(String title, String header) {
        super(title, header);
        initializeBody();
    }

    public CategoryForm(Category category) {
        this(EDIT, "Edit category");
        this.category = category;
        addSampleData();
        show();
    }

    public CategoryForm() {
        this("Add", "New category");
        show();
    }

    @Override
    protected void initializeBody() {
        nameInput.setToolTipText(NAME_TOOLTIP + "CATEGORIES!");

        gridExtensions(GridBagConstraints.HORIZONTAL, 0, 5);

        gridInsets(10);
        gridAdd(nameLabel, 0, 0);
        gridAdd(colorLabel, 0, 2);
        gridAdd(colorInput, 0, 4);

        gridInsets(-20, 10, 10, 10);
        gridAdd(nameInput, 0, 1);
        gridAdd(new JSeparator(SwingConstants.HORIZONTAL), 0, 3);
    }

    @Override
    protected boolean onAction() {
        var tableModel = MainWindow.getCategoryModel();
        if (!verifyName(tableModel, category, nameInput.getText())) {
            return false;
        }
        if (nameInput.getText().equalsIgnoreCase("uncategorized")) {
            showErrorDialog(
                    "Name cannot be '" + nameInput.getText() + "' because it's reserved for recipes without Category.",
                    "Invalid name"
            );
            return false;
        }

        var newColor = new Color(colorInput.getColor().getRGB());

        if (isEdit()) {
            category.setName(nameInput.getText());
            category.setColor(newColor);
            tableModel.updateRow(category);
        } else {
            tableModel.addRow(new Category(nameInput.getText(), newColor));
        }

        return true;
    }

    private void addSampleData() {
        nameInput.setText(category.getName());
        colorInput.setColor(category.getColor());
    }
}
