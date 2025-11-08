package com.pawdcast.pawdcast.application.service;

import com.pawdcast.pawdcast.application.model.*;
import com.pawdcast.pawdcast.application.repository.OrderRepository;
import com.pawdcast.pawdcast.application.repository.OrderItemRepository;
import com.pawdcast.pawdcast.application.repository.ProductRepository;
import com.pawdcast.pawdcast.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartService cartService;

    @SuppressWarnings("unused")
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Order createOrder(String shippingAddress, String billingAddress, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<CartItem> cartItems = cartService.getCartItems(user.getEmail());
        if (cartItems.isEmpty()) throw new RuntimeException("Cart is empty");

        // Calculate total and validate inventory
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if (product.getInventoryCount() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient inventory for product: " + product.getName());
            }
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setShippingAddress(shippingAddress);
        order.setBillingAddress(billingAddress);
        order.setCustomerEmail(user.getEmail());
        order.setCustomerPhone(user.getPhone());
        order.setOrderStatus("pending");

        Order savedOrder = orderRepository.save(order);

        // Create order items and reduce inventory
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            
            orderItemRepository.save(orderItem);
            
            // Reduce inventory
            product.setInventoryCount(product.getInventoryCount() - cartItem.getQuantity());
            productRepository.save(product);
        }

        // Clear cart after successful order
        cartService.clearCart(user.getEmail());

        return savedOrder;
    }

    public List<Order> getUserOrders(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Optional<Order> getOrderById(Integer orderId, Integer userId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent() && !order.get().getUser().getId().equals(userId)) {
            throw new RuntimeException("Order not found");
        }
        return order;
    }

    @Transactional
    public Order updateOrderStatus(Integer orderId, String status, Integer userId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) throw new RuntimeException("Order not found");

        Order order = orderOpt.get();
        
        // Verify user owns the order (unless admin)
        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }
        
        order.setOrderStatus(status);
        return orderRepository.save(order);
    }

    @Transactional
    public boolean cancelOrder(Integer orderId, Integer userId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) return false;

        Order order = orderOpt.get();
        
        // Verify user owns the order
        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }
        
        // Only allow cancellation for pending or confirmed orders
        if (order.getOrderStatus().equals("pending") || 
            order.getOrderStatus().equals("confirmed")) {
            
            // Restore inventory
            for (OrderItem item : order.getOrderItems()) {
                Product product = item.getProduct();
                product.setInventoryCount(product.getInventoryCount() + item.getQuantity());
                productRepository.save(product);
            }
            
            order.setOrderStatus("cancelled");
            orderRepository.save(order);
            return true;
        }
        
        return false;
    }
}