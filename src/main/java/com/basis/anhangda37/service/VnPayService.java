package com.basis.anhangda37.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.stereotype.Service;

import com.basis.anhangda37.config.PayConfig;
import com.basis.anhangda37.domain.dto.InitPaymentDto;
import com.basis.anhangda37.domain.dto.PaymentResponseDto;
import com.basis.anhangda37.service.interf.PaymentService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class VnPayService implements PaymentService {
    public String initPayment(HttpServletRequest request, InitPaymentDto initPaymentDto) {
        Long orderId = initPaymentDto.getOrderId();
        String bankCode = initPaymentDto.getBankCode();
        String language = initPaymentDto.getLanguage();
        Long amount = initPaymentDto.getAmount();

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";

        long amountVnp = amount * 100;
        String vnp_TxnRef = String.valueOf(PayConfig.getRandomNumber(8));
        String vnp_IpAddr = PayConfig.getIpAddress(request);
        String vnp_TmnCode = PayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amountVnp));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty())
            vnp_Params.put("vnp_BankCode", bankCode);

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", (language != null && !language.isEmpty()) ? language : "vn");
        vnp_Params.put("vnp_ReturnUrl", buildReturnUrl(orderId, "success"));
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());

        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (Iterator<String> itr = fieldNames.iterator(); itr.hasNext();) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII))
                        .append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    hashData.append('&');
                    query.append('&');
                }
            }
        }

        String vnp_SecureHash = PayConfig.hmacSHA512(PayConfig.secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);
        String paymentUrl = PayConfig.vnp_PayUrl + "?" + query;

        return paymentUrl;
    }

    private String buildReturnUrl(Long orderId, String statusRaw) {
        String s = PayConfig.vnp_ReturnUrl;
        return s.replace("{id}", String.valueOf(orderId))
                .replace("{status}", statusRaw);
    }
}
