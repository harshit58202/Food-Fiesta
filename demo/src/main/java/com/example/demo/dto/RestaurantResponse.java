package com.example.demo.dto;

import com.example.demo.entities.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;
 
@Data
@AllArgsConstructor
public class RestaurantResponse {
 
    private Long id;
    private Long ownerId;
    private String name;
    private String description;
    private String cuisineType;
    private String addressLine;
    private String city;
    private String pincode;
    private Double avgRating;
    private boolean isOpen;
 
    public static RestaurantResponse from(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getOwner().getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getCuisineType(),
                restaurant.getAddressLine(),
                restaurant.getCity(),
                restaurant.getPincode(),
                restaurant.getAvgRating(),
                restaurant.isOpen()
        );
    }
}
