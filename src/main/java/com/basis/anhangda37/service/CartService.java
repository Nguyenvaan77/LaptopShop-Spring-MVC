package com.basis.anhangda37.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.basis.anhangda37.controller.admin.UserController;
import com.basis.anhangda37.domain.Cart;
import com.basis.anhangda37.domain.CartDetail;
import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.repository.CartRepository;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }
    
    public Cart findCartByUser(User user) {
        Cart cart = cartRepository.findByUser(user);
        if(cart == null) {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setSum(0);
            newCart = cartRepository.save(newCart);
            return newCart;
        }
        return cart;
    }

    public List<CartDetail> getAllCartDetails(Cart cart) {
        return cartRepository.findCartDetailsById(cart.getId());
    }
}
