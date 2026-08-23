package com.example.demo.repositories;
import com.example.demo.entities.Order;

import org.springframework.data.jpa.repository.JpaRepository;
 
import java.util.List;
 
public interface OrderRepository extends JpaRepository<Order, Long> {
 
    // A customer's order history.
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
 
    // A restaurant owner's incoming orders.
    List<Order> findByRestaurantIdOrderByCreatedAtDesc(Long restaurantId);
}
