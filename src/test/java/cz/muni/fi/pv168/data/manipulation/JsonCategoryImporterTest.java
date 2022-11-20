package cz.muni.fi.pv168.data.manipulation;

import cz.muni.fi.pv168.model.Category;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public final class JsonCategoryImporterTest extends AbstractJsonImporterTest<Category> {
    public JsonCategoryImporterTest() {
        super(Category.class);
    }

    @Test
    void wrongDifferentFormat(){
        super.wrongDifferentFormat("multi-units.json");
    }

    @Test
    void singleCategory() {
        Path importFilePath = TEST_RESOURCES.resolve("single-category.json");
        Collection<Category> categories = importer.loadEntities(importFilePath.toString(), Category.class);

        assertThat(categories).containsExactly(
                new Category("Dřevěné jídla","FF996600")
        );
    }

    @Test
    void multipleCategories() {
        Path importFilePath = TEST_RESOURCES.resolve("multi-categories.json");
        Collection<Category> categories = importer.loadEntities(importFilePath.toString(), Category.class);

        assertThat(categories).containsExactly(
                new Category("Dřevěné jídla","FF996600"),
                new Category("Železná jídla", "FF666666"),
                new Category("Vodové jídla", "FF00CCFF")
        );
    }
}
