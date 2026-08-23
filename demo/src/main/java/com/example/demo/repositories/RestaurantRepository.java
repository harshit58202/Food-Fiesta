package com.example.demo.repositories;
import com.example.demo.entities.Restaurant;

import org.springframework.data.jpa.repository.JpaRepository;
 
import java.util.List;
import java.util.Optional;
 
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
 
    // Basic discovery: "restaurants near me" via city match for now.
    List<Restaurant> findByCityIgnoreCase(String city);
 
    Optional<Restaurant> findByOwnerId(Long ownerId);
}
