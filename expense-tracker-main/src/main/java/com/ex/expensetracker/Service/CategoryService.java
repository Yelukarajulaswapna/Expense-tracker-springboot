package com.ex.expensetracker.Service;

import com.ex.expensetracker.Exception.CategoryExistsException;
import com.ex.expensetracker.Exception.CategoryNotFoundException;
import com.ex.expensetracker.Repository.CategoryRepository;
import com.ex.expensetracker.model.Category;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 🔐 get logged-in user
    private String currentUser() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    // ---------- READ ALL (ONLY OWN) ----------
    public List<Category> getCategories() {
        return categoryRepository.findByUsername(currentUser());
    }

    // ---------- READ BY ID (ONLY OWN) ----------
    public Category getCategoryById(Long categoryId)
            throws CategoryNotFoundException {

        return categoryRepository
                .findByIdAndUsername(categoryId, currentUser())
                .orElseThrow(() ->
                        new CategoryNotFoundException("Category not found or not yours"));
    }

    // ---------- CREATE ----------
    public Category addNewCategory(Category category)
            throws CategoryExistsException {

        Optional<Category> existing =
                categoryRepository.findByNameAndUsername(
                        category.getName(), currentUser());

        if (existing.isPresent()) {
            throw new CategoryExistsException("Category already exists");
        }

        category.setUsername(currentUser());
        return categoryRepository.save(category);
    }

    // ---------- UPDATE ----------
    @Transactional
    public Category updateCategory(Long categoryId, Category updatedCategory)
            throws CategoryNotFoundException {

        Category category = categoryRepository
                .findByIdAndUsername(categoryId, currentUser())
                .orElseThrow(() ->
                        new CategoryNotFoundException("Category not found or not yours"));

        category.setName(updatedCategory.getName());
        category.setDescription(updatedCategory.getDescription());

        return categoryRepository.save(category);
    }

    // ---------- DELETE ----------
    public void deleteCategoryById(Long categoryId)
            throws CategoryNotFoundException {

        Category category = categoryRepository
                .findByIdAndUsername(categoryId, currentUser())
                .orElseThrow(() ->
                        new CategoryNotFoundException("Category not found or not yours"));

        categoryRepository.delete(category);
    }
}