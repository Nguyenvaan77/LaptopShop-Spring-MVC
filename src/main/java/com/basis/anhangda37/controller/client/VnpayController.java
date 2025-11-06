package com.basis.anhangda37.controller.client;

import com.basis.anhangda37.config.PayConfig;
import com.basis.anhangda37.domain.dto.InitPaymentDto;
import com.basis.anhangda37.service.VnPayService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

@Controller
public class VnpayController {
    private final VnPayService vnPayService;

    public VnpayController(VnPayService vnPayService) {
        this.vnPayService = vnPayService;
    }

    @PostMapping("/payment/create")
    public void createPayment(HttpServletRequest req, HttpServletResponse response, @RequestParam("amount") long amount,
            @RequestParam(value = "bankCode", required = false) String bankCode,
            @RequestParam(value = "language", required = false) String language,
            @RequestParam(value = "orderId") Long orderId)
            throws IOException {
        InitPaymentDto initPaymentDto = new InitPaymentDto();
        initPaymentDto.setAmount(amount);
        initPaymentDto.setBankCode(bankCode);
        initPaymentDto.setLanguage(language);
        initPaymentDto.setOrderId(orderId);

        String paymentUrl = vnPayService.initPayment(req, initPaymentDto);
        response.sendRedirect(paymentUrl);
    }

    @GetMapping("/payment/vnpay-return")
    public void vnpayReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Bước 1: Lấy toàn bộ tham số trả về
        Map<String, String> fields = new HashMap<>();
        Enumeration<String> params = request.getParameterNames();
        String url = "";
        while (params.hasMoreElements()) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                fields.put(fieldName, fieldValue);
                url += "&" + fieldName + "=" + fieldValue;
            }
        }

        String queryString = PayConfig.getQueryString(fields);

        // Bước 2: Tách chuỗi secure hash
        String vnp_SecureHash = fields.get("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        // Bước 3: Tính lại hash để so sánh
        String signValue = PayConfig.hashAllFields(fields);

        // Bước 4: Kiểm tra tính hợp lệ
        if (signValue.equals(vnp_SecureHash)) {
            // ✅ Check mã phản hồi
            response.sendRedirect("/thanks");
        } else {
            response.sendRedirect("/access-deny");
        }
    }
}
