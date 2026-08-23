package com.example.demo.controllers;

import com.example.demo.dto.AddToCartRequest;
import com.example.demo.dto.CartResponse;
import com.example.demo.services.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
 
    private final CartService cartService;
 
    @GetMapping
    public ResponseEntity<CartResponse> viewCart(@RequestParam Long userId) {
        return ResponseEntity.ok(CartResponse.from(cartService.viewCart(userId)));
    }
 
    @PostMapping("/items")
    public ResponseEntity<CartResponse> addToCart(
            @RequestParam Long userId,
            @Valid @RequestBody AddToCartRequest request) {
 
        var cart = cartService.addToCart(userId, request.getMenuItemId(), request.getQuantity());
        return ResponseEntity.ok(CartResponse.from(cart));
    }
 
    @DeleteMapping("/items/{menuItemId}")
    public ResponseEntity<CartResponse> removeFromCart(
            @RequestParam Long userId,
            @PathVariable Long menuItemId) {
 
        var cart = cartService.removeFromCart(userId, menuItemId);
        return ResponseEntity.ok(CartResponse.from(cart));
    }
 
    @DeleteMapping
    public ResponseEntity<CartResponse> clearCart(@RequestParam Long userId) {
        var cart = cartService.clearCart(userId);
        return ResponseEntity.ok(CartResponse.from(cart));
    }
}
