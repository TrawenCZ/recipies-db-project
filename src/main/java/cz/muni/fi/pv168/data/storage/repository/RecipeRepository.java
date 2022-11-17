package cz.muni.fi.pv168.data.storage.repository;

import cz.muni.fi.pv168.data.storage.dao.RecipeDao;
import cz.muni.fi.pv168.data.storage.entity.RecipeEntity;
import cz.muni.fi.pv168.data.storage.mapper.EntityMapper;
import cz.muni.fi.pv168.model.Recipe;

public class RecipeRepository extends AbstractRepository<RecipeDao, RecipeEntity, Recipe> {
    public RecipeRepository(RecipeDao dao, EntityMapper<RecipeEntity, Recipe> mapper) {
        super(dao, mapper);
    }
}
