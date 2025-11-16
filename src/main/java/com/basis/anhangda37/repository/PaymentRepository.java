package com.basis.anhangda37.repository;

import com.basis.anhangda37.controller.client.HomePageController;
import com.basis.anhangda37.domain.Payment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{
    Optional<Payment> findByVnp_TxnRef(String vnp_TxnRef);
}
