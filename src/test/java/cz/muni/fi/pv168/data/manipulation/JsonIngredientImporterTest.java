package cz.muni.fi.pv168.data.manipulation;

import cz.muni.fi.pv168.model.Category;
import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.Unit;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonIngredientImporterTest extends AbstractJsonImporterTest<Ingredient> {
    public JsonIngredientImporterTest() {
        super(Ingredient.class);
    }

    @Test
    void wrongDifferentFormat(){
        super.wrongDifferentFormat("multi-categories.json");
    }

    @Test
    void singleIngredient() {
        Path importFilePath = TEST_RESOURCES.resolve("single-ingredient.json");
        Collection<Ingredient> ingredients = importer.loadEntities(importFilePath.toString(), Ingredient.class);

        assertThat(ingredients).containsExactly(
                new Ingredient("Soda", 1.0, new Unit("g", 1.0, null))
        );
    }

    @Test
    void multipleIngredients() {
        Path importFilePath = TEST_RESOURCES.resolve("multi-ingredients.json");
        Collection<Ingredient> ingredients = importer.loadEntities(importFilePath.toString(), Ingredient.class);

        assertThat(ingredients).containsExactly(
                new Ingredient("Soda", 1.0, new Unit("g", 1.0, null)),
                new Ingredient("Cukr", 200.0, new Unit("pc(s)", 1.0, null)),
                new Ingredient("Mléko", 0.5555555555555556, new Unit("ml", 1.0, null))
        );
    }
}
