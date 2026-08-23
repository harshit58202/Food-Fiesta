package com.example.demo.dto;

import com.example.demo.entities.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;
 
import java.math.BigDecimal;
import java.util.List;
 
@Data
@AllArgsConstructor
public class CartResponse {
 
    private Long id;
    private Long restaurantId;
    private String restaurantName;
    private List<CartItemResponse> items;
    private BigDecimal totalAmount;
 
    public static CartResponse from(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(CartItemResponse::from)
                .toList();
 
        BigDecimal total = itemResponses.stream()
                .map(CartItemResponse::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
 
        return new CartResponse(
                cart.getId(),
                cart.getRestaurant() != null ? cart.getRestaurant().getId() : null,
                cart.getRestaurant() != null ? cart.getRestaurant().getName() : null,
                itemResponses,
                total
        );
    }
}
