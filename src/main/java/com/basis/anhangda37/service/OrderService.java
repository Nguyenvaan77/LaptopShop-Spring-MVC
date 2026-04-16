package com.basis.anhangda37.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.basis.anhangda37.config.PayConfig;
import com.basis.anhangda37.domain.Cart;
import com.basis.anhangda37.domain.CartDetail;
import com.basis.anhangda37.domain.Order;
import com.basis.anhangda37.domain.OrderDetail;
import com.basis.anhangda37.domain.Payment;
import com.basis.anhangda37.domain.Product;
import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.domain.dto.OrderRequestDto;
import com.basis.anhangda37.domain.dto.OrderResponseDto;
import com.basis.anhangda37.domain.dto.payment.PaymentResponseDto;
import com.basis.anhangda37.domain.enums.OrderStatus;
import com.basis.anhangda37.domain.enums.PaymentStatus;
import com.basis.anhangda37.exception.OrderNotFoundException;
import com.basis.anhangda37.exception.UserNotFoundException;
import com.basis.anhangda37.repository.CartDetailRepository;
import com.basis.anhangda37.repository.CartRepository;
import com.basis.anhangda37.repository.OrderRepository;
import com.basis.anhangda37.repository.PaymentRepository;
import com.basis.anhangda37.repository.ProductRepository;
import com.basis.anhangda37.repository.UserRepository;
import com.basis.anhangda37.util.AppConstants;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

/**
 * Service class for Order-related operations.
 * Handles order management, payment processing, and checkout operations.
 * Provides comprehensive logging and error handling.
 */
@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final CartDetailRepository cartDetailRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PaymentRepository paymentRepository;
    private final VnPayService vnPayService;

    public OrderService(OrderRepository orderRepository, CartDetailRepository cartDetailRepository,
            ProductRepository productRepository, UserRepository userRepository, CartRepository cartRepository,
            PaymentRepository paymentRepository, VnPayService vnPayService) {
        this.orderRepository = orderRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.paymentRepository = paymentRepository;
        this.vnPayService = vnPayService;
    }

    @Transactional
    public OrderResponseDto book(HttpServletRequest request, OrderRequestDto orderRequestDto,
            List<CartDetail> cartDetails) {
        HttpSession session = request.getSession();
        OrderResponseDto dto = new OrderResponseDto();

        handleProductBeforeCheckout(cartDetails);
        Long orderCode = handlePlaceOrder(session, orderRequestDto.getCustomerName(),
                orderRequestDto.getCustomerAddress(), orderRequestDto.getCustomerPhone(),
                orderRequestDto.getTotalPayment());
        String paymentUrl = vnPayService.initPayment(orderRequestDto);

        /*
         * TODO: Tạo và gán Payment cho Order
         */

        // 1. Lấy lại Order vừa được lưu (đang trong trạng thái managed)
        Order order = orderRepository.findById(orderCode)
                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy Order vừa tạo. ID: " + orderCode));

        // 2. Trích xuất thông tin từ paymentUrl bằng các hàm helper
        String vnpTxnRef = VnPayService.getTxnRefFromUrl(paymentUrl);
        Long amountVnp = VnPayService.getAmountFromUrl(paymentUrl);
        LocalDateTime createDate = VnPayService.getCreateDateFromUrl(paymentUrl);

        // 3. Tạo đối tượng Payment
        Payment payment = new Payment();
        payment.setMethod("VNPAY"); // Phương thức thanh toán
        payment.setBankCode(orderRequestDto.getBankCode());
        payment.setAmount(amountVnp); // Số tiền (đã * 100)
        payment.setVnpTxnRef(vnpTxnRef); // Mã giao dịch VNPAY
        payment.setCreatedAt(createDate); // Thời gian tạo
        payment.setStatus(PaymentStatus.PENDING); // Trạng thái: Chờ thanh toán

        // 4. Thiết lập quan hệ hai chiều (Bidirectional relationship)
        payment.setOrder(order);
        order.setPayment(payment);

        // 5. Lưu lại
        // Do hàm book() là @Transactional và Order có cascade = CascadeType.ALL
        // nên khi ta lưu order, payment cũng sẽ tự động được lưu.
        orderRepository.save(order);

        // KẾT THÚC PHẦN TODO

        dto.setOrderId(orderCode);
        dto.setPaymentResponse(new PaymentResponseDto(paymentUrl, VnPayService.getReturnUrl(paymentUrl)));
        return dto;
    }

    public Order getByVnpTxnRef(String vnp_TxnRef) {
        Payment payment = paymentRepository.findByVnpTxnRef(vnp_TxnRef).get();
        return orderRepository.findByPayment(payment).get();
    }

    /**
     * Retrieves an order by ID.
     * @param id The order ID
     * @return The order if found
     * @throws OrderNotFoundException if the order doesn't exist
     */
    public Order getOrderById(Long id) {
        logger.debug("Fetching order with id: {}", id);
        return orderRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Order not found with id: {}", id);
                    return new OrderNotFoundException(id);
                });
    }

    /**
     * Retrieves all orders for a specific user.
     * @param user The user
     * @return A list of user orders
     */
    public List<Order> getAllOrdersByUser(User user) {
        logger.debug("Fetching all orders for user: {}", user.getEmail());
        return orderRepository.findByUser(user);
    }

    /**
     * Retrieves all orders for a user sorted by creation date (descending).
     * @param user The user
     * @return A list of user orders sorted by creation date
     */
    public List<Order> getAllOrdersByUserOrderByCreatedAtDesc(User user) {
        logger.debug("Fetching user orders sorted by date (descending): {}", user.getEmail());
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    /**
     * Retrieves all orders.
     * @return A list of all orders
     */
    public List<Order> getAllOrders() {
        logger.debug("Fetching all orders");
        return orderRepository.findAll();
    }

    /**
     * Retrieves all orders with pagination.
     * @param pageable The pagination information
     * @return A page of orders
     */
    public Page<Order> getAllOrders(Pageable pageable) {
        logger.debug("Fetching orders with pagination: page={}, size={}", 
                pageable.getPageNumber(), pageable.getPageSize());
        return orderRepository.findAll(pageable);
    }

    /**
     * Updates the status of an order.
     * @param order The order to update
     * @param orderStatus The new status
     * @return The updated order
     */
    public Order updateStatus(Order order, OrderStatus orderStatus) {
        logger.info("Updating order {} status to: {}", order.getId(), orderStatus);
        order.setStatus(orderStatus);
        Order updatedOrder = orderRepository.save(order);
        logger.debug("Order status updated successfully");
        return updatedOrder;
    }

    /**
     * Deletes an order by ID.
     * @param id The order ID
     */
    public void deleteById(Long id) {
        logger.info("Deleting order with id: {}", id);
        if (!orderRepository.existsById(id)) {
            logger.warn("Attempted to delete non-existent order with id: {}", id);
            throw new OrderNotFoundException(id);
        }
        orderRepository.deleteById(id);
        logger.debug("Order deleted successfully");
    }

    /**
     * Returns the total count of orders.
     * @return The order count
     */
    public long countOrders() {
        logger.debug("Counting total orders");
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

    private Long handlePlaceOrder(HttpSession session, String receiverName, String receiverAddress,

            String receiverPhone, Double totalPayment) {
        String email = (String) session.getAttribute("email");
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

            cartDetail.getProduct().setSold(cartDetail.getProduct().getSold() + cartDetail.getQuantity());
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
        return order.getId();
    }
}
