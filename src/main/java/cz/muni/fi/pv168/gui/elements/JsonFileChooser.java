package cz.muni.fi.pv168.gui.elements;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * File chooser that shows only directories and json files.
 *
 * @author Jan Martinek
 */
public class JsonFileChooser extends JFileChooser {

    /**
     * {@link JsonFileChooser#JsonFileChooser(boolean)}
     * <p> With multi-selection disabled by default.
     */
    public JsonFileChooser() {
        this(false);
    }

    /**
     * Shows only directories AND json files.
     *
     * @param hasMultiSelection sets multi-selection, true for enabled
     */
    public JsonFileChooser(boolean hasMultiSelection) {
        this.setMultiSelectionEnabled(hasMultiSelection);
        this.setAcceptAllFileFilterUsed(false);
        this.setFileHidingEnabled(true);
        this.setFileFilter(new FileNameExtensionFilter("JSON files", "json"));
    }

    /**
     * {@link JsonFileChooser#JsonFileChooser(boolean)}
     * <p> Sets the directory (current user directory if true, else default
     * user directory), if possible.
     *
     * @param hasMultiSelection sets multi-selection, true for enabled
     * @param atUserDir         true if current dir, false if default
     */
    public JsonFileChooser(boolean hasMultiSelection, boolean atUserDir) {
        this(hasMultiSelection);
        File directory = null;
        if (atUserDir) directory = new File(System.getProperty("user.dir"));
        this.setCurrentDirectory(directory);
    }

    /**
     * Returns the currently selected file as a path ending with ".json"
     *
     * @return .json ended path string
     */
    public String getJsonPath() {
        String path = this.getSelectedFile().getAbsolutePath();
        if (!path.endsWith(".json")) path += ".json";
        return path;
    }
}
