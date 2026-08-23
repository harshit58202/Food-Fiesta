package com.example.demo.services;

import com.example.demo.exception.InvalidOperationException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.entities.MenuItem;
import com.example.demo.entities.Restaurant;
import com.example.demo.repositories.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
 
import java.util.List;
 
@Service
@RequiredArgsConstructor
public class MenuItemService {
 
    private final MenuItemRepository menuItemRepository;
    private final RestaurantService restaurantService;
 
    public MenuItem addItem(Long restaurantId, Long requestingOwnerId, MenuItem item) {
        Restaurant restaurant = restaurantService.getById(restaurantId);
        assertOwnership(restaurant, requestingOwnerId);
 
        item.setRestaurant(restaurant);
        return menuItemRepository.save(item);
    }
 
    public List<MenuItem> getMenuForRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }
 
    public MenuItem getById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
    }
 
    public MenuItem toggleAvailability(Long itemId, Long requestingOwnerId) {
        MenuItem item = getById(itemId);
        assertOwnership(item.getRestaurant(), requestingOwnerId);
 
        item.setAvailable(!item.isAvailable());
        return menuItemRepository.save(item);
    }
 
    public MenuItem updatePrice(Long itemId, Long requestingOwnerId, java.math.BigDecimal newPrice) {
        MenuItem item = getById(itemId);
        assertOwnership(item.getRestaurant(), requestingOwnerId);
 
        item.setPrice(newPrice);
        return menuItemRepository.save(item);
    }
 
    private void assertOwnership(Restaurant restaurant, Long requestingOwnerId) {
        if (!restaurant.getOwner().getId().equals(requestingOwnerId)) {
            throw new InvalidOperationException("You do not own this restaurant");
        }
    }
}