package com.basis.anhangda37.controller.client;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.basis.anhangda37.config.PayConfig;
import com.basis.anhangda37.domain.Cart;
import com.basis.anhangda37.domain.CartDetail;
import com.basis.anhangda37.domain.Order;
import com.basis.anhangda37.domain.Payment;
import com.basis.anhangda37.domain.dto.InitPaymentDto;
import com.basis.anhangda37.domain.dto.OrderRequestDto;
import com.basis.anhangda37.service.CommonPaymentService;
import com.basis.anhangda37.service.OrderDetailService;
import com.basis.anhangda37.service.OrderService;
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

    public OrderController(VnPayService vnPayService, CommonPaymentService commonPaymentService, OrderService orderService, OrderDetailService orderDetailService) {
        this.vnPayService = vnPayService;
        this.commonPaymentService = commonPaymentService;
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
    }

    @PostMapping("order/payment/create")
    public void createPayment(@Valid OrderRequestDto orderRequest,
                                HttpServletRequest request, 
                                HttpServletResponse response,
                                @RequestParam("customerName") String name,
                                @RequestParam("customerAddress") String address,
                                @RequestParam("customerPhone") String phone,
                                @RequestParam("totalPayment") Double totalPayment,
                                @ModelAttribute("cart") Cart cart)
                                throws IOException {
        HttpSession session = request.getSession();
        String email = ((String) session.getAttribute("email"));
        List<CartDetail> cartDetails = cart.getCartDetails();

        String ipAddress = PayConfig.getIpAddress(request);
        orderRequest.setIpAddress(ipAddress);

        orderService.book(email, session, name, ipAddress, phone, totalPayment, cartDetails);
        String paymentUrl =  vnPayService.initPayment(request, orderRequest);
        response.sendRedirect(paymentUrl);
    }

    @GetMapping("order/payment/vnpay-return")
    public void vnpayReturn(HttpServletRequest request, 
                                HttpServletResponse response,
                                Model model) throws IOException {
        // Bước 1: Lấy toàn bộ tham số trả về
        Map<String, String> fields = new HashMap<>();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                fields.put(fieldName, fieldValue);
            }
        }

        // Bước 2: Tách chuỗi secure hash
        String vnp_SecureHash = fields.get("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        //Kiểm tra sự nhất quán dữ liệu
        if(verifySign(fields, vnp_SecureHash)) {
            System.out.println("Dũ liệu nhất quán");

        } else {
            System.out.println("Không trùng chữ kí");
            response.sendRedirect("/access-deny");
        }
        
    }

    private boolean verifySign(Map<String,String> fields, String vnp_SecureHash) {
        String signValue = PayConfig.hashAllFields(fields);
        return signValue.equals(vnp_SecureHash);
    }

    @GetMapping("/order/{txnRef}/{status}")
    public String getSuccessCheckOutPage(@PathVariable("txnRef") String txnRef, @PathVariable("status") String status,
            Model model) {
        if (status.equals("success")) {
            Payment payment = commonPaymentService.getPaymentByTxn_Ref(txnRef);
            Order order = orderService.getOrderById(payment.getOrder().getId());
            model.addAttribute("orderCode", order.getId());
        }
        ;
        return "client/cart/thanks";
    }
}

