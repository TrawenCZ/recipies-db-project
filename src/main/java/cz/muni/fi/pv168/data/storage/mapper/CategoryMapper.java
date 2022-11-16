package cz.muni.fi.pv168.data.storage.mapper;


import cz.muni.fi.pv168.data.storage.entity.CategoryEntity;
import cz.muni.fi.pv168.data.validation.Validator;
import cz.muni.fi.pv168.model.Category;

import java.awt.*;

public class CategoryMapper implements EntityMapper<CategoryEntity, Category> {

    private final Validator<Category> categoryValidator;

    public CategoryMapper(Validator<Category> categoryValidator) {
        this.categoryValidator = categoryValidator;
    }
    @Override
    public CategoryEntity mapToEntity(Category source) {
        categoryValidator.validate(source).intoException();

        return new CategoryEntity(
                source.getId(),
                source.getName(),
                source.getSerializedColor());
    }

    @Override
    public Category mapToModel(CategoryEntity entity) {
        String colorString = entity.color();
        Color color = new Color(
                Integer.valueOf(colorString.substring(2, 4), 16),
                Integer.valueOf(colorString.substring(4, 6), 16),
                Integer.valueOf(colorString.substring(6, 8), 16),
                Integer.valueOf(colorString.substring(0, 2), 16)
        );
        return new Category(
                entity.id(),
                entity.name(),
                color);
    }
}
