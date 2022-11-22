package cz.muni.fi.pv168.data.storage.entity;

public record RecipeIngredientEntity(
        Long id,
        long recipeId,     // foreign key
        long ingredientId, // foreign key
        double amount,
        long unitId        // foreign key
) {
    public RecipeIngredientEntity(
            long recipeId,
            long ingredientId,
            double amount,
            long unitId
    ){
        this(null, recipeId, ingredientId, amount, unitId);
    }
}
