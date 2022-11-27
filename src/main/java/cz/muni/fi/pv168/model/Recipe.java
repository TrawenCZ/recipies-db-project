package cz.muni.fi.pv168.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Jan Martinek, Radim Stejskal
 */
public class Recipe implements Nameable, Identifiable {

    // shown in table
    private Long id;
    private String name;
    private String description;
    private String instructions;
    private Category category;
    private int requiredTime;
    private int portions;

    // shown only in details window
    private List<RecipeIngredient> ingredients;

    @JsonCreator
    public Recipe(@JsonProperty("name") String name,
                  @JsonProperty("description") String description,
                  @JsonProperty("instructions") String instructions,
                  @JsonProperty("category") Category category,
                  @JsonProperty("preparationTime") int requiredTime,
                  @JsonProperty("portions") int portions,
                  @JsonProperty("ingredients") List<RecipeIngredient> ingredients
    ) {
        this(null, name, description, instructions, category, requiredTime, portions, ingredients);
    }

    @JsonIgnore
    public Recipe(Long id,
                  String name,
                  String description,
                  String instructions,
                  Category category,
                  int requiredTime,
                  int portions,
                  List<RecipeIngredient> ingredients
    ) {
        setId(id);
        setName(name);
        setDescription(description);
        setInstructions(instructions);
        setCategory(category);
        setRequiredTime(requiredTime);
        setPortions(portions);
        setIngredients(ingredients);
    }

    @JsonIgnore
    public Long getId() {
        return id;
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
    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    @JsonProperty("instructions")
    public String getInstructions() {
        return instructions;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public void setDescription(String description) {
        Objects.requireNonNull(description, "description cannot be null");
        this.description = description.replace("\r\n", "\n");
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

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = new ArrayList<>(Objects.requireNonNull(ingredients));
    }

    public void setInstructions(String instructions) {
        Objects.requireNonNull(instructions, "instructions cannot be null");
        this.instructions = instructions.replace("\r\n", "\n");
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
