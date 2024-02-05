package com.example.cafe.repository;

import com.example.cafe.model.entity.CategoryEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CategoryRepositoryIntegrationTest {

    private final Integer categoryId = 1;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void findAllCategories() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        assertEquals(10, categories.size());
    }

    @Test
    public void findCategory() {
        final Optional<CategoryEntity> categoryWrapper = categoryRepository.findById(1);
        assertTrue(categoryWrapper.isPresent());

        final CategoryEntity category = categoryWrapper.get();
        assertEquals("Cafe", category.getName());
        assertEquals(1, category.getId());
    }

    @Test
    public void findNotExistentCategory() {
        final Optional<CategoryEntity> categoryWrapper = categoryRepository.findById(-1);
        assertTrue(categoryWrapper.isEmpty());
    }

    @Test
    @Transactional
    @DirtiesContext
    public void createCategory() {
        CategoryEntity category = new CategoryEntity();

        category.setName("Drinks test");

        final Integer savedCategoryId = categoryRepository.save(category).getId();

        CategoryEntity savedCategory = categoryRepository.findById(savedCategoryId).get();
        assertEquals(category.getName(), savedCategory.getName());
    }

    @Test
    @DirtiesContext
    public void updateCategory() {
        final Optional<CategoryEntity> categoryWrapper = categoryRepository.findById(categoryId);
        assertTrue(categoryWrapper.isPresent());

        final CategoryEntity category = categoryWrapper.get();

        category.setName("Test name");

        categoryRepository.save(category);

        CategoryEntity updatedCategory = categoryRepository.findById(categoryId).get();
        assertEquals("Test name", updatedCategory.getName());
    }

    @Test
    @DirtiesContext
    public void deleteCategory() {
        assertTrue(categoryRepository.findById(categoryId).isPresent());
        categoryRepository.deleteById(categoryId);
        assertTrue(categoryRepository.findById(categoryId).isEmpty());
    }

}