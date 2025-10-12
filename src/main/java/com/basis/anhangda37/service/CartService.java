package com.basis.anhangda37.service;

import org.springframework.stereotype.Service;

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
        return cartRepository.findByUser(user);
    }
}
