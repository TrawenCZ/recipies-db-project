package cz.muni.fi.pv168.data.storage.mapper;

import java.util.List;
import java.util.Objects;

import cz.muni.fi.pv168.data.storage.entity.RecipeEntity;
import cz.muni.fi.pv168.data.storage.entity.RecipeIngredientEntity;
import cz.muni.fi.pv168.data.validation.Validator;
import cz.muni.fi.pv168.model.Category;
import cz.muni.fi.pv168.model.Recipe;
import cz.muni.fi.pv168.model.RecipeIngredient;

public class RecipeMapper implements EntityMapper<RecipeEntity, Recipe> {

    private final Validator<Recipe> recipeValidator;
    private final Lookup<Category> categorySupplier;
    private final Lookup<List<RecipeIngredientEntity>> recipeIngredientEntitySupplier;
    private final RecipeIngredientMapper recipeIngredientMapper;

    public RecipeMapper(Validator<Recipe> recipeValidator,
                        Lookup<Category> categorySupplier,
                        Lookup<List<RecipeIngredientEntity>> recipeIngredientEntitySupplier,
                        RecipeIngredientMapper recipeIngredientMapper
    ) {
        this.recipeValidator = Objects.requireNonNull(recipeValidator);
        this.categorySupplier = Objects.requireNonNull(categorySupplier);
        this.recipeIngredientEntitySupplier = Objects.requireNonNull(recipeIngredientEntitySupplier);
        this.recipeIngredientMapper = Objects.requireNonNull(recipeIngredientMapper);
    }

    @Override
    public RecipeEntity mapToEntity(Recipe source) {
        recipeValidator.validate(source).intoException();

        return new RecipeEntity(
            source.getId(),
            source.getName(),
            source.getDescription(),
            (source.getCategory() == null) ? null : source.getCategory().getId(),
            source.getPortions(),
            source.getRequiredTime(),
            source.getInstructions()
        );
    }

    @Override
    public Recipe mapToModel(RecipeEntity entity) {
        Category category = categorySupplier.get(entity.categoryId()).orElseThrow();
        List<RecipeIngredient> ingredients = recipeIngredientEntitySupplier.get(entity.id()).orElseThrow()
                                                                           .stream()
                                                                           .map(recipeIngredientMapper::mapToModel)
                                                                           .toList();

        return new Recipe(
            entity.id(),
            entity.name(),
            entity.description(),
            entity.instruction(),
            category,
            (int) entity.duration(),
            (int) entity.portions(),
            ingredients
        );
    }
}
