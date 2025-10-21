package com.pawdcast.pawdcast.application.service;

import com.pawdcast.pawdcast.application.model.Product;
import com.pawdcast.pawdcast.application.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findByIsActiveTrue();
    }

    public List<Product> getAvailableProducts() {
        return productRepository.findAvailableProducts();
    }

    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(query);
    }

    public Product createProduct(Product product) {
        // Check if SKU already exists
        if (product.getSku() != null && productRepository.findBySku(product.getSku()).isPresent()) {
            throw new RuntimeException("SKU already exists: " + product.getSku());
        }
        return productRepository.save(product);
    }

    public Product updateProduct(Integer id, Product productDetails) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            
            // Check if SKU is being changed and if it already exists
            if (productDetails.getSku() != null && 
                !productDetails.getSku().equals(product.getSku()) &&
                productRepository.existsBySkuAndIdNot(productDetails.getSku(), id)) {
                throw new RuntimeException("SKU already exists: " + productDetails.getSku());
            }
            
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            product.setSku(productDetails.getSku());
            product.setInventoryCount(productDetails.getInventoryCount());
            product.setIsActive(productDetails.getIsActive());
            
            // Only update image URL if provided
            if (productDetails.getImageUrl() != null && !productDetails.getImageUrl().isEmpty()) {
                product.setImageUrl(productDetails.getImageUrl());
            }
            
            return productRepository.save(product);
        }
        return null;
    }

    public void deleteProduct(Integer id) {
        productRepository.softDeleteProduct(id);
    }

    public boolean reduceInventory(Integer productId, Integer quantity) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            if (product.getInventoryCount() >= quantity) {
                product.setInventoryCount(product.getInventoryCount() - quantity);
                productRepository.save(product);
                return true;
            }
        }
        return false;
    }

    // New service methods using updated repository
    public List<Product> getProductsWithImages() {
        return productRepository.findProductsWithImages();
    }

    public List<Product> getProductsWithoutImages() {
        return productRepository.findProductsWithoutImages();
    }

    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceRange(minPrice, maxPrice);
    }

    public List<Product> getOutOfStockProducts() {
        return productRepository.findOutOfStockProducts();
    }

    public void updateProductImage(Integer productId, String imageUrl) {
        productRepository.updateProductImage(productId, imageUrl);
    }

    public void restoreProduct(Integer productId) {
        productRepository.restoreProduct(productId);
    }

    public long getActiveProductCount() {
        return productRepository.countByIsActiveTrue();
    }

    public long getAvailableProductCount() {
        return productRepository.countAvailableProducts();
    }
}