package com.basis.anhangda37.service;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.atn.OrderedATNConfigSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.stereotype.Service;
import com.basis.anhangda37.controller.admin.OrderController;
import com.basis.anhangda37.domain.Cart;
import com.basis.anhangda37.domain.CartDetail;
import com.basis.anhangda37.domain.Order;
import com.basis.anhangda37.domain.OrderDetail;
import com.basis.anhangda37.domain.Product;
import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.domain.dto.OrderDetailToShowDto;
import com.basis.anhangda37.domain.dto.OrderRequestDto;
import com.basis.anhangda37.domain.dto.OrderResponseDto;
import com.basis.anhangda37.domain.dto.OrderToShowDto;
import com.basis.anhangda37.domain.dto.PaymentResponseDto;
import com.basis.anhangda37.domain.enums.OrderStatus;
import com.basis.anhangda37.repository.CartDetailRepository;
import com.basis.anhangda37.repository.CartRepository;
import com.basis.anhangda37.repository.OrderRepository;
import com.basis.anhangda37.repository.ProductRepository;
import com.basis.anhangda37.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartDetailRepository cartDetailRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    private final VnPayService vnPayService;

    public OrderService(OrderRepository orderRepository, CartDetailRepository cartDetailRepository, ProductRepository productRepository, UserRepository userRepository, CartRepository cartRepository, VnPayService vnPayService) {
        this.orderRepository = orderRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.vnPayService = vnPayService;
    }

    public OrderResponseDto book(String email, HttpSession session, String receiverName, String receiverAddress,
                String receiverPhone, Double totalPayment, List<CartDetail> cartDetails) {

           

            return null;
    }

    public List<Order> getAllOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).get();
    }

    public Order updateStatus(Order order, OrderStatus orderStatus) {
        order.setStatus(orderStatus);
        return order = orderRepository.save(order);
    }

    public void deleteById(Long id) {
        if(orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
        }
    }

    public long countOrder() {
        return orderRepository.count();
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
