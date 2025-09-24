package com.basis.anhangda37.service;

import org.springframework.stereotype.Service;

import com.basis.anhangda37.domain.Product;
import com.basis.anhangda37.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}
