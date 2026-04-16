package com.basis.anhangda37.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.basis.anhangda37.domain.Cart;
import com.basis.anhangda37.domain.CartDetail;
import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.exception.CartNotFoundException;
import com.basis.anhangda37.repository.CartRepository;
import com.basis.anhangda37.service.iface.ICartService;

import java.util.List;

/**
 * Service class for Cart-related operations.
 * Handles shopping cart management including creation and retrieval.
 * Implements clean separation of concerns with proper logging.
 */
@Service
public class CartService implements ICartService {

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;

    /**
     * Constructs a CartService with required repositories.
     */
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    /**
     * Retrieves or creates a cart for a user.
     * If the user doesn't have a cart, a new one is created.
     * @param user The user
     * @return The user's cart
     */
    @Override
    public Cart findOrCreateCartForUser(User user) {
        logger.debug("Finding or creating cart for user: {}", user.getEmail());
        Cart cart = cartRepository.findByUser(user);
        
        if (cart == null) {
            logger.info("No cart found for user {}, creating new cart", user.getEmail());
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setSum(0);
            newCart = cartRepository.save(newCart);
            logger.debug("New cart created with id: {}", newCart.getId());
            return newCart;
        }
        
        logger.debug("Existing cart found for user: {}", user.getEmail());
        return cart;
    }

    /**
     * Retrieves all cart details for a specific cart.
     * @param cart The cart
     * @return A list of cart details
     */
    @Override
    public List<CartDetail> getCartDetails(Cart cart) {
        logger.debug("Fetching cart details for cart id: {}", cart.getId());
        return cartRepository.findCartDetailsById(cart.getId());
    }

    /**
     * Finds a cart by user.
     * @param user The user
     * @return The cart if found
     * @throws CartNotFoundException if cart doesn't exist
     */
    public Cart findCartByUser(User user) {
        logger.debug("Fetching cart for user: {}", user.getEmail());
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            logger.error("Cart not found for user: {}", user.getEmail());
            throw new CartNotFoundException(user.getId());
        }
        return cart;
    }
}
