package com.example.demo.controllers;

import com.example.demo.dto.OrderResponse;
import com.example.demo.dto.PlaceOrderRequest;
import com.example.demo.dto.UpdateOrderStatusRequest;
import com.example.demo.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
 
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
 
    private final OrderService orderService;
 
    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @RequestParam Long userId,
            @Valid @RequestBody PlaceOrderRequest request) {
 
        var order = orderService.placeOrder(userId, request.getAddressLine(), request.getCity(), request.getPincode());
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.from(order));
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(OrderResponse.from(orderService.getById(id)));
    }
 
    // Customer's own order history
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders(@RequestParam Long userId) {
        List<OrderResponse> response = orderService.getOrderHistory(userId)
                .stream().map(OrderResponse::from).toList();
        return ResponseEntity.ok(response);
    }
 
    // Restaurant owner's incoming orders
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<OrderResponse>> getRestaurantOrders(
            @PathVariable Long restaurantId,
            @RequestParam Long ownerId) {
 
        List<OrderResponse> response = orderService.getRestaurantOrders(restaurantId, ownerId)
                .stream().map(OrderResponse::from).toList();
        return ResponseEntity.ok(response);
    }
 
    // Restaurant owner advances the order through its lifecycle
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam Long ownerId,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
 
        var order = orderService.updateStatus(id, ownerId, request.getStatus());
        return ResponseEntity.ok(OrderResponse.from(order));
    }
 
    // Customer cancels their own order
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long id,
            @RequestParam Long userId) {
 
        var order = orderService.cancelOrder(id, userId);
        return ResponseEntity.ok(OrderResponse.from(order));
    }
}
