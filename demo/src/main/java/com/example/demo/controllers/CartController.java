package com.example.demo.controllers;

import com.example.demo.dto.AddToCartRequest;
import com.example.demo.dto.CartResponse;
import com.example.demo.config.CustomUserDetails;
import com.example.demo.services.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> viewCart(@AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(CartResponse.from(cartService.viewCart(principal.getUserId())));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addToCart(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody AddToCartRequest request) {

        var cart = cartService.addToCart(principal.getUserId(), request.getMenuItemId(), request.getQuantity());
        return ResponseEntity.ok(CartResponse.from(cart));
    }

    @DeleteMapping("/items/{menuItemId}")
    public ResponseEntity<CartResponse> removeFromCart(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long menuItemId) {

        var cart = cartService.removeFromCart(principal.getUserId(), menuItemId);
        return ResponseEntity.ok(CartResponse.from(cart));
    }

    @DeleteMapping
    public ResponseEntity<CartResponse> clearCart(@AuthenticationPrincipal CustomUserDetails principal) {
        var cart = cartService.clearCart(principal.getUserId());
        return ResponseEntity.ok(CartResponse.from(cart));
    }
}