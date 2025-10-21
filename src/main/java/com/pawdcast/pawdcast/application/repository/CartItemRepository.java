package com.pawdcast.pawdcast.application.repository;

import com.pawdcast.pawdcast.application.model.CartItem;
import com.pawdcast.pawdcast.application.model.Product;
import com.pawdcast.pawdcast.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByUserOrderByCreatedAtDesc(User user);
    List<CartItem> findByUser_IdOrderByCreatedAtDesc(Integer userId);
    
    Optional<CartItem> findByUserAndProduct(User user, Product product);
    Optional<CartItem> findByUserIdAndProductId(Integer userId, Integer productId);
    
    @Query("SELECT SUM(ci.quantity) FROM CartItem ci WHERE ci.user.id = :userId")
    Integer getTotalCartItemsCount(@Param("userId") Integer userId);
    
    @Query("SELECT ci FROM CartItem ci WHERE ci.user.id = :userId")
    List<CartItem> findByUserId(@Param("userId") Integer userId);
    
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.user.id = :userId")
    void deleteByUserId(@Param("userId") Integer userId);
    
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.user.id = :userId AND ci.product.id = :productId")
    void deleteByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);
    
    boolean existsByUserAndProduct(User user, Product product);
}