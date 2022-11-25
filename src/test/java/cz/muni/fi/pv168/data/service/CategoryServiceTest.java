package cz.muni.fi.pv168.data.service;

import cz.muni.fi.pv168.data.manipulation.services.Service;
import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.model.Category;
import cz.muni.fi.pv168.model.Nameable;
import cz.muni.fi.pv168.wiring.TestDependencyProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryServiceTest {
    private DatabaseManager databaseManager;
    private Service<Category> categoryService;

    @BeforeEach
    void setUp() {
        this.databaseManager = DatabaseManager.createTestInstance(true);
        var dependencyProvider = new TestDependencyProvider(databaseManager);
        this.categoryService = dependencyProvider.getCategoryService();
    }

    @AfterEach
    void tearDown() {
        databaseManager.destroySchema();
    }

    @Test
    void shouldNotSaveAllCategories() {
        List<Category> categories = List.of(new Category("soups", Color.orange),
                new Category("sides", Color.yellow),
                new Category("bio", Color.black));
        categoryService.saveRecords(categories);
        List<String> saved = categoryService.findAll().stream().map(Nameable::getName).toList();
        List<String> toSave = categories.stream().map(Nameable::getName).toList();
        assertThat(saved).containsAll(toSave);
    }

    @Test
    void shouldSaveOnlyOnce() {
        int before = categoryService.findAll().size();
        List<Category> categories = List.of(new Category("soups", Color.orange),
                new Category("soups", Color.orange));
        categoryService.saveRecords(categories);
        System.out.println(categoryService.findAll());
        assertThat(categoryService.findAll().size()).isEqualTo(before + 1);
    }
}
