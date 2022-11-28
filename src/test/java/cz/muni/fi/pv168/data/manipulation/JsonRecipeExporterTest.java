package cz.muni.fi.pv168.data.manipulation;

import cz.muni.fi.pv168.model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class JsonRecipeExporterTest extends AbstractJsonExporterTest<Recipe> {
    @Test
    void oneRecipe() throws IOException {
        List<RecipeIngredient> ingredientList = new ArrayList<>();
        ingredientList.add(new RecipeIngredient(new Ingredient("Cukr", 200.0, new Unit("pc(s)", 1.0, null)),
                5.0,
                new Unit("pc(s)", 1.0, null))
        );
        ingredientList.add(new RecipeIngredient(new Ingredient("Soda", 1.0, new Unit("g", 1.0, null)),
                4.0,
                new Unit("g", 1.0, null))
        );
        var recipe = new Recipe("Dřevo s chlebem",
                "Velmi chutné jídlo.",
                "Do vody přidáme dřevo a pak chleba.",
                new Category("Dřevěné jídla","FF996600"),
                15,
                3,
                ingredientList
        );
        testDirSave(List.of(recipe));
        assertExportedContent("""
                [ {
                  "name" : "Dřevo s chlebem",
                  "description" : "Velmi chutné jídlo.",
                  "instructions" : "Do vody přidáme dřevo a pak chleba.",
                  "category" : {
                    "name" : "Dřevěné jídla",
                    "color" : "FF996600"
                  },
                  "preparationTime" : 15,
                  "portions" : 3,
                  "ingredients" : [ {
                    "ingredient" : {
                      "name" : "Cukr",
                      "kcal" : 200.0,
                      "unit" : {
                        "name" : "pc(s)",
                        "valueInBaseUnit" : 1.0,
                        "baseUnit" : null
                      }
                    },
                    "amount" : 5.0,
                    "unit" : {
                      "name" : "pc(s)",
                      "valueInBaseUnit" : 1.0,
                      "baseUnit" : null
                    }
                  }, {
                    "ingredient" : {
                      "name" : "Soda",
                      "kcal" : 1.0,
                      "unit" : {
                        "name" : "g",
                        "valueInBaseUnit" : 1.0,
                        "baseUnit" : null
                      }
                    },
                    "amount" : 4.0,
                    "unit" : {
                      "name" : "g",
                      "valueInBaseUnit" : 1.0,
                      "baseUnit" : null
                    }
                  } ]
                } ]
        """);
    }

    @Test
    void multipleRecipes() throws IOException {
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
        var recipes = List.of(
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
        testDirSave(recipes);
        assertExportedContent("""
            [ {
                "name" : "Dřevo s chlebem",
                "description" : "Velmi chutné jídlo.",
                "instructions" : "Do vody přidáme dřevo a pak chleba.",
                "category" : {
                "name" : "Dřevěné jídla",
                "color" : "FF996600"
                },
                "preparationTime" : 15,
                "portions" : 3,
                "ingredients" : [ {
                "ingredient" : {
                    "name" : "Cukr",
                    "kcal" : 200.0,
                    "unit" : {
                    "name" : "pc(s)",
                    "valueInBaseUnit" : 1.0,
                    "baseUnit" : null
                    }
                },
                "amount" : 5.0,
                "unit" : {
                    "name" : "pc(s)",
                    "valueInBaseUnit" : 1.0,
                    "baseUnit" : null
                }
                }, {
                "ingredient" : {
                    "name" : "Soda",
                    "kcal" : 1.0,
                    "unit" : {
                    "name" : "g",
                    "valueInBaseUnit" : 1.0,
                    "baseUnit" : null
                    }
                },
                "amount" : 4.0,
                "unit" : {
                    "name" : "asdasdasd",
                    "valueInBaseUnit" : 5.0,
                    "baseUnit" : "GRAM"
                }
                } ]
            }, {
                "name" : "Železo v troubě",
                "description" : "Velice chutné železo, které je zdravé.",
                "instructions" : "Ohřejeme troubu na 250 stupňů a dáme železo na horní patro a pečeme než se spálí.",
                "category" : {
                "name" : "Železná jídla",
                "color" : "FF666666"
                },
                "preparationTime" : 30,
                "portions" : 269,
                "ingredients" : [ {
                "ingredient" : {
                    "name" : "Mléko",
                    "kcal" : 0.5555555555555556,
                    "unit" : {
                    "name" : "ml",
                    "valueInBaseUnit" : 1.0,
                    "baseUnit" : null
                    }
                },
                "amount" : 5.0,
                "unit" : {
                    "name" : "ml",
                    "valueInBaseUnit" : 1.0,
                    "baseUnit" : null
                }
                }, {
                "ingredient" : {
                    "name" : "Soda",
                    "kcal" : 1.0,
                    "unit" : {
                    "name" : "g",
                    "valueInBaseUnit" : 1.0,
                    "baseUnit" : null
                    }
                },
                "amount" : 8.0,
                "unit" : {
                    "name" : "g",
                    "valueInBaseUnit" : 1.0,
                    "baseUnit" : null
                }
                }, {
                "ingredient" : {
                    "name" : "Cukr",
                    "kcal" : 200.0,
                    "unit" : {
                    "name" : "pc(s)",
                    "valueInBaseUnit" : 1.0,
                    "baseUnit" : null
                    }
                },
                "amount" : 65.0,
                "unit" : {
                    "name" : "pc(s)",
                    "valueInBaseUnit" : 1.0,
                    "baseUnit" : null
                }
                } ]
            } ]
        """);
    }

    @Test
    void uncategorizedRecipe() throws IOException {
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
        testDirSave(List.of(recipe));
        assertExportedContent("""
                [ {
                  "name" : "Dřevo s chlebem",
                  "description" : "Velmi chutné jídlo.",
                  "instructions" : "Do vody přidáme dřevo a pak chleba.",
                  "category" : null,
                  "preparationTime" : 15,
                  "portions" : 3,
                  "ingredients" : [ {
                    "ingredient" : {
                      "name" : "Cukr",
                      "kcal" : 200.0,
                      "unit" : {
                        "name" : "pc(s)",
                        "valueInBaseUnit" : 1.0,
                        "baseUnit" : null
                      }
                    },
                    "amount" : 5.0,
                    "unit" : {
                      "name" : "pc(s)",
                      "valueInBaseUnit" : 1.0,
                      "baseUnit" : null
                    }
                  } ]
                } ]
        """);
    }
}
