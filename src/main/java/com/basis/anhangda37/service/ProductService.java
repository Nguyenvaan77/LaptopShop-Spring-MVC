package com.basis.anhangda37.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.basis.anhangda37.domain.Product;
import com.basis.anhangda37.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    public void deleteProductById(Long id) {
        if(productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        if(productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }
    }
}
