package com.pawdcast.pawdcast.application.repository;

import com.pawdcast.pawdcast.application.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);
    List<Category> findByParentIsNull(); // Main categories only
    List<Category> findByParentId(Integer parentId); // Subcategories
    
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL ORDER BY c.name")
    List<Category> findAllMainCategories();
}