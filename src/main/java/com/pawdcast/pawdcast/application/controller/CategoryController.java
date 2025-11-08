package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.model.Category;
import com.pawdcast.pawdcast.application.model.Product;
import com.pawdcast.pawdcast.application.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // PUBLIC - Get all categories
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // PUBLIC - Get main categories
    @GetMapping("/main")
    public ResponseEntity<List<Category>> getMainCategories() {
        List<Category> categories = categoryService.getMainCategories();
        return ResponseEntity.ok(categories);
    }

    // PUBLIC - Get category by ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        Optional<Category> category = categoryService.getCategoryById(id);
        return category.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    // PUBLIC - Get products by category
    @GetMapping("/{id}/products")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Integer id) {
        List<Product> products = categoryService.getProductsByCategory(id);
        return ResponseEntity.ok(products);
    }

    // PUBLIC - Get subcategories
    @GetMapping("/{parentId}/subcategories")
    public ResponseEntity<List<Category>> getSubcategories(@PathVariable Integer parentId) {
        List<Category> subcategories = categoryService.getSubcategories(parentId);
        return ResponseEntity.ok(subcategories);
    }

    // SECURED - Create category (Admin only)
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category, HttpServletRequest request) {
        // Check authentication
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(401).body("Authentication required");
        }
        
        // Optional: Add admin role check here
        // if (!userService.isAdmin(userEmail)) {
        //     return ResponseEntity.status(403).body("Admin access required");
        // }
        
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity.ok(createdCategory);
    }
}