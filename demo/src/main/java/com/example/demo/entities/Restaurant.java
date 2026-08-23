package com.example.demo.entities;

 
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
 
@Entity
@Table(name = "restaurants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false, unique = true)
    private User owner;
 
    @Column(nullable = false)
    private String name;
 
    @Column(length = 1000)
    private String description;
 
    // e.g. "North Indian", "Italian", "Chinese"
    private String cuisineType;
 
    @Column(nullable = false)
    private String addressLine;
 
    @Column(nullable = false)
    private String city;
 
    @Column(nullable = false)
    private String pincode;
 
    @Builder.Default
    private Double avgRating = 0.0;
 
    @Builder.Default
    private boolean isOpen = true;
 
    @Builder.Default
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItem> menuItems = new ArrayList<>();
 
    @Column(updatable = false)
    private LocalDateTime createdAt;
 
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
