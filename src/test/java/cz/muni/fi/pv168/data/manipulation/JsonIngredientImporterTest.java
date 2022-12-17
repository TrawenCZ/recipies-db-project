package cz.muni.fi.pv168.data.manipulation;

import cz.muni.fi.pv168.model.BaseUnitsEnum;
import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.Unit;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

public final class JsonIngredientImporterTest extends AbstractJsonImporterTest<Ingredient> {

    protected Importer getImporter() {
        return dependencyProvider.getIngredientImporter();
    }

    @Test
    void wrongDifferentFormat(){
        super.wrongDifferentFormat("multi-categories.json");
    }

    @Test
    void singleIngredient() {
        Path importFilePath = TEST_RESOURCES.resolve("single-ingredient.json");

        var truth = new ArrayList<>(dependencyProvider.getIngredientRepository().findAll());
        truth.add(new Ingredient("Soda", 1.0, new Unit("g", 1.0, null)));

        var truthUnits = new ArrayList<>(dependencyProvider.getUnitRepository().findAll());

        importer.importData(importFilePath.toString());
        var progress = importer.getProgress();

        assertThat(progress.isDone()).isTrue();
        assertThat(progress.getInsert()).isEqualTo(1);
        assertThat(progress.getUpdate()).isEqualTo(0);
        assertThat(progress.getIgnore()).isEqualTo(0);
        assertThat(progress.getRemove()).isEqualTo(0);

        dependencyProvider.getUnitRepository().refresh();
        assertThat(dependencyProvider.getUnitRepository().findAll()).containsExactly(truthUnits.toArray(new Unit[0]));

        dependencyProvider.getIngredientRepository().refresh();
        assertThat(dependencyProvider.getIngredientRepository().findAll()).containsExactly(truth.toArray(new Ingredient[0]));
    }

    @Test
    void multipleIngredients() {
        Path importFilePath = TEST_RESOURCES.resolve("multi-ingredients.json");

        var truth = new ArrayList<>(dependencyProvider.getIngredientRepository().findAll());
        truth.add(new Ingredient("Soda", 1.0, new Unit("g", 1.0, null)));
        truth.add(new Ingredient("Cukr", 200.0, new Unit("pc(s)", 1.0, null)));
        truth.add(new Ingredient("Vepřové maso", 1000, new Unit("uniqueUnit", 150.0, BaseUnitsEnum.GRAM)));
        truth.add(new Ingredient("Mléko", 0.5555555555555556, new Unit("ml", 1.0, null)));

        var truthUnits = new ArrayList<>(dependencyProvider.getUnitRepository().findAll());
        truthUnits.add(new Unit("uniqueUnit", 150.0, BaseUnitsEnum.GRAM));

        importer.importData(importFilePath.toString());
        var progress = importer.getProgress();

        assertThat(progress.isDone()).isTrue();
        assertThat(progress.getInsert()).isEqualTo(5);
        assertThat(progress.getUpdate()).isEqualTo(0);
        assertThat(progress.getIgnore()).isEqualTo(0);
        assertThat(progress.getRemove()).isEqualTo(0);

        dependencyProvider.getUnitRepository().refresh();
        assertThat(dependencyProvider.getUnitRepository().findAll()).containsExactly(truthUnits.toArray(new Unit[0]));

        dependencyProvider.getIngredientRepository().refresh();
        assertThat(dependencyProvider.getIngredientRepository().findAll()).containsExactly(truth.toArray(new Ingredient[0]));
    }

    @Test
    void duplicateIngredients() {
        Path importFilePath = TEST_RESOURCES.resolve("multi-ingredients.json");

        var truth = new ArrayList<>(dependencyProvider.getIngredientRepository().findAll());
        truth.add(new Ingredient("Soda", 1.0, new Unit("g", 1.0, null)));
        truth.add(new Ingredient("Cukr", 200.0, new Unit("pc(s)", 1.0, null)));
        truth.add(new Ingredient("Vepřové maso", 1000, new Unit("uniqueUnit", 150.0, BaseUnitsEnum.GRAM)));
        truth.add(new Ingredient("Mléko", 0.5555555555555556, new Unit("ml", 1.0, null)));

        var truthUnits = new ArrayList<>(dependencyProvider.getUnitRepository().findAll());
        truthUnits.add(new Unit("uniqueUnit", 150.0, BaseUnitsEnum.GRAM));

        importer.importData(importFilePath.toString());
        assertThatThrownBy(() -> importer.importData(importFilePath.toString()))
            .isInstanceOf(DuplicateException.class);

        var progress = importer.getProgress();

        assertThat(progress.isDone()).isFalse();
        assertThat(progress.getInsert()).isEqualTo(0);
        assertThat(progress.getUpdate()).isEqualTo(0);
        assertThat(progress.getIgnore()).isGreaterThan(0);
        assertThat(progress.getRemove()).isEqualTo(0);

        dependencyProvider.getUnitRepository().refresh();
        assertThat(dependencyProvider.getUnitRepository().findAll()).containsExactly(truthUnits.toArray(new Unit[0]));

        dependencyProvider.getIngredientRepository().refresh();
        assertThat(dependencyProvider.getIngredientRepository().findAll()).containsExactly(truth.toArray(new Ingredient[0]));
    }
}
