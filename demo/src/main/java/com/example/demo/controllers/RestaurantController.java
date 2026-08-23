package com.example.demo.controllers;

import com.example.demo.dto.CreateRestaurantRequest;
import com.example.demo.dto.RestaurantResponse;
import com.example.demo.entities.Restaurant;
import com.example.demo.services.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
 
@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
 
    private final RestaurantService restaurantService;
 
    // ownerId is passed as a param for now since auth/JWT isn't built yet —
    // once it is, this will come from the authenticated principal instead.
    @PostMapping
    public ResponseEntity<RestaurantResponse> create(
            @RequestParam Long ownerId,
            @Valid @RequestBody CreateRestaurantRequest request) {
 
        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .description(request.getDescription())
                .cuisineType(request.getCuisineType())
                .addressLine(request.getAddressLine())
                .city(request.getCity())
                .pincode(request.getPincode())
                .build();
 
        Restaurant saved = restaurantService.createRestaurant(ownerId, restaurant);
        return ResponseEntity.status(HttpStatus.CREATED).body(RestaurantResponse.from(saved));
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(RestaurantResponse.from(restaurantService.getById(id)));
    }
 
    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getAll(
            @RequestParam(required = false) String city) {
 
        List<Restaurant> restaurants = (city != null)
                ? restaurantService.getByCity(city)
                : restaurantService.getAll();
 
        List<RestaurantResponse> response = restaurants.stream()
                .map(RestaurantResponse::from)
                .toList();
 
        return ResponseEntity.ok(response);
    }
 
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<RestaurantResponse> toggleStatus(
            @PathVariable Long id,
            @RequestParam Long ownerId) {
 
        Restaurant updated = restaurantService.toggleOpenStatus(id, ownerId);
        return ResponseEntity.ok(RestaurantResponse.from(updated));
    }
}
