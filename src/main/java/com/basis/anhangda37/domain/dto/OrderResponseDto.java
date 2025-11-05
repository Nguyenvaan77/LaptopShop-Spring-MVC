package com.basis.anhangda37.domain.dto;

import com.basis.anhangda37.domain.Order;

public class OrderResponseDto {
    private Order order;
    private PaymentResponseDto paymentResponse;

    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }
    public PaymentResponseDto getPaymentResponse() {
        return paymentResponse;
    }
    public void setPaymentResponse(PaymentResponseDto paymentResponse) {
        this.paymentResponse = paymentResponse;
    }

    
}
