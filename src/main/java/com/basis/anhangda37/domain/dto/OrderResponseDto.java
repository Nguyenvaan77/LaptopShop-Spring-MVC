package com.basis.anhangda37.domain.dto;

public class OrderResponseDto {
    private Long orderId;
    private PaymentResponseDto paymentResponse;

    public PaymentResponseDto getPaymentResponse() {
        return paymentResponse;
    }
    public void setPaymentResponse(PaymentResponseDto paymentResponse) {
        this.paymentResponse = paymentResponse;
    }
    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    
}
