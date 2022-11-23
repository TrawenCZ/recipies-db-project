package cz.muni.fi.pv168.data.storage.dao;

import cz.muni.fi.pv168.data.storage.DataStorageException;
import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.data.storage.entity.CategoryEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

final class CategoryDaoTest {
    private DatabaseManager manager;
    private CategoryDao categoryDao;
    @BeforeEach
    void setUp() {
        this.manager = DatabaseManager.createTestInstance(true);
        this.categoryDao = new CategoryDao(manager::getConnectionHandler);
    }

    @AfterEach
    void tearDown() {
        manager.destroySchema();
    }

    @Test
    void listAllCategories() {
        var allCategories = categoryDao.findAll();
        assertThat(allCategories)
                .as("There should be 10 testing units")
                .hasSize(10);
    }

    @Test
    void createNewCategory() {
        var params = new CategoryEntity("testing category", "FF1113B2");
        var createdCategory = categoryDao.create(params);
        assertThat(createdCategory)
                .as("There should be a new category created")
                .isNotNull();
        assertThat(createdCategory.id())
                .isGreaterThan(1);

        assertThat(createdCategory.name()).isEqualTo(params.name());
        assertThat(createdCategory.color()).isEqualTo(params.color());

        var foundCreated = categoryDao.findById(createdCategory.id());
        assertThat(foundCreated)
                .as("Created category should be present")
                .isPresent()
                .contains(createdCategory);
    }

    @Test
    void findExistingCategory(){
        var existingCategoryOpt = categoryDao.findById(1);
        assertThat(existingCategoryOpt)
                .isPresent();
    }

    @Test
    void deleteExistingCategoryWithReference() {
        final long id = 1;
        var existingCategoryOpt = categoryDao.findById(id);
        assertThat(existingCategoryOpt)
                .isPresent();
        var existingCategory = existingCategoryOpt.orElseThrow();
        assertThatThrownBy(() -> categoryDao.deleteById(existingCategory.id()))
                .isInstanceOf(DataStorageException.class)
                .hasMessageContaining("Failed to delete category " + id);

    }

    @Test
    void deleteExistingCategoryWithNoReference() {
        var existingCategoryOpt = categoryDao.findById(3);
        assertThat(existingCategoryOpt)
                .isPresent();
        var existingCategory = existingCategoryOpt.orElseThrow();
        assertThatNoException().isThrownBy(() -> categoryDao.deleteById(existingCategory.id()));
        var deletedCategory = categoryDao.findById(existingCategory.id());
        assertThat(deletedCategory).isEmpty();
    }

    @Test
    void updateCategory() {
        var existingCategoryOpt = categoryDao.findById(1);
        assertThat(existingCategoryOpt)
                .isPresent();
        var existingCategory = existingCategoryOpt.orElseThrow();
        String newName = "updated test category";
        String newColor = "FF00AC2D";
        var newCategory = new CategoryEntity(
                existingCategory.id(),
                newName,
                newColor);
        assertThatNoException().isThrownBy(()-> categoryDao.update(newCategory));
        var updatedCategoryOpt = categoryDao.findById(existingCategory.id());
        assertThat(updatedCategoryOpt)
                .isPresent();
        var updatedCategory = updatedCategoryOpt.orElseThrow();
        assertThat(updatedCategory.id()).isEqualTo(existingCategory.id());
        assertThat(updatedCategory.name()).isEqualTo(newName);
        assertThat(updatedCategory.color()).isEqualTo(newColor);
    }
}
