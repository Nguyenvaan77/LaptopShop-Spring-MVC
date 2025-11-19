package com.basis.anhangda37.domain.dto;

public class PaymentResponseDto {
    private String paymentUrl;
    private String callBackUrl;

    public PaymentResponseDto(String paymentUrl, String callBackUrl) {
        this.callBackUrl = callBackUrl;
        this.paymentUrl = paymentUrl;
    }

    void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public String getCallBackUrl() {
        return this.callBackUrl;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    };
}
