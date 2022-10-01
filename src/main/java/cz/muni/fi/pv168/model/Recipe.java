package cz.muni.fi.pv168.model;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

/**
 * Currently in a PLACEHOLDER STATE for rendering the tables
 */
public class Recipe {
    
    // shown in table
    private String name;
    private String description;
    private Category category;
    private int requiredTime;
    private int portions;

    // shown only in details window
    private Map<Ingredient, Double> ingredients;

    public Recipe(String name, String description, Category category, int requiredTime, int portions, Map<Ingredient, Double> ingredients) {
        setName(name);
        setDescription(description);
        setCategory(category);
        setRequiredTime(requiredTime);
        setPortions(portions);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = Objects.requireNonNull(description);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = Objects.requireNonNull(category);
    }

    public int getRequiredTime() {
        return requiredTime;
    }

    public void setRequiredTime(int requiredTime) {
        this.requiredTime = requiredTime;
    }

    public int getPortions() {
        return portions;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }

    public Map<Ingredient, Double> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<Ingredient, Double> ingredients) {
        Objects.requireNonNull(ingredients);
        for (Ingredient k : ingredients.keySet()) {
            Objects.requireNonNull(k);
        }
        for (Double k : ingredients.values()) {
            if (Double.compare(k, 0) <= 0) throw new IllegalArgumentException("Ingredient weight/count must be more than 0");
        }
        this.ingredients = new HashMap<>(ingredients);
    }

    @Override
    public String toString() {
        return name;
    }
}
