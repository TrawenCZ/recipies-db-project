package cz.muni.fi.pv168.data.manipulation;

import cz.muni.fi.pv168.model.*;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public final class JsonRecipeImporterTest extends AbstractJsonImporterTest<Recipe> {

    private final Unit u_g = new Unit("g", 1.0, null);
    private final Unit u_pcs = new Unit("pc(s)", 1.0, null);
    private final Unit u_ml = new Unit("ml", 1.0, null);

    public Importer getImporter() {
        return dependencyProvider.getRecipeImporter();
    }

    @Test
    void wrongDifferentFormat(){
        super.wrongDifferentFormat("multi-units.json");
    }

    @Test
    void singleRecipe() {
        Path importFilePath = TEST_RESOURCES.resolve("single-recipe.json");

        Category c_drevo = new Category("Dřevěné jídla","FF996600");

        Ingredient i_cukr = new Ingredient("Cukr", 200.0, u_pcs);
        Ingredient i_soda = new Ingredient("Soda", 1.0, u_g);

        Recipe r1 = new Recipe(
            "Dřevo s chlebem",
            "Velmi chutné jídlo.",
            "Do vody přidáme dřevo a pak chleba.",
            c_drevo,
            15,
            3,
            List.of(
                new RecipeIngredient(i_cukr, 5.0, u_pcs),
                new RecipeIngredient(i_soda, 4.0, u_g)
            )
        );

        var truthCategory = new ArrayList<>(dependencyProvider.getCategoryRepository().findAll());
        truthCategory.add(c_drevo);

        var truthUnit = new ArrayList<>(dependencyProvider.getUnitRepository().findAll());

        var truthIngredient = new ArrayList<>(dependencyProvider.getIngredientRepository().findAll());
        truthIngredient.add(i_cukr);
        truthIngredient.add(i_soda);

        var truth = new ArrayList<>(dependencyProvider.getRecipeRepository().findAll());
        truth.add(r1);

        importer.importData(importFilePath.toString());
        var progress = importer.getProgress();

        assertThat(progress.isDone()).isTrue();
        assertThat(progress.getInsert()).isEqualTo(4);
        assertThat(progress.getUpdate()).isEqualTo(0);
        assertThat(progress.getIgnore()).isEqualTo(0);
        assertThat(progress.getRemove()).isEqualTo(0);

        refreshRepositories();
        assertThat(dependencyProvider.getCategoryRepository().findAll()).containsExactly(truthCategory.toArray(new Category[0]));
        assertThat(dependencyProvider.getUnitRepository().findAll()).containsExactly(truthUnit.toArray(new Unit[0]));
        assertThat(dependencyProvider.getIngredientRepository().findAll()).containsExactly(truthIngredient.toArray(new Ingredient[0]));
        assertThat(dependencyProvider.getRecipeRepository().findAll()).containsExactly(truth.toArray(new Recipe[0]));
    }

    @Test
    void multipleRecipes() {
        Path importFilePath = TEST_RESOURCES.resolve("multi-recipes.json");

        Category c_drevo = new Category("Dřevěné jídla","FF996600");
        Category c_zelezo = new Category("Železná jídla", "FF666666");

        Unit u_asd = new Unit("asdasdasd", 5.0, BaseUnitsEnum.GRAM);

        Ingredient i_cukr = new Ingredient("Cukr", 200.0, u_pcs);
        Ingredient i_soda = new Ingredient("Soda", 1.0, u_g);
        Ingredient i_milk = new Ingredient("Mléko", 0.5555555555555556, u_ml);

        Recipe r1 = new Recipe(
            "Dřevo s chlebem",
            "Velmi chutné jídlo.",
            "Do vody přidáme dřevo a pak chleba.",
            c_drevo,
            15,
            3,
            List.of(
                new RecipeIngredient(i_cukr, 5.0, u_pcs),
                new RecipeIngredient(i_soda, 4.0, u_asd)
            )
        );

        Recipe r2 = new Recipe(
            "Železo v troubě",
            "Velice chutné železo, které je zdravé.",
            "Ohřejeme troubu na 250 stupňů a dáme železo na horní patro a pečeme než se spálí.",
            c_zelezo,
            30,
            269,
            List.of(
                new RecipeIngredient(i_milk, 5.0, u_ml)
            )
        );

        var truthCategory = new ArrayList<>(dependencyProvider.getCategoryRepository().findAll());
        truthCategory.add(c_drevo);
        truthCategory.add(c_zelezo);

        var truthUnit = new ArrayList<>(dependencyProvider.getUnitRepository().findAll());
        truthUnit.add(u_asd);

        var truthIngredient = new ArrayList<>(dependencyProvider.getIngredientRepository().findAll());
        truthIngredient.add(i_cukr);
        truthIngredient.add(i_soda);
        truthIngredient.add(i_milk);

        var truth = new ArrayList<>(dependencyProvider.getRecipeRepository().findAll());
        truth.add(r1);
        truth.add(r2);

        importer.importData(importFilePath.toString());
        var progress = importer.getProgress();

        assertThat(progress.isDone()).isTrue();
        assertThat(progress.getInsert()).isEqualTo(8);
        assertThat(progress.getUpdate()).isEqualTo(0);
        assertThat(progress.getIgnore()).isEqualTo(0);
        assertThat(progress.getRemove()).isEqualTo(0);

        refreshRepositories();
        assertThat(dependencyProvider.getCategoryRepository().findAll()).containsExactly(truthCategory.toArray(new Category[0]));
        assertThat(dependencyProvider.getUnitRepository().findAll()).containsExactly(truthUnit.toArray(new Unit[0]));
        assertThat(dependencyProvider.getIngredientRepository().findAll()).containsExactly(truthIngredient.toArray(new Ingredient[0]));
        assertThat(dependencyProvider.getRecipeRepository().findAll()).containsExactly(truth.toArray(new Recipe[0]));
    }

    @Test
    void duplicateRecipes() {
        Path importFilePath = TEST_RESOURCES.resolve("multi-recipes.json");

        Category c_drevo = new Category("Dřevěné jídla","FF996600");
        Category c_zelezo = new Category("Železná jídla", "FF666666");

        Unit u_asd = new Unit("asdasdasd", 5.0, BaseUnitsEnum.GRAM);

        Ingredient i_cukr = new Ingredient("Cukr", 200.0, u_pcs);
        Ingredient i_soda = new Ingredient("Soda", 1.0, u_g);
        Ingredient i_milk = new Ingredient("Mléko", 0.5555555555555556, u_ml);

        Recipe r1 = new Recipe(
            "Dřevo s chlebem",
            "Velmi chutné jídlo.",
            "Do vody přidáme dřevo a pak chleba.",
            c_drevo,
            15,
            3,
            List.of(
                new RecipeIngredient(i_cukr, 5.0, u_pcs),
                new RecipeIngredient(i_soda, 4.0, u_asd)
            )
        );

        Recipe r2 = new Recipe(
            "Železo v troubě",
            "Velice chutné železo, které je zdravé.",
            "Ohřejeme troubu na 250 stupňů a dáme železo na horní patro a pečeme než se spálí.",
            c_zelezo,
            30,
            269,
            List.of(
                new RecipeIngredient(i_milk, 5.0, u_ml)
            )
        );

        var truthCategory = new ArrayList<>(dependencyProvider.getCategoryRepository().findAll());
        truthCategory.add(c_drevo);
        truthCategory.add(c_zelezo);

        var truthUnit = new ArrayList<>(dependencyProvider.getUnitRepository().findAll());
        truthUnit.add(u_asd);

        var truthIngredient = new ArrayList<>(dependencyProvider.getIngredientRepository().findAll());
        truthIngredient.add(i_cukr);
        truthIngredient.add(i_soda);
        truthIngredient.add(i_milk);

        var truth = new ArrayList<>(dependencyProvider.getRecipeRepository().findAll());
        truth.add(r1);
        truth.add(r2);

        importer.importData(importFilePath.toString());
        assertThatThrownBy(() -> importer.importData(importFilePath.toString()))
            .isInstanceOf(DuplicateException.class);

        var progress = importer.getProgress();

        assertThat(progress.isDone()).isFalse();
        assertThat(progress.getInsert()).isEqualTo(0);
        assertThat(progress.getUpdate()).isEqualTo(0);
        assertThat(progress.getIgnore()).isGreaterThan(0);
        assertThat(progress.getRemove()).isEqualTo(0);

        refreshRepositories();
        assertThat(dependencyProvider.getCategoryRepository().findAll()).containsExactly(truthCategory.toArray(new Category[0]));
        assertThat(dependencyProvider.getUnitRepository().findAll()).containsExactly(truthUnit.toArray(new Unit[0]));
        assertThat(dependencyProvider.getIngredientRepository().findAll()).containsExactly(truthIngredient.toArray(new Ingredient[0]));
        assertThat(dependencyProvider.getRecipeRepository().findAll()).containsExactly(truth.toArray(new Recipe[0]));
    }

    @Test
    void uncategorizedRecipe() {
        Path importFilePath = TEST_RESOURCES.resolve("uncategorized-recipe.json");

        var recipe = new Recipe(
            "Dřevo s chlebem",
            "Velmi chutné jídlo.",
            "Do vody přidáme dřevo a pak chleba.",
            Category.UNCATEGORIZED,
            15,
            3,
            List.of(new RecipeIngredient(new Ingredient("Cukr", 200.0, u_pcs), 5.0, u_pcs))
        );

        var truthCategory = new ArrayList<>(dependencyProvider.getCategoryRepository().findAll());

        var truth = new ArrayList<>(dependencyProvider.getRecipeRepository().findAll());
        truth.add(recipe);

        importer.importData(importFilePath.toString());
        var progress = importer.getProgress();

        assertThat(progress.isDone()).isTrue();
        assertThat(progress.getInsert()).isEqualTo(2);
        assertThat(progress.getUpdate()).isEqualTo(0);
        assertThat(progress.getIgnore()).isEqualTo(0);
        assertThat(progress.getRemove()).isEqualTo(0);

        refreshRepositories();
        assertThat(dependencyProvider.getCategoryRepository().findAll()).containsExactly(truthCategory.toArray(new Category[0]));
        assertThat(dependencyProvider.getRecipeRepository().findAll()).containsExactly(truth.toArray(new Recipe[0]));
    }

    private void refreshRepositories() {
        dependencyProvider.getUnitRepository().refresh();
        dependencyProvider.getIngredientRepository().refresh();
        dependencyProvider.getCategoryRepository().refresh();
        dependencyProvider.getRecipeRepository().refresh();
    }
}
