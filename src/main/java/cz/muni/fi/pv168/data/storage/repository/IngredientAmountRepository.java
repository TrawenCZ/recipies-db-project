package cz.muni.fi.pv168.data.storage.repository;

import cz.muni.fi.pv168.data.storage.dao.IngredientListDao;
import cz.muni.fi.pv168.data.storage.entity.IngredientListEntity;
import cz.muni.fi.pv168.data.storage.mapper.EntityMapper;
import cz.muni.fi.pv168.model.IngredientAmount;

import java.util.Optional;

public class IngredientAmountRepository extends AbstractRepository<IngredientListDao, IngredientListEntity, IngredientAmount> {
    public IngredientAmountRepository(IngredientListDao dao, EntityMapper<IngredientListEntity, IngredientAmount> mapper) {
        super(dao, mapper);
    }

    public Optional<IngredientAmount> findByRecipeId(Long recipeId) {
        return entities.stream().filter(e -> e.getRecipeId() == recipeId).findFirst();
    }

}
