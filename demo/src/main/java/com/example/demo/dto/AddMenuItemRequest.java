package com.example.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
 
import java.math.BigDecimal;
 
@Data
public class AddMenuItemRequest {
 
    @NotBlank(message = "Name is required")
    private String name;
    private String restaurantName;
 
    private String description;
 
    private String category;
 
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
 
    private boolean isVeg = true;
 
    private String imageUrl;
}
