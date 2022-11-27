package cz.muni.fi.pv168.data.importers;

import cz.muni.fi.pv168.data.manipulation.importers.ObjectImporter;
import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.model.Category;
import cz.muni.fi.pv168.model.Nameable;
import cz.muni.fi.pv168.wiring.TestDependencyProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryImportTest {
    private DatabaseManager databaseManager;
    private ObjectImporter<Category> importer;

    @BeforeEach
    void setUp() {
        this.databaseManager = DatabaseManager.createTestInstance(true);
        var dependencyProvider = new TestDependencyProvider(databaseManager);
        this.importer = dependencyProvider.getCategoryImporter();
    }

    @AfterEach
    void tearDown() {
        databaseManager.destroySchema();
    }

    @Test
    @Disabled
    void shouldNotSaveAllCategories() {
//        List<Category> categories = List.of(new Category("soups", Color.orange),
//                new Category("sides", Color.yellow),
//                new Category("bio", Color.black));
//        importer.saveRecords(categories);
//        List<String> saved = importer.findAll().stream().map(Nameable::getName).toList();
//        List<String> toSave = categories.stream().map(Nameable::getName).toList();
//        assertThat(saved).containsAll(toSave);
    }

    @Test
    @Disabled
    void shouldSaveOnlyOnce() {
//        int before = importer.findAll().size();
//        List<Category> categories = List.of(new Category("soups", Color.orange),
//                new Category("soups", Color.orange));
//        importer.saveRecords(categories);
//        System.out.println(importer.findAll());
//        assertThat(importer.findAll().size()).isEqualTo(before + 1);
    }
}
