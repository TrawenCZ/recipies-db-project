package cz.muni.fi.pv168.data.manipulation;

import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.Unit;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

final class JsonIngredientExportTest extends AbstractJsonExporterTest<Ingredient> {
    @Test
    void oneIngredient() throws IOException {
        var ingredient = new Ingredient("Soda", 1.0, new Unit("g", 1.0, null));
        testDirSave(List.of(ingredient));
        assertExportedContent("""
                [ {
                  "name" : "Soda",
                  "kcal" : 1.0,
                  "unit" : {
                    "name" : "g",
                    "valueInBaseUnit" : 1.0,
                    "baseUnit" : null
                  }
                } ]
                """);
    }
    @Test
    void multipleCategories() throws  IOException {
        var ingredients = List.of(
                new Ingredient("Soda", 1.0, new Unit("g", 1.0, null)),
                new Ingredient("Cukr", 200.0, new Unit("pc(s)", 1.0, null)),
                new Ingredient("Mléko", 0.5555555555555556, new Unit("ml", 1.0, null))
        );
        testDirSave(ingredients);
        assertExportedContent("""
                [ {
                  "name" : "Soda",
                  "kcal" : 1.0,
                  "unit" : {
                    "name" : "g",
                    "valueInBaseUnit" : 1.0,
                    "baseUnit" : null
                  }
                }, {
                  "name" : "Cukr",
                  "kcal" : 200.0,
                  "unit" : {
                    "name" : "pc(s)",
                    "valueInBaseUnit" : 1.0,
                    "baseUnit" : null
                  }
                }, {
                  "name" : "Mléko",
                  "kcal" : 0.5555555555555556,
                  "unit" : {
                    "name" : "ml",
                    "valueInBaseUnit" : 1.0,
                    "baseUnit" : null
                  }
                } ]
                """);
    }
}
