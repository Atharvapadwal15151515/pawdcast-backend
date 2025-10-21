package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.model.Order;
import com.pawdcast.pawdcast.application.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, String> request, HttpSession session) {
        try {
            String shippingAddress = request.get("shippingAddress");
            String billingAddress = request.get("billingAddress");
            
            if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Shipping address is required");
            }
            if (billingAddress == null || billingAddress.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Billing address is required");
            }
            
            Order order = orderService.createOrder(shippingAddress, billingAddress, session);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserOrders(HttpSession session) {
        try {
            List<Order> orders = orderService.getUserOrders(session);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Integer orderId, HttpSession session) {
        try {
            Optional<Order> order = orderService.getOrderById(orderId, session);
            return order.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Integer orderId, 
                                              @RequestBody Map<String, String> request, 
                                              HttpSession session) {
        try {
            String status = request.get("status");
            if (status == null) {
                return ResponseEntity.badRequest().body("Status is required");
            }
            
            Order order = orderService.updateOrderStatus(orderId, status, session);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Integer orderId, HttpSession session) {
        try {
            boolean cancelled = orderService.cancelOrder(orderId, session);
            Map<String, Object> response = new HashMap<>();
            response.put("success", cancelled);
            response.put("message", cancelled ? "Order cancelled successfully" : "Cannot cancel order");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}