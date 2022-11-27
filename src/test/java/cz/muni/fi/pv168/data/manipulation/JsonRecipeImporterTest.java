package cz.muni.fi.pv168.data.manipulation;

import cz.muni.fi.pv168.model.*;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public final class JsonRecipeImporterTest extends AbstractJsonImporterTest<Recipe> {
    public JsonRecipeImporterTest() {
        super(Recipe.class);
    }

    @Test
    void wrongDifferentFormat(){
        super.wrongDifferentFormat("multi-units.json");
    }

    @Test
    void singleRecipe() {
        Path importFilePath = TEST_RESOURCES.resolve("single-recipe.json");
        Collection<Recipe> recipes = importer.loadEntities(importFilePath.toString(), Recipe.class);
        List<RecipeIngredient> ingredientList = new ArrayList<>();
        ingredientList.add(new RecipeIngredient(new Ingredient("Cukr", 200.0, new Unit("pc(s)", 1.0, null)),
                5.0,
                new Unit("pc(s)", 1.0, null))
        );
        ingredientList.add(new RecipeIngredient(new Ingredient("Soda", 1.0, new Unit("g", 1.0, null)),
                4.0,
                new Unit("g", 1.0, null))
        );

        assertThat(recipes).containsExactly(
                new Recipe("Dřevo s chlebem",
                        "Velmi chutné jídlo.",
                        "Do vody přidáme dřevo a pak chleba.",
                        new Category("Dřevěné jídla","FF996600"),
                        15,
                        3,
                        ingredientList
                        )
        );
    }

    @Test
    void multipleRecipes() {
        Path importFilePath = TEST_RESOURCES.resolve("multi-recipes.json");
        Collection<Recipe> recipes = importer.loadEntities(importFilePath.toString(), Recipe.class);
        List<RecipeIngredient> ingredientList1 = new ArrayList<>();

        ingredientList1.add(new RecipeIngredient(new Ingredient("Cukr", 200.0, new Unit("pc(s)", 1.0, null)),
                5.0,
                new Unit("pc(s)", 1.0, null))
        );
        ingredientList1.add(new RecipeIngredient(new Ingredient("Soda", 1.0, new Unit("g", 1.0, null)),
                4.0,
                new Unit("asdasdasd", 5.0, BaseUnitsEnum.GRAM))
        );


        List<RecipeIngredient> ingredientList2 = new ArrayList<>();
        ingredientList2.add(new RecipeIngredient(new Ingredient("Mléko", 0.5555555555555556, new Unit("ml", 1.0, null)),
                5.0,
                new Unit("ml", 1.0, null))
        );
        ingredientList2.add(new RecipeIngredient(new Ingredient("Soda", 1.0, new Unit("g", 1.0, null)),
                8.0,
                new Unit("g", 1.0, null))
        );
        ingredientList2.add(new RecipeIngredient(new Ingredient("Cukr", 200.0, new Unit("pc(s)", 1.0, null)),
                65.0,
                new Unit("pc(s)", 1.0, null))
        );

        assertThat(recipes).containsExactly(
                new Recipe("Dřevo s chlebem",
                        "Velmi chutné jídlo.",
                        "Do vody přidáme dřevo a pak chleba.",
                        new Category("Dřevěné jídla","FF996600"),
                        15,
                        3,
                        ingredientList1
                ),
                new Recipe("Železo v troubě",
                        "Velice chutné železo, které je zdravé.",
                        "Ohřejeme troubu na 250 stupňů a dáme železo na horní patro a pečeme než se spálí.",
                        new Category("Železná jídla", "FF666666"),
                        30,
                        269,
                        ingredientList2)

        );
    }

    @Test
    void uncategorizedRecipe() {
        Path importFilePath = TEST_RESOURCES.resolve("uncategorized-recipe.json");
        Collection<Recipe> recipes = importer.loadEntities(importFilePath.toString(), Recipe.class);

        var recipe = new Recipe("Dřevo s chlebem",
                "Velmi chutné jídlo.",
                "Do vody přidáme dřevo a pak chleba.",
                Category.UNCATEGORIZED,
                15,
                3,
                List.of(new RecipeIngredient(
                    new Ingredient("Cukr", 200.0, new Unit("pc(s)", 1.0, null)),
                    5.0,
                    new Unit("pc(s)", 1.0, null))
                )
        );

        assertThat(recipes).containsExactly(recipe);
    }
}
