package com.pawdcast.pawdcast.application.repository;

import com.pawdcast.pawdcast.application.model.Order;
import com.pawdcast.pawdcast.application.model.OrderItem;
import com.pawdcast.pawdcast.application.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrder(Order order);
    List<OrderItem> findByProduct(Product product);
    
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId")
    List<OrderItem> findByOrderId(@Param("orderId") Integer orderId);
    
    @Query("SELECT oi.product, SUM(oi.quantity) as totalSold FROM OrderItem oi GROUP BY oi.product ORDER BY totalSold DESC")
    List<Object[]> findTopSellingProducts();
}