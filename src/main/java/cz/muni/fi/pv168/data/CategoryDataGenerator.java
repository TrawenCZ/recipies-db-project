package cz.muni.fi.pv168.data;

import cz.muni.fi.pv168.model.Category;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

public class CategoryDataGenerator extends AbstractDataGenerator<Category> {
    
    private static final List<Category> CATEGORIES = List.of(
        new Category("Dezert", new Color(0xFF5555)),
        new Category("Staročeská kuchyně", new Color(0xEEEE11)),
        new Category("Světová kuchyně", new Color(0xAA55AA)),
        new Category("Salát", new Color(0x88FF88)),
        new Category("Těstoviny", new Color(0xCCCC66)),
        new Category("Pro domácí mazlíčky", new Color(0x7788CC))
    );

    public Category createTestEntity() {
        return selectRandom(CATEGORIES);
    }

    public static List<Category> getAll() {
        return Collections.unmodifiableList(CATEGORIES);
    }

}
