package cz.muni.fi.pv168.data.manipulation;

import cz.muni.fi.pv168.model.Category;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

final class JsonCategoryExporterTest extends AbstractJsonExporterTest<Category> {
    @Test
    void oneCategory() throws IOException {
        var category = new Category("Dřevěné jídla","FF996600");
        testDirSave(List.of(category));
        assertExportedContent("""
                [ {
                  "name" : "Dřevěné jídla",
                  "color" : "FF996600"
                } ]
                """);
    }
    @Test
    void multipleCategories() throws  IOException {
        var categories = List.of(
                new Category("Dřevěné jídla","FF996600"),
                new Category("Železná jídla", "FF666666"),
                new Category("Vodové jídla", "FF00CCFF")
        );
        testDirSave(categories);
        assertExportedContent("""
                [ {
                  "name" : "Dřevěné jídla",
                  "color" : "FF996600"
                }, {
                  "name" : "Železná jídla",
                  "color" : "FF666666"
                }, {
                  "name" : "Vodové jídla",
                  "color" : "FF00CCFF"
                } ]
                """);
    }
}
