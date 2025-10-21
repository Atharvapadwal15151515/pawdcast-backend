package com.pawdcast.pawdcast.application.service;

import com.pawdcast.pawdcast.application.model.Category;
import com.pawdcast.pawdcast.application.model.Product;
import com.pawdcast.pawdcast.application.repository.CategoryRepository;
import com.pawdcast.pawdcast.application.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getMainCategories() {
        return categoryRepository.findByParentIsNull();
    }

    public List<Category> getSubcategories(Integer parentId) {
        return categoryRepository.findByParentId(parentId);
    }

    public Optional<Category> getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Product> getProductsByCategory(Integer categoryId) {
        return productCategoryRepository.findProductsByCategoryId(categoryId);
    }

    public void addProductToCategory(Integer productId, Integer categoryId) {
        // Implementation would require Product and Category entities
        // This is a simplified version
    }
}