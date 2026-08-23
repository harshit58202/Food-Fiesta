package com.example.demo.dto;

import com.example.demo.entities.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
 
@Data
@AllArgsConstructor
public class OrderItemResponse {
 
    private Long menuItemId;
    private String menuItemName;
    private int quantity;
    private BigDecimal priceAtOrderTime;
    private BigDecimal lineTotal;
 
    public static OrderItemResponse from(OrderItem item) {
        BigDecimal lineTotal = item.getPriceAtOrderTime().multiply(BigDecimal.valueOf(item.getQuantity()));
        return new OrderItemResponse(
                item.getMenuItem().getId(),
                item.getMenuItem().getName(),
                item.getQuantity(),
                item.getPriceAtOrderTime(),
                lineTotal
        );
    }
}
