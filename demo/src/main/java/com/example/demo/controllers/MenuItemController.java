package com.example.demo.controllers;

import com.example.demo.dto.AddMenuItemRequest;
import com.example.demo.dto.MenuItemResponse;
import com.example.demo.entities.MenuItem;
import com.example.demo.config.CustomUserDetails;
import com.example.demo.services.MenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @PostMapping
    public ResponseEntity<MenuItemResponse> addItem(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody AddMenuItemRequest request) {

        MenuItem item = MenuItem.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .isVeg(request.isVeg())
                .imageUrl(request.getImageUrl())
                .build();

        MenuItem saved = menuItemService.addItem(restaurantId, principal.getUserId(), item);
        return ResponseEntity.status(HttpStatus.CREATED).body(MenuItemResponse.from(saved));
    }

    @GetMapping
    public ResponseEntity<List<MenuItemResponse>> getMenu(@PathVariable Long restaurantId) {
        List<MenuItemResponse> response = menuItemService.getMenuForRestaurant(restaurantId)
                .stream()
                .map(MenuItemResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{itemId}/toggle-availability")
    public ResponseEntity<MenuItemResponse> toggleAvailability(
            @PathVariable Long restaurantId,
            @PathVariable Long itemId,
            @AuthenticationPrincipal CustomUserDetails principal) {

        MenuItem updated = menuItemService.toggleAvailability(itemId, principal.getUserId());
        return ResponseEntity.ok(MenuItemResponse.from(updated));
    }

    @PatchMapping("/{itemId}/price")
    public ResponseEntity<MenuItemResponse> updatePrice(
            @PathVariable Long restaurantId,
            @PathVariable Long itemId,
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam BigDecimal price) {

        MenuItem updated = menuItemService.updatePrice(itemId, principal.getUserId(), price);
        return ResponseEntity.ok(MenuItemResponse.from(updated));
    }
}