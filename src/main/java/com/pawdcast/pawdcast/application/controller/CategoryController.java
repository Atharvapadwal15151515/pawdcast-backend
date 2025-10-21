package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.model.Category;
import com.pawdcast.pawdcast.application.model.Product;
import com.pawdcast.pawdcast.application.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/main")
    public ResponseEntity<List<Category>> getMainCategories() {
        List<Category> categories = categoryService.getMainCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        Optional<Category> category = categoryService.getCategoryById(id);
        return category.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Integer id) {
        List<Product> products = categoryService.getProductsByCategory(id);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{parentId}/subcategories")
    public ResponseEntity<List<Category>> getSubcategories(@PathVariable Integer parentId) {
        List<Category> subcategories = categoryService.getSubcategories(parentId);
        return ResponseEntity.ok(subcategories);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity.ok(createdCategory);
    }
}