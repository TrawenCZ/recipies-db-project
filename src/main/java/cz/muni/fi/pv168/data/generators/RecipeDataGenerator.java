package cz.muni.fi.pv168.data.generators;

import cz.muni.fi.pv168.model.Category;
import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.IngredientAmount;
import cz.muni.fi.pv168.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeDataGenerator extends AbstractDataGenerator<Recipe> {

    private static int lastId = 0;

    private static final List<String> NAMES = new ArrayList<>(List.of(
        "Svíčková",
        "Hovězí guláš",
        "Kuřecí řízek",
        "Vepřový řízek",
        "Šopský salát",
        "Boloňské špagety",
        "Srbské rizoto",
        "Jahodový dort",
        "Pestrá selská pánev",
        "Uzenářské ragů"
    ));

    private static final List<String> DESCRIPTIONS = List.of(
        "Velmi chutný lehký pokrm.",
        "Pravá česká kuchyně přímo od babičky.",
        "Jediné světlo mého života je světlo v ledničce",
        "Varování: Přežírání může zmenšovat oblečení.",
        "Až jednoho dne ucítíš velké prázdno, jez. Je to hlad!",
        "Celý život jsem si myslel, že vzduch je zadarmo. Myslel jsem si to do té doby, než jsem si připravil tohle jídlo."
    );

    private static final CategoryDataGenerator categories = new CategoryDataGenerator();
    private static final IngredientDataGenerator ingredients = new IngredientDataGenerator();
    private static final UnitDataGenerator units = new UnitDataGenerator();

    public Recipe createTestEntity() {
        String name;
        if (NAMES.isEmpty()) {
            name = "recipe_" + ++lastId;
        } else {
            name = selectRandom(NAMES);
            NAMES.remove(name);
        }

        String description = selectRandom(DESCRIPTIONS);
        String instructions = "instructions";
        //Category category = (random.nextInt(0, 10) == 0) ? Category.UNCATEGORIZED : categories.createTestEntity(); -- original line 52
        Category category = categories.createTestEntity();
        int time = random.nextInt(1, 120);
        int portions = random.nextInt(1, 12);
        List<IngredientAmount> ingredients = getIngredients(2000d, random.nextInt(20));

        return new Recipe(0, name, description, instructions, category, time, portions, ingredients);
    }


    private List<IngredientAmount> getIngredients(Double maxValue, int count) {
        List<IngredientAmount> m = new ArrayList<>();
        for (Ingredient i : ingredients.createTestData(count)) {
            m.add(new IngredientAmount(0, 0,
                i,
                (double) Math.round(random.nextDouble(2000) * 100) / 100,
                units.createTestEntity()));
        }
        return m;
    }

}
