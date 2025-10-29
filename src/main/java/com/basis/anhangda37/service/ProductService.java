package com.basis.anhangda37.service;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.boot.autoconfigure.jms.JmsProperties.Listener.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.basis.anhangda37.domain.Cart;
import com.basis.anhangda37.domain.CartDetail;
import com.basis.anhangda37.domain.Order;
import com.basis.anhangda37.domain.OrderDetail;
import com.basis.anhangda37.domain.Product;
import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.repository.CartDetailRepository;
import com.basis.anhangda37.repository.CartRepository;
import com.basis.anhangda37.repository.OrderDetailRepository;
import com.basis.anhangda37.repository.OrderRepository;
import com.basis.anhangda37.repository.ProductRepository;
import com.basis.anhangda37.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class ProductService {

    private final CartService cartService;

    private final CartDetailService cartDetailService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;

    public ProductService(UserRepository userRepository, ProductRepository productRepository,
            CartRepository cartRepository, CartDetailRepository cartDetailRepository, OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository, CartDetailService cartDetailService, CartService cartService) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
        this.cartDetailService = cartDetailService;
        this.cartService = cartService;
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
            newCart.setSum(0);
            cartRepository.save(newCart);

            CartDetail newCartDetail = new CartDetail();
            newCartDetail.setCart(newCart);
            newCartDetail.setQuantity(1L);
            newCartDetail.setProduct(product);

            newCart.addCartDetail(newCartDetail);
            cartRepository.save(newCart);
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

    @Transactional
    public String handleCheckOut(String email, HttpSession session, String receiverName, String receiverAddress,
            String receiverPhone, Double totalPayment, List<CartDetail> cartDetails) {
        handleProductBeforeCheckout(cartDetails);
        return handlePlaceOrder(email, session, receiverName, receiverAddress, receiverPhone, totalPayment);
    }

    private void handleProductBeforeCheckout(List<CartDetail> cartDetails) {
        if (cartDetails == null || cartDetails.isEmpty()) {
            return;
        }

        for (CartDetail cartDetailDto : cartDetails) {
            CartDetail fetchCartDetail = cartDetailRepository.findById(cartDetailDto.getId()).get();
            Product product = fetchCartDetail.getProduct();
            Long quantityToSold = cartDetailDto.getQuantity();
            fetchCartDetail.setQuantity(quantityToSold);
            product.setQuantity(product.getQuantity() - quantityToSold);
            productRepository.save(product);
            cartDetailRepository.save(fetchCartDetail);
        }
    }

    private String handlePlaceOrder(String email, HttpSession session, String receiverName, String receiverAddress,
            String receiverPhone, Double totalPayment) {
        // User user = userRepository.findByEmail(email);
        // Cart cart = user.getCart();

        // cart.removeAllCartDetail();
        // cartRepository.save(cart);
        // user.removeCart();
        // cart.setUser(null);
        // cartRepository.deleteById(cart.getId());

        User managedUser = userRepository.findByEmail(email);
        Order order = new Order();
        order.setReceiverAddress(receiverAddress);
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setUser(managedUser);
        order.setTotalPrice(totalPayment);

        Cart cart = cartRepository.findByUser(managedUser);
        List<CartDetail> cartDetails = cartDetailRepository.findByCart(cart);

        List<OrderDetail> orderDetails = new ArrayList<>();

        for (CartDetail cartDetail : cartDetails) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(cartDetail.getQuantity());
            orderDetail.setProduct(cartDetail.getProduct());
            orderDetail.setPrice(cartDetail.getProduct().getPrice() *
                    cartDetail.getQuantity());
            // Tăng số lượng sản phẩm đã bán ~ ++ sold (Product)
            cartDetail.getProduct().setSold(cartDetail.getProduct().getSold() +
                    cartDetail.getQuantity());
            orderDetail.setOrder(order);
            orderDetails.add(orderDetail);
        }

        order.setOrderDetails(orderDetails);
        orderRepository.save(order);

        cart.removeAllCartDetail();
        managedUser.removeCart();
        cart.setUser(null);
        cartRepository.deleteById(cart.getId());

        session.setAttribute("sum", 0);
        return String.valueOf(order.getId());
    }
}
