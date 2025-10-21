package com.pawdcast.pawdcast.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pawdcast.pawdcast.application.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    
    // Basic CRUD operations are provided by JpaRepository
    
    // Find by SKU
    Optional<Product> findBySku(String sku);
    
    // Find active products
    List<Product> findByIsActiveTrue();
    
    // Search by name (case insensitive) - only active products
    List<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);
    
    // Find available products (in stock and active)
    @Query("SELECT p FROM Product p WHERE p.inventoryCount > 0 AND p.isActive = true")
    List<Product> findAvailableProducts();
    
    // Find products with inventory greater than specified count
    List<Product> findByInventoryCountGreaterThanAndIsActiveTrue(int count);
    
    // Find out-of-stock products
    @Query("SELECT p FROM Product p WHERE p.inventoryCount <= 0 AND p.isActive = true")
    List<Product> findOutOfStockProducts();
    
    // Find products by price range
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.isActive = true")
    List<Product> findByPriceRange(@Param("minPrice") java.math.BigDecimal minPrice, 
                                   @Param("maxPrice") java.math.BigDecimal maxPrice);
    
    // Find products with images
    @Query("SELECT p FROM Product p WHERE p.imageUrl IS NOT NULL AND p.imageUrl != '' AND p.isActive = true")
    List<Product> findProductsWithImages();
    
    // Find products without images
    @Query("SELECT p FROM Product p WHERE (p.imageUrl IS NULL OR p.imageUrl = '') AND p.isActive = true")
    List<Product> findProductsWithoutImages();
    
    // Update inventory count
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.inventoryCount = :newCount WHERE p.id = :productId")
    int updateInventoryCount(@Param("productId") Integer productId, @Param("newCount") Integer newCount);
    
    // Update product image URL
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.imageUrl = :imageUrl WHERE p.id = :productId")
    int updateProductImage(@Param("productId") Integer productId, @Param("imageUrl") String imageUrl);
    
    // Soft delete product (set isActive to false)
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.isActive = false WHERE p.id = :productId")
    int softDeleteProduct(@Param("productId") Integer productId);
    
    // Restore soft-deleted product
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.isActive = true WHERE p.id = :productId")
    int restoreProduct(@Param("productId") Integer productId);
    
    // Find products with pagination (you might need Pageable import)
    // List<Product> findByIsActiveTrue(org.springframework.data.domain.Pageable pageable);
    
    // Count active products
    long countByIsActiveTrue();
    
    // Count available products (in stock)
    @Query("SELECT COUNT(p) FROM Product p WHERE p.inventoryCount > 0 AND p.isActive = true")
    long countAvailableProducts();
    
    // Check if SKU exists (excluding a specific product for updates)
    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE p.sku = :sku AND p.id != :excludeId")
    boolean existsBySkuAndIdNot(@Param("sku") String sku, @Param("excludeId") Integer excludeId);
    
    // Find products created after a specific date
    List<Product> findByCreatedAtAfterAndIsActiveTrue(java.time.LocalDateTime date);
}