package com.example.demo.services;

import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.InvalidOperationException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.entities.Restaurant;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
 
import java.util.List;
 
@Service
@RequiredArgsConstructor
public class RestaurantService {
 
    private final RestaurantRepository restaurantRepository;
    private final UserService userService;
 
    public Restaurant createRestaurant(Long ownerId, Restaurant restaurant) {
        User owner = userService.getById(ownerId);
 
        if (owner.getRole() != Role.RESTAURANT_OWNER) {
            throw new InvalidOperationException("Only users with role RESTAURANT_OWNER can create a restaurant");
        }
 
        if (restaurantRepository.findByOwnerId(ownerId).isPresent()) {
            throw new DuplicateResourceException("This owner already has a registered restaurant");
        }
 
        restaurant.setOwner(owner);
        return restaurantRepository.save(restaurant);
    }
 
    public Restaurant getById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
    }
 
    public List<Restaurant> getByCity(String city) {
        return restaurantRepository.findByCityIgnoreCase(city);
    }
 
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }
 
    public Restaurant toggleOpenStatus(Long restaurantId, Long requestingOwnerId) {
        Restaurant restaurant = getById(restaurantId);
 
        if (!restaurant.getOwner().getId().equals(requestingOwnerId)) {
            throw new InvalidOperationException("Only the owning restaurant owner can change open/closed status");
        }
 
        restaurant.setOpen(!restaurant.isOpen());
        return restaurantRepository.save(restaurant);
    }
}
 
