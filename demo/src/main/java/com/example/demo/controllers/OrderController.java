package com.example.demo.controllers;

import com.example.demo.dto.OrderResponse;
import com.example.demo.dto.PlaceOrderRequest;
import com.example.demo.dto.UpdateOrderStatusRequest;
import com.example.demo.config.CustomUserDetails;
import com.example.demo.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody PlaceOrderRequest request) {

        var order = orderService.placeOrder(principal.getUserId(), request.getAddressLine(), request.getCity(), request.getPincode());
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.from(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(OrderResponse.from(orderService.getById(id)));
    }

    // Customer's own order history
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders(@AuthenticationPrincipal CustomUserDetails principal) {
        List<OrderResponse> response = orderService.getOrderHistory(principal.getUserId())
                .stream().map(OrderResponse::from).toList();
        return ResponseEntity.ok(response);
    }

    // Restaurant owner's incoming orders
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<OrderResponse>> getRestaurantOrders(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails principal) {

        List<OrderResponse> response = orderService.getRestaurantOrders(restaurantId, principal.getUserId())
                .stream().map(OrderResponse::from).toList();
        return ResponseEntity.ok(response);
    }

    // Restaurant owner advances the order through its lifecycle
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody UpdateOrderStatusRequest request) {

        var order = orderService.updateStatus(id, principal.getUserId(), request.getStatus());
        return ResponseEntity.ok(OrderResponse.from(order));
    }

    // Customer cancels their own order
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails principal) {

        var order = orderService.cancelOrder(id, principal.getUserId());
        return ResponseEntity.ok(OrderResponse.from(order));
    }
}