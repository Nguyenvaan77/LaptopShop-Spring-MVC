package com.basis.anhangda37.service.iface;

import com.basis.anhangda37.domain.Cart;
import com.basis.anhangda37.domain.CartDetail;
import com.basis.anhangda37.domain.User;

import java.util.List;

/**
 * Service interface for Cart-related operations.
 * Defines contracts for shopping cart management.
 */
public interface ICartService {

    /**
     * Retrieves or creates a cart for a user.
     * If the user doesn't have a cart, a new one is created.
     * @param user The user
     * @return The user's cart
     */
    Cart findOrCreateCartForUser(User user);

    /**
     * Retrieves all cart details for a specific cart.
     * @param cart The cart
     * @return A list of cart details
     */
    List<CartDetail> getCartDetails(Cart cart);
}
