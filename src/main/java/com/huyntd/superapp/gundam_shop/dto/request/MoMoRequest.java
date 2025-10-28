package com.huyntd.superapp.gundam_shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoMoRequest {
    private String orderInfo;
    private String partnerCode;
    private String redirectUrl;
    private String ipnUrl;
    private long amount;
    private String orderId;
    private String requestId;
    private String extraData;
    private String partnerName;
    private String storeId;
    private String requestType;
    private String orderGroupId;
    private boolean autoCapture;
    private String lang;
    private String signature;
}
