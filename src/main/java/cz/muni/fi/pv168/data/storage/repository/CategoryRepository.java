package cz.muni.fi.pv168.data.storage.repository;

import cz.muni.fi.pv168.data.storage.dao.CategoryDao;
import cz.muni.fi.pv168.data.storage.entity.CategoryEntity;
import cz.muni.fi.pv168.data.storage.mapper.EntityMapper;
import cz.muni.fi.pv168.model.Category;


public class CategoryRepository extends AbstractRepository<CategoryDao, CategoryEntity, Category> {
    public CategoryRepository(CategoryDao dao, EntityMapper<CategoryEntity, Category> mapper) {
        super(dao, mapper);
    }
}
