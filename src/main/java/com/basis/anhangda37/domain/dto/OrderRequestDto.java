package com.basis.anhangda37.domain.dto;

import org.springframework.format.annotation.NumberFormat;

import com.basis.anhangda37.domain.Cart;

import jakarta.validation.constraints.NotNull;

public class OrderRequestDto {
    @NotNull
    private Long orderId;

    @NotNull
    private String customerName;

    @NotNull
    private String customerAddress;

    @NotNull
    private String customerPhone;

    @NotNull
    @NumberFormat
    private Double totalPayment;

    @NotNull
    private Cart cart;

    @NotNull
    private String bankCode;

    private String language;

    private String ipAddress;

    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public Double getTotalPayment() {
        return totalPayment;
    }
    public void setTotalPayment(Double totalPayment) {
        this.totalPayment = totalPayment;
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
    public String getIpAddress() {
        return ipAddress;
    }
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    
}
