package com.basis.anhangda37.service;

import org.springframework.stereotype.Service;

import com.basis.anhangda37.domain.Payment;
import com.basis.anhangda37.repository.PaymentRepository;

@Service
public class CommonPaymentService {
    private final PaymentRepository paymentRepository;

    public CommonPaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment getPaymentByTxn_Ref(String txnRef) {
        return paymentRepository.findByVnp_TxnRef(txnRef).get();
    }

    public PaymentRepository getPaymentRepository() {
        return paymentRepository;
    }

    
}
