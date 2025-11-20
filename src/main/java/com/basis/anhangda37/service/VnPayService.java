package com.basis.anhangda37.service;

import java.net.URLEncoder;
import java.io.ObjectInputFilter.Config;
import java.net.URLDecoder; // Đảm bảo import này
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime; // THÊM IMPORT NÀY
import java.time.format.DateTimeFormatter; // THÊM IMPORT NÀY
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.remote.server.HttpHeaderAccessManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.basis.anhangda37.config.PayConfig;
import com.basis.anhangda37.domain.Order;
import com.basis.anhangda37.domain.Payment;
import com.basis.anhangda37.domain.dto.OrderRequestDto;
import com.basis.anhangda37.domain.dto.payment.InitPaymentDto;
import com.basis.anhangda37.domain.dto.payment.PaymentCallBackData;
import com.basis.anhangda37.domain.dto.payment.PaymentResponseDto;
import com.basis.anhangda37.domain.enums.OrderStatus;
import com.basis.anhangda37.domain.enums.PaymentParam;
import com.basis.anhangda37.domain.enums.PaymentStatus;
import com.basis.anhangda37.exception.NotFoundEntityException;
import com.basis.anhangda37.exception.payment.SignNotVerifyException;
import com.basis.anhangda37.repository.OrderRepository;
import com.basis.anhangda37.repository.PaymentRepository;
import com.basis.anhangda37.service.interf.PaymentService;

import jakarta.transaction.Transactional;

@Service
public class VnPayService implements PaymentService {

    private final RestTemplate restTemplate;

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public VnPayService(PaymentRepository paymentRepository, RestTemplate restTemplate,
            OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.restTemplate = restTemplate;
    }

    public Payment getPaymentByTxnRef(String vnp_TxnRef) {
        return paymentRepository.findByVnpTxnRef(vnp_TxnRef).get();
    }

    public void updatePayStatus(Long id, PaymentStatus status) {
        Payment oldPayment = paymentRepository.findById(id).get();
        if (oldPayment.getStatus().equals(status))
            return;
        oldPayment.setStatus(status);
        paymentRepository.save(oldPayment);
    }

    public String initPayment(OrderRequestDto orderRequest) {

        // ... (Code của bạn vẫn giữ nguyên) ...
        Long orderId = orderRequest.getOrderId();
        String bankCode = orderRequest.getBankCode();
        String language = orderRequest.getLanguage();
        Double amount = orderRequest.getTotalPayment();
        String ipAddress = orderRequest.getIpAddress();

        // Required values
        String vnpVersion = "2.1.0";
        String vnpCommand = "pay";
        String vnpOrderType = "other";
        long amountVnp = Math.round(amount) * 100;

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

    /*
     * Kiểm tra đơn hàng đã được thanh toán hay chưa dựa trên API query gửi lên
     * meichant
     */
    @Transactional
    public String checkOrderPaymentStatus(Long orderId, String ipAddress) {
        Order order;
        try {
            order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Order với ID: " + orderId));
        } catch (Exception e) {
            // Nếu không tìm thấy Order ID
            return PayConfig.REDIRECT_FAILED_URL + "0"; // Trả về trang thất bại chung
        }

        Payment payment = order.getPayment();
        if (payment == null || payment.getVnpTxnRef() == null || payment.getCreatedAt() == null) {
            // Đơn hàng không có thông tin thanh toán -> Lỗi
            return PayConfig.REDIRECT_FAILED_URL + orderId;
        }

        // 1. Gọi VNPAY Service để truy vấn
        Map<String, Object> vnpayResponse;
        try {
            vnpayResponse = queryTransaction(
                    payment.getVnpTxnRef(),
                    payment.getCreatedAt(),
                    ipAddress);
        } catch (Exception e) {
            // Lỗi khi gọi VNPAY (vd: timeout, sai chữ ký)
            System.err.println("Lỗi VNPAY Query: " + e.getMessage());
            // Tạm trả về trang pending, vì chúng ta chưa biết chắc trạng thái
            return PayConfig.REDIRECT_PENDING_URL + orderId;
        }

        // 2. Phân tích kết quả
        String vnp_ResponseCode = (String) vnpayResponse.get("vnp_ResponseCode");
        String vnp_TransactionStatus = (String) vnpayResponse.get("vnp_TransactionStatus");

        // Kiểm tra xem lệnh QueryDr có thành công không
        if (!"00".equals(vnp_ResponseCode)) {
            // Lỗi từ VNPAY (vd: 91: Không tìm thấy giao dịch)
            String message = (String) vnpayResponse.get("vnp_Message");
            System.err.println("Lỗi VNPAY Query (vnp_ResponseCode != 00): " + message);

            // Không tìm thấy giao dịch -> coi như thất bại
            return PayConfig.REDIRECT_FAILED_URL + orderId;
        }

        // 3. Cập nhật trạng thái Payment và Order dựa trên vnp_TransactionStatus
        // và quyết định URL redirect
        String redirectUrl;

        switch (vnp_TransactionStatus) {
            case "00": // Giao dịch thanh toán thành công
                payment.setStatus(PaymentStatus.SUCCESS);
                order.setStatus(OrderStatus.SHIPPING); // Hoặc PAID, SUCCESS tùy logic
                redirectUrl = PayConfig.REDIRECT_SUCCESS_URL + orderId;
                break;

            case "01": // Giao dịch chưa hoàn tất
                payment.setStatus(PaymentStatus.PENDING);
                order.setStatus(OrderStatus.PENDING);
                redirectUrl = PayConfig.REDIRECT_PENDING_URL + orderId;
                break;

            default: // 02, 04, 05, 06, 07, 09... -> Giao dịch lỗi/bị từ chối/hoàn
                payment.setStatus(PaymentStatus.FAIL);
                order.setStatus(OrderStatus.CANCEL);
                redirectUrl = PayConfig.REDIRECT_FAILED_URL + orderId;
                break;
        }

        // 4. Lưu thay đổi vào CSDL
        paymentRepository.save(payment);
        orderRepository.save(order);

        // 5. Trả về URL
        return redirectUrl;
    }

    public Map<String, Object> queryTransaction(String vnp_TxnRef, LocalDateTime transactionDate, String vnp_IpAddr)
            throws Exception {

        // 1. Chuẩn bị các tham số yêu cầu
        String vnp_RequestId = PayConfig.getRandomNumber(8); // Mã RequestId duy nhất
        String vnp_Version = "2.1.0";
        String vnp_Command = "querydr";
        String vnp_TmnCode = PayConfig.vnp_TmnCode;
        String vnp_TransactionDate = transactionDate.format(PayConfig.VNPAY_DATE_FORMATTER);
        String vnp_CreateDate = LocalDateTime.now(TimeZone.getTimeZone("Etc/GMT+7").toZoneId())
                .format(PayConfig.VNPAY_DATE_FORMATTER);
        String vnp_OrderInfo = "Truy van giao dich " + vnp_TxnRef;

        // 2. Tạo chuỗi dữ liệu để băm (Hash)
        // Quy tắc: vnp_RequestId + "|" + vnp_Version + "|" + vnp_Command + "|" +
        // vnp_TmnCode + "|" + vnp_TxnRef + "|" + vnp_TransactionDate + "|" +
        // vnp_CreateDate + "|" + vnp_IpAddr + "|" + vnp_OrderInfo
        String hashData = Stream.of(
                vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode,
                vnp_TxnRef, vnp_TransactionDate, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo)
                .collect(Collectors.joining("|"));

        String vnp_SecureHash = PayConfig.hmacSHA512(PayConfig.secretKey, hashData);

        // 3. Tạo Request Body (JSON)
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("vnp_RequestId", vnp_RequestId);
        requestBody.put("vnp_Version", vnp_Version);
        requestBody.put("vnp_Command", vnp_Command);
        requestBody.put("vnp_TmnCode", vnp_TmnCode);
        requestBody.put("vnp_TxnRef", vnp_TxnRef);
        requestBody.put("vnp_OrderInfo", vnp_OrderInfo);
        requestBody.put("vnp_TransactionDate", vnp_TransactionDate);
        requestBody.put("vnp_CreateDate", vnp_CreateDate);
        requestBody.put("vnp_IpAddr", vnp_IpAddr);
        requestBody.put("vnp_SecureHash", vnp_SecureHash);

        // 4. Gửi yêu cầu POST
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(PayConfig.vnp_ApiUrl, entity, Map.class);

        Map<String, Object> responseBody = responseEntity.getBody();

        if (responseBody == null) {
            throw new Exception("Lỗi khi truy vấn VNPAY: Response rỗng");
        }

        // 5. Xác thực chữ ký của VNPAY trả về (Rất quan trọng)
        String vnp_SecureHash_Response = (String) responseBody.get("vnp_SecureHash");

        // Xóa hash ra khỏi map để chuẩn bị hash lại
        responseBody.remove("vnp_SecureHash");

        // Dữ liệu hash trả về (theo tài liệu)
        String responseHashData = Stream.of(
                getField(responseBody, "vnp_ResponseId"),
                getField(responseBody, "vnp_Command"),
                getField(responseBody, "vnp_ResponseCode"),
                getField(responseBody, "vnp_Message"),
                getField(responseBody, "vnp_TmnCode"),
                getField(responseBody, "vnp_TxnRef"),
                getField(responseBody, "vnp_Amount"),
                getField(responseBody, "vnp_BankCode"),
                getField(responseBody, "vnp_PayDate"),
                getField(responseBody, "vnp_TransactionNo"),
                getField(responseBody, "vnp_TransactionType"),
                getField(responseBody, "vnp_TransactionStatus"),
                getField(responseBody, "vnp_OrderInfo"),
                getField(responseBody, "vnp_PromotionCode"),
                getField(responseBody, "vnp_PromotionAmount")).collect(Collectors.joining("|"));

        String myHash = PayConfig.hmacSHA512(PayConfig.secretKey, responseHashData);

        if (!myHash.equals(vnp_SecureHash_Response)) {
            throw new Exception("Xác thực chữ ký VNPAY thất bại!");
        }

        return responseBody;
    }

    public void hanlderVnPayReturnAfterSendPaymentUrl(PaymentCallBackData paymentCallBackData)
            throws SignNotVerifyException {
        // Bước 1: Lấy toàn bộ tham số trả về
        Map<String, String> fields = paymentCallBackData.getParams();

        // Bước 2: Tách chuỗi secure hash
        String vnp_SecureHash = fields.get("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        // Kiểm tra sự nhất quán dữ liệu
        if (verifySign(fields, vnp_SecureHash)) {
            System.out.println("Dũ liệu nhất quán");
            // response.sendRedirect("/thanks");
            String vnp_ResponseCode = fields.get("vnp_ResponseCode");
            if (vnp_ResponseCode.equals("00")) {
                String vnp_TxnRef = fields.get("vnp_TxnRef");
                Payment payment = getPaymentByTxnRef(vnp_TxnRef);
                updatePayStatus(payment.getId(), PaymentStatus.SUCCESS);
                Order order = orderRepository.findByPayment(payment).get();
                order.isShipping();
                orderRepository.save(order);
            } else {
                String vnp_TxnRef = fields.get("vnp_TxnRef");
                Payment payment = getPaymentByTxnRef(vnp_TxnRef);
                updatePayStatus(payment.getId(), PaymentStatus.FAIL);
                Order order = orderRepository.findByPayment(payment).get();
                order.isPending();
                orderRepository.save(order);
            }
        } else {
            System.out.println("Không trùng chữ kí");
            String vnp_TxnRef = fields.get("vnp_TxnRef");
            Payment payment = getPaymentByTxnRef(vnp_TxnRef);
            updatePayStatus(payment.getId(), PaymentStatus.PENDING);
            Order order = orderRepository.findByPayment(payment).get();
            order.isPending();
            orderRepository.save(order);
            throw new SignNotVerifyException("Url trả về không hợp lệ: Không trùng chữ kí");
        }
    }

    private boolean verifySign(Map<String, String> fields, String vnp_SecureHash) {
        String signValue = PayConfig.hashAllFields(fields);
        return signValue.equals(vnp_SecureHash);
    }

    /**
     * Helper để lấy giá trị từ map, trả về chuỗi rỗng nếu null
     */
    private String getField(Map<String, Object> map, String key) {
        return String.valueOf(map.getOrDefault(key, ""));
    }

    // HÀM HELPER CHUNG ĐỂ PARSE URL
    private static String getParamFromUrl(String paymentUrl, String paramName) {
        if (paymentUrl == null || !paymentUrl.contains(paramName)) {
            return null;
        }
        try {
            String[] parts = paymentUrl.split("\\?");
            if (parts.length < 2)
                return null;

            String queryString = parts[1];
            String[] params = queryString.split("&");

            for (String p : params) {
                if (p.startsWith(paramName + "=")) {
                    String encodedValue = p.substring(paramName.length() + 1);
                    // Sửa lỗi: Phải giải mã bằng US_ASCII giống như lúc mã hóa
                    return URLDecoder.decode(encodedValue, StandardCharsets.US_ASCII.name());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // SỬA LẠI HÀM NÀY: Dùng helper chung cho sạch
    public static String getReturnUrl(String paymentUrl) {
        return getParamFromUrl(paymentUrl, PaymentParam.VNP_RETURNURL.value());
    }

    // THÊM CÁC HÀM HELPER MỚI:
    public static String getTxnRefFromUrl(String paymentUrl) {
        return getParamFromUrl(paymentUrl, PaymentParam.VNP_TXNREF.value());
    }

    public static Long getAmountFromUrl(String paymentUrl) {
        String amountStr = getParamFromUrl(paymentUrl, PaymentParam.VNP_AMOUNT.value());
        if (amountStr != null) {
            try {
                return Long.parseLong(amountStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static LocalDateTime getCreateDateFromUrl(String paymentUrl) {
        String dateStr = getParamFromUrl(paymentUrl, PaymentParam.VNP_CREATDATE.value());
        if (dateStr != null) {
            try {
                // VNPAY format: yyyyMMddHHmmss
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                return LocalDateTime.parse(dateStr, formatter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String buildInitPaymentUrl(Map<String, String> params) {
        // ... (Code của bạn vẫn giữ nguyên) ...
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);
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
        String secureHash = sign(hashData.toString());
        query.append("&").append(PaymentParam.VNP_SECUREHASH.value()).append("=").append(secureHash);
        return query.toString();
    }

    private String sign(String data) {
        // ... (Code của bạn vẫn giữ nguyên) ...
        return PayConfig.hmacSHA512(PayConfig.secretKey, data.toString());
    }

    private String buildReturnUrl(String txnRef, String statusRaw) {
        // ... (Code của bạn vẫn giữ nguyên) ...
        String s = PayConfig.vnp_ReturnUrl;
        return s.replace("{txnRef}", txnRef)
                .replace("{status}", statusRaw);
    }
}