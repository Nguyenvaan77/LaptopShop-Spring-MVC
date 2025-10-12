package com.basis.anhangda37.domain;

import java.util.ArrayList;
import java.util.List;

import javax.smartcardio.CardTerminal;

import org.springframework.security.authentication.CachingUserDetailsService;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart")
    private List<CartDetail> cartDetails = new ArrayList<>();

    private Integer sum;

    public Cart() {
    }

    public Cart(Integer id, User user, List<CartDetail> cartDetails, Integer sum) {
        this.id = id;
        this.user = user;
        this.cartDetails = cartDetails;
        this.sum = sum;
    }

    public void addCartDetail(CartDetail cartDetail){
        cartDetails.add(cartDetail);
        ++sum;
    }

    public void removeCartDetail(CartDetail cartDetail) {
        cartDetails.remove(cartDetail);
        --sum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartDetail> getCartDetails() {
        return cartDetails;
    }

    public void setCartDetails(List<CartDetail> cartDetails) {
        this.cartDetails = cartDetails;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    
}