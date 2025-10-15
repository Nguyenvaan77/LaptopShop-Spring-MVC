package com.basis.anhangda37.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.basis.anhangda37.domain.Cart;
import com.basis.anhangda37.domain.CartDetail;
import com.basis.anhangda37.domain.Product;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long>{
    Optional<CartDetail> findById(Long id);

    List<CartDetail> findByCart(Cart cart);
    
    CartDetail findByCartAndProduct(Cart cart, Product product);

    void deleteById(Long id);
}
