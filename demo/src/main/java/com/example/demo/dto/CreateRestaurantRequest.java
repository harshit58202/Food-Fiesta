package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data
;
 
@Data
public class CreateRestaurantRequest {
 
    @NotBlank(message = "Name is required")
    private String name;
 
    private String description;
 
    private String cuisineType;
 
    @NotBlank(message = "Address is required")
    private String addressLine;
 
    @NotBlank(message = "City is required")
    private String city;
 
    @NotBlank(message = "Pincode is required")
    private String pincode;
}
