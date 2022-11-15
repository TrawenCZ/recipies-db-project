package cz.muni.fi.pv168.data.storage.mapper;

import cz.muni.fi.pv168.data.storage.dao.CategoryDao;
import cz.muni.fi.pv168.data.storage.dao.IngredientListDao;
import cz.muni.fi.pv168.data.storage.dao.RecipeDao;
import cz.muni.fi.pv168.data.storage.entity.CategoryEntity;
import cz.muni.fi.pv168.data.storage.entity.IngredientListEntity;
import cz.muni.fi.pv168.data.storage.entity.RecipeEntity;
import cz.muni.fi.pv168.data.storage.repository.AbstractRepository;
import cz.muni.fi.pv168.data.validation.Validator;
import cz.muni.fi.pv168.model.Category;
import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.IngredientAmount;
import cz.muni.fi.pv168.model.Recipe;

public class RecipeMapper implements EntityMapper<RecipeEntity, Recipe> {

    private final Validator<Recipe> recipeValidator;
    private final AbstractRepository<CategoryDao, CategoryEntity, Category> categories;
    private final AbstractRepository<IngredientListDao, IngredientListEntity, IngredientAmount> ingredients;

    public RecipeMapper(Validator<Recipe> recipeValidator,
                        AbstractRepository<CategoryDao, CategoryEntity, Category> categories,
                        AbstractRepository<IngredientListDao, IngredientListEntity, IngredientAmount> ingredients) {
        this.recipeValidator = recipeValidator;
        this.categories = categories;
        this.ingredients = ingredients;
    }

    @Override
    public RecipeEntity mapToEntity(Recipe source) {
        recipeValidator.validate(source).intoException();

        Long categoryId = null;
        if (source.getCategory() != null) {
            categoryId = source.getCategory().getId();
        }

        return new RecipeEntity(
                source.getId(),
                source.getName(),
                source.getDescription(),
                categoryId,
                source.getPortions(),
                source.getRequiredTime(),
                source.getInstructions());
    }

    @Override
    public Recipe mapToModel(RecipeEntity entity) {
        Category category = categories.findById(entity.categoryId()).orElseThrow();
        return new Recipe(entity.id(), entity.name(), entity.description(), entity.instruction(), category,
                entity.duration(), entity.portions(), ingredients.findAll());
    }
}
