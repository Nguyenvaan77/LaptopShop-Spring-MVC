package com.basis.anhangda37.domain.dto;

import com.basis.anhangda37.domain.Order;

public class PaymentResponseDto {
    private final String callBackUrl;

    public PaymentResponseDto(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public String getCallBackUrl() {return this.callBackUrl;};
}
