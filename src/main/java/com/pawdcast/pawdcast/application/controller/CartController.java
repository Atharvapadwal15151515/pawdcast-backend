package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.model.CartItem;
import com.pawdcast.pawdcast.application.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<?> getCart(HttpSession session) {
        try {
            List<CartItem> cartItems = cartService.getCartItems(session);
            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Object> request, HttpSession session) {
        try {
            Integer productId = (Integer) request.get("productId");
            Integer quantity = (Integer) request.get("quantity");
            
            CartItem cartItem = cartService.addToCart(productId, quantity, session);
            return ResponseEntity.ok(cartItem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCartItem(@RequestBody Map<String, Object> request, HttpSession session) {
        try {
            Integer productId = (Integer) request.get("productId");
            Integer quantity = (Integer) request.get("quantity");
            
            CartItem cartItem = cartService.updateCartItem(productId, quantity, session);
            return ResponseEntity.ok(cartItem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Integer productId, HttpSession session) {
        try {
            cartService.removeFromCart(productId, session);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(HttpSession session) {
        try {
            cartService.clearCart(session);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCartCount(HttpSession session) {
        try {
            Integer count = cartService.getCartItemsCount(session);
            Map<String, Integer> response = new HashMap<>();
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}