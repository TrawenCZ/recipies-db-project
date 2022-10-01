package cz.muni.fi.pv168.data;

import cz.muni.fi.pv168.model.Category;

import java.util.Collections;
import java.util.List;

public class CategoryDataGenerator extends AbstractDataGenerator<Category> {
    
    private static final List<Category> CATEGORIES = List.of(
        new Category("Dezert"),
        new Category("Staročeská kuchyně"),
        new Category("Světová kuchyně"),
        new Category("Salát"),
        new Category("Těstoviny"),
        new Category("Pro domácí mazlíčky")
    );

    public Category createTestEntity() {
        return selectRandom(CATEGORIES);
    }

    public static List<Category> getAll() {
        return Collections.unmodifiableList(CATEGORIES);
    }

}
