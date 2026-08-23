package com.example.demo.dto;

import com.example.demo.entities.Order;
import com.example.demo.entities.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
 
@Data
@AllArgsConstructor
public class OrderResponse {
 
    private Long id;
    private Long userId;
    private Long restaurantId;
    private String restaurantName;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String deliveryAddressLine;
    private String deliveryCity;
    private String deliveryPincode;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
 
    public static OrderResponse from(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(OrderItemResponse::from)
                .toList();
 
        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getRestaurant().getId(),
                order.getRestaurant().getName(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getDeliveryAddressLine(),
                order.getDeliveryCity(),
                order.getDeliveryPincode(),
                itemResponses,
                order.getCreatedAt()
        );
    }
}
