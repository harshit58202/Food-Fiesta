package com.example.demo.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 
import java.math.BigDecimal;
 
@Entity
@Table(name = "menu_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItem {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
 
    @Column(nullable = false)
    private String name;
 
    @Column(length = 1000)
    private String description;
 
    // e.g. "Starters", "Main Course" — plain string instead of a separate table,
    // still enough to group/filter items on the frontend.
    private String category;
 
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
 
    @Builder.Default
    private boolean isVeg = true;
 
    @Builder.Default
    private boolean isAvailable = true;
 
    private String imageUrl;
}