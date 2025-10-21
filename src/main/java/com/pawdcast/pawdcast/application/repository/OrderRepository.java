package com.pawdcast.pawdcast.application.repository;

import com.pawdcast.pawdcast.application.model.Order;
import com.pawdcast.pawdcast.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
    List<Order> findByUser_IdOrderByCreatedAtDesc(Integer userId);
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.orderStatus = :status ORDER BY o.createdAt DESC")
    List<Order> findByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") String status);
    
    List<Order> findByOrderStatusOrderByCreatedAtAsc(String status);
    
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.createdAt DESC")
    List<Order> findRecentOrdersByUserId(@Param("userId") Integer userId);
}