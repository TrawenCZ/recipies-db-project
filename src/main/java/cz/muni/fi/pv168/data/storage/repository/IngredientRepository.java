package cz.muni.fi.pv168.data.storage.repository;

import cz.muni.fi.pv168.data.storage.dao.IngredientDao;
import cz.muni.fi.pv168.data.storage.entity.IngredientEntity;
import cz.muni.fi.pv168.data.storage.mapper.EntityMapper;
import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.wiring.Supported;

public class IngredientRepository extends AbstractRepository<IngredientDao, IngredientEntity, Ingredient> {

    public IngredientRepository(IngredientDao dao, EntityMapper<IngredientEntity, Ingredient> mapper) {
        super(dao, mapper);
    }

    @Override
    public String toString() {
        return Supported.INGREDIENT;
    }
}
