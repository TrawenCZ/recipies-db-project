package cz.muni.fi.pv168.data.validation;

import cz.muni.fi.pv168.model.Category;

public class CategoryValidator implements Validator<Category> {

    @Override
    public ValidationResult validate(Category category) {
        var result = new ValidationResult();

        validateStringLength("Category name", category.getName(), 1, 100)
                .ifPresent(result::add);
        validateStringLength("Category color", category.getSerializedColor(), 1, 20)
                .ifPresent(result::add);

        return result;
    }
}
