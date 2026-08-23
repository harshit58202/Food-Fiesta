package com.example.demo.dto;

import com.example.demo.entities.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Data;
 
import java.math.BigDecimal;
 
@Data
@AllArgsConstructor
public class MenuItemResponse {
 
    private Long id;
    private Long restaurantId;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private boolean isVeg;
    private boolean isAvailable;
    private String imageUrl;
 
    public static MenuItemResponse from(MenuItem item) {
        return new MenuItemResponse(
                item.getId(),
                item.getRestaurant().getId(),
                item.getName(),
                item.getDescription(),
                item.getCategory(),
                item.getPrice(),
                item.isVeg(),
                item.isAvailable(),
                item.getImageUrl()
        );
    }
}
