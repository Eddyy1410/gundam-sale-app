package com.huyntd.superapp.gundam_shop.service.payment.impl;

import com.huyntd.superapp.gundam_shop.configuration.VnPayConfig;
import com.huyntd.superapp.gundam_shop.dto.request.PaymentRequest;
import com.huyntd.superapp.gundam_shop.service.payment.PaymentService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class VNPAYService {

    private final VnPayConfig vnPayConfig;
    PaymentService paymentService;

    public VNPAYService(VnPayConfig vnPayConfig, PaymentService paymentService) {
        this.vnPayConfig = vnPayConfig;

        this.paymentService = paymentService;
    }

    public String createPaymentUrl(long amount, int orderId) {
        try {
            // ✅ Tạo payment record
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setAmount(amount);
            paymentRequest.setOrderId(orderId);
            var payment = paymentService.createPayment(paymentRequest);

            // ✅ Dùng TreeMap để giữ thứ tự alphabet (VNPay yêu cầu)
            Map<String, String> vnpParams = new TreeMap<>();

            vnpParams.put("vnp_Version", vnPayConfig.getVnpVersion());  // "2.1.0"
            vnpParams.put("vnp_Command", vnPayConfig.getVnpCommand());  // "pay"
            vnpParams.put("vnp_TmnCode", vnPayConfig.getVnpTmnCode());  // TMN code của bạn
            vnpParams.put("vnp_Amount", String.valueOf(amount * 100));  // nhân 100 theo yêu cầu VNPay
            vnpParams.put("vnp_CurrCode", vnPayConfig.getVnpCurrCode()); // "VND"

            // ✅ Tạo mã giao dịch unique, tránh bị lỗi định dạng hoặc trùng
            // Format: <OrderId>_<PaymentId> (VD: 12_456)
            String vnpTxnRef = orderId + "_" + payment.getId();
            vnpParams.put("vnp_TxnRef", vnpTxnRef);
            vnpParams.put("vnp_OrderInfo", "Thanh toan don hang #" + orderId);
            vnpParams.put("vnp_OrderType", "other"); // hoặc "billpayment" đều hợp lệ
            vnpParams.put("vnp_Locale", vnPayConfig.getVnpLocale()); // "vn"
            vnpParams.put("vnp_ReturnUrl", vnPayConfig.getVnpReturnUrl());
            vnpParams.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
            vnpParams.put("vnp_IpAddr", "127.0.0.1");

            // ✅ Không bắt buộc, chỉ thêm khi test với BankCode cụ thể
            vnpParams.put("vnp_BankCode", "NCB");

            // ✅ Gọi hàm tạo URL đã hash ký tự
            return createRequestUrl(vnPayConfig.getVnpUrl(), vnPayConfig.getVnpHashSecret(), vnpParams);

        } catch (Exception e) {
            throw new RuntimeException("Error creating VNPay payment URL", e);
        }
    }

//    private String createRequestUrl(String baseUrl, String vnp_HashSecret, Map<String, String> params) throws Exception {
//        StringBuilder data = new StringBuilder();
//        StringBuilder query = new StringBuilder();
//
//        for (Map.Entry<String, String> entry : params.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//
//            if (value != null && !value.isEmpty()) {
//                // ✅ Encode key và value
//                String encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8.toString());
//                String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
//
//                data.append(encodedKey).append("=").append(encodedValue).append("&");
//                query.append(encodedKey).append("=").append(encodedValue).append("&");
//            }
//        }
//
//        // ✅ Xóa dấu '&' cuối cùng
//        if (data.length() > 0) {
//            data.deleteCharAt(data.length() - 1);
//            query.deleteCharAt(query.length() - 1);
//        }
//
//        // ✅ Tạo chữ ký HMAC SHA512
//        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, data.toString());
//
//        // ✅ Ghép URL cuối cùng
//        return baseUrl + "?" + query + "&vnp_SecureHash=" + vnp_SecureHash;
//    }
//
//
//    // --- Utility method to sign HMAC SHA512 ---
//    private String hmacSHA512(String key, String data) throws Exception {
//        Mac hmac = Mac.getInstance("HmacSHA512");
//        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
//        hmac.init(secretKey);
//        byte[] bytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
//
//        StringBuilder hash = new StringBuilder();
//        for (byte b : bytes) {
//            hash.append(String.format("%02x", b));
//        }
//        return hash.toString();
//    }

    public String createRequestUrl(String baseUrl, String vnp_HashSecret, Map<String, String> params) throws Exception {
        StringBuilder query = new StringBuilder();
        StringBuilder hashData = new StringBuilder();

        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value != null && !value.isEmpty()) {
                String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8.toString());

                // Build query string
                if (!first) {
                    query.append("&");
                    hashData.append("&");
                }
                query.append(key).append("=").append(encodedValue);
                hashData.append(key).append("=").append(encodedValue);
                first = false;
            }
        }

        // Tạo chữ ký
        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());

        return baseUrl + "?" + query.toString() + "&vnp_SecureHash=" + vnp_SecureHash;
    }

    private static String hmacSHA512(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac.init(secretKeySpec);
            byte[] bytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error calculating HMAC SHA512", e);
        }
    }

}
