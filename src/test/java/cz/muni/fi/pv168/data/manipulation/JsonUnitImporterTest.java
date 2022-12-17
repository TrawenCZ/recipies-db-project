package cz.muni.fi.pv168.data.manipulation;

import cz.muni.fi.pv168.model.BaseUnitsEnum;
import cz.muni.fi.pv168.model.Unit;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

public final class JsonUnitImporterTest extends AbstractJsonImporterTest<Unit> {

    protected Importer getImporter() {
        return dependencyProvider.getUnitImporter();
    }

    @Test
    void wrongDifferentFormat(){
        super.wrongDifferentFormat("multi-ingredients.json");
    }

    @Test
    void singleUnit() {
        Path importFilePath = TEST_RESOURCES.resolve("single-unit.json");

        var truth = new ArrayList<>(dependencyProvider.getUnitRepository().findAll());
        truth.add(new Unit("planckPiece", 23.0, BaseUnitsEnum.PIECE));

        importer.importData(importFilePath.toString());
        var progress = importer.getProgress();

        assertThat(progress.isDone()).isTrue();
        assertThat(progress.getInsert()).isEqualTo(1);
        assertThat(progress.getUpdate()).isEqualTo(0);
        assertThat(progress.getIgnore()).isEqualTo(0);
        assertThat(progress.getRemove()).isEqualTo(0);

        dependencyProvider.getUnitRepository().refresh();
        assertThat(dependencyProvider.getUnitRepository().findAll()).containsExactly(truth.toArray(new Unit[0]));
    }


    @Test
    void baseUnit() {
        Path importFilePath = TEST_RESOURCES.resolve("base-unit.json");

        var truth = new ArrayList<>(dependencyProvider.getUnitRepository().findAll());

        importer.importData(importFilePath.toString());
        var progress = importer.getProgress();

        assertThat(progress.isDone()).isTrue();
        assertThat(progress.getInsert()).isEqualTo(0);
        assertThat(progress.getUpdate()).isEqualTo(0);
        assertThat(progress.getIgnore()).isEqualTo(0);
        assertThat(progress.getRemove()).isEqualTo(0);

        dependencyProvider.getUnitRepository().refresh();
        assertThat(dependencyProvider.getUnitRepository().findAll()).containsExactly(truth.toArray(new Unit[0]));
    }

    @Test
    void multipleUnits() {
        Path importFilePath = TEST_RESOURCES.resolve("multi-units.json");

        var truth = new ArrayList<>(dependencyProvider.getUnitRepository().findAll());
        truth.add(new Unit("vetsiGram", 10.0, BaseUnitsEnum.GRAM));
        truth.add(new Unit("jeste vetsi gram s mezerama", 10.1, BaseUnitsEnum.GRAM));
        truth.add(new Unit("jeste vetsi vetsi gram s mezerama ale neni to gram", 10.3, BaseUnitsEnum.MILLILITER));

        importer.importData(importFilePath.toString());
        var progress = importer.getProgress();

        assertThat(progress.isDone()).isTrue();
        assertThat(progress.getInsert()).isEqualTo(3);
        assertThat(progress.getUpdate()).isEqualTo(0);
        assertThat(progress.getIgnore()).isEqualTo(0);
        assertThat(progress.getRemove()).isEqualTo(0);

        dependencyProvider.getUnitRepository().refresh();
        assertThat(dependencyProvider.getUnitRepository().findAll()).containsExactly(truth.toArray(new Unit[0]));
    }

    @Test
    void duplicateUnits() {
        Path importFilePath = TEST_RESOURCES.resolve("multi-units.json");

        var truth = new ArrayList<>(dependencyProvider.getUnitRepository().findAll());
        truth.add(new Unit("vetsiGram", 10.0, BaseUnitsEnum.GRAM));
        truth.add(new Unit("jeste vetsi gram s mezerama", 10.1, BaseUnitsEnum.GRAM));
        truth.add(new Unit("jeste vetsi vetsi gram s mezerama ale neni to gram", 10.3, BaseUnitsEnum.MILLILITER));

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
        assertThat(dependencyProvider.getUnitRepository().findAll()).containsExactly(truth.toArray(new Unit[0]));
    }
}
