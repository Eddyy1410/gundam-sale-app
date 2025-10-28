package com.huyntd.superapp.gundam_shop.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VnPayConfig {

    @Value("${vnpay.tmnCode}")
    private String vnpTmnCode;

    @Value("${vnpay.hashSecret}")
    private String vnpHashSecret;

    @Value("${vnpay.url}")
    private String vnpUrl;

    @Value("${vnpay.returnUrl}")
    private String vnpReturnUrl;

    @Value("${vnpay.version}")
    private String vnpVersion;

    @Value("${vnpay.command}")
    private String vnpCommand;

    @Value("${vnpay.locale}")
    private String vnpLocale;

    @Value("${vnpay.currCode}")
    private String vnpCurrCode;

    // --- Getters ---
    public String getVnpTmnCode() { return vnpTmnCode; }
    public String getVnpHashSecret() { return vnpHashSecret; }
    public String getVnpUrl() { return vnpUrl; }
    public String getVnpReturnUrl() { return vnpReturnUrl; }
    public String getVnpVersion() { return vnpVersion; }
    public String getVnpCommand() { return vnpCommand; }
    public String getVnpLocale() { return vnpLocale; }
    public String getVnpCurrCode() { return vnpCurrCode; }
}
