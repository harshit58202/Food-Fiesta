package com.example.demo.dto;

import com.example.demo.entities.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
 
@Data
public class UpdateOrderStatusRequest {
 
    @NotNull(message = "status is required")
    private OrderStatus status;
}
