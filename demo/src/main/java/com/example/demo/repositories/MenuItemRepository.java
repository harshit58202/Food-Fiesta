package com.example.demo.repositories;
import com.example.demo.entities.MenuItem;

import org.springframework.data.jpa.repository.JpaRepository;
 
import java.util.List;
 
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
 
    List<MenuItem> findByRestaurantId(Long restaurantId);
 
    List<MenuItem> findByRestaurantIdAndIsAvailableTrue(Long restaurantId);
}
