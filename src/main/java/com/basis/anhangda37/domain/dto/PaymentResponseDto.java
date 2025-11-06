package com.basis.anhangda37.domain.dto;

public class PaymentResponseDto {
    private String callBackUrl;

    public PaymentResponseDto(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public String getCallBackUrl() {return this.callBackUrl;};
}
