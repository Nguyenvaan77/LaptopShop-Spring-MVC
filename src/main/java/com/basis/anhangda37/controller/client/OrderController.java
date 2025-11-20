package com.basis.anhangda37.controller.client;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;

import com.basis.anhangda37.config.PayConfig;
import com.basis.anhangda37.domain.Cart;
import com.basis.anhangda37.domain.CartDetail;
import com.basis.anhangda37.domain.Order;
import com.basis.anhangda37.domain.Payment;
import com.basis.anhangda37.domain.dto.OrderRequestDto;
import com.basis.anhangda37.domain.dto.OrderResponseDto;
import com.basis.anhangda37.domain.dto.payment.InitPaymentDto;
import com.basis.anhangda37.domain.dto.payment.PaymentCallBackData;
import com.basis.anhangda37.exception.payment.SignNotVerifyException;
import com.basis.anhangda37.repository.OrderRepository;
import com.basis.anhangda37.service.CommonPaymentService;
import com.basis.anhangda37.service.OrderDetailService;
import com.basis.anhangda37.service.OrderService;
import com.basis.anhangda37.service.ProductService;
import com.basis.anhangda37.service.VnPayService;
import com.basis.anhangda37.service.interf.PaymentService;

import org.springframework.ui.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class OrderController {

    private final OrderDetailService orderDetailService;
    private final VnPayService vnPayService;
    private final CommonPaymentService commonPaymentService;
    private final OrderService orderService;
    private final ProductService productService;
    private final OrderRepository orderRepository;

    public OrderController(VnPayService vnPayService, CommonPaymentService commonPaymentService,
            OrderService orderService, OrderDetailService orderDetailService, ProductService productService,
            OrderRepository orderRepository) {
        this.vnPayService = vnPayService;
        this.commonPaymentService = commonPaymentService;
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.productService = productService;
        this.orderRepository = orderRepository;
    }

    @PostMapping("order/payment/create")
    public void createPayment(@Valid OrderRequestDto orderRequest,
            HttpServletRequest request,
            HttpServletResponse response,
            @ModelAttribute("cart") Cart cart)
            throws IOException {
        List<CartDetail> cartDetails = cart.getCartDetails();

        String ipAddress = PayConfig.getIpAddress(request);
        orderRequest.setIpAddress(ipAddress);

        OrderResponseDto result = orderService.book(request, orderRequest, cartDetails);

        String paymentUrl = result.getPaymentResponse().getPaymentUrl();

        response.sendRedirect(paymentUrl);
    }

    @GetMapping("order/payment/vnpay-return")
    public String vnpayReturn(HttpServletRequest request,
            HttpServletResponse response,
            Model model) throws IOException {
        Map<String, String> fields = new HashMap<>();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                fields.put(fieldName, fieldValue);
            }
        }
        PaymentCallBackData callBackData = new PaymentCallBackData();
        callBackData.setIpAddress(request.getRemoteAddr());
        callBackData.setParams(fields);
        callBackData.setVnp_TxnRef(fields.get("vnp_TxnRef"));

        try {
            vnPayService.hanlderVnPayReturnAfterSendPaymentUrl(callBackData);
        } catch (SignNotVerifyException ex) {
            return "client/homepage/deny";
        }
        Long orderId = orderService.getByVnpTxnRef(callBackData.getVnp_TxnRef()).getId();
        model.addAttribute("orderCode", orderId);
        return "client/cart/thanks";
    }

    // @GetMapping("/order/{txnRef}/{status}")
    // public String getSuccessCheckOutPage(@PathVariable("txnRef") String txnRef,
    // @PathVariable("status") String status,
    // Model model) {
    // if (status.equals("success")) {
    // Payment payment = commonPaymentService.getPaymentByTxn_Ref(txnRef);
    // Order order = orderService.getOrderById(payment.getOrder().getId());
    // model.addAttribute("orderCode", order.getId());
    // }
    // ;
    // return "client/cart/thanks";
    // }

    @GetMapping("/order/check-status/{id}")
    public void checkStatusAndRedirect(
            @PathVariable("id") Long orderId,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        try {
            // Lấy IP server (bạn có thể hardcode "127.0.0.1" nếu test local)
            String ipAddress = PayConfig.getIpAddress(request);

            // 1. GỌI SERVICE, SERVICE SẼ TỰ ĐỘNG CẬP NHẬT DB
            String redirectUrl = vnPayService.checkOrderPaymentStatus(orderId, ipAddress);

            // 2. CONTROLLER CHỈ VIỆC REDIRECT
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            // Lỗi chung
            response.sendRedirect("/error-page");
        }
    }

}
