package com.huyntd.superapp.gundam_shop.controller;

import com.huyntd.superapp.gundam_shop.configuration.VnPayConfig;
import com.huyntd.superapp.gundam_shop.dto.request.PaymentRequest;
import com.huyntd.superapp.gundam_shop.dto.response.PaymentResponse;
import com.huyntd.superapp.gundam_shop.model.Payment;
import com.huyntd.superapp.gundam_shop.service.payment.PaymentService;
import com.huyntd.superapp.gundam_shop.service.payment.impl.MoMoService;
import com.huyntd.superapp.gundam_shop.service.payment.impl.VNPAYService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.huyntd.superapp.gundam_shop.service.payment.impl.VNPAYLibrary.hmacSHA512;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/payment")
public class PaymentController {
    PaymentService paymentService;
    VNPAYService  vNPAYService;
    MoMoService  moMoService;

    VnPayConfig  vnPayConfig;


    //---------------------------------------VNPAY------------------------------------------------------------------------
    @PostMapping("/vnpay-create-payment")
    public ResponseEntity<?> createVNPAYPayment(@RequestBody PaymentRequest paymentRequest) {
        long amount = (long) paymentRequest.getAmount();
        String vnpayUrl = vNPAYService.createPaymentUrl(amount, paymentRequest.getOrderId());
        return ResponseEntity.ok(Map.of("paymentUrl", vnpayUrl));
    }

    @GetMapping("/vnpay-return")
    public RedirectView handleVnPayReturn(@RequestParam Map<String, String> params) throws Exception {
        log.info("VNPay Return Params: {}", params);

        String vnp_SecureHash = params.get("vnp_SecureHash");
        if (vnp_SecureHash == null) {
            log.error("Missing vnp_SecureHash");
            return new RedirectView("vnPayConfig.getUrlFail()");
        }

        // --- B1: Lấy nguyên query string mà không sort ---
        StringBuilder hashData = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            if (key.equals("vnp_SecureHash") || key.equals("vnp_SecureHashType")) continue;

            String value = entry.getValue();
            if (value != null && !value.isEmpty()) {
                String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);
                if (!first) hashData.append("&");
                hashData.append(key).append("=").append(encodedValue);
                first = false;
            }
        }

        // --- B2: Tính chữ ký ---
        String calculatedHash = hmacSHA512(vnPayConfig.getVnpHashSecret(), hashData.toString());
        boolean isValid = calculatedHash.equalsIgnoreCase(vnp_SecureHash);

        log.info("HashData for verification: {}", hashData);
        log.info("Calculated Hash: {}", calculatedHash);
        log.info("Received Hash: {}", vnp_SecureHash);
        log.info("Signature valid: {}", isValid);

        String txnRef = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");
        String transactionStatus = params.get("vnp_TransactionStatus");

        try {
            String[] parts = txnRef.split("_");
            if (!isValid || !"00".equals(responseCode) || !"00".equals(transactionStatus)) {
                paymentService.updatePayment(Integer.parseInt(parts[1]), Integer.parseInt(parts[0]), "FAILED");
                return new RedirectView("vnPayConfig.getUrlFail()");
            }

            paymentService.updatePayment(Integer.parseInt(parts[1]), Integer.parseInt(parts[0]), "SUCCESS");
            return new RedirectView("https://www.youtube.com/watch?v=pN34FNbOKXc");

        } catch (Exception e) {
            log.error("Error processing VNPay return", e);
            return new RedirectView("vnPayConfig.getUrlFail()");
        }
    }



    //------------------------------------------------------MOMO---------------------------------------------------------
    @PostMapping("/momo-create-payment")
    public ResponseEntity<?> createMoMoPayment(@RequestBody PaymentRequest paymentRequest) throws Exception {
        long amount = (long) paymentRequest.getAmount();
        String momoUrl = moMoService.createPaymentUrl(amount, paymentRequest.getOrderId());
        return ResponseEntity.ok(Map.of("paymentUrl", momoUrl));
    }

    @GetMapping("/momo-return")
    public RedirectView handleMoMoReturn(@RequestParam Map<String, String> queryParams) {
        try {
            // ✅ Lấy tham số từ MoMo redirect về
            String partnerCode = queryParams.get("partnerCode");
            String paymentCode = queryParams.get("orderId");
            String requestId = queryParams.get("requestId");
            BigDecimal amount = new BigDecimal(queryParams.get("amount"));
            String orderInfo = queryParams.get("orderInfo");
            String orderType = queryParams.get("orderType");
            String transId = queryParams.get("transId");
            String resultCode = queryParams.get("resultCode");
            String message = queryParams.get("message");
            String payType = queryParams.get("payType");
            String signature = queryParams.get("signature");

            // ✅ Xử lý orderId tại khi create gán cho nó duy nhất
            var list = paymentCode.split("_");
            // ✅ Xử lý kết quả thanh toán
            if ("0".equals(resultCode) && "Successful.".equalsIgnoreCase(message)) {
                paymentService.updatePayment(Integer.parseInt(list[1]), Integer.parseInt(list[0]), "Success");
                return new RedirectView("https://www.youtube.com/watch?v=pN34FNbOKXc");
            } else {
                paymentService.updatePayment(Integer.parseInt(list[1]), Integer.parseInt(list[0]), "FAILED");
                return new RedirectView("https://yourdomain.com/payment/fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("https://yourdomain.com/payment/fail");
        }
    }


}
