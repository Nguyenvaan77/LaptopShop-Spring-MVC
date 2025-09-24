package com.basis.anhangda37.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.basis.anhangda37.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    Optional<Product> findById(Long id);
}
