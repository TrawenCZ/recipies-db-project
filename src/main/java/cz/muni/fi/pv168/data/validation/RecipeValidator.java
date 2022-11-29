package cz.muni.fi.pv168.data.validation;

import cz.muni.fi.pv168.model.Recipe;

public class RecipeValidator implements Validator<Recipe> {

    @Override
    public ValidationResult validate(Recipe recipe) {
        var result = new ValidationResult();

        validateStringLength("Recipe name", recipe.getName(), 1, FieldMaxLengths.NAME)
                .ifPresent(result::add);
        validateStringLength("Recipe description", recipe.getDescription(), 1, FieldMaxLengths.RECIPE_DESCRIPTION)
                .ifPresent(result::add);
        validateStringLength("Recipe instructions", recipe.getDescription(), 1, FieldMaxLengths.RECIPE_INSTRUCTIONS)
                .ifPresent(result::add);
        validateIntValue(recipe.getPortions(), 1, 999)
                .ifPresent(result::add);
        validateIntValue(recipe.getRequiredTime(), 1, 999)
                .ifPresent(result::add);

        return result;
    }
}
