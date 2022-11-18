package cz.muni.fi.pv168.data.storage.repository;

import java.util.Objects;

import cz.muni.fi.pv168.data.storage.dao.RecipeDao;
import cz.muni.fi.pv168.data.storage.dao.RecipeIngredientDao;
import cz.muni.fi.pv168.data.storage.entity.RecipeEntity;
import cz.muni.fi.pv168.data.storage.mapper.EntityMapper;
import cz.muni.fi.pv168.data.storage.mapper.RecipeIngredientMapper;
import cz.muni.fi.pv168.model.Recipe;

public class RecipeRepository extends AbstractRepository<RecipeDao, RecipeEntity, Recipe> {

    private final RecipeIngredientDao ingredientDao;
    private final RecipeIngredientMapper ingredientMapper;

    public RecipeRepository(RecipeDao dao,
                            EntityMapper<RecipeEntity, Recipe> mapper,
                            RecipeIngredientDao ingredientDao,
                            RecipeIngredientMapper ingredientMapper
    ) {
        super(dao, mapper, false);
        this.ingredientDao = Objects.requireNonNull(ingredientDao);
        this.ingredientMapper = Objects.requireNonNull(ingredientMapper);
        ingredientMapper.updateSuppliers(this::findById);
        refresh();
    }

    public void create(Recipe newEntity) {
        super.create(newEntity);
        newEntity.getIngredients().stream()
                .map(ingredientMapper::mapToEntity)
                .map(ingredientDao::create);
    }

    public void update(Recipe entity) {
        super.update(entity);
        entity.getIngredients().stream()
                .map(ingredientMapper::mapToEntity)
                .map(ingredientDao::update);
    }

    @Override
    public String toString() {
        return "Recipe";
    }
}
