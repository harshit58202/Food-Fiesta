package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
 
@Data
public class PlaceOrderRequest {
 
    @NotBlank(message = "Delivery address is required")
    private String addressLine;
 
    @NotBlank(message = "City is required")
    private String city;
 
    @NotBlank(message = "Pincode is required")
    private String pincode;
}
