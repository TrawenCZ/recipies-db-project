package cz.muni.fi.pv168.data.validation;

import cz.muni.fi.pv168.model.Ingredient;

public class IngredientValidator implements Validator<Ingredient> {
    @Override
    public ValidationResult validate(Ingredient ingredient) {
        var result = new ValidationResult();

        validateStringLength("Ingredient name", ingredient.getName(), 1, 100)
                .ifPresent(result::add);

        return result;
    }
}
