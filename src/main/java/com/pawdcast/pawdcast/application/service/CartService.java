package com.pawdcast.pawdcast.application.service;

import com.pawdcast.pawdcast.application.model.CartItem;
import com.pawdcast.pawdcast.application.model.Product;
import com.pawdcast.pawdcast.application.model.User;
import com.pawdcast.pawdcast.application.repository.CartItemRepository;
import com.pawdcast.pawdcast.application.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @SuppressWarnings("unused")
	@Autowired
    private ProductService productService;

    public List<CartItem> getCartItems(HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) return List.of();
        return cartItemRepository.findByUserId(user.getId());
    }

    @Transactional
    public CartItem addToCart(Integer productId, Integer quantity, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) throw new RuntimeException("User not logged in");

        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) throw new RuntimeException("Product not found");

        Product product = productOpt.get();
        if (product.getInventoryCount() < quantity) {
            throw new RuntimeException("Insufficient inventory");
        }

        Optional<CartItem> existingCartItem = cartItemRepository.findByUserIdAndProductId(user.getId(), productId);
        
        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            return cartItemRepository.save(cartItem);
        } else {
            CartItem newCartItem = new CartItem(user, product, quantity);
            return cartItemRepository.save(newCartItem);
        }
    }

    @Transactional
    public CartItem updateCartItem(Integer productId, Integer quantity, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) throw new RuntimeException("User not logged in");

        Optional<CartItem> cartItemOpt = cartItemRepository.findByUserIdAndProductId(user.getId(), productId);
        if (cartItemOpt.isEmpty()) throw new RuntimeException("Cart item not found");

        CartItem cartItem = cartItemOpt.get();
        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
            return null;
        }

        if (cartItem.getProduct().getInventoryCount() < quantity) {
            throw new RuntimeException("Insufficient inventory");
        }

        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public void removeFromCart(Integer productId, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) throw new RuntimeException("User not logged in");
        
        cartItemRepository.deleteByUserIdAndProductId(user.getId(), productId);
    }

    @Transactional
    public void clearCart(HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) throw new RuntimeException("User not logged in");
        
        cartItemRepository.deleteByUserId(user.getId());
    }

    public Integer getCartItemsCount(HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) return 0;
        
        Integer count = cartItemRepository.getTotalCartItemsCount(user.getId());
        return count != null ? count : 0;
    }

    private User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }
}