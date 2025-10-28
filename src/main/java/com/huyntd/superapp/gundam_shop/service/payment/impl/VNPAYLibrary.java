package com.huyntd.superapp.gundam_shop.service.payment.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class VNPAYLibrary {

    public static final String VERSION = "2.1.0";
    private final SortedMap<String, String> requestData = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final SortedMap<String, String> responseData = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    // Thêm dữ liệu gửi lên VNPAY
    public void addRequestData(String key, String value) {
        if (value != null && !value.isEmpty()) {
            requestData.put(key, value);
        }
    }

    // Thêm dữ liệu phản hồi từ VNPAY
    public void addResponseData(String key, String value) {
        if (value != null && !value.isEmpty()) {
            responseData.put(key, value);
        }
    }

    // Lấy giá trị phản hồi
    public String getResponseData(String key) {
        return responseData.getOrDefault(key, "");
    }

    // ------------------ CREATE PAYMENT URL ------------------
    public String createRequestUrl(String baseUrl, String vnp_HashSecret) throws Exception {
        StringBuilder data = new StringBuilder();

        for (Map.Entry<String, String> entry : requestData.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                data.append(urlEncode(entry.getKey()))
                        .append("=")
                        .append(urlEncode(entry.getValue()))
                        .append("&");
            }
        }

        String queryString = data.toString();
        String signData = queryString;

        if (signData.length() > 0) {
            signData = signData.substring(0, signData.length() - 1); // bỏ ký tự '&' cuối
        }

        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, signData);
        return baseUrl + "?" + queryString + "vnp_SecureHash=" + vnp_SecureHash;
    }

    // ------------------ VALIDATE SIGNATURE ------------------
    public boolean validateSignature(String inputHash, String secretKey) throws Exception {
        String rspRaw = getResponseDataForSign();
        String myChecksum = hmacSHA512(secretKey, rspRaw);
        return myChecksum.equalsIgnoreCase(inputHash);
    }

    // ------------------ PRIVATE HELPERS ------------------
    private String getResponseDataForSign() throws UnsupportedEncodingException {
        if (responseData.containsKey("vnp_SecureHashType")) {
            responseData.remove("vnp_SecureHashType");
        }
        if (responseData.containsKey("vnp_SecureHash")) {
            responseData.remove("vnp_SecureHash");
        }

        StringBuilder data = new StringBuilder();
        for (Map.Entry<String, String> entry : responseData.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                data.append(urlEncode(entry.getKey()))
                        .append("=")
                        .append(urlEncode(entry.getValue()))
                        .append("&");
            }
        }

        if (data.length() > 0) {
            data.deleteCharAt(data.length() - 1);
        }
        return data.toString();
    }

    // ------------------ UTILITIES ------------------
    public static String hmacSHA512(String key, String data) throws Exception {
        Mac hmac512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmac512.init(secretKey);
        byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder hash = new StringBuilder();
        for (byte b : bytes) {
            hash.append(String.format("%02x", b));
        }
        return hash.toString();
    }

    private static String urlEncode(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }
}

