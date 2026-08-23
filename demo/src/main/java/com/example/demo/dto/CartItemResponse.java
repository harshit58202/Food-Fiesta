package com.example.demo.dto;

import com.example.demo.entities.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
 
import java.math.BigDecimal;
 
@Data
@AllArgsConstructor
public class CartItemResponse {
 
    private Long menuItemId;
    private String menuItemName;
    private BigDecimal price;
    private int quantity;
    private BigDecimal lineTotal;
 
    public static CartItemResponse from(CartItem item) {
        BigDecimal lineTotal = item.getMenuItem().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        return new CartItemResponse(
                item.getMenuItem().getId(),
                item.getMenuItem().getName(),
                item.getMenuItem().getPrice(),
                item.getQuantity(),
                lineTotal
        );
    }
}
