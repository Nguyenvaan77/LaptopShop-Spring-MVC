package com.basis.anhangda37.service;

import org.springframework.stereotype.Service;

import com.basis.anhangda37.repository.CartDetailRepository;

@Service
public class CartDetailService {
    private final CartDetailRepository cartDetailRepository;

    public CartDetailService(CartDetailRepository cartDetailRepository) {
        this.cartDetailRepository = cartDetailRepository;
    }

    
}
