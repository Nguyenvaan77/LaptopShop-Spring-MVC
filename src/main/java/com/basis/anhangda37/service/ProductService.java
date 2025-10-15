package com.basis.anhangda37.service;

import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.boot.autoconfigure.jms.JmsProperties.Listener.Session;
import org.springframework.stereotype.Service;

import com.basis.anhangda37.domain.Cart;
import com.basis.anhangda37.domain.CartDetail;
import com.basis.anhangda37.domain.Product;
import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.repository.CartDetailRepository;
import com.basis.anhangda37.repository.CartRepository;
import com.basis.anhangda37.repository.ProductRepository;
import com.basis.anhangda37.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class ProductService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;

    public ProductService(UserRepository userRepository, ProductRepository productRepository,
            CartRepository cartRepository, CartDetailRepository cartDetailRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    public void deleteProductById(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }
    }

    public void handleProductToCart(HttpSession session, String gmailUser, Long productId) {
        User user = userRepository.findByEmail(gmailUser);
        Cart cart = cartRepository.findByUser(user);
        Product product = productRepository.findById(productId).orElse(null);

        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setSum(1);
            cartRepository.save(newCart);

            CartDetail newCartDetail = new CartDetail();
            newCartDetail.setCart(newCart);
            newCartDetail.setQuantity(1L);
            newCartDetail.setProduct(product);

            newCart.addCartDetail(newCartDetail);
            cartRepository.save(newCart);
            cartDetailRepository.save(newCartDetail);
            session.setAttribute("sum", newCart.getSum());
            return;
        }

        CartDetail existCartDetail = cartDetailRepository.findByCartAndProduct(cart, product);
        if (existCartDetail == null) {
            CartDetail newCartDetail = new CartDetail();
            newCartDetail.setProduct(product);
            newCartDetail.setQuantity(1L);
            newCartDetail.setCart(cart);
            cart.addCartDetail(newCartDetail);
            cartDetailRepository.save(newCartDetail);
            session.setAttribute("sum", cart.getSum());
            return;
        }

        existCartDetail.setQuantity(existCartDetail.getQuantity() + 1);
        cartDetailRepository.save(existCartDetail);
    }
}
