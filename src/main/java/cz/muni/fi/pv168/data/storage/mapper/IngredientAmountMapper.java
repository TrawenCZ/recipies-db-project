package cz.muni.fi.pv168.data.storage.mapper;

import cz.muni.fi.pv168.data.storage.entity.IngredientListEntity;
import cz.muni.fi.pv168.data.storage.repository.IngredientRepository;
import cz.muni.fi.pv168.data.storage.repository.RecipeRepository;
import cz.muni.fi.pv168.data.storage.repository.UnitRepository;
import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.IngredientAmount;
import cz.muni.fi.pv168.model.Recipe;
import cz.muni.fi.pv168.model.Unit;

public class IngredientAmountMapper implements EntityMapper<IngredientListEntity, IngredientAmount> {
    private final IngredientRepository ingredients;
    private final UnitRepository units;
    private final RecipeRepository recipes;

    public IngredientAmountMapper(
            IngredientRepository ingredients,
            UnitRepository units,
            RecipeRepository recipes,
    ) {
        this.ingredients = ingredients;
        this.units = units;
        this.recipes = recipes;
    }

    @Override
    public IngredientListEntity mapToEntity(IngredientAmount source) {
        return new IngredientListEntity(
                source.getId(),
                source.getRecipe().getId(),
                source.getIngredient().getId(),
                source.getAmount(),
                source.getUnit().getId()
        );
    }

    @Override
    public IngredientAmount mapToModel(IngredientListEntity entity) {
        Ingredient ingredient = ingredients.findById(entity.ingredientId()).orElseThrow();
        Unit unit = units.findById(entity.unitId()).orElseThrow();
        Recipe recipe = recipes.findById(entity.recipeId()).orElseThrow();
        return new IngredientAmount(
                entity.id(),
                recipe,
                ingredient,
                entity.amount(),
                unit
        );
    }
}
