package cz.muni.fi.pv168.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Jan Martinek, Radim Stejskal
 */
public class Recipe implements Nameable {

    // shown in table
    private String name;
    private String description;
    private String instructions;
    private Category category;
    private int requiredTime;
    private int portions;

    // shown only in details window
    private List<IngredientAmount> ingredients;

    @JsonCreator
    public Recipe(@JsonProperty("name") String name,
                  @JsonProperty("description") String description,
                  @JsonProperty("instruction") String instructions,
                  @JsonProperty("category") Category category,
                  @JsonProperty("preparationTime") int requiredTime,
                  @JsonProperty("portions") int portions,
                  @JsonProperty("ingredients") List<IngredientAmount> ingredients) {
        setName(name);
        setDescription(description);
        setInstructions(instructions);
        setCategory(category);
        setRequiredTime(requiredTime);
        setPortions(portions);
        setIngredients(ingredients);
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("category")
    public Category getCategory() {
        return category;
    }

    @JsonProperty("preparationTime")
    public int getRequiredTime() {
        return requiredTime;
    }

    @JsonProperty("portions")
    public int getPortions() {
        return portions;
    }

    @JsonProperty("ingredients")
    public List<IngredientAmount> getIngredients() {
        return ingredients;
    }

    @JsonProperty("instructions")
    public String getInstructions() {
        return instructions;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public void setDescription(String description) {
        this.description = Objects.requireNonNull(description);
    }

    public void setCategory(Category category) {
        this.category = Objects.requireNonNull(category);
    }

    public void setRequiredTime(int requiredTime) {
        if (requiredTime <= 0) throw new IllegalArgumentException("time must be >0");
        this.requiredTime = requiredTime;
    }

    public void setPortions(int portions) {
        if (portions <= 0) throw new IllegalArgumentException("portions must be >0");
        this.portions = portions;
    }

    public void setIngredients(List<IngredientAmount> ingredients) {
        this.ingredients = new ArrayList<>(Objects.requireNonNull(ingredients));
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Recipe other = (Recipe) o;
        return requiredTime == other.requiredTime
            && portions == other.portions
            && name.equals(other.name)
            && description.equals(other.description)
            && instructions.equals(other.instructions)
            && category.equals(other.category)
            && ingredients.equals(other.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            name, description, instructions, category, requiredTime, portions, ingredients
        );
    }

    @Override
    public String toString() {
        return name;
    }
}
