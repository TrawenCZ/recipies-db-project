package cz.muni.fi.pv168.data.manipulation;

import cz.muni.fi.pv168.model.Category;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

public final class JsonCategoryImporterTest extends AbstractJsonImporterTest<Category> {

    protected Importer getImporter() {
        return dependencyProvider.getCategoryImporter();
    }

    @Test
    void wrongDifferentFormat(){
        super.wrongDifferentFormat("multi-units.json");
    }

    @Test
    void singleCategory() {
        Path importFilePath = TEST_RESOURCES.resolve("single-category.json");

        var truth = new ArrayList<>(dependencyProvider.getCategoryRepository().findAll());
        truth.add(new Category("Dřevěné jídla","FF996600"));

        importer.importData(importFilePath.toString());

        var progress = importer.getProgress();
        assertThat(progress.isDone()).isTrue();
        assertThat(progress.getInsert()).isEqualTo(1);
        assertThat(progress.getUpdate()).isEqualTo(0);
        assertThat(progress.getIgnore()).isEqualTo(0);
        assertThat(progress.getRemove()).isEqualTo(0);

        dependencyProvider.getCategoryRepository().refresh();
        assertThat(dependencyProvider.getCategoryRepository().findAll()).containsExactly(truth.toArray(new Category[0]));
    }

    @Test
    void multipleCategories() {
        Path importFilePath = TEST_RESOURCES.resolve("multi-categories.json");

        var truth = new ArrayList<>(dependencyProvider.getCategoryRepository().findAll());
        truth.add(new Category("Dřevěné jídla","FF996600"));
        truth.add(new Category("Železná jídla", "FF666666"));
        truth.add(new Category("Vodové jídla", "FF00CCFF"));

        importer.importData(importFilePath.toString());

        var progress = importer.getProgress();
        assertThat(progress.isDone()).isTrue();
        assertThat(progress.getInsert()).isEqualTo(3);
        assertThat(progress.getUpdate()).isEqualTo(0);
        assertThat(progress.getIgnore()).isEqualTo(0);
        assertThat(progress.getRemove()).isEqualTo(0);

        dependencyProvider.getCategoryRepository().refresh();
        assertThat(dependencyProvider.getCategoryRepository().findAll()).containsExactly(truth.toArray(new Category[0]));
    }

    @Test
    void duplicateCategories() {
        Path importFilePath = TEST_RESOURCES.resolve("multi-categories.json");

        var truth = new ArrayList<>(dependencyProvider.getCategoryRepository().findAll());
        truth.add(new Category("Dřevěné jídla","FF996600"));
        truth.add(new Category("Železná jídla", "FF666666"));
        truth.add(new Category("Vodové jídla", "FF00CCFF"));

        importer.importData(importFilePath.toString());
        assertThatThrownBy(() -> importer.importData(importFilePath.toString()))
            .isInstanceOf(DuplicateException.class);

        var progress = importer.getProgress();
        assertThat(progress.isDone()).isFalse();
        assertThat(progress.getInsert()).isEqualTo(0);
        assertThat(progress.getUpdate()).isEqualTo(0);
        assertThat(progress.getIgnore()).isGreaterThan(0);
        assertThat(progress.getRemove()).isEqualTo(0);

        dependencyProvider.getCategoryRepository().refresh();
        assertThat(dependencyProvider.getCategoryRepository().findAll()).containsExactly(truth.toArray(new Category[0]));
    }
}
