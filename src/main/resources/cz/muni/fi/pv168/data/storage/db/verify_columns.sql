-- Check if columns exist
SELECT (`id`, `name`, `amount`, `baseUnitId`) FROM Unit;
SELECT (`id`, `name`, `kcalPerUnit`, `baseUnitId`) FROM Ingredient;
SELECT (`id`, `name`, `color`) FROM Category;
SELECT (`id`, `name`, `description`, `categoryId`, `portions`, `duration`, `instruction`) FROM Recipe;
SELECT (`id`, `recipeId`, `ingredientId`, `amount`, `unitId`) FROM IngredientList;
