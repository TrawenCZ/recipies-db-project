package cz.muni.fi.pv168.data.storage.repository;

import cz.muni.fi.pv168.data.storage.DataStorageException;
import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.model.Category;
import cz.muni.fi.pv168.wiring.TestDependencyProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


final class CategoryRepositoryTest {
    private DatabaseManager databaseManager;
    private Repository<Category> categoryRepository;

    @BeforeEach
    void setUp() {
        this.databaseManager = DatabaseManager.createTestInstance(true);
        var dependencyProvider = new TestDependencyProvider(databaseManager);
        this.categoryRepository = dependencyProvider.getCategoryRepository();
    }

    @AfterEach
    void tearDown() {
        databaseManager.destroySchema();
    }

    @Test
    void listAllTestingCategories() {
        List<Category> categories = categoryRepository
                .findAll();

        assertThat(categories).hasSize(10);
    }

    @Test
    void findCategoryByIndex() {
        Optional<Category> storedCategory = categoryRepository
                .findByIndex(1);

        assertThat(storedCategory).isPresent();
    }

    @Test
    void findCategoryByIndexShouldFailForIndexTooHigh() {
        Optional<Category> storedCategory = categoryRepository
                .findByIndex(1000);

        assertThat(storedCategory).isEmpty();
    }

    @Test
    void findCategoryById() {
        Optional<Category> storedCategory = categoryRepository
                .findById(1);

        assertThat(storedCategory).isPresent();
    }

    @Test
    void findCategoryByIndexShouldFailForIdTooHigh() {
        Optional<Category> storedCategory = categoryRepository
                .findById(1000);

        assertThat(storedCategory).isEmpty();
    }

    @Test
    void findCategoryByName() {
        Optional<Category> storedCategoryOpt = categoryRepository.
                findByName("vegan");

        assertThat(storedCategoryOpt).isPresent();
        var storedCategory = storedCategoryOpt.orElseThrow();
        assertThat(storedCategory.getName()).isEqualTo("vegan");
        assertThat(storedCategory.getColor()).isEqualTo(new Color(0x287800));
    }

    @Test
    void findCategoryByNameShouldFailForNonExistingName() {
        Optional<Category> storedCategory = categoryRepository.
                findByName("non existing category");

        assertThat(storedCategory).isEmpty();
    }

    @Test
    void createCategory() {
        final var name = "Staročeská kuchyně";
        final var color = new Color(0xEEEE11);
        Category categoryToCreate = new Category(name, color);
        categoryRepository.create(categoryToCreate);

        Category storedCategory = categoryRepository
                .findByIndex(categoryRepository.getSize() - 1)
                .orElseThrow();

        assertThat(storedCategory.getId()).isNotNull();
        assertThat(storedCategory.getName()).isEqualTo("Staročeská kuchyně");
        assertThat(storedCategory.getColor()).isEqualTo(color);
    }

    @Test
    void createExistingCategory() {
        // test data should contain at least one category
        assertThat(categoryRepository.findAll()).isNotEmpty();
        final Category existing = categoryRepository.findAll().get(0);

        // creation should throw error
        assertThatThrownBy(() -> categoryRepository.create(existing))
            .isInstanceOf(DataStorageException.class)
            .hasMessageContaining("Failed to store:");

        // check for no changes
        assertThat(categoryRepository.findAll()).isNotEmpty();
        final Category check = categoryRepository.findAll().get(0);
        assertThat(check).isEqualTo(existing);
    }

    @Test
    void createCategoryUncategorized() {
        categoryRepository.create(Category.UNCATEGORIZED);
        assertThat(categoryRepository.findByName(Category.UNCATEGORIZED.getName())).isEmpty();
    }

    @Test
    void updateCategory() {
        var category = categoryRepository.findAll()
                .stream()
                .filter(e -> e.getName().equals("vegan"))
                .findFirst()
                .orElseThrow();

        category.setName("Updated Vegan CATEGORY");
        category.setColor(new Color(0xFFFFFF));

        categoryRepository.update(category);
        assertThat(categoryRepository.findByName("vegan")).isEmpty();
        Category updatedCategory = categoryRepository.findById(category.getId()).orElseThrow();

        assertThat(updatedCategory.getName()).isEqualTo("Updated Vegan CATEGORY");
        assertThat(updatedCategory.getColor()).isEqualTo(new Color(0xFFFFFF));
    }

    @Test
    void updateNullIdCategory() {
        assertThatThrownBy(() -> categoryRepository.update(
            new Category(null, "SOME NAME", new Color(0xFFFFFF))
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    void updateNonExistentCategory() {
        long id = 1;
        while (categoryRepository.findById(id).isPresent()) {
            id++;
        }
        final Category category = new Category("NON EXISTENT", new Color(0xFFFFFF));
        category.setId(id);

        assertThatThrownBy(() -> categoryRepository.update(category))
            .isInstanceOf(DataStorageException.class)
            .hasMessageContaining("Failed to update non-existing");
    }

    @Test
    void deleteCategoryReferenced() {
        var categories = categoryRepository.findAll();
        for (int i = 0 ; i < categories.size() ; i++ ) {
            if (categories.get(i).getName().equals("pokrm")) {
                int deleteIndex = i; // lambda
                String errorIndex =  String.valueOf(deleteIndex + 1);
                assertThatThrownBy(() -> categoryRepository.deleteByIndex(deleteIndex))
                        .isInstanceOf(DataStorageException.class)
                        .hasMessageContaining("Failed to delete category " + errorIndex);
                return;
            }
        }
    }

    @Test
    void deleteCategoryNonReferenced() {
        var categories = categoryRepository.findAll();
        for (int i = 0 ; i < categories.size() ; i++ ) {
            if (categories.get(i).getName().equals("pití")) {
                int deleteIndex = i;
                assertThatNoException().isThrownBy(() -> categoryRepository.deleteByIndex(deleteIndex));
                assertThat(categoryRepository.findByName("pití")).isEmpty();
                return;
            }
        }
    }
}
