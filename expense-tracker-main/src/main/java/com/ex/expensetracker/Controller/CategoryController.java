package com.ex.expensetracker.Controller;

import com.ex.expensetracker.Exception.CategoryExistsException;
import com.ex.expensetracker.Exception.CategoryNotFoundException;
import com.ex.expensetracker.Service.CategoryService;
import com.ex.expensetracker.model.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/v1/category")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    // ---------- GET ALL ----------
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getCategories();
    }

    // ---------- GET BY ID ----------
    @GetMapping("/{categoryId}")
    public Category getCategoryById(@PathVariable Long categoryId) {
        try {
            return categoryService.getCategoryById(categoryId);
        } catch (CategoryNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    // ---------- CREATE ----------
    @PostMapping
    public ResponseEntity<Category> addNewCategory(
            @RequestBody Category category) {
        try {
            return ResponseEntity.ok(
                    categoryService.addNewCategory(category));
        } catch (CategoryExistsException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    // ---------- UPDATE ----------
    @PutMapping("/{categoryId}")
    public Category updateCategory(
            @PathVariable Long categoryId,
            @RequestBody Category category) {
        try {
            return categoryService.updateCategory(categoryId, category);
        } catch (CategoryNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    // ---------- DELETE ----------
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable Long categoryId) {
        try {
            categoryService.deleteCategoryById(categoryId);
            return ResponseEntity.ok("Category deleted successfully");
        } catch (CategoryNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}