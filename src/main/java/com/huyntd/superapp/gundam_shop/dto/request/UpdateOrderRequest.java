package com.huyntd.superapp.gundam_shop.dto.request;

import com.huyntd.superapp.gundam_shop.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequest{
    private String orderStatus;
    private BigDecimal totalPrice;
    private String paymentMethod;
    private String billingAddress;
}
