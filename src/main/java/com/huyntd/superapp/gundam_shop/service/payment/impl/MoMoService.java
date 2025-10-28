package com.huyntd.superapp.gundam_shop.service.payment.impl;

import com.huyntd.superapp.gundam_shop.configuration.MoMoConfig;
import com.huyntd.superapp.gundam_shop.dto.request.MoMoRequest;
import com.huyntd.superapp.gundam_shop.dto.request.PaymentRequest;
import com.huyntd.superapp.gundam_shop.service.payment.PaymentService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class MoMoService {

    private MoMoConfig momoConfig;

    PaymentService paymentService;

    public MoMoService(MoMoConfig momoConfig, PaymentService paymentService) {
        this.momoConfig = momoConfig;
        this.paymentService = paymentService;
    }

    public String createPaymentUrl(long amount, int orderId) throws Exception {

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(amount);
        paymentRequest.setOrderId(orderId);
        var payment = paymentService.createPayment(paymentRequest);
        String uniqueOrderId = orderId + "_" + payment.getId() + "_" + System.currentTimeMillis();

        String requestId = UUID.randomUUID().toString();
        String extraData = "";
        String orderInfo = "Thanh toán đơn hàng";
        // ✅ Tạo rawSignature đúng chuẩn
        String rawSignature = String.format(
                "accessKey=%s&amount=%d&extraData=%s&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=%s",
                momoConfig.getAccessKey(), amount, extraData, momoConfig.getIpnUrl(),
                uniqueOrderId, orderInfo, momoConfig.getPartnerCode(), momoConfig.getRedirectUrl(),
                requestId, "payWithATM"
        );

        // ✅ Ký bằng secretKey
        String signature = sign(rawSignature, momoConfig.getSecretKey());

        // ✅ Gán thông tin vào request
        MoMoRequest request = new MoMoRequest();
        request.setPartnerCode(momoConfig.getPartnerCode());
        request.setPartnerName("MoMo Payment");
        request.setStoreId("Demo Store");
        request.setOrderId(uniqueOrderId);
        request.setRequestId(requestId);
        request.setAmount(amount);
        request.setOrderInfo("Thanh toán đơn hàng");
        request.setRedirectUrl(momoConfig.getRedirectUrl());
        request.setIpnUrl(momoConfig.getIpnUrl());
        request.setLang("vi");
        request.setExtraData(extraData);
        request.setAutoCapture(true);
        request.setRequestType("payWithATM");
        request.setSignature(signature);

        // ✅ Gửi request đến MoMo
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MoMoRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(momoConfig.getEndpoint(), entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Object payUrl = response.getBody().get("payUrl");
            if (payUrl != null) {
                return payUrl.toString();
            }
        }

        System.out.println("MoMo response: " + response.getBody());


        throw new RuntimeException("Không thể tạo URL thanh toán MoMo");
    }

    public static String sign(String data, String secretKey) throws Exception {
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSHA256.init(secretKeySpec);
        byte[] hashBytes = hmacSHA256.doFinal(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b)); // lowercase HEX
        }
        return sb.toString();
    }

}



