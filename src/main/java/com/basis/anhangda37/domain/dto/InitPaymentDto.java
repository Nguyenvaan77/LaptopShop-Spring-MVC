package com.basis.anhangda37.domain.dto;

public class InitPaymentDto {
    private Long amount;
    private String bankCode;
    private String language;
    private Long orderId;

    public Long getAmount() {
        return amount;
    }
    public void setAmount(Long amount) {
        this.amount = amount;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    
}
