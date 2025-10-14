package com.basis.anhangda37.domain.dto;

public class CartDetailDto {
    private Long productId;
    private String image;
    private String name;
    private Double price;
    private Integer quantity;
    private Double total;

    public CartDetailDto() {
        price = 0D;
        quantity = 0;
        total = 0D;
    };

    public CartDetailDto(Long id, String image, String name, Double price, Integer quantity) {
        this.productId = id;
        price = 0D;
        quantity = 0;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        this.total = this.price * this.quantity;
    }

    public Double getTotal() {
        return this.total;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long id) {
        this.productId = id;
    }

}
