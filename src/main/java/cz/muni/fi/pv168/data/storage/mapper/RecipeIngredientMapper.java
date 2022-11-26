package cz.muni.fi.pv168.data.storage.mapper;

import java.util.Objects;

import cz.muni.fi.pv168.data.storage.entity.RecipeIngredientEntity;
import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.RecipeIngredient;
import cz.muni.fi.pv168.model.Unit;

/**
 * @author Jan Martinek
 */
public class RecipeIngredientMapper implements EntityMapper<RecipeIngredientEntity, RecipeIngredient> {

    private final Lookup<Unit> unitSupplier;
    private final Lookup<Ingredient> ingredientSupplier;

    public RecipeIngredientMapper(Lookup<Unit> unitSupplier,
                                  Lookup<Ingredient> ingredientSupplier
    ) {
        this.unitSupplier = Objects.requireNonNull(unitSupplier);
        this.ingredientSupplier = Objects.requireNonNull(ingredientSupplier);
    }

    @Override
    public RecipeIngredientEntity mapToEntity(RecipeIngredient source) {
        return new RecipeIngredientEntity(
                source.getId(),
                source.getRecipeId(),
                source.getIngredient().getId(),
                source.getAmount(),
                source.getUnit().getId()
        );
    }

    @Override
    public RecipeIngredient mapToModel(RecipeIngredientEntity entity) {
        Unit unit = unitSupplier.get(entity.unitId()).orElseThrow();
        Ingredient ingredient = ingredientSupplier.get(entity.ingredientId()).orElseThrow();

        return new RecipeIngredient(
                entity.id(),
                entity.recipeId(),
                ingredient,
                entity.amount(),
                unit
        );
    }
}
