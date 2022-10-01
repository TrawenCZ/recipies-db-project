package cz.muni.fi.pv168.data;

import cz.muni.fi.pv168.model.Category;
import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.Recipe;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class RecipeDataGenerator extends AbstractDataGenerator<Recipe> {
    
    private static final List<String> NAMES = List.of(
        "Svíčková",
        "Hovězí guláš",
        "Kuřecí řízek",
        "Vepřový řízek",
        "Šopský salát",
        "Boloňské špagety",
        "Srbské rizoto",
        "Jahodový dort",
        "Pestrá selská pánev",
        "Uzenářské ragů");

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

    public Recipe createTestEntity() {
        String name = selectRandom(NAMES);
        String description = selectRandom(DESCRIPTIONS);
        Category category = categories.createTestEntity();
        int time = random.nextInt(120);
        int portions = random.nextInt(12);
        Map<Ingredient, Double> ingredients = getIngredients(2000d, random.nextInt(20));
        
        return new Recipe(name, description, category, time, portions, ingredients);
    }

    private Map<Ingredient, Double> getIngredients(Double maxValue, int count) {
        Map<Ingredient, Double> m = new HashMap<>();
        for (Ingredient i : ingredients.createTestData(count)) {
            m.put(i, random.nextDouble(2000));
        }
        return m;
    }
}
