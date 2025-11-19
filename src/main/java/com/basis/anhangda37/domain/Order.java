package com.basis.anhangda37.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.basis.anhangda37.domain.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    private String receiverName;

    private String receiverAddress;

    private String receiverPhone;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

    @CreatedDate
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") // Định dạng Json trả về
    private LocalDateTime createdAt;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    public Order() {
    }

    public Order(Long id, User user, Double totalPrice, String receiverName, String receiverAddress,
            String receiverPhone, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.totalPrice = totalPrice;
        this.receiverAddress = receiverAddress;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.status = OrderStatus.PENDING;
        this.createdAt = createdAt;
    }

    public void setStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }

    public void isShipping() {
        this.status = OrderStatus.SHIPPING;
    }

    public void isSuccess() {
        this.status = OrderStatus.SUCCESS;
    }

    public void isCancel() {
        this.status = OrderStatus.CANCEL;
    }

    public void isPending() {
        this.status = OrderStatus.PENDING;
    }

    public OrderStatus getStatus() {
        return this.status;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
