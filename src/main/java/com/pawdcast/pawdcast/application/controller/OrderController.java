package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.model.Order;
import com.pawdcast.pawdcast.application.service.OrderService;
import com.pawdcast.pawdcast.application.service.AuthService;
import com.pawdcast.pawdcast.application.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private AuthService authService;

    private User getCurrentUser(HttpServletRequest request) {
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            throw new RuntimeException("User not authenticated");
        }
        return authService.findByEmail(userEmail);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, String> request, HttpServletRequest servletRequest) {
        try {
            User user = getCurrentUser(servletRequest);
            String shippingAddress = request.get("shippingAddress");
            String billingAddress = request.get("billingAddress");
            
            if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Shipping address is required");
            }
            if (billingAddress == null || billingAddress.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Billing address is required");
            }
            
            Order order = orderService.createOrder(shippingAddress, billingAddress, user.getId());
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserOrders(HttpServletRequest request) {
        try {
            User user = getCurrentUser(request);
            List<Order> orders = orderService.getUserOrders(user.getId());
            return ResponseEntity.ok(orders);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Integer orderId, HttpServletRequest request) {
        try {
            User user = getCurrentUser(request);
            Optional<Order> order = orderService.getOrderById(orderId, user.getId());
            return order.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Integer orderId, 
                                              @RequestBody Map<String, String> request, 
                                              HttpServletRequest servletRequest) {
        try {
            User user = getCurrentUser(servletRequest);
            String status = request.get("status");
            if (status == null) {
                return ResponseEntity.badRequest().body("Status is required");
            }
            
            Order order = orderService.updateOrderStatus(orderId, status, user.getId());
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Integer orderId, HttpServletRequest request) {
        try {
            User user = getCurrentUser(request);
            boolean cancelled = orderService.cancelOrder(orderId, user.getId());
            Map<String, Object> response = new HashMap<>();
            response.put("success", cancelled);
            response.put("message", cancelled ? "Order cancelled successfully" : "Cannot cancel order");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}