package com.basis.anhangda37.domain.dto;

import java.util.List;

public class OrderToShowDto {
    
    private List<OrderDetailToShowDto> orderDetailToShowDtos;
    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;
    
    public List<OrderDetailToShowDto> getOrderDetailToShowDtos() {
        return orderDetailToShowDtos;
    }
    public void setOrderDetailToShowDtos(List<OrderDetailToShowDto> orderDetailToShowDtos) {
        this.orderDetailToShowDtos = orderDetailToShowDtos;
    }
    public String getReceiverName() {
        return receiverName;
    }
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    public String getReceiverAddress() {
        return receiverAddress;
    }
    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }
    public String getReceiverPhone() {
        return receiverPhone;
    }
    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    
}
