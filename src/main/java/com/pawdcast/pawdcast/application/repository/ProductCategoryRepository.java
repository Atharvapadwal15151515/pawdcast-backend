package com.pawdcast.pawdcast.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pawdcast.pawdcast.application.model.Category;
import com.pawdcast.pawdcast.application.model.Product;
import com.pawdcast.pawdcast.application.model.ProductCategory;
import com.pawdcast.pawdcast.application.model.ProductCategory.ProductCategoryId;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategoryId> {
    
    List<ProductCategory> findByProduct(Product product);
    List<ProductCategory> findByCategory(Category category);
    
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.product.id = :productId")
    List<ProductCategory> findByProductId(@Param("productId") Integer productId);
    
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.category.id = :categoryId")
    List<ProductCategory> findByCategoryId(@Param("categoryId") Integer categoryId);
    
    @Query("SELECT pc.product FROM ProductCategory pc WHERE pc.category.id = :categoryId")
    List<Product> findProductsByCategoryId(@Param("categoryId") Integer categoryId);
    
    @Modifying
    @Query("DELETE FROM ProductCategory pc WHERE pc.product.id = :productId")
    void deleteByProductId(@Param("productId") Integer productId);
    
    @Modifying
    @Query("DELETE FROM ProductCategory pc WHERE pc.product.id = :productId AND pc.category.id = :categoryId")
    void deleteByProductIdAndCategoryId(@Param("productId") Integer productId, @Param("categoryId") Integer categoryId);
    
    boolean existsByProductAndCategory(Product product, Category category);
    
    // Additional useful methods for composite key operations
    Optional<ProductCategory> findById(ProductCategoryId id);
    
    @Modifying
    @Query("DELETE FROM ProductCategory pc WHERE pc.id.productId = :productId AND pc.id.categoryId = :categoryId")
    void deleteByIdProductIdAndIdCategoryId(@Param("productId") Integer productId, @Param("categoryId") Integer categoryId);
    
    boolean existsByIdProductIdAndIdCategoryId(Integer productId, Integer categoryId);
}