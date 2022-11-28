package cz.muni.fi.pv168;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Configuration class, contains all changeable configuration
 * for the application.
 *
 * @author Jan Martinek
 */
public class Config {

    private static final Logger LOGGER = Main.logger;
    private static final String LIGHT_DEFAULT = "light";
    private static final String LIGHT_INTELLI = "intellij";
    private static final String DARK_DEFAULT = "dark";
    private static final String DARK_DARCULA = "darcula";

    public static final String[] THEMES = {
        LIGHT_DEFAULT,
        LIGHT_INTELLI,
        DARK_DEFAULT,
        DARK_DARCULA,
    };
    public static final int MIN_OPACITY = 10;
    public static final int MAX_OPACITY = 80;

    public static final Font FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);

    // configurable
    public static String THEME = LIGHT_DEFAULT;
    public static int OPACITY = 50;


    private Config() {
        // intentionally private
    }

    public static void load() {
        try (var reader = new BufferedReader(new FileReader(getConfigPath()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] items = line.split("=", 2);
                if (items.length > 0) {
                    if (items.length != 2) {
                        LOGGER.warning("Invalid line: %s".formatted(line));
                        continue;
                    }
                    setOption(items[0], items[1]);
                }
            }
        } catch (IOException e) {
            LOGGER.warning("Error occurred while reading config file: %s".formatted(e.getMessage()));
        }
    }

    public static boolean save() {
        try (var writer = new BufferedWriter(new FileWriter(getConfigPath(), false))) {
            writer.write("THEME=%s".formatted(Config.THEME));
            writer.newLine();
            writer.write("OPACITY=%s".formatted(Config.OPACITY));
            writer.newLine();
        } catch (IOException e) {
            LOGGER.severe("Error occurred while writing to the config file: %s".formatted(e.getMessage()));
            return false;
        }
        return true;
    }

    public static void setOption(String name, String value) {
        switch (name) {
            case "THEME": THEME = resolveTheme(value); break;
            case "OPACITY": OPACITY = resolveOpacity(value); break;
            default: LOGGER.warning("Unknown option: %s=%s".formatted(name, value)); return;
        }
        LOGGER.config("set %s=%s".formatted(name, value));
    }

    private static int resolveOpacity(String opacity) {
        try {
            int value = Integer.parseInt(opacity);
            if (value < 10 || value > 80) {
                throw new NumberFormatException();
            }
            return value;
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid value: %s".formatted(opacity));
        }
        return OPACITY;
    }

    private static String resolveTheme(String theme) {
        for (var t : THEMES) {
            if (t.equals(theme)) {
                return t;
            }
        }
        LOGGER.warning("Unknown theme: %s".formatted(theme));
        return THEME;
    }

    private static String getConfigPath() {
        URL url = Main.class.getResource("config");
        if (url == null) {
            LOGGER.warning("Config file could not be located, DEFAULTS used instead");
            return null;
        }
        return url.getPath();
    }

}
