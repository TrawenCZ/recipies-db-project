package cz.muni.fi.pv168.data.validation;

import cz.muni.fi.pv168.model.Recipe;

public class RecipeValidator implements Validator<Recipe> {

    @Override
    public ValidationResult validate(Recipe recipe) {
        var result = new ValidationResult();

        validateStringLength("Recipe name", recipe.getName(), 1, 100)
                .ifPresent(result::add);
        validateStringLength("Recipe description", recipe.getDescription(), 1, 256)
                .ifPresent(result::add);
        validateStringLength("Recipe instructions", recipe.getDescription(), 1, 512)
                .ifPresent(result::add);

        return result;
    }
}
