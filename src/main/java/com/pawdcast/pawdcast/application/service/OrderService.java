package com.pawdcast.pawdcast.application.service;

import com.pawdcast.pawdcast.application.model.*;
import com.pawdcast.pawdcast.application.repository.OrderRepository;
import com.pawdcast.pawdcast.application.repository.OrderItemRepository;
import com.pawdcast.pawdcast.application.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
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

    @Transactional
    public Order createOrder(String shippingAddress, String billingAddress, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) throw new RuntimeException("User not logged in");

        List<CartItem> cartItems = cartService.getCartItems(session);
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
        cartService.clearCart(session);

        return savedOrder;
    }

    public List<Order> getUserOrders(HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) throw new RuntimeException("User not logged in");
        
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Optional<Order> getOrderById(Integer orderId, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) throw new RuntimeException("User not logged in");
        
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent() && !order.get().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Order not found");
        }
        return order;
    }

    @Transactional
    public Order updateOrderStatus(Integer orderId, String status, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) throw new RuntimeException("User not logged in");

        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) throw new RuntimeException("Order not found");

        Order order = orderOpt.get();
        order.setOrderStatus(status);
        return orderRepository.save(order);
    }

    @Transactional
    public boolean cancelOrder(Integer orderId, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) throw new RuntimeException("User not logged in");

        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) return false;

        Order order = orderOpt.get();
        
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

    private User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }
}