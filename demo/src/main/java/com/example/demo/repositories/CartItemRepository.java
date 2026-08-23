package com.example.demo.repositories;
import com.example.demo.entities.CartItem;

import org.springframework.data.jpa.repository.JpaRepository;
 
import java.util.Optional;
 
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
 
    // Used to check if an item is already in the cart, so we bump quantity
    // instead of creating a duplicate row.
    Optional<CartItem> findByCartIdAndMenuItemId(Long cartId, Long menuItemId);
}
