package cz.muni.fi.pv168.data.generators;

import cz.muni.fi.pv168.gui.coloring.DisplayableColor;
import cz.muni.fi.pv168.model.Category;

import java.util.Collections;
import java.util.List;

public class CategoryDataGenerator extends AbstractDataGenerator<Category> {

    private static final List<Category> CATEGORIES = List.of(
        new Category("Dezert", new DisplayableColor(0xFF5555)),
        new Category("Staročeská kuchyně", new DisplayableColor(0xEEEE11)),
        new Category("Světová kuchyně", new DisplayableColor(0xAA55AA)),
        new Category("Salát", new DisplayableColor(0x88FF88)),
        new Category("Těstoviny", new DisplayableColor(0x00CC66)),
        new Category("Black sample", new DisplayableColor(0x000000)),
        new Category("White sample", new DisplayableColor(0xFFFFFF))
    );

    public Category createTestEntity() {
        return selectRandom(CATEGORIES);
    }

    public static List<Category> getAll() {
        return Collections.unmodifiableList(CATEGORIES);
    }

}
