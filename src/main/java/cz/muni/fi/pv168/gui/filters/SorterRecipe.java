package cz.muni.fi.pv168.gui.filters;

import java.util.List;
import java.util.Objects;

import javax.swing.JTable;
import javax.swing.RowFilter;

import cz.muni.fi.pv168.gui.elements.Filterable;
import cz.muni.fi.pv168.gui.models.AbstractModel;
import cz.muni.fi.pv168.model.Recipe;

/**
 * @author Jan Martinek
 */
public class SorterRecipe extends Sorter {

    private Filterable<List<String>> categories;
    private Filterable<List<Integer>> requiredTime;
    private Filterable<List<Integer>> portions;
    private Filterable<List<String>> ingredients;

    public SorterRecipe(JTable table,
                        AbstractModel<?> model,
                        Filterable<String> searchBar,
                        Filterable<List<String>> categories,
                        Filterable<List<Integer>> requiredTime,
                        Filterable<List<Integer>> portions,
                        Filterable<List<String>> ingredients
    ) {
        super(table, model, searchBar);
        this.categories = Objects.requireNonNull(categories);
        this.requiredTime = Objects.requireNonNull(requiredTime);
        this.portions = Objects.requireNonNull(portions);
        this.ingredients = Objects.requireNonNull(ingredients);
    }

    @Override
    public void resetFilters() {
        categories.resetFilters();
        requiredTime.resetFilters();
        portions.resetFilters();
        ingredients.resetFilters();
        super.resetFilters();
    }

    @Override
    protected RowFilter<AbstractModel<?>, Integer> newFilter() {
        var portionsValues = portions.getFilters();
        var timeValues = requiredTime.getFilters();

        RowFilter<AbstractModel<?>, Integer> categoryFilter = null;
        RowFilter<AbstractModel<?>, Integer> requiredTimeFilter = null;
        RowFilter<AbstractModel<?>, Integer> portionsFilter = null;
        RowFilter<AbstractModel<?>, Integer> ingredientsFilter = null;

        try {
            categoryFilter = new CategoryFilter(categories.getFilters());
            requiredTimeFilter = new NumericFilter(timeValues.get(0), timeValues.get(1), Recipe::getRequiredTime);
            portionsFilter = new NumericFilter(portionsValues.get(0), portionsValues.get(1), Recipe::getPortions);
            ingredientsFilter = new IngredientFilter(ingredients.getFilters());
        } catch (java.util.regex.PatternSyntaxException e) {
            return null;
        }

        return RowFilter.andFilter(List.of(
            super.newFilter(),
            categoryFilter,
            requiredTimeFilter,
            portionsFilter,
            ingredientsFilter
        ));
    }
}
