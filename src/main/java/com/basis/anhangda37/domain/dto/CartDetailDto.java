package com.basis.anhangda37.domain.dto;

public class CartDetailDto {
    private Long cartDetailId;
    private Long productId;
    private String image;
    private String name;
    private Double price;
    private Long quantity;
    private Double total;
    private Long quantityInStock;

    public CartDetailDto() {
        price = 0D;
        quantity = 0L;
        total = 0D;
    };

    public CartDetailDto(Long cartDetailId, Long productId, String image, String name, Double price, Long quantity, Long quantityInStock) {
        this.productId = productId;
        this.cartDetailId = cartDetailId;
        price = 0D;
        quantity = 0L;
        total = 0D;
        this.image = image;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        total = quantity * price;
        this.quantityInStock = quantityInStock;
    }

    public Long getCartDetailId() {
        return cartDetailId;
    }

    public void setCartDetailId(Long cartDetailId) {
        this.cartDetailId = cartDetailId;
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long id) {
        this.productId = id;
    }

    public Long getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Long quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
}
