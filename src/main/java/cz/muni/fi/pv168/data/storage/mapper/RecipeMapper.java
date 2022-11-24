package cz.muni.fi.pv168.data.storage.mapper;

import java.sql.Types;
import java.util.List;
import java.util.Objects;

import cz.muni.fi.pv168.data.storage.entity.RecipeEntity;
import cz.muni.fi.pv168.data.validation.Validator;
import cz.muni.fi.pv168.model.Category;
import cz.muni.fi.pv168.model.Recipe;

/**
 * @author Jan Martinek
 */
public class RecipeMapper implements EntityMapper<RecipeEntity, Recipe> {

    private final Validator<Recipe> recipeValidator;
    private final Lookup<Category> categorySupplier;

    public RecipeMapper(Validator<Recipe> recipeValidator,
                        Lookup<Category> categorySupplier
    ) {
        this.recipeValidator = Objects.requireNonNull(recipeValidator);
        this.categorySupplier = Objects.requireNonNull(categorySupplier);
    }

    @Override
    public RecipeEntity mapToEntity(Recipe source) {
        recipeValidator.validate(source).intoException();

        return new RecipeEntity(
            source.getId(),
            source.getName(),
            source.getDescription(),
            (source.getCategory().equals(Category.UNCATEGORIZED)) ? null : source.getCategory().getId(),
            source.getPortions(),
            source.getRequiredTime(),
            source.getInstructions()
        );
    }

    @Override
    public Recipe mapToModel(RecipeEntity entity) {
        Category category = Category.UNCATEGORIZED;
        if (entity.categoryId() != Types.NULL) {
            category = categorySupplier.get(entity.categoryId()).orElseThrow();
        }

        return new Recipe(
            entity.id(),
            entity.name(),
            entity.description(),
            entity.instruction(),
            category,
            (int) entity.duration(),
            (int) entity.portions(),
            List.of()
        );
    }
}
