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
import com.basis.anhangda37.domain.Payment;
import com.basis.anhangda37.domain.dto.InitPaymentDto;
import com.basis.anhangda37.domain.dto.OrderRequestDto;
import com.basis.anhangda37.domain.dto.PaymentResponseDto;
import com.basis.anhangda37.domain.enums.PaymentParam;
import com.basis.anhangda37.service.interf.PaymentService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class VnPayService implements PaymentService {

    public String initPayment(HttpServletRequest request, OrderRequestDto orderRequest) {

        Long orderId = orderRequest.getOrderId();
        String bankCode = orderRequest.getBankCode();
        String language = orderRequest.getLanguage();
        Long amount = orderRequest.getAmount();
        String ipAddress = orderRequest.getIpAddress();

        // Required values
        String vnpVersion = "2.1.0";
        String vnpCommand = "pay";
        String vnpOrderType = "other";
        long amountVnp = amount * 100;

        String vnpTxnRef = PayConfig.getRandomNumber(8);
        String vnpIpAddr = ipAddress;
        String vnpTmnCode = PayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put(PaymentParam.VNP_VERSION.value(), vnpVersion);
        vnp_Params.put(PaymentParam.VNP_COMMAND.value(), vnpCommand);
        vnp_Params.put(PaymentParam.VNP_TMNCODE.value(), vnpTmnCode);
        vnp_Params.put(PaymentParam.VNP_AMOUNT.value(), String.valueOf(amountVnp));
        vnp_Params.put(PaymentParam.VNP_CURRCODE.value(), "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put(PaymentParam.VNP_BANKCODE.value(), bankCode);
        }

        vnp_Params.put(PaymentParam.VNP_TXNREF.value(), vnpTxnRef);
        vnp_Params.put(PaymentParam.VNP_ORDERINFO.value(), "Thanh toan don hang: " + vnpTxnRef);
        vnp_Params.put(PaymentParam.VNP_ORDERTYPE.value(), vnpOrderType);
        vnp_Params.put(PaymentParam.VNP_LOCALE.value(),
                (language != null && !language.isEmpty()) ? language : "vn");

        vnp_Params.put(PaymentParam.VNP_RETURNURL.value(), buildReturnUrl(vnpTxnRef, "success"));
        vnp_Params.put(PaymentParam.VNP_IPADDR.value(), vnpIpAddr);

        // Generate datetime
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        String vnpCreateDate = formatter.format(cld.getTime());
        cld.add(Calendar.MINUTE, 15);
        String vnpExpireDate = formatter.format(cld.getTime());

        vnp_Params.put(PaymentParam.VNP_CREATDATE.value(), vnpCreateDate);
        vnp_Params.put(PaymentParam.VNP_EXPIREDATE.value(), vnpExpireDate);

        String queryString = buildInitPaymentUrl(vnp_Params);

        // Build payment URL
        return PayConfig.vnp_PayUrl + "?" + queryString;
    }

    public String buildInitPaymentUrl(Map<String, String> params) {
        // Sort field names
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        // Build hashData + query
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (Iterator<String> itr = fieldNames.iterator(); itr.hasNext();) {
            String fieldName = itr.next();
            String fieldValue = params.get(fieldName);

            if (fieldValue != null && !fieldValue.isEmpty()) {

                hashData.append(fieldName)
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                if (itr.hasNext()) {
                    hashData.append('&');
                    query.append('&');
                }
            }
        }

        // Generate secure hash
        String secureHash = sign(hashData.toString());
        
        query.append("&").append(PaymentParam.VNP_SECUREHASH.value()).append("=").append(secureHash);

        return query.toString();
    }

    private String sign(String data) {
        return PayConfig.hmacSHA512(PayConfig.secretKey, data.toString());
    } 

    private String buildReturnUrl(String txnRef, String statusRaw) {
        String s = PayConfig.vnp_ReturnUrl;
        return s.replace("{txnRef}", txnRef)
                .replace("{status}", statusRaw);
    }
}
