package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.model.CartItem;
import com.pawdcast.pawdcast.application.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<?> getCart(HttpServletRequest request) {
        try {
            // Get user from JWT token
            String userEmail = (String) request.getAttribute("userEmail");
            if (userEmail == null) {
                return ResponseEntity.status(401).body("Authentication required");
            }
            
            List<CartItem> cartItems = cartService.getCartItems(userEmail);
            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) {
        try {
            // Get user from JWT token
            String userEmail = (String) request.getAttribute("userEmail");
            if (userEmail == null) {
                return ResponseEntity.status(401).body("Authentication required");
            }
            
            Integer productId = (Integer) requestBody.get("productId");
            Integer quantity = (Integer) requestBody.get("quantity");
            
            CartItem cartItem = cartService.addToCart(productId, quantity, userEmail);
            return ResponseEntity.ok(cartItem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCartItem(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) {
        try {
            // Get user from JWT token
            String userEmail = (String) request.getAttribute("userEmail");
            if (userEmail == null) {
                return ResponseEntity.status(401).body("Authentication required");
            }
            
            Integer productId = (Integer) requestBody.get("productId");
            Integer quantity = (Integer) requestBody.get("quantity");
            
            CartItem cartItem = cartService.updateCartItem(productId, quantity, userEmail);
            return ResponseEntity.ok(cartItem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Integer productId, HttpServletRequest request) {
        try {
            // Get user from JWT token
            String userEmail = (String) request.getAttribute("userEmail");
            if (userEmail == null) {
                return ResponseEntity.status(401).body("Authentication required");
            }
            
            cartService.removeFromCart(productId, userEmail);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(HttpServletRequest request) {
        try {
            // Get user from JWT token
            String userEmail = (String) request.getAttribute("userEmail");
            if (userEmail == null) {
                return ResponseEntity.status(401).body("Authentication required");
            }
            
            cartService.clearCart(userEmail);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCartCount(HttpServletRequest request) {
        try {
            // Get user from JWT token
            String userEmail = (String) request.getAttribute("userEmail");
            if (userEmail == null) {
                return ResponseEntity.status(401).body("Authentication required");
            }
            
            Integer count = cartService.getCartItemsCount(userEmail);
            Map<String, Integer> response = new HashMap<>();
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}