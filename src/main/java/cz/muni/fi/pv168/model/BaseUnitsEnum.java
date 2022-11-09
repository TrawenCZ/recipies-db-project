package cz.muni.fi.pv168.model;

import java.util.Arrays;

/**
 * @author Radim Stejskal
 */
public enum BaseUnitsEnum {

    GRAM("g"),
    MILLILITER("ml"),
    PIECE("pc(s)");

    private final String value;

    BaseUnitsEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String[] getAllValues() {
        return Arrays.stream(BaseUnitsEnum.values()).map(BaseUnitsEnum::getValue).toArray(String[]::new);
    }
}
