package com.basis.anhangda37.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.basis.anhangda37.domain.Order;
import com.basis.anhangda37.domain.Payment;
import com.basis.anhangda37.domain.User;
import com.mysql.cj.log.Log;
import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order,Long>{
    List<Order> findByUser(User user);
    Page<Order> findAll(Pageable pageable);
    Optional<Order> findById(Long id);
    Optional<Order> findByPayment(Payment payment);
}
