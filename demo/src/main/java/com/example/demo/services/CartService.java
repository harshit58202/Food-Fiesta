package com.example.demo.services;

import com.example.demo.exception.InvalidOperationException;
import com.example.demo.entities.Cart;
import com.example.demo.entities.CartItem;
import com.example.demo.entities.MenuItem;
import com.example.demo.entities.User;
import com.example.demo.repositories.CartItemRepository;
import com.example.demo.repositories.CartRepository;
import com.example.demo.services.MenuItemService;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
@Service
@RequiredArgsConstructor
public class CartService {
 
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MenuItemService menuItemService;
    private final UserService userService;
 
    @Transactional
    public Cart addToCart(Long userId, Long menuItemId, int quantity) {
        if(quantity<=2)
      //  if (quantity <= 0)
             {
            throw new InvalidOperationException("Quantity must be at least 1");
        }
 
        Cart cart = getOrCreateCart(userId);
        MenuItem menuItem = menuItemService.getById(menuItemId);
 
        // Enforce single-restaurant cart, same as Swiggy/Zomato behavior.
        if (cart.getRestaurant() != null && !cart.getRestaurant().getId().equals(menuItem.getRestaurant().getId())) {
            throw new InvalidOperationException(
                    "Your cart has items from another restaurant. Clear the cart before adding items from a new restaurant.");
        }
 
        if (cart.getRestaurant() == null) {
            cart.setRestaurant(menuItem.getRestaurant());
        }
 
        CartItem existingItem = cartItemRepository.findByCartIdAndMenuItemId(cart.getId(), menuItemId).orElse(null);
 
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .menuItem(menuItem)
                    .quantity(quantity)
                    .build();
            cart.getItems().add(newItem);
            cartItemRepository.save(newItem);
        }
 
        return cartRepository.save(cart);
    }
 
    @Transactional
    public Cart removeFromCart(Long userId, Long menuItemId) {
        Cart cart = getCartOrThrow(userId);
 
        CartItem item = cartItemRepository.findByCartIdAndMenuItemId(cart.getId(), menuItemId)
                .orElseThrow(() -> new InvalidOperationException("Item is not in the cart"));
 
        cart.getItems().remove(item);
        cartItemRepository.delete(item);
 
        // Unlock the cart from its restaurant once it's empty.
        if (cart.getItems().isEmpty()) {
            cart.setRestaurant(null);
        }
 
        return cartRepository.save(cart);
    }
 
    @Transactional
    public Cart clearCart(Long userId) {
        Cart cart = getCartOrThrow(userId);
        cart.getItems().clear();
        cart.setRestaurant(null);
        return cartRepository.save(cart);
    }
 
    public Cart viewCart(Long userId) {
        return getOrCreateCart(userId);
    }
 
    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            User user = userService.getById(userId);
            Cart newCart = Cart.builder().user(user).build();
            return cartRepository.save(newCart);
        });
    }
 
    private Cart getCartOrThrow(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new InvalidOperationException("Cart is empty"));
    }
}
