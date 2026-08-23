package com.example.demo.dto;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
 
@Data
@AllArgsConstructor
public class UserResponse {
 
    private Long id;
    private String name;
    private String email;
    private String phone;
    private Role role;
 
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );
    }
}