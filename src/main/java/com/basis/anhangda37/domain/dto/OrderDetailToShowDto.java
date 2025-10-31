package com.basis.anhangda37.domain.dto;

public class OrderDetailToShowDto {
    private String image;
    private String name;
    private Double price;
    private Long quantity;
    private Double total;

    public OrderDetailToShowDto() {
        price = 0D;
        quantity = 0L;
        total = 0D;
    };

    public OrderDetailToShowDto(String image, String name, Double price, Long quantity) {
        price = 0D;
        quantity = 0L;
        total = 0D;
        this.image = image;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        total = quantity * price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
        this.total = this.price * this.quantity;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
        this.total = this.price * this.quantity;
    }

    public Double getTotal() {
        return this.total;
    }
}
