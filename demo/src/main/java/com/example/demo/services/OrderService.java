package com.example.demo.services;

import com.example.demo.exception.InvalidOperationException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.entities.*;
import com.example.demo.repositories.CartRepository;
import com.example.demo.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
 
@Service
@RequiredArgsConstructor
public class OrderService {
 
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
 
    // Defines which status can move to which next status — prevents e.g. jumping
    // straight from PLACED to DELIVERED, or "un-cancelling" an order.
    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS = new EnumMap<>(OrderStatus.class);
    static {
        ALLOWED_TRANSITIONS.put(OrderStatus.PLACED, EnumSet.of(OrderStatus.CONFIRMED, OrderStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(OrderStatus.CONFIRMED, EnumSet.of(OrderStatus.PREPARING, OrderStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(OrderStatus.PREPARING, EnumSet.of(OrderStatus.OUT_FOR_DELIVERY));
        ALLOWED_TRANSITIONS.put(OrderStatus.OUT_FOR_DELIVERY, EnumSet.of(OrderStatus.DELIVERED));
        ALLOWED_TRANSITIONS.put(OrderStatus.DELIVERED, EnumSet.noneOf(OrderStatus.class));
        ALLOWED_TRANSITIONS.put(OrderStatus.CANCELLED, EnumSet.noneOf(OrderStatus.class));
    }
 
    @Transactional
    public Order placeOrder(Long userId, String addressLine, String city, String pincode) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new InvalidOperationException("Cart is empty"));
 
        if (cart.getItems().isEmpty() || cart.getRestaurant() == null) {
            throw new InvalidOperationException("Cannot place an order with an empty cart");
        }
 
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new java.util.ArrayList<>();
 
        Order order = Order.builder()
                .user(cart.getUser())
                .restaurant(cart.getRestaurant())
                .deliveryAddressLine(addressLine)
                .deliveryCity(city)
                .deliveryPincode(pincode)
                .status(OrderStatus.PLACED)
                .totalAmount(BigDecimal.ZERO) // set below once items are totalled
                .build();
 
        for (CartItem cartItem : cart.getItems()) {
            BigDecimal lineTotal = cartItem.getMenuItem().getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            total = total.add(lineTotal);
 
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .menuItem(cartItem.getMenuItem())
                    .quantity(cartItem.getQuantity())
                    .priceAtOrderTime(cartItem.getMenuItem().getPrice()) // snapshot price now
                    .build();
            orderItems.add(orderItem);
        }
 
        order.setTotalAmount(total);
        order.setItems(orderItems);
 
        Order savedOrder = orderRepository.save(order);
 
        // Cart is consumed once the order is placed.
        cart.getItems().clear();
        cart.setRestaurant(null);
        cartRepository.save(cart);
 
        return savedOrder;
    }
 
    public Order getById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }
 
    public List<Order> getOrderHistory(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
 
    public List<Order> getRestaurantOrders(Long restaurantId, Long requestingOwnerId) {
        Order sample = orderRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId)
                .stream().findFirst().orElse(null);
 
        // Only check ownership if there's at least one order to compare against;
        // otherwise just return the empty list.
        if (sample != null && !sample.getRestaurant().getOwner().getId().equals(requestingOwnerId)) {
            throw new InvalidOperationException("You do not own this restaurant");
        }
 
        return orderRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId);
    }
 
    @Transactional
    public Order updateStatus(Long orderId, Long requestingOwnerId, OrderStatus newStatus) {
        Order order = getById(orderId);
 
        if (!order.getRestaurant().getOwner().getId().equals(requestingOwnerId)) {
            throw new InvalidOperationException("Only the restaurant that received this order can update its status");
        }
 
        Set<OrderStatus> allowedNext = ALLOWED_TRANSITIONS.get(order.getStatus());
        if (!allowedNext.contains(newStatus)) {
            throw new InvalidOperationException(
                    "Cannot move order from " + order.getStatus() + " to " + newStatus);
        }
 
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
 
    @Transactional
    public Order cancelOrder(Long orderId, Long requestingUserId) {
        Order order = getById(orderId);
 
        if (!order.getUser().getId().equals(requestingUserId)) {
            throw new InvalidOperationException("Only the customer who placed this order can cancel it");
        }
 
        Set<OrderStatus> allowedNext = ALLOWED_TRANSITIONS.get(order.getStatus());
        if (!allowedNext.contains(OrderStatus.CANCELLED)) {
            throw new InvalidOperationException("Order can no longer be cancelled — it's already " + order.getStatus());
        }
 
        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }
}