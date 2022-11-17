package cz.muni.fi.pv168.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * @author Radim Stejskal
 */
public class IngredientAmount implements Identifiable {
    private Long id;
    private Long recipeId;
    private Ingredient ingredient;
    private Double amount;
    private Unit unit;

    @JsonCreator
    public IngredientAmount(@JsonProperty("id") Long id,
                            @JsonProperty("recipeId") Long recipeId,
                            @JsonProperty("ingredient") Ingredient ingredient,
                            @JsonProperty("amount") Double amount,
                            @JsonProperty("unit") Unit unit) {
        this.id = id;
        this.recipeId = recipeId;
        this.ingredient = Objects.requireNonNull(ingredient, "ingredient must not be null");
        this.amount = Objects.requireNonNull(amount, "amount must not be null");
        this.unit = Objects.requireNonNull(unit, "unit must not be null");
    }
    public IngredientAmount(
                            Ingredient ingredient,
                            Double amount,
                            Unit unit) {
        this.ingredient = Objects.requireNonNull(ingredient, "ingredient must not be null");
        this.amount = Objects.requireNonNull(amount, "amount must not be null");
        this.unit = Objects.requireNonNull(unit, "unit must not be null");
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("recipeId")
    public Long getRecipeId() {
        return recipeId;
    }

    @JsonProperty("ingredient")
    public Ingredient getIngredient() {
        return ingredient;
    }

    @JsonProperty("amount")
    public Double getAmount() {
        return amount;
    }

    @JsonProperty("unit")
    public Unit getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientAmount other = (IngredientAmount) o;
        return ingredient.equals(other.ingredient)
            && amount.equals(other.amount)
            && unit.equals(other.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredient, amount, unit);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = Objects.requireNonNull(ingredient, "ingredient must not be null");
    }

    public void setAmount(Double amount) {
        this.amount = Objects.requireNonNull(amount, "amount must not be null");
        if (amount <= 0) throw new IllegalArgumentException("amount must be > 0");
    }

    public void setUnit(Unit unit) {
        this.unit = Objects.requireNonNull(unit, "unit must not be null");
    }
}
